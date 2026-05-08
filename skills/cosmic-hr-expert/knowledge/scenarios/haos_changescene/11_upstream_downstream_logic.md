# 上下游联动逻辑 · 行政组织变动场景（haos_changescene）

> **状态**: 🟢 基于 refentity_reverse + admin_org form_lifecycle.json + homs_orgbatchchgbill 11_upstream 双向印证
> **维度定位**: 业务级联动（业务动作 → 下游业务反应）
> **与 05_data_flow 区别**: 05 讲字段级技术反写；本文讲业务逻辑联动
> **confidence**: verified

---

## 一、总览图（基础资料字典 · 双向耦合）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 上游入口 │                                                           │
│   └─────────┘                                                           │
│        │                                                                │
│        ├─ 平台升级（出厂数据自动刷 1010/1070/1100_S 等预置主键）         │
│        ├─ HR 管理员手工建（业务自定义类型 · 占多数实际维护）              │
│        ├─ HIES 批量导入（importdata_hr opKey）                            │
│        └─ ERP / OA 系统通过 OpenAPI 同步（少见 · 走 OpenAPI 写入）        │
│                                                                         │
│        ▼                                                                │
│                                                                         │
│   ┌──────────────────────────────────────┐                              │
│   │ 本场景：haos_changescene             │                              │
│   │ - 31 字段 / 49 opKey / 3 反编译类     │                              │
│   │ - BaseFormModel 基础资料 · 非时序     │                              │
│   │ - 物理表：t_haos_changescene 等 4 张  │                              │
│   └──────────────────────────────────────┘                              │
│        │                                                                │
│        │ enable=1 进入下游 F7 选择列表                                    │
│        ▼                                                                │
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 直接下游 │                                                           │
│   └─────────┘ (强引用 · BasedataField FK)                                │
│        │                                                                │
│        ├─ haos_adminorgdetail.changescene（必填 · 详情视图）              │
│        ├─ haos_adminorghis.changescene（时序历史快照）                    │
│        └─ homs_orgbatchchgbill 7 entry × changescene 字段（业务申请单）   │
│                                                                         │
│        ▼                                                                │
│                                                                         │
│   ┌─────────┐                                                           │
│   │ 间接下游 │                                                           │
│   └─────────┘ (通过 admin_org / homs 业务流连带影响)                      │
│        │                                                                │
│        ├─ hrpi_empjobrel.adminorg → 间接（adminorg 是时序模型）           │
│        ├─ hbpm_positionhr.adminorg → 间接（岗位归属）                     │
│        ├─ 数据权限缓存（按组织过滤）                                       │
│        └─ 业务报表"按变动类型/原因统计"                                    │
│                                                                         │
│        ▲                                                                │
│        │                                                                │
│   ┌─────────┐                                                           │
│   │ 配对资料 │                                                           │
│   └─────────┘                                                           │
│        ├─ haos_orgchangetype（变动类型 · 上游 BasedataField）             │
│        ├─ haos_orgchangereason（变动原因 · 配对 MulBasedataField）        │
│        └─ haos_orgchangeoperate（变动操作 · 联动 MulBasedataField）       │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、上游详解

### 2.1 平台升级（出厂数据刷新）

- **触发源**：苍穹平台版本升级
- **行为**：自动刷新 issyspreset=true 的出厂数据
- **典型主键**：
  - 1010L = ADMINISTRATIVE（行政组织变动场景 · 标品默认）
  - 1070L = 隐藏的"内部模板项"（标品 ListPlugin 强制隐藏 · INV-CS-04）
  - 1100_S = 子集团变动场景（标品搜索时排除 · INV-CS-05）
  - 1110L = "组织修订"（formRule `37IJ/XY669PB` 描述名称不可改）
- **ISV 影响**：**不要扩展或改这些主键的关键字段** · 平台升级会覆盖（INV-CS-03）

### 2.2 HR 管理员手工建（占大头）

- **触发源**：HR 部门定期 review 业务流程 · 发现新的变动类型
- **业务场景示例**：
  - 集团并购扩张 → 加"并购整合"变动场景
  - 疫情期间临时调整 → 加"应急临时调整"变动场景（CS-03 案例）
  - 战略性裁员 → 加"战略裁员"变动场景
- **入口**：菜单【组织管理 / 行政组织维护 / 组织基础设置 / 变动场景】→ 列表点【新增】

### 2.3 HIES 批量导入

- **触发源**：HR 系统迁移 / 批量初始化数据
- **走 opKey**：`importdata_hr` · `import_multientry_hr`（如有子表）
- **特殊行为**：ChangeSceneSaveOp.beginOperationTransaction 在 `importtype != null` 时兜底联动 changeoperat（实证 §04）

