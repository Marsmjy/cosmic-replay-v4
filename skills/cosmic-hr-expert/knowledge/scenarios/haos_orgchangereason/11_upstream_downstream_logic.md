# 上下游联动逻辑 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于 refentity_reverse + haos_changescene 03_model_design + admin_org form_lifecycle.json 双向印证
> **维度定位**: 业务级联动（业务动作 → 下游业务反应）
> **与 05_data_flow 区别**: 05 讲字段级技术反写；本文讲业务逻辑联动
> **confidence**: verified

---

## 一、总览图（基础资料字典 · 直接下游单一 · 间接下游多层）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 上游入口 │                                                           │
│   └─────────┘                                                           │
│        │                                                                │
│        ├─ 平台升级（出厂数据自动刷 1010 等预置主键）                     │
│        ├─ HR 管理员手工建（业务自定义原因 · 占多数实际维护）              │
│        ├─ HIES 批量导入（importdata_hr opKey）                            │
│        └─ ERP / OA 系统通过 OpenAPI 同步（少见 · 走 OpenAPI 写入）        │
│                                                                         │
│        ▼                                                                │
│                                                                         │
│   ┌──────────────────────────────────────┐                              │
│   │ 本场景：haos_orgchangereason         │                              │
│   │ - 28 字段 / 49 opKey / 2 反编译类     │                              │
│   │ - BaseFormModel 基础资料 · 非时序     │                              │
│   │ - 物理表：t_haos_orgchangereason 等 2 │                              │
│   └──────────────────────────────────────┘                              │
│        │                                                                │
│        │ enable=1 进入下游 F7 多选                                       │
│        ▼                                                                │
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 直接下游 │                                                           │
│   └─────────┘ (强引用 · MulBasedataField FK · 单一)                      │
│        │                                                                │
│        └─ haos_changescene.changereason 多选字段                          │
│            存储在 t_haos_cschangereason.fbasedataid                      │
│                                                                         │
│        ▼                                                                │
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 间接下游 │                                                           │
│   └─────────┘ (通过 changescene 链路 2 跳)                               │
│        │                                                                │
│        ├─ haos_adminorgdetail.changescene → 间接 reason                  │
│        ├─ haos_adminorghis.changescene → 间接 reason 的历史快照           │
│        └─ homs_orgbatchchgbill 7 entry × changescene → 间接 reason       │
│                                                                         │
│        ▼                                                                │
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 下下游  │                                                           │
│   └─────────┘ (通过 admin_org / homs 业务流连带影响 3+ 跳)                │
│        │                                                                │
│        ├─ hrpi_empjobrel.adminorg → 极间接（3 层链路）                    │
│        ├─ hbpm_positionhr.adminorg → 极间接（同上）                       │
│        ├─ 数据权限缓存（按组织过滤）                                       │
│        └─ 业务报表"按变动原因统计"（直接命中本表 changereason 字段）        │
│                                                                         │
│        ▲                                                                │
│        │                                                                │
│   ┌─────────┐                                                           │
│   │ 配对资料 │                                                           │
│   └─────────┘                                                           │
│        ├─ haos_changescene（变动场景 · 配对 · changereason 多选反向引用本表）│
│        ├─ haos_orgchangetype（变动类型 · 跟 changescene 配套）            │
│        └─ haos_orgchangeoperate（变动操作 · 跟 changescene 配套）          │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、上游详解

### 2.1 平台升级（出厂数据刷新）

- **触发源**：苍穹平台版本升级
- **行为**：自动刷新 issyspreset=true 的出厂数据
- **典型主键**：
  - 1010L = 平台保留主键 · 列表层强制隐藏（INV-CR-04）
  - 其他出厂原因（如标品默认的"组织设立"/"组织调整"/"组织合并"/"组织分立"等）
- **ISV 影响**：**不要扩展或改这些主键的关键字段** · 平台升级会覆盖（INV-CR-03）

### 2.2 HR 管理员手工建（占大头）

