"""
cosmic-replay v4 - 测试配置和共享fixtures
"""
import pytest
import json
import tempfile
import shutil
from pathlib import Path
from typing import Any, Generator

# 添加项目根目录到路径
SKILL_ROOT = Path(__file__).resolve().parent.parent
import sys
sys.path.insert(0, str(SKILL_ROOT))


# ============================================================
# 目录路径fixtures
# ============================================================
@pytest.fixture(scope="session")
def test_data_dir() -> Path:
    """测试数据目录"""
    return Path(__file__).parent / "fixtures"


@pytest.fixture(scope="session")
def sample_cases_dir() -> Path:
    """示例用例目录"""
    return SKILL_ROOT / "cases"


# ============================================================
# 临时目录fixtures
# ============================================================
@pytest.fixture
def temp_dir(tmp_path: Path) -> Path:
    """临时工作目录"""
    return tmp_path


@pytest.fixture
def temp_config_dir(tmp_path: Path) -> Path:
    """临时配置目录"""
    config_dir = tmp_path / "config"
    config_dir.mkdir()
    (config_dir / "envs").mkdir()
    
    # 创建默认webui配置
    webui_config = {
        "webui": {
            "port": 8765,
            "host": "127.0.0.1",
            "open_browser": False,
            "default_env": "test"
        },
        "logging": {
            "level": "debug",
            "log_dir": str(tmp_path / "logs")
        },
        "paths": {
            "cases_dir": str(tmp_path / "cases"),
            "har_upload_dir": str(tmp_path / "har_uploads")
        }
    }
    import yaml
    try:
        with open(config_dir / "webui.yaml", "w", encoding="utf-8") as f:
            yaml.dump(webui_config, f, allow_unicode=True)
    except ImportError:
        # 无yaml时用JSON
        with open(config_dir / "webui.yaml", "w", encoding="utf-8") as f:
            json.dump(webui_config, f, ensure_ascii=False, indent=2)
    
    return config_dir


@pytest.fixture
def temp_cases_dir(tmp_path: Path) -> Path:
    """临时用例目录"""
    cases_dir = tmp_path / "cases"
    cases_dir.mkdir()
    return cases_dir


@pytest.fixture
def temp_logs_dir(tmp_path: Path) -> Path:
    """临时日志目录"""
    logs_dir = tmp_path / "logs"
    logs_dir.mkdir()
    (logs_dir / "runs").mkdir()
    return logs_dir


# ============================================================
# 配置fixtures
# ============================================================
@pytest.fixture
def config(temp_config_dir: Path):
    """Config实例"""
    from lib.config import Config
    return Config(config_dir=temp_config_dir)


@pytest.fixture
def env_config_data() -> dict:
    """环境配置数据"""
    return {
        "name": "测试环境",
        "base_url": "http://test.example.com",
        "datacenter_id": "test_dc_001",
        "credentials": {
            "username": "test_user",
            "password": "test_pass",
            "username_env": "",
            "password_env": ""
        },
        "basedata": {},
        "runtime": {
            "sign_required": False,
            "timeout": 30,
            "login_retries": 3
        }
    }


# ============================================================
# LogStore fixture
# ============================================================
@pytest.fixture
def log_store(temp_logs_dir: Path):
    """LogStore实例"""
    from lib.webui.log_store import LogStore
    store = LogStore(temp_logs_dir, buffer_size=10, retention_days=1)
    yield store
    # 清理


# ============================================================
# 示例数据fixtures
# ============================================================
@pytest.fixture
def minimal_case() -> dict:
    """最小用例结构"""
    return {
        "name": "minimal_test_case",
        "description": "最小测试用例",
        "env": {
            "base_url": "http://test.local",
            "username": "test",
            "password": "test123"
        },
        "steps": []
    }


