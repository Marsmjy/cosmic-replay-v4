import json

from fastapi.testclient import TestClient

from lib.agent_evidence import build_repair_evidence_package, save_repair_evidence_package
from lib.task_manager import CaseResult


def test_agent_evidence_package_contains_guardrails_and_artifacts(tmp_path):
    case_path = tmp_path / "case.yaml"
    case_path.write_text(
        "\n".join([
            "name: demo",
            "main_form_id: demo_form",
            "vars:",
            "  test_number: CRPLY_${rand:6}",
            "vars_meta:",
            "  test_number:",
            "    field_key: number",
            "    form_id: demo_form",
            "steps: []",
        ]),
        encoding="utf-8",
    )
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
    assert package["run_artifacts"]["pageid_trace"]["summary"]["total_steps"] == 0
    assert package["run_artifacts"]["ir_summary"]["status"] == "ready"
    assert package["run_artifacts"]["ir_summary"]["raw_har_included"] is False
    assert package["run_artifacts"]["dynamic_value_flow"]["status"] == "ready"
    assert package["skills_to_use"]
    assert package["write_verification"]["readback_plan"]["status"] == "ready"
    assert package["write_verification"]["readback_plan"]["plans"][0]["preferred_filter"]["value_ref"] == "${vars.test_number}"
    assert any("不得删除 menuItemClick" in rule for rule in package["guardrails"])
    assert any("先比对 HAR 原始 pageId 链路" in rule for rule in package["guardrails"])
    assert any("run_artifacts.ir_summary" in rule for rule in package["guardrails"])
    assert any("run_artifacts.dynamic_value_flow" in rule for rule in package["guardrails"])
    assert any("readback_plan" in rule for rule in package["guardrails"])
    assert package["experience_matches"]["status"] == "catalog_unavailable"

    saved = save_repair_evidence_package(package, tmp_path / "evidence")
    loaded = json.loads(saved.read_text(encoding="utf-8"))
    assert loaded["case_name"] == "demo"


def test_agent_evidence_ir_summary_is_value_safe_and_pageid_aware(tmp_path):
    case_path = tmp_path / "case.yaml"
    case_path.write_text(
        "\n".join([
            "name: demo_write",
            "main_form_id: demo_form",
            "target_forms:",
            "  - demo_form",
            "vars:",
            "  test_name: CRPLY_${rand:6}",
            "  secret_note: SHOULD_NOT_APPEAR_IN_IR_SUMMARY",
            "vars_meta:",
            "  test_name:",
            "    field_key: name",
            "    form_id: demo_form",
            "pick_fields:",
            "  pick_org_id:",
            "    field_key: org_id",
            "    label: 组织",
            "    form_id: demo_form",
            "    value: ORG001",
            "steps:",
            "  - id: click_tblnew",
            "    type: invoke",
            "    form_id: demo_form",
            "    app_id: demo",
            "    ac: click",
            "    method: itemClick",
            "    key: tblnew",
            "    preserve_l2_page: true",
            "    _har_page_id: 123root0123456789abcdef0123456789abcdef",
            "  - id: click_save",
            "    type: invoke",
            "    form_id: demo_form",
            "    app_id: demo",
            "    ac: save",
            "    method: save",
            "    key: tbmain",
            "assertions:",
            "  - type: no_save_failure",
            "    step: click_save",
        ]),
        encoding="utf-8",
    )

    package = build_repair_evidence_package(
        task_id="task_1",
        case_name="demo_write",
        report_data={"case_results": [{"name": "demo_write", "passed": False}]},
        case_path=case_path,
        run_events=[
            {
                "type": "pageid_trace",
                "data": {
                    "step_id": "click_save",
                    "runtime_pageid_type": "L2",
                    "runtime_pageid_fragment": "root012345...",
                },
            },
            {
                "type": "step_ok",
                "data": {
                    "step_id": "click_save",
                    "response": [
                        {"a": "u", "p": [{"k": "billno", "v": "BILL_SHOULD_NOT_APPEAR"}]},
                        {"a": "showMessage", "p": [{"msg": "保存成功"}]},
                        {
                            "a": "showConfirm",
                            "p": [{
                                "id": "lockedConfirm",
                                "callbackValue": '{"pkvalue":"PK_SHOULD_NOT_APPEAR"}',
                            }],
                        },
                    ],
                },
            },
        ],
        skill_root=tmp_path,
    )

    ir_summary = package["run_artifacts"]["ir_summary"]
    payload = json.dumps(ir_summary, ensure_ascii=False)

    assert ir_summary["status"] == "ready"
    assert ir_summary["case_shape"]["write_step_count"] == 1
    assert ir_summary["steps"][0]["expected_pageid_role"] == "L2"
    assert ir_summary["steps"][1]["role"] == "write"
    assert ir_summary["environment_fields"][0]["value_shape"] == "code_like_len_6"
    assert ir_summary["dynamic_value_flow"]["summary"]["value_kinds"]["billno"] >= 1
    assert ir_summary["response_anchor_candidates"][0]["anchor_code"] == "save_success"
    assert package["run_artifacts"]["dynamic_value_flow"] == ir_summary["dynamic_value_flow"]
    assert "SHOULD_NOT_APPEAR_IN_IR_SUMMARY" not in payload
    assert "BILL_SHOULD_NOT_APPEAR" not in payload
    assert "PK_SHOULD_NOT_APPEAR" not in payload
    assert "0123456789abcdef0123456789abcdef" not in payload


