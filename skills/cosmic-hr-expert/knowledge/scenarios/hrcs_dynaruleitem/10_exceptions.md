# 异常诊断 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + cosmic_realworld_traps 权威坑位 + scene_doc.json 元数据实证
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI + `kb_cosmic_buildmeta_traps.md` / `kb_cosmic_addrule_traps.md` / `kb_cosmic_modifymeta_traps.md` (2026-04-28)

---

## 一、来自 cosmic_realworld_traps 的共性陷阱

### 1.1 buildMeta 陷阱（本场景相关）

来源：`kb_cosmic_buildmeta_traps.md`

| 陷阱 | 本场景表现 | 后果 |
|---|---|---|
| `parentId` 为空时兜底到 bos 基础资料模板 | 如果 ISV 用 buildMeta 建 ISV 扩展元数据时不传 parentId，新元数据会继承 bos 基础资料模板，**不会**自动挂到 hrcs_dynaruleitem 的字段集合 | 字段在元数据里定义了但 UI 不显示 · 因为不在正确的父模板下 |
| EmployeeField 不支持（OpenAPI 视作 BasedataField 兜底） | 本场景标品没有 EmployeeField · ISV 如想加员工字段只能走 `BasedataField + hrpi_person + Java 插件` | 加了 EmployeeField 会被降级为 BasedataField · 失去员工 F7 的快捷选择能力 |
| HRMulPositionField / HRMulAdminOrgField OpenAPI 不支持 | 本场景标品没有多选岗位/组织字段 · ISV 加这类字段必须走 IDEA 插件 Web UI 而非 OpenAPI | OpenAPI buildMeta 会将其当作 BasedataField 创建 |

### 1.2 modifyMeta 陷阱

来源：`kb_cosmic_modifymeta_traps.md`

| 陷阱 | 本场景具体风险 | 预防 |
|---|---|---|
| `modifyMeta add field` 参数名是 `fieldType/name/columnName` 不是 `dataType/displayName` | 传错了参数名 → 其他参数被忽略 → 字段类型被当作 TextField 兜底 | 严格按 API 文档填 `fieldType` / `key` / `name` |
| `EmbedFormAp` 假成功 | errorCode=0 但实际未嵌入 | 嵌入后调 `getFormSchema` 二次验证 |
| ISV 归属判定缺失（P0-2） | 两个不同 ISV 都对 hrcs_dynaruleitem 做 `modifyMeta add field`，后一个会覆盖前一个的字段注册 | 目前平台未补齐——ISV 之间需要协调 |

### 1.3 addRule 陷阱

来源：`kb_cosmic_addrule_traps.md`

| 陷阱 | 本场景具体风险 |
|---|---|
| preCondition 不能用 `==''`（空字符串判断） | 本场景 formRule 的 preCondition 都是 `datatype='bd'` / `isrelatparam=true` 等具体值 · 没有 `==''` 的先例——ISV 加新 formRule 时也不要用 `==''` |
| PascalCase actionType | 如果 ISV 加 bizRule（非 formRule），actionType 必须 PascalCase |
| 坏规则不清理会持续报错 | 加了规则后如果运行时报错 · 需要主动删掉坏规则（标品不会自动清理） |

---

## 二、本场景特有陷阱

### 2.1 datatype=org 时 entitytype 被硬编码覆盖

**症状**：用户在 UI 上选 datatype=org，entitytype 自动变成 `haos_adminorghrf7`，用户无法选其他组织实体。

**原因**：DynaRuleItemEdit.propertyChanged 硬编码写入 `haos_adminorghrf7`。

**ISV 常见错误**：在 propertyChanged 里重新 setValue entitytype 为自定义值——保存后下次打开表单，afterBindData 不会纠正（因为 afterBindData 没有 entitytype 回填逻辑）—— entitytype 留在 DB 的是 ISV 的值，但下次用户切 datatype 时又会被标品覆盖。

**正确做法**：如果确实需要支持其他组织实体，在 propertyChanged 里**并列监听** `datatype="org"` 时写入自定义 entitytype，并且 afterBindData 也要补一段回填逻辑。但这本质上是跟标品行为不一致的定制——需要业务确认。

### 2.2 被 dynascheme 引用的参数项 disable 后静默失效

**症状**：用户 disable 了一条被 15 个方案引用的规则参数项。没有报错、没有提示。15 个方案的 condition 评估失效——权限规则静默失效。

**原因**：标品 disable 只挂 `HRBaseDataLogOp`（仅记日志），**不检查下游引用**。

**对比 delete**：delete 有 `DynaItemDelValidator` 阻断，但 disable 没有。

**预防**：CS-04 模式下，ISV 可以自建 Validator 在 disable 前检查下游引用并提示用户。如果不需要阻断，至少记录 WARN 日志。

