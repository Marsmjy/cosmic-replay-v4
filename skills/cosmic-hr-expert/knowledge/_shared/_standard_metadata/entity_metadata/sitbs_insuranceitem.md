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

# sitbs_insuranceitem — 险种项目

**表单编码**: `sitbs_insuranceitem`  
**表单ID**: `2HNO9JUGHC4Q`  
**归属**: 薪酬福利云 / 社保个税基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: sitbs_insuranceitem（险种项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_sitbs_insuranceitem` | 主表 · 23 列 |
| `t_sitbs_insuranceitem_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_sitbs_insuranceitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_sitbs_insuranceitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_sitbs_insuranceitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_sitbs_insuranceitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_sitbs_insuranceitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_sitbs_insuranceitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_sitbs_insuranceitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_sitbs_insuranceitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_sitbs_insuranceitem.fmasterid |  |  |
| group | 险种 | GroupField | — | ✓ | sitbs_welfaretype |
| disabler | 禁用人 | UserField | t_sitbs_insuranceitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_sitbs_insuranceitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_sitbs_insuranceitem.fissyspreset |  |  |
| orinumber | 出厂编码 | TextField | t_sitbs_insuranceitem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_sitbs_insuranceitem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_sitbs_insuranceitem_l.foriname |  |  |
| comenglishname | 通用英文名 | TextField | t_sitbs_insuranceitem.fcomenglishname |  |  |
| country | 国家/地区 | BasedataField | t_sitbs_insuranceitem.fcountryid | ✓ | bd_country |
| description | 描述 | MuliLangTextField | t_sitbs_insuranceitem_l.fdescription |  |  |
| insurancetypeattr | 险种属性 | BasedataField | t_sitbs_insuranceitem.finsurancetypeattrid | ✓ | sitbs_insuranceprop |
| datatype | 数据类型 | BasedataField | t_sitbs_insuranceitem.fdatatypeid | ✓ | sitbs_datatype |
| dataprecision | 数据精度 | BasedataField | t_sitbs_insuranceitem.fdataprecisionid |  | sitbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_sitbs_insuranceitem.fdataroundid | ✓ | sitbs_dataround |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_sitbs_insuranceitem（主表） | 19 |
| t_sitbs_insuranceitem_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 2 |
