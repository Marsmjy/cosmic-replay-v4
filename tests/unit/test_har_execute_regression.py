from scripts.har_execute_regression import _baseline_view, _classify_failure, _compare_baseline


def test_classify_failure_uses_runtime_trace_and_business_validation():
    backend = {
        "parse": {"status": "ok"},
        "execution": {"passed": False, "failed_steps": [{"id": "click_save", "error": "TraceId：abc java.lang.NullPointerException"}]},
    }
    required = {
        "parse": {"status": "ok"},
        "execution": {"passed": False, "failed_steps": [{"id": "click_submit", "error": "[Notification] 变动原因必填"}]},
    }
    save_blocked = {
        "parse": {"status": "ok"},
        "execution": {"passed": False, "failed_steps": [{"id": "click_save", "error": "请填写“岗位名称”。"}]},
    }

    assert _classify_failure(backend) == "backend_runtime_exception"
    assert _classify_failure(required) == "required_field_missing"
    assert _classify_failure(save_blocked) == "required_field_missing"


def test_baseline_view_keeps_value_safe_execution_shape():
    report = {
        "env": "uat",
        "har_dir": "/local/hars",
        "sample_count": 1,
        "parse_ok": 1,
        "exec_pass": 1,
        "exec_total": 1,
        "results": [
            {
                "id": "01_sample",
                "title": "样本",
                "har_sha256": "abc",
                "parse": {
                    "status": "ok",
                    "main_form_id": "form_a",
                    "step_count": 3,
                    "vars_count": 2,
                    "pick_fields_count": 1,
                    "field_catalog_count": 4,
                    "unknown_catalog_count": 0,
                    "business_flow_count": 1,
                    "response_signature_step_count": 1,
                },
                "execution": {
                    "status": "done",
                    "passed": True,
                    "failed_steps": [],
                    "write_events": [{"response_tokens": ["保存成功"]}],
                    "stdout_tail": "would contain values but must not be copied",
                },
                "failure_kind": "passed",
            }
        ],
    }

    baseline = _baseline_view(report)

    assert baseline["samples"][0]["passed"] is True
    assert baseline["samples"][0]["write_event_tokens"] == ["保存成功"]
    assert "stdout_tail" not in baseline["samples"][0]


def test_compare_baseline_flags_execution_regression():
    baseline = {
        "samples": [
            {"id": "01_sample", "passed": True, "failure_kind": "passed", "step_count": 3},
        ]
    }
    current = {
        "samples": [
            {"id": "01_sample", "passed": False, "failure_kind": "pageid_chain", "step_count": 3},
        ]
    }

    diff = _compare_baseline(baseline, current)

    assert diff["status"] == "changed"
    assert {item["path"] for item in diff["diffs"]} >= {"passed", "failure_kind"}
