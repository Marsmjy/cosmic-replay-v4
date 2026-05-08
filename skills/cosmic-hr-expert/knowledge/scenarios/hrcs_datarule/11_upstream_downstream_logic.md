# 上下游联动 · 数据规则 (hrcs_datarule)

> **状态**: 🟢 基于 3 类反编译实证 + 24 plugin 实抓 + 21 字段 scene_doc.json + rules_chain_all.json 42 opKey
> **confidence**: verified（上游 F7 关系来自 scene_doc.json refEntity 实证 · 下游消费链来自 HRDataRuleSaveOp.afterExecute 实证 + 权限链架构分析）

---

## 一、上下游全景图

```
                         ┌──────────────────────────────────────┐
                         │  上游基础资料                           │
                         │  ─────────────                        │
                         │  bos_entityobject    （业务对象定义）   │
                         │  每个 entitynum 引用一个 formId        │
                         │  提供字段集 schema（EntityMetadataCache）│
                         └─────────────┬────────────────────────┘
                                       │ entitynum F7 引用
                                       │ BasedataField · refEntity=bos_entityobject
                                       ↓
                         ┌──────────────────────────────────────┐
                         │  hrcs_datarule （主场景 · 本文档）      │
                         │                                      │
                         │  主表 t_hrcs_datarule                  │
                         │  21 字段 · 物理主表                    │
                         │                                      │
                         │  entitynum → 引用上游                  │
                         │  rule      → 存 FilterCondition JSON  │
                         │  status    → draft/saved/audit/submit │
                         │  enable    → 0/1                     │
                         └─────────────┬────────────────────────┘
                                       │
           ┌───────────────────────────┼──────────────────────────┐
           │                           │                          │
           ↓ save OP afterExecute     ↓ 权限链消费（查询时）       ↓ 权限方案引用
           │                           │                          │
  ┌────────────────────┐    ┌──────────────────────┐    ┌────────────────────────────┐
  │ 权限缓存 + 通知      │    │ hrcs 权限链运行时      │    │ 权限方案配置（下游）         │
  │ ────────────────    │    │ ────────────────      │    │ ──────────────             │
  │ HRPermCacheMgr      │    │ kd.hr.hrcs.bussiness  │    │ hrcs_dynascheme （动态方案）│
  │  .clearCache(       │    │  .service.perm.*      │    │  .permfilter 引用规则 id    │
  │   BS_HR_PERM_DATA_  │    │                      │    │                             │
  │   RULE,             │    │ 查规则 → 反序列化      │    │ hrcs_role （HR 角色）       │
  │   BS_HR_PERM_BD_    │    │ FilterCondition →     │    │  .dataruleentry 引用规则 id  │
  │   DATA_RULE)        │    │ FilterBuilder →       │    │                             │
  │                     │    │ SQL where 子句        │    │ （标品无外键 · ISV 需        │
  │ PermNotifyService   │    └──────────┬───────────┘    │  CS-04 自建引用校验）         │
  │  .notifyByDataRule  │               │                └────────────────────────────┘
  │  (id)               │               ↓
  │                     │    某个 form 列表/表单查询
  │ DataRuleLogService  │    拼上数据规则的 SQL where
  │ Helper              │    → 用户只能看符合条件的数据
  │  .dataRuleLogInit   │
  └────────────────────┘
```

---

## 二、上游依赖清单（hrcs_datarule 依赖谁）

### 2.1 直接上游（F7 / 引用关系）

| 上游实体 | hrcs_datarule 字段 | 关系 | 失败影响 |
|---|---|---|---|
| `bos_entityobject` | `entitynum` (BasedataField · refEntity=bos_entityobject) | F7 引用 · 必填 · 创建后只读 | 选不到业务对象 → 无法新建规则 |
| HisModel 时序基础资料（如 `haos_adminorg` / `hbjm_job` / `hrpi_empjobrel`）| 通过 `entitynum` 间接引用 | F7 弹窗时自动过滤 `iscurrentversion=1`（`HisModelServiceHelper.isInheritHisModelTemplate` 判定 · L82-L84） | 如果下游传 entitynum 的 boid 而非 id → 实体引用失效（PR-009） |

### 2.2 间接上游（FilterGrid 加载列时用）

