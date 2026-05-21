from lib.pageid_trace import (
    build_pageid_trace,
    classify_pageid,
    compact_pageid_trace,
    expected_pageid_role,
    step_allows_l2_pageid,
)


def test_classify_pageid_layers():
    assert classify_pageid("root" + "a" * 32) == "L0"
    assert classify_pageid("1443450410974114816root" + "b" * 32) == "L2"
    assert classify_pageid("c" * 32) == "L1_or_L3"
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
