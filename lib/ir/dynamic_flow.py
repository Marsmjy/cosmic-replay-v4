"""Value-safe producer/consumer graph for runtime replay values.

The graph is diagnostic only.  It records that a kind of runtime value was
produced or consumed by a step, but never records the actual value.
"""
from __future__ import annotations

import json
from collections import Counter, defaultdict
from typing import Any

from lib.pageid_trace import classify_pageid, expected_pageid_role


CRITICAL_KINDS = {"billno", "confirm_callback", "task_row", "upload_url"}
MAX_ITEMS = 80


def build_dynamic_value_flow(
    case: dict[str, Any],
    *,
    run_events: list[dict[str, Any]] | None = None,
    max_items: int = MAX_ITEMS,
) -> dict[str, Any]:
    """Build a value-safe runtime value flow summary.

    The graph helps an AI repair agent answer questions like:
    - which response produced the runtime billno used by a workflow search;
    - whether an afterConfirm step is still using a recorded callbackValue;
    - whether an upload step has a real upload URL producer;
    - whether repeated polling should become a wait_until step.
    """
    if not isinstance(case, dict):
        case = {}
    run_events = run_events or []
    step_order = _step_order(case)
    steps_by_id = {
        _step_id(step, index): step
        for index, step in enumerate(case.get("steps") or [])
        if isinstance(step, dict)
    }

    producers: list[dict[str, Any]] = []
    consumers: list[dict[str, Any]] = []
    seen: set[tuple[str, str, str, str]] = set()

    for index, step in enumerate(case.get("steps") or []):
        if not isinstance(step, dict):
            continue
        sid = _step_id(step, index)
        order = step_order.get(sid, index * 100)
        recorded_source = str(step.get("recorded_pageid_source_step_id") or "")
        if recorded_source:
            source_step = steps_by_id.get(recorded_source) or {}
            har_type = classify_pageid(step.get("_har_page_id"))
            if har_type == "missing":
                har_type = str(step.get("recorded_pageid_type") or "missing")
            _append_node(
                producers,
                seen,
                role="producer",
                kind="page_id",
                step_id=recorded_source,
                order=step_order.get(recorded_source, max(0, order - 1)),
                source="recorded_har_response",
                confidence="high",
                step=source_step,
                extra={
                    "pageid_types": {har_type: 1},
                    "recorded_source_step_id": recorded_source,
                    "producer_kind": step.get("recorded_pageid_source_kind", ""),
                    "source_retained": step.get("recorded_pageid_source_retained"),
                },
            )
            _append_node(
                consumers,
                seen,
                role="consumer",
                kind="page_id",
                step_id=sid,
                order=order,
                source="recorded_har_request",
                confidence="high",
                step=step,
                extra={
                    "runtime_pageid_type": har_type,
                    "expected_pageid_role": expected_pageid_role(step),
                    "recorded_source_step_id": recorded_source,
                    "source_retained": step.get("recorded_pageid_source_retained"),
                },
            )
        for kind in _kinds_from_step(step):
            _append_node(
                consumers,
                seen,
                role="consumer",
                kind=kind,
                step_id=sid,
                order=order,
                source="yaml_step",
                confidence="medium",
                step=step,
            )

    for event_index, event in enumerate(run_events):
        if not isinstance(event, dict):
            continue
        event_type = str(event.get("type") or "")
        data = event.get("data") if isinstance(event.get("data"), dict) else {}
        step_id = str(data.get("step_id") or data.get("id") or "")
        order = event_index * 100 + _phase_for_event(event_type)

        if event_type == "pageid_trace" and step_id:
            _append_node(
                consumers,
                seen,
                role="consumer",
                kind="page_id",
                step_id=step_id,
                order=order,
                source="runtime_pageid_trace",
                confidence="high",
                extra={
                    "form_id": data.get("form_id") or "",
                    "app_id": data.get("app_id") or "",
                    "ac": data.get("ac") or "",
                    "method": data.get("method") or "",
                    "expected_pageid_role": data.get("expected_pageid_role") or "",
                    "runtime_pageid_type": data.get("runtime_pageid_type") or "",
                    "pending_pageid_type": data.get("pending_pageid_type") or "",
                    "har_pageid_type": data.get("har_pageid_type") or "",
                    "phase": data.get("phase") or "",
                    "status": data.get("status") or "",
                },
            )

        request_payload = data.get("resolved_request")
        if request_payload is not None:
            for kind in _kinds_from_payload(request_payload, role="request", event_type=event_type):
                _append_node(
                    consumers,
                    seen,
                    role="consumer",
                    kind=kind,
                    step_id=step_id or f"event_{event_index + 1}",
                    order=order,
                    source="runtime_request",
                    confidence="high",
                    extra=_payload_node_extra(request_payload, data=data),
                )

        response_payload = data.get("response")
        if response_payload is not None:
            for kind in _kinds_from_payload(response_payload, role="response", event_type=event_type):
                _append_node(
                    producers,
                    seen,
                    role="producer",
                    kind=kind,
                    step_id=step_id or f"event_{event_index + 1}",
                    order=order,
                    source="runtime_response",
                    confidence="high",
                    extra=_payload_node_extra(response_payload, data=data),
                )

        # Some runner events store useful context directly on data.  Avoid
        # re-scanning ordinary step_start/step_ok wrappers because their
        # resolved_request/response payloads were handled above.
        if event_type not in {"step_start", "step_ok"}:
            for kind in _kinds_from_payload(data, role="event_data", event_type=event_type):
                target = producers if event_type in {
                    "runtime_billno_search_retry",
                    "runtime_billno_wait_ok",
                    "upload_file_ok",
                } else consumers
                _append_node(
                    target,
                    seen,
                    role="producer" if target is producers else "consumer",
                    kind=kind,
                    step_id=step_id or f"event_{event_index + 1}",
                    order=order,
                    source=f"runtime_event:{event_type}",
                    confidence="medium",
                    extra=_payload_node_extra(data, data=data),
                )

    producers.sort(key=lambda item: (item["order"], item["step_id"], item["kind"]))
    consumers.sort(key=lambda item: (item["order"], item["step_id"], item["kind"]))
    edges, edge_warnings = _pair_edges(producers, consumers)
    warnings = edge_warnings + _pageid_warnings(producers, consumers) + _polling_warnings(case, producers, consumers)

    kind_counts = Counter(item["kind"] for item in producers + consumers)
    return {
        "schema_version": "0.1",
        "status": "ready",
        "source": "generated_yaml_runtime",
        "raw_values_included": False,
        "summary": {
            "producer_count": len(producers),
            "consumer_count": len(consumers),
            "edge_count": len(edges),
            "value_kinds": dict(sorted(kind_counts.items())),
            "warning_count": len(warnings),
        },
        "producers": [_public_node(item) for item in producers[:max_items]],
        "consumers": [_public_node(item) for item in consumers[:max_items]],
        "edges": edges[:max_items],
        "warnings": warnings[:max_items],
        "truncated": len(producers) > max_items or len(consumers) > max_items or len(edges) > max_items,
    }


