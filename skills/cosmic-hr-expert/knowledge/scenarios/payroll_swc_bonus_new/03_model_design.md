# payroll_swc_bonus_new · 数据模型

> **数据源**：deep_scan_inventory.md + deep_scan_audit.md A/H 段

## form 实体清单

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

## 元数据归属（PR-001 ISV 隔离）

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 资产 inventory（dym/schdata/SQL）

## datamodel 元数据

| 类型 | 数量 | 文件清单 |
|---|---:|---|
| dym (form 表) | 17 | `bonus_approval_2nd`, `bonus_approval_flow`, `bonus_distribute_sum`, `bonus_dis_detail`, `bonus_items`, `bonus_payment`, `bonus_plan`, `bonus_plan_approval`, `bonus_sum_approval`, `bonus_types`... |
| schdata (调度) | 0 |  |
| SQL (预置数据) | 15 | — |
| cld/cldx (i18n) | 2 | — |

## 类继承层级（SDK 白名单）

## 继承链全貌

```

(无继承·工具/独立类)
  ↑   [178 LOC]
  ↑ PersonOrgUtil  [49 LOC]
  ↑ SWCI18NParam  [62 LOC]
  ↑ BonusUtil  [231 LOC]
  ↑ PushSalaryUtil  [58 LOC]
  ↑ TdkwSendMessageUtil  [108 LOC]
  ↑ OperateDistributeCacheUtil  [40 LOC]
  ↑ BounsPaymentUtils  [586 LOC]
  ↑ PayrollPushUtil  [130 LOC]
  ↑ BonusWriteBackUtil  [330 LOC]
  ↑ CalendarUtil  [57 LOC]
  ↑   [72 LOC]
  ↑ ObjectUtils  [32 LOC]
  ↑ PersonOrgUtil  [35 LOC]

AbstractBillPlugIn
  ↑ BonusDisSumAllPlugin  [238 LOC implements BeforeF7SelectListener, RowClickEventListener]
  ↑ BonusDistributeBillPlugin  [258 LOC implements HyperLinkClickListener]
  ↑ BonusPaymentBillPlugin  [67 LOC implements CellClickListener]
  ↑ BonusPlanApprovalBillPlugin  [172 LOC implements BeforeF7SelectListener, CellClickListener]
  ↑ BounsAgentApproveBillPlugin  [38 LOC implements BeforeF7SelectListener]
  ↑ BounsImportBillPlugin  [306 LOC]
  ↑ BounsRecodBillPlugin  [602 LOC implements Plugin, BeforeF7SelectListener]
  ↑ BonusDisSecondPlugin  [728 LOC implements BeforeF7SelectListener]
  ↑ BonusApprovalSheetBillSecondFlowPlugin  [173 LOC implements BeforeF7SelectListener, HyperLinkClickListener]
  ↑ BonusApprovalSheetBillSecondPlugin  [971 LOC implements BeforeF7SelectListener]

AbstractOperationServicePlugIn
  ↑ BonusDistributeSumOperatePlugin  [387 LOC]
  ↑ BonusSumApprovalOperatePlugin  [419 LOC]
  ↑ BonusPlanApprovalOperationPlugin  [79 LOC]
  ↑ BounsRecordOperation  [263 LOC]
  ↑ BounsImportOperation  [214 LOC]
  ↑ BounsEndApproveOperation  [91 LOC]
  ↑ BounsAgentApproveOperation  [66 LOC]
  ↑ BonusPlanOperation  [92 LOC]
  ↑ BonusPlanOtherOperation  [98 LOC]
  ↑ BounsPaymentAgentOperation  [156 LOC]
  ↑ BonusDisSecondPushPlugin  [461 LOC]
  ↑ BonusApprovalAuditPlugin  [131 LOC implements Plugin]
  ↑ BonusApprovalSheetBillSecondPushPlugin  [1014 LOC]

AbstractValidator
  ↑ BonusDistributeSumValidatePlugin  [92 LOC]
  ↑ BonusPlanValidator  [147 LOC]
  ↑ BounsPaymentValidator  [42 LOC]
  ↑ BonusDisSecValidator  [119 LOC]
  ↑ BonusApprovalSecValidator  [321 LOC]

AbstractListPlugin
  ↑ BonusDistributeSumListPlugin  [298 LOC]
  ↑ BounsPaymentListPlugin  [281 LOC]
  ↑ CreateApproval  [233 LOC]
  ↑ BonusDisListPlugin  [158 LOC]
  ↑ BonusApprovalSheetBillListPlugin  [195 LOC]

BatchImportPlugin
  ↑ BonusPaymentImportPlugin  [140 LOC]
  ↑ BonusPlanImportPlugin  [168 LOC]

AbstractFormPlugin
  ↑ FilterItemByType  [327 LOC implements BeforeF7SelectListener]
  ↑ BonusDisRejectPlugin  [405 LOC]
  ↑ TransferToHandleDisTask  [52 LOC]

AbstractConvertPlugIn
  ↑ BonusPlanConvertPlugin  [130 LOC implements Plugin]
  ↑ BounsAgentConvertPlugin  [65 LOC implements Plugin]
  ↑ BounsEndConvertPlugin  [65 LOC implements Plugin]
  ↑ BounsRecordConvertPlugin  [43 LOC implements Plugin]
  ↑ BonusApprovalConvertPlugin  [155 LOC implements Plugin]
```

## 关联

- 类继承细节：deep_scan_class_tree.md
