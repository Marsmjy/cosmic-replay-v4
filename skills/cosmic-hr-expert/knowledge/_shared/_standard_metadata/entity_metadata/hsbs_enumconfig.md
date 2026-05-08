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

# hsbs_enumconfig — 枚举值配置

**表单编码**: `hsbs_enumconfig`  
**表单ID**: `0M3L+U8Y61ZQ`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_enumconfig（枚举值配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_enumconfig` | 主表 · 15 列 |
| `t_hsbs_enumentry` | 分录表 · 2 列 |
| `t_hsbs_enumconfig_l` | 多语言表 · 3 列 |
| `t_hsbs_enumentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_enumconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_enumconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_enumconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_enumconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_enumconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_enumconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_enumconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_enumconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_enumconfig.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_enumconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_enumconfig_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_enumconfig.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_enumconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_enumconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_enumconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: entryentity（枚举分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| enumvalue | 值 | TextField | t_hsbs_enumentry.fenumvalue | ✓ |  |
| enumname | 显示名称 | MuliLangTextField | t_hsbs_enumentry_l.fenumname | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_enumconfig（主表） | 12 |
| t_hsbs_enumconfig_l | 3 |
| t_hsbs_enumentry | 1 |
| t_hsbs_enumentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
