# 业务流转 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 14 反编译类调用链 + opKey 链路实证
> **confidence**：verified

## 1. 状态机总览

```
                          ┌─ status ─────────────────────────────────────┐
                          │  A=草稿/暂存                                  │
                          │  B=已提交                                     │
                          │  C=已审核                                     │
                          └───────────────────────────────────────────────┘
                          ┌─ enable ─────────────────────────────────────┐
                          │  0=已禁用                                    │
                          │  10=启用中（中间态 · BdVersion 切换过程）   │
                          │  1=已启用                                    │
                          └───────────────────────────────────────────────┘

      [新建]                                            HRBaseDataLogOp
        │       save                  submit                  audit
        ▼  ─────────►   ▼  ─────────►   ▼  ──────────►   ▼
   status=A       status=A→A      status=A→B      status=B→C
   enable=0       enable=0        enable=0         enable=0
   （草稿）       （已保存草稿）  （已提交）       （已审核）

                                                  unaudit / unsubmit
                                                       ▼
                                                  status=C→A
                                                  enable=0

      [已审核 status=C]
        │       enable
        ▼  ─────────►
   enable: 0 → 10 → 1（BdVersion 启用过程）
   StructProjectEnableOp.beginOperationTransaction 是空方法 · 状态切换由 HRBaseDataEnableOp 控制

      [已启用 enable=1]
        │       disable
        ▼  ─────────►
   enable: 1 → 0
   StructProjectDisableOp.beginOperationTransaction 是空方法

      [enable=10 启用中]
        │       delete
        ▼  ─────────►
   StructProjectDeleteOP.beginOperationTransaction L46-55
   仅 enable=10 行参与 doWithDelete · 其它跳过
```

## 2. 完整生命周期：方案设计 → 派生 → 维护 → 退役

### 阶段 1 · 方案新建（设计阶段）

**触发**：用户在列表点【新增】（opKey=`new` 或 `addnew_org`）

**调用链**：
1. `StructProjectListPlugin.openOperationPage` L120-122 (addnew_org) 调 `showStructProjectForm()`
2. `showStructProjectForm` L134-149 构造 `BaseShowParameter` · 设 `formId=haos_structproject` + `customParam("orgId", 当前 BU id)`
3. 跳到表单 `haos_structproject` · `StructProjectEditPlugin.preOpenForm` L201-206 设 `OperationStatus.ADDNEW`（除非用户指定 `opentype=1`）
4. `StructProjectEditPlugin.afterCreateNewData` L119-136：
   - 拿 customParam orgId（如有）→ `setValue(ORGFIELD, orgId)`
   - 否则调 `OrgPermHelper.getHRPermOrg(true)` 拿权限组织集合 · 取首位（优先 `RequestContext.getOrgId()`）
5. `afterBindData` L138-161 · `OperationStatus.ADDNEW` 分支 L153-156：
   - 隐藏 rootnumber/rootname/rooteffdt/rootdescription/orgorg
   - setMustInput(true, ROOTORG) · setMustInput(false, "rootnumber"...)
6. 用户填写表单 · 触发 `propertyChanged`（roottype / isincludevirtualorg）联动显隐字段

**用户操作**：
- 选 `org`（创建组织 BU · 已默认）
- 填 `name` / `number`（编码可走 CodeRule 自动生成）
- 选 `roottype`：
  - 选 `1`（实组织）：必填 rootorg
  - 选 `2`（虚拟根）：必填 rootnumber/rootname/rooteffdt + 调 `AdminOrgCodeRuleServiceHelper.setOrgNumber` 自动赋编码
- 填 `effdt` 生效日期（必填）
- 选 `otclassify`（可选 · 影响该方案下虚拟组织的分类）
- 填其它业务标记：`isprimary` / `istoallareas` / `issyncorg`

### 阶段 2 · 方案保存（save opKey · 7 OP 链）

**触发**：用户点【保存】（opKey=`save`）

**调用链**（`opkeys/save.json`）：
1. `CodeRuleOp` · 编码规则（如启用了）· onAddValidators 注册 numberValidator
2. `BdVersionSaveServicePlugin` · 基础资料版本管理 · 主表 name + 版本子表 name 写历史
3. `HRBaseDataStatusOp` · HR 基础资料单据状态（draft/audit/disable）
4. `HRBaseDataLogOp` · HR 基础资料操作日志
5. `HRBaseDataEnableOp` · HR 基础资料启用态管理 · 控制 enable 字段
6. `HRBaseOriginalOp` · HR 基础资料原始值记录（对比变更前后）
7. `StructProjectSaveOp` · 业务核心保存逻辑（**183 行 · 真业务**）

