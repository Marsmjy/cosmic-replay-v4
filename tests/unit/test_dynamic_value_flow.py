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
