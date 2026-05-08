# payroll_swc_bonus_new · 上下游逻辑

> **协议**：ADR-009 跨云穿透架构
> **数据源**：deep_scan_audit.md A/E 段（form 清单 / QFilter 过滤）

## 上游（本场景的数据源）

### 直接上游（来自真扫的 form 引用）

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

### 查询过滤上下文

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `adminorg.id` | `QCP.equals` | BonusWriteBackUtil.java:219 |
| `adminorg.id` | `QCP.in` | BonusDisSumAllPlugin.java:126, FilterItemByType.java:86, BonusPlanApprovalBillPlugin.java:95 |
| `adminorgtype.number` | `QCP.equals` | BounsAgentApproveBillPlugin.java:32, BonusDisSecondPlugin.java:93 |
| `adminorgtype.number` | `QCP.in` | FilterItemByType.java:67, BonusDisSecondPlugin.java:120 |
| `adminorgtype.number` | `QCP.not_in` | BonusDisSecondPlugin.java:112 |
| `billno` | `QCP.equals` | BonusDisSumAllPlugin.java:64, BonusDistributeSumOperatePlugin.java:219, BonusPaymentBillPlugin.java:48 |
| `billno` | `QCP.in` | BonusDistributeSumOperatePlugin.java:302, BounsImportBillPlugin.java:82, BonusDisListPlugin.java:76 |
| `billstatus` | `QCP.equals` | BonusDistributeSumListPlugin.java:256, CreateApproval.java:139, BonusApprovalSheetBillSecondPlugin.java:697 |
| `billstatus` | `QCP.in` | BonusDistributeSumListPlugin.java:79, BounsPaymentUtils.java:50, BounsPaymentUtils.java:65 |
| `datastatus` | `QCP.equals` | PersonOrgUtil.java:45, BonusWriteBackUtil.java:217, BonusWriteBackUtil.java:241 |
| `employee.empnumber` | `QCP.equals` | BonusDisSumAllPlugin.java:227, BonusDistributeSumOperatePlugin.java:253, BounsPaymentUtils.java:422 |
| `employee.empnumber` | `QCP.in` | BonusDisSecondPushPlugin.java:94 |
| `employee.number` | `QCP.equals` | BonusDisSecondPlugin.java:103, BonusApprovalSheetBillSecondFlowPlugin.java:68 |
| `employee` | `QCP.equals` | BonusApprovalSheetBillSecondPlugin.java:830 |
| `empposrel.id` | `QCP.in` | BonusWriteBackUtil.java:282 |
| `empposrel.isprimary` | `QCP.equals` | BonusUtil.java:208, BonusUtil.java:222, BounsPaymentUtils.java:195 |
| `enable` | `QCP.equals` | PersonOrgUtil.java:46, PersonOrgUtil.java:32 |
| `id` | `QCP.equals` | PersonOrgUtil.java:32, BonusUtil.java:206, BonusDisSumAllPlugin.java:91 |
| `id` | `QCP.in` | BonusSumApprovalOperatePlugin.java:123, BonusSumApprovalOperatePlugin.java:146, BonusDistributeSumListPlugin.java:131 |
| `iscurrentversion` | `QCP.equals` | PersonOrgUtil.java:35, PersonOrgUtil.java:41, BonusWriteBackUtil.java:240 |
| `isdeleted` | `QCP.equals` | BonusDisSumAllPlugin.java:228, BonusDistributeSumOperatePlugin.java:254, BounsPaymentUtils.java:423 |
| `ismain` | `QCP.equals` | BonusWriteBackUtil.java:221 |
| `number` | `QCP.equals` | PersonOrgUtil.java:34, PersonOrgUtil.java:44, BonusUtil.java:43 |
| `number` | `QCP.in` | BounsImportBillPlugin.java:87, BounsImportBillPlugin.java:95, BounsImportBillPlugin.java:103 |
| `orgtype.number` | `QCP.equals` | BonusApprovalSheetBillSecondPlugin.java:215 |

## 下游（本场景的影响范围）

### 直接下游

补本资产写入哪些字段·影响哪些标品记录

### 跨云影响

补本资产是否触发跨云事件（薪酬/考勤/福利等）

## 跨云穿透矩阵

补具体跨云目标 / 触发链路 / 延迟 / 一致性

## ISV 扩展跨云联动建议

参考 W1 标杆模式（不直接发 BEC·靠标品自动派发）

## 关联资产

- 资产复刻：[`_assets/<ASSET_ID>/`](../../_assets/<ASSET_ID>/)
- 部署 SOP：[`_assets/<ASSET_ID>/deploy_sop.md`](../../_assets/<ASSET_ID>/deploy_sop.md)
- ADR-009：[../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md](../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md)
