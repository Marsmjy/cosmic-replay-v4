# cosmic-replay-v4 数据架构深度分析与优化方案

> 分析日期：2026-04-28  
> 分析角色：数据架构师

---

## 一、现有数据模型分析

### 1.1 核心数据实体

| 实体 | 源文件 | 存储方式 | 描述 |
|------|--------|----------|------|
| CaseResult | task_manager.py | 内存 OrderedDict | 单个用例执行结果 |
| ExecutionTask | task_manager.py | 内存 OrderedDict | 批量执行任务 |
| ExecutionReport | task_manager.py | 内存 OrderedDict | 执行报告汇总 |
| LogEntry | log_store.py | 内存 deque + 文件 | 日志条目 |
| RunResult | runner.py | 内存 | 单次运行结果 |
| WebUIPrefs | config.py | YAML 文件 | Web UI 配置 |
| Credentials | config.py | YAML 文件 | 环境凭证 |
| EnvConfig | config.py | YAML 文件 | 环境配置 |

### 1.2 数据模型定义（当前）

```
CaseResult:
  - name: str              # 用例名称
  - passed: bool           # 是否通过
  - step_ok: int           # 成功步骤数
  - step_count: int        # 总步骤数
  - duration_s: float      # 执行耗时
  - error: str             # 错误信息
  - phases: list[dict]     # 执行阶段详情

ExecutionTask:
  - task_id: str           # 任务ID
  - name: str              # 任务名称
  - case_names: list[str]  # 用例列表
  - env_id: str            # 环境ID
  - status: str            # pending|running|completed|cancelled
  - created_at: str        # 创建时间
  - started_at: str        # 开始时间
  - finished_at: str       # 结束时间
  - results: list[CaseResult]

LogEntry:
  - ts: float              # 时间戳
  - level: str             # debug|info|warn|error
  - source: str            # stdout|stderr|logger|runner|exception
  - message: str           # 日志内容
```

---

## 二、存储策略评估

### 2.1 当前存储架构

```
cosmic-replay-v4/
├── config/
│   ├── webui.yaml          # Web UI 配置
│   └── envs/*.yaml         # 环境配置
├── logs/
│   ├── server-YYYYMMDD.log # 服务日志（按天）
│   └── runs/
│       └── <run_id>.jsonl  # 运行事件日志
├── cases/
│   └── *.yaml              # 测试用例
└── har_uploads/
    └── *.har               # HAR 上传文件
```

### 2.2 存储问题分析

| 问题类型 | 严重程度 | 描述 |
|----------|----------|------|
| 无持久化数据库 | **高** | 所有执行数据仅内存存储，重启丢失 |
| 内存容量限制 | **高** | TaskManager 限100条，LogStore 限500条，History 限100条 |
| 无结构化查询 | **高** | 无法按用例/时间/状态高效查询历史 |
| 无数据备份 | **高** | 无自动备份机制，存在数据丢失风险 |
| JSONL 无索引 | **中** | 历史回放需全文件扫描，效率低 |
| 配置明文存储 | **中** | 密码以明文或简单环境变量存储 |

---

## 三、数据一致性与持久化评估

### 3.1 一致性问题

```
问题1: 内存状态与文件不同步
  - RunSession 运行中 crash → logs/runs/<id>.jsonl 部分写入
  - 无事务保护，无回滚机制

问题2: 并发写入风险
  - TaskManager 使用 threading.Lock 但仅保护内存结构
  - 多进程部署时无分布式锁

问题3: 配置热重载冲突
  - save_webui/save_env 写入后立即 _load()
  - 高并发下可能读到中间状态
```

### 3.2 持久化缺失影响

| 场景 | 影响 |
|------|------|
| 服务重启 | 所有运行中任务状态丢失 |
| 批量执行中途崩溃 | 无法恢复执行进度 |
| 历史趋势分析 | 无数据支撑 |
| 合规审计 | 无完整执行记录 |

