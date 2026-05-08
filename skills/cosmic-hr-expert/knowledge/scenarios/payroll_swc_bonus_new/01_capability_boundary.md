# payroll_swc_bonus_new · 能力边界

> **资产名**：奖金新版
> **数据源**：deep_scan_inventory.md / deep_scan_audit.md A/G 段

## 场景定位

| 项 | 值 |
|---|---|
| 所属云 / 应用 | payroll / swc |
| 主 form | `swc_bonusbill` |
| ISV 包前缀 | `tdkw.swc.hsbm.common` |
| Java 业务文件数 | 57 |

## 类清单（继承自苍穹白名单基类）

- `PersonOrgUtil` (PersonOrgUtil.java) extends `(独立)`
- `SWCI18NParam` (SWCI18NParam.java) extends `(独立)`
- `BonusUtil` (BonusUtil.java) extends `(独立)`
- `PushSalaryUtil` (PushSalaryUtil.java) extends `(独立)`
- `TdkwSendMessageUtil` (TdkwSendMessageUtil.java) extends `(独立)`
- `BonusDisSumAllPlugin` (BonusDisSumAllPlugin.java) extends `AbstractBillPlugIn`
- `BonusDistributeSumOperatePlugin` (BonusDistributeSumOperatePlugin.java) extends `AbstractOperationServicePlugIn`
- `BonusSumApprovalOperatePlugin` (BonusSumApprovalOperatePlugin.java) extends `AbstractOperationServicePlugIn`
- `OperateDistributeCacheUtil` (OperateDistributeCacheUtil.java) extends `(独立)`
- `BonusDistributeSumValidatePlugin` (BonusDistributeSumValidatePlugin.java) extends `AbstractValidator`
- `BonusDistributeBillPlugin` (BonusDistributeBillPlugin.java) extends `AbstractBillPlugIn`
- `BonusDistributeSumListPlugin` (BonusDistributeSumListPlugin.java) extends `AbstractListPlugin`
- `BonusPaymentBillPlugin` (BonusPaymentBillPlugin.java) extends `AbstractBillPlugIn`
- `BonusPaymentImportPlugin` (BonusPaymentImportPlugin.java) extends `BatchImportPlugin`
- `BounsPaymentListPlugin` (BounsPaymentListPlugin.java) extends `AbstractListPlugin`
- `BounsPaymentUtils` (BounsPaymentUtils.java) extends `(独立)`
- `BonusPlanImportPlugin` (BonusPlanImportPlugin.java) extends `BatchImportPlugin`
- `CreateApproval` (CreateApproval.java) extends `AbstractListPlugin`
- `FilterItemByType` (FilterItemByType.java) extends `AbstractFormPlugin`
- `BonusPlanApprovalBillPlugin` (BonusPlanApprovalBillPlugin.java) extends `AbstractBillPlugIn`
- `BonusPlanApprovalOperationPlugin` (BonusPlanApprovalOperationPlugin.java) extends `AbstractOperationServicePlugIn`
- `BounsAgentApproveBillPlugin` (BounsAgentApproveBillPlugin.java) extends `AbstractBillPlugIn`
- `BounsImportBillPlugin` (BounsImportBillPlugin.java) extends `AbstractBillPlugIn`
- `BounsRecodBillPlugin` (BounsRecodBillPlugin.java) extends `AbstractBillPlugIn`
- `PayrollPushUtil` (PayrollPushUtil.java) extends `(独立)`
- `BounsRecordOperation` (BounsRecordOperation.java) extends `AbstractOperationServicePlugIn`
- `BounsImportOperation` (BounsImportOperation.java) extends `AbstractOperationServicePlugIn`
- `BounsEndApproveOperation` (BounsEndApproveOperation.java) extends `AbstractOperationServicePlugIn`
- `BounsAgentApproveOperation` (BounsAgentApproveOperation.java) extends `AbstractOperationServicePlugIn`
- `BonusPlanConvertPlugin` (BonusPlanConvertPlugin.java) extends `AbstractConvertPlugIn`
- `BonusPlanOperation` (BonusPlanOperation.java) extends `AbstractOperationServicePlugIn`
- `BonusPlanOtherOperation` (BonusPlanOtherOperation.java) extends `AbstractOperationServicePlugIn`
- `BonusPlanValidator` (BonusPlanValidator.java) extends `AbstractValidator`
- `BounsAgentConvertPlugin` (BounsAgentConvertPlugin.java) extends `AbstractConvertPlugIn`
- `BounsEndConvertPlugin` (BounsEndConvertPlugin.java) extends `AbstractConvertPlugIn`
- `BounsRecordConvertPlugin` (BounsRecordConvertPlugin.java) extends `AbstractConvertPlugIn`
- `BounsPaymentAgentOperation` (BounsPaymentAgentOperation.java) extends `AbstractOperationServicePlugIn`
- `BounsPaymentValidator` (BounsPaymentValidator.java) extends `AbstractValidator`
- `BonusDisListPlugin` (BonusDisListPlugin.java) extends `AbstractListPlugin`
- `BonusDisRejectPlugin` (BonusDisRejectPlugin.java) extends `AbstractFormPlugin`
- `BonusDisSecondPlugin` (BonusDisSecondPlugin.java) extends `AbstractBillPlugIn`
- `TransferToHandleDisTask` (TransferToHandleDisTask.java) extends `AbstractFormPlugin`
- `BonusDisSecondPushPlugin` (BonusDisSecondPushPlugin.java) extends `AbstractOperationServicePlugIn`
- `BonusDisSecValidator` (BonusDisSecValidator.java) extends `AbstractValidator`
- `BonusApprovalSheetBillListPlugin` (BonusApprovalSheetBillListPlugin.java) extends `AbstractListPlugin`
- `BonusApprovalSheetBillSecondFlowPlugin` (BonusApprovalSheetBillSecondFlowPlugin.java) extends `AbstractBillPlugIn`
- `BonusApprovalSheetBillSecondPlugin` (BonusApprovalSheetBillSecondPlugin.java) extends `AbstractBillPlugIn`
- `BonusWriteBackUtil` (BonusWriteBackUtil.java) extends `(独立)`
- `BonusApprovalConvertPlugin` (BonusApprovalConvertPlugin.java) extends `AbstractConvertPlugIn`
- `BonusApprovalAuditPlugin` (BonusApprovalAuditPlugin.java) extends `AbstractOperationServicePlugIn`
- `BonusApprovalSheetBillSecondPushPlugin` (BonusApprovalSheetBillSecondPushPlugin.java) extends `AbstractOperationServicePlugIn`
- `BonusApprovalSecValidator` (BonusApprovalSecValidator.java) extends `AbstractValidator`
- `CalendarUtil` (CalendarUtil.java) extends `(独立)`
- `ObjectUtils` (ObjectUtils.java) extends `(独立)`
- `PersonOrgUtil` (PersonOrgUtil.java) extends `(独立)`

