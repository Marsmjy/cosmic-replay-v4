"""Dry-run validation for IR and generated YAML without network access."""
from __future__ import annotations

from typing import Any

import yaml

from lib.yaml_schema import validate_yaml_schema

from .sanitizer import scan_sensitive_text


def dry_run_flow(flow: dict[str, Any]) -> dict[str, Any]:
    errors: list[str] = []
    warnings: list[str] = []
    if not flow.get("steps"):
        errors.append("steps_missing")
    if not flow.get("request"):
        errors.append("request_missing")
    for step in flow.get("steps") or []:
        if step.get("request_ref") not in (flow.get("request") or {}):
            errors.append(f"missing_request_ref:{step.get('id')}")
        if step.get("response_ref") not in (flow.get("response") or {}):
            warnings.append(f"missing_response_ref:{step.get('id')}")
    risks = scan_sensitive_text(yaml.safe_dump(flow, allow_unicode=True, sort_keys=False))
    errors.extend(f"sensitive:{risk}" for risk in risks)
    return {"ok": not errors, "errors": errors, "warnings": warnings}


def dry_run_yaml_case(yaml_text_or_case: str | dict[str, Any]) -> dict[str, Any]:
    case = yaml.safe_load(yaml_text_or_case) if isinstance(yaml_text_or_case, str) else yaml_text_or_case
    errors: list[str] = []
    warnings: list[str] = []
    if not isinstance(case, dict):
        return {"ok": False, "errors": ["yaml_not_mapping"], "warnings": []}
    schema_contract = validate_yaml_schema(case)
    errors.extend(f"schema:{code}" for code in schema_contract.get("error_codes") or [])
    warnings.extend(f"schema:{code}" for code in schema_contract.get("warning_codes") or [])
    risks = scan_sensitive_text(yaml.safe_dump(case, allow_unicode=True, sort_keys=False))
    errors.extend(f"sensitive:{risk}" for risk in risks)
    return {
        "ok": not errors,
        "errors": errors,
        "warnings": warnings,
        "yaml_schema_contract": schema_contract,
    }
