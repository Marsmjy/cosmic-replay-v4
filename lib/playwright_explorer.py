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
from collections import Counter
from dataclasses import asdict, dataclass, field
from pathlib import Path
from typing import Any
from urllib.parse import parse_qs, urlparse, urlunparse

from . import cosmic_login
from .pageid_trace import classify_pageid, pageid_fragment
from .playwright_deep_actions import render_action_value, validate_action_plan


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
    "薪酬",
    "薪资",
    "福利",
    "社保",
    "公积金",
    "计薪",
    "任职",
    "工资条",
    "基础",
    "设置",
    "门户",
    "中心",
)

NOISE_MENU_LABELS = {
    "开发者门户",
    "跨环境传输中心",
    "帮助中心",
    "查询我的登录历史",
    "查询我的登录历史忽略",
}

L2_CONTEXT_ACS = {
    "addnew",
    "entryRowClick",
    "itemClick",
    "loadData",
    "menuItemClick",
    "postExpandNodes",
    "queryTreeNodeChildren",
    "refresh",
    "treeMenuClick",
    "treeNodeClick",
}

L3_WRITE_ACS = {
    "audit",
    "save",
    "saveandaudit",
    "saveandeffect",
    "submit",
    "submitandeffect",
    "unaudit",
}


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
    target_app_keyword: str = ""
    record_har_path: Path | None = None
    search_app_launcher: bool = True
    drilldown_apps: list[str] = field(default_factory=list)
    open_menu_samples: list[dict[str, str]] = field(default_factory=list)
    deep_action_plan: dict[str, Any] = field(default_factory=dict)
    deep_confirm_write: str = ""
    deep_confirm_workflow: str = ""


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
    target_app_keyword: str = ""
    target_app_status: dict[str, Any] = field(default_factory=dict)
    menu_candidates: list[dict[str, str]] = field(default_factory=list)
    app_tree: list[dict[str, Any]] = field(default_factory=list)
    subapp_explorations: list[dict[str, Any]] = field(default_factory=list)
    menu_sample_explorations: list[dict[str, Any]] = field(default_factory=list)
    menu_tree: list[dict[str, Any]] = field(default_factory=list)
    clicked_menus: list[dict[str, Any]] = field(default_factory=list)
    deep_action_capture: dict[str, Any] = field(default_factory=dict)
    network: list[NetworkEvent] = field(default_factory=list)
    har_path: str = ""
    har_summary: dict[str, Any] = field(default_factory=dict)
    pageid_trace: list[dict[str, Any]] = field(default_factory=list)
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
    if text in NOISE_MENU_LABELS or "登录历史" in text:
        return False
    if is_write_action_label(text):
        return False
    return any(hint in text for hint in SAFE_MENU_HINTS)


def risk_level_for_label(label: str) -> str:
    text = normalize_label(label)
    if is_write_action_label(text):
        return "high"
    if any(keyword in text for keyword in ("发放", "计算", "结账", "导入", "审批", "批量")):
        return "medium"
    return "low"


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


def infer_pageid_context_role(ac: str = "", method: str = "") -> str:
    ac = str(ac or "")
    method = str(method or "")
    if ac in {"getMenuData", "getFrequentData"}:
        return "app_menu_catalog"
    if ac in {"clientCallBack", "customEvent", "selectTab"}:
        return "portal_callback"
    if ac in L3_WRITE_ACS:
        return "L3_write"
    if ac in L2_CONTEXT_ACS or method == "itemClick":
        return "L2_context"
    if ac == "click" or method == "click":
        return "L3_or_ui_action"
    return "unknown"


def keyword_variants(keyword: str) -> list[str]:
    """Build safe search variants for app/menu discovery."""
    text = normalize_label(keyword)
    if not text:
        return []
    variants = [text]
    trimmed = re.sub(r"[云中心]+$", "", text)
    if trimmed and trimmed != text:
        variants.append(trimmed)
    if "薪酬" in text:
        variants.extend(["薪酬", "薪资"])
    if "福利" in text:
        variants.append("福利")
    if "提报" in text:
        variants.append("提报")
    seen: set[str] = set()
    out: list[str] = []
    for item in variants:
        item = normalize_label(item)
        if item and item not in seen:
            seen.add(item)
            out.append(item)
    return out


