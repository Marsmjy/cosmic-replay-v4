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

# wtpm_multicard — 多次卡记录

**表单编码**: `wtpm_multicard`  
**表单ID**: `2HV95XBD/X7Z`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_multicard（多次卡记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_multicard` | 主表 · 15 列 |
| `t_wtpm_multicardentry` | 分录表 · 14 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_multicard.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_multicard.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_multicard.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_multicard.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_wtpm_multicard.finitdatasource |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_multicard.forgid |  | bos_org |
| attcard | 考勤卡号 | TextField | t_wtpm_multicard.fattcard |  |  |
| shiftdate | 班次日期 | DateField | t_wtpm_multicard.fshiftdate |  |  |
| shift | 班次 | BasedataField | t_wtpm_multicard.fshiftid |  | wtbd_shift |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| effectivepoint | 有效卡点 | DateTimeField | t_wtpm_multicardentry.feffectivepoint |  |  |
| source | 打卡来源 | BasedataField | t_wtpm_multicardentry.fsourceid |  | wtbd_signsource |
| mustpoint | 应打卡点 | DateTimeField | t_wtpm_multicardentry.fmustpoint |  |  |
| pointdesc | 卡点说明 | BasedataField | t_wtpm_multicardentry.fpointdescid |  | wtbd_punchcardpoint |
| accesstag | 进出卡 | ComboField | t_wtpm_multicardentry.faccesstag |  |  |
| pointtag | 卡点符号标识 | ComboField | t_wtpm_multicardentry.fpointtag |  |  |
| multipointutc | 0时区有效卡点 | DateTimeField | t_wtpm_multicardentry.fmultipointutc |  |  |
| timezone | 时区 | BasedataField | t_wtpm_multicardentry.ftimezoneid |  | inte_timezone |
| matchdate | 班次日期 | DateField | t_wtpm_multicardentry.fmatchdate |  |  |
| device | 打卡设备 | BasedataField | t_wtpm_multicardentry.fdeviceid |  | wtpm_punchcardequip |
| applyreason | 补签原因 | BasedataField | t_wtpm_multicardentry.fapplyreasonid |  | wtbd_reason |
| presetbiz1 | 预留业务字段1 | TextField | t_wtpm_multicardentry.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtpm_multicardentry.fpresetbiz2 |  |  |
| employeeid | 员工ID | BigIntField | t_wtpm_multicardentry.femployeeid |  |  |
| attfilebo | 考勤档案 | BasedataField | t_wtpm_multicard.fattfileboid |  | wtp_attfilebase |
| week | 星期 | ComboField | t_wtpm_multicard.fweek |  |  |
| datetype | 日期类型 | BasedataField | t_wtpm_multicard.fdatetypeid |  | wtbd_datetype |
| attfile | 考勤档案版本 | BasedataField | t_wtpm_multicard.fattfileid |  | wtp_attfilebase |
| employee | 员工 | BasedataField | t_wtpm_multicardentry.femployeeid |  | hrpi_employee |
| sourcesyskey | 来源系统唯一标识 | TextField | t_wtpm_multicard.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_multicard（主表） | 14 |
| t_wtpm_multicardentry | 15 |
