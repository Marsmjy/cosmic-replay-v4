"""
任务管理与执行报告系统
参考 pytest/Jenkins 设计，支持批量执行、任务跟踪、执行报告生成

功能：
1. 任务创建与管理 - 创建执行任务，跟踪状态
2. 批量执行 - 并发执行多个用例
3. 执行报告 - 生成详细报告，包含统计信息
4. 历史查询 - 查看历史执行记录和报告
"""

from __future__ import annotations

import json
import time
import uuid
from collections import OrderedDict
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path
from typing import Any
import threading


@dataclass
class CaseResult:
    """单个用例的执行结果"""
    name: str
    passed: bool
    run_id: str = ""  # 执行ID，用于跳转到执行历史
    step_ok: int = 0
    step_count: int = 0
    duration_s: float = 0.0
    error: str = ""
    error_category: str = ""  # business / technical / environment / framework / ""(无错误)
    phases: list[dict] = field(default_factory=list)  # 执行阶段详情
    assertions: list[dict] = field(default_factory=list)
    failure_analysis: dict = field(default_factory=dict)
    repair_plan: list[dict] = field(default_factory=list)
    env_fields: list[dict] = field(default_factory=list)
    runtime_evidence: dict = field(default_factory=dict)
    write_status: str = "not_checked"  # verified(readback) / unverified / failed / not_applicable / not_checked
    write_evidence: dict = field(default_factory=dict)
    write_verification: dict = field(default_factory=dict)
    next_action: str = "none"  # none / auto_repair / manual_confirm / ai_agent
    ai_reason: str = ""
    decision_summary: dict = field(default_factory=dict)
    
    def to_dict(self) -> dict:
        return {
            "name": self.name,
            "passed": self.passed,
            "run_id": self.run_id,
            "step_ok": self.step_ok,
            "step_count": self.step_count,
            "duration_s": self.duration_s,
            "error": self.error,
            "error_category": self.error_category,
            "phases": self.phases[:20] if self.phases else [],
            "assertions": self.assertions[:10] if self.assertions else [],
            "failure_analysis": self.failure_analysis,
            "repair_plan": self.repair_plan,
            "env_fields": self.env_fields[:20] if self.env_fields else [],
            "runtime_evidence": self.runtime_evidence,
            "write_status": self.write_status,
            "write_evidence": self.write_evidence,
            "write_verification": self.write_verification,
            "next_action": self.next_action,
            "ai_reason": self.ai_reason,
            "decision_summary": self.decision_summary,
        }


@dataclass
class ExecutionTask:
    """执行任务"""
    task_id: str
    name: str = ""
    case_names: list[str] = field(default_factory=list)
    env_id: str = "sit"
    concurrency: int = 3  # 并发执行数
    status: str = "pending"  # pending | running | completed | cancelled
    created_at: str = ""
    started_at: str = ""
    finished_at: str = ""
    results: list[CaseResult] = field(default_factory=list)
    
    def __post_init__(self):
        if not self.task_id:
            self.task_id = f"task_{int(time.time()*1000)}"
        if not self.created_at:
            self.created_at = datetime.now().isoformat()
    
    @property
    def total_count(self) -> int:
        return len(self.case_names)
    
    @property
    def passed_count(self) -> int:
        return sum(1 for r in self.results if r.passed)
    
    @property
    def failed_count(self) -> int:
        return sum(1 for r in self.results if not r.passed)
    
    @property
    def duration_s(self) -> float:
        return sum(r.duration_s for r in self.results)
    
    @property
    def pass_rate(self) -> float:
        if not self.results:
            return 0.0
        return self.passed_count / len(self.results) * 100
    
    def to_dict(self) -> dict:
        return {
            "task_id": self.task_id,
            "name": self.name,
            "case_names": self.case_names,
            "env_id": self.env_id,
            "concurrency": self.concurrency,
            "status": self.status,
            "created_at": self.created_at,
            "started_at": self.started_at,
            "finished_at": self.finished_at,
            "total_count": self.total_count,
            "passed_count": self.passed_count,
            "failed_count": self.failed_count,
            "duration_s": round(self.duration_s, 2),
            "pass_rate": round(self.pass_rate, 1),
            "results": [r.to_dict() for r in self.results],
        }


