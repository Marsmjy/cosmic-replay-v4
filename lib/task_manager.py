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
    phases: list[dict] = field(default_factory=list)  # 执行阶段详情
    
    def to_dict(self) -> dict:
        return {
            "name": self.name,
            "passed": self.passed,
            "run_id": self.run_id,
            "step_ok": self.step_ok,
            "step_count": self.step_count,
            "duration_s": self.duration_s,
            "error": self.error,
            "phases": self.phases[:5] if self.phases else [],  # 只保留前5个关键阶段
        }


@dataclass
class ExecutionTask:
    """执行任务"""
    task_id: str
    name: str = ""
    case_names: list[str] = field(default_factory=list)
    env_id: str = "sit"
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
                "pass_rate": round(self.pass_rate, 1),
            },
            "case_results": self.case_results,
            "errors": self.errors,
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
        report.pass_rate = (report.passed_cases / report.total_cases * 100) if report.total_cases > 0 else 0
        
        # 用例详情
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
