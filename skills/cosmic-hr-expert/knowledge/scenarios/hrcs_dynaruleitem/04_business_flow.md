# 业务流转 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类（DynaRuleItemEdit / DynaItemDeleteOp / HRAdminStrictPlugin）+ scene_doc.json + opkeys_index.json
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI `getOpKeyClasses` (2026-04-28)

---

## 一、业务全景：规则参数项要做什么

`hrcs_dynaruleitem`（规则参数项）解决的是 **"为动态授权方案提供可配置的规则字典"** 的运营问题：

1. **HR 管理员**通过本菜单维护一组"规则参数项" —— 描述方案规则可用的"字段"和"枚举值"
2. 当 HR 管理员配置 `hrcs_dynascheme`（动态授权方案）时 · PermFilter 控件从本表加载可选 paramId + value
3. 用户在方案上配置规则时选的是本表数据 · 形成 dynascheme.condition JSON
4. 后续规则引擎按方案的 condition JSON 反查本表 + 相应实体 · 评估员工是否命中

> **关键差异点**（决定本场景为何这么独立）：
> - 一个规则参数项是 **元数据级配置**（不是业务运营数据 · 配完通常很少改）
> - **不是 HisModel** · 没有变更工作流（无 confirmchange / change / hisversion 等操作）
> - 与 dynascheme **强耦合**：本表数据是被 dynascheme.condition JSON 直接引用的"字典数据"
> - **三种数据类型**（datatype）给方案提供完全不同形式的规则参数：基础资料 F7 / 行政组织 F7 / 枚举下拉
> - **两种值来源**（valsourcetype）：实体属性 / 微服务调用

---

## 二、业务状态机

### 2.1 数据状态（`status` 字段 · BillStatusField）

```
A 暂存 ──── submit ────► B 已提交 ──── audit ───► C 已审核
   ▲                           │                        │
   │                           │ unsubmit               │ unaudit
   └───────────────────────────┘                        │
```

**关键 opKey 映射**（与 dynascheme 类似 · 但本场景**没有 confirmchange**）：

| opKey | 状态变化 | 关键 OP | 反编译实证位置 |
|---|---|---|---|
| `save` | 不改状态 · 但触发 enum 校验 + 净化脏字段 | DynaRuleItemEdit.beforeItemClick + 标品 6 OP 链 | DynaRuleItemEdit.java L233-L251 |
| `submit` | A → B | 标品 5 OP（CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp） | rules_chain_all.json submit |
| `unsubmit` | B → A | 标品 OP · 无场景特别逻辑 | - |
| `audit` | B → C | 仅 HRBaseDataLogOp（仅记日志 · 无场景工作流） | rules_chain_all.json audit |
| `unaudit` | C → A | 标品 OP · 无场景特别逻辑 | - |

### 2.2 使用状态（`enable` 字段 · BillStatusField）

```
0 已禁用 ──── enable ───► 1 已启用 / 10 启用中
   ▲                          │
   │                          │ disable
   └──────────────────────────┘
```

**关键约束**：
- enable / disable 都只挂 `HRBaseDataLogOp`（仅记日志）· 没有特别业务校验 · ISV 想做"禁用前查下游引用" 必须自建 OP（套路同 CS-04）
- **disable 不会自动清理 dynascheme.condition 引用** —— 标品没做联动清理 · 禁用一个被引用的参数项后 · dynascheme 仍然引用它（只是 condition 评估时可能空）

### 2.3 删除链（`delete` opKey）

```
列表 / 单据点【删除】
    ↓
CodeRuleDeleteOp.beforeExecute (enabled=True · 平台编码规则清理)
    ↓
HRBaseDataStatusOp.onAddValidators (单据状态校验)
    ↓
HRBaseDataLogOp.beforeExecute (记日志)
    ↓
DynaItemDeleteOp.onPreparePropertys (声明读 name)
    ↓
DynaItemDeleteOp.onAddValidators (注册 DynaItemDelValidator)
    ↓
DynaItemDelValidator.validate (反查 DynaSchemeServiceHelper.queryRelDynaScheme)
    ↓
被引用 → addErrorMessage("规则参数项 [%s] 被 N 个动态授权方案引用·请先解除引用再删除")
不被引用 → 走标品删除 + DB DELETE
    ↓
HRBaseDataLogOp.afterExecute (落日志)
```

---

## 三、新建参数项的完整链路

### 3.1 入口：菜单 → 列表 → 新增

```
HR 通用服务 / 权限管理 / 规则参数项 (菜单)
   ↓
hrcs_dynaruleitem 列表（HRAdminStrictPlugin.preOpenForm 校验 HR 管理员）
   ↓
列表点【新增】(opKey = new)
   ↓
打开 hrcs_dynaruleitem 表单
```

