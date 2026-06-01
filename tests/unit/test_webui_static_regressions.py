from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parent.parent.parent


def _index_html() -> str:
    return (PROJECT_ROOT / "lib" / "webui" / "static" / "index.html").read_text(encoding="utf-8")


def test_har_preview_grouping_keeps_original_field_object_references():
    html = _index_html()

    assert "const item = raw;" in html
    assert "const item = {...raw};" not in html


def test_har_preview_env_fields_have_explicit_confirm_action():
    html = _index_html()

    assert ':value="harPickFieldDraftValue(pf)"' in html
    assert "@input=\"setHarPickFieldDraft(pf, $event.target.value)\"" in html
    assert '@click="savePickFieldValue(pickFieldDraftKey(pf), harPickFieldDraftValue(pf), \'display\')"' in html
    assert "harPickFieldDrafts: {}" in html
    assert 'pf_input_' not in html
    assert ':disabled="pf.readonly"' not in html
    assert "已修改，生成 YAML 后生效" in html


def test_har_preview_hides_manual_env_resolve_buttons_from_primary_flow():
    html = _index_html()

    assert "先解析字段" not in html
    assert "一键解析当前环境" not in html
    assert "建议先一键解析环境字段" not in html


def test_agent_repair_prompt_points_ai_to_ir_summary_and_safe_har_tool():
    html = _index_html()

    assert "run_artifacts.ir_summary" in html
    assert "variables.value_shape" in html
    assert "environment_fields.value_shape" in html
    assert "scripts/har_ir_tool.py build --har" in html
    assert "原始 HAR 不得提交 Git" in html
    assert "真实编辑/保存/提交步骤才切到 L3" in html


def test_ai_repair_ui_explains_handoff_without_hidden_steps():
    html = _index_html()

    assert "下一步：交给 AI 排查" in html
    assert "复制后发给 AI，系统会带上：" in html
    assert "指令包含 IR 摘要、pageId 链路和证据包地址。" in html
    assert "复制AI指令" in html
    assert "让AI修复" not in html


def test_har_advanced_diagnostics_include_ir_coverage_radar():
    html = _index_html()

    assert "IR 覆盖雷达" in html
    assert "harPreview?.ir_alignment?.summary" in html
    assert "harPreview?.ir_alignment?.checks?.ir_api_entry_count" in html
    assert "harPreview?.ir_alignment?.checks?.preview_role_counts?.write" in html
