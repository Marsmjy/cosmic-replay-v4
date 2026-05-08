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

# hsbs_standardspecialitem — 辅助项目

**表单编码**: `hsbs_standardspecialitem`  
**表单ID**: `4WPPEVGTO91L`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_standardspecialitem（辅助项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_standarditem` | 主表 · 36 列 |
| `t_hsbs_standarditem_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_standarditem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_standarditem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_standarditem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_standarditem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_standarditem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_standarditem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_standarditem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_standarditem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_standarditem.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_standarditem.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_standarditem.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_standarditem.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_standarditem.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_standarditem.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_standarditem.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_standarditem.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_standarditem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_standarditem_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_standarditem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_standarditem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_standarditem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| biztype | 适用业务类型 | MulComboField | t_hsbs_standarditem.fbiztype |  |  |
| fixeditem | 固定项目 | ComboField | t_hsbs_standarditem.ffixeditem |  |  |
| datatype | 数据类型 | BasedataField | t_hsbs_standarditem.fdatatypeid | ✓ | hsbs_datatype |
| datalength | 数据长度 | IntegerField | t_hsbs_standarditem.fdatalength |  |  |
| dataprecision | 数据精度 | BasedataField | t_hsbs_standarditem.fdataprecisionid |  | hsbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_hsbs_standarditem.fdataroundid |  | hsbs_dataround |
| calfrequency | 默认频度 | BasedataField | t_hsbs_standarditem.fcalfrequencyid |  | hsbs_calfrequency |
| currency | 默认币种 | BasedataField | t_hsbs_standarditem.fcurrencyid |  | bd_currency |
| generalenname | 通用英文名 | TextField | t_hsbs_standarditem.fgeneralenname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_standarditem.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_standarditem.fcountryid |  | bd_country |
| type | 项目归属类型 | ComboField | t_hsbs_standarditem.ftype |  |  |
| uniquecode | 唯一编码 | TextField | t_hsbs_standarditem.funiquecode |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_standarditem（主表） | 31 |
| t_hsbs_standarditem_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
