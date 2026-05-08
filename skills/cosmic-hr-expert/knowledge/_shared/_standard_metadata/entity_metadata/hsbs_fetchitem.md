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

# hsbs_fetchitem — 取数项目

**表单编码**: `hsbs_fetchitem`  
**表单ID**: `25GREPG0AIM/`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_fetchitem（取数项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_fetchitem` | 主表 · 29 列 |
| `t_hsbs_fetchitem_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_fetchitem.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_fetchitem_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_fetchitem.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_fetchitem.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_fetchitem.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_fetchitem.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_fetchitem.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_fetchitem.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_fetchitem.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_fetchitem_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_fetchitem_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsbs_fetchitem.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_fetchitem.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_fetchitem.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_fetchitem.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hsbs_fetchitem.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hsbs_fetchitem.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hsbs_fetchitem_l.foriname |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_fetchitem.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_fetchitem.fcountryid |  | bd_country |
| datatype | 数据类型 | BasedataField | t_hsbs_fetchitem.fdatatypeid | ✓ | hsbs_datatype |
| fetchitemgroupid | 取数项目类别 | BasedataField | t_hsbs_fetchitem.ffetchitemgroupid | ✓ | hsbs_fetchitemgroup |
| fetchconfig | 取数配置 | TextField | t_hsbs_fetchitem.ffetchconfig |  |  |
| configtype | 取数配置类型 | ComboField | t_hsbs_fetchitem.fconfigtype |  |  |
| calculationfetch | 计算时取数 | ComboField | t_hsbs_fetchitem.fcalculationfetch |  |  |
| dimensionality | 取数维度 | ComboField | t_hsbs_fetchitem.fdimensionality |  |  |
| uniquecode | 唯一编码 | TextField | t_hsbs_fetchitem.funiquecode |  |  |
| dataprecision | 数据精度 | BasedataField | t_hsbs_fetchitem.fdataprecisionid |  | hsbs_dataprecision |
| dataround | 精度尾差处理 | BasedataField | t_hsbs_fetchitem.fdataroundid |  | hsbs_dataround |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_fetchitem（主表） | 25 |
| t_hsbs_fetchitem_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
