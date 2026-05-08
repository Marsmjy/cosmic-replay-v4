# hrcs_activityexception · 模型设计

> **form**：`hrcs_activityexception` · 异常监控
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**7**
- 必填字段：**0**
- 引用基础资料字段：**3**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `activityins` | 活动任务实例 | `BasedataField` | `hrcs_activityins` |
| `creator` | 发起人 | `CreaterField` | `bos_user` |
| `modifier` | 处理人 | `UserField` | `bos_user` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ActivityExceptionLogEdit` | `HRDataBaseEdit` | Edit/Form |
| `ActivityExceptionLogPlugin` | `AbstractListPlugin` | Edit/Form |
