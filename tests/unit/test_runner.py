"""
cosmic-replay v2 - Runner模块单元测试

测试目标：
1. YAML解析功能
2. 变量解析系统
3. 步骤处理器分发
4. 断言处理器
5. 运行器主流程
"""
import pytest
import json
import sys
from pathlib import Path
from datetime import date, datetime

# 添加项目根目录到路径
SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))

from lib.runner import (
    load_yaml, _parse_yaml_light, resolve_vars, _resolve_str, _resolve_ref,
    STEP_HANDLERS, ASSERTION_HANDLERS,
)


class TestYAMLParsing:
    """YAML解析测试"""
    
    def test_load_yaml_simple_dict(self, temp_dir: Path):
        """简单字典解析"""
        yaml_file = temp_dir / "test.yaml"
        yaml_file.write_text("name: test\nvalue: 123", encoding="utf-8")
        result = load_yaml(yaml_file)
        assert result["name"] == "test"
        assert result["value"] == 123
    
    def test_load_yaml_with_list(self, temp_dir: Path):
        """包含列表的解析"""
        yaml_file = temp_dir / "test.yaml"
        yaml_file.write_text("""
steps:
  - id: s1
    type: open_form
  - id: s2
    type: invoke
""", encoding="utf-8")
        result = load_yaml(yaml_file)
        assert "steps" in result
        assert len(result["steps"]) == 2
        assert result["steps"][0]["id"] == "s1"
    
    def test_load_yaml_nested_dict(self, temp_dir: Path):
        """嵌套字典解析"""
        yaml_file = temp_dir / "test.yaml"
        yaml_file.write_text("""
env:
  base_url: http://test.local
  credentials:
    username: admin
    password: secret
""", encoding="utf-8")
        result = load_yaml(yaml_file)
        assert result["env"]["base_url"] == "http://test.local"
        assert result["env"]["credentials"]["username"] == "admin"
    
    def test_load_yaml_chinese_content(self, temp_dir: Path):
        """中文内容解析"""
        yaml_file = temp_dir / "test.yaml"
        yaml_file.write_text('name: "测试用例"\ndescription: 这是一个中文描述', encoding="utf-8")
        result = load_yaml(yaml_file)
        assert "测试" in result["name"]
    
    def test_load_yaml_multilang_value(self, temp_dir: Path):
        """多语言值解析"""
        yaml_file = temp_dir / "test.yaml"
        yaml_file.write_text('name: {"zh_CN": "中文", "en_US": "English"}', encoding="utf-8")
        result = load_yaml(yaml_file)
        assert result["name"]["zh_CN"] == "中文"
    
    def test_parse_yaml_light_empty_string(self):
        """空字符串解析"""
        result = _parse_yaml_light("")
        assert result == {}
    
    def test_parse_yaml_light_comments(self):
        """注释过滤"""
        yaml_text = """
# 这是注释
name: test  # 行尾注释
# 另一个注释
value: 123
"""
        result = _parse_yaml_light(yaml_text)
        assert result["name"] == "test"
        assert result["value"] == 123
    
    def test_parse_yaml_light_boolean(self):
        """布尔值解析"""
        yaml_text = """
enabled: true
disabled: false
flag1: True
flag2: FALSE
"""
        result = _parse_yaml_light(yaml_text)
        assert result["enabled"] == True
        assert result["disabled"] == False
        assert result["flag1"] == True
        assert result["flag2"] == False
    
    def test_parse_yaml_light_null(self):
        """空值解析"""
        yaml_text = """
name: null
empty: ~
none_value: None
"""
        result = _parse_yaml_light(yaml_text)
        assert result["name"] == None
        assert result["empty"] == None
        assert result["none_value"] == None
    
    def test_parse_yaml_light_numbers(self):
        """数字解析"""
        yaml_text = """
integer: 123
float_num: 45.67
negative: -100
scientific: 1.5e10
"""
        result = _parse_yaml_light(yaml_text)
        assert result["integer"] == 123
        assert result["float_num"] == 45.67
        assert result["negative"] == -100
    
    def test_parse_yaml_light_inline_json(self):
        """内联JSON解析"""
        yaml_text = """
list_field: [1, 2, 3]
dict_field: {"key": "value"}
"""
        result = _parse_yaml_light(yaml_text)
        assert result["list_field"] == [1, 2, 3]
        assert result["dict_field"] == {"key": "value"}


