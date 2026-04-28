"""数据库初始化脚本 - 创建所有表

运行方法：
    python -m lib.db.init_db
"""
import sqlite3
from pathlib import Path

DB_PATH = Path(__file__).parent.parent.parent / "cosmic_replay.db"

SCHEMA = """
-- 环境配置表
CREATE TABLE IF NOT EXISTS envs (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    base_url TEXT NOT NULL,
    credentials_json TEXT,
    basedata_json TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用例表
CREATE TABLE IF NOT EXISTS cases (
    name TEXT PRIMARY KEY,
    yaml_path TEXT NOT NULL,
    description TEXT,
    tags_json TEXT,
    main_form_id TEXT,
    step_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 任务表
CREATE TABLE IF NOT EXISTS tasks (
    task_id TEXT PRIMARY KEY,
    name TEXT,
    status TEXT DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    total_count INTEGER DEFAULT 0,
    passed_count INTEGER DEFAULT 0,
    failed_count INTEGER DEFAULT 0,
    duration_s REAL DEFAULT 0
);

-- 执行历史表
CREATE TABLE IF NOT EXISTS run_history (
    run_id TEXT PRIMARY KEY,
    task_id TEXT,
    case_name TEXT NOT NULL,
    env_id TEXT,
    passed INTEGER DEFAULT 0,
    step_ok INTEGER DEFAULT 0,
    step_count INTEGER DEFAULT 0,
    duration_s REAL DEFAULT 0,
    error TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);

-- 步骤结果表
CREATE TABLE IF NOT EXISTS step_results (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT NOT NULL,
    step_index INTEGER,
    step_id TEXT,
    description TEXT,
    passed INTEGER DEFAULT 0,
    response_status INTEGER,
    duration_ms INTEGER,
    error TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (run_id) REFERENCES run_history(run_id)
);

-- 日志表
CREATE TABLE IF NOT EXISTS logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT,
    level TEXT DEFAULT 'info',
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_run_history_case ON run_history(case_name);
CREATE INDEX IF NOT EXISTS idx_run_history_time ON run_history(started_at);
CREATE INDEX IF NOT EXISTS idx_logs_level ON logs(level);
CREATE INDEX IF NOT EXISTS idx_logs_time ON logs(created_at);
"""

def init_database(db_path: Path = DB_PATH):
    """初始化数据库"""
    db_path.parent.mkdir(parents=True, exist_ok=True)
    
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # 执行建表语句
    cursor.executescript(SCHEMA)
    
    conn.commit()
    conn.close()
    
    print(f"数据库已初始化: {db_path}")

if __name__ == "__main__":
    init_database()