**StructProjectSaveOp 调用链**（`StructProjectSaveOp.java` 反编译）：
- `onAddValidators` L72-75：注册 `StructProjectValidator`
- `beginOperationTransaction` L77-82：拿到第 0 个 dataEntity · 调 `saveStructProjectAndRootOrg`
- `saveStructProjectAndRootOrg` L84-181：根据 roottype 分支处理
  - **roottype=2 + 新建**（rootOrgId=0）：生成虚拟组织 ID + 设 `otclassify=1010L` + 一并保存
  - **roottype=2 + 修改**（rootOrgId≠0）：加载现有虚拟组织 + 更新基本信息
  - **roottype=1**（实组织）：加载现有 rootOrg + 更新 index + bsed
  - **新建场景**（dbStructProjectDy=null L123）：`new BatchOrgExecuteOpService(new BatchAdminOrgNewOpService(...)).execute()` 派生根组织
  - **修改场景** + roottype/effdt 变化：调 `OtherStructService.saveOtherStruct()` · 改写整个下挂组织树

**派生根组织流向**（roottype=2 时触发）：
```
StructProjectSaveOp.saveStructProjectAndRootOrg
  └─→ BatchOrgExecuteOpService.execute (BatchOrgExecuteOpService.java L42)
        └─→ BatchAdminOrgNewOpService.execute (L80-96)
              └─→ super.execute() · BatchProjectTeamNewOpService（标品父类）
              └─→ TimeLineHelper.saveTimelineDys("haos_orgteamcooprel"...)
        └─→ BatchAdminOrgNewOpService.endDoChangeOp (L98-104)
              └─→ saveAdminOrgMsgDetail (L113-122) · 写 haos_adminorg_msgdetail
              └─→ saveAdminorgChangeLog (L124-131) · 写 admin_org change log
        └─→ BatchAdminOrgNewOpService.afterTransDoOp (L133-175)
              └─→ saveFullNameData (L137)
              └─→ syncOrgUnit / syncOrgChgUnit (L157-163)
              └─→ AdminChangeMsgService.handleChangeMsg (L172) · 派 sch_task
```

### 阶段 3 · 方案审核（audit opKey）

**调用链**（`opkeys/audit.json`）：
1. `HRBaseDataLogOp` · 写日志（仅有这一个）

**业务后果**：status A/B → C · 没有真业务 OP（基础资料标准 · 业务方真要走审批用 OPM）

### 阶段 4 · 方案启用（enable opKey）

**调用链**（`opkeys/enable.json`）：
1. `HRBaseDataLogOp` · 写日志
2. `StructProjectEnableOp` · 注册 StructProjectValidator + 声明 org 字段 + 空 beginOperationTransaction

**状态变化**：enable 0 → 10 → 1（BdVersion 中间态）

**业务后果**：方案进入"可被 haos_structure 实例引用"状态

### 阶段 5 · 方案被派生（外部场景 · 配套）

**触发**：用户在 `haos_structure` 表单选 `relyonstructproject` 引本方案 → 保存 → 派生具体矩阵组织实例

**方案侧无操作**：方案数据不变 · 仅被引用

**反向影响**：方案 enable 状态变化会影响实例的可见性（haos_structure setFilter 过滤 enable in [1, 0]）· 标品没有强制级联禁用

### 阶段 6 · 方案修改（modify + save opKey · 含互斥锁）

**触发**：用户在列表点行 → 点【修改】或在表单点 modify 按钮（opKey=`modify`）

**调用链**：
1. `StructProjectEditPlugin.beforeDoOperation` L217-232：
   - 检查 pageCache `edit_struct_clock`（避免重复抢锁）
   - 调 `MutexHelper.require(view, "haos_structproject", id, "edit_struct", true, sb)`
   - 抢不到锁 → showErrorNotification + setCancel(true)（ResId=`OrgStructProjectPermTreeListPlugin_1`）
2. `afterBindData` L138-161 · `OperationStatus.EDIT` 分支 L140-152：
   - 调 `IStructProjectApplication.isRootOrgMaintain(dataEntity)` 判断根组织是否在维护中
   - 维护中时 setEnable(false, rootorg, roottype) · 锁定根字段
   - enable=10 时允许编辑 rooteffdt
   - roottype=2 时调 `showVirtualRootInfo()` 反查虚拟组织信息回显
   - 已派生虚拟组织时 setEnable(false, isincludevirtualorg) · 锁定开关
