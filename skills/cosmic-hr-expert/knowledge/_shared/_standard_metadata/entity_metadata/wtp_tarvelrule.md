---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1O9FOLRY18YW
app_number: wtp
app_name: 工时假勤规则
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wtp_tarvelrule — 出差规则

**表单编码**: `wtp_tarvelrule`  
**表单ID**: `1HVN1I2QXU=O`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_tarvelrule（出差规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_triprule` | 主表 · 29 列 |
| `t_wtp_tripruleentry` | 分录表 · 13 列 |
| `t_wtp_triprule_l` | 多语言表 · 3 列 |
| `t_wtp_tripruleentry_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_triprule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_triprule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_triprule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_triprule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_triprule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_triprule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_triprule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_triprule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_triprule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_triprule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_triprule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_triprule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_triprule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_triprule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_triprule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_triprule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_triprule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_triprule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_triprule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_triprule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_triprule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_triprule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_triprule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_triprule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_triprule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_triprule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_triprule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_triprule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_triprule.fhisversion |  |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| originalatt | 原始时长 | BasedataField | t_wtp_tripruleentry.foriginalatt |  | wtbd_attitem |
| roundrule | 舍入规则 | BasedataField | t_wtp_tripruleentry.froundrule |  | wtbd_roundrule |
| roundatt | 结果时长 | BasedataField | t_wtp_tripruleentry.froundatt |  | wtbd_attitem |
| entryboid | 分录BOID | BigIntField | t_wtp_tripruleentry.fentryboid |  |  |
| traveltype | 出差类型 | BasedataField | t_wtp_tripruleentry.ftraveltypeid |  | wtbd_traveltype |
| travelmeter | 出差基础配置 | BasedataField | t_wtp_tripruleentry.ftravelmeterid |  | wtp_travelmeter |
| travelchange | 出差变更配置 | BasedataField | t_wtp_tripruleentry.ftravelchangeid |  | wtp_tripchangeset |
| travelsystem | 出差制度 | BasedataField | t_wtp_tripruleentry.ftravelsystemid |  | wtbd_attpolicy |
| tripsource | 出差时数来源 | BasedataField | t_wtp_tripruleentry.ftripsourceid |  | wtbd_ottimesource |
| limitscope | 限定范围JSON | TextAreaField | t_wtp_tripruleentry.flimitscope |  |  |
| daterangecondition | 计算日期范围JSON | TextAreaField | t_wtp_tripruleentry.fdaterangecondition |  |  |
| calculname | 计算日期范围 | MuliLangTextField | t_wtp_tripruleentry_l.fcalculname |  |  |
| calculdes | 限定条件 | MuliLangTextField | t_wtp_tripruleentry_l.fcalculdes |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_triprule（主表） | 26 |
| t_wtp_triprule_l | 3 |
| t_wtp_tripruleentry | 11 |
| t_wtp_tripruleentry_l | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 7 |
