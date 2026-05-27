from lib.playwright_explorer import (
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
    assert "开发者门户" in labels
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


def test_keyword_variants_for_salary_cloud_search():
    assert keyword_variants("薪酬福利云") == ["薪酬福利云", "薪酬福利", "薪酬", "薪资", "福利"]
    assert keyword_variants("业务数据提报") == ["业务数据提报", "提报"]


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
