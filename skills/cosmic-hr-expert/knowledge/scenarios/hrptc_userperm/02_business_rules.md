# hrptc_userperm · 业务规则

> **form**：`hrptc_userperm` · 用户授权管理
> **生成时间**：2026-04-29
> **方法**：从反编译实物（3 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `user` | 用户 | `UserField` | `bos_user` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `ReportUserPermOp`

- **FQN**：`kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp`
- **继承**：`HRDataBaseOp`
- **实现**：`ReportUserPermConstants`

**触发的 Validator**（save/delete 前置校验）：
- `ReportUserPermValidator`

**调用的 Service / Helper**（3 个）：`HRBaseServiceHelper`, `ReportSubscribeConfigService`, `ReportUserPermValidator`

**实现的生命周期方法**：`onAddValidators`(L76), `beforeExecuteOperationTransaction`(L90)

## 三、FormPlugin 类业务规则（UI 联动）

### `ReportUserPermEdit`

- **FQN**：`kd.hr.hrptc.formplugin.perm.ReportUserPermEdit`
- **继承**：`HRBaseDataCommonEdit`
- **生命周期方法**：`propertyChanged`(L243), `beforeDoOperation`(L228), `afterDoOperation`(L360)
- **读字段**（15）：`aoqfield`, `baseDataIds`, `baseDataNumber`, `dimSubGroupId`, `entryentity`, `fieldId`, `id`, `includeSub`

### `ReportUserPermList`

- **FQN**：`kd.hr.hrptc.formplugin.perm.ReportUserPermList`
- **继承**：`AbstractListPlugin`
- **生命周期方法**：`beforeDoOperation`(L53), `afterDoOperation`(L74)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrptc_userperm/` 的 3 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrptc_userperm.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.perm.ReportUserPermEdit -->

## chgaction 实证补充（ReportUserPermEdit 跨类追踪聚合）

> FQN: `kd.hr.hrptc.formplugin.perm.ReportUserPermEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.perm.ReportUserPermEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrptmc_analyseobject` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnalyseObjectService.assembleQue |
| `hrptmc_anobjgroupfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.anobj.AnObjGroupFieldService.getGroupF |
| `hrptmc_reportmanage` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |
| `hrptmc_commonsort` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrptmc.business.repdesign.ReportManageService.getAnObj |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRFormShowParameterService_1` | 值转Long类型错误，值：%1$s，错误信息：%2$s。 |
| `` | "analyse object join conditions is empty." |
| `AnalyseObjectService_2` | 当前分析对象下的%s编码报表存在脏数据，缓存解析失败，请修复数据。 |
| `HRFilterUtil_0` | 日期类型转换错误。 |
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

### 调用的核心 Service（Top 10）
- `anObjEnumFieldHandler.getEnumItems`
- `AnObjEnumFieldHandler.AnObjEnumItem`
- `AnObjEnumFieldHandler.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.perm.ReportUserPermEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.perm.ReportUserPermList -->

## chgaction 实证补充（ReportUserPermList 跨类追踪聚合）

> FQN: `kd.hr.hrptc.formplugin.perm.ReportUserPermList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.perm.ReportUserPermList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.formplugin.perm.ReportUserPermList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp -->

## chgaction 实证补充（ReportUserPermOp 跨类追踪聚合）

> FQN: `kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrptc.opplugin.web.perm.ReportUserPermOp -->
