---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_orgchart_new — 行政组织结构图

**表单编码**: `homs_orgchart_new`  
**表单ID**: `3PHQT4NSVZKL`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_orgchart_new（行政组织结构图） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_haos_adminorg` | 主表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| boid | 行政组织id | HRAdminOrgField | t_haos_adminorg.fboid |  | haos_adminorghrf7 |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_haos_adminorg（主表） | 1 |