@dataclass
class ExecutionReport:
    """执行报告"""
    report_id: str
    task_id: str
    task_name: str = ""
    generated_at: str = ""
    env: str = "sit"
    
    # 汇总统计
    total_cases: int = 0
    passed_cases: int = 0
    failed_cases: int = 0
    skipped_cases: int = 0
    total_steps: int = 0
    passed_steps: int = 0
    failed_steps: int = 0
    total_duration_s: float = 0.0
    pass_rate: float = 0.0
    
    # 用例详情
    case_results: list[dict] = field(default_factory=list)
    
    # 错误汇总
    errors: list[dict] = field(default_factory=list)
    
    # 错误分类统计
    error_breakdown: dict = field(default_factory=dict)  # {"business": 2, "technical": 1, ...}
    
    # 性能指标
    avg_step_duration_s: float = 0.0
    slowest_cases: list = field(default_factory=list)  # [{"name": "xxx", "duration_s": 13.9}, ...]
    fastest_cases: list = field(default_factory=list)   # [{"name": "xxx", "duration_s": 1.2}, ...]

    # 产品化验收
    acceptance: dict = field(default_factory=dict)
    action_queues: dict = field(default_factory=dict)
    
    def __post_init__(self):
        if not self.report_id:
            self.report_id = f"rpt_{int(time.time()*1000)}"
        if not self.generated_at:
            self.generated_at = datetime.now().isoformat()
    
    def to_dict(self) -> dict:
        return {
            "report_id": self.report_id,
            "task_id": self.task_id,
            "task_name": self.task_name,
            "generated_at": self.generated_at,
            "env": self.env,
            "summary": {
                "total_cases": self.total_cases,
                "passed_cases": self.passed_cases,
                "failed_cases": self.failed_cases,
                "skipped_cases": self.skipped_cases,
                "total_steps": self.total_steps,
                "passed_steps": self.passed_steps,
                "failed_steps": self.failed_steps,
                "total_duration_s": round(self.total_duration_s, 2),
                "pass_rate": round(self.pass_rate, 4),
            },
            "case_results": self.case_results,
            "errors": self.errors,
            "error_breakdown": self.error_breakdown,
            "performance": {
                "avg_step_duration_s": self.avg_step_duration_s,
                "slowest_cases": self.slowest_cases,
                "fastest_cases": self.fastest_cases,
            },
            "acceptance": self.acceptance,
            "action_queues": self.action_queues,
        }


