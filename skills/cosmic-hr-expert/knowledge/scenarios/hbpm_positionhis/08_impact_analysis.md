# 变更影响面 · hbpm_positionhis

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（mines + invariants + 跨云引用 · v2）
> **数据源**: `rules_chain_all.json::mines` · `scene_doc.json::invariants` · `_cross_cloud_index.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品禁区（mines · ISV 改之前必读）

- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名（出现于 opKey=`export_from_list_hr`）
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了（出现于 opKey=`export_from_list_hr`）
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好（出现于 opKey=`export_from_list_hr`）
- ❌ 禁继承 PositionHistList（场景专属 final ListPlugin · ISV 用 HRDataBaseList 并列挂）（出现于 opKey=`his_save`）
- ❌ 禁继承 PositionEdit（场景专属 FormPlugin · ISV 用 HRDataBaseEdit 并列挂）（出现于 opKey=`his_save`）
- ❌ 禁继承 HisModelFormCommonPlugin（@SdkInternal 内部类 · ISV 禁继承）（出现于 opKey=`his_save`）
- ❌ 禁继承 HisModelListCommonPlugin（@SdkInternal 内部类 · ISV 禁继承）（出现于 opKey=`his_save`）
- ❌ 禁继承 HisModelOPCommonPlugin（@SdkInternal 内部类 · ISV 禁继承）（出现于 opKey=`his_save`）
- ❌ 禁继承 PositionHisSaveOp（场景专属 SaveOp · ISV 用 HRDataBaseOp 并列挂）（出现于 opKey=`his_save`）

## 🟡 字段级影响（待人工补充）

<TODO 人工补> 修改主表关键字段的具体影响：
- [ ] 修改 `number`（编码）的下游影响
- [ ] 修改 `name` / `org` 等关键字段的下游影响
- [ ] 删除主记录的级联反应
