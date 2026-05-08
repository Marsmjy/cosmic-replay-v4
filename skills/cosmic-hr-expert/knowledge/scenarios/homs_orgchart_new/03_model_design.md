# homs_orgchart_new · 模型设计

> **form**：`homs_orgchart_new` · 行政组织结构图
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**1**
- 必填字段：**0**
- 引用基础资料字段：**1**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `boid` | 行政组织id | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
