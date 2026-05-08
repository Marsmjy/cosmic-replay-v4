# brm_special_list · 模型设计

> **form**：`brm_special_list` · 名单管理
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**41**
- 必填字段：**9**
- 引用基础资料字段：**7**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bu` | 创建组织 | `OrgField` | `bos_org` |
| `entityperson` | 姓名 | `BasedataField` | `bos_user` |
| `entityorg` | 行政组织名称 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `entityemp` | 姓名 | `BasedataField` | `hsbs_employee` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
