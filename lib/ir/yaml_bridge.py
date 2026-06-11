"""Bridge IR-observed roles to generated YAML steps.

The bridge is the first migration layer from the legacy HAR extractor toward
an IR-driven generator.  It is deliberately value-safe and behavior-neutral:
it does not rewrite YAML yet; it explains which IR roles are covered by the
current generated YAML and which role families are ready to migrate next.
"""
from __future__ import annotations

from collections import Counter, defaultdict
from typing import Any, Mapping


SCHEMA_VERSION = "0.2"

ROLE_REQUIREMENTS = {
    "navigation": {
        "yaml_step_types": ["invoke", "click_menu", "click_toolbar", "wait_until"],
        "pageid_policy": "prefer_l2_for_menu_list_tree_toolbar",
        "migration_stage": "stage_1_navigation_list",
    },
    "edit": {
        "yaml_step_types": ["update_fields", "pick_basedata", "select_f7_list_row", "invoke"],
        "pageid_policy": "require_l3_or_active_dialog",
        "migration_stage": "stage_2_edit_fields",
    },
    "write": {
        "yaml_step_types": ["invoke", "click_toolbar", "wait_until"],
        "pageid_policy": "require_l3_and_response_contract",
        "migration_stage": "stage_4_write_anchors",
    },
    "action": {
        "yaml_step_types": ["invoke", "sleep", "wait_until"],
        "pageid_policy": "follow_recorded_context",
        "migration_stage": "stage_5_misc_actions",
    },
    "unknown": {
        "yaml_step_types": ["invoke"],
        "pageid_policy": "manual_review",
        "migration_stage": "stage_6_unknowns",
    },
}


