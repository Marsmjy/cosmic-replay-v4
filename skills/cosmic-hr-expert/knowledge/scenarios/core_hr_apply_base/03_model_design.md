# core_hr_apply_base · 模型设计

> **form**：`hrom_applybill` · HR服务申请基础模板
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**27**
- 必填字段：**5**
- 引用基础资料字段：**10**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `auditor` | 审核人 | `UserField` | `bos_user` |
| `org` | HR管理组织 | `OrgField` | `bos_org` |
| `billtype` | 单据类型 | `BillTypeField` | `bos_billtype` |
| `applicant` | 申请人 | `UserField` | `bos_user` |
| `positionhr` | 岗位 | `BasedataField` | `bos_position` |
| `company` | 所属公司 | `BasedataField` | `bos_adminorg` |
| `applydept` | 申请部门 | `BasedataField` | `haos_adminorghrf7` |
| `dept` | 任职部门 | `BasedataField` | `bos_adminorg` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ApplyBaseFormPlugin` | `AbstractFormPlugin` | Edit/Form |
| `ApplyBaseListPlugin` | `AbstractListPlugin` | List |
| `HRBaseDataImportEdit` | `HRCoreBaseBillEdit` | Edit/Form |
| `HRBaseUeEdit` | `HRDataBaseEdit` | Edit/Form |
| `HRBUCAApplicationEdit` | `HRDataBaseEdit` | Edit/Form |
| `HRCodeRuleOp` | `AbstractOperationServicePlugIn` | OP |
| `HRHiesButtonSwitchPlugin` | `AbstractFormPlugin` | Edit/Form |
| `HRPermCommonEdit` | `HRDataBaseEdit` | Edit/Form |
| `HRPermCommonList` | `HRCoreBaseList` | List |
| `HRTemplateBillEdit` | `HRCoreBaseBillEdit` | Edit/Form |
| `HRTemplateBillList` | `HRCoreBaseBillList` | List |
