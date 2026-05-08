---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1WXBPN7+OHJZ
app_number: hspm
app_name: 人员信息
cloud_number: HR
cloud_name: 核心人力云
model_type: MobileFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hspm_ermanfilemob — 员工端档案

**表单编码**: `hspm_ermanfilemob`  
**表单ID**: `4KVYSVDCOWNP`  
**归属**: 核心人力云 / 人员信息  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hspm_ermanfilemob（员工端档案） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| employee | 员工 | BasedataField | — |  | hrpi_employee |
| fileview | 多视图 | BasedataField | — |  | hspm_fileviewconfigtpl |
| assignment | 主实体 | BasedataField | — |  | hrpi_assignment |
| showcollectbox | 仅显示需采集的信息 | CheckBoxField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