class TaskManager:
    """任务管理器"""
    
    def __init__(self, max_tasks: int = 100):
        self.max_tasks = max_tasks
        self._tasks: OrderedDict[str, ExecutionTask] = OrderedDict()
        self._reports: OrderedDict[str, ExecutionReport] = OrderedDict()
        self._lock = threading.Lock()
    
    def create_task(self, case_names: list[str], env_id: str = "sit", name: str = "") -> ExecutionTask:
        """创建新任务"""
        with self._lock:
            task_id = f"task_{int(time.time()*1000)}_{uuid.uuid4().hex[:6]}"
            if not name:
                name = f"批量执行 ({len(case_names)}个用例)"
            
            task = ExecutionTask(
                task_id=task_id,
                name=name,
                case_names=case_names,
                env_id=env_id,
                status="pending",
            )
            self._tasks[task_id] = task
            
            # 清理旧任务
            if len(self._tasks) > self.max_tasks:
                oldest = next(iter(self._tasks))
                del self._tasks[oldest]
            
            return task
    
    def get_task(self, task_id: str) -> ExecutionTask | None:
        """获取任务"""
        with self._lock:
            return self._tasks.get(task_id)
    
    def list_tasks(self, limit: int = 20) -> list[dict]:
        """列出最近的任务"""
        with self._lock:
            tasks = list(self._tasks.values())[-limit:]
            return [t.to_dict() for t in reversed(tasks)]
    
    def update_task_status(self, task_id: str, status: str):
        """更新任务状态"""
        with self._lock:
            task = self._tasks.get(task_id)
            if task:
                task.status = status
                if status == "running":
                    task.started_at = datetime.now().isoformat()
                elif status in ("completed", "cancelled"):
                    task.finished_at = datetime.now().isoformat()
    
    def add_result(self, task_id: str, result: CaseResult):
        """添加执行结果"""
        with self._lock:
            task = self._tasks.get(task_id)
            if task:
                task.results.append(result)
    
    def generate_report(self, task_id: str) -> ExecutionReport | None:
        """生成执行报告"""
        with self._lock:
            task = self._tasks.get(task_id)
            if not task:
                return None
        
        report = ExecutionReport(
            report_id=f"rpt_{task_id}",
            task_id=task_id,
            task_name=task.name,
            env=task.env_id,
        )
        
        # 统计数据
        report.total_cases = len(task.results)
        report.passed_cases = sum(1 for r in task.results if r.passed)
        report.failed_cases = report.total_cases - report.passed_cases
        report.total_steps = sum(r.step_count for r in task.results)
        report.passed_steps = sum(r.step_ok for r in task.results)
        report.failed_steps = report.total_steps - report.passed_steps
        report.total_duration_s = sum(r.duration_s for r in task.results)
        report.pass_rate = (report.passed_cases / report.total_cases) if report.total_cases > 0 else 0
        
        # 用例详情
        for result in task.results:
            enrich_case_result(result)
        report.case_results = [r.to_dict() for r in task.results]
        
        # 错误汇总
        for r in task.results:
            if not r.passed and r.error:
                report.errors.append({
                    "case": r.name,
                    "error": r.error[:200],  # 截断错误信息
                    "step_count": r.step_count,
                    "step_ok": r.step_ok,
                })
        
        # 错误分类
        error_breakdown: dict[str, int] = {}
        for r in task.results:
            if not r.passed and r.error:
                err_lower = r.error.lower()
                if any(kw in err_lower for kw in ("assertion", "断言", "assert")):
                    category = "business"
                elif any(kw in err_lower for kw in ("connection", "connect", "login", "登录", "timeout", "refused")):
                    category = "environment"
                elif any(kw in err_lower for kw in ("4xx", "5xx", "http", "status", "500", "404", "403")):
                    category = "technical"
                elif any(kw in err_lower for kw in ("parse", "config", "解析", "配置", "key", "field")):
                    category = "framework"
                else:
                    category = "technical"
                r.error_category = category
                error_breakdown[category] = error_breakdown.get(category, 0) + 1
        report.error_breakdown = error_breakdown
        
        # 性能指标
        if report.total_steps > 0:
            report.avg_step_duration_s = round(report.total_duration_s / report.total_steps, 3)
        
        sorted_by_duration = sorted(task.results, key=lambda r: r.duration_s, reverse=True)
        report.slowest_cases = [{"name": r.name, "duration_s": r.duration_s} for r in sorted_by_duration[:3]]
        report.fastest_cases = [{"name": r.name, "duration_s": r.duration_s} for r in sorted_by_duration[-3:]]
        report.acceptance = build_acceptance_summary(task.results)
        report.action_queues = build_action_queues(task.results)
        
        # 保存报告
        with self._lock:
            self._reports[report.report_id] = report
            if len(self._reports) > self.max_tasks:
                oldest = next(iter(self._reports))
                del self._reports[oldest]
        
        return report
    
    def get_report(self, report_id: str) -> ExecutionReport | None:
        """获取报告"""
        with self._lock:
            return self._reports.get(report_id)
    
    def get_report_by_task(self, task_id: str) -> ExecutionReport | None:
        """根据任务ID获取报告"""
        report_id = f"rpt_{task_id}"
        return self.get_report(report_id)


# 全局任务管理器实例
TASK_MANAGER = TaskManager(max_tasks=100)


