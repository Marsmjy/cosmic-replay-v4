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

# wtp_taplan — 出差方案

**表单编码**: `wtp_taplan`  
**表单ID**: `1IMT=24+L6RW`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_taplan（出差方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_tripplan` | 主表 · 30 列 |
| `t_wtp_tripplan_l` | 多语言表 · 3 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_tripplan.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_tripplan_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_tripplan.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_tripplan.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_tripplan.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_tripplan.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_tripplan.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_tripplan.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_tripplan.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_tripplan.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_tripplan.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_tripplan.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_tripplan.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | — |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_tripplan.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_tripplan.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_tripplan_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_tripplan_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_tripplan.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_tripplan.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_tripplan.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_tripplan.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_tripplan.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_tripplan.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_tripplan.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_tripplan.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_tripplan.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_tripplan.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_tripplan.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_tripplan.fhisversion |  |  |
| policyid | 策略ID | BigIntField | — |  |  |
| setruleway | 配置规则方式 | ComboField | — |  |  |
| policynumber | 策略编码 | TextField | — |  |  |
| triprule | 出差规则 | BasedataField | t_wtp_tripplan.ftripruleid |  | wtp_tarvelrule |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_tripplan（主表） | 27 |
| t_wtp_tripplan_l | 3 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 10 |
