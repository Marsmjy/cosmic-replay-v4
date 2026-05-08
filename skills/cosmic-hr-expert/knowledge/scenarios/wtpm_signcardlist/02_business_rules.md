# 业务规则全图 · wtpm_signcardlist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 已实证（opKeys 执行链 + mines 禁继承 + 反编译类画像）
> **数据源**: `rules_chain_all.json::opKeys` · `_deep_resolve_index.json` · `_auto_rules_deep.md`
> **生成**: polish_form_scene_v2.py（v5.1 render_02）

## ✅ verified · 主表 `wtpm_signcardlist` 共 35 个 opKey

### 🔥 核心 opKey 执行链（2 个有真链）

#### `delete` · 删除（opType=delete）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `CodeRuleDeleteOp` | enabled=True |

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

#### `saveofsubmiteffect` · 提交并生效（opType=donothing）

**执行链（1 个 plugin · 按 order 排）**：

| order | className | 角色 |
|:---:|---|---|
| 1 | `HRCodeRuleOp` | 重写 1 个方法：onAddValidators（注册校验器） |

**业务规则（1）**：
- `B_HRCodeRuleOp_1`: 

**🚨 mines（3 条 ISV 雷区）**：
- ⚠ 直接修改 DynamicObject.set(fieldKey, ...) 必须用字段 key · 不能用显示名
- ⚠ 注册自定义 Validator 必须在 onAddValidators 阶段 · 在 beforeExecute 注册晚了
- ⚠ beforeExecuteOperationTransaction 抛异常 → 事务回滚 · 使用 addErrorMessage 更好

### 其他 opKey（33 个无独立规则·复用主链或反编译为空）

- `importdata_hr` · `show_import_record_hr` · `export_from_list_hr` · `export_from_impttpl_hr` · `export_from_expttpl_hr` · `show_export_record_hr` · `close` · `refresh` · `unsubmit` · `returndata` · `mobtoolbarselect` · `mobtoolbarcancel`
- `viewtrash` · `trash` · `restoredata` · `discard` · `unaudit` · `audit` · `deleteentry` · `modify` · `viewlog` · `new` · `dounsubmit` · `dodiscard`
- `dodelete` · `doaudit` · `exportlist_expt` · `exportlist` · `exportlistbyselectfields` · `exportdetails` · `importexport_userset` · `importdetails` · `saveofsubmit`

## ✅ verified · 反编译类画像（共 8 个真处理类）

| FQN（短名）| 类型 | opKey | 可 ISV 继承 | 重写生命周期 | 文档 |
|---|---|---|:---:|---|---|
| `HRTemplateBillEdit` | FORM_PLUGIN | `save` | ❌ | beforeBindData, afterDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/extension_guide.md) |
| `HRPermCommonEdit` | FORM_PLUGIN | `save` | ✅ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md) |
| `HRBaseDataImportEdit` | FORM_PLUGIN | `save` | ❌ |  | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md) |
| `HRBaseUeEdit` | FORM_PLUGIN | `save` | ✅ | preOpenForm | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md) |
| `HRHiesButtonSwitchPlugin` | FORM_PLUGIN | `save` | ✅ | afterBindData | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md) |
| `HRTemplateBillList` | FORM_PLUGIN | `save` | ❌ | afterDoOperation | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/extension_guide.md) |
| `HRPermCommonList` | FORM_PLUGIN | `save` | ❌ | beforeF7Select | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/extension_guide.md) |
| `HRCodeRuleOp` | OP_PLUGIN | `saveofsubmiteffect` | ✅ | onAddValidators | [查看](../../<skills-dir>/cosmic-hr-expert/knowledge/_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md) |

## 🚨 ISV 写代码必看

- 跑业务规则定制前·请把 `rules_chain_all.json::opKeys.*.mines` 全段读完
- 想加自定义 Validator → 必须在 `onAddValidators` 阶段（详见 mines）
- 想加 BusinessRule → 在 `beforeExecuteOperationTransaction` 阶段·throw → 事务回滚
- 禁继承类清单参见各 opKey 的 `mines[]` + `_sdk_audit/abuse_blacklist.md`
