# 业务流转 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类（HRRoleListPlugin / HRAdminStrictPlugin）+ opkeys_index.json 15 opKey + main_form.xml
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI `getOpKeyClasses` (2026-04-28)

---

## 一、业务全景：HR 角色管理列表要做什么

`hrcs_rolelist` 解决的是 **"HR 管理员维护角色及其下挂的功能/数据/字段权限"** 的运营入口。它不是单纯的 CRUD 列表，而是 **"角色生命周期 + 角色分组组织 + 用户分配 + 批量授权 + 导入导出"** 5 大子流的会聚点。本场景区别于：

- **静态分配** `hrcs_userrolerelat`（手动赋权 · 用户视角）：本场景是**角色视角**
- **动态分配** `hrcs_dynascheme`（按规则批量赋权 · 规则视角）：本场景是**直接维护角色**
- **角色矩阵编辑** `hrcs_modifyrole`：本场景**点击行 hyperLink 后跳转的下游表单**

> **关键差异点**（决定本场景为何这么复杂）：
> - 本表单是 **QueryListModel**（不是 BillFormModel）· 没有 status/audit/unaudit/submit/unsubmit
> - 本表单 **没有 OP 插件** · 所有"操作"都在 FormPlugin 的 `beforeDoOperation` 拦截后绕过 OP 链
> - 角色一旦被删除 · **23 行 SQL 同步级联清理**（详见 03 第五节）
> - 列表的"行级权限" 由 `HRRolePermHelper.queryViewableRoles(currUserId)` 决定（不是所有角色都给所有人看）

---

## 二、业务状态机

### 2.1 角色启用状态（`perm_role.enable` 字段）

```
0 已禁用 ──── enable ───► 1 已启用 / 10 启用中
   ▲                          │
   │                          │ disable
   └──────────────────────────┘
```

**关键约束**（`HRRoleListPlugin.enableRole` L1270-L1324 / `disableRole` L1338-L1392）：
- enable / disable 都先用 `BusinessDataServiceHelper.load("perm_role", "enable", filters)` 加载选中行（L1282 / L1350）
- 对每行调用 `canOperateRole(groupIds, longNumbers, role.id)` 校验（L1292 / L1360）—— 当前管理员要是创建组成员、或在 `hrcs_roleassignscope` 里有 `ismodifiable=1` 才能改
- 同时刷新 `T_Perm_BizRoleComRole.FEnable` 表（L1401-L1403）—— 是 BOS 的"业务角色复用"表 · ISV 不要碰
- `disable` 走完会 `HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds)` 异步清缓存（L1409）
- `enable` 走完会 `HRPermCacheMgr.clearAllCacheAsync()` 全清（L1023）

> ⚠️ 本场景**没有"已审核"概念** · 角色是基础资料形态，创建 → 直接启用 → 用户使用 → 禁用/删除。

### 2.2 数据存在状态（删除）

```
存在 (perm_role 行) ──── delete ───► 物理删除 (23 张表清理)
                              │
                              └─ 拦截前置条件不满足 → 拦截 + 写日志
```

**拦截条件**（`HRRoleListPlugin.checkCanDel` L1488-L1553）：
| 条件 | 拦截原因 | 反编译位置 |
|---|---|---|
| `issyspreset = true` | 预置角色不允许删除 | L1512-L1517 |
| 被 `hrcs_dynascheme.roleentry` 引用 | 动态授权方案在用 | L1506-L1511 ⭐ CS-04 重头 |
| 当前管理员不在 `createadmingrp` 范围 + 没在 `roleassignscope ismodifiable=1` 的开放范围 | 当前管理员不允许修改 | L1521-L1528 |
| `enable != "0"` 或 `memberCount > 0` | 角色启用中或有成员 | L1534-L1538 |

⚠️ **`memberCount` 来自 `RoleServiceHelper.getRoleMembers(roleIdList)`**（L1485 · 通过 `getExistRole` 调），返回 Map<roleId, Map<roleField, value>> · 其中 `memberCount` 是 hrcs_userrolerelat 关联用户数（不是 hrcs_userrole 直接表 · 是聚合）。

### 2.3 状态字段关系

