# 上下游联动逻辑 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于 refentity_reverse + haos_adminorg 03_model_design + admin_org form_lifecycle.json 双向印证
> **维度定位**: 业务级联动（业务动作 → 下游业务反应）
> **与 05_data_flow 区别**: 05 讲字段级技术反写；本文讲业务逻辑联动
> **confidence**: verified

---

## 一、总览图（基础资料字典 · 直接下游唯一 · 下游量级等于组织总数）

```
┌────────────────────────────────────────────────────────────────────────┐
│                                                                        │
│  ┌─────────┐                                                           │
│  │ 上游入口 │                                                           │
│  └─────────┘                                                           │
│       │                                                                │
│       ├─ 平台升级（出厂数据自动刷预置职能）                              │
│       ├─ HR 管理员手工建（业务自定义职能 · 占多数实际维护）               │
│       ├─ HIES 批量导入（importdata_hr opKey）                           │
│       └─ ERP / OA 系统通过 OpenAPI 同步（少见）                         │
│                                                                        │
│       ▼                                                                │
│                                                                        │
│  ┌──────────────────────────────────────┐                              │
│  │ 本场景：haos_adminorgfunction        │                              │
│  │ - 27 字段 / 49 opKey / 2 反编译类    │                              │
│  │ - BaseFormModel 基础资料 · 非时序    │                              │
│  │ - 物理表：t_haos_adminorgfunction 等 2│                              │
│  └──────────────────────────────────────┘                              │
│       │                                                                │
│       │ enable=1 进入下游 F7 单选                                       │
│       ▼                                                                │
│                                                                        │
│  ┌─────────┐                                                           │
│  │ 直接下游 │                                                           │
│  └─────────┘ (强引用 · BasedataField 单选 · 直接外键 · 唯一)            │
│       │                                                                │
│       └─ haos_adminorg.adminorgfunction 单选字段                        │
│           存储在 t_haos_adminorg.fadminorgfunctionid                    │
│                                                                        │
│       ▼                                                                │
│                                                                        │
│  ┌─────────┐                                                           │
│  │ 间接下游 │                                                           │
│  └─────────┘ (通过 haos_adminorg 链路)                                 │
│       │                                                                │
│       ├─ haos_adminorghis（历史快照 · admin_org HisModel 特性）          │
│       ├─ hrpi_empjobrel.adminorg → 员工任职关系（极间接）                │
│       ├─ hbpm_positionhr.adminorg → 岗位归属（极间接）                  │
│       └─ 组织职能汇总报表（按职能统计 admin_org 数量）                   │
│                                                                        │
│       ▲                                                                │
│       │                                                                │
│  ┌─────────┐                                                           │
│  │ 上游协同 │                                                           │
│  └─────────┘                                                           │
│       ├─ haos_adminorg（直接使用方 · 单选关联）                          │
│       └─（无配对字典 · 本场景独立 · 不像 haos_orgchangereason 配对        │
│           haos_changescene）                                           │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 二、上游详解

### 2.1 平台升级（出厂数据刷新）

- **触发源**：苍穹平台版本升级
- **行为**：自动刷新 issyspreset=true 的出厂预置职能数据
- **典型内容**：行政/综合/专项等标准职能（具体清单随版本变化）
- **ISV 影响**：**不要扩展或改这些预置职能的关键字段** · 平台升级会覆盖（INV-AF-03）
- **listRules 约束**：本场景有 1 条 listRules formRule 保护 issyspreset=true 的行（与 haos_orgchangereason 不同）

### 2.2 HR 管理员手工建（占大头）

- **触发源**：HR 部门定期 review 业务需求 · 发现需要新的职能分类
- **业务场景示例**：
  - 数字化转型推进 → 加"数字运营"职能
  - 公司合规要求 → 加"合规审计"职能
  - 新设孵化业务 → 加"孵化创新"职能
  - 总部职能拆分 → 加"战略规划"/"资本运营"职能
- **入口**：菜单【组织管理 / 行政组织维护 / 组织基础设置 / 行政组织职能】→ 列表点【新增】

### 2.3 HIES 批量导入

- **触发源**：HR 系统迁移 / 批量初始化数据
- **走 opKey**：`importdata_hr` · `import_multientry_hr`（如有子表）
- **特殊行为**：跟 haos_orgchangereason 相同 · BaseDataBuOp **不感知 importtype**（不做兜底联动）· 走标品默认导入

### 2.4 ERP/OA 同步（少见）

- **触发源**：跟外部系统集成需要
- **走 OpenAPI**：`save.do` 接口直接写入
- **注意**：跨系统同步职能字典是**反模式** · 通常应让外部系统通过 OpenAPI 查询本表 · 不是同步状态

---

## 三、下游业务反应详解

### 3.1 直接下游：`haos_adminorg.adminorgfunction`（唯一直接路径 · 单选）

> 数据源：haos_adminorg 模型设计实证（BasedataField 单选）

**用户视角**：
1. HR 在【行政组织】维护某条 admin_org 时 · 填写 adminorgfunction 单选 F7 → 选中本表的一条职能
2. 保存 → t_haos_adminorg 该行 `fadminorgfunctionid = <选中职能 id>`
3. 此后 admin_org 列表展示时 · adminorgfunction 列展示选中的职能 name（多语言）

**与 haos_orgchangereason 下游关系的关键差异**：
| 维度 | 本场景 | haos_orgchangereason |
|---|---|---|
| 引用类型 | BasedataField 单选 | MulBasedataField 多选 |
| 存储方式 | 直接外键列 `fadminorgfunctionid` | 关系子表行 `t_haos_cschangereason.fbasedataid` |
| 每条记录最多引用数 | 1（单选）| N（多选）|
| 反向查询路径 | `QFilter("adminorgfunction", "=", id)` | `QFilter("changereason.fbasedataid", "=", id)` |
| HisModel 约束 | **有**（admin_org 时序）| 无 |

**联动数据**：
| 触发动作 | 下游反应 |
|---|---|
| 加新 adminorgfunction | haos_adminorg 维护时单选 F7 多了一个选项 |
| 修改 adminorgfunction name（多语言）| 已有 admin_org 外键 id 不变 · F7 / 列表显示自动跟随更新 |
| 修改 ctrlstrategy（缩范围）| 已有 admin_org 关联保留 · 但部分组织在重新选职能时 F7 选不到 |
| disable adminorgfunction | 已有 admin_org 关联保留 · 新建 admin_org 时 F7 选不到 |
| delete adminorgfunction ⚠ | t_haos_adminorg.fadminorgfunctionid 该记录变游离 · admin_org 列表职能列空白 |

**反向引用强度**：⭐⭐⭐⭐⭐（量级等于行政组织总数 · 远大于 haos_orgchangereason）

### 3.2 间接下游：`haos_adminorghis`（通过 admin_org HisModel）

> 引用链：haos_adminorgfunction → t_haos_adminorg.fadminorgfunctionid → haos_adminorghis（历史快照）

**业务影响传递**：
- admin_org 保存时生成新版本历史记录 · 历史行同样包含 fadminorgfunctionid
- 删本场景某条职能 → t_haos_adminorg 所有版本行（含历史）的 fadminorgfunctionid 游离
- 历史快照里"该组织历史上是什么职能"的信息丢失
- **反向查询提醒**：CS-02 的校验只需查当前版本（iscurrentversion=true）· 不需要去查历史版本游离

### 3.3 间接下游：员工任职关系 / 岗位（极间接）

> 引用链：haos_adminorgfunction → haos_adminorg → hrpi_empjobrel.adminorg

- 员工任职关系 (hrpi_empjobrel) 通过 adminorg boid 关联 admin_org
- admin_org 上的 adminorgfunction 只是分类属性 · 不直接影响 hrpi_empjobrel 的数据完整性
- **极间接影响**：BI 报表"按职能统计员工数"可能出错 · 但不影响员工任职关系本身

### 3.4 间接下游：组织职能汇总报表

```
[改 haos_adminorgfunction]
    ↓ 直接下游 (单选外键)
