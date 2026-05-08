# hrobs_carddatasetinfo · 业务规则

> **form**：`hrobs_carddatasetinfo` · 数据集基本信息
> **生成时间**：2026-04-29
> **方法**：从反编译实物（4 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 编码 | `TextField` | — |
| `name` | 名称 | `MuliLangTextField` | — |
| `wbgroup` | 所属领域 | `BasedataField` | `hrobs_wbgroup` |
| `dstype` | 数据集类型 | `BasedataField` | `hrobs_datasettype` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `CardDataSetInfoDeleteOp`

- **FQN**：`kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp`
- **继承**：`AbstractOperationServicePlugIn`

**实现的生命周期方法**：`beginOperationTransaction`(L22)

### `CardDataSetInfoSaveOp`

- **FQN**：`kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp`
- **继承**：`AbstractOperationServicePlugIn`

**调用的 Service / Helper**（1 个）：`CardDataSetInfoSaveValidator`

**实现的生命周期方法**：`onAddValidators`(L17)

## 三、FormPlugin 类业务规则（UI 联动）

### `CardDataSetInfoFormPlugin`

- **FQN**：`kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin`
- **继承**：`AbstractBasePlugIn`
- **生命周期方法**：`registerListener`(L93), `propertyChanged`(L152), `beforeF7Select`(L143), `beforeDoOperation`(L256), `afterDoOperation`(L265), `click`(L191)
- **读字段**（8）：`basedatainfotext`, `binddatainfo`, `commonbinddata.id`, `commonbinddataname`, `commonbinddetailpage.id`, `dstype`, `name`, `wbgroup`

### `CardDataSetInfoListPlugin`

- **FQN**：`kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin`
- **继承**：`AbstractListPlugin`
- **读字段**（8）：`binddatainfo`, `commonbinddata`, `commonbinddata.id`, `commonbinddetailpage.id`, `dstype`, `dstype.id`, `id`, `number`
- **写字段**（1）：`binddatainfo`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrobs_carddatasetinfo/` 的 4 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrobs_carddatasetinfo.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin -->

## chgaction 实证补充（CardDataSetInfoFormPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin -->

## chgaction 实证补充（CardDataSetInfoListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp -->

## chgaction 实证补充（CardDataSetInfoSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp -->

## chgaction 实证补充（CardDataSetInfoDeleteOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp -->