| 状态组合 | 含义 | 能否 enable | 能否 disable | 能否 delete |
|---|---|---|---|---|
| `enable=0`, `memberCount=0`, `issyspreset=false`, 未被引用 | 可清理 | ✅ | ❌（已禁） | ✅ |
| `enable=0`, `memberCount=0`, `issyspreset=true` | 预置角色 · 已禁 | ✅ | ❌ | ❌ |
| `enable=1`, `memberCount>0` | 启用使用中 | ❌ | ✅ | ❌（必须先 disable + 清成员） |
| `enable=0`, `memberCount=0`, 但被 dynascheme 引用 | 已禁但有外引 | ✅ | ❌ | ❌ |

---

## 三、3 大核心业务流（按 opKey）

### 3.1 流程 A · 准入 + 列表展示（典型加载链）

```
1. 用户从菜单点击"角色管理" → 平台打开 form hrcs_rolelist
   触发：HRAdminStrictPlugin.preOpenForm（L29-L37）
        - 校验 PermissionServiceHelper.isAdminUser(userId) || PermCommonUtil.isCosmicUser(userId)
        - 校验 HRAdminService.isHrAdmin()
        - 不通过 → e.setCancel(true) + e.setCancelMessage("您无法访问该功能·因为您不是HR领域管理员")

2. 平台开始构建表单
   触发：HRRoleListPlugin.createTreeListView (L304-L307)
        - 把默认的 TreeListView 替换为 CommonTreeListView (`perm_rolegroup` 实体类型)

3. 表单数据绑定前
   触发：HRRoleListPlugin.beforeBindData (L355-L358)
        → updateSearch (L360-L365)
          - SearchAp 控件 setSearchEmptyText("请输入角色组名称")

4. 工具栏初始化
   触发：HRRoleListPlugin.initTreeToolbar (L309-L313)
        - 显示 btnnew/btnedit/btndel 树工具栏按钮
        - 隐藏 iscontainnow/iscontainlower 复选框

5. 列表行级过滤（重要 ⭐ 行级权限闸）
   触发：HRRoleListPlugin.setFilter (L367-L372)
        - long currUserId = RequestContext.get().getCurrUserId();
        - QFilter filter = new QFilter("id", "in", HRRolePermHelper.queryViewableRoles(currUserId));
        - setFilterEvent.getQFilters().add(filter);
        ⚠ 这里限定了"当前用户能看到哪些角色行"·不同管理员看到的行不同

6. 过滤容器初始化
   触发：HRRoleListPlugin.filterContainerInit (L407-L415)
        - 找 perm_role.enable 列 · 默认值 "1"（启用）

7. 自定义数据源注入
   触发：HRRoleListPlugin.beforeCreateListDataProvider (L1762-L1764)
        - args.setListDataProvider(new HRRoleProvider());
        ⚠ HRRoleProvider 是内部类·查询时合并 perm_role + hrcs_role 数据

8. 列表 + 树渲染完成 · 用户看到"角色管理"页面
```

### 3.2 流程 B · 删除角色（最复杂的链 · 23 张表级联）