def summarize_har_file(path: Path) -> dict[str, Any]:
    """Return a value-safe HAR summary focused on Kingdee protocol shape."""
    if not path or not path.exists():
        return {}
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc:
        return {"error": f"failed_to_read_har: {type(exc).__name__}"}

    entries = data.get("log", {}).get("entries", [])
    pageid_rows: list[dict[str, Any]] = []
    ac_counter: Counter[str] = Counter()
    form_counter: Counter[str] = Counter()
    pageid_counter: Counter[str] = Counter()

    for index, entry in enumerate(entries):
        req = entry.get("request") or {}
        url = str(req.get("url") or "")
        if "batchInvokeAction.do" not in url and "getEntityType.do" not in url and "showForm.do" not in url:
            continue
        post_data = _har_post_data_text(req.get("postData") or {})
        hints = summarize_kingdee_request(url, post_data)
        ac_counter[hints["ac"] or "-"] += 1
        form_counter[hints["form_id"] or "-"] += 1
        pageid_counter[hints["pageid_type"] or "missing"] += 1
        row = {
            "index": index,
            "url": redact_url(url),
            "method": req.get("method", ""),
            "status": (entry.get("response") or {}).get("status"),
            **hints,
            "expected_pageid_role": infer_pageid_context_role(hints.get("ac", ""), hints.get("invoke_method", "")),
        }
        pageid_rows.append(row)

    return {
        "entry_count": len(entries),
        "kingdee_event_count": len(pageid_rows),
        "ac_counts": dict(ac_counter.most_common()),
        "form_counts": dict(form_counter.most_common(20)),
        "pageid_type_counts": dict(pageid_counter.most_common()),
        "pageid_trace": pageid_rows[:300],
    }


def _har_post_data_text(post_data: dict[str, Any]) -> str:
    text = post_data.get("text")
    if isinstance(text, str):
        return text
    params = post_data.get("params") or []
    if not isinstance(params, list):
        return ""
    return "&".join(
        f"{item.get('name', '')}={item.get('value', '')}"
        for item in params
        if isinstance(item, dict) and item.get("name")
    )


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


def summarize_menu_tree(
    menu_candidates: list[dict[str, str]],
    network_events: list[NetworkEvent],
    *,
    app_name: str = "",
    url: str = "",
) -> list[dict[str, Any]]:
    first_event = network_events[0] if network_events else NetworkEvent(url="")
    rows: list[dict[str, Any]] = []
    for item in menu_candidates:
        label = item.get("text", "")
        rows.append(
            {
                "menu_text": label,
                "app_name": app_name,
                "form_id": first_event.form_id,
                "app_id": first_event.app_id,
                "url": url,
                "first_request": asdict(first_event) if first_event.url else {},
                "pageid_type": first_event.pageid_type or "missing",
                "readonly": risk_level_for_label(label) != "high",
                "has_new_button": False,
                "has_save_button": False,
                "has_submit_button": False,
                "risk_level": risk_level_for_label(label),
            }
        )
    return rows


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


def collect_menu_candidates(page: Any, *, min_x: int = 0, limit: int = 120) -> list[dict[str, str]]:
    """Collect value-safe visible text candidates, optionally excluding the left app tree."""
    try:
        raw_candidates = page.evaluate(
            """
minX => {
  const nodes = Array.from(document.querySelectorAll('a,button,[role="menuitem"],li,span,div'));
  const seen = new Set();
  const out = [];
  for (const el of nodes) {
    const text = (el.innerText || el.textContent || '').replace(/\\s+/g, ' ').trim();
    if (!text || text.length > 40 || seen.has(text)) continue;
    const rect = el.getBoundingClientRect();
    if (rect.width < 8 || rect.height < 8 || rect.x < minX) continue;
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
""",
            min_x,
        )
    except Exception:
        raw_candidates = page.evaluate(_collect_menu_candidates_script())
    return expand_menu_candidates(raw_candidates)[:limit]


def _button_state_script() -> str:
    return """
() => {
  const text = Array.from(document.querySelectorAll('button,a,[role="button"],span,div'))
    .map(el => (el.innerText || el.textContent || '').replace(/\\s+/g, ' ').trim())
    .filter(Boolean)
    .join(' ');
  return {
    has_new_button: /新增|新建/.test(text),
    has_save_button: /保存/.test(text),
    has_submit_button: /提交/.test(text)
  };
}
"""


def _collect_app_tree_script() -> str:
    return """
() => {
  const clouds = Array.from(document.querySelectorAll('.kd-cq-tile-cloud-item'));
  const rows = [];
  const seenClouds = new Set();
  for (const cloud of clouds) {
    const cloudText = (cloud.innerText || cloud.textContent || '').replace(/\\s+/g, ' ').trim();
    if (!cloudText || seenClouds.has(cloudText)) continue;
    seenClouds.add(cloudText);
    const root = cloud.closest('li') || cloud.parentElement;
    if (!root) continue;
    const apps = [];
    const seenApps = new Set();
    for (const item of Array.from(root.querySelectorAll('.kd-cq-tile-app-list-item, li, span, div'))) {
      const text = (item.innerText || item.textContent || '').replace(/\\s+/g, ' ').trim();
      if (!text || text === cloudText || text.length > 40 || seenApps.has(text)) continue;
      const rect = item.getBoundingClientRect();
      if (rect.width < 8 || rect.height < 8) continue;
      seenApps.add(text);
      apps.push({
        text,
        tag: item.tagName.toLowerCase(),
        className: String(item.className || '').slice(0, 120)
      });
      if (apps.length >= 80) break;
    }
    const rect = cloud.getBoundingClientRect();
    rows.push({
      cloud_text: cloudText,
      x: Math.round(rect.x),
      y: Math.round(rect.y),
      app_count: apps.length,
      apps
    });
    if (rows.length >= 120) break;
  }
  return rows;
}
"""


