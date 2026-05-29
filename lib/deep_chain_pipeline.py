"""Deep-chain scenario factory orchestration helpers.

This module keeps the real browser/HAR/YAML steps separate, but gives the
project a single value-safe place to answer: where is each salary-cloud sample
in the closed-loop pipeline, what should run next, and how should a PASS be
verified as a real write.
"""
from __future__ import annotations

import json
from collections import Counter
from datetime import datetime
from pathlib import Path
from typing import Any

import yaml

from lib.failure_analysis import classify_error
from lib.har_chain_probe import probe_har_chain

DEFAULT_CATALOG = Path("tests/fixtures/deep_chain_factory/catalog.json")
DEFAULT_OUTPUT_DIR = Path("tmp/deep_chain_pipeline")

SUCCESS_TOKENS = {"保存成功", "操作成功"}
PRIMARY_KEY_TOKENS = {"pkvalue", "billid", "saveresult", "bos_operationresult"}
BUSINESS_KEY_FIELDS = {"number", "billno", "code", "name", "description"}
KEY_PRIORITY = {"number": 0, "billno": 1, "code": 2, "name": 3, "description": 4}


def load_catalog(path: Path | str = DEFAULT_CATALOG) -> dict[str, Any]:
    return json.loads(Path(path).read_text(encoding="utf-8"))


def load_yaml_case(path: Path | str) -> dict[str, Any]:
    case = yaml.safe_load(Path(path).read_text(encoding="utf-8"))
    return case if isinstance(case, dict) else {}


def scenario_stage(scenario: dict[str, Any]) -> str:
    status = str(scenario.get("status") or "")
    if status == "closed_write_passed":
        return "closed_write_passed"
    if status.startswith("blocked"):
        return "blocked_needs_component_or_rule"
    if status in {"menu_open_not_writable", "readonly", "read_only"}:
        return "readonly_or_not_writable"
    if scenario.get("case_file"):
        return "yaml_ready_needs_smoke"
    if scenario.get("latest_local_har"):
        return "har_captured_needs_yaml"
    return "discovered_needs_har"


def summarize_progress(catalog: dict[str, Any]) -> dict[str, Any]:
    scenarios = catalog.get("scenarios") or []
    stage_counts = Counter(scenario_stage(item) for item in scenarios)
    app_counts = Counter(str(item.get("app_label") or "") for item in scenarios if item.get("app_label"))
    closed = stage_counts.get("closed_write_passed", 0)
    total = len(scenarios)
    return {
        "target_cloud": catalog.get("target_cloud", ""),
        "updated_at": catalog.get("updated_at", ""),
        "scenario_count": total,
        "closed_write_passed": closed,
        "blocked": stage_counts.get("blocked_needs_component_or_rule", 0),
        "readonly_or_not_writable": stage_counts.get("readonly_or_not_writable", 0),
        "maturity_percent": round((closed / total * 100), 1) if total else 0.0,
        "stage_counts": dict(sorted(stage_counts.items())),
        "app_counts": dict(sorted(app_counts.items())),
        "current_phase": _current_phase(total, closed, stage_counts),
        "next_focus": build_next_focus(catalog, limit=5),
    }


def _current_phase(total: int, closed: int, stage_counts: Counter[str]) -> str:
    if not total:
        return "stage_0_no_catalog"
    if closed >= 5 and stage_counts.get("blocked_needs_component_or_rule", 0):
        return "stage_2_auto_pipeline_and_component_gaps"
    if closed >= 5:
        return "stage_2_auto_pipeline_and_readback"
    if closed >= 3:
        return "stage_1_deep_chain_factory_seeded"
    return "stage_0_discovery_and_first_writes"


