# working_hours · 模型设计

> **form**：`working_hours` · 工作时段
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**17**
- 必填字段：**4**
- 引用基础资料字段：**3**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `disabler` | 禁用人 | `ModifierField` | `bos_user` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
