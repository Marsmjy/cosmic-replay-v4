---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: WidgetFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_calplatformcard — 专员工作台

**表单编码**: `hsas_calplatformcard`  
**表单ID**: `2A4/8ZRVC5H+`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calplatformcard（专员工作台） [MainEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| payrollgroup | 薪资核算组 | BasedataField | — |  | hsas_payrollgrp |
| payrollscene | 薪资核算场景 | BasedataField | — |  | hsas_payrollscene |
| calperiod | 薪资期间 | BasedataField | — |  | hsbs_calperiod |
| calperiodtype | 期间类型 | BasedataField | — |  | hsbs_calperiodtype |
| iscontaintemp | 含临时核算任务 | CheckBoxField | — |  |  |
| group | 按人员分组查看： | BasedataField | — |  | hsas_checkpersongrpent |
| range | 历史范围： | ComboField | — |  |  |
| adminorg | 筛选组织： | HRAdminOrgField | — |  | haos_adminorghrf7 |
| tabtype | 页签类型 | TextField | — |  |  |
| currentpage | currentpage | TextField | — |  |  |
| totalsalaryitem | 总薪资项目 | BasedataField | — |  | hsbs_salaryitem |
| netsalaryitem | 净薪资项目 | BasedataField | — |  | hsbs_salaryitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