### 2.4 ERP/OA 同步（少见）

- **触发源**：跟外部系统集成需要
- **走 OpenAPI**：`save.do` 接口直接写入
- **注意**：要在 OperateOption 里设置正确的 `importtype` 让标品兜底联动生效

---

## 三、下游业务反应详解

### 3.1 强引用：`homs_orgbatchchgbill` 7 entry

> 数据源：admin_org `06_customization_solutions.md` CS-02 · 4 OID 实证 + homs_orgbatchchgbill 11_upstream 实证

**用户视角**：
1. HR 主管要批量调整组织（如部门合并）→ 进入【组织调整申请】
2. 选择主体 entry 的 **changescene** F7 → 选"部门合并"
3. 在 6 个子 entry（add/parent/info/disable/merge/split）→ 各自的 *_changescene 字段也要选
4. 提交 → 审核 → 生效（OrgBatchChgBillEffectOp）→ 触发 admin_org 实际变更

**联动数据**：
| 触发动作 | 下游反应 |
|---|---|
| 选 changescene id=11000（"应急临时调整"）| 7 entry 都引用此 id · 落 t_homs_orgbatchchgbill 7 张物理表 |
| 申请单生效 | OrgBatchChgBillEffectOp 派单 · 写入 haos_adminorg.changescene · 写入 haos_adminorghis.changescene 历史快照 |
| 申请单审核驳回 | 不写 admin_org · changescene 引用仅在申请单层面 |

### 3.2 强引用：`haos_adminorgdetail`（必填字段）

> 数据源：admin_org form_lifecycle.json `_metadata.summary.requiredFieldsMain = ["number", "name", "enable", "adminorgtype", "org", "otclassify", "changescene"]` 实证

**用户视角**：
1. HR 在【组织快速维护】新建/调整组织
2. 必须选 `changescene`（required=true）→ 走本场景的 F7 选择
3. 保存 → 落 t_haos_adminorgdetail · 同时写 t_haos_adminorghis 时序历史

**联动数据**：
- changescene 字段是 BasedataField → 存的是 haos_changescene 主表 id（即版本 id · 不是 boid · 因为本场景非时序）
- haos_adminorgdetail 是时序视图 · 每次组织变更产生新版本 · 都复制 changescene 引用
- 因此一个 changescene 主键 · 可能被同一组织的多个历史版本引用

### 3.3 间接下游：admin_org 业务连带

```
[改 haos_changescene]
    ↓ 直接下游 (FK 引用)
[改 haos_adminorgdetail.changescene · 改 haos_adminorghis.changescene · 改 homs_orgbatchchgbill.7×changescene]
    ↓ 业务连带
[admin_org 整个时序版本树]
    ↓
[hrpi_empjobrel.adminorg]（员工任职关系 · 通过 boid 引用 admin_org · 不直接看 changescene）
    ↓
[hbpm_positionhr.adminorg]（岗位归属 · 同上）
    ↓
[数据权限缓存]（按组织过滤数据 · 通过 admin_org 间接生效）
    ↓
[业务报表]（按变动场景统计 · 直接命中本表 changescene 字段）
```

---

## 四、跨模块联动表

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **admin_org**（haos）| 改 changescene 直接看到的字典 + 调整申请落地 | 同步（本场景内字典写入）| 标品事务 rollback |
| **org_change**（业务流程）| OrgBatchChgBillEffectOp 派单 · 写 admin_org / adminorghis | 异步（standard 走 sch_task）| 重试机制 + 失败补偿 |
| **hrpi**（人事）| 间接：admin_org 变 → empjobrel 路径变 | 异步（admin_org BEC 通知）| 标品自动重派 |
| **pay**（薪酬）| 间接：组织变 → 成本中心归属变 | 异步 | 标品异步刷新 |
| **attendance**（考勤）| 间接：组织变 → 班次归属重算 | 异步 | 标品异步刷新 |
| **performance**（绩效）| 间接：组织变 → 跨周期重新评估 | 异步 | 标品异步刷新 |
| **wf**（工作流）| 通过 homs_orgbatchchgbill.audit 触发 | 同步（提交后立即派单）| 工作流自带补偿 |

> ⚠ 上述异步联动**都是 admin_org / homs 层的能力** · 跟本场景 haos_changescene 直接无关。
> 本场景仅作为字典维护 · **不发任何 BEC 事件**（grep 0 实证 · 见 01 §六）。

---

## 五、灵魂业务规则：改字典纪律

