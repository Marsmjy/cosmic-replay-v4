"""FastAPI 后端 - cosmic-replay Web UI

Endpoint 清单：
  GET  /                        静态 index.html
  GET  /api/info                启动信息（版本/路径）
  GET  /api/config              读配置（含所有环境）
  PUT  /api/config/webui        保存 webui 偏好
  GET  /api/envs                列环境
  PUT  /api/envs/{id}           保存/新建环境
  DELETE /api/envs/{id}         删环境
  GET  /api/cases               列所有用例 YAML
  GET  /api/cases/{name}/yaml   读 YAML 源
  PUT  /api/cases/{name}/yaml   保存 YAML
  POST /api/cases/{name}/run    触发运行（返回 run_id）
  GET  /api/runs/{run_id}/events  SSE 事件流
  POST /api/har/preview         HAR → 预览结构
  POST /api/har/extract         HAR → YAML 用例
  GET  /api/field-labels        字段标签映射（KB+静态合并）

设计原则：
- 后端只调 lib 的现成函数（Config/runner/har_extractor），不重复业务
- 运行用例用后台线程，SSE 推事件
- 所有路径相对 skill 根（CONFIG.webui.cases_dir 等）
"""
from __future__ import annotations

import argparse
import asyncio
import sys as _sys

# Windows ProactorEventLoop 对大体积 HTTP body 读取极慢，
# 必须在任何 asyncio event loop 创建之前切到 Selector。
if _sys.platform == "win32":
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())

import json
import os
import queue
import re
import shutil
import sys
import threading
import time
import uuid
import webbrowser
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import date, datetime

# ⭐ 导入任务管理器
from lib.task_manager import TASK_MANAGER, CaseResult, ExecutionTask
# ⭐ 导入任务 DAO（持久化）
from lib.db.dao import TaskDAO, ReportDAO


def _load_dotenv():
    """从项目根目录加载 .env 文件到 os.environ（不覆盖已有值）"""
    dotenv_path = Path(__file__).resolve().parent.parent.parent / ".env"
    if dotenv_path.exists():
        for line in dotenv_path.read_text(encoding="utf-8").splitlines():
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if "=" not in line:
                continue
            key, _, val = line.partition("=")
            key, val = key.strip(), val.strip().strip("\"'").strip()
            if key and val and key not in os.environ:
                os.environ[key] = val


class _SafeEncoder(json.JSONEncoder):
    """JSON encoder that handles datetime.date / datetime objects gracefully."""
    def default(self, o):
        if isinstance(o, (date, datetime)):
            return o.isoformat()
        return super().default(o)


def _safe_json_dumps(obj, **kwargs):
    """json.dumps with date-safe encoder."""
    return json.dumps(obj, cls=_SafeEncoder, ensure_ascii=False, **kwargs)
from pathlib import Path
from typing import Any

try:
    from fastapi import FastAPI, HTTPException, UploadFile, File, Body, Request
    from fastapi.responses import FileResponse, StreamingResponse, JSONResponse, HTMLResponse
    from fastapi.staticfiles import StaticFiles
    import uvicorn
except ImportError:
    print("ERROR: 缺少 FastAPI 依赖。请执行：")
    print("  pip install fastapi uvicorn python-multipart")
    sys.exit(2)

# skill 内部模块
SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))

from lib.config import Config, CONFIG_DIR
from lib.runner import run_case, load_yaml
from lib import har_extractor
from lib.report_exporter import export_html
from lib.agent_evidence import build_repair_evidence_package, save_repair_evidence_package
from lib.session_manager import SESSION_MGR
from lib.webui.log_store import LogStore, install_global_capture
from lib.notifier import EmailNotifier


# ============================================================
# 全局状态
# ============================================================
CONFIG = Config()
APP = FastAPI(title="cosmic-replay", version="0.1.0")
_start_time = time.time()  # ⭐ P0-3: 启动时间，用于健康检查

# 邮件通知
_notification_config = {}
try:
    _notification_config = CONFIG.raw.get("notification", {})
except Exception:
    pass
EMAIL_NOTIFIER = EmailNotifier(_notification_config)

# ⭐ P1-2: 执行历史存储（内存中保留最近100次）
class ExecutionHistory:
    """存储最近100次执行结果，用于历史面板展示"""
    def __init__(self, max_size=100):
        self.max_size = max_size
        self._history: list[dict] = []
    
    def add(self, run_id: str, case_name: str, passed: bool, step_ok: int, step_count: int, 
            duration_s: float, env: str, timestamp: str):
        record = {
            "run_id": run_id,
            "case_name": case_name,
            "passed": passed,
            "step_ok": step_ok,
            "step_count": step_count,
            "duration_s": duration_s,
            "env": env,
            "timestamp": timestamp,
        }
        self._history.append(record)
        if len(self._history) > self.max_size:
            self._history = self._history[-self.max_size:]
    
    def get_recent(self, limit=10):
        return self._history[-limit:]
    
    def get_by_case(self, case_name: str, limit=5):
        return [r for r in self._history if r["case_name"] == case_name][-limit:]

EXECUTION_HISTORY = ExecutionHistory(100)

# ⭐ 任务持久化 DAO
def _ensure_task_tables():
    """确保任务相关表存在（首次启动自动创建）"""
    try:
        from lib.db.pool import get_pool
        pool = get_pool()
        pool.execute("""
            CREATE TABLE IF NOT EXISTS tasks (
                id TEXT PRIMARY KEY,
                name TEXT,
                env_id TEXT,
                total_count INTEGER DEFAULT 0,
                passed_count INTEGER DEFAULT 0,
                failed_count INTEGER DEFAULT 0,
                duration_s REAL DEFAULT 0,
                status TEXT DEFAULT 'pending',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                started_at TIMESTAMP,
                finished_at TIMESTAMP
            )
        """)
        pool.execute("""
            CREATE TABLE IF NOT EXISTS task_cases (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                task_id TEXT NOT NULL,
                case_name TEXT NOT NULL,
                case_id INTEGER,
                status TEXT DEFAULT 'pending',
                step_ok INTEGER DEFAULT 0,
                step_count INTEGER DEFAULT 0,
                duration_s REAL DEFAULT 0,
                error_message TEXT,
                FOREIGN KEY (task_id) REFERENCES tasks(id)
            )
        """)
        pool.get_connection().commit()
    except Exception as e:
        import logging
        logging.getLogger(__name__).warning(f"创建任务表失败（非致命）: {e}")

_ensure_task_tables()
TASK_DAO = TaskDAO()
REPORT_DAO = ReportDAO()

# 日志存储（目录从 config 读）
_log_dir_path = (SKILL_ROOT / CONFIG.webui.logging_dir.lstrip("./")) if CONFIG.webui.logging_dir else (SKILL_ROOT / "logs")
LOG_STORE = LogStore(_log_dir_path, buffer_size=500, retention_days=30)
install_global_capture(LOG_STORE)
LOG_STORE.add("info", "server", f"cosmic-replay Web UI 启动中... log_dir={_log_dir_path}")

