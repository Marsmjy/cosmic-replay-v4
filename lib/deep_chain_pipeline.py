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

READBACK_STRATEGY_LIBRARY: dict[str, dict[str, Any]] = {
    "khr_hcdm_fapplybill": {
        "strategy_id": "hcdm_salary_adjust_apply_menu_refresh",
        "preferred_fields": ["name", "billno"],
        "app_id": "hcdm",
        "method": "fresh_menu_refresh",
        "assertion_strategy": "fresh_menu_refresh",
        "assertion_field_key": "khr_name",
        "menu_id": "2371045759278662656",
        "uniqueness_hint": "员工定调薪申请单保存后需新会话重新进入菜单并刷新列表，再按名称/单号回查；通用 commonSearch(khr_name) 在该表单不可靠。",
    },
    "hpdi_bizdatabill": {
        "strategy_id": "ua_submit_business_key",
        "preferred_fields": ["description", "billno", "number", "name"],
        "method": "business_key_with_template_context",
        "uniqueness_hint": "UA 提报建议使用 CRPLY_ 描述/单据号，并结合业务模板或创建时间缩小范围。",
    },
    "hpdi_bizdatabillnewentry": {
        "strategy_id": "ua_submit_entry_business_key",
        "preferred_fields": ["name", "description", "billno", "number"],
        "method": "entry_business_key_query",
        "uniqueness_hint": "子窗明细保存后优先回查主单或明细业务键，避免只看主保存 PASS。",
    },
    "hsas_payrollscene": {
        "strategy_id": "salary_scene_number_name",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_name_common_search",
        "uniqueness_hint": "薪资核算场景优先使用 CRPLY_ 编码回查；规则分组/F7 子窗必须先确认回填。",
    },
    "hsbs_salaryitem": {
        "strategy_id": "salary_item_number_name",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_name_common_search",
        "uniqueness_hint": "薪酬项目编码应唯一，优先按 number 回查。",
    },
    "hsbs_statisticstag": {
        "strategy_id": "salary_category_number_name",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_name_common_search",
        "uniqueness_hint": "薪酬项目类别通常按编码/名称回查；层级 taglevel 是环境字段。",
    },
    "hsbs_calperiodtype": {
        "strategy_id": "salary_period_number_name",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_name_common_search",
        "uniqueness_hint": "薪资期间建议按编码回查，并关注期间分录日期是否完整。",
    },
    "hsas_payrollgrp": {
        "strategy_id": "salary_group_number_org",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_with_org_context",
        "uniqueness_hint": "薪资核算组建议按 number + 组织上下文回查。",
    },
    "hsas_retroreason": {
        "strategy_id": "retro_reason_number_name",
        "preferred_fields": ["number", "name", "description"],
        "method": "number_name_common_search",
        "uniqueness_hint": "薪资回溯原因可按 number/name 回查；注意 bos_list/billFormId 别名。",
    },
}


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
        "sample_expansion": {
            "next_batch": build_sample_expansion_plan(catalog, limit=5)["next_batch"],
        },
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


def build_sample_expansion_plan(catalog: dict[str, Any], *, limit: int = 8) -> dict[str, Any]:
    """Build the next safe expansion batch for real-environment HAR samples.

    The plan is deliberately value-safe and non-executing. It translates the
    catalog into concrete Level 0/1/2 tasks so the team can keep collecting new
    HAR experience without letting automation silently submit, audit or delete
    business data.
    """
    candidates = []
    reference_count = 0
    for scenario in catalog.get("scenarios") or []:
        stage = scenario_stage(scenario)
        if stage == "closed_write_passed":
            reference_count += 1
            continue
        candidates.append(_expansion_candidate_for_scenario(scenario, stage=stage))
    candidates.sort(key=lambda item: (item["priority"], item["app_label"], item["menu_label"]))
    return {
        "schema_version": 1,
        "generated_at": datetime.now().isoformat(timespec="seconds"),
        "target_cloud": catalog.get("target_cloud", ""),
        "reference_closed_samples": reference_count,
        "candidate_count": len(candidates),
        "levels": [
            {
                "level": "L0",
                "name": "只读采集",
                "allowed": "登录、展开应用、打开列表、查询、刷新、采集 HAR/pageId 摘要。",
                "blocked": "不点新增、保存、提交、审核、删除、导入、上传。",
            },
            {
                "level": "L1",
                "name": "打开新增不保存",
                "allowed": "打开新增页、采集 showForm/L3、识别字段和组件，不写库。",
                "blocked": "不保存、不提交、不点确定类写入按钮。",
            },
            {
                "level": "L2",
                "name": "测试数据写入闭环",
                "allowed": "仅 CRPLY_ 前缀测试数据，显式确认后保存并做只读回查。",
                "blocked": "提交、审核、删除、反审核、导入、上传、批量操作。",
            },
            {
                "level": "L3",
                "name": "高风险动作",
                "allowed": "当前默认禁止，除非用户单独批准并提供回滚方案。",
                "blocked": "提交、审核、生效、删除、上传、导入、批量处理。",
            },
        ],
        "guardrails": [
            "原始 HAR、cookie、token、截图和真实运行日志只能放 ignored 目录，不能提交 Git。",
            "写库 smoke 必须显式传 --confirm-write YES_GENERATE_TEST_DATA，并使用 CRPLY_ 测试数据。",
            "PASS 后必须按 readback_plan 做主键/业务键只读回查；没有回查证据不能沉淀 baseline。",
            "发现解析失败时优先查 pageId 链路、组件链路和环境字段，不允许硬补 save.post_data。",
        ],
        "next_batch": candidates[:limit],
    }


