"""Value-safe field binding bridge from normalized IR to generated YAML."""
from __future__ import annotations

import re
from typing import Any, Mapping


SCHEMA_VERSION = "1.0"
_VAR_REF_RE = re.compile(r"\$\{vars\.([A-Za-z0-9_]+)\}")


def build_ir_field_bridge(
    flow: Mapping[str, Any] | None,
    yaml_steps: list[dict[str, Any]] | None,
    *,
    vars_meta: Mapping[str, Any] | None = None,
    pick_fields: Mapping[str, Any] | None = None,
    max_items: int = 120,
) -> dict[str, Any]:
    """Explain whether IR-observed field actions and maintainable values have YAML consumers."""
    flow = flow if isinstance(flow, Mapping) else {}
    yaml_steps = yaml_steps or []
    requests = flow.get("request") if isinstance(flow.get("request"), Mapping) else {}
    step_targets = _step_targets(yaml_steps)
    ir_rows: list[dict[str, Any]] = []
    ir_total = 0
    covered_ir = 0
    matched_orders: list[int] = []

    for step in flow.get("steps") or []:
        if not isinstance(step, Mapping):
            continue
        request = requests.get(str(step.get("request_ref") or ""), {})
        action = request.get("action") if isinstance(request.get("action"), Mapping) else {}
        operation_kind = str(action.get("operation_kind") or "")
        if operation_kind not in {"field_update", "lookup_selection", "grid_selection"}:
            continue
        refs = action.get("field_refs") if isinstance(action.get("field_refs"), list) else []
        if not refs and action.get("key"):
            refs = [{"field_key": action.get("key"), "row_index": None, "value_shape": "unknown"}]
        for ref in refs:
            if not isinstance(ref, Mapping):
                continue
            ir_total += 1
            field_key = str(ref.get("field_key") or "")
            match = _match_target(
                step_targets,
                source_index=step.get("source_index"),
                action_index=step.get("action_index"),
                form_id=str(request.get("form_id") or ""),
                field_key=field_key,
                operation_kind=operation_kind,
            )
            if match:
                covered_ir += 1
                matched_orders.append(int(match.get("index") or 0))
            if len(ir_rows) < max_items:
                ir_rows.append({
                    "ir_step_id": str(step.get("id") or ""),
                    "source_index": step.get("source_index"),
                    "action_index": step.get("action_index"),
                    "form_id": str(request.get("form_id") or ""),
                    "field_key": field_key,
                    "row_index": ref.get("row_index"),
                    "value_shape": str(ref.get("value_shape") or "unknown"),
                    "operation_kind": operation_kind,
                    "selector_interface": str(action.get("selector_interface") or ""),
                    "coverage": "covered" if match else "uncovered",
                    "yaml_step_id": str((match or {}).get("step_id") or ""),
                    "yaml_step_type": str((match or {}).get("step_type") or ""),
                    "yaml_step_order": (match or {}).get("index"),
                    "match_reason": str((match or {}).get("match_reason") or ""),
                })

    binding_plan = build_maintainable_field_binding_plan(
        yaml_steps,
        vars_meta=vars_meta,
        pick_fields=pick_fields,
    )
    binding_summary = binding_plan.get("summary") or {}
    overridden_unbound = int(binding_summary.get("overridden_unbound_count") or 0)
    unbound = int(binding_summary.get("unbound_count") or 0)
    uncovered_ir = max(ir_total - covered_ir, 0)
    order_mismatch_count = sum(
        1
        for previous, current in zip(matched_orders, matched_orders[1:])
        if current < previous
    )
    score_denominator = ir_total + int(binding_summary.get("field_count") or 0)
    score_numerator = (
        covered_ir
        + int(binding_summary.get("bound_count") or 0)
        + int(binding_summary.get("context_count") or 0)
    )
    score = int(round(score_numerator / score_denominator * 100)) if score_denominator else 100
    status = "ready"
    if overridden_unbound:
        status = "blocked"
    elif uncovered_ir or unbound or order_mismatch_count:
        status = "needs_review"

    return {
        "schema_version": SCHEMA_VERSION,
        "status": status,
        "raw_values_included": False,
        "coverage_score": score,
        "summary": _summary(
            status,
            score,
            uncovered_ir,
            unbound,
            overridden_unbound,
            order_mismatch_count,
        ),
        "checks": {
            "ir_field_action_count": ir_total,
            "covered_ir_field_action_count": covered_ir,
            "uncovered_ir_field_action_count": uncovered_ir,
            "field_action_order_mismatch_count": order_mismatch_count,
            **binding_summary,
        },
        "ir_field_map": ir_rows,
        "maintainable_field_binding_plan": binding_plan,
    }


