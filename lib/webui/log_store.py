"""日志存储 - 环形内存缓冲 + 按天文件落盘 + 过期清理

两种日志：
1. 服务日志（stdout/stderr/logging）→ logs/server-YYYYMMDD.log
   同时推环形缓冲，供 /api/logs 和 SSE 订阅
2. 用例执行历史 → logs/runs/<run_id>.jsonl（每行一个 SSE 事件）
   供 /api/runs 列出、/api/runs/<id>/events 回放

清理：启动时删 30 天前的 logs/server-*.log 和 logs/runs/*.jsonl
"""
from __future__ import annotations

import json
import logging
import os
import queue
import re
import sys
import threading
import time
from collections import deque
from dataclasses import dataclass, asdict
from datetime import datetime, timedelta
from pathlib import Path
from typing import Any


# ============================================================
# 数据模型
# ============================================================
@dataclass
class LogEntry:
    ts: float                 # time.time()
    level: str                # "debug" | "info" | "warn" | "error"
    source: str               # "stdout" | "stderr" | "logger" | "runner" | "exception"
    message: str

    def to_dict(self) -> dict:
        return asdict(self)


# ============================================================
# 日志存储（单例）
# ============================================================
class LogStore:
    """进程级日志收集。"""

    def __init__(self, log_dir: Path, buffer_size: int = 500,
                 retention_days: int = 30):
        self.log_dir = Path(log_dir)
        self.log_dir.mkdir(parents=True, exist_ok=True)
        (self.log_dir / "runs").mkdir(parents=True, exist_ok=True)

        self.buffer: deque[LogEntry] = deque(maxlen=buffer_size)
        self.retention_days = retention_days
        self._lock = threading.Lock()
        # SSE 订阅者
        self._subscribers: list[queue.Queue] = []

        # 启动时清理过期日志
        self.purge_expired()
        # 当天文件
        self._open_today_file()

    # ---------- 文件 ----------

    def _open_today_file(self):
        today = datetime.now().strftime("%Y%m%d")
        self._current_file_path = self.log_dir / f"server-{today}.log"
        # 用 append 模式，每次写都 flush
        self._fp = self._current_file_path.open("a", encoding="utf-8")

    def _rotate_if_needed(self):
        expected = self.log_dir / f"server-{datetime.now().strftime('%Y%m%d')}.log"
        if expected != self._current_file_path:
            try:
                self._fp.close()
            except Exception:
                pass
            self._open_today_file()

    def purge_expired(self):
        """删 retention_days 天前的日志文件"""
        cutoff = time.time() - self.retention_days * 86400
        for pattern in ("server-*.log", "runs/*.jsonl"):
            for f in self.log_dir.glob(pattern):
                try:
                    if f.stat().st_mtime < cutoff:
                        f.unlink()
                except Exception:
                    pass

    # ---------- 写入 ----------

    def add(self, level: str, source: str, message: str):
        """对外入口：追加一条日志到缓冲/文件/订阅者"""
        if not message or not message.strip():
            return
        entry = LogEntry(
            ts=time.time(),
            level=(level or "info").lower(),
            source=source,
            message=message.rstrip("\n"),
        )
        with self._lock:
            self._rotate_if_needed()
            self.buffer.append(entry)
            try:
                line = f"[{datetime.fromtimestamp(entry.ts).strftime('%H:%M:%S')}] " \
                       f"{entry.level.upper():5s} [{entry.source}] {entry.message}\n"
                self._fp.write(line)
                self._fp.flush()
            except Exception:
                pass
            # 推送给 SSE 订阅者
            dead = []
            for sub in self._subscribers:
                try:
                    sub.put_nowait(entry)
                except Exception:
                    dead.append(sub)
            for d in dead:
                try: self._subscribers.remove(d)
                except: pass

    # ---------- 读取 ----------

    def snapshot(self, level_filter: str | None = None,
                 search: str | None = None, limit: int = 500) -> list[dict]:
        with self._lock:
            items = list(self.buffer)
        out = []
        levels_priority = {"debug": 0, "info": 1, "warn": 2, "error": 3}
        min_level = levels_priority.get((level_filter or "").lower(), -1)
        needle = (search or "").lower()
        for e in items:
            if min_level >= 0 and levels_priority.get(e.level, 1) < min_level:
                continue
            if needle and needle not in e.message.lower():
                continue
            out.append(e.to_dict())
            if len(out) >= limit:
                break
        return out

    def subscribe(self) -> queue.Queue:
        q: queue.Queue = queue.Queue(maxsize=1000)
        with self._lock:
            self._subscribers.append(q)
        return q

    def unsubscribe(self, q: queue.Queue):
        with self._lock:
            try: self._subscribers.remove(q)
            except ValueError: pass

    # ---------- 运行历史 ----------

    def save_run_event(self, run_id: str, event_type: str, payload: dict):
        """把一次 run 的每个 SSE 事件追加到 logs/runs/<id>.jsonl"""
        f = self.log_dir / "runs" / f"{run_id}.jsonl"
        record = {"ts": time.time(), "type": event_type, "data": payload}
        try:
            with f.open("a", encoding="utf-8") as fp:
                fp.write(json.dumps(record, ensure_ascii=False) + "\n")
        except Exception as e:
            self.add("warn", "logstore", f"save_run_event 失败: {e}")

    def list_runs(self, limit: int = 100) -> list[dict]:
        """列最近的 run 历史（按 mtime 倒序）"""
        runs_dir = self.log_dir / "runs"
        if not runs_dir.exists():
            return []
        items = []
        for f in sorted(runs_dir.glob("*.jsonl"),
                        key=lambda p: p.stat().st_mtime, reverse=True)[:limit]:
            run_id = f.stem
            mtime = f.stat().st_mtime
            # 粗读：从首行拿 case_start，末行拿 case_done
            case_name = ""
            passed = None
            duration_s = None
            step_ok = step_fail = 0
            try:
                lines = f.read_text(encoding="utf-8").splitlines()
                for line in lines:
                    try:
                        rec = json.loads(line)
                    except Exception:
                        continue
                    if rec.get("type") == "case_start":
                        case_name = (rec.get("data") or {}).get("name", "")
                    elif rec.get("type") == "case_done":
                        d = rec.get("data") or {}
                        passed = d.get("passed")
                        duration_s = d.get("duration_s")
                        step_ok = d.get("step_ok", 0)
                        step_fail = d.get("step_fail", 0)
            except Exception:
                pass
            items.append({
                "run_id": run_id,
                "case_name": case_name,
                "passed": passed,
                "mtime": mtime,
                "duration_s": duration_s,
                "step_ok": step_ok,
                "step_fail": step_fail,
            })
        return items

    def read_run(self, run_id: str) -> list[dict]:
        """返回某个 run 的全部事件（按时间）"""
        safe = re.sub(r"[^a-zA-Z0-9_\-]", "", run_id)
        f = self.log_dir / "runs" / f"{safe}.jsonl"
        if not f.exists():
            return []
        out = []
        for line in f.read_text(encoding="utf-8").splitlines():
            try:
                out.append(json.loads(line))
            except Exception:
                continue
        return out