- **触发源**：HR 部门定期 review 业务流程 · 发现新的变动原因
- **业务场景示例**：
  - 集团并购扩张 → 加"并购整合"变动原因
  - 疫情期间临时调整 → 加"应急临时调整"变动原因
  - 战略性裁员 → 加"战略裁员"变动原因
  - 业务线重组 → 加"业务线整合"变动原因
- **入口**：菜单【组织管理 / 行政组织维护 / 组织基础设置 / 变动原因】→ 列表点【新增】

### 2.3 HIES 批量导入

- **触发源**：HR 系统迁移 / 批量初始化数据
- **走 opKey**：`importdata_hr` · `import_multientry_hr`（如有子表）
- **特殊行为**：跟 haos_changescene 不同 · 本场景的 BaseDataBuOp **不感知 importtype**（不做兜底联动）· 走标品默认导入

### 2.4 ERP/OA 同步（少见）

- **触发源**：跟外部系统集成需要
- **走 OpenAPI**：`save.do` 接口直接写入
- **注意**：跨系统同步原因字典是**反模式**（参考 CS-05 BEC 反指引）· 通常应该让外部系统通过 OpenAPI 查询本表 · 不是同步状态

---

## 三、下游业务反应详解

### 3.1 直接下游：`haos_changescene.changereason`（唯一直接路径）

> 数据源：`scenarios/haos_changescene/03_model_design.md` §3.7 双向印证

**用户视角**：
1. HR 在【变动场景】维护时打开某条 changescene
2. 选 changereason 多选 F7 → 选中本表的多个原因
3. 保存 → t_haos_cschangereason 写入多行（fid=changescene id, fbasedataid=本表 id）
4. 此后该 changescene 列表展示时 · changereason 列展示选中的原因 name（多语言）

**联动数据**：
| 触发动作 | 下游反应 |
|---|---|
| 加新 changereason | haos_changescene 维护时多选 F7 多了一个选项 |
| 修改 changereason name（多语言） | 已有 changescene 多选关系外键 id 不变 · F7 / 列表显示自动跟随更新 |
| 修改 ctrlstrategy（缩范围） | 已有 changescene 关系数据保留 · 但部分组织在新建时多选不到 |
| disable changereason | 已有 changescene 关系数据保留 · 新建 changescene 时多选不到 |
| delete changereason ⚠ | t_haos_cschangereason 该原因的所有行变孤儿 · changescene 多选展示空白 |

**反向引用强度**：⭐⭐⭐⭐（每条 changescene 通常引用 1-3 个 changereason）

### 3.2 间接下游：`homs_orgbatchchgbill` 7 entry（通过 changescene）

> 引用链：reason → changescene → 7 个 *_changescene 字段 → 调整申请单业务

**业务影响传递**：
- 删本表某条 reason → t_haos_cschangereason 行游离 → changescene 多选展示空 → 历史 homs 调整申请单的 changescene 字段链路不变（仅展示分支链路有空白）
- → 不直接触发 homs 申请单的孤儿数据 · 但报表展示出错

**反向引用强度**：⭐⭐⭐（间接 · 但通过链路放大影响面）

### 3.3 间接下游：`haos_adminorgdetail / haos_adminorghis`（通过 changescene）

> 引用链同上：reason → changescene → adminorgdetail.changescene · adminorghis.changescene

**业务影响传递**：
- 通过 changescene 链路 · 每个组织详情记录的 changescene 字段间接引用本表
- 删 reason → adminorgdetail 业务报表"按 changescene → changereason 关联统计"出错

### 3.4 下下游：admin_org 业务连带

