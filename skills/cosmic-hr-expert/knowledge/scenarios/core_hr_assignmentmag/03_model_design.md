# core_hr_assignmentmag · 模型设计

> **form**：`hrpi_assignmentmag` · 组织分配管理主体
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**26**
- 必填字段：**9**
- 引用基础资料字段：**12**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `adminorg` | 管理部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `org` | 人事管理组织 | `OrgField` | `bos_org` |
| `persongroup` | 员工组 | `BasedataField` | `hbss_employeegroup` |
| `primaryassignment` | 主组织分配 | `BasedataField` | `hrpi_assignment` |
| `orgtype` | 组织分类 | `BasedataField` | `haos_otclassify` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |
| `country` | 国家/地区 | `BasedataField` | `bd_country` |
| `businesstype` | 业务类型 | `BasedataField` | `hbss_bussinessfield` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentMagDeleteOp` | `HRDataBaseOp` | OP |
| `AssignmentMagSaveOp` | `HRDataBaseOp` | OP |
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