def collect_app_tree(page: Any) -> list[dict[str, Any]]:
    """Collect a value-safe app cloud tree from Kingdee's all-app launcher."""
    try:
        raw_rows = page.evaluate(_collect_app_tree_script())
    except Exception:
        return []
    rows: list[dict[str, Any]] = []
    for row in raw_rows or []:
        apps = []
        for item in row.get("apps") or []:
            text = normalize_label(str(item.get("text", "")))
            if not text:
                continue
            apps.append(
                {
                    "text": text,
                    "risk_level": risk_level_for_label(text),
                    "readonly": not is_write_action_label(text),
                    "tag": str(item.get("tag", "")),
                    "className": str(item.get("className", "")),
                }
            )
        rows.append(
            {
                "cloud_text": normalize_label(str(row.get("cloud_text", ""))),
                "app_count": len(apps),
                "apps": apps,
            }
        )
    return rows


def _visible_app_search_inputs_script() -> str:
    return """
() => Array.from(document.querySelectorAll('input'))
  .map((el, index) => {
    const rect = el.getBoundingClientRect();
    const style = getComputedStyle(el);
    return {
      index,
      placeholder: el.getAttribute('placeholder') || '',
      visible: rect.width > 8 && rect.height > 8 && style.display !== 'none' && style.visibility !== 'hidden',
      x: Math.round(rect.x),
      y: Math.round(rect.y),
      width: Math.round(rect.width),
      height: Math.round(rect.height),
      className: String(el.className || '').slice(0, 120)
    };
  })
  .filter(item => item.placeholder || item.visible)
"""


def _try_open_app_launcher(page: Any, timeout_ms: int = 2_000) -> dict[str, Any]:
    """Open the Kingdee all-app launcher without touching business actions."""
    result: dict[str, Any] = {"ok": False, "method": ""}
    selectors = [
        '[class*="quanbuyingyong"]',
        '[aria-label*="应用"]',
        '[title*="应用"]',
    ]
    for selector in selectors:
        try:
            locator = page.locator(selector).first
            if locator.count() < 1:
                continue
            locator.click(timeout=timeout_ms)
            page.wait_for_timeout(800)
            result.update({"ok": True, "method": f"selector:{selector}"})
            break
        except Exception as exc:
            result.setdefault("selector_errors", []).append({"selector": selector, "error": type(exc).__name__})

    if not result.get("ok"):
        try:
            # Kingdee desktop keeps the all-app icon at the top-left corner.
            # This is still a read-only launcher open, not a business action.
            page.mouse.click(24, 24)
            page.wait_for_timeout(800)
            result.update({"ok": True, "method": "coordinate:24,24"})
        except Exception as exc:
            result.update({"error": type(exc).__name__})

    try:
        result["search_inputs"] = page.evaluate(_visible_app_search_inputs_script())
    except Exception:
        result["search_inputs"] = []
    if not any(item.get("visible") and item.get("placeholder") for item in result.get("search_inputs", [])):
        result["ok"] = False
        result.setdefault("error", "search_input_not_visible")
    return result


def _is_app_launcher_open(page: Any) -> bool:
    try:
        inputs = page.evaluate(_visible_app_search_inputs_script())
    except Exception:
        return False
    return any(item.get("visible") and item.get("placeholder") for item in inputs)


def _ensure_app_launcher_open(page: Any, timeout_ms: int = 2_000) -> dict[str, Any]:
    if _is_app_launcher_open(page):
        return {"ok": True, "method": "already_open"}
    return _try_open_app_launcher(page, timeout_ms=timeout_ms)


def _try_search_app_launcher(page: Any, keyword: str, timeout_ms: int = 2_000) -> dict[str, Any]:
    """Search the app/form launcher and return value-safe visible matches."""
    text = normalize_label(keyword)
    result: dict[str, Any] = {"keyword": text, "ok": False, "matches": []}
    if not text:
        result["error"] = "empty_keyword"
        return result
    selectors = [
        'input[placeholder="搜索应用/表单"]',
        'input[placeholder="请输入表单名称"]',
    ]
    for selector in selectors:
        try:
            locator = page.locator(selector).first
            if locator.count() < 1:
                continue
            box = locator.bounding_box(timeout=timeout_ms)
            if not box or box.get("width", 0) <= 8 or box.get("height", 0) <= 8:
                continue
            locator.fill(text, timeout=timeout_ms)
            page.wait_for_timeout(1_200)
            result.update({"ok": True, "selector": selector})
            break
        except Exception as exc:
            result.setdefault("selector_errors", []).append({"selector": selector, "error": type(exc).__name__})

    if not result.get("ok"):
        try:
            js_result = page.evaluate(
                """
keyword => {
  const candidates = Array.from(document.querySelectorAll('input'))
    .filter(el => {
      const rect = el.getBoundingClientRect();
      const style = getComputedStyle(el);
      const placeholder = el.getAttribute('placeholder') || '';
      return rect.width > 8 && rect.height > 8
        && style.display !== 'none'
        && style.visibility !== 'hidden'
        && /搜索应用|表单名称/.test(placeholder);
    });
  const el = candidates[0];
  if (!el) return { ok: false, error: 'visible_search_input_not_found' };
  el.focus();
  el.value = keyword;
  el.dispatchEvent(new InputEvent('input', { bubbles: true, inputType: 'insertText', data: keyword }));
  el.dispatchEvent(new Event('change', { bubbles: true }));
  return { ok: true, placeholder: el.getAttribute('placeholder') || '' };
}
""",
                text,
            )
            if js_result.get("ok"):
                page.wait_for_timeout(1_200)
                result.update({"ok": True, "selector": f"js:{js_result.get('placeholder', '')}"})
            else:
                result.update(js_result)
        except Exception as exc:
            result.update({"error": type(exc).__name__})

    try:
        result["matches"] = [
            item.get("text", "")
            for item in expand_menu_candidates(page.evaluate(_collect_menu_candidates_script()))
            if any(part in item.get("text", "") for part in keyword_variants(text))
        ][:40]
    except Exception:
        result["matches"] = []
    return result


