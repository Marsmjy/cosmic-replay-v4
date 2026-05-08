# 变更影响面 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 14 反编译类 + 52 opKey + refEntity 反向追踪
> **confidence**：verified

## 1. 影响维度总览

```
              ┌─ 直接派生 ─────────────────────────┐
              │ haos_structure（实例引本方案）     │
              │ haos_adminorgdetail（虚拟根组织 · │
              │   roottype=2 时派生）              │
              │ haos_adminorgstruct（OrgStruct 下挂）│
              └────────────────────────────────────┘
                              │
              ┌─ 间接 ──────────────────────────────┐
              │ haos_orgteamcooprel（组织团队协作）│
              │ haos_adminorg_msgdetail（变更消息）│
              │ admin_org change log               │
              │ sch_task（异步派单）               │
              └────────────────────────────────────┘
                              │
              ┌─ 反向引用 ──────────────────────────┐
              │ haos_structproject.relyonstructproject │
              │   （自引用 · 链式依赖）            │
              │ haos_structure.relyonstructproject │
              │   （实例反向引方案）              │
              └────────────────────────────────────┘
```

## 2. 改本场景对 haos_structure（直接派生 · 配套场景）的影响

### 2.1 修改方案 roottype（实↔虚切换）的影响

| 改动 | 标品行为 | 对 haos_structure 实例的影响 | 风险等级 |
|---|---|---|---|
| roottype 1 → 2 | StructProjectSaveOp.saveStructProjectAndRootOrg L131-180 调 OtherStructService 改写下挂组织树 + setDeleteRoot(true) 删旧根 | **整批改写已派生实例的下挂组织** · 旧根 → 新根迁移 | **🔴 高**（业务故障核心） |
| roottype 2 → 1 | 同上 | 同上 | 🔴 高 |
| roottype 不变 + effdt 变 | virtualAndNotChange 走 HisModelAPIService.reviseVersions 走历史版本路径 | 实例下挂 startdate 同步更新 | 🟡 中 |
| roottype 不变 + effdt 不变 | 仅更新基本信息 | 无影响（实例下挂不动）| 🟢 低 |

**ISV 必加校验**：CS-02 · save 阶段 onAddValidators 反查 haos_structure.relyonstructproject IN (saveIds) · 已被引用时阻断 roottype 切换

### 2.2 修改方案 enable 状态（启用 / 禁用）的影响

| 改动 | 标品行为 | 对 haos_structure 实例的影响 |
|---|---|---|
| enable 0 → 1 | StructProjectEnableOp 走 HRBaseDataEnableOp 改 enable 字段 | 实例对应方案"已启用" · 列表 setFilter `enable in [1, 0]` 都可见 |
| enable 1 → 0 | StructProjectDisableOp 走 HRBaseDataLogOp 写日志 | **没级联禁用实例** · 实例仍为 enable=1 状态（业务方真要级联走 CS-04 / CS-05）|

**ISV 选项**：
- 禁用方案后通知下游：CS-05 BEC 发布方 · 订阅方收到后批量禁用相关实例
- 禁用方案前置校验：参考 CS-04 反向引用 · 阻断"已被实例引用的方案禁用"

### 2.3 删除方案的影响

| 改动 | 标品行为 | 对 haos_structure 实例的影响 |
|---|---|---|
| delete / delete_project（标品） | StructProjectDeleteOP.beginOperationTransaction L46-55 仅 enable=10 行删除 · 没反向引用校验 | **悬挂引用风险** · 实例的 relyonstructproject = 已删方案 id · 业务方读实例时会发现引用不到母本 |

**ISV 必加校验**：CS-04 · delete + delete_project 同时挂 onAddValidators · 反查 haos_structure.relyonstructproject IN (deleteIds) · 有引用就阻断

### 2.4 修改方案字段的影响

| 改动 | 标品行为 | 对 haos_structure 实例的影响 |
|---|---|---|
| 改 number（编码）| 标品没限制 · 但 issyspreset='1' 的预置母本 number 不应改（PR-007）| 实例显示的"母本编码"也变（如果实例有显示这个字段）|
| 改 name（多语言名）| 落 t_haos_structproject_l | 实例显示的"母本名"也变 |
| 改 isincludevirtualorg | 关联控制虚拟组织开关 | 标品 afterBindData L150 已派生虚拟组织时锁定不可改 · 已派生实例不再受影响 |
| 改 otclassify | 母本一般 ≠ 1010 · 改成 1010 会让母本"变成实例"（被列表过滤） | 实例可能误读到母本（业务上不应改）|
| 加 ISV 扩展字段（CS-01）| 物理列加在共用表 · 影响 haos_structure 实例 | 走 CS-06 form 元数据隔离 · 让实例表单看不见此字段 |

