# attendance_wtc_roster · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|

## R-02 · 字段读取规则（核心业务读）

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

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `fileboid` | `QCP.equals` | ArchivalInformationServiceImpl.java:16 |
| `fileboid` | `QCP.in` | RosterValidatorStorage.java:52 |
| `number` | `QCP.in` | CommonPropertiesQueryUtil.java:60 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
