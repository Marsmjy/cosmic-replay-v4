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
from datetime import date, datetime

# ⭐ 导入任务管理器
from lib.task_manager import TASK_MANAGER, CaseResult, ExecutionTask


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
    from fastapi.responses import FileResponse, StreamingResponse, JSONResponse
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
from lib.webui.log_store import LogStore, install_global_capture


# ============================================================
# 全局状态
# ============================================================
CONFIG = Config()
APP = FastAPI(title="cosmic-replay", version="0.1.0")
_start_time = time.time()  # ⭐ P0-3: 启动时间，用于健康检查

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
        except Exception as e:
            meta["parse_error"] = str(e)
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


@APP.delete("/api/cases/{name:path}")
def api_delete_case(name: str):
    p = case_path_from_name(name)
    if p.exists():
        p.unlink()
    return {"ok": True}


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
    return case


@APP.post("/api/cases/{name:path}/run")
def api_run_case(name: str, body: dict = Body(default={})):
    """启动异步执行。返回 run_id。前端用 /api/runs/{run_id}/events 订阅事件。"""
    p = case_path_from_name(name)
    if not p.exists():
        raise HTTPException(404, f"用例不存在: {name}")

    env_id = body.get("env_id") or CONFIG.webui.default_env
    try:
        case = load_yaml(p)
    except Exception as e:
        raise HTTPException(400, f"YAML 解析失败: {e}")

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
    try:
        # ⭐ 热重载 har_extractor，确保用最新代码
        import importlib
        importlib.reload(har_extractor)

        preview = har_extractor.preview_har(save_path)
        return {
            "ok": True,
            "preview": preview,
            "har_file": save_path.name,   # 后续 extract 用这个
        }
    except Exception as e:
        return JSONResponse(
            status_code=500,
            content={"ok": False, "error": f"{type(e).__name__}: {e}"},
        )


@APP.post("/api/har/extract")
def api_har_extract(body: dict = Body(...)):
    """由上一个 preview 的结果生成 YAML 写入 cases/"""
    har_file = body.get("har_file")
    case_name = body.get("case_name") or "untitled"
    var_overrides = body.get("var_overrides")  # ⭐ 用户变量配置
    if not har_file:
        raise HTTPException(400, "缺少 har_file")
    har_path = har_upload_dir() / har_file
    if not har_path.exists():
        raise HTTPException(404, f"HAR 文件不存在: {har_file}")

    out_path = case_path_from_name(case_name)
    overwritten = out_path.exists()

    try:
        # ⭐ 重新导入时强制使用最新 har_extractor 代码（模块级热重载）
        import importlib
        importlib.reload(har_extractor)

        yaml_text = har_extractor.build_yaml_case(har_path, case_name, var_overrides=var_overrides)
        out_path.parent.mkdir(parents=True, exist_ok=True)
        out_path.write_text(yaml_text, encoding="utf-8")
        action = "覆盖" if overwritten else "生成"
        return {"ok": True, "name": case_name, "file": str(out_path.relative_to(SKILL_ROOT)),
                "overwritten": overwritten, "action": action}
    except Exception as e:
        raise HTTPException(500, f"抽取失败: {e}")


# ============================================================
# Endpoint: 任务管理（批量执行）
# ============================================================
@APP.post("/api/tasks")
def api_create_task(body: dict = Body(...)):
    """创建批量执行任务"""
    case_names = body.get("case_names", [])
    env_id = body.get("env_id") or CONFIG.webui.default_env
    name = body.get("name", "")
    
    if not case_names:
        raise HTTPException(400, "缺少用例列表")
    
    # 创建任务
    task = TASK_MANAGER.create_task(case_names, env_id, name)
    
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
    
    # 更新状态
    TASK_MANAGER.update_task_status(task_id, "running")
    
    # 启动执行线程
    def worker():
        for case_name in task.case_names:
            try:
                p = case_path_from_name(case_name)
                if not p.exists():
                    TASK_MANAGER.add_result(task_id, CaseResult(
                        name=case_name,
                        passed=False,
                        error=f"用例文件不存在",
                    ))
                    continue
                
                case = load_yaml(p)
                case = _merge_env_into_case(case, task.env_id)
                
                run_id = uuid.uuid4().hex[:12]
                sess = RunSession(run_id, case_name, task.env_id)
                RUNS[run_id] = sess
                
                start_time = time.time()
                result = CaseResult(name=case_name, passed=False, run_id=run_id)  # ⭐ 记录run_id用于跳转执行历史
                
                def capture_event(evt_type, payload):
                    sess.emit(evt_type, payload)
                    # ⭐ 捕获阶段信息用于报告
                    if evt_type == "step_ok":
                        result.step_ok += 1
                        result.step_count += 1
                    elif evt_type == "step_fail":
                        result.step_count += 1
                        if not result.error:
                            result.error = payload.get("error", "步骤失败")
                
                try:
                    run_case(case, on_event=capture_event)
                    result.passed = True
                except Exception as e:
                    result.passed = False
                    result.error = str(e)[:200]
                finally:
                    result.duration_s = time.time() - start_time
                    TASK_MANAGER.add_result(task_id, result)
                    sess.close()
                    
            except Exception as e:
                import traceback
                TASK_MANAGER.add_result(task_id, CaseResult(
                    name=case_name,
                    passed=False,
                    error=f"{type(e).__name__}: {str(e)[:200]}",
                ))
        
        # 生成报告
        TASK_MANAGER.update_task_status(task_id, "completed")
        TASK_MANAGER.generate_report(task_id)
    
    t = threading.Thread(target=worker, daemon=True)
    t.start()
    
    return {"ok": True, "task_id": task_id, "status": "running"}


@APP.get("/api/tasks")
def api_list_tasks(limit: int = 20):
    """列出最近的任务"""
    return TASK_MANAGER.list_tasks(limit)


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
    if not report:
        raise HTTPException(404, f"报告不存在: {task_id}")
    return report.to_dict()


@APP.get("/api/reports")
def api_list_reports(limit: int = 20):
    """列出所有执行报告"""
    reports = []
    for task in TASK_MANAGER.list_tasks(limit):
        rpt = TASK_MANAGER.get_report_by_task(task["task_id"])
        if rpt:
            reports.append(rpt.to_dict())
    return reports


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