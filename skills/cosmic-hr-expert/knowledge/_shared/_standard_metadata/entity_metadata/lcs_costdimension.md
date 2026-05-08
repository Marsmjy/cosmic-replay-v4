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

# lcs_costdimension — 人力成本维度

**表单编码**: `lcs_costdimension`  
**表单ID**: `198O/3QJ32QD`  
**归属**: HR基础服务云 / 人力成本基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: lcs_costdimension（人力成本维度） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_lcs_costdimension` | 主表 · 30 列 |
| `t_lcs_costdimension_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_lcs_costdimension.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_lcs_costdimension_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_lcs_costdimension.fstatus |  |  |
| creator | 创建人 | CreaterField | t_lcs_costdimension.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_lcs_costdimension.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_lcs_costdimension.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_lcs_costdimension.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_lcs_costdimension.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_lcs_costdimension.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_lcs_costdimension.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_lcs_costdimension.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_lcs_costdimension.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_lcs_costdimension.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_lcs_costdimension.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_lcs_costdimension.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_lcs_costdimension.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_lcs_costdimension_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_lcs_costdimension_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_lcs_costdimension.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_lcs_costdimension.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_lcs_costdimension.fdisabledate |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| valuetype | 值类型 | ComboField | t_lcs_costdimension.fvaluetype | ✓ |  |
| valuesource | 值来源 | BasedataField | t_lcs_costdimension.fvaluesource |  | bos_entityobject |
| assistant | 辅助值来源 | BasedataField | t_lcs_costdimension.fassistantid |  | bos_assistantdatagroup |
| filtercondition | 过滤条件 | TextField | t_lcs_costdimension.ffiltercondition |  |  |
| displayproperty | 值显示属性 | ComboField | t_lcs_costdimension.fdisplayproperty | ✓ |  |
| filterdesc | 过滤条件 | TextField | — |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| initstatus | 初始化状态 | ComboField | — |  |  |
| initbatch | 初始化批次 | BigIntField | — |  |  |
| importproperty | 导入属性 | ComboField | t_lcs_costdimension.fimportproperty | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_lcs_costdimension（主表） | 24 |
| t_lcs_costdimension_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
