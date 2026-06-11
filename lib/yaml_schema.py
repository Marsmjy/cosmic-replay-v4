"""Value-safe YAML schema linting for generated replay cases.

This module validates the shape of HAR-generated YAML without executing the
case and without inspecting raw business values.  It is intentionally light:
old cases should keep running, while newly generated cases get a stable
contract section that UI, reports, and AI repair can reason about.
"""
from __future__ import annotations

from collections import Counter
from typing import Any, Mapping


SCHEMA_VERSION = "1.0"

KNOWN_STEP_TYPES = {
    "open_form",
    "invoke",
    "update_fields",
    "pick_basedata",
    "select_f7_list_row",
    "click_toolbar",
    "click_menu",
    "upload_file",
    "sleep",
    "wait_until",
}

WRITE_ACS = {
    "save",
    "submit",
    "audit",
    "unaudit",
    "delete",
    "modify",
    "saveandeffect",
    "submitandeffect",
    "saveandaudit",
    "doconfirm",
    "afterconfirm",
    "startupflow",
}


def validate_yaml_schema(case: Mapping[str, Any] | None) -> dict[str, Any]:
    """Return a value-safe schema contract for a generated YAML case."""
    errors: list[dict[str, str]] = []
    warnings: list[dict[str, str]] = []
    if not isinstance(case, Mapping):
        return _result(
            errors=[_issue("yaml_not_mapping", "$", "YAML 根节点必须是对象。")],
            warnings=[],
            summary={},
        )

    steps_raw = case.get("steps")
    steps = steps_raw if isinstance(steps_raw, list) else []
    vars_map = case.get("vars") if isinstance(case.get("vars"), Mapping) else {}
    vars_meta = case.get("vars_meta") if isinstance(case.get("vars_meta"), Mapping) else {}
    pick_fields = case.get("pick_fields") if isinstance(case.get("pick_fields"), Mapping) else {}
    assertions_raw = case.get("assertions")
    assertions = assertions_raw if isinstance(assertions_raw, list) else []

    if not str(case.get("name") or "").strip():
        errors.append(_issue("name_missing", "$.name", "缺少用例名称。"))
    if "schema_version" not in case:
        warnings.append(_issue("schema_version_missing", "$.schema_version", "建议声明 YAML schema_version。"))
    if not isinstance(steps_raw, list):
        errors.append(_issue("steps_missing", "$.steps", "steps 必须是列表。"))
    if "vars" in case and not isinstance(case.get("vars"), Mapping):
        errors.append(_issue("vars_not_mapping", "$.vars", "vars 必须是对象。"))
    if "vars_meta" in case and not isinstance(case.get("vars_meta"), Mapping):
        errors.append(_issue("vars_meta_not_mapping", "$.vars_meta", "vars_meta 必须是对象。"))
    if "pick_fields" in case and not isinstance(case.get("pick_fields"), Mapping):
        errors.append(_issue("pick_fields_not_mapping", "$.pick_fields", "pick_fields 必须是对象。"))
    if "assertions" in case and not isinstance(assertions_raw, list):
        errors.append(_issue("assertions_not_list", "$.assertions", "assertions 必须是列表。"))

    step_ids: list[str] = []
    type_counts: Counter[str] = Counter()
    write_step_count = 0
    for index, step in enumerate(steps):
        path = f"$.steps[{index}]"
        if not isinstance(step, Mapping):
            errors.append(_issue("step_not_mapping", path, "step 必须是对象。"))
            continue
        step_id = str(step.get("id") or "").strip()
        step_type = str(step.get("type") or "").strip()
        ac = str(step.get("ac") or "").strip().lower()
        method = str(step.get("method") or "").strip()
        key = str(step.get("key") or "").strip()
        if not step_id:
            errors.append(_issue("step_id_missing", f"{path}.id", "step 缺少 id。"))
        else:
            step_ids.append(step_id)
        if not step_type:
            errors.append(_issue("step_type_missing", f"{path}.type", f"step {step_id or index} 缺少 type。"))
        elif step_type not in KNOWN_STEP_TYPES:
            warnings.append(_issue("unknown_step_type", f"{path}.type", f"未知 step 类型: {step_type}。"))
        type_counts[step_type or ""] += 1
        if step_type == "invoke" and not (ac or method or key):
            warnings.append(_issue("invoke_missing_action", path, f"invoke step {step_id or index} 缺少 ac/method/key。"))
        if step_type == "update_fields" and not isinstance(step.get("fields"), Mapping):
            warnings.append(_issue("update_fields_missing_fields", f"{path}.fields", f"update_fields step {step_id or index} 缺少 fields。"))
        if step_type in {"pick_basedata", "select_f7_list_row"} and not str(step.get("field_key") or "").strip():
            warnings.append(_issue("selector_missing_field_key", f"{path}.field_key", f"{step_type} step {step_id or index} 缺少 field_key。"))
        if ac in WRITE_ACS:
            write_step_count += 1

    duplicated_ids = sorted(item for item, count in Counter(step_ids).items() if count > 1)
    for step_id in duplicated_ids[:10]:
        errors.append(_issue("duplicate_step_id", "$.steps", f"step id 重复: {step_id}。"))

    for index, assertion in enumerate(assertions):
        path = f"$.assertions[{index}]"
        if not isinstance(assertion, Mapping):
            errors.append(_issue("assertion_not_mapping", path, "assertion 必须是对象。"))
            continue
        if not str(assertion.get("type") or "").strip():
            errors.append(_issue("assertion_type_missing", f"{path}.type", "assertion 缺少 type。"))

    assertion_types = {
        str(item.get("type") or "")
        for item in assertions
        if isinstance(item, Mapping)
    }
    if write_step_count and "no_save_failure" not in assertion_types:
        warnings.append(_issue("write_missing_no_save_failure", "$.assertions", "写入用例建议包含 no_save_failure。"))
    if "no_error_actions" not in assertion_types:
        warnings.append(_issue("missing_no_error_actions", "$.assertions", "建议包含 no_error_actions 基础断言。"))

    ir_contract = case.get("ir_contract") if isinstance(case.get("ir_contract"), Mapping) else {}
    if not ir_contract:
        warnings.append(_issue("ir_contract_missing", "$.ir_contract", "建议生成 IR 对齐契约，便于回归判断解析覆盖。"))
    else:
        policy = ir_contract.get("policy") if isinstance(ir_contract.get("policy"), Mapping) else {}
        coverage = ir_contract.get("coverage") if isinstance(ir_contract.get("coverage"), Mapping) else {}
        if not isinstance(coverage, Mapping) or "ir_step_count" not in coverage:
            warnings.append(_issue("ir_contract_coverage_missing", "$.ir_contract.coverage", "IR 契约缺少 coverage 摘要。"))
        if policy.get("store_full_ir_in_yaml") is True:
            errors.append(_issue("unsafe_full_ir_in_yaml", "$.ir_contract.policy", "YAML 不应内嵌完整 IR 或原始 HAR。"))
        if policy.get("raw_har_committed") is True:
            errors.append(_issue("unsafe_raw_har_committed", "$.ir_contract.policy", "YAML 不应声明提交原始 HAR。"))

    contract_sections = {
        "capability",
        "ai_assistance",
        "environment_binding_plan",
        "runtime_value_flow_plan",
        "execution_contract",
    }
    missing_contract_sections = sorted(section for section in contract_sections if section not in case)
    if missing_contract_sections:
        warnings.append(_issue(
            "case_contract_sections_missing",
            "$",
            "缺少 case contract 段: " + ", ".join(missing_contract_sections),
        ))

    summary = {
        "step_count": len(steps),
        "step_type_counts": dict(sorted((key, value) for key, value in type_counts.items() if key)),
        "assertion_count": len(assertions),
        "variable_count": len([key for key in vars_map.keys() if not str(key).startswith("_")]),
        "vars_meta_count": len(vars_meta),
        "environment_field_count": len(pick_fields),
        "maintainable_field_count": len(vars_meta) + len(pick_fields),
        "write_step_count": write_step_count,
        "has_ir_contract": bool(ir_contract),
        "has_case_contract": not missing_contract_sections,
    }
    return _result(errors=errors, warnings=warnings, summary=summary)


def attach_yaml_schema_contract(case: dict[str, Any]) -> dict[str, Any]:
    """Attach a fresh YAML schema contract to a mutable case."""
    case["yaml_schema_contract"] = validate_yaml_schema(case)
    return case


def _result(*, errors: list[dict[str, str]], warnings: list[dict[str, str]], summary: dict[str, Any]) -> dict[str, Any]:
    status = "valid"
    if errors:
        status = "invalid"
    elif warnings:
        status = "valid_with_warnings"
    return {
        "schema_version": SCHEMA_VERSION,
        "status": status,
        "ok": not errors,
        "error_count": len(errors),
        "warning_count": len(warnings),
        "error_codes": sorted({item["code"] for item in errors}),
        "warning_codes": sorted({item["code"] for item in warnings}),
        "errors": errors[:20],
        "warnings": warnings[:20],
        "summary": summary,
    }


def _issue(code: str, path: str, message: str) -> dict[str, str]:
    return {"code": code, "path": path, "message": message}