def _expansion_candidate_for_scenario(scenario: dict[str, Any], *, stage: str) -> dict[str, Any]:
    scenario_id = str(scenario.get("id") or "")
    case_file = str(scenario.get("case_file") or "")
    har_path = str(scenario.get("latest_local_har") or "")
    base = {
        "scenario_id": scenario_id,
        "app_label": str(scenario.get("app_label") or ""),
        "menu_label": str(scenario.get("menu_label") or ""),
        "stage": stage,
        "env": str(scenario.get("env") or ""),
        "required_confirmation": "",
        "har_policy": "raw_har_local_ignored_only",
    }
    if stage == "blocked_needs_component_or_rule":
        return {
            **base,
            "priority": 0,
            "risk_level": "high",
            "allowed_level": "repair_first",
            "objective": "先补组件处理器或业务校验识别，再重新进入 HAR→YAML→smoke。",
            "recommended_action": "读取 scenario-report 和 failure_analysis，补最小通用规则后重跑回归。",
            "command_hint": f"./venv/bin/python scripts/deep_chain_pipeline.py scenario-report --scenario-id {scenario_id}",
        }
    if stage == "yaml_ready_needs_smoke":
        return {
            **base,
            "priority": 1,
            "risk_level": "medium",
            "allowed_level": "L2",
            "objective": "已有 YAML，下一步做测试数据写入 smoke 和入库回查。",
            "recommended_action": "确认测试数据安全后运行 run-scenario --run-smoke，再按 readback_plan 验证。",
            "required_confirmation": "YES_GENERATE_TEST_DATA",
            "command_hint": (
                f"./venv/bin/python scripts/deep_chain_pipeline.py run-scenario --scenario-id {scenario_id}"
                f"{' --case ' + case_file if case_file else ''} --run-smoke --confirm-write YES_GENERATE_TEST_DATA"
            ),
        }
    if stage == "har_captured_needs_yaml":
        return {
            **base,
            "priority": 2,
            "risk_level": "medium",
            "allowed_level": "L1_to_L2",
            "objective": "已有本地 HAR，先生成 YAML 和链路画像，再决定是否写入 smoke。",
            "recommended_action": "运行 run-scenario 生成 YAML、pageId 画像和经验匹配；确认后再执行 smoke。",
            "command_hint": (
                f"./venv/bin/python scripts/deep_chain_pipeline.py run-scenario --scenario-id {scenario_id}"
                f"{' --har ' + har_path if har_path else ''}"
            ),
        }
    if stage == "discovered_needs_har":
        return {
            **base,
            "priority": 3,
            "risk_level": "low",
            "allowed_level": "L0_to_L1",
            "objective": "先录制只读/打开新增不保存 HAR，补齐 pageId 和组件画像。",
            "recommended_action": "用 Playwright discovery record-har 打开菜单和新增页，但不保存。",
            "command_hint": (
                "./venv/bin/python scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 "
                f"--open-menu-samples {base['app_label']}:{base['menu_label']} --record-har"
            ),
        }
    return {
        **base,
        "priority": 4,
        "risk_level": "low",
        "allowed_level": "L0",
        "objective": "当前识别为只读或未发现写入入口，先复核菜单能力边界。",
        "recommended_action": "重新做只读 discovery，确认是否确实无新增/保存入口；不要强行写库。",
        "command_hint": (
            "./venv/bin/python scripts/playwright_discover.py --env sit --app-keyword 薪酬福利云 "
            f"--open-menu-samples {base['app_label']}:{base['menu_label']} --record-har"
        ),
    }


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
    runtime_billno_step = _find_runtime_billno_readback_step(case)
    if runtime_billno_step:
        return {
            "status": "ready",
            "method": "runtime_billno_query",
            "reason": "HAR 包含提交后按运行时单号查询最终业务列表的只读链路。",
            "plans": [{
                "form_id": runtime_billno_step["form_id"],
                "app_id": runtime_billno_step["app_id"],
                "query_method": "recorded_runtime_billno_query",
                "preferred_filter": {
                    "field_key": "billno",
                    "value_ref": "${runtime.billno}",
                    "value_template": "",
                    "source": "runtime_submit_response",
                },
                "fallback_filters": [],
                "strategy": {
                    "strategy_id": "recorded_runtime_billno_query",
                    "source": "recorded_runtime_query",
                    "method": "recorded_step_response",
                    "recorded_step": runtime_billno_step["step"],
                    "assertion_field_key": "billno",
                    "grid_key": runtime_billno_step["grid_key"],
                    "preferred_fields": ["billno"],
                    "available_fields": ["billno"],
                    "uniqueness_hint": "提交响应产生的新单号必须出现在后续最终业务列表查询中。",
                    "manual_fallback": "若未命中，检查提交响应单号提取、查询参数注入和最终列表 pageId。",
                },
                "assertion_policy": {
                    "auto_append": True,
                    "mode": "strict",
                    "reason": "该查询由 HAR 录制且运行时单号由本次提交响应动态注入。",
                },
                "success_criteria": "最终业务列表按本次运行 billno 至少回查到 1 条记录。",
                "suggested_assertion": {
                    "type": "readback_by_business_key",
                    "step": runtime_billno_step["step"],
                    "form_id": runtime_billno_step["form_id"],
                    "app_id": runtime_billno_step["app_id"],
                    "field_key": "billno",
                    "value": "${runtime.billno}",
                    "value_from_runtime": "billno",
                    "grid_key": runtime_billno_step["grid_key"],
                },
            }],
            "guardrails": _readback_guardrails(),
        }

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
        readback_strategy = _readback_strategy_for_form(form_id, filters)
        recorded_step = {}
        for index, candidate_filter in enumerate(filters):
            candidate_key = sorted_keys[index] if index < len(sorted_keys) else {}
            candidate_step = _find_recorded_readback_step(
                case,
                value_ref=str(candidate_filter.get("value_ref") or ""),
                write_step_id=str(candidate_key.get("write_step_id") or ""),
                preferred_field_key=str(candidate_filter.get("field_key") or ""),
            )
            if candidate_step:
                recorded_step = candidate_step
                strongest = candidate_filter
                break
        if recorded_step:
            assertion_strategy = (
                "fresh_recorded_context"
                if recorded_step.get("pageid_source_kind") == "responsePageId"
                else "recorded_step"
            )
            readback_strategy = {
                "strategy_id": "recorded_post_write_query",
                "source": "recorded_har_query",
                "method": (
                    "fresh_recorded_navigation_query"
                    if assertion_strategy == "fresh_recorded_context"
                    else "recorded_step_response"
                ),
                "assertion_strategy": assertion_strategy,
                "recorded_step": recorded_step["step"],
                "recorded_form_id": recorded_step["form_id"],
                "recorded_app_id": recorded_step["app_id"],
                "assertion_field_key": recorded_step["field_key"],
                "query_field_key": recorded_step.get("query_field_key", ""),
                "query_value_ref": recorded_step.get("query_value_ref", ""),
                "grid_key": recorded_step["grid_key"],
                "retry_until_found": assertion_strategy == "recorded_step",
                "preferred_fields": [recorded_step["field_key"]],
                "available_fields": [recorded_step["field_key"]],
                "uniqueness_hint": "复用 HAR 中保存后真实查询，并将查询值绑定到本次运行变量。",
                "manual_fallback": "若录制查询未命中，检查变量是否传播到查询参数及目标环境列表字段。",
            }
        if readback_strategy.get("app_id"):
            app_id = str(readback_strategy.get("app_id") or app_id)
        assertion_form_id = str(readback_strategy.get("recorded_form_id") or form_id)
        assertion_app_id = str(readback_strategy.get("recorded_app_id") or app_id)
        suggested_assertion = {
            "type": "readback_by_business_key",
            "form_id": assertion_form_id,
            "app_id": assertion_app_id,
            "field_key": readback_strategy.get("assertion_field_key") or strongest.get("field_key", ""),
            "value": strongest.get("value_ref", ""),
            "match_mode": "grid_field_exact",
        }
        if (
            readback_strategy.get("recorded_step")
            and readback_strategy.get("assertion_strategy") == "recorded_step"
        ):
            suggested_assertion["step"] = str(readback_strategy.get("recorded_step") or "")
        elif readback_strategy.get("recorded_step"):
            suggested_assertion["query_step"] = str(readback_strategy.get("recorded_step") or "")
        if readback_strategy.get("grid_key"):
            suggested_assertion["grid_key"] = str(readback_strategy.get("grid_key") or "")
        if readback_strategy.get("retry_until_found"):
            suggested_assertion["retry_until_found"] = True
        if readback_strategy.get("query_value_ref"):
            suggested_assertion["query_field_key"] = str(readback_strategy.get("query_field_key") or "")
            suggested_assertion["query_value_ref"] = str(readback_strategy.get("query_value_ref") or "")
        if readback_strategy.get("assertion_strategy"):
            suggested_assertion["strategy"] = str(readback_strategy.get("assertion_strategy") or "")
        if readback_strategy.get("menu_id"):
            suggested_assertion["menu_id"] = str(readback_strategy.get("menu_id") or "")
        plans.append({
            "form_id": form_id,
            "app_id": app_id,
            "query_method": "list_filter_or_common_search",
            "preferred_filter": strongest,
            "fallback_filters": filters[1:],
            "strategy": readback_strategy,
            "assertion_policy": _readback_assertion_policy(readback_strategy),
            "success_criteria": "至少回查到 1 条记录，且首选业务键与本次运行变量一致。",
            "suggested_assertion": suggested_assertion,
        })

    return {
        "status": "ready",
        "method": "business_key_query",
        "reason": "已从 YAML 识别可用于后置回查的业务键。",
        "plans": plans,
        "guardrails": _readback_guardrails(),
    }


