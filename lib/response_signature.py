"""Value-safe semantic response contracts for HAR replay.

The comparison is intentionally not byte-for-byte. Kingdee responses contain
dynamic pageIds, ids, timestamps, bill numbers, and environment-specific row
values. Contracts retain stable business effects instead: outcome category,
important actions, target forms, callback field shape, and lookup/list schema.
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

_STRUCTURAL_ACTIONS = {
    "ShowNotificationMsg",
    "closeWindow",
    "showConfirm",
    "showErrMsg",
    "showForm",
    "showFormValidMsg",
}

_STABLE_FIELD_KEYS = {
    "billid",
    "billno",
    "billstatus",
    "fieldconfig",
    "fieldextattrname",
    "fieldextattrtype",
    "fieldtype",
    "id",
    "name",
    "number",
    "proptype",
    "refdisplayprop",
    "showbasedata",
}

_IDENTITY_COLUMN_HINTS = (
    "id",
    "number",
    "code",
    "name",
    "billno",
    "billstatus",
    "status",
)


def _action_semantic(name: Any) -> str:
    action = str(name or "")
    if action in {"closeBrowserPage", "closeWindow"}:
        return "closePage"
    return action


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


def _collect_show_forms(resp: Any) -> list[str]:
    forms: list[str] = []
    for cmd in iter_action_commands(resp):
        if str(cmd.get("a") or "") != "showForm":
            continue
        for item in cmd.get("p") or []:
            if not isinstance(item, dict):
                continue
            for key in ("formId", "billFormId"):
                form_id = str(item.get(key) or "").strip()
                if form_id and form_id not in forms:
                    forms.append(form_id)
    return forms


def _collect_grid_schemas(resp: Any) -> list[dict[str, Any]]:
    schemas: list[dict[str, Any]] = []
    positions: dict[tuple[str, tuple[str, ...]], int] = {}

    def walk(node: Any, control: str = "") -> None:
        if isinstance(node, list):
            for item in node:
                walk(item, control)
            return
        if not isinstance(node, dict):
            return
        next_control = str(node.get("k") or control or "")
        data = node.get("data")
        if isinstance(data, dict):
            dataindex = data.get("dataindex")
            rows = data.get("rows")
            if isinstance(dataindex, dict) and isinstance(rows, list):
                columns = tuple(sorted(str(key) for key in dataindex if str(key)))
                dedupe_key = (next_control, columns)
                if columns and dedupe_key not in positions:
                    positions[dedupe_key] = len(schemas)
                    schemas.append({
                        "control": next_control,
                        "columns": list(columns),
                        "non_empty": bool(rows),
                    })
                elif columns and rows:
                    schemas[positions[dedupe_key]]["non_empty"] = True
        for value in node.values():
            if isinstance(value, (dict, list)):
                walk(value, next_control)

    walk(resp)
    return schemas


def _message_categories(text: str) -> list[str]:
    value = str(text or "").lower()
    categories: list[str] = []

    def add(category: str) -> None:
        if category not in categories:
            categories.append(category)

    if any(token in value for token in ("无效请求", "invalid request")):
        add("invalid_request")
    if any(token in value for token in ("必填", "不能为空", "请选择", "请填写", "required")):
        add("required_field")
    if any(token in value for token in ("无权限", "没有权限", "permission", "forbidden")):
        add("permission")
    if any(token in value for token in ("页面未初始化", "已经过期", "csrf", "token")):
        add("session_or_page_expired")
    if any(token in value for token in ("exception", "traceid", "nullpointer", "运行时异常")):
        add("backend_exception")
    if any(token in value for token in ("驳回", "不同意", "reject")):
        add("reject")
    if any(token in value for token in ("审核", "审批", "同意", "approve")):
        add("audit")
    if any(token in value for token in ("提交", "submit")):
        add("submit")
    if any(token in value for token in ("保存", "save")):
        add("save")
    if any(token in value for token in ("生效", "effect")):
        add("effect")
    if any(token in value for token in ("确认", "confirm")):
        add("confirm")
    return categories


def _collect_notification_categories(resp: Any) -> tuple[list[str], list[str]]:
    success_categories: list[str] = []
    failure_categories: list[str] = []

    def extend(target: list[str], values: list[str]) -> None:
        for value in values:
            if value not in target:
                target.append(value)

    for cmd in iter_action_commands(resp):
        action = str(cmd.get("a") or "")
        if action == "ShowNotificationMsg":
            for item in cmd.get("p") or []:
                if not isinstance(item, dict):
                    continue
                categories = _message_categories(str(item.get("content") or ""))
                if item.get("type") == 0:
                    extend(success_categories, categories)
                else:
                    extend(failure_categories, categories)
        elif action == "showMessage":
            for item in cmd.get("p") or []:
                if not isinstance(item, dict):
                    continue
                text = " ".join(
                    str(item.get(key) or "")
                    for key in ("msg", "message", "detail", "content")
                )
                categories = _message_categories(text)
                if item.get("messageType") in (-1, 1, "error"):
                    extend(failure_categories, categories)
                else:
                    extend(success_categories, categories)

    if isinstance(resp, dict):
        text = " ".join(str(resp.get(key) or "") for key in ("msg", "message", "detail"))
        categories = _message_categories(text)
        if has_error_action(resp):
            extend(failure_categories, categories)
        elif resp.get("success") is True or resp.get("status") is True:
            extend(success_categories, categories)
    return success_categories, failure_categories


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


def build_response_signature(
    resp: Any,
    *,
    include_candidates: bool = False,
) -> dict[str, Any]:
    """Build a value-safe semantic signature from a runtime response."""
    errors = has_error_action(resp)
    success = _collect_success(resp)
    actions = _collect_actions(resp)
    field_effects = _collect_field_effects(resp)
    show_forms = _collect_show_forms(resp)
    grid_schemas = _collect_grid_schemas(resp)
    success_categories, failure_categories = _collect_notification_categories(resp)
    required_field_effects = [
        {
            "control": effect["control"],
            "field": effect["field"],
            "row": effect["row"],
            "non_empty": effect["non_empty"],
            "value_shape": effect["value_shape"],
        }
        for effect in field_effects
        if effect["non_empty"]
        and (
            str(effect["field"]) in _REQUIRED_FIELD_KEYS
            or str(effect["field"]).lower() in _STABLE_FIELD_KEYS
        )
    ]
    outcome = "failure" if errors else "success" if success else "neutral"
    signature: dict[str, Any] = {
        "version": 2,
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
    if show_forms:
        signature["show_forms"] = show_forms
    if grid_schemas:
        signature["grid_schemas"] = grid_schemas
    if success_categories:
        signature["success_categories"] = success_categories
    if failure_categories:
        signature["failure_categories"] = failure_categories
    if include_candidates:
        signature["_field_effect_candidates"] = field_effects
    return signature


def build_response_signature_from_text(
    text: str,
    *,
    include_candidates: bool = False,
) -> dict[str, Any]:
    resp = parse_response_text(text)
    if resp is None:
        return {}
    return build_response_signature(resp, include_candidates=include_candidates)


def is_meaningful_response_signature(signature: dict[str, Any] | None) -> bool:
    if not isinstance(signature, dict) or not signature:
        return False
    if signature.get("outcome") in {"success", "failure"}:
        return True
    if signature.get("required_actions"):
        return True
    if signature.get("required_field_effects"):
        return True
    if signature.get("show_forms"):
        return True
    if signature.get("grid_schemas"):
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
    level = str(signature.get("contract_level") or "business")
    show_forms = [str(form) for form in (signature.get("required_forms") or []) if form]
    grid_schemas = [
        schema for schema in (signature.get("required_grid_schemas") or [])
        if isinstance(schema, dict)
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
    if show_forms:
        parts.append(f"期望打开表单 {len(show_forms)} 个")
    if grid_schemas:
        parts.append(f"期望列表结构 {len(grid_schemas)} 处")
    return {
        "contract_level": level,
        "outcome": outcome,
        "required_action_count": len(required_actions),
        "required_actions": required_actions[:8],
        "required_field_effect_count": len(field_effects),
        "required_field_keys": [
            str(effect.get("field") or "")
            for effect in field_effects[:8]
            if effect.get("field")
        ],
        "required_form_count": len(show_forms),
        "required_forms": show_forms[:8],
        "required_grid_count": len(grid_schemas),
        "label": "；".join(parts) or "响应语义锚点",
    }


def _step_field_keys(step: dict[str, Any]) -> set[str]:
    keys: set[str] = set()
    field_key = str(step.get("field_key") or "").strip()
    if field_key:
        keys.add(field_key)
    fields = step.get("fields")
    if isinstance(fields, dict):
        keys.update(str(key) for key in fields if str(key))

    def walk(node: Any) -> None:
        if isinstance(node, dict):
            field = str(node.get("k") or node.get("fieldKey") or "").strip()
            if field:
                keys.add(field)
            field_names = node.get("FieldName")
            if isinstance(field_names, list):
                keys.update(str(item) for item in field_names if str(item))
            elif field_names:
                keys.add(str(field_names))
            for value in node.values():
                if isinstance(value, (dict, list)):
                    walk(value)
        elif isinstance(node, list):
            for item in node:
                walk(item)

    walk(step.get("post_data"))
    walk(step.get("args"))
    return keys


def _compact_grid_schema(schema: dict[str, Any], step_fields: set[str]) -> dict[str, Any]:
    columns = [str(column) for column in (schema.get("columns") or []) if column]
    normalized_step_fields = {
        "".join(ch for ch in field.lower() if ch.isalnum())
        for field in step_fields
    }
    stable = [
        column
        for column in columns
        if column in step_fields
        or "".join(ch for ch in column.lower() if ch.isalnum()) in normalized_step_fields
        or column.lower() in _IDENTITY_COLUMN_HINTS
    ]
    if not stable:
        stable = columns[:8]
    return {
        "control": str(schema.get("control") or ""),
        "required_columns": stable[:24],
        "non_empty": bool(schema.get("non_empty")),
    }


def specialize_response_signature(
    signature: dict[str, Any],
    step: dict[str, Any],
    *,
    contract_level: str,
    anchor_reason: str,
) -> dict[str, Any]:
    """Turn candidate response semantics into a compact recorded contract."""
    result = {
        key: value
        for key, value in signature.items()
        if not str(key).startswith("_")
        and key not in {"show_forms", "grid_schemas"}
    }
    result["contract_level"] = contract_level
    result["anchor_reason"] = anchor_reason
    step_fields = _step_field_keys(step)

    required_actions = set(result.get("required_actions") or [])
    if contract_level == "critical":
        required_actions.update(
            action for action in (signature.get("actions") or [])
            if action in _STRUCTURAL_ACTIONS
        )
    if required_actions:
        result["required_actions"] = sorted({
            _action_semantic(action)
            for action in required_actions
        })

    forms = [str(form) for form in (signature.get("show_forms") or []) if form]
    if forms:
        result["required_forms"] = forms

    effects = []
    for effect in signature.get("_field_effect_candidates") or []:
        if not isinstance(effect, dict) or not effect.get("non_empty"):
            continue
        field = str(effect.get("field") or "")
        if not (
            field in step_fields
            or field.lower() in _STABLE_FIELD_KEYS
            or contract_level == "business"
        ):
            continue
        compact_effect = dict(effect)
        if contract_level == "business" and field not in _REQUIRED_FIELD_KEYS:
            # Entry row numbers can shift when runtime metadata adds/removes
            # fields. Match the stable control+field identity instead.
            compact_effect["row"] = None
        effects.append(compact_effect)
    if effects:
        result["required_field_effects"] = effects[:32]

    schemas = []
    if contract_level == "business":
        schemas = [
            _compact_grid_schema(schema, step_fields)
            for schema in (signature.get("grid_schemas") or [])
            if isinstance(schema, dict)
        ]
    schemas = [schema for schema in schemas if schema.get("required_columns")]
    if schemas:
        result["required_grid_schemas"] = schemas[:8]
    return result


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


def _find_matching_grid_schema(
    expected: dict[str, Any],
    actual_schemas: list[dict[str, Any]],
) -> dict[str, Any] | None:
    exp_control = str(expected.get("control") or "")
    for schema in actual_schemas:
        if exp_control and str(schema.get("control") or "") != exp_control:
            continue
        return schema
    return None


def _response_contract_mismatches(expected: dict[str, Any], actual_resp: Any) -> list[str]:
    actual = build_response_signature(actual_resp, include_candidates=True)
    mismatches: list[str] = []

    def add(message: str) -> None:
        mismatches.append(f"[ResponseSemantic] {message}")

    expected_outcome = str(expected.get("outcome") or "")
    actual_outcome = str(actual.get("outcome") or "")
    if expected_outcome == "success" and actual_outcome != "success":
        add(f"recorded success but runtime outcome is {actual_outcome or 'unknown'}")
    elif expected_outcome == "failure" and actual_outcome != "failure":
        add("recorded business validation was not reproduced")
    elif expected_outcome == "not_failure" and actual_outcome == "failure":
        add("recorded write anchor had no failure but runtime response failed")

    actual_actions = {
        _action_semantic(action)
        for action in (actual.get("actions") or [])
    }
    for action in expected.get("required_actions") or []:
        if str(action) not in actual_actions:
            add(f"missing recorded action {action}")

    actual_forms = set(actual.get("show_forms") or [])
    for form_id in expected.get("required_forms") or []:
        if str(form_id) not in actual_forms:
            add(f"missing recorded target form {form_id}")

    for category in expected.get("success_categories") or []:
        if str(category) not in set(actual.get("success_categories") or []):
            add(f"missing recorded success category {category}")
    for category in expected.get("failure_categories") or []:
        if str(category) not in set(actual.get("failure_categories") or []):
            add(f"missing recorded failure category {category}")

    actual_effects = actual.get("_field_effect_candidates") or []
    for effect in expected.get("required_field_effects") or []:
        if not isinstance(effect, dict):
            continue
        actual_effect = _find_matching_field_effect(effect, actual_effects)
        field = effect.get("field") or "?"
        row = effect.get("row")
        row_text = f" row={row}" if isinstance(row, int) else ""
        if actual_effect is None:
            add(f"missing non-empty callback field {field}{row_text}")
        elif effect.get("non_empty") and not actual_effect.get("non_empty"):
            add(f"callback field {field}{row_text} became empty")

    actual_schemas = actual.get("grid_schemas") or []
    for schema in expected.get("required_grid_schemas") or []:
        if not isinstance(schema, dict):
            continue
        actual_schema = _find_matching_grid_schema(schema, actual_schemas)
        control = str(schema.get("control") or "?")
        if actual_schema is None:
            add(f"missing recorded list structure {control}")
            continue
        actual_columns = set(actual_schema.get("columns") or [])
        missing_columns = [
            str(column)
            for column in (schema.get("required_columns") or [])
            if str(column) not in actual_columns
        ]
        if missing_columns:
            add(f"list {control} missing columns {','.join(missing_columns[:8])}")
        if schema.get("non_empty") and not actual_schema.get("non_empty"):
            add(f"list {control} became empty")
    return mismatches


def evaluate_response_contract(expected: Any, actual_resp: Any) -> dict[str, Any]:
    """Evaluate a recorded response contract with strict/advisory semantics."""
    if not isinstance(expected, dict) or not expected:
        return {"contract_level": "", "errors": [], "warnings": []}
    level = str(expected.get("contract_level") or "critical")
    mismatches = _response_contract_mismatches(expected, actual_resp)
    if expected.get("allow_transient_empty"):
        mismatches = [
            message for message in mismatches
            if not message.endswith(" became empty")
        ]
    return {
        "contract_level": level,
        "errors": [] if level == "advisory" else mismatches,
        "warnings": mismatches if level == "advisory" else [],
    }


def compare_response_signature(expected: Any, actual_resp: Any) -> list[str]:
    """Backward-compatible strict comparison used by tests and callers."""
    result = evaluate_response_contract(expected, actual_resp)
    return list(result["errors"] or result["warnings"])
