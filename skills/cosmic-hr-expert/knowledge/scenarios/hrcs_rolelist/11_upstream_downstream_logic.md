# 上下游联动 · 角色管理 (hrcs_rolelist)

> **状态**: 🟢 基于 scene_doc.json + rules_chain_all.json 15 opKey + form_lifecycle_rules.json 22 rules + curated_sdk.json + CFR 反编译 2 类 + 02_business_rules.md + 05_data_flow.md
> **confidence**: verified（上游树节点双实体合并来自 HRRoleListPlugin 实证 · 下游 23 张表删除链来自 doDeleteBosRole/doDeleteHrRole 反编译实证 · 行级权限闸来自 setFilter 实证）

---

## 一、上下游全景图

```
                         ┌──────────────────────────────────────┐
                         │  上游（角色 group 树 · 双实体合并）     │
                         │  ────────────────────                 │
                         │  perm_rolegroup   （BOS 角色组基础资料）│
                         │  hrcs_rolegrp     （HR 角色组注册表）  │
                         │  CommonTreeListView 合并双实体树节点   │
                         │                                      │
                         │  perm_admingrp    （BOS 管理员组）      │
                         │  canOperateRole 闸校验管理员组归属     │
                         │                                      │
                         │  hrcs_userrolerelat （用户-角色关联）  │
                         │  delete 时级联清理                     │
                         └─────────────┬────────────────────────┘
                                       │ perm_rolegroup 为树基础
                                       │ hrcs_rolegrp 注册才可见
                                       │ perm_admingrp 决定操作权限
                                       ↓
                         ┌──────────────────────────────────────┐
                         │  hrcs_rolelist （主场景 · 本文档）     │
                         │                                      │
                         │  左侧: 角色分组树（CommonTreeListView） │
                         │  右侧: 角色列表（HRRoleProvider 合并）  │
                         │                                      │
                         │  perm_role + hrcs_role 双表 1:1 ID   │
                         │  HRRoleListPlugin extends            │
                         │     HRStandardTreeList               │
                         │  22 条 FormPlugin 规则               │
                         │  15 opKey · 2 FormPlugin 类          │
                         └─────────────┬────────────────────────┘
                                       │
           ┌───────────────────────────┼──────────────────────────┐
           │                           │                          │
           ↓ 删除级联（23 张表）       ↓ 被引用（阻止删除）        ↓ 跳转子页面
           │                           │                          │
  ┌────────────────────┐    ┌──────────────────────┐    ┌────────────────────────────┐
  │ 事务1: BOS 侧 9 表  │    │ hrcs_dynascheme       │    │ hrcs_modifyrole           │
  │ ────────────────    │    │ ────────────────      │    │  (角色详细信息编辑)        │
  │ perm_roleperm       │    │ getRefrencedRoles()   │    │                            │
  │ perm_rolefieldperm  │    │ L1555-L1568           │    │ hrcs_newrole              │
  │ perm_fieldperm      │    │ 反查 roleentry.role   │    │  (新建角色)                │
  │ perm_roledataperm   │    │ 引用本角色 id         │    │                            │
  │ perm_dataperm       │    │ → 阻止删除 (R2)       │    │ hrcs_perminitrecord       │
  │ perm_userrole       │    └──────────────────────┘    │  (权限初始化记录)          │
  │ perm_role           │                                │                            │
  │                     │    ┌──────────────────────┐    │ hrcs_exportperm           │
  │ 事务2: HR 侧 14 表  │    │ BizRoleComRole        │    │  (导出权限进度)           │
  │ ────────────────    │    │ ────────────────      │    └────────────────────────────┘
  │ hrcs_userrole       │    │ doDisableRole         │
  │ hrcs_userrolerelat  │    │ L1401:                │
  │ hrcs_userdatarule*  │    │ DB.update(DBRoute.    │
  │ hrcs_rolebu         │    │   basedata,           │
  │ hrcs_roledatarule*  │    │   "Update T_Perm_     │
  │ hrcs_rolefield      │    │    BizRoleComRole...")│
  │ hrcs_role           │    │ 同步刷 FEnable        │
  │ hrcs_roledimension  │    └──────────────────────┘
  │ hrcs_roledimgroup   │
  │ hrcs_roleopenscope  │
  │ hrcs_roleassignscope│
  └────────────────────┘
```

