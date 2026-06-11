"""Case-level contract helpers for HAR → YAML → replay.

This module intentionally stays value-safe and schema-light.  It does not try
to execute anything; it only summarizes what the generated YAML promises, what
must be resolved in the target environment, and which runtime values must be
produced before later steps consume them.
"""
from __future__ import annotations

import json
import re
from typing import Any, Mapping


SCHEMA_VERSION = "1.0"

WRITE_ACS = {
    "save",
    "submit",
    "audit",
    "unaudit",
    "delete",
    "modify",
    "saveandeffect",
    "submitandeffect",
    "saveandaudit",
    "doconfirm",
    "afterconfirm",
    "startupflow",
}

WRITE_KEYS = {
    "btnsave",
    "btn_save",
    "bar_save",
    "barsave",
    "btn_confirm",
    "btnconfirm",
    "bar_confirm",
    "barconfirm",
    "btnok",
    "btn_ok",
    "bar_submit",
    "barsubmit",
    "barstart",
    "bar_start",
    "btn_delete",
    "bardelete",
}

QUERY_ACS = {"loaddata", "commonsearch", "query", "getlookuplist", "querytreenodechildren"}
PARTIAL_STEP_TYPES = {"upload_file"}


def build_case_contract(case: Mapping[str, Any] | None) -> dict[str, Any]:
    """Build a first-class, value-safe contract summary for a YAML case."""
    case = case if isinstance(case, Mapping) else {}
    steps = [step for step in (case.get("steps") or []) if isinstance(step, Mapping)]
    pick_fields = case.get("pick_fields") if isinstance(case.get("pick_fields"), Mapping) else {}
    vars_meta = case.get("vars_meta") if isinstance(case.get("vars_meta"), Mapping) else {}

    environment_binding_plan = build_environment_binding_plan(case)
    runtime_value_flow_plan = build_runtime_value_flow_plan(case)
    capability = build_capability(case, environment_binding_plan, runtime_value_flow_plan)
    ai_assistance = build_ai_assistance(case, capability, environment_binding_plan, runtime_value_flow_plan)
    execution_contract = build_execution_contract(case, capability)

    return {
        "schema_version": SCHEMA_VERSION,
        "capability": capability,
        "ai_assistance": ai_assistance,
        "environment_binding_plan": environment_binding_plan,
        "runtime_value_flow_plan": runtime_value_flow_plan,
        "execution_contract": execution_contract,
        "field_model_summary": {
            "business_variable_count": len(vars_meta),
            "environment_field_count": len(pick_fields),
            "maintainable_field_count": len(vars_meta) + len(pick_fields),
            "step_count": len(steps),
        },
    }


def attach_case_contract(case: dict[str, Any]) -> dict[str, Any]:
    """Attach contract sections to a mutable YAML case and return it."""
    contract = build_case_contract(case)
    case.setdefault("schema_version", 1)
    case["capability"] = contract["capability"]
    case["ai_assistance"] = contract["ai_assistance"]
    case["environment_binding_plan"] = contract["environment_binding_plan"]
    case["runtime_value_flow_plan"] = contract["runtime_value_flow_plan"]
    case["execution_contract"] = contract["execution_contract"]
    return case


