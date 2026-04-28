"""数据库模块

模块：
- init_db: 初始化数据库表
- pool: 连接池管理
- dao: 数据访问对象
- migrate: 数据迁移
- backup: 备份恢复
"""

from .init_db import init_database

__all__ = ["init_database"]