def _step_order(case: dict[str, Any]) -> dict[str, int]:
    result: dict[str, int] = {}
    for index, step in enumerate(case.get("steps") or []):
        if isinstance(step, dict):
            result[_step_id(step, index)] = index * 100
    return result


def _step_id(step: dict[str, Any], index: int) -> str:
    return str(step.get("id") or f"step_{index + 1}")


def _phase_for_event(event_type: str) -> int:
    if event_type == "step_start":
        return 10
    if event_type == "step_ok":
        return 80
    if event_type in {"step_fail", "case_error"}:
        return 90
    return 50


def _append_node(
    target: list[dict[str, Any]],
    seen: set[tuple[str, str, str, str]],
    *,
    role: str,
    kind: str,
    step_id: str,
    order: int,
    source: str,
    confidence: str,
    step: dict[str, Any] | None = None,
    extra: dict[str, Any] | None = None,
) -> None:
    key = (role, kind, step_id, source)
    if key in seen:
        return
    seen.add(key)
    node: dict[str, Any] = {
        "kind": kind,
        "step_id": step_id,
        "order": order,
        "source": source,
        "confidence": confidence,
    }
    if step:
        node.update(_step_shape(step))
    if extra:
        node.update({k: v for k, v in extra.items() if v not in (None, "", [], {})})
    target.append(node)


def _payload_node_extra(payload: Any, *, data: dict[str, Any] | None = None) -> dict[str, Any]:
    extra: dict[str, Any] = {"payload_shape": _value_shape(payload)}
    pageid_types = _pageid_type_counts(payload)
    if pageid_types:
        extra["pageid_types"] = pageid_types
    pageid_forms = _pageid_forms(payload)
    if pageid_forms:
        extra["pageid_forms"] = pageid_forms[:12]
    if data:
        for key in ("form_id", "app_id", "ac", "method"):
            if data.get(key):
                extra[key] = data.get(key)
    return extra


def _public_node(node: dict[str, Any]) -> dict[str, Any]:
    return {k: v for k, v in node.items() if k != "order"}


