"""配置模块 - 统一读写 config/webui.yaml + config/envs/*.yaml

设计：
- Layer 1: config/webui.yaml  - Web UI 偏好（端口、默认环境等）
- Layer 2: config/envs/*.yaml - 各环境配置（base_url / 凭证 / basedata）
- config/ 不入 git；config.example/ 入 git 作为模板

用法：
    from .config import Config
    cfg = Config()                            # 自动查找 config/ 目录
    cfg.webui.port                            # 8765
    cfg.envs                                  # list[EnvConfig]
    env = cfg.get_env("sit")                  # EnvConfig
    cfg.save_webui({"port": 9000})            # 写回 webui.yaml + 热重载
    cfg.save_env("sit", {...})                # 写回 envs/sit.yaml
    cfg.init_from_example()                   # 首次使用：从 example 拷贝

凭证解析：username_env / password_env 优先于明文。
"""
from __future__ import annotations

import os
import shutil
from dataclasses import dataclass, field, asdict
from pathlib import Path
from typing import Any


SKILL_ROOT = Path(__file__).resolve().parent.parent
CONFIG_DIR = SKILL_ROOT / "config"
CONFIG_EXAMPLE_DIR = SKILL_ROOT / "config.example"


# =============================================================
# YAML 读写（复用 runner 的轻量解析器 + pyyaml fallback）
# =============================================================
def _load_yaml(path: Path) -> dict:
    try:
        import yaml  # type: ignore
        with path.open(encoding="utf-8") as f:
            return yaml.safe_load(f) or {}
    except ImportError:
        pass
    from .runner import _parse_yaml_light  # type: ignore
    return _parse_yaml_light(path.read_text(encoding="utf-8"))


def _dump_yaml(path: Path, data: dict) -> None:
    """写回 YAML。优先用 pyyaml，没有时用本模块内置简化版。"""
    try:
        import yaml  # type: ignore
        path.parent.mkdir(parents=True, exist_ok=True)
        with path.open("w", encoding="utf-8") as f:
            yaml.safe_dump(data, f, allow_unicode=True, sort_keys=False, default_flow_style=False)
        return
    except ImportError:
        pass
    from .har_extractor import to_yaml as _to_yaml  # type: ignore
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(_to_yaml(data) + "\n", encoding="utf-8")


# =============================================================
# 数据模型
# =============================================================
@dataclass
class WebUIPrefs:
    port: int = 8765
    host: str = "127.0.0.1"
    open_browser: bool = True
    default_env: str = "sit"
    logging_level: str = "info"
    logging_dir: str = "./logs"
    cases_dir: str = "./cases"
    har_upload_dir: str = "./har_uploads"
    # ⭐ P2-3: 日志轮转增强配置
    log_max_size_mb: int = 100       # 单文件最大100MB
    log_retention_days: int = 30     # 保留30天
    log_compress: bool = True        # 压缩旧日志


@dataclass
class Credentials:
    username: str = ""
    password: str = ""
    username_env: str = ""
    password_env: str = ""

    def resolve_username(self) -> str:
        # 环境变量优先，fallback 到 yaml 里的明文 username
        # 之前只走 env 是错的：yaml 里写了账号但没设环境变量时会拿到空字符串
        if self.username_env:
            v = os.environ.get(self.username_env, "")
            if v:
                return v
        return self.username

    def resolve_password(self) -> str:
        if self.password_env:
            v = os.environ.get(self.password_env, "")
            if v:
                return v
        return self.password

    def is_configured(self) -> bool:
        return bool(self.resolve_username() and self.resolve_password())


@dataclass
class EnvConfig:
    file: str                        # 文件名（不含路径），用作 id
    name: str                        # 显示名
    base_url: str = ""
    datacenter_id: str = ""
    credentials: Credentials = field(default_factory=Credentials)
    basedata: dict[str, str] = field(default_factory=dict)
    sign_required: bool = True
    timeout: int = 30
    login_retries: int = 3

    @property
    def id(self) -> str:
        """用文件名（不含扩展名）作为 id"""
        return Path(self.file).stem


