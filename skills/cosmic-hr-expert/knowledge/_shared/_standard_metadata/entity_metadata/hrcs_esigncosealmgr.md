---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15NPDX/GJFOO
app_number: hrcs
app_name: HR通用服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrcs_esigncosealmgr — 企业印章管理

**表单编码**: `hrcs_esigncosealmgr`  
**表单ID**: `3F4YGLYHTZJ6`  
**归属**: HR基础服务云 / HR通用服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrcs_esigncosealmgr（企业印章管理） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| esignsp | 电子签服务商 | BasedataField | — |  | hrcs_esignspmgr |
| curappnumber | 当前应用 | TextField | — |  |  |
| curcorpnumber | 当前企业 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