3. 用户编辑 → 保存（save opKey · 跑 7 OP 链 · 详见阶段 2）
4. save 成功后 `StructProjectEditPlugin.openOperationPage` L298-303：
   - `MutexHelper.release(...)` 释放锁
   - 清 pageCache（`MUTEX_*` + `edit_struct_clock`）

**roottype 切换的下挂组织迁移**（关键业务规则 R-03）：
- dbRootType ≠ newRootType + dbEffDate ≠ effDate（L131）：
  - 从 `OrgStructRepository.queryOriginalCurrentStructDys` 查所有现有结构数据
  - 构造 OtherStructEntity 列表 · 旧根 → 新根映射
  - `otherStructEntity.setDeleteRoot(true)` 删除旧根
  - `OtherStructService.saveOtherStruct()` 整批迁移
- 业务后果：roottype 切换会改写整个方案下挂的组织树 · ISV 必须加 CS-02 校验"已派生 haos_structure 实例时禁切换 roottype"

### 阶段 7 · 方案禁用（disable opKey）

**调用链**（`opkeys/disable.json`）：
1. `HRBaseDataLogOp` · 写日志
2. `StructProjectDisableOp` · 注册 StructProjectValidator + 空 beginOperationTransaction

**状态变化**：enable 1 → 0

**业务后果**：方案不可见于 enable in [1, 10] 过滤的列表（haos_structure 等）· 已派生 haos_structure 实例**不会自动禁用**（标品没级联）

### 阶段 8 · 方案删除（delete / delete_project opKey）

**调用链**（`opkeys/delete_project.json`）：
1. `StructProjectDeleteOP` · 注册 StructProjectDeleteValidator + 仅 enable=10 行参与删除（L50）

**业务后果**：
- enable=1 / enable=0 行被跳过（看似成功但实际未删）
- enable=10 行调 `structProjectApplication.doWithDelete(dyToDel)` 删除
- `haos_structure` 实例的反向引用**没有被检查** · ISV 必须加 CS-04 反向引用校验

**列表后续操作**（`StructProjectListPlugin.afterDeleteOperation` L179-197）：
- 检查 OperateOption 是否有 TITLE/MESSAGE/SHOW_CONFIRM 变量
- 有 + showConfirm=true：showTipNotification + invokeOperation("refresh")
- 有 + showConfirm=false：用 StaffFormService.showOperationResultPage 弹结果页（带 closeCallBack `deleteConfirmCallBack`）
- 没有：默认 showSuccessNotification（ResId=`StructListPlugin_1` "删除成功"）+ refresh

### 阶段 9 · 维护方案下挂的组织（maintain_struct / maintainframework opKey）

**触发**：
- 表单内点【维护架构】按钮（opKey=`maintain_struct`）
- 列表点行【维护架构】图标 vectorap（opKey=`maintainframework`）

**调用链**（表单）：
1. `StructProjectEditPlugin.openOperationPage` L292-296 · 调 `showMaintainFrameworkForm()`
2. `showMaintainFrameworkForm` L318-337：
   - 构造 ListShowParameter
   - 设 `pageId="haos_orgstructlist_" + structProjectId + "_" + mainPageId`（多窗口隔离）
   - 设 `formId="haos_orgstructlist"` + `billFormId="haos_structorgdetail"`
   - 设 customParam：`custom_parent_f7_prop=boid` + `struct_project_ids=[id]` + `rootorg=id`
   - 设 caption=方案 name
   - showForm 跳到列表

**调用链**（列表）：
1. `StructProjectListPlugin.openOperationPage` L124-127 · 调 `showMaintainFrameworkForm(eventArgs)`
2. `showMaintainFrameworkForm` L151-177（与表单版同）：
   - 多一步：`if ("1040".equals(primaryKeyValue))` 设 `struct_project_is_to_all_areas=true`（业务硬编码 1040 是全领域方案 ID）

## 3. 跨 form 协作流程（与 haos_structure）

