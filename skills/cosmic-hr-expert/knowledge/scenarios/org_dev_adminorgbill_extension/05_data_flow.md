# org_dev_adminorgbill_extension · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_chgsourcevid` | OrgBatchBillSubmitDealSourceInfoOp.java:51 | `vId` | — |

## 数据读取流向

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
