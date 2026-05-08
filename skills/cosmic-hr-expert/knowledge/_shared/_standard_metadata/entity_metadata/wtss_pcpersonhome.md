---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1Q=BI7RA+PXM
app_number: wtss
app_name: 假勤自助服务
cloud_number: WTC
cloud_name: 工时假勤云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtss_pcpersonhome — 我的假勤

**表单编码**: `wtss_pcpersonhome`  
**表单ID**: `30UWUYOD=BZP`  
**归属**: 工时假勤云 / 假勤自助服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtss_pcpersonhome（我的假勤） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|

## 实体: fastappentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fastpic | 图片字段 | PictureField | — |  |  |
| fastname | 单据名称 | TextField | — |  |  |
| fastbill | 单据 | TextField | — |  |  |
| attperiodentry | 考勤期间 | BasedataField | — | ✓ | wtp_perattperiod |
| daydate | 日期 | DateField | — | ✓ |  |

## 实体: entryentitycount（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| datasouvalue | 0 | TextField | — |  |  |
| datasouname | 名称 | TextField | — |  |  |
| unit1 | ( | TextField | — |  |  |
| unit | 天 | TextField | — |  |  |
| unit2 | ) | TextField | — |  |  |

## 实体: perentryentitycount（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| datasouvaluepre | 0 | TextField | — |  |  |
| datasounamepre | 名称 | TextField | — |  |  |
| unit11 | ( | TextField | — |  |  |
| unitpre | 天 | TextField | — |  |  |
| unit21 | ) | TextField | — |  |  |

## 实体: qtentryentitycount（定额单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| datasouvalueqt | 0 | TextField | — |  |  |
| datasounameqt | 名称 | TextField | — |  |  |
| unit111 | ( | TextField | — |  |  |
| unitqt | 天 | TextField | — |  |  |
| unit211 | ) | TextField | — |  |  |
| qtdate | 年份 | DateField | — | ✓ |  |
| attperiod | 考勤期间 | BasedataField | — | ✓ | wtp_attperiodentry |
| questionpk1 | 问题1的主键 | BigIntField | — |  |  |
| questionpk2 | 问题2的主键 | BigIntField | — |  |  |
| questionpk3 | 问题3的主键 | BigIntField | — |  |  |
| questionpk4 | 问题4的主键 | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 26 |
