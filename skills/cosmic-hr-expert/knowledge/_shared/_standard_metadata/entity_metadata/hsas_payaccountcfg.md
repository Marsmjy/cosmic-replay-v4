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

# hsas_payaccountcfg — 发放规则

**表单编码**: `hsas_payaccountcfg`  
**表单ID**: `0Z7OE80UOQV8`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_payaccountcfg（发放规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_payaccountcfg` | 主表 · 33 列 |
| `t_hsas_payaccdefrule` | 分录表 · 15 列 |
| `t_hsas_payaccspcrule` | 分录表 · 2 列 |
| `t_hsas_payspcdetail` | 分录表 · 15 列 |
| `t_hsas_payaccountcfg_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_payaccountcfg.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_payaccountcfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_payaccountcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_payaccountcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_payaccountcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_payaccountcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_payaccountcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_payaccountcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_payaccountcfg.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_payaccountcfg.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_payaccountcfg.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_payaccountcfg.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_payaccountcfg.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_payaccountcfg.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_payaccountcfg.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_payaccountcfg.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_payaccountcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_payaccountcfg_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_payaccountcfg.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_payaccountcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_payaccountcfg.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_payaccountcfg.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_payaccountcfg.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_payaccountcfg.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsas_payaccountcfg.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_payaccountcfg.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_payaccountcfg.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_payaccountcfg.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_payaccountcfg.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_payaccountcfg.fhisversion |  |  |
| country | 国家/地区 | BasedataField | t_hsas_payaccountcfg.fcountryid | ✓ | bd_country |

## 实体: opentryentity（默认规则） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| opsalaryitem | 薪酬实发项目 | ComboField | — |  |  |
| opsalaryitemdata | 指定薪酬实发项目 | BasedataField | — |  | hsbs_salaryitem |
| oppaycurrencydata | 指定收款币种 | CurrencyField | — |  | bd_currency |
| oppaymentway | 发放方式 | ComboField | — | ✓ |  |
| oppayscale | 发放比例(%) | DecimalField | — |  |  |
| oppayamount | 发放金额 | DecimalField | — |  |  |
| oppaysubjectdata | 支付主体 | BasedataField | — |  | hsbs_paysubject |
| oppaytype | 支付形式 | BasedataField | — | ✓ | hsbs_paymethod |
| opbankpurpose | 银行卡用途 | ComboField | — |  |  |
| oppayrollacrelationdata | 指定账户关系 | BasedataField | — |  | hbss_payrollacrelation |
| opbankpurposedata | 指定银行卡用途 | BasedataField | — |  | hbss_bankpurpose |
| oppayrollacrelation | 账户关系 | ComboField | — |  |  |
| oppaycurrency | 收款币种 | ComboField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_hsas_payspcdetail.fentryboid |  |  |
| opsalaryitemshow | 薪酬实发项目 | ComboField | — |  |  |
| oppaycurrencyshow | 收款币种 | ComboField | — |  |  |
| opbankpurposeshow | 银行卡用途 | ComboField | — |  |  |
| oppayrollacrelationshow | 账户关系 | ComboField | — |  |  |
| oppaysubjectshow | 支付主体 | ComboField | — |  |  |
| oppaysubject | 支付主体 | ComboField | — |  |  |

## 实体: specialentryentity（特殊规则） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| empscope | 员工范围 | TextAreaField | — |  |  |
| rulecontent | 规则内容 | TextAreaField | t_hsas_payaccspcrule.frulecontent |  |  |
| entryboidspecial | 分录BOID | BigIntField | — |  |  |
| salaryitemdata | 薪酬实发基础资料 | BasedataField | — |  | hsbs_salaryitem |
| paycurrency | 收款币种 | ComboField | — |  |  |
| paycurrencydata | 收款币种基础资料 | BasedataField | — |  | bd_currency |
| paymentway | 发放方式 | ComboField | t_hsas_payspcdetail.fpaymentway | ✓ |  |
| payamount | 发放金额 | DecimalField | t_hsas_payspcdetail.fpayamount |  |  |
| paysubjectdata | 支付主体基础资料 | BasedataField | — |  | hsbs_paysubject |
| paytype | 支付方式 | BasedataField | — | ✓ | hsbs_paymethod |
| bankpurpose | 银行卡用途 | ComboField | — |  |  |
| bankpurposedata | 银行卡用途基础资料 | BasedataField | — |  | hbss_bankpurpose |
| payrollacrelation | 账户关系 | ComboField | — |  |  |
| payrollacrelationdata | 账户关系基础资料 | BasedataField | — |  | hbss_payrollacrelation |
| entryboidspcdetail | 分录BOID | BigIntField | — |  |  |
| salaryitem | 薪酬实发项目 | ComboField | — |  |  |
| paysubject | 支付主体 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_payaccountcfg（主表） | 28 |
| t_hsas_payaccountcfg_l | 3 |
| t_hsas_payaccspcrule | 1 |
| t_hsas_payspcdetail | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 39 |