def validate_case_contract_for_run(case: Mapping[str, Any] | None) -> dict[str, Any]:
    """Validate whether a case is safe enough to start replay.

    The validator is deliberately conservative only for system guardrails.  It
    blocks unsupported/unsafe write cases, but leaves dynamic-value concerns as
    warnings so the existing runtime repair mechanisms can still handle them.
    """
    contract = build_case_contract(case)
    capability = contract["capability"]
    env_plan = contract["environment_binding_plan"]
    runtime_plan = contract["runtime_value_flow_plan"]
    execution_contract = contract["execution_contract"]

    errors: list[str] = []
    warnings: list[str] = []

    if capability.get("status") == "unsupported":
        reasons = capability.get("unsupported_reasons") or ["unsupported_case"]
        errors.append("当前 YAML 被标记为 unsupported: " + ", ".join(str(item) for item in reasons))

    missing_required_env = [
        item for item in env_plan.get("fields") or []
        if isinstance(item, Mapping)
        and item.get("required")
        and item.get("failure_policy") == "block_before_write"
        and item.get("status") in {"missing", "unresolved"}
    ]
    for item in missing_required_env[:8]:
        label = item.get("label") or item.get("id")
        errors.append(f"目标环境必需字段未配置或无法解析: {label} ({item.get('id')})")

    missing_checks = set(execution_contract.get("missing_recommended_checks") or [])
    if capability.get("write_mode") == "write" and "no_save_failure" in missing_checks:
        errors.append("写入用例缺少系统断言 no_save_failure，不能执行写库回放。")
    if "no_error_actions" in missing_checks:
        warnings.append("用例缺少 no_error_actions 断言，接口错误可能无法被基础校验捕获。")

    if capability.get("partial_supported_reasons"):
        warnings.extend(
            f"partial_supported: {reason}"
            for reason in capability.get("partial_supported_reasons") or []
        )
    if (env_plan.get("summary") or {}).get("static_id_risk_count"):
        warnings.append("存在录制环境内部 ID 风险，跨环境执行前需解析为目标环境真实 ID。")
    for item in (runtime_plan.get("warnings") or [])[:8]:
        if isinstance(item, Mapping):
            warnings.append(str(item.get("message") or item.get("code") or "runtime_value_flow_warning"))

    return {
        "ok": not errors,
        "errors": errors,
        "warnings": warnings,
        "contract": contract,
    }


def build_capability(
    case: Mapping[str, Any] | None,
    environment_binding_plan: Mapping[str, Any] | None = None,
    runtime_value_flow_plan: Mapping[str, Any] | None = None,
) -> dict[str, Any]:
    case = case if isinstance(case, Mapping) else {}
    steps = [step for step in (case.get("steps") or []) if isinstance(step, Mapping)]
    acs = {str(step.get("ac") or "").lower() for step in steps}
    step_types = {str(step.get("type") or "").lower() for step in steps}
    write_steps = [str(step.get("id") or "") for step in steps if is_write_step(step)]
    query_steps = [str(step.get("id") or "") for step in steps if is_query_step(step)]
    delete_steps = [
        str(step.get("id") or "")
        for step in steps
        if str(step.get("ac") or "").lower() == "delete"
        or "delete" in str(step.get("key") or "").lower()
    ]
    audit_steps = [
        str(step.get("id") or "")
        for step in steps
        if str(step.get("ac") or "").lower() in {"audit", "unaudit", "afterconfirm", "doconfirm", "startupflow"}
    ]
    upload_steps = [str(step.get("id") or "") for step in steps if str(step.get("type") or "") == "upload_file"]
    unresolved_bindings = [
        item for item in ((environment_binding_plan or {}).get("fields") or [])
        if isinstance(item, Mapping) and item.get("required") and item.get("status") in {"missing", "unresolved"}
    ]

    reasons: list[str] = []
    status = "supported"
    flow_kind = "query_only"
    if not steps:
        status = "unsupported"
        reasons.append("no_replay_steps")
        flow_kind = "empty"
    elif delete_steps:
        status = "partial_supported"
        reasons.append("delete_requires_target_data_selector_and_manual_confirmation")
        flow_kind = "delete"
    elif audit_steps:
        status = "partial_supported"
        reasons.append("workflow_or_audit_chain_depends_on_target_env_todo_and_permissions")
        flow_kind = "submit_or_audit"
    elif upload_steps:
        status = "partial_supported"
        reasons.append("upload_requires_user_file_and_runtime_upload_url")
        flow_kind = "write" if write_steps else "upload"
    elif write_steps:
        flow_kind = "write"
    elif query_steps:
        flow_kind = "query_only"
    else:
        status = "partial_supported"
        reasons.append("no_write_or_query_anchor_detected")
        flow_kind = "navigation_or_ui"

    if unresolved_bindings and status == "supported":
        status = "partial_supported"
        reasons.append("required_environment_bindings_need_target_env_resolution")
    if PARTIAL_STEP_TYPES & step_types and "upload_requires_user_file_and_runtime_upload_url" not in reasons:
        status = "partial_supported"
        reasons.append("contains_partial_supported_step_type")

    return {
        "status": status,
        "flow_kind": flow_kind,
        "write_mode": "write" if write_steps else "read_only",
        "unsupported_reasons": reasons if status == "unsupported" else [],
        "partial_supported_reasons": reasons if status == "partial_supported" else [],
        "write_step_ids": [sid for sid in write_steps if sid],
        "query_step_ids": [sid for sid in query_steps if sid],
        "audit_step_ids": [sid for sid in audit_steps if sid],
        "delete_step_ids": [sid for sid in delete_steps if sid],
        "upload_step_ids": [sid for sid in upload_steps if sid],
        "detected_actions": sorted(item for item in acs if item),
        "requires_readback": bool(write_steps),
        "requires_environment_preflight": bool((environment_binding_plan or {}).get("fields")),
        "requires_runtime_value_flow": bool((runtime_value_flow_plan or {}).get("consumers")),
    }


