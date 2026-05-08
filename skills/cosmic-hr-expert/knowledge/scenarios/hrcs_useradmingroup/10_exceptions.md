# 异常诊断 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 5 反编译类实证 + cosmic_realworld_traps 共性陷阱 + rules_chain_all.json mines 字段
> **confidence**：verified
> **数据源**：CFR 反编译（2026-04-28）+ knowledge/cosmic_realworld_traps/*.md

---

## 一、共性陷阱（来自 cosmic_realworld_traps 仓库）

以下陷阱是本场景 ISV 二开中最高频的共性坑，源自 `knowledge/cosmic_realworld_traps/`。

### 陷阱 1 · buildMeta 不加 parentId 兜底到 bos 基础资料模板

**来源**：`buildmeta_parent_template_trap.md`
**场景**：ISV 给 hrcs_useradmingroup 建扩展元数据时，如果写 `buildMeta` 不传 `parentId`，苍穹会兜底到 `bos_basetpl` 基础资料模板。关联表会自动带 number/name/enable 等字段，与本场景的极简 2 字段形态冲突。

**症状**：新扩展元数据的 schema_text 比预期多出 number/name/status 等字段。

**修复**：`buildMeta` 显式传 `parentId` 指向正确的母表。本场景是中间关联表，不需要 number/name，parentId 应该传极简模板或直接不传 parentId 自己从零建。

### 陷阱 2 · modifyMeta 参数名传错静默走 TextField

**来源**：`modifymeta_param_names_and_hr_sdk_limits.md`
**场景**：ISV 用 `modifyMeta` 给 hrcs_useradmingroup 加字段时，传了 `dataType` 而不是 `fieldType`。结果：苍穹框架识别不了，静默回退到 `TextField`。

**症状**：界面上显示文本框而非预期的 MulComboField。

**修复**：正确参数名是 `fieldType`（不是 `dataType`），`name`（不是 `displayName`），`columnName` 可选（不传让平台自动生成）。详见 06_customization_solutions.md CS-01。

### 陷阱 3 · addRule 的 preCondition 不能用 `==''`

**来源**：`kb_cosmic_addrule_traps.md`
**场景**：ISV 想加一条规则"如果 tdkyw_admintag 为空则提示必填"，写 `preCondition: "tdkyw_admintag == ''"`。

**症状**：规则不触发。

**修复**：苍穹规则引擎的空值判断用 `isNullOrEmpty(tdkyw_admintag)`，不是 `== ''`。

### 陷阱 4 · EmployeeField 等 HR SDK 扩展类型 OpenAPI 不支持

**来源**：`modifymeta_param_names_and_hr_sdk_limits.md`
**场景**：ISV 想给 hrcs_useradmingroup 加字段时使用 `EmployeeField`（直接关联员工）。

**症状**：`modifyMeta` 调用成功（errorCode="0"），但 getFormSchema 拉回来是普通 BasedataField。

**修复**：EmployeeField 是 HR SDK 标注的扩展类型，OpenAPI 不能通过 modifyMeta 创建。需要通过 IDEA 插件 Web UI 或元数据同步工具处理。

---

## 二、本场景特有陷阱（来自反编译实证）

### 陷阱 5 · donothing_* opKey 上挂 Validator 不生效

**来源**：`rules_chain_all.json` mines 字段实证
**场景**：ISV 给 `donothing_add_user` 或 `donothing_remove_user` 的 onAddValidators 注册了 Validator。

**症状**：Validator 从未被调用。

**根因**：donothing_* opKey 由 FormPlugin.beforeDoOperation 拦截后不进入 OP 链——框架直接 `setCancel(true)` 或弹出子页面。onAddValidators 只对真正的 do_* OP 生效。

**修复**：将校验逻辑挂到 `do_add_user` / `do_remove_user` / `do_remove_group` 的 onAddValidators（真正的 OP opKey）。

### 陷阱 6 · 在 afterExecuteOperationTransaction 做关键校验

**来源**：PR-010 + rules_chain_all.json mines 字段
**场景**：ISV 在 `do_remove_group` 的 `afterExecuteOperationTransaction` 中反查 ISV 自建表，发现有引用后抛异常。

**症状**：标品 AdminGroupDelOp 的 9 表级联已完成（事务已提交），但 ISV 抛异常无法回滚 → ISV 表残留孤儿行。

**修复**：所有关键校验移到 `onAddValidators`（事务前）或 `beginOperationTransaction`（事务中）。`afterExecuteOperationTransaction` 只能做通知/日志等非关键操作。

### 陷阱 7 · 继承 HRAdminGroupTreeListPlugin 导致标品升级破坏

**来源**：PR-001 + form_lifecycle_rules.json
**场景**：ISV 写了 `class MyPlugin extends HRAdminGroupTreeListPlugin` 并重写 `beforeDoOperation`。

**症状**：标品升级后 HRAdminGroupTreeListPlugin 新增了生命周期方法或修改了 donothing 路由逻辑 → ISV 子类因为没调 super 或调 super 的时机不对而导致行为异常。

**修复**：ISV 必须继承 `AbstractTreeListPlugin`（不是 HRAdminGroupTreeListPlugin），并列注册到 hrcs_useradmingroup。

### 陷阱 8 · 改了 IPageCache 的 8 个标品 key

**来源**：form_lifecycle_rules.json treeNodeClick / setFilter 规则
**场景**：ISV 把 `focusNodeId` 重写为 `myApp_focusNodeId`。

**症状**：`donothing_add_user` / `donothing_remove_user` 等 OP 通过 OperateOption 拿不到 focusNodeId → 后续所有操作失败。

**修复**：标品的 5 个 key（focusNodeId / focusNodeParentId / focusAdgNumber / focusAdgName / focusNodeLongNumber）+ 3 个辅助 key（superiorGroupIds / currentUserInGroup / adminGroupCanSee）读写都是靠这些 key 名硬编码。ISV 必须新增自有 key（带 ISV 前缀），不能覆盖标品 key。

### 陷阱 9 · 删组前不反查 ISV 自建表导致孤儿数据

**来源**：rules_chain_all.json B_DRG_no_isv_cascade + AdminGroupDelOp 实证
**场景**：ISV 自建了 `tdkyw_admingroupext` 表引用 admingroup，但不挂 do_remove_group 的 Validator。

**症状**：管理员组被删后，ISV 表中仍有引用已删除 adminGroupId 的行 → 后续查询/展示时报"Reference not found"。

**修复**：在 `do_remove_group` 的 onAddValidators 中注册 Validator，反查 ISV 表——有引用则阻止删除，或在 beginOperationTransaction 中级联删除 ISV 表行。

### 陷阱 10 · refresh 不清 IPageCache

**来源**：rules_chain_all.json refresh mines 字段
**场景**：ISV 假设点"刷新"按钮后所有页面缓存被清空。

**症状**：refresh 后 IPageCache 中 `adminGroupCanSee` 等 key 还是旧值 → 用户列表过滤条件不更新。

**根因**：`HRAdminGroupTreeListPlugin.refresh()` 只调 `HRAdminGroupService.initAdminGroupTree` 重建树，不调 `pageCache.clear()`。

**修复**：如果 ISV 需要完整重置缓存，在自有 FormPlugin 的 `beforeDoOperation(refresh)` 或 `afterDoOperation(refresh)` 中显式 `pageCache.clear()` 或 `pageCache.remove(key)`。

---

## 三、异常码速查表

| 错误提示（i18n key） | 含义 | 触发源 | 排查方向 |
|---|---|---|---|
| "您无法访问该功能，因为您不是HR领域管理员。" (`HRAdminStrictPlugin_0`) | 当前用户不是 HR 管理员 | `HRAdminStrictPlugin.preOpenForm` 或 `HRAdminGroupTreeListPlugin.beforeDoOperation` | 检查用户是否在 HR 管理员组内；检查 HRAdminService.isHrAdmin() 返回值 |
| "仅允许创建%s级管理员。" (`HRAdminGroupTreeListPlugin_1`) | 管理员组层级达到上限 | `adminGroupTreeAddOperation` level >= getAdminLevelLimit() | 调整平台级配置 PermCommonUtil.getAdminLevelLimit() |
| "当前分组不在您的管控范围内..." (`HRAdminGroupTreeListPlugin_3`) | 操作的是上级组 | `verifyAdminGroup` superiorGroupIds 包含操作节点 | 确认用户的管理员组树结构；上级组只能查看不能操作 |
| "不允许删除管理员分组\"%s\"，该组已被角色引用。" (`HRAdminGroupTreeListPlugin_4`) | 权限角色引用了该组 | `checkHasRoleRef` 3 张表反查 | 先解除 perm_role / hrcs_roleopenscope / hrcs_roleassignscope 中的引用 |
| "选择一个非明细管理员分组。" (`HRAdminGroupTreeListPlugin_5`) | 批量授权目标是最明细组 | `verifyBatchAuth` longnumber 仅 1 条 | 选择有下级的非明细组做批量授权 |
| "删除失败。" (`AdminGroupTreeListPlugin_7`) | 9 表级联中出现 DB 异常 | `AdminGroupDelOp.beginOperationTransaction` | 检查 DB 连接；检查是否有外键约束阻止删除 |
| "成功删除%s条数据，以下分组不在您的管控范围内...:%s" | 跨组删用户部分成功 | `AdminGroupDelUserOp` delAll=false | 用户在不同管理员组中；部分组超出管控范围 |
| FOCUS_NODE_ID_IS_EMPTY（日志级别 INFO） | OP 拿不到 focusNodeId | `AdminGroupDelOp` / `AdminGroupDelUserOp` | 非 TreeList 触发；检查 OperateOption 是否正确传参 |

---

## 四、ISV 二开前自检清单

| 检查项 | 不通过 → 暂停 |
|---|---|
| 是否用了 EmployeeField / 其他 HR SDK 扩展类型？ | 是 → OpenAPI modifyMeta 不支持，换 IDEA 插件 |
| modifyMeta 参数传的是 `fieldType` 还是 `dataType`？ | dataType → 改 fieldType |
| 校验挂在 donothing_* 还是 do_* opKey？ | donothing_* → 改挂 do_* |
| 关键校验在 onAddValidators 还是 afterExecuteOperationTransaction？ | afterExecute → 移到 onAddValidators |
| 继承了场景专属类吗？ | 是 → 改为继承 AbstractTreeListPlugin / HRDataBaseOp |
| 覆盖了标品 IPageCache key 吗？ | 是 → 改用 ISV 前缀的 key |
| ISV 自建表引用 admingroup 吗？ | 是 → 加 do_remove_group 的 onAddValidators 反查 |
