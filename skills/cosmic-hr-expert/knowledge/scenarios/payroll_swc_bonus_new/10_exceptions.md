# payroll_swc_bonus_new · 异常处理

> **数据源**：deep_scan_audit.md F 段（业务规则推断·含异常分支）

## 已知异常路径

基于 java 源 try/catch + throw KDBizException 扫描·补具体 EX-01 ~ EX-N

提示：参考 W1 标杆 [contract_renew_batch 10_exceptions.md](../core_hr_contract_renew/10_exceptions.md)

## 业务规则推断（含异常分支）

## F · 业务规则推断（自动·从 set 模式扫·⚡需人工确认）

⚡ **待精修**：以下推断只是脚本看到的状态字段写入·人工要把它们组织成业务规则 R-01 / R-02 …

可能的状态机字段：
- `auditstatus` 可能取值: A, K
- `billstatus` 可能取值: A, F, H, K
- `${ISV_FLAG}_approval_status` 可能取值: A, K, null
- `${ISV_FLAG}_is_agent` 可能取值: 0, 1
- `${ISV_FLAG}_push_status` 可能取值: B, C, D, E, F
- `${ISV_FLAG}_source_bill_type` 可能取值: A, B

## 容错策略

### 事务一致性

补本资产 op 事务边界

### 幂等性

补本资产关键操作的幂等性保证

## 业务侧排错指南

HR / 实施人员典型问题 → 排查步骤

## 关联

- 真扫源：deep_scan_audit.md F 段
