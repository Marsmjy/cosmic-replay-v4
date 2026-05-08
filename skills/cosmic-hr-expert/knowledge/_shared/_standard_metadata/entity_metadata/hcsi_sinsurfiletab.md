---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2AXKDRPJUQ77
app_number: hcsi
app_name: 中国社保
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcsi_sinsurfiletab — 社保档案列表

**表单编码**: `hcsi_sinsurfiletab`  
**表单ID**: `5+6I+C2B6/FV`  
**归属**: 薪酬福利云 / 中国社保  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcsi_sinsurfiletab（社保档案列表） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| iscurrentversion | 是否当前版本 | CheckBoxField | — |  |  |
| sinsurfilerel | 社保档案 | BasedataField | — |  | hrcs_coordbizobject |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
