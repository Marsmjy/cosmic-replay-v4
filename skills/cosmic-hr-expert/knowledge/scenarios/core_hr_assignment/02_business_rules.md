# core_hr_assignment · 业务规则

> **form**：`hrpi_assignment` · 组织分配
> **生成时间**：2026-04-29
> **方法**：从反编译实物（12 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 组织分配号 | `TextField` | — |
| `org` | 人事管理组织 | `OrgField` | `bos_org` |
| `orgtype` | 组织分类 | `BasedataField` | `haos_otclassify` |
| `businesstype` | 业务类型 | `BasedataField` | `hbss_bussinessfield` |
| `adminorg` | 管理部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `employee` | 员工 | `BasedataField` | `hrpi_employee` |
| `startdate` | 开始日期 | `DateField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `AssignmentDeleteOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`onAddValidators`(L25), `beginOperationTransaction`(L33)

### `AssignmentSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`AssignmentMagSaveValidator`

**实现的生命周期方法**：`onAddValidators`(L62), `beforeExecuteOperationTransaction`(L81), `beginOperationTransaction`(L66)

**KDBizException 抛出点**：1 处（业务校验失败/前置条件不满足时主动抛错）

### `ChgRecordSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（2 个）：`HRBaseServiceHelper`, `OperationService`

### `EmployeeCommonStandardMustInputOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`EmployeeCommonStandardMustInputValidator`

**实现的生命周期方法**：`onAddValidators`(L26)

### `EmployeeDeleteOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.employee.EmployeeDeleteOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`onAddValidators`(L23), `beginOperationTransaction`(L27)

### `EmployeeNumberCodeRuleDeleteOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.employee.EmployeeNumberCodeRuleDeleteOp`
- **继承**：`CodeRuleDeleteOp`

### `EmployeeNumberCodeRuleOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.employee.EmployeeNumberCodeRuleOp`
- **继承**：`CodeRuleOp`

### `EmployeeSaveOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.employee.EmployeeSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（2 个）：`HRBaseServiceHelper`, `OperationService`

**实现的生命周期方法**：`onAddValidators`(L107), `beforeExecuteOperationTransaction`(L125), `beginOperationTransaction`(L141), `afterExecuteOperationTransaction`(L236)

### `InfoCollectStartOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `InfoCollectHiredValidator`

**调用的 Service / Helper**（1 个）：`InfoCollectHiredValidator`

**实现的生命周期方法**：`onAddValidators`(L35), `afterExecuteOperationTransaction`(L40)

### `OperateLogOp`

- **FQN**：`kd.hrmp.hrpi.opplugin.web.log.OperateLogOp`
- **继承**：`AbstractOperationServicePlugIn`

**实现的生命周期方法**：`beginOperationTransaction`(L43), `afterExecuteOperationTransaction`(L65)

## 三、FormPlugin 类业务规则（UI 联动）

### `EmployeeNumberCodeRulePlugin`

- **FQN**：`kd.hrmp.hrpi.formplugin.web.employee.EmployeeNumberCodeRulePlugin`
- **继承**：`CodeRulePlugin`

### `EmployeePlugin`

- **FQN**：`kd.hrmp.hrpi.formplugin.web.person.EmployeePlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`propertyChanged`(L39), `beforeDoOperation`(L50)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_assignment/` 的 12 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrpi_assignment.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp -->

## chgaction 实证补充（AssignmentSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp/`

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

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.AssignmentSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.log.OperateLogOp -->

## chgaction 实证补充（OperateLogOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.log.OperateLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.log.OperateLogOp/`

### 调用的核心 Service（Top 10）
- `operateLogHandler.buildModifyContent`
- `operateLogHandler.setModifyInfoMap`
- `operateLogHandler.batchInsertLog`
- `operateLogHandler.getModifyInfoMap`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.log.OperateLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp -->

## chgaction 实证补充（EmployeeCommonStandardMustInputOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp/`

### 调用的核心 Service（Top 10）
- `DevParamConfigServiceImpl.getInstance`
- `DevParamConfigServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp -->

## chgaction 实证补充（AssignmentDeleteOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp/`

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

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.AssignmentDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp -->

## chgaction 实证补充（InfoCollectStartOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.assignment.infocollect.InfoCollectStartOp -->
