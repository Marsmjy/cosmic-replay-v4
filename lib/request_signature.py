"""Value-safe semantic request contracts for HAR replay.

Recorded requests contain dynamic pageIds, tokens, internal ids and user
maintained values.  Contracts therefore compare request intent and structure:
target form, action family, controls, field keys, filter keys and entry shape.
"""
from __future__ import annotations

from typing import Any


_WRITE_ACTIONS = {
    "save",
    "submit",
    "saveandeffect",
    "submitandeffect",
    "saveandaudit",
    "doconfirm",
    "afterconfirm",
    "startupflow",
}


def _value_shape(value: Any) -> str:
    if value is None:
        return "null"
    if isinstance(value, bool):
        return "bool"
    if isinstance(value, (int, float, str)):
        # User-maintained values and Kingdee enums commonly move between JSON
        # numbers and numeric strings during normalization. The contract cares
        # that the field is still a scalar, not which representation was used.
        return "scalar"
    if isinstance(value, list):
        return "array"
    if isinstance(value, dict):
        return "object"
    return type(value).__name__


def _action_family(step: dict[str, Any]) -> str:
    ac = str(step.get("ac") or "").strip().lower()
    method = str(step.get("method") or "").strip().lower()
    key = str(step.get("key") or "").strip().lower()
    if ac in _WRITE_ACTIONS:
        return ac
    if key in {"btnsave", "btn_save", "bar_save", "barsave"}:
        return "save"
    if key in {"bar_submit", "barsubmit", "btnsubmit", "btn_submit"}:
        return "submit"
    if key in {"btnok", "btn_ok", "btn_confirm", "btnconfirm"}:
        return "confirm"
    if method in {"setitembyidfromclient", "setitemvaluebyidfromclient"}:
        return "pick"
    if ac in {"commonsearch", "query", "getlookuplist"} or method in {
        "commonsearch",
        "query",
        "getlookuplist",
    }:
        return "query"
    return ac or method


def _collect_post_field_shapes(post_data: Any) -> dict[str, str]:
    shapes: dict[str, str] = {}
    if not isinstance(post_data, list) or len(post_data) < 2:
        return shapes
    entries = post_data[1]
    if not isinstance(entries, list):
        return shapes
    for entry in entries:
        if not isinstance(entry, dict):
            continue
        key = str(entry.get("k") or "").strip()
        if key:
            shapes[key] = _value_shape(entry.get("v"))
    return shapes


def _collect_filter_fields(node: Any) -> list[str]:
    fields: set[str] = set()

    def walk(value: Any) -> None:
        if isinstance(value, dict):
            names = value.get("FieldName")
            if isinstance(names, list):
                fields.update(str(item) for item in names if item)
            elif names:
                fields.add(str(names))
            for child in value.values():
                walk(child)
        elif isinstance(value, list):
            for child in value:
                walk(child)

    walk(node)
    return sorted(fields)


def _collect_control_keys(post_data: Any) -> list[str]:
    if not isinstance(post_data, list) or not post_data:
        return []
    root = post_data[0]
    if not isinstance(root, dict):
        return []
    return sorted(str(key) for key in root if key)


def _selector_row_width(args: Any) -> int:
    if not isinstance(args, list):
        return 0

    def walk(value: Any) -> int:
        if isinstance(value, dict):
            rows = value.get("selDatas")
            if isinstance(rows, list) and rows:
                row = rows[0]
                if isinstance(row, list):
                    return len(row)
            for child in value.values():
                width = walk(child)
                if width:
                    return width
        elif isinstance(value, list):
            for child in value:
                width = walk(child)
                if width:
                    return width
        return 0

    return walk(args)


