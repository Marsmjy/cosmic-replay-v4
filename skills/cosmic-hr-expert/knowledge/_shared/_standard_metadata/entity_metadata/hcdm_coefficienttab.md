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

# hcdm_coefficienttab — 薪酬标准系数表

**表单编码**: `hcdm_coefficienttab`  
**表单ID**: `15R44=9RPJRS`  
**归属**: 薪酬福利云 / 薪酬管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcdm_coefficienttab（薪酬标准系数表） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcdm_coefficienttab` | 主表 · 26 列 |
| `t_hcdm_coefficienttab_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcdm_coefficienttab.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcdm_coefficienttab_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcdm_coefficienttab.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcdm_coefficienttab.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcdm_coefficienttab.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcdm_coefficienttab.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcdm_coefficienttab.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcdm_coefficienttab.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcdm_coefficienttab.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hcdm_coefficienttab.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hcdm_coefficienttab.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hcdm_coefficienttab.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hcdm_coefficienttab.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hcdm_coefficienttab.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hcdm_coefficienttab.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hcdm_coefficienttab.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hcdm_coefficienttab_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcdm_coefficienttab_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcdm_coefficienttab.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcdm_coefficienttab.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcdm_coefficienttab.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| latestcoefnum | 最新版本系数数量 | IntegerField | t_hcdm_coefficienttab.flatestcoefnum |  |  |
| hasmodifyingcoef | 存在变更中系数 | CheckBoxField | t_hcdm_coefficienttab.fhasmodifyingcoef |  |  |
| coefficientdim | 系数维度 | BasedataField | t_hcdm_coefficienttab.fcoefficientdimid | ✓ | hcdm_coefficientdim |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcdm_coefficienttab（主表） | 21 |
| t_hcdm_coefficienttab_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
