# core_hr_dispatchinfo · 模型设计

> **form**：`hrpi_dispatchinfo` · 外派信息
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**24**
- 必填字段：**5**
- 引用基础资料字段：**13**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `company` | 公司 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `adminorg` | 部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `job` | 职位 | `BasedataField` | `hbjm_jobhr` |
| `dispworkplace` | 常驻工作地 | `BasedataField` | `hbss_workplace` |
| `disptype` | 外派类型 | `BasedataField` | `hbss_disptype` |
| `dispreason` | 外派原因 | `BasedataField` | `hpfs_chgreason` |
| `dispcontry` | 外派国家/地区 | `BasedataField` | `bd_country` |
| `empposorgrel` | 任职经历 | `BasedataField` | `hrpi_empposorgrel` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `DispatchSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