```
[改 haos_orgchangereason]
    ↓ 直接下游 (FK 引用)
[影响 t_haos_cschangereason 子表行]
    ↓ 业务连带 (跨 1 跳)
[haos_changescene 多选 F7 / 列表展示]
    ↓ 业务连带 (跨 2 跳)
[haos_adminorgdetail / homs_orgbatchchgbill 7 entry × changescene 字段]
    ↓ 业务连带 (跨 3 跳)
[hrpi_empjobrel.adminorg]（员工任职关系 · 通过 boid 引用 admin_org · 不直接看 reason）
    ↓
[hbpm_positionhr.adminorg]（岗位归属 · 同上）
    ↓
[数据权限缓存]（按组织过滤数据 · 通过 admin_org 间接生效）
    ↓
[业务报表]（按变动原因统计 · 直接命中本表 changereason 字段）
```

---

## 四、跨模块联动表

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **changescene**（haos）| 直接：changereason 多选关系 | 同步（本场景内字典写入）| 标品事务 rollback |
| **admin_org**（haos）| 间接：通过 changescene → adminorgdetail.changescene | 异步（admin_org 自己的派单链）| 标品自动重派 |
| **org_change**（业务流程）| 间接：通过 changescene → OrgBatchChgBillEffectOp | 异步（standard 走 sch_task）| 重试机制 + 失败补偿 |
| **hrpi**（人事）| 极间接：3 层链路 | 异步 | 标品自动 |
| **pay**（薪酬）| 极间接：报表层 | 异步 | 标品异步刷新 |
| **attendance**（考勤）| 极间接：报表层 | 异步 | 标品异步刷新 |
| **performance**（绩效）| 极间接：报表层 | 异步 | 标品异步刷新 |
| **wf**（工作流）| 不直接 | 不联动 | n/a |

> ⚠ 上述异步联动**都是 admin_org / homs 层的能力** · 跟本场景 haos_orgchangereason 直接无关。
> 本场景仅作为字典维护 · **不发任何 BEC 事件**（grep 0 实证 · 见 01 §六）。

---

## 五、灵魂业务规则：改字典纪律

### 5.1 业务铁律

| 规则 | 描述 |
|---|---|
| **铁律 1** | 出厂数据（issyspreset=true）不能修改 / 不能删除（INV-CR-03）|
| **铁律 2** | 已被引用的 changereason 不能 delete（CS-02 拦）· 只能 disable |
| **铁律 3** | disable 安全 · 历史引用保留 · 新 changescene 多选不到 |
| **铁律 4** | 修改 name 安全（多语言字段 · 下游 F7 自动跟随）|
| **铁律 5** | 修改 ctrlstrategy 中危（缩范围会影响下游可见组织）|
| **铁律 6** | 加 ISV 字段安全（CS-01 · 不影响下游）|
| **铁律 7** | 双字典协同（跟 haos_changescene 维护协调）|

### 5.2 SOP 流程图

```
[业务方说"想改一条 changereason"]
    ↓
[判断改什么]
    ├─ 只改 name / description / otclassify → ✅ 安全 · 直接改
    ├─ 改 ctrlstrategy → ⚠ 中危 · review 已用情况 · 可能要业务通知
    ├─ 加 ISV 字段 → ✅ 安全 · 走 CS-01
    └─ 删除 → ❌ 拒绝 · 走 disable
```

### 5.3 双字典协同 SOP

```
[加新 changereason A]
    ↓
[业务上常跟某 changescene B "绑使用"？]
    ├─ 是 → 立即去 changescene B 编辑 · changereason 多选追加 A
    │       让 t_haos_cschangereason 写入 (B.id, A.id) 的关系
    └─ 否 → 仅本表新增 · 不动 changescene
```

---

## 六、跟 haos_changescene 的配对关系

### 6.1 双字典联动（业务上紧耦合）

```
haos_orgchangereason (变动原因 · 本表)
    ↓ 被 MulBasedataField changereason 引用
haos_changescene (变动场景)
    ├─ MulBasedataField changereason → haos_orgchangereason ⭐ 跟本表的关系
    ├─ BasedataField orgchangetype → haos_orgchangetype
    └─ MulBasedataField changeoperat → haos_orgchangeoperate
                                       ↑ ChangeSceneServiceHelper.getChangeOperate
                                         按 type 自动算出
```