---

## 四、查询效率与索引设计评估

### 4.1 当前查询模式

```python
# TaskManager.list_tasks - O(n) 线性扫描
tasks = list(self._tasks.values())[-limit:]

# LogStore.snapshot - O(n) 全量过滤
for e in items:
    if min_level >= 0 and levels_priority.get(e.level, 1) < min_level:
        continue

# LogStore.list_runs - O(n) 文件系统遍历 + 全文解析
for f in sorted(runs_dir.glob("*.jsonl")):
    lines = f.read_text().splitlines()  # 全文件读取
```

### 4.2 性能瓶颈

| 操作 | 当前复杂度 | 数据量瓶颈 |
|------|------------|------------|
| 列出最近任务 | O(n) | >1000条时变慢 |
| 按用例查询历史 | O(n) 全扫描 | 不可用 |
| 搜索日志 | O(n*m) | >10000条日志卡顿 |
| 历史趋势统计 | 无支持 | 需人工分析 |

---

## 五、数据库设计方案

### 5.1 推荐技术选型

| 场景 | 推荐方案 | 理由 |
|------|----------|------|
| 主数据库 | **SQLite** | 轻量、零配置、单文件、足够本项目规模 |
| 全文搜索 | SQLite FTS5 | 日志/错误消息全文检索 |
| 时序数据 | SQLite + 分区表 | 执行历史按月分区 |
| 缓存层 | Redis (可选) | 多实例部署时共享状态 |

### 5.2 数据库 Schema 设计

