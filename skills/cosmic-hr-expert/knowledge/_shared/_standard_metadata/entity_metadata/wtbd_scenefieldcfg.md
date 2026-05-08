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

# wtbd_scenefieldcfg — 取数项目

**表单编码**: `wtbd_scenefieldcfg`  
**表单ID**: `3M4AEF1KHDC1`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_scenefieldcfg（取数项目） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_scenefieldcfg` | 主表 · 45 列 |
| `t_wtbd_scenefieldcfg_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 字段标识 | TextField | t_wtbd_scenefieldcfg.fnumber |  |  |
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
| parent | 项目分组 | ParentBasedataField | — |  | wtbd_scenefieldcfg |
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
| type | 类型 | ComboField | t_wtbd_scenefieldcfg.ftype | ✓ |  |
| belongobj | 来源实体 | BasedataField | t_wtbd_scenefieldcfg.fbelongobjid |  | hbp_entityobject |
| fieldlist | 来源字段 | ComboField | — |  |  |
| category | 类别 | ComboField | t_wtbd_scenefieldcfg.fcategory | ✓ |  |
| uniquecode | 唯一标识码 | TextField | t_wtbd_scenefieldcfg.funiquecode |  |  |
| calctype | 公式类型 | MulComboField | t_wtbd_scenefieldcfg.fcalctype |  |  |
| grouptype | 项目分组类别 | ComboField | t_wtbd_scenefieldcfg.fgrouptype |  |  |
| way | 取数方式 | ComboField | t_wtbd_scenefieldcfg.fway | ✓ |  |
| scale | 数据精度 | ComboField | t_wtbd_scenefieldcfg.fscale |  |  |
| tailprocessing | 精度尾差处理 | ComboField | t_wtbd_scenefieldcfg.ftailprocessing |  |  |
| parentf7 | 项目分组 | BasedataField | — | ✓ | wtbd_retrievalgroup |
| config | 所属取数配置 | BasedataField | — |  | wtbd_retrievalconfig |
| numberx | 编码 | TextField | t_wtbd_scenefieldcfg.fnumberx | ✓ |  |
| assigntype | 分配类型 | ComboField | t_wtbd_scenefieldcfg.fassigntype |  |  |
| applyorg | 适用组织 | OrgField | t_wtbd_scenefieldcfg.fapplyorg |  | bos_org |
| multiple | 支持多选 | CheckBoxField | t_wtbd_scenefieldcfg.fmultiple |  |  |
| dateformat | 掩码 | ComboField | t_wtbd_scenefieldcfg.fdateformat |  |  |
| combofield | 下拉项 | TextField | t_wtbd_scenefieldcfg.fcombofield |  |  |

## 实体: comboentryentity（下拉项） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| combotitle | 下拉标题 | TextField | — |  |  |
| combovalue | 下拉值 | TextField | — |  |  |
| sourcetag | 来源标记 | ComboField | t_wtbd_scenefieldcfg.fsourcetag |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_scenefieldcfg（主表） | 37 |
| t_wtbd_scenefieldcfg_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
