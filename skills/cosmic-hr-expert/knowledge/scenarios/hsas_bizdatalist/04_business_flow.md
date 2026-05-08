# 业务流转 · 前端业务数据(纵表)

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 🟡 likely · 状态机（基于插件命名推测）

<TODO> 根据 `*SaveOp` / `*SubmitOp` / `*AuditOp` / `*EffectOp` 等插件，推测状态流：

```
草稿 → 已提交 → 已审核 → 已生效 → 已终止
```

## ⚠️ unverified · 实际流程

<TODO> 问专家：
1. 状态字段用的是哪个字段？（`billstatus` / `enable` / ...）
2. 审批链是否固定？有没有客户能改？
3. 生效时机（提交即生效 / 审批通过生效 / 指定日期生效）？