```sql
-- ============================================
-- 1. 环境配置表
-- ============================================
CREATE TABLE envs (
    id TEXT PRIMARY KEY,              -- 环境标识
    name TEXT NOT NULL,               -- 显示名称
    base_url TEXT NOT NULL,
    datacenter_id TEXT,
    sign_required INTEGER DEFAULT 1,
    timeout INTEGER DEFAULT 30,
    login_retries INTEGER DEFAULT 3,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE env_credentials (
    env_id TEXT NOT NULL,
    username TEXT,
    password_encrypted TEXT,          -- AES-256 加密存储
    username_env TEXT,
    password_env TEXT,
    PRIMARY KEY (env_id),
    FOREIGN KEY (env_id) REFERENCES envs(id) ON DELETE CASCADE
);

CREATE TABLE env_basedata (
    env_id TEXT NOT NULL,
    key TEXT NOT NULL,
    value TEXT,
    PRIMARY KEY (env_id, key),
    FOREIGN KEY (env_id) REFERENCES envs(id) ON DELETE CASCADE
);

-- ============================================
-- 2. 用例管理表
-- ============================================
CREATE TABLE cases (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,        -- 用例标识（相对路径）
    display_name TEXT,
    description TEXT,
    file_path TEXT NOT NULL,          -- YAML 文件路径
    tags TEXT,                        -- JSON 数组存储
    step_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cases_tags ON cases(tags);
CREATE INDEX idx_cases_name ON cases(name);

-- ============================================
-- 3. 执行任务表
-- ============================================
CREATE TABLE tasks (
    id TEXT PRIMARY KEY,              -- task_xxx
    name TEXT,
    status TEXT NOT NULL DEFAULT 'pending',  -- pending|running|completed|cancelled
    env_id TEXT NOT NULL,
    total_count INTEGER DEFAULT 0,
    passed_count INTEGER DEFAULT 0,
    failed_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    started_at TEXT,
    finished_at TEXT,
    duration_s REAL DEFAULT 0,
    FOREIGN KEY (env_id) REFERENCES envs(id)
);

CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_created ON tasks(created_at DESC);

-- ============================================
-- 4. 任务-用例关联表（批量执行详情）
-- ============================================
CREATE TABLE task_cases (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id TEXT NOT NULL,
    case_id INTEGER NOT NULL,
    case_name TEXT NOT NULL,
    run_id TEXT,                      -- 对应 run_history.id
    status TEXT DEFAULT 'pending',    -- pending|running|passed|failed|skipped
    step_ok INTEGER DEFAULT 0,
    step_count INTEGER DEFAULT 0,
    duration_s REAL DEFAULT 0,
    error_message TEXT,
    started_at TEXT,
    finished_at TEXT,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (case_id) REFERENCES cases(id)
);

CREATE INDEX idx_task_cases_task ON task_cases(task_id);
CREATE INDEX idx_task_cases_status ON task_cases(status);

-- ============================================
-- 5. 运行历史表（单次执行记录）
-- ============================================
CREATE TABLE run_history (
    id TEXT PRIMARY KEY,              -- run_id (uuid hex[:12])
    case_id INTEGER,
    case_name TEXT NOT NULL,
    task_id TEXT,                     -- 批量执行时关联
    env_id TEXT NOT NULL,
    status TEXT NOT NULL,             -- running|passed|failed|error
    passed INTEGER DEFAULT 0,
    step_ok INTEGER DEFAULT 0,
    step_count INTEGER DEFAULT 0,
    duration_s REAL DEFAULT 0,
    error_message TEXT,
    started_at TEXT DEFAULT CURRENT_TIMESTAMP,
    finished_at TEXT,
    FOREIGN KEY (case_id) REFERENCES cases(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (env_id) REFERENCES envs(id)
);

CREATE INDEX idx_run_case ON run_history(case_name);
CREATE INDEX idx_run_status ON run_history(status);
CREATE INDEX idx_run_time ON run_history(started_at DESC);

-- ============================================
-- 6. 步骤执行详情表
-- ============================================
CREATE TABLE step_results (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT NOT NULL,
    step_id TEXT NOT NULL,
    step_type TEXT NOT NULL,
    step_detail TEXT,
    optional INTEGER DEFAULT 0,
    passed INTEGER DEFAULT 0,
    duration_ms INTEGER DEFAULT 0,
    error_message TEXT,
    request_snapshot TEXT,            -- JSON: 解析后的请求参数
    response_snapshot TEXT,           -- JSON: 响应数据截断版
    executed_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (run_id) REFERENCES run_history(id) ON DELETE CASCADE
);

CREATE INDEX idx_step_run ON step_results(run_id);

-- ============================================
-- 7. 日志表（替代纯文件存储）
-- ============================================
CREATE TABLE logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT,                      -- 关联运行
    ts REAL NOT NULL,
    level TEXT NOT NULL,
    source TEXT NOT NULL,
    message TEXT NOT NULL,
    FOREIGN KEY (run_id) REFERENCES run_history(id) ON DELETE SET NULL
);

CREATE INDEX idx_logs_time ON logs(ts DESC);
CREATE INDEX idx_logs_level ON logs(level);
CREATE INDEX idx_logs_run ON logs(run_id);

-- FTS5 全文搜索
CREATE VIRTUAL TABLE logs_fts USING fts5(
    message,
    content='logs',
    content_rowid='id',
    tokenize='unicode61'
);

-- 触发器：自动同步到 FTS
CREATE TRIGGER logs_ai AFTER INSERT ON logs BEGIN
    INSERT INTO logs_fts(rowid, message) VALUES (new.id, new.message);
END;

-- ============================================
-- 8. 断言结果表
-- ============================================
CREATE TABLE assertions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT NOT NULL,
    assertion_type TEXT NOT NULL,
    passed INTEGER DEFAULT 0,
    message TEXT,
    FOREIGN KEY (run_id) REFERENCES run_history(id) ON DELETE CASCADE
);

CREATE INDEX idx_assertion_run ON assertions(run_id);

-- ============================================
-- 9. 执行报告表
-- ============================================
CREATE TABLE reports (
    id TEXT PRIMARY KEY,              -- rpt_taskid
    task_id TEXT NOT NULL UNIQUE,
    generated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    total_cases INTEGER DEFAULT 0,
    passed_cases INTEGER DEFAULT 0,
    failed_cases INTEGER DEFAULT 0,
    skipped_cases INTEGER DEFAULT 0,
    total_steps INTEGER DEFAULT 0,
    passed_steps INTEGER DEFAULT 0,
    failed_steps INTEGER DEFAULT 0,
    total_duration_s REAL DEFAULT 0,
    pass_rate REAL DEFAULT 0,
    error_summary TEXT,               -- JSON: 错误汇总
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE INDEX idx_reports_task ON reports(task_id);

-- ============================================
-- 10. 修复建议表（Advisor 结果）
-- ============================================
CREATE TABLE fix_suggestions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id TEXT NOT NULL,
    error_type TEXT,
    field_key TEXT,
    field_caption TEXT,
    diagnosis TEXT,
    suggested_value TEXT,
    patch_yaml TEXT,
    confidence REAL DEFAULT 0,
    applied INTEGER DEFAULT 0,        -- 用户是否已采纳
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (run_id) REFERENCES run_history(id) ON DELETE CASCADE
);

CREATE INDEX idx_fix_run ON fix_suggestions(run_id);
```

