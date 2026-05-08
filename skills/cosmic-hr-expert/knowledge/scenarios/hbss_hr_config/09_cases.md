# hbss_hr_config · 案例

> **生成时间**：2026-04-29
> **方法**：从 `_cross_cloud_reports/hr_hrmp_consumed_by.json` 提取真实跨云消费场景

## 真实跨云消费案例

### CASE-1: `hbss_rolesourcetype` 被消费 (1 处 · 1 跨云)

**org_dev（1 处）：**

- `hbpm_positionrelation` → `sourcetype`

## ISV 改本场景实体的影响推演

假如 ISV 试图修改 `number` / `name` / `enable` 等标品公共字段：

| 改动 | 影响下游 | 后果 |
|---|---|---|
| 改 `number` 类型从 TextField 改 LongField | ❌ 全部下游 BasedataField 引用 | 下游 form 加载失败 |
| 删除标品字段 | ❌ 全部下游引用 | 下游 form 元数据加载错误 |
| 加 ISV 字段（前缀 _ext） | ✅ 不影响下游 | 下游不感知 |
| 改 `enable` 默认值 | ⚠️ 部分下游 | 数据导入时可能默认值不一致 |
