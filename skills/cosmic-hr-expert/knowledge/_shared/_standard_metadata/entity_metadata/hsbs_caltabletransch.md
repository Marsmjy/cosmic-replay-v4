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

# hsbs_caltabletransch — 薪资结果转存配置

**表单编码**: `hsbs_caltabletransch`  
**表单ID**: `5=6IW228B5F+`  
**归属**: 薪酬福利云 / 薪酬基础服务  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsbs_caltabletransch（薪资结果转存配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsbs_caltabletransch` | 主表 · 27 列 |
| `t_hsbs_caltabletranschent` | 分录表 · 6 列 |
| `t_hsbs_caltabletransch_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 转存实体编码 | TextField | t_hsbs_caltabletransch.fnumber | ✓ |  |
| name | 转存实体名称 | MuliLangTextField | t_hsbs_caltabletransch_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsbs_caltabletransch.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsbs_caltabletransch.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsbs_caltabletransch.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsbs_caltabletransch.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsbs_caltabletransch.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsbs_caltabletransch.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsbs_caltabletransch.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | t_hsbs_caltabletransch_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_hsbs_caltabletransch_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsbs_caltabletransch.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsbs_caltabletransch.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsbs_caltabletransch.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsbs_caltabletransch.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_hsbs_caltabletransch.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_hsbs_caltabletransch.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_hsbs_caltabletransch.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_hsbs_caltabletransch.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_hsbs_caltabletransch.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_hsbs_caltabletransch.fbsed |  |  |
| bsled | 失效日期 | DateField | t_hsbs_caltabletransch.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_hsbs_caltabletransch.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_hsbs_caltabletransch.fhisversion |  |  |

## 实体: itementry（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| item | 项目编码 | ItemClassField | — |  |  |
| itemtype | 项目类型 | ItemClassTypeField | t_hsbs_caltabletranschent.fitemtype |  |  |
| itemname | 项目名称 | TextField | — |  |  |
| itemdatatype | 数据类型 | TextField | — |  |  |
| entryboid | entryboid | BigIntField | t_hsbs_caltabletranschent.fentryboid |  |  |
| itemenable | 使用状态 | ComboField | — |  |  |
| itemreleasestatus | 发布状态 | ComboField | t_hsbs_caltabletranschent.fitemreleasestatus |  |  |
| property | 属性名 | TextField | t_hsbs_caltabletranschent.fproperty |  |  |
| suffix | 分表标识 | TextField | t_hsbs_caltabletranschent.fsuffix |  |  |
| trastrigger | 转存时机 | ComboField | t_hsbs_caltabletransch.ftrastrigger | ✓ |  |
| datafilter | 转存条件 | TextAreaField | t_hsbs_caltabletransch.fdatafilter |  |  |
| releasestatus | 发布状态 | BillStatusField | t_hsbs_caltabletransch.freleasestatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsbs_caltabletransch（主表） | 24 |
| t_hsbs_caltabletransch_l | 3 |
| t_hsbs_caltabletranschent | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 8 |
