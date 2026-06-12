"""Target-environment row selection contracts for modify/delete replays."""
from __future__ import annotations

import re
from typing import Any, Mapping

from .write_contract import classify_write_operation


SCHEMA_VERSION = "1.0"
IDENTITY_KEYS = {
    "id", "pk", "pkid", "pkvalue", "billid", "entryid", "entry_id",
    "version", "dataversion",
}
QUERY_ACS = {"loaddata", "commonsearch", "query", "getlookuplist"}


def build_target_data_selector_plan(case: Mapping[str, Any] | None) -> dict[str, Any]:
    """Describe how existing target-environment rows are selected and bound."""
    case = case if isinstance(case, Mapping) else {}
    steps = [item for item in (case.get("steps") or []) if isinstance(item, Mapping)]
    selectors: list[dict[str, Any]] = []

    for index, step in enumerate(steps):
        operation = classify_write_operation(
            ac=step.get("ac"),
            method=step.get("method"),
            key=step.get("key"),
            args=step.get("args"),
            step_id=step.get("id"),
        )
        explicit = step.get("target_data_selector")
        static_fields = sorted(_identity_fields(step))
        is_delete = operation == "write_delete"
        action_text = " ".join(
            str(step.get(key) or "").strip().lower()
            for key in ("ac", "method", "operation_mode", "record_mode")
        )
        is_modify = any(
            token in {"modify", "updatebill", "edit_existing", "update_existing"}
            for token in action_text.split()
        )
        existing_record_write = bool(
            step.get("requires_target_selector")
            or explicit
            or is_modify
        )
        if not (is_delete or existing_record_write):
            continue

        spec = explicit if isinstance(explicit, Mapping) else {}
        source_step_id = str(spec.get("source_step") or "")
        source_step = next(
            (
                candidate
                for candidate in steps[:index]
                if str(candidate.get("id") or "") == source_step_id
            ),
            None,
        )
        source_is_query = bool(source_step and _is_query_step(source_step))
        field_key = str(spec.get("field_key") or "")
        value_ref = str(spec.get("value") or spec.get("value_ref") or "")
        bindings = [
            dict(item)
            for item in (spec.get("bindings") or [])
            if isinstance(item, Mapping)
            and str(item.get("source_field") or "")
            and str(item.get("target_path") or "")
        ]
        reasons: list[str] = []
        if not spec:
            reasons.append("target_selector_missing")
        if spec and not source_is_query:
            reasons.append("selector_source_query_missing")
        if spec and not (field_key and value_ref):
            reasons.append("selector_business_key_missing")
        if spec and not bindings:
            reasons.append("selector_identity_bindings_missing")

        selectors.append({
            "step_id": str(step.get("id") or f"step_{index + 1}"),
            "order": index,
            "form_id": str(step.get("form_id") or ""),
            "operation_kind": operation or "existing_record_write",
            "status": "blocked" if reasons else "ready",
            "reasons": reasons,
            "source_step_id": source_step_id,
            "field_key": field_key,
            "value_ref": value_ref,
            "match_policy": str(spec.get("match_policy") or "exactly_one"),
            "bindings": bindings,
            "recorded_static_identity_fields": static_fields,
            "recorded_static_identity_risk": bool(static_fields),
            "failure_policy": "block_before_write",
            "readback_expectation": (
                "not_exists" if is_delete else "exists_with_updated_fields"
            ),
        })

    blocked = [item for item in selectors if item["status"] != "ready"]
    return {
        "schema_version": SCHEMA_VERSION,
        "status": "blocked" if blocked else "ready",
        "raw_values_included": False,
        "selectors": selectors,
        "summary": {
            "selector_count": len(selectors),
            "ready_count": len(selectors) - len(blocked),
            "blocked_count": len(blocked),
            "static_identity_risk_count": sum(
                1 for item in selectors if item["recorded_static_identity_risk"]
            ),
            "delete_selector_count": sum(
                1 for item in selectors if item["operation_kind"] == "write_delete"
            ),
        },
    }


def _is_query_step(step: Mapping[str, Any]) -> bool:
    return (
        str(step.get("ac") or "").strip().lower() in QUERY_ACS
        or str(step.get("method") or "").strip().lower() in QUERY_ACS
    )


def _identity_fields(value: Any, prefix: str = "") -> set[str]:
    found: set[str] = set()
    if isinstance(value, Mapping):
        for key, child in value.items():
            key_text = str(key)
            path = f"{prefix}.{key_text}" if prefix else key_text
            normalized = re.sub(r"[^a-z0-9_]", "", key_text.lower())
            if normalized in IDENTITY_KEYS and _looks_like_recorded_identity(child):
                found.add(path)
            found.update(_identity_fields(child, path))
    elif isinstance(value, list):
        for index, child in enumerate(value):
            found.update(_identity_fields(child, f"{prefix}.{index}" if prefix else str(index)))
    return found


def _looks_like_recorded_identity(value: Any) -> bool:
    if isinstance(value, bool) or value is None:
        return False
    text = str(value).strip()
    if not text or "${" in text:
        return False
    return bool(
        re.fullmatch(r"\d{8,}", text)
        or re.fullmatch(r"[0-9a-fA-F-]{24,}", text)
    )