def build_next_focus(catalog: dict[str, Any], *, limit: int = 5) -> list[dict[str, Any]]:
    priority = {
        "blocked_needs_component_or_rule": 0,
        "yaml_ready_needs_smoke": 1,
        "har_captured_needs_yaml": 2,
        "discovered_needs_har": 3,
        "readonly_or_not_writable": 4,
        "closed_write_passed": 9,
    }
    rows = []
    for scenario in catalog.get("scenarios") or []:
        stage = scenario_stage(scenario)
        if stage == "closed_write_passed":
            continue
        rows.append({
            "id": scenario.get("id", ""),
            "app_label": scenario.get("app_label", ""),
            "menu_label": scenario.get("menu_label", ""),
            "stage": stage,
            "next_action": _next_action_for_stage(stage),
        })
    rows.sort(key=lambda row: (priority.get(row["stage"], 99), row["app_label"], row["menu_label"]))
    return rows[:limit]


def match_experience_catalog(
    catalog: dict[str, Any],
    *,
    case: dict[str, Any] | None = None,
    har_probe: dict[str, Any] | None = None,
    limit: int = 3,
) -> dict[str, Any]:
    """Match a new HAR/YAML profile to closed deep-chain experience.

    The matcher is intentionally value-safe: it only uses form ids, app ids,
    HAR chain feature codes and scenario lesson tags. It never stores request
    bodies, cookies, tokens or business values.
    """
    scenarios = catalog.get("scenarios") or []
    query_forms = _query_form_ids(case or {}, har_probe or {})
    query_apps = _query_app_ids(case or {}, har_probe or {})
    query_features = _query_feature_tags(har_probe or {})
    if not query_forms and not query_apps and not query_features:
        return {
            "status": "no_signal",
            "query": {"forms": [], "apps": [], "features": []},
            "matches": [],
            "guardrails": _experience_match_guardrails(),
        }

    rows = []
    for scenario in scenarios:
        score, reasons = _score_scenario_match(
            scenario,
            query_forms=query_forms,
            query_apps=query_apps,
            query_features=query_features,
        )
        if score <= 0:
            continue
        rows.append({
            "scenario_id": scenario.get("id", ""),
            "app_label": scenario.get("app_label", ""),
            "menu_label": scenario.get("menu_label", ""),
            "status": scenario.get("status", ""),
            "score": score,
            "matched_reasons": reasons,
            "reusable_lessons": (scenario.get("lessons") or [])[:4],
        })
    rows.sort(key=lambda item: (-item["score"], item["scenario_id"]))
    return {
        "status": "matched" if rows else "no_match",
        "query": {
            "forms": sorted(query_forms),
            "apps": sorted(query_apps),
            "features": sorted(query_features),
        },
        "matches": rows[:limit],
        "guardrails": _experience_match_guardrails(),
    }


def _query_form_ids(case: dict[str, Any], har_probe: dict[str, Any]) -> set[str]:
    forms: set[str] = set()
    main_form = str(case.get("main_form_id") or "")
    if main_form:
        forms.add(main_form)
    for step in case.get("steps") or []:
        if not isinstance(step, dict):
            continue
        _add_if_present(forms, step.get("form_id"))
        for form_id in step.get("target_forms") or []:
            _add_if_present(forms, form_id)
    for assertion in case.get("assertions") or []:
        if isinstance(assertion, dict):
            _add_if_present(forms, assertion.get("form_id"))
    for meta in (case.get("vars_meta") or {}).values():
        if isinstance(meta, dict):
            _add_if_present(forms, meta.get("form_id"))
    for form_id in ((har_probe.get("summary") or {}).get("forms") or []):
        _add_if_present(forms, form_id)
    return forms


def _query_app_ids(case: dict[str, Any], har_probe: dict[str, Any]) -> set[str]:
    apps: set[str] = set()
    for form_id in _query_form_ids(case, har_probe):
        _add_if_present(apps, _guess_app_id_from_form(form_id))
    for step in case.get("steps") or []:
        if isinstance(step, dict):
            _add_if_present(apps, step.get("app_id"))
    for meta in (case.get("vars_meta") or {}).values():
        if isinstance(meta, dict):
            _add_if_present(apps, meta.get("app_id"))
    return apps


