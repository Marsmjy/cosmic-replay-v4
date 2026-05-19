"""执行失败自动归因。

把 run_case 中分散的 step_fail/assertion_fail 转成稳定分类，便于前端展示、
批量报告聚合，以及后续自动修复。
"""
from __future__ import annotations

import re
from typing import Any


_TRANSIENT_PATTERNS = (
    "HTTP 502",
    "HTTP 503",
    "HTTP 504",
    "Bad Gateway",
    "Gateway Timeout",
    "Read timed out",
    "Connection aborted",
    "请求超时",
)

_PAGE_ID_PATTERNS = (
    "页面未初始化或者已经过期",
    "获取缓存连接客户端失败",
    "pageId",
    "pageid",
    "表单会话超时",
)

_SERVICE_PATTERNS = (
    "未发现AppIdName",
    "服务或访问服务网络异常",
    "FormService",
    "错误码:1002",
)

_MISSING_PATTERNS = (
    "请填写",
    "请选择",
    "不能为空",
    "必填",
)

_DUPLICATE_PATTERNS = (
    "已存在",
    "重复",
    "唯一",
)

_FORMAT_PATTERNS = (
    "格式不正确",
    "格式错误",
    "不合法",
    "不允许",
    "超出",
)


def classify_run_failure(
    steps: list[dict],
    assertions: list[dict],
    case: dict | None = None,
) -> dict[str, Any]:
    """归因一次执行失败。"""
    case = case or {}
    failures = []
    for step in steps or []:
        if step.get("ok"):
            continue
        if step.get("optional"):
            continue
        failures.append({
            "source": "step",
            "step_id": step.get("id", ""),
            "step_type": step.get("type", ""),
            "form_id": step.get("form_id", ""),
            "error": step.get("error") or "; ".join(step.get("_errors") or []),
            "raw": step,
        })

    for assertion in assertions or []:
        if assertion.get("ok"):
            continue
        failures.append({
            "source": "assertion",
            "step_id": assertion.get("step", ""),
            "step_type": "assertion",
            "form_id": "",
            "error": assertion.get("msg", ""),
            "raw": assertion,
        })

    if not failures:
        return {
            "category": "unknown",
            "severity": "low",
            "retryable": False,
            "confidence": "low",
            "root_cause": "未捕获到明确的失败步骤。",
            "evidence": "",
            "recommended_actions": ["查看完整 run_history，确认是否为断言外异常或日志截断。"],
            "failures": [],
        }

    primary = classify_error(
        failures[0].get("error", ""),
        step=failures[0].get("raw") or {},
        case=case,
    )
    category_counts: dict[str, int] = {}
    classified_failures = []
    for failure in failures:
        item = classify_error(
            failure.get("error", ""),
            step=failure.get("raw") or {},
            case=case,
        )
        category_counts[item["category"]] = category_counts.get(item["category"], 0) + 1
        classified_failures.append({**failure, "analysis": item})

    return {
        **primary,
        "category_counts": category_counts,
        "failures": classified_failures,
    }


