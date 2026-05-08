---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 15R5/4TCA97N
app_number: wts
app_name: 排班管理
cloud_number: WTC
cloud_name: 工时假勤云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# wts_shiftgroup — 班次组

**表单编码**: `wts_shiftgroup`  
**表单ID**: `1C+7UV8=/9+J`  
**归属**: 工时假勤云 / 排班管理  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wts_shiftgroup（班次组） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wts_shiftgroup` | 主表 · 26 列 |
| `t_wts_shiftentry` | 分录表 · 4 列 |
| `t_wts_shiftgroup_l` | 多语言表 · 4 列 |
| `t_wts_shiftentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wts_shiftgroup.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wts_shiftgroup_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wts_shiftgroup.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wts_shiftgroup.fcreatorid |  | bos_user |
| modifier | 最近修改人 | ModifierField | t_wts_shiftgroup.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wts_shiftgroup.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wts_shiftgroup.fcreatetime |  |  |
| modifytime | 最近修改时间 | ModifyDateField | t_wts_shiftgroup.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wts_shiftgroup.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_wts_shiftgroup_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wts_shiftgroup_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_wts_shiftgroup.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wts_shiftgroup.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wts_shiftgroup.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wts_shiftgroup.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wts_shiftgroup.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wts_shiftgroup.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wts_shiftgroup_l.foriname |  |  |
| boid | 业务ID | BigIntField | t_wts_shiftgroup.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wts_shiftgroup.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wts_shiftgroup.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wts_shiftgroup.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | — |  |  |
| bsed | 生效日期 | DateField | — |  |  |
| bsled | 失效日期 | DateField | — |  |  |
| changedescription | 变更说明 | TextField | t_wts_shiftgroup.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wts_shiftgroup.fhisversion |  |  |

## 实体: wts_shiftentry（班次明细） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| shift | 编码 | BasedataField | t_wts_shiftentry.fshiftid | ✓ | wtbd_shift |
| shiftname | 班次 | MuliLangTextField | t_wts_shiftentry_l.fshiftname |  |  |
| lastshiftstartdate | 最早上班时间 | BasedataPropField | — |  |  |
| lastshiftenddate | 最晚下班时间 | BasedataPropField | — |  |  |
| shiftcolor | 班次颜色 | TextField | t_wts_shiftentry.fshiftcolor |  |  |
| shifttype | 班次类型 | BasedataPropField | — |  |  |
| shiftattribute | 班次属性 | BasedataPropField | — |  |  |
| shiftstartdate | 生效日期 | BasedataPropField | — |  |  |
| isoff | 是否为OFF班 | BasedataPropField | — |  |  |
| entryboid | 长整数3 | BigIntField | t_wts_shiftentry.fentryboid |  |  |
| basedatapropfield | 名称 | BasedataPropField | — |  |  |
| authstatus | 分配状态 | BillStatusField | t_wts_shiftgroup.fauthstatus |  |  |
| org | 行政组织 | HRAdminOrgField | — |  | haos_adminorghrf7 |
| createorg | 考勤管理组织 | OrgField | t_wts_shiftgroup.fcreateorgid | ✓ | bos_org |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wts_shiftgroup（主表） | 22 |
| t_wts_shiftentry | 3 |
| t_wts_shiftentry_l | 1 |
| t_wts_shiftgroup_l | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 12 |