[haos_adminorg.adminorgfunction 列展示变化]
    ↓ 业务连带
[组织职能汇总报表]
    → "按职能统计组织数量" / "各职能下的组织分布"
    → delete 某条职能 → 该职能下的所有 admin_org 行职能列空白 → 报表缺口
    → disable 某条职能 → 历史数据保留展示 · 新增不可选
```

---

## 四、跨模块联动表

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| **haos_adminorg**（haos）| 直接：adminorgfunction 单选外键 | 同步（本场景内字典写入）| 标品事务 rollback |
| **haos_adminorghis**（haos）| 间接：admin_org 保存时生成历史快照 | 同步（HisModel 自动）| 标品自动 |
| **hrpi_empjobrel**（hrpi）| 极间接：员工任职 → admin_org boid | 异步（hrpi 自己的派单链）| 标品自动重派 |
| **hbpm_positionhr**（hbpm）| 极间接：岗位 → admin_org | 异步 | 标品自动 |
| **pay**（薪酬）| 极间接：报表层 | 异步 | 标品异步刷新 |
| **attendance**（考勤）| 极间接：报表层 | 异步 | 标品异步刷新 |
| **wf**（工作流）| 不直接 | 不联动 | n/a |
| **BEC**（事件总线）| 不发（grep 0 实证）| n/a | n/a |

> ⚠ 上述异步联动**都是 haos_adminorg / hrpi 层的能力** · 跟本场景 haos_adminorgfunction 直接无关。
> 本场景仅作为字典维护 · **不发任何 BEC 事件**（grep 0 实证 · 见 01 §BEC 节）。

---

## 五、灵魂业务规则：改字典纪律

### 5.1 业务铁律

| 规则 | 描述 |
|---|---|
| **铁律 1** | 出厂数据（issyspreset=true）不能修改 / 不能删除（INV-AF-03 · listRules INV-AF-04）|
| **铁律 2** | 已被引用的 adminorgfunction 不能 delete（CS-02 拦）· 只能 disable |
| **铁律 3** | disable 安全 · 历史引用保留 · 新 admin_org 不能选 |
| **铁律 4** | 修改 name 安全（多语言字段 · 下游 F7 自动跟随）|
| **铁律 5** | 修改 ctrlstrategy 中危（缩范围会影响下游可见组织）|
| **铁律 6** | 加 ISV 字段安全（CS-01 · 不影响下游）|
| **铁律 7** | 反向引用校验必须加 iscurrentversion=true（HisModel 特性）|

### 5.2 SOP 流程图

```
[业务方说"想改一条 adminorgfunction"]
    ↓
