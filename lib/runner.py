"""用例 Runner：读 YAML 用例 → 调 CosmicFormReplay 逐步执行 → 打印结果报告

YAML 用例 schema（最小可用）：

    name: <case-id>
    description: ...
    env:
      base_url:       <url>  或  ${env:COSMIC_BASE_URL}
      username:       ...
      password:       ...
      datacenter_id:  ...
    vars:
      any_key: <value>            # 可用 ${timestamp}/${rand:N}/${today} 辅助
    main_form_id: <form_id>       # 可选，主表单（runner 会先 open）
    steps:
      - id: <step-id>
        type: open_form | invoke | update_fields | pick_basedata |
              click_toolbar | sleep
        form_id: <form_id>
        app_id: <app_id>
        ...                       # 每种 type 字段见下文 STEP_HANDLERS
        optional: true            # 可选：失败不终止流程
        capture: <var-name>       # 可选：把响应存到 vars.<var-name>
    assertions:
      - type: no_error_actions
        last_step: true
      - type: no_save_failure    # 检查 bos_operationresult

取值系统：字符串里的 ${ref} 会被 resolve：
  ${vars.xxx}                 vars 内字段
  ${env:ENV_NAME}             系统环境变量
  ${env:ENV_NAME:default}     带默认值
  ${timestamp}                当前毫秒
  ${today}                    YYYY-MM-DD
  ${rand:N}                   N 位随机数字
  ${uuid}                     uuid4 hex
"""
from __future__ import annotations

import argparse
import json
import logging
import os
import random
import re
import sys
import time
import uuid
from datetime import date, datetime
from pathlib import Path
from typing import Any

from .replay import (
    CosmicError, LoginError, ProtocolError, BusinessError,
    CosmicFormReplay, login,
)
from .diagnoser import (
    extract_save_errors, summarize_response, format_error_report, has_error_action,
)
from .advisor import analyze_errors, format_fixes


log = logging.getLogger("cosmic_replay.runner")


# =============================================================
# YAML 解析（最小实现，不依赖 pyyaml）
# =============================================================
def load_yaml(path: Path) -> dict:
    """尝试 pyyaml，回退到轻量解析器"""
    try:
        import yaml  # type: ignore
        with path.open(encoding="utf-8") as f:
            return yaml.safe_load(f)
    except ImportError:
        pass
    # 回退
    return _parse_yaml_light(path.read_text(encoding="utf-8"))


