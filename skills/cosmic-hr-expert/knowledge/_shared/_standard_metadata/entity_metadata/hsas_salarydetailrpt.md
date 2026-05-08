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

# hsas_salarydetailrpt — 员工薪资明细表

**表单编码**: `hsas_salarydetailrpt`  
**表单ID**: `0R2WS3INI/R2`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_salarydetailrpt（员工薪资明细表） [ReportEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| period | 薪资期间 | ComboField | — | ✓ |  |
| startperiod | 薪资起始年月 | DateField | — | ✓ |  |
| endperiod | 薪资截止年月 | DateField | — | ✓ |  |
| empgroup | 薪资档案分组 | MulBasedataField | — |  |  |
| payrollgroup | 薪资核算组 | MulBasedataField | — |  |  |
| laborreltype | 用工关系类型 | MulBasedataField | — |  |  |
| calcurrency | 核算币种 | CurrencyField | — | ✓ | bd_currency |
| salaryitem | 薪酬项目 | MulBasedataField | — |  |  |
| employeefield | 选择人员 | TextField | — |  |  |
| org | 算发薪管理组织 | MulBasedataField | — | ✓ |  |
| isshowdisabled | 是否显示禁用 | CheckBoxField | — |  |  |
| iscontainlower | 包含下级 | CheckBoxField | — |  |  |
| displayscheme | 显示方案 | BasedataField | — | ✓ | hsas_salaryrptdisplayschm |
| belongstartdate | 薪资起始日期 | DateField | — |  |  |
| belongenddate | 薪资截止日期 | DateField | — |  |  |
| periodtype | 期间类型 | BasedataField | — |  | hsbs_calperiodtype |
| calperiod | 期间 | MulBasedataField | — |  |  |
| calstatus | 核算状态 | MulComboField | — |  |  |
| adminorg | 行政组织 | HRMulAdminOrgField | — |  |  |
| payrollscene | 薪资核算场景 | MulBasedataField | — |  |  |
| attachadminorg | 管理部门 | HRMulAdminOrgField | — |  |  |
| onholdstatus | 停缓发状态 | MulComboField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 22 |
