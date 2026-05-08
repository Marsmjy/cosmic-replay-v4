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

# hsbs_fetchconfig — 取数配置

**表单编码**: `hsbs_fetchconfig`  
**表单ID**: `24LNW/J5+5Y9`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_fetchconfig（取数配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_fetchconfig` | 主表 · 27 列 |
| `t_hsbs_fetchfieldentry` | 分录表 · 8 列 |
| `t_hsbs_fetchsortentry` | 分录表 · 3 列 |
| `t_hsbs_fetchfilterentry` | 分录表 · 8 列 |
| `t_hsbs_fetchconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_fetchconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_fetchconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_fetchconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_fetchconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_fetchconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_fetchconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_fetchconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_fetchconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_fetchconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_fetchconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_fetchconfig_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsbs_fetchconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_fetchconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_fetchconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_fetchconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: fetchfieldentry（取数字段） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| field | 字段 | TextField | — | ✓ |  |
| fieldname | 字段名称 | TextField | t_hsbs_fetchfilterentry.ffieldname | ✓ |  |
| datatype | 数据类型 | ComboField | t_hsbs_fetchfilterentry.fdatatype | ✓ |  |
| srctype | 值来源类型 | ComboField | t_hsbs_fetchfieldentry.fsrctype |  |  |
| srcentity | 值来源实体 | BasedataField | t_hsbs_fetchfieldentry.fsrcentity |  | bos_objecttype |
| srcenum | 值来源枚举 | BasedataField | t_hsbs_fetchfieldentry.fsrcenum |  | hsbs_enumconfig |
| srcfield | 值来源字段隐藏 | TextField | t_hsbs_fetchfieldentry.fsrcfield |  |  |
| srcfieldnum | 值来源字段 | TextField | — |  |  |
| fetchitem | 取数项目 | BasedataField | — | ✓ | hsbs_fetchitem |
| sumtype | 聚合方式 | ComboField | t_hsbs_fetchfieldentry.fsumtype |  |  |

## 实体: fetchsortentry（数据排序依据） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sortfield | 字段 | TextField | — | ✓ |  |
| sortfieldname | 字段名称 | TextField | — | ✓ |  |
| sorttype | 排序 | ComboField | t_hsbs_fetchsortentry.fsorttype | ✓ |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_fetchconfig.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_fetchconfig.fcountryid |  | bd_country |
| fetchmethod | 取数方法 | ComboField | t_hsbs_fetchconfig.ffetchmethod | ✓ |  |
| fetchsource | 取数来源 | BasedataField | t_hsbs_fetchconfig.ffetchsourceid | ✓ | hbp_entityobject |
| dimensionality | 取数维度 | ComboField | t_hsbs_fetchconfig.fdimensionality | ✓ |  |
| calculationfetch | 计算时取数 | CheckBoxField | — |  |  |

## 实体: relationentity（条件分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| conditionnumber | 编码 | TextField | t_hsbs_fetchfilterentry.fconditionnumber |  |  |
| filterfieldname | 字段名称 | TextField | — |  |  |
| filterfield | 字段 | TextField | t_hsbs_fetchfilterentry.ffilterfield |  |  |
| filterdatatype | 字段类型 | ComboField | — |  |  |
| comparetype | 判断条件 | TextField | — |  |  |
| condition | 判断条件值 | TextField | t_hsbs_fetchfilterentry.fcondition |  |  |
| comparevaluetype | 比较值类型 | TextField | — |  |  |
| valuetype | 比较值类型值 | TextField | t_hsbs_fetchfilterentry.fvaluetype |  |  |
| comparevaluetext | 比较值 | TextField | t_hsbs_fetchfilterentry.fcomparevaluetext |  |  |
| comparevalue | 比较值 | TextField | t_hsbs_fetchfilterentry.fcomparevalue |  |  |
| conditionlogictype | 单选按钮组 | RadioGroupField | t_hsbs_fetchconfig.fconditionlogictype |  |  |
| radiofield2 | 自定义逻辑 | RadioField | — |  |  |
| conditionlogiccode | 条件逻辑 | TextField | — |  |  |
| proration | 是否分段 | CheckBoxField | t_hsbs_fetchconfig.fproration |  |  |
| prorationstartfield | 分段开始日期 | TextField | t_hsbs_fetchconfig.fprorationstartfield |  |  |
| prorationendfield | 分段结束日期 | TextField | t_hsbs_fetchconfig.fprorationendfield |  |  |
| prorationstartdatename | 分段开始日期 | TextField | — |  |  |
| prorationenddatename | 分段结束日期 | TextField | — |  |  |
| sumfetch | 聚合取数 | CheckBoxField | t_hsbs_fetchconfig.fsumfetch |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_fetchconfig（主表） | 22 |
| t_hsbs_fetchconfig_l | 3 |
| t_hsbs_fetchfieldentry | 5 |
| t_hsbs_fetchfilterentry | 8 |
| t_hsbs_fetchsortentry | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 18 |
