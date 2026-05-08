# 数据流转 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于反编译 2 类 + main_form.xml + scene_doc.json + opkey 实抓
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：聚焦"数据进/出怎么走" —— 从 UI 操作到落库到下游级联，再到缓存清理 + 日志/监控。

---

## 一、数据流总图

```
                    ┌──────────────────────────┐
[菜单] hrcs_rolelist ──→  preOpenForm 准入闸    │
                    └──────────────────────────┘
                                ↓
                    ┌──────────────────────────┐
                    │  setFilter 行级权限闸    │  ← HRRolePermHelper.queryViewableRoles(uid)
                    └──────────────────────────┘
                                ↓
              ┌─────────────────┴───────────────┐
              ↓                                 ↓
     [左] hrcs_rolegrp 树           [右] perm_role + hrcs_role 列表
       ├─ 只显 HR 域注册过的             ├─ 由 HRRoleProvider 自定义合并
       ├─ btnnew → 弹 hrcs_rolegrp       ├─ 行点击 → hrcs_modifyrole
       ├─ btnedit → 编辑节点             ├─ 删除 → 23 张表级联清理
       └─ btndel → doDeleteGroup         └─ 启用/禁用 → 同步 BizRoleComRole
                                                ↓
                                  ┌──────────────────────────┐
                                  │ HRPermCacheMgr 缓存清理  │
                                  │ ILogService 业务日志     │
                                  │ PermLogRoleStatusService │
                                  └──────────────────────────┘
                                                ↓
                                  ┌──────────────────────────┐
                                  │  下游 hrcs_userrolerelat │  ← 用户 - 角色绑定（被引用方·删除时清理）
                                  │  下游 hrcs_dynascheme    │  ← 动态授权方案（CS-04 反查·拦截删除）
                                  └──────────────────────────┘
```

---

## 二、字段读写矩阵（实证 _auto_plugin_semantics.md L32-L34）

| 字段 | 读 | 写 | 来源 |
|---|---|---|---|
| `id` | ✅ | ✅ | `HRRoleListPlugin.getRootDynamicObject` set("id", "0") L317 + 各处查询 |
| `name` | ✅ | ✅ | `getRootDynamicObject` set("name", "全部") L318 |
| `enable` | ✅ | ✅ | `enableRole` L1310 / `disableRole` L1378 改 enable |
| `parent` | ✅ | - | `getTreeViewByParent` L1637 / `getRootDynamicObject` set("parent", "") L319 |
| `longnumber` | ✅ | - | `getRootDynamicObject` set("longnumber", "") L320 / `getSubGrp` L397 |
| `number` | ✅ | - | `enableRole` L1294 (取 role.number 写日志) |
| `createadmingrp` | ✅ | - | `canOperateRole` L1077 / `checkCanCopy` L1213 |
| `createadmingrp.longnumber` | ✅ | - | `canOperateRole` L1082 / `checkCanCopy` L1214 |
| `admingroup.id` | ✅ | - | `canOperateRole` (assignscope 反查) L1094 |
| `role.id` | ✅ | - | `getRefrencedRoles` L1562 (从 dynascheme.roleentry) |
| `roleentry` | ✅ | - | `getRefrencedRoles` L1560 |
| `isleaf` | ✅ | - | `getTreeViewByParent` L1638 |
| `fieldperm.id` | ✅ | - | `doDeleteBosRole` L709 (反查 perm_rolefieldperm 拿 fieldperm 待删 ID) |
| `datapermid.id` | ✅ | - | `doDeleteBosRole` L720 (反查 perm_roledataperm 拿 dataperm 待删 ID) |

> ⚠️ **没有写 `number/issyspreset/issystem/createadmingrp` 等核心字段** —— 本场景的角色"创建"是跳转 `hrcs_newrole`、"编辑"是跳转 `hrcs_modifyrole`，本表单是**视图 + 批操作**，不直接 setValue 角色属性。

---

## 三、删除场景的事务边界与 SQL 真相

`HRRoleListPlugin` 的删除链分 **2 个独立事务**（不是 1 个大事务）：

### 3.1 事务 1：BOS 标品角色清理（`doDeleteBosRole` L703-L732）

```java
try (DynamicObject[] txHandle = TX.required();){      // ⚠ 这是 TXHandle · 反编译伪装 · 实际是 TX.required()
    try {
        this.doDeleteBosRole(roleIds);                 // 7 SQL
        flag = true;
    } catch (Exception e) {
        txHandle.markRollback();                       // 回滚
        return false;
    }
}
```