```
1. 用户在列表勾选 N 行 · 点【删除】 → opKey=delete
   触发：HRRoleListPlugin.beforeItemClick (L829-L861)
        - itemKey="delete"·没特殊校验·直接走通

2. 触发：HRRoleListPlugin.beforeDoOperation (L989-L1003)
   → handleBeforeOperatetionEvent (L1012-L1072) → case "delete"
     → deleteRoleInfo (L1436-L1474)
        a. 查 hrRoleSet（选中行的 queryEntityPrimaryKeyValue）
        b. 调 RoleServiceHelper.getRoleMembers(roleIdList)
           → 拿到 Map<roleId, Map<key, value>> · 含 number/enable/issyspreset/memberCount
        c. 算 diff = hrRoleSet - bosRoleSet（在 hrcs_role 但不在 perm_role 的，是脏数据）
        d. 如有脏数据 → 调 deleteHrRoleById 清掉 hrcs 域配置 + 提示"dirty data is removed"
        e. 调 checkCanDel (L1488-L1553) · 见 2.2 节 4 个拦截
           ❌ 任何一行不通过 → 累计 errorMsgs · 写 log（addLogNoOpKey 失败）
           ❌ 全失败 → return false → 父调用 setTipNotification + setCancel
           ✅ 部分通过 → 把通过的 roleIdList put 到 PageCache "canDelRoleList"

3. confirmDeleteRoleInfo (L558-L599)
   ⚠ 注意：此函数被 args.setCancel(true) 后绕过标品 OP 链 ·  不走 deleteOp ·  全在 FormPlugin 内完成
   a. 从 PageCache 拿 canDelRoleList / errorCount / errorMsg
   b. 对每个 roleId 调用 PermLogService.initPermLog("del", before, after, roleId)
      → 初始化"角色信息日志" before/after · 用于审计
   c. 调 deleteBosRoleById (L662-L700)
      → doDeleteBosRole (L703-L732) [TX.required 事务]
        DELETE FROM perm_roleperm WHERE roleid IN (...)
        DELETE FROM perm_fieldperm WHERE id IN (...)
        DELETE FROM perm_rolefieldperm WHERE role IN (...)
        DELETE FROM perm_dataperm WHERE id IN (...)
        DELETE FROM perm_roledataperm WHERE role IN (...)
        DELETE FROM perm_userrole WHERE role IN (...)
        DELETE FROM perm_role WHERE id IN (...)
        for roleId in roleIds: removeRolePermCache(roleId)
      → 写日志 RoleService.addLogNoOpKey("delete", "删除", true, "hrcs_permfilelist", appId, list, desc) (L692-L694)
   d. 调 deleteHrRoleById (L602-L614)
      → doDeleteHrRole (L617-L637) [TX.required 事务]
        DELETE FROM hrcs_userrole WHERE userrolerealt IN (...)
        DELETE FROM hrcs_userdataruleentry WHERE userdatarule IN (...)
        DELETE FROM hrcs_userdataruleentry / userbdruleentry / userdatarule
        DELETE FROM hrcs_userrolerelat WHERE id IN (...)
        DELETE FROM hrcs_rolebu WHERE role IN (...)
        delRoleRule(roleIds) → DELETE roledataruleentry / rolebdruleentry / roledatarule (L640-L649)
        DELETE FROM hrcs_rolefield WHERE role IN (...)
        DELETE FROM hrcs_role WHERE id IN (...)
        DELETE FROM hrcs_roledimension WHERE role IN (...)
        PermDBServiceHelper.roleDimGrpDBService.deleteByFilter (L634)
        DELETE FROM hrcs_roleopenscope WHERE roleid IN (...)
        DELETE FROM hrcs_roleassignscope WHERE roleid IN (...)
   e. ILogService.addBatchLog(appLogInfoList) 落操作日志 (L696-L697)
   f. 提示成功："删除成功"·或"共 N 条单据，删除成功 X 条，失败 Y 条"
   g. BillList.clearSelection() + getView().updateView("billlistap") (L596-L598)

4. afterDoOperation (L1005-L1009)
   触发：清 PageCache "itemKey"
        ⚠ 注意 args.setCancel(true) 的 op 不会触发 OP 链的 afterExecuteOperationTransaction
```

### 3.3 流程 C · 启用 / 禁用（带 BOS 双表同步）

```
1. 用户勾选 N 行 · 点【启用】或【禁用】 → opKey=enable/disable
   触发：HRRoleListPlugin.beforeItemClick (L829-L861)
        - itemKey="bar_enable" / "bar_disable" · 不在 switch case 中 · 走 fallthrough → handlePage
        - handlePage (L863-L871) ·  size==1 时调 checkRoleInfoExist(roleId) · 校验角色实存
          ❌ 角色已被并发删 → showErrorNotification("角色数据过期，请重新刷新页面后再操作")
                              + clearSelection + refresh + return false → setCancel(true)

2. 触发：HRRoleListPlugin.beforeDoOperation (L989-L1003)
   → handleBeforeOperatetionEvent (L1012-L1072) → case "bar_enable" / "bar_disable"
     a. RequestContext.get().getCurrUserId() → 当前用户
     b. HRRolePermHelper.queryUserAdminGroupInfos(userId) → Map<groupId, longnumber> 当前用户绑定的管理员组
     c. enableRoleInfo / disableRoleInfo (L1259 / L1327)
        → 校验 selectRows 不为空（L1262 / L1330）
        → 调 enableRole / disableRole (L1270-L1324 / L1338-L1392)

3. enableRole / disableRole 主体
   a. 收集所有 roleIds + builder 拼 SQL IN 列表
   b. BusinessDataServiceHelper.load("perm_role", "enable", filters) 拿当前 enable 值
   c. 对每行 role 检查：
      - canOperateRole(groupIds, longNumbers, role.id) (L1075-L1099)
        ❌ 不允许 → ++fail · errorMsg 累计 · addLogNoOpKey "失败"
      - 当前 enable == 目标 enable → ++fail · "数据已启用/已禁用"
      - 通过 → role.set("enable", "1" / "0") · changeRoleIds.add(roleId)
   d. 调 doDisableRole (L1395-L1433) [TX.required 事务]
      - SaveServiceHelper.save(arrDObj[0].getDataEntityType(), arrDObj) 保存 perm_role.enable
      - DB.update(DBRoute.basedata, "Update T_Perm_BizRoleComRole set FEnable=? where FRoleID in (...)", [])
        ⚠ 这是裸 SQL · 非 ORM · 数据来自 BOS 内部表
      - fail==0 → showSuccessNotification("启用成功"/"禁用成功", 2000ms)
      - fail==1 && len==1 → showTipNotification(失败原因)
      - 其他 → showForm bos_operationresult 弹"共 N 条，成功 X，失败 Y"
   e. PermLogRoleStatusServiceHelper.addPermLog(changeRoleIds, LOG_TYPE_ROLE_ENABLE/DISABLE) 写权限日志 (L1319/L1387)
   f. ILogService.addBatchLog(appLogInfoList) 落业务日志
   g. ListView.refresh()

4. 收尾：args.setCancel(true) → BillList.clearSelection() (L1022/L1029)
   - enable 时 HRPermCacheMgr.clearAllCacheAsync()  全清缓存（L1023）
```

