---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_periodsynresult — 期间同步日志

**表单编码**: `wtp_periodsynresult`  
**表单ID**: `3FWT7CHRAU0X`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_periodsynresult（期间同步日志） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_periodsynresult` | 主表 · 10 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtp_periodsynresult.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtp_periodsynresult.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtp_periodsynresult.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtp_periodsynresult.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtp_periodsynresult.forg |  | bos_org |
| synstatus | 同步状态 | ComboField | t_wtp_periodsynresult.fsynstatus |  |  |
| periodsyntask | 期间同步任务 | BasedataField | t_wtp_periodsynresult.fperiodsyntaskid |  | wtp_periodtask |
| number | 任务号 | TextField | t_wtp_periodsynresult.fnumber |  |  |
| startdate | 开始日期 | DateField | t_wtp_periodsynresult.fstartdate |  |  |
| enddate | 结束日期 | DateField | t_wtp_periodsynresult.fenddate |  |  |
| costtime | 耗时 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_periodsynresult（主表） | 10 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
