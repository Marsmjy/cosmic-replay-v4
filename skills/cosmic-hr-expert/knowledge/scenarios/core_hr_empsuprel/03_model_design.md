# core_hr_empsuprel · 模型设计

> **form**：`hrpi_empsuprel` · 汇报关系
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**21**
- 必填字段：**6**
- 引用基础资料字段：**8**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `reporttype` | 汇报关系类型 | `BasedataField` | `hbpm_reportcoreltype` |
| `empposorgrel` | 任职 | `EmployeeField` | `hrpi_empposf7query` |
| `superiorempposorgrel` | 汇报上级 | `EmployeeField` | `hrpi_empposf7query` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `EmpSupRelExpireOp` | `HRDataBaseOp` | OP |
| `EmpSupRelSaveOp` | `HRDataBaseOp` | OP |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
