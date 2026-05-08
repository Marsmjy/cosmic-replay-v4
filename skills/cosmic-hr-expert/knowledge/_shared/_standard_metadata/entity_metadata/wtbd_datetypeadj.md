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

# wtbd_datetypeadj — 日类型规则

**表单编码**: `wtbd_datetypeadj`  
**表单ID**: `2GHZ70=O3S6I`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_datetypeadj（日类型规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_datetypeadj` | 主表 · 34 列 |
| `t_wtbd_datetypeadjentry` | 分录表 · 5 列 |
| `t_wtbd_datetypeadj_l` | 多语言表 · 4 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_datetypeadj.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_datetypeadj_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_datetypeadj.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_datetypeadj.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtbd_datetypeadj.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_datetypeadj.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_datetypeadj.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtbd_datetypeadj.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_datetypeadj.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_datetypeadj.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_datetypeadj.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_datetypeadj.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_datetypeadj.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_datetypeadj.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_datetypeadj.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_datetypeadj.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_datetypeadj_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_datetypeadj_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_datetypeadj.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_datetypeadj.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_datetypeadj.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | t_wtbd_datetypeadj.forinumber |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | t_wtbd_datetypeadj.foristatus |  |  |
| oriname | 出厂名称 | MuliLangTextField | t_wtbd_datetypeadj_l.foriname |  |  |
| boid | 业务ID | BigIntField | t_wtbd_datetypeadj.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_datetypeadj.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_datetypeadj.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_datetypeadj.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | — |  |  |
| bsed | 生效日期 | DateField | — |  |  |
| bsled | 失效日期 | DateField | — |  |  |
| changedescription | 变更说明 | TextField | — |  |  |
| hisversion | 版本号 | TextField | t_wtbd_datetypeadj.fhisversion |  |  |
| specialdate | 特殊日期 | BasedataField | t_wtbd_datetypeadj.fspecialdateid |  | wtbd_specialdate |
| dayofweeks | 星期值 | MulComboField | t_wtbd_datetypeadj.fdayofweeks | ✓ |  |
| datetypes | 日期类型 | MulBasedataField | — | ✓ |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| dayofweek | 星期值 | ComboField | t_wtbd_datetypeadjentry.fdayofweek |  |  |
| fromdatetype | 日期类型 | BasedataField | t_wtbd_datetypeadjentry.ffromdatetypeid |  | wtbd_datetype |
| combination | 组合 | TextField | — |  |  |
| todatetype | 日期类型调整 | BasedataField | t_wtbd_datetypeadjentry.ftodatetypeid |  | wtbd_datetype |
| busmeaning | 业务语义 | TextField | — |  |  |
| entryboid | 历史模型BOID | BigIntField | t_wtbd_datetypeadjentry.fentryboid |  |  |
| shiftofftype | 班次类型 | ComboField | t_wtbd_datetypeadjentry.fshiftofftype |  |  |
| shiftofftypes | 是否为OFF班 | MulComboField | t_wtbd_datetypeadj.fshiftofftypes |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_datetypeadj（主表） | 28 |
| t_wtbd_datetypeadj_l | 4 |
| t_wtbd_datetypeadjentry | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
