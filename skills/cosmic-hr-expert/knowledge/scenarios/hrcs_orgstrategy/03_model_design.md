# hrcs_orgstrategy · 模型设计

> **form**：`hrcs_orgstrategy` · 行政组织-组织管理关系策略设置
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**42**
- 必填字段：**6**
- 引用基础资料字段：**14**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `disabler` | 禁用人 | `UserField` | `bos_user` |
| `inheritedorg` | 参照行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `defstrategytype` | 默认策略 | `BasedataField` | `hrcs_strategy` |
| `sourceorg` | 源策略组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `hrbu` | 业务单元 | `OrgField` | `bos_org` |
| `orgteam` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `bussinessfield` | 业务类型关系 | `BasedataField` | `hrcs_bussinesstype` |
| `entryhrbu` | 默认HR管理组织 | `OrgField` | `bos_org` |
| `entryinheritedorg` | 参照行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `entrydefstrategy` | 策略 | `BasedataField` | `hrcs_strategy` |
| `entryorgteam` | 分录行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `entrysourceorg` | 源策略组织 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AdminOrgManageStrategyEdit` | `OrgManageStrategyEdit` | Edit/Form |
| `ManageStrategyListPlugin` | `HRDataBaseList` | List |
| `ManageStrategyLogOp` | `AbstractOperationServicePlugIn` | OP |
| `OrgManageStrategyQueryPlugin` | `ManageStrategyQueryPlugin` | Edit/Form |
| `OrgManageStrategySaveOp` | `ManageStrategySaveOp` | OP |
