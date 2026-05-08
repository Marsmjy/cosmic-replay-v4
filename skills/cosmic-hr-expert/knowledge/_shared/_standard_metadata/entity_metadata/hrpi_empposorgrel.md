# hrpi_empposorgrel — 任职经历

**表单编码**: `hrpi_empposorgrel`  
**表单ID**: `15D680ZV7YNR`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_empposorgrel（任职经历） [BaseEntity]

- **数据库表**: `t_hrpi_empposorgrel`  

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
| company | 所属公司 | HRAdminOrgField | fcompanyid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| postype | 任职类型 | BasedataField | fpostypeid | ✓ | hbss_postype |
| isexistprobation | 有考察期 | CheckBoxField | fisexistprobation |  |  |
| isprimary | 主任职 | CheckBoxField | fisprimary |  |  |
| startprobation | 考察开始日期 | DateField | fstartprobation |  |  |
| endprobation | 考察结束日期 | DateField | fendprobation |  |  |
| posstatus | 任职状态 | BasedataField | fposstatus | ✓ | hbss_poststate |
| servicelength | 时长（天） | IntegerField | fservicelength |  |  |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid | ✓ | haos_adminorghrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |
| number | 任职编号 | TextField | fnumber |  |  |
| workplace | 常驻工作地 | BasedataField | fworkplaceid |  | hbss_workplace |
| adminorgvid | 历史行政组织 | HRAdminOrgField | fadminorgvid |  | haos_adminorghrf7 |
| positionvid | 历史岗位 | HRPositionField | fpositionvid |  | hbpm_positionhrf7 |
| jobvid | 历史职位 | BasedataField | fjobvid |  | hbjm_jobhr |
| contractworkplace | 协议工作地 | BasedataField | fcontractworkplaceid |  | hbss_workplace |
| orgrelseq | 任职序号 | IntegerField | forgrelseq |  |  |
| timeseq | 时间序号 | IntegerField | ftimeseq |  |  |
| isinsystem | 系统内任职 | CheckBoxField | fisinsystem |  |  |
| chgaction | 变动操作 | BasedataField | fchgactionid | ✓ | hpfs_chgaction |
| chgreason | 变动原因 | BasedataField | fchgreasonid |  | hpfs_chgreason |
| workcalendar | 工作日历 | QueryField | fworkcalendarid |  | hrcs_workingplanquery |
| islatestrecord | 退出组织分配前最新记录 | CheckBoxField | fislatestrecord |  |  |
| isquitassignment | 退出组织分配后记录 | CheckBoxField | fisquitassignment |  |  |
| isseqlatestrecord | 最新任职记录 | CheckBoxField | fisseqlatestrecord |  |  |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| ismaxtimeseqrecord | 当天最新记录 | CheckBoxField | fismaxtimeseqrecord |  |  |
| sortcode | 排序码 | TextField | fsortcode |  |  |
| jobscm | 职位体系方案 | BasedataField | — |  | hbjm_jobscmhr |
| jobgradescm | 职等方案 | BasedataField | — |  | hbjm_jobgradescmhr |
| jobgrade | 职等 | BasedataField | — |  | hbjm_jobgradehr |
| joblevelscm | 职级方案 | BasedataField | — |  | hbjm_joblevelscmhr |
| joblevel | 职级 | BasedataField | — |  | hbjm_joblevelhr |
| hrbu | 职位体系管理组织 | OrgField | — |  | bos_org |

