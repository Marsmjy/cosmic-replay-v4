---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHLXNRR9KPZ
app_number: hsbs
app_name: 薪酬基础服务
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsbs_trdtaxfield — 第三方个税字段

**表单编码**: `hsbs_trdtaxfield`  
**表单ID**: `5G4VKXUKDJ3H`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_trdtaxfield（第三方个税字段） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_trdtaxfield` | 主表 · 22 列 |
| `t_hsbs_trdtaxfield_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_trdtaxfield.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_trdtaxfield_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_trdtaxfield.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_trdtaxfield.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_trdtaxfield.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_trdtaxfield.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_trdtaxfield.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_trdtaxfield.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_trdtaxfield.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_trdtaxfield_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_trdtaxfield_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_trdtaxfield.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_trdtaxfield.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_trdtaxfield.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_trdtaxfield.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| trdlogo | 第三方标识 | BasedataField | t_hsbs_trdtaxfield.ftrdlogoid | ✓ | hsbs_trdlogo |
| datatype | 数据类型 | BasedataField | t_hsbs_trdtaxfield.fdatatypeid | ✓ | hsbs_datatype |
| datalength | 数据长度 | IntegerField | t_hsbs_trdtaxfield.fdatalength |  |  |
| dataprecision | 数据精度 | BasedataField | t_hsbs_trdtaxfield.fdataprecisionid |  | hsbs_dataprecision |
| accitem | 累计项 | ComboField | t_hsbs_trdtaxfield.faccitem | ✓ |  |
| dataround | 精度尾差处理 | BasedataField | t_hsbs_trdtaxfield.fdataroundid |  | hsbs_dataround |
| currencyid | 币种 | CurrencyField | t_hsbs_trdtaxfield.fcurrencyid |  | bd_currency |
| taxcategories | 个税种类 | MulBasedataField | — | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_trdtaxfield（主表） | 19 |
| t_hsbs_trdtaxfield_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 5 |
