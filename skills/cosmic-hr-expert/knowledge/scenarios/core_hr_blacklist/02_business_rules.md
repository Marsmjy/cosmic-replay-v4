# core_hr_blacklist · 业务规则

> **form**：`hrpi_blacklist` · 黑名单
> **生成时间**：2026-04-29
> **方法**：从反编译实物（3 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `name` | 姓名 | `TextField` | — |
| `phone` | 联系电话 | `TelephoneField` | — |
| `toreason` | 加入原因 | `BasedataField` | `hrpi_toblacklistreason` |
| `adminororg` | 黑名单管理组织 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 三、FormPlugin 类业务规则（UI 联动）

### `HRBaseDataImportEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- **继承**：`HRCoreBaseBillEdit`

### `HRBaseUeEdit`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`preOpenForm`(L40)

### `HRHiesButtonSwitchPlugin`

- **FQN**：`kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- **继承**：`AbstractFormPlugin`
- **生命周期方法**：`afterBindData`(L56)
- **读字段**（5）：`enablestatus`, `newop`, `oldop`, `optype`, `permitem`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/core_hr_blacklist/` 的 3 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrpi_blacklist.md`