# core_hr_person_attrs · 模型设计

> **聚合场景**：人员属性聚合（个人/家庭/联系/证件/履历记录 11 实体）（11 个子实体）
> **生成时间**：2026-04-30

## 一、子实体清单与字段统计

| 实体 | 中文名 | 字段数 | 必填字段数 | 引用基础资料数 | 跨云被引用 |
|---|---|:---:|:---:|:---:|:---:|
| `hrpi_partymember` | 党员信息 | 24 | 1 | 3 | — |
| `hrpi_fertilityinfo` | 生育信息 | 14 | 1 | 4 | — |
| `hrpi_perhobby` | 特长及爱好 | 13 | 1 | 3 | — |
| `hrpi_peraddress` | 人员地址 | 14 | 1 | 5 | — |
| `hrpi_percontact` | 联系方式 | 28 | 1 | 3 | — |
| `hrpi_perregion` | 区域信息 | 16 | 1 | 7 | — |
| `hrpi_percre` | 证件信息 | 33 | 3 | 8 | — |
| `hrpi_familymemb` | 家庭成员 | 21 | 1 | 6 | — |
| `hrpi_emrgcontact` | 紧急联系人 | 15 | 1 | 5 | — |
| `hrpi_perfresult` | 绩效结果 | 22 | 2 | 7 | — |
| `hrpi_perrprecord` | 奖惩记录 | 27 | 1 | 6 | — |

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
