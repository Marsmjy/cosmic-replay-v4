# payroll_bankcardchange · 数据流

> **数据源**：deep_scan_audit.md B/C/H 段

## 数据写入流向

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

## 数据读取流向

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

## 数据隔离与归属

## H · 元数据归属（PR-001）

⚡ **待精修**：人工分类。

## 跨云数据流（ADR-009）

补本资产是否发 BEC 事件 / 是否被跨云订阅 / 数据延迟等

## 关联

- 真扫源：deep_scan_audit.md
