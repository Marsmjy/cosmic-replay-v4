import json
import subprocess
import sys
from pathlib import Path

import yaml

from lib.har_extractor import build_yaml_case, preview_har
from lib.ir import assess_ir_preview_alignment, build_normalized_flow, compact_flow_for_preview
from lib.ir.dry_run import dry_run_flow, dry_run_yaml_case
from lib.ir.normalizer import normalize_har_entries
from lib.ir.sanitizer import sanitize_har, scan_sensitive_text
from lib.ir.yaml_generator import generate_yaml_case_from_ir


PAGE_ID = "123root0123456789abcdef0123456789abcdef"
EDIT_PAGE_ID = "abcdef0123456789abcdef0123456789"


def _synthetic_har() -> dict:
    actions = [{
        "key": "tbmain",
        "methodName": "click",
        "args": ["bar_save", "save"],
        "postData": [
            {"number": {"fieldKey": "number"}},
            [{"k": "number", "v": "CRPLY_001", "r": -1}],
        ],
    }]
    return {
        "log": {
            "version": "1.2",
            "entries": [
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.invalid/ierp/form/batchInvokeAction.do?appId=demo&f=demo_form&ac=save",
                        "headers": [
                            {"name": "Cookie", "value": "SESSION=secret"},
                            {"name": "Authorization", "value": "Bearer secret-token"},
                            {"name": "kd-csrf-token", "value": "secret-csrf"},
                        ],
                        "postData": {
                            "mimeType": "application/x-www-form-urlencoded",
                            "text": "pageId=" + EDIT_PAGE_ID + "&actions=" + json.dumps(actions),
                        },
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "text": json.dumps([
                                {
                                    "a": "sendDynamicFormAction",
                                    "p": [{
                                        "pageId": EDIT_PAGE_ID,
                                        "actions": [{"a": "ShowNotificationMsg", "p": [{"content": "保存成功。"}]}],
                                    }],
                                }
                            ], ensure_ascii=False),
                        },
                    },
                },
                {
                    "request": {
                        "method": "GET",
                        "url": "https://example.invalid/static/app.js",
                        "headers": [],
                    },
                    "response": {"status": 200, "content": {"text": "console.log('noise')"}},
                },
            ],
        }
    }


def test_sanitize_har_redacts_secret_headers_and_pageids():
    sanitized, redactions = sanitize_har(_synthetic_har())
    payload = json.dumps(sanitized, ensure_ascii=False)

    assert "secret-token" not in payload
    assert EDIT_PAGE_ID not in payload
    assert "${SESSION_COOKIE}" in payload
    assert "${PAGE_ID}" in payload
    assert any(item["type"] == "authorization" for item in redactions)


def test_normalize_har_entries_keeps_value_safe_api_shape():
    normalized = normalize_har_entries(_synthetic_har())

    assert normalized["entry_count"] == 2
    assert normalized["api_entry_count"] == 1
    entry = normalized["entries"][0]
    assert entry["url_shape"] == "/form/batchInvokeAction.do"
    assert entry["signals"]["form_id"] == "demo_form"
    assert entry["signals"]["app_id"] == "demo"
    assert entry["signals"]["ac"] == "save"
    assert entry["signals"]["pageid_type"] == "L1_or_L3"
    assert entry["request"]["headers"]["Cookie"] == "${SESSION_COOKIE}"


def test_build_normalized_flow_and_preview_are_redacted():
    flow = build_normalized_flow(_synthetic_har(), source_name="synthetic.har")
    preview = compact_flow_for_preview(flow)
    payload = json.dumps(flow, ensure_ascii=False)

    assert flow["source_har"]["api_entry_count"] == 1
    assert flow["steps"][0]["role"] == "write"
    assert flow["assertions"][0]["type"] == "no_save_failure"
    assert preview["source_har"]["redacted"] is True
    assert "secret-token" not in payload
    assert EDIT_PAGE_ID not in payload
    assert scan_sensitive_text(payload) == []


def test_ir_alignment_detects_missing_write_coverage():
    flow = build_normalized_flow(_synthetic_har(), source_name="synthetic.har")
    result = assess_ir_preview_alignment(
        flow,
        preview_steps=[{"id": "open", "type": "invoke", "ac": "loadData"}],
        detected_vars=[],
        pick_fields=[],
    )

    assert result["risk_level"] == "high"
    assert result["checks"]["ir_role_counts"]["write"] == 1
    assert any(issue["code"] == "write_step_not_covered" for issue in result["issues"])


