# core_hr_quit · 业务规则

> **聚合场景**：离职管理（htm · 5 单据 · INFO_QUIT 大类）（2 个子实体）
> **生成时间**：2026-04-30
> **方法**：从 `_shared/_standard_metadata/entity_metadata/<entity>.md` 提取每子实体真字段元数据 → 标品 HRBaseDataTplEdit 共性规则 + 子实体特有约束

## 一、共性规则（HRBaseDataTplEdit 标品模板 · 12 实体共用）

本聚合场景所有子实体均继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` 标品模板，自带规则：

| 规则 ID | 触发点 | 行为 | ISV 是否可改 |
|---|---|---|:---:|
| BR_TPL_1 | 表单加载 (afterBindData) | 自动加载基础资料元数据 + 渲染字段 | ❌ 标品 |
| BR_TPL_2 | save 操作 | 触发 CodeRuleOp 自动生成 number 字段（按编码规则） | ❌ 标品 · 规则在元数据里配置 |
| BR_TPL_3 | save 操作 | HRBaseDataStatusOp · 设置 status 字段（A/B/C 状态机） | ❌ 标品 |
| BR_TPL_4 | save 操作 | HRBaseOriginalOp · 维护 orinumber/oriname/oristatus 出厂数据 | ❌ 标品 |
| BR_TPL_5 | enable / disable | HRBaseDataEnableOp · 维护 enable 字段 + disabledate/disabler | ❌ 标品 |
| BR_TPL_6 | save 操作 | HRBaseDataLogOp · 写变更日志（按 HRBaseDataConfigUtil 配置启用） | ❌ 标品 |

> ⚠️ ISV 不应继承 HRBaseDataTplEdit · 应**并列挂** `HRDataBaseEdit` 实现自定义逻辑（PR-001）

## 二、子实体特有规则（按字段提取）

### `htm_quitapply` · ⚠️ 标品 entity_metadata md 不存在

### `htm_quitapplybill` · ⚠️ 标品 entity_metadata md 不存在

## 三、关键约束（共 12 实体）

| 约束 | 适用实体 | 来源 |
|---|---|---|
| `number` 唯一性 | 全部 12 实体 | 标品 CodeRuleOp + 元数据 UniqueValidation |
| `enable` 默认值 = '1'（启用） | 全部 12 实体 | HRBaseDataEnableOp |
| `status` 状态机 (A 暂存 → B 待审核 → C 已审核) | 全部 12 实体 | HRBaseDataStatusOp |
| `disabler` / `disabledate` 自动维护 | 全部 12 实体 | HRBaseDataEnableOp |

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 数据源：12 子实体的 `_shared/_standard_metadata/entity_metadata/<entity>.md`
- 标品共性来自 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBaseDataLogOp` 反编译实证

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

## chgaction 实证补充（HRTemplateBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## chgaction 实证补充（HRPermCommonEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## chgaction 实证补充（HRBaseUeEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

## chgaction 实证补充（PerChgNewBillTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.CommonHPFSPlugin -->

## chgaction 实证补充（CommonHPFSPlugin 跨类追踪聚合）

> FQN: `kd.hr.htm.formplugin.apply.CommonHPFSPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.formplugin.apply.CommonHPFSPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.CommonHPFSPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitApplyCommonPlugin -->

## chgaction 实证补充（QuitApplyCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.htm.formplugin.apply.QuitApplyCommonPlugin`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.formplugin.apply.QuitApplyCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitApplyCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

## chgaction 实证补充（HRTemplateBillList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

## chgaction 实证补充（HRPermCommonList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

## chgaction 实证补充（PerChgNewBillTplList 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplList`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitApplyListPlugin -->

## chgaction 实证补充（QuitApplyListPlugin 跨类追踪聚合）

> FQN: `kd.hr.htm.formplugin.apply.QuitApplyListPlugin`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.formplugin.apply.QuitApplyListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitApplyListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

## chgaction 实证补充（HRCommonListFilterPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitapplyMobPlugin -->

## chgaction 实证补充（QuitapplyMobPlugin 跨类追踪聚合）

> FQN: `kd.hr.htm.formplugin.apply.QuitapplyMobPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.formplugin.apply.QuitapplyMobPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.formplugin.apply.QuitapplyMobPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## chgaction 实证补充（PerChgTplSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySaveOp -->

## chgaction 实证补充（QuitApplySaveOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplySaveOp`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplySaveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `htm_workcalendar` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CalendarRepository.gene |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

## chgaction 实证补充（PerChgTplUpdateChgRecordOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationCommonOp -->

## chgaction 实证补充（PerChgTplTerminationCommonOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationCommonOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationCommonOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationCommonOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

## chgaction 实证补充（PerChgTplSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySubmitOp -->

## chgaction 实证补充（QuitApplySubmitOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplySubmitOp`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplySubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `QuitApplySubmitOp_0` | 编制调用参数错误。 |
| `QuitApplySubmitOp_1` | 编制释放异常 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitStaffServiceImpl.class`
- `QuitApplyServiceImpl.SELECT_FIELD`
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

## chgaction 实证补充（PerChgTplDiscardChgRecordOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyUnSubmitOp -->

## chgaction 实证补充（QuitApplyUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplyUnSubmitOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplyUnSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `htm_workcalendar` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CalendarRepository.gene |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.SELECT_FIELD`
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySubmitEffectOp -->

