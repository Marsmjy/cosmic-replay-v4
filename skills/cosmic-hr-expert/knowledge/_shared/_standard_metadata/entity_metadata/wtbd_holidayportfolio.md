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

# wtbd_holidayportfolio — 假期组合

**表单编码**: `wtbd_holidayportfolio`  
**表单ID**: `1=V2D09VLY+S`  
**归属**: 工时假勤云 / 工时假勤基础资料  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtbd_holidayportfolio（假期组合） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtbd_hp` | 主表 · 32 列 |
| `t_wtbd_hpentry` | 分录表 · 4 列 |
| `t_wtbd_hp_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtbd_hp.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtbd_hp_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtbd_hp.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtbd_hp.fcreatorid |  | bos_user |
| modifier | 最近修改人 | ModifierField | t_wtbd_hp.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtbd_hp.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtbd_hp.fcreatetime |  |  |
| modifytime | 最近修改时间 | ModifyDateField | t_wtbd_hp.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtbd_hp.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtbd_hp.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtbd_hp.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtbd_hp.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtbd_hp.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtbd_hp.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtbd_hp.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtbd_hp.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtbd_hp_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtbd_hp_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtbd_hp.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtbd_hp.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtbd_hp.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtbd_hp.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtbd_hp.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtbd_hp.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtbd_hp.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtbd_hp.ffirstbsed |  |  |
| bsed | 生效年份 | DateField | t_wtbd_hp.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtbd_hp.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtbd_hp.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtbd_hp.fhisversion |  |  |

## 实体: holidayentity（假期） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| holiday | 编码 | BasedataField | t_wtbd_hpentry.fholidayid | ✓ | wtbd_holiday |
| holidayname | 名称 | BasedataPropField | — |  |  |
| holidayattr | 日期类型 | BasedataPropField | — |  |  |
| holidaytype | 假期类型 | BasedataPropField | — |  |  |
| startyear | 有效开始年份 | DateField | t_wtbd_hpentry.fstartyear | ✓ |  |
| endyear | 有效结束年份 | DateField | t_wtbd_hpentry.fendyear | ✓ |  |
| entryboid | 分录BOID | BigIntField | t_wtbd_hpentry.fentryboid |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtbd_hp（主表） | 27 |
| t_wtbd_hp_l | 3 |
| t_wtbd_hpentry | 4 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
