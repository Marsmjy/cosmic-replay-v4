"""HAR import preflight and pageId alignment scoring.

These helpers are diagnostic-only. They do not rewrite preview steps or YAML;
they give the Web UI and AI evidence an earlier answer to: is this HAR complete
enough to generate, and is the pageId chain likely to replay correctly?
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


Issue = dict[str, Any]

_PAGEID_RISK_PENALTIES = {
    "har_l2_on_l3_step": 24,
    "runtime_l2_used_for_l3_step": 24,
    "runtime_l3_used_for_l2_step": 18,
    "missing_preserve_l2_page": 14,
    "pending_l2_for_l3_step": 12,
    "write_anchor_uses_l2_pageid": 24,
    "showform_billformid_not_followed": 10,
}

_PERSISTENCE_ACS = {
    "save",
    "saveandeffect",
    "submit",
    "submitandeffect",
    "audit",
    "unaudit",
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
) -> dict[str, Any]:
    """Build a user-facing import preflight decision."""
    quality = quality or {}
    pageid_alignment = pageid_alignment or {}
    ir_alignment = ir_alignment or {}
    component_summary = (component_report or {}).get("summary") or {}
    checks = {
        "main_form_id": main_form_id,
        "core_count": int((tier_counts or {}).get("core") or 0),
        "ui_reaction_count": int((tier_counts or {}).get("ui_reaction") or 0),
        "noise_count": int((tier_counts or {}).get("noise") or 0),
        "step_count": len(steps or []),
        "persistence_step_count": sum(1 for step in steps or [] if _is_persistence_step(step)),
        "detected_var_count": len(detected_vars or []),
        "pick_field_count": len(pick_fields or []),
        "component_coverage_percent": int(component_summary.get("coverage_percent", 100) or 0),
        "component_unsupported_count": int(component_summary.get("unsupported_steps", 0) or 0),
        "quality_score": int(quality.get("score", 0) or 0),
        "pageid_score": int(pageid_alignment.get("score", 0) or 0),
        "ir_alignment_score": int(ir_alignment.get("score", 100) or 0),
        "ir_alignment_risk_level": ir_alignment.get("risk_level", ""),
    }
    issues = _preflight_issues(checks, quality, pageid_alignment, ir_alignment)
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
        "next_actions": _preflight_next_actions(decision, issues, pageid_alignment, ir_alignment),
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
        severity = "high" if code in {"har_l2_on_l3_step", "write_anchor_uses_l2_pageid"} else "medium"
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
) -> list[Issue]:
    issues: list[Issue] = []
    ir_alignment = ir_alignment or {}
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
    if checks["persistence_step_count"] <= 0:
        issues.append({
            "severity": "high",
            "code": "persistence_step_missing",
            "message": "未识别到保存/提交/确认类写入动作。",
            "suggestion": "如果目标是写库用例，需要重新录制保存/提交动作；只读用例可忽略。",
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
    ac = str(step.get("ac") or "").lower()
    method = str(step.get("method") or "").lower()
    key = str(step.get("key") or "").lower()
    args = " ".join(str(item).lower() for item in (step.get("args") or []))
    if ac in _PERSISTENCE_ACS:
        return True
    blob = " ".join([ac, method, key, args])
    return any(token in blob for token in ("save", "submit", "audit", "confirm", "btnok"))


def _expects_l2(step: dict[str, Any]) -> bool:
    return expected_pageid_role(step) == "L2"


def _pageid_issue_message(code: str, count: int) -> str:
    messages = {
        "missing_preserve_l2_page": "发现 L2 步骤未显式 preserve_l2_page。",
        "har_l2_on_l3_step": "发现真实编辑/保存类步骤携带 L2 pageId。",
        "write_anchor_uses_l2_pageid": "HAR 链路中写入锚点疑似使用 L2 pageId。",
        "showform_billformid_not_followed": "showForm 的 billFormId 后续未被请求跟随。",
        "runtime_l3_used_for_l2_step": "列表/树/工具栏步骤可能过早切到 L3。",
    }
    return f"{messages.get(code, '发现 pageId 链路风险。')}（{count} 处）"


def _pageid_issue_suggestion(code: str) -> str:
    suggestions = {
        "missing_preserve_l2_page": "在 HAR 解析时为菜单/列表/树/工具栏步骤保留 L2，不要过早切 L3。",
        "har_l2_on_l3_step": "确认该步骤是否只是工具栏桥接；真实字段更新和保存应使用 L3。",
        "write_anchor_uses_l2_pageid": "优先比对原始 HAR 与回放 trace，不要通过硬补 save.post_data 绕过。",
        "showform_billformid_not_followed": "检查 showForm/billFormId 别名绑定和 target_forms 是否完整。",
        "runtime_l3_used_for_l2_step": "列表/树/工具栏动作应继续保留 L2，避免影响 addnew 前置上下文。",
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
