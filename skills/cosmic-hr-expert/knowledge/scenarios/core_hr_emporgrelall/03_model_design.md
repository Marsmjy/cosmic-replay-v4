# core_hr_emporgrelall · 模型设计

> **form**：`hrpi_emporgrelall` · 任职经历总表
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**31**
- 必填字段：**4**
- 引用基础资料字段：**4**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
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
