# int_holidayclass · 模型设计

> **form**：`int_holidayclass` · 假期类型
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**11**
- 必填字段：**2**
- 引用基础资料字段：**3**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `country` | 国家或地区 | `BasedataField` | `bd_country` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
