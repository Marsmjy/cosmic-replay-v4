#!/usr/bin/env python3
"""Deep-chain factory status and value-safe pipeline reports."""
from __future__ import annotations

import argparse
import json
import subprocess
import sys
from datetime import datetime
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.deep_chain_pipeline import (
    DEFAULT_CATALOG,
    DEFAULT_OUTPUT_DIR,
    build_readback_plan,
    build_auto_pipeline_report,
    build_experience_candidate,
    build_report_from_paths,
    build_sample_expansion_plan,
    load_catalog,
    load_yaml_case,
    match_experience_catalog,
    summarize_progress,
    write_json_report,
)
from lib.har_chain_probe import probe_har_chain

WRITE_CONFIRM_TOKEN = "YES_GENERATE_TEST_DATA"


def _print_progress(report: dict) -> None:
    print(f"目标云：{report.get('target_cloud')}")
    print(f"当前阶段：{report.get('current_phase')}")
    print(
        "进度："
        f"{report.get('closed_write_passed')}/{report.get('scenario_count')} 已写入闭环，"
        f"成熟度 {report.get('maturity_percent')}%"
    )
    print("阶段分布：")
    for key, value in (report.get("stage_counts") or {}).items():
        print(f"- {key}: {value}")
    next_focus = report.get("next_focus") or []
    if next_focus:
        print("下一批建议：")
        for item in next_focus:
            print(
                f"- {item.get('app_label')} / {item.get('menu_label')} "
                f"({item.get('stage')}): {item.get('next_action')}"
            )


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="cmd", required=True)

    status_cmd = sub.add_parser("status", help="Summarize deep-chain factory progress.")
    status_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    status_cmd.add_argument("--json", action="store_true", help="Print machine-readable JSON.")
    expansion_cmd = sub.add_parser("expansion-plan", help="Build the next safe Playwright/HAR sample expansion batch.")
    expansion_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    expansion_cmd.add_argument("--limit", type=int, default=8)
    expansion_cmd.add_argument("--output", type=Path, help="Optional JSON output path.")

    report_cmd = sub.add_parser("scenario-report", help="Build one value-safe scenario pipeline report.")
    report_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    report_cmd.add_argument("--scenario-id", required=True)
    report_cmd.add_argument("--case", type=Path, help="YAML case path; defaults to catalog case_file.")
    report_cmd.add_argument("--har", type=Path, help="Raw local ignored HAR to probe; never committed.")
    report_cmd.add_argument("--smoke-evidence", type=Path, help="Local write_smoke_run evidence JSON.")
    report_cmd.add_argument(
        "--output",
        type=Path,
        default=DEFAULT_OUTPUT_DIR / f"scenario_report_{datetime.now():%Y%m%d_%H%M%S}.json",
    )
    readback_cmd = sub.add_parser("readback-plan", help="Build a value-safe post-save readback plan for one YAML case.")
    readback_cmd.add_argument("--case", type=Path, required=True)
    readback_cmd.add_argument("--output", type=Path, help="Optional JSON output path.")
    match_cmd = sub.add_parser("match-experience", help="Match one HAR/YAML profile to closed deep-chain experience.")
    match_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    match_cmd.add_argument("--case", type=Path, help="YAML case path.")
    match_cmd.add_argument("--har", type=Path, help="Raw local ignored HAR to probe; never committed.")
    match_cmd.add_argument("--limit", type=int, default=3)
    match_cmd.add_argument("--output", type=Path, help="Optional JSON output path.")
    candidate_cmd = sub.add_parser("experience-candidate", help="Build a value-safe catalog candidate from HAR/YAML/smoke evidence.")
    candidate_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    candidate_cmd.add_argument("--scenario-id", required=True)
    candidate_cmd.add_argument("--case", type=Path, help="YAML case path; defaults to catalog case_file.")
    candidate_cmd.add_argument("--har", type=Path, help="Raw local ignored HAR to probe; never committed.")
    candidate_cmd.add_argument("--smoke-evidence", type=Path, help="Local write_smoke_run evidence JSON.")
    candidate_cmd.add_argument("--output", type=Path, help="Optional JSON output path.")
    run_cmd = sub.add_parser("run-scenario", help="Run a value-safe HAR→YAML→report closed-loop pipeline.")
    run_cmd.add_argument("--catalog", type=Path, default=DEFAULT_CATALOG)
    run_cmd.add_argument("--scenario-id", required=True)
    run_cmd.add_argument("--har", type=Path, help="Raw local ignored HAR to probe and optionally convert.")
    run_cmd.add_argument("--case", type=Path, help="Existing YAML case. If omitted with --har, YAML is generated.")
    run_cmd.add_argument("--case-output", type=Path, help="Where generated YAML should be written.")
    run_cmd.add_argument("--smoke-evidence", type=Path, help="Existing write_smoke_run evidence JSON.")
    run_cmd.add_argument("--env", default="sit", help="Environment used only when --run-smoke is set.")
    run_cmd.add_argument("--run-smoke", action="store_true", help="Execute generated/existing YAML via write_smoke_run.py.")
    run_cmd.add_argument(
        "--confirm-write",
        default="",
        help=f"Must equal {WRITE_CONFIRM_TOKEN!r} when --run-smoke is used.",
    )
    run_cmd.add_argument("--var", dest="vars", action="append", default=[], help="Forward KEY=VALUE to write_smoke_run.")
    run_cmd.add_argument("--pick-field", dest="pick_fields", action="append", default=[], help="Forward FIELD=VALUE to write_smoke_run.")
    run_cmd.add_argument("--no-readback-assertions", action="store_true", help="Do not add readback assertions to generated YAML.")
    run_cmd.add_argument(
        "--output",
        type=Path,
        default=DEFAULT_OUTPUT_DIR / f"auto_pipeline_{datetime.now():%Y%m%d_%H%M%S}.json",
    )

    args = parser.parse_args(argv)
    if args.cmd == "status":
        report = summarize_progress(load_catalog(args.catalog))
        if args.json:
            print(json.dumps(report, ensure_ascii=False, indent=2))
        else:
            _print_progress(report)
        return 0

    if args.cmd == "expansion-plan":
        plan = build_sample_expansion_plan(load_catalog(args.catalog), limit=args.limit)
        if args.output:
            output = write_json_report(plan, args.output)
            print(f"Sample expansion plan: {output.resolve()}")
        print(json.dumps(plan, ensure_ascii=False, indent=2))
        return 0

    if args.cmd == "scenario-report":
        report = build_report_from_paths(
            catalog_path=args.catalog,
            scenario_id=args.scenario_id,
            case_path=args.case,
            har_path=args.har,
            smoke_evidence_path=args.smoke_evidence,
        )
        output = write_json_report(report, args.output)
        print(f"Scenario pipeline report: {output.resolve()}")
        print(json.dumps({
            "scenario": report.get("scenario", {}),
            "write_verification": report.get("write_verification", {}),
            "failure_or_gap": report.get("failure_or_gap", {}),
        }, ensure_ascii=False, indent=2))
        return 0

    if args.cmd == "readback-plan":
        case = load_yaml_case(args.case)
        plan = build_readback_plan(case)
        if args.output:
            output = write_json_report(plan, args.output)
            print(f"Readback plan: {output.resolve()}")
        print(json.dumps(plan, ensure_ascii=False, indent=2))
        return 0

    if args.cmd == "match-experience":
        case = load_yaml_case(args.case) if args.case else {}
        har_probe = probe_har_chain(args.har) if args.har else {}
        result = match_experience_catalog(
            load_catalog(args.catalog),
            case=case,
            har_probe=har_probe,
            limit=args.limit,
        )
        if args.output:
            output = write_json_report(result, args.output)
            print(f"Experience match report: {output.resolve()}")
        print(json.dumps(result, ensure_ascii=False, indent=2))
        return 0

    if args.cmd == "experience-candidate":
        report = build_report_from_paths(
            catalog_path=args.catalog,
            scenario_id=args.scenario_id,
            case_path=args.case,
            har_path=args.har,
            smoke_evidence_path=args.smoke_evidence,
        )
        candidate = report.get("experience_candidate") or build_experience_candidate({})
        if args.output:
            output = write_json_report(candidate, args.output)
            print(f"Experience candidate: {output.resolve()}")
        print(json.dumps(candidate, ensure_ascii=False, indent=2))
        return 0

    if args.cmd == "run-scenario":
        smoke_evidence = args.smoke_evidence
        first_report = build_auto_pipeline_report(
            catalog_path=args.catalog,
            scenario_id=args.scenario_id,
            case_path=args.case,
            har_path=args.har,
            smoke_evidence_path=smoke_evidence,
            output_dir=args.output.parent,
            generated_case_path=args.case_output,
            include_readback_assertions=not args.no_readback_assertions,
        )
        case_file_raw = (first_report.get("pipeline", {}).get("artifacts") or {}).get("case_file") or ""
        case_file = Path(case_file_raw) if case_file_raw else None
        smoke_result = {}
        if args.run_smoke:
            if args.confirm_write != WRITE_CONFIRM_TOKEN:
                raise SystemExit(f"Refusing to write. Pass --confirm-write {WRITE_CONFIRM_TOKEN}")
            if not case_file:
                raise SystemExit("Cannot run smoke without a generated or existing YAML case.")
            smoke_evidence = smoke_evidence or args.output.parent / f"{args.scenario_id}_write_smoke.json"
            smoke_cmd = [
                sys.executable,
                str(PROJECT_ROOT / "scripts" / "write_smoke_run.py"),
                "--env",
                args.env,
                "--case",
                str(case_file),
                "--confirm-write",
                WRITE_CONFIRM_TOKEN,
                "--output",
                str(smoke_evidence),
            ]
            for item in args.vars:
                smoke_cmd.extend(["--var", item])
            for item in args.pick_fields:
                smoke_cmd.extend(["--pick-field", item])
            completed = subprocess.run(
                smoke_cmd,
                cwd=PROJECT_ROOT,
                text=True,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                check=False,
            )
            smoke_result = {
                "returncode": completed.returncode,
                "evidence": str(smoke_evidence),
                "stdout_tail": completed.stdout[-2000:],
                "stderr_tail": completed.stderr[-2000:],
            }

        final_report = build_auto_pipeline_report(
            catalog_path=args.catalog,
            scenario_id=args.scenario_id,
            case_path=case_file or args.case,
            har_path=args.har,
            smoke_evidence_path=smoke_evidence if smoke_evidence and Path(smoke_evidence).exists() else None,
            output_dir=args.output.parent,
            generated_case_path=args.case_output,
            include_readback_assertions=not args.no_readback_assertions,
        )
        if smoke_result:
            final_report["pipeline"]["smoke_command"] = smoke_result
        output = write_json_report(final_report, args.output)
        print(f"Auto pipeline report: {output.resolve()}")
        print(json.dumps({
            "status": final_report.get("pipeline", {}).get("status"),
            "case_file": final_report.get("pipeline", {}).get("artifacts", {}).get("case_file"),
            "smoke_evidence": final_report.get("pipeline", {}).get("artifacts", {}).get("smoke_evidence"),
            "baseline_candidate": final_report.get("pipeline", {}).get("baseline_candidate"),
            "next_actions": final_report.get("pipeline", {}).get("next_actions"),
        }, ensure_ascii=False, indent=2))
        if smoke_result and smoke_result.get("returncode") != 0:
            return int(smoke_result["returncode"])
        return 0

    return 1


if __name__ == "__main__":
    raise SystemExit(main())
