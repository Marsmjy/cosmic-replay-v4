"""Build and validate normalized_flow.json structures."""
from __future__ import annotations

from typing import Any

from .detector import enrich_entries
from .interaction_contract import build_ir_interaction_contract
from .normalizer import normalize_har_entries
from .write_contract import classify_write_operation, is_write_operation_kind
from lib.response_signature import (
    is_meaningful_response_signature,
    specialize_response_signature,
)

SCHEMA_VERSION = "0.4"


def build_normalized_flow(
    har: dict[str, Any],
    *,
    source_name: str = "",
    environment: dict[str, Any] | None = None,
    playwright_context: dict[str, Any] | None = None,
) -> dict[str, Any]:
    normalized = normalize_har_entries(har)
    enriched = enrich_entries(normalized["entries"], playwright_context=playwright_context)
    requests: dict[str, Any] = {}
    responses: dict[str, Any] = {}
    warnings: list[dict[str, str]] = []

    for step, unit in zip(enriched["steps"], enriched["units"]):
        entry = unit["entry"]
        action = unit["action"]
        signals = unit["signals"]
        action_shape = _safe_action_shape(
            action,
            action_index=unit["action_index"],
            ac=str(signals.get("ac") or ""),
        )
        requests[step["request_ref"]] = {
            "method": entry.get("method", ""),
            "path": entry.get("path", ""),
            "url_shape": entry.get("url_shape", ""),
            "headers": entry.get("request", {}).get("headers", {}),
            "query": entry.get("request", {}).get("query", {}),
            "body": entry.get("request", {}).get("body_params", {}),
            "form_id": signals.get("form_id", ""),
            "app_id": signals.get("app_id", ""),
            "ac": signals.get("ac", ""),
            "invoke_method": signals.get("method", ""),
            "action": action_shape,
            "source_index": unit["source_index"],
            "action_index": unit["action_index"],
        }
        response = dict(entry.get("response") or {})
        semantic_signature = response.pop("semantic_signature", {})
        semantic_contract = _build_ir_response_contract(
            semantic_signature,
            step_id=step["id"],
            role=str(step.get("role") or ""),
            form_id=str(signals.get("form_id") or ""),
            ac=str(signals.get("ac") or ""),
            action=action_shape,
        )
        if semantic_contract:
            response["semantic_contract"] = semantic_contract
        responses[step["response_ref"]] = response

    if not normalized["entries"]:
        warnings.append({"code": "api_entries_missing", "message": "未识别到苍穹业务 API 请求。"})

    flow = {
        "meta": {
            "schema_version": SCHEMA_VERSION,
            "generator": "cosmic-replay-ir",
            "confidence_score": enriched["confidence_score"],
            "sources": ["HAR"] + (["PlaywrightContext"] if playwright_context else []),
        },
        "source_har": {
            "file_name": source_name,
            "entry_count": normalized["entry_count"],
            "api_entry_count": normalized["api_entry_count"],
            "action_count": len(enriched["steps"]),
            "redacted": True,
            "raw_har_committed": False,
        },
        "environment": {
            "env_id": (environment or {}).get("env_id", ""),
            "base_url": "${BASE_URL}",
            "auth": "${SESSION_COOKIE}",
        },
        "pages": enriched["pages"],
        "steps": enriched["steps"],
        "request": requests,
        "response": responses,
        "extractors": _build_extractors(enriched["steps"], responses),
        "variables": _build_variables(requests),
        "assertions": _build_assertions(enriched["steps"], responses),
        "dependencies": enriched["dependencies"],
        "sensitive_fields": normalized["sensitive_fields"],
        "confidence_score": enriched["confidence_score"],
        "warnings": warnings,
    }
    flow["interaction_contract"] = build_ir_interaction_contract(flow)
    ok, validation_warnings = validate_normalized_flow(flow)
    if not ok:
        flow["warnings"].extend({"code": "schema_warning", "message": item} for item in validation_warnings)
    return flow


