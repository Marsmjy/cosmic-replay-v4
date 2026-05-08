# 数据流转 · 组织调整申请单

> **状态**: 🟢 基于 8 类 Java 反编译实证 · 跨表写入路径完整梳理
> **confidence**: verified
> **数据源**: `OrgBatchChgBillEffectOp.java` / `OrgBatchBillSaveOp.java` / `AdminChangeMsgService.java` / `OrgBatchBillSubmitOp.java` / `OrgBatchBillSubmitAndEffectiveOp.java` 反编译

---

## 一、数据流总览图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│  [用户 UI · BillView]                                                        │
│       │                                                                     │
│       │ FormPlugin · AdminOrgBatchBillPlugin                                │
│       │ getModel().setValue / propertyChanged 联动                            │
│       ▼                                                                     │
│  [DataEntity 内存对象 · 7 entry collection]                                  │
│       │                                                                     │
│       │ save / submit / submiteffect 触发 OP 链                               │
│       ▼                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │ 第 1 阶段 · OP beginOperationTransaction (save)                        │  │
│  │                                                                      │  │
│  │  OrgBatchBillSaveHelper.orgBatchBillSave(dataEntity)                  │  │
│  │  ├─ INSERT t_homs_orgchgbill   (主表)                                  │  │
│  │  ├─ INSERT t_homs_orgchgbill_l (主表多语言)                            │  │
│  │  ├─ DELETE t_homs_orgchgbillentry WHERE billid = X                   │  │
│  │  └─ INSERT t_homs_orgchgbillentry × N（7 entry 全部按 changetype 区分写）│  │
│  │     INSERT t_homs_orgchgbillentry_l × N                              │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│       │                                                                     │
│       │  submit 时不走生效逻辑（走审批）                                       │
│       │  submiteffect 或 audit 触发                                          │
│       ▼                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │ 第 2 阶段 · OP beginOperationTransaction (submiteffect / audit)        │  │
│  │                                                                      │  │
│  │  OrgBillBatchEffectService.batchEffect(bills)                        │  │
│  │  ├─ 按 changetype 分发 6 大策略                                         │  │
│  │  ├─ Strategy(add)    → INSERT haos_adminorg + haos_adminorghis        │  │
│  │  ├─ Strategy(parent) → UPDATE haos_adminorg.parentorg + 创新版本        │  │
│  │  ├─ Strategy(info)   → UPDATE haos_adminorg + 创新版本                  │  │
│  │  ├─ Strategy(disable)→ UPDATE haos_adminorg.enable=0                  │  │
│  │  ├─ Strategy(merge)  → 多组织合并到目标 + 各自 disable                   │  │
│  │  ├─ Strategy(split)  → 源组织 disable + 拆出多新组织 INSERT              │  │
│  │  └─ 维护 boid / id / sourcevid / iscurrentversion                      │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│       │                                                                     │
│       ▼                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │ 第 3 阶段 · OP afterExecuteOperationTransaction（事务已提交）           │  │
│  │                                                                      │  │
│  │  AdminChangeMsgService.handleChangeMsg()                             │  │
│  │  ├─ 查 sch_task 是否已有 SCHEDULED 任务                                │  │
│  │  ├─ 若无 · 通过 JobClient.dispatch(JobInfo)                           │  │
│  │  │   JOB_ID="5+X/4Y=AOZ=O"  SCHEDULE_ID="5+X/=KD8ZXFW"                 │  │
│  │  └─ 任务异步消费 · 调 EventServiceHelper.triggerEventSubscribeJobs     │  │
│  │      └─ 派发 BEC 业务事件 · 通知所有订阅方                              │  │
│  │                                                                      │  │
│  │  billBatchEffectService.afterBatchEffect(bills)                      │  │
│  │  └─ 触发缓存刷新 · 清理临时数据                                          │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│       │                                                                     │
│       ▼                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │ 第 4 阶段 · OP endOperationTransaction（最后修复）                      │  │
│  │                                                                      │  │
│  │  ├─ DELETE t_homs_orgchgbillentry WHERE creator=0 AND billid IN X     │  │
│  │  ├─ 加载所有 entry · 按 changetype 分组                                 │  │
│  │  ├─ for entry in addOrgEntries:                                       │  │
│  │  │     boid = entry.adminorg.id                                       │  │
│  │  │     vid  = entry.adminorg.sourcevid                                │  │
│  │  │     map.put(boid, vid)                                             │  │
│  │  ├─ for entry in 所有 entry:                                          │  │
│  │  │     if entry.parentorg.id ∈ map.keys: entry.parentorg = map[id]    │  │
│  │  │     if entry.adminorg.id ∈ map.keys: entry.adminorg = map[id]      │  │
│  │  └─ batchOrgEntityHelper.save(updatedEntries)                         │  │
│  │     （把 add entry 创建的临时 boid 替换成真实 vid · 保持引用一致）        │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、写入对象清单（按时序）

