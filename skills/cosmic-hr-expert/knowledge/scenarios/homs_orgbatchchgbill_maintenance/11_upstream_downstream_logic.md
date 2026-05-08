# 上下游联动 · 组织调整申请单

> **状态**: 🟢 基于 refentity_reverse + 反编译 + sch_task 异步链路实证
> **confidence**: verified
> **数据源**: `refentity_reverse.json` + `AdminChangeMsgService.java` + `OrgBatchChgBillEffectOp.java`

---

## 一、上下游全景图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│  ┌─────────┐                                                                │
│  │ 上游入口 │                                                                │
│  └─────────┘                                                                │
│      │                                                                      │
│      ├─ UI 入口：组织树菜单"组织调整申请"列表（`homs_orgbatchchgbill` 列表）   │
│      ├─ wf 工作流入口：审批人在工作中心打开（appId="wftask"）                  │
│      ├─ HIES 导入：批量数据导入（`importdata_hr` / `import_multientry_hr`）   │
│      ├─ OpenAPI：外部系统通过 OpenAPI 创建申请单                               │
│      └─ 列表"组织调整明细"超链接：从 admin_org_quick 跳过来                     │
│                                                                             │
│      ▼                                                                      │
│                                                                             │
│  ┌─────────────────────────────────┐                                        │
│  │ 本场景：homs_orgbatchchgbill     │                                        │
│  │ 7 entry 申请单生命周期            │                                        │
│  └─────────────────────────────────┘                                        │
│      │                                                                      │
│      │ audit / submiteffect 生效                                             │
│      ▼                                                                      │
│                                                                             │
│  ┌─────────┐                                                                │
│  │ 直接下游 │                                                                │
│  └─────────┘                                                                │
│      │                                                                      │
│      ├─ haos_adminorg            主数据 · 多版本时序 · 4 张物理表             │
│      ├─ haos_adminorghis          历史表 · 全量保留                           │
│      ├─ haos_adminorgdetail       详情视图                                   │
│      └─ haos_adminorg_msgdetail   变动消息明细 · 派单表                        │
│                                                                             │
│      │                                                                      │
│      │ AdminChangeMsgService.handleChangeMsg                                │
│      │ JOB_ID="5+X/4Y=AOZ=O" sch_task 异步派单                                │
│      ▼                                                                      │
│                                                                             │
│  ┌─────────┐                                                                │
│  │ 间接下游 │                                                                │
│  └─────────┘ (BEC 异步事件分发)                                              │
│      │                                                                      │
│      ├─ hrpi_empjobrel            员工任职关系（最核心 · 17+ hrpi_*）          │
│      ├─ hrpi_empposorgrel         人-岗-组关系                                │
│      ├─ hrpi_rotationinfo         轮岗信息                                    │
│      ├─ hrpi_dispatchinfo         派遣信息                                    │
│      ├─ hrpi_secondment           借调记录                                    │
│      ├─ hrpi_currentposthtml      当前岗位 HTML                              │
│      ├─ hbpm_positionhr.adminorg  岗位归属（核心 · 跟人事一起转）              │
│      ├─ 数据权限缓存刷新           （标品订阅方）                              │
│      ├─ 组织树缓存                 （标品订阅方）                              │
│      └─ ISV 自定义订阅方           （CS-05 实施点）                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、上游来源详解

### 2.1 UI 列表入口

**菜单路径**：行政组织维护 / 行政组织维护 / 组织调整申请

**列表表单**：`homs_orgbatchchgbill` (`AdminOrgBatchBillListPlugin.java`)

用户进入 → 看到 5 状态的所有单据 → 点【新建】打开空表单 / 点【编辑】打开已有单。

**列表展示能力**（来自反编译实证）：
- 7 entry 数聚合显示（addcount/parentcount/infocount/disablecount/mergecount/splitcount + entryentity_all 总数）
- 默认排序 `createtime desc, billno desc`（`setFilter` L116）
- 按 BU 过滤 `customParam.businessUnit`

