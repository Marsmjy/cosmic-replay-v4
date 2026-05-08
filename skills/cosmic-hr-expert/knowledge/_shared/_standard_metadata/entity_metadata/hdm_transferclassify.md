---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1WX49GDDDZ0V
app_number: hdm
app_name: 调配管理
cloud_number: HR
cloud_name: 核心人力云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hdm_transferclassify — 调动类别

**表单编码**: `hdm_transferclassify`  
**表单ID**: `2VD=HBNN9ZBK`  
**归属**: 核心人力云 / 调配管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hdm_transferclassify（调动类别） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hdm_transferclassify` | 主表 · 18 列 |
| `t_hdm_transferclassify_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hdm_transferclassify.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hdm_transferclassify_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hdm_transferclassify.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hdm_transferclassify.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hdm_transferclassify.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hdm_transferclassify.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hdm_transferclassify.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hdm_transferclassify.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hdm_transferclassify.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hdm_transferclassify_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hdm_transferclassify_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hdm_transferclassify.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hdm_transferclassify.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hdm_transferclassify.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hdm_transferclassify.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hdm_transferclassify.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hdm_transferclassify.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hdm_transferclassify_l.foriname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hdm_transferclassify（主表） | 14 |
| t_hdm_transferclassify_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