### 阶段 1：save 写入（4 张表 · 1 个事务）

| 表 | 操作 | 关键字段 | 实证 |
|---|---|---|---|
| `t_homs_orgchgbill` | INSERT/UPDATE | fbillno, fbillstatus, forgid, feffdt, fauditstatus | OrgBatchBillSaveHelper.orgBatchBillSave |
| `t_homs_orgchgbill_l` | INSERT/UPDATE | fdescription, fdispatchname | 同上 |
| `t_homs_orgchgbillentry` | DELETE+INSERT | fchangetypeid, fadminorgid, fparentorgid, fnumber | 同上 |
| `t_homs_orgchgbillentry_l` | DELETE+INSERT | fname, fsimplename | 同上 |

⚠️ **DELETE+INSERT 模式**：分录每次 save 都是 "先删除旧 entry · 再 insert 新 entry"。这是单据类常见模式 · 但要注意：
- 自定义字段值在每次 save 后如果不在 entry 实体里 · 会**丢失**
- 不要依赖 entry 的 sequence 顺序保持稳定

### 阶段 2：submit 增量写入（无新表 · 仅 update）

| 表 | 操作 | 关键字段 | 实证 |
|---|---|---|---|
| `t_homs_orgchgbill` | UPDATE | `fbillstatus = "B"` | OrgBatchBillSubmitOp.java:36 |
| `t_homs_orgchgbillentry` | UPDATE | beforechgvid / afterchgvid / bsed | OrgBatchBillSubmitOp.java:43-44 + L53 |

注意 `bsed` 字段写在 entry 上 · 不是单据上：
```java
// OrgBatchBillSubmitOp.java:53
Arrays.stream(billEntries).forEach(dy -> dy.set("bsed", (Object)effdt));
```

### 阶段 3：audit / submiteffect 写入下游（**真正落 haos_adminorg**）

| 表 | 操作 | 触发条件 | 实证 |
|---|---|---|---|
| `t_haos_adminorg` 或 `_b/_h/_l` | INSERT/UPDATE | add/parent/info/disable/merge/split entry | OrgBillBatchEffectService.batchEffect |
| `t_haos_adminorghis` | INSERT | 任何变更（保留历史版本） | 同上 |
| `t_haos_adminorgdetail` | UPDATE/INSERT | 任何变更（详情视图同步） | OrgBatchBillSubmitAndEffectiveOp.java:84（haos_adminorgdetail copy 实证） |
| `t_haos_adminorg_msgdetail` | INSERT | 每条变更都生成 1 条变动消息 | AdminChangeMsgService.java:48-52 |
| `t_homs_orgchgbill` | UPDATE | `fbillstatus = "C"` + `fauditstatus = ...` | wf 引擎更新 |

### 阶段 4：异步派发链（事件驱动）

| 表 | 操作 | 触发条件 | 实证 |
|---|---|---|---|
| `t_sch_task` | INSERT | handleChangeMsg 派单 | AdminChangeMsgService.java:113-123 |
| `t_haos_adminorg_msgdetail` | UPDATE | 任务消费时回写 sendstate | AdminChangeMsgService.java:108 |
| BEC 事件队列 | publish | sch_task 消费时调 EventServiceHelper | 见 admin_org `04_business_flow.md` 标品发布方实证 |

---

## 三、`OrgBatchChgBillEffectOp` 反编译实证（最关键 OP）

来源：`OrgBatchChgBillEffectOp.java`（132 行 · `HRDataBaseOp` 子类）

### 字段读写明细（来自 `_auto_plugin_semantics.md`）

```
读字段 (6 个): adminorg, adminorg.id, changetype.id, id, parentorg.id, sourcevid
写字段 (2 个): adminorg, parentorg
QFilter 查询 (3): creator=, billid in, billid in
super 调用: beginOperationTransaction(), endOperationTransaction(), onAddValidators()
```

### 引用的业务类（2 个 Service）

```
new OrgBillBatchEffectService()   // 真正的批量生效编排 service · 内部分发到 6 大策略
new AdminChangeMsgService()        // 派单 + 异步派 BEC 事件 · 124 行实证
```

### 5 个生命周期方法

