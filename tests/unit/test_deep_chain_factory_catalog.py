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
    assert scenarios["salary_item_new_validation"]["status"] == "level1_new_page_collected"


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
