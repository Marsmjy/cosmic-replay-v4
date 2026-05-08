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

# wtp_qtdeducrule — 定额扣减规则

**表单编码**: `wtp_qtdeducrule`  
**表单ID**: `2YYT/RE/MGFS`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_qtdeducrule（定额扣减规则） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_qtdeducrule` | 主表 · 34 列 |
| `t_wtp_qtdeducruleentry` | 分录表 · 2 列 |
| `t_wtp_qtdeducodentry` | 分录表 · 3 列 |
| `t_wtp_qtdeducrule_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_qtdeducrule.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_qtdeducrule_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_qtdeducrule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_qtdeducrule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_qtdeducrule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_qtdeducrule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_qtdeducrule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_qtdeducrule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_qtdeducrule.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_qtdeducrule.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_qtdeducrule.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_qtdeducrule.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_qtdeducrule.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_qtdeducrule.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_qtdeducrule.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_qtdeducrule.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_qtdeducrule_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_qtdeducrule_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_qtdeducrule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_qtdeducrule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_qtdeducrule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_qtdeducrule.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_qtdeducrule.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_qtdeducrule.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_qtdeducrule.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_qtdeducrule.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_qtdeducrule.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_qtdeducrule.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_qtdeducrule.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_qtdeducrule.fhisversion |  |  |
| unit | 单位 | ComboField | t_wtp_qtdeducrule.funit | ✓ |  |

## 实体: entryentity（定额类型） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| qttype | 定额名称 | BasedataField | t_wtp_qtdeducruleentry.fqttypeid | ✓ | wtp_qttype |
| qttype_number | 编码 | BasedataPropField | — |  |  |
| qttype_unit | 单位 | BasedataPropField | — |  |  |
| qttype_category | 类型 | BasedataPropField | — |  |  |
| entryboid | 分录BOID | BigIntField | t_wtp_qtdeducodentry.fentryboid |  |  |
| qttypef7 | 定额类型 | BasedataField | — |  | wtp_qttype |
| lowerlimit | 单次最小扣减时长 | DecimalField | t_wtp_qtdeducrule.flowerlimit | ✓ |  |

## 实体: orderentity（扣减顺序） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| sortfield | 排序字段 | ComboField | t_wtp_qtdeducodentry.fsortfield | ✓ |  |
| sortrule | 排序方式 | ComboField | t_wtp_qtdeducodentry.fsortrule | ✓ |  |
| entryboid1 | 分录BOID | BigIntField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_qtdeducrule（主表） | 29 |
| t_wtp_qtdeducodentry | 3 |
| t_wtp_qtdeducrule_l | 3 |
| t_wtp_qtdeducruleentry | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 11 |
