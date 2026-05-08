# hbss_law_entity · 模型设计

> **聚合场景**：法律实体与聘用单位（8 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_lawentity` | 法律实体 | 86 | 1 | 14 | **8** |
| `hbss_lawentityuse` | 法律实体使用情况 | 21 | 1 | 3 | — |
| `hbss_lawentityvrinf` | 法律实体版本详情 | 39 | 3 | 5 | — |
| `hbss_signcompany` | 聘用单位 | 44 | 1 | 10 | **1** |
| `hbss_signcompanyhis` | 聘用单位历史 | 23 | 5 | 5 | — |
| `hbss_taxunit` | 纳税单位 | 22 | 1 | 5 | **1** |
| `hbss_enterprise` | 用人单位 | 28 | 0 | 8 | **1** |
| `hbss_payrollacrelation` | — | ? | ? | ? | 2 |

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
| org_dev | 8 |
| core_hr | 4 |
| payroll | 1 |
