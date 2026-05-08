---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_callistfieldcfgview — 核算名单字段配置

**表单编码**: `hsbs_callistfieldcfgview`  
**表单ID**: `2+U0RH=OMDSR`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_callistfieldcfgview（核算名单字段配置） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| type | 字段类型 | ComboField | — |  |  |
| isusedbyrule | 核算名单规则 | CheckBoxField | — |  |  |
| isusedbyviewsch | 核算名单显示方案 | CheckBoxField | — |  |  |
| name | 字段名称 | MuliLangTextField | — |  |  |
| field | 表字段 | TextField | — |  |  |
| perruleobj | 对象 | BasedataField | — |  | hsbs_perruleobj |
| number | 字段编码 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
