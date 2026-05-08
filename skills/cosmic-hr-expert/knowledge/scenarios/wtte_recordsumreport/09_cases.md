# ISV 实战 case · wtte_recordsumreport

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（从 deep_resolve 推典型 case）
> **数据源**: `_deep_resolve_index.json` · `rules_chain_all.json::opKeys`
> **生成**: polish_form_scene_v2.py（v5.1 render_09）

## 典型 ISV 定制 case（按可继承类聚合）

## 典型 opKey 业务边界 case

### opKey `reportquery` · 账表查询

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `refresh` · 刷新

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `close` · 关闭

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `syncexportexcel` · 导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `exportexcel` · 导出

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### opKey `exportreport` · 导出表格数据

**ISV 在此 opKey 上易踩的雷**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## 通用 ISV 套路提醒

- **必调 super**：所有 lifecycle 方法 override 必须先调 super · 保留标品行为
- **不动标品 form**：要加字段 → 建 ISV 扩展元数据 + `_inherits` 引用主表
- **注册插件 targetType**：BILL_FORM / LIST_FORM / OPERATION 之一（大写枚举·区分大小写）
- **测试**：跑标品测试用例 + ISV 自有用例·验证 super 行为完好
