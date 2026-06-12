"""IR-driven write anchors and first-success contracts.

This module is value-safe. It identifies persistence semantics, maps recorded
IR anchors to generated YAML steps, and evaluates whether a write replay has
enough evidence to be called a verified first success.
"""
from __future__ import annotations

import re
from typing import Any, Mapping


SCHEMA_VERSION = "1.0"

WRITE_OPERATION_KINDS = {
    "write_save",
    "write_save_and_effect",
    "write_submit",
    "write_submit_and_effect",
    "write_save_and_audit",
    "write_audit",
    "write_unaudit",
    "write_approve",
    "write_reject",
    "write_delete",
    "write_confirm",
    "write_workflow_start",
}

_SAVE_KEYS = {
    "btnsave",
    "btn_save",
    "bar_save",
    "barsave",
    "btnsaveandeffect",
    "btn_saveandeffect",
}
_SUBMIT_KEYS = {"btnsubmit", "btn_submit", "bar_submit", "barsubmit"}
_BUSINESS_CONFIRM_KEYS = {
    "btnconfirm",
    "btn_confirm",
    "barconfirm",
    "bar_confirm",
}
_APPROVE_KEYS = {"btnapprove", "btn_approve", "barapprove", "bar_approve"}
_REJECT_KEYS = {"btnreject", "btn_reject", "barreject", "bar_reject"}
_DELETE_KEYS = {"btndelete", "btn_delete", "bardelete", "bar_delete"}
_SEMANTIC_ARG_KEYS = {
    "action",
    "actionid",
    "action_id",
    "button",
    "buttonid",
    "button_id",
    "cmd",
    "command",
    "event",
    "key",
    "method",
    "methodname",
    "operation",
}


def classify_write_operation(
    *,
    ac: Any = "",
    method: Any = "",
    key: Any = "",
    args: Any = None,
    step_id: Any = "",
) -> str:
    """Return a canonical write operation kind, or an empty string."""
    ac_text = str(ac or "").strip().lower()
    method_text = str(method or "").strip().lower()
    key_text = str(key or "").strip().lower()
    step_text = str(step_id or "").strip().lower()
    identifiers = {ac_text, method_text, key_text}
    identifiers.update(_semantic_arg_tokens(args))
    step_tokens = set(re.findall(r"[a-z0-9_]+", step_text))

    if "submitandeffect" in identifiers or "submitandeffect" in step_tokens:
        return "write_submit_and_effect"
    if "saveandeffect" in identifiers or "saveandeffect" in step_tokens:
        return "write_save_and_effect"
    if "saveandaudit" in identifiers or "saveandaudit" in step_tokens:
        return "write_save_and_audit"
    if "unaudit" in identifiers:
        return "write_unaudit"
    if (
        "delete" in identifiers
        or bool(_DELETE_KEYS & identifiers)
        or bool(_DELETE_KEYS & step_tokens)
    ):
        return "write_delete"
    if (
        bool(_REJECT_KEYS & identifiers)
        or bool(_REJECT_KEYS & step_tokens)
        or "reject" in identifiers
        or "驳回" in identifiers
    ):
        return "write_reject"
    if (
        bool(_APPROVE_KEYS & identifiers)
        or bool(_APPROVE_KEYS & step_tokens)
        or "approve" in identifiers
        or "同意" in identifiers
    ):
        return "write_approve"
    if "audit" in identifiers or "audit" in step_tokens:
        return "write_audit"
    if (
        "startupflow" in identifiers
        or "startupflow" in step_tokens
        or bool({"barstart", "bar_start"} & identifiers)
    ):
        return "write_workflow_start"
    if (
        "submit" in identifiers
        or bool(_SUBMIT_KEYS & identifiers)
    ):
        return "write_submit"
    if (
        "save" in identifiers
        or bool(_SAVE_KEYS & identifiers)
    ):
        return "write_save"
    if (
        bool({"doconfirm", "afterconfirm", "confirm"} & identifiers)
        or bool(_BUSINESS_CONFIRM_KEYS & identifiers)
    ):
        return "write_confirm"
    return ""


def is_write_operation_kind(kind: Any) -> bool:
    return str(kind or "") in WRITE_OPERATION_KINDS


def is_write_step(step: Mapping[str, Any] | None) -> bool:
    step = step if isinstance(step, Mapping) else {}
    explicit = str(step.get("ir_write_kind") or "")
    if explicit:
        return is_write_operation_kind(explicit)
    return bool(classify_write_operation(
        ac=step.get("ac"),
        method=step.get("method"),
        key=step.get("key"),
        args=step.get("args"),
        step_id=step.get("id"),
    ))


