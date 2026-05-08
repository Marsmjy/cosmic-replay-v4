# 扩展点全图 · hies_mgrmonitor

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hies_mgrmonitor` 标品接入 **4** 个插件。

### FormPlugin（4）
- `CardTempletePlugin` ← `kd.bos.card.plugin.CardTempletePlugin`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `ForbidUrlOpenPlugin` ← `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
- `DiaeMgrMonitorPlugin` ← `kd.hr.hies.formplugin.DiaeMgrMonitorPlugin`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）
