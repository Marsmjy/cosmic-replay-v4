"""
数据迁移模块

从现有文件系统存储迁移到 SQLite 数据库。
"""

import json
import sqlite3
from datetime import datetime
from pathlib import Path
from typing import Optional
import logging

log = logging.getLogger(__name__)


class DataMigrator:
    """数据迁移器"""
    
    def __init__(self, db_path: Path, log_dir: Path, config_dir: Path):
        self.db_path = Path(db_path)
        self.log_dir = Path(log_dir)
        self.config_dir = Path(config_dir)
        self.conn: Optional[sqlite3.Connection] = None
    
    def connect(self):
        """连接数据库"""
        self.conn = sqlite3.connect(str(self.db_path))
        self.conn.row_factory = sqlite3.Row
        self.conn.execute("PRAGMA foreign_keys=ON")
    
    def close(self):
        """关闭连接"""
        if self.conn:
            self.conn.commit()
            self.conn.close()
            self.conn = None
    
    def migrate_all(self):
        """执行完整迁移"""
        print("开始数据迁移...")
        print(f"  数据库: {self.db_path}")
        print(f"  日志目录: {self.log_dir}")
        print(f"  配置目录: {self.config_dir}")
        
        self.connect()
        
        try:
            # 1. 迁移环境配置
            env_count = self.migrate_envs()
            print(f"  环境配置: {env_count} 条")
            
            # 2. 迁移用例
            case_count = self.migrate_cases()
            print(f"  用例: {case_count} 条")
            
            # 3. 迁移运行历史
            run_count = self.migrate_run_history()
            print(f"  运行历史: {run_count} 条")
            
            # 4. 迁移日志
            log_count = self.migrate_logs()
            print(f"  服务日志: {log_count} 条")
            
            self.conn.commit()
            
            print("数据迁移完成")
            return {
                "envs": env_count,
                "cases": case_count,
                "runs": run_count,
                "logs": log_count
            }
            
        except Exception as e:
            self.conn.rollback()
            print(f"迁移失败: {e}")
            raise
        finally:
            self.close()
    
    def migrate_envs(self) -> int:
        """迁移环境配置"""
        envs_dir = self.config_dir / "envs"
        if not envs_dir.exists():
            return 0
        
        count = 0
        for env_file in sorted(envs_dir.glob("*.yaml")):
            try:
                import yaml
                with open(env_file, encoding="utf-8") as f:
                    data = yaml.safe_load(f) or {}
                
                env_id = env_file.stem
                env_block = data.get("env", {}) or {}
                creds_block = data.get("credentials", {}) or {}
                runtime_block = data.get("runtime", {}) or {}
                
                # 插入环境
                self.conn.execute("""
                    INSERT OR REPLACE INTO envs 
                    (id, name, base_url, datacenter_id, sign_required, timeout, login_retries)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """, (
                    env_id,
                    env_block.get("name", env_id),
                    env_block.get("base_url", ""),
                    env_block.get("datacenter_id", ""),
                    int(runtime_block.get("sign_required", True)),
                    runtime_block.get("timeout", 30),
                    runtime_block.get("login_retries", 3)
                ))
                
                # 插入凭证
                self.conn.execute("""
                    INSERT OR REPLACE INTO env_credentials 
                    (env_id, username, password_encrypted, username_env, password_env)
                    VALUES (?, ?, ?, ?, ?)
                """, (
                    env_id,
                    creds_block.get("username", ""),
                    creds_block.get("password", ""),  # TODO: 加密
                    creds_block.get("username_env", ""),
                    creds_block.get("password_env", "")
                ))
                
                # 插入 basedata
                basedata = data.get("basedata", {}) or {}
                for key, value in basedata.items():
                    self.conn.execute("""
                        INSERT OR REPLACE INTO env_basedata (env_id, key, value)
                        VALUES (?, ?, ?)
                    """, (env_id, key, str(value)))
                
                count += 1
                
            except Exception as e:
                log.warning(f"迁移环境 {env_file.name} 失败: {e}")
        
        return count
    
    def migrate_cases(self) -> int:
        """迁移用例"""
        # 从 cases 目录获取用例列表
        cases_dir = self.config_dir.parent / "cases"
        if not cases_dir.exists():
            return 0
        
        count = 0
        for case_file in sorted(cases_dir.rglob("*.yaml")):
            try:
                rel_path = case_file.relative_to(cases_dir)
                case_name = str(rel_path).replace("\\", "/").replace(".yaml", "")
                
                import yaml
                with open(case_file, encoding="utf-8") as f:
                    data = yaml.safe_load(f) or {}
                
                tags = data.get("tags", []) or []
                tags_json = json.dumps(tags)
                
                self.conn.execute("""
                    INSERT OR REPLACE INTO cases 
                    (name, display_name, description, file_path, tags, step_count)
                    VALUES (?, ?, ?, ?, ?, ?)
                """, (
                    case_name,
                    data.get("name", case_name),
                    data.get("description", ""),
                    str(case_file),
                    tags_json,
                    len(data.get("steps", []) or [])
                ))
                
                count += 1
                
            except Exception as e:
                log.warning(f"迁移用例 {case_file.name} 失败: {e}")
        
        return count
    
    def migrate_run_history(self) -> int:
        """迁移运行历史"""
        runs_dir = self.log_dir / "runs"
        if not runs_dir.exists():
            return 0
        
        count = 0
        for run_file in sorted(runs_dir.glob("*.jsonl")):
            try:
                run_id = run_file.stem
                events = []
                
                with open(run_file, encoding="utf-8") as f:
                    for line in f:
                        line = line.strip()
                        if not line:
                            continue
                        try:
                            events.append(json.loads(line))
                        except:
                            continue
                
                if not events:
                    continue
                
                # 解析事件
                case_start = next((e for e in events if e.get("type") == "case_start"), {})
                case_done = next((e for e in events if e.get("type") == "case_done"), {})
                login_ok = next((e for e in events if e.get("type") == "login_ok"), {})
                
                case_name = case_start.get("data", {}).get("name", "unknown")
                passed = case_done.get("data", {}).get("passed", False)
                step_ok = case_done.get("data", {}).get("step_ok", 0)
                step_count = case_done.get("data", {}).get("step_count", 0)
                duration_s = case_done.get("data", {}).get("duration_s", 0)
                
                # 获取开始时间
                ts = events[0].get("ts", 0)
                started_at = datetime.fromtimestamp(ts).isoformat() if ts else None
                finished_at = datetime.fromtimestamp(events[-1].get("ts", 0)).isoformat() if len(events) > 1 else None
                
                # 获取用例ID
                case_id = None
                result = self.conn.execute(
                    "SELECT id FROM cases WHERE name=?", (case_name,)
                ).fetchone()
                if result:
                    case_id = result[0]
                
                # 插入运行历史
                self.conn.execute("""
                    INSERT OR IGNORE INTO run_history 
                    (id, case_id, case_name, status, passed, step_ok, step_count, 
                     duration_s, started_at, finished_at, env_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, (
                    run_id,
                    case_id,
                    case_name,
                    "passed" if passed else "failed",
                    int(passed),
                    step_ok,
                    step_count,
                    duration_s,
                    started_at,
                    finished_at,
                    "sit"  # 默认环境
                ))
                
                # 插入步骤详情
                for event in events:
                    event_type = event.get("type")
                    event_data = event.get("data", {})
                    
                    if event_type in ("step_ok", "step_fail"):
                        self.conn.execute("""
                            INSERT INTO step_results 
                            (run_id, step_id, step_type, step_detail, optional, passed, duration_ms, error_message)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """, (
                            run_id,
                            event_data.get("id", "?"),
                            event_data.get("type", "?"),
                            event_data.get("detail", ""),
                            int(event_data.get("optional", False)),
                            int(event_type == "step_ok"),
                            event_data.get("duration_ms", 0),
                            "; ".join(event_data.get("errors", [])) if event_type == "step_fail" else None
                        ))
                    
                    elif event_type in ("assertion_ok", "assertion_fail"):
                        self.conn.execute("""
                            INSERT INTO assertions (run_id, assertion_type, passed, message)
                            VALUES (?, ?, ?, ?)
                        """, (
                            run_id,
                            event_data.get("type", "unknown"),
                            int(event_type == "assertion_ok"),
                            event_data.get("msg", "")
                        ))
                
                count += 1
                
            except Exception as e:
                log.warning(f"迁移运行 {run_file.name} 失败: {e}")
        
        return count
    
    def migrate_logs(self) -> int:
        """迁移服务日志"""
        count = 0
        
        for log_file in sorted(self.log_dir.glob("server-*.log")):
            try:
                with open(log_file, encoding="utf-8") as f:
                    for line in f:
                        line = line.strip()
                        if not line:
                            continue
                        
                        # 解析日志格式
                        # [HH:MM:SS] LEVEL [source] message
                        import re
                        match = re.match(
                            r'\[(\d{2}:\d{2}:\d{2})\]\s+(\w+)\s+\[([^\]]+)\]\s+(.*)',
                            line
                        )
                        
                        if match:
                            time_str, level, source, message = match.groups()
                            
                            # 从文件名推断日期
                            date_str = log_file.stem.replace("server-", "")
                            try:
                                log_ts = datetime.strptime(
                                    f"{date_str} {time_str}", "%Y%m%d %H:%M:%S"
                                ).timestamp()
                            except:
                                log_ts = datetime.now().timestamp()
                            
                            self.conn.execute("""
                                INSERT INTO logs (ts, level, source, message)
                                VALUES (?, ?, ?, ?)
                            """, (log_ts, level.lower(), source, message))
                            
                            count += 1
                            
            except Exception as e:
                log.warning(f"迁移日志 {log_file.name} 失败: {e}")
        
        return count
    
    def verify(self) -> dict:
        """验证迁移结果"""
        if not self.conn:
            self.connect()
        
        result = {}
        
        # 检查各表记录数
        tables = [
            "envs", "env_credentials", "cases", "run_history",
            "step_results", "assertions", "logs"
        ]
        
        for table in tables:
            count = self.conn.execute(
                f"SELECT COUNT(*) FROM {table}"
            ).fetchone()[0]
            result[table] = count
        
        # 检查外键完整性
        violations = self.conn.execute("PRAGMA foreign_key_check").fetchall()
        result["fk_violations"] = len(violations)
        
        # 检查数据库完整性
        integrity = self.conn.execute("PRAGMA integrity_check").fetchone()[0]
        result["integrity"] = integrity
        
        return result


def main():
    import argparse
    
    ap = argparse.ArgumentParser(description="数据迁移工具")
    ap.add_argument(
        "--db-path",
        type=Path,
        default=Path("data/cosmic_replay.db"),
        help="数据库文件路径"
    )
    ap.add_argument(
        "--log-dir",
        type=Path,
        default=Path("logs"),
        help="日志目录"
    )
    ap.add_argument(
        "--config-dir",
        type=Path,
        default=Path("config"),
        help="配置目录"
    )
    ap.add_argument(
        "--verify-only",
        action="store_true",
        help="仅验证，不迁移"
    )
    
    args = ap.parse_args()
    
    migrator = DataMigrator(args.db_path, args.log_dir, args.config_dir)
    
    if args.verify_only:
        result = migrator.verify()
        print("验证结果:")
        for k, v in result.items():
            print(f"  {k}: {v}")
    else:
        result = migrator.migrate_all()
        print("迁移结果:", result)
        
        # 验证
        verify_result = migrator.verify()
        print("验证结果:", verify_result)


if __name__ == "__main__":
    main()