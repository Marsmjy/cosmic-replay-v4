# 能力边界 · wtbd_roundrule

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（2 个 opKey）
- `save` · 保存
- `saveandnew` · 保存并新增

### 审核/审批类（7 个 opKey）
- `audit` · 审核
- `unaudit` · 反审核
- `submit` · 提交
- `unsubmit` · 撤销
- `submitandnew` · 提交并新增
- `auditmodify` · 空操作
- `submitandeffect` · 提交并生效

### 删除类（3 个 opKey）
- `delete` · 删除
- `disable` · 禁用
- `deleteentry` · 删除分录

### 导入导出（14 个 opKey）
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
- ...（共 14 个，参见 `_auto_operations.md`）

### 查询/导航（8 个 opKey）
- `view` · 查看
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `namehistoryview` · 名称历史查询
- `logview` · 查看全部日志
- `viewonelog` · 查看日志

### 历史版本（1 个 opKey）
- `namehistory` · 改名

### 其他（14 个 opKey）
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
- ...（共 14 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wtbd_roundrule`：**25** 个标品插件接入
- 字段总数：**32** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