### 3.2 表单初始化阶段

按生命周期执行（实证 _auto_plugin_registry.md 19 plugin · DynaRuleItemEdit 在 #7）：

```
1. preOpenForm
   - HRBaseDataTplEdit (HR 基础资料模板)
   - HRAdminStrictPlugin (HR 域准入校验 · 不通过弹错)

2. beforeBindData
   - HRBaseDataTplList (列表层 · 不影响表单)
   - HRBasedataLogList (列表层)

3. afterBindData (执行顺序按 _auto_plugin_registry.md 注册顺序)
   - #3 HRBaseDataTplEdit
   - #5 HRHiesButtonSwitchPlugin (HIES 按钮切换)
   - #7 DynaRuleItemEdit
       - fillRelateParamPropName (isrelatparam=true 时反查 propKey→propName)
       - fillSourceEntityPropName (valsourcetype=1 时反查 sourcepropkey→sourcepropname)
       - presetView (issyspreset=true 时强制 VIEW + 隐藏 enumbar)
       - disableEnumEntry (datatype=enum 时锁已有 value 列)

4. registerListener (#7 DynaRuleItemEdit)
   - entitytype.BeforeF7Select / relatruleparam.BeforeF7Select / sourceentitytype.BeforeF7Select
   - enumbar.ItemClick
   - relatpropname.Click / sourcepropname.Click
```

### 3.3 用户填字段（前端联动）

```
用户填 datatype = enum
   ↓
DynaRuleItemEdit.propertyChanged (datatype 监听)
   - 不是 org → entitytype = null
   ↓
formRule 4ZRHZU5CYXJR (datatype=enum) 显示 entryentity + enumbar
   ↓
用户在 enumbar 点【新增】(opKey = newentry)
   ↓
平台默认 newEntry · 加 1 行空 entryentity
   ↓
用户填 entryentity.value = "MALE" + displayvalue = "男"
   ↓
用户填 valsourcetype = 1 (实体取值)
   ↓
formRule 4ZRHJC9QK0/A 显示 sourceentitytype/sourcepropkey/sourcepropname
   ↓
用户在 sourceentitytype 点 F7
   ↓
DynaRuleItemEdit.beforeF7Select (sourceentitytype 监听)
   - QFilter("modeltype", "in", [BaseFormModel, BillFormModel])
   ↓
用户选 "hrpi_person" (人员)
   ↓
用户点 sourcepropname
   ↓
DynaRuleItemEdit.click (sourcepropname 监听)
   - 校验 sourceentitytype 已选
   - showFieldSelF7("hrpi_person", CALLKEY_SELSOURCEENTITYPROP)
   - 弹 hrcs_choosefield_page Modal · paramEntityName="hrpi_person" · param_ifShowForDynaRule="true"
   ↓
用户在子页面选属性 "gender"
   ↓
子页面关闭 → DynaRuleItemEdit.closedCallBack
   - actionId == CALLKEY_SELSOURCEENTITYPROP
   - res.get(0).getPrimaryKeyValue() = "gender||性别"
   - 切 || → infos[0]="gender" · infos[1]="性别"
   - setValue("sourcepropkey", "gender")
   - setValue("sourcepropname", "性别")
```

### 3.4 用户点保存（save 链路）

```
用户点【保存】（工具栏 bar_save）
   ↓
DynaRuleItemEdit.beforeItemClick
   - key == "bar_save"
   - dataType = "enum" · 调 checkEnumEntry()
       - entryentity 不空 ✓
       - 无空行 ✓
       - value 不重 ✓
       - displayvalue 不重 ✓
   - 通过 → clearUnMustData()
       - dataType=enum → datatype 不在 (bd, org) → entitytype=null
       - isrelatparam=false → relatruleparam=null + relatpropkey=null
       - valsourcetype=1 → mserviceapp=null + mserviceclass=null
   ↓
进入 OP 链（save opKey · 6 标品 OP）
   ↓
1. CodeRuleOp.onAddValidators (注册 numberValidator) · 自动生成 number 字段（PR-006）
2. BdVersionSaveServicePlugin.endOperationTransaction · 写 _l 多语言子表 + name 历史
3. HRBaseDataStatusOp.onAddValidators · 状态校验
4. HRBaseDataLogOp.beforeExecute + afterExecute · 操作日志埋点 + 落库
5. HRBaseDataEnableOp.beforeExecute · enable 字段管理
6. HRBaseOriginalOp.beforeExecute · 原始值记录（orinumber/oristatus/oriname）
   ↓
事务提交（commit）→ 写主表 t_hrcs_dynaruleitem + 子表 t_hrcs_dynaruleitemenum
```

