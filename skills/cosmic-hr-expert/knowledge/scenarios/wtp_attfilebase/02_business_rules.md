# 业务规则全图 · wtp_attfilebase

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（opKeys 执行链 + mines 禁继承 + 反编译类画像）
> **数据源**: `rules_chain_all.json::opKeys` · `_deep_resolve_index.json` · `_auto_rules_deep.md`
> **生成**: polish_form_scene_v2.py（v5.1 render_02）

## ✅ verified · 主表 `wtp_attfilebase` 共 69 个 opKey

### 🔥 核心 opKey 执行链（69 个有真链）

#### `importdata_hr` · 导入数据（opType=importdata_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `show_import_record_hr` · 查看导入记录（opType=show_import_record_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `export_from_list_hr` · 按列表导出（opType=export_from_list_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `export_from_impttpl_hr` · 按导入模板导出（opType=export_from_impttpl_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `export_from_expttpl_hr` · 按导出模板导出（opType=export_from_expttpl_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `show_export_record_hr` · 查看导出记录（opType=show_export_record_hr）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `new` · 新增（opType=new）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `modify` · 修改（opType=modify）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `view` · 查看（opType=view）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `close` · 关闭（opType=close）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `returndata` · 返回数据（opType=returndata）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `copy` · 复制（opType=copy）

**执行链（12 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataStatusOp` | plugins.json 实证 · 标品 · extendable |
| 2 | `HRBaseDataLogOp` | plugins.json 实证 · 标品 · extendable |
| 3 | `HRBaseDataEnableOp` | plugins.json 实证 · 标品 · extendable |
| 4 | `HRBaseOriginalOp` | plugins.json 实证 · 标品 · extendable |
| 5 | `HisModelOPCommonPlugin` | plugins.json 实证 · 标品 · extendable |
| 6 | `AttFileBaseSaveOp` | plugins.json 实证 · 标品 · extendable |
| 7 | `HisUniqueValidateOp` | plugins.json 实证 · 标品 · extendable |
| 8 | `AttFileBaseModifyOp` | plugins.json 实证 · 标品 · extendable |
| 9 | `AttFileBaseDiscardOp` | plugins.json 实证 · 标品 · extendable |
| 10 | `AttFileResignRollBackOp` | plugins.json 实证 · 标品 · extendable |
| 11 | `AttFileTransferOp` | plugins.json 实证 · 标品 · extendable |
| 12 | `AttFileBatchReviseOp` | plugins.json 实证 · 标品 · extendable |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

## ✅ verified · 反编译类画像（共 16 个真处理类）

| FQN（短名）| 类型 | opKey | 可 ISV 继承 | 重写生命周期 | 文档 |
|---|---|---|:---:|---|---|
| `HRBaseDataTplEdit` | FORM_PLUGIN | `save` | ✅ | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md) |
| `HRBaseDataImportEdit` | FORM_PLUGIN | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md) |
| `HRHiesButtonSwitchPlugin` | FORM_PLUGIN | `save` | ✅ | afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md) |
| `HisModelFormCommonPlugin` | FORM_PLUGIN | `save` | ✅ | beforeBindData, afterCreateNewData, afterLoadData, afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md) |
| `HRBaseDataTplList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, filterContainerInit, preOpenForm | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md) |
| `HRBasedataLogList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md) |
| `HisModelFilterPanelListPlugin` | FORM_PLUGIN | `save` | ❌ | filterContainerInit, setFilter | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md) |
| `HisModelFilterPanelF7ListPlugin` | OP_PLUGIN | `save` | ❌ | beforeBindData, afterBindData, beforePackageData, setFilter | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md) |
| `HRBaseDataStatusOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md) |
| `HRBaseDataLogOp` | OP_PLUGIN | `save` | ✅ | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md) |
| `HRBaseDataEnableOp` | OP_PLUGIN | `save` | ✅ | beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md) |
| `HRBaseOriginalOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md) |
| `HisModelOPCommonPlugin` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction, beginOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md) |
| `HisUniqueValidateOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md) |
| `HisModelBatchImportPlugin` | OTHER | `multisheetimport` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.impt.HisModelBatchImportPlugin/extension_guide.md) |
| `HisBaseDataF7FastFilter` | OTHER | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md) |

## 🚨 ISV 写代码必看

- 跑业务规则定制前·请把 `rules_chain_all.json::opKeys.*.mines` 全段读完
- 想加自定义 Validator → 必须在 `onAddValidators` 阶段（详见 mines）
- 想加 BusinessRule → 在 `beforeExecuteOperationTransaction` 阶段·throw → 事务回滚
- 禁继承类清单参见各 opKey 的 `mines[]` + `_sdk_audit/abuse_blacklist.md`
