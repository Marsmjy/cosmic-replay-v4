# 数据流转 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译 `PermfilesListPlugin.java` + ServiceHelper 调用链 + opkey_registry
> **confidence**: verified
> **数据源**: CFR 反编译 · 2026-04-28

---

## ⭐ 一、本场景驱动的 4 张物理表

| 物理表 | 主从关系 | 写入入口 | 写入触发 |
|---|---|---|---|
| `hrcs_userpermfile` | 主表 | PermFilesSaveOp（标品 OP）+ hrcs_userpermfile 表单的 save | bar_new 弹表单 / save / enable / disable / delete |
| `hrcs_permfilegrp` | 分组主表 | hrcs_permfilegrp 表单（btnnew/btnedit 弹出）| treeToolbarClick → showForm |
| `hrcs_permfilegrpmember` | 分组成员中间表（多对多） | hrcs_permfilegrptree 表单 + RoleServiceHelper | batchgroup / 删档案级联 |
| `hrcs_userrolerelat` | 角色关联表 | hrcs_userassignrole / hrcs_copyperm + RoleServiceHelper.disablePermfile | assignrole / copyperm / disable 级联 |

---

## 二、数据写入链路图（5 主流程）

### 2.1 流程 A：新增档案（bar_new + save）

```
PermfilesListPlugin.beforeDoOperation                 ← 拦截 bar_new
        ↓ args.setCancel(true) · 阻断列表 default new
showForm("hrcs_userpermfile", customParam={groupId})  ← 弹模态表单
        ↓ 用户填写 user / org · 点保存
hrcs_userpermfile 表单（独立元数据 · 不在本场景管辖）
        ↓ save opKey
PermFilesSaveOp.beforeExecuteOperationTransaction
        ↓ 校验 user / org / permfileenable
        ↓ INSERT
        +─→ INSERT INTO t_hrcs_userpermfile (id, user, org, permfileenable=1, ...)
        ↓
PermFilesSaveOp.afterExecuteOperationTransaction        （事务已提交）
        ↓
模态关闭 returnData={"isNeedRefresh":"true"}
        ↓
PermfilesListPlugin.closedCallBack(NEW_PERMFILE)
        ↓ billList.refresh()
列表刷新 → setFilter 重新计算 → SELECT 拉新数据
```

⚠️ 该流程的事务边界仅限于 hrcs_userpermfile 表单内部（PermFilesSaveOp 一个 OP 链 · 一个 TX）· 列表刷新和审计日志（commonWriteLogBeforeDoOp）都在 TX 外。

### 2.2 流程 B：失效档案（disable · 级联）

```
PermfilesListPlugin.beforeItemClick("disable")
        ↓ ifNoOneSelected → 没选报错
PermfilesListPlugin.beforeDoOperation("disable")
        ↓ setVariableValue("tag_of_view", "true")
PermFilesSaveOp.beforeExecuteOperationTransaction       （TX 开始）
        ↓ 收集 idList（被选中行）
PermFilesSaveOp.endOperationTransaction
        ↓ setEnable("0", idList)         ← 反编译 L767-L781
        ├─→ HRBaseServiceHelper.update
        │   UPDATE t_hrcs_userpermfile SET fpermfileenable='0' WHERE fid in (?)
        ├─→ RoleServiceHelper.disablePermfile(idList)
        │   ├─→ UPDATE t_hrcs_userrolerelat SET fcustomenable='0' WHERE fpermfileid in (?)
        │   └─→ （或类似 disable 标记 · 视实际实现）
        └─→ HRPermCacheMgr.clearAllCache()              ⚠️ 缓存清理 · 不在 TX
                                                         + clearAllManageCache()
PermFilesSaveOp 提交 TX
        ↓
PermfilesListPlugin.afterDoOperation
        ↓ 读 customDataMap.dealResult_*
        ↓ showChangeStatusMessage(N, success, "0", detailMsg)   ← UI 提示
        ↓ billList.refresh() + clearSelection()
```

⚠️ **3 个写入操作** · 全在事务内（除缓存）：
1. UPDATE `t_hrcs_userpermfile` · 主表 N 行
2. 级联 UPDATE `t_hrcs_userrolerelat` · 关联角色批量失效（M 行 · M = sum(每档案的角色数)）
3. clearAllCache · 不在 TX · 立即生效（**这是个不一致窗口** · 标品接受）

