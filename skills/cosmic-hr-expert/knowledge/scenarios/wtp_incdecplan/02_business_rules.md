# 业务规则全图 · wtp_incdecplan

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（opKeys 执行链 + mines 禁继承 + 反编译类画像）
> **数据源**: `rules_chain_all.json::opKeys` · `_deep_resolve_index.json` · `_auto_rules_deep.md`
> **生成**: polish_form_scene_v2.py（v5.1 render_02）

## ✅ verified · 主表 `wtp_incdecplan` 共 63 个 opKey

### 🔥 核心 opKey 执行链（10 个有真链）

#### `save` · 保存（opType=save）

**执行链（11 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BaseDataSavePlugin` | enabled=True |
| 3 | `BdVersionSaveServicePlugin` | 基础资料版本管理 · 主表 name + 版本子表 name 的写历史 |
| 4 | `HRBaseDataStatusOp` | HR 基础资料单据状态（draft/audit/disable） |
| 5 | `IncrDecrPlanOp` | enabled=True |
| 6 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 7 | `HRBaseDataEnableOp` | HR 基础资料启用态管理 · 控制 enable 字段 |
| 8 | `HRBaseOriginalOp` | HR 基础资料原始值记录（对比变更前后） |
| 9 | `HisUniqueValidateOp` | 时态模型唯一性校验（同一 boid 同一时间段唯一） |
| 10 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 11 | `RuleEngingSaveOp` | enabled=True |

**🚨 mines（5 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
- ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

#### `submit` · 提交（opType=submit）

**执行链（10 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BaseDataSubmitPlugin` | enabled=True |
| 3 | `BdVersionSaveServicePlugin` | 基础资料版本管理 · 主表 name + 版本子表 name 的写历史 |
| 4 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 5 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 6 | `HRBaseDataEnableOp` | HR 基础资料启用态管理 · 控制 enable 字段 |
| 7 | `HisUniqueValidateOp` | 时态模型唯一性校验（同一 boid 同一时间段唯一） |
| 8 | `IncrDecrPlanOp` | enabled=True |
| 9 | `HRBaseOriginalOp` | HR 基础资料原始值记录（对比变更前后） |
| 10 | `RuleEngingSaveOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `delete` · 删除（opType=delete）

**执行链（5 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataDeletePlugin` | enabled=True |
| 2 | `CodeRuleDeleteOp` | enabled=True |
| 3 | `HRBaseDataStatusOp` | HR 基础资料单据状态（draft/audit/disable） |
| 4 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 5 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `audit` · 审核（opType=audit）

**执行链（4 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataAuditPlugin` | enabled=True |
| 2 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 3 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 4 | `RuleEngingSaveOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `confirmchange` · 确认变更（opType=save）

**执行链（4 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 2 | `HisUniqueValidateOp` | 时态模型唯一性校验（同一 boid 同一时间段唯一） |
| 3 | `RuleEngingSaveOp` | enabled=True |
| 4 | `IncrDecrPlanOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `unaudit` · 反审核（opType=unaudit）

**执行链（3 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataUnAuditPlugin` | enabled=True |
| 2 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 3 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `disable` · 禁用（opType=disable）

**执行链（3 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataDisablePlugin` | enabled=True |
| 2 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 3 | `RuleEngingSaveOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `enable` · 启用（opType=enable）

**执行链（3 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataEnablePlugin` | enabled=True |
| 2 | `HisModelOPCommonPlugin` | 时态模型通用 · his_bsed/his_bsled/effectdate 的维护 |
| 3 | `RuleEngingSaveOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `unsubmit` · 撤销（opType=unsubmit）

**执行链（2 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataUnSubmitPlugin` | enabled=True |
| 2 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `saveandnew` · 保存并新增（opType=saveandnew）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BaseDataSavePlugin` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### 其他 opKey（53 个无独立规则·复用主链或反编译为空）

