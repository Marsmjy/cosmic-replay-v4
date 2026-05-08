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

# hsbs_salarygrade — 薪等

**表单编码**: `hsbs_salarygrade`  
**表单ID**: `4SFHRRO=YOZH`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_salarygrade（薪等） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_salarygrade` | 主表 · 25 列 |
| `t_hsbs_salarygrade_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_salarygrade.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_salarygrade_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_salarygrade.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_salarygrade.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_salarygrade.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_salarygrade.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_salarygrade.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_salarygrade.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_salarygrade.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_salarygrade.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_salarygrade.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_salarygrade.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_salarygrade.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_salarygrade.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_salarygrade.fsrccreateorgid |  | bos_org |
| index | 顺序号 | IntegerField | t_hsbs_salarygrade.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_salarygrade_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_salarygrade_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_salarygrade.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_salarygrade.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_salarygrade.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| areatype | 国家/地区类型 | ComboField | t_hsbs_salarygrade.fareatype | ✓ |  |
| country | 国家/地区 | BasedataField | t_hsbs_salarygrade.fcountryid |  | bd_country |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_salarygrade（主表） | 20 |
| t_hsbs_salarygrade_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
