# 数据流转 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 14 反编译类调用链实证
> **confidence**：verified

## 1. 数据落地总图

```
┌──────────────── UI 层 ────────────────┐
│  StructProjectEditPlugin              │
│  - getModel().setValue(key, value)    │
│  - getView().updateView(key)          │
└─────────────┬─────────────────────────┘
              │
              │ save / submit / submitandnew opKey
              ▼
┌──────────────── OP 链（7 个 OP · 顺序执行）──────────────┐
│ 1. CodeRuleOp           · 编码自动生成（onAddValidators）│
│ 2. BdVersionSaveServicePlugin · 版本管理 · 写历史子表    │
│ 3. HRBaseDataStatusOp   · 单据状态 · status A/B/C        │
│ 4. HRBaseDataLogOp      · 操作日志 · 写 t_*_log 表       │
│ 5. HRBaseDataEnableOp   · 启用态管理 · enable 0/10/1     │
│ 6. HRBaseOriginalOp     · 原始值记录 · 对比变更前后      │
│ 7. StructProjectSaveOp  · 业务核心 · saveStructProjectAndRootOrg │
└─────────────┬───────────────────────────────────────────┘
              │
              │ StructProjectSaveOp.beginOperationTransaction
              ▼
┌──────────────── 数据写入路径 ─────────────────────────┐
│                                                      │
│  saveStructProjectAndRootOrg(structProject)          │
│   │                                                  │
│   ├─ roottype=2 + rootOrgId=0（新建虚拟根）          │
│   │  └─ ORM.create().genLongId("haos_adminorgdetail")│
│   │  └─ OrgRepository.genEmptyDy()                   │
│   │  └─ rootOrg.set(...)                             │
│   │  └─ structProject.set("rootorg", rootOrg)        │
│   │                                                  │
│   ├─ roottype=2 + rootOrgId≠0（修改虚拟根）          │
│   │  └─ OrgRepository.loadCurrentVersionOrgDy(id)    │
│   │  └─ rootOrg.set(更新基本信息)                     │
│   │                                                  │
│   ├─ roottype=1（实组织）                             │
│   │  └─ OrgRepository.loadCurrentVersionOrgDy(id)    │
│   │  └─ rootOrg.set("index", 1)                      │
│   │  └─ rootOrg.set("bsed", effDate)                 │
│   │                                                  │
│   ├─ rootOrg.set("org", structProject.org)           │
│   ├─ rootOrg.set("parentorg", null)                  │
│   │                                                  │
│   ├─ if dbStructProjectDy == null（新建场景）：       │
│   │  └─ new BatchAdminOrgNewOpService(rootOrg, ...)  │
│   │     .execute()                                   │
│   │                                                  │
│   ├─ else if dbEffDate == effDate（修改场景 · 同效期）│
│   │  ├─ virtualAndNotChange（rootType 同 + 都是 2）  │
│   │  │  └─ HisModelAPIService.createDataVersions     │
│   │  └─ 否则（rootType 变化）                         │
│   │     ├─ BatchOrgDeleteOpService(旧根)             │
│   │     └─ BatchAdminOrgNewOpService(新根)           │
│   │                                                  │
│   └─ else（dbEffDate ≠ effDate）                      │
│      └─ OrgStructRepository.queryOriginalCurrentStructDys │
│      └─ 构造 OtherStructEntity                       │
│      └─ OtherStructService.saveOtherStruct()         │
│         整批迁移现有 OrgStruct（旧根 → 新根）         │
│                                                      │
└─────────────┬───────────────────────────────────────┘
              │
              │ 事务提交（保存方案主表 + 派生根组织 + 改写下挂）
              ▼
┌──────────────── 物理表落库 ──────────────────────────┐
│ t_haos_structproject       · 方案主数据              │
│ t_haos_structproject_l     · 多语言（name/...）      │
│ t_haos_adminorg            · 派生的根组织（roottype=2）│
│ t_haos_adminorg_l          · 多语言                   │
│ t_haos_adminorgstruct      · 行政组织结构（OrgStruct）│
│ t_haos_orgteamcooprel      · 组织团队协作关系         │
└─────────────┬───────────────────────────────────────┘
              │
              │ 派单 / 日志（在 endDoChangeOp / afterTransDoOp）
              ▼
┌──────────────── 事件 / 日志 ──────────────────────────┐
│ haos_adminorg_msgdetail · saveAdminOrgMsgDetail L113  │
│ admin_org change log    · saveAdminorgChangeLog L124  │
│ sch_task                · AdminChangeMsgService      │
│                           .handleChangeMsg L113-123  │
│                           JobClient.dispatch         │
└──────────────────────────────────────────────────────┘
```

