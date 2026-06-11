"""Normalized HAR flow IR helpers.

The IR layer is intentionally value-safe: it keeps request shape, pageId roles,
dependencies, variables and assertion policy without persisting raw cookies,
tokens or full pageIds.
"""

from .schema import build_normalized_flow, compact_flow_for_preview, validate_normalized_flow
from .alignment import assess_ir_preview_alignment
from .yaml_bridge import build_ir_yaml_bridge, classify_yaml_step_role

__all__ = [
    "assess_ir_preview_alignment",
    "build_ir_yaml_bridge",
    "build_normalized_flow",
    "classify_yaml_step_role",
    "compact_flow_for_preview",
    "validate_normalized_flow",
]
