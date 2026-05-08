# 变更影响面 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + scene_doc.json 34 字段 + form_lifecycle_rules.json 26 生命周期方法
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、改动影响矩阵

### 1.1 核心字段变更影响

| 改什么 | 影响范围 | 级联动作 | 风险等级 |
|---|---|---|---|
| **`datatype`** 从 bd 改 enum | entryentity 从隐藏变显示 · entitytype 清空（FP_PC2）· 原有的 entitytype 值丢失 | clearUnMustData 在 save 时清空 entitytype · 历史数据 entitytype 仍残留在 DB（被清空仅限 model 层） | **中** |
| **`datatype`** 从 bd 改 org | entitytype 强制 = `haos_adminorghrf7`（FP_PC1）| 用户无法选其他组织实体 | **低**（标品设计如此） |
| **`datatype`** 从 enum 改 bd | 所有枚举子表行在 save 时被 `clearUnMustData()` 清空（`deleteEnumEntry()`）| **不可逆** —— DB 里的枚举行在 save 时被 DELETE · 如果 dynascheme 有引用这些 value 的 condition，会脱钩 | **高** |
| **`valsourcetype`** 从 1 改 2 | sourceentitytype + sourcepropkey 清空 · mserviceapp + mserviceclass 变为必填 | 原有的实体属性值来源配置丢失 · 需要重新配微服务 | **中** |
| **`isrelatparam`** 取消勾选 | relatruleparam + relatpropkey + relatpropname 清空（clearUnMustData）| 已有的属性关联配置丢失 | **中** |
| **`entryentity.value`** 修改已有行 | 已有行的 value 被 `FP_ABD4` 锁住（`setEnable(false)`）—— **UI 层不允许改** | 如果绕过 UI 直接写 DB 改了 value · 下游 dynascheme.condition 引用脱钩 | **极高** |
| **`entryentity.value`** 新增行 | 无影响 —— 新行 value 列可编辑 | dynascheme 可以引用新 value | **低** |
| **`entryentity`** 删除行 | deleteentry 操作：标品 `FP_BDO1` 查 dynascheme.condition 引用 · 命中则阻断 | **被引用则不允许删** —— 如果绕过 UI 直接删 DB，下游脱钩 | **高** |
| **`name`** 变更 | BdVersionSaveServicePlugin 写名称历史子表 `_n_h` · 不影响下游引用（下游引用用 id · 不用 name） | 仅影响 UI 显示名 | **低** |

### 1.2 状态/启禁用变更影响

| 操作 | 影响范围 | 标品行为 | ISV 注意 |
|---|---|---|---|
| **disable** 某规则参数项 | 被禁用的参数项不再出现在 dynascheme PermFilter 控件可选列表 · 但**已配置的 dynascheme.condition 仍然引用它** | 标品 disable 只挂 HRBaseDataLogOp · **不清理下游引用** · 不作失效处理 | dynascheme 评估 condition 时可能因为 entitytype 被禁用而查不到数据——这是静默失效 |
| **enable** 某规则参数项 | 恢复可选 | 仅 HRBaseDataLogOp · 无校验 | 不需要检查 dynascheme 引用——enable 是恢复 |
| **delete** 某规则参数项 | dynascheme.condition JSON 中的 paramId 引用脱钩 | `DynaItemDelValidator` 查被引用数 · 被引用 >0 则阻断 | ISV 自建表引用不会被标品检查——必须 CS-04 自建 Validator 并行检查 |
| **issyspreset** 被错误修改 | 预置参数项的编辑行为变化 | FP_ABD3 强制 VIEW + 隐藏 enumbar · 但 issyspreset 字段本身 `isvCanModify=false`（元数据保护）· **UI 不允许改** | 如果 DB 直改 issyspreset=false · 预置参数项变成可编辑 · 可能破坏标品内置规则逻辑 |

### 1.3 entitytype 硬编码变更风险

**实证**：DynaRuleItemEdit.propertyChanged L144-L150

```java
if (V_DATATYPE_ORG.equals(newDataType)) {
    this.getModel().setValue(F_ENTITYTYPE, "haos_adminorghrf7");
}
```

| 场景 | 风险 |
|---|---|
| 用户先把 datatype 改成 org（entitytype 强制 haos_adminorghrf7）· 再改成 bd · entitytype 清空 | entitytype 需要重新选——之前的值丢失 |
| ISV 在 propertyChanged 里覆盖 entitytype 为其他值 | 标品 DynaRuleItemEdit 在 ISV 之前执行 · ISV 后覆盖可行 · 但**下次打开表单时** afterBindData 不会纠正（因为没有反写逻辑） |
| datatype=org 后用户不保存直接关闭 | entitytype 只在 model 层被写过 · DB 不变 |

---

