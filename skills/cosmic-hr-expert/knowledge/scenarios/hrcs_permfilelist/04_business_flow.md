# 业务流转 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译 `PermfilesListPlugin.java` + `_auto_operations.md`（23 opKey 实抓）+ ServiceHelper 调用链
> **confidence**: verified
> **数据源**: CFR 反编译 · OpenAPI listOperations · 2026-04-28

---

## ⭐ 一、状态机 · `permfileenable` 软启停（不是工作流）

`hrcs_userpermfile`（用户授权档案）**不是单据形态** · 没有 `audit/unaudit/submit/unsubmit` 工作流（虽然 opKey 列表里登记了 `audit` 类操作但本场景未启用）。它是**基础资料形态** · 只有"生效 / 失效"二态：

```
新增（bar_new → showForm hrcs_userpermfile · save）
        ↓
permfileenable="1" 生效（默认）
        ↓ disable opKey   (PermFilesSaveOp.beforeExecuteOperationTransaction)
permfileenable="0" 失效   (同时 RoleServiceHelper.disablePermfile 级联清空 hrcs_userrolerelat)
        ↓ enable opKey
permfileenable="1" 重新生效（hrcs_userrolerelat 不会自动恢复 · 需重新分配角色）
        ↓ delete opKey
档案物理删除（同步级联清 hrcs_permfilegrpmember + hrcs_userrolerelat）
```

⚠️ **状态字段是 `permfileenable`** · **不是 `enable`** · **不是 `status`**。
- `permfileenable`：业务自定义状态字段（"0" / "1"）· hrcs_userpermfile 主数据特有
- `enable`：标品启停字段（很多基础资料用 · 这里不用）
- `status`：单据状态（A/B/C 暂存/提交/审核 · 单据形态用 · 这里不用）

反编译 `setEnable` L767-L781 直接 `update permfileenable` · 与 `enable / status` 字段无关。

---

## 二、23 opKey 全景图（按业务功能分组）

`_auto_operations.md` 列了 23 个 opKey · 按业务能动性分 4 组：

### 2.1 真实业务 opKey（用户主动触发 · 12 个）

| opKey | 名称 | opType | 背后调用 | 反编译来源 |
|---|---|---|---|---|
| `save` | 保存 | save | PermFilesSaveOp（jar 缺）→ 落 hrcs_userpermfile | _auto_operations.md L57-L68 |
| `enable` | 生效 | donothing | PermFilesSaveOp · 改 permfileenable=1 + showChangeStatusMessage | L70-L86 + L767-L781 |
| `disable` | 失效 | donothing | PermFilesSaveOp · 改 permfileenable=0 + RoleServiceHelper.disablePermfile + clearAllCache | L88-L92 + L775-L778 |
| `delete` | 删除 | delete | PermFilesSaveOp · 物理删 + 级联 | L94-L111 |
| `disable_new` | 禁用 | disable | PermFilesSaveOp（StatusFieldId=NmBey3RvMr · 不是 permfileenable） | L118-L134 |
| `importdata` | 引入数据 | importdata | 标品 + writeLog "importdata" | _auto_operations.md L161-L165 |
| `assignrole` | 分配角色 | donothing | userAssignRole 弹 hrcs_userassignrole | L567-L586 |
| `exportlist` | 引出数据（按引入模板） | exportlist | 标品 | _auto_operations.md L173-L177 |
| `syncperm` | 按人事档案生成 | donothing | genPermFiles → SyncPermFilesTask（异步任务） | L1040-L1102 |
| `inituserperm` | 初始化用户权限 | donothing | userPermInit · 跳 bos_list 显示 hrcs_perminitrecord | L588-L600 |
| `exportuserperm` | 引出用户权限 | donothing | exportUserPerm → PermFilesExportTask（异步任务） | L602-L632 |
| `copyuserperm` | 复制权限 | donothing | copyPerm · 双重权限校验后弹 hrcs_copyperm | L1140-L1188 |
| `batchgroup` | 批量分组 | donothing | handleBatchGroup · 弹 hrcs_permfilegrptree | L531-L543 |

### 2.2 平台默认 opKey（鲜少触发 · 7 个）

| opKey | 名称 | opType | 用途 |
|---|---|---|---|
| `first` | 第一 | first | 列表导航 · 平台默认 |
| `previous` | 前一 | previous | 列表导航 |
| `next` | 后一 | next | 列表导航 |
| `last` | 最后 | last | 列表导航 |
| `new` | 新增 | new | ⚠️ 注意：本场景的"新建"实际走 `bar_new`（itemKey）· 不走标品 `new` opKey · 反编译 L555-L565 args.setCancel(true) 拦截 |
| `refresh` | 刷新 | refresh | 列表刷新 |
| `close` | 关闭 | close | 关闭页签 |
| `modify` | 修改 | modify | 平台默认（通过 hyperLink 进入编辑） |
| `view` | 查看 | view | 平台默认 |
| `donothing` | 空操作 | donothing | 占位 · 不实际执行 |