def build_request_signature(
    step: dict[str, Any],
    *,
    contract_level: str = "",
    anchor_reason: str = "",
) -> dict[str, Any]:
    """Build a compact request contract without retaining business values."""
    signature: dict[str, Any] = {
        "contract_level": contract_level,
        "anchor_reason": anchor_reason,
        "type": str(step.get("type") or ""),
        "form_id": str(step.get("form_id") or ""),
        "app_id": str(step.get("app_id") or ""),
    }
    stype = signature["type"]
    if stype in {"invoke", "wait_until"}:
        signature.update({
            "action_family": _action_family(step),
            "ac": str(step.get("ac") or ""),
            "key": str(step.get("key") or ""),
            "method": str(step.get("method") or ""),
            "post_field_shapes": _collect_post_field_shapes(step.get("post_data")),
            "control_keys": _collect_control_keys(step.get("post_data")),
            "filter_fields": _collect_filter_fields(step.get("args")),
        })
        row_width = _selector_row_width(step.get("args"))
        if row_width:
            signature["selector_row_width"] = row_width
    elif stype == "update_fields":
        signature["field_shapes"] = {
            str(key): _value_shape(value)
            for key, value in (step.get("fields") or {}).items()
        }
    elif stype == "pick_basedata":
        signature.update({
            "field_key": str(step.get("field_key") or ""),
            "row_index": step.get("row_index") if isinstance(step.get("row_index"), int) else None,
            "has_value_id": step.get("value_id") not in (None, ""),
        })
    elif stype == "select_f7_list_row":
        signature.update({
            "grid_key": str(step.get("grid_key") or "billlistap"),
            "field_key": str(step.get("field_key") or "name"),
            "target_field_key": str(step.get("target_field_key") or ""),
            "confirm_key": str(step.get("confirm_key") or "btnok"),
        })
    return {
        key: value
        for key, value in signature.items()
        if value not in ("", None, [], {})
    }


def evaluate_request_contract(
    expected: Any,
    actual_step: dict[str, Any],
) -> dict[str, Any]:
    """Evaluate stable request structure using strict/advisory semantics."""
    if not isinstance(expected, dict) or not expected:
        return {"contract_level": "", "errors": [], "warnings": []}
    actual = build_request_signature(actual_step)
    mismatches: list[str] = []

    def add(message: str) -> None:
        mismatches.append(f"[RequestSemantic] {message}")

    for key in ("type", "form_id", "app_id", "action_family", "key", "method"):
        expected_value = expected.get(key)
        if expected_value not in (None, "") and actual.get(key) != expected_value:
            add(f"{key} changed from {expected_value} to {actual.get(key) or '<missing>'}")

    for key in ("field_shapes", "post_field_shapes"):
        expected_shapes = expected.get(key) or {}
        actual_shapes = actual.get(key) or {}
        for field, shape in expected_shapes.items():
            if field not in actual_shapes:
                add(f"missing recorded field {field}")
            elif actual_shapes[field] != shape:
                add(f"field {field} shape changed from {shape} to {actual_shapes[field]}")

    for key, label in (("control_keys", "control"), ("filter_fields", "filter field")):
        actual_values = set(actual.get(key) or [])
        for value in expected.get(key) or []:
            if value not in actual_values:
                add(f"missing recorded {label} {value}")

    for key in ("field_key", "row_index", "grid_key", "target_field_key", "confirm_key"):
        expected_value = expected.get(key)
        if expected_value not in (None, "") and actual.get(key) != expected_value:
            add(f"{key} changed from {expected_value} to {actual.get(key) or '<missing>'}")

    expected_width = int(expected.get("selector_row_width") or 0)
    actual_width = int(actual.get("selector_row_width") or 0)
    if expected_width and actual_width < expected_width:
        add(f"selector row width shrank from {expected_width} to {actual_width}")
    if expected.get("has_value_id") and not actual.get("has_value_id"):
        add("basedata selection lost its value id")

    level = str(expected.get("contract_level") or "critical")
    return {
        "contract_level": level,
        "errors": [] if level == "advisory" else mismatches,
        "warnings": mismatches if level == "advisory" else [],
    }