def _query_feature_tags(har_probe: dict[str, Any]) -> set[str]:
    summary = har_probe.get("summary") or {}
    risks = {str(item.get("code") or "") for item in har_probe.get("risks") or []}
    lessons = {str(item.get("code") or "") for item in har_probe.get("lessons") or []}
    features = {item for item in risks | lessons if item}
    if int(summary.get("lookup_prefetch_count") or 0) > 0:
        features.add("lookup_prefetch")
    if int(summary.get("showform_alias_count") or 0) > 0:
        features.add("showform_alias")
    if int(summary.get("write_anchor_count") or 0) > 0:
        features.add("write_anchor")
    if int(summary.get("default_context_count") or 0) > 0:
        features.add("default_context")
    return features


def _score_scenario_match(
    scenario: dict[str, Any],
    *,
    query_forms: set[str],
    query_apps: set[str],
    query_features: set[str],
) -> tuple[int, list[str]]:
    score = 0
    reasons: list[str] = []
    scenario_forms = {str(item) for item in scenario.get("form_ids") or [] if item}
    shared_forms = sorted(query_forms & scenario_forms)
    if shared_forms:
        score += 60 + min(20, len(shared_forms) * 5)
        reasons.append(f"form_id 命中：{', '.join(shared_forms[:4])}")

    scenario_apps = {_guess_app_id_from_form(form_id) for form_id in scenario_forms}
    shared_apps = sorted(query_apps & scenario_apps)
    if shared_apps:
        score += 12
        reasons.append(f"app_id 相近：{', '.join(shared_apps[:3])}")

    scenario_features = _scenario_lesson_tags(scenario)
    shared_features = sorted(query_features & scenario_features)
    if shared_features:
        score += min(24, len(shared_features) * 8)
        reasons.append(f"链路特征相似：{', '.join(shared_features[:4])}")

    if scenario.get("status") == "closed_write_passed":
        score += 8
        reasons.append("已写入闭环样本")
    return score, reasons


def _scenario_lesson_tags(scenario: dict[str, Any]) -> set[str]:
    text = "\n".join(str(item) for item in scenario.get("lessons") or [])
    tags = set()
    tag_keywords = {
        "lookup_prefetch": ("getLookUpList", "预热", "lookup"),
        "showform_alias": ("showForm", "billFormId", "bos_list", "别名"),
        "write_anchor": ("保存", "提交", "bar_save", "btnsave"),
        "default_context": ("默认带出", "服务端上下文", "loadData"),
        "f7_selector": ("F7", "entryRowClick", "btnok", "选人", "选择"),
        "newentry_dialog": ("newentry", "明细", "子窗口", "弹窗"),
        "l2_l3_switch": ("L2", "L3", "pageId"),
    }
    for tag, keywords in tag_keywords.items():
        if any(keyword in text for keyword in keywords):
            tags.add(tag)
    return tags


def _experience_match_guardrails() -> list[str]:
    return [
        "经验匹配只提供排障优先级，不能直接替代 HAR 原始链路比对。",
        "命中相似样本时，优先复用 pageId/组件处理经验；不要硬补 save.post_data。",
        "没有匹配不代表解析失败，应继续按 pageId、变量、环境字段、断言顺序排查。",
    ]


def _add_if_present(target: set[str], value: Any) -> None:
    text = str(value or "").strip()
    if text:
        target.add(text)


def _next_action_for_stage(stage: str) -> str:
    return {
        "blocked_needs_component_or_rule": "补组件处理器或业务校验识别，再重跑 YAML smoke",
        "yaml_ready_needs_smoke": "运行 write_smoke_run，并生成入库验证策略",
        "har_captured_needs_yaml": "生成 YAML，补变量/环境字段后运行 smoke",
        "discovered_needs_har": "Playwright 录制只读/新增页 HAR",
        "readonly_or_not_writable": "标记只读或等待人工确认可写入口",
    }.get(stage, "人工确认下一步")