| 上游系统/缓存 | 用途 | 失败影响 |
|---|---|---|
| `EntityMetadataCache` | `EntityMetadataCache.getDataEntityType(entityNum)` → 加载业务对象的字段集 → 作为 FilterGrid 的 filterColumns | 字段列为空 · FilterGrid 没列 · 没法配条件 → save 阶段 FilterBuilder 校验无字段可用 |
| `EntityTypeUtil.createFilterColumns` | 从 entityType 生成 FilterGrid 的列定义 | 同 EntityMetadataCache |
| `HisModelServiceHelper` (@SdkPublic) | 判断 entitynum 是否时序基础资料 · 决定 F7 是否加 iscurrentversion 过滤 | 判断失败 → 时序基础资料 F7 显示历史版本 → 用户可能选到历史数据 |
| `bos_user` | `creator` / `modifier` / `disabler` 字段引用（CreaterField / ModifierField / UserField · BOS L0/L1 系统字段） | 创建人/修改人显示为空 · 不影响核心功能 |

---

## 三、下游消费清单（谁依赖 hrcs_datarule）

### 3.1 权限链消费端（核心下游 · 查询时消费）

**消费者**：`kd.hr.hrcs.bussiness.service.perm.*` (hrcs 权限链运行时)

**消费时机**：任何 form 列表/表单查询时 · 权限链运行时计算当前用户的数据权限范围。

**消费逻辑**：
```
1. 查 hrcs_datarule WHERE
     fstatus = 'C' (audit)
     AND fenable = '1'
     AND fentitynum.fnumber = '<当前 form 的业务对象 number>'
2. 拿到 rule 字符串 → SerializationUtils.fromJsonString(rule, FilterCondition.class)
3. new FilterBuilder(targetEntityType, fc).buildFilter()
4. 得到 SQL where 子句 → 加到当前查询的 QFilter 列表
```

**同步/异步**：**同步**（查询时内联 · 不在查询前预计算）· 但有缓存层优化（见 3.2）。

**失败策略**：
- FilterBuilder.buildFilter 抛异常 → 查询失败 · 用户看到"规则字段非法"或类似错误 → 需要管理员修规则
- rule 字段为空 → 视为"无规则" · 不加 where 子句 → 可能授权所有数据（危险）
- 查询结果为空 → 用户看不到数据 · 但不会报错

### 3.2 权限缓存层（中间层 · save 时触发更新）

**消费方**：`HRPermCacheMgr` + `PermNotifyService`

**触发时机**：`HRDataRuleSaveOp.afterExecute` (仅 save 操作 · 且 isChange=true 时)：
```java
// HRDataRuleSaveOp.java L76-L78
String[] dataRuleCacheKeyArr = new String[]{
    HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_DATA_RULE"),
    HRPermCacheMgr.getTypeByPrefix("BS_HR_PERM_BD_DATA_RULE")
};
HRPermCacheMgr.clearCache(dataRuleCacheKeyArr);
PermNotifyService.notifyByDataRule(dataRuleId);
```

| 缓存 Key Prefix | 用途 | 覆盖范围 |
|---|---|---|
| `BS_HR_PERM_DATA_RULE` | 普通业务对象的数据规则缓存 | 所有 entitynum 指向普通 form 的规则 |
| `BS_HR_PERM_BD_DATA_RULE` | 基础资料类业务对象的数据规则缓存 | 所有 entitynum 指向基础资料 form 的规则 |

**同步/异步**：缓存清理是**同步**（afterExecute 内直调）· 但缓存失效后权限链下次查询时才重载规则 → **最终一致**。

**失败策略**：
- clearCache 失败 → 规则变更不立即生效 → 等缓存 TTL 自动过期后重载
- notifyByDataRule 失败 → 同上

### 3.3 权限方案配置层（下游 · 引用规则 ID）

| 下游实体 | 引用方式 | 关系 | 引用失效后果 |
|---|---|---|---|
| `hrcs_dynascheme` （动态授权方案） | `permfilter` 子表引用 hrcs_datarule.id | 方案通过 permfilter 配置"按数据规则过滤"→ 权限重算时展开方案 + 加载规则 + 拼 SQL | 方案里的 permfilter 条件消失 → 可能授权超过预期范围（"指空"等价于无过滤） |
| `hrcs_role` （HR 角色） | `dataruleentry` 子表引用 hrcs_datarule.id（推测 · 待反编译实证确切实体名） | 角色绑定数据规则 → 角色成员的权限范围受规则限制 | 角色成员的数据权限扩大 · 可能看到不该看到的数据 |

