"""
数据访问对象（DAO）模块

提供对各个数据表的高级操作接口。
"""

from dataclasses import dataclass
from datetime import datetime
from typing import Optional, Any
import json

from .pool import DatabasePool, get_pool


@dataclass
class RunRecord:
    """运行记录"""
    id: str
    case_name: str
    case_id: Optional[int]
    task_id: Optional[str]
    env_id: str
    status: str
    passed: bool
    step_ok: int
    step_count: int
    duration_s: float
    error_message: Optional[str]
    started_at: str
    finished_at: Optional[str]


@dataclass
class TaskRecord:
    """任务记录"""
    id: str
    name: str
    status: str
    env_id: str
    total_count: int
    passed_count: int
    failed_count: int
    duration_s: float
    created_at: str
    started_at: Optional[str]
    finished_at: Optional[str]


class RunHistoryDAO:
    """运行历史数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def create(self, run_id: str, case_name: str, env_id: str,
               case_id: int = None, task_id: str = None) -> str:
        """创建运行记录"""
        self.pool.execute("""
            INSERT INTO run_history (id, case_name, case_id, task_id, env_id, status)
            VALUES (?, ?, ?, ?, ?, 'running')
        """, (run_id, case_name, case_id, task_id, env_id))
        return run_id
    
    def update_status(self, run_id: str, status: str, passed: bool,
                      step_ok: int, step_count: int, duration_s: float,
                      error_message: str = None):
        """更新运行状态"""
        finished_at = datetime.now().isoformat() if status in ("passed", "failed", "error") else None
        
        self.pool.execute("""
            UPDATE run_history 
            SET status=?, passed=?, step_ok=?, step_count=?, 
                duration_s=?, error_message=?, finished_at=?
            WHERE id=?
        """, (status, int(passed), step_ok, step_count, duration_s, error_message, finished_at, run_id))
    
    def get_by_id(self, run_id: str) -> Optional[RunRecord]:
        """获取运行记录"""
        row = self.pool.query_one("SELECT * FROM run_history WHERE id=?", (run_id,))
        if row:
            return RunRecord(
                id=row["id"],
                case_name=row["case_name"],
                case_id=row["case_id"],
                task_id=row["task_id"],
                env_id=row["env_id"],
                status=row["status"],
                passed=bool(row["passed"]),
                step_ok=row["step_ok"],
                step_count=row["step_count"],
                duration_s=row["duration_s"],
                error_message=row["error_message"],
                started_at=row["started_at"],
                finished_at=row["finished_at"]
            )
        return None
    
    def list_by_case(self, case_name: str, limit: int = 50) -> list[RunRecord]:
        """按用例查询历史"""
        rows = self.pool.query_all("""
            SELECT * FROM run_history 
            WHERE case_name=? 
            ORDER BY started_at DESC 
            LIMIT ?
        """, (case_name, limit))
        return [self._row_to_record(r) for r in rows]
    
    def list_recent(self, limit: int = 100) -> list[RunRecord]:
        """最近运行列表"""
        rows = self.pool.query_all("""
            SELECT * FROM run_history 
            ORDER BY started_at DESC 
            LIMIT ?
        """, (limit,))
        return [self._row_to_record(r) for r in rows]
    
    def get_stats(self) -> dict:
        """获取统计信息"""
        total = self.pool.query_count("SELECT COUNT(*) FROM run_history")
        passed = self.pool.query_count("SELECT COUNT(*) FROM run_history WHERE passed=1")
        failed = total - passed
        
        # 最近7天统计
        recent = self.pool.query_all("""
            SELECT date(started_at) as day, 
                   COUNT(*) as total,
                   SUM(passed) as passed
            FROM run_history
            WHERE started_at >= datetime('now', '-7 days')
            GROUP BY day
            ORDER BY day DESC
        """)
        
        return {
            "total_runs": total,
            "passed": passed,
            "failed": failed,
            "pass_rate": round(passed * 100.0 / total, 1) if total > 0 else 0,
            "daily_stats": recent
        }
    
    def _row_to_record(self, row: dict) -> RunRecord:
        return RunRecord(
            id=row["id"],
            case_name=row["case_name"],
            case_id=row["case_id"],
            task_id=row["task_id"],
            env_id=row["env_id"],
            status=row["status"],
            passed=bool(row["passed"]),
            step_ok=row["step_ok"],
            step_count=row["step_count"],
            duration_s=row["duration_s"],
            error_message=row["error_message"],
            started_at=row["started_at"],
            finished_at=row["finished_at"]
        )


class TaskDAO:
    """任务数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def create(self, task_id: str, name: str, case_names: list[str],
               env_id: str) -> str:
        """创建任务"""
        self.pool.execute("""
            INSERT INTO tasks (id, name, env_id, total_count, status)
            VALUES (?, ?, ?, ?, 'pending')
        """, (task_id, name, env_id, len(case_names)))
        
        # 插入任务用例关联
        for case_name in case_names:
            self.pool.execute("""
                INSERT INTO task_cases (task_id, case_name, case_id)
                SELECT ?, ?, id FROM cases WHERE name=?
            """, (task_id, case_name, case_name))
        
        return task_id
    
    def update_status(self, task_id: str, status: str):
        """更新任务状态"""
        now = datetime.now().isoformat()
        
        if status == "running":
            self.pool.execute("""
                UPDATE tasks SET status=?, started_at=? WHERE id=?
            """, (status, now, task_id))
        elif status == "completed":
            # 计算统计信息
            stats = self.pool.query_one("""
                SELECT 
                    COUNT(*) as total,
                    SUM(CASE WHEN status='passed' THEN 1 ELSE 0 END) as passed,
                    SUM(CASE WHEN status='failed' THEN 1 ELSE 0 END) as failed,
                    SUM(duration_s) as total_duration
                FROM task_cases WHERE task_id=?
            """, (task_id,))
            
            self.pool.execute("""
                UPDATE tasks 
                SET status=?, finished_at=?, 
                    passed_count=?, failed_count=?, duration_s=?
                WHERE id=?
            """, (status, now, stats["passed"], stats["failed"], 
                  stats["total_duration"] or 0, task_id))
        else:
            self.pool.execute("""
                UPDATE tasks SET status=? WHERE id=?
            """, (status, task_id))
    
    def get_by_id(self, task_id: str) -> Optional[TaskRecord]:
        """获取任务"""
        row = self.pool.query_one("SELECT * FROM tasks WHERE id=?", (task_id,))
        if row:
            return TaskRecord(
                id=row["id"],
                name=row["name"],
                status=row["status"],
                env_id=row["env_id"],
                total_count=row["total_count"],
                passed_count=row["passed_count"],
                failed_count=row["failed_count"],
                duration_s=row["duration_s"],
                created_at=row["created_at"],
                started_at=row["started_at"],
                finished_at=row["finished_at"]
            )
        return None
    
    def list_recent(self, limit: int = 20) -> list[TaskRecord]:
        """列出最近任务"""
        rows = self.pool.query_all("""
            SELECT * FROM tasks 
            ORDER BY created_at DESC 
            LIMIT ?
        """, (limit,))
        return [self._row_to_record(r) for r in rows]
    
    def _row_to_record(self, row: dict) -> TaskRecord:
        return TaskRecord(
            id=row["id"],
            name=row["name"],
            status=row["status"],
            env_id=row["env_id"],
            total_count=row["total_count"],
            passed_count=row["passed_count"],
            failed_count=row["failed_count"],
            duration_s=row["duration_s"],
            created_at=row["created_at"],
            started_at=row["started_at"],
            finished_at=row["finished_at"]
        )


class LogDAO:
    """日志数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def insert(self, run_id: str, level: str, source: str, message: str, ts: float = None):
        """插入日志"""
        if ts is None:
            ts = datetime.now().timestamp()
        
        self.pool.execute("""
            INSERT INTO logs (run_id, ts, level, source, message)
            VALUES (?, ?, ?, ?, ?)
        """, (run_id, ts, level, source, message))
    
    def search(self, query: str, limit: int = 100) -> list[dict]:
        """全文搜索"""
        return self.pool.query_all("""
            SELECT l.*, r.case_name
            FROM logs_fts fts
            JOIN logs l ON fts.rowid = l.id
            LEFT JOIN run_history r ON l.run_id = r.id
            WHERE logs_fts MATCH ?
            ORDER BY l.ts DESC
            LIMIT ?
        """, (query, limit))
    
    def list_by_run(self, run_id: str, level: str = None) -> list[dict]:
        """按运行查询日志"""
        if level:
            return self.pool.query_all("""
                SELECT * FROM logs 
                WHERE run_id=? AND level=?
                ORDER BY ts ASC
            """, (run_id, level))
        return self.pool.query_all("""
            SELECT * FROM logs WHERE run_id=? ORDER BY ts ASC
        """, (run_id,))
    
    def list_recent(self, level: str = None, limit: int = 500) -> list[dict]:
        """最近日志"""
        if level:
            return self.pool.query_all("""
                SELECT * FROM logs 
                WHERE level=?
                ORDER BY ts DESC 
                LIMIT ?
            """, (level, limit))
        return self.pool.query_all("""
            SELECT * FROM logs ORDER BY ts DESC LIMIT ?
        """, (limit,))


class CaseDAO:
    """用例数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def upsert(self, name: str, display_name: str = None, 
               description: str = None, file_path: str = None,
               tags: list[str] = None, step_count: int = 0) -> int:
        """创建或更新用例"""
        tags_json = json.dumps(tags) if tags else "[]"
        
        result = self.pool.execute("""
            INSERT INTO cases (name, display_name, description, file_path, tags, step_count)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(name) DO UPDATE SET
                display_name=excluded.display_name,
                description=excluded.description,
                file_path=excluded.file_path,
                tags=excluded.tags,
                step_count=excluded.step_count,
                updated_at=CURRENT_TIMESTAMP
            RETURNING id
        """, (name, display_name or name, description, file_path, tags_json, step_count))
        
        row = result.fetchone()
        return row[0] if row else self.get_id_by_name(name)
    
    def get_id_by_name(self, name: str) -> Optional[int]:
        """根据名称获取ID"""
        result = self.pool.query_one(
            "SELECT id FROM cases WHERE name=?", (name,)
        )
        return result["id"] if result else None
    
    def get_stats(self) -> list[dict]:
        """获取用例统计"""
        return self.pool.query_all("""
            SELECT * FROM case_stats
            ORDER BY total_runs DESC
        """)
    
    def list_all(self) -> list[dict]:
        """列出所有用例"""
        return self.pool.query_all("""
            SELECT c.*, 
                   s.total_runs, s.passed_runs, s.avg_duration, s.last_run
            FROM cases c
            LEFT JOIN case_stats s ON c.id = s.id
            ORDER BY c.name
        """)


