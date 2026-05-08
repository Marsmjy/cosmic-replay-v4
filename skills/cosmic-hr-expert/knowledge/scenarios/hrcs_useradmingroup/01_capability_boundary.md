# 能力边界 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于反编译 5 类 + scene_doc.json 实抓 + opKey 实证
> **confidence**：verified
> **数据源**：CFR 反编译 + OpenAPI 实抓（2026-04-28）

---

## 一、场景定位

`hrcs_useradmingroup` 是 HR 通用服务（hrcs）域 → HR 权限管理 → **HR管理员** 菜单。负责"用户 × 管理员组"的多对多关联管理 + 管理员组树形结构维护。

| 维度 | 实证 |
|---|---|
| 菜单路径 | HR通用服务 / 权限管理 / HR管理员 |
| 主表 formNumber | `hrcs_useradmingroup` |
| 形态 | DynamicFormModel · TreeList 视图（左树 + 右列表） |
| 物理表 | `t_perm_useradmingroup`（关联表 · 2 字段） + 强耦合 `t_perm_admingroup`（左树） |
| 字段 | 2 个（user / usergroup · 都是 BasedataField） |
| 是否 HisModel | ❌（grep 无 boid/iscurrentversion/HisModel·非时序） |
| 主操作 | 7 个 donothing_* + 3 个 do_* OP |
| ISV 插件 | 0（标品 8 个全标品 · 0 ISV 二开） |
| BEC 发布 | 0 处（标品没发 · CS-05 已砍） |
| SDK 扩展点 | 1 个（`IAdminGroupListSubExtPlugin#beforeAddCustomUser` · 反编译 L123-L124 实证） |

---

## 二、能力清单

### 2.1 标品已支持

| 能力 | opKey | 反编译实证 | 备注 |
|---|---|---|---|
| 添加用户到管理员组 | `donothing_add_user` → `do_add_user` | `HRAdminGroupTreeListPlugin.showUserF7TreeList` + `closedCallBack` + `AdminGroupAddUserOp.beginOperationTransaction` | 5 道用户F7范围过滤 + 权限日志 + 缓存清理 |
| 从管理员组删除用户 | `donothing_remove_user` → `do_remove_user` | `HRAdminGroupTreeListPlugin.beforeDoOperation` + `afterDoOperation` + `AdminGroupDelUserOp.beginOperationTransaction` | 跨组守护 + 部分成功反馈 + 权限日志 |
| 新增管理员组 | `donothing_add_group` | `HRAdminGroupTreeListPlugin.adminGroupTreeAddOperation` | 弹 hrcs_admingroupdetail 子页面 · 校验层级上限 |
| 修改管理员组 | `donothing_modify_group` | `HRAdminGroupTreeListPlugin.adminGroupTreeModifyOperation` | 弹 hrcs_admingroupdetail · EDIT/VIEW 双态 |
| 删除管理员组 | `donothing_remove_group` → `do_remove_group` | `HRAdminGroupTreeListPlugin.adminGroupTreeRemoveOperation` + `confirmCallBack` + `AdminGroupDelOp.beginOperationTransaction` | 4 道前置校验 + 9 表级联删 |
| 批量授权 | `donothing_batch_perm` | `HRAdminGroupTreeListPlugin.adminGroupTreeBatchPermOperation` | 弹 hrcs_amingroupbatchauth · type=batchAuth |
| 刷新树 | `refresh` | `HRAdminGroupTreeListPlugin.refresh` | 重建 TreeView · 异常吞掉 |
| HR 域准入闸 | （前置 preOpenForm） | `HRAdminStrictPlugin.preOpenForm` + `HRAdminGroupTreeListPlugin.beforeDoOperation` 二次校验 | 11 hrcs 表单共用 · 非 HR 管理员直接拒 |
| 树节点点击缓存 | （前置 treeNodeClick） | `HRAdminGroupTreeListPlugin.treeNodeClick` | 5 个 IPageCache key |
| 用户列表权限过滤 | （setFilter） | `HRAdminGroupTreeListPlugin.setFilter` | adminGroupCanSee 加 in 过滤 |

### 2.2 业务可配能力（不需要 ISV 二开）

| 能力 | 配置位置 | 备注 |
|---|---|---|
| 管理员级别上限 | `PermCommonUtil.getAdminLevelLimit()` | 平台级配置 · 默认 5 级 |
| 权限日志开关 | `PermCommonUtil.isEnablePermLog()` | 启用后 add/remove 用户写权限日志 |
| 权限变更通知开关 | `PermCommonUtil.isEnableAuthorityChangeNotice()` | 启用后清缓存 cancelShowFormRights |
| 管理员组树初始化 | `HRAdminGroupService.initAdminGroupTree` | 业务侧不需要改 · 标品按 longnumber/parent 自动构建 |

### 2.3 标品 **不**支持（ISV 二开点）

| 不支持的能力 | ISV 解决方案 | 对应 CS |
|---|---|---|
| 给关联表加业务属性（如"管理员标签"） | modifyMeta + ISV 自建字段 | CS-01 |
| ISV 字段联动（如选标签后联动授权等级） | ISV 自建 FormPlugin · propertyChanged 联动 | CS-02 |
| 防重复加用户 / 防删超管 | ISV 自建 OP · onAddValidators 注册 Validator | CS-03 |
| ISV 自建表反向引用反查（删组前） | ISV 自建 OP · 同事务 cascade | CS-04 |
| 跨系统通知（如同步到外部 SSO） | ISV 自建定时任务 / `afterExecuteOperationTransaction` 调外部 API | CS-05（但不推荐 BEC） |
| 用户F7范围扩展（如只允许部门负责人） | ISV 实现 `IAdminGroupListSubExtPlugin#beforeAddCustomUser` SDK 接口 | CS-06 |
| 批量授权自动化模板 | ISV 自建 FormPlugin 挂 hrcs_amingroupbatchauth 子页面 | CS-07 |

---

## 三、能力边界（红线·禁区）

### 3.1 ISV 不能做

| 红线 | 原因 | PR |
|---|---|---|
| 继承 `HRAdminGroupTreeListPlugin` 重写其方法 | 场景专属类 · 标品升级会破坏 ISV 代码 | PR-001 |
| 继承 `AdminGroupAddUserOp` / `AdminGroupDelUserOp` / `AdminGroupDelOp` | 同上 · 场景专属 OP 类 | PR-001 |
| 继承 `HRAdminStrictPlugin` 改 HR 准入逻辑 | 11 hrcs 表单共用 · 改一处坏所有 | PR-001 |
| 直接修改 `t_perm_useradmingroup` 物理表（DML） | 绕过 OP 链 · 权限日志/缓存清理全失效 | - |
| 修改 user / usergroup 字段的 `isvCanModify` 元属性 | 破坏关联表语义 · 行为不可预测 | PR-007 |
| 在 `do_add_user` / `do_remove_user` 的 `endOperationTransaction` 阶段做 cascade | 与标品 OP 撞同事务边界 · 顺序不确定 | PR-010 |
| 在 `afterExecuteOperationTransaction` 阶段做关键校验 | 事务已提交 · 校验失败无法回滚 | PR-010 |

### 3.2 ISV 谨慎做

| 黄区 | 风险 | 缓解 |
|---|---|---|
| 在 `setFilter` 阶段加自定义过滤 | 可能与 adminGroupCanSee 标品过滤冲突 · 撞 in 集合 | 通过 IAdminGroupListSubExtPlugin SDK 扩展 |
| 改 `IPageCache` 的 8 个标品 key | 标品 TreeList 行为可能错乱 | 加自己的 key（带 ISV 前缀） |
| 监听 `propertyChanged(usergroup)` 联动 | usergroup 是 BasedataField · F7 选择会触发多次 propertyChanged | 必加 beginInit/endInit · 详见 CS-02 |
| 自建 BEC 发布 | 标品 0 处订阅 · ISV 自建无价值 | CS-05 已砍 · 不推荐 |

### 3.3 平台限制

| 限制 | 说明 |
|---|---|
| 字段名长度 | `fieldName` 列名 ≤ 25 字符 |
| 字段 key | 必须 ISV 前缀（如 `${ISV_FLAG}_`）防标品升级覆盖 |
| 管理员组层级 | 默认 5 级（PermCommonUtil.getAdminLevelLimit）· 业务可改 |
| 节点 id 格式 | `<adminGroupId>_<level>` · 根节点是 UUID `8609760E-EF83-4775-A9FF-CCDEC7C0B689` |

---

## 四、典型业务场景路径

```
场景 A · "新增 HR 管理员"
  HR 总监打开 hrcs_useradmingroup
  → HRAdminStrictPlugin 准入校验通过
  → 左树点选 "财务管理员" 组
  → treeNodeClick 缓存 5 个 key
  → 点工具栏添加用户按钮 (donothing_add_user)
  → beforeDoOperation 拦截 → showUserF7TreeList
  → 弹 bos_user F7（已加用户/自己/同scheme/enable=1/usertype 5 道过滤 + IAdminGroupListSubExtPlugin 扩展）
  → 选 3 个用户 → closedCallBack
  → 构造 hrcs_useradmingroup DO[3] · OperateOption 含 5 个 key
  → executeOperate(do_add_user) → AdminGroupAddUserOp
  → SaveServiceHelper.save → 落库 t_perm_useradmingroup
  → cancelShowFormRights 清缓存 + adminEvent2PermLog 写日志
  → 返回 success
  → FormPlugin treeNodeClick 刷新当前节点 + clearCache + showSuccessNotification

场景 B · "删除一个管理员组（会清 9 张表）"
  HR 总监打开 hrcs_useradmingroup
  → 左树点选 "临时管理员" 组
  → 点工具栏删除按钮 (donothing_remove_group)
  → beforeDoOperation 调 adminGroupTreeRemoveOperation
  → 校验 1: 不是根节点 ✓
  → 校验 2: verifyAdminGroup（在管控范围）✓
  → 校验 3: 无下级分组 ✓
  → 校验 4: 无用户 ✓
  → 校验 5: checkHasRoleRef 反查 perm_role / hrcs_roleopenscope / hrcs_roleassignscope ✓
  → 弹确认对话框（OK/Cancel）
  → 用户点 OK → confirmCallBack
  → 构造 hrcs_useradmingroup DO（usergroup=adminGroupId）
  → executeOperate(do_remove_group) → AdminGroupDelOp
  → 9 表级联 delete（perm_admingroupfunperm/bizunit/org/app/adduser/admingroup + hrcs_admingrouporg/func/file）
  → ISV 挂的 TdkwAdminGroupExtCascadeOp（CS-04）同事务 delete ${ISV_FLAG}_admingroupext
  → 全部成功 → 事务提交
  → FormPlugin reload 树（initAdminGroupTree）
```

---

## 五、跨场景对照（同 hrcs 域）

| 场景 | 形态 | 是否 HisModel | 字段数 | BEC | 共用类 |
|---|---|---|---|---|---|
| `hrcs_useradmingroup`（本场景） | TreeList | ❌ | 2 | 0 处 | `HRAdminStrictPlugin` |
| `hrcs_dynascheme` | List + 单据 | ✅ | 56 | 0 处 | `HRAdminStrictPlugin` |
| `hrcs_amingroupbatchauth`（CS-07 子页面） | Form | ❓ | 待探针 | 待探针 | `HRAdminStrictPlugin` |
| `hrcs_admingroupdetail`（CS-01/02 子页面） | Form | ❓ | 待探针 | 待探针 | `HRAdminStrictPlugin` |

→ Claude 跨场景生成代码时 · 不要套 hrcs_dynascheme 的 HisModel 模式到本场景 · 不要套本场景的 IAdminGroupListSubExtPlugin SDK 接口到其他场景。

---

## 六、能力边界总结（一句话）

> hrcs_useradmingroup 标品已经把"用户 × 管理员组"关联表的所有标准操作（增/删用户、增/改/删/批量授权 组）封装到 `HRAdminGroupTreeListPlugin` + 3 个 OP 里。**ISV 二开的核心抓手**是：modifyMeta 加扩展字段（CS-01）、ISV 自建 FormPlugin 联动（CS-02）、并列挂 OP + Validator（CS-03/04）、实现 SDK 扩展点 `IAdminGroupListSubExtPlugin`（CS-06）、挂子页面 FormPlugin（CS-07）。**严禁继承场景专属类**（PR-001 红线）· 严禁绕过 OP 链直改物理表 · BEC 标品 0 处发布不推荐 ISV 自建（CS-05 砍）。
