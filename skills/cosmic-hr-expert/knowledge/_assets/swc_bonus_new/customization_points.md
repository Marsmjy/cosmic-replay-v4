# 奖金管理 (swc_bonus_new) · 扩展点（Customization Points）

> 55 个标准扩展点·按客户需求定制·**不改资产骨架**
> 数据源：deep_scan_class_tree.md（每类 → 1 EP）+ _asset_meta.yaml

## 扩展点总览

| EP ID | 名称 | 修改位置 | 难度 | CS 关联 |
|---|---|---|---|---|
| EP-01 | 改 PersonOrgUtil 行为 | PersonOrgUtil.java | ⭐⭐ | (按需) |
| EP-02 | 改 SWCI18NParam 行为 | SWCI18NParam.java | ⭐⭐ | (按需) |
| EP-03 | 改 BonusUtil 行为 | BonusUtil.java | ⭐⭐ | (按需) |
| EP-04 | 改 PushSalaryUtil 行为 | PushSalaryUtil.java | ⭐⭐ | (按需) |
| EP-05 | 改 TdkwSendMessageUtil 行为 | TdkwSendMessageUtil.java | ⭐⭐ | (按需) |
| EP-06 | 改 BonusDisSumAllPlugin 行为 | BonusDisSumAllPlugin.java | ⭐⭐ | (按需) |
| EP-07 | 改 BonusDistributeSumOperatePlugin 行为 | BonusDistributeSumOperatePlugin.java | ⭐⭐ | (按需) |
| EP-08 | 改 BonusSumApprovalOperatePlugin 行为 | BonusSumApprovalOperatePlugin.java | ⭐⭐ | (按需) |
| EP-09 | 改 OperateDistributeCacheUtil 行为 | OperateDistributeCacheUtil.java | ⭐⭐ | (按需) |
| EP-10 | 改 BonusDistributeSumValidatePlugin 行为 | BonusDistributeSumValidatePlugin.java | ⭐⭐ | (按需) |
| EP-11 | 改 BonusDistributeBillPlugin 行为 | BonusDistributeBillPlugin.java | ⭐⭐ | (按需) |
| EP-12 | 改 BonusDistributeSumListPlugin 行为 | BonusDistributeSumListPlugin.java | ⭐⭐ | (按需) |
| EP-13 | 改 BonusPaymentBillPlugin 行为 | BonusPaymentBillPlugin.java | ⭐⭐ | (按需) |
| EP-14 | 改 BonusPaymentImportPlugin 行为 | BonusPaymentImportPlugin.java | ⭐⭐ | (按需) |
| EP-15 | 改 BounsPaymentListPlugin 行为 | BounsPaymentListPlugin.java | ⭐⭐ | (按需) |
| EP-16 | 改 BounsPaymentUtils 行为 | BounsPaymentUtils.java | ⭐⭐ | (按需) |
| EP-17 | 改 BonusPlanImportPlugin 行为 | BonusPlanImportPlugin.java | ⭐⭐ | (按需) |
| EP-18 | 改 CreateApproval 行为 | CreateApproval.java | ⭐⭐ | (按需) |
| EP-19 | 改 FilterItemByType 行为 | FilterItemByType.java | ⭐⭐ | (按需) |
| EP-20 | 改 BonusPlanApprovalBillPlugin 行为 | BonusPlanApprovalBillPlugin.java | ⭐⭐ | (按需) |
| EP-21 | 改 BonusPlanApprovalOperationPlugin 行为 | BonusPlanApprovalOperationPlugin.java | ⭐⭐ | (按需) |
| EP-22 | 改 BounsAgentApproveBillPlugin 行为 | BounsAgentApproveBillPlugin.java | ⭐⭐ | (按需) |
| EP-23 | 改 BounsImportBillPlugin 行为 | BounsImportBillPlugin.java | ⭐⭐ | (按需) |
| EP-24 | 改 BounsRecodBillPlugin 行为 | BounsRecodBillPlugin.java | ⭐⭐ | (按需) |
| EP-25 | 改 PayrollPushUtil 行为 | PayrollPushUtil.java | ⭐⭐ | (按需) |
| EP-26 | 改 BounsRecordOperation 行为 | BounsRecordOperation.java | ⭐⭐ | (按需) |
| EP-27 | 改 BounsImportOperation 行为 | BounsImportOperation.java | ⭐⭐ | (按需) |
| EP-28 | 改 BounsEndApproveOperation 行为 | BounsEndApproveOperation.java | ⭐⭐ | (按需) |
| EP-29 | 改 BounsAgentApproveOperation 行为 | BounsAgentApproveOperation.java | ⭐⭐ | (按需) |
| EP-30 | 改 BonusPlanConvertPlugin 行为 | BonusPlanConvertPlugin.java | ⭐⭐ | (按需) |
| EP-31 | 改 BonusPlanOperation 行为 | BonusPlanOperation.java | ⭐⭐ | (按需) |
| EP-32 | 改 BonusPlanOtherOperation 行为 | BonusPlanOtherOperation.java | ⭐⭐ | (按需) |
| EP-33 | 改 BonusPlanValidator 行为 | BonusPlanValidator.java | ⭐⭐ | (按需) |
| EP-34 | 改 BounsAgentConvertPlugin 行为 | BounsAgentConvertPlugin.java | ⭐⭐ | (按需) |
| EP-35 | 改 BounsEndConvertPlugin 行为 | BounsEndConvertPlugin.java | ⭐⭐ | (按需) |
| EP-36 | 改 BounsRecordConvertPlugin 行为 | BounsRecordConvertPlugin.java | ⭐⭐ | (按需) |
| EP-37 | 改 BounsPaymentAgentOperation 行为 | BounsPaymentAgentOperation.java | ⭐⭐ | (按需) |
| EP-38 | 改 BounsPaymentValidator 行为 | BounsPaymentValidator.java | ⭐⭐ | (按需) |
| EP-39 | 改 BonusDisListPlugin 行为 | BonusDisListPlugin.java | ⭐⭐ | (按需) |
| EP-40 | 改 BonusDisRejectPlugin 行为 | BonusDisRejectPlugin.java | ⭐⭐ | (按需) |
| EP-41 | 改 BonusDisSecondPlugin 行为 | BonusDisSecondPlugin.java | ⭐⭐ | (按需) |
| EP-42 | 改 TransferToHandleDisTask 行为 | TransferToHandleDisTask.java | ⭐⭐ | (按需) |
| EP-43 | 改 BonusDisSecondPushPlugin 行为 | BonusDisSecondPushPlugin.java | ⭐⭐ | (按需) |
| EP-44 | 改 BonusDisSecValidator 行为 | BonusDisSecValidator.java | ⭐⭐ | (按需) |
| EP-45 | 改 BonusApprovalSheetBillListPlugin 行为 | BonusApprovalSheetBillListPlugin.java | ⭐⭐ | (按需) |
| EP-46 | 改 BonusApprovalSheetBillSecondFlowPlugin 行为 | BonusApprovalSheetBillSecondFlowPlugin.java | ⭐⭐ | (按需) |
| EP-47 | 改 BonusApprovalSheetBillSecondPlugin 行为 | BonusApprovalSheetBillSecondPlugin.java | ⭐⭐ | (按需) |
| EP-48 | 改 BonusWriteBackUtil 行为 | BonusWriteBackUtil.java | ⭐⭐ | (按需) |
| EP-49 | 改 BonusApprovalConvertPlugin 行为 | BonusApprovalConvertPlugin.java | ⭐⭐ | (按需) |
| EP-50 | 改 BonusApprovalAuditPlugin 行为 | BonusApprovalAuditPlugin.java | ⭐⭐ | (按需) |
| EP-51 | 改 BonusApprovalSheetBillSecondPushPlugin 行为 | BonusApprovalSheetBillSecondPushPlugin.java | ⭐⭐ | (按需) |
| EP-52 | 改 BonusApprovalSecValidator 行为 | BonusApprovalSecValidator.java | ⭐⭐ | (按需) |
| EP-53 | 改 CalendarUtil 行为 | CalendarUtil.java | ⭐⭐ | (按需) |
| EP-54 | 改 ObjectUtils 行为 | ObjectUtils.java | ⭐⭐ | (按需) |
| EP-55 | 改 PersonOrgUtil 行为 | PersonOrgUtil.java | ⭐⭐ | (按需) |

