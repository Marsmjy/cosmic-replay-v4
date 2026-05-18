"""Structured repair planner for failed YAML cases.

The planner converts failure analysis and advisor output into conservative,
user-confirmed YAML mutations. It never applies changes by itself; callers must
explicitly pass a selected repair to ``apply_repair``.
"""
from __future__ import annotations

from copy import deepcopy
from dataclasses import asdict, dataclass, field
from typing import Any


@dataclass
class Repair:
    id: str
    title: str
    reason: str
    operation: str
    confidence: str = "medium"
    safe_to_apply: bool = False
    preview: str = ""
    target: dict[str, Any] = field(default_factory=dict)
    payload: dict[str, Any] = field(default_factory=dict)

    def to_dict(self) -> dict[str, Any]:
        return asdict(self)


_BASEDATA_HINTS = {
    "adminorg",
    "org",
    "createorg",
    "useorg",
    "position",
    "job",
    "country",
    "countryregion",
    "city",
    "workplace",
    "enterprise",
    "lawentity",
    "parentorg",
    "adminorgtype",
    "changescene",
    "changereason",
    "changedesc",
}

_DATE_HINTS = {
    "date",
    "bsed",
    "bsled",
    "startdate",
    "enddate",
    "effectdate",
    "establishmentdate",
}

_UNIQUE_HINTS = {
    "number": ("auto_number", "AUTO${rand:6}"),
    "code": ("auto_code", "AUTO${rand:6}"),
    "name": ("auto_name", "自动修复${rand:6}"),
    "email": ("test_email", "auto${rand:6}@example.com"),
    "peremail": ("test_email", "auto${rand:6}@example.com"),
    "phone": ("test_phone", "138${rand:8}"),
}


def build_repair_plan(
    case: dict,
    failure_analysis: dict | None = None,
    fixes: list[dict] | None = None,
) -> list[dict[str, Any]]:
    """Build conservative repair suggestions."""
    case = case or {}
    failure_analysis = failure_analysis or {}
    fixes = fixes or []
    repairs: list[Repair] = []

    nav_repair = _plan_navigation_optional(case, failure_analysis)
    if nav_repair:
        repairs.append(nav_repair)

    for fix in fixes:
        error_type = str(fix.get("error_type") or "")
        if error_type == "missing_required":
            repair = _plan_missing_required(case, fix)
        elif error_type == "duplicate":
            repair = _plan_duplicate(case, fix)
        else:
            repair = None
        if repair:
            repairs.append(repair)

    # Failure analysis can classify duplicate/missing even when advisor did not
    # produce a structured fix.
    category = failure_analysis.get("category")
    if category == "business_duplicate" and not any(r.operation == "refresh_unique_var" for r in repairs):
        repair = _plan_duplicate(case, {
            "field_key": "",
            "field_caption": failure_analysis.get("field_caption", ""),
            "confidence": failure_analysis.get("confidence", "medium"),
        })
        if repair:
            repairs.append(repair)

    return [_dedupe_id(repair, repairs[:idx]).to_dict() for idx, repair in enumerate(repairs)]


def apply_repair(case: dict, repair: dict) -> tuple[dict, bool, str]:
    """Apply a selected repair to a case dict."""
    if not repair.get("safe_to_apply"):
        return case, False, "该修复建议未标记为安全自动应用，请手工确认后修改。"

    op = repair.get("operation")
    new_case = deepcopy(case)
    if op == "mark_step_optional":
        return _apply_mark_step_optional(new_case, repair)
    if op == "refresh_unique_var":
        return _apply_refresh_unique_var(new_case, repair)
    if op == "insert_missing_field":
        return _apply_insert_missing_field(new_case, repair)
    return case, False, f"未知修复操作: {op}"


def _plan_navigation_optional(case: dict, analysis: dict) -> Repair | None:
    if analysis.get("category") != "navigation_service_unavailable":
        return None
    step_id = str(analysis.get("step_id") or "")
    step = _find_step(case, step_id)
    if not step:
        return Repair(
            id="nav_optional_unresolved",
            title="将导航步骤标记为 optional",
            reason="失败归因为非主导航服务不可达，但未在 YAML 中找到对应步骤。",
            operation="mark_step_optional",
            confidence="low",
            safe_to_apply=False,
            preview=f"未找到 step: {step_id}",
            target={"step_id": step_id},
        )

    main_form = str(case.get("main_form_id") or "")
    form_id = str(step.get("form_id") or "")
    safe = _is_navigation_form(form_id, main_form)
    return Repair(
        id=f"mark_optional_{step_id}",
        title="导航步骤降级为 optional",
        reason="非主业务导航表单不可达，不应阻断主表单写库。",
        operation="mark_step_optional",
        confidence="high" if safe else "low",
        safe_to_apply=safe,
        preview=f"steps[{step_id}].optional = true",
        target={"step_id": step_id},
    )


