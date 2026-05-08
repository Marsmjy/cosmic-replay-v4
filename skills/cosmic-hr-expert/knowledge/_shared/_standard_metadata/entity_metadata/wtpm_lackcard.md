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

# wtpm_lackcard — 缺卡明细

**表单编码**: `wtpm_lackcard`  
**表单ID**: `31N6KVTZJZF3`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_lackcard（缺卡明细） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_lackcard` | 主表 · 20 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_lackcard.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_lackcard.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_lackcard.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_lackcard.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| attcard | 考勤卡号 | TextField | t_wtpm_lackcard.fattcard |  |  |
| shiftdate | 班次日期 | DateField | t_wtpm_lackcard.fshiftdate |  |  |
| shift | 班次 | BasedataField | t_wtpm_lackcard.fshiftid |  | wtbd_shift |
| attmode | 考勤方式 | ComboField | t_wtpm_lackcard.fattmode |  |  |
| week | 星期 | ComboField | t_wtpm_lackcard.fweek |  |  |
| datetype | 日期类型 | BasedataField | t_wtpm_lackcard.fdatetypeid |  | wtbd_datetype |
| lackpoint | 缺卡时间 | DateTimeField | t_wtpm_lackcard.flackpoint |  |  |
| pointdesc | 卡点说明 | BasedataField | t_wtpm_lackcard.fpointdescid |  | wtbd_punchcardpoint |
| attfilebo | 考勤档案 | BasedataField | t_wtpm_lackcard.fattfileboid |  | wtp_attfilebase |
| org | 考勤管理组织 | OrgField | t_wtpm_lackcard.forgid |  | bos_org |
| attfile | 考勤档案版本 | BasedataField | t_wtpm_lackcard.fattfileid |  | wtp_attfilebase |
| lackpointutc | 缺卡时间-0时区 | DateTimeField | t_wtpm_lackcard.flackpointutc |  |  |
| timezone | 时区 | BasedataField | t_wtpm_lackcard.ftimezoneid |  | inte_timezone |
| presetbiz1 | 预留业务字段1 | TextField | t_wtpm_lackcard.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtpm_lackcard.fpresetbiz2 |  |  |
| employee | 员工 | BasedataField | t_wtpm_lackcard.femployeeid |  | hrpi_employee |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_lackcard（主表） | 20 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