def _try_click_main_area_text(page: Any, label: str, min_x: int = 245, timeout_ms: int = 2_000) -> dict[str, Any]:
    text = normalize_label(label)
    risk = risk_level_for_label(text)
    result: dict[str, Any] = {"text": text, "ok": False, "risk_level": risk}
    if is_write_action_label(text) or risk == "high":
        result["blocked"] = "write_action"
        return result
    if risk == "medium":
        result["blocked"] = "medium_risk_requires_explicit_human_review"
        return result
    try:
        js_result = page.evaluate(
            """
({ text, minX }) => {
  const normalize = value => (value || '').replace(/\\s+/g, ' ').trim();
  const nodes = Array.from(document.querySelectorAll('a,button,[role="menuitem"],li,span,div'));
  const el = nodes.find(node => {
    const label = normalize(node.innerText || node.textContent || '');
    const rect = node.getBoundingClientRect();
    return label === text && rect.width > 8 && rect.height > 8 && rect.x >= minX;
  });
  if (!el) return { ok: false, error: 'visible_menu_not_found' };
  el.scrollIntoView({ block: 'center', inline: 'nearest' });
  const rect = el.getBoundingClientRect();
  const eventInit = { bubbles: true, cancelable: true, clientX: rect.x + 10, clientY: rect.y + 10 };
  el.dispatchEvent(new MouseEvent('mouseover', eventInit));
  el.dispatchEvent(new MouseEvent('mouseenter', eventInit));
  el.dispatchEvent(new MouseEvent('mousedown', eventInit));
  el.click();
  el.dispatchEvent(new MouseEvent('mouseup', eventInit));
  return {
    ok: true,
    x: Math.round(rect.x),
    y: Math.round(rect.y),
    width: Math.round(rect.width),
    height: Math.round(rect.height)
  };
}
""",
            {"text": text, "minX": min_x},
        )
        result.update(js_result)
        if js_result.get("ok"):
            page.wait_for_timeout(timeout_ms)
            result["url"] = redact_url(page.url)
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _try_click_text(page: Any, label: str, timeout_ms: int = 3_000) -> dict[str, Any]:
    started = time.time()
    result: dict[str, Any] = {"text": label, "ok": False}
    if is_write_action_label(label):
        result["blocked"] = "write_action"
        return result
    try:
        locator = page.get_by_text(label, exact=True)
        total = locator.count()
        if total < 1:
            locator = page.get_by_text(label, exact=False)
            total = locator.count()
        if total < 1:
            result["error"] = "not_found"
            return result
        target = None
        for index in range(min(total, 12)):
            item = locator.nth(index)
            try:
                box = item.bounding_box(timeout=500)
            except Exception:
                box = None
            if box and box.get("width", 0) > 8 and box.get("height", 0) > 8:
                target = item
                break
        if target is None:
            result["error"] = "visible_target_not_found"
            return result
        try:
            target.click(timeout=timeout_ms)
        except Exception:
            # Some Kingdee tile/list items are visually clickable but wrapped by
            # overlay containers. Force is only used after write labels are blocked.
            target.click(timeout=timeout_ms, force=True)
        page.wait_for_timeout(1_000)
        result.update({"ok": True, "url": redact_url(page.url), "elapsed_ms": int((time.time() - started) * 1000)})
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _click_text_without_risk_check(page: Any, label: str, timeout_ms: int = 3_000) -> dict[str, Any]:
    """Click text after the whole deep action plan has passed risk validation."""
    started = time.time()
    result: dict[str, Any] = {"text": normalize_label(label), "ok": False}
    try:
        locator = page.get_by_text(label, exact=True)
        if locator.count() < 1:
            locator = page.get_by_text(label, exact=False)
        total = locator.count()
        if total < 1:
            result["error"] = "not_found"
            return result
        target = None
        target_box = None
        for index in range(min(total, 20)):
            item = locator.nth(index)
            try:
                box = item.bounding_box(timeout=500)
            except Exception:
                box = None
            if box and box.get("width", 0) > 8 and box.get("height", 0) > 8:
                target = item
                target_box = box
                break
        if target is None:
            result["error"] = "visible_target_not_found"
            return result
        try:
            target.click(timeout=timeout_ms)
        except Exception:
            target.click(timeout=timeout_ms, force=True)
        page.wait_for_timeout(800)
        result.update({
            "ok": True,
            "url": redact_url(page.url),
            "elapsed_ms": int((time.time() - started) * 1000),
            "x": round(target_box.get("x", 0)) if target_box else None,
            "y": round(target_box.get("y", 0)) if target_box else None,
        })
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _fill_text_control(page: Any, action: dict[str, Any], timeout_ms: int = 3_000) -> dict[str, Any]:
    label = normalize_label(str(action.get("label") or action.get("placeholder") or action.get("selector") or ""))
    value = str(render_action_value(action.get("value") or ""))
    result: dict[str, Any] = {"label": label, "ok": False, "value_shape": "empty" if not value else "literal"}
    try:
        selector = str(action.get("selector") or "")
        if selector:
            locator = page.locator(selector).first
        elif action.get("placeholder"):
            locator = page.get_by_placeholder(str(action["placeholder"]), exact=False).first
        else:
            locator = page.get_by_label(label, exact=False).first
        locator.fill(value, timeout=timeout_ms)
        page.wait_for_timeout(300)
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _click_selector_control(page: Any, action: dict[str, Any], timeout_ms: int = 3_000) -> dict[str, Any]:
    selector = str(action.get("selector") or "")
    result: dict[str, Any] = {"selector": selector, "ok": False}
    if not selector:
        result["error"] = "selector_required"
        return result
    try:
        locator = page.locator(selector).first
        locator.click(timeout=timeout_ms, force=bool(action.get("force")))
        page.wait_for_timeout(int(action.get("after_ms") or 500))
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _click_at_control(page: Any, action: dict[str, Any]) -> dict[str, Any]:
    x = action.get("x")
    y = action.get("y")
    result: dict[str, Any] = {"x": x, "y": y, "ok": False}
    try:
        page.mouse.click(float(x), float(y))
        page.wait_for_timeout(int(action.get("after_ms") or 500))
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _fill_at_control(page: Any, action: dict[str, Any]) -> dict[str, Any]:
    x = action.get("x")
    y = action.get("y")
    value = str(render_action_value(action.get("value") or ""))
    result: dict[str, Any] = {"x": x, "y": y, "ok": False, "value_shape": "empty" if not value else "literal"}
    try:
        page.mouse.click(float(x), float(y))
        if action.get("clear"):
            page.keyboard.press("Meta+A")
            page.keyboard.press("Backspace")
        page.keyboard.type(value, delay=int(action.get("delay_ms") or 20))
        if action.get("press"):
            page.keyboard.press(str(action.get("press")))
        page.wait_for_timeout(int(action.get("after_ms") or 500))
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _press_control(page: Any, action: dict[str, Any], timeout_ms: int = 3_000) -> dict[str, Any]:
    selector = str(action.get("selector") or "")
    key = str(action.get("key") or "Enter")
    result: dict[str, Any] = {"selector": selector, "key": key, "ok": False}
    try:
        if selector:
            page.locator(selector).first.press(key, timeout=timeout_ms)
        else:
            page.keyboard.press(key)
        page.wait_for_timeout(int(action.get("after_ms") or 300))
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _wait_for_selector_control(page: Any, action: dict[str, Any], timeout_ms: int = 5_000) -> dict[str, Any]:
    selector = str(action.get("selector") or "")
    state = str(action.get("state") or "visible")
    result: dict[str, Any] = {"selector": selector, "state": state, "ok": False}
    if not selector:
        result["error"] = "selector_required"
        return result
    try:
        page.locator(selector).first.wait_for(state=state, timeout=timeout_ms)
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _select_option_control(page: Any, action: dict[str, Any], timeout_ms: int = 3_000) -> dict[str, Any]:
    selector = str(action.get("selector") or "")
    result: dict[str, Any] = {"selector": selector, "ok": False}
    if not selector:
        result["error"] = "selector_required"
        return result
    try:
        locator = page.locator(selector).first
        option_value = render_action_value(action.get("value"))
        option_label = render_action_value(action.get("label"))
        if option_value not in (None, ""):
            locator.select_option(value=str(option_value), timeout=timeout_ms)
        elif option_label not in (None, ""):
            locator.select_option(label=str(option_label), timeout=timeout_ms)
        else:
            result["error"] = "value_or_label_required"
            return result
        page.wait_for_timeout(int(action.get("after_ms") or 300))
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _snapshot_controls(page: Any, action: dict[str, Any]) -> dict[str, Any]:
    selector = str(action.get("selector") or "body")
    text_filter = normalize_label(str(action.get("text_filter") or "")).lower()
    limit = max(1, min(int(action.get("limit") or 80), 200))
    result: dict[str, Any] = {"selector": selector, "ok": False, "items": []}
    script = """
      ({ selector, textFilter, limit }) => {
        const root = document.querySelector(selector) || document.body;
        const candidates = Array.from(root.querySelectorAll(
          'button,input,textarea,select,span,label,[role],a,[class*="button"],[class*="btn"],[class*="label"],[class*="grid"],[class*="toolbar"],[class*="panel"]'
        ));
        const rows = [];
        for (const el of candidates) {
          const rect = el.getBoundingClientRect();
          const style = window.getComputedStyle(el);
          if (rect.width < 2 || rect.height < 2 || style.visibility === 'hidden' || style.display === 'none') continue;
          const text = (el.innerText || el.textContent || el.getAttribute('aria-label') || el.getAttribute('title') || el.getAttribute('placeholder') || '').trim().replace(/\\s+/g, ' ');
          if (textFilter && !text.toLowerCase().includes(textFilter)) continue;
          const id = el.id || '';
          const cls = (el.className && typeof el.className === 'string') ? el.className.split(/\\s+/).slice(0, 4).join('.') : '';
          rows.push({
            tag: el.tagName.toLowerCase(),
            role: el.getAttribute('role') || '',
            text: text.slice(0, 80),
            placeholder: el.getAttribute('placeholder') || '',
            title: el.getAttribute('title') || '',
            aria: el.getAttribute('aria-label') || '',
            data_key: el.getAttribute('data-key') || el.getAttribute('data-control-key') || el.getAttribute('data-page-id') || '',
            id,
            class_hint: cls,
            selector_hint: id ? `#${id}` : (cls ? `${el.tagName.toLowerCase()}.${cls}` : el.tagName.toLowerCase()),
            x: Math.round(rect.x),
            y: Math.round(rect.y),
            w: Math.round(rect.width),
            h: Math.round(rect.height)
          });
          if (rows.length >= limit) break;
        }
        return rows;
      }
    """
    try:
        result["items"] = page.evaluate(script, {
            "selector": selector,
            "textFilter": text_filter,
            "limit": limit,
        })
        result["ok"] = True
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _execute_deep_action_plan(
    page: Any,
    plan: dict[str, Any],
    *,
    confirm_write: str,
    confirm_workflow: str,
) -> dict[str, Any]:
    validation = validate_action_plan(
        plan,
        confirm_write=confirm_write,
        confirm_workflow=confirm_workflow,
    )
    result: dict[str, Any] = {"validation": validation, "actions": []}
    if not validation.get("ok"):
        result["blocked"] = True
        return result

    for index, action in enumerate(plan.get("actions") or []):
        if not isinstance(action, dict):
            continue
        action_type = str(action.get("type") or "click_text")
        row = {"index": index, "type": action_type, "id": action.get("id", "")}
        if action_type == "click_text":
            row.update(_click_text_without_risk_check(page, str(action.get("text") or ""), timeout_ms=3_000))
        elif action_type in {"fill", "fill_text"}:
            row.update(_fill_text_control(page, action, timeout_ms=3_000))
        elif action_type == "fill_at":
            row.update(_fill_at_control(page, action))
        elif action_type in {"click_selector", "click_locator"}:
            row.update(_click_selector_control(page, action, timeout_ms=3_000))
        elif action_type == "click_at":
            row.update(_click_at_control(page, action))
        elif action_type == "press":
            row.update(_press_control(page, action, timeout_ms=3_000))
        elif action_type == "wait_for_selector":
            row.update(_wait_for_selector_control(page, action, timeout_ms=5_000))
        elif action_type == "select_option":
            row.update(_select_option_control(page, action, timeout_ms=3_000))
        elif action_type == "snapshot_controls":
            row.update(_snapshot_controls(page, action))
        elif action_type == "wait":
            ms = int(action.get("ms") or 800)
            page.wait_for_timeout(max(0, min(ms, 10_000)))
            row["ok"] = True
            row["ms"] = ms
        else:
            row.update({"ok": False, "error": "unsupported_action_type"})
        result["actions"].append(row)
        if not row.get("ok") and not action.get("optional"):
            result["stopped_at"] = index
            break
    return result


