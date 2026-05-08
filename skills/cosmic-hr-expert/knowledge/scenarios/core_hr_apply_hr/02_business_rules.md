# core_hr_apply_hr · 业务规则

> **form**：`hrom_applybill_hr` · hrom_applybill_hr
> **生成时间**：2026-04-29
> **方法**：从反编译实物（11 类）提取真业务规则 + standard md 字段必填约束

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
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_apply_hr/` 的 11 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrom_applybill_hr.md`