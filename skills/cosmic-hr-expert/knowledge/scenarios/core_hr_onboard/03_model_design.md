# core_hr_onboard · 模型设计

> **聚合场景**：入职管理（hom · 入职办理 + 预入职 + 候选人协作）（7 个子实体）
> **生成时间**：2026-04-30

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hom_onbrdinfo` | — | ? | ? | ? | — |
| `hom_onbrdinfo_emp` | — | ? | ? | ? | — |
| `hom_preonbrdbasebill` | — | ? | ? | ? | — |
| `hom_prebatchonbrdbill` | — | ? | ? | ? | — |
| `hom_personallonbrd` | — | ? | ? | ? | — |
| `hom_personwaitstart` | — | ? | ? | ? | — |
| `hom_candidate` | — | ? | ? | ? | — |

## 二、继承层次

```
bos_basetpl                      ← 苍穹基础模板
    └── hbp_bd_tpl_all           ← HR 基础资料模板（4 层）
            └── <子实体>          ← 12 个 hbss 字典实体
```

继承深度：4 层（hbss 字典场景标准深度）

## 三、跨云引用拓扑

本场景的子实体被以下云引用（详见 `11_upstream_downstream_logic.md` 自动注入的下游段）：

> 本场景实体当前未被其他云引用。
