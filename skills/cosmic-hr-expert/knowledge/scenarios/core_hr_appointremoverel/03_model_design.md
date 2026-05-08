# core_hr_appointremoverel · 模型设计

> **form**：`hrpi_appointremoverel` · 任免经历
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**43**
- 必填字段：**6**
- 引用基础资料字段：**20**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `jobvid` | 历史职位 | `HisModelBasedataField` | `hbjm_jobhr` |
| `appointtype` | 任命类型 | `BasedataField` | `hbss_appointtype` |
| `cadrecat` | 干部类别 | `BasedataField` | `hbss_cadrecategory` |
| `apptreasonggroup` | 任命原因 | `BasedataField` | `hbss_apptreasongroup` |
| `empposrel` | 员工任职 | `BasedataField` | `hrpi_empposorgrel` |
| `dismisstype` | 免职类型 | `BasedataField` | `hbss_appointtype` |
| `dismissreason` | 免职原因 | `BasedataField` | `hbss_apptreasongroup` |
| `company` | 所属公司 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `postype` | 任职类型 | `BasedataField` | `hbss_postype` |
| `posstatus` | 任职状态 | `BasedataField` | `hbss_poststate` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |
| `adminorgvid` | 历史行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `positionvid` | 历史岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `job` | 职位 | `BasedataField` | `hbjm_jobhr` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AppointRemoveRelSaveOp` | `HRDataBaseOp` | OP |
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
