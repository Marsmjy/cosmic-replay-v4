---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_varule — 休假规则

**表单编码**: `wtp_varule`  
**表单ID**: `1H=ZUWFNH=AL`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_varule（休假规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_varule` | 主表 · 32 列 |
| `t_wtp_varuleentry` | 分录表 · 18 列 |
| `t_wtp_varule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_varule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_varule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_varule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_varule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_varule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_varule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_varule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_varule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_varule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_varule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_varule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_varule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_varule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_varule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_varule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_varule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_varule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_varule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_varule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_varule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_varule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_varule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_varule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_varule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_varule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_varule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_varule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_varule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_varule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_varule.fhisversion |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| vatype | 休假类型 | BasedataField | t_wtp_varuleentry.fvatypeid |  | wtbd_vacationtype |
| vabasesetid | 休假基础配置 | BasedataField | t_wtp_varuleentry.fvabasesetid |  | wtp_vabaseset |
| vachangesetid | 休假变更配置 | BasedataField | t_wtp_varuleentry.fvachangesetid |  | wtp_vachangeset |
| attpolicy | 休假制度 | BasedataField | t_wtp_varuleentry.fattpolicyid |  | wtbd_attpolicy |
| daterange | 计算日期范围JSON | LargeTextField | — |  |  |
| vaitem | 原始时长 | BasedataField | t_wtp_varuleentry.fvaitemid |  | wtbd_attitem |
| roundrule | 舍入规则 | BasedataField | t_wtp_varuleentry.froundruleid |  | wtbd_roundrule |
| roundruleitem | 舍入时长 | BasedataField | t_wtp_varuleentry.froundruleitemid |  | wtbd_attitem |
| resultitem | 结果时长 | BasedataField | t_wtp_varuleentry.fresultitemid |  | wtbd_attitem |
| entryboid | 分录BOID | BigIntField | t_wtp_varuleentry.fentryboid |  |  |
| basedataid | 计算规则主键 | BigIntField | t_wtp_varuleentry.fbasedataid |  |  |
| limitscope | 限定条件JSON | TextAreaField | t_wtp_varuleentry.flimitscope |  |  |
| calculname | 计算日期范围 | TextField | — |  |  |
| calculdes | 限定条件 | TextField | — |  |  |
| deductrule | 定额扣减规则 | BasedataField | t_wtp_varuleentry.fdeductruleid |  | wtp_qtdeducrule |
| daterangecondition | 日期范围JSON | TextAreaField | t_wtp_varuleentry.fdaterangecondition |  |  |
| timebyshiftitem | 班次/日历天转换时长 | BasedataField | t_wtp_varuleentry.ftimebyshiftitemid |  | wtbd_attitem |
| isquota | 额度限制 | CheckBoxField | t_wtp_varuleentry.fisquota |  |  |
| isdisposable | 是否一次性假 | CheckBoxField | t_wtp_varuleentry.fisdisposable |  |  |
| isnonquotaapply | 无额度可申请 | CheckBoxField | t_wtp_varuleentry.fisnonquotaapply |  |  |
| issplit | 申请时拆分单据 | CheckBoxField | t_wtp_varuleentry.fissplit |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_varule（主表） | 27 |
| t_wtp_varule_l | 3 |
| t_wtp_varuleentry | 18 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
