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
              select_f7_list_row | click_toolbar | sleep
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
import copy
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


def _pick_field_query_value(pf_meta: dict) -> str:
    resolve_by = str(pf_meta.get("resolve_by") or "").strip()
    value_code = str(pf_meta.get("value_code") or "").strip()
    if resolve_by == "value_code" and value_code:
        return value_code
    for key in ("value_code", "value_number", "value_id", "value_name"):
        value = str(pf_meta.get(key) or "").strip()
        if value:
            return value
    return ""


def _pick_field_resolver_plan(pf_id: str, pf_meta: dict) -> dict[str, Any]:
    """Describe how a frontend-maintained env field will be resolved at replay time."""
    field_key = str(pf_meta.get("field_key") or "").strip()
    form_id = str(pf_meta.get("form_id") or "").strip()
    app_id = str(pf_meta.get("app_id") or "").strip()
    query = _pick_field_query_value(pf_meta)
    base = {
        "step_id": pf_id,
        "field_key": field_key,
        "label": pf_meta.get("label", pf_id),
        "form_id": form_id,
        "app_id": app_id,
        "query": query,
        "resolve_by": pf_meta.get("resolve_by", ""),
        "auto_resolve": bool(pf_meta.get("auto_resolve")),
        "source_step_id": pf_meta.get("source_step_id", ""),
        "write_step_id": pf_meta.get("write_step_id", ""),
    }
    if pf_meta.get("selector_source") == "entryRowClick":
        base.update({
            "resolver_kind": "grid_selector",
            "interface": "loadData",
            "control_key": pf_meta.get("selector_control_key", "billlistap"),
            "result_shape": "row/selRows/selDatas",
            "candidate_source": "response_history.dataindex/rows",
        })
    elif pf_id.startswith("pick_") or pf_meta.get("auto_resolve"):
        base.update({
            "resolver_kind": "lookup",
            "interface": "getLookUpList",
            "control_key": field_key,
            "result_shape": "internal_id",
            "candidate_source": "lookup.list/rows",
        })
    elif pf_id.startswith(("enum_", "bool_", "num_", "date_")):
        base.update({
            "resolver_kind": "literal",
            "interface": "update_fields",
            "control_key": field_key,
            "result_shape": "literal_value",
            "candidate_source": "front_end_value",
        })
    else:
        base.update({
            "resolver_kind": "manual",
            "interface": "",
            "control_key": field_key,
            "result_shape": "recorded_or_manual_value",
            "candidate_source": "",
        })
    return base


def _build_env_resolution_plan(pick_fields: dict) -> list[dict[str, Any]]:
    if not isinstance(pick_fields, dict):
        return []
    return [
        _pick_field_resolver_plan(str(pf_id), pf_meta)
        for pf_id, pf_meta in pick_fields.items()
        if isinstance(pf_meta, dict)
    ]


def _pick_field_display_value(meta: dict | None, raw: dict | None = None) -> str:
    """User-facing value for panels: business code first, internal id last."""
    meta = meta or {}
    raw = raw or {}
    for key in ("value_code", "value_number", "value_name", "value_id"):
        value = meta.get(key)
        if value not in (None, ""):
            return str(value)
    for key in ("value_code", "value_number", "value_name", "value_id"):
        value = raw.get(key)
        if value not in (None, ""):
            return str(value)
    return ""


def _pick_field_case_order(case: dict, pf_id: str, pf_meta: dict | None = None) -> int:
    """Sort environment fields by the original HAR/case step order."""
    steps = case.get("steps") or []
    step_order = {
        str(step.get("id") or ""): idx
        for idx, step in enumerate(steps)
        if isinstance(step, dict) and step.get("id")
    }
    pf_meta = pf_meta or {}
    for key in ("source_step_id", "write_step_id"):
        step_id = str(pf_meta.get(key) or "")
        if step_id in step_order:
            return step_order[step_id]
    if pf_id in step_order:
        return step_order[pf_id]
    return len(step_order) + 1


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


def _case_targets_form_via_menu(case: dict, form_id: str) -> bool:
    """Whether a form is reached through a recorded menu L2 context.

    Such forms should not be opened eagerly with getConfig before the recorded
    navigation runs. Some Kingdee detail forms, for example template-driven
    entry pages, only become valid after the list/menu flow creates the server
    model context.
    """
    if not form_id:
        return False
    for step in case.get("steps") or []:
        if step.get("ac") != "menuItemClick":
            continue
        if step.get("target_form") == form_id:
            return True
        if form_id in (step.get("target_forms") or []):
            return True
    return False


def _case_reaches_form_via_recorded_context(case: dict, form_id: str) -> bool:
    """Whether a form is first reached after recorded parent/list/dialog steps."""
    if not form_id:
        return False
    for idx, step in enumerate(case.get("steps") or []):
        if step.get("form_id") != form_id:
            continue
        if step.get("type") == "open_form":
            return False
        return idx > 0
    return False


def _claim_pending_pageid_for_form(replay, form_id: str, app_id: str) -> bool:
    """Bind an addVirtualTab/showForm pageId to the next form that needs it."""
    pending = getattr(replay, "_pending_by_app", {}).get(app_id)
    if not pending:
        return False
    replay.page_ids[form_id] = pending
    try:
        replay._pending_by_app.pop(app_id, None)
    except Exception:
        pass
    log.info(f"[pending-bind] {form_id}: claimed pending pageId={str(pending)[:20]}...")
    return True


