# 🔌 beforeSave@haos_adminorg

> **类型**: 扩展点独立档案（单一数据源，多场景引用）
> **confidence**: verified

---

## 基本信息

| 属性 | 值 |
|---|---|
| **ID** | `ep_before_save_haos_adminorg` |
| **助记名** | `beforeSave@haos_adminorg` |
| **宿主实体** | `haos_adminorg` (行政组织主实体) |
| **触发时机** | `before_save` (保存前) |
| **事务阶段** | `pre_transaction` (事务未开始) |
| **接口** | 操作扩展（继承 `AbsOrgBaseOp`，绑定 opKey=`save`） |
| **方法签名** | `void beforeExecuteOperationTransaction(BeforeOperationArgs e)` |
| **执行顺序** | `serial` (按插件 order 串行) |
| **覆盖风险** | ⚠️ **高** |

---

## 入参详情

| 参数 | 类型 | 说明 |
|---|---|---|
| `entity` | `OrgEntity` | 当前保存的组织实体。`entity.isNew()` 判断新增/修改；`entity.getOldValue(key)` 拿原值 |
| `ctx` | `SaveContext` | 保存上下文。`ctx.getOperatorId()` 操作人；`ctx.getSource()` 来源（web/api/batch） |

### 关键上下文能力说明

在 `beforeExecuteOperationTransaction(BeforeOperationArgs e)` 中常用的上下文能力：

- **判断新增 vs 修改**：从 `e.getDataEntities()` 拿到动态对象数组，通过主键是否为 0 或状态字段区分新建 / 修改。
- **拿修改前的值**：通过 `DynamicObject#getDataEntityState().getBizChangedProperties()` 或查询数据库原值比较。
- **跳过后续指定校验器**：通过操作参数（OperateOption）或自定义开关字段控制标品校验的启用。
- **来源识别**：从 `OperateOption` 的业务参数判断是否为"批量导入"来源，必要时短路严格校验。

> ⚠️ 真实字段 / 方法请以 `AbsOrgBaseOp` 父类与 HR SDK 的反编译结果为准，以上仅列举业务意图对应的能力点。

---

## 标品已注册插件（执行链）

按执行顺序：

### 1. OrgCodeValidatePlugin
- **作用**: 编码格式校验（字母+数字+下划线，长度 ≤ 50）
- **是否必须 super 调用**: ✅ 是
- **典型报错**: `编码格式不合法`

### 2. OrgCodeUniquePlugin
- **作用**: 编码全局唯一
- **是否必须 super 调用**: ✅ 是
- **跳过方法**: `ctx.skipPlugin(OrgCodeUniquePlugin.class)` (如实现按租户唯一)
- **典型报错**: `EX-03 组织编码重复`

### 3. OrgHierarchyCheckPlugin
- **作用**: 层级深度检查（≤ 10 级）+ 循环引用检查
- **是否必须 super 调用**: ✅ 是
- **典型报错**: `EX-01 组织循环引用`, `EX-04 层级超限`

**→ 你的自定义插件应该在 3 之后追加**

---

## 推荐模式

### 模式 1 · 继承 + super 调用（80% 场景）⭐

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`（行政组织操作类通用父类）
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 事务开启前做业务校验与字段补全

**业务意图**：在保留全部标品校验（编码格式 / 编码唯一 / 层级循环）之外，追加单字段业务校验（例如"所属大区编码必须在大区主档里"），**不改变标品任何语义**，只增量补一条规则。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 务必先调 `super.beforeExecuteOperationTransaction(e)` 保留 3 个标品插件；自定义校验抛 `KDBizException`，框架会回滚事务并把消息反馈给前端。

### 模式 2 · 覆盖指定标品（改变其语义）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`（若要完全替换某个标品校验，推荐改走独立 `AbstractValidator` 并通过 `onAddValidators` 添加，不要 implements 接口以免丢失其他标品校验）
- 关键重写方法：
  - `onAddValidators(AddValidatorsEventArgs e)` — 动态插入自定义 `AbstractValidator`
  - 在自定义 Validator 里实现"按租户隔离查重"

**业务意图**：把"编码全局唯一"改为"按租户范围内唯一"，等效于替换标品的唯一性校验语义，其它标品校验（编码格式、层级循环）仍要保留。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本操作扩展类；该类在 `onAddValidators` 里 add 自定义 Validator，并通过 OperateOption 参数让标品唯一性校验跳过
4. 保存 → 部署生效

> ⚠️ 自定义 Validator 独立继承 `AbstractValidator`，在 `validate()` 里做租户维度查重；切忌用 `implements ISaveOpPlugin` 这种伪接口（平台不存在）。

---

## 禁止模式

### ⛔ 禁止 1: 不继承 `AbsOrgBaseOp`、自己裸写一个操作类
**错误做法**：直接继承 `AbstractOperationServicePlugIn` 或 `implements` 某个伪接口，导致 3 个标品插件（`OrgCodeValidatePlugin` / `OrgCodeUniquePlugin` / `OrgHierarchyCheckPlugin`）被完全绕过。

**正确做法**：继承 `AbsOrgBaseOp` 并在每个重写方法里先调 `super.xxx(e)`。

