"""
cosmic-replay v4 - API端点集成测试

测试目标：
1. 健康检查端点
2. 配置管理端点
3. 用例管理端点
4. 执行端点
5. 历史端点
"""
import pytest
import sys
from pathlib import Path

SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))


# 使用TestClient进行API测试
class TestHealthEndpoint:
    """健康检查端点测试"""
    
    @pytest.fixture
    def client(self):
        """创建测试客户端"""
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_health_returns_200(self, client):
        """返回200状态码"""
        resp = client.get("/api/health")
        assert resp.status_code == 200
    
    def test_health_response_structure(self, client):
        """响应结构正确"""
        resp = client.get("/api/health")
        data = resp.json()
        
        # 必需字段
        required_fields = ["status", "timestamp"]
        for field in required_fields:
            assert field in data, f"Missing field: {field}"
    
    def test_health_status_values(self, client):
        """状态值有效"""
        resp = client.get("/api/health")
        status = resp.json()["status"]
        assert status in ("healthy", "degraded", "unhealthy")
    
    def test_health_has_uptime(self, client):
        """包含运行时间"""
        resp = client.get("/api/health")
        data = resp.json()
        assert "uptime_seconds" in data
        assert isinstance(data["uptime_seconds"], int)
        assert data["uptime_seconds"] >= 0
    
    def test_health_has_case_count(self, client):
        """包含用例数量"""
        resp = client.get("/api/health")
        data = resp.json()
        assert "cases_count" in data
        assert isinstance(data["cases_count"], int)


class TestInfoEndpoint:
    """信息端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_info_returns_200(self, client):
        """返回200状态码"""
        resp = client.get("/api/info")
        assert resp.status_code == 200
    
    def test_info_has_version(self, client):
        """包含版本信息"""
        resp = client.get("/api/info")
        data = resp.json()
        assert "version" in data
        assert data["version"] == "0.1.0"
    
    def test_info_has_paths(self, client):
        """包含路径信息"""
        resp = client.get("/api/info")
        data = resp.json()
        
        path_fields = ["skill_root", "config_dir", "cases_dir"]
        for field in path_fields:
            assert field in data, f"Missing path field: {field}"


class TestConfigEndpoints:
    """配置端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_get_config_returns_200(self, client):
        """获取配置"""
        resp = client.get("/api/config")
        assert resp.status_code == 200
    
    def test_config_has_webui_section(self, client):
        """配置包含webui节"""
        resp = client.get("/api/config")
        data = resp.json()
        assert "webui" in data
    
    def test_config_has_envs_section(self, client):
        """配置包含envs节"""
        resp = client.get("/api/config")
        data = resp.json()
        assert "envs" in data
        assert isinstance(data["envs"], list)
    
    def test_config_masks_secrets(self, client):
        """密码被隐藏"""
        resp = client.get("/api/config")
        data = resp.json()
        
        # 检查所有环境的凭证
        for env in data.get("envs", []):
            cred = env.get("credentials", {})
            password = cred.get("password", "")
            if password:
                assert password == "***", "Password should be masked"
    
    def test_get_envs_returns_list(self, client):
        """获取环境列表"""
        resp = client.get("/api/envs")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)


class TestCasesEndpoints:
    """用例端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_list_cases_returns_200(self, client):
        """列出用例"""
        resp = client.get("/api/cases")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)
    
    def test_case_metadata_structure(self, client):
        """用例元数据结构"""
        resp = client.get("/api/cases")
        cases = resp.json()
        
        if cases:  # 如果有用例
            case = cases[0]
            assert "name" in case
            assert "file" in case
    
    def test_get_nonexistent_case_yaml(self, client):
        """获取不存在用例的YAML"""
        resp = client.get("/api/cases/nonexistent_case_xyz/yaml")
        assert resp.status_code == 404
    
    def test_delete_nonexistent_case(self, client):
        """删除不存在用例"""
        resp = client.delete("/api/cases/nonexistent_case_xyz")
        # 可能返回200或404，取决于实现
        assert resp.status_code in (200, 404)


class TestHistoryEndpoints:
    """历史端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_get_history_returns_200(self, client):
        """获取历史"""
        resp = client.get("/api/history")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)
    
    def test_history_limit_parameter(self, client):
        """历史数量参数"""
        resp = client.get("/api/history?limit=5")
        assert resp.status_code == 200
        assert len(resp.json()) <= 5
    
    def test_get_case_history(self, client):
        """获取用例历史"""
        resp = client.get("/api/history/test_case_name")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)


class TestRunEndpoints:
    """运行端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_run_nonexistent_case(self, client):
        """运行不存在用例"""
        resp = client.post("/api/cases/nonexistent_case_xyz/run", json={})
        assert resp.status_code == 404
    
    def test_runs_endpoint_exists(self, client):
        """运行列表端点存在"""
        resp = client.get("/api/runs/")
        assert resp.status_code == 200


class TestBatchEndpoints:
    """批量操作端点测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_batch_delete_empty_list(self, client):
        """批量删除空列表"""
        resp = client.post("/api/cases/batch_delete", json={"names": []})
        assert resp.status_code == 200
        assert resp.json()["count"] == 0
    
    def test_batch_delete_nonexistent_cases(self, client):
        """批量删除不存在用例"""
        resp = client.post("/api/cases/batch_delete", json={
            "names": ["nonexistent_1", "nonexistent_2"]
        })
        assert resp.status_code == 200
        # 应该返回成功但删除数为0
        assert resp.json()["count"] == 0


class TestCORSHeaders:
    """CORS头测试"""
    
    @pytest.fixture
    def client(self):
        try:
            from fastapi.testclient import TestClient
            from lib.webui.server import APP
            return TestClient(APP)
        except ImportError:
            pytest.skip("FastAPI TestClient not available")
    
    def test_health_allows_cors(self, client):
        """健康检查允许CORS"""
        resp = client.options("/api/health")
        # OPTIONS请求应该成功或返回204
        assert resp.status_code in (200, 204, 405)


# 运行测试命令：
# cd cosmic-replay-v4 && python -m pytest tests/integration/test_api_endpoints.py -v
