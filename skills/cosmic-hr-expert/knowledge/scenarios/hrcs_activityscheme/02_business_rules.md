# hrcs_activityscheme · 业务规则

> **form**：`hrcs_activityscheme` · 活动方案
> **生成时间**：2026-04-29
> **方法**：从反编译实物（4 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `bizobj` | 业务对象 | `BasedataField` | `bos_entityobject` |
| `app` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `version` | 版本 | `TextField` | — |
| `adminorg` | 管理行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `groupactivity` | 活动 | `BasedataField` | `hrcs_activity` |
| `activitytype` | 活动类型 | `ComboField` | — |
| `taskcreatetype` | 任务创建方式 | `ComboField` | — |
| `tasktheme` | 任务主题 | `TextField` | — |
| `taskassignmenttype` | 任务分配方式 | `ComboField` | — |
| `flowparam` | 流程流转参数 | `ComboField` | — |
| `checkhandler` | 是否需要活动处理人 | `ComboField` | — |
| `taskthemedisplay` | 任务主题 | `TextField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `ActivitySchemeSaveOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivitySchemeSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`ActivitySchemeSaveValidator`

**实现的生命周期方法**：`onAddValidators`(L41), `beginOperationTransaction`(L46)

## 三、FormPlugin 类业务规则（UI 联动）

### `ActivityGroupConfigEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityGroupConfigEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L87), `afterBindData`(L113), `propertyChanged`(L145), `beforeF7Select`(L265), `beforeDoOperation`(L197), `afterDoOperation`(L128)
- **读字段**（7）：`activity.id`, `app`, `groupactivity`, `groupactivity.id`, `id`, `isopen`, `number`
- **调用的 Service**：`HRBaseServiceHelper`

### `ActivitySchemeEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivitySchemeEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L159), `afterBindData`(L192), `propertyChanged`(L542), `beforeF7Select`(L873), `beforeDoOperation`(L342), `afterDoOperation`(L419), `click`(L237)
- **读字段**（15）：`actbizobj`, `actgroupentity`, `actgroupname`, `actinfo`, `activity`, `activity.id`, `activity.name`, `actschemeentry`
- **写字段**（15）：`actbizobj`, `bindinglayoutid`, `bizapp`, `bizobj`, `cloud`, `fieldactivityid`, `flowparam`, `method`
- **调用的 Service**：`HRBaseServiceHelper`

### `ActivitySchemeTreeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivitySchemeTreeListPlugin`
- **继承**：`BaseActivityTreeListPlugin`
- **生命周期方法**：`beforeDoOperation`(L84)
- **读字段**（1）：`sequence`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_activityscheme/` 的 4 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_activityscheme.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivitySchemeEdit -->

## chgaction 实证补充（ActivitySchemeEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivitySchemeEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivitySchemeEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_activityclientconf` | saveOne | HRBaseServiceHelper | <self> (depth=0) |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TplVariableConfigPlugin_12` | 请选择字段 |
| `TplVariableConfigPlugin_18` | 请选择合规的字段。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivitySchemeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityGroupConfigEdit -->

## chgaction 实证补充（ActivityGroupConfigEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivityGroupConfigEdit`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityGroupConfigEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityGroupConfigEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivitySchemeTreeListPlugin -->

## chgaction 实证补充（ActivitySchemeTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivitySchemeTreeListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivitySchemeTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivitySchemeTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivitySchemeSaveOp -->

## chgaction 实证补充（ActivitySchemeSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivitySchemeSaveOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivitySchemeSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivitySchemeSaveOp -->

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
