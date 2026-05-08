# core_hr_archive_my · 业务规则

> **form**：`hspm_myermanfilereform` · 我的档案
> **生成时间**：2026-04-29
> **方法**：从反编译实物（1 类）提取真业务规则 + standard md 字段必填约束

## 三、FormPlugin 类业务规则（UI 联动）

### `MyFileReformHomePlugin`

- **FQN**：`kd.hr.hspm.formplugin.web.reform.file.web.employee.MyFileReformHomePlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L197), `afterBindData`(L123), `propertyChanged`(L216), `afterDoOperation`(L148), `click`(L136)
- **读字段**（2）：`employee.id`, `id`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_archive_my/` 的 1 个反编译类 + `_shared/_standard_metadata/entity_metadata/hspm_myermanfilereform.md`