---

## 二、上游依赖清单（hrcs_rolelist 依赖谁）

### 2.1 双实体角色组树（核心上游）

角色列表左侧的树由 **两个实体合并**：

| 上游实体 | 角色 | 实证 |
|---|---|---|
| `perm_rolegroup` (ENTITYTYPE_BOSROLEGRP) | BOS 标品角色组 · CommonTreeListView 构造参数 · `createTreeListView` L304-L307 (FP_HRR4) | 树绑定的基础实体 |
| `hrcs_rolegrp` (ENTITYTYPE_HRROLEGRP) | HR 注册的角色组 · F7 过滤时只显示 hrcs_rolegrp 中存在的组 | `filterContainerBeforeF7Select` L418-L432 (FP_HRR9) |

**树节点合并逻辑**（HRRoleListPlugin · 常量区 L86-L89）：

```java
// ENTITYTYPE_BOSROLEGRP = "perm_rolegroup"
// ENTITYTYPE_HRROLEGRP = "hrcs_rolegrp"
// ID_ROOTNODE = "0"
// ID_ROOTNODE_PRESET = "8609760E-EF83-4775-A9FF-CCDEC7C0B689"
```

**树 F7 过滤**（FP_HRR9 · L418-L432）：角色组 F7 弹窗查询全量 hrcs_rolegrp id 列表 → 以 `QFilter("id", "in", idList)` 注入 F7 → 只有 HR 域注册过的角色组才出现在选择列表中。

失败影响：hrcs_rolegrp 表为空 → F7 无候选 → 无法给角色选择分组。

### 2.2 管理员组权限上游

| 上游实体 | 用途 | 消费方法 |
|---|---|---|
| `perm_admingrp` | canOperateRole 闸（L1075-L1099）校验当前用户是否在角色的 createadmingrp 范围内 | `canOperateRole` 用 `masterGroup` 判断用户 group 是否等于 createadmingrp |
| `hrcs_roleassignscope` | canOperateRole 的第四层判断（L1093-L1096）→ 存在 roleid=this + admingroup IN currUserGroups + ismodifiable=1 则通过 | `canOperateRole` L1094 |
| `hrcs_roleopenscope` | checkCanCopy 的公开范围判断（L1218）→ 公开角色允许复制 | `checkCanCopy` L1218 |

### 2.3 数据加载上游（HRRoleProvider + viewableRoles）

| 上游服务 | 用途 | 实证位置 |
|---|---|---|
| `HRRoleProvider` (内部类) | `beforeCreateListDataProvider` L1762-L1764 (FP_HRR18) → 替换苍穹默认数据提供者 · 合并 perm_role + hrcs_role 双表 | `beforeCreateListDataProvider` |
| `HRRolePermHelper.queryViewableRoles(currUserId)` | `setFilter` L367-L372 (FP_HRR7) → 查询当前用户可见的角色 ID 列表 · 以 QFilter("id", "in", ...) 注入列表 | `setFilter` |
| 默认启用过滤 `perm_role.enable = "1"` | `filterContainerInit` L407-L415 (FP_HRR8) → 列表初次打开默认只显示已启用的角色 | `filterContainerInit` |

---

## 三、下游消费清单（谁依赖 hrcs_rolelist）

### 3.1 BOS 标品角色相关表（删除事务 1 · 9 张表）

`doDeleteBosRole` (L703-L732) · 在独立 `TX.required()` 事务内执行：