@pytest.fixture
def case_with_steps() -> dict:
    """包含步骤的用例"""
    return {
        "name": "test_case_with_steps",
        "description": "包含多个步骤的测试用例",
        "env": {
            "base_url": "http://test.local",
            "username": "test",
            "password": "test123"
        },
        "vars": {
            "test_number": "TEST${rand:4}",
            "test_name": "测试员${test_number}"
        },
        "main_form_id": "test_form",
        "steps": [
            {"id": "open_form", "type": "open_form", "form_id": "test_form", "app_id": "test_app"},
            {"id": "fill_name", "type": "update_fields", "form_id": "test_form", "app_id": "test_app", 
             "fields": {"name": {"zh_CN": "${vars.test_name}"}}},
            {"id": "save", "type": "invoke", "form_id": "test_form", "app_id": "test_app",
             "ac": "save", "key": "tbmain", "method": "itemClick", "args": ["save"]}
        ],
        "assertions": [
            {"type": "no_save_failure", "step": "save"},
            {"type": "no_error_actions", "step": "save"}
        ]
    }


@pytest.fixture
def sample_har() -> dict:
    """示例HAR数据"""
    return {
        "log": {
            "version": "1.2",
            "creator": {"name": "test", "version": "1.0"},
            "entries": [
                {
                    "request": {
                        "url": "http://test.local/form/batchInvokeAction.do",
                        "method": "POST",
                        "postData": {
                            "mimeType": "application/json",
                            "text": json.dumps({
                                "formId": "test_form",
                                "appId": "test_app",
                                "pageId": "test_page_001",
                                "actions": [
                                    {"a": "u", "k": "name", "v": "测试名称"},
                                    {"a": "u", "k": "number", "v": "TEST12345"}
                                ]
                            })
                        }
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "mimeType": "application/json",
                            "text": json.dumps([
                                {"a": "u", "k": "name", "v": "测试名称"},
                                {"a": "showErrMsg", "args": [""]}
                            ])
                        }
                    }
                }
            ]
        }
    }


@pytest.fixture
def mock_invoke_response() -> dict:
    """模拟invoke响应"""
    return [
        {"a": "u", "k": "name", "v": "测试值"},
        {"a": "setCaption", "k": "name", "args": [{"zh_CN": "名称"}]},
        {"a": "showErrMsg", "args": [""]}
    ]


@pytest.fixture
def error_response() -> dict:
    """错误响应"""
    return [
        {"a": "showErrMsg", "args": ["请填写名称"]},
        {"a": "setFocus", "k": "name"}
    ]


@pytest.fixture
def save_success_response() -> dict:
    """保存成功响应"""
    return [
        {"a": "showMessage", "args": ["保存成功"]},
        {"a": "close"}
    ]


@pytest.fixture
def save_fail_response() -> dict:
    """保存失败响应（含bos_operationresult）"""
    return [
        {"a": "openForm", "formId": "bos_operationresult", "pageId": "op_result_page_001"},
        {"a": "showMessage", "args": ["成功数量：0，失败数量：1"]}
    ]


# ============================================================
# 参数化测试数据
# ============================================================
@pytest.fixture(params=[
    ("${rand:4}", 4),
    ("${rand:6}", 6),
    ("${rand:8}", 8),
    ("${rand:10}", 10),
])
def rand_length_params(request):
    """随机数长度参数化"""
    return request.param


@pytest.fixture(params=[
    "${timestamp}",
    "${today}",
    "${uuid}",
    "${now}",
])
def builtin_vars(request):
    """内置变量参数化"""
    return request.param


@pytest.fixture(params=[
    ("number", "EMP001"),
    ("name", "测试员"),
    ("phone", "13800138000"),
    ("code", "TEST123"),
])
def unique_field_params(request):
    """唯一字段参数化"""
    return request.param


# ============================================================
# 工具函数
# ============================================================
def create_yaml_file(path: Path, data: dict) -> None:
    """创建YAML文件"""
    try:
        import yaml
        with open(path, "w", encoding="utf-8") as f:
            yaml.dump(data, f, allow_unicode=True, sort_keys=False)
    except ImportError:
        from lib.har_extractor import to_yaml
        path.write_text(to_yaml(data) + "\n", encoding="utf-8")


def create_har_file(path: Path, data: dict) -> None:
    """创建HAR文件"""
    path.write_text(json.dumps(data, ensure_ascii=False), encoding="utf-8")


# 导出公共接口
__all__ = [
    "test_data_dir",
    "temp_config_dir",
    "config",
    "log_store",
    "minimal_case",
    "case_with_steps",
    "sample_har",
    "create_yaml_file",
    "create_har_file",
]
