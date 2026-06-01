#!/usr/bin/env python3
"""Build value-safe HAR IR artifacts without executing network requests."""
from __future__ import annotations

import argparse
import json
import sys
from datetime import datetime
from pathlib import Path
from typing import Any

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.ir import build_normalized_flow, compact_flow_for_preview
from lib.ir.dry_run import dry_run_flow, dry_run_yaml_case
from lib.ir.sanitizer import scan_sensitive_text
from lib.ir.yaml_generator import generate_yaml_case_from_ir

DEFAULT_OUTPUT_DIR = Path("tmp/ir")


def _load_json(path: Path) -> dict[str, Any]:
    return json.loads(path.read_text(encoding="utf-8"))


def _write_text_safely(path: Path, text: str) -> Path:
    risks = scan_sensitive_text(text)
    if risks:
        raise ValueError(f"refuse_to_write_sensitive_output:{','.join(risks)}")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text, encoding="utf-8")
    return path


def _default_output(stem: str, suffix: str) -> Path:
    stamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    safe_stem = "".join(ch if ch.isalnum() or ch in ("-", "_") else "_" for ch in stem)[:80] or "har"
    return DEFAULT_OUTPUT_DIR / f"{safe_stem}_{stamp}{suffix}"


def _build_flow_from_args(args: argparse.Namespace) -> dict[str, Any]:
    if getattr(args, "flow", None):
        return _load_json(args.flow)
    if not getattr(args, "har", None):
        raise ValueError("provide --har or --flow")
    har = _load_json(args.har)
    return build_normalized_flow(
        har,
        source_name=args.har.name,
        environment={"env_id": getattr(args, "env_id", "") or ""},
    )


def _print_summary(payload: dict[str, Any]) -> None:
    print(json.dumps(payload, ensure_ascii=False, indent=2))


def cmd_build(args: argparse.Namespace) -> int:
    flow = _build_flow_from_args(args)
    dry = dry_run_flow(flow)
    output = args.output or _default_output(Path(args.har).stem if args.har else "normalized_flow", ".json")
    text = json.dumps(flow, ensure_ascii=False, indent=2)
    _write_text_safely(output, text)
    _print_summary({
        "ok": dry["ok"],
        "artifact": str(output.resolve()),
        "kind": "normalized_flow",
        "step_count": len(flow.get("steps") or []),
        "api_entry_count": (flow.get("source_har") or {}).get("api_entry_count", 0),
        "confidence_score": flow.get("confidence_score", 0),
        "errors": dry["errors"],
        "warnings": dry["warnings"] + [item.get("code", "") for item in flow.get("warnings", [])],
    })
    return 0 if dry["ok"] else 2


def cmd_yaml_preview(args: argparse.Namespace) -> int:
    flow = _build_flow_from_args(args)
    yaml_text = generate_yaml_case_from_ir(flow, case_name=args.case_name or "")
    dry = dry_run_yaml_case(yaml_text)
    output = args.output or _default_output(Path(args.har).stem if args.har else "ir_case", ".yaml")
    _write_text_safely(output, yaml_text)
    _print_summary({
        "ok": dry["ok"],
        "artifact": str(output.resolve()),
        "kind": "yaml_preview",
        "step_count": len(flow.get("steps") or []),
        "confidence_score": flow.get("confidence_score", 0),
        "errors": dry["errors"],
        "warnings": dry["warnings"],
    })
    return 0 if dry["ok"] else 2


def cmd_dry_run(args: argparse.Namespace) -> int:
    if args.yaml:
        yaml_text = args.yaml.read_text(encoding="utf-8")
        result = dry_run_yaml_case(yaml_text)
        kind = "yaml"
        preview = {}
    else:
        flow = _build_flow_from_args(args)
        result = dry_run_flow(flow)
        kind = "normalized_flow"
        preview = compact_flow_for_preview(flow)
    _print_summary({
        "ok": result["ok"],
        "kind": kind,
        "errors": result["errors"],
        "warnings": result["warnings"],
        "preview": preview,
    })
    return 0 if result["ok"] else 2


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="cmd", required=True)

    build = sub.add_parser("build", help="Write normalized_flow.json from a raw local HAR.")
    build.add_argument("--har", type=Path, required=True, help="Local HAR path; raw HAR is never copied.")
    build.add_argument("--env-id", default="", help="Optional logical env id such as sit/uat.")
    build.add_argument("--output", type=Path, help="Output JSON path; defaults to tmp/ir.")
    build.set_defaults(func=cmd_build)

    yaml_cmd = sub.add_parser("yaml-preview", help="Write a conservative YAML draft from HAR or normalized flow.")
    source = yaml_cmd.add_mutually_exclusive_group(required=True)
    source.add_argument("--har", type=Path, help="Local HAR path; raw HAR is never copied.")
    source.add_argument("--flow", type=Path, help="Existing normalized_flow.json path.")
    yaml_cmd.add_argument("--env-id", default="", help="Optional logical env id used with --har.")
    yaml_cmd.add_argument("--case-name", default="", help="YAML case name.")
    yaml_cmd.add_argument("--output", type=Path, help="Output YAML path; defaults to tmp/ir.")
    yaml_cmd.set_defaults(func=cmd_yaml_preview)

    dry = sub.add_parser("dry-run", help="Validate HAR-derived IR or YAML without network requests.")
    dry_source = dry.add_mutually_exclusive_group(required=True)
    dry_source.add_argument("--har", type=Path, help="Local HAR path; raw HAR is never copied.")
    dry_source.add_argument("--flow", type=Path, help="Existing normalized_flow.json path.")
    dry_source.add_argument("--yaml", type=Path, help="Existing generated YAML path.")
    dry.add_argument("--env-id", default="", help="Optional logical env id used with --har.")
    dry.set_defaults(func=cmd_dry_run)

    args = parser.parse_args(argv)
    try:
        return args.func(args)
    except Exception as exc:
        _print_summary({"ok": False, "error": type(exc).__name__, "message": str(exc)})
        return 2


if __name__ == "__main__":
    raise SystemExit(main())