def _readback_strategy_for_form(form_id: str, filters: list[dict[str, str]]) -> dict[str, Any]:
    strategy = READBACK_STRATEGY_LIBRARY.get(form_id)
    available_fields = [str(item.get("field_key") or "") for item in filters if item.get("field_key")]
    if strategy:
        preferred = [field for field in strategy["preferred_fields"] if field in available_fields]
        return {
            "strategy_id": strategy["strategy_id"],
            "source": "strategy_library",
            "method": strategy["method"],
            **({"app_id": strategy["app_id"]} if strategy.get("app_id") else {}),
            **({"assertion_strategy": strategy["assertion_strategy"]} if strategy.get("assertion_strategy") else {}),
            **({"assertion_field_key": strategy["assertion_field_key"]} if strategy.get("assertion_field_key") else {}),
            **({"menu_id": strategy["menu_id"]} if strategy.get("menu_id") else {}),
            "preferred_fields": preferred or strategy["preferred_fields"],
            "available_fields": available_fields,
            "uniqueness_hint": strategy["uniqueness_hint"],
            "manual_fallback": "若按推荐字段回查不到，先确认变量值和 pageId 链路，再允许人工确认。",
        }
    return {
        "strategy_id": "generic_business_key",
        "source": "generic",
        "method": "business_key_common_search",
        "preferred_fields": available_fields[:2] or ["number", "name", "description"],
        "available_fields": available_fields,
        "uniqueness_hint": "通用策略：优先使用 CRPLY_ 编码/名称/描述等本次运行变量回查。",
        "manual_fallback": "无稳定业务键或回查接口不可用时，才允许人工确认入库。",
    }