def build_environment_binding_plan(case: Mapping[str, Any] | None) -> dict[str, Any]:
    case = case if isinstance(case, Mapping) else {}
    fields: list[dict[str, Any]] = []
    pick_fields = case.get("pick_fields") if isinstance(case.get("pick_fields"), Mapping) else {}
    has_write = any(is_write_step(step) for step in case.get("steps") or [] if isinstance(step, Mapping))

    for field_id, meta in pick_fields.items():
        if not isinstance(meta, Mapping):
            continue
        resolver_kind = _resolver_kind(str(field_id), meta)
        query_value = _query_value(meta)
        status = _binding_status(meta, query_value=query_value)
        is_soft_runtime_context = bool(
            meta.get("required_context")
            or str(meta.get("source") or "") == "runtime_rule"
            or status == "missing_required_context"
        )
        required = bool(
            has_write
            and resolver_kind in {"lookup", "grid_selector"}
            and not meta.get("context_only")
            and not is_soft_runtime_context
        )
        fields.append({
            "id": str(field_id),
            "label": str(meta.get("label") or field_id),
            "field_key": str(meta.get("field_key") or ""),
            "form_id": str(meta.get("form_id") or ""),
            "app_id": str(meta.get("app_id") or ""),
            "group_key": str(meta.get("group_key") or ""),
            "group_label": str(meta.get("group_label") or ""),
            "source_step_id": str(meta.get("source_step_id") or ""),
            "write_step_id": str(meta.get("write_step_id") or ""),
            "resolver_kind": resolver_kind,
            "interface": _resolver_interface(resolver_kind),
            "query": query_value,
            "resolve_by": str(meta.get("resolve_by") or ""),
            "auto_resolve": bool(meta.get("auto_resolve")),
            "env_sensitive": str(meta.get("env_sensitive") or "medium"),
            "required": required,
            "status": status,
            "failure_policy": "block_before_write" if required else "warn",
            "recorded_static_id_risk": _looks_like_internal_id(meta.get("recorded_value_id") or meta.get("value_id")),
            "user_overridden": bool(meta.get("user_overridden") or meta.get("manual_override")),
        })

    return {
        "schema_version": SCHEMA_VERSION,
        "status": "ready",
        "fields": sorted(
            fields,
            key=lambda item: (
                str(item.get("group_key") or ""),
                str(item.get("source_step_id") or ""),
                str(item.get("id") or ""),
            ),
        ),
        "summary": {
            "field_count": len(fields),
            "required_count": sum(1 for item in fields if item.get("required")),
            "auto_resolve_count": sum(1 for item in fields if item.get("auto_resolve")),
            "static_id_risk_count": sum(1 for item in fields if item.get("recorded_static_id_risk")),
        },
    }


