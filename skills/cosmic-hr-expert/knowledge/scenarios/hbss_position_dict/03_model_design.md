# hbss_position_dict · 模型设计

> **聚合场景**：职位/职务字典（15 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_postype` | 任职类型 | 20 | 1 | 4 | **2** |
| `hbss_postcategory` | 任职类型分类 | 19 | 0 | 3 | — |
| `hbss_poststate` | 任职状态 | 20 | 1 | 4 | **2** |
| `hbss_poststatecls` | 任职状态分类 | 19 | 0 | 3 | — |
| `hbss_costcenter` | HR成本承担单位 | 22 | 1 | 5 | — |
| `hbss_bussinessfield` | 业务类型 | 21 | 1 | 5 | **2** |
| `hbss_hrbusinessfield` | 业务领域 | 19 | 0 | 3 | **2** |
| `hbss_industrytype` | — | ? | ? | ? | 12 |
| `hbss_companyscale` | 公司规模 | 19 | 0 | 3 | **2** |
| `hbss_cadrecategory` | 干部类别 | 20 | 1 | 3 | **2** |
| `hbss_projecttype` | — | ? | ? | ? | 2 |
| `hbss_rotationtype` | — | ? | ? | ? | 1 |
| `hbss_disptype` | — | ? | ? | ? | 1 |
| `hbss_perflevel` | — | ? | ? | ? | 1 |
| `hbss_depcytype` | — | ? | ? | ? | 1 |

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
| core_hr | 15 |
| org_dev | 9 |
| payroll | 5 |
| other | 1 |
