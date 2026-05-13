"""
session_manager.py — 苍穹登录 Session 生命周期管理

管理多个环境的登录 session，支持：
- 按 env_id 缓存，避免重复登录
- TTL 过期检测（默认 4 小时）
- 线程安全（threading.Lock）
- 优雅降级（登录失败返回 None）
"""

import logging
import threading
import time
from typing import Optional

log = logging.getLogger(__name__)

# 默认 session 有效期：4 小时
DEFAULT_SESSION_TTL = 4 * 60 * 60  # 14400 秒


class SessionManager:
    """环境级 session 缓存与生命周期管理。

    缓存结构: {env_id: {"cookie": str, "csrf_token": str, "base_url": str,
                        "user_id": str, "account_id": str, "created_at": float}}
    """

    def __init__(self, ttl: int = DEFAULT_SESSION_TTL):
        self._sessions: dict[str, dict] = {}
        self._lock = threading.Lock()
        self._ttl = ttl

    def get_or_create(self, env_id: str, base_url: str, username: str,
                      password: str, datacenter_id: str) -> Optional[dict]:
        """获取已缓存的有效 session，或创建新的。

        Args:
            env_id: 环境标识（如 "uat", "sit"）
            base_url: 苍穹平台 URL
            username: 登录用户名
            password: 登录密码
            datacenter_id: 数据中心 ID

        Returns:
            session dict (含 cookie, csrf_token 等) 或 None（登录失败）
        """
        with self._lock:
            cached = self._sessions.get(env_id)
            if cached and self._is_valid(cached):
                log.debug(f"Reusing cached session for env '{env_id}'")
                return cached

        # 缓存中没有或已过期，创建新 session
        log.info(f"Creating new session for env '{env_id}' ({base_url})")
        session = self._do_login(base_url, username, password, datacenter_id)

        if session:
            session["created_at"] = time.time()
            session["base_url"] = base_url
            with self._lock:
                self._sessions[env_id] = session
            log.info(f"Session created for env '{env_id}', user_id={session.get('user_id', '?')}")
        else:
            log.warning(f"Failed to create session for env '{env_id}'")

        return session

    def get_cached(self, env_id: str) -> Optional[dict]:
        """仅获取缓存中的 session（不创建新的）。"""
        with self._lock:
            cached = self._sessions.get(env_id)
            if cached and self._is_valid(cached):
                return cached
            return None

    def invalidate(self, env_id: str) -> None:
        """标记某个环境的 session 失效。"""
        with self._lock:
            removed = self._sessions.pop(env_id, None)
            if removed:
                log.info(f"Session invalidated for env '{env_id}'")

    def invalidate_all(self) -> None:
        """清除所有缓存 session。"""
        with self._lock:
            count = len(self._sessions)
            self._sessions.clear()
            if count:
                log.info(f"All {count} cached sessions invalidated")

    def check_alive(self, env_id: str) -> bool:
        """主动检查 session 是否仍然有效（调用苍穹 API 验证）。"""
        with self._lock:
            cached = self._sessions.get(env_id)
        if not cached:
            return False

        try:
            from lib.cosmic_login import check_session
            alive = check_session(
                cached["base_url"],
                cached["cookie"],
                cached.get("csrf_token", "")
            )
            if not alive:
                self.invalidate(env_id)
            return alive
        except Exception as e:
            log.warning(f"Session health check failed for env '{env_id}': {e}")
            return False

    def status(self) -> dict:
        """返回所有缓存 session 的状态摘要（供调试/API 展示）。"""
        with self._lock:
            result = {}
            now = time.time()
            for env_id, sess in self._sessions.items():
                age = now - sess.get("created_at", 0)
                result[env_id] = {
                    "base_url": sess.get("base_url", ""),
                    "user_id": sess.get("user_id", ""),
                    "age_seconds": int(age),
                    "ttl_remaining": max(0, int(self._ttl - age)),
                    "valid": age < self._ttl,
                }
            return result

    def _is_valid(self, session: dict) -> bool:
        """检查 session 是否在 TTL 内。"""
        created_at = session.get("created_at", 0)
        return (time.time() - created_at) < self._ttl

    def _do_login(self, base_url: str, username: str, password: str,
                  datacenter_id: str) -> Optional[dict]:
        """执行实际登录，返回 session dict 或 None。"""
        try:
            from lib.cosmic_login import login
            result = login(base_url, username, password, datacenter_id)
            if result.get("success"):
                return {
                    "cookie": result["cookie"],
                    "csrf_token": result.get("csrf_token", ""),
                    "user_id": result.get("user_id", ""),
                    "account_id": result.get("account_id", ""),
                }
            else:
                log.warning(f"Login failed: {result.get('error', 'unknown')}")
                return None
        except Exception as e:
            log.error(f"Login error: {e}")
            return None


# 模块级单例
SESSION_MGR = SessionManager()
