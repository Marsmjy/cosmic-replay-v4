# hrcs_warnscheme · 业务规则

> **form**：`hrcs_warnscheme` · 预警方案
> **生成时间**：2026-04-29
> **方法**：从反编译实物（12 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `repeatperiod` | 重复周期 | `ComboField` | — |
| `weekday` | 指定时间 | `MulComboField` | — |
| `monthday` | 指定时间（日期） | `MulComboField` | — |
| `pushchannel` | 推送渠道 | `MulComboField` | — |
| `warnscene` | 预警场景 | `BasedataField` | `hrcs_warnscene` |
| `createorg` | 创建组织 | `OrgField` | `bos_org` |
| `timezone` | 时区 | `BasedataField` | `inte_timezone` |
| `daterangefield` | 预警日期范围 | `DateRangeField` | — |
| `warntype` | 预警对象类型 | `ComboField` | — |
| `warnbizobj` | 业务对象 | `BasedataField` | `hbp_entityobject` |
| `monitortime` | 监控时间（按小时） | `MulComboField` | — |
| `monthweek` | 指定时间（星期） | `MulComboField` | — |
| `monthweekday` | 指定星期 | `MulComboField` | — |
| `customcron` | cron表达式 | `TextField` | — |
| `msgreceivertype` | 消息接收人类型 | `CheckBoxGroupField` | — |
| `localeid` | 通知语言 | `MulComboField` | — |
| `rangetype` | 周期设定 | `ComboField` | — |
| `monitorstartdate` | 开始日期 | `DateField` | — |
| `rcrelationship` | 人员关系 | `MulComboField` | — |
| `serviceclass` | 服务类 | `TextField` | — |
| `plugindesc` | 描述 | `MuliLangTextField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `WarnReceiverSaveOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`onAddValidators`(L17)

### `WarnSchemeAdConditionOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp`
- **继承**：`WarnAdOp`
- **实现**：`WarnAdConditionConstants`

### `WarnSchemeBaseConditionOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp`
- **继承**：`HRDataBaseOp`
- **实现**：`WarnSchemeBaseConditionConstants`

**调用的 Service / Helper**（1 个）：`WarnSchemeBaseConditionValidator`

**实现的生命周期方法**：`onAddValidators`(L31)

### `WarnSchemeLogOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp`
- **继承**：`AbstractOperationServicePlugIn`

**实现的生命周期方法**：`beforeExecuteOperationTransaction`(L28), `beginOperationTransaction`(L32), `afterExecuteOperationTransaction`(L45)

### `WarnSchemeOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `WarnSchemeValidator`

**调用的 Service / Helper**（1 个）：`WarnSchemeValidator`

**实现的生命周期方法**：`onAddValidators`(L30), `beginOperationTransaction`(L35)

## 三、FormPlugin 类业务规则（UI 联动）

### `WarningSceneReceiverEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L116), `afterBindData`(L120), `propertyChanged`(L132), `beforeDoOperation`(L176), `afterDoOperation`(L214)
- **读字段**（5）：`adminorgfield`, `id`, `position`, `rcuser`, `serviceclass`
- **写字段**（1）：`rcuserdisplay`
- **调用的 Service**：`HRBaseServiceHelper`

### `WarnSceneMsgBaseEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`propertyChanged`(L144), `afterDoOperation`(L310)
- **调用的 Service**：`MsgConfigService`

### `WarnSchemeBCFilterEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`afterBindData`(L152), `propertyChanged`(L192), `beforeDoOperation`(L308)
- **读字段**（11）：`baseConditionData`, `baseDataIds`, `commoncondition`, `entityNumber`, `fieldAlias`, `id`, `multi`, `number`
- **调用的 Service**：`FieldDefineService`, `HRBaseServiceHelper`

### `WarnSchemeEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L121), `afterBindData`(L154), `afterCreateNewData`(L126), `propertyChanged`(L280), `beforeF7Select`(L408), `beforeDoOperation`(L328), `itemClick`(L270)
- **读字段**（8）：`bizapp.id`, `id`, `name`, `number`, `permrc`, `rcrelationentryentity`, `warnbizobj`, `warnscene`
- **调用的 Service**：`HRBaseServiceHelper`, `InteService`

### `WarnSchemeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`beforeDoOperation`(L103), `afterDoOperation`(L204)
- **读字段**（6）：`id`, `name`, `planid`, `warnbizobj`, `warnscene`, `warntype`
- **调用的 Service**：`HRBaseServiceHelper`, `HRWarnPreSQLHelper`

### `WarnSchemeReceiverEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L80), `afterBindData`(L84), `beforeDoOperation`(L416)
- **读字段**（10）：`dpt`, `entryentity`, `id`, `name`, `number`, `permrc`, `rcfixuser`, `rcroleentry`
- **调用的 Service**：`HRBaseServiceHelper`

### `WarnSchemeTreeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin`
- **继承**：`HRF7TreeListPlugin`
- **生命周期方法**：`beforeDoOperation`(L343)
- **读字段**（4）：`bizapp`, `id`, `masterid`, `name`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_warnscheme/` 的 12 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_warnscheme.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin -->

## chgaction 实证补充（WarnSchemeEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnC |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |

### 调用的核心 Service（Top 10）
- `EarlyWarnServiceImpl.getInstance`
- `inteServiceImpl.getUserTimezone`
- `inteServiceImpl.getSysTimezone`
- `EarlyWarnServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin -->

## chgaction 实证补充（WarnSchemeBCFilterEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit -->

## chgaction 实证补充（WarningSceneReceiverEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnC |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit -->

## chgaction 实证补充（WarnSceneMsgBaseEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit`
> 跨类追踪: 8 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warnmsgconf` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.WarnSchemeServ |
| `hbss_logview` | updateOne | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.plan.PlanModif |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit -->

## chgaction 实证补充（WarnSchemeReceiverEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin -->

## chgaction 实证补充（WarnSchemeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin -->

## chgaction 实证补充（WarnSchemeTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp -->

## chgaction 实证补充（WarnSchemeOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp`
> 跨类追踪: 10 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exp.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `inteServiceImpl.getSysTimezone`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp -->

## chgaction 实证补充（WarnSchemeBaseConditionOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp -->

## chgaction 实证补充（WarnReceiverSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp -->

## chgaction 实证补充（WarnSchemeAdConditionOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp -->

## chgaction 实证补充（WarnSchemeLogOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->
