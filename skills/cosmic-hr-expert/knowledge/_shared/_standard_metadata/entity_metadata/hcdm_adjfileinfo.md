---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_adjfileinfo — 定调薪档案

**表单编码**: `hcdm_adjfileinfo`  
**表单ID**: `1FBBV/OR7RR+`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_adjfileinfo（定调薪档案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_adjfileinfo` | 主表 · 43 列 |
| `t_hcdm_adjfileinfo_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 档案编号 | TextField | t_hcdm_adjfileinfo.fnumber |  |  |
| name | 姓名 | MuliLangTextField | t_hcdm_adjfileinfo_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_hcdm_adjfileinfo.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_adjfileinfo.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_adjfileinfo.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_adjfileinfo.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_adjfileinfo.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_adjfileinfo.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_adjfileinfo.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_adjfileinfo_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_adjfileinfo_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hcdm_adjfileinfo.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_adjfileinfo.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_adjfileinfo.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_adjfileinfo.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_hcdm_adjfileinfo.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hcdm_adjfileinfo.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hcdm_adjfileinfo.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hcdm_adjfileinfo.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hcdm_adjfileinfo.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hcdm_adjfileinfo.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hcdm_adjfileinfo.fbsed | ✓ |  |
| bsled | 失效日期 | DateField | t_hcdm_adjfileinfo.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hcdm_adjfileinfo.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hcdm_adjfileinfo.fhisversion |  |  |
| offerid | offerid | BigIntField | t_hcdm_adjfileinfo.fofferid |  |  |
| employee | 员工 | EmployeeField | t_hcdm_adjfileinfo.femployeeid |  | hsbs_employeequery |
| assignment | 组织分配号 | EmployeeField | t_hcdm_adjfileinfo.fassignmentid |  | hsbs_assignmentquery |
| empposorgrel | 员工任职 | EmployeeField | t_hcdm_adjfileinfo.fempposorgrelid | ✓ | hsbs_empposf7query |
| org | 薪酬管理组织 | OrgField | t_hcdm_adjfileinfo.forgid | ✓ | bos_org |
| country | 薪酬管理属地 | BasedataField | t_hcdm_adjfileinfo.fcountryid | ✓ | bd_country |
| stdscm | 薪酬体系 | BasedataField | t_hcdm_adjfileinfo.fstdscmid | ✓ | hcdm_stdscm |
| manageadminorg | 管理部门 | HRAdminOrgField | t_hcdm_adjfileinfo.fmanageadminorgid | ✓ | haos_adminorghrf7 |
| empgroup | 定调薪档案分组 | BasedataField | t_hcdm_adjfileinfo.fempgroupid | ✓ | hbss_empgroup |
| job | 职位 | BasedataField | t_hcdm_adjfileinfo.fjobid |  | hbjm_jobhr |
| biznumber | 任职编号 | TextField | t_hcdm_adjfileinfo.fbiznumber |  |  |
| escrowstaff | 代管员工 | CheckBoxField | t_hcdm_adjfileinfo.fescrowstaff |  |  |
| salarystructure | 薪酬结构 | BasedataField | t_hcdm_adjfileinfo.fsalarystructureid | ✓ | hcdm_salaystructure |
| bussinessid | 数据来源业务ID | BigIntField | t_hcdm_adjfileinfo.fbussinessid |  |  |
| datasource | 数据来源 | ComboField | t_hcdm_adjfileinfo.fdatasource | ✓ |  |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_adjfileinfo.fadminorgid |  | haos_adminorghrf7 |
| filestatus | 档案状态 | ComboField | t_hcdm_adjfileinfo.ffilestatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_adjfileinfo（主表） | 39 |
| t_hcdm_adjfileinfo_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
