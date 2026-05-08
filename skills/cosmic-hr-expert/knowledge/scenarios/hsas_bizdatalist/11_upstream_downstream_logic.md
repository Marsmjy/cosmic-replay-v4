# 上下游联动 · 前端业务数据(纵表)

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 🟡 likely · 跨模块影响

<TODO> 本场景与哪些 HR 子域有联动：

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| hrpi (人事) | `<TODO>` | | |
| pay (薪酬) | `<TODO>` | | |
| attendance (考勤) | `<TODO>` | | |
| performance (绩效) | `<TODO>` | | |

## ⚠️ unverified

<TODO> 专家补充：具体反写字段 / 触发时机 / 失败后果

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->