| # | SQL | 表 | 实证 |
|---|---|---|---|
| 1 | DELETE FROM perm_roleperm WHERE roleid IN (...) | `perm_roleperm` | L707 |
| 2 | SELECT fieldperm.id FROM perm_rolefieldperm | `perm_rolefieldperm` (反查ID) | L709 |
| 3 | DELETE FROM perm_fieldperm WHERE id IN (...) | `perm_fieldperm` | L710-L713 |
| 4 | DELETE FROM perm_rolefieldperm WHERE role IN (...) | `perm_rolefieldperm` | L714 |
| 5 | SELECT datapermid.id FROM perm_roledataperm | `perm_roledataperm` (反查ID) | L720 |
| 6 | DELETE FROM perm_dataperm WHERE id IN (...) | `perm_dataperm` | L721-L724 |
| 7 | DELETE FROM perm_roledataperm WHERE role IN (...) | `perm_roledataperm` | L724 |
| 8 | DELETE FROM perm_userrole WHERE role IN (...) | `perm_userrole` | L727 |
| 9 | DELETE FROM perm_role WHERE id IN (...) | `perm_role` | L728 |

**事务外副作用**：`removeRolePermCache(roleId)` 异步清 BOS 缓存 → `CacheMrg.clearCache(type4RolePerm, roleId)` + `clearCacheWithPrefix(type, roleId + "_")`

### 3.2 HR 域角色相关表（删除事务 2 · 14 张表）

`doDeleteHrRole` (L617-L637) · 在独立 `TX.required()` 事务内执行：

| # | SQL | 表 | 实证位置（05_data_flow.md） |
|---|---|---|---|
| 1 | SELECT id FROM hrcs_userrolerelat WHERE role IN (...) | `hrcs_userrolerelat` (反查) | 3.2 #1 |
| 2 | DELETE FROM hrcs_userrole WHERE userrolerealt IN (...) | `hrcs_userrole` | 3.2 #2 |
| 3 | 调 delUserDataRule → DELETE 3 张表 | `hrcs_userdataruleentry` / `hrcs_userbdruleentry` / `hrcs_userdatarule` | 3.2 #3 |
| 4 | DELETE FROM hrcs_userrolerelat WHERE id IN (...) | `hrcs_userrolerelat` | 3.2 #4 |
| 5 | DELETE FROM hrcs_rolebu WHERE role IN (...) | `hrcs_rolebu` | 3.2 #5 |
| 6 | 调 delRoleRule → DELETE 3 张表 | `hrcs_rolebdruleentry` / `hrcs_roledataruleentry` / `hrcs_roledatarule` | 3.2 #6 |
| 7 | DELETE FROM hrcs_rolefield WHERE role IN (...) | `hrcs_rolefield` | 3.2 #7 |
| 8 | DELETE FROM hrcs_role WHERE id IN (...) | `hrcs_role` | 3.2 #8 |
| 9 | DELETE FROM hrcs_roledimension WHERE role IN (...) | `hrcs_roledimension` | 3.2 #9 |
| 10 | DELETE FROM hrcs_roledimgroup WHERE role IN (...) | `hrcs_roledimgroup` | 3.2 #10 |
| 11 | DELETE FROM hrcs_roleopenscope WHERE roleid IN (...) | `hrcs_roleopenscope` | 3.2 #11 |
| 12 | DELETE FROM hrcs_roleassignscope WHERE roleid IN (...) | `hrcs_roleassignscope` | 3.2 #12 |

**关键风险**：两个事务独立 · BOS 事务 1 先通过 → 事务 2 失败 → BOS 已物理删 · HR 侧未删 → 数据不一致（标品设计已知 · 有脏数据补偿日志 `LOGGER.info("dirty data is removed!")` L1453）。

### 3.3 hrcs_dynascheme · 动态授权方案（受保护下游 · 阻止删除）

`getRefrencedRoles` (L1555-L1568) · 删除前反查：