def infer_write_verification_strategy(
    case: dict[str, Any],
    smoke_summary: dict[str, Any] | None = None,
) -> dict[str, Any]:
    smoke_summary = smoke_summary or {}
    if not smoke_summary:
        business_keys = _case_business_keys(case)
        return {
            "status": "not_checked",
            "method": "missing_smoke_evidence",
            "reason": "尚未提供 YAML smoke 证据，不能判断是否真实入库。",
            "business_keys": business_keys,
            "readback_plan": build_readback_plan(case, business_keys),
        }
    if smoke_summary and smoke_summary.get("passed") is False:
        return {
            "status": "not_checked",
            "method": "blocked_by_execution_failure",
            "reason": "执行未通过，不能做入库验证。",
            "business_keys": [],
            "readback_plan": build_readback_plan(case, []),
        }

    token_set = {
        str(token)
        for event in smoke_summary.get("write_events") or []
        for token in event.get("response_tokens") or []
    }
    lowered_tokens = {token.lower() for token in token_set}
    business_keys = _case_business_keys(case)

    if lowered_tokens & PRIMARY_KEY_TOKENS:
        return {
            "status": "verified_by_response",
            "method": "primary_key_or_operation_result",
            "reason": "保存/提交响应包含主键或明确操作结果，可作为强入库证据。",
            "business_keys": business_keys,
            "readback_plan": build_readback_plan(case, business_keys),
        }
    if token_set & SUCCESS_TOKENS and business_keys:
        return {
            "status": "needs_readback",
            "method": "business_key_query",
            "reason": "响应只有成功提示，建议按编码/名称/描述等业务键做后置回查。",
            "business_keys": business_keys,
            "readback_plan": build_readback_plan(case, business_keys),
        }
    if token_set & SUCCESS_TOKENS:
        return {
            "status": "needs_manual_or_custom_query",
            "method": "success_token_without_business_key",
            "reason": "响应只有成功提示，但 YAML 中缺少可稳定回查的业务键。",
            "business_keys": [],
            "readback_plan": build_readback_plan(case, []),
        }
    return {
        "status": "manual_required",
        "method": "no_write_evidence",
        "reason": "未发现保存主键、成功 token 或业务键，需补断言或人工确认。",
        "business_keys": business_keys,
        "readback_plan": build_readback_plan(case, business_keys),
    }


def build_readback_plan(
    case: dict[str, Any],
    business_keys: list[dict[str, str]] | None = None,
) -> dict[str, Any]:
    """Build a value-safe readback plan from YAML business keys.

    The plan is intentionally declarative. It tells the pipeline/AI which form
    and business key should be used for post-save query assertions without
    embedding cookies, raw HAR, or resolved personal data.
    """
    business_keys = list(business_keys if business_keys is not None else _case_business_keys(case))
    if not business_keys:
        return {
            "status": "not_ready",
            "method": "manual_or_custom_query",
            "reason": "YAML 中没有稳定业务键，需补 number/name/billno/description 变量或手工确认。",
            "plans": [],
            "guardrails": _readback_guardrails(),
        }

    grouped: dict[str, list[dict[str, str]]] = {}
    for item in business_keys:
        form_id = str(item.get("form_id") or case.get("main_form_id") or "")
        grouped.setdefault(form_id, []).append(item)

    plans = []
    for form_id, keys in grouped.items():
        app_id = next((str(item.get("app_id") or "") for item in keys if item.get("app_id")), "")
        if not app_id:
            app_id = _guess_app_id_from_form(form_id)
        sorted_keys = sorted(
            keys,
            key=lambda item: KEY_PRIORITY.get(str(item.get("field_key") or ""), 99),
        )
        filters = []
        for item in sorted_keys:
            var_name = str(item.get("var") or "")
            field_key = str(item.get("field_key") or "")
            filters.append({
                "field_key": field_key,
                "value_ref": f"${{vars.{var_name}}}" if var_name else "",
                "value_template": _case_var_template(case, var_name),
                "source": str(item.get("source") or ""),
            })
        strongest = filters[0] if filters else {}
        plans.append({
            "form_id": form_id,
            "app_id": app_id,
            "query_method": "list_filter_or_common_search",
            "preferred_filter": strongest,
            "fallback_filters": filters[1:],
            "success_criteria": "至少回查到 1 条记录，且首选业务键与本次运行变量一致。",
            "suggested_assertion": {
                "type": "readback_by_business_key",
                "form_id": form_id,
                "app_id": app_id,
                "field_key": strongest.get("field_key", ""),
                "value": strongest.get("value_ref", ""),
            },
        })

    return {
        "status": "ready",
        "method": "business_key_query",
        "reason": "已从 YAML 识别可用于后置回查的业务键。",
        "plans": plans,
        "guardrails": _readback_guardrails(),
    }


