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

# hsas_itemgroup — 项目资格组

**表单编码**: `hsas_itemgroup`  
**表单ID**: `1P+6=VDDAAST`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_itemgroup（项目资格组） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_itemgrp` | 主表 · 27 列 |
| `t_hsas_itemgrpsl` | 分录表 · 2 列 |
| `t_hsas_itemgrpsltype` | 分录表 · 2 列 |
| `t_hsas_itemgrpslexc` | 分录表 · 2 列 |
| `t_hsas_itemgrp_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_itemgrp.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_itemgrp_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_itemgrp.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_itemgrp.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_itemgrp.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_itemgrp.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_itemgrp.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_itemgrp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_itemgrp.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_itemgrp_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_itemgrp_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_itemgrp.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_itemgrp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_itemgrp.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_itemgrp.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsas_itemgrp.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsas_itemgrp.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsas_itemgrp.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsas_itemgrp.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsas_itemgrp.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsas_itemgrp.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsas_itemgrp.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsas_itemgrp.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsas_itemgrp.fhisversion |  |  |
| generalenname | 通用英文名 | TextField | t_hsas_itemgrp.fgeneralenname |  |  |

## 实体: slitementry（薪酬项目配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitem | 薪酬项目名称 | BasedataField | t_hsas_itemgrpsl.fsalaryitemid |  | hsbs_salaryitem |
| salaryitemnumber | 薪酬项目编码 | BasedataPropField | — |  |  |
| basedatapropfield1 | 薪酬项目系统分类 | BasedataPropField | — |  |  |
| entryboid | 分录业务ID | BigIntField | t_hsas_itemgrpsl.fentryboid |  |  |

## 实体: slitemtypeentry（按薪酬项目系统分类设置单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitemtype | 薪酬项目系统分类名称 | BasedataField | t_hsas_itemgrpsltype.fsalaryitemtypeid |  | hsbs_salaryitemtype |
| salaryitemtypenumber | 薪酬项目系统分类编码 | BasedataPropField | — |  |  |
| entryboidtype | 类别分录业务ID | BigIntField | t_hsas_itemgrpsltype.fentryboidtype |  |  |
| excsalaryitem | 薪酬项目 | BasedataField | t_hsas_itemgrpslexc.fexcsalaryitemid |  | hsbs_salaryitem |
| entryboidexc | 排除子分录业务ID | BigIntField | t_hsas_itemgrpslexc.fentryboidexc |  |  |
| country | 国家/地区 | BasedataField | t_hsas_itemgrp.fcountryid |  | bd_country |
| areatype | 国家/地区类型 | ComboField | t_hsas_itemgrp.fareatype | ✓ |  |
| org | 算发薪管理组织 | OrgField | t_hsas_itemgrp.forgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_itemgrp（主表） | 25 |
| t_hsas_itemgrp_l | 3 |
| t_hsas_itemgrpsl | 2 |
| t_hsas_itemgrpslexc | 2 |
| t_hsas_itemgrpsltype | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