## 3. 改本场景对派生 haos_adminorg / haos_adminorgstruct（变体）的影响

### 3.1 派生根组织（roottype=2 时）的影响

| 改动 | 影响 | 反编译实证 |
|---|---|---|
| 新建虚拟根方案 | 派生新 haos_adminorgdetail 行（带 isvirtualorg=1, otclassify=1010）| StructProjectSaveOp L94-105 |
| 修改虚拟根 number/name | 同步更新 haos_adminorg.number/name | StructProjectSaveOp L108-110 |
| 修改 effdt 同步到 bsed | 更新 haos_adminorg.bsed | StructProjectSaveOp L111 |
| 修改 description | 同步更新 haos_adminorg.description | StructProjectSaveOp L110 |
| roottype 1 → 2（且无现有根）| 走"新建虚拟根"路径 + 派单 | 同 3.1 第 1 行 |
| roottype 2 → 1（删旧虚拟根 + 用现成实组织）| **删除旧虚拟根** + 加载新实组织 | StructProjectSaveOp L141-145 走 BatchOrgDeleteOpService + BatchAdminOrgNewOpService |

### 3.2 OrgStruct 下挂关系的影响

| 改动 | 标品行为 | 实证 |
|---|---|---|
| 同 roottype + 同 effdt | 不动 OrgStruct | StructProjectSaveOp L114-118 |
| 同 roottype + 不同 effdt | 走 HisModel 时序版本（virtualAndNotChange）or 整批迁移 | StructProjectSaveOp L131-180 |
| roottype 切换 | **整批改写 OrgStruct** · setDeleteRoot=true 删旧根 | StructProjectSaveOp L162-180 |

### 3.3 OrgTeamCoopRel（组织团队协作关系）的影响

| 改动 | 标品行为 | 实证 |
|---|---|---|
| 派生新根组织（roottype=2）| BatchAdminOrgNewOpService.execute L93-94 调 `TimeLineHelper.deleteTimelineDys` 旧 + `saveTimelineDys` 新 | BatchAdminOrgNewOpService.java |
| 修改方案不派生根 | 不动 OrgTeamCoopRel | - |

## 4. 改本场景对 admin_org 域消息系统的影响

### 4.1 写入 haos_adminorg_msgdetail 的影响

| 改动 | 写入 | 实证 |
|---|---|---|
| 派生新根组织（roottype=2 + 新建/修改）| `BatchAdminOrgNewOpService.endDoChangeOp` L98-104 调 saveAdminOrgMsgDetail · 写 haos_adminorg_msgdetail 一条记录 | BatchAdminOrgNewOpService.java L113-122 |

### 4.2 写入 admin_org change log 的影响

| 改动 | 写入 | 实证 |
|---|---|---|
| 派生新根组织 | `BatchAdminOrgNewOpService.endDoChangeOp` L98-104 调 saveAdminorgChangeLog · 写 admin_org change log 表 | AdminOrgChangeLogService.java |

### 4.3 触发 sch_task 调度的影响

| 改动 | 调度 | 实证 |
|---|---|---|
| 派生新根组织 + 非 batchApply 模式 + 非 importtype | `BatchAdminOrgNewOpService.afterTransDoOp` L172 调 `AdminChangeMsgService.handleChangeMsg` · 派 sch_task | BatchAdminOrgNewOpService.java L167-174 |

**派单防风暴机制**：`AdminChangeMsgService.handleChangeMsg` L113-118 查 sch_task 已有 `JOB_ID="5+X/4Y=AOZ=O"` 处于 SCHEDULED 状态时跳过派单（一个 JOB 同时只跑一个）。

## 5. 反向引用影响（被谁引）

### 5.1 haos_structure 实例反向引方案

- 字段：`haos_structure.relyonstructproject` → haos_structproject
- 影响：方案任何变更（roottype / enable / 删除）都可能影响实例
- ISV 处理：CS-02 / CS-04 / CS-05 全部跨表查 haos_structure 反向引用

### 5.2 haos_structproject 自引用（链式依赖）

