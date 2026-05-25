#!/usr/bin/env python3
"""Build a sanitized consultant handoff zip for Cosmic Replay."""
from __future__ import annotations

import argparse
import fnmatch
import sys
import zipfile
from datetime import datetime
from pathlib import Path, PurePosixPath


ROOT = Path(__file__).resolve().parents[1]
DIST = ROOT / "dist"
DEFAULT_NAME = f"cosmic-replay-consultant-handoff-{datetime.now():%Y%m%d}.zip"

EXCLUDED_DIRS = {
    ".git",
    ".idea",
    ".pytest_cache",
    ".ruff_cache",
    ".venv",
    "__pycache__",
    "build",
    "data",
    "dist",
    "downloads",
    "eggs",
    "env",
    "ENV",
    "har_uploads",
    "htmlcov",
    "logs",
    "node_modules",
    "secrets",
    "temp",
    "tmp",
    "venv",
}

EXCLUDED_NAMES = {
    ".DS_Store",
    ".env",
    "config/webui.yaml",
    "debug.log",
    "findings.md",
    "out.txt",
    "pf_result.txt",
    "progress.md",
    "task_plan.md",
}

EXCLUDED_SUFFIXES = {
    ".bak",
    ".db",
    ".db-journal",
    ".db-shm",
    ".db-wal",
    ".har",
    ".jsonl",
    ".log",
    ".pyc",
    ".pyo",
    ".sqlite",
    ".sqlite3",
    ".tmp",
    ".zip",
}

EXCLUDED_FILE_PATTERNS = {
    "_verify_*.py",
    "check_*.py",
    "diag_*.py",
    "entity_*.json",
    "final_check.py",
    "quick_check.py",
    "run_remaining*.py",
    "step*_*.png",
    "test_getEntityType.py",
    "tmp_*.json",
    "verify_*.py",
}

SENSITIVE_ROOTS = {".git", ".venv", "data", "har_uploads", "logs", "secrets", "venv"}


def _relative(path: Path) -> str:
    return path.relative_to(ROOT).as_posix()


def should_exclude(path: Path) -> bool:
    rel = _relative(path)
    parts = path.relative_to(ROOT).parts
    if any(part in EXCLUDED_DIRS for part in parts[:-1]):
        return True
    if path.is_dir() and path.name in EXCLUDED_DIRS:
        return True
    if rel in EXCLUDED_NAMES or path.name in EXCLUDED_NAMES:
        return True
    if rel.startswith("cases/") and path.suffix in {".yaml", ".yml"} and not path.name.endswith(".example.yaml"):
        return True
    if rel.startswith("config/envs/") and path.suffix == ".yaml" and not path.name.endswith(".example.yaml"):
        return True
    if any(path.name.endswith(suffix) for suffix in EXCLUDED_SUFFIXES):
        return True
    if any(fnmatch.fnmatch(path.name, pattern) for pattern in EXCLUDED_FILE_PATTERNS):
        return True
    return False


def iter_project_files() -> list[Path]:
    files: list[Path] = []
    for path in sorted(ROOT.rglob("*")):
        if path == DIST:
            continue
        if should_exclude(path):
            continue
        if path.is_file():
            files.append(path)
    return files


def validate_zip(zip_path: Path) -> None:
    with zipfile.ZipFile(zip_path) as zf:
        names = zf.namelist()
    violations: list[str] = []
    for name in names:
        parts = PurePosixPath(name).parts
        if parts and parts[0] in SENSITIVE_ROOTS:
            violations.append(name)
        if name == "config/webui.yaml":
            violations.append(name)
        if name.startswith("config/envs/") and name.endswith(".yaml") and not name.endswith(".example.yaml"):
            violations.append(name)
        if name.endswith((".har", ".db", ".sqlite", ".sqlite3", ".log", ".jsonl")):
            violations.append(name)
    if violations:
        sample = "\n".join(sorted(set(violations))[:20])
        raise SystemExit(f"Refusing to ship sensitive/generated files:\n{sample}")


def build_zip(output: Path) -> Path:
    DIST.mkdir(exist_ok=True)
    output = output if output.is_absolute() else ROOT / output
    files = iter_project_files()
    demo_gif = ROOT / "dist" / "cosmic-replay-demo.gif"

    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        for path in files:
            zf.write(path, _relative(path))
        if demo_gif.exists():
            zf.write(demo_gif, "cosmic-replay-demo.gif")

    validate_zip(output)
    return output


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--output",
        default=str(DIST / DEFAULT_NAME),
        help="Output zip path. Defaults to dist/cosmic-replay-consultant-handoff-YYYYMMDD.zip",
    )
    args = parser.parse_args(argv)
    zip_path = build_zip(Path(args.output))
    print(zip_path)
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