```
onPreparePropertys        (L57-60)   声明读 effdt
onAddValidators           (L62-64)   仅 super
beginOperationTransaction (L66-74)   ⭐ 调用 batchEffect 写 haos_adminorg 全家
afterExecuteOperationTransaction (L76-83) ⭐ 调 handleChangeMsg + afterBatchEffect
endOperationTransaction   (L85-131)  清脏 + boid→vid 替换修复
```

---

## 四、boid / id / sourcevid 三件套（关键概念）

`haos_adminorg` 是 HisModel 时序资料 · 每个组织有：
- `boid` — 业务对象 ID（同一组织所有版本共用）
- `id` — 当前版本 ID（每次变更产生新值）
- `sourcevid` — 当前版本指向的源版本 ID（用于追溯版本树）
- `iscurrentversion` — 是否当前版本（同一 boid 只有 1 条记录是 true）

⚠️ **`homs_orgbatchchgbill` 的 entry 引用组织 时**：
- `entryentity_*.adminorg` 字段是 `HRAdminOrgField` 类型 · 引用 `haos_adminorghrf7`（视图）
- 实际查询时 · UI 给的是 **boid**（业务维度）· 不是 id
- 所以反编译里看到的 `dy.getLong("adminorg.boid")` 是真实查询用的 ID（如 `OrgBatchBillSubmitOp.java:39`）

⚠️ **add entry 特殊**：
- 用户填 add 分录时 · 还没真创建组织 · 所以 add entry 的 `adminorg` 是**临时 ID**
- 生效后真组织被创建 · `haos_adminorg.boid` / `id` / `sourcevid` 才有真值
- `OrgBatchChgBillEffectOp.endOperationTransaction` L102-128 实证：把 add entry 的临时 boid **替换**成真实 sourcevid · 修复其他 entry 引用

---

## 五、跨表写入的事务边界（PR-010 实证）

```
事务 1: save 操作（OrgBatchBillSaveOp）
  ├── beginOperationTransaction
  ├── 写 4 张本单表
  └── endOperationTransaction · 清脏

事务 2: submit 操作（OrgBatchBillSubmitOp）
  ├── beginOperationTransaction
  │   ├── 写 billstatus = B
  │   └── 写 entry 的 vid 字段
  └── afterExecuteOperationTransaction · 写 entry.bsed = effdt
  （注意：afterExec 在事务提交后 · 单独一个小写入）

事务 3: audit/submiteffect 操作（OrgBatchChgBillEffectOp）
  ├── beginOperationTransaction
  │   └── billBatchEffectService.batchEffect(bills)
  │       ├── 写 haos_adminorg（多版本时序写入）
  │       ├── 写 haos_adminorghis（历史表）
  │       ├── 写 haos_adminorgdetail（详情视图）
  │       └── 写 haos_adminorg_msgdetail（变动消息明细）
  ├── afterExecuteOperationTransaction（事务已提交 · 不能再写主数据 · PR-010）
  │   ├── handleChangeMsg() · 派 sch_task（独立异步事务）
  │   └── billBatchEffectService.afterBatchEffect()
  └── endOperationTransaction
      ├── 清 batchorgentity 脏数据
      └── 修复 add boid → sourcevid 引用
```

⚠️ **PR-010 关键点**：
- `beginOperationTransaction` / `endOperationTransaction` 在事务内
- `afterExecuteOperationTransaction` **在事务提交后** · 此处发事件**安全**
- `AdminChangeMsgService.handleChangeMsg` 调用位置在 **afterExecute**（事务提交后），符合 PR-010 + PR-011

---

## 六、`AdminChangeMsgService` 异步派发机制（124 行实证）

来源：`AdminChangeMsgService.java`（kd.hr.haos.business.domain.org.service · 标品发布方）

### 关键常量

```java
private static final String JOB_ID = "5+X/4Y=AOZ=O";
private static final String SCHEDULE_ID = "5+X/=KD8ZXFW";
private static final HRBaseServiceHelper helper = new HRBaseServiceHelper("haos_adminorg_msgdetail");
```

### 派单逻辑（L113-123）

```java
public void handleChangeMsg() {
    HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("sch_task");
    DynamicObject task = serviceHelper.queryOriginalOne("id",
        new QFilter[]{new QFilter("job.id", "=", JOB_ID),
                      new QFilter("status", "=", "SCHEDULED")});
    if (task != null) {
        return;  // 已经有调度中的任务 · 不重复派
    }
    JobInfo jobInfo = ScheduleService.getInstance().getObjectFactory()
                          .getJobDao().get(JOB_ID);
    jobInfo.setScheduleId(SCHEDULE_ID);
    String dispatch = JobClient.dispatch(jobInfo);
    LOGGER.info(dispatch);
}
```

