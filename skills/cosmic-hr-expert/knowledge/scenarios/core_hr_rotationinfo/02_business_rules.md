# core_hr_rotationinfo · 业务规则

> **form**：`hrpi_rotationinfo` · 轮岗情况
> **生成时间**：2026-04-29
> **方法**：从反编译实物（12 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `startdate` | 开始日期 | `DateField` | — |
| `enddate` | 结束日期 | `DateField` | — |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `AssignmentTplCommonSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`onAddValidators`(L26)

### `ChgRecordSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（2 个）：`HRBaseServiceHelper`, `OperationService`

### `EmployeeCommonStandardMustInputOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`EmployeeCommonStandardMustInputValidator`

**实现的生命周期方法**：`onAddValidators`(L26)

### `IgnoreReferenceDeleteOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`onAddValidators`(L17)

### `RotationSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.assignment.attach.RotationSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（3 个）：`BelongCompanyValidator`, `PositionAndJobMatchValidator`, `PositionValidator`

**实现的生命周期方法**：`onAddValidators`(L40)

### `TimeLineCommonSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`TimeLineForbiddenCoverValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `TimelineLogOp`

- **FQN**：`kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp`
- **继承**：`AbstractOperationServicePlugIn`
- **实现**：`TimelineConstants`

**实现的生命周期方法**：`beginOperationTransaction`(L47), `afterExecuteOperationTransaction`(L75)

### `TimelineTplOp`

- **FQN**：`kd.hr.hbp.opplugin.web.timeline.TimelineTplOp`
- **继承**：`HRDataBaseOp`
- **实现**：`TimelineConstants`

**触发的 Validator**（save/delete 前置校验）：
- `TimelineValidator`

**调用的 Service / Helper**（1 个）：`TimelineValidator`

**实现的生命周期方法**：`onAddValidators`(L90), `beginOperationTransaction`(L109), `afterExecuteOperationTransaction`(L141)

**KDBizException 抛出点**：2 处（业务校验失败/前置条件不满足时主动抛错）

## 三、FormPlugin 类业务规则（UI 联动）

### `EmployeeAuditCommonOP`

- **FQN**：`kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP`
- **继承**：`HRDataBaseOp`
- **生命周期方法**：`onAddValidators`(L79), `beforeExecuteOperationTransaction`(L86), `beginOperationTransaction`(L102)
- **读字段**（1）：`id`
- **调用的 Service**：`EmployeeAuditRejectDataValidator`, `EmployeeAuditValidator`

### `EmpStageHandleOpPlugin`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin`
- **继承**：`HRDataBaseOp`
- **读字段**（1）：`employee.id`
- **调用的 Service**：`HRBaseServiceHelper`

### `TimelineTplFormEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`afterBindData`(L87), `afterCreateNewData`(L82), `propertyChanged`(L101), `beforeDoOperation`(L115), `afterDoOperation`(L137)
- **调用的 Service**：`TimelineCommonValidator`

### `TimelineTplListPlugin`

- **FQN**：`kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`beforeDoOperation`(L111), `afterDoOperation`(L122)
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_rotationinfo/` 的 12 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrpi_rotationinfo.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit -->

## chgaction 实证补充（TimelineTplFormEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin -->

## chgaction 实证补充（TimelineTplListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `TimeLinePluginUtil_1` | 当前实体未配置时间段约束模式或逻辑主键字段，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timeLineHandler.deleteBatch`
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList -->

## chgaction 实证补充（TimelineLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

## chgaction 实证补充（TimelineTplOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp`
> 跨类追踪: 13 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.TimelineTplOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `TimelineTplOp_2` | 删除失败。 |
| `TimelineTplOp_1` | 保存失败。 |
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

## chgaction 实证补充（TimelineLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp/`

### 调用的核心 Service（Top 10）
- `timeLineLogHandler.buildModifyContent`
- `timeLineLogHandler.setModifyInfoMap`
- `timeLineLogHandler.batchInsertLog`
- `timeLineLogHandler.getModifyInfoMap`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp -->

## chgaction 实证补充（TimeLineCommonSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp -->

## chgaction 实证补充（AssignmentTplCommonSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg |
| `DynamicTransformUtil_1` | 获取属性失败。 |
| `DynamicTransformUtil_2` | 返回结果失败。 |
| `DynamicTransformUtil_3` | 转换失败! |
| `DynamicTransformUtil_4` | 获取属性失败。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonServiceImpl_5` | 请传入有效校验值。 |

### 调用的核心 Service（Top 10）
- `AssignmentApplicationServiceImpl.class`
- `EmpPosOrgRelApplicationServiceImpl.class`
- `AdminOrgServiceImpl.class`
- `HbpmServiceImpl.getAdminOrgHis`
- `HbpmServiceImpl.queryOrgDetailDys`
- `PositionServiceImpl.class`
- `HbpmServiceImpl.getHisPos`
- `JobServiceImpl.class`
- `HbpmServiceImpl.getJobHis`
- `ChangePersonServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

## chgaction 实证补充（ChgRecordSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `DevParamConfigDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin -->

## chgaction 实证补充（EmpStageHandleOpPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `EmpStageApplicationServiceImpl.class`
- `EmpStageApplicationServiceImpl.getEmpStageByDate`
- `EmpStageApplicationServiceImpl.getClosestEmpStages`
- `EmpStageApplicationServiceImpl.getEmpStageConfigMaps`
- `RedundantLogHandler.writeLog`
- `EmpStageDomainServiceImpl.class`
- `RedundantLogHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.attach.RotationSaveOp -->

## chgaction 实证补充（RotationSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.attach.RotationSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.attach.RotationSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.attach.RotationSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->

## chgaction 实证补充（IgnoreReferenceDeleteOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter -->

## chgaction 实证补充（TimelineF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter -->