class EnvDAO:
    """环境配置数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def upsert(self, env_id: str, name: str, base_url: str,
               datacenter_id: str = None, sign_required: bool = True,
               timeout: int = 30) -> str:
        """创建或更新环境"""
        self.pool.execute("""
            INSERT INTO envs (id, name, base_url, datacenter_id, sign_required, timeout)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                name=excluded.name,
                base_url=excluded.base_url,
                datacenter_id=excluded.datacenter_id,
                sign_required=excluded.sign_required,
                timeout=excluded.timeout,
                updated_at=CURRENT_TIMESTAMP
        """, (env_id, name, base_url, datacenter_id, int(sign_required), timeout))
        
        return env_id
    
    def set_credentials(self, env_id: str, username: str = None,
                        password_encrypted: str = None,
                        username_env: str = None,
                        password_env: str = None):
        """设置凭证"""
        self.pool.execute("""
            INSERT INTO env_credentials (env_id, username, password_encrypted, username_env, password_env)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(env_id) DO UPDATE SET
                username=excluded.username,
                password_encrypted=excluded.password_encrypted,
                username_env=excluded.username_env,
                password_env=excluded.password_env
        """, (env_id, username, password_encrypted, username_env, password_env))
    
    def get_by_id(self, env_id: str) -> Optional[dict]:
        """获取环境配置"""
        return self.pool.query_one(
            "SELECT * FROM env_full WHERE id=?", (env_id,)
        )
    
    def list_all(self) -> list[dict]:
        """列出所有环境"""
        return self.pool.query_all("SELECT * FROM env_full ORDER BY name")
    
    def delete(self, env_id: str):
        """删除环境（级联删除凭证和基础数据）"""
        self.pool.execute("DELETE FROM envs WHERE id=?", (env_id,))


class StepResultDAO:
    """步骤结果数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def insert(self, run_id: str, step_id: str, step_type: str,
               step_detail: str = None, optional: bool = False,
               passed: bool = False, duration_ms: int = 0,
               error_message: str = None,
               request_snapshot: dict = None,
               response_snapshot: dict = None) -> int:
        """插入步骤结果"""
        req_json = json.dumps(request_snapshot) if request_snapshot else None
        resp_json = json.dumps(response_snapshot) if response_snapshot else None
        
        result = self.pool.execute("""
            INSERT INTO step_results 
            (run_id, step_id, step_type, step_detail, optional, passed, 
             duration_ms, error_message, request_snapshot, response_snapshot)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """, (run_id, step_id, step_type, step_detail, int(optional),
              int(passed), duration_ms, error_message, req_json, resp_json))
        
        row = result.fetchone()
        return row[0] if row else 0
    
    def list_by_run(self, run_id: str) -> list[dict]:
        """获取运行的所有步骤"""
        return self.pool.query_all("""
            SELECT * FROM step_results 
            WHERE run_id=? 
            ORDER BY id ASC
        """, (run_id,))
    
    def get_failed_steps(self, run_id: str) -> list[dict]:
        """获取失败步骤"""
        return self.pool.query_all("""
            SELECT * FROM step_results 
            WHERE run_id=? AND passed=0
            ORDER BY id ASC
        """, (run_id,))


