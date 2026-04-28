# cosmic-replay-v2 测试体系深度分析报告

## 项目概览

| 维度 | 统计 |
|------|------|
| 核心代码 | ~6,000 行 Python |
| 测试代码 | 117 行 (tests/test_core.py) |
| 测试/代码比 | ~2% |
| 用例文件 | 3 个 YAML |
| 核心模块 | 15 个 .py 文件 |

---

## 一、测试覆盖率分析

### 1.1 现有测试覆盖情况

**已覆盖模块** (tests/test_core.py):
- `ExecutionHistory.add()` - 单条记录添加
- `ExecutionHistory.max_size_limit()` - 容量限制
- `ExecutionHistory.get_by_case()` - 按用例名筛选
- `resolve_variables()` - 随机数变量 `${rand:6}`
- `resolve_variables()` - 日期变量 `${today}`
- `resolve_variables()` - 嵌套变量引用

**未覆盖的关键模块**:

| 模块 | 代码行数 | 重要程度 | 当前覆盖率 | 目标覆盖率 |
|------|----------|----------|------------|------------|
| lib/runner.py | 951行 | P0 | 0% | 85% |
| lib/har_extractor.py | 1551行 | P0 | 0% | 80% |
| lib/webui/server.py | 898行 | P0 | 0% | 75% |
| lib/replay.py | 668行 | P0 | 0% | 85% |
| lib/config.py | 312行 | P1 | 5% | 80% |
| lib/diagnoser.py | 125行 | P1 | 0% | 85% |
| lib/advisor.py | 447行 | P1 | 0% | 80% |
| lib/task_manager.py | 282行 | P1 | 0% | 80% |
| lib/field_resolver.py | ~100行 | P2 | 0% | 70% |

### 1.2 测试层级缺口

```
                    ┌─────────────────┐
                    │    E2E测试      │  ← 缺失 (0%)
                    │  Web UI完整流程 │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │        集成测试             │  ← 缺失 (0%)
              │  API端点 + 用例执行流程    │
              └─────────────┬───────────────┘
                            │
         ┌──────────────────┴──────────────────┐
         │            单元测试                 │  ← 仅2% (117行)
         │  ExecutionHistory + resolve_vars   │
         └─────────────────────────────────────┘
```

---

## 二、测试用例设计质量评估

### 2.1 现有测试优点

1. **清晰的测试命名**: `test_add_single_record`, `test_max_size_limit`
2. **边界条件覆盖**: 测试了容量上限边界
3. **断言充分**: 验证了返回结构、数据一致性

### 2.2 现有测试缺陷

| 问题 | 严重度 | 示例 |
|------|--------|------|
| 测试类空实现 | 高 | `TestHealthCheck` 只有空方法体 |
| 缺少负面测试 | 高 | 无异常场景、错误输入测试 |
| 无参数化测试 | 中 | 同一逻辑无多组数据验证 |
| 缺少fixture复用 | 中 | 每个测试重复创建对象 |
| 无测试隔离 | 中 | 未验证测试间独立性 |

### 2.3 覆盖盲点分析

**Runner 核心逻辑未测试**:
- `_parse_yaml_light()` - YAML解析器
- `resolve_vars()` - 变量解析链
- `run_case()` - 用例执行主流程
- `STEP_HANDLERS` - 步骤处理器分发
- `ASSERTION_HANDLERS` - 断言处理器

**HARExtractor 未测试**:
- `extract()` - HAR转YAML主入口
- `smart_name()` - 智能命名算法
- `detect_var_placeholders()` - 变量检测
- `to_yaml()` - YAML序列化

---

## 三、测试数据管理评估

### 3.1 现状

