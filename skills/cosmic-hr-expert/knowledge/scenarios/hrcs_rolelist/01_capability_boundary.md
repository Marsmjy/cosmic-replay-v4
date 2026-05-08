# 能力边界 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类 + opkeys_index.json 15 opKey + main_form.xml 实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、场景定位

`hrcs_rolelist`（HR 角色管理列表）位于 **HR 通用服务（hrcs）/ 权限管理 / 角色管理** 菜单 · 是 HR 权限体系**角色生命周期 + 用户分配 + 批量授权 + 导入导出**的运营入口。区别于其他权限场景：

- **本场景**：HR 角色清单 + 角色批量启用/禁用/删除 + 跳转矩阵编辑
- `hrcs_modifyrole`：单角色权限矩阵编辑（功能/数据/字段三大权限）—— 本场景**点击行 hyperLink 后跳转的下游表单**
- `hrcs_userrolerelat`：用户-角色关联（手动赋权 · 用户视角）
- `hrcs_dynascheme`：动态授权方案（按规则批量赋权 · 规则视角）—— 本场景**反查依赖**（CS-04）

**主表单**：`hrcs_rolelist`（QueryListModel · 不是 BillFormModel · 没有 status/audit）
**ModelType**：QueryListModel（main_form.xml L9）
**EntityId**：`0SXDKS4AO4Q0` · **FormId**：`0SXDNZK1PW66`
**应用**：HR 通用服务（hrcs · BizappId `15NPDX/GJFOO`）
**路径**：scene_doc `hrcs_rolelist`（菜单挂载点 · 见 menu_path.json）

---

## 二、✅ verified（已覆盖能力）

### 2.1 标品支持的核心动作（15 opKey · 详见 opkeys_index.json）

| 类别 | opKey 清单 |
|---|---|
| **基础 CRUD** | `new`（跳 hrcs_newrole） / `modify`（跳 hrcs_modifyrole · billListHyperLinkClick L959） / `delete`（23 表级联） / `refresh`（清选 + 刷新） / `save`（QueryListModel 不存数据 · 此 opKey 实际无业务） |
| **状态管理** | `enable`（启用 · 同步 BizRoleComRole） / `disable`（禁用 · 异步清缓存） |
| **批量授权** | `assign`（btn_allocprem 批量分配权限 · 跳 hrcs_modifyrole） / `bar_assignmember`（分配成员 · 跳 hrcs_modifyrole + useroperation=assignmember） |
| **复制** | `option`（实际是 bar_copy · 跳 hrcs_modifyrole + copy=1） |
| **导出** | `exportlist`（标品列表导出） / `exportdetails`（导出角色权限 · 跳 hrcs_exportperm + RoleExportTask 调度） |
| **导入** | `importdata`（标品列表导入） |
| **关闭/返回** | `close` / `returndata`（F7 模式回填） |
| **空操作** | `donothing`（占位） |

⚠️ **opkeys_index.json 是 15 个**（save/close/disable/enable/modify/refresh/donothing/exportlist/new/delete/assign/importdata/returndata/exportdetails/option），但实际 `main_form.xml` + `HRRoleListPlugin.beforeDoOperation` 还有 7 个**未在 opkeys_index 体现**的工具栏 itemKey：
- `bar_assignmember`（分配成员）
- `exportroleperm`（导出用户权限）
- `inituserperm`（用户权限初始化 · 跳 hrcs_perminitrecord）
- `bar_initrole`（角色权限初始化）
- `bar_exportrole`（导出角色权限）
- `bar_copy`（复制 · 实际由 option 间接体现）
- `baritem_alteruserperm`（修改用户权限）
- `btn_batchgroup`（批量分组）
- `btnnew/btnedit/btndel`（树工具栏）

这些 itemKey 都在 `HRRoleListPlugin.handleBeforeOperatetionEvent` (L1012-L1072) switch case 处理。

### 2.2 标品自带规则

- **HR 域管理员准入闸**：`HRAdminStrictPlugin.preOpenForm` 三层校验（isAdminUser + isCosmicUser + isHrAdmin）
- **行级权限闸**：`setFilter` 加 `id IN viewableRoles(currUserId)` 过滤
- **默认显示已启用**：`filterContainerInit` 设 `perm_role.enable=1`
- **角色分组 F7 二级过滤**：`filterContainerBeforeF7Select` 限 hrcs_rolegrp 注册过的
- **删除 4 重前置校验**：`checkCanDel`（预置/被引用/管理员权/启用状态）
- **删除 23 SQL 级联**：`doDeleteBosRole`（7 表）+ `doDeleteHrRole`（16 表）双事务
- **启用/禁用同步 BizRoleComRole**：裸 SQL 在主事务内
- **3 套日志**：业务日志 + 权限信息日志 + 状态切换日志，全用批量提交
- **行存活校验**：beforeItemClick 单选时跑 `checkRoleInfoExist`，并发删除可被发现
- **复制 4 重权校验**：checkCanCopy（masterGroup / topAdminGroup / opensope / assignscope ismodifiable=1）
- **F7 lookUp 例外**：`HRAdminStrictPlugin` 在 `isLookUp()=true` 时跳过准入（普通用户用 F7 选角色场景）

### 2.3 标品自带集成点

- **下游 hrcs_userrolerelat**：删除时清空（doDeleteHrRole L619-L623）
- **下游 hrcs_dynascheme**：反查 `roleentry.role` 阻止删除（getRefrencedRoles L1556）
- **下游 hrcs_modifyrole**：行点击 hyperLink 跳转编辑（billListHyperLinkClick L959）
- **下游 hrcs_perminitrecord**：bar_initrole / inituserperm 跳转（rolePermInit/userPermInit）
- **下游 hrcs_exportperm**：bar_exportrole / exportroleperm 跳转（showProgressForm + TaskInfo 回调）
- **缓存清理**：HRPermCacheMgr 异步清 + CacheMrg 同步清（删除时每个 roleId 都清）
- **业务变更类型同步刷**：`T_Perm_BizRoleComRole.FEnable` 跟着 enable/disable 同步（裸 SQL）

---

## 三、🟡 likely（标品支持 · 待业务确认）

- **批量分组 (btn_batchgroup)**：`isNoOneSelected` 检查后通过 · 真正动作位于 hrcs_modifyrole 或独立子页面（待探针子页面）
- **批量分配权限 (btn_allocprem)**：beforeItemClick 不校验（L840-L842 fallthrough）· 真正校验由 hrcs_modifyrole 编辑页处理
- **修改用户权限 (baritem_alteruserperm)**：`isOnlyOneSelected` 必选 1 行 · 跳转目标待探针
- **导出权限 export taskinfo 回调**：`closedCallBack` 接收 taskInfo · 调 `clientViewProxy.addAction("download", url)` 触发浏览器下载（L1144-L1146）
- **新建角色路径**：beforeDoOperation new → openNewRolePage → `FormModel("hrcs_newrole", null, "1")` + `groupId` 填当前节点（L1171-L1180）
- **F7 lookUp 跳过准入闸**：业务上含义 = "其他模块用 F7 选角色时不要拒非 HR 管理员"

---

## 四、⚠️ unverified（需专家确认）

- [ ] `hrcs_rolegrp` 树节点删除/编辑后 · 已挂的角色去哪？（看 `doDeleteGroup` 只删 hrcs_rolegrp 行 · 不动 perm_role.group · 是否会"孤儿"？）
- [ ] 启用/禁用时如果角色已有 hrcs_userrolerelat 关联 · 用户那侧的 enable 是否同步？（看不到这部分代码 · 推测在 hrcs 下游平台插件链）
- [ ] 复制角色后 hrcs_role 的 HR 域字段是否会复制？（实际复制逻辑在 hrcs_modifyrole + copy=1 模式 · 不在本场景）
- [ ] `isCosmicUser` 究竟判断什么？（PermCommonUtil 内部 · 推测 = 是否签了苍穹平台账号 · 不是简单 admin）
- [ ] 多语言：12 个工具栏按钮的 caption 在哪个 multi-lang 包？（HRRoleListPlugin_xx 系列）
- [ ] `T_Perm_BizRoleComRole` 表：FRoleID + FEnable 是 BOS 内部表 · 业务侧用途待文档确认（推测：业务角色复用关系 · 跨企业/跨集团角色"借用")

---

## 五、不覆盖（已知限制）

### 5.1 业务限制

- ❌ **不支持工作流审核**：本场景是 QueryListModel · 没有 submit/audit/unaudit · 角色启用/禁用/删除直接生效
- ❌ **不支持时序版本**：本场景非 HisModel · 改了就改了 · 不可回溯（详见 03 第六节）
- ❌ **不支持复制成"启用态"角色**：copyRole L1200 拦截 enable=true（必须先禁用源角色再复制 · 业务上 UX 反直觉但是标品行为）
- ❌ **不支持批量复制**：copyRole L1190 强制单选
- ❌ **不支持普通用户访问**：HRAdminStrictPlugin 三层闸拦死（除 isLookUp）
- ❌ **不支持自定义"已禁角色仍可被分配"**：分配成员 (bar_assignmember) 走 validateAssignMember L941 → checkRoleEnable 强制 enable=1（"请先启用角色"）
- ❌ **不支持"包含下级分组的分组"删除**：canDelGroup L539 强制叶子节点

### 5.2 元数据限制

- ❌ **不支持 ISV 直改 perm_role 元数据**：标品 form · ISV 隔离铁律 PR-001 · 必须建 ISV 扩展元数据继承
- ❌ **不支持 ISV 直改 hrcs_role**：标品 HR 域元数据 · 同上
- ❌ **不支持把 QueryListModel 改成 BillFormModel**：架构级改动 · 苍穹元数据模型不支持运行时切换
- ⚠️ **MuliLangTextField _l 表**：取决于 ParentId 配置 · ISV 加多语言字段时不要假设默认建 _l 表

### 5.3 性能限制

- ⚠️ 单批删除 ≥ 50 角色：异步清缓存延迟显著 · 用户立即点【刷新】可能看到旧数据
- ⚠️ 单角色删除：1-3 秒（23 SQL + 双事务）· 不便宜
- ⚠️ ISV 自定义批操作 ≥ 500 行：撞 ORM 默认事务超时（30s）· 必须分批

### 5.4 反编译能力限制

- 反编译看到的是**字节码 + cfr 0.152 反推的 Java 源码**，不是原始源码
- 部分注解（@SdkPublic / @SdkPlugin）需在 jar 内查 · 可能丢失（参考 `cosmic_sdk_annotation_whitelist.md`）
- 内部类 `HRRoleProvider`（L1762 + L122）反编译可能不完整 · ISV 不要尝试模仿

---

## 六、能力速查表（业务侧动作 → opKey → 风险 → CS 关联）

| 业务侧动作 | 触发 opKey | 数据风险 | 推荐 CS |
|---|---|---|---|
| 新增角色 | `new` → 跳 hrcs_newrole | 创建链不在本场景 | （CS-01 加扩展字段同步影响新增） |
| 删除角色 | `delete` → 23 SQL 级联 | 高（含下游引用） | CS-03 onAddValidators / CS-04 反查 |
| 启用/禁用 | `enable` / `disable` | 中（异步缓存延迟） | CS-03 onAddValidators |
| 复制角色 | `bar_copy` (option) → 跳 hrcs_modifyrole | 低（仅跳转） | （不需要扩展） |
| 分配成员 | `bar_assignmember` → 跳 hrcs_modifyrole | 中（涉及 hrcs_userrolerelat） | CS-04 反查依赖 |
| 批量授权 | `btn_allocprem` → 跳 hrcs_modifyrole | 中 | CS-07 批量授权扩展 |
| 导出权限 | `bar_exportrole` / `exportroleperm` | 低 | （标品已支持） |
| 列表过滤 | setFilter | **高（行级权限失误会泄露）** | CS-06 列表过滤定制 |
| 加自定义字段 | (元数据级) | 中（必须 ISV 扩展继承） | CS-01 加扩展字段 |
| 字段联动 | (FormPlugin propertyChanged) | 低 | CS-02 字段联动 |
| 加自定义按钮 | (元数据 BarItem + ListPlugin) | 中（要在 beforeItemClick 注册） | （混入 CS-03/CS-07） |

---

## 七、跟同应用其他场景对比

### 7.1 hrcs_rolelist vs hrcs_dynascheme

详见 `03_model_design.md` 第八节。简而言之：**rolelist 是列表、dynascheme 是单据**；**rolelist 没工作流、dynascheme 有工作流**；**rolelist 不是时序、dynascheme 是时序**；**rolelist 删除链 23 表、dynascheme 删除链 5 表**。

### 7.2 hrcs_rolelist vs haos_adminorgdetail

| 维度 | hrcs_rolelist | haos_adminorgdetail |
|---|---|---|
| 列表壳 form | hrcs_rolelist (treelist) | haos_adminorgdetail (treelist) |
| 数据实体 | perm_role (BOS 标品) | haos_adminorg (HR 标品) |
| 时序 | ❌ 不是 | ✅ HisModel |
| 复杂度 | 中（删除 23 表 · enable 双表同步） | 高（时序版本管理） |

→ **本场景的特点是 BOS 标品依赖（perm_role 由 BOS 维护）**，导致 ISV 扩展时需要"两层隔离"（不能改 perm_role 也不能改 hrcs_role）。

---

## 八、给 Claude / IDEA 插件 / cosmic-dev skill 的建议

> 当用户给出"角色"相关的需求时，先用本表判断该需求是否在本场景内：

| 用户描述 | 入口场景 |
|---|---|
| "给角色加个字段" | hrcs_rolelist + ISV 扩展 perm_role / hrcs_role · CS-01 |
| "禁用角色时要二次确认" | hrcs_rolelist + ListPlugin · CS-03 |
| "分组管理员只能管自己组的角色" | hrcs_rolelist + setFilter 增强 · CS-06 |
| "改角色权限矩阵" | hrcs_modifyrole（**不是本场景**） |
| "新建角色加自定义初始化" | hrcs_newrole（**不是本场景**） |
| "按规则自动分配角色" | hrcs_dynascheme（**不是本场景**） |
| "用户给一批员工赋角色" | hrcs_userrolerelat（**不是本场景**） |

> 不在本场景的需求 → 跳到对应场景查 `01_capability_boundary.md` 再判断。
