#!/usr/bin/env python3
"""Run one generated YAML case as a controlled write smoke.

This script intentionally uses the existing HAR/YAML runner for writes. The
Playwright explorer is great for finding menus and pageId shapes; writes should
reuse the project path that already has variables, env fields, assertions, and
agent evidence.
"""
from __future__ import annotations

import argparse
import contextlib
import copy
import io
import json
import re
import sys
from datetime import datetime
from pathlib import Path
from typing import Any

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.config import Config
from lib.deep_chain_pipeline import build_readback_explanation
from lib.runner import load_yaml, run_case, _case_has_write_step, _is_write_step

CONFIRM_TOKEN = "YES_GENERATE_TEST_DATA"


def parse_var_override(item: str) -> tuple[str, str]:
    if "=" not in item:
        raise argparse.ArgumentTypeError("expected KEY=VALUE")
    key, value = item.split("=", 1)
    key = key.strip()
    if not key:
        raise argparse.ArgumentTypeError("empty var key")
    return key, value


def parse_pick_override(item: str) -> tuple[str, str, str]:
    if "=" not in item:
        raise argparse.ArgumentTypeError("expected PICK_FIELD=VALUE or PICK_FIELD=VALUE|NAME")
    key, raw_value = item.split("=", 1)
    key = key.strip()
    if not key:
        raise argparse.ArgumentTypeError("empty pick field key")
    value_id, _, value_name = raw_value.partition("|")
    value_id = value_id.strip()
    value_name = value_name.strip() or value_id
    if not value_id:
        raise argparse.ArgumentTypeError("empty pick field value")
    return key, value_id, value_name


def apply_pick_overrides(case: dict[str, Any], overrides: list[tuple[str, str, str]]) -> None:
    pick_fields = case.setdefault("pick_fields", {})
    for field_id, value_id, value_name in overrides:
        meta = pick_fields.get(field_id)
        if not isinstance(meta, dict):
            meta = {}
            pick_fields[field_id] = meta
        meta["value_id"] = value_id
        meta["value_name"] = value_name
        meta["value_code"] = value_id
        meta["value_number"] = value_id
        meta["manual_override"] = True
        meta["auto_resolve"] = False
        meta["resolve_status"] = "manual"
        meta["resolve_by"] = "manual"


def apply_optional_steps(case: dict[str, Any], step_ids: list[str]) -> None:
    targets = {item.strip() for item in step_ids if item.strip()}
    if not targets:
        return
    for step in case.get("steps") or []:
        if step.get("id") in targets:
            step["optional"] = True
            step.setdefault("write_smoke_note", "optional only for controlled write smoke")


def apply_disabled_steps(case: dict[str, Any], step_ids: list[str]) -> None:
    targets = {item.strip() for item in step_ids if item.strip()}
    if not targets:
        return
    case["steps"] = [step for step in (case.get("steps") or []) if step.get("id") not in targets]
    case.setdefault("write_smoke_disabled_steps", sorted(targets))


def apply_prefetch_pick_steps(case: dict[str, Any], step_ids: list[str]) -> None:
    targets = {item.strip() for item in step_ids if item.strip()}
    if not targets:
        return
    for step in case.get("steps") or []:
        if step.get("id") in targets and step.get("type") == "pick_basedata":
            step["prefetch_lookup"] = True
            step.setdefault("prefetch_lookup_args", [["%", "", "%", 0, 20, 0]])
            step.setdefault("write_smoke_note", "prefetch lookup only for controlled write smoke")


def build_safe_summary(
    case: dict[str, Any],
    events: list[dict[str, Any]],
    passed_or_result: Any,
    duration_s: float,
) -> dict[str, Any]:
    run_result = passed_or_result if hasattr(passed_or_result, "passed") else None
    passed = bool(run_result.passed) if run_result is not None else bool(passed_or_result)
    step_failures = [
        {
            "id": (event.get("payload") or {}).get("id", ""),
            "error": (event.get("payload") or {}).get("error", "")
            or "; ".join((event.get("payload") or {}).get("errors", [])[:3]),
        }
        for event in events
        if event.get("event") == "step_fail"
    ]
    step_map = {
        str(step.get("id") or ""): step
        for step in case.get("steps") or []
        if step.get("id")
    }
    write_events = []
    for event in events:
        payload = event.get("payload") or {}
        if event.get("event") != "step_ok":
            continue
        step_id = str(payload.get("id", ""))
        if not _is_write_step(step_map.get(step_id) or {}):
            continue
        response = payload.get("response")
        response_text = json.dumps(response, ensure_ascii=False, default=str).lower()
        write_events.append(
            {
                "step_id": step_id,
                "has_response": response is not None,
                "response_tokens": sorted(
                    {
                        token
                        for token in ("保存成功", "操作成功", "pkvalue", "billid", "saveresult", "bos_operationresult")
                        if token in response_text
                    }
                ),
            }
        )
    pageid_events = [
        (event.get("payload") or {}).get("pageid_trace")
        for event in events
        if event.get("event") in {"step_ok", "step_fail", "step_warning"}
        and (event.get("payload") or {}).get("pageid_trace")
    ]
    assertions = list(getattr(run_result, "assertions", []) or [])
    runtime_evidence = dict(getattr(run_result, "runtime_evidence", {}) or {})
    request_contract_results = runtime_evidence.get("request_contract_results") or {}
    response_contract_results = runtime_evidence.get("response_contract_results") or {}
    request_contract_failure_count = sum(
        len((item or {}).get("errors") or [])
        for item in request_contract_results.values()
    )
    request_contract_warning_count = sum(
        len((item or {}).get("warnings") or [])
        for item in request_contract_results.values()
    )
    response_contract_failure_count = sum(
        len((item or {}).get("errors") or [])
        for item in response_contract_results.values()
    )
    response_contract_warning_count = sum(
        len((item or {}).get("warnings") or [])
        for item in response_contract_results.values()
    )
    readback_assertions = [
        item
        for item in assertions
        if str(item.get("type") or "").startswith("readback")
    ]
    if any(item.get("ok") and not item.get("advisory") for item in readback_assertions):
        readback_status = "verified"
    elif any(not item.get("ok") and not item.get("advisory") for item in readback_assertions):
        readback_status = "failed"
    elif readback_assertions:
        readback_status = "advisory_only"
    else:
        readback_status = "not_supported"
    write_token_count = sum(len(item.get("response_tokens") or []) for item in write_events)
    write_evidence_status = (
        "verified_by_response"
        if write_token_count
        else "response_present"
        if any(item.get("has_response") for item in write_events)
        else "missing"
    )
    maintenance_expected = int(runtime_evidence.get("maintenance_expected_count") or 0)
    maintenance_matched = int(runtime_evidence.get("maintenance_matched_count") or 0)
    first_success_gate = runtime_evidence.get("first_success_gate") or {}
    fully_verified = bool(first_success_gate.get("verified"))
    if not first_success_gate:
        fully_verified = bool(
            passed
            and request_contract_failure_count == 0
            and response_contract_failure_count == 0
            and write_evidence_status != "missing"
            and readback_status == "verified"
            and maintenance_expected == maintenance_matched
        )
    readback_explanation = build_readback_explanation(
        case,
        readback_status=readback_status,
        write_evidence_status=write_evidence_status,
        request_contract_failure_count=request_contract_failure_count,
        response_contract_failure_count=response_contract_failure_count,
        maintenance_expected_count=maintenance_expected,
        maintenance_matched_count=maintenance_matched,
    )
    return {
        "case_name": case.get("name", ""),
        "main_form_id": case.get("main_form_id", ""),
        "passed": passed,
        "duration_s": round(duration_s, 3),
        "step_count": sum(1 for event in events if event.get("event") == "step_start"),
        "failed_steps": step_failures,
        "write_events": write_events,
        "write_evidence_status": write_evidence_status,
        "assertions": assertions,
        "readback_results": runtime_evidence.get("readback_results") or [],
        "readback_status": readback_status,
        "readback_explanation": readback_explanation,
        "request_contract_failure_count": request_contract_failure_count,
        "request_contract_warning_count": request_contract_warning_count,
        "response_contract_failure_count": response_contract_failure_count,
        "response_contract_warning_count": response_contract_warning_count,
        "maintenance_expected_count": maintenance_expected,
        "maintenance_matched_count": maintenance_matched,
        "maintenance_value_trace": runtime_evidence.get("maintenance_value_trace") or [],
        "first_success_verified": fully_verified,
        "first_success_status": first_success_gate.get(
            "status",
            "verified" if fully_verified else "write_unverified",
        ),
        "first_success_missing": list(first_success_gate.get("missing") or []),
        "first_success_checks": first_success_gate.get("checks") or {},
        "pageid_trace_count": len(pageid_events),
        "vars_used": {k: v for k, v in (case.get("vars") or {}).items() if not str(k).startswith("_")},
        "disabled_steps": case.get("write_smoke_disabled_steps", []),
        "prefetch_pick_steps": [
            step.get("id")
            for step in (case.get("steps") or [])
            if step.get("type") == "pick_basedata" and step.get("prefetch_lookup")
        ],
    }


