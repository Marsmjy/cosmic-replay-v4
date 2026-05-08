# 能力边界 · wtpm_supsignlist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（3 个 opKey）
- `save` · 保存
- `saveofsubmit` · 提交
- `saveofsubmiteffect` · 提交并生效

### 审核/审批类（8 个 opKey）
- `submitandnew` · 提交并新增
- `wfauditing` · 审批中
- `wfrejecttosubmit` · 驳回至提交人
- `wfauditnotpass` · 审批不通过
- `wfauditpass` · 审批通过(废弃)
- `unaudit` · 反审核
- `audit` · 审核
- `dounsubmit` · 撤销

### 删除类（1 个 opKey）
- `dodelete` · 删除

### 导入导出（10 个 opKey）
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

### 查询/导航（5 个 opKey）
- `printpreview` · 打印预览
- `view` · 查看
- `viewtrash` · 回收站
- `viewlog` · 查看申请日志
- `viewflowchart` · 查看流程图

### 其他（15 个 opKey）
- `new` · 新增
- `close` · 退出
- `refresh` · 刷新
- `copy` · 复制
- `option` · 选项设置
- `print` · 打印
- `printsetting` · 打印设置
- `mobtoolbarselect` · 选择
- `mobtoolbarcancel` · 取消
- `trash` · 回收数据
- ...（共 15 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wtpm_supsignlist`：**11** 个标品插件接入
- 字段总数：**27** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
