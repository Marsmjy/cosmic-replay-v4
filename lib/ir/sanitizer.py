"""Value-safe sanitization utilities for HAR-derived IR.

These helpers never need network access and are safe to use in preview,
tests and evidence generation.  They preserve structure while replacing
credentials, session headers and page identifiers with placeholders.
"""
from __future__ import annotations

import copy
import re
from typing import Any


PLACEHOLDERS = {
    "authorization": "${ACCESS_TOKEN}",
    "cookie": "${SESSION_COOKIE}",
    "set-cookie": "${SESSION_COOKIE}",
    "password": "${PASSWORD}",
    "csrf": "${CSRF_TOKEN}",
    "token": "${ACCESS_TOKEN}",
    "pageid": "${PAGE_ID}",
    "page_id": "${PAGE_ID}",
    "billid": "${BILL_ID}",
    "bill_id": "${BILL_ID}",
}

SENSITIVE_HEADER_NAMES = {
    "authorization",
    "cookie",
    "set-cookie",
    "kd-csrf-token",
    "x-csrf-token",
    "x-xsrf-token",
}

SENSITIVE_KEY_RE = re.compile(
    r"(authorization|set-cookie|cookie|password|passwd|pwd|token|csrf|pageid|page_id|billid|bill_id)",
    re.IGNORECASE,
)

RAW_SECRET_RE = re.compile(
    r"(Authorization\s*:|Set-Cookie\s*:|Cookie\s*:|kd-csrf-token\s*:|Bearer\s+[A-Za-z0-9._~+/=-]+)",
    re.IGNORECASE,
)

PAGEID_RE = re.compile(
    r"(?<![A-Za-z0-9])(?:root[0-9a-f]{32}|\d+root[0-9a-f]{32}|[0-9a-f]{32})(?![A-Za-z0-9])",
    re.IGNORECASE,
)


def sanitize_har(har: dict[str, Any]) -> tuple[dict[str, Any], list[dict[str, str]]]:
    """Return a redacted deep copy and a list of redaction records."""
    redactions: list[dict[str, str]] = []
    sanitized = _sanitize_node(copy.deepcopy(har), path="$", redactions=redactions)
    return sanitized, redactions


def sanitize_headers(headers: list[dict[str, Any]] | dict[str, Any]) -> tuple[dict[str, str], list[dict[str, str]]]:
    """Normalize and redact HAR headers."""
    result: dict[str, str] = {}
    redactions: list[dict[str, str]] = []
    items = headers.items() if isinstance(headers, dict) else [
        (item.get("name", ""), item.get("value", ""))
        for item in (headers or [])
        if isinstance(item, dict)
    ]
    for raw_name, raw_value in items:
        name = str(raw_name or "")
        if not name:
            continue
        lowered = name.lower()
        if lowered in SENSITIVE_HEADER_NAMES:
            result[name] = _placeholder_for(lowered)
            redactions.append({"location": f"headers.{name}", "type": lowered, "replacement": result[name]})
        else:
            result[name] = _redact_inline_pageids(str(raw_value or ""))
    return result, redactions


def redact_value(key: str, value: Any, *, location: str = "") -> tuple[Any, dict[str, str] | None]:
    """Redact one value when its key or content is sensitive."""
    key_lower = str(key or "").lower()
    if SENSITIVE_KEY_RE.search(key_lower):
        replacement = _placeholder_for(key_lower)
        return replacement, {"location": location or key, "type": key_lower, "replacement": replacement}
    if isinstance(value, str):
        redacted = _redact_inline_pageids(value)
        if redacted != value:
            return redacted, {"location": location or key, "type": "pageid", "replacement": "${PAGE_ID}"}
    return value, None


def scan_sensitive_text(text: str) -> list[str]:
    """Return risk codes found in generated text."""
    risks: list[str] = []
    safe_text = re.sub(
        r"(?im)^\s*(Authorization|Set-Cookie|Cookie|kd-csrf-token)\s*:\s*\$\{[A-Z_]+}\s*$",
        "",
        text or "",
    )
    if RAW_SECRET_RE.search(safe_text):
        risks.append("raw_secret_header")
    if PAGEID_RE.search(safe_text):
        risks.append("raw_pageid")
    return sorted(set(risks))


def _sanitize_node(node: Any, *, path: str, redactions: list[dict[str, str]]) -> Any:
    if isinstance(node, dict):
        if "name" in node and "value" in node:
            header_name = str(node.get("name") or "")
            lowered = header_name.lower()
            if lowered in SENSITIVE_HEADER_NAMES:
                replacement = _placeholder_for(lowered)
                redactions.append({"location": f"{path}.value", "type": lowered, "replacement": replacement})
                node = dict(node)
                node["value"] = replacement
        result = {}
        for key, value in node.items():
            child_path = f"{path}.{key}"
            redacted, record = redact_value(str(key), value, location=child_path)
            if record:
                redactions.append(record)
                result[key] = redacted
            else:
                result[key] = _sanitize_node(value, path=child_path, redactions=redactions)
        return result
    if isinstance(node, list):
        return [_sanitize_node(item, path=f"{path}[{idx}]", redactions=redactions) for idx, item in enumerate(node)]
    if isinstance(node, str):
        return _redact_inline_pageids(node)
    return node


def _placeholder_for(key: str) -> str:
    lowered = str(key or "").lower()
    for needle, placeholder in PLACEHOLDERS.items():
        if needle in lowered:
            return placeholder
    return "${REDACTED}"


def _redact_inline_pageids(value: str) -> str:
    return PAGEID_RE.sub("${PAGE_ID}", value)
