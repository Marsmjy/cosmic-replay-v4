from lib.task_manager import (
    CaseResult,
    TaskManager,
    build_acceptance_summary,
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
    assert result.next_action == "none"
    assert "assertion:readback_by_business_key" in result.write_evidence["signals"]
    assert summary["status"] == "ready"
    assert summary["write_verified"] == 1


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
    assert data["action_queues"]["ai_agent"][0]["name"] == "case_a"


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