def _parse_yaml_light(text: str) -> dict:
    """最小 YAML 解析器（仅覆盖本 skill 生成/维护的用例 YAML 子集）。
    支持：dict、list、str/int/float/bool/null scalar、内联 JSON（[...] 或 {...}）、# 注释。
    """
    lines = []
    for raw in text.splitlines():
        # 去注释（简易，不处理引号里的 #）
        s = raw.rstrip()
        if not s.strip() or s.strip().startswith("#"):
            continue
        # 去行尾注释
        if " #" in s:
            # 不在字符串内才去——简易：只处理 `# ` 前无引号
            in_str = False
            q = None
            cut = -1
            for i, c in enumerate(s):
                if c in ('"', "'"):
                    if in_str and q == c: in_str = False; q = None
                    elif not in_str: in_str = True; q = c
                elif c == "#" and not in_str and (i == 0 or s[i-1] == " "):
                    cut = i
                    break
            if cut >= 0:
                s = s[:cut].rstrip()
                if not s: continue
        lines.append(s)

    idx = [0]

    def _indent(s: str) -> int:
        return len(s) - len(s.lstrip(" "))

    def _scalar(v: str) -> Any:
        v = v.strip()
        if not v: return ""
        if v in ("null", "~", "Null", "NULL"): return None
        if v in ("true", "True", "TRUE"): return True
        if v in ("false", "False", "FALSE"): return False
        # 内联 JSON
        if (v.startswith("[") and v.endswith("]")) or (v.startswith("{") and v.endswith("}")):
            try:
                return json.loads(v)
            except Exception:
                pass
        # 数字
        try:
            if "." in v: return float(v)
            return int(v)
        except Exception:
            pass
        # 引号字符串
        if (v.startswith('"') and v.endswith('"')) or (v.startswith("'") and v.endswith("'")):
            try:
                return json.loads(v) if v[0] == '"' else v[1:-1]
            except Exception:
                return v[1:-1]
        return v

    def parse_block(base_indent: int) -> Any:
        # 看下一行判断是 dict 还是 list
        if idx[0] >= len(lines):
            return None
        first = lines[idx[0]]
        if _indent(first) < base_indent:
            return None
        if first.lstrip().startswith("- "):
            return parse_list(base_indent)
        return parse_dict(base_indent)

    def parse_dict(base_indent: int) -> dict:
        d: dict = {}
        while idx[0] < len(lines):
            line = lines[idx[0]]
            ind = _indent(line)
            if ind < base_indent: break
            if ind > base_indent:
                # 错误缩进？跳过
                idx[0] += 1
                continue
            content = line[ind:]
            if content.startswith("- "):
                break
            # key: [value]
            if ":" not in content:
                idx[0] += 1
                continue
            key, _, rest = content.partition(":")
            key = key.strip()
            # 去除 key 的引号
            if (key.startswith('"') and key.endswith('"')) or (key.startswith("'") and key.endswith("'")):
                key = key[1:-1]
            rest = rest.strip()
            idx[0] += 1
            if rest == "":
                # 子块
                if idx[0] < len(lines):
                    child_ind = _indent(lines[idx[0]])
                    if child_ind > base_indent:
                        d[key] = parse_block(child_ind)
                        continue
                d[key] = None
            else:
                d[key] = _scalar(rest)
        return d

    def parse_list(base_indent: int) -> list:
        lst: list = []
        while idx[0] < len(lines):
            line = lines[idx[0]]
            ind = _indent(line)
            if ind < base_indent: break
            content = line[ind:]
            if not content.startswith("- "):
                break
            if ind != base_indent:
                break
            rest = content[2:]
            idx[0] += 1
            if ":" in rest and not rest.startswith(("[", "{", '"', "'")):
                # 列表项是 dict 的第一行
                # 构造虚拟 dict 块，把这行视作 base_indent+2 的第一个 k:v
                item_ind = base_indent + 2
                # 把剩余内容回填为 dict 的首行
                # 简化：往前 unshift 虚拟行
                first_kv = rest
                d: dict = {}
                key, _, v = first_kv.partition(":")
                key = key.strip()
                v = v.strip()
                if v == "":
                    if idx[0] < len(lines):
                        child_ind = _indent(lines[idx[0]])
                        if child_ind > base_indent:
                            d[key] = parse_block(child_ind)
                else:
                    d[key] = _scalar(v)
                # 继续解析同一 item 后续 kv（缩进必须 > base_indent）
                while idx[0] < len(lines):
                    line2 = lines[idx[0]]
                    ind2 = _indent(line2)
                    if ind2 <= base_indent: break
                    content2 = line2[ind2:]
                    if content2.startswith("- "): break
                    if ":" not in content2:
                        idx[0] += 1
                        continue
                    k2, _, v2 = content2.partition(":")
                    k2 = k2.strip(); v2 = v2.strip()
                    idx[0] += 1
                    if v2 == "":
                        if idx[0] < len(lines):
                            cid = _indent(lines[idx[0]])
                            if cid > ind2:
                                d[k2] = parse_block(cid)
                                continue
                        d[k2] = None
                    else:
                        d[k2] = _scalar(v2)
                lst.append(d)
            else:
                lst.append(_scalar(rest))
        return lst

    if not lines:
        return {}
    return parse_dict(_indent(lines[0]))


# =============================================================
# 变量解析
# =============================================================
_VAR_RE = re.compile(r"\$\{([^}]+)\}")


def resolve_vars(obj: Any, vars_ns: dict) -> Any:
    """递归把 ${...} 占位符替换成实际值"""
    if isinstance(obj, str):
        return _resolve_str(obj, vars_ns)
    if isinstance(obj, dict):
        return {k: resolve_vars(v, vars_ns) for k, v in obj.items()}
    if isinstance(obj, list):
        return [resolve_vars(x, vars_ns) for x in obj]
    # ⭐ 安全兜底：YAML 自动解析出的 date/datetime 对象转成字符串
    if isinstance(obj, (date, datetime)):
        return obj.isoformat()
    return obj


