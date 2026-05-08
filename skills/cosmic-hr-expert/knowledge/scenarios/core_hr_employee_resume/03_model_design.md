# core_hr_employee_resume · 模型设计

> **聚合场景**：员工履历聚合（教育/工作/技能/职称/培训/导师等 12 实体）（12 个子实体）
> **生成时间**：2026-04-30

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hrpi_pereduexp` | 教育经历 | 33 | 1 | 10 | — |
| `hrpi_preworkexp` | 前工作经历 | 34 | 1 | 8 | — |
| `hrpi_empproexp` | 项目经历 | 30 | 1 | 7 | — |
| `hrpi_perlgability` | 语言技能 | 25 | 2 | 9 | — |
| `hrpi_perocpqual` | 职业资格 | 23 | 1 | 6 | — |
| `hrpi_perpractqual` | 执业资格 | 20 | 2 | 6 | — |
| `hrpi_perprotitle` | 职称信息 | 22 | 1 | 5 | — |
| `hrpi_emptrainfile` | 培训经历 | 28 | 1 | 6 | — |
| `hrpi_rsmpatinv` | 专利发明 | 21 | 1 | 6 | — |
| `hrpi_rsmproskl` | 专业技能 | 12 | 1 | 4 | — |
| `hrpi_empstage` | 雇佣阶段 | 12 | 3 | 3 | — |
| `hrpi_emptutor` | 导师 | 15 | 1 | 5 | — |

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
| core_hr | 13 |
