---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_breaktime — 休息时间

**表单编码**: `wtbd_breaktime`  
**表单ID**: `17/NHD4=O95J`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_breaktime（休息时间） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_breaktime` | 主表 · 25 列 |
| `t_wtbd_breaktime_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_breaktime.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_breaktime_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_breaktime.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_breaktime.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_breaktime.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_breaktime.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_breaktime.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_breaktime.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_breaktime.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_breaktime_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_breaktime_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtbd_breaktime.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_breaktime.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_breaktime.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_breaktime.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | t_wtbd_breaktime.finitdatasource |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_breaktime.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_breaktime.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_breaktime_l.foriname |  |  |
| brestartdate | 休息开始时间 | TimeField | t_wtbd_breaktime.fbrestartdate |  |  |
| breenddate | 休息结束时间 | TimeField | t_wtbd_breaktime.fbreenddate |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_wtbd_breaktime.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_breaktime（主表） | 18 |
| t_wtbd_breaktime_l | 4 |
