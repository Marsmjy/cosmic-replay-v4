import sys
from pathlib import Path

import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.har_extractor import build_yaml_case, preview_har
from lib.ir.dry_run import dry_run_yaml_case
from lib.yaml_schema import validate_yaml_schema


def test_yaml_schema_contract_accepts_query_only_shape():
    contract = validate_yaml_schema({
        "name": "query_only",
        "schema_version": 1,
        "steps": [{
            "id": "load_list",
            "type": "invoke",
            "form_id": "demo_list",
            "app_id": "demo",
            "ac": "loadData",
            "method": "loadData",
        }],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
        "recording": {"source": "HAR", "base_url": "https://example.test/sit"},
        "ir_contract": {
            "coverage": {"ir_step_count": 1},
            "policy": {"store_full_ir_in_yaml": False, "raw_har_committed": False},
        },
        "capability": {},
        "ai_assistance": {},
        "environment_binding_plan": {},
        "runtime_value_flow_plan": {},
        "execution_contract": {},
    })

    assert contract["ok"] is True
    assert contract["summary"]["write_step_count"] == 0
    assert contract["summary"]["has_ir_contract"] is True
    assert "write_missing_no_save_failure" not in contract["warning_codes"]


def test_yaml_schema_contract_flags_unsafe_full_ir_and_duplicate_steps():
    contract = validate_yaml_schema({
        "name": "bad_case",
        "schema_version": 1,
        "steps": [
            {"id": "same", "type": "invoke", "ac": "save"},
            {"id": "same", "type": "invoke", "ac": "submit"},
        ],
        "assertions": [{"type": "no_error_actions"}],
        "ir_contract": {
            "coverage": {"ir_step_count": 2},
            "policy": {"store_full_ir_in_yaml": True, "raw_har_committed": False},
        },
    })

    assert contract["ok"] is False
    assert "duplicate_step_id" in contract["error_codes"]
    assert "unsafe_full_ir_in_yaml" in contract["error_codes"]
    assert "write_missing_no_save_failure" in contract["warning_codes"]


def test_generated_yaml_and_preview_include_yaml_schema_contract():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    case = yaml.safe_load(build_yaml_case(har_path, case_name="schema_contract_adminorg"))
    preview = preview_har(har_path)

    assert case["yaml_schema_contract"]["summary"]["step_count"] == len(case["steps"])
    assert case["yaml_schema_contract"]["summary"]["has_ir_contract"] is True
    assert case["yaml_schema_contract"]["summary"]["scenario_kind"] == case["scenario"]["kind"]
    assert case["yaml_schema_contract"]["summary"]["field_catalog_count"] == len(case["field_catalog"])
    assert all(item.get("field_id") for item in case["field_catalog"])
    assert [item["order"] for item in case["field_catalog"]] == sorted(
        item["order"] for item in case["field_catalog"]
    )
    assert preview["yaml_schema_contract"]["summary"]["step_count"] == len(preview["steps"])
    assert preview["yaml_schema_contract"]["summary"]["scenario_kind"] == preview["scenario"]["kind"]
    assert preview["yaml_schema_contract"]["ok"] is True


def test_dry_run_yaml_case_uses_yaml_schema_contract():
    summary = dry_run_yaml_case({
        "name": "",
        "steps": [{"id": "", "type": ""}],
    })

    assert summary["ok"] is False
    assert "schema:name_missing" in summary["errors"]
    assert "schema:step_id_missing" in summary["errors"]
    assert summary["yaml_schema_contract"]["status"] == "invalid"
