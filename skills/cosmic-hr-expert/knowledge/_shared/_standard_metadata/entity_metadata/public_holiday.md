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

# public_holiday — 公共假期

**表单编码**: `public_holiday`  
**表单ID**: `1G8TAYI+/0BX`  
**归属**: 基础服务云 / 企业建模  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: public_holiday（公共假期） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_int_publicholiday` | 主表 · 22 列 |
| `t_int_publicholiday_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_int_publicholiday.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_int_publicholiday_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_int_publicholiday.fstatus |  |  |
| creator | 创建人 | CreaterField | t_int_publicholiday.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_int_publicholiday.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_int_publicholiday.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_int_publicholiday.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_int_publicholiday.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_int_publicholiday.fmasterid |  |  |
| disabler | 禁用人 | ModifierField | t_int_publicholiday.fdisablerid |  | bos_user |
| disabletime | 禁用时间 | ModifyDateField | t_int_publicholiday.fdisabletime |  |  |
| description | 描述 | MuliLangTextField | t_int_publicholiday_l.fdescription |  |  |
| type | 类型 | ComboField | t_int_publicholiday.ftype |  |  |
| month | 月 | ComboField | t_int_publicholiday.fmonth |  |  |
| weekofmonth | 第几个星期 | ComboField | t_int_publicholiday.fweekofmonth |  |  |
| holiday | 假期日 | TextField | t_int_publicholiday.fholiday |  |  |
| countryid | 国家或地区 | BasedataField | t_int_publicholiday.fcountryid |  | bd_country |
| holidayclass | 假期类型 | BasedataField | t_int_publicholiday.fholidayclass |  | int_holidayclass |
| dayofmonth | 日 | MulComboField | t_int_publicholiday.fdayofmonth |  |  |
| holidaytype | 半天 | CheckBoxField | t_int_publicholiday.fholidaytype |  |  |
| timeperiod | 工作时段 | BasedataField | t_int_publicholiday.ftimeperiod |  | working_hours |
| issystem | 是否系统预置 | CheckBoxField | t_int_publicholiday.fissystem |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_int_publicholiday（主表） | 20 |
| t_int_publicholiday_l | 2 |
