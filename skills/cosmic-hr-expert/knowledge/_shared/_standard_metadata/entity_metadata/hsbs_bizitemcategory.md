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

# hsbs_bizitemcategory — 业务项目类别

**表单编码**: `hsbs_bizitemcategory`  
**表单ID**: `1EJ7AFU6F8KV`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_bizitemcategory（业务项目类别） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_bizitemcategory` | 主表 · 24 列 |
| `t_hsbs_bizitemcategory_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_bizitemcategory.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_bizitemcategory_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_bizitemcategory.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_bizitemcategory.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_bizitemcategory.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_bizitemcategory.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_bizitemcategory.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_bizitemcategory.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_bizitemcategory.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_hsbs_bizitemcategory.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_hsbs_bizitemcategory.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_hsbs_bizitemcategory.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_hsbs_bizitemcategory.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_hsbs_bizitemcategory.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_hsbs_bizitemcategory.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_hsbs_bizitemcategory.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_bizitemcategory_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_bizitemcategory_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_bizitemcategory.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_bizitemcategory.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_bizitemcategory.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| radiogroupfield | 单选按钮组 | RadioGroupField | — |  |  |
| hrbusinessfield | 业务领域 | BasedataField | t_hsbs_bizitemcategory.fhrbusinessfieldid |  | hbss_hrbusinessfield |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_bizitemcategory（主表） | 19 |
| t_hsbs_bizitemcategory_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