**关键约束**：
- 标品**没有外键级联约束** · 删除 hrcs_datarule 不级联删除 hrcs_dynascheme.permfilter 中的引用条目
- 标品 delete OP **不会查下游引用**（实证）→ 删除后方案"指空"→ 无报错 → 静默生效
- ISV 必须通过 CS-04 自建 DeleteValidator 拦截（见 06_customization_solutions.md#CS-04）

### 3.4 DataRuleLog 日志（下游 · 仅 save modify 模式）

**消费方**：`DataRuleLogServiceHelper` · 写数据规则变更日志表。

**触发时机**：`HRDataRuleSaveOp.afterExecute` · 仅 save 且 isChange=true 时：
```java
DataRuleLogModel beforeData = SerializationUtils.fromJsonString(beforeDataStr, DataRuleLogModel.class);
DataRuleLogModel afterData = DataRuleLogServiceHelper.getDataRuleLogModel(dataRuleId, false);
afterData.setBeforeDataRuleModel(beforeData);
DataRuleLogServiceHelper.dataRuleLogInit("modify", afterData);
```

**同步/异步**：**同步**（afterExecute 内直调 · 但主事务已提交 · 日志写是新事务）。

**失败策略**：dataRuleLogInit 失败 → 日志丢失 · 不影响主事务（afterExecute 已在事务外）· 不影响业务数据。

### 3.5 操作日志（下游 · 通用 · 所有 opKey 都有）

**消费方**：`HRBaseDataLogOp` · 写标品操作日志表（操作人/时间/操作类型）。

**触发时机**：所有 42 opKey 的 afterExecute 都挂 HRBaseDataLogOp（标品通用）。

---

## 四、跨模块联动场景（日常运维/ISV 开发必懂）

### 4.1 新增规则 → 联动整个链条

```
[新建] hrcs_datarule.save
   ↓
[序列化] rule 字段存 FilterCondition JSON
   ↓
[缓存] save 后 afterExecute 清 BS_HR_PERM_DATA_RULE / BS_HR_PERM_BD_DATA_RULE 缓存
   ↓
[通知] PermNotifyService.notifyByDataRule(id) → 通知权限链
   ↓
[审核] audit 后 status 变 C → 规则进入权限链
   ↓（标品 audit OP 不清缓存 → 下次权限查询才重载）
[消费] 权限链下次查询 → 加载规则 → FilterBuilder → SQL where → 用户数据范围受限
```

### 4.2 修改规则 → 联动整个链条

```
[编辑] 用户改 FilterGrid 条件 → 点保存
   ↓
[序列化] doSave 重新序列化 FilterCondition → 覆盖 rule 字段
   ↓
[对比] HRDataRuleSaveOp.beforeExecute compareFilterCondition 做语义比较
   ↓
[缓存] isChange=true → afterExecute 清缓存 + 通知 + 写 DataRuleLog
   ↓
[消费] 权限链重载规则 → 用户数据范围立即变化
```

**关键**：只有 isChange=true 才走缓存清理分支 · 如果用户打开规则没改任何东西就保存 → isChange=false → 不清缓存 → 不通知。

### 4.3 引用规则的方案被删除 → 规则不受影响

hrcs_dynascheme 被删除后 · 引用该方案的 hrcs_datarule **不受影响** · 规则本身仍然存在 · 可以在其他方案中继续使用。

**反之不成立**：hrcs_datarule 被删除后 · 引用该规则的方案**受影响**（permfilter 条目失效）· 但方案本身不被删除（标品无级联删除）。

### 4.4 业务对象被删除 → 数据规则全部失效

如果 `bos_entityobject` 中的某个业务对象被删除：
- hrcs_datarule 的 entitynum F7 列表中该业务对象消失（无法新建规则引用它）
- 已有规则的 entitynum 仍然指向该业务对象 number · 但 EntityMetadataCache 查不到 schema
- 权限链消费时 FilterBuilder.buildFilter 抛异常（无法获取 entityType）
- 该 entitynum 对应的 form 列表/表单可能打不开（取决于权限链的异常处理策略）

**建议**：删除业务对象前 · 先在 hrcs_datarule 列表按 entitynum 过滤 · 找出所有引用该业务对象的规则 → 逐一禁用 → 再删业务对象。

---

