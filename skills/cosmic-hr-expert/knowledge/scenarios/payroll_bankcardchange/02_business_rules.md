# payroll_bankcardchange · 业务规则

> **数据源**：deep_scan_audit.md B/C/D/E/F 段
> **日期**：2026-05-06

## R-01 · 字段写入规则（核心业务行为）

## B · 字段写入操作（set 模式）

⚡ **待精修**：补每个字段的业务含义 / 状态机说明。

| 字段 | 写入位置 | 写值类型 | 业务含义 |
|---|---|---|---|
| `kdcd_acctmodifybill` | AcctModifyBankByPayDetailOperatePlugin.java:69 | `old !=null ? 0L : old.getDynamicObject("` | — |
| `${ISV_FLAG}_account_name` | ChangeBankCardBillPlugin.java:74 等 2 处 | `beforebankcard.getString("username"` | — |
| `${ISV_FLAG}_accountrelation` | ChangeBankCardBillPlugin.java:75 等 2 处 | `beforebankcard.getDynamicObject("account` | — |
| `${ISV_FLAG}_bank_card` | ChangeBankCardBillPlugin.java:72 | `beforebankcard.getString("bankcardnum"` | — |
| `${ISV_FLAG}_bank_name` | ChangeBankCardBillPlugin.java:73 等 2 处 | `beforebankcard.getDynamicObject("bankdep` | — |
| `${ISV_FLAG}_cardpurpose` | ChangeBankCardBillPlugin.java:79 等 2 处 | `collection` | — |
| `${ISV_FLAG}_is_mobile` | ChangeBankCardBillPlugin.java:33 | `"true"` | — |
| `${ISV_FLAG}_person_name` | ChangeBankCardBillPlugin.java:41 | `"变更人员："+employee.getString("name"` | — |

## R-02 · 字段读取规则（核心业务读）

## C · 字段读取操作（get 模式）

| 字段 | 读取位置（前 3 处） | 业务含义|
|---|---|---|
| `accountrelation` | ChangeBankCardBillPlugin.java:75 | — |
| `acctmodifybill` | AcctModifyBankByPayDetailOperatePlugin.java:69 | — |
| `bankcardnum` | ChangeBankCardBillPlugin.java:72, ChangeBankCardBillPlugin.java:123, ChangeBankCardBillPlugin.java:123 | — |
| `bankdeposit` | ChangeBankCardBillPlugin.java:73 | — |
| `billstatus` | ChangeBankCardBillPlugin.java:114 | — |
| `boid` | ChangeBankCardOperationPlugin.java:153, ChangeBankCardOperationPlugin.java:213 | — |
| `cardpurpose` | ChangeBankCardOperationPlugin.java:178 | — |
| `employee` | ChangeBankCardBillPlugin.java:38, ChangeBankCardBillPlugin.java:85, ChangeBankCardOperationPlugin.java:63 | — |
| `entryentity` | ChangeBankCardBillPlugin.java:29, ChangeBankCardBillPlugin.java:115, ChangeBankCardOperationPlugin.java:33 等 6 处 | — |
| `fbasedataid` | ChangeBankCardOperationPlugin.java:164, ChangeBankCardOperationPlugin.java:180 | — |
| `fbasedataid_id` | ChangeBankCardOperationPlugin.java:132 | — |
| `id` | ChangeBankCardOperationPlugin.java:73, ChangeBankCardOperationPlugin.java:111, ChangeBankCardOperationPlugin.java:155 等 14 处 | — |
| `modifyperbankcard` | ChangeBankCardBillPlugin.java:46, ChangeBankCardOperationPlugin.java:39, ChangeBankCardOperationPlugin.java:136 | — |
| `name` | ChangeBankCardBillPlugin.java:41, ChangeBankCardBillPlugin.java:86, ChangeBankCardOperationPlugin.java:87 | — |
| `number` | ChangeBankCardOperationPlugin.java:64, ChangeBankCardOperationPlugin.java:148, ChangeBankCardOperationPlugin.java:165 等 4 处 | — |
| `perbankcard` | ChangeBankCardBillPlugin.java:44, ChangeBankCardBillPlugin.java:119, ChangeBankCardOperationPlugin.java:42 等 4 处 | — |
| `salaryfilehis` | ChangeBankCardBillPlugin.java:36, ChangeBankCardBillPlugin.java:84, ChangeBankCardOperationPlugin.java:62 | — |
| `${ISV_FLAG}_account_name` | ChangeBankCardBillPlugin.java:127, ChangeBankCardOperationPlugin.java:81, ChangeBankCardOperationPlugin.java:125 | — |
| `${ISV_FLAG}_accountrelation` | ChangeBankCardOperationPlugin.java:82, ChangeBankCardOperationPlugin.java:126 | — |
| `${ISV_FLAG}_bank_card` | ChangeBankCardBillPlugin.java:128, ChangeBankCardOperationPlugin.java:79, ChangeBankCardOperationPlugin.java:123 | — |

## R-03 · 平台 op 调用（标品 op 复用）

## D · 平台 op 调用（标品 op 复用）

| op | form | 调用位置 | 业务含义 |
|---|---|---|---|

## R-04 · 查询过滤规则（QFilter 复合条件）

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `boid` | `QCP.equals` | ChangeBankCardOperationPlugin.java:104, ChangeBankCardOperationPlugin.java:220 |
| `datastatus` | `QCP.equals` | ChangeBankCardOperationPlugin.java:69, ChangeBankCardOperationPlugin.java:75 |
| `id` | `QCP.in` | AcctModifyBankByPayDetailOperatePlugin.java:35, AcctModifyBankByPayDetailOperatePlugin.java:49 |
| `iscurrentversion` | `QCP.equals` | ChangeBankCardOperationPlugin.java:68, ChangeBankCardOperationPlugin.java:74 |
| `number` | `QCP.equals` | ChangeBankCardBillPlugin.java:89, ChangeBankCardBillPlugin.java:94, ChangeBankCardOperationPlugin.java:67 |
| `person` | `QCP.equals` | ChangeBankCardOperationPlugin.java:73, ChangeBankCardOperationPlugin.java:169 |

## R-05 · 业务规则推断（待业务释义确认）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：

## 关联

- 真扫源：deep_scan_audit.md（与本场景同根的 case 目录下）
