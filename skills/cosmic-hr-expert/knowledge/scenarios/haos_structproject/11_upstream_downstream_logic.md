# 上下游联动 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 14 反编译类 + refEntity 反向追踪 + 配套场景实证
> **confidence**：verified

## 1. 上下游全图

```
                          ┌─ 上游（数据来源）─────────────────────┐
                          │ · 业务方手动建方案（haos_structproject │
                          │   表单 · new + save）                  │
                          │ · 列表导入 HIES（importdata_hr opKey）│
                          │ · 标品系统预置（issyspreset='1' ·     │
                          │   STRUCT_PROJECT_MANAGE）              │
                          └────────────────────┬──────────────────┘
                                               │
                                               ▼
                          ┌─ 本场景核心 ──────────────────────────┐
                          │ haos_structproject（35 字段）         │
                          │   ├─ t_haos_structproject 主表        │
                          │   └─ t_haos_structproject_l 多语言    │
                          └────────┬──────────┬───────┬───────────┘
                                   │          │       │
        ┌──────────────────────────┘          │       └────────────────────┐
        │                                      │                            │
        ▼                                      ▼                            ▼
┌─ 直接派生 ──────────────┐  ┌─ 间接 / 异步 ─────────────┐  ┌─ 反向引用（被谁引）──┐
│ haos_adminorgdetail     │  │ haos_orgteamcooprel       │  │ haos_structure       │
│ （roottype=2 时派生     │  │ （组织团队协作 · BatchAd- │  │ （实例 ·             │
│   虚拟根组织）          │  │   minOrgNewOpService L93- │  │   relyonstructproject│
│                         │  │   94 写）                 │  │   引本方案）         │
│ haos_adminorgstruct     │  │                           │  │                      │
│ （行政组织树 · OrgStruct│  │ haos_adminorg_msgdetail   │  │ haos_structproject   │
│   下挂关系）            │  │ （变更消息 · BatchAdminOrg│  │ （自引用 ·           │
│                         │  │   NewOpService.endDoChange│  │   relyonstructproject│
│ haos_adminorg           │  │   Op L113-122 写）        │  │   链式依赖）         │
│ （行政组织主表 · 当     │  │                           │  │                      │
│   roottype=1 时引用）   │  │ admin_org change log      │  │                      │
│                         │  │ （AdminOrgChangeLog-      │  │                      │
│                         │  │   Service.addAdminOrg-    │  │                      │
│                         │  │   ChangeLog L124-131）    │  │                      │
│                         │  │                           │  │                      │
│                         │  │ sch_task                  │  │                      │
│                         │  │ （AdminChangeMsgService.  │  │                      │
│                         │  │   handleChangeMsg L113-123│  │                      │
│                         │  │   JobClient.dispatch）    │  │                      │
└─────────────────────────┘  └───────────────────────────┘  └──────────────────────┘
                                               │
                                               ▼
                          ┌─ 业务下游消费方（推断）─────────────┐
                          │ · 组织视图重算服务（异步消费）      │
                          │ · 行政组织全名服务（OrgFullNameHelper│
                          │   .dispatchUpdateOrgFullNameJob L204│
                          │   异步派单）                        │
                          │ · ISV 自建合规审计 / 第三方系统     │
                          │   （CS-05 BEC 自建发布 + 订阅）     │
                          └─────────────────────────────────────┘
```

## 2. 上游 · 数据来源

### 2.1 业务方手动建方案（主路径）

- 入口：组织管理 / 行政组织维护 / 行政组织维护 / 矩阵组织设置 / 列表 / 【新增】
- opKey: `new` / `addnew_org`
- 调用链：StructProjectListPlugin.openOperationPage L120-122 → showStructProjectForm → StructProjectEditPlugin.afterCreateNewData / afterBindData / propertyChanged → save (7 OP 链)
- 数据流：见 [05_data_flow.md](05_data_flow.md) 第 1-2 节

### 2.2 列表导入（HIES）

- 入口：列表 / 【导入数据】
- opKey: `importdata_hr`
- 标品插件：`HRBaseDataImportEdit` + `HRHiesButtonSwitchPlugin`（HRMP HIES 模板）
- 数据格式：导入模板（`importtemplatelist` opKey 管理）
- 后续：导入数据走 save OP 链 · 同手动新建

