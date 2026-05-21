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
    CosmicFormReplay, CosmicSession, login, _is_l2_pageid,
)
from .diagnoser import (
    extract_save_errors, summarize_response, format_error_report, has_error_action,
)
from .advisor import analyze_errors, format_fixes
from .failure_analysis import classify_run_failure
from .repair_planner import build_repair_plan
from .field_resolver import FieldResolver, ResolveResult, _looks_like_internal_id
from .pageid_trace import (
    build_runtime_pageid_trace,
    step_allows_l2_pageid as _trace_step_allows_l2_pageid,
)


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
        if v in ("null", "~", "none", "Null", "NULL", "None", "NONE"): return None
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
    ref = ref.strip()
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


def _step_allows_l2_pageid(step: dict) -> bool:
    """Whether this step should keep the menu/list L2 pageId.

    Kingdee keeps list/tree/toolbar state on the L2 pageId. Replacing it with a
    pending L3 before addnew corrupts the form-model chain and later field
    writes can look "locked" even though the browser HAR succeeded.
    """
    return _trace_step_allows_l2_pageid(step)


def _validate_pageid_before_invoke(form_id: str, app_id: str, replay, step: dict, ctx: dict) -> None:
    """invoke 执行前的 pageId 有效性预验证

    仅对首次使用的 form_id 执行验证，通过 ctx["_validated_forms"] 避免重复。
    不对 loadData/open_form 步骤做预验证（避免递归）。
    """
    ac = step.get("ac", "")

    # 排除不需要预验证的操作
    _skip_actions = ("loadData", "open_form", "close_form", "startupflow", "doconfirm", "afterConfirm")
    if ac in _skip_actions:
        return
    # HAR 中明确处于 L2 列表/菜单上下文的动作不能被 pending L3 抢走。
    if _step_allows_l2_pageid(step):
        return

    # 避免重复校验
    validated = ctx.setdefault("_validated_forms", set())
    if form_id in validated:
        return

    # 标记已验证
    validated.add(form_id)

    # 场景1：pageId 完全缺失 → 自动 open_form + loadData
    if form_id not in replay.page_ids:
        try:
            pid = replay.open_form(form_id, app_id, lazy=False)
            if pid:
                replay.load_data(form_id, app_id)
                log.info(f"[pre-validate] {form_id}: 缺失pageId, 已自动open+load, pid={pid[:20]}...")
        except Exception as e:
            log.warning(f"[pre-validate] {form_id}: 自动open失败: {e}, 将由安全网兜底")
        return

    pid = replay.page_ids[form_id]

    # 场景2：L2 pageId 用于非 toolbar 操作 → 降级
    _toolbar_actions = {"startupflow", "doconfirm", "afterConfirm", "itemClick"}
    _is_toolbar = step.get("method") == "itemClick" or ac in _toolbar_actions

    if _is_l2_pageid(pid) and not _is_toolbar:
        # 尝试从 pending_by_app 获取 L3 pageId
        pending = replay._pending_by_app.get(app_id)
        if pending and not _is_l2_pageid(pending):
            replay.page_ids[form_id] = pending
            log.info(f"[pre-validate] {form_id}: L2降级→L3 via pending, pid={pending[:20]}...")
        else:
            # pending 不可用，重新打开
            try:
                new_pid = replay.open_form(form_id, app_id, lazy=False)
                if new_pid:
                    log.info(f"[pre-validate] {form_id}: L2替换为新L3, pid={new_pid[:20]}...")
            except Exception as e:
                log.warning(f"[pre-validate] {form_id}: L2替换失败: {e}")
        return

    # 场景3/4：检查是否已 loadData
    if form_id not in replay._loaded_forms and ac not in ("loadData",):
        try:
            replay.load_data(form_id, app_id)
            log.info(f"[pre-validate] {form_id}: 预热loadData成功")
        except Exception as e:
            log.warning(f"[pre-validate] {form_id}: 预热loadData失败: {e}, 将由安全网兜底")


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

    # ⭐ L2 pageId 有效性检查：防止菜单级 pageId 被误用于表单 loadData/open 操作
    # 但 toolbar 操作（如 startupflow、itemClick）应保留 L2 pageId，因为 toolbar 在 L2 页面上下文
    form_id = step["form_id"]
    pid = replay.page_ids.get(form_id)
    ac_name = step.get("ac", "")
    # 列表/树/工具栏动作使用 L2 pageId 是正确的（原始 HAR 中就是这样）。
    # 只有字段写入、基础资料选择等表单态动作才需要降级到 L3。
    if pid and _is_l2_pageid(pid) and not _step_allows_l2_pageid(step):
        log.warning(f"[invoke] {form_id}/{ac_name}: detected L2 pageId {pid[:30]}..., will attempt fallback")
        app_id = step.get("app_id", "")
        pending = replay._pending_by_app.get(app_id)
        if pending:
            log.info(f"[invoke] Using pending pageId from app {app_id}")
            replay.page_ids[form_id] = pending
            replay._pending_by_app.pop(app_id, None)

    # ⭐ pageId 有效性预验证
    _validate_pageid_before_invoke(
        step["form_id"], step.get("app_id", "bos"), replay, step, ctx
    )

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
                # 收集所有需要 L2 pageId 的目标表单
                targets = list(step.get("target_forms") or [])
                main_target = step.get("target_form") or ctx.get("main_form_id")
                if main_target and main_target not in targets:
                    targets.insert(0, main_target)
                for t in targets:
                    old_pid = replay.page_ids.get(t, "(none)")
                    replay.page_ids[t] = l2_pid
                    log.info(f"[menuItemClick] L2 pageId for {t}: {l2_pid} (was: {old_pid[:30]})")

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
    _auto_resolve_pick_basedata_step(step, replay, ctx)
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


