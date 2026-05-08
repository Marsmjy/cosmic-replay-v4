# 业务规则 · 停缓发管理

> **状态**: 🟡 骨架（机读事实已填 · 人工语义待补）
> **初始化**: 2026-05-03
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 真实 formRule / bizRule（listRules 实抓）

_本场景无 formRule / bizRule（可能规则写在插件代码里，见下面 likely 段）_

## 🟡 likely · 隐式规则（来自插件命名）

<TODO> 看 `_auto_plugin_registry.md` 里 `*ValidateOp` / `*CheckOp` 插件，这些通常承载隐式规则：

- [ ] 组织编码唯一性
- [ ] 禁用前是否有下级校验
- [ ] 变动场景选择后的字段联动

## ⚠️ unverified · 跨模块约束

<TODO> 问专家：
1. 启用 / 禁用会触发哪些下游校验？（薪酬 / 考勤成本中心等）
2. 有没有不写在 rule 而是靠人肉 SOP 的规则？

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.query.QueryListPlugin -->

## chgaction 实证补充（QueryListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.query.QueryListPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.query.QueryListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.query.QueryListPlugin -->