- `importdata_hr` · `show_import_record_hr` · `export_from_list_hr` · `export_from_impttpl_hr` · `export_from_expttpl_hr` · `show_export_record_hr` · `new` · `modify` · `view` · `close` · `returndata` · `copy`
- `refresh` · `option` · `submitandnew` · `first` · `previous` · `next` · `last` · `importdata` · `importdetails` · `importtemplatelist` · `exportlist` · `exportlistbyselectfields`
- `exportlist_expt` · `exportdetails` · `importexport_userset` · `mobtoolbarselect` · `mobtoolbarcancel` · `namehistory` · `namehistoryview` · `assign` · `unassign` · `bdctrlchange` · `individuation` · `assign_new`
- `auto_assign` · `tbl_assign_import` · `logview` · `viewonelog` · `hisversioninfo` · `change` · `hiscopy` · `orgpermchange` · `newhisversion` · `revise` · `reviserecord` · `versionchangecompare`
- `showallversion` · `addentry` · `deleteentryforprz` · `moveentryupforprz` · `moveentrydownforprz`

## ✅ verified · 反编译类画像（共 20 个真处理类）

| FQN（短名）| 类型 | opKey | 可 ISV 继承 | 重写生命周期 | 文档 |
|---|---|---|:---:|---|---|
| `HRBaseDataTplEdit` | FORM_PLUGIN | `save` | ✅ | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md) |
| `HRBaseDataImportEdit` | FORM_PLUGIN | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md) |
| `HRHiesButtonSwitchPlugin` | FORM_PLUGIN | `save` | ✅ | afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md) |
| `HisModelFormCommonPlugin` | FORM_PLUGIN | `save` | ✅ | beforeBindData, afterCreateNewData, afterLoadData, afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md) |
| `HisModelBuFormPlugin` | FORM_PLUGIN | `save` | ✅ | afterBindData, beforeItemClick, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin/extension_guide.md) |
| `HRBaseDataTplList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, filterContainerInit, preOpenForm | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md) |
| `HRBasedataLogList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md) |
| `HisModelListCommonPlugin` | FORM_PLUGIN | `save` | ❌ | filterContainerInit, beforeBindData, setFilter, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/extension_guide.md) |
| `HisModelBuListPlugin` | LIST_PLUGIN | `save` | ❌ | afterBindData, beforeItemClick, itemClick, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin/extension_guide.md) |
| `HisModelF7ListPlugin` | OP_PLUGIN | `save` | ❌ | setFilter, beforePackageData, propertyChanged, beforeBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/extension_guide.md) |
| `HisModelFilterPanelListPlugin` | FORM_PLUGIN | `save` | ❌ | filterContainerInit, setFilter | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md) |
| `HisModelFilterPanelF7ListPlugin` | OP_PLUGIN | `save` | ❌ | beforeBindData, afterBindData, beforePackageData, setFilter | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md) |
| `HisModelMobileListPlugin` | OTHER | `save` | ❌ | setFilter | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/extension_guide.md) |
| `HRBaseDataStatusOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md) |
| `HRBaseDataEnableOp` | OP_PLUGIN | `save` | ✅ | beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md) |
| `HRBaseOriginalOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md) |
| `HisUniqueValidateOp` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md) |
| `HisModelOPCommonPlugin` | OP_PLUGIN | `save` | ✅ | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction, beginOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md) |
| `HRBaseDataLogOp` | OP_PLUGIN | `unsubmit` | ✅ | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md) |
| `HisBaseDataF7FastFilter` | OTHER | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md) |

## 🚨 ISV 写代码必看

- 跑业务规则定制前·请把 `rules_chain_all.json::opKeys.*.mines` 全段读完
- 想加自定义 Validator → 必须在 `onAddValidators` 阶段（详见 mines）
- 想加 BusinessRule → 在 `beforeExecuteOperationTransaction` 阶段·throw → 事务回滚
- 禁继承类清单参见各 opKey 的 `mines[]` + `_sdk_audit/abuse_blacklist.md`
