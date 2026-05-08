---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_schemeaftercal — 核算后处理方案

**表单编码**: `hsas_schemeaftercal`  
**表单ID**: `3Z=OFY4JOL19`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_schemeaftercal（核算后处理方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_schaftercal` | 主表 · 34 列 |
| `t_hsas_schaftercal_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_schaftercal.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_schaftercal_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_schaftercal.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_schaftercal.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_schaftercal.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_schaftercal.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_schaftercal.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_schaftercal.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_schaftercal.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsas_schaftercal.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsas_schaftercal.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsas_schaftercal.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsas_schaftercal.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsas_schaftercal.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsas_schaftercal.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsas_schaftercal.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_schaftercal_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_schaftercal_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_schaftercal.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_schaftercal.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_schaftercal.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hsas_schaftercal.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hsas_schaftercal.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hsas_schaftercal_l.foriname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_schaftercal.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_schaftercal.fcountryid |  | bd_country |
| nocountbizdata | 业务数据消费不计使用次数 | CheckBoxField | — |  |  |
| nocountsinsur | 社保数据消费不计使用次数 | CheckBoxField | — |  |  |
| nocountatt | 考勤数据消费不计使用次数 | CheckBoxField | — |  |  |
| calapprove | 启用薪资审批 | CheckBoxField | — |  |  |
| costallot | 启用成本分摊/入账 | CheckBoxField | — |  |  |
| genbizdata | 启用生成业务数据 | CheckBoxField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_schaftercal（主表） | 22 |
| t_hsas_schaftercal_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
