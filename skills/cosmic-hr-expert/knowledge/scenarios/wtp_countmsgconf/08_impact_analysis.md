# 变更影响面 · wtp_countmsgconf

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（mines + invariants + 跨云引用 · v2）
> **数据源**: `rules_chain_all.json::mines` · `scene_doc.json::invariants` · `_cross_cloud_index.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品禁区（mines · ISV 改之前必读）

- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名（出现于 opKey=`importdata_hr`）
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了（出现于 opKey=`importdata_hr`）
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好（出现于 opKey=`importdata_hr`）
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）（出现于 opKey=`save`）
- ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）（出现于 opKey=`save`）

## 🟡 字段级影响（待人工补充）

<TODO 人工补> 修改主表关键字段的具体影响：
- [ ] 修改 `number`（编码）的下游影响
- [ ] 修改 `name` / `org` 等关键字段的下游影响
- [ ] 删除主记录的级联反应
