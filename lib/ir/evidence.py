"""Value-safe IR summaries derived from generated YAML and runtime events."""
from __future__ import annotations

import json
import re
from typing import Any

from lib.pageid_trace import build_pageid_trace

from .dynamic_flow import build_dynamic_value_flow
from .sanitizer import scan_sensitive_text
from .yaml_bridge import classify_yaml_step_role


def build_case_ir_summary(
    case: dict[str, Any],
    *,
    run_events: list[dict[str, Any]] | None = None,
    max_steps: int = 120,
) -> dict[str, Any]:
    """Build a compact IR-like summary for evidence packages.

    Evidence endpoints usually receive the generated YAML and run events, not
    the raw HAR. This summary is therefore explicit about its source and never
    includes variable values, cookies, tokens or full pageIds.
    """
    if not isinstance(case, dict):
        case = {}
    pageid_trace = build_pageid_trace(case, run_events=run_events or [], include_fragments=False)
    dynamic_value_flow = build_dynamic_value_flow(case, run_events=run_events or [])
    trace_by_id = {
        str(row.get("step_id") or ""): row
        for row in pageid_trace.get("steps") or []
        if row.get("step_id")
    }
    steps: list[dict[str, Any]] = []
    for index, step in enumerate(case.get("steps") or []):
        if len(steps) >= max_steps:
            break
        sid = str(step.get("id") or f"step_{index + 1}")
        trace = trace_by_id.get(sid, {})
        steps.append({
            "index": index,
            "step_id": sid,
            "type": step.get("type", ""),
            "form_id": step.get("form_id", ""),
            "app_id": step.get("app_id", ""),
            "ac": step.get("ac", ""),
            "method": step.get("method", ""),
            "role": _step_role(step),
            "expected_pageid_role": trace.get("expected_pageid_role", ""),
            "har_pageid_type": trace.get("har_pageid_type", ""),
            "runtime_pageid_type": trace.get("runtime_pageid_type", ""),
            "preserve_l2_page": bool(step.get("preserve_l2_page")),
            "recorded_pageid_source_step_id": step.get("recorded_pageid_source_step_id", ""),
            "recorded_pageid_source_kind": step.get("recorded_pageid_source_kind", ""),
            "recorded_pageid_source_retained": step.get("recorded_pageid_source_retained"),
            "target_forms": list(step.get("target_forms") or [])[:12],
            "risk_codes": trace.get("risk_codes", []),
        })

    summary = {
        "schema_version": "0.1",
        "status": "ready",
        "source": "generated_yaml_runtime",
        "raw_har_included": False,
        "note": "由 YAML 和运行事件派生；如需 HAR 原始链路，请结合 pageid_trace 或本地 har_ir_tool。",
        "case_shape": _case_shape(case),
        "pageid_trace_summary": pageid_trace.get("summary", {}),
        "dynamic_value_flow": dynamic_value_flow,
        "response_anchor_candidates": _response_anchor_candidates(run_events or []),
        "steps": steps,
        "variables": _variables_shape(case),
        "environment_fields": _pick_fields_shape(case),
        "assertions": _assertions_shape(case),
        "warnings": _warnings(case, steps, pageid_trace, dynamic_value_flow),
    }
    risks = scan_sensitive_text(json.dumps(summary, ensure_ascii=False))
    if risks:
        return {
            "schema_version": "0.1",
            "status": "blocked_sensitive_scan",
            "source": "generated_yaml_runtime",
            "raw_har_included": False,
            "warnings": [{"code": "sensitive_summary_blocked", "risks": risks}],
        }
    return summary


def _case_shape(case: dict[str, Any]) -> dict[str, Any]:
    steps = case.get("steps") or []
    write_steps = [step for step in steps if _step_role(step) == "write"]
    return {
        "name": case.get("name", ""),
        "main_form_id": case.get("main_form_id", ""),
        "target_forms": list(case.get("target_forms") or [])[:30],
        "step_count": len(steps),
        "write_step_count": len(write_steps),
        "assertion_count": len(case.get("assertions") or []),
        "var_count": len(case.get("vars") or {}),
        "env_field_count": len(case.get("pick_fields") or {}),
    }


def _variables_shape(case: dict[str, Any]) -> list[dict[str, Any]]:
    vars_map = case.get("vars") or {}
    vars_meta = case.get("vars_meta") or {}
    result: list[dict[str, Any]] = []
    for name in sorted(vars_map):
        meta = vars_meta.get(name) if isinstance(vars_meta, dict) else {}
        result.append({
            "name": name,
            "field_key": (meta or {}).get("field_key", ""),
            "form_id": (meta or {}).get("form_id", ""),
            "action": (meta or {}).get("action", ""),
            "value_shape": _value_shape(vars_map.get(name)),
        })
    return result[:80]


def _pick_fields_shape(case: dict[str, Any]) -> list[dict[str, Any]]:
    fields = case.get("pick_fields") or {}
    result: list[dict[str, Any]] = []
    for name in sorted(fields):
        cfg = fields.get(name) or {}
        value = cfg.get("value") if isinstance(cfg, dict) else cfg
        result.append({
            "name": name,
            "field_key": cfg.get("field_key", "") if isinstance(cfg, dict) else "",
            "label": cfg.get("label", "") if isinstance(cfg, dict) else "",
            "form_id": cfg.get("form_id", "") if isinstance(cfg, dict) else "",
            "source": cfg.get("source", "") if isinstance(cfg, dict) else "",
            "value_shape": _value_shape(value),
            "user_overridden": bool(cfg.get("user_overridden")) if isinstance(cfg, dict) else False,
        })
    return result[:120]


