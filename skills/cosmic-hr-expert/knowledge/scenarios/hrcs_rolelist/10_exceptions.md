# 异常诊断 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类（HRRoleListPlugin / HRAdminStrictPlugin）+ 共性陷阱（cosmic_realworld_traps）+ 实战案例
> **confidence**: verified（标品级异常） + distilled（ISV 级陷阱）
> **数据源**: CFR 反编译 + cosmic_realworld_traps + 同应用 hrcs_dynascheme 类比 (2026-04-28)

> 📌 **本文档作用**：列出本场景标品已知异常 + ISV 实战常踩的坑 + 排查路径。

---

## 一、标品异常（用户看到的提示）

### 1.1 准入闸异常

| 提示 | 来源 | 触发条件 |
|---|---|---|
| "您无法访问该功能，因为您不是HR领域管理员。" | `HRAdminStrictPlugin.preOpenForm` (L45/L50) | 当前用户不是 isAdminUser/isCosmicUser/isHrAdmin · 且不是 lookUp F7 模式 |

**排查**：
1. 看用户是否在 BOS 系统管理员组（PermissionServiceHelper.isAdminUser）
2. 看用户是否在苍穹平台用户列表（PermCommonUtil.isCosmicUser）
3. 看用户是否绑定 HR 域管理员（HRAdminService.isHrAdmin）
4. 都不满足 → 联系系统管理员加权限

### 1.2 删除前置异常

| 提示 | 来源 | 触发条件 | 解决 |
|---|---|---|---|
| "%s：预置角色不允许删除。" | `HRRoleListPlugin.checkCanDel` L1514 | issyspreset=true 的角色 | 不能删 · 改 issyspreset 是 BOS 内部 |
| "%s：删除失败，因为"动态授权方案"的"角色"字段引用了此数据。" | `HRRoleListPlugin.checkCanDel` L1508 | 被 hrcs_dynascheme.roleentry.role 引用 | 先在 hrcs_dynascheme 移除引用 |
| "%s：当前管理员不允许修改该通用角色。" | `HRRoleListPlugin.checkCanDel` L1521 / `enableRole` L1294 / `disableRole` | canOperateRole 不通过 | 联系角色 createadmingrp 内的用户操作 / 申请加入分组 |
| "%s：角色未禁用，且角色成员不为空。" | `HRRoleListPlugin.checkCanDel` L1534 | enable!=0 或 memberCount>0 | 先 disable + 清空所有 hrcs_userrolerelat 记录再删 |
| "当前管理员不允许删除该通用角色。" | `HRRoleListPlugin.deleteRoleInfo` L1465 (cannotOperate=1 缓存) | 同上 | 同上 |
| "不允许删除预置角色、启用状态角色、被动态授权方案引用或者成员不为空的角色。" | `HRRoleListPlugin.deleteRoleInfo` L1467 | 同上多种组合 | 同上 |

### 1.3 启用 / 禁用异常

| 提示 | 来源 | 触发条件 | 解决 |
|---|---|---|---|
| "请选择要执行的数据。" | `enableRoleInfo` / `disableRoleInfo` L1262/L1330 | 没选行 | 先勾选 |
| "数据不存在。" | `enableRole` / `disableRole` L1284/L1352 | BusinessDataServiceHelper.load 返回空 | 角色被并发删 → 刷新 |
| "编号%s：当前管理员不允许修改该通用角色。" | `enableRole` L1294 / `disableRole` L1362 | canOperateRole 不通过 | 同上 |
| "编号%s：数据已启用。" | `enableRole` L1303 | enable==true 时尝试 enable | 重复操作 · 不需要再启用 |
| "编号%s：数据已禁用。" | `disableRole` L1371 | enable==false 时尝试 disable | 重复操作 · 不需要再禁用 |

### 1.4 其他工具栏异常

