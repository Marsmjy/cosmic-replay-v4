# 数据流转 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `_auto_operations.md` 执行链 + `rules_chain_all.json` writeFields / readFields + `scene_doc.json` 物理表 + 7 类反编译整合
> **关键发现**: 本场景是**时序基础资料 + 标准岗位适用组织分录 + 协作关系**的三维数据流，加上**独有的 changetype 三分类自动填充 + 消息调度任务**

---

## 一、数据落地 2+N 张物理表

```
hbpm_positionhr 表单保存
    ↓
分发到物理表：
    ├── t_hbpm_position           主物理表（68 字段 · 多语言直接存主表）
    ├── t_hbpm_standposentry      标准岗位适用组织分录（isstandardpos=1 时用）
    ↓
关联的业务服务（非物理表）：
    ├── IPositionRelationServiceApplication
    │     saveSysRelation         → 系统汇报关系（行政汇报）
    │     saveCooperationRelation → 协作关系（hbpm_positionrelation）
    │     changeCooperationRelation → 协作关系变更
    ├── IPositionHrServiceApplication
    │     afterSavePosition       → 保存后处理（通知下游缓存）
    └── IBosPositionService
          addOrUpdatePositions    → 新建/更新下游缓存
          disablePositions        → 禁用时校验 + 更新
          commonSyncPositions     → 启用时同步
```

**数据源**：`scene_doc.json` L37 `physicalTable: "t_hbpm_position + t_hbpm_standposentry（2 张物理表）"` + `PositionHrSaveOp.beginOperationTransaction` 调用链

### 字段 → 物理表映射

| 物理表 | 字段类型 | 字段数 | 典型字段 |
|---|---|---|---|
| `t_hbpm_position` | 标量 / 外键 / 日期 / 枚举 / 多语言 | 约 68 | `fnumber` / `fadminorgid` / `fparentid` / `fstatus` / `fbsed` / `fboid` / `fname` / `fposduty`（多语言直接存） |
| `t_hbpm_standposentry` | 标准岗位适用组织分录 | 3+ | `fadminorgid`（行政组织）· `fisincludesuborg`（是否包含下级）· `fentryboid` |

**⚠️ 注意**：岗位多语言字段**直接存主表** · 没有 `_l` / `_i` 子表（区别于职位场景）· `scene_doc.json` 各多语言字段 `physicalTable="t_hbpm_position"` 证明。

---

## 二、save 操作的数据写入链

### 写入顺序（按 `_auto_operations.md` save 链 L94-L107）

```
save 触发
    ↓
[Phase 1: 编码准备]
    (1) CodeRuleOp - 如未填 number 按编码规则自动生成
    ↓
[Phase 2: 基础资料写入]
    (1) BdVersionSaveServicePlugin - 基础资料版本化（名称历史等）
    ↓
[Phase 3: HR 状态维护]
    (3) HRBaseDataStatusOp - 维护 fstatus
    (4) HRBaseDataLogOp - 操作日志（默认 disabled）
    (5) HRBaseDataEnableOp - 维护 fenable
    ↓
[Phase 4: 时序维护]
    (6) HisModelOPCommonPlugin - 写 fboid / fiscurrentversion / fdatastatus / fhisversion / ffirstbsed
    (7) HRBaseOriginalOp - 记录 forinumber / foriname / foristatus 出厂值（首次）
    (7) HisUniqueValidateOp - 校验同 boid 同时段唯一
    ↓
[Phase 5: 岗位业务层]
    (9) PositionHrSaveOp - ⭐ 核心业务
         ├─ beforeExecuteOperationTransaction:
         │    ├─ 解析 unitPositionMap (导入复用 id)
         │    ├─ entity.set("org", adminorg.org)  ⭐ 自动写 org 派生
         │    └─ 新建时批量重建 parent 引用
         │
         ├─ beginOperationTransaction:
         │    ├─ super → PositionHrCommonOp (onAddValidators 继承)
         │    ├─ IPositionRelationServiceApplication.saveSysRelation(positions)
         │    │    → 写 hbpm_positionrelation 系统汇报关系
         │    │
         │    ├─ IPositionRelationServiceApplication.saveCooperationRelation(positions)
         │    │    → 写 hbpm_positionrelation 协作关系
         │    │
         │    ├─ 查 afterVersions = positions.sourcevid
         │    ├─ 查三分类 (1010 / 1010 / 1010)
         │    ├─ for afterVersion:
         │    │    set changetype / changeoperate / changescene
         │    │    set changedesc / changedescription (从 curData copy)
         │    │
         │    └─ IPositionHrServiceApplication.afterSavePosition(afterVersions)
         │         → 写下游缓存
         │
         └─ afterExecuteOperationTransaction (继承 PositionHrCommonOp):
              if (!isImport()):
                  ChangeMsgServiceImpl.sendMsg()
                  → 启动调度任务 sch_task (jobId "5/2/X9QCCFNS")
                  → 调度任务读 hbpm_position_msgdetail 批量发消息
              IBosPositionService.addOrUpdatePositions(boids)
              → 更新下游岗位缓存
```

### 关键写入字段（基于 `rules_chain_all.json` writeFields + 反编译）

| opKey | writeFields | 来源 |
|---|---|---|
| `save` | `[org, parent, changetype, changeoperate, changescene, changedesc, changedescription, id]` (通过 PositionHrSaveOp.beforeExecute + beginOperation) | `PositionHrSaveOp.java` L60 / L96-L98 |
| `enable` | `[sourcevid, changetype, changeoperate, changescene]` | `PositionHrEnableOp.java` L81-L85 |
| `disable` | `[sourcevid, changetype, changeoperate, changescene]` | `PositionHrDisableOp.java` L66-L69 |
| `confirmchange` | `[sourcevid, changetype, changeoperate, changescene]` (仅新版本) | `PositionHrChangeOp.java` L52-L56 |
| `dochangerelation` | `[changetype, changeoperate, changescene]` (所有行) | `PositionHrRelationChangeOp.java` L36-L39 |

⚠️ **注意**：writeFields 是从**反编译的方法级抽取**，不代表所有标品写入；标品插件（如 `BdVersionSaveServicePlugin` / `HisModelOPCommonPlugin`）会隐式写入更多字段（`boid` / `iscurrentversion` / `hisversion` 等）。

---

## 三、消息与调度任务机制 ⭐（岗位域特有）

### ChangeMsgServiceImpl 的 3 个核心方法

来源：`ChangeMsgServiceImpl.java` L40-L85

```
ChangeMsgServiceImpl
├── handleChangeMsg(List<EventMsgBo> eventMsgBoList)
│     保存批量消息到 hbpm_position_msgdetail
│     (dataEntityType = MetadataServiceHelper.getDataEntityType("hbpm_position_msgdetail"))
│     HBPMMsgRepository.saveBatch
│
├── sendMsg()  ⭐ save/enable/disable 后被调用
│     检查是否已有 SCHEDULED 任务 (jobId "5/2/X9QCCFNS")
│     不存在时:
│       ScheduleService.getInstance().getObjectFactory().getJobDao().get("5/2/X9QCCFNS")
│       jobInfo.setScheduleId("5/202+6WE40+")
│       JobClient.dispatch(jobInfo)
│     → 走苍穹平台 sch_task 机制
│
└── sendStdPosMsg()  (标准岗位独立消息)
      同理 · jobId "5/20+40R8LME" + scheduleId "5/20B=R/LY95"
```

### 消息调度任务流

```
[PositionHr*Op.afterExecuteOperationTransaction]
    ↓
[ChangeMsgServiceImpl.sendMsg]
    ↓
[苍穹 ScheduleService / JobClient]
    ↓ 异步
[调度任务执行]
    ↓
读取 hbpm_position_msgdetail 未处理消息
    ↓
批量发送通知给下游 app
    ↓
(具体发送实现在 kd.hrmp.hbpm.business.domain.position.service.msghandler.* 中)
```

⚠️ **关键认知**：岗位域**没有用苍穹业务事件中心（BEC · PR-011）**· 用的是自己的调度任务 + `hbpm_position_msgdetail` 消息明细表。ISV 扩展跨模块通知时应走 BEC（PR-011）· 不要直接复用 ChangeMsgServiceImpl 内部机制。

---

## 四、变更版本的数据流

### PositionHrChangeOp 处理数据流（`PositionHrChangeOp.java` L40-L63）

```
confirmchange 事务触发
    ↓
[beforeExecuteOperationTransaction]
    读: 所有位置的 boid
    查: PositionQueryRepository.getBoToLatestHisIdMap(boIdList)
    → 建 Map<boid, latestHisId> 缓存（存到 this.boToBeforeIdMap）
    ↓
[beginOperationTransaction]
    过滤 positions where !iscurrentversion  (只处理新版本)
    查三分类 (1020 / 1020 / 1020)
    for each afterVersion:
        set sourcevid = boToBeforeIdMap.getOrDefault(boid, 0L)  → 指向上一版本
        set changetype / changeoperate / changescene → 写 1020/1020/1020
    IPositionRelationServiceApplication.saveSysRelation
    if (importtype) changeCooperationRelation(positions)
    IPositionHrServiceApplication.afterSavePosition(afterVersions)
```

**业务意图**：在时序版本链上，让新版本记录知道它的上一版是谁（通过 `sourcevid` 字段），形成单向版本链。同时标记为"变更态"（changetype=1020）· 供下游消费区分。

---

## 五、事务边界

### 同步（事务内，失败则整体回滚）

| 写入目标 | 事务 | 失败 |
|---|---|---|
| `t_hbpm_position` | 主事务 | 回滚 |
| `t_hbpm_standposentry` | 主事务 | 回滚 |
| `hbpm_positionrelation`（系统汇报/协作关系） | 主事务 | 回滚（由 `IPositionRelationServiceApplication` 参与主事务） |
| 操作日志（`HRBaseDataLogOp`） | 主事务 | 回滚 |
| 编码规则序号占用 | 主事务 | 回滚（`CodeRuleOp` 处理 · 导入走 `PositionImptRuleNumberValidator` 兜底回收） |

### 异步（主事务后触发）

| 异步操作 | 触发点 | 失败影响 |
|---|---|---|
| `ChangeMsgServiceImpl.sendMsg` | `afterExecuteOperationTransaction` | 主事务已提交 · 失败不回滚（降级：下次 save 重发） |
| `IBosPositionService.addOrUpdatePositions` | `afterExecuteOperationTransaction` | 下游缓存更新 · 失败下次自动重建 |
| `IBosPositionService.disablePositions` | `afterExecuteOperationTransaction` | 禁用场景 · 内部校验通过后清缓存 |
| `hbpm_position_msgdetail` → sch_task | 调度任务异步 | 通知下游失败 · 调度有重试 |

⚠️ **遵循 PR-010**：发业务事件必须在 `afterExecuteOperationTransaction`（主事务已提交）· 不在 `endOperationTransaction`（事务可能还未最终提交产生脏事件）。标品 PositionHr*Op 都符合这一规则。

---

## 六、数据读取链

### 列表读取

```
PositionList.setFilter (super.setFilter 走 HRDataBaseList)
    ↓
追加默认 filter:
  ├── isstandardpos = 0 (业务岗位列表 · 默认)
  │   或 = 1 (标准岗位 F7 视图)
  └── isdeleted != 1 (排除软删除)
    ↓
默认排序: adminorg.id asc, positiontype.id asc, isleader desc
    ↓
查询 t_hbpm_position
    ↓
(HRDataBaseList 自动处理权限)
    ↓
返回列表数据
```

### 详情读取

```
PositionList.beforeDoOperation / hisversion_view → Mutex + 打开 PositionEdit
    ↓
PositionEdit.preOpenForm → setCustomParam hbss_entitytype_id=1021
    ↓
PositionEdit.afterBindData
    按 id 或 boid 查 t_hbpm_position (默认查 iscurrentversion=true 当前版本 · 遵循 PR-008)
    ↓
关联 t_hbpm_standposentry (标准岗位分录 · 根据 isstandardpos 控制)
    ↓
bindRelationData:
    按 boid 查 ReportingrelationQueryRepository.queryReportRelationByWorkRoleIds
    或 queryFutureReportRelationByBsed (未来生效版本)
    或 queryChangeRelationhis (历史查询)
    填充 entryentity 分录 (id + reporttype + targetpos)
    ↓
setPositionTplVisable / setControlVisible*
    ↓
渲染
```

### 岗位树上级 F7 查询

```
用户在 parent 字段点 F7
    ↓
PositionEdit.beforeF7Select "parent" 分支 L932-L936
    追加 QFilter("boid", "!=", currentBoid)  → 禁选自己
    ↓
F7 窗口查 hbpm_positionhrf7 视图 (自动带 iscurrentversion=true)
    ↓
用户选中 → 回填 parent 字段
```

### 历史版本查询

```
hisversion_view opKey
    ↓
PositionList.beforeDoOperation L69-L91
    Mutex 互斥锁
    打开 hbpm_positionrevise 页面
    customParam.position = 选中的 id
    ↓
hbpm_positionrevise 页面读所有版本 (按 boid 查)
    显示版本链 + 对比差异
```

---

## 七、时序模型的数据版本链

```
时间轴：
┌──────────────────────────────────────────────────────────┐
│  v1 (初始)            v2 (当前)             v3 (未来)      │
│  bsed=2020-01-01     bsed=2024-06-01      bsed=2026-10-01 │
│  bsled=2024-05-31    bsled=2026-09-30     bsled=9999-12-31│
│  datastatus=初始     datastatus=当前       datastatus=未来 │
│  iscurrentversion=F  iscurrentversion=T    iscurrentversion=F│
│  sourcevid=NULL      sourcevid=v1.id       sourcevid=v2.id │
│  changetype=1010     changetype=1020       changetype=1020 │
│  hisversion=1        hisversion=2          hisversion=3    │
│                                                            │
│  ^ 同一 boid 共享此业务链                                    │
└──────────────────────────────────────────────────────────┘
```

### 新增数据版本时的数据流

```
newhisversion (donothing)
    ↓
前端准备新版本数据：
  复制当前版本所有字段 → 新记录
  新记录.boid = 原记录.boid (共享业务标识)
  新记录.sourcevid = 原记录.id
  新记录.iscurrentversion = false (新版本未生效)
  新记录.bsed = 用户填写的新生效日期
  新记录.changetype = 1020 (变更)
    ↓
用户填写新字段值
    ↓
save 或 confirmchange → 入库
    ↓
PositionHisSaveOp.beforeExecuteOperationTransaction:
    calcBsledByNextBsed(positionHisArray)
    → 自动算 bsled = nextBsed - 1 天（严格连续版本）
    ↓
HisModelOPCommonPlugin 自动切换 iscurrentversion 当到期
```

---

## 八、性能陷阱

### 陷阱 1：岗位树深度遍历

- **场景**：岗位 `parent` 自引用 · 组织架构图渲染时递归查询
- **问题**：树深度 > 10 时 N+1 查询明显
- **缓解**：
  - 用 `PositionQueryRepository.queryPositionParentByIds` 批量查
  - 或缓存岗位树到 `IBosPositionService`

### 陷阱 2：成环校验全量扫描

- **场景**：`PositionHisLoopValidator.checkSysRel` 查 `queryPosHisEnableAdminRelByTimeRange`
- **问题**：同期所有岗位的 parent 链全部拉出 · 大组织（>10000 岗位）性能差
- **缓解**：
  - 按行政组织树 subtree 收敛查询范围
  - 或只校验本次变更涉及的子树

### 陷阱 3：变更消息批量发送堆积

- **场景**：批量导入 1000+ 岗位 → 每条都触发 sendMsg?
- **避坑**：`PositionHrCommonOp.afterExecuteOperationTransaction` `!isImport()` 判断跳过 · 导入时不发消息 · 由导入完成后统一触发
- **ISV 扩展注意**：如果自己扩展 afterExecute · 要照抄 `isImport()` 判断

### 陷阱 4：协作关系分录 entryentity 膨胀

- **场景**：同一岗位有 N 种汇报类型（1010/1020/...）· entryentity 分录行数 × 类型数
- **问题**：大岗位（>50 协作关系）时 N+1 查询
- **缓解**：`ReportingrelationQueryRepository.queryPosHisEnableAdminRelByTimeRange` 按类型批量查

---

## 九、数据一致性保障

### 机制 1：保存链 9 个插件的顺序约束

`CodeRuleOp` 必须在第 1 位（否则编码未生成就保存失败）；`PositionHrSaveOp` 在第 9 位（最后业务补完）。`updateOperation.plugins` 是全量替换语义，**覆盖前必须先 get 再 append**，否则会丢标品 8 个插件。

### 机制 2：HisUniqueValidateOp 时序唯一性

同一 `boid` 在同一时段只能有一条有效数据（`iscurrentversion=true`）。save 和 confirmchange 的执行链都包含此插件（`_auto_operations.md` L105 / L381）。

### 机制 3：HRBaseOriginalOp 出厂值锁

标品 `HRBaseOriginalOp` 负责记录 `orinumber` / `oriname` / `oristatus`（出厂字段）初始值；后续任何修改不能反写这 3 字段（PR-007 保护）。

### 机制 4：PositionHisValidator 时序完整性（his_save 独有）

`PositionHisValidator` 在 his_save 场景强制 9 条校验（04 L318-L330）：
- 首版本 bsed ≥ firstbsed
- 末版本 bsled 必填
- 历史版本 adminorg 需与当前版本一致
- 版本间 bsled = 下一版本 bsed - 1 天（严格连续）

### 机制 5：editXX · adminorg 不允许修改

`PositionEdit.setControlVisibleEanbleWhenEdit` L383：`setEnable(Boolean.FALSE, new String[]{"adminorg"})` · 已有数据编辑时禁改行政组织（改行政组织会影响组织归属链）。

### 机制 6：Mutex 互斥锁（`hisversion_view`）

`PositionList.doMutex` L95-L108 · 打开历史记录前对 id 加 modify 互斥锁 · 避免同一岗位多人同时改。

### 机制 7：编码回收机制

前端切字段时：
```
PositionEdit.propertyChanged → 触发字段命中 → changeNumber
    ↓
recycleNumber (把旧号写回 CodeRuleServiceHelper.recycleBatchNumber)
    ↓
setPositionNumber (按新字段值重新 CodeRuleServiceHelper.getNumber)
```
`beforeClosed` / `pageRelease` 都会调 recycleNumber · 避免未保存退出时编码号黑洞占用。

---

## 十、本场景与职位域的数据流差异

| 维度 | 岗位 hbpm_positionhr（本场景） | 职位 hbjm_jobhr |
|---|---|---|
| **物理表** | 2 张（主 + 标准岗位分录 · 多语言直存主表） | 3 张（主 + 多语言 `_i` + 多选 `_mul`） |
| **下游反写** | 2 处（`IBosPositionService` + `IPositionRelationServiceApplication`） + 1 异步（ChangeMsg） | 0 标品反写 |
| **自引用树** | 有（`parent` + 成环校验） | 无 |
| **分录** | 标准岗位适用组织 + 协作关系 | jobscm 多选（隐式 `_mul`） |
| **消息机制** | ⭐ 调度任务 (sch_task) + hbpm_position_msgdetail | `JobHrMsgHandleOp` 仅维护 sourcevid |
| **变动双分类** | ⭐ changetype + changeoperate + changescene | 无 |
| **保存链插件数** | 9 | 10 |
| **校验插件** | his_save: 3 Validator（职级范围 + 成环 + 时序） | save: MustInput + 3 GroupFieldUnique + 1 FormValidate |
| **import 特殊处理** | ⭐ isImport() 分支跳消息 + 激活回收 Validator | 无特殊分支 |

---

**📌 来源追溯**：
- 2 物理表：`scene_doc.json` L37 / L959
- save 9 插件：`_auto_operations.md` L94-L107
- PositionHrSaveOp 读写字段：`PositionHrSaveOp.java` L51-L101
- ChangeMsgServiceImpl 机制：`ChangeMsgServiceImpl.java` L40-L85
- PositionHrChangeOp 流：`PositionHrChangeOp.java` L40-L63
- PositionHisValidator 9 校验：`PositionHisValidator.java` L53-L186
- 性能陷阱 1-4：`PositionHisLoopValidator` + `PositionQueryRepository` + `PositionHrCommonOp.isImport`
- 数据一致性 5：`PositionEdit.setControlVisibleEanbleWhenEdit` L383
- 数据一致性 6：`PositionList.doMutex` L95-L108
- 数据一致性 7：`PositionEdit.recycleNumber` / `setPositionNumber` L636-L665
- 编码回收 Validator：`PositionImptRuleNumberValidator.java` L27-L29
- 时序机制：`HisUniqueValidateOp` + `HisModelOPCommonPlugin`（save 链 L104 / L105）