---

### 2.3 流程 C：复制权限（copyperm · 跨档案的关系复制）

```
PermfilesListPlugin.beforeItemClick("btn_copyperm")
        ↓ 选中数 ≠ 1 → 报"请选择一个用户"
PermfilesListPlugin.beforeDoOperation("btn_copyperm")
        ↓ copyPerm(rows) L1140-L1163
        ↓ checkCanCopy L1166-L1188
        │   ├── checkRoleForBidden          ← user.isforbidden=true 或 permfileenable=0 拒
        │   ├── getUserRelatesByPermFields  ← 当前档案有可复制的角色
        │   └── HRRolePermHelper.queryViewableRoles ← 当前操作员能看到这些角色
        ↓ showCopyPermForm L1150-L1163
showForm("hrcs_copyperm", customParam={permfileId})       ← 弹复制目标选择
        ↓ 用户在弹框选目标用户 · 选要复制的角色子集
        ↓
hrcs_copyperm 表单内部（不在本场景管辖）
        ├─→ INSERT INTO t_hrcs_userrolerelat (id, user=新用户, role=源角色, permfile=新档案, ...)
        └─→ HRPermCacheMgr.clearAllCache()
        ↓ 表单关闭 · 不需要回调（PermfilesListPlugin 没注册 copyperm 的 closedCallBack）
列表自动看到新数据（下次手动 refresh）
```

⚠️ **复制不是数据库 INSERT 一行 · 而是 INSERT N 行**（一个档案绑了 N 个角色 · 复制也产生 N 行新关联）。每行新关联用 `kd.bos.id.ID.genLongId()` 分配新 ID（PR-005）· **复制后的角色关联 · `creator` 字段填当前操作员 · 不是源档案的 creator**。

### 2.4 流程 D：syncperm（按人事档案生成 · 异步）

```
PermfilesListPlugin.beforeDoOperation("btn_initdata")
        ↓ genPermFiles → showSyncPermForm
showForm("hrcs_syncpermfile")    ← 弹 laborrelType 选择
        ↓ 用户选员工类型集合
        ↓ 表单关闭返回 MulBasedataDynamicObjectCollection
PermfilesListPlugin.closedCallBack(CONFIRMCALLBACK_SAVE)
        ↓ launchSyncPermfileJob(laborrelTypeIds) L1071-L1102
JobInfo.setTaskClassname("kd.hr.hrcs.bussiness.task.SyncPermFilesTask")
JobInfo.setParams({laborrelTypeIds})
        ↓ dispatch(jobFormInfo)
showForm("hrcs_syncprocess")     ← 弹进度条 · setShowClose(false)
        ↓ 调度框架（kd.bos.schedule）异步起 SyncPermFilesTask
SyncPermFilesTask.execute（在后台 worker 线程 · 单独事务）
        ├── 查 hrpi_pernontsprop / hrpi_empjobrel  · 按 laborrelTypeIds 筛人
        ├── 逐人 INSERT INTO t_hrcs_userpermfile (id, user, org, permfileenable=1, ...)
        ├── 已存在的 user+org → UPDATE
        ├── 累计 newCount + updCount
        └── HRAppCache.put("syncPermFilesTask{userId}", {newCount, updCount, success, errorInfo})
        ↓
hrcs_syncprocess 监听任务结束 · 关闭模态
        ↓
PermfilesListPlugin.closedCallBack(syncPermFilesTask)
        ↓ 读 HRAppCache · 清 syncPermFilesTaskId
        ↓ showConfirm("按组织分配生成全部成功，本次共创建 X 条，生效 Y 条")
```

⚠️ **数据写入完全在后台任务事务内** · 列表 OP 链不直接写。这是平台调度任务（schedule）的标准模式 · **ISV 自定义代码不要尝试同步等待任务结果** · 走 closedCallBack 异步等。

### 2.5 流程 E：批量分组（batchgroup）