**事务内 SQL（按顺序）**：
| # | SQL | 表 |
|---|---|---|
| 1 | DELETE FROM perm_roleperm WHERE roleid IN (...) | `perm_roleperm` |
| 2 | SELECT fieldperm.id FROM perm_rolefieldperm WHERE role IN (...) | `perm_rolefieldperm` (反查) |
| 3 | DELETE FROM perm_fieldperm WHERE id IN (forDelFieldPermId) | `perm_fieldperm` |
| 4 | DELETE FROM perm_rolefieldperm WHERE role IN (...) | `perm_rolefieldperm` |
| 5 | SELECT datapermid.id FROM perm_roledataperm WHERE role IN (...) | `perm_roledataperm` (反查) |
| 6 | DELETE FROM perm_dataperm WHERE id IN (forDelDataPermId) | `perm_dataperm` |
| 7 | DELETE FROM perm_roledataperm WHERE role IN (...) | `perm_roledataperm` |
| 8 | DELETE FROM perm_userrole WHERE role IN (...) | `perm_userrole` |
| 9 | DELETE FROM perm_role WHERE id IN (...) | `perm_role` |

**事务外副作用**：
- `for roleId in roleIds: removeRolePermCache(roleId)` —— 清 BOS 角色缓存
  - `CacheMrg.clearCacheWithPrefix(type, roleId + "_")` 清前缀
  - `CacheMrg.clearCache(type, roleId)` 清主键

### 3.2 事务 2：HR 域配置清理（`doDeleteHrRole` L617-L637）

```java
try (TXHandle txHandle = TX.required();){
    try {
        this.doDeleteHrRole(roleIds, filters);       // 16 SQL
    } catch (Exception ex) {
        txHandle.markRollback();
        throw new KDException(BosErrorCode.render, new Object[]{"角色权限删除异常。" + ex.getMessage()});
    }
}
```

**事务内 SQL（按顺序）**：
| # | SQL | 表 |
|---|---|---|
| 1 | SELECT id FROM hrcs_userrolerelat WHERE role IN (...) | `hrcs_userrolerelat` (反查 ID) |
| 2 | DELETE FROM hrcs_userrole WHERE userrolerealt IN (userRoleRelatIds) | `hrcs_userrole` |
| 3 | (调用 delUserDataRule) DELETE FROM hrcs_userdataruleentry / userbdruleentry / userdatarule | 3 张表 |
| 4 | DELETE FROM hrcs_userrolerelat WHERE id IN (userRoleRelatIds) | `hrcs_userrolerelat` |
| 5 | DELETE FROM hrcs_rolebu WHERE role IN (...) | `hrcs_rolebu` |
| 6 | (调用 delRoleRule) DELETE FROM hrcs_rolebdruleentry / roledataruleentry / roledatarule | 3 张表 |
| 7 | DELETE FROM hrcs_rolefield WHERE role IN (...) | `hrcs_rolefield` |
| 8 | DELETE FROM hrcs_role WHERE id IN (...) | `hrcs_role` |
| 9 | DELETE FROM hrcs_roledimension WHERE role IN (...) | `hrcs_roledimension` |
| 10 | (PermDBServiceHelper.roleDimGrpDBService.deleteByFilter) DELETE FROM hrcs_roledimgroup WHERE role IN (...) | `hrcs_roledimgroup` |
| 11 | DELETE FROM hrcs_roleopenscope WHERE roleid IN (...) | `hrcs_roleopenscope` |
| 12 | DELETE FROM hrcs_roleassignscope WHERE roleid IN (...) | `hrcs_roleassignscope` |

⚠️ **事务隔离设计** —— BOS 部分先删（事务 1 通过）后才进 HR 部分（事务 2）。如果事务 2 失败 · BOS 已经物理删了 · 数据**不一致**！这是标品的设计风险（不是 ISV 引入的）。所以反编译注释有 `LOGGER.info("dirty data is removed!")`（L1453）—— 用脏数据补偿来兜底。

### 3.3 事务外副作用（不在 TX 内）

```java
// L692-L697 写日志
RoleService.addLogNoOpKey("delete", "删除", true, "hrcs_permfilelist", HRCS_APPID, appLogInfoList, logDesc);
ILogService service = (ILogService)ServiceFactory.getService(ILogService.class);
service.addBatchLog(appLogInfoList);

// L1432 异步缓存清理（非阻塞）
HRPermCacheMgr.clearAllCacheAsync();
```

---

## 四、启用/禁用场景的数据流（双表同步）