def classify_error(error: str, step: dict | None = None, case: dict | None = None) -> dict[str, Any]:
    """归因单条错误文本。"""
    step = step or {}
    case = case or {}
    text = str(error or "")
    form_id = str(step.get("form_id") or "")
    step_id = str(step.get("id") or "")
    main_form = str(case.get("main_form_id") or "")

    if _contains(text, _TRANSIENT_PATTERNS):
        return _result(
            "transient_protocol",
            "medium",
            True,
            "协议层瞬态错误，通常与网关、服务短暂抖动或网络超时有关。",
            text,
            [
                "优先重试该用例；runner 已对 HTTP 502/503/504 做自动重试。",
                "如果同一步连续失败，再检查目标表单服务和网络链路。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _SERVICE_PATTERNS):
        if _is_navigation_form(form_id, main_form):
            return _result(
                "navigation_service_unavailable",
                "medium",
                False,
                "非主业务导航表单服务不可达。",
                text,
                [
                    "该类 apphome/侧栏/快捷卡片步骤应保持 optional，不应阻断主表单写库。",
                    "如果失败步骤不是 optional，重新导入 HAR 以应用导航降级规则。",
                ],
                step_id=step_id,
                form_id=form_id,
                confidence="high",
            )
        return _result(
            "environment_service_unavailable",
            "high",
            False,
            "主业务或必需应用服务不可达。",
            text,
            [
                "确认当前环境已启用对应 AppIdName 服务，并且账号有访问权限。",
                "如果这是菜单入口后的目标表单，检查 target_form/target_forms 是否正确生成。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _PAGE_ID_PATTERNS):
        return _result(
            "pageid_context",
            "high",
            True,
            "PageId 或表单上下文失效。",
            text,
            [
                "检查 menuItemClick 是否带 target_form/target_forms。",
                "检查 HAR 导入是否保留列表/树到主表单的上下文桥接步骤。",
                "若是保存后继续操作同一页面，确认 keep_page 或重新 open/load 策略。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if "无根组织" in text or "根组织初始化" in text:
        return _result(
            "environment_business_prerequisite",
            "high",
            False,
            "当前环境缺少组织管理根行政组织初始化，新增行政组织被业务规则拦截。",
            text,
            [
                "先在目标环境执行组织管理根行政组织生成调度计划，或选择已完成根组织初始化的数据中心。",
                "不要通过删除 addnew/menuItemClick 或保存断言绕过该问题；根组织未初始化时后续字段会级联缺失。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="high",
        )

    if _contains(text, _MISSING_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_missing_required",
            "high",
            False,
            f"业务必填字段缺失{f'：{field}' if field else ''}。",
            text,
            [
                "检查该字段是否被 HAR 解析成 update_fields 或 pick_basedata。",
                "若字段由浏览器上下文带出，需补充上下文字段规则或 pick_fields 环境配置。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="medium",
        )

    if _contains(text, _DUPLICATE_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_duplicate",
            "medium",
            False,
            f"唯一字段重复{f'：{field}' if field else ''}。",
            text,
            [
                "检查编号、名称、手机号、邮箱等字段是否已抽成 ${vars.*}。",
                "为重复字段模板加入 ${rand:N} 或 ${timestamp}。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="high",
        )

    if _contains(text, _FORMAT_PATTERNS):
        field = _extract_field_caption(text)
        return _result(
            "business_invalid_value",
            "medium",
            False,
            f"字段值不符合业务约束{f'：{field}' if field else ''}。",
            text,
            [
                "检查 vars 模板、pick_fields 环境值和字段格式限制。",
                "如果错误提示包含不允许字符，优先调整随机前缀或固定模板。",
            ],
            step_id=step_id,
            form_id=form_id,
            field_caption=field,
            confidence="medium",
        )

    if "找不到步骤" in text:
        return _result(
            "assertion_anchor_missing",
            "medium",
            False,
            "断言挂靠的保存/提交步骤未执行到。",
            text,
            [
                "先查看更早的 step_fail，它通常才是真正根因。",
                "如果 YAML 被手工改名，更新 assertions 中的 step。",
            ],
            step_id=step_id,
            form_id=form_id,
            confidence="medium",
        )

    return _result(
        "unknown",
        "low",
        False,
        "暂未匹配到已知失败模式。",
        text,
        ["保留 run_history 和失败响应，按错误文本补充 failure_analysis 规则。"],
        step_id=step_id,
        form_id=form_id,
        confidence="low",
    )


def _result(
    category: str,
    severity: str,
    retryable: bool,
    root_cause: str,
    evidence: str,
    recommended_actions: list[str],
    *,
    step_id: str = "",
    form_id: str = "",
    field_caption: str = "",
    confidence: str = "medium",
) -> dict[str, Any]:
    return {
        "category": category,
        "severity": severity,
        "retryable": retryable,
        "confidence": confidence,
        "root_cause": root_cause,
        "evidence": evidence[:800],
        "recommended_actions": recommended_actions,
        "step_id": step_id,
        "form_id": form_id,
        "field_caption": field_caption,
    }


def _contains(text: str, patterns: tuple[str, ...]) -> bool:
    return any(pattern in text for pattern in patterns)


def _is_navigation_form(form_id: str, main_form: str) -> bool:
    if not form_id or form_id == main_form:
        return False
    return (
        form_id.endswith("_apphome")
        or form_id.startswith("bos_card_")
        or form_id.startswith("gbs_bgtask")
        or form_id in {"hom_wbcalendar", "hom_wbwaitin", "hom_wbwarning"}
    )


def _extract_field_caption(text: str) -> str:
    patterns = (
        r'请(?:填写|输入|录入|选择)\s*[""“”「」]?([^""“”「」，。；\s]+)',
        r'[""“”「」]?([^""“”「」]+?)[""“”「」]?\s*不能为空',
        r'[""“”「」]?([^""“”「」]+?)[""“”「」]?\s*(?:已存在|重复)',
    )
    for pattern in patterns:
        m = re.search(pattern, text)
        if m:
            return m.group(1).strip()
    return ""
