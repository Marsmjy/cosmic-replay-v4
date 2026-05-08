# hrcs_activity · 模型设计

> **form**：`hrcs_activity` · 活动
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**25**
- 必填字段：**3**
- 引用基础资料字段：**7**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `activitytype` | 活动类型 | `BasedataField` | `hrcs_activitytype` |
| `bizobj` | 业务对象 | `BasedataField` | `bos_entityobject` |
| `app` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `adminorg` | 管理行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ActivityDeleteOp` | `HRDataBaseOp` | OP |
| `ActivityDisableOp` | `HRDataBaseOp` | OP |
| `ActivityEditPlugin` | `HRDataBaseEdit` | Edit/Form |
| `ActivityEnableOp` | `HRDataBaseOp` | OP |
| `ActivitySaveOp` | `HRDataBaseOp` | OP |
| `ActivityTreeListPlugin` | `BaseActivityTreeListPlugin` | List |
