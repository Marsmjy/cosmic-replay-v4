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

# hsas_resultcheckscheme — 算薪结果检核方案

**表单编码**: `hsas_resultcheckscheme`  
**表单ID**: `2HR0JR+9M/VO`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_resultcheckscheme（算薪结果检核方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_retcheckscheme` | 主表 · 23 列 |
| `t_hsas_checkitementry` | 分录表 · 16 列 |
| `t_hsas_retcheckscheme_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_retcheckscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_retcheckscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_retcheckscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_retcheckscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_retcheckscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_retcheckscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_retcheckscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_retcheckscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_retcheckscheme.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_retcheckscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_retcheckscheme_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_retcheckscheme.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_retcheckscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_retcheckscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_retcheckscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_retcheckscheme.forgid | ✓ | bos_org |
| payrollgroup | 薪资核算组 | BasedataField | t_hsas_retcheckscheme.fpayrollgroupid | ✓ | hsas_payrollgrp |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_retcheckscheme.fpayrollsceneid | ✓ | hsas_payrollscene |
| changestatus | 变更状态 | ComboField | t_hsas_retcheckscheme.fchangestatus |  |  |
| chainincrease | 环比增额 | CheckBoxField | t_hsas_retcheckscheme.fchainincrease |  |  |
| yeargrowth | 同比增幅 | CheckBoxField | t_hsas_retcheckscheme.fyeargrowth |  |  |
| chaingrowth | 环比增幅 | CheckBoxField | t_hsas_retcheckscheme.fchaingrowth |  |  |
| yearincrease | 同比增额 | CheckBoxField | t_hsas_retcheckscheme.fyearincrease |  |  |

## 实体: valueentry（隐藏分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| itemnumber | 项目编码 | TextField | t_hsas_checkitementry.fitemnumber |  |  |
| itemname | 项目名称 | TextField | t_hsas_checkitementry.fitemname |  |  |
| workbenchshow | 工作台展示 | CheckBoxField | t_hsas_checkitementry.fworkbenchshow |  |  |
| itemid | 项目ID | BigIntField | t_hsas_checkitementry.fitemid |  |  |
| valueequals | 值= | TextField | t_hsas_checkitementry.fvalueequals |  |  |
| valueless | 值< | TextField | t_hsas_checkitementry.fvalueless |  |  |
| valuebigger | 值> | TextField | t_hsas_checkitementry.fvaluebigger |  |  |
| chainincreaseless | 环比增额≤ | TextField | t_hsas_checkitementry.fchainincreaseless |  |  |
| chainincreasebigger | 环比增额≥ | TextField | t_hsas_checkitementry.fchainincreasebigger |  |  |
| chaingrowthless | 环比增幅≤ | TextField | t_hsas_checkitementry.fchaingrowthless |  |  |
| chaingrowthbigger | 环比增幅≥ | TextField | t_hsas_checkitementry.fchaingrowthbigger |  |  |
| yearincreaseless | 同比增额≤ | TextField | t_hsas_checkitementry.fyearincreaseless |  |  |
| yearincreasebigger | 同比增额≥ | TextField | t_hsas_checkitementry.fyearincreasebigger |  |  |
| yeargrowthless | 同比增幅≤ | TextField | t_hsas_checkitementry.fyeargrowthless |  |  |
| yeargrowthbigger | 同比增幅≥ | TextField | t_hsas_checkitementry.fyeargrowthbigger |  |  |
| itemtype | 项目类别 | ComboField | t_hsas_checkitementry.fitemtype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_retcheckscheme（主表） | 20 |
| t_hsas_checkitementry | 16 |
| t_hsas_retcheckscheme_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
