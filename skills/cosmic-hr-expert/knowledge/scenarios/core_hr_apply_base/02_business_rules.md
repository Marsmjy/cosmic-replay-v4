# core_hr_apply_base · 业务规则

> **form**：`hrom_applybill` · HR服务申请基础模板
> **生成时间**：2026-04-29
> **方法**：从反编译实物（11 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `billno` | 单据编号 | `BillNoField` | — |
| `billstatus` | 单据状态 | `BillStatusField` | — |
| `org` | HR管理组织 | `OrgField` | `bos_org` |
| `auditstatus` | 审批状态 | `BillStatusField` | — |
| `billtype` | 单据类型 | `BillTypeField` | `bos_billtype` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `HRCodeRuleOp`

- **FQN**：`kd.hr.hbp.opplugin.web.HRCodeRuleOp`
- **继承**：`AbstractOperationServicePlugIn`

**实现的生命周期方法**：`onAddValidators`(L44)

## 三、FormPlugin 类业务规则（UI 联动）

### `ApplyBaseFormPlugin`

- **FQN**：`kd.hros.hrom.formplugin.route.apply.ApplyBaseFormPlugin`
- **继承**：`AbstractFormPlugin`
- **生命周期方法**：`preOpenForm`(L134), `registerListener`(L125), `afterBindData`(L199), `afterCreateNewData`(L251), `propertyChanged`(L256), `beforeF7Select`(L395), `click`(L167)
- **读字段**（4）：`applicant`, `billno`, `id`, `post`
- **调用的 Service**：`HRBaseServiceHelper`

### `ApplyBaseListPlugin`

- **FQN**：`kd.hros.hrom.formplugin.route.apply.ApplyBaseListPlugin`
- **继承**：`AbstractListPlugin`

### `HRBaseDataImportEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承**：`HRCoreBaseBillEdit`

### `HRBaseUeEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`preOpenForm`(L40)

### `HRBUCAApplicationEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.hrbu.HRBUCAApplicationEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L33), `beforeF7Select`(L71)
- **读字段**（1）：`businesstype.controlfuntype.id`
- **调用的 Service**：`HRBaseServiceHelper`

### `HRHiesButtonSwitchPlugin`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- **继承**：`AbstractFormPlugin`
- **生命周期方法**：`afterBindData`(L56)
- **读字段**（5）：`enablestatus`, `newop`, `oldop`, `optype`, `permitem`
- **调用的 Service**：`HRBaseServiceHelper`

### `HRPermCommonEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.HRPermCommonEdit`
- **继承**：`HRDataBaseEdit`

### `HRPermCommonList`

- **FQN**：`kd.hr.hbp.formplugin.web.HRPermCommonList`
- **继承**：`HRCoreBaseList`
- **生命周期方法**：`beforeF7Select`(L30)

### `HRTemplateBillEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
- **继承**：`HRCoreBaseBillEdit`
- **生命周期方法**：`afterDoOperation`(L70)

### `HRTemplateBillList`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
- **继承**：`HRCoreBaseBillList`
- **生命周期方法**：`afterDoOperation`(L21)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_apply_base/` 的 11 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrom_applybill.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

## chgaction 实证补充（HRTemplateBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## chgaction 实证补充（HRPermCommonEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

## chgaction 实证补充（HRTemplateBillList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

## chgaction 实证补充（HRPermCommonList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->
