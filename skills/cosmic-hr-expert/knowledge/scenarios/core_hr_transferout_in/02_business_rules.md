# core_hr_transferout_in · 业务规则

> **聚合场景**：调配管理 - 调出/调入（hdm · 跨组织协同 4 form）（1 个子实体）
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

### `hdm_transferapply` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgHdmBillTplEdit -->

## chgaction 实证补充（PerChgHdmBillTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgHdmBillTplEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgHdmBillTplEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgHdmBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

## chgaction 实证补充（CommonPermissionEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillHeadEdit -->

## chgaction 实证补充（TransferBillHeadEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferBillHeadEdit`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferBillHeadEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillPropChangedEdit -->

## chgaction 实证补充（TransferBillPropChangedEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferBillPropChangedEdit`
> 跨类追踪: 31 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferBillPropChangedEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `EmployeeHRF7Handler_0` | 无法获取到人员卡片信息，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `EntryPropertyChangedHandler.propertyChange`
- `EntryPropertyChangedHandler.class`
- `AbaseLocationHandler.getInstance`
- `AcounytryHandler.getInstance`
- `AcompanyHandler.getInstance`
- `ApoistionHandler.getInstance`
- `AorgHandler.getInstance`
- `PlandateHandler.getInstance`
- `TransferdateHandler.getInstance`
- `AffactionHandler.getInstance`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillPropChangedEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferDateEdit -->

## chgaction 实证补充（TransferDateEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferDateEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferDateEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `TransferPersonChangeServiceImpl.class`
- `PersonalChangeExternalServiceImpl.class`
- `TransferConfirmValidatorServiceImpl.class`
- `TransferConfirmValidatorServiceImpl.getDevConfigFields`
- `DevParamConfigExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferDateEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillViewEdit -->

## chgaction 实证补充（TransferBillViewEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferBillViewEdit`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferBillViewEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillViewEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferButtonEdit -->

## chgaction 实证补充（TransferButtonEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferButtonEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferButtonEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferOperationEdit -->

## chgaction 实证补充（TransferOperationEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferOperationEdit`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferOperationEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferOperationEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.billin.TransferInViewEdit -->

## chgaction 实证补充（TransferInViewEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.billin.TransferInViewEdit`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.billin.TransferInViewEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.billin.TransferInViewEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.billin.TransferStageInViewEdit -->

## chgaction 实证补充（TransferStageInViewEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.billin.TransferStageInViewEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.billin.TransferStageInViewEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.billin.TransferStageInViewEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillEdit -->

## chgaction 实证补充（TransferApplyBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillEdit`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

## chgaction 实证补充（CommonPermissionList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillList -->

## chgaction 实证补充（TransferBillList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.common.TransferBillList`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.common.TransferBillList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferValidatorServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.common.TransferBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillList -->

## chgaction 实证补充（TransferApplyBillList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillList`
> 跨类追踪: 23 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferValidatorServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.transfer.web.apply.TransferApplyBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

## chgaction 实证补充（HRCommonListFilterPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferDeleteOp -->

## chgaction 实证补充（TransferDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferDeleteOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## chgaction 实证补充（PerChgTplSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferSaveOp -->

## chgaction 实证补充（TransferSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferSaveOp`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `TransferOpenApiSaveParamsServiceImpl_3` | 调动前调动前评定职位(bevaluationjob)为空，请检查 |
| `ransferOpenApiSaveParamsServiceImpl_\uff15` | {0}不在{1}范围内，{2}:{3}****\n*****{4}:{5} |
| `TransferOpenApiSaveParamsServiceImpl_6` | 公司内调动时，调入公司应与调出公司相同，请重新选择调入部门。 |
| `TransferOpenApiSaveParamsServiceImpl_7` | 跨公司调动时，调入公司应与调出公司不同，请重新选择调入部门。 |
| `` | errorMsg |
| `TransferOpenApiSaveParamsServiceImpl_1` | 无法获取到人员卡片信息，请联系管理员。 |
| `PersonServiceImpl_5` | 请传入有效校验值。 |
| `DynamicTransformUtil_1` | 获取属性失败。 |
| `DynamicTransformUtil_2` | 返回结果失败。 |
| `DynamicTransformUtil_3` | 转换失败! |
| `DynamicTransformUtil_4` | 获取属性失败。 |

