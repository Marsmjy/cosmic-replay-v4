"""
cosmic-replay v4 - HARExtractor模块单元测试

测试目标：
1. HAR文件加载
2. 业务请求识别
3. 智能命名
4. 变量检测与占位符
5. YAML生成
"""
import pytest
import json
import sys
from pathlib import Path

SKILL_ROOT = Path(__file__).resolve().parent.parent.parent
sys.path.insert(0, str(SKILL_ROOT))

from lib.har_extractor import (
    load_har, is_business_request, smart_name, _sanitize,
    detect_var_placeholders, AC_TIER,
    generate_step_description, _form_short, _extract_value_prefix,
)


class TestHARLoading:
    """HAR文件加载测试"""
    
    def test_load_har_valid(self, temp_dir: Path, sample_har: dict):
        """加载有效HAR文件"""
        har_file = temp_dir / "test.har"
        har_file.write_text(json.dumps(sample_har), encoding="utf-8")
        result = load_har(har_file)
        assert "log" in result
        assert "entries" in result["log"]
    
    def test_load_har_empty_entries(self, temp_dir: Path):
        """空entries的HAR"""
        har_data = {"log": {"version": "1.2", "entries": []}}
        har_file = temp_dir / "empty.har"
        har_file.write_text(json.dumps(har_data), encoding="utf-8")
        result = load_har(har_file)
        assert result["log"]["entries"] == []
    
    def test_load_har_minimal(self, temp_dir: Path):
        """最小HAR结构"""
        har_data = {"log": {"entries": []}}
        har_file = temp_dir / "minimal.har"
        har_file.write_text(json.dumps(har_data), encoding="utf-8")
        result = load_har(har_file)
        assert result["log"]["entries"] == []


class TestBusinessRequestDetection:
    """业务请求识别测试"""
    
    def test_is_business_request_form_url(self):
        """包含/form/的业务请求"""
        assert is_business_request("http://test.com/form/batchInvokeAction.do") == True
        assert is_business_request("https://api.example.com/form/api") == True
    
    def test_is_business_request_static_js(self):
        """静态JS文件"""
        assert is_business_request("http://test.com/static/app.js") == False
    
    def test_is_business_request_static_css(self):
        """静态CSS文件"""
        assert is_business_request("http://test.com/style/main.css") == False
    
    def test_is_business_request_static_images(self):
        """图片文件"""
        assert is_business_request("http://test.com/images/logo.png") == False
        assert is_business_request("http://test.com/icons/arrow.svg") == False
    
    def test_is_business_request_static_fonts(self):
        """字体文件"""
        assert is_business_request("http://test.com/fonts/main.woff") == False
        assert is_business_request("http://test.com/fonts/main.woff2") == False
    
    def test_is_business_request_non_form_path(self):
        """非/form/路径"""
        assert is_business_request("http://test.com/api/data") == False
        assert is_business_request("http://test.com/v1/users") == False
    
    def test_is_business_request_static_suffix_list(self):
        """所有静态后缀"""
        static_files = [
            "http://test.com/a.js",
            "http://test.com/b.css",
            "http://test.com/c.png",
            "http://test.com/d.jpg",
            "http://test.com/e.jpeg",
            "http://test.com/f.gif",
            "http://test.com/g.svg",
            "http://test.com/h.ico",
            "http://test.com/i.woff",
            "http://test.com/j.woff2",
            "http://test.com/k.ttf",
            "http://test.com/l.eot",
            "http://test.com/m.map",
        ]
        for url in static_files:
            assert is_business_request(url) == False, f"{url} should not be business request"


class TestSmartNaming:
    """智能命名测试"""
    
    def test_smart_name_save(self):
        """保存动作命名"""
        action = {"methodName": "itemClick", "key": "tbmain", "args": ["save"]}
        name = smart_name(action, "save", 1)
        assert "save" in name.lower() or name == "save"
    
    def test_smart_name_addnew(self):
        """新增动作命名"""
        action = {
            "methodName": "itemClick",
            "key": "toolbarap",
            "args": ["addnew"]
        }
        name = smart_name(action, "addnew", 1)
        assert "addnew" in name.lower() or "new" in name.lower()
    
    def test_smart_name_toolbar_click(self):
        """工具栏点击命名"""
        action = {
            "key": "toolbarap",
            "methodName": "itemClick",
            "args": ["custom_button"]
        }
        name = smart_name(action, "itemClick", 1)
        assert "custom" in name.lower() or "click" in name.lower()
    
    def test_smart_name_load_data(self):
        """加载数据命名"""
        action = {"_form_id": "test_form_detail"}
        name = smart_name(action, "loadData", 1)
        assert "load" in name.lower()
    
    def test_sanitize_chinese(self):
        """中文清理"""
        result = _sanitize("测试按钮")
        assert "测试" not in result  # 中文被清理
        assert isinstance(result, str)
    
    def test_sanitize_special_chars(self):
        """特殊字符清理"""
        result = _sanitize("test-name@#$%value")
        assert "@" not in result
        assert "$" not in result
        assert "%" not in result
    
    def test_sanitize_underscores(self):
        """连续下划线清理"""
        result = _sanitize("test___name___value")
        assert "___" not in result
        # 注意：可能有单个下划线
    
    def test_form_short_basic(self):
        """表单简称提取"""
        assert _form_short("haos_adminorgdetail") != ""
        assert _form_short("bos_portal_myapp_new") != ""
    
    def test_form_short_empty(self):
        """空表单ID"""
        assert _form_short("") == ""
    
    def test_extract_value_prefix_numeric(self):
        """数值前缀提取"""
        result = _extract_value_prefix("TEST12345")
        assert "TEST" in result.upper() or result == "QA"  # 根据具体实现