def _case_var_template(case: dict[str, Any], var_name: str) -> str:
    value = (case.get("vars") or {}).get(var_name, "")
    if isinstance(value, (str, int, float, bool)) or value is None:
        return "" if value is None else str(value)
    return json.dumps(value, ensure_ascii=False, default=str)


def _readback_guardrails() -> list[str]:
    return [
        "只做查询/回读，不允许新增、保存、提交、审核、删除、导入或上传。",
        "优先使用本次 YAML 的 number/billno/code/name/description 等业务键，不使用浏览器会话凭据或真实 HAR 原文。",
        "若业务键不是唯一键，应结合创建时间、组织或 CRPLY_ 前缀缩小范围。",
        "回查失败不能直接改 save.post_data，应先检查 pageId 链路、变量覆盖和业务键是否被用户修改。",
    ]


def _case_business_keys(case: dict[str, Any]) -> list[dict[str, str]]:
    keys: list[dict[str, str]] = []
    vars_meta = case.get("vars_meta") or {}
    for var_name, meta in vars_meta.items():
        if not isinstance(meta, dict):
            continue
        field_key = str(meta.get("field_key") or "").lower()
        if field_key in BUSINESS_KEY_FIELDS:
            keys.append({
                "source": "vars_meta",
                "var": str(var_name),
                "field_key": field_key,
                "form_id": str(meta.get("form_id") or case.get("main_form_id") or ""),
                "app_id": str(meta.get("app_id") or _guess_app_id_from_form(str(meta.get("form_id") or case.get("main_form_id") or ""))),
            })
    if keys:
        return keys
    for var_name in (case.get("vars") or {}).keys():
        key = str(var_name)
        if any(token in key.lower() for token in ("number", "name", "description", "code", "billno")):
            keys.append({
                "source": "vars",
                "var": key,
                "field_key": _field_from_var_name(key),
                "form_id": str(case.get("main_form_id") or ""),
                "app_id": _guess_app_id_from_form(str(case.get("main_form_id") or "")),
            })
    return keys


def _guess_app_id_from_form(form_id: str) -> str:
    return form_id.split("_", 1)[0] if "_" in form_id else "bos"


def _field_from_var_name(var_name: str) -> str:
    lower = var_name.lower()
    if "number" in lower or "code" in lower:
        return "number"
    if "description" in lower:
        return "description"
    if "billno" in lower:
        return "billno"
    if "name" in lower:
        return "name"
    return ""


def classify_pipeline_outcome(
    *,
    case: dict[str, Any] | None = None,
    smoke_summary: dict[str, Any] | None = None,
    har_probe: dict[str, Any] | None = None,
) -> dict[str, Any]:
    case = case or {}
    smoke_summary = smoke_summary or {}
    har_probe = har_probe or {}

    if not smoke_summary:
        return {
            "category": "pipeline_evidence_missing",
            "severity": "medium",
            "root_cause": "尚未提供 YAML smoke 结果，无法完成自动闭环判定。",
            "recommended_actions": [
                "先运行 scripts/write_smoke_run.py 生成脱敏 smoke 证据。",
                "再用 scripts/deep_chain_pipeline.py scenario-report 合并 HAR 链路和写入结果。",
            ],
        }

    if smoke_summary.get("passed") is False:
        failed = (smoke_summary.get("failed_steps") or [{}])[0]
        analysis = classify_error(
            str(failed.get("error") or ""),
            step={"id": failed.get("id", ""), "form_id": case.get("main_form_id", "")},
            case=case,
        )
        return {
            "category": analysis.get("category", "unknown"),
            "severity": analysis.get("severity", "medium"),
            "root_cause": analysis.get("root_cause", ""),
            "recommended_actions": analysis.get("recommended_actions", []),
        }

    verification = infer_write_verification_strategy(case, smoke_summary)
    if smoke_summary.get("passed") is True and verification["status"] != "verified_by_response":
        return {
            "category": "write_verification_gap",
            "severity": "medium",
            "root_cause": verification["reason"],
            "recommended_actions": [
                "优先按业务键补后置回查断言。",
                "若无法稳定回查，再允许人工确认入库，但要记录原因。",
            ],
        }

    risk_codes = {
        str(risk.get("code") or "")
        for risk in har_probe.get("risks") or []
    }
    if "write_anchor_uses_l2_pageid" in risk_codes:
        return {
            "category": "pageid_chain_risk",
            "severity": "high",
            "root_cause": "HAR 链路中写入锚点疑似使用 L2 pageId，需确认是否为工具栏桥接。",
            "recommended_actions": [
                "比对 HAR 原始 pageId 与 YAML runner pageid_trace。",
                "保存/提交真实编辑态应使用 L3，不要硬补 save.post_data。",
            ],
        }

    return {
        "category": "closed_or_ready",
        "severity": "low",
        "root_cause": "未发现阻断性风险。",
        "recommended_actions": ["若样本稳定且脱敏，可沉淀为 baseline 或经验库条目。"],
    }


