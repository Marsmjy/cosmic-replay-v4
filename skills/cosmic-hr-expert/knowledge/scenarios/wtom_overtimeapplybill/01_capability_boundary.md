# 能力边界 · wtom_overtimeapplybill

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（机读 opKey 分类 + 标品插件链 + 字段统计 · v2）
> **数据源**: `_metadata_rules_opkey.json` · `_auto_plugin_registry.md` · `_metadata_rules_form.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 实抓能力（按 opKey 分类）

### 保存类（2 个 opKey）
- `save` · 保存
- `wtcreaudit` · 恢复核算

### 审核/审批类（10 个 opKey）
- `submit` · 提交
- `unsubmit` · 撤销
- `submitandnew` · 提交并新增
- `submiteffect` · 提交并生效
- `wfauditing` · 审批中
- `wfrejecttosubmit` · 驳回至提交人
- `wfauditnotpass` · 审批不通过
- `wfauditpass` · 审批通过(废弃)
- `unaudit` · 反审核
- `audit` · 审批通过

### 删除类（4 个 opKey）
- `delete` · 删除
- `deleteentry` · 删除分录
- `deletescentry` · 删行
- `mydeleteentry` · 自定义删除分录操作

### 导入导出（14 个 opKey）
- `importdata_hr` · 导入数据
- `show_import_record_hr` · 查看导入记录
- `export_from_impttpl_hr` · 导出数据（按导入模板）
- `show_export_record_hr` · 查看导出结果
- `export_from_expttpl_hr` · 导出数据（按导出模板）
- `importtemplate` · 设置模板
- `importtemplatelist` · 管理模板
- `importdetails` · 查看导入结果
- `importdata` · 导入数据
- `exportlist` · 导出数据（按导入模板）
- ...（共 14 个，参见 `_auto_operations.md`）

### 查询/导航（8 个 opKey）
- `view` · 查看
- `first` · 第一
- `previous` · 前一
- `next` · 后一
- `last` · 最后
- `viewtrash` · 回收站
- `viewflowchart` · 查看流程图
- `showhisbill` · 展开

### 其他（25 个 opKey）
- `new` · 新增
- `close` · 关闭
- `copy` · 复制
- `option` · 选项设置
- `returndata` · 返回数据
- `mobtoolbarselect` · 选择
- `mobtoolbarcancel` · 取消
- `trash` · 回收数据
- `restoredata` · 恢复数据
- `modify` · 修改
- ...（共 25 个，参见 `_auto_operations.md`）

## ✅ verified · 标品插件链

- 主表 `wtom_overtimeapplybill`：**39** 个标品插件接入
- 字段总数：**62** 个（实抓自 OpenAPI listMeta）
- 完整清单参见 `_auto_plugin_registry.md`

## 🟡 不覆盖 / 已知限制（待人工补充）

<TODO 人工补> 该场景**做不到什么**（比能做什么更重要）：
- [ ] 是否有只有审批单才能做的动作？
- [ ] 是否有跨云协同动作（薪酬 / 考勤 / 福利侧）？
- [ ] 版本差异（2024R1 / 2025）是否行为不同？
