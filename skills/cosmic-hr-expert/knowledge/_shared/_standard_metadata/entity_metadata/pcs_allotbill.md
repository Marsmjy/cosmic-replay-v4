---
source: openapi_runtime
extracted_at: 2026-05-03
extractor: build_standard_metadata_md_from_openapi.py
app_id: 1ANC8T4UC434
app_number: pcs
app_name: 薪酬成本
cloud_number: SWC
cloud_name: 薪酬福利云
model_type: BillFormModel
_note: 标品 entity_metadata 知识库未覆盖此 form · 用 OpenAPI 实时反推 · 标品同步若后续补齐会自动覆盖
---

# pcs_allotbill — 成本分配单

**表单编码**: `pcs_allotbill`  
**表单ID**: `1PAGSF158TOK`  
**归属**: 薪酬福利云 / 薪酬成本  
**来源系统**: 金蝶苍穹（OpenAPI 实时反推 · 非标品维护）  

---

## 实体: pcs_allotbill（成本分配单） [BillEntity]

### 物理表

| 表名 | 说明 |
|------|------|
| `t_pcs_allotbill` | 主表 · 25 列 |
| `t_pcs_allotbillentry` | 分录表 · 8 列 |
| `t_pcs_allotbill_l` | 多语言表 · 1 列 |

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| billno | 分配单编号 | BillNoField | t_pcs_allotbill.fbillno | ✓ |  |
| billstatus | 单据状态 | BillStatusField | t_pcs_allotbill.fbillstatus |  |  |
| creator | 创建人 | CreaterField | t_pcs_allotbill.fcreatorid |  | bos_user |
| modifier | 修改人 | ModifierField | t_pcs_allotbill.fmodifierid |  | bos_user |
| auditor | 审核人 | UserField | t_pcs_allotbill.fauditorid |  | bos_user |
| auditdate | 审核日期 | DateTimeField | t_pcs_allotbill.fauditdate |  |  |
| modifytime | 修改时间 | ModifyDateField | t_pcs_allotbill.fmodifytime |  |  |
| createtime | 创建日期 | CreateDateField | t_pcs_allotbill.fcreatetime |  |  |
| barcode | 条形码 | TextField | t_pcs_allotbill.fbarcode |  |  |
| eventeffectdate | 事务生效日期(废弃) | DateTimeField | t_pcs_allotbill.feventeffectdate |  |  |
| isexistsworkflow | 是否有工作流 | CheckBoxField | t_pcs_allotbill.fisexistsworkflow |  |  |
| inputdevicetype | 输入设备 | TextField | t_pcs_allotbill.finputdevicetype |  |  |
| auditstatus | 审批状态 | BillStatusField | t_pcs_allotbill.fauditstatus |  |  |
| personcount | 包含人数 | IntegerField | t_pcs_allotbill.fpersoncount |  |  |

## 实体: entryentity（明细数据） [EntryEntity]

### 字段列表

| 字段Key | 中文名 | 类型 | 数据库字段名 | 必填 | 引用 |
|---------|--------|------|-------------|------|------|
| costitem | 成本项目 | BasedataField | t_pcs_allotbillentry.fcostitemid | ✓ | lcs_costitem |
| costsetupconstval | 人力成本维度组合值 | TextField | t_pcs_allotbillentry.fcostsetupconstval |  |  |
| costsetupconst | 核算方案常量 | BasedataField | t_pcs_allotbillentry.fcostsetupconstid | ✓ | pcs_costsetupconst |
| oriamount | 原币金额 | DecimalField | t_pcs_allotbillentry.foriamount |  |  |
| startdate | 薪资起始日期 | DateField | t_pcs_allotbillentry.fstartdate | ✓ |  |
| enddate | 薪资结束日期 | DateField | t_pcs_allotbillentry.fenddate | ✓ |  |
| paydate | 预计支付日期 | DateField | t_pcs_allotbillentry.fpaydate | ✓ |  |
| assuamount | 承担金额 | AmountField | t_pcs_allotbillentry.fassuamount | ✓ |  |
| costadapter | 人力成本维度方案 | BasedataField | t_pcs_allotbill.fcostadapterid | ✓ | lcs_costadaption |
| basedatapropfield | 承担币种 | BasedataPropField | — |  |  |
| org | 算发薪管理组织 | OrgField | t_pcs_allotbill.forgid | ✓ | bos_org |
| description | 备注： | MuliLangTextField | t_pcs_allotbill_l.fdescription |  |  |
| amountsum | 费用总额 | AmountField | t_pcs_allotbill.famountsum | ✓ |  |
| financingstatus | 提交财务状态 | BillStatusField | t_pcs_allotbill.ffinancingstatus |  |  |
| finaccountstatus | 财务记账状态 | BillStatusField | t_pcs_allotbill.ffinaccountstatus |  |  |
| assucurrency | 币种 | CurrencyField | — |  | bd_currency |
| number | 编码 | TextField | — |  |  |
| showcreatetime | 创建时间 | DateTimeField | — |  |  |
| accountorg | 记账组织 | OrgField | t_pcs_allotbill.faccountorgid |  | bos_org |
| postingdate | 记账日期 | BookdateField | t_pcs_allotbill.fpostingdate |  |  |
| splitcondition | 拆单条件 | TextField | t_pcs_allotbill.fsplitcondition |  |  |
| splitconditionvalue | 拆单条件值 | TextField | t_pcs_allotbill.fsplitconditionvalue |  |  |

---

## 字段按物理表分布统计

| 物理表 | 字段数 |
|--------|--------|
| t_pcs_allotbill（主表） | 23 |
| t_pcs_allotbill_l | 1 |
| t_pcs_allotbillentry | 8 |
| 无数据库列（RadioField/虚拟字段/未匹配） | 4 |
