# hrcs_workingplanlist · 业务规则

> **form**：`hrcs_workingplanlist` · hrcs_workingplanlist
> **生成时间**：2026-04-29
> **方法**：从反编译实物（1 类）提取真业务规则 + standard md 字段必填约束

## 三、FormPlugin 类业务规则（UI 联动）

### `WorkingPlanListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.calendar.WorkingPlanListPlugin`
- **继承**：`AbstractListPlugin`
- **生命周期方法**：`beforeDoOperation`(L137), `afterDoOperation`(L182), `itemClick`(L378)
- **读字段**（14）：`cardentryentity`, `enable`, `enddate`, `entryentity1`, `entryentity2`, `fieldname`, `id`, `name`
- **写字段**（1）：`enable`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_workingplanlist/` 的 1 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_workingplanlist.md`