## 二、删除影响的完整分析

### 2.1 标品阻断点

```
delete 操作
  → CodeRuleDeleteOp (编码资源释放)
  → HRBaseDataStatusOp (状态校验)
  → HRBaseDataLogOp (日志埋点)
  → DynaItemDeleteOp.onAddValidators → DynaItemDelValidator.validate()
        ↓
    调 DynaSchemeServiceHelper.queryRelDynaScheme(itemId)
        ↓
    返回非空 → addErrorMessage("规则参数项 [name] 被 N 个动态授权方案引用，请先解除引用再删除。")
        ↓
    返回空 → 标品允许删除
```

### 2.2 标品未覆盖的引用

标品 `DynaItemDelValidator` **只查 hrcs_dynascheme**。以下引用不会被检查：

- ISV 自建表引用了 `hrcs_dynaruleitem.id`
- 其他 HR 模块的条件表达式引用了本表（非 dynascheme）
- 报表/BI 的配置引用了本表数据

ISV 必须通过 CS-04 自建 Validator 并行检查。

### 2.3 deleteentry 的子行影响

```
deleteentry 操作（FormPlugin 端 · 不走 OP 链）
  → DynaRuleItemEdit.beforeDoOperation(operateKey=="deleteentry")
        ↓
    DynaSchemeServiceHelper.queryRelDynaScheme(itemId) → 遍历所有 scheme
        ↓
    解析每个 scheme.condition JSON → conditionList[].value
        ↓
    命中选中行的 value → setCancel(true)
        ↓
    未命中 → 允许删除该枚举行
```

**标品不检查的**：ISV 自建表的枚举值引用。需要 ISV 在 beforeDoOperation 自建拦截。

---

## 三、历史/审计影响

### 3.1 操作日志

所有 opKey（save/delete/submit/audit/enable/disable 等）都走 `HRBaseDataLogOp` 写 `hbp_dataeditlog` 表。**操作日志不记录具体字段变更值**——只记录 `操作类型 + 数据 id + 操作人 + 时间`。

### 3.2 名称版本历史

`BdVersionSaveServicePlugin` 在 name 变更时写 `t_hrcs_dynaruleitem_n_h` 历史子表。**这只记录 name 字段的历史**——不是完整的字段级审计。

### 3.3 非 HisModel · 无时序版本

本场景不是 HisModel（grep 0 命中 boid/iscurrentversion）。**改任何字段都直接覆盖**——没有变更单据、没有 confirmchange 工作流、没有 hisversion 多版本。如果需要完整字段级审计，ISV 必须自建。

---

## 四、跨场景影响

| 改动 | 影响的场景 | 可见性 |
|---|---|---|
| 新增/修改/删除 规则参数项 | `hrcs_dynascheme`（动态授权方案）—— PermFilter 控件实时加载本表数据 | 实时（F7 查询 · 无缓存） |
| 修改 entryentity.value | `hrcs_dynascheme.condition.conditionList[].value` 引用脱钩 | 静默（condition 评估时值不匹配 · 规则静默失效） |
| disable 参数项 | `hrcs_dynascheme` —— 参数项不再出现于可选列表 | 实时 |
| delete 参数项（被引用的）| `hrcs_dynascheme` —— 标品阻断 · 不允许删 | N/A（被阻断） |

**关键认知**：dynaruleitem 是 dynascheme 的"字典数据"。字典变更直接影响使用方，但**标品没有任何推送/通知机制**（0 处 BEC 发布）——使用方只能通过 F7 查询感知变更。ISV 如需要主动通知，走 CS-05。

---

## 五、生产高风险操作 Top 5

| 排名 | 操作 | 为什么高风险 | 防护 |
|---|---|---|---|
| 1 | **DB 直改 entryentity.value**（绕过 UI） | UI 已锁 value 列 · DB 直接改会导致 dynascheme.condition 引用脱钩 | 绝不允许 DB 直改——这条规则是红线 |
| 2 | **datatype 从 enum 改成 bd 并保存** | clearUnMustData 会 DELETE 所有子表行 · 不可逆 | save 前确认 datatype 不计划切换回 enum |
| 3 | **删除被 dynascheme 引用的参数项**（绕过 UI 删除） | 下游 condition JSON paramId 指向不存在的数据 | 标品 Validator 在 UI 层阻断——但绕过操作（如 OpenAPI batchDelete）可能不触发 Validator |
| 4 | **disable 被大量方案引用的参数项** | dynascheme 静默失效 · 权限规则不再生效 | disable 前先跑 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` 评估影响 |
| 5 | **修改标品预置参数项的 issyspreset/entitytype（DB 直改）** | 破坏标品内置动态权限规则 | issyspreset=true 的行 UI VIEW 保护——不允许改 |
