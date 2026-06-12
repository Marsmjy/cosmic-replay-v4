"""Confidence scoring and dependency detection for normalized entries."""
from __future__ import annotations

from collections import Counter
import copy
from typing import Any

from lib.pageid_trace import expected_pageid_role
from .write_contract import classify_write_operation


def enrich_entries(entries: list[dict[str, Any]], *, playwright_context: dict[str, Any] | None = None) -> dict[str, Any]:
    pages: list[dict[str, Any]] = []
    steps: list[dict[str, Any]] = []
    units: list[dict[str, Any]] = []
    dependencies: list[dict[str, Any]] = []
    confidence_values: list[float] = []
    form_counts = Counter(str((entry.get("signals") or {}).get("form_id") or "") for entry in entries)

    previous_step_id = ""
    for entry in entries:
        actions = entry.get("actions") if isinstance(entry.get("actions"), list) else []
        action_units = actions or [None]
        for action_index, action in enumerate(action_units):
            signals = copy.deepcopy(entry.get("signals") or {})
            if isinstance(action, dict):
                signals["method"] = str(action.get("methodName") or action.get("method") or signals.get("method") or "")
                signals["key"] = str(action.get("key") or "")
            source_index = int(entry.get("index", len(steps)))
            step_id = _step_id(source_index, action_index, len(action_units))
            role = _step_role(signals, action=action)
            expected_role = expected_pageid_role({
                "type": "invoke",
                "ac": signals.get("ac", ""),
                "method": signals.get("method", ""),
                "key": signals.get("key", ""),
                "args": action.get("args", []) if isinstance(action, dict) else [],
            })
            confidence = score_detection(
                entry,
                expected_role=expected_role,
                form_stability=form_counts.get(signals.get("form_id", ""), 0),
                has_playwright_context=bool(playwright_context),
                has_previous_dependency=bool(previous_step_id),
            )
            confidence_values.append(confidence)
            pages.append({
                "id": f"page_{step_id}",
                "form_id": signals.get("form_id", ""),
                "app_id": signals.get("app_id", ""),
                "pageid": "${PAGE_ID}" if signals.get("has_pageid") else "",
                "pageid_type": signals.get("pageid_type", "missing"),
                "pageid_fragment": signals.get("pageid_fragment", ""),
                "expected_role": expected_role,
                "source": "HAR",
                "source_index": source_index,
                "action_index": action_index,
                "confidence_score": confidence,
            })
            steps.append({
                "id": step_id,
                "role": role,
                "source": "HAR",
                "source_index": source_index,
                "action_index": action_index,
                "request_ref": f"req_{step_id}",
                "response_ref": f"resp_{step_id}",
                "page_ref": f"page_{step_id}",
                "confidence_score": confidence,
            })
            units.append({
                "entry": entry,
                "action": action if isinstance(action, dict) else {},
                "signals": signals,
                "source_index": source_index,
                "action_index": action_index,
            })
            if previous_step_id:
                dependencies.append({
                    "from": previous_step_id,
                    "to": step_id,
                    "kind": "sequence",
                    "required": role in {"write", "edit", "navigation"},
                    "confidence_score": min(confidence, 0.85),
                })
            previous_step_id = step_id

    return {
        "pages": pages,
        "steps": steps,
        "units": units,
        "dependencies": dependencies,
        "confidence_score": round(sum(confidence_values) / len(confidence_values), 3) if confidence_values else 0.0,
    }


def score_detection(
    entry: dict[str, Any],
    *,
    expected_role: str,
    form_stability: int = 0,
    has_playwright_context: bool = False,
    has_previous_dependency: bool = False,
) -> float:
    signals = entry.get("signals") or {}
    response = entry.get("response") or {}
    score = 0
    if entry.get("url_shape") in {"/form/batchInvokeAction.do", "/metadata/getEntityType.do"}:
        score += 15
    if signals.get("form_id") or signals.get("app_id") or signals.get("ac") or signals.get("method"):
        score += 20
    if signals.get("has_pageid"):
        score += 10
    if response.get("has_pageid") or response.get("write_refs"):
        score += 20
    if has_previous_dependency or expected_role in {"L2", "L3"}:
        score += 15
    if has_playwright_context:
        score += 10
    if form_stability > 1:
        score += 5
    if signals.get("pageid_type") != "missing":
        score += 5
    return round(min(score, 100) / 100, 3)


def _step_id(source_index: int, action_index: int, action_count: int) -> str:
    base = f"step_{source_index + 1}"
    return f"{base}_{action_index + 1}" if action_count > 1 else base


def _step_role(signals: dict[str, Any], *, action: dict[str, Any] | None = None) -> str:
    ac = str(signals.get("ac") or "").lower()
    method = str(signals.get("method") or "").lower()
    key = str(signals.get("key") or "").lower()
    if classify_write_operation(
        ac=ac,
        method=method,
        key=key,
        args=(action or {}).get("args") if isinstance(action, dict) else None,
    ):
        return "write"
    if ac in {"menuitemclick", "treemenuclick", "loadData".lower(), "refresh", "querytreenodechildren"}:
        return "navigation"
    if method in {
        "menuitemclick",
        "treemenuclick",
        "loaddata",
        "refresh",
        "querytreenodechildren",
        "postexpandnodes",
        "entryrowclick",
    }:
        return "navigation"
    if ac in {"updatevalue", "setitembyidfromclient"} or method in {"updatevalue", "setitembyidfromclient"}:
        return "edit"
    return "action"


def _action_semantic_text(action: dict[str, Any] | None) -> str:
    if not isinstance(action, dict):
        return ""
    tokens: list[str] = []
    for value in action.get("args") or []:
        if isinstance(value, str):
            lowered = value.strip().lower()
            if any(marker in lowered for marker in (
                "save",
                "submit",
                "audit",
                "confirm",
                "approve",
                "reject",
                "btnok",
                "startupflow",
            )):
                tokens.append(lowered)
    return " ".join(tokens)
