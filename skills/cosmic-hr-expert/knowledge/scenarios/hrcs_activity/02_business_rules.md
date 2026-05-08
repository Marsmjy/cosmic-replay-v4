# hrcs_activity · 业务规则

> **form**：`hrcs_activity` · 活动
> **生成时间**：2026-04-29
> **方法**：从反编译实物（6 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `activitytype` | 活动类型 | `BasedataField` | `hrcs_activitytype` |
| `adminorg` | 管理行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `newtype` | 单据实例生成方式 | `ComboField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `ActivityDeleteOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `ActivityDeleteValidator`

**调用的 Service / Helper**（1 个）：`ActivityDeleteValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `ActivityDisableOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `ActivityDisableValidator`

**调用的 Service / Helper**（1 个）：`ActivityDisableValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `ActivityEnableOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp`
- **继承**：`HRDataBaseOp`

### `ActivitySaveOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`ActivitySaveValidator`

**实现的生命周期方法**：`onAddValidators`(L51)

## 三、FormPlugin 类业务规则（UI 联动）

### `ActivityEditPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityEditPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L41), `afterBindData`(L47), `propertyChanged`(L68), `beforeF7Select`(L116)
- **读字段**（1）：`number`
- **调用的 Service**：`HRBaseServiceHelper`

### `ActivityTreeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityTreeListPlugin`
- **继承**：`BaseActivityTreeListPlugin`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_activity/` 的 6 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_activity.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityEditPlugin -->

## chgaction 实证补充（ActivityEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivityEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityTreeListPlugin -->

## chgaction 实证补充（ActivityTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivityTreeListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityTreeListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp -->

## chgaction 实证补充（ActivitySaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp -->

## chgaction 实证补充（ActivityDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp -->

## chgaction 实证补充（ActivityDisableOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp -->

## chgaction 实证补充（ActivityEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp -->