---

## 四、删除参数项的完整链路（重点）

### 4.1 列表批量删除

```
列表选 N 条 · 点【删除】(opKey = delete)
   ↓
进入 OP 链（4 标品 OP）
   ↓
1. CodeRuleDeleteOp.beforeExecute · 平台编码规则清理（enabled=True）
2. HRBaseDataStatusOp.onAddValidators · 单据状态校验
3. HRBaseDataLogOp.beforeExecute · 操作日志埋点
4. DynaItemDeleteOp.onPreparePropertys (声明读 name)
   ↓
DynaItemDeleteOp.onAddValidators · 注册 DynaItemDelValidator
   ↓
DynaItemDelValidator.validate
   ↓
对每行 itemId · 调 DynaSchemeServiceHelper.queryRelDynaScheme(itemId)
   ↓
得到所有引用本参数项的 dynascheme[]
   ↓
schemeArr.isEmpty() → 静默通过
schemeArr.notEmpty() → addErrorMessage(row, "规则参数项 [%s] 被 N 个动态授权方案引用，请先解除引用再删除")
   ↓
通过 → 走标品删除 + DB DELETE 主表 + 子表
   ↓
HRBaseDataLogOp.afterExecute · 落日志
```

### 4.2 单据态删除枚举行（deleteentry）

这是 dynaruleitem 特有的子操作 · 不是 delete · 是 deleteentry：

```
单据态打开规则参数项 (datatype=enum)
   ↓
用户在 entryentity 选某行 · 点 enumbar 工具栏【删除】(opKey = deleteentry)
   ↓
DynaRuleItemEdit.beforeDoOperation (operateKey == deleteentry)
   ↓
取当前 dataEntity.id
   ↓
调 DynaSchemeServiceHelper.queryRelDynaScheme(itemId) 反查所有引用本参数项的方案
   ↓
ArrayUtils.isEmpty(schemeDynArr) → 直接 return（无引用 · 让平台默认 deleteentry 走）
   ↓
有引用 → 取选中行 value 集合 (getSelEnumEntryValSet)
   ↓
checkRelSchemeVal(schemeDynArr, enumValSet)
   - 对每个 dynascheme · parseObject(condition).getJSONArray("conditionList")
   - 对 conditionList 每个 paramRel.value · 看是否在 enumValSet
   - 命中则加进 relEntryVals
   ↓
relEntryVals.notEmpty()
   → showTipNotification("枚举值被动态授权方案引用，不允许删除：%s。")
   → args.setCancel(true)（前端不再走平台 deleteentry · 行不删）
relEntryVals.isEmpty()
   → 通过 → 平台默认 deleteentry 删行
```

**业务约束**：
- 一旦枚举值被任意 dynascheme.condition 引用 · 不能删除该行
- 但可以删除"未被引用"的同表其他行（FormPlugin 只过滤选中行 · 不一刀切阻断整个 deleteentry）
- 这是用户手感最好的设计（多选删除时只阻断真冲突的行）

---

## 五、新增枚举行（newentry）的链路

```
单据态打开 (datatype=enum)
   ↓
用户点 enumbar【新增】(opKey = newentry)
   ↓
DynaRuleItemEdit.registerListener 在初始化时已挂 enumbar.ItemClickListener
   - 但 newentry 没特别拦截 → 走平台默认
   ↓
平台默认 newEntry · 加 1 行 entryentity
   ↓
用户填新行 value 和 displayvalue
   ↓
用户点【保存】（bar_save）
   ↓
（同 3.4 路径）
```

---

## 六、关联参数项（isrelatparam）的特殊流程

### 6.1 业务背景

"关联参数项" 是 dynascheme 规则的**外键级联**能力：A 参数项基础资料 = 部门 · B 参数项 = 部门下的"科员级以上"员工 · B.isrelatparam=true · B.relatruleparam=A · B.relatpropkey="员工.职级"。dynascheme 配规则时 · 选 A 选了"研发部" · B 就只能在研发部的员工属性范围里选条件值。

### 6.2 用户填关联参数项