```java
// HRRoleListPlugin.java L1555-L1568
// 反查 hrcs_dynascheme WHERE roleentry.role.id IN (roleIds)
// 返回被引用的 roleId 集合
// → checkCanDel (L1506-L1511): 若 roleId 在集合中 → 报错阻断
```

这是 **hrcs_rolelist 与 hrcs_dynascheme 唯一的硬删除约束**（02_business_rules.md R2 · 3.2 节）。

### 3.4 BizRoleComRole · 业务角色复用关系（启用/禁用同步）

`doDisableRole` L1401 裸 SQL 同步刷 `T_Perm_BizRoleComRole.FEnable`：

```java
// DB.update(DBRoute.basedata,
//   "Update T_Perm_BizRoleComRole set FEnable = '" +
//   (isEnable ? 1 : 0) + "' where FRoleID in (" + builder + ")")
```

两写同事务 (`TX.required()`) · 但第二写不触发 BOS OP 链（不写日志/不刷缓存）。ISV 不要碰这张表。

### 3.5 跳转子页面（非数据写入 · Modal/Page 跳转）

| 子页面 formId | 触发按钮 | 实证 |
|---|---|---|
| `hrcs_modifyrole` | 列表行超链接点击 (FP_HRR13 · L959-L986) / bar_copy 复制 (FP_HRR14 · L1048) | `billListHyperLinkClick` → setFormId("hrcs_modifyrole") + setCustomParam("roleId", roleId) |
| `hrcs_newrole` | new 按钮 (FP_HRR14 · L1045) | `openNewRolePage` → setFormId("hrcs_newrole") |
| `hrcs_perminitrecord` | bar_initrole (角色权限初始化) / inituserperm (用户权限初始化) | FP_HRR14 · L1040 / L1015 |
| `hrcs_exportperm` | bar_exportrole / exportroleperm | FP_HRR14 · L1032 / L1021 |
| `hrcs_rolegrp` | btnnew 树按钮 (FP_HRR10 · L439) | `addGroupNode` → setFormId("hrcs_rolegrp") |

### 3.6 缓存层（异步失效）

| 缓存层 | API | 触发时机 | 失效延迟 |
|---|---|---|---|
| BOS 角色权限缓存 | `CacheMrg.clearCache(type, roleId)` + `clearCacheWithPrefix(type, roleId + "_")` | 删除每个 roleId 后 (L735-L740) | 同步 · 0ms |
| HR 全局角色权限缓存 | `HRPermCacheMgr.clearAllCacheAsync()` | enable / disable / delete 后 (L1023 / L1409 / L1432) | 异步 · 可能数秒 |
| HR 角色缓存 + 跨节点通知 | `HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds)` | disable 主流程后 (L1409) | 异步 · 多节点延迟更长 |

### 3.7 日志体系（4 套 · 非阻塞下游）

| 日志体系 | API | 触发时机 | 实证 |
|---|---|---|---|
| BOS 业务日志 | `RoleService.addLogNoOpKey(opKey, name, success, formId, appId, list, desc)` | 每个 enable/disable/delete/复制操作 | 02_business_rules.md 第九节 |
| 批量业务日志 | `ILogService.addBatchLog(appLogInfoList)` | 收集完毕后批量提交 | 同上 |
| 角色信息日志 | `PermLogService.initPermLog("del", before, after, roleId)` | delete 操作 (仅) | 同上 #2 |
| 角色状态日志 | `PermLogRoleStatusServiceHelper.addPermLog(roleIds, LOG_TYPE_xxx)` | enable / disable (仅) | 同上 #3 |

---

## 四、跨模块联动场景

### 4.1 删除角色 → 2 事务 23 张表级联

