---
source: openapi_runtime
extracted_at: 2026-05-02
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_holiday — 假期

**表单编码**: `wtbd_holiday`  
**表单ID**: `1===5CJKAU+V`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_holiday（假期） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_holiday` | 主表 · 32 列 |
| `t_wtbd_holidayentry` | 分录表 · 5 列 |
| `t_wtbd_holiday_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_holiday.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_holiday_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_holiday.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_holiday.fcreatorid |  | bos_user |
| modifier | 最近修改人 | ModifierField | t_wtbd_holiday.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_holiday.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_holiday.fcreatetime |  |  |
| modifytime | 最近修改时间 | ModifyDateField | t_wtbd_holiday.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_holiday.fmasterid |  |  |
| simplename | 缩写 | MuliLangTextField | t_wtbd_holiday_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_holiday_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtbd_holiday.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_holiday.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_holiday.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_holiday.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_holiday.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_holiday.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_holiday_l.foriname |  |  |
| holidayattr | 假期属性 | ComboField | t_wtbd_holiday.fholidayattr |  |  |

## 实体: unfixentry（非循环假期） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| ufstartdate | 开始日期 | DateField | t_wtbd_holidayentry.fufstartdate | ✓ |  |
| ufstarttime | 开始时间 | TimeField | t_wtbd_holidayentry.fufstarttime |  |  |
| ufenddate | 结束日期 | DateField | t_wtbd_holidayentry.fufenddate | ✓ |  |
| ufendtime | 结束时间 | TimeField | t_wtbd_holidayentry.fufendtime |  |  |
| usestatus | 状态 | ComboField | t_wtbd_holidayentry.fusestatus |  |  |
| vid | 当前历史版本 | BigIntField | t_wtbd_holiday.fvid |  |  |
| month | 指定月份 | ComboField | t_wtbd_holiday.fmonth |  |  |
| day | 放假日期 | ComboField | t_wtbd_holiday.fday |  |  |
| endtime | 结束时间 | TimeField | t_wtbd_holiday.fendtime |  |  |
| starttime | 开始时间 | TimeField | t_wtbd_holiday.fstarttime |  |  |
| dymonth | 指定月份 | ComboField | t_wtbd_holiday.fdymonth |  |  |
| week | 指定周 | ComboField | t_wtbd_holiday.fweek |  |  |
| dystarttime | 开始时间 | TimeField | t_wtbd_holiday.fdystarttime |  |  |
| dyendtime | 结束时间 | TimeField | t_wtbd_holiday.fdyendtime |  |  |
| dyday | 发放日期 | ComboField | t_wtbd_holiday.fdyday |  |  |
| holidaytype | 假期分类 | ComboField | t_wtbd_holiday.fholidaytype | ✓ |  |
| fixedtype | 循环类型 | ComboField | t_wtbd_holiday.ffixedtype |  |  |
| datetype | 日期类型 | BasedataField | t_wtbd_holiday.fdatetypeid | ✓ | wtbd_datetype |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_holiday（主表） | 28 |
| t_wtbd_holiday_l | 4 |
| t_wtbd_holidayentry | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
