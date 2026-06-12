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
    assert quality["checks"]["detected_var_count"] >= 1
    assert all(issue["severity"] != "critical" for issue in quality["issues"])
    assert preview["preflight"]["score"] >= 60
    assert preview["preflight"]["allow_generate"] is True
    assert preview["pageid_alignment"]["score"] >= 60
    assert "risk_counts" in preview["pageid_alignment"]["checks"]


def test_quality_flags_missing_main_form_without_inventing_save_requirement():
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
    assert "persistence_step_missing" not in codes


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


def test_failure_analysis_classifies_invalid_protocol_request():
    result = classify_error(
        "[Protocol] 无效请求",
        step={"id": "menuItemClick_32", "form_id": "bos_portal_myapp_new"},
        case={"main_form_id": "haos_adminorgdetail"},
    )

    assert result["category"] == "invalid_protocol_request"
    assert result["severity"] == "high"
    assert result["retryable"] is False


def test_failure_analysis_classifies_locked_field_update():
    result = classify_error(
        "ShowNotificationMsg: 无法修改锁定字段组织变动生效日期的值。",
        step={"id": "fill_bsed", "form_id": "haos_adminorgdetail"},
        case={"main_form_id": "haos_adminorgdetail"},
    )

    assert result["category"] == "locked_field_update"
    assert result["field_caption"] == "组织变动生效日期"
    assert result["retryable"] is False


def test_failure_analysis_pageid_recommendation_prioritizes_har_chain():
    result = classify_error(
        "页面未初始化或者已经过期",
        step={"id": "treeNodeClick_47", "form_id": "haos_adminorgdetail"},
        case={"main_form_id": "haos_adminorgdetail"},
    )

    assert result["category"] == "pageid_context"
    assert any("HAR 原始 pageId" in action for action in result["recommended_actions"])
    assert any("preserve_l2_page" in action for action in result["recommended_actions"])


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


def test_failure_analysis_classifies_rule_group_filter_component_gap():
    analysis = classify_run_failure(
        steps=[{
            "id": "click_bar_save",
            "type": "invoke",
            "form_id": "hsas_payrollscene",
            "ok": False,
            "error": "[Notification] 规则分组“默认规则”中，常用筛选不允许为空，请至少填写一行数据。",
        }],
        assertions=[],
        case={"main_form_id": "hsas_payrollscene"},
    )

    assert analysis["category"] == "component_rule_group_filter_missing"
    assert analysis["severity"] == "high"
    assert "entryentity" in analysis["root_cause"]
    assert any("select_f7_list_row" in action for action in analysis["recommended_actions"])
    assert any("不要硬补 save.post_data" in action for action in analysis["recommended_actions"])


def test_failure_analysis_classifies_recorded_temp_attachment_stale():
    analysis = classify_run_failure(
        steps=[{
            "id": "click_bar_submit",
            "type": "invoke",
            "form_id": "khr_hcdm_fapplybill",
            "ok": False,
            "error": "[Timeout] 临时附件已超时，请重新上传以下文件:\nimage.png",
        }],
        assertions=[],
        case={"main_form_id": "khr_hcdm_fapplybill"},
    )

    assert analysis["category"] == "recorded_temp_attachment_stale"
    assert "临时附件句柄已过期" in analysis["root_cause"]
    assert any("skip_replay" in action for action in analysis["recommended_actions"])


def test_failure_analysis_classifies_upload_file_configuration_missing():
    analysis = classify_run_failure(
        steps=[{
            "id": "upload_attach_1",
            "type": "upload_file",
            "form_id": "khr_hcdm_fapplybill",
            "ok": False,
            "error": "协议错误: 真实附件上传缺少 upload_endpoint/upload_url",
        }],
        assertions=[],
        case={"main_form_id": "khr_hcdm_fapplybill"},
    )

    assert analysis["category"] == "upload_file_configuration_missing"
    assert "真实附件上传配置不完整" in analysis["root_cause"]
    assert any("file_path" in action for action in analysis["recommended_actions"])
    assert any("upload_endpoint" in action for action in analysis["recommended_actions"])


def test_failure_analysis_classifies_database_schema_mismatch():
    analysis = classify_run_failure(
        steps=[{
            "id": "click_barstart",
            "type": "invoke",
            "ok": False,
            "error": 'ERROR: column "finitdatasource" of relation "t_hom_onbrdbill_c" does not exist',
        }],
        assertions=[],
        case={"main_form_id": "hom_persononbrdhandlebody"},
    )

    assert analysis["category"] == "environment_schema_mismatch"
    assert "数据库结构" in analysis["root_cause"]


def test_failure_analysis_classifies_server_stack_exception():
    analysis = classify_run_failure(
        steps=[{
            "id": "click_barstart",
            "type": "invoke",
            "ok": False,
            "error": "Key: toolbarap\nTraceId：4da7dce01bf34260\n调用堆栈：\njava.lang.RuntimeException",
        }],
        assertions=[],
        case={"main_form_id": "hom_persononbrdhandlebody"},
    )

    assert analysis["category"] == "environment_server_exception"
    assert "TraceId" in analysis["recommended_actions"][0]


