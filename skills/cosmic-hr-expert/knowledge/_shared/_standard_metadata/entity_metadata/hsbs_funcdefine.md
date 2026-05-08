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

# hsbs_funcdefine — 函数

**表单编码**: `hsbs_funcdefine`  
**表单ID**: `0K5T8EDC1N2M`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_funcdefine（函数） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_funcdefine` | 主表 · 21 列 |
| `t_hsbs_funcdefineentry` | 分录表 · 3 列 |
| `t_hsbs_funcdefine_l` | 多语言表 · 4 列 |
| `t_hsbs_funcdefineentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_funcdefine.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_funcdefine_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_funcdefine.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_funcdefine.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_funcdefine.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_funcdefine.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_funcdefine.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_funcdefine.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_funcdefine.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_funcdefine_l.fsimplename |  |  |
| description | 功能描述 | MuliLangTextField | t_hsbs_funcdefine_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsbs_funcdefine.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_funcdefine.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_funcdefine.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_funcdefine.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| define | 函数定义 | TextField | t_hsbs_funcdefine.fdefine | ✓ |  |
| functype | 函数类别 | BasedataField | t_hsbs_funcdefine.ffunctypeid | ✓ | hsbs_functype |
| funcdatatype | 返回值类型 | ComboField | t_hsbs_funcdefine.ffuncdatatype | ✓ |  |
| uniquecode | 唯一编码 | TextField | t_hsbs_funcdefine.funiquecode |  |  |

## 实体: entryentity（参数） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paramname | 参数名 | MuliLangTextField | t_hsbs_funcdefineentry_l.fparamname | ✓ |  |
| paramdatatype | 参数类型 | ComboField | t_hsbs_funcdefineentry.fparamdatatype | ✓ |  |
| paramdesc | 参数说明 | MuliLangTextField | t_hsbs_funcdefineentry_l.fparamdesc |  |  |
| funcexp | 函数体 | TextAreaField | t_hsbs_funcdefine.ffuncexp |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_funcdefine（主表） | 17 |
| t_hsbs_funcdefine_l | 3 |
| t_hsbs_funcdefineentry | 1 |
| t_hsbs_funcdefineentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
