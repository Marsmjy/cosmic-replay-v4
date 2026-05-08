# 业务规则 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类 + opkeys_index.json 15 opKey + main_form.xml
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：把 `HRRoleListPlugin` / `HRAdminStrictPlugin` 反编译类里的业务规则提炼成可机读 + 可人读的清单。**ISV 扩展时不要打破这些规则** —— 否则会跟标品冲突。

---

## 一、页面级准入规则（preOpenForm 闸 · 11 hrcs 表单共用）

### 1.1 HR 领域管理员强校验

`HRAdminStrictPlugin.preOpenForm` (L29-L37) → `showMesIfUserIsNotAdmin` (L39-L53)：

```java
public static void showMesIfUserIsNotAdmin(PreOpenFormEventArgs e) {
    long userId = RequestContext.get().getCurrUserId();
    boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);
    boolean isCosmic = PermCommonUtil.isCosmicUser(userId);
    if (!isAdmin & !isCosmic) {
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。");   // HRAdminStrictPlugin_0 多语言
        return;
    }
    if (!HRAdminService.isHrAdmin()) {
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。");
        return;
    }
}
```

→ **三层闸**：
1. 先看是否系统管理员（`isAdminUser`）
2. 再看是否苍穹用户（`isCosmicUser`）—— 两者满足任一才进
3. 最后看是否 HR 领域管理员（`HRAdminService.isHrAdmin()`）—— 必须是

⚠️ 例外：`ListShowParameter.isLookUp() == true` 时**跳过**准入闸（L33-L34）—— 因为 F7 弹窗也走 hrcs_rolelist · 普通用户用 F7 选择角色不应被拒。

---

## 二、列表行级权限规则（setFilter · 行级权限闸）

### 2.1 viewableRoles 强过滤

`HRRoleListPlugin.setFilter` (L367-L372)：

```java
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    long currUserId = RequestContext.get().getCurrUserId();
    QFilter filter = new QFilter("id", "in", HRRolePermHelper.queryViewableRoles(currUserId));
    setFilterEvent.getQFilters().add(filter);
}
```

→ **行级隔离**：每个管理员看到的列表行**只是 `viewableRoles` 子集**，不是 perm_role 全集。`queryViewableRoles` 实际逻辑在 hrcs 业务包里（**ISV 不要继承**），简化版语义：
- 当前用户在创建组（`createadmingrp`）下的角色 ✅
- 公开范围（`hrcs_roleopenscope`）含当前用户管理员组的角色 ✅
- 分配范围（`hrcs_roleassignscope`）含当前用户管理员组的角色 ✅
- 顶级管理员组（`isTopAdminGroupModify`）下的子组 ✅

### 2.2 默认启用过滤

`HRRoleListPlugin.filterContainerInit` (L407-L415)：
```java
for (FilterColumn listFilter : listFilterColumns) {
    CommonFilterColumn commFilter = (CommonFilterColumn)listFilter;
    String fieldName = commFilter.getFieldName();
    if (HRStringUtils.equals("perm_role.enable", fieldName)) {
        commFilter.setDefaultValue("1");                     // 默认显示 enable=1
    }
}
```

→ 列表第一次打开默认只显示**已启用**角色，用户要看禁用的需手动切换过滤条件。

### 2.3 group.name F7 二级过滤

`HRRoleListPlugin.filterContainerBeforeF7Select` (L418-L432)：
```java
if (HRStringUtils.equals("perm_role.group.name", fieldName)) {
    HRBaseServiceHelper roleGrpHelper = new HRBaseServiceHelper(ENTITYTYPE_HRROLEGRP);
    QFilter[] filters = new QFilter[]{QFilter.isNotNull("id")};
    DynamicObjectCollection roleGrpColl = roleGrpHelper.queryOriginalCollection("id", filters);
    ArrayList idList = ...;                                    // 收集 hrcs_rolegrp 全部 ID
    QFilter filter = new QFilter("id", "in", idList);
    args.addCustomQFilter(filter);
}
```

