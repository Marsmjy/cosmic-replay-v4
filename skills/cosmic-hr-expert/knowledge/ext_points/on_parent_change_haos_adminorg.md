# 🔌 onParentChange@haos_adminorg ⭐ 组织特有

> **类型**: 扩展点独立档案（组织域**独有**的扩展点）
> **confidence**: verified
> **重要性**: ⭐⭐⭐ 行政组织定制的核心之一

---

## 基本信息

| 属性 | 值 |
|---|---|
| **ID** | `ep_on_parent_change_haos_adminorg` |
| **助记名** | `onParentChange@haos_adminorg` |
| **宿主实体** | `haos_adminorg` |
| **触发时机** | `on_parent_change` (父节点变更时) ⭐ **组织特有** |
| **触发条件** | `parentorg` 字段发生变化 |
| **事务阶段** | `in_transaction` (beforeSave 之后、入库之前) |
| **接口** | 操作扩展（继承 `AbsOrgBaseOp`，绑定 opKey=`movesup`/调整上级 opKey） |
| **覆盖风险** | 🔴 **极高** |

---

## 为什么需要独立的扩展点？

`haos_adminorg` 的 `parentorg` 字段变化**极其特殊**：

1. **级联重算**: 触发 `level` / `longnumber` / `structlongnumber` / `belongcompany` 重算
2. **所有后代组织**的上述 4 字段也级联重算（可能上千条）
3. **跨模块反写**: 员工归属 / 薪酬成本中心 / 考勤范围 / 绩效周期
4. **业务语义**: 代表组织重组，非普通字段修改

**如果只有通用的 `beforeSave`**：
- 每次保存都要判断 parentorg 是否变了 → 代码冗余
- 标品插件执行顺序混乱

所以苍穹为 `parentorg` 设计了独立的 `onParentChange` 扩展点。

---

## 入参详情（真实入口）

苍穹平台**不存在** `IOrgChangePlugin#onParentChange` 这类独立接口。"父节点变更"在真实 HR SDK 中是通过`haos_adminorg` 的"调整上级"操作（opKey 类似 `movesup` / `changesup` / `adjustsup`，需以开发平台实际 opKey 为准）触发的操作扩展。

推荐按如下入口切入：

- **推荐父类**：`AbsOrgBaseOp`
- **关键重写方法**：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 事务前校验（如目标上级状态、循环依赖）
  - `beginOperationTransaction(BeginOperationTransactionArgs e)` — 事务内，标品在此阶段重算 `level` / `longnumber` / `structlongnumber` / `belongcompany`，自定义扩展**不要**在此阶段改这 4 个字段
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务后做通知 / 审计 / 外部同步
- **读取新旧上级**：从动态对象的 `parent` 字段当前值读"新上级"；"旧上级"需要在 `beforeExecuteOperationTransaction` 阶段查 DB 快照或由调用方通过 OperateOption 传入。

> ⚠️ 历史知识出现过的 `ctx.getOldParent()` / `ctx.getNewParent()` / `ctx.getAffectedDescendants()` / `ctx.isBatchMode()` 等方法，**全部是伪 API**，平台未提供；请以 `AbstractOperationServicePlugIn` 的生命周期事件 + HR SDK 的 `AbsOrgBaseOp` 反编译结果为准。

---

## 标品已注册插件

只有 1 个标品插件，但**极其关键**：

### OrgPathRecalcPlugin 🔒 不建议覆盖

- **作用**: 重算 `level` + `longnumber` + `structlongnumber` + `belongcompany`
- **级联**: 同时重算所有后代组织的上述 4 字段
- **性能**: 下属 1000+ 组织时可能耗时 5-10 秒
- **超级重要**: 覆盖会破坏整棵组织树的完整性

**→ 你的自定义插件应该在此之后追加，绝不覆盖。**

---

## 推荐模式

### 模式 1 · 继承 + super 调用（唯一推荐）⭐

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`movesup` / `changesup` / `adjustsup`（以开发平台实际"调整上级"opKey 为准）
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `beginOperationTransaction(BeginOperationTransactionArgs e)` — 标品路径重算在此发生，先 super
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 路径重算已完成，在这里做通知 / 审计 / 外部同步

**业务意图**：先让标品完成 `level` / `longnumber` / `structlongnumber` / `belongcompany` 的重算（包括所有后代组织），然后追加"通知员工 + 记审计 + 同步外部系统"3 类自定义逻辑。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `movesup`（或实际的调整上级 opKey）
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⭐⭐⭐ 必须先调 `super.beginOperationTransaction(e)` 再做任何自定义逻辑，否则会破坏整棵组织树的路径字段。

### 模式 2 · 异步处理（推荐大组织）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`movesup`（调整上级）
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 在此把"通知员工"之类耗时任务丢到异步线程池或发 MQ

**业务意图**：大组织（1000+ 下属）调整上级时，主流程不再同步发通知，而是把"当前组织 ID + 新旧上级 ID"打包后异步处理：查员工列表 → 批量发站内信 / 邮件。

> ⚠️ 旧上级信息只在 `beforeExecuteOperationTransaction` 阶段还在 DB 里，建议在 before 阶段把旧父 ID 暂存到类字段或 ThreadLocal，after 阶段再取用；不要寄希望于类似 `ctx.getOldParentName()` 的伪 API。

---

## 禁止模式

