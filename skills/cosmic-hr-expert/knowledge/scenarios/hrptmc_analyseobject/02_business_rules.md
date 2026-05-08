# hrptmc_analyseobject · 业务规则

> **form**：`hrptmc_analyseobject` · 分析对象
> **生成时间**：2026-04-29
> **方法**：从反编译实物（6 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 对象编码 | `TextField` | — |
| `name` | 对象名称 | `MuliLangTextField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `ReportAnalyseObjectOp`

- **FQN**：`kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp`
- **继承**：`HRDataBaseOp`
- **实现**：`AnalyseObjectConstants`

**触发的 Validator**（save/delete 前置校验）：
- `ReportAnalyseObjectValidator`

**拦截的 opKey 分支**（在 beginOperationTransaction 里分流处理）：
- `delete`
- `save`

**调用的 Service / Helper**（3 个）：`AnalyseObjectService`, `HRBaseServiceHelper`, `ReportAnalyseObjectValidator`

**异步任务**（JobClient.dispatch）：
- `kd.hr.hrptmc.business.anobj.AnalyseObjectDimCountTask`

> ⚠️ 该 OP 走 sch_task 异步处理 · ISV 关注后置任务可能影响事务边界

**实现的生命周期方法**：`onAddValidators`(L103), `beginOperationTransaction`(L108), `afterExecuteOperationTransaction`(L117)

## 三、FormPlugin 类业务规则（UI 联动）

### `AnalyseObjectCalculateConfigEdit`

- **FQN**：`kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCalculateConfigEdit`
- **继承**：`AnalyseObjectCommonEdit`
- **调用的 Service**：`CalculateFieldService`

### `AnalyseObjectDataProcessEdit`

- **FQN**：`kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectDataProcessEdit`
- **继承**：`AnalyseObjectCommonEdit`
- **生命周期方法**：`beforeDoOperation`(L349)

### `AnalyseObjectListPlugin`

- **FQN**：`kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectListPlugin`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`beforeDoOperation`(L108), `afterDoOperation`(L137)
- **读字段**（2）：`name`, `number`

### `AnalyseObjectPreSqlListPlugin`

- **FQN**：`kd.hr.hrptmc.formplugin.web.export.sql.AnalyseObjectPreSqlListPlugin`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`afterDoOperation`(L29)
- **调用的 Service**：`HRReportPreSQLHelper`

### `ReportAnalyseObjectEdit`

- **FQN**：`kd.hr.hrptmc.formplugin.web.anobj.ReportAnalyseObjectEdit`
- **继承**：`AnalyseObjectCommonEdit`
- **生命周期方法**：`afterBindData`(L129), `beforeDoOperation`(L168), `afterDoOperation`(L215)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrptmc_analyseobject/` 的 6 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrptmc_analyseobject.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.ReportAnalyseObjectEdit -->

## chgaction 实证补充（ReportAnalyseObjectEdit 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.anobj.ReportAnalyseObjectEdit`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.anobj.ReportAnalyseObjectEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.ReportAnalyseObjectEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectDataProcessEdit -->

## chgaction 实证补充（AnalyseObjectDataProcessEdit 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectDataProcessEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectDataProcessEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_anobjsidebar` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjSideBarService.newSideBarTo |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.getRefRepor |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRFilterUtil_0` | 日期类型转换错误。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectDataProcessEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCalculateConfigEdit -->

## chgaction 实证补充（AnalyseObjectCalculateConfigEdit 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCalculateConfigEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCalculateConfigEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.queryAndAss |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectCalculateConfigEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectListPlugin -->

## chgaction 实证补充（AnalyseObjectListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectListPlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.getAnObjDy  |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRFilterUtil_0` | 日期类型转换错误。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.anobj.AnalyseObjectListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.export.sql.AnalyseObjectPreSqlListPlugin -->

## chgaction 实证补充（AnalyseObjectPreSqlListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.export.sql.AnalyseObjectPreSqlListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.export.sql.AnalyseObjectPreSqlListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.export.sql.AnalyseObjectPreSqlListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp -->

## chgaction 实证补充（ReportAnalyseObjectOp 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_anobjjoinentity` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_anobjqueryfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_anobjentityrel` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_calculatefield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.queryAndAss |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_anobjsidebar` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjSideBarService.saveSideBars |
| `hrptmc_permrule` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjPermRuleService.copyPermRul |
| `hrptmc_anobjconfighis` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjHisVersionService.saveAnObj |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `AnObjHisVersionService_1` | 保存失败，分析对象配置版本记录异常。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp -->
