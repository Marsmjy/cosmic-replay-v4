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

# wtpm_signcard — 原始卡记录

**表单编码**: `wtpm_signcard`  
**表单ID**: `4WIYGQX7CWJR`  
**归属**: 工时假勤云 / 打卡管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtpm_signcard（原始卡记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtpm_signcard` | 主表 · 25 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtpm_signcard.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtpm_signcard.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtpm_signcard.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtpm_signcard.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | t_wtpm_signcard.finitdatasource |  |  |
| org | 考勤管理组织 | OrgField | t_wtpm_signcard.forgid |  | bos_org |
| employee | 员工 | BasedataField | t_wtpm_signcard.femployeeid |  | hrpi_employee |
| attfile | 考勤档案版本 | BasedataField | t_wtpm_signcard.fattfileid |  | wtp_attfilebase |
| attfilebo | 考勤档案 | BasedataField | t_wtpm_signcard.fattfileboid |  | wtp_attfilebase |
| attcard | 考勤卡号 | TextField | t_wtpm_signcard.fattcard |  |  |
| signpoint | 打卡时间 | DateTimeField | t_wtpm_signcard.fsignpoint |  |  |
| signpointshort | 打卡日期(短日期) | DateField | t_wtpm_signcard.fsignpointshort |  |  |
| source | 打卡来源 | BasedataField | t_wtpm_signcard.fsourceid |  | wtbd_signsource |
| device | 打卡设备 | BasedataField | t_wtpm_signcard.fdeviceid |  | wtpm_punchcardequip |
| remark | 备注 | TextField | t_wtpm_signcard.fremark |  |  |
| status | 状态 | ComboField | t_wtpm_signcard.fstatus |  |  |
| accesstag | 进出卡 | ComboField | t_wtpm_signcard.faccesstag |  |  |
| pointtag | 卡点符号标识 | ComboField | t_wtpm_signcard.fpointtag |  |  |
| timezone | 时区 | BasedataField | t_wtpm_signcard.ftimezoneid |  | inte_timezone |
| signpointutc | 打卡具体时间-0时区 | DateTimeField | t_wtpm_signcard.fsignpointutc |  |  |
| applyreason | 补签原因 | BasedataField | t_wtpm_signcard.fapplyreasonid |  | wtbd_reason |
| presetbiz1 | 预留业务字段1 | TextField | t_wtpm_signcard.fpresetbiz1 |  |  |
| presetbiz2 | 预留业务字段2 | TextField | t_wtpm_signcard.fpresetbiz2 |  |  |
| modifytype | 修改方式 | ComboField | t_wtpm_signcard.fmodifytype |  |  |
| sourcesyskey | 来源系统唯一标识 | TextField | t_wtpm_signcard.fsourcesyskey |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtpm_signcard（主表） | 25 |