→ **不是同步发 BEC 事件**！而是查 `sch_task` 表 · 看是否有 SCHEDULED 任务 · 没有就派单 · 让独立线程异步消费 · 消费时再调 `EventServiceHelper.triggerEventSubscribeJobs`。

### 单条变动消息明细（L58-74 · saveChgAdminOrgMsgDetail）

每条 entry 生效时 · 生成 1 条 `haos_adminorg_msgdetail` 记录 · 包含：

| 字段 | 来源 | 含义 |
|---|---|---|
| `bo` | afterChgDy.boid | 变更的组织 boid |
| `beforeversion` | sourceVidMap[oldOrg.boid] | 变更前版本 |
| `afterversion` | afterChgDy.sourcevid 或 id | 变更后版本 |
| `changescene` | afterChgDy.changescene | 变动场景 |
| `changeoperate` | changescene.changeoperat[0] | 变动操作类型 |
| `isbelongcompanychange` | 计算 belongcompany 是否变化 | 法律实体是否变 |
| `traceid` | RequestContext.getTraceId | 追溯 ID |
| `sendstate` | "0" | 派发状态（待派发） |
| `creator` | RequestContext.currUserId | 创建人 |

→ 消费方根据 `changeoperate` 字段路由到不同处理（如新增/调上级/合并 各自走不同业务流）。

### 跟 hjm 的 BEC 3 层异步对比

| 维度 | hjm_jobhr (BEC 直发) | homs_orgbatchchgbill (本场景 · sch_task 异步) |
|---|---|---|
| 发布触发位置 | OP afterExecuteOperationTransaction | OP afterExecuteOperationTransaction（同位置） |
| 派发方式 | 直接调 IEventService.triggerEventSubscribeJobs | 先派 sch_task · 异步线程再派 |
| 重复发防护 | 无 · 每次都发 | 查 sch_task 是否 SCHEDULED · 已有就跳过 |
| 业务 payload | 在 KDBizEvent.source JSON | 写到 haos_adminorg_msgdetail 表 · 异步线程读表派 |
| 优点 | 简单直接 | 解耦 + 削峰 + 持久化重试 |
| 缺点 | 主事务前不能再有错（已 send） | 链路长 · 调试时要看 sch_task 表 |

---

## 七、对下游业务的写入（21 张引用 haos_adminorghrf7 的表）

参考 `knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.haos_adminorghrf7` 段（17 hrpi_* + 4 其他）：

```
hrpi_empjobrel.adminorg          员工任职关系（核心）
hrpi_empposorgrel.adminorg       人-岗-组关系
hrpi_rotationinfo.adminorg       轮岗信息
hrpi_dispatchinfo.adminorg       派遣信息
hrpi_secondment.adminorg         借调记录
... 共 17+ 张 hrpi_* 表
```

⚠️ **`homs_orgbatchchgbill` 生效后**：
- ❌ **不会**直接联级更新这些 hrpi_* 表
- ✅ 通过 `haos_adminorg_msgdetail` + `sch_task` 异步消费 BEC 事件 · 由订阅方（如 hrpi_empjobrel 同步插件）自行处理

→ 这是 ISV 扩展的最大机会点（CS-05 BEC 订阅方）：可以订阅 `haos_adminorg_msgdetail` 派发的事件 · 在订阅方业务逻辑里同步联动其他业务 · 不挂任何 OP 不影响主事务（PR-001 + PR-011）。

---

**📌 来源追溯**：

- 4 张本单表写入：`OrgBatchBillSaveOp.java:46-62` + `_shared/_standard_metadata/entity_metadata/homs_orgbatchchgbill.md` 物理表元数据
- billstatus 状态机写入：
  - `OrgBatchBillSubmitOp.java:36` → "B"
  - `AdminOrgBatchBillListPlugin.java:358` → "F"
- entry.bsed 写入：`OrgBatchBillSubmitOp.java:53`
- effect 写下游：`OrgBatchChgBillEffectOp.java:66-83`（batchEffect + afterBatchEffect）
- haos_adminorgdetail 写入：`OrgBatchBillSubmitAndEffectiveOp.java:84-90`（实证 setMainOrg）
- 异步派发：`AdminChangeMsgService.java:113-123`（JOB_ID 实证）
- haos_adminorg_msgdetail 写入：`AdminChangeMsgService.java:50-52` + `assembleMsgDy` L81-111
- boid→vid 修复：`OrgBatchChgBillEffectOp.java:97-128`