### 2.2 wftask 工作流入口

```java
// AdminOrgBatchBillPlugin.java:228-234 实证
public void preOpenForm(PreOpenFormEventArgs e) {
    super.preOpenForm(e);
    FormShowParameter formShowParameter = e.getFormShowParameter();
    if (HRStringUtils.equals(formShowParameter.getAppId(), "wftask")) {
        formShowParameter.setCustomParam("FormOperate_SkipAllOpCheckRight", Boolean.TRUE);
    }
}
```

→ 当 `appId="wftask"` 时 · 跳过所有 OP 操作权限校验（让审批人能在工作中心直接操作）。

### 2.3 HIES 批量导入

7 个 HIES 注入 opKey：
- `importdata_hr` 单纯导入
- `import_multientry_hr` 多分录导入（**只允许 A/G 状态** · `AdminOrgBatchBillPlugin.java:448`）
- `show_import_record_hr` 查看导入记录
- `export_from_list_hr` 按列表导出
- `export_from_impttpl_hr` 按导入模板导出
- `export_from_expttpl_hr` 按导出模板导出
- `show_export_record_hr` 查看导出记录

### 2.4 OpenAPI 外部触发

外部系统（如 OA 工单系统）可通过 OpenAPI `save` 接口创建申请单 · 透传 `customParam` 在 `afterCreateNewData` 阶段读取（参考案例 6）。

**调用方式**：
```
POST /api/save.do
{
  "formNumber": "homs_orgbatchchgbill",
  "data": {
    "org": {"id": <BU_id>},
    "effdt": "2026-05-01",
    "entryentity_add": [...],
    ...
  }
}
```

### 2.5 admin_org_quick 跳转

admin_org_quick_maintenance 的一些操作（如批量调整）会引导用户跳到本场景。具体 hyperlink 跳转代码在 admin_org 相关 FormPlugin · 不在本场景反编译范围。

---

## 三、直接下游（生效写入 4 张表）

### 3.1 `haos_adminorg` 主表

**当变动类型 = add**：
```
INSERT INTO t_haos_adminorg (...)
VALUES (id=新生成, boid=新生成, sourcevid=NULL, iscurrentversion=1, ...)
```

**当变动类型 = parent / info**：
```
1. UPDATE t_haos_adminorg SET iscurrentversion = 0 WHERE boid = X AND iscurrentversion = 1
2. INSERT INTO t_haos_adminorg (id=新, boid=X, sourcevid=旧版本id, iscurrentversion=1, ...)
```

**当变动类型 = disable**：
```
UPDATE t_haos_adminorg SET enable = '0', iscurrentversion = 1 WHERE boid = X
（停用是当前版本上的状态变更 · 不创新版本）
```

**当变动类型 = merge**：
- 多源组织 disable
- 目标组织（来自 add entry）创建为新版本
- 维护 sourcevid 链

**当变动类型 = split**：
- 源组织 disable
- 多目标组织（来自 add entry）创建
- 维护 sourcevid 链

### 3.2 `haos_adminorghis` 历史表

每次主表写新版本 · 同步全量插历史表（保留快照）。

### 3.3 `haos_adminorgdetail` 详情视图

UI 列表查询走的视图 · 跟主表同步刷新。

### 3.4 `haos_adminorg_msgdetail` 变动消息明细

来源：`AdminChangeMsgService.java:81-111` (`assembleMsgDy`)

```java
adminChangeMsgDy.set("bo", afterChgDy.getLong("boid"));
adminChangeMsgDy.set("beforeversion", beforeChgDy);
adminChangeMsgDy.set("afterversion", afterChgDy.getLong("sourcevid"));  // 或 id（无 sourcevid 时）
adminChangeMsgDy.set("isbelongcompanychange", ...);                       // 法律实体是否变
adminChangeMsgDy.set("traceid", RequestContext.get().getTraceId());
adminChangeMsgDy.set("changescene", changeSceneDy);
adminChangeMsgDy.set("changeoperate", ...);                                // 变动操作类型
adminChangeMsgDy.set("sendstate", "0");                                    // 待派发
adminChangeMsgDy.set("creator", RequestContext.get().getCurrUserId());
```

