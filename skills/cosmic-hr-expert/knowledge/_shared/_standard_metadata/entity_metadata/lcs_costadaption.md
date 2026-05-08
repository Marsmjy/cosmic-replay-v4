---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 198IF7HLNV46
app_number: lcs
app_name: 人力成本基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# lcs_costadaption — 人力成本维度方案

**表单编码**: `lcs_costadaption`  
**表单ID**: `1BMJ88076=LI`  
**归属**: HR基础服务云 / 人力成本基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: lcs_costadaption（人力成本维度方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_lcs_costadaption` | 主表 · 27 列 |
| `t_lcs_costadaption_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_lcs_costadaption.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_lcs_costadaption_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_lcs_costadaption.fstatus |  |  |
| creator | 创建人 | CreaterField | t_lcs_costadaption.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_lcs_costadaption.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_lcs_costadaption.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_lcs_costadaption.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_lcs_costadaption.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_lcs_costadaption.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_lcs_costadaption.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_lcs_costadaption.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_lcs_costadaption.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_lcs_costadaption.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_lcs_costadaption.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_lcs_costadaption.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_lcs_costadaption.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_lcs_costadaption_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_lcs_costadaption_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_lcs_costadaption.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_lcs_costadaption.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_lcs_costadaption.fdisabledate |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| country | 国家/地区 | BasedataField | t_lcs_costadaption.fcountryid |  | bd_country |
| coststru | 人力成本维度组合 | BasedataField | t_lcs_costadaption.fcoststruid | ✓ | lcs_coststru |
| currency | 成本承担币种 | BasedataField | t_lcs_costadaption.fcurrencyid | ✓ | bd_currency |
| initdatasource | 数据来源 | ComboField | — |  |  |
| initstatus | 初始化状态 | ComboField | — |  |  |
| initbatch | 初始化批次 | BigIntField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_lcs_costadaption.fareatype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_lcs_costadaption（主表） | 22 |
| t_lcs_costadaption_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