def build_maintainable_field_binding_plan(
    yaml_steps: list[dict[str, Any]] | None,
    *,
    vars_meta: Mapping[str, Any] | None = None,
    pick_fields: Mapping[str, Any] | None = None,
) -> dict[str, Any]:
    """Map every user-maintainable field to concrete executable YAML steps."""
    yaml_steps = yaml_steps or []
    targets = _step_targets(yaml_steps)
    fields: list[dict[str, Any]] = []

    for name, meta in (vars_meta or {}).items():
        if not isinstance(meta, Mapping) or str(name).startswith("_"):
            continue
        fields.append(_binding_row(
            field_id=str(name),
            panel="vars",
            meta=meta,
            targets=targets,
            variable_name=str(name),
        ))
    for field_id, meta in (pick_fields or {}).items():
        if not isinstance(meta, Mapping):
            continue
        fields.append(_binding_row(
            field_id=str(field_id),
            panel="pick_fields",
            meta=meta,
            targets=targets,
            variable_name="",
        ))

    bound_count = sum(1 for item in fields if item.get("status") == "bound")
    unbound_count = sum(1 for item in fields if item.get("status") == "unbound")
    context_count = sum(1 for item in fields if item.get("status") == "context")
    overridden_unbound_count = sum(
        1 for item in fields
        if item.get("status") == "unbound" and item.get("user_overridden")
    )
    cross_env_selector_count = sum(
        1 for item in fields
        if item.get("resolver_kind") in {"lookup", "grid_selector"}
    )
    cross_env_bound_count = sum(
        1 for item in fields
        if item.get("resolver_kind") in {"lookup", "grid_selector"}
        and item.get("status") == "bound"
    )
    cross_env_context_count = sum(
        1 for item in fields
        if item.get("resolver_kind") in {"lookup", "grid_selector"}
        and item.get("status") == "context"
    )
    return {
        "schema_version": SCHEMA_VERSION,
        "status": "blocked" if overridden_unbound_count else "ready" if not unbound_count else "needs_review",
        "raw_values_included": False,
        "fields": fields,
        "summary": {
            "field_count": len(fields),
            "bound_count": bound_count,
            "unbound_count": unbound_count,
            "context_count": context_count,
            "overridden_count": sum(1 for item in fields if item.get("user_overridden")),
            "overridden_unbound_count": overridden_unbound_count,
            "cross_env_selector_count": cross_env_selector_count,
            "cross_env_selector_bound_count": cross_env_bound_count,
            "cross_env_selector_context_count": cross_env_context_count,
            "cross_env_selector_ready_count": cross_env_bound_count + cross_env_context_count,
        },
    }


