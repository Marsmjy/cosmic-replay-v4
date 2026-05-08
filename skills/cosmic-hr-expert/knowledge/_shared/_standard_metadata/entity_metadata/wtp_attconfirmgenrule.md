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

# wtp_attconfirmgenrule — 考勤确认生成规则

**表单编码**: `wtp_attconfirmgenrule`  
**表单ID**: `4/96Q8WCR9SU`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attconfirmgenrule（考勤确认生成规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_acgenrule` | 主表 · 41 列 |
| `t_wtp_acgenrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_acgenrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_acgenrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_acgenrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_acgenrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_acgenrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_acgenrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_acgenrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_acgenrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_acgenrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_acgenrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_acgenrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_acgenrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_acgenrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_acgenrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_acgenrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_acgenrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_acgenrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_acgenrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_acgenrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_acgenrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_acgenrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| openbyday | 启用 | CheckBoxField | t_wtp_acgenrule.fopenbyday |  |  |
| acbyday | 确认期限(天) | IntegerField | t_wtp_acgenrule.facbyday | ✓ |  |
| overbyday | 逾期处理方式 | ComboField | t_wtp_acgenrule.foverbyday | ✓ |  |
| nogen | 值均为0时不生成 | CheckBoxField | t_wtp_acgenrule.fnogen |  |  |
| showconfirm | 确认后自动锁定考勤记录 | CheckBoxField | t_wtp_acgenrule.fshowconfirm |  |  |
| showlast | 允许查看上次确认的内容 | CheckBoxField | t_wtp_acgenrule.fshowlast |  |  |
| openbyperiod | 启用 | CheckBoxField | t_wtp_acgenrule.fopenbyperiod |  |  |
| acbyperiod | 确认期限(天) | IntegerField | t_wtp_acgenrule.facbyperiod | ✓ |  |
| overbypr | 逾期处理方式 | ComboField | t_wtp_acgenrule.foverbypr | ✓ |  |
| startmonth | 开始月份 | ComboField | t_wtp_acgenrule.fstartmonth | ✓ |  |
| startdays | 开始日期 | ComboField | t_wtp_acgenrule.fstartdays | ✓ |  |
| endmonth | 结束月份 | ComboField | t_wtp_acgenrule.fendmonth | ✓ |  |
| enddays | 结束日期 | ComboField | t_wtp_acgenrule.fenddays | ✓ |  |
| nogenpr | 值均为0时不生成 | CheckBoxField | t_wtp_acgenrule.fnogenpr |  |  |
| showconfirmpr | 确认后自动锁定考勤记录 | CheckBoxField | t_wtp_acgenrule.fshowconfirmpr |  |  |
| showlastbypr | 允许查看上次确认的内容 | CheckBoxField | t_wtp_acgenrule.fshowlastbypr |  |  |
| genscopebyday | 生成范围 | IntegerField | t_wtp_acgenrule.fgenscopebyday | ✓ |  |
| scopeunit | 单位 | ComboField | t_wtp_acgenrule.fscopeunit | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_acgenrule（主表） | 36 |
| t_wtp_acgenrule_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
