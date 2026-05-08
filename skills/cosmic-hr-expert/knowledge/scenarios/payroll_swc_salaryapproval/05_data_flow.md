# payroll_swc_salaryapproval · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|

## 数据读取流向

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `approvebilltplv` | PayrollApprovalMobileEditPlugin.java:51 | — |
| `billstatus` | PayrollApprovalMobileEditPlugin.java:98 | — |
| `caltasks` | PayrollApprovalMobileEditPlugin.java:125 | — |
| `fbasedataid_id` | PayrollApprovalMobileEditPlugin.java:131 | — |
| `isenable` | PayrollApprovalMobileEditPlugin.java:67 | — |
| `name` | PayrollApprovalEditPlugin.java:44 | — |
| `overviewentryentity` | PayrollApprovalMobileEditPlugin.java:49, PayrollApprovalMobileEditPlugin.java:59 | — |

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