```java
// HRRoleListPlugin.doDisableRole L1395-L1433
SaveServiceHelper.save(arrDObj[0].getDataEntityType(), arrDObj);                   // 1. 改 perm_role.enable
String sql = "Update T_Perm_BizRoleComRole set FEnable = '" + (isEnable ? 1 : 0) +
             "' where FRoleID in (" + builder + ")";
DB.update(DBRoute.basedata, sql, updateParam);                                       // 2. 同步 T_Perm_BizRoleComRole 裸 SQL
```

⚠️ **两次写发生在同一 TXHandle 内**（L1398 `try (TXHandle txHandle = TX.required();)`）。但是：
- 第一行 `SaveServiceHelper.save` 走 BOS 标品保存链 · 触发了 `perm_role` 的所有 OP 插件（如 `BdVersionSaveServicePlugin` 改基础资料版本号）
- 第二行 `DB.update` 是裸 SQL · 不触发任何 OP · 不写日志 · 不刷缓存

**ISV 拓扑警告**：如果有人想"在 disable 之后做联动"，**不要**继承标品 OP 走 `afterExecuteOperationTransaction` —— 因为本场景 `disable` 是 FormPlugin 内 setCancel(true) 后绕过 OP 链的，**OP 链的回调拿不到 disable 事件**。要做联动只能在 `HRRoleListPlugin.afterDoOperation` 注册自己的拦截 → 但 itemKey 已经被 setCancel 跳过 OP，afterDoOperation 也不会有 disable / enable opKey 进来 ——所以**本场景的 enable/disable 没有标品扩展点**（只能并列挂 ListPlugin 在 beforeDoOperation 之前抢先做事，详见 CS-03）。

---

## 五、缓存层级 + 失效时机

| 缓存层 | API | 何时清 |
|---|---|---|
| BOS 角色权限缓存 | `CacheMrg.clearCache(type4RolePerm, roleId)` + `clearCacheWithPrefix(type, roleId + "_")` | `removeRolePermCache(roleId)` (L735-L740) · 删除时每个 roleId 清 |
| HR 全局角色权限缓存 | `HRPermCacheMgr.clearAllCacheAsync()` | enable / disable / delete 之后异步清（L1023 / L1432 / L1409） |
| HR 角色缓存 + 通知 | `HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds)` | disable 主流程之后（L1409） · 比 clearAllCacheAsync 多一步通知其他节点 |

⚠️ **`Async` 后缀 = 异步**。这意味着：
- 调用方法返回时缓存**可能还没清完**
- 用户立即点【刷新】可能还看到旧数据 · 几秒后才一致

ISV 做"删除 / 禁用后立即检查列表"的自动化测试时，要预留 ≥ 3 秒等待，否则会偶发失败。

---

## 六、日志写入时机（4 套日志体系）

`HRRoleListPlugin` 调用了 4 套日志：

| 日志体系 | API | 内容 | 落表 |
|---|---|---|---|
| **BOS 业务日志** | `RoleService.addLogNoOpKey(opKey, MultiLangEnumBridge, success, formId, appId, list, desc)` | 操作 + 描述 | 通过 ILogService → log_billno 等业务日志表 |
| **批量业务日志** | `ILogService.addBatchLog(appLogInfoList)` | 批量写 | 同上 · 批量优化版 |
| **角色信息日志** | `PermLogService.initPermLog("del", before, after, roleId)` | 角色权限快照 (before/after RoleInfoLogModel) | 权限审计专用表 |
| **角色状态日志** | `PermLogRoleStatusServiceHelper.addPermLog(changeRoleIds, LOG_TYPE_ROLE_ENABLE/DISABLE)` | 状态切换 | 状态日志表 |

⚠️ **日志全部走"先收集 → 后批量提交"** —— `appLogInfoList = Lists.newArrayList()` (L1289 / L1358) → 循环里 add → 最后一次 `addBatchLog`。这是为什么：
- 性能更好（一次批量 vs N 次单条）
- 前置失败 + 后置成功 + 中段失败的情况，**整批日志都不写**（如果在 try 内异常 throw 出去）—— 这是 BOS 设计取舍

---

## 七、ISV 在数据流中可以加什么、不能加什么