def _bind_l2_targets_from_navigation_step(
    step: dict,
    replay,
    ctx: dict,
    menu_id: str,
    *,
    overwrite: bool = True,
) -> str:
    """Bind a recorded menu/tree L2 pageId to the business target forms."""
    menu_id = str(menu_id or "").strip()
    if not menu_id or not getattr(replay.s, "root_base_id", ""):
        return ""
    l2_pid = f"{menu_id}root{replay.s.root_base_id}"
    targets = list(step.get("target_forms") or [])
    main_target = step.get("target_form") or ctx.get("main_form_id")
    if main_target and main_target not in targets:
        targets.insert(0, main_target)
    for target in targets:
        old_pid = replay.page_ids.get(target, "(none)")
        if old_pid != "(none)" and not overwrite:
            log.info(
                f"[navigation-l2] keep existing pageId for {target}: "
                f"{str(old_pid)[:30]}..., skip synthetic L2 overwrite"
            )
            continue
        replay.page_ids[target] = l2_pid
        log.info(f"[navigation-l2] L2 pageId for {target}: {l2_pid} (was: {old_pid[:30]})")
    return l2_pid


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
        if _claim_pending_pageid_for_form(replay, form_id, app_id):
            return
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
    _auto_resolve_selector_row_step(step, replay, ctx)
    action = {
        "key": step.get("key", ""),
        "methodName": step.get("method", ""),
        "args": step.get("args", []),
        "postData": step.get("post_data", [{}, []]),
    }
    # invalidate_pages: 执行前清除指定表单的旧 pageId（如 saveandeffect 后表单上下文已变）
    for fid in step.get("invalidate_pages", []):
        replay.page_ids.pop(fid, None)

    if step.get("bind_l2_only"):
        args = step.get("args", [])
        menu_id = str(
            step.get("menu_id")
            or (args[1] if step.get("ac") == "treeMenuClick" and len(args) >= 2 else "")
            or (args[0] if args else "")
            or ""
        )
        # App-specific treeMenuClick often depends on a browser app-shell model
        # that API replay cannot reconstruct. Treat it as a navigation hint:
        # bind L2 only when the target form has no usable pageId yet; otherwise
        # keep the pre-opened form context and rely on recorded default fields.
        l2_pid = _bind_l2_targets_from_navigation_step(
            step,
            replay,
            ctx,
            menu_id,
            overwrite=False,
        )
        return [{
            "a": "syntheticL2Bind",
            "p": [{
                "targetCount": len(step.get("target_forms") or []),
                "pageIdType": "L2" if l2_pid else "missing",
                "overwrite": False,
            }],
        }]

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
            _bind_l2_targets_from_navigation_step(step, replay, ctx, menu_id)

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
    if step.get("prefetch_lookup"):
        lookup_args = step.get("prefetch_lookup_args")
        if not isinstance(lookup_args, list) or not lookup_args:
            lookup_args = [["%", "", "%", 0, 20, 0]]
        replay.invoke_action(
            step["form_id"], step["app_id"], "getLookUpList",
            [{
                "key": step["field_key"],
                "methodName": "getLookUpList",
                "args": lookup_args,
                "postData": [{}, []],
            }],
        )
    return replay.pick_basedata(
        step["form_id"], step["app_id"],
        step["field_key"], str(step["value_id"]),
    )


def _extract_grid_payload(resp: Any, grid_key: str = "billlistap") -> tuple[dict[str, int], list[list[Any]]]:
    """Return dataindex + rows from a Kingdee grid response."""
    def walk(obj: Any) -> tuple[dict[str, int], list[list[Any]]] | None:
        if isinstance(obj, dict):
            if obj.get("k") == grid_key and isinstance(obj.get("data"), dict):
                data = obj["data"]
                dataindex = data.get("dataindex")
                rows = data.get("rows")
                if isinstance(dataindex, dict) and isinstance(rows, list):
                    return dataindex, rows
            for value in obj.values():
                found = walk(value)
                if found:
                    return found
        elif isinstance(obj, list):
            for item in obj:
                found = walk(item)
                if found:
                    return found
        return None

    found = walk(resp)
    if not found:
        return {}, []
    dataindex, rows = found
    clean_rows = [row for row in rows if isinstance(row, list)]
    return {str(k): int(v) for k, v in dataindex.items() if isinstance(v, int)}, clean_rows


def _grid_cell(row: list[Any], dataindex: dict[str, int], field: str) -> Any:
    idx = dataindex.get(field)
    if idx is None or idx < 0 or idx >= len(row):
        return None
    return row[idx]


def _find_grid_row(
    rows: list[list[Any]],
    dataindex: dict[str, int],
    *,
    value_code: str = "",
    value_name: str = "",
    match_fields: list[str] | None = None,
) -> tuple[int, list[Any]]:
    needles = [str(v).strip() for v in (value_code, value_name) if str(v or "").strip()]
    if not needles:
        raise ProtocolError("select_f7_list_row requires value_code or value_name")
    fields = match_fields or ["number", "name"]
    for pos, row in enumerate(rows):
        candidates = [str(_grid_cell(row, dataindex, field) or "").strip() for field in fields]
        if any(needle == candidate for needle in needles for candidate in candidates):
            row_idx = _grid_cell(row, dataindex, "rk")
            if isinstance(row_idx, int):
                return row_idx, row
            return pos, row
    raise ProtocolError(f"select_f7_list_row found no row for {needles!r} in fields {fields!r}")