# 运行任务管理
class RunSession:
    """一次 run 的生命周期：事件 queue + 结果"""
    def __init__(self, run_id: str, case_name: str, env_id: str = ""):
        self.run_id = run_id
        self.case_name = case_name
        self.env_id = env_id
        self.queue: queue.Queue = queue.Queue()
        self.started_at = time.time()
        self.finished = False
        self.thread: threading.Thread | None = None

    def emit(self, event_type: str, payload: dict):
        self.queue.put({"type": event_type, "data": payload, "ts": time.time()})
        # 同步持久化一份到 logs/runs/<id>.jsonl 供历史回放
        try:
            LOG_STORE.save_run_event(self.run_id, event_type, payload)
        except Exception:
            pass
        # ⭐ P1-2: 拦截case_done事件，记录到执行历史
        if event_type == "case_done":
            try:
                EXECUTION_HISTORY.add(
                    run_id=self.run_id,
                    case_name=self.case_name,
                    passed=payload.get("passed", False),
                    step_ok=payload.get("step_ok", 0),
                    step_count=payload.get("step_count", 0),
                    duration_s=payload.get("duration_s", 0),
                    env=self.env_id,
                    timestamp=datetime.now().isoformat(),
                )
            except Exception:
                pass

    def close(self):
        self.finished = True
        self.queue.put({"type": "_close", "data": {}, "ts": time.time()})


RUNS: dict[str, RunSession] = {}


# ============================================================
# 工具
# ============================================================
def skill_path(rel: str) -> Path:
    """相对 skill 根的路径"""
    return (SKILL_ROOT / rel.lstrip("./"))


def cases_dir() -> Path:
    return skill_path(CONFIG.webui.cases_dir)


def har_upload_dir() -> Path:
    d = skill_path(CONFIG.webui.har_upload_dir)
    d.mkdir(parents=True, exist_ok=True)
    return d


def list_case_files() -> list[Path]:
    d = cases_dir()
    if not d.exists():
        return []
    return sorted(d.rglob("*.yaml"))


def case_name_from_path(path: Path) -> str:
    """相对 cases_dir 的路径作为用例 name"""
    try:
        rel = path.relative_to(cases_dir())
        return str(rel).replace("\\", "/").replace(".yaml", "")
    except ValueError:
        return path.stem


def case_path_from_name(name: str) -> Path:
    """用例 name → 文件路径"""
    safe = name.replace("..", "").replace("\\", "/").lstrip("/")
    return cases_dir() / f"{safe}.yaml"


# ============================================================
# Endpoint: 基础信息
# ============================================================
@APP.get("/api/info")
def api_info():
    return {
        "version": "0.1.0",
        "skill_root": str(SKILL_ROOT),
        "config_dir": str(CONFIG_DIR),
        "cases_dir": str(cases_dir()),
        "config_initialized": CONFIG_DIR.exists(),
    }


# ⭐ P0-3: 健康检查API
@APP.get("/api/health")
def api_health():
    """健康检查端点，用于运维监控"""
    try:
        cases_count = len(list_case_files())
        envs_count = len(CONFIG.envs)
        uptime = int(time.time() - _start_time)
        
        return {
            "status": "healthy",
            "timestamp": datetime.now().isoformat(),
            "cases_count": cases_count,
            "envs_count": envs_count,
            "uptime_seconds": uptime,
        }
    except Exception as e:
        return {
            "status": "degraded",
            "error": str(e),
            "timestamp": datetime.now().isoformat(),
        }


# ⭐ P1-2: 执行历史API
@APP.get("/api/history")
def api_get_history(limit: int = 10):
    """获取最近执行历史"""
    return EXECUTION_HISTORY.get_recent(limit)

@APP.get("/api/history/{case_name}")
def api_get_case_history(case_name: str, limit: int = 5):
    """获取指定用例的执行历史"""
    return EXECUTION_HISTORY.get_by_case(case_name, limit)


# ============================================================
# Endpoint: 字段标签
# ============================================================
_field_labels_cache_merged: dict | None = None

@APP.get("/api/field-labels")
def api_field_labels():
    """获取所有字段标签映射（合并知识库 + 静态字典）"""
    global _field_labels_cache_merged
    if _field_labels_cache_merged is None:
        from lib.kb_loader import get_all_field_labels
        from lib.har_extractor import _FIELD_LABELS

        # 合并：KB 优先级高于静态字典
        merged = dict(_FIELD_LABELS)  # 静态字典作为基础
        kb_labels = get_all_field_labels()
        merged.update(kb_labels)  # KB 覆盖静态字典
        _field_labels_cache_merged = merged

    return {
        "labels": _field_labels_cache_merged,
        "count": len(_field_labels_cache_merged),
        "source": "kb+static",
    }


# ============================================================
# Endpoint: 配置
# ============================================================
@APP.get("/api/config")
def api_get_config(mask: bool = True):
    return CONFIG.to_dict(mask_secrets=mask)


@APP.post("/api/config/init")
def api_init_config(force: bool = False):
    try:
        created = CONFIG.init_from_example(force=force)
        return {"ok": True, "created": created}
    except Exception as e:
        raise HTTPException(500, str(e))


@APP.put("/api/config/webui")
def api_save_webui(prefs: dict = Body(...)):
    try:
        CONFIG.save_webui(prefs)
        return {"ok": True, "webui": CONFIG.webui.__dict__}
    except Exception as e:
        raise HTTPException(500, str(e))


@APP.get("/api/envs")
def api_list_envs(mask: bool = True):
    return [e for e in CONFIG.to_dict(mask_secrets=mask)["envs"]]


@APP.put("/api/envs/{env_id}")
def api_save_env(env_id: str, data: dict = Body(...)):
    try:
        CONFIG.save_env(env_id, data)
        return {"ok": True, "env_id": env_id}
    except Exception as e:
        raise HTTPException(500, str(e))


@APP.delete("/api/envs/{env_id}")
def api_delete_env(env_id: str):
    ok = CONFIG.delete_env(env_id)
    return {"ok": ok}


# ============================================================
# Endpoint: 用例
# ============================================================
@APP.get("/api/cases")
def api_list_cases():
    """返回所有用例的元信息（name / 路径 / tags / last_run）"""
    # 批量获取每个用例的最近执行记录
    last_runs = LOG_STORE.get_last_run_per_case(limit=200)

    items = []
    for p in list_case_files():
        name = case_name_from_path(p)
        meta = {"name": name, "file": str(p.relative_to(SKILL_ROOT)),
                "size": p.stat().st_size,
                "mtime": p.stat().st_mtime}
        # 尝试读 name / description / tags
        try:
            case = load_yaml(p)
            if isinstance(case, dict):
                meta["display_name"] = case.get("name", name)
                meta["description"] = case.get("description", "")
                meta["tags"] = case.get("tags", [])
                meta["main_form_id"] = case.get("main_form_id", "")
                meta["step_count"] = len(case.get("steps", []))
                # created_at: 优先从 YAML 读取，否则用文件创建时间
                if case.get("created_at"):
                    meta["created_at"] = str(case["created_at"])
                else:
                    meta["created_at"] = datetime.fromtimestamp(p.stat().st_ctime).isoformat()
        except Exception as e:
            meta["parse_error"] = str(e)
            meta["created_at"] = datetime.fromtimestamp(p.stat().st_ctime).isoformat()

        # 附加最近执行信息
        lr = last_runs.get(name)
        if lr:
            meta["last_result"] = "PASS" if lr.get("passed") else "FAIL"
            meta["last_run_time"] = datetime.fromtimestamp(lr["mtime"]).strftime("%Y-%m-%d %H:%M:%S") if lr.get("mtime") else None
            meta["last_run_id"] = lr.get("run_id")
            meta["last_duration_s"] = lr.get("duration_s")
        else:
            meta["last_result"] = None
            meta["last_run_time"] = None
            meta["last_run_id"] = None
            meta["last_duration_s"] = None

        items.append(meta)
    return items


