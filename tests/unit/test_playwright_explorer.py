import json
from pathlib import Path

from lib.playwright_explorer import (
    DiscoveryReport,
    ExplorerConfig,
    build_home_url,
    expand_menu_candidates,
    infer_pageid_context_role,
    is_safe_menu_label,
    is_write_action_label,
    keyword_variants,
    normalize_base_url,
    parse_cookie_header,
    redact_url,
    risk_level_for_label,
    summarize_har_file,
    summarize_kingdee_request,
    summarize_menu_tree,
)
from scripts.playwright_discover import _parse_menu_samples
from scripts.write_smoke_run import (
    CONFIRM_TOKEN,
    apply_disabled_steps,
    apply_optional_steps,
    apply_pick_overrides,
    apply_prefetch_pick_steps,
    build_safe_summary,
    parse_pick_override,
    parse_var_override,
)
from lib.playwright_deep_actions import (
    WORKFLOW_CONFIRM_TOKEN,
    WRITE_CONFIRM_TOKEN,
    classify_action_risk,
    render_action_value,
    validate_action_plan,
)


def test_normalize_base_url_keeps_app_path_and_strips_query():
    assert (
        normalize_base_url("https://feature.kingdee.com:1026/feature_sit_hrpro/?formId=home_page")
        == "https://feature.kingdee.com:1026/feature_sit_hrpro"
    )
    assert (
        build_home_url("https://feature.kingdee.com:1026/feature_sit_hrpro/?formId=home_page")
        == "https://feature.kingdee.com:1026/feature_sit_hrpro/?formId=home_page"
    )


def test_safe_menu_classifier_blocks_write_actions():
    assert is_safe_menu_label("行政组织维护")
    assert is_safe_menu_label("人员信息查询")
    assert is_safe_menu_label("薪酬福利云")
    assert is_safe_menu_label("薪资核算")
    assert is_safe_menu_label("社保公积金")
    assert is_safe_menu_label("工资条")
    assert not is_safe_menu_label("新增")
    assert not is_safe_menu_label("保存")
    assert not is_safe_menu_label("批量导入")
    assert not is_safe_menu_label("开发者门户")
    assert not is_safe_menu_label("查询我的登录历史忽略")
    assert is_write_action_label("提交并审核")
    assert risk_level_for_label("保存") == "high"
    assert risk_level_for_label("薪资计算") == "medium"
    assert risk_level_for_label("薪资项目维护") == "low"


def test_expand_menu_candidates_splits_compound_home_block():
    items = expand_menu_candidates(
        [
            {
                "text": "快速发起 出差申请 差旅报销 开发者门户 跨环境传输中心",
                "tag": "div",
                "role": "",
                "className": "menu",
            }
        ]
    )

    labels = {item["text"] for item in items}
    assert "出差申请" in labels
    assert "差旅报销" in labels
    assert "开发者门户" not in labels
    assert "快速发起" not in labels


def test_parse_cookie_header_for_playwright_context():
    cookies = parse_cookie_header("sid=abc; kd_csrf_token=t", "https://feature.kingdee.com:1026/feature_sit_hrpro")

    assert cookies[0]["name"] == "sid"
    assert cookies[0]["domain"] == "feature.kingdee.com"
    assert cookies[0]["secure"] is True
    assert cookies[1]["name"] == "kd_csrf_token"


def test_redact_url_removes_query_and_fragment():
    assert (
        redact_url("https://feature.kingdee.com:1026/feature_sit_hrpro/form/batchInvokeAction.do?pageId=abc#x")
        == "https://feature.kingdee.com:1026/feature_sit_hrpro/form/batchInvokeAction.do"
    )


def test_summarize_kingdee_request_extracts_protocol_hints():
    summary = summarize_kingdee_request(
        "https://feature.kingdee.com:1026/feature_sit_hrpro/form/batchInvokeAction.do?appId=hr&f=form_a&ac=loadData",
        "pageId=123rootabcdefabcdefabcdefabcdefabcdefab&method=itemClick",
    )

    assert summary["app_id"] == "hr"
    assert summary["form_id"] == "form_a"
    assert summary["ac"] == "loadData"
    assert summary["invoke_method"] == "itemClick"
    assert summary["pageid_type"] == "L2"
    assert summary["pageid_fragment"].startswith("123root")


def test_infer_pageid_context_role():
    assert infer_pageid_context_role("loadData", "") == "L2_context"
    assert infer_pageid_context_role("save", "") == "L3_write"
    assert infer_pageid_context_role("click", "") == "L3_or_ui_action"
    assert infer_pageid_context_role("getMenuData", "") == "app_menu_catalog"
    assert infer_pageid_context_role("selectTab", "") == "portal_callback"


def test_keyword_variants_for_salary_cloud_search():
    assert keyword_variants("薪酬福利云") == ["薪酬福利云", "薪酬福利", "薪酬", "薪资", "福利"]
    assert keyword_variants("业务数据提报") == ["业务数据提报", "提报"]


