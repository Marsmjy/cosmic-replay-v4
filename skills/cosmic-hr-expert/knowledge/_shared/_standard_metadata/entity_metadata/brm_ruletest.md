---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1IMTC4ANI0KA
app_number: brm
app_name: 业务规则管理
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: DynamicFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# brm_ruletest — 规则测试

**表单编码**: `brm_ruletest`  
**表单ID**: `1IMW3PXBWFZ8`  
**归属**: HR基础服务云 / 业务规则管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: brm_ruletest（规则测试） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bizapp | 应用 | BasedataField | — | ✓ | hbp_devportal_bizapp |
| scene | 场景 | BasedataField | — | ✓ | brm_scene |
| bu | 组织 | OrgField | — | ✓ | bos_org |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| policyname | 策略编码 | TextField | — |  |  |
| rulenumber | 规则编码 | TextField | — |  |  |
| rulename | 规则名称 | TextField | — |  |  |
| resultvalue | 结果值 | TextField | — |  |  |
| policymode | 策略模式 | ComboField | — |  |  |
| returnresult | 返回结果 | ComboField | — |  |  |
| resulttype | 结果类型 | ComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
