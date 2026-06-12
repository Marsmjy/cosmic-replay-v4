import json

from lib.deep_chain_pipeline import (
    build_auto_pipeline_report,
    build_experience_candidate,
    build_readback_explanation,
    build_readback_plan,
    build_report_from_paths,
    build_sample_expansion_plan,
    classify_pipeline_outcome,
    infer_write_verification_strategy,
    load_catalog,
    load_yaml_case,
    match_experience_catalog,
    summarize_progress,
)


def test_deep_chain_pipeline_progress_reports_current_stage():
    catalog = load_catalog()
    progress = summarize_progress(catalog)

    assert progress["target_cloud"] == "薪酬福利云"
    assert progress["closed_write_passed"] >= 7
    assert progress["blocked"] == 0
    assert progress["current_phase"] == "stage_2_auto_pipeline_and_readback"
    assert progress["next_focus"][0]["stage"] == "readonly_or_not_writable"
    assert progress["sample_expansion"]["next_batch"][0]["allowed_level"] == "L0"


def test_sample_expansion_plan_sorts_safe_next_actions_and_keeps_guardrails():
    catalog = {
        "target_cloud": "薪酬福利云",
        "scenarios": [
            {
                "id": "closed",
                "app_label": "薪资核算",
                "menu_label": "薪酬项目",
                "status": "closed_write_passed",
            },
            {
                "id": "blocked",
                "app_label": "薪资核算",
                "menu_label": "复杂子窗",
                "status": "blocked_missing_component",
            },
            {
                "id": "ready",
                "app_label": "薪资核算",
                "menu_label": "待 smoke",
                "status": "draft",
                "case_file": "cases/demo.yaml",
            },
            {
                "id": "har",
                "app_label": "中国社保",
                "menu_label": "已录 HAR",
                "status": "draft",
                "latest_local_har": "tmp/playwright_hars/demo.har",
            },
        ],
    }

    plan = build_sample_expansion_plan(catalog)

    assert plan["reference_closed_samples"] == 1
    assert [item["scenario_id"] for item in plan["next_batch"][:3]] == ["blocked", "ready", "har"]
    assert plan["next_batch"][0]["allowed_level"] == "repair_first"
    assert plan["next_batch"][1]["required_confirmation"] == "YES_GENERATE_TEST_DATA"
    assert "--confirm-write YES_GENERATE_TEST_DATA" in plan["next_batch"][1]["command_hint"]
    assert any("不能提交 Git" in guard for guard in plan["guardrails"])


def test_write_verification_strategy_prefers_business_key_readback_for_success_only():
    case = load_yaml_case("tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml")
    smoke_summary = {
        "passed": True,
        "write_events": [{"step_id": "click_bar_save", "response_tokens": ["保存成功"]}],
    }

    strategy = infer_write_verification_strategy(case, smoke_summary)

    assert strategy["status"] == "needs_readback"
    assert strategy["method"] == "business_key_query"
    assert {item["field_key"] for item in strategy["business_keys"]} >= {"number", "name"}
    assert strategy["readback_plan"]["status"] == "ready"
    assert strategy["readback_plan"]["plans"][0]["preferred_filter"]["field_key"] == "number"
    assert strategy["readback_plan"]["plans"][0]["preferred_filter"]["value_ref"] == "${vars.test_number}"
    assert strategy["readback_plan"]["plans"][0]["suggested_assertion"]["type"] == "readback_by_business_key"
    assert strategy["readback_plan"]["plans"][0]["suggested_assertion"]["value"] == "${vars.test_number}"


def test_write_verification_strategy_accepts_primary_key_response_tokens():
    case = load_yaml_case("tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml")
    smoke_summary = {
        "passed": True,
        "write_events": [{"step_id": "click_bar_save", "response_tokens": ["pkvalue"]}],
    }

    strategy = infer_write_verification_strategy(case, smoke_summary)

    assert strategy["status"] == "verified_by_response"
    assert strategy["method"] == "primary_key_or_operation_result"
    assert strategy["readback_plan"]["status"] == "ready"


