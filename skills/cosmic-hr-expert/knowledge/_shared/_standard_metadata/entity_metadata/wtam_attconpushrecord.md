---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15=TGRTUNG1B
app_number: wtam
app_name: 日常考勤
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtam_attconpushrecord — 考勤确认推送记录

**表单编码**: `wtam_attconpushrecord`  
**表单ID**: `4/5T7MJOO4SO`  
**归属**: 工时假勤云 / 日常考勤  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtam_attconpushrecord（考勤确认推送记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtam_attconpushrecord` | 主表 · 19 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtam_attconpushrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtam_attconpushrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtam_attconpushrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtam_attconpushrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| sender | 推送人 | UserField | t_wtam_attconpushrecord.fsenderid | ✓ | bos_user |
| attconrecord | 考勤确认记录 | BasedataField | t_wtam_attconpushrecord.fattconrecordid | ✓ | wtam_attconrecord |
| attfile | 考勤档案 | BasedataField | t_wtam_attconpushrecord.fattfileid | ✓ | wtp_attfilebase |
| attfileversion | 考勤档案版本 | BasedataField | — | ✓ | wtp_attfilebase |
| perattperiod | 人员考勤期间 | BasedataField | t_wtam_attconpushrecord.fperattperiodid | ✓ | wtp_perattperiod |
| number | 编码 | TextField | t_wtam_attconpushrecord.fnumber |  |  |
| name | 名称 | TextField | t_wtam_attconpushrecord.fname |  |  |
| msgtemplate | 消息模板 | BasedataField | t_wtam_attconpushrecord.fmsgtemplate |  | msg_template |
| msgchannel | 消息渠道 | BasedataPropField | — |  |  |
| status | 推送状态 | ComboField | t_wtam_attconpushrecord.fstatus | ✓ |  |
| failmsg | 失败原因 | TextField | t_wtam_attconpushrecord.ffailmsg |  |  |
| type | 确认维度 | BasedataPropField | — |  |  |
| startdate | 开始日期 | BasedataPropField | — |  |  |
| confirmendtime | 考勤确认截止日期 | BasedataPropField | — |  |  |
| enddate | 结束日期 | BasedataPropField | — |  |  |
| receiver | 接收人 | UserField | t_wtam_attconpushrecord.freceiverid |  | bos_user |
| msgid | 消息平台id | BigIntField | t_wtam_attconpushrecord.fmsgid |  |  |
| sendtime | 推送时间 | DateTimeField | t_wtam_attconpushrecord.fsendtime |  |  |
| source | 数据来源 | ComboField | t_wtam_attconpushrecord.fsource | ✓ |  |
| pushrule | 考勤确认推送规则 | BasedataField | t_wtam_attconpushrecord.fpushrule |  | wtp_attconfirmpushrule |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtam_attconpushrecord（主表） | 18 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