### 2.3 标品系统预置

- `STRUCT_PROJECT_MANAGE` 是预置母本（issyspreset='1'）· 来自标品代码常量
- StructProjectListPlugin.setFilter L98-99 默认排预置但保留 STRUCT_PROJECT_MANAGE
- 业务方一般不修改预置母本 · PR-007 规定预置数据 number 不可改

### 2.4 跨表派生根组织时的"反向数据来源"

- 当 roottype=1（实组织根）时 · rootorg 字段引用 haos_adminorg 已有行政组织
- StructProjectSaveOp L115 调 `OrgRepository.loadCurrentVersionOrgDy(rootOrg.getLong("id"))` 加载现有行政组织
- 这种情况下 · "haos_adminorg" 是本场景的"上游数据源"

## 3. 直接派生（下游 · 同步）

### 3.1 haos_adminorgdetail（虚拟根组织）

| 派生条件 | 写入字段 | 反编译位置 |
|---|---|---|
| roottype=2 + 新建 | id（genLongId）, number, name, description, isvirtualorg=1, bsed, establishmentdate, otclassify=1010L, index=1, org, parentorg=null | StructProjectSaveOp L94-105 + L119-120 |
| roottype=2 + 修改 | number, name, description, bsed, establishmentdate（更新现有 dy）| StructProjectSaveOp L107-113 |
| roottype 切换（1→2 或 2→1）| 新建虚拟根（同上）+ 删旧根 | StructProjectSaveOp L141-145 + BatchOrgDeleteOpService 删 |

业务后果：派生数据进入 admin_org 域 · 行政组织树多了一个虚拟根节点

### 3.2 haos_adminorgstruct（OrgStruct 下挂关系）

| 派生条件 | 写入 | 反编译位置 |
|---|---|---|
| 新建方案 | 单条 OrgStruct（仅根组织 · parentorg=null）| BatchAdminOrgNewOpService.beforeBuildStructDy L181-192 + super.execute |
| 修改方案 + roottype 切换 + effdt 变 | 整批改写现有 OrgStruct（parentorg 旧根 → 新根）| StructProjectSaveOp L147-180 + OtherStructService.saveOtherStruct |
| 修改方案 + virtualAndNotChange + effdt 变 | reviseVersions（HisModel 时序版本）| StructProjectSaveOp L161-170 + HisModelAPIService.reviseVersions |

业务后果：方案下挂的所有组织层级跟随方案的 roottype + effdt 变化

### 3.3 haos_adminorg（roottype=1 实组织根）

| 派生条件 | 写入 | 反编译位置 |
|---|---|---|
| roottype=1 + 修改方案 | rootOrg.set("index", 1) + rootOrg.set("bsed", effDate) + rootOrg.set("org", structProject.org) + rootOrg.set("parentorg", null) | StructProjectSaveOp L115-118 |
| roottype 切换 1→2 | 删除旧实根（BatchOrgDeleteOpService）| StructProjectSaveOp L141-142 |

业务后果：方案的实组织根字段调整 · 反向影响 admin_org 域

## 4. 间接 / 异步派单（下游）

### 4.1 haos_orgteamcooprel（组织团队协作关系）

- **写入条件**：派生新根组织（含 BatchAdminOrgNewOpService.execute L93-94）
- **写入逻辑**：
  - 删除旧 timeline：`TimeLineHelper.deleteTimelineDys("haos_orgteamcooprel", oldCoopRelIds)`
  - 写新 timeline：`TimeLineHelper.saveTimelineDys("haos_orgteamcooprel", orgCoopRelDys)`
- **业务后果**：新根组织自动建立默认协作关系

### 4.2 haos_adminorg_msgdetail（变更消息表）

- **写入条件**：BatchAdminOrgNewOpService.endDoChangeOp（非 isAddTempTag 时）
- **写入逻辑**：
  - `AdminChangeMsgService.assembleMsgDy(null, dataEntity, null)` 构造消息 dy（assembleMsgDy L81-110）
  - `AdminChangeMsgService.saveAdminChangeMsg(msgDetailList)` 写入（L50-52）
- **写入字段**：bo, beforeversion, afterversion, isbelongcompanychange, traceid, changeoperate, changescene, sendstate='0', creator
- **业务后果**：消息表多一条记录 · 用于异步消费

