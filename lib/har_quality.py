"""HAR 导入质量评估。

目标：在生成 YAML 前给出可解释的风险评分，帮助用户提前发现变量遗漏、
环境字段缺失、主链路不完整和新组件兼容风险。
"""
from __future__ import annotations

from typing import Any

from lib.ir.write_contract import is_write_step


Issue = dict[str, Any]


_WEIGHTS = {
    "workflow": 35,
    "variables": 25,
    "environment": 25,
    "compatibility": 15,
}

_SEVERITY_PENALTY = {
    "critical": 30,
    "high": 16,
    "medium": 8,
    "low": 3,
    "info": 0,
}

_KNOWN_ACS = {
    "addnew",
    "addsonlogicentity",
    "afterConfirm",
    "appItemClick",
    "audit",
    "changeYear",
    "click",
    "clientCallBack",
    "clientPosInvokeMethod",
    "close",
    "commonSearch",
    "customEvent",
    "delete",
    "doConfirm",
    "donothing_newbill",
    "entryRowClick",
    "getHintScroll",
    "getCityInfo",
    "getCountrys",
    "getFrequentData",
    "getLookUpList",
    "getMenuData",
    "getProvincesByCountryId",
    "getTelViaList",
    "hyperLinkClick",
    "itemClick",
    "loadData",
    "menuItemClick",
    "modify",
    "new",
    "postExpandNodes",
    "queryExceedMaxCount",
    "queryTreeNodeChildren",
    "refresh",
    "release",
    "save",
    "saveSetting",
    "saveandeffect",
    "selectTab",
    "setItemByIdFromClient",
    "setItemValueByIdFromClient",
    "submit",
    "submitandeffect",
    "treeMenuClick",
    "unaudit",
    "updateValue",
}

_UNIQUE_FIELD_HINTS = (
    "number",
    "code",
    "billno",
    "orderno",
    "name",
    "phone",
    "certificatenumber",
    "email",
    "peremail",
)

_ENV_FIELD_HINTS = {
    "adminorg",
    "org",
    "createorg",
    "useorg",
    "enterprise",
    "lawentity",
    "position",
    "job",
    "country",
    "countryregion",
    "city",
    "workplace",
    "dept",
    "company",
}