def _step_shape(step: dict[str, Any]) -> dict[str, Any]:
    return {
        "form_id": step.get("form_id", ""),
        "app_id": step.get("app_id", ""),
        "ac": step.get("ac", ""),
        "method": step.get("method", ""),
        "key": step.get("key", ""),
    }


def _kinds_from_step(step: dict[str, Any]) -> set[str]:
    text = _step_text(step)
    kinds: set[str] = set()
    if "afterconfirm" in text or "doconfirm" in text or "callbackvalue" in text:
        kinds.add("confirm_callback")
    if "commonsearch" in text and "billno" in text:
        kinds.add("billno")
    if "wf_task" in text and ("entryrowclick" in text or "billlistap" in text or "seldatas" in text):
        kinds.add("task_row")
    if "uploadfile" in text or "beforeupload" in text:
        kinds.add("upload_url")
    if "upload" in text and ("tempfile" in text or "download.do" in text):
        kinds.add("upload_url")
    if "getpercent" in text or '"percent"' in text or " progress" in text:
        kinds.add("poll_percent")
    if step.get("type") == "wait_until":
        condition = step.get("condition") if isinstance(step.get("condition"), dict) else {}
        condition_kind = str(condition.get("kind") or "").lower()
        if condition_kind in {"grid_row_exists", "list_row_exists", "table_row_exists"}:
            kinds.add("task_row")
            if "billno" in _node_text(condition).lower():
                kinds.add("billno")
        if condition_kind in {"percent_at_least", "poll_percent_at_least"}:
            kinds.add("poll_percent")
    if step.get("type") in {"pick_basedata", "select_f7_list_row"}:
        kinds.add("lookup_candidate")
    if step.get("type") == "update_fields" and "${vars." in text:
        kinds.add("user_variable")
    return kinds


def _kinds_from_payload(payload: Any, *, role: str, event_type: str) -> set[str]:
    text = _node_text(payload)
    lowered = text.lower()
    keys = _collect_keys(payload)
    kinds: set[str] = set()

    if (
        "pageid" in keys
        or "page_id" in keys
        or "pageidtype" in keys
        or '"pageid"' in lowered
        or '"page_id"' in lowered
    ):
        kinds.add("page_id")
    if "billno" in keys or '"billno"' in lowered or '"billNo"' in text:
        kinds.add("billno")
    if "callbackvalue" in keys or "callbackvalue" in lowered or "showconfirm" in lowered:
        kinds.add("confirm_callback")
    if "billlistap" in lowered and ("seldatas" in lowered or "dataindex" in lowered or "wf_task" in lowered):
        kinds.add("task_row")
    if "uploadfile" in lowered or "download.do" in lowered or "tempfile" in lowered:
        kinds.add("upload_url")
    if "getpercent" in lowered or "percent" in keys or '"percent"' in lowered:
        kinds.add("poll_percent")
    if role == "request" and ("getlookuplist" in lowered or "setitembyidfromclient" in lowered):
        kinds.add("lookup_candidate")
    if event_type in {
        "runtime_billno_search_retry",
        "runtime_billno_wait_start",
        "runtime_billno_wait_ok",
        "runtime_billno_wait_timeout",
        "runtime_billno_wait_error",
    }:
        kinds.add("billno")
        kinds.add("task_row")
    if event_type == "upload_file_ok":
        kinds.add("upload_url")
    if event_type == "runtime_upload_applied":
        kinds.add("upload_url")
    return kinds


def _pair_edges(
    producers: list[dict[str, Any]],
    consumers: list[dict[str, Any]],
) -> tuple[list[dict[str, Any]], list[dict[str, Any]]]:
    by_kind: dict[str, list[dict[str, Any]]] = defaultdict(list)
    for producer in producers:
        by_kind[producer["kind"]].append(producer)

    edges: list[dict[str, Any]] = []
    warnings: list[dict[str, Any]] = []
    for consumer in consumers:
        kind = consumer["kind"]
        candidates = [item for item in by_kind.get(kind, []) if item["order"] <= consumer["order"]]
        if kind == "page_id":
            edge = _pageid_edge(candidates, consumer)
            if edge:
                edges.append(edge)
                continue
            if _pageid_consumer_needs_prior_producer(consumer):
                warnings.append({
                    "code": "pageid_consumer_without_matching_producer",
                    "kind": kind,
                    "consumer_step_id": consumer["step_id"],
                    "message": "page_id 运行时消费步骤没有匹配到更早的响应 pageId 生产者，需检查 showForm/addVirtualTab/menu L2 是否丢失或使用了录制旧 pageId。",
                })
            continue
        if candidates:
            producer = candidates[-1]
            edges.append({
                "kind": kind,
                "producer_step_id": producer["step_id"],
                "consumer_step_id": consumer["step_id"],
                "producer_source": producer["source"],
                "consumer_source": consumer["source"],
                "confidence": "high" if producer["source"].startswith("runtime") and consumer["source"].startswith("runtime") else "medium",
            })
            continue
        if kind in CRITICAL_KINDS:
            warnings.append({
                "code": "dynamic_consumer_without_prior_producer",
                "kind": kind,
                "consumer_step_id": consumer["step_id"],
                "message": f"{kind} 有消费步骤但在当前证据中未看到更早的运行时生产者，需确认是否仍在使用 HAR 录制旧值。",
            })
    return edges, warnings


