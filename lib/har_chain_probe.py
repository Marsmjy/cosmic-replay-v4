"""Value-safe HAR chain probing for pageId and component-link experience.

This module reads raw HAR requests before YAML generation and extracts a
redacted chain profile: endpoint shape, pageId tiers, lookup prefetches,
showForm/billFormId aliases, default context fields and write anchors.
"""
from __future__ import annotations

import hashlib
import json
import urllib.parse
from collections import Counter
from pathlib import Path
from typing import Any

from lib.har_extractor import is_business_request, load_har
from lib.pageid_trace import (
    classify_pageid,
    expected_pageid_role,
    extract_response_pageid_closures,
    extract_response_pageid_producers,
    pageid_fragment,
)

SCHEMA_VERSION = 1
_WRITE_ACS = {"save", "submit", "saveandeffect", "submitandeffect", "saveandaudit"}
_WRITE_KEY_TOKENS = ("save", "submit", "confirm", "ok")


def probe_har_chain(path: Path | str, *, include_fragments: bool = False) -> dict[str, Any]:
    har_path = Path(path)
    har = load_har(har_path)
    events = _extract_events(har, include_fragments=include_fragments)
    links = _build_links(events)
    risks = _build_risks(events, links)
    lessons = _build_lessons(events, links, risks)
    summary = _build_summary(events, links, risks)

    return {
        "schema_version": SCHEMA_VERSION,
        "source_har": har_path.name,
        "summary": summary,
        "links": links,
        "risks": risks,
        "lessons": lessons,
        "events": [_public_event(event) for event in events],
        "value_safety": {
            "stores_pageid_fragments": include_fragments,
            "stores_request_values": False,
            "stores_response_values": False,
            "notification_content": "sha1_only",
        },
    }


def build_experience_catalog(samples: list[dict[str, Any]]) -> dict[str, Any]:
    """Build a compact catalog suitable for committing as a regression fixture."""
    items = []
    aggregate_risks: Counter[str] = Counter()
    aggregate_lessons: Counter[str] = Counter()
    for item in samples:
        probe = item["probe"]
        for risk in probe.get("risks") or []:
            aggregate_risks[str(risk.get("code") or "")] += 1
        for lesson in probe.get("lessons") or []:
            aggregate_lessons[str(lesson.get("code") or "")] += 1
        items.append({
            "sample_id": item.get("id", ""),
            "title": item.get("title", ""),
            "business_type": item.get("business_type", ""),
            "source_har": probe.get("source_har", ""),
            "summary": probe.get("summary", {}),
            "lesson_codes": [lesson.get("code", "") for lesson in probe.get("lessons") or []],
            "risk_codes": [risk.get("code", "") for risk in probe.get("risks") or []],
        })
    return {
        "schema_version": SCHEMA_VERSION,
        "sample_count": len(items),
        "samples": items,
        "aggregate": {
            "risk_counts": dict(sorted(aggregate_risks.items())),
            "lesson_counts": dict(sorted(aggregate_lessons.items())),
        },
    }


