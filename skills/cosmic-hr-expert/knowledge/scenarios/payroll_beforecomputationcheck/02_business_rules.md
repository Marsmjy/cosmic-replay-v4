# payroll_beforecomputationcheck · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `${ISV_FLAG}_latestpushtime` | CalPayPushPlugin.java:119 | `new Date(` | — |
| `${ISV_FLAG}_pushstatus` | CalPayPushPlugin.java:118 | `PUSHED` | — |

## R-02 · 字段读取规则（核心业务读）

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `billname` | CalPayPushPlugin.java:85, CalPayPushPlugin.java:112 | — |
| `billno` | CalPayPushPlugin.java:84, CalPayPushPlugin.java:113 | — |
| `bizdatabillid` | HsasCalpayRolltListPlugin.java:90 | — |
| `id` | CalPayPushPlugin.java:87 | — |
| `period` | HsasCalpayRolltListPlugin.java:57 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `billstatus` | `QCP.in` | HsasCalpayRolltListPlugin.java:81 |
| `bizdatabillid` | `QCP.in` | HsasCalpayRolltListPlugin.java:86 |
| `calperiod` | `QCP.equals` | HsasCalpayRolltListPlugin.java:81 |
| `empposorgrel.assignment` | `QCP.in` | HsasCalpayRolltListPlugin.java:86 |
| `id` | `QCP.in` | CalPayPushPlugin.java:46, CalPayPushPlugin.java:59, CalPayPushPlugin.java:76 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