### 5.3 视图设计

```sql
-- 用例执行统计视图
CREATE VIEW case_stats AS
SELECT 
    c.name,
    c.display_name,
    COUNT(r.id) AS total_runs,
    SUM(CASE WHEN r.passed = 1 THEN 1 ELSE 0 END) AS passed_runs,
    AVG(r.duration_s) AS avg_duration,
    MAX(r.started_at) AS last_run,
    SUM(CASE WHEN r.started_at >= datetime('now', '-7 days') THEN 1 ELSE 0 END) AS runs_last_7d
FROM cases c
LEFT JOIN run_history r ON c.id = r.case_id
GROUP BY c.id;

-- 任务执行概览视图
CREATE VIEW task_overview AS
SELECT 
    t.id,
    t.name,
    t.status,
    t.env_id,
    t.total_count,
    t.passed_count,
    t.failed_count,
    ROUND(t.passed_count * 100.0 / NULLIF(t.total_count, 0), 1) AS pass_rate,
    t.duration_s,
    t.created_at,
    t.started_at,
    t.finished_at
FROM tasks t
ORDER BY t.created_at DESC;

-- 错误趋势视图
CREATE VIEW error_trends AS
SELECT 
    date(started_at) AS run_date,
    error_type,
    COUNT(*) AS error_count
FROM fix_suggestions f
JOIN run_history r ON f.run_id = r.id
WHERE r.started_at >= datetime('now', '-30 days')
GROUP BY run_date, error_type
ORDER BY run_date DESC;
```

---

## 六、数据迁移方案

### 6.1 迁移脚本设计