def enrich_case_result(result: CaseResult) -> None:
    """Attach write verification and next-action metadata to a case result."""
    result.write_status, result.write_evidence = infer_write_status(result)
    if (
        result.passed
        and result.write_status == "unverified"
        and (result.write_verification or {}).get("manual_confirmed")
    ):
        result.write_status = "manual_verified"
        result.write_evidence.setdefault("signals", []).append("manual_confirmed")
        result.write_evidence["manual_confirmed"] = True
        result.next_action = "none"
        result.ai_reason = ""
        return
    if result.passed and result.write_status == "unverified":
        result.next_action = "ai_agent"
        if (result.write_evidence or {}).get("response_verified"):
            result.ai_reason = "保存/提交响应已有成功证据，但尚未通过业务键只读回查确认真实入库。"
        else:
            result.ai_reason = "执行 PASS 但保存/提交响应缺少明确写入和入库证据，需排查 pageId 链路或补入库断言。"
    elif not result.passed:
        safe_repairs = [r for r in result.repair_plan or [] if r.get("safe_to_apply")]
        needs_confirm = [r for r in result.repair_plan or [] if not r.get("safe_to_apply")]
        category = (result.failure_analysis or {}).get("category") or result.error_category
        if safe_repairs:
            result.next_action = "auto_repair"
        elif needs_confirm:
            result.next_action = "manual_confirm"
        elif category == "readback_assertion_gap":
            result.next_action = "ai_agent"
            result.ai_reason = "保存/提交已执行，但通用入库回查未命中；需确认真实入库或补表单专用回查策略。"
        elif category in {"pageid_context", "unknown", "assertion_anchor_missing"}:
            result.next_action = "ai_agent"
            result.ai_reason = f"系统规则未生成安全修复，失败类型为 {category or 'unknown'}。"
        else:
            result.next_action = "manual_confirm"
    else:
        result.next_action = "none"
    result.decision_summary = build_decision_summary(result)


def infer_write_status(result: CaseResult) -> tuple[str, dict]:
    """Infer whether a passed case has enough evidence that data was written."""
    write_phases = [p for p in result.phases or [] if _is_write_phase(p)]
    evidence = {
        "write_step_count": len(write_phases),
        "checked": bool(write_phases),
        "signals": [],
    }
    if not write_phases:
        return "not_applicable", evidence
    if not result.passed:
        evidence["signals"].append("case_failed")
        return "failed", evidence
    if _has_runtime_upload_consumption_assertion(result):
        evidence["signals"].append("assertion:runtime_upload_consumed")
    if _has_verified_attachment_readback_assertion(result):
        evidence["signals"].append("assertion:readback_runtime_upload")
        return "verified", evidence
    if _has_verified_readback_assertion(result):
        evidence["signals"].append("assertion:readback_by_business_key")
        return "verified", evidence

    response_evidence_found = False
    response_contract_results = (result.runtime_evidence or {}).get("response_contract_results") or {}
    for phase in write_phases:
        phase_id = str(phase.get("id") or "")
        step_id = phase_id[5:] if phase_id.startswith("step:") else phase_id
        contract = (
            response_contract_results.get(step_id)
            or response_contract_results.get(phase_id)
            or {}
        )
        if isinstance(contract, dict) and contract.get("errors"):
            evidence["signals"].append(f"{phase.get('id', '')}:response_contract_failed")
            evidence["response_contract_errors"] = list(contract.get("errors") or [])[:5]
            return "failed", evidence
        if isinstance(contract, dict) and contract.get("contract_level") and not contract.get("errors"):
            evidence["signals"].append(f"{phase.get('id', '')}:response_contract_ok")
            response_evidence_found = True
        response = phase.get("response")
        text = _response_text(response)
        compact_text = "".join(text.split()).lower()
        if _is_empty_response(response, text):
            evidence["signals"].append(f"{phase.get('id', '')}:empty_response")
            continue
        if _contains_any(
            compact_text,
            ("无效请求", "非法请求", "invalidrequest", "csrf", "signature"),
        ):
            evidence["signals"].append(f"{phase.get('id', '')}:invalid_request")
            return "failed", evidence
        if _contains_any(
            compact_text,
            (
                "pkvalue",
                '"fid"',
                "billid",
                '"id":',
                "saveresult",
                '"success":true',
                "'success':true",
                "保存成功",
                "操作成功",
            ),
        ):
            evidence["signals"].append(f"{phase.get('id', '')}:write_response_token")
            response_evidence_found = True
            continue
        if _contains_any(
            compact_text,
            ("bos_operationresult", "showfieldtips", '"success":false', "'success':false"),
        ):
            evidence["signals"].append(f"{phase.get('id', '')}:failure_dialog")
            return "failed", evidence
        evidence["signals"].append(f"{phase.get('id', '')}:non_empty_response")
    if response_evidence_found:
        evidence["response_verified"] = True
    return "unverified", evidence