→ 异步派单的"消息体" · sch_task 消费时读这个表。

---

## 四、间接下游（BEC 异步事件链 · 21+ 张表）

### 4.1 异步派发机制

来源：`AdminChangeMsgService.java:113-123`

```java
public void handleChangeMsg() {
    HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("sch_task");
    DynamicObject task = serviceHelper.queryOriginalOne("id",
        new QFilter("job.id", "=", JOB_ID),       // "5+X/4Y=AOZ=O"
        new QFilter("status", "=", "SCHEDULED")
    );
    if (task != null) return;  // 已派 · 不重发
    JobInfo jobInfo = ScheduleService.getInstance().getObjectFactory()
                          .getJobDao().get(JOB_ID);
    jobInfo.setScheduleId(SCHEDULE_ID);                  // "5+X/=KD8ZXFW"
    String dispatch = JobClient.dispatch(jobInfo);
}
```

**特点**：
- 不直接发 BEC · 先写 `haos_adminorg_msgdetail`（持久化）+ 派 sch_task
- 异步线程消费 · 调 `EventServiceHelper.triggerEventSubscribeJobs` 派 BEC
- 有重发去重（已 SCHEDULED 不重派）

### 4.2 17+ 张 hrpi_* 引用表（来自 refentity_reverse.json）

| 下游表 | 引用字段 | 标品订阅方处理 |
|---|---|---|
| `hrpi_empjobrel` | adminorg | 员工任职跟随组织变化（核心） |
| `hrpi_empposorgrel` | adminorg | 人-岗-组联动 |
| `hrpi_rotationinfo` | adminorg | 轮岗信息更新 |
| `hrpi_dispatchinfo` | adminorg | 派遣信息更新 |
| `hrpi_secondment` | adminorg | 借调记录更新 |
| `hrpi_currentposthtml` | adminorg | 当前岗位 HTML 刷新 |
| `hrpi_emergencycontact` | adminorg | 弱依赖 |
| `hrpi_resigncrtcadre` | adminorg | 离职创建干部 |
| `hrpi_termjobchgrec` | adminorg | 离职变更记录 |
| `hrpi_*` 其他 8+ 张 | adminorg | 同模式 |

### 4.3 hbpm_positionhr 岗位下游

`hbpm_positionhr.adminorg` 引用 `haos_adminorghrf7`：
- 组织变动 → 岗位归属自动跟随（标品订阅方处理）
- 组织 disable → 关联岗位强制 disable（业务规则）

### 4.4 ISV 自定义订阅方（CS-05）

订阅同一个"行政组织变更"事件 · 跟标品订阅方并列消费。

---

## 五、跟其他场景的关系

### 5.1 跟 admin_org_quick_maintenance 共用 BEC 事件号

⭐ 关键发现：本场景的 audit/submiteffect 跟 admin_org_quick 的 confirmchange 调用**同一个** `AdminChangeMsgService.handleChangeMsg` · sch_task JOB_ID 一致 · BEC 事件号一致。

→ ISV 订阅方一次订阅 · admin_org_quick 直改 + 本场景走单 都能收到。

### 5.2 跟 hbpm_position_maintenance 的关系

岗位场景 `hbpm_positionhr.adminorg` 字段引用本场景生效的组织。当本场景生效时：
- 标品订阅方更新 `hbpm_positionhr` 的 adminorg 引用
- ISV 想监听岗位级变化 → 订阅"岗位变更"事件（不是"行政组织变更"）

### 5.3 跟 hjm_jobhr_maintenance 的关系

弱关联：职位 `hbjm_jobhr` 间接引用组织（通过岗位 `hbpm_positionhr.adminorg` → 关联职位）· 组织变动**不直接**触发职位变动。

---

## 六、跨模块业务影响（按 HR 子域）

### 6.1 hrpi (人事) — 强依赖

