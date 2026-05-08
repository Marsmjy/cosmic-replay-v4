# core_hr_assignment · 模型设计

> **form**：`hrpi_assignment` · 组织分配
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**23**
- 必填字段：**7**
- 引用基础资料字段：**11**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `org` | 人事管理组织 | `OrgField` | `bos_org` |
| `orgtype` | 组织分类 | `BasedataField` | `haos_otclassify` |
| `businesstype` | 业务类型 | `BasedataField` | `hbss_bussinessfield` |
| `adminorg` | 管理部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `persongroup` | 员工组 | `BasedataField` | `hbss_employeegroup` |
| `employee` | 员工 | `BasedataField` | `hrpi_employee` |
| `primaryassignment` | 主组织分配 | `BasedataField` | `hrpi_assignment` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |
| `country` | 国家/地区 | `BasedataField` | `bd_country` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentDeleteOp` | `HRDataBaseOp` | OP |
| `AssignmentSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmployeeDeleteOp` | `HRDataBaseOp` | OP |
| `EmployeeNumberCodeRuleDeleteOp` | `CodeRuleDeleteOp` | OP |
| `EmployeeNumberCodeRuleOp` | `CodeRuleOp` | OP |
| `EmployeeNumberCodeRulePlugin` | `CodeRulePlugin` | Edit/Form |
| `EmployeePlugin` | `HRDataBaseEdit` | Edit/Form |
| `EmployeeSaveOp` | `HRDataBaseOp` | OP |
| `InfoCollectStartOp` | `HRDataBaseOp` | OP |
| `OperateLogOp` | `AbstractOperationServicePlugIn` | OP |