def apply_ir_write_contracts(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
) -> dict[str, Any]:
    """Annotate generated YAML write steps from exact IR provenance."""
    flow = flow if isinstance(flow, Mapping) else {}
    yaml_steps = yaml_steps or []
    rows = _ir_write_rows(flow)
    candidates = [_yaml_candidate(step, index) for index, step in enumerate(yaml_steps)]
    used: set[int] = set()
    applied = 0

    for row in rows:
        match = _best_match(row, candidates, used)
        if not match:
            continue
        used.add(int(match["index"]))
        step = yaml_steps[int(match["index"])]
        step["ir_write_anchor"] = True
        step["ir_write_kind"] = row["operation_kind"]
        step["expected_pageid_role"] = row["expected_pageid_role"]
        step["ir_write_contract_source"] = "normalized_flow"
        contract = row.get("recorded_response_contract") or {}
        if contract:
            existing = step.get("expected_response_signature")
            if not isinstance(existing, dict) or not existing:
                step["expected_response_signature"] = dict(contract)
            else:
                existing["contract_level"] = "critical"
                existing.setdefault("anchor_reason", "write_anchor")
                existing["ir_write_kind"] = row["operation_kind"]
        applied += 1

    report = build_ir_write_anchor_bridge(flow, yaml_steps)
    report["applied_count"] = applied
    return report


def build_ir_write_anchor_bridge(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
    *,
    assertions: list[dict[str, Any]] | None = None,
    max_items: int = 80,
) -> dict[str, Any]:
    """Explain whether every recorded write anchor is executable and guarded."""
    flow = flow if isinstance(flow, Mapping) else {}
    yaml_steps = yaml_steps or []
    rows = _ir_write_rows(flow)
    candidates = [_yaml_candidate(step, index) for index, step in enumerate(yaml_steps)]
    used: set[int] = set()
    mapped: list[dict[str, Any]] = []

    covered = 0
    contract_ready = 0
    minimal_contracts = 0
    l2_risks = 0
    kind_mismatches = 0
    for row in rows:
        match = _best_match(row, candidates, used)
        if match:
            used.add(int(match["index"]))
            covered += 1
        yaml_contract = (match or {}).get("response_contract") or {}
        contract_level = str(yaml_contract.get("contract_level") or "")
        if contract_level == "critical":
            contract_ready += 1
        if str((row.get("recorded_response_contract") or {}).get("contract_strength") or "") == "minimal":
            minimal_contracts += 1
        if (
            row.get("expected_pageid_role") == "L3"
            and row.get("recorded_pageid_type") != "L2"
            and (match or {}).get("preserve_l2_page")
        ):
            l2_risks += 1
        if match and match.get("operation_kind") and match.get("operation_kind") != row.get("operation_kind"):
            kind_mismatches += 1
        if len(mapped) < max_items:
            mapped.append({
                **row,
                "coverage": "covered" if match else "uncovered",
                "yaml_step_id": str((match or {}).get("step_id") or ""),
                "yaml_step_type": str((match or {}).get("step_type") or ""),
                "yaml_operation_kind": str((match or {}).get("operation_kind") or ""),
                "response_contract_level": contract_level,
                "match_reason": str((match or {}).get("match_reason") or ""),
            })

    assertion_types = {
        str(item.get("type") or "")
        for item in (assertions or [])
        if isinstance(item, Mapping)
    }
    total = len(rows)
    uncovered = max(total - covered, 0)
    missing_contracts = max(total - contract_ready, 0)
    no_save_guard_missing = bool(total and assertions is not None and "no_save_failure" not in assertion_types)
    status = "ready"
    if uncovered or missing_contracts or l2_risks or kind_mismatches or no_save_guard_missing:
        status = "blocked"
    score = 100
    if total:
        score = max(0, round(
            (
                covered
                + contract_ready
                + max(total - l2_risks - kind_mismatches, 0)
            )
            / (total * 3)
            * 100
        ))
    return {
        "schema_version": SCHEMA_VERSION,
        "status": status,
        "raw_values_included": False,
        "coverage_score": score,
        "summary": _bridge_summary(
            status=status,
            score=score,
            uncovered=uncovered,
            missing_contracts=missing_contracts,
            l2_risks=l2_risks,
            kind_mismatches=kind_mismatches,
        ),
        "checks": {
            "ir_write_anchor_count": total,
            "covered_write_anchor_count": covered,
            "uncovered_write_anchor_count": uncovered,
            "critical_response_contract_count": contract_ready,
            "critical_response_contract_missing_count": missing_contracts,
            "minimal_recorded_contract_count": minimal_contracts,
            "write_anchor_l2_risk_count": l2_risks,
            "write_kind_mismatch_count": kind_mismatches,
            "no_save_failure_missing": no_save_guard_missing,
        },
        "write_anchor_map": mapped,
        "first_success_requirements": {
            "all_write_anchors_covered": True,
            "critical_request_contracts": True,
            "critical_response_contracts": True,
            "maintained_values_applied": True,
            "no_save_failure": True,
            "write_response_evidence": True,
            "readback_or_manual_verification": True,
        },
    }


