# hrcs_warnexeclog · 业务规则

> **form**：`hrcs_warnexeclog` · 预警执行日志
> **生成时间**：2026-04-29
> **方法**：从反编译实物（2 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `scheme` | 预警方案ID | `BigIntField` | — |
| `warnscene` | 预警场景ID | `BigIntField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 三、FormPlugin 类业务规则（UI 联动）

### `EarlyWarnLogListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.log.EarlyWarnLogListPlugin`
- **继承**：`HRDataBaseList`
- **读字段**（1）：`schemename`

### `EarlyWarnLogPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.earlywarn.log.EarlyWarnLogPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`preOpenForm`(L26)

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_warnexeclog/` 的 2 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_warnexeclog.md`