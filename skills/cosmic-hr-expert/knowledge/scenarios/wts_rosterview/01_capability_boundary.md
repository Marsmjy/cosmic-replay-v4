# 能力边界 · wts_rosterview

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（1 个 opKey）
- `saveroster` · 保存

### 导入导出（6 个 opKey）
- `show_import_record_hr` · 查看导入结果
- `show_export_record_hr` · 查看导出结果
- `importroster` · 按列表导入
- `exportroster` · 按列表导出
- `exportrostertable` · 按排班表导出
- `importrostertable` · 按排班表导入

### 查询/导航（1 个 opKey）
- `viewlog` · 查看排班日志

### 其他（21 个 opKey）
- `copypersonroster` · 复制排班
- `donothing` · 空操作
- `lockpersonroster` · 锁定人员排班
- `unlockpersonroster` · 解锁人员排班
- `completepersonroster` · 完成人员计划排班
- `copyorgroster` · 复制行政组织排班
- `lockorgroster` · 锁定行政组织排班
- `unlockorgroster` · 解锁行政组织排班
- `completeorgroster` · 完成行政组织排班
- `restore` · 重置
- ...（共 21 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wts_rosterview`：**2** 个标品插件接入
- 字段总数：**3** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