---

## 三、5 大核心业务流程（端到端 · 反编译实证）

### 3.1 流程 1：新增用户授权档案

```
用户点【新增】(bar_new)
   ↓
PermfilesListPlugin.beforeDoOperation L416-L466
   case "bar_new":
     showNewForm(args) L555-L565
     ├── new BillShowParameter
     ├── setCustomParam("groupId", currentTreeNodeId)  ← 携带当前选中分组
     ├── setFormId("hrcs_userpermfile")
     ├── setShowType(Modal)
     ├── setCloseCallBack(NEW_PERMFILE)
     ├── view.showForm(bsp)
     ├── RoleService.commonWriteLogBeforeDoOp(args)    ← 审计日志
     └── args.setCancel(true)                          ← 阻断标品 new
   ↓
hrcs_userpermfile 模态表单弹出（独立编辑表单 · 其插件链不在本场景内）
用户填表 · 点保存 → 走 hrcs_userpermfile 上的 save opKey + PermFilesSaveOp
   ↓
模态关闭返回 (returnData={"isNeedRefresh":"true"})
   ↓
PermfilesListPlugin.closedCallBack L784-L797
   case NEW_PERMFILE:
     if isNeedRefresh = "true": billList.refresh()
```

⚠️ **关键事实**：标品 `new` opKey 不会触发 · 本场景的"新建"是 itemKey=`bar_new`（工具栏按钮 key）· 由 ListPlugin 自己控制 showForm。`new` opKey 在 `_auto_operations.md` 里登记是占位 · `plugins=0 / validations=0`。

### 3.2 流程 2：生效（enable）

```
用户选中 N 行 · 点【生效】
   ↓
PermfilesListPlugin.beforeItemClick L344-L386
   case "enable":
     ifNoOneSelected(rows) → 没选中报"请选择要执行的数据"
   ↓
PermfilesListPlugin.beforeDoOperation L416-L466
   case "enable":
     setVariableValue("tag_of_view", "true")           ← 给后续 OP 传上下文
   ↓
PermFilesSaveOp.beforeExecuteOperationTransaction（标品 jar 缺 · 但根据 enable opKey 行为推断）
   - 校验 user.isforbidden = false
   - 校验 permfileenable 已经是 0（不是已经生效）
   ↓
PermFilesSaveOp.endOperationTransaction
   setEnable("1", idList) L767-L781
     ├── helper.update(permfile.permfileenable="1")   ← 改 hrcs_userpermfile
     └── HRPermCacheMgr.clearAllCache()                ← 清缓存
   ↓
PermfilesListPlugin.afterDoOperation L468-L496
   case "enable":
     read OperationResult.customDataMap.dealResult_*    ← 标品 OP 写出的统计
     showChangeStatusMessage(selectSize, success, "1", detailMsg) L684-L701
     billList.refresh() + clearSelection()
```

### 3.3 流程 3：失效（disable · 含级联）⭐ 关键

```
用户选中 N 行 · 点【失效】
   ↓
PermfilesListPlugin.beforeItemClick → ifNoOneSelected
   ↓
PermfilesListPlugin.beforeDoOperation
   case "disable":
     setVariableValue("tag_of_view", "true")
   ↓
PermFilesSaveOp.endOperationTransaction
   setEnable("0", idList) L767-L781
     ├── helper.update(permfile.permfileenable="0")
     ├── RoleServiceHelper.disablePermfile(idList)     ← ⚠️ 级联失效 hrcs_userrolerelat
     │   └── 把 idList 对应的所有角色关联记录 disable
     └── HRPermCacheMgr.clearAllCache()
   ↓
PermfilesListPlugin.afterDoOperation
   showChangeStatusMessage 提示"X 条单据，成功 N 条，失败 M 条"
```

⚠️ **disable 比 enable 副作用大很多**：
1. 改 `permfileenable=0`（主表 1 行）
2. 级联清空所有 `hrcs_userrolerelat.role` 关联（多行 · 业务上"失效档案 = 失效所有角色绑定"）
3. 清全局权限缓存（影响全系统的鉴权命中）