def build_decision_summary(result: CaseResult) -> dict:
    """Return a concise user-facing diagnosis and next-step summary."""
    runtime_evidence = result.runtime_evidence or {}
    capability = runtime_evidence.get("capability") or (
        runtime_evidence.get("case_contract") or {}
    ).get("capability") or {}
    env_plan = runtime_evidence.get("environment_binding_plan") or (
        runtime_evidence.get("case_contract") or {}
    ).get("environment_binding_plan") or {}
    field_binding_plan = runtime_evidence.get("maintainable_field_binding_plan") or (
        runtime_evidence.get("case_contract") or {}
    ).get("maintainable_field_binding_plan") or {}
    dynamic_plan = runtime_evidence.get("runtime_value_flow_plan") or (
        runtime_evidence.get("case_contract") or {}
    ).get("runtime_value_flow_plan") or {}
    failure_category = (result.failure_analysis or {}).get("category") or result.error_category or ""
    write_status = result.write_status

    unresolved_env_fields = [
        item for item in (env_plan.get("fields") or [])
        if isinstance(item, dict)
        and item.get("required")
        and item.get("status") in {"missing", "unresolved"}
    ]
    overridden_unbound_fields = [
        item for item in (field_binding_plan.get("fields") or [])
        if isinstance(item, dict)
        and item.get("user_overridden")
        and item.get("status") == "unbound"
    ]
    dynamic_warnings = [
        item for item in (dynamic_plan.get("warnings") or [])
        if isinstance(item, dict)
    ]
    response_contract_failures = [
        {"step_id": step_id, "errors": item.get("errors") or []}
        for step_id, item in ((runtime_evidence.get("response_contract_results") or {}).items())
        if isinstance(item, dict) and item.get("errors")
    ]

    if capability.get("status") == "unsupported":
        category = "unsupported"
        title = "当前场景超出 HAR 回放核心边界"
        next_step = "不要让 AI 幻觉补全业务链路；先确认是否拆成专项能力或人工测试。"
        confidence = "high"
    elif overridden_unbound_fields:
        category = "maintainable_value_unbound"
        title = "用户维护值没有进入可执行步骤"
        next_step = "交给 AI 修字段解析或绑定规则；无需先排查目标环境。"
        confidence = "high"
    elif unresolved_env_fields:
        category = "environment_binding"
        title = "目标环境字段尚未完成解析"
        next_step = "优先确认目标环境是否存在对应人员、组织、基础资料、权限或菜单入口。"
        confidence = "high"
    elif response_contract_failures:
        category = "script_or_environment_contract_drift"
        title = "关键接口响应与录制语义不一致"
        next_step = "先对比录制和回放的关键接口；若业务页面也异常则查环境，否则交给 AI 修 pageId/字段解析。"
        confidence = "high"
    elif failure_category in {"invalid_request_or_session", "navigation_or_environment_service", "server_stack_exception"}:
        category = "environment_or_session"
        title = "更像环境、会话、权限或后端服务问题"
        next_step = "先在目标环境手工确认页面和账号权限；环境正常后再让 AI 修用例。"
        confidence = "medium"
    elif failure_category in {"pageid_context", "assertion_anchor_missing"}:
        category = "script_chain"
        title = "更像回放链路或 pageId 上下文问题"
        next_step = "交给 AI 检查 pageId、L2/L3、弹窗、F7 和动态值生产消费链路。"
        confidence = "high"
    elif dynamic_warnings:
        category = "runtime_value_flow"
        title = "运行时动态值链路存在风险"
        next_step = "检查保存后 ID、单号、审批任务、回调值是否由前序响应产生并传给后续步骤。"
        confidence = "medium"
    elif result.passed and write_status == "unverified":
        category = "write_unverified"
        title = "执行通过但缺少入库证据"
        next_step = "补业务键只读回查，或由人工确认后再标记为已验证。"
        confidence = "high"
    elif result.passed:
        category = "ok"
        title = "执行结果满足当前校验门槛"
        next_step = ""
        confidence = "high"
    else:
        category = failure_category or "unknown"
        title = "失败原因需要进一步诊断"
        next_step = result.ai_reason or "交给 AI 读取证据包并按 pageId、变量、环境字段、断言顺序排查。"
        confidence = "low"

    return {
        "category": category,
        "title": title,
        "confidence": confidence,
        "next_step": next_step,
        "capability_status": capability.get("status", ""),
        "flow_kind": capability.get("flow_kind", ""),
        "write_status": write_status,
        "unresolved_env_field_count": len(unresolved_env_fields),
        "dynamic_warning_count": len(dynamic_warnings),
        "response_contract_failure_count": len(response_contract_failures),
    }


