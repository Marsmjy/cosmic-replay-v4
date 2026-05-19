import sys
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(PROJECT_ROOT))

from lib.failure_analysis import classify_error, classify_run_failure
from lib.har_extractor import preview_har
from lib.har_quality import assess_preview_quality


def test_preview_har_returns_quality_for_known_position_har():
    har_path = PROJECT_ROOT / "har_uploads" / "preview_1778835351_岗位信息维护-新增一个岗位.har"

    preview = preview_har(har_path)
    quality = preview["quality"]

    assert quality["score"] >= 70
    assert quality["checks"]["persistence_step_count"] >= 1
    assert quality["checks"]["detected_var_count"] >= 2
    assert all(issue["severity"] != "critical" for issue in quality["issues"])


def test_quality_flags_missing_main_form_and_save_step():
    quality = assess_preview_quality(
        main_form_id="",
        tier_counts={"core": 0, "ui_reaction": 1, "noise": 10},
        steps=[{"id": "load_home", "type": "invoke", "ac": "loadData", "form_id": "home_page"}],
        detected_vars=[],
        pick_fields=[],
    )

    codes = {issue["code"] for issue in quality["issues"]}

    assert quality["blocking"] is True
    assert "main_form_missing" in codes
    assert "core_steps_missing" in codes
    assert "persistence_step_missing" in codes


def test_quality_flags_hardcoded_unique_value():
    quality = assess_preview_quality(
        main_form_id="demo_form",
        tier_counts={"core": 2, "ui_reaction": 0, "noise": 0},
        steps=[
            {
                "id": "fill_number",
                "type": "update_fields",
                "form_id": "demo_form",
                "fields": {"number": "FIXED001"},
            },
            {"id": "click_save", "type": "invoke", "ac": "save", "form_id": "demo_form"},
        ],
        detected_vars=[],
        pick_fields=[],
    )

    assert any(issue["code"] == "hardcoded_unique_value" for issue in quality["issues"])


def test_failure_analysis_classifies_navigation_service_error():
    err = "请求FormService:(homs_apphome.selectTab)失败，原因:未发现AppIdName(homs)服务或访问服务网络异常.错误码:1002"

    result = classify_error(
        err,
        step={"id": "selectTab_3", "form_id": "homs_apphome"},
        case={"main_form_id": "hbpm_positionhr"},
    )

    assert result["category"] == "navigation_service_unavailable"
    assert result["severity"] == "medium"
    assert result["confidence"] == "high"


def test_failure_analysis_classifies_transient_protocol_error():
    result = classify_error(
        "协议错误: invoke hrbm_comboitem_page/updateValue HTTP 502:",
        step={"id": "fill_value", "form_id": "hrbm_comboitem_page"},
        case={"main_form_id": "hrbm_logicentity_display"},
    )

    assert result["category"] == "transient_protocol"
    assert result["retryable"] is True


def test_classify_run_failure_uses_first_non_optional_failure():
    analysis = classify_run_failure(
        steps=[
            {"id": "optional_nav", "ok": False, "optional": True, "error": "ignored"},
            {"id": "save", "ok": False, "error": "请填写\"编码\""},
        ],
        assertions=[],
        case={"main_form_id": "demo_form"},
    )

    assert analysis["category"] == "business_missing_required"
    assert analysis["field_caption"] == "编码"


def test_failure_analysis_classifies_missing_root_org_prerequisite():
    analysis = classify_run_failure(
        steps=[{
            "id": "click_addnew",
            "type": "invoke",
            "ok": False,
            "error": "[Notification] 无根组织，请先完成根组织初始化！",
        }],
        assertions=[],
        case={"main_form_id": "haos_adminorgdetail"},
    )

    assert analysis["category"] == "environment_business_prerequisite"
    assert "根行政组织初始化" in analysis["root_cause"]