- 字段：`haos_structproject.relyonstructproject` → haos_structproject（自引）
- 业务语义：方案 A 依赖方案 B（某个方案的字段范围 / 权限模板从另一个方案派生）
- 影响：删除方案 B 会导致方案 A 的链式依赖悬挂
- ISV 处理：CS-04 校验时除查 haos_structure 还要查自引用（`haos_structproject.relyonstructproject IN (deleteIds)`）

### 5.3 业务下游可能监听的表（推断 · 非本场景代码）

- `haos_othorgstruct` · 其它组织视图（haos_structure 实例的下游）
- `haos_adminorgstruct` · 行政组织树
- `haos_orgteamcooprel` · 组织团队协作关系

## 6. 跨域影响（跟 admin_org 域）

### 6.1 共用 service 类

本场景**复用**以下 admin_org 域 service：

| Service | 来自 jar | 用途 | 标品依赖 |
|---|---|---|---|
| `BatchAdminOrgNewOpService` | hrmp-haos-business-1.0.jar | 派生 / 修改根组织 | StructProjectSaveOp 调 |
| `BatchOrgDeleteOpService` | 同上 | 删除旧根（roottype 切换时）| StructProjectSaveOp 调 |
| `BatchOrgExecuteOpService` | 同上 | 执行 OP 包装（壳）| StructProjectSaveOp 调 |
| `OrgChangeDetailService` | 同上 · 591 行 · 业务核心 | 组织变动详情 service · 26 reads / 18 writes | BatchAdminOrgNewOpService 调（继承父类）|
| `AdminChangeMsgService` | 同上 · 124 行 | 变更消息 + sch_task 派单 | BatchAdminOrgNewOpService.afterTransDoOp 调 |
| `AdminOrgChangeLogService` | 同上 · 226 行 | 变更日志 | BatchAdminOrgNewOpService.endDoChangeOp 调 |

**意味着**：
- 修复 admin_org 域 bug 会影响本场景的派生根组织流程
- 升级 admin_org 域 service 签名变化会破坏本场景（`BatchAdminOrgNewOpService` 构造函数签名变化）

### 6.2 共用消息表

`haos_adminorg_msgdetail` 是 admin_org 域的消息表 · 本场景的派生根组织也写这个表。订阅方需按 `changescene` / `changeoperate` 区分来源（admin_org 直接变更 vs 本场景派生）。

## 7. 改动模拟矩阵（典型 ISV 改动 → 影响）

| ISV 改动 | 直接影响 | 间接影响 | 反向影响 | 总风险 |
|---|---|---|---|---|
| 加字段 `${ISV_FLAG}_appdomain`（CS-01）| 物理列加 · 主表多列 | haos_structure 实例物理列也多（NULL 兜底）| 无 | 🟢 低 |
| 加字段 + form 隔离（CS-06）| 物理列加 · 仅 haos_structproject form 显示 | haos_structure 表单看不到 · 业务无感 | 无 | 🟢 低 |
| onAddValidators@save 加 roottype 校验（CS-02）| save 时多一道校验 · 阻断 roottype 切换 | 不影响下游 · 阻断在前 | 改善反向引用一致性 | 🟢 低（防御性）|
| onAddValidators@delete 加反向引用校验（CS-04）| delete 时多一道校验 | 不影响下游 · 阻断在前 | 改善反向引用一致性 | 🟢 低（防御性）|
| afterExecuteOperationTransaction 发 BEC（CS-05）| 主事务后多发一条事件 | 订阅方异步消费 · 不阻断主流 | 通知派生实例服务 | 🟡 中（业务事件中心需配 eventNumber）|
| propertyChanged 加联动（CS-03）| FormPlugin 多一道联动 | 不影响 OP / 数据 | 无 | 🟢 低 |
| 替换 StructProjectBUListPlugin（左树）| 改变左树过滤逻辑 | 列表显示数据范围变 | 无 | 🟡 中（需要全量回归测试列表）|

## 8. 生产事故案例（业务方常见踩坑）

### 8.1 事故 A · roottype 切换误改下挂组织树

**场景**：业务方在已建好"销售域方案 A"（roottype=1 实组织）+ 派生 5 个 haos_structure 实例后 · 在 haos_structproject 表单把 roottype 改成 2（虚拟根）保存。

**结果**：标品 StructProjectSaveOp.saveStructProjectAndRootOrg L139-180 走"非同 roottype + 非同 effdt"分支 · 调 OtherStructService.saveOtherStruct(setDeleteRoot=true) · 整批改写 5 个实例下挂的 OrgStruct（旧实根 → 新虚根）· 旧实根被删

