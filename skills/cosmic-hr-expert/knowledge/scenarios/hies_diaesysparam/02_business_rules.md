# hies_diaesysparam · 业务规则

> **form**：`hies_diaesysparam` · 系统默认参数
> **生成时间**：2026-04-29
> **方法**：从反编译实物（1 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `setnullparam` | 置空处理参数 | `ComboField` | — |
| `maxshownum` | 基础资料下拉值上限 | `ComboField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 三、FormPlugin 类业务规则（UI 联动）

### `DiaeSysParamPlugin`

- **FQN**：`kd.hr.hies.formplugin.DiaeSysParamPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`preOpenForm`(L162), `registerListener`(L63), `afterCreateNewData`(L76), `click`(L88)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hies_diaesysparam/` 的 1 个反编译类 + `_shared/_standard_metadata/entity_metadata/hies_diaesysparam.md`