```python
# lib/db/migrate.py

import sqlite3
import json
from pathlib import Path
from datetime import datetime

class DataMigrator:
    """从文件系统迁移到 SQLite"""
    
    def __init__(self, db_path: Path, log_dir: Path, config_dir: Path):
        self.db_path = db_path
        self.log_dir = log_dir
        self.config_dir = config_dir
        self.conn = sqlite3.connect(str(db_path))
        self._create_schema()
    
    def migrate_envs(self):
        """迁移环境配置"""
        envs_dir = self.config_dir / "envs"
        if not envs_dir.exists():
            return
        
        for env_file in envs_dir.glob("*.yaml"):
            import yaml
            with open(env_file, encoding="utf-8") as f:
                data = yaml.safe_load(f) or {}
            
            env_id = env_file.stem
            env_block = data.get("env", {}) or {}
            
            self.conn.execute("""
                INSERT OR REPLACE INTO envs (id, name, base_url, datacenter_id, sign_required, timeout)
                VALUES (?, ?, ?, ?, ?, ?)
            """, (
                env_id,
                env_block.get("name", env_id),
                env_block.get("base_url", ""),
                env_block.get("datacenter_id", ""),
                int(data.get("runtime", {}).get("sign_required", True)),
                data.get("runtime", {}).get("timeout", 30)
            ))
            
            # 迁移凭证
            creds = data.get("credentials", {}) or {}
            self.conn.execute("""
                INSERT OR REPLACE INTO env_credentials 
                (env_id, username, password_encrypted, username_env, password_env)
                VALUES (?, ?, ?, ?, ?)
            """, (
                env_id,
                creds.get("username", ""),
                self._encrypt(creds.get("password", "")),
                creds.get("username_env", ""),
                creds.get("password_env", "")
            ))
        
        self.conn.commit()
    
    def migrate_run_history(self):
        """迁移历史运行记录"""
        runs_dir = self.log_dir / "runs"
        if not runs_dir.exists():
            return
        
        for run_file in sorted(runs_dir.glob("*.jsonl")):
            run_id = run_file.stem
            events = []
            
            with open(run_file, encoding="utf-8") as f:
                for line in f:
                    try:
                        events.append(json.loads(line))
                    except:
                        continue
            
            if not events:
                continue
            
            # 解析 case_start 和 case_done
            case_start = next((e for e in events if e.get("type") == "case_start"), {})
            case_done = next((e for e in events if e.get("type") == "case_done"), {})
            
            case_name = case_start.get("data", {}).get("name", "unknown")
            passed = case_done.get("data", {}).get("passed", False)
            
            # 插入运行历史
            self.conn.execute("""
                INSERT OR IGNORE INTO run_history 
                (id, case_name, status, passed, step_ok, step_count, duration_s, started_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                run_id,
                case_name,
                "passed" if passed else "failed",
                int(passed),
                case_done.get("data", {}).get("step_ok", 0),
                case_done.get("data", {}).get("step_count", 0),
                case_done.get("data", {}).get("duration_s", 0),
                datetime.fromtimestamp(events[0].get("ts", 0)).isoformat()
            ))
            
            # 插入步骤详情
            for event in events:
                if event.get("type") in ("step_ok", "step_fail"):
                    step_data = event.get("data", {})
                    self.conn.execute("""
                        INSERT INTO step_results 
                        (run_id, step_id, step_type, step_detail, optional, passed, duration_ms)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """, (
                        run_id,
                        step_data.get("id", "?"),
                        step_data.get("type", "?"),
                        step_data.get("detail", ""),
                        int(step_data.get("optional", False)),
                        int(event.get("type") == "step_ok"),
                        step_data.get("duration_ms", 0)
                    ))
        
        self.conn.commit()
    
    def _encrypt(self, plaintext: str) -> str:
        """加密敏感数据（使用项目级密钥）"""
        # TODO: 实现 AES-256 加密
        return plaintext  # 临时
    
    def close(self):
        self.conn.close()
```

### 6.2 迁移执行流程

```
1. 备份现有数据
   cp -r config config.backup
   cp -r logs logs.backup

2. 初始化数据库
   python -m lib.db.init_db

3. 执行迁移
   python -m lib.db.migrate

4. 验证数据完整性
   python -m lib.db.verify

5. 切换服务使用数据库
   重启 cosmic-replay 服务
```

---

## 七、数据备份与恢复方案

### 7.1 备份策略

