"""Safe Playwright-based discovery helpers for Kingdee Cosmic pages.

The explorer is intentionally read-only by default. It logs in with the
existing cosmic_login flow, injects cookies into Playwright, opens the home
page, collects menu/form/network hints, and only clicks menu-like labels when
explicitly requested.
"""
from __future__ import annotations

import json
import re
import time
from dataclasses import asdict, dataclass, field
from pathlib import Path
from typing import Any
from urllib.parse import parse_qs, urlparse, urlunparse

from . import cosmic_login
from .pageid_trace import classify_pageid, pageid_fragment


WRITE_ACTION_KEYWORDS = (
    "新增",
    "新建",
    "保存",
    "提交",
    "删除",
    "作废",
    "审核",
    "反审核",
    "启用",
    "禁用",
    "导入",
    "上传",
    "下载模板",
    "确定",
    "确认",
    "批量",
    "同步",
    "生效",
    "失效",
    "关闭",
    "终止",
    "撤销",
    "退回",
    "审批",
)

SAFE_MENU_HINTS = (
    "维护",
    "列表",
    "查询",
    "管理",
    "资料",
    "组织",
    "人员",
    "岗位",
    "申请",
    "提报",
    "报销",
    "基础",
    "设置",
    "门户",
    "中心",
)


@dataclass
class ExplorerConfig:
    base_url: str
    username: str = ""
    password: str = ""
    datacenter_id: str = ""
    datacenter_name: str = ""
    form_id: str = "home_page"
    headless: bool = True
    timeout_ms: int = 30_000
    max_menu_clicks: int = 0
    output: Path = Path("tmp/playwright_discovery/latest.json")
    safe_only: bool = True


@dataclass
class NetworkEvent:
    url: str
    method: str = ""
    status: int | None = None
    resource_type: str = ""
    app_id: str = ""
    form_id: str = ""
    ac: str = ""
    invoke_method: str = ""
    pageid_type: str = ""
    pageid_fragment: str = ""


@dataclass
class DiscoveryReport:
    base_url: str
    home_url: str
    datacenter_id: str
    title: str = ""
    final_url: str = ""
    menu_candidates: list[dict[str, str]] = field(default_factory=list)
    clicked_menus: list[dict[str, Any]] = field(default_factory=list)
    network: list[NetworkEvent] = field(default_factory=list)
    warnings: list[str] = field(default_factory=list)

    def to_dict(self) -> dict[str, Any]:
        data = asdict(self)
        data["network"] = [asdict(item) for item in self.network]
        return data


def normalize_base_url(url: str) -> str:
    """Strip query/fragment and keep the Kingdee app path as the base URL."""
    raw = (url or "").strip()
    if not raw:
        raise ValueError("base_url is required")
    parsed = urlparse(raw)
    if not parsed.scheme or not parsed.netloc:
        raise ValueError(f"invalid base_url: {url!r}")
    path = parsed.path or ""
    if path.endswith("/"):
        path = path[:-1]
    return urlunparse((parsed.scheme, parsed.netloc, path, "", "", "")).rstrip("/")


def build_home_url(base_url: str, form_id: str = "home_page") -> str:
    return f"{normalize_base_url(base_url)}/?formId={form_id or 'home_page'}"


def is_write_action_label(label: str) -> bool:
    text = normalize_label(label)
    return any(keyword in text for keyword in WRITE_ACTION_KEYWORDS)


def normalize_label(label: str) -> str:
    return re.sub(r"\s+", " ", (label or "").strip())


def is_safe_menu_label(label: str) -> bool:
    text = normalize_label(label)
    if not text or len(text) > 40:
        return False
    if is_write_action_label(text):
        return False
    return any(hint in text for hint in SAFE_MENU_HINTS)


def expand_menu_candidates(raw_candidates: list[dict[str, Any]]) -> list[dict[str, str]]:
    """Split DOM text blocks into individual safe menu labels."""
    expanded: list[dict[str, str]] = []
    seen: set[str] = set()
    for item in raw_candidates:
        text = normalize_label(str(item.get("text", "")))
        labels = [text]
        if " " in text:
            labels = [part for part in text.split(" ") if 2 <= len(part) <= 20]
        for label in labels:
            label = normalize_label(label)
            if label in seen or not is_safe_menu_label(label):
                continue
            seen.add(label)
            expanded.append(
                {
                    "text": label,
                    "tag": str(item.get("tag", "")),
                    "role": str(item.get("role", "")),
                    "className": str(item.get("className", "")),
                }
            )
    return expanded


def redact_url(url: str) -> str:
    parsed = urlparse(url)
    return urlunparse((parsed.scheme, parsed.netloc, parsed.path, "", "", ""))


def summarize_kingdee_request(url: str, post_data: str | None = "") -> dict[str, str]:
    """Extract value-safe protocol hints from a Kingdee request."""
    parsed = urlparse(url)
    query = parse_qs(parsed.query)
    body = parse_qs(post_data or "")
    page_id = (body.get("pageId") or query.get("pageId") or [""])[0]
    return {
        "app_id": (query.get("appId") or body.get("appId") or [""])[0],
        "form_id": (query.get("f") or query.get("formId") or body.get("formId") or [""])[0],
        "ac": (query.get("ac") or body.get("ac") or [""])[0],
        "invoke_method": (query.get("method") or body.get("method") or [""])[0],
        "pageid_type": classify_pageid(page_id),
        "pageid_fragment": pageid_fragment(page_id),
    }


