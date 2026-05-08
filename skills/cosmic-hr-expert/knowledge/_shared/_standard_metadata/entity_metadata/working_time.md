---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 83bfebc8000037ac
app_number: base
app_name: 企业建模
cloud_number: BAMP
cloud_name: 基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# working_time — 工作日模式

**表单编码**: `working_time`  
**表单ID**: `1JT10EXRFGH=`  
**归属**: 基础服务云 / 企业建模  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: working_time（工作日模式） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_int_workingtime` | 主表 · 14 列 |
| `t_int_workingtimeentry` | 分录表 · 3 列 |
| `t_int_workingtime_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| modifier | 修改人 | ModifierField | t_int_workingtime.fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_int_workingtime.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_int_workingtime.fmodifytime |  |  |
| enable | 使用状态 | BillStatusField | t_int_workingtime.fenable |  |  |
| number | 编码 | TextField | t_int_workingtime.fnumber | ✓ |  |
| startdate | 开始日期 | DateField | t_int_workingtime.fstartdate |  |  |
| cyclemode | 模式 | ComboField | t_int_workingtime.fcyclemode | ✓ |  |
| creator | 创建人 | CreaterField | t_int_workingtime.fcreatorid |  | bos_user |
| disabler | 禁用人 | ModifierField | t_int_workingtime.fdisablerid |  | bos_user |
| disabletime | 禁用时间 | ModifyDateField | t_int_workingtime.fdisabletime |  |  |
| status | 数据状态 | BillStatusField | t_int_workingtime.fstatus |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| day | 天 | TextField | t_int_workingtimeentry.fday |  |  |
| datetype | 日期类型 | ComboField | t_int_workingtimeentry.fdatetype | ✓ |  |
| hours | 工作时段编码 | BasedataField | t_int_workingtimeentry.fhoursid |  | working_hours |
| hourssummary | 具体时间 | BasedataPropField | — |  |  |
| summary | 描述 | TextField | — |  |  |
| masterid | 主数据内码 | MasterIdField | t_int_workingtime.fmasterid |  |  |
| name | 名称 | MuliLangTextField | t_int_workingtime_l.fname | ✓ |  |
| issystem | 是否系统预置 | CheckBoxField | t_int_workingtime.fissystem |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_int_workingtime（主表） | 13 |
| t_int_workingtime_l | 1 |
| t_int_workingtimeentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
