# payroll_beforecomputationcheck · 上下游逻辑

> **协议**：ADR-009 跨云穿透架构
> **数据源**：deep_scan_audit.md A/E 段（form 清单 / QFilter 过滤）

## 上游（本场景的数据源）

### 直接上游（来自真扫的 form 引用）

## A · 涉及 form 清单（自动）

按 java 文件聚合的 form 引用·每条带 file:line 锚点：

| form | 引用位置 | 业务含义|
|---|---|---|
| `hpdi_bizdatabill` | CalPayPushPlugin.java:76 | — |
| `hpdi_bizdatabillent` | HsasCalpayRolltListPlugin.java:87 | — |
| `hsas_calperson` | HsasCalpayRolltListPlugin.java:74 | — |
| `tdkw_calpaypush` | HsasCalpayRolltListPlugin.java:93 | — |

### 查询过滤上下文

## E · 查询过滤条件（QFilter 模式）

按字段汇总·查询特征：

| 字段 | 过滤操作 | 用例位置（前 3 处） |
|---|---|---|
| `billstatus` | `QCP.in` | HsasCalpayRolltListPlugin.java:81 |
| `bizdatabillid` | `QCP.in` | HsasCalpayRolltListPlugin.java:86 |
| `calperiod` | `QCP.equals` | HsasCalpayRolltListPlugin.java:81 |
| `empposorgrel.assignment` | `QCP.in` | HsasCalpayRolltListPlugin.java:86 |
| `id` | `QCP.in` | CalPayPushPlugin.java:46, CalPayPushPlugin.java:59, CalPayPushPlugin.java:76 |

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
