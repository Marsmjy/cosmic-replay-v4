# 变更影响面 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 5 反编译类 + rules_chain_all.json + form_lifecycle_rules.json 实证
> **confidence**：verified
> **数据源**：CFR 反编译（2026-04-28）

---

## 一、影响面矩阵（改什么 → 影响什么）

### 1.1 改 hrCS_useradmingroup 元数据（加/改/删字段）

| 变更 | 直接影响 | 间接影响 | 级联动作 |
|---|---|---|---|
| 加 ISV 字段（如 `${ISV_FLAG}_admintag`） | `t_perm_useradmingroup` 物理表新增列 | OP 的 SaveServiceHelper.save 自动落新字段（无需改 OP） | 删管理员组时新字段随主行一起删（DynamicFormModel 默认 1:1 级联） |
| 改 `user` 字段的 refEntity | 用户 F7 引用目标变化 | `showUserF7TreeList` 中 5 道 DB 直查过滤仍指向 `bos_user` 物理表 → 不匹配导致 F7 空 | ❌ 不推荐改标品字段的 refEntity |
| 改 `usergroup` 字段的 refEntity | 管理员组树引用目标变化 | `treeNodeClick` 中 `BusinessDataServiceHelper.loadSingleFromCache` 失败 → 树节点点击后缓存空 → 后续 OP 拿不到 focusAdgNumber/focusAdgName | ❌ 不推荐改标品字段的 refEntity |
| 加 `MulComboField` | 同上 + 可能触发多语言表 `_l` | 本场景当前无 `_l` 表 → 行为取决于苍穹版本 | 详见 06_customization_solutions.md CS-01 踩坑 |

### 1.2 改 FormPlugin 插件配置（注册/注销/调序）

| 变更 | 直接影响 | 间接影响 |
|---|---|---|
| ISV 注销 `HRAdminStrictPlugin` | 整个 hrcs 11 个表单的 HR 准入闸失效 → 非 HR 管理员可进入 | 🔴 **禁止**——这是共用准入闸 |
| ISV 在 `HRAdminGroupTreeListPlugin` 前注册自定义 FormPlugin | ISV 的 `beforeDoOperation` 先于标品执行 | 标品的 `beforeDoOperation` 中二次 `isHrAdmin` 校验仍然生效（拦截发生在 ISV 之后）· 但 ISV 可能在标品拦截前操作数据 |
| ISV 在 `HRAdminGroupTreeListPlugin` 后注册自定义 FormPlugin | ISV 的 `afterDoOperation` / `closedCallBack` 后于标品执行 | ✅ 推荐——ISV 追加逻辑不干扰标品核心流程 |
| ISV 修改 `treeNodeClick` 监听顺序 | IPageCache 的 5 个 key（focusNodeId 等）的写入时序变化 | 可能导致 ISV FormPlugin 读到旧的缓存值 |

### 1.3 改 OP 插件配置（注册/注销/调序）

| 变更 | 直接影响 | 间接影响 |
|---|---|---|
| ISV 注销 `AdminGroupAddUserOp` | 添加用户不再写权限日志 + 不再清缓存 | `do_add_user` 仍然落库（SaveServiceHelper.save）· 但权限变更通知失效 → 用户下次访问可能拿旧权限 |
| ISV 注销 `AdminGroupDelOp` | 删管理员组不再级联清理 9 表 → 遗留孤儿数据 | 🔴 9 张标品表（perm_admingroup* / hrcs_admingroup*）会残留引用已删除组的数据 |
| ISV 在标品 OP 前注册自定义 OP | ISV OP 的 `beginOperationTransaction` 先于标品执行 | 同事务内，ISV 的级联删在标品之前做（顺序可控） |
| ISV 在标品 OP 后注册自定义 OP | ISV OP 的 `beginOperationTransaction` 后于标品执行 | ✅ 推荐——标品核心逻辑先跑，ISV 只做追加 cascade |

### 1.4 改管理员组树（perm_admingroup）

