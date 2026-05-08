---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: ReportFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_attperiodtotallist — 考勤记录-期间汇总

**表单编码**: `wtte_attperiodtotallist`  
**表单ID**: `1PZELXBQG=FN`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_attperiodtotallist（考勤记录-期间汇总） [ReportEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attperinf | 人员 | MulBasedataField | — |  |  |
| confirm | 确认 | ComboField | — |  |  |
| release | 发布 | ComboField | — |  |  |
| push | 推送 | ComboField | — |  |  |
| attperiodentry | 考勤期间 | BasedataField | — |  | wtp_attperiodentry |
| con_adminorg | 行政组织 | HRMulAdminOrgField | — |  |  |
| con_affiliateadminorg | 挂靠行政组织 | HRMulAdminOrgField | — |  |  |
| con_showplan | 显示方案 | BasedataField | — | ✓ | wtte_persumscheme |
| con_empgroup | 考勤档案分组 | MulBasedataField | — |  |  |
| con_org | 考勤管理组织 | OrgField | — | ✓ | bos_org |
| owndate | 归属日期 | DateRangeField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