def _try_click_tile_text(page: Any, label: str, selector: str, timeout_ms: int = 3_000) -> dict[str, Any]:
    """Click an exact visible Kingdee app launcher tile by selector and text."""
    text = normalize_label(label)
    result: dict[str, Any] = {"text": text, "ok": False, "selector": selector}
    if is_write_action_label(text):
        result["blocked"] = "write_action"
        return result
    if not text:
        result["error"] = "empty_label"
        return result
    try:
        js_result = page.evaluate(
            """
({ selector, text }) => {
  const normalize = value => (value || '').replace(/\\s+/g, ' ').trim();
  const nodes = Array.from(document.querySelectorAll(selector));
  const el = nodes.find(node => {
    const label = normalize(node.innerText || node.textContent || '');
    const rect = node.getBoundingClientRect();
    return label === text && rect.width > 8 && rect.height > 8;
  });
  if (!el) return { ok: false, error: 'visible_tile_not_found' };
  el.scrollIntoView({ block: 'center', inline: 'nearest' });
  const rect = el.getBoundingClientRect();
  const eventInit = { bubbles: true, cancelable: true, clientX: rect.x + 10, clientY: rect.y + 10 };
  el.dispatchEvent(new MouseEvent('mouseover', eventInit));
  el.dispatchEvent(new MouseEvent('mouseenter', eventInit));
  el.dispatchEvent(new MouseEvent('mousedown', eventInit));
  el.click();
  el.dispatchEvent(new MouseEvent('mouseup', eventInit));
  return {
    ok: true,
    x: Math.round(rect.x),
    y: Math.round(rect.y),
    width: Math.round(rect.width),
    height: Math.round(rect.height)
  };
}
""",
            {"selector": selector, "text": text},
        )
        if js_result.get("ok"):
            page.wait_for_timeout(timeout_ms)
            result.update(js_result)
            result["url"] = redact_url(page.url)
        else:
            result.update(js_result)
    except Exception as exc:
        result.update({"error": type(exc).__name__})
    return result


