# core_hr_blacklist · 模型设计

> **form**：`hrpi_blacklist` · 黑名单
> **生成时间**：2026-04-29

## 一、字段统计

- 总字段数：**41**
- 必填字段：**4**
- 引用基础资料字段：**19**

## 二、引用基础资料 / 上游实体

| 字段 | 字段名 | 类型 | 引用实体 |
|---|---|---|---|
| `creator` | 创建人 | `CreaterField` | `bos_user` |
| `modifier` | 修改人 | `ModifierField` | `bos_user` |
| `nation` | 国籍 | `BasedataField` | `hbss_nationality` |
| `toreason` | 加入原因 | `BasedataField` | `hrpi_toblacklistreason` |
| `employee` | 员工 | `EmployeeField` | `hrpi_employeenewf7query` |
| `gender` | 性别 | `BasedataField` | `hbss_sex` |
| `emptype` | 用工类型 | `BasedataField` | `hbss_laborreltype` |
| `toreasonview` | 加入原因 | `BasedataField` | `hrpi_toblacklistreason` |
| `maincardtype` | 主证件类型 | `BasedataField` | `hbss_credentialstype` |
| `cardtypeimport` | 证件类型 | `BasedataField` | `hbss_credentialstype` |
| `adminororg` | 黑名单管理组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `org` | 组织 | `OrgField` | `bos_org` |
| `position` | 原就任岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `job` | 原就任职位 | `BasedataField` | `hbjm_jobhr` |
| `quitreasonid` | 离职原因 | `BasedataField` | `hpfs_chgreason` |
| `quittypeid` | 离职类型 | `BasedataField` | `hrpi_quittype` |
| `adminorg` | 原就任部门 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `assignment` | 主组织分配 | `EmployeeField` | `hrpi_assignmentf7query` |
| `cardtype` | 证件类型 | `BasedataField` | `hbss_credentialstype` |

> 跨云引用详见 `11_upstream_downstream_logic.md` 自动注入的上游底座引用段

## 三、反编译类继承层次

| 类 | 父类 | 类型 |
|---|---|---|
| `HRBaseDataImportEdit` | `HRCoreBaseBillEdit` | Edit/Form |
| `HRBaseUeEdit` | `HRDataBaseEdit` | Edit/Form |
| `HRHiesButtonSwitchPlugin` | `AbstractFormPlugin` | Edit/Form |