def _plan_duplicate(case: dict, fix: dict) -> Repair | None:
    vars_map = case.get("vars") or {}
    if not isinstance(vars_map, dict):
        return None

    field_key = str(fix.get("field_key") or "").lower()
    field_caption = str(fix.get("field_caption") or "")
    var_name = _find_unique_var(vars_map, field_key, field_caption)
    if not var_name:
        return Repair(
            id="refresh_unique_var_unresolved",
            title="刷新唯一字段变量",
            reason="检测到唯一字段重复，但未能定位对应 vars 变量。",
            operation="refresh_unique_var",
            confidence="low",
            safe_to_apply=False,
            preview="请手工为编号/名称/邮箱等变量追加 ${rand:6} 或 ${timestamp}。",
            target={"field_key": field_key, "field_caption": field_caption},
        )

    current = str(vars_map.get(var_name) or "")
    if "${rand:" in current or "${timestamp}" in current:
        preview = f"vars.{var_name} 已包含随机片段，无需自动修改。"
        safe = False
    else:
        preview = f"vars.{var_name}: {current} -> {current}${{rand:6}}"
        safe = True

    return Repair(
        id=f"refresh_var_{var_name}",
        title="为唯一变量追加随机后缀",
        reason="重复错误通常来自编号、名称、手机号或邮箱等唯一字段复用旧值。",
        operation="refresh_unique_var",
        confidence=str(fix.get("confidence") or "medium"),
        safe_to_apply=safe,
        preview=preview,
        target={"var_name": var_name},
        payload={"suffix": "${rand:6}"},
    )


def _plan_missing_required(case: dict, fix: dict) -> Repair | None:
    field_key = str(fix.get("field_key") or "").strip()
    if not field_key:
        return Repair(
            id="insert_missing_field_unresolved",
            title="补充必填字段",
            reason="检测到必填缺失，但未能推断字段 key。",
            operation="insert_missing_field",
            confidence="low",
            safe_to_apply=False,
            preview=str(fix.get("patch_yaml") or "请手工确认字段 key 后插入填写步骤。"),
        )

    insert_at = _find_insert_index(case)
    form_id, app_id = _main_form_app(case)
    step_type, value, safe = _suggest_missing_value(field_key, fix)
    if not form_id or not app_id:
        safe = False

    step_id = f"auto_fill_{field_key.lower()}"
    if step_type == "pick_basedata":
        step = {
            "id": step_id,
            "type": "pick_basedata",
            "form_id": form_id or "<主表单 id>",
            "app_id": app_id or "<主表单 app_id>",
            "field_key": field_key,
            "value_id": value or "<基础资料 id>",
        }
    else:
        step = {
            "id": step_id,
            "type": "update_fields",
            "form_id": form_id or "<主表单 id>",
            "app_id": app_id or "<主表单 app_id>",
            "fields": {field_key: value},
        }

    return Repair(
        id=f"insert_missing_{field_key.lower()}",
        title=f"补充必填字段 {field_key}",
        reason=str(fix.get("diagnosis") or "业务必填字段缺失。"),
        operation="insert_missing_field",
        confidence=str(fix.get("confidence") or ("medium" if safe else "low")),
        safe_to_apply=safe,
        preview=f"在第 {insert_at + 1} 个步骤前插入: {step}",
        target={"insert_index": insert_at, "field_key": field_key},
        payload={"step": step},
    )


def _apply_mark_step_optional(case: dict, repair: dict) -> tuple[dict, bool, str]:
    step_id = (repair.get("target") or {}).get("step_id")
    step = _find_step(case, step_id)
    if not step:
        return case, False, f"未找到步骤: {step_id}"
    step["optional"] = True
    return case, True, f"已将步骤 {step_id} 标记为 optional。"


def _apply_refresh_unique_var(case: dict, repair: dict) -> tuple[dict, bool, str]:
    var_name = (repair.get("target") or {}).get("var_name")
    suffix = (repair.get("payload") or {}).get("suffix") or "${rand:6}"
    vars_map = case.setdefault("vars", {})
    if var_name not in vars_map:
        return case, False, f"未找到变量: {var_name}"
    current = str(vars_map.get(var_name) or "")
    if "${rand:" in current or "${timestamp}" in current:
        return case, False, f"变量 {var_name} 已包含随机片段。"
    vars_map[var_name] = current + suffix
    return case, True, f"已为变量 {var_name} 追加随机后缀。"


def _apply_insert_missing_field(case: dict, repair: dict) -> tuple[dict, bool, str]:
    step = deepcopy((repair.get("payload") or {}).get("step") or {})
    if not step:
        return case, False, "修复建议缺少待插入步骤。"
    steps = case.setdefault("steps", [])
    if not isinstance(steps, list):
        return case, False, "YAML steps 不是数组，无法自动插入。"
    insert_index = int((repair.get("target") or {}).get("insert_index") or _find_insert_index(case))
    insert_index = max(0, min(insert_index, len(steps)))
    if any(s.get("id") == step.get("id") for s in steps if isinstance(s, dict)):
        return case, False, f"步骤 {step.get('id')} 已存在。"

    _ensure_vars_for_step(case, step)
    steps.insert(insert_index, step)
    return case, True, f"已插入步骤 {step.get('id')}。"