def sanitize_events(events: list[dict[str, Any]]) -> list[dict[str, Any]]:
    """Keep local evidence useful while avoiding accidental credential leakage."""
    sensitive = re.compile(r"(password|cookie|token|session|csrf|username|account|mobile|phone)", re.IGNORECASE)

    def scrub(value: Any) -> Any:
        if isinstance(value, dict):
            return {k: ("<redacted>" if sensitive.search(str(k)) else scrub(v)) for k, v in value.items()}
        if isinstance(value, list):
            return [scrub(v) for v in value]
        if isinstance(value, str) and sensitive.search(value):
            return sensitive.sub("<redacted>", value)
        return value

    return [scrub(event) for event in events]


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--env", default="sit", help="Use config/envs/<env>.yaml without printing credentials")
    parser.add_argument("--case", type=Path, required=True, help="Generated YAML case to execute")
    parser.add_argument(
        "--confirm-write",
        default="",
        help=f"Must equal {CONFIRM_TOKEN!r}; prevents accidental data generation",
    )
    parser.add_argument("--var", dest="vars", type=parse_var_override, action="append", default=[])
    parser.add_argument(
        "--optional-step",
        dest="optional_steps",
        action="append",
        default=[],
        help="Mark one step optional in memory for this smoke only; does not edit YAML",
    )
    parser.add_argument(
        "--disable-step",
        dest="disabled_steps",
        action="append",
        default=[],
        help="Remove one step in memory for this smoke only; use only for known locked default context fields",
    )
    parser.add_argument(
        "--pick-field",
        dest="pick_fields",
        type=parse_pick_override,
        action="append",
        default=[],
        help="Override a pick_fields entry in memory: FIELD_ID=VALUE or FIELD_ID=VALUE|NAME",
    )
    parser.add_argument(
        "--prefetch-pick",
        dest="prefetch_pick_steps",
        action="append",
        default=[],
        help="Run getLookUpList before one pick_basedata step in memory for this smoke only",
    )
    parser.add_argument("--test-prefix", default="CRPLY_WRITE")
    parser.add_argument(
        "--output",
        type=Path,
        default=Path(f"tmp/write_smoke_runs/write_smoke_{datetime.now():%Y%m%d_%H%M%S}.json"),
    )
    args = parser.parse_args(argv)

    if args.confirm_write != CONFIRM_TOKEN:
        raise SystemExit(f"Refusing to write. Pass --confirm-write {CONFIRM_TOKEN}")

    case = load_yaml(args.case)
    if not isinstance(case, dict):
        raise SystemExit(f"Invalid case YAML: {args.case}")
    case = copy.deepcopy(case)
    if not _case_has_write_step(case):
        raise SystemExit(f"Refusing to run non-write case as write smoke: {args.case}")

    cfg = Config()
    env = cfg.get_env(args.env)
    if not env:
        raise SystemExit(f"Environment not found: {args.env}")
    case_env = case.setdefault("env", {})
    case_env["base_url"] = env.base_url
    case_env["username"] = env.credentials.resolve_username()
    case_env["password"] = env.credentials.resolve_password()
    case_env["datacenter_id"] = env.datacenter_id
    case["_runtime_env_id"] = args.env

    vars_ns = case.setdefault("vars", {})
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    if "test_description" in vars_ns and not any(k == "test_description" for k, _ in args.vars):
        vars_ns["test_description"] = f"{args.test_prefix}_{timestamp}"
    for key, value in args.vars:
        vars_ns[key] = value
    apply_pick_overrides(case, args.pick_fields)
    apply_optional_steps(case, args.optional_steps)
    apply_disabled_steps(case, args.disabled_steps)
    apply_prefetch_pick_steps(case, args.prefetch_pick_steps)

    events: list[dict[str, Any]] = []

    def capture(event_type: str, payload: dict | None = None) -> None:
        events.append({"event": event_type, "payload": payload or {}})

    started = datetime.now()
    # The runner is allowed to print login/protocol details; keep the terminal output
    # summary-only and rely on sanitized local evidence for debugging.
    with contextlib.redirect_stdout(io.StringIO()), contextlib.redirect_stderr(io.StringIO()):
        result = run_case(case, on_event=capture)
    finished = datetime.now()
    summary = build_safe_summary(case, events, result, (finished - started).total_seconds())

    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(
        json.dumps(
            {
                "generated_at": finished.isoformat(timespec="seconds"),
                "env": args.env,
                "case_file": str(args.case),
                "summary": summary,
                "events": sanitize_events(events),
            },
            ensure_ascii=False,
            indent=2,
        ),
        encoding="utf-8",
    )

    print(f"Write smoke evidence: {args.output.resolve()}")
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    return 0 if result.passed else 1


if __name__ == "__main__":
    raise SystemExit(main())