## 2. 主路径 · 写入字段映射

### 2.1 StructProjectSaveOp 写入字段（来自反编译实证）

`StructProjectSaveOp.saveStructProjectAndRootOrg` L84-181 涉及的字段：

| 写入对象 | 字段 | 值来源 | 行号 |
|---|---|---|---|
| `rootOrg` (DynamicObject for haos_adminorgdetail) | `id` | ORM.create().genLongId | L96 |
| | `number` | structProject.rootnumber | L97 |
| | `name` | structProject.rootname | L98 |
| | `description` | structProject.rootdescription | L99 |
| | `isvirtualorg` | "1"（硬编码）| L100 |
| | `bsed` | effDate（structProject.effdt）| L101 |
| | `establishmentdate` | effDate | L102 |
| | `otclassify` | 1010L（硬编码）| L103 |
| | `index` | 1（硬编码）| L104 |
| | `org` | structProject.org | L119 |
| | `parentorg` | null | L120 |
| `structProject` | `rootorg` | rootOrg DynamicObject（级联保存）| L105 |

### 2.2 AdminOrgChangeLogService 写入字段（变更日志）

`AdminOrgChangeLogService.addAdminOrgChangeLog` 涉及的字段（_auto_plugin_semantics 实证 · 写 15 个字段读 16 个）：

- 写：`batchorgentity`, `bo`, `bsed`, `changereason`, `changescene`, `changesource`, `datastatus`, `entryafterversion`, `entrybeforeversion`, `entrydatastatus`, `errormsg`, `id`, `isfuture`, `pid`, `refbill`
- 读：`adminorg.boid`, `billid`, `boid`, `bsed`, `changereason.id`, `changescene.id`, `datastatus`, `effdt`, `entryafterversion.id`, `entrydatastatus`, `id`, `pid.id`, `refbill.id`, `sourcevid`

### 2.3 AdminChangeMsgService 写入字段（变更消息）

`AdminChangeMsgService.assembleMsgDy` L81-110 涉及的字段：

| 字段 | 值来源 | 行号 |
|---|---|---|
| `bo` | afterChgDy.boid | L83 |
| `beforeversion` | beforeChgDy（DynamicObject）| L84 |
| `afterversion` | afterChgDy.sourcevid（≠0 时）or .id | L85-89 |
| `isbelongcompanychange` | 比较 belongcompany 前后 | L90-94 |
| `traceid` | RequestContext.getTraceId() | L95 |
| `changeoperate` | changeSceneDy.changeoperat[0].fbasedataid | L98-103 |
| `changescene` | afterChgDy.changescene DynamicObject | L107 |
| `sendstate` | "0"（硬编码）| L108 |
| `creator` | RequestContext.getCurrUserId() | L109 |

落库表：`haos_adminorg_msgdetail`（`HRBaseServiceHelper.save` L51）

## 3. 事务边界

### 3.1 主事务边界

`StructProjectSaveOp` 是 OP 链最后一环（RowKey=7）· 走 `beginOperationTransaction` 阶段（PR-010 第 6 步）：
- **进入主事务**：beginOperationTransaction · 整个 saveStructProjectAndRootOrg 在主事务内
- **跨表写**：方案主表 + 派生根组织 + OrgStruct + OrgTeamCoopRel · 全部在主事务
- **抛异常**：事务回滚 · 所有跨表写都撤销

### 3.2 派单 / 日志的事务