class TestACTier:
    """AC分级测试"""
    
    def test_core_actions(self):
        """核心动作"""
        core_actions = [
            "menuItemClick", "appItemClick", "treeMenuClick",
            "loadData", "addnew", "save", "saveandeffect",
            "submit", "submitandeffect", "delete", "modify", "close"
        ]
        for ac in core_actions:
            assert AC_TIER.get(ac) == "core", f"{ac} should be core"
    
    def test_noise_actions(self):
        """噪声动作"""
        noise_actions = ["clientCallBack", "queryExceedMaxCount", "customEvent"]
        for ac in noise_actions:
            assert AC_TIER.get(ac) == "noise", f"{ac} should be noise"
    
    def test_ui_reaction_actions(self):
        """UI联动动作"""
        ui_actions = ["getCityInfo", "getTelViaList", "getCountrys", "getLookUpList"]
        for ac in ui_actions:
            assert AC_TIER.get(ac) == "ui_reaction", f"{ac} should be ui_reaction"


class TestVariableDetection:
    """变量检测与占位符测试"""
    
    def test_detect_number_field(self):
        """编号字段检测"""
        actions = [{
            "methodName": "updateValue",
            "postData": [{}, [{"k": "number", "v": "TEST12345"}]]
        }]
        modified, vars_map = detect_var_placeholders(actions)
        assert "test_number" in vars_map
        assert "${rand:" in vars_map["test_number"]
    
    def test_detect_name_field(self):
        """名称字段检测"""
        actions = [{
            "methodName": "updateValue",
            "postData": [{}, [{"k": "name", "v": "测试员张三"}]]
        }]
        modified, vars_map = detect_var_placeholders(actions)
        assert "test_name" in vars_map
    
    def test_detect_phone_field(self):
        """电话字段检测"""
        actions = [{
            "methodName": "updateValue",
            "postData": [{}, [{"k": "phone", "v": "13800138000"}]]
        }]
        modified, vars_map = detect_var_placeholders(actions)
        assert "test_phone" in vars_map
    
    def test_detect_date_field(self):
        """日期字段检测"""
        actions = [{
            "methodName": "updateValue",
            "postData": [{}, [{"k": "effectdate", "v": "2026-04-28"}]]
        }]
        modified, vars_map = detect_var_placeholders(actions)
        # 检查日期是否被替换
        for e in actions[0]["postData"][1]:
            if isinstance(e, dict) and "v" in e:
                assert e["v"] == "${today}" or "today" in vars_map.values()
    
    def test_classify_key_number(self):
        """编号键分类"""
        assert _classify_key("number") == "number"
        assert _classify_key("code") == "number"
        assert _classify_key("billno") == "number"
    
    def test_classify_key_name(self):
        """名称键分类"""
        assert _classify_key("name") == "name"
        assert _classify_key("simplename") == "name"
        assert _classify_key("fullname") == "name"
    
    def test_classify_key_unique(self):
        """唯一键分类"""
        assert _classify_key("empnumber") == "number"
        assert _classify_key("phone") == "phone"


class TestStepDescription:
    """步骤描述生成测试"""
    
    def test_generate_open_form_description(self):
        """打开表单描述"""
        step = {"type": "open_form", "form_id": "haos_adminorgdetail"}
        desc = generate_step_description(step)
        assert "打开" in desc or "adminorg" in desc.lower()
    
    def test_generate_save_description(self):
        """保存描述"""
        step = {"type": "invoke", "ac": "save"}
        desc = generate_step_description(step)
        assert "保存" in desc or "save" in desc.lower()
    
    def test_generate_addnew_description(self):
        """新增描述"""
        step = {"type": "invoke", "ac": "addnew"}
        desc = generate_step_description(step)
        assert "新增" in desc or "new" in desc.lower()
    
    def test_generate_update_fields_description(self):
        """填写字段描述"""
        step = {"type": "update_fields", "fields": {"name": "测试"}}
        desc = generate_step_description(step)
        assert "填写" in desc or "字段" in desc
    
    def test_generate_pick_basedata_description(self):
        """选择基础资料描述"""
        step = {"type": "pick_basedata", "field_key": "adminorgtype"}
        desc = generate_step_description(step)
        assert "选择" in desc or "基础" in desc


class TestEdgeCases:
    """边界条件测试"""
    
    def test_is_business_request_empty_url(self):
        """空URL"""
        # 空字符串不包含任何静态后缀，但也不包含/form/
        assert is_business_request("") == False
    
    def test_smart_name_empty_action(self):
        """空动作"""
        name = smart_name({}, "unknown", 1)
        assert name != ""  # 应该有默认名称
    
    def test_sanitize_empty_string(self):
        """空字符串清理"""
        result = _sanitize("")
        assert result == ""
    
    def test_classify_key_empty(self):
        """空键分类"""
        result = _classify_key("")
        assert result is None
    
    def test_classify_key_unknown(self):
        """未知键分类"""
        result = _classify_key("unknown_field_xyz")
        assert result is None


# 运行测试命令：
# cd cosmic-replay-v4 && python -m pytest tests/unit/test_har_extractor.py -v