---

## EP-01 · 改 PersonOrgUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PersonOrgUtil.java`

### 改造方案

PersonOrgUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-02 · 改 SWCI18NParam 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`SWCI18NParam.java`

### 改造方案

SWCI18NParam 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-03 · 改 BonusUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusUtil.java`

### 改造方案

BonusUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-04 · 改 PushSalaryUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PushSalaryUtil.java`

### 改造方案

PushSalaryUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-05 · 改 TdkwSendMessageUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`TdkwSendMessageUtil.java`

### 改造方案

TdkwSendMessageUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-06 · 改 BonusDisSumAllPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisSumAllPlugin.java`

### 改造方案

BonusDisSumAllPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-07 · 改 BonusDistributeSumOperatePlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDistributeSumOperatePlugin.java`

### 改造方案

BonusDistributeSumOperatePlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-08 · 改 BonusSumApprovalOperatePlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusSumApprovalOperatePlugin.java`

### 改造方案

BonusSumApprovalOperatePlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-09 · 改 OperateDistributeCacheUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`OperateDistributeCacheUtil.java`

### 改造方案

OperateDistributeCacheUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-10 · 改 BonusDistributeSumValidatePlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDistributeSumValidatePlugin.java`

### 改造方案

BonusDistributeSumValidatePlugin 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-11 · 改 BonusDistributeBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDistributeBillPlugin.java`

### 改造方案

BonusDistributeBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-12 · 改 BonusDistributeSumListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDistributeSumListPlugin.java`

