---
source: openapi_runtime
extracted_at: 2026-04-29
extractor: build_standard_metadata_md_from_openapi.py
app_id: 2W6FZY1I61I+
app_number: hrobs
app_name: HR运营基础服务
cloud_number: HRMP
cloud_name: HR基础服务云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hrobs_appgroup — HR服务应用分组

**表单编码**: `hrobs_appgroup`  
**表单ID**: `2WMRSJX85NNB`  
**归属**: HR基础服务云 / HR运营基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hrobs_appgroup（HR服务应用分组） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hrobs_appgroup` | 主表 · 18 列 |
| `t_hrobs_appgroup_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hrobs_appgroup.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hrobs_appgroup_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hrobs_appgroup.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hrobs_appgroup.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hrobs_appgroup.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hrobs_appgroup.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hrobs_appgroup.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hrobs_appgroup.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hrobs_appgroup.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hrobs_appgroup_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hrobs_appgroup_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hrobs_appgroup.findex | ✓ |  |
| issyspreset | 系统预置 | CheckBoxField | t_hrobs_appgroup.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hrobs_appgroup.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hrobs_appgroup.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_hrobs_appgroup.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_hrobs_appgroup.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_hrobs_appgroup_l.foriname |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hrobs_appgroup（主表） | 14 |
| t_hrobs_appgroup_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 1 |
