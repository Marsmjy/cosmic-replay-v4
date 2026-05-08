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

# hsas_agencypaybill — 薪资代发单

**表单编码**: `hsas_agencypaybill`  
**表单ID**: `0P4D29=TWH8F`  
**归属**: 薪酬福利云 / 薪资核算  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: hsas_agencypaybill（薪资代发单） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_hsas_agencypaybill` | 主表 · 23 列 |
| `t_hsas_agencypayent` | 分录表 · 11 列 |
| `t_hsas_agencypaybill_l` | 多语言表 · 2 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 单据编号 | BillNoField | t_hsas_agencypaybill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_hsas_agencypaybill.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_hsas_agencypaybill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_hsas_agencypaybill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_hsas_agencypaybill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_hsas_agencypaybill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_hsas_agencypaybill.fmodifytime |  |  |
| createtime | 创建时间 | CreateDateField | t_hsas_agencypaybill.fcreatetime |  |  |
| org | 算发薪管理组织 | OrgField | t_hsas_agencypaybill.forgid | ✓ | bos_org |
| barcode | 条形码 | TextField | t_hsas_agencypaybill.fbarcode |  |  |
| inputdevicetype | 输入设备 | TextField | t_hsas_agencypaybill.finputdevicetype |  |  |
| isexistsworkflow | 是否存在工作流 | CheckBoxField | t_hsas_agencypaybill.fisexistsworkflow |  |  |
| auditstatus | 审批状态 | BillStatusField | t_hsas_agencypaybill.fauditstatus |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_hsas_agencypaybill.feventeffectdate |  |  |
| issubmit | 是否进行过提交(废弃) | CheckBoxField | t_hsas_agencypaybill.fissubmit |  |  |
| billname | 单据名称 | MuliLangTextField | t_hsas_agencypaybill_l.fbillname | ✓ |  |
| description | 备注 | MuliLangTextField | t_hsas_agencypaybill_l.fdescription |  |  |

## 实体: entryentity（代发分录） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| paydetail | 发放明细 | BasedataField | t_hsas_agencypayent.fpaydetailid | ✓ | hsas_paydetail |
| basedatapropfield | 姓名 | BasedataPropField | — |  |  |
| basedatapropfield1 | 工号 | BasedataPropField | — |  |  |
| basedatapropfield2 | 用工关系状态 | BasedataPropField | — |  |  |
| basedatapropfield3 | 管理部门 | BasedataPropField | — |  |  |
| basedatapropfield4 | 薪资档案分组 | BasedataPropField | — |  |  |
| basedatapropfield41 | 所属公司 | BasedataPropField | — |  |  |
| basedatapropfield42 | 行政组织 | BasedataPropField | — |  |  |
| basedatapropfield43 | 岗位 | BasedataPropField | — |  |  |
| basedatapropfield21 | 用工关系类型 | BasedataPropField | — |  |  |
| basedatapropfield22 | 实发项目 | BasedataPropField | — |  |  |
| basedatapropfield23 | 收款币种 | BasedataPropField | — |  |  |
| basedatapropfield24 | 收款金额 | BasedataPropField | — |  |  |
| basedatapropfield242 | 账户名 | BasedataPropField | — |  |  |
| basedatapropfield243 | 账户关系 | BasedataPropField | — |  |  |
| basedatapropfield2431 | 银行账号 | BasedataPropField | — |  |  |
| basedatapropfield2432 | 收款银行 | BasedataPropField | — |  |  |
| basedatapropfield2436 | 核算期次 | BasedataPropField | — |  |  |
| agentpayorg | 付款资金组织 | OrgField | t_hsas_agencypayent.fagentpayorgid |  | bos_org |
| paystate | 付款状态 | ComboField | t_hsas_agencypayent.fpaystate |  |  |
| operator | 添加人 | UserField | t_hsas_agencypayent.foperatorid |  | bos_user |
| operatedate | 添加时间 | DateTimeField | t_hsas_agencypayent.foperatedate |  |  |
| basedatapropfield7 | 算发薪管理组织 | BasedataPropField | — |  |  |
| payrollgroup | 薪资核算组 | BasedataField | t_hsas_agencypayent.fpayrollgroupid |  | hsas_payrollgrp |
| payamount | 收款金额 | TextField | — |  |  |
| salaryfilehis | 档案历史 | HisModelBasedataField | — |  | hsas_salaryfile |
| paysubjectv | 支付主体 | HisModelBasedataField | — |  | hsbs_paysubject |
| perbankcardv | 银行卡 | BasedataField | t_hsas_agencypayent.fperbankcardvid |  | hsbs_perbankcard |
| paytype | 支付形式 | BasedataField | t_hsas_agencypayent.fpaytypeid |  | hsbs_paymethod |
| failreason | 失败原因 | TextField | t_hsas_agencypayent.ffailreason |  |  |
| totalcount | 待发总笔数 | IntegerField | t_hsas_agencypaybill.ftotalcount |  |  |
| unpaycount | 付款失败 | IntegerField | t_hsas_agencypaybill.funpaycount |  |  |
| feedbackinfo | 反馈信息 | TextField | t_hsas_agencypaybill.ffeedbackinfo |  |  |
| iswriteback | 添加 | CheckBoxField | — |  |  |
| paidcount | 付款成功 | IntegerField | t_hsas_agencypaybill.fpaidcount |  |  |
| summaryname | 单据名称 | TextField | — |  |  |
| summarybillno | 单据编号 | TextField | — |  |  |
| summarycreatetime | 创建时间 | DateTimeField | — |  |  |
| payrollgrpmul | 薪资核算组 | MulBasedataField | — | ✓ |  |
| agencypaystatus | 代发状态 | ComboField | t_hsas_agencypaybill.fagencypaystatus |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_hsas_agencypaybill（主表） | 20 |
| t_hsas_agencypaybill_l | 2 |
| t_hsas_agencypayent | 9 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 26 |
