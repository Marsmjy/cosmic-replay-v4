import sys
from pathlib import Path

import pytest
import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_extractor import (
    _append_recorded_default_pick_steps,
    _attach_pick_field_scopes,
    _clean_display_label,
    _scoped_pick_field_id,
    build_yaml_case,
    preview_har,
    to_yaml,
)
from lib import kb_loader


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


def test_recorded_default_pick_steps_are_inserted_for_intermediate_choice_form():
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

    org_step = next(step for step in out if step.get("field_key") == "org")
    assert org_step["form_id"] == "hpdi_bizdatabillchoicetpl"
    assert org_step["value_id"] == "JDGJJT"
    assert org_step["value_code"] == "JDGJJT"
    assert out.index(org_step) < next(
        idx for idx, step in enumerate(out) if step.get("field_key") == "bizitemgroup"
    )


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
    assert steps["fill_bizdate"]["fields"]["bizdate"] == "${vars.test_business_belong_date}"
    assert steps["fill_kd311"]["fields"]["kd311"] == "${vars.test_workday_overtime_hours}"
    assert steps["fill_kd305"]["fields"]["kd305"] == "${vars.test_weekend_overtime_hours}"
    assert steps["fill_kd306"]["fields"]["kd306"] == "${vars.test_holiday_overtime_hours}"
    assert case["vars"]["test_business_belong_date"] == "${today}"
    assert case["vars_labels"]["test_workday_overtime_hours"] == "工作加班小时"

    selector = case["pick_fields"]["selector_employee_position_id"]
    assert selector["label"] == "计薪人员任职经历"
    assert selector["value_id"] == "012890005"
    assert selector["recorded_value_id"] == "2381390967690242048"
    assert selector["source_step_id"] == "entryRowClick_33"
    assert selector["write_step_id"] == "click_34"
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


def test_build_yaml_case_adds_business_block_metadata_for_vars_and_pick_fields():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(har_path, case_name="regression_adminorg_grouping")
    case = yaml.safe_load(yaml_text)

    assert case["vars_meta"]["test_name"]["form_id"] == "haos_adminorgdetail"
    assert "保存" in case["vars_meta"]["test_name"]["group_label"]
    assert case["pick_fields"]["pick_org_id"]["form_id"] == "haos_adminorgdetail"
    assert case["pick_fields"]["pick_org_id"]["source_step_id"]


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
