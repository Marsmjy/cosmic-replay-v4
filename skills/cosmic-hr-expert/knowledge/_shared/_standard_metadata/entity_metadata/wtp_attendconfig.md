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

# wtp_attendconfig — 应出勤配置

**表单编码**: `wtp_attendconfig`  
**表单ID**: `3262RA4+08SW`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_attendconfig（应出勤配置） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_attendconfig` | 主表 · 35 列 |
| `t_wtp_attendconfig_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_attendconfig.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_attendconfig_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_attendconfig.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_attendconfig.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_attendconfig.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_attendconfig.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_attendconfig.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_attendconfig.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_attendconfig.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_attendconfig.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_attendconfig.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_attendconfig.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_attendconfig.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_attendconfig.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_attendconfig.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_attendconfig.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_attendconfig_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_attendconfig_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_attendconfig.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_attendconfig.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_attendconfig.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_attendconfig.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_attendconfig.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_attendconfig.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_attendconfig.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_attendconfig.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_attendconfig.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_attendconfig.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_attendconfig.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_attendconfig.fhisversion |  |  |
| attenddays | 应出勤天数 | ComboField | t_wtp_attendconfig.fattenddays | ✓ |  |
| shiftconditionjson | 应出勤日期条件设置 | LargeTextField | t_wtp_attendconfig.fshiftconditionjson |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_attendconfig（主表） | 29 |
| t_wtp_attendconfig_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 6 |
