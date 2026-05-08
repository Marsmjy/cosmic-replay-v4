---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 198IF7HLNV46
app_number: lcs
app_name: 人力成本基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# lcs_costcenter — 人力成本中心

**表单编码**: `lcs_costcenter`  
**表单ID**: `2YECURL+52F7`  
**归属**: HR基础服务云 / 人力成本基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: lcs_costcenter（人力成本中心） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_lcs_costcenter` | 主表 · 18 列 |
| `t_lcs_costcenter_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_lcs_costcenter.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_lcs_costcenter_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_lcs_costcenter.fstatus |  |  |
| creator | 创建人 | CreaterField | t_lcs_costcenter.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_lcs_costcenter.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_lcs_costcenter.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_lcs_costcenter.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_lcs_costcenter.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_lcs_costcenter.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_lcs_costcenter_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_lcs_costcenter_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_lcs_costcenter.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_lcs_costcenter.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_lcs_costcenter.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_lcs_costcenter.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| englishname | 通用英文名 | TextField | t_lcs_costcenter.fenglishname |  |  |
| parent | 上级成本中心 | BasedataField | t_lcs_costcenter.fparentid |  | lcs_costcenter |
| createorg | 算发薪管理组织 | OrgField | t_lcs_costcenter.fcreateorgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_lcs_costcenter（主表） | 15 |
| t_lcs_costcenter_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