@APP.get("/api/cases/{name:path}/yaml")
def api_get_case_yaml(name: str):
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")
    return {"name": name, "yaml": p.read_text(encoding="utf-8")}


@APP.put("/api/cases/{name:path}/yaml")
def api_save_case_yaml(name: str, body: dict = Body(...)):
    p = case_path_from_name(name)
    p.parent.mkdir(parents=True, exist_ok=True)
    content = body.get("yaml", "")
    if not isinstance(content, str):
        raise HTTPException(400, "yaml 字段必须是字符串")
    p.write_text(content, encoding="utf-8")
    return {"ok": True, "name": name, "size": len(content)}


@APP.post("/api/cases/{name:path}/repairs/plan")
def api_case_repair_plan(name: str, body: dict = Body(...)):
    """根据失败归因和 advisor 输出生成结构化修复计划。"""
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")
    case = load_yaml(p)
    if not isinstance(case, dict):
        raise HTTPException(400, "YAML 顶层必须是 dict")
    from lib.repair_planner import build_repair_plan

    repairs = build_repair_plan(
        case,
        body.get("failure_analysis") or {},
        body.get("fixes") or [],
    )
    return {"ok": True, "name": name, "repair_plan": repairs}


@APP.post("/api/cases/{name:path}/repairs/apply")
def api_apply_case_repair(name: str, body: dict = Body(...)):
    """应用用户确认过的结构化修复。"""
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")
    case = load_yaml(p)
    if not isinstance(case, dict):
        raise HTTPException(400, "YAML 顶层必须是 dict")

    repair = body.get("repair") or {}
    if not isinstance(repair, dict):
        raise HTTPException(400, "repair 必须是对象")

    from lib.repair_planner import apply_repair
    import yaml

    new_case, applied, message = apply_repair(case, repair)
    if not applied:
        return JSONResponse(status_code=409, content={
            "ok": False,
            "applied": False,
            "message": message,
        })

    backup_path = p.with_suffix(p.suffix + ".bak")
    try:
        backup_path.write_text(p.read_text(encoding="utf-8"), encoding="utf-8")
    except Exception:
        pass
    new_yaml = yaml.dump(new_case, allow_unicode=True, sort_keys=False, default_flow_style=False)
    p.write_text(new_yaml, encoding="utf-8")
    return {
        "ok": True,
        "applied": True,
        "message": message,
        "name": name,
        "yaml": new_yaml,
    }


@APP.delete("/api/cases/{name}")
def api_delete_case(name: str):
    p = case_path_from_name(name)
    if p.exists():
        p.unlink()
    return {"ok": True}


@APP.put("/api/cases/{name:path}/display_name")
def api_update_case_display_name(name: str, body: dict = Body(...)):
    """更新用例的显示名称（修改YAML中的name字段）"""
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")
    
    new_display_name = body.get("display_name", "")
    if not new_display_name:
        raise HTTPException(400, "display_name不能为空")
    
    # 读取YAML
    case = load_yaml(p)
    if not isinstance(case, dict):
        raise HTTPException(500, "用例解析失败")
    
    # 更新name字段
    case["name"] = new_display_name
    
    # 写回文件
    import yaml
    new_content = yaml.dump(case, allow_unicode=True, sort_keys=False, default_flow_style=False)
    p.write_text(new_content, encoding="utf-8")
    
    return {"ok": True, "display_name": new_display_name}


@APP.post("/api/cases/batch_delete")
def api_batch_delete_cases(body: dict = Body(...)):
    """批量删除用例"""
    names = body.get("names", [])
    if not isinstance(names, list):
        raise HTTPException(400, "names must be a list")
    deleted = []
    for name in names:
        p = case_path_from_name(name)
        if p.exists():
            p.unlink()
            deleted.append(name)
    return {"ok": True, "deleted": deleted, "count": len(deleted)}


@APP.post("/api/cases/{name:path}/rename")
def api_rename_case(name: str, body: dict = Body(...)):
    """重命名用例（修改文件名 + 同步更新 YAML 内 name 字段）"""
    new_name = body.get("new_name", "").strip()
    if not new_name:
        raise HTTPException(400, "新名称不能为空")
    old_path = case_path_from_name(name)
    new_path = case_path_from_name(new_name)
    if not old_path.exists():
        raise HTTPException(404, f"用例 {name} 不存在")
    if new_path.exists():
        raise HTTPException(409, f"用例 {new_name} 已存在")
    # 同步更新 YAML 内的 name 字段（用正则替换保留注释和格式）
    import re as _re
    now_iso = datetime.now().isoformat(timespec='seconds')
    try:
        content = old_path.read_text(encoding="utf-8")
        # 匹配 name: xxx 行（可能带引号）
        new_content = _re.sub(
            r'^(name:\s*).*$',
            rf'\g<1>{new_name}',
            content,
            count=1,
            flags=_re.MULTILINE
        )
        # 更新 created_at 字段为当前时间
        if _re.search(r'^created_at:', new_content, flags=_re.MULTILINE):
            new_content = _re.sub(
                r'^(created_at:\s*).*$',
                rf'\g<1>{now_iso}',
                new_content,
                count=1,
                flags=_re.MULTILINE
            )
        else:
            # 没有 created_at 行，在 name: 行之后插入
            new_content = _re.sub(
                r'^(name:\s*.*)$',
                rf'\1\ncreated_at: {now_iso}',
                new_content,
                count=1,
                flags=_re.MULTILINE
            )
        if new_content != content:
            old_path.write_text(new_content, encoding="utf-8")
    except Exception:
        pass  # 即使更新 name 字段失败，仍允许重命名文件
    new_path.parent.mkdir(parents=True, exist_ok=True)
    shutil.move(str(old_path), str(new_path))
    return {"ok": True, "new_name": new_name}


@APP.post("/api/cases/{name:path}/description")
def api_update_case_description(name: str, body: dict = Body(...)):
    """更新用例描述"""
    import yaml
    description = body.get("description", "")
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例 {name} 不存在")
    # 读取 YAML，修改 description 字段，写回
    data = load_yaml(p)
    if not isinstance(data, dict):
        raise HTTPException(500, "用例解析失败")
    data['description'] = description
    data['created_at'] = datetime.now().isoformat(timespec='seconds')
    new_content = yaml.dump(data, allow_unicode=True, sort_keys=False, default_flow_style=False)
    p.write_text(new_content, encoding="utf-8")
    return {"ok": True}


# ============================================================
# Endpoint: 执行
# ============================================================
def _case_field_needs_env_override(v) -> bool:
    """判断用例 env 字段值是否需要被环境配置覆盖。

    认作"需要覆盖"的情况：
      - 空 / None / 纯空白
      - 仍是 ${env:XXX} 或 ${env:XXX:default} 形式，且对应环境变量未设（无默认值也算未设）

    这样用例里写 `username: ${env:COSMIC_USERNAME}` 但重装后 env 没设时，
    仍能自动回退到 config/envs/<env>.yaml 的凭证，不用每次手动给默认值。
    """
    if v is None:
        return True
    if isinstance(v, str):
        s = v.strip()
        if not s:
            return True
        # 纯 ${env:...} 占位符且环境变量未设 → 视作未填
        m = re.fullmatch(r"\$\{env:([^}:]+)(?::([^}]*))?\}", s)
        if m:
            name = m.group(1).strip()
            default = m.group(2)
            env_val = os.environ.get(name, "")
            if not env_val and (default is None or default.strip() == ""):
                return True
    return False