def assess_preview_quality(
    *,
    main_form_id: str,
    tier_counts: dict[str, int],
    steps: list[dict],
    detected_vars: list[dict],
    pick_fields: list[dict],
    component_report: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """基于 HAR preview 结果生成质量评分。"""
    issues: list[Issue] = []

    def add_issue(
        category: str,
        severity: str,
        code: str,
        message: str,
        suggestion: str,
        *,
        step_id: str = "",
        form_id: str = "",
    ) -> None:
        issues.append({
            "category": category,
            "severity": severity,
            "code": code,
            "message": message,
            "suggestion": suggestion,
            "step_id": step_id,
            "form_id": form_id,
        })

    core_count = int((tier_counts or {}).get("core") or 0)
    ui_count = int((tier_counts or {}).get("ui_reaction") or 0)
    noise_count = int((tier_counts or {}).get("noise") or 0)
    step_count = len(steps or [])
    persistence_steps = [s for s in steps if _is_persistence_step(s)]
    pick_step_count = sum(1 for s in steps if s.get("type") == "pick_basedata")
    high_env_count = sum(1 for pf in pick_fields if pf.get("env_sensitive") == "high")
    medium_env_count = sum(1 for pf in pick_fields if pf.get("env_sensitive") == "medium")
    unknown_acs = sorted({
        str(s.get("ac") or "")
        for s in steps
        if s.get("type") == "invoke"
        and s.get("ac")
        and str(s.get("ac")) not in _KNOWN_ACS
    })
    component_summary = (component_report or {}).get("summary") or {}
    component_coverage = int(component_summary.get("coverage_percent", 100) or 0)
    unsupported_components = int(component_summary.get("unsupported_steps", 0) or 0)
    partial_components = int(component_summary.get("partial_steps", 0) or 0)

    if not main_form_id:
        add_issue(
            "workflow",
            "critical",
            "main_form_missing",
            "未能识别主业务表单。",
            "检查 HAR 是否包含完整业务操作，或在解析规则中补充主表单识别特征。",
        )
    elif main_form_id.endswith("_apphome"):
        add_issue(
            "workflow",
            "high",
            "main_form_looks_like_navigation",
            f"主表单 {main_form_id} 看起来是首页/导航表单。",
            "确认 HAR 是否录到了真正的业务卡片；若业务表单被菜单打开，需检查 target_form 推断。",
            form_id=main_form_id,
        )

    if core_count <= 0:
        add_issue(
            "workflow",
            "critical",
            "core_steps_missing",
            "未识别到核心业务步骤。",
            "重新录制 HAR，确保包含打开表单、填写字段、保存/提交等业务请求。",
        )

    if step_count > 250:
        add_issue(
            "compatibility",
            "medium",
            "too_many_steps",
            f"导入后仍有 {step_count} 个候选步骤，链路偏长。",
            "优先检查是否录入了跨应用查看、后台任务、列表刷新等非主流程步骤。",
        )

    if unknown_acs:
        add_issue(
            "compatibility",
            "medium" if len(unknown_acs) > 3 else "low",
            "unknown_actions",
            "发现未登记的苍穹动作类型：" + ", ".join(unknown_acs[:8]),
            "如果执行失败点落在这些动作上，应为对应组件补充处理器或降级策略。",
        )

    if unsupported_components:
        examples = [
            str(item.get("step_id") or item.get("ac") or "-")
            for item in (component_report or {}).get("unsupported", [])[:5]
        ]
        add_issue(
            "compatibility",
            "high" if unsupported_components >= 3 else "medium",
            "unsupported_components",
            f"组件雷达发现 {unsupported_components} 个未覆盖步骤。",
            "优先为这些步骤补充组件处理器：" + ", ".join(examples),
        )

    if component_coverage < 80:
        add_issue(
            "compatibility",
            "medium",
            "component_coverage_low",
            f"组件覆盖率仅 {component_coverage}%。",
            "导入新 HAR 前建议先处理未知组件，避免执行期才暴露协议差异。",
        )
    elif partial_components >= 10:
        add_issue(
            "compatibility",
            "low",
            "partial_components_many",
            f"有 {partial_components} 个步骤依赖通用/部分支持组件。",
            "若这些步骤处于主链路，应逐步沉淀为专用组件处理器。",
        )

    if not detected_vars and persistence_steps:
        add_issue(
            "variables",
            "medium",
            "vars_missing",
            "未识别到智能变量，但用例包含写库动作。",
            "检查编号、名称、手机号、证件号、邮箱等字段是否被硬编码；必要时补充变量识别词表。",
        )

    for step in steps:
        for field_key, value in _iter_update_fields(step):
            key_lower = field_key.lower()
            if not any(h in key_lower for h in _UNIQUE_FIELD_HINTS):
                continue
            if _contains_var_ref(value):
                continue
            if value in ("", None):
                continue
            add_issue(
                "variables",
                "medium",
                "hardcoded_unique_value",
                f"字段 {field_key} 像唯一字段，但仍是固定值。",
                "建议抽成 ${vars.*}，避免第二次运行出现“已存在/重复”。",
                step_id=str(step.get("id") or ""),
                form_id=str(step.get("form_id") or ""),
            )
            break

    if pick_step_count and not pick_fields:
        add_issue(
            "environment",
            "high",
            "pick_fields_missing",
            "检测到基础资料选择步骤，但未生成环境字段配置。",
            "需要进入 pick_fields 面板配置跨环境 value_id，或补充字段类型识别。",
        )

    pick_field_keys = {str(pf.get("field_key") or "").lower() for pf in pick_fields}
    for step in steps:
        if step.get("type") != "pick_basedata":
            continue
        field_key = str(step.get("field_key") or "").lower()
        if field_key in _ENV_FIELD_HINTS and field_key not in pick_field_keys:
            add_issue(
                "environment",
                "medium",
                "env_field_not_exposed",
                f"环境相关字段 {field_key} 未暴露到配置面板。",
                "将该字段加入 pick_fields，避免跨环境 ID 不一致。",
                step_id=str(step.get("id") or ""),
                form_id=str(step.get("form_id") or ""),
            )

    if high_env_count == 0:
        context_steps = [
            s for s in steps
            if s.get("type") == "invoke" and s.get("ac") in {"new", "addnew"}
        ]
        if any(_has_tree_focus(s) for s in context_steps):
            add_issue(
                "environment",
                "low",
                "tree_context_not_highlighted",
                "新增动作包含树焦点上下文，但没有 high 级环境字段。",
                "应将 treeview.focus.id 暴露为 high 级环境字段，便于跨环境调整。",
            )

    dimensions = _score_dimensions(issues)
    score = round(sum(
        dimensions[name]["score"] * _WEIGHTS[name]
        for name in _WEIGHTS
    ) / sum(_WEIGHTS.values()))

    grade = _grade(score)
    blocking = any(i["severity"] in {"critical", "high"} for i in issues)
    summary = _summary(score, grade, issues)

    return {
        "score": score,
        "grade": grade,
        "blocking": blocking,
        "summary": summary,
        "dimensions": list(dimensions.values()),
        "issues": issues,
        "checks": {
            "main_form_id": main_form_id,
            "step_count": step_count,
            "core_count": core_count,
            "ui_reaction_count": ui_count,
            "noise_count": noise_count,
            "detected_var_count": len(detected_vars or []),
            "pick_field_count": len(pick_fields or []),
            "high_env_field_count": high_env_count,
            "medium_env_field_count": medium_env_count,
            "persistence_step_count": len(persistence_steps),
            "unknown_actions": unknown_acs,
            "component_coverage_percent": component_coverage,
            "component_unsupported_count": unsupported_components,
            "component_partial_count": partial_components,
            "component_risk_level": component_summary.get("risk_level", "low"),
        },
    }


def _score_dimensions(issues: list[Issue]) -> dict[str, dict[str, Any]]:
    dimensions: dict[str, dict[str, Any]] = {
        name: {"name": name, "score": 100, "issue_count": 0}
        for name in _WEIGHTS
    }
    for issue in issues:
        category = issue.get("category")
        if category not in dimensions:
            continue
        dimensions[category]["issue_count"] += 1
        dimensions[category]["score"] -= _SEVERITY_PENALTY.get(issue.get("severity"), 0)
    for dim in dimensions.values():
        dim["score"] = max(0, min(100, int(dim["score"])))
    return dimensions


def _grade(score: int) -> str:
    if score >= 90:
        return "A"
    if score >= 80:
        return "B"
    if score >= 70:
        return "C"
    if score >= 60:
        return "D"
    return "E"


def _summary(score: int, grade: str, issues: list[Issue]) -> str:
    critical = sum(1 for i in issues if i.get("severity") == "critical")
    high = sum(1 for i in issues if i.get("severity") == "high")
    if critical:
        return f"{grade} 级 / {score} 分：存在阻断级导入风险，需要先修复。"
    if high:
        return f"{grade} 级 / {score} 分：存在高风险项，建议生成前确认。"
    if issues:
        return f"{grade} 级 / {score} 分：可生成，建议关注提示项。"
    return f"{grade} 级 / {score} 分：结构完整，适合直接生成并执行。"


def _is_persistence_step(step: dict) -> bool:
    return is_write_step(step)


def _iter_update_fields(step: dict):
    if step.get("type") != "update_fields":
        return
    fields = step.get("fields") or {}
    if not isinstance(fields, dict):
        return
    for key, value in fields.items():
        yield str(key), value


def _contains_var_ref(value: Any) -> bool:
    if isinstance(value, str):
        return "${vars." in value or "${timestamp}" in value or "${rand:" in value
    if isinstance(value, dict):
        return any(_contains_var_ref(v) for v in value.values())
    if isinstance(value, list):
        return any(_contains_var_ref(v) for v in value)
    return False


def _has_tree_focus(step: dict) -> bool:
    post_data = step.get("post_data")
    items = post_data if isinstance(post_data, list) else [post_data]
    for item in items:
        if isinstance(item, dict) and isinstance(item.get("treeview"), dict):
            focus = item["treeview"].get("focus")
            if isinstance(focus, dict) and (focus.get("id") or focus.get("text")):
                return True
    return False