def test_build_readback_plan_groups_business_keys_by_form():
    case = {
        "main_form_id": "hpdi_bizdatabillnewentry",
        "vars": {
            "test_description": "CRPLY_WRITE_${timestamp}",
            "test_name": "CRPLY_NAME_${timestamp}",
        },
        "vars_meta": {
            "test_description": {
                "field_key": "description",
                "form_id": "hpdi_bizdatabill",
            },
            "test_name": {
                "field_key": "name",
                "form_id": "hpdi_bizdatabillnewentry",
            },
        },
    }

    plan = build_readback_plan(case)

    assert plan["status"] == "ready"
    form_ids = {item["form_id"] for item in plan["plans"]}
    assert "hpdi_bizdatabill" in form_ids
    main_plan = next(item for item in plan["plans"] if item["form_id"] == "hpdi_bizdatabill")
    assert main_plan["preferred_filter"]["field_key"] == "description"
    assert main_plan["preferred_filter"]["value_ref"] == "${vars.test_description}"
    assert main_plan["app_id"] == "hpdi"
    assert main_plan["strategy"]["strategy_id"] == "ua_submit_business_key"
    assert main_plan["strategy"]["source"] == "strategy_library"
    assert main_plan["assertion_policy"]["auto_append"] is True
    assert main_plan["assertion_policy"]["mode"] == "strict"
    assert main_plan["suggested_assertion"] == {
        "type": "readback_by_business_key",
        "form_id": "hpdi_bizdatabill",
        "app_id": "hpdi",
        "field_key": "description",
        "value": "${vars.test_description}",
        "match_mode": "grid_field_exact",
    }
    assert any("不允许新增、保存" in guard for guard in plan["guardrails"])


def test_readback_plan_uses_generic_strategy_for_unknown_forms():
    case = {
        "main_form_id": "custom_form",
        "vars": {"test_number": "CRPLY_${rand:4}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "custom_form",
            }
        },
    }

    plan = build_readback_plan(case)

    assert plan["plans"][0]["strategy"]["strategy_id"] == "generic_business_key"
    assert plan["plans"][0]["strategy"]["manual_fallback"]
    assert plan["plans"][0]["assertion_policy"]["auto_append"] is False
    assert plan["plans"][0]["assertion_policy"]["mode"] == "advisory"


def test_readback_plan_uses_fresh_menu_refresh_for_hcdm_salary_apply():
    case = {
        "main_form_id": "khr_hcdm_fapplybill",
        "vars": {"test_name": "自动化${rand:4}"},
        "vars_meta": {
            "test_name": {
                "field_key": "name",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
            }
        },
    }

    plan = build_readback_plan(case)

    item = plan["plans"][0]
    assert item["strategy"]["strategy_id"] == "hcdm_salary_adjust_apply_menu_refresh"
    assert item["strategy"]["method"] == "fresh_menu_refresh"
    assert item["app_id"] == "hcdm"
    assert item["assertion_policy"]["auto_append"] is True
    assert item["suggested_assertion"]["strategy"] == "fresh_menu_refresh"
    assert item["suggested_assertion"]["menu_id"] == "2371045759278662656"
    assert item["suggested_assertion"]["field_key"] == "khr_name"


def test_readback_plan_prefers_recorded_post_write_query_using_runtime_var():
    case = {
        "main_form_id": "hbss_nationality",
        "vars": {"test_number": "AUTO${rand:4}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "write_step_id": "save_record",
            }
        },
        "steps": [
            {
                "id": "save_record",
                "type": "invoke",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "ac": "save",
            },
            {
                "id": "search_after_save",
                "type": "invoke",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "ac": "commonSearch",
                "args": [[{
                    "FieldName": ["number"],
                    "Value": ["${vars.test_number}"],
                }]],
                "expected_response_signature": {
                    "grid": {
                        "controls": ["billlistap"],
                        "stable_fields": ["number"],
                    }
                },
            },
        ],
    }

    plan = build_readback_plan(case)
    item = plan["plans"][0]

    assert item["strategy"]["source"] == "recorded_har_query"
    assert item["strategy"]["recorded_step"] == "search_after_save"
    assert item["suggested_assertion"]["step"] == "search_after_save"
    assert item["suggested_assertion"]["field_key"] == "number"
    assert item["suggested_assertion"]["match_mode"] == "grid_field_exact"
    assert item["assertion_policy"]["auto_append"] is True


