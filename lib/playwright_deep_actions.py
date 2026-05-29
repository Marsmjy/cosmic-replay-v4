"""Risk guardrails for deeper Playwright action capture.

The actual browser automation can be flexible, but every generated action plan
must pass these guards before it clicks write/workflow buttons.
"""
from __future__ import annotations

import random
import re
from collections import Counter
from datetime import datetime
from typing import Any

WRITE_CONFIRM_TOKEN = "YES_GENERATE_TEST_DATA"
WORKFLOW_CONFIRM_TOKEN = "YES_SUBMIT_OR_AUDIT_TEST_DATA"

_FORBIDDEN_KEYWORDS = (
    "删除",
    "反审核",
    "反提交",
    "作废",
    "撤销",
    "退回",
    "批量",
    "导入",
    "上传",
    "下载模板",
    "失效",
    "禁用",
)

_WORKFLOW_KEYWORDS = (
    "提交",
    "审核",
    "审批",
    "生效",
    "确认入职",
    "确认离职",
)

_WRITE_KEYWORDS = (
    "新增",
    "新建",
    "保存",
    "确定",
    "确认",
    "添加",
    "维护",
)


def classify_action_risk(action: dict[str, Any] | str) -> str:
    """Classify a planned UI action as read/write/workflow/forbidden."""
    if isinstance(action, str):
        text = action
        action_type = "click_text"
        declared_risk = ""
    else:
        text = str(action.get("text") or action.get("label") or action.get("selector") or "")
        action_type = str(action.get("type") or "")
        declared_risk = str(action.get("risk") or "")
    if action_type in {"wait", "screenshot", "noop", "open_home", "snapshot_controls"}:
        return "read"
    if action_type in {"wait_for_selector"}:
        return "read"
    if action_type in {"fill", "fill_text", "fill_at", "select", "set_value"}:
        return "write"
    if action_type in {"select_option", "press"}:
        return "write"
    if any(keyword in text for keyword in _FORBIDDEN_KEYWORDS):
        return "forbidden"
    if any(keyword in text for keyword in _WORKFLOW_KEYWORDS):
        return "workflow"
    if any(keyword in text for keyword in _WRITE_KEYWORDS):
        return "write"
    if action_type in {"click_selector", "click_locator", "click_at"}:
        if declared_risk in {"read", "write", "workflow"}:
            return declared_risk
        return "write"
    return "read"


def validate_action_plan(
    plan: dict[str, Any],
    *,
    confirm_write: str = "",
    confirm_workflow: str = "",
) -> dict[str, Any]:
    """Return validation result; does not mutate the plan."""
    actions = plan.get("actions") or []
    if not isinstance(actions, list):
        return {"ok": False, "errors": ["actions_must_be_list"], "summary": {}}

    errors: list[str] = []
    risk_counts: Counter[str] = Counter()
    owned_test_data = bool(plan.get("owned_test_data"))
    test_prefix = str(plan.get("test_prefix") or "")

    for index, action in enumerate(actions):
        if not isinstance(action, dict):
            errors.append(f"action_{index + 1}_must_be_object")
            continue
        risk = classify_action_risk(action)
        risk_counts[risk] += 1
        action_id = action.get("id") or action.get("text") or action.get("label") or f"action_{index + 1}"
        if risk == "forbidden":
            errors.append(f"{action_id}: forbidden_action")
        elif risk == "write" and confirm_write != WRITE_CONFIRM_TOKEN:
            errors.append(f"{action_id}: write_requires_{WRITE_CONFIRM_TOKEN}")
        elif risk == "workflow":
            if confirm_write != WRITE_CONFIRM_TOKEN:
                errors.append(f"{action_id}: workflow_requires_{WRITE_CONFIRM_TOKEN}")
            if confirm_workflow != WORKFLOW_CONFIRM_TOKEN:
                errors.append(f"{action_id}: workflow_requires_{WORKFLOW_CONFIRM_TOKEN}")
            if not owned_test_data:
                errors.append(f"{action_id}: workflow_requires_owned_test_data")
    if risk_counts.get("write", 0) or risk_counts.get("workflow", 0):
        if not test_prefix.startswith("CRPLY_"):
            errors.append("write_or_workflow_requires_CRPLY_test_prefix")

    return {
        "ok": not errors,
        "errors": errors,
        "summary": {
            "action_count": len(actions),
            "risk_counts": dict(sorted(risk_counts.items())),
            "owned_test_data": owned_test_data,
            "test_prefix": test_prefix,
        },
    }


def render_action_value(value: Any, *, now: datetime | None = None) -> Any:
    """Render safe placeholders used by deep Playwright action plans."""
    if not isinstance(value, str):
        return value
    current = now or datetime.now()
    rendered = value.replace("${timestamp}", current.strftime("%Y%m%d%H%M%S"))
    rendered = rendered.replace("${today}", current.strftime("%Y-%m-%d"))

    def _rand(match: re.Match[str]) -> str:
        length = int(match.group(1))
        length = max(1, min(length, 18))
        return "".join(str(random.randint(0, 9)) for _ in range(length))

    return re.sub(r"\$\{rand:(\d+)\}", _rand, rendered)
