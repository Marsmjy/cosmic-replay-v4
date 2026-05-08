# core_hr_emp_super_rel_chart · 业务规则

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

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `businessstatus` | `QCP.equals` | EmpSuprelChartPlugin.java:676, EmpSuprelChartPlugin.java:687, EmpSuprelChartPlugin.java:709 |
| `datastatus` | `QCP.equals` | EmpSuprelChartPlugin.java:237, EmpSuprelChartPlugin.java:743 |
| `employee.id` | `QCP.equals` | EmpSuprelChartPlugin.java:691 |
| `employee` | `QCP.in` | EmpSuprelChartPlugin.java:706, EmpSuprelChartPlugin.java:741 |
| `enable` | `QCP.equals` | EmpSuprelChartPlugin.java:257 |
| `id` | `QCP.equals` | EmpSuprelChartPlugin.java:239 |
| `id` | `QCP.in` | EmpSuprelChartPlugin.java:724 |
| `iscurrentversion` | `QCP.equals` | EmpSuprelChartPlugin.java:238, EmpSuprelChartPlugin.java:678, EmpSuprelChartPlugin.java:689 |
| `isprimary` | `QCP.equals` | EmpSuprelChartPlugin.java:707 |
| `number` | `QCP.equals` | EmpSuprelChartPlugin.java:258 |
| `reporttype.number` | `QCP.in` | EmpSuprelChartPlugin.java:402 |
| `status` | `QCP.equals` | EmpSuprelChartPlugin.java:256 |
| `superiorempposorgrel.employee.id` | `QCP.equals` | EmpSuprelChartPlugin.java:680 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
