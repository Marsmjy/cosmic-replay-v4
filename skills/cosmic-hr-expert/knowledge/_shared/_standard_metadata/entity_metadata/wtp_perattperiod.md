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

# wtp_perattperiod — 人员考勤期间

**表单编码**: `wtp_perattperiod`  
**表单ID**: `3FJ0/U8QRIAV`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_perattperiod（人员考勤期间） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_perattperiod` | 主表 · 16 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtp_perattperiod.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtp_perattperiod.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtp_perattperiod.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtp_perattperiod.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 期间流水号 | TextField | t_wtp_perattperiod.fnumber |  |  |
| attfileid | 考勤档案 | BasedataField | t_wtp_perattperiod.fattfileid |  | wtp_attfilebase |
| attfilevid | 考勤档案版本 | BasedataField | t_wtp_perattperiod.fattfilevid |  | wtp_attfilebase |
| employee | 员工 | BasedataField | t_wtp_perattperiod.femployeeid |  | hrpi_employee |
| periodentry | 考勤期间 | BasedataField | t_wtp_perattperiod.fperiodentryid |  | wtp_attperiodentry |
| startdate | 开始日期 | DateField | t_wtp_perattperiod.fstartdate |  |  |
| enddate | 结束日期 | DateField | t_wtp_perattperiod.fenddate |  |  |
| totaldays | 总天数 | IntegerField | t_wtp_perattperiod.ftotaldays |  |  |
| busistatus | 业务状态 | ComboField | t_wtp_perattperiod.fbusistatus |  |  |
| period | 考勤周期 | BasedataField | t_wtp_perattperiod.fperiodid |  | wtp_attperiod |
| laststorage | 最晚已封存期间 | CheckBoxField | t_wtp_perattperiod.flaststorage |  |  |
| firstnotstorage | 最早未封存期间 | CheckBoxField | t_wtp_perattperiod.ffirstnotstorage |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_perattperiod（主表） | 16 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
