# hrcs_activityscheme · 模型设计

> **form**：`hrcs_activityscheme` · 活动方案
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**67**
- 必填字段：**12**
- 引用基础资料字段：**14**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `createorg` | 创建组织 | `OrgField` | `bos_org` |
| `org` | 管理组织 | `OrgField` | `bos_org` |
| `useorg` | 使用组织 | `OrgField` | `bos_org` |
| `srccreateorg` | 原创建组织 | `OrgField` | `bos_org` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `bizobj` | 业务对象 | `BasedataField` | `bos_entityobject` |
| `app` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `adminorg` | 管理行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `activity` | 请选择活动 | `BasedataField` | `hrcs_activity` |
| `groupactivity` | 活动 | `BasedataField` | `hrcs_activity` |
| `actbizobj` | 业务对象 | `BasedataField` | `bos_entityobject` |
| `bizapp` | 所属应用 | `BasedataField` | `hbp_devportal_bizapp` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ActivityGroupConfigEdit` | `HRDataBaseEdit` | Edit/Form |
| `ActivitySchemeEdit` | `HRDataBaseEdit` | Edit/Form |
| `ActivitySchemeSaveOp` | `HRDataBaseOp` | OP |
| `ActivitySchemeTreeListPlugin` | `BaseActivityTreeListPlugin` | List |