def _readback_assertion_policy(strategy: dict[str, Any]) -> dict[str, Any]:
    """Decide whether a readback plan may become a hard YAML assertion.

    A generic commonSearch plan is useful guidance, but not reliable enough to
    fail a saved case: some Kingdee list forms do not expose freshly saved rows
    through commonSearch with the bill form id. Only strategy-library entries
    that we have explicitly modeled are auto-appended as hard assertions.
    """
    if (
        strategy.get("source") == "recorded_har_query"
        and strategy.get("assertion_strategy") == "fresh_recorded_context"
    ):
        return {
            "auto_append": False,
            "mode": "candidate",
            "reason": (
                "该查询需要在新会话重建录制导航上下文；真实样本表明列表过滤、"
                "组织上下文或数据可见性可能与保存会话不同。未经表单专用验证前，"
                "只能作为候选回查，不能自动生成硬断言。"
            ),
        }
    if strategy.get("source") in {"strategy_library", "recorded_har_query"}:
        return {
            "auto_append": True,
            "mode": "strict",
            "reason": "该表单已有专用入库回查策略，可自动生成硬断言。",
        }
    return {
        "auto_append": False,
        "mode": "advisory",
        "reason": "通用 commonSearch 回查不保证所有表单可用，仅作为建议，不自动生成硬断言。",
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
        "严格回查必须在独立查询 grid 的指定字段中精确命中；保存响应中的字段回显、HTTP 200 和成功提示都不能算入库。",
        "回查失败不能直接改 save.post_data，应先检查 pageId 链路、变量覆盖和业务键是否被用户修改。",
    ]