def build_acceptance_summary(results: list[CaseResult]) -> dict:
    total = len(results)
    passed = sum(1 for r in results if r.passed)
    failed = total - passed
    write_unverified = sum(1 for r in results if r.write_status == "unverified")
    write_verified = sum(1 for r in results if r.write_status in {"verified", "manual_verified"})
    auto_repairable = sum(1 for r in results if r.next_action == "auto_repair")
    manual_confirm = sum(1 for r in results if r.next_action == "manual_confirm")
    ai_required = sum(1 for r in results if r.next_action == "ai_agent")
    status = "ready" if failed == 0 and write_unverified == 0 else (
        "needs_ai" if ai_required else "needs_repair"
    )
    if total == 0:
        title = "暂无执行结果"
    elif status == "ready":
        title = "本次批量执行已通过验收"
    elif ai_required:
        title = "本次批量执行需要 AI 诊断介入"
    else:
        title = "本次批量执行存在待修复项"
    return {
        "status": status,
        "title": title,
        "total": total,
        "passed": passed,
        "failed": failed,
        "write_verified": write_verified,
        "write_unverified": write_unverified,
        "auto_repairable": auto_repairable,
        "manual_confirm": manual_confirm,
        "ai_required": ai_required,
        "summary_text": (
            f"共 {total} 条，通过 {passed} 条，失败 {failed} 条；"
            f"入库已验证 {write_verified} 条，入库未验证 {write_unverified} 条；"
            f"可自动修复 {auto_repairable} 条，需确认 {manual_confirm} 条，需 AI 诊断 {ai_required} 条。"
        ),
    }


def build_action_queues(results: list[CaseResult]) -> dict:
    queues = {"auto_repair": [], "manual_confirm": [], "ai_agent": [], "write_unverified": []}
    for result in results:
        decision_summary = result.decision_summary or build_decision_summary(result)
        item = {
            "name": result.name,
            "run_id": result.run_id,
            "error": result.error,
            "write_status": result.write_status,
            "reason": result.ai_reason or (result.failure_analysis or {}).get("root_cause", ""),
            "decision_summary": decision_summary,
            "decision_category": decision_summary.get("category", ""),
            "decision_title": decision_summary.get("title", ""),
            "next_step": decision_summary.get("next_step", ""),
        }
        if result.next_action in queues:
            queues[result.next_action].append(item)
        if result.write_status == "unverified":
            queues["write_unverified"].append(item)
    return queues


def _is_write_phase(phase: dict) -> bool:
    text = " ".join(
        str(phase.get(k) or "") for k in ("id", "label", "detail")
    ).lower()
    req_text = _response_text(phase.get("resolved_request")).lower()
    return _contains_any(
        text + " " + req_text,
        ("save", "submit", "保存", "提交", "btnsave", "saveandeffect", "submitandeffect"),
    )


def _has_verified_readback_assertion(result: CaseResult) -> bool:
    for assertion in result.assertions or []:
        if assertion.get("type") == "readback_by_business_key" and assertion.get("ok"):
            return True
    return False


def _has_runtime_upload_consumption_assertion(result: CaseResult) -> bool:
    for assertion in result.assertions or []:
        if assertion.get("type") == "runtime_upload_consumed" and assertion.get("ok"):
            return True
    return False


def _has_verified_attachment_readback_assertion(result: CaseResult) -> bool:
    for assertion in result.assertions or []:
        if assertion.get("type") in {"readback_runtime_upload", "readback_uploaded_attachment"}:
            if assertion.get("ok") and not assertion.get("advisory"):
                return True
    return False


def _response_text(value: Any) -> str:
    if value is None:
        return ""
    if isinstance(value, str):
        return value
    try:
        return json.dumps(value, ensure_ascii=False, default=str)
    except Exception:
        return str(value)


def _is_empty_response(response: Any, text: str) -> bool:
    if response is None:
        return True
    if response == [] or response == {}:
        return True
    return text.strip() in {"", "[]", "{}"}


def _contains_any(text: str, patterns: tuple[str, ...]) -> bool:
    return any(pattern in text for pattern in patterns)