```python
# lib/db/backup.py

import sqlite3
import shutil
from pathlib import Path
from datetime import datetime
import gzip

class DatabaseBackup:
    """数据库备份管理"""
    
    def __init__(self, db_path: Path, backup_dir: Path, retention_days: int = 30):
        self.db_path = db_path
        self.backup_dir = backup_dir
        self.backup_dir.mkdir(parents=True, exist_ok=True)
        self.retention_days = retention_days
    
    def full_backup(self) -> Path:
        """完整备份（SQLite 在线备份 API）"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_file = self.backup_dir / f"cosmic_replay_{timestamp}.db"
        
        # 使用 SQLite 在线备份 API
        src = sqlite3.connect(str(self.db_path))
        dst = sqlite3.connect(str(backup_file))
        
        with dst:
            src.backup(dst)
        
        src.close()
        dst.close()
        
        # 压缩
        self._compress(backup_file)
        backup_file.unlink()
        
        return backup_file.with_suffix(".db.gz")
    
    def incremental_backup(self) -> Path:
        """增量备份（SQL 导出）"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_file = self.backup_dir / f"cosmic_replay_incr_{timestamp}.sql"
        
        conn = sqlite3.connect(str(self.db_path))
        
        # 导出为 SQL
        with open(backup_file, "w", encoding="utf-8") as f:
            for line in conn.iterdump():
                f.write(f"{line}\n")
        
        conn.close()
        self._compress(backup_file)
        backup_file.unlink()
        
        return backup_file.with_suffix(".sql.gz")
    
    def _compress(self, file_path: Path):
        """Gzip 压缩"""
        with open(file_path, "rb") as f_in:
            with gzip.open(file_path.with_suffix(".gz"), "wb") as f_out:
                shutil.copyfileobj(f_in, f_out)
    
    def rotate_backups(self):
        """清理过期备份"""
        cutoff = datetime.now().timestamp() - self.retention_days * 86400
        
        for f in self.backup_dir.glob("*.gz"):
            if f.stat().st_mtime < cutoff:
                f.unlink()
    
    def restore(self, backup_file: Path):
        """从备份恢复"""
        # 解压
        if backup_file.suffix == ".gz":
            decompressed = backup_file.with_suffix("")
            with gzip.open(backup_file, "rb") as f_in:
                with open(decompressed, "wb") as f_out:
                    shutil.copyfileobj(f_in, f_out)
            backup_file = decompressed
        
        # 恢复
        if backup_file.suffix == ".db":
            shutil.copy(backup_file, self.db_path)
        elif backup_file.suffix == ".sql":
            conn = sqlite3.connect(str(self.db_path))
            with open(backup_file, "r", encoding="utf-8") as f:
                conn.executescript(f.read())
            conn.close()
```

### 7.2 自动备份配置

```yaml
# config/backup.yaml
backup:
  schedule:
    full: "0 2 * * 0"      # 每周日凌晨2点全量备份
    incremental: "0 2 * * 1-6"  # 周一至周六增量备份
  
  retention:
    full_days: 90          # 全量备份保留90天
    incremental_days: 14   # 增量备份保留14天
  
  storage:
    local: "./backups"
    # remote: "s3://your-bucket/cosmic-replay-backups/"
  
  compression: gzip
  encryption: aes-256-gcm  # 可选加密
```

---

## 八、性能优化建议

### 8.1 索引优化

```sql
-- 高频查询索引
CREATE INDEX idx_run_case_time ON run_history(case_name, started_at DESC);
CREATE INDEX idx_logs_level_time ON logs(level, ts DESC);

-- 覆盖索引（避免回表）
CREATE INDEX idx_task_list ON tasks(created_at DESC, status, total_count);
```

### 8.2 分区策略

```sql
-- 按月分区历史数据
CREATE TABLE run_history_202604 (
    -- 同 run_history 结构
) WITHOUT ROWID;

-- 视图合并
CREATE VIEW run_history AS
SELECT * FROM run_history_202604
UNION ALL
SELECT * FROM run_history_202605;
```

### 8.3 连接池配置

