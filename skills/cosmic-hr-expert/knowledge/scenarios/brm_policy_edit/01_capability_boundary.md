# 能力边界 · brm_policy_edit

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（3 个 opKey）
- `save` · save
- `saveandnew` · saveandnew
- `saveandconfigrule` · saveandconfigrule

### 审核/审批类（5 个 opKey）
- `audit` · audit
- `unaudit` · unaudit
- `submit` · submit
- `unsubmit` · unsubmit
- `submitandnew` · submitandnew

### 删除类（5 个 opKey）
- `delete` · delete
- `disable` · disable
- `deleteruleentry` · deleteruleentry
- `deletebuentry` · deletebuentry
- `deletepolicy` · deletepolicy

### 导入导出（15 个 opKey）
- `importdata_hr` · importdata_hr
- `show_import_record_hr` · show_import_record_hr
- `export_from_list_hr` · export_from_list_hr
- `export_from_impttpl_hr` · export_from_impttpl_hr
- `export_from_expttpl_hr` · export_from_expttpl_hr
- `show_export_record_hr` · show_export_record_hr
- `importdata` · importdata
- `importdetails` · importdetails
- `importtemplatelist` · importtemplatelist
- `exportlist` · exportlist
- ...（共 15 个，参见 `_auto_operations.md`）

### 查询/导航（7 个 opKey）
- `view` · view
- `first` · first
- `previous` · previous
- `next` · next
- `last` · last
- `logview` · logview
- `viewonelog` · viewonelog

### 其他（15 个 opKey）
- `new` · new
- `modify` · modify
- `enable` · enable
- `closepage` · closepage
- `returndata` · returndata
- `refresh` · refresh
- `option` · option
- `mobtoolbarselect` · mobtoolbarselect
- `mobtoolbarcancel` · mobtoolbarcancel
- `newrule` · newrule
- ...（共 15 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `brm_policy_edit`：**23** 个标品插件接入
- 字段总数：**48** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
