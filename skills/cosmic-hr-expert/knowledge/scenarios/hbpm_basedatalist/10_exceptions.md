# 异常诊断 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 基于反编译实证 + 场景特征分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景特有陷阱

### TRAP-BD-01 · 误把 hbpm_basedatalist 当作具体岗位字典表

**症状**：ISV 在 hbpm_basedatalist 主列表页加字段，发现每行只有"页面标识"类数据，查不到岗位级别/序列/分类的具体条目。

**原因**：`hbpm_basedatalist` 是**分组入口聚合页**，每行代表一类基础资料（如"岗位级别"这一类），而不是具体的岗位级别条目（如"M1/M2/M3"）。具体条目存放在各子列表 form 中（由 pagekey 指向）。

**解决**：在对应子 form（通过 pagekey 查到的 formId）上进行扩展，而非在 hbpm_basedatalist 主表上操作。

---

### TRAP-BD-02 · afterBindData 联动时未跳过 issyspreset=true 行

**症状**：ISV 加了 afterBindData 联动逻辑，对 ISV 扩展字段 setValue；打开系统预置数据行时报错（"VIEW 状态下不允许 setValue"）。

**原因**：`PositionBasedataEdit` 先执行（#6）将 issyspreset=true 的行切为 VIEW 状态；ISV 插件后执行（#7+）时再 setValue 触发异常。

**解决**：
```java
@Override
public void afterBindData(EventObject e) {
    super.afterBindData(e);
    // ⭐ 必须先检查 issyspreset
    if (this.getView().getModel().getDataEntity().getBoolean("issyspreset")) {
        return;  // 预置数据不执行联动
    }
    // ISV 自定义联动逻辑
    ...
}
```

---

### TRAP-BD-03 · 在 BaseFormModel 查询时加了 iscurrentversion 过滤

**症状**：ISV 的 Validator 或 OP 中写了 `.and(new QFilter("iscurrentversion","=",Boolean.TRUE))`，运行时抛 FieldNotFoundException 或查询结果为空。

**原因**：`hbpm_basedatalist` 是 BaseFormModel（非 HisModel），主表无 `iscurrentversion` 字段。

**解决**：BaseFormModel 直接用 id 查，不加任何版本过滤：
```java
QFilter qf = new QFilter("id", "=", basedataId);  // ✅ BaseFormModel 直接查
// ❌ .and(new QFilter("iscurrentversion","=",Boolean.TRUE))  // 错！BaseFormModel 无此字段
```

---

### TRAP-BD-04 · 继承 PositionBasedataEdit 或 HRBDGroupList 导致标品升级编译失败

**症状**：标品升级后，ISV 包编译报错，提示 `PositionBasedataEdit.afterBindData` 方法签名不匹配。

**原因**：`PositionBasedataEdit` 是场景专属类，标品升级可能修改其方法签名或内部实现；继承后 ISV 代码耦合到标品内部。

**解决**：严格遵守 PR-001，ISV 只能继承白名单父类（`HRDataBaseEdit`），并列挂而非继承。

---

### TRAP-BD-05 · 分组跳转失败 · "请检查页面标识是否正确"

**症状**：用户点击基础资料分类行的超链接，出现提示"请检查页面标识是否正确"，无法跳转到子列表。

**原因**：`HRBDGroupList.getPageKeyById` 从数据库查 `pagekey` 字段为空或无效，`showForm` 时 formId 不存在。

**排查步骤**：
1. 检查当前行的 `pagekey`（fpagekey）字段值是否为空
2. 检查 pagekey 对应的 formId 是否在苍穹开发平台中存在
3. 检查 ISV 是否意外修改了 pagekey 字段（isvCanModify=false · 禁止修改）

---

## 二、通用 modifyMeta 陷阱（来自 cosmic_realworld_traps）

- **加字段 key > 24 字符**：数据库列名截断 → 运行时报错（buildmeta_traps.md）
- **EmployeeField 类型**：OpenAPI 不支持（modifymeta_param_names_and_hr_sdk_limits.md）
- **fieldType 传错名**：传 `dataType` 而非 `fieldType` → 静默走 TextField（modifymeta_param_names_and_hr_sdk_limits.md）
- **parentScope 写错**：加字段到不存在的子实体 → 报"scope 不存在"
- **重复 add 相同 key**：第二次 add 同名字段不报错，但字段重复（需先 remove 再 add）

---

## 三、插件 RowKey 顺序陷阱

```
错误场景：ISV 插件 RowKey 排在 PositionBasedataEdit(#6) 之前

结果：ISV afterBindData 先执行，此时 issyspreset 还未触发 VIEW 状态
     → ISV 的字段初始化逻辑在预置数据上成功执行
     → PositionBasedataEdit 后执行，切到 VIEW 状态
     → 用户界面看起来正常，但 ISV 字段值已被错误写入

正确做法：ISV 插件 RowKey 排在 PositionBasedataEdit 之后（PR-002 · 按需调整顺序）
```
