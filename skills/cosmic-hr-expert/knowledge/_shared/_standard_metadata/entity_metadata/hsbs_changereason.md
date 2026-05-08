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

# hsbs_changereason — 变动原因

**表单编码**: `hsbs_changereason`  
**表单ID**: `34L0G47GBC2G`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_changereason（变动原因） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_changereason` | 主表 · 19 列 |
| `t_hsbs_changereason_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsbs_changereason.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsbs_changereason_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_changereason.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_changereason.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_changereason.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_changereason.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_changereason.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_changereason.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_changereason.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_changereason_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_changereason_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_changereason.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_changereason.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_changereason.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_changereason.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hsbs_changereason.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hsbs_changereason.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hsbs_changereason_l.foriname |  |  |
| bussinessfield | 业务类型 | BasedataField | t_hsbs_changereason.fbussinessfieldid | ✓ | hbss_bussinessfield |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_changereason（主表） | 15 |
| t_hsbs_changereason_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
