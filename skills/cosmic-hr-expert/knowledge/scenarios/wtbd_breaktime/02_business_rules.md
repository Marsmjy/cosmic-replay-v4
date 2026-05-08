# 业务规则全图 · wtbd_breaktime

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（opKeys 执行链 + mines 禁继承 + 反编译类画像）
> **数据源**: `rules_chain_all.json::opKeys` · `_deep_resolve_index.json` · `_auto_rules_deep.md`
> **生成**: polish_form_scene_v2.py（v5.1 render_02）

## ✅ verified · 主表 `wtbd_breaktime` 共 31 个 opKey

### 🔥 核心 opKey 执行链（10 个有真链）

#### `save1` · 保存（opType=save）

**执行链（7 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BdVersionSaveServicePlugin` | 基础资料版本管理 · 主表 name + 版本子表 name 的写历史 |
| 3 | `BreakTimeOp` | enabled=True |
| 4 | `HRBaseDataStatusOp` | HR 基础资料单据状态（draft/audit/disable） |
| 5 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 6 | `HRBaseDataEnableOp` | HR 基础资料启用态管理 · 控制 enable 字段 |
| 7 | `HRBaseOriginalOp` | HR 基础资料原始值记录（对比变更前后） |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `submit` · 提交（opType=submit）

**执行链（5 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BdVersionSaveServicePlugin` | 基础资料版本管理 · 主表 name + 版本子表 name 的写历史 |
| 3 | `HRBaseDataLogOp` | HR 基础资料操作日志 |
| 4 | `HRBaseDataEnableOp` | HR 基础资料启用态管理 · 控制 enable 字段 |
| 5 | `HRBaseOriginalOp` | HR 基础资料原始值记录（对比变更前后） |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `save` · 保存（opType=save）

**执行链（2 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BreakTimeOp` | enabled=True |

**🚨 mines（5 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好
- ❌ 禁继承 HisModelOPCommonPlugin/HisUniqueValidateOp/HisModelFormCommonPlugin/HisModelListCommonPlugin（@SdkInternal 平台历史版本内部类 · ISV 不得继承）
- ❌ 禁继承 AbsOrgBaseOp（非 HR 通用推荐 · 用 HRDataBaseOp 代替）

#### `update` · 保存（opType=save）

**执行链（2 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleOp` | 编码规则（未启用时跳过）· onAddValidators 注册 numberValidator |
| 2 | `BreakTimeOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `audit` · 审核（opType=audit）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `unaudit` · 反审核（opType=unaudit）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `disable` · 禁用（opType=disable）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `enable` · 启用（opType=enable）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `unsubmit` · 撤销（opType=unsubmit）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRBaseDataLogOp` | HR 基础资料操作日志 |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `wtbs_save` · 休息时间保存（opType=donothing）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `BreakTimeOP` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### 其他 opKey（21 个无独立规则·复用主链或反编译为空）

- `new` · `close` · `returndata` · `copy` · `refresh` · `option` · `submitandnew` · `saveandnew` · `first` · `previous` · `next` · `last`
- `importtemplatelist` · `mobtoolbarselect` · `mobtoolbarcancel` · `namehistory` · `namehistoryview` · `logview` · `viewonelog` · `delete` · `breaktime_new`

## ✅ verified · 反编译类画像（共 9 个真处理类）

| FQN（短名）| 类型 | opKey | 可 ISV 继承 | 重写生命周期 | 文档 |
|---|---|---|:---:|---|---|
| `HRBaseDataTplEdit` | FORM_PLUGIN | `save` | ✅ | afterBindData, afterLoadData, beforeDoOperation, afterDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md) |
| `HRBaseDataImportEdit` | FORM_PLUGIN | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md) |
| `HRHiesButtonSwitchPlugin` | FORM_PLUGIN | `save` | ✅ | afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md) |
| `HRBaseDataTplList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, filterContainerInit, preOpenForm | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md) |
| `HRBasedataLogList` | FORM_PLUGIN | `save` | ❌ | beforeBindData, beforeDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md) |
| `HRBaseDataStatusOp` | OP_PLUGIN | `save1` | ✅ | onPreparePropertys, onAddValidators, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md) |
| `HRBaseDataLogOp` | OP_PLUGIN | `save1` | ✅ | beforeExecuteOperationTransaction, beginOperationTransaction, afterExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md) |
| `HRBaseDataEnableOp` | OP_PLUGIN | `save1` | ✅ | beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md) |
| `HRBaseOriginalOp` | OP_PLUGIN | `save1` | ✅ | onPreparePropertys, beforeExecuteOperationTransaction | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md) |

## 🚨 ISV 写代码必看

- 跑业务规则定制前·请把 `rules_chain_all.json::opKeys.*.mines` 全段读完
- 想加自定义 Validator → 必须在 `onAddValidators` 阶段（详见 mines）
- 想加 BusinessRule → 在 `beforeExecuteOperationTransaction` 阶段·throw → 事务回滚
- 禁继承类清单参见各 opKey 的 `mines[]` + `_sdk_audit/abuse_blacklist.md`