### 2.3 datatype 从 enum 切到 bd 后子表数据不可逆丢失

**症状**：用户创建了一条带 10 个枚举子表行的参数项，后来发现应该用 bd 类型。修改 datatype 为 bd 并保存——再切回 enum 时子表行全没了。

**原因**：`clearUnMustData()` 在 save 时检测 `datatype != "enum"` → 调 `deleteEnumEntry()` 清空所有子表行。这是标品**有意的净化行为**（防止脏数据残留），但对用户来说是不可逆的。

**预防**：在 UI 上做 datatype 切换时弹确认框提示"切换数据类型会清空已有枚举值"。
**ISV 能做到吗**：可以在 propertyChanged 里加 `showConfirm` —— 但标品 propertyChanged 没有 confirm 机制，ISV 需要自建。

### 2.4 listRules OpenAPI 返回的 formRule 中有空 preCondition

**症状**：部分 formRule 在 `probe_snapshot.json` 或 `listRules` API 返回中 preCondition 为空字符串。

**本场景实测**：本场景 5 条 formRule 都有明确的 preCondition（`datatype='bd'` / `isrelatparam=true` 等），没有空 preCondition 的情况。但如果 ISV 通过 `modifyMeta add field` 后再 `addRule`，可能因为字段 key 不匹配导致 preCondition 空。

**预防**：加规则后立即调 `listRules` 验证 preCondition 非空。

### 2.5 DynaItemDelValidator 只查 hrcs_dynascheme · 不查 ISV 自建表

**症状**：ISV 自建表 `t_isv_custom_rule` 引用了 `hrcs_dynaruleitem.id`。用户删除一条参数项时，标品 `DynaItemDelValidator` 只查了 dynascheme 引用——没查到 → 允许删除。自建表的数据脱钩。

**原因**：`DynaItemDelValidator` 只调 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` —— 硬编码了查询范围。

**修复**：CS-04 —— 自建 OP 在 `onAddValidators` 注册 ISV Validator，查自建表引用。ISV Validator 与标品并列，任一报错都阻断。

### 2.6 issyspreset=true 的参数项被误当普通数据修改

**症状**：ISV 插件没有检查 `issyspreset` 字段，对一条 issyspreset=true 的标品预置参数项做了 setValue 修改。

**原因**：`issyspreset` 字段 `isvCanModify=false`（元数据保护），但插件代码里仍可以 `getModel().setValue(...)` 修改。

**标品保护**：FP_ABD3 在 afterBindData 时检测 `issyspreset=true` → `billView.setBillStatus(VIEW)` 强制只读 + `setVisible(false, "enumbar")` 隐藏枚举工具栏。但这个保护在 `afterBindData` 阶段——如果 ISV 在 `afterBindData` 之后调用 setValue，仍然能写入脏数据。

**预防**：ISV 插件在 `afterBindData` 或 `beforeItemClick` 时检查 `dataEntity.getBoolean("issyspreset")`，如果 true 则跳过 ISV 逻辑。

---

## 三、运行时异常诊断清单

| 异常现象 | 可能原因 | 排查入口 |
|---|---|---|
| 新增参数项后 entitytype 被清空 | datatype 被选成了 enum（formRule 隐藏 entitytype + DynaRuleItemEdit 清空） | 检查 datatype 是否有非预期的 formRule 干扰 |
| save 时报"枚举值和枚举名称不能为空" | datatype=enum 但 entryentity 没有行 | DynaRuleItemEdit.checkEnumEntry L307 |
| save 时报"存在相同的枚举值：xxx" | entryentity.value 有重复 | 同上 · focusCell 定位重复行 |
| deleteentry 时报"枚举值被动态授权方案引用" | 这行 value 被 dynascheme.condition 引用了 | DynaRuleItemEdit.checkRelSchemeVal L253 |
| delete 时报"被 N 个动态授权方案引用" | 本条参数项被 dynascheme 引用 | DynaItemDelValidator |
| 打开表单时强制 VIEW 态 · 不能编辑 | 这是 issyspreset=true 的预置数据 | FP_ABD3 · 预期行为 |
| F7 弹窗里看不到某些基础资料 | modeltype 被 beforeF7Select 过滤（entitytype→BaseFormModel / sourceentitytype→BaseFormModel+BillFormModel）| DynaRuleItemEdit.beforeF7Select L178-L190 |
| relatruleparam F7 里看不到某些参数项 | 被 isrelatparam=false 或 datatype not in (bd, org) 过滤 | DynaRuleItemEdit.beforeF7Select L181-L186 |
| relatpropname 点击报"请选择主规则参数项" | 还没选 relatruleparam 就点了 | DynaRuleItemEdit.click L158-L165 |
