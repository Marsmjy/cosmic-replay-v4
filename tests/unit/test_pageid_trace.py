from lib.pageid_trace import (
    annotate_pageid_recovery_strategies,
    annotate_recorded_pageid_sources,
    build_pageid_trace,
    classify_pageid,
    compact_pageid_trace,
    expected_pageid_role,
    finalize_recorded_pageid_source_retention,
    step_allows_l2_pageid,
    extract_response_pageid_producers,
)


def test_classify_pageid_layers():
    assert classify_pageid("root" + "a" * 32) == "L0"
    assert classify_pageid("1443450410974114816root" + "b" * 32) == "L2"
    assert classify_pageid("c" * 32) == "L1_or_L3"
    assert classify_pageid("cd0f5bfd-897c-443e-959c-2c61dc32b0cc") == "L1_or_L3"
    assert classify_pageid("") == "missing"


def test_step_l2_expectations_match_list_and_edit_semantics():
    assert step_allows_l2_pageid({
        "type": "invoke",
        "ac": "treeNodeClick",
        "method": "treeNodeClick",
    })
    assert expected_pageid_role({
        "type": "pick_basedata",
        "form_id": "haos_adminorgdetail",
    }) == "L3"
    assert expected_pageid_role({
        "type": "invoke",
        "ac": "loadData",
        "method": "loadData",
    }) == "L2_or_L3"
    assert expected_pageid_role({
        "type": "invoke",
        "ac": "save",
        "method": "itemClick",
        "preserve_l2_page": False,
        "key": "tbmain",
        "args": ["new_save", "save"],
    }) == "L3"


def test_build_pageid_trace_flags_missing_preserve_l2():
    case = {
        "steps": [
            {
                "id": "treeNodeClick_1",
                "type": "invoke",
                "form_id": "haos_adminorgdetail",
                "app_id": "haos",
                "ac": "treeNodeClick",
                "method": "treeNodeClick",
            }
        ]
    }
    har_steps = [
        {
            "id": "treeNodeClick_1",
            "_har_page_id": "1443450410974114816root" + "a" * 32,
        }
    ]

    trace = build_pageid_trace(case, har_steps=har_steps)

    assert trace["steps"][0]["har_pageid_type"] == "L2"
    assert "missing_preserve_l2_page" in trace["steps"][0]["risk_codes"]
    assert trace["summary"]["risk_counts"]["missing_preserve_l2_page"] == 1


def test_build_pageid_trace_merges_runtime_events_and_compacts_fragments():
    case = {
        "steps": [
            {
                "id": "fill_name",
                "type": "update_fields",
                "form_id": "demo_form",
                "app_id": "demo",
            }
        ]
    }
    events = [
        {
            "type": "pageid_trace",
            "data": {
                "step_id": "fill_name",
                "runtime_pageid_type": "L2",
                "runtime_pageid_fragment": "123root...abcd",
            },
        }
    ]

    trace = build_pageid_trace(case, run_events=events)
    compact = compact_pageid_trace(trace)

    assert trace["steps"][0]["runtime_pageid_type"] == "L2"
    assert "runtime_l2_used_for_l3_step" in trace["steps"][0]["risk_codes"]
    assert "runtime_pageid_fragment" not in compact["steps"][0]


def test_recorded_pageid_source_annotation_is_exact_and_value_safe():
    page_id = "c" * 32
    steps = [
        {
            "id": "open_detail",
            "type": "invoke",
            "form_id": "list_form",
            "app_id": "demo",
            "ac": "entryRowClick",
            "_har_index": 10,
            "_resp_text": (
                '[{"a":"showForm","p":[{"formId":"detail_form",'
                f'"pageId":"{page_id}"'
                "}]}]"
            ),
        },
        {
            "id": "load_detail",
            "type": "invoke",
            "form_id": "detail_form",
            "app_id": "demo",
            "ac": "loadData",
            "_har_index": 11,
            "_har_page_id": page_id,
        },
    ]

    annotate_recorded_pageid_sources(steps)
    finalize_recorded_pageid_source_retention(steps)
    trace = build_pageid_trace({"steps": steps})
    compact = compact_pageid_trace(trace)

    assert steps[1]["recorded_pageid_source_step_id"] == "open_detail"
    assert steps[1]["recorded_pageid_source_kind"] == "showForm"
    assert steps[1]["recorded_pageid_source_form_match"] is True
    assert steps[1]["recorded_pageid_source_retained"] is True
    assert trace["steps"][1]["recorded_pageid_source_step_id"] == "open_detail"
    assert page_id not in str(compact)


