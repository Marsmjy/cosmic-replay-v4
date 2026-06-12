#!/usr/bin/env python3
"""Run the local/CI regression gate for HAR -> IR -> YAML replay contracts."""
from __future__ import annotations

import argparse
import json
import subprocess
import sys
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[1]
BASELINE_ROOT = PROJECT_ROOT / "tests" / "fixtures" / "har_regression"
REAL_WRITE_CONFIRMATION = "YES_GENERATE_TEST_DATA"

sys.path.insert(0, str(PROJECT_ROOT))

from lib.ir.sanitizer import scan_sensitive_text


def _run(command: list[str]) -> None:
    print("+", " ".join(command), flush=True)
    subprocess.run(command, cwd=PROJECT_ROOT, check=True)


def _scan_baselines() -> None:
    risks: list[dict[str, object]] = []
    for path in sorted(BASELINE_ROOT.rglob("*")):
        if not path.is_file():
            continue
        found = scan_sensitive_text(path.read_text(encoding="utf-8", errors="ignore"))
        if found:
            risks.append({
                "path": str(path.relative_to(PROJECT_ROOT)),
                "risks": found,
            })
    if risks:
        raise SystemExit(
            "HAR regression baseline sensitive scan failed:\n"
            + json.dumps(risks, ensure_ascii=False, indent=2)
        )
    print(f"sensitive scan: ok ({BASELINE_ROOT.relative_to(PROJECT_ROOT)})")


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--skip-pytest", action="store_true")
    parser.add_argument("--real-har-dir", type=Path)
    parser.add_argument("--real-env", default="auto")
    parser.add_argument("--real-baseline", type=Path)
    parser.add_argument("--confirm-write", default="")
    parser.add_argument("--reuse-evidence", action="store_true")
    args = parser.parse_args(argv)

    python = str(PROJECT_ROOT / "venv" / "bin" / "python")
    if not Path(python).exists():
        python = sys.executable

    if (
        args.real_har_dir
        and args.confirm_write != REAL_WRITE_CONFIRMATION
        and not args.reuse_evidence
    ):
        raise SystemExit(
            "Real HAR execution requires "
            f"--confirm-write {REAL_WRITE_CONFIRMATION}; "
            "use --reuse-evidence for a non-writing report rebuild."
        )

    if not args.skip_pytest:
        _run([python, "-m", "pytest", "-q"])
    _run([
        python,
        "scripts/har_regression_report.py",
        "compare",
        "--fail-on-diff",
    ])
    _scan_baselines()

    if args.real_har_dir:
        command = [
            python,
            "scripts/har_execute_regression.py",
            "--har-dir",
            str(args.real_har_dir),
            "--env",
            args.real_env,
            "--fail-on-diff",
        ]
        if args.real_baseline:
            command.extend(["--baseline", str(args.real_baseline)])
        if args.reuse_evidence:
            command.append("--reuse-evidence")
        _run(command)

    print("core regression gate: passed")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
