# hrpi_blacklist — 黑名单

**表单编码**: `hrpi_blacklist`  
**表单ID**: `3D0HBFW7DE2P`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_blacklist（黑名单） [BaseEntity]

### 物理表

| 表名 | 实体类型 | 说明 |
|------|----------|------|
| `t_hrpi_blacklist` | BaseEntity | 主表 |
| `t_hrpi_blackcard` | EntryEntity | 单据体 |

### 字段列表 — t_hrpi_blacklist（主表·BaseEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hrpi_blacklist.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hrpi_blacklist.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hrpi_blacklist.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hrpi_blacklist.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_hrpi_blacklist.finitdatasource |  |  |
| name | 姓名 | TextField | t_hrpi_blacklist.fname | ✓ |  |
| phone | 联系电话 | TelephoneField | t_hrpi_blacklist.fphone | ✓ |  |
| email | 邮箱 | EmailField | t_hrpi_blacklist.femail |  |  |
| nation | 国籍 | BasedataField | t_hrpi_blacklist.fnationid |  | hbss_nationality |
| toreason | 加入原因 | BasedataField | t_hrpi_blacklist.ftoreasonid | ✓ | hrpi_toblacklistreason |
| reasondetail | 描述 | MuliLangTextField | t_hrpi_blacklist.freasondetail |  |  |
| number | 编码 | TextField | t_hrpi_blacklist.fnumber |  |  |
| empnumber | 工号 | TextField | t_hrpi_blacklist.fempnumber |  |  |
| employee | 员工 | EmployeeField | t_hrpi_blacklist.femployeeid |  | hrpi_employeenewf7query |
| index | 序号 | IntegerField | t_hrpi_blacklist.findex |  |  |
| datasource | 业务来源 | ComboField | t_hrpi_blacklist.fdatasource |  |  |
| gender | 性别 | BasedataField | t_hrpi_blacklist.fgenderid |  | hbss_sex |
| employeeid | 员工ID | BigIntField | t_hrpi_blacklist.fempid |  |  |
| emptype | 用工类型 | BasedataField | t_hrpi_blacklist.femptypeid |  | hbss_laborreltype |
| emppicture | 头像 | PictureField | t_hrpi_blacklist.femppicture |  |  |
| toreasonview | 加入原因 | BasedataField | — |  | hrpi_toblacklistreason |
| reasondetailview | 描述 | MuliLangTextField | — |  |  |
| modifyreason | 修改原因 | MuliLangTextField | — |  |  |
| maincardtype | 主证件类型 | BasedataField | — |  | hbss_credentialstype |
| maincardnumber | 主证件号码 | TextField | — |  |  |
| cardtypeimport | 证件类型 | BasedataField | — |  | hbss_credentialstype |
| cardnumberimport | 证件号码 | TextField | — |  |  |
| adminororg | 黑名单管理组织 | HRAdminOrgField | t_hrpi_blacklist.fadminororgid | ✓ | haos_adminorghrf7 |
| org | 组织 | OrgField | t_hrpi_blacklist.forgid |  | bos_org |
| position | 原就任岗位 | HRPositionField | t_hrpi_blacklist.fpositionhisid |  | hbpm_positionhrf7 |
| job | 原就任职位 | BasedataField | t_hrpi_blacklist.fjobhisid |  | hbjm_jobhr |
| quitdate | 离职日期 | DateField | t_hrpi_blacklist.fquitdate |  |  |
| issysperson | 是否原企业内员工 | CheckBoxField | t_hrpi_blacklist.fissysperson |  |  |
| quitreasonid | 离职原因 | BasedataField | t_hrpi_blacklist.fquitreasonid |  | hpfs_chgreason |
| quittypeid | 离职类型 | BasedataField | t_hrpi_blacklist.fquittypeid |  | hrpi_quittype |
| adminorg | 原就任部门 | HRAdminOrgField | t_hrpi_blacklist.fadminorghisid |  | haos_adminorghrf7 |
| businessstatus | 数据状态 | BillStatusField | t_hrpi_blacklist.fbusinessstatus |  |  |
| assignment | 主组织分配 | EmployeeField | t_hrpi_blacklist.fassignmentid |  | hrpi_assignmentf7query |
| entryentity | 单据体 | EntryEntity | → t_hrpi_blackcard |  |  |

### 字段列表 — t_hrpi_blackcard（单据体·EntryEntity）

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| cardtype | 证件类型 | BasedataField | t_hrpi_blackcard.fcardtypeid |  | hbss_credentialstype |
| ismaincard | 是否主证件 | CheckBoxField | t_hrpi_blackcard.fismaincard |  |  |
| cardnumber | 证件号码 | TextField | t_hrpi_blackcard.fcardnumber |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrpi_blacklist（主表） | 38 |
| t_hrpi_blackcard（单据体） | 3 |
| 无数据库列 | 7 |

