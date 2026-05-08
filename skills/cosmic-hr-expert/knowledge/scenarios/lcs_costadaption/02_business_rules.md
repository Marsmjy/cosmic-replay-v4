# lcs_costadaption · 业务规则

> **form**：`lcs_costadaption` · 人力成本维度方案
> **生成时间**：2026-04-29
> **方法**：从反编译实物（5 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 编码 | `TextField` | — |
| `name` | 名称 | `MuliLangTextField` | — |
| `createorg` | 创建组织 | `OrgField` | `bos_org` |
| `ctrlstrategy` | 控制策略 | `ComboField` | — |
| `coststru` | 人力成本维度组合 | `BasedataField` | `lcs_coststru` |
| `currency` | 成本承担币种 | `BasedataField` | `bd_currency` |
| `areatype` | 国家/地区类型 | `ComboField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `CostAdaptionSaveOp`

- **FQN**：`kd.hrmp.lcs.opplugin.web.CostAdaptionSaveOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`CostAdaptionSaveValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `CostAdaptionSubmitOp`

- **FQN**：`kd.hrmp.lcs.opplugin.web.CostAdaptionSubmitOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`CostAdaptionSubmitValidator`

**实现的生命周期方法**：`onAddValidators`(L28)

### `CostAdaptionUnAuditOp`

- **FQN**：`kd.hrmp.lcs.opplugin.web.CostAdaptionUnAuditOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`CostAdaptionUnAuditValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

## 三、FormPlugin 类业务规则（UI 联动）

### `CostAdaptionEdit`

- **FQN**：`kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`afterBindData`(L47), `propertyChanged`(L35)
- **读字段**（3）：`areatype`, `id`, `name`

### `CostAdaptionList`

- **FQN**：`kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionList`
- **继承**：`HRDataBaseList`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/lcs_costadaption/` 的 5 个反编译类 + `_shared/_standard_metadata/entity_metadata/lcs_costadaption.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionEdit -->

## chgaction 实证补充（CostAdaptionEdit 跨类追踪聚合）

> FQN: `kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionEdit`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionList -->

## chgaction 实证补充（CostAdaptionList 跨类追踪聚合）

> FQN: `kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.formplugin.web.basedata.CostAdaptionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionSaveOp -->

## chgaction 实证补充（CostAdaptionSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.lcs.opplugin.web.CostAdaptionSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.lcs.opplugin.web.CostAdaptionSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionUnAuditOp -->

## chgaction 实证补充（CostAdaptionUnAuditOp 跨类追踪聚合）

> FQN: `kd.hrmp.lcs.opplugin.web.CostAdaptionUnAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.lcs.opplugin.web.CostAdaptionUnAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionUnAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionSubmitOp -->

## chgaction 实证补充（CostAdaptionSubmitOp 跨类追踪聚合）

> FQN: `kd.hrmp.lcs.opplugin.web.CostAdaptionSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.lcs.opplugin.web.CostAdaptionSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.lcs.opplugin.web.CostAdaptionSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->