## 五、跨场景联动（hrcs_datarule 跟兄弟场景的关系）

| 兄弟场景 | 关系 | 联动点 | 具体行为 |
|---|---|---|---|
| `hrcs_dynascheme` （动态授权方案） | 方案**引用**数据规则 | `permfilter` 子表存规则 id → 权限重算时加载规则 + 拼 SQL | 方案决定"谁 + 什么条件下"分角色 · 数据规则决定"能看到哪些数据" |
| `hrcs_role` （HR 角色） | 角色**挂载**数据规则 | `dataruleentry` 子表存规则 id（推测）· 角色成员权限含规则过滤 | 角色决定"能做什么操作" · 数据规则决定"能做操作的数据范围" |
| `bos_dataperm` （平台数据权限） | 平台层的另一个数据权限体系 | 跟 hrcs_datarule 正交 · 平台权限在 SQL 层拦截 · hrcs 数据规则在 FilterBuilder 层加 where | 二者叠加生效 · 最终权限 = 平台权限 AND hrcs 数据规则 |
| `bos_entityobject` （业务对象） | hrcs_datarule 保护的目标 | entitynum 引用业务对象 · FilterCondition 引用业务对象的字段 | 业务对象 = 数据规则的作用域 |
| `haos_adminorg` （行政组织） | 最常被 entitynum 引用的业务对象之一 | FilterGrid 里选该组织的字段如"部门=研发部"过滤 | 按组织范围限制数据 |

---

## 六、同步 vs 异步总结

| 联动动作 | 同步/异步 | 延迟 | 失败影响 |
|---|---|---|---|
| save → clearCache | 同步（afterExecute 内） | 0ms | 缓存没清 → 等 TTL 过期重载 |
| save → notifyByDataRule | 同步 | 0ms | 通知失败 → 同上 |
| save → dataRuleLogInit | 同步（新事务） | 0ms | 日志丢失 · 不影响业务 |
| audit → 权限链生效 | **异步**（标品 audit OP 不清缓存） | 取决于缓存 TTL（可能数分钟） | 规则不立即生效 · ISV CS-03 补 |
| disable/enable → 权限链感知 | **异步**（标品 disable OP 不清缓存） | 同上 | 同上 |
| delete → 方案"指空" | **同步**（delete 立即删规则 · 方案不感知） | 0ms | 方案权限范围扩大 · ISV CS-04 补 |
| 权限链消费 → SQL 拼上 where | **同步**（查询时内联） | 0ms | FilterBuilder 失败 → 用户打不开列表 |
| BEC 事件发布（CS-05 ISV 自建） | **异步**（afterExecute 触发 · 平台调度） | ≥100ms | BEC 丢失 · 订阅方收不到 |

---

## 七、ISV 需要注意的联动陷阱

### 7.1 删除规则前必须主动查引用

标品不提供引用查询 → ISV 必须在 CS-04 自建 Validator · 查询：
- `hrcs_dynascheme.permfilter` 是否引用本规则 id
- `hrcs_role` 子表是否引用本规则 id（待反编译实证确切实体名）

### 7.2 缓存清理时机不完整

标品只有 save OP 清缓存 · audit / disable / enable / delete / unaudit OP 都不清 → ISV 在 CS-03 自建对应 OP 补缓存清理。

### 7.3 业务对象字段变更后规则可能失效

业务对象的字段被删除/重命名/改类型 → 已有规则的 FilterCondition 引用不存在的字段 → FilterBuilder 校验失败 → 影响权限链所有用户。

**建议**：修改业务对象字段前 · 先在 hrcs_datarule 找出引用了该字段的规则 · 手动修规则后再改字段。

### 7.4 不要在规则上引用 HisModel 的 id（版本维度）

如果 FilterGrid 里选了 HisModel 时序基础资料的字段（如"行政组织=某 id"）· 要确认是引用 **number（业务编码）** 不是引用 **id（版本维度）**。

id 在每次 confirmchange 后会变（PR-009）→ 新版本产生新 id → 规则失效。
number / boid 才是稳定的业务标识。

### 7.5 不要在 ISV BEC 订阅方里依赖规则的当前状态

BEC 是异步的（≥100ms 延迟）· 订阅方收到事件时的规则状态可能已经变了（被其他管理员改了）→ 订阅方应该按 id 重新查最新值 · 不要依赖 variables 里的快照。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/hr_hrmp_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 13 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