| 联动点 | 同步/异步 | 失败策略 | 关键字段 |
|---|---|---|---|
| `hrpi_empjobrel.adminorg` | 异步（BEC 订阅方） | BEC 重试 | adminorg, employee, iscurrentversion |
| `hrpi_empposorgrel` | 异步 | 同 | 同 |

→ 组织变动后 · 任职关系自动跟随是**标品已实现**的（不需要 ISV 写）。

### 6.2 hbpm (岗位) — 强依赖

| 联动点 | 同步/异步 | 失败策略 |
|---|---|---|
| `hbpm_positionhr.adminorg` 强引用 | 异步（BEC） | 标品订阅方处理 |
| 组织 disable → 岗位 disable | 异步 | 标品业务规则 |

### 6.3 pay (薪酬) — 弱依赖（间接）

薪酬规则按法律实体 · 法律实体跟组织有关联（`haos_adminorg.belongcompany`）。

| 联动点 | 同步/异步 |
|---|---|
| 组织变法律实体 · 触发 `isbelongcompanychange=true` | 标品订阅方 + 薪酬域订阅 |
| 员工跨组织（changescene=组织合并） · 跨法人 | 触发员工调薪流程 |

### 6.4 attendance (考勤) — 弱依赖

考勤按组织排班 · 组织变 → 排班可能要调。但**没有标品自动同步**· 通常是业务方手动维护。

### 6.5 performance (绩效) — 弱依赖

绩效目标按组织设定 · 组织变 → 绩效层级可能要调。**业务自定义**·非标品。

---

## 七、失败策略（按下游）

| 下游 | 失败时主流程影响 | 失败原因 | 缓解 |
|---|---|---|---|
| haos_adminorg 写入 | ❌ 主事务回滚 | 编码冲突 / 字段约束 | save 阶段 Validator 拦截（CS-02） |
| haos_adminorg_msgdetail 写入 | ❌ 主事务回滚（同一事务） | 表损坏 / 锁等 | 极少发生 · 联系 IT |
| sch_task 派单 | ⚠️ 业务事件不触发 · 主流程已成功 | sch_task 表锁 / JobClient 故障 | 标品自动重试 · 告警 |
| BEC 订阅方消费 | ⚠️ 订阅方独立 · 不影响主流程 | 订阅方代码异常 | 订阅方自己幂等 + 重试 |
| ISV CS-05 订阅方 | ⚠️ 不影响 | 订阅方异常 | 写补偿表 · 不抛异常 |
| ISV CS-06 同步 ERP | ⚠️ 主事务已提交 · 用户看到红错 | ERP timeout | catch + 写补偿表 |

---

## 八、跨场景操作流程（典型）

### 流程 A · "新员工入职 · 同时建新部门"

```
1. HR 在 homs_orgbatchchgbill 新建申请单
2. add entry 加 1 行：新建"前端研发部"
3. submit + audit 生效
4. → haos_adminorg 落地新部门
5. → 标品订阅方刷新组织树缓存
6. HR 在 hrpi 模块给新员工绑定任职 · 选"前端研发部"
   ✓（hrpi 实时查 haos_adminorg · 立刻可见）
```

### 流程 B · "组织合并 · 员工任职自动跟随"

```
1. HR 在 homs_orgbatchchgbill 配 add entry（建合并目标） + merge entry
2. submit + audit 生效
3. → haos_adminorg 多版本写入：源组织 disable · 目标组织 active
4. → AdminChangeMsgService 写 haos_adminorg_msgdetail (changeoperate=组织合并)
5. → sch_task 派单 → BEC 派事件
6. → 标品订阅方处理 hrpi_empjobrel：
   - 查源组织所有员工任职
   - 自动新建一条任职记录（adminorg=目标组织 · 旧记录 iscurrentversion=false）
7. → 员工在 ESS / 自助系统看到任职已变到新部门
```

### 流程 C · "外系发起组织调整"

