# 上下游联动 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译实证 · 跨域影响梳理
> **数据源**: PermfilesListPlugin · ServiceHelper 调用图 · 2026-04-28

---

## 一、上下游图谱总览

```
            [hrpi_pernontsprop / hrpi_empjobrel] (人事档案 · HisModel)
                          ↓ syncperm（按人事档案生成）
            [HRAdminService.isHrAdmin / PermissionServiceHelper] (准入)
                          ↓
            [hrcs_admingroup] (HR 域管理员组 · 决定 setFilter)
                          ↓
─────────  hrcs_permfilelist 列表壳  ─────────
                          ↓
[hrcs_permfilegrp] ← (TreeView) ← PermfilesListPlugin → (BillList) → [hrcs_userpermfile]
       ↑                                                                       ↓
[hrcs_permfilegrpmember] ← batchgroup                              [hrcs_userrolerelat]
       ↓                                                                       ↓
                         [perm_role / hrcs_role] (角色)
                                                  ↓
                             [hrcs_dynascheme] (动态授权方案 · sourcetype="4")
                                                  ↓
            [全系统鉴权缓存 HRPermCacheMgr] (clearAllCache 影响)

     [BEC IEventService] ← ❌ 标品 0 处发布 · ISV 自建（CS-05）
     [外部 BPM / 数仓 / SAP HR] ← ISV 订阅方
```

---

## 二、上游模块（被谁触发 / 数据来自哪里）

### 2.1 HR 域管理员体系（准入闸）

| 上游 | 接口 | 触发点 |
|---|---|---|
| `PermissionServiceHelper.isAdminUser(userId)` | 平台超管判断 | HRAdminStrictPlugin.preOpenForm L42 |
| `PermCommonUtil.isCosmicUser(userId)` | 苍穹技术管理员 | 同上 L43 |
| `HRAdminService.isHrAdmin()` | HR 域管理员 | HRAdminStrictPlugin.isHrAdmin L55-L57 |
| `HRPermServiceHelper.getUserGroupMinLevel()` | admingroup 等级 | PermfilesListPlugin.setFilter L1032 |
| `HRPermServiceHelper.getUserPermFile()` | 用户可见权限档案 ID 列表 | PermfilesListPlugin.setFilter L1034 |

⚠️ **HRAdminService 是关键准入点** —— 整个 hrcs 域 11 表单都靠它。ISV 不要 mock / 替换。

### 2.2 人事档案体系（数据源）

| 上游 | 接口 | 触发点 |
|---|---|---|
| `hrpi_pernontsprop` | 人员非时序属性（personid / mainorgid） | syncperm 任务用作筛选 |
| `hrpi_empjobrel` | 员工任职关系（time-series） | syncperm 任务（按 laborrelType 过滤） |
| `hrpi_pertype` | 员工用工类型 | hrcs_syncpermfile 弹窗收集 laborrelTypeIds |

⚠️ **本场景写 hrcs_userpermfile · 但 user 实际指向 bos_user** · 而 bos_user 的人事属性来自 hrpi_*。如果 hrpi_* 是时序资料 · 查时必须 `iscurrentversion=true`（PR-008）。

### 2.3 角色体系

| 上游 | 接口 | 触发点 |
|---|---|---|
| `perm_role` | 平台角色 · hrcs_userrolerelat.role 关联 | copyperm / assignrole / 列表显示 |
| `hrcs_role` | HR 中台角色（基于 perm_role 的特化） | RoleDBServiceHelper.queryHrRoleDyn |
| `HRRolePermHelper.queryViewableRoles(userId)` | 当前操作员可见角色集 | copyperm 第三重校验 L1176 |
| `HRRolePermHelper.queryUserAdminGroups(userId)` | 用户的管理员组 | hrcs_dynascheme 查询过滤 |

### 2.4 动态授权方案（hrcs_dynascheme）

| 关系 | 触发点 |
|---|---|
| dynascheme audit / confirmchange 通过 → INSERT hrcs_userrolerelat（sourcetype="4"） | DynaAuthSchemeAuditOp / DynaAuthSchemeConfirmChangeOp |
| 本场景列表显示这些"动态分配"的角色 · 与"手动 assignrole"的角色混合呈现 | hrcs_userassignrole 表单（无来源区分） |

⚠️ **删除/失效本场景 permfile 时 · sourcetype="4" 的角色也会跟着失效**（标品 RoleServiceHelper.disablePermfile 不区分 sourcetype）· 这可能让动态方案数据脏（CS-04 推荐方案）。

---

