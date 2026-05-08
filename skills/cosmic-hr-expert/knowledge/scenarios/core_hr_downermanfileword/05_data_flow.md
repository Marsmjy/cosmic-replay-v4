# core_hr_downermanfileword · 数据流

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
