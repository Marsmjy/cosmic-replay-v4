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

# hsbs_statisticstag — 薪酬项目类别

**表单编码**: `hsbs_statisticstag`  
**表单ID**: `3KZPJCKF//P6`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_statisticstag（薪酬项目类别） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_statisticstag` | 主表 · 34 列 |
| `t_hsbs_statisticstag_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_statisticstag.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_statisticstag_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_statisticstag.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_statisticstag.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_statisticstag.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_statisticstag.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_statisticstag.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_statisticstag.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_statisticstag.fmasterid |  |  |
| longnumber | 长编码 | TextField | t_hsbs_statisticstag.flongnumber |  |  |
| level | 级次 | IntegerField | t_hsbs_statisticstag.flevel |  |  |
| fullname | 长名称 | MuliLangTextField | t_hsbs_statisticstag_l.ffullname |  |  |
| isleaf | 叶子节点 | CheckBoxField | t_hsbs_statisticstag.fisleaf |  |  |
| parent | 上级类别 | ParentBasedataField | — |  | hsbs_statisticstag |
| createorg | 创建组织 | OrgField | t_hsbs_statisticstag.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_statisticstag.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_statisticstag.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_statisticstag.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_statisticstag.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_statisticstag.fsrccreateorgid |  | bos_org |
| simplename | 简称 | MuliLangTextField | t_hsbs_statisticstag_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_statisticstag_l.fdescription |  |  |
| disabler | 禁用人 | UserField | t_hsbs_statisticstag.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_statisticstag.fdisabledate |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_statisticstag.fissyspreset |  |  |
| index | 排序号 | IntegerField | t_hsbs_statisticstag.findex |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_statisticstag.fgeneralenname |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_statisticstag.fcountryid |  | bd_country |
| areatype | 国家/地区类型 | ComboField | t_hsbs_statisticstag.fareatype | ✓ |  |
| taglevel | 薪酬项目类别层级 | BasedataField | t_hsbs_statisticstag.ftaglevelid | ✓ | hsbs_taglevel |
| enableaddjunior | 允许增加下级类别 | CheckBoxField | t_hsbs_statisticstag.fenableaddjunior |  |  |
| parenttagnumber | 上级类别.编码 | TextField | — |  |  |
| dataprecision | 数据精度 | BasedataField | t_hsbs_statisticstag.fdataprecisionid | ✓ | hsbs_dataprecision |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_statisticstag（主表） | 27 |
| t_hsbs_statisticstag_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