def _pageid_edge(candidates: list[dict[str, Any]], consumer: dict[str, Any]) -> dict[str, Any] | None:
    if not candidates:
        return None
    scored: list[tuple[int, int, dict[str, Any]]] = []
    for idx, producer in enumerate(candidates):
        score = _pageid_match_score(producer, consumer)
        if score > 0:
            scored.append((score, idx, producer))
    if not scored:
        return None
    scored.sort(key=lambda item: (item[0], item[1]))
    score, _idx, producer = scored[-1]
    return {
        "kind": "page_id",
        "producer_step_id": producer["step_id"],
        "consumer_step_id": consumer["step_id"],
        "producer_source": producer["source"],
        "consumer_source": consumer["source"],
        "confidence": "high" if score >= 7 else "medium",
        "match_detail": _pageid_match_detail(producer, consumer),
    }


def _pageid_match_score(producer: dict[str, Any], consumer: dict[str, Any]) -> int:
    score = 0
    if (
        consumer.get("recorded_source_step_id")
        and consumer.get("recorded_source_step_id") == producer.get("step_id")
    ):
        score += 10
    consumer_type = str(consumer.get("runtime_pageid_type") or "")
    expected_role = str(consumer.get("expected_pageid_role") or "")
    producer_types = producer.get("pageid_types") if isinstance(producer.get("pageid_types"), dict) else {}
    if consumer_type and consumer_type in producer_types:
        score += 4
    elif expected_role == "L2" and "L2" in producer_types:
        score += 3
    elif expected_role == "L3" and "L1_or_L3" in producer_types:
        score += 3
    elif producer_types:
        score += 1

    consumer_form = str(consumer.get("form_id") or "")
    producer_forms = set(str(x) for x in (producer.get("pageid_forms") or []) if x)
    if consumer_form and consumer_form in producer_forms:
        score += 5
    elif consumer_form and str(producer.get("form_id") or "") == consumer_form:
        score += 3
    elif producer_forms:
        score -= 2

    consumer_app = str(consumer.get("app_id") or "")
    producer_app = str(producer.get("app_id") or "")
    if consumer_app and producer_app and consumer_app == producer_app:
        score += 1
    return score


def _pageid_match_detail(producer: dict[str, Any], consumer: dict[str, Any]) -> dict[str, Any]:
    return {
        "consumer_expected_role": consumer.get("expected_pageid_role", ""),
        "consumer_runtime_type": consumer.get("runtime_pageid_type", ""),
        "producer_pageid_types": producer.get("pageid_types", {}),
        "form_scope_match": bool(
            consumer.get("form_id")
            and consumer.get("form_id") in set(producer.get("pageid_forms") or [])
        ),
    }


def _pageid_consumer_needs_prior_producer(consumer: dict[str, Any]) -> bool:
    if consumer.get("source") != "runtime_pageid_trace":
        return False
    if consumer.get("phase") == "before_handler":
        return False
    return str(consumer.get("runtime_pageid_type") or "") not in {"", "missing", "unknown"}


def _pageid_warnings(
    producers: list[dict[str, Any]],
    consumers: list[dict[str, Any]],
) -> list[dict[str, Any]]:
    warnings: list[dict[str, Any]] = []
    for consumer in consumers:
        if consumer.get("kind") != "page_id" or consumer.get("source") != "runtime_pageid_trace":
            continue
        if consumer.get("phase") != "after_handler":
            continue
        expected = str(consumer.get("expected_pageid_role") or "")
        runtime_type = str(consumer.get("runtime_pageid_type") or "")
        if expected == "L3" and runtime_type == "L2":
            warnings.append({
                "code": "pageid_role_mismatch",
                "kind": "page_id",
                "consumer_step_id": consumer["step_id"],
                "message": "运行后仍用 L2 pageId 执行需要 L3 的字段维护/保存步骤，优先检查 pending L3、showForm 或预验证降级。",
            })
        elif expected == "L2" and runtime_type == "L1_or_L3":
            warnings.append({
                "code": "pageid_role_mismatch",
                "kind": "page_id",
                "consumer_step_id": consumer["step_id"],
                "message": "运行时把列表/树/工具栏 L2 上下文替换成 L3，可能丢失服务端列表模型或默认上下文。",
            })
    return warnings