# =============================================================
# Config 主对象
# =============================================================
class Config:
    """配置管理入口。单例使用即可。"""

    def __init__(self, config_dir: Path | None = None):
        self.config_dir = Path(config_dir) if config_dir else CONFIG_DIR
        self.webui = WebUIPrefs()
        self.envs: list[EnvConfig] = []
        self._load()

    # ---------- 加载 ----------

    def _load(self):
        if not self.config_dir.exists():
            # 首次使用，不报错，返回默认值
            return
        # webui.yaml
        webui_file = self.config_dir / "webui.yaml"
        if webui_file.exists():
            data = _load_yaml(webui_file)
            self._apply_webui(data)
        # envs/*.yaml
        envs_dir = self.config_dir / "envs"
        if envs_dir.exists():
            self.envs = []
            for f in sorted(envs_dir.glob("*.yaml")):
                try:
                    env = self._load_env_file(f)
                    self.envs.append(env)
                except Exception as e:
                    # 坏文件不要让整个 config 挂掉
                    import logging
                    logging.warning(f"Failed to load env {f.name}: {e}")

    def _apply_webui(self, data: dict):
        webui_part = data.get("webui", {}) or {}
        self.webui.port = int(webui_part.get("port", self.webui.port))
        self.webui.host = str(webui_part.get("host", self.webui.host))
        self.webui.open_browser = bool(webui_part.get("open_browser", self.webui.open_browser))
        self.webui.default_env = str(webui_part.get("default_env", self.webui.default_env))
        logging_part = data.get("logging", {}) or {}
        self.webui.logging_level = str(logging_part.get("level", self.webui.logging_level))
        self.webui.logging_dir = str(logging_part.get("log_dir", self.webui.logging_dir))
        paths_part = data.get("paths", {}) or {}
        self.webui.cases_dir = str(paths_part.get("cases_dir", self.webui.cases_dir))
        self.webui.har_upload_dir = str(paths_part.get("har_upload_dir", self.webui.har_upload_dir))

    def _load_env_file(self, path: Path) -> EnvConfig:
        raw = _load_yaml(path)
        env_block = raw.get("env", {}) or {}
        creds_block = raw.get("credentials", {}) or {}
        basedata_block = raw.get("basedata", {}) or {}
        runtime_block = raw.get("runtime", {}) or {}

        creds = Credentials(
            username=str(creds_block.get("username", "") or ""),
            password=str(creds_block.get("password", "") or ""),
            username_env=str(creds_block.get("username_env", "") or ""),
            password_env=str(creds_block.get("password_env", "") or ""),
        )
        env = EnvConfig(
            file=path.name,
            name=str(env_block.get("name", path.stem)),
            base_url=str(env_block.get("base_url", "") or ""),
            datacenter_id=str(env_block.get("datacenter_id", "") or ""),
            credentials=creds,
            basedata={str(k): str(v) for k, v in basedata_block.items()},
            sign_required=bool(runtime_block.get("sign_required", True)),
            timeout=int(runtime_block.get("timeout", 30)),
            login_retries=int(runtime_block.get("login_retries", 3)),
        )
        return env

    # ---------- 查询 ----------

    def get_env(self, env_id: str) -> EnvConfig | None:
        for e in self.envs:
            if e.id == env_id:
                return e
        return None

    def default_env(self) -> EnvConfig | None:
        env = self.get_env(self.webui.default_env)
        if env:
            return env
        return self.envs[0] if self.envs else None

    # ---------- 写回 ----------

    def save_webui(self, prefs: dict) -> None:
        """部分更新 webui.yaml 并热重载"""
        path = self.config_dir / "webui.yaml"
        existing = _load_yaml(path) if path.exists() else {}
        # 嵌套合并
        for top_key, sub in (("webui", {"port", "host", "open_browser", "default_env"}),
                             ("logging", {"logging_level", "logging_dir"}),
                             ("paths", {"cases_dir", "har_upload_dir"})):
            section = existing.setdefault(top_key, {})
            for k in sub:
                flat_key = k
                # 统一 flat key → section key
                if top_key == "logging":
                    section_key = "level" if k == "logging_level" else "log_dir"
                else:
                    section_key = k
                if flat_key in prefs:
                    section[section_key] = prefs[flat_key]
        _dump_yaml(path, existing)
        self._load()

    def save_env(self, env_id: str, data: dict) -> None:
        """保存或新建一个环境配置"""
        filename = f"{env_id}.yaml"
        path = self.config_dir / "envs" / filename
        out: dict = {
            "env": {
                "name": data.get("name", env_id),
                "base_url": data.get("base_url", ""),
                "datacenter_id": data.get("datacenter_id", ""),
            },
            "credentials": data.get("credentials", {}) or {},
            "basedata": data.get("basedata", {}) or {},
            "runtime": {
                "sign_required": bool(data.get("sign_required", True)),
                "timeout": int(data.get("timeout", 30)),
                "login_retries": int(data.get("login_retries", 3)),
            },
        }
        _dump_yaml(path, out)
        self._load()

    def delete_env(self, env_id: str) -> bool:
        path = self.config_dir / "envs" / f"{env_id}.yaml"
        if path.exists():
            path.unlink()
            self._load()
            return True
        return False

    def reload(self) -> None:
        self._load()

    # ---------- 初始化（从 example 拷贝） ----------

    def init_from_example(self, force: bool = False) -> bool:
        """首次使用：从 config.example/ 复制到 config/"""
        if self.config_dir.exists() and not force:
            return False
        if not CONFIG_EXAMPLE_DIR.exists():
            raise FileNotFoundError(f"模板目录不存在：{CONFIG_EXAMPLE_DIR}")
        if force and self.config_dir.exists():
            shutil.rmtree(self.config_dir)
        shutil.copytree(CONFIG_EXAMPLE_DIR, self.config_dir)
        self._load()
        return True

    # ---------- 便利 ----------

    def to_dict(self, mask_secrets: bool = True) -> dict:
        """序列化供 Web UI 展示（敏感字段打码）"""
        envs_out = []
        for e in self.envs:
            cred = {
                "username": e.credentials.username,
                "password": "********" if mask_secrets and e.credentials.password else e.credentials.password,
                "username_env": e.credentials.username_env,
                "password_env": e.credentials.password_env,
                "configured": e.credentials.is_configured(),
            }
            envs_out.append({
                "id": e.id,
                "file": e.file,
                "name": e.name,
                "base_url": e.base_url,
                "datacenter_id": e.datacenter_id,
                "credentials": cred,
                "basedata": dict(e.basedata),
                "sign_required": e.sign_required,
                "timeout": e.timeout,
                "login_retries": e.login_retries,
            })
        return {
            "webui": asdict(self.webui),
            "envs": envs_out,
        }