**业务后果**：5 个实例的下挂组织全部错位 · 用户在 haos_orgstructlist 看到的层级跟昨天完全不同

**预防**：CS-02 · save 阶段 onAddValidators 加跨表校验 · 已被引用时阻断 roottype 切换

### 8.2 事故 B · 删除已被引用的方案 → 实例引用悬挂

**场景**：业务方在 haos_structproject 表单删除"试用方案 B"（enable=10 启用中）· 但 B 已被 1 个 haos_structure 实例引用（relyonstructproject = B id）。

**结果**：标品 StructProjectDeleteOP.beginOperationTransaction L46-55 直接 doWithDelete 删除 · 没反向引用校验

**业务后果**：实例的 relyonstructproject 引用 B（已不存在）· 用户在 haos_structure 列表点开实例时 · F7 字段显示空 / 出错

**预防**：CS-04 · delete + delete_project 阶段 onAddValidators 加反向引用校验 · 阻断删除被引用方案

### 8.3 事故 C · 共用物理表导致 ISV 字段污染配套场景

**场景**：业务方给 haos_structproject 加字段 `${ISV_FLAG}_appdomain`（CS-01）· 但**没**走 form 元数据隔离（CS-06）· 直接修改 haos_structproject 主表 metadata（违反 PR-001）

**结果**：物理列加成功 · 但因为 haos_structproject 和 haos_structure 共用 t_haos_structproject 物理表 · haos_structure 表单也"看到"这个字段（自动出现在表单某处）

**业务后果**：haos_structure 表单多了"应用领域"字段 · 业务方混淆"实例为什么要选应用领域"

**预防**：CS-06 思路 A · ISV 扩展元数据**只挂 haos_structproject** · 不挂 haos_structure · 物理列共用但 form 元数据隔离

### 8.4 事故 D · 套用 hjm 模式发 BEC

**场景**：业务方按 hjm_jobhr CS-05 给 haos_structproject 写发布方 · 在 OP 里调 `EventServiceHelper.triggerEventSubscribeJobs`（标品 hjm 标品有这个调用）· 但**没**在【业务事件中心】配 eventNumber

**结果**：调用静默失败 · 无错误 · 订阅方什么也收不到

**业务后果**：业务方以为代码部署成功 · 实际事件根本没发出去 · 测试半天找不到原因

**预防**：CS-05 必读 ⚠ 重要前置 · 本场景标品**没**发标准 BEC（grep 0 命中）· ISV 必须先在业务事件中心配 eventNumber 再发

## 9. 改动需要回归测试的范围

### 9.1 ISV 加字段（CS-01）后

- ✅ haos_structproject 表单：新字段是否显示 / 必填 / 联动正确
- ✅ haos_structure 表单（共用物理表）：新字段是否**未**显示（CS-06 思路 A）
- ✅ 列表查询性能：新字段是否影响 setFilter
- ✅ 物理表 SQL 报表：新字段 NULL 兜底是否正确

### 9.2 ISV 加 Validator（CS-02 / CS-04 / CS-07）后

- ✅ 标品 6 个 Validator 仍正常运行
- ✅ ISV 新 Validator 在跨表查询性能可接受
- ✅ 错误消息显示在表单正确位置
- ✅ 列表批量操作时 Validator 行为正确（每条独立校验）

### 9.3 ISV 加 BEC 发布方（CS-05）后

- ✅ 业务事件中心配置正确（eventNumber 已建）
- ✅ 主事务正常提交（BEC 失败不回滚主事务）
- ✅ 订阅方收到事件 + 幂等处理
- ✅ 跟标品 sch_task 派单**并行不冲突**（两套独立）

### 9.4 ISV 改列表过滤后

- ✅ 全权限 / 部分权限用户都能正确过滤
- ✅ 标品 setFilter 4 层逻辑（issyspreset / iscustomorg / org BU / orderBy）仍生效
- ✅ 列表导入导出（HIES）行为不变

## 10. 来源追溯

- 直接派生：`StructProjectSaveOp.saveStructProjectAndRootOrg` L84-181
- OrgStruct 改写：L131-180 + OtherStructService.saveOtherStruct
- 派单：`BatchAdminOrgNewOpService.afterTransDoOp` L133-175 + AdminChangeMsgService.handleChangeMsg L113-123
- 反向引用：scene_doc.json fields[24].refEntity = haos_structproject（自引用）+ haos_structure_maintenance/03 model_design 实证
- 跨域共用：admin_org 域 6 个 service 类（详见 plugin_semantics）