def _explore_subapp_menu(
    page: Any,
    report: DiscoveryReport,
    app_label: str,
    *,
    min_x: int = 245,
) -> dict[str, Any]:
    """Reveal a child app's read-only menu panel and capture menu/pageId hints."""
    started_network = len(report.network)
    click_result = _try_click_tile_text(page, app_label, ".kd-cq-tile-app-list-item", timeout_ms=1_200)
    candidates = collect_menu_candidates(page, min_x=min_x, limit=160)
    try:
        button_state = page.evaluate(_button_state_script())
    except Exception:
        button_state = {}
    network_delta = report.network[started_network:]
    menu_tree = summarize_menu_tree(
        candidates,
        network_delta or report.network,
        app_name=app_label,
        url=redact_url(page.url),
    )
    for row in menu_tree:
        row.update({
            "has_new_button": bool(button_state.get("has_new_button")),
            "has_save_button": bool(button_state.get("has_save_button")),
            "has_submit_button": bool(button_state.get("has_submit_button")),
        })
    return {
        "app_label": normalize_label(app_label),
        "click": click_result,
        "menu_candidates": candidates,
        "menu_tree": menu_tree,
        "network_delta": [asdict(item) for item in network_delta],
        "risk_summary": dict(Counter(row.get("risk_level", "unknown") for row in menu_tree)),
    }