class TestVariableResolution:
    """变量解析测试"""
    
    def test_resolve_vars_simple_string(self):
        """普通字符串（无变量）"""
        result = resolve_vars("hello world", {})
        assert result == "hello world"
    
    def test_resolve_vars_dict(self):
        """字典中的变量"""
        vars_dict = {"name": "test"}
        result = resolve_vars({"key": "${vars.name}"}, vars_dict)
        assert result["key"] == "test"
    
    def test_resolve_vars_list(self):
        """列表中的变量"""
        vars_dict = {"id": "123"}
        result = resolve_vars(["${vars.id}", "static", "${vars.id}_suffix"], vars_dict)
        assert result == ["123", "static", "123_suffix"]
    
    def test_resolve_vars_nested(self):
        """嵌套结构中的变量"""
        vars_dict = {"name": "test", "value": "123"}
        data = {
            "level1": {
                "level2": {
                    "field": "${vars.name}_${vars.value}"
                },
                "list": ["${vars.name}", {"sub": "${vars.value}"}]
            }
        }
        result = resolve_vars(data, vars_dict)
        assert result["level1"]["level2"]["field"] == "test_123"
        assert result["level1"]["list"][0] == "test"
        assert result["level1"]["list"][1]["sub"] == "123"
    
    def test_resolve_timestamp(self):
        """时间戳变量"""
        result = _resolve_ref("timestamp", {})
        assert result.isdigit()
        assert len(result) == 13  # 毫秒级时间戳
    
    def test_resolve_today(self):
        """日期变量"""
        result = _resolve_ref("today", {})
        expected = datetime.now().strftime("%Y-%m-%d")
        assert result == expected
    
    def test_resolve_now(self):
        """当前时间变量"""
        result = _resolve_ref("now", {})
        # 格式：YYYY-MM-DD HH:MM:SS
        assert len(result) == 19
        assert "-" in result
        assert ":" in result
    
    def test_resolve_rand_4_digits(self):
        """4位随机数"""
        result = _resolve_ref("rand:4", {})
        assert len(result) == 4
        assert result.isdigit()
    
    def test_resolve_rand_6_digits(self):
        """6位随机数"""
        result = _resolve_ref("rand:6", {})
        assert len(result) == 6
        assert result.isdigit()
    
    def test_resolve_rand_different_lengths(self, rand_length_params):
        """参数化随机数长度"""
        expr, expected_len = rand_length_params
        n = int(expr.split(":")[1].rstrip("}"))
        result = _resolve_ref(f"rand:{n}", {})
        assert len(result) == n
    
    def test_resolve_uuid(self):
        """UUID变量"""
        result = _resolve_ref("uuid", {})
        assert len(result) == 32  # hex格式
        # 验证可以转换为有效的UUID
        import uuid
        uuid.UUID(hex=result)  # 不抛异常即为有效
    
    def test_resolve_vars_reference(self):
        """变量引用"""
        vars_dict = {"test_name": "hello", "test_value": "world"}
        result = _resolve_ref("vars.test_name", vars_dict)
        assert result == "hello"
    
    def test_resolve_env_without_default(self, monkeypatch):
        """环境变量（无默认值）"""
        monkeypatch.setenv("TEST_VAR_123", "test_value")
        result = _resolve_ref("env:TEST_VAR_123", {})
        assert result == "test_value"
    
    def test_resolve_env_with_default(self, monkeypatch):
        """环境变量（有默认值，环境变量存在）"""
        monkeypatch.setenv("TEST_VAR_WITH_DEFAULT", "actual_value")
        result = _resolve_ref("env:TEST_VAR_WITH_DEFAULT:fallback", {})
        assert result == "actual_value"
    
    def test_resolve_env_fallback(self, monkeypatch):
        """环境变量回退到默认值"""
        monkeypatch.delenv("NONEXISTENT_VAR_123", raising=False)
        result = _resolve_ref("env:NONEXISTENT_VAR_123:fallback_value", {})
        assert result == "fallback_value"
    
    def test_resolve_missing_var(self):
        """未定义变量"""
        result = _resolve_ref("vars.undefined_var", {})
        assert "UNRESOLVED" in result
    
    def test_resolve_empty_var_namespace(self):
        """空变量命名空间"""
        result = _resolve_ref("vars.nonexistent", {})
        assert "UNRESOLVED" in result
    
    def test_resolve_str_multiple_vars(self):
        """字符串中多个变量"""
        vars_dict = {"a": "x", "b": "y"}
        result = _resolve_str("${vars.a}_${vars.b}", vars_dict)
        assert result == "x_y"
    
    def test_resolve_str_mixed_content(self):
        """混合内容字符串"""
        vars_dict = {"name": "test"}
        result = _resolve_str("prefix_${vars.name}_suffix", vars_dict)
        assert result == "prefix_test_suffix"
    
    def test_resolve_date_object(self):
        """日期对象转换"""
        test_date = date(2026, 4, 28)
        result = resolve_vars(test_date, {})
        assert result == "2026-04-28"
    
    def test_resolve_datetime_object(self):
        """日期时间对象转换"""
        test_datetime = datetime(2026, 4, 28, 10, 30, 45)
        result = resolve_vars(test_datetime, {})
        assert "2026-04-28" in result


