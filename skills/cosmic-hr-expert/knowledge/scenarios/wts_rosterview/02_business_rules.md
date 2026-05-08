# 业务规则全图 · wts_rosterview

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（opKeys 执行链 + mines 禁继承 + 反编译类画像）
> **数据源**: `rules_chain_all.json::opKeys` · `_deep_resolve_index.json` · `_auto_rules_deep.md`
> **生成**: polish_form_scene_v2.py（v5.1 render_02）

## ✅ verified · 主表 `wts_rosterview` 共 29 个 opKey

### 其他 opKey（29 个无独立规则·复用主链或反编译为空）

- `show_import_record_hr` · `show_export_record_hr` · `copypersonroster` · `donothing` · `saveroster` · `lockpersonroster` · `unlockpersonroster` · `completepersonroster` · `copyorgroster` · `lockorgroster` · `unlockorgroster` · `completeorgroster`
- `restore` · `rosterpersonselect` · `clearselect` · `cyclepersonroster` · `close` · `viewlog` · `search` · `refresh` · `insertpower` · `parsepower` · `syncplanroster` · `importroster`
- `exportroster` · `datetypeadjust` · `exportrostertable` · `importrostertable` · `initdata`

## 🚨 ISV 写代码必看

- 跑业务规则定制前·请把 `rules_chain_all.json::opKeys.*.mines` 全段读完
- 想加自定义 Validator → 必须在 `onAddValidators` 阶段（详见 mines）
- 想加 BusinessRule → 在 `beforeExecuteOperationTransaction` 阶段·throw → 事务回滚
- 禁继承类清单参见各 opKey 的 `mines[]` + `_sdk_audit/abuse_blacklist.md`
