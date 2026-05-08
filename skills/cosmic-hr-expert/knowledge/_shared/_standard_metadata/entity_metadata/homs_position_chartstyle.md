---
source: openapi_runtime
extracted_at: 2026-04-28
extractor: build_standard_metadata_md_from_openapi.py
app_id: 217WYC/L9U7E
app_number: homs
app_name: 组织管理
cloud_number: ODC
cloud_name: 组织发展云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# homs_position_chartstyle — 关系结构样式设置

**表单编码**: `homs_position_chartstyle`  
**表单ID**: `2P4ANJ5JN91L`  
**归属**: 组织发展云 / 组织管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: homs_position_chartstyle（关系结构样式设置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_homs_chartstyle` | 主表 · 20 列 |
| `t_homs_chartstyleentry` | 分录表 · 2 列 |
| `t_homs_chastyorgentry` | 分录表 · 2 列 |
| `t_homs_chartstyle_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_homs_chartstyle.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_homs_chartstyle_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_homs_chartstyle.fstatus |  |  |
| creator | 创建人 | CreaterField | t_homs_chartstyle.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_homs_chartstyle.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_homs_chartstyle.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_homs_chartstyle.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_homs_chartstyle.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_homs_chartstyle.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_homs_chartstyle_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_homs_chartstyle_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_homs_chartstyle.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_homs_chartstyle.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_homs_chartstyle.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_homs_chartstyle.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_homs_chartstyle.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_homs_chartstyle.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_homs_chartstyle_l.foriname |  |  |

## 实体: chartstyleentry（卡片样式分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| cardstyle | 卡片样式 | BasedataField | t_homs_chartstyleentry.fcardstyleid |  | homs_cardstyle_new |
| displayfield | 显示字段 | BasedataPropField | — |  |  |
| carddimension | 卡片维度 | BasedataField | t_homs_chartstyleentry.fcarddimensionid |  | homs_carddimension |

## 实体: chastyorgentry（适用组织范围） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| adminorg | 组织名称 | HRAdminOrgField | t_homs_chastyorgentry.fadminorgid | ✓ | haos_adminorghrf7 |
| iscontainslower | 包含下级 | CheckBoxField | t_homs_chastyorgentry.fiscontainslower |  |  |
| adminorgnumber | 组织编码 | TextField | — |  |  |
| orgteam | 所属组织 | HRAdminOrgField | t_homs_chartstyle.forgteamid | ✓ | haos_adminorghrf7 |
| carddimensionid | 卡片维度 | BasedataField | t_homs_chartstyleentry.fcarddimensionid | ✓ | homs_carddimension |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_homs_chartstyle（主表） | 15 |
| t_homs_chartstyle_l | 4 |
| t_homs_chartstyleentry | 3 |
| t_homs_chastyorgentry | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 3 |
