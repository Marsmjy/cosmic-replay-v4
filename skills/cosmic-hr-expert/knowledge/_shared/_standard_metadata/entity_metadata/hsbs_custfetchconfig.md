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

# hsbs_custfetchconfig — 插件取数配置

**表单编码**: `hsbs_custfetchconfig`  
**表单ID**: `267MXLSWWGU3`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_custfetchconfig（插件取数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_custfetchconfig` | 主表 · 20 列 |
| `t_hsbs_custfetchoutentry` | 分录表 · 6 列 |
| `t_hsbs_custfetchinentry` | 分录表 · 7 列 |
| `t_hsbs_custfetchconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_custfetchconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_custfetchconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_custfetchconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_custfetchconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_custfetchconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_custfetchconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_custfetchconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_custfetchconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_custfetchconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_custfetchconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_custfetchconfig_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsbs_custfetchconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_custfetchconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_custfetchconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_custfetchconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: fetchfieldentry（出参） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| outputparamname | 出参名称 | TextField | — | ✓ |  |
| fetchitem | 取数项目 | BasedataField | t_hsbs_custfetchinentry.ffetchitemid | ✓ | hsbs_fetchitem |
| srctype | 值来源类型 | ComboField | t_hsbs_custfetchoutentry.fsrctype |  |  |
| srcentity | 值来源实体 | BasedataField | t_hsbs_custfetchoutentry.fsrcentity |  | bos_objecttype |
| srcenum | 值来源枚举 | BasedataField | t_hsbs_custfetchoutentry.fsrcenum |  | hsbs_enumconfig |
| srcfield | 值来源字段隐藏 | TextField | t_hsbs_custfetchoutentry.fsrcfield |  |  |
| srcfieldnum | 值来源字段 | TextField | — |  |  |
| outputparam | 出参 | TextField | — | ✓ |  |
| datatype | 数据类型 | BasedataPropField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_custfetchconfig.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_custfetchconfig.fcountryid |  | bd_country |
| dimensionality | 取数维度 | ComboField | t_hsbs_custfetchconfig.fdimensionality | ✓ |  |
| calculationfetch | 计算时取数 | CheckBoxField | — |  |  |
| fetchpath | 插件路径 | TextField | t_hsbs_custfetchconfig.ffetchpath | ✓ |  |

## 实体: inputfieldentry（入参） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| inputparam | 入参 | TextField | — | ✓ |  |
| inputparamname | 入参名称 | TextField | — | ✓ |  |
| inputparamdatatype | 数据类型 | ComboField | — | ✓ |  |
| inputparamitemtype | 参数类型 | ComboField | — | ✓ |  |
| inputparamitem | 参数值 | TextField | — | ✓ |  |
| inputdescription | 参数说明 | TextField | — |  |  |
| inputparamfetchitem | 取数项目 | BasedataField | — |  | hsbs_fetchitem |
| inputparamfixedvalue | 固定值 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_custfetchconfig（主表） | 16 |
| t_hsbs_custfetchconfig_l | 3 |
| t_hsbs_custfetchinentry | 1 |
| t_hsbs_custfetchoutentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
