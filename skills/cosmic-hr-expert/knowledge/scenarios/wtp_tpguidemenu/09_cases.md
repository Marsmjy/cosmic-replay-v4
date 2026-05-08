# ISV 实战 case · wtp_tpguidemenu

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（从 deep_resolve 推典型 case）
> **数据源**: `_deep_resolve_index.json` · `rules_chain_all.json::opKeys`
> **生成**: polish_form_scene_v2.py（v5.1 render_09）

## 典型 ISV 定制 case（按可继承类聚合）

## 通用 ISV 套路提醒

- **必调 super**：所有 lifecycle 方法 override 必须先调 super · 保留标品行为
- **不动标品 form**：要加字段 → 建 ISV 扩展元数据 + `_inherits` 引用主表
- **注册插件 targetType**：BILL_FORM / LIST_FORM / OPERATION 之一（大写枚举·区分大小写）
- **测试**：跑标品测试用例 + ISV 自有用例·验证 super 行为完好
