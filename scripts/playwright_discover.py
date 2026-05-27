#!/usr/bin/env python3
"""Run safe Playwright discovery against a configured Kingdee environment."""
from __future__ import annotations

import argparse
import json
import os
import sys
from datetime import datetime
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(PROJECT_ROOT))

from lib.config import Config
from lib.playwright_explorer import ExplorerConfig, run_discovery


def _parse_menu_samples(value: str) -> list[dict[str, str]]:
    samples: list[dict[str, str]] = []
    for part in (value or "").split(","):
        item = part.strip()
        if not item:
            continue
        if ":" not in item:
            raise SystemExit(f"Invalid --open-menu-samples item: {item!r}; expected 子应用:菜单")
        app_label, menu_label = item.split(":", 1)
        app_label = app_label.strip()
        menu_label = menu_label.strip()
        if not app_label or not menu_label:
            raise SystemExit(f"Invalid --open-menu-samples item: {item!r}; expected 子应用:菜单")
        samples.append({"app_label": app_label, "menu_label": menu_label})
    return samples


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
    parser.add_argument("--app-keyword", default="", help="Target app/menu keyword, e.g. 薪酬福利云")
    parser.add_argument(
        "--drilldown-apps",
        default="",
        help="Comma-separated child app labels to reveal read-only menu panels, e.g. 薪酬管理,薪资核算",
    )
    parser.add_argument(
        "--open-menu-samples",
        default="",
        help="Comma-separated low-risk samples in 子应用:菜单 format, e.g. 薪资数据集成:业务数据提报",
    )
    parser.add_argument("--headful", action="store_true", help="Show browser window")
    parser.add_argument("--record-har", action="store_true", help="Record a local ignored HAR under tmp/playwright_hars")
    parser.add_argument("--har-output", default="", help="Override HAR output path")
    parser.add_argument("--max-menu-clicks", type=int, default=0, help="Default is 0: collect only, no clicks")
    parser.add_argument("--deep-action-plan", type=Path, help="JSON plan for controlled deeper UI actions")
    parser.add_argument("--confirm-write", default="", help="Required for save/new/fill actions in deep plans")
    parser.add_argument("--confirm-workflow", default="", help="Required for submit/audit workflow actions in deep plans")
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

    har_output = ""
    if args.record_har:
        har_output = args.har_output or f"tmp/playwright_hars/discovery_{datetime.now():%Y%m%d_%H%M%S}.har"
    drilldown_apps = [item.strip() for item in args.drilldown_apps.split(",") if item.strip()]
    menu_samples = _parse_menu_samples(args.open_menu_samples)
    deep_action_plan = {}
    if args.deep_action_plan:
        deep_action_plan = json.loads(args.deep_action_plan.read_text(encoding="utf-8"))

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
            target_app_keyword=args.app_keyword,
            record_har_path=Path(har_output) if har_output else None,
            drilldown_apps=drilldown_apps,
            open_menu_samples=menu_samples,
            deep_action_plan=deep_action_plan,
            deep_confirm_write=args.confirm_write,
            deep_confirm_workflow=args.confirm_workflow,
        )
    )
    print(f"Discovery report: {Path(args.output).resolve()}")
    print(f"Title: {report.title}")
    print(f"Safe menu candidates: {len(report.menu_candidates)}")
    print(f"Sub-app explorations: {len(report.subapp_explorations)}")
    print(f"Menu sample explorations: {len(report.menu_sample_explorations)}")
    if report.deep_action_capture:
        validation = report.deep_action_capture.get("validation", {})
        print(f"Deep action plan: {'ok' if validation.get('ok') else 'blocked'}")
    print(f"Captured Kingdee network events: {len(report.network)}")
    if report.har_path:
        print(f"HAR: {Path(report.har_path).resolve()}")
        print(f"HAR Kingdee events: {report.har_summary.get('kingdee_event_count', 0)}")
    if report.warnings:
        print("Warnings:")
        for item in report.warnings:
            print(f"- {item}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