### 6.2 维护协同

业务上"加一类新变动场景"通常涉及 4 张字典联动：
1. **haos_orgchangetype**：先确认有对应的"变动类型"·没有则先建
2. **haos_orgchangeoperate**：先确认有对应的"变动操作"·没有则先建（默认会被 ChangeSceneServiceHelper 联动）
3. **haos_orgchangereason** ⭐ 本表：先确认有对应的"变动原因"·没有则先建（多选 · 业务可填多个）
4. **haos_changescene**：最后建场景 · 引用前 3 个

→ 因此本场景的"上游"业务是 haos_changescene 维护需求 · 维护时要按上述顺序协同。

---

## 七、本场景的"业务定位三句话"

1. **业务字典侧**：定义"组织变动有哪些原因" · 是一张普通基础资料字典。
2. **被深度引用**：通过 haos_changescene.changereason MulBasedata 链路被深度耦合下游。
3. **维护纪律高于改造能力**：禁删禁改是核心约束（CS-02）· 加字段才是常见扩展（CS-01）· 跨模块通知应该挂在业务侧（admin_org / homs）而非本字典（CS-05 反指引）。

---

## 八、跟其他基础资料场景的对比

| 维度 | haos_orgchangereason | haos_changescene | haos_orgchangetype |
|---|---|---|---|
| 业务定位 | 变动原因字典 | 变动场景字典（核心） | 变动类型字典 |
| 被引用强度 | ⭐⭐⭐⭐（changescene 多选）| ⭐⭐⭐⭐⭐（adminorgdetail / adminorghis / homs 7 entry）| ⭐⭐⭐⭐⭐（changescene 必填 + 业务报表）|
| 直接下游数 | 1（changescene.changereason）| 3+（多个直接引用）| 1（changescene.orgchangetype）|
| 联动复杂度 | 低（仅 ctrlstrategy 平台标准）| 中（type→operat 联动）| 低（字典）|
| 反编译类数 | 2（薄壳 50 行）| 3（薄壳 139 行）| 1-2（薄壳 估计）|
| ISV 改造频率 | 低 | 中 | 低 |

→ 三者的**维护频率 / 修改危险度**整体在同一档：低频维护 · 高危下游耦合 · 必装反向引用校验。
→ 本场景在三者中**业务深度最浅** · 是新手最适合练手的字典场景。

---

## 九、跨场景知识引用

- **haos_changescene_maintenance** · 03 模型设计 + 06 CS · 直接配对参考
- **homs_orgbatchchgbill_maintenance** · 03/06 模型设计 · 间接下游引用方
- **admin_org_quick_maintenance** · 03 模型设计 · 跟 admin_org 域协作的间接路径
- **haos_orgchangetype** · 配套上游字典
- **knowledge/_shared/platform_rules.json** · PR-001/002/003/004/005/006/007/010/011 全部相关
- **knowledge/cosmic_realworld_traps/buildmeta_traps.md / addrule_traps.md / modifymeta_traps.md** · 常见踩坑参考

---

## 十、跟 haos_changescene 11_upstream_downstream 的差异总结

| 维度 | 本场景 | haos_changescene |
|---|---|---|
| 直接下游数 | 1（changescene.changereason）| 3+（adminorgdetail · adminorghis · homs 7 entry）|
| 间接下游路径 | 1 跳 → changescene → 然后看 changescene 11 | 直接到 admin_org / homs |
| 反向引用查询性能 | ~3ms（单查）| ~16ms（多查）|
| 业务关注度 | 中（HR 维护字典）| 高（HR + 业务方都关注）|
| 改造成本 | 低 | 中 |
| 双字典协同需求 | ⭐⭐ 跟 changescene 强协同 | ⭐⭐ 跟 reason / type / operat 三向协同 |

→ **结论**：本场景是 haos_changescene 的"配料"字典 · 业务深度依附 changescene。维护本表的诀窍是"先看 changescene 用了哪些"再操作。

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
