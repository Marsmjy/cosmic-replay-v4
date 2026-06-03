import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.component_registry import analyze_component_coverage, classify_step
from lib.har_extractor import preview_har
from lib.har_quality import assess_preview_quality


def test_component_registry_classifies_common_steps():
    steps = [
        {"id": "open_demo", "type": "open_form", "form_id": "demo"},
        {"id": "fill_number", "type": "update_fields", "fields": {"number": "N1"}},
        {"id": "pick_org", "type": "pick_basedata", "field_key": "adminorg"},
        {"id": "save", "type": "invoke", "ac": "save", "form_id": "demo"},
    ]

    report = analyze_component_coverage(steps)

    assert report["summary"]["total_steps"] == 4
    assert report["summary"]["unsupported_steps"] == 0
    assert report["summary"]["coverage_percent"] == 100
    assert {s["handler_id"] for s in report["steps"]} == {
        "open_form",
        "field_update",
        "basedata_selector",
        "persistence_action",
    }


def test_component_registry_reports_unknown_actions():
    match = classify_step({
        "id": "mystery",
        "type": "invoke",
        "ac": "brandNewAction",
        "method": "doSomething",
    })

    assert match.handler_id == "unknown_action"
    assert match.support_level == "unsupported"
    assert match.risk == "medium"
    assert match.remediation == "add_handler"
    assert "组件处理器" in match.suggestion


def test_component_registry_unknown_gap_suggestions_are_actionable():
    report = analyze_component_coverage([
        {
            "id": "unknown_ok_bridge",
            "type": "invoke",
            "ac": "customAction",
            "method": "doOk",
            "key": "btnok",
        },
        {
            "id": "unknown_hint",
            "type": "invoke",
            "ac": "customHintScroll",
            "method": "scrollTooltip",
        },
    ])

    assert report["summary"]["unsupported_steps"] == 2
    assert report["remediation_counts"]["must_preserve"] == 1
    assert report["remediation_counts"]["safe_noise_optional"] == 1
    suggestions = {item["step_id"]: item for item in report["gap_suggestions"]}
    assert suggestions["unknown_ok_bridge"]["remediation"] == "must_preserve"
    assert suggestions["unknown_hint"]["remediation"] == "safe_noise_optional"


def test_component_registry_classifies_ua_bridge_and_hint_scroll_as_known():
    bridge = classify_step({
        "id": "donothing_newbill",
        "type": "invoke",
        "ac": "donothing_newbill",
    })
    hint = classify_step({
        "id": "getHintScroll_27",
        "type": "invoke",
        "ac": "getHintScroll",
    })

    assert bridge.handler_id == "business_entry_bridge"
    assert bridge.support_level == "supported"
    assert hint.handler_id == "hint_scroll"
    assert hint.support_level == "partial"
    assert hint.risk == "low"


def test_component_registry_classifies_rule_group_filter_grid_as_known_gap():
    match = classify_step({
        "id": "fill_rule_group_filter",
        "type": "update_fields",
        "form_id": "hsas_payrollscene",
        "fields": {
            "salarycalcstyle": "按薪资核算组",
            "attachcondition": "等于",
        },
        "row_index": 0,
    })

    assert match.handler_id == "rule_group_filter_grid"
    assert match.support_level == "partial"
    assert match.risk == "high"
    assert "entryentity" in match.reason


def test_component_registry_classifies_f7_list_selector_as_supported():
    match = classify_step({
        "id": "select_salarycalcstyle",
        "type": "select_f7_list_row",
        "form_id": "hsbp_allowreturnnullf7",
        "app_id": "hsas",
        "value_code": "1010_S",
    })

    assert match.handler_id == "f7_list_selector"
    assert match.support_level == "supported"
    assert match.risk == "low"


def test_quality_uses_component_report_for_compatibility_risk():
    component_report = analyze_component_coverage([
        {"id": "mystery", "type": "invoke", "ac": "brandNewAction"},
        {"id": "save", "type": "invoke", "ac": "save"},
    ])

    quality = assess_preview_quality(
        main_form_id="demo_form",
        tier_counts={"core": 2, "ui_reaction": 0, "noise": 0},
        steps=[
            {"id": "mystery", "type": "invoke", "ac": "brandNewAction"},
            {"id": "save", "type": "invoke", "ac": "save"},
        ],
        detected_vars=[{"name": "number"}],
        pick_fields=[],
        component_report=component_report,
    )

    codes = {issue["code"] for issue in quality["issues"]}
    assert "unsupported_components" in codes
    assert quality["checks"]["component_unsupported_count"] == 1


def test_preview_har_includes_component_radar():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    preview = preview_har(har_path)

    assert preview["components"]["summary"]["total_steps"] == len(preview["steps"])
    assert preview["components"]["summary"]["coverage_percent"] >= 75
    assert preview["quality"]["checks"]["component_coverage_percent"] >= 75
    assert all("component" in step for step in preview["steps"])
