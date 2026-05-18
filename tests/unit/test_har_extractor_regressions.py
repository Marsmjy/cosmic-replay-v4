import sys
from pathlib import Path

import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_extractor import build_yaml_case, to_yaml


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