→ 角色分组 F7 弹窗**只显示 hrcs_rolegrp 注册过的分组**（不是全 perm_rolegroup）。这是为了**隔离 HR 域 vs 其他域的角色分组**。

---

## 三、删除前置规则（4 重 checkCanDel · CS-04 重头）

`HRRoleListPlugin.checkCanDel` (L1488-L1553) 和 `getRefrencedRoles` (L1555-L1568) · 是**最重要的 ISV 扩展点参考**：

### 3.1 R1 · 预置角色不允许删除

```java
} else if (null != isSysPreSet && isSysPreSet.booleanValue()) {
    errorMsgs.add("%s：预置角色不允许删除。");                          // L1514
    opDesc = "编号%s，预置角色不允许删除。";
    RoleService.addLogNoOpKey("delete", "删除", false, "hrcs_rolelist", appId, list, opDesc);
}
```

→ `issyspreset = true` 直接拦 ·  无解。这是平台**全局规则**（PR-007 预置数据不可改）。

### 3.2 R2 · 被 hrcs_dynascheme 引用的角色不允许删除

```java
private Set<String> getRefrencedRoles(Set<String> roleIds) {       // L1555
    HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_dynascheme");
    DynamicObject[] schemeArr = helper.query("roleentry.role",
        new QFilter[]{new QFilter("roleentry.role.id", "in", roleIds)});
    for (DynamicObject schemeDyna : schemeArr) {
        DynamicObjectCollection entryColl = schemeDyna.getDynamicObjectCollection("roleentry");
        for (DynamicObject entryDyno : entryColl) {
            String roleId = entryDyno.getString("role.id");
            if (roleIds.contains(roleId)) refrencedRoles.add(roleId);
        }
    }
    return refrencedRoles;
}

// 主流程 L1506-L1511
if (refrencedRoles.contains(roleId)) {
    errorMsgs.add("%s：删除失败，因为"动态授权方案"的"角色"字段引用了此数据。");
}
```

→ **本场景与 hrcs_dynascheme 唯一的硬约束**。ISV 加自己的反查规则也按这个套路（CS-04 完整代码见 06）。

### 3.3 R3 · 当前管理员不允许操作

```java
if (!this.canOperateRole(groupIds, longNumbers, roleId)) {        // L1520
    errorMsgs.add("%s：当前管理员不允许修改该通用角色。");
    cache.put("cannotOperate", "1");
}
```

`canOperateRole` (L1075-L1099) 详细规则：
1. 角色没有 `createadmingrp` → 任何人能操作（noCreateAdminGroup → return true）
2. 当前用户在 `createadmingrp` 范围 → 通过（masterGroup → return true）
3. `createadmingrp.longnumber` 是当前用户 longnumber 的祖先 → 通过（topAdminGroupModify → return true）
4. `hrcs_roleassignscope` 表存在 (roleid=this, admingroup IN currUserGroups, ismodifiable=1) → 通过
5. 否则不通过

### 3.4 R4 · 启用中或有成员的角色不允许删除

```java
if (HRStringUtils.equals(enable, "0") && memberCount == 0) {       // L1530
    roleIdList.add(roleId);                                         // 通过
} else {
    errorMsgs.add("%s：角色未禁用，且角色成员不为空。");                 // L1534
}
```

→ **必须先 disable 再删 + 必须先清空 hrcs_userrolerelat 关联用户再删**。

---

## 四、启用/禁用前置规则

### 4.1 R5 · 选中行不能为空

`enableRoleInfo` / `disableRoleInfo` (L1259 / L1327)：
```java
if (roleList.size() == 0) {
    this.getView().showTipNotification("请选择要执行的数据。");        // HRRoleListPlugin_15
    return;
}
```

### 4.2 R6 · canOperateRole 校验（每行）

`enableRole` / `disableRole` (L1292 / L1360)：每个选中行都跑一遍 `canOperateRole`，不通过的行：
- ++fail
- errorMsg += "编号%s：当前管理员不允许修改该通用角色。"
- 写失败日志 `addLogNoOpKey("enable"/"disable", "启用"/"禁用", false, ...)`

