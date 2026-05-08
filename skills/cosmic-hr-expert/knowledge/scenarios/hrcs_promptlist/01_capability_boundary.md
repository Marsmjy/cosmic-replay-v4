# 能力边界 · hrcs_promptlist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（1 个 opKey）
- `save` · 保存

### 审核/审批类（5 个 opKey）
- `audit` · 审核
- `unaudit` · 反审核
- `submit` · 提交
- `unsubmit` · 撤销
- `submitandnew` · 提交并新增

### 删除类（2 个 opKey）
- `delete` · 删除
- `disable` · 禁用

### 导入导出（7 个 opKey）
- `importdata_hr` · 引入数据
- `export_from_impttpl_hr` · 按引入模板引出
- `export_from_expttpl_hr` · 引出
- `importdata` · 引入数据
- `exportlist` · 引出数据（按引入模板）
- `exportlistbyselectfields` · 引出数据（按列表）
- `export_prompt` · 引出提示语及内容

### 查询/导航（6 个 opKey）
- `view` · 查看
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `viewruledetail` · 查看规则

### 其他（11 个 opKey）
- `new` · 新增
- `modify` · 修改
- `enable` · 启用
- `close` · 关闭
- `copy` · 复制
- `refresh` · 刷新
- `selectfield` · 选择变量
- `opentips` · 空操作
- `returndata` · 返回数据
- `customwindow` · 自定义弹窗
- ...（共 11 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `hrcs_promptlist`：**4** 个标品插件接入
- 字段总数：**0** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
