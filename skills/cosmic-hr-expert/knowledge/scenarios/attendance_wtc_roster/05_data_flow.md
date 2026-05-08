# attendance_wtc_roster · 数据流

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
| `attfile_id` | BusiTripBillSubmitPlugin.java:25 | — |
| `billno` | BatchBusiTripBillSubmitPlugin.java:66 | — |
| `employee` | BatchBusiTripBillSubmitPlugin.java:67 | — |
| `empnumber` | BatchBusiTripBillSubmitPlugin.java:69 | — |
| `entryentity` | BatchBusiTripBillSubmitPlugin.java:32, BatchBusiTripBillSubmitPlugin.java:51 | — |
| `fileboid` | RosterValidatorStorage.java:57 | — |
| `id` | RosterValidatorStorage.java:28 | — |
| `key` | CommonPropertiesQueryUtil.java:36, CommonPropertiesQueryUtil.java:52 | — |
| `name` | BatchBusiTripBillSubmitPlugin.java:68 | — |
| `number` | CommonPropertiesQueryUtil.java:35, CommonPropertiesQueryUtil.java:49 | — |
| `rosterdate` | RosterValidatorStorage.java:62 | — |
| `startdateshowstr` | BatchBusiTripBillSubmitPlugin.java:63 | — |
| `storageto` | BatchBusiTripBillSubmitPlugin.java:62, BusiTripBillSubmitPlugin.java:30, RosterValidatorStorage.java:57 | — |
| `value` | CommonPropertiesQueryUtil.java:36, CommonPropertiesQueryUtil.java:53 | — |

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
