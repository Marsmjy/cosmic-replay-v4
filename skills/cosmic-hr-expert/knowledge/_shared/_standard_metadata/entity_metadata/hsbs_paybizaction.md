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

# hsbs_paybizaction — 算发薪步骤

**表单编码**: `hsbs_paybizaction`  
**表单ID**: `1J9ZRO1RU/3L`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_paybizaction（算发薪步骤） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_paybizaction` | 主表 · 24 列 |
| `t_hsbs_paybizaction_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_paybizaction.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_paybizaction_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_paybizaction.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_paybizaction.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_paybizaction.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_paybizaction.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_paybizaction.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_paybizaction.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_paybizaction.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_paybizaction.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_paybizaction.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_paybizaction.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_paybizaction.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_paybizaction.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_paybizaction.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_paybizaction.findex |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_paybizaction_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_paybizaction.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_paybizaction.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_paybizaction.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| generalenname | 通用英文名 | TextField | t_hsbs_paybizaction.fgeneralenname |  |  |
| basedatafield | 实体对象 | BasedataField | — |  | bos_objecttype |
| steptype | 步骤类型 | ComboField | t_hsbs_paybizaction.fsteptype | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_paybizaction（主表） | 20 |
| t_hsbs_paybizaction_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