def _merge_env_into_case(case: dict, env_id: str | None) -> dict:
    """把环境配置 merge 进用例 env 块。

    规则：用例里该字段为空 / 纯 ${env:XXX} 占位符且 env var 未设 / 仅占位符无默认值
    → 用 config/envs/<env_id>.yaml 的值覆盖；否则尊重用例。
    """
    if not env_id:
        return case
    env = CONFIG.get_env(env_id)
    if not env:
        return case
    case_env = case.setdefault("env", {})

    if _case_field_needs_env_override(case_env.get("base_url")):
        case_env["base_url"] = env.base_url
    if _case_field_needs_env_override(case_env.get("datacenter_id")):
        case_env["datacenter_id"] = env.datacenter_id

    username = env.credentials.resolve_username()
    password = env.credentials.resolve_password()
    if username and _case_field_needs_env_override(case_env.get("username")):
        case_env["username"] = username
    if password and _case_field_needs_env_override(case_env.get("password")):
        case_env["password"] = password

    # basedata 注入到 vars 下 basedata 子节点
    if env.basedata:
        vars_ns = case.setdefault("vars", {}) or {}
        vars_ns["_basedata"] = env.basedata
    # 签名配置
    case.setdefault("sign_required", env.sign_required)
    # 仅供运行期诊断/缓存使用，不写入 YAML。
    case["_runtime_env_id"] = env_id
    return case


@APP.post("/api/cases/{name:path}/run")
def api_run_case(name: str, body: dict = Body(default={})):
    """启动异步执行。返回 run_id。前端用 /api/runs/{run_id}/events 订阅事件。"""
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")

    env_id = body.get("env_id") or CONFIG.webui.default_env
    if body.get("env_id") and body["env_id"] != CONFIG.webui.default_env:
        CONFIG.set_default_env(body["env_id"])
    try:
        case = load_yaml(p)
    except Exception as e:
        raise HTTPException(400, f"YAML 解析失败: {e}")

    if not isinstance(case, dict):
        raise HTTPException(400,
            f"YAML 格式错误: 期望顶层是字典(dict)，实际得到 {type(case).__name__}。"
            f"请检查文件 {name}.yaml 是否为空或格式损坏。")

    case = _merge_env_into_case(case, env_id)

    run_id = uuid.uuid4().hex[:12]
    sess = RunSession(run_id, name, env_id)  # ⭐ P1-2: 传入env_id
    RUNS[run_id] = sess

    def worker():
        try:
            run_case(case, on_event=sess.emit)
        except Exception as e:
            import traceback
            tb = traceback.format_exc()
            LOG_STORE.add("error", "runner",
                         f"run_case 异常 run_id={run_id} case={name}: {e}\n{tb}")
            sess.emit("case_error", {"error": f"{type(e).__name__}: {e}"})
        finally:
            sess.close()

    t = threading.Thread(target=worker, daemon=True)
    sess.thread = t
    t.start()

    return {"run_id": run_id, "name": name, "env_id": env_id}


@APP.get("/api/runs/{run_id}/events")
async def api_sse_events(run_id: str, request: Request):
    """SSE 流。客户端 EventSource 订阅。"""
    sess = RUNS.get(run_id)
    if not sess:
        raise HTTPException(404, f"run_id 不存在: {run_id}")

    async def event_gen():
        # 心跳 + 事件推送
        while True:
            if await request.is_disconnected():
                break
            try:
                evt = sess.queue.get_nowait()
            except queue.Empty:
                await asyncio.sleep(1.0)
                yield ": keepalive\n\n"
                continue
            if evt["type"] == "_close":
                yield f"event: close\ndata: {{}}\n\n"
                break
            data_str = _safe_json_dumps(evt["data"])
            yield f"event: {evt['type']}\ndata: {data_str}\n\n"

    return StreamingResponse(event_gen(), media_type="text/event-stream",
                             headers={"Cache-Control": "no-cache",
                                      "X-Accel-Buffering": "no"})


@APP.get("/api/runs")
def api_list_runs():
    """当前进行中的/最近的 runs"""
    return [
        {
            "run_id": r.run_id,
            "case": r.case_name,
            "started_at": r.started_at,
            "finished": r.finished,
        }
        for r in list(RUNS.values())[-20:]
    ]


# ============================================================
# Endpoint: 日志
# ============================================================
@APP.get("/api/logs")
def api_get_logs(level: str | None = None, search: str | None = None, limit: int = 500):
    """读服务日志快照（最近 500 条）"""
    return LOG_STORE.snapshot(level_filter=level, search=search, limit=limit)


@APP.get("/api/logs/stream")
async def api_sse_logs(request: Request):
    """SSE 实时推送新日志"""
    q = LOG_STORE.subscribe()

    async def gen():
        # 先推一份快照让前端立即看到历史
        snapshot = LOG_STORE.snapshot(limit=100)
        for entry in snapshot:
            yield f"event: log\ndata: {_safe_json_dumps(entry)}\n\n"
        while True:
            if await request.is_disconnected():
                break
            try:
                entry = q.get_nowait()
            except Exception:
                # 用 asyncio.sleep 让出 event loop，不阻塞其他协程
                await asyncio.sleep(1.0)
                yield ": keepalive\n\n"
                continue
            payload = _safe_json_dumps(entry.to_dict())
            yield f"event: log\ndata: {payload}\n\n"

    try:
        return StreamingResponse(gen(), media_type="text/event-stream",
                                 headers={"Cache-Control": "no-cache",
                                          "X-Accel-Buffering": "no"})
    finally:
        # StreamingResponse 会在连接关闭后回调；这里保障手动解绑
        pass


@APP.get("/api/run_history")
def api_run_history(limit: int = 100):
    """列历史 runs（从 logs/runs/*.jsonl）"""
    return LOG_STORE.list_runs(limit=limit)


@APP.get("/api/run_history/{run_id}")
def api_run_detail(run_id: str):
    """回放某个历史 run 的完整事件流"""
    events = LOG_STORE.read_run(run_id)
    if not events:
        raise HTTPException(404, f"run_id 不存在或无记录: {run_id}")
    return {"run_id": run_id, "events": events}


# ============================================================
# 全局异常 handler
# ============================================================
@APP.exception_handler(Exception)
async def _global_exc_handler(request: Request, exc: Exception):
    import traceback
    tb = traceback.format_exc()
    LOG_STORE.add("error", "http",
                 f"Endpoint 异常 {request.method} {request.url.path}: {exc}\n{tb}")
    return JSONResponse(
        status_code=500,
        content={"detail": f"{type(exc).__name__}: {exc}"},
    )


# ============================================================
# Endpoint: HAR
# ============================================================
def _create_replay_for_env(env_id: str):
    """为在线环境字段解析创建轻量 replay。"""
    env_cfg = CONFIG.get_env(env_id) or CONFIG.default_env()
    if not env_cfg:
        raise HTTPException(400, f"环境不存在: {env_id}")
    if not env_cfg.credentials.is_configured():
        raise HTTPException(400, f"环境未配置凭证: {env_id}")

    login_session = SESSION_MGR.get_or_create(
        env_id=env_id,
        base_url=env_cfg.base_url,
        username=env_cfg.credentials.resolve_username(),
        password=env_cfg.credentials.resolve_password(),
        datacenter_id=env_cfg.datacenter_id,
    )
    if not login_session:
        raise HTTPException(502, f"环境登录失败: {env_id}")

    from lib.replay import CosmicFormReplay, CosmicSession

    sess = CosmicSession(
        base_url=env_cfg.base_url.rstrip("/"),
        cookie=login_session["cookie"],
        user_id=login_session.get("user_id", ""),
        account_id=login_session.get("account_id", ""),
        csrf_token=login_session.get("csrf_token", ""),
    )
    replay = CosmicFormReplay(sess, sign_required=bool(env_cfg.sign_required))
    replay.init_root()
    return replay


