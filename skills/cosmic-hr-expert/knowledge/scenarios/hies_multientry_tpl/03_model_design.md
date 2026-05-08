# hies_multientry_tpl · 模型设计

> **form**：`hies_multientry_tpl` · 单据体导入模板配置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**93**
- 必填字段：**9**
- 引用基础资料字段：**10**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `entity` | 实体 | `BasedataField` | `hbp_entityobject` |
| `orgfield` | 创建组织 | `OrgField` | `bos_org` |
| `user` | 人员 | `BasedataField` | `bos_user` |
| `role` | 角色 | `BasedataField` | `perm_role` |
| `org` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `orgrole` | 角色编码 | `BasedataField` | `perm_role` |
| `orgrolenumber` | 行政组织编码 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
