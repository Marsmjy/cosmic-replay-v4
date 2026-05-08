# payroll_bankcardchange · 上下游逻辑

> **协议**：ADR-009 跨云穿透架构
> **数据源**：deep_scan_audit.md A/E 段（form 清单 / QFilter 过滤）

## 上游（本场景的数据源）

### 直接上游（来自真扫的 form 引用）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hbss_bankpurpose` | ChangeBankCardBillPlugin.java:95 | — |
| `hbss_payrollacrelation` | ChangeBankCardBillPlugin.java:90 | — |
| `hrpi_employee` | ChangeBankCardOperationPlugin.java:76 | — |
| `hrpi_perbankcard` | ChangeBankCardOperationPlugin.java:110, ChangeBankCardOperationPlugin.java:154, ChangeBankCardOperationPlugin.java:176 等 | — |
| `hrpi_person` | ChangeBankCardOperationPlugin.java:70 | — |
| `hsas_acctmodifybill` | AcctModifyBankByPayDetailOperatePlugin.java:36 | — |
| `hsas_paydetail` | AcctModifyBankByPayDetailOperatePlugin.java:50 | — |

### 查询过滤上下文

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

## 下游（本场景的影响范围）

### 直接下游

补本资产写入哪些字段·影响哪些标品记录

### 跨云影响

补本资产是否触发跨云事件（薪酬/考勤/福利等）

## 跨云穿透矩阵

补具体跨云目标 / 触发链路 / 延迟 / 一致性

## ISV 扩展跨云联动建议

参考 W1 标杆模式（不直接发 BEC·靠标品自动派发）

## 关联资产

- 资产复刻：[`_assets/<ASSET_ID>/`](../../_assets/<ASSET_ID>/)
- 部署 SOP：[`_assets/<ASSET_ID>/deploy_sop.md`](../../_assets/<ASSET_ID>/deploy_sop.md)
- ADR-009：[../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md](../../../v3_rewrite_plan/decisions/ADR_009_cross_cloud_penetration.md)
