# 🔌 afterSave@haos_adminorg

> **类型**: 扩展点独立档案
> **confidence**: verified

---

## 基本信息

| 属性 | 值 |
|---|---|
| **ID** | `ep_after_save_haos_adminorg` |
| **助记名** | `afterSave@haos_adminorg` |
| **宿主实体** | `haos_adminorg` |
| **触发时机** | `after_save` (保存后) |
| **事务阶段** | `in_transaction` (事务内, 入库后) |
| **接口** | 操作扩展（继承 `AbsOrgBaseOp`，绑定 opKey=`save`） |
| **方法签名** | `void afterExecuteOperationTransaction(AfterOperationArgs e)` |
| **覆盖风险** | 中 |

---

## 标品插件执行链

| 顺序 | 插件 | 作用 | 事务 |
|---|---|---|---|
| 1 | `RefreshLongNumberPlugin` | 刷新 longnumber（如果未在 beforeSave 完成） | 同步 |
| 2 | `UpdateChildOrgsPlugin` | 更新下属组织的路径信息 | 同步 |
| 3 | `AuditLogPlugin` | 审计日志记录 | 同步 |
| 4 | `NotificationPlugin` | 标品通知（站内信/邮件） | 异步 |
| 5 | `CacheRebuildPlugin` | 组织缓存刷新 | 异步 |

---

## 推荐模式

### 模式 1 · 追加外部通知 / 审计（最常见）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`（保存后阶段）
- 推荐父类：`AbsOrgBaseOp`（行政组织操作类通用父类）
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 保存/修改后（事务已提交）

**业务意图**：在行政组织保存完成后，写一条自定义审计表（非标品通用审计），记录变更人、变更时间、业务属性等，满足行业特殊审计要求。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 标品已注册插件不可关闭，本扩展将与标品插件按注册顺序依次执行，务必在 `afterExecuteOperationTransaction` 里正确调用 `super.afterExecuteOperationTransaction(e)`。

### 模式 2 · 发布领域事件（推荐用于跨模块联动）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)`

**业务意图**：保存完成后根据"新建"或"修改"区分事件类型，向事件总线发布 `OrgChangeEvent`，让薪酬 / 考勤 / 绩效等下游模块按自己的节奏订阅并处理，彻底解耦。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 发事件前必须先调 `super.afterExecuteOperationTransaction(e)` 完成标品的审计 / 缓存刷新；事件通道推荐走 MQ，避免订阅者异常影响主流程。

---

## 禁止模式

### ⛔ 禁止 1: 在 afterSave 里抛异常阻止保存
此时事务已提交，抛异常**不会回滚**，但会让后续操作失败。

### ⛔ 禁止 2: 修改 entity 数据
此时数据已入库，修改 entity 不会落库（需要再调 `save`，事务嵌套混乱）。

### ⛔ 禁止 3: 同步长耗时任务
如发邮件、调外部 API，必须异步，避免拖长事务时间。

---

## 代码模板（扩展入口骨架）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务已提交，此处适合做通知 / 事件 / 外部同步

**业务意图**：保存后的通用骨架。先调 `super.afterExecuteOperationTransaction(e)` 让标品审计 + 缓存刷新完成，再根据"新建"或"修改"分支处理自定义逻辑（例如推送消息、反写下游模块）。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 事务已提交，此时抛异常不会回滚但会让后续操作失败；判断"是否新增"请依赖 `BeforeOperationArgs` 阶段暂存到类字段的标记，不要再查 DB。

---

## 版本兼容性

| 特性 | 2022R1 | 2023 | 2024 | 2024R1 | 2025 |
|---|---|---|---|---|---|
| 扩展点存在 | ✅ | ✅ | ✅ | ✅ | ✅ |
| `AuditLogPlugin` | ❌ | ✅ | ✅ | ✅ | ✅ |
| `CacheRebuildPlugin` | ❌ | ❌ | ✅ | ✅ | ✅ |

---

## 相关

- [beforeSave@haos_adminorg](./before_save_haos_adminorg.md) - 保存前配对扩展点
- [onParentChange@haos_adminorg](./on_parent_change_haos_adminorg.md) - 上级变更特殊扩展点

---

**📌 来源**: `adminorg_extension_pattern.md` + 实测
