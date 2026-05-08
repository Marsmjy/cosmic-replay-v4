# hrpi_appointremoverel — 任免经历

**表单编码**: `hrpi_appointremoverel`  
**表单ID**: `3ADU27WG/KPT`  
**归属**: HR基础服务云 / 员工信息中心  
**来源系统**: 金蝶苍穹 HCM 人力资源  

---

## 实体: hrpi_appointremoverel（任免经历） [BaseEntity]

- **数据库表**: `t_hrpi_appointremoverel`  

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
| jobvid | 历史职位 | HisModelBasedataField | fjobvid |  | hbjm_jobhr |
| appointdate | 任命日期 | DateField | fappointdate | ✓ |  |
| dismissdate | 免职日期 | DateField | fdismissdate |  |  |
| appointtype | 任命类型 | BasedataField | fappointtypeid |  | hbss_appointtype |
| cadrecat | 干部类别 | BasedataField | fcadrecatid | ✓ | hbss_cadrecategory |
| appointreason | 任命原因详述 | TextAreaField | fappointreason |  |  |
| isprimappoint | 主岗任命 | CheckBoxField | fisprimappoint |  |  |
| appointtypestatus | 任免状态 | ComboField | fappointstatus |  |  |
| apptreasonggroup | 任命原因 | BasedataField | fapptreasonggroupid |  | hbss_apptreasongroup |
| appointdispatchdate | 任命发文日期 | DateField | fappointdispatchdate |  |  |
| dismissdispatchdate | 免职发文日期 | DateField | fdismissdispatchdate |  |  |
| empposrel | 员工任职 | BasedataField | fempposorgrelid |  | hrpi_empposorgrel |
| dismisstype | 免职类型 | BasedataField | fdismisstypeid |  | hbss_appointtype |
| businessstatus | 业务状态 | ComboField | fbusinessstatus |  |  |
| appointdispatchnymber | 任命发文文号 | TextField | fappointdispatchnymber |  |  |
| dismissdispatchnymber | 免职发文文号 | TextField | fdismissdispatchnymber |  |  |
| dismissreason | 免职原因 | BasedataField | fdismissreasonid |  | hbss_apptreasongroup |
| dismissreasondetail | 免职原因详述 | TextAreaField | fdismissreasondetail |  |  |
| company | 所属公司 | HRAdminOrgField | fcompanyid |  | haos_adminorghrf7 |
| postype | 任职类型 | BasedataField | fpostypeid |  | hbss_postype |
| number | 任免编号 | TextField | fnumber |  |  |
| posstatus | 任职状态 | BasedataField | fposstatus |  | hbss_poststate |
| empstage | 雇佣阶段 | BasedataField | fempstageid |  | hrpi_empstage |
| adminorgvid | 历史行政组织 | HRAdminOrgField | fadminorgvid |  | haos_adminorghrf7 |
| positionvid | 历史岗位 | HRPositionField | fpositionvid |  | hbpm_positionhrf7 |
| position | 岗位 | HRPositionField | fpositionid |  | hbpm_positionhrf7 |
| adminorg | 行政组织 | HRAdminOrgField | fadminorgid |  | haos_adminorghrf7 |
| job | 职位 | BasedataField | fjobid |  | hbjm_jobhr |

