# 变更影响面 · 组织调整申请单

> **状态**: 🟢 基于反编译 + refentity_reverse + sch_task 异步链路实证
> **confidence**: verified
> **数据源**: `OrgBatchChgBillEffectOp.java` / `AdminChangeMsgService.java` / `refentity_reverse.json`

---

## 一、改本场景会影响什么（一图看懂）

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  ISV 改 homs_orgbatchchgbill 影响面                           │
│                                                                             │
│   改什么？             直接影响                       间接影响（异步）         │
│   ────────             ──────────                  ─────────────────         │
│                                                                             │
│   加 entry 字段        本单 4 张物理表新增列         无                       │
│                                                                             │
│   加 save Validator    save / submit / submiteffect  无                      │
│                       链增加校验失败概率                                       │
│                                                                             │
│   加 submit Validator  submit / submiteffect 链      无                      │
│                                                                             │
│   加 audit afterExec   audit / submiteffect 拖时长   外系事件可能晚到         │
│                       (主事务后)                     （CS-05 BEC 受影响）      │
│                                                                             │
│   加 BEC 订阅方        无主事务影响                  BEC 队列消费方多 1       │
│                       (独立事务)                     (PR-011 解耦)            │
│                                                                             │
│   改 F7 过滤           本表单 F7 行为变              无                       │
│                                                                             │
│   字段联动（CS-03）    本表单 propertyChanged 链      无                      │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、生效后下游写入实证（haos_adminorg 主数据 4 张表）

来源：`OrgBatchChgBillEffectOp.java:66-83` + `OrgBillBatchEffectService.batchEffect`（标品内部）

```
homs_orgbatchchgbill audit/submiteffect
  │
  ├──► t_haos_adminorg              主表 · INSERT/UPDATE 多版本
  │    ├─ 多版本时序：每个 boid 多个 id（不同 effdt 的版本）
  │    ├─ 字段：number / name / parentorg / boid / sourcevid / iscurrentversion
  │    └─ 维护：每次变更产生新版本 · 旧版本 iscurrentversion=false · 新版本=true
  │
  ├──► t_haos_adminorghis           历史表 · INSERT
  │    └─ 全量保留每次变更的快照
  │
  ├──► t_haos_adminorgdetail        详情视图 · INSERT/UPDATE
  │    └─ 跟主表同步 · UI 列表通过它查询
  │
  └──► t_haos_adminorg_msgdetail    变动消息明细 · INSERT
       └─ AdminChangeMsgService.assembleMsgDy 写入（L81-111）
       字段：bo, beforeversion, afterversion, changescene,
             changeoperate, isbelongcompanychange, traceid, sendstate, creator
```

---

## 三、生效后异步派发链（事件驱动 · 21+ 张下游表）

### 3.1 sch_task → BEC 链路

```
afterExecuteOperationTransaction（事务已提交）
        │
        ▼
AdminChangeMsgService.handleChangeMsg (L113-123)
        │
        ▼
查 sch_task 是否 SCHEDULED
        │
        ├── 是 → return（已派 · 不重发）
        │
        └── 否 → JobClient.dispatch(JobInfo)
                JOB_ID = "5+X/4Y=AOZ=O"
                SCHEDULE_ID = "5+X/=KD8ZXFW"
                       │
                       ▼
            异步消费方：调 EventServiceHelper.triggerEventSubscribeJobs
                       │
                       ▼
            BEC 派发"行政组织变更"事件
                       │
                       ▼
            订阅方独立消费（事务隔离）
```

### 3.2 BEC 订阅方影响清单（17+ 张 hrpi_* 表）

来源：`knowledge/workbench/_indexes/refentity_reverse.json` 的 `refs.haos_adminorghrf7` 段

| 下游表 | 引用字段 | 业务含义 | 影响 |
|---|---|---|---|
| `hrpi_empjobrel` | `adminorg` | 员工任职关系（核心） | 组织变动 → 员工任职归属变 |
| `hrpi_empposorgrel` | `adminorg` | 人-岗-组关系 | 同 |
| `hrpi_rotationinfo` | `adminorg` | 轮岗信息 | 同 |
| `hrpi_dispatchinfo` | `adminorg` | 派遣信息 | 同 |
| `hrpi_secondment` | `adminorg` | 借调记录 | 同 |
| `hrpi_currentposthtml` | `adminorg` | 当前岗位 HTML | UI 展示需刷新 |
| `hrpi_emergencycontact` | `adminorg` | 紧急联系人 | 弱依赖 |
| `hrpi_resigncrtcadre` | `adminorg` | 离职创建干部 | 历史保留 |
| `hrpi_termjobchgrec` | `adminorg` | 离职变更记录 | 历史保留 |
| `hrpi_*` | `adminorg` | 其他 7+ 张 hrpi_* 表 | 同模式 |
| `hbpm_positionhr` | `adminorg` | 岗位（核心） | 组织变 → 岗位归属变 |
| `hbjm_jobhr` | `adminorg` (间接) | 职位（核心） | 弱依赖 |

### 3.3 标品订阅方实证

`AdminChangeMsgService` 派的 BEC 事件 · 标品里就有以下订阅方（不脑补 · 走"行政组织变更"事件号）：
- `hrpi_empjobrel` 同步插件（员工任职关系自动跟随组织调整）
- 组织树缓存刷新插件
- 数据权限刷新插件

⚠️ **ISV 加 BEC 订阅方时**：
- 订阅 = 加 1 个独立消费方 · 不影响标品订阅方
- 多订阅方独立事务 · 互不影响（PR-011）
- 同一事件多订阅方按订阅注册顺序消费

---

## 四、改本场景元数据的影响传播

### 4.1 加 add entry 字段（CS-01）

| 影响维度 | 是否影响 | 说明 |
|---|---|---|
| 物理表 t_homs_orgchgbillentry | ✅ ALTER TABLE 加列 | PDM 同步生成 SQL |
| haos_adminorg 主表 | ❌ 不影响 | 申请单字段不会自动同步到主表 |
| BEC 事件 payload | ❌ 不会自动加入 | 标品 `assembleMsgDy` 没读 ISV 字段 |
| 订阅方 | ❌ 收不到 ISV 字段 | 订阅方按 boid 自己回查 · 但回查的是 haos_adminorg · 不是申请单 |
| 列表展示 | ⚠️ 需配列表列 | 默认不展示 · 业务侧配列表列才出现 |
| 导出导入 | ✅ HIES 自动支持 | 字段加进去后 · 标品导入导出自动识别 |

⚠️ **关键认知**：本场景的 entry 字段**不会**自动传播到 haos_adminorg。如果业务想让 ISV 字段最终落到主数据，需要在 CS-01 同时给 haos_adminorg 也加同名字段（admin_org CS-01 + CS-02 模式）。

### 4.2 加 save Validator（CS-02）

| 影响 | 程度 |
|---|---|
| save 链耗时 | 增加（每次 save 多查 1 次 haos_adminorg） |
| submit 链耗时 | 增加（submit 复用 save Validator） |
| submiteffect 链耗时 | 增加（同上） |
| 用户体验 | 报错更精准 + 阻断时机更早 |
| 误报风险 | 中（本租户外的相同编码会误报 · 需要带 BU 维度） |

### 4.3 加 audit afterExec（CS-06）

| 影响 | 程度 |
|---|---|
| audit / submiteffect 操作时长 | 增加（外系调用时间） |
| 同步外系失败的可能 | 中（网络 / 外系故障） |
| 主事务可回滚性 | ❌ 不可（已 commit） |
| 用户感知 | 操作完成提示晚 |

⚠️ 如果业务体量大（一次审批 100+ entry）· 建议改 CS-05 BEC 订阅方异步处理。

### 4.4 加 BEC 订阅方（CS-05）

| 影响 | 程度 |
|---|---|
| 主事务 | 0（订阅独立） |
| 订阅消费延迟 | 秒级（sch_task 派单 + BEC 队列） |
| 失败影响 | 不影响主流程（订阅独立事务） |
| 重试 | BEC 自动重试 + ISV 自建幂等 |

→ 几乎无副作用 · 推荐优先选。

---

## 五、影响下游 hrpi_empjobrel 的真实链路（核心）

`homs_orgbatchchgbill` 生效**不直接**写 `hrpi_empjobrel` · 走异步：

```
homs_orgbatchchgbill audit
        │
        ▼
OrgBatchChgBillEffectOp.beginExec
        │
        ▼
billBatchEffectService.batchEffect()
        │
        ▼
[标品策略：parent / merge / split 涉及上级变化]
        │
        ▼
更新 haos_adminorg.parentorg + 创新版本（旧版本 iscurrentversion=false）
        │
        ▼
OrgBatchChgBillEffectOp.afterExec
        │
        ├──► AdminChangeMsgService.handleChangeMsg → 派 sch_task
        │           │
        │           ▼
        │   异步消费 → BEC 发"行政组织变更"事件
        │           │
        │           ▼
        │   [标品订阅方] hrpi_empjobrel 同步插件
        │           │
        │           ▼
        │   按 boid 查 hrpi_empjobrel 当前版本
        │   按新 adminorg 版本反推任职关系（业务规则）
        │   写新 hrpi_empjobrel 版本
        │           │
        │           ▼
        │   原任职关系 iscurrentversion=false
        │   新任职关系 iscurrentversion=true
        │
        └──► billBatchEffectService.afterBatchEffect
                    │
                    ▼
            清理临时表 + 刷新缓存
```

**ISV 扩展时要注意**：
- 改 `homs_orgbatchchgbill` 的 entry 字段 → **不会自动**传到 hrpi_empjobrel（除非 ISV 自己在订阅方写逻辑）
- 监听 `homs_orgbatchchgbill` 生效事件想拿到员工任职变化 → 走 BEC 订阅方（CS-05）· 自己回查 `hrpi_empjobrel`

---

## 六、隐式风险点（容易踩的坑）

### 6.1 改 F7 过滤可能导致选不到组织

如果 ISV 给 `parent_adminorg` 等字段加自定义 QFilter · 但条件太严 · 用户会发现"以前能选的组织现在选不到"。**复测覆盖**：
- 普通 BU 操作员工号
- 跨 BU 全局管理员
- 已停用组织选择
- 虚拟组织选择
- 时态过滤（过去 / 当前 / 未来组织）

### 6.2 改 save Validator 让历史单据无法保存

ISV 加新校验后 · A 状态的旧单据再 save 会触发新校验 · 老数据可能不符合新规则。**缓解**：
- Validator 区分"新建" vs "已存在"（按 `dataEntity.getLong("id") == 0L` 判断）
- 仅对新建做强校 · 历史单据放过 · 由用户手动整改

### 6.3 BEC 订阅方处理慢导致积压

订阅方处理一个事件耗时 5s · 一次大型组织调整产生 100 条变动 · 队列处理需要 500s · 期间用户等待外系数据更新。**缓解**：
- 订阅方处理逻辑要短 · 长任务转后台
- 大批量场景 · BEC 队列要监控 · 配阈值告警

### 6.4 改 audit afterExec 让 wf 工作中心 timeout

wf 工作流引擎调审批通过 · 触发 audit · 等 OP 链全跑完才返回。如果 ISV 在 afterExec 调外系 · 网络慢会让审批人在 UI 看到 "loading 30s" 假死状态。**缓解**：
- afterExec 调外系必设 timeout（如 5s）
- 或改 CS-05 异步

---

## 七、跨场景影响（改 admin_org_quick 也会影响本场景）

| 在 admin_org_quick 改了 | 在 homs_orgbatchchgbill 的影响 |
|---|---|
| 加 haos_adminorg 字段（CS-01） | 申请单 add entry 想用该字段 → ⭐ 必须在 CS-01 同时给 entry 也加同名字段（不会自动级联） |
| 修改 haos_adminorg save 逻辑（CS-03） | 申请单生效写 haos_adminorg 时 · 也会触发改后的 save 逻辑（间接影响） |
| 加 BEC 订阅方（CS-04） | 申请单生效产生事件 · 同一订阅方会同时收到 admin_org_quick 和 homs_orgbatchchgbill 的事件 · 要分流处理 |
| 改 F7 过滤 haos_adminorgtablist | 申请单的 `parent_adminorg` F7 走 `haos_orgbatchtreelistf7`（独立 form · 见 `AdminOrgBatchBillPlugin.java:962-968`）· 不受影响 |

---

## 八、影响分析矩阵（汇总）

| 改动范围 | 影响层级 | 是否需要回归测试 | 推荐覆盖 |
|---|---|---|---|
| add entry 字段 | 仅本表单 | 加字段 SIT | 单表单测试 |
| parent / info / disable entry 字段 | 仅本表单 | 同 | 同 |
| save Validator | save/submit/submiteffect 链 | 完整 SIT（含历史单据回归） | 含 wftask 工作流走通 |
| submit Validator | submit/submiteffect 链 | 完整 SIT | 含 wf 引擎完整走 |
| audit afterExec | audit/submiteffect 链 + 外系 | 完整 SIT + 外系 mock | 含失败重试 / timeout |
| BEC 订阅方 | 异步队列 | BEC 队列测试 + 订阅方独立测试 | 含队列堆积压测 |
| F7 过滤 | 仅本表单 UI | UI 测试（多角色） | 含权限边界 |
| propertyChanged 联动 | 仅本表单 UI | 单表单 UI 测试 | 含 N+M 行 entry |
| ListPlugin 过滤 | 列表页 | 列表测试 | 含 BU 切换 |
| breakup 校验 | breakup 链 | 完整 breakup 流程测试 | 含 C 状态 → G 反向回退 |

---

**📌 来源追溯**：

- 主数据 4 张表：`OrgBatchChgBillEffectOp.java:66-74` + `_shared/_standard_metadata/entity_metadata/haos_adminorg.md`
- haos_adminorg_msgdetail 字段：`AdminChangeMsgService.java:81-111`
- sch_task 异步：`AdminChangeMsgService.java:113-123`
- 17+ 张 hrpi_* 引用：`knowledge/workbench/_indexes/refentity_reverse.json`
- `parent_adminorg` F7 走 haos_orgbatchtreelistf7：`AdminOrgBatchBillPlugin.java:962-968`