def build_case_write_anchor_plan(case: Mapping[str, Any] | None) -> dict[str, Any]:
    """Build a runtime-ready write plan from generated YAML only."""
    case = case if isinstance(case, Mapping) else {}
    assertions = [
        item for item in (case.get("assertions") or [])
        if isinstance(item, Mapping)
    ]
    assertion_types = {str(item.get("type") or "") for item in assertions}
    anchors = []
    for index, step in enumerate(case.get("steps") or []):
        if not isinstance(step, Mapping) or not is_write_step(step):
            continue
        contract = step.get("expected_response_signature")
        contract = contract if isinstance(contract, Mapping) else {}
        kind = str(step.get("ir_write_kind") or classify_write_operation(
            ac=step.get("ac"),
            method=step.get("method"),
            key=step.get("key"),
            args=step.get("args"),
            step_id=step.get("id"),
        ))
        anchors.append({
            "step_id": str(step.get("id") or f"step_{index + 1}"),
            "order": index,
            "form_id": str(step.get("form_id") or ""),
            "operation_kind": kind,
            "expected_pageid_role": str(step.get("expected_pageid_role") or ""),
            "response_contract_level": str(contract.get("contract_level") or ""),
            "response_contract_strength": str(contract.get("contract_strength") or ""),
            "has_request_contract": bool(step.get("expected_request_signature")),
            "has_response_contract": bool(contract),
            "optional": bool(step.get("optional")),
            "ir_driven": bool(step.get("ir_write_anchor")),
        })
    ir_driven = any(item.get("ir_driven") for item in anchors)
    missing_response = [
        item["step_id"] for item in anchors
        if item.get("ir_driven") and item.get("response_contract_level") != "critical"
    ]
    missing_request = [
        item["step_id"] for item in anchors
        if item.get("ir_driven") and not item.get("has_request_contract")
    ]
    return {
        "schema_version": SCHEMA_VERSION,
        "status": "blocked" if missing_response or missing_request else "ready",
        "anchors": anchors,
        "summary": {
            "write_anchor_count": len(anchors),
            "ir_driven_anchor_count": sum(1 for item in anchors if item.get("ir_driven")),
            "critical_response_contract_count": sum(
                1 for item in anchors if item.get("response_contract_level") == "critical"
            ),
            "missing_response_contract_count": len(missing_response),
            "missing_request_contract_count": len(missing_request),
            "has_no_save_failure": "no_save_failure" in assertion_types,
            "has_no_error_actions": "no_error_actions" in assertion_types,
        },
        "missing_response_contract_step_ids": missing_response,
        "missing_request_contract_step_ids": missing_request,
    }