# ============================================================
# 重定向 stdout / stderr / logging 到 LogStore
# ============================================================
class _StreamToStore:
    """文件类对象，把 write 转给 LogStore"""
    def __init__(self, store: LogStore, level: str, source: str, original):
        self.store = store
        self.level = level
        self.source = source
        self.original = original      # 原 stdout/stderr，继续往终端写
        self._buf = ""

    def write(self, s: str):
        try:
            self.original.write(s)
            self.original.flush()
        except Exception:
            pass
        # 按行缓冲后推给 store
        self._buf += s
        while "\n" in self._buf:
            line, self._buf = self._buf.split("\n", 1)
            if line.strip():
                self.store.add(self.level, self.source, line)

    def flush(self):
        try: self.original.flush()
        except: pass

    def isatty(self):
        try: return self.original.isatty()
        except: return False


class _StoreLogHandler(logging.Handler):
    """logging → LogStore"""
    def __init__(self, store: LogStore):
        super().__init__()
        self.store = store

    def emit(self, record: logging.LogRecord):
        try:
            msg = self.format(record)
        except Exception:
            msg = record.getMessage()
        level = "error" if record.levelno >= logging.ERROR \
            else "warn" if record.levelno >= logging.WARNING \
            else "info" if record.levelno >= logging.INFO \
            else "debug"
        self.store.add(level, "logger", msg)


def install_global_capture(store: LogStore):
    """把全局 stdout/stderr/logging 都接到 store。幂等。"""
    if getattr(install_global_capture, "_installed", False):
        return
    install_global_capture._installed = True

    sys.stdout = _StreamToStore(store, "info", "stdout", sys.stdout)
    sys.stderr = _StreamToStore(store, "error", "stderr", sys.stderr)

    root = logging.getLogger()
    handler = _StoreLogHandler(store)
    handler.setFormatter(logging.Formatter("%(name)s: %(message)s"))
    root.addHandler(handler)
    # 保留原有 handler（不清理）
    if root.level > logging.INFO:
        root.setLevel(logging.INFO)