def build_readback_explanation(
    case: dict[str, Any],
    *,
    readback_status: str,
    write_evidence_status: str,
    request_contract_failure_count: int = 0,
    response_contract_failure_count: int = 0,
    maintenance_expected_count: int = 0,
    maintenance_matched_count: int = 0,
) -> dict[str, Any]:
    """Explain write verification in user decision language.

    This deliberately separates request/write evidence from independent
    persistence evidence so a successful save response cannot be presented as
    a verified database write.
    """
    confirmed: list[str] = []
    if write_evidence_status != "missing":
        confirmed.append("保存/提交写入步骤已执行并返回响应")
    if request_contract_failure_count == 0 and response_contract_failure_count == 0:
        confirmed.append("关键请求与响应的稳定语义未发现差异")
    if maintenance_expected_count and maintenance_expected_count == maintenance_matched_count:
        confirmed.append(f"用户维护值已进入最终请求（{maintenance_matched_count}/{maintenance_expected_count}）")
    if readback_status == "verified":
        confirmed.append("目标环境独立只读查询已按本次业务键精确命中")
        return {
            "status": "verified",
            "confirmed": confirmed,
            "unconfirmed": [],
            "reason": "已有独立业务键回查证据。",
            "next_action": "",
        }

    plan = build_readback_plan(case)
    plans = plan.get("plans") or []
    policies = [item.get("assertion_policy") or {} for item in plans if isinstance(item, dict)]
    strategies = [item.get("strategy") or {} for item in plans if isinstance(item, dict)]
    has_strict = any(policy.get("auto_append") for policy in policies)
    has_recorded_candidate = any(
        strategy.get("source") == "recorded_har_query"
        and policy.get("mode") == "candidate"
        for strategy, policy in zip(strategies, policies)
    )

    if has_strict:
        reason = "已存在表单专用安全回查策略，但本次 YAML 未执行或未命中该回查。"
        next_action = "重新生成包含严格回查断言的 YAML 并执行；未命中时检查目标环境数据可见范围。"
    elif has_recorded_candidate:
        reason = "HAR 有保存后查询，但其列表 pageId/组织上下文无法在新会话中稳定重建，不能自动认定入库。"
        next_action = "在目标环境业务列表按本次编码、名称或单号人工查询；确认稳定入口后再沉淀表单专用回查。"
    elif plan.get("status") == "ready":
        reason = "已识别业务键，但尚无经过真实环境验证的表单专用只读查询，通用 commonSearch 可能误查旧数据。"
        next_action = "在目标环境业务列表按本次业务键人工查询；后续录制时保留保存后的列表查询链路。"
    else:
        reason = str(plan.get("reason") or "当前没有可安全自动执行的入库回查。")
        next_action = "人工确认目标环境中的业务结果，并补充可唯一定位本次数据的业务键或只读查询入口。"

    return {
        "status": readback_status or "not_supported",
        "confirmed": confirmed,
        "unconfirmed": ["目标环境中是否真实存在本次运行创建或更新的数据"],
        "reason": reason,
        "next_action": next_action,
    }


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
                "write_step_id": str(meta.get("write_step_id") or ""),
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
                "write_step_id": "",
            })
    return keys


