---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 19KOKMMSZFZJ
app_number: wtbd
app_name: 工时假勤基础资料
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtbd_retrievalgroup — 项目分组

**表单编码**: `wtbd_retrievalgroup`  
**表单ID**: `4/5QZ9RAAIHK`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_retrievalgroup（项目分组） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_scenefieldcfg` | 主表 · 34 列 |
| `t_wtbd_scenefieldcfg_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 原编码 | TextField | t_wtbd_scenefieldcfg.fnumber |  |  |
| name | 名称 | MuliLangTextField | t_wtbd_scenefieldcfg_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_scenefieldcfg.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_scenefieldcfg.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_scenefieldcfg.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_scenefieldcfg.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_scenefieldcfg.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_scenefieldcfg.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_scenefieldcfg.fmasterid |  |  |
| longnumber | 长编码 | TextField | t_wtbd_scenefieldcfg.flongnumber |  |  |
| level | 级次 | IntegerField | t_wtbd_scenefieldcfg.flevel |  |  |
| fullname | 长名称 | MuliLangTextField | t_wtbd_scenefieldcfg_l.ffullname |  |  |
| isleaf | 是否叶子 | CheckBoxField | t_wtbd_scenefieldcfg.fisleaf |  |  |
| parent | 上级项目分组 | ParentBasedataField | — | ✓ | wtbd_retrievalgroup |
| createorg | 创建组织 | OrgField | t_wtbd_scenefieldcfg.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_scenefieldcfg.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_scenefieldcfg.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_scenefieldcfg.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_scenefieldcfg.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_scenefieldcfg.fsrccreateorgid |  | bos_org |
| simplename | 简称 | MuliLangTextField | t_wtbd_scenefieldcfg_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_scenefieldcfg_l.fdescription |  |  |
| disabler | 禁用人 | UserField | t_wtbd_scenefieldcfg.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_scenefieldcfg.fdisabledate |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_scenefieldcfg.fissyspreset |  |  |
| index | 排序号 | IntegerField | t_wtbd_scenefieldcfg.findex |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| category | 项目类别 | ComboField | t_wtbd_scenefieldcfg.fcategory | ✓ |  |
| belongobj | 所属对象 | BasedataField | t_wtbd_scenefieldcfg.fbelongobjid |  | hbp_entityobject |
| fieldlist | 属性 | ComboField | — |  |  |
| type | 类型 | ComboField | t_wtbd_scenefieldcfg.ftype |  |  |
| calctype | 公式类型 | MulComboField | t_wtbd_scenefieldcfg.fcalctype |  |  |
| grouptype | 类别 | ComboField | t_wtbd_scenefieldcfg.fgrouptype | ✓ |  |
| numberx | 编码 | TextField | t_wtbd_scenefieldcfg.fnumberx | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_scenefieldcfg（主表） | 27 |
| t_wtbd_scenefieldcfg_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
