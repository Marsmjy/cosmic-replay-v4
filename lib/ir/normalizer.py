"""Normalize HAR entries into value-safe API entry records."""
from __future__ import annotations

import json
from typing import Any
from urllib.parse import parse_qs, urlparse

from .sanitizer import redact_value, sanitize_headers
from lib.pageid_trace import classify_pageid, pageid_fragment
from lib.response_signature import build_response_signature_from_text


def normalize_har_entries(har: dict[str, Any]) -> dict[str, Any]:
    """Extract value-safe normalized entries from a HAR object."""
    entries = ((har or {}).get("log") or {}).get("entries") or []
    normalized: list[dict[str, Any]] = []
    sensitive_fields: list[dict[str, str]] = []
    skipped = 0
    for index, entry in enumerate(entries):
        item = normalize_entry(entry, index=index)
        sensitive_fields.extend(item.pop("sensitive_fields", []))
        if item.get("is_api_entry"):
            normalized.append(item)
        else:
            skipped += 1
    return {
        "entries": normalized,
        "entry_count": len(entries),
        "api_entry_count": len(normalized),
        "skipped_entry_count": skipped,
        "sensitive_fields": sensitive_fields,
    }


def normalize_entry(entry: dict[str, Any], *, index: int) -> dict[str, Any]:
    req = (entry or {}).get("request") or {}
    resp = (entry or {}).get("response") or {}
    url = str(req.get("url") or "")
    parsed = urlparse(url)
    query = parse_qs(parsed.query, keep_blank_values=True)
    post = req.get("postData") or {}
    post_text = str(post.get("text") or "")
    body_params = parse_qs(post_text, keep_blank_values=True) if post_text else {}
    body_json = _parse_json_body(post_text)
    headers, header_redactions = sanitize_headers(req.get("headers") or [])

    page_id = _first(body_params.get("pageId")) or _first(query.get("pageId"))
    form_id = _first(query.get("f")) or _first(body_params.get("f"))
    app_id = _first(query.get("appId")) or _first(body_params.get("appId"))
    ac = _first(query.get("ac")) or _first(body_params.get("ac"))
    invoke_method = _extract_invoke_method(body_params, body_json)
    actions = _extract_actions(body_params, body_json)
    response_features = _response_features(resp)

    redacted_query, query_redactions = _redact_mapping(query, prefix=f"entries[{index}].request.query")
    redacted_body, body_redactions = _redact_mapping(body_params, prefix=f"entries[{index}].request.body")

    return {
        "index": index,
        "is_api_entry": _is_api_url(parsed.path),
        "method": str(req.get("method") or "GET").upper(),
        "path": parsed.path,
        "url_shape": _url_shape(parsed.path),
        "request": {
            "headers": headers,
            "query": redacted_query,
            "body_params": redacted_body,
            "mime_type": post.get("mimeType", ""),
        },
        "signals": {
            "form_id": form_id,
            "app_id": app_id,
            "ac": ac,
            "method": invoke_method,
            "pageid_type": classify_pageid(page_id),
            "pageid_fragment": pageid_fragment(page_id),
            "has_pageid": bool(page_id),
        },
        "actions": actions,
        "response": response_features,
        "sensitive_fields": header_redactions + query_redactions + body_redactions,
    }


def _is_api_url(path: str) -> bool:
    return "/form/" in path or "/metadata/" in path or path.endswith("/batchInvokeAction.do")


def _url_shape(path: str) -> str:
    if path.endswith("/batchInvokeAction.do"):
        return "/form/batchInvokeAction.do"
    if path.endswith("/getEntityType.do"):
        return "/metadata/getEntityType.do"
    return path


def _redact_mapping(values: dict[str, list[str]], *, prefix: str) -> tuple[dict[str, Any], list[dict[str, str]]]:
    result: dict[str, Any] = {}
    redactions: list[dict[str, str]] = []
    for key, raw_values in (values or {}).items():
        if str(key).lower() in {"actions", "params"}:
            result[key] = "${ACTION_PAYLOAD}"
            redactions.append({
                "location": f"{prefix}.{key}",
                "type": "action_payload",
                "replacement": "${ACTION_PAYLOAD}",
            })
            continue
        out_values = []
        for idx, value in enumerate(raw_values or [""]):
            redacted, record = redact_value(key, value, location=f"{prefix}.{key}[{idx}]")
            if record:
                redactions.append(record)
            out_values.append(redacted)
        result[key] = out_values[0] if len(out_values) == 1 else out_values
    return result, redactions


def _parse_json_body(text: str) -> Any:
    if not text:
        return None
    stripped = text.strip()
    if not stripped or stripped[0] not in "[{":
        return None
    try:
        return json.loads(stripped)
    except Exception:
        return None


def _extract_actions(body_params: dict[str, list[str]], body_json: Any) -> list[Any]:
    for key in ("actions", "params"):
        raw = _first(body_params.get(key))
        if not raw:
            continue
        try:
            value = json.loads(raw)
            return value if isinstance(value, list) else [value]
        except Exception:
            continue
    if isinstance(body_json, dict):
        for key in ("actions", "params"):
            actions = body_json.get(key)
            if isinstance(actions, list):
                return actions
    return []


def _extract_invoke_method(body_params: dict[str, list[str]], body_json: Any) -> str:
    actions = _extract_actions(body_params, body_json)
    for action in actions:
        if isinstance(action, dict):
            return str(action.get("methodName") or action.get("method") or "")
    return ""


def _response_features(resp: dict[str, Any]) -> dict[str, Any]:
    content = (resp or {}).get("content") or {}
    text = str(content.get("text") or "")
    features = {
        "status": (resp or {}).get("status"),
        "content_size": len(text),
        "has_pageid": False,
        "pageid_type": "missing",
        "success_signals": [],
        "write_refs": [],
    }
    data = _parse_json_body(text)
    semantic_signature = build_response_signature_from_text(
        text,
        include_candidates=True,
    )
    page_id = _find_key(data, "pageId")
    if page_id:
        features["has_pageid"] = True
        features["pageid_type"] = classify_pageid(str(page_id))
    if "保存成功" in text or "success" in text.lower():
        features["success_signals"].append("save_success_message")
    if _find_key(data, "pkValue") or _find_key(data, "billId") or _find_key(data, "fid"):
        features["write_refs"].append("${BILL_ID}")
    if semantic_signature:
        features["semantic_signature"] = semantic_signature
    return features


def _find_key(node: Any, key: str) -> Any:
    if isinstance(node, dict):
        if key in node:
            return node[key]
        for value in node.values():
            found = _find_key(value, key)
            if found not in (None, ""):
                return found
    elif isinstance(node, list):
        for item in node:
            found = _find_key(item, key)
            if found not in (None, ""):
                return found
    return None


def _first(values: list[str] | None) -> str:
    return str(values[0]) if values else ""
