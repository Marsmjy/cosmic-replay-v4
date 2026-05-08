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

# wtp_accountplan — 核算方案

**表单编码**: `wtp_accountplan`  
**表单ID**: `1U074/WM8H/7`  
**归属**: 工时假勤云 / 工时假勤规则  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: wtp_accountplan（核算方案） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_wtp_accountplan` | 主表 · 34 列 |
| `t_wtp_phaseentry` | 分录表 · 10 列 |
| `t_wtp_stepsentry` | 分录表 · 5 列 |
| `t_wtp_accountplan_l` | 多语言表 · 3 列 |
| `t_wtp_phaseentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_wtp_accountplan.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_wtp_accountplan_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_wtp_accountplan.fstatus |  |  |
| creator | 创建人 | CreaterField | t_wtp_accountplan.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_wtp_accountplan.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_wtp_accountplan.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_wtp_accountplan.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_wtp_accountplan.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_wtp_accountplan.fmasterid |  |  |
| createorg | 创建组织 | OrgField | t_wtp_accountplan.fcreateorgid | ✓ | bos_org |
| org | 管理组织 | OrgField | t_wtp_accountplan.forgid |  | bos_org |
| useorg | 使用组织 | OrgField | t_wtp_accountplan.fuseorgid |  | bos_org |
| ctrlstrategy | 控制策略 | ComboField | t_wtp_accountplan.fctrlstrategy | ✓ |  |
| sourcedata | 原资料id | BigIntField | — |  |  |
| bitindex | 位图 | IntegerField | t_wtp_accountplan.fbitindex |  |  |
| srcindex | 原资料位图 | IntegerField | — |  |  |
| srccreateorg | 原创建组织 | OrgField | t_wtp_accountplan.fsrccreateorgid |  | bos_org |
| index | 排序号 | IntegerField | t_wtp_accountplan.findex |  |  |
| simplename | 简称 | MuliLangTextField | t_wtp_accountplan_l.fsimplename |  |  |
| description | 描述 | MuliLangTextField | t_wtp_accountplan_l.fdescription |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_wtp_accountplan.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_wtp_accountplan.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_wtp_accountplan.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| boid | 业务ID | BigIntField | t_wtp_accountplan.fboid |  |  |
| iscurrentversion | 是否当前生效数据 | CheckBoxField | t_wtp_accountplan.fiscurrentversion |  |  |
| datastatus | 数据版本状态 | ComboField | t_wtp_accountplan.fdatastatus |  |  |
| sourcevid | 关联历史版本 | BigIntField | t_wtp_accountplan.fsourcevid |  |  |
| firstbsed | 最早生效日期 | DateField | t_wtp_accountplan.ffirstbsed |  |  |
| bsed | 生效日期 | DateField | t_wtp_accountplan.fbsed |  |  |
| bsled | 失效日期 | DateField | t_wtp_accountplan.fbsled |  |  |
| changedescription | 变更说明 | TextField | t_wtp_accountplan.fchangedescription |  |  |
| hisversion | 版本号 | TextField | t_wtp_accountplan.fhisversion |  |  |

## 实体: entryentity（阶段单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| phasename | 阶段名称 | TextField | t_wtp_phaseentry.fphasename |  |  |
| del | 是否可删除 | CheckBoxField | t_wtp_phaseentry.fdel |  |  |
| phasenumber | 编码 | TextField | t_wtp_phaseentry.fphasenumber |  |  |
| entryboid | 分录业务版本ID | BigIntField | t_wtp_stepsentry.fentryboid |  |  |
| phasetype | 核算阶段类型 | BasedataField | t_wtp_phaseentry.fphasetype |  | wtp_accountstagetype |
| move | 是否可移动 | CheckBoxField | t_wtp_phaseentry.fmove |  |  |
| phaseresultdeal | 阶段结果处理 | ComboField | t_wtp_phaseentry.fphaseresultdeal |  |  |
| phasesave | 阶段落库 | CheckBoxField | t_wtp_phaseentry.fphasesave |  |  |
| noreturnhandler | 无输出决策器 | ComboField | t_wtp_phaseentry.fnoreturnhandler |  |  |
| phasenamelang | 阶段名称多语言 | MuliLangTextField | t_wtp_phaseentry_l.fphasenamelang |  |  |
| stepid | 步骤ID | BasedataField | t_wtp_stepsentry.fstepid |  | wtp_accountsteps |
| stepname | 名称 | BasedataPropField | — |  |  |
| stepsyspreset | 系统预置 | BasedataPropField | — |  |  |
| stepindex | 排序码 | BasedataPropField | — |  |  |
| stepstartdate | 生效日期 | BasedataPropField | — |  |  |
| stependdate | 失效日期 | BasedataPropField | — |  |  |
| setpresulttype | 结果类型 | BasedataPropField | — |  |  |
| setpcalrule | 计算规则 | BasedataPropField | — |  |  |
| entryboid1 | 分录业务版本ID | BigIntField | — |  |  |
| stepnoreturnhandler | 无输出决策器 | ComboField | t_wtp_stepsentry.fstepnoreturnhandler |  |  |
| stepdel | 是否可删除 | CheckBoxField | t_wtp_stepsentry.fstepdel |  |  |
| stepmove | 是否可移动 | CheckBoxField | t_wtp_stepsentry.fstepmove |  |  |
| accounttype | 核算类型 | ComboField | t_wtp_accountplan.faccounttype | ✓ |  |
| accountmode | 核算方式 | ComboField | t_wtp_accountplan.faccountmode | ✓ |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_wtp_accountplan（主表） | 29 |
| t_wtp_accountplan_l | 3 |
| t_wtp_phaseentry | 8 |
| t_wtp_phaseentry_l | 1 |
| t_wtp_stepsentry | 5 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 14 |
