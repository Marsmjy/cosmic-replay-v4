import json

import pytest

from lib.har_regression import (
    DEFAULT_BASELINE_DIR,
    DEFAULT_MANIFEST,
    RegressionSample,
    compare_manifest,
    compare_snapshots,
    generate_snapshot,
    load_manifest,
    summarize_case,
    summarize_preview,
)


def test_summarize_case_redacts_business_values():
    case = {
        "name": "demo",
        "main_form_id": "demo_form",
        "vars": {
            "test_number": "SECRET${rand:6}",
            "test_description": "secret description",
        },
        "vars_labels": {"test_description": "描述"},
        "pick_fields": {
            "pick_org_id": {
                "value_id": "100000",
                "value_name": "敏感组织",
                "field_key": "org",
                "label": "组织",
                "form_id": "demo_form",
                "app_id": "demo",
                "env_sensitive": "high",
                "auto_resolve": True,
                "resolve_status": "pending",
            }
        },
        "steps": [
            {
                "id": "fill",
                "type": "update_fields",
                "form_id": "demo_form",
                "fields": {"name": {"zh_CN": "敏感名称"}},
            },
            {
                "id": "save",
                "type": "invoke",
                "form_id": "demo_form",
                "ac": "save",
                "method": "click",
                "post_data": [{}, [{"k": "description", "v": {"zh_CN": "secret description"}}]],
            },
        ],
        "assertions": [{"type": "no_save_failure", "step": "save"}],
    }

    summary = summarize_case(case)
    payload = json.dumps(summary, ensure_ascii=False)

    assert "SECRET" not in payload
    assert "secret description" not in payload
    assert "敏感组织" not in payload
    assert "敏感名称" not in payload
    assert summary["vars"][0]["value_shape"]["kind"] in {"literal", "template"}
    assert summary["pick_fields"][0]["value_id_shape"] == {"kind": "literal", "length": 6}


def test_compare_snapshots_classifies_main_form_change_as_breaking():
    baseline = {
        "sample_id": "demo",
        "case": {"main_form_id": "old", "steps": []},
        "preview": {},
    }
    current = {
        "sample_id": "demo",
        "case": {"main_form_id": "new", "steps": []},
        "preview": {},
    }

    report = compare_snapshots(baseline, current)

    assert report["changed"] is True
    assert report["impact_level"] == "breaking"
    assert report["diffs"][0]["path"] == "case.main_form_id"


def test_compare_snapshots_classifies_added_variable_as_review():
    baseline = {
        "sample_id": "demo",
        "case": {"vars": [{"name": "test_number"}]},
        "preview": {},
    }
    current = {
        "sample_id": "demo",
        "case": {"vars": [{"name": "test_number"}, {"name": "test_description"}]},
        "preview": {},
    }

    report = compare_snapshots(baseline, current)

    assert report["changed"] is True
    assert report["impact_level"] == "review"
    assert any(diff["path"] == "case.vars[test_description]" for diff in report["diffs"])


def test_summarize_preview_tracks_quality_metrics_without_values():
    preview = {
        "main_form_id": "demo_form",
        "quality": {"score": 90, "grade": "A", "blocking": False, "issues": []},
        "components": {"summary": {"total_steps": 3, "unsupported_steps": 0}},
        "detected_vars": [{"name": "test_name", "template": "敏感${rand:4}"}],
        "pick_fields": [
            {"field_key": "org", "auto_resolve": True, "env_sensitive": "high", "value_name": "敏感组织"},
        ],
        "business_flow": [{"step_count": 3}],
        "steps": [{"response_signature": {"label": "期望成功响应"}}],
        "pageid_alignment": {
            "grade": "A",
            "risk_level": "low",
            "issues": [{"code": "missing_preserve_l2_page"}],
            "checks": {"preview_l2_preserve_count": 1},
        },
        "ir_alignment": {
            "grade": "B",
            "risk_level": "medium",
            "issues": [{"code": "write_coverage_gap"}],
        },
        "ir_preview": {"warnings": [{"code": "dynamic_value_flow_warnings"}]},
    }

    summary = summarize_preview(preview)
    payload = json.dumps(summary, ensure_ascii=False)

    assert summary["maintainability"]["detected_var_count"] == 1
    assert summary["maintainability"]["pick_field_count"] == 1
    assert summary["maintainability"]["business_flow_count"] == 1
    assert summary["maintainability"]["response_signature_step_count"] == 1
    assert summary["pageid_alignment"]["issue_codes"] == ["missing_preserve_l2_page"]
    assert summary["ir_alignment"]["warning_codes"] == ["dynamic_value_flow_warnings"]
    assert "敏感组织" not in payload
    assert "敏感${rand:4}" not in payload


def test_manifest_baselines_match_local_hars_when_available():
    samples = load_manifest(DEFAULT_MANIFEST)
    missing = [sample.har_path for sample in samples if not sample.har_path.exists()]
    if missing:
        pytest.skip("local ignored HAR fixtures are not present")

    report = compare_manifest(DEFAULT_MANIFEST, DEFAULT_BASELINE_DIR)

    assert report["sample_count"] == len(samples)
    assert report["changed_count"] == 0, json.dumps(report, ensure_ascii=False, indent=2)


def test_enterprise_snapshot_tracks_description_variable_when_har_available():
    samples = {sample.id: sample for sample in load_manifest(DEFAULT_MANIFEST)}
    sample: RegressionSample = samples["06_enterprise"]
    if not sample.har_path.exists():
        pytest.skip("local ignored HAR fixture is not present")

    snapshot = generate_snapshot(sample)
    var_names = {item["name"] for item in snapshot["case"]["vars"]}

    assert "test_description" in var_names
    assert snapshot["case"]["main_form_id"] == "hbss_enterprise"