### 4.3 admin_org change log

- **写入条件**：BatchAdminOrgNewOpService.endDoChangeOp（非 isAddTempTag · 非 future 时）
- **写入逻辑**：`adminOrgChangeLogService.addAdminOrgChangeLog(opData, oldOrgMap, fromAdd=true)`
- **业务后果**：变更日志表多一条记录 · 审计追溯用

### 4.4 sch_task 派单（异步）

- **派单条件**：BatchAdminOrgNewOpService.afterTransDoOp（非 batchApply / 非 importtype）L167-174
- **派单逻辑**：
  - `AdminChangeMsgService.handleChangeMsg()` L113-123
  - 查 sch_task 已有 JOB_ID="5+X/4Y=AOZ=O" 处于 SCHEDULED 状态时跳过
  - 否则：`JobClient.dispatch(jobInfo)` 派单到调度服务
- **业务后果**：异步任务消费 haos_adminorg_msgdetail · 通知下游

### 4.5 OrgFullNameHelper（异步全名服务）

- **派单条件**：BatchAdminOrgNewOpService.saveFullNameData L202-205（非空 adminOrgAddOrgBoIds）
- **派单逻辑**：`OrgFullNameHelper.dispatchUpdateOrgFullNameJob(adminOrgAddOrgBoIds)`
- **业务后果**：行政组织全名异步重算

## 5. 反向引用（被谁引）

### 5.1 haos_structure 实例

- 字段：`haos_structure.relyonstructproject` BasedataField → haos_structproject
- 业务流：用户在 haos_structure 表单选 relyonstructproject = 本方案 · 派生具体矩阵实例
- 当本方案变更时：
  - 修改 number/name → 实例显示同步变化（如显示了母本 name）
  - 修改 roottype → 整批改写实例下挂组织（CS-02 必加校验）
  - 删除 → 实例 relyonstructproject 引用悬挂（CS-04 必加校验）
- 跨表查询：`haos_structure.relyonstructproject IN (?)`

### 5.2 haos_structproject 自引用（链式依赖）

- 字段：`haos_structproject.relyonstructproject` BasedataField → haos_structproject（自引）
- 业务语义：方案 A 依赖方案 B（某个方案的字段范围 / 权限模板从另一个方案派生）
- 链式校验：删除方案 B 时也要查方案 A.relyonstructproject = B id（除查 haos_structure 外）

## 6. 跨域协作（admin_org 域）

### 6.1 共用 Service 类清单（来自反编译）

| Service | 文件 | 用途 |
|---|---|---|
| `BatchAdminOrgNewOpService` | BatchAdminOrgNewOpService.java 207 行 | 派生 / 修改根组织 |
| `BatchOrgDeleteOpService` | BatchOrgDeleteOpService.java 89 行 | 删除旧根组织 |
| `BatchOrgExecuteOpService` | BatchOrgExecuteOpService.java 42 行 | OP 执行包装壳 |
| `OrgChangeDetailService` | OrgChangeDetailService.java 591 行 | 组织变动详情 service · 26 reads / 18 writes |
| `AdminChangeMsgService` | AdminChangeMsgService.java 124 行 | 变更消息表 + sch_task 派单 |
| `AdminOrgChangeLogService` | AdminOrgChangeLogService.java 226 行 | 变更日志表 |

### 6.2 数据共享方向

- **本场景写 → admin_org 读**：派生根组织时本场景写 haos_adminorg · admin_org 域消费方读取
- **admin_org 写 → 本场景读**：rootorg 引用 haos_adminorg 时反查（StructProjectSaveOp L107 / 115）
- **共享消息表**：haos_adminorg_msgdetail 是 admin_org 域的消息表 · 本场景的派生根组织也写
- **共享调度任务**：sch_task JOB_ID="5+X/4Y=AOZ=O" 是 admin_org 域的全局任务 · 本场景共用

### 6.3 升级影响

- admin_org 域 service 签名变化（如 BatchAdminOrgNewOpService 构造函数）→ 本场景 StructProjectSaveOp 编译失败
- admin_org 域消息表结构变化（如 haos_adminorg_msgdetail 加字段）→ 影响订阅方反序列化

## 7. 配套场景 haos_structure 的协作流

