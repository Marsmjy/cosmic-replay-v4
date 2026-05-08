# payroll_swc_bonus_new · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_action_org` | BonusPlanConvertPlugin.java:117 | `ermanfile.getDynamicObject("company"` | — |
| `${ISV_FLAG}_admin_dept` | BonusDisSecondPushPlugin.java:435 等 2 处 | `entry.getDynamicObject("tdkw_admin_dept"` | — |
| `${ISV_FLAG}_admin_org_keep` | BonusSumApprovalOperatePlugin.java:371 等 6 处 | `dept` | — |
| `${ISV_FLAG}_admin_post` | BonusDisSecondPushPlugin.java:436 等 2 处 | `entry.getDynamicObject("tdkw_admin_post"` | — |
| `${ISV_FLAG}_agent_per_count` | BounsAgentConvertPlugin.java:56 | `entryEntity.size(` | — |
| `${ISV_FLAG}_already_dis_count` | BonusDistributeSumOperatePlugin.java:336 等 2 处 | `alreadyDistributePersonCount` | — |
| `${ISV_FLAG}_already_dis_count_to` | BonusApprovalSheetBillSecondPushPlugin.java:786 | `personDistributeDetailTotal` | — |
| `${ISV_FLAG}_already_dis_decimal` | BonusDistributeSumOperatePlugin.java:277 | `disObject.getBigDecimal("tdkw_already_di` | — |
| `${ISV_FLAG}_already_dis_per_tl` | BonusDistributeSumOperatePlugin.java:337 | `alreadyDistributePersonTotal` | — |
| `${ISV_FLAG}_already_dis_total` | BonusDistributeSumOperatePlugin.java:335 等 2 处 | `alreadyDistributeTotal` | — |
| `${ISV_FLAG}_amount` | BonusSumApprovalOperatePlugin.java:278 等 3 处 | `disTotalPerson` | — |
| `${ISV_FLAG}_ap_company` | BonusDisSecondPushPlugin.java:343 等 3 处 | `bill.get("tdkw_ap_company"` | — |
| `${ISV_FLAG}_ap_creator` | BonusDisSecondPushPlugin.java:342 等 3 处 | `bill.get("tdkw_ap_creator"` | — |
| `${ISV_FLAG}_ap_dept` | BonusDisSecondPushPlugin.java:344 等 3 处 | `bill.get("tdkw_ap_dept"` | — |
| `${ISV_FLAG}_ap_post` | BonusDisSecondPushPlugin.java:345 等 3 处 | `bill.get("tdkw_ap_post"` | — |
| `${ISV_FLAG}_app_total_amount` | BonusWriteBackUtil.java:53 | `checkAmount` | — |
| `${ISV_FLAG}_approval` | BonusDisSecondPushPlugin.java:279 等 2 处 | `bill.getDynamicObject("tdkw_approval"` | — |
| `${ISV_FLAG}_approval_all` | BonusApprovalConvertPlugin.java:105 | `totalBudget` | — |
| `${ISV_FLAG}_approval_bill` | BonusApprovalSheetBillSecondPushPlugin.java:760 | `bill` | — |
| `${ISV_FLAG}_approval_bill_id` | BonusApprovalSheetBillSecondPushPlugin.java:759 | `bill.getLong("id"` | — |

## 数据读取流向

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `adminorg` | BonusDistributeSumOperatePlugin.java:260, BonusPlanImportPlugin.java:73, BonusPlanApprovalBillPlugin.java:129 等 14 处 | — |
| `adminorgtype` | BonusDisSecondPushPlugin.java:310, BonusApprovalSheetBillSecondPlugin.java:625, BonusApprovalSheetBillSecondPushPlugin.java:321 | — |
| `auditstatus` | BonusApprovalSheetBillSecondPushPlugin.java:181 | — |
| `belongcompany` | FilterItemByType.java:253, BonusDisSecondPlugin.java:304, BonusApprovalSheetBillSecondPlugin.java:667 等 4 处 | — |
| `belongdept` | FilterItemByType.java:251, BonusApprovalSheetBillSecondPlugin.java:874 | — |
| `billno` | BonusDistributeSumOperatePlugin.java:118, BonusDistributeSumOperatePlugin.java:271, BonusDistributeSumOperatePlugin.java:355 等 31 处 | — |
| `billstatus` | BonusSumApprovalOperatePlugin.java:132, BonusDistributeSumValidatePlugin.java:60, BonusDistributeSumValidatePlugin.java:67 等 33 处 | — |
| `boid` | PayrollPushUtil.java:115, PayrollPushUtil.java:121 | — |
| `bsed` | PayrollPushUtil.java:83, PayrollPushUtil.java:84, PayrollPushUtil.java:96 等 4 处 | — |
| `bsled` | PayrollPushUtil.java:90, PayrollPushUtil.java:91, PayrollPushUtil.java:103 等 4 处 | — |
| `company` | BonusDisSumAllPlugin.java:207, BonusDistributeSumOperatePlugin.java:261, BonusPlanImportPlugin.java:74 等 20 处 | — |
| `createtime` | BonusSumApprovalOperatePlugin.java:252, BonusSumApprovalOperatePlugin.java:261, BounsRecordOperation.java:191 等 5 处 | — |
| `creator` | BounsRecordOperation.java:190 | — |
| `effdt` | BonusWriteBackUtil.java:207, BonusWriteBackUtil.java:207 | — |
| `employee` | PersonOrgUtil.java:19, BonusDisSecondPushPlugin.java:99, BonusDisSecondPushPlugin.java:190 等 6 处 | — |
| `empnumber` | BonusDisSecondPushPlugin.java:89, BonusDisSecondPushPlugin.java:99, BonusDisSecondPushPlugin.java:190 等 6 处 | — |
| `entryentity` | BounsPaymentUtils.java:241, BounsRecodBillPlugin.java:465, BounsRecordOperation.java:183 等 6 处 | — |
| `fbasedataid_id` | BonusPlanApprovalOperationPlugin.java:65 | — |
| `id` | BonusUtil.java:47, BonusDisSumAllPlugin.java:65, BonusDisSumAllPlugin.java:126 等 147 处 | — |
| `name` | BonusApprovalSecValidator.java:274 | — |

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