### 4.3 R7 · 重复操作拦截

`enableRole` (L1301) / `disableRole` (L1369)：
- enable 时 `enable == true` → "编号%s：数据已启用。" 失败
- disable 时 `enable == false` → "编号%s：数据已禁用。" 失败

### 4.4 R8 · BizRoleComRole 同步

`doDisableRole` (L1395-L1433) → `DB.update(DBRoute.basedata, "Update T_Perm_BizRoleComRole set FEnable=...")` (L1401)

→ 业务角色复用关系表也要同步刷 FEnable —— 这是 BOS 标品要求 · ISV 不要碰这张表。

---

## 五、复制角色前置规则

### 5.1 R9 · checkCanCopy 4 重判断

`checkCanCopy` (L1208-L1221)：

| 通过条件 | 说明 |
|---|---|
| `noCreateAdminGroup` | 角色没绑定创建组 → 任何人能复制 |
| `masterGroup` | 当前用户在角色 createadmingrp 内 |
| `topAdminGroupModify` | 当前用户管理员组是顶级组 |
| `checkHasModifyPerm` | hrcs_roleopenscope 公开范围 OR usescope=0 + createadmingrp IN groupIds OR hrcs_roleassignscope ismodifiable=1 |

不满足 → "您无权复制当前角色，请联系角色所属管理员分组的用户，设置角色的公开范围"（L1195）

### 5.2 R10 · 禁用状态不允许复制

```java
if (null != item && !item.getBoolean("enable")) {                  // L1200
    this.getView().showTipNotification("禁用状态不允许复制。");
    return;
}
```

→ **必须先启用再复制**。

### 5.3 R11 · 单选限制

```java
if (roleIds.length == 0) showTipNotification("请选择角色");          // L1186
if (roleIds.length > 1)  showTipNotification("请选择一个角色");      // L1190
```

→ 复制是**单选操作**·不支持批量复制。

---

## 六、按钮点击前置规则（beforeItemClick 闸）

`HRRoleListPlugin.beforeItemClick` (L829-L861) 拦点：

| itemKey | 校验函数 | 规则 |
|---|---|---|
| `btn_batchgroup` (批量分组) | `isNoOneSelected` | 至少选 1 行 |
| `btn_allocprem` (分配权限) | (无) | 不校验 |
| `bar_assignmember` (分配成员) | `validateAssignMember` (L936) | 1) 必选 1 行（不为空，size==1）2) checkRoleEnable (L941) `enable!="1"` 时拒绝 → "请先启用角色" |
| `exportroleperm` (导出用户权限) | `isNoOneSelected` | 至少选 1 行 |
| `baritem_alteruserperm` (修改用户权限) | `isOnlyOneSelected` | 必选 1 行（不能多 · 不能 0） |
| `""` (BARITEM_SHARE 分享 · 未填实际 key) | `isNoOneSelected` | 至少选 1 行 |

### 6.1 R12 · checkRoleInfoExist 单选行校验

`handlePage` (L863-L871) → 任意 itemKey · 选中**恰好 1 行** · 都会跑 `checkRoleInfoExist`（L893-L913）：
- 调 `PermDBServiceHelper.roleDBService.queryOne(roleId)`
- 角色不存（被并发删了）→ 列表清选+刷新+提示"角色数据过期，请重新刷新页面后再操作。" → setCancel(true)

⚠️ 这个校验对 `refresh` 不跑（L868），其他 12 个按钮都跑。

---

## 七、树节点交互规则（btnnew/btnedit/btndel）

### 7.1 R13 · 根节点不可改

`editAction` / `delAction` (L479 / L456) 都先校验：
```java
if (this.getTreeModel().getRoot().getId().equals(currentNodeId)) {
    showTipNotification("不允许编辑根节点。"/"不允许删除根节点。");
    return;
}
```

### 7.2 R14 · 禁用刷新树情况下不可改

```java
if (!prop.isNeedRefreshTree()) {
    showTipNotification("禁用刷新情况不允许修改节点"/"禁用刷新情况不允许删除节点");
}
```

