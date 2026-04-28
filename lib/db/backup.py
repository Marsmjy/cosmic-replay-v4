"""
数据库备份模块

支持完整备份、增量备份、定时清理。
"""

import gzip
import json
import shutil
import sqlite3
from datetime import datetime
from pathlib import Path
from typing import Optional
import logging

log = logging.getLogger(__name__)


class DatabaseBackup:
    """数据库备份管理"""
    
    def __init__(self, db_path: Path, backup_dir: Path, retention_days: int = 30):
        self.db_path = Path(db_path)
        self.backup_dir = Path(backup_dir)
        self.backup_dir.mkdir(parents=True, exist_ok=True)
        self.retention_days = retention_days
    
    def full_backup(self) -> Path:
        """
        完整备份（使用 SQLite 在线备份 API）
        
        适用场景：
        - 定期全量备份（如每周）
        - 迁移前备份
        - 重大版本升级前
        """
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_file = self.backup_dir / f"cosmic_replay_full_{timestamp}.db"
        
        log.info(f"开始完整备份: {backup_file}")
        
        # 使用 SQLite 在线备份 API（不阻塞读写）
        src = sqlite3.connect(str(self.db_path))
        dst = sqlite3.connect(str(backup_file))
        
        try:
            # 增量备份直到完成
            src.backup(dst, pages=100, progress=self._backup_progress)
            dst.close()
            src.close()
            
            # 压缩
            compressed = self._compress(backup_file)
            backup_file.unlink()
            
            log.info(f"完整备份完成: {compressed}")
            return compressed
            
        except Exception as e:
            log.error(f"备份失败: {e}")
            if backup_file.exists():
                backup_file.unlink()
            raise
    
    def incremental_backup(self) -> Path:
        """
        增量备份（SQL 导出）
        
        适用场景：
        - 每日增量备份
        - 快速备份（比全量快）
        """
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_file = self.backup_dir / f"cosmic_replay_incr_{timestamp}.sql"
        
        log.info(f"开始增量备份: {backup_file}")
        
        conn = sqlite3.connect(str(self.db_path))
        
        try:
            # 导出为 SQL
            with open(backup_file, "w", encoding="utf-8") as f:
                # 写入元数据
                f.write(f"-- cosmic-replay incremental backup\n")
                f.write(f"-- timestamp: {timestamp}\n")
                f.write(f"-- source: {self.db_path}\n\n")
                
                # 导出所有数据
                for line in conn.iterdump():
                    f.write(f"{line}\n")
            
            conn.close()
            
            # 压缩
            compressed = self._compress(backup_file)
            backup_file.unlink()
            
            log.info(f"增量备份完成: {compressed}")
            return compressed
            
        except Exception as e:
            log.error(f"增量备份失败: {e}")
            if backup_file.exists():
                backup_file.unlink()
            raise
    
    def data_export(self, tables: list[str] = None) -> Path:
        """
        数据导出（JSON 格式）
        
        适用场景：
        - 数据分析
        - 跨系统迁移
        - 归档
        """
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        export_file = self.backup_dir / f"cosmic_replay_export_{timestamp}.json"
        
        if tables is None:
            tables = [
                "envs", "env_credentials", "cases", "tasks",
                "run_history", "step_results", "logs", "reports"
            ]
        
        conn = sqlite3.connect(str(self.db_path))
        conn.row_factory = sqlite3.Row
        
        try:
            data = {
                "export_time": timestamp,
                "source": str(self.db_path),
                "tables": {}
            }
            
            for table in tables:
                rows = conn.execute(f"SELECT * FROM {table}").fetchall()
                data["tables"][table] = [dict(row) for row in rows]
                log.info(f"导出表 {table}: {len(rows)} 条记录")
            
            conn.close()
            
            # 写入 JSON
            with open(export_file, "w", encoding="utf-8") as f:
                json.dump(data, f, ensure_ascii=False, indent=2)
            
            # 压缩
            compressed = self._compress(export_file)
            export_file.unlink()
            
            log.info(f"数据导出完成: {compressed}")
            return compressed
            
        except Exception as e:
            log.error(f"数据导出失败: {e}")
            raise
    
    def restore(self, backup_file: Path) -> bool:
        """
        从备份恢复
        
        Args:
            backup_file: 备份文件路径（.db.gz, .sql.gz, .json.gz）
        
        Returns:
            是否恢复成功
        """
        backup_file = Path(backup_file)
        
        if not backup_file.exists():
            raise FileNotFoundError(f"备份文件不存在: {backup_file}")
        
        log.info(f"开始从备份恢复: {backup_file}")
        
        # 解压
        decompressed = self._decompress(backup_file)
        
        try:
            # 备份当前数据库
            if self.db_path.exists():
                current_backup = self.db_path.with_suffix(".db.bak")
                shutil.copy(self.db_path, current_backup)
                log.info(f"当前数据库已备份到: {current_backup}")
            
            # 恢复
            if decompressed.suffix == ".db":
                # 直接复制
                shutil.copy(decompressed, self.db_path)
                log.info("从 .db 文件恢复完成")
                
            elif decompressed.suffix == ".sql":
                # 执行 SQL
                conn = sqlite3.connect(str(self.db_path))
                with open(decompressed, "r", encoding="utf-8") as f:
                    conn.executescript(f.read())
                conn.close()
                log.info("从 .sql 文件恢复完成")
                
            elif decompressed.suffix == ".json":
                # 导入 JSON
                self._restore_from_json(decompressed)
                log.info("从 .json 文件恢复完成")
            
            # 清理解压文件
            decompressed.unlink()
            
            # 验证
            if self._verify_restore():
                log.info("恢复验证通过")
                return True
            else:
                log.error("恢复验证失败")
                return False
                
        except Exception as e:
            log.error(f"恢复失败: {e}")
            # 尝试回滚
            current_backup = self.db_path.with_suffix(".db.bak")
            if current_backup.exists():
                shutil.copy(current_backup, self.db_path)
                log.info("已回滚到恢复前状态")
            raise
    
    def _restore_from_json(self, json_file: Path):
        """从 JSON 文件恢复"""
        with open(json_file, "r", encoding="utf-8") as f:
            data = json.load(f)
        
        conn = sqlite3.connect(str(self.db_path))
        
        for table, rows in data.get("tables", {}).items():
            if not rows:
                continue
            
            # 清空表
            conn.execute(f"DELETE FROM {table}")
            
            # 插入数据
            columns = list(rows[0].keys())
            placeholders = ", ".join(["?" for _ in columns])
            sql = f"INSERT INTO {table} ({', '.join(columns)}) VALUES ({placeholders})"
            
            conn.executemany(sql, [tuple(r.get(c) for c in columns) for r in rows])
            log.info(f"恢复表 {table}: {len(rows)} 条记录")
        
        conn.commit()
        conn.close()
    
    def rotate_backups(self):
        """清理过期备份"""
        cutoff = datetime.now().timestamp() - self.retention_days * 86400
        removed = 0
        
        for f in self.backup_dir.glob("*.gz"):
            if f.stat().st_mtime < cutoff:
                f.unlink()
                removed += 1
                log.info(f"删除过期备份: {f.name}")
        
        log.info(f"清理完成，删除 {removed} 个过期备份")
        return removed
    
    def list_backups(self) -> list[dict]:
        """列出所有备份"""
        backups = []
        
        for f in sorted(self.backup_dir.glob("*.gz"), reverse=True):
            stat = f.stat()
            
            # 解析备份类型
            name = f.stem
            backup_type = "unknown"
            if "_full_" in name:
                backup_type = "full"
            elif "_incr_" in name:
                backup_type = "incremental"
            elif "_export_" in name:
                backup_type = "export"
            
            backups.append({
                "file": f.name,
                "path": str(f),
                "type": backup_type,
                "size_mb": round(stat.st_size / 1024 / 1024, 2),
                "created": datetime.fromtimestamp(stat.st_mtime).isoformat(),
                "age_days": int((datetime.now().timestamp() - stat.st_mtime) / 86400)
            })
        
        return backups
    
    def _compress(self, file_path: Path) -> Path:
        """Gzip 压缩"""
        compressed_path = file_path.with_suffix(file_path.suffix + ".gz")
        
        with open(file_path, "rb") as f_in:
            with gzip.open(compressed_path, "wb") as f_out:
                shutil.copyfileobj(f_in, f_out)
        
        return compressed_path
    
    def _decompress(self, file_path: Path) -> Path:
        """解压 Gzip"""
        if not file_path.suffix == ".gz":
            return file_path
        
        decompressed = file_path.with_suffix("")
        
        with gzip.open(file_path, "rb") as f_in:
            with open(decompressed, "wb") as f_out:
                shutil.copyfileobj(f_in, f_out)
        
        return decompressed
    
    def _backup_progress(self, status):
        """备份进度回调"""
        remaining = status.remaining
        total = status.total_pages
        if total > 0:
            percent = (total - remaining) * 100 // total
            log.debug(f"备份进度: {percent}%")
    
    def _verify_restore(self) -> bool:
        """验证恢复结果"""
        if not self.db_path.exists():
            return False
        
        conn = sqlite3.connect(str(self.db_path))
        
        # 完整性检查
        result = conn.execute("PRAGMA integrity_check").fetchone()
        if result[0] != "ok":
            log.error(f"完整性检查失败: {result}")
            return False
        
        # 外键检查
        violations = conn.execute("PRAGMA foreign_key_check").fetchall()
        if violations:
            log.error(f"外键违规: {violations}")
            return False
        
        conn.close()
        return True


