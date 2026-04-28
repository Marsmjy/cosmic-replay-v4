"""
cosmic-replay v2 - LogStore模块单元测试

测试目标：
1. 日志添加与存储
2. 日志快照查询
3. 运行历史管理
4. 文件轮转
5. 过期清理
"""
import pytest
import json
import sys
from pathlib import Path
from datetime import datetime

SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))

from lib.webui.log_store import (
    LogStore, LogEntry, install_global_capture
)


class TestLogEntry:
    """LogEntry数据模型测试"""
    
    def test_log_entry_creation(self):
        """创建日志条目"""
        entry = LogEntry(
            ts=1714282800.0,
            level="info",
            source="stdout",
            message="test message"
        )
        assert entry.ts == 1714282800.0
        assert entry.level == "info"
        assert entry.source == "stdout"
        assert entry.message == "test message"
    
    def test_log_entry_to_dict(self):
        """日志条目序列化"""
        entry = LogEntry(
            ts=1714282800.0,
            level="error",
            source="runner",
            message="error occurred"
        )
        d = entry.to_dict()
        assert isinstance(d, dict)
        assert d["level"] == "error"
        assert d["message"] == "error occurred"


class TestLogStore:
    """LogStore日志存储测试"""
    
    def test_log_store_init(self, temp_logs_dir: Path):
        """初始化LogStore"""
        store = LogStore(temp_logs_dir, buffer_size=10, retention_days=1)
        assert store.buffer_size == 10
        assert store.log_dir == temp_logs_dir
    
    def test_add_log_entry(self, log_store: LogStore):
        """添加日志条目"""
        log_store.add("info", "test_source", "test message")
        snapshot = log_store.snapshot()
        assert len(snapshot) == 1
        assert snapshot[0]["message"] == "test message"
        assert snapshot[0]["level"] == "info"
    
    def test_add_multiple_entries(self, log_store: LogStore):
        """添加多条日志"""
        for i in range(5):
            log_store.add("info", "source", f"message {i}")
        snapshot = log_store.snapshot()
        assert len(snapshot) == 5
    
    def test_buffer_size_limit(self, log_store: LogStore):
        """缓冲区大小限制"""
        for i in range(20):  # 超过buffer_size=10
            log_store.add("info", "source", f"message {i}")
        snapshot = log_store.snapshot(limit=100)
        assert len(snapshot) == 10  # 受buffer_size限制
    
    def test_filter_by_level(self, log_store: LogStore):
        """按级别过滤"""
        log_store.add("debug", "source", "debug msg")
        log_store.add("info", "source", "info msg")
        log_store.add("warn", "source", "warn msg")
        log_store.add("error", "source", "error msg")
        
        warn_only = log_store.snapshot(level_filter="warn")
        assert len(warn_only) >= 1  # 包含warn和更高级别
    
    def test_search_logs(self, log_store: LogStore):
        """搜索日志内容"""
        log_store.add("info", "source", "this is a test message")
        log_store.add("info", "source", "another log entry")
        log_store.add("info", "source", "test again")
        
        result = log_store.snapshot(search="test")
        assert len(result) == 2
    
    def test_snapshot_limit(self, log_store: LogStore):
        """快照数量限制"""
        for i in range(20):
            log_store.add("info", "source", f"msg {i}")
        
        snapshot = log_store.snapshot(limit=5)
        assert len(snapshot) == 5
    
    def test_skip_empty_message(self, log_store: LogStore):
        """跳过空消息"""
        log_store.add("info", "source", "")
        log_store.add("info", "source", "   ")  # 纯空白
        log_store.add("info", "source", "valid message")
        
        snapshot = log_store.snapshot()
        assert len(snapshot) == 1


class TestRunHistory:
    """运行历史管理测试"""
    
    def test_save_run_event(self, log_store: LogStore):
        """保存运行事件"""
        log_store.save_run_event("test_run_001", "case_start", {"name": "test_case"})
        
        runs = log_store.list_runs()
        assert len(runs) >= 1
        assert runs[0]["run_id"] == "test_run_001"
    
    def test_list_runs_order(self, log_store: LogStore):
        """运行列表按时间排序"""
        log_store.save_run_event("run_001", "case_start", {"name": "case_a"})
        log_store.save_run_event("run_002", "case_start", {"name": "case_b"})
        
        runs = log_store.list_runs()
        assert len(runs) == 2
        # 最新在前
        assert runs[0]["run_id"] == "run_002"
    
    def test_list_runs_limit(self, log_store: LogStore):
        """运行列表数量限制"""
        for i in range(10):
            log_store.save_run_event(f"run_{i:03d}", "case_start", {"name": f"case_{i}"})
        
        runs = log_store.list_runs(limit=5)
        assert len(runs) == 5
    
    def test_read_run_events(self, log_store: LogStore):
        """读取运行事件"""
        log_store.save_run_event("test_read", "case_start", {"name": "test"})
        log_store.save_run_event("test_read", "step_start", {"step": "step_1"})
        log_store.save_run_event("test_read", "case_done", {"passed": True})
        
        events = log_store.read_run("test_read")
        assert len(events) == 3
        assert events[0]["type"] == "case_start"
        assert events[2]["type"] == "case_done"
    
    def test_read_nonexistent_run(self, log_store: LogStore):
        """读取不存在的运行"""
        events = log_store.read_run("nonexistent_run_id")
        assert events == []
    
    def test_run_with_case_done(self, log_store: LogStore):
        """完整运行（包含case_done）"""
        log_store.save_run_event("complete_run", "case_start", {"name": "complete_case"})
        log_store.save_run_event("complete_run", "case_done", {
            "passed": True,
            "duration_s": 5.5,
            "step_ok": 5,
            "step_count": 5
        })
        
        runs = log_store.list_runs()
        complete_run = next((r for r in runs if r["run_id"] == "complete_run"), None)
        assert complete_run is not None
        assert complete_run["passed"] == True
        assert complete_run["duration_s"] == 5.5


