from pathlib import Path

import pytest

from scripts import core_regression_gate


def test_sensitive_scan_accepts_redacted_har_baselines():
    core_regression_gate._scan_baselines()


def test_real_execution_requires_explicit_write_confirmation(tmp_path: Path):
    with pytest.raises(SystemExit, match="confirm-write"):
        core_regression_gate.main([
            "--skip-pytest",
            "--real-har-dir",
            str(tmp_path),
        ])
