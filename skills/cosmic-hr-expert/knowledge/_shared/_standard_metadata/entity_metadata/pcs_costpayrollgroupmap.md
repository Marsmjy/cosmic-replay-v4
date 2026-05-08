---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1ANC8T4UC434
app_number: pcs
app_name: 薪酬成本
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# pcs_costpayrollgroupmap — 薪资核算组层映射

**表单编码**: `pcs_costpayrollgroupmap`  
**表单ID**: `2=GT+3U3P84N`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_costpayrollgroupmap（薪资核算组层映射） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_costitemmap` | 主表 · 33 列 |
| `t_pcs_costitemmap_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_pcs_costitemmap.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_pcs_costitemmap_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_pcs_costitemmap.fstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_costitemmap.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_costitemmap.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_pcs_costitemmap.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_pcs_costitemmap.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_costitemmap.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_pcs_costitemmap.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_pcs_costitemmap_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_pcs_costitemmap_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_pcs_costitemmap.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_pcs_costitemmap.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_pcs_costitemmap.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_pcs_costitemmap.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_pcs_costitemmap.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_pcs_costitemmap.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_pcs_costitemmap.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_pcs_costitemmap.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_pcs_costitemmap.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_pcs_costitemmap.fbsed |  |  |
| bsled | 失效日期 | DateField | t_pcs_costitemmap.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_pcs_costitemmap.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_pcs_costitemmap.fhisversion |  |  |
| source | 薪资核算组 | BasedataField | t_pcs_costitemmap.fsourceid | ✓ | hsas_payrollgrp |
| hrorg | 算发薪管理组织 | OrgField | t_pcs_costitemmap.fhrorgid | ✓ | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_pcs_costitemmap.fctrlstrategy | ✓ |  |
| sourcedata | 原资料ID | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_pcs_costitemmap.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| salaryitem | 薪酬项目 | BasedataField | t_pcs_costitemmap.fsalaryitemid | ✓ | hsbs_salaryitem |
| costitem | 人力成本项目 | BasedataField | t_pcs_costitemmap.fcostitemid | ✓ | lcs_costitem |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_costitemmap（主表） | 27 |
| t_pcs_costitemmap_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