def test_readback_plan_matches_name_variable_to_name_query_field():
    case = {
        "main_form_id": "hspm_assignmentlist",
        "vars": {
            "test_name": "自动化${rand:4}",
            "test_number": "EMP-${rand:6}",
        },
        "vars_meta": {
            "test_name": {
                "field_key": "ba_em_name",
                "form_id": "hspm_assignmentlist",
                "app_id": "hspm",
                "write_step_id": "confirm_onboard",
            },
            "test_number": {
                "field_key": "ba_em_empnumber",
                "form_id": "hspm_assignmentlist",
                "app_id": "hspm",
                "write_step_id": "confirm_onboard",
            },
        },
        "steps": [
            {"id": "confirm_onboard", "type": "invoke", "ac": "save"},
            {
                "id": "search_assignment",
                "type": "invoke",
                "form_id": "hspm_assignmentlist",
                "app_id": "hspm",
                "ac": "commonSearch",
                "args": [[{
                    "FieldName": [
                        "hrpi_employee.empnumber",
                        "hrpi_employee.name",
                    ],
                    "Value": ["${vars.test_name}"],
                }]],
                "expected_response_signature": {
                    "required_grid_schemas": [{
                        "control": "billlistap",
                        "required_columns": ["name", "number"],
                    }]
                },
            },
        ],
    }

    item = build_readback_plan(case)["plans"][0]

    assert item["strategy"]["recorded_step"] == "search_assignment"
    assert item["suggested_assertion"]["field_key"] == "number"
    assert item["suggested_assertion"]["value"] == "${vars.test_number}"
    assert item["suggested_assertion"]["query_field_key"] == "hrpi_employee.empnumber"
    assert item["suggested_assertion"]["query_value_ref"] == "${vars.test_number}"
    assert item["suggested_assertion"]["retry_until_found"] is True


def test_readback_plan_rebuilds_context_for_response_pageid_query():
    case = {
        "main_form_id": "hbss_nationality",
        "vars": {"test_number": "AUTO${rand:4}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "write_step_id": "save_record",
            }
        },
        "steps": [
            {"id": "save_record", "type": "invoke", "ac": "save"},
            {
                "id": "search_after_save",
                "type": "invoke",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "ac": "commonSearch",
                "args": [[{
                    "FieldName": ["number"],
                    "Value": ["${vars.test_number}"],
                }]],
                "recorded_pageid_source_kind": "responsePageId",
            },
        ],
    }

    item = build_readback_plan(case)["plans"][0]

    assert item["strategy"]["method"] == "fresh_recorded_navigation_query"
    assert item["suggested_assertion"]["strategy"] == "fresh_recorded_context"
    assert item["suggested_assertion"]["query_step"] == "search_after_save"
    assert "step" not in item["suggested_assertion"]
    assert item["assertion_policy"]["auto_append"] is False
    assert item["assertion_policy"]["mode"] == "candidate"


def test_readback_explanation_keeps_unverified_recorded_context_honest():
    case = {
        "main_form_id": "hbss_nationality",
        "vars": {"test_number": "AUTO${rand:4}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "write_step_id": "save_record",
            }
        },
        "steps": [
            {"id": "save_record", "type": "invoke", "ac": "save"},
            {
                "id": "search_after_save",
                "type": "invoke",
                "form_id": "hbss_nationality",
                "app_id": "hbss",
                "ac": "commonSearch",
                "args": [[{
                    "FieldName": ["number"],
                    "Value": ["${vars.test_number}"],
                }]],
                "recorded_pageid_source_kind": "responsePageId",
            },
        ],
    }

    explanation = build_readback_explanation(
        case,
        readback_status="not_supported",
        write_evidence_status="verified_by_response",
        maintenance_expected_count=2,
        maintenance_matched_count=2,
    )

    assert explanation["status"] == "not_supported"
    assert any("维护值" in item for item in explanation["confirmed"])
    assert explanation["unconfirmed"]
    assert "pageId" in explanation["reason"]
    assert "人工查询" in explanation["next_action"]


def test_readback_explanation_marks_independent_query_verified():
    explanation = build_readback_explanation(
        {},
        readback_status="verified",
        write_evidence_status="response_present",
    )

    assert explanation["status"] == "verified"
    assert explanation["unconfirmed"] == []
    assert any("独立只读查询" in item for item in explanation["confirmed"])


