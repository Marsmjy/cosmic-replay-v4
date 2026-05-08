# 异常诊断 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类实证 + 业务场景分析
> **confidence**: verified
> **异常数**: 4 个

---

## 异常一 · adminorgtypestd 字段在已被引用时不可修改

### 现象

用户打开已有行政组织类型的编辑视图，"类型归属"（adminorgtypestd）字段显示为灰色，无法点击输入。其他字段正常可编辑。

### 判断

**这是正确行为，不是 Bug**。

### 原因

AdminorgtypeEditPlugin.beforeBindData 实证（R-2）：当 `baseDataCheckReference(id).isRefence() == true` 时，执行 `getView().setEnable(false, ["adminorgtypestd"])`。

字段被禁用的原因：当前 adminorgtypestd 所对应的 haos_adminorgtypestd 记录已被其他实体引用，修改会破坏下游数据一致性（orgpattern 联动链断裂 + 报表语义变化）。

### 诊断步骤

```
1. 确认是 adminorgtypestd 字段灰色（不是其他字段）
2. 确认记录 id != 0（已存在记录 · 不是新建）
3. 查询 haos_adminorgtypestd 被引用情况：
   select * from t_haos_adminorgtype where fadminorgtypestdid = <当前值>
4. 确认有其他 adminorgtype 记录也使用了同一个 adminorgtypestd 值
```

### 处理方案

- 业务上这是设计保护，不建议绕过
- 如果必须修改 adminorgtypestd，需先解除下游引用（修改引用方的 adminorgtypestd 值）
- 若是 ISV 代码触发（后端 OP 强行修改），必须手动同步修正 orgpattern（R-3 联动不会自动触发）

---

## 异常二 · HIES 导入时 orgpattern 为 null 或不正确

### 现象

通过 HIES 导入行政组织类型数据后，发现部分记录的 orgpattern 字段为 null 或与导入文件原值不一致。

### 可能原因分析

**情况 A：标品修正逻辑正常触发（预期行为）**

AdminOrgTypeSaveOp.beginOperationTransaction 检测到 importtype 非空，按 AdminOrgTypeStdEnum 枚举修正了 orgpattern。导入文件中的 orgpattern 值被覆盖是正常的。

**情况 B：adminorgtypestd 未正确填写**

导入文件中 adminorgtypestd 字段为 null 或无效值，AdminOrgTypeStdEnum.getOrgPatternIdById(null) 返回 null，导致 orgpattern 未被修正。

**情况 C：AdminOrgTypeSaveOp 修正逻辑异常**

AdminOrgTypeStdEnum 枚举中没有对应的 adminorgtypestd → orgpattern 映射（数据超出枚举定义范围）。

### 诊断步骤

```
1. 检查导入文件中 adminorgtypestd 字段是否有值（不为空）
2. 检查 adminorgtypestd 的值是否是 haos_adminorgtypestd 实体中已有的合法记录
3. 查看 HIES 导入日志（show_import_record_hr）确认是否有警告或错误
4. 确认 bos_org_pattern 实体中 AdminOrgTypeStdEnum 对应的记录是否存在
```

### 处理方案

- **情况 A**：这是正确行为，不需要处理
- **情况 B**：修正导入文件，确保 adminorgtypestd 字段有正确的合法值
- **情况 C**：联系标品 HR 支持，或使用手动修正（保存时会触发 R-3 联动）

### ISV 开发注意

- ISV 扩展 HIES 导入修正逻辑（CS-04）时，必须加 importtype 守卫，且不能继承 AdminOrgTypeSaveOp（PR-001）
- AdminOrgTypeSaveOp 的修正发生在 beginOperationTransaction 阶段（事务内），ISV 并列挂 OP 要注意执行顺序

---

## 异常三 · issyspreset 预置数据保护（正常拦截）

### 现象

用户尝试修改或删除某条行政组织类型记录，系统提示无法操作，或字段全部不可编辑。

### 判断

**这是正确行为，是系统预置数据保护机制**。

