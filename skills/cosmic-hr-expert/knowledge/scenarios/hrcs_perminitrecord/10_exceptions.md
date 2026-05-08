# 异常诊断 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于 20 插件反编译 + 通用陷阱规则）
> **数据源**: `form_lifecycle_rules.json` + `rules_chain_all.json` + `curated_sdk.json`

## 🟢 通用共性陷阱（来自 cosmic_realworld_traps）

| 陷阱 | 适用本场景说明 |
|---|---|
| **buildMeta parentId 陷阱** | 建 ISV 扩展元数据加字段到 hrcs_perminitrecord 子分录时，parentId 必须显式传母表物理 ID，否则兜底到 bos 基础资料模板。详见 `memory/buildmeta_parent_template_trap.md` |
| **modifyMeta 参数名陷阱** | add field 时参数名是 `fieldType/name/columnName`（非 dataType/displayName）；传错静默走 TextField。详见 `memory/modifymeta_param_names_and_hr_sdk_limits.md` |
| **addRule preCondition 陷阱** | preCondition 不支持 `==''`（空字符串判断）；校验字段为空用 `isnull`。见 `memory/kb_cosmic_addrule_traps.md` |
| **EmbedFormAp 假成功陷阱** | 嵌入子表单到 hrcs_perminitrecord 主布局需要 Java 插件支持；OpenAPI 调 modifyMeta 加 EmbedFormAp 会返回成功但实际不生效。标品已嵌入 hrcs_dynadim/hrcs_permroleinitfunc/hrcs_permroleinitdim/hrcs_permroleinitdr 4 个子表单 |
| **ISV 归属陷阱** | 标品 formId=hrcs_perminitrecord 归属 hrcs（标品），ISV 不能直接 modifyMeta add/remove field。见 `memory/isv_ownership_redline.md` |

## 🟢 场景特有陷阱

### 陷阱 1：ORM.genLongId 主键未生成导致 save 失败

**症状**：在 beforeDoOperation 里新增了分录行但没设主键，save 时 ORM 报错。

**根因**：标品的 ORM.genLongId 兜底只在 save 阶段执行（FP_BDO5）。如果 ISV 在 beforeDoOperation（userimport/roleimport）里新增分录数据对象，主键仍为 0，后续 save 失败。

**修复**：新增分录行时调用 `ORM.create().genLongId(subEntityFormId)` 预分配主键，或等待 save 阶段的兜底逻辑。

### 陷阱 2：paintErrMarkCol 拿不到中文名 → 分录显示 propKey 机器码

**症状**：userfieldentry 或 rolefieldentry 分录中字段权限的 propName 显示为机器码（如 `src_staff_id`）而非中文名（如"员工ID"）。

**根因**：paintErrMarkCol 依赖 `PageCache.entityFields` 缓存。新增的实体类型或 propKey 不在缓存中，回填失败。

**修复**：确保 `ChoiceFieldPageCustomQueryService.parseProperty` 覆盖了新的实体类型。若实体尚未在 MetaDataCache 注册，需先在平台注册。

### 陷阱 3：finishinit 半成功 — 权限表部分写入

**症状**：finishinit 报错但部分权限数据已经落到下游表。

**根因**：finishinit 调用的 UserPermInitConvertService.convertRecord 或 PermRoleInitService.initRole 是批量操作，按用户/角色逐条写入权限表。中途某条失败不会回滚已写入的数据。

**修复**：标品 finishinit 前有 PermInitFinishValidateService 全局校验，正常情况不会半成功。ISV 在 finishinit 后加自定义落库应该用事务包裹。

### 陷阱 4：reimport 清空子分录后未保存 → 关闭页面数据丢失

**症状**：用户二次确认清空已有数据（reimport）后点了关闭，下次打开分录为空但主表仍存在。

**根因**：reimport 只调 `helper.deleteOne` 删除子分录行，不自动 save。若用户不清空后导入新数据直接关闭 → 主表 dealstatus=0 仍在，但子表无数据。

**规避**：标品 beforeClosed 会清理 dealstatus=0 的脏数据（包括清空后的空主表）。但最好在 reimport 后引导用户立即导入。

### 陷阱 5：inittype 切换后子表单不刷新

**症状**：用户从 userrole 模式切换到 role 模式，子表单仍显示 userrole 的分录。

**根因**：PermInitRecordEdit.beforeBindData 只在打开表单时根据 inittype 初始化一次（FP_BBD1），切换 inittype 后不会自动重渲染。

**修复**：监 inittype 的 propertyChanged 事件，触发手动刷新分录面板。

### 陷阱 6：大文件导入超时

**症状**：导入 1000+ 用户权限时，导入操作超时。

**根因**：userimport 是同步 UI 操作，Excel 解析 + 逐行回填到 4 张子分录耗时长。

**规避**：分批导入（每次 200-300 行）；或后台异步导入（需 ISV 扩展 sch_task 调度任务）。

## 🟢 快速诊断表

| 异常现象 | 可能原因 | 排查方向 |
|---|---|---|
| finishinit 按钮不显示 | dealstatus=1（已完成），FP_BBD3 自动隐藏 | 检查 dealstatus 字段值；若是未完成但被隐藏，检查 inittype 是否正确 |
| userimport 点不动 | registerListener 只注册了 userimport 的 ClickListener，未注册自定义按钮 | 继承 PermInitRecordEdit.registerListener 追加 addClickListeners |
| 导入后分录数据为空 | Excel 格式不匹配模板 | 先下载 dlusertemp/dlroletemp 模板对照 |
| 任务名重复提示 | 存在同名 dealstatus=1 的已完成任务 | checkTaskName 会清理 dealstatus=0 脏数据但不清理 dealstatus=1 的记录 |
| save 后 dealstatus 不变 | save 只写字段值不变 dealstatus；需点 finishinit 才变 dealstatus=1 | 设计如此，save≠完成初始化 |
| Export 无下载链接 | 异步导出任务未完成 | 监听 sch_task 回调的 taskInfo.isTaskEnd() |
