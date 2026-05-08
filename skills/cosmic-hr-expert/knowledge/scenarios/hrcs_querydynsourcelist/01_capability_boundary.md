# 能力边界 · hrcs_querydynsourcelist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 删除类（1 个 opKey）
- `delete` · 删除

### 查询/导航（2 个 opKey）
- `delquery` · 删除
- `addquery` · 新增

### 其他（3 个 opKey）
- `refresh` · 刷新
- `close` · 关闭
- `donothing_esschemeconfig` · ES同步方案配置

## ✅ verified · 标品插件链

- 主表 `hrcs_querydynsourcelist`：**4** 个标品插件接入
- 字段总数：**5** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