def test_discovery_report_serializes_subapp_explorations():
    cfg = ExplorerConfig(base_url="https://example.test/ierp", drilldown_apps=["薪酬管理"])
    report = DiscoveryReport(base_url=cfg.base_url, home_url="https://example.test/ierp/?formId=home_page", datacenter_id="dc")
    report.subapp_explorations.append({"app_label": "薪酬管理", "menu_candidates": [{"text": "薪酬设计"}]})

    data = report.to_dict()

    assert cfg.drilldown_apps == ["薪酬管理"]
    assert data["subapp_explorations"][0]["app_label"] == "薪酬管理"


def test_parse_menu_samples_cli_shape():
    assert _parse_menu_samples("薪资数据集成:业务数据提报,薪资核算:计薪人员") == [
        {"app_label": "薪资数据集成", "menu_label": "业务数据提报"},
        {"app_label": "薪资核算", "menu_label": "计薪人员"},
    ]


def test_deep_action_plan_requires_explicit_write_and_workflow_tokens():
    plan = {
        "owned_test_data": True,
        "test_prefix": "CRPLY_DEEP",
        "actions": [
            {"type": "click_text", "text": "新增"},
            {"type": "fill_text", "label": "名称", "value": "CRPLY_DEEP_001"},
            {"type": "click_text", "text": "提交"},
            {"type": "click_text", "text": "删除"},
        ],
    }

    assert classify_action_risk({"type": "click_text", "text": "查询"}) == "read"
    assert classify_action_risk({"type": "click_text", "text": "保存"}) == "write"
    assert classify_action_risk({"type": "click_text", "text": "审核"}) == "workflow"
    assert classify_action_risk({"type": "click_text", "text": "删除"}) == "forbidden"

    blocked = validate_action_plan(plan)
    assert blocked["ok"] is False
    assert any("write_requires" in item for item in blocked["errors"])
    assert any("workflow_requires" in item for item in blocked["errors"])
    assert any("forbidden_action" in item for item in blocked["errors"])

    workflow_only = {
        "owned_test_data": True,
        "test_prefix": "CRPLY_DEEP",
        "actions": [{"type": "click_text", "text": "提交"}],
    }
    allowed = validate_action_plan(
        workflow_only,
        confirm_write=WRITE_CONFIRM_TOKEN,
        confirm_workflow=WORKFLOW_CONFIRM_TOKEN,
    )
    assert allowed["ok"] is True
    assert allowed["summary"]["risk_counts"] == {"workflow": 1}


def test_deep_action_plan_selector_actions_are_guarded_and_templates_render():
    assert classify_action_risk({"type": "click_selector", "selector": ".kd-btn-primary"}) == "write"
    assert classify_action_risk({"type": "click_selector", "selector": ".kd-tab", "risk": "read"}) == "read"
    assert classify_action_risk({"type": "click_at", "x": 10, "y": 20}) == "write"
    assert classify_action_risk({"type": "fill_at", "x": 10, "y": 20, "value": "CRPLY"}) == "write"
    assert classify_action_risk({"type": "press", "selector": "input", "key": "Enter"}) == "write"
    assert classify_action_risk({"type": "wait_for_selector", "selector": ".kd-dialog"}) == "read"
    assert classify_action_risk({"type": "snapshot_controls", "selector": "body"}) == "read"

    blocked = validate_action_plan({
        "owned_test_data": True,
        "test_prefix": "CRPLY_DEEP",
        "actions": [{"type": "click_selector", "selector": ".kd-btn-primary"}],
    })
    assert blocked["ok"] is False
    assert any("write_requires" in item for item in blocked["errors"])

    rendered = render_action_value("CRPLY_${today}_${timestamp}_${rand:4}")
    assert rendered.startswith("CRPLY_")
    assert "${" not in rendered


