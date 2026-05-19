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