def build_scenario_report(
    scenario: dict[str, Any],
    *,
    case: dict[str, Any] | None = None,
    har_probe: dict[str, Any] | None = None,
    smoke_evidence: dict[str, Any] | None = None,
    catalog: dict[str, Any] | None = None,
) -> dict[str, Any]:
    smoke_summary = (smoke_evidence or {}).get("summary") or {}
    verification = infer_write_verification_strategy(case or {}, smoke_summary)
    return {
        "schema_version": 1,
        "generated_at": datetime.now().isoformat(timespec="seconds"),
        "scenario": {
            "id": scenario.get("id", ""),
            "app_label": scenario.get("app_label", ""),
            "menu_label": scenario.get("menu_label", ""),
            "stage": scenario_stage(scenario),
            "status": scenario.get("status", ""),
            "case_file": scenario.get("case_file", ""),
        },
        "har_chain": _compact_probe(har_probe or {}),
        "smoke_summary": smoke_summary,
        "write_verification": verification,
        "experience_matches": match_experience_catalog(
            catalog or {"scenarios": [scenario]},
            case=case or {},
            har_probe=har_probe or {},
        ),
        "failure_or_gap": classify_pipeline_outcome(
            case=case,
            smoke_summary=smoke_summary,
            har_probe=har_probe,
        ),
        "value_safety": {
            "raw_har_committed": False,
            "raw_events_committed": False,
            "stores_credentials": False,
        },
    }


def _compact_probe(probe: dict[str, Any]) -> dict[str, Any]:
    if not probe:
        return {}
    return {
        "summary": probe.get("summary", {}),
        "lesson_codes": [item.get("code", "") for item in probe.get("lessons") or []],
        "risk_codes": [item.get("code", "") for item in probe.get("risks") or []],
    }


def build_report_from_paths(
    *,
    catalog_path: Path | str = DEFAULT_CATALOG,
    scenario_id: str,
    case_path: Path | str | None = None,
    har_path: Path | str | None = None,
    smoke_evidence_path: Path | str | None = None,
) -> dict[str, Any]:
    catalog = load_catalog(catalog_path)
    scenario = next((item for item in catalog.get("scenarios") or [] if item.get("id") == scenario_id), None)
    if not scenario:
        raise ValueError(f"scenario not found: {scenario_id}")
    resolved_case_path = Path(case_path or scenario.get("case_file") or "")
    case = load_yaml_case(resolved_case_path) if str(resolved_case_path) else {}
    har_probe = probe_har_chain(har_path) if har_path else {}
    smoke_evidence = (
        json.loads(Path(smoke_evidence_path).read_text(encoding="utf-8"))
        if smoke_evidence_path
        else {}
    )
    return build_scenario_report(
        scenario,
        case=case,
        har_probe=har_probe,
        smoke_evidence=smoke_evidence,
        catalog=catalog,
    )