def test_write_smoke_helpers_are_safe_and_summary_focused():
    assert CONFIRM_TOKEN == "YES_GENERATE_TEST_DATA"
    assert parse_var_override("test_description=CRPLY") == ("test_description", "CRPLY")
    assert parse_pick_override("pick_org_id=00|环宇国际集团有限公司") == ("pick_org_id", "00", "环宇国际集团有限公司")
    case = {"pick_fields": {"pick_org_id": {"field_key": "org", "auto_resolve": True}}}
    apply_pick_overrides(case, [("pick_org_id", "00", "环宇国际集团有限公司")])
    assert case["pick_fields"]["pick_org_id"]["value_id"] == "00"
    assert case["pick_fields"]["pick_org_id"]["auto_resolve"] is False
    case_steps = {"steps": [{"id": "pick_org_ctx"}, {"id": "save"}]}
    apply_optional_steps(case_steps, ["pick_org_ctx"])
    assert case_steps["steps"][0]["optional"] is True
    assert "optional" not in case_steps["steps"][1]
    disabled_case = {"steps": [{"id": "pick_org_ctx"}, {"id": "save"}]}
    apply_disabled_steps(disabled_case, ["pick_org_ctx"])
    assert [step["id"] for step in disabled_case["steps"]] == ["save"]
    assert disabled_case["write_smoke_disabled_steps"] == ["pick_org_ctx"]
    prefetch_case = {"steps": [{"id": "pick_bizitemgroup", "type": "pick_basedata"}]}
    apply_prefetch_pick_steps(prefetch_case, ["pick_bizitemgroup"])
    assert prefetch_case["steps"][0]["prefetch_lookup"] is True
    assert prefetch_case["steps"][0]["prefetch_lookup_args"] == [["%", "", "%", 0, 20, 0]]
    summary = build_safe_summary(
        {
            "name": "demo",
            "main_form_id": "demo_form",
            "vars": {"test_description": "CRPLY"},
            "steps": [{
                "id": "click_bar_save",
                "type": "invoke",
                "ac": "save",
                "key": "tbmain",
                "args": ["bar_save", "save"],
            }],
        },
        [
            {"event": "step_start", "payload": {"id": "save"}},
            {"event": "step_ok", "payload": {"id": "click_bar_save", "response": {"msg": "保存成功"}}},
            {"event": "step_ok", "payload": {"id": "load", "pageid_trace": {"runtime_pageid_type": "L2"}}},
        ],
        True,
        1.23,
    )

    assert summary["passed"] is True
    assert summary["write_events"][0]["step_id"] == "click_bar_save"
    assert summary["write_events"][0]["response_tokens"] == ["保存成功"]


def test_salary_cloud_catalog_fixture_is_value_safe():
    path = Path("tests/fixtures/playwright_discovery/salary_cloud_menu_catalog.json")
    data = json.loads(path.read_text(encoding="utf-8"))
    raw = path.read_text(encoding="utf-8")

    assert data["target_cloud"] == "薪酬福利云"
    assert len(data["subapps"]) == 9
    assert len(data["menu_samples"]) == 2
    assert data["network_summary"]["ac_counts"]["getMenuData"] == 9
    assert "cookie" not in raw.lower()
    assert "token" not in raw.lower()
    assert "http" not in raw.lower()


def test_salary_calc_scene_snapshot_plan_is_readonly_after_opening_new_page():
    path = Path("tests/fixtures/deep_chain_factory/salary_calc_scene_common_filter_snapshot_plan.json")
    plan = json.loads(path.read_text(encoding="utf-8"))

    result = validate_action_plan(plan, confirm_write=WRITE_CONFIRM_TOKEN)

    assert result["ok"] is True
    assert result["summary"]["risk_counts"] == {"read": 5, "write": 1}
    assert "CRPLY_" in plan["test_prefix"]


def test_summarize_menu_tree_uses_first_network_context():
    from lib.playwright_explorer import NetworkEvent

    rows = summarize_menu_tree(
        [{"text": "薪资项目维护", "tag": "div", "role": "", "className": ""}],
        [NetworkEvent(url="https://example.test/form/batchInvokeAction.do", app_id="swc", form_id="swc_demo", ac="loadData", pageid_type="L0")],
        app_name="薪酬福利云",
        url="https://example.test/",
    )

    assert rows[0]["menu_text"] == "薪资项目维护"
    assert rows[0]["app_name"] == "薪酬福利云"
    assert rows[0]["form_id"] == "swc_demo"
    assert rows[0]["risk_level"] == "low"


def test_summarize_har_file_extracts_value_safe_pageid_trace(tmp_path):
    har = {
        "log": {
            "entries": [
                {
                    "request": {
                        "method": "POST",
                        "url": "https://feature.kingdee.com:1026/feature_sit_hrpro/form/batchInvokeAction.do?appId=swc&f=swc_demo&ac=loadData",
                        "postData": {"text": "pageId=123rootabcdefabcdefabcdefabcdefabcdefab&method=itemClick"},
                    },
                    "response": {"status": 200},
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://feature.kingdee.com:1026/feature_sit_hrpro/form/batchInvokeAction.do?appId=swc&f=swc_demo&ac=save",
                        "postData": {"params": [{"name": "pageId", "value": "abcdefabcdefabcdefabcdefabcdefab"}]},
                    },
                    "response": {"status": 200},
                },
            ]
        }
    }
    path = tmp_path / "demo.har"
    path.write_text(__import__("json").dumps(har), encoding="utf-8")

    summary = summarize_har_file(path)

    assert summary["kingdee_event_count"] == 2
    assert summary["ac_counts"] == {"loadData": 1, "save": 1}
    assert summary["pageid_trace"][0]["url"].endswith("/form/batchInvokeAction.do")
    assert "pageId=" not in summary["pageid_trace"][0]["url"]
    assert summary["pageid_trace"][0]["expected_pageid_role"] == "L2_context"
    assert summary["pageid_trace"][1]["expected_pageid_role"] == "L3_write"
