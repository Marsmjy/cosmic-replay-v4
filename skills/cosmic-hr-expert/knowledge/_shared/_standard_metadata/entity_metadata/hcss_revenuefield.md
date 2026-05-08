---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 33EUPRZ1Q202
app_number: hcss
app_name: 员工薪酬服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcss_revenuefield — 收入证明项目

**表单编码**: `hcss_revenuefield`  
**表单ID**: `33IXLZ9=+GD5`  
**归属**: 薪酬福利云 / 员工薪酬服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcss_revenuefield（收入证明项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcss_revenuefield` | 主表 · 33 列 |
| `t_hcss_revenuefield_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcss_revenuefield.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcss_revenuefield_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcss_revenuefield.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcss_revenuefield.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcss_revenuefield.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcss_revenuefield.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcss_revenuefield.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcss_revenuefield.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcss_revenuefield.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcss_revenuefield.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcss_revenuefield.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcss_revenuefield.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcss_revenuefield.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcss_revenuefield.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcss_revenuefield.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcss_revenuefield.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcss_revenuefield_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcss_revenuefield_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcss_revenuefield.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcss_revenuefield.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcss_revenuefield.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | — | ✓ |  |
| country | 国家/地区 | BasedataField | t_hcss_revenuefield.fcountryid |  | bd_country |
| valuesource | 取值来源 | ComboField | t_hcss_revenuefield.fvaluesource | ✓ |  |
| standarditem | 定调薪项目 | BasedataField | t_hcss_revenuefield.fstandarditemid |  | hsbs_standarditem |
| valueexp | 取值表达式 | TextAreaField | t_hcss_revenuefield.fvalueexp |  |  |
| dataround | 精度尾差处理 | BasedataField | t_hcss_revenuefield.fdataroundid |  | hsbs_dataround |
| currency | 统计币种 | BasedataField | t_hcss_revenuefield.fcurrencyid |  | bd_currency |
| exratetable | 汇率表 | BasedataField | t_hcss_revenuefield.fexratetableid |  | bd_exratetable |
| exratedate | 汇率日期 | ComboField | t_hcss_revenuefield.fexratedate |  |  |
| valueexpshow | 取值表达式 | TextField | — |  |  |
| plugpath | 插件路径 | TextField | t_hcss_revenuefield.fplugpath |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcss_revenuefield（主表） | 27 |
| t_hcss_revenuefield_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
