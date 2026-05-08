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

# sitbs_taxitem — 个税项目

**表单编码**: `sitbs_taxitem`  
**表单ID**: `1BW051I5H5KX`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_taxitem（个税项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_taxitem` | 主表 · 34 列 |
| `t_sitbs_taxitem_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_taxitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_taxitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_taxitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_taxitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_taxitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_taxitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_taxitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_taxitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_taxitem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_sitbs_taxitem.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_sitbs_taxitem.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_sitbs_taxitem.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_sitbs_taxitem.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_sitbs_taxitem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_sitbs_taxitem.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_sitbs_taxitem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_sitbs_taxitem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_sitbs_taxitem_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_taxitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_sitbs_taxitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_taxitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| datatype | 数据类型 | BasedataField | t_sitbs_taxitem.fdatatypeid | ✓ | sitbs_datatype |
| datalength | 数据长度 | IntegerField | t_sitbs_taxitem.fdatalength |  |  |
| dataprecision | 数据精度 | BasedataField | t_sitbs_taxitem.fdataprecisionid |  | sitbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_sitbs_taxitem.fdataroundid |  | sitbs_dataround |
| countrytype | 国家/地区类型 | ComboField | t_sitbs_taxitem.fcountrytype | ✓ |  |
| country | 国家/地区 | BasedataField | t_sitbs_taxitem.fcountryid |  | bd_country |
| taxitemtype | 个税项目类别 | BasedataField | t_sitbs_taxitem.ftaxitemtypeid | ✓ | sitbs_taxitemtype |
| currency | 币种 | BasedataField | t_sitbs_taxitem.fcurrencyid |  | bd_currency |
| caltaxtype | 用途 | BasedataField | t_sitbs_taxitem.fcaltaxtypeid |  | itc_caltaxtype |
| taxcategories | 个税种类 | MulBasedataField | — | ✓ |  |
| incomeitem | 所得项目 | ComboField | t_sitbs_taxitem.fincomeitem |  |  |
| splitalgo | 拆分算法 | BasedataField | t_sitbs_taxitem.fsplitalgo |  | sitbs_splitalgo |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_taxitem（主表） | 29 |
| t_sitbs_taxitem_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