class TestStepHandlers:
    """步骤处理器测试"""
    
    def test_open_form_handler_registered(self):
        """open_form处理器已注册"""
        assert "open_form" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["open_form"])
    
    def test_invoke_handler_registered(self):
        """invoke处理器已注册"""
        assert "invoke" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["invoke"])
    
    def test_update_fields_handler_registered(self):
        """update_fields处理器已注册"""
        assert "update_fields" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["update_fields"])
    
    def test_pick_basedata_handler_registered(self):
        """pick_basedata处理器已注册"""
        assert "pick_basedata" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["pick_basedata"])
    
    def test_click_toolbar_handler_registered(self):
        """click_toolbar处理器已注册"""
        assert "click_toolbar" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["click_toolbar"])
    
    def test_click_menu_handler_registered(self):
        """click_menu处理器已注册"""
        assert "click_menu" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["click_menu"])
    
    def test_sleep_handler_registered(self):
        """sleep处理器已注册"""
        assert "sleep" in STEP_HANDLERS
        assert callable(STEP_HANDLERS["sleep"])
    
    def test_all_handlers_callable(self):
        """所有处理器可调用"""
        for name, handler in STEP_HANDLERS.items():
            assert callable(handler), f"Handler {name} is not callable"


class TestAssertionHandlers:
    """断言处理器测试"""
    
    def test_no_error_actions_handler_registered(self):
        """no_error_actions处理器已注册"""
        assert "no_error_actions" in ASSERTION_HANDLERS
        assert callable(ASSERTION_HANDLERS["no_error_actions"])
    
    def test_no_save_failure_handler_registered(self):
        """no_save_failure处理器已注册"""
        assert "no_save_failure" in ASSERTION_HANDLERS
        assert callable(ASSERTION_HANDLERS["no_save_failure"])
    
    def test_response_contains_handler_registered(self):
        """response_contains处理器已注册"""
        assert "response_contains" in ASSERTION_HANDLERS
        assert callable(ASSERTION_HANDLERS["response_contains"])
    
    def test_no_error_actions_pass_on_empty(self):
        """无错误时通过"""
        ctx = {
            "last_response": [],
            "last_step_response": [],
            "step_responses": {}
        }
        passed, msg = ASSERTION_HANDLERS["no_error_actions"](
            {"last_step": True}, ctx
        )
        assert passed == True
    
    def test_no_error_actions_fail_on_error(self):
        """有错误时失败"""
        ctx = {
            "last_response": [{"a": "showErrMsg", "args": ["错误信息"]}],
            "last_step_response": [{"a": "showErrMsg", "args": ["错误信息"]}],
            "step_responses": {}
        }
        passed, msg = ASSERTION_HANDLERS["no_error_actions"](
            {"last_step": True}, ctx
        )
        assert passed == False
        assert "错误" in msg
    
    def test_response_contains_found(self):
        """响应包含指定内容"""
        ctx = {
            "last_response": {"result": "success", "data": "test_value"},
            "step_responses": {}
        }
        passed, msg = ASSERTION_HANDLERS["response_contains"](
            {"needle": "success"}, ctx
        )
        assert passed == True
    
    def test_response_contains_not_found(self):
        """响应不包含指定内容"""
        ctx = {
            "last_response": {"result": "failure"},
            "step_responses": {}
        }
        passed, msg = ASSERTION_HANDLERS["response_contains"](
            {"needle": "success"}, ctx
        )
        assert passed == False
        assert "没找到" in msg or "not found" in msg.lower()
    
    def test_all_assertion_handlers_callable(self):
        """所有断言处理器可调用"""
        for name, handler in ASSERTION_HANDLERS.items():
            assert callable(handler), f"Assertion handler {name} is not callable"


