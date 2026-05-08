# 能力边界 · wtp_tripchangeset

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（3 个 opKey）
- `save` · 保存
- `saveandnew` · 保存并新增
- `confirmchange` · 确认变更

### 审核/审批类（5 个 opKey）
- `audit` · 审核
- `unaudit` · 反审核
- `submit` · 提交
- `unsubmit` · 撤销
- `submitandnew` · 提交并新增

### 删除类（3 个 opKey）
- `delete` · 删除
- `disable` · 禁用
- `deleteentry` · 删行

### 导入导出（15 个 opKey）
- `importdata_hr` · 导入数据
- `show_import_record_hr` · 查看导入记录
- `export_from_list_hr` · 按列表导出
- `export_from_impttpl_hr` · 按导入模板导出
- `export_from_expttpl_hr` · 按导出模板导出
- `show_export_record_hr` · 查看导出记录
- `importdata` · 导入数据
- `importdetails` · 查看导入结果
- `importtemplatelist` · 管理模板
- `exportlist` · 导出数据（按导入模板）
- ...（共 15 个，参见 `_auto_operations.md`）

### 查询/导航（9 个 opKey）
- `view` · 查看
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `namehistoryview` · 名称历史查询
- `logview` · 查看全部日志
- `viewonelog` · 查看日志
- `showallversion` · 查看所有版本

### 历史版本（7 个 opKey）
- `namehistory` · 改名
- `hisversioninfo` · 历史版本信息
- `hiscopy` · 复制
- `newhisversion` · 新增数据版本
- `revise` · 修订
- `reviserecord` · 版本修订历史
- `versionchangecompare` · 版本对比

### 其他（22 个 opKey）
- `new` · 新增
- `modify` · 修改
- `enable` · 启用
- `close` · 关闭
- `returndata` · 返回数据
- `copy` · 复制
- `refresh` · 刷新
- `option` · 选项设置
- `mobtoolbarselect` · 选择
- `mobtoolbarcancel` · 取消
- ...（共 22 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wtp_tripchangeset`：**51** 个标品插件接入
- 字段总数：**51** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
