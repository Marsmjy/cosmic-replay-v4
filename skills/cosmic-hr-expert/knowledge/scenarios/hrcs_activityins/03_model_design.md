# hrcs_activityins · 模型设计

> **form**：`hrcs_activityins` · 活动任务实例
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**25**
- 必填字段：**6**
- 引用基础资料字段：**3**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `applier` | 发起人 | `UserField` | `bos_user` |
| `activity` | 活动 | `BasedataField` | `hrcs_activity` |
| `actscheme` | 活动方案 | `BasedataField` | `hrcs_activityscheme` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ActivityInsAssigntoOp` | `HRDataBaseOp` | OP |
| `ActivityInsSaveOp` | `HRDataBaseOp` | OP |
| `ActivityInstancePlugin` | `AbstractListPlugin` | Edit/Form |
