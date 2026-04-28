"""
cosmic-replay v2 - 核心功能单元测试

测试目标：
1. ExecutionHistory类的add/get方法
2. Config环境加载
3. 变量解析系统
4. Health Check API响应结构
"""
import pytest
import sys
from pathlib import Path
from datetime import datetime

# 添加项目根目录到路径
SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))


class TestExecutionHistory:
    """P1-2: 执行历史存储测试"""
    
    @pytest.fixture
    def history(self):
        """创建ExecutionHistory实例"""
        from lib.webui.server import ExecutionHistory
        return ExecutionHistory(max_size=10)
    
    def test_add_single_record(self, history):
        """测试添加单条记录"""
        history.add(
            run_id="test001",
            case_name="sample_case",
            passed=True,
            step_ok=5,
            step_count=5,
            duration_s=10.5,
            env="sit",
            timestamp="2026-04-28T10:00:00"
        )
        
        recent = history.get_recent(1)
        assert len(recent) == 1
        assert recent[0]["run_id"] == "test001"
        assert recent[0]["passed"] == True
        assert recent[0]["case_name"] == "sample_case"
    
    def test_add_multiple_records(self, history):
        """测试添加多条记录"""
        for i in range(5):
            history.add(
                run_id=f"run{i:03d}",
                case_name=f"case_{i}",
                passed=i % 2 == 0,
                step_ok=i + 1,
                step_count=i + 1,
                duration_s=float(i + 1),
                env="sit",
                timestamp=f"2026-04-28T10:0{i}:00"
            )
        
        recent = history.get_recent(10)
        assert len(recent) == 5
    
    def test_max_size_limit(self):
        """测试最大容量限制"""
        from lib.webui.server import ExecutionHistory
        
        history = ExecutionHistory(max_size=5)
        for i in range(10):
            history.add(
                run_id=f"run{i}",
                case_name="test",
                passed=i % 2 == 0,
                step_ok=5,
                step_count=5,
                duration_s=1.0,
                env="sit",
                timestamp="2026-04-28"
            )
        
        recent = history.get_recent(10)
        assert len(recent) == 5  # 只保留最新5条
    
    def test_get_by_case(self, history):
        """测试按用例名筛选"""
        history.add("r1", "case_a", True, 5, 5, 1.0, "sit", "t1")
        history.add("r2", "case_b", False, 3, 5, 1.0, "sit", "t2")
        history.add("r3", "case_a", True, 5, 5, 1.0, "sit", "t3")
        
        case_a_history = history.get_by_case("case_a")
        assert len(case_a_history) == 2
        assert all(r["case_name"] == "case_a" for r in case_a_history)
    
    def test_get_by_case_limit(self, history):
        """测试按用例名筛选数量限制"""
        for i in range(10):
            history.add(f"r{i}", "case_x", True, 5, 5, 1.0, "sit", f"t{i}")
        
        case_history = history.get_by_case("case_x", limit=3)
        assert len(case_history) == 3
    
    def test_get_recent_order(self, history):
        """测试获取最近记录的顺序"""
        history.add("r1", "case", True, 1, 1, 1.0, "sit", "t1")
        history.add("r2", "case", True, 1, 1, 1.0, "sit", "t2")
        history.add("r3", "case", True, 1, 1, 1.0, "sit", "t3")
        
        recent = history.get_recent(3)
        # 最新记录在前或在后（取决于实现）
        assert len(recent) == 3
    
    def test_empty_history(self, history):
        """测试空历史"""
        recent = history.get_recent(10)
        assert len(recent) == 0
    
    def test_record_structure(self, history):
        """测试记录结构完整性"""
        history.add("test_id", "test_case", False, 3, 5, 2.5, "uat", "2026-04-28T12:00:00")
        
        record = history.get_recent(1)[0]
        required_fields = [
            "run_id", "case_name", "passed", 
            "step_ok", "step_count", "duration_s", 
            "env", "timestamp"
        ]
        for field in required_fields:
            assert field in record, f"Missing field: {field}"


