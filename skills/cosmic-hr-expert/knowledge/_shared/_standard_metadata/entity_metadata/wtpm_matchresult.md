---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FSW4YM0ZO
app_number: wtpm
app_name: 打卡管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtpm_matchresult — 取卡匹配

**表单编码**: `wtpm_matchresult`  
**表单ID**: `2MQJ8=O3SJY2`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_matchresult（取卡匹配） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_matchresult` | 主表 · 11 列 |
| `t_wtpm_matchresult_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_matchresult.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_matchresult.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_matchresult.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_matchresult.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| version | 任务号 | TextField | t_wtpm_matchresult.fversion |  |  |
| task | 匹配任务 | BasedataField | t_wtpm_matchresult.ftaskid |  | wtpm_matchtask |
| startdate | 开始日期 | DateField | t_wtpm_matchresult.fstartdate |  |  |
| enddate | 结束日期 | DateField | t_wtpm_matchresult.fenddate |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_matchresult.forgid |  | bos_org |
| desc | 描述 | MuliLangTextField | t_wtpm_matchresult_l.fdesc |  |  |
| cost | 耗时 | TextField | — |  |  |
| taskstatus | 任务状态(废弃) | ComboField | t_wtpm_matchresult.ftaskstatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_matchresult（主表） | 10 |
| t_wtpm_matchresult_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