| 提示 | 来源 | 触发条件 | 解决 |
|---|---|---|---|
| "请选择有效的分组节点。" | `delAction` / `editAction` L462/L485 | currentNodeId 空 | 先在树上选节点 |
| "不允许删除根节点。" | `delAction` L458 | 选的是根节点 | 选具体分组 |
| "不允许编辑根节点。" | `editAction` L481 | 同上 | 同上 |
| "禁用刷新情况不允许删除节点" | `delAction` L466 | prop.isNeedRefreshTree() == false | 标品平台层 · 不应触发 |
| "不允许删除该分组，因为它包含下级分组。" | `canDelGroup` L544 | 子分组存在 | 先删子分组 |
| "该分组存在角色，不允许删除。" | `canDelGroup` L551 | 分组有角色 | 先把角色挪走 |
| "请选择一个数据。" | `isOnlyOneSelected` L921 | 多选了 | 单选 |
| "请选择要执行的数据。" | `isNoOneSelected` L952 | 没选 | 选 |
| "请先启用角色" | `validateAssignMember` (checkRoleEnable) | 当前角色 enable!=1 | 先 enable 再分配成员 |
| "请选择角色。" | `copyRole` L1186 | 复制时 0 行 | 单选 1 个角色 |
| "请选择一个角色。" | `copyRole` L1190 | 多选了 | 复制是单选 |
| "您无权复制当前角色，请联系角色所属管理员分组的用户，设置角色的公开范围。" | `copyRole` L1195 | checkCanCopy 不通过 | 联系源角色 createadmingrp 的用户 |
| "禁用状态不允许复制。" | `copyRole` L1201 | enable=false 复制 | 先启用源角色再复制（业务上反直觉但是标品行为） |
| "角色数据过期，请重新刷新页面后再操作。" | `checkRoleInfoExist` L900/L906 | 角色被并发删 | 刷新页面 |

---

## 二、共性陷阱（来自 `cosmic_realworld_traps/`）

### 2.1 buildmeta_traps（加字段时的坑） · 详 `kb_cosmic_buildmeta_traps.md`

ISV 用 CS-01 加字段时常踩：
- ❌ **直接 modifyMeta perm_role 失败 errorCode=0 但实际没生效** —— ISV 归属校验拦在 `perm_role` 上 · 必须在 ISV 扩展元数据上加（详见 `feedback_isv_ownership_redline.md`）
- ❌ **EmployeeField 等 HR SDK 扩展类型 OpenAPI 不支持** —— 用 BasedataField + parent=hbpm_employee 替代
- ❌ **74 ComboField 值枚举不全** —— 详见 `buildmeta_traps.md` 第三节
- ❌ **fieldName 不传会按 `f + key.lower()` 自动 · 传错会变 ffk_xxx** —— 不要手填
- ❌ **MuliLangTextField 加到 hrcs_role 子表能成功 · 加到 perm_role 子表可能失败** —— 视 ParentId 配置 · 实证 getFormSchema 二次验证

### 2.2 modifymeta_traps（改元数据时的坑） · 详 `kb_cosmic_modifymeta_traps.md`

- ❌ **ops 数组格式：dataType / displayName 错** —— 必须 fieldType / name / columnName（详见 `modifymeta_param_names_and_hr_sdk_limits.md`）
- ❌ **EmbedFormAp 假成功** —— 列表里 EmbedFormAp 用的少 · 但树形列表如果想嵌入子表必须实证
- ❌ **平台 errorCode=0 是字符串 truthy 是成功** —— 详见 `cosmic_openapi_response_format.md`

### 2.3 addrule_traps（加规则时的坑） · 详 `kb_cosmic_addrule_traps.md`

- ⚠️ 本场景**不通过 listRule / formRule 加规则**（rules.json 实抓为空）· 所有规则都在 Java 代码里硬编码（详见 02 第三节）· 故 addrule_traps 本场景不适用
- ⚠️ ISV 想加业务规则 → 走 ListPlugin 的 beforeDoOperation 抢拦（CS-03 / CS-04）

---

## 三、ISV 实战陷阱（本场景特有）

### 3.1 ⚠️ Trap-01 · setFilter 用 super 替换 (CS-06)

**坑**：