### 7.3 R15 · canDelGroup 双闸（只对 btndel）

`canDelGroup` (L539-L555)：
1. ❌ 包含子分组（`perm_rolegroup` 中存在 parent==nodeId）→ "不允许删除该分组，因为它包含下级分组。"
2. ❌ 包含角色（`perm_role` 中存在 group==nodeId）→ "该分组存在角色，不允许删除。"

---

## 八、authaction / status / boid 规则不适用

| 规则类型 | hrcs_rolelist 适用吗 |
|---|---|
| BillFormModel status 工作流（A/B/C） | ❌ 本场景是 QueryListModel · 没 status |
| HisModel 时序版本（boid/iscurrentversion） | ❌ 本场景非时序 · grep 0 处命中 |
| authaction 必填 + 切换二次确认 | ❌ 这是 dynascheme 的（authaction 字段） · 本场景没该字段 |
| codeRule / numberValidator | ❌ 列表不创建数据 · 创建走 hrcs_newrole · 不在本场景范围 |
| HisUniqueValidateOp 唯一性 | ❌ 同上 |
| BdVersionSaveServicePlugin 版本号 | ❌ 同上 |

---

## 九、4 重日志规则

详见 05_data_flow.md 第 6 节。每个操作（enable/disable/delete/复制）都会写：
1. **业务日志** `RoleService.addLogNoOpKey(opKey, name, success, formId, appId, list, desc)` → ILogService 批量提交
2. **角色信息日志** `PermLogService.initPermLog(opKey, before, after, roleId)` → 权限审计专用表（仅 delete）
3. **角色状态日志** `PermLogRoleStatusServiceHelper.addPermLog(roleIds, LOG_TYPE_xxx)` → 状态日志（仅 enable / disable）
4. **批量提交** `ILogService.addBatchLog(appLogInfoList)` → 一次性写完

⚠️ ISV 不要重复写 —— 标品已经全写了。如果 ISV 拦截后做了自己的业务，也要写 `addLogNoOpKey` 提示自己介入了，方便审计追踪。

---

## 十、规则速查表（决定 ISV 能不能修改）

| 规则 | 可绕过 | 强度 | 实证 |
|---|---|---|---|
| R0 HR 管理员准入 | ❌ 不可 | 🔴 平台级 | HRAdminStrictPlugin |
| R1 预置角色不可删 | ❌ 不可（PR-007） | 🔴 平台级 | checkCanDel L1512 |
| R2 被 dynascheme 引用 | ⚠️ 业务上不应改 | 🟠 强 | getRefrencedRoles L1555 |
| R3 当前管理员不允许操作 | ⚠️ 改了要重做权限模型 | 🟠 强 | canOperateRole L1075 |
| R4 启用/有成员不可删 | ❌ 数据安全要求 | 🔴 业务硬 | checkCanDel L1534 |
| R5-R7 启用/禁用前置 | ❌ 业务约束 | 🟠 强 | enableRole/disableRole |
| R8 BizRoleComRole 同步 | ❌ BOS 标品要求 | 🔴 平台级 | doDisableRole L1401 |
| R9 复制权限校验 | ⚠️ 业务上可放宽 | 🟡 中 | checkCanCopy |
| R10 禁用不可复制 | ⚠️ 业务上可放宽 | 🟡 中 | copyRole L1200 |
| R11 复制单选 | ⚠️ 业务上可改成批 | 🟡 中 | copyRole L1190 |
| R12 行存活校验 | ❌ 数据一致性 | 🟠 强 | checkRoleInfoExist |
| R13 根节点不可改 | ❌ UX | 🟡 中 | delAction/editAction |
| R14 禁用刷新不可改 | ❌ 树状态依赖 | 🟡 中 | 同上 |
| R15 含下级/含角色不可删 | ❌ 数据完整性 | 🟠 强 | canDelGroup |

→ ISV 想"放宽"任何规则，必须先得到业务确认，并在 `08_impact_analysis.md` 评估对下游模块的影响。