### 5.1 业务铁律

| 规则 | 描述 |
|---|---|
| **铁律 1** | 出厂数据（issyspreset=true）不能修改 / 不能删除（INV-CS-03）|
| **铁律 2** | 已被引用的 changescene 不能 delete（CS-02 拦）· 只能 disable |
| **铁律 3** | disable 安全 · 历史引用保留 · 新申请单选不到 |
| **铁律 4** | 修改 name 安全（多语言字段 · 下游 F7 自动跟随）|
| **铁律 5** | 修改 orgchangetype 危险（已被使用的 changescene 改类型 · 历史报表分类错位）|
| **铁律 6** | 加 ISV 字段安全（CS-01 · 不影响下游）|

### 5.2 SOP 流程图

```
[业务方说"想改一条 changescene"]
    ↓
[判断改什么]
    ├─ 只改 name / description / changereason / index → ✅ 安全 · 直接改
    ├─ 改 orgchangetype → ⚠ 危险 · 走"禁用旧 + 新建新"路径
    ├─ 加 ISV 字段 → ✅ 安全 · 走 CS-01
    └─ 删除 → ❌ 拒绝 · 走 disable
```

---

## 六、跟 haos_orgchangereason / haos_orgchangetype / haos_orgchangeoperate 的配对关系

### 6.1 三字典联动（业务上紧耦合）

```
haos_orgchangetype (变动类型)
    ↓ 上游 BasedataField (orgchangetype 字段)
haos_changescene (变动场景) ⭐ 本表
    ├─ MulBasedataField changereason → haos_orgchangereason (变动原因)
    └─ MulBasedataField changeoperat → haos_orgchangeoperate (变动操作)
                                       ↑ ChangeSceneServiceHelper.getChangeOperate
                                         按 type 自动算出
```

### 6.2 维护协同

业务上"加一类新变动场景"通常涉及 4 张字典联动：
1. **haos_orgchangetype**：先确认有对应的"变动类型"·没有则先建
2. **haos_orgchangeoperate**：先确认有对应的"变动操作"·没有则先建（默认会被 ChangeSceneServiceHelper 联动）
3. **haos_orgchangereason**：先确认有对应的"变动原因"·没有则先建（多选 · 业务可填多个）
4. **haos_changescene**：最后建本场景 · 引用前 3 个

→ 因此本场景的"上游"实际包含三张配对资料 · 维护时要按上述顺序。

---

## 七、本场景的"业务定位三句话"

1. **业务字典侧**：定义"组织变动有哪些类型" · 是一张普通基础资料字典。
2. **被深度引用**：homs_orgbatchchgbill 7 entry + haos_adminorgdetail / his 都引用本表 · 强耦合下游。
3. **维护纪律高于改造能力**：禁删禁改是核心约束（CS-02）· 加字段才是常见扩展（CS-01）· 跨模块通知应该挂在业务侧（admin_org / homs）而非本字典（CS-05 反指引）。

---

## 八、跟其他基础资料场景的对比

| 维度 | haos_changescene | haos_orgchangereason | haos_orgchangetype |
|---|---|---|---|
| 业务定位 | 变动场景字典 | 变动原因字典 | 变动类型字典 |
| 被引用强度 | ⭐⭐⭐⭐⭐（7 entry × 调整单 + admin_org）| ⭐⭐⭐⭐（changescene 多选引用 + 调整单子表）| ⭐⭐⭐⭐⭐（被 changescene 反向引用 + 业务报表分类）|
| 联动复杂度 | 中（type→operat 联动）| 低（字典）| 低（字典）|
| 反编译类数 | 3（薄壳）| 1-2（薄壳）| 1-2（薄壳）|
| ISV 改造频率 | 中（加业务线/规模分类）| 低 | 低 |

→ 三者的**维护频率 / 修改危险度**整体在同一档：低频维护 · 高危下游耦合 · 必装反向引用校验。

---

## 九、跨场景知识引用

- **admin_org_quick_maintenance** · 03 模型设计 · CS-04 BEC 订阅方范式（可作为下游联动的反面对照 — 字典层不该做这事）
- **homs_orgbatchchgbill_maintenance** · 03/06 模型设计 + CS-02 引用本表 7 entry × changescene 字段
- **haos_orgchangereason** · 配对维护
- **haos_orgchangetype** · 上游字典
- **knowledge/_shared/platform_rules.json** · PR-001/002/003/004/005/006/007/010/011 全部相关
- **knowledge/cosmic_realworld_traps/buildmeta_traps.md / addrule_traps.md / modifymeta_traps.md** · 常见踩坑参考

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