- `saveAdminOrgMsgDetail` / `saveAdminorgChangeLog`（`BatchAdminOrgNewOpService.endDoChangeOp` L98-104）：在 BatchAdminOrgNewOpService 自己的 endDoChangeOp 阶段 · 仍属主事务范畴
- `handleChangeMsg`（`afterTransDoOp` L172）：在 afterTransDoOp 阶段 · 主事务已提交 · sch_task 派单是异步任务（独立事务）

### 3.3 异步派单链

```
主事务提交（StructProjectSaveOp.beginOperationTransaction 完成）
        ↓
afterTransDoOp（BatchAdminOrgNewOpService L133）
        ↓
AdminChangeMsgService.handleChangeMsg (L172)
        ↓
JobClient.dispatch(jobInfo) · 派 sch_task
        ↓
（异步） JOB_ID="5+X/4Y=AOZ=O" 执行
        ↓
（消费方） 读 haos_adminorg_msgdetail 处理消息
```

## 4. 跨表写流向（roottype 切换时 · 改写下挂组织树）

```
StructProjectSaveOp.saveStructProjectAndRootOrg L131-180
   │
   ▼
OrgStructRepository.queryOriginalCurrentStructDys(structProjectId, null, "sortcode")
   │  L147 · 拿当前方案下所有现有 OrgStruct
   ▼
构造 OtherStructEntity（List<OtherStructVO>）
   │  L148-159 · 把 oldRootOrgId 的 parent 替换为新 rootOrgId
   ▼
   ├─ virtualAndNotChange = true（dbRootType=2 + newRootType=2）
   │   └─ HisModelAPIService.reviseVersions 历史版本路径
   │   └─ otherStructEntity.setDeleteRoot(false)
   │   └─ TimeLineHelper.saveTimelineDys("haos_orgteamcooprel"...)
   │
   └─ virtualAndNotChange = false（roottype 切换或非虚拟）
       └─ otherStructEntity.setDeleteRoot(true) · 删旧根
       └─ OtherStructService.saveOtherStruct() · 整批迁移
```

物理表影响：
- `t_haos_adminorgstruct` · 整批改写 parentorg 列
- `t_haos_orgteamcooprel` · timeline 重写
- `t_haos_adminorg` · 旧根删除 · 新根创建（virtualAndNotChange=true 时只 reviseVersions 不删）

## 5. 列表读路径

```
StructProjectListPlugin.setFilter L91-108
   │
   ├─ L98 · QFilter noPreSetFilter = ("issyspreset","=","0").or("id","=", STRUCT_PROJECT_MANAGE)
   ├─ L100 · QFilter isCustomorgFilter = ("iscustomorg","=","0")
   │
   ├─ L102 · OrgPermHelper.getHRPermOrg(false) · 拿当前用户管辖 BU
   │   └─ if !hasAllOrgPerm
   │       └─ event.getQFilters().add(("org","in", hasPermOrgs))
   │
   └─ L107 · event.setOrderBy("enable desc, number asc")
                        ↓
                   平台 ListView 按 QFilter 拼 SQL
                        ↓
              SELECT FROM t_haos_structproject
              WHERE issyspreset='0' OR id=STRUCT_PROJECT_MANAGE
                AND iscustomorg='0'
                AND org IN (用户管辖 BU)
              ORDER BY enable DESC, number ASC
```

## 6. 表单读路径