def parse_cookie_header(cookie_header: str, base_url: str) -> list[dict[str, Any]]:
    host = urlparse(base_url).hostname or ""
    secure = urlparse(base_url).scheme == "https"
    cookies: list[dict[str, Any]] = []
    for part in (cookie_header or "").split(";"):
        if "=" not in part:
            continue
        name, value = part.split("=", 1)
        name = name.strip()
        value = value.strip()
        if not name:
            continue
        cookies.append(
            {
                "name": name,
                "value": value,
                "domain": host,
                "path": "/",
                "secure": secure,
                "httpOnly": False,
            }
        )
    return cookies


def resolve_datacenter_id(base_url: str, datacenter_id: str = "", datacenter_name: str = "") -> str:
    if datacenter_id:
        return datacenter_id
    if not datacenter_name:
        return ""
    dcs = cosmic_login.list_datacenters(normalize_base_url(base_url))
    target = datacenter_name.strip().lower()
    for dc in dcs:
        if not isinstance(dc, dict):
            continue
        dc_id = str(dc.get("id", dc.get("accountId", dc.get("dcId", ""))))
        dc_name = str(dc.get("name", dc.get("dcName", dc.get("number", ""))))
        if target in dc_name.lower() or target == dc_id.lower():
            return dc_id
    available = ", ".join(
        str(dc.get("name", dc.get("dcName", dc.get("id", "?"))))
        for dc in dcs
        if isinstance(dc, dict)
    )
    raise RuntimeError(f"datacenter not found: {datacenter_name!r}; available: {available}")


def _collect_menu_candidates_script() -> str:
    return """
() => {
  const nodes = Array.from(document.querySelectorAll('a,button,[role="menuitem"],li,span,div'));
  const seen = new Set();
  const out = [];
  for (const el of nodes) {
    const text = (el.innerText || el.textContent || '').replace(/\\s+/g, ' ').trim();
    if (!text || text.length > 40 || seen.has(text)) continue;
    const rect = el.getBoundingClientRect();
    if (rect.width < 8 || rect.height < 8) continue;
    seen.add(text);
    out.push({
      text,
      tag: el.tagName.toLowerCase(),
      role: el.getAttribute('role') || '',
      className: String(el.className || '').slice(0, 120)
    });
    if (out.length >= 300) break;
  }
  return out;
}
"""


def run_discovery(config: ExplorerConfig) -> DiscoveryReport:
    try:
        from playwright.sync_api import TimeoutError as PlaywrightTimeoutError
        from playwright.sync_api import sync_playwright
    except ImportError as exc:  # pragma: no cover - exercised only without optional dependency
        raise RuntimeError(
            "Playwright is not installed. Run: ./venv/bin/pip install playwright && "
            "./venv/bin/python -m playwright install chromium"
        ) from exc

    base_url = normalize_base_url(config.base_url)
    home_url = build_home_url(base_url, config.form_id)
    account_id = resolve_datacenter_id(base_url, config.datacenter_id, config.datacenter_name)
    if not account_id:
        raise RuntimeError("datacenter_id or datacenter_name is required")

    login_result = cosmic_login.login(
        base_url,
        config.username,
        config.password,
        account_id,
        timeout=max(5, int(config.timeout_ms / 1000)),
    )
    if not login_result.get("success"):
        raise RuntimeError(f"login failed: {login_result.get('error', 'unknown error')}")

    report = DiscoveryReport(base_url=base_url, home_url=home_url, datacenter_id=account_id)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=config.headless)
        context = browser.new_context(ignore_https_errors=True)
        context.add_cookies(parse_cookie_header(login_result.get("cookie", ""), base_url))
        if login_result.get("csrf_token"):
            context.set_extra_http_headers({"kd-csrf-token": login_result["csrf_token"]})
        page = context.new_page()
        page.set_default_timeout(config.timeout_ms)

        def on_response(resp: Any) -> None:
            url = resp.url
            if not any(token in url for token in ("batchInvokeAction.do", "getEntityType.do", "showForm.do")):
                return
            try:
                req = resp.request
                hints = summarize_kingdee_request(url, req.post_data or "")
                event = NetworkEvent(
                    url=redact_url(url),
                    method=req.method,
                    status=resp.status,
                    resource_type=req.resource_type,
                    **hints,
                )
                report.network.append(event)
            except Exception:
                return

        page.on("response", on_response)
        page.goto(home_url, wait_until="domcontentloaded")
        try:
            page.wait_for_load_state("networkidle", timeout=config.timeout_ms)
        except PlaywrightTimeoutError:
            report.warnings.append("networkidle timeout after opening home page")

        report.title = page.title()
        report.final_url = redact_url(page.url)
        raw_candidates = page.evaluate(_collect_menu_candidates_script())
        report.menu_candidates = expand_menu_candidates(raw_candidates)[:120]

        click_budget = max(0, int(config.max_menu_clicks))
        for item in report.menu_candidates[:click_budget]:
            label = str(item.get("text", ""))
            if config.safe_only and is_write_action_label(label):
                continue
            started = time.time()
            click_info: dict[str, Any] = {"text": label, "ok": False}
            try:
                page.get_by_text(label, exact=True).first.click(timeout=3_000)
                page.wait_for_timeout(800)
                click_info.update({"ok": True, "url": redact_url(page.url), "elapsed_ms": int((time.time() - started) * 1000)})
            except Exception as exc:
                click_info.update({"error": type(exc).__name__})
            report.clicked_menus.append(click_info)

        context.close()
        browser.close()

    config.output.parent.mkdir(parents=True, exist_ok=True)
    config.output.write_text(json.dumps(report.to_dict(), ensure_ascii=False, indent=2), encoding="utf-8")
    return report
