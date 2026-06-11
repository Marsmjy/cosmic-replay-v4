"""Apply navigation/list replay policy from action-level IR provenance."""
from __future__ import annotations

from collections import Counter
from typing import Any, Iterable, Mapping


def apply_ir_navigation_policy(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]],
    *,
    main_form: str,
    decorative_form_ids: Iterable[str] = (),
    decorative_prefixes: tuple[str, ...] = ("bos_card_", "gbs_bgtask"),
    decorative_suffixes: tuple[str, ...] = ("_apphome",),
) -> dict[str, Any]:
    """Normalize navigation steps using exact HAR entry/action provenance.

    The policy never synthesizes requests or business values. It only applies
    page-role, menu-target and optional-shell decisions verified by IR plus the
    generated step's original HAR coordinates.
    """
    flow = flow if isinstance(flow, Mapping) else {}
    decorative_ids = {str(item) for item in decorative_form_ids if str(item)}
    ir_rows = _ir_navigation_rows(flow)
    exact = {
        (row["source_index"], row["action_index"]): row
        for row in ir_rows
        if row["source_index"] is not None and row["action_index"] is not None
    }
    by_source: dict[int, list[dict[str, Any]]] = {}
    for row in ir_rows:
        source_index = row["source_index"]
        if source_index is not None:
            by_source.setdefault(source_index, []).append(row)

    matched = 0
    l2_preserved = 0
    menu_bound = 0
    optional_shell = 0
    requires_l3 = 0
    first_menu_bound = False
    role_counts: Counter[str] = Counter()
    first_main_idx = next(
        (index for index, step in enumerate(yaml_steps) if str(step.get("form_id") or "") == main_form),
        None,
    )

    for index, step in enumerate(yaml_steps):
        row = _match_ir_row(step, exact=exact, by_source=by_source)
        if row:
            matched += 1
            role_counts[row["role"]] += 1
        form_id = str(step.get("form_id") or "")
        ac = str(step.get("ac") or "")

        if row and row["role"] == "navigation" and row["pageid_type"] == "L2":
            if not step.get("preserve_l2_page"):
                step["preserve_l2_page"] = True
                l2_preserved += 1

        if ac == "menuItemClick" and main_form:
            if not step.get("target_form") and not first_menu_bound:
                step["target_form"] = main_form
                menu_bound += 1
                first_menu_bound = True
            if step.get("target_form"):
                step.setdefault("env_sensitive", "high")
                step.setdefault("resolve_by", "menu_path_or_form")
                step.setdefault("navigation_form_id", str(step.get("target_form") or main_form))

        if _is_decorative_form(
            form_id,
            main_form=main_form,
            decorative_ids=decorative_ids,
            prefixes=decorative_prefixes,
            suffixes=decorative_suffixes,
        ):
            if not step.get("optional"):
                optional_shell += 1
            step["optional"] = True
            if (
                step.get("type") == "invoke"
                and ac == "loadData"
                and row
                and row["pageid_type"] not in {"", "missing", "L2"}
            ):
                if not step.get("requires_harvested_l3_page"):
                    requires_l3 += 1
                step["requires_harvested_l3_page"] = True

        if (
            first_main_idx is not None
            and index < first_main_idx
            and step.get("type") == "invoke"
            and ac in {"refresh", "entryRowClick"}
        ):
            step.pop("optional", None)

    unmatched = max(0, len(ir_rows) - matched)
    status = "no_ir_navigation"
    if ir_rows:
        status = "applied" if unmatched == 0 else "applied_with_gaps"
    return {
        "schema_version": 1,
        "stage": "stage_1_navigation_list",
        "mode": "active",
        "status": status,
        "ir_navigation_count": len(ir_rows),
        "matched_yaml_count": matched,
        "unmatched_ir_count": unmatched,
        "l2_preserved_count": l2_preserved,
        "menu_bound_count": menu_bound,
        "optional_shell_count": optional_shell,
        "requires_l3_count": requires_l3,
        "matched_role_counts": dict(sorted(role_counts.items())),
    }


def _ir_navigation_rows(flow: Mapping[str, Any]) -> list[dict[str, Any]]:
    requests = flow.get("request") if isinstance(flow.get("request"), Mapping) else {}
    pages = {
        str(page.get("id") or ""): page
        for page in (flow.get("pages") or [])
        if isinstance(page, Mapping) and page.get("id")
    }
    rows: list[dict[str, Any]] = []
    for step in flow.get("steps") or []:
        if not isinstance(step, Mapping) or str(step.get("role") or "") != "navigation":
            continue
        request = requests.get(str(step.get("request_ref") or ""), {})
        page = pages.get(str(step.get("page_ref") or ""), {})
        action = request.get("action") if isinstance(request.get("action"), Mapping) else {}
        rows.append({
            "ir_step_id": str(step.get("id") or ""),
            "role": "navigation",
            "source_index": _optional_int(step.get("source_index")),
            "action_index": _optional_int(step.get("action_index")),
            "form_id": str(request.get("form_id") or page.get("form_id") or ""),
            "app_id": str(request.get("app_id") or page.get("app_id") or ""),
            "ac": str(request.get("ac") or ""),
            "method": str(request.get("invoke_method") or action.get("method") or ""),
            "key": str(action.get("key") or ""),
            "pageid_type": str(page.get("pageid_type") or ""),
            "expected_pageid_role": str(page.get("expected_role") or ""),
        })
    return rows


def _match_ir_row(
    step: Mapping[str, Any],
    *,
    exact: Mapping[tuple[int, int], dict[str, Any]],
    by_source: Mapping[int, list[dict[str, Any]]],
) -> dict[str, Any] | None:
    source_index = _optional_int(step.get("_har_index"))
    action_index = _optional_int(step.get("_har_action_index"))
    if source_index is None:
        return None
    if action_index is not None:
        row = exact.get((source_index, action_index))
        if row:
            return row
    candidates = by_source.get(source_index) or []
    if len(candidates) == 1:
        return candidates[0]
    form_id = str(step.get("form_id") or "")
    ac = str(step.get("ac") or "")
    method = str(step.get("method") or "")
    key = str(step.get("key") or "")
    for row in candidates:
        if (
            (not form_id or row["form_id"] == form_id)
            and (not ac or row["ac"] == ac)
            and (not method or row["method"] == method)
            and (not key or row["key"] == key)
        ):
            return row
    return None


def _is_decorative_form(
    form_id: str,
    *,
    main_form: str,
    decorative_ids: set[str],
    prefixes: tuple[str, ...],
    suffixes: tuple[str, ...],
) -> bool:
    if not form_id or form_id == main_form:
        return False
    return (
        form_id in decorative_ids
        or form_id.startswith(prefixes)
        or form_id.endswith(suffixes)
    )


def _optional_int(value: Any) -> int | None:
    try:
        return int(value)
    except (TypeError, ValueError):
        return None
