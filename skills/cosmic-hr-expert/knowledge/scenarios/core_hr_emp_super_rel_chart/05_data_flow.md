# core_hr_emp_super_rel_chart · 数据流

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
| `currentLevel` | EmpSuprelChartPlugin.java:387 | — |
| `employee` | EmpSuprelChartPlugin.java:542 | — |
| `headsculpture` | EmpSuprelChartPlugin.java:474 | — |
| `id` | EmpSuprelChartPlugin.java:243, EmpSuprelChartPlugin.java:263, EmpSuprelChartPlugin.java:281 等 7 处 | — |
| `level` | EmpSuprelChartPlugin.java:225, EmpSuprelChartPlugin.java:403 | — |
| `name` | EmpSuprelChartPlugin.java:244, EmpSuprelChartPlugin.java:264, EmpSuprelChartPlugin.java:381 等 4 处 | — |
| `nodeId` | EmpSuprelChartPlugin.java:383 | — |
| `number` | EmpSuprelChartPlugin.java:245, EmpSuprelChartPlugin.java:265, EmpSuprelChartPlugin.java:473 | — |
| `parentId` | EmpSuprelChartPlugin.java:379 | — |
| `peremail` | EmpSuprelChartPlugin.java:481 | — |
| `personId` | EmpSuprelChartPlugin.java:218, EmpSuprelChartPlugin.java:229, EmpSuprelChartPlugin.java:320 | — |
| `phone` | EmpSuprelChartPlugin.java:480 | — |
| `reportReltypeNumber` | EmpSuprelChartPlugin.java:227, EmpSuprelChartPlugin.java:401 | — |
| `showDirection` | EmpSuprelChartPlugin.java:377 | — |

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
