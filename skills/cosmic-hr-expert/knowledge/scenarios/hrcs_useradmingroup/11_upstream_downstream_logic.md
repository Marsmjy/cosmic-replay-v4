# 上下游联动 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 5 反编译类实证 + rules_chain_all.json + scene_doc.json + 02_business_rules.md 角色反查规则
> **confidence**：verified
> **数据源**：CFR 反编译 HRAdminGroupTreeListPlugin / AdminGroupDelOp（2026-04-28）

---

## 一、数据流向全景

hrcs_useradmingroup 作为 HR 权限管理的"用户-管理员组"关联枢纽，其上下游关系网如下：

```
上游（数据来源）
├── bos_user                    → 用户 F7 选项（user 字段 refEntity）
├── perm_admingroup             → 左树管理员组数据源（usergroup 字段 refEntity · treeNodeClick 加载）
├── HRAdminGroupService         → initAdminGroupTree 初始化树结构
└── PermCommonUtil              → 平台级权限配置（adminLevelLimit / permLog / authorityChangeNotice）

    ┌─────────────────────────────────────────┐
    │        hrcs_useradmingroup               │
    │    (用户 × 管理员组 多对多关联)            │
    │    TreeList：左树 + 右列表                │
    └─────────────────────────────────────────┘

下游（消费方 / 级联影响）
├── 权限系统
│   ├── FormConfigFactory.cancelShowFormRights  → 用户表单权限刷新（同步）
│   ├── HRPermCacheMgr.clearAllCache            → 全局权限缓存清空（同步）
│   └── PermAdminLogHelper.adminEventImage      → 权限审计日志写入（条件同步）
├── 管理员组明细（hrcs_admingroupdetail）
│   └── customParams 三件套（adminGroupId/adminGroupParentId/level）→ 子页面初始化
├── 批量授权（hrcs_amingroupbatchauth）
│   └── customParams: type=batchAuth → 子页面模式识别
├── 动态授权方案（hrcs_dynascheme 耦合点）
│   ├── hrcs_roleopenscope.admingroup           → 角色启用范围引用
│   ├── hrcs_roleassignscope.admingroup         → 角色分配范围引用
│   └── perm_role.createadmingrp                → 角色创建者引用
└── 9 表级联（do_remove_group 触发）
    ├── perm_admingroupfunperm                  → 功能权限（usergroup 过滤）
    ├── perm_admingroupbizunit                  → 业务单元（usergroup 过滤）
    ├── perm_admingrouporg                      → 行政组织（usergroup 过滤）
    ├── perm_admingroupapp                      → 应用（usergroup 过滤）
    ├── perm_admingroupadduser                  → 可加用户范围（usergroup 过滤）
    ├── perm_admingroup                         → 主表（id 过滤）
    ├── hrcs_admingrouporg                      → hrcs 组织扩展（id 过滤）
    ├── hrcs_admingroupfunc                     → hrcs 功能扩展（id 过滤）
    └── hrcs_admingroupfile                     → hrcs 档案扩展（id 过滤）
```

---

## 二、上游联动详细说明

### 2.1 bos_user（用户基础资料）

**联动点**：`user` 字段 → `bos_user` F7 基础资料引用（physicalColumn = `fuserid`）

**联动时机**：
- `donothing_add_user` → `showUserF7TreeList` 弹 bos_user 列表 F7 → 用户选择 → `closedCallBack` → 构造 hrcs_useradmingroup DO 写 `user.id = selectedUserId`

**约束规则**：
- `enable = "1"`（只显示启用状态用户）
- `usertype` 4-OR 链匹配 "1"（特殊用户类型代码，逗号分隔多值）
- 排除当前组已有用户（DB 直查 `t_perm_useradmingroup`）
- 排除当前用户自己（`RequestContext.get().getCurrUserId()`）
- 排除同 adminScheme 不同 adminType 的用户

**ISV 扩展入口**：`IAdminGroupListSubExtPlugin.beforeAddCustomUser`（SDK 扩展点，在标品 5 道过滤之后调用）

### 2.2 perm_admingroup（管理员组树）

**联动点**：`usergroup` 字段 → `perm_admingroup` 基础资料引用（physicalColumn = `fadmingroupid`）

**联动时机**：
- `initializeTree` → `HRAdminGroupService.initAdminGroupTree` 装载整棵树
- `treeNodeClick` → `BusinessDataServiceHelper.loadSingleFromCache(adminGroupId, "perm_admingroup", "number,name,longnumber")` 缓存 5 个 IPageCache key
- `donothing_modify_group` → `HRBaseServiceHelper.queryOne("perm_admingroup", "number,name,description")` 拿明细页标题
- `donothing_remove_group` → `BusinessDataServiceHelper.loadSingle("perm_admingroup", "parent=" + adminGroupId)` 反查下级
- `donothing_batch_perm` → `HRBaseServiceHelper.queryOriginalCollection("perm_admingroup", "id", "longnumber like %X%")` 判断是否有子节点

**约束规则**：
- 节点 id 格式：`<adminGroupId>_<level>`，其中 adminGroupId 是 perm_admingroup 的数字主键
- 根节点 id 是固定 UUID：`8609760E-EF83-4775-A9FF-CCDEC7C0B689`
- 层级上限由 `PermCommonUtil.getAdminLevelLimit()` 平台级配置决定（默认 5 级）

### 2.3 PermCommonUtil（平台级权限配置）

| 配置项 | 方法 | 影响本场景的行为 |
|---|---|---|
| 管理员级别上限 | `getAdminLevelLimit()` | `donothing_add_group` 是否允许新增（level >= limit 拒） |
| 权限日志开关 | `isEnablePermLog()` | add/remove 用户时是否写权限审计日志 |
| 权限变更通知 | `isEnableAuthorityChangeNotice()` | add/remove 用户后是否清空表单权限缓存 |
| 是否是平台管理员 | `isAdminUser(uid)` | `HRAdminStrictPlugin.preOpenForm` 第 1 层判定 |
| 是否是 cosmic 用户 | `isCosmicUser(uid)` | `HRAdminStrictPlugin.preOpenForm` 第 1 层判定 |

---

## 三、下游联动详细说明

### 3.1 权限系统（实时同步）

**联动点 1**：用户表单权限刷新
- **触发时机**：`AdminGroupAddUserOp.beginOperationTransaction` / `AdminGroupDelUserOp.beginOperationTransaction`
- **执行逻辑**：`FormConfigFactory.cancelShowFormRights(userIds)` —— 清空被加/删用户的表单权限缓存
- **同步/异步**：同步（OP 事务中执行）
- **失败策略**：异常被 catch 后吞掉（不影响主业务）
- **影响范围**：仅被操作的用户（不是全部用户）

**联动点 2**：全局权限缓存清空
- **触发时机**：`HRAdminGroupTreeListPlugin.afterDoOperation`（do_remove_user 成功后）
- **执行逻辑**：`HRPermCacheMgr.clearAllCache()` —— 清空全部 hrcs 权限缓存
- **同步/异步**：同步（afterDoOperation 阶段）
- **影响范围**：全部用户——所有人在刷新前可能读到旧权限

**联动点 3**：权限审计日志
- **触发时机**：`AdminGroupAddUserOp.beginOperationTransaction` / `AdminGroupDelUserOp.beginOperationTransaction`
- **执行逻辑**：`PermAdminLogHelper.adminEventImage` + `adminEvent2PermLog` + 写 `EnumPermBusiType.ADMIN_ADD` / `ADMIN_DEL`
- **同步/异步**：条件同步（`isEnablePermLog()` 控制）
- **失败策略**：异常被 catch 后吞掉

### 3.2 动态授权方案（强耦合 · 删除前阻断）

三个下游表的引用反查在 `checkHasRoleRef` 方法中（L500-L519），全部在删组前（donothing_remove_group）执行：

| 下游表 | 过滤条件 | 查询服务 | 引用语义 |
|---|---|---|---|
| `perm_role` | `createadmingrp = adminGroupId` | `PermDBServiceHelper.roleDBService.isExists` | 角色创建者引用——删组后角色无管理者 |
| `hrcs_roleopenscope` | `admingroup = adminGroupId` | `HRBaseServiceHelper.isExists` | 动态授权方案的角色启用范围——删组后方案范围失效 |
| `hrcs_roleassignscope` | `admingroup = adminGroupId` | `HRBaseServiceHelper.isExists` | 动态授权方案的分配范围——删组后分配逻辑错误 |

**关键认知**：这是 hrcs_useradmingroup 与 hrcs_dynascheme 的关键耦合点。任一引用存在 → `showTipNotification` 阻断删除 → 用户需要先去解除角色引用。

**ISV 新增引用表**：如果 ISV 自建表（如 `${ISV_FLAG}_admingroupext`）也引用了 adminGroupId，必须自建反查——标品 `checkHasRoleRef` 不会自动扫描 ISV 表。

### 3.3 管理员组明细页（hrcs_admingroupdetail）

**联动点**：子页面参数传递
- **触发时机**：`donothing_add_group` / `donothing_modify_group`
- **传递参数**（通过 `bsp.getCustomParams()`）：
  - `adminGroupId`：当前树节点对应的 perm_admingroup.id
  - `adminGroupParentId`：父节点 id（level==1 时强制为 "0"）
  - `level`：当前树的层级
  - `viewstatus`："0"（EDIT 可编辑）/ "1"（VIEW 只读）——由 superiorGroupIds 决定
- **pageId 规则**：`adminGroupId + "|" + parentPageId` —— 防止重复打开同一管理员组的明细页

**同步/异步**：异步（弹出独立主页签，不与 TreeList 共享状态）

### 3.4 批量授权页（hrcs_amingroupbatchauth）

**联动点**：子页面模式传递
- **触发时机**：`donothing_batch_perm`
- **传递参数**：`type = "batchAuth"`（区别于其他打开此子页面的模式）
- **前提校验**：`verifyBatchAuth` 三道（非根 / 有下级 / 在管控范围）

### 3.5 9 表级联（do_remove_group · 事务内执行）

所有级联删除在 `AdminGroupDelOp.beginOperationTransaction` 中同一事务执行：

```
DeleteServiceHelper.delete("perm_admingroupfunperm",   QFilter("usergroup", "=", adminGroupId))
DeleteServiceHelper.delete("perm_admingroupbizunit",   QFilter("usergroup", "=", adminGroupId))
DeleteServiceHelper.delete("perm_admingrouporg",       QFilter("usergroup", "=", adminGroupId))
DeleteServiceHelper.delete("perm_admingroupapp",       QFilter("usergroup", "=", adminGroupId))
DeleteServiceHelper.delete("perm_admingroupadduser",   QFilter("usergroup", "=", adminGroupId))
DeleteServiceHelper.delete("perm_admingroup",          QFilter("id", "=", adminGroupId))
DeleteServiceHelper.delete("hrcs_admingrouporg",       QFilter("id", "=", adminGroupId))
DeleteServiceHelper.delete("hrcs_admingroupfunc",      QFilter("id", "=", adminGroupId))
DeleteServiceHelper.delete("hrcs_admingroupfile",      QFilter("id", "=", adminGroupId))
```

**关键认知**：
- 前 5 张表用 `usergroup = adminGroupId` 过滤（它们引用的是 hrcs_useradmingroup 的 usergroup 字段）
- 后 4 张表（包括 perm_admingroup 主表）用 `id = adminGroupId` 过滤（它们的 id 就是 adminGroupId 本身）
- 任何一张表 delete 失败 → 整个 try 块抛 `KDBizException("删除失败。")` → 事务回滚 → 9 表协同回滚
- ISV 自建表不会被自动级联——必须 ISV 自建 OP 在 beginOperationTransaction 阶段追加级联

---

## 四、跨模块影响速查

| 联动方向 | 模块 | 联动类型 | 同步/异步 | 失败后果 |
|---|---|---|---|---|
| 上游 ← | bos_user | F7 数据源 | 按需查询 | F7 空列表 |
| 上游 ← | perm_admingroup | 树数据源 | 按需查询 | TreeList 无法加载树 |
| 上游 ← | PermCommonUtil | 平台配置 | 启动时读取 | 层级上限/日志开关失效 |
| 下游 → | FormConfigFactory | 用户表单权限 | 同步 | 被吞（不影响主业务） |
| 下游 → | HRPermCacheMgr | 全局权限缓存 | 同步 | 用户看到旧权限 |
| 下游 → | PermAdminLogHelper | 权限审计日志 | 条件同步 | 被吞 |
| 下游 → | hrcs_admingroupdetail | 子页面初始化 | 异步（弹窗） | 子页面无法正确初始化 |
| 下游 → | hrcs_amingroupbatchauth | 批量授权 | 异步（弹窗） | 子页面模式错误 |
| 下游 → | perm_role / hrcs_roleopenscope / hrcs_roleassignscope | 角色引用反查 | 同步（删组前阻断） | 阻止删除（showTipNotification） |
| 下游 → | 9 张标品表 | 级联删 | 同步（同一事务） | KDBizException + 事务回滚 |

---

## 五、ISV 新增联动自查

| 新增联动类型 | 推荐做法 | 不推荐做法 |
|---|---|---|
| ISV 自建表引用 adminGroupId | do_remove_group OP 的 onAddValidators 反查 + beginOperationTransaction 级联删 | afterExecuteOperationTransaction 做级联 |
| ISV 跨系统通知 | do_add_user / do_remove_user OP 的 afterExecuteOperationTransaction + REST API | 自建 BEC 发布（标品 0 订阅方 · 无生态价值） |
| ISV 表单联动 | ISV 自建 FormPlugin 挂 hrcs_useradmingroup · propertyChanged | 继承 HRAdminGroupTreeListPlugin 重写 |
| ISV 缓存联动 | 用自己的 IPageCache key（带 ISV 前缀） | 覆盖标品 focusNodeId 等 8 个 key |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
