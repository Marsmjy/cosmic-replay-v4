# admin_org_basedata · 模型设计

> **聚合场景**：组织基础资料 5 件套（5 个子实体）
> **生成时间**：2026-04-30

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `haos_adminorgtype` | 行政组织类型 | 29 | 1 | 9 | — |
| `haos_adminorglayer` | 管理层级 | 27 | 0 | 7 | — |
| `haos_adminorgfunction` | 行政组织职能 | 27 | 0 | 7 | — |
| `haos_orgchangereason` | 行政组织变动原因 | 28 | 0 | 8 | — |
| `haos_changescene` | 行政组织变动场景 | 31 | 1 | 9 | — |

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
| org_dev | 47 |
