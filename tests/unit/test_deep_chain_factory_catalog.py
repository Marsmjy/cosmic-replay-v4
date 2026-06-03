from pathlib import Path

import yaml
import json


PROJECT_ROOT = Path(__file__).resolve().parents[2]


def test_deep_chain_factory_catalog_tracks_closed_salary_samples():
    catalog_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/catalog.json"
    catalog = json.loads(catalog_path.read_text(encoding="utf-8"))

    assert catalog["target_cloud"] == "薪酬福利云"
    scenarios = {item["id"]: item for item in catalog["scenarios"]}
    assert scenarios["salary_data_integration_ua_submit_save"]["status"] == "closed_write_passed"
    assert scenarios["salary_item_category_protocol_save"]["status"] == "closed_write_passed"
    assert scenarios["salary_item_new_validation"]["status"] == "closed_write_passed"
    assert scenarios["salary_period_new_validation"]["status"] == "closed_write_passed"
    assert scenarios["salary_calc_group_protocol_save"]["status"] == "closed_write_passed"
    assert scenarios["salary_retro_reason_protocol_save"]["status"] == "closed_write_passed"
    assert scenarios["salary_calc_scene_rule_group_blocked"]["status"] == "closed_write_passed"
    assert any(
        "规则分组" in lesson
        for lesson in scenarios["salary_calc_scene_rule_group_blocked"].get("lessons", [])
    )


def test_salary_item_category_protocol_fixture_preserves_l2_to_l3_chain():
    case_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/salary_item_category_protocol_save.yaml"
    case = yaml.safe_load(case_path.read_text(encoding="utf-8"))
    steps = {step["id"]: step for step in case["steps"]}

    assert steps["load_statisticstag_list"]["preserve_l2_page"] is True
    assert steps["click_tblnew"]["preserve_l2_page"] is True
    assert steps["pick_taglevel"]["prefetch_lookup"] is True
    assert steps["click_save"]["ac"] == "click"
    assert steps["click_save"]["key"] == "btnsave"
    assert steps["click_save"]["method"] == "click"
    assert {item["type"] for item in case["assertions"]} == {"no_save_failure", "no_error_actions"}


def test_salary_item_protocol_fixture_resolves_required_lookup_before_save():
    case_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/salary_item_protocol_save.yaml"
    case = yaml.safe_load(case_path.read_text(encoding="utf-8"))
    steps = {step["id"]: step for step in case["steps"]}
    pick_fields = case["pick_fields"]

    assert steps["load_salaryitem_list"]["preserve_l2_page"] is True
    assert steps["click_tblnew"]["preserve_l2_page"] is True
    assert steps["pick_salaryitemtype"]["prefetch_lookup"] is True
    assert steps["pick_salaryitemtype"]["auto_resolve"] is True
    assert steps["fill_required_fields"]["fields"]["ispayoutitem"] == "0"
    assert steps["click_bar_save"]["ac"] == "save"
    assert steps["click_bar_save"]["key"] == "tbmain"
    assert steps["click_bar_save"]["args"] == ["bar_save", "save"]
    assert pick_fields["pick_salaryitemtype_id"]["resolve_by"] == "value_name"
    assert pick_fields["enum_ispayoutitem"]["field_key"] == "ispayoutitem"


def test_salary_period_protocol_fixture_adds_entry_after_frequency_rule():
    case_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/salary_period_protocol_save.yaml"
    case = yaml.safe_load(case_path.read_text(encoding="utf-8"))
    steps = {step["id"]: step for step in case["steps"]}
    pick_fields = case["pick_fields"]

    assert steps["load_period_list"]["preserve_l2_page"] is True
    assert steps["click_tblnew"]["preserve_l2_page"] is True
    assert steps["pick_calfrequency"]["prefetch_lookup"] is True
    assert steps["pick_calfrequency"]["auto_resolve"] is True
    assert steps["fill_period_type_fields"]["fields"]["halfmonthfirstday"] == "1"
    assert steps["fill_period_type_fields"]["fields"]["halfmonthsecday"] == "16"
    assert steps["click_addrow"]["ac"] == "newentry"
    assert steps["click_addrow"]["key"] == "advcontoolbarap"
    assert steps["click_addrow"]["args"] == ["addrow", "newentry"]
    assert steps["fill_entry_row"]["row_index"] == 0
    assert steps["click_bar_save"]["args"] == ["bar_save", "save"]
    assert pick_fields["pick_calfrequency_id"]["resolve_by"] == "value_name"
    assert pick_fields["enum_halfmonthfirstday"]["field_key"] == "halfmonthfirstday"
    assert pick_fields["enum_halfmonthsecday"]["field_key"] == "halfmonthsecday"


def test_salary_calc_group_protocol_fixture_resolves_three_required_basedata_fields():
    case_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/salary_calc_group_protocol_save.yaml"
    case = yaml.safe_load(case_path.read_text(encoding="utf-8"))
    steps = {step["id"]: step for step in case["steps"]}
    pick_fields = case["pick_fields"]

    assert steps["load_calc_group_list"]["preserve_l2_page"] is True
    assert steps["click_tblnew"]["preserve_l2_page"] is True
    assert steps["fill_calc_group_required_fields"]["fields"]["bsed"] == "${vars.begin_date}"
    assert steps["pick_country"]["prefetch_lookup"] is True
    assert steps["pick_currency"]["prefetch_lookup"] is True
    assert steps["pick_exratetable"]["prefetch_lookup"] is True
    assert steps["click_bar_save"]["ac"] == "save"
    assert steps["click_bar_save"]["key"] == "tbmain"
    assert steps["click_bar_save"]["args"] == ["bar_save", "save"]
    assert pick_fields["pick_country_id"]["field_key"] == "country"
    assert pick_fields["pick_currency_id"]["field_key"] == "currency"
    assert pick_fields["pick_exratetable_id"]["field_key"] == "exratetable"


def test_salary_retro_reason_protocol_fixture_handles_bos_list_alias():
    case_path = PROJECT_ROOT / "tests/fixtures/deep_chain_factory/salary_retro_reason_protocol_save.yaml"
    case = yaml.safe_load(case_path.read_text(encoding="utf-8"))
    steps = {step["id"]: step for step in case["steps"]}

    assert case["main_form_id"] == "hsas_retroreason"
    assert "hsas_retroreason" in steps["menuItemClick_salary_retro_reason"]["target_forms"]
    assert steps["load_retro_reason_list"]["preserve_l2_page"] is True
    assert steps["click_tblnew"]["preserve_l2_page"] is True
    assert steps["fill_retro_reason_fields"]["fields"]["description"]["zh_CN"] == "${vars.test_description}"
    assert steps["click_bar_save"]["ac"] == "save"
    assert steps["click_bar_save"]["key"] == "tbmain"
    assert steps["click_bar_save"]["args"] == ["bar_save", "save"]
