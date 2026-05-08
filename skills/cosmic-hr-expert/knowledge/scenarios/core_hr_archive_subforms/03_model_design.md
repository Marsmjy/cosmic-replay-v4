# core_hr_archive_subforms · 模型设计

> **聚合场景**：员工档案派生模板大聚合（hspm 200+ form · 按后缀分类）（8 个子实体）
> **生成时间**：2026-04-30

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hspm_ermanfile_e` | — | ? | ? | ? | — |
| `hspm_ermanfile_ly` | — | ? | ? | ? | — |
| `hspm_ermanfile_emly` | — | ? | ? | ? | — |
| `hspm_ermanfile_tl` | — | ? | ? | ? | — |
| `hspm_ermanfile_tlmob` | — | ? | ? | ? | — |
| `hspm_attmgr` | — | ? | ? | ? | — |
| `hspm_attmgr_emly` | — | ? | ? | ? | — |
| `hspm_chgrecordview` | — | ? | ? | ? | — |

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