def _expected_notification_needles(step: dict) -> list[str]:
    specs = step.get("expected_notifications") or step.get("expected_errors") or []
    needles: list[str] = []
    if isinstance(specs, (str, bytes)):
        specs = [specs]
    for item in specs:
        if isinstance(item, dict):
            text = item.get("content") or item.get("contains") or item.get("needle") or ""
        else:
            text = str(item or "")
        text = str(text or "").strip()
        if text:
            needles.append(text)
    return needles


def _split_expected_errors(step: dict, errors: list[str]) -> tuple[list[str], list[str]]:
    """Split response errors into HAR-recorded expected notifications and unexpected errors."""
    needles = _expected_notification_needles(step)
    if not needles or not errors:
        return [], errors
    expected: list[str] = []
    unexpected: list[str] = []
    for err in errors:
        if any(needle in err for needle in needles):
            expected.append(err)
        else:
            unexpected.append(err)
    return expected, unexpected


@assertion_handler("no_error_actions")
def _a_no_errors(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step")
    step_desc = ""
    if step_id:
        step_desc = (ctx.get("step_descriptions") or {}).get(step_id, "")
        resp = ctx["step_responses"].get(step_id)
        if resp is None:
            return False, f"找不到步骤 '{step_id}' 的响应"
    elif assert_spec.get("last_step"):
        resp = ctx["last_step_response"]
    else:
        resp = ctx["last_response"]
    errs = has_error_action(resp)
    if errs:
        where = f"【{step_desc}】" if step_desc else (f"步骤 {step_id}" if step_id else "最后一步")
        return False, f"{where} 发现 {len(errs)} 条错误消息: {errs[:3]}"
    # ⭐ 成功时也返回带业务含义的 msg，避免日志里 msg 为空
    if step_desc:
        return True, f"✅ 【{step_desc}】响应未出现苍穹错误 action（showErrMsg / ShowNotificationMsg-error）"
    return True, "✅ 响应未出现苍穹错误 action"


@assertion_handler("no_save_failure")
def _a_no_save_failure(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step", "save")
    step_desc = (ctx.get("step_descriptions") or {}).get(step_id, "")
    resp = ctx["step_responses"].get(step_id)
    if resp is None:
        return False, f"找不到步骤 '{step_id}' 的响应"
    errs = extract_save_errors(resp, ctx["replay"])
    if errs:
        # 把错误也塞给 ctx 供 advisor 用
        ctx.setdefault("collected_errors", []).extend(errs)
        where = f"【{step_desc}】" if step_desc else f"步骤 {step_id}"
        return False, f"{where} 保存被拦截: {errs[:5]}"
    # ⭐ 成功时 msg 带业务含义
    if step_desc:
        return True, f"✅ 【{step_desc}】写库成功（无字段级错误、无操作失败 action）"
    return True, "✅ 写库成功（无错误消息）"


@assertion_handler("expected_notification")
def _a_expected_notification(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    step_id = assert_spec.get("step")
    needle = str(assert_spec.get("contains") or assert_spec.get("needle") or "").strip()
    if not step_id:
        return False, "expected_notification 缺少 step"
    if not needle:
        return False, "expected_notification 缺少 contains"
    resp = ctx["step_responses"].get(step_id)
    if resp is None:
        return False, f"找不到步骤 '{step_id}' 的响应"
    errs = has_error_action(resp)
    if any(needle in err for err in errs):
        step_desc = (ctx.get("step_descriptions") or {}).get(step_id, "")
        where = f"【{step_desc}】" if step_desc else f"步骤 {step_id}"
        return True, f"✅ {where} 出现预期业务校验提示：{needle}"
    return False, f"步骤 '{step_id}' 未出现预期业务校验提示：{needle}"


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
# pick_fields 运行时值注入
# =============================================================
def _apply_pick_fields(case: dict):
    """运行前将 pick_fields 的用户修改值注入到对应步骤参数"""
    pick_fields = case.get("pick_fields") or {}
    if not pick_fields:
        return
    steps = case.get("steps") or []
    step_map = {s.get("id", ""): s for s in steps}

    for pf_id, pf_meta in pick_fields.items():
        if not isinstance(pf_meta, dict):
            continue

        # date_* -> 替换 update_fields 步骤中的日期字段
        # 对 date_* 字段，优先用 value_id（用户编辑的值），fallback 到 value_name
        if pf_id.startswith("date_"):
            field_key = pf_id[5:]  # 去掉 "date_" 前缀，如 date_bsed -> bsed
            value = pf_meta.get("value_id") or pf_meta.get("value_name", "")
            if not value:
                continue
            for step in steps:
                if step.get("type") == "update_fields":
                    fields = step.get("fields") or {}
                    if field_key in fields:
                        fv = fields[field_key]
                        if isinstance(fv, dict):
                            # 多语言字段：更新所有语言版本
                            for lang in list(fv.keys()):
                                fv[lang] = value
                        else:
                            fields[field_key] = value
            continue

        # env_*_treeview_focus -> 更新 addnew 步骤的 post_data 中 treeview.focus.id
        if pf_id.startswith("env_") and pf_id.endswith("_treeview_focus"):
            # 优先使用 value_id（数字ID），fallback 到 value_name
            inject_value = pf_meta.get("value_id") or pf_meta.get("value_name", "")
            if not inject_value:
                continue
            # 提取 step_id: env_click_addnew_treeview_focus -> click_addnew
            step_id = pf_id[4:-15]  # 去掉 "env_" 前缀和 "_treeview_focus" 后缀
            step = step_map.get(step_id)
            if step and step.get("post_data"):
                post_data = step["post_data"]
                if isinstance(post_data, list):
                    for pd_item in post_data:
                        if isinstance(pd_item, dict) and "treeview" in pd_item:
                            tv = pd_item["treeview"]
                            if isinstance(tv, dict) and "focus" in tv:
                                focus = tv["focus"]
                                if isinstance(focus, dict):
                                    focus["id"] = inject_value
                elif isinstance(post_data, dict) and "treeview" in post_data:
                    tv = post_data["treeview"]
                    if isinstance(tv, dict) and "focus" in tv:
                        focus = tv["focus"]
                        if isinstance(focus, dict):
                            focus["id"] = inject_value

        # pick_* → 覆盖匹配的 pick_basedata 步骤中的 value_id
        elif pf_id.startswith("pick_"):
            field_key = pf_meta.get("field_key") or pf_id[5:]  # 优先用 meta 中的 field_key，fallback 到去前缀
            inject_vid = pf_meta.get("value_id", "")
            inject_vname = pf_meta.get("value_name", "")
            inject_vcode = pf_meta.get("value_code", "")
            if inject_vid or inject_vname:
                applied = False
                for step in steps:
                    if step.get("type") == "pick_basedata" and step.get("field_key") == field_key:
                        if inject_vid:
                            step["value_id"] = str(inject_vid)
                        if inject_vname:
                            step["value_name"] = str(inject_vname)
                        elif pf_meta.get("manual_override") or pf_meta.get("resolve_status") == "manual":
                            step["value_name"] = ""
                        if inject_vcode:
                            step["value_code"] = str(inject_vcode)
                        step["_env_field_id"] = pf_id
                        step["_env_field_meta"] = pf_meta
                        step["auto_resolve"] = bool(pf_meta.get("auto_resolve"))
                        step["resolve_by"] = str(pf_meta.get("resolve_by") or step.get("resolve_by") or "")
                        log.debug(f"[pick inject] {pf_id} → step[{step.get('id', '')}].value_id={inject_vid}")
                        applied = True
                # MainOrgProp 等上下文字段可能以 update_fields 形式补偿写入；
                # 允许沿用 pick_* 配置面板去覆盖这些字段，保持 UI/配置方式一致。
                for step in steps:
                    if step.get("type") != "update_fields":
                        continue
                    fields = step.get("fields") or {}
                    if field_key not in fields:
                        continue
                    new_value = inject_vid or inject_vname
                    if new_value:
                        fields[field_key] = str(new_value)
                        log.debug(f"[pick inject->update_fields] {pf_id} → step[{step.get('id', '')}].fields[{field_key}]={new_value}")
                        applied = True
                if not applied:
                    log.debug(f"[pick inject] {pf_id} 未找到匹配 step，field_key={field_key}")

        # enum_* / bool_* / num_* -> 替换 update_fields 或 pick_basedata 步骤中的对应字段
        elif pf_id.startswith("enum_") or pf_id.startswith("bool_") or pf_id.startswith("num_"):
            field_key = pf_id.split("_", 1)[1]  # 去掉前缀
            value = pf_meta.get("value_name") or pf_meta.get("value_id", "")
            if not value:
                continue
            # 在 update_fields 步骤中查找并替换对应字段
            for step in steps:
                if step.get("type") == "update_fields":
                    fields = step.get("fields") or {}
                    if field_key in fields:
                        fields[field_key] = value
                        break
            # 也检查 pick_basedata 类型步骤
            # （枚举字段可能通过 setItemByIdFromClient 设值）
            for step in steps:
                if step.get("type") == "pick_basedata" and step.get("field_key") == field_key:
                    step["value_id"] = value
                    break


def _auto_resolve_pick_basedata_step(step: dict, replay: CosmicFormReplay, ctx: dict) -> None:
    """按当前环境自动解析 pick_basedata 的 value_id。

    安全策略：仅在 pick_fields 明确标记 auto_resolve 且 value_name 可用时触发；
    解析成功才覆盖 value_id，解析失败/歧义时保留原始值。
    """
    if not step.get("auto_resolve"):
        return

    pf_id = step.get("_env_field_id") or step.get("id") or ""
    pf_meta = step.get("_env_field_meta") or {}
    value_name = str(step.get("value_name") or pf_meta.get("value_name") or "").strip()
    value_code = str(step.get("value_code") or pf_meta.get("value_code") or "").strip()
    resolve_by = str(step.get("resolve_by") or pf_meta.get("resolve_by") or "value_name").strip()
    query_value = value_code if resolve_by == "value_code" and value_code else value_name
    original_value_id = str(step.get("value_id") or "").strip()
    recorded_value_id = str(
        pf_meta.get("recorded_value_id")
        or step.get("recorded_value_id")
        or original_value_id
    ).strip()
    field_key = str(step.get("field_key") or pf_meta.get("field_key") or "").strip()
    form_id = str(step.get("form_id") or "").strip()
    app_id = str(step.get("app_id") or "").strip()

    if not query_value or not field_key or not form_id or not app_id:
        return
    if query_value.startswith("${"):
        return
    if query_value == original_value_id and _looks_like_internal_id(original_value_id):
        return

    env_resolution = ctx.setdefault("env_resolution", {})
    cached = env_resolution.get(pf_id)
    if cached and cached.get("status") == "resolved" and cached.get("resolved_value_id"):
        step["value_id"] = cached["resolved_value_id"]
        return

    resolver: FieldResolver = ctx.setdefault(
        "field_resolver",
        FieldResolver(replay, env_id=str(ctx.get("env_id") or "")),
    )
    result = resolver.resolve_basedata_result(
        form_id,
        app_id,
        field_key,
        query_value,
        original_value_id=recorded_value_id or original_value_id,
    )
    result_dict = result.to_dict() if isinstance(result, ResolveResult) else dict(result)
    if result.status == "resolved" and result.resolved_value_id:
        step["value_id"] = result.resolved_value_id
        result_dict["effective_value_id"] = result.resolved_value_id
    else:
        if recorded_value_id and recorded_value_id != original_value_id:
            step["value_id"] = recorded_value_id
        result_dict["effective_value_id"] = step.get("value_id", original_value_id)
    result_dict["step_id"] = pf_id
    result_dict["label"] = pf_meta.get("label", field_key)
    result_dict["env_sensitive"] = pf_meta.get("env_sensitive", "medium")
    result_dict["value_id"] = step.get("value_id", original_value_id)
    result_dict["value_name"] = value_name
    result_dict["value_code"] = value_code
    result_dict["resolve_by"] = resolve_by
    result_dict["query"] = query_value
    env_resolution[pf_id] = result_dict

    run_ev = ctx.get("run_event")
    if run_ev:
        run_ev("env_fields_resolved", {"fields": [{
            "step_id": pf_id,
            "field_key": field_key,
            "value_id": str(step.get("value_id", original_value_id)),
            "value_name": value_name,
            "value_code": value_code,
            "label": pf_meta.get("label", field_key),
            "env_sensitive": pf_meta.get("env_sensitive", "medium"),
            "status": "pending",
            "resolve_status": result_dict.get("status"),
            "resolve_by": resolve_by,
            "query": query_value,
            "resolved_value_id": result_dict.get("resolved_value_id", ""),
            "confidence": result_dict.get("confidence", "low"),
            "message": result_dict.get("message", ""),
            "candidates": result_dict.get("candidates", []),
        }]})


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


_WRITE_ACS = {
    "save", "submit", "saveandeffect", "submitandeffect", "saveandaudit",
    "doconfirm", "afterconfirm", "startupflow",
}
_WRITE_KEYS = {
    "btnsave", "btn_save", "bar_save", "barsave",
    "btn_confirm", "btnconfirm", "bar_confirm", "barconfirm",
    "btnok", "btn_ok", "bar_submit", "barsubmit",
    "barstart", "bar_start",
    "new_save",
}


def _case_has_write_step(case: dict) -> bool:
    for step in case.get("steps") or []:
        ac = str(step.get("ac") or "").lower()
        key = str(step.get("key") or "").lower()
        args = step.get("args") or []
        arg_text = json.dumps(args, ensure_ascii=False).lower() if args else ""
        if ac in _WRITE_ACS or key in _WRITE_KEYS or any(k in arg_text for k in _WRITE_KEYS):
            return True
    return False


def _clone_session(sess: CosmicSession) -> CosmicSession:
    return CosmicSession(
        base_url=sess.base_url,
        cookie=sess.cookie,
        user_id=sess.user_id,
        account_id=sess.account_id,
        csrf_token=sess.csrf_token,
        diff_time=sess.diff_time,
        root_base_id="",
        root_page_id="",
    )


def _find_first_menu_step(case: dict, vars_ns: dict[str, Any]) -> dict | None:
    for raw_step in case.get("steps") or []:
        step = resolve_vars(raw_step, vars_ns)
        if step.get("type") == "invoke" and step.get("ac") == "menuItemClick":
            return step
    return None


def _preflight_write_case(
    case: dict,
    sess: CosmicSession,
    sign_required: bool,
    vars_ns: dict[str, Any],
    emit,
) -> list[str]:
    """写库前的跨环境导航探针。只做登录后读/导航，不触发新增或保存。"""
    if case.get("preflight") is False or not _case_has_write_step(case):
        return []

    menu_step = _find_first_menu_step(case, vars_ns)
    if not menu_step:
        return []

    main_form = case.get("main_form_id") or menu_step.get("target_form") or ""
    emit("preflight_start", {
        "id": "preflight_navigation",
        "main_form_id": main_form,
        "checks": ["csrf", "root", "portal", "menuItemClick", "main_loadData"],
    })

    errors: list[str] = []
    warnings: list[str] = []
    if not sess.csrf_token:
        warnings.append("当前登录未获取到 kd-csrf-token，目标环境可能拒绝 batchInvokeAction。")

    probe = CosmicFormReplay(_clone_session(sess), sign_required=sign_required)
    try:
        probe.init_root()
        portal_form = menu_step.get("form_id", "bos_portal_myapp_new")
        portal_app = menu_step.get("app_id", "bos")
        portal_pid = probe.open_portal(portal_form, portal_app, lazy=False)
        action = {
            "key": menu_step.get("key", ""),
            "methodName": menu_step.get("method", ""),
            "args": menu_step.get("args", []),
            "postData": menu_step.get("post_data", [{}, []]),
        }
        menu_resp = probe.invoke(
            portal_form, portal_app, "menuItemClick", [action], page_id=portal_pid
        )
        menu_errors = has_error_action(menu_resp)
        if menu_errors:
            errors.extend([f"menuItemClick 预检失败: {e}" for e in menu_errors])
        elif main_form:
            args = menu_step.get("args") or []
            menu_id = str(args[0].get("menuId", "")) if args and isinstance(args[0], dict) else ""
            if menu_id and probe.s.root_base_id:
                probe.page_ids[main_form] = f"{menu_id}root{probe.s.root_base_id}"
            main_app = _guess_app_id(main_form, case)
            load_resp = probe.load_data(main_form, main_app)
            load_errors = has_error_action(load_resp)
            if load_errors:
                errors.extend([f"main loadData 预检失败: {e}" for e in load_errors])
    except Exception as e:
        errors.append(f"preflight 异常: {type(e).__name__}: {e}")
    finally:
        probe.close()

    payload = {"id": "preflight_navigation", "warnings": warnings, "errors": errors}
    emit("preflight_fail" if errors else "preflight_ok", payload)
    return errors


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

    # 构建 pick_fields 预览（状态为 pending）
    _pick_fields_raw = case.get("pick_fields") or {}
    _pick_fields_preview = []
    for pf_id, pf_meta in _pick_fields_raw.items():
        _pick_fields_preview.append({
            "step_id": pf_id,
            "field_key": pf_meta.get("field_key", ""),
            "value_id": str(pf_meta.get("value_id", "") or pf_meta.get("value_name", "")),
            "value_name": pf_meta.get("value_name", ""),
            "value_code": pf_meta.get("value_code", ""),
            "label": pf_meta.get("label", pf_id),
            "env_sensitive": pf_meta.get("env_sensitive", "medium"),
            "status": "pending",
            "auto_resolve": bool(pf_meta.get("auto_resolve")),
            "resolve_status": pf_meta.get("resolve_status", ""),
            "resolve_by": pf_meta.get("resolve_by", ""),
        })

    emit("case_start", {
        "name": case.get("name", "?"), 
        "description": case.get("description", ""),
        # 先发送vars定义（显示用户配置的模板）
        "vars_def": {k: v for k, v in (case.get("vars") or {}).items() if not k.startswith("_")},
        "vars_labels": case.get("vars_labels", {}),
        "pick_fields_preview": _pick_fields_preview,
    })

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
        "resolved_vars": _build_display_vars(
            vars_ns,
            case.get("vars_labels") or {},
            case.get("steps") or [],
        ),
    })

    # 应用 pick_fields 中用户修改的环境值
    _apply_pick_fields(case)

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
        "env_resolution": {},
        "run_event": emit,
        "env_id": case.get("_runtime_env_id") or case.get("env_id") or env.get("id") or "",
        "main_form_id": main_form,  # ⭐ 供 menuItemClick L2 pageId 自动绑定
        # ⭐ step_id → 中文业务描述，供断言/日志生成人话
        "step_descriptions": {
            s.get("id"): (s.get("description") or "")
            for s in (case.get("steps") or [])
            if s.get("id")
        },
    }

    preflight_errors = _preflight_write_case(
        case, sess, bool(case.get("sign_required", True)), vars_ns, emit
    )
    preflight_blocked = bool(preflight_errors)
    if preflight_blocked:
        detail = "跨环境写库前置检查"
        result.steps.append({
            "id": "preflight_navigation",
            "type": "preflight",
            "ok": False,
            "optional": False,
            "detail": detail,
            "error": "; ".join(preflight_errors[:5]),
            "_errors": preflight_errors,
        })
        emit("step_fail", {
            "id": "preflight_navigation",
            "errors": preflight_errors[:5],
            "duration_ms": 0,
            "response": {"preflight": "failed"},
        })

    steps_to_run = [] if preflight_blocked else (case.get("steps") or [])

    for raw_step in steps_to_run:
        step = resolve_vars(raw_step, vars_ns)

        # ---- date pick_fields 后置注入：防止 resolve_vars 用 ${today} 覆盖用户自定义日期 ----
        if step.get("type") == "update_fields":
            _pf = case.get("pick_fields") or {}
            for _pf_id, _pf_meta in _pf.items():
                if not _pf_id.startswith("date_"):
                    continue
                if not isinstance(_pf_meta, dict):
                    continue
                _field_key = _pf_id[5:]  # 去掉 "date_" 前缀
                _value = _pf_meta.get("value_id") or _pf_meta.get("value_name", "")
                if not _value:
                    continue
                _value = resolve_vars(_value, vars_ns)  # 解析${today}等变量引用
                _fields = step.get("fields") or {}
                if _field_key in _fields:
                    _fv = _fields[_field_key]
                    if isinstance(_fv, dict):
                        for _lang in list(_fv.keys()):
                            _fv[_lang] = _value
                    else:
                        step.setdefault("fields", {})[_field_key] = _value
        # ---- end date pick_fields 后置注入 ----

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

        # ⭐ 通用安全网：如果目标 form_id 没有有效 pageId，自动 open_form 补偿
        _target_form = step.get("form_id")
        _target_app = step.get("app_id")
        if _target_form and _target_app and stype not in ("open_form", "sleep"):
            _need_open = False

            # pageId 完全缺失时才触发 auto-open
            if _target_form not in replay.page_ids:
                _need_open = True
                log.debug(f"[auto-open] {_target_form}: pageId 缺失")

            if _need_open:
                try:
                    _auto_pid = replay.open_form(_target_form, _target_app, lazy=False)
                    log.info(f"[auto-open] {_target_form} → pageId={_auto_pid[:20]}...")
                except Exception as _e:
                    log.warning(f"[auto-open] failed for {_target_form}: {_e}")

        def _runtime_pageid_trace(phase: str, status: str = "") -> dict[str, Any]:
            current_pid = replay.page_ids.get(_target_form or "", "")
            pending_pid = replay._pending_by_app.get(_target_app or "", "")
            return build_runtime_pageid_trace(
                step,
                current_page_id=current_pid,
                pending_page_id=pending_pid,
                phase=phase,
                status=status,
            )

        if stype not in ("sleep",):
            emit("pageid_trace", _runtime_pageid_trace("before_handler"))

        try:
            # ========= 安全网：invoke 通用重试机制 =========
            _INVOKE_MAX_RETRIES = 2  # 最大重试次数
            _RETRYABLE_ERRORS = (
                "页面未初始化或者已经过期",
                "获取缓存连接客户端失败",
                "请求超时",
                "NullPointerException",
            )
            _RETRYABLE_PROTOCOL_ERRORS = (
                "HTTP 502",
                "HTTP 503",
                "HTTP 504",
                "Bad Gateway",
                "Gateway Timeout",
                "Read timed out",
                "Connection aborted",
                "请求超时",
            )

            _retry_count = 0
            while True:
                try:
                    resp = handler(step, replay, ctx)
                except ProtocolError as _pe:
                    _err_text = str(_pe)
                    _should_retry = any(pat in _err_text for pat in _RETRYABLE_PROTOCOL_ERRORS)
                    if not _should_retry or _retry_count >= _INVOKE_MAX_RETRIES:
                        raise

                    _retry_count += 1
                    _wait = min(2 ** _retry_count, 4)
                    log.warning(
                        f"[invoke-retry] {step.get('id','?')}: 检测到协议瞬态错误, "
                        f"第{_retry_count}次重试 (等待{_wait}s)"
                    )
                    log.warning(f"[invoke-retry] 原始协议错误: {_err_text[:120]}")

                    _run_ev = ctx.get("run_event")
                    if _run_ev:
                        _run_ev("retry", {
                            "step_id": step.get("id", ""),
                            "attempt": _retry_count,
                            "error": _err_text[:150],
                        })

                    time.sleep(_wait)
                    if _target_form and _target_app:
                        try:
                            replay.page_ids.pop(_target_form, None)
                            # open_form 自身的瞬态错误交给下一轮 handler 重试，避免重复打开。
                            if stype != "open_form":
                                _fresh_pid = replay.open_form(_target_form, _target_app, lazy=False)
                                if _fresh_pid:
                                    replay.load_data(_target_form, _target_app)
                                    log.info(
                                        f"[invoke-retry] 协议错误恢复成功: "
                                        f"{_target_form} → {_fresh_pid[:20]}..."
                                    )
                        except Exception as _re:
                            log.warning(f"[invoke-retry] 协议错误恢复失败，将直接重试原步骤: {_re}")
                    continue
                errs = has_error_action(resp) if resp else []
                expected_errs, unexpected_errs = _split_expected_errors(step, errs)
                if expected_errs and not unexpected_errs:
                    ctx.setdefault("expected_notifications", {})[sid] = expected_errs
                    errs = []
                elif expected_errs:
                    ctx.setdefault("expected_notifications", {})[sid] = expected_errs
                    errs = unexpected_errs

                # 无错误或已达最大重试次数 → 跳出
                if not errs or _retry_count >= _INVOKE_MAX_RETRIES:
                    break

                # 检测是否为可重试错误
                _should_retry = any(
                    any(pat in e for pat in _RETRYABLE_ERRORS)
                    for e in errs
                )

                # 业务逻辑错误不重试（如数据校验失败等）
                if not _should_retry:
                    break

                # 需要 form_id 和 app_id 才能恢复
                if not _target_form or not _target_app:
                    break

                _retry_count += 1
                _wait = min(2 ** _retry_count, 4)  # 指数退避：2s, 4s
                log.warning(f"[invoke-retry] {step.get('id','?')}: 检测到可重试错误, 第{_retry_count}次重试 (等待{_wait}s)")
                log.warning(f"[invoke-retry] 原始错误: {errs[0][:100]}")

                # SSE 推送重试信息（如果有 run_event 回调）
                _run_ev = ctx.get("run_event")
                if _run_ev:
                    _run_ev("retry", {
                        "step_id": step.get("id", ""),
                        "attempt": _retry_count,
                        "error": errs[0][:150] if errs else "",
                    })

                time.sleep(_wait)

                # 恢复流程：pop → open_form → loadData
                try:
                    replay.page_ids.pop(_target_form, None)
                    _fresh_pid = replay.open_form(_target_form, _target_app, lazy=False)
                    if _fresh_pid:
                        replay.load_data(_target_form, _target_app)
                        log.info(f"[invoke-retry] 恢复成功: {_target_form} → {_fresh_pid[:20]}...")
                    else:
                        log.error(f"[invoke-retry] open_form 返回空, 中止重试")
                        break
                except Exception as _re:
                    log.error(f"[invoke-retry] 恢复失败: {_re}, 中止重试")
                    break  # open_form 失败直接中止，避免死循环
            # ========= 安全网结束 =========

            ctx["last_response"] = resp
            ctx["last_step_response"] = resp
            ctx["step_responses"][sid] = resp
            if resp is not None:
                ctx["response_history"].append(resp)

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
                    "pageid_trace": _runtime_pageid_trace("after_handler", "fail"),
                })
                if stype in ("invoke", "update_fields", "pick_basedata", "click_toolbar"):
                    break
            else:
                step_record = {
                    "id": sid, "type": stype, "ok": True, "optional": optional,
                    "detail": detail,
                }
                if expected_errs:
                    step_record["expected_notifications"] = expected_errs
                if errs and optional:
                    step_record["warning"] = "; ".join(errs[:3])
                    emit("step_warning", {
                        "id": sid,
                        "warnings": errs[:5],
                        "duration_ms": int((time.time() - step_start) * 1000),
                        "pageid_trace": _runtime_pageid_trace("after_handler", "warning"),
                    })
                result.steps.append(step_record)
                # ⭐ 推送完整响应数据供前端展示
                resp_snapshot = _truncate_response(resp)
                emit("step_ok", {
                    "id": sid,
                    "duration_ms": int((time.time() - step_start) * 1000),
                    "response": resp_snapshot,
                    "pageid_trace": _runtime_pageid_trace("after_handler", "ok"),
                })
                if step.get("capture"):
                    vars_ns[step["capture"]] = resp
        except BusinessError as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"业务错误: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"业务错误: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000),
                               "pageid_trace": _runtime_pageid_trace("after_handler", "fail")})
            if not optional: break
        except ProtocolError as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"协议错误: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"协议错误: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000),
                               "pageid_trace": _runtime_pageid_trace("after_handler", "fail")})
            if not optional: break
        except Exception as e:
            result.steps.append({
                "id": sid, "type": stype, "ok": False, "optional": optional,
                "detail": detail, "error": f"{type(e).__name__}: {e}",
            })
            emit("step_fail", {"id": sid, "error": f"{type(e).__name__}: {e}",
                               "duration_ms": int((time.time() - step_start) * 1000),
                               "pageid_trace": _runtime_pageid_trace("after_handler", "fail")})
            if not optional: break

    # 6. 断言（先把 ${vars.xxx} 解析掉）
    for a_raw in ([] if preflight_blocked else (case.get("assertions") or [])):
        a = resolve_vars(a_raw, vars_ns)
        atype = a.get("type")
        # ⭐ 预查断言挂靠的 step 描述，供日志展示
        _asrt_step = a.get("step") or ""
        _asrt_step_label = (ctx.get("step_descriptions") or {}).get(_asrt_step, "")
        handler = ASSERTION_HANDLERS.get(atype)
        if not handler:
            result.assertions.append({
                "type": atype, "ok": False, "msg": f"未知断言: {atype}",
            })
            emit("assertion_fail", {
                "type": atype, "msg": f"未知断言: {atype}",
                "step": _asrt_step, "step_label": _asrt_step_label,
            })
            continue
        try:
            ok, msg = handler(a, ctx)
            result.assertions.append({"type": atype, "ok": ok, "msg": msg})
            emit("assertion_ok" if ok else "assertion_fail",
                 {"type": atype, "msg": msg,
                  "step": _asrt_step, "step_label": _asrt_step_label})
        except Exception as e:
            result.assertions.append({
                "type": atype, "ok": False, "msg": f"断言执行异常: {e}",
            })
            emit("assertion_fail", {
                "type": atype, "msg": f"异常: {e}",
                "step": _asrt_step, "step_label": _asrt_step_label,
            })

    # 7. 失败时生成修复建议
    if not result.passed:
        failure_analysis = None
        try:
            failure_analysis = classify_run_failure(result.steps, result.assertions, case)
            emit("failure_analysis", failure_analysis)
        except Exception as e:
            log.warning(f"failure_analysis 执行异常，跳过归因: {e}")

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
        fix_payload: list[dict[str, Any]] = []
        if dedup:
            try:
                result.fixes = analyze_errors(dedup, ctx.get("response_history", []))
                fix_payload = [
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
                ]
            except Exception as e:
                log.warning(f"advisor 执行异常，跳过建议: {e}")
        try:
            repair_plan = build_repair_plan(case, failure_analysis, fix_payload)
            if fix_payload or repair_plan:
                emit("fixes_ready", {"fixes": fix_payload, "repair_plan": repair_plan})
        except Exception as e:
            log.warning(f"repair_planner 执行异常，跳过自动修复计划: {e}")

    result.end_ts = time.time()
    env_fields = _build_env_fields(case, result, ctx.get("env_resolution", {}))
    if env_fields:
        emit("env_fields_resolved", {"fields": env_fields})
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


