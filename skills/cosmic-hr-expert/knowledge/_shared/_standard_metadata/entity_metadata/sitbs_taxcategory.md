---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 17/+7RIW4SCJ
app_number: sitbs
app_name: 社保个税基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# sitbs_taxcategory — 个税种类

**表单编码**: `sitbs_taxcategory`  
**表单ID**: `1FIEIHTULRUT`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_taxcategory（个税种类） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_taxcategory` | 主表 · 19 列 |
| `t_sitbs_taxcategory_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_taxcategory.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_taxcategory_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_taxcategory.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_taxcategory.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_taxcategory.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_taxcategory.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_taxcategory.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_taxcategory.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_taxcategory.fmasterid |  |  |
| group | 个税大类 | GroupField | — |  | sitbs_taxgroup |
| disabler | 禁用人 | UserField | t_sitbs_taxcategory.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_taxcategory.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_taxcategory.fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_taxcategory_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_taxcategory_l.fdescription |  |  |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_taxcategory.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_taxcategory.fcountryid |  | bd_country |
| index | 顺序号 | IntegerField | t_sitbs_taxcategory.findex |  |  |
| taxgroupsearch | 个税大类 | BasedataField | — |  | sitbs_taxgroup |
| taxpayertype | 纳税人类型 | BasedataField | t_sitbs_taxcategory.ftaxpayertypeid |  | itc_taxpayertype |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_taxcategory（主表） | 15 |
| t_sitbs_taxcategory_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