ISV 的 disable 前置校验（CS-03）必须在 `beforeExecuteOperationTransaction` 阶段做 · 进入 endOperationTransaction 已不可逆。

### 3.4 流程 4：批量生成（syncperm · 异步任务）

```
用户点【按人事档案生成】(btn_initdata · opKey=syncperm)
   ↓
PermfilesListPlugin.beforeDoOperation L450-L455
   case "btn_initdata":
     genPermFiles() L1040-L1056
       ├── if !RoleService.isAdmin() → 报错（双闸：HRAdminStrictPlugin + 此处）
       ├── 检查 syncPermFilesTaskId 缓存 · 防重复
       └── showSyncPermForm() L1058-L1068
           └── view.showForm(hrcs_syncpermfile · Modal · setCloseCallBack(CONFIRMCALLBACK_SAVE))
   ↓
hrcs_syncpermfile 表单收集 laborrelTypeIds (员工用工类型)
   ↓
模态关闭返回 (MulBasedataDynamicObjectCollection)
   ↓
PermfilesListPlugin.closedCallBack L835-L838
   case CONFIRMCALLBACK_SAVE:
     launchSyncPermfileJob(laborrelTypeIds) L1071-L1102
       ├── 构造 JobInfo
       │     setJobType(JobType.REALTIME)
       │     setTaskClassname("kd.hr.hrcs.bussiness.task.SyncPermFilesTask")
       │     setClickClassName("kd.hr.hrcs.bussiness.task.SyncPermFilesTaskClick")
       │     setParams({laborrelTypeIds: [...]})
       └── dispatch(jobFormInfo) L1105-L1122
             └── view.showForm(hrcs_syncprocess · Modal · setShowClose(false))
                 进度条页面 · 监听任务结束 · setCloseCallBack(syncPermFilesTask)
   ↓
SyncPermFilesTask 后台执行（在 kd.bos.schedule 调度框架）
   完成 · 把 result(newCount/updCount/success/errorInfo) 存到 HRAppCache("hrcs")
   ↓
hrcs_syncprocess 关闭 · 触发 syncPermFilesTask closedCallBack
   ↓
PermfilesListPlugin.closedCallBack L798-L827
   case "syncPermFilesTask":
     从 HRAppCache 读 newCount/updCount/success/errorInfo
     if success: showConfirm("按组织分配生成全部成功，本次共创建 X 条，生效 Y 条")
     else: showMessage("按组织分配生成部分失败...")
```

⚠️ **本场景唯一异步操作**。任务类是 `kd.hr.hrcs.bussiness.task.SyncPermFilesTask`（jar 在 hrmp-hrcs-bussiness · 不是 formplugin）· **ISV 不要在 SyncPermFilesTask 同事务里调下游写**。如要在生成完后做下游同步 · 推荐方案：
- 自建 OP 挂 `syncperm` opKey 的 afterExecuteOperationTransaction
- 或订阅 BEC（PR-011）· 标品本身没发 BEC · ISV 自建发布方

### 3.5 流程 5：批量分组（batchgroup）

```
用户选中 N 行 · 点【批量分组】(btn_batchgroup)
   ↓
PermfilesListPlugin.beforeItemClick → ifNoOneSelected
   ↓
PermfilesListPlugin.beforeDoOperation L460-L463
   case "btn_batchgroup":
     handleBatchGroup(rows) L531-L543
       ├── 拼 permfileIds = "id1,id2,id3"
       ├── RoleService.commonWriteLogNoOpKey("batch grouping", ...)
       └── RoleServiceHelper.showForm("hrcs_permfilegrptree", params, ShowType.Modal)
   ↓
hrcs_permfilegrptree 表单弹出（独立分组选择树 · 用户选目标分组）
   ↓
该表单内部 INSERT hrcs_permfilegrpmember 关联表（permfilegrp + permfile 笛卡尔积）
   ↓
关闭返回 · billList 自然刷新（分组 -1- 关联到档案）
```

---

## 四、`closedCallBack` 4 大动作分支汇总（反编译 L784-L841）

`closedCallBack` 接收 4 类 actionId · 是流程的"关键中转点"：

| actionId | 触发场景 | 处理 |
|---|---|---|
| `NEW_PERMFILE` | bar_new 弹 hrcs_userpermfile 关闭后 | returnData.isNeedRefresh="true" → billList.refresh() |
| `syncPermFilesTask` | syncperm 异步任务结束 | 读 HRAppCache 显示 newCount/updCount + clear taskId |
| `exportUrl` | exportuserperm 任务结束 · 拿到下载 URL | 调用 IClientViewProxy.addAction("download", url) |
| `confirmcallback_save` | hrcs_syncpermfile 确认 · 收到 laborrelTypeIds | launchSyncPermfileJob(laborrelTypeIds) 起任务 |

