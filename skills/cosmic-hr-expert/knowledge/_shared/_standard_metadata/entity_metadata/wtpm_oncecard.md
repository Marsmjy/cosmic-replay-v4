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

# wtpm_oncecard — 一次卡记录

**表单编码**: `wtpm_oncecard`  
**表单ID**: `2HVFW/GO=WC8`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_oncecard（一次卡记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_oncecard` | 主表 · 24 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_oncecard.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_oncecard.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_oncecard.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_oncecard.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_oncecard.forgid |  | bos_org |
| attcard | 考勤卡号 | TextField | t_wtpm_oncecard.fattcard |  |  |
| shiftdate | 班次日期 | DateField | t_wtpm_oncecard.fshiftdate |  |  |
| shift | 班次 | BasedataField | t_wtpm_oncecard.fshiftid |  | wtbd_shift |
| accesstag | 进出卡 | ComboField | t_wtpm_oncecard.faccesstag |  |  |
| oncepoint | 一次卡点(长日期) | DateTimeField | t_wtpm_oncecard.foncepoint |  |  |
| source | 打卡来源 | BasedataField | t_wtpm_oncecard.fsourceid |  | wtbd_signsource |
| pointtag | 卡点标识 | ComboField | t_wtpm_oncecard.fpointtag |  |  |
| oncerange | 一次卡取卡范围 | ComboField | t_wtpm_oncecard.foncerange |  |  |
| timezone | 时区 | BasedataField | t_wtpm_oncecard.ftimezoneid |  | inte_timezone |
| oncepointutc | 一次卡点-0时区 | DateTimeField | t_wtpm_oncecard.foncepointutc |  |  |
| attfile | 考勤档案版本 | BasedataField | t_wtpm_oncecard.fattfileid |  | wtp_attfilebase |
| attfilebo | 考勤档案 | BasedataField | t_wtpm_oncecard.fattfileboid |  | wtp_attfilebase |
| week | 星期 | ComboField | t_wtpm_oncecard.fweek |  |  |
| datetype | 日期类型 | BasedataField | t_wtpm_oncecard.fdatetypeid |  | wtbd_datetype |
| device | 打卡设备 | BasedataField | t_wtpm_oncecard.fdeviceid |  | wtpm_punchcardequip |
| applyreason | 补签原因 | BasedataField | t_wtpm_oncecard.fapplyreasonid |  | wtbd_reason |
| presetbiz1 | 预留业务字段1 | TextField | t_wtpm_oncecard.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtpm_oncecard.fpresetbiz2 |  |  |
| employee | 员工 | BasedataField | t_wtpm_oncecard.femployeeid |  | hrpi_employee |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_oncecard（主表） | 24 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
