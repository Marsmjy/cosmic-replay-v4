# hbss_capability · 模型设计

> **聚合场景**：胜任力/能力字典（12 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_capacitygroup` | 能力素质维度 | 34 | 1 | 9 | — |
| `hbss_capacityitem` | 能力素质项 | 51 | 3 | 9 | — |
| `hbss_capacityaction` | 能力行为 | 21 | 0 | 3 | — |
| `hbss_capacityrankscheme` | 能力等级方案 | 32 | 2 | 7 | — |
| `hbss_passessnode` | 绩效业务活动 | 19 | 0 | 3 | — |
| `hbss_rewpnmlevel` | — | ? | ? | ? | 1 |
| `hbss_rewpnmtype` | — | ? | ? | ? | 1 |
| `hbss_scoreinterval` | 评分间隔 | 14 | 5 | 2 | — |
| `hbss_scoresystem` | 评分分制 | 38 | 4 | 8 | **1** |
| `hbss_hrbuca` | HR业务管理视图 | 24 | 0 | 4 | — |
| `hbss_hrbucafunc` | — | ? | ? | ? | — |
| `hbss_familiarity` | — | ? | ? | ? | 10 |

## 二、继承层次

```
bos_basetpl                      ← 苍穹基础模板
    └── hbp_bd_tpl_all           ← HR 基础资料模板（4 层）
            └── <子实体>          ← 12 个 hbss 字典实体
```

继承深度：4 层（hbss 字典场景标准深度）

## 三、跨云引用拓扑

本场景的子实体被以下云引用（详见 `11_upstream_downstream_logic.md` 自动注入的下游段）：

| 消费云 | 引用次数 |
|---|:---:|
| core_hr | 8 |
| payroll | 5 |