def _infer_labels_from_steps(steps: list[dict]) -> dict[str, str]:
    """从 steps 的 description 反推每个变量对应的业务中文名。

    规则：
      1. update_fields.fields 中 value 引用 ${vars.KEY} 的 → 把 step.description 里
         「...」的第一个匹配作为 KEY 的业务 label
      2. pick_basedata.value_id 引用 ${vars.KEY} 的 → 同理

    这让右侧"本次运行变量值"面板的 label 能跟左侧"执行步骤"的「...」对齐。
    """
    import re as _re
    labels: dict[str, str] = {}
    ref_re = _re.compile(r"\$\{vars\.([A-Za-z_][A-Za-z0-9_]*)\}")
    zh_re = _re.compile(r"[「【]([^」】]+)[」】]")

    def _extract_zh(text: str | None) -> str | None:
        if not text:
            return None
        m = zh_re.search(str(text))
        return m.group(1).strip() if m else None

    for s in steps or []:
        if not isinstance(s, dict):
            continue
        desc = s.get("description")
        biz_name = _extract_zh(desc)
        if not biz_name:
            continue

        # 递归展平，从任意嵌套结构里提取所有 ${vars.KEY} 引用
        def _collect_refs(obj, acc: set):
            if isinstance(obj, str):
                for k in ref_re.findall(obj):
                    acc.add(k)
            elif isinstance(obj, dict):
                for v in obj.values():
                    _collect_refs(v, acc)
            elif isinstance(obj, list):
                for v in obj:
                    _collect_refs(v, acc)

        t = s.get("type")
        if t == "update_fields":
            refs: set = set()
            _collect_refs(s.get("fields"), refs)
            # 单变量引用（去重后）才给业务名归属，避免多字段误标
            if len(refs) == 1:
                labels.setdefault(next(iter(refs)), biz_name)
        elif t == "pick_basedata":
            vid = s.get("value_id")
            if isinstance(vid, str):
                for k in ref_re.findall(vid):
                    labels.setdefault(k, biz_name)
    return labels


