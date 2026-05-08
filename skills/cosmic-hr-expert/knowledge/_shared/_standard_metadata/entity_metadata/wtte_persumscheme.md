---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_persumscheme — 期间汇总显示方案

**表单编码**: `wtte_persumscheme`  
**表单ID**: `2HR6H8NPZOZC`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_persumscheme（期间汇总显示方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_persumscheme` | 主表 · 27 列 |
| `t_wtte_persumdisentry` | 分录表 · 5 列 |
| `t_wtte_persumsortentry` | 分录表 · 3 列 |
| `t_wtte_persumscheme_l` | 多语言表 · 4 列 |
| `t_wtte_persumdisentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_persumscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_persumscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_persumscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_persumscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_persumscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_persumscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_persumscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_persumscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_persumscheme.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtte_persumscheme.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtte_persumscheme.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtte_persumscheme.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtte_persumscheme.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtte_persumscheme.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtte_persumscheme.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtte_persumscheme.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_persumscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_persumscheme_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_persumscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_persumscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_persumscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtte_persumscheme.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtte_persumscheme.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtte_persumscheme_l.foriname |  |  |
| dailydetscheme | 关联日明细显示方案 | BasedataField | t_wtte_persumscheme.fdailydetschemeid | ✓ | wtte_dailydetscheme |

## 实体: displayentryentity（显示字段单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldname | 字段名称 | TextField | t_wtte_persumdisentry.ffieldname | ✓ |  |
| displayname | 显示名称 | MuliLangTextField | t_wtte_persumdisentry_l.fdisplayname |  |  |
| freeze | 冻结 | CheckBoxField | t_wtte_persumdisentry.ffreeze |  |  |
| fieldsource | 字段来源 | TextField | t_wtte_persumdisentry.ffieldsource | ✓ |  |
| fieldvalue | 字段值 | TextField | t_wtte_persumdisentry.ffieldvalue |  |  |

## 实体: sortentryentity（排序单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sortname | 字段名称 | TextField | t_wtte_persumsortentry.fsortname |  |  |
| sortmethod | 排序方式 | ComboField | t_wtte_persumsortentry.fsortmethod | ✓ |  |
| sortvalue | 字段值 | TextField | t_wtte_persumsortentry.fsortvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_persumscheme（主表） | 21 |
| t_wtte_persumdisentry | 4 |
| t_wtte_persumdisentry_l | 1 |
| t_wtte_persumscheme_l | 4 |
| t_wtte_persumsortentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