def test_readback_plan_prefers_final_runtime_billno_query():
    case = {
        "main_form_id": "khr_hcdm_fapplybill",
        "steps": [
            {
                "id": "submit_bill",
                "type": "invoke",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
                "ac": "submit",
            },
            {
                "id": "search_final_bill",
                "type": "invoke",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
                "ac": "commonSearch",
                "args": [[{
                    "FieldName": ["billno"],
                    "Value": ["RECORDED-BILLNO"],
                }]],
                "expected_response_signature": {
                    "required_grid_schemas": [{
                        "control": "billlistap",
                        "required_columns": ["billno"],
                    }]
                },
            },
        ],
    }

    plan = build_readback_plan(case)
    item = plan["plans"][0]

    assert plan["method"] == "runtime_billno_query"
    assert item["strategy"]["source"] == "recorded_runtime_query"
    assert item["suggested_assertion"]["step"] == "search_final_bill"
    assert item["suggested_assertion"]["value_from_runtime"] == "billno"
    assert item["assertion_policy"]["auto_append"] is True


def test_pipeline_failure_classification_uses_existing_failure_analysis_rules():
    case = {"main_form_id": "demo_form"}
    smoke_summary = {
        "passed": False,
        "failed_steps": [{"id": "save", "error": "页面未初始化或者已经过期 pageId"}],
    }

    outcome = classify_pipeline_outcome(case=case, smoke_summary=smoke_summary)

    assert outcome["category"] == "pageid_context"
    assert outcome["severity"] == "high"


def test_pipeline_failure_classification_detects_rule_group_filter_gap():
    case = {"main_form_id": "hsas_payrollscene"}
    smoke_summary = {
        "passed": False,
        "failed_steps": [{
            "id": "click_bar_save",
            "error": "规则分组“默认规则”中，常用筛选不允许为空，请至少填写一行数据。",
        }],
    }

    outcome = classify_pipeline_outcome(case=case, smoke_summary=smoke_summary)

    assert outcome["category"] == "component_rule_group_filter_missing"
    assert outcome["severity"] == "high"
    assert any("常用筛选" in action for action in outcome["recommended_actions"])


def test_pipeline_report_without_smoke_evidence_is_not_marked_closed():
    case = load_yaml_case("tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml")

    strategy = infer_write_verification_strategy(case)
    outcome = classify_pipeline_outcome(case=case)

    assert strategy["status"] == "not_checked"
    assert strategy["method"] == "missing_smoke_evidence"
    assert outcome["category"] == "pipeline_evidence_missing"


def test_scenario_report_is_value_safe_and_highlights_verification_gap(tmp_path):
    smoke_path = tmp_path / "smoke.json"
    smoke_path.write_text(
        json.dumps({
            "summary": {
                "passed": True,
                "write_events": [{"step_id": "click_bar_save", "response_tokens": ["保存成功"]}],
            }
        }, ensure_ascii=False),
        encoding="utf-8",
    )

    report = build_report_from_paths(
        scenario_id="salary_item_new_validation",
        smoke_evidence_path=smoke_path,
    )

    assert report["scenario"]["stage"] == "closed_write_passed"
    assert report["write_verification"]["status"] == "needs_readback"
    assert report["failure_or_gap"]["category"] == "write_verification_gap"
    assert report["experience_matches"]["status"] == "matched"
    assert report["experience_candidate"]["status"] == "needs_readback"
    assert report["experience_candidate"]["catalog_patch"]["status"] == "candidate_needs_readback"
    raw = json.dumps(report, ensure_ascii=False)
    assert "password" not in raw.lower()
    assert "cookie" not in raw.lower()


def test_experience_candidate_ready_is_value_safe_and_builds_catalog_patch():
    case = {
        "main_form_id": "hsbs_salaryitem",
        "vars": {"test_number": "CRPLY_${rand:4}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "hsbs_salaryitem",
                "app_id": "hsbs",
            }
        },
        "steps": [
            {"form_id": "hsbs_salaryitem", "type": "menuItemClick", "app_id": "hsbs"},
            {"form_id": "hsbs_salaryitem", "type": "invoke", "ac": "save"},
        ],
    }
    har_probe = {
        "summary": {
            "forms": ["hsbs_salaryitem"],
            "lookup_prefetch_count": 1,
            "showform_alias_count": 0,
            "write_anchor_count": 1,
            "default_context_count": 1,
        },
        "lessons": [{"code": "lookup_prefetch_before_pick"}],
        "risks": [],
    }
    smoke_evidence = {
        "summary": {
            "passed": True,
            "write_events": [{"step_id": "click_bar_save", "response_tokens": ["pkvalue"]}],
        }
    }

    candidate = build_experience_candidate(
        {
            "id": "salary_item_candidate",
            "app_label": "薪资核算",
            "menu_label": "薪酬项目",
            "env": "sit",
            "case_file": "cases/demo.yaml",
        },
        case=case,
        har_probe=har_probe,
        smoke_evidence=smoke_evidence,
    )

    assert candidate["status"] == "ready"
    assert candidate["catalog_patch"]["status"] == "closed_write_passed"
    assert candidate["catalog_patch"]["form_ids"] == ["hsbs_salaryitem"]
    assert "lookup_prefetch" in candidate["feature_tags"]
    assert any("L2" in lesson and "L3" in lesson for lesson in candidate["reusable_lessons"])
    raw = json.dumps(candidate, ensure_ascii=False)
    assert "CRPLY_" not in raw
    assert "cookie" not in raw.lower()


