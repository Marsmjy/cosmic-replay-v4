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


def test_har_preview_code_override_updates_stored_value_id_before_extract():
    html = _index_html()

    assert "pf.value_id = text;" in html
    assert "pf.value_id = newValue;" in html
    assert "value_id: newValue," in html
    assert "pf.value_code = text;" in html
    assert "pf.value_code = newValue;" in html


def test_har_preview_maintenance_panel_has_data_functions_and_code_detection():
    html = _index_html()

    assert "harMaintenanceItems()" in html
    assert "harMaintenanceGroups()" in html
    assert "可维护业务字段" in html
    assert "系统字段、锁定字段和技术上下文已自动隐藏" in html
    assert "harMaintenanceBusinessItem" in html
    assert "harMaintenanceTechnicalField" in html
    assert "harHiddenMaintenanceCount()" in html
    assert "harMaintenancePriorityLabel(item)" in html
    assert "harPickResolveLabel(item.ref)" in html
    assert "_harStepOrderMap()" in html
    assert "_harFieldCatalogOrderMaps(data.preview)" in html
    assert "_harCatalogOrderForVar(v, catalogOrder)" in html
    assert "_harCatalogOrderForPick(item, catalogOrder)" in html
    assert "if (Number.isFinite(ref._catalog_order)) return ref._catalog_order;" in html
    assert "pickFieldCanResolveTypedCode(pf, value)" in html
    assert "const asCode = kind === 'code' || this.pickFieldCanResolveTypedCode(pf, text);" in html
    assert "const asCode = kind === 'code' || this.pickFieldCanResolveTypedCode(pf, newValue);" in html


def test_har_preview_maintenance_panel_hides_technical_fields():
    html = _index_html()

    assert "'pageid'" in html
    assert "'treeview.focus'" in html
    assert "'session'" in html
    assert "if (ref.readonly === true) return true;" in html
    assert "if (!this.harMaintenanceBusinessItem(pf, 'pick')) continue;" in html
    assert "if (!this.harMaintenanceBusinessItem(v, 'var')) continue;" in html


def test_case_variable_panel_reuses_unified_maintainable_business_fields():
    html = _index_html()

    assert "详情页与 HAR 预览共用同一套“可维护业务字段”视图" in html
    assert 'x-text="\'(\' + caseMaintenanceItems().length + \')\'"' in html
    assert "caseMaintenanceItems()" in html
    assert "caseMaintenanceGroups()" in html
    assert "caseMaintenanceKindCount('var')" in html
    assert "caseMaintenanceKindCount('pick')" in html
    assert "caseHiddenMaintenanceCount()" in html
    assert "this.harMaintenanceBusinessItem(v, 'var')" in html
    assert "this.harMaintenanceBusinessItem(pf, 'pick')" in html
    assert "_harStepOrderMapFromSteps(this.parsedSteps())" in html


def test_case_variable_panel_drafts_are_scoped_to_current_yaml():
    html = _index_html()

    assert "this.harPreview = null;" in html
    assert "this.harVarConfig = [];" in html
    assert "this.harPickFields = [];" in html
    assert "this.hydrateCasePickFieldDrafts();" in html
    assert "hydrateCasePickFieldDrafts()" in html
    assert "for (const pf of (this.parsedPickFields() || []))" in html
    assert "casePickFieldItems()" in html
    assert "const parsed = this.parsedPickFields();" in html
    assert "return parsed;" in html


def test_run_saves_case_pick_field_drafts_before_execution():
    html = _index_html()

    assert "await this.savePendingCasePickFieldDrafts();" in html
    assert "async savePendingCasePickFieldDrafts()" in html
    assert "for (const pf of (this.parsedPickFields() || []))" in html
    assert "draft && draft !== stored" in html
    assert "await this.savePickFieldValue(item.key, item.value, 'display');" in html


def test_run_panels_display_business_value_before_internal_id():
    html = _index_html()

    assert "envFieldDisplayValue(ef)" in html
    assert "ef.display_value || ef.value_code || ef.value_number || ef.value_name || ef.value_id" in html
    assert 'x-text="envFieldDisplayValue(ef)"' in html
    assert "x-text=\"'ID ' + ef.value_id\"" in html


def test_case_list_supports_copy_case_action():
    html = _index_html()

    assert '@click="copyCase(c.name)"' in html
    assert "async copyCase(name)" in html
    assert "/copy" in html
    assert "复制为新用例名称" in html


def test_har_import_supports_multi_file_batch_import():
    html = _index_html()

    assert 'accept=".har" multiple' in html
    assert "importHarBatch(files)" in html
    assert "harBatchResults: []" in html
    assert "harBatchSummary()" in html
    assert "批量导入结果" in html
    assert "this.uniqueCaseName(file.name.replace(/\\.har$/i, ''), usedNames)" in html


def test_har_preview_hides_manual_env_resolve_buttons_from_primary_flow():
    html = _index_html()

    assert "先解析字段" not in html
    assert "一键解析当前环境" not in html
    assert "建议先一键解析环境字段" not in html


def test_agent_repair_prompt_points_ai_to_ir_summary_and_safe_har_tool():
    html = _index_html()

    assert "永远围绕项目核心目标" in html
    assert "用户维护值必须生效" in html
    assert "执行必须校验保存/提交和入库证据" in html
    assert "run_artifacts.ir_summary" in html
    assert "variables.value_shape" in html
    assert "environment_fields.value_shape" in html
    assert "scripts/har_ir_tool.py build --har" in html
    assert "原始 HAR 不得提交 Git" in html
    assert "真实编辑/保存/提交步骤才切到 L3" in html


def test_project_core_goals_are_documented_in_skills():
    overview = (PROJECT_ROOT / "skills" / "cosmic-replay-overview" / "SKILL.md").read_text(encoding="utf-8")
    troubleshooter = (PROJECT_ROOT / "skills" / "cosmic-replay-troubleshooter" / "SKILL.md").read_text(encoding="utf-8")

    for text in (overview, troubleshooter):
        assert "项目核心目标" in text
        assert "HAR 解析要识别真正可维护字段" in text
        assert "用户在预览页或变量面板维护的值" in text
        assert "调用目标环境接口解析真实 id" in text
        assert "执行结果不能只看无异常或最终 PASS" in text
        assert "修复经验要沉淀为通用解析/执行规则" in text


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
