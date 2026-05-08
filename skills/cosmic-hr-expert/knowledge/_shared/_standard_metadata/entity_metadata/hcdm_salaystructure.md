---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 0VO5EV13=I9W
app_number: hcdm
app_name: 薪酬管理
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcdm_salaystructure — 薪酬结构

**表单编码**: `hcdm_salaystructure`  
**表单ID**: `2TZJJWVSRVTJ`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_salaystructure（薪酬结构） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_salaystructure` | 主表 · 24 列 |
| `t_hcdm_slystructureent` | 分录表 · 1 列 |
| `t_hcdm_salaystructure_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_salaystructure.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_salaystructure_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_salaystructure.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_salaystructure.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_salaystructure.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_salaystructure.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_salaystructure.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_salaystructure.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_salaystructure.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_salaystructure.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_salaystructure.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_salaystructure.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_salaystructure.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_salaystructure.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_salaystructure.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_salaystructure.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_salaystructure_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_salaystructure_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_salaystructure.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_salaystructure.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_salaystructure.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| country | 国家/地区 | BasedataField | t_hcdm_salaystructure.fcountryid | ✓ | bd_country |

## 实体: entryentity（薪酬结构） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| standarditem | 定调薪项目 | BasedataField | t_hcdm_slystructureent.fstandarditemid | ✓ | hsbs_standarditem |
| fixeditemnew | 固定项目 | BasedataPropField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_salaystructure（主表） | 19 |
| t_hcdm_salaystructure_l | 3 |
| t_hcdm_slystructureent | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
