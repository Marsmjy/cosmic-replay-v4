# hrcs_querydynsourcelist · 模型设计

> **form**：`hrcs_querydynsourcelist` · HR多实体查询配置列表
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**5**
- 必填字段：**0**
- 引用基础资料字段：**1**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `bizappid` | 应用 | `BasedataField` | `bos_devportal_bizapp` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `HRQueryListOp` | `HRDataBaseOp` | OP |
| `HRQueryTreeListPlugin` | `QueryTreeListPlugin` | List |