def build_runtime_value_flow_plan(case: Mapping[str, Any] | None) -> dict[str, Any]:
    case = case if isinstance(case, Mapping) else {}
    steps = [step for step in (case.get("steps") or []) if isinstance(step, Mapping)]
    producers: list[dict[str, Any]] = []
    consumers: list[dict[str, Any]] = []
    warnings: list[dict[str, Any]] = []
    seen_producers: set[tuple[str, str]] = set()

    for index, step in enumerate(steps):
        sid = str(step.get("id") or f"step_{index + 1}")
        ac = str(step.get("ac") or "").lower()
        if is_write_step(step):
            _remember_node(producers, seen_producers, "write_result", sid, "runtime_response", index)
            if ac in {"save", "saveandeffect", "saveandaudit"} or "save" in sid.lower():
                _remember_node(producers, seen_producers, "bill_id", sid, "save_response", index)
                _remember_node(producers, seen_producers, "billno", sid, "save_response", index)
        source_step = str(step.get("recorded_pageid_source_step_id") or "")
        if source_step:
            _remember_node(producers, seen_producers, "page_id", source_step, "recorded_pageid_source", index)
            consumers.append({
                "kind": "page_id",
                "step_id": sid,
                "source": "recorded_pageid_consumer",
                "order": index,
                "producer_step_id": source_step,
            })
        if _payload_contains_runtime_callback(step):
            consumers.append({
                "kind": "confirm_callback",
                "step_id": sid,
                "source": "recorded_afterconfirm_payload",
                "order": index,
            })
        if _payload_contains_workflow_search(step):
            consumers.append({
                "kind": "billno",
                "step_id": sid,
                "source": "workflow_or_query_filter",
                "order": index,
            })

    producer_keys = {(item["kind"], item["step_id"]) for item in producers}
    for consumer in consumers:
        kind = str(consumer.get("kind") or "")
        producer_step_id = str(consumer.get("producer_step_id") or "")
        if producer_step_id:
            continue
        has_prior = any(
            item.get("kind") == kind and int(item.get("order") or 0) < int(consumer.get("order") or 0)
            for item in producers
        )
        if not has_prior and kind in {"bill_id", "billno", "confirm_callback", "task_row", "upload_url"}:
            warnings.append({
                "code": "runtime_consumer_without_prior_producer",
                "kind": kind,
                "step_id": consumer.get("step_id", ""),
                "message": "后续步骤依赖运行时值，但 YAML 中没有明确的前序生产步骤。",
            })

    return {
        "schema_version": SCHEMA_VERSION,
        "status": "ready",
        "raw_values_included": False,
        "producers": producers,
        "consumers": consumers,
        "warnings": warnings,
        "summary": {
            "producer_count": len(producers),
            "consumer_count": len(consumers),
            "warning_count": len(warnings),
            "producer_kinds": sorted({item["kind"] for item in producers}),
            "consumer_kinds": sorted({item["kind"] for item in consumers}),
        },
    }


def build_ai_assistance(
    case: Mapping[str, Any] | None,
    capability: Mapping[str, Any],
    environment_binding_plan: Mapping[str, Any],
    runtime_value_flow_plan: Mapping[str, Any],
) -> dict[str, Any]:
    assumptions: list[str] = []
    need_confirm: list[str] = []
    confidence = "high"

    if capability.get("status") == "partial_supported":
        confidence = "medium"
        assumptions.extend(str(item) for item in capability.get("partial_supported_reasons") or [])
    if capability.get("status") == "unsupported":
        confidence = "low"
        assumptions.extend(str(item) for item in capability.get("unsupported_reasons") or [])
    if (environment_binding_plan.get("summary") or {}).get("required_count"):
        need_confirm.append("目标环境需能按用户维护的编码/名称解析 F7、下拉、基础资料字段。")
    if (environment_binding_plan.get("summary") or {}).get("static_id_risk_count"):
        confidence = "medium" if confidence == "high" else confidence
        need_confirm.append("存在 HAR 录制环境内部 ID，跨环境执行前必须解析为目标环境真实 ID。")
    if (runtime_value_flow_plan.get("summary") or {}).get("warning_count"):
        confidence = "medium" if confidence == "high" else confidence
        need_confirm.append("存在运行时值生产/消费风险，需要检查保存后 ID、单号、审批任务或回调值链路。")
    if capability.get("flow_kind") == "query_only":
        assumptions.append("该用例未检测到写入锚点，按只读查询/校验场景处理，不要求入库回查。")

    return {
        "confidence": confidence,
        "assumptions": assumptions,
        "need_confirm": need_confirm,
        "anti_hallucination": {
            "allow_ai_to_add_business_steps": False,
            "require_har_or_runtime_evidence": True,
            "require_manual_confirm_for_unsupported": True,
        },
    }


def build_execution_contract(case: Mapping[str, Any] | None, capability: Mapping[str, Any]) -> dict[str, Any]:
    assertions = [item for item in ((case or {}).get("assertions") or []) if isinstance(item, Mapping)]
    assertion_types = {str(item.get("type") or "") for item in assertions}
    write_mode = str(capability.get("write_mode") or "read_only")
    required = ["no_error_actions"]
    if write_mode == "write":
        required.append("no_save_failure")
        required.append("response_semantic_contract")
        required.append("readback_or_manual_write_verification")

    return {
        "schema_version": SCHEMA_VERSION,
        "write_mode": write_mode,
        "required_system_checks": required,
        "present_assertion_types": sorted(item for item in assertion_types if item),
        "missing_recommended_checks": [
            item for item in required
            if item not in assertion_types
            and item not in {"response_semantic_contract", "readback_or_manual_write_verification"}
        ],
        "user_validation_points_are_business_checks": True,
        "unchecked_user_points_do_not_disable_system_checks": True,
    }