class TestEdgeCases:
    """边界条件测试"""
    
    def test_empty_vars_dict(self):
        """空变量字典"""
        result = resolve_vars("plain string", {})
        assert result == "plain string"
    
    def test_none_value(self):
        """None值处理"""
        result = resolve_vars(None, {})
        assert result == None
    
    def test_empty_list(self):
        """空列表"""
        result = resolve_vars([], {})
        assert result == []
    
    def test_empty_dict(self):
        """空字典"""
        result = resolve_vars({}, {})
        assert result == {}
    
    def test_special_characters_in_vars(self):
        """变量包含特殊字符"""
        vars_dict = {"path": "/api/v1/test"}
        result = resolve_vars("${vars.path}", vars_dict)
        assert result == "/api/v1/test"
    
    def test_unicode_in_vars(self):
        """变量包含Unicode"""
        vars_dict = {"name": "测试用户"}
        result = resolve_vars("${vars.name}", vars_dict)
        assert result == "测试用户"
    
    def test_numeric_key_in_vars(self):
        """数字作为变量值"""
        vars_dict = {"count": 42, "ratio": 3.14}
        result = resolve_vars({"num": "${vars.count}", "float": "${vars.ratio}"}, vars_dict)
        assert result["num"] == 42
        assert result["float"] == 3.14


class TestHelperFunctions:
    """辅助函数测试"""
    
    def test_resolve_str_returns_type(self):
        """_resolve_str返回正确类型"""
        # 整串是变量时返回解析后的类型
        vars_dict = {"num": 123}
        result = _resolve_str("${vars.num}", vars_dict)
        assert result == 123
        assert isinstance(result, int)
    
    def test_resolve_str_returns_string_for_partial(self):
        """部分变量时返回字符串"""
        vars_dict = {"name": "test"}
        result = _resolve_str("prefix_${vars.name}_suffix", vars_dict)
        assert result == "prefix_test_suffix"
        assert isinstance(result, str)
    
    def test_resolve_ref_handles_whitespace(self):
        """处理空白"""
        # 带空格的引用
        vars_dict = {"name": "test"}
        result = _resolve_ref(" vars.name ", vars_dict)
        assert result == "test"


# 运行测试命令：
# cd cosmic-replay-v2 && python -m pytest tests/unit/test_runner.py -v
