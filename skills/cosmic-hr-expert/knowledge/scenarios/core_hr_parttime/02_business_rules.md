# core_hr_parttime · 业务规则

> **聚合场景**：调配管理 - 兼职（hdm · 5 单据）（4 个子实体）
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

### `hdm_parttimeapplybill` · ⚠️ 标品 entity_metadata md 不存在

### `hdm_parttimeendbill` · ⚠️ 标品 entity_metadata md 不存在

### `hdm_batchparttime` · ⚠️ 标品 entity_metadata md 不存在

### `hdm_parttimebatch` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgPartBillTplEdit -->

## chgaction 实证补充（PerChgPartBillTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgPartBillTplEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgPartBillTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgPartBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin -->

## chgaction 实证补充（ParttimeApplyBillHeadPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit -->

## chgaction 实证补充（ParttimeApplyBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit`
> 跨类追踪: 31 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |

### 调用的核心 Service（Top 10）
- `ParttimeStatusHandler.getInstance`
- `PartimeBillServiceImpl.class`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`
- `AdminOrgExternalServiceImpl.class`
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit -->

## chgaction 实证补充（ParttimeButtonEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin -->

## chgaction 实证补充（ParttimeSideWorkflowPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin -->

## chgaction 实证补充（ParttimeChgActionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList -->

## chgaction 实证补充（BasePartTimeApplyBillList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList -->

## chgaction 实证补充（ParttimeApplyBillList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

## chgaction 实证补充（HRCommonListFilterPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## chgaction 实证补充（PerChgTplSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartSaveOp -->

## chgaction 实证补充（PartSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartSaveOp`
> 跨类追踪: 34 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonAboutServiceImpl_6` | 获取人员数据失败，请联系管理员。 |
| `PersonAboutServiceImpl_15` | 该员工试用期信息为空，请前往“人员信息>人员列表”维护试用期信息。 |
| `` | errorMsg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonServiceImpl_5` | 请传入有效校验值。 |

### 调用的核心 Service（Top 10）
- `PersonAboutServiceImpl.class`
- `PersonAboutServiceImpl.processSuperAndChargeInfo`
- `PersonAboutServiceImpl.doPersonCrossValidateBatch`
- `PersonAboutServiceImpl.createCommonMsg`
- `PersonAboutServiceImpl.executeReturnMap`
- `PersonAboutServiceImpl.queryTrialPeriodByEmployeeId`
- `AssignmentApplicationServiceImpl.class`
- `EmpPosOrgRelApplicationServiceImpl.class`
- `AdminOrgServiceImpl.class`
- `PositionServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillSubmitOp -->

## chgaction 实证补充（PartBillSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.UnSubmitOp -->

## chgaction 实证补充（UnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.UnSubmitOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.UnSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.UnSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp -->

## chgaction 实证补充（PartBillSubmitAndEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeEffectOp -->

## chgaction 实证补充（PartTimeEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeEffectOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp -->

## chgaction 实证补充（ParttimeEffectRetryOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillDiscardOp -->

## chgaction 实证补充（PartBillDiscardOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillDiscardOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillDiscardOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillDiscardOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp -->

## chgaction 实证补充（PartTimeAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillRollbackOp -->

## chgaction 实证补充（PartBillRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillRollbackOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillRollbackOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartBillRollbackOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditPassOp -->

## chgaction 实证补充（WFAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditPassOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp -->

## chgaction 实证补充（WFRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditPendingOp -->

## chgaction 实证补充（WFAuditPendingOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditPendingOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPendingOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditPendingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp -->

## chgaction 实证补充（WFAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp -->

## chgaction 实证补充（PartTimeAfterRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp -->

## chgaction 实证补充（ParttimeTerminalDoOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp -->

## chgaction 实证补充（PerChgEndTplFinishSupRelOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp -->

## chgaction 实证补充（PartTimeEndAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit -->

## chgaction 实证补充（BatchParttimeEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit -->

## chgaction 实证补充（BatchParttimeHeadEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit -->

## chgaction 实证补充（BatchParttimeButtonEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit -->

## chgaction 实证补充（BatchPartTimeEntryImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit -->

## chgaction 实证补充（BatchParttimeEntryEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `InvokeHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit -->

## chgaction 实证补充（BatchParttimeTerminateEdit 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin -->

## chgaction 实证补充（BatchParttimeChgActionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList -->

## chgaction 实证补充（BatchParttimeList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_batchparttime` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeBatchR |
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList -->

## chgaction 实证补充（BatchParttimeTerminateList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_batchparttime` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeBatchR |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList -->

## chgaction 实证补充（BatchParttimeRollBackList 跨类追踪聚合）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchPartSaveOp -->

## chgaction 实证补充（BatchPartSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchPartSaveOp`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartSaveOp/`

### 调用的核心 Service（Top 10）
- `AdminOrgExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchPartSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp -->

## chgaction 实证补充（BatchParttimeSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errInfo |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp -->

## chgaction 实证补充（BatchParttimeEventOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |
| `hdm_batchparttime` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeBatchR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp -->

## chgaction 实证补充（BatchParttimeSubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errInfo |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp -->

## chgaction 实证补充（BatchPartBillRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |
| `hdm_batchparttime` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeBatchR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp -->

## chgaction 实证补充（BatchParttimeTerminateDoOp 跨类追踪聚合）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hdm_batchparttime` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeBatchR |
| `hdm_parttimeapplybill` | updateOne | HRBaseServiceHelper | kd.hr.hdm.business.domain.parttime.repository.ParttimeApplyR |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `PersonExternalServiceImpl_0` | 获取系统人员异常，请联系管理员。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PartimeBillServiceImpl.class`
- `ParttimeStatusHandler.getInstance`
- `ParttimeAdminOrgExternalServiceImpl.class`
- `InvokeHandler.invokeService`
- `JobExternalServiceImpl.class`
- `PersonExternalServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp -->
