# hrcs_warnscene · 业务规则

> **form**：`hrcs_warnscene` · 预警场景
> **生成时间**：2026-04-29
> **方法**：从反编译实物（13 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `bizapp` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |
| `fieldname` | 映射的预警对象控权字段 | `TextField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `WarnSceneAdConditionOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp`
- **继承**：`WarnAdOp`

### `WarnSceneCommonConditionOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp`
- **继承**：`HRDataBaseOp`
- **实现**：`WarnSceneComConditionConstants`

**调用的 Service / Helper**（1 个）：`WarnSceneCommonConditionValidator`

**实现的生命周期方法**：`onAddValidators`(L31)

### `WarnSceneOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp`
- **继承**：`HRDataBaseOp`
- **实现**：`WarningSceneConstants`

**触发的 Validator**（save/delete 前置校验）：
- `WarnSceneValidator`

**拦截的 opKey 分支**（在 beginOperationTransaction 里分流处理）：
- `delete`
- `save`

**调用的 Service / Helper**（3 个）：`HRBaseServiceHelper`, `WarnSceneValidator`, `WarningSceneService`

**实现的生命周期方法**：`onAddValidators`(L70), `beginOperationTransaction`(L80)

## 三、FormPlugin 类业务规则（UI 联动）

### `WarnAdFilterRightTreeEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L77)

### `WarnDataPermEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L89), `afterBindData`(L101), `click`(L211)
- **读字段**（2）：`datapermname`, `warndataperm`
- **写字段**（3）：`datapermname`, `warndataperm`, `warnpermtype`

### `WarningSceneDataFilterPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`propertyChanged`(L126)
- **调用的 Service**：`ReportQueryService`

### `WarningSceneReceiverPermEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L114), `afterBindData`(L130), `propertyChanged`(L222), `beforeF7Select`(L431), `click`(L316)
- **读字段**（3）：`id`, `name`, `warnentityperm`
- **写字段**（4）：`id`, `name`, `number`, `warnentityperm`

### `WarningSceneTreeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin`
- **继承**：`HRF7TreeListPlugin`
- **生命周期方法**：`beforeDoOperation`(L109)

### `WarnSceneAdFilterEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin`
- **继承**：`WarnAdFilterEditPlugin`

### `WarnSceneAdFilterLeftTreeEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin`
- **继承**：`WarnAdFilterLeftTreeEditPlugin`
- **调用的 Service**：`FieldDefineService`

### `WarnSceneCalConfigEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit`
- **继承**：`WarnSceneCommonEdit`
- **调用的 Service**：`WarnCalcFieldService`

### `WarnSceneCommonConditionsEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit`
- **继承**：`WarnSceneCommonEdit`
- **生命周期方法**：`registerListener`(L129), `afterBindData`(L206), `propertyChanged`(L287), `beforeDoOperation`(L152), `afterDoOperation`(L184)
- **读字段**（12）：`baseDataIds`, `commonConditionData`, `commoncondition`, `dataSource`, `entityNumber`, `fieldAlias`, `id`, `multi`
- **调用的 Service**：`HRBaseServiceHelper`

### `WarnSceneEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit`
- **继承**：`WarnSceneCommonEdit`
- **生命周期方法**：`registerListener`(L112), `afterBindData`(L144), `propertyChanged`(L204), `beforeDoOperation`(L271), `afterDoOperation`(L293), `itemClick`(L319)
- **写字段**（1）：`id`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_warnscene/` 的 13 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_warnscene.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit -->

## chgaction 实证补充（WarnSceneEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin -->

## chgaction 实证补充（WarningSceneDataFilterPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnC |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRFilterUtil_0` | 日期类型转换错误。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit -->

## chgaction 实证补充（WarnSceneCommonConditionsEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit`
> 跨类追踪: 8 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRFormShowParameterService_1` | 值转Long类型错误，值：%1$s，错误信息：%2$s。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit -->

## chgaction 实证补充（WarnSceneCalConfigEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnC |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit -->

## chgaction 实证补充（WarningSceneReceiverPermEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin -->

## chgaction 实证补充（WarnSceneAdFilterLeftTreeEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin -->

## chgaction 实证补充（WarnAdFilterRightTreeEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "fcExecuteCode isEmpty" |
| `FormulaUtils_0` | 第{0}行，第{1}列，“{2}”作为数组索引，必须定义为整数。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin -->

## chgaction 实证补充（WarnSceneAdFilterEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit -->

## chgaction 实证补充（WarnDataPermEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit`
> 跨类追踪: 8 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin -->

## chgaction 实证补充（WarningSceneTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp -->

## chgaction 实证补充（WarnSceneOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_warnscenejoinentity` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_warnscenequeryfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_warnsceneentityrel` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_warncalfield` | deleteByFilter | HRBaseServiceHelper | kd.hr.hrcs.bussiness.domain.service.earlywarn.calfield.WarnC |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `ParamsUtil_0` | %s编码对应的业务对象不存在。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp -->

## chgaction 实证补充（WarnSceneCommonConditionOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp -->

## chgaction 实证补充（WarnSceneAdConditionOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp -->