def _resolve_str(s: str, vars_ns: dict) -> Any:
    def repl(m: re.Match) -> str:
        ref = m.group(1).strip()
        return str(_resolve_ref(ref, vars_ns))
    # 特例：整串就是 ${xxx}，可能需要返回非字符串类型
    m = _VAR_RE.fullmatch(s)
    if m:
        return _resolve_ref(m.group(1).strip(), vars_ns)
    return _VAR_RE.sub(repl, s)


def _resolve_ref(ref: str, vars_ns: dict) -> Any:
    if ref.startswith("vars."):
        key = ref[5:]
        return vars_ns.get(key, f"${{UNRESOLVED:{ref}}}")
    if ref.startswith("env:"):
        body = ref[4:]
        if ":" in body:
            name, default = body.split(":", 1)
            return os.environ.get(name.strip(), default.strip())
        return os.environ.get(body.strip(), "")
    if ref == "timestamp":
        return str(int(time.time() * 1000))
    if ref == "today":
        return datetime.now().strftime("%Y-%m-%d")
    if ref == "now":
        return datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    if ref.startswith("rand:"):
        n = int(ref[5:])
        return "".join(random.choices("0123456789", k=n))
    if ref == "uuid":
        return uuid.uuid4().hex
    # 最后兜底：如果 ref 是一个已声明的 var 名（不带 vars. 前缀），也能解析
    # 方便 vars 块内互相引用，例如 test_name: "xx${test_number}"
    if ref in vars_ns:
        return vars_ns[ref]
    return f"${{UNRESOLVED:{ref}}}"


# =============================================================
# Step 处理器
# =============================================================
STEP_HANDLERS = {}


def step_handler(name: str):
    def deco(fn):
        STEP_HANDLERS[name] = fn
        return fn
    return deco