def is_write_step(step: Mapping[str, Any]) -> bool:
    ac = str(step.get("ac") or "").lower()
    key = str(step.get("key") or "").lower()
    method = str(step.get("method") or "").lower()
    sid = str(step.get("id") or "").lower()
    args = step.get("args") or []
    arg_text = json.dumps(args, ensure_ascii=False).lower() if args else ""
    return (
        ac in WRITE_ACS
        or method in WRITE_ACS
        or key in WRITE_KEYS
        or any(token in arg_text for token in WRITE_KEYS)
        or any(token in sid for token in ("save", "submit", "audit", "delete"))
    )


def is_query_step(step: Mapping[str, Any]) -> bool:
    ac = str(step.get("ac") or "").lower()
    method = str(step.get("method") or "").lower()
    return ac in QUERY_ACS or method in QUERY_ACS


def _remember_node(
    target: list[dict[str, Any]],
    seen: set[tuple[str, str]],
    kind: str,
    step_id: str,
    source: str,
    order: int,
) -> None:
    key = (kind, step_id)
    if key in seen:
        return
    seen.add(key)
    target.append({
        "kind": kind,
        "step_id": step_id,
        "source": source,
        "order": order,
    })


def _resolver_kind(field_id: str, meta: Mapping[str, Any]) -> str:
    if meta.get("selector_source") == "entryRowClick" or field_id.startswith("selector_"):
        return "grid_selector"
    if field_id.startswith("pick_") or meta.get("auto_resolve"):
        return "lookup"
    if field_id.startswith("date_"):
        return "literal_date"
    if field_id.startswith("bool_"):
        return "literal_boolean"
    if field_id.startswith(("enum_", "num_")):
        return "literal"
    if meta.get("source_type") == "upload_file":
        return "user_file"
    return "manual"


def _resolver_interface(kind: str) -> str:
    return {
        "lookup": "getLookUpList",
        "grid_selector": "loadData/commonSearch",
        "literal": "update_fields",
        "literal_date": "update_fields",
        "literal_boolean": "update_fields",
        "user_file": "upload",
        "manual": "",
    }.get(kind, "")


def _query_value(meta: Mapping[str, Any]) -> str:
    resolve_by = str(meta.get("resolve_by") or "").strip()
    if resolve_by == "value_code" and str(meta.get("value_code") or "").strip():
        return str(meta.get("value_code") or "").strip()
    for key in ("value_code", "value_number", "value_name", "value_id"):
        value = str(meta.get(key) or "").strip()
        if value:
            return value
    return ""


def _binding_status(meta: Mapping[str, Any], *, query_value: str) -> str:
    raw_status = str(meta.get("resolve_status") or "").strip()
    if raw_status in {"resolved", "pending", "manual", "context", "missing_required_context"}:
        return raw_status
    if meta.get("context_only"):
        return "context"
    if meta.get("manual_override") or meta.get("user_overridden"):
        return "manual" if not meta.get("auto_resolve") else "pending"
    if query_value:
        return "pending" if meta.get("auto_resolve") else "manual"
    return "missing"


def _looks_like_internal_id(value: Any) -> bool:
    text = str(value or "").strip()
    return bool(re.fullmatch(r"\d{15,}", text))


def _payload_contains_runtime_callback(step: Mapping[str, Any]) -> bool:
    if str(step.get("ac") or "").lower() not in {"afterconfirm", "doconfirm"}:
        return False
    payload = json.dumps(step.get("args") or step.get("post_data") or [], ensure_ascii=False)
    return "callback" in payload.lower() or "pkvalue" in payload.lower()


def _payload_contains_workflow_search(step: Mapping[str, Any]) -> bool:
    if str(step.get("ac") or "").lower() not in {"commonsearch", "loaddata", "query"}:
        return False
    payload = json.dumps(step.get("args") or step.get("post_data") or [], ensure_ascii=False).lower()
    return "billno" in payload or "单据编号" in payload
