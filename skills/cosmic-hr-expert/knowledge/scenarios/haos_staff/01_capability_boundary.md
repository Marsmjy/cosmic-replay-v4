# 能力边界 · haos_staff

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（1 个 opKey）
- `save` · save

### 删除类（26 个 opKey）
- `disable` · disable
- `delete_useorgdetail` · delete_useorgdetail
- `delete_dutyorg` · delete_dutyorg
- `donothing_delete` · donothing_delete
- `donothing_deletejob` · donothing_deletejob
- `donothing_deletelaborreltype` · donothing_deletelaborreltype
- `donothing_deleteflexdim1` · donothing_deleteflexdim1
- `donothing_deleteflexdim2` · donothing_deleteflexdim2
- `donothing_deleteflexdim3` · donothing_deleteflexdim3
- `donothing_deleteflexdim4` · donothing_deleteflexdim4
- ...（共 26 个，参见 `_auto_operations.md`）

### 导入导出（33 个 opKey）
- `importdata_hr` · importdata_hr
- `show_import_record_hr` · show_import_record_hr
- `export_from_list_hr` · export_from_list_hr
- `export_from_impttpl_hr` · export_from_impttpl_hr
- `export_from_expttpl_hr` · export_from_expttpl_hr
- `show_export_record_hr` · show_export_record_hr
- `importmuldimendetail` · importmuldimendetail
- `import_dutyorg` · import_dutyorg
- `importuseorgdetail` · importuseorgdetail
- `exportentry_useorg` · exportentry_useorg
- ...（共 33 个，参见 `_auto_operations.md`）

### 查询/导航（1 个 opKey）
- `namehistoryview` · namehistoryview

### 历史版本（1 个 opKey）
- `namehistory` · namehistory

### 其他（35 个 opKey）
- `enable` · enable
- `close` · close
- `mobtoolbarselect` · mobtoolbarselect
- `mobtoolbarcancel` · mobtoolbarcancel
- `copy_useorgdetail` · copy_useorgdetail
- `new_useorgdetail` · new_useorgdetail
- `donothing_sum` · donothing_sum
- `copy_muldimendetail` · copy_muldimendetail
- `new_dutyorg` · new_dutyorg
- `new` · new
- ...（共 35 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `haos_staff`：**24** 个标品插件接入
- 字段总数：**111** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
