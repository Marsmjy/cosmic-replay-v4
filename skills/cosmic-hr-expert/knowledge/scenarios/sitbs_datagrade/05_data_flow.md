# 数据流转 · 个税税率表

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 🟡 likely · 数据落地链路（基于插件 package 名推测）

<TODO> 从主操作插件（带 `SaveOp` 字样的）→ 看它调用哪个 `IBatchOpService` →
→ 实际更新哪些 t_* 表 → 是否落到时序表 t_*_his

## ⚠️ unverified

- [ ] 历史表写入时机
- [ ] 事务边界（哪些在一个 TX 内）
- [ ] 失败回滚策略