## chgaction 实证补充（QuitApplySubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplySubmitEffectOp`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplySubmitEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `QuitApplySubmitOp_0` | 编制调用参数错误。 |
| `QuitApplySubmitOp_1` | 编制释放异常 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitStaffServiceImpl.class`
- `QuitApplyServiceImpl.SELECT_FIELD`
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplySubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

## chgaction 实证补充（PerChgTplEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationOp -->

## chgaction 实证补充（PerChgTplTerminationOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyEffectOp -->

## chgaction 实证补充（QuitApplyEffectOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplyEffectOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplyEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditingOp -->

## chgaction 实证补充（ApprovalAuditingOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.ApprovalAuditingOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.ApprovalAuditingOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.addComm |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalRejectToSubmitOp -->

## chgaction 实证补充（ApprovalRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.ApprovalRejectToSubmitOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.ApprovalRejectToSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `htm_workcalendar` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CalendarRepository.gene |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditNotPassOp -->

## chgaction 实证补充（ApprovalAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.ApprovalAuditNotPassOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.ApprovalAuditNotPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.addComm |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.SELECT_FIELD`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`
- `QuitActivityGenerateServiceImpl.initCoopActivity`
- `QuitActivityGenerateServiceImpl.initInterview`
- `QuitActivityGenerateServiceImpl.batchRevocation`
- `QuitActivityGenerateServiceImpl.handleMessage`
- `QuitApplyValidateServiceImpl.class`
- `QuitApplyValidateServiceImpl.batchSubmitValidate`
- `QuitApplyValidateServiceImpl.getEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditPassOp -->

## chgaction 实证补充（ApprovalAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.ApprovalAuditPassOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.ApprovalAuditPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.addComm |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.is |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.isA |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `ActivityHandleServiceImpl.class`
- `ActivityHandleServiceImpl.dealTransfer`
- `ActivityHandleServiceImpl.dealReject`
- `QuitApplyValidateServiceImpl.class`
- `QuitApplyValidateServiceImpl.batchSubmitValidate`
- `QuitApplyValidateServiceImpl.getEmployeeId`
- `QuitApplyValidateServiceImpl.empApplyValidate`
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.htmtpl.HtmTplEffectOp -->

## chgaction 实证补充（HtmTplEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.htmtpl.HtmTplEffectOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.htmtpl.HtmTplEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.htmtpl.HtmTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyTerminationBatchOp -->

## chgaction 实证补充（QuitApplyTerminationBatchOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplyTerminationBatchOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplyTerminationBatchOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.update  |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.SELECT_FIELD`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`
- `QuitActivityGenerateServiceImpl.initCoopActivity`
- `QuitActivityGenerateServiceImpl.initInterview`
- `QuitActivityGenerateServiceImpl.batchRevocation`
- `QuitActivityGenerateServiceImpl.handleMessage`
- `QuitApplyValidateServiceImpl.class`
- `QuitApplyValidateServiceImpl.batchSubmitValidate`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyTerminationBatchOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationAfterEffectOp -->

## chgaction 实证补充（PerChgTplTerminationAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationAfterEffectOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationAfterEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.termination.PerChgTplTerminationAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyAfterEffectOp -->

## chgaction 实证补充（QuitApplyAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.QuitApplyAfterEffectOp`
> 跨类追踪: 33 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.QuitApplyAfterEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.QuitApplyAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

## chgaction 实证补充（PerChgTplEffectSyncOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalTerminationOp -->

## chgaction 实证补充（ApprovalTerminationOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.apply.ApprovalTerminationOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.apply.ApprovalTerminationOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `htm_quitapplybasebill` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.QuitApplyHelper.queryOn |
| `htm_coophandle` | updateOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CoopHandleRepository.sa |
| `htm_interviewhandle` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.InterviewRepository.sav |
| `htm_workcalendar` | saveOne | HRBaseServiceHelper | kd.hr.htm.business.domain.repository.CalendarRepository.gene |
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

### 调用的核心 Service（Top 10）
- `QuitApplyServiceImpl.class`
- `QuitApplyServiceImpl.handleRevoke`
- `QuitApplyServiceImpl.handleTermination`
- `QuitApplyServiceImpl.handleApplyTermination`
- `QuitApplyServiceImpl.handleApplyRevoke`
- `QuitApplyServiceImpl.terminateWorkFlow`
- `EffectQuitServiceImpl.addToBlackList`
- `QuitApplyServiceImpl.sendGuideMsg`
- `QuitActivityGenerateServiceImpl.class`
- `QuitActivityGenerateServiceImpl.initAllActivity`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.apply.ApprovalTerminationOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.web.BatchCommonOp -->

## chgaction 实证补充（BatchCommonOp 跨类追踪聚合）

> FQN: `kd.hr.htm.opplugin.web.BatchCommonOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.htm.opplugin.web.BatchCommonOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.htm.opplugin.web.BatchCommonOp -->
