# core_hr_empentrel · 模型设计

> **form**：`hrpi_empentrel` · 雇佣信息
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**24**
- 必填字段：**6**
- 引用基础资料字段：**9**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `enterprise` | 用人单位 | `BasedataField` | `hbss_enterprise` |
| `laborreltype` | 用工关系类型 | `BasedataField` | `hbss_laborreltype` |
| `laborrelstatus` | 用工关系状态 | `BasedataField` | `hbss_laborrelstatus` |
| `onboardsource` | 入职来源 | `BasedataField` | `hbss_onboardsource` |
| `candidate` | 候选人id | `BasedataField` | `hcf_candidate` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmpEntRelEmpStageSaveOp` | `HRDataBaseOp` | OP |
| `EmpEntRelReviseEntryDateOp` | `HRDataBaseOp` | OP |
| `EmpEntRelSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
