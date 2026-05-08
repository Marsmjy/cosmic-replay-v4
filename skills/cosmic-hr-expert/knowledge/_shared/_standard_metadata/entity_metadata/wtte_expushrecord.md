---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1C8H4/N38LCY
app_number: wtte
app_name: 考勤核算
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtte_expushrecord — 异常推送记录

**表单编码**: `wtte_expushrecord`  
**表单ID**: `2MQ3CSCH+2TV`  
**归属**: 工时假勤云 / 考勤核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtte_expushrecord（异常推送记录） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtte_expushrecord` | 主表 · 15 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| creator | 创建人 | CreaterField | t_wtte_expushrecord.fcreatorid |  | bos_user |
| createtime | 创建时间 | CreateDateField | t_wtte_expushrecord.fcreatetime |  |  |
| modifier | 修改人 | ModifierField | t_wtte_expushrecord.fmodifierid |  | bos_user |
| modifytime | 修改时间 | ModifyDateField | t_wtte_expushrecord.fmodifytime |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| number | 消息编码 | TextField | t_wtte_expushrecord.fnumber | ✓ |  |
| name | 名称 | TextField | t_wtte_expushrecord.fname | ✓ |  |
| receiver | 接收人 | BasedataField | t_wtte_expushrecord.freceiver |  | bos_user |
| msgtemplate | 消息模板 | BasedataField | t_wtte_expushrecord.fmsgtemplate |  | msg_template |
| pushstatus | 推送状态 | ComboField | t_wtte_expushrecord.fpushstatus |  |  |
| exinfo | 异常信息 | TextField | t_wtte_expushrecord.fexinfo |  |  |
| readstatus | 阅读状态 | ComboField | t_wtte_expushrecord.freadstatus |  |  |
| recnumber | 工号 | BasedataPropField | — |  |  |
| infochannel | 消息渠道 | BasedataPropField | — |  |  |
| rectype | 接收人类别 | ComboField | t_wtte_expushrecord.frectype |  |  |
| bizcreator | 创建人展示 | TextField | t_wtte_expushrecord.fbizcreator |  |  |
| msgid | 消息平台ID | BigIntField | t_wtte_expushrecord.fmsgid |  |  |
| pushtime | 推送时间 | DateTimeField | t_wtte_expushrecord.fpushtime |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtte_expushrecord（主表） | 15 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