def _open_menu_sample(
    page: Any,
    report: DiscoveryReport,
    *,
    cloud_label: str,
    app_label: str,
    menu_label: str,
) -> dict[str, Any]:
    """Open one explicitly requested low-risk business menu and capture network deltas."""
    start_network = len(report.network)
    launcher_result = _ensure_app_launcher_open(page, timeout_ms=2_000)
    search_result: dict[str, Any] = {}
    cloud_click: dict[str, Any] = {}
    app_click: dict[str, Any] = {}
    if launcher_result.get("ok"):
        if cloud_label:
            search_result = _try_search_app_launcher(page, cloud_label, timeout_ms=2_000)
            cloud_click = _try_click_tile_text(page, cloud_label, ".kd-cq-tile-cloud-item", timeout_ms=800)
        app_click = _try_click_tile_text(page, app_label, ".kd-cq-tile-app-list-item", timeout_ms=1_200)
    before_menu_network = len(report.network)
    menu_click = _try_click_main_area_text(page, menu_label, min_x=245, timeout_ms=2_500)
    menu_network_delta = report.network[before_menu_network:]
    candidates = collect_menu_candidates(page, min_x=245, limit=120)
    try:
        button_state = page.evaluate(_button_state_script())
    except Exception:
        button_state = {}
    return {
        "app_label": normalize_label(app_label),
        "menu_label": normalize_label(menu_label),
        "launcher": launcher_result,
        "search": search_result,
        "cloud_click": cloud_click,
        "app_click": app_click,
        "menu_click": menu_click,
        "network_delta": [asdict(item) for item in report.network[start_network:]],
        "menu_network_delta": [asdict(item) for item in menu_network_delta],
        "visible_candidates_after_open": candidates,
        "button_state": {
            "has_new_button": bool(button_state.get("has_new_button")),
            "has_save_button": bool(button_state.get("has_save_button")),
            "has_submit_button": bool(button_state.get("has_submit_button")),
        },
        "final_url": redact_url(page.url),
    }


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

    report = DiscoveryReport(
        base_url=base_url,
        home_url=home_url,
        datacenter_id=account_id,
        target_app_keyword=normalize_label(config.target_app_keyword),
    )

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=config.headless)
        context_kwargs: dict[str, Any] = {"ignore_https_errors": True}
        if config.record_har_path:
            config.record_har_path.parent.mkdir(parents=True, exist_ok=True)
            context_kwargs.update({
                "record_har_path": str(config.record_har_path),
                # full + embed keeps request postData, which is required to
                # inspect pageId chains. HAR files remain local/ignored.
                "record_har_mode": "full",
                "record_har_content": "embed",
            })
        context = browser.new_context(**context_kwargs)
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
        report.menu_candidates = collect_menu_candidates(page)
        report.app_tree = collect_app_tree(page)

        if config.target_app_keyword:
            report.target_app_status = _try_click_text(page, config.target_app_keyword)
            if not report.target_app_status.get("ok"):
                if config.search_app_launcher:
                    launcher_result = _try_open_app_launcher(page, timeout_ms=2_000)
                    search_attempts = []
                    for keyword in keyword_variants(config.target_app_keyword):
                        search_result = _try_search_app_launcher(page, keyword, timeout_ms=2_000)
                        search_attempts.append(search_result)
                        if search_result.get("matches"):
                            break
                    matched_labels = [
                        normalize_label(match)
                        for search_result in search_attempts
                        for match in (search_result.get("matches") or [])
                    ]
                    if normalize_label(config.target_app_keyword) in matched_labels:
                        retry_result = _try_click_text(page, config.target_app_keyword)
                    else:
                        retry_result = {
                            "text": config.target_app_keyword,
                            "ok": False,
                            "error": "target_label_not_visible_after_search",
                        }
                    fallback_result: dict[str, Any] = {}
                    if not retry_result.get("ok"):
                        fallback_label = ""
                        if matched_labels:
                            fallback_label = matched_labels[0]
                        if fallback_label:
                            fallback_result = _try_click_text(page, fallback_label)
                    report.target_app_status = {
                        "text": config.target_app_keyword,
                        "ok": bool(retry_result.get("ok") or fallback_result.get("ok")),
                        "launcher": launcher_result,
                        "search_attempts": search_attempts,
                        "retry": retry_result,
                        "fallback": fallback_result,
                    }
                for opener in ("全部应用", "我的应用", "应用", "更多应用", "工作台"):
                    if report.target_app_status.get("ok"):
                        break
                    opener_result = _try_click_text(page, opener, timeout_ms=2_000)
                    if opener_result.get("ok"):
                        report.target_app_status.setdefault("openers", []).append(opener_result)
                        retry_result = _try_click_text(page, config.target_app_keyword)
                        report.target_app_status = {
                            "text": config.target_app_keyword,
                            "ok": bool(retry_result.get("ok")),
                            "after_opener": opener,
                            "retry": retry_result,
                        }
                        break
            report.menu_candidates = collect_menu_candidates(page)
            report.app_tree = collect_app_tree(page)
            report.final_url = redact_url(page.url)

        if config.drilldown_apps:
            if config.target_app_keyword:
                cloud_result = _try_click_tile_text(
                    page,
                    config.target_app_keyword,
                    ".kd-cq-tile-cloud-item",
                    timeout_ms=800,
                )
                report.target_app_status.setdefault("cloud_click", cloud_result)
            for app_label in config.drilldown_apps:
                report.subapp_explorations.append(_explore_subapp_menu(page, report, app_label))
            if report.subapp_explorations:
                report.menu_candidates = report.subapp_explorations[-1].get("menu_candidates", [])

        for sample in config.open_menu_samples:
            app_label = normalize_label(str(sample.get("app_label", "")))
            menu_label = normalize_label(str(sample.get("menu_label", "")))
            if not app_label or not menu_label:
                continue
            report.menu_sample_explorations.append(
                _open_menu_sample(
                    page,
                    report,
                    cloud_label=config.target_app_keyword,
                    app_label=app_label,
                    menu_label=menu_label,
                )
            )

        if config.deep_action_plan:
            start_network = len(report.network)
            report.deep_action_capture = _execute_deep_action_plan(
                page,
                config.deep_action_plan,
                confirm_write=config.deep_confirm_write,
                confirm_workflow=config.deep_confirm_workflow,
            )
            report.deep_action_capture["network_delta"] = [
                asdict(item) for item in report.network[start_network:]
            ]
            report.deep_action_capture["final_url"] = redact_url(page.url)

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

        try:
            button_state = page.evaluate(_button_state_script())
        except Exception:
            button_state = {}
        report.menu_tree = summarize_menu_tree(
            report.menu_candidates,
            report.network,
            app_name=config.target_app_keyword,
            url=report.final_url,
        )
        for row in report.menu_tree:
            row.update({
                "has_new_button": bool(button_state.get("has_new_button")),
                "has_save_button": bool(button_state.get("has_save_button")),
                "has_submit_button": bool(button_state.get("has_submit_button")),
            })
        context.close()
        browser.close()

    if config.record_har_path and config.record_har_path.exists():
        report.har_path = str(config.record_har_path)
        report.har_summary = summarize_har_file(config.record_har_path)
        report.pageid_trace = report.har_summary.get("pageid_trace", [])

    config.output.parent.mkdir(parents=True, exist_ok=True)
    config.output.write_text(json.dumps(report.to_dict(), ensure_ascii=False, indent=2), encoding="utf-8")
    return report
