import json
from types import SimpleNamespace

from scripts.har_execute_regression import (
    _baseline_view,
    _classify_failure,
    _compare_baseline,
    _detect_recorded_env,
    _write_markdown,
)


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
                    "recorded_pageid_exact_link_count": 3,
                    "recorded_pageid_external_root_count": 1,
                    "recorded_pageid_filtered_source_count": 1,
                    "recorded_pageid_cross_form_count": 0,
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
    assert baseline["samples"][0]["recorded_pageid_exact_link_count"] == 3
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


def test_detect_recorded_env_uses_har_host_and_base_path(tmp_path):
    har_path = tmp_path / "sample.har"
    har_path.write_text(
        json.dumps({
            "log": {
                "entries": [
                    {
                        "request": {
                            "url": (
                                "https://feature.kingdee.com:1026/"
                                "feature_sit_hrpro/form/batchInvokeAction.do"
                            ),
                        },
                    },
                    {
                        "request": {
                            "url": (
                                "https://feature.kingdee.com:1026/"
                                "feature_sit_hrpro/form/batchInvokeAction.do"
                            ),
                        },
                    },
                    {
                        "request": {
                            "url": "https://cdn.example.test/static/app.js",
                        },
                    },
                ],
            },
        }),
        encoding="utf-8",
    )
    envs = [
        SimpleNamespace(
            id="sit",
            name="SIT",
            base_url="https://feature.kingdee.com:1026/feature_sit_hrpro",
        ),
        SimpleNamespace(
            id="uat",
            name="UAT",
            base_url="http://kdhruat.kingdee.com:8022/ierp",
        ),
    ]

    detected = _detect_recorded_env(har_path, envs)

    assert detected["status"] == "matched"
    assert detected["env_id"] == "sit"
    assert detected["recorded_host"] == "feature.kingdee.com:1026"
    assert detected["recorded_base_path"] == "feature_sit_hrpro"


def test_markdown_labels_passed_non_blocking_failed_steps_as_diagnostics(tmp_path):
    report = {
        "generated_at": "2026-06-11T00:00:00",
        "env": "auto",
        "har_dir": "/local/hars",
        "sample_count": 1,
        "parse_ok": 1,
        "exec_pass": 1,
        "exec_total": 1,
        "first_success_verified": 0,
        "readback_verified": 0,
        "execution_duration_s": 1,
        "baseline_path": "/tmp/baseline.json",
        "results": [
            {
                "id": "01_sample",
                "title": "样本",
                "recorded_env": "sit",
                "recorded_host": "feature.kingdee.com:1026",
                "execution_env": "sit",
                "failure_kind": "passed",
                "parse": {"status": "ok", "main_form_id": "form_a", "step_count": 3},
                "execution": {
                    "status": "done",
                    "passed": True,
                    "duration_s": 1,
                    "failed_steps": [{"id": "advisory_lookup", "error": "optional dynamic row not found"}],
                },
            }
        ],
    }
    path = tmp_path / "summary.md"

    _write_markdown(path, report)

    text = path.read_text(encoding="utf-8")
    assert "非阻断诊断：`advisory_lookup`" in text
    assert "失败步骤：`advisory_lookup`" not in text
