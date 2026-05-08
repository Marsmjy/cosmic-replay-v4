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

# wtp_expushrule — 异常推送规则

**表单编码**: `wtp_expushrule`  
**表单ID**: `2MO1+4D2S9CH`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_expushrule（异常推送规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_expushrule` | 主表 · 42 列 |
| `t_wtp_expushsetentity` | 分录表 · 3 列 |
| `t_wtp_expushmesentity` | 分录表 · 2 列 |
| `t_wtp_expushrule_l` | 多语言表 · 4 列 |
| `t_wtp_expushmesentity_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_expushrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_expushrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_expushrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_expushrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_expushrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_expushrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_expushrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_expushrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_expushrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_expushrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_expushrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_expushrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_expushrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_expushrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_expushrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_expushrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_expushrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_expushrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_expushrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_expushrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_expushrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_expushrule.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_expushrule.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_expushrule_l.foriname |  |  |
| tplscene | 推送场景 | BasedataField | t_wtp_expushrule.ftplsceneid | ✓ | msg_tplscene |
| pushdatarange | 推送数据范围 | ComboField | t_wtp_expushrule.fpushdatarange | ✓ |  |

## 实体: pussetentity（推送设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| msgtemplate | 消息模板 | BasedataField | t_wtp_expushsetentity.fmsgtemplateid | ✓ | msg_template |
| msgtype | 推送类型隐藏 | BasedataPropField | — |  |  |
| msgchannel | 推送渠道 | BasedataPropField | — |  |  |
| recipient | 接收人 | MulComboField | t_wtp_expushsetentity.frecipient | ✓ |  |
| recipientplugin | 其他接收人 | BasedataField | t_wtp_expushsetentity.frecipientpluginid |  | wtbs_pluginregister |
| msgtypeshow | 推送类型 | TextField | — |  |  |
| startmonth | 选择日期 | ComboField | t_wtp_expushrule.fstartmonth |  |  |
| startdays | 起始日选择 | ComboField | t_wtp_expushrule.fstartdays |  |  |
| endmonth | 月选择 | ComboField | t_wtp_expushrule.fendmonth |  |  |
| startweektype | 选择日期 | ComboField | t_wtp_expushrule.fstartweektype |  |  |
| startweek | 起始星期选择 | ComboField | t_wtp_expushrule.fstartweek |  |  |
| endweektype | 周选择 | ComboField | t_wtp_expushrule.fendweektype |  |  |
| stepdays | 选择天数 | StepperField | t_wtp_expushrule.fstepdays |  |  |
| datarange | 数据范围插件 | BasedataField | t_wtp_expushrule.fdatarangeid |  | wtbs_pluginregister |
| includeday | 含当天 | CheckBoxField | t_wtp_expushrule.fincludeday |  |  |
| includesat | 含周六 | CheckBoxField | t_wtp_expushrule.fincludesat |  |  |
| includesun | 含周日 | CheckBoxField | t_wtp_expushrule.fincludesun |  |  |
| endweek | 终止星期选择 | ComboField | t_wtp_expushrule.fendweek |  |  |
| enddays | 终止日选择 | ComboField | t_wtp_expushrule.fenddays |  |  |

## 实体: messetentity（消息设置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attenditem | 考勤项目 | BasedataField | t_wtp_expushmesentity.fattenditemid | ✓ | wtbd_attitem |
| mescontent | 消息内容 | MuliLangTextField | t_wtp_expushmesentity_l.fmescontent | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_expushrule（主表） | 35 |
| t_wtp_expushmesentity | 1 |
| t_wtp_expushmesentity_l | 1 |
| t_wtp_expushrule_l | 4 |
| t_wtp_expushsetentity | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
