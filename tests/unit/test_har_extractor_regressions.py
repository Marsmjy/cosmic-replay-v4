import json
import sys
import urllib.parse
from pathlib import Path

import pytest
import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_extractor import (
    _append_readback_assertions,
    _append_recorded_default_pick_steps,
    _annotate_dynamic_query_row_selections,
    _annotate_env_field_sources,
    _annotate_repeated_menu_targets,
    _attach_pick_field_scopes,
    _build_preview_readback_plan,
    _build_preview_business_blocks,
    _build_preview_business_flow,
    _build_default_assertions,
    _build_validation_points,
    _apply_validation_points_to_assertions,
    _clean_display_label,
    collapse_repeated_polling_steps,
    _drop_locked_update_fields,
    _apply_user_pick_field_values_to_update_steps,
    _extract_entry_row_selector,
    _mark_recorded_business_validations,
    _scoped_pick_field_id,
    build_yaml_case,
    detect_var_placeholders,
    extract_steps,
    insert_workflow_task_wait_steps,
    lower_set_item_to_pick_basedata,
    merge_consecutive_update_values,
    preview_har,
    to_yaml,
)
from lib import kb_loader
from lib.response_signature import build_response_signature, specialize_response_signature


def test_to_yaml_keeps_multilang_numeric_values_as_strings():
    data = {
        "fields": {
            "posorientation": {
                "zh_CN": "11111",
                "zh_TW": "11111",
            }
        }
    }

    yaml_text = to_yaml(data)
    parsed = yaml.safe_load(yaml_text)

    assert '"11111"' in yaml_text
    assert parsed["fields"]["posorientation"]["zh_CN"] == "11111"
    assert isinstance(parsed["fields"]["posorientation"]["zh_CN"], str)


def test_to_yaml_keeps_leading_zero_business_codes_as_strings():
    data = {"pick_fields": {"pick_city_id": {"value_id": "00407", "value_code": "00407"}}}

    yaml_text = to_yaml(data)
    parsed = yaml.safe_load(yaml_text)

    assert '"00407"' in yaml_text
    assert parsed["pick_fields"]["pick_city_id"]["value_id"] == "00407"
    assert parsed["pick_fields"]["pick_city_id"]["value_code"] == "00407"


def test_workflow_task_entry_row_is_not_exposed_as_selector_pick_field():
    step = {
        "id": "entryRowClick_88",
        "type": "invoke",
        "form_id": "wf_task",
        "app_id": "bos",
        "ac": "entryRowClick",
        "key": "billlistap",
        "method": "entryRowClick",
        "post_data": [{
            "billlistap": {
                "fieldKey": "subject",
                "row": 0,
                "selRows": [0],
                "selDatas": [[
                    "2494949344130694145",
                    "DTX20260605332",
                    "请对定调薪申请单进行薪酬结构拆解（单据编号：DTX20260605332）",
                ]],
            }
        }, []],
    }

    assert _extract_entry_row_selector(step, {
        "parent_form_id": "khr_hcdm_proposalplan",
        "parent_field_key": "khr_zcurrencyfield",
        "label": "调薪后-币种",
    }) is None


def test_merge_update_values_preserves_repeated_field_toggles():
    def update_step(step_id: str, field: str, value: str) -> dict:
        return {
            "id": step_id,
            "type": "invoke",
            "form_id": "khr_hcdm_fapplybill",
            "app_id": "hcdm",
            "method": "updateValue",
            "key": "",
            "post_data": [{}, [{"k": field, "v": value, "r": -1}]],
        }

    merged = merge_consecutive_update_values([
        update_step("set_proposal_0", "khr_salaryproposal", "0"),
        update_step("set_scope_3", "khr_scope", "3"),
        update_step("set_proposal_1", "khr_salaryproposal", "1"),
        update_step("set_proposal_0_again", "khr_salaryproposal", "0"),
    ])

    assert [step["fields"] for step in merged] == [
        {"khr_salaryproposal": "0", "khr_scope": "3"},
        {"khr_salaryproposal": "1"},
        {"khr_salaryproposal": "0"},
    ]


def test_user_pick_override_rewrites_repeated_same_form_update_fields():
    steps = [
        {
            "id": "fill_khr_scope",
            "type": "update_fields",
            "form_id": "khr_hcdm_fapplybill",
            "fields": {"khr_scope": 1},
        },
        {
            "id": "fill_khr_zcurrency_etc",
            "type": "update_fields",
            "form_id": "khr_hcdm_fapplybill",
            "fields": {"khr_zcurrency": 1, "khr_scope": 3},
        },
        {
            "id": "fill_other_scope",
            "type": "update_fields",
            "form_id": "other_form",
            "fields": {"khr_scope": 3},
        },
    ]
    pick_fields = {
        "pick_khr_scope_id": {
            "field_key": "khr_scope",
            "form_id": "khr_hcdm_fapplybill",
            "source_step_id": "fill_khr_scope",
            "value_id": "1",
            "value_code": "1",
            "value_number": "1",
            "user_overridden": True,
            "resolve_by": "value_code",
        },
    }

    _apply_user_pick_field_values_to_update_steps(steps, pick_fields)

    assert steps[0]["fields"]["khr_scope"] == "1"
    assert steps[1]["fields"]["khr_scope"] == "1"
    assert steps[2]["fields"]["khr_scope"] == 3


def test_drop_locked_fields_keeps_recorded_basedata_pick():
    steps = [
        {
            "id": "pick_khr_proposer",
            "type": "pick_basedata",
            "form_id": "khr_hcdm_fapplybill",
            "field_key": "khr_proposer",
            "value_id": "2381390676873980001",
            "value_code": "00001",
            "_har_index": 64,
        }
    ]
    observations = {"locked_events": {62: {"khr_proposer"}}}

    assert _drop_locked_update_fields(steps, observations) == steps


def test_append_readback_assertions_is_opt_in_and_uses_business_key():
    case = {
        "main_form_id": "hsas_payrollscene",
        "vars": {"test_number": "CRPLY_${rand:6}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "hsas_payrollscene",
            }
        },
        "assertions": [{"type": "no_save_failure", "step": "click_bar_save"}],
    }

    plan = _append_readback_assertions(case)

    assert plan["status"] == "ready"
    assert case["assertions"][-1] == {
        "type": "readback_by_business_key",
        "form_id": "hsas_payrollscene",
        "app_id": "hsas",
        "field_key": "number",
        "value": "${vars.test_number}",
        "match_mode": "grid_field_exact",
    }


def test_append_readback_assertions_skips_generic_common_search_plan():
    case = {
        "main_form_id": "custom_org_reason",
        "vars": {"test_number": "CRPLY_${rand:6}"},
        "vars_meta": {
            "test_number": {
                "field_key": "number",
                "form_id": "custom_org_reason",
            }
        },
        "assertions": [{"type": "no_save_failure", "step": "click_bar_save"}],
    }

    plan = _append_readback_assertions(case)

    assert plan["status"] == "ready"
    assert plan["plans"][0]["assertion_policy"]["auto_append"] is False
    assert case["assertions"] == [{"type": "no_save_failure", "step": "click_bar_save"}]


def test_append_readback_assertions_includes_fresh_menu_strategy_for_hcdm_salary_apply():
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
        "assertions": [{"type": "no_save_failure", "step": "click_bar_save"}],
    }

    plan = _append_readback_assertions(case)

    assert plan["status"] == "ready"
    assert case["assertions"][-1] == {
        "type": "readback_by_business_key",
        "strategy": "fresh_menu_refresh",
        "menu_id": "2371045759278662656",
        "form_id": "khr_hcdm_fapplybill",
        "app_id": "hcdm",
        "field_key": "khr_name",
        "value": "${vars.test_name}",
        "match_mode": "grid_field_exact",
    }


def test_preview_readback_plan_uses_detected_var_metadata():
    plan = _build_preview_readback_plan(
        "hsas_payrollscene",
        [{
            "name": "test_name",
            "template": "CRPLY_NAME_${rand:6}",
            "field_key": "name",
            "form_id": "hsas_payrollscene",
        }],
    )

    assert plan["status"] == "ready"
    assert plan["plans"][0]["suggested_assertion"]["type"] == "readback_by_business_key"
    assert plan["plans"][0]["suggested_assertion"]["value"] == "${vars.test_name}"


def test_recorded_readback_maps_qualified_query_field_to_matching_grid_column():
    case = {
        "main_form_id": "hom_onbrdinfo",
        "vars": {"test_number": "AUTO001"},
        "vars_meta": {
            "test_number": {
                "field_key": "ba_em_empnumber",
                "form_id": "hom_onbrdinfo",
                "source_step_id": "fill_employee_number",
            },
        },
        "steps": [
            {
                "id": "click_confirm",
                "type": "invoke",
                "form_id": "hom_onbrdinfo",
                "app_id": "hom",
                "ac": "doConfirm",
                "method": "doConfirm",
            },
            {
                "id": "search_assignment",
                "type": "invoke",
                "form_id": "hspm_assignmentlist",
                "app_id": "hspm",
                "ac": "commonSearch",
                "method": "commonSearch",
                "args": [[{
                    "FieldName": [
                        "hrpi_employee.empnumber",
                        "hrpi_employee.name",
                    ],
                    "Value": ["recorded name"],
                }]],
                "expected_response_signature": {
                    "required_grid_schemas": [{
                        "control": "billlistap",
                        "required_columns": [
                            "number",
                            "hrpi_employee_empnumber",
                            "hrpi_employee_name",
                        ],
                    }],
                },
            },
        ],
    }

    plan = _append_readback_assertions(case)

    assert plan["status"] == "ready"
    assertion = case["assertions"][0]
    assert assertion["query_field_key"] == "hrpi_employee.empnumber"
    assert assertion["field_key"] == "hrpi_employee_empnumber"


def test_response_contract_keeps_grid_column_matching_qualified_query_field():
    signature = build_response_signature(
        [{
            "p": [{
                "data": {
                    "dataindex": {
                        "number": 0,
                        "hrpi_employee_empnumber": 1,
                        "hrpi_employee_name": 2,
                    },
                    "rows": [["assignment001", "employee001", "自动化"]],
                },
                "k": "billlistap",
            }],
            "a": "u",
        }],
        include_candidates=True,
    )
    specialized = specialize_response_signature(
        signature,
        {
            "type": "invoke",
            "form_id": "hspm_assignmentlist",
            "ac": "commonSearch",
            "method": "commonSearch",
            "args": [[{
                "FieldName": ["hrpi_employee.empnumber", "hrpi_employee.name"],
                "Value": ["employee001"],
            }]],
        },
        contract_level="business",
        anchor_reason="selector_data_source",
    )

    columns = specialized["required_grid_schemas"][0]["required_columns"]
    assert "hrpi_employee_empnumber" in columns
    assert "hrpi_employee_name" in columns


def test_env_field_source_annotation_explains_metadata_and_lookup_sources():
    pick_fields = {
        "pick_org_id": {
            "field_key": "org",
            "form_id": "demo_form",
            "auto_resolve": True,
            "resolve_by": "value_code",
        }
    }
    observations = {
        "response_values_by_form": {
            "demo_form": {
                "org": {"value_code": "ORG001", "value_name": "演示组织"},
            }
        },
        "response_internal_ids_by_form": {
            "demo_form": {"org": "1234567890"},
        },
        "combo_options_by_form": {},
        "labels_by_form": {"demo_form": {"org": "组织"}},
    }

    _annotate_env_field_sources(pick_fields, observations)

    meta = pick_fields["pick_org_id"]
    assert meta["source_type"] == "loadData_response"
    assert "list_dataindex" in meta["source_detail"]
    assert "auto_resolve:value_code" in meta["source_detail"]


