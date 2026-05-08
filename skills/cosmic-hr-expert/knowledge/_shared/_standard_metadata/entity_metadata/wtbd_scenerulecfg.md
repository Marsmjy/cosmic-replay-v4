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

# wtbd_scenerulecfg — 取数应用场景

**表单编码**: `wtbd_scenerulecfg`  
**表单ID**: `3M6J52TTFAFL`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_scenerulecfg（取数应用场景） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_scenerule` | 主表 · 26 列 |
| `t_wtbd_sceneruleparam` | 分录表 · 5 列 |
| `t_wtbd_sceneruleorg` | 分录表 · 1 列 |
| `t_wtbd_scenerulefield` | 分录表 · 4 列 |
| `t_wtbd_scenerule_l` | 多语言表 · 3 列 |
| `t_wtbd_sceneruleparam_l` | 多语言表 · 1 列 |
| `t_wtbd_scenerulefield_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_sceneruleparam.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_sceneruleparam_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_scenerule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_scenerule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_scenerule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_scenerule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_scenerule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_scenerule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_scenerule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_scenerule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_scenerule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_scenerule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_scenerule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_scenerule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_scenerule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_scenerule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_scenerule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_scenerule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_scenerulefield.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_scenerule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_scenerule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |

## 实体: entryentity（条件参数） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| inputnumber | 标识 | TextField | — | ✓ |  |
| inputname | 名称 | MuliLangTextField | — |  |  |
| inputparamstype | 参数类型 | ComboField | — |  |  |
| inputobject | 项目分组 | BasedataField | — |  | wtbd_retrievalgroup |
| entryispreset | 系统预置 | CheckBoxField | — |  |  |
| fielddisplayname | 展示名称 | MuliLangTextField | — |  |  |
| fieldid | 取数项目 | BasedataField | t_wtbd_scenerulefield.ffieldid |  | wtbd_scenefieldcfg |
| fieldissyspreset | 系统预置 | CheckBoxField | — |  |  |
| param1 | 中台场景参数 | TextField | t_wtbd_scenerulefield.fparam1 |  |  |
| grouptype | 取数类别 | ComboField | t_wtbd_scenerule.fgrouptype | ✓ |  |
| calctype | 公式类型 | ComboField | t_wtbd_scenerule.fcalctype |  |  |
| usecfgname | 应用目标 | TextField | — |  |  |
| assigntype | 分配类型 | ComboField | t_wtbd_scenerule.fassigntype | ✓ |  |

## 实体: applyscope（适用组织） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| applyorg | 考勤管理组织 | OrgField | t_wtbd_sceneruleorg.fapplyorgid |  | bos_org |
| orgnumber | 编码 | BasedataPropField | — |  |  |
| applyorgf7 | 考勤管理组织f7 | OrgField | — |  | bos_org |
| usecfg | 应用控件 | MulBasedataField | — | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_scenerule（主表） | 19 |
| t_wtbd_scenerule_l | 2 |
| t_wtbd_scenerulefield | 3 |
| t_wtbd_sceneruleorg | 1 |
| t_wtbd_sceneruleparam | 1 |
| t_wtbd_sceneruleparam_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
