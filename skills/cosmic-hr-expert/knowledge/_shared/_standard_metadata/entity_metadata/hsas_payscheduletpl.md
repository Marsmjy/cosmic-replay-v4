---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BaseFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_payscheduletpl — 薪资日程模板

**表单编码**: `hsas_payscheduletpl`  
**表单ID**: `1KPRA+B9GGPR`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_payscheduletpl（薪资日程模板） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_payscheduletpl` | 主表 · 35 列 |
| `t_hsas_payschtplentry` | 分录表 · 4 列 |
| `t_hsas_payschtpldetail` | 分录表 · 6 列 |
| `t_hsas_payscheduletpl_l` | 多语言表 · 2 列 |
| `t_hsas_payschtplentry_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 编码 | TextField | t_hsas_payscheduletpl.fnumber | ✓ |  |
| name | 名称 | MuliLangTextField | t_hsas_payscheduletpl_l.fname | ✓ |  |
| status | 数据状态 | BillStatusField | t_hsas_payscheduletpl.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_payscheduletpl.fcreatorid | ✓ | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_payscheduletpl.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_payscheduletpl.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_payscheduletpl.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_payscheduletpl.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_payscheduletpl.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_hsas_payscheduletpl_l.fdescription |  |  |
| index | 顺序号 | IntegerField | t_hsas_payscheduletpl.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_payscheduletpl.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_payscheduletpl.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_payscheduletpl.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| paybizproc | 算发薪作业流程 | BasedataField | t_hsas_payscheduletpl.fpaybizprocid |  | hsbs_paybizproc |

## 实体: entryentity（算发薪流程） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paybizaction | 日程名称 | BasedataField | t_hsas_payschtplentry.fpaybizactionid | ✓ | hsbs_paybizaction |
| basedatapropfield2 | 步骤类型 | BasedataPropField | — |  |  |
| actdaytype | 工作日/自然日 | ComboField | t_hsas_payschtplentry.factdaytype | ✓ |  |
| prepaybizaction | 前序日程 | MulBasedataField | — |  |  |
| tracker | 协作人 | MulBasedataField | — |  |  |
| paybizactiondesc | 描述 | MuliLangTextField | t_hsas_payschtplentry_l.fpaybizactiondesc |  |  |
| msgcfgop | 消息通知 | TextField | — |  |  |
| actdaynum | 天数 | IntegerField | t_hsas_payschtplentry.factdaynum | ✓ |  |
| isenable | 启用 | CheckBoxField | t_hsas_payschtpldetail.fisenable |  |  |
| receiver | 消息接收对象 | MulBasedataField | — | ✓ |  |
| msgtpl | 消息模板 | BasedataField | t_hsas_payschtpldetail.fmsgtplid | ✓ | msg_template |
| msgpushstep | 日程开始/终止前 | ComboField | t_hsas_payschtpldetail.fmsgpushstep |  |  |
| msgpushnum | 数量 | StepperField | t_hsas_payschtpldetail.fmsgpushnum |  |  |
| msgpushdaytype | 工作日/自然日 | ComboField | t_hsas_payschtpldetail.fmsgpushdaytype |  |  |
| msgpushtime | 时间 | TimeField | t_hsas_payschtpldetail.fmsgpushtime |  |  |
| paybizactionpoint | 日程 | BasedataField | — | ✓ | hsbs_paybizaction |
| actdateboundary | 日期 | ComboField | t_hsas_payscheduletpl.factdateboundary | ✓ |  |
| isworkday | 遇非工作日， | CheckBoxField | t_hsas_payscheduletpl.fisworkday |  |  |
| selworkday | 休息日规则 | ComboField | t_hsas_payscheduletpl.fselworkday | ✓ |  |
| perioddateboundary | 日期 | ComboField | t_hsas_payscheduletpl.fperioddateboundary |  |  |
| operation | 偏移规则 | ComboField | t_hsas_payscheduletpl.foperation |  |  |
| oprationdays | 天数 | StepperField | t_hsas_payscheduletpl.foprationdays |  |  |
| daytype | 偏移规则 | ComboField | t_hsas_payscheduletpl.fdaytype | ✓ |  |
| selperiod | 偏移规则 | ComboField | t_hsas_payscheduletpl.fselperiod |  |  |
| everymonthday | 每月几日 | StepperField | t_hsas_payscheduletpl.feverymonthday |  |  |

## 实体: entryentitysch（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paybizactionexample | 日程名称 | BasedataField | — |  | hsbs_paybizaction |
| startdate | 开始日期 | DateField | — |  |  |
| enddate | 结束日期 | DateField | — |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_payscheduletpl.forgid | ✓ | bos_org |
| generalenname | 通用英文名 | TextField | t_hsas_payscheduletpl.fgeneralenname |  |  |
| calperiodtype | 期间类型 | BasedataField | t_hsas_payscheduletpl.fcalperiodtypeid | ✓ | hsbs_calperiodtype |
| calfrequency | 期间频度 | BasedataField | t_hsas_payscheduletpl.fcalfrequencyid |  | hsbs_calfrequency |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_payscheduletpl.fpayrollsceneid | ✓ | hsas_payrollscene |
| actdatetype | 基准 | ComboField | t_hsas_payscheduletpl.factdatetype | ✓ |  |
| payrollgrp | 薪资核算组 | HisModelBasedataField | — | ✓ | hsas_payrollgrp |
| payrollgrpv | 薪资核算组版本 | HisModelBasedataField | — | ✓ | hsas_payrollgrp |
| country | 国家/地区 | BasedataField | t_hsas_payscheduletpl.fcountryid | ✓ | bd_country |
| workplan | 公共日历 | QueryField | — | ✓ | hrcs_workingplanquery |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_payscheduletpl（主表） | 29 |
| t_hsas_payscheduletpl_l | 2 |
| t_hsas_payschtpldetail | 6 |
| t_hsas_payschtplentry | 3 |
| t_hsas_payschtplentry_l | 1 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 17 |