def test_agent_evidence_package_matches_deep_chain_experience(tmp_path):
    catalog_path = tmp_path / "tests/fixtures/deep_chain_factory/catalog.json"
    catalog_path.parent.mkdir(parents=True)
    catalog_path.write_text(
        json.dumps({
            "scenarios": [
                {
                    "id": "salary_item_new_validation",
                    "app_label": "薪资核算",
                    "menu_label": "薪酬项目",
                    "form_ids": ["hsbs_salaryitem"],
                    "status": "closed_write_passed",
                    "lessons": ["新增后 L2 切 L3，salaryitemtype 需要 getLookUpList 预热。"],
                }
            ]
        }, ensure_ascii=False),
        encoding="utf-8",
    )
    case_path = tmp_path / "salary_item.yaml"
    case_path.write_text(
        "\n".join([
            "name: salary_item",
            "main_form_id: hsbs_salaryitem",
            "steps:",
            "  - id: open",
            "    form_id: hsbs_salaryitem",
        ]),
        encoding="utf-8",
    )

    package = build_repair_evidence_package(
        task_id="task_salary",
        case_name="salary_item",
        report_data={"case_results": [{"name": "salary_item", "passed": False}]},
        case_path=case_path,
        run_events=[],
        skill_root=tmp_path,
    )

    assert package["experience_matches"]["status"] == "matched"
    assert package["experience_matches"]["matches"][0]["scenario_id"] == "salary_item_new_validation"
    assert any("experience_matches" in rule for rule in package["guardrails"])


def test_agent_evidence_guardrails_include_project_core_goals(tmp_path):
    case_path = tmp_path / "case.yaml"
    case_path.write_text("name: demo\nsteps: []\n", encoding="utf-8")

    package = build_repair_evidence_package(
        task_id="task_demo",
        case_name="demo",
        report_data={"case_results": [{"name": "demo", "passed": False}]},
        case_path=case_path,
        run_events=[],
        skill_root=tmp_path,
    )

    guardrails = "\n".join(package["guardrails"])
    assert "项目核心目标" in guardrails
    assert "用户维护值必须生效" in guardrails
    assert "按目标环境接口解析" in guardrails
    assert "入库证据" in guardrails


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


def test_single_run_evidence_reclassifies_old_unknown_readback_gap(tmp_path, monkeypatch):
    from lib.webui import server

    case_path = tmp_path / "readback_gap.yaml"
    case_path.write_text("name: readback_gap\nsteps: []\n", encoding="utf-8")
    evidence_path = server.SKILL_ROOT / "logs/agent_evidence/run_gap_readback_gap.json"

    monkeypatch.setattr(server, "case_path_from_name", lambda _name: case_path)
    monkeypatch.setattr(server.LOG_STORE, "read_run", lambda _run_id: [
        {
            "type": "assertion_fail",
            "data": {
                "type": "readback_by_business_key",
                "msg": "入库回查未找到：haos_orgchangereason.number = KKK852860（只读 commonSearch，响应未包含 grid 行或业务键文本）",
            },
        },
        {
            "type": "failure_analysis",
            "data": {
                "category": "unknown",
                "root_cause": "暂未匹配到已知失败模式。",
            },
        },
        {"type": "case_done", "data": {"passed": False}},
    ])
    monkeypatch.setattr(
        server,
        "save_repair_evidence_package",
        lambda _package, _output_dir: evidence_path,
    )

    response = TestClient(server.APP).get("/api/runs/gap/agent-evidence/readback_gap")

    assert response.status_code == 200
    data = response.json()
    assert data["problem_summary"]["failure_analysis"]["category"] == "readback_assertion_gap"
    assert "通用入库回查未命中" in data["problem_summary"]["ai_reason"]


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
