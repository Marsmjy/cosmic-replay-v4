"""Generate a conservative cosmic YAML draft from normalized flow IR."""
from __future__ import annotations

from typing import Any

import yaml


def generate_yaml_case_from_ir(flow: dict[str, Any], *, case_name: str = "") -> str:
    case = {
        "name": case_name or "IR 生成用例",
        "tags": ["har_import", "ir_generated"],
        "env": {
            "base_url": "${env:COSMIC_BASE_URL}",
            "username": "${env:COSMIC_USERNAME}",
            "password": "${env:COSMIC_PASSWORD}",
            "datacenter_id": "${env:COSMIC_DATACENTER_ID}",
        },
        "setup": {
            "login": "configured",
            "dry_run_supported": True,
        },
        "vars": {
            item["name"]: item.get("value_template", "CRPLY_${rand:6}")
            for item in flow.get("variables", [])
            if item.get("name")
        },
        "steps": [_yaml_step_from_ir(step, flow) for step in flow.get("steps", [])],
        "assertions": flow.get("assertions", []),
        "evidence": {
            "include": ["pageid_trace", "resolved_request", "response_summary"],
            "redaction": {
                "headers": ["Cookie", "Authorization", "Set-Cookie", "kd-csrf-token"],
                "placeholders": ["${ACCESS_TOKEN}", "${SESSION_COOKIE}", "${PAGE_ID}", "${BILL_ID}"],
            },
        },
    }
    return yaml.safe_dump(case, allow_unicode=True, sort_keys=False)


def _yaml_step_from_ir(step: dict[str, Any], flow: dict[str, Any]) -> dict[str, Any]:
    request = (flow.get("request") or {}).get(step.get("request_ref", ""), {})
    page = _page_by_ref(flow, step.get("page_ref", ""))
    item = {
        "id": step.get("id", ""),
        "type": "invoke",
        "form_id": request.get("form_id", ""),
        "app_id": request.get("app_id", ""),
        "ac": request.get("ac", ""),
        "method": request.get("invoke_method", ""),
        "request": {
            "path": request.get("url_shape", request.get("path", "")),
            "body_ref": step.get("request_ref", ""),
        },
        "pageId": {
            "role": page.get("expected_role", ""),
            "source": page.get("source", "HAR"),
            "value": "${PAGE_ID}" if page.get("pageid") else "",
            "confidence_score": page.get("confidence_score", 0),
        },
        "retry": {
            "max_attempts": 2,
            "on": ["page_expired", "cache_client_error"],
        },
    }
    if step.get("role") == "write":
        item["cleanup"] = {"invalidate_pages": [request.get("form_id", "")]}
    return item


def _page_by_ref(flow: dict[str, Any], ref: str) -> dict[str, Any]:
    for page in flow.get("pages", []):
        if page.get("id") == ref:
            return page
    return {}
