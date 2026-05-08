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

# wtp_otsub — 加班扣减配置

**表单编码**: `wtp_otsub`  
**表单ID**: `1EQ6FKD/W431`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_otsub（加班扣减配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_otsub` | 主表 · 38 列 |
| `t_wtp_otsubentry` | 分录表 · 3 列 |
| `t_wtp_otsub_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_otsub.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_otsub_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_otsub.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_otsub.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_otsub.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_otsub.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_otsub.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_otsub.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_otsub.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_otsub.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_otsub.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_otsub.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_otsub.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_otsub.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_otsub.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_otsub.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_otsub_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_otsub_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_otsub.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_otsub.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_otsub.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_otsub.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_otsub.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_otsub.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_otsub.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_otsub.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_otsub.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_otsub.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_otsub.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_otsub.fhisversion |  |  |
| subtype | 扣减方式 | ComboField | t_wtp_otsub.fsubtype | ✓ |  |

## 实体: entryentity（时间段） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| starttime | 开始时间 | TimeField | t_wtp_otsubentry.fstarttime | ✓ |  |
| endtime | 结束时间 | TimeField | t_wtp_otsubentry.fendtime | ✓ |  |
| entryboid | 分录BOID | BigIntField | t_wtp_otsubentry.fentryboid |  |  |
| selectsub | 扣减默认休息时间 | ComboField | t_wtp_otsub.fselectsub |  |  |
| satisfyhours | 满足小时数 | DecimalField | t_wtp_otsub.fsatisfyhours |  |  |
| deducthours | 扣减小时数 | DecimalField | t_wtp_otsub.fdeducthours |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_otsub（主表） | 31 |
| t_wtp_otsub_l | 3 |
| t_wtp_otsubentry | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
