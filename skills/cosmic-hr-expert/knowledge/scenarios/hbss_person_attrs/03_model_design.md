# hbss_person_attrs · 模型设计

> **聚合场景**：人员基础属性字典（15 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_sex` | — | ? | ? | ? | 6 |
| `hbss_nationality` | — | ? | ? | ? | 6 |
| `hbss_bloodtype` | — | ? | ? | ? | 2 |
| `hbss_constellation` | — | ? | ? | ? | 2 |
| `hbss_zodiac` | — | ? | ? | ? | 2 |
| `hbss_marriagestatus` | — | ? | ? | ? | 2 |
| `hbss_healthstatus` | — | ? | ? | ? | 2 |
| `hbss_religion` | — | ? | ? | ? | 2 |
| `hbss_party` | — | ? | ? | ? | 2 |
| `hbss_politicalstatus` | — | ? | ? | ? | 4 |
| `hbss_flok` | — | ? | ? | ? | 4 |
| `hbss_familiarity` | — | ? | ? | ? | 10 |
| `hbss_familymemberrel` | — | ? | ? | ? | 2 |
| `hbss_emergcontactype` | — | ? | ? | ? | 2 |
| `hbss_addresstype` | — | ? | ? | ? | 2 |

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
| core_hr | 27 |
| payroll | 23 |
