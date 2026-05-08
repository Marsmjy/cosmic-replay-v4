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

# wtpm_checkcard — 校验卡记录

**表单编码**: `wtpm_checkcard`  
**表单ID**: `2HVHRA7J2ORE`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_checkcard（校验卡记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_checkcard` | 主表 · 13 列 |
| `t_wtpm_checkcardentry` | 分录表 · 12 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_checkcard.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_checkcard.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_checkcard.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_checkcard.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_checkcard.forgid |  | bos_org |
| attcard | 考勤卡号 | TextField | t_wtpm_checkcard.fattcard |  |  |
| shiftdate | 班次日期 | DateField | t_wtpm_checkcard.fshiftdate |  |  |
| shift | 班次 | BasedataField | t_wtpm_checkcard.fshiftid |  | wtbd_shift |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| checkpoint | 校验卡点 | DateTimeField | t_wtpm_checkcardentry.fcheckpoint |  |  |
| source | 打卡来源 | BasedataField | t_wtpm_checkcardentry.fsourceid |  | wtbd_signsource |
| pointdesc | 校验卡取卡范围 | BasedataField | t_wtpm_checkcardentry.fpointdescid |  | wtbd_shiftperiod |
| accesstag | 进出卡 | ComboField | t_wtpm_checkcardentry.faccesstag |  |  |
| pointtag | 卡点标识 | ComboField | t_wtpm_checkcardentry.fpointtag |  |  |
| checkpointutc | 0时区校验卡点 | DateTimeField | t_wtpm_checkcardentry.fcheckpointutc |  |  |
| timezone | 时区 | BasedataField | t_wtpm_checkcardentry.ftimezoneid |  | inte_timezone |
| device | 打卡设备 | BasedataField | t_wtpm_checkcardentry.fdeviceid |  | wtpm_punchcardequip |
| matchdate | 班次日期 | DateField | t_wtpm_checkcardentry.fmatchdate |  |  |
| employeeid | 员工ID | BigIntField | t_wtpm_checkcardentry.femployeeid |  |  |
| presetbiz1 | 预留业务字段1 | TextField | t_wtpm_checkcardentry.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtpm_checkcardentry.fpresetbiz2 |  |  |
| attfile | 考勤档案版本 | BasedataField | t_wtpm_checkcard.fattfileid |  | wtp_attfilebase |
| attfilebo | 考勤档案 | BasedataField | t_wtpm_checkcard.fattfileboid |  | wtp_attfilebase |
| week | 星期 | ComboField | t_wtpm_checkcard.fweek |  |  |
| datetype | 日期类型 | BasedataField | t_wtpm_checkcard.fdatetypeid |  | wtbd_datetype |
| employee | 员工 | BasedataField | t_wtpm_checkcardentry.femployeeid |  | hrpi_employee |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_checkcard（主表） | 12 |
| t_wtpm_checkcardentry | 13 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
