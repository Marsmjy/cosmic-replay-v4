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
import mimetypes
import os
import re
import subprocess
import sys
import time
import uuid
import urllib.parse
from dataclasses import dataclass
from email.utils import parsedate_to_datetime
from pathlib import Path
from typing import Any

import urllib3
urllib3.disable_warnings()
import requests

try:
    import websocket as _ws_mod
except ImportError:
    _ws_mod = None


log = logging.getLogger("cosmic_replay")

# L2 pageId 特征：数字前缀 + "root" + 32hex（菜单级 pageId）
_L2_PATTERN = re.compile(r'^\d+root[0-9a-f]{32}$')


def _is_l2_pageid(pid: str) -> bool:
    """判断 pageId 是否为 L2（菜单级）pageId。
    L2 形如 {数字menuId}root{32hex}，而 L3 是纯 {32hex}。
    """
    return bool(_L2_PATTERN.match(pid))


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
    # 先找同目录下的 cosmic_login.py（lib/ 目录，和 replay.py 同目录）
    same_dir = here.parent / "cosmic_login.py"
    if same_dir.exists():
        return same_dir
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
        # ⭐ 浏览器兼容头：UAT 等环境对 updateValue / setItemByIdFromClient 等
        #    写操作校验 signature，签名依赖 diff_time（服务器-本地时间差）。
        #    同时缺少 Origin / Referer 等头时服务端可能静默拒绝写操作（返回 []）。
        # ⭐ 注意：HAR 录制使用 fetch API，不发送 X-Requested-With 头。
        #    额外发送该头可能导致服务端 CSRF 中间件对写操作（updateValue 等）
        #    进行不同的校验逻辑，导致静默返回 []。
        parsed = urllib.parse.urlparse(self.base_url)
        origin = f"{parsed.scheme}://{parsed.netloc}" if parsed.scheme and parsed.netloc else ""
        return {
            "Accept": "*/*",
            "Cookie": self.cookie,
            "userId": self.user_id,
            "client-start-time": str(int(time.time() * 1000)),
            "traceId": uuid.uuid4().hex[:16],
            "cqappid": cqappid,
            "ajax": "true",
            "kd-client-type": "web",
            **({"kd-csrf-token": self.csrf_token} if self.csrf_token else {}),
            # 浏览器兼容头（UAT 环境写操作需要）
            **({"Origin": origin} if origin else {}),
            **({"Referer": f"{self.base_url}/?formId=home_page"} if self.base_url else {}),
            "Accept-Language": "zh-CN,zh;q=0.9",
            "User-Agent": (
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                "AppleWebKit/537.36 (KHTML, like Gecko) "
                "Chrome/149.0.0.0 Safari/537.36"
            ),
            # ⭐ 注意：HAR 录制中 updateValue 等写操作请求不包含 Sec-Fetch-* 头。
            #    额外发送这些头可能导致服务端 CSRF 中间件对写操作进行不同的校验逻辑，
            #    导致静默返回 []。因此移除 Sec-Fetch-* 头。
            #    （之前添加 Sec-Fetch 头的尝试已证明无效，updateValue 仍返回 []）
            "Connection": "keep-alive",
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
                m_effective = re.search(r"^EFFECTIVE_BASE_URL=(.+)$", out, re.M)
                m_cookie = re.search(r"^COOKIE=(.+)$", out, re.M)
                m_acct = re.search(r"^ACCOUNT_ID=(.+)$", out, re.M)
                m_user = re.search(r"^USER_ID=(.+)$", out, re.M)
                m_csrf = re.search(r"^CSRF_TOKEN=(.*)$", out, re.M)
                if not (m_cookie and m_acct and m_user):
                    raise LoginError(f"Login output missing fields:\n{out}")
                return CosmicSession(
                    base_url=(m_effective.group(1).strip().rstrip("/") if m_effective else base_url.rstrip("/")),
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
        # ⭐ 将登录 cookie 设置到 session jar 中，让 requests 自动管理所有 cookie。
        #    服务器通过 Set-Cookie 设置的新 cookie（如 ierp-tenant_kdshareflag）
        #    会被自动捕获并在后续请求中发送。如果使用显式 Cookie 头，这些
        #    服务器设置的 cookie 不会被发送，可能导致服务端拒绝写操作（返回 []）。
        if session.cookie:
            parsed = urllib.parse.urlparse(session.base_url)
            domain = parsed.hostname or ""
            for _ck in session.cookie.split(";"):
                _ck = _ck.strip()
                if "=" in _ck:
                    _name, _val = _ck.split("=", 1)
                    self.http.cookies.set(_name.strip(), _val.strip(),
                                          domain=domain, path="/")
        # form_id → pageId 映射
        self.page_ids: dict[str, str] = {}
        # 历史响应存档（调试用）
        self.last_response: Any = None
        # menuItemClick 响应里下发的 tab pageId，等下一次 open_form / invoke 消费
        self._pending_tab_page_id: str | None = None
        # addVirtualTab 响应里按 appId 记的 pending pageId（未绑定表单，按 app 兜底）
        self._pending_by_app: dict[str, str] = {}
        # ⭐ 已成功 loadData 的 form 集合，防止 showForm 从兄弟表单响应覆盖已初始化的 pageId
        self._loaded_forms: set[str] = set()
        # 当前正在 invoke 的 form_id（供 _harvest_page_ids 判断来源）
        self._current_invoke_form: str | None = None
        # ⭐ 记录每个表单所有 changeYear 的 key/args 列表，用于 updateValue/setItemByIdFromClient
        #    在回放环境中被服务端静默忽略（返回 []）时的 fallback 机制。
        #    存为列表而非单个值，因为 fallback 需要选择与被更新字段不同的 changeYear key
        #    （服务端只处理 postData 中与 changeYear key 不同的字段值）。
        self._last_changeyear: dict[str, list[tuple[str, list]]] = {}
        # ⭐ WebSocket 连接：苍穹表单编辑模式要求浏览器建立 WS 连接 (wsconfig.wsurl)，
        #    服务端可能通过 WS 连接验证写操作 (updateValue/setItemByIdFromClient)。
        #    缺少 WS 连接时服务端静默忽略写操作（返回 []）。
        self._ws = None
        self._ws_url: str | None = None
        # ⭐ 待落库脏字段：updateValue/setItemByIdFromClient 在回放环境中被服务端
        #    静默忽略（返回 []）时，将字段值暂存于此。后续 changeYear 步骤或
        #    保存前自动 flush 时，通过 changeYear postData 机制传递给服务端。
        #    这是唯一被服务端接受的字段值传递方式（ba_em_empnumber 即通过此机制落库）。
        #    格式: {form_id: {"app_id": str, "items": [{"k": str, "v": Any, "r": int}]}}
        self._pending_dirty_fields: dict[str, dict] = {}

    # ---------- WebSocket ----------

    def _extract_wsurl(self, resp: Any) -> str | None:
        """从响应中递归搜索 wsconfig.wsurl。"""
        def _find(obj):
            if isinstance(obj, dict):
                if "wsurl" in obj:
                    return obj["wsurl"]
                if "wsconfig" in obj and isinstance(obj["wsconfig"], dict):
                    return obj["wsconfig"].get("wsurl")
                for v in obj.values():
                    r = _find(v)
                    if r:
                        return r
            elif isinstance(obj, list):
                for item in obj:
                    r = _find(item)
                    if r:
                        return r
            return None
        return _find(resp)

    def _ensure_ws_connection(self, resp: Any):
        """从响应中提取 wsconfig 并建立 WebSocket 连接。

        ⭐ 苍穹表单编辑模式中，modify/loadData 响应包含 wsconfig.wsurl，
        指示浏览器建立 WebSocket 连接到 /ierp/msgwatch/。
        服务端可能通过此 WS 连接验证写操作（updateValue/setItemByIdFromClient），
        缺少 WS 连接时静默返回 []。
        """
        if _ws_mod is None:
            log.warning("[ws] websocket-client not installed, skipping WS connection")
            return
        ws_url = self._extract_wsurl(resp)
        if not ws_url:
            return
        if self._ws is not None:
            # 已有连接，检查是否还活着
            try:
                self._ws.ping()
                return
            except Exception:
                log.info("[ws] connection lost, reconnecting")
                try:
                    self._ws.close()
                except Exception:
                    pass
                self._ws = None
        # 建立新连接
        ws_url = ws_url.replace("ws://", "http://").replace("wss://", "https://")
        # websocket-client 需要 ws:// 前缀
        if ws_url.startswith("http://"):
            ws_url = "ws://" + ws_url[7:]
        elif ws_url.startswith("https://"):
            ws_url = "wss://" + ws_url[8:]
        self._ws_url = ws_url
        try:
            # 收集 cookies
            cookie_str = "; ".join(f"{k}={v}" for k, v in self.http.cookies.items())
            parsed = urllib.parse.urlparse(self.s.base_url)
            origin = f"{parsed.scheme}://{parsed.netloc}" if parsed.scheme and parsed.netloc else ""
            headers = {
                "Origin": origin,
                "User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                               "AppleWebKit/537.36 (KHTML, like Gecko) "
                               "Chrome/149.0.0.0 Safari/537.36"),
            }
            if cookie_str:
                headers["Cookie"] = cookie_str
            log.info(f"[ws] connecting to {ws_url}")
            print(f"[WS] connecting to {ws_url}")
            self._ws = _ws_mod.create_connection(
                ws_url,
                header=headers,
                timeout=10,
                enable_multithread=True,
            )
            print(f"[WS] connected, ready={self._ws.connected}")
            log.info(f"[ws] connected: {self._ws.connected}")
            # 发送注册消息（苍穹消息订阅格式）
            reg_msg = json.dumps({
                "type": "subscribe",
                "userId": self.s.user_id,
                "t": int(time.time() * 1000),
            })
            self._ws.send(reg_msg)
            print(f"[WS] sent registration message")
            # 尝试接收响应（非阻塞）
            self._ws.settimeout(2)
            try:
                result = self._ws.recv()
                print(f"[WS] received: {str(result)[:200]}")
            except Exception:
                pass
            self._ws.settimeout(None)
        except Exception as e:
            log.warning(f"[ws] connection failed: {e}")
            print(f"[WS] connection failed: {e}")
            self._ws = None

    # ---------- 资源管理 ----------

    def close(self):
        """释放 HTTP 会话资源"""
        if hasattr(self, 'http') and self.http:
            try:
                self.http.close()
            except Exception:
                pass

    def __del__(self):
        self.close()

    def __enter__(self):
        return self

    def __exit__(self, *args):
        self.close()

    # ---------- HTTP 低层 ----------

    def _post_with_retry(self, url, *, data=None, json_data=None, headers=None,
                         retries=1, retry_wait=1.0, **kwargs):
        """对 HTTP POST 添加重试，仅在网络异常时重试，成功路径不变"""
        import time as _time
        last_err = None
        for attempt in range(retries + 1):
            try:
                resp = self.http.post(url, data=data, json=json_data,
                                     headers=headers, **kwargs)
                return resp
            except (requests.ConnectionError, requests.Timeout) as e:
                last_err = e
                if attempt < retries:
                    _time.sleep(retry_wait * (2 ** attempt))
        raise last_err

    def _refresh_diff_time(self, r: requests.Response):
        """从响应 Date 头刷新 diff_time（有符号：server_ms - local_ms）。

        长时间运行的测试中，本地时钟可能漂移，导致 init_root 时计算的 diff_time
        失效。每次 HTTP 响应后刷新，确保下一次请求的签名使用最新值。
        """
        date_header = r.headers.get("Date", "")
        if not date_header:
            return
        try:
            server_dt = parsedate_to_datetime(date_header)
            server_ms = int(server_dt.timestamp() * 1000)
            local_ms = int(time.time() * 1000)
            diff = server_ms - local_ms
            new_diff = str(diff)
            if new_diff != self.s.diff_time:
                old_diff = self.s.diff_time
                self.s.diff_time = new_diff
                log.debug(f"[diff_time] refreshed: {old_diff} → {new_diff} (server={server_ms}, local={local_ms})")
        except Exception:
            pass

    def _post(self, path: str, body_urlenc: str, cqappid: str,
              extra_headers: dict | None = None) -> requests.Response:
        headers = self.s.base_headers(cqappid=cqappid)
        headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        # ⭐ 移除显式 Cookie 头，让 requests 从 session jar 自动发送所有 cookie
        #    （登录 cookie + 服务器 Set-Cookie 设置的新 cookie）
        if self.http.cookies:
            headers.pop("Cookie", None)
        if extra_headers:
            headers.update(extra_headers)
        url = self.s.base_url + path
        r = self._post_with_retry(url, data=body_urlenc, headers=headers, timeout=self.timeout)
        # ⭐ 每次响应后刷新 diff_time（防止时钟漂移导致后续签名失效）
        self._refresh_diff_time(r)
        return r

    def _get(self, path: str, params: dict, cqappid: str = "bos") -> requests.Response:
        headers = self.s.base_headers(cqappid=cqappid)
        headers["Content-Type"] = "application/json;charset=utf-8"
        # ⭐ 同 _post：移除显式 Cookie 头，让 requests 自动管理
        if self.http.cookies:
            headers.pop("Cookie", None)
        url = self.s.base_url + path
        return self.http.get(url, params=params, headers=headers, timeout=self.timeout)

    def _abs_url(self, endpoint: str) -> str:
        endpoint = str(endpoint or "").strip()
        if not endpoint:
            raise ProtocolError("upload_file 缺少 upload_endpoint/upload_url")
        if endpoint.startswith(("http://", "https://")):
            return endpoint
        if endpoint.startswith("/"):
            return self.s.base_url.rstrip("/") + endpoint
        return self.s.base_url.rstrip("/") + "/" + endpoint.lstrip("/")

    def upload_file(
        self,
        endpoint: str,
        file_path: str | os.PathLike,
        *,
        app_id: str = "bos",
        field_name: str = "file",
        extra_data: dict | list[tuple[str, Any]] | None = None,
        extra_headers: dict | None = None,
    ) -> Any:
        """Upload a real local file through a recorded/configured multipart endpoint.

        HAR files only contain the browser's temporary upload state, not the file
        bytes. This helper is the runtime path for "用户文件 → 上传接口 → 响应 id/url".
        It deliberately does not reuse any tempfile/download.do URL from the HAR.
        """
        path = Path(file_path).expanduser()
        if not path.exists() or not path.is_file():
            raise ProtocolError(f"真实附件上传找不到本地文件: {path}")
        if not field_name:
            field_name = "file"

        url = self._abs_url(endpoint)
        headers = self.s.base_headers(cqappid=app_id or "bos")
        if extra_headers:
            headers.update(extra_headers)
        content_type = mimetypes.guess_type(path.name)[0] or "application/octet-stream"
        with path.open("rb") as fh:
            files = {field_name: (path.name, fh, content_type)}
            r = self._post_with_retry(
                url,
                data=extra_data or {},
                files=files,
                headers=headers,
                timeout=self.timeout,
            )
        if r.status_code < 200 or r.status_code >= 300:
            raise ProtocolError(f"upload_file HTTP {r.status_code}: {r.text[:200]}")
        try:
            resp: Any = r.json()
        except Exception:
            resp = {
                "status_code": r.status_code,
                "text": r.text,
            }
        self.last_response = resp
        self._current_invoke_form = None
        self._harvest_page_ids(resp)
        self._harvest_virtual_tab_pageids(resp)
        return resp

    # ---------- 会话初始化 ----------

    def init_root(self) -> str:
        """拉取会话根 pageId（从 getConfig.do GET 返回的 pageId 字段取）。

        同时从响应 Date 头计算服务器-本地时间差（diff_time），
        用于后续 signature 签名。UAT 等环境对 updateValue / setItemByIdFromClient
        等写操作校验签名，diff_time 错误会导致服务端静默返回 []。
        """
        flag = uuid.uuid4().hex[:16]
        f_val = uuid.uuid4().hex[:18]
        r = self._get("/form/getConfig.do", {
            "params": json.dumps({"formId": "home_page", "flag": flag, "f": f_val},
                                 separators=(",", ":")),
            "random": "0.5",
        }, cqappid="bos")
        if r.status_code != 200:
            raise ProtocolError(f"init_root HTTP {r.status_code}: {r.text[:200]}")
        # ⭐ 从响应 Date 头计算 diff_time（服务器时间 - 本地时间，毫秒，有符号）
        #    服务端签名验证：expected_server_time = ts + diff_time
        #    若 diff_time = server_ms - local_ms（有符号），则：
        #      expected_server_time = local_ms + (server_ms - local_ms) = server_ms ✓
        #    若使用 abs() 且 diff 为负（本地时间领先服务器），则：
        #      expected_server_time = local_ms + |server_ms - local_ms| ≠ server_ms ✗
        #    这会导致服务端时间校验失败，写操作被静默忽略（返回 []）。
        self._refresh_diff_time(r)
        log.info(f"[init_root] diff_time={self.s.diff_time}")
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

    def _is_pageid_likely_stale(self, form_id: str) -> bool:
        """启发式判定 pageId 是否可能已过期

        检测逻辑：
        - L2 pageId 绑定到未 loadData 的表单 → 可能过期
        - form_id 不在 _loaded_forms 中且 pageId 来自兜底 → 可能过期
        """
        pid = self.page_ids.get(form_id)
        if not pid:
            return True  # 无 pageId 肯定无效
        if _is_l2_pageid(pid) and form_id not in self._loaded_forms:
            return True
        return False

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
        # ⭐ 重新打开表单时清除 loaded 标记，允许后续 showForm 更新
        self._loaded_forms.discard(form_id)
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
        # ⭐ 浏览器自然延迟：HAR 录制中浏览器在 changeYear/getLookUpList 等操作后
        #    需要时间处理响应并发送下一个写操作（JS 事件循环、DOM 渲染等）。
        #    回放中请求间几乎 0 延迟，服务端可能未处理完前一个操作就收到写请求，
        #    导致静默返回 []（而非正常的 [u] 确认）。
        #    HAR 时间戳分析：
        #    - changeYear → updateValue: ~1.8s
        #    - getLookUpList → setItemByIdFromClient: ~1.5s
        _write_delay_map = {
            "updateValue": 2.0,
            "setItemByIdFromClient": 2.0,
        }
        if ac in _write_delay_map:
            time.sleep(_write_delay_map[ac])
        if page_id is None:
            page_id = self.page_ids.get(form_id)
            # ⭐ 优先使用 _pending_by_app（来自 addVirtualTab 的下发 pageId）
            # 但只在当前 pageId 是 L2 pageId（菜单级）时才覆盖，不覆盖 32hex 表单级 pageId
            pending_pid = self._pending_by_app.get(app_id)
            if (pending_pid and len(pending_pid) >= 16
                    and page_id != pending_pid
                    and (page_id is None or len(page_id) > 32 or '/' in page_id)):
                old_id = page_id
                page_id = pending_pid
                log.debug(f"[pending_by_app] {form_id}/{ac}: {str(old_id)[:20]}→{str(pending_pid)[:20]}")
            if page_id is None:
                page_id = self.s.root_page_id
        if not page_id:
            raise ProtocolError(f"No pageId for {form_id}. Call init_root() / open_form() first.")

        # ⭐ 保存前处理待落库脏字段（双保险机制）：
        #    1. 将脏字段注入保存操作的 postData（服务端可能在保存时处理这些值）
        #    2. 通过 changeYear 自动 flush（服务端可能静默处理 postData 字段）
        #    updateValue/setItemByIdFromClient 在回放环境中被静默忽略（返回 []），
        #    字段值暂存在 _pending_dirty_fields 中。
        _SAVE_ACTIONS = {"btn_billsave", "save", "submit", "saveandeffect", "submitandeffect"}
        if ac in _SAVE_ACTIONS:
            # 1. 注入脏字段到保存操作的 postData
            _all_dirty_items = []
            for _fid, _pending in list(self._pending_dirty_fields.items()):
                if _pending.get("items"):
                    _all_dirty_items.extend(_pending["items"])
            if _all_dirty_items and actions:
                _pd = actions[0].get("postData", [{}, []])
                if isinstance(_pd, list) and len(_pd) >= 2 and isinstance(_pd[1], list):
                    _existing_keys = {item.get("k") for item in _pd[1] if isinstance(item, dict)}
                    _new_items = [item for item in _all_dirty_items if item.get("k") not in _existing_keys]
                    if _new_items:
                        _pd[1].extend(_new_items)
                        actions[0]["postData"] = _pd
                        print(f"[DIRTY-SAVE] Injected {len(_new_items)} dirty fields into {ac} postData: {[i.get('k') for i in _new_items]}")
            # 2. 同时通过 changeYear 自动 flush（双保险）
            self._flush_pending_dirty_fields()

        # ⭐ 注入待落库脏字段到 changeYear postData：
        #    当 updateValue/setItemByIdFromClient 返回 [] 时，字段值被暂存到
        #    _pending_dirty_fields。changeYear 是服务端唯一接受的字段值传递方式，
        #    因此在 changeYear 请求中将这些字段值合并到 postData 中。
        if ac == "changeYear" and actions:
            _pending = self._pending_dirty_fields.get(form_id)
            if _pending and _pending.get("items"):
                _pd = actions[0].get("postData", [{}, []])
                if isinstance(_pd, list) and len(_pd) >= 2 and isinstance(_pd[1], list):
                    _existing_keys = {item.get("k") for item in _pd[1] if isinstance(item, dict)}
                    _new_items = [item for item in _pending["items"] if item.get("k") not in _existing_keys]
                    if _new_items:
                        _pd[1].extend(_new_items)
                        actions[0]["postData"] = _pd
                        print(f"[DIRTY-INJECT] Merged {len(_new_items)} pending dirty fields into changeYear for {form_id}: {[i.get('k') for i in _new_items]}")
                self._pending_dirty_fields.pop(form_id, None)

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
        # ⭐ 调试日志：updateValue / loadData / changeYear 的请求和响应
        if ac in ("updateValue", "loadData", "changeYear", "setItemByIdFromClient"):
            print(f"[DEBUG-{ac}] form={form_id}, pageId={str(page_id)[:32]}, params={params_str[:300]}")
            print(f"[DEBUG-{ac}] resp={str(resp)[:2000]}")
            # ⭐ dump 完整请求头（包括 requests 自动添加的头）用于与 HAR 对比
            if ac in ("updateValue", "setItemByIdFromClient"):
                try:
                    prepared_headers = dict(r.request.headers)
                    print(f"[DEBUG-{ac}] PREPARED HEADERS:")
                    for k, v in sorted(prepared_headers.items()):
                        print(f"  {k}: {v[:100] if isinstance(v, str) else v}")
                    print(f"[DEBUG-{ac}] BODY: {r.request.body[:500]}")
                    print(f"[DEBUG-{ac}] URL: {r.request.url}")
                    print(f"[DEBUG-{ac}] RESPONSE STATUS: {r.status_code}")
                    print(f"[DEBUG-{ac}] RESPONSE HEADERS:")
                    for k, v in sorted(r.headers.items()):
                        print(f"  {k}: {v[:100]}")
                    print(f"[DEBUG-{ac}] RESPONSE TEXT (first 500): {r.text[:500]}")
                    # 签名计算详情
                    if self.sign_required:
                        ts_used = extra.get("client-start-time", "")
                        sig_input = ts_used + self.s.csrf_token + self.s.diff_time + params_str
                        print(f"[DEBUG-{ac}] SIGN_INPUT: ts={ts_used}, csrf={self.s.csrf_token[:10]}..., diff_time={self.s.diff_time}, params_len={len(params_str)}")
                        print(f"[DEBUG-{ac}] SIGN_HASH: {hashlib.sha256(sig_input.encode('utf-8')).hexdigest()}")
                        print(f"[DEBUG-{ac}] SIGN_FULL: {extra.get('signature', '')}")
                    # cookie jar
                    jar_cookies = dict(self.http.cookies)
                    print(f"[DEBUG-{ac}] COOKIE_JAR: {jar_cookies if jar_cookies else 'empty'}")
                    # ⭐ 追踪 pageId 变化历史
                    print(f"[DEBUG-{ac}] PAGE_IDS: {dict((k, str(v)[:32]) for k, v in self.page_ids.items() if 'onbrd' in k)}")
                    print(f"[DEBUG-{ac}] LOADED_FORMS: {self._loaded_forms}")
                except Exception as dbg_e:
                    print(f"[DEBUG-{ac}] debug log error: {dbg_e}")
            # 搜索响应中的 showForm 动作 和 setFormStatus
            if ac == "loadData":
                _showforms = []
                _form_status = None
                _call_client_actions = []
                def _find_showform(obj):
                    nonlocal _form_status
                    if isinstance(obj, list):
                        for item in obj:
                            _find_showform(item)
                    elif isinstance(obj, dict):
                        if obj.get("a") == "showForm":
                            for p in obj.get("p", []):
                                if isinstance(p, dict):
                                    _showforms.append({"formId": p.get("formId"), "pageId": str(p.get("pageId", ""))[:32]})
                        elif obj.get("a") == "setFormStatus":
                            _form_status = obj.get("p", [])
                        elif obj.get("a") == "callClientAction":
                            for p in obj.get("p", []):
                                if isinstance(p, dict) and "ai" in p:
                                    _call_client_actions.append(p.get("ai"))
                        for v in obj.values():
                            _find_showform(v)
                _find_showform(resp)
                print(f"[DEBUG-loadData] form={form_id} setFormStatus={_form_status} callClientAction_ais={_call_client_actions}")
                if _showforms:
                    print(f"[DEBUG-loadData] showForm found: {_showforms}")
                else:
                    print(f"[DEBUG-loadData] NO showForm in response")
        # ⭐ 记录 changeYear 的 key/args，用于 updateValue/setItemByIdFromClient 失败时 fallback
        if ac == "changeYear" and actions:
            _act = actions[0]
            _cy_key = _act.get("key", "")
            _cy_args = _act.get("args", [])
            if _cy_key:
                if form_id not in self._last_changeyear:
                    self._last_changeyear[form_id] = []
                self._last_changeyear[form_id].append((_cy_key, _cy_args))
                log.debug(f"[changeYear] Tracked for {form_id}: key={_cy_key}, args={_cy_args} (total={len(self._last_changeyear[form_id])})")
        # ⭐ modify/addnew 等切态操作前保存旧 pageId，切态后自动 release 旧页面
        _old_pids = dict(self.page_ids) if ac in ("modify", "addnew", "copyBill", "edit", "new") else None
        self._current_invoke_form = form_id
        self._harvest_page_ids(resp)
        self._current_invoke_form = None
        self._harvest_virtual_tab_pageids(resp)
        # ⭐ WebSocket 连接：modify/loadData 响应可能包含 wsconfig.wsurl，
        #    浏览器会建立 WS 连接用于服务端推送。服务端可能通过 WS 连接验证写操作。
        if ac in ("modify", "addnew", "loadData"):
            self._ensure_ws_connection(resp)
        # ⭐ loadData 完成后标记表单已加载，后续兄弟表单响应中的 showForm 不再覆盖其 pageId
        if ac == "loadData":
            self._loaded_forms.add(form_id)
            # ⭐ loadData 后自动调用 getCityInfo（浏览器行为）
            #    HAR 录制中浏览器在 loadData 后会自动调用 getCityInfo 初始化城市控件，
            #    缺少此步骤会导致服务端表单控件未初始化，后续 updateValue 被静默忽略（返回 []）。
            self._auto_get_city_info(form_id, app_id, resp)
        # ⭐ 关键：服务端通过 addVirtualTab 下发的新 pageId 需要被正确路由到下一个目标表单
        #
        # 两种场景：
        # A. menuItemClick 响应里下发"列表态" pageId（形如 {menuId}root{baseId}）
        #    → 给后续下一个打开的业务表单用
        # B. addnew 响应里下发"新建态" pageId（随机 32hex）
        #    → 覆盖当前 form_id 的 pageId（同一个 form，不同态）
        new_pid = self._extract_new_tab_page_id(resp, form_id)
        if new_pid:
            if ac in ("addnew", "modify", "copyBill", "edit", "new"):
                # 同 form 切换态
                if new_pid != self.page_ids.get(form_id):
                    log.info(f"[{ac}] form {form_id} pageId: {self.page_ids.get(form_id, '')[:30]} → {new_pid[:30]}")
                    self.page_ids[form_id] = new_pid
            elif ac == "menuItemClick":
                # 菜单点击打开新 tab，把 pageId 压入 pending 待消费
                self._pending_tab_page_id = new_pid
                print(f"[menuItemClick] pending tab pageId: {new_pid[:30]}")
        # ⭐ 自动 release 旧 pageId：modify/addnew 等切态操作会通过 showForm 下发新 pageId，
        #    旧 pageId 不释放会导致服务端页面状态混乱，后续 updateValue 等写操作可能被静默忽略（返回 []）。
        #    HAR 录制中浏览器在 modify 后会显式调用 release 释放旧页面。
        if _old_pids:
            print(f"[DEBUG-release] _old_pids = {dict((k, str(v)[:25]) for k, v in _old_pids.items())}")
            for fid, old_pid in _old_pids.items():
                new_pid_now = self.page_ids.get(fid)
                _changed = old_pid != new_pid_now if old_pid and new_pid_now else False
                _len32 = len(old_pid) == 32 if old_pid else False
                print(f"[DEBUG-release] {fid}: old={str(old_pid)[:25]} new={str(new_pid_now)[:25]} changed={_changed} len32={_len32}")
                if (old_pid and new_pid_now and old_pid != new_pid_now
                        and len(old_pid) == 32 and '/' not in old_pid):
                    try:
                        log.info(f"[{ac}] Auto-releasing old pageId for {fid}: {old_pid[:30]}")
                        self._send_release(fid, app_id, old_pid)
                    except Exception as e:
                        log.warning(f"[{ac}] Failed to release old pageId for {fid}: {e}")
        return resp

    def _send_release(self, form_id: str, app_id: str, page_id: str):
        """发送 release 请求释放指定 pageId（不触发 harvest，避免副作用）。

        ⭐ HAR 录制中 release 的 args 包含 {"attachmentpanel":{"ep":true}}，
        告知服务端正确清理附件面板状态。缺少此参数可能导致服务端页面状态不完整，
        影响后续写操作（updateValue/setItemByIdFromClient 被静默忽略，返回 []）。
        """
        actions = [{"key": "", "methodName": "release", "args": [{"attachmentpanel": {"ep": True}}], "postData": []}]
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
        path = f"/form/batchInvokeAction.do?appId={app_id}&f={form_id}&ac=release"
        r = self._post(path, body, cqappid=app_id, extra_headers=extra)
        if r.status_code != 200:
            log.warning(f"release {form_id} HTTP {r.status_code}: {r.text[:200]}")
        else:
            log.info(f"[release] Released old pageId for {form_id}: {page_id[:30]}")

    def _auto_get_city_info(self, form_id: str, app_id: str, resp: Any):
        """loadData 后检查表单是否有 checkcity 控件，有则自动调用 getCityInfo（模拟浏览器行为）。

        HAR 录制中浏览器在每次 loadData 后会自动调用 getCityInfo 初始化城市选择控件。
        缺少此步骤时，服务端表单控件未初始化，后续 updateValue 可能被静默忽略（返回 []）。
        """
        # 检查响应中是否包含 "checkcity" 控件
        _has_checkcity = False
        def _scan(obj):
            nonlocal _has_checkcity
            if _has_checkcity:
                return
            if isinstance(obj, list):
                for item in obj:
                    _scan(item)
            elif isinstance(obj, dict):
                if obj.get("k") == "checkcity":
                    _has_checkcity = True
                    return
                for v in obj.values():
                    _scan(v)
        _scan(resp)
        print(f"[DEBUG-getCityInfo] form={form_id}, checkcity_found={_has_checkcity}")
        # ⭐ 只在响应中包含 checkcity 控件时才调用 getCityInfo（与 HAR 录制行为一致）。
        #    HAR 中浏览器只为 hom_onbrdinfo 调用 getCityInfo，不会为 hom_onbrddetailhead、
        #    hom_activityoverview 等没有 city 控件的表单调用。无条件的额外请求会干扰
        #    服务端表单会话状态，导致后续 updateValue/setItemByIdFromClient 被静默忽略（返回 []）。
        if not _has_checkcity:
            log.debug(f"[getCityInfo] Skipped for {form_id}: no checkcity control in response")
            return
        actions = [{"key": "checkcity", "methodName": "getCityInfo", "args": [[]], "postData": [{}, []]}]
        try:
            result = self.invoke_action(form_id, app_id, "getCityInfo", actions)
            print(f"[DEBUG-getCityInfo] response for {form_id}: {str(result)[:300]}")
            log.info(f"[getCityInfo] Auto-called for {form_id} after loadData")
        except Exception as e:
            print(f"[DEBUG-getCityInfo] FAILED for {form_id}: {e}")
            log.warning(f"[getCityInfo] Failed for {form_id} after loadData: {e}")

    def invoke_action(self, form_id: str, app_id: str, ac: str,
                      actions: list[dict], page_id: str | None = None) -> list | dict:
        """调用 /form/invokeAction.do。

        Some Kingdee lookup controls initialize candidate state through
        invokeAction.do rather than batchInvokeAction.do. Replaying that
        prefetch on the original endpoint keeps subsequent setItemByIdFromClient
        behavior aligned with the recorded HAR.
        """
        if page_id is None:
            page_id = self.page_ids.get(form_id)
            pending_pid = self._pending_by_app.get(app_id)
            if (pending_pid and len(pending_pid) >= 16
                    and page_id != pending_pid
                    and (page_id is None or len(page_id) > 32 or '/' in page_id)):
                page_id = pending_pid
            if page_id is None:
                page_id = self.s.root_page_id
        if not page_id:
            raise ProtocolError(f"No pageId for {form_id}. Call init_root() / open_form() first.")

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

        path = (f"/form/invokeAction.do"
                f"?appId={app_id}&f={form_id}&ac={ac}")
        r = self._post(path, body, cqappid=app_id, extra_headers=extra)
        if r.status_code != 200:
            raise ProtocolError(f"invokeAction {form_id}/{ac} HTTP {r.status_code}: {r.text[:200]}")
        try:
            resp = r.json()
        except Exception:
            raise ProtocolError(f"invokeAction {form_id}/{ac} bad json: {r.text[:200]}")

        self.last_response = resp
        self._current_invoke_form = form_id
        self._harvest_page_ids(resp)
        self._current_invoke_form = None
        self._harvest_virtual_tab_pageids(resp)
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
        self._harvest_list_page_ids(resp)

        # 先递归收集所有 showForm 里的 formId → pageId（强覆盖）
        # ⭐ 修复：不覆盖已 loadData 的表单 pageId（除非 showForm 来自同表单的请求响应）
        #    根因：苍穹多 tab 页面中，兄弟表单（日历/待入职/快捷卡片等）的 loadData 响应
        #    会附带 showForm 为主表单下发新 pageId，但该 pageId 未经 loadData 初始化，
        #    导致后续对主表单的操作报 "页面未初始化或者已经过期"。
        _invoking = self._current_invoke_form
        def harvest_showform(obj):
            if isinstance(obj, list):
                for item in obj:
                    harvest_showform(item)
            elif isinstance(obj, dict):
                if obj.get("a") == "showForm":
                    for p in obj.get("p", []):
                        if isinstance(p, dict):
                            pid = p.get("pageId")
                            form_ids = [p.get("formId")]
                            # Some F7 dialogs render with a generic formId but
                            # subsequent HAR requests use billFormId as `f=`.
                            # Bind both names to the same pageId so replay can
                            # follow the recorded request chain.
                            bill_fid = p.get("billFormId")
                            if bill_fid and bill_fid not in form_ids:
                                form_ids.append(bill_fid)
                            for fid in form_ids:
                                if not (isinstance(fid, str) and isinstance(pid, str) and len(pid) >= 16 and fid):
                                    continue
                                # ⭐ L2 pageId 保护：菜单级 pageId 不应绑定给表单 form_id
                                if _is_l2_pageid(pid):
                                    log.debug(f"[harvest/showForm] SKIP L2 pageId for {fid}: {pid[:30]}")
                                    continue
                                # 如果该表单已 loadData 且不是当前请求表单，仅在 pageId 未变化时跳过。
                                # 相同 pageId 通常是兄弟表单响应里的噪声 showForm；不同 pageId
                                # 表示表单/弹窗被重新打开，必须接受新 pageId，否则后续会沿用
                                # 已关闭窗口的过期 pageId。
                                if fid in self._loaded_forms and fid != _invoking:
                                    existing = self.page_ids.get(fid)
                                    if existing == pid:
                                        log.debug(f"[harvest/showForm] SKIP {fid}: already loaded, same pid={str(pid)[:20]} from sibling {_invoking}")
                                        continue
                                    log.debug(f"[harvest/showForm] REOPEN {fid}: {str(existing)[:20]}→{pid[:20]} (via {_invoking})")
                                old = self.page_ids.get(fid)
                                if old != pid:
                                    log.debug(f"[harvest/showForm] {fid}: {str(old)[:20]}→{pid[:20]}")
                                self.page_ids[fid] = pid
                # 递归进入子结构（actions 嵌套、p 数组内嵌 dict 等）
                for v in obj.values():
                    harvest_showform(v)
        harvest_showform(resp)

        def scoped_descendant_forms(node, owner_page_id):
            forms = []
            has_activate = False

            def visit(value, *, root=False):
                nonlocal has_activate
                if isinstance(value, list):
                    for item in value:
                        visit(item)
                    return
                if not isinstance(value, dict):
                    return
                nested_page_id = value.get("pageId")
                if not root and isinstance(nested_page_id, str) and nested_page_id != owner_page_id:
                    return
                if value.get("a") == "activate":
                    has_activate = True
                for key in ("formId", "billFormId"):
                    form_id = value.get(key)
                    if isinstance(form_id, str) and form_id and form_id not in forms:
                        forms.append(form_id)
                for child in value.values():
                    if isinstance(child, (dict, list)):
                        visit(child)

            visit(node, root=True)
            return forms, has_activate

        # 再递归扫其余 formId/pageId（首次出现才登记）
        def walk(obj):
            if isinstance(obj, dict):
                fid = obj.get("formId")
                pid = obj.get("pageId")
                if (isinstance(fid, str) and isinstance(pid, str)
                        and len(pid) >= 16 and fid and fid not in self.page_ids):
                    # ⭐ L2 pageId 保护：walk 阶段也不将菜单级 pageId 绑给表单
                    if _is_l2_pageid(pid):
                        log.debug(f"[harvest/walk] SKIP L2 pageId for {fid}: {pid[:30]}")
                    else:
                        self.page_ids[fid] = pid
                elif (
                    isinstance(pid, str)
                    and len(pid) >= 16
                    and not fid
                    and not _is_l2_pageid(pid)
                ):
                    scoped_forms, has_activate = scoped_descendant_forms(obj, pid)
                    for scoped_form in scoped_forms:
                        if has_activate or scoped_form not in self.page_ids:
                            self.page_ids[scoped_form] = pid
                for v in obj.values(): walk(v)
            elif isinstance(obj, list):
                for x in obj: walk(x)
        walk(resp)

    def _harvest_list_page_ids(self, resp: Any) -> None:
        """Bind an L2 response wrapper to the list entity it explicitly owns.

        Save/close responses commonly return the parent list model as
        ``{pageId: <L2>, actions: [...]}`` without a top-level ``formId``.
        The list metadata still names its entity through
        ``billlistap.entryentities[].key``. Treat that exact entity key as the
        ownership proof instead of guessing from the invoking form.
        """
        def entity_forms(value: Any) -> set[str]:
            forms: set[str] = set()

            def collect(node: Any) -> None:
                if isinstance(node, dict):
                    entries = node.get("entryentities")
                    if isinstance(entries, list):
                        for entry in entries:
                            if not isinstance(entry, dict):
                                continue
                            form_id = entry.get("key")
                            if isinstance(form_id, str) and form_id:
                                forms.add(form_id)
                    for child in node.values():
                        collect(child)
                elif isinstance(node, list):
                    for child in node:
                        collect(child)

            collect(value)
            return forms

        def walk(value: Any) -> None:
            if isinstance(value, dict):
                page_id = value.get("pageId")
                actions = value.get("actions")
                if (
                    isinstance(page_id, str)
                    and _is_l2_pageid(page_id)
                    and isinstance(actions, list)
                ):
                    for form_id in entity_forms(actions):
                        old = self.page_ids.get(form_id)
                        if old != page_id:
                            log.debug(
                                "[harvest/list] %s: %s→%s",
                                form_id,
                                str(old)[:20],
                                page_id[:20],
                            )
                        self.page_ids[form_id] = page_id
                        self._loaded_forms.add(form_id)
                for child in value.values():
                    walk(child)
            elif isinstance(value, list):
                for child in value:
                    walk(child)

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
        """updateValue 多字段一次发

        ⭐ 当 updateValue 返回空 []（回放环境中服务端静默忽略），
        将字段值暂存到 _pending_dirty_fields。后续 changeYear 步骤
        或保存前自动 flush 时通过 postData 机制传递给服务端。
        这是唯一被服务端接受的字段值传递方式（ba_em_empnumber 即通过此机制落库）。
        """
        items = [{"k": k, "v": v, "r": row_index} for k, v in fields.items()]
        resp = self.invoke(form_id, app_id, "updateValue", [{
            "key": "", "methodName": "updateValue", "args": [],
            "postData": [{}, items],
        }])
        # ⭐ 当 updateValue 返回空 []，将字段值暂存到 _pending_dirty_fields
        #    后续 changeYear 步骤会自动注入，或在保存前自动 flush
        if isinstance(resp, list) and len(resp) == 0 and items:
            if form_id not in self._pending_dirty_fields:
                self._pending_dirty_fields[form_id] = {"app_id": app_id, "items": []}
            self._pending_dirty_fields[form_id]["items"].extend(items)
            print(f"[DIRTY-STASH] Stashed {len(items)} fields for {form_id} (updateValue returned []): {[i.get('k') for i in items]}")
        return resp

    def pick_basedata(self, form_id: str, app_id: str,
                      field_key: str, value_id: str,
                      row_index: int = 0,
                      value_name: str = "",
                      value_code: str = "") -> list | dict:
        """setItemByIdFromClient 选基础资料

        ⭐ 当 setItemByIdFromClient 返回空 []（回放环境中服务端静默忽略），
        将字段值暂存到 _pending_dirty_fields。后续 changeYear 步骤
        或保存前自动 flush 时通过 postData 机制传递给服务端。
        """
        resp = self.invoke(form_id, app_id, "setItemByIdFromClient", [{
            "key": field_key, "methodName": "setItemByIdFromClient",
            "args": [[value_id, row_index]], "postData": [{}, []],
        }])
        # ⭐ 当 setItemByIdFromClient 返回空 []，将字段值暂存到 _pending_dirty_fields
        if isinstance(resp, list) and len(resp) == 0:
            item = {"k": field_key, "v": value_id, "r": row_index}
            if form_id not in self._pending_dirty_fields:
                self._pending_dirty_fields[form_id] = {"app_id": app_id, "items": []}
            self._pending_dirty_fields[form_id]["items"].append(item)
            print(f"[DIRTY-STASH] Stashed basedata field for {form_id} (setItemByIdFromClient returned []): {field_key}={value_id}")
        return resp

    def _flush_pending_dirty_fields(self):
        """保存前自动 flush 所有待落库脏字段。

        遍历所有有待落库脏字段的表单，为每个表单自动发送一个 changeYear 请求，
        将脏字段值通过 postData 传递给服务端。这是 updateValue/setItemByIdFromClient
        在回放环境中被静默忽略时的 fallback 机制。
        """
        for form_id, pending in list(self._pending_dirty_fields.items()):
            items = pending.get("items", [])
            if not items:
                continue
            app_id = pending.get("app_id", "hom")
            # 取出并清空待 flush 的字段
            items_to_flush = items[:]
            self._pending_dirty_fields.pop(form_id, None)
            # 找一个有效的 changeYear key
            cy_list = self._last_changeyear.get(form_id, [])
            cy_key, cy_args = (cy_list[-1] if cy_list else ("b_effectivedate", [2026]))
            print(f"[DIRTY-FLUSH] Flushing {len(items_to_flush)} fields for {form_id} via changeYear(key={cy_key}): {[i.get('k') for i in items_to_flush]}")
            try:
                resp = self.invoke(form_id, app_id, "changeYear", [{
                    "key": cy_key, "methodName": "changeYear", "args": cy_args,
                    "postData": [{}, items_to_flush],
                }])
                _has_u = any(isinstance(c, dict) and c.get("a") == "u" for c in (resp if isinstance(resp, list) else []))
                print(f"[DIRTY-FLUSH] Response has 'u' action: {_has_u}, resp={str(resp)[:300]}")
            except Exception as e:
                print(f"[DIRTY-FLUSH] Failed for {form_id}: {e}")
                log.warning(f"[DIRTY-FLUSH] Failed for {form_id}: {e}")

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


def _iter_action_commands(node: Any):
    """Yield action command dicts, including nested sendDynamicFormAction payloads."""
    if isinstance(node, dict):
        if "a" in node:
            yield node
        for key in ("p", "actions", "args"):
            child = node.get(key)
            if isinstance(child, (list, dict)):
                yield from _iter_action_commands(child)
    elif isinstance(node, list):
        for item in node:
            yield from _iter_action_commands(item)


def has_error_action(resp: Any) -> list[str]:
    """扫响应错误消息（含嵌套 action），返回错误文本列表。"""
    errors = []
    seen = set()
    success_kw = ("成功", "已保存", "已提交", "已生效", "已审核", "已完成", "操作成功")
    error_kw = (
        "失败", "错误", "不能", "必填", "缺失", "为空", "不能为空", "请填写",
        "请选择", "不允许", "必须", "不合法", "无效", "异常",
    )

    def add_error(text: str) -> None:
        text = str(text or "")[:150]
        if text and text not in seen:
            seen.add(text)
            errors.append(text)

    if isinstance(resp, dict):
        # 空 dict 是苍穹常见的正常空响应，不判错。只处理非空错误摘要。
        for key in ("msg", "message", "detail", "error", "errorMsg", "errMsg"):
            val = resp.get(key)
            if not val:
                continue
            text = str(val)
            if any(kw in text for kw in success_kw):
                continue
            if key in {"error", "errorMsg", "errMsg"} or any(kw in text for kw in error_kw):
                add_error(f"[Protocol] {text}")
        if resp.get("success") is False:
            add_error("[Protocol] success=false")
        if resp.get("status") is False:
            add_error("[Protocol] status=false")
        code = resp.get("errorCode") or resp.get("code")
        if code and str(code) not in {"0", "200"}:
            add_error(f"[Protocol] errorCode={code}")
        return errors

    if not isinstance(resp, list):
        return errors

    for cmd in _iter_action_commands(resp):
        a = cmd.get("a")
        if a in ("showErrMsg",):
            for item in cmd.get("args", []):
                if item:
                    add_error(str(item))
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    t = p.get("errorTitle") or ""
                    i = p.get("errorInfo") or ""
                    if t or i:
                        add_error(f"{t} | {str(i)[:150]}")
        if a == "ShowNotificationMsg":
            # P0-2 优化：苍穹 Notification 的 type 字段是可靠信号
            #   - type=0  → info/success（真实 HAR 证实：saveandeffect 成功返回 type=0 "保存并生效成功"）
            #   - type=1  → warning
            #   - type=2  → error
            #   - type=3+ → 其他告警
            # 先按 type 判定；type 缺失或非 0 再按关键词白名单兜底，避免老响应误判。
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    content = str(p.get("content") or "")
                    if not content:
                        continue
                    ntype = p.get("type")
                    # type=0 明确是信息/成功类，直接放行
                    if ntype == 0:
                        continue
                    # 成功类 / 信息通知类不算错误（type 缺失场景的兜底）
                    success_kw = ("成功", "已保存", "已提交", "已生效", "已审核", "已完成",
                                  "已设置", "已清空", "已更新", "已调整", "已同步",
                                  "属于非", "自动", "将关闭")
                    if any(kw in content for kw in success_kw):
                        continue
                    add_error(f"[Notification] {content[:150]}")
        if a == "showConfirm":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    cid = p.get("id") or ""
                    msg = p.get("msg") or ""
                    # pagetimeout / 会话超时 = 表单会话失效，是真错误
                    if cid == "pagetimeout" or "会话超时" in msg or "超时" in msg:
                        add_error(f"[Timeout] {msg[:150]}")
        if a == "showFormValidMsg":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    msg = p.get("msg") or p.get("message") or ""
                    if msg:
                        add_error(f"[Validation] {str(msg)[:150]}")
        if a == "showMessage":
            for p in cmd.get("p", []):
                if isinstance(p, dict):
                    msg = str(p.get("msg") or p.get("message") or "").strip()
                    detail = str(p.get("detail") or "").strip()
                    text = "\n".join(part for part in (msg, detail) if part)
                    if not text or any(kw in text for kw in success_kw):
                        continue
                    message_type = p.get("messageType")
                    is_negative_type = isinstance(message_type, (int, float)) and message_type < 0
                    if is_negative_type or any(kw in text for kw in error_kw):
                        add_error(f"[Message] {text[:150]}")
    return errors
