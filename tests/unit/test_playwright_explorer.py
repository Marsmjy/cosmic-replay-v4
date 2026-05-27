from lib.playwright_explorer import (
    build_home_url,
    expand_menu_candidates,
    is_safe_menu_label,
    is_write_action_label,
    normalize_base_url,
    parse_cookie_header,
    redact_url,
    summarize_kingdee_request,
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
    assert not is_safe_menu_label("新增")
    assert not is_safe_menu_label("保存")
    assert not is_safe_menu_label("批量导入")
    assert is_write_action_label("提交并审核")


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