[判断改什么]
    ├─ 只改 name / description → ✅ 安全 · 直接改
    ├─ 改 ctrlstrategy → ⚠ 中危 · review 已用 admin_org 情况 · 可能要业务通知
    ├─ 加 ISV 字段 → ✅ 安全 · 走 CS-01
    └─ 删除 → ❌ 拒绝 · 走 disable
```

### 5.3 维护纪律（无双字典协同需求）

与 haos_orgchangereason 不同 · 本场景**没有配对字典协同需求**：
- haos_orgchangereason 需要跟 haos_changescene 双字典同步维护（changereason 多选关系）
- 本场景 adminorgfunction 是 haos_adminorg 的单选属性 · **直接外键 · 无关系子表** · 无双字典维护协议

```
[加新 adminorgfunction]
    ↓
[业务上 admin_org 维护时直接可用]
    ├─ 单选 F7 自动出现新职能
    └─ 不需要去其他字典做额外配置（与 haos_orgchangereason 的双字典协同不同）
```

---

## 六、跟 haos_adminorg 的关系

### 6.1 单选关联（业务上松耦合）

```
haos_adminorgfunction (行政组织职能 · 本表)
    ↓ 被 BasedataField 单选 adminorgfunction 引用
haos_adminorg (行政组织)
    ├─ BasedataField adminorgfunction → haos_adminorgfunction ⭐ 跟本表的关系
    ├─ BasedataField adminorglevel → haos_adminorglevel
    ├─ TreeEntity parent → haos_adminorg（自引用树形）
    └─ MulBasedataField 等其他字段
