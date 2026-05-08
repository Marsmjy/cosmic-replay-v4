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

# working_hours — 工作时段

**表单编码**: `working_hours`  
**表单ID**: `1JWN1HTP6WOM`  
**归属**: 基础服务云 / 企业建模  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: working_hours（工作时段） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_int_workinghours` | 主表 · 14 列 |
| `t_int_workinghoursentry` | 分录表 · 3 列 |
| `t_int_workinghours_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| modifier | 修改人 | ModifierField | t_int_workinghours.fmodifierid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_int_workinghours.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_int_workinghours.fmodifytime |  |  |
| creator | 创建人 | CreaterField | t_int_workinghours.fcreatorid |  | bos_user |
| disabler | 禁用人 | ModifierField | t_int_workinghours.fdisablerid |  | bos_user |
| disabletime | 禁用时间 | ModifyDateField | t_int_workinghours.fdisabletime |  |  |
| status | 数据状态 | BillStatusField | t_int_workinghours.fstatus |  |  |
| enable | 使用状态 | BillStatusField | t_int_workinghours.fenable |  |  |
| number | 编码 | TextField | t_int_workinghours.fnumber | ✓ |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| starttime | 开始时间 | TimeField | t_int_workinghoursentry.fstarttime | ✓ |  |
| endtime | 结束时间 | TimeField | t_int_workinghoursentry.fendtime | ✓ |  |
| duration | 持续时长(小时) | DecimalField | t_int_workinghoursentry.fduration |  |  |
| hours | 工作时长(小时) | DecimalField | t_int_workinghours.fhours |  |  |
| hourssummary | 时段汇总 | TextField | t_int_workinghours.fhourssummary |  |  |
| masterid | 主数据内码 | MasterIdField | t_int_workinghours.fmasterid |  |  |
| name | 名称 | MuliLangTextField | t_int_workinghours_l.fname | ✓ |  |
| issystem | 是否系统预制 | CheckBoxField | t_int_workinghours.fissystem |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_int_workinghours（主表） | 13 |
| t_int_workinghours_l | 1 |
| t_int_workinghoursentry | 3 |