### ⛔ 禁止 2: 修改系统计算字段
**错误做法**：在 `beforeExecuteOperationTransaction` 里对 `level` / `longnumber` / `structlongnumber` / `belongcompany` 这 4 个字段赋值。

**原因**：这 4 个字段由标品在"父节点变更"阶段自动计算，手动赋值会被覆盖或破坏组织树的完整性。

### ⛔ 禁止 3: 重写方法里不调 super
**错误做法**：`beforeExecuteOperationTransaction` 里只写自己的校验，忘记调 `super.beforeExecuteOperationTransaction(e)`。

**后果**：丢失标品所有校验（编码格式 / 唯一性 / 层级循环），出现脏数据无人拦截。

---

## 代码模板（扩展入口骨架）

### 模板 1 · 追加自定义校验（最常用）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — 先调 super 保留标品校验，再追加业务分支（新增 / 修改两条路）
  - 如为纯校验，建议独立继承 `AbstractValidator` 并在 `onAddValidators` 里 add

**业务意图**：在 3 个标品校验之后追加业务校验，并区分新增 / 修改两条路。新增时跑"必填扩展字段""编码前缀合规"等校验；修改时对比修改前后值，做"受控字段变更"校验。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 修改场景拿原值请走 `DynamicObject` 的 `getDataEntityState()` 或 DB 查询前序快照，切勿自己编一个 `entity.getOldValues()` 的伪 API。

### 模板 2 · 前置填充（如自动编码）

**扩展入口坐标**
- 绑定表单：`haos_adminorg`
- 绑定操作：`save`
- 推荐父类：`AbsOrgBaseOp`
- 关键重写方法：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs e)` — **先补字段，再 super**

**业务意图**：在 super 调用**之前**，若判断为新增且用户未填写编码，按"公司代码_序号"规则自动生成并回填到动态对象的 `number` 字段；生成后必须立即调 `super.beforeExecuteOperationTransaction(e)` 让标品接管编码格式 / 唯一性校验。

> ⭐ 顺序铁律：**先 `setNumber`，后 `super`**，反过来会在 super 阶段因编码为空直接报错，或跳过你的自动生成逻辑。

**平台绑定方式**：
1. 打开【苍穹开发平台】→ 定位表单 `haos_adminorg`
2. 选择【操作】标签 → 找到 opKey = `save`
3. 点击【扩展插件】→ 添加本自定义类
4. 保存 → 部署生效

> ⚠️ 序号生成必须走分布式锁，避免并发重复；批量导入来源建议通过 `OperateOption` 参数识别并跳过自动生成。

---

## 版本兼容性

| 特性 / 版本 | 2022R1 | 2023 | 2024 | 2024R1 | 2025 |
|---|---|---|---|---|---|
| 扩展点存在 | ✅ | ✅ | ✅ | ✅ | ✅ |
| `OrgCodeValidatePlugin` | ❌ | ✅ | ✅ | ✅ | ✅ |
| `SaveContext.getSource()` | ❌ | ❌ | ✅ | ✅ | ✅ |
| `ctx.skipPlugin()` | ❌ | ✅ | ✅ | ✅ | ✅ |
| 异步 `beforeSave` | ❌ | ❌ | ❌ | ⚠️ 实验 | ✅ |

### 升级风险

- **2024 → 2024R1**: 新增实验性异步 `beforeSave`。同步插件不受影响，但如启用异步需检查事务逻辑。

---

## 涉及的场景（自动反向索引）

本扩展点会在以下场景触发：

| 场景 | 频率 | 场景专属注解 |
|---|---|---|
| [员工入职](../scenarios/employee_onboarding/) | 高 | 员工入职默认不会动组织，仅组织管理员场景触发 |
| **[行政组织快速维护](../scenarios/admin_org_quick_maintenance/)** ⭐ | **极高** | **本扩展点的主战场**，新增/变更/调整上级都触发 |
| [批量入职导入](../scenarios/batch_employee_import/) | 低 | `ctx.getSource() == "batch_import"` 需跳过严格校验 |
| [组织架构调整（调整申请）](../scenarios/org_change_bill/) | 中 | 审批通过后才触发 |

---

## 相关异常

- [EX-01 · 组织循环引用](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-01--组织循环引用)
- [EX-03 · 组织编码重复](../scenarios/admin_org_quick_maintenance/10_exceptions.md#ex-03--组织编码重复)

---

## 相关案例

- [CASE-01 · 行政组织扩展 3 字段](../scenarios/admin_org_quick_maintenance/09_cases.md#case-01)
- [CS-03 · 组织编码自动生成](../scenarios/admin_org_quick_maintenance/06_customization_solutions.md#cs-03)
- [CS-05 · 按租户隔离唯一](../scenarios/admin_org_quick_maintenance/06_customization_solutions.md#cs-05)

---

**📌 来源追溯**：
- 签名/事务阶段: `knowledge/domain/org/anchors.md` + jar 反编译
- 标品插件链: `adminorg_extension_pattern.md` + 实测
- 版本兼容: `platform/openapi_capability_map.md`
