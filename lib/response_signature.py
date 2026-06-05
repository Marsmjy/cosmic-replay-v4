"""Semantic response signatures for recorded HAR and runtime replay.

The goal is not byte-for-byte response equality. Kingdee responses contain
dynamic pageIds, internal ids, timestamps, and localized display values. This
module extracts stable effects that matter for replay correctness: error or
success outcome, modal/navigation actions, and non-empty field-state callbacks
for business configuration fields.
"""
from __future__ import annotations

import json
from typing import Any

from .replay import has_error_action


_SUCCESS_KEYWORDS = (
    "成功", "已保存", "已提交", "已生效", "已审核", "已完成", "操作成功",
)

_REQUIRED_FIELD_KEYS = {
    "fieldconfig",
    "fieldextattrname",
    "fieldextattrtype",
    "fieldtype",
    "proptype",
    "refdisplayprop",
    "showbasedata",
    "baseDataNumber",
    "baseDataPropNumber",
}

_REQUIRED_ACTIONS = {
    "showForm",
    "showConfirm",
    "showFormValidMsg",
    "showErrMsg",
    "addVirtualTab",
}


def parse_response_text(text: str) -> Any:
    """Parse a HAR response body, returning None when it is not JSON."""
    if not text:
        return None
    try:
        return json.loads(text)
    except Exception:
        return None


def iter_action_commands(node: Any):
    """Yield action command dictionaries from nested response payloads."""
    if isinstance(node, dict):
        if "a" in node:
            yield node
        for child in node.values():
            if isinstance(child, (list, dict)):
                yield from iter_action_commands(child)
    elif isinstance(node, list):
        for item in node:
            yield from iter_action_commands(item)


def _value_shape(value: Any) -> str:
    if value is None:
        return "null"
    if isinstance(value, bool):
        return "bool"
    if isinstance(value, (int, float)):
        return "number"
    if isinstance(value, str):
        stripped = value.strip()
        if not stripped:
            return "empty_string"
        if stripped in {"{}", "[]", "null"}:
            return "empty_literal"
        if stripped.startswith("{") and stripped.endswith("}"):
            return "json_object_string"
        if stripped.startswith("[") and stripped.endswith("]"):
            return "json_array_string"
        return "string"
    if isinstance(value, list):
        return "array" if value else "empty_array"
    if isinstance(value, dict):
        return "object" if value else "empty_object"
    return type(value).__name__


def _is_non_empty(value: Any) -> bool:
    return _value_shape(value) not in {
        "null",
        "empty_string",
        "empty_literal",
        "empty_array",
        "empty_object",
    }


def _field_effect_key(effect: dict[str, Any]) -> tuple[str, str, int | None, bool]:
    row = effect.get("row")
    if not isinstance(row, int):
        row = None
    return (
        str(effect.get("control") or ""),
        str(effect.get("field") or ""),
        row,
        bool(effect.get("non_empty")),
    )


def _collect_field_effects(resp: Any) -> list[dict[str, Any]]:
    effects: list[dict[str, Any]] = []
    seen: set[tuple[str, str, int | None, bool]] = set()

    def add(control: str, field: str, row: Any, value: Any) -> None:
        if not field:
            return
        clean_row = row if isinstance(row, int) else None
        effect = {
            "control": str(control or ""),
            "field": str(field or ""),
            "row": clean_row,
            "non_empty": _is_non_empty(value),
            "value_shape": _value_shape(value),
        }
        key = _field_effect_key(effect)
        if key in seen:
            return
        seen.add(key)
        effects.append(effect)

    def walk(node: Any, control: str = "") -> None:
        if isinstance(node, dict):
            next_control = str(node.get("k") or control or "")
            fieldstates = node.get("fieldstates")
            if isinstance(fieldstates, list):
                for state in fieldstates:
                    if isinstance(state, dict):
                        add(next_control, str(state.get("k") or ""), state.get("r"), state.get("v"))
            for value in node.values():
                if isinstance(value, (list, dict)):
                    walk(value, next_control)
        elif isinstance(node, list):
            for item in node:
                walk(item, control)

    walk(resp)
    return effects


def _collect_success(resp: Any) -> bool:
    if isinstance(resp, dict):
        if resp.get("success") is True or resp.get("status") is True:
            return True
        text = " ".join(str(resp.get(k) or "") for k in ("msg", "message", "detail"))
        return any(keyword in text for keyword in _SUCCESS_KEYWORDS)

    for cmd in iter_action_commands(resp):
        action = str(cmd.get("a") or "")
        if action == "ShowNotificationMsg":
            for item in cmd.get("p") or []:
                if not isinstance(item, dict):
                    continue
                if item.get("type") == 0:
                    return True
                content = str(item.get("content") or "")
                if any(keyword in content for keyword in _SUCCESS_KEYWORDS):
                    return True
        if action == "showMessage":
            for item in cmd.get("p") or []:
                if not isinstance(item, dict):
                    continue
                text = " ".join(str(item.get(k) or "") for k in ("msg", "message", "detail"))
                if any(keyword in text for keyword in _SUCCESS_KEYWORDS):
                    return True
    return False


def _collect_actions(resp: Any) -> list[str]:
    actions: list[str] = []
    seen: set[str] = set()
    for cmd in iter_action_commands(resp):
        name = str(cmd.get("a") or "")
        if name and name not in seen:
            seen.add(name)
            actions.append(name)
    return actions


