import json

from lib.ir.dynamic_flow import build_dynamic_value_flow


def test_dynamic_value_flow_links_runtime_values_without_leaking_payload_values():
    case = {
        "name": "salary_audit",
        "steps": [
            {
                "id": "submit_bill",
                "type": "invoke",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
                "ac": "submit",
                "method": "submit",
                "key": "bar_submit",
            },
            {
                "id": "search_task",
                "type": "invoke",
                "form_id": "wf_task",
                "app_id": "bos",
                "ac": "commonSearch",
                "method": "commonSearch",
                "key": "filtercontainerap",
                "args": [[{"FieldName": ["billno"], "Value": ["DTX20260604001"]}]],
            },
            {
                "id": "choose_task",
                "type": "invoke",
                "form_id": "wf_task",
                "app_id": "bos",
                "ac": "entryRowClick",
                "method": "entryRowClick",
                "key": "billlistap",
                "post_data": [{"billlistap": {"selDatas": [["old_task", "DTX20260604001"]]}}],
            },
            {
                "id": "confirm_locked",
                "type": "invoke",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
                "ac": "afterConfirm",
                "method": "afterConfirm",
                "args": ["lockedConfirm", 6, '{"pkvalue":"old-pk"}'],
            },
            {
                "id": "attach_upload",
                "type": "invoke",
                "form_id": "hcdm_adjfileinfof7",
                "app_id": "hcdm",
                "ac": "upload",
                "method": "upload",
                "args": ["tempfile/download.do?configKey=tempfile.mock&id=old"],
            },
        ],
    }
    run_events = [
        {
            "type": "step_ok",
            "data": {
                "step_id": "submit_bill",
                "response": [
                    {"a": "u", "p": [{"k": "billno", "v": "DTX20260604999"}]},
                    {
                        "a": "showConfirm",
                        "p": [{
                            "id": "lockedConfirm",
                            "callbackValue": '{"pkvalue":"runtime-pk","billNo":"DTX20260604999"}',
                        }],
                    },
                ],
            },
        },
        {
            "type": "step_start",
            "data": {
                "step_id": "search_task",
                "resolved_request": {
                    "ac": "commonSearch",
                    "args": [[{"FieldName": ["billno"], "Value": ["DTX20260604999"]}]],
                },
            },
        },
        {
            "type": "step_ok",
            "data": {
                "step_id": "search_task",
                "response": [{
                    "a": "u",
                    "p": [{
                        "k": "billlistap",
                        "data": {
                            "dataindex": {"billno": 2, "wf_task_id": 14},
                            "rows": [["row", "task_id", "DTX20260604999"]],
                        },
                    }],
                }],
            },
        },
        {
            "type": "step_start",
            "data": {
                "step_id": "choose_task",
                "resolved_request": {
                    "ac": "entryRowClick",
                    "post_data": [{"billlistap": {"selDatas": [["task_id", "DTX20260604999"]]}}],
                },
            },
        },
        {
            "type": "step_start",
            "data": {
                "step_id": "confirm_locked",
                "resolved_request": {
                    "ac": "afterConfirm",
                    "args": ["lockedConfirm", 6, '{"pkvalue":"runtime-pk"}'],
                },
            },
        },
        {
            "type": "step_ok",
            "data": {
                "step_id": "prepare_upload",
                "response": {"url": "https://uat.example/tempfile/download.do?configKey=tempfile.mock&id=runtime"},
            },
        },
        {
            "type": "step_start",
            "data": {
                "step_id": "attach_upload",
                "resolved_request": {"url": "tempfile/download.do?configKey=tempfile.mock&id=runtime"},
            },
        },
    ]

    flow = build_dynamic_value_flow(case, run_events=run_events)
    payload = json.dumps(flow, ensure_ascii=False)

    assert flow["status"] == "ready"
    assert flow["raw_values_included"] is False
    assert flow["summary"]["value_kinds"]["billno"] >= 2
    assert flow["summary"]["value_kinds"]["confirm_callback"] >= 2
    assert flow["summary"]["value_kinds"]["task_row"] >= 2
    assert flow["summary"]["value_kinds"]["upload_url"] >= 2
    assert {
        ("billno", "submit_bill", "search_task"),
        ("confirm_callback", "submit_bill", "confirm_locked"),
        ("task_row", "search_task", "choose_task"),
        ("upload_url", "prepare_upload", "attach_upload"),
    }.issubset({
        (edge["kind"], edge["producer_step_id"], edge["consumer_step_id"])
        for edge in flow["edges"]
    })
    assert "DTX20260604999" not in payload
    assert "DTX20260604001" not in payload
    assert "runtime-pk" not in payload
    assert "tempfile.mock&id=runtime" not in payload