def _assertions_shape(case: dict[str, Any]) -> list[dict[str, Any]]:
    result: list[dict[str, Any]] = []
    for item in case.get("assertions") or []:
        if not isinstance(item, dict):
            continue
        result.append({
            "type": item.get("type", ""),
            "step": item.get("step", item.get("step_id", "")),
            "mode": item.get("mode", ""),
            "reason": item.get("reason", ""),
        })
    return result[:120]


def _response_anchor_candidates(run_events: list[dict[str, Any]]) -> list[dict[str, Any]]:
    """Return value-safe response anchor suggestions from runtime events."""
    candidates: list[dict[str, Any]] = []
    seen: set[tuple[str, str, str]] = set()
    success_patterns = [
        ("save_success", "保存成功", "response_contains/no_save_failure"),
        ("submit_success", "提交成功", "response_contains/no_save_failure"),
        ("audit_success", "审核成功", "response_contains/no_save_failure"),
        ("operation_success", "操作成功", "response_contains/no_save_failure"),
    ]
    failure_patterns = [
        ("invalid_request", "无效请求", "no_error_actions/no_save_failure"),
        ("required_missing", "必填", "expected_notification 或字段补全"),
        ("empty_required", "不能为空", "expected_notification 或字段补全"),
        ("backend_error", "异常", "no_error_actions/no_save_failure"),
        ("operation_failed", "失败", "no_error_actions/no_save_failure"),
        ("task_missing", "任务不存在", "动态 billno/task_row 链路"),
        ("page_expired", "页面未初始化或者已经过期", "pageid_trace"),
    ]
    for event in run_events:
        if not isinstance(event, dict):
            continue
        if event.get("type") not in {"step_ok", "step_fail", "assertion_fail", "case_error"}:
            continue
        data = event.get("data") if isinstance(event.get("data"), dict) else {}
        step_id = str(data.get("step_id") or data.get("id") or "")
        text = json.dumps(data.get("response", data), ensure_ascii=False, default=str)
        for code, phrase, suggestion in success_patterns + failure_patterns:
            if phrase not in text:
                continue
            key = (step_id, code, phrase)
            if key in seen:
                continue
            seen.add(key)
            candidates.append({
                "step_id": step_id,
                "anchor_code": code,
                "anchor_phrase": phrase,
                "polarity": "success" if (code, phrase, suggestion) in success_patterns else "failure",
                "suggested_assertion": suggestion,
                "confidence": "medium",
            })
            if len(candidates) >= 40:
                return candidates
    return candidates


def _warnings(
    case: dict[str, Any],
    steps: list[dict[str, Any]],
    pageid_trace: dict[str, Any],
    dynamic_value_flow: dict[str, Any],
) -> list[dict[str, Any]]:
    warnings: list[dict[str, Any]] = []
    if not case.get("steps"):
        warnings.append({"code": "steps_missing", "message": "YAML 未包含可分析步骤。"})
    if pageid_trace.get("summary", {}).get("risk_steps", 0):
        warnings.append({
            "code": "pageid_trace_risk",
            "message": "存在 pageId 角色或切换风险，AI 修复应优先比对 HAR 原始链路与回放链路。",
        })
    if any(step.get("role") == "write" for step in steps) and not case.get("assertions"):
        warnings.append({
            "code": "write_assertions_missing",
            "message": "存在写入步骤但没有断言，需确认 no_save_failure 或入库验证策略。",
        })
    if len(case.get("steps") or []) > len(steps):
        warnings.append({
            "code": "steps_truncated",
            "message": f"证据包仅展示前 {len(steps)} 个步骤，完整 YAML 仍在 case_artifacts.yaml。",
        })
    if (dynamic_value_flow.get("summary") or {}).get("warning_count"):
        warnings.append({
            "code": "dynamic_value_flow_warnings",
            "message": "运行时动态值生产/消费链存在风险，需查看 dynamic_value_flow.warnings。",
        })
    return warnings


def _step_role(step: dict[str, Any]) -> str:
    role = classify_yaml_step_role(step)
    if role == "write":
        return "write"
    if role == "edit":
        return "edit"
    if role == "navigation":
        return "navigation_or_list"
    return "unknown"


def _value_shape(value: Any) -> str:
    if value is None:
        return "null"
    if isinstance(value, bool):
        return "bool"
    if isinstance(value, (int, float)):
        return "number"
    if isinstance(value, (list, tuple)):
        return f"list[{len(value)}]"
    if isinstance(value, dict):
        return f"dict[{len(value)}]"
    text = str(value)
    if re.search(r"\$\{[^}]+}", text):
        return "template"
    if re.fullmatch(r"\d{4}-\d{2}-\d{2}", text):
        return "date"
    if re.fullmatch(r"\d+", text):
        return f"numeric_string_len_{len(text)}"
    if re.fullmatch(r"[A-Za-z0-9_-]+", text):
        return f"code_like_len_{len(text)}"
    return f"text_len_{len(text)}"