def _binding_row(
    *,
    field_id: str,
    panel: str,
    meta: Mapping[str, Any],
    targets: list[dict[str, Any]],
    variable_name: str,
) -> dict[str, Any]:
    field_key = str(meta.get("field_key") or "")
    form_id = str(meta.get("form_id") or "")
    source_step_id = str(meta.get("source_step_id") or "")
    resolver_kind = _resolver_kind(field_id, meta, panel=panel)
    user_overridden = bool(meta.get("user_overridden") or meta.get("manual_override"))
    is_runtime_context = bool(
        meta.get("context_only")
        or meta.get("required_context")
        or str(meta.get("source") or "") == "runtime_rule"
    )
    if is_runtime_context and not user_overridden:
        return {
            "id": field_id,
            "panel": panel,
            "label": str(meta.get("label") or field_id),
            "field_key": field_key,
            "form_id": form_id,
            "source_step_id": source_step_id,
            "write_step_id": str(meta.get("write_step_id") or ""),
            "resolver_kind": resolver_kind,
            "resolver_interface": _resolver_interface(resolver_kind),
            "injection_strategy": "runtime_context",
            "runtime_injection_supported": False,
            "user_overridden": False,
            "status": "context",
            "target_step_ids": [],
            "target_step_types": [],
            "match_reasons": ["runtime_context"],
        }
    matches = []
    for target in targets:
        reasons: list[str] = []
        if variable_name and variable_name in target.get("variable_refs", set()):
            reasons.append("variable_ref")
        if source_step_id and source_step_id == target.get("step_id"):
            reasons.append("source_step")
        if field_key and field_key.lower() in target.get("field_keys", set()):
            reasons.append("field_key")
        if form_id and form_id == target.get("form_id"):
            reasons.append("form")
        if not reasons:
            continue
        exact_source_binding = (
            "source_step" in reasons
            and resolver_kind in {"grid_selector", "manual", "user_file"}
        )
        if (
            field_key
            and field_key.lower() not in target.get("field_keys", set())
            and "variable_ref" not in reasons
            and not exact_source_binding
        ):
            continue
        if form_id and target.get("form_id") and form_id != target.get("form_id"):
            continue
        if source_step_id and resolver_kind == "grid_selector" and source_step_id != target.get("step_id"):
            continue
        matches.append({
            "step_id": target.get("step_id", ""),
            "step_type": target.get("step_type", ""),
            "match_reason": "+".join(reasons),
        })
    runtime_supported = panel == "vars" or _runtime_pick_injection_supported(field_id, meta)
    status = "bound" if matches and runtime_supported else "unbound"
    return {
        "id": field_id,
        "panel": panel,
        "label": str(meta.get("label") or field_id),
        "field_key": field_key,
        "form_id": form_id,
        "source_step_id": source_step_id,
        "write_step_id": str(meta.get("write_step_id") or ""),
        "resolver_kind": resolver_kind,
        "resolver_interface": _resolver_interface(resolver_kind),
        "injection_strategy": _injection_strategy(field_id, meta, panel=panel),
        "runtime_injection_supported": runtime_supported,
        "user_overridden": user_overridden,
        "status": status,
        "target_step_ids": [str(item.get("step_id") or "") for item in matches],
        "target_step_types": sorted({str(item.get("step_type") or "") for item in matches}),
        "match_reasons": sorted({str(item.get("match_reason") or "") for item in matches}),
    }


def _step_targets(steps: list[dict[str, Any]]) -> list[dict[str, Any]]:
    targets: list[dict[str, Any]] = []
    for index, step in enumerate(steps):
        if not isinstance(step, Mapping):
            continue
        field_keys: set[str] = set()
        if isinstance(step.get("fields"), Mapping):
            field_keys.update(str(key).lower() for key in step["fields"])
        for key in ("field_key", "target_field_key"):
            if step.get(key):
                field_keys.add(str(step.get(key)).lower())
        post_data = step.get("post_data")
        if isinstance(post_data, list) and len(post_data) >= 2 and isinstance(post_data[1], list):
            field_keys.update(
                str(item.get("k")).lower()
                for item in post_data[1]
                if isinstance(item, Mapping) and item.get("k")
            )
        targets.append({
            "index": index,
            "step_id": str(step.get("id") or f"step_{index + 1}"),
            "step_type": str(step.get("type") or ""),
            "form_id": str(step.get("form_id") or ""),
            "field_keys": field_keys,
            "variable_refs": _variable_refs(step),
            "ir_sources": _ir_sources(step),
        })
    return targets


def _match_target(
    targets: list[dict[str, Any]],
    *,
    source_index: Any,
    action_index: Any,
    form_id: str,
    field_key: str,
    operation_kind: str,
) -> dict[str, Any] | None:
    wanted_types = {
        "field_update": {"update_fields", "invoke"},
        "lookup_selection": {"pick_basedata", "update_fields", "invoke"},
        "grid_selection": {"select_f7_list_row", "invoke"},
    }.get(operation_kind, set())
    field_key_lower = field_key.lower()
    for target in targets:
        if wanted_types and target.get("step_type") not in wanted_types:
            continue
        if any(
            item.get("source_index") == source_index and item.get("action_index") == action_index
            for item in target.get("ir_sources") or []
        ):
            return {**target, "match_reason": "ir_source"}
    for target in targets:
        if wanted_types and target.get("step_type") not in wanted_types:
            continue
        if form_id and target.get("form_id") != form_id:
            continue
        if field_key_lower and field_key_lower not in target.get("field_keys", set()):
            continue
        return {**target, "match_reason": "form+field"}
    return None


