"""Evidence package builder for AI-assisted Cosmic Replay repair.

The package is intentionally read-only: it gathers the failing case, report
metadata, run events, and guardrails so an external agent can propose a minimal
patch without touching known-good cases.
"""
from __future__ import annotations

import json
from datetime import datetime
from pathlib import Path
from typing import Any

import yaml

from .deep_chain_pipeline import DEFAULT_CATALOG, build_readback_plan, load_catalog, match_experience_catalog
from .failure_analysis import classify_run_failure
from .ir.evidence import build_case_ir_summary
from .pageid_trace import build_pageid_trace
from .case_contract import build_case_contract


def build_repair_evidence_package(
    *,
    task_id: str,
    case_name: str,
    report_data: dict[str, Any],
    case_path: Path,
    run_events: list[dict[str, Any]],
    skill_root: Path,
) -> dict[str, Any]:
    case_result = _normalize_case_result_analysis(_find_case_result(report_data, case_name), case_name=case_name)
    yaml_text = case_path.read_text(encoding="utf-8") if case_path.exists() else ""
    try:
        case_data = yaml.safe_load(yaml_text) if yaml_text else {}
    except Exception:
        case_data = {}
    if not isinstance(case_data, dict):
        case_data = {}
    run_id = case_result.get("run_id", "")
    readback_plan = build_readback_plan(case_data)
    experience_matches = _build_experience_matches(skill_root, case_data)
    pageid_trace = build_pageid_trace(
        case_data if isinstance(case_data, dict) else {},
        run_events=run_events,
        include_fragments=True,
    )
    ir_summary = build_case_ir_summary(
        case_data if isinstance(case_data, dict) else {},
        run_events=run_events,
    )
    case_contract = build_case_contract(case_data)
    package = {
        "schema_version": "1.0",
        "created_at": datetime.now().isoformat(),
        "task_id": task_id,
        "case_name": case_name,
        "run_id": run_id,
        "problem_summary": {
            "passed": case_result.get("passed"),
            "error": case_result.get("error", ""),
            "error_category": case_result.get("error_category", ""),
            "write_status": case_result.get("write_status", "not_checked"),
            "write_evidence": case_result.get("write_evidence", {}),
            "next_action": case_result.get("next_action", ""),
            "ai_reason": case_result.get("ai_reason", ""),
            "failure_analysis": case_result.get("failure_analysis", {}),
        },
        "write_verification": {
            "readback_plan": readback_plan,
            "when_to_use": "当执行 PASS 但 write_status=unverified，优先按本计划做只读业务键回查。",
        },
        "experience_matches": experience_matches,
        "case_artifacts": {
            "case_path": str(case_path),
            "yaml": yaml_text,
            "case_contract": case_contract,
        },
        "run_artifacts": {
            "events_count": len(run_events),
            "events": run_events[-300:],
            "failed_events": [
                event for event in run_events
                if event.get("type") in {"step_fail", "assertion_fail", "case_error"}
            ],
            "pageid_trace": pageid_trace,
            "ir_summary": ir_summary,
            "dynamic_value_flow": ir_summary.get("dynamic_value_flow", {}),
            "scenario": case_contract.get("scenario", {}),
            "report_metadata": case_contract.get("report_metadata", {}),
            "environment_binding_plan": case_contract.get("environment_binding_plan", {}),
            "maintainable_field_binding_plan": case_contract.get("maintainable_field_binding_plan", {}),
            "write_anchor_plan": case_contract.get("write_anchor_plan", {}),
            "runtime_value_flow_plan": case_contract.get("runtime_value_flow_plan", {}),
            "first_success_gate": (
                (case_result.get("runtime_evidence") or {}).get("first_success_gate") or {}
            ),
            "capability": case_contract.get("capability", {}),
        },
        "report_context": {
            "acceptance": report_data.get("acceptance", {}),
            "action_queues": report_data.get("action_queues", {}),
            "case_result": case_result,
        },
        "skills_to_use": [
            str(skill_root / "skills" / "cosmic-replay-overview" / "skill.md"),
            str(skill_root / "skills" / "cosmic-replay-troubleshooter" / "SKILL.md"),
            str(skill_root / "skills" / "cosmic-replay-troubleshooter" / "references" / "pageid-chain-debugging.md"),
            str(skill_root / "skills" / "cosmic-replay-troubleshooter" / "references" / "assertion-blindspots.md"),
            str(skill_root / "skills" / "cosmic-hr-expert" / "SKILL.md"),
        ],
        "guardrails": [
            "项目核心目标：HAR 解析出可维护字段、用户维护值必须生效、F7/下拉/基础资料按目标环境接口解析、跨环境动态适配、执行必须校验保存/提交和入库证据、经验沉淀为通用规则和知识库、批量 HAR 要比较优化前后并输出报告。",
            "只允许对当前 case 或通用解析/执行规则做最小补丁。",
            "不得删除 menuItemClick、target_forms、pick_fields 或 no_save_failure 断言来绕过问题。",
            "不得修改已成功回归样本的 YAML baseline，除非影响报告明确为 none/review 且有理由。",
            "修复后必须运行 HAR 回归 compare --fail-on-diff 和相关单测。",
            "如果缺少入库证据，优先补入库验证或 pageId 链路，不要把 PASS 当作成功。",
            "若 write_status=unverified，先按 write_verification.readback_plan 做只读业务键回查；不要新增、保存、提交或硬补 save.post_data。",
            "先查看 experience_matches：若命中已闭环样本，优先复用相似样本的 pageId、lookup、F7、子弹窗和入库回查经验。",
            "先查看 run_artifacts.ir_summary：它按 YAML/runtime 摘要展示写入步骤、变量形态、环境字段形态和 pageId 风险，不包含原始 HAR 或真实敏感值。",
            "先查看 run_artifacts.dynamic_value_flow：它脱敏展示 pageId/billno/确认回调/上传 URL/待办任务行等运行时值的生产和消费链路，用来判断是否仍在消费 HAR 录制旧值。",
            "若 YAML 步骤包含 recorded_pageid_source_step_id/source_kind/source_retained，先按该精确 HAR 生产者链路核对运行时 pageId；source_retained=false 只是过滤诊断，需结合 optional、requires_harvested_l3_page 和运行 trace 判断，不能直接硬保留 clientCallBack。",
            "先查看 failure_analysis.diagnosis_priority：它会提示优先查 pageId、模板/F7、环境字段、子弹窗明细、预期业务校验或入库断言盲区。",
            "先比对 HAR 原始 pageId 链路与回放 pageId 是否一致，再看变量解析和字段补偿；不得用硬补 save 字段替代 pageId 修复。",
            "遇到 createorg/ctrlstrategy/默认组织/控制策略缺失时，从 HAR loadData、showForm 元数据、列表 dataindex/rows 提取环境字段和内部 id，写入模型上下文而不是 save.post_data。",
        ],
        "expected_agent_output": {
            "diagnosis_json": "根因、证据、风险等级、影响范围",
            "patch_diff": "最小代码或 YAML 补丁",
            "tests_to_run": [
                "./venv/bin/python -m pytest -q tests/unit tests/test_core.py",
                "./venv/bin/python scripts/har_regression_report.py compare --fail-on-diff",
            ],
            "rollback_plan": "如何回滚本次补丁",
            "needs_human_confirmation": "是否需要用户确认环境字段或业务字段",
        },
    }
    return package