---

## 五、与外部系统的集成点

### 5.1 上游（被谁触发）

| 上游 | 关系 | 触发方式 |
|---|---|---|
| HR 域管理员体系 | 准入闸 | HRAdminStrictPlugin.preOpenForm L29-L37 · 非管理员直接拒绝打开列表 |
| 人事档案体系（hrpi_*） | 数据来源 | syncperm opKey · 按人事档案生成权限档案（按 laborrelType 筛选员工 · 自动生成对应授权档案） |
| 角色体系（perm_role） | 关联数据 | assignrole / copyperm 都涉及向 hrcs_userrolerelat 写入角色关联 |

### 5.2 下游（谁被影响）

| 下游 | 关系 | 影响时机 |
|---|---|---|
| `hrcs_userrolerelat` | 强耦合 | save / disable / delete 都直接影响 |
| `hrcs_permfilegrpmember` | 强耦合 | batchgroup / 删档案时级联清 |
| 全系统鉴权缓存（HRPermCacheMgr） | 强耦合 | enable / disable / 任何 perm 改动都触发 clearAllCache · 影响全系统鉴权命中率（瞬时空缓存重建） |
| HR 调度任务（kd.bos.schedule） | 异步 | syncperm / exportuserperm 走 JobInfo 派 SyncPermFilesTask / PermFilesExportTask |
| 业务事件中心 BEC（PR-011） | ❌ **标品没发** | grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录全无命中 · ISV 自建（CS-05）发布方 |

---

## 六、事务边界 · 哪些操作在同 TX 内

反编译 `setEnable` L767-L781 揭示了 disable 的事务边界：

```java
TX 开始（PermFilesSaveOp.beginOperationTransaction）
  1. helper.update(permfile.permfileenable="0")          ← 主表 update
  2. RoleServiceHelper.disablePermfile(idList)            ← 级联 hrcs_userrolerelat
  3. HRPermCacheMgr.clearAllCache()                       ← ⚠️ 缓存清理（不在事务内 · 立即生效）
TX 提交（PermFilesSaveOp.endOperationTransaction）
TX 外
  4. afterDoOperation showChangeStatusMessage             ← 用户提示
  5. billList.refresh()                                    ← UI 层刷新
```

⚠️ **缓存清理（第 3 步）不参与事务**。如果 TX 第 1/2 步失败回滚 · 缓存已经清了 · 短暂时间内会出现"DB 仍是旧值 · 缓存被清"的不一致。这是标品已知行为 · ISV 不要在自定义 OP 里再加一次 clearCache（重复）· 如果加 · 也要放在 `afterExecuteOperationTransaction`（事务确认提交后）。

---

## 七、跨场景流程协同点

| 协同方 | 场景 | 协同点 |
|---|---|---|
| `hrcs_dynascheme` | 动态授权方案 | dynascheme.save 时反查表（search_*）会用到 user/org · 与 permfile 的 user/org 是同一份数据 · 但角色绑定路径不同（dynascheme 走 hrcs_userrolerelat.sourcetype="4" · permfile 走 sourcetype 默认） |
| `hrcs_admingroup` | HR 管理员组 | 列表 setFilter 用 admingroup 等级控制可见 org 范围 · 间接限定档案可见 |
| `hrcs_role` | 中台角色 | 复制权限（copyperm）时校验 viewableRoles · permfile 失效时级联失效角色绑定 |
| `bos_user` | 用户基础资料 | user.isforbidden=true 时 · permfile 不能复制 / 不能授权 |

---

## 八、流程定制扩展点（导向 06 CS）

| 流程节点 | 定制需求示例 | 推荐 CS |
|---|---|---|
| save 前 | 同 user+org 不允许重复建档案 | CS-03 操作前置校验 |
| save 前 | user 必须是在职员工（not isforbidden） | CS-03 |
| disable 前 | 询问"是否确认级联失效 N 条角色" | CS-03 + ISV ListPlugin 改 confirmcallback |
| disable 后 | 通知下游 BPM/工作流系统 | CS-05 BEC 自建发布 |
| delete 前 | 查 hrcs_userrolerelat 反向引用 | CS-04 删除/禁用前查下游引用 |
| 列表 setFilter | ISV 按 customenable / 项目分组限定 | CS-06 列表过滤定制 |
| batchgroup | 批量分组时 · 自动写 ISV 扩展字段 | CS-07 批量授权扩展 |

详见 `06_customization_solutions.md`。
