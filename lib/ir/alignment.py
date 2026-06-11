"""Compare value-safe IR signals with the main HAR preview/YAML chain."""
from __future__ import annotations

from collections import Counter
from typing import Any

from .yaml_bridge import classify_yaml_step_role


def assess_ir_preview_alignment(
    flow: dict[str, Any],
    *,
    preview_steps: list[dict[str, Any]],
    detected_vars: list[dict[str, Any]],
    pick_fields: list[dict[str, Any]],
) -> dict[str, Any]:
    """Score whether the main parser covered important IR-observed signals.

    This is diagnostic-only. It does not rewrite YAML, because the first safe
    step is to expose suspicious gaps before migrating rules into the parser.
    """
    ir_steps = flow.get("steps") or []
    pages = {
        str(page.get("id") or ""): page
        for page in flow.get("pages") or []
        if page.get("id")
    }
    ir_roles = Counter(str(step.get("role") or "unknown") for step in ir_steps)
    preview_roles = Counter(classify_yaml_step_role(step) for step in preview_steps or [])
    ir_l2_expected = sum(
        1
        for step in ir_steps
        if (pages.get(str(step.get("page_ref") or "")) or {}).get("expected_role") == "L2"
    )
    ir_l3_expected = sum(
        1
        for step in ir_steps
        if (pages.get(str(step.get("page_ref") or "")) or {}).get("expected_role") == "L3"
    )
    ir_pageid_missing = sum(
        1
        for step in ir_steps
        if (pages.get(str(step.get("page_ref") or "")) or {}).get("pageid_type") == "missing"
    )
    checks = {
        "ir_api_entry_count": int((flow.get("source_har") or {}).get("api_entry_count") or 0),
        "ir_step_count": len(ir_steps),
        "preview_step_count": len(preview_steps or []),
        "ir_role_counts": dict(sorted(ir_roles.items())),
        "preview_role_counts": dict(sorted(preview_roles.items())),
        "ir_l2_expected_count": ir_l2_expected,
        "ir_l3_expected_count": ir_l3_expected,
        "ir_pageid_missing_count": ir_pageid_missing,
        "preview_l2_preserve_count": sum(1 for step in preview_steps or [] if step.get("preserve_l2_page")),
        "detected_var_count": len(detected_vars or []),
        "pick_field_count": len(pick_fields or []),
    }
    issues = _alignment_issues(checks)
    score = _alignment_score(checks, issues)
    return {
        "score": score,
        "grade": _grade(score),
        "risk_level": _risk_level(score, issues),
        "summary": _summary(score, issues),
        "issues": issues,
        "checks": checks,
    }


def _alignment_issues(checks: dict[str, Any]) -> list[dict[str, Any]]:
    issues: list[dict[str, Any]] = []
    ir_roles = checks.get("ir_role_counts") or {}
    preview_roles = checks.get("preview_role_counts") or {}
    ir_write = int(ir_roles.get("write") or 0)
    ir_edit = int(ir_roles.get("edit") or 0)
    preview_write = int(preview_roles.get("write") or 0)
    preview_edit = int(preview_roles.get("edit") or 0)

    if checks["ir_api_entry_count"] <= 0:
        issues.append({
            "severity": "critical",
            "code": "ir_api_entries_missing",
            "message": "IR 未识别到苍穹业务 API 请求。",
            "suggestion": "确认 HAR 是否只包含静态资源；必要时重新录制完整业务链路。",
        })
    if checks["ir_step_count"] > 0 and checks["preview_step_count"] <= 0:
        issues.append({
            "severity": "critical",
            "code": "main_preview_steps_missing",
            "message": "IR 识别到业务请求，但主解析链路没有生成候选 step。",
            "suggestion": "优先补 HAR 过滤或 step 转换规则，不要直接手写 YAML 绕过。",
        })
    if ir_write > 0 and preview_write <= 0:
        issues.append({
            "severity": "high",
            "code": "write_step_not_covered",
            "message": "IR 发现写入动作，但主解析链路未覆盖保存/提交/确认步骤。",
            "suggestion": "检查 ac/method/key/args 识别，确保保存或提交步骤进入 YAML。",
        })
    if ir_edit > 0 and preview_edit <= 0:
        issues.append({
            "severity": "medium",
            "code": "edit_step_not_covered",
            "message": "IR 发现字段编辑/回填动作，但主解析链路未覆盖编辑步骤。",
            "suggestion": "检查 updateValue、setItemByIdFromClient、F7 回填或子窗字段维护解析。",
        })
    if checks["ir_l2_expected_count"] > 0 and checks["preview_l2_preserve_count"] <= 0:
        issues.append({
            "severity": "medium",
            "code": "l2_context_not_preserved",
            "message": "IR 观察到 L2 列表/菜单上下文，但主链路没有显式保留 L2。",
            "suggestion": "菜单、列表、树、工具栏和 addnew 前置桥接步骤应设置 preserve_l2_page。",
        })
    if checks["ir_pageid_missing_count"] >= max(3, checks["ir_step_count"] * 0.7) and checks["ir_step_count"] > 0:
        issues.append({
            "severity": "medium",
            "code": "ir_pageid_signal_weak",
            "message": "IR 中多数业务请求缺少 pageId 信号。",
            "suggestion": "确认 HAR 是否录到 batchInvokeAction 请求体；pageId 缺失会降低一次通过概率。",
        })
    if (ir_write or ir_edit) and checks["detected_var_count"] <= 0 and checks["pick_field_count"] <= 0:
        issues.append({
            "severity": "low",
            "code": "no_configurable_fields_detected",
            "message": "存在写入/编辑动作，但未暴露智能变量或环境字段。",
            "suggestion": "若用例包含可维护字段，应检查字段变量化和环境字段识别。",
        })
    return issues


def _alignment_score(checks: dict[str, Any], issues: list[dict[str, Any]]) -> int:
    score = 100
    for issue in issues:
        severity = issue.get("severity")
        if severity == "critical":
            score -= 30
        elif severity == "high":
            score -= 18
        elif severity == "medium":
            score -= 8
        elif severity == "low":
            score -= 3
    if checks["ir_step_count"] and checks["preview_step_count"]:
        coverage = min(1.0, checks["preview_step_count"] / max(1, checks["ir_step_count"]))
        if coverage < 0.4:
            score -= 8
    return max(0, min(100, score))


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


def _risk_level(score: int, issues: list[dict[str, Any]]) -> str:
    if any(issue.get("severity") in {"critical", "high"} for issue in issues) or score < 70:
        return "high"
    if any(issue.get("severity") == "medium" for issue in issues) or score < 85:
        return "medium"
    return "low"


def _summary(score: int, issues: list[dict[str, Any]]) -> str:
    if not issues:
        return f"{score} 分：IR 与主解析链路覆盖基本一致。"
    high = sum(1 for issue in issues if issue.get("severity") in {"critical", "high"})
    if high:
        return f"{score} 分：发现 {high} 个 IR/主链路覆盖高风险，请先确认。"
    return f"{score} 分：发现 {len(issues)} 个 IR/主链路覆盖提示项。"
