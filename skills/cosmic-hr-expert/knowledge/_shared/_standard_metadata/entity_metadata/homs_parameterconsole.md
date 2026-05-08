---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_parameterconsole — 参数配置

**表单编码**: `homs_parameterconsole`  
**表单ID**: `2DNHF68ZJ8DX`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_parameterconsole（参数配置） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| orgfield | 组织 | OrgField | — |  | bos_org |
| subsystem | 子系统 | TextField | — |  |  |
| viewtype | 职能类型 | TextField | — |  |  |
| acctbook | 账簿 | TextField | — |  |  |
| acctingbook | 会计账簿 | TextField | — |  |  |
| orgfieldlist | 组织 | ComboField | — | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
