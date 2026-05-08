---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_attfilebase — 考勤档案

**表单编码**: `wtp_attfilebase`  
**表单ID**: `2=H497E05G03`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attfilebase（考勤档案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_attfilebase` | 主表 · 51 列 |
| `t_wtp_attfilebase_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 档案编号 | TextField | t_wtp_attfilebase.fnumber |  |  |
| status | 数据状态 | BillStatusField | t_wtp_attfilebase.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_attfilebase.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_attfilebase.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_attfilebase.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_attfilebase.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_attfilebase.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_attfilebase.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 说明 | MuliLangTextField | t_wtp_attfilebase_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtp_attfilebase.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_attfilebase.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_attfilebase.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_attfilebase.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_wtp_attfilebase.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_attfilebase.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_attfilebase.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_attfilebase.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_attfilebase.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_attfilebase.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_attfilebase.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_attfilebase.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_attfilebase.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_attfilebase.fhisversion |  |  |
| mode | 考勤方式 | ComboField | — |  |  |
| card | 考勤卡号 | TextField | — |  |  |
| period | 考勤周期 | BasedataField | — | ✓ | wtp_attperiod |
| ws | 工作日程表 | BasedataField | — | ✓ | wtbd_workschedule |
| tz | 时区 | BasedataField | — | ✓ | inte_timezone |
| adminorg | 行政组织 | HRAdminOrgField | t_wtp_attfilebase.fadminorgid |  | haos_adminorghrf7 |
| vp | 休假方案 | BasedataField | — |  | wtp_vacationplan |
| tp | 出差方案 | BasedataField | — |  | wtp_taplan |
| otp | 加班方案 | BasedataField | — |  | wtp_otplan |
| ad | 补签方案 | BasedataField | — |  | wtp_suppleplan |
| idp | 增减方案 | BasedataField | — |  | wtp_incdecplan |
| ex | 异常方案 | BasedataField | — |  | wtp_exscheme |
| att | 出勤方案 | BasedataField | — |  | wtp_attendplan |
| employee | 员工 | EmployeeField | t_wtp_attfilebase.femployeeid | ✓ | hrpi_employeenewf7query |
| enddate | 档案结束日期 | DateField | t_wtp_attfilebase.fenddate | ✓ |  |
| startdate | 档案开始日期 | DateField | t_wtp_attfilebase.fstartdate | ✓ |  |
| org | 考勤管理组织 | OrgField | t_wtp_attfilebase.forgid | ✓ | bos_org |
| affiliateadminorg | 挂靠行政组织 | HRAdminOrgField | t_wtp_attfilebase.faffiliateadminorgid | ✓ | haos_adminorghrf7 |
| ismanaged | 代管员工 | CheckBoxField | t_wtp_attfilebase.fismanaged |  |  |
| dependency | 国家/地区 | BasedataField | t_wtp_attfilebase.fdependencyid | ✓ | bd_country |
| empgroup | 考勤档案分组 | BasedataField | t_wtp_attfilebase.fempgroupid |  | hbss_empgroup |
| atttag | 考勤标识 | BasedataField | t_wtp_attfilebase.fatttagid | ✓ | wtbd_attendtag |
| attstatus | 考勤状态 | BasedataPropField | — |  |  |
| wtteinfo | 核算信息 | BasedataField | t_wtp_attfilebase.fwtteinfoid |  | wtp_attstateinfo |
| saveschedule | 添加基础方案、规则方案 | CheckBoxField | — |  |  |
| workplace | 考勤地点 | BasedataField | t_wtp_attfilebase.fworkplaceid | ✓ | hbss_workplace |
| company | 所属公司 | HRAdminOrgField | t_wtp_attfilebase.fcompanyid |  | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | t_wtp_attfilebase.fjobid |  | hbjm_jobhr |
| agreedlocation | 协议工作地 | BasedataField | t_wtp_attfilebase.fagreedlocationid |  | hbss_workplace |
| qt | 定额方案 | BasedataField | — |  | wtp_qtscheme |
| empnumber | 工号 | TextField | t_wtp_attfilebase.fempnumber |  |  |
| qtfirsteffectdate | 系统最早定额生成日期 | DateField | — |  |  |
| usablestatus | 使用状态 | ComboField | t_wtp_attfilebase.fusablestatus |  |  |
| fm | 考勤公式方案 | BasedataField | — |  | wtp_formulaplan |
| textname | 姓名 | TextField | t_wtp_attfilebase.ftextname |  |  |
| swshift | 调班方案 | BasedataField | — |  | wtp_swshiftplan |
| sourcetype | 来源类型 | ComboField | t_wtp_attfilebase.fsourcetype |  |  |
| qf | 定额公式方案 | BasedataField | — |  | wtp_qtformulaplan |
| postype | 任职类型 | BasedataField | t_wtp_attfilebase.fpostypeid |  | hbss_postype |
| hrworkplace | 常驻工作地 | BasedataField | t_wtp_attfilebase.fhrworkplaceid |  | hbss_workplace |
| empposorgrel | 关联任职 | EmployeeField | t_wtp_attfilebase.fempposorgrelid | ✓ | hrpi_empposf7query |
| persongroup | 员工组 | BasedataField | t_wtp_attfilebase.fpersongroupid |  | hbss_employeegroup |
| assignment | 组织分配 | EmployeeField | t_wtp_attfilebase.fassignmentid | ✓ | hrpi_assignmentf7query |
| sourcedata | 来源数据 | TextField | t_wtp_attfilebase.fsourcedata |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_wtp_attfilebase.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_attfilebase（主表） | 48 |
| t_wtp_attfilebase_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
