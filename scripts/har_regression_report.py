#!/usr/bin/env python3
"""CLI wrapper for HAR regression snapshots and impact reports."""
from pathlib import Path
import sys

PROJECT_ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_regression import main


if __name__ == "__main__":
    raise SystemExit(main())
