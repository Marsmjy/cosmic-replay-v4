from lib.task_manager import (
    CaseResult,
    TaskManager,
    build_acceptance_summary,
    build_decision_summary,
    enrich_case_result,
    infer_write_status,
)


def test_infer_write_status_flags_empty_save_response_as_unverified():
    result = CaseResult(
        name="case_unverified",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "点击保存",
                "status": "ok",
                "response": [],
            }
        ],
    )

    status, evidence = infer_write_status(result)

    assert status == "unverified"
    assert "empty_response" in evidence["signals"][0]
    assert evidence["request_success"] is True
    assert evidence["action_success"] is True
    assert evidence["write_verified"] is False
    assert evidence["business_result"] == "write_unverified"


def test_infer_write_status_flags_invalid_request_as_failed():
    result = CaseResult(
        name="case_invalid_request",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "点击保存",
                "status": "ok",
                "response": {"msg": "无效请求"},
            }
        ],
    )

    status, evidence = infer_write_status(result)

    assert status == "failed"
    assert "invalid_request" in evidence["signals"][0]
    assert evidence["business_result"] == "business_failed"


def test_acceptance_summary_routes_unverified_pass_to_ai_agent():
    result = CaseResult(
        name="case_unverified",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
    )

    enrich_case_result(result)
    summary = build_acceptance_summary([result])

    assert result.next_action == "ai_agent"
    assert result.write_status == "unverified"
    assert summary["status"] == "needs_ai"
    assert summary["ai_required"] == 1


def test_readback_assertion_marks_passed_write_as_verified():
    result = CaseResult(
        name="case_readback_verified",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
        assertions=[
            {
                "type": "readback_by_business_key",
                "ok": True,
                "msg": "入库回查通过",
            }
        ],
    )

    enrich_case_result(result)
    summary = build_acceptance_summary([result])

    assert result.write_status == "verified"
    assert result.write_evidence["write_verified"] is True
    assert result.write_evidence["business_result"] == "write_verified"
    assert result.next_action == "none"
    assert "assertion:readback_by_business_key" in result.write_evidence["signals"]
    assert summary["status"] == "ready"
    assert summary["write_verified"] == 1


def test_runtime_upload_consumption_is_evidence_but_not_full_write_verification():
    result = CaseResult(
        name="case_attachment_consumed",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
        assertions=[
            {
                "type": "runtime_upload_consumed",
                "ok": True,
                "msg": "附件链路回查通过",
            }
        ],
    )

    enrich_case_result(result)

    assert result.write_status == "unverified"
    assert result.next_action == "ai_agent"
    assert "assertion:runtime_upload_consumed" in result.write_evidence["signals"]


def test_strict_attachment_readback_marks_write_as_verified():
    result = CaseResult(
        name="case_attachment_readback_verified",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
        assertions=[
            {
                "type": "readback_runtime_upload",
                "ok": True,
                "msg": "附件入库回查通过",
            }
        ],
    )

    enrich_case_result(result)

    assert result.write_status == "verified"
    assert result.next_action == "none"
    assert "assertion:readback_runtime_upload" in result.write_evidence["signals"]


def test_advisory_attachment_readback_does_not_mark_write_verified():
    result = CaseResult(
        name="case_attachment_readback_advisory",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
        assertions=[
            {
                "type": "readback_runtime_upload",
                "ok": True,
                "advisory": True,
                "msg": "附件入库回查通过",
            }
        ],
    )

    enrich_case_result(result)

    assert result.write_status == "unverified"
    assert result.next_action == "ai_agent"


def test_advisory_readback_failure_does_not_mark_write_verified():
    result = CaseResult(
        name="case_advisory_readback",
        passed=True,
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
        assertions=[
            {
                "type": "readback_by_business_key",
                "ok": False,
                "advisory": True,
                "msg": "通用 commonSearch 未命中，仅作为建议",
            }
        ],
    )

    enrich_case_result(result)
    summary = build_acceptance_summary([result])

    assert result.write_status == "unverified"
    assert result.next_action == "ai_agent"
    assert summary["status"] == "needs_ai"
    assert "assertion:readback_by_business_key" not in result.write_evidence["signals"]


def test_manual_write_confirmation_suppresses_ai_action():
    result = CaseResult(
        name="case_manual_confirmed",
        passed=True,
        write_verification={"manual_confirmed": True},
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [],
            }
        ],
    )

    enrich_case_result(result)
    summary = build_acceptance_summary([result])

    assert result.write_status == "manual_verified"
    assert result.next_action == "none"
    assert result.write_evidence["manual_confirmed"] is True
    assert summary["status"] == "ready"
    assert summary["write_verified"] == 1
    assert summary["ai_required"] == 0


