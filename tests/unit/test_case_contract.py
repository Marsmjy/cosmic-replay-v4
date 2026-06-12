import sys
from pathlib import Path

import yaml

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.case_contract import build_case_contract, validate_case_contract_for_run
from lib.har_extractor import build_yaml_case, preview_har


def test_query_only_case_is_read_only_and_does_not_require_readback():
    contract = build_case_contract({
        "name": "query_only",
        "steps": [{
            "id": "load_list",
            "type": "invoke",
            "form_id": "demo_list",
            "app_id": "demo",
            "ac": "loadData",
            "method": "loadData",
        }],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    })

    assert contract["capability"]["flow_kind"] == "query_only"
    assert contract["capability"]["write_mode"] == "read_only"
    assert contract["capability"]["requires_readback"] is False
    assert contract["scenario"]["kind"] == "query"
    assert "只读查询" in contract["ai_assistance"]["assumptions"][0]


def test_write_case_contract_exposes_environment_and_runtime_plans():
    contract = build_case_contract({
        "name": "write_with_pick",
        "pick_fields": {
            "pick_person_id": {
                "label": "人员",
                "field_key": "person",
                "form_id": "demo_bill",
                "app_id": "demo",
                "value_code": "001",
                "value_id": "2381390676873980001",
                "recorded_value_id": "2381390676873980001",
                "auto_resolve": True,
                "resolve_by": "value_code",
                "source_step_id": "pick_person",
                "write_step_id": "save_bill",
            }
        },
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "app_id": "demo",
            "ac": "save",
            "method": "save",
        }],
        "assertions": [{"type": "no_save_failure", "step": "save_bill"}],
    })

    assert contract["capability"]["write_mode"] == "write"
    assert contract["environment_binding_plan"]["summary"]["required_count"] == 1
    assert contract["environment_binding_plan"]["summary"]["static_id_risk_count"] == 1
    assert contract["environment_binding_plan"]["fields"][0]["interface"] == "getLookUpList"
    assert "bill_id" in contract["runtime_value_flow_plan"]["summary"]["producer_kinds"]
    assert contract["write_anchor_plan"]["summary"]["write_anchor_count"] == 1
    assert "目标环境" in contract["ai_assistance"]["need_confirm"][0]


def test_generated_yaml_and_preview_share_case_contract_sections():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835311_新增一条行政组织.har"

    yaml_text = build_yaml_case(har_path, case_name="contract_adminorg")
    case = yaml.safe_load(yaml_text)
    preview = preview_har(har_path)

    for key in (
        "cleanup",
        "scenario",
        "capability",
        "ai_assistance",
        "environment_binding_plan",
        "maintainable_field_binding_plan",
        "write_anchor_plan",
        "runtime_value_flow_plan",
        "execution_contract",
        "report_metadata",
    ):
        assert key in case
        assert key in preview

    assert case["capability"]["requires_environment_preflight"] is True
    assert case["scenario"]["kind"] == preview["scenario"]["kind"]
    assert case["recording"]["base_url"] == preview["recording"]["base_url"]
    assert case["env"]["base_url"].endswith(case["recording"]["base_url"] + "}")
    assert preview["environment_binding_plan"]["summary"]["field_count"] == len(preview["pick_fields"])
    assert any(step.get("ir_sources") for step in case["steps"])
    assert case["cleanup"]["automatic"] is False
    assert case["report_metadata"]["value_safe"] is True


def test_generic_dialog_ok_is_not_a_write_anchor():
    contract = build_case_contract({
        "name": "dialog_confirm_only",
        "steps": [{
            "id": "confirm_picker",
            "type": "invoke",
            "form_id": "demo_f7",
            "ac": "click",
            "method": "click",
            "key": "btnok",
        }],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    })

    assert contract["scenario"]["kind"] == "navigation"
    assert contract["scenario"]["write_mode"] == "read_only"
    assert contract["write_anchor_plan"]["summary"]["write_anchor_count"] == 0
    assert contract["capability"]["requires_readback"] is False