```java
// ❌ 错误
@Override
public void setFilter(SetFilterEvent e) {
    // 没调 super.setFilter(e) → 标品 viewableRoles 闸丢失
    QFilter myFilter = new QFilter("group", "in", userGroups);
    e.getQFilters().add(myFilter);
}
```

**后果**：标品 `id IN viewableRoles` 闸没生效 · 用户看到所有角色（包括不该看的） · **数据泄露**。

**正确**：

```java
// ✅ 正确
@Override
public void setFilter(SetFilterEvent e) {
    super.setFilter(e);                         // 让标品 viewableRoles 闸先加
    QFilter myFilter = new QFilter("group", "in", userGroups);
    e.getQFilters().add(myFilter);              // 再叠加自己
}
```

**实证**：`HRRoleListPlugin.setFilter` L367-L372 · 必须保留。

---

### 3.2 ⚠️ Trap-02 · RowKey 没排在标品之前

**坑**：

ISV ListPlugin 的 RowKey 默认是 1（跟标品冲突）或 ≥ 2（晚于标品）· 想拦截标品 setCancel(true) 之前的 args · 但来不及。

**后果**：
- 标品 `HRRoleListPlugin.beforeDoOperation` 先跑完
- args.setCancel(true) 后 · 标品执行完自己的逻辑
- ISV 后跑 · args.isCancel() 已 true · 自己逻辑跳过

**正确**：

在元数据上把 ISV 插件 RowKey 设为 0（最小）· 或在 IDEA 插件里调用 `add_plugin(row_key=0)`。

**实证**：`HRRoleListPlugin` 的 RowKey=1（main_form.xml L33）· ISV 设 0 即可在标品之前。

---

### 3.3 ⚠️ Trap-03 · selectedRows 在 afterDoOperation 已被清空

**坑**：

```java
// ❌ 错误（CS-05 BEC 发布场景）
@Override
public void afterDoOperation(AfterDoOperationEventArgs e) {
    BillList billList = (BillList) this.getView().getControl("billlistap");
    ListSelectedRowCollection rows = billList.getSelectedRows();   // 空了！
    // 无法取选中行 → 发不出 BEC
}
```

**根因**：`HRRoleListPlugin.handleBeforeOperatetionEvent` L1022/L1029 在 enable/disable case 里**主动调了 `billList.clearSelection()`** · 走到 afterDoOperation 时已经空了。

**正确**：

在 `beforeDoOperation` 阶段先 PageCache 暂存：

```java
@Override
public void beforeDoOperation(BeforeDoOperationEventArgs e) {
    super.beforeDoOperation(e);
    BillList billList = (BillList) this.getView().getControl("billlistap");
    ListSelectedRowCollection rows = billList.getSelectedRows();
    if (rows != null && !rows.isEmpty()) {
        Set<Object> ids = new HashSet<>();
        rows.forEach(row -> ids.add(row.getPrimaryKeyValue()));
        this.getPageCache().put("${ISV_FLAG}_pendingRoleIds", SerializationUtils.toJsonString(ids));
    }
}

@Override
public void afterDoOperation(AfterDoOperationEventArgs e) {
    String json = this.getPageCache().get("${ISV_FLAG}_pendingRoleIds");
    Set<Long> ids = SerializationUtils.fromJsonString(json, Set.class);
    // 现在可以用 ids 发 BEC 了
    this.getPageCache().remove("${ISV_FLAG}_pendingRoleIds");
}
```

---

### 3.4 ⚠️ Trap-04 · 在 BEC 异常时 throw 上去

**坑**：

```java
// ❌ 错误
IEventService svc = ServiceFactory.getService(IEventService.class);
svc.triggerEventSubscribeJobs(app, eventNumber, msg, vars);   // 异常没 catch
```

**后果**：BEC 服务挂了 / 网络抖动 → 异常上抛 → 标品启用/禁用流程失败 → 用户投诉。

**正确**：

```java
// ✅ 正确
try {
    IEventService svc = ServiceFactory.getService(IEventService.class);
    svc.triggerEventSubscribeJobs(app, eventNumber, msg, vars);
} catch (Exception ex) {
    LOG.error("Failed to publish BEC event", ex);
    // 不抛 · 不影响标品流程
}
```