def _collect_required_actions(resp: Any, actions: list[str]) -> list[str]:
    required = {name for name in actions if name in _REQUIRED_ACTIONS}
    for cmd in iter_action_commands(resp):
        if str(cmd.get("a") or "") == "invokeMethod":
            method = str(cmd.get("methodname") or cmd.get("methodName") or "")
            if method == "addVirtualTab":
                required.add("addVirtualTab")
    return sorted(required)


def build_response_signature(resp: Any) -> dict[str, Any]:
    """Build a value-safe semantic signature from a runtime response."""
    errors = has_error_action(resp)
    success = _collect_success(resp)
    actions = _collect_actions(resp)
    field_effects = _collect_field_effects(resp)
    required_field_effects = [
        {
            "control": effect["control"],
            "field": effect["field"],
            "row": effect["row"],
            "non_empty": effect["non_empty"],
            "value_shape": effect["value_shape"],
        }
        for effect in field_effects
        if effect["non_empty"] and str(effect["field"]) in _REQUIRED_FIELD_KEYS
    ]
    outcome = "failure" if errors else "success" if success else "neutral"
    signature: dict[str, Any] = {
        "version": 1,
        "outcome": outcome,
        "actions": actions,
        "success": success,
        "error": bool(errors),
    }
    required_actions = _collect_required_actions(resp, actions)
    if required_actions:
        signature["required_actions"] = required_actions
    if required_field_effects:
        signature["required_field_effects"] = required_field_effects
    return signature


def build_response_signature_from_text(text: str) -> dict[str, Any]:
    resp = parse_response_text(text)
    if resp is None:
        return {}
    return build_response_signature(resp)


def is_meaningful_response_signature(signature: dict[str, Any] | None) -> bool:
    if not isinstance(signature, dict) or not signature:
        return False
    if signature.get("outcome") in {"success", "failure"}:
        return True
    if signature.get("required_actions"):
        return True
    if signature.get("required_field_effects"):
        return True
    return False


def is_meaningful_response_text(text: str) -> bool:
    return is_meaningful_response_signature(build_response_signature_from_text(text))


def summarize_response_signature(signature: Any) -> dict[str, Any]:
    """Return a compact value-safe summary for UI and evidence packages."""
    if not is_meaningful_response_signature(signature):
        return {}
    outcome = str(signature.get("outcome") or "neutral")
    required_actions = [str(action) for action in (signature.get("required_actions") or []) if action]
    field_effects = [
        effect for effect in (signature.get("required_field_effects") or [])
        if isinstance(effect, dict)
    ]
    parts: list[str] = []
    if outcome == "success":
        parts.append("期望成功响应")
    elif outcome == "failure":
        parts.append("期望业务校验")
    if required_actions:
        parts.append("期望动作 " + "/".join(required_actions[:3]))
    if field_effects:
        parts.append(f"期望字段回填 {len(field_effects)} 项")
    return {
        "outcome": outcome,
        "required_action_count": len(required_actions),
        "required_actions": required_actions[:8],
        "required_field_effect_count": len(field_effects),
        "required_field_keys": [
            str(effect.get("field") or "")
            for effect in field_effects[:8]
            if effect.get("field")
        ],
        "label": "；".join(parts) or "响应语义锚点",
    }


def _find_matching_field_effect(
    expected: dict[str, Any],
    actual_effects: list[dict[str, Any]],
) -> dict[str, Any] | None:
    exp_control = str(expected.get("control") or "")
    exp_field = str(expected.get("field") or "")
    exp_row = expected.get("row")
    for effect in actual_effects:
        if exp_control and str(effect.get("control") or "") != exp_control:
            continue
        if exp_field and str(effect.get("field") or "") != exp_field:
            continue
        if isinstance(exp_row, int) and effect.get("row") != exp_row:
            continue
        return effect
    return None


def compare_response_signature(expected: Any, actual_resp: Any) -> list[str]:
    """Return semantic mismatch messages between recorded and runtime response."""
    if not isinstance(expected, dict) or not expected:
        return []
    actual = build_response_signature(actual_resp)
    errors: list[str] = []

    expected_outcome = str(expected.get("outcome") or "")
    actual_outcome = str(actual.get("outcome") or "")
    if expected_outcome == "success" and actual_outcome == "failure":
        errors.append("[ResponseSemantic] recorded success but runtime returned business error")
    elif expected_outcome == "failure" and actual_outcome != "failure":
        errors.append("[ResponseSemantic] recorded business validation was not reproduced")

    actual_actions = set(actual.get("actions") or [])
    for action in expected.get("required_actions") or []:
        if str(action) not in actual_actions:
            errors.append(f"[ResponseSemantic] missing recorded action {action}")

    actual_effects = actual.get("required_field_effects") or []
    for effect in expected.get("required_field_effects") or []:
        if not isinstance(effect, dict):
            continue
        actual_effect = _find_matching_field_effect(effect, actual_effects)
        if actual_effect is None:
            field = effect.get("field") or "?"
            row = effect.get("row")
            row_text = f" row={row}" if isinstance(row, int) else ""
            errors.append(f"[ResponseSemantic] missing non-empty callback field {field}{row_text}")
            continue
        if effect.get("non_empty") and not actual_effect.get("non_empty"):
            field = effect.get("field") or "?"
            row = effect.get("row")
            row_text = f" row={row}" if isinstance(row, int) else ""
            errors.append(f"[ResponseSemantic] callback field {field}{row_text} became empty")

    return errors