def _extract_events(har: dict[str, Any], *, include_fragments: bool) -> list[dict[str, Any]]:
    events: list[dict[str, Any]] = []
    for har_index, entry in enumerate(har.get("log", {}).get("entries", [])):
        req = entry.get("request") or {}
        url = str(req.get("url") or "")
        if not is_business_request(url):
            continue
        parsed = urllib.parse.urlparse(url)
        qs = urllib.parse.parse_qs(parsed.query)
        body = urllib.parse.parse_qs(((req.get("postData") or {}).get("text") or ""))
        path_name = Path(parsed.path).name
        form_id = qs.get("f", [""])[0]
        app_id = qs.get("appId", [""])[0]
        ac = qs.get("ac", [""])[0]
        page_id = body.get("pageId", [""])[0]

        if path_name in {"batchInvokeAction.do", "invokeAction.do"} and ac:
            for action_index, action in enumerate(_parse_actions(body.get("params", [""])[0])):
                if not isinstance(action, dict):
                    continue
                method = str(action.get("methodName") or "")
                key = str(action.get("key") or "")
                step = {
                    "type": "invoke",
                    "form_id": form_id,
                    "app_id": app_id,
                    "ac": ac,
                    "method": method,
                    "key": key,
                    "args": action.get("args") or [],
                    "preserve_l2_page": classify_pageid(page_id) == "L2" and ac in {"loadData", "itemClick"},
                }
                events.append({
                    "event_id": f"e{len(events) + 1:04d}",
                    "har_index": har_index,
                    "action_index": action_index,
                    "endpoint": path_name,
                    "form_id": form_id,
                    "app_id": app_id,
                    "ac": ac,
                    "method": method,
                    "key": key,
                    "pageid_type": classify_pageid(page_id),
                    "_request_page_id": page_id,
                    "pageid_fragment": pageid_fragment(page_id) if include_fragments else "",
                    "expected_pageid_role": expected_pageid_role(step),
                    "response": _extract_response_features(entry),
                })
        elif path_name == "getConfig.do":
            params_raw = qs.get("params", [""])[0]
            form_from_params = form_id
            try:
                params = json.loads(params_raw) if params_raw else {}
                form_from_params = params.get("formId") or form_from_params
            except Exception:
                pass
            response_features = _extract_response_features(entry)
            events.append({
                "event_id": f"e{len(events) + 1:04d}",
                "har_index": har_index,
                "action_index": 0,
                "endpoint": path_name,
                "form_id": form_from_params,
                "app_id": app_id,
                "ac": "getConfig",
                "method": "getConfig",
                "key": "",
                "pageid_type": "missing",
                "_request_page_id": "",
                "pageid_fragment": "",
                "expected_pageid_role": "L3",
                "response": response_features,
            })
    return events


def _parse_actions(params_raw: str) -> list[Any]:
    if not params_raw:
        return []
    try:
        parsed = json.loads(params_raw)
        return parsed if isinstance(parsed, list) else []
    except Exception:
        return []


def _extract_response_features(entry: dict[str, Any]) -> dict[str, Any]:
    text = (((entry.get("response") or {}).get("content") or {}).get("text") or "")
    if not text:
        return _finalize_features(_empty_features())
    try:
        data = json.loads(text)
    except Exception:
        return _finalize_features(_empty_features())
    features = _empty_features()
    _walk_response(data, features)
    features["_pageid_producers"] = extract_response_pageid_producers(data)
    features["_pageid_closures"] = extract_response_pageid_closures(data)
    return _finalize_features(features)


def _empty_features() -> dict[str, Any]:
    return {
        "show_forms": [],
        "virtual_tabs": [],
        "closed_windows": [],
        "lookup_lists": [],
        "default_context_fields": [],
        "updated_keys": set(),
        "notifications": [],
        "_pageid_producers": [],
        "_pageid_closures": [],
    }


def _finalize_features(features: dict[str, Any]) -> dict[str, Any]:
    updated_keys = features.get("updated_keys")
    if isinstance(updated_keys, set):
        features["updated_keys"] = sorted(updated_keys)
    return features