```
[本场景 haos_structproject]              [配套场景 haos_structure]

T1: 业务方建方案（new + save）
   · 派生根组织 + sch_task 派单 + write change log
   · enable → enable=1 · 方案可被引用

                                        T2: 业务方建实例（new + save）
                                          · 选 relyonstructproject = 本方案
                                          · 在 t_haos_structproject 同表写入
                                            (otclassify=1010L · 实例标识)
                                          · 跨域调 hrcs.getUserStructProjectsF7
                                            校验权限

                                        T3: 用户【维护架构】跳到 haos_orgstructlist
                                          · 在矩阵下挂具体行政组织
                                          · 写 haos_adminorgstruct 表

T4: 业务方修改方案（modify + save）
   · roottype 切换会改写 T3 下挂的层级（CS-02）
   · 标品没校验 → ISV 必加

T5: 业务方禁用方案（disable）
   · enable 1 → 0
   · 标品没级联禁用实例 → ISV 走 CS-05 BEC 通知

                                        T6: 实例侧 setFilter `enable in [1, 0]`
                                          · 禁用方案的实例仍可见
                                          · 业务方需手动处理（按业务策略）

T7: 业务方删除方案（delete + delete_project）
   · 标品仅 enable=10 行参与删除
   · 标品没反向引用校验 → ISV 必加 CS-04

                                        T8: 如果方案被删（违反 CS-04）：
                                          · 实例 relyonstructproject 引用悬挂
                                          · F7 显示空 / 报错
```

## 8. ISV 业务事件订阅入口（CS-05 详证）

业务方实施 CS-05（BEC 自建发布）后 · 订阅链路：

```
StructProjectChangedPublishOp.afterExecuteOperationTransaction
  · 主事务已提交（PR-010 · 第 9 阶段）
  · IEventService.triggerEventSubscribeJobs("haos", "${ISV_FLAG}_haos_structproject_changed",
                                            payload, null)
        ↓
业务事件中心
  · eventNumber=${ISV_FLAG}_haos_structproject_changed
  · 派发到所有订阅方
        ↓
StructProjectChangedNotifyConsumer.handleEvent(KDBizEvent evt)
  · 反序列化 payload
  · 按 id 自查具体变更内容（PR-011 · variables 不放 DynamicObject）
  · 触发业务通知（合规审计 / 第三方系统 / 派生实例服务）
```

## 9. 真实派生实例的协作模式

```
[本场景 haos_structproject]           [配套场景 haos_structure]

  方案 "销售域" (roottype=2)           ┌─ 实例 "华南销售矩阵"
  ├─ rootorg → 虚拟根 V1              │   relyonstructproject → 销售域方案
  ├─ otclassify ≠ 1010                │   rootorg → 华南区行政组织
  │                                   │
  │  保存方案触发派生流程              │   保存实例（仅写 t_haos_structproject 同表）
  │   ├─ 创建虚拟根 V1                │   · otclassify=1010L
  │   ├─ 写 OrgStruct（V1 → null）     │
  │   ├─ 写 OrgTeamCoopRel             │  → 用户【维护架构】
  │   ├─ 写 haos_adminorg_msgdetail   │     跳到 haos_orgstructlist + haos_structorgdetail
  │   └─ 派 sch_task                   │
                                       └─ 用户在该实例下挂华南各部门
                                           · 写 t_haos_adminorgstruct
                                           · parentorg 关联 V1 (虚拟根)

  ↓ 后续修改方案 effdt（不切换 roottype）
  · StructProjectSaveOp 走 "同 roottype + 不同 effdt" 分支
  · virtualAndNotChange=true 时走 reviseVersions
  · 实例下挂的 OrgStruct startdate 同步更新

  ↓ 修改方案 roottype 1→2（已派生实例）
  · StructProjectSaveOp 走 "非同 roottype + 非同 effdt" 分支
  · OtherStructService.saveOtherStruct(setDeleteRoot=true)
  · 实例下挂的整树被改写 · 旧根删除
  · 业务故障预警：CS-02 必加校验
```

## 10. 跨模块影响矩阵

