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

# wtp_vachangeset — 休假变更配置

**表单编码**: `wtp_vachangeset`  
**表单ID**: `22Y8RGPGFJ/=`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_vachangeset（休假变更配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_vachangeset` | 主表 · 35 列 |
| `t_wtp_vachangeentry` | 分录表 · 2 列 |
| `t_wtp_vachangeset_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_vachangeset.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_vachangeset_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_vachangeset.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_vachangeset.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_vachangeset.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_vachangeset.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_vachangeset.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_vachangeset.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_vachangeset.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_vachangeset.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_vachangeset.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_vachangeset.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_vachangeset.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_vachangeset.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_vachangeset.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_vachangeset.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_vachangeset_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_vachangeset_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_vachangeset.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_vachangeset.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_vachangeset.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_vachangeset.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_vachangeset.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_vachangeset.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_vachangeset.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_vachangeset.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_vachangeset.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_vachangeset.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_vachangeset.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_vachangeset.fhisversion |  |  |
| iscloudchangeval | 限制 | CheckBoxField | t_wtp_vachangeset.fiscloudchangeval |  |  |
| changeval | 时间变更方式 | ComboField | t_wtp_vachangeset.fchangeval | ✓ |  |

## 实体: entryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| votypenumber | 编码 | BasedataField | t_wtp_vachangeentry.fvotypenumber | ✓ | wtbd_vacationtype |
| votypename | 名称 | BasedataPropField | — |  |  |
| votypecreateorgnumber | 创建组织 | BasedataPropField | — |  |  |
| votypesyspreset | 系统预置 | BasedataPropField | — |  |  |
| votypeenable | 使用状态 | BasedataPropField | — |  |  |
| votypectrlstrategy | 控制策略 | BasedataPropField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_vachangeentry.fentryboid |  |  |
| iscloudchangetype | 限制 | CheckBoxField | t_wtp_vachangeset.fiscloudchangetype |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_vachangeset（主表） | 30 |
| t_wtp_vachangeentry | 2 |
| t_wtp_vachangeset_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
