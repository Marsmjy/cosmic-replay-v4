"""
数据库连接池模块

SQLite WAL 模式下的线程安全连接管理。
"""

import sqlite3
import threading
from contextlib import contextmanager
from pathlib import Path
from typing import Optional
import atexit


class DatabasePool:
    """SQLite 连接池（线程本地连接）"""
    
    _instance: Optional["DatabasePool"] = None
    _lock = threading.Lock()
    
    def __init__(self, db_path: Path, pool_size: int = 5):
        self.db_path = Path(db_path)
        self._local = threading.local()
        self._connections: list[sqlite3.Connection] = []
        
        # 确保父目录存在
        self.db_path.parent.mkdir(parents=True, exist_ok=True)
        
        # 配置数据库参数
        self._configure_db()
        
        # 注册退出时关闭所有连接
        atexit.register(self.close_all)
    
    @classmethod
    def get_instance(cls, db_path: Path = None) -> "DatabasePool":
        """获取单例实例"""
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    # 默认路径
                    if db_path is None:
                        from pathlib import Path
                        db_path = Path(__file__).parent.parent.parent / "data" / "cosmic_replay.db"
                    cls._instance = cls(db_path)
        return cls._instance
    
    def _configure_db(self):
        """配置数据库参数"""
        conn = self._get_raw_connection()
        
        # WAL 模式：支持并发读写
        conn.execute("PRAGMA journal_mode=WAL")
        
        # 同步模式：NORMAL 比 FULL 更快，足够安全
        conn.execute("PRAGMA synchronous=NORMAL")
        
        # 缓存大小：负数表示 KB，-64000 = 64MB
        conn.execute("PRAGMA cache_size=-64000")
        
        # 外键约束
        conn.execute("PRAGMA foreign_keys=ON")
        
        # 忙等待超时
        conn.execute("PRAGMA busy_timeout=10000")
        
        conn.close()
    
    def _get_raw_connection(self) -> sqlite3.Connection:
        """创建原始连接"""
        conn = sqlite3.connect(
            str(self.db_path),
            check_same_thread=False,
            timeout=10.0
        )
        # 使用 Row factory 方便字典访问
        conn.row_factory = sqlite3.Row
        return conn
    
    def get_connection(self) -> sqlite3.Connection:
        """获取线程本地连接"""
        if not hasattr(self._local, "conn") or self._local.conn is None:
            self._local.conn = self._get_raw_connection()
            self._connections.append(self._local.conn)
        return self._local.conn
    
    @contextmanager
    def transaction(self):
        """事务上下文管理器"""
        conn = self.get_connection()
        try:
            yield conn
            conn.commit()
        except Exception:
            conn.rollback()
            raise
    
    def close_all(self):
        """关闭所有连接"""
        for conn in self._connections:
            try:
                conn.close()
            except:
                pass
        self._connections.clear()
        
        # 清理线程本地
        if hasattr(self._local, "conn"):
            self._local.conn = None
    
    def execute(self, sql: str, params: tuple = None) -> sqlite3.Cursor:
        """便捷执行方法"""
        conn = self.get_connection()
        if params:
            return conn.execute(sql, params)
        return conn.execute(sql)
    
    def executemany(self, sql: str, params_list: list) -> sqlite3.Cursor:
        """批量执行"""
        conn = self.get_connection()
        return conn.executemany(sql, params_list)
    
    def query_one(self, sql: str, params: tuple = None) -> Optional[dict]:
        """查询单条"""
        cursor = self.execute(sql, params)
        row = cursor.fetchone()
        if row:
            return dict(row)
        return None
    
    def query_all(self, sql: str, params: tuple = None) -> list[dict]:
        """查询多条"""
        cursor = self.execute(sql, params)
        return [dict(row) for row in cursor.fetchall()]
    
    def query_count(self, sql: str, params: tuple = None) -> int:
        """查询计数"""
        result = self.query_one(sql, params)
        if result:
            return list(result.values())[0]
        return 0


# 全局连接池实例
_pool: Optional[DatabasePool] = None


def init_pool(db_path: Path) -> DatabasePool:
    """初始化全局连接池"""
    global _pool
    _pool = DatabasePool(db_path)
    return _pool


def get_pool() -> DatabasePool:
    """获取全局连接池"""
    global _pool
    if _pool is None:
        # 使用默认路径
        db_path = Path(__file__).parent.parent.parent / "data" / "cosmic_replay.db"
        _pool = DatabasePool.get_instance(db_path)
    return _pool


@contextmanager
def get_connection():
    """获取连接上下文"""
    pool = get_pool()
    conn = pool.get_connection()
    try:
        yield conn
    except Exception:
        conn.rollback()
        raise


@contextmanager
def transaction():
    """事务上下文"""
    pool = get_pool()
    with pool.transaction() as conn:
        yield conn