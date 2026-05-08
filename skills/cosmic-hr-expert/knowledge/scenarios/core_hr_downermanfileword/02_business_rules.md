# core_hr_downermanfileword · 业务规则

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
| `addressinfo` | FileConversionImpl.java:1306, FileConversionImpl.java:1309, FileConversionImpl.java:1312 | — |
| `adminorg` | FileConversionImpl.java:227, FileConversionImpl.java:321 | — |
| `admissiondate` | FileConversionImpl.java:541 | — |
| `apositiontype` | PersonFileDownloadFormPlugin.java:124 | — |
| `basedatafield` | FileConversionImpl.java:1100 | — |
| `birthplace` | FileConversionImpl.java:998, FileConversionImpl.java:999 | — |
| `businessstatus` | FileConversionImpl.java:608 | — |
| `classify` | CustomPersonModelUtil.java:109 | — |
| `company` | FileConversionImpl.java:226, FileConversionImpl.java:323 | — |
| `content` | FileConversionImpl.java:700 | — |
| `department` | FileConversionImpl.java:355 | — |
| `empnumber` | CustomFileHeadHelper.java:54 | — |
| `emrgphone` | FileConversionImpl.java:503 | — |
| `enddate` | FileConversionImpl.java:217, FileConversionImpl.java:248, FileConversionImpl.java:249 等 8 处 | — |
| `entity` | CustomPersonModelUtil.java:110 | — |
| `familymembship` | FileConversionImpl.java:420, FileConversionImpl.java:428, FileConversionImpl.java:435 | — |
| `fbasedataid` | CustomValueConvertHelper.java:26, FileConversionImpl.java:1234 | — |
| `ffileid` | FileConversionImpl.java:75, FileConversionImpl.java:737 | — |
| `flag` | FileConversionImpl.java:707, FileConversionImpl.java:1053 | — |
| `gradutiondate` | FileConversionImpl.java:543 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `assignment.id` | `QCP.equals` | FileConversionImpl.java:1339 |
| `contractstatus` | `QCP.equals` | FileConversionImpl.java:1405 |
| `employee.id` | `QCP.equals` | FileConversionImpl.java:1327 |
| `id` | `QCP.in` | ErmanFileReusePlugin.java:46, PersonFileDownloadFormPlugin.java:228, PersonFileDownloadRptPlugin.java:62 |
| `iscurrentdata` | `QCP.equals` | FileConversionImpl.java:1340, FileConversionImpl.java:1356 |
| `status` | `QCP.equals` | ErmanFileReusePlugin.java:98 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