@step_handler("open_form")
def _h_open_form(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    form_id = step["form_id"]
    app_id = step["app_id"]
    lazy = step.get("lazy", True)  # 默认 lazy=True 复用缓存；设 false 强制重新 getConfig
    # 门户类表单（bos_portal_*）需要用 rootPageId 打开，否则拿到的 pageId 是空壳
    if form_id.startswith("bos_portal"):
        pid = replay.open_portal(form_id, app_id, lazy=lazy)
    else:
        pid = replay.open_form(form_id, app_id, lazy=lazy)
    log.debug(f"    open_form({form_id}) → pageId={pid}")
    return {"page_id": pid}


@step_handler("invoke")
def _h_invoke(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    action = {
        "key": step.get("key", ""),
        "methodName": step.get("method", ""),
        "args": step.get("args", []),
        "postData": step.get("post_data", [{}, []]),
    }
    # invalidate_pages: 执行前清除指定表单的旧 pageId（如 saveandeffect 后表单上下文已变）
    for fid in step.get("invalidate_pages", []):
        replay.page_ids.pop(fid, None)
    resp = replay.invoke(step["form_id"], step["app_id"], step["ac"], [action])

    ac = step.get("ac", "")

    # ⭐ 规则13：menuItemClick 后自动计算并绑定 L2 pageId
    # 苍穹菜单导航链：menuItemClick → addVirtualTab → selectTab → 主表单在 L2 pageId 上操作
    # L2 pageId 公式：{menuId}root{session.root_base_id}
    # 如果不主动绑定，后续 open_form(getConfig) 会获取一个独立的、与菜单上下文无关的 pageId，
    # 导致 addnew/save 等操作发送到错误的页面，服务端不会正确初始化表单，自动带出字段丢失。
    if ac == "menuItemClick":
        args = step.get("args", [])
        if args and isinstance(args[0], dict):
            menu_id = str(args[0].get("menuId", ""))
            if menu_id and replay.s.root_base_id:
                l2_pid = f"{menu_id}root{replay.s.root_base_id}"
                target = step.get("target_form") or ctx.get("main_form_id")
                if target:
                    old_pid = replay.page_ids.get(target, "(none)")
                    replay.page_ids[target] = l2_pid
                    log.info(f"[menuItemClick] L2 pageId for {target}: {l2_pid} (was: {old_pid[:30]})")

    # saveandeffect / submitandeffect 后，被操作表单的 pageId 通常已失效
    # 但某些场景（如连续新增）服务端保持 pageId 不变，此时需 keep_page: true
    if ac in ("saveandeffect", "submitandeffect", "save", "submit"):
        if not step.get("keep_page"):
            target_form = step["form_id"]
            replay.page_ids.pop(target_form, None)
            log.debug(f"    [{ac}] invalidated pageId for {target_form}")
        else:
            log.debug(f"    [{ac}] keep_page=true, pageId retained for {step['form_id']}")
    return resp


@step_handler("update_fields")
def _h_update_fields(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    fields = step.get("fields", {}) or {}
    row = step.get("row_index", -1)
    return replay.update_fields(step["form_id"], step["app_id"], fields, row_index=row)


@step_handler("pick_basedata")
def _h_pick_basedata(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    return replay.pick_basedata(
        step["form_id"], step["app_id"],
        step["field_key"], str(step["value_id"]),
    )


@step_handler("click_toolbar")
def _h_click_toolbar(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    return replay.click_toolbar(
        step["form_id"], step["app_id"], step.get("ac", "itemClick"),
        step["item_id"], step.get("click_id"),
        toolbar_key=step.get("toolbar_key", "toolbarap"),
        post_data=step.get("post_data"),
    )


@step_handler("click_menu")
def _h_click_menu(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    """点击左侧菜单项 - 自动完成 L1(portal)→L2(list) 的 pageId 跃迁。

    YAML:
      - type: click_menu
        menu_id: "1443450410974114816"     # 必填，菜单项主键
        cloud_id: "0MUWQ6HSY5JA"            # 必填，云 id
        menu_app_id: "217WYC/L9U7E"         # 必填，应用 id（菜单元数据里的 appId）
        portal_form: bos_portal_myapp_new  # 可选，默认 bos_portal_myapp_new
        portal_app: bos                     # 可选
    """
    return replay.click_menu(
        menu_id=str(step["menu_id"]),
        cloud_id=str(step["cloud_id"]),
        menu_app_id=str(step["menu_app_id"]),
        target_form=step.get("target_form"),
        portal_form=step.get("portal_form", "bos_portal_myapp_new"),
        portal_app=step.get("portal_app", "bos"),
    )


@step_handler("sleep")
def _h_sleep(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    time.sleep(float(step.get("seconds", 1)))
    return None


# =============================================================
# 断言
# =============================================================
ASSERTION_HANDLERS = {}


def assertion_handler(name: str):
    def deco(fn):
        ASSERTION_HANDLERS[name] = fn
        return fn
    return deco


@assertion_handler("no_error_actions")
def _a_no_errors(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step")
    if step_id:
        resp = ctx["step_responses"].get(step_id)
        if resp is None:
            return False, f"找不到步骤 '{step_id}' 的响应"
    elif assert_spec.get("last_step"):
        resp = ctx["last_step_response"]
    else:
        resp = ctx["last_response"]
    errs = has_error_action(resp)
    if errs:
        return False, f"发现 {len(errs)} 条错误消息: {errs[:3]}"
    return True, ""


@assertion_handler("no_save_failure")
def _a_no_save_failure(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step", "save")
    resp = ctx["step_responses"].get(step_id)
    if resp is None:
        return False, f"找不到步骤 '{step_id}' 的响应"
    errs = extract_save_errors(resp, ctx["replay"])
    if errs:
        # 把错误也塞给 ctx 供 advisor 用
        ctx.setdefault("collected_errors", []).extend(errs)
        return False, f"保存被拦截: {errs[:5]}"
    return True, ""


@assertion_handler("response_contains")
def _a_response_contains(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step")
    needle = assert_spec["needle"]
    resp = ctx["step_responses"].get(step_id) if step_id else ctx["last_response"]
    if resp is None:
        return False, f"找不到步骤 '{step_id}' 的响应"
    if needle in json.dumps(resp, ensure_ascii=False):
        return True, ""
    return False, f"响应里没找到 '{needle}'"


# =============================================================
# Runner 主流程
# =============================================================
class RunResult:
    def __init__(self):
        self.steps: list[dict] = []
        self.assertions: list[dict] = []
        self.fixes: list = []   # list[advisor.Fix]
        self.start_ts = time.time()
        self.end_ts: float | None = None

    @property
    def duration(self) -> float:
        return (self.end_ts or time.time()) - self.start_ts

    @property
    def passed(self) -> bool:
        step_ok = all(s["ok"] or s.get("optional") for s in self.steps)
        assert_ok = all(a["ok"] for a in self.assertions)
        return step_ok and assert_ok

    def print_report(self, out=sys.stdout):
        # 使用 ASCII 符号，避免 Windows gbk 控制台 UnicodeEncodeError
        line = "=" * 60
        print("\n" + line, file=out)
        status = "[PASS]" if self.passed else "[FAIL]"
        print(f"{status}  duration {self.duration:.1f}s", file=out)
        print(line, file=out)
        for s in self.steps:
            if s["ok"]: mark = "[ok] "
            elif s.get("optional"): mark = "[opt]"
            else: mark = "[ERR]"
            print(f"  {mark} [{s['id']}] {s['type']} {s.get('detail','')}", file=out)
            if not s["ok"]:
                print(f"        ERROR: {s['error']}", file=out)
        if self.assertions:
            print("\nASSERTIONS:", file=out)
            for a in self.assertions:
                mark = "[ok] " if a["ok"] else "[ERR]"
                print(f"  {mark} {a['type']}", file=out)
                if not a["ok"]:
                    print(f"        {a['msg']}", file=out)
        # 失败时打修复建议
        if self.fixes:
            print(format_fixes(self.fixes), file=out)


def run_case(case: dict, on_event=None) -> RunResult:
    """执行一份用例。返回 RunResult。

    on_event: 可选回调 callable(event_type: str, payload: dict)。
              用于 Web UI 的 SSE 推送。None = 纯 CLI，行为不变。
    """
    def emit(event_type: str, payload: dict | None = None):
        if on_event is not None:
            try:
                on_event(event_type, payload or {})
            except Exception:
                pass

    result = RunResult()
    emit("case_start", {"name": case.get("name", "?"), "description": case.get("description", "")})

    # 1. 解析 env
    env = case.get("env", {}) or {}
    env = {k: resolve_vars(v, {}) for k, v in env.items()}
    base_url = env.get("base_url")

    # 占位符兜底：常见错误就是用户没把 YOUR_XXX 改成真值
    def _is_placeholder(v) -> bool:
        if not v: return True
        s = str(v).strip().upper()
        return s.startswith("YOUR_") or s.startswith("${ENV:") or s.startswith("${UNRESOLVED")

    missing: list[str] = []
    if _is_placeholder(base_url): missing.append("base_url")
    if _is_placeholder(env.get("datacenter_id")): missing.append("datacenter_id")
    if _is_placeholder(env.get("username")): missing.append("username（或对应环境变量未设置）")
    if _is_placeholder(env.get("password")): missing.append("password（或对应环境变量未设置）")
    if missing:
        msg = ("以下字段未配置或仍是占位符: " + ", ".join(missing) +
               "。请打开 Web UI → ⚙ 配置 → 环境列表，或编辑 config/envs/*.yaml 填真实值。")
        emit("case_error", {"error": msg})
        raise ValueError(msg)

    # 2. vars
    vars_ns: dict[str, Any] = {}
    for k, v in (case.get("vars") or {}).items():
        if k.startswith("_"):
            continue
        vars_ns[k] = resolve_vars(v, vars_ns)

    # 3. 登录
    print(f"[login] {base_url} as {env.get('username')}")
    emit("login_start", {"base_url": base_url, "username": env.get("username")})
    sess = login(base_url, env["username"], env["password"],
                 datacenter_id=env.get("datacenter_id"))
    print(f"  user_id={sess.user_id}")
    emit("login_ok", {"user_id": sess.user_id})

    # 4. 初始化回放器
    replay = CosmicFormReplay(sess, sign_required=bool(case.get("sign_required", True)))
    replay.init_root()
    print(f"  root_page_id={sess.root_page_id}")

    # 注入 session 级别的内置变量，供 YAML steps 中引用
    # ${session.root_page_id}   → 本次会话根 pageId
    # ${session.root_base_id}   → 根 pageId 的 32-hex 部分（用于拼 L2 pageId）
    vars_ns["session.root_page_id"] = sess.root_page_id
    vars_ns["session.root_base_id"] = sess.root_base_id

    emit("session_ready", {
        "root_page_id": sess.root_page_id,
        "resolved_vars": _build_display_vars(vars_ns),
    })

    # 主表单预开
    main_form = case.get("main_form_id")
    if main_form:
        for s in case.get("steps") or []:
            if s.get("type") == "open_form" and s.get("form_id") == main_form:
                break
        else:
            app_id = _guess_app_id(main_form, case)
            replay.open_form(main_form, app_id)

    # 5. 执行 steps
    ctx: dict[str, Any] = {
        "replay": replay,
        "vars": vars_ns,
        "step_responses": {},
        "last_response": None,
        "last_step_response": None,
        "response_history": [],   # advisor 用，累积所有响应
        "main_form_id": main_form,  # ⭐ 供 menuItemClick L2 pageId 自动绑定
    }

    for raw_step in case.get("steps") or []:
        step = resolve_vars(raw_step, vars_ns)
        stype = step.get("type")
        sid = step.get("id") or f"<{stype}>"
        optional = bool(step.get("optional"))
        print(f"\n[{sid}] {stype}", end="")
        detail = _step_detail(step)
        if detail:
            print(f"  {detail}", end="")
        print()

        # ⭐ 构建解析后的请求摘要（供前端展示完整请求参数）
        resolved_request = _build_resolved_request(step)

        step_start = time.time()
        # 优先使用HAR提取的description（中文描述），否则使用_step_label推断
        label = step.get("description") or _step_label(step)
        emit("step_start", {
            "id": sid, "type": stype, "label": label, "detail": detail, "optional": optional,
            "resolved_request": resolved_request,
        })

        handler = STEP_HANDLERS.get(stype)
        if not handler:
            err_msg = f"未知 step type: {stype}"
            result.steps.append({
                "id": sid, "type": stype, "ok": False,
                "error": err_msg, "optional": optional,
            })
            emit("step_fail", {"id": sid, "error": err_msg,
                               "duration_ms": int((time.time() - step_start) * 1000)})
            if not optional:
                break
            continue

        try:
            resp = handler(step, replay, ctx)
            ctx["last_response"] = resp
            ctx["last_step_response"] = resp
            ctx["step_responses"][sid] = resp
            if resp is not None:
                ctx["response_history"].append(resp)

            # 检测错误 action
            errs = has_error_action(resp) if resp else []
            if errs and not optional:
                # 进一步尝试从 bos_operationresult 拉详情
                save_errs = extract_save_errors(resp, replay) if resp else errs
                collected = save_errs or errs
                result.steps.append({
                    "id": sid, "type": stype, "ok": False, "optional": optional,
                    "detail": detail,
                    "error": "; ".join(collected[:5]),
                    "_errors": collected,
                })
                _print_error_detail(sid, collected, resp)
                resp_snapshot = _truncate_response(resp)
                emit("step_fail", {
                    "id": sid, "errors": collected[:5],
                    "duration_ms": int((time.time() - step_start) * 1000),
                    "response": resp_snapshot,
                })
                if stype in ("invoke", "update_fields", "pick_basedata", "click_toolbar"):
                    break
            else:
                result.steps.append({
                    "id": sid, "type": stype, "ok": True, "optional": optional,
                    "detail": detail,
                })
                # ⭐ 推送完整响应数据供前端展示
                resp_snapshot = _truncate_response(resp)
                emit("step_ok", {
                    "id": sid,
                    "duration_ms": int((time.time() - step_start) * 1000),
                    "response": resp_snapshot,
                })
                if step.get("capture"):
                    vars_ns[step["capture"]] = resp
        except BusinessError as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"业务错误: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"业务错误: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000)})
            if not optional: break
        except ProtocolError as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"协议错误: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"协议错误: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000)})
            if not optional: break
        except Exception as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"{type(e).__name__}: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"{type(e).__name__}: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000)})
            if not optional: break

    # 6. 断言（先把 ${vars.xxx} 解析掉）
    for a_raw in case.get("assertions") or []:
        a = resolve_vars(a_raw, vars_ns)
        atype = a.get("type")
        handler = ASSERTION_HANDLERS.get(atype)
        if not handler:
            result.assertions.append({
                "type": atype, "ok": False, "msg": f"未知断言: {atype}",
            })
            emit("assertion_fail", {"type": atype, "msg": f"未知断言: {atype}"})
            continue
        try:
            ok, msg = handler(a, ctx)
            result.assertions.append({"type": atype, "ok": ok, "msg": msg})
            emit("assertion_ok" if ok else "assertion_fail",
                 {"type": atype, "msg": msg})
        except Exception as e:
            result.assertions.append({
                "type": atype, "ok": False, "msg": f"断言执行异常: {e}",
            })
            emit("assertion_fail", {"type": atype, "msg": f"异常: {e}"})

    # 7. 失败时生成修复建议
    if not result.passed:
        # 收集所有错误：step 级别的 + 断言提取出的
        all_errors: list[str] = []
        for s in result.steps:
            if not s["ok"] and s.get("_errors"):
                all_errors.extend(s["_errors"])
        all_errors.extend(ctx.get("collected_errors") or [])
        # 去重
        seen: set[str] = set()
        dedup: list[str] = []
        for e in all_errors:
            if e not in seen:
                seen.add(e)
                dedup.append(e)
        if dedup:
            try:
                result.fixes = analyze_errors(dedup, ctx.get("response_history", []))
                # 推送修复建议事件
                emit("fixes_ready", {
                    "fixes": [
                        {
                            "diagnosis": f.diagnosis,
                            "error_type": f.error_type,
                            "field_caption": f.field_caption,
                            "field_key": f.field_key,
                            "suggested_value": f.suggested_value,
                            "patch_yaml": f.patch_yaml,
                            "confidence": f.confidence,
                        }
                        for f in result.fixes
                    ],
                })
            except Exception as e:
                log.warning(f"advisor 执行异常，跳过建议: {e}")

    result.end_ts = time.time()
    emit("case_done", {
        "passed": result.passed,
        "duration_s": round(result.duration, 2),
        "step_count": len(result.steps),
        "step_ok": sum(1 for s in result.steps if s.get("ok")),
        "step_fail": sum(1 for s in result.steps if not s.get("ok") and not s.get("optional")),
        "assertion_ok": sum(1 for a in result.assertions if a.get("ok")),
        "assertion_fail": sum(1 for a in result.assertions if not a.get("ok")),
    })
    return result


def _guess_app_id(form_id: str, case: dict) -> str:
    """从 case 的 steps 里找 form_id 对应的 app_id"""
    for s in case.get("steps") or []:
        if s.get("form_id") == form_id and s.get("app_id"):
            return s["app_id"]
    # 粗粒度：前缀匹配
    return form_id.split("_", 1)[0] if "_" in form_id else "bos"


_VAR_LABEL_MAP = {
    "test_number": "编码",
    "test_name": "名称",
    "test_phone": "电话",
    "test_cert_no": "证件号",
}


def _build_display_vars(vars_ns: dict) -> list[dict]:
    """提取所有用户声明的变量（排除内部/session变量），附带中文标签。

    返回格式: [{"key": "test_number", "label": "编码", "value": "kdtest_xxx"}, ...]
    """
    results: list[dict] = []
    for k, v in vars_ns.items():
        if k.startswith("_") or k.startswith("session."):
            continue
        # 优先用已知标签，否则用变量名本身
        label = _VAR_LABEL_MAP.get(k)
        if not label:
            # 尝试后缀匹配
            for suffix, lbl in [("number", "编码"), ("name", "名称"),
                                ("phone", "电话"), ("cert", "证件号"),
                                ("code", "编码")]:
                if suffix in k.lower():
                    label = lbl
                    break
        if not label:
            label = k
        results.append({
            "key": k,
            "label": label,
            "value": str(v),
        })
    return results


def _build_resolved_request(step: dict) -> dict:
    """构建解析后的请求摘要，供前端展示完整请求参数。"""
    t = step.get("type")
    req: dict[str, Any] = {
        "type": t,
        "form_id": step.get("form_id", ""),
        "app_id": step.get("app_id", ""),
    }
    if t == "invoke":
        req["ac"] = step.get("ac", "")
        req["key"] = step.get("key", "")
        req["method"] = step.get("method", "")
        req["args"] = step.get("args", [])
        req["post_data"] = step.get("post_data", [{}, []])
        if step.get("keep_page"):
            req["keep_page"] = True
    elif t == "update_fields":
        req["fields"] = step.get("fields", {})
    elif t == "pick_basedata":
        req["field_key"] = step.get("field_key", "")
        req["value_id"] = step.get("value_id", "")
    elif t == "open_form":
        pass  # form_id/app_id already included
    return req


def _truncate_response(resp: Any, max_len: int = 8000) -> Any:
    """截断过长的响应数据，避免 SSE 推送过大。"""
    if resp is None:
        return None
    try:
        s = json.dumps(resp, ensure_ascii=False)
        if len(s) <= max_len:
            return resp
        # 超长：返回截断的字符串形式
        return {"_truncated": True, "_length": len(s), "_preview": s[:max_len]}
    except (TypeError, ValueError):
        s = str(resp)
        if len(s) > max_len:
            return {"_truncated": True, "_length": len(s), "_preview": s[:max_len]}
        return resp


def _step_detail(step: dict) -> str:
    t = step.get("type")
    if t == "invoke":
        return f"{step.get('form_id')}/{step.get('ac')}  key={step.get('key','')}  method={step.get('method','')}"
    if t == "open_form":
        return step.get("form_id", "")
    if t == "update_fields":
        fs = step.get("fields", {})
        return f"{step.get('form_id')}  fields={list(fs.keys())}"
    if t == "pick_basedata":
        return f"{step.get('form_id')}  {step.get('field_key')}={step.get('value_id')}"
    return ""

def _step_label(step: dict) -> str:
    """生成步骤的中文描述标签"""
    t = step.get("type")
    
    # invoke操作的细分描述
    if t == "invoke":
        ac = step.get("ac", "")
        ac_labels = {
            "menuItemClick": "切换菜单",
            "saveandeffect": "保存并生效",
            "submitandeffect": "提交并生效",
            "addnew": "新增",
            "delete": "删除",
            "edit": "编辑",
            "submit": "提交",
            "save": "保存",
        }
        if ac in ac_labels:
            return ac_labels[ac]
        # 默认invoke描述
        method = step.get("method", "")
        if method:
            return f"执行{method}"
        return "执行操作"
    
    # 步骤类型的中文描述
    type_labels = {
        "open_form": "打开表单",
        "update_fields": "更新字段",
        "pick_basedata": "选择基础资料",
        "click_toolbar": "点击工具栏",
        "click_menu": "点击菜单",
        "sleep": "等待",
        "assert": "断言检查",
        "wait_loading": "等待加载",
    }
    
    if t in type_labels:
        return type_labels[t]
    return t or "未知步骤"


def _print_error_detail(step_id: str, errors: list[str], resp: Any):
    print(f"  [ERR] step '{step_id}' failed.")
    for i, e in enumerate(errors[:8], 1):
        print(f"    [{i}] {e}")
    summary = summarize_response(resp)
    if summary.get("actions"):
        print(f"    响应 actions: {', '.join(summary['actions'])}")


# =============================================================
# CLI
# =============================================================
def main():
    # Windows 控制台默认 gbk，强制 stdout 用 utf-8
    try:
        sys.stdout.reconfigure(encoding="utf-8", errors="replace")
        sys.stderr.reconfigure(encoding="utf-8", errors="replace")
    except Exception:
        pass
    ap = argparse.ArgumentParser(description="苍穹 Replay 用例执行器")
    sub = ap.add_subparsers(dest="cmd", required=True)

    p_run = sub.add_parser("run", help="运行单个用例")
    p_run.add_argument("case", type=Path, help="YAML 用例文件")
    p_run.add_argument("-v", "--verbose", action="store_true")

    args = ap.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(levelname)s %(name)s: %(message)s",
    )

    if args.cmd == "run":
        case = load_yaml(args.case)
        if not isinstance(case, dict):
            print(f"ERROR: 用例文件格式不对（期望 dict 根）: {args.case}", file=sys.stderr)
            sys.exit(2)
        try:
            result = run_case(case)
        except LoginError as e:
            print(f"✗ 登录失败: {e}", file=sys.stderr)
            sys.exit(3)
        except CosmicError as e:
            print(f"✗ 协议错误: {e}", file=sys.stderr)
            sys.exit(4)
        result.print_report()
        sys.exit(0 if result.passed else 1)


if __name__ == "__main__":
    main()