def _walk_response(obj: Any, features: dict[str, Any]) -> None:
    if isinstance(obj, list):
        for item in obj:
            _walk_response(item, features)
        return
    if not isinstance(obj, dict):
        return

    action = obj.get("a")
    if action == "showForm":
        for item in obj.get("p") or []:
            if not isinstance(item, dict):
                continue
            pid = str(item.get("pageId") or "")
            features["show_forms"].append({
                "form_id": item.get("formId", ""),
                "bill_form_id": item.get("billFormId", ""),
                "pageid_type": classify_pageid(pid),
            })
    elif action == "closeWindow":
        for item in obj.get("p") or []:
            if isinstance(item, dict):
                features["closed_windows"].append({
                    "pageid_type": classify_pageid(item.get("pageId", "")),
                })
    elif action == "InvokeControlMethod":
        for item in obj.get("p") or []:
            if not isinstance(item, dict):
                continue
            method = str(item.get("methodname") or "")
            if method == "addVirtualTab":
                for arg in item.get("args") or []:
                    if isinstance(arg, dict):
                        features["virtual_tabs"].append({
                            "app_id": arg.get("appId", ""),
                            "pageid_type": classify_pageid(arg.get("pageId", "")),
                            "has_tab_name": bool(arg.get("tabName")),
                        })
            elif method == "setLookUpListValue":
                for arg in item.get("args") or []:
                    if isinstance(arg, dict):
                        features["lookup_lists"].append({
                            "field_key": arg.get("k") or item.get("key", ""),
                            "row_count": len(arg.get("data") or []),
                            "column_count": len(arg.get("columns") or []),
                        })
    elif action == "u":
        for item in obj.get("p") or []:
            if not isinstance(item, dict):
                continue
            key = str(item.get("k") or "")
            if key:
                features["updated_keys"].add(key)
            value = item.get("v")
            if key and _looks_like_basedata_tuple(value):
                features["default_context_fields"].append({
                    "field_key": key,
                    "value_shape": "basedata_tuple",
                    "part_count": len(value),
                })
    elif action in {"ShowNotificationMsg", "showErrMsg"}:
        for item in obj.get("p") or []:
            if not isinstance(item, dict):
                continue
            content = str(item.get("content") or item.get("msg") or "")
            features["notifications"].append({
                "type": item.get("type", ""),
                "content_sha1": hashlib.sha1(content.encode("utf-8")).hexdigest() if content else "",
                "content_length": len(content),
            })

    for value in obj.values():
        _walk_response(value, features)


def _looks_like_basedata_tuple(value: Any) -> bool:
    return (
        isinstance(value, list)
        and len(value) >= 3
        and all(isinstance(value[idx], str) for idx in range(3))
    )


