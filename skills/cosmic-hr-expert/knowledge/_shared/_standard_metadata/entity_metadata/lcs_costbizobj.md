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

# lcs_costbizobj — 人力成本分拆策略

**表单编码**: `lcs_costbizobj`  
**表单ID**: `19OMNA960PQA`  
**归属**: HR基础服务云 / 人力成本基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: lcs_costbizobj（人力成本分拆策略） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_lcs_costbizobj` | 主表 · 19 列 |
| `t_lcs_costbizobj_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_lcs_costbizobj.fnumber | ✓ |  |
| name | 成本设置对象 | MuliLangTextField | t_lcs_costbizobj_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_lcs_costbizobj.fstatus |  |  |
| creator | 创建人 | CreaterField | t_lcs_costbizobj.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_lcs_costbizobj.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_lcs_costbizobj.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_lcs_costbizobj.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_lcs_costbizobj.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_lcs_costbizobj.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_lcs_costbizobj_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_lcs_costbizobj_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_lcs_costbizobj.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_lcs_costbizobj.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_lcs_costbizobj.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_lcs_costbizobj.fdisabledate |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| costbiztype | 业务类型 | BasedataField | t_lcs_costbizobj.fcostbiztypeid |  | lcs_costbiztype |
| controlstrategy | 控制策略 | ComboField | t_lcs_costbizobj.fcontrolstrategy |  |  |
| entityobject | 主实体对象 | BasedataField | t_lcs_costbizobj.fentityobjectid |  | bos_entityobject |
| isallowedsplit | 允许成本分拆 | CheckBoxField | t_lcs_costbizobj.fisallowedsplit |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| initstatus | 初始化状态 | ComboField | — |  |  |
| initbatch | 初始化批次 | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_lcs_costbizobj（主表） | 16 |
| t_lcs_costbizobj_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