| 下游模块 | 联动点 | 同步/异步 | 失败策略 | 业务影响 |
|---|---|---|---|---|
| **admin_org**（行政组织域）| 派生 haos_adminorgdetail / 修改 haos_adminorg / 写 OrgStruct | 同步（在主事务内）| 主事务回滚 | 行政组织树跟随方案变化 |
| **haos_structure**（配套实例）| relyonstructproject 引本方案 | （无主动联动 · 业务方自处理）| - | 实例跟随方案变更（CS-02 / CS-04 / CS-05）|
| **haos_orgstructlist + haos_structorgdetail**（下挂组织）| 改写 OrgStruct 时影响实例下挂层级 | 同步 | 主事务回滚 | 实例下挂被改写 · 业务方观感故障 |
| **OrgFullNameHelper**（全名服务）| dispatchUpdateOrgFullNameJob | 异步 | 不阻塞主事务 | 全名延迟更新 |
| **OrgUnitHelper**（组织单元服务）| syncOrgUnit / syncOrgChgUnit | 同步（afterTransDoOp 内）| LOG · 不阻塞 | 组织单元延迟同步 |
| **变更消息表**（haos_adminorg_msgdetail）| 写消息表 + sch_task 派单 | 同步写表 + 异步派单 | 派单失败 LOG | 异步消费方延迟收到 |
| **OPM 审批中心**（如业务方接入）| 不联动（基础资料不天然走审批）| - | - | 业务方自配 OPM |
| **hrcs（HR 跨域权限）**| 列表查询时跨域调（仅 haos_structure 列表用 · 本场景列表不用）| 同步 RPC | 三重判空兜底（haos_structure CS-08）| 列表查询延迟 |

## 11. 失败回滚 / 补偿策略

### 11.1 主事务失败

- **触发位置**：StructProjectSaveOp.beginOperationTransaction L77-82 / saveStructProjectAndRootOrg L84-181 抛异常
- **回滚范围**：方案主表 + 派生根组织 + OrgStruct + OrgTeamCoopRel · 全部撤销
- **用户感知**：表单显示错误 · 数据未保存

### 11.2 异步派单失败

- **触发位置**：AdminChangeMsgService.handleChangeMsg L113-123 抛异常
- **行为**：LOG.info(dispatch) · 不阻塞主事务（异步独立）
- **用户感知**：方案保存成功 · 但下游异步消费可能延迟
- **补偿**：业务方监控 sch_task 失败任务 · 手动重派或修复

### 11.3 BEC 发布失败（CS-05 ISV 自建）

- **触发位置**：StructProjectChangedPublishOp.afterExecuteOperationTransaction（ISV 代码）
- **行为**：try-catch · LOG.error · 不抛出（订阅方独立事务）
- **用户感知**：方案保存成功 · 订阅方未收到事件
- **补偿**：业务方监控 BEC 失败 · 手动重发 / 业务事件中心查 EventLog

## 12. ISV 与配套场景 haos_structure 的协作 checklist

实施 ISV 扩展时按以下顺序确认：

- [ ] 是否需要跨表查询 haos_structure（CS-02 / CS-04 用反向引用查询）
- [ ] 是否需要跟 haos_structure 的 setFilter 协调（避免重复过滤）
- [ ] 是否需要跟 haos_structure 的扩展点协调（如 ISV 在两边都加 propertyChanged · 业务语义独立）
- [ ] 是否需要业务事件中心配 eventNumber + 订阅方监听（CS-05）
- [ ] 是否需要在 ISV 自建权限模板基础资料（${ISV_FLAG}_permtpl）中跟 haos_structure 联动

## 13. 来源追溯

- 派生路径：StructProjectSaveOp.saveStructProjectAndRootOrg L84-181
- 跨表写：BatchAdminOrgNewOpService.execute L80-96 + endDoChangeOp L98-104 + afterTransDoOp L133-175
- 派单链：AdminChangeMsgService.handleChangeMsg L113-123 + saveAdminChangeMsg L50-52 + assembleMsgDy L81-110
- 反向引用：haos_structure.relyonstructproject 字段（详见 [`../haos_structure_maintenance/03_model_design.md`](../haos_structure_maintenance/03_model_design.md) 第 3.5 节）
- 自引用：scene_doc.json fields[24].refEntity = haos_structproject
- 共享 service 清单：_auto_plugin_semantics.md 14 反编译类 + admin_org 域共用类

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
