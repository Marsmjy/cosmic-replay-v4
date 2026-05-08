# payroll_swc_bonus_new · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

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

## R-02 · 字段读取规则（核心业务读）

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

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

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

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：
- `auditstatus` 可能取值: A, K
- `billstatus` 可能取值: A, F, H, K
- `${ISV_FLAG}_approval_status` 可能取值: A, K, null
- `${ISV_FLAG}_is_agent` 可能取值: 0, 1
- `${ISV_FLAG}_push_status` 可能取值: B, C, D, E, F
- `${ISV_FLAG}_source_bill_type` 可能取值: A, B

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