| 变更 | 直接影响 hrcs_useradmingroup |
|---|---|
| 改 `perm_admingroup.longnumber` 值 | 所有 TreeList 节点的 `focusNodeLongNumber` 缓存变化 → `verifyAdminGroup`（startsWith 前缀匹配）可能错判管控范围 |
| 改 `perm_admingroup.parent` 值 | 树结构变化 → `superiorGroupIds` 重算 → `donothing_remove_user` / `donothing_modify_group` 中上级组判定错误 |
| 改管理员级别上限 `getAdminLevelLimit()` | `donothing_add_group` 是否允许新增 → 过高可能导致树太深、按钮置灰→用户困惑 |
| 删 `perm_admingroup` 中 hrcs_useradmingroup 引用的行 | 右侧用户列表的 `usergroup.id` 指向已删除组 → QFilter `usergroup.id in (...)` 匹配不到 → 列表显示异常 |

---

## 二、下游耦合（改本场景 → 下游谁受影响）

### 2.1 权限系统（实时影响）

| 下游 | 耦合点 | 同步/异步 | 影响 |
|---|---|---|---|
| 用户表单权限 | `FormConfigFactory.cancelShowFormRights(userIds)` 在 `AdminGroupAddUserOp` / `AdminGroupDelUserOp` 中每次调 | 同步（add/remove 后立即执行） | 加/删管理员后，被操作的用户的表单权限菜单立即刷新 |
| 权限缓存 | `HRPermCacheMgr.clearAllCache()` 在 `afterDoOperation` 中每次调 | 同步 | 清空所有权限缓存——不仅仅是当前 form，整个 hrcs 的权限缓存都清 |
| 权限日志 | `HRAdminGroupService.adminEvent2PermLog(...)` 在 OP 中条件调（`isEnablePermLog()` 开关控制） | 同步 | 写权限操作审计日志 |

### 2.2 动态授权方案（hrcs_dynascheme · 强耦合）

| 下游 | 耦合点 | 同步/异步 | 为什么重要 |
|---|---|---|---|
| `hrcs_roleopenscope` | `checkHasRoleRef` 删组前反查 `admingroup = adminGroupId` | 同步（删组前） | 动态授权方案的角色启用范围引用了管理员组——如果组被删而 open scope 还在引用，授权方案失效 |
| `hrcs_roleassignscope` | `checkHasRoleRef` 删组前反查 `admingroup = adminGroupId` | 同步（删组前） | 动态授权方案的分配范围引用了管理员组 |
| `perm_role` | `checkHasRoleRef` 删组前反查 `createadmingrp = adminGroupId` | 同步（删组前） | 角色由本组创建——删组后角色没有管理者 |

⚠ 这是 **hrcs_dynascheme 与 hrcs_useradmingroup 的关键耦合点**。这 3 张表的反查逻辑在 `HRAdminGroupTreeListPlugin.checkHasRoleRef` 中（L500-L519），ISV 加新引用表时必须自建反查（详见 06_customization_solutions.md CS-04）。

### 2.3 管理员组明细页（hrcs_admingroupdetail）

| 下游 | 耦合点 | 影响 |
|---|---|---|
| `hrcs_admingroupdetail` 子页面 | `adminGroupTreeAddOperation` / `adminGroupTreeModifyOperation` 通过 `bsp.getCustomParams()` 传 adminGroupId/adminGroupParentId/level 给子页面 | 子页面依赖这 3 个 customParams——如果 TreeList 侧不传或传错，子页面无法正确初始化 |
| `hrcs_admingroupdetail` 的 VIEW/EDIT 双态 | `adminGroupTreeModifyOperation` 中 `superiorGroupIds.contains(adminGroupId)` 决定 OperationStatus | 上级组只能 VIEW → 子页面字段只读；下级组能 EDIT |

### 2.4 批量授权页（hrcs_amingroupbatchauth）

| 下游 | 耦合点 | 影响 |
|---|---|---|
| `hrcs_amingroupbatchauth` 主页签 | `adminGroupTreeBatchPermOperation` 通过 `bsp.getCustomParams().put("type", "batchAuth")` 传 mode | 子页面通过 type 参数区分批量授权模式 |

