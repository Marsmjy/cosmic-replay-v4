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

# wtp_persumsource — 期间汇总考勤项目

**表单编码**: `wtp_persumsource`  
**表单ID**: `2IGE=KE6R9YS`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_persumsource（期间汇总考勤项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_persumsource` | 主表 · 29 列 |
| `t_wtp_persumsouentry` | 分录表 · 1 列 |
| `t_wtp_persumsource_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_persumsource.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_persumsource_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_persumsource.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_persumsource.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_persumsource.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_persumsource.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_persumsource.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_persumsource.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_persumsource.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_persumsource.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_persumsource.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_persumsource.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_persumsource.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_persumsource.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_persumsource.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_persumsource.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_persumsource_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_persumsource_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_persumsource.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_persumsource.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_persumsource.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtp_persumsource.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtp_persumsource.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtp_persumsource_l.foriname |  |  |
| datatype | 数据类型 | ComboField | t_wtp_persumsource.fdatatype | ✓ |  |
| unit | 单位 | ComboField | t_wtp_persumsource.funit | ✓ |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| attitem | 考勤项目 | BasedataField | t_wtp_persumsouentry.fattitemid | ✓ | wtbd_attitem |
| attitemcode | 编码 | BasedataPropField | — |  |  |
| attitemitemtype | 项目类型 | BasedataPropField | — |  |  |
| attitemdatatype | 数据类型 | BasedataPropField | — |  |  |
| attitemunit | 单位 | BasedataPropField | — |  |  |
| treatmentmeth | 考勤项目处理方式 | RadioOptGroupField | t_wtp_persumsource.ftreatmentmeth | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_persumsource（主表） | 23 |
| t_wtp_persumsouentry | 1 |
| t_wtp_persumsource_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