def _ir_sources(step: Mapping[str, Any]) -> list[dict[str, Any]]:
    sources = step.get("ir_sources")
    if isinstance(sources, list):
        return [
            {
                "source_index": item.get("source_index"),
                "action_index": item.get("action_index"),
            }
            for item in sources
            if isinstance(item, Mapping)
        ]
    source_index = step.get("_har_index")
    action_index = step.get("_har_action_index")
    if source_index is None:
        return []
    return [{"source_index": source_index, "action_index": action_index}]


def _variable_refs(step: Mapping[str, Any]) -> set[str]:
    refs: set[str] = set()

    def visit(value: Any) -> None:
        if isinstance(value, str):
            refs.update(_VAR_REF_RE.findall(value))
        elif isinstance(value, Mapping):
            for item in value.values():
                visit(item)
        elif isinstance(value, list):
            for item in value:
                visit(item)

    visit(step)
    return refs


def _resolver_kind(field_id: str, meta: Mapping[str, Any], *, panel: str) -> str:
    if panel == "vars":
        return "literal"
    if meta.get("selector_source") == "entryRowClick" or field_id.startswith("selector_"):
        return "grid_selector"
    if field_id.startswith("pick_") or meta.get("auto_resolve"):
        return "lookup"
    if field_id.startswith("date_"):
        return "literal_date"
    if field_id.startswith("bool_"):
        return "literal_boolean"
    if field_id.startswith(("enum_", "num_")):
        return "literal"
    if meta.get("source_type") == "upload_file":
        return "user_file"
    return "manual"


def _resolver_interface(kind: str) -> str:
    return {
        "lookup": "getLookUpList/setItemByIdFromClient",
        "grid_selector": "loadData/commonSearch/entryRowClick",
        "literal": "updateValue",
        "literal_date": "updateValue",
        "literal_boolean": "updateValue",
        "user_file": "upload",
        "manual": "recorded_action",
    }.get(kind, "")


def _runtime_pick_injection_supported(field_id: str, meta: Mapping[str, Any]) -> bool:
    if meta.get("source_type") == "upload_file":
        return True
    if field_id.startswith(("pick_", "selector_", "date_", "enum_", "bool_", "num_")):
        return True
    return field_id.startswith("env_") and field_id.endswith("_treeview_focus")


def _injection_strategy(field_id: str, meta: Mapping[str, Any], *, panel: str) -> str:
    if panel == "vars":
        return "variable_reference"
    if meta.get("source_type") == "upload_file":
        return "runtime_upload"
    if field_id.startswith("selector_"):
        return "target_env_grid_selector"
    if field_id.startswith("pick_"):
        return "target_env_lookup_or_model_update"
    if field_id.startswith("date_"):
        return "runtime_date_update"
    if field_id.startswith(("enum_", "bool_", "num_")):
        return "runtime_literal_update"
    if field_id.startswith("env_") and field_id.endswith("_treeview_focus"):
        return "runtime_tree_context_update"
    return "unsupported"


def _summary(
    status: str,
    score: int,
    uncovered_ir: int,
    unbound: int,
    overridden_unbound: int,
    order_mismatch: int,
) -> str:
    if status == "blocked":
        return f"{score} 分：有 {overridden_unbound} 个用户已修改字段没有可执行落点，写入前必须修复。"
    if status == "needs_review":
        return (
            f"{score} 分：有 {uncovered_ir} 个 IR 字段动作、{unbound} 个维护项"
            f"未完成绑定，字段顺序倒置 {order_mismatch} 处。"
        )
    return f"{score} 分：IR 字段动作和可维护值均已绑定到可执行步骤。"