@step_handler("select_f7_list_row")
def _h_select_f7_list_row(step: dict, replay: CosmicFormReplay, ctx: dict) -> Any:
    """Select one row in an F7/list dialog and optionally click OK.

    This models Kingdee chains such as:
    parent click(labelap4) -> showForm(F7) -> F7 loadData -> entryRowClick -> btnok
    -> parent entry grid/groupcontent update. It keeps the child pageId chain
    intact instead of hard-patching the final save request body.
    """
    form_id = step["form_id"]
    app_id = step["app_id"]
    grid_key = str(step.get("grid_key") or "billlistap")
    field_key = str(step.get("field_key") or "name")
    load_ac = str(step.get("load_ac") or "loadData")
    load_key = str(step.get("load_key") or "")
    load_method = str(step.get("load_method") or "loadData")
    load_args = step.get("load_args") or []
    load_post_data = step.get("load_post_data", [])

    load_resp = replay.invoke(form_id, app_id, load_ac, [{
        "key": load_key,
        "methodName": load_method,
        "args": load_args,
        "postData": load_post_data,
    }])
    dataindex, rows = _extract_grid_payload(load_resp, grid_key)
    row_index, row = _find_grid_row(
        rows,
        dataindex,
        value_code=str(step.get("value_code") or ""),
        value_name=str(step.get("value_name") or step.get("value_id") or ""),
        match_fields=[str(x) for x in (step.get("match_fields") or ["number", "name"])],
    )
    env_field_id = str(step.get("_env_field_id") or step.get("id") or "")
    ctx.setdefault("env_resolution", {})[env_field_id] = {
        "status": "resolved",
        "step_id": env_field_id,
        "field_key": field_key,
        "query": str(step.get("value_code") or step.get("value_name") or step.get("value_id") or ""),
        "value_code": str(step.get("value_code") or ""),
        "value_name": str(step.get("value_name") or ""),
        "value_id": str(_selector_pk_from_grid_row(row, dataindex, form_id)),
        "resolved_value_id": str(_selector_pk_from_grid_row(row, dataindex, form_id)),
        "resolved_value_name": str(_grid_cell(row, dataindex, "name") or _grid_cell(row, dataindex, field_key) or ""),
        "effective_value_id": str(_selector_pk_from_grid_row(row, dataindex, form_id)),
        "resolve_by": "value_code" if step.get("value_code") else "value_name",
        "resolver_kind": "grid_selector",
        "interface": load_ac,
        "control_key": grid_key,
        "row": row_index,
        "confidence": "high",
        "message": "select_f7_list_row 从 loadData 候选行解析",
    }

    select_resp = replay.invoke(form_id, app_id, str(step.get("select_ac") or "entryRowClick"), [{
        "key": grid_key,
        "methodName": str(step.get("select_method") or "entryRowClick"),
        "args": step.get("select_args") or [row_index, field_key],
        "postData": [{
            grid_key: {
                "fieldKey": field_key,
                "row": row_index,
                "selRows": [row_index],
                "selDatas": [row],
                "isClientNewRow": False,
                "clientNewRows": "",
            }
        }, []],
    }])

    if step.get("confirm", True):
        return replay.invoke(form_id, app_id, str(step.get("confirm_ac") or "click"), [{
            "key": str(step.get("confirm_key") or "btnok"),
            "methodName": str(step.get("confirm_method") or "click"),
            "args": step.get("confirm_args") or [],
            "postData": step.get("confirm_post_data", [{}, []]),
        }])
    return select_resp


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


@assertion_handler("readback_by_business_key")
def _a_readback_by_business_key(assert_spec: dict, ctx: dict) -> tuple[bool, str]:
    """Read-only post-save verification by a stable business key.

    This assertion is intentionally conservative: it either inspects a recorded
    readback step response, or sends one commonSearch request. It never clicks
    save/submit and never patches the original save payload.
    """
    form_id = str(assert_spec.get("form_id") or ctx.get("main_form_id") or "")
    app_id = str(assert_spec.get("app_id") or _guess_app_id(form_id, ctx.get("case") or {}) or "")
    field_key = str(assert_spec.get("field_key") or "").strip()
    value = str(assert_spec.get("value") or assert_spec.get("value_ref") or "").strip()
    if not (form_id and app_id and field_key and value):
        return False, "readback_by_business_key 缺少 form_id/app_id/field_key/value"

    source_step = str(assert_spec.get("step") or assert_spec.get("search_step") or "").strip()
    if source_step:
        resp = ctx["step_responses"].get(source_step)
        if resp is None:
            return False, f"找不到回查步骤 '{source_step}' 的响应"
        source_desc = f"步骤 {source_step}"
    elif str(assert_spec.get("strategy") or "").strip() in {
        "fresh_menu_refresh",
        "fresh_session_menu_refresh",
        "menu_refresh",
    }:
        resp = _run_fresh_menu_refresh_readback_query(assert_spec, ctx, form_id, app_id)
        source_desc = "新会话菜单刷新"
    else:
        replay = ctx["replay"]
        if form_id not in replay.page_ids and assert_spec.get("open_if_missing", True):
            replay.open_form(form_id, app_id, lazy=False)
            replay.load_data(form_id, app_id)
        resp = _run_business_key_readback_query(assert_spec, replay, form_id, app_id, field_key, value)
        source_desc = "只读 commonSearch"

    matched, detail = _response_contains_business_key(
        resp,
        field_key=field_key,
        expected=value,
        grid_key=str(assert_spec.get("grid_key") or "billlistap"),
    )
    ctx.setdefault("readback_results", []).append({
        "form_id": form_id,
        "app_id": app_id,
        "field_key": field_key,
        "value": value,
        "matched": matched,
        "source": source_desc,
        "detail": detail,
    })
    if matched:
        return True, f"✅ 入库回查通过：{form_id}.{field_key} = {value}（{source_desc}，{detail}）"
    return False, f"入库回查未找到：{form_id}.{field_key} = {value}（{source_desc}，{detail}）"


def _find_menu_navigation_for_form(case: dict, form_id: str) -> dict:
    for step in case.get("steps") or []:
        if step.get("ac") != "menuItemClick":
            continue
        targets = set(step.get("target_forms") or [])
        if step.get("target_form"):
            targets.add(step.get("target_form"))
        if form_id in targets:
            args = step.get("args") or []
            arg0 = args[0] if args and isinstance(args[0], dict) else {}
            return {
                "menu_id": str(arg0.get("menuId") or ""),
                "menu_app_id": str(arg0.get("appId") or step.get("app_id") or ""),
                "cloud_id": str(arg0.get("cloudId") or "undefined"),
                "portal_form": str(step.get("form_id") or "bos_portal_myapp_new"),
                "portal_app": str(step.get("app_id") or "bos"),
            }
    return {}


