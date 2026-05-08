# haos_staff · 模型设计

> **form**：`haos_staff` · 编制信息维护
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**85**
- 必填字段：**7**
- 引用基础资料字段：**11**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `orgteam` | 使用组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `buseorg` | 使用组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `buseorgboid` | 使用组织当前版本 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `bdutyorg` | 责任组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `staffcycle` | 填报期间 | `BasedataField` | `haos_staffcycle` |
| `staffproject` | 控编规则 | `BasedataField` | `haos_staffproject` |
| `org` | 组织体系管理组织 | `OrgField` | `bos_org` |
| `useorg` | 使用组织 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段
