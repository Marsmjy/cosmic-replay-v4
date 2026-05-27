#!/usr/bin/env python3
"""Run safe Playwright discovery against a configured Kingdee environment."""
from __future__ import annotations

import argparse
import os
import sys
from datetime import datetime
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.config import Config
from lib.playwright_explorer import ExplorerConfig, run_discovery


def _config_from_env(env_id: str):
    if not env_id:
        return None
    env = Config().get_env(env_id)
    if not env:
        raise SystemExit(f"Environment not found: {env_id}")
    return env


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--env", default="", help="Use config/envs/<env>.yaml without printing credentials")
    parser.add_argument("--base-url", default=os.environ.get("COSMIC_DISCOVER_BASE_URL", ""))
    parser.add_argument("--username", default=os.environ.get("COSMIC_USERNAME", ""))
    parser.add_argument("--password", default=os.environ.get("COSMIC_PASSWORD", ""))
    parser.add_argument("--datacenter-id", default=os.environ.get("COSMIC_DATACENTER_ID", ""))
    parser.add_argument("--datacenter-name", default=os.environ.get("COSMIC_DATACENTER_NAME", ""))
    parser.add_argument("--form-id", default="home_page")
    parser.add_argument("--headful", action="store_true", help="Show browser window")
    parser.add_argument("--max-menu-clicks", type=int, default=0, help="Default is 0: collect only, no clicks")
    parser.add_argument("--timeout-ms", type=int, default=30_000)
    parser.add_argument(
        "--output",
        default=f"tmp/playwright_discovery/discovery_{datetime.now():%Y%m%d_%H%M%S}.json",
    )
    args = parser.parse_args(argv)

    env_cfg = _config_from_env(args.env)
    if env_cfg:
        base_url = args.base_url or env_cfg.base_url
        username = args.username or env_cfg.credentials.resolve_username()
        password = args.password or env_cfg.credentials.resolve_password()
        datacenter_id = args.datacenter_id or env_cfg.datacenter_id
    else:
        base_url = args.base_url
        username = args.username
        password = args.password
        datacenter_id = args.datacenter_id

    missing = [
        name
        for name, value in {
            "base-url": base_url,
            "username": username,
            "password": password,
            "datacenter-id/datacenter-name": datacenter_id or args.datacenter_name,
        }.items()
        if not value
    ]
    if missing:
        raise SystemExit(f"Missing required options: {', '.join(missing)}")

    report = run_discovery(
        ExplorerConfig(
            base_url=base_url,
            username=username,
            password=password,
            datacenter_id=datacenter_id,
            datacenter_name=args.datacenter_name,
            form_id=args.form_id,
            headless=not args.headful,
            timeout_ms=args.timeout_ms,
            max_menu_clicks=args.max_menu_clicks,
            output=Path(args.output),
        )
    )
    print(f"Discovery report: {Path(args.output).resolve()}")
    print(f"Title: {report.title}")
    print(f"Safe menu candidates: {len(report.menu_candidates)}")
    print(f"Captured Kingdee network events: {len(report.network)}")
    if report.warnings:
        print("Warnings:")
        for item in report.warnings:
            print(f"- {item}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