def _prime_replay_form_from_har(replay, har_file: str, form_id: str, app_id: str) -> bool:
    """使用 HAR 中同表单的首个 loadData 初始化在线解析上下文。"""
    if not har_file:
        return False
    har_path = har_upload_dir() / Path(har_file).name
    if not har_path.exists():
        return False
    try:
        steps = har_extractor.extract_steps(har_extractor.load_har(har_path))
        steps = har_extractor.dedup_open_forms(steps)
        steps = har_extractor.relocate_premature_open_forms(steps)
        steps = har_extractor.lower_set_item_to_pick_basedata(steps)
        steps = har_extractor.merge_consecutive_update_values(steps)
    except Exception:
        return False

    for step in steps:
        if step.get("form_id") != form_id or step.get("app_id") != app_id:
            continue
        if step.get("type") != "invoke" or step.get("ac") != "loadData":
            continue
        action = {
            "key": step.get("key", ""),
            "methodName": step.get("method", ""),
            "args": step.get("args", []),
            "postData": step.get("post_data", [{}, []]),
        }
        try:
            replay.invoke(form_id, app_id, "loadData", [action])
            return True
        except Exception:
            return False
    return False


@APP.post("/api/har/preview")
async def api_har_preview(request: Request):
    """上传 HAR，返回预览（不落盘）

    接受两种上传方式:
    1. raw binary body + ?filename=xxx.har（推荐，大文件快）
    2. multipart form-data（兼容旧前端）
    """
    d = har_upload_dir()
    content_type = request.headers.get("content-type", "")
    if "multipart/form-data" in content_type:
        # 旧方式：multipart（python-multipart 解析大文件极慢，仅做兼容）
        form = await request.form()
        upload = form.get("file")
        if upload is None:
            return JSONResponse(status_code=400, content={"ok": False, "error": "missing file"})
        filename = getattr(upload, "filename", "upload.har")
        content = await upload.read()
    else:
        # 新方式：raw body，文件名走 query 参数
        filename = request.query_params.get("filename", "upload.har")
        content = await request.body()
    if not content:
        return JSONResponse(status_code=400, content={"ok": False, "error": "empty body"})

    save_path = d / f"preview_{int(time.time())}_{filename}"
    save_path.write_bytes(content)

    # ⭐ 提取环境参数，确定元数据状态
    env_id = request.query_params.get("env_id", "")
    metadata_status = "offline"  # offline / online / degraded

    if env_id:
        try:
            env_cfg = CONFIG.get_env(env_id) or CONFIG.default_env()
            if env_cfg and env_cfg.credentials.is_configured():
                login_session = SESSION_MGR.get_or_create(
                    env_id=env_id,
                    base_url=env_cfg.base_url,
                    username=env_cfg.credentials.resolve_username(),
                    password=env_cfg.credentials.resolve_password(),
                    datacenter_id=env_cfg.datacenter_id,
                )
                if login_session:
                    metadata_status = "online"
                else:
                    metadata_status = "degraded"
            elif env_cfg:
                metadata_status = "degraded"
        except Exception as e:
            import logging
            logging.getLogger(__name__).warning(f"Preview login failed for env '{env_id}': {e}")
            metadata_status = "degraded"

    # ⭐ 构建 MetadataResolver 实例（在线模式下用于实时元数据增强）
    meta_resolver = None
    if env_id and metadata_status == "online":
        try:
            from lib.metadata_resolver import MetadataResolver
            meta_resolver = MetadataResolver(
                base_url=env_cfg.base_url,
                cookie=login_session["cookie"],
                csrf_token=login_session.get("csrf_token", ""),
            )
        except Exception as e:
            import logging
            logging.getLogger(__name__).warning(f"MetadataResolver 构建失败: {e}")
            meta_resolver = None

    try:
        # ⭐ 热重载 har_extractor，确保用最新代码
        import importlib
        importlib.reload(har_extractor)

        preview = har_extractor.preview_har(save_path, meta_resolver=meta_resolver)
        return {
            "ok": True,
            "preview": preview,
            "har_file": save_path.name,   # 后续 extract 用这个
            "metadata_status": metadata_status,  # ⭐ 告知前端元数据增强状态
        }
    except Exception as e:
        return JSONResponse(
            status_code=500,
            content={"ok": False, "error": f"{type(e).__name__}: {e}", "metadata_status": "offline"},
        )


@APP.post("/api/env-fields/resolve")
def api_resolve_env_fields(body: dict = Body(...)):
    """批量解析 HAR 预览中的环境字段。

    只解析显式标记 auto_resolve=true 的基础资料字段；歧义/失败时返回诊断，
    不会静默修改业务语义。
    """
    env_id = body.get("env_id") or CONFIG.webui.default_env
    har_file = body.get("har_file") or ""
    fields = body.get("fields") or []
    if not isinstance(fields, list):
        raise HTTPException(400, "fields 必须是数组")

    replay = None
    try:
        from lib.field_resolver import FieldResolver, ResolveResult

        replay = _create_replay_for_env(env_id)
        resolver = FieldResolver(replay, env_id=env_id)
        resolved_fields = []
        opened_forms: set[tuple[str, str]] = set()
        for item in fields:
            if not isinstance(item, dict):
                continue
            step_id = item.get("id") or item.get("step_id") or ""
            field_key = str(item.get("field_key") or "").strip()
            value_name = str(item.get("value_name") or "").strip()
            value_id = str(item.get("value_id") or "").strip()
            form_id = str(item.get("form_id") or "").strip()
            app_id = str(item.get("app_id") or "").strip()

            base = {
                "step_id": step_id,
                "field_key": field_key,
                "value_id": value_id,
                "value_name": value_name,
                "label": item.get("label", field_key),
                "env_sensitive": item.get("env_sensitive", "medium"),
                "auto_resolve": bool(item.get("auto_resolve")),
            }

            if not item.get("auto_resolve"):
                resolved_fields.append({
                    **base,
                    "resolve_status": "manual",
                    "effective_value_id": value_id,
                    "message": "未启用自动解析",
                })
                continue
            if not (form_id and app_id and field_key and value_name):
                resolved_fields.append({
                    **base,
                    "resolve_status": "skipped",
                    "effective_value_id": value_id,
                    "message": "缺少 form_id/app_id/field_key/value_name，无法解析",
                })
                continue

            form_key = (form_id, app_id)
            if form_key not in opened_forms:
                try:
                    replay.open_form(form_id, app_id)
                except Exception:
                    # 个别字段可能来自运行中弹窗/列表态，预打开失败时仍尝试按 root 上下文解析。
                    pass
                _prime_replay_form_from_har(replay, har_file, form_id, app_id)
                opened_forms.add(form_key)

            result = resolver.resolve_basedata_result(
                form_id,
                app_id,
                field_key,
                value_name,
                original_value_id=value_id,
            )
            data = result.to_dict() if isinstance(result, ResolveResult) else dict(result)
            effective_value_id = data.get("resolved_value_id") if data.get("status") == "resolved" else value_id
            resolved_fields.append({
                **base,
                "resolve_status": data.get("status"),
                "resolved_value_id": data.get("resolved_value_id", ""),
                "resolved_value_name": data.get("resolved_value_name", ""),
                "effective_value_id": effective_value_id or value_id,
                "confidence": data.get("confidence", "low"),
                "message": data.get("message", ""),
                "candidates": data.get("candidates", []),
            })

        return {"ok": True, "env_id": env_id, "fields": resolved_fields}
    finally:
        if replay is not None:
            try:
                replay.close()
            except Exception:
                pass