## 三、下游模块（影响谁）

### 3.1 hrcs_userrolerelat（强耦合 · 实时联动）

| 触发 opKey | 写入 |
|---|---|
| save（含编辑）| 改 hrcs_userpermfile · 不直接改关联（关联通过 assignrole 弹窗改） |
| disable | RoleServiceHelper.disablePermfile · 级联失效全部关联 |
| delete | RoleServiceHelper.disablePermfile + 物理删 / 标品 jar 缺失推断 |
| copyperm | INSERT 新关联（一档案 N 角色 → 复制到目标用户产生 N 行新关联） |
| assignrole | hrcs_userassignrole 弹窗内部直接 INSERT |

### 3.2 hrcs_permfilegrpmember（强耦合 · 直接写）

| 触发 | 行为 |
|---|---|
| batchgroup | hrcs_permfilegrptree 弹窗内部 INSERT |
| 删档案 | 级联清（推断 PermFilesSaveOp 内部）|
| 删分组 | canDelGroup 阻断（不允许删非空分组）|

### 3.3 全系统鉴权缓存（HRPermCacheMgr）

| 触发 | 影响范围 |
|---|---|
| save / enable / disable / delete | 全 hrcs 用户的 perm 缓存（推断 PermFilesSaveOp 内 + 反编译 setEnable L779） |
| btn_clearcache 手动 | clearAllCache + clearAllManageCache |

⚠️ **缓存清空是全系统级** —— 一个 disable 操作让全 hrcs 鉴权命中率瞬时归零 · DB 压力短时拉高。

### 3.4 调度任务（kd.bos.schedule）

| Task 类 | 触发 opKey | 调度方式 |
|---|---|---|
| `kd.hr.hrcs.bussiness.task.SyncPermFilesTask` | syncperm | JobType.REALTIME · 单实例 |
| `kd.hr.hrcs.formplugin.web.perm.init.task.PermFilesExportTask` | exportuserperm | 异步 · 多实例 |

### 3.5 业务事件中心（BEC · 标品 0 发布 · ISV 可发）

⚠️ grep `triggerEventSubscribe|IEventService|EventServiceHelper` 反编译目录全无命中。**标品没发任何 BEC**。

ISV 自建 BEC 发布场景（CS-05）：
- 失效档案通知 BPM
- 删除档案通知数仓
- 复制权限通知合规审计

订阅方需在【开发平台】→【业务事件订阅】挂载 IEventServicePlugin。

---

## 四、跨子域联动详情表

| 下游子域 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| `hrpi`（人事） | syncperm 按人事档案生成 | 异步（SyncPermFilesTask） | 按错误信息提示 · 部分成功部分失败 |
| `pay`（薪酬） | ❌ 无直接联动（薪酬通过 perm_role 间接关联） | - | - |
| `attendance`（考勤） | ❌ 无直接联动 | - | - |
| `performance`（绩效） | ❌ 无直接联动 | - | - |
| `hrcs_dynascheme`（动态方案） | sourcetype="4" 角色绑定共用 hrcs_userrolerelat | 同步（OP 内 INSERT） | dynascheme audit 失败 · permfile 不受影响 |
| `hrcs_admingroup`（HR 域管理员组） | setFilter 用 admingroup level / org 限定 | 同步 | admingroup 查询失败 · 列表为空 |
| `bos_user`（用户基础资料） | user.isforbidden 影响档案能否复制 / 失效 | 同步 | bos_user 失败 · 操作中断 |
| `BPM`（外部）| ISV BEC 发布触发权限交接流程（CS-05） | 异步（BEC） | 订阅方失败 · BEC 重试（按订阅配置） |
| `数仓 / 审计` | ISV BEC 推数据 | 异步 | 累积重试 |
| `SAP HR` 等外部 | ISV BEC + REST API | 异步 | 业务侧补偿 |

---

## 五、字段级联动表

| 字段 | 上游来源 | 下游影响 |
|---|---|---|
| `hrcs_userpermfile.user` | `bos_user`（员工选择F7）| `hrcs_userrolerelat.user` 同步同 |
| `hrcs_userpermfile.org` | HR 行政组织（HAOS_adminorg）| 列表 setFilter 限定 / 数据隔离 |
| `hrcs_userpermfile.permfileenable` | enable / disable opKey | 影响列表显示 / hrcs_userrolerelat 级联失效 |
| `hrcs_permfilegrp.longnumber` | parent 计算（前缀树）| 子树查询用 like '<longnumber>.%' |
| `hrcs_permfilegrpmember.permfilegrp` | batchgroup 选择 | 树节点点击过滤主列表 |
| `hrcs_userrolerelat.role` | 角色 F7（HRRolePermHelper.showRoleF7） | 用户实际权限命中 |
| `hrcs_userrolerelat.customenable` | 角色范围属性 | 数据范围鉴权 |
| `hrcs_userrolerelat.sourcetype`（推断 · 与 dynascheme 共用） | dynascheme="4" / 本场景默认 | 删除 / 失效路径区分 |

