"""苍穹表单协议回放器 - 核心库

设计目标：可移植（不依赖项目其他模块）、可扩展（便利方法易改）、健壮（分级异常）。

核心能力：
- login(base_url, user, password, dc_id) → CosmicSession
- replay = CosmicFormReplay(session)
- replay.init_root()                          # 会话根 pageId
- replay.open_form(form_id, app_id) → pageId  # 为表单申请 pageId
- replay.invoke(form_id, app_id, ac, actions) → list  # 调 batchInvokeAction
- 自动追踪响应里下发的新 pageId
- 通过 diagnoser.extract_operation_result(resp, replay) 可拉取 bos_operationresult 错误

协议假设：batchInvokeAction.do 可绕过 signature（SIT 实测；其他环境走签名路径）
签名算法（SIT 之外需开启）：
  SHA256(ts + csrf_token + diff_time + params_str) + diff_time + __length__ + len(params_str)
  （来自 commonsrc.52aaf349.js 逆向，双样本验证 2026-03-25）
"""
from __future__ import annotations

import hashlib
import json
import logging
import os
import re
import subprocess
import sys
import time
import uuid
import urllib.parse
from dataclasses import dataclass
from pathlib import Path
from typing import Any

import urllib3
urllib3.disable_warnings()
import requests


log = logging.getLogger("cosmic_replay")


# =============================================================
# 异常体系
# =============================================================
class CosmicError(Exception):
    """通用苍穹协议错误"""


class LoginError(CosmicError):
    """登录失败（网络 / 凭证 / 数据中心）"""


class ProtocolError(CosmicError):
    """协议层错误（HTTP 非 200、JSON 解析失败、pageId 缺失等）"""


class BusinessError(CosmicError):
    """业务层错误（服务端 showErrMsg / showMessage 明确反馈）"""
    def __init__(self, msg: str, raw_response: Any = None):
        super().__init__(msg)
        self.raw_response = raw_response


# =============================================================
# 定位 cosmic-login skill（可移植：搜几个常见路径）
# =============================================================
def _find_login_script() -> Path:
    """查找 cosmic-login skill 脚本路径（支持多种部署布局）"""
    # 环境变量显式指定优先
    env = os.environ.get("COSMIC_LOGIN_SCRIPT")
    if env and Path(env).exists():
        return Path(env)

    # 本 skill 同级目录往上找 .claude/skills/cosmic-login/cosmic_login.py
    here = Path(__file__).resolve()
    for parent in [here.parent.parent.parent, here.parent.parent, here.parent.parent.parent.parent]:
        p = parent / "cosmic-login" / "cosmic_login.py"
        if p.exists():
            return p
        # 也可能和 cosmic-replay 同目录
        p2 = parent / ".claude" / "skills" / "cosmic-login" / "cosmic_login.py"
        if p2.exists():
            return p2

    raise FileNotFoundError(
        "找不到 cosmic-login skill。请设置 COSMIC_LOGIN_SCRIPT 环境变量指向 cosmic_login.py"
    )


# =============================================================
# 会话
# =============================================================
@dataclass
class CosmicSession:
    base_url: str
    cookie: str
    user_id: str                 # "accountId_userId"
    account_id: str
    csrf_token: str = ""
    diff_time: str = "0"
    root_base_id: str = ""
    root_page_id: str = ""

    def sign(self, params_str: str, ts: str) -> str:
        """签名算法（部分环境必需）。"""
        s = ts + self.csrf_token + self.diff_time + params_str
        h = hashlib.sha256(s.encode("utf-8")).hexdigest()
        return h + self.diff_time + "__length__" + str(len(params_str))

    def base_headers(self, cqappid: str = "bos") -> dict:
        return {
            "Cookie": self.cookie,
            "userId": self.user_id,
            "client-start-time": str(int(time.time() * 1000)),
            "traceId": uuid.uuid4().hex[:16],
            "cqappid": cqappid,
            "ajax": "true",
            "kd-client-type": "web",
            "X-Requested-With": "XMLHttpRequest",
            **({"kd-csrf-token": self.csrf_token} if self.csrf_token else {}),
        }