```
PermfilesListPlugin.beforeDoOperation("btn_batchgroup")
        ↓ handleBatchGroup(rows)
        ↓ params.permfiles = "id1,id2,id3"
RoleServiceHelper.showForm("hrcs_permfilegrptree", params, Modal)
        ↓ 用户在分组树上选目标分组（一或多）
        ↓ 表单内部
hrcs_permfilegrptree（独立表单 · 不在本场景管辖）
        └─→ INSERT INTO t_hrcs_permfilegrpmember (id, permfilegrp, permfile)
            笛卡尔积：M 个分组 × N 个 permfile = M*N 行
        ↓ 表单关闭 · 列表 refresh 时新数据生效
```

---

## 三、SELECT 数据读取链路（核心查询）

### 3.1 列表主查询（setFilter + nodeClickFilter 双层过滤）

```
列表初始化 · setFilter 触发
        ↓
PermfilesListPlugin.setFilter L1030-L1037
        ↓ super.setFilter
        ↓ 查当前用户 admingroup level
        │   long level = HRPermServiceHelper.getUserGroupMinLevel();
        ├── if level == -1 || level > 2 (非顶级管理员)
        │   ↓ List<Long> userPermFileIds = HRPermServiceHelper.getUserPermFile()
        │   ↓ qfilters.add(new QFilter("org.id", "in", userPermFileIds))   ← 限定 org 集合
        └── else 顶级管理员 · 不加限制
        ↓
（如果用户点了树节点）
PermfilesListPlugin.nodeClickFilter L996-L1005
        ↓ focusNodeId = 当前选中树节点
        ↓ if focusNodeId != ID_ROOTNODE
            ↓ groupFilter = new QFilter("hrcs_permfilegrpmember.permfilegrp.id", "=", focusNodeId)
            ↓ resultFilter = resultFilter.and(groupFilter)
最终 SQL（推断 · 平台 ORM 翻译）：
        SELECT * FROM t_hrcs_userpermfile p
        WHERE p.permfileenable = '1'                                   ← filterContainerInit 默认值
          AND p.org IN (?)                                             ← setFilter 加的
          AND EXISTS (
            SELECT 1 FROM t_hrcs_permfilegrpmember m
            WHERE m.fpermfileid = p.fid AND m.fpermfilegrpid = ?       ← nodeClickFilter 加的
          )
        ORDER BY ...
```

### 3.2 复制权限校验查询（getUserRelatesByPermFields）

```java
// 反编译 L1190-L1194
HRBaseServiceHelper helper = new HRBaseServiceHelper("hrcs_userrolerelat");
return helper.query(
    "id,user.id,user.name,user.number,permfile.org.id,permfile.org.name,permfile.org.number," +
    "role.id,role.number,role.name,role.enable,customenable,validstart,validend,creator",
    new QFilter[]{
        new QFilter("permfile.id", "in", permFileIds),
        new QFilter("role.enable", "=", "1")        ← 仅在用角色（启用）
    },
    "role.number,user.name"  // ORDER BY
);
```

⚠️ **15 字段一次拉取**·`permfile.org.*` 是关联导航。**HRBaseServiceHelper.query 自动按 ORM 翻译为 LEFT JOIN**·查询时 `role.enable='1'` 过滤掉了已停用角色。

### 3.3 树节点查询（getTreeViewCollection · 递归子节点）

```java
// 反编译 L279-L308
String selectFields = "id, name, parent, longnumber, isleaf";
// 根节点点击 · 不加 parent 过滤 · 加 enable=1
// 子节点点击 · 按 parent = currentNodeId 过滤
QFilter filter = isRoot
    ? new QFilter("enable", "=", "1")
    : new QFilter("parent", "=", Long.valueOf(parentId));
DynamicObject[] objects = helper.query(selectFields, filters, "longnumber");

// 转 dyColl + 处理 parent 字段
for (DynamicObject d : objects) {
    if (d.get("parent") == null) d.set("parent", 0);
    else d.set("parent", d.get("parent.id"));
    dyColl.add(d);
}
```

⚠️ **树展开是按需懒加载** · 不是一次拉全树。每点开一个节点 · 才查它的子节点（`parent=parentId`）。这是 TreeView 的标准模式（PR-002 RowKey 执行链以下）。

### 3.4 子树命中查询（buildNodeClickFilter · longnumber 加深递归）