def _build_links(events: list[dict[str, Any]]) -> dict[str, Any]:
    lookup_prefetches = []
    set_items_without_prefetch = []
    showform_aliases = []
    write_anchors = []
    default_contexts = []
    pageid_flows = []
    pageid_external_roots = []
    pageid_reuse_after_close = []
    pageid_cross_form = []
    pageid_producers: dict[str, dict[str, Any]] = {}
    closed_pageids: dict[str, dict[str, Any]] = {}
    external_pageids: set[str] = set()

    for idx, event in enumerate(events):
        request_page_id = str(event.get("_request_page_id") or "")
        if request_page_id:
            producer = pageid_producers.get(request_page_id)
            if producer and producer.get("har_index") != event.get("har_index"):
                producer_forms = list(producer.get("form_ids") or [])
                consumer_form = str(event.get("form_id") or "")
                form_scope_match = not producer_forms or consumer_form in set(producer_forms)
                flow = {
                    "producer_event": producer.get("event_id", ""),
                    "consumer_event": event.get("event_id", ""),
                    "producer_kind": producer.get("producer_kind", ""),
                    "pageid_type": event.get("pageid_type", ""),
                    "producer_forms": producer_forms,
                    "consumer_form": consumer_form,
                    "form_scope_match": form_scope_match,
                    "consumer_har_index": event.get("har_index"),
                    "consumer_action_index": event.get("action_index"),
                }
                pageid_flows.append(flow)
                if not form_scope_match:
                    pageid_cross_form.append(flow)
            elif producer is None and request_page_id not in external_pageids:
                external_pageids.add(request_page_id)
                pageid_external_roots.append({
                    "consumer_event": event.get("event_id", ""),
                    "pageid_type": event.get("pageid_type", ""),
                    "consumer_form": event.get("form_id", ""),
                    "consumer_har_index": event.get("har_index"),
                })

            closed_by = closed_pageids.get(request_page_id)
            if closed_by and closed_by.get("har_index") != event.get("har_index"):
                pageid_reuse_after_close.append({
                    "closed_by_event": closed_by.get("event_id", ""),
                    "consumer_event": event.get("event_id", ""),
                    "pageid_type": event.get("pageid_type", ""),
                    "consumer_form": event.get("form_id", ""),
                    "consumer_har_index": event.get("har_index"),
                })

        if event["method"] == "setItemByIdFromClient":
            prefetch = _find_previous_lookup(events, idx, event)
            if prefetch:
                lookup_prefetches.append({
                    "field_key": event.get("key", ""),
                    "form_id": event.get("form_id", ""),
                    "app_id": event.get("app_id", ""),
                    "prefetch_event": prefetch.get("event_id", ""),
                    "pick_event": event.get("event_id", ""),
                    "prefetch_endpoint": prefetch.get("endpoint", ""),
                })
            else:
                set_items_without_prefetch.append({
                    "field_key": event.get("key", ""),
                    "form_id": event.get("form_id", ""),
                    "app_id": event.get("app_id", ""),
                    "pick_event": event.get("event_id", ""),
                })

        if _is_write_anchor(event):
            write_anchors.append({
                "event_id": event.get("event_id", ""),
                "form_id": event.get("form_id", ""),
                "app_id": event.get("app_id", ""),
                "ac": event.get("ac", ""),
                "method": event.get("method", ""),
                "key": event.get("key", ""),
                "pageid_type": event.get("pageid_type", ""),
            })

        for field in event.get("response", {}).get("default_context_fields") or []:
            default_contexts.append({
                "event_id": event.get("event_id", ""),
                "form_id": event.get("form_id", ""),
                "field_key": field.get("field_key", ""),
                "value_shape": field.get("value_shape", ""),
            })

        for show in event.get("response", {}).get("show_forms") or []:
            bill_form_id = str(show.get("bill_form_id") or "")
            form_id = str(show.get("form_id") or "")
            if bill_form_id and bill_form_id != form_id:
                future_use = next((
                    item for item in events[idx + 1:]
                    if item.get("form_id") == bill_form_id
                ), None)
                showform_aliases.append({
                    "source_event": event.get("event_id", ""),
                    "form_id": form_id,
                    "bill_form_id": bill_form_id,
                    "pageid_type": show.get("pageid_type", ""),
                    "future_request_event": (future_use or {}).get("event_id", ""),
                })

        response = event.get("response") or {}
        for producer in response.get("_pageid_producers") or []:
            pageid = str(producer.get("page_id") or "")
            if pageid:
                pageid_producers[pageid] = {
                    **producer,
                    "event_id": event.get("event_id", ""),
                    "har_index": event.get("har_index"),
                }
                closed_pageids.pop(pageid, None)
        for pageid in response.get("_pageid_closures") or []:
            closed_pageids[str(pageid)] = {
                "event_id": str(event.get("event_id") or ""),
                "har_index": event.get("har_index"),
            }

    return {
        "lookup_prefetches": lookup_prefetches,
        "set_items_without_prefetch": set_items_without_prefetch,
        "showform_aliases": showform_aliases,
        "write_anchors": write_anchors,
        "default_contexts": default_contexts,
        "pageid_flows": pageid_flows,
        "pageid_external_roots": pageid_external_roots,
        "pageid_reuse_after_close": pageid_reuse_after_close,
        "pageid_cross_form": pageid_cross_form,
    }


def _find_previous_lookup(events: list[dict[str, Any]], index: int, event: dict[str, Any]) -> dict[str, Any] | None:
    for candidate in reversed(events[max(0, index - 8):index]):
        if candidate.get("ac") != "getLookUpList" and candidate.get("method") != "getLookUpList":
            continue
        if candidate.get("form_id") != event.get("form_id"):
            continue
        if candidate.get("app_id") != event.get("app_id"):
            continue
        if candidate.get("key") != event.get("key"):
            continue
        return candidate
    return None


def _is_write_anchor(event: dict[str, Any]) -> bool:
    ac = str(event.get("ac") or "").lower()
    method = str(event.get("method") or "").lower()
    key = str(event.get("key") or "").lower()
    if ac in _WRITE_ACS or method in _WRITE_ACS:
        return True
    return any(token in key for token in _WRITE_KEY_TOKENS)