```

### 6.2 业务关联语义

- adminorgfunction 表示"这个行政组织的职能定位" · 例如"市场营销"/"技术研发"/"行政支撑"
- 是一个**分类标签** · 不影响 admin_org 的业务逻辑（不像 changescene 影响 admin_org 变动流程）
- **修改或禁用 adminorgfunction 的影响仅在展示层** · 不影响 admin_org 的业务操作

### 6.3 维护时序

业务上"给行政组织设置职能"的流程：
1. **本表**：确认有对应的"职能"数据 · 没有则先建
2. **haos_adminorg**：维护 admin_org 时选择 adminorgfunction 单选 F7

→ 因此本场景的"上游"业务需求 = haos_adminorg 维护需要 · 维护时只需一步：确保本表有所需职能数据即可。

---

## 七、本场景的"业务定位三句话"

1. **业务字典侧**：定义"行政组织有哪些职能定位" · 是一张轻量基础资料字典。
2. **被单选引用**：通过 haos_adminorg.adminorgfunction 单选外键被直接耦合 · 引用量级 = 组织总数。
3. **维护纪律高于改造能力**：禁删禁改是核心约束（CS-02）· 加字段才是常见扩展（CS-01）· 跨模块通知应挂在业务侧（admin_org）而非本字典（CS-05 反指引）。

---

## 八、跟 haos_orgchangereason 上下游逻辑对比

| 维度 | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| 直接下游 | 1 个（haos_adminorg.adminorgfunction 单选）| 1 个（haos_changescene.changereason 多选）|
| 外键类型 | 直接外键列（单选）| 关系子表行（多选）|
| 下游是否 HisModel | **是**（admin_org 时序）| 否（changescene 非时序）|
| 引用量级 | ⭐⭐⭐⭐⭐ 等于组织总数 | ⭐⭐⭐⭐ changescene × 原因数 |
| 间接下游路径 | haos_adminorg → adminorghis + hrpi | haos_changescene → adminorgdetail / homs 7 entry |
| 删除影响 | t_haos_adminorg 行直接游离（量大）| t_haos_cschangereason 子表行游离 |
| 反向查询性能 | ~3-5ms（单查 + iscurrentversion 索引）| ~3ms（单查 .fbasedataid 路径）|
| 双字典协同需求 | **无**（单选关联 · 无关系子表维护协议）| **有**（多选关联 · 需跟 changescene 协同）|
| 业务重要度 | 高（影响 admin_org 分类展示）| 中（影响 changescene 字典 · 间接影响变动流）|

---

## 九、跨场景知识引用

- **haos_adminorg_quick_maintenance** · 03 模型设计 + 06 CS · 直接配对参考（主要使用方）
- **haos_adminorghis** · 03 模型设计 · 间接下游引用方（HisModel 历史快照）
- **hrpi_empjobrel** · 03 模型设计 · 极间接下游（通过 admin_org boid）
- **haos_orgchangereason** · 双胞胎场景 · 对比参考（多选 vs 单选 / setFilter vs setOrderBy / 有无 listRules）
- **knowledge/_shared/platform_rules.json** · PR-001/002/003/004/005/006/007/010/011 全部相关
- **knowledge/cosmic_realworld_traps/** · buildmeta_traps / addrule_traps / modifymeta_traps 常见踩坑参考

---

## 参考

- PR-001 · PR-006 · PR-007 · PR-010
- `06_customization_solutions.md` — CS-02 详细代码（iscurrentversion 实证 · 单选路径实证）
- `08_impact_analysis.md` — 影响面分析
- `10_exceptions.md` — 异常诊断（S3/S4/S5 本场景特有陷阱）

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