def compact_flow_for_preview(flow: dict[str, Any]) -> dict[str, Any]:
    """Return a small value-safe IR summary for Web UI and evidence."""
    return {
        "schema_version": (flow.get("meta") or {}).get("schema_version", SCHEMA_VERSION),
        "confidence_score": flow.get("confidence_score", 0),
        "source_har": {
            "entry_count": (flow.get("source_har") or {}).get("entry_count", 0),
            "api_entry_count": (flow.get("source_har") or {}).get("api_entry_count", 0),
            "action_count": (flow.get("source_har") or {}).get("action_count", len(flow.get("steps") or [])),
            "redacted": True,
        },
        "step_count": len(flow.get("steps") or []),
        "page_count": len(flow.get("pages") or []),
        "sensitive_field_count": len(flow.get("sensitive_fields") or []),
        "warnings": flow.get("warnings") or [],
        "interaction_contract": flow.get("interaction_contract") or {},
        "steps": [
            {
                "id": step.get("id", ""),
                "role": step.get("role", ""),
                "source_index": step.get("source_index"),
                "action_index": step.get("action_index"),
                "page_ref": step.get("page_ref", ""),
                "confidence_score": step.get("confidence_score", 0),
            }
            for step in (flow.get("steps") or [])[:20]
        ],
        "pages": [
            {
                "form_id": page.get("form_id", ""),
                "app_id": page.get("app_id", ""),
                "pageid_type": page.get("pageid_type", ""),
                "expected_role": page.get("expected_role", ""),
                "source_index": page.get("source_index"),
                "action_index": page.get("action_index"),
                "confidence_score": page.get("confidence_score", 0),
            }
            for page in (flow.get("pages") or [])[:20]
        ],
    }


def validate_normalized_flow(flow: dict[str, Any]) -> tuple[bool, list[str]]:
    warnings: list[str] = []
    for key in ("meta", "source_har", "steps", "request", "response", "sensitive_fields"):
        if key not in flow:
            warnings.append(f"missing_{key}")
    if (flow.get("source_har") or {}).get("redacted") is not True:
        warnings.append("source_har_not_marked_redacted")
    return not warnings, warnings


def _build_extractors(steps: list[dict[str, Any]], responses: dict[str, Any]) -> list[dict[str, Any]]:
    extractors: list[dict[str, Any]] = []
    for step in steps:
        response = responses.get(step.get("response_ref", ""), {})
        if response.get("has_pageid"):
            extractors.append({
                "name": f"{step['id']}_page_id",
                "from": step.get("response_ref", ""),
                "type": "pageId",
                "target": "${PAGE_ID}",
                "source": "HAR.response",
                "confidence_score": step.get("confidence_score", 0),
            })
        if response.get("write_refs"):
            extractors.append({
                "name": f"{step['id']}_bill_id",
                "from": step.get("response_ref", ""),
                "type": "billId",
                "target": "${BILL_ID}",
                "source": "HAR.response",
                "confidence_score": step.get("confidence_score", 0),
            })
    return extractors


def _build_variables(requests: dict[str, Any]) -> list[dict[str, Any]]:
    variables: list[dict[str, Any]] = []
    seen: set[str] = set()
    for request in requests.values():
        body = request.get("body") or {}
        for key in ("number", "code", "name", "billno"):
            if key in body and key not in seen:
                seen.add(key)
                variables.append({
                    "name": f"test_{key}",
                    "field_key": key,
                    "value_template": "CRPLY_${rand:6}",
                    "source": "HAR.request",
                    "confidence_score": 0.7,
                })
    return variables


def _build_assertions(steps: list[dict[str, Any]], responses: dict[str, Any]) -> list[dict[str, Any]]:
    assertions: list[dict[str, Any]] = []
    for step in steps:
        if step.get("role") == "write":
            assertions.append({"type": "no_save_failure", "step_id": step["id"], "mode": "hard"})
            if not (responses.get(step.get("response_ref", ""), {}).get("write_refs")):
                assertions.append({
                    "type": "readback_by_business_key",
                    "step_id": step["id"],
                    "mode": "advisory",
                    "reason": "generic_common_search_not_form_specific",
                })
    return assertions