def _find_recorded_readback_step(
    case: dict[str, Any],
    *,
    value_ref: str,
    write_step_id: str = "",
    preferred_field_key: str = "",
) -> dict[str, str]:
    """Find a recorded readonly query that consumes the current run value."""
    if not value_ref:
        return {}
    steps = case.get("steps") or []
    write_index = -1
    if write_step_id:
        write_index = next(
            (index for index, step in enumerate(steps) if step.get("id") == write_step_id),
            -1,
        )

    def inspect_filters(node: Any) -> list[str]:
        fields: list[str] = []
        if isinstance(node, dict):
            values = node.get("Value")
            names = node.get("FieldName")
            values_text = json.dumps(values, ensure_ascii=False, default=str)
            if value_ref in values_text:
                if isinstance(names, list):
                    fields.extend(str(name) for name in names if name)
                elif names:
                    fields.append(str(names))
            for child in node.values():
                fields.extend(inspect_filters(child))
        elif isinstance(node, list):
            for child in node:
                fields.extend(inspect_filters(child))
        return fields

    def inspect_all_filter_fields(node: Any) -> list[str]:
        fields: list[str] = []
        if isinstance(node, dict):
            names = node.get("FieldName")
            if isinstance(names, list):
                fields.extend(str(name) for name in names if name)
            elif names:
                fields.append(str(names))
            for child in node.values():
                fields.extend(inspect_all_filter_fields(child))
        elif isinstance(node, list):
            for child in node:
                fields.extend(inspect_all_filter_fields(child))
        return fields

    def choose_grid_field(query_field: str, schemas: list[Any]) -> str:
        columns = [
            str(column)
            for schema in schemas
            if isinstance(schema, dict)
            for column in (schema.get("required_columns") or [])
            if column
        ]
        if not columns:
            return query_field
        query_lower = query_field.lower()
        preferred_lower = preferred_field_key.lower()

        def normalized_field(value: str) -> str:
            return "".join(ch for ch in value.lower() if ch.isalnum())

        query_normalized = normalized_field(query_lower)
        preferred_normalized = normalized_field(preferred_lower)
        for candidate in columns:
            candidate_normalized = normalized_field(candidate)
            if candidate_normalized == query_normalized:
                return candidate
        for candidate in columns:
            candidate_normalized = normalized_field(candidate)
            if candidate_normalized == preferred_normalized:
                return candidate

        semantic_leaf = (
            preferred_lower.rsplit(".", 1)[-1].rsplit("_", 1)[-1]
            or query_lower.rsplit(".", 1)[-1].rsplit("_", 1)[-1]
        )
        for candidate in columns:
            if candidate.lower() in {preferred_lower, query_lower}:
                return candidate
        if semantic_leaf == "name":
            return next(
                (
                    candidate for candidate in columns
                    if candidate.lower().rsplit(".", 1)[-1].rsplit("_", 1)[-1] == "name"
                ),
                query_field,
            )
        if semantic_leaf in {"number", "code", "empnumber"}:
            return next(
                (
                    candidate for candidate in columns
                    if any(
                        token in candidate.lower().rsplit(".", 1)[-1]
                        for token in ("number", "code")
                    )
                ),
                query_field,
            )
        return next(
            (
                candidate for candidate in columns
                if semantic_leaf and semantic_leaf in candidate.lower()
            ),
            query_field,
        )

    for index, step in enumerate(steps):
        if index <= write_index:
            continue
        ac = str(step.get("ac") or "").lower()
        method = str(step.get("method") or "").lower()
        if ac not in {"commonsearch", "query"} and method not in {"commonsearch", "query"}:
            continue
        fields = inspect_filters(step.get("args"))
        value_rebind = False
        if not fields:
            fields = inspect_all_filter_fields(step.get("args"))
            value_rebind = True
        if not fields:
            continue
        preferred_lower = preferred_field_key.lower()
        preferred_leaf = preferred_lower.rsplit(".", 1)[-1].rsplit("_", 1)[-1]
        if preferred_leaf:
            if preferred_leaf == "name":
                preferred = next(
                    (
                        field for field in fields
                        if field.lower().rsplit(".", 1)[-1].endswith("name")
                        and not field.lower().rsplit(".", 1)[-1].endswith("number")
                    ),
                    "",
                )
            elif preferred_leaf in {"number", "code", "empnumber"}:
                preferred = next(
                    (
                        field for field in fields
                        if any(
                            token in field.lower().rsplit(".", 1)[-1]
                            for token in ("number", "code")
                        )
                    ),
                    "",
                )
            else:
                preferred = next(
                    (field for field in fields if preferred_leaf in field.lower()),
                    "",
                )
            if preferred:
                fields = [preferred, *[field for field in fields if field != preferred]]
            elif value_rebind:
                continue
        elif value_rebind:
            continue
        response_contract = step.get("expected_response_signature") or {}
        grid_schemas = response_contract.get("required_grid_schemas") or []
        assertion_field = choose_grid_field(fields[0], grid_schemas)
        grid_key = next(
            (
                str(schema.get("control") or "")
                for schema in grid_schemas
                if isinstance(schema, dict) and schema.get("control")
            ),
            "billlistap",
        )
        return {
            "step": str(step.get("id") or ""),
            "form_id": str(step.get("form_id") or case.get("main_form_id") or ""),
            "app_id": str(step.get("app_id") or _guess_app_id_from_form(str(step.get("form_id") or ""))),
            "field_key": assertion_field,
            "query_field_key": fields[0],
            "query_value_ref": value_ref if value_rebind else "",
            "grid_key": grid_key,
            "pageid_source_kind": str(step.get("recorded_pageid_source_kind") or ""),
            "pageid_type": str(step.get("recorded_pageid_type") or ""),
        }
    return {}


