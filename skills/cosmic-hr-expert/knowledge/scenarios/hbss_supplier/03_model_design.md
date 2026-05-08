# hbss_supplier · 模型设计

> **聚合场景**：供应商/银行/工作地点字典（12 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_supplier` | 供应商 | 38 | 7 | 5 | — |
| `hbss_supplierlevel` | 供应商等级 | 19 | 0 | 3 | — |
| `hbss_bankdeposit` | 开户行 | 19 | 0 | 3 | — |
| `hbss_workplace` | 工作地 | 24 | 3 | 6 | **13** |
| `hbss_encouragewords` | 激励文案 | 7 | 1 | 2 | — |
| `hbss_addressdetail` | 详细地址 | 23 | 4 | 6 | — |
| `hbss_cycletype` | 周期类型 | 19 | 0 | 3 | — |
| `hbss_entitytype` | 实体类型 | 20 | 1 | 3 | — |
| `hbss_procreatstatus` | 生育状况 | 19 | 0 | 3 | **2** |
| `hbss_procreatmode` | — | ? | ? | ? | 1 |
| `hbss_certmember` | 许可分组员工 | 3 | 0 | 0 | — |
| `hbss_category` | — | ? | ? | ? | 2 |

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
| org_dev | 10 |
| core_hr | 6 |
| payroll | 2 |