def _safe_action_shape(
    action: dict[str, Any],
    *,
    action_index: int,
    ac: str = "",
) -> dict[str, Any]:
    if not isinstance(action, dict) or not action:
        return {
            "index": action_index,
            "key": "",
            "method": "",
            "args_shape": [],
            "post_data_shape": [],
            "operation_kind": "none",
            "field_refs": [],
            "selector_interface": "",
        }
    method = str(action.get("methodName") or action.get("method") or "")
    key = str(action.get("key") or "")
    operation_kind = _action_operation_kind(
        ac=ac,
        method=method,
        key=key,
        args=action.get("args"),
    )
    return {
        "index": action_index,
        "key": key,
        "method": method,
        "args_shape": [_value_shape(value) for value in (action.get("args") or [])],
        "post_data_shape": [_value_shape(value) for value in (action.get("postData") or [])],
        "operation_kind": operation_kind,
        "field_refs": _safe_field_refs(
            action,
            fallback_key=key,
            operation_kind=operation_kind,
        ),
        "selector_interface": _selector_interface(operation_kind),
    }


def _value_shape(value: Any) -> str:
    if isinstance(value, dict):
        return "dict"
    if isinstance(value, list):
        return "list"
    if isinstance(value, bool):
        return "bool"
    if value is None:
        return "null"
    if isinstance(value, (int, float)):
        return "number"
    return "string"


def _action_operation_kind(
    *,
    ac: str,
    method: str,
    key: str = "",
    args: Any = None,
) -> str:
    write_kind = classify_write_operation(
        ac=ac,
        method=method,
        key=key,
        args=args,
    )
    if write_kind:
        return write_kind
    text = f"{ac} {method}".strip().lower()
    if "getlookuplist" in text:
        return "lookup_query"
    if "setitembyidfromclient" in text:
        return "lookup_selection"
    if "entryrowclick" in text:
        return "grid_selection"
    if "updatevalue" in text:
        return "field_update"
    return "action"


def _safe_field_refs(
    action: dict[str, Any],
    *,
    fallback_key: str,
    operation_kind: str,
) -> list[dict[str, Any]]:
    """Return field names and value shapes without persisting recorded values."""
    if operation_kind in {"grid_selection", "lookup_query"}:
        return [{
            "field_key": fallback_key,
            "row_index": None,
            "value_shape": "selector",
        }] if fallback_key else []
    refs: list[dict[str, Any]] = []
    seen: set[tuple[str, int | None]] = set()
    post_data = action.get("postData") or []
    entries = post_data[1] if isinstance(post_data, list) and len(post_data) >= 2 else []
    if isinstance(entries, list):
        for item in entries:
            if not isinstance(item, dict):
                continue
            field_key = str(item.get("k") or "").strip()
            if not field_key:
                continue
            raw_row = item.get("r")
            row_index = raw_row if isinstance(raw_row, int) and raw_row >= 0 else None
            dedupe_key = (field_key.lower(), row_index)
            if dedupe_key in seen:
                continue
            seen.add(dedupe_key)
            refs.append({
                "field_key": field_key,
                "row_index": row_index,
                "value_shape": _value_shape(item.get("v")),
            })
    if fallback_key and not refs:
        refs.append({
            "field_key": fallback_key,
            "row_index": None,
            "value_shape": "unknown",
        })
    return refs


def _selector_interface(operation_kind: str) -> str:
    return {
        "lookup_query": "getLookUpList",
        "lookup_selection": "getLookUpList/setItemByIdFromClient",
        "grid_selection": "loadData/commonSearch/entryRowClick",
        "field_update": "updateValue",
    }.get(operation_kind, "")


def _build_ir_response_contract(
    signature: Any,
    *,
    step_id: str,
    role: str,
    form_id: str,
    ac: str,
    action: dict[str, Any],
) -> dict[str, Any]:
    """Build a value-safe critical contract for an IR write anchor."""
    operation_kind = str(action.get("operation_kind") or "")
    if not is_write_operation_kind(operation_kind) and role != "write":
        return {}
    if is_meaningful_response_signature(signature):
        contract = specialize_response_signature(
            signature,
            {
                "id": step_id,
                "form_id": form_id,
                "ac": ac,
                "key": action.get("key", ""),
            },
            contract_level="critical",
            anchor_reason="ir_write_anchor",
        )
        contract["contract_strength"] = "semantic"
    else:
        contract = {
            "version": 2,
            "outcome": "not_failure",
            "contract_level": "critical",
            "anchor_reason": "ir_write_anchor",
            "contract_strength": "minimal",
        }
    contract["ir_write_kind"] = (
        operation_kind
        if is_write_operation_kind(operation_kind)
        else classify_write_operation(ac=ac, key=action.get("key"))
    )
    return contract