def test_experience_candidate_failed_sample_requires_repair_first():
    candidate = build_experience_candidate(
        {"id": "failed", "app_label": "薪资核算", "menu_label": "复杂链路"},
        case={"main_form_id": "hsas_payrollscene"},
        smoke_evidence={
            "summary": {
                "passed": False,
                "failed_steps": [{
                    "id": "click_bar_save",
                    "error": "规则分组常用筛选不允许为空，请至少填写一行数据。",
                }],
            }
        },
    )

    assert candidate["status"] == "repair_first"
    assert candidate["failure_category"] == "component_rule_group_filter_missing"
    assert candidate["catalog_patch"]["status"] == "candidate_repair_first"
    assert any("不能加入 closed_write_passed" in action for action in candidate["next_actions"])


def test_match_experience_catalog_prefers_closed_form_and_feature_match():
    catalog = load_catalog()
    case = {
        "main_form_id": "hsas_payrollscene",
        "steps": [
            {"form_id": "hsas_payrollscene", "type": "menuItemClick"},
            {"form_id": "hsas_salarycalcstyle", "type": "select_f7_list_row"},
        ],
    }
    har_probe = {
        "summary": {
            "forms": ["hsas_payrollscene", "hsas_salarycalcstyle"],
            "lookup_prefetch_count": 1,
            "showform_alias_count": 1,
            "write_anchor_count": 1,
            "default_context_count": 0,
        },
        "lessons": [{"code": "lookup_prefetch_before_pick"}],
        "risks": [],
    }

    result = match_experience_catalog(catalog, case=case, har_probe=har_probe)

    assert result["status"] == "matched"
    assert result["matches"][0]["scenario_id"] == "salary_calc_scene_rule_group_blocked"
    assert any("form_id 命中" in reason for reason in result["matches"][0]["matched_reasons"])
    assert any("硬补 save.post_data" in guard for guard in result["guardrails"])


def test_auto_pipeline_report_reuses_yaml_and_waits_for_smoke(tmp_path):
    report = build_auto_pipeline_report(
        scenario_id="salary_calc_scene_rule_group_blocked",
        case_path="tests/fixtures/deep_chain_factory/salary_calc_scene_protocol_save.yaml",
        output_dir=tmp_path,
    )

    assert report["pipeline"]["status"] == "yaml_ready_needs_smoke"
    assert report["pipeline"]["artifacts"]["case_generated"] is False
    assert report["pipeline"]["baseline_candidate"]["status"] == "not_ready"
    assert any("write smoke" in action for action in report["pipeline"]["next_actions"])
    assert report["scenario_report"]["experience_matches"]["matches"][0]["scenario_id"] == "salary_calc_scene_rule_group_blocked"


def test_auto_pipeline_report_marks_verified_smoke_as_baseline_candidate(tmp_path):
    smoke_path = tmp_path / "smoke.json"
    smoke_path.write_text(
        json.dumps({
            "summary": {
                "passed": True,
                "write_events": [{"step_id": "click_bar_save", "response_tokens": ["pkvalue"]}],
            }
        }, ensure_ascii=False),
        encoding="utf-8",
    )

    report = build_auto_pipeline_report(
        scenario_id="salary_item_new_validation",
        case_path="tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml",
        smoke_evidence_path=smoke_path,
        output_dir=tmp_path,
    )

    assert report["pipeline"]["status"] == "closed_verified"
    assert report["pipeline"]["baseline_candidate"]["status"] == "ready"
    assert report["scenario_report"]["write_verification"]["status"] == "verified_by_response"
