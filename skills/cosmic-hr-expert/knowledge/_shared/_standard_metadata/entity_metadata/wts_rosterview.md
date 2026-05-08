---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R5/4TCA97N
app_number: wts
app_name: 排班管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wts_rosterview — 排班管理

**表单编码**: `wts_rosterview`  
**表单ID**: `1CI=WSWCCODE`  
**归属**: 工时假勤云 / 排班管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wts_rosterview（排班管理） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| checkboxreal | 实际 | CheckBoxField | — |  |  |
| checkboxplan | 计划 | CheckBoxField | — |  |  |
| daterange | 日期范围 | DateRangeField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
