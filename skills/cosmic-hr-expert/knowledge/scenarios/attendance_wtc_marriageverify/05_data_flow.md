# attendance_wtc_marriageverify · 数据流

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
