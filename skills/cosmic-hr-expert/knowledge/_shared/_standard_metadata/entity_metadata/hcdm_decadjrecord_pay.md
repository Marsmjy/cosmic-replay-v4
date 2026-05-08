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

# hcdm_decadjrecord_pay — 算薪数据准备-定调薪数据

**表单编码**: `hcdm_decadjrecord_pay`  
**表单ID**: `5G1M1Q8JG8YR`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_decadjrecord_pay（算薪数据准备-定调薪数据） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_decadjrecord` | 主表 · 53 列 |
| `t_hcdm_decadjrecord_l` | 多语言表 · 8 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hcdm_decadjrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hcdm_decadjrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hcdm_decadjrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_decadjrecord.fmodifytime |  |  |
| initdatasource | 初始化数据来源 | ComboField | t_hcdm_decadjrecord.finitdatasource |  |  |
| isdeleted | 已删除 | CheckBoxField | t_hcdm_decadjrecord.fisdeleted |  |  |
| iscurrentdata | 当前数据 | CheckBoxField | t_hcdm_decadjrecord.fiscurrentdata |  |  |
| startdate | 生效日期 | DateField | t_hcdm_decadjrecord.fstartdate | ✓ |  |
| enddate | 失效日期 | DateField | t_hcdm_decadjrecord.fenddate | ✓ |  |
| payrollgroup | 薪资核算组 | BasedataField | t_hcdm_decadjrecord.fpayrollgroupid |  | hsas_payrollgrp |
| usagecount | 使用次数 | IntegerField | t_hcdm_decadjrecord.fusagecount |  |  |
| hisjobname | 历史职位 | MuliLangTextField | t_hcdm_decadjrecord_l.fhisjobname |  |  |
| hisadminorgname | 历史行政组织 | MuliLangTextField | t_hcdm_decadjrecord_l.fhisadminorgname |  |  |
| hiscompanyname | 历史公司 | MuliLangTextField | t_hcdm_decadjrecord_l.fhiscompanyname |  |  |
| salaryadjustrsn | 定调薪类型 | BasedataField | t_hcdm_decadjrecord.fsalaryadjustrsnid |  | hsbs_salaryadjustrsn |
| standarditem | 定调薪项目 | BasedataField | t_hcdm_decadjrecord.fstandarditemid | ✓ | hsbs_standarditem |
| employee | 员工 | EmployeeField | t_hcdm_decadjrecord.femployeeid |  | hsbs_employeequery |
| frequency | 频度 | BasedataField | t_hcdm_decadjrecord.ffrequencyid | ✓ | hsbs_calfrequency |
| salarygrade | 薪等 | BasedataField | t_hcdm_decadjrecord.fsalarygradeid |  | hsbs_salarygrade |
| empposorgrel | 员工任职 | EmployeeField | t_hcdm_decadjrecord.fempposorgrelid | ✓ | hsbs_empposf7query |
| salaryrank | 薪档 | BasedataField | t_hcdm_decadjrecord.fsalaryrankid |  | hsbs_salaryrank |
| currency | 币种 | CurrencyField | — | ✓ | bd_currency |
| amount | 金额 | AmountField | t_hcdm_decadjrecord.famount | ✓ |  |
| overstandardtype | 超标准 | ComboField | — |  |  |
| adjfilev | 定调薪档案版本 | BasedataField | t_hcdm_decadjrecord.fadjfilevid |  | hcdm_adjfileinfo |
| datasource | 定调薪数据来源 | ComboField | t_hcdm_decadjrecord.fdatasource |  |  |
| bussinessid | 数据来源业务ID | BigIntField | t_hcdm_decadjrecord.fbussinessid |  |  |
| issend | 试用期全额发放 | ComboField | — |  |  |
| salarystdv | 薪酬标准表版本 | BasedataField | — |  | hcdm_salarystandard |
| intervalmin | 薪酬标准金额Min | AmountField | — |  |  |
| intervalmax | 薪酬标准金额Max | AmountField | — |  |  |
| baselocation | 常驻工作地 | BasedataField | — |  | hbss_workplace |
| graderankrange | 薪等薪档区间 | TextField | — |  |  |
| country | 薪酬管理属地 | BasedataField | t_hcdm_decadjrecord.fcountryid |  | bd_country |
| hcdmorg | 薪酬管理组织 | OrgField | t_hcdm_decadjrecord.fhcdmorgid |  | bos_org |
| empgroup | 定调薪档案分组 | BasedataField | t_hcdm_decadjrecord.fempgroupid |  | hbss_empgroup |
| company | 所属公司 | HRAdminOrgField | t_hcdm_decadjrecord.fcompanyid |  | haos_adminorghrf7 |
| adminorg | 行政组织 | HRAdminOrgField | t_hcdm_decadjrecord.fadminorgid |  | haos_adminorghrf7 |
| manageadminorg | 管理部门 | HRAdminOrgField | t_hcdm_decadjrecord.fmanageadminorgid |  | haos_adminorghrf7 |
| job | 职位 | BasedataField | t_hcdm_decadjrecord.fjobid |  | hbjm_jobhr |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| joblevel | 职级 | BasedataField | t_hcdm_decadjrecord.fjoblevelid |  | hbjm_joblevelhr |
| jobgrade | 职等 | BasedataField | t_hcdm_decadjrecord.fjobgradeid |  | hbjm_jobgradehr |
| laborreltype | 用工关系类型 | BasedataField | — |  | hbss_laborreltype |
| laborrelstatus | 用工关系状态 | BasedataField | t_hcdm_decadjrecord.flaborrelstatusid |  | hbss_laborrelstatus |
| contrworkloc | 协议工作地 | BasedataField | — |  | hbss_workplace |
| entrydate | 入职日期 | DateField | — |  |  |
| realregulardate | 转正日期 | DateField | — |  |  |
| jobscm | 职位体系方案 | BasedataField | — |  | hbjm_jobscmhr |
| jobseq | 职位序列 | BasedataField | — |  | hbjm_jobseqhr |
| jobfamily | 职位族 | BasedataField | — |  | hbjm_jobfamilyhr |
| jobclass | 职位类 | BasedataField | — |  | hbjm_jobclasshr |
| postype | 任职类型 | BasedataField | — |  | hbss_postype |
| admindivision | 组织所在城市 | BasedataField | — |  | bd_admindivision |
| workplace | 工作地所在城市 | BasedataField | — |  | bd_admindivision |
| industrytype | 行业类别 | BasedataField | — |  | hbss_industrytype |
| operationequal | 主要执业资格 | BasedataField | — |  | hbss_operationqual |
| eocpquallevel | 主要执业资格等级 | BasedataField | — |  | hbss_ocpquallevel |
| protitle | 最高职称 | BasedataField | t_hcdm_decadjrecord.fprotitleid |  | hbss_protitle |
| protitlelevel | 最高职称级别 | BasedataField | — |  | hbss_protitlelevel |
| protitletype | 最高职称类别 | BasedataField | — |  | hbss_protitletype |
| pocpquallevel | 主要职业资格等级 | BasedataField | — |  | hbss_ocpquallevel |
| ocpqual | 主要职业资格 | BasedataField | — |  | hbss_ocpqual |
| schooltype | 最高学历院校特性 | MulBasedataField | — |  |  |
| diploma | 最高学历 | BasedataField | — |  | hbss_diploma |
| religion | 宗教 | BasedataField | — |  | hbss_religion |
| nationality | 国籍 | BasedataField | t_hcdm_decadjrecord.fnationalityid |  | hbss_nationality |
| stdscmv | 薪酬体系版本 | BasedataField | t_hcdm_decadjrecord.fstdscmvid |  | hcdm_stdscm |
| salarystructure | 薪酬结构 | BasedataField | t_hcdm_decadjrecord.fsalarystructureid |  | hcdm_salaystructure |
| assignment | 组织分配号 | BasedataField | t_hcdm_decadjrecord.fassignmentid |  | hsbs_assignment |
| biznumber | 来源单据 | TextField | t_hcdm_decadjrecord.fbiznumber |  |  |
| graderankshow | 薪等薪档区间 | MuliLangTextField | t_hcdm_decadjrecord_l.fgraderankshow |  |  |
| reason | 调薪原因 | MuliLangTextField | t_hcdm_decadjrecord_l.freason |  |  |
| remark | 备注 | MuliLangTextField | t_hcdm_decadjrecord_l.fremark |  |  |
| adminorgtype | 行政组织类型 | BasedataField | — |  | haos_adminorgtype |
| stdmiddlevalue | 薪酬标准金额中位值 | AmountField | — |  |  |
| matchtype | 匹配类型 | ComboField | — |  |  |
| isusesalaryrank | 启用薪档 | CheckBoxField | — |  |  |
| bizentity | 来源单据实体 | TextField | t_hcdm_decadjrecord.fbizentity |  |  |
| excesscontrol | 超标准控制 | ComboField | — |  |  |
| matchstrategy | 匹配策略 | BasedataField | — |  | hcdm_stdmatchstrategy |
| stdcurrency | 标准表币种 | CurrencyField | — |  | bd_currency |
| preamount | 上一次金额 | AmountField | — |  |  |
| precurrency | 上一次币种 | CurrencyField | — |  | bd_currency |
| presalarystdv | 上一次标准表版本 | BasedataField | — |  | hcdm_salarystandard |
| prefrequency | 上一次频度 | BasedataField | — |  | hsbs_calfrequency |
| presalarygrade | 上一次薪等 | BasedataField | — |  | hsbs_salarygrade |
| presalaryrank | 上一次薪档 | BasedataField | — |  | hsbs_salaryrank |
| pregraderankshow | 上一次薪等薪档区间 | MuliLangTextField | t_hcdm_decadjrecord_l.fpregraderankshow |  |  |
| adjfile | 定调薪档案 | BasedataField | t_hcdm_decadjrecord.fadjfileid |  | hcdm_adjfileinfo |
| revisionreason | 修订原因 | MuliLangTextField | t_hcdm_decadjrecord_l.frevisionreason |  |  |
| stdcoefficientv | 薪酬标准系数版本 | MulBasedataField | — |  |  |
| coefficientvalue | 系数值 | DecimalField | t_hcdm_decadjrecord.fcoefficientvalue |  |  |
| datastatus | 数据状态 | TextField | t_hcdm_decadjrecord.fdatastatus |  |  |
| index | 排序号 | IntegerField | t_hcdm_decadjrecord.findex |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_decadjrecord（主表） | 43 |
| t_hcdm_decadjrecord_l | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 44 |