```
[列表] 选中角色 → 点 delete
   ↓
[前置闸] HRRoleListPlugin.beforeItemClick (FP_HRR11 L829-L861):
   - validateDelete: 必选行 + 预置不删(R1) + dynascheme引用不删(R2)
     + 当前管理员不删(R3) + 启用或有成员不删(R4)
   - setCancel(false) 放行
   ↓
[分发] beforeDoOperation handleBeforeOperatetionEvent (FP_HRR14 L989-L1072):
   - itemKey="delete" → deleteRoleInfo
   ↓
[事务1] doDeleteBosRole L703-L732:
   TX.required() 内 9 条 SQL → perm_roleperm/fieldperm/dataperm/userrole/role
   ↓ 成功
[事务外] removeRolePermCache(roleId)
   ↓
[事务2] doDeleteHrRole L617-L637:
   TX.required() 内 14 条 SQL → hrcs_userrole/rolerelat/rolebu/rolefield/role/roledimension/...
   ↓ 成功
[事务外] HRPermCacheMgr.clearAllCacheAsync() + ILogService.addBatchLog()
```

### 4.2 禁用角色 → 双表同步 + 防重复操作

```
[列表] 选中启用中的角色 → 点 bar_disable
   ↓
[前置闸] disableRoleInfo (FP_HRR14 · L1327):
   - 每行跑 canOperateRole (R6)
   - enable==false → 跳过（已禁用 · R7）
   - enable==true → 收集到 enableRoleList
   ↓
[执行] doDisableRole L1395-L1433:
   TX.required() 内:
   1. SaveServiceHelper.save → 改 perm_role.enable=0
   2. DB.update 裸 SQL → 同步 T_Perm_BizRoleComRole.FEnable=0
   ↓
[事务外] HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds)
         + PermLogRoleStatusServiceHelper.addPermLog(...)
```

### 4.3 复制角色 → 权限校验链

```
[列表] 选中角色 → 点 bar_copy
   ↓
[前置闸] copyRole (FP_HRR14 · L1048):
   - checkCanCopy 4 重: noCreateAdminGroup/masterGroup/topAdminGroupModify/checkHasModifyPerm (R9)
   - 禁用状态不可复制 (R10)
   - 单选限制 (R11)
   ↓
[跳转] 打开 hrcs_modifyrole 并传参数（复制模式）
```

### 4.4 F7 lookup 模式 · 回填到目标页面

```
[F7 弹窗] 其他页面打开角色 F7 选择器
   ↓
[准入] HRAdminStrictPlugin.preOpenForm (FP_HRA1 L33-L34):
   - isLookUp() == true → 跳过准入闸
   ↓
[列表加载] setFilter (FP_HRR7): viewableRoles 过滤 + 默认启用过滤
   ↓
[选择] 用户点击行 → listRowClick (FP_HRR19 L1689-L1710):
   - 提取选中行 pks
   - getRoleInfo(pks) 查询 perm_role id + name
   - 生成 ValueTextItem 列表
   - f7SelectedList.addItems(valueTextItems)
   ↓ 回填到目标页面的 F7 已选列表
```

### 4.5 跨场景联动总表

