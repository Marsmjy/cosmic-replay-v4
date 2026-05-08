# 变更影响面 · 参数配置

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 🟡 likely · 改动本场景会影响哪些下游

<TODO> 结合 `knowledge/domain/org/anchors.md` 的禁区规则：

| 改什么 | 影响范围 | 级联动作 |
|---|---|---|
| 行政组织字段 | 岗位、员工、成本中心 | 需要反写到下游 |
| 状态（启/停） | 所有引用该组织的单据 | 参见锁定规则 |

## ⚠️ unverified · 生产事故案例

<TODO> 专家补充 1-2 个真实事故（改错了带崩了什么）
