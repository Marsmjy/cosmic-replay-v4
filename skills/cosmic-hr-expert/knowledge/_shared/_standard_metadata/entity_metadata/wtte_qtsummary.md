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

# wtte_qtsummary — 定额汇总

**表单编码**: `wtte_qtsummary`  
**表单ID**: `379S8ZLDZ1GV`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_qtsummary（定额汇总） [ReportEntity]

### 物理表

| 表名 | 说明 |
|------|------|

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| con_adminorg | 行政组织 | HRMulAdminOrgField | — |  |  |
| con_affiliateadminorg | 挂靠行政组织 | HRMulAdminOrgField | — |  |  |
| con_empgroup | 考勤档案分组 | MulBasedataField | — |  |  |
| con_attperson | 人员 | MulBasedataField | — |  |  |
| attfilebo | 姓名 | BasedataField | — |  | wtp_attfilebase |
| number | 工号 | BasedataPropField | — |  |  |
| attfilenumer | 档案编号 | BasedataPropField | — |  |  |
| qtname | 定额名称 | BasedataField | — |  | wtp_qttype |
| pastvalue | 失效时长 | DecimalField | — |  |  |
| usablevalue | 可用时长 | DecimalField | — |  |  |
| freezevalue | 冻结时长 | DecimalField | — |  |  |
| usedvalue | 已用时长 | DecimalField | — |  |  |
| useodvalue | 已透支时长 | DecimalField | — |  |  |
| cdedvalue | 结转时长 | DecimalField | — |  |  |
| con_genyear | 生成年份 | DateField | — |  |  |
| con_useyear | 使用年份 | DateField | — |  |  |
| orgid | 考勤管理组织 | BasedataPropField | — |  |  |
| companyvid | 所属公司 | BasedataPropField | — |  |  |
| departmentvid | 行政组织 | BasedataPropField | — |  |  |
| affiliateadminorgvid | 挂靠行政组织 | BasedataPropField | — |  |  |
| empgroup | 考勤档案分组 | BasedataPropField | — |  |  |
| dependency | 国家/地区 | BasedataPropField | — |  |  |
| workplace | 考勤地点 | BasedataPropField | — |  |  |
| positionvid | 岗位 | BasedataPropField | — |  |  |
| jobvid | 职位 | BasedataPropField | — |  |  |
| qttypeunit | 单位 | BasedataPropField | — |  |  |
| ownvalue | 享有时长 | DecimalField | — |  |  |
| con_qttype | 定额名称 | MulBasedataField | — |  |  |
| agreedlocation | 协议工作地 | BasedataPropField | — |  |  |
| canbeodvalue | 可透支时长 | DecimalField | — |  |  |
| con_multorg | 考勤管理组织 | MulBasedataField | — | ✓ |  |
| invalidodvalue | 透支失效时长 | DecimalField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| （主表） | 0 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 32 |
