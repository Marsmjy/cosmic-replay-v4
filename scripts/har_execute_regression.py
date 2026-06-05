#!/usr/bin/env python3
"""Batch HAR import + YAML execution regression runner.

The committed HAR regression report checks parser structure. This script is the
real-environment companion: it imports every HAR in a local directory, executes
the generated YAML through the controlled write smoke runner, and stores a
value-safe baseline summary under tmp/.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import subprocess
import sys
import time
import traceback
from datetime import datetime
from pathlib import Path
from typing import Any

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_extractor import build_yaml_case, preview_har

CONFIRM_TOKEN = "YES_GENERATE_TEST_DATA"
DEFAULT_HAR_DIR = Path("/Users/mars/Desktop/项目归档/回归专用HAR")
DEFAULT_OUTPUT_ROOT = PROJECT_ROOT / "tmp" / "har_execute_regression"
DEFAULT_BASELINE = DEFAULT_OUTPUT_ROOT / "baseline.json"


def _safe_filename(value: str, *, max_len: int = 80) -> str:
    text = re.sub(r"[\\/:*?\"<>|\s]+", "_", value.strip())
    text = re.sub(r"_+", "_", text).strip("_")
    return (text or "sample")[:max_len]


def _sample_id(index: int, har_path: Path) -> str:
    return f"{index:02d}_{_safe_filename(har_path.stem, max_len=48)}"


def _sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as fh:
        for chunk in iter(lambda: fh.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def _count_by(items: list[dict[str, Any]], key: str) -> dict[str, int]:
    counts: dict[str, int] = {}
    for item in items:
        value = str(item.get(key) or "unknown")
        counts[value] = counts.get(value, 0) + 1
    return dict(sorted(counts.items()))


def _preview_summary(preview: dict[str, Any]) -> dict[str, Any]:
    fields = preview.get("field_catalog") or []
    unknown_count = sum(1 for item in fields if item.get("category") in ("", "unknown"))
    response_signature_steps = sum(
        1
        for step in preview.get("steps") or []
        if isinstance(step, dict) and step.get("response_signature")
    )
    return {
        "status": "ok",
        "main_form_id": preview.get("main_form_id", ""),
        "step_count": len(preview.get("steps") or []),
        "vars_count": len(preview.get("detected_vars") or preview.get("vars") or []),
        "pick_fields_count": len(preview.get("pick_fields") or []),
        "field_catalog_count": len(fields),
        "unknown_catalog_count": unknown_count,
        "category_counts": _count_by(fields, "category"),
        "panel_counts": _count_by(fields, "panel"),
        "business_flow_count": len(preview.get("business_flow") or []),
        "response_signature_step_count": response_signature_steps,
        "pageid_risk_level": ((preview.get("pageid_alignment") or {}).get("risk_level") or ""),
        "ir_risk_level": ((preview.get("ir_alignment") or {}).get("risk_level") or ""),
    }


def _load_json(path: Path) -> dict[str, Any]:
    return json.loads(path.read_text(encoding="utf-8"))


def _write_json(path: Path, payload: dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, ensure_ascii=False, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def _write_markdown(path: Path, report: dict[str, Any]) -> None:
    lines = [
        "# HAR 执行回归报告",
        "",
        f"- 生成时间：{report.get('generated_at')}",
        f"- 环境：{report.get('env')}",
        f"- HAR 目录：`{report.get('har_dir')}`",
        f"- 样本数：{report.get('sample_count')}",
        f"- 导入：{report.get('parse_ok')}/{report.get('sample_count')} 成功",
        f"- 执行：{report.get('exec_pass')}/{report.get('exec_total')} 通过",
        f"- baseline：`{report.get('baseline_path')}`",
        "",
        "## 单样本结果",
        "",
    ]
    for item in report.get("results") or []:
        parse_status = (item.get("parse") or {}).get("status", "error")
        execution = item.get("execution") or {}
        status = "PASS" if execution.get("passed") else "FAIL"
        if execution.get("status") in ("not_run", "import_failed"):
            status = execution.get("status", "not_run")
        lines.extend(
            [
                f"### {item.get('id')} · {item.get('title')}",
                f"- 导入：{parse_status}，主表单 `{(item.get('parse') or {}).get('main_form_id', '')}`，步骤 {(item.get('parse') or {}).get('step_count', 0)}",
                f"- 维护项：vars={(item.get('parse') or {}).get('vars_count', 0)}，pick_fields={(item.get('parse') or {}).get('pick_fields_count', 0)}，field_catalog={(item.get('parse') or {}).get('field_catalog_count', 0)}，unknown={(item.get('parse') or {}).get('unknown_catalog_count', 0)}",
                f"- 执行：{status}，分类 `{item.get('failure_kind', '')}`，耗时 {execution.get('duration_s', 0)}s",
            ]
        )
        failed_steps = execution.get("failed_steps") or []
        if failed_steps:
            first = failed_steps[0]
            lines.append(f"- 失败步骤：`{first.get('id', '')}`；{first.get('error', '')[:220]}")
        write_events = execution.get("write_events") or []
        if write_events:
            tokens = sorted({token for event in write_events for token in (event.get("response_tokens") or [])})
            lines.append(f"- 写入锚点：{tokens or ['has_response']}")
        lines.append("")
    comparison = report.get("baseline_comparison") or {}
    if comparison:
        lines.extend(
            [
                "## Baseline 对比",
                "",
                f"- 结果：{comparison.get('status')}",
                f"- 差异数：{comparison.get('diff_count', 0)}",
                "",
            ]
        )
        for diff in comparison.get("diffs") or []:
            lines.append(f"- `{diff.get('sample_id')}` {diff.get('path')}: {diff.get('baseline')} -> {diff.get('current')}")
        lines.append("")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text("\n".join(lines), encoding="utf-8")


def _execution_env() -> dict[str, str]:
    env = os.environ.copy()
    no_proxy_values = ["127.0.0.1", "localhost", "kdhruat.kingdee.com", ".kingdee.com"]
    existing = env.get("NO_PROXY") or env.get("no_proxy") or ""
    merged = []
    for item in [*existing.split(","), *no_proxy_values]:
        item = item.strip()
        if item and item not in merged:
            merged.append(item)
    env["NO_PROXY"] = ",".join(merged)
    env["no_proxy"] = env["NO_PROXY"]
    return env


def _assertion_failed_steps(events: list[dict[str, Any]]) -> list[dict[str, str]]:
    failures: list[dict[str, str]] = []
    for event in events:
        if event.get("event") != "assertion_fail":
            continue
        payload = event.get("payload") or {}
        failures.append({
            "id": str(payload.get("step") or payload.get("step_id") or ""),
            "error": str(payload.get("msg") or payload.get("error") or ""),
        })
    return failures


def _summary_from_evidence(evidence_path: Path, *, returncode: int, duration_s: float, status: str) -> dict[str, Any]:
    evidence = _load_json(evidence_path) if evidence_path.exists() else {}
    summary = evidence.get("summary") or {}
    events = evidence.get("events") or []
    effective_duration = duration_s if duration_s > 0 else float(summary.get("duration_s") or 0)
    failed_steps = list(summary.get("failed_steps") or [])
    assertion_failures = _assertion_failed_steps(events)
    if not failed_steps and assertion_failures:
        failed_steps = assertion_failures
    return {
        "status": status,
        "returncode": returncode,
        "duration_s": round(effective_duration, 3),
        "passed": bool(summary.get("passed")) and returncode == 0,
        "case_name": summary.get("case_name", ""),
        "main_form_id": summary.get("main_form_id", ""),
        "step_count": summary.get("step_count", 0),
        "failed_steps": failed_steps,
        "assertion_failures": assertion_failures,
        "write_events": summary.get("write_events", []),
        "pageid_trace_count": summary.get("pageid_trace_count", 0),
        "evidence_path": str(evidence_path),
    }


def _run_smoke(case_path: Path, evidence_path: Path, *, env_id: str, timeout_s: int) -> dict[str, Any]:
    started = time.monotonic()
    cmd = [
        sys.executable,
        str(PROJECT_ROOT / "scripts" / "write_smoke_run.py"),
        "--env",
        env_id,
        "--case",
        str(case_path),
        "--confirm-write",
        CONFIRM_TOKEN,
        "--output",
        str(evidence_path),
    ]
    try:
        completed = subprocess.run(
            cmd,
            cwd=PROJECT_ROOT,
            env=_execution_env(),
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            timeout=timeout_s,
            check=False,
        )
        duration_s = round(time.monotonic() - started, 3)
    except subprocess.TimeoutExpired as exc:
        return {
            "status": "timeout",
            "returncode": 124,
            "duration_s": round(time.monotonic() - started, 3),
            "passed": False,
            "failed_steps": [{"id": "", "error": f"timeout after {timeout_s}s"}],
            "write_events": [],
            "stdout_tail": (exc.stdout or "")[-2000:] if isinstance(exc.stdout, str) else "",
            "stderr_tail": (exc.stderr or "")[-2000:] if isinstance(exc.stderr, str) else "",
            "evidence_path": str(evidence_path),
        }

    result = _summary_from_evidence(
        evidence_path,
        returncode=completed.returncode,
        duration_s=duration_s,
        status="done",
    )
    result.update({
        "stdout_tail": completed.stdout[-2000:],
        "stderr_tail": completed.stderr[-2000:],
    })
    return result


def _classify_failure(result: dict[str, Any]) -> str:
    execution = result.get("execution") or {}
    parse = result.get("parse") or {}
    if parse.get("status") != "ok":
        return "import_failed"
    if execution.get("status") == "not_run":
        return "not_run"
    if execution.get("passed"):
        return "passed"
    text = " ".join(
        str(step.get("error", ""))
        for step in execution.get("failed_steps") or []
        if isinstance(step, dict)
    )
    text += " " + str(execution.get("stderr_tail") or "")
    lowered = text.lower()
    if execution.get("status") == "timeout" or "timeout" in lowered:
        return "timeout"
    if "refusing to run non-write" in lowered:
        return "non_write_case"
    if "无法修改锁定字段" in text:
        return "locked_field_update"
    if "必填" in text or "请填写" in text:
        return "required_field_missing"
    if "无效请求" in text or "invalid request" in lowered:
        return "invalid_protocol_request"
    if "traceid" in lowered or "java.lang" in lowered or "调用堆栈" in text:
        return "backend_runtime_exception"
    if "pageid" in lowered or "page id" in lowered:
        return "pageid_chain"
    if "attributeerror" in lowered or "typeerror" in lowered:
        return "executor_error"
    return "execution_failed"


def _baseline_view(report: dict[str, Any]) -> dict[str, Any]:
    samples = []
    for item in report.get("results") or []:
        parse = item.get("parse") or {}
        execution = item.get("execution") or {}
        failed_steps = execution.get("failed_steps") or []
        samples.append(
            {
                "id": item.get("id", ""),
                "title": item.get("title", ""),
                "har_sha256": item.get("har_sha256", ""),
                "parse_status": parse.get("status", ""),
                "main_form_id": parse.get("main_form_id", ""),
                "step_count": parse.get("step_count", 0),
                "vars_count": parse.get("vars_count", 0),
                "pick_fields_count": parse.get("pick_fields_count", 0),
                "field_catalog_count": parse.get("field_catalog_count", 0),
                "unknown_catalog_count": parse.get("unknown_catalog_count", 0),
                "business_flow_count": parse.get("business_flow_count", 0),
                "response_signature_step_count": parse.get("response_signature_step_count", 0),
                "execution_status": execution.get("status", ""),
                "passed": bool(execution.get("passed")),
                "failure_kind": item.get("failure_kind", ""),
                "failed_step_ids": [str(step.get("id", "")) for step in failed_steps if isinstance(step, dict)],
                "write_event_count": len(execution.get("write_events") or []),
                "write_event_tokens": sorted(
                    {
                        str(token)
                        for event in execution.get("write_events") or []
                        for token in (event.get("response_tokens") or [])
                    }
                ),
            }
        )
    return {
        "schema_version": 1,
        "env": report.get("env", ""),
        "har_dir": report.get("har_dir", ""),
        "sample_count": report.get("sample_count", 0),
        "parse_ok": report.get("parse_ok", 0),
        "exec_pass": report.get("exec_pass", 0),
        "exec_total": report.get("exec_total", 0),
        "samples": samples,
    }


def _compare_baseline(baseline: dict[str, Any], current: dict[str, Any]) -> dict[str, Any]:
    diffs: list[dict[str, Any]] = []
    baseline_samples = {item.get("id"): item for item in baseline.get("samples") or []}
    current_samples = {item.get("id"): item for item in current.get("samples") or []}
    keys = sorted(set(baseline_samples) | set(current_samples))
    compare_fields = [
        "parse_status",
        "main_form_id",
        "step_count",
        "vars_count",
        "pick_fields_count",
        "field_catalog_count",
        "unknown_catalog_count",
        "business_flow_count",
        "response_signature_step_count",
        "execution_status",
        "passed",
        "failure_kind",
        "failed_step_ids",
        "write_event_count",
        "write_event_tokens",
    ]
    for sample_id in keys:
        before = baseline_samples.get(sample_id)
        after = current_samples.get(sample_id)
        if before is None or after is None:
            diffs.append({
                "sample_id": sample_id,
                "path": "sample",
                "baseline": "missing" if before is None else "present",
                "current": "missing" if after is None else "present",
            })
            continue
        for field in compare_fields:
            if before.get(field) != after.get(field):
                diffs.append({
                    "sample_id": sample_id,
                    "path": field,
                    "baseline": before.get(field),
                    "current": after.get(field),
                })
    return {
        "status": "same" if not diffs else "changed",
        "diff_count": len(diffs),
        "diffs": diffs,
    }


def run_suite(args: argparse.Namespace) -> dict[str, Any]:
    har_dir = args.har_dir.expanduser().resolve()
    if not har_dir.exists():
        raise SystemExit(f"HAR directory not found: {har_dir}")
    har_files = sorted(path for path in har_dir.rglob("*.har") if path.is_file())
    if not har_files:
        raise SystemExit(f"No .har files found under: {har_dir}")

    run_id = args.run_id or datetime.now().strftime("%Y%m%d_%H%M%S")
    output_dir = (args.output_dir or DEFAULT_OUTPUT_ROOT / f"run_{run_id}").resolve()
    case_dir = output_dir / "cases"
    evidence_dir = output_dir / "runs"
    report_path = output_dir / "summary.json"
    markdown_path = output_dir / "summary.md"

    results: list[dict[str, Any]] = []
    started = datetime.now()
    for index, har_path in enumerate(har_files, start=1):
        sample_id = _sample_id(index, har_path)
        title = har_path.stem
        case_path = case_dir / f"{sample_id}.yaml"
        evidence_path = evidence_dir / f"{sample_id}.json"
        result: dict[str, Any] = {
            "id": sample_id,
            "title": title,
            "har_path": str(har_path),
            "har_sha256": _sha256(har_path),
            "case_path": str(case_path),
            "run_path": str(evidence_path),
        }
        print(f"[{index}/{len(har_files)}] import {title}")
        try:
            preview = preview_har(har_path)
            result["parse"] = _preview_summary(preview)
            yaml_text = build_yaml_case(
                har_path,
                case_name=f"execute_regression_{sample_id}",
                include_readback_assertions=not args.no_readback_assertions,
            )
            case_path.parent.mkdir(parents=True, exist_ok=True)
            case_path.write_text(yaml_text, encoding="utf-8")
        except Exception as exc:
            result["parse"] = {
                "status": "error",
                "error": f"{type(exc).__name__}: {exc}",
                "traceback_tail": traceback.format_exc()[-2000:],
            }
            result["execution"] = {"status": "import_failed", "passed": False}
            result["failure_kind"] = "import_failed"
            results.append(result)
            print(f"  -> IMPORT FAIL {result['parse']['error']}")
            continue

        print(f"  execute {case_path.name}")
        if args.skip_execute:
            result["execution"] = {"status": "not_run", "passed": False}
        elif args.reuse_evidence and evidence_path.exists():
            result["execution"] = _summary_from_evidence(
                evidence_path,
                returncode=0,
                duration_s=0.0,
                status="done",
            )
        else:
            result["execution"] = _run_smoke(
                case_path,
                evidence_path,
                env_id=args.env,
                timeout_s=args.timeout,
            )
        result["failure_kind"] = _classify_failure(result)
        results.append(result)
        status = "PASS" if (result.get("execution") or {}).get("passed") else "FAIL"
        print(f"  -> {status} {result['failure_kind']}")

    finished = datetime.now()
    report = {
        "schema_version": 1,
        "generated_at": finished.isoformat(timespec="seconds"),
        "duration_s": round((finished - started).total_seconds(), 3),
        "env": args.env,
        "har_dir": str(har_dir),
        "output_dir": str(output_dir),
        "baseline_path": str(args.baseline.resolve()),
        "sample_count": len(results),
        "parse_ok": sum(1 for item in results if (item.get("parse") or {}).get("status") == "ok"),
        "parse_error": sum(1 for item in results if (item.get("parse") or {}).get("status") != "ok"),
        "exec_total": sum(1 for item in results if (item.get("parse") or {}).get("status") == "ok"),
        "exec_pass": sum(1 for item in results if (item.get("execution") or {}).get("passed")),
        "exec_fail": sum(
            1
            for item in results
            if (item.get("parse") or {}).get("status") == "ok"
            and not (item.get("execution") or {}).get("passed")
        ),
        "results": results,
    }
    current_baseline_view = _baseline_view(report)
    baseline_path = args.baseline.resolve()
    if args.update_baseline:
        _write_json(baseline_path, current_baseline_view)
        report["baseline_comparison"] = {"status": "updated", "diff_count": 0, "diffs": []}
    elif baseline_path.exists():
        report["baseline_comparison"] = _compare_baseline(_load_json(baseline_path), current_baseline_view)
    else:
        report["baseline_comparison"] = {"status": "missing", "diff_count": 0, "diffs": []}
    _write_json(report_path, report)
    _write_markdown(markdown_path, report)
    print(f"Summary JSON: {report_path}")
    print(f"Summary Markdown: {markdown_path}")
    if args.update_baseline:
        print(f"Baseline updated: {baseline_path}")
    return report


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--har-dir", type=Path, default=DEFAULT_HAR_DIR)
    parser.add_argument("--env", default="uat")
    parser.add_argument("--output-dir", type=Path)
    parser.add_argument("--run-id", default="")
    parser.add_argument("--baseline", type=Path, default=DEFAULT_BASELINE)
    parser.add_argument("--update-baseline", action="store_true")
    parser.add_argument("--fail-on-diff", action="store_true")
    parser.add_argument("--skip-execute", action="store_true")
    parser.add_argument("--reuse-evidence", action="store_true", help="Rebuild reports from existing evidence files without executing writes.")
    parser.add_argument("--no-readback-assertions", action="store_true")
    parser.add_argument("--timeout", type=int, default=420)
    args = parser.parse_args(argv)

    report = run_suite(args)
    comparison = report.get("baseline_comparison") or {}
    if args.fail_on_diff and comparison.get("status") == "changed":
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
