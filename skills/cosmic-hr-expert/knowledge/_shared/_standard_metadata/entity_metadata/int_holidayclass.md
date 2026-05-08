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

# int_holidayclass — 假期类型

**表单编码**: `int_holidayclass`  
**表单ID**: `2484OZUD2U1Z`  
**归属**: 基础服务云 / 企业建模  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: int_holidayclass（假期类型） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_int_holidayclass` | 主表 · 11 列 |
| `t_int_holidayclass_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_int_holidayclass.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_int_holidayclass_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_int_holidayclass.fstatus |  |  |
| creator | 创建人 | CreaterField | t_int_holidayclass.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_int_holidayclass.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_int_holidayclass.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_int_holidayclass.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_int_holidayclass.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_int_holidayclass.fmasterid |  |  |
| country | 国家或地区 | BasedataField | t_int_holidayclass.fcountry |  | bd_country |
| issystem | 系统预置 | CheckBoxField | t_int_holidayclass.fissystem |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_int_holidayclass（主表） | 10 |
| t_int_holidayclass_l | 1 |
