# 扩展点全图 · core_hr_hssc_home

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hssc_apphome` 标品接入 **3** 个插件。

### FormPlugin（3）
- `BizAppHomePlugin` ← `kd.bos.portal.plugin.BizAppHomePlugin`
- `GridContainerPlugin` ← `kd.bos.portal.pluginnew.GridContainerPlugin`
- `BizAppHomePlugin` ← `kd.bd.gbs.plugin.backgroundtask.form.BizAppHomePlugin`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）