### 原因

R-1 规则：`issyspreset = true` 的预置数据受平台保护。虽然 listRules formRule（ruleId: 0SWB2VGDUNX+）的 `enabled=false`（已禁用），但以下保护仍然有效：

1. `issyspreset` 字段 `isvCanModify=false`：ISV 无法通过 modifyMeta 修改此字段
2. 字段 `minefield=red`：平台层保护
3. 代码层保护：ISV 加的 CS-02 校验逻辑显式拦截 issyspreset=true 的删除请求

### 诊断步骤

```
1. 查看当前记录的 issyspreset 字段值
   select fissyspreset from t_haos_adminorgtype where fid = <id>
2. 确认 issyspreset = 1（预置数据）
3. 这是平台保护 · 正常行为
```

### 处理方案

- 预置数据是平台/标品内置的，设计上不允许修改
- 若业务确实需要类似的组织类型但语义不同，建议新建自定义数据（issyspreset=false）
- **不要尝试通过后端直接修改 issyspreset 字段**（isvCanModify=false · 平台拒绝 · 即使绕过也破坏数据一致性）

---

## 异常四 · propertyChanged 联动后 orgpattern 不更新

### 现象

用户在编辑视图修改了 adminorgtypestd 字段，但 orgpattern 字段没有自动更新。或者 ISV 实现了类似联动但出现了值反复刷新（死循环）的问题。

### 可能原因分析

**情况 A：adminorgtypestd 枚举映射为 null**

`AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)` 对新的 stdId 返回 null（该 stdId 不在枚举映射中），导致 setValue("orgpattern", null) 清空了 orgpattern。

**情况 B：标品 propertyChanged 未被触发**

ISV 加了一个并列挂插件，RowKey 排在 AdminorgtypeEditPlugin 之前，且没有调 super.propertyChanged(e)，导致标品联动逻辑未执行。

**情况 C：ISV 自定义联动死循环（PR-004 违反）**

ISV 实现了类似联动，在 propertyChanged 中调 setValue 时未使用 beginInit/endInit，导致 setValue 再次触发 propertyChanged，陷入死循环。

### 诊断步骤

```
情况 A：
  - 检查 adminorgtypestd 选择的记录是否在 AdminOrgTypeStdEnum 枚举范围内
  - 查看 haos_adminorgtypestd 记录的 id 是否是平台预置值

情况 B：
  - 检查 ISV 插件的 RowKey 顺序（是否排在标品 #9 之前）
  - 确认 ISV 的 propertyChanged 是否调了 super.propertyChanged(e)

情况 C（死循环）：
  - 查看浏览器 console 或后端日志是否有无限递归的 propertyChanged 调用
  - 检查 ISV 代码是否在 propertyChanged 里直接调 setValue 而未用 beginInit/endInit
```

### 处理方案

- **情况 A**：确认 adminorgtypestd 选值是否合法（需在枚举映射范围内）；如需支持新的 adminorgtypestd，联系 HR 标品扩展枚举
- **情况 B**：确保 ISV 插件在调 super.propertyChanged(e) 后再追加自定义逻辑
- **情况 C**：在 ISV 的 propertyChanged 中用 beginInit/endInit 包裹 setValue（PR-004 铁律）

```java
// ✅ 正确写法（ISV 扩展联动必须这样）
this.getModel().beginInit();
this.getModel().setValue("target_field", value);
this.getModel().endInit();
this.getView().updateView("target_field");

// ❌ 错误写法（导致死循环）
this.getModel().setValue("target_field", value);  // 无 beginInit/endInit 包裹
```

### ISV 扩展注意

- 禁止继承 `AdminorgtypeEditPlugin` 来"增强"联动（PR-001）
- ISV 实现类似 propertyChanged 联动必须遵循 PR-004（死循环防护）
- 标品 AdminorgtypeEditPlugin.propertyChanged 未用 beginInit（框架内部机制防止重入），但 **ISV 代码没有这个保障，必须显式加**