---

## 六、上下游事务关系

| 写入序列 | 同事务 / 跨事务 |
|---|---|
| save permfile + 加 grpmember 关联 | 同事务（同一 OP 链） |
| save permfile + clearAllCache | **跨事务**（缓存调用在事务外）|
| disable permfile + disablePermfile（级联失效角色） | 同事务 |
| disable permfile + clearAllCache | 跨事务（同上）|
| delete permfile + 级联清 grpmember + 清角色关联 | 同事务（推断 PermFilesSaveOp 内）|
| copyperm 跨用户 | 同事务（hrcs_copyperm 弹窗内）|
| syncperm 异步任务 | **独立事务**（后台 worker）|
| BEC 发布 | **独立事务**（订阅方在 IEventServicePlugin 里另起）|

⚠️ **缓存清理永远跨事务** —— 即便代码上看起来在 OP 内 · clearAllCache 是直接 RPC 到 Redis · 不参与 DB 事务。

---

## 七、数据一致性的脏数据风险点

### 7.1 disable 失败 · 缓存已清

OP 步骤 1（UPDATE permfile）成功 · 步骤 2（disablePermfile 级联）失败回滚 · 但步骤 3（clearAllCache）已发生 · 短暂时间出现"DB 旧 + 缓存空"。

**业务影响**：用户登录瞬间没权限（缓存空 · DB 仍是旧值反而能拿权限）→ 缓存重建后恢复。窗口数秒。

**对策**：标品已知行为 · 不修。如果 ISV 要消除 · 必须在 endOperationTransaction 后做缓存清理（而不是事务内）。

### 7.2 syncperm 任务跑完 · 列表没刷新

任务在后台跑完 · `HRAppCache` 已存结果 · 但用户没点【刷新】 · 列表数据是旧的。

**对策**：closedCallBack(syncPermFilesTask) 自动 showConfirm 提示用户刷新。

### 7.3 dynascheme 改了 · permfile 列表不立即更新

dynascheme audit / confirmchange 写 hrcs_userrolerelat（sourcetype="4"）· 但本场景列表的 setFilter 用的是 admingroup org · 不是直接读 hrcs_userrolerelat。这种改动不会立即在列表表现出来 · 看角色是 hrcs_userassignrole 弹窗才能看到。

**对策**：ISV 可加"按角色筛选"列表过滤（CS-06 类似）· 让用户能看到 sourcetype="4" 的关联。

### 7.4 BEC 订阅方失败 → 数据不一致

ISV BEC 发了 · 但外部 BPM 订阅方失败 · BEC 按配置重试。如果重试都失败 · BPM 的"权限交接"流程没起 · 业务流断裂。

**对策**：ISV 订阅方 handleEvent 内 · 失败要日志 + 抛异常（让 BEC 重试）。重试上限达到 · 平台会标记事件失败 · 需要人工介入。

---

## 八、上下游联动的标品 vs ISV 边界

| 联动点 | 标品做了什么 | ISV 应该做什么 |
|---|---|---|
| 准入闸 | HRAdminStrictPlugin · 双闸 | ❌ 不要继承 · 配置即可 |
| 列表过滤 | setFilter · admingroup level | ✅ super 后追加 ISV 过滤（CS-06）|
| disable 级联角色 | RoleServiceHelper.disablePermfile | ✅ ISV OP 加前置确认（CS-03）/ 后置 BEC（CS-05）|
| syncperm 调度 | SyncPermFilesTask | ❌ 不要复用同 task · 自起独立任务 |
| BEC 发布 | ❌ 0 处 | ✅ ISV 自建（CS-05）|
| BEC 订阅 | 无 | ✅ ISV 集成 BPM / 数仓 / SAP |
| 缓存清理 | clearAllCache | ❌ 不要重复 · 标品已做 |

---

参考文档：
- `04_business_flow.md` —— 5 大流程详解
- `05_data_flow.md` —— SQL 级数据流
- `06_customization_solutions.md` —— CS-04/CS-05 跨场景定制
- `08_impact_analysis.md` —— 改动影响面

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
