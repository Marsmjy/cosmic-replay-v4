"""Build and validate normalized_flow.json structures."""
from __future__ import annotations

from typing import Any

from .detector import enrich_entries
from .normalizer import normalize_har_entries

SCHEMA_VERSION = "0.1"


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

    for step, entry in zip(enriched["steps"], normalized["entries"]):
        requests[step["request_ref"]] = {
            "method": entry.get("method", ""),
            "path": entry.get("path", ""),
            "url_shape": entry.get("url_shape", ""),
            "headers": entry.get("request", {}).get("headers", {}),
            "query": entry.get("request", {}).get("query", {}),
            "body": entry.get("request", {}).get("body_params", {}),
            "form_id": entry.get("signals", {}).get("form_id", ""),
            "app_id": entry.get("signals", {}).get("app_id", ""),
            "ac": entry.get("signals", {}).get("ac", ""),
            "invoke_method": entry.get("signals", {}).get("method", ""),
        }
        responses[step["response_ref"]] = entry.get("response", {})

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
            "redacted": True,
        },
        "step_count": len(flow.get("steps") or []),
        "page_count": len(flow.get("pages") or []),
        "sensitive_field_count": len(flow.get("sensitive_fields") or []),
        "warnings": flow.get("warnings") or [],
        "steps": [
            {
                "id": step.get("id", ""),
                "role": step.get("role", ""),
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