def test_dynamic_value_flow_warns_when_confirm_callback_has_no_runtime_producer():
    case = {
        "steps": [{
            "id": "confirm_locked",
            "type": "invoke",
            "form_id": "demo",
            "ac": "afterConfirm",
            "method": "afterConfirm",
            "args": ["lockedConfirm", 6, '{"pkvalue":"recorded"}'],
        }]
    }

    flow = build_dynamic_value_flow(case, run_events=[])

    assert any(
        warning["code"] == "dynamic_consumer_without_prior_producer"
        and warning["kind"] == "confirm_callback"
        for warning in flow["warnings"]
    )


def test_dynamic_value_flow_uses_recorded_exact_pageid_source_without_raw_value():
    case = {
        "steps": [
            {
                "id": "open_detail",
                "type": "invoke",
                "form_id": "list_form",
                "app_id": "demo",
                "ac": "entryRowClick",
            },
            {
                "id": "load_detail",
                "type": "invoke",
                "form_id": "detail_form",
                "app_id": "demo",
                "ac": "loadData",
                "recorded_pageid_type": "L1_or_L3",
                "recorded_pageid_source_step_id": "open_detail",
                "recorded_pageid_source_kind": "showForm",
                "recorded_pageid_source_retained": True,
            },
        ]
    }

    flow = build_dynamic_value_flow(case)

    assert {
        (edge["kind"], edge["producer_step_id"], edge["consumer_step_id"])
        for edge in flow["edges"]
    } >= {("page_id", "open_detail", "load_detail")}
    assert flow["raw_values_included"] is False


def test_dynamic_value_flow_links_upload_file_event_to_runtime_upload_consumer():
    case = {
        "steps": [
            {
                "id": "upload_1",
                "type": "upload_file",
                "upload_endpoint": "/tempfile/upload.do",
                "file_path": "/Users/demo/image.png",
            },
            {
                "id": "attach_commit",
                "type": "invoke",
                "form_id": "hcdm_adjfileinfof7",
                "app_id": "hcdm",
                "ac": "click",
                "method": "click",
                "args": ["tempfile/download.do?configKey=tempfile.mock&id=old"],
            },
        ],
    }
    run_events = [
        {
            "type": "upload_file_ok",
            "data": {
                "step_id": "upload_1",
                "upload_id": "upload_1",
                "kind": "upload_url",
            },
        },
        {
            "type": "runtime_upload_applied",
            "data": {
                "step_id": "attach_commit",
                "upload_id": "upload_1",
                "replacement_count": 1,
                "kind": "upload_url",
            },
        },
    ]

    flow = build_dynamic_value_flow(case, run_events=run_events)
    payload = json.dumps(flow, ensure_ascii=False)

    assert ("upload_url", "upload_1", "attach_commit") in {
        (edge["kind"], edge["producer_step_id"], edge["consumer_step_id"])
        for edge in flow["edges"]
    }
    assert "tempfile.mock&id=old" not in payload


def test_dynamic_value_flow_marks_repeated_polling_as_wait_until_candidate():
    case = {
        "steps": [
            {
                "id": f"poll_{idx}",
                "type": "invoke",
                "form_id": "upload_progress",
                "app_id": "bos",
                "ac": "getpercent",
                "method": "getpercent",
                "key": "progress",
            }
            for idx in range(4)
        ]
    }

    flow = build_dynamic_value_flow(case, run_events=[])

    assert any(warning["code"] == "polling_wait_until_candidate" for warning in flow["warnings"])


