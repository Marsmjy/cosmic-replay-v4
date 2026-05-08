---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: /UHMBBGZQ65X
app_number: hsas
app_name: 薪资核算
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# hsas_approvebill — 薪资审批单

**表单编码**: `hsas_approvebill`  
**表单ID**: `0TSJZB3+D6HX`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_approvebill（薪资审批单） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_approvebill` | 主表 · 27 列 |
| `t_hsas_approvebillent` | 分录表 · 4 列 |
| `t_hsas_aproverviewent` | 分录表 · 3 列 |
| `t_hsas_approvebill_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hsas_approvebill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hsas_approvebill.fbillstatus | ✓ |  |
| creator | 创建人 | CreaterField | t_hsas_approvebill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_approvebill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hsas_approvebill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hsas_approvebill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_approvebill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_approvebill.fcreatetime |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_approvebill.forgid |  | bos_org |
| barcode | 条形码 | TextField | t_hsas_approvebill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_hsas_approvebill.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_hsas_approvebill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hsas_approvebillent.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_hsas_approvebill.feventeffectdate |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_hsas_approvebill.fissubmit |  |  |
| summaryname | 单据名称 | TextField | — |  |  |
| summarybillno | 单据编号 | TextField | — |  |  |
| summaryorg | 算发薪管理组织 | TextField | — |  |  |
| summarycreator | 创建人 | TextField | — |  |  |
| caltask | 核算任务 | BasedataField | t_hsas_approvebill.fcaltaskid |  | hsas_calpayrolltask |
| recentapprovedate | 最近审批日期 | CreateDateField | t_hsas_approvebill.frecentapprovedate |  |  |
| totalpersonnum | 总人数 | IntegerField | t_hsas_approvebill.ftotalpersonnum |  |  |
| totalsalaryfilenum | 总人次 | IntegerField | t_hsas_approvebill.ftotalsalaryfilenum |  |  |

## 实体: calentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| unauditstatus | 反审核标识 | ComboField | — |  |  |
| caltableid | 核算列表 | BigIntField | t_hsas_approvebillent.fcaltableid |  |  |
| calpersonid | 核算名单 | BigIntField | t_hsas_approvebillent.fcalpersonid |  |  |
| paydetailid | 发放明细ID | BigIntField | t_hsas_approvebillent.fpaydetailid |  |  |
| processtime | 处理时长 | TimeField | — |  |  |
| nextauditor | 下一步审核人 | TextField | t_hsas_approvebill.fnextauditor |  |  |
| adminorg | 所属行政组织 | HRAdminOrgField | t_hsas_approvebill.fadminorgid |  | haos_adminorghrf7 |
| description | 备注 | MuliLangTextField | t_hsas_approvebill_l.fdescription |  |  |
| billname | 单据名称 | MuliLangTextField | t_hsas_approvebill_l.fbillname | ✓ |  |
| payrollgroup | 薪资核算组(废弃) | HisModelBasedataField | — |  | hsas_payrollgrp |
| approvebilltpl | 薪资审批单模板 | HisModelBasedataField | — |  | hsas_approvebilltpl |
| approvebilltplv | 薪资审批单模板 | HisModelBasedataField | — |  | hsas_approvebilltpl |
| caltasks | 核算任务集合 | MulBasedataField | — |  |  |
| payrollgroups | 薪资核算组 | MulBasedataField | — |  |  |
| summarycreatetime | 创建时间 | DateTimeField | — |  |  |
| lockstatus | 锁定状态 | ComboField | t_hsas_approvebill.flockstatus |  |  |

## 实体: overviewentryentity（单据体） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| tploverviewid | 模板概览分录id | BigIntField | t_hsas_aproverviewent.ftploverviewid |  |  |
| overviewvalue | 概览值 | DecimalField | t_hsas_aproverviewent.foverviewvalue |  |  |
| currency | 币别 | CurrencyField | — |  | bd_currency |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_approvebill（主表） | 21 |
| t_hsas_approvebill_l | 2 |
| t_hsas_approvebillent | 4 |
| t_hsas_aproverviewent | 2 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 13 |
