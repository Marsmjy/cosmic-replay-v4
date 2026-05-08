# core_hr_employeetaxcn · 模型设计

> **form**：`hrpi_employeetaxcn` · 员工个税信息
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**67**
- 必填字段：**8**
- 引用基础资料字段：**18**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `employee` | 工号 | `EmployeeField` | `hrpi_employeenewf7query` |
| `assignment` | 组织分配号 | `EmployeeField` | `hrpi_assignmentf7query` |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `taxunit` | 纳税单位 | `BasedataField` | `hbss_taxunit` |
| `birthplace` | 出生地（税局） | `BasedataField` | `hbss_nationality` |
| `education` | 学历 | `BasedataField` | `hbss_diploma` |
| `credentialstype` | 证件类型 | `BasedataField` | `hbss_credentialstype` |
| `regpermres` | 户籍所在地（省） | `BasedataField` | `bd_admindivision` |
| `regpermrescity` | 户籍所在地（市） | `BasedataField` | `bd_admindivision` |
| `regpermrescounty` | 户籍所在地（区县） | `BasedataField` | `bd_admindivision` |
| `habitres` | 经常居住地（省） | `BasedataField` | `bd_admindivision` |
| `habitrescity` | 经常居住地（市） | `BasedataField` | `bd_admindivision` |
| `habitrescounty` | 经常居住地（区县） | `BasedataField` | `bd_admindivision` |
| `address` | 联系地址（省） | `BasedataField` | `bd_admindivision` |
| `addresscity` | 联系地址（市） | `BasedataField` | `bd_admindivision` |
| `addresscounty` | 联系地址（区县） | `BasedataField` | `bd_admindivision` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmployeeTaxCNSaveOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
