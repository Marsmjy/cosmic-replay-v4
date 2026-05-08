---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15=TGRTUNG1B
app_number: wtam
app_name: 日常考勤
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtam_attconfirmbill — 考勤确认单

**表单编码**: `wtam_attconfirmbill`  
**表单ID**: `40YB0F71/IJ9`  
**归属**: 工时假勤云 / 日常考勤  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtam_attconfirmbill（考勤确认单） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtam_attconfirmbill` | 主表 · 6 列 |
| `t_wtam_attconbillentry` | 分录表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtam_attconfirmbill.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtam_attconfirmbill.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtam_attconfirmbill.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtam_attconfirmbill.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| attconrecord | 考勤确认记录 | BasedataField | t_wtam_attconfirmbill.fattconrecordid | ✓ | wtam_attconrecord |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitem | 考勤项目 | BasedataField | t_wtam_attconbillentry.fattitemid |  | wtp_dailydetsource |
| value | 最新结果 | TextField | — |  |  |
| unit | 单位 | BasedataPropField | — |  |  |
| resultvalue | 最新结果 | TextField | — |  |  |
| latestvalue | 最新结果 | TextField | — |  |  |
| lastvalue | 上次结果 | TextField | — |  |  |
| sumattitem | 考勤项目 | BasedataField | — |  | wtp_persumsource |
| sumunit | 单位 | BasedataPropField | — |  |  |
| startdate | 开始日期 | BasedataPropField | — |  |  |
| basedatapropfield2 | 结束日期 | BasedataPropField | — |  |  |
| basedatapropfield21 | 开始日期 | BasedataPropField | — |  |  |
| basedatapropfield3 | 确认截止日期 | BasedataPropField | — |  |  |
| basedatapropfield31 | 确认时间 | BasedataPropField | — |  |  |
| basedatapropfield32 | 确认截止日期 | BasedataPropField | — |  |  |
| basedatapropfield411 | 撤回时间 | BasedataPropField | — |  |  |
| isshowdiff | 显示差异 | CheckBoxField | — |  |  |

## 实体: diffentryentity（差异单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| diff_attitem | 考勤项目 | BasedataField | — |  | wtp_dailydetsource |
| diff_latestvalue | 最新结果 | TextField | — |  |  |
| diff_lastvalue | 上次结果 | TextField | — |  |  |
| diff_unit | 单位 | BasedataPropField | — |  |  |
| changedescription | 变化说明 | ComboField | — |  |  |
| diff_sumattitem | 考勤项目 | BasedataField | — |  | wtp_persumsource |
| diff_sumunit | 单位 | BasedataPropField | — |  |  |
| attfilevid | 考勤档案版本 | BasedataField | t_wtam_attconfirmbill.fattfilevid | ✓ | wtp_attfilebase |
| attstatus | 确认状态 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtam_attconfirmbill（主表） | 6 |
| t_wtam_attconbillentry | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 24 |
