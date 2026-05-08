# 能力边界 · hbpm_positionhr

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（3 个 opKey）
- `save` · save
- `saveandnew` · saveandnew
- `his_save` · his_save

### 审核/审批类（1 个 opKey）
- `submitandnew` · submitandnew

### 删除类（2 个 opKey）
- `disable` · disable
- `deleteentry` · deleteentry

### 导入导出（17 个 opKey）
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
- ...（共 17 个，参见 `_auto_operations.md`）

### 查询/导航（10 个 opKey）
- `view` · view
- `first` · first
- `previous` · previous
- `next` · next
- `last` · last
- `namehistoryview` · namehistoryview
- `logview` · logview
- `viewonelog` · viewonelog
- `showallversion` · showallversion
- `hisversion_view` · hisversion_view

### 历史版本（6 个 opKey）
- `namehistory` · namehistory
- `hiscopy` · hiscopy
- `newhisversion` · newhisversion
- `revise` · revise
- `reviserecord` · reviserecord
- `versionchangecompare` · versionchangecompare

### 其他（18 个 opKey）
- `new` · new
- `modify` · modify
- `enable` · enable
- `close` · close
- `returndata` · returndata
- `copy` · copy
- `refresh` · refresh
- `option` · option
- `mobtoolbarselect` · mobtoolbarselect
- `mobtoolbarcancel` · mobtoolbarcancel
- ...（共 18 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `hbpm_positionhr`：**37** 个标品插件接入
- 字段总数：**72** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
