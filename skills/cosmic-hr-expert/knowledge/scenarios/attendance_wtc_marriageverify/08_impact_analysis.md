# attendance_wtc_marriageverify · 影响分析

> **数据源**：deep_scan_inventory.md + 业务推理

## 引入资产对客户环境的影响

### 元数据层

| 影响项 | 数量 | 说明 |
|---|---|---|
| Java 业务文件 | 5 | 全 ISV 包路径·无冲突 |
| Java 总 LOC | 624 | 复杂度估算 |
| dym | 10 | 含 ISV 自建 + ext 扩展 |
| schdata | 0 | 调度任务·按需 |
| preinsdata SQL | 0 | 部署前确认现场是否已存在 |

## 性能影响评估

补关键性能点 / 优化建议·参考 case_001 / contract_renew_batch 标杆

## 跨云影响（ADR-009）

补本资产是否发 BEC / 是否被跨云订阅 / 数据延迟

## 升级风险

补苍穹版本兼容性 / 关键升级风险点 / 回滚方案

## 关联

- 部署 SOP：[`_assets/<ASSET_ID>/deploy_sop.md`](../../_assets/<ASSET_ID>/deploy_sop.md)