def test_preview_business_blocks_group_vars_and_env_fields_by_form_action():
    blocks = _build_preview_business_blocks(
        [{
            "name": "test_name",
            "label": "名称",
            "field_key": "name",
            "form_id": "form_a",
            "form_label": "表单A",
            "group_key": "form_a:save",
            "group_label": "表单A / 保存",
            "write_step_id": "save_a",
        }],
        [{
            "id": "pick_org_id",
            "label": "组织",
            "field_key": "org",
            "form_id": "form_b",
            "form_label": "表单B",
            "group_key": "form_b:save",
            "group_label": "表单B / 保存",
            "source_type": "loadData_response",
        }],
    )

    assert [block["key"] for block in blocks] == ["form_a:save", "form_b:save"]
    assert blocks[0]["smart_var_count"] == 1
    assert blocks[1]["env_field_count"] == 1


def test_preview_business_flow_groups_steps_and_pageid_roles_by_business_anchor():
    steps = [
        {
            "id": "menu",
            "type": "invoke",
            "form_id": "bos_portal",
            "app_id": "bos",
            "ac": "menuItemClick",
            "method": "menuItemClick",
        },
        {
            "id": "load",
            "type": "invoke",
            "form_id": "demo_bill",
            "app_id": "demo",
            "ac": "loadData",
            "method": "loadData",
            "preserve_l2_page": True,
        },
        {
            "id": "fill",
            "type": "update_fields",
            "form_id": "demo_bill",
            "app_id": "demo",
            "fields": {"name": "${vars.test_name}"},
        },
        {
            "id": "save",
            "type": "invoke",
            "form_id": "demo_bill",
            "app_id": "demo",
            "ac": "save",
            "method": "save",
            "key": "bar_save",
            "description": "保存【demo bill】",
        },
    ]
    vars_meta = [{
        "name": "test_name",
        "group_key": "demo_bill:save",
        "group_label": "演示单据 / 保存",
    }]
    pick_fields = [{
        "id": "pick_org_id",
        "group_key": "demo_bill:save",
        "group_label": "演示单据 / 保存",
    }]

    flow = _build_preview_business_flow(steps, vars_meta, pick_fields, "demo_bill")
    by_key = {item["key"]: item for item in flow}

    assert "demo_bill:save" in by_key
    block = by_key["demo_bill:save"]
    assert block["step_count"] == 3
    assert block["input_step_count"] == 1
    assert block["write_step_count"] == 1
    assert block["smart_var_count"] == 1
    assert block["env_field_count"] == 1
    assert block["pageid_roles"]["L2"] == 1
    assert block["pageid_roles"]["L3"] == 2


def test_clean_display_label_hides_deprecated_suffix_for_user_facing_labels():
    assert _clean_display_label("入职日期_废弃") == "入职日期"
    assert _clean_display_label("入职日期（废弃）") == "入职日期"


def test_scoped_pick_field_id_keeps_base_key_until_cross_form_collision():
    existing = {}

    first = _scoped_pick_field_id(
        "pick_adminorg_id",
        existing,
        form_id="form_a",
        source_step_id="pick_a",
    )
    existing[first] = {"form_id": "form_a"}

    assert first == "pick_adminorg_id"
    assert _scoped_pick_field_id(
        "pick_adminorg_id",
        existing,
        form_id="form_a",
        source_step_id="pick_a_again",
    ) == ""
    assert _scoped_pick_field_id(
        "pick_adminorg_id",
        existing,
        form_id="form_b",
        source_step_id="pick_b",
    ) == "pick_adminorg_id__pick_b"


def test_attach_pick_field_scopes_uses_form_before_field_key_match():
    pick_fields = {
        "pick_adminorg_id__pick_b": {
            "field_key": "adminorg",
            "form_id": "form_b",
        }
    }
    steps = [
        {
            "id": "pick_a",
            "type": "pick_basedata",
            "form_id": "form_a",
            "field_key": "adminorg",
        },
        {
            "id": "pick_b",
            "type": "pick_basedata",
            "form_id": "form_b",
            "field_key": "adminorg",
        },
        {
            "id": "save_b",
            "type": "invoke",
            "form_id": "form_b",
            "ac": "save",
            "description": "保存【B表单】",
        },
    ]

    _attach_pick_field_scopes(pick_fields, steps)

    assert pick_fields["pick_adminorg_id__pick_b"]["source_step_id"] == "pick_b"
    assert pick_fields["pick_adminorg_id__pick_b"]["write_step_id"] == "save_b"


def test_recorded_default_pick_steps_inject_choice_form_org_before_direct_template_pick():
    steps = [
        {
            "id": "load_choice",
            "type": "invoke",
            "form_id": "hpdi_bizdatabillchoicetpl",
            "app_id": "hpdi",
            "ac": "loadData",
        },
        {
            "id": "pick_bizitemgroup",
            "type": "pick_basedata",
            "form_id": "hpdi_bizdatabillchoicetpl",
            "app_id": "hpdi",
            "field_key": "bizitemgroup",
            "value_id": "2365355356009289728",
        },
        {
            "id": "click_ok",
            "type": "invoke",
            "form_id": "hpdi_bizdatabillchoicetpl",
            "app_id": "hpdi",
            "ac": "click",
        },
    ]
    observations = {
        "response_values_by_form": {
            "hpdi_bizdatabillchoicetpl": {
                "org": {
                    "value_code": "JDGJJT",
                    "value_name": "金蝶国际软件集团有限公司",
                    "value_number": "JDGJJT",
                }
            }
        }
    }

    out = _append_recorded_default_pick_steps(
        steps,
        observations,
        main_form="hpdi_bizdatabillnewentry",
        app_id="hpdi",
    )

    assert any(
        step.get("form_id") == "hpdi_bizdatabillchoicetpl"
        and step.get("field_key") == "org"
        and step.get("_is_recorded_default")
        for step in out
    )
    assert [step["id"] for step in out[:3]] == [
        "load_choice",
        "pick_org_ctx",
        "pick_bizitemgroup",
    ]


def test_recorded_default_pick_steps_skip_choice_form_org_without_direct_template_pick():
    steps = [
        {
            "id": "load_choice",
            "type": "invoke",
            "form_id": "hpdi_bizdatabillchoicetpl",
            "app_id": "hpdi",
            "ac": "loadData",
        },
        {
            "id": "click_bizitemgroup",
            "type": "invoke",
            "form_id": "hpdi_bizdatabillchoicetpl",
            "app_id": "hpdi",
            "ac": "click",
            "key": "bizitemgroup",
        },
        {
            "id": "load_bizitemgroup",
            "type": "invoke",
            "form_id": "hsbs_bizitemgroup",
            "app_id": "hsbs",
            "ac": "loadData",
        },
    ]
    observations = {
        "response_values_by_form": {
            "hpdi_bizdatabillchoicetpl": {
                "org": {
                    "value_code": "JDGJJT",
                    "value_name": "金蝶国际软件集团有限公司",
                    "value_number": "JDGJJT",
                }
            }
        }
    }

    out = _append_recorded_default_pick_steps(
        steps,
        observations,
        main_form="hpdi_bizdatabillnewentry",
        app_id="hpdi",
    )

    assert not any(
        step.get("form_id") == "hpdi_bizdatabillchoicetpl"
        and step.get("field_key") == "org"
        and step.get("_is_recorded_default")
        for step in out
    )


def _batch_invoke_har_entry(
    *,
    form_id: str,
    app_id: str,
    ac: str,
    actions: list[dict],
    page_id: str = "",
    response: list | dict | None = None,
) -> dict:
    return {
        "request": {
            "method": "POST",
            "url": (
                "http://example.test/form/batchInvokeAction.do"
                f"?appId={app_id}&f={form_id}&ac={ac}"
            ),
            "postData": {
                "text": urllib.parse.urlencode({
                    "params": json.dumps(actions, ensure_ascii=False),
                    "pageId": page_id,
                })
            },
        },
        "response": {
            "content": {
                "text": json.dumps(response or [], ensure_ascii=False)
            }
        },
    }


def test_custom_event_showform_is_core_navigation_producer():
    har = {
        "log": {
            "entries": [
                _batch_invoke_har_entry(
                    form_id="hrbm_bmmanagement",
                    app_id="hrbm",
                    ac="customEvent",
                    page_id="1855928977336805376rootaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    actions=[{
                        "key": "",
                        "methodName": "customEvent",
                        "args": ["modelcustomcontrolap", "clickCardHandle", "{}"],
                        "postData": [],
                    }],
                    response=[{
                        "a": "showForm",
                        "p": [{
                            "formId": "hrbm_schedule_quest",
                            "pageId": "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                        }],
                    }],
                )
            ]
        }
    }

    steps = extract_steps(har)

    assert steps[0]["ac"] == "customEvent"
    assert steps[0]["_tier"] == "core"
    assert steps[0]["preserve_l2_page"] is True


def test_menu_reopen_response_pageid_is_retained_without_showform():
    page_id = "1443450410974114816root" + "a" * 32
    har = {
        "log": {
            "entries": [
                _batch_invoke_har_entry(
                    form_id="bos_portal_myapp_new",
                    app_id="bos",
                    ac="menuItemClick",
                    page_id="b" * 32,
                    actions=[{
                        "key": "appnavigationmenuap",
                        "methodName": "menuItemClick",
                        "args": [{"menuId": "1443450410974114816"}],
                        "postData": [{}, []],
                    }],
                    response=[{
                        "p": [{
                            "pageId": page_id,
                            "actions": [{
                                "a": "sendDynamicFormAction",
                                "p": [],
                            }],
                        }],
                    }],
                )
            ]
        }
    }

    steps = extract_steps(har)

    assert steps[0]["id"].startswith("menuItemClick")
    assert page_id in steps[0]["_resp_text"]


def test_repeated_menu_target_uses_following_l2_consumer():
    root = "a" * 32
    steps = [
        {
            "id": "menu_first",
            "type": "invoke",
            "ac": "menuItemClick",
            "args": [{"menuId": "100"}],
            "target_form": "first_list",
        },
        {
            "id": "menu_reopen",
            "type": "invoke",
            "ac": "menuItemClick",
            "args": [{"menuId": "200"}],
        },
        {
            "id": "select_tab",
            "type": "invoke",
            "ac": "selectTab",
            "form_id": "home_page",
            "_har_page_id": f"approot{root}",
        },
        {
            "id": "search_list",
            "type": "invoke",
            "ac": "commonSearch",
            "form_id": "target_list",
            "_har_page_id": f"200root{root}",
        },
    ]

    _annotate_repeated_menu_targets(steps, "detail_form")

    assert steps[1]["target_form"] == "target_list"
    assert steps[1]["navigation_form_id"] == "target_list"
    assert steps[1]["resolve_by"] == "menu_path_or_form"


