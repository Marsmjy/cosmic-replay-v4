# 定制方案 · 时间窗口

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## 可套用的 pattern

<TODO> 从 `knowledge/pattern/` 挑合适的模式：
- `add_field_extension` — 加字段（最常见）
- `add_sub_entity` — 加附表
- `add_unique_validation` — 加唯一校验
- `override_plugin_behavior` — 继承标品插件 + super 调用 + 追加逻辑

## 🟡 likely · 推荐覆盖位置

<TODO> 推荐在哪个标品插件上继承扩展：

| 需求类型 | 推荐覆盖的标品插件 | 扩展方式 |
|---|---|---|
| 保存前校验 | `<TODO>` | 继承 + onAddValidators 追加 |
| UI 动态控件 | `<TODO>` | 继承 + afterBindData 追加 |

## ⚠️ unverified · 历史案例

<TODO> 专家补充 2-3 个真实客户项目 + GitLab 路径