def test_recorded_pageid_source_uses_latest_reopen_of_same_pageid():
    page_id = "1443450410974114816root" + "a" * 32
    response = (
        '[{"a":"showForm","p":[{"formId":"list_form",'
        f'"pageId":"{page_id}"'
        "}]}]"
    )
    steps = [
        {
            "id": "open_list_first",
            "type": "invoke",
            "form_id": "portal",
            "app_id": "bos",
            "_har_index": 10,
            "_resp_text": response,
        },
        {
            "id": "open_list_again",
            "type": "invoke",
            "form_id": "portal",
            "app_id": "bos",
            "_har_index": 20,
            "_resp_text": response,
        },
        {
            "id": "search_list",
            "type": "invoke",
            "form_id": "list_form",
            "app_id": "demo",
            "_har_index": 21,
            "_har_page_id": page_id,
        },
    ]

    annotate_recorded_pageid_sources(steps)

    assert steps[2]["recorded_pageid_source_step_id"] == "open_list_again"
    assert steps[2]["recorded_pageid_source_har_index"] == 20


def test_response_pageid_producer_inherits_scoped_descendant_form_metadata():
    page_id = "hcdmroot" + "a" * 32
    producers = extract_response_pageid_producers([{
        "p": [{
            "pageId": page_id,
            "actions": [{
                "a": "activate",
                "p": [{
                    "formId": "hcdm_apphome",
                    "appId": "hcdm",
                }],
            }],
        }],
    }])

    producer = next(item for item in producers if item["page_id"] == page_id)

    assert producer["form_ids"] == ["hcdm_apphome"]
    assert producer["app_id"] == "hcdm"


def test_response_pageid_producer_ignores_unrelated_slide_form_metadata():
    page_id = "b" * 32
    producers = extract_response_pageid_producers([{
        "p": [{
            "pageId": page_id,
            "actions": [{
                "a": "setSlideBillFormId",
                "p": [{"formId": "hbp_reviselogpage"}],
            }],
        }],
        "a": "sendDynamicFormAction",
    }])

    producer = next(item for item in producers if item["page_id"] == page_id)

    assert producer["form_ids"] == []
    assert producer["app_id"] == ""


def test_recorded_pageid_source_marks_filtered_producer():
    page_id = "d" * 32
    steps = [
        {
            "id": "client_callback",
            "type": "invoke",
            "form_id": "home",
            "app_id": "demo",
            "ac": "clientCallBack",
            "_tier": "noise",
            "_har_index": 20,
            "_resp_text": (
                '[{"a":"showForm","p":[{"formId":"card_form",'
                f'"pageId":"{page_id}"'
                "}]}]"
            ),
        },
        {
            "id": "load_card",
            "type": "invoke",
            "form_id": "card_form",
            "app_id": "demo",
            "ac": "loadData",
            "_tier": "core",
            "_har_index": 21,
            "_har_page_id": page_id,
        },
    ]

    annotate_recorded_pageid_sources(steps)
    finalize_recorded_pageid_source_retention(steps, excluded_tiers={"noise"})

    assert steps[1]["recorded_pageid_source_retained"] is False


def test_filtered_l3_producer_enables_runtime_form_revalidation():
    steps = [
        {
            "id": "fill_name",
            "type": "update_fields",
            "form_id": "detail_form",
            "app_id": "demo",
            "recorded_pageid_source_retained": False,
            "recorded_pageid_type": "L1_or_L3",
        },
        {
            "id": "click_new",
            "type": "invoke",
            "form_id": "list_form",
            "app_id": "demo",
            "ac": "addnew",
            "recorded_pageid_source_retained": False,
            "recorded_pageid_type": "L2",
        },
    ]

    annotate_pageid_recovery_strategies(steps)

    assert steps[0]["pageid_recovery_strategy"] == "runtime_form_revalidate"
    assert steps[0]["force_pageid_validation"] is True
    assert steps[1]["pageid_recovery_strategy"] == "recorded_l2_context"
    assert not steps[1].get("force_pageid_validation")
