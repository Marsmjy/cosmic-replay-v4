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

# wtp_attconfirmpushrule — 考勤确认推送规则

**表单编码**: `wtp_attconfirmpushrule`  
**表单ID**: `4/F8871XFDXO`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attconfirmpushrule（考勤确认推送规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_acpushrule` | 主表 · 31 列 |
| `t_wtp_acpushrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_acpushrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_acpushrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_acpushrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_acpushrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_acpushrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_acpushrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_acpushrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_acpushrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_acpushrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_acpushrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_acpushrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_acpushrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_acpushrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_acpushrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_acpushrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_acpushrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_acpushrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_acpushrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_acpushrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_acpushrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_acpushrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| openbyday | 启用 | CheckBoxField | t_wtp_acpushrule.fopenbyday |  |  |
| pushrange | 推送范围(天) | IntegerField | t_wtp_acpushrule.fpushrange | ✓ |  |
| msgschme | 消息场景 | BasedataField | t_wtp_acpushrule.fmsgschme | ✓ | msg_tplscene |
| msgtemplate | 消息模板 | BasedataField | t_wtp_acpushrule.fmsgtemplate | ✓ | msg_template |
| openbyperiod | 启用 | CheckBoxField | t_wtp_acpushrule.fopenbyperiod |  |  |
| pushrangepr | 推送范围(天) | IntegerField | t_wtp_acpushrule.fpushrangepr | ✓ |  |
| msgschmepr | 消息场景 | BasedataField | t_wtp_acpushrule.fmsgschmepr | ✓ | msg_tplscene |
| msgtemplatepr | 消息模板 | BasedataField | t_wtp_acpushrule.fmsgtemplatepr | ✓ | msg_template |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_acpushrule（主表） | 26 |
| t_wtp_acpushrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