# =============================================================
# 登录（调用 cosmic-login skill）
# =============================================================
def login(base_url: str, username: str, password: str,
          datacenter_id: str | None = None,
          retries: int = 3, retry_wait: float = 3.0) -> CosmicSession:
    """登录苍穹。失败时自动重试 getPublicKey 网关抖动。"""
    script = _find_login_script()
    args = [str(script), base_url, username, password]
    if datacenter_id:
        args.append(datacenter_id)

    last_err = None
    for attempt in range(1, retries + 1):
        try:
            result = subprocess.run(
                [sys.executable, *args],
                capture_output=True, text=True, timeout=60
            )
            out = result.stdout or ""
            if "LOGIN_SUCCESS" in out:
                m_cookie = re.search(r"^COOKIE=(.+)$", out, re.M)
                m_acct = re.search(r"^ACCOUNT_ID=(.+)$", out, re.M)
                m_user = re.search(r"^USER_ID=(.+)$", out, re.M)
                m_csrf = re.search(r"^CSRF_TOKEN=(.*)$", out, re.M)
                if not (m_cookie and m_acct and m_user):
                    raise LoginError(f"Login output missing fields:\n{out}")
                return CosmicSession(
                    base_url=base_url.rstrip("/"),
                    cookie=m_cookie.group(1).strip(),
                    account_id=m_acct.group(1).strip(),
                    user_id=f"{m_acct.group(1).strip()}_{m_user.group(1).strip()}",
                    csrf_token=(m_csrf.group(1).strip() if m_csrf else ""),
                )
            last_err = LoginError(f"Login failed: {out}\n{result.stderr}")
            log.warning(f"Login attempt {attempt}/{retries} failed: {out.strip().splitlines()[-1] if out.strip() else 'no output'}")
        except subprocess.TimeoutExpired as e:
            last_err = LoginError(f"Login timed out: {e}")
            log.warning(f"Login attempt {attempt}/{retries} timeout")

        if attempt < retries:
            time.sleep(retry_wait)

    raise last_err or LoginError("Login failed after retries")