def build_ir_yaml_bridge(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
    *,
    vars_meta: Mapping[str, Any] | None = None,
    pick_fields: Mapping[str, Any] | None = None,
    max_items: int = 80,
) -> dict[str, Any]:
    """Summarize how well generated YAML covers IR-observed step roles."""
    flow = flow if isinstance(flow, Mapping) else {}
    yaml_steps = yaml_steps or []
    requests = flow.get("request") if isinstance(flow.get("request"), Mapping) else {}
    pages = {
        str(page.get("id") or ""): page
        for page in (flow.get("pages") or [])
        if isinstance(page, Mapping) and page.get("id")
    }
    yaml_candidates = [_yaml_candidate(step, index) for index, step in enumerate(yaml_steps)]
    field_model_forms = _field_model_forms(vars_meta=vars_meta, pick_fields=pick_fields)
    used_yaml_indexes: set[int] = set()
    role_rows: list[dict[str, Any]] = []
    role_totals: dict[str, dict[str, int]] = defaultdict(lambda: {"ir": 0, "covered": 0, "uncovered": 0})

    for index, step in enumerate(flow.get("steps") or []):
        if not isinstance(step, Mapping):
            continue
        request = requests.get(str(step.get("request_ref") or ""), {})
        page = pages.get(str(step.get("page_ref") or ""), {})
        role = _normalize_role(step.get("role"))
        signature = {
            "role": role,
            "form_id": str(request.get("form_id") or page.get("form_id") or ""),
            "app_id": str(request.get("app_id") or page.get("app_id") or ""),
            "ac": str(request.get("ac") or ""),
            "method": str(request.get("invoke_method") or request.get("method") or ""),
            "key": str((request.get("action") or {}).get("key") or ""),
            "source_index": step.get("source_index"),
            "action_index": step.get("action_index"),
            "expected_pageid_role": str(page.get("expected_role") or ""),
        }
        match = _best_yaml_match(signature, yaml_candidates, used_yaml_indexes)
        coverage = "covered" if match else "uncovered"
        match_reason = match.get("reason", "") if match else ""
        if not match and role == "edit" and signature["form_id"] in field_model_forms:
            coverage = "covered_by_field_model"
            match_reason = "field_model"
        if match:
            used_yaml_indexes.add(match["index"])
        row = {
            "ir_step_id": str(step.get("id") or f"step_{index + 1}"),
            "role": role,
            "form_id": signature["form_id"],
            "app_id": signature["app_id"],
            "ac": signature["ac"],
            "method": signature["method"],
            "key": signature["key"],
            "source_index": signature["source_index"],
            "action_index": signature["action_index"],
            "expected_pageid_role": signature["expected_pageid_role"],
            "coverage": coverage,
            "yaml_step_id": match.get("id", "") if match else "",
            "yaml_step_type": match.get("type", "") if match else "",
            "match_reason": match_reason,
        }
        if len(role_rows) < max_items:
            role_rows.append(row)
        role_totals[role]["ir"] += 1
        role_totals[role]["covered" if coverage != "uncovered" else "uncovered"] += 1

    yaml_role_counts = Counter(candidate["role"] for candidate in yaml_candidates)
    requirements = []
    uncovered_high_risk = 0
    uncovered_total = 0
    for role in sorted(set(role_totals.keys()) | set(ROLE_REQUIREMENTS.keys())):
        totals = role_totals.get(role, {"ir": 0, "covered": 0, "uncovered": 0})
        if not totals["ir"]:
            continue
        uncovered_total += totals["uncovered"]
        if role in {"write", "edit"}:
            uncovered_high_risk += totals["uncovered"]
        spec = ROLE_REQUIREMENTS.get(role) or ROLE_REQUIREMENTS["unknown"]
        requirements.append({
            "role": role,
            "ir_count": totals["ir"],
            "yaml_role_count": int(yaml_role_counts.get(role) or 0),
            "covered_count": totals["covered"],
            "uncovered_count": totals["uncovered"],
            "suggested_yaml_step_types": spec["yaml_step_types"],
            "pageid_policy": spec["pageid_policy"],
            "migration_stage": spec["migration_stage"],
            "status": "ready" if totals["uncovered"] == 0 else "needs_review",
        })

    ir_step_count = sum(item["ir"] for item in role_totals.values())
    covered_count = sum(item["covered"] for item in role_totals.values())
    coverage_score = int(round((covered_count / ir_step_count) * 100)) if ir_step_count else 0
    status = "ready"
    if uncovered_high_risk:
        status = "needs_review"
    elif uncovered_total:
        status = "ready_with_warnings"

    return {
        "schema_version": SCHEMA_VERSION,
        "source": "normalized_flow_to_generated_yaml",
        "status": status,
        "mode": "shadow_contract",
        "coverage_score": coverage_score,
        "summary": _summary(status, coverage_score, uncovered_high_risk, uncovered_total),
        "checks": {
            "ir_step_count": ir_step_count,
            "yaml_step_count": len(yaml_steps),
            "covered_count": covered_count,
            "uncovered_count": uncovered_total,
            "uncovered_write_or_edit_count": uncovered_high_risk,
            "ir_role_counts": {
                role: totals["ir"]
                for role, totals in sorted(role_totals.items())
            },
            "yaml_role_counts": dict(sorted(yaml_role_counts.items())),
            "field_model_form_count": len(field_model_forms),
        },
        "role_requirements": requirements,
        "uncovered_roles": [
            item["role"]
            for item in requirements
            if item["uncovered_count"] > 0
        ],
        "step_role_map": role_rows,
        "migration_policy": {
            "current_generator": "har_extractor_main",
            "bridge_mode": "observe_and_validate",
            "candidate_order": [
                "stage_1_navigation_list",
                "stage_2_edit_fields",
                "stage_3_selectors_f7",
                "stage_4_write_anchors",
                "stage_5_misc_actions",
            ],
            "do_not_migrate_without_baseline": True,
        },
    }