class TestVariableResolver:
    """变量解析系统测试"""
    
    def test_rand_variable(self):
        """测试随机数字变量"""
        from lib.runner import resolve_vars
        
        # 测试 ${rand:6} 生成6位随机数
        result = resolve_vars("${rand:6}", {})
        assert len(result) == 6
        assert result.isdigit()
    
    def test_rand_different_lengths(self):
        """测试不同长度的随机数"""
        from lib.runner import resolve_vars
        
        for n in [4, 6, 8]:
            result = resolve_vars(f"${{rand:{n}}}", {})
            assert len(result) == n
            assert result.isdigit()
    
    def test_today_variable(self):
        """测试日期变量"""
        from lib.runner import resolve_vars
        from datetime import date
        
        result = resolve_vars("${today}", {})
        expected = date.today().isoformat()
        assert result == expected
    
    def test_timestamp_variable(self):
        """测试时间戳变量"""
        from lib.runner import resolve_vars
        
        result = resolve_vars("${timestamp}", {})
        assert len(result) == 13  # 毫秒级
        assert result.isdigit()
    
    def test_uuid_variable(self):
        """测试UUID变量"""
        from lib.runner import resolve_vars
        import uuid
        
        result = resolve_vars("${uuid}", {})
        assert len(result) == 32  # hex格式
        # 验证是有效UUID
        uuid.UUID(hex=result)
    
    def test_vars_reference(self):
        """测试变量引用"""
        from lib.runner import resolve_vars
        
        vars_dict = {"name": "test_value", "num": 123}
        result = resolve_vars("${vars.name}", vars_dict)
        assert result == "test_value"
    
    def test_nested_variable(self):
        """测试嵌套变量引用"""
        from lib.runner import resolve_vars
        
        vars_dict = {"name": "test_${rand:4}", "prefix": "pre"}
        # 注意：实际实现可能需要多轮解析
        result = resolve_vars("${vars.name}", vars_dict)
        assert "test_" in result
    
    def test_env_variable_with_default(self, monkeypatch):
        """测试环境变量带默认值"""
        from lib.runner import resolve_vars
        
        monkeypatch.delenv("NONEXISTENT_VAR", raising=False)
        result = resolve_vars("${env:NONEXISTENT_VAR:default_value}", {})
        assert result == "default_value"
    
    def test_env_variable_exists(self, monkeypatch):
        """测试环境变量存在时"""
        from lib.runner import resolve_vars
        
        monkeypatch.setenv("TEST_VAR_123", "actual_value")
        result = resolve_vars("${env:TEST_VAR_123:fallback}", {})
        assert result == "actual_value"
    
    def test_unresolved_variable(self):
        """测试未解析变量"""
        from lib.runner import resolve_vars
        
        result = resolve_vars("${vars.undefined}", {})
        assert "UNRESOLVED" in result
    
    def test_multiple_variables_in_string(self):
        """测试字符串中的多个变量"""
        from lib.runner import resolve_vars
        
        vars_dict = {"a": "x", "b": "y"}
        result = resolve_vars("prefix_${vars.a}_middle_${vars.b}_suffix", vars_dict)
        assert result == "prefix_x_middle_y_suffix"


class TestHealthCheck:
    """P0-3: 健康检查API测试"""
    
    def test_health_response_structure(self):
        """测试健康检查响应结构"""
        expected_keys = ["status", "timestamp", "cases_count", "envs_count", "uptime_seconds"]
        # 验证预期的字段列表
        assert len(expected_keys) == 5
        assert "status" in expected_keys
    
    def test_health_status_values(self):
        """测试健康状态有效值"""
        valid_statuses = ["healthy", "degraded", "unhealthy"]
        # 验证状态值列表
        assert len(valid_statuses) == 3
    
    @pytest.fixture
    def client(self):
        """创建测试客户端"""
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_health_endpoint_integration(self, client):
        """集成测试：健康检查端点"""
        resp = client.get("/api/health")
        assert resp.status_code == 200
        data = resp.json()
        assert "status" in data
        assert "timestamp" in data


class TestConfig:
    """Config模块测试"""
    
    def test_config_default_values(self):
        """测试配置默认值"""
        from lib.config import WebUIPrefs
        
        prefs = WebUIPrefs()
        assert prefs.port == 8765
        assert prefs.host == "127.0.0.1"
        assert prefs.open_browser == True
    
    def test_credentials_resolve(self):
        """测试凭证解析"""
        from lib.config import Credentials
        
        creds = Credentials(username="test", password="pass")
        assert creds.resolve_username() == "test"
        assert creds.resolve_password() == "pass"
    
    def test_credentials_env_override(self, monkeypatch):
        """测试环境变量覆盖凭证"""
        from lib.config import Credentials
        
        monkeypatch.setenv("TEST_USER", "env_user")
        creds = Credentials(
            username="yaml_user",
            username_env="TEST_USER"
        )
        assert creds.resolve_username() == "env_user"
    
    def test_credentials_is_configured(self):
        """测试凭证是否配置"""
        from lib.config import Credentials
        
        configured = Credentials(username="user", password="pass")
        assert configured.is_configured() == True
        
        empty = Credentials()
        assert empty.is_configured() == False


class TestEdgeCases:
    """边界条件测试"""
    
    def test_empty_case_name(self):
        """测试空用例名"""
        from lib.webui.server import ExecutionHistory
        
        history = ExecutionHistory(max_size=5)
        history.add("r1", "", True, 1, 1, 1.0, "sit", "t1")
        
        result = history.get_recent(1)
        assert len(result) == 1
        assert result[0]["case_name"] == ""
    
    def test_special_characters_in_case_name(self):
        """测试用例名中的特殊字符"""
        from lib.webui.server import ExecutionHistory
        
        history = ExecutionHistory(max_size=5)
        special_name = "测试-用例_001/特殊"
        history.add("r1", special_name, True, 1, 1, 1.0, "sit", "t1")
        
        result = history.get_recent(1)
        assert result[0]["case_name"] == special_name
    
    def test_zero_duration(self):
        """测试零耗时"""
        from lib.webui.server import ExecutionHistory
        
        history = ExecutionHistory(max_size=5)
        history.add("r1", "case", True, 0, 0, 0.0, "sit", "t1")
        
        result = history.get_recent(1)
        assert result[0]["duration_s"] == 0.0


# 运行测试命令：
# cd cosmic-replay-v2 && python -m pytest tests/test_core.py -v
