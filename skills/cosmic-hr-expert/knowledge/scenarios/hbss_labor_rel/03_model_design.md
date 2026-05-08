# hbss_labor_rel · 模型设计

> **聚合场景**：劳动/用工关系字典（13 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_laborreltype` | 用工关系类型 | 20 | 1 | 4 | **6** |
| `hbss_laborreltypecls` | 用工关系类型分类 | 19 | 0 | 3 | — |
| `hbss_laborrelstatus` | 用工关系状态 | 21 | 0 | 3 | **1** |
| `hbss_laborrelsub` | 人员分类 | 20 | 1 | 3 | — |
| `hbss_contracttypes` | 合同类型 | 26 | 1 | 8 | **1** |
| `hbss_contracttypecat` | — | ? | ? | ? | — |
| `hbss_empnature` | 企业性质 | 19 | 0 | 3 | **4** |
| `hbss_employeegroup` | 员工组 | 19 | 0 | 3 | **2** |
| `hbss_empgroup` | 业务档案分组 | 20 | 1 | 4 | — |
| `hbss_onboardsource` | — | ? | ? | ? | 1 |
| `hbss_appointtype` | — | ? | ? | ? | 2 |
| `hbss_apptreasongroup` | — | ? | ? | ? | 2 |
| `hbss_timelimittype` | 合同期限类型 | 27 | 0 | 7 | **1** |

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
| core_hr | 14 |
| org_dev | 4 |
| payroll | 2 |
