# org_dev_adminorgbill_extension · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_chgsourcevid` | OrgBatchBillSubmitDealSourceInfoOp.java:51 | `vId` | — |

## R-02 · 字段读取规则（核心业务读）

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `adminorgfunction` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:60, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:150, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:150 等 11 处 | — |
| `adminorglayer` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:58, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:143, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:143 等 11 处 | — |
| `adminorgtype` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:50, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:115, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:115 等 14 处 | — |
| `belongcompany` | AdminOrgShowMobBillPlugin.java:337, AdminOrgShowMobBillPlugin.java:337, AdminOrgShowMobBillPlugin.java:515 等 6 处 | — |
| `changedescription` | AdminOrgShowMobBillPlugin.java:140, AdminOrgShowMobBillPlugin.java:140, AdminOrgShowMobBillPlugin.java:182 等 8 处 | — |
| `city` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:64, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:164, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:164 等 11 处 | — |
| `companyarea` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:62, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:157, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:157 等 11 处 | — |
| `corporateorg` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:56, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:136, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:136 等 11 处 | — |
| `description` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:70, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:70, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:185 等 16 处 | — |
| `detailaddress` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:68, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:68, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:178 等 16 处 | — |
| `establishmentdate` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:54, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:54, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:129 等 22 处 | — |
| `id` | AdminOrgShowMobBillPlugin.java:56, AdminOrgShowMobBillPlugin.java:165, AdminOrgShowMobBillPlugin.java:214 等 5 处 | — |
| `index` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:72, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:72, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:192 等 16 处 | — |
| `industrytype` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:52, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:122, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:122 等 11 处 | — |
| `mainduty` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:76, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:76, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:206 等 16 处 | — |
| `name` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:42, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:42, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:89 等 28 处 | — |
| `number` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:44, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:44, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:95 等 20 处 | — |
| `parentorg` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:46, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:101, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:101 等 11 处 | — |
| `positioning` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:74, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:74, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:199 等 16 处 | — |
| `simplename` | AdminOrgBatchDetailDealSourceInfoFormPlugin.java:48, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:48, AdminOrgBatchDetailDealSourceInfoFormPlugin.java:108 等 8 处 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `billid` | `QCP.in` | OrgBatchBillSubmitDealSourceInfoOp.java:36 |
| `id` | `QCP.equals` | OrgBatchBillInfoHelper.java:37 |
| `id` | `QCP.in` | OrgBatchBillInfoHelper.java:23, OrgBatchBillInfoHelper.java:30 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
