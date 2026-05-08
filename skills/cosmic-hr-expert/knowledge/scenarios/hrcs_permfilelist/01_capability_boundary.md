# 能力边界 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译 PermfilesListPlugin + _auto_operations.md（23 opKey）+ ServiceHelper 调用矩阵
> **confidence**: verified
> **数据源**: CFR 反编译 · OpenAPI listOperations · 2026-04-28

---

## ⭐ 一、本场景定位 · 用户授权管理列表

**菜单路径**：HR通用服务 / 权限管理 / 用户授权
**主表单**：`hrcs_permfilelist` （列表型 · TreeListFormModel · 无字段）
**业务实质**：HR 域管理员管理"哪个用户在哪个 org 有什么角色"的中央台账

它**不是单据**（无 audit/submit 工作流）· **不是基础资料**（无 enable 启停字段）· **不是时序资料**（无 boid/iscurrentversion）—— 是**列表壳 + 主数据 hrcs_userpermfile + 分组树 hrcs_permfilegrp + 关联 hrcs_userrolerelat + hrcs_permfilegrpmember** 五件套形态。

---

## 二、✅ 已覆盖能力（标品支持 · 反编译实证）

### 2.1 数据浏览能力

| 能力 | 实现位置 | 范围 |
|---|---|---|
| 树+列表混合视图 | PermfilesListPlugin extends HRStandardTreeList | 左树（hrcs_permfilegrp）+ 右列表（hrcs_userpermfile） |
| 树根懒加载子节点 | registerListener · TreeNodeQueryListener · queryTreeNodeChildren L1024-L1027 | 按 parent=parentId 增量查询 |
| 子树过滤主列表 | nodeClickFilter L996-L1005 + buildNodeClickFilter L310-L329 | 用 longnumber.like + grpId in 二段查询 |
| 通用过滤器 | filterContainerInit L1007-L1016 | permfileenable 默认 "1"（生效） |
| 数据隔离过滤 | setFilter L1030-L1037 | admingroup level > 2 限定 org 范围 |
| 搜索框（按分组名） | updateSearch L257-L262 | "请输入权限档案组名称。" |
| 超链点击进编辑 | billListHyperLinkClick L967-L983 | 双闸权限校验决定 EDIT vs VIEW |

### 2.2 主数据 CRUD 能力

| 能力 | 入口 | 流转链路 |
|---|---|---|
| 新增（bar_new） | 工具栏【新增】 | beforeDoOperation L423 → showNewForm 弹 hrcs_userpermfile |
| 修改 | 列表行超链 | billListHyperLinkClick · setStatus(EDIT) |
| 查看 | 列表行超链（无修改权） | billListHyperLinkClick · setStatus(VIEW) |
| 生效（enable） | 工具栏【生效】 | PermFilesSaveOp · permfileenable="1" + clearAllCache |
| 失效（disable） | 工具栏【失效】 | PermFilesSaveOp + RoleServiceHelper.disablePermfile（级联） |
| 删除（delete） | 工具栏【删除】 | PermFilesSaveOp · 物理删 |
| 引入数据（importdata） | 工具栏【引入】 | 标品 importdata + writeLog |
| 引出数据（exportlist） | 工具栏【引出】 | 标品 exportlist |

### 2.3 高级业务能力

| 能力 | 入口 | 实现链 |
|---|---|---|
| 复制权限（copyperm） | 工具栏【复制权限】 | copyPerm L1140-L1163 · 三重校验 → 弹 hrcs_copyperm |
| 分配角色（assignrole） | 工具栏【分配角色】 | userAssignRole L567-L586 · 弹 hrcs_userassignrole |
| 批量分组（batchgroup） | 工具栏【批量分组】 | handleBatchGroup L531-L543 · 弹 hrcs_permfilegrptree |
| 按人事档案生成（syncperm） | 工具栏【按人事档案生成】 | genPermFiles → 异步 SyncPermFilesTask（kd.bos.schedule） |
| 初始化用户权限（inituserperm） | 工具栏【初始化用户权限】 | userPermInit · 跳 bos_list 显示 hrcs_perminitrecord |
| 引出用户权限（exportuserperm） | 工具栏【引出用户权限】 | exportUserPerm → 异步 PermFilesExportTask |
| 清空缓存（btn_clearcache） | 工具栏【清空缓存】 | HRPermCacheMgr.clearAllCache + clearAllManageCache |
| 刷新（refresh） | 工具栏【刷新】 | billList.refresh + 树节点重点击 |

### 2.4 分组管理能力

| 能力 | 入口 | 实现链 |
|---|---|---|
| 新建分组（btnnew） | 树工具栏 | addGroupNode L931-L939 · 弹 hrcs_permfilegrp 表单 |
| 编辑分组（btnedit） | 树工具栏 | editGroupNode L920-L929 · 含禁编辑根节点等校验 |
| 删除分组（btndel） | 树工具栏 | canDelGroup L902-L918 · 含禁删非空分组 + 二次确认 |

### 2.5 准入闸能力

| 能力 | 实现 | 范围 |
|---|---|---|
| HR 领域管理员准入 | HRAdminStrictPlugin.preOpenForm L29-L37 | 双闸 (isAdmin OR isCosmic) AND isHrAdmin · 失败弹"您无法访问该功能" |
| 修改权限校验（permItemId="4715a0df000000ac"）| validateModifyPermission L985-L993 | 决定超链点击是 EDIT 还是 VIEW |
| 复制权限三重校验 | checkCanCopy L1166-L1188 | user.isforbidden / 角色非空 / 操作员可见角色集 |
| 自己不能授权给自己 | checkDisable L660-L665 | 当前用户 ≠ 档案 user 才允许 |

---

## 三、🟡 边界能力（标品有 · 但容易踩坑）

### 3.1 列表过滤的"看不见"陷阱

`setFilter` L1032-L1036 自动加 `org.id in userPermFileIds` —— 当前用户的 admingroup level > 2（非顶级管理员）时 · 列表只能看到自己 admingroup 范围内的 org 下的档案。

⚠️ **不可见 ≠ 不存在** · 顶级管理员能看的档案数远多于普通管理员。ISV 做"看不见的下游引用"校验时（CS-04）要绕过 setFilter 直接查（`HRBaseServiceHelper.queryOriginalCollection` 不走 setFilter）。

### 3.2 缓存清理时机的不一致窗口

`HRPermCacheMgr.clearAllCache()` 在 OP 事务**外**调用（反编译 L779）· 如果 OP 事务后续步骤失败回滚 · DB 仍是旧值但缓存已清 · 短暂时间出现"DB 旧 + 缓存空"的不一致。

⚠️ ISV 不要在自定义 OP 里再加一次 clearCache · 如要加 · 必须放在 `afterExecuteOperationTransaction`（事务确认提交后）。

### 3.3 syncperm 任务的并发约束

`launchSyncPermfileJob` L1075-L1081 检查 `HRAppCache.get("syncPermFilesTaskId")` · 已有任务在跑就拒绝。

⚠️ **同一时间只能有一个 syncperm 任务**。如果用户在 A 浏览器起任务 · B 浏览器再起会报"已有后台执行任务，无需再执行。" 这是单实例约束 · 不是用户级。

### 3.4 disable 的级联失效是不可逆的

`setEnable("0", idList)` L767-L781 调用 `RoleServiceHelper.disablePermfile(idList)` 把所有关联角色失效 · enable 回来时**不会自动恢复角色绑定** · 需要重新分配角色。

⚠️ 这是业务设计 · 不是 bug。ISV 自定义"软删除"逻辑时不要照搬此模式 —— 看业务是否要求"恢复时角色一起恢复"。

### 3.5 标品 PermFilesSaveOp jar 缺失

OpenAPI listOperations 显示 `PermFilesSaveOp` 挂在 save / enable / delete / disable_new opKey 上 · 但反编译 jar 不可得（_auto_plugin_registry.md L14 标 `decompileError: jar_not_found`）。

⚠️ ISV 写 OP 校验/扩展时 · 不能假设 PermFilesSaveOp 内部细节。**保持 PR-001 并列挂 · 不继承** · 不依赖其内部状态。

---

## 四、⚠️ 不覆盖（已知限制 · 重要）

### 4.1 不支持单据流

`hrcs_userpermfile` **不是单据**（无 audit/unaudit/submit/unsubmit 工作流虽然 opKey 注册表有占位但实际未启用）。如果客户要求"用户授权要走审批" · 标品不支持 · ISV 必须自建：
- 自建一个 ISV 单据 · 走苍穹 BPM
- 单据审批通过后 · 由审批后置 OP 把数据 INSERT 到 hrcs_userpermfile（不能直接放 BPM 里）

### 4.2 不支持时序版本控制

`hrcs_userpermfile` 不带 `boid / iscurrentversion / sourcevid / bsed / bsled`。如果业务要求"档案变更要保留历史" · 标品不支持。

⚠️ 替代方案：
- 在 hrcs_userrolerelat 上做版本（已有 validstart/validend 时段字段）
- 或自建 ISV 历史表跟踪 hrcs_userpermfile 变更（OP 后置写）

### 4.3 不支持业务事件中心（BEC · PR-011）

grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录 **0 命中**。标品不发 BEC · 下游想监听"权限档案失效"必须 ISV 自建（CS-05）。

### 4.4 不支持自定义 sourcetype 的角色关联

`hrcs_userrolerelat.sourcetype` 字段在动态方案场景（hrcs_dynascheme）用 `"4"` 区分"由动态方案分配" · 在本场景**没看到**显式区分。ISV 加自己的 sourcetype 值要协调（避免 1-9 已用区间）。

### 4.5 列表搜索框只能按分组名搜

`updateSearch` L260 设 `setSearchEmptyText("请输入权限档案组名称。")` —— 搜索框只对树节点过滤生效 · 不直接搜列表（hrcs_userpermfile 行）。

⚠️ 用户经常误以为可以搜用户姓名 · 实际不能。要支持搜用户姓名需 ISV 自定义搜索（CS-06）。

### 4.6 删除分组的不级联

`canDelGroup` L902-L918：分组下有子分组 → 不允许删；分组下有 member（关联档案）→ 不允许删。**没有"级联删除"选项**。

⚠️ 如果客户要求"组织调整 · 一键删除整棵分组子树" · 标品不支持 · ISV 必须自建：
- 自建 ListPlugin 在 btndel 拦截
- 弹自定义二次确认
- 自己控制级联删除顺序（先子后父 · 先 member 后分组）

---

## 五、版本差异（待考证）

| 苍穹版本 | 行为差异 | 来源 |
|---|---|---|
| 2024R1 / 2025 | 当前反编译来自 2025R1（jar 标记 1.0）· 早期版本可能没有某些 opKey（btn_initdata/copyperm 是后加的） | 推断 · 待版本确认 |
| HR 8.0 / 9.0 | HRPermCacheMgr / HRPermServiceHelper API 在不同版本可能签名不同 | _shared/_decompiled 待对照 |

---

## 六、扩展空间矩阵（导向 06 CS）

| 维度 | 标品能力 | ISV 扩展点 | 推荐 CS |
|---|---|---|---|
| 字段扩展 | hrcs_userpermfile 11 字段 | modifyMeta add field | CS-01 |
| 字段联动 | 标品无（基础资料无字段联动） | propertyChanged FormPlugin | CS-02 |
| 操作前置校验 | 仅 3 个 FormValidate | 自建 Validator + onAddValidators | CS-03 |
| 下游引用查询 | 无 | 自建 Validator 查 hrcs_userrolerelat 反向 | CS-04 |
| 跨模块通知 | 无 BEC | 自建 OP 发 BEC | CS-05 |
| 列表过滤定制 | setFilter 已加 admingroup 限制 | 自建 ListPlugin · super 后追加 | CS-06 |
| 批量授权扩展 | batchgroup 仅写中间表 | 自建 ListPlugin 拦截 batchgroup | CS-07 |

---

## 七、能力边界总结

| 维度 | 标品能力 | 评分 |
|---|---|---|
| 浏览/查询 | 树+列表+过滤+搜索+超链 | 9/10 |
| CRUD | bar_new/save/enable/disable/delete | 8/10 |
| 高级业务 | 复制/分配/批量/异步任务 5 大流程 | 9/10 |
| 准入控制 | HRAdminStrictPlugin 双闸 | 10/10 |
| 单据流 | 不支持 | 0/10 |
| 时序版本 | 不支持 | 0/10 |
| 跨模块通知 | 不支持（无 BEC） | 0/10 |
| 缓存一致性 | 主动 clearAllCache · 有不一致窗口 | 5/10 |
| 性能（大数据） | longnumber.like 子树查询 + 懒加载 · 一般 | 7/10 |

ISV 的扩展工作主要集中在"单据流（4.1）"·"时序版本（4.2）"·"跨模块通知（4.3）"·"批量级联（4.6）" 4 大空白上 · 以及前面列出的 CS-01~07 七大常见定制点。
