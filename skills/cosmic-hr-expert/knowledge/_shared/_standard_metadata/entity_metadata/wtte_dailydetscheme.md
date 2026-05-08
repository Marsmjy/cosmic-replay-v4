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

# wtte_dailydetscheme — 日明细显示方案

**表单编码**: `wtte_dailydetscheme`  
**表单ID**: `2HR6YOYJAGIR`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_dailydetscheme（日明细显示方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_dailydetscheme` | 主表 · 26 列 |
| `t_wtte_dailydetdisentry` | 分录表 · 5 列 |
| `t_wtte_dailydetsortentry` | 分录表 · 3 列 |
| `t_wtte_dailydetscheme_l` | 多语言表 · 4 列 |
| `t_wtte_dailydetdisentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtte_dailydetscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtte_dailydetscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtte_dailydetscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtte_dailydetscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtte_dailydetscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtte_dailydetscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtte_dailydetscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtte_dailydetscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtte_dailydetscheme.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtte_dailydetscheme.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtte_dailydetscheme.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtte_dailydetscheme.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtte_dailydetscheme.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtte_dailydetscheme.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtte_dailydetscheme.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtte_dailydetscheme.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtte_dailydetscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtte_dailydetscheme_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtte_dailydetscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtte_dailydetscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtte_dailydetscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtte_dailydetscheme.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtte_dailydetscheme.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtte_dailydetscheme_l.foriname |  |  |

## 实体: displayentryentity（显示字段单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fieldsource | 字段来源 | TextField | t_wtte_dailydetdisentry.ffieldsource | ✓ |  |
| fieldname | 字段名称 | TextField | t_wtte_dailydetdisentry.ffieldname | ✓ |  |
| displayname | 显示名称 | MuliLangTextField | t_wtte_dailydetdisentry_l.fdisplayname |  |  |
| freeze | 冻结 | CheckBoxField | t_wtte_dailydetdisentry.ffreeze |  |  |
| fieldvalue | 字段值 | TextField | t_wtte_dailydetdisentry.ffieldvalue |  |  |

## 实体: sortentryentity（排序单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sortname | 字段名称 | TextField | t_wtte_dailydetsortentry.fsortname |  |  |
| sortmethod | 排序方式 | ComboField | t_wtte_dailydetsortentry.fsortmethod | ✓ |  |
| sortvalue | 字段值 | TextField | t_wtte_dailydetsortentry.fsortvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_dailydetscheme（主表） | 20 |
| t_wtte_dailydetdisentry | 4 |
| t_wtte_dailydetdisentry_l | 1 |
| t_wtte_dailydetscheme_l | 4 |
| t_wtte_dailydetsortentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
