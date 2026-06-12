"""Normalized HAR flow IR helpers.

The IR layer is intentionally value-safe: it keeps request shape, pageId roles,
dependencies, variables and assertion policy without persisting raw cookies,
tokens or full pageIds.
"""

from .alignment import assess_ir_preview_alignment
from .data_selector import build_target_data_selector_plan
from .field_bridge import build_ir_field_bridge, build_maintainable_field_binding_plan
from .interaction_contract import (
    apply_ir_interaction_contracts,
    build_ir_interaction_bridge,
    build_ir_interaction_contract,
)
from .navigation import apply_ir_navigation_policy
from .schema import build_normalized_flow, compact_flow_for_preview, validate_normalized_flow
from .write_contract import (
    apply_ir_write_contracts,
    build_case_write_anchor_plan,
    build_ir_write_anchor_bridge,
    classify_write_operation,
    evaluate_first_success_gate,
    is_write_operation_kind,
    is_write_step,
)
from .yaml_bridge import build_ir_yaml_bridge, classify_yaml_step_role

__all__ = [
    "assess_ir_preview_alignment",
    "apply_ir_navigation_policy",
    "apply_ir_interaction_contracts",
    "apply_ir_write_contracts",
    "build_case_write_anchor_plan",
    "build_target_data_selector_plan",
    "build_ir_field_bridge",
    "build_ir_interaction_bridge",
    "build_ir_interaction_contract",
    "build_ir_write_anchor_bridge",
    "build_ir_yaml_bridge",
    "build_maintainable_field_binding_plan",
    "build_normalized_flow",
    "classify_yaml_step_role",
    "classify_write_operation",
    "compact_flow_for_preview",
    "evaluate_first_success_gate",
    "is_write_operation_kind",
    "is_write_step",
    "validate_normalized_flow",
]