def evaluate_first_success_gate(
    case: Mapping[str, Any] | None,
    *,
    passed: bool,
    assertions: list[dict[str, Any]] | None,
    runtime_evidence: Mapping[str, Any] | None,
    executed_step_ids: set[str] | None = None,
    response_step_ids: set[str] | None = None,
) -> dict[str, Any]:
    """Evaluate the write replay as failed, unverified, or fully verified."""
    plan = build_case_write_anchor_plan(case)
    anchors = [item for item in plan.get("anchors") or [] if not item.get("optional")]
    if not anchors:
        return {
            "schema_version": SCHEMA_VERSION,
            "status": "not_applicable",
            "verified": bool(passed),
            "summary": "只读用例不适用写入首次成功门槛。",
            "checks": {},
            "missing": [],
        }

    evidence = runtime_evidence if isinstance(runtime_evidence, Mapping) else {}
    request_results = evidence.get("request_contract_results") or {}
    response_results = evidence.get("response_contract_results") or {}
    executed_step_ids = executed_step_ids or set()
    response_step_ids = response_step_ids or set()
    anchor_ids = {str(item.get("step_id") or "") for item in anchors}
    executed = {
        step_id for step_id in anchor_ids
        if step_id in executed_step_ids or step_id in response_step_ids or step_id in response_results
    }
    request_failures = _contract_failure_count(request_results, anchor_ids)
    response_failures = _contract_failure_count(response_results, anchor_ids)
    response_evaluated = {
        step_id for step_id in anchor_ids
        if isinstance(response_results.get(step_id), Mapping)
        and response_results.get(step_id, {}).get("contract_level")
    }
    assertion_rows = assertions or []
    hard_assertion_failures = [
        item for item in assertion_rows
        if isinstance(item, Mapping) and not item.get("ok") and not item.get("advisory")
    ]
    readbacks = [
        item for item in assertion_rows
        if isinstance(item, Mapping) and str(item.get("type") or "").startswith("readback")
    ]
    readback_verified = any(item.get("ok") and not item.get("advisory") for item in readbacks)
    manual_verified = bool(evidence.get("manual_write_verified"))
    maintenance_expected = int(evidence.get("maintenance_expected_count") or 0)
    maintenance_matched = int(evidence.get("maintenance_matched_count") or 0)

    checks = {
        "case_passed": bool(passed),
        "write_anchor_count": len(anchor_ids),
        "executed_write_anchor_count": len(executed),
        "critical_response_contract_evaluated_count": len(response_evaluated),
        "request_contract_failure_count": request_failures,
        "response_contract_failure_count": response_failures,
        "hard_assertion_failure_count": len(hard_assertion_failures),
        "maintenance_expected_count": maintenance_expected,
        "maintenance_matched_count": maintenance_matched,
        "readback_verified": readback_verified,
        "manual_write_verified": manual_verified,
    }
    missing: list[str] = []
    if len(executed) != len(anchor_ids):
        missing.append("write_anchor_execution")
    if len(response_evaluated) != len(anchor_ids):
        missing.append("critical_response_contract")
    if request_failures:
        missing.append("request_contract")
    if response_failures:
        missing.append("response_contract")
    if maintenance_expected != maintenance_matched:
        missing.append("maintained_values")
    if hard_assertion_failures:
        missing.append("system_assertions")
    if not (readback_verified or manual_verified):
        missing.append("readback_or_manual_verification")

    hard_failure = bool(
        not passed
        or len(executed) != len(anchor_ids)
        or len(response_evaluated) != len(anchor_ids)
        or request_failures
        or response_failures
        or maintenance_expected != maintenance_matched
        or hard_assertion_failures
    )
    verified = not hard_failure and (readback_verified or manual_verified)
    status = "verified" if verified else "failed" if hard_failure else "write_unverified"
    summary = {
        "verified": "首次执行、关键契约、维护值和入库证据均已闭环。",
        "failed": "写入链路未达到首次成功门槛，需修复脚本或目标环境问题。",
        "write_unverified": "执行与关键契约通过，但缺少只读回查或人工入库确认。",
    }[status]
    return {
        "schema_version": SCHEMA_VERSION,
        "status": status,
        "verified": verified,
        "summary": summary,
        "checks": checks,
        "missing": missing,
    }


def _ir_write_rows(flow: Mapping[str, Any]) -> list[dict[str, Any]]:
    requests = flow.get("request") if isinstance(flow.get("request"), Mapping) else {}
    responses = flow.get("response") if isinstance(flow.get("response"), Mapping) else {}
    pages = {
        str(item.get("id") or ""): item
        for item in (flow.get("pages") or [])
        if isinstance(item, Mapping)
    }
    rows = []
    for step in flow.get("steps") or []:
        if not isinstance(step, Mapping):
            continue
        request = requests.get(str(step.get("request_ref") or ""), {})
        action = request.get("action") if isinstance(request.get("action"), Mapping) else {}
        operation_kind = str(action.get("operation_kind") or "")
        if not is_write_operation_kind(operation_kind):
            operation_kind = classify_write_operation(
                ac=request.get("ac"),
                method=request.get("invoke_method"),
                key=action.get("key"),
            )
        if not operation_kind:
            continue
        response = responses.get(str(step.get("response_ref") or ""), {})
        page = pages.get(str(step.get("page_ref") or ""), {})
        rows.append({
            "ir_step_id": str(step.get("id") or ""),
            "source_index": step.get("source_index"),
            "action_index": step.get("action_index"),
            "form_id": str(request.get("form_id") or ""),
            "app_id": str(request.get("app_id") or ""),
            "operation_kind": operation_kind,
            "expected_pageid_role": str(page.get("expected_role") or "L3"),
            "recorded_pageid_type": str(page.get("pageid_type") or ""),
            "recorded_response_contract": (
                response.get("semantic_contract")
                if isinstance(response.get("semantic_contract"), Mapping)
                else {}
            ),
        })
    return rows