def _find_runtime_billno_readback_step(case: dict[str, Any]) -> dict[str, str]:
    """Prefer a final recorded business-list query fed by runtime billno."""
    has_prior_write = False
    candidates: list[dict[str, str]] = []

    def queries_billno(node: Any) -> bool:
        if isinstance(node, dict):
            names = node.get("FieldName")
            if isinstance(names, list) and "billno" in names:
                return True
            if names == "billno":
                return True
            return any(queries_billno(value) for value in node.values())
        if isinstance(node, list):
            return any(queries_billno(value) for value in node)
        return False

    for step in case.get("steps") or []:
        ac = str(step.get("ac") or "").lower()
        key = str(step.get("key") or "").lower()
        if ac in {"save", "submit", "saveandeffect", "submitandeffect"} or any(
            token in key for token in ("save", "submit")
        ):
            has_prior_write = True
        if not has_prior_write:
            continue
        method = str(step.get("method") or "").lower()
        form_id = str(step.get("form_id") or "")
        if form_id == "wf_task":
            continue
        if ac != "commonsearch" and method != "commonsearch":
            continue
        if not queries_billno(step.get("args")):
            continue
        response_contract = step.get("expected_response_signature") or {}
        grid_schemas = response_contract.get("required_grid_schemas") or []
        grid_key = next(
            (
                str(schema.get("control") or "")
                for schema in grid_schemas
                if isinstance(schema, dict) and schema.get("control")
            ),
            "billlistap",
        )
        candidates.append({
            "step": str(step.get("id") or ""),
            "form_id": form_id,
            "app_id": str(step.get("app_id") or _guess_app_id_from_form(form_id)),
            "grid_key": grid_key,
        })
    return candidates[-1] if candidates else {}


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
    report = {
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
    report["experience_candidate"] = build_experience_candidate(
        scenario,
        case=case or {},
        har_probe=har_probe or {},
        smoke_evidence=smoke_evidence or {},
        scenario_report=report,
    )
    return report


def build_experience_candidate(
    scenario: dict[str, Any],
    *,
    case: dict[str, Any] | None = None,
    har_probe: dict[str, Any] | None = None,
    smoke_evidence: dict[str, Any] | None = None,
    scenario_report: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """Build a value-safe catalog candidate from one closed-loop attempt.

    This does not update the catalog. It gives reviewers a sanitized patch-like
    object containing only structural facts and reusable lessons, so successful
    or failed samples can become future matching experience without carrying
    raw HAR bodies, cookies, tokens or business values.
    """
    case = case or {}
    har_probe = har_probe or {}
    smoke_summary = (smoke_evidence or {}).get("summary") or {}
    report = scenario_report or {}
    verification = report.get("write_verification") or infer_write_verification_strategy(case, smoke_summary)
    failure = report.get("failure_or_gap") or classify_pipeline_outcome(
        case=case,
        smoke_summary=smoke_summary,
        har_probe=har_probe,
    )
    risk_codes = [str(item.get("code") or "") for item in har_probe.get("risks") or [] if item.get("code")]
    lesson_codes = [str(item.get("code") or "") for item in har_probe.get("lessons") or [] if item.get("code")]
    forms = sorted(_query_form_ids(case, har_probe) or {str(item) for item in scenario.get("form_ids") or [] if item})
    apps = sorted(_query_app_ids(case, har_probe) or {_guess_app_id_from_form(form_id) for form_id in forms})
    feature_tags = sorted(_query_feature_tags(har_probe) | _scenario_lesson_tags(scenario))
    status = _experience_candidate_status(
        smoke_summary=smoke_summary,
        verification=verification,
        failure=failure,
        risk_codes=risk_codes,
    )
    lessons = _experience_candidate_lessons(
        forms=forms,
        feature_tags=feature_tags,
        risk_codes=risk_codes,
        lesson_codes=lesson_codes,
        verification=verification,
        failure=failure,
    )
    catalog_status = "closed_write_passed" if status == "ready" else f"candidate_{status}"
    return {
        "schema_version": 1,
        "status": status,
        "reason": _experience_candidate_reason(status, verification=verification, failure=failure, risk_codes=risk_codes),
        "scenario_id": scenario.get("id", ""),
        "app_label": scenario.get("app_label", ""),
        "menu_label": scenario.get("menu_label", ""),
        "env": scenario.get("env", ""),
        "forms": forms,
        "apps": apps,
        "feature_tags": feature_tags,
        "risk_codes": risk_codes,
        "lesson_codes": lesson_codes,
        "write_verification_status": verification.get("status", "not_checked"),
        "failure_category": failure.get("category", ""),
        "reusable_lessons": lessons,
        "catalog_patch": {
            "id": scenario.get("id", ""),
            "app_label": scenario.get("app_label", ""),
            "menu_label": scenario.get("menu_label", ""),
            "form_ids": forms,
            "status": catalog_status,
            "env": scenario.get("env", ""),
            "case_file": scenario.get("case_file", ""),
            "lessons": lessons,
        },
        "next_actions": _experience_candidate_next_actions(status),
        "value_safety": {
            "raw_har_included": False,
            "request_bodies_included": False,
            "auth_headers_included": False,
            "business_values_included": False,
        },
        "guardrails": [
            "这是经验库候选，不会自动写入 catalog；合入前需人工确认脱敏和代表性。",
            "只能沉淀结构经验：form_id/app_id/pageId/组件/断言策略，不记录真实业务值。",
            "若 status 不是 ready，不能加入 closed_write_passed；应先修复或补入库回查。",
        ],
    }


def _experience_candidate_status(
    *,
    smoke_summary: dict[str, Any],
    verification: dict[str, Any],
    failure: dict[str, Any],
    risk_codes: list[str],
) -> str:
    if smoke_summary.get("passed") is False:
        return "repair_first"
    if smoke_summary.get("passed") is True:
        if verification.get("status") == "verified_by_response" and not risk_codes:
            return "ready"
        if verification.get("status") == "verified_by_response":
            return "review"
        return "needs_readback"
    if failure.get("category") and failure.get("category") != "pipeline_evidence_missing":
        return "review"
    return "not_ready"


def _experience_candidate_reason(
    status: str,
    *,
    verification: dict[str, Any],
    failure: dict[str, Any],
    risk_codes: list[str],
) -> str:
    if status == "ready":
        return "执行和入库证据已闭环，且未发现 HAR 链路高风险，可人工确认后沉淀经验库。"
    if status == "needs_readback":
        return verification.get("reason") or "执行已通过，但仍缺少自动入库回查证据。"
    if status == "repair_first":
        return failure.get("root_cause") or "执行失败，需先修复后再沉淀经验。"
    if status == "review" and risk_codes:
        return f"HAR 链路存在需复核风险：{', '.join(risk_codes)}。"
    return "证据不足，需补 HAR 链路画像、YAML smoke 或入库验证。"


def _experience_candidate_next_actions(status: str) -> list[str]:
    return {
        "ready": [
            "人工确认 catalog_patch 脱敏且具代表性后，再加入 deep_chain_factory catalog。",
            "若样本可稳定复现，再考虑加入 HAR 回归 baseline。",
        ],
        "needs_readback": [
            "先按 write_verification.readback_plan 补 readback_by_business_key 断言。",
            "重跑 smoke，拿到 verified_by_response 或只读回查证据后再沉淀。",
        ],
        "repair_first": [
            "按 failure_analysis.diagnosis_priority 或 failure_or_gap 推荐动作修复。",
            "修复后重跑 run-scenario；不能加入 closed_write_passed，也不要把失败样本标为已闭环。",
        ],
        "review": [
            "复核 HAR pageId 风险和组件链路，确认不是侥幸通过。",
            "必要时补单测或 HAR 回归再沉淀。",
        ],
        "not_ready": [
            "补充 HAR、YAML 或 smoke 证据。",
            "不要把证据不足样本加入经验库。",
        ],
    }.get(status, ["人工确认下一步。"])


def _experience_candidate_lessons(
    *,
    forms: list[str],
    feature_tags: list[str],
    risk_codes: list[str],
    lesson_codes: list[str],
    verification: dict[str, Any],
    failure: dict[str, Any],
) -> list[str]:
    lessons: list[str] = []
    if forms:
        lessons.append(f"涉及表单：{', '.join(forms[:5])}。")
    if "l2_l3_switch" in feature_tags or "write_anchor" in feature_tags:
        lessons.append("菜单/列表/工具栏动作优先保留 L2；showForm/open_form 后字段维护、保存和提交使用 L3。")
    if "lookup_prefetch" in feature_tags or "f7_selector" in feature_tags:
        lessons.append("存在 lookup/F7 预热或选择链路；候选值应暴露为可维护环境字段，并在执行时解析内部 id。")
    if "newentry_dialog" in feature_tags:
        lessons.append("存在 newentry/子弹窗/明细链路；必须确认确定响应或主保存响应包含明细回填。")
    if "showform_alias" in feature_tags:
        lessons.append("存在 showForm/billFormId 或列表别名，需确认业务 form 与列表 L2 绑定正确。")
    if "default_context" in feature_tags:
        lessons.append("存在 loadData 默认上下文；优先沉淀为环境字段或模型上下文，不硬补 save.post_data。")
    if risk_codes:
        lessons.append(f"HAR 链路风险需复核：{', '.join(risk_codes[:5])}。")
    if lesson_codes:
        lessons.append(f"HAR 画像经验码：{', '.join(lesson_codes[:5])}。")
    if verification.get("readback_plan", {}).get("status") == "ready":
        plans = verification.get("readback_plan", {}).get("plans") or []
        filters = [
            str((plan.get("preferred_filter") or {}).get("field_key") or "")
            for plan in plans
            if (plan.get("preferred_filter") or {}).get("field_key")
        ]
        if filters:
            lessons.append(f"入库回查优先业务键：{', '.join(filters[:4])}。")
    if failure.get("category") and failure.get("category") != "closed_or_ready":
        lessons.append(f"失败/缺口分类：{failure.get('category')}，后续按诊断优先级修复。")
    return _dedupe_preserve_order(lessons)[:8]


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
