# 扩展点全图 · core_hr_apply_emp

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrom_applybill_emp` 标品接入 **14** 个插件。

### FormPlugin（8）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRTemplateBillEdit` ← `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
- `HRBUCAApplicationEdit` ← `kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit`
- `HRPermCommonEdit` ← `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `ApplyBaseFormPlugin` ← `kd.hros.hrom.formplugin.route.apply.ApplyBaseFormPlugin`

### ListPlugin（3）
- `HRTemplateBillList` ← `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
- `HRPermCommonList` ← `kd.hr.hbp.formplugin.web.HRPermCommonList`
- `ApplyBaseListPlugin` ← `kd.hros.hrom.formplugin.route.apply.ApplyBaseListPlugin`

### OpPlugin（3）
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRCodeRuleOp` ← `kd.hr.hbp.opplugin.web.HRCodeRuleOp`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）