def _polling_warnings(
    case: dict[str, Any],
    producers: list[dict[str, Any]],
    consumers: list[dict[str, Any]],
) -> list[dict[str, Any]]:
    warnings: list[dict[str, Any]] = []
    signatures = [
        _poll_signature(step)
        for step in case.get("steps") or []
        if isinstance(step, dict) and _poll_signature(step)
    ]
    if len(signatures) >= 3 and len(set(signatures)) <= max(1, len(signatures) // 2):
        warnings.append({
            "code": "polling_wait_until_candidate",
            "message": "发现重复轮询请求，可考虑在 HAR 解析阶段折叠为 wait_until(percent=100) 语义步骤。",
            "poll_step_count": len(signatures),
        })
    poll_nodes = [item for item in producers + consumers if item["kind"] == "poll_percent"]
    if len(poll_nodes) >= 3 and not warnings:
        warnings.append({
            "code": "polling_wait_until_candidate",
            "message": "运行事件中出现多次 percent/getpercent，可考虑抽象为 wait_until，减少录制噪声。",
            "poll_step_count": len(poll_nodes),
        })
    return warnings


def _poll_signature(step: dict[str, Any]) -> str:
    text = _step_text(step)
    if "getpercent" not in text and '"percent"' not in text:
        return ""
    return "|".join(str(step.get(key) or "") for key in ("form_id", "app_id", "ac", "method", "key"))


def _step_text(step: dict[str, Any]) -> str:
    return _node_text({
        "type": step.get("type"),
        "form_id": step.get("form_id"),
        "app_id": step.get("app_id"),
        "ac": step.get("ac"),
        "method": step.get("method"),
        "key": step.get("key"),
        "args": step.get("args"),
        "post_data": step.get("post_data"),
        "skip_reason": step.get("skip_reason"),
    }).lower()


def _node_text(node: Any) -> str:
    try:
        return json.dumps(node, ensure_ascii=False, default=str)[:30000]
    except Exception:
        return str(node)[:30000]


def _collect_keys(node: Any) -> set[str]:
    keys: set[str] = set()
    stack = [node]
    while stack:
        current = stack.pop()
        if isinstance(current, dict):
            for key, value in current.items():
                keys.add(str(key).lower())
                stack.append(value)
        elif isinstance(current, list):
            stack.extend(current)
    return keys


def _pageid_type_counts(node: Any) -> dict[str, int]:
    counts: Counter[str] = Counter()

    def walk(current: Any, parent_key: str = "") -> None:
        key_l = parent_key.lower()
        if isinstance(current, dict):
            for key, value in current.items():
                walk(value, str(key))
            return
        if isinstance(current, list):
            for item in current:
                walk(item, parent_key)
            return
        if key_l in {"pageid", "page_id"}:
            pid_type = classify_pageid(current)
            if pid_type not in {"missing", "unknown"}:
                counts[pid_type] += 1
        elif key_l == "pageidtype":
            text = str(current or "").strip()
            if text:
                counts[text] += 1

    walk(node)
    return dict(sorted(counts.items()))


def _pageid_forms(node: Any) -> list[str]:
    forms: list[str] = []
    seen: set[str] = set()

    def remember(value: Any) -> None:
        text = str(value or "").strip()
        if text and text not in seen:
            seen.add(text)
            forms.append(text)

    def walk(current: Any) -> None:
        if isinstance(current, dict):
            if current.get("pageId") or current.get("page_id") or current.get("pageIdType"):
                remember(current.get("formId") or current.get("form_id"))
                remember(current.get("billFormId") or current.get("bill_form_id"))
            for value in current.values():
                walk(value)
        elif isinstance(current, list):
            for item in current:
                walk(item)

    walk(node)
    return forms


def _value_shape(value: Any) -> str:
    if value is None:
        return "null"
    if isinstance(value, bool):
        return "bool"
    if isinstance(value, (int, float)):
        return "number"
    if isinstance(value, dict):
        return f"dict[{len(value)}]"
    if isinstance(value, list):
        return f"list[{len(value)}]"
    return f"text_len_{len(str(value))}"
