"""IR contracts for F7, selector, entry-grid and dialog boundaries."""
from __future__ import annotations

from typing import Any, Mapping


SCHEMA_VERSION = "1.0"
COMPLEX_KINDS = {
    "dialog_open",
    "selector_query",
    "selector_apply",
    "grid_row_select",
    "entry_field_update",
    "dialog_confirm",
}
HIGH_RISK_KINDS = {
    "selector_apply",
    "grid_row_select",
    "entry_field_update",
    "dialog_confirm",
}


def build_ir_interaction_contract(flow: Mapping[str, Any] | None) -> dict[str, Any]:
    flow = flow if isinstance(flow, Mapping) else {}
    requests = flow.get("request") if isinstance(flow.get("request"), Mapping) else {}
    responses = flow.get("response") if isinstance(flow.get("response"), Mapping) else {}
    rows: list[dict[str, Any]] = []

    for order, step in enumerate(flow.get("steps") or []):
        if not isinstance(step, Mapping):
            continue
        request = requests.get(str(step.get("request_ref") or ""), {})
        response = responses.get(str(step.get("response_ref") or ""), {})
        action = request.get("action") if isinstance(request.get("action"), Mapping) else {}
        kind = _interaction_kind(action, response)
        if not kind:
            continue
        field_refs = [
            {
                "field_key": str(item.get("field_key") or ""),
                "row_index": item.get("row_index"),
                "value_shape": str(item.get("value_shape") or ""),
            }
            for item in (action.get("field_refs") or [])
            if isinstance(item, Mapping)
        ]
        rows.append({
            "ir_step_id": str(step.get("id") or f"step_{order + 1}"),
            "order": order,
            "kind": kind,
            "role": str(step.get("role") or ""),
            "form_id": str(request.get("form_id") or ""),
            "app_id": str(request.get("app_id") or ""),
            "ac": str(request.get("ac") or ""),
            "method": str(action.get("method") or request.get("invoke_method") or ""),
            "key": str(action.get("key") or ""),
            "source_index": step.get("source_index"),
            "action_index": step.get("action_index"),
            "page_ref": str(step.get("page_ref") or ""),
            "selector_interface": str(action.get("selector_interface") or ""),
            "field_refs": field_refs,
            "requires_exact_provenance": kind in HIGH_RISK_KINDS,
        })

    return {
        "schema_version": SCHEMA_VERSION,
        "raw_values_included": False,
        "interactions": rows,
        "summary": {
            "interaction_count": len(rows),
            "high_risk_count": sum(1 for row in rows if row["kind"] in HIGH_RISK_KINDS),
            "kind_counts": {
                kind: sum(1 for row in rows if row["kind"] == kind)
                for kind in sorted(COMPLEX_KINDS)
                if any(row["kind"] == kind for row in rows)
            },
        },
    }


def apply_ir_interaction_contracts(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
) -> dict[str, Any]:
    yaml_steps = yaml_steps or []
    report = build_ir_interaction_bridge(flow, yaml_steps)
    by_step = {
        str(item.get("yaml_step_id") or ""): item
        for item in report.get("interaction_map") or []
        if item.get("coverage") == "covered" and item.get("yaml_step_id")
    }
    for step in yaml_steps:
        match = by_step.get(str(step.get("id") or ""))
        if not match:
            continue
        step["ir_interaction_kind"] = str(match.get("kind") or "")
        step["ir_interaction_source"] = {
            "source_index": match.get("source_index"),
            "action_index": match.get("action_index"),
        }
    return report