# =============================================================
# 回放器
# =============================================================
class CosmicFormReplay:
    """会话级表单协议回放器。一个实例 = 一次登录的窗口。"""

    # 响应里这些字段认作 pageId，自动收集
    PAGEID_FIELD_NAMES = ("pageId", "parentPageId")

    def __init__(self, session: CosmicSession, sign_required: bool = True,
                 timeout: int = 30):
        """
        sign_required: 是否发送 signature 头。SIT 环境可设 False；其他环境保持 True
        """
        self.s = session
        self.sign_required = sign_required
        self.timeout = timeout
        self.http = requests.Session()
        self.http.verify = False
        # form_id → pageId 映射
        self.page_ids: dict[str, str] = {}
        # 历史响应存档（调试用）
        self.last_response: Any = None
        # menuItemClick 响应里下发的 tab pageId，等下一次 open_form / invoke 消费
        self._pending_tab_page_id: str | None = None

    # ---------- HTTP 低层 ----------

    def _post(self, path: str, body_urlenc: str, cqappid: str,
              extra_headers: dict | None = None) -> requests.Response:
        headers = self.s.base_headers(cqappid=cqappid)
        headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        if extra_headers:
            headers.update(extra_headers)
        url = self.s.base_url + path
        r = self.http.post(url, data=body_urlenc, headers=headers, timeout=self.timeout)
        return r

    def _get(self, path: str, params: dict, cqappid: str = "bos") -> requests.Response:
        headers = self.s.base_headers(cqappid=cqappid)
        headers["Content-Type"] = "application/json;charset=utf-8"
        url = self.s.base_url + path
        return self.http.get(url, params=params, headers=headers, timeout=self.timeout)

    # ---------- 会话初始化 ----------

    def init_root(self) -> str:
        """拉取会话根 pageId（从 getConfig.do GET 返回的 pageId 字段取）"""
        flag = uuid.uuid4().hex[:16]
        f_val = uuid.uuid4().hex[:18]
        r = self._get("/form/getConfig.do", {
            "params": json.dumps({"formId": "home_page", "flag": flag, "f": f_val},
                                 separators=(",", ":")),
            "random": "0.5",
        }, cqappid="bos")
        if r.status_code != 200:
            raise ProtocolError(f"init_root HTTP {r.status_code}: {r.text[:200]}")
        try:
            j = r.json()
        except Exception as e:
            raise ProtocolError(f"init_root bad json: {e}")
        page_id = j.get("pageId")
        if not page_id:
            raise ProtocolError(f"init_root no pageId, resp keys={list(j.keys())}")
        self.s.root_page_id = page_id
        m = re.match(r"^root([a-f0-9]{32})$", page_id)
        if m:
            self.s.root_base_id = m.group(1)
        self.page_ids["home_page"] = page_id
        return page_id

    def open_form(self, form_id: str, app_id: str,
                  parent_page_id: str | None = None,
                  lazy: bool = True) -> str:
        """为指定表单申请 pageId（用 parentPageId，适合普通业务子表单）。

        ⚠ 打开 portal 首页类表单（bos_portal_myapp_new 等）请改用 open_portal —
        这类表单服务端要求 rootPageId 参数，用 parentPageId 拿回来的 pid 是空壳。

        lazy=True（默认）：若 self.page_ids 已有该 form 的 pageId（可能来自
        menuItemClick 响应 harvest），直接返回，不调 getConfig。
        """
        if lazy and form_id in self.page_ids:
            return self.page_ids[form_id]
        if parent_page_id is None:
            parent_page_id = self.s.root_page_id
        params_obj = {
            "formId": form_id,
            "flag": uuid.uuid4().hex[:16],
            "f": uuid.uuid4().hex[:18],
        }
        if parent_page_id:
            params_obj["parentPageId"] = parent_page_id
        r = self._get("/form/getConfig.do", {
            "params": json.dumps(params_obj, separators=(",", ":")),
            "random": "0.5",
        }, cqappid=app_id)
        if r.status_code != 200:
            raise ProtocolError(f"open_form({form_id}) HTTP {r.status_code}: {r.text[:200]}")
        try:
            j = r.json()
        except Exception as e:
            raise ProtocolError(f"open_form({form_id}) bad json: {e}")
        # 服务端偶尔返回 list（如跨应用打开）—— 取第一个 dict 元素
        if isinstance(j, list):
            for item in j:
                if isinstance(item, dict) and "pageId" in item:
                    j = item
                    break
            else:
                raise ProtocolError(f"open_form({form_id}) got list without pageId: {str(j)[:200]}")
        page_id = j.get("pageId")
        if not page_id:
            raise ProtocolError(f"open_form({form_id}) no pageId in resp: {str(j)[:200]}")
        self.page_ids[form_id] = page_id
        return page_id

    def open_portal(self, form_id: str, app_id: str = "bos",
                    lazy: bool = True) -> str:
        """打开门户类表单（如 bos_portal_myapp_new）。

        关键差异：用 rootPageId 而不是 parentPageId；返回 32-hex 随机 pid。
        用 parentPageId 拿到的 pid 服务端没挂 Model，后续 menuItemClick 会返回空 []。
        2026-04-23 实证。
        """
        if lazy and form_id in self.page_ids:
            return self.page_ids[form_id]
        if not self.s.root_page_id:
            raise ProtocolError("open_portal needs root_page_id - call init_root() first")
        params_obj = {
            "formId": form_id,
            "rootPageId": self.s.root_page_id,
            "flag": uuid.uuid4().hex[:16],
            "f": uuid.uuid4().hex[:18],
        }
        r = self._get("/form/getConfig.do", {
            "params": json.dumps(params_obj, separators=(",", ":")),
            "random": "0.5",
        }, cqappid=app_id)
        if r.status_code != 200:
            raise ProtocolError(f"open_portal({form_id}) HTTP {r.status_code}: {r.text[:200]}")
        try:
            j = r.json()
        except Exception as e:
            raise ProtocolError(f"open_portal({form_id}) bad json: {e}")
        page_id = j.get("pageId")
        if not page_id:
            raise ProtocolError(f"open_portal({form_id}) no pageId in resp")
        self.page_ids[form_id] = page_id
        return page_id

    def click_menu(self, menu_id: str, cloud_id: str, menu_app_id: str,
                   target_form: str | None = None,
                   portal_form: str = "bos_portal_myapp_new",
                   portal_app: str = "bos") -> dict:
        """点击左侧菜单项 - 封装 open_portal + menuItemClick。

        菜单点击是"四层 pageId"里 L1→L2 的跃迁：服务端通过 addVirtualTab 下发
        两个新 pageId（apphome + list），我们通过 _harvest_page_ids 自动收下，
        后续业务请求就能按 formId 拿到正确的 L2 pageId 了。

        ⚠ 不是所有 form 都会被 harvest：click_menu 响应里下发的是"菜单挂着的 form"
        （如 haos_adminorgtablist），但用户真正操作的是主表单 haos_adminorgdetail。
        所以需要显式告知 target_form，我们按 {menuId}root{baseId} 公式算出 L2 pid
        并登记到 page_ids[target_form]。

        Args:
            menu_id: 菜单项主键（从菜单元数据查，跨环境语义等价）
            cloud_id: 云 id（如 "0MUWQ6HSY5JA" 人力云）
            menu_app_id: 菜单绑定的应用 id（如 "217WYC/L9U7E"）
            target_form: 点菜单后要操作的主表单 formId（默认 None = 自动 harvest）
        Returns:
            menuItemClick 的完整响应
        """
        portal_pid = self.open_portal(portal_form, portal_app, lazy=True)
        actions = [{
            "key": "appnavigationmenuap",
            "methodName": "menuItemClick",
            "args": [{
                "menuId": menu_id,
                "appId": menu_app_id,
                "cloudId": cloud_id,
            }],
            "postData": [{}, []],
        }]
        resp = self.invoke(portal_form, portal_app, "menuItemClick",
                           actions, page_id=portal_pid)
        # 显式把 target_form 绑到 L2 pageId
        if target_form:
            self.page_ids[target_form] = self.l2_page_id(menu_id)
        return resp

    def l2_page_id(self, menu_id: str) -> str:
        """按 {menuId}root{baseId} 规则拼 L2 列表态 pageId（确定性公式）。

        多数场景下 click_menu 的响应 harvest 会把业务 formId→L2 pageId 存进
        self.page_ids，直接 invoke(form_id, ...) 就会自动用上。这个方法用于
        你明确知道 menuId 但 formId 还没被 harvest 的兜底场景。
        """
        if not self.s.root_base_id:
            raise ProtocolError("root_base_id not set - call init_root() first")
        return f"{menu_id}root{self.s.root_base_id}"

    # ---------- 动作调用 ----------

    def invoke(self, form_id: str, app_id: str, ac: str,
               actions: list[dict], page_id: str | None = None) -> list | dict:
        """调用 batchInvokeAction.do。
        pageId 选择规则（per-formId 状态机）：
          1. 显式传 page_id 参数优先
          2. 否则查 self.page_ids[form_id]（由同 form_id 前次响应下发）
          3. 都没有就用 root_page_id 兜底
        """
        if page_id is None:
            page_id = self.page_ids.get(form_id) or self.s.root_page_id
        if not page_id:
            raise ProtocolError(f"No pageId for {form_id}. Call init_root() / open_form() first.")

        # default=str: 兜底 date / datetime / Decimal 等 YAML 解析出来的对象
        params_str = json.dumps(actions, ensure_ascii=False, separators=(",", ":"), default=str)
        body = urllib.parse.urlencode([
            ("pageId", page_id),
            ("appId", app_id),
            ("params", params_str),
        ])
        extra = {}
        if self.sign_required:
            ts = str(int(time.time() * 1000))
            extra["client-start-time"] = ts
            extra["signature"] = self.s.sign(params_str, ts)

        path = (f"/form/batchInvokeAction.do"
                f"?appId={app_id}&f={form_id}&ac={ac}")
        r = self._post(path, body, cqappid=app_id, extra_headers=extra)

        if r.status_code != 200:
            raise ProtocolError(f"invoke {form_id}/{ac} HTTP {r.status_code}: {r.text[:200]}")
        try:
            resp = r.json()
        except Exception:
            raise ProtocolError(f"invoke {form_id}/{ac} bad json: {r.text[:200]}")

        self.last_response = resp
        self._harvest_page_ids(resp)
        # ⭐ 关键：服务端通过 addVirtualTab 下发的新 pageId 需要被正确路由到下一个目标表单
        #
        # 两种场景：
        # A. menuItemClick 响应里下发"列表态" pageId（形如 {menuId}root{baseId}）
        #    → 给后续下一个打开的业务表单用
        # B. addnew 响应里下发"新建态" pageId（随机 32hex）
        #    → 覆盖当前 form_id 的 pageId（同一个 form，不同态）
        new_pid = self._extract_new_tab_page_id(resp, form_id)
        if new_pid:
            if ac in ("addnew", "modify", "copyBill", "edit"):
                # 同 form 切换态
                if new_pid != self.page_ids.get(form_id):
                    log.info(f"[{ac}] form {form_id} pageId: {self.page_ids.get(form_id, '')[:30]} → {new_pid[:30]}")
                    self.page_ids[form_id] = new_pid
            elif ac == "menuItemClick":
                # 菜单点击打开新 tab，把 pageId 压入 pending 待消费
                self._pending_tab_page_id = new_pid
                print(f"[menuItemClick] pending tab pageId: {new_pid[:30]}")
        return resp

    def _harvest_page_ids(self, resp: Any):
        """扫响应收集下发的 (formId, pageId)。

        策略：
        - showForm 动作里的 formId+pageId 总是覆盖（服务端主动打开新表单，pid 必然最新）
        - 其他位置仅首次出现才登记，不覆盖已有

        ⭐ 2026-04-27 增强：递归搜索 showForm（不仅限于顶层）。
        苍穹 addnew/modify 的响应中，showForm 可能嵌套在 sendDynamicFormAction→actions
        多层深处。如果只检查顶层会漏掉，导致新表单 pageId 不更新。
        """
        # 先递归收集所有 showForm 里的 formId → pageId（强覆盖）
        def harvest_showform(obj):
            if isinstance(obj, list):
                for item in obj:
                    harvest_showform(item)
            elif isinstance(obj, dict):
                if obj.get("a") == "showForm":
                    for p in obj.get("p", []):
                        if isinstance(p, dict):
                            fid = p.get("formId")
                            pid = p.get("pageId")
                            if isinstance(fid, str) and isinstance(pid, str) and len(pid) >= 16 and fid:
                                old = self.page_ids.get(fid)
                                if old != pid:
                                    log.debug(f"[harvest/showForm] {fid}: {str(old)[:20]}→{pid[:20]}")
                                self.page_ids[fid] = pid
                # 递归进入子结构（actions 嵌套、p 数组内嵌 dict 等）
                for v in obj.values():
                    harvest_showform(v)
        harvest_showform(resp)

        # 再递归扫其余 formId/pageId（首次出现才登记）
        def walk(obj):
            if isinstance(obj, dict):
                fid = obj.get("formId")
                pid = obj.get("pageId")
                if (isinstance(fid, str) and isinstance(pid, str)
                        and len(pid) >= 16 and fid and fid not in self.page_ids):
                    self.page_ids[fid] = pid
                for v in obj.values(): walk(v)
            elif isinstance(obj, list):
                for x in obj: walk(x)
        walk(resp)

    def _harvest_virtual_tab_pageids(self, resp: Any) -> None:
        """扫 addVirtualTab.args[].pageId + tabName/appId 提示，
        把它映射到对应 formId 的 pageId 状态（即使那个 formId 我们还没请求过）。
        tabName 通常含业务名，不足以精确映射到 formId，只能按"app 里当前活跃 tab"当作
        pending，等下个对应 app 的请求消费。但实际上 HAR 分析显示：菜单 tab 的
        pageId 会直接被 f=<主表单>的后续请求采用——所以我们把它绑到 appId 级，
        由下一个同 app 的请求认领。
        """
        for tab_info in self._find_virtual_tabs(resp):
            tab_pid = tab_info.get("pageId")
            app = tab_info.get("appId")
            if not isinstance(tab_pid, str) or len(tab_pid) < 16:
                continue
            # 按 appId 记 pending；下一次同 app 的请求如该 formId 没 pageId 就领走
            if app:
                self._pending_by_app[app] = tab_pid

    @staticmethod
    def _find_virtual_tabs(resp: Any) -> list[dict]:
        out = []
        def walk(obj):
            if isinstance(obj, dict):
                if obj.get("methodname") == "addVirtualTab" or obj.get("methodName") == "addVirtualTab":
                    for a in obj.get("args", []) or []:
                        if isinstance(a, dict):
                            out.append(a)
                for v in obj.values(): walk(v)
            elif isinstance(obj, list):
                for x in obj: walk(x)
        walk(resp)
        return out

    def _extract_new_tab_page_id(self, resp: Any, form_id: str) -> str | None:
        """从 addnew 响应里找 addVirtualTab 推送的新 pageId。
        典型结构：
          [{"a":"sendDynamicFormAction","p":[{"pageId":...,"actions":[
            {"a":"InvokeControlMethod","p":[{
               "key":"homepagetabap",
               "methodname":"addVirtualTab",
               "args":[{"tabName":"新增...","appId":"haos","pageId":"<新pageId>"}]
            }]}
          ]}]}]
        """
        candidates: list[str] = []

        def walk(obj):
            if isinstance(obj, dict):
                # 命中 addVirtualTab 动作
                if obj.get("methodname") == "addVirtualTab" or obj.get("methodName") == "addVirtualTab":
                    for a in obj.get("args", []) or []:
                        if isinstance(a, dict):
                            pid = a.get("pageId")
                            if isinstance(pid, str) and len(pid) >= 16:
                                candidates.append(pid)
                for v in obj.values():
                    walk(v)
            elif isinstance(obj, list):
                for x in obj:
                    walk(x)

        walk(resp)
        # addnew 通常只返回 1 个新 pageId；如果有多个取最后一个（最深的 tab）
        return candidates[-1] if candidates else None

    # ---------- 便利方法 ----------

    def load_data(self, form_id: str, app_id: str,
                  key: str = "", post_data: list | None = None) -> list | dict:
        pd = post_data if post_data is not None else [{}, []]
        return self.invoke(form_id, app_id, "loadData", [{
            "key": key, "methodName": "loadData", "args": [], "postData": pd,
        }])

    def click_toolbar(self, form_id: str, app_id: str,
                      ac: str, item_id: str, click_id: str | None = None,
                      toolbar_key: str = "toolbarap",
                      post_data: list | None = None) -> list | dict:
        """点击工具栏按钮（addnew / save / close 等）"""
        pd = post_data if post_data is not None else [{}, []]
        return self.invoke(form_id, app_id, ac, [{
            "key": toolbar_key, "methodName": "itemClick",
            "args": [item_id, click_id or item_id], "postData": pd,
        }])

    def update_field(self, form_id: str, app_id: str, field_key: str, value: Any,
                     row_index: int = -1) -> list | dict:
        """updateValue 单字段"""
        return self.invoke(form_id, app_id, "updateValue", [{
            "key": "", "methodName": "updateValue", "args": [],
            "postData": [{}, [{"k": field_key, "v": value, "r": row_index}]],
        }])

    def update_fields(self, form_id: str, app_id: str, fields: dict,
                      row_index: int = -1) -> list | dict:
        """updateValue 多字段一次发"""
        items = [{"k": k, "v": v, "r": row_index} for k, v in fields.items()]
        return self.invoke(form_id, app_id, "updateValue", [{
            "key": "", "methodName": "updateValue", "args": [],
            "postData": [{}, items],
        }])

    def pick_basedata(self, form_id: str, app_id: str,
                      field_key: str, value_id: str) -> list | dict:
        """setItemByIdFromClient 选基础资料"""
        return self.invoke(form_id, app_id, "setItemByIdFromClient", [{
            "key": field_key, "methodName": "setItemByIdFromClient",
            "args": [[value_id, 0]], "postData": [{}, []],
        }])

    def query_tree(self, form_id: str, app_id: str,
                   parent_node_id: str = "", tree_key: str = "treeview") -> list | dict:
        return self.invoke(form_id, app_id, "queryTreeNodeChildren", [{
            "key": tree_key, "methodName": "queryTreeNodeChildren",
            "args": ["", parent_node_id], "postData": [{}, []],
        }])


