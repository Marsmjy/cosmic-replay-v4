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

# wtp_supplerule — 补签规则

**表单编码**: `wtp_supplerule`  
**表单ID**: `210R9B1ZW+LF`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_supplerule（补签规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_supplerule` | 主表 · 37 列 |
| `t_wtp_suppleruleentry` | 分录表 · 3 列 |
| `t_wtp_extypeentry` | 分录表 · 3 列 |
| `t_wtp_supplerule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_supplerule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_supplerule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_supplerule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_supplerule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_supplerule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_supplerule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_supplerule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_supplerule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_supplerule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_supplerule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_supplerule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_supplerule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_supplerule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_supplerule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_supplerule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_supplerule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_supplerule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_supplerule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_supplerule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_supplerule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_supplerule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_supplerule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_supplerule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_supplerule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_supplerule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_supplerule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_supplerule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_supplerule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_supplerule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_supplerule.fhisversion |  |  |
| timeset | 时间限制配置 | BasedataField | t_wtp_supplerule.ftimesetid |  | wtp_suppletimeset |
| countset | 次数限制配置 | BasedataField | t_wtp_supplerule.fcountsetid |  | wtp_countset |

## 实体: entryentity（补签原因及制度） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| reason | 补签原因 | BasedataField | t_wtp_suppleruleentry.freasonid | ✓ | wtbd_reason |
| attpolicy | 考勤制度 | BasedataField | t_wtp_suppleruleentry.fattpolicyid |  | wtbd_attpolicy |
| entryboid | 分录BOID | BigIntField | t_wtp_extypeentry.fentryboid |  |  |
| entextsuppleset | 入离职日补签配置 | BasedataField | t_wtp_supplerule.fentextsupplesetid |  | wtp_entextsuppleset |
| onlyexsupple | 仅考勤异常时允许补签 | CheckBoxField | — |  |  |

## 实体: extypeentry（异常类型） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| exattribute | 编码 | BasedataField | t_wtp_extypeentry.fexattributeid | ✓ | wtbd_exattribute |
| entryboid2 | 分录BOID | BigIntField | — |  |  |
| basedatapropfield1 | 名称 | BasedataPropField | — |  |  |
| ctrlbyexprocess | 受异常处理方式控制 | CheckBoxField | t_wtp_extypeentry.fctrlbyexprocess |  |  |
| exattributef7 | 异常类型 | BasedataField | — |  | wtbd_exattribute |
| reasonf7 | 补签原因 | BasedataField | — |  | wtbd_reason |
| reasonnotmust | 补签原因不必填 | CheckBoxField | t_wtp_supplerule.freasonnotmust |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_supplerule（主表） | 31 |
| t_wtp_extypeentry | 3 |
| t_wtp_supplerule_l | 3 |
| t_wtp_suppleruleentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
