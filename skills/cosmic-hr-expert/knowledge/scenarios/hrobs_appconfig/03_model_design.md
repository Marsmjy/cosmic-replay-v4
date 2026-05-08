# hrobs_appconfig · 模型设计

> **form**：`hrobs_appconfig` · HR服务应用
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**33**
- 必填字段：**4**
- 引用基础资料字段：**5**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `appgroup` | 应用分组 | `BasedataField` | `hrobs_appgroup` |
| `form` | 处理页面 | `BasedataField` | `bos_formmeta` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
