# core_hr_empjobrel · 模型设计

> **form**：`hrpi_empjobrel` · 职级职等
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**35**
- 必填字段：**5**
- 引用基础资料字段：**20**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `joblevel` | 职级 | `BasedataField` | `hbjm_joblevelhr` |
| `jobgrade` | 职等 | `BasedataField` | `hbjm_jobgradehr` |
| `job` | 职位 | `BasedataField` | `hbjm_jobhr` |
| `company` | 所属公司 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `jobscm` | 职位体系方案 | `BasedataField` | `hbjm_jobscmhr` |
| `jobclass` | 职位类 | `BasedataField` | `hbjm_jobclasshr` |
| `jobfamily` | 职位族 | `BasedataField` | `hbjm_jobfamilyhr` |
| `jobseq` | 职位序列 | `BasedataField` | `hbjm_jobseqhr` |
| `hrbu` | 职位体系管理组织 | `OrgField` | `bos_org` |
| `joblevelscm` | 职级方案 | `BasedataField` | `hbjm_joblevelscmhr` |
| `jobgradescm` | 职等方案 | `BasedataField` | `hbjm_jobgradescmhr` |
| `chgaction` | 变动操作 | `BasedataField` | `hpfs_chgaction` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |
| `beforeadjempjobrel` | 调整前职级职等 | `BasedataField` | `hrpi_empjobrel` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmpJobRelSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |
