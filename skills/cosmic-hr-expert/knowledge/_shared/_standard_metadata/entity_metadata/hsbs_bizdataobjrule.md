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

# hsbs_bizdataobjrule — 业务数据对象规则

**表单编码**: `hsbs_bizdataobjrule`  
**表单ID**: `1ZXLCM2LFOLN`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_bizdataobjrule（业务数据对象规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_bizdataobjrule` | 主表 · 28 列 |
| `t_hsbs_bizdataobjruleent` | 分录表 · 4 列 |
| `t_hsbs_bizdataobjruledim` | 分录表 · 2 列 |
| `t_hsbs_bizdatapropent` | 分录表 · 4 列 |
| `t_hsbs_bizdataobjrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_bizdataobjrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_bizdataobjrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_bizdataobjrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_bizdataobjrule.fcreatorid |  | bos_user |
| modifier | 更新人 | ModifierField | t_hsbs_bizdataobjrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_bizdataobjrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_bizdataobjrule.fcreatetime |  |  |
| modifytime | 更新日期 | ModifyDateField | t_hsbs_bizdataobjrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_bizdataobjrule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_bizdataobjrule_l.fsimplename |  |  |
| description | 备注 | MuliLangTextField | t_hsbs_bizdataobjrule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_bizdataobjrule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_bizdataobjrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_bizdataobjrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_bizdataobjrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_bizdataobjrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_bizdataobjrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_bizdataobjrule.fdatastatus |  |  |
| sourcevid | 关联历史版本ID/来源版本 | BigIntField | t_hsbs_bizdataobjrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_bizdataobjrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_bizdataobjrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_bizdataobjrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_bizdataobjrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_bizdataobjrule.fhisversion |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| bizobjruleconf | 字段 | BasedataField | t_hsbs_bizdataobjruleent.fbizobjruleconfid |  | hsbs_bizobjruleconf |
| mustrequired | 是否必填 | CheckBoxField | t_hsbs_bizdataobjruleent.fmustrequired |  |  |
| remark | 说明 | BasedataPropField | — |  |  |
| mustchoose | 是否必选 | BasedataPropField | — |  |  |
| mustmodify | 是否可修改 | BasedataPropField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsbs_bizdatapropent.fentryboid |  |  |
| fillmode | 导入填写方式 | ComboField | t_hsbs_bizdataobjruleent.ffillmode | ✓ |  |
| default | 是否默认 | CheckBoxField | t_hsbs_bizdataobjrule.fdefault |  |  |
| matchdatetype | 使用日期匹配任职 | ComboField | t_hsbs_bizdataobjrule.fmatchdatetype | ✓ |  |
| matchmode | 匹配人员任职方式 | ComboField | t_hsbs_bizdataobjrule.fmatchmode | ✓ |  |
| matchrule | 薪资档案匹配规则 | RadioGroupField | t_hsbs_bizdataobjrule.fmatchrule |  |  |
| depemp | 按人员任职匹配薪资档案推送算薪 | RadioField | — |  |  |
| customdim | 填写并使用自定义维度匹配薪资档案推送算薪 | RadioField | — |  |  |

## 实体: dimentryentity（自定义维度分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| entityobject | 提报维度 | BasedataField | t_hsbs_bizdataobjruledim.fentityobjectid |  | hbp_entityobject |
| dimensionlistlink | 维度值列表 | TextField | — |  |  |
| entryboiddim | 分录业务ID | BigIntField | — |  |  |

## 实体: bizdatapropent（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| fillmethod | 填写方式 | ComboField | t_hsbs_bizdatapropent.ffillmethod | ✓ |  |
| isshow | 是否显示 | ComboField | t_hsbs_bizdatapropent.fisshow |  |  |
| memo | 说明 | TextField | — |  |  |
| field | 字段 | ComboField | t_hsbs_bizdatapropent.ffield |  |  |
| entryboid1 | 分录业务ID | BigIntField | — |  |  |
| ismatchgrp | 推送算薪时赋值薪资核算组 | CheckBoxField | t_hsbs_bizdataobjrule.fismatchgrp |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_bizdataobjrule（主表） | 26 |
| t_hsbs_bizdataobjrule_l | 3 |
| t_hsbs_bizdataobjruledim | 1 |
| t_hsbs_bizdataobjruleent | 3 |
| t_hsbs_bizdatapropent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
