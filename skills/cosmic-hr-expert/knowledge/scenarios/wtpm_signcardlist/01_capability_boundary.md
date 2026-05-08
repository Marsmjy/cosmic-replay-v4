# 能力边界 · wtpm_signcardlist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（2 个 opKey）
- `saveofsubmiteffect` · 提交并生效
- `saveofsubmit` · 提交

### 审核/审批类（5 个 opKey）
- `unsubmit` · 撤销
- `unaudit` · 反审核
- `audit` · 审核
- `dounsubmit` · 撤销
- `doaudit` · 审核

### 删除类（3 个 opKey）
- `delete` · 删除
- `deleteentry` · 删除
- `dodelete` · 删除

### 导入导出（12 个 opKey）
- `importdata_hr` · 导入数据
- `show_import_record_hr` · 查看导入记录
- `export_from_list_hr` · 按列表导出
- `export_from_impttpl_hr` · 按导入模板导出
- `export_from_expttpl_hr` · 按导出模板导出
- `show_export_record_hr` · 查看导出记录
- `exportlist_expt` · 导出数据（按导出模板）
- `exportlist` · 导出数据（按导入模板）
- `exportlistbyselectfields` · 导出数据（按列表）
- `exportdetails` · 查看导出结果
- ...（共 12 个，参见 `_auto_operations.md`）

### 查询/导航（2 个 opKey）
- `viewtrash` · 回收站
- `viewlog` · 查看申请日志

### 其他（11 个 opKey）
- `close` · 关闭
- `refresh` · 刷新
- `returndata` · 返回数据
- `mobtoolbarselect` · 选择
- `mobtoolbarcancel` · 取消
- `trash` · 回收数据
- `restoredata` · 恢复数据
- `discard` · 废弃
- `modify` · 修改
- `new` · 新增
- ...（共 11 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wtpm_signcardlist`：**12** 个标品插件接入
- 字段总数：**25** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