# =============================================================
# 响应工具：便于 runner / diagnoser 使用
# =============================================================
def find_actions(resp: Any, action_name: str) -> list:
    """从响应里找所有 a=xxx 的 action，返回它们的 p 列表"""
    out = []
    if isinstance(resp, list):
        for cmd in resp:
            if isinstance(cmd, dict) and cmd.get("a") == action_name:
                out.append(cmd.get("p"))
    return out


def find_form_in_response(resp: Any, form_id: str) -> dict | None:
    """在响应里找 formId=xxx 的 showForm 子表单定义（拿 pageId 用）"""
    if isinstance(resp, list):
        for cmd in resp:
            if not isinstance(cmd, dict): continue
            if cmd.get("a") != "showForm": continue
            for p in cmd.get("p", []):
                if isinstance(p, dict) and p.get("formId") == form_id:
                    return p
    return None


def has_error_action(resp: Any) -> list[str]:
    """扫响应顶层错误消息（showErrMsg / ShowNotificationMsg / showMessage 带错误态 / showConfirm 超时）。返回错误文本列表。"""
    errors = []
    if not isinstance(resp, list): return errors
    for cmd in resp:
        if not isinstance(cmd, dict): continue
        a = cmd.get("a")
        if a in ("showErrMsg",):
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    t = p.get("errorTitle") or ""
                    i = p.get("errorInfo") or ""
                    if t or i:
                        errors.append(f"{t} | {str(i)[:150]}")
        if a == "ShowNotificationMsg":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    content = str(p.get("content") or "")
                    if not content:
                        continue
                    # 成功类 / 信息通知类不算错误
                    success_kw = ("成功", "已保存", "已提交", "已生效", "已审核", "完成",
                                  "已设置", "已清空", "已更新", "已调整", "已同步",
                                  "属于非", "自动", "将关闭")
                    if any(kw in content for kw in success_kw):
                        continue
                    errors.append(f"[Notification] {content[:150]}")
        if a == "showConfirm":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    cid = p.get("id") or ""
                    msg = p.get("msg") or ""
                    # pagetimeout / 会话超时 = 表单会话失效，是真错误
                    if cid == "pagetimeout" or "会话超时" in msg or "超时" in msg:
                        errors.append(f"[Timeout] {msg[:150]}")
        if a == "showFormValidMsg":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    msg = p.get("msg") or p.get("message") or ""
                    if msg:
                        errors.append(f"[Validation] {str(msg)[:150]}")
        if a == "showMessage":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    msg = p.get("msg") or p.get("message")
                    if msg and ("失败" in str(msg) or "错误" in str(msg) or "不能" in str(msg)):
                        errors.append(str(msg))
    return errors