def test_task_manager_report_contains_acceptance_and_queues():
    manager = TaskManager()
    task = manager.create_task(["case_a"], env_id="sit")
    manager.add_result(
        task.task_id,
        CaseResult(
            name="case_a",
            passed=False,
            error="页面未初始化或者已经过期",
            failure_analysis={"category": "pageid_context", "root_cause": "PageId 失效"},
        ),
    )

    report = manager.generate_report(task.task_id)
    data = report.to_dict()

    assert data["acceptance"]["failed"] == 1
    assert data["acceptance"]["ai_required"] == 1
    item = data["action_queues"]["ai_agent"][0]
    assert item["name"] == "case_a"
    assert item["decision_category"] == "script_chain"
    assert "pageId" in item["decision_title"]
    assert "交给 AI" in item["next_step"]


def test_readback_assertion_gap_routes_to_ai_with_clear_reason():
    result = CaseResult(
        name="case_readback_gap",
        passed=False,
        error="断言 readback_by_business_key 入库回查未找到",
        failure_analysis={
            "category": "readback_assertion_gap",
            "root_cause": "通用 commonSearch 不适配该表单",
        },
        phases=[
            {
                "id": "step:save_main",
                "label": "保存",
                "status": "ok",
                "response": [{"p": "保存成功。"}],
            }
        ],
    )

    enrich_case_result(result)

    assert result.next_action == "ai_agent"
    assert "通用入库回查未命中" in result.ai_reason


def test_report_hydration_applies_manual_write_confirmation(monkeypatch):
    from lib.webui import server

    monkeypatch.setattr(
        server,
        "_case_write_verification",
        lambda name: {"manual_confirmed": True, "reason": "人工确认"} if name == "case_a" else {},
    )
    report = {
        "case_results": [{
            "name": "case_a",
            "passed": True,
            "write_status": "unverified",
            "write_evidence": {"signals": ["save:empty_response"]},
            "next_action": "ai_agent",
            "ai_reason": "缺少明确入库证据",
        }],
        "acceptance": {},
        "action_queues": {},
    }

    hydrated = server._apply_manual_write_confirmations(report)

    row = hydrated["case_results"][0]
    assert row["write_status"] == "manual_verified"
    assert row["next_action"] == "none"
    assert hydrated["acceptance"]["status"] == "ready"
    assert hydrated["action_queues"]["ai_agent"] == []


def test_response_contract_failure_marks_write_failed_and_explains_next_step():
    result = CaseResult(
        name="case_contract_drift",
        passed=True,
        phases=[{
            "id": "step:save_main",
            "label": "保存",
            "status": "ok",
            "response": {"success": True},
        }],
        runtime_evidence={
            "response_contract_results": {
                "save_main": {
                    "contract_level": "critical",
                    "errors": ["required action ShowNotificationMsg missing"],
                    "warnings": [],
                }
            }
        },
    )

    enrich_case_result(result)

    assert result.write_status == "failed"
    assert "response_contract_failed" in result.write_evidence["signals"][0]
    assert result.decision_summary["category"] == "script_or_environment_contract_drift"
    assert "关键接口响应" in result.decision_summary["title"]


def test_decision_summary_prefers_environment_binding_when_required_field_missing():
    result = CaseResult(
        name="case_env_missing",
        passed=False,
        runtime_evidence={
            "capability": {"status": "partial_supported", "flow_kind": "write"},
            "environment_binding_plan": {
                "fields": [{
                    "id": "pick_person_id",
                    "label": "人员",
                    "required": True,
                    "status": "missing",
                }]
            },
        },
    )

    summary = build_decision_summary(result)

    assert summary["category"] == "environment_binding"
    assert summary["unresolved_env_field_count"] == 1


def test_decision_summary_explains_first_success_gate_failure():
    result = CaseResult(
        name="case_first_success_gap",
        passed=False,
        runtime_evidence={
            "capability": {"status": "supported", "flow_kind": "write"},
            "first_success_gate": {
                "status": "failed",
                "missing": ["write_anchor_execution", "critical_response_contract"],
            },
        },
    )

    summary = build_decision_summary(result)

    assert summary["category"] == "first_success_gate_failed"
    assert summary["first_success_status"] == "failed"
    assert "write_anchor_execution" in summary["first_success_missing"]


def test_decision_summary_separates_confirmed_and_unconfirmed_write_evidence():
    result = CaseResult(
        name="case_write_unverified",
        passed=True,
        write_status="unverified",
        runtime_evidence={
            "first_success_gate": {
                "status": "write_unverified",
                "missing": ["readback_or_manual_verification"],
            },
        },
    )

    summary = build_decision_summary(result)

    assert summary["category"] == "write_unverified"
    assert summary["confirmed"]
    assert summary["unconfirmed"] == ["目标环境中是否真实存在本次运行写入的数据"]
    assert "不要把保存成功当成入库成功" in summary["next_step"]
