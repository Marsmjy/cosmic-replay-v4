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

# wtp_accountstagetype — 核算阶段类型

**表单编码**: `wtp_accountstagetype`  
**表单ID**: `2MJKU8T5XA9P`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_accountstagetype（核算阶段类型） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_accountstagetype` | 主表 · 19 列 |
| `t_wtp_accountstagetype_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_accountstagetype.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_accountstagetype_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_accountstagetype.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_accountstagetype.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_accountstagetype.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_accountstagetype.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_accountstagetype.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_accountstagetype.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_accountstagetype.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_accountstagetype_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_accountstagetype_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wtp_accountstagetype.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_accountstagetype.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_accountstagetype.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_accountstagetype.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_accountstagetype.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_accountstagetype.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_accountstagetype_l.foriname |  |  |
| accounttype | 核算类型 | MulComboField | t_wtp_accountstagetype.faccounttype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_accountstagetype（主表） | 15 |
| t_wtp_accountstagetype_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
