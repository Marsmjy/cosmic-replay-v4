# payroll_beforecomputationcheck · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_latestpushtime` | CalPayPushPlugin.java:119 | `new Date(` | — |
| `${ISV_FLAG}_pushstatus` | CalPayPushPlugin.java:118 | `PUSHED` | — |

## 数据读取流向

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `billname` | CalPayPushPlugin.java:85, CalPayPushPlugin.java:112 | — |
| `billno` | CalPayPushPlugin.java:84, CalPayPushPlugin.java:113 | — |
| `bizdatabillid` | HsasCalpayRolltListPlugin.java:90 | — |
| `id` | CalPayPushPlugin.java:87 | — |
| `period` | HsasCalpayRolltListPlugin.java:57 | — |

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