def _build_display_vars(
    vars_ns: dict,
    vars_labels: dict | None = None,
    steps: list[dict] | None = None,
) -> list[dict]:
    """提取所有用户声明的变量（排除内部/session变量），附带中文标签。

    返回格式: [{"key": "test_number", "label": "员工编号", "value": "kdtest_xxx"}, ...]

    label 优先级（高 → 低）：
      1. step.description 反查       —— 与左侧执行步骤「...」内业务名对齐（最准确）
      2. case.vars_labels[k]        —— YAML 里用户显式声明（覆盖兜底）
      3. _VAR_LABEL_MAP[k]          —— 内置常见字段字典
      4. 后缀启发式匹配
      5. k 自身
    """
    results: list[dict] = []
    vars_labels = vars_labels or {}
    # 1) 从 steps 反推的业务 label（如 "员工姓名"、"企业"、"行政组织"）
    step_labels = _infer_labels_from_steps(steps or [])
    for k, v in vars_ns.items():
        if k.startswith("_") or k.startswith("session."):
            continue
        # 1) step 反查（最权威，与左侧执行步骤对齐）
        label = step_labels.get(k)
        # 2) YAML vars_labels（用户自定义覆盖；仅在 step 反查未命中时使用）
        if not label and isinstance(vars_labels, dict):
            label = vars_labels.get(k)
        # 3) 内置字典
        if not label:
            label = _VAR_LABEL_MAP.get(k)
        if not label:
            # 4) 尝试后缀匹配
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