def _build_experience_matches(skill_root: Path, case_data: dict[str, Any]) -> dict[str, Any]:
    catalog_path = skill_root / DEFAULT_CATALOG
    try:
        catalog = load_catalog(catalog_path)
    except Exception:
        return {
            "status": "catalog_unavailable",
            "query": {"forms": [], "apps": [], "features": []},
            "matches": [],
            "guardrails": [
                "未找到深链路经验库，仍需按 pageId、变量、环境字段、断言顺序排查。",
            ],
        }
    return match_experience_catalog(catalog, case=case_data, limit=3)


def save_repair_evidence_package(package: dict[str, Any], output_dir: Path) -> Path:
    output_dir.mkdir(parents=True, exist_ok=True)
    task_id = _safe_name(package.get("task_id", "task"))
    case_name = _safe_name(package.get("case_name", "case"))
    path = output_dir / f"{task_id}_{case_name}.json"
    path.write_text(json.dumps(package, ensure_ascii=False, indent=2, default=str), encoding="utf-8")
    return path


def _find_case_result(report_data: dict[str, Any], case_name: str) -> dict[str, Any]:
    for item in report_data.get("case_results") or []:
        if item.get("name") == case_name:
            return item
    return {"name": case_name}


def _normalize_case_result_analysis(case_result: dict[str, Any], *, case_name: str) -> dict[str, Any]:
    """Reclassify old run histories whose stored failure_analysis was unknown."""
    if not isinstance(case_result, dict):
        return {"name": case_name}
    failure_analysis = case_result.get("failure_analysis") or {}
    if failure_analysis and failure_analysis.get("category") != "unknown":
        return case_result
    unknown_root = str(failure_analysis.get("root_cause") or "") if isinstance(failure_analysis, dict) else ""
    if failure_analysis and unknown_root and "暂未匹配" not in unknown_root and "未捕获" not in unknown_root:
        return case_result
    assertions = case_result.get("assertions") or []
    phases = case_result.get("phases") or []
    if not assertions and not phases and not case_result.get("error"):
        return case_result
    next_result = dict(case_result)
    failed_phases = [
        {
            **p,
            "ok": False,
            "error": p.get("error") or "; ".join(str(item) for item in (p.get("errors") or [])),
        }
        for p in phases
        if p.get("status") == "fail" and not str(p.get("id") or "").startswith("assert:")
    ]
    analysis = classify_run_failure(
        steps=failed_phases,
        assertions=assertions,
        case={"name": case_name},
    )
    next_result["failure_analysis"] = analysis
    next_result["error_category"] = analysis.get("category", next_result.get("error_category", ""))
    if analysis.get("category") == "readback_assertion_gap":
        next_result["next_action"] = "ai_agent"
        next_result["ai_reason"] = "保存/提交已执行，但通用入库回查未命中；需确认真实入库或补表单专用回查策略。"
    elif not next_result.get("ai_reason"):
        next_result["ai_reason"] = analysis.get("root_cause", "")
    return next_result


def _safe_name(value: str) -> str:
    safe = "".join(ch if ch.isalnum() or ch in "-_." else "_" for ch in str(value))
    return safe[:120] or "item"