def _build_risks(events: list[dict[str, Any]], links: dict[str, Any]) -> list[dict[str, Any]]:
    risks = []
    for item in links["set_items_without_prefetch"]:
        # Some basedata controls work without lookup prefetch. Keep this as review,
        # not breaking, so agents inspect it only when replay fails.
        risks.append({
            "code": "setitem_without_lookup_prefetch",
            "severity": "review",
            "event_id": item.get("pick_event", ""),
            "form_id": item.get("form_id", ""),
            "field_key": item.get("field_key", ""),
        })
    for item in links["showform_aliases"]:
        if not item.get("future_request_event"):
            risks.append({
                "code": "showform_billformid_not_followed",
                "severity": "review",
                "event_id": item.get("source_event", ""),
                "form_id": item.get("form_id", ""),
                "field_key": item.get("bill_form_id", ""),
            })
    for event in events:
        if _is_write_anchor(event) and event.get("pageid_type") == "L2":
            risks.append({
                "code": "write_anchor_uses_l2_pageid",
                "severity": "high",
                "event_id": event.get("event_id", ""),
                "form_id": event.get("form_id", ""),
                "field_key": event.get("key", ""),
            })
    for item in links["pageid_cross_form"]:
        risks.append({
            "code": "pageid_producer_consumer_form_mismatch",
            "severity": "high",
            "event_id": item.get("consumer_event", ""),
            "form_id": item.get("consumer_form", ""),
            "field_key": item.get("producer_event", ""),
        })
    return risks


def _build_lessons(events: list[dict[str, Any]], links: dict[str, Any], risks: list[dict[str, Any]]) -> list[dict[str, Any]]:
    lessons = []
    if links["lookup_prefetches"]:
        lessons.append({
            "code": "lookup_prefetch_before_pick",
            "detail": "HAR contains invokeAction.do/getLookUpList before setItemByIdFromClient; YAML should keep prefetch_lookup.",
        })
    if links["showform_aliases"]:
        lessons.append({
            "code": "showform_billformid_alias",
            "detail": "showForm provides billFormId used by later requests; runner must bind both formId and billFormId.",
        })
    if links["default_contexts"]:
        lessons.append({
            "code": "load_data_default_context",
            "detail": "loadData returns basedata tuples that belong to pageId server context; do not blindly replay defaults as user picks.",
        })
    if links["write_anchors"]:
        lessons.append({
            "code": "write_anchor_present",
            "detail": "HAR has save/submit/confirm anchors; write smoke can reuse generated YAML with explicit confirmation.",
        })
    if links["pageid_flows"]:
        lessons.append({
            "code": "exact_pageid_producer_consumer_flow",
            "detail": "Recorded request pageIds are linked to the exact earlier response that produced them; generated YAML should preserve that producer before its consumers.",
        })
    if any(risk.get("code") == "write_anchor_uses_l2_pageid" for risk in risks):
        lessons.append({
            "code": "write_l2_mismatch",
            "detail": "A write anchor carries L2 pageId in HAR; confirm it is a toolbar bridge, not a real edit-state save.",
        })
    return lessons


def _build_summary(events: list[dict[str, Any]], links: dict[str, Any], risks: list[dict[str, Any]]) -> dict[str, Any]:
    return {
        "event_count": len(events),
        "form_count": len({item.get("form_id") for item in events if item.get("form_id")}),
        "forms": sorted({item.get("form_id") for item in events if item.get("form_id")}),
        "endpoint_counts": dict(Counter(item.get("endpoint", "") for item in events)),
        "pageid_type_counts": dict(Counter(item.get("pageid_type", "") for item in events)),
        "expected_role_counts": dict(Counter(item.get("expected_pageid_role", "") for item in events)),
        "lookup_prefetch_count": len(links["lookup_prefetches"]),
        "showform_alias_count": len(links["showform_aliases"]),
        "write_anchor_count": len(links["write_anchors"]),
        "default_context_count": len(links["default_contexts"]),
        "pageid_exact_link_count": len(links["pageid_flows"]),
        "pageid_external_root_count": len(links["pageid_external_roots"]),
        "pageid_reuse_after_close_count": len(links["pageid_reuse_after_close"]),
        "pageid_cross_form_count": len(links["pageid_cross_form"]),
        "risk_counts": dict(Counter(item.get("code", "") for item in risks)),
    }


def _public_event(event: dict[str, Any]) -> dict[str, Any]:
    public = {
        key: value
        for key, value in event.items()
        if not str(key).startswith("_")
    }
    response = public.get("response")
    if isinstance(response, dict):
        public["response"] = {
            key: value
            for key, value in response.items()
            if not str(key).startswith("_")
        }
    return public
