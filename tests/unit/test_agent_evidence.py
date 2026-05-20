import json

from fastapi.testclient import TestClient

from lib.agent_evidence import build_repair_evidence_package, save_repair_evidence_package
from lib.task_manager import CaseResult


def test_agent_evidence_package_contains_guardrails_and_artifacts(tmp_path):
    case_path = tmp_path / "case.yaml"
    case_path.write_text("name: demo\nsteps: []\n", encoding="utf-8")
    report = {
        "acceptance": {"status": "needs_ai"},
        "action_queues": {"ai_agent": [{"name": "demo"}]},
        "case_results": [
            {
                "name": "demo",
                "passed": True,
                "run_id": "run_1",
                "write_status": "unverified",
                "write_evidence": {"signals": ["save:empty_response"]},
                "next_action": "ai_agent",
            }
        ],
    }

    package = build_repair_evidence_package(
        task_id="task_1",
        case_name="demo",
        report_data=report,
        case_path=case_path,
        run_events=[{"type": "case_done", "data": {"passed": True}}],
        skill_root=tmp_path,
    )

    assert package["problem_summary"]["write_status"] == "unverified"
    assert "name: demo" in package["case_artifacts"]["yaml"]
    assert package["skills_to_use"]
    assert any("不得删除 menuItemClick" in rule for rule in package["guardrails"])
    assert any("先比对 HAR 原始 pageId 链路" in rule for rule in package["guardrails"])

    saved = save_repair_evidence_package(package, tmp_path / "evidence")
    loaded = json.loads(saved.read_text(encoding="utf-8"))
    assert loaded["case_name"] == "demo"


def test_agent_evidence_endpoint_uses_task_report_context(tmp_path, monkeypatch):
    from lib.webui import server

    case_path = tmp_path / "demo_endpoint.yaml"
    case_path.write_text("name: demo_endpoint\nsteps: []\n", encoding="utf-8")
    evidence_path = server.SKILL_ROOT / "logs/agent_evidence/demo_endpoint.json"

    monkeypatch.setattr(server, "case_path_from_name", lambda _name: case_path)
    monkeypatch.setattr(server.LOG_STORE, "read_run", lambda _run_id: [
        {"type": "step_ok", "data": {"step_id": "save_main", "response": []}},
    ])
    monkeypatch.setattr(
        server,
        "save_repair_evidence_package",
        lambda _package, _output_dir: evidence_path,
    )

    task = server.TASK_MANAGER.create_task(["demo_endpoint"], env_id="sit")
    server.TASK_MANAGER.add_result(
        task.task_id,
        CaseResult(
            name="demo_endpoint",
            passed=True,
            run_id="run_endpoint",
            phases=[
                {
                    "id": "save_main",
                    "label": "保存",
                    "status": "ok",
                    "response": [],
                }
            ],
        ),
    )
    server.TASK_MANAGER.generate_report(task.task_id)

    response = TestClient(server.APP).get(
        f"/api/tasks/{task.task_id}/agent-evidence/demo_endpoint"
    )

    assert response.status_code == 200
    data = response.json()
    assert data["problem_summary"]["write_status"] == "unverified"
    assert data["report_context"]["acceptance"]["status"] == "needs_ai"
    assert data["evidence_path"] == "logs/agent_evidence/demo_endpoint.json"


def test_single_run_agent_evidence_endpoint_builds_report_context(tmp_path, monkeypatch):
    from lib.webui import server

    case_path = tmp_path / "single_run.yaml"
    case_path.write_text("name: single_run\nsteps: []\n", encoding="utf-8")
    evidence_path = server.SKILL_ROOT / "logs/agent_evidence/run_run_123_single_run.json"

    monkeypatch.setattr(server, "case_path_from_name", lambda _name: case_path)
    monkeypatch.setattr(server.LOG_STORE, "read_run", lambda _run_id: [
        {
            "type": "step_fail",
            "data": {
                "id": "load_activityoverview",
                "errors": ["java.lang.NullPointerException"],
            },
        },
        {
            "type": "failure_analysis",
            "data": {
                "category": "unknown",
                "root_cause": "流程概览页刷新失败",
            },
        },
        {"type": "case_done", "data": {"passed": False}},
    ])
    monkeypatch.setattr(
        server,
        "save_repair_evidence_package",
        lambda _package, _output_dir: evidence_path,
    )

    response = TestClient(server.APP).get("/api/runs/run_123/agent-evidence/single_run")

    assert response.status_code == 200
    data = response.json()
    assert data["task_id"] == "run_run_123"
    assert data["problem_summary"]["next_action"] == "ai_agent"
    assert data["problem_summary"]["failure_analysis"]["root_cause"] == "流程概览页刷新失败"
    assert data["report_context"]["acceptance"]["status"] == "needs_ai"


def test_single_run_diagnosis_flags_passed_empty_save_response(monkeypatch):
    from lib.webui import server

    monkeypatch.setattr(server, "_case_write_verification", lambda _name: {})
    monkeypatch.setattr(server.LOG_STORE, "read_run", lambda _run_id: [
        {
            "type": "step_start",
            "data": {
                "id": "click_save",
                "label": "保存",
                "detail": "demo/save key=tbmain method=itemClick",
            },
        },
        {
            "type": "step_ok",
            "data": {
                "id": "click_save",
                "duration_ms": 88,
                "response": [],
            },
        },
        {
            "type": "case_done",
            "data": {
                "passed": True,
                "duration_s": 1.2,
                "step_count": 1,
                "step_ok": 1,
            },
        },
    ])

    response = TestClient(server.APP).get("/api/runs/run_empty_save/diagnosis/demo_case")

    assert response.status_code == 200
    data = response.json()
    assert data["passed"] is True
    assert data["write_status"] == "unverified"
    assert data["next_action"] == "ai_agent"
    assert "入库证据" in data["ai_reason"]
