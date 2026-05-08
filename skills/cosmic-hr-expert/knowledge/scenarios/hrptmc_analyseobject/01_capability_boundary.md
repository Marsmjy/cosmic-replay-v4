# 能力边界 · hrptmc_analyseobject

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（1 个 opKey）
- `save` · 保存

### 删除类（2 个 opKey）
- `disable` · 禁用
- `delete` · 删除

### 导入导出（10 个 opKey）
- `importdata_hr` · 导入数据
- `show_import_record_hr` · 查看导入记录
- `export_from_list_hr` · 按列表导出
- `export_from_impttpl_hr` · 按导入模板导出
- `export_from_expttpl_hr` · 按导出模板导出
- `show_export_record_hr` · 查看导出记录
- `exportconfig` · 导出配置
- `importconfig` · 导入配置
- `importbytemplatelib` · 从模板库导入
- `exportconfigsql` · 导出SQL

### 查询/导航（10 个 opKey）
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `previewdata` · 数据预览
- `viewonelog` · 查看日志
- `logview` · 查看全部日志
- `pivotpreview` · 预览数据
- `showdimvalflex` · 显示维度值面板
- `showindexflex` · 显示指标面板

### 其他（11 个 opKey）
- `nextstep` · 下一步
- `laststep` · 上一步
- `enable` · 启用
- `controlperm` · 控权维度
- `syncstrategy` · 同步策略
- `modify` · 修改
- `new` · 新增
- `generatenewindex` · 生成新指标
- `configdataperm` · 数据控权规则配置
- `copyanobj` · 复制分析对象
- ...（共 11 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `hrptmc_analyseobject`：**13** 个标品插件接入
- 字段总数：**21** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
