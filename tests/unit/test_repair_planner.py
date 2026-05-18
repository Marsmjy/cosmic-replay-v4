import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.repair_planner import apply_repair, build_repair_plan


def test_repair_plan_marks_navigation_step_optional():
    case = {
        "main_form_id": "demo_bill",
        "steps": [
            {"id": "selectTab_1", "type": "invoke", "form_id": "homs_apphome", "ac": "selectTab"},
            {"id": "save", "type": "invoke", "form_id": "demo_bill", "app_id": "demo", "ac": "save"},
        ],
    }

    plan = build_repair_plan(
        case,
        {"category": "navigation_service_unavailable", "step_id": "selectTab_1"},
        [],
    )
    repair = plan[0]
    new_case, applied, message = apply_repair(case, repair)

    assert repair["operation"] == "mark_step_optional"
    assert repair["safe_to_apply"] is True
    assert applied is True
    assert "optional" in message
    assert new_case["steps"][0]["optional"] is True


def test_repair_plan_refreshes_duplicate_unique_var():
    case = {
        "vars": {"test_number": "AUTO001"},
        "steps": [{"id": "save", "type": "invoke", "form_id": "demo", "app_id": "demo", "ac": "save"}],
    }

    plan = build_repair_plan(
        case,
        {"category": "business_duplicate", "field_caption": "编码"},
        [],
    )
    repair = next(item for item in plan if item["operation"] == "refresh_unique_var")
    new_case, applied, _ = apply_repair(case, repair)

    assert repair["safe_to_apply"] is True
    assert applied is True
    assert new_case["vars"]["test_number"] == "AUTO001${rand:6}"


def test_repair_plan_inserts_missing_name_before_save():
    case = {
        "main_form_id": "demo_bill",
        "vars": {},
        "steps": [
            {"id": "load_demo", "type": "invoke", "form_id": "demo_bill", "app_id": "demo", "ac": "loadData"},
            {"id": "save", "type": "invoke", "form_id": "demo_bill", "app_id": "demo", "ac": "save"},
        ],
    }

    plan = build_repair_plan(
        case,
        {"category": "business_missing_required"},
        [{
            "error_type": "missing_required",
            "diagnosis": "请填写“名称”",
            "field_caption": "名称",
            "field_key": "name",
            "confidence": "high",
        }],
    )
    repair = next(item for item in plan if item["operation"] == "insert_missing_field")
    new_case, applied, _ = apply_repair(case, repair)

    assert repair["safe_to_apply"] is True
    assert applied is True
    assert new_case["steps"][1]["id"] == "auto_fill_name"
    assert new_case["steps"][1]["fields"]["name"] == {"zh_CN": "${vars.auto_name}"}
    assert new_case["vars"]["auto_name"] == "自动修复${rand:6}"
    assert new_case["steps"][2]["id"] == "save"


def test_repair_plan_does_not_apply_unsafe_basedata_without_value():
    case = {
        "main_form_id": "demo_bill",
        "steps": [{"id": "save", "type": "invoke", "form_id": "demo_bill", "app_id": "demo", "ac": "save"}],
    }

    plan = build_repair_plan(
        case,
        {"category": "business_missing_required"},
        [{
            "error_type": "missing_required",
            "diagnosis": "请选择“行政组织”",
            "field_caption": "行政组织",
            "field_key": "adminorg",
            "confidence": "medium",
        }],
    )
    repair = next(item for item in plan if item["operation"] == "insert_missing_field")
    _, applied, message = apply_repair(case, repair)

    assert repair["safe_to_apply"] is False
    assert applied is False
    assert "未标记为安全" in message
