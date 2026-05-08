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

# hsas_payschedule — 薪资日程

**表单编码**: `hsas_payschedule`  
**表单ID**: `1JPLIE8TFZJU`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_payschedule（薪资日程） [BaseEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_payschedule` | 主表 · 31 列 |
| `t_hsas_payschmsgcfgentry` | 分录表 · 6 列 |
| `t_hsas_payschedule_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| number | 薪资日程编码 | TextField | t_hsas_payschedule.fnumber | ✓ |  |
| name | 薪资日程名称 | MuliLangTextField | t_hsas_payschedule_l.fname |  |  |
| status | 数据状态 | BillStatusField | t_hsas_payschedule.fstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_payschedule.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_payschedule.fmodifierid |  | bos_user |
| enable | 使用状态 | BillStatusField | t_hsas_payschedule.fenable |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_payschedule.fcreatetime |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_payschedule.fmodifytime |  |  |
| masterid | 主数据内码 | MasterIdField | t_hsas_payschedule.fmasterid |  |  |
| simplename | 简称 | MuliLangTextField | — |  |  |
| description | 描述 | MuliLangTextField | t_hsas_payschedule_l.fdescription |  |  |
| index | 排序号 | IntegerField | t_hsas_payschedule.findex |  |  |
| issyspreset | 系统预置 | CheckBoxField | t_hsas_payschedule.fissyspreset |  |  |
| disabler | 禁用人 | UserField | t_hsas_payschedule.fdisablerid |  | bos_user |
| disabledate | 禁用时间 | DateTimeField | t_hsas_payschedule.fdisabledate |  |  |
| initdatasource | 数据来源 | ComboField | — |  |  |
| orinumber | 出厂编码 | TextField | — |  |  |
| oristatus | 出厂数据编辑状态 | ComboField | — |  |  |
| oriname | 出厂名称 | MuliLangTextField | — |  |  |
| paybizaction | 算发薪步骤 | BasedataField | t_hsas_payschedule.fpaybizactionid |  | hsbs_paybizaction |
| startdate | 开始日期 | DateField | t_hsas_payschedule.fstartdate | ✓ |  |
| enddate | 结束日期 | DateField | t_hsas_payschedule.fenddate | ✓ |  |
| tracker | 协作人 | MulBasedataField | — |  |  |

## 实体: entryentity（消息配置） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| isenable | 启用 | CheckBoxField | t_hsas_payschmsgcfgentry.fisenable |  |  |
| receiver | 消息接收对象 | MulBasedataField | — |  |  |
| msgtpl | 消息模板 | BasedataField | t_hsas_payschmsgcfgentry.fmsgtplid |  | msg_template |
| msgpushstep | 日程开始/终止前 | ComboField | t_hsas_payschmsgcfgentry.fmsgpushstep |  |  |
| msgpushnum | 数量 | StepperField | t_hsas_payschmsgcfgentry.fmsgpushnum |  |  |
| msgpushdaytype | 工作日/自然日 | ComboField | t_hsas_payschmsgcfgentry.fmsgpushdaytype |  |  |
| msgpushtime | 时间 | TimeField | t_hsas_payschmsgcfgentry.fmsgpushtime |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_payschedule.forgid |  | bos_org |
| payrollgrp | 薪资核算组 | BasedataField | t_hsas_payschedule.fpayrollgrpid |  | hsas_payrollgrp |
| calperiodtype | 期间类型 | BasedataField | t_hsas_payschedule.fcalperiodtypeid |  | hsbs_calperiodtype |
| calperiod | 期间 | BasedataField | t_hsas_payschedule.fcalperiodid |  | hsbs_calperiod |
| calfrequency | 频度 | BasedataField | t_hsas_payschedule.fcalfrequencyid |  | hsbs_calfrequency |
| paybizproc | 薪资作业流程 | BasedataField | t_hsas_payschedule.fpaybizprocid |  | hsbs_paybizproc |
| prepaybizaction | 前序日程 | MulBasedataField | — |  |  |
| payrollgrpv | 薪资核算组版本 | BasedataField | t_hsas_payschedule.fpayrollgrpvid |  | hsas_payrollgrp |
| payschtpl | 薪资日程模板 | BasedataField | t_hsas_payschedule.fpayschtplid |  | hsas_payscheduletpl |
| schedulestatus | 日程状态 | ComboField | t_hsas_payschedule.fschedulestatus |  |  |
| finishtime | 完成时间 | DateTimeField | t_hsas_payschedule.ffinishtime |  |  |
| payrollscene | 薪资核算场景 | BasedataField | t_hsas_payschedule.fpayrollsceneid |  | hsas_payrollscene |
| markuser | 状态标记人 | UserField | t_hsas_payschedule.fmarkuser |  | bos_user |
| generatebatch | 生成日程批次 | TextField | t_hsas_payschedule.fgeneratebatch |  |  |
| generatesource | 日程来源 | ComboField | t_hsas_payschedule.fgeneratesource |  |  |
| overdue | 逾期状态 | TextField | — |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_payschedule（主表） | 29 |
| t_hsas_payschedule_l | 2 |
| t_hsas_payschmsgcfgentry | 6 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 9 |
