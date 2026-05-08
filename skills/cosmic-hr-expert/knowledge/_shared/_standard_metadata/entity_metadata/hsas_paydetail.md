---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_paydetail — 发放明细

**表单编码**: `hsas_paydetail`  
**表单ID**: `08/D1AR+XX2/`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_paydetail（发放明细） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_paydetail` | 主表 · 43 列 |
| `t_hsas_paydetail_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_hsas_paydetail.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_hsas_paydetail.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_hsas_paydetail.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_hsas_paydetail.fmodifytime |  |  |
| description | 描述 | MuliLangTextField | t_hsas_paydetail_l.fdescription |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| calperiod | 核算期次 | TextField | t_hsas_paydetail.fcalperiod |  |  |
| periodcategory | 周期分类 | ComboField | t_hsas_paydetail.fperiodcategory |  |  |
| adminorg | 管理部门 | HRAdminOrgField | t_hsas_paydetail.fadminorgid | ✓ | haos_adminorghrf7 |
| empgroup | 薪资档案分组 | BasedataField | t_hsas_paydetail.fempgroupid |  | hbss_empgroup |
| company | 所属公司 | HRAdminOrgField | t_hsas_paydetail.fcompanyid |  | haos_adminorghrf7 |
| department | 行政组织 | HRAdminOrgField | t_hsas_paydetail.fdepartmentid | ✓ | haos_adminorghrf7 |
| position | 岗位 | HRPositionField | — |  | hbpm_positionhrf7 |
| job | 职位 | BasedataField | t_hsas_paydetail.fjobid |  | hbjm_jobhr |
| laborrelstatus | 用工关系状态 | BasedataField | t_hsas_paydetail.flaborrelstatusid |  | hbss_laborrelstatus |
| laborreltype | 用工关系类型 | BasedataField | t_hsas_paydetail.flaborreltypeid |  | hbss_laborreltype |
| payrollgroup | 薪资核算组 | BasedataField | — | ✓ | hsas_payrollgrp |
| salaryitemtype | 项目类别 | BasedataField | t_hsas_paydetail.fsalaryitemtypeid |  | hsbs_salaryitemtype |
| salaryitem | 实发项目 | BasedataField | t_hsas_paydetail.fsalaryitemid | ✓ | hsbs_salaryitem |
| paycurrency | 收款币种 | CurrencyField | — | ✓ | bd_currency |
| payrate | 发放比例(%) | DecimalField | t_hsas_paydetail.fpayrate |  |  |
| payamount | 收款金额 | AmountField | t_hsas_paydetail.fpayamount | ✓ |  |
| paystate | 付款状态 | BillStatusField | t_hsas_paydetail.fpaystate | ✓ |  |
| calamount | 核算金额 | AmountField | t_hsas_paydetail.fcalamount | ✓ |  |
| calcurrency | 核算币种 | CurrencyField | — | ✓ | bd_currency |
| exrate | 汇率 | DecimalField | — |  |  |
| exratetable | 汇率表 | BasedataField | — |  | bd_exratetable |
| exratedate | 汇率日期 | DateField | — |  |  |
| exquotation | 汇率标价法 | ComboField | t_hsas_paydetail.fexquotation |  |  |
| oriamount | 原币金额(废弃) | AmountField | t_hsas_paydetail.foriamount |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_paydetail.forgid | ✓ | bos_org |
| agencypaystate | 代发单状态 | BillStatusField | t_hsas_paydetail.fagencypaystate |  |  |
| agencypaybill | 最新代发单 | BasedataField | t_hsas_paydetail.fagencypaybillid |  | hsas_agencypaydata |
| number | 编码 | TextField | t_hsas_paydetail.fnumber |  |  |
| onholdstatus | 停缓发状态 | ComboField | t_hsas_paydetail.fonholdstatus |  |  |
| caltableid | 核算大表ID | BigIntField | t_hsas_paydetail.fcaltableid |  |  |
| caltask | 薪资核算向导 | BasedataField | t_hsas_paydetail.fcaltaskid |  | hsas_calpayrolltask |
| payrolldate | 薪资所属年月 | DateField | t_hsas_paydetail.fpayrolldate | ✓ |  |
| bankofferstatus | 银行报盘状态 | ComboField | — |  |  |
| bankoffertimes | 报盘导出次数 | IntegerField | — |  |  |
| bankofferlog | 最新报盘文件 | BasedataField | — |  | hsas_bankreportlog |
| abandonedres | 废弃原因 | MuliLangTextField | t_hsas_paydetail_l.fabandonedres |  |  |
| abandonedstatus | 是否废弃 | CheckBoxField | t_hsas_paydetail.fabandonedstatus |  |  |
| payedtime | 付款完成时间 | DateTimeField | — |  |  |
| bankofferfailureres | 报盘失效原因 | TextAreaField | — |  |  |
| paytype | 支付形式 | BasedataField | — |  | hsbs_paymethod |
| ismustbankcard | 支付形式是否需要选择银行卡 | CheckBoxField | t_hsas_paydetail.fismustbankcard |  |  |
| acctmodifybill | 银行卡变更单 | BasedataField | — |  | hsas_acctmodifydata |
| exratetype | 换算方式 | ComboField | — |  |  |
| calpersonid | 核算名单ID | BigIntField | — |  |  |
| approvebill | 薪资审批单 | BasedataField | — |  | hsas_approvebillbasedata |
| agentpaybank | 付款银行 | BasedataField | — |  | bd_finorginfo |
| agentpayaccount | 付款账号 | TextField | — |  |  |
| agentpayorg | 付款资金组织 | OrgField | — |  | bos_org |
| payrollscene | 薪资核算场景 | BasedataField | — |  | hsas_payrollscene |
| perbankcard | 银行卡 | BasedataField | — |  | hsbs_perbankcard |
| salaryfilehis | 薪资档案历史版本 | HisModelBasedataField | — | ✓ | hsas_salaryfile |
| username | 账户名 | BasedataPropField | — |  |  |
| accountrelation | 账户关系 | BasedataPropField | — |  |  |
| bankdeposit | 收款银行 | BasedataPropField | — |  |  |
| bankcardnum | 银行账号 | BasedataPropField | — |  |  |
| paysettinghis | 发放设置历史版本 | HisModelBasedataField | — |  | hsas_paysetting |
| paysubjecthis | 支付主体历史版本 | HisModelBasedataField | — |  | hsbs_paysubject |
| paysettingentryid | 所属薪资发放设置分录 | BigIntField | t_hsas_paydetail.fpaysettingentryid |  |  |
| bank | 收款银行 | BasedataField | — |  | bd_bebank |
| svcdaemoninst | 服务守护者实例 | BasedataField | — |  | hsbs_svcdaemoninst |
| lockuuid | 锁UUID | TextField | — |  |  |
| operate | 操作 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_paydetail（主表） | 32 |
| t_hsas_paydetail_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 34 |