```
StructProjectEditPlugin.afterBindData L138-161
   │
   ├─ status=EDIT 分支 L140-152
   │   ├─ structProjectApplication.isRootOrgMaintain(dataEntity) · 判断根组织是否维护中
   │   ├─ 维护中 · setEnable(false, "rootorg", "roottype")
   │   ├─ enable="10" · setEnable(true, "rooteffdt")
   │   ├─ roottype="2" · 调 showVirtualRootInfo() 反查
   │   │   └─ AdOrgRepository.queryByPk("number,name,org,establishmentdate,description", rootOrgId)
   │   │   └─ setValue rootnumber/rootname/rooteffdt/rootdescription
   │   ├─ AdminOrgStructRepository.queryOriVirtualOrgByStructProId · 已派生虚拟组织时
   │   │   └─ setEnable(false, "isincludevirtualorg")
   │   └─ setVisible(false, ORGORG)
   │
   ├─ status=ADDNEW 分支 L153-156
   │   ├─ setVisible(false, "rootnumber"/"rootname"/"rooteffdt"/"rootdescription"/"orgorg")
   │   ├─ setMustInput(false, "rootnumber"/"rootname"/"rooteffdt")
   │   └─ setMustInput(true, "rootorg")
   │
   ├─ status=VIEW 分支 L157-159
   │   └─ showVisibleOrDisVisible() · 按 isincludevirtualorg + roottype 综合显隐
   │
   └─ setTips L160 · 给 7 个字段（roottype/rootorg/rooteffdt/isincludevirtualorg/effdt/issyncorg）加 tips
       └─ HRCSRPCServiceHelper.queryPromptForString · 从 hrcs 拿提示文案
```

## 7. F7 选择 · 跨表读

`StructProjectEditPlugin.beforeF7Select` L208-215：
- 字段 `org` 或 `orgorg` · 拿 `OrgPermHelper.getHRPermOrg(false)`
- 非全权限时 · `addCustomQFilter(QFilter("id", "in", hasPermOrgs))`
- 跨表读 `bos_org` 但不 join · 走 customQFilter 过滤

## 8. propertyChanged 联动写入路径

`StructProjectEditPlugin.propertyChanged` L250-289：
- 监听 `roottype` / `isincludevirtualorg`
- roottype="2" 时：
  - 显示 rootnumber/rootname/rooteffdt/rootdescription
  - setMustInput(true, ...)
  - 隐藏 rootorg
  - setValue rootorg=null
  - 显示 orgorg + setMustInput(false, rootorg)
  - 调 `AdminOrgCodeRuleServiceHelper.create(view, model).setOrgNumber(hrDy, "rootnumber")` 自动赋编码
- isincludevirtualorg=true：
  - setMustInput(true, rootnumber/rootname/rooteffdt)
  - 综合 roottype 决定显隐
- isincludevirtualorg=false：
  - 隐藏 roottype/rootnumber/rootname/rooteffdt/rootdescription
  - 显示 rootorg
  - setValue roottype="1"

> **注意**：标品 propertyChanged 没用 beginInit/endInit · 因为内部都是显隐切换 + setValue 不会触发新一轮 propertyChanged 死循环（rootnumber/rootname 是 TextField 可手填 · roottype 切换不会自我触发）。ISV 加联动如果调用面更广 · 必须包 beginInit/endInit（PR-004）。

## 9. 互斥锁数据流

```
StructProjectEditPlugin.beforeDoOperation L223-230 (modify)
   │
   ▼
MutexHelper.require(view, "haos_structproject", id, "edit_struct", true, sb)
   │
   ▼ （平台底层）
   t_bos_mutex 表写入互斥记录
   key = ("haos_structproject", id, "edit_struct")
   │
   ▼
返回 true（拿到锁）/ false（被占用）
   │
   ├─ true · pageCache.put("edit_struct_clock", "true")
   └─ false · pageCache.put("edit_struct_clock", "false")
              + showErrorNotification
              + setCancel(true)

StructProjectEditPlugin.openOperationPage L298-303 (save)
   │
   ▼
MutexHelper.release("haos_structproject", "edit_struct", id)
   │
   ▼
   t_bos_mutex 表删除互斥记录
   pageCache.remove "MUTEX_*" + "edit_struct_clock"
```

## 10. 失败回滚策略

### 10.1 OP 链中失败

| 失败位置 | 处理 |
|---|---|
| onAddValidators 阶段（StructProjectValidator）| addErrorMessage + 阻断 OP 链 · 数据未写 · 表单回到错误状态 |
| beforeExecuteOperationTransaction 抛异常 | 主事务回滚 · 所有跨表写撤销 |
| beginOperationTransaction 抛异常（StructProjectSaveOp 内）| 主事务回滚 |
| BatchAdminOrgNewOpService.execute 抛异常 | 主事务回滚 |
| afterExecuteOperationTransaction 抛异常 | 主事务已提交 · 不回滚（PR-010 第 9 阶段）· 异常吞掉或抛出由用户感知 |