def test_business_model_schedule_quest_har_keeps_wizard_context_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780638489_业务模型-添加一个全字段类型人员附表.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="business_model_person_appendix")
    case = yaml.safe_load(yaml_text)
    steps_by_id = {step["id"]: step for step in case["steps"]}
    step_order = {step["id"]: idx for idx, step in enumerate(case["steps"])}

    assert "customEvent_8" in steps_by_id
    assert step_order["customEvent_8"] < step_order["load_schedule_quest"]
    assert step_order["click_10"] < step_order["fill_number"]

    defaults = steps_by_id["fill_hrbm_schedule_quest_recorded_defaults"]
    assert defaults["form_id"] == "hrbm_schedule_quest"
    assert set(defaults["fields"]) == {
        "infotype",
        "timeline",
        "timeconstraintmode",
        "mulline",
    }

    pick_fields = case["pick_fields"]
    expected_labels = {
        "pick_infotype_id": "请选择需要添加的信息类型",
        "pick_timeline_id": "请选择添加信息集是否需要按照时间轴记录数据",
        "pick_timeconstraintmode_id": "请选择时间轴的约束信息集",
        "pick_mulline_id": "默认样式是否多行",
    }
    for field_id, label in expected_labels.items():
        assert pick_fields[field_id]["label"] == label
        assert pick_fields[field_id]["form_id"] == "hrbm_schedule_quest"
        assert pick_fields[field_id]["resolve_status"] == "context"
        assert pick_fields[field_id]["context_only"] is True