def main():
    import argparse
    
    ap = argparse.ArgumentParser(description="数据库备份工具")
    ap.add_argument(
        "--db-path",
        type=Path,
        default=Path("data/cosmic_replay.db"),
        help="数据库文件路径"
    )
    ap.add_argument(
        "--backup-dir",
        type=Path,
        default=Path("backups"),
        help="备份目录"
    )
    ap.add_argument(
        "--retention",
        type=int,
        default=30,
        help="备份保留天数"
    )
    
    subparsers = ap.add_subparsers(dest="command", required=True)
    
    # full 命令
    subparsers.add_parser("full", help="执行完整备份")
    
    # incremental 命令
    subparsers.add_parser("incremental", help="执行增量备份")
    
    # export 命令
    subparsers.add_parser("export", help="导出数据为 JSON")
    
    # restore 命令
    restore_parser = subparsers.add_parser("restore", help="从备份恢复")
    restore_parser.add_argument("backup_file", type=Path, help="备份文件")
    
    # list 命令
    subparsers.add_parser("list", help="列出所有备份")
    
    # rotate 命令
    subparsers.add_parser("rotate", help="清理过期备份")
    
    args = ap.parse_args()
    
    backup = DatabaseBackup(args.db_path, args.backup_dir, args.retention)
    
    if args.command == "full":
        result = backup.full_backup()
        print(f"完整备份完成: {result}")
    
    elif args.command == "incremental":
        result = backup.incremental_backup()
        print(f"增量备份完成: {result}")
    
    elif args.command == "export":
        result = backup.data_export()
        print(f"数据导出完成: {result}")
    
    elif args.command == "restore":
        success = backup.restore(args.backup_file)
        print(f"恢复{'成功' if success else '失败'}")
    
    elif args.command == "list":
        backups = backup.list_backups()
        print(f"共 {len(backups)} 个备份:")
        for b in backups:
            print(f"  {b['file']} ({b['type']}, {b['size_mb']}MB, {b['age_days']}天前)")
    
    elif args.command == "rotate":
        removed = backup.rotate_backups()
        print(f"清理了 {removed} 个过期备份")


if __name__ == "__main__":
    main()