class AssertionDAO:
    """断言结果数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def insert(self, run_id: str, assertion_type: str,
               passed: bool, message: str = None) -> int:
        """插入断言结果"""
        result = self.pool.execute("""
            INSERT INTO assertions (run_id, assertion_type, passed, message)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """, (run_id, assertion_type, int(passed), message))
        
        row = result.fetchone()
        return row[0] if row else 0
    
    def list_by_run(self, run_id: str) -> list[dict]:
        """获取运行的所有断言"""
        return self.pool.query_all("""
            SELECT * FROM assertions 
            WHERE run_id=? 
            ORDER BY id ASC
        """, (run_id,))


class FixSuggestionDAO:
    """修复建议数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def insert(self, run_id: str, error_type: str = None,
               field_key: str = None, field_caption: str = None,
               diagnosis: str = None, suggested_value: str = None,
               patch_yaml: str = None, confidence: float = 0) -> int:
        """插入修复建议"""
        result = self.pool.execute("""
            INSERT INTO fix_suggestions 
            (run_id, error_type, field_key, field_caption, diagnosis,
             suggested_value, patch_yaml, confidence)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """, (run_id, error_type, field_key, field_caption, diagnosis,
              suggested_value, patch_yaml, confidence))
        
        row = result.fetchone()
        return row[0] if row else 0
    
    def mark_applied(self, suggestion_id: int):
        """标记为已应用"""
        self.pool.execute(
            "UPDATE fix_suggestions SET applied=1 WHERE id=?",
            (suggestion_id,)
        )
    
    def list_by_run(self, run_id: str) -> list[dict]:
        """获取运行的修复建议"""
        return self.pool.query_all("""
            SELECT * FROM fix_suggestions 
            WHERE run_id=? 
            ORDER BY confidence DESC
        """, (run_id,))


class ReportDAO:
    """执行报告数据访问"""
    
    def __init__(self, pool: DatabasePool = None):
        self.pool = pool or get_pool()
    
    def create(self, task_id: str) -> str:
        """创建报告"""
        report_id = f"rpt_{task_id}"
        
        # 汇总数据
        stats = self.pool.query_one("""
            SELECT 
                COUNT(*) as total_cases,
                SUM(CASE WHEN status='passed' THEN 1 ELSE 0 END) as passed_cases,
                SUM(CASE WHEN status='failed' THEN 1 ELSE 0 END) as failed_cases,
                SUM(step_ok) as passed_steps,
                SUM(step_count - step_ok) as failed_steps,
                SUM(step_count) as total_steps,
                SUM(duration_s) as total_duration
            FROM task_cases 
            WHERE task_id=?
        """, (task_id,))
        
        # 计算通过率
        pass_rate = 0
        if stats["total_cases"] > 0:
            pass_rate = round(stats["passed_cases"] * 100.0 / stats["total_cases"], 1)
        
        # 收集错误汇总
        errors = self.pool.query_all("""
            SELECT case_name, error_message 
            FROM task_cases 
            WHERE task_id=? AND status='failed'
            LIMIT 10
        """, (task_id,))
        
        error_summary = json.dumps([dict(e) for e in errors])
        
        self.pool.execute("""
            INSERT INTO reports 
            (id, task_id, total_cases, passed_cases, failed_cases,
             total_steps, passed_steps, failed_steps, total_duration_s,
             pass_rate, error_summary)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (report_id, task_id, stats["total_cases"], stats["passed_cases"],
              stats["failed_cases"], stats["total_steps"], stats["passed_steps"],
              stats["failed_steps"], stats["total_duration"] or 0,
              pass_rate, error_summary))
        
        return report_id
    
    def get_by_task(self, task_id: str) -> Optional[dict]:
        """获取任务报告"""
        return self.pool.query_one(
            "SELECT * FROM reports WHERE task_id=?", (task_id,)
        )
    
    def list_recent(self, limit: int = 20) -> list[dict]:
        """列出最近报告"""
        return self.pool.query_all("""
            SELECT r.*, t.name as task_name, t.env_id
            FROM reports r
            JOIN tasks t ON r.task_id = t.id
            ORDER BY r.generated_at DESC
            LIMIT ?
        """, (limit,))