def _run_fresh_menu_refresh_readback_query(
    assert_spec: dict,
    ctx: dict,
    form_id: str,
    app_id: str,
) -> Any:
    case = ctx.get("case") or {}
    env = ctx.get("env") or case.get("env") or {}
    base_url = env.get("base_url")
    username = env.get("username")
    password = env.get("password")
    datacenter_id = env.get("datacenter_id")
    if not (base_url and username and password):
        raise ValueError("fresh_menu_refresh 回查缺少 base_url/username/password")

    nav = _find_menu_navigation_for_form(case, form_id)
    menu_id = str(assert_spec.get("menu_id") or nav.get("menu_id") or "").strip()
    if not menu_id:
        raise ValueError(f"fresh_menu_refresh 回查找不到 {form_id} 的 menu_id")
    menu_app_id = str(assert_spec.get("menu_app_id") or nav.get("menu_app_id") or app_id)
    cloud_id = str(assert_spec.get("cloud_id") or nav.get("cloud_id") or "undefined")
    portal_form = str(assert_spec.get("portal_form") or nav.get("portal_form") or "bos_portal_myapp_new")
    portal_app = str(assert_spec.get("portal_app") or nav.get("portal_app") or "bos")
    refresh_key = str(assert_spec.get("refresh_key") or "toolbarap")
    refresh_args = assert_spec.get("refresh_args")
    if not isinstance(refresh_args, list):
        refresh_args = ["tblrefresh", "refresh"]

    fresh_sess = login(
        str(base_url),
        str(username),
        str(password),
        datacenter_id=str(datacenter_id) if datacenter_id else None,
    )
    fresh = CosmicFormReplay(fresh_sess, sign_required=bool(case.get("sign_required", True)))
    try:
        fresh.init_root()
        portal_pid = fresh.open_portal(portal_form, portal_app, lazy=True)
        fresh.invoke(portal_form, portal_app, "menuItemClick", [{
            "key": "appnavigationmenuap",
            "methodName": "menuItemClick",
            "args": [{
                "menuId": menu_id,
                "appId": menu_app_id,
                "cloudId": cloud_id,
            }],
            "postData": [{}, []],
        }], page_id=portal_pid)
        fresh.page_ids[form_id] = fresh.l2_page_id(menu_id)
        return fresh.invoke(form_id, app_id, "refresh", [{
            "key": refresh_key,
            "methodName": "itemClick",
            "args": refresh_args,
            "postData": [{}, []],
        }], page_id=fresh.l2_page_id(menu_id))
    finally:
        fresh.close()


def _run_business_key_readback_query(
    assert_spec: dict,
    replay: CosmicFormReplay,
    form_id: str,
    app_id: str,
    field_key: str,
    value: str,
) -> Any:
    key = str(assert_spec.get("key") or "filtercontainerap")
    search_form_id = str(assert_spec.get("search_form_id") or form_id)
    extra_filters = assert_spec.get("extra_filters")
    if not isinstance(extra_filters, list):
        extra_filters = []
    args = [
        [{"FieldName": [field_key], "Value": [value]}],
        extra_filters,
        search_form_id,
    ]
    post_data = assert_spec.get("post_data")
    if not isinstance(post_data, list):
        post_data = [{key: {"triggerSearch": True}}, []]
    return replay.invoke(form_id, app_id, "commonSearch", [{
        "key": key,
        "methodName": "commonSearch",
        "args": args,
        "postData": post_data,
    }])


def _response_contains_business_key(
    resp: Any,
    *,
    field_key: str,
    expected: str,
    grid_key: str = "billlistap",
) -> tuple[bool, str]:
    expected_s = str(expected or "").strip()
    if not expected_s:
        return False, "业务键为空"
    dataindex, rows = _extract_grid_payload(resp, grid_key)
    if dataindex and rows:
        if field_key in dataindex:
            for row in rows:
                if str(_grid_cell(row, dataindex, field_key) or "").strip() == expected_s:
                    return True, f"grid {grid_key} 命中字段 {field_key}"
            return False, f"grid {grid_key} 有 {len(rows)} 行，但字段 {field_key} 未命中"
        for row in rows:
            if any(str(cell or "").strip() == expected_s for cell in row):
                return True, f"grid {grid_key} 命中任意列"
        return False, f"grid {grid_key} 有 {len(rows)} 行，但未命中业务键"
    text = json.dumps(resp, ensure_ascii=False, default=str)
    if expected_s in text:
        return True, "响应文本包含业务键"
    return False, "响应未包含 grid 行或业务键文本"


# =============================================================
# pick_fields 运行时值注入
# =============================================================
def _pick_field_targets_step(pf_meta: dict, step: dict) -> bool:
    """Return whether a pick/date override should touch this YAML step.

    New HARs carry source_step_id/form_id metadata so long multi-form chains can
    safely contain the same field_key in different forms. Older YAML files do
    not have this metadata, so absence still means "legacy broad match".
    """
    if not isinstance(pf_meta, dict):
        return True
    source_step_id = str(pf_meta.get("source_step_id") or "")
    if source_step_id and str(step.get("id") or "") != source_step_id:
        return False
    form_id = str(pf_meta.get("form_id") or "")
    step_form_id = str(step.get("form_id") or "")
    if form_id and step_form_id and form_id != step_form_id:
        return False
    return True


def _apply_pick_fields(case: dict):
    """运行前将 pick_fields 的用户修改值注入到对应步骤参数"""
    pick_fields = case.get("pick_fields") or {}
    if not pick_fields:
        return
    steps = case.get("steps") or []
    step_map = {s.get("id", ""): s for s in steps}

    def _model_context_value(pf_meta: dict, *, fallback_id: Any = "", fallback_name: Any = "") -> str:
        """Return the value that should be written into update_fields model context.

        For recorded defaults such as createorg/ctrlstrategy, the editable UI
        displays a business code when possible, while the step must receive the
        model value. Prefer explicit user-maintained code/number first, then the
        stored id, then name as a last resort.
        """
        if str(pf_meta.get("resolve_by") or "") == "value_code" and pf_meta.get("value_code"):
            return str(pf_meta.get("value_code") or "")
        for key in ("value_code", "value_number", "value_id", "value_name"):
            value = pf_meta.get(key)
            if value not in (None, ""):
                return str(value)
        if fallback_id not in (None, ""):
            return str(fallback_id)
        if fallback_name not in (None, ""):
            return str(fallback_name)
        return ""

    for pf_id, pf_meta in pick_fields.items():
        if not isinstance(pf_meta, dict):
            continue

        # date_* -> 替换 update_fields 步骤中的日期字段
        # 对 date_* 字段，优先用 value_id（用户编辑的值），fallback 到 value_name
        if pf_id.startswith("date_"):
            field_key = str(pf_meta.get("field_key") or pf_id[5:])  # 去掉 "date_" 前缀，如 date_bsed -> bsed
            value = pf_meta.get("value_id") or pf_meta.get("value_name", "")
            if not value:
                continue
            for step in steps:
                if step.get("type") == "update_fields":
                    if not _pick_field_targets_step(pf_meta, step):
                        continue
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

        # selector_* -> 覆盖 F7/列表弹窗 entryRowClick 的 selDatas 选中行
        if pf_id.startswith("selector_"):
            source_step_id = str(pf_meta.get("source_step_id") or "")
            step = step_map.get(source_step_id)
            if not step:
                continue
            step["_selector_env_field_id"] = pf_id
            step["_selector_env_field_meta"] = pf_meta
            _apply_selector_row_value(step, pf_meta)
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
            resolve_by = str(pf_meta.get("resolve_by") or "")
            inject_value_id = str(inject_vcode) if resolve_by == "value_code" and inject_vcode else str(inject_vid or "")
            if inject_vid or inject_vname or inject_vcode:
                applied = False
                for step in steps:
                    if not _pick_field_targets_step(pf_meta, step):
                        continue
                    if step.get("type") == "pick_basedata" and step.get("field_key") == field_key:
                        if inject_value_id:
                            step["value_id"] = inject_value_id
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
                        log.debug(f"[pick inject] {pf_id} → step[{step.get('id', '')}].value_id={inject_value_id or inject_vid}")
                        applied = True
                    elif step.get("type") == "select_f7_list_row":
                        target_field_key = str(step.get("target_field_key") or step.get("field_key") or "")
                        if target_field_key and target_field_key != field_key and not pf_meta.get("source_step_id"):
                            continue
                        if inject_vcode or inject_vid:
                            step["value_code"] = str(inject_vcode or inject_vid)
                        if inject_vname:
                            step["value_name"] = str(inject_vname)
                        elif inject_vid and not _looks_like_internal_id(str(inject_vid)):
                            step["value_name"] = str(inject_vid)
                        log.debug(f"[pick inject->select_f7] {pf_id} → step[{step.get('id', '')}]")
                        applied = True
                # MainOrgProp 等上下文字段可能以 update_fields 形式补偿写入；
                # 允许沿用 pick_* 配置面板去覆盖这些字段，保持 UI/配置方式一致。
                for step in steps:
                    if not _pick_field_targets_step(pf_meta, step):
                        continue
                    if step.get("type") != "update_fields":
                        continue
                    fields = step.get("fields") or {}
                    if field_key not in fields:
                        continue
                    if pf_meta.get("context_only") and not (
                        pf_meta.get("manual_override") or pf_meta.get("user_overridden")
                    ):
                        continue
                    new_value = _model_context_value(
                        pf_meta,
                        fallback_id=inject_vid,
                        fallback_name=inject_vname,
                    )
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


def _selector_rows(step: dict, pf_meta: dict) -> list | None:
    post_data = step.get("post_data")
    if not (isinstance(post_data, list) and post_data and isinstance(post_data[0], dict)):
        return None
    control_key = str(pf_meta.get("selector_control_key") or "")
    candidates = [post_data[0].get(control_key)] if control_key else list(post_data[0].values())
    for payload in candidates:
        if not isinstance(payload, dict):
            continue
        rows = payload.get("selDatas")
        if isinstance(rows, list) and rows and isinstance(rows[0], list):
            return rows
    return None


def _selector_query_value(pf_meta: dict) -> str:
    resolve_by = str(pf_meta.get("resolve_by") or "").strip()
    value_code = str(pf_meta.get("value_code") or "").strip()
    user_overridden = bool(pf_meta.get("user_overridden") or pf_meta.get("manual_override"))
    if resolve_by == "value_code" and value_code and user_overridden:
        return value_code
    value_id = str(pf_meta.get("value_id") or "").strip()
    if value_id:
        return value_id
    value_number = str(pf_meta.get("value_number") or "").strip()
    if value_number:
        return value_number
    return str(pf_meta.get("value_name") or "").strip()


def _resolve_selector_row_from_recent_grid(step: dict, ctx: dict) -> None:
    pf_meta = step.get("_selector_env_field_meta") or {}
    if not isinstance(pf_meta, dict) or not pf_meta.get("auto_resolve"):
        return
    query_value = _selector_query_value(pf_meta)
    if not query_value:
        return
    control_key = str(pf_meta.get("selector_control_key") or step.get("key") or "billlistap")
    field_key = str(pf_meta.get("field_key") or "")
    match_fields = [field_key, "number", "name", "employee_name", "employee_empnumber", "empnumber"]
    seen_fields: set[str] = set()
    match_fields = [f for f in match_fields if f and not (f in seen_fields or seen_fields.add(f))]

    for resp in reversed(ctx.get("response_history") or []):
        dataindex, rows = _extract_grid_payload(resp, control_key)
        if not dataindex or not rows:
            continue
        try:
            row_index, row = _find_grid_row(
                rows,
                dataindex,
                value_code=query_value,
                value_name=str(pf_meta.get("value_name") or ""),
                match_fields=match_fields,
            )
        except ProtocolError:
            continue

        post_data = step.get("post_data")
        if not (isinstance(post_data, list) and post_data and isinstance(post_data[0], dict)):
            return
        payload = post_data[0].get(control_key)
        if not isinstance(payload, dict):
            return
        existing_rows = payload.get("selDatas")
        template_row = (
            existing_rows[0]
            if isinstance(existing_rows, list) and existing_rows and isinstance(existing_rows[0], list)
            else []
        )
        selected_row = _build_selector_selected_row(
            template_row,
            row,
            dataindex,
            pf_meta,
            query_value,
            form_id=str(step.get("form_id") or ""),
        )
        payload["fieldKey"] = field_key or payload.get("fieldKey") or ""
        payload["row"] = row_index
        payload["selRows"] = [row_index]
        payload["selDatas"] = [selected_row]
        args = step.get("args")
        if isinstance(args, list) and args:
            args[0] = row_index
        step["_selector_grid_resolved"] = {
            "query": query_value,
            "row": row_index,
            "value_id": str((_grid_cell(row, dataindex, "hcdm_adjfileinfo_id") or row[0]) if row else ""),
            "value_code": query_value,
            "value_name": str(_grid_cell(row, dataindex, field_key) or ""),
            "resolver_kind": "grid_selector",
            "interface": "loadData",
            "control_key": control_key,
        }
        pf_id = str(step.get("_selector_env_field_id") or "")
        if pf_id:
            result_dict = {
                "status": "resolved",
                "step_id": pf_id,
                "field_key": field_key,
                "label": pf_meta.get("label", field_key),
                "query": query_value,
                "value_code": query_value,
                "value_name": step["_selector_grid_resolved"]["value_name"],
                "value_id": step["_selector_grid_resolved"]["value_id"],
                "resolved_value_id": step["_selector_grid_resolved"]["value_id"],
                "resolved_value_name": step["_selector_grid_resolved"]["value_name"],
                "effective_value_id": step["_selector_grid_resolved"]["value_id"],
                "resolve_by": pf_meta.get("resolve_by", "value_code"),
                "resolver_kind": "grid_selector",
                "interface": "loadData",
                "control_key": control_key,
                "row": row_index,
                "confidence": "high",
                "message": "从最近 loadData 列表候选行解析",
            }
            ctx.setdefault("env_resolution", {})[pf_id] = result_dict
            run_ev = ctx.get("run_event")
            if run_ev:
                run_ev("env_field_resolved", result_dict)
        return


