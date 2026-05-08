# hrptc_rptdimmap · 业务规则

> **form**：`hrptc_rptdimmap` · 报表控权维度配置
> **生成时间**：2026-04-29
> **方法**：从反编译实物（3 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `rptmanage` | 报表 | `BasedataField` | `hrptmc_reportmanage` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `RptDimMapOp`

- **FQN**：`kd.hr.hrptc.opplugin.web.perm.RptDimMapOp`
- **继承**：`HRDataBaseOp`
- **实现**：`EntityConstants`

**实现的生命周期方法**：`beginOperationTransaction`(L45)

## 三、FormPlugin 类业务规则（UI 联动）

### `RptDimMapEdit`

- **FQN**：`kd.hr.hrptc.formplugin.permission.RptDimMapEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`preOpenForm`(L116), `registerListener`(L110), `propertyChanged`(L243), `beforeF7Select`(L352), `beforeDoOperation`(L183)
- **读字段**（3）：`aoqfield`, `id`, `rptmanage`

### `RptDimMapList`

- **FQN**：`kd.hr.hrptc.formplugin.permission.RptDimMapList`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`beforeDoOperation`(L57)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrptc_rptdimmap/` 的 3 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrptc_rptdimmap.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.permission.RptDimMapEdit -->

## chgaction 实证补充（RptDimMapEdit 跨类追踪聚合）

> FQN: `kd.hr.hrptc.formplugin.permission.RptDimMapEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getPermF |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getPermF |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.isFileSourc |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrcs_entityforbid` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportPermissionService.chec |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.permission.RptDimMapEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.permission.RptDimMapList -->

## chgaction 实证补充（RptDimMapList 跨类追踪聚合）

> FQN: `kd.hr.hrptc.formplugin.permission.RptDimMapList`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.queryRpt |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.queryRpt |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.isFileSourc |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrcs_entityforbid` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportPermissionService.delR |
| `hrptmc_publishmenu` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.upd |
| `hrptmc_reportmanage` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.upd |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.upd |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.permission.RptDimMapList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.opplugin.web.perm.RptDimMapOp -->

## chgaction 实证补充（RptDimMapOp 跨类追踪聚合）

> FQN: `kd.hr.hrptc.opplugin.web.perm.RptDimMapOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.opplugin.web.perm.RptDimMapOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_entityforbid` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportPermissionService.isEx |
| `hrptmc_publishmenu` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.isP |
| `hrptmc_reportmanage` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.isP |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.isP |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getPermF |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getPermF |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.isFileSourc |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | String.valueOf(result.get("message") |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.opplugin.web.perm.RptDimMapOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## chgaction 实证补充（HRDataBaseOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRDataBaseOp_0` | 数据量超过限制阈值%1$s，当前记录数：%2$s。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->