def test_ir_alignment_scores_matching_write_coverage_as_low_risk():
    flow = build_normalized_flow(_synthetic_har(), source_name="synthetic.har")
    result = assess_ir_preview_alignment(
        flow,
        preview_steps=[{"id": "save", "type": "invoke", "ac": "save", "method": "save"}],
        detected_vars=[{"name": "test_number"}],
        pick_fields=[],
    )

    assert result["score"] >= 90
    assert result["risk_level"] == "low"


def test_generate_yaml_from_ir_and_dry_run_without_network():
    flow = build_normalized_flow(_synthetic_har(), source_name="synthetic.har")
    yaml_text = generate_yaml_case_from_ir(flow, case_name="IR合成用例")
    case = yaml.safe_load(yaml_text)

    assert case["name"] == "IR合成用例"
    assert case["steps"][0]["pageId"]["value"] == "${PAGE_ID}"
    assert dry_run_flow(flow)["ok"] is True
    assert dry_run_yaml_case(yaml_text)["ok"] is True
    assert "secret-token" not in yaml_text
    assert EDIT_PAGE_ID not in yaml_text


def test_build_yaml_case_includes_value_safe_ir_contract(tmp_path: Path):
    har_path = tmp_path / "synthetic.har"
    har_path.write_text(json.dumps(_synthetic_har(), ensure_ascii=False), encoding="utf-8")

    yaml_text = build_yaml_case(har_path, case_name="IR主干契约用例")
    case = yaml.safe_load(yaml_text)
    payload = json.dumps(case["ir_contract"], ensure_ascii=False)

    assert case["ir_contract"]["source"] == "normalized_flow"
    assert case["ir_contract"]["policy"]["store_full_ir_in_yaml"] is False
    assert case["ir_contract"]["policy"]["raw_har_committed"] is False
    assert case["ir_contract"]["coverage"]["api_entry_count"] == 1
    assert case["ir_contract"]["coverage"]["ir_step_count"] >= 1
    assert case["ir_contract"]["coverage"]["yaml_step_count"] >= 0
    assert case["ir_contract"]["alignment"]["risk_level"] in {"low", "medium", "high"}
    assert "secret-token" not in payload
    assert EDIT_PAGE_ID not in payload


def test_preview_har_includes_ir_preview_without_changing_main_preview(tmp_path: Path):
    har_path = tmp_path / "synthetic.har"
    har_path.write_text(json.dumps(_synthetic_har(), ensure_ascii=False), encoding="utf-8")

    preview = preview_har(har_path)

    assert "ir_preview" in preview
    assert preview["ir_preview"]["source_har"]["api_entry_count"] == 1
    assert preview["ir_alignment"]["checks"]["ir_api_entry_count"] == 1
    assert preview["ir_preview"]["sensitive_field_count"] >= 3
    assert "main_form_id" in preview


def test_har_ir_tool_builds_redacted_flow_yaml_and_dry_runs(tmp_path: Path):
    project_root = Path(__file__).resolve().parents[2]
    script = project_root / "scripts" / "har_ir_tool.py"
    har_path = tmp_path / "synthetic.har"
    flow_path = tmp_path / "normalized_flow.json"
    yaml_path = tmp_path / "case.yaml"
    har_path.write_text(json.dumps(_synthetic_har(), ensure_ascii=False), encoding="utf-8")

    build = subprocess.run(
        [sys.executable, str(script), "build", "--har", str(har_path), "--output", str(flow_path)],
        cwd=project_root,
        text=True,
        capture_output=True,
        check=True,
    )
    build_summary = json.loads(build.stdout)
    flow_text = flow_path.read_text(encoding="utf-8")

    assert build_summary["ok"] is True
    assert build_summary["kind"] == "normalized_flow"
    assert "secret-token" not in build.stdout
    assert "secret-token" not in flow_text
    assert EDIT_PAGE_ID not in flow_text

    yaml_preview = subprocess.run(
        [
            sys.executable,
            str(script),
            "yaml-preview",
            "--flow",
            str(flow_path),
            "--case-name",
            "IR工具用例",
            "--output",
            str(yaml_path),
        ],
        cwd=project_root,
        text=True,
        capture_output=True,
        check=True,
    )
    yaml_summary = json.loads(yaml_preview.stdout)
    case = yaml.safe_load(yaml_path.read_text(encoding="utf-8"))

    assert yaml_summary["ok"] is True
    assert case["name"] == "IR工具用例"
    assert case["steps"][0]["pageId"]["value"] == "${PAGE_ID}"

    dry = subprocess.run(
        [sys.executable, str(script), "dry-run", "--yaml", str(yaml_path)],
        cwd=project_root,
        text=True,
        capture_output=True,
        check=True,
    )
    dry_summary = json.loads(dry.stdout)

    assert dry_summary["ok"] is True
    assert dry_summary["kind"] == "yaml"