def _build_env_fields(case: dict, result, env_resolution: dict | None = None) -> list[dict]:
    """收集所有 pick_basedata 步骤的执行结果，构造 env_fields 列表。"""
    env_resolution = env_resolution or {}
    pick_fields = case.get("pick_fields") or {}
    raw_steps = case.get("steps") or []
    # 建立 step_id -> 原始 step 的映射
    raw_step_map = {s["id"]: s for s in raw_steps if s.get("id")}

    env_fields: list[dict] = []
    for result_step in result.steps:
        if result_step.get("type") != "pick_basedata":
            continue
        step_id = result_step.get("id")
        raw = raw_step_map.get(step_id, {})
        env_step_id = raw.get("_env_field_id") or step_id
        meta = pick_fields.get(env_step_id) or pick_fields.get(step_id) or {}
        resolved = env_resolution.get(env_step_id) or env_resolution.get(step_id) or {}
        env_fields.append({
            "step_id": env_step_id,
            "field_key": raw.get("field_key", ""),
            "value_id": str(raw.get("value_id", "")),
            "value_name": raw.get("value_name", ""),
            "value_code": raw.get("value_code", "") or meta.get("value_code", ""),
            "label": meta.get("label", raw.get("description", raw.get("field_key", ""))),
            "env_sensitive": meta.get("env_sensitive", "medium"),
            "status": "ok" if result_step.get("ok") else "fail",
            "auto_resolve": bool(meta.get("auto_resolve")),
            "resolve_status": resolved.get("status") or meta.get("resolve_status", ""),
            "resolve_by": resolved.get("resolve_by") or meta.get("resolve_by", ""),
            "resolved_value_id": resolved.get("resolved_value_id", ""),
            "confidence": resolved.get("confidence", ""),
            "message": resolved.get("message", ""),
            "candidates": resolved.get("candidates", []),
        })
    return env_fields


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
        if step.get("value_code"):
            req["value_code"] = step.get("value_code", "")
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