def test_failure_analysis_classifies_assignment_projection_timeout_as_environment_sync():
    analysis = classify_run_failure(
        steps=[{
            "id": "entryRowClick_80",
            "type": "invoke",
            "form_id": "hspm_assignmentlist",
            "ok": False,
            "error": "dynamic list row not found after 40 attempts: commonSearch_79 values=['自动化7317']",
        }],
        assertions=[],
        case={"main_form_id": "hom_onbrdinfo"},
    )

    assert analysis["category"] == "environment_async_business_sync_timeout"
    assert analysis["retryable"] is True
    assert "异步同步" in analysis["root_cause"]
    assert any("运行时姓名或编号" in action for action in analysis["recommended_actions"])


def test_failure_analysis_recovers_form_scope_from_case_step():
    analysis = classify_run_failure(
        steps=[{
            "id": "entryRowClick_80",
            "type": "invoke",
            "ok": False,
            "error": "dynamic list row not found after 40 attempts: commonSearch_79 values=['自动化7317']",
        }],
        assertions=[],
        case={
            "main_form_id": "hom_onbrdinfo",
            "steps": [{
                "id": "entryRowClick_80",
                "form_id": "hspm_assignmentlist",
                "type": "invoke",
            }],
        },
    )

    assert analysis["category"] == "environment_async_business_sync_timeout"
    assert analysis["form_id"] == "hspm_assignmentlist"


def test_failure_analysis_marks_recorded_validation_as_expected():
    result = classify_error(
        "ShowNotificationMsg: 请选择所属L1流程：ITM下的L2流程",
        step={
            "id": "click_new_save",
            "form_id": "haos_adminorgdetail",
            "expected_notifications": [{"content": "请选择所属L1流程：ITM下的L2流程"}],
        },
        case={"main_form_id": "haos_adminorgdetail"},
    )

    assert result["category"] == "business_validation_expected"
    assert result["severity"] == "low"
    assert any("expected_notifications" in action for action in result["recommended_actions"])


def test_failure_analysis_classifies_template_context_before_plain_pageid():
    result = classify_error(
        "ProtocolError: open_form(hpdi_bizdatabillnewentry) got list without pageId: 当前业务数据模板数据缺失，请重新选择模板并创建提报单。",
        step={"id": "open_detail", "form_id": "hpdi_bizdatabillnewentry"},
        case={"main_form_id": "hpdi_bizdatabill"},
    )

    assert result["category"] == "business_template_context_missing"
    assert result["confidence"] == "high"
    assert any("模板" in item for item in result["diagnosis_priority"])


def test_failure_analysis_classifies_environment_context_fields():
    result = classify_error(
        "业务必填字段缺失：创建组织不能为空，控制策略未回填。",
        step={"id": "click_save", "form_id": "homs_chgreason"},
        case={"main_form_id": "homs_chgreason"},
    )

    assert result["category"] == "environment_field_context_missing"
    assert any("loadData" in action for action in result["recommended_actions"])


def test_failure_analysis_classifies_f7_lookup_gap():
    result = classify_error(
        "getLookUpList 候选为空，请选择人员后再确定。",
        step={"id": "select_person", "form_id": "hpdi_bizdatabillnewentry"},
        case={"main_form_id": "hpdi_bizdatabill"},
    )

    assert result["category"] == "f7_lookup_chain_missing"
    assert any("getLookUpList" in action for action in result["recommended_actions"])


def test_failure_analysis_classifies_dialog_detail_gap():
    result = classify_error(
        "子窗口明细分录未回填，主单保存成功但工作加班小时只入库为空。",
        step={"id": "click_btnok", "form_id": "hpdi_bizdatabillnewentry"},
        case={"main_form_id": "hpdi_bizdatabill"},
    )

    assert result["category"] == "dialog_detail_chain_incomplete"
    assert any("newentry" in item for item in result["diagnosis_priority"])


def test_failure_analysis_classifies_readback_assertion_gap():
    result = classify_error(
        "断言 readback_by_business_key 入库回查未找到: "
        "haos_orgchangereason.number = KKKaa720903 "
        "(只读 commonSearch，响应未包含 grid 行或业务键文本)",
        step={"id": "assert_readback", "form_id": "haos_orgchangereason"},
        case={"main_form_id": "haos_orgchangereason"},
    )

    assert result["category"] == "readback_assertion_gap"
    assert result["confidence"] == "high"
    assert any("commonSearch" in item for item in result["diagnosis_priority"])


def test_failure_analysis_classifies_workflow_task_wait_timeout():
    result = classify_error(
        "TimeoutError: wait_until 未在 15s/15 次内满足条件 (grid_row_exists)",
        step={"id": "wait_wf_task_billno_10", "type": "wait_until", "form_id": "wf_task"},
        case={"main_form_id": "khr_hcdm_fapplybill"},
    )

    assert result["category"] == "workflow_task_wait_timeout"
    assert result["retryable"] is True
    assert "审批待办" in result["root_cause"]
    assert any("运行时单号" in item for item in result["diagnosis_priority"])