**遵循 PR-011** · 跟 `feedback_bec_3layer_async_publish.md` 一致。

---

### 3.5 ⚠️ Trap-05 · 删除链里塞自己的 SQL

**坑**：

ISV 想"在标品 doDeleteHrRole 之后清理自己的 ISV 表" · 写在了**没有正确监听点**的位置（如 setCancel 之后）。

**后果**：SQL 实际没跑 · 半年后审计发现 ISV 表脏数据。

**正确**：

在 ISV ListPlugin 的 `afterDoOperation` 监听 itemKey="delete" 或 operateKey="delete"：

```java
@Override
public void afterDoOperation(AfterDoOperationEventArgs e) {
    String operateKey = e.getOperateKey();
    if (!"delete".equals(operateKey)) return;
    // 此时标品 doDeleteBosRole + doDeleteHrRole 都已完成 · ISV 表清理可以做了
    Set<Long> ids = ...; // 从 PageCache 取（Trap-03）
    DeleteServiceHelper.delete("${ISV_FLAG}_role_isv_extra", new QFilter[]{new QFilter("roleId", "in", ids)});
}
```

---

### 3.6 ⚠️ Trap-06 · QueryListModel 里挂 propertyChanged 没用

**坑**：

```java
// ❌ 错误（CS-02 字段联动想挂列表）
@Override
public void propertyChanged(PropertyChangedArgs e) {  // 在挂 hrcs_rolelist 的 ListPlugin 里
    // ...
}
```

**后果**：QueryListModel 没有 IDataModel · propertyChanged 永不触发 · 代码白写。

**正确**：

挂到 `hrcs_newrole` / `hrcs_modifyrole`（DynamicFormModel）· 它们才有 propertyChanged。**hrcs_rolelist 不能做联动**。

---

### 3.7 ⚠️ Trap-07 · checkRoleInfoExist 的 PageCache 缓存

**坑**：

```java
// ❌ 错误（CS-03 高风险校验场景）
@Override
public void beforeDoOperation(BeforeDoOperationEventArgs e) {
    super.beforeDoOperation(e);
    String itemKey = this.getPageCache().get("itemKey");
    // 但这个 itemKey 是上一次的（标品 afterDoOperation 还没清）！
}
```

**根因**：`HRRoleListPlugin.beforeItemClick` L833 在 PageCache 写 itemKey · `afterDoOperation` L1007 才清。如果两个 ListPlugin 共享同一个 PageCache · 时序错可能拿到上一次的 itemKey。

**正确**：

直接用 `e.getOperationKey()` 拿当前的 opKey · PageCache 用 itemKey 仅当确认是当前操作（一般 opKey 跟 itemKey 一致 / 或在 switch case 里同步检查）。

---

### 3.8 ⚠️ Trap-08 · 直接改 perm_role / hrcs_role 元数据

**坑**：

直接 IDEA 插件 designer.open_existing_designer({"id": "perm_role", ...}) → modifyMeta add field。

**后果**：苍穹平台 ISV 归属校验拦截 · errorCode=0 但 implements 不到位（详见 `feedback_formid_no_fabrication.md` + `isv_ownership_redline.md`）。

**正确**：建 ISV 扩展元数据 `${ISV_FLAG}_perm_role_ext` 继承 perm_role · 在子元数据上加。

---

### 3.9 ⚠️ Trap-09 · 缓存清理异步导致测试不一致

**坑**：

```java
// 自动化测试场景
testEnableRole(roleId);
assertNotNull(getCachedPerm(roleId));  // 期望权限缓存里有
```

**后果**：`HRPermCacheMgr.clearAllCacheAsync()` 异步执行 · 测试断言时缓存可能还没清 · 偶发失败。

**正确**：

测试代码里加 `Thread.sleep(3000)` 等待 / 或主动调 `HRPermCacheMgr.clearAllCacheSync()`（同步版本 · 但本场景标品没用）。