@APP.post("/api/har/extract")
def api_har_extract(body: dict = Body(...)):
    """由上一个 preview 的结果生成 YAML 写入 cases/"""
    har_file = body.get("har_file")
    case_name = body.get("case_name") or "untitled"
    var_overrides = body.get("var_overrides")  # ⭐ 用户变量配置
    pick_field_overrides = body.get("pick_field_overrides")  # ⭐ 环境字段配置
    if not har_file:
        raise HTTPException(400, "缺少 har_file")
    har_path = har_upload_dir() / har_file
    if not har_path.exists():
        raise HTTPException(404, f"HAR 文件不存在: {har_file}")

    out_path = case_path_from_name(case_name)
    overwritten = out_path.exists()

    # ⭐ 构建 MetadataResolver（从缓存 session 获取）
    meta_resolver = None
    env_id = body.get("env_id", "")
    if env_id:
        try:
            env_cfg = CONFIG.get_env(env_id) or CONFIG.default_env()
            login_session = SESSION_MGR.get_cached(env_id)
            if login_session and env_cfg:
                from lib.metadata_resolver import MetadataResolver
                meta_resolver = MetadataResolver(
                    base_url=env_cfg.base_url,
                    cookie=login_session["cookie"],
                    csrf_token=login_session.get("csrf_token", ""),
                )
        except Exception as e:
            import logging
            logging.getLogger(__name__).warning(f"Extract MetadataResolver 构建失败: {e}")
            meta_resolver = None

    try:
        # ⭐ 重新导入时强制使用最新 har_extractor 代码（模块级热重载）
        import importlib
        importlib.reload(har_extractor)

        yaml_text = har_extractor.build_yaml_case(har_path, case_name, var_overrides=var_overrides, pick_field_overrides=pick_field_overrides, meta_resolver=meta_resolver)
        # ⭐ 在 YAML 中注入 created_at 字段（在 name: 行之后插入）
        now_iso = datetime.now().isoformat(timespec='seconds')
        if re.search(r'^created_at:', yaml_text, flags=re.MULTILINE):
            yaml_text = re.sub(
                r'^(created_at:\s*).*$',
                rf'\1{now_iso}',
                yaml_text,
                count=1,
                flags=re.MULTILINE
            )
        else:
            yaml_text = re.sub(
                r'^(name:\s*.*)$',
                rf'\1\ncreated_at: {now_iso}',
                yaml_text,
                count=1,
                flags=re.MULTILINE
            )
        out_path.parent.mkdir(parents=True, exist_ok=True)
        out_path.write_text(yaml_text, encoding="utf-8")
        action = "覆盖" if overwritten else "生成"
        return {"ok": True, "name": case_name, "file": str(out_path.relative_to(SKILL_ROOT)),
                "overwritten": overwritten, "action": action}
    except Exception as e:
        raise HTTPException(500, f"抽取失败: {e}")