```java
// 反编译 L310-L341
DynamicObject selectNodeDy = HRBaseDaoFactory.getInstance(entityName).queryOne("longnumber", focusNodeFilter);
// 子树查询：按 longnumber.like '<focus longnumber>.%'
QFilter[] filters = new QFilter[]{
    new QFilter("longnumber", "like", focusNodeDy.getString("longnumber") + "." + "%")
};
DynamicObjectCollection coll = helper.queryOriginalCollection("id", filters);
// list = 子节点 ID 数组 + 当前节点 ID
List<Long> list = ...;
list.add(focusNodeDy.getLong("id"));
nodeClickFilter = new QFilter("hrcs_permfilegrpmember.permfilegrp.id", "in", list);
```

⚠️ **关键算法**：
- `longnumber` 字段形如 `1.2.3.4` · 树形编码
- 查"当前节点及所有子孙节点" · 用 `longnumber like '<focus>.%'` + 当前节点本身
- 这避开了递归查询的 N+1 性能问题
- 查到 grpId 集合后 · 中间表 `permfilegrpmember.permfilegrp.id in (?)` 拉数据

---

## 四、缓存写入链路（HRPermCacheMgr）

苍穹 HR 权限缓存有 2 个 cache 区：

| Cache 区 | 关键 API | 触发清空时机 |
|---|---|---|
| 全局权限缓存 | `HRPermCacheMgr.clearAllCache()` | enable / disable / save / delete · 任何 permfileenable 变更 · btn_clearcache 按钮 |
| 管理员组缓存 | `HRPermCacheMgr.clearAllManageCache()` | btn_clearcache 按钮（手动） |
| App 缓存 | `HRAppCache.get("hrcs")` · `IHRAppCache` | syncperm 任务进度（taskId / result） |

⚠️ **缓存写入不在 OP 事务内** · 是直接 RPC 到 Redis · 失败不回滚。这是平台已知行为。

---

## 五、外部系统数据通道

### 5.1 调度任务（kd.bos.schedule · 异步写）

| Task 类 | 触发 opKey | 数据流向 |
|---|---|---|
| `kd.hr.hrcs.bussiness.task.SyncPermFilesTask` | syncperm | hrpi_* （读员工表） → hrcs_userpermfile（写） |
| `kd.hr.hrcs.formplugin.web.perm.init.task.PermFilesExportTask` | exportuserperm | hrcs_userpermfile / hrcs_userrolerelat（读） → 文件下载 URL |

### 5.2 BEC 业务事件（PR-011）

⚠️ **本场景标品 0 处发 BEC**（grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录全无命中）。

如要把 enable / disable / delete 同步给下游 · 走 ISV 自建 BEC 发布（参考 CS-05）：
- 发布方挂 enable/disable/delete 的 `afterExecuteOperationTransaction`
- 调 `IEventService.triggerEventSubscribeJobs(appId, eventNumber, message, variables)`

### 5.3 OpLog（审计日志）· 反编译广泛使用

`PermfilesListPlugin` 多处调用：
- `RoleService.commonWriteLogBeforeDoOp(args)` —— bar_new 触发（L563）
- `RoleService.commonWriteLogNoOpKey(opKey, bridge, success, formId, appId)` —— 6+ 处（btn_clearcache, batchgroup, ...）
- `RoleService.addLogWithOpKey(opKey, opName, success, formId, appId, list, msg)` —— enable/disable 错误时（L680）

这些写入 `t_oplog_*` 系统审计表（不在本场景查询范围）· **不要 ISV 重复写日志**。

---

## 六、数据一致性建议（导向 06 CS）

| 一致性诉求 | 推荐做法 | 风险 |
|---|---|---|
| 失效档案前提示用户"将级联 N 个角色" | ISV ListPlugin 拦截 disable opKey · 在 beforeDoOperation 弹 confirmcallback | 低（CS-03） |
| 失效档案后通知下游 BPM | ISV OP 挂 disable.afterExecuteOperationTransaction · 发 BEC | 中（CS-05） |
| 删除档案前查 sourcetype=4（动态方案分配出来的）保护 | 查 hrcs_userrolerelat where permfile=? + sourcetype="4" · 如有 → 抛异常 | 低（CS-04） |
| 复制权限时记审计明细（从 A → B 复制了哪些角色） | 自建 OP 挂 copyperm.afterExecute · 写自定义日志表 | 中 |

详见 `06_customization_solutions.md`。
