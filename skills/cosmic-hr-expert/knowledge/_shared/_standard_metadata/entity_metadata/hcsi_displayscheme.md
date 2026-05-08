---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2AXKDRPJUQ77
app_number: hcsi
app_name: 中国社保
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hcsi_displayscheme — 社保明细显示方案

**表单编码**: `hcsi_displayscheme`  
**表单ID**: `3PVPHUDRU/GC`  
**归属**: 薪酬福利云 / 中国社保  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hcsi_displayscheme（社保明细显示方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hcsi_displayscheme` | 主表 · 19 列 |
| `t_hcsi_displayschentry` | 分录表 · 7 列 |
| `t_hcsi_displayscheme_l` | 多语言表 · 3 列 |
| `t_hcsi_displayschentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hcsi_displayscheme.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hcsi_displayscheme_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hcsi_displayscheme.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hcsi_displayscheme.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hcsi_displayscheme.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hcsi_displayscheme.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hcsi_displayscheme.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hcsi_displayscheme.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hcsi_displayscheme.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hcsi_displayscheme_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hcsi_displayscheme_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hcsi_displayscheme.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hcsi_displayscheme.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hcsi_displayscheme.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hcsi_displayscheme.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| org | 社保公积金管理组织 | OrgField | t_hcsi_displayscheme.forgid | ✓ | bos_org |
| welfarepayer | 参保单位 | MulBasedataField | — | ✓ |  |
| isdefaulttpl | 是否为默认模板 | CheckBoxField | t_hcsi_displayscheme.fisdefaulttpl |  |  |
| ishighlight | 调整数据是否高亮显示 | CheckBoxField | t_hcsi_displayscheme.fishighlight |  |  |
| highlightcolor | 高亮字体颜色 | ComboField | t_hcsi_displayscheme.fhighlightcolor |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isfreeze | 冻结 | CheckBoxField | t_hcsi_displayschentry.fisfreeze |  |  |
| order | 排序 | ComboField | t_hcsi_displayschentry.forder |  |  |
| priority | 排序优先级 | ComboField | t_hcsi_displayschentry.fpriority |  |  |
| ishide | 隐藏 | CheckBoxField | t_hcsi_displayschentry.fishide |  |  |
| showname | 显示名称 | MuliLangTextField | t_hcsi_displayschentry_l.fshowname |  |  |
| itemclasstype | 字段类型 | ItemClassTypeField | t_hcsi_displayschentry.fitemclasstype |  |  |
| itemclass | 字段名称 | ItemClassField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hcsi_displayscheme（主表） | 16 |
| t_hcsi_displayscheme_l | 3 |
| t_hcsi_displayschentry | 5 |
| t_hcsi_displayschentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
