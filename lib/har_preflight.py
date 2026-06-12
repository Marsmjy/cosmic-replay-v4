"""HAR import preflight and pageId alignment scoring.

Scoring remains diagnostic, while deterministic generation blockers are
carried by the shared case generation gate. This module must not turn a low
score or a mapping heuristic alone into an unsafe hard block.
"""
from __future__ import annotations

from collections import Counter
from typing import Any

from lib.pageid_trace import (
    classify_pageid,
    expected_pageid_role,
    pageid_fragment,
    pageid_risks,
)
from lib.case_contract import build_scenario_contract
from lib.ir.write_contract import is_write_step


Issue = dict[str, Any]

_PAGEID_RISK_PENALTIES = {
    "har_l2_on_l3_step": 24,
    "runtime_l2_used_for_l3_step": 24,
    "runtime_l3_used_for_l2_step": 18,
    "missing_preserve_l2_page": 14,
    "pending_l2_for_l3_step": 12,
    "write_anchor_uses_l2_pageid": 24,
    "showform_billformid_not_followed": 10,
    "recorded_pageid_producer_filtered": 20,
    "recorded_pageid_source_form_mismatch": 20,
    "recorded_pageid_recovery_missing": 24,
    "pageid_producer_consumer_form_mismatch": 20,
    "pageid_reused_after_close": 24,
}

