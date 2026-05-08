# core_hr_promotion · 业务规则

> **聚合场景**：试用和转正（hdm · 自助/批量/补录 6 单据）（3 个子实体）
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

### `hdm_regselfhelpbill` · ⚠️ 标品 entity_metadata md 不存在

### `hdm_regbasebill` · ⚠️ 标品 entity_metadata md 不存在

### `hdm_batchregbill` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin -->

## chgaction 实证补充（RegBaseBillPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin`
> 跨类追踪: 27 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `RegPeronalBillHelper_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `RegProbationServiceImpl.class`
- `RegDateServiceImpl.class`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin -->

## chgaction 实证补充（RegSingleHrBillPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin -->

## chgaction 实证补充（SideWorkflowPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin -->

## chgaction 实证补充（RegChgAffactionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin -->

## chgaction 实证补充（WebRegSelfHelpPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin`
> 跨类追踪: 32 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | customErrorMsg |
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `RegPeronalBillHelper_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

## chgaction 实证补充（HRTemplateBillList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

## chgaction 实证补充（PerChgNewBillTplList 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplList`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList -->

## chgaction 实证补充（RegularApplySourceList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `RegWorkFlowServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin -->

## chgaction 实证补充（TerminateWorkFlowListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin/`

### 调用的核心 Service（Top 10）
- `RegWorkFlowServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin -->

## chgaction 实证补充（RegBaseBillMobPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin`
> 跨类追踪: 38 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegPeronalBillHelper_0` | 获取系统人员异常，请联系管理员。 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `` | errorMsg |
| `PersonServiceImpl_5` | 请传入有效校验值。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`
- `AssignmentApplicationServiceImpl.class`
- `EmpPosOrgRelApplicationServiceImpl.class`
- `EmployeeApplicationServiceImpl.class`
- `AssignmentAttApplicationServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin -->

## chgaction 实证补充（RegSelfHelpMobPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin`
> 跨类追踪: 33 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `RegApplyValidatorHelper_3` | 调用人事事务变动接口返回结果异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin -->

## chgaction 实证补充（RegMobileChgAffactionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## chgaction 实证补充（PerChgTplSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSaveOp -->

## chgaction 实证补充（RegSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSaveOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `` | "orgIds.size()>5000" |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp -->

## chgaction 实证补充（RegSaveOpenAPIOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp`
> 跨类追踪: 27 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSubmitOp -->

## chgaction 实证补充（RegSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSubmitOp`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp -->

## chgaction 实证补充（RegSelfSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp -->

## chgaction 实证补充（RegUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp -->

## chgaction 实证补充（RegSubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp -->

## chgaction 实证补充（RegRejectToSubmitEventOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp -->

## chgaction 实证补充（RegWfAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegEffectOp -->

## chgaction 实证补充（RegEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegEffectOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp -->

## chgaction 实证补充（RegEffectRetryOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `IRegEffectService.getInstance`
- `RegBillServiceImpl.class`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.RegAfterEffectOp -->

## chgaction 实证补充（RegAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.reg.RegAfterEffectOp`
> 跨类追踪: 13 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegAfterEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.RegAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

## chgaction 实证补充（PerChgTplRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | result.getMessage() |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp -->

## chgaction 实证补充（RegRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp -->

## chgaction 实证补充（RegSelfSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `` | "orgIds.size()>5000" |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp -->

## chgaction 实证补充（RegAppBillTerminateOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | allErrorOrValidateInfo |
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp -->

## chgaction 实证补充（RegRemindConfirmDelayOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp/`

### 调用的核心 Service（Top 10）
- `RegWorkFlowServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp -->

## chgaction 实证补充（RegRpcDonothingOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp -->

## chgaction 实证补充（RegWfRemindOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp/`

### 调用的核心 Service（Top 10）
- `RegWorkFlowServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp -->

## chgaction 实证补充（RegBillTerminateDoOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | allErrorOrValidateInfo |
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp -->

## chgaction 实证补充（RegHrSupplySubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `RegProbationServiceImpl.class`
- `RegDateServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp -->

## chgaction 实证补充（RegHrSupplementOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit -->

## chgaction 实证补充（BatchRegHeadEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit -->

## chgaction 实证补充（BatchRegButtonEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit -->

## chgaction 实证补充（BatchRegBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin -->

## chgaction 实证补充（BatchRegChgAffactionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

## chgaction 实证补充（HRPermCommonList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegList -->

## chgaction 实证补充（BatchRegList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegList`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.reg.web.batch.BatchRegList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp -->

## chgaction 实证补充（BatchRegApplyBillSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `` | "orgIds.size()>5000" |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp -->

## chgaction 实证补充（BatchRegApplyBillSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | allErrorOrValidateInfo |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp -->

## chgaction 实证补充（BatchRegUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp -->

## chgaction 实证补充（BatchRegApplyBillSubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | allErrorOrValidateInfo |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp -->

## chgaction 实证补充（BatchRegWfRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp -->

## chgaction 实证补充（BatchRegWfAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp -->

## chgaction 实证补充（BatchRegWfAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.BatchRegTerminateOp -->

## chgaction 实证补充（BatchRegTerminateOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.reg.BatchRegTerminateOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.BatchRegTerminateOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | allErrorOrValidateInfo |
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.reg.BatchRegTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp -->

## chgaction 实证补充（BatchRegRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp -->

## chgaction 实证补充（BatchRegTerminateDoOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `RegBillServiceImpl_29` | 其他用户正在为该员工发起转正申请，请稍后重试。 |
| `RegBillServiceImpl_15` | “实际转正日期”为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `RegBillServiceImpl.class`
- `IRegEffectService.getInstance`
- `RegEffectServiceImpl.class`
- `RegEffectServiceImpl.assembleHpfsParam`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp -->
