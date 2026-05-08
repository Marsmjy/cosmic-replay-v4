# hrpi_empjobrel — 职级职等

**表单编码**: `hrpi_empjobrel`  
**表单ID**: `15BEN9B6=4FR`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_empjobrel（职级职等） [BaseEntity]

- **数据库表**: `t_hrpi_empjobrel`  

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
| joblevel | 职级 | BasedataField | fjoblevelid |  | hbjm_joblevelhr |
| jobgrade | 职等 | BasedataField | fjobgradeid |  | hbjm_jobgradehr |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| company | 所属公司 | HRAdminOrgField | fcompanyid |  | haos_adminorghrf7 |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| joblength | 职级职等时长（天） | IntegerField | fjoblength |  |  |
| jobscm | 职位体系方案 | BasedataField | fjobscmid |  | hbjm_jobscmhr |
| jobclass | 职位类 | BasedataField | fjobclassid |  | hbjm_jobclasshr |
| jobfamily | 职位族 | BasedataField | fjobfamilyid |  | hbjm_jobfamilyhr |
| jobseq | 职位序列 | BasedataField | fjobseqid |  | hbjm_jobseqhr |
| hrbu | 职位体系管理组织 | OrgField | fhrbuid |  | bos_org |
| joblevelscm | 职级方案 | BasedataField | fjoblevelscmid |  | hbjm_joblevelscmhr |
| jobgradescm | 职等方案 | BasedataField | fjobgradescmid |  | hbjm_jobgradescmhr |
| orgrelseq | 任职序号 | IntegerField | forgrelseq |  |  |
| chgaction | 变动操作 | BasedataField | fchgactionid | ✓ | hpfs_chgaction |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| primaryempjobrel | 主要职级职等 | CheckBoxField | fprimaryempjobrel |  |  |
| beforeadjempjobrel | 调整前职级职等 | BasedataField | — |  | hrpi_empjobrel |
| jobclasslongname | 职位类长名称（废弃） | TextField | — |  |  |

