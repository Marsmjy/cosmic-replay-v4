---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1ANC8T4UC434
app_number: pcs
app_name: 薪酬成本
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# pcs_allotdetail — 成本分摊明细

**表单编码**: `pcs_allotdetail`  
**表单ID**: `1PDTQQ78N=A/`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_allotdetail（成本分摊明细） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_allotdetail` | 主表 · 51 列 |
| `t_pcs_allotdetailentry` | 分录表 · 5 列 |
| `t_pcs_allotdetail_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_pcs_allotdetail.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_pcs_allotdetail_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_pcs_allotdetail.fstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_allotdetail.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_allotdetail.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_pcs_allotdetail.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_pcs_allotdetail.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_allotdetail.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_pcs_allotdetail.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_pcs_allotdetail_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_pcs_allotdetail_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_pcs_allotdetail.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_pcs_allotdetail.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_pcs_allotdetail.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_pcs_allotdetail.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| costsetupconst | 成本分摊结构常量 | BasedataField | t_pcs_allotdetail.fcostsetupconstid | ✓ | pcs_costsetupconst |
| company | 公司 | HRAdminOrgField | t_pcs_allotdetail.fcompanyid |  | haos_adminorghrf7 |
| department | 部门 | HRAdminOrgField | t_pcs_allotdetail.fdepartmentid |  | haos_adminorghrf7 |
| persongroup | 薪资档案分组 | BasedataField | t_pcs_allotdetail.fpersongroupid |  | hbss_empgroup |
| laborreltype | 用工关系类型 | BasedataField | t_pcs_allotdetail.flaborreltypeid |  | hbss_laborreltype |
| laborrelstatus | 用工关系状态 | BasedataField | t_pcs_allotdetail.flaborrelstatusid |  | hbss_laborrelstatus |

## 实体: pcs_allotdetailentry（费用分摊明细分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| costitem | 成本项目 | BasedataField | t_pcs_allotdetailentry.fcostitemid | ✓ | lcs_costitem |
| calcurrency | 核算币种 | CurrencyField | — | ✓ | bd_currency |
| assucurrency | 承担币种 | CurrencyField | — | ✓ | bd_currency |
| calamount | 核算币承担金额 | AmountField | t_pcs_allotdetailentry.fcalamount | ✓ |  |
| assuamount | 承担金额 | AmountField | t_pcs_allotdetailentry.fassuamount | ✓ |  |
| exratedate | 汇率日期 | DateField | t_pcs_allotdetail.fexratedate |  |  |
| exquotation | 汇率标价法 | ComboField | t_pcs_allotdetail.fexquotation |  |  |
| adminorg | 管理部门 | HRAdminOrgField | t_pcs_allotdetail.fadminorgid |  | haos_adminorghrf7 |
| allotbillstatus | 成本分配单状态 | BillStatusField | t_pcs_allotdetail.fallotbillstatus |  |  |
| allotbillfeedback | 生成分配单失败原因 | MuliLangTextField | t_pcs_allotdetail_l.fallotbillfeedback |  |  |
| allotbillno | 成本分配单编号 | TextField | t_pcs_allotdetail.fallotbillno |  |  |
| caltask | 核算任务 | BasedataField | t_pcs_allotdetail.fcaltaskid | ✓ | hsas_calpayrolltask |
| dataprecision | 汇率小数位 | BasedataField | t_pcs_allotdetail.fdataprecisionid |  | hsbs_dataprecision |
| payrollgroup | 薪资核算组 | BasedataField | t_pcs_allotdetail.fpayrollgroupid |  | hsas_payrollgrp |
| allotbill | 成本分配单 | BigIntField | — |  |  |
| onholdstatus | 停缓发状态 | ComboField | t_pcs_allotdetail.fonholdstatus |  |  |
| org | 算发薪管理组织 | OrgField | t_pcs_allotdetail.forgid | ✓ | bos_org |
| caltableid | 核算记录 | BigIntField | t_pcs_allotdetail.fcaltableid |  |  |
| salaryfile | 薪资档案 | BasedataField | t_pcs_allotdetail.fsalaryfileid | ✓ | hsas_salaryfile |
| calcur | 核算币种 | CurrencyField | — |  | bd_currency |
| costsetupconstval | 人力成本维度组合值 | TextField | t_pcs_allotdetail.fcostsetupconstval |  |  |
| costsetup | 成本分摊结构 | BasedataField | t_pcs_allotdetail.fcostsetupid |  | pcs_costsetuprst |
| coststru | 人力成本维度组合 | BasedataField | t_pcs_allotdetail.fcoststruid |  | lcs_coststru |
| daterange | 薪资起止日期 | DateRangeField | — |  |  |
| paydate | 支付日期 | DateField | t_pcs_allotdetail.fpaydate |  |  |
| costadapter | 人力成本维度方案 | BasedataField | t_pcs_allotdetail.fcostadapterid | ✓ | lcs_costadaption |
| caexrate | 核算-承担汇率值 | DecimalField | t_pcs_allotdetail.fcaexrate |  |  |
| acexrate | 承担-核算汇率值 | DecimalField | t_pcs_allotdetail.facexrate |  |  |
| acexratequotetype | 承担-核算换算方式 | ComboField | t_pcs_allotdetail.facexratequotetype |  |  |
| caexratequotetype | 核算-承担换算方式 | ComboField | t_pcs_allotdetail.fcaexratequotetype |  |  |
| parentid | 父ID | BigIntField | t_pcs_allotdetail.fparentid |  |  |
| isadjustment | 是否已调整 | CheckBoxField | t_pcs_allotdetail.fisadjustment |  |  |
| belongperiod | 归属期间 | DateField | t_pcs_allotdetail.fbelongperiod |  |  |
| adjuststatus | 调整状态 | ComboField | t_pcs_allotdetail.fadjuststatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_allotdetail（主表） | 43 |
| t_pcs_allotdetail_l | 4 |
| t_pcs_allotdetailentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
