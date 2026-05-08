# 变更影响面 · homs_orgchart_new

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（mines + invariants + 跨云引用 · v2）
> **数据源**: `rules_chain_all.json::mines` · `scene_doc.json::invariants` · `_cross_cloud_index.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品禁区（mines · ISV 改之前必读）

- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名（出现于 opKey=`edit_donothing`）
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了（出现于 opKey=`edit_donothing`）
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好（出现于 opKey=`edit_donothing`）
- ❌ 禁继承 NewChartOrgChartPlugin（组织架构图主插件 · 标品场景专属 · 无 SDK 注解）（出现于 opKey=`edit_donothing`）
- ❌ 禁继承 NewOrgChartSchemePlugin（架构图方案切换插件 · 标品场景专属 · 无 SDK 注解）（出现于 opKey=`edit_donothing`）
- ❌ 禁继承 NewPersonalStylePlugin（个人显示风格插件 · 标品场景专属 · 无 SDK 注解）（出现于 opKey=`edit_donothing`）

## 🟡 字段级影响（待人工补充）

<TODO 人工补> 修改主表关键字段的具体影响：
- [ ] 修改 `number`（编码）的下游影响
- [ ] 修改 `name` / `org` 等关键字段的下游影响
- [ ] 删除主记录的级联反应
