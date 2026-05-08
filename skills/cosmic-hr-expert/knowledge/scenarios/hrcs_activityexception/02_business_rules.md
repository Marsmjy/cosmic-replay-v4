# hrcs_activityexception · 业务规则

> **form**：`hrcs_activityexception` · 异常监控
> **生成时间**：2026-04-29
> **方法**：从反编译实物（2 类）提取真业务规则 + standard md 字段必填约束

## 三、FormPlugin 类业务规则（UI 联动）

### `ActivityExceptionLogEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityExceptionLogEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`afterBindData`(L18)

### `ActivityExceptionLogPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityExceptionLogPlugin`
- **继承**：`AbstractListPlugin`
- **生命周期方法**：`registerListener`(L72), `beforeDoOperation`(L76)
- **读字段**（5）：`activityins`, `bizbillid`, `exceptiontype`, `param`, `result`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_activityexception/` 的 2 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_activityexception.md`