### ⛔ 禁止 1: 重写方法里不调 super
**错误做法**：在 `beginOperationTransaction` 里直接写通知逻辑，忘记调 `super.beginOperationTransaction(e)`。

**后果**：**灾难性**——所有后代组织的 `longnumber` / `level` 都不会重算，整棵组织树的路径字段错乱。

### ⛔ 禁止 2: 同步处理大量下游
**错误做法**：在 `afterExecuteOperationTransaction` 里同步 for-loop 给所有员工发邮件。

**后果**：调整有 1000+ 下属的组织时主流程卡几十秒到几分钟，前端超时，用户重复点击造成事件风暴。

**正确做法**：把耗时任务丢到线程池或发 MQ 异步处理，`afterExecuteOperationTransaction` 里只做轻量的投递。

### ⛔ 禁止 3: 修改 4 个系统计算字段
**错误做法**：在任何重写方法里对 `longnumber` / `level` / `structlongnumber` / `belongcompany` 赋值。

**后果**：这 4 个字段由标品在 `beginOperationTransaction` 阶段自动计算，手动赋值会被 super 覆盖或破坏组织树完整性。

### ⛔ 禁止 4: 在 after 阶段再查旧值
**错误做法**：在 `afterExecuteOperationTransaction` 里查 DB 希望拿到"旧上级 ID / 名称"。

**后果**：此时父节点已在事务内被更新为新值，查到的永远是新值或空值。

**正确做法**：在 `beforeExecuteOperationTransaction` 阶段把旧父节点 ID / 名称暂存到类字段或 `ThreadLocal`，after 阶段再读取。

---

## 代码模板（扩展入口骨架）

### 模板 1 · 通知 + 审计（最常见）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`movesup`（调整上级，以开发平台实际 opKey 为准）
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 暂存"旧上级 ID / 名称"到类字段
  - `beginOperationTransaction(BeginOperationTransactionArgs e)` — 先 `super` 让标品重算路径
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 同步记审计 + 异步发通知

**业务意图**：调整上级完成后，同步写一条审计日志（快）；异步查询该组织下员工列表，批量发站内信（慢）。异步任务必须捕获异常，失败不能静默。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `movesup`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

### 模板 2 · 外部系统同步（AD/LDAP）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`movesup`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务提交后发 MQ 消息，把"组织 ID + 新旧上级 ID"打包投递

**业务意图**：调整上级完成后向消息队列发布 `ORG_PARENT_CHANGED` 事件，由 AD / LDAP 同步服务独立订阅、重试、降级，主流程完全不感知同步失败。

> ⚠️ 发 MQ 必须在事务提交**之后**（即 after 阶段），否则事务回滚时消息已经发出会造成数据不一致。

---

## 版本兼容性

| 特性 / 版本 | 2022R1 | 2023 | 2024 | 2024R1 | 2025 |
|---|---|---|---|---|---|
| 扩展点存在 | ✅ | ✅ | ✅ | ✅ | ✅ |
| `OrgPathRecalcPlugin` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `ctx.getAffectedDescendants()` | ❌ | ✅ | ✅ | ✅ | ✅ |
| `ctx.isBatchMode()` | ❌ | ❌ | ❌ | ✅ | ✅ |
| `batchMode` 参数 | ❌ | ❌ | ❌ | ✅ | ✅ |

### ⚠️ 2024R1 升级重要提示

新增 `batchMode` 参数。如果你的插件用反射调用此方法，需要适配新签名。

---

## 涉及的场景

| 场景 | 频率 | 注解 |
|---|---|---|
| **[行政组织快速维护](../scenarios/admin_org_quick_maintenance/)** ⭐ | **极高** | "调整上级"子场景专用 |
| 组织架构调整（调整申请单） | 中 | 审批通过后走这里 |
| 批量组织调整 | 低 | `ctx.isBatchMode()=true`，批处理优化 |
| 组织合并 | 低 | 变相的 parent 变更 |

---

## 性能陷阱

### 陷阱 1 · 大组织调整

**症状**: 调整有 1000+ 下属的组织，onParentChange 卡 30 秒

**原因**: `OrgPathRecalcPlugin` 同步重算所有后代

**缓解**:
- 改走"调整申请单"走审批流（异步批处理）
- 或用 `ctx.isBatchMode()` 识别批量模式，跳过部分逻辑

### 陷阱 2 · 深层组织树

**症状**: 11+ 层深度，一次变更触发上万条 SQL

**缓解**: 系统参数限制层级 ≤ 10

### 陷阱 3 · 联锁事件风暴

**症状**: 批量调整 100+ 组织，每个触发 5 个异步事件 = 500 次消息

**缓解**: 用 `homs_orgbatchc` 批量单，走标品的批处理聚合

---

## 相关异常

- [EX-01 · 组织循环引用](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-01)
- [EX-02 · 目标上级已停用](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-02)
- [EX-05 · 系统计算字段被篡改](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-05)

---

## 相关案例 / 方案

- [CS-04 · 调整上级时自动通知下属员工](../scenarios/admin_org_quick_maintenance/06_customization_solutions.md#cs-04)
- [CASE-04 · 批量组织调整](../scenarios/admin_org_quick_maintenance/09_cases.md#case-04)

---

**📌 来源追溯**：
- 接口签名: jar 反编译 `kd.bos.hr.haos.opplugin`
- 标品插件: `adminorg_extension_pattern.md`
- 4 字段计算: `ontology.md` belongcompany 推算算法
