# hrpi_dispatchinfo — 外派信息

**表单编码**: `hrpi_dispatchinfo`  
**表单ID**: `4SF7N=NQ0W4T`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_dispatchinfo（外派信息） [BaseEntity]

- **数据库表**: `t_hrpi_dispatchinfo`  

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | fcreatetime |  |  |
| modifier | 修改人 | ModifierField | fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | finitdatasource |  |  |
| isdeleted | 已删除 | CheckBoxField | — |  |  |
| iscurrentdata | 当前数据 | CheckBoxField | fiscurrentdata |  |  |
| startdate | 外派开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 外派结束日期 | DateField | fenddate | ✓ |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| company | 公司 | HRAdminOrgField | fcompanyid |  | haos_adminorghrf7 |
| adminorg | 部门 | HRAdminOrgField | fadminorgid | ✓ | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| dispworkplace | 常驻工作地 | BasedataField | fdispworkplaceid |  | hbss_workplace |
| disptype | 外派类型 | BasedataField | fdisptypeid |  | hbss_disptype |
| dispreason | 外派原因 | BasedataField | fdispreasonid |  | hpfs_chgreason |
| plandispenddate | 计划外派结束日期 | DateField | fplandispenddate |  |  |
| dispcontry | 外派国家/地区 | BasedataField | fdispcontryid |  | bd_country |
| empposorgrel | 任职经历 | BasedataField | — |  | hrpi_empposorgrel |

