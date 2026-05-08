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

# hsas_paynode — 时间窗口约束设置

**表单编码**: `hsas_paynode`  
**表单ID**: `2+TYI013RTIO`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_paynode（时间窗口约束设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_paynode` | 主表 · 21 列 |
| `t_hsas_paynodebizent` | 分录表 · 1 列 |
| `t_hsas_paynodecalent` | 分录表 · 2 列 |
| `t_hsas_paynode_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 约束规则编码 | TextField | t_hsas_paynode.fnumber | ✓ |  |
| name | 约束规则名称 | MuliLangTextField | t_hsas_paynode_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_paynode.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_paynode.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_paynode.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_paynode.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_paynode.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_paynode.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_paynode.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsas_paynode_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsas_paynode_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_paynode.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_paynode.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_paynode.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_paynode.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| createorg | 算发薪管理组织 | OrgField | t_hsas_paynode.fcreateorgid | ✓ | bos_org |
| paynodetype | 约束对象类型 | ComboField | t_hsas_paynode.fpaynodetype | ✓ |  |
| paynodegrpent | 时间节点 | BasedataField | t_hsas_paynode.fpaynodegrpentid | ✓ | hsas_paynodegrpenthis |
| paynodegrp | 时间窗口模板 | BasedataField | t_hsas_paynode.fpaynodegrpid | ✓ | hsas_paynodegrp |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| basedatapropfield | 业务数据模板编码 | BasedataPropField | — |  |  |
| basedatapropfield1 | 业务数据模板名称 | BasedataPropField | — |  |  |
| bizcalperiod | 期间类型 | BasedataPropField | — |  |  |
| bizitem | 业务数据模板 | BasedataField | — |  | hsbs_bizitemgroup |

## 实体: entryentityget（取数单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| payrollgrp | 薪资核算组 | BasedataField | t_hsas_paynodecalent.fpayrollgrpid | ✓ | hsas_payrollgrp |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_paynodecalent.fpayrollsceneid | ✓ | hsas_payrollscene |
| calrule | 计算规则 | BasedataPropField | — |  |  |
| periodtype | 期间类型 | BasedataPropField | — |  |  |
| bizallcfg | 默认规则 | CheckBoxField | t_hsas_paynode.fbizallcfg |  |  |
| calperiod | 期间类型 | BasedataPropField | — |  |  |
| paynodegrphis | 时间窗口模板版本 | HisModelBasedataField | — | ✓ | hsas_paynodegrp |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_paynode（主表） | 17 |
| t_hsas_paynode_l | 3 |
| t_hsas_paynodecalent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
