# 变更影响面 · homs_positionrelation

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（mines + invariants + 跨云引用 · v2）
> **数据源**: `rules_chain_all.json::mines` · `scene_doc.json::invariants` · `_cross_cloud_index.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品禁区（mines · ISV 改之前必读）

- ❌ 禁继承 PositionRelationChartPlugin（岗位关系图插件 · 标品场景专属 · 无 SDK 注解）（出现于 opKey=`view`）
- ❌ 禁继承 HRDynamicFormBasePlugin（HR 动态表单基础插件 · 禁继承）（出现于 opKey=`view`）
- ❌ 禁继承 AbstractFormPlugin（苍穹基础 FormPlugin · ISV 应继承 HRDataBaseEdit）（出现于 opKey=`view`）

## 🟡 字段级影响（待人工补充）

<TODO 人工补> 修改主表关键字段的具体影响：
- [ ] 修改 `number`（编码）的下游影响
- [ ] 修改 `name` / `org` 等关键字段的下游影响
- [ ] 删除主记录的级联反应