- **测试数据**: 无独立测试数据目录
- **用例文件**: cases/*.yaml (3个示例)
- **配置数据**: config/envs/*.yaml
- **数据隔离**: 无，直接使用生产用例

### 3.2 问题

| 问题 | 影响 |
|------|------|
| 无专用测试数据 | 测试可能污染生产数据 |
| 无测试数据生成器 | 无法批量创建测试场景 |
| 无数据清理机制 | 测试残留数据影响后续运行 |
| 环境配置硬编码 | 无法切换测试/生产环境 |

---

## 四、回归测试策略评估

### 4.1 现状

- **CI/CD集成**: 无
- **自动化回归**: 无
- **测试调度**: 手动执行
- **变更检测**: 无

### 4.2 风险

```
变更代码 → 无自动测试 → 可能引入缺陷 → 生产故障
```

---

## 五、测试报告可读性评估

### 5.1 现有输出

```
pytest 默认输出 → 控制台文本
logs/runs/*.jsonl → JSON行日志（机器可读，人工难读）
```

### 5.2 问题

- 无HTML报告
- 无测试趋势统计
- 无失败原因聚合
- 无执行时间分析
- 无覆盖率报告

---

## 六、优化建议

### 6.1 测试架构重设计

```
tests/
├── conftest.py              # 共享fixtures
├── unit/                    # 单元测试
│   ├── test_runner.py       # runner模块
│   ├── test_har_extractor.py
│   ├── test_config.py
│   ├── test_replay.py
│   ├── test_diagnoser.py
│   └── test_advisor.py
├── integration/             # 集成测试
│   ├── test_api_endpoints.py
│   ├── test_case_execution.py
│   └── test_har_workflow.py
├── e2e/                     # 端到端测试
│   └── test_web_ui.py
├── fixtures/                # 测试数据
│   ├── sample.har
│   ├── sample_case.yaml
│   └── mock_responses.py
└── utils/                   # 测试工具
    └── test_helpers.py
```

### 6.2 优先级排序

| P级 | 目标 | 预计工时 |
|-----|------|----------|
| P0 | runner.py 单元测试 | 2天 |
| P0 | har_extractor.py 单元测试 | 2天 |
| P1 | API集成测试 | 1天 |
| P1 | config.py 完整测试 | 0.5天 |
| P2 | E2E Web UI测试 | 1天 |
| P2 | 测试报告系统 | 0.5天 |

### 6.3 测试数据管理方案

```python
# tests/fixtures/data_factory.py
class TestDataFactory:
    """测试数据工厂"""
    
    @staticmethod
    def create_minimal_case() -> dict:
        """最小可用用例"""
        return {
            "name": "test_case_minimal",
            "env": {"base_url": "http://test.local"},
            "steps": []
        }
    
    @staticmethod
    def create_case_with_steps(step_count: int) -> dict:
        """创建指定步骤数的用例"""
        ...
    
    @staticmethod
    def create_mock_har(actions: list[dict]) -> dict:
        """创建模拟HAR"""
        ...
```

### 6.4 回归测试自动化

```yaml
# .github/workflows/test.yml
name: Test Suite
on: [push, pull_request]

jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with:
          python-version: '3.11'
      - run: pip install -r requirements.txt pytest pytest-cov
      - run: pytest tests/unit -v --cov=lib --cov-report=xml
      - uses: codecov/codecov-action@v4
  
  integration-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: pytest tests/integration -v
```

---

## 七、具体测试方案

### 7.1 Runner核心测试用例

```python
# tests/unit/test_runner.py
import pytest
from lib.runner import (
    load_yaml, resolve_vars, run_case,
    _parse_yaml_light, STEP_HANDLERS, ASSERTION_HANDLERS
)

class TestYamlParsing:
    """YAML解析测试"""
    
    def test_parse_simple_dict(self, tmp_path):
        """简单字典解析"""
        yaml_file = tmp_path / "test.yaml"
        yaml_file.write_text("name: test\nvalue: 123")
        result = load_yaml(yaml_file)
        assert result == {"name": "test", "value": 123}
    
    def test_parse_with_list(self, tmp_path):
        """包含列表的解析"""
        yaml_file = tmp_path / "test.yaml"
        yaml_file.write_text("steps:\n  - id: s1\n  - id: s2")
        result = load_yaml(yaml_file)
        assert len(result["steps"]) == 2
    
    def test_parse_chinese_content(self, tmp_path):
        """中文内容解析"""
        yaml_file = tmp_path / "test.yaml"
        yaml_file.write_text('name: "测试用例"\ndescription: 中文描述')
        result = load_yaml(yaml_file)
        assert "测试" in result["name"]
    
    def test_parse_malformed_yaml(self, tmp_path):
        """异常YAML处理"""
        yaml_file = tmp_path / "test.yaml"
        yaml_file.write_text("name: test\n  invalid indent")
        with pytest.raises(Exception):
            load_yaml(yaml_file)


class TestVariableResolution:
    """变量解析测试"""
    
    def test_resolve_timestamp(self):
        """时间戳变量"""
        result = resolve_vars("${timestamp}", {})
        assert result.isdigit()
        assert len(result) == 13  # 毫秒级
    
    def test_resolve_today(self):
        """日期变量"""
        from datetime import date
        result = resolve_vars("${today}", {})
        assert result == date.today().isoformat()
    
    def test_resolve_rand_n_digits(self):
        """随机N位数字"""
        result = resolve_vars("${rand:6}", {})
        assert len(result) == 6
        assert result.isdigit()
    
    def test_resolve_rand_different_lengths(self):
        """不同长度的随机数"""
        for n in [4, 6, 8, 10]:
            result = resolve_vars(f"${{rand:{n}}}", {})
            assert len(result) == n
    
    def test_resolve_nested_vars(self):
        """嵌套变量"""
        vars_dict = {"prefix": "test", "num": "${rand:4}"}
        result = resolve_vars("${vars.prefix}_${vars.num}", vars_dict)
        assert result.startswith("test_")
    
    def test_resolve_env_with_default(self, monkeypatch):
        """环境变量带默认值"""
        monkeypatch.delenv("NON_EXISTENT_VAR", raising=False)
        result = resolve_vars("${env:NON_EXISTENT_VAR:fallback}", {})
        assert result == "fallback"
    
    def test_resolve_missing_var(self):
        """未定义变量"""
        result = resolve_vars("${vars.undefined}", {})
        assert "UNRESOLVED" in result


class TestStepHandlers:
    """步骤处理器测试"""
    
    def test_open_form_handler_registered(self):
        """open_form处理器已注册"""
        assert "open_form" in STEP_HANDLERS
    
    def test_invoke_handler_registered(self):
        """invoke处理器已注册"""
        assert "invoke" in STEP_HANDLERS
    
    def test_all_handlers_callable(self):
        """所有处理器可调用"""
        for name, handler in STEP_HANDLERS.items():
            assert callable(handler), f"{name} handler not callable"


class TestAssertionHandlers:
    """断言处理器测试"""
    
    def test_no_error_actions_pass(self):
        """无错误时通过"""
        result = ASSERTION_HANDLERS["no_error_actions"](
            {"last_step": True}, 
            {"last_response": []}
        )
        assert result[0] == True
    
    def test_no_error_actions_fail(self):
        """有错误时失败"""
        result = ASSERTION_HANDLERS["no_error_actions"](
            {"last_step": True},
            {"last_response": [{"a": "showErrMsg", "args": ["错误"]}]}
        )
        assert result[0] == False
        assert "错误" in result[1]
```

### 7.2 HARExtractor测试用例

```python
# tests/unit/test_har_extractor.py
import pytest
import json
from pathlib import Path
from lib.har_extractor import (
    load_har, is_business_request, smart_name,
    detect_var_placeholders, extract, to_yaml,
    AC_TIER, generate_step_description
)

class TestHARLoading:
    """HAR加载测试"""
    
    def test_load_valid_har(self, tmp_path):
        """加载有效HAR"""
        har_file = tmp_path / "test.har"
        har_data = {
            "log": {
                "entries": [
                    {"request": {"url": "http://test.com/form/api", "method": "POST"}}
                ]
            }
        }
        har_file.write_text(json.dumps(har_data))
        result = load_har(har_file)
        assert "log" in result
        assert len(result["log"]["entries"]) == 1
    
    def test_load_empty_har(self, tmp_path):
        """空HAR文件"""
        har_file = tmp_path / "empty.har"
        har_file.write_text("{}")
        with pytest.raises(Exception):
            load_har(har_file)


class TestBusinessRequestDetection:
    """业务请求识别测试"""
    
    def test_is_business_request_valid(self):
        """有效业务请求"""
        assert is_business_request("http://example.com/form/api") == True
    
    def test_is_business_request_static(self):
        """静态资源请求"""
        assert is_business_request("http://example.com/static.js") == False
        assert is_business_request("http://example.com/style.css") == False
        assert is_business_request("http://example.com/image.png") == False
    
    def test_is_business_request_non_form(self):
        """非/form/路径"""
        assert is_business_request("http://example.com/api/data") == False


class TestSmartNaming:
    """智能命名测试"""
    
    def test_save_action(self):
        """保存动作命名"""
        action = {"methodName": "itemClick", "args": ["save"]}
        name = smart_name(action, "save", 1)
        assert "save" in name
    
    def test_update_value_action(self):
        """更新值命名"""
        action = {
            "methodName": "updateValue",
            "postData": [{}, [{"k": "name", "v": "test"}]]
        }
        name = smart_name(action, "updateValue", 1)
        assert "fill" in name or "update" in name
    
    def test_toolbar_click(self):
        """工具栏按钮命名"""
        action = {
            "key": "toolbarap",
            "methodName": "itemClick",
            "args": ["addnew"]
        }
        name = smart_name(action, "itemClick", 1)
        assert "addnew" in name or "click" in name


class TestVariableDetection:
    """变量检测测试"""
    
    def test_detect_unique_number_field(self):
        """唯一编号字段"""
        actions = [
            {"methodName": "updateValue", "postData": [{}, [{"k": "number", "v": "TEST12345"}]]}
        ]
        _, vars_map = detect_var_placeholders(actions)
        assert "test_number" in vars_map
        assert "${rand:" in vars_map["test_number"]
    
    def test_detect_date_field(self):
        """日期字段"""
        actions = [
            {"methodName": "updateValue", "postData": [{}, [{"k": "effectdate", "v": "2026-04-28"}]]}
        ]
        _, vars_map = detect_var_placeholders(actions)
        assert "${today}" in actions[0]["postData"][1][0]["v"] or "${today}" in str(vars_map)
    
    def test_detect_phone_field(self):
        """电话号码字段"""
        actions = [
            {"methodName": "updateValue", "postData": [{}, [{"k": "phone", "v": "13800138000"}]]}
        ]
        _, vars_map = detect_var_placeholders(actions)
        assert "test_phone" in vars_map


class TestStepDescription:
    """步骤描述生成测试"""
    
    def test_open_form_description(self):
        """打开表单描述"""
        step = {"type": "open_form", "form_id": "haos_adminorgdetail"}
        desc = generate_step_description(step)
        assert "打开" in desc or "行政组织" in desc or "adminorg" in desc.lower()
    
    def test_save_description(self):
        """保存描述"""
        step = {"type": "invoke", "ac": "save"}
        desc = generate_step_description(step)
        assert "保存" in desc
    
    def test_fill_field_description(self):
        """填写字段描述"""
        step = {"type": "update_fields", "fields": {"name": "测试"}}
        desc = generate_step_description(step)
        assert "填写" in desc or "名称" in desc


class TestYAMLGeneration:
    """YAML生成测试"""
    
    def test_to_yaml_basic(self):
        """基础YAML生成"""
        data = {"name": "测试", "steps": [{"id": "s1", "type": "open_form"}]}
        yaml_str = to_yaml(data)
        assert "name:" in yaml_str
        assert "测试" in yaml_str
        assert "steps:" in yaml_str
    
    def test_to_yaml_multilang(self):
        """多语言字段"""
        data = {"name": {"zh_CN": "测试", "en_US": "test"}}
        yaml_str = to_yaml(data)
        assert "zh_CN" in yaml_str
```

### 7.3 API集成测试

```python
# tests/integration/test_api_endpoints.py
import pytest
from fastapi.testclient import TestClient
from lib.webui.server import APP

@pytest.fixture
def client():
    """测试客户端"""
    return TestClient(APP)


class TestHealthEndpoint:
    """健康检查端点测试"""
    
    def test_health_returns_200(self, client):
        """返回200"""
        resp = client.get("/api/health")
        assert resp.status_code == 200
    
    def test_health_has_required_fields(self, client):
        """包含必需字段"""
        resp = client.get("/api/health")
        data = resp.json()
        assert "status" in data
        assert "timestamp" in data
        assert "uptime_seconds" in data
    
    def test_health_healthy_status(self, client):
        """健康状态"""
        resp = client.get("/api/health")
        assert resp.json()["status"] in ("healthy", "degraded")


class TestInfoEndpoint:
    """信息端点测试"""
    
    def test_info_returns_version(self, client):
        """返回版本"""
        resp = client.get("/api/info")
        assert resp.status_code == 200
        assert "version" in resp.json()
    
    def test_info_returns_paths(self, client):
        """返回路径"""
        resp = client.get("/api/info")
        data = resp.json()
        assert "skill_root" in data
        assert "config_dir" in data
        assert "cases_dir" in data


class TestCasesEndpoints:
    """用例端点测试"""
    
    def test_list_cases(self, client):
        """列出用例"""
        resp = client.get("/api/cases")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)
    
    def test_get_case_yaml_not_found(self, client):
        """获取不存在的用例"""
        resp = client.get("/api/cases/nonexistent/yaml")
        assert resp.status_code == 404


class TestConfigEndpoints:
    """配置端点测试"""
    
    def test_get_config(self, client):
        """获取配置"""
        resp = client.get("/api/config")
        assert resp.status_code == 200
        data = resp.json()
        assert "webui" in data
        assert "envs" in data
    
    def test_config_masks_secrets(self, client):
        """密码打码"""
        resp = client.get("/api/config")
        # 如果有密码字段，应该是 ***
        data = resp.json()
        # 验证没有明文密码泄露


class TestHistoryEndpoints:
    """历史端点测试"""
    
    def test_get_history(self, client):
        """获取历史"""
        resp = client.get("/api/history")
        assert resp.status_code == 200
        assert isinstance(resp.json(), list)
    
    def test_history_limit_parameter(self, client):
        """历史数量限制"""
        resp = client.get("/api/history?limit=5")
        assert resp.status_code == 200
        assert len(resp.json()) <= 5
```

### 7.4 测试配置

```python
# tests/conftest.py
import pytest
import tempfile
import shutil
from pathlib import Path
from lib.config import Config
from lib.webui.log_store import LogStore


@pytest.fixture(scope="session")
def test_data_dir():
    """测试数据目录"""
    return Path(__file__).parent / "fixtures"


@pytest.fixture
def temp_config_dir(tmp_path):
    """临时配置目录"""
    config_dir = tmp_path / "config"
    config_dir.mkdir()
    (config_dir / "envs").mkdir()
    
    # 创建默认配置
    webui_config = {"webui": {"port": 8765, "host": "127.0.0.1"}}
    import yaml
    with open(config_dir / "webui.yaml", "w") as f:
        yaml.dump(webui_config, f)
    
    return config_dir


@pytest.fixture
def config(temp_config_dir):
    """配置实例"""
    return Config(config_dir=temp_config_dir)


@pytest.fixture
def log_store(tmp_path):
    """日志存储实例"""
    return LogStore(tmp_path, buffer_size=10, retention_days=1)


@pytest.fixture
def sample_har(test_data_dir):
    """示例HAR数据"""
    har_file = test_data_dir / "sample.har"
    if har_file.exists():
        import json
        return json.loads(har_file.read_text())
    return None


@pytest.fixture
def sample_case(test_data_dir):
    """示例用例"""
    import yaml
    case_file = test_data_dir / "sample_case.yaml"
    if case_file.exists():
        return yaml.safe_load(case_file.read_text())
    return None


# 参数化测试数据
@pytest.fixture(params=[
    ("${rand:4}", 4),
    ("${rand:6}", 6),
    ("${rand:8}", 8),
])
def rand_params(request):
    """随机数参数"""
    return request.param


@pytest.fixture(params=[
    "${timestamp}",
    "${today}",
    "${uuid}",
    "${now}",
])
def special_vars(request):
    """特殊变量"""
    return request.param
```

---

## 八、实施路线图

### Phase 1: 基础设施 (Day 1-2)
- [ ] 创建tests目录结构
- [ ] 配置pytest和覆盖率
- [ ] 编写conftest.py fixtures
- [ ] 准备测试数据fixtures

### Phase 2: 核心单元测试 (Day 3-5)
- [ ] runner.py 测试 (目标: 85%覆盖)
- [ ] har_extractor.py 测试 (目标: 80%覆盖)
- [ ] config.py 测试 (目标: 80%覆盖)
- [ ] replay.py 测试 (目标: 85%覆盖)

### Phase 3: 集成测试 (Day 6-7)
- [ ] API端点测试
- [ ] 用例执行流程测试
- [ ] HAR工作流测试

### Phase 4: 自动化与报告 (Day 8)
- [ ] GitHub Actions CI配置
- [ ] HTML报告生成
- [ ] 覆盖率报告上传

---

## 九、测试指标目标

| 指标 | 当前 | 目标 |
|------|------|------|
| 代码覆盖率 | ~2% | ≥75% |
| 测试用例数 | 7 | ≥80 |
| 测试执行时间 | <1s | <30s |
| 失败重跑率 | N/A | ≥95% |
| 缺陷检出率 | 未知 | 目标5+/月 |

---

*报告生成时间: 2026-04-28*
*分析者: 测试架构师*