def test_schedule_quest_pick_override_updates_button_post_data_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780638489_业务模型-添加一个全字段类型人员附表.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="business_model_person_appendix_override",
        pick_field_overrides={
            "pick_timeconstraintmode_id": {
                "value_id": "1",
                "value_code": "1",
                "user_overridden": True,
                "resolve_status": "manual",
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    steps_by_id = {step["id"]: step for step in case["steps"]}
    defaults = steps_by_id["fill_hrbm_schedule_quest_recorded_defaults"]
    click_ok = steps_by_id["click_10"]

    assert str(defaults["fields"]["timeconstraintmode"]) == "1"
    assert str(click_ok["post_data"][1][0]["v"]) == "1"


def test_business_model_har_records_response_semantics_for_field_callbacks():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780641444_业务模型-添加一个全字段类型人员附表.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    case = yaml.safe_load(build_yaml_case(har_path, case_name="business_model_person_appendix"))
    required_rows = set()
    success_anchors = 0
    for step in case["steps"]:
        signature = step.get("expected_response_signature") or {}
        if signature.get("outcome") == "success":
            success_anchors += 1
        for effect in signature.get("required_field_effects") or []:
            if effect.get("field") == "fieldconfig" and effect.get("non_empty"):
                required_rows.add(effect.get("row"))

    assert 7 in required_rows
    assert 32 in required_rows
    assert 33 in required_rows
    assert success_anchors >= 1


def test_ua_newentry_detail_flow_is_core_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779437599_UA提报保存.har"
    if not har_path.exists():
        pytest.skip("local ignored UA HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="ua_submit_save")
    case = yaml.safe_load(yaml_text)
    steps = {step["id"]: step for step in case["steps"]}

    assert steps["click_31"]["key"] == "newentry"
    assert steps["click_31"].get("optional") is not True
    assert "load_empposf7querylist" in steps
    assert "click_34" in steps
    assert "load_bizdatabillnewentry" in steps
    assert steps["pick_bizitemgroup"]["prefetch_lookup"] is True
    step_ids = [step["id"] for step in case["steps"]]
    assert "pick_org_ctx" in steps
    assert step_ids.index("pick_org_ctx") < step_ids.index("pick_bizitemgroup")
    assert steps["fill_bizdate"]["fields"]["bizdate"] == "${vars.test_business_belong_date}"
    assert steps["fill_kd311"]["fields"]["kd311"] == "${vars.test_workday_overtime_hours}"
    assert steps["fill_kd305"]["fields"]["kd305"] == "${vars.test_weekend_overtime_hours}"
    assert steps["fill_kd306"]["fields"]["kd306"] == "${vars.test_holiday_overtime_hours}"
    assert case["vars"]["test_business_belong_date"] == "2026-05-01"
    assert case["vars_labels"]["test_workday_overtime_hours"] == "工作加班小时"

    selector = case["pick_fields"]["selector_employee_position_id"]
    assert selector["label"] == "计薪人员任职经历"
    assert selector["value_id"] == "012890005"
    assert selector["recorded_value_id"] == "2381390967690242048"
    assert selector["source_step_id"] == "entryRowClick_33"
    assert selector["write_step_id"] == "click_34"
    assert selector["resolve_by"] == "value_code"


def test_ua_detail_only_recording_keeps_template_and_selector_bridge():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780283704_提报单据保存002.har"
    if not har_path.exists():
        pytest.skip("local ignored UA HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="ua_submit_save_detail")
    case = yaml.safe_load(yaml_text)
    steps = {step["id"]: step for step in case["steps"]}

    assert "load_bizdatabillchoicetpl" in steps
    assert steps["click_3"]["key"] == "bizitemgroup"
    assert steps["click_3"].get("optional") is not True
    assert steps["click_24"]["key"] == "newentry"
    assert steps["click_24"].get("optional") is not True
    assert "load_empposf7querylist" in steps
    assert "click_27" in steps
    assert "load_bizdatabillnewentry" in steps
    assert "open_bizdatabillnewentry" not in steps


def test_no_menu_l2_recording_with_refresh_reconstructs_menu_bridge():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780292717_金蝶HR-新增员工定调薪申请单.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_apply")
    case = yaml.safe_load(yaml_text)
    steps = {step["id"]: step for step in case["steps"]}
    first_ids = [step["id"] for step in case["steps"][:4]]

    assert first_ids[:2] == ["open_portal", "menuItemClick_hcdm_fapplybill"]
    assert "open_hcdm_fapplybill" not in steps
    assert steps["menuItemClick_hcdm_fapplybill"]["target_form"] == "khr_hcdm_fapplybill"
    assert steps["click_tblrefresh"].get("preserve_l2_page") is True
    assert steps["click_tblnew"].get("preserve_l2_page") is True
    assert "load_hcdm_fapplybill" in steps


def test_salary_adjust_import_drops_portal_side_effect_cards():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780302785_金蝶HR-新增员工定调薪申请单NEW.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment NEW HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_apply_new")
    case = yaml.safe_load(yaml_text)
    form_ids = {step.get("form_id") for step in case["steps"]}
    steps = {step["id"]: step for step in case["steps"]}

    assert "gbs_flowcard" not in form_ids
    assert "gbs_bgtaskdetailsidebar" not in form_ids
    assert "gbs_bgtasklistsidebar" not in form_ids
    assert "khr_hrobs_announcement" not in form_ids
    assert "nbj_user_selfhelp_sc" not in form_ids
    assert steps["menuItemClick_4"]["target_form"] == "khr_hcdm_fapplybill"
    assert steps["click_tblnew"].get("preserve_l2_page") is True
    assert "click_bar_save" in steps


def test_salary_adjust_import_exposes_scalar_combo_fields():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780304604_金蝶HR-新增员工定调薪申请单-薪酬提案为是.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment proposal HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_proposal_yes")
    case = yaml.safe_load(yaml_text)
    pick_fields = case["pick_fields"]

    assert pick_fields["pick_khr_salaryproposal_id"]["label"] == "是否薪酬提案"
    assert pick_fields["pick_khr_salaryproposal_id"]["field_key"] == "khr_salaryproposal"
    assert pick_fields["pick_khr_salaryproposal_id"]["value_code"] == "1"
    assert pick_fields["pick_khr_salaryproposal_id"]["source_step_id"] == "fill_khr_zcurrency_etc"
    assert pick_fields["pick_khr_zcurrency_id"]["label"] == "是否使用薪资核算币种"
    assert pick_fields["pick_khr_scope_id"]["label"] == "定调薪范围"


def test_salary_adjust_submit_import_exposes_adjust_employee_selector():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780367056_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_proposal_no_submit")
    case = yaml.safe_load(yaml_text)
    selector = case["pick_fields"]["selector_salary_adjust_employee_id"]

    assert selector["label"] == "定调薪人员"
    assert selector["field_key"] == "employee_name"
    assert selector["form_id"] == "hcdm_adjfileinfof7"
    assert selector["app_id"] == "hcdm"
    assert selector["value_id"] == "00186-0001"
    assert selector["value_code"] == "00186-0001"
    assert selector["recorded_value_id"] == "2465334257644485632"
    assert selector["source_step_id"] == "entryRowClick_18"
    assert selector["write_step_id"] == "click_19"
    assert selector["resolve_by"] == "value_code"
    assert selector["selector_value_index"] == 0
    assert selector["selector_code_index"] == 1

    step_order = {step["id"]: idx for idx, step in enumerate(case["steps"])}
    selector_order = step_order[selector["source_step_id"]]
    salary_level_order = step_order[case["pick_fields"]["pick_khr_salarylevel_id"]["source_step_id"]]
    assert selector_order < salary_level_order


def test_salary_adjust_submit_import_exposes_upper_person_generic_employee_f7():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780461984_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    preview = preview_har(har_path)
    preview_fields = {item["id"]: item for item in preview["pick_fields"]}
    assert preview_fields["pick_khr_scope_id"]["label"] == "定调薪范围"
    assert preview_fields["selector_khr_upperson_id"]["label"] == "薪酬直接上级"
    assert preview_fields["selector_khr_upperson_id"]["field_key"] == "khr_upperson"
    assert preview_fields["selector_khr_upperson_id"]["value_id"] == "00002"
    assert preview_fields["selector_khr_upperson_id"]["value_code"] == "00002"
    assert preview_fields["selector_khr_upperson_id"]["recorded_value_id"] == "2381390676873979991"
    assert preview_fields["selector_khr_upperson_id"]["parent_form_id"] == "khr_hcdm_fapplybill"
    assert preview_fields["selector_khr_upperson_id"]["parent_field_key"] == "khr_upperson"

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_upper_person")
    case = yaml.safe_load(yaml_text)
    selector = case["pick_fields"]["selector_khr_upperson_id"]
    assert case["pick_fields"]["pick_khr_scope_id"]["label"] == "定调薪范围"
    assert selector["label"] == "薪酬直接上级"
    assert selector["form_id"] == "hrpi_employee"
    assert selector["app_id"] == "hrpi"
    assert selector["source_step_id"] == "entryRowClick_61"
    assert selector["write_step_id"] == "click_62"
    assert selector["resolve_by"] == "value_code"


def test_build_yaml_case_preserves_list_context_for_enterprise_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835335_基础资料-用人单位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_enterprise")
    case = yaml.safe_load(yaml_text)
    steps = case["steps"]

    first_ids = [step["id"] for step in steps[:5]]
    first_forms = [step["form_id"] for step in steps[:5]]

    assert "click_tblrefresh" in first_ids
    assert "entryRowClick_2" in first_ids
    assert first_forms[0] == "hbss_basedatalist"
    assert "open_enterprise" not in first_ids
    assert steps[1].get("optional") is not True
    assert steps[2].get("optional") is not True


def test_build_yaml_case_injects_createorg_context_step_for_enterprise_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835335_基础资料-用人单位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_enterprise")
    case = yaml.safe_load(yaml_text)

    createorg_step = next(step for step in case["steps"] if step["id"] == "fill_createorg_ctx")
    pick_fields = case["pick_fields"]

    assert createorg_step["type"] == "update_fields"
    assert createorg_step["fields"]["createorg"] == "100000"
    assert pick_fields["pick_createorg_id"]["value_id"] == "100000"
    assert pick_fields["pick_createorg_id"]["field_key"] == "createorg"


def test_org_change_reason_har_replays_recorded_control_defaults_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779951981_基础资料-受控-变动原因.har"
    if not har_path.exists():
        pytest.skip("local ignored org change reason HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="regression_org_change_reason")
    case = yaml.safe_load(yaml_text)
    steps = case["steps"]
    step_ids = [step["id"] for step in steps]
    steps_by_id = {step["id"]: step for step in steps}

    assert step_ids[0] == "open_orgchangereason"
    assert "treeMenuClick_1" not in step_ids
    assert steps_by_id["click_tblnew"]["preserve_l2_page"] is True
    assert "fill_haos_orgchangereason_recorded_defaults" in step_ids
    defaults = steps_by_id["fill_haos_orgchangereason_recorded_defaults"]
    assert defaults["type"] == "update_fields"
    assert defaults["fields"]["createorg"] == "100000"
    assert defaults["fields"]["ctrlstrategy"] == "5"
    assert case["pick_fields"]["pick_createorg_id"]["value_id"] == "100000"
    assert case["pick_fields"]["pick_createorg_id"]["value_code"] == "100000"
    assert case["pick_fields"]["pick_createorg_id"]["value_name"] == "环宇国际集团有限公司"
    assert case["pick_fields"]["pick_ctrlstrategy_id"]["label"] == "控制策略"
    assert case["pick_fields"]["pick_ctrlstrategy_id"]["value_name"] == "全局共享"
    assert steps_by_id["click_6"]["key"] == "btnsave"


def test_org_change_reason_pick_override_marks_user_overridden_when_generated():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779951981_基础资料-受控-变动原因.har"
    if not har_path.exists():
        pytest.skip("local ignored org change reason HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="regression_org_change_reason_override",
        pick_field_overrides={
            "pick_createorg_id": {
                "value_id": "100000",
                "value_code": "200000",
                "value_number": "200000",
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "pending",
                "user_overridden": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)

    createorg = case["pick_fields"]["pick_createorg_id"]
    assert createorg["value_code"] == "200000"
    assert createorg["user_overridden"] is True


def test_build_yaml_case_adds_business_block_metadata_for_vars_and_pick_fields():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_adminorg_grouping")
    case = yaml.safe_load(yaml_text)

    assert case["vars_meta"]["test_name"]["form_id"] == "haos_adminorgdetail"
    assert "保存" in case["vars_meta"]["test_name"]["group_label"]
    assert case["pick_fields"]["pick_org_id"]["form_id"] == "haos_adminorgdetail"
    assert case["pick_fields"]["pick_org_id"]["source_step_id"]


def test_build_yaml_case_emits_validation_points_for_system_and_fields():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(
        har_path,
        case_name="regression_adminorg_validation_points",
        var_overrides={
            "test_name": {
                "enabled": True,
                "template": "自动化校验点",
                "user_overridden": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)

    points = case["validation_points"]
    assert any(p["category"] == "system" and p["required"] for p in points)
    field_point = next(p for p in points if p.get("kind") == "variable" and p.get("target_id") == "test_name")
    assert field_point["enabled"] is True
    assert {
        "type": "maintained_value_applied",
        "kind": "variable",
        "target_id": "test_name",
        "step": field_point["step_id"],
    } in case["assertions"]


def test_disabled_field_validation_point_does_not_create_assertion():
    case = {
        "vars_meta": {
            "test_name": {
                "label": "名称",
                "field_key": "name",
                "source_step_id": "fill_name",
                "user_overridden": True,
            }
        },
        "pick_fields": {},
        "steps": [{"id": "fill_name", "type": "update_fields"}],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    }

    points = _build_validation_points(
        case,
        validation_point_overrides={
            "field_var_test_name": {"enabled": False},
        },
    )
    case["validation_points"] = points
    _apply_validation_points_to_assertions(case)

    assert not any(a.get("type") == "maintained_value_applied" for a in case["assertions"])


def test_validation_point_override_can_match_field_metadata_when_id_changes():
    case = {
        "vars_meta": {},
        "pick_fields": {
            "pick_salary_adjust_employee_id": {
                "label": "定调薪人",
                "field_key": "person",
                "source_step_id": "update_salary_adjust_employee",
                "user_overridden": False,
            }
        },
        "steps": [{"id": "update_salary_adjust_employee", "type": "update_fields"}],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    }

    points = _build_validation_points(
        case,
        validation_point_overrides={
            "preview_generated_temporary_id": {
                "enabled": True,
                "kind": "environment_field",
                "target_id": "pick_salary_adjust_employee_id",
                "category": "recommended",
                "scope": "maintainable_field",
            },
        },
    )

    point = next(
        p
        for p in points
        if p.get("kind") == "environment_field"
        and p.get("target_id") == "pick_salary_adjust_employee_id"
    )
    assert point["enabled"] is True


def test_dynamic_query_row_retry_is_limited_to_ten_attempts():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1781256406_0608变动原因.har"

    case = yaml.safe_load(build_yaml_case(har_path, case_name="regression_dynamic_row_retry"))
    dynamic_rows = [
        step
        for step in case["steps"]
        if step.get("dynamic_row_retry_until_found")
    ]

    assert dynamic_rows
    assert all(step.get("dynamic_row_max_attempts") == 10 for step in dynamic_rows)


def test_build_yaml_case_applies_preview_var_override_to_generated_vars():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(
        har_path,
        case_name="regression_adminorg_var_override",
        var_overrides={
            "test_name": {
                "enabled": True,
                "template": "预览页维护后的名称",
            }
        },
    )
    case = yaml.safe_load(yaml_text)

    assert case["vars"]["test_name"] == "预览页维护后的名称"


def test_build_yaml_case_allows_preview_var_override_to_empty_string():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(
        har_path,
        case_name="regression_adminorg_empty_var_override",
        var_overrides={
            "test_name": {
                "enabled": True,
                "template": "",
            }
        },
    )
    case = yaml.safe_load(yaml_text)

    assert case["vars"]["test_name"] == ""


def test_build_yaml_case_syncs_preview_code_override_to_pick_field_value_id():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    yaml_text = build_yaml_case(
        har_path,
        case_name="regression_preview_pick_code_override",
        pick_field_overrides={
            "pick_changedesc_id": {
                "value_id": "100",
                "value_name": "业态兴起",
                "value_code": "0018611-0001",
                "value_number": "0018611-0001",
                "resolve_by": "value_code",
                "auto_resolve": True,
                "resolve_status": "pending",
                "manual_override": False,
                "user_overridden": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    city = case["pick_fields"]["pick_changedesc_id"]

    assert city["value_id"] == "0018611-0001"
    assert city["value_code"] == "0018611-0001"
    assert city["value_number"] == "0018611-0001"
    assert city["value_name"] == ""
    assert city["resolve_by"] == "value_code"
    assert city["auto_resolve"] is True
    assert city["resolve_status"] == "pending"
    assert city["user_overridden"] is True


def test_salary_detail_numeric_update_fields_become_smart_variables():
    steps = [{
        "type": "update_fields",
        "id": "fill_salary_detail",
        "form_id": "khr_hcdm_targetsalary",
        "app_id": "hcdm",
        "fields": {
            "khr_hpostallowance": 11,
            "khr_hmonthlyincome": 22,
        },
    }]

    updated, vars_map, labels = detect_var_placeholders(steps)

    assert vars_map["test_salary_after_post_allowance"] == 11
    assert vars_map["test_salary_after_fixed_monthly_income"] == 22
    assert labels["test_salary_after_post_allowance"] == "调薪后-岗位津贴"
    assert labels["test_salary_after_fixed_monthly_income"] == "调薪后-固定月收入"
    assert updated[0]["fields"]["khr_hpostallowance"] == "${vars.test_salary_after_post_allowance}"
    assert updated[0]["fields"]["khr_hmonthlyincome"] == "${vars.test_salary_after_fixed_monthly_income}"


def test_recorded_post_save_query_prefix_uses_runtime_variable():
    steps = [
        {
            "type": "update_fields",
            "id": "fill_number",
            "form_id": "demo_form",
            "app_id": "demo",
            "fields": {"number": "mars123456"},
        },
        {
            "type": "invoke",
            "id": "save_record",
            "form_id": "demo_form",
            "app_id": "demo",
            "ac": "save",
        },
        {
            "type": "invoke",
            "id": "search_after_save",
            "form_id": "demo_form",
            "app_id": "demo",
            "ac": "commonSearch",
            "args": [[{
                "FieldName": ["number"],
                "Value": ["mars"],
            }]],
        },
    ]

    updated, vars_map, _ = detect_var_placeholders(steps)

    assert vars_map["test_number"].startswith("mars")
    assert updated[2]["args"][0][0]["Value"] == ["${vars.test_number}"]


def test_variable_templates_preserve_recorded_identifier_suffix_shape():
    steps = [{
        "type": "update_fields",
        "id": "fill_employee",
        "form_id": "hom_onbrdinfo",
        "app_id": "hom",
        "fields": {
            "ba_em_empnumber": "zhangxiaojie001",
            "certificatenumber": "zhangxiaojie001",
        },
    }]

    updated, vars_map, _ = detect_var_placeholders(steps)

    assert vars_map["test_number"] == "zhangxiao${rand:6}"
    assert vars_map["test_cert_no"] == "zhangxiao${rand:6}"
    assert updated[0]["fields"]["ba_em_empnumber"] == "${vars.test_number}"
    assert updated[0]["fields"]["certificatenumber"] == "${vars.test_cert_no}"


def test_dynamic_query_row_selection_records_grid_field_mapping():
    query_response = [{
        "a": "u",
        "p": [{
            "k": "billlistap",
            "data": {
                "dataindex": {
                    "hrpi_employee_empnumber": 0,
                    "hspm_assignmentquery_id": 1,
                    "hrpi_employee_id": 2,
                    "org_id": 3,
                },
                "rows": [["EMP001", "A1", "E1", "ORG1"]],
            },
        }],
    }]
    steps = [
        {
            "id": "search_employee",
            "type": "invoke",
            "form_id": "hspm_assignmentlist",
            "app_id": "hspm",
            "ac": "commonSearch",
            "args": [[{
                "FieldName": ["hrpi_employee.empnumber"],
                "Value": ["EMP001"],
            }]],
            "_resp_text": json.dumps(query_response),
        },
        {
            "id": "click_employee",
            "type": "invoke",
            "form_id": "hspm_assignmentlist",
            "app_id": "hspm",
            "ac": "entryRowClick",
            "key": "billlistap",
            "post_data": [{
                "billlistap": {
                    "selDatas": [["A1", "E1", "ORG1"]],
                },
            }, []],
        },
    ]

    _annotate_dynamic_query_row_selections(steps)

    click = steps[1]
    assert click["dynamic_row_source_step_id"] == "search_employee"
    assert click["dynamic_row_field_map"] == [
        "hspm_assignmentquery_id",
        "hrpi_employee_id",
        "org_id",
    ]


def test_salary_adjust_preview_exposes_detail_amounts_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780379727_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    preview = preview_har(har_path)
    by_name = {item["name"]: item for item in preview["detected_vars"]}

    assert by_name["test_salary_after_post_allowance"]["template"] == "11"
    assert by_name["test_salary_after_post_allowance"]["label"] == "调薪后-岗位津贴"
    assert by_name["test_salary_after_post_allowance"]["category"] == "金额"
    assert by_name["test_salary_after_fixed_monthly_income"]["template"] == "22"
    assert by_name["test_salary_after_fixed_monthly_income"]["label"] == "调薪后-固定月收入"
    assert by_name["test_salary_after_fixed_monthly_income"]["category"] == "金额"


def test_salary_adjust_preview_exposes_level_model_and_effective_date_fields_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780379727_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    preview = preview_har(har_path)
    by_id = {item["id"]: item for item in preview["pick_fields"]}

    assert by_id["pick_khr_salarylevel_id"]["label"] == "薪酬水平"
    assert by_id["pick_khr_salarylevel_id"]["value_id"] == "PAY-XCSPDBKD-00001"
    assert by_id["pick_khr_salarylevel_id"]["value_code"] == "PAY-XCSPDBKD-00001"
    assert by_id["pick_khr_salarylevel_id"]["value_name"] == "低于宽带下限二档"
    assert by_id["pick_khr_salarylevel_id"]["resolve_by"] == "value_code"
    assert by_id["pick_khr_hsalarymodel_id"]["label"] == "调薪后-薪酬模式"
    assert by_id["pick_khr_hsalarymodel_id"]["value_id"] == "PAY-XCMS-00001"
    assert by_id["pick_khr_hsalarymodel_id"]["value_code"] == "PAY-XCMS-00001"
    assert by_id["pick_khr_hsalarymodel_id"]["value_name"] == "年薪制"
    assert by_id["pick_khr_hsalarymodel_id"]["resolve_by"] == "value_code"
    assert by_id["pick_khr_hsalarylevel_id"]["label"] == "调薪后-薪酬水平"
    assert by_id["pick_khr_hsalarylevel_id"]["value_id"] == "PAY-XCSPDBKD-00001"
    assert by_id["pick_khr_hsalarylevel_id"]["value_code"] == "PAY-XCSPDBKD-00001"
    assert by_id["pick_khr_hsalarylevel_id"]["value_name"] == "低于宽带下限二档"
    assert by_id["pick_khr_hsalarylevel_id"]["resolve_by"] == "value_code"
    assert by_id["pick_khr_zcurrencyfield_id"]["label"] == "调薪后-币种"
    assert by_id["pick_khr_zcurrencyfield_id"]["value_id"] == "1"
    assert by_id["pick_khr_zcurrencyfield_id"]["value_code"] == "CNY"
    assert by_id["pick_khr_zcurrencyfield_id"]["value_name"] == "人民币"
    assert by_id["pick_khr_zcurrencyfield_id"]["resolve_by"] == "value_code"
    assert by_id["date_khr_heffectivedate"]["label"] == "调薪后-生效日期"
    assert by_id["date_khr_heffectivedate"]["value_id"] == "2026-06-30"


def test_salary_adjust_grid_column_labels_are_used_when_metadata_headers_are_missing():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780379727_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    preview = preview_har(har_path)
    by_id = {item["id"]: item for item in preview["pick_fields"]}

    assert by_id["pick_khr_zcurrencyfield_id"]["label"] == "调薪后-币种"


def test_salary_adjust_archive_hars_do_not_expose_technical_grid_column_labels():
    archive_root = PROJECT_ROOT.parent / "项目归档" / "HAR场景录制库 2" / "金蝶HR项目"
    har_path = archive_root / "定调薪申请审批通过.har"
    if not har_path.exists():
        pytest.skip("desktop HAR archive fixture is not present")

    preview = preview_har(har_path)
    by_id = {item["id"]: item for item in preview["pick_fields"]}

    assert by_id["pick_khr_upperson_id"]["label"] == "薪酬直接上级"
    assert by_id["date_khr_hjteffectivedate"]["label"] == "调薪后津贴-生效日期"
    assert by_id["date_khr_hfleffectivedate"]["label"] == "调薪后福利-生效日期"


def test_salary_adjust_build_keeps_recorded_effective_date_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780379727_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_recorded_date")
    case = yaml.safe_load(yaml_text)
    date_field = case["pick_fields"]["date_khr_heffectivedate"]
    pick_fields = case["pick_fields"]
    fill_step = next(step for step in case["steps"] if step["id"] == "fill_khr_heffectivedate")

    assert pick_fields["pick_khr_salarylevel_id"]["value_id"] == "PAY-XCSPDBKD-00001"
    assert pick_fields["pick_khr_salarylevel_id"]["value_code"] == "PAY-XCSPDBKD-00001"
    assert pick_fields["pick_khr_salarylevel_id"]["value_name"] == "低于宽带下限二档"
    assert pick_fields["pick_khr_hsalarymodel_id"]["value_id"] == "PAY-XCMS-00001"
    assert pick_fields["pick_khr_hsalarymodel_id"]["value_code"] == "PAY-XCMS-00001"
    assert pick_fields["pick_khr_hsalarymodel_id"]["value_name"] == "年薪制"
    assert pick_fields["pick_khr_hsalarylevel_id"]["value_id"] == "PAY-XCSPDBKD-00001"
    assert pick_fields["pick_khr_hsalarylevel_id"]["value_code"] == "PAY-XCSPDBKD-00001"
    assert pick_fields["pick_khr_hsalarylevel_id"]["value_name"] == "低于宽带下限二档"
    assert pick_fields["pick_khr_zcurrencyfield_id"]["label"] == "调薪后-币种"
    assert pick_fields["pick_khr_zcurrencyfield_id"]["value_code"] == "CNY"
    assert pick_fields["pick_khr_zcurrencyfield_id"]["value_name"] == "人民币"
    assert pick_fields["pick_khr_zcurrencyfield_id"]["resolve_by"] == "value_code"
    assert date_field["value_id"] == "2026-06-30"
    assert date_field["value_name"] == "2026-06-30"
    assert fill_step["fields"]["khr_heffectivedate"] == "2026-06-30"
    assert "${today}" not in yaml_text


def test_salary_adjust_scope_three_derives_allowance_and_welfare_dates_from_recorded_har():
    har_path = PROJECT_ROOT.parent / "项目归档" / "回归专用HAR" / "金蝶HR-新增员工定调薪申请单-薪酬提案为否-提交&审核.har"
    if not har_path.exists():
        pytest.skip("desktop regression HAR fixture is not present")

    preview = preview_har(har_path)
    preview_fields = {item["id"]: item for item in preview["pick_fields"]}

    assert preview_fields["date_khr_hjteffectivedate"]["label"] == "调薪后津贴-生效日期"
    assert preview_fields["date_khr_hjteffectivedate"]["value_id"] == "2026-06-30"
    assert preview_fields["date_khr_hfleffectivedate"]["label"] == "调薪后福利-生效日期"
    assert preview_fields["date_khr_hfleffectivedate"]["value_id"] == "2026-06-30"

    yaml_text = build_yaml_case(
        har_path,
        case_name="salary_adjust_scope_all_dates",
        pick_field_overrides={
            "date_khr_hjteffectivedate": {
                "field_key": "khr_hjteffectivedate",
                "form_id": "khr_hcdm_targetsalary",
                "value_id": "2026-07-01",
                "user_overridden": True,
            },
            "date_khr_hfleffectivedate": {
                "field_key": "khr_hfleffectivedate",
                "form_id": "khr_hcdm_targetsalary",
                "value_id": "2026-07-02",
                "user_overridden": True,
            },
        },
    )
    case = yaml.safe_load(yaml_text)
    pick_fields = case["pick_fields"]
    steps_by_id = {step["id"]: step for step in case["steps"]}

    assert pick_fields["date_khr_hjteffectivedate"]["value_id"] == "2026-07-01"
    assert pick_fields["date_khr_hfleffectivedate"]["value_id"] == "2026-07-02"
    assert steps_by_id["fill_khr_hjteffectivedate"]["fields"]["khr_hjteffectivedate"] == "2026-07-01"
    assert steps_by_id["fill_khr_hfleffectivedate"]["fields"]["khr_hfleffectivedate"] == "2026-07-02"
    assert steps_by_id["fill_khr_hjteffectivedate"]["row_index"] == 0
    assert steps_by_id["fill_khr_hfleffectivedate"]["row_index"] == 0
    assert steps_by_id["fill_khr_heffectivedate"]["fields"]["khr_heffectivedate"] == "2026-06-30"


def test_salary_adjust_approval_import_exposes_action_and_opinion_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780544303_金蝶HR-新增员工定调薪申请单-薪酬提案为否-提交&审核.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment approval HAR fixture is not present")

    preview = preview_har(har_path)
    by_id = {item["id"]: item for item in preview["pick_fields"]}
    detected_vars = {item["name"]: item for item in preview["detected_vars"]}

    decision = by_id["pick_decision_radio_group_id"]
    assert decision["label"] == "审批动作"
    assert decision["value_id"] == "Consent"
    assert decision["value_code"] == "Consent"
    assert decision["value_name"] == "同意"
    assert decision["options_text"] == "Consent=同意|Reject=驳回"
    assert decision["options"] == [
        {"value_id": "Consent", "value_code": "Consent", "value_name": "同意"},
        {"value_id": "Reject", "value_code": "Reject", "value_name": "驳回"},
    ]

    opinion = detected_vars["test_workflow_approval_opinion"]
    assert opinion["label"] == "审批意见"
    assert opinion["template"] == "同意"

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_approval_fields")
    case = yaml.safe_load(yaml_text)
    approval_step = next(step for step in case["steps"] if step["id"] == "fill_workflow_approval")

    assert case["vars"]["test_workflow_approval_opinion"] == "同意"
    assert case["vars_meta"]["test_workflow_approval_opinion"]["label"] == "审批意见"
    assert case["pick_fields"]["pick_decision_radio_group_id"]["options_text"] == "Consent=同意|Reject=驳回"
    assert approval_step["fields"]["decision_radio_group"] == "Consent"
    assert approval_step["fields"]["msg_approval"]["zh_CN"] == "${vars.test_workflow_approval_opinion}"


def test_salary_adjust_proposal_audit_import_exposes_proposal_fields_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780560406_金蝶HR-新增员工定调薪申请单-薪酬提案为是-提案审核.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment proposal audit HAR fixture is not present")

    preview = preview_har(har_path)
    detected_vars = {item["name"]: item for item in preview["detected_vars"]}

    proposal_amount = detected_vars["test_salary_proposal_amount"]
    assert proposal_amount["label"] == "薪酬提案"
    assert proposal_amount["field_key"] == "khr_saproposal"
    assert proposal_amount["form_id"] == "khr_hcdm_proposalplan"
    assert proposal_amount["template"] == "5"

    proposal_explanation = detected_vars["test_salary_proposal_explanation"]
    assert proposal_explanation["label"] == "提案说明"
    assert proposal_explanation["field_key"] == "khr_sapexplanation"
    assert proposal_explanation["form_id"] == "khr_hcdm_proposalplan"
    assert proposal_explanation["template"] == "okokok"

    yaml_text = build_yaml_case(har_path, case_name="salary_adjust_proposal_audit")
    case = yaml.safe_load(yaml_text)
    message_step = next(step for step in case["steps"] if step["id"] == "load_msg_message")
    task_step = next(step for step in case["steps"] if step["id"] == "load_task")
    message_center_open = next(step for step in case["steps"] if step["id"] == "open_wf_msg_center")
    message_center_load = next(step for step in case["steps"] if step["id"] == "load_wf_msg_center")
    message_center_tab = next(step for step in case["steps"] if step["id"] == "selectTab_45")
    amount_step = next(step for step in case["steps"] if step["id"] == "fill_khr_saproposal")
    explanation_step = next(step for step in case["steps"] if step["id"] == "fill_khr_sapexplanation")
    menu_step = next(step for step in case["steps"] if step["id"] == "menuItemClick_2")
    steps_by_id = {step["id"]: idx for idx, step in enumerate(case["steps"])}

    assert message_step["optional"] is True
    assert task_step.get("optional") is not True
    assert message_center_open["type"] == "open_form"
    assert message_center_open["form_id"] == "wf_msg_center"
    assert message_center_open["lazy"] is False
    assert message_center_load["form_id"] == "wf_msg_center"
    assert message_center_load["ac"] == "loadData"
    assert steps_by_id["load_wf_msg_center"] < steps_by_id["load_task"]
    assert message_center_tab["optional"] is True
    assert "wf_task" not in (menu_step.get("target_forms") or [])
    assert "khr_hcdm_proposalplan" not in (menu_step.get("target_forms") or [])
    assert case["vars"]["test_salary_proposal_amount"] == 5
    assert case["vars"]["test_salary_proposal_explanation"] == "okokok"
    assert case["vars_meta"]["test_salary_proposal_amount"]["label"] == "薪酬提案"
    assert case["vars_meta"]["test_salary_proposal_explanation"]["label"] == "提案说明"
    assert amount_step["fields"]["khr_saproposal"] == "${vars.test_salary_proposal_amount}"
    assert explanation_step["fields"]["khr_sapexplanation"] == "${vars.test_salary_proposal_explanation}"


def test_set_item_lowering_preserves_entry_row_index_for_basedata_fields():
    lowered = lower_set_item_to_pick_basedata([
        {
            "id": "pick_khr_upperson",
            "type": "invoke",
            "form_id": "khr_hcdm_fapplybill",
            "app_id": "hcdm",
            "ac": "setItemByIdFromClient",
            "method": "setItemByIdFromClient",
            "key": "khr_upperson",
            "args": [["2381416156917661696", 1]],
            "post_data": [{}, []],
            "_har_index": 123,
            "_har_page_id": "proposal-page",
        }
    ])

    assert len(lowered) == 1
    pick_step = lowered[0]
    assert pick_step["type"] == "pick_basedata"
    assert pick_step["field_key"] == "khr_upperson"
    assert pick_step["value_id"] == "2381416156917661696"
    assert pick_step["row_index"] == 1


def test_extract_steps_marks_recorded_tempfile_upload_as_skip_replay():
    actions = [
        {
            "key": "attachmentpanel",
            "methodName": "beforeUpload",
            "args": [[{"name": "image.png", "size": 140526, "type": "image/png"}]],
            "postData": [{}, []],
        },
        {
            "key": "attachmentpanel",
            "methodName": "upload",
            "args": [[{
                "name": "image.png",
                "status": "success",
                "url": "http://example.test/ierp/tempfile/download.do?configKey=tempfile.mock&id=expired",
            }]],
            "postData": [{}, []],
        },
    ]
    har = {
        "log": {
            "entries": [{
                "request": {
                    "url": "http://example.test/ierp/kapi/app/hcdm/form/batchInvokeAction.do?f=khr_hcdm_fapplybill&appId=hcdm&ac=upload",
                    "postData": {
                        "text": urllib.parse.urlencode({
                            "pageId": "runtime-page",
                            "params": json.dumps(actions),
                        })
                    },
                },
                "response": {"content": {"text": "[]"}},
            }]
        }
    }

    steps = extract_steps(har)

    assert [step["method"] for step in steps] == ["beforeUpload", "upload"]
    assert all(step["skip_replay"] is True for step in steps)
    assert all(step["optional"] is True for step in steps)
    assert "预上传" in steps[0]["skip_reason"]
    assert "临时附件" in steps[1]["skip_reason"]
    assert steps[0]["requires_user_file"] is True
    assert steps[1]["upload_replay_strategy"] == "user_file_required"
    assert steps[1]["recorded_tempfile_reference"] is True
    assert steps[1]["recorded_file_names"] == ["image.png"]


def _upload_har_with_real_multipart_request() -> dict:
    actions = [
        {
            "key": "attachmentpanel",
            "methodName": "beforeUpload",
            "args": [[{"name": "image.png", "size": 140526, "type": "image/png"}]],
            "postData": [{}, []],
        },
        {
            "key": "attachmentpanel",
            "methodName": "upload",
            "args": [[{
                "name": "image.png",
                "status": "success",
                "url": "http://example.test/ierp/tempfile/download.do?configKey=tempfile.mock&id=expired",
            }]],
            "postData": [{}, []],
        },
    ]
    return {
        "log": {
            "entries": [
                {
                    "request": {
                        "url": "http://example.test/ierp/tempfile/upload.do?configKey=tempfile.mock",
                        "headers": [
                            {
                                "name": "Content-Type",
                                "value": "multipart/form-data; boundary=----cosmic",
                            }
                        ],
                        "postData": {
                            "mimeType": "multipart/form-data; boundary=----cosmic",
                            "params": [
                                {"name": "file", "fileName": "image.png", "contentType": "image/png"},
                                {"name": "configKey", "value": "tempfile.mock"},
                            ],
                        },
                    },
                    "response": {"content": {"text": "{\"id\":\"runtime-upload\"}"}},
                },
                {
                    "request": {
                        "url": "http://example.test/ierp/kapi/app/hcdm/form/batchInvokeAction.do?f=khr_hcdm_fapplybill&appId=hcdm&ac=upload",
                        "postData": {
                            "text": urllib.parse.urlencode({
                                "pageId": "runtime-page",
                                "params": json.dumps(actions),
                            })
                        },
                    },
                    "response": {"content": {"text": "[]"}},
                },
            ]
        }
    }


def test_extract_steps_attaches_real_upload_endpoint_only_to_upload_action():
    steps = extract_steps(_upload_har_with_real_multipart_request())

    assert [step["method"] for step in steps] == ["beforeUpload", "upload"]
    assert "upload_endpoint" not in steps[0]
    assert steps[1]["upload_endpoint"] == "/tempfile/upload.do?configKey=tempfile.mock"
    assert steps[1]["file_field"] == "file"
    assert steps[1]["extra_data"] == {"configKey": "tempfile.mock"}


def test_preview_har_exposes_single_upload_file_path_pick_field(tmp_path):
    har_path = tmp_path / "upload.har"
    har_path.write_text(json.dumps(_upload_har_with_real_multipart_request()), encoding="utf-8")

    preview = preview_har(har_path)
    upload_fields = [pf for pf in preview["pick_fields"] if pf.get("source_type") == "upload_file"]

    assert len(upload_fields) == 1
    pf = upload_fields[0]
    assert pf["value_id"] == ""
    assert pf["value_name"] == "image.png"
    assert pf["upload_endpoint"] == "/tempfile/upload.do?configKey=tempfile.mock"
    assert pf["file_field"] == "file"
    assert pf["recorded_file_names"] == ["image.png"]
    assert pf["resolve_status"] == "missing_file"


def test_build_yaml_case_applies_upload_file_path_override_to_upload_step(tmp_path):
    har_path = tmp_path / "upload.har"
    har_path.write_text(json.dumps(_upload_har_with_real_multipart_request()), encoding="utf-8")
    preview = preview_har(har_path)
    upload_pf = next(pf for pf in preview["pick_fields"] if pf.get("source_type") == "upload_file")
    file_path = "/Users/mars/Desktop/image.png"

    yaml_text = build_yaml_case(
        har_path,
        case_name="upload attachment",
        pick_field_overrides={
            upload_pf["id"]: {
                "value_id": file_path,
                "value_name": "",
                "manual_override": True,
                "resolve_status": "manual",
                "source_type": "upload_file",
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    upload_steps = [
        step for step in case["steps"]
        if step.get("requires_user_file") and step.get("method") == "upload"
    ]
    before_steps = [
        step for step in case["steps"]
        if step.get("requires_user_file") and step.get("method") == "beforeUpload"
    ]

    assert case["pick_fields"][upload_pf["id"]]["value_id"] == file_path
    assert len(upload_steps) == 1
    assert upload_steps[0]["file_path"] == file_path
    assert upload_steps[0]["upload_endpoint"] == "/tempfile/upload.do?configKey=tempfile.mock"
    assert upload_steps[0]["file_field"] == "file"
    assert before_steps and "file_path" not in before_steps[0]


def test_collapse_repeated_polling_steps_to_wait_until():
    poll_steps = [
        {
            "id": f"getpercent_{idx}",
            "type": "invoke",
            "form_id": "bos_upload_progress",
            "app_id": "bos",
            "ac": "getpercent",
            "key": "progress",
            "method": "getpercent",
            "args": ["upload-token"],
            "post_data": [{}, []],
            "_har_index": 40 + idx,
            "optional": True,
        }
        for idx in range(4)
    ]

    collapsed = collapse_repeated_polling_steps([
        {"id": "before", "type": "invoke", "ac": "loadData"},
        *poll_steps,
        {"id": "after", "type": "invoke", "ac": "save"},
    ])

    assert [step["id"] for step in collapsed] == ["before", "wait_getpercent_upload_progress_40", "after"]
    wait_step = collapsed[1]
    assert wait_step["type"] == "wait_until"
    assert wait_step["condition"] == {"kind": "percent_at_least", "threshold": 100}
    assert wait_step["source_step_count"] == 4
    assert wait_step["optional"] is True
    assert wait_step["max_attempts"] >= 10


def test_build_yaml_case_emits_wait_until_for_repeated_polling(tmp_path):
    actions = [
        {
            "key": "progress",
            "methodName": "getpercent",
            "args": ["upload-token"],
            "postData": [{}, []],
        }
        for _ in range(4)
    ]
    har_path = tmp_path / "polling.har"
    har_path.write_text(
        json.dumps({
            "log": {
                "entries": [{
                    "request": {
                        "url": "http://example.test/ierp/kapi/app/bos/form/batchInvokeAction.do?f=bos_upload_progress&appId=bos&ac=getpercent",
                        "postData": {
                            "text": urllib.parse.urlencode({
                                "pageId": "runtime-page",
                                "params": json.dumps(actions),
                            })
                        },
                    },
                    "response": {"content": {"text": json.dumps({"percent": 100})}},
                }]
            }
        }),
        encoding="utf-8",
    )

    case = yaml.safe_load(build_yaml_case(har_path, case_name="polling_case"))

    wait_steps = [step for step in case["steps"] if step["type"] == "wait_until"]
    assert len(wait_steps) == 1
    assert wait_steps[0]["source_step_count"] == 4
    assert wait_steps[0]["condition"]["kind"] == "percent_at_least"
    assert wait_steps[0]["optional"] is True


def test_insert_workflow_task_wait_steps_before_recorded_task_click():
    steps = [
        {
            "_har_index": 10,
            "id": "commonSearch_10",
            "type": "invoke",
            "form_id": "wf_task",
            "app_id": "bos",
            "ac": "commonSearch",
            "key": "filtercontainerap",
            "method": "commonSearch",
            "args": [[{"FieldName": ["billno"], "Value": ["DTX20260605001"]}]],
            "post_data": [{}, []],
        },
        {
            "_har_index": 11,
            "id": "entryRowClick_11",
            "type": "invoke",
            "form_id": "wf_task",
            "app_id": "bos",
            "ac": "entryRowClick",
            "key": "billlistap",
            "method": "entryRowClick",
        },
    ]

    inserted = insert_workflow_task_wait_steps(steps)

    assert [step["type"] for step in inserted] == ["invoke", "wait_until", "invoke"]
    wait_step = inserted[1]
    assert wait_step["id"] == "wait_wf_task_billno_10"
    assert wait_step["wait_source"] == "workflow_task_search"
    assert wait_step["condition"] == {
        "kind": "grid_row_exists",
        "grid_key": "billlistap",
        "field_key": "billno",
        "value": "DTX20260605001",
        "match_fields": ["billno"],
    }
    assert wait_step["timeout_seconds"] == 15


def test_insert_workflow_task_wait_steps_does_not_touch_plain_search():
    steps = [
        {
            "id": "commonSearch_1",
            "type": "invoke",
            "form_id": "wf_task",
            "app_id": "bos",
            "ac": "commonSearch",
            "args": [[{"FieldName": ["billno"], "Value": ["DTX20260605001"]}]],
        },
        {
            "id": "loadData_2",
            "type": "invoke",
            "form_id": "wf_msg_message",
            "app_id": "bos",
            "ac": "loadData",
        },
    ]

    assert insert_workflow_task_wait_steps(steps) == steps


def test_salary_adjust_approval_preview_override_updates_generated_step_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780544303_金蝶HR-新增员工定调薪申请单-薪酬提案为否-提交&审核.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment approval HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="salary_adjust_approval_reject",
        pick_field_overrides={
            "pick_decision_radio_group_id": {
                "value_id": "Reject",
                "value_name": "驳回",
                "value_code": "Reject",
                "value_number": "Reject",
                "resolve_by": "value_code",
                "resolve_status": "manual",
                "manual_override": False,
                "user_overridden": True,
            },
        },
    )
    case = yaml.safe_load(yaml_text)
    decision = case["pick_fields"]["pick_decision_radio_group_id"]
    approval_step = next(step for step in case["steps"] if step["id"] == "fill_workflow_approval")

    assert decision["value_id"] == "Reject"
    assert decision["value_code"] == "Reject"
    assert decision["value_name"] == "驳回"
    assert decision["user_overridden"] is True
    assert approval_step["fields"]["decision_radio_group"] == "Reject"


def test_salary_adjust_preview_and_generated_maintenance_order_match_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780544303_金蝶HR-新增员工定调薪申请单-薪酬提案为否-提交&审核.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment approval HAR fixture is not present")

    preview = preview_har(har_path)
    case = yaml.safe_load(build_yaml_case(har_path, case_name="salary_adjust_order"))

    def _order(value):
        try:
            return float(value)
        except Exception:
            return 99999

    def _preview_sequence():
        items = []
        for item in preview.get("detected_vars") or []:
            items.append((_order(item.get("order")), "var", item.get("name"), item.get("label", "")))
        for item in preview.get("pick_fields") or []:
            items.append((_order(item.get("order")), "pick", item.get("id"), item.get("label", "")))
        return [
            (kind, key)
            for _order_value, kind, key, _label in sorted(
                items,
                key=lambda row: (row[0], 0 if row[1] == "var" else 1, row[3]),
            )
        ]

    def _case_sequence():
        items = []
        for key in (case.get("vars") or {}):
            if key.startswith("_"):
                continue
            meta = (case.get("vars_meta") or {}).get(key) or {}
            items.append((_order(meta.get("order")), "var", key, meta.get("label", "")))
        for key, meta in (case.get("pick_fields") or {}).items():
            meta = meta or {}
            items.append((_order(meta.get("order")), "pick", key, meta.get("label", "")))
        return [
            (kind, key)
            for _order_value, kind, key, _label in sorted(
                items,
                key=lambda row: (row[0], 0 if row[1] == "var" else 1, row[3]),
            )
        ]

    assert _preview_sequence() == _case_sequence()
    assert case["pick_fields"]["selector_salary_adjust_employee_id"]["order"] < case["vars_meta"]["test_salary_after_post_allowance"]["order"]
    preview_upperson = next(
        item for item in preview.get("pick_fields") or []
        if item.get("id") == "pick_khr_upperson_id"
    )
    case_upperson = case["pick_fields"]["pick_khr_upperson_id"]
    assert preview_upperson["label"] == "薪酬直接上级"
    assert preview_upperson["group_key"].endswith("click_bar_submit_2")
    assert case_upperson["group_key"].endswith("click_bar_submit_2")
    assert preview_upperson["order"] > next(
        item["order"] for item in preview.get("pick_fields") or []
        if item.get("id") == "pick_khr_zcurrencyfield_id"
    )


def test_salary_adjust_preview_exposes_unified_field_catalog_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780379727_金蝶HR-新增员工定调薪申请单-薪酬提案为否-保存&提交.har"
    if not har_path.exists():
        pytest.skip("local ignored salary adjustment submit HAR fixture is not present")

    preview = preview_har(har_path)
    by_key = {
        (item["form_id"], item["field_key"]): item
        for item in preview["field_catalog"]
        if item.get("kind") == "field"
    }

    post_allowance = by_key[("khr_hcdm_targetsalary", "khr_hpostallowance")]
    effective_date = by_key[("khr_hcdm_targetsalary", "khr_heffectivedate")]
    salary_level = by_key[("khr_hcdm_targetsalary", "khr_hsalarylevel")]

    assert post_allowance["category"] == "amount"
    assert post_allowance["panel"] == "vars"
    assert "test_salary_after_post_allowance" in post_allowance["vars"]
    assert effective_date["category"] == "date"
    assert effective_date["panel"] == "pick_fields"
    assert "date_khr_heffectivedate" in effective_date["pick_fields"]
    assert salary_level["panel"] == "pick_fields"


def test_build_yaml_case_extracts_enterprise_description_var():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835335_基础资料-用人单位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_enterprise")
    case = yaml.safe_load(yaml_text)
    save_step = next(step for step in case["steps"] if step["id"] == "click_9")
    desc_value = save_step["post_data"][1][0]["v"]

    assert case["vars"]["test_description"] == "aaaaaa"
    assert case["vars_labels"]["test_description"] == "描述"
    assert desc_value["zh_CN"] == "${vars.test_description}"


def test_kb_loader_reads_shared_hr_entity_metadata():
    scene = kb_loader.resolve_scene("hbss_enterprise")
    meta = kb_loader.field_meta("hbss_enterprise", "description")

    assert scene["name"] == "用人单位"
    assert meta["label"] == "描述"
    assert meta["t"] == "MuliLangTextField"


def test_build_yaml_case_keeps_short_numeric_multilang_values_quoted_in_position_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_position")
    case = yaml.safe_load(yaml_text)
    first_ids = [step["id"] for step in case["steps"][:3]]

    target_step = next(step for step in case["steps"] if step["id"] == "fill_posorientation")

    assert "open_positionhr" in first_ids
    assert target_step["fields"]["posorientation"]["zh_CN"] == "11111"
    assert isinstance(target_step["fields"]["posorientation"]["zh_CN"], str)


def test_build_yaml_case_injects_adminorg_context_step_for_position_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_position")
    case = yaml.safe_load(yaml_text)

    adminorg_step = next(step for step in case["steps"] if step["id"] == "pick_adminorg_ctx")
    pick_fields = case["pick_fields"]

    assert adminorg_step["type"] == "pick_basedata"
    assert adminorg_step["value_id"] == "${vars.adminorg_id}"
    assert pick_fields["pick_adminorg_id"]["value_id"] == "100000"
    assert pick_fields["env_click_tblnew_treeview_focus"]["value_id"] == "100000"


def test_build_yaml_case_marks_menu_navigation_env_sensitive():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="admin_org_nav")
    case = yaml.safe_load(yaml_text)
    menu_step = next(step for step in case["steps"] if step.get("ac") == "menuItemClick")

    assert menu_step["env_sensitive"] == "high"
    assert menu_step["resolve_by"] == "menu_path_or_form"
    assert menu_step["navigation_form_id"] == case["main_form_id"]


def test_build_yaml_case_marks_non_main_navigation_steps_optional():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_position")
    case = yaml.safe_load(yaml_text)

    nav_step = next(step for step in case["steps"] if step["id"] == "selectTab_3")
    business_step = next(step for step in case["steps"] if step["id"] == "click_tblnew")

    assert nav_step["form_id"] == "homs_apphome"
    assert nav_step["optional"] is True
    assert business_step.get("optional") is not True


def test_build_yaml_case_extracts_email_var_for_onboard_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835319_新增入职0512测试.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_onboard")
    case = yaml.safe_load(yaml_text)
    fill_step = next(step for step in case["steps"] if step["id"] == "fill_phone_etc")

    assert case["vars"]["test_email"].endswith("@163.com")
    assert fill_step["fields"]["peremail"] == "${vars.test_email}"


def test_build_yaml_case_marks_onboard_activity_overview_optional():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835319_新增入职0512测试.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_onboard")
    case = yaml.safe_load(yaml_text)
    overview_steps = [
        step for step in case["steps"]
        if step.get("form_id") == "hom_activityoverview"
    ]

    assert overview_steps
    assert all(step.get("optional") is True for step in overview_steps)


def test_onboard_navigation_card_loads_require_harvested_l3_page():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835319_新增入职0512测试.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    case = yaml.safe_load(build_yaml_case(har_path, case_name="onboard_pageid_guard"))
    guarded_forms = {
        step.get("form_id")
        for step in case["steps"]
        if step.get("requires_harvested_l3_page")
    }

    assert {
        "hom_wbcalendar",
        "hom_wbwaitin",
        "bos_card_quicklaunch",
        "hom_wbwarning",
    } <= guarded_forms
    assert not any(
        step.get("requires_harvested_l3_page")
        for step in case["steps"]
        if step.get("form_id") in {"hom_onbrdinfo", "hom_persononbrdhandlebody"}
    )


def test_build_yaml_case_marks_revision_log_page_optional():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779169429_新增一条行政组织.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_adminorg")
    case = yaml.safe_load(yaml_text)
    revision_steps = [
        step for step in case["steps"]
        if step.get("form_id") == "hbp_reviselogpage"
    ]

    assert revision_steps
    assert all(step.get("optional") is True for step in revision_steps)


def test_real_adminorg_har_keeps_recorded_date_and_business_codes():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779256712_金蝶HR-行政组织新增.har"
    if not har_path.exists():
        pytest.skip("local ignored real HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="real_adminorg")
    case = yaml.safe_load(yaml_text)
    pick_fields = case["pick_fields"]

    assert any(
        "bsed" in (step.get("fields") or {})
        for step in case["steps"]
        if step.get("type") == "update_fields"
    )
    assert "date_bsed" in pick_fields
    assert "test_confidential_description" in case["vars"]
    assert case["vars_labels"]["test_confidential_description"] == "保密描述"

    orgform = pick_fields["pick_khr_homs_orgform_id"]
    assert orgform["value_id"] == "KD001"
    assert orgform["recorded_value_id"] == "2336398131039579136"
    assert orgform["value_code"] == "KD001"
    assert orgform["value_name"] == "行政组织"
    assert orgform["auto_resolve"] is True
    assert orgform["resolve_by"] == "value_code"

    orgloc = pick_fields["pick_khr_homs_orgloc_id"]
    assert orgloc["value_id"] == "JD_DW_001"
    assert orgloc["recorded_value_id"] == "2370364949164732416"
    assert orgloc["value_code"] == "JD_DW_001"
    assert orgloc["value_name"] == "总部"
    assert orgloc["auto_resolve"] is True
    assert orgloc["resolve_by"] == "value_code"

    parentorg = pick_fields["pick_parentorg_id"]
    assert parentorg["label"] == "上级行政组织"
    assert parentorg["value_code"] == "-260520-046"
    assert parentorg["value_name"] == "Autotest组织"
    assert parentorg.get("readonly") is not True
    assert any(step.get("field_key") == "parentorg" for step in case["steps"])

    preview = preview_har(har_path)
    preview_ids = {step["id"] for step in preview["steps"]}
    preview_pick_fields = {pf["id"]: pf for pf in preview["pick_fields"]}

    assert "fill_bsed" in preview_ids
    assert preview_pick_fields["pick_khr_homs_orgform_id"]["value_code"] == "KD001"
    assert preview_pick_fields["pick_khr_homs_orgloc_id"]["value_code"] == "JD_DW_001"
    assert preview_pick_fields["pick_parentorg_id"].get("readonly") is not True


def test_real_adminorg_replays_recorded_required_defaults_before_save():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779259965_金蝶HR-行政组织新增.har"
    if not har_path.exists():
        pytest.skip("local ignored real HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="real_adminorg_defaults")
    case = yaml.safe_load(yaml_text)
    pick_fields = case["pick_fields"]
    defaults = {
        "parentorg": "-260520-046",
        "companyarea": "001",
        "city": "00407",
        "org": "JDGJJT",
        "changescene": "1010_S",
        "otclassify": "1010_S",
    }

    first_save_index = next(i for i, step in enumerate(case["steps"]) if step["id"] == "click_new_save")
    first_input_index = next(
        i for i, step in enumerate(case["steps"])
        if step.get("form_id") == case["main_form_id"]
        and step.get("type") in ("update_fields", "pick_basedata")
    )
    for field_key, value_code in defaults.items():
        step = next(step for step in case["steps"] if step.get("field_key") == field_key)
        pf = pick_fields[f"pick_{field_key}_id"]
        assert case["steps"].index(step) < first_save_index
        assert step["value_code"] == value_code
        assert pf["value_code"] == value_code
        assert pf["auto_resolve"] is True
        assert pf["resolve_by"] == "value_code"

    org_step = next(step for step in case["steps"] if step.get("field_key") == "org")
    assert case["steps"].index(org_step) == first_input_index
    assert pick_fields["pick_changescene_id"]["value_id"] == "1010"

    assert not any(
        step.get("id") == "pick_adminorglayer_ctx"
        for step in case["steps"]
    )
    assert "pick_adminorglayer_id" not in pick_fields


def test_real_adminorg_context_parentorg_becomes_active_when_user_overrides():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779256712_金蝶HR-行政组织新增.har"
    if not har_path.exists():
        pytest.skip("local ignored real HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="real_adminorg_parentorg_override",
        pick_field_overrides={
            "pick_parentorg_id": {
                "value_id": "NEW_PARENT_ORG",
                "value_name": "",
                "value_code": "",
                "resolve_status": "manual",
                "manual_override": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)

    parentorg = case["pick_fields"]["pick_parentorg_id"]
    parent_step = next(step for step in case["steps"] if step.get("field_key") == "parentorg")

    assert parentorg["value_id"] == "NEW_PARENT_ORG"
    assert parentorg["resolve_status"] == "manual"
    assert parentorg["manual_override"] is True
    assert "context_only" not in parentorg
    assert parent_step["type"] == "pick_basedata"
    assert parent_step["value_id"] == "${vars.pick_parentorg_id}"


def test_real_adminorg_marks_recorded_intermediate_validation_as_expected():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779259965_金蝶HR-行政组织新增.har"
    if not har_path.exists():
        pytest.skip("local ignored real HAR fixture is not present")

    yaml_text = build_yaml_case(har_path, case_name="real_adminorg_expected_validation")
    case = yaml.safe_load(yaml_text)
    first_save = next(step for step in case["steps"] if step["id"] == "click_new_save")
    final_save = next(step for step in case["steps"] if step["id"] == "click_new_save_2")

    assert first_save["expected_notifications"][0]["content"] == "请选择所属L1流程：ITM下的L2流程"
    assert first_save["continue_on_expected_error"] is True
    assert "expected_notifications" not in final_save
    assert {"type": "expected_notification", "step": "click_new_save", "contains": "请选择所属L1流程：ITM下的L2流程"} in case["assertions"]
    assert {"type": "no_save_failure", "step": "click_new_save_2"} in case["assertions"]
    assert {"type": "no_save_failure", "step": "click_new_save"} not in case["assertions"]


def test_show_message_validation_between_input_and_retry_save_is_expected():
    steps = [
        {
            "id": "click_save",
            "type": "invoke",
            "form_id": "demo_form",
            "ac": "save",
            "_resp_text": json.dumps([{
                "a": "showMessage",
                "p": [{"msg": "xhtestone 调薪后-目标年度奖金不能为空，请填写。；", "messageType": -1}],
            }], ensure_ascii=False),
        },
        {
            "id": "fill_khr_hannualbonus",
            "type": "update_fields",
            "form_id": "demo_form",
            "fields": {"khr_hannualbonus": "88888"},
        },
        {
            "id": "click_save_2",
            "type": "invoke",
            "form_id": "demo_form",
            "ac": "save",
            "_resp_text": json.dumps([{
                "a": "showConfirm",
                "p": [{"msg": "点击“确定”继续提交"}],
            }], ensure_ascii=False),
        },
    ]

    _mark_recorded_business_validations(steps)
    assertions = _build_default_assertions(steps)

    assert steps[0]["expected_notifications"][0]["content"] == "xhtestone 调薪后-目标年度奖金不能为空，请填写。；"
    assert steps[0]["continue_on_expected_error"] is True
    assert {"type": "expected_notification", "step": "click_save", "contains": "xhtestone 调薪后-目标年度奖金不能为空，请填写。；"} in assertions
    assert {"type": "no_save_failure", "step": "click_save_2"} in assertions
    assert {"type": "no_save_failure", "step": "click_save"} not in assertions


def test_salary_calc_decompose_validation_and_final_combo_decision_when_local_har_exists():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1780627096_金蝶HR-薪资核算为是-提交-确认薪资调整审批第一节点-NEW.har"
    if not har_path.exists():
        pytest.skip("local ignored salary calculation HAR fixture is not present")

    preview = preview_har(har_path)
    combo = next(item for item in preview["pick_fields"] if item["id"] == "pick_combo_decision_id")
    assert combo["value_id"] == "同意_and_Consent_and_approve"
    assert combo["source_step_id"] == "fill_combo_decision_2"

    case = yaml.safe_load(build_yaml_case(har_path, case_name="salary_calc_decompose_expected_validation"))
    first_save = next(step for step in case["steps"] if step["id"] == "click_khr_baritemap_2")
    final_save = next(step for step in case["steps"] if step["id"] == "click_khr_baritemap_3")

    assert first_save["expected_notifications"][0]["content"] == "xhtestone 调薪后-目标年度奖金不能为空，请填写。；"
    assert first_save["continue_on_expected_error"] is True
    assert "expected_notifications" not in final_save
    assert case["pick_fields"]["pick_combo_decision_id"]["value_id"] == "同意_and_Consent_and_approve"
    assert {"type": "expected_notification", "step": "click_khr_baritemap_2", "contains": "xhtestone 调薪后-目标年度奖金不能为空，请填写。；"} in case["assertions"]
    assert {"type": "no_save_failure", "step": "click_khr_baritemap_2"} not in case["assertions"]


def test_pick_field_code_override_keeps_code_resolve_editable():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1779259965_金蝶HR-行政组织新增.har"
    if not har_path.exists():
        pytest.skip("local ignored real HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="real_adminorg_code_override",
        pick_field_overrides={
            "pick_khr_homs_orgloc_id": {
                "value_id": "2370364949164732416",
                "value_name": "总部",
                "value_code": "JD_DW_002",
                "value_number": "JD_DW_002",
                "resolve_by": "value_code",
                "auto_resolve": True,
                "resolve_status": "pending",
                "manual_override": False,
                "user_overridden": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    orgloc = case["pick_fields"]["pick_khr_homs_orgloc_id"]

    assert orgloc["value_id"] == "JD_DW_002"
    assert orgloc["recorded_value_id"] == "2370364949164732416"
    assert orgloc["value_code"] == "JD_DW_002"
    assert orgloc["value_number"] == "JD_DW_002"
    assert orgloc["resolve_by"] == "value_code"
    assert orgloc["auto_resolve"] is True
    assert orgloc["resolve_status"] == "pending"
    assert "manual_override" not in orgloc


def test_cross_env_locked_fields_keep_recorded_writes_with_runtime_fallback():
    samples = [
        (
            PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har",
            {"number"},
        ),
        (
            PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har",
            {"name", "number"},
        ),
    ]
    missing = [path for path, _ in samples if not path.exists()]
    if missing:
        pytest.skip("local ignored HAR fixtures are not present")

    for har_path, recorded_fields in samples:
        yaml_text = build_yaml_case(har_path, case_name=f"locked_number_{har_path.stem}")
        case = yaml.safe_load(yaml_text)
        tolerant_update_fields = {
            str(field_key).lower()
            for step in case["steps"]
            if step.get("type") == "update_fields"
            for field_key in (step.get("skip_if_locked_fields") or [])
        }
        assert recorded_fields <= tolerant_update_fields

        tolerant_picks = {
            str(step.get("field_key") or "").lower()
            for step in case["steps"]
            if step.get("type") == "pick_basedata" and step.get("skip_if_locked")
        }
        if har_path.stem.startswith("岗位"):
            assert {"positiontype", "workplace", "job", "diplomareq"} <= tolerant_picks


def test_position_har_exposes_soft_required_template_context_fields():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    case = yaml.safe_load(build_yaml_case(har_path, case_name="position_required_context"))
    pick_fields = case.get("pick_fields") or {}

    changedesc = pick_fields["pick_changedesc_id"]
    assert changedesc["label"] == "变动原因"
    assert changedesc["env_sensitive"] == "high"
    assert changedesc["required_context"] is True

    for field_id, label in {
        "pick_khr_positiontpltype_id": "岗位模板类型",
        "pick_positiontpl_id": "岗位模板",
        "pick_parent_id": "上级岗位",
    }.items():
        assert field_id in pick_fields
        assert pick_fields[field_id]["label"] == label
        assert pick_fields[field_id]["env_sensitive"] == "high"
        assert pick_fields[field_id]["resolve_status"] == "missing_required_context"
        assert pick_fields[field_id]["required_context"] is True


def test_onboard_change_reason_is_exposed_as_soft_required_context_field():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835319_新增入职0512测试.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    preview = preview_har(har_path)
    preview_reason = next(
        item for item in preview["pick_fields"]
        if item.get("field_key") == "chgreason"
    )
    assert preview_reason["label"] == "变动原因"
    assert preview_reason["env_sensitive"] == "high"
    assert preview_reason["required_context"] is True
    assert preview_reason["form_id"] == "hom_onbrdinfo"

    yaml_text = build_yaml_case(har_path, case_name="onboard_reason")
    case = yaml.safe_load(yaml_text)
    reason = case["pick_fields"]["pick_chgreason_id"]
    assert reason["resolve_status"] == "missing_required_context"
    assert reason["required_context"] is True


def test_onboard_change_reason_override_inserts_replay_step():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835319_新增入职0512测试.har"
    if not har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    yaml_text = build_yaml_case(
        har_path,
        case_name="onboard_reason_override",
        pick_field_overrides={
            "pick_chgreason_id": {
                "value_id": "1010_S",
                "value_name": "测试变动原因",
                "value_code": "1010_S",
                "value_number": "1010_S",
                "resolve_by": "value_code",
                "user_overridden": True,
            }
        },
    )
    case = yaml.safe_load(yaml_text)
    reason_steps = [
        step for step in case["steps"]
        if step.get("type") == "pick_basedata" and step.get("field_key") == "chgreason"
    ]
    reason = case["pick_fields"]["pick_chgreason_id"]

    assert reason_steps
    assert reason_steps[0]["value_id"] == "${vars.pick_chgreason_id}"
    assert reason["env_sensitive"] == "high"
    assert reason["required_context"] is True
    assert reason["value_id"] == "1010_S"
