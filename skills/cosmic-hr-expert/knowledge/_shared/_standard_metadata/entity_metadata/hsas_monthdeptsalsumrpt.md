---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: ReportFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_monthdeptsalsumrpt — 组织薪资月汇总表

**表单编码**: `hsas_monthdeptsalsumrpt`  
**表单ID**: `0KPIMQY3=K7X`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_monthdeptsalsumrpt（组织薪资月汇总表） [ReportEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| lastlevel | 最末显示层级 | ComboField | — | ✓ |  |
| isshowlower | 包含下级 | CheckBoxField | — |  |  |
| calcurrency | 核算币种 | CurrencyField | — | ✓ | bd_currency |
| laborreltype | 用工关系类型 | MulBasedataField | — |  |  |
| payrollgroup | 薪资核算组 | MulBasedataField | — |  |  |
| orgtree | 组织架构树 | ComboField | — | ✓ |  |
| adminorg | 行政组织 | HRAdminOrgField | — | ✓ | haos_adminorghrf7 |
| startperiod | 薪资起始年月 | DateField | — | ✓ |  |
| endperiod | 薪资截止年月 | DateField | — | ✓ |  |
| period | 薪资期间 | ComboField | — | ✓ |  |
| empgroup | 薪资档案分组 | MulBasedataField | — |  |  |
| payrollscene | 薪资核算场景 | MulBasedataField | — |  |  |
| countstructure | 统计架构 | ComboField | — | ✓ |  |
| attachadminorg | 管理部门 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| calstatus | 核算状态 | MulComboField | — |  |  |
| org | 算发薪管理组织 | MulBasedataField | — | ✓ |  |
| displayscheme | 显示方案 | BasedataField | — | ✓ | hsas_salaryrptdisplayschm |
| onholdstatus | 停缓发状态 | MulComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 18 |