### 改造方案

BonusDistributeSumListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-13 · 改 BonusPaymentBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPaymentBillPlugin.java`

### 改造方案

BonusPaymentBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-14 · 改 BonusPaymentImportPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPaymentImportPlugin.java`

### 改造方案

BonusPaymentImportPlugin 继承 BatchImportPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-15 · 改 BounsPaymentListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsPaymentListPlugin.java`

### 改造方案

BounsPaymentListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-16 · 改 BounsPaymentUtils 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsPaymentUtils.java`

### 改造方案

BounsPaymentUtils 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-17 · 改 BonusPlanImportPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanImportPlugin.java`

### 改造方案

BonusPlanImportPlugin 继承 BatchImportPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-18 · 改 CreateApproval 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CreateApproval.java`

### 改造方案

CreateApproval 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-19 · 改 FilterItemByType 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`FilterItemByType.java`

### 改造方案

FilterItemByType 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-20 · 改 BonusPlanApprovalBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanApprovalBillPlugin.java`

### 改造方案

BonusPlanApprovalBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-21 · 改 BonusPlanApprovalOperationPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanApprovalOperationPlugin.java`

### 改造方案

BonusPlanApprovalOperationPlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-22 · 改 BounsAgentApproveBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsAgentApproveBillPlugin.java`

### 改造方案

BounsAgentApproveBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-23 · 改 BounsImportBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsImportBillPlugin.java`

### 改造方案

BounsImportBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-24 · 改 BounsRecodBillPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsRecodBillPlugin.java`

### 改造方案

BounsRecodBillPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-25 · 改 PayrollPushUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PayrollPushUtil.java`

### 改造方案

PayrollPushUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-26 · 改 BounsRecordOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsRecordOperation.java`

### 改造方案

BounsRecordOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-27 · 改 BounsImportOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsImportOperation.java`

### 改造方案

BounsImportOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-28 · 改 BounsEndApproveOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsEndApproveOperation.java`

### 改造方案

BounsEndApproveOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-29 · 改 BounsAgentApproveOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsAgentApproveOperation.java`

### 改造方案

BounsAgentApproveOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-30 · 改 BonusPlanConvertPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanConvertPlugin.java`

### 改造方案

BonusPlanConvertPlugin 继承 AbstractConvertPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-31 · 改 BonusPlanOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanOperation.java`

### 改造方案

BonusPlanOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-32 · 改 BonusPlanOtherOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanOtherOperation.java`

### 改造方案

BonusPlanOtherOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-33 · 改 BonusPlanValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusPlanValidator.java`

### 改造方案

BonusPlanValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-34 · 改 BounsAgentConvertPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsAgentConvertPlugin.java`

### 改造方案

BounsAgentConvertPlugin 继承 AbstractConvertPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-35 · 改 BounsEndConvertPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsEndConvertPlugin.java`

### 改造方案

BounsEndConvertPlugin 继承 AbstractConvertPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-36 · 改 BounsRecordConvertPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsRecordConvertPlugin.java`

### 改造方案

BounsRecordConvertPlugin 继承 AbstractConvertPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-37 · 改 BounsPaymentAgentOperation 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsPaymentAgentOperation.java`

### 改造方案

BounsPaymentAgentOperation 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-38 · 改 BounsPaymentValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BounsPaymentValidator.java`

### 改造方案

BounsPaymentValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-39 · 改 BonusDisListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisListPlugin.java`

### 改造方案

BonusDisListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-40 · 改 BonusDisRejectPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisRejectPlugin.java`

### 改造方案

BonusDisRejectPlugin 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-41 · 改 BonusDisSecondPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisSecondPlugin.java`

### 改造方案

BonusDisSecondPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-42 · 改 TransferToHandleDisTask 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`TransferToHandleDisTask.java`

### 改造方案

TransferToHandleDisTask 继承 AbstractFormPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-43 · 改 BonusDisSecondPushPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisSecondPushPlugin.java`

### 改造方案

BonusDisSecondPushPlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-44 · 改 BonusDisSecValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusDisSecValidator.java`

### 改造方案

BonusDisSecValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-45 · 改 BonusApprovalSheetBillListPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalSheetBillListPlugin.java`

### 改造方案

BonusApprovalSheetBillListPlugin 继承 AbstractListPlugin·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-46 · 改 BonusApprovalSheetBillSecondFlowPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalSheetBillSecondFlowPlugin.java`

### 改造方案

BonusApprovalSheetBillSecondFlowPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-47 · 改 BonusApprovalSheetBillSecondPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalSheetBillSecondPlugin.java`

### 改造方案

BonusApprovalSheetBillSecondPlugin 继承 AbstractBillPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-48 · 改 BonusWriteBackUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusWriteBackUtil.java`

### 改造方案

BonusWriteBackUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-49 · 改 BonusApprovalConvertPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalConvertPlugin.java`

### 改造方案

BonusApprovalConvertPlugin 继承 AbstractConvertPlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-50 · 改 BonusApprovalAuditPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalAuditPlugin.java`

### 改造方案

BonusApprovalAuditPlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-51 · 改 BonusApprovalSheetBillSecondPushPlugin 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalSheetBillSecondPushPlugin.java`

### 改造方案

BonusApprovalSheetBillSecondPushPlugin 继承 AbstractOperationServicePlugIn·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-52 · 改 BonusApprovalSecValidator 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`BonusApprovalSecValidator.java`

### 改造方案

BonusApprovalSecValidator 继承 AbstractValidator·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-53 · 改 CalendarUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`CalendarUtil.java`

### 改造方案

CalendarUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-54 · 改 ObjectUtils 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`ObjectUtils.java`

### 改造方案

ObjectUtils 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)

---

## EP-55 · 改 PersonOrgUtil 行为

### 业务场景

⚡ 按客户需求场景化补充

### 入口

`PersonOrgUtil.java`

### 改造方案

PersonOrgUtil 继承 独立·扩展时按 SDK 白名单 @Override 重写

### 风险

⚡ 按业务真实场景补风险点·参考 W1 标杆 [contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)


---

## ❌ 反指引（不要在本场景做）

| 反模式 | 为什么不应该 |
|---|---|
| ❌ 直接 modifyMeta 标品 form 加字段 | PR-001·必须走 ext 扩展元数据 |
| ❌ 在 BillPlugin 里直接 SaveServiceHelper.save 标品记录 | 应在 OpPlugin before/after·保事务一致 |
| ❌ 在 OpPlugin 里发 BEC 事件 | 标品自带 BEC·ISV 不重发·避免重复事件 |
| ❌ 改标品 form 的 billstatus / auditstatus | 标品状态机·ISV 不能动·只能加 `${ISV_FLAG}_*` 自定义字段 |
| ❌ 继承 ISV 自己的插件加 if/else 分支 | 双继承死循环（PR-001）·必须并列挂多 plugin |

详见 [`_antipatterns.json`](../_antipatterns.json)。

## 关联 PR 红线

- **PR-001** ISV 隔离·所有 ISV 加字段必带 `${ISV_FLAG}_` 前缀·走 ext 扩展元数据 ✅
- **PR-008/009** 时序资料·HisModel 查询要带 `iscurrentversion=1`
- **PR-010** OP 13 个生命周期方法
- **PR-011** BEC 事件中心·标品自带

详见 [`knowledge/_shared/platform_rules.json`](../../_shared/platform_rules.json)

## 关联文档

- 部署 SOP：[deploy_sop.md](deploy_sop.md)
- 业务规则：[02_business_rules.md](../../scenarios/<scene>/02_business_rules.md)
- 扩展点全景：[07_ext_points.md](../../scenarios/<scene>/07_ext_points.md)
- 定制方案：[06_customization_solutions.md](../../scenarios/<scene>/06_customization_solutions.md)
- W1 标杆：[contract_renew_batch customization_points.md](../contract_renew_batch/customization_points.md)
