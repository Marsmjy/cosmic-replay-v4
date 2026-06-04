"""
cosmic-replay v4 - Runner模块单元测试

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

from lib import runner as runner_mod
from lib.field_resolver import ResolveResult
from lib.runner import (
    load_yaml, _parse_yaml_light, resolve_vars, _resolve_str, _resolve_ref,
    STEP_HANDLERS, ASSERTION_HANDLERS, _auto_resolve_pick_basedata_step,
    _step_allows_l2_pageid, _case_targets_form_via_menu,
    _case_reaches_form_via_recorded_context,
    _claim_pending_pageid_for_form, _apply_pick_fields,
    _auto_resolve_selector_row_step, _bind_l2_targets_from_navigation_step,
    _build_env_fields, _build_env_resolution_plan, _resolve_selector_row_from_recent_grid,
    _build_selector_selected_row, _apply_runtime_billno_to_step,
)
from lib.replay import CosmicFormReplay, CosmicSession, has_error_action


def test_env_fields_display_business_code_and_keep_har_order():
    case = {
        "steps": [
            {
                "id": "entryRowClick_18",
                "type": "select_f7_list_row",
                "form_id": "hcdm_adjfileinfof7",
                "value_id": "2465334257644485632",
                "value_code": "00186-0001",
                "_env_field_id": "selector_salary_adjust_employee_id",
            },
            {
                "id": "pick_khr_salarylevel",
                "type": "pick_basedata",
                "form_id": "khr_hcdm_targetsalary",
                "field_key": "khr_salarylevel",
                "value_id": "2366111555608643584",
                "value_name": "低于宽带下限二档",
                "_env_field_id": "pick_khr_salarylevel_id",
            },
        ],
        "pick_fields": {
            "pick_khr_salarylevel_id": {
                "field_key": "khr_salarylevel",
                "label": "薪酬水平",
                "value_id": "PAY-XCSPDBKD-00001",
                "value_code": "PAY-XCSPDBKD-00001",
                "value_name": "低于宽带下限二档",
                "recorded_value_id": "2366111555608643584",
                "source_step_id": "pick_khr_salarylevel",
            },
            "selector_salary_adjust_employee_id": {
                "field_key": "employee_name",
                "label": "定调薪人员",
                "value_id": "00186-0001",
                "value_code": "00186-0001",
                "recorded_value_id": "2465334257644485632",
                "source_step_id": "entryRowClick_18",
            },
        },
    }
    result = runner_mod.RunResult()
    result.steps = [
        {"id": "pick_khr_salarylevel", "type": "pick_basedata", "ok": True},
        {"id": "entryRowClick_18", "type": "select_f7_list_row", "ok": True},
    ]

    fields = _build_env_fields(case, result)

    assert [item["step_id"] for item in fields] == [
        "selector_salary_adjust_employee_id",
        "pick_khr_salarylevel_id",
    ]
    assert fields[0]["display_value"] == "00186-0001"
    assert fields[1]["display_value"] == "PAY-XCSPDBKD-00001"


class SelectorParentLookupReplay:
    def invoke(self, form_id, app_id, ac, actions, page_id=None):
        assert (form_id, app_id, ac) == ("khr_hcdm_fapplybill", "khr", "getLookUpList")
        assert actions[0]["key"] == "khr_upperson"
        assert actions[0]["args"][0][1] == "53478"
        return [{
            "rows": [
                ["2381416858701015056", "53478", "赵月凛"],
            ],
            "dataindex": {"id": 0, "number": 1, "name": 2},
        }]


def test_selector_auto_resolve_uses_parent_field_lookup_for_entry_grid_f7():
    step = {
        "id": "entryRowClick_61",
        "type": "invoke",
        "form_id": "hrpi_employee",
        "app_id": "hrpi",
        "post_data": [{
            "billlistap": {
                "fieldKey": "name",
                "row": 1,
                "selRows": [1],
                "selDatas": [["2381390676873979991", "00002", "9289684"]],
            }
        }, []],
        "_selector_env_field_id": "selector_khr_upperson_id",
        "_selector_env_field_meta": {
            "field_key": "khr_upperson",
            "label": "薪酬直接上级",
            "value_id": "53478",
            "value_code": "53478",
            "value_name": "53478",
            "recorded_value_id": "2381390676873979991",
            "resolve_by": "value_code",
            "auto_resolve": True,
            "user_overridden": True,
            "selector_control_key": "billlistap",
            "selector_value_index": 0,
            "selector_code_index": 1,
            "selector_source": "entryRowClick",
            "parent_form_id": "khr_hcdm_fapplybill",
            "parent_field_key": "khr_upperson",
        },
    }
    ctx = {
        "env_resolution": {},
        "case": {"steps": [{"form_id": "khr_hcdm_fapplybill", "app_id": "khr"}]},
    }

    _auto_resolve_selector_row_step(step, SelectorParentLookupReplay(), ctx)

    payload = step["post_data"][0]["billlistap"]
    assert payload["selDatas"] == [["2381416858701015056", "53478", "赵月凛"]]
    resolved = ctx["env_resolution"]["selector_khr_upperson_id"]
    assert resolved["status"] == "resolved"
    assert resolved["field_key"] == "khr_upperson"
    assert resolved["control_key"] == "khr_upperson"
    assert resolved["value_id"] == "2381416858701015056"
    assert resolved["value_code"] == "53478"


def test_selector_selected_row_rebuilds_display_cell_from_matched_grid_row():
    compact = _build_selector_selected_row(
        ["2381390676873979991", "00002", "9289684"],
        [12, "赵月凛", "53478", "2381416858701015056"],
        {"rk": 0, "name": 1, "number": 2, "hrpi_employee_id": 3},
        {
            "selector_value_index": 0,
            "selector_code_index": 1,
        },
        "53478",
        form_id="hrpi_employee",
    )

    assert compact == ["2381416858701015056", "53478", "赵月凛"]


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


class TestReplayErrorDetection:
    """苍穹响应错误识别"""

    def test_env_resolution_plan_lists_lookup_and_selector_interfaces(self):
        plan = _build_env_resolution_plan({
            "pick_adminorg_id": {
                "field_key": "adminorg",
                "form_id": "demo_form",
                "app_id": "demo",
                "value_code": "ORG001",
                "resolve_by": "value_code",
                "auto_resolve": True,
            },
            "selector_salary_adjust_employee_id": {
                "field_key": "employee_name",
                "form_id": "hcdm_adjfileinfof7",
                "app_id": "hcdm",
                "value_code": "04041-0001",
                "resolve_by": "value_code",
                "auto_resolve": True,
                "selector_source": "entryRowClick",
                "selector_control_key": "billlistap",
            },
        })

        by_id = {item["step_id"]: item for item in plan}
        assert by_id["pick_adminorg_id"]["resolver_kind"] == "lookup"
        assert by_id["pick_adminorg_id"]["interface"] == "getLookUpList"
        assert by_id["pick_adminorg_id"]["query"] == "ORG001"
        selector = by_id["selector_salary_adjust_employee_id"]
        assert selector["resolver_kind"] == "grid_selector"
        assert selector["interface"] == "loadData"
        assert selector["control_key"] == "billlistap"
        assert selector["query"] == "04041-0001"

    def test_has_error_action_detects_nested_notification(self):
        resp = [{
            "a": "sendDynamicFormAction",
            "p": [{
                "pageId": "root123",
                "actions": [{
                    "a": "ShowNotificationMsg",
                    "p": [{
                        "type": 1,
                        "content": "无根组织，请先完成根组织初始化！",
                    }],
                }],
            }],
        }]
        assert has_error_action(resp) == ["[Notification] 无根组织，请先完成根组织初始化！"]

    def test_has_error_action_detects_invalid_request_dict(self):
        assert has_error_action({"msg": "无效请求"}) == ["[Protocol] 无效请求"]

    def test_has_error_action_allows_empty_dict_and_list(self):
        assert has_error_action({}) == []
        assert has_error_action([]) == []

    def test_expected_notification_assertion_accepts_recorded_business_validation(self):
        resp = [{
            "a": "ShowNotificationMsg",
            "p": [{
                "type": 1,
                "content": "请选择所属L1流程：ITM下的L2流程",
            }],
        }]
        ctx = {
            "step_responses": {"click_new_save": resp},
            "step_descriptions": {"click_new_save": "保存【行政组织详情】"},
        }

        ok, msg = ASSERTION_HANDLERS["expected_notification"](
            {
                "type": "expected_notification",
                "step": "click_new_save",
                "contains": "请选择所属L1流程：ITM下的L2流程",
            },
            ctx,
        )

        assert ok is True
        assert "预期业务校验提示" in msg

    def test_auto_resolve_keeps_business_code_display_but_uses_internal_id(self):
        class FakeReplay:
            def invoke(self, form_id, app_id, ac, actions, page_id=None):
                assert ac == "getLookUpList"
                return [{
                    "rows": [["2483502552415473664", "-260520-046", "Autotest组织"]],
                    "dataindex": {"id": 0, "number": 1, "name": 2},
                }]

        step = {
            "id": "pick_parentorg_ctx",
            "type": "pick_basedata",
            "form_id": "haos_adminorgdetail",
            "app_id": "haos",
            "field_key": "parentorg",
            "value_id": "-260520-046",
            "value_code": "-260520-046",
            "value_name": "Autotest组织",
            "auto_resolve": True,
            "resolve_by": "value_code",
        }

        _auto_resolve_pick_basedata_step(step, FakeReplay(), {"env_id": "uat"})

        assert step["value_id"] == "2483502552415473664"

    def test_pick_code_override_does_not_fall_back_to_recorded_id(self, monkeypatch):
        step = {
            "id": "pick_khr_proposer",
            "type": "pick_basedata",
            "form_id": "khr_hcdm_fapplybill",
            "app_id": "khr",
            "field_key": "khr_proposer",
            "value_id": "00001",
        }
        case = {
            "steps": [step],
            "pick_fields": {
                "pick_khr_proposer_id": {
                    "field_key": "khr_proposer",
                    "form_id": "khr_hcdm_fapplybill",
                    "app_id": "khr",
                    "source_step_id": "pick_khr_proposer",
                    "value_id": "00001",
                    "value_name": "7300166",
                    "value_code": "00002",
                    "value_number": "00001",
                    "recorded_value_id": "2381390676873980001",
                    "auto_resolve": True,
                    "resolve_by": "value_code",
                    "user_overridden": True,
                }
            },
        }

        _apply_pick_fields(case)

        assert step["value_id"] == "00002"
        assert step["value_code"] == "00002"

        class FakeResolver:
            def __init__(self, replay, env_id=""):
                pass

            def resolve_basedata_result(self, form_id, app_id, field_key, query, original_value_id=""):
                return ResolveResult(
                    status="not_found",
                    field_key=field_key,
                    query=query,
                    original_value_id=original_value_id,
                    message="候选项与 value_name 不匹配",
                )

        monkeypatch.setattr(runner_mod, "FieldResolver", FakeResolver)

        _auto_resolve_pick_basedata_step(step, object(), {"env_id": "uat"})

        assert step["value_id"] == "00002"

    def test_selector_env_field_uses_user_code_and_resolves_internal_id(self, monkeypatch):
        row = ["2381390967690242048", "", "", "012890005"]
        step = {
            "id": "entryRowClick_33",
            "type": "invoke",
            "form_id": "hsbs_empposf7querylist",
            "app_id": "hsbs",
            "ac": "entryRowClick",
            "post_data": [{"billlistap": {"selDatas": [row]}}],
        }
        case = {
            "steps": [step],
            "pick_fields": {
                "selector_employee_position_id": {
                    "field_key": "employee",
                    "form_id": "hsbs_empposf7querylist",
                    "app_id": "hsbs",
                    "source_step_id": "entryRowClick_33",
                    "value_id": "012890006",
                    "value_code": "012890005",
                    "value_name": "012890006",
                    "recorded_value_id": "2381390967690242048",
                    "auto_resolve": True,
                    "resolve_by": "value_code",
                    "selector_control_key": "billlistap",
                    "selector_value_index": 0,
                    "selector_code_index": 3,
                }
            },
        }

        _apply_pick_fields(case)

        assert row[0] == "2381390967690242048"
        assert row[3] == "012890006"

        queries = []

        class FakeResolver:
            def __init__(self, replay, env_id=""):
                pass

            def resolve_basedata_result(self, form_id, app_id, field_key, query, original_value_id=""):
                queries.append((form_id, app_id, field_key, query, original_value_id))
                return ResolveResult(
                    status="resolved",
                    field_key=field_key,
                    query=query,
                    original_value_id=original_value_id,
                    resolved_value_id="2381390967690242999",
                    resolved_value_name="012890006",
                    confidence="high",
                )

        monkeypatch.setattr(runner_mod, "FieldResolver", FakeResolver)

        _auto_resolve_selector_row_step(step, object(), {"env_id": "uat"})

        assert queries == [(
            "hsbs_empposf7querylist",
            "hsbs",
            "employee",
            "012890006",
            "2381390967690242048",
        )]
        assert row[0] == "2381390967690242999"
        assert row[3] == "012890006"

    def test_selector_code_override_does_not_fall_back_to_recorded_id_when_unresolved(self, monkeypatch):
        row = ["2381390967690242048", "", "", "012890005"]
        step = {
            "id": "entryRowClick_33",
            "type": "invoke",
            "form_id": "hsbs_empposf7querylist",
            "app_id": "hsbs",
            "ac": "entryRowClick",
            "post_data": [{"billlistap": {"selDatas": [row]}}],
        }
        case = {
            "steps": [step],
            "pick_fields": {
                "selector_employee_position_id": {
                    "field_key": "employee",
                    "form_id": "hsbs_empposf7querylist",
                    "app_id": "hsbs",
                    "source_step_id": "entryRowClick_33",
                    "value_id": "012890006",
                    "value_code": "012890006",
                    "value_name": "012890005",
                    "recorded_value_id": "2381390967690242048",
                    "auto_resolve": True,
                    "resolve_by": "value_code",
                    "user_overridden": True,
                    "selector_control_key": "billlistap",
                    "selector_value_index": 0,
                    "selector_code_index": 3,
                }
            },
        }

        _apply_pick_fields(case)

        class FakeResolver:
            def __init__(self, replay, env_id=""):
                pass

            def resolve_basedata_result(self, form_id, app_id, field_key, query, original_value_id=""):
                return ResolveResult(
                    status="not_found",
                    field_key=field_key,
                    query=query,
                    original_value_id=original_value_id,
                    message="候选项与 value_name 不匹配",
                )

        monkeypatch.setattr(runner_mod, "FieldResolver", FakeResolver)

        _auto_resolve_selector_row_step(step, object(), {"env_id": "uat"})

        assert row[0] == "012890006"
        assert row[3] == "012890006"

    def test_selector_code_override_rebuilds_row_from_recent_grid_response(self):
        recorded_row = ["2465334257644485632", "00186-0001", "100000", "00186-0001", "C"]
        selected_row = [
            4,
            5,
            "06019",
            "06019-0001",
            "7933263",
            "智慧科技事业部总经理",
            "060190005",
            "管理岗",
            "060190005",
            "金蝶国际软件集团有限公司",
            "主要任职",
            "中国",
            "金蝶信用科技（深圳）有限公司",
            "默认薪酬体系",
            "金蝶信科智慧科技事业部",
            False,
            "定调薪档案分组",
            "年薪制薪酬组成",
            ["2024-11-01", "2024-11-01 00:00:00"],
            ["2999-12-31", "2999-12-31 00:00:00"],
            "金蝶信科智慧科技事业部",
            "1",
            "杨春煦",
            ["2026-05-21 10:15:04", "2026-05-21 10:15:04"],
            False,
            "1",
            "杨春煦",
            ["2026-05-21 10:22:56", "2026-05-21 10:22:56"],
            "100000",
            "C",
            "2484119967973259264",
            {},
            {},
        ]
        step = {
            "id": "entryRowClick_18",
            "type": "invoke",
            "form_id": "hcdm_adjfileinfof7",
            "app_id": "hcdm",
            "ac": "entryRowClick",
            "key": "billlistap",
            "args": [1, "employee_name"],
            "post_data": [{
                "billlistap": {
                    "fieldKey": "employee_name",
                    "row": 1,
                    "selRows": [1],
                    "selDatas": [recorded_row],
                }
            }, []],
            "_selector_env_field_id": "selector_salary_adjust_employee_id",
            "_selector_env_field_meta": {
                "field_key": "employee_name",
                "form_id": "hcdm_adjfileinfof7",
                "app_id": "hcdm",
                "value_id": "00186-0001",
                "value_code": "06019-0001",
                "value_name": "00186-0001",
                "recorded_value_id": "2465334257644485632",
                "auto_resolve": True,
                "resolve_by": "value_code",
                "user_overridden": True,
                "selector_control_key": "billlistap",
                "selector_value_index": 0,
                "selector_code_index": 1,
            },
        }
        ctx = {
            "response_history": [{
                "a": "u",
                "p": [{
                    "k": "billlistap",
                    "data": {
                        "dataindex": {
                            "rk": 0,
                            "fseq": 1,
                            "employee_empnumber": 2,
                            "number": 3,
                            "employee_name": 4,
                            "hcdm_adjfileinfo_id": 30,
                        },
                        "rows": [selected_row],
                    },
                }],
            }],
        }

        _resolve_selector_row_from_recent_grid(step, ctx)

        payload = step["post_data"][0]["billlistap"]
        assert step["args"][0] == 4
        assert payload["row"] == 4
        assert payload["selRows"] == [4]
        assert payload["selDatas"] == [[
            "2484119967973259264",
            "06019-0001",
            "100000",
            "06019-0001",
            "C",
        ]]
        assert ctx["env_resolution"]["selector_salary_adjust_employee_id"]["resolver_kind"] == "grid_selector"
        assert ctx["env_resolution"]["selector_salary_adjust_employee_id"]["resolved_value_id"] == "2484119967973259264"

        class ExplodingResolver:
            def __init__(self, replay, env_id=""):
                pass

            def resolve_basedata_result(self, *args, **kwargs):
                raise AssertionError("selector resolved from grid should not call getLookUpList fallback")

        monkeypatch = pytest.MonkeyPatch()
        try:
            monkeypatch.setattr(runner_mod, "FieldResolver", ExplodingResolver)
            _auto_resolve_selector_row_step(step, object(), ctx)
        finally:
            monkeypatch.undo()

        assert payload["selDatas"] == [[
            "2484119967973259264",
            "06019-0001",
            "100000",
            "06019-0001",
            "C",
        ]]

    def test_runtime_billno_rewrites_task_search_and_selected_row(self):
        common_search = {
            "id": "commonSearch_96",
            "type": "invoke",
            "form_id": "wf_task",
            "app_id": "bos",
            "ac": "commonSearch",
            "key": "filtercontainerap",
            "method": "commonSearch",
            "args": [
                [{"FieldName": ["billno"], "Value": ["DTX20260604256"]}],
                [{"FieldName": ["createdate"], "Value": ["24"], "Compare": ["24"]}],
                "wf_task",
            ],
        }
        entry_click = {
            "id": "entryRowClick_97",
            "type": "invoke",
            "form_id": "wf_task",
            "app_id": "bos",
            "ac": "entryRowClick",
            "key": "billlistap",
            "method": "entryRowClick",
            "args": [0, "priorityshow"],
            "post_data": [{
                "billlistap": {
                    "fieldKey": "priorityshow",
                    "row": 0,
                    "selRows": [0],
                    "selDatas": [[
                        "2494284326619915265",
                        "DTX20260604256",
                        "请审批赵月凛发起的定调薪申请单（单据编号：DTX20260604256）",
                    ]],
                }
            }, []],
        }
        ctx = {
            "runtime_fields": {"billno": "DTX20260604269"},
            "response_history": [{
                "a": "u",
                "p": [{
                    "k": "gridview",
                    "data": {
                        "dataindex": {
                            "rk": 0,
                            "id": 1,
                            "billno": 2,
                            "subject": 6,
                            "wf_task_id": 14,
                        },
                        "rows": [[
                            0,
                            "2499999999999999999",
                            "DTX20260604269",
                            "员工定调薪申请单",
                            "赵月凛(53478)",
                            None,
                            "请审批赵月凛发起的定调薪申请单（单据编号：DTX20260604269）",
                            None,
                            "willApproval",
                            "一级审批人",
                            "赵月凛(53478)",
                            "",
                            ["2026-06-04 11:52:00", "2026-06-04 11:52:00"],
                            "",
                            "2499999999999999999",
                        ]],
                    },
                }],
            }],
        }

        _apply_runtime_billno_to_step(common_search, ctx)
        _apply_runtime_billno_to_step(entry_click, ctx)

        assert common_search["args"][0][0]["Value"] == ["DTX20260604269"]
        payload = entry_click["post_data"][0]["billlistap"]
        assert entry_click["args"][0] == 0
        assert payload["selRows"] == [0]
        assert payload["selDatas"] == [[
            "2499999999999999999",
            "DTX20260604269",
            "请审批赵月凛发起的定调薪申请单（单据编号：DTX20260604269）",
        ]]

    def test_select_f7_list_row_loads_selects_and_confirms(self):
        calls = []
        f7_row = [
            1, 2, "组织", "1020_S", "离职后补发补扣", "1", None, None,
            "1", "说明", True, "C", "1", "5", None, None, None, None,
            "100000", "1276916607024658432",
        ]

        class FakeReplay:
            def invoke(self, form_id, app_id, ac, actions):
                calls.append((form_id, app_id, ac, actions))
                if ac == "loadData":
                    return [{
                        "a": "u",
                        "p": [{
                            "k": "billlistap",
                            "data": {
                                "dataindex": {
                                    "rk": 0,
                                    "number": 3,
                                    "name": 4,
                                    "hsas_salarycalcstyle_id": 19,
                                },
                                "rows": [f7_row],
                            },
                        }],
                    }]
                if ac == "entryRowClick":
                    return [{"a": "InvokeControlMethod"}]
                return [{"a": "sendDynamicFormAction", "p": [{"pageId": "parent", "actions": []}]}]

        step = {
            "type": "select_f7_list_row",
            "form_id": "hsbp_allowreturnnullf7",
            "app_id": "hsas",
            "value_code": "1020_S",
            "field_key": "name",
        }

        resp = STEP_HANDLERS["select_f7_list_row"](step, FakeReplay(), {})

        assert resp[0]["a"] == "sendDynamicFormAction"
        assert [call[2] for call in calls] == ["loadData", "entryRowClick", "click"]
        select_action = calls[1][3][0]
        assert select_action["key"] == "billlistap"
        assert select_action["args"] == [1, "name"]
        payload = select_action["postData"][0]["billlistap"]
        assert payload["selRows"] == [1]
        assert payload["selDatas"] == [f7_row]
        assert calls[2][3][0]["key"] == "btnok"

    def test_pick_field_override_updates_select_f7_list_row(self):
        step = {
            "id": "select_salarycalcstyle_f7",
            "type": "select_f7_list_row",
            "form_id": "hsbp_allowreturnnullf7",
            "app_id": "hsas",
            "value_code": "1010_S",
            "value_name": "在职算薪",
        }
        case = {
            "steps": [step],
            "pick_fields": {
                "pick_salarycalcstyle_id": {
                    "field_key": "salarycalcstyle",
                    "form_id": "hsbp_allowreturnnullf7",
                    "source_step_id": "select_salarycalcstyle_f7",
                    "value_id": "1020_S",
                    "value_name": "离职后补发补扣",
                    "value_code": "1020_S",
                }
            },
        }

        _apply_pick_fields(case)

        assert step["value_code"] == "1020_S"
        assert step["value_name"] == "离职后补发补扣"

    def test_tree_node_click_preserves_l2_page_id(self):
        assert _step_allows_l2_pageid({
            "type": "invoke",
            "ac": "treeNodeClick",
            "method": "treeNodeClick",
        }) is True
        assert _step_allows_l2_pageid({
            "type": "update_fields",
            "ac": "updateValue",
            "method": "updateValue",
        }) is False
        assert _step_allows_l2_pageid({
            "type": "invoke",
            "ac": "save",
            "method": "itemClick",
            "key": "tbmain",
            "args": ["new_save", "save"],
        }) is False
        assert _step_allows_l2_pageid({
            "type": "invoke",
            "ac": "addnew",
            "method": "itemClick",
        }) is True

    def test_menu_targeted_main_form_is_not_preopened(self):
        case = {
            "main_form_id": "hpdi_bizdatabillnewentry",
            "steps": [{
                "type": "invoke",
                "ac": "menuItemClick",
                "target_form": "hpdi_bizdatabillnewentry",
                "target_forms": ["hpdi_bizdatabill"],
            }],
        }

        assert _case_targets_form_via_menu(case, "hpdi_bizdatabillnewentry") is True
        assert _case_targets_form_via_menu(case, "hpdi_bizdatabill") is True
        assert _case_targets_form_via_menu(case, "other_form") is False

    def test_context_reached_main_form_is_not_preopened(self):
        case = {
            "main_form_id": "hpdi_bizdatabillnewentry",
            "steps": [
                {
                    "type": "invoke",
                    "id": "click_btnok",
                    "form_id": "hsbs_empposf7querylist",
                    "app_id": "hsbs",
                    "ac": "click",
                    "key": "btnok",
                },
                {
                    "type": "invoke",
                    "id": "load_detail",
                    "form_id": "hpdi_bizdatabillnewentry",
                    "app_id": "hpdi",
                    "ac": "loadData",
                },
            ],
        }

        assert _case_reaches_form_via_recorded_context(case, "hpdi_bizdatabillnewentry") is True
        assert _case_reaches_form_via_recorded_context(case, "other_form") is False

    def test_pending_pageid_is_claimed_before_auto_opening_context_form(self):
        class FakeReplay:
            def __init__(self):
                self.page_ids = {}
                self._pending_by_app = {"hpdi": "abcdef0123456789abcdef0123456789"}

        replay = FakeReplay()

        assert _claim_pending_pageid_for_form(replay, "hpdi_bizdatabillnewentry", "hpdi") is True
        assert replay.page_ids["hpdi_bizdatabillnewentry"] == "abcdef0123456789abcdef0123456789"
        assert "hpdi" not in replay._pending_by_app

    def test_tree_menu_l2_binding_targets_business_form(self):
        class FakeSession:
            root_base_id = "0123456789abcdef0123456789abcdef"

        class FakeReplay:
            def __init__(self):
                self.s = FakeSession()
                self.page_ids = {}

        step = {
            "target_form": "haos_orgchangereason",
            "target_forms": ["haos_orgchangereason"],
        }
        replay = FakeReplay()

        pid = _bind_l2_targets_from_navigation_step(
            step,
            replay,
            {"main_form_id": "haos_orgchangereason"},
            "1655715311321754624",
        )

        assert pid == "1655715311321754624root0123456789abcdef0123456789abcdef"
        assert replay.page_ids["haos_orgchangereason"] == pid

    def test_tree_menu_l2_binding_keeps_existing_target_when_not_overwriting(self):
        class FakeSession:
            root_base_id = "0123456789abcdef0123456789abcdef"

        class FakeReplay:
            def __init__(self):
                self.s = FakeSession()
                self.page_ids = {"haos_orgchangereason": "existing-page"}

        step = {
            "target_form": "haos_orgchangereason",
            "target_forms": ["haos_orgchangereason"],
        }
        replay = FakeReplay()

        pid = _bind_l2_targets_from_navigation_step(
            step,
            replay,
            {"main_form_id": "haos_orgchangereason"},
            "1655715311321754624",
            overwrite=False,
        )

        assert pid == "1655715311321754624root0123456789abcdef0123456789abcdef"
        assert replay.page_ids["haos_orgchangereason"] == "existing-page"

    def test_show_form_harvest_binds_bill_form_id_alias(self):
        sess = CosmicSession(
            base_url="http://example.test",
            cookie="",
            user_id="",
            account_id="",
            csrf_token="",
            diff_time=0,
            root_base_id="",
            root_page_id="rootabcdef0123456789abcdef0123456789",
        )
        replay = CosmicFormReplay(sess)
        response = [{
            "a": "showForm",
            "p": [{
                "formId": "hsbs_employeequerylistf7",
                "billFormId": "hsbs_empposf7querylist",
                "pageId": "01179cbf5035422581622d93b880ebb8",
            }],
        }]

        replay._harvest_page_ids(response)

        assert replay.page_ids["hsbs_employeequerylistf7"] == "01179cbf5035422581622d93b880ebb8"
        assert replay.page_ids["hsbs_empposf7querylist"] == "01179cbf5035422581622d93b880ebb8"

    def test_show_form_harvest_accepts_reopened_loaded_dialog_pageid(self):
        sess = CosmicSession(
            base_url="http://example.test",
            cookie="",
            user_id="",
            account_id="",
            csrf_token="",
            diff_time=0,
            root_base_id="",
            root_page_id="rootabcdef0123456789abcdef0123456789",
        )
        replay = CosmicFormReplay(sess)
        replay.page_ids["hcdm_adjfileinfof7"] = "01179cbf5035422581622d93b880ebb8"
        replay._loaded_forms.add("hcdm_adjfileinfof7")
        replay._current_invoke_form = "khr_hcdm_fapplybill"

        replay._harvest_page_ids([{
            "a": "showForm",
            "p": [{
                "formId": "hcdm_adjfilelistf7",
                "billFormId": "hcdm_adjfileinfof7",
                "pageId": "11119cbf5035422581622d93b880ebb8",
            }],
        }])

        assert replay.page_ids["hcdm_adjfileinfof7"] == "11119cbf5035422581622d93b880ebb8"
        assert replay.page_ids["hcdm_adjfilelistf7"] == "11119cbf5035422581622d93b880ebb8"

        replay._harvest_page_ids([{
            "a": "showForm",
            "p": [{
                "formId": "hcdm_adjfileinfof7",
                "pageId": "11119cbf5035422581622d93b880ebb8",
            }],
        }])

        assert replay.page_ids["hcdm_adjfileinfof7"] == "11119cbf5035422581622d93b880ebb8"


class TestYAMLLightParsing:
    """轻量 YAML 解析测试"""

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

    def test_readback_by_business_key_handler_registered(self):
        assert "readback_by_business_key" in ASSERTION_HANDLERS
        assert callable(ASSERTION_HANDLERS["readback_by_business_key"])
    
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

    def test_no_error_actions_fail_on_invalid_request_dict(self):
        ctx = {
            "last_response": {"msg": "无效请求"},
            "last_step_response": {"msg": "无效请求"},
            "step_responses": {},
        }
        passed, msg = ASSERTION_HANDLERS["no_error_actions"](
            {"last_step": True}, ctx
        )
        assert passed is False
        assert "无效请求" in msg

    def test_no_save_failure_fails_on_invalid_request_dict(self):
        ctx = {
            "replay": object(),
            "step_responses": {"save": {"msg": "无效请求"}},
            "step_descriptions": {"save": "保存"},
        }
        passed, msg = ASSERTION_HANDLERS["no_save_failure"](
            {"step": "save"}, ctx
        )
        assert passed is False
        assert "无效请求" in msg
    
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

    def test_readback_by_business_key_uses_recorded_grid_response(self):
        ctx = {
            "step_responses": {
                "search_after_save": [{
                    "a": "u",
                    "p": [{
                        "k": "billlistap",
                        "data": {
                            "dataindex": {"rk": 0, "number": 1, "name": 2},
                            "rows": [[0, "CRPLY_001", "测试记录"]],
                        },
                    }],
                }],
            },
            "main_form_id": "demo_bill",
        }

        passed, msg = ASSERTION_HANDLERS["readback_by_business_key"]({
            "step": "search_after_save",
            "form_id": "demo_bill",
            "app_id": "demo",
            "field_key": "number",
            "value": "CRPLY_001",
        }, ctx)

        assert passed is True
        assert "入库回查通过" in msg

    def test_readback_by_business_key_executes_common_search_when_no_step(self):
        calls = []

        class FakeReplay:
            page_ids = {"demo_bill": "pid"}

            def invoke(self, form_id, app_id, ac, actions):
                calls.append((form_id, app_id, ac, actions))
                return [{
                    "a": "u",
                    "p": [{
                        "k": "billlistap",
                        "data": {
                            "dataindex": {"rk": 0, "number": 1},
                            "rows": [[0, "CRPLY_002"]],
                        },
                    }],
                }]

        ctx = {
            "replay": FakeReplay(),
            "step_responses": {},
            "case": {"main_form_id": "demo_bill"},
            "main_form_id": "demo_bill",
        }

        passed, msg = ASSERTION_HANDLERS["readback_by_business_key"]({
            "form_id": "demo_bill",
            "app_id": "demo",
            "field_key": "number",
            "value": "CRPLY_002",
        }, ctx)

        assert passed is True
        assert calls[0][2] == "commonSearch"
        assert calls[0][3][0]["key"] == "filtercontainerap"
        assert "入库回查通过" in msg

    def test_readback_by_business_key_fresh_menu_refresh_strategy(self, monkeypatch):
        calls = []

        class FakeSession:
            root_base_id = "a" * 32
            root_page_id = "root" + root_base_id

        class FakeReplay:
            def __init__(self, session, sign_required=True):
                self.s = session
                self.page_ids = {}

            def init_root(self):
                calls.append(("init_root",))
                return self.s.root_page_id

            def open_portal(self, form_id, app_id, lazy=True):
                calls.append(("open_portal", form_id, app_id, lazy))
                self.page_ids[form_id] = "portalpid"
                return "portalpid"

            def l2_page_id(self, menu_id):
                return f"{menu_id}root{self.s.root_base_id}"

            def invoke(self, form_id, app_id, ac, actions, page_id=None):
                calls.append((form_id, app_id, ac, page_id, actions))
                if ac == "refresh":
                    return [{
                        "a": "u",
                        "p": [{
                            "k": "billlistap",
                            "data": {
                                "dataindex": {"rk": 0, "khr_name": 1},
                                "rows": [[0, "自动化1234"]],
                            },
                        }],
                    }]
                return []

            def close(self):
                calls.append(("close",))

        monkeypatch.setattr(runner_mod, "login", lambda *args, **kwargs: FakeSession())
        monkeypatch.setattr(runner_mod, "CosmicFormReplay", FakeReplay)
        ctx = {
            "step_responses": {},
            "case": {"sign_required": True, "steps": []},
            "env": {
                "base_url": "https://example.test",
                "username": "user",
                "password": "pw",
                "datacenter_id": "dc",
            },
            "main_form_id": "khr_hcdm_fapplybill",
        }

        passed, msg = ASSERTION_HANDLERS["readback_by_business_key"]({
            "strategy": "fresh_menu_refresh",
            "menu_id": "2371045759278662656",
            "form_id": "khr_hcdm_fapplybill",
            "app_id": "hcdm",
            "field_key": "khr_name",
            "value": "自动化1234",
        }, ctx)

        assert passed is True
        assert "新会话菜单刷新" in msg
        assert any(call[2] == "refresh" for call in calls if len(call) > 2)

    def test_advisory_assertion_failure_does_not_fail_run_result(self):
        result = runner_mod.RunResult()
        result.steps.append({"id": "save", "ok": True, "type": "invoke"})
        result.assertions.append({
            "type": "readback_by_business_key",
            "ok": False,
            "advisory": True,
            "msg": "通用 commonSearch 未命中",
        })

        assert result.passed is True

    def test_assertion_is_advisory_from_mode(self):
        assert runner_mod._assertion_is_advisory({"mode": "advisory"}) is True
        assert runner_mod._assertion_is_advisory({"mode": "strict"}) is False
    
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
# cd cosmic-replay-v4 && python -m pytest tests/unit/test_runner.py -v