### 调用的核心 Service（Top 10）
- `EmployeeDomainServiceImpl.class`
- `EmployeeDomainServiceImpl.getBosUserInfo`
- `EmployeeDomainServiceImpl.getBosUserByTermSql`
- `EmployeeDomainServiceImpl.appendInParam`
- `TransferConfirmValidatorServiceImpl.class`
- `TransferConfirmValidatorServiceImpl.getDevConfigFields`
- `DevParamConfigExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplySubmitOp -->

## chgaction 实证补充（TransferApplySubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferApplySubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferApplySubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplySubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferUnSubmitOp -->

## chgaction 实证补充（TransferUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferUnSubmitOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferUnSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferStaffServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferUnSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgHDMTplEffectOp -->

## chgaction 实证补充（PerChgHDMTplEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgHDMTplEffectOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgHDMTplEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgHDMTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewPerSerLenOp -->

## chgaction 实证补充（PerChgEffectNewPerSerLenOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewPerSerLenOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewPerSerLenOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewPerSerLenOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferEffectOp -->

## chgaction 实证补充（TransferEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferEffectOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `DevParamConfigExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewTrialPeriodsOp -->

## chgaction 实证补充（PerChgEffectNewTrialPeriodsOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewTrialPeriodsOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewTrialPeriodsOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEffectNewTrialPeriodsOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgHdmTplFinishSupRelOp -->

## chgaction 实证补充（PerChgHdmTplFinishSupRelOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgHdmTplFinishSupRelOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgHdmTplFinishSupRelOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgHdmTplFinishSupRelOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplyAuditingOp -->

## chgaction 实证补充（TransferApplyAuditingOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferApplyAuditingOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferApplyAuditingOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplyAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplyRejectToSubmitOp -->

## chgaction 实证补充（TransferApplyRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferApplyRejectToSubmitOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferApplyRejectToSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |
| `hdm_transferbatch` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBatchR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | errInfo |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferStaffServiceImpl.class`
- `TransferBillDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApplyRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferTerminalDoOp -->

## chgaction 实证补充（TransferTerminalDoOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferTerminalDoOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferTerminalDoOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferTerminalDoOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferEffectRetryOp -->

## chgaction 实证补充（TransferEffectRetryOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferEffectRetryOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferEffectRetryOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferFlowTerminalOp -->

## chgaction 实证补充（TransferFlowTerminalOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferFlowTerminalOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferFlowTerminalOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferFlowTerminalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp -->

## chgaction 实证补充（TransferAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | ex.getMessage() |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferStaffServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferAfterEffectOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferRollbackOp -->

## chgaction 实证补充（TransferRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferRollbackOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferRollbackOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferRollbackOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApprovalAgreeOp -->

## chgaction 实证补充（TransferApprovalAgreeOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferApprovalAgreeOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferApprovalAgreeOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferApprovalAgreeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferAfterRollbackOp -->

## chgaction 实证补充（TransferAfterRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferAfterRollbackOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferAfterRollbackOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferAfterRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferTerminateOp -->

## chgaction 实证补充（TransferTerminateOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferTerminateOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferTerminateOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 占编异常，请联系编制管理员。 |
| `TransferStaffServiceImpl_1` | 释放编制异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `TransferStaffServiceImpl.class`
- `TransferStaffServiceImpl.map`
- `TransferStaffServiceImpl.getDimensionValue`
- `TransferStaffServiceImpl.getRealOrgTeamId`
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferRejectOp -->

## chgaction 实证补充（TransferRejectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferRejectOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferRejectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_transferbasebill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.transfer.repository.TransferBillRe |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `TransferBillServiceImpl.class`
- `TransferBillServiceImpl.getBaseServiceHelper`
- `PersonExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`
- `PositionExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `JobExternalServiceImpl.class`
- `TransferStaffServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferRejectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferConfirmOp -->

## chgaction 实证补充（TransferConfirmOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.transfer.TransferConfirmOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.transfer.TransferConfirmOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errInfo |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.transfer.TransferConfirmOp -->