@APP.post("/api/pick-field-update")
def api_pick_field_update(body: dict = Body(...)):
    """更新已生成YAML中的pick_field值"""
    case_name = body.get("case_name")
    step_id = body.get("step_id")
    value_id = str(body.get("value_id") or "").strip()
    value_name_provided = "value_name" in body
    value_name = str(body.get("value_name") or "").strip()
    manual_override = bool(body.get("manual_override", True))

    if not case_name or not step_id:
        raise HTTPException(400, "缺少 case_name 或 step_id")
    if not value_id:
        raise HTTPException(400, "缺少 value_id")

    p = case_path_from_name(case_name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {case_name}")

    # 读取YAML
    case = load_yaml(p)
    if not isinstance(case, dict):
        raise HTTPException(500, "用例解析失败")

    pick_fields = case.get("pick_fields")
    if pick_fields is None:
        raise HTTPException(404, "用例中无 pick_fields")

    # 支持 dict 和 list 两种格式
    found = False
    if isinstance(pick_fields, dict):
        # dict 格式：键为 step_id（如 pick_gender_id）
        if step_id in pick_fields and isinstance(pick_fields[step_id], dict):
            _apply_pick_field_manual_update(
                pick_fields[step_id],
                value_id,
                value_name if value_name_provided else None,
                manual_override=manual_override,
            )
            found = True
    elif isinstance(pick_fields, list):
        # list 格式：每项有 id 字段
        for item in pick_fields:
            if isinstance(item, dict) and item.get("id") == step_id:
                _apply_pick_field_manual_update(
                    item,
                    value_id,
                    value_name if value_name_provided else None,
                    manual_override=manual_override,
                )
                found = True
                break
    else:
        raise HTTPException(500, "pick_fields 格式异常")

    if not found:
        raise HTTPException(404, f"pick_fields 中未找到 id={step_id}")

    # 写回文件
    import yaml
    new_content = yaml.dump(case, allow_unicode=True, sort_keys=False, default_flow_style=False)
    p.write_text(new_content, encoding="utf-8")

    return {"ok": True, "step_id": step_id, "value_id": value_id}


def _apply_pick_field_manual_update(
    item: dict,
    value_id: str,
    value_name: str | None = None,
    *,
    manual_override: bool = True,
) -> None:
    """Apply a user-maintained env field value without stale auto-resolve metadata."""
    old_value_id = str(item.get("value_id") or "")
    old_value_name = str(item.get("value_name") or "")
    item["value_id"] = value_id
    if value_name is not None:
        item["value_name"] = value_name
    elif old_value_name and old_value_name != value_id and old_value_id != value_id:
        item["value_name"] = ""
    if manual_override:
        item["auto_resolve"] = False
        item["resolve_status"] = "manual"
        item["manual_override"] = True


# ============================================================
# Endpoint: 任务管理（批量执行）
# ============================================================
@APP.post("/api/tasks")
def api_create_task(body: dict = Body(...)):
    """创建批量执行任务"""
    case_names = body.get("case_names", [])
    env_id = body.get("env_id") or CONFIG.webui.default_env
    name = body.get("name", "")
    concurrency = body.get("concurrency", 3)
    
    if not case_names:
        raise HTTPException(400, "缺少用例列表")
    
    # 创建任务
    task = TASK_MANAGER.create_task(case_names, env_id, name)
    task.concurrency = max(1, int(concurrency or 3))  # 存储并发数
    
    # ⭐ 持久化到数据库
    try:
        TASK_DAO.create(task_id=task.task_id, name=task.name,
                        case_names=task.case_names, env_id=env_id)
    except Exception as e:
        LOG_STORE.add("warn", "task_dao", f"任务持久化失败: {e}")
    
    return {
        "ok": True,
        "task_id": task.task_id,
        "total_count": task.total_count,
    }


@APP.post("/api/tasks/{task_id}/start")
def api_start_task(task_id: str):
    """启动任务执行"""
    task = TASK_MANAGER.get_task(task_id)
    if not task:
        raise HTTPException(404, f"任务不存在: {task_id}")
    
    if task.status not in ("pending", "completed"):
        raise HTTPException(400, f"任务状态不允许启动: {task.status}")
    
    # 重跑时清空旧结果
    if task.status == "completed":
        task.results.clear()
    
    # 更新状态
    TASK_MANAGER.update_task_status(task_id, "running")
    # ⭐ 同步持久化状态
    try:
        TASK_DAO.update_status(task_id, "running")
    except Exception:
        pass
    
    # 启动执行线程
    concurrency = getattr(task, 'concurrency', 3) or 3
    
    def worker():
        def run_single_case(case_name):
            """独立执行一个用例（线程安全）"""
            try:
                p = case_path_from_name(case_name)
                if not p.exists():
                    TASK_MANAGER.add_result(task_id, CaseResult(
                        name=case_name,
                        passed=False,
                        error=f"用例文件不存在",
                    ))
                    return
                
                case = load_yaml(p)
                if not isinstance(case, dict):
                    TASK_MANAGER.add_result(task_id, CaseResult(
                        name=case_name,
                        passed=False,
                        error=f"YAML格式错误: 文件为空或格式损坏",
                    ))
                    return
                case = _merge_env_into_case(case, task.env_id)
                
                run_id = uuid.uuid4().hex[:12]
                sess = RunSession(run_id, case_name, task.env_id)
                RUNS[run_id] = sess
                
                start_time = time.time()
                result = CaseResult(name=case_name, passed=False, run_id=run_id)
                
                def capture_event(evt_type, payload):
                    sess.emit(evt_type, payload)
                    def _phase_id(step_id: str) -> str:
                        return "step:" + str(step_id or "")

                    def _phase(step_id: str) -> dict:
                        pid = _phase_id(step_id)
                        existing = next((p for p in result.phases if p.get("id") == pid), None)
                        if existing is not None:
                            return existing
                        created = {"id": pid, "label": step_id, "detail": "", "status": "pending"}
                        result.phases.append(created)
                        return created

                    if evt_type == "step_start":
                        p = _phase(payload.get("id", ""))
                        p.update({
                            "label": payload.get("label") or payload.get("id") or "",
                            "detail": payload.get("detail") or "",
                            "status": "running",
                            "optional": payload.get("optional", False),
                            "resolved_request": payload.get("resolved_request"),
                        })
                    if evt_type == "step_ok":
                        result.step_ok += 1
                        result.step_count += 1
                        p = _phase(payload.get("id", ""))
                        p.update({
                            "status": "ok",
                            "duration_ms": payload.get("duration_ms"),
                            "response": payload.get("response"),
                        })
                    elif evt_type == "step_fail":
                        result.step_count += 1
                        p = _phase(payload.get("id", ""))
                        p.update({
                            "status": "fail",
                            "duration_ms": payload.get("duration_ms"),
                            "error": payload.get("error") or "; ".join(payload.get("errors") or []),
                            "errors": payload.get("errors") or ([payload.get("error")] if payload.get("error") else []),
                            "response": payload.get("response"),
                        })
                        if not result.error:
                            result.error = payload.get("error", "步骤失败")
                    elif evt_type in ("assertion_ok", "assertion_fail"):
                        result.assertions.append({
                            "type": payload.get("type", ""),
                            "ok": evt_type == "assertion_ok",
                            "msg": payload.get("msg", ""),
                            "step": payload.get("step", ""),
                            "step_label": payload.get("step_label", ""),
                        })
                    elif evt_type == "failure_analysis":
                        result.failure_analysis = payload or {}
                    elif evt_type == "fixes_ready":
                        result.repair_plan = payload.get("repair_plan") or []
                    elif evt_type == "env_fields_resolved":
                        result.env_fields = payload.get("fields") or result.env_fields
                
                try:
                    run_result = run_case(case, on_event=capture_event)
                    result.passed = run_result.passed
                    if not result.assertions:
                        result.assertions = run_result.assertions
                    if not run_result.passed and not result.error:
                        failed_steps = [s for s in run_result.steps if not s.get("ok") and not s.get("optional")]
                        if failed_steps:
                            result.error = failed_steps[0].get("error", "步骤失败")[:200]
                except Exception as e:
                    result.passed = False
                    result.error = str(e)[:200]
                finally:
                    result.duration_s = time.time() - start_time
                    TASK_MANAGER.add_result(task_id, result)
                    sess.close()
                    
            except Exception as e:
                TASK_MANAGER.add_result(task_id, CaseResult(
                    name=case_name,
                    passed=False,
                    error=f"{type(e).__name__}: {str(e)[:200]}",
                ))
        
        with ThreadPoolExecutor(max_workers=concurrency) as executor:
            futures = {executor.submit(run_single_case, name): name
                      for name in task.case_names}
            for future in as_completed(futures):
                # 等待完成（异常已在 run_single_case 内部处理）
                pass
        
        # 所有用例完成后生成报告
        TASK_MANAGER.update_task_status(task_id, "completed")
        TASK_MANAGER.generate_report(task_id)
        # ⭐ 持久化报告到数据库
        try:
            report = TASK_MANAGER.get_report_by_task(task_id)
            if report:
                REPORT_DAO.create(task_id, report.to_dict())
        except Exception:
            pass
        # ⭐ 发送邮件通知
        try:
            report = TASK_MANAGER.get_report_by_task(task_id)
            if report:
                EMAIL_NOTIFIER.notify_task_completed(report.to_dict())
        except Exception:
            pass
        # ⭐ 同步持久化完成状态
        try:
            TASK_DAO.update_status(task_id, "completed")
        except Exception:
            pass
    
    t = threading.Thread(target=worker, daemon=True)
    t.start()
    
    return {"ok": True, "task_id": task_id, "status": "running"}


@APP.get("/api/tasks")
def api_list_tasks(limit: int = 20):
    """列出最近的任务"""
    tasks = TASK_MANAGER.list_tasks(limit)
    if not tasks:
        # 内存为空，从数据库恢复
        try:
            db_records = TASK_DAO.list_recent(limit)
            tasks = [
                {
                    "task_id": r.id,
                    "name": r.name,
                    "case_names": [],
                    "env_id": r.env_id or "",
                    "concurrency": 3,
                    "status": r.status,
                    "created_at": r.created_at or "",
                    "started_at": r.started_at or "",
                    "finished_at": r.finished_at or "",
                    "total_count": r.total_count or 0,
                    "passed_count": r.passed_count or 0,
                    "failed_count": r.failed_count or 0,
                    "duration_s": round(r.duration_s or 0, 2),
                    "pass_rate": round(
                        (r.passed_count or 0) * 100.0 / r.total_count, 1
                    ) if r.total_count else 0,
                    "results": [],
                }
                for r in db_records
            ]
        except Exception:
            tasks = []
    return tasks


@APP.get("/api/tasks/{task_id}")
def api_get_task(task_id: str):
    """获取任务详情"""
    task = TASK_MANAGER.get_task(task_id)
    if not task:
        raise HTTPException(404, f"任务不存在: {task_id}")
    return task.to_dict()


@APP.get("/api/tasks/{task_id}/report")
def api_get_task_report(task_id: str):
    """获取任务执行报告"""
    report = TASK_MANAGER.get_report_by_task(task_id)
    if report:
        return report.to_dict()
    # fallback 到数据库
    try:
        db_report = REPORT_DAO.get_by_task_id(task_id)
        if db_report:
            return db_report
    except Exception:
        pass
    raise HTTPException(404, f"报告不存在: {task_id}")


@APP.get("/api/tasks/{task_id}/report/export")
def api_export_report(task_id: str, format: str = "html"):
    """导出报告为 HTML 文件"""
    # 获取报告数据（内存或数据库）
    report = TASK_MANAGER.get_report_by_task(task_id)
    if report:
        report_data = report.to_dict()
    else:
        try:
            report_data = REPORT_DAO.get_by_task_id(task_id)
        except Exception:
            report_data = None
        if not report_data:
            raise HTTPException(status_code=404, detail="Report not found")

    if format == "html":
        html_content = export_html(report_data)
        return HTMLResponse(
            content=html_content,
            headers={"Content-Disposition": f"attachment; filename=report_{task_id}.html"}
        )

    raise HTTPException(400, "Unsupported format")


@APP.get("/api/tasks/{task_id}/agent-evidence/{case_name:path}")
def api_task_agent_evidence(task_id: str, case_name: str):
    """生成 AI Agent 修复证据包。"""
    report = TASK_MANAGER.get_report_by_task(task_id)
    if report:
        report_data = report.to_dict()
    else:
        try:
            report_data = REPORT_DAO.get_by_task_id(task_id)
        except Exception:
            report_data = None
    if not report_data:
        raise HTTPException(status_code=404, detail="Report not found")

    case_result = next(
        (item for item in report_data.get("case_results", []) if item.get("name") == case_name),
        None,
    )
    if not case_result:
        raise HTTPException(status_code=404, detail=f"Case not found in report: {case_name}")

    run_id = case_result.get("run_id") or ""
    run_events = LOG_STORE.read_run(run_id) if run_id else []
    package = build_repair_evidence_package(
        task_id=task_id,
        case_name=case_name,
        report_data=report_data,
        case_path=case_path_from_name(case_name),
        run_events=run_events,
        skill_root=SKILL_ROOT,
    )
    evidence_path = save_repair_evidence_package(
        package,
        skill_path("logs/agent_evidence"),
    )
    package["evidence_path"] = str(evidence_path.relative_to(SKILL_ROOT))
    return package


@APP.get("/api/reports")
def api_list_reports(limit: int = 20):
    """列出所有执行报告"""
    # 先从内存获取
    reports = []
    for task in TASK_MANAGER.list_tasks(limit):
        rpt = TASK_MANAGER.get_report_by_task(task["task_id"])
        if rpt:
            reports.append(rpt.to_dict())
    if reports:
        return reports
    # fallback 数据库
    try:
        return REPORT_DAO.list_recent(limit)
    except Exception:
        return []


@APP.get("/api/reports/trend")
def api_reports_trend(days: int = 14):
    """获取报告趋势数据"""
    try:
        return REPORT_DAO.list_trend(days)
    except Exception:
        return []


@APP.get("/api/cases/{name:path}/stability")
def api_case_stability(name: str, limit: int = 10):
    """获取用例稳定性数据"""
    try:
        return REPORT_DAO.get_case_stability(name, limit)
    except Exception:
        return {"case_name": name, "total_runs": 0, "passed": 0, "failed": 0, "pass_rate": 0, "is_flaky": False}


@APP.get("/api/reports/{report_id}/compare/{baseline_id}")
def api_compare_reports(report_id: str, baseline_id: str):
    """对比两份报告"""
    try:
        current = REPORT_DAO.get_by_task_id(report_id)
        baseline = REPORT_DAO.get_by_task_id(baseline_id)
        if not current or not baseline:
            raise HTTPException(404, "Report not found")

        # 提取两份报告的用例结果
        current_cases = {r["name"]: r["passed"] for r in (current.get("case_results") or [])}
        baseline_cases = {r["name"]: r["passed"] for r in (baseline.get("case_results") or [])}

        new_failures = []   # 新增失败
        fixed = []          # 修复成功
        still_failing = []  # 持续失败

        for name, passed in current_cases.items():
            baseline_passed = baseline_cases.get(name)
            if baseline_passed is None:
                continue  # 新增用例，跳过对比
            if not passed and baseline_passed:
                new_failures.append(name)
            elif passed and not baseline_passed:
                fixed.append(name)
            elif not passed and not baseline_passed:
                still_failing.append(name)

        return {
            "current_id": report_id,
            "baseline_id": baseline_id,
            "new_failures": new_failures,
            "fixed": fixed,
            "still_failing": still_failing,
            "summary": {
                "current_pass_rate": current.get("summary", {}).get("pass_rate", 0),
                "baseline_pass_rate": baseline.get("summary", {}).get("pass_rate", 0),
            }
        }
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(500, str(e))


@APP.get("/api/execution_history")
def api_execution_history(limit: int = 50):
    """获取执行历史（增强版，包含业务描述）"""
    history = EXECUTION_HISTORY.get_recent(limit)
    # ⭐ 丰富返回数据
    return {
        "records": history,
        "summary": {
            "total": len(history),
            "passed": sum(1 for r in history if r.get("passed")),
            "failed": sum(1 for r in history if not r.get("passed")),
        }
    }


# ============================================================
# 静态页面
# ============================================================
STATIC_DIR = Path(__file__).parent / "static"


@APP.get("/")
def serve_index():
    index = STATIC_DIR / "index.html"
    if index.exists():
        return FileResponse(index)
    return JSONResponse(
        content={"error": "前端文件未就绪。index.html 尚未创建。",
                 "expected": str(index)},
        status_code=503,
    )


if STATIC_DIR.exists():
    APP.mount("/static", StaticFiles(directory=str(STATIC_DIR)), name="static")


# ============================================================
# 启动
# ============================================================
def main():
    # 启动时自动加载项目 .env，确保 COSMIC_LOGIN_SCRIPT 等环境变量可用
    _load_dotenv()

    ap = argparse.ArgumentParser(description="cosmic-replay Web UI")
    ap.add_argument("--port", type=int, default=None, help="端口（默认读配置 8765）")
    ap.add_argument("--host", default=None, help="监听地址（默认读配置 127.0.0.1）")
    ap.add_argument("--env", default=None, help="默认环境 id（仅本次启动）")
    ap.add_argument("--no-browser", action="store_true", help="不自动开浏览器")
    ap.add_argument("--init", action="store_true", help="从 config.example/ 初始化 config/")
    ap.add_argument("--force", action="store_true", help="配合 --init 强制覆盖")
    args = ap.parse_args()

    if args.init:
        try:
            created = CONFIG.init_from_example(force=args.force)
            if created:
                print(f"✓ 已初始化 config/（从 config.example/ 拷贝）")
            else:
                print(f"config/ 已存在（加 --force 覆盖）")
            CONFIG.reload()
        except Exception as e:
            print(f"✗ 初始化失败: {e}")
            sys.exit(1)

    port = args.port or CONFIG.webui.port
    host = args.host or CONFIG.webui.host
    if args.env:
        CONFIG.webui.default_env = args.env

    url = f"http://{host}:{port}"
    print(f"")
    print(f"  cosmic-replay Web UI")
    print(f"  → {url}")
    print(f"")
    if not CONFIG_DIR.exists():
        print(f"  ⚠ config/ 未初始化。首次用请运行: python -m lib.webui --init")
        print(f"")
    print(f"  默认环境: {CONFIG.webui.default_env}")
    print(f"  用例目录: {cases_dir()}")
    print(f"")

    if CONFIG.webui.open_browser and not args.no_browser:
        threading.Timer(1.0, lambda: webbrowser.open(url)).start()

    # 优先用 httptools（C 实现的 HTTP 解析器），fallback h11
    http_impl = "auto"
    try:
        import httptools  # noqa: F401
        http_impl = "httptools"
    except ImportError:
        pass

    uvicorn.run(APP, host=host, port=port, log_level="warning", http=http_impl)


if __name__ == "__main__":
    main()