def _build_selector_selected_row(
    template_row: list[Any],
    grid_row: list[Any],
    dataindex: dict[str, int],
    pf_meta: dict,
    query_value: str,
    *,
    form_id: str = "",
) -> list[Any]:
    """Build the row shape expected by entryRowClick selDatas.

    Kingdee list responses often contain full grid rows, while entryRowClick
    records a compact selection row. Keep the recorded compact shape and only
    replace the pk/code/status cells from the matched grid row.
    """
    if not template_row or len(template_row) >= len(grid_row):
        return copy.deepcopy(grid_row)

    compact = copy.deepcopy(template_row)
    value_idx = int(pf_meta.get("selector_value_index", 0) or 0)
    code_idx = int(pf_meta.get("selector_code_index", -1) or -1)
    pk_value = _selector_pk_from_grid_row(grid_row, dataindex, form_id)
    if pk_value not in (None, "") and 0 <= value_idx < len(compact):
        compact[value_idx] = pk_value

    old_code = str(template_row[code_idx] or "").strip() if 0 <= code_idx < len(template_row) else ""
    if query_value:
        for idx, cell in enumerate(list(compact)):
            cell_text = str(cell or "").strip()
            if idx == code_idx or (old_code and cell_text == old_code) or _looks_like_business_code(cell_text):
                compact[idx] = query_value

    org_id = _grid_cell(grid_row, dataindex, "org_id")
    if org_id not in (None, ""):
        for idx, cell in enumerate(list(compact)):
            if str(cell or "").strip() == "100000" or idx == 2 and len(compact) >= 5:
                compact[idx] = org_id
                break

    status = _grid_cell(grid_row, dataindex, "status")
    if status not in (None, ""):
        for idx in range(len(compact) - 1, -1, -1):
            if str(compact[idx] or "").strip() in {"A", "B", "C", "D"}:
                compact[idx] = status
                break

    return compact


def _looks_like_business_code(value: Any) -> bool:
    text = str(value or "").strip()
    return bool(re.match(r"^[A-Za-z0-9]+-[A-Za-z0-9]+$", text))


def _selector_pk_from_grid_row(grid_row: list[Any], dataindex: dict[str, int], form_id: str = "") -> Any:
    form_hint = form_id[:-2] if form_id.endswith("f7") else form_id
    preferred = []
    if form_hint:
        preferred.append(f"{form_hint}_id")
    preferred.extend(k for k in dataindex if k.endswith("_id") and k != "org_id")
    preferred.extend(k for k in dataindex if k.endswith("id") and k != "org_id")
    for key in preferred:
        value = _grid_cell(grid_row, dataindex, key)
        if value not in (None, ""):
            return value
    return grid_row[0] if grid_row else ""


def _apply_selector_row_value(step: dict, pf_meta: dict, resolved_value_id: str = "") -> None:
    rows = _selector_rows(step, pf_meta)
    if not rows:
        return
    row = rows[0]
    value_idx = int(pf_meta.get("selector_value_index", 0) or 0)
    code_idx = int(pf_meta.get("selector_code_index", -1) or -1)
    recorded = str(pf_meta.get("recorded_value_id") or "").strip()
    user_value = _selector_query_value(pf_meta)
    resolve_by = str(pf_meta.get("resolve_by") or "").strip()
    user_overrode_code = (
        resolve_by == "value_code"
        and user_value
        and not _looks_like_internal_id(user_value)
        and (pf_meta.get("user_overridden") or pf_meta.get("manual_override"))
    )
    value_id = str(resolved_value_id or (user_value if user_overrode_code else recorded) or user_value or "").strip()
    value_code = user_value if user_value and not _looks_like_internal_id(user_value) else str(pf_meta.get("value_code") or "").strip()
    value_name = str(pf_meta.get("value_name") or "").strip()
    if value_id and 0 <= value_idx < len(row):
        row[value_idx] = value_id
    if value_code and 0 <= code_idx < len(row):
        row[code_idx] = value_code
    if value_name:
        step["_selector_display_value"] = value_name


def _auto_resolve_selector_row_step(step: dict, replay: CosmicFormReplay, ctx: dict) -> None:
    pf_meta = step.get("_selector_env_field_meta") or {}
    if not isinstance(pf_meta, dict) or not pf_meta.get("auto_resolve"):
        return
    pf_id = step.get("_selector_env_field_id") or ""
    if step.get("_selector_grid_resolved"):
        resolved = dict(step["_selector_grid_resolved"])
        result_dict = {
            "status": "resolved",
            "step_id": pf_id,
            "field_key": pf_meta.get("field_key", ""),
            "label": pf_meta.get("label", pf_meta.get("field_key", "")),
            "query": resolved.get("query", ""),
            "value_code": resolved.get("value_code", ""),
            "value_name": resolved.get("value_name", ""),
            "value_id": resolved.get("value_id", ""),
            "resolved_value_id": resolved.get("value_id", ""),
            "resolved_value_name": resolved.get("value_name", ""),
            "effective_value_id": resolved.get("value_id", ""),
            "resolve_by": pf_meta.get("resolve_by", "value_code"),
            "resolver_kind": resolved.get("resolver_kind", "grid_selector"),
            "interface": resolved.get("interface", "loadData"),
            "control_key": resolved.get("control_key", ""),
            "row": resolved.get("row"),
            "confidence": "high",
            "message": "已由 F7/list loadData 候选行解析，跳过 getLookUpList 兜底",
        }
        ctx.setdefault("env_resolution", {})[pf_id] = result_dict
        return
    field_key = str(pf_meta.get("field_key") or "").strip()
    form_id = str(pf_meta.get("form_id") or step.get("form_id") or "").strip()
    app_id = str(pf_meta.get("app_id") or step.get("app_id") or "").strip()
    user_value = _selector_query_value(pf_meta)
    value_code = user_value if user_value and not _looks_like_internal_id(user_value) else str(pf_meta.get("value_code") or "").strip()
    value_name = str(pf_meta.get("value_name") or "").strip()
    resolve_by = str(pf_meta.get("resolve_by") or "value_code").strip()
    query_value = value_code if resolve_by == "value_code" and value_code else value_name
    if not (field_key and form_id and app_id and query_value):
        return

    env_resolution = ctx.setdefault("env_resolution", {})
    cached = env_resolution.get(pf_id)
    if cached and cached.get("status") == "resolved" and cached.get("resolved_value_id"):
        _apply_selector_row_value(step, pf_meta, str(cached["resolved_value_id"]))
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
        original_value_id=str(pf_meta.get("recorded_value_id") or pf_meta.get("value_id") or ""),
    )
    result_dict = result.to_dict() if isinstance(result, ResolveResult) else dict(result)
    if result.status == "resolved" and result.resolved_value_id:
        _apply_selector_row_value(step, pf_meta, result.resolved_value_id)
        result_dict["effective_value_id"] = result.resolved_value_id
    else:
        _apply_selector_row_value(step, pf_meta)
        rows = _selector_rows(step, pf_meta) or []
        value_idx = int(pf_meta.get("selector_value_index", 0) or 0)
        result_dict["effective_value_id"] = (
            rows[0][value_idx]
            if rows and 0 <= value_idx < len(rows[0])
            else pf_meta.get("recorded_value_id") or pf_meta.get("value_id") or ""
        )
    result_dict["step_id"] = pf_id
    result_dict["label"] = pf_meta.get("label", field_key)
    result_dict["env_sensitive"] = pf_meta.get("env_sensitive", "medium")
    result_dict["value_id"] = result_dict["effective_value_id"]
    result_dict["value_name"] = value_name
    result_dict["value_code"] = value_code
    result_dict["resolve_by"] = resolve_by
    result_dict["query"] = query_value
    result_dict["resolver_kind"] = "lookup"
    result_dict["interface"] = "getLookUpList"
    result_dict["control_key"] = field_key
    env_resolution[pf_id] = result_dict


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
        user_overrode_code = (
            resolve_by == "value_code"
            and value_code
            and (
                pf_meta.get("user_overridden")
                or pf_meta.get("manual_override")
                or value_code != str(pf_meta.get("recorded_value_code") or pf_meta.get("value_number") or "")
            )
        )
        if recorded_value_id and recorded_value_id != original_value_id and not user_overrode_code:
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
            "display_value": _pick_field_display_value(pf_meta, {
                "value_id": step.get("value_id", original_value_id),
                "value_name": value_name,
                "value_code": value_code,
            }),
            "label": pf_meta.get("label", field_key),
            "env_sensitive": pf_meta.get("env_sensitive", "medium"),
            "status": "pending",
            "resolve_status": result_dict.get("status"),
            "resolve_by": resolve_by,
            "query": query_value,
            "resolver_kind": "lookup",
            "resolver_interface": "getLookUpList",
            "resolver_control_key": field_key,
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
        assert_ok = all(a["ok"] or a.get("advisory") for a in self.assertions)
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
                mark = "[ok] " if a["ok"] else ("[adv]" if a.get("advisory") else "[ERR]")
                print(f"  {mark} {a['type']}", file=out)
                if not a["ok"]:
                    print(f"        {a['msg']}", file=out)
        # 失败时打修复建议
        if self.fixes:
            print(format_fixes(self.fixes), file=out)


def _assertion_is_advisory(assert_spec: dict) -> bool:
    """Return True when a failed assertion should warn but not fail the case."""
    mode = str(assert_spec.get("mode") or assert_spec.get("severity") or "").strip().lower()
    return bool(assert_spec.get("advisory")) or mode in {"advisory", "warn", "warning", "soft"}


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
    _env_resolution_plan = _build_env_resolution_plan(_pick_fields_raw)
    _env_resolution_plan_map = {item["step_id"]: item for item in _env_resolution_plan}
    _pick_fields_preview = []
    for pf_id, pf_meta in _pick_fields_raw.items():
        plan_item = _env_resolution_plan_map.get(str(pf_id), {})
        _pick_fields_preview.append({
            "step_id": pf_id,
            "field_key": pf_meta.get("field_key", ""),
            "value_id": str(pf_meta.get("value_id", "") or pf_meta.get("value_name", "")),
            "value_name": pf_meta.get("value_name", ""),
            "value_code": pf_meta.get("value_code", ""),
            "value_number": pf_meta.get("value_number", ""),
            "display_value": _pick_field_display_value(pf_meta),
            "label": pf_meta.get("label", pf_id),
            "form_id": pf_meta.get("form_id", ""),
            "form_label": pf_meta.get("form_label", ""),
            "group_key": pf_meta.get("group_key", ""),
            "group_label": pf_meta.get("group_label", ""),
            "source_step_id": pf_meta.get("source_step_id", ""),
            "write_step_id": pf_meta.get("write_step_id", ""),
            "env_sensitive": pf_meta.get("env_sensitive", "medium"),
            "status": "pending",
            "auto_resolve": bool(pf_meta.get("auto_resolve")),
            "resolve_status": pf_meta.get("resolve_status", ""),
            "resolve_by": pf_meta.get("resolve_by", ""),
            "resolver_kind": plan_item.get("resolver_kind", ""),
            "resolver_interface": plan_item.get("interface", ""),
            "resolver_query": plan_item.get("query", ""),
            "_order": _pick_field_case_order(case, str(pf_id), pf_meta),
        })
    _pick_fields_preview.sort(key=lambda item: (item.get("_order", 999999), str(item.get("step_id") or "")))

    emit("case_start", {
        "name": case.get("name", "?"), 
        "description": case.get("description", ""),
        # 先发送vars定义（显示用户配置的模板）
        "vars_def": {k: v for k, v in (case.get("vars") or {}).items() if not k.startswith("_")},
        "vars_labels": case.get("vars_labels", {}),
        "vars_meta": case.get("vars_meta", {}),
        "pick_fields_preview": _pick_fields_preview,
        "env_resolution_plan": _env_resolution_plan,
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
        if _case_targets_form_via_menu(case, main_form):
            log.info(f"[main-preopen] skip {main_form}: recorded menu flow will provide pageId context")
        elif _case_reaches_form_via_recorded_context(case, main_form):
            log.info(f"[main-preopen] skip {main_form}: recorded parent flow will provide pageId context")
        else:
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
        "env": env,
        "env_id": case.get("_runtime_env_id") or case.get("env_id") or env.get("id") or "",
        "main_form_id": main_form,  # ⭐ 供 menuItemClick L2 pageId 自动绑定
        "case": case,
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
                if not _pick_field_targets_step(_pf_meta, step):
                    continue
                _field_key = str(_pf_meta.get("field_key") or _pf_id[5:])  # 去掉 "date_" 前缀
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
        if stype == "invoke" and step.get("_selector_env_field_meta"):
            _resolve_selector_row_from_recent_grid(step, ctx)
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
        if _target_form and _target_app and stype not in ("open_form", "sleep") and not step.get("bind_l2_only"):
            _need_open = False

            # pageId 完全缺失时才触发 auto-open
            if _target_form not in replay.page_ids:
                if _claim_pending_pageid_for_form(replay, _target_form, _target_app):
                    _need_open = False
                else:
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
        advisory = _assertion_is_advisory(a)
        # ⭐ 预查断言挂靠的 step 描述，供日志展示
        _asrt_step = a.get("step") or ""
        _asrt_step_label = (ctx.get("step_descriptions") or {}).get(_asrt_step, "")
        handler = ASSERTION_HANDLERS.get(atype)
        if not handler:
            result.assertions.append({
                "type": atype, "ok": False, "advisory": advisory, "msg": f"未知断言: {atype}",
            })
            emit("assertion_advisory" if advisory else "assertion_fail", {
                "type": atype, "msg": f"未知断言: {atype}",
                "step": _asrt_step, "step_label": _asrt_step_label,
                "advisory": advisory,
            })
            continue
        try:
            ok, msg = handler(a, ctx)
            result.assertions.append({"type": atype, "ok": ok, "advisory": advisory, "msg": msg})
            emit("assertion_ok" if ok else ("assertion_advisory" if advisory else "assertion_fail"),
                 {"type": atype, "msg": msg,
                  "step": _asrt_step, "step_label": _asrt_step_label,
                  "advisory": advisory})
        except Exception as e:
            result.assertions.append({
                "type": atype, "ok": False, "advisory": advisory, "msg": f"断言执行异常: {e}",
            })
            emit("assertion_advisory" if advisory else "assertion_fail", {
                "type": atype, "msg": f"异常: {e}",
                "step": _asrt_step, "step_label": _asrt_step_label,
                "advisory": advisory,
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
        "assertion_fail": sum(1 for a in result.assertions if not a.get("ok") and not a.get("advisory")),
        "assertion_advisory": sum(1 for a in result.assertions if not a.get("ok") and a.get("advisory")),
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
        if result_step.get("type") not in {"pick_basedata", "select_f7_list_row"}:
            continue
        step_id = result_step.get("id")
        raw = raw_step_map.get(step_id, {})
        env_step_id = raw.get("_env_field_id") or step_id
        meta = pick_fields.get(env_step_id) or pick_fields.get(step_id) or {}
        resolved = env_resolution.get(env_step_id) or env_resolution.get(step_id) or {}
        item = {
            "step_id": env_step_id,
            "field_key": meta.get("field_key", raw.get("field_key", "")),
            "value_id": str(raw.get("value_id", "") or raw.get("value_code", "")),
            "value_name": raw.get("value_name", ""),
            "value_code": raw.get("value_code", "") or meta.get("value_code", ""),
            "value_number": raw.get("value_number", "") or meta.get("value_number", ""),
            "label": meta.get("label", raw.get("description", raw.get("field_key", ""))),
            "form_id": meta.get("form_id", raw.get("form_id", "")),
            "form_label": meta.get("form_label", ""),
            "group_key": meta.get("group_key", ""),
            "group_label": meta.get("group_label", ""),
            "source_step_id": meta.get("source_step_id", ""),
            "write_step_id": meta.get("write_step_id", ""),
            "env_sensitive": meta.get("env_sensitive", "medium"),
            "status": "ok" if result_step.get("ok") else "fail",
            "auto_resolve": bool(meta.get("auto_resolve")),
            "resolve_status": resolved.get("status") or meta.get("resolve_status", ""),
            "resolve_by": resolved.get("resolve_by") or meta.get("resolve_by", ""),
            "resolver_kind": resolved.get("resolver_kind", ""),
            "resolver_interface": resolved.get("interface", ""),
            "resolver_query": resolved.get("query", ""),
            "resolver_control_key": resolved.get("control_key", ""),
            "resolved_value_id": resolved.get("resolved_value_id", ""),
            "confidence": resolved.get("confidence", ""),
            "message": resolved.get("message", ""),
            "candidates": resolved.get("candidates", []),
            "_order": _pick_field_case_order(case, str(env_step_id), meta),
        }
        item["display_value"] = _pick_field_display_value(meta, item)
        env_fields.append(item)
    env_fields.sort(key=lambda item: (item.get("_order", 999999), str(item.get("step_id") or "")))
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
    elif t == "select_f7_list_row":
        req["grid_key"] = step.get("grid_key", "billlistap")
        req["field_key"] = step.get("field_key", "name")
        req["target_field_key"] = step.get("target_field_key", "")
        req["value_code"] = step.get("value_code", "")
        req["value_name"] = step.get("value_name", "")
        req["confirm_key"] = step.get("confirm_key", "btnok")
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
    if t == "select_f7_list_row":
        return f"{step.get('form_id')}  {step.get('grid_key', 'billlistap')}={step.get('value_code') or step.get('value_name')}"
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
        "select_f7_list_row": "选择F7列表行",
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