def test_dynamic_value_flow_links_wait_until_grid_row_to_runtime_billno():
    case = {
        "steps": [
            {
                "id": "submit_bill",
                "type": "invoke",
                "form_id": "khr_hcdm_fapplybill",
                "app_id": "hcdm",
                "ac": "submit",
                "method": "submit",
            },
            {
                "id": "wait_task_row",
                "type": "wait_until",
                "form_id": "wf_task",
                "app_id": "bos",
                "ac": "commonSearch",
                "method": "commonSearch",
                "condition": {
                    "kind": "grid_row_exists",
                    "grid_key": "billlistap",
                    "field_key": "billno",
                    "value": "DTX20260604999",
                },
            },
        ],
    }
    run_events = [{
        "type": "step_ok",
        "data": {
            "step_id": "submit_bill",
            "response": {"billno": "DTX20260604999"},
        },
    }]

    flow = build_dynamic_value_flow(case, run_events=run_events)

    assert flow["summary"]["value_kinds"]["billno"] >= 2
    assert flow["summary"]["value_kinds"]["task_row"] >= 1
    assert ("billno", "submit_bill", "wait_task_row") in {
        (edge["kind"], edge["producer_step_id"], edge["consumer_step_id"])
        for edge in flow["edges"]
    }


def test_dynamic_value_flow_links_pageid_by_form_scope_without_leaking_values():
    runtime_pid = "a" * 32
    case = {
        "steps": [
            {
                "id": "open_child",
                "type": "invoke",
                "form_id": "parent_form",
                "app_id": "demo",
                "ac": "addnew",
                "method": "addnew",
            },
            {
                "id": "fill_child",
                "type": "update_fields",
                "form_id": "child_form",
                "app_id": "demo",
                "fields": {"name": "自动化"},
            },
        ],
    }
    run_events = [
        {
            "type": "step_ok",
            "data": {
                "step_id": "open_child",
                "response": [{
                    "a": "showForm",
                    "p": [{
                        "formId": "child_shell",
                        "billFormId": "child_form",
                        "pageId": runtime_pid,
                    }],
                }],
            },
        },
        {
            "type": "pageid_trace",
            "data": {
                "step_id": "fill_child",
                "form_id": "child_form",
                "app_id": "demo",
                "expected_pageid_role": "L3",
                "runtime_pageid_type": "L1_or_L3",
                "phase": "after_handler",
                "status": "ok",
            },
        },
    ]

    flow = build_dynamic_value_flow(case, run_events=run_events)
    payload = json.dumps(flow, ensure_ascii=False)

    page_edges = [edge for edge in flow["edges"] if edge["kind"] == "page_id"]
    assert page_edges
    assert page_edges[0]["producer_step_id"] == "open_child"
    assert page_edges[0]["consumer_step_id"] == "fill_child"
    assert page_edges[0]["match_detail"]["form_scope_match"] is True
    assert "L1_or_L3" in page_edges[0]["match_detail"]["producer_pageid_types"]
    assert runtime_pid not in payload


def test_dynamic_value_flow_warns_pageid_role_mismatch_after_handler():
    case = {
        "steps": [{
            "id": "save_bill",
            "type": "invoke",
            "form_id": "demo_bill",
            "app_id": "demo",
            "ac": "save",
            "method": "save",
            "key": "bar_save",
        }]
    }
    run_events = [{
        "type": "pageid_trace",
        "data": {
            "step_id": "save_bill",
            "form_id": "demo_bill",
            "app_id": "demo",
            "expected_pageid_role": "L3",
            "runtime_pageid_type": "L2",
            "phase": "after_handler",
            "status": "fail",
        },
    }]

    flow = build_dynamic_value_flow(case, run_events=run_events)

    assert any(
        warning["code"] == "pageid_role_mismatch"
        and warning["consumer_step_id"] == "save_bill"
        for warning in flow["warnings"]
    )