---

## 三、上游依赖（谁改了什么 → 本场景受影响）

| 上游变更 | 本场景影响 | 症状 |
|---|---|---|
| `bos_user` 表加 `usertype` 新值 | `showUserF7TreeList` 中 `usertype` 4-OR 链匹配 "1" 可能漏新类型 | 新类型的用户不出现在 F7 可选列表 |
| `bos_user` 改 `enable` 字段名 | 第 4 道过滤 `enable = "1"` 失败 | 所有用户在 F7 中消失 |
| `perm_admingroup` 改 `longnumber` 编码规则 | `verifyAdminGroup` 的 startsWith 前缀匹配逻辑失效 | 管理员看到自己无权操作的管理员组 |
| `perm_admingroup` 改主键生成策略 | `treeNodeClick` 中 `Long.parseLong(nodeId.split("_")[0])` 失败 | 树节点点击后 NPE → TreeList 右侧用户列表不刷新 |
| `PermCommonUtil.getAdminLevelLimit()` 平台层改小 | 现有层级超过新上限的管理员组仍可操作（只影响新增） | 不一致：旧组层级 5 级可看到根，但不能再新建第 6 级 |

---

## 四、已知生产事故案例

### 事故 1：HRAdminStrictPlugin 被 ISV 覆盖导致全 hrcs 权限洞穿

**诱因**：ISV 在 `hrcs_useradmingroup` 上注册了新的 `preOpenForm` 插件并设了更高优先级，但忘记调 `super.preOpenForm`。

**影响**：非 HR 管理员直接进入 hrcs 权限管理全部 11 个表单，可查看/修改他人管理员组。

**修复**：移除 ISV 的 preOpenForm 插件，恢复到标品 HRAdminStrictPlugin。ISV 的准入校验应通过 HRAdminService.isHrAdmin() 在 FormPlugin 层做二次兜底。

**教训**：绝不覆盖/注销 HRAdminStrictPlugin（PR-001 红线）。hrcs 11 表单共用准入闸，改一处坏所有。

### 事故 2：ISV 在 afterExecuteOperationTransaction 做关键校验导致删组后数据不一致

**诱因**：ISV 在 `do_remove_group` 的 `afterExecuteOperationTransaction` 中反查 ISV 自建表，发现引用后抛异常。

**影响**：标品 AdminGroupDelOp 的事务已提交（9 张表已删），但 ISV 抛异常无法回滚 → ISV 表残留引用已删除管理员的孤儿行。

**修复**：将反查逻辑移到 `onAddValidators`（事务前校验），不在 `afterExecuteOperationTransaction` 做关键判断。

**教训**：PR-010——afterExecuteOperationTransaction 阶段事务已提交，校验失败无法回滚。所有业务校验应在 onAddValidators 或 beginOperationTransaction 来做。

---

## 五、ISV 变更自查清单

| 变更前问自己 | 答案为空 → 暂停变更 |
|---|---|
| 我改的是标品字段还是 ISV 字段？ | 改标品字段（user/usergroup）→ 停止（PR-007 红线） |
| 我继承了 HRAdminGroupTreeListPlugin 或 AdminGroup*Op 吗？ | 是 → 停止（PR-001 红线）→ 改继承 AbstractTreeListPlugin / HRDataBaseOp |
| 我注销了 HRAdminStrictPlugin 吗？ | 是 → 停止（PR-001 红线） |
| 我的 ISV 自建表有 admingroup 引用吗？ | 是 → 必须在 do_remove_group 的 onAddValidators 加反查 |
| 我在 afterExecuteOperationTransaction 做校验了吗？ | 是 → 移到 onAddValidators 或 beginOperationTransaction（PR-010） |
| 我改了 IPageCache 的标品 key 吗？ | 是 → 用 ISV 前缀的 key 替代（如 ${ISV_FLAG}_focusNodeId） |