```
1. OA 工单系统触发：调 OpenAPI save homs_orgbatchchgbill
2. → 申请单创建（A 暂存）
3. ISV CS-04 校验：${ISV_FLAG}_oa_ticket 必填（业务约束）
4. HR 在工作中心收到工单 → 编辑确认 → submit
5. → wf 工作流启动 → 审批人审批
6. → audit 生效
7. → 标品 haos_adminorg 落地
8. → ISV CS-05 BEC 订阅方收到事件 → 反向通知 OA "工单已生效"
9. → OA 关闭工单
```

---

## 九、上下游对照速查

| 我想做 | 接入点 | 对应 CS |
|---|---|---|
| 监听本场景生效 → 调外系（实时） | `afterExecuteOperationTransaction@audit` | CS-06 |
| 监听本场景生效 → 调外系（异步） | BEC 订阅方 | CS-05 |
| 监听 admin_org_quick 直改 + 本场景走单（统一） | BEC 订阅方（共用事件号） | CS-05 |
| 接收外系工单创建申请单 | OpenAPI save | 案例 6 |
| 把 OA 工单号写到申请单 | CS-01 加字段 + afterCreateNewData 自动填 | 案例 6 |
| 阻止终止已生效大型变更 | onAddValidators@breakup | CS-07 |
| 同步推送到 ERP 成本中心 | CS-06 | 案例 4 |

---

**📌 来源追溯**：

- 主数据 4 张表：`OrgBatchChgBillEffectOp.java:66-83` + `_shared/_standard_metadata/entity_metadata/haos_adminorg.md`
- BEC 异步派发：`AdminChangeMsgService.java:113-123`（JOB_ID 实证）
- haos_adminorg_msgdetail 字段：`AdminChangeMsgService.java:81-111`
- 17+ hrpi_* 引用：`knowledge/workbench/_indexes/refentity_reverse.json` `refs.haos_adminorghrf7`
- wftask 入口：`AdminOrgBatchBillPlugin.java:228-234`（preOpenForm 实证）
- HIES 7 opKey：`_auto_facts.md` + `rules_chain_all.json`
- import 状态约束：`AdminOrgBatchBillPlugin.java:448`
- 跟 admin_org_quick 共用 BEC：`AdminChangeMsgService.java`（同一 service 被两边调用）

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json` · 更新时间 2026-04-29
> 本 form（`homs_orgbatchchgbill`，所属 组织发展云）引用了其他云的 **11** 个底座实体：

### ⬆️ HR 基础服务云（`hr_hrmp`）11 个引用

| 字段 | 字段名 | 类型 | 引用实体 | 上游场景 |
|---|---|---|---|---|
| `add_corporateorg` | 法律实体 | BasedataField | `hbss_lawentity` | [hbss_law_entity](../hbss_law_entity/) |
| `add_workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `add_industrytype` | 行业类别 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `parent_workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `parent_corporateorg` | 法律实体 | BasedataField | `hbss_lawentity` | [hbss_law_entity](../hbss_law_entity/) |
| `parent_industrytype` | 行业类别 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `info_workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |
| `info_corporateorg` | 法律实体 | BasedataField | `hbss_lawentity` | [hbss_law_entity](../hbss_law_entity/) |
| `info_industrytype` | 行业类别 | BasedataField | `hbss_industrytype` | [hbss_position_dict](../hbss_position_dict/) |
| `corporateorg` | 法律实体 | BasedataField | `hbss_lawentity` | [hbss_law_entity](../hbss_law_entity/) |
| `workplace` | 工作地 | BasedataField | `hbss_workplace` | [hbss_supplier](../hbss_supplier/) |

> ⚠️ ISV 扩展须知（ADR-009）：
> - 上游底座实体是**标品字典**，原则上不可改字段（参各上游场景的 06_customization_solutions.md）
> - 引用方式（fieldType / refEntity）由本 form 元数据控制；本 form 改 ref 字段值用 `setValue` 即可
> - 修改前必须读对应上游场景的 11_upstream_downstream_logic.md，确认上游 ISV 扩展规则

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