```
[阶段 1-8 本场景]                    [haos_structure 配套场景]
                                       │
新建方案 → save → enable               │
   │                                   │
   ▼                                   │
方案已启用 (enable=1)                  │
                                       ▼
                              用户在 haos_structure 列表
                              点【新增】 → 选 relyonstructproject = 本方案
                                       │
                                       ▼
                              填 rootorg / 配置实例字段 → save
                                       │
                                       ▼
                              haos_structure.relyonstructproject = 本方案 id
                              t_haos_structproject 同表写入实例数据 (otclassify=1010L)
                                       │
                                       ▼
                              用户在 haos_structure 点【维护架构】
                              跳到 haos_orgstructlist + haos_structorgdetail
                              在矩阵下挂具体的行政组织

[本场景维护] modify → save             │
   │                                   │
   ▼                                   │
roottype 切换会改写下挂组织树（R-03）  │
   │                                   │
   ▼                                   │
ISV 应加 CS-02 校验：已派生实例时禁改  │

[本场景禁用] disable                   │
   │                                   │
   ▼                                   │
enable=0                               │
                                       ▼
                              haos_structure 列表 setFilter
                              `enable in [1, 0]` · 被禁用方案
                              的实例仍可见 · 但用户操作时
                              视业务约束（标品没级联）

[本场景删除] delete                    │
   │                                   │
   ▼                                   │
仅 enable=10 行真删                    │
ISV 应加 CS-04 反向引用校验            │
                                       ▼
                              如果方案被删除而无反向引用
                              校验 → haos_structure.relyon-
                              structproject 引用悬挂 → 业务故障
```

## 4. 关键事件触发点（含 BEC "伪发布"派单）

```
                                      ┌── 派单触发条件 ──┐
                                      │                  │
   StructProjectSaveOp                 │                  │
     beginOperationTransaction         │                  │
        ├─ saveStructProjectAndRootOrg │                  │
        │    └─ roottype=2 / 1 时        │                  │
        │       派生 / 修改根组织      │                  │
        │       BatchAdminOrgNewOp     │                  │
        │       Service.execute        │                  │
        │       └─ endDoChangeOp       │                  │
        │            └─ saveAdminOrg-  │                  │
        │               MsgDetail      │ 写 haos_adminorg_│
        │               (L113-122)     │ msgdetail 表    │
        │            └─ saveAdminorg-  │                  │
        │               ChangeLog      │ 写 admin_org    │
        │               (L124-131)     │ change log 表   │
        │       └─ afterTransDoOp      │                  │
        │            └─ AdminChange-   │                  │
        │               MsgService.    │ JobClient.      │
        │               handleChange-  │ dispatch 派单到 │
        │               Msg (L172)     │ sch_task         │
        │                              └──────────────────┘
        │
        ▼
   主事务提交（L82 以后）
```

**伪 BEC vs 标准 BEC**：
- **本场景实证**：14 反编译类全 grep · 0 处 `triggerEventSubscribe / IEventService / EventServiceHelper`
- **本场景做法**：用 `JobClient.dispatch(jobInfo)` + 调度服务 sch_task 表 · admin_org 域共用机制
- **跟标品 BEC 区别**：标品 BEC 走业务事件中心 + IEventService · 本场景没接
- **ISV 想要标品 BEC**：CS-05 自建发布方 + 业务事件中心预配 eventNumber

## 5. 异常分支

| 异常 | 触发条件 | 处理 |
|---|---|---|
| 抢不到 edit_struct 互斥锁 | 同方案在其它页签打开 | StructProjectEditPlugin.beforeDoOperation L228 · setCancel(true) + showErrorNotification（ResId=`OrgStructProjectPermTreeListPlugin_1`）|
| 用户无创建组织 BU 权限 | OrgPermHelper.getHRPermOrg(true).getHasPermOrgs() 为空 | StructProjectEditPlugin.afterCreateNewData L131-133 · LOG.error · 表单仍打开但 org 字段为空 |
| StructProjectValidator 校验失败 | save / enable / disable 时业务约束不满足 | onAddValidators 注册的 Validator 走 addErrorMessage · 阻断 OP 链 |
| StructProjectDeleteValidator 校验失败 | delete 时业务约束不满足 | 同上 |
| BatchAdminOrgNewOpService 派生失败 | 跨表写出错 | 主事务回滚（PR-010 beginOperationTransaction 抛异常时事务回滚）|
| sch_task 派单失败 | JobClient.dispatch 异常 | LOG · 不阻断主流程（异步任务独立事务）|

## 6. 跟 admin_org 业务事件的复用关系

本场景**复用** admin_org 域的派单 service：
- `AdminChangeMsgService` · 写 `haos_adminorg_msgdetail` 表
- `AdminOrgChangeLogService` · 写 admin_org change log
- `BatchAdminOrgNewOpService` · 派生根组织时调（继承 `BatchProjectTeamNewOpService`）
- `BatchOrgDeleteOpService` · 删除旧根组织时调（roottype 切换时）

**这意味着**：
- 订阅方监听 `haos_adminorg_msgdetail` 表变化 · 可以同时收到 admin_org 域和本场景的变更（但要按 changescene 区分）
- ISV 不需要重复造派单机制 · 复用 admin_org 即可
