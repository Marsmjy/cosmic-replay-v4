---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_paysubject — 支付主体

**表单编码**: `hsbs_paysubject`  
**表单ID**: `00+FFKO/JW5Q`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_paysubject（支付主体） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_paysubject` | 主表 · 36 列 |
| `t_hsbs_payorgent` | 分录表 · 6 列 |
| `t_hsbs_paysubject_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_paysubject.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_paysubject_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_paysubject.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_paysubject.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_paysubject.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_paysubject.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_paysubject.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_paysubject.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_paysubject.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_paysubject.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_paysubject.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_paysubject.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_paysubject.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_paysubject.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_paysubject.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_paysubject.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_paysubject_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_paysubject_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_paysubject.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_paysubject.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_paysubject.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_paysubject.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_paysubject.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_paysubject.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsbs_paysubject.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_paysubject.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_paysubject.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_paysubject.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_paysubject.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_paysubject.fhisversion |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_paysubject.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_paysubject.fcountryid | ✓ | bd_country |
| isagentpay | 财务代发 | CheckBoxField | — |  |  |

## 实体: payorgent（付款信息） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| agentpayorg | 付款资金组织 | OrgField | t_hsbs_payorgent.fagentpayorgid |  | bos_org |
| isdefault | 默认 | CheckBoxField | t_hsbs_payorgent.fisdefault |  |  |
| applicationscope | 适用范围-内容 | TextAreaField | t_hsbs_payorgent.fapplicationscope |  |  |
| salaryitemmul | 适用范围–指定薪酬项目 | MulBasedataField | — |  |  |
| paymethodmul | 适用范围–指定支付形式 | MulBasedataField | — |  |  |
| currencymul | 适用范围–指定收款币种 | MulBasedataField | — |  |  |
| bankpurposemul | 适用范围：指定银行卡用途 | MulBasedataField | — |  |  |
| agentpaybank | 付款银行 | BasedataField | t_hsbs_payorgent.fagentpaybankid |  | bd_finorginfo |
| agentpayaccount | 付款账号 | TextField | t_hsbs_payorgent.fagentpayaccount |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsbs_payorgent.fentryboid |  |  |
| cashintergration | 对接出纳系统 | ComboField | t_hsbs_paysubject.fcashintergration |  |  |
| lawentity | 关联法律实体 | BasedataField | t_hsbs_paysubject.flawentityid |  | hbss_lawentity |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_paysubject（主表） | 31 |
| t_hsbs_payorgent | 6 |
| t_hsbs_paysubject_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