def build_ir_interaction_bridge(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
) -> dict[str, Any]:
    contract = build_ir_interaction_contract(flow)
    candidates = [
        _yaml_candidate(step, index)
        for index, step in enumerate(yaml_steps or [])
        if isinstance(step, Mapping)
    ]
    used: set[int] = set()
    mapped: list[dict[str, Any]] = []
    covered = 0
    uncovered_high_risk = 0

    for row in contract["interactions"]:
        match = _best_match(row, candidates, used)
        if match:
            used.add(match["index"])
            covered += 1
        elif row["kind"] in HIGH_RISK_KINDS:
            uncovered_high_risk += 1
        mapped.append({
            **row,
            "coverage": "covered" if match else "uncovered",
            "yaml_step_id": str((match or {}).get("step_id") or ""),
            "yaml_step_type": str((match or {}).get("step_type") or ""),
            "match_reason": str((match or {}).get("match_reason") or ""),
        })

    total = len(mapped)
    score = round(covered / total * 100) if total else 100
    return {
        "schema_version": SCHEMA_VERSION,
        "status": "blocked" if uncovered_high_risk else (
            "ready" if covered == total else "ready_with_warnings"
        ),
        "raw_values_included": False,
        "coverage_score": score,
        "summary": {
            **contract["summary"],
            "covered_count": covered,
            "uncovered_count": total - covered,
            "uncovered_high_risk_count": uncovered_high_risk,
        },
        "interaction_map": mapped,
        "policy": {
            "high_risk_requires_exact_source_action": True,
            "missing_high_risk_blocks_generation": True,
        },
    }


def _interaction_kind(action: Mapping[str, Any], response: Mapping[str, Any]) -> str:
    operation = str(action.get("operation_kind") or "")
    key = str(action.get("key") or "").strip().lower()
    method = str(action.get("method") or "").strip().lower()
    refs = [item for item in (action.get("field_refs") or []) if isinstance(item, Mapping)]
    if operation == "lookup_query":
        return "selector_query"
    if operation == "lookup_selection":
        return "selector_apply"
    if operation == "grid_selection":
        return "grid_row_select"
    if operation == "field_update" and any(item.get("row_index") is not None for item in refs):
        return "entry_field_update"
    if operation == "write_confirm" or key in {"btnok", "btn_ok", "ok"}:
        return "dialog_confirm"
    if response.get("has_pageid") and (
        key in {"newentry", "btnnew", "addnew"}
        or "newentry" in method
    ):
        return "dialog_open"
    return ""


def _yaml_candidate(step: Mapping[str, Any], index: int) -> dict[str, Any]:
    sources = step.get("ir_sources") if isinstance(step.get("ir_sources"), list) else []
    primary = next((item for item in sources if isinstance(item, Mapping)), {})
    return {
        "index": index,
        "step_id": str(step.get("id") or f"step_{index + 1}"),
        "step_type": str(step.get("type") or ""),
        "form_id": str(step.get("form_id") or ""),
        "ac": str(step.get("ac") or ""),
        "method": str(step.get("method") or ""),
        "key": str(step.get("key") or ""),
        "source_index": step.get("_har_index", primary.get("source_index")),
        "action_index": step.get("_har_action_index", primary.get("action_index")),
    }


def _best_match(
    row: Mapping[str, Any],
    candidates: list[dict[str, Any]],
    used: set[int],
) -> dict[str, Any]:
    for candidate in candidates:
        if candidate["index"] in used:
            continue
        if (
            row.get("source_index") is not None
            and candidate.get("source_index") == row.get("source_index")
            and candidate.get("action_index") == row.get("action_index")
        ):
            return {**candidate, "match_reason": "source+action"}
    expected_types = {
        "selector_query": {"invoke", "pick_basedata", "select_f7_list_row"},
        "selector_apply": {"invoke", "pick_basedata", "select_f7_list_row"},
        "grid_row_select": {"invoke", "select_f7_list_row"},
        "entry_field_update": {"invoke", "update_fields"},
        "dialog_confirm": {"invoke", "click_toolbar"},
        "dialog_open": {"invoke", "click_toolbar"},
    }.get(str(row.get("kind") or ""), set())
    for candidate in candidates:
        if candidate["index"] in used or candidate["step_type"] not in expected_types:
            continue
        if row.get("form_id") and candidate["form_id"] != row.get("form_id"):
            continue
        if row.get("key") and candidate["key"] == row.get("key"):
            return {**candidate, "match_reason": "form+key"}
        if row.get("ac") and candidate["ac"] == row.get("ac"):
            return {**candidate, "match_reason": "form+ac"}
        if row.get("method") and candidate["method"] == row.get("method"):
            return {**candidate, "match_reason": "form+method"}
    return {}