### 3.4 流程 D · 复制角色（特殊·链式跳转）

```
1. 用户勾选 1 行 · 点【复制】 → opKey=bar_copy
   触发：HRRoleListPlugin.beforeItemClick (L829-L861)
        - itemKey="bar_copy" · 不在 switch · 走 handlePage 校验 size 和 roleExist

2. 触发：HRRoleListPlugin.beforeDoOperation
   → handleBeforeOperatetionEvent → case "bar_copy"
     → copyRole (L1183-L1205)
        ❌ 0 行 → "请选择角色"
        ❌ 多于 1 行 → "请选择一个角色"
        ❌ checkCanCopy 不通过 → "您无权复制当前角色，请联系角色所属管理员分组的用户，设置角色的公开范围"
        ❌ enable == false 已禁用 → "禁用状态不允许复制"
        ✅ → showNewRole(roleId) (L1244-L1256)
            - 跳转 form hrcs_modifyrole（不是 hrcs_newrole）
            - 携带 customParam: roleId（源角色） + copy="1"（复制模式）
            - showType=MainNewTabPage · 新 Tab 页打开
            - PageId 加随机数避免冲突
   args.setCancel(true) (L1064)

3. 用户在 hrcs_modifyrole 编辑页改名/改编号 → 保存 ·  此时 HR 域复制逻辑由该页插件处理 ·  不在本场景范围
```

### 3.5 其他关键 opKey 流转（简表）

| opKey | 触发 | 主要动作 |
|---|---|---|
| `new` (新增) | beforeDoOperation case "new" → openNewRolePage L1171 | 跳转 hrcs_newrole · groupId 自动填当前节点 · args.setCancel(true) |
| `bar_assignmember` (分配角色成员) | beforeItemClick → validateAssignMember L936 → beforeDoOperation case → beforeAssignRole L1153 | 跳转 hrcs_modifyrole + useroperation=assignmember + roleName + roleId |
| `bar_initrole` (角色权限初始化) | beforeDoOperation case → rolePermInit L883 | 列表 hrcs_perminitrecord 跳转 |
| `inituserperm` (用户权限初始化) | beforeDoOperation case → userPermInit L874 | 列表 hrcs_perminitrecord 跳转 |
| `bar_exportrole` (导出角色权限) | beforeDoOperation case → exportRole L1744 → showProgressForm L1751 | 跳转 hrcs_exportperm + taskClassName=RoleExportTask |
| `exportroleperm` (导出用户权限) | beforeDoOperation case → exportRolePerm L1730 → showProgressForm | 同上 + taskClassName=RoleUserExportTask · 但前置先校验 PermHelper.getUserRelatesByRoleIds 不为空 |
| `refresh` (刷新) | itemClick (L1101-L1112) | clearSelection + refresh |

### 3.6 树节点交互（btnnew/btnedit/btndel）