def build_auto_pipeline_report(
    *,
    catalog_path: Path | str = DEFAULT_CATALOG,
    scenario_id: str,
    case_path: Path | str | None = None,
    har_path: Path | str | None = None,
    smoke_evidence_path: Path | str | None = None,
    output_dir: Path | str = DEFAULT_OUTPUT_DIR,
    generated_case_path: Path | str | None = None,
    include_readback_assertions: bool = True,
) -> dict[str, Any]:
    """Run the value-safe parts of one deep-chain closed-loop pipeline.

    This function intentionally does not execute writes. It builds the same
    artifacts a human would create by hand: HAR chain profile, optional YAML,
    experience matches, readback plan, and the final scenario report. The CLI
    can then decide whether to run the separate write smoke command with an
    explicit confirmation token.
    """
    catalog = load_catalog(catalog_path)
    scenario = _find_scenario(catalog, scenario_id)
    output_root = Path(output_dir)
    output_root.mkdir(parents=True, exist_ok=True)

    artifacts: dict[str, Any] = {}
    pipeline_steps: list[dict[str, Any]] = []
    har_probe: dict[str, Any] = {}

    if har_path:
        har_probe = probe_har_chain(har_path)
        har_report_path = output_root / f"{_safe_slug(scenario_id)}_har_chain.json"
        write_json_report(_compact_probe(har_probe), har_report_path)
        artifacts["har_chain_report"] = str(har_report_path)
        pipeline_steps.append({
            "name": "har_chain_probe",
            "status": "done",
            "output": str(har_report_path),
        })
    else:
        pipeline_steps.append({
            "name": "har_chain_probe",
            "status": "skipped",
            "reason": "未提供 HAR；只能基于 YAML/catalog 做经验匹配。",
        })

    resolved_case_path = Path(case_path or "")
    generated_yaml = False
    if not resolved_case_path and har_path:
        from lib.har_extractor import build_yaml_case

        yaml_text = build_yaml_case(
            Path(har_path),
            str(scenario.get("menu_label") or scenario_id),
            include_readback_assertions=include_readback_assertions,
        )
        resolved_case_path = Path(
            generated_case_path
            or output_root / f"{_safe_slug(scenario_id)}_generated.yaml"
        )
        resolved_case_path.parent.mkdir(parents=True, exist_ok=True)
        resolved_case_path.write_text(yaml_text, encoding="utf-8")
        generated_yaml = True
        pipeline_steps.append({
            "name": "yaml_generation",
            "status": "done",
            "output": str(resolved_case_path),
            "include_readback_assertions": include_readback_assertions,
        })
    elif resolved_case_path:
        pipeline_steps.append({
            "name": "yaml_generation",
            "status": "reused",
            "output": str(resolved_case_path),
        })
    else:
        pipeline_steps.append({
            "name": "yaml_generation",
            "status": "blocked",
            "reason": "缺少 HAR 和 YAML，无法生成可执行用例。",
        })

    case = load_yaml_case(resolved_case_path) if resolved_case_path else {}
    if resolved_case_path:
        artifacts["case_file"] = str(resolved_case_path)
        artifacts["case_generated"] = generated_yaml

    smoke_evidence = (
        json.loads(Path(smoke_evidence_path).read_text(encoding="utf-8"))
        if smoke_evidence_path
        else {}
    )
    if smoke_evidence_path:
        artifacts["smoke_evidence"] = str(smoke_evidence_path)
        pipeline_steps.append({
            "name": "write_smoke",
            "status": "evidence_loaded",
            "output": str(smoke_evidence_path),
        })
    else:
        pipeline_steps.append({
            "name": "write_smoke",
            "status": "not_run",
            "reason": "默认不写库；需要 CLI 显式 --run-smoke 和确认 token。",
        })

    scenario_report = build_scenario_report(
        scenario,
        case=case,
        har_probe=har_probe,
        smoke_evidence=smoke_evidence,
        catalog=catalog,
    )
    automation = _build_pipeline_next_actions(
        scenario_report,
        has_case=bool(resolved_case_path),
        has_har=bool(har_path),
        has_smoke=bool(smoke_evidence_path),
    )
    return {
        "schema_version": 1,
        "generated_at": datetime.now().isoformat(timespec="seconds"),
        "pipeline": {
            "status": automation["status"],
            "scenario_id": scenario_id,
            "steps": pipeline_steps,
            "artifacts": artifacts,
            "next_actions": automation["next_actions"],
            "baseline_candidate": automation["baseline_candidate"],
        },
        "scenario_report": scenario_report,
        "value_safety": {
            "raw_har_committed": False,
            "raw_events_committed": False,
            "stores_credentials": False,
            "write_requires_explicit_confirmation": True,
        },
    }