def _best_yaml_match(signature: dict[str, Any], candidates: list[dict[str, Any]], used: set[int]) -> dict[str, Any]:
    best: dict[str, Any] = {}
    best_score = 0
    for candidate in candidates:
        if candidate["index"] in used:
            continue
        if signature["role"] != "unknown" and candidate["role"] != signature["role"]:
            continue
        score = 0
        reasons: list[str] = []
        if candidate["role"] == signature["role"]:
            score += 5
            reasons.append("role")
        if signature["form_id"] and candidate["form_id"] == signature["form_id"]:
            score += 3
            reasons.append("form")
        if signature["app_id"] and candidate["app_id"] == signature["app_id"]:
            score += 1
            reasons.append("app")
        if signature["ac"] and candidate["ac"] == signature["ac"]:
            score += 2
            reasons.append("ac")
        if signature["method"] and candidate["method"] == signature["method"]:
            score += 2
            reasons.append("method")
        if signature["key"] and candidate["key"] == signature["key"]:
            score += 2
            reasons.append("key")
        if signature["source_index"] is not None and candidate["source_index"] == signature["source_index"]:
            score += 4
            reasons.append("source")
            if candidate["action_index"] == signature["action_index"]:
                score += 3
                reasons.append("action")
        if signature["expected_pageid_role"] == "L2" and candidate["preserve_l2_page"]:
            score += 1
            reasons.append("l2")
        threshold = 5 if signature["role"] in {"write", "edit"} else 4
        if score >= threshold and score > best_score:
            best = {**candidate, "reason": "+".join(reasons)}
            best_score = score
    return best


def _yaml_candidate(step: dict[str, Any], index: int) -> dict[str, Any]:
    ir_sources = step.get("ir_sources") if isinstance(step.get("ir_sources"), list) else []
    primary_source = next((item for item in ir_sources if isinstance(item, Mapping)), {})
    return {
        "index": index,
        "id": str(step.get("id") or f"yaml_step_{index + 1}"),
        "type": str(step.get("type") or ""),
        "role": classify_yaml_step_role(step),
        "form_id": str(step.get("form_id") or ""),
        "app_id": str(step.get("app_id") or ""),
        "ac": str(step.get("ac") or ""),
        "method": str(step.get("method") or ""),
        "key": str(step.get("key") or ""),
        "preserve_l2_page": bool(step.get("preserve_l2_page")),
        "source_index": step.get("_har_index", primary_source.get("source_index")),
        "action_index": step.get("_har_action_index", primary_source.get("action_index")),
    }


def _field_model_forms(
    *,
    vars_meta: Mapping[str, Any] | None,
    pick_fields: Mapping[str, Any] | None,
) -> set[str]:
    forms: set[str] = set()
    for source in (vars_meta or {}, pick_fields or {}):
        if not isinstance(source, Mapping):
            continue
        for meta in source.values():
            if isinstance(meta, Mapping):
                form_id = str(meta.get("form_id") or "").strip()
                if form_id:
                    forms.add(form_id)
    return forms


def _normalize_role(role: Any) -> str:
    text = str(role or "").strip().lower()
    if text in ROLE_REQUIREMENTS:
        return text
    if text in {"list", "query", "menu"}:
        return "navigation"
    if text in {"field", "selector"}:
        return "edit"
    if text in {"save", "submit", "audit"}:
        return "write"
    return "unknown" if not text else "action"


def classify_yaml_step_role(step: Mapping[str, Any]) -> str:
    """Classify a generated YAML step into the shared IR bridge role taxonomy."""
    step_type = str(step.get("type") or "").lower()
    ac = str(step.get("ac") or "").lower()
    method = str(step.get("method") or "").lower()
    key = str(step.get("key") or "").lower()
    args = " ".join(str(item).lower() for item in (step.get("args") or []))
    text = " ".join([step_type, ac, method, key, args])
    if any(token in text for token in ("save", "submit", "audit", "confirm", "btnok", "startupflow")):
        return "write"
    if step_type in {"update_fields", "pick_basedata", "select_f7_list_row", "upload_file"}:
        return "edit"
    if step.get("preserve_l2_page") or ac in {
        "menuitemclick",
        "loaddata",
        "treenodeclick",
        "treemenuclick",
        "postexpandnodes",
        "querytreenodechildren",
        "entryrowclick",
        "refresh",
        "itemclick",
    }:
        return "navigation"
    return "action"


def _summary(status: str, score: int, high_risk: int, uncovered: int) -> str:
    if status == "ready":
        return f"{score} 分：IR 角色已由当前 YAML 生成链路覆盖，可作为后续迁移基线。"
    if high_risk:
        return f"{score} 分：仍有 {high_risk} 个写入/编辑 IR 角色未覆盖，迁移前需修解析。"
    return f"{score} 分：有 {uncovered} 个低风险 IR 角色未覆盖，建议继续观察。"