def _find_step(case: dict, step_id: str) -> dict | None:
    for step in case.get("steps") or []:
        if isinstance(step, dict) and step.get("id") == step_id:
            return step
    return None


def _find_insert_index(case: dict) -> int:
    steps = case.get("steps") or []
    for idx, step in enumerate(steps):
        if not isinstance(step, dict):
            continue
        blob = " ".join(str(step.get(k) or "").lower() for k in ("id", "type", "ac", "key"))
        if step.get("ac") in {"save", "submit", "audit", "saveandeffect", "submitandeffect"}:
            return idx
        if any(token in blob for token in ("bar_save", "submit", "audit", "startupflow", "doconfirm")):
            return idx
    return len(steps)


def _main_form_app(case: dict) -> tuple[str, str]:
    main_form = str(case.get("main_form_id") or "")
    for step in case.get("steps") or []:
        if not isinstance(step, dict):
            continue
        if main_form and step.get("form_id") != main_form:
            continue
        app_id = str(step.get("app_id") or "")
        if step.get("form_id") and app_id:
            return str(step.get("form_id")), app_id
    for step in case.get("steps") or []:
        if isinstance(step, dict) and step.get("form_id") and step.get("app_id"):
            return str(step.get("form_id")), str(step.get("app_id"))
    return main_form, ""


def _suggest_missing_value(field_key: str, fix: dict) -> tuple[str, Any, bool]:
    key = field_key.lower()
    suggested = fix.get("suggested_value")
    if key in _BASEDATA_HINTS:
        if suggested and str(suggested) not in {"你的值", "填写一个值"}:
            return "pick_basedata", str(suggested), True
        return "pick_basedata", "", False
    if key in _DATE_HINTS or key.endswith("date"):
        return "update_fields", "${today}", True
    if key in {"name", "fullname", "simplename", "description"}:
        var_name, template = _UNIQUE_HINTS.get(key, ("auto_text", "自动修复${rand:6}"))
        return "update_fields", {"zh_CN": f"${{vars.{var_name}}}"}, True
    if key in _UNIQUE_HINTS:
        var_name, _ = _UNIQUE_HINTS[key]
        return "update_fields", f"${{vars.{var_name}}}", True
    if suggested and str(suggested) not in {"你的值", "填写一个值"}:
        return "update_fields", suggested, True
    return "update_fields", "填写一个值", False


def _ensure_vars_for_step(case: dict, step: dict) -> None:
    vars_map = case.setdefault("vars", {})
    values = []
    if step.get("type") == "update_fields":
        values.extend((step.get("fields") or {}).values())
    else:
        values.append(step.get("value_id"))
    for value in values:
        _ensure_vars_for_value(vars_map, value)


def _ensure_vars_for_value(vars_map: dict, value: Any) -> None:
    if isinstance(value, str):
        for key, template in _UNIQUE_HINTS.values():
            if f"${{vars.{key}}}" == value and key not in vars_map:
                vars_map[key] = template
    elif isinstance(value, dict):
        for item in value.values():
            _ensure_vars_for_value(vars_map, item)
    elif isinstance(value, list):
        for item in value:
            _ensure_vars_for_value(vars_map, item)


def _find_unique_var(vars_map: dict, field_key: str, field_caption: str) -> str:
    candidates = []
    field_key = field_key.lower()
    caption = field_caption.lower()
    hints = [field_key] if field_key else []
    if caption:
        if any(x in caption for x in ("编码", "编号", "工号")):
            hints.extend(["number", "code"])
        if "名称" in caption or "姓名" in caption:
            hints.append("name")
        if "邮箱" in caption:
            hints.append("email")
        if "手机" in caption or "电话" in caption:
            hints.append("phone")
    hints.extend(["number", "code", "name", "email", "phone"])

    for name, value in vars_map.items():
        blob = f"{name} {value}".lower()
        if any(h and h in blob for h in hints):
            candidates.append(name)
    return candidates[0] if candidates else ""


def _is_navigation_form(form_id: str, main_form: str) -> bool:
    if not form_id or form_id == main_form:
        return False
    return (
        form_id.endswith("_apphome")
        or form_id.startswith("bos_card_")
        or form_id.startswith("gbs_bgtask")
        or form_id in {"hom_wbcalendar", "hom_wbwaitin", "hom_wbwarning"}
    )


def _dedupe_id(repair: Repair, previous: list[Repair]) -> Repair:
    seen = {item.id for item in previous}
    if repair.id not in seen:
        return repair
    base = repair.id
    idx = 2
    while f"{base}_{idx}" in seen:
        idx += 1
    repair.id = f"{base}_{idx}"
    return repair