## 涉及 form 清单（来自真扫）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `bd_currency` | BonusUtil.java:43, BonusSumApprovalOperatePlugin.java:220, BonusSumApprovalOperatePlugin.java:339 | — |
| `bos_list` | BounsPaymentListPlugin.java:189 | — |
| `bos_org` | PersonOrgUtil.java:32, PersonOrgUtil.java:47, PersonOrgUtil.java:18 等 | — |
| `bos_user` | BonusUtil.java:207, BonusDisSumAllPlugin.java:92, BonusDisSumAllPlugin.java:226 等 | — |
| `haos_adminorg` | FilterItemByType.java:75, FilterItemByType.java:85, FilterItemByType.java:250 等 | — |
| `haos_adminorghr` | PersonOrgUtil.java:36, PersonOrgUtil.java:42, BonusDisSumAllPlugin.java:117 等 | — |
| `hrpi_assignment` | BonusApprovalSheetBillSecondPlugin.java:805 | — |
| `hrpi_chargeperson` | BonusWriteBackUtil.java:226 | — |
| `hrpi_empposorgrel` | BonusDisSumAllPlugin.java:229, BonusDistributeSumOperatePlugin.java:255, BounsPaymentUtils.java:424 等 | — |
| `hrpi_person` | BounsImportBillPlugin.java:98, TransferToHandleDisTask.java:43, BonusWriteBackUtil.java:266 | — |
| `hrpi_personuserrel` | PersonOrgUtil.java:17 | — |
| `hsas_payrollgrp` | BounsImportBillPlugin.java:106, BounsImportBillPlugin.java:118, BounsRecodBillPlugin.java:83 | — |
| `hsas_salaryfile` | PayrollPushUtil.java:57 | — |
| `hsbs_calperiodtype` | BounsImportBillPlugin.java:110, BounsRecodBillPlugin.java:87 | — |
| `hspm_ermanfile` | BonusUtil.java:214, BonusUtil.java:228, BounsPaymentUtils.java:201 等 | — |
| `tdkw_bonus_approval_2nd` | BonusDistributeSumOperatePlugin.java:217, CreateApproval.java:184, BounsImportBillPlugin.java:82 等 | — |
| `tdkw_bonus_approval_flow` | BonusApprovalSheetBillListPlugin.java:70, BonusApprovalSheetBillListPlugin.java:164, BonusApprovalSheetBillListPlugin.java:177 等 | — |
| `tdkw_bonus_dis_detail` | BonusDistributeSumOperatePlugin.java:303, BonusDisRejectPlugin.java:156, BonusDisRejectPlugin.java:205 等 | — |
| `tdkw_bonus_distribute_sum` | BonusDisSumAllPlugin.java:63, BonusDisListPlugin.java:78, BonusDisRejectPlugin.java:382 等 | — |
| `tdkw_bonus_items` | BonusPlanImportPlugin.java:52 | — |
| `tdkw_bonus_payment` | BonusSumApprovalOperatePlugin.java:206, BonusPaymentImportPlugin.java:56, BounsPaymentUtils.java:364 等 | — |
| `tdkw_bonus_plan` | CreateApproval.java:165, CreateApproval.java:197, FilterItemByType.java:289 等 | — |
| `tdkw_bonus_plan_approval` | CreateApproval.java:78, CreateApproval.java:140, CreateApproval.java:211 等 | — |
| `tdkw_bonus_sum_approval` | BonusDistributeBillPlugin.java:125, BonusDistributeSumListPlugin.java:80, BonusDistributeSumListPlugin.java:227 | — |
| `tdkw_bouns_agent_approve` | BounsPaymentUtils.java:52, BounsPaymentUtils.java:118, BounsPaymentUtils.java:152 | — |
| `tdkw_bouns_end_approval` | BounsPaymentUtils.java:67, BounsPaymentUtils.java:136, BounsPaymentUtils.java:168 等 | — |
| `tdkw_bouns_record` | BonusPaymentBillPlugin.java:48, BonusPaymentBillPlugin.java:56, BounsPaymentUtils.java:82 等 | — |
| `tdkw_department_funds` | BonusSumApprovalOperatePlugin.java:333, BonusApprovalSheetBillSecondPushPlugin.java:931 | — |
| `tdkw_urgeing` | BonusDistributeSumListPlugin.java:135 | — |

## 边界（脑补防火墙）

## G · 关键否定型事实（脑补防火墙）

⚡ **待精修**：人工补"代码里没看到的功能"·防 LLM 脑补：

- ❌ "<待人工补>·如：'本资产没用调度任务'"
- ❌ "<待人工补>·如：'本资产没发 BEC 跨云事件'"

## SDK 白名单合规

详见 [`deep_scan_forbidden_check.md`](../../../<skills-dir>/cosmic-hr-expert/dcs_regression/passed/case_013_swc_bonus_new/deep_scan_forbidden_check.md)·走苍穹白名单基类·无内部 API 调用。

## 关联资产 / 模板

- 资产复刻：[`_assets/`](../../_assets/)
- Phase D 真扫：[`dcs_regression/`](../../../dcs_regression/)