def _yaml_candidate(step: Mapping[str, Any], index: int) -> dict[str, Any]:
    sources = step.get("ir_sources") if isinstance(step.get("ir_sources"), list) else []
    if not sources and step.get("_har_index") is not None:
        sources = [{
            "source_index": step.get("_har_index"),
            "action_index": step.get("_har_action_index"),
        }]
    operation_kind = str(step.get("ir_write_kind") or classify_write_operation(
        ac=step.get("ac"),
        method=step.get("method"),
        key=step.get("key"),
        args=step.get("args"),
        step_id=step.get("id"),
    ))
    return {
        "index": index,
        "step_id": str(step.get("id") or f"step_{index + 1}"),
        "step_type": str(step.get("type") or ""),
        "form_id": str(step.get("form_id") or ""),
        "app_id": str(step.get("app_id") or ""),
        "operation_kind": operation_kind,
        "ir_sources": [
            {
                "source_index": item.get("source_index"),
                "action_index": item.get("action_index"),
            }
            for item in sources
            if isinstance(item, Mapping)
        ],
        "preserve_l2_page": bool(step.get("preserve_l2_page")),
        "response_contract": (
            step.get("expected_response_signature")
            if isinstance(step.get("expected_response_signature"), Mapping)
            else {}
        ),
    }


def _best_match(
    row: Mapping[str, Any],
    candidates: list[dict[str, Any]],
    used: set[int],
) -> dict[str, Any] | None:
    for candidate in candidates:
        if candidate["index"] in used or not candidate.get("operation_kind"):
            continue
        if any(
            source.get("source_index") == row.get("source_index")
            and source.get("action_index") == row.get("action_index")
            for source in candidate.get("ir_sources") or []
        ):
            return {**candidate, "match_reason": "ir_source"}
    for candidate in candidates:
        if candidate["index"] in used or not candidate.get("operation_kind"):
            continue
        if row.get("form_id") and candidate.get("form_id") != row.get("form_id"):
            continue
        if candidate.get("operation_kind") != row.get("operation_kind"):
            continue
        return {**candidate, "match_reason": "form+operation_kind"}
    return None


def _semantic_arg_tokens(args: Any) -> set[str]:
    """Extract action identifiers without scanning arbitrary payload values."""
    tokens: set[str] = set()

    def add(value: Any) -> None:
        if isinstance(value, str):
            text = value.strip().lower()
            if not text:
                return
            tokens.add(text)
            tokens.update(re.findall(r"[a-z0-9_]+", text))
            return
        if isinstance(value, (list, tuple)):
            for item in value:
                if isinstance(item, Mapping):
                    for key, nested in item.items():
                        if str(key or "").strip().lower() in _SEMANTIC_ARG_KEYS:
                            add(nested)
                elif isinstance(item, (str, int, float)):
                    add(item)
            return
        if isinstance(value, Mapping):
            for key, nested in value.items():
                if str(key or "").strip().lower() in _SEMANTIC_ARG_KEYS:
                    add(nested)

    add(args)
    return tokens


def _contract_failure_count(results: Any, anchor_ids: set[str]) -> int:
    if not isinstance(results, Mapping):
        return 0
    return sum(
        len((item or {}).get("errors") or [])
        for step_id, item in results.items()
        if step_id in anchor_ids and isinstance(item, Mapping)
    )


def _bridge_summary(
    *,
    status: str,
    score: int,
    uncovered: int,
    missing_contracts: int,
    l2_risks: int,
    kind_mismatches: int,
) -> str:
    if status == "ready":
        return f"{score} 分：IR 写入锚点均已进入 YAML，并带关键响应契约。"
    return (
        f"{score} 分：未覆盖写入 {uncovered}，缺关键响应契约 {missing_contracts}，"
        f"L2 风险 {l2_risks}，动作类型不一致 {kind_mismatches}。"
    )
