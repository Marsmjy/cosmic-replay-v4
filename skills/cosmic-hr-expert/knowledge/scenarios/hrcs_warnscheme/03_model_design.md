# hrcs_warnscheme · 模型设计

> **form**：`hrcs_warnscheme` · 预警方案
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**65**
- 必填字段：**21**
- 引用基础资料字段：**14**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `warnscene` | 预警场景 | `BasedataField` | `hrcs_warnscene` |
| `bizapp` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `createorg` | 创建组织 | `OrgField` | `bos_org` |
| `timezone` | 时区 | `BasedataField` | `inte_timezone` |
| `org` | 所属HR管理组织 | `OrgField` | `bos_org` |
| `adminorg` | 所属行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `warnbizobj` | 业务对象 | `BasedataField` | `hbp_entityobject` |
| `rcfixuser` | 人员姓名 | `BasedataField` | `bos_user` |
| `rcroleentry` | 角色编码 | `BasedataField` | `perm_role` |
| `adminorgfield` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `WarningSceneReceiverEdit` | `HRDataBaseEdit` | Edit/Form |
| `WarnReceiverSaveOp` | `HRDataBaseOp` | OP |
| `WarnSceneMsgBaseEdit` | `HRDataBaseEdit` | Edit/Form |
| `WarnSchemeAdConditionOp` | `WarnAdOp` | OP |
| `WarnSchemeBaseConditionOp` | `HRDataBaseOp` | OP |
| `WarnSchemeBCFilterEditPlugin` | `HRDataBaseEdit` | Edit/Form |
| `WarnSchemeEditPlugin` | `HRDataBaseEdit` | Edit/Form |
| `WarnSchemeListPlugin` | `HRDataBaseList` | List |
| `WarnSchemeLogOp` | `AbstractOperationServicePlugIn` | OP |
| `WarnSchemeOp` | `HRDataBaseOp` | OP |
| `WarnSchemeReceiverEdit` | `HRDataBaseEdit` | Edit/Form |
| `WarnSchemeTreeListPlugin` | `HRF7TreeListPlugin` | List |