```
treeToolbarClick (L434-L453)
  case "btnnew":
    addGroupNode(ENTITYTYPE_HRROLEGRP=hrcs_rolegrp, currentNodeId)
    → 弹窗 form hrcs_rolegrp + customParam operate=addnew + tree_parent_id={key,value}
  case "btnedit":
    editAction (L479-L493)
      ❌ 根节点不允许编辑
      ❌ 空节点不允许编辑
      ❌ 不允许刷新树情况下不允许修改
      ✅ → editGroupNode 弹窗 form hrcs_rolegrp + 编辑模式
  case "btndel":
    delAction (L456-L477)
      ❌ 根节点不允许删除
      ❌ 不允许刷新树情况下不允许删除节点
      ❌ canDelGroup 不通过 (含下级或包含角色) → 拦截
      ✅ → showConfirm "您确认删除分组 X？" + ConfirmCallBackListener("group_bar_del", this)
      → 用户选 Yes → confirmCallBack (L499-L503) → doDeleteGroup (L505-L537)
        - getTreeModel().deleteGroup({nodeId})
        - HRBaseServiceHelper(hrcs_rolegrp).delete(list)
        - 刷新树 + 当前节点
```

---

## 四、典型操作 → 数据流时序图

```
[用户] 点击【删除】 (3 行选中: id=A,B,C)
   ↓
[FormPlugin] beforeItemClick → handlePage 校验
   ↓ checkRoleInfoExist(每行 roleId) ── 数据库 perm_role
   ↓
[FormPlugin] beforeDoOperation → handleBeforeOperatetionEvent → deleteRoleInfo
   ↓
[Service] HRRolePermHelper.queryUserAdminGroupInfos(uid) ── 数据库 hrcs_admingrp 等
   ↓
[Service] RoleServiceHelper.getRoleMembers(rolesIds) ── 数据库 perm_role + hrcs_userrolerelat
   ↓ Map<roleId, {number, enable, issyspreset, memberCount}>
   ↓
[FormPlugin] checkCanDel → 4 重校验
   ↓ if 拦截 → 早返回
   ↓
[FormPlugin] confirmDeleteRoleInfo
   ↓ for each roleId: PermLogService.initPermLog("del", ...)
   ↓
[FormPlugin] doDeleteBosRole [TX.required] (7 张 BOS 表)
   ↓
[FormPlugin] doDeleteHrRole [TX.required] (16 张 HR 表)
   ↓
[Service] ILogService.addBatchLog(appLogInfoList)
   ↓
[FormPlugin] BillList.clearSelection + updateView
   ↓
[用户] 看到"删除成功"
```

---

## 五、关键 OP / FormPlugin 链上的"拦点" 速查

| 拦点位置 | 方法 | 拦哪些 opKey | 逻辑 |
|---|---|---|---|
| `preOpenForm` | `HRAdminStrictPlugin.showMesIfUserIsNotAdmin` | 全部（页面级） | 非 HR 管理员直接拒打开页面 |
| `setFilter` | `HRRoleListPlugin.setFilter` | 列表查询 | 加 viewableRoles 过滤（行级权限） |
| `beforeItemClick` | `HRRoleListPlugin.beforeItemClick` | 12 个工具栏按钮 | 校验选中数 + checkRoleInfoExist 角色存活 |
| `beforeDoOperation` | `HRRoleListPlugin.beforeDoOperation` | 12 opKey 全部 | 在标品 OP 链前阻断 + 自定义流程跳转 |
| `afterDoOperation` | `HRRoleListPlugin.afterDoOperation` | 全部 | 清 itemKey pageCache |
| `closedCallBack` | `HRRoleListPlugin.closedCallBack` | "exportUrl" actionId | 处理导出 URL 回调（task end → download URL） |

---

## 六、工作流不存在的说明（与其他场景对比）

`hrcs_rolelist` 是**没有工作流**的场景（和 hrcs_dynascheme 不同）：

| 场景 | submit | unsubmit | audit | unaudit | confirmchange | save | enable | disable | delete |
|---|---|---|---|---|---|---|---|---|---|
| `hrcs_rolelist` | ❌ 无 | ❌ 无 | ❌ 无 | ❌ 无 | ❌ 无 | 列表本身没保存 | ✅ | ✅ | ✅ |
| `hrcs_dynascheme` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| `haos_adminorg` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

**为什么本场景没工作流**？
- 角色基础资料的修改不需要"审批" —— HR 管理员就是最终决策者
- 角色启用/禁用是"运维操作" · 直接生效
- 删除走的是"二阶段确认"（弹窗 + 提示）+ 业务约束拦截（4 重 checkCanDel）· 替代了审批流的角色

如果客户要求"角色启用/禁用要走审批"，必须改造 form 类型从 QueryListModel 到 BillFormModel —— 这是**架构级改动**·成本极高 · 应该 push back 业务 · 看是否能接受"管理员组分级 + canOperateRole 校验"的标品方式（更轻）。
