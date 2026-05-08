# attendance_wtc_marriageverify · 业务规则

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
| `billno` | BatchMarriageDateValidator.java:64 | — |
| `employee` | MobilleMarriageVerifyPlugin.java:37, BatchMarriageDateValidator.java:65 | — |
| `empnumber` | BatchMarriageDateValidator.java:67 | — |
| `enddate` | MarriageVerifyPlugin.java:160 | — |
| `enddateshowstr` | BatchMarriageDateValidator.java:78 | — |
| `entryenddate` | MobilleMarriageVerifyPlugin.java:80, MarriageDateValidator.java:77 | — |
| `entryentity` | MobilleMarriageVerifyPlugin.java:33, MobilleMarriageVerifyPlugin.java:77, BatchMarriageDateValidator.java:31 等 5 处 | — |
| `entrystartdate` | MobilleMarriageVerifyPlugin.java:79, MarriageDateValidator.java:76 | — |
| `entryvacationtype` | MobilleMarriageVerifyPlugin.java:35 | — |
| `fbasedataid` | BatchMarriageDateValidator.java:39 | — |
| `id` | MarriageVerifyPlugin.java:43, MarriageVerifyPlugin.java:61, MarriageVerifyPlugin.java:108 等 7 处 | — |
| `marriageregistdate` | BatchMarriageDateValidator.java:58, BatchMarriageDateValidator.java:59, MarriageDateValidator.java:57 等 5 处 | — |
| `name` | BatchMarriageDateValidator.java:66 | — |
| `number` | MarriageVerifyPlugin.java:41, MarriageVerifyPlugin.java:58, MarriageVerifyPlugin.java:106 等 6 处 | — |
| `startdate` | MarriageVerifyPlugin.java:159 | — |
| `startdateshowstr` | BatchMarriageDateValidator.java:77 | — |
| `vacationtype` | MarriageVerifyPlugin.java:40, MarriageVerifyPlugin.java:57, MarriageVerifyPlugin.java:105 等 4 处 | — |
| `vacationtypelist` | BatchMarriageDateValidator.java:37 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `id` | `QCP.equals` | MarriageDateDealUtil.java:40 |
| `id` | `QCP.in` | MarriageDateDealUtil.java:58 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