```python
# lib/db/pool.py

import sqlite3
from contextlib import contextmanager
import threading

class ConnectionPool:
    """SQLite 连接池（WAL 模式）"""
    
    def __init__(self, db_path: str, pool_size: int = 5):
        self.db_path = db_path
        self._local = threading.local()
        self._init_db()
    
    def _init_db(self):
        conn = sqlite3.connect(self.db_path)
        conn.execute("PRAGMA journal_mode=WAL")
        conn.execute("PRAGMA synchronous=NORMAL")
        conn.execute("PRAGMA cache_size=-64000")  # 64MB cache
        conn.execute("PRAGMA busy_timeout=5000")
        conn.close()
    
    @contextmanager
    def get_connection(self):
        if not hasattr(self._local, 'conn'):
            self._local.conn = sqlite3.connect(
                self.db_path,
                check_same_thread=False
            )
            self._local.conn.row_factory = sqlite3.Row
        
        try:
            yield self._local.conn
        except Exception:
            self._local.conn.rollback()
            raise
```

---

## 九、安全加固建议

### 9.1 敏感数据加密

```python
# lib/security/crypto.py

from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC
import base64
import os

class DataEncryption:
    """敏感数据加密"""
    
    def __init__(self, master_key: bytes = None):
        if master_key is None:
            # 从环境变量或文件读取
            master_key = os.environ.get("COSMIC_MASTER_KEY", "").encode()
        
        self.fernet = Fernet(self._derive_key(master_key))
    
    def _derive_key(self, password: bytes) -> bytes:
        salt = b"cosmic-replay-salt"  # 生产环境应从安全位置读取
        kdf = PBKDF2HMAC(
            algorithm=hashes.SHA256(),
            length=32,
            salt=salt,
            iterations=100000,
        )
        return base64.urlsafe_b64encode(kdf.derive(password))
    
    def encrypt(self, plaintext: str) -> str:
        return self.fernet.encrypt(plaintext.encode()).decode()
    
    def decrypt(self, ciphertext: str) -> str:
        return self.fernet.decrypt(ciphertext.encode()).decode()
```

### 9.2 访问控制

```sql
-- 创建只读用户视图
CREATE VIEW public_run_stats AS
SELECT 
    case_name,
    COUNT(*) AS total_runs,
    AVG(duration_s) AS avg_duration
FROM run_history
GROUP BY case_name;

-- 敏感数据脱敏
CREATE VIEW masked_credentials AS
SELECT 
    env_id,
    username,
    '***' AS password_masked,
    username_env,
    password_env
FROM env_credentials;
```

---

## 十、实施路线图

### Phase 1: 基础设施 (1-2周)

1. 创建 SQLite 数据库及 Schema
2. 实现 DB 连接池和基础 DAO
3. 迁移环境配置数据
4. 集成到现有服务启动流程

### Phase 2: 数据迁移 (1周)

1. 编写历史数据迁移脚本
2. 执行迁移并验证数据完整性
3. 双写模式并行运行（写入数据库+文件）
4. 切换主数据源为数据库

### Phase 3: 功能增强 (1-2周)

1. 重构 TaskManager 使用数据库
2. 实现高级查询 API
3. 添加趋势分析功能
4. 集成全文搜索

### Phase 4: 运维保障 (持续)

1. 部署自动备份
2. 监控数据库性能
3. 定期数据归档
4. 安全审计日志

---

## 十一、总结

### 现有问题优先级

| 优先级 | 问题 | 建议措施 |
|--------|------|----------|
| P0 | 数据无持久化 | 立即引入 SQLite |
| P0 | 服务重启数据丢失 | 实施数据库持久化 |
| P1 | 无结构化查询 | 建立索引和查询 API |
| P1 | 无备份机制 | 部署自动备份 |
| P2 | 密码明文存储 | 加密敏感数据 |
| P2 | 查询性能差 | 添加索引、分区 |

### 预期收益

1. **数据可靠性**: 从零保障到企业级备份恢复能力
2. **查询能力**: 从全扫描到毫秒级索引查询
3. **扩展性**: 支持百万级历史数据
4. **合规性**: 满足审计追踪要求
5. **运维效率**: 自动化备份监控
