# hrpi_rotationinfo — 轮岗情况

**表单编码**: `hrpi_rotationinfo`  
**表单ID**: `3N/MW32RRZSB`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_rotationinfo（轮岗情况） [BaseEntity]

- **数据库表**: `t_hrpi_rotationinfo`  

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
| startdate | 开始日期 | DateField | fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | fenddate | ✓ |  |
| assignment | 组织分配 | BasedataField | fassignmentid | ✓ | hrpi_assignment |
| description | 描述 | MuliLangTextField | fdescription |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | fsourcesyskey |  |  |
| modifyexceptional | 修改异常 | ComboField | — |  |  |
| employee | 员工 | EmployeeField | femployeeid | ✓ | hrpi_employeenewf7query |
| issingle | 单行显示 | CheckBoxField | fissingle |  |  |
| adminorg | 轮岗部门 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| job | 担任职位 | BasedataField | fjobid |  | hbjm_jobhr |
| position | 担任岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| rotunittype | 轮岗单位类型 | ComboField | frotunittype |  |  |
| company | 轮岗公司 | HRAdminOrgField | fcompanyid |  | haos_adminorghrf7 |
| rottype | 轮岗类型 | BasedataField | frottype |  | hbss_rotationtype |
| adminorgtext | 轮岗部门 | MuliLangTextField | fadminorgtext |  |  |
| companytext | 轮岗公司 | MuliLangTextField | fcompanytext |  |  |
| jobtext | 担任职位 | MuliLangTextField | fjobtext |  |  |
| positiontext | 担任岗位/职位 | MuliLangTextField | fpositiontext |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |

