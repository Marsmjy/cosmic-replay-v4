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

# wtp_qtturnoverconver — 离职折算

**表单编码**: `wtp_qtturnoverconver`  
**表单ID**: `34L+87DF25=K`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtturnoverconver（离职折算） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtturnoverconver` | 主表 · 30 列 |
| `t_wtp_qtturnoverconver_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtturnoverconver.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtturnoverconver_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtturnoverconver.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtturnoverconver.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtturnoverconver.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtturnoverconver.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtturnoverconver.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtturnoverconver.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtturnoverconver.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtturnoverconver.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtturnoverconver.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtturnoverconver.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtturnoverconver.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtturnoverconver.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtturnoverconver.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtturnoverconver.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtturnoverconver_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtturnoverconver_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtturnoverconver.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtturnoverconver.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtturnoverconver.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_qtturnoverconver.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_qtturnoverconver.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_qtturnoverconver_l.foriname |  |  |
| conversionmethod | 折算类型 | ComboField | t_wtp_qtturnoverconver.fconversionmethod | ✓ |  |
| convertdate | 折算日期 | DateField | t_wtp_qtturnoverconver.fconvertdate | ✓ |  |
| conversionfator | 离职折算系数 | DecimalField | t_wtp_qtturnoverconver.fconversionfator |  |  |
| settlementitem | 结算时长 | BasedataField | t_wtp_qtturnoverconver.fsettlementitemid |  | wtbd_attitem |
| conversionfatorview | 离职折算系数 | DecimalField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtturnoverconver（主表） | 24 |
| t_wtp_qtturnoverconver_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
