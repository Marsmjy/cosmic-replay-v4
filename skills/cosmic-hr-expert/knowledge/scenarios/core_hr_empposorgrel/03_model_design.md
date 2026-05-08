# core_hr_empposorgrel · 模型设计

> **form**：`hrpi_empposorgrel` · 任职经历
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**50**
- 必填字段：**8**
- 引用基础资料字段：**25**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `assignment` | 组织分配 | `BasedataField` | `hrpi_assignment` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `company` | 所属公司 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `position` | 岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `postype` | 任职类型 | `BasedataField` | `hbss_postype` |
| `posstatus` | 任职状态 | `BasedataField` | `hbss_poststate` |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `job` | 职位 | `BasedataField` | `hbjm_jobhr` |
| `workplace` | 常驻工作地 | `BasedataField` | `hbss_workplace` |
| `adminorgvid` | 历史行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `positionvid` | 历史岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `jobvid` | 历史职位 | `BasedataField` | `hbjm_jobhr` |
| `contractworkplace` | 协议工作地 | `BasedataField` | `hbss_workplace` |
| `chgaction` | 变动操作 | `BasedataField` | `hpfs_chgaction` |
| `chgreason` | 变动原因 | `BasedataField` | `hpfs_chgreason` |
| `workcalendar` | 工作日历 | `QueryField` | `hrcs_workingplanquery` |
| `empstage` | 雇佣阶段 | `BasedataField` | `hrpi_empstage` |
| `jobscm` | 职位体系方案 | `BasedataField` | `hbjm_jobscmhr` |
| `jobgradescm` | 职等方案 | `BasedataField` | `hbjm_jobgradescmhr` |
| `jobgrade` | 职等 | `BasedataField` | `hbjm_jobgradehr` |
| `joblevelscm` | 职级方案 | `BasedataField` | `hbjm_joblevelscmhr` |
| `joblevel` | 职级 | `BasedataField` | `hbjm_joblevelhr` |
| `hrbu` | 职位体系管理组织 | `OrgField` | `bos_org` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `AssignmentTplCommonSaveOp` | `HRDataBaseOp` | OP |
| `ChgRecordSaveOp` | `HRDataBaseOp` | OP |
| `EmployeeAuditCommonOP` | `HRDataBaseOp` | Edit/Form |
| `EmployeeCommonStandardMustInputOp` | `HRDataBaseOp` | OP |
| `EmpPosOrgRelDeleteOp` | `HRDataBaseOp` | OP |
| `EmpPosOrgRelSaveOp` | `HRDataBaseOp` | OP |
| `EmpStageHandleOpPlugin` | `HRDataBaseOp` | Edit/Form |
| `IgnoreReferenceDeleteOp` | `HRDataBaseOp` | OP |
| `TimeLineCommonSaveOp` | `HRDataBaseOp` | OP |
| `TimelineLogOp` | `AbstractOperationServicePlugIn` | OP |
| `TimelineTplFormEdit` | `HRDataBaseEdit` | Edit/Form |
| `TimelineTplListPlugin` | `HRDataBaseList` | List |
| `TimelineTplOp` | `HRDataBaseOp` | OP |

## 四、跨云被引用拓扑

本 form 实体被其他云引用 **3** 处。详见 `11_upstream_downstream_logic.md` 注入的下游消费者段。
