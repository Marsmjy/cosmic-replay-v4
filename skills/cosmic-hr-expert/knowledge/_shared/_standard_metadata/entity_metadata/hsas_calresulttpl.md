---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_calresulttpl — 薪资明细结果模板

**表单编码**: `hsas_calresulttpl`  
**表单ID**: `2/I1YO8EFK72`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_calresulttpl（薪资明细结果模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_calresulttpl` | 主表 · 24 列 |
| `t_hsas_calpersontplentry` | 分录表 · 7 列 |
| `t_hsas_calitemtplentry` | 分录表 · 5 列 |
| `t_hsas_calresulttpl_l` | 多语言表 · 3 列 |
| `t_hsas_calpersontplentry_l` | 多语言表 · 1 列 |
| `t_hsas_calitemtplentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_calresulttpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_calresulttpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_calresulttpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_calresulttpl.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_calresulttpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_calresulttpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_calresulttpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_calresulttpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_calresulttpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_calresulttpl_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_calresulttpl_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_calresulttpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_calresulttpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_calresulttpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_calresulttpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| payrollgroup | 薪资核算组 | BasedataField | t_hsas_calresulttpl.fpayrollgroupid |  | hsas_payrollgrp |
| bsed | 生效日期 | DateField | t_hsas_calresulttpl.fbsed | ✓ |  |
| generalenname | 通用英文名 | TextField | t_hsas_calresulttpl.fgeneralenname |  |  |

## 实体: payrolllist（核算名单字段选择） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| alias | 别名 | MuliLangTextField | t_hsas_calitemtplentry_l.falias |  |  |
| fieldtype | 字段类别 | ComboField | t_hsas_calpersontplentry.ffieldtype |  |  |
| fieldnumber | 字段编码 | TextField | t_hsas_calpersontplentry.ffieldnumber |  |  |
| perruleobj | 对象 | BasedataField | t_hsas_calpersontplentry.fperruleobjid |  | hsbs_perruleobj |
| fieldsource | 字段来源 | TextField | t_hsas_calpersontplentry.ffieldsource |  |  |
| fieldname | 字段名称 | TextField | t_hsas_calpersontplentry.ffieldname |  |  |
| field | 字段编码 | TextField | t_hsas_calpersontplentry.ffield |  |  |

## 实体: resultlist（项目类别分录表） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemunicodeid | 项目唯一ID | TextField | t_hsas_calitemtplentry.fitemunicodeid |  |  |
| itemcategory | 项目类别 | ComboField | t_hsas_calitemtplentry.fitemcategory |  |  |
| itemname | 项目名称 | TextField | t_hsas_calitemtplentry.fitemname |  |  |
| resultalias | 项目别名 | MuliLangTextField | — |  |  |
| itemnumber | 项目编码 | TextField | t_hsas_calitemtplentry.fitemnumber |  |  |
| isdefault | 是否为默认模板 | ComboField | t_hsas_calresulttpl.fisdefault |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_calresulttpl.forgid | ✓ | bos_org |
| ishighlight | 覆盖数据是否高亮显示 | CheckBoxField | t_hsas_calresulttpl.fishighlight |  |  |
| highlightcolor | 高亮字体颜色 | ComboField | t_hsas_calresulttpl.fhighlightcolor |  |  |
| payrollgroups | 薪资核算组 | MulBasedataField | — | ✓ |  |
| areatype | 国家/地区类型 | ComboField | t_hsas_calresulttpl.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsas_calresulttpl.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_calresulttpl（主表） | 21 |
| t_hsas_calitemtplentry | 4 |
| t_hsas_calitemtplentry_l | 1 |
| t_hsas_calpersontplentry | 6 |
| t_hsas_calresulttpl_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
