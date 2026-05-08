# hrptmc_reportmanage · 业务规则

> **form**：`hrptmc_reportmanage` · 报表管理
> **生成时间**：2026-04-29
> **方法**：从反编译实物（4 类）提取真业务规则 + standard md 字段必填约束

## 二、OP 类业务规则（反编译实证）

### `ReportManageOp`

- **FQN**：`kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp`
- **继承**：`HRDataBaseOp`
- **实现**：`AnalyseObjectConstants`, `ReportManageConstants`

**触发的 Validator**（save/delete 前置校验）：
- `ReportValidator`

**调用的 Service / Helper**（2 个）：`HRBaseServiceHelper`, `ReportValidator`

**实现的生命周期方法**：`onAddValidators`(L192), `beginOperationTransaction`(L200), `afterExecuteOperationTransaction`(L318)

**KDBizException 抛出点**：2 处（业务校验失败/前置条件不满足时主动抛错）

## 三、FormPlugin 类业务规则（UI 联动）

### `ReportManageEditPlugin`

- **FQN**：`kd.hr.hrptmc.formplugin.web.repdesign.ReportManageEditPlugin`
- **继承**：`ReportManageServicePlugin`
- **生命周期方法**：`afterBindData`(L235), `afterCreateNewData`(L155), `beforeDoOperation`(L256), `afterDoOperation`(L318)
- **读字段**（12）：`anobjid`, `anobjid.id`, `cloudid`, `cloudid.id`, `createorg.id`, `description`, `id`, `locale`
- **调用的 Service**：`ReportConfigService`

### `ReportManageList`

- **FQN**：`kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList`
- **继承**：`StandardTreeListPlugin`
- **生命周期方法**：`registerListener`(L181), `beforeDoOperation`(L240), `afterDoOperation`(L306), `itemClick`(L328)
- **读字段**（15）：`anobjid`, `appsrc`, `cloud.id`, `cloudid`, `createorg`, `description`, `id`, `isendgroup`
- **调用的 Service**：`HRBaseServiceHelper`, `PresetIndexServiceHelper`

### `ReportManagePreSqlExportList`

- **FQN**：`kd.hr.hrptmc.formplugin.web.export.sql.ReportManagePreSqlExportList`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`afterDoOperation`(L31)
- **调用的 Service**：`HRReportMetaDataHelper`, `HRReportPreSQLHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrptmc_reportmanage/` 的 4 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrptmc_reportmanage.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.repdesign.ReportManageEditPlugin -->

## chgaction 实证补充（ReportManageEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.repdesign.ReportManageEditPlugin`
> 跨类追踪: 32 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.repdesign.ReportManageEditPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_filesourceenum` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.filesource.ReportFileSourceService.cle |
| `hrptmc_filesourcetable` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.filesource.FileSourceTableMsgService.q |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.loadAnObjDy |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_reportjump` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService |
| `hrptmc_publishmenu` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.get |
| `hrptmc_reportmanage` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.get |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.publish.HRReportPublishMenuService.get |
| `hrptmc_rptdispscm` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.RptDisplaySchemeService.upda |
| `hrptmc_queryscheme` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportPreViewService.saveSch |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | args.getOperationResult().getMessage() |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `ReportJumpConfigService_2` | 组织长编码为空，无法查询下级组织。 |

### 调用的核心 Service（Top 10）
- `ReportFileSourceCustomSortHandler.handleCustomSortBeforeCloseReportPage`
- `preIndexHandler.getPresetIndexByPageCache`
- `tableHandler.deleteTable`
- `tableHandler.createTable`
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`
- `enumFieldHandler.getEnumItems`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.repdesign.ReportManageEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList -->

## chgaction 实证补充（ReportManageList 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList`
> 跨类追踪: 23 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.isReport |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.isReport |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.isFileSourc |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |

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

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.repdesign.ReportManageList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.export.sql.ReportManagePreSqlExportList -->

## chgaction 实证补充（ReportManagePreSqlExportList 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.formplugin.web.export.sql.ReportManagePreSqlExportList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.formplugin.web.export.sql.ReportManagePreSqlExportList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.formplugin.web.export.sql.ReportManagePreSqlExportList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp -->

## chgaction 实证补充（ReportManageOp 跨类追踪聚合）

> FQN: `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_reportconfig` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_filter` | saveOne | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_algorithmcol` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_rowfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_colfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_reportpreindex` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_dimmap` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_dimmap` | saveOne | HRBaseServiceHelper | <self> (depth=0) |
| `hrptmc_filesourceenum` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.filesource.ReportFileSourceService.cle |
| `hrptmc_filesourcetable` | saveOne | HRBaseServiceHelper | kd.hr.hrptmc.business.filesource.FileSourceTableMsgService.q |
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.loadAnObjDy |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_reportjump` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.jump.ReportJumpConfigService |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ReportManageOp_1` | 保存失败 |
| `ReportManageOp_2` | 删除失败 |
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `ReportJumpConfigService_2` | 组织长编码为空，无法查询下级组织。 |
| `ReportHisVersionService_1` | 保存失败，报表配置版本记录异常。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `tableHandler.deleteTable`
- `tableHandler.createTable`
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp -->