def _find_scenario(catalog: dict[str, Any], scenario_id: str) -> dict[str, Any]:
    scenario = next((item for item in catalog.get("scenarios") or [] if item.get("id") == scenario_id), None)
    if not scenario:
        raise ValueError(f"scenario not found: {scenario_id}")
    return scenario


def _build_pipeline_next_actions(
    report: dict[str, Any],
    *,
    has_case: bool,
    has_har: bool,
    has_smoke: bool,
) -> dict[str, Any]:
    failure = report.get("failure_or_gap") or {}
    verification = report.get("write_verification") or {}
    matches = report.get("experience_matches") or {}
    next_actions: list[str] = []
    if not has_har:
        next_actions.append("补充本地 HAR 后重跑，可获得 pageId 链路画像和更准经验匹配。")
    if not has_case:
        next_actions.append("先提供 HAR 或 YAML；没有 YAML 时无法进入 runner 执行。")
    elif not has_smoke:
        next_actions.append("确认测试数据安全后运行 write smoke，生成脱敏执行证据。")
    if verification.get("readback_plan", {}).get("status") == "ready":
        next_actions.append("保留/补齐 readback_by_business_key，只读回查真实入库。")
    if matches.get("status") == "matched":
        next_actions.append("优先复用 experience_matches 中命中样本的 pageId/组件经验。")
    if failure.get("category") and failure.get("category") not in {"closed_or_ready"}:
        next_actions.extend(str(item) for item in failure.get("recommended_actions") or [])

    smoke_summary = report.get("smoke_summary") or {}
    status = "needs_case"
    if has_case and not has_smoke:
        status = "yaml_ready_needs_smoke"
    if smoke_summary.get("passed") is False:
        status = "failed_needs_ai_repair"
    elif smoke_summary.get("passed") is True:
        write_status = verification.get("status")
        status = "closed_verified" if write_status == "verified_by_response" else "passed_needs_readback"
    baseline_candidate = _baseline_candidate(report, status=status)
    return {
        "status": status,
        "next_actions": _dedupe_preserve_order(next_actions),
        "baseline_candidate": baseline_candidate,
    }


def _baseline_candidate(report: dict[str, Any], *, status: str) -> dict[str, Any]:
    har_chain = report.get("har_chain") or {}
    risk_codes = set(har_chain.get("risk_codes") or [])
    if status == "closed_verified" and not risk_codes:
        return {
            "status": "ready",
            "reason": "执行和入库证据已闭环，HAR 链路无高风险结构，可考虑脱敏后沉淀 baseline。",
        }
    if status == "passed_needs_readback":
        return {
            "status": "needs_readback",
            "reason": "执行已通过，但仍需业务键回查或人工确认真实入库。",
        }
    if risk_codes:
        return {
            "status": "review",
            "reason": f"HAR 链路存在需复核风险：{', '.join(sorted(risk_codes))}",
        }
    return {
        "status": "not_ready",
        "reason": "尚未完成 YAML smoke 与入库验证闭环。",
    }


def _dedupe_preserve_order(items: list[str]) -> list[str]:
    seen: set[str] = set()
    out = []
    for item in items:
        if item and item not in seen:
            seen.add(item)
            out.append(item)
    return out


def _safe_slug(value: str) -> str:
    safe = "".join(ch if ch.isalnum() or ch in "-_." else "_" for ch in str(value))
    return safe[:100] or "scenario"


def write_json_report(report: dict[str, Any], output: Path | str) -> Path:
    path = Path(output)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    return path
