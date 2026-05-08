---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1ANC8T4UC434
app_number: pcs
app_name: 薪酬成本
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# pcs_allottask — 成本分摊任务

**表单编码**: `pcs_allottask`  
**表单ID**: `2K91/S4/I/ZR`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_allottask（成本分摊任务） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_allottask` | 主表 · 8 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_pcs_allottask.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_pcs_allottask.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_pcs_allottask.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_pcs_allottask.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| caltask | 核算任务 | BasedataField | t_pcs_allottask.fcaltaskid |  | hsas_calpayrolltask |
| viewdetail | 查看图标 | MulComboField | — |  |  |
| costtaskstatus | 成本任务状态 | ComboField | t_pcs_allottask.fcosttaskstatus |  |  |
| hrorg | 算发薪管理组织 | OrgField | — |  | bos_org |
| costadapter | 成本核算方案 | BasedataField | t_pcs_allottask.fcostadapterid |  | lcs_costadaption |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_allottask（主表） | 7 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
