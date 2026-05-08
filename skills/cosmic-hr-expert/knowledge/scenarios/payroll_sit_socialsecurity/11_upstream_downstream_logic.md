# payroll_sit_socialsecurity · 上下游逻辑

> **协议**：ADR-009 跨云穿透架构
> **数据源**：deep_scan_audit.md A/E 段（form 清单 / QFilter 过滤）

## 上游（本场景的数据源）

### 直接上游（来自真扫的 form 引用）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `bos_org` | SocialSecurityPersonFilterFormPlugin.java:56, PersonOrgUtil.java:18, PersonOrgUtil.java:33 | — |
| `haos_adminorghr` | PersonOrgUtil.java:22, PersonOrgUtil.java:28 | — |
| `hcsi_sibase` | SinsurfileHelper.java:187, SinsurfileHelper.java:254, SinsurfileHelper.java:310 等 | — |
| `hcsi_sinsurfile` | SocialSecurityPaybillListPlugin.java:58, SocialSecurityPayClickNumberPlugin.java:44, SocialSecurityPayFormPlugin.java:475 等 | — |
| `hrpi_empjobrel` | SocialSecurityPayFormPlugin.java:700, SocialSecurityPersonFilterFormPlugin.java:170 | — |
| `hrpi_employee` | SocialSecurityPayFormPlugin.java:715 | — |
| `hrpi_empposorgrel` | SocialSecurityPayFormPlugin.java:78, SocialSecurityPayFormPlugin.java:644 | — |
| `hrpi_percre` | SocialSecurityPayFormPlugin.java:659 | — |
| `hsbs_empentrel` | SocialSecurityPayFormPlugin.java:737 | — |
| `sitbs_insuranceprop` | SinsurfileHelper.java:548 | — |
| `sitbs_sinsurstd` | SocialSecurityPayValidator.java:63 | — |
| `sitbs_welfaretype` | SinsurfileHelper.java:533 | — |
| `tdkw_payment_service_type` | SocialSecurityPaybillListPlugin.java:76 | — |
| `tdkw_social_security` | SocialSecurityPaybillListPlugin.java:67, SocialSecurityPaybillListPlugin.java:149, SocialSecurityPayFormPlugin.java:535 | — |
| `tdkw_social_security_fund` | SocialSecurityPersonFilterFormPlugin.java:178 | — |

### 查询过滤上下文

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `billstatus` | `QCP.in` | SocialSecurityPayFormPlugin.java:529 |
| `createorg.number` | `QCP.equals` | SocialSecurityFundCommon.java:49 |
| `datastatus` | `QCP.equals` | SocialSecurityFundCommon.java:47, SocialSecurityPayFormPlugin.java:549, SocialSecurityPayFormPlugin.java:668 |
| `employee.empnumber` | `QCP.equals` | SocialSecurityPayFormPlugin.java:546 |
| `employee.number` | `QCP.in` | SocialSecurityPayOperationPlugin.java:110 |
| `employee` | `QCP.equals` | SocialSecurityPayFormPlugin.java:74, SocialSecurityPayFormPlugin.java:641, SocialSecurityPayFormPlugin.java:658 |
| `empstagelatestrecord` | `QCP.equals` | SocialSecurityPayFormPlugin.java:735 |
| `empstage` | `QCP.equals` | SocialSecurityPayFormPlugin.java:736 |
| `enable` | `QCP.equals` | SocialSecurityFundCommon.java:46, SocialSecurityPayOperationPlugin.java:112, PersonOrgUtil.java:32 |
| `enddate` | `QCP.large_equals` | SocialSecurityPayFormPlugin.java:75, SocialSecurityPayFormPlugin.java:642, SocialSecurityPersonFilterFormPlugin.java:168 |
| `id` | `QCP.equals` | SocialSecurityPayFormPlugin.java:473, SocialSecurityPayFormPlugin.java:712, SocialSecurityPersonFilterFormPlugin.java:177 |
| `id` | `QCP.in` | SocialSecurityPaybillListPlugin.java:66, SocialSecurityPayValidator.java:65 |
| `id` | `QCP.not_equals` | SocialSecurityPayFormPlugin.java:531 |
| `id` | `QCP.not_in` | SocialSecurityFundFormPlugin.java:113 |
| `insured` | `QCP.equals` | SinsurfileHelper.java:186, SinsurfileHelper.java:253, SinsurfileHelper.java:309 |
| `iscurrentdata` | `QCP.equals` | SocialSecurityPayFormPlugin.java:76, SocialSecurityPayFormPlugin.java:699, SocialSecurityPersonFilterFormPlugin.java:169 |
| `iscurrentversion` | `QCP.equals` | SocialSecurityFundCommon.java:48, SocialSecurityPayFormPlugin.java:548, SocialSecurityPayFormPlugin.java:667 |
| `isescrowstaff` | `QCP.equals` | SocialSecurityPaybillListPlugin.java:56, SocialSecurityPayFormPlugin.java:547, SocialSecurityPayOperationPlugin.java:113 |
| `isprimary` | `QCP.equals` | SocialSecurityPayFormPlugin.java:77, SocialSecurityPayFormPlugin.java:643 |
| `number` | `QCP.equals` | SocialSecurityPersonFilterFormPlugin.java:56, PersonOrgUtil.java:20, PersonOrgUtil.java:30 |
| `number` | `QCP.in` | SocialSecurityPaybillListPlugin.java:75 |
| `org` | `QCP.equals` | SocialSecurityPersonFilterFormPlugin.java:74 |
| `sinsurfile` | `QCP.equals` | SinsurfileHelper.java:185, SinsurfileHelper.java:252, SinsurfileHelper.java:308 |
| `sinsurfile` | `QCP.in` | SocialSecurityPayFormPlugin.java:560 |
| `sinsurstatus` | `QCP.equals` | SocialSecurityPayFormPlugin.java:474, SocialSecurityPayFormPlugin.java:545 |

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
