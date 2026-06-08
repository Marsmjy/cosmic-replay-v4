"""PageId chain diagnostics for HAR import and replay.

The trace is intentionally value-safe by default: full pageId values are not
required for regression snapshots. Evidence packages may include short
fragments so an agent can compare L2/L3 transitions without leaking entire
session identifiers.
"""
from __future__ import annotations

import json
import re
from typing import Any


_ROOT_RE = re.compile(r"^root[0-9a-f]{32}$")
_HEX32_RE = re.compile(r"^[0-9a-f]{32}$")
_UUID_RE = re.compile(r"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
_NUMERIC_L2_RE = re.compile(r"^\d+root[0-9a-f]{32}$")
_ROOT_WITH_BASE_RE = re.compile(r"root[0-9a-f]{32}")

_L2_CONTEXT_ACS = {
    "addnew",
    "itemClick",
    "loadData",
    "treeNodeClick",
    "treeMenuClick",
    "postExpandNodes",
    "queryTreeNodeChildren",
    "entryRowClick",
    "refresh",
}

_EDIT_STEP_TYPES = {"update_fields", "pick_basedata"}
_WRITE_ACS = {
    "click",
    "save",
    "submit",
    "saveandeffect",
    "submitandeffect",
    "saveandaudit",
}

_WRITE_KEYS = {
    "btnsave",
    "btn_save",
    "bar_save",
    "barsave",
    "new_save",
    "btn_confirm",
    "btnconfirm",
    "bar_confirm",
    "barconfirm",
    "btnok",
    "btn_ok",
    "bar_submit",
    "barsubmit",
}


def classify_pageid(value: Any) -> str:
    """Classify a pageId string into the project's L0/L2/L3 diagnostic buckets."""
    pid = str(value or "").strip()
    if not pid:
        return "missing"
    if _ROOT_RE.match(pid):
        return "L0"
    if _NUMERIC_L2_RE.match(pid):
        return "L2"
    if _HEX32_RE.match(pid) or _UUID_RE.match(pid):
        return "L1_or_L3"
    if _ROOT_WITH_BASE_RE.search(pid):
        return "compound_root"
    return "unknown"


def pageid_fragment(value: Any) -> str:
    """Return a short fragment that is useful in evidence but safe for reports."""
    pid = str(value or "").strip()
    if not pid:
        return ""
    if len(pid) <= 24:
        return pid
    return f"{pid[:14]}...{pid[-8:]}"


def extract_response_pageid_producers(response: Any) -> list[dict[str, Any]]:
    """Extract exact recorded pageId producers for in-memory correlation.

    Returned ``page_id`` values are internal-only. Callers must strip them
    before writing reports, YAML, baselines, or evidence packages.
    """
    if isinstance(response, str):
        try:
            response = json.loads(response)
        except Exception:
            return []

    producers: list[dict[str, Any]] = []
    seen: set[tuple[str, str, tuple[str, ...], str]] = set()

    def scoped_descendant_metadata(node: Any, owner_page_id: str) -> tuple[list[str], str]:
        forms: list[str] = []
        app_id = ""

        def collect(value: Any, *, root: bool = False) -> None:
            nonlocal app_id
            if isinstance(value, list):
                for item in value:
                    collect(item)
                return
            if not isinstance(value, dict):
                return
            nested_page_id = str(value.get("pageId") or "").strip()
            if not root and nested_page_id and nested_page_id != owner_page_id:
                return
            for key in ("formId", "billFormId"):
                form_id = str(value.get(key) or "").strip()
                if form_id and form_id not in forms:
                    forms.append(form_id)
            if not app_id:
                app_id = str(value.get("appId") or "").strip()
            for child in value.values():
                if isinstance(child, (dict, list)):
                    collect(child)

        def visit(value: Any) -> None:
            if isinstance(value, list):
                for item in value:
                    visit(item)
                return
            if not isinstance(value, dict):
                return
            nested_page_id = str(value.get("pageId") or "").strip()
            if nested_page_id and nested_page_id != owner_page_id:
                return
            if str(value.get("a") or "") in {"activate", "showForm"}:
                collect(value, root=True)
            for child in value.values():
                if isinstance(child, (dict, list)):
                    visit(child)

        visit(node)
        return forms, app_id

    def append(page_id: Any, *, kind: str, forms: list[Any] | None = None, app_id: Any = "") -> None:
        pid = str(page_id or "").strip()
        if classify_pageid(pid) in {"missing", "unknown"}:
            return
        form_ids = tuple(sorted({
            str(form or "").strip()
            for form in (forms or [])
            if str(form or "").strip()
        }))
        app = str(app_id or "").strip()
        key = (pid, kind, form_ids, app)
        if key in seen:
            return
        seen.add(key)
        producers.append({
            "page_id": pid,
            "pageid_type": classify_pageid(pid),
            "producer_kind": kind,
            "form_ids": list(form_ids),
            "app_id": app,
        })

    def walk(obj: Any) -> None:
        if isinstance(obj, list):
            for item in obj:
                walk(item)
            return
        if not isinstance(obj, dict):
            return

        action = str(obj.get("a") or "")
        if action == "showForm":
            for item in obj.get("p") or []:
                if not isinstance(item, dict):
                    continue
                append(
                    item.get("pageId"),
                    kind="showForm",
                    forms=[item.get("formId"), item.get("billFormId")],
                    app_id=item.get("appId"),
                )

        method = str(obj.get("methodname") or obj.get("methodName") or "")
        if method == "addVirtualTab":
            for item in obj.get("args") or []:
                if not isinstance(item, dict):
                    continue
                append(
                    item.get("pageId"),
                    kind="addVirtualTab",
                    forms=[item.get("formId"), item.get("billFormId")],
                    app_id=item.get("appId"),
                )

        if "pageId" in obj and action != "showForm" and method != "addVirtualTab":
            page_id = str(obj.get("pageId") or "").strip()
            forms = [obj.get("formId"), obj.get("billFormId")]
            app_id = obj.get("appId")
            if page_id and not any(forms):
                descendant_forms, descendant_app = scoped_descendant_metadata(obj, page_id)
                forms = descendant_forms
                app_id = app_id or descendant_app
            append(
                page_id,
                kind="responsePageId",
                forms=forms,
                app_id=app_id,
            )

        for value in obj.values():
            if isinstance(value, (dict, list)):
                walk(value)

    walk(response)
    priority = {"showForm": 3, "addVirtualTab": 2, "responsePageId": 1}
    best_by_pageid: dict[str, dict[str, Any]] = {}
    for producer in producers:
        page_id = producer["page_id"]
        current = best_by_pageid.get(page_id)
        if (
            current is None
            or priority.get(producer["producer_kind"], 0)
            > priority.get(current["producer_kind"], 0)
        ):
            best_by_pageid[page_id] = producer
    return list(best_by_pageid.values())


def extract_response_pageid_closures(response: Any) -> list[str]:
    """Return exact pageIds explicitly closed by a recorded response."""
    if isinstance(response, str):
        try:
            response = json.loads(response)
        except Exception:
            return []
    closed: list[str] = []

    def walk(obj: Any) -> None:
        if isinstance(obj, list):
            for item in obj:
                walk(item)
            return
        if not isinstance(obj, dict):
            return
        if obj.get("a") == "closeWindow":
            for item in obj.get("p") or []:
                if not isinstance(item, dict):
                    continue
                pid = str(item.get("pageId") or "").strip()
                if classify_pageid(pid) not in {"missing", "unknown"} and pid not in closed:
                    closed.append(pid)
        for value in obj.values():
            if isinstance(value, (dict, list)):
                walk(value)

    walk(response)
    return closed


def annotate_recorded_pageid_sources(steps: list[dict[str, Any]]) -> list[dict[str, Any]]:
    """Attach value-safe exact HAR pageId producer metadata to generated steps."""
    producers: dict[str, dict[str, Any]] = {}
    index = 0
    while index < len(steps):
        har_index = steps[index].get("_har_index")
        group_end = index + 1
        while (
            group_end < len(steps)
            and har_index is not None
            and steps[group_end].get("_har_index") == har_index
        ):
            group_end += 1
        group = steps[index:group_end]

        for step in group:
            pid = str(step.get("_har_page_id") or "").strip()
            source = producers.get(pid)
            if not source:
                continue
            step["recorded_pageid_type"] = classify_pageid(pid)
            step["recorded_pageid_source_step_id"] = source["step_id"]
            step["recorded_pageid_source_har_index"] = source.get("har_index")
            step["recorded_pageid_source_kind"] = source["producer_kind"]
            source_forms = source.get("form_ids") or []
            if source_forms:
                step["recorded_pageid_source_form_match"] = (
                    str(step.get("form_id") or "") in set(source_forms)
                )

        response_text = next(
            (
                str(step.get("_resp_text") or "")
                for step in group
                if step.get("_resp_text")
            ),
            "",
        )
        source_step_id = str((group[0] if group else {}).get("id") or "")
        for producer in extract_response_pageid_producers(response_text):
            producers[producer["page_id"]] = {
                **producer,
                "step_id": source_step_id,
                "har_index": har_index,
            }
        index = group_end
    return steps


def finalize_recorded_pageid_source_retention(
    steps: list[dict[str, Any]],
    *,
    excluded_tiers: set[str] | None = None,
) -> list[dict[str, Any]]:
    """Mark whether each exact recorded pageId producer survives generation."""
    excluded_tiers = excluded_tiers or set()
    retained_har_indices = {
        step.get("_har_index")
        for step in steps
        if step.get("_har_index") is not None
        and str(step.get("_tier") or "") not in excluded_tiers
    }
    retained_step_ids = {
        str(step.get("id") or "")
        for step in steps
        if str(step.get("_tier") or "") not in excluded_tiers
    }
    for step in steps:
        source_har_index = step.get("recorded_pageid_source_har_index")
        source_step_id = str(step.get("recorded_pageid_source_step_id") or "")
        if source_har_index is None and not source_step_id:
            continue
        step["recorded_pageid_source_retained"] = (
            source_har_index in retained_har_indices
            if source_har_index is not None
            else source_step_id in retained_step_ids
        )
    return steps


def annotate_pageid_recovery_strategies(
    steps: list[dict[str, Any]],
) -> list[dict[str, Any]]:
    """Describe how replay will recover when an exact HAR producer was filtered."""
    for step in steps:
        retained = step.get("recorded_pageid_source_retained")
        if retained is True:
            step["pageid_recovery_strategy"] = "recorded_source"
            continue
        if retained is not False:
            continue

        role = expected_pageid_role(step)
        if step.get("requires_harvested_l3_page"):
            step["pageid_recovery_strategy"] = "harvested_l3_guard"
        elif role == "L2" or step_allows_l2_pageid(step):
            step["pageid_recovery_strategy"] = "recorded_l2_context"
        elif role == "L3" and step.get("form_id") and step.get("app_id"):
            step["pageid_recovery_strategy"] = "runtime_form_revalidate"
            step["force_pageid_validation"] = True
        elif role == "L2_or_L3" and step.get("form_id") and step.get("app_id"):
            step["pageid_recovery_strategy"] = "runtime_auto_open"
        else:
            step["pageid_recovery_strategy"] = "missing"
    return steps


def step_allows_l2_pageid(step: dict[str, Any]) -> bool:
    """Whether this step should keep a menu/list L2 pageId."""
    if step.get("requires_harvested_l3_page"):
        return False
    if _is_write_like_step(step):
        return False
    if step.get("preserve_l2_page") is True:
        return True
    ac = str(step.get("ac") or step.get("method") or "")
    method = str(step.get("method") or "")
    if method == "itemClick":
        return True
    return ac in _L2_CONTEXT_ACS


def expected_pageid_role(step: dict[str, Any]) -> str:
    """Return the expected pageId role for the step based on replay semantics."""
    stype = str(step.get("type") or "")
    ac = str(step.get("ac") or "")
    form_id = str(step.get("form_id") or "")
    if stype == "open_form":
        return "L1" if form_id.startswith("bos_portal") else "L3"
    if stype in _EDIT_STEP_TYPES:
        return "L3"
    if stype == "invoke":
        if step.get("requires_harvested_l3_page"):
            return "L3"
        if ac == "loadData" and not step.get("preserve_l2_page"):
            return "L2_or_L3"
        if _is_write_like_step(step):
            return "L3"
        if step_allows_l2_pageid(step):
            return "L2"
        return "L3"
    return "not_applicable"


def build_runtime_pageid_trace(
    step: dict[str, Any],
    *,
    current_page_id: Any = "",
    pending_page_id: Any = "",
    phase: str = "",
    status: str = "",
) -> dict[str, Any]:
    """Build a compact trace payload for a running step."""
    har_page_id = step.get("_har_page_id", "")
    return {
        "step_id": step.get("id", ""),
        "step_type": step.get("type", ""),
        "form_id": step.get("form_id", ""),
        "app_id": step.get("app_id", ""),
        "ac": step.get("ac", ""),
        "method": step.get("method", ""),
        "phase": phase,
        "status": status,
        "expected_pageid_role": expected_pageid_role(step),
        "preserve_l2_page": bool(step.get("preserve_l2_page")),
        "har_pageid_type": (
            classify_pageid(har_page_id)
            if classify_pageid(har_page_id) != "missing"
            else str(step.get("recorded_pageid_type") or "missing")
        ),
        "har_pageid_fragment": pageid_fragment(har_page_id),
        "runtime_pageid_type": classify_pageid(current_page_id),
        "runtime_pageid_fragment": pageid_fragment(current_page_id),
        "pending_pageid_type": classify_pageid(pending_page_id),
        "pending_pageid_fragment": pageid_fragment(pending_page_id),
        "recorded_pageid_source_step_id": step.get("recorded_pageid_source_step_id", ""),
        "recorded_pageid_source_retained": step.get("recorded_pageid_source_retained"),
        "recorded_pageid_source_kind": step.get("recorded_pageid_source_kind", ""),
        "recorded_pageid_source_form_match": step.get("recorded_pageid_source_form_match"),
        "pageid_recovery_strategy": step.get("pageid_recovery_strategy", ""),
        "force_pageid_validation": bool(step.get("force_pageid_validation")),
        "risk_codes": pageid_risks(
            step,
            har_page_id=har_page_id,
            runtime_page_id=current_page_id,
            pending_page_id=pending_page_id,
            phase=phase,
        ),
    }


def build_pageid_trace(
    case: dict[str, Any],
    *,
    har_steps: list[dict[str, Any]] | None = None,
    run_events: list[dict[str, Any]] | None = None,
    include_fragments: bool = True,
) -> dict[str, Any]:
    """Build a static/runtime pageId trace for evidence packages and reports."""
    har_by_id = {
        str(step.get("id")): step
        for step in (har_steps or [])
        if step.get("id")
    }
    runtime_by_id = _runtime_trace_by_step(run_events or [])

    rows: list[dict[str, Any]] = []
    for index, step in enumerate(case.get("steps") or []):
        sid = str(step.get("id") or f"step_{index + 1}")
        har_step = har_by_id.get(sid) or {}
        har_page_id = step.get("_har_page_id") or har_step.get("_har_page_id", "")
        har_pageid_type = classify_pageid(har_page_id)
        if har_pageid_type == "missing":
            har_pageid_type = str(
                step.get("recorded_pageid_type")
                or har_step.get("recorded_pageid_type")
                or "missing"
            )
        runtime = runtime_by_id.get(sid, {})
        row = {
            "index": index,
            "step_id": sid,
            "step_type": step.get("type", ""),
            "form_id": step.get("form_id", ""),
            "app_id": step.get("app_id", ""),
            "ac": step.get("ac", ""),
            "method": step.get("method", ""),
            "expected_pageid_role": expected_pageid_role(step),
            "preserve_l2_page": bool(step.get("preserve_l2_page")),
            "har_pageid_type": har_pageid_type,
            "runtime_pageid_type": runtime.get("runtime_pageid_type", ""),
            "pending_pageid_type": runtime.get("pending_pageid_type", ""),
            "recorded_pageid_source_step_id": step.get("recorded_pageid_source_step_id", ""),
            "recorded_pageid_source_retained": step.get("recorded_pageid_source_retained"),
            "recorded_pageid_source_kind": step.get("recorded_pageid_source_kind", ""),
            "recorded_pageid_source_form_match": step.get("recorded_pageid_source_form_match"),
            "pageid_recovery_strategy": step.get("pageid_recovery_strategy", ""),
            "force_pageid_validation": bool(step.get("force_pageid_validation")),
        }
        if include_fragments:
            row["har_pageid_fragment"] = pageid_fragment(har_page_id)
            row["runtime_pageid_fragment"] = runtime.get("runtime_pageid_fragment", "")
            row["pending_pageid_fragment"] = runtime.get("pending_pageid_fragment", "")
        row["risk_codes"] = pageid_risks(
            step,
            har_page_id=har_page_id,
            runtime_page_id=runtime.get("runtime_pageid_fragment", ""),
            pending_page_id=runtime.get("pending_pageid_fragment", ""),
            runtime_pageid_type=runtime.get("runtime_pageid_type", ""),
        )
        rows.append(row)

    return {
        "summary": summarize_pageid_trace(rows),
        "steps": rows,
    }


def compact_pageid_trace(trace: dict[str, Any]) -> dict[str, Any]:
    """Strip fragments for deterministic value-safe regression baselines."""
    return {
        "summary": trace.get("summary", {}),
        "steps": [
            {
                "step_id": row.get("step_id", ""),
                "step_type": row.get("step_type", ""),
                "form_id": row.get("form_id", ""),
                "app_id": row.get("app_id", ""),
                "ac": row.get("ac", ""),
                "method": row.get("method", ""),
                "expected_pageid_role": row.get("expected_pageid_role", ""),
                "preserve_l2_page": bool(row.get("preserve_l2_page")),
                "har_pageid_type": row.get("har_pageid_type", ""),
                "recorded_pageid_source_step_id": row.get("recorded_pageid_source_step_id", ""),
                "recorded_pageid_source_retained": row.get("recorded_pageid_source_retained"),
                "recorded_pageid_source_kind": row.get("recorded_pageid_source_kind", ""),
                "recorded_pageid_source_form_match": row.get("recorded_pageid_source_form_match"),
                "pageid_recovery_strategy": row.get("pageid_recovery_strategy", ""),
                "force_pageid_validation": bool(row.get("force_pageid_validation")),
                "risk_codes": row.get("risk_codes", []),
            }
            for row in trace.get("steps", [])
            if _is_trace_step_interesting(row)
        ],
    }


def summarize_pageid_trace(rows: list[dict[str, Any]]) -> dict[str, Any]:
    counts: dict[str, int] = {}
    risks: dict[str, int] = {}
    preserve_l2 = 0
    for row in rows:
        role = str(row.get("expected_pageid_role") or "unknown")
        counts[role] = counts.get(role, 0) + 1
        if row.get("preserve_l2_page"):
            preserve_l2 += 1
        for risk in row.get("risk_codes") or []:
            risks[str(risk)] = risks.get(str(risk), 0) + 1
    return {
        "total_steps": len(rows),
        "expected_roles": counts,
        "preserve_l2_steps": preserve_l2,
        "risk_counts": risks,
        "risk_level": "review" if risks else "none",
    }


def pageid_risks(
    step: dict[str, Any],
    *,
    har_page_id: Any = "",
    runtime_page_id: Any = "",
    pending_page_id: Any = "",
    runtime_pageid_type: str = "",
    phase: str = "",
) -> list[str]:
    expected = expected_pageid_role(step)
    har_type = classify_pageid(har_page_id)
    if har_type == "missing":
        har_type = str(step.get("recorded_pageid_type") or "missing")
    runtime_type = runtime_pageid_type or classify_pageid(runtime_page_id)
    risks: list[str] = []

    if har_type == "L2" and expected == "L2" and not step.get("preserve_l2_page"):
        risks.append("missing_preserve_l2_page")
    # Before the handler runs, runner may still hold the list L2 while a pending
    # L3 is available. _h_invoke will consume pending L3, so this is not a
    # defect unless the after-handler trace still shows L2.
    if expected == "L3" and runtime_type == "L2" and phase != "before_handler":
        risks.append("runtime_l2_used_for_l3_step")
    if expected == "L2" and runtime_type in {"L1_or_L3"} and har_type == "L2":
        risks.append("runtime_l3_used_for_l2_step")
    if expected == "L3" and har_type == "L2" and not step_allows_l2_pageid(step):
        risks.append("har_l2_on_l3_step")
    if classify_pageid(pending_page_id) == "L2" and expected == "L3":
        risks.append("pending_l2_for_l3_step")
    if step.get("recorded_pageid_source_form_match") is False:
        risks.append("recorded_pageid_source_form_mismatch")
    if (
        step.get("recorded_pageid_source_retained") is False
        and expected == "L3"
        and str(step.get("pageid_recovery_strategy") or "") in {"", "missing"}
    ):
        risks.append("recorded_pageid_recovery_missing")
    return risks


def _runtime_trace_by_step(events: list[dict[str, Any]]) -> dict[str, dict[str, Any]]:
    by_id: dict[str, dict[str, Any]] = {}
    for event in events:
        data = event.get("data") if isinstance(event, dict) else None
        if not isinstance(data, dict):
            continue
        payload = data.get("pageid_trace") if event.get("type") != "pageid_trace" else data
        if not isinstance(payload, dict):
            continue
        sid = str(payload.get("step_id") or data.get("id") or "")
        if not sid:
            continue
        by_id[sid] = payload
    return by_id


def _is_write_like_step(step: dict[str, Any]) -> bool:
    ac = str(step.get("ac") or "").lower()
    key = str(step.get("key") or "").lower()
    args_text = str(step.get("args") or "").lower()
    if ac in {item.lower() for item in _WRITE_ACS}:
        if ac == "click" and key not in _WRITE_KEYS and "save" not in args_text and "submit" not in args_text:
            return False
        return True
    if key in _WRITE_KEYS:
        return True
    return any(token in args_text for token in ("new_save", "save", "submit", "confirm"))


def _is_trace_step_interesting(row: dict[str, Any]) -> bool:
    if row.get("preserve_l2_page"):
        return True
    if row.get("risk_codes"):
        return True
    if row.get("expected_pageid_role") in {"L2", "L3"}:
        return True
    return False