def assess_pageid_alignment(
    steps: list[dict[str, Any]],
    *,
    har_probe: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """Score static HAR pageId usage against replay expectations."""
    rows: list[dict[str, Any]] = []
    risks: Counter[str] = Counter()
    role_counts: Counter[str] = Counter()
    har_type_counts: Counter[str] = Counter()
    preserve_l2_count = 0

    for index, step in enumerate(steps or []):
        har_page_id = step.get("_har_page_id", "")
        har_type = classify_pageid(har_page_id)
        expected = expected_pageid_role(step)
        risk_codes = pageid_risks(step, har_page_id=har_page_id)
        if step.get("preserve_l2_page"):
            preserve_l2_count += 1
        role_counts[expected] += 1
        har_type_counts[har_type] += 1
        for code in risk_codes:
            risks[code] += 1
        if _is_interesting_pageid_step(step, expected, har_type, risk_codes):
            rows.append({
                "index": index,
                "step_id": step.get("id", ""),
                "form_id": step.get("form_id", ""),
                "app_id": step.get("app_id", ""),
                "type": step.get("type", ""),
                "ac": step.get("ac", ""),
                "method": step.get("method", ""),
                "expected_pageid_role": expected,
                "har_pageid_type": har_type,
                "har_pageid_fragment": pageid_fragment(har_page_id),
                "preserve_l2_page": bool(step.get("preserve_l2_page")),
                "risk_codes": risk_codes,
            })

    probe_risks = _probe_risk_counts(har_probe or {})
    for code, count in probe_risks.items():
        risks[code] += count

    issues = _pageid_issues(rows, risks, har_type_counts)
    score = _pageid_score(risks, har_type_counts, steps)
    return {
        "score": score,
        "grade": _grade(score),
        "risk_level": _pageid_risk_level(score, issues),
        "summary": _pageid_summary(score, issues),
        "issues": issues,
        "checks": {
            "total_steps": len(steps or []),
            "interesting_steps": len(rows),
            "preserve_l2_step_count": preserve_l2_count,
            "expected_role_counts": dict(sorted(role_counts.items())),
            "har_pageid_type_counts": dict(sorted(har_type_counts.items())),
            "risk_counts": dict(sorted(risks.items())),
        },
        "steps": rows[:80],
    }


def assess_har_preflight(
    *,
    main_form_id: str,
    tier_counts: dict[str, int],
    steps: list[dict[str, Any]],
    detected_vars: list[dict[str, Any]],
    pick_fields: list[dict[str, Any]],
    component_report: dict[str, Any] | None,
    quality: dict[str, Any] | None,
    pageid_alignment: dict[str, Any] | None,
    ir_alignment: dict[str, Any] | None = None,
    ir_field_bridge: dict[str, Any] | None = None,
    ir_interaction_bridge: dict[str, Any] | None = None,
    ir_write_bridge: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """Build a user-facing import preflight decision."""
    quality = quality or {}
    pageid_alignment = pageid_alignment or {}
    ir_alignment = ir_alignment or {}
    ir_field_bridge = ir_field_bridge or {}
    ir_interaction_bridge = ir_interaction_bridge or {}
    ir_write_bridge = ir_write_bridge or {}
    scenario = build_scenario_contract({"steps": steps or []})
    field_checks = ir_field_bridge.get("checks") or {}
    interaction_checks = ir_interaction_bridge.get("summary") or {}
    write_checks = ir_write_bridge.get("checks") or {}
    component_summary = (component_report or {}).get("summary") or {}
    checks = {
        "main_form_id": main_form_id,
        "core_count": int((tier_counts or {}).get("core") or 0),
        "ui_reaction_count": int((tier_counts or {}).get("ui_reaction") or 0),
        "noise_count": int((tier_counts or {}).get("noise") or 0),
        "step_count": len(steps or []),
        "persistence_step_count": sum(1 for step in steps or [] if _is_persistence_step(step)),
        "scenario_kind": scenario.get("kind", ""),
        "scenario_stages": list(scenario.get("stages") or []),
        "detected_var_count": len(detected_vars or []),
        "pick_field_count": len(pick_fields or []),
        "component_coverage_percent": int(component_summary.get("coverage_percent", 100) or 0),
        "component_unsupported_count": int(component_summary.get("unsupported_steps", 0) or 0),
        "quality_score": int(quality.get("score", 0) or 0),
        "pageid_score": int(pageid_alignment.get("score", 0) or 0),
        "ir_alignment_score": int(ir_alignment.get("score", 100) or 0),
        "ir_alignment_risk_level": ir_alignment.get("risk_level", ""),
        "ir_field_binding_score": int(ir_field_bridge.get("coverage_score", 100) or 0),
        "ir_field_action_count": int(field_checks.get("ir_field_action_count") or 0),
        "ir_field_action_uncovered_count": int(field_checks.get("uncovered_ir_field_action_count") or 0),
        "maintainable_field_bound_count": int(field_checks.get("bound_count") or 0),
        "maintainable_field_unbound_count": int(field_checks.get("unbound_count") or 0),
        "overridden_unbound_count": int(field_checks.get("overridden_unbound_count") or 0),
        "ir_interaction_count": int(interaction_checks.get("interaction_count") or 0),
        "ir_interaction_uncovered_count": int(interaction_checks.get("uncovered_count") or 0),
        "ir_interaction_high_risk_uncovered_count": int(
            interaction_checks.get("uncovered_high_risk_count") or 0
        ),
        "ir_write_anchor_count": int(write_checks.get("ir_write_anchor_count") or 0),
        "ir_write_anchor_uncovered_count": int(write_checks.get("uncovered_write_anchor_count") or 0),
        "ir_write_contract_missing_count": int(
            write_checks.get("critical_response_contract_missing_count") or 0
        ),
        "ir_write_l2_risk_count": int(write_checks.get("write_anchor_l2_risk_count") or 0),
        "ir_write_kind_mismatch_count": int(write_checks.get("write_kind_mismatch_count") or 0),
    }
    issues = _preflight_issues(
        checks,
        quality,
        pageid_alignment,
        ir_alignment,
        ir_field_bridge,
        ir_interaction_bridge,
        ir_write_bridge,
    )
    score = _preflight_score(checks, issues)
    decision = _preflight_decision(score, issues)
    return {
        "score": score,
        "grade": _grade(score),
        "decision": decision,
        "allow_generate": decision != "blocked",
        "recommend_generate": decision in {"ready", "review"},
        "summary": _preflight_summary(score, decision, issues),
        "issues": issues,
        "checks": checks,
        "next_actions": _preflight_next_actions(
            decision,
            issues,
            pageid_alignment,
            ir_alignment,
        ),
    }


def _is_interesting_pageid_step(
    step: dict[str, Any],
    expected: str,
    har_type: str,
    risk_codes: list[str],
) -> bool:
    if risk_codes:
        return True
    if step.get("preserve_l2_page"):
        return True
    if expected in {"L2", "L3", "L2_or_L3"}:
        return True
    return har_type not in {"missing", "unknown"}


def _probe_risk_counts(har_probe: dict[str, Any]) -> Counter[str]:
    counts: Counter[str] = Counter()
    for risk in har_probe.get("risks") or []:
        code = str(risk.get("code") or "")
        if code:
            counts[code] += 1
    return counts


def _pageid_issues(
    rows: list[dict[str, Any]],
    risks: Counter[str],
    har_type_counts: Counter[str],
) -> list[Issue]:
    issues: list[Issue] = []
    if not rows:
        issues.append({
            "severity": "medium",
            "code": "pageid_signal_missing",
            "message": "未发现可用于评分的 pageId 链路步骤。",
            "suggestion": "确认 HAR 是否包含 batchInvokeAction/pageId；若只录到静态资源，需要重新录制。",
        })
    for code, count in sorted(risks.items()):
        severity = "high" if code in {
            "har_l2_on_l3_step",
            "write_anchor_uses_l2_pageid",
            "recorded_pageid_producer_filtered",
            "recorded_pageid_source_form_mismatch",
            "recorded_pageid_recovery_missing",
            "pageid_producer_consumer_form_mismatch",
            "pageid_reused_after_close",
        } else "medium"
        issues.append({
            "severity": severity,
            "code": code,
            "message": _pageid_issue_message(code, count),
            "suggestion": _pageid_issue_suggestion(code),
        })
    if har_type_counts.get("L2", 0) == 0 and har_type_counts.get("compound_root", 0) == 0:
        issues.append({
            "severity": "medium",
            "code": "l2_context_not_observed",
            "message": "未观察到明确的菜单/列表 L2 pageId。",
            "suggestion": "如果该用例包含菜单、列表、树或新增动作，应优先确认录制链路是否从菜单入口开始。",
        })
    return issues


def _pageid_score(
    risks: Counter[str],
    har_type_counts: Counter[str],
    steps: list[dict[str, Any]],
) -> int:
    score = 100
    for code, count in risks.items():
        score -= _PAGEID_RISK_PENALTIES.get(code, 8) * count
    if steps and har_type_counts.get("missing", 0) >= len(steps) * 0.7:
        score -= 18
    if har_type_counts.get("L2", 0) == 0 and any(_expects_l2(step) for step in steps or []):
        score -= 12
    return max(0, min(100, score))


def _preflight_issues(
    checks: dict[str, Any],
    quality: dict[str, Any],
    pageid_alignment: dict[str, Any],
    ir_alignment: dict[str, Any] | None = None,
    ir_field_bridge: dict[str, Any] | None = None,
    ir_interaction_bridge: dict[str, Any] | None = None,
    ir_write_bridge: dict[str, Any] | None = None,
) -> list[Issue]:
    issues: list[Issue] = []
    ir_alignment = ir_alignment or {}
    ir_field_bridge = ir_field_bridge or {}
    ir_interaction_bridge = ir_interaction_bridge or {}
    ir_write_bridge = ir_write_bridge or {}
    if not checks["main_form_id"]:
        issues.append({
            "severity": "critical",
            "code": "main_form_missing",
            "message": "未识别到主业务表单。",
            "suggestion": "重新录制或补充主表单识别规则后再生成。",
        })
    if checks["core_count"] <= 0:
        issues.append({
            "severity": "critical",
            "code": "core_steps_missing",
            "message": "未识别到核心业务步骤。",
            "suggestion": "确认 HAR 包含打开、填写、选择、保存等业务请求。",
        })
    if checks["component_unsupported_count"] > 0:
        issues.append({
            "severity": "high" if checks["component_unsupported_count"] >= 3 else "medium",
            "code": "unsupported_components",
            "message": f"存在 {checks['component_unsupported_count']} 个未知组件步骤。",
            "suggestion": "先查看组件雷达，确认未知动作是噪声、必保留还是需要新增 handler。",
        })
    if checks["pageid_score"] < 70:
        issues.append({
            "severity": "high",
            "code": "pageid_alignment_low",
            "message": f"pageId 链路评分较低：{checks['pageid_score']}。",
            "suggestion": "优先检查 L2/L3 切换、保存是否误用 L2、showForm/target_forms 是否完整。",
        })
    elif checks["pageid_score"] < 85:
        issues.append({
            "severity": "medium",
            "code": "pageid_alignment_review",
            "message": f"pageId 链路需要复核：{checks['pageid_score']}。",
            "suggestion": "生成前建议查看 pageId 评分，避免执行期才暴露上下文丢失。",
        })
    for issue in (quality.get("issues") or [])[:5]:
        if issue.get("severity") in {"critical", "high"}:
            issues.append({
                "severity": issue.get("severity", "medium"),
                "code": f"quality_{issue.get('code', 'issue')}",
                "message": issue.get("message", ""),
                "suggestion": issue.get("suggestion", ""),
            })
    for issue in (pageid_alignment.get("issues") or [])[:5]:
        if issue.get("severity") == "high":
            issues.append({
                "severity": "high",
                "code": f"pageid_{issue.get('code', 'issue')}",
                "message": issue.get("message", ""),
                "suggestion": issue.get("suggestion", ""),
            })
    for issue in (ir_alignment.get("issues") or [])[:5]:
        severity = issue.get("severity", "medium")
        if severity in {"critical", "high", "medium"}:
            issues.append({
                "severity": severity,
                "code": f"ir_{issue.get('code', 'issue')}",
                "message": issue.get("message", ""),
                "suggestion": issue.get("suggestion", ""),
            })
    if checks.get("ir_alignment_score", 100) < 70:
        issues.append({
            "severity": "high",
            "code": "ir_alignment_low",
            "message": f"IR 覆盖雷达评分较低：{checks['ir_alignment_score']}。",
            "suggestion": "优先确认 IR 识别到的写入/编辑/L2 上下文是否已被主解析链路覆盖。",
        })
    elif checks.get("ir_alignment_score", 100) < 85:
        issues.append({
            "severity": "medium",
            "code": "ir_alignment_review",
            "message": f"IR 覆盖雷达需要复核：{checks['ir_alignment_score']}。",
            "suggestion": "生成前建议查看 IR 覆盖雷达，避免录制链路里有动作但 YAML 未覆盖。",
        })
    if checks.get("overridden_unbound_count", 0):
        issues.append({
            "severity": "critical",
            "code": "maintainable_value_unbound",
            "message": (
                f"有 {checks['overridden_unbound_count']} 个用户已修改字段"
                "没有绑定到可执行步骤。"
            ),
            "suggestion": "先修复字段解析/绑定规则，不能继续使用 HAR 旧值执行写入。",
        })
    elif ir_field_bridge.get("status") == "needs_review" and checks.get("ir_field_action_uncovered_count", 0):
        issues.append({
            "severity": "medium",
            "code": "ir_field_action_uncovered",
            "message": (
                f"有 {checks['ir_field_action_uncovered_count']} 个 HAR 字段动作"
                "尚未由生成步骤明确覆盖。"
            ),
            "suggestion": "检查普通录入、F7/基础资料和下拉动作是否进入字段目录及 YAML。",
        })
    if checks.get("ir_interaction_high_risk_uncovered_count", 0):
        issues.append({
            "severity": "high",
            "code": "ir_complex_interaction_uncovered",
            "message": (
                f"有 {checks['ir_interaction_high_risk_uncovered_count']} 个 F7/分录/"
                "子弹窗确认动作未进入生成 YAML。"
            ),
            "suggestion": "按 IR source_index/action_index 复核选择、分录维护和确认边界；只有确认证据缺失时才阻断生成。",
        })
    elif (
        ir_interaction_bridge.get("status") == "ready_with_warnings"
        and checks.get("ir_interaction_uncovered_count", 0)
    ):
        issues.append({
            "severity": "medium",
            "code": "ir_complex_interaction_review",
            "message": (
                f"有 {checks['ir_interaction_uncovered_count']} 个低风险复杂交互仅被部分覆盖。"
            ),
            "suggestion": "检查弹窗打开和选择器预取动作是否属于可忽略 UI 联动。",
        })
    if checks.get("ir_write_anchor_uncovered_count", 0):
        issues.append({
            "severity": "critical",
            "code": "ir_write_anchor_uncovered",
            "message": (
                f"有 {checks['ir_write_anchor_uncovered_count']} 个 HAR 写入动作"
                "没有进入生成的 YAML。"
            ),
            "suggestion": "先修复保存/提交/审核/确认动作识别，不能生成缺写入锚点的用例。",
        })
    if checks.get("ir_write_contract_missing_count", 0):
        issues.append({
            "severity": "critical",
            "code": "ir_write_contract_missing",
            "message": (
                f"有 {checks['ir_write_contract_missing_count']} 个写入锚点"
                "缺少录制响应语义契约。"
            ),
            "suggestion": "从 HAR 录制响应生成关键契约后再执行，不能只依赖 HTTP 200。",
        })
    if checks.get("ir_write_l2_risk_count", 0) or checks.get("ir_write_kind_mismatch_count", 0):
        issues.append({
            "severity": "high",
            "code": "ir_write_anchor_contract_risk",
            "message": (
                f"写入锚点存在 L2 风险 {checks.get('ir_write_l2_risk_count', 0)} 处，"
                f"动作类型不一致 {checks.get('ir_write_kind_mismatch_count', 0)} 处。"
            ),
            "suggestion": "核对原始 HAR 的表单作用域、L3 pageId 与保存/提交/审核动作类型。",
        })
    return _dedupe_issues(issues)


def _preflight_score(checks: dict[str, Any], issues: list[Issue]) -> int:
    quality_score = checks.get("quality_score", 0)
    pageid_score = checks.get("pageid_score", 0)
    component_score = checks.get("component_coverage_percent", 100)
    score = round(quality_score * 0.45 + pageid_score * 0.35 + component_score * 0.20)
    for issue in issues:
        if issue.get("severity") == "critical":
            score -= 18
        elif issue.get("severity") == "high":
            score -= 10
        elif issue.get("severity") == "medium":
            score -= 4
    return max(0, min(100, score))


def _preflight_decision(score: int, issues: list[Issue]) -> str:
    if any(issue.get("severity") == "critical" for issue in issues):
        return "blocked"
    if any(issue.get("severity") == "high" for issue in issues) or score < 70:
        return "risky"
    if score < 85 or any(issue.get("severity") == "medium" for issue in issues):
        return "review"
    return "ready"


def _preflight_next_actions(
    decision: str,
    issues: list[Issue],
    pageid_alignment: dict[str, Any],
    ir_alignment: dict[str, Any] | None = None,
) -> list[str]:
    actions = []
    ir_alignment = ir_alignment or {}
    if decision == "blocked":
        actions.append("先补齐主表单/核心业务步骤，再生成 YAML。")
    if any("pageid" in str(issue.get("code")) for issue in issues):
        actions.append("先看 pageId 链路评分：L2 列表/树/工具栏保留，字段/保存/提交切 L3。")
    if any(str(issue.get("code", "")).startswith("ir_") for issue in issues):
        actions.append("先看 IR 覆盖雷达：确认录制里的写入、编辑、L2 上下文都进入 YAML 主链路。")
    if any(issue.get("code") == "unsupported_components" for issue in issues):
        actions.append("打开组件雷达，确认未知组件是噪声、必保留还是需要新增 handler。")
    if pageid_alignment.get("risk_level") in {"high", "medium"}:
        actions.append("若生成后执行失败，先对比 HAR 原始 pageId 与 runner pageid_trace。")
    if ir_alignment.get("risk_level") in {"high", "medium"}:
        actions.append("若执行漏写或只入库部分数据，优先对照 IR 覆盖雷达定位缺失 step。")
    if not actions:
        actions.append("可生成 YAML；执行后按入库回查断言确认真实写入。")
    return _dedupe_strings(actions)


def _is_persistence_step(step: dict[str, Any]) -> bool:
    return is_write_step(step)


def _expects_l2(step: dict[str, Any]) -> bool:
    return expected_pageid_role(step) == "L2"


def _pageid_issue_message(code: str, count: int) -> str:
    messages = {
        "missing_preserve_l2_page": "发现 L2 步骤未显式 preserve_l2_page。",
        "har_l2_on_l3_step": "发现真实编辑/保存类步骤携带 L2 pageId。",
        "write_anchor_uses_l2_pageid": "HAR 链路中写入锚点疑似使用 L2 pageId。",
        "showform_billformid_not_followed": "showForm 的 billFormId 后续未被请求跟随。",
        "runtime_l3_used_for_l2_step": "列表/树/工具栏步骤可能过早切到 L3。",
        "recorded_pageid_producer_filtered": "HAR 中产生该 pageId 的前置请求在生成 YAML 时被过滤。",
        "recorded_pageid_source_form_mismatch": "HAR 中 pageId 生产者与消费者的表单作用域不一致。",
        "recorded_pageid_recovery_missing": "HAR 中 L3 pageId 生产者被过滤，且未生成安全恢复策略。",
        "pageid_producer_consumer_form_mismatch": "HAR 原始 pageId 精确关联跨越了不匹配的表单作用域。",
        "pageid_reused_after_close": "HAR 在关闭窗口后仍继续使用同一个 pageId。",
    }
    return f"{messages.get(code, '发现 pageId 链路风险。')}（{count} 处）"


def _pageid_issue_suggestion(code: str) -> str:
    suggestions = {
        "missing_preserve_l2_page": "在 HAR 解析时为菜单/列表/树/工具栏步骤保留 L2，不要过早切 L3。",
        "har_l2_on_l3_step": "确认该步骤是否只是工具栏桥接；真实字段更新和保存应使用 L3。",
        "write_anchor_uses_l2_pageid": "优先比对原始 HAR 与回放 trace，不要通过硬补 save.post_data 绕过。",
        "showform_billformid_not_followed": "检查 showForm/billFormId 别名绑定和 target_forms 是否完整。",
        "runtime_l3_used_for_l2_step": "列表/树/工具栏动作应继续保留 L2，避免影响 addnew 前置上下文。",
        "recorded_pageid_producer_filtered": "保留产生 L3 的 showForm/addVirtualTab/clientCallBack 前置步骤，或为可选导航步骤设置 harvested L3 守卫。",
        "recorded_pageid_source_form_mismatch": "按 showForm 的 formId/billFormId 重新绑定消费者，避免跨表单复用 L3。",
        "recorded_pageid_recovery_missing": "保留对应 showForm/addVirtualTab，或为该核心步骤生成同表单 L3 复核策略。",
        "pageid_producer_consumer_form_mismatch": "检查 showForm/formId/billFormId 别名和 target_forms，确保 pageId 只进入对应表单。",
        "pageid_reused_after_close": "关闭弹窗后必须等待新的 showForm pageId，不能复用已关闭窗口的旧值。",
    }
    return suggestions.get(code, "优先检查 pageId 链路，再看字段解析和断言。")


def _preflight_summary(score: int, decision: str, issues: list[Issue]) -> str:
    label = {
        "ready": "适合直接生成",
        "review": "可生成但建议确认",
        "risky": "有高风险，建议先处理",
        "blocked": "阻断风险，暂不建议生成",
    }.get(decision, "待确认")
    high = sum(1 for issue in issues if issue.get("severity") in {"critical", "high"})
    if high:
        return f"{score} 分：{label}，发现 {high} 个高风险/阻断项。"
    if issues:
        return f"{score} 分：{label}，建议关注 {len(issues)} 个提示项。"
    return f"{score} 分：{label}，HAR 结构、pageId 和组件覆盖较稳。"


def _pageid_summary(score: int, issues: list[Issue]) -> str:
    if any(issue.get("severity") == "high" for issue in issues):
        return f"{score} 分：pageId 链路存在高风险，优先检查 L2/L3 切换。"
    if issues:
        return f"{score} 分：pageId 链路可用，但建议复核提示项。"
    return f"{score} 分：pageId 链路结构稳定。"


def _pageid_risk_level(score: int, issues: list[Issue]) -> str:
    if any(issue.get("severity") == "high" for issue in issues) or score < 70:
        return "high"
    if issues or score < 85:
        return "medium"
    return "low"


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


def _dedupe_issues(issues: list[Issue]) -> list[Issue]:
    seen: set[tuple[str, str]] = set()
    out = []
    for issue in issues:
        key = (str(issue.get("code") or ""), str(issue.get("message") or ""))
        if key in seen:
            continue
        seen.add(key)
        out.append(issue)
    return out


def _dedupe_strings(items: list[str]) -> list[str]:
    seen: set[str] = set()
    out = []
    for item in items:
        if item and item not in seen:
            seen.add(item)
            out.append(item)
    return out