def test_business_confirm_and_submit_remain_write_scenarios():
    confirm = build_case_contract({
        "steps": [{
            "id": "business_confirm",
            "type": "invoke",
            "form_id": "demo_bill",
            "ac": "click",
            "key": "btn_confirm",
        }],
    })
    submit = build_case_contract({
        "steps": [{
            "id": "submit_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "ac": "submit",
        }],
    })

    assert confirm["scenario"]["kind"] == "confirm"
    assert confirm["write_anchor_plan"]["summary"]["write_anchor_count"] == 1
    assert submit["scenario"]["kind"] == "submit"
    assert submit["capability"]["status"] == "partial_supported"
    assert "workflow_or_audit_chain_depends_on_target_env_todo_and_permissions" in (
        submit["capability"]["partial_supported_reasons"]
    )


def test_workflow_decision_plus_submit_is_classified_as_approval():
    contract = build_case_contract({
        "steps": [{
            "id": "fill_approval",
            "type": "update_fields",
            "form_id": "wf_batchtask_handle",
            "fields": {
                "decision_radio_group": "Consent",
                "msg_approval": {"zh_CN": "同意"},
            },
        }, {
            "id": "confirm_approval",
            "type": "invoke",
            "form_id": "wf_batchtask_handle",
            "ac": "btnsubmit",
            "key": "toolbarap",
        }],
    })

    assert contract["scenario"]["kind"] == "approve"
    assert contract["scenario"]["stages"] == ["approve"]
    assert contract["scenario"]["operations"][0]["source"] == (
        "workflow_decision_and_submit_action"
    )


def test_environment_binding_plan_preserves_zero_based_har_order():
    contract = build_case_contract({
        "pick_fields": {
            "pick_second": {
                "label": "第二个字段",
                "value_code": "002",
                "auto_resolve": True,
                "order": 1,
            },
            "pick_first": {
                "label": "第一个字段",
                "value_code": "001",
                "auto_resolve": True,
                "order": 0,
            },
        },
        "steps": [{"id": "load", "type": "invoke", "ac": "loadData"}],
    })

    assert [
        item["id"] for item in contract["environment_binding_plan"]["fields"]
    ] == ["pick_first", "pick_second"]


def test_contract_preflight_blocks_write_missing_no_save_failure_and_required_env_field():
    result = validate_case_contract_for_run({
        "name": "unsafe_write",
        "pick_fields": {
            "pick_person_id": {
                "label": "人员",
                "field_key": "person",
                "form_id": "demo_bill",
                "app_id": "demo",
                "auto_resolve": True,
                "source_step_id": "pick_person",
                "write_step_id": "save_bill",
            }
        },
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "app_id": "demo",
            "ac": "save",
            "method": "save",
        }],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    })

    assert result["ok"] is False
    assert any("目标环境必需字段" in item for item in result["errors"])
    assert any("no_save_failure" in item for item in result["errors"])


def test_contract_preflight_warns_for_soft_runtime_required_context_fields():
    result = validate_case_contract_for_run({
        "name": "soft_context_write",
        "pick_fields": {
            "pick_chgreason_id": {
                "label": "变动原因",
                "field_key": "chgreason",
                "form_id": "hom_onbrdinfo",
                "app_id": "hom",
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "missing_required_context",
                "required_context": True,
                "source": "runtime_rule",
            }
        },
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "hom_onbrdinfo",
            "app_id": "hom",
            "ac": "save",
            "method": "save",
        }],
        "assertions": [{"type": "no_save_failure", "step": "save_bill"}],
    })

    contract = build_case_contract({
        "pick_fields": {
            "pick_chgreason_id": {
                "label": "变动原因",
                "field_key": "chgreason",
                "form_id": "hom_onbrdinfo",
                "app_id": "hom",
                "auto_resolve": True,
                "resolve_by": "value_code",
                "resolve_status": "missing_required_context",
                "required_context": True,
                "source": "runtime_rule",
            }
        },
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "hom_onbrdinfo",
            "app_id": "hom",
            "ac": "save",
            "method": "save",
        }],
        "assertions": [{"type": "no_save_failure", "step": "save_bill"}],
    })

    field = contract["environment_binding_plan"]["fields"][0]
    assert result["ok"] is True
    assert field["status"] == "missing_required_context"
    assert field["required"] is False
    assert field["failure_policy"] == "warn"