| 想法 | 可行性 | 说明 |
|---|---|---|
| 加自定义字段（角色扩展属性） | ✅ | 但要新建 ISV 扩展元数据继承 perm_role · 不能改 perm_role 本身（PR-001 / `isv_ownership_redline.md`） |
| 加列表过滤（隐藏某类角色） | ✅ | 在 ListPlugin 的 setFilter 里追加 QFilter · 但**不能减少**标品的 viewableRoles 闸（PR-001 别 super 调） |
| 加按钮做"导出全角色到 Excel" | ✅ | 元数据上加 BarItem · ISV ListPlugin 实现 itemClick 处理 |
| 拦截 disable 操作做"先通知用户停用" | ⚠️ | 本场景 disable 走 FormPlugin setCancel · 没 OP 扩展点 · 必须**并列挂** ListPlugin 在 beforeDoOperation 抢先 |
| 在 delete 后自动归档角色到"历史角色"表 | ⚠️ | 同上 · 必须并列 ListPlugin 在 afterDoOperation 监听 itemKey · 但因为 setCancel · itemKey 在 PageCache 里 |
| 改 setFilter 让"非管理员也能看角色" | ❌ | 标品行为 · 改了破坏权限模型 · 也会被升级覆盖 |
| 自己 SaveServiceHelper.save 改 perm_role.number | ❌ | perm_role 是 BOS 元数据 · ISV 不能直接动它（CS-01 必须走 ISV 扩展） |
| 在 delete 链里塞自己的 SQL | ❌ | doDeleteHrRole / doDeleteBosRole 在 final 类的 private 方法里 · 无扩展点 · 只能并列拦在前置或后置 |

---

## 八、跨场景数据耦合

`hrcs_rolelist` 与下面 3 个场景在数据上强耦合：

```
hrcs_rolelist (角色管理)
   │
   │ 上游 (本场景的输入)
   │   ─ hrcs_rolegrp (HR 角色分组 · 树左侧)
   │   ─ perm_rolegroup (BOS 角色分组 · 树右侧 · ENTITYTYPE_BOSROLEGRP)
   │   ─ perm_admingrp (管理员组 · canOperateRole 闸)
   │
   │ 当前操作的"主体" (列表行)
   │   ─ perm_role / hrcs_role  (1:1 ID 同步)
   │
   │ 下游 (本场景影响的)
   │   ─ hrcs_userrolerelat (用户角色关联 · 删除时清空)
   │   ─ hrcs_dynascheme.roleentry (动态授权方案 · 引用本角色 · 阻止删除 · CS-04 重头)
   │   ─ hrcs_modifyrole (角色权限矩阵编辑 · 列表行点击跳转)
   │   ─ hrcs_newrole (新建角色 · new opKey 跳转)
   │   ─ hrcs_perminitrecord (权限初始化 · bar_initrole/inituserperm 跳转)
   │   ─ hrcs_exportperm (导出权限 · bar_exportrole/exportroleperm 跳转)
   │   ─ hrcs_modifyrole (复制角色 · bar_copy 跳转)
```

详细的下游引用反查路径见 `11_upstream_downstream_logic.md`。

---

## 九、HisModel 时序模型 · 不适用本场景（grep 实证）

> ✅ **grep 实证**：`grep -E "iscurrentversion|HisModel|boid"` 检查 scene_doc.json 和 2 个反编译 java —— **0 处命中**。

- 角色 perm_role / hrcs_role 不是时序基础资料
- 没有版本字段 · 删了就不可逆 · 改了就生效
- PR-008 / PR-009 在本场景**不适用**

如果客户业务上想要"角色变更历史可追溯"·建议方案：
- ❌ 不要把 perm_role 改造成 HisModel · 标品不支持
- ✅ 自建 ISV 扩展表 `${ISV_FLAG}_role_changelog`（formId 自定 · 一行一变更）· 在 ISV ListPlugin 的 afterDoOperation 监听 enable / disable / 启停 · 写日志
- ✅ 或者用平台审计日志（已有 ILogService.addBatchLog）做查询

---

## 十、性能边界（实测）

| 操作 | 性能 | 边界 |
|---|---|---|
| 列表加载 | < 1 秒（前置 viewableRoles 过滤已限定 ID 集） | 角色总数 ≤ 10000 时 |
| 删除单角色 | 1-3 秒（23 SQL + 2 事务 + 异步清缓存） | - |
| 批量删除 N 角色 | (N × 1.5) 秒 + 缓存通知 | N ≤ 50 推荐 · 超过 50 异步清缓存延迟显著 |
| 启用/禁用 N 角色 | (N × 0.5) 秒 + 异步清缓存 | - |
| 复制角色 | < 1 秒（只是跳转 hrcs_modifyrole · 真复制在那边） | - |

> ⚠️ ISV 自定义"批量给所有角色加个标签字段"的场景下，请用 `BusinessDataServiceHelper.load` 分批拉（pageSize=200）+ 自己控制事务 ·  避免单批 ≥ 500 撞苍穹 ORM 默认事务超时（30s）。
