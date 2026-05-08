# hbss_edu_train · 模型设计

> **聚合场景**：教育/培训/资历字典（18 个子实体）
> **生成时间**：2026-04-29

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hbss_college` | 高等院校 | 26 | 1 | 5 | **3** |
| `hbss_diploma` | — | ? | ? | ? | 6 |
| `hbss_degree` | — | ? | ? | ? | 2 |
| `hbss_diplomatype` | — | ? | ? | ? | 2 |
| `hbss_educerttype` | — | ? | ? | ? | 2 |
| `hbss_languagecert` | — | ? | ? | ? | 2 |
| `hbss_languagetype` | 语言种类 | 19 | 0 | 3 | **2** |
| `hbss_trainmode` | — | ? | ? | ? | 2 |
| `hbss_traintype` | — | ? | ? | ? | 2 |
| `hbss_ocpqual` | — | ? | ? | ? | 2 |
| `hbss_ocpquallevel` | — | ? | ? | ? | 3 |
| `hbss_protitle` | 职称 | 20 | 0 | 4 | **1** |
| `hbss_protitlelevel` | — | ? | ? | ? | 1 |
| `hbss_protitletype` | — | ? | ? | ? | — |
| `hbss_operationqual` | 执业资格 | 19 | 0 | 3 | **1** |
| `hbss_patentstatus` | — | ? | ? | ? | 2 |
| `hbss_patentscategory` | — | ? | ? | ? | 2 |
| `hbss_credentialstype` | 证件类型 | 21 | 0 | 4 | **6** |

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
| core_hr | 23 |
| payroll | 14 |
| org_dev | 3 |
| other | 1 |
