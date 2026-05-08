# 能力边界 · core_hr_assignment

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（2 个 opKey）
- `save` · 保存
- `saveatt` · 保存

### 删除类（2 个 opKey）
- `delete` · 删除
- `deleteatt` · 删除

### 导入导出（9 个 opKey）
- `importdata_hr` · 导入数据
- `show_import_record_hr` · 查看导入记录
- `export_from_list_hr` · 按列表导出
- `export_from_impttpl_hr` · 按导入模板导出
- `export_from_expttpl_hr` · 按导出模板导出
- `show_export_record_hr` · 查看导出记录
- `importexport_userset` · 导入导出个性化设置
- `batchimport` · 批量导入
- `batchexport` · 批量导出

### 查询/导航（7 个 opKey）
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `printpreview` · 打印预览
- `record_view` · 导入导出记录查看
- `person_attachview` · 人员附件查看

### 历史版本（1 个 opKey）
- `historyrecord` · 历史记录

### 其他（9 个 opKey）
- `printsetting` · 打印设置
- `print` · 打印
- `selecttplprint` · 选择模板打印
- `selecttplprintext` · 打印
- `modify` · 修改
- `start_infocollect` · 发起信息采集任务（要求勾选数据）
- `infocollect_list` · 信息采集任务跟踪
- `infocollect_records` · 员工完善档案记录
- `start_infocollect_donothing` · 发起信息采集任务

## ✅ verified · 标品插件链

- 主表 `hrpi_assignment`：**15** 个标品插件接入
- 字段总数：**23** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