### 10.2 异步派单失败

| 失败位置 | 处理 |
|---|---|
| AdminChangeMsgService.handleChangeMsg 内 JobClient.dispatch 抛异常 | LOG · 不回滚主事务（异步独立）· 业务方需自己监控 sch_task 失败 |
| sch_task JOB_ID="5+X/4Y=AOZ=O" 执行失败 | 调度服务重试 · 视调度服务策略 |

## 11. 关键 SQL 路径（推断 · 非反编译实证）

```sql
-- 列表查询
SELECT * FROM t_haos_structproject
WHERE (issyspreset='0' OR id={STRUCT_PROJECT_MANAGE})
  AND iscustomorg='0'
  AND org IN ({user 管辖 BU})
ORDER BY enable DESC, number ASC;

-- 表单读
SELECT * FROM t_haos_structproject WHERE id=:id;
SELECT * FROM t_haos_structproject_l WHERE pkid=:id;

-- 派生虚拟根组织（roottype=2 + 新建）
INSERT INTO t_haos_adminorg(id, fnumber, fname, ..., fotclassifyid)
VALUES(:newId, :rootnumber, :rootname, ..., 1010);

-- 改写下挂结构
UPDATE t_haos_adminorgstruct
SET fparentorgid={newRootOrgId}
WHERE fparentorgid={oldRootOrgId} AND fstructprojectid=:structProjectId;

-- 派单消息表
INSERT INTO t_haos_adminorg_msgdetail(...)
VALUES(...);

-- 调度任务
SELECT * FROM t_sch_task WHERE fjobid='5+X/4Y=AOZ=O' AND fstatus='SCHEDULED';
INSERT INTO t_sch_task(...) VALUES(...);
```

## 12. ISV 扩展时的数据流注意事项

### 12.1 加字段（CS-01）

- 物理列加在 `t_haos_structproject` · 共用物理表 · haos_structure 也会有这一列
- ISV 字段需走 `getModel().setValue` / `entity.set` 写
- 跨表 join 不影响 · ISV 字段是平面字段

### 12.2 onAddValidators@save 校验（CS-02）

- 在 StructProjectValidator 之前 / 之后跑（按 RowKey 顺序）
- Validator.validate 走 `this.getDataEntities()` · 拿 ExtendedDataEntity[]
- 校验失败 · `addErrorMessage(ext, msg)` 阻断 OP 链 · 数据未写

### 12.3 跨表反向引用校验（CS-04）

- delete 阶段 onAddValidators 注册新 Validator
- Validator 内反查 `haos_structure.relyonstructproject IN (deleteIds)`
- 用 `HRBaseServiceHelper.queryOriginalArray("id", filter)` 跨表读
- 性能：单次 IN (10-100 个) 是可接受的（业务方案级别）

### 12.4 BEC 自建发布方（CS-05）

- 在 afterExecuteOperationTransaction 阶段（主事务已提交）发 BEC
- 用 `IEventService.triggerEventSubscribeJobs` · 走业务事件中心
- 跟标品的 sch_task 派单**并行**（不冲突 · 但会有两套派单链）
- ISV 一般只发 ISV 自建的 eventNumber · 不动标品 sch_task

---

## 来源追溯

- 14 反编译类：`knowledge/_sdk_audit/_decompiled/scenarios/haos_structproject/`
- 关键调用链：StructProjectSaveOp.saveStructProjectAndRootOrg L84-181
- 派单链：BatchAdminOrgNewOpService.afterTransDoOp L133-175 + AdminChangeMsgService.handleChangeMsg L113-123
- 互斥锁：StructProjectEditPlugin.beforeDoOperation L217-232 + openOperationPage L298-303
- 列表过滤：StructProjectListPlugin.setFilter L91-108
- 表单显隐：StructProjectEditPlugin.afterBindData L138-161 + propertyChanged L250-289
