# hrcs_lblstrategy · 模型设计

> **form**：`hrcs_lblstrategy` · 打标策略
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**34**
- 必填字段：**4**
- 引用基础资料字段：**6**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `label` | 标签 | `BasedataField` | `hrcs_label` |
| `labelobject` | 打标对象 | `BasedataField` | `hrcs_labelobject` |
| `labelvalue` | 标签值 | `BasedataField` | `hrcs_labelvalue` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `LabelStrategyOp` | `HRDataBaseOp` | OP |
| `LabelStrategyPlugin` | `HRBaseDataCommonEdit` | Edit/Form |
| `LblStrategyTreeListPlugin` | `StandardTreeListPlugin` | List |