---

### 3.10 ⚠️ Trap-10 · perm_role 上批量加字段超过 25 字符 fieldName

**坑**：

CS-01 加字段 `${ISV_FLAG}_role_security_classification_level` · key 太长 · 平台自动生成 `ftdkw_role_security_class...` 截断 · 数据库报错。

**后果**：建表失败 · 字段加不上去。

**正确**：key 控制 ≤ 20 字符（加上 `f` 前缀 ≤ 25 字符 = 平台规范上限）。

---

## 四、本场景"绝不会"看到的异常（与其他场景对比）

| 异常（其他场景常见） | 本场景为什么不会 |
|---|---|
| `HisUniqueValidateOp` 唯一性失败 | 本场景非时序 · grep 0 处（详 03 第六节） |
| `BdVersionSaveServicePlugin` 版本号冲突 | 同上 |
| audit / unaudit 审核校验失败 | 本场景没 status / 没工作流（详 04 第六节） |
| `HisModelOPCommonPlugin` 历史版本异常 | 本场景非时序 |
| OP 链 endOperationTransaction 异常回滚 | 本场景没 OP 链 |
| `confirmchange` 业务变更冲突 | 本场景非时序 |

---

## 五、排查日志路径

### 5.1 标品日志位置

| 日志类型 | 表 | 查询字段 |
|---|---|---|
| 业务日志（addLogNoOpKey） | `log_billno` 等 | formid='hrcs_rolelist' AND optime > now() - 1h |
| 角色权限信息日志（PermLogService） | (权限审计专用表) | roleId / opKey |
| 角色状态日志（PermLogRoleStatusServiceHelper） | (状态日志表) | roleId / LOG_TYPE_ROLE_ENABLE/DISABLE |

### 5.2 服务端日志关键字（在 Cosmic 后台日志找）

| 关键字 | 来源 | 含义 |
|---|---|---|
| `HRRoleListPlugin.billListHyperLinkClick exception` | `HRRoleListPlugin` L905 | 点击行查角色失败 |
| `dirty data is removed!` | `HRRoleListPlugin` L1453 | 删除时发现脏数据被清 |
| `Got selected role ids: {}` | `HRRoleListPlugin` L1739 / L1746 | 导出操作的选中角色 |
| `export URL is :` | `HRRoleListPlugin` L1126 | 导出回调拿到的 URL |
| `parentView's tab control is null` | `HRRoleListPlugin` L1594 | 分配成员时父 view 找不到 tab |
| `query HRRoleListPlugin.billListHyperLinkClick exception` | L905 | 角色实存校验异常 |
| `角色权限删除异常。%s` | L611 | doDeleteHrRole 异常（被 KDException 包装） |

---

## 六、推荐排查 SOP

当用户反馈"删除/启用/禁用失败"时：

1. **看 UI 提示**：先按第一节 1.2 / 1.3 节匹配标品异常 · 找根因
2. **看后台日志**：grep 用户操作时间 ± 30 秒的服务端日志 · 找 `HRRoleListPlugin` 关键字
3. **查日志表**：`SELECT * FROM log_billno WHERE formid='hrcs_rolelist' AND optime > now() - 1h ORDER BY optime DESC` 看最近操作
4. **查 ISV 自建拦截**：grep ISV ListPlugin 类名（`tdkw*`）日志 · 看是否有自己的 setCancel 拦截
5. **检查 RowKey 顺序**：`getFormMetadata("hrcs_rolelist")` 返回的 plugins 数组顺序 · 确保 ISV < 标品
6. **手动 SQL 查权限**：
   - `SELECT * FROM perm_role WHERE id = ?` · 看是否被并发删
   - `SELECT * FROM hrcs_userrolerelat WHERE role = ? AND status='1'` · 看是否还有用户绑定
   - `SELECT * FROM hrcs_dynascheme WHERE roleentry.role = ?` · 看是否被动态方案引用
7. **找 HRAdmin 重新做一遍**：用顶级 HR 管理员账号操作 · 看是否能成功（排除权限 / 行级过滤问题）
