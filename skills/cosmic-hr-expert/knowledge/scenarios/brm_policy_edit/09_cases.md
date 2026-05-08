# brm_policy_edit · 案例

> **生成时间**：2026-04-29

## CASE-2 · 跨云引用真实场景

本 form 被以下云的 form 引用（共 1 处）：

- org_dev：1 处

> 详见 `11_upstream_downstream_logic.md` 自动注入的下游消费者段

## CASE-3 · ISV 改本 form 的影响推演

| 改动 | 影响 | 后果 |
|---|---|---|
| 加 ISV 字段（_ext 前缀） | 仅 ISV 私域 | ✅ 安全 |
| 删除标品字段 | 标品 OP 读不到 | ❌ save 失败 |
| 改字段必填属性 | 标品校验链冲突 | ❌ 元数据加载错 |
| 改字段名 / 类型 / refEntity | 影响 1 处下游 | ❌ 下游 form 加载错 |
