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

# hsbs_salslipprint — 工资条打印方案

**表单编码**: `hsbs_salslipprint`  
**表单ID**: `3HDY+8MI+OGZ`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_salslipprint（工资条打印方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_salslipprint` | 主表 · 25 列 |
| `t_hsbs_salslipprintent` | 分录表 · 4 列 |
| `t_hsbs_salslipprint_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_salslipprint.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_salslipprint_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_salslipprint.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_salslipprint.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_salslipprint.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_salslipprint.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_salslipprint.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_salslipprint.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_salslipprint.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_salslipprint.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_salslipprint.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_salslipprint.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_salslipprint.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_salslipprint.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_salslipprint.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_salslipprint.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_salslipprint_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_salslipprint_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_salslipprint.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_salslipprint.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_salslipprint.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| country | 国家/地区 | BasedataField | t_hsbs_salslipprint.fcountryid | ✓ | bd_country |
| printtpl | 打印模板 | BasedataField | t_hsbs_salslipprint.fprinttplid | ✓ | bos_print_meta |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| salaryitem | 薪酬项目 | BasedataField | t_hsbs_salslipprintent.fsalaryitemid | ✓ | hsbs_salaryitem |
| datatype | 数据类型 | BasedataPropField | — |  |  |
| iszerodisplay | 数据为0时显示 | CheckBoxField | t_hsbs_salslipprintent.fiszerodisplay |  |  |
| isemptydisplay | 数据为空时显示 | CheckBoxField | t_hsbs_salslipprintent.fisemptydisplay |  |  |
| displayname | 显示名称 | TextField | t_hsbs_salslipprintent.fdisplayname | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_salslipprint（主表） | 20 |
| t_hsbs_salslipprint_l | 3 |
| t_hsbs_salslipprintent | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