| 兄弟场景 | 关系 | 联动点 | 具体行为 |
|---|---|---|---|
| `hrcs_dynascheme` （动态授权方案） | 下游**引用** hrcs_rolelist.id | roleentry.role 外键 + getRefrencedRoles 反查（L1555-L1568） | 角色被方案引用 → 不允许删除（R2） |
| `hrcs_modifyrole` （角色编辑） | **跳转子页面** | billListHyperLinkClick (FP_HRR13 L959-L986) + bar_copy (FP_HRR14 L1048) | 角色编辑/复制通过此页面完成 · hrcs_rolelist 不直接写角色字段 |
| `hrcs_newrole` （新建角色） | **跳转子页面** | new opKey (FP_HRR14 L1045) | 创建新角色走独立页面 |
| `hrcs_rolegrp` （角色分组） | **树左侧上游** | addGroupNode/editAction/delAction (FP_HRR10 L434-L453) | 树工具栏 CRUD 操作 · 根节点不可改(R13) |
| `hrcs_perminitrecord` （权限初始化） | **跳转子页面** | bar_initrole / inituserperm | 角色权限导入/初始化 |
| `hrcs_exportperm` （导出权限） | **跳转子页面** + closedCallBack 下载 | bar_exportrole / exportroleperm + closedCallBack (FP_HRR17 L1115-L1150) | 导出完成后解析 TaskInfo.exportUrl → 触发下载 |
| `hrcs_userrolerelat` （用户-角色关联） | **级联删除** | doDeleteHrRole L617 (#4) | 角色删除 → 清理所有用户绑定 |
| `hrcs_dimension` （维度管理） | **间接引用** | hrcs_roledimension / EntityCtrlServiceHelper.getRoles(dimensionId) | 维度被角色引用 → 维度 entry.value 锁定 |
| `bos_dataperm` （平台数据权限） | **级联删除** | doDeleteBosRole L721-L724 (#6, #7) | 角色删除 → 清理所有数据权限配置 |

---

## 五、消息与事件机制

### 5.1 缓存通知（非 BEC）

hrcs_rolelist 不使用苍穹 BEC 事件系统。缓存失效通过专用 API：

| 方法 | 作用 | 异步 |
|---|---|---|
| `HRPermCacheMgr.clearAllCacheAsync()` | 清除全部 HR 权限缓存 | 异步 |
| `HRPermCacheMgr.clearAllCacheAndNotifyAsync(roleIds)` | 清除缓存 + 通知其他集群节点 | 异步 |
| `CacheMrg.clearCache(type, roleId)` | 清除 BOS 单角色权限缓存 | 同步 |
| `CacheMrg.clearCacheWithPrefix(type, roleId + "_")` | 清除 BOS 角色前缀缓存 | 同步 |

**ISV 自建 BEC**（CS-05）：如果要让其他模块订阅角色变更事件（如某角色被禁用后自动通知使用该角色的下游系统），ISV 需在自定义 OP 的 afterExecuteOperationTransaction 中发布 BEC 事件。

### 5.2 没有版本控制 / 时序模型

- hrcs_rolelist 的 perm_role / hrcs_role 都不是 HisModel 时序基础资料
- 没有版本字段 · 没有 confirmchange · 没有 iscurrentversion/boid（grep 实证 0 命中 · 05_data_flow.md 第九节）
- PR-008 / PR-009 不适用

---

## 六、同步 vs 异步总结

| 联动动作 | 同步/异步 | 延迟 | 失败影响 |
|---|---|---|---|
| preOpenForm · HR 管理员准入闸 | 同步 | 0ms | 非 HR 管理员被拒 · 整个页面不可用 |
| setFilter · viewableRoles 行级过滤 | 同步（列表加载时） | 0ms | 用户看不到本应在范围内的角色 |
| 树节点查询 · TreeNodeQueryListener | 同步（展开/点击时） | 0ms | 树展开失败 |
| 删除 BOS 事务 (9 表) | 同步（TX.required） | 正常 < 1s | 事务回滚 · 角色保留 · 有脏数据补偿日志 |
| 删除 HR 事务 (14 表) | 同步（TX.required） | 正常 < 1s | 事务回滚 · BOS 已删 → 数据不一致（标品已知设计风险） |
| 删除后 clearCacheAsync | 异步 | 数秒 | 缓存未清 → 旧数据可能仍被缓存命中 |
| 删除后 clearAllCacheAndNotifyAsync | 异步 | 数秒（跨节点更长） | 其他节点可能看到旧缓存 |
| 启用/禁用 doDisableRole → SaveServiceHelper.save | 同步 | 0ms | save 失败 → 事务回滚 |
| 启用/禁用 BizRoleComRole 同步 | 同步（同一 TX） | 0ms | 双表不一致 |
| 日志 addLogNoOpKey + addBatchLog | 同步（事务外） | 0ms | 日志丢失 · 不影响业务 |
| 权限初始化 · Modal 跳转 | 同步（用户操作触发） | 取决于用户 | 页面未打开 · 操作失败 |

---

## 七、ISV 开发注意事项

### 7.1 删除操作前必须扩展引用检查（CS-04）

标品 `checkCanDel` 只检查了 `hrcs_dynascheme` 引用（R2）。如果 ISV 新建了其他引用角色的表（如 ${ISV_FLAG}_custom_role_ref），必须在删除流程中追加自定义反查：

```java
// 建议在 ISV ListPlugin 的 beforeDoOperation 中追加:
// 反查 ISV 自有表 WHERE roleid IN (...)
// 有引用 → showTipNotification("删除失败: 角色被 X 引用") + setCancel(true)
```

标品的 `getRefrencedRoles` 只查 `hrcs_dynascheme` · ISV 自建表不在检查范围内。

### 7.2 不要继承 HRRoleListPlugin（PR-001 · 标品封闭类）

- **禁止** `extends HRRoleListPlugin` — 这是标品内部类（@SdkInternal）
- **正确方式**：并列挂新的 `HRStandardTreeList` 子类 + `HRDataBaseOp` 子类

### 7.3 启用/禁用的扩展点有限

hrcs_rolelist 的 enable/disable 走 FormPlugin 的 `beforeDoOperation` → `setCancel(true)` → 绕过苍穹 OP 链。因此：
- **OP 链的 afterExecuteOperationTransaction 不会触发**（因为 OP 根本没执行）
- ISV 要加 disable 后置逻辑 → 必须在 `afterDoOperation` 中检查 itemKey
- 但 itemKey 在 beforeDoOperation 阶段被 cancel 后 · afterDoOperation 能拿到（通过 pageCache）→ 但需要检查 itemKey=="bar_disable"/"bar_enable"

### 7.4 不要碰 BizRoleComRole 和 perm_role 裸 SQL

`doDisableRole` L1401 的裸 SQL 写 `T_Perm_BizRoleComRole` 是 BOS 标品行为 · ISV 不要仿写 · 不要扩影响面。

### 7.5 异步缓存的测试等待

删除/禁用后需要至少 **3 秒**等待才能进行自动化验证（05_data_flow.md 第十节）。`clearAllCacheAsync` 不保证方法返回时缓存已清。

### 7.6 树操作保护（根节点 / 含子分组 / 含角色）

FP_HRR10 (L434-L453) 中的三个保护规则：
- 不允许编辑/删除根节点（id==0）· R13
- 不允许在禁用刷新情况下编辑/删除 · R14
- 删除分组前检查 canDelGroup (L539-L555)：有子分组或有角色 → 拒绝删除 · R15

ISV 扩展树按钮时不要绕过这些保护。

### 7.7 F7 lookup 模式注意双闸豁免

HRAdminStrictPlugin (FP_HRA1) 对 F7 lookup 模式豁免准入校验。ISV 在自定义 ListPlugin 中做准入控制时，要注意 `isLookUp()==true` 的场景也应豁免（否则 F7 选择器不可用）。

### 7.8 viewableRoles 不能缩小

`setFilter` (FP_HRR7 L367-L372) 的行级隔离已经是标品最小可见集。ISV 追加 QFilter 时只能**更窄**（如过滤某类角色），不能**放宽**（不能绕过 viewableRoles 的限制）。

### 7.9 角色列表不是数据入口

hrcs_rolelist 是**视图 + 批操作**页 · 角色的"创建"走 hrcs_newrole · "编辑"走 hrcs_modifyrole。本表单**不直接写**角色属性字段（number / name / issyspreset 等）。ISV 在本表单扩展字段读写时 · 需要确认字段归属（是否属于 perm_role 主表）—— perm_role 是 BOS 元数据 · ISV 不能直接改它（CS-01 必须走 ISV 扩展元数据）。

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