```
用户勾【关联参数项】（isrelatparam = true）
   ↓
formRule 4ZR5YQFF=KLX 显示 relatruleparam + relatpropkey + relatpropname
   ↓
用户点 relatruleparam F7
   ↓
DynaRuleItemEdit.beforeF7Select (relatruleparam 监听)
   - QFilter("isrelatparam", "=", false) · 排除已经是关联参数项的（避免链式关联）
   - QFilter("datatype", "in", ["bd", "org"]) · 仅选实体维度的
   ↓
用户选了主参数项 "部门" (datatype=bd, entitytype=部门基础资料)
   ↓
用户点 relatpropname
   ↓
DynaRuleItemEdit.click (relatpropname 监听)
   - 校验 relatruleparam 已选 ✓
   - 取 relateRuleParam.entitytype.id (主参数项的实体类型 = 部门基础资料 number)
   - showFieldSelF7(部门基础资料 number, CALLKEY_SELRELENTITYPROP)
   - 弹 hrcs_choosefield_page · paramEntityName=部门基础资料 number
   ↓
用户在子页面选属性 "manager" (部门经理)
   ↓
DynaRuleItemEdit.closedCallBack
   - actionId == CALLKEY_SELRELENTITYPROP
   - 切 || → relatpropkey="manager" · relatpropname="部门经理"
```

→ B 参数项现在配置完成：B 关联 A · 当 dynascheme 选了 A=某部门 · B 的可选值就跟该部门 manager 联动。

---

## 七、跨场景流程对比（dynaruleitem 与 dynascheme）

| 流程阶段 | dynaruleitem | dynascheme | 差异 |
|---|---|---|---|
| **数据模型** | BillFormModel + entry · 非 HisModel | BillFormModel + entry × 6 · HisModel 时序 | 本场景简单很多 |
| **工作流** | save / submit / audit / enable / disable | save / submit / audit / **confirmchange** / **change** / enable / disable | 本场景无变更工作流 |
| **关键校验** | save 前枚举校验 / delete 前下游引用阻断 | save 前规则 condition 校验 / delete 前级联清理 5 表 | 校验维度不同 · dynascheme 复杂 |
| **下游引用** | dynascheme.condition.JSON 引用本表 | hrcs_userrolerelat (sourcetype=4) 由权限重算落 | 直接对接元数据级 vs 运营数据级 |
| **BEC 事件** | 标品 0 处发 · ISV 自建 | 标品 0 处发 · ISV 自建 | 同 |
| **HR 准入闸** | HRAdminStrictPlugin（共用 · 同插件） | HRAdminStrictPlugin（共用） | 完全相同 |
| **删除引用阻断** | DynaItemDelValidator + FormPlugin deleteentry 双层 | DynaAuthSchemeListPlugin.afterDoOperation 级联清 5 表 | 本场景"前置阻断" · dynascheme "事后清理" |

→ ISV 在本场景做扩展 · 主要参考 dynascheme 的 CS-01/CS-02/CS-03/CS-04 套路 · 但 **CS-05 BEC 套路要重新 grep 实证**（已做 · 0 命中）· **HisModel 相关认知（PR-008/PR-009）不要带入本场景**。

---

## 八、性能考量

### 8.1 deleteentry / delete 时的反查性能

`DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` 性能依赖系统中 dynascheme 数量 N。

| dynascheme 数量 | 平均响应（推算） | 业务影响 |
|---|---|---|
| < 50 | < 100ms | 无感 |
| 100-500 | 200-500ms | 用户可感（点删除后等半秒） |
| > 1000 | > 1s | 明显卡顿 · 需要后端优化或加缓存 |

→ 大客户（dynascheme 1000+）做"批量删除规则参数项" 操作时 · 删 N 条 → N 次 queryRelDynaScheme → 总耗时 N×K（K=单次反查耗时）· 可能超时。建议生产环境监控这个反查性能。

### 8.2 afterBindData 的字段映射查询

`RolePermLogServiceHelper.getEntityFieldMap(entityNumber)` 拿目标实体所有字段 key→name 映射 · 是元数据查询 · 平台有缓存 · 响应快（< 50ms）。但如果 ISV 给 entityNumber 传错值 · 会返回空 Map · UI 上 propname 显示空。

---

## 九、已知卡点 / 边界场景

1. **datatype 改后旧字段不会立即清** —— 必须等到 bar_save 才走 clearUnMustData 净化。如果用户切换很多次 · 中间状态可能有"幽灵值"在 model 里 · UI 不显示但 model 有值。这通常无影响（save 时会清）· 但 ISV 自建 propertyChanged 监听时要注意
2. **datatype=enum + valsourcetype=2** 的边界状态 · 业务上是否合法？标品 clearUnMustData 不直接拦 · 但实际语义上"枚举型规则参数项 + 微服务取值"是矛盾的（枚举的取值就是枚举行 · 不需要微服务）· 业务侧应该自我约束
3. **issyspreset=true 的预置参数项 disable 后再 enable** · enable 链不阻断 · 行为正常（enable 在元数据层不受 issyspreset 保护）
4. **复制（copy）行为不明确** —— 没找到本场景 ListPlugin 反编译实证 · copy 行为走平台默认（推断会复制主表行 + entryentity 子表行 · 但需业务验证）