def test_contract_preflight_allows_query_without_write_assertions():
    result = validate_case_contract_for_run({
        "name": "query_only",
        "steps": [{
            "id": "load_list",
            "type": "invoke",
            "form_id": "demo_list",
            "app_id": "demo",
            "ac": "loadData",
            "method": "loadData",
        }],
        "assertions": [],
    })

    assert result["ok"] is True
    assert any("no_error_actions" in item for item in result["warnings"])


def test_contract_preflight_blocks_user_override_without_executable_binding_for_write():
    result = validate_case_contract_for_run({
        "name": "unbound_override",
        "pick_fields": {
            "pick_person_id": {
                "label": "人员",
                "field_key": "person",
                "form_id": "demo_bill",
                "value_code": "001",
                "user_overridden": True,
                "auto_resolve": True,
            },
        },
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "ac": "save",
            "method": "save",
        }],
        "assertions": [{"type": "no_save_failure", "step": "save_bill"}],
    })

    assert result["ok"] is False
    assert any("用户维护值没有绑定到可执行步骤" in item for item in result["errors"])


def test_contract_preflight_does_not_apply_write_binding_gate_to_query_only_case():
    result = validate_case_contract_for_run({
        "name": "query_with_optional_filter",
        "pick_fields": {
            "pick_person_id": {
                "label": "人员",
                "field_key": "person",
                "form_id": "demo_list",
                "value_code": "001",
                "user_overridden": True,
                "auto_resolve": True,
            },
        },
        "steps": [{
            "id": "load_list",
            "type": "invoke",
            "form_id": "demo_list",
            "ac": "loadData",
            "method": "loadData",
        }],
        "assertions": [{"type": "no_error_actions", "last_step": True}],
    })

    assert result["ok"] is True


def test_contract_preflight_blocks_ir_write_anchor_without_contracts():
    result = validate_case_contract_for_run({
        "name": "ir_write_contract_gap",
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "ac": "save",
            "ir_write_anchor": True,
            "ir_write_kind": "write_save",
        }],
        "assertions": [{"type": "no_save_failure", "step": "save_bill"}],
    })

    assert result["ok"] is False
    assert any("关键响应契约" in item for item in result["errors"])
    assert any("请求结构契约" in item for item in result["errors"])


def test_delete_requires_target_environment_selector():
    result = validate_case_contract_for_run({
        "name": "unsafe_delete",
        "steps": [{
            "id": "delete_record",
            "type": "invoke",
            "form_id": "demo_list",
            "ac": "delete",
            "method": "delete",
            "args": [{"pkvalue": "2381390676873980001", "version": "12"}],
        }],
        "assertions": [{"type": "no_save_failure", "step": "delete_record"}],
    })

    selector_plan = result["contract"]["target_data_selector_plan"]
    assert result["ok"] is False
    assert selector_plan["status"] == "blocked"
    assert selector_plan["summary"]["static_identity_risk_count"] == 1
    assert any("目标数据选择器不可执行" in item for item in result["errors"])


def test_delete_selector_uses_business_key_and_runtime_identity_bindings():
    case = {
        "name": "safe_delete",
        "steps": [
            {
                "id": "find_target",
                "type": "invoke",
                "form_id": "demo_list",
                "ac": "commonSearch",
                "method": "commonSearch",
            },
            {
                "id": "delete_record",
                "type": "invoke",
                "form_id": "demo_list",
                "ac": "delete",
                "method": "delete",
                "requires_target_selector": True,
                "target_data_selector": {
                    "source_step": "find_target",
                    "field_key": "number",
                    "value": "${vars.target_number}",
                    "match_policy": "exactly_one",
                    "bindings": [
                        {"source_field": "id", "target_path": "args.0.pkvalue"},
                        {"source_field": "version", "target_path": "args.0.version"},
                    ],
                },
            },
        ],
        "assertions": [{"type": "no_save_failure", "step": "delete_record"}],
    }

    contract = build_case_contract(case)
    selector = contract["target_data_selector_plan"]["selectors"][0]
    assert selector["status"] == "ready"
    assert selector["readback_expectation"] == "not_exists"
    assert selector["match_policy"] == "exactly_one"
    assert contract["capability"]["requires_target_data_selector"] is True