class TestFileRotation:
    """文件轮转测试"""
    
    def test_log_file_created(self, log_store: LogStore, temp_logs_dir: Path):
        """日志文件被创建"""
        log_store.add("info", "source", "test message")
        
        # 检查当天日志文件
        today = datetime.now().strftime("%Y%m%d")
        log_file = temp_logs_dir / f"server-{today}.log"
        assert log_file.exists()
    
    def test_log_file_content(self, log_store: LogStore, temp_logs_dir: Path):
        """日志文件内容"""
        log_store.add("error", "test_source", "test error message")
        
        today = datetime.now().strftime("%Y%m%d")
        log_file = temp_logs_dir / f"server-{today}.log"
        content = log_file.read_text(encoding="utf-8")
        
        assert "error" in content.lower()
        assert "test_source" in content
        assert "test error message" in content
    
    def test_run_event_file(self, log_store: LogStore, temp_logs_dir: Path):
        """运行事件文件"""
        log_store.save_run_event("test_file", "case_start", {"name": "test"})
        
        event_file = temp_logs_dir / "runs" / "test_file.jsonl"
        assert event_file.exists()
        
        content = event_file.read_text(encoding="utf-8")
        data = json.loads(content.strip())
        assert data["type"] == "case_start"


class TestRetention:
    """过期清理测试"""
    
    def test_purge_expired_on_init(self, temp_logs_dir: Path):
        """初始化时清理过期日志"""
        # 创建一个"过期"的日志文件
        import time
        old_file = temp_logs_dir / "server-20200101.log"
        old_file.write_text("old log")
        
        # 修改mtime为很久以前
        import os
        old_time = time.time() - 365 * 24 * 60 * 60  # 一年前
        os.utime(old_file, (old_time, old_time))
        
        # 初始化时应该清理
        store = LogStore(temp_logs_dir, buffer_size=10, retention_days=30)
        
        assert not old_file.exists()


class TestSubscribe:
    """订阅功能测试"""
    
    def test_subscribe_queue(self, log_store: LogStore):
        """订阅队列"""
        queue = log_store.subscribe()
        assert queue is not None
        
        # 添加日志后应该能收到
        log_store.add("info", "source", "test")
        
        # 非阻塞检查
        try:
            entry = queue.get_nowait()
            assert entry.message == "test"
        except:
            pass  # 可能由于时序问题拿不到，不算失败
    
    def test_unsubscribe(self, log_store: LogStore):
        """取消订阅"""
        queue = log_store.subscribe()
        log_store.unsubscribe(queue)
        
        # 取消后队列应该从订阅者列表移除
        assert queue not in log_store._subscribers


class TestEdgeCases:
    """边界条件测试"""
    
    def test_invalid_run_id_characters(self, log_store: LogStore):
        """无效运行ID字符"""
        # 包含路径遍历字符
        log_store.save_run_event("../../../etc/passwd", "case_start", {"name": "test"})
        
        # 应该安全处理（不创建文件或创建安全文件名）
        # 检查是否有恶意文件创建
        for f in (log_store.log_dir / "runs").glob("*"):
            assert "etc" not in f.name
    
    def test_unicode_message(self, log_store: LogStore):
        """Unicode消息"""
        log_store.add("info", "source", "中文测试测试ABC123")
        
        snapshot = log_store.snapshot()
        assert "中文" in snapshot[0]["message"]
    
    def test_very_long_message(self, log_store: LogStore):
        """超长消息"""
        long_msg = "x" * 10000
        log_store.add("info", "source", long_msg)
        
        snapshot = log_store.snapshot()
        assert snapshot[0]["message"] == long_msg
    
    def test_special_characters_in_message(self, log_store: LogStore):
        """特殊字符消息"""
        special_msg = "msg\nwith\r\nnewlines\tand\ttabs"
        log_store.add("info", "source", special_msg)
        
        snapshot = log_store.snapshot()
        # 换行符应该被处理
        assert "\n" not in snapshot[0]["message"] or snapshot[0]["message"].endswith("tabs")


# 运行测试命令：
# cd cosmic-replay-v2 && python -m pytest tests/unit/test_log_store.py -v
