# 异常诊断 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于反编译实证 + 场景特征分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景特有陷阱

### TRAP-TT-01 · ISV afterBindData 在 enable="0" 状态下 setValue 报错

**症状**：ISV 并列挂的 FormPlugin afterBindData 中执行 getModel().setValue("${ISV_FLAG}_xxx", val)，打开禁用态数据行时报错。

**原因**：`PositionTplTypeEditPlugin`（#9）先执行，检测 enable="0" 后将页面切换为 VIEW 状态。ISV 插件（#10+）后执行，此时 setValue 在 VIEW 状态下无效或抛异常。

**解决**：
```java
@Override
public void afterBindData(EventObject e) {
    super.afterBindData(e);
    // ⭐ 先检查 enable 状态
    String enable = (String) this.getModel().getValue("enable");
    if ("0".equals(enable)) return;  // 禁用态跳过联动
    // ISV 自定义联动逻辑
    ...
}
```

---

### TRAP-TT-02 · index 唯一性冲突高亮不显示（API 导入场景）

**症状**：通过 API 批量导入岗位模板类型，index 重复时报错，但错误信息模糊，没有高亮字段。

**原因**：`PositionTplTypeEditPlugin.afterDoOperation` 负责高亮字段，但 API 导入不走 FormPlugin，afterDoOperation 不执行。

**解决**：导入前预先计算 index 值，确保无冲突。可通过 HRBaseServiceHelper 查最大 index 后步长 10 递增。

---

### TRAP-TT-03 · 继承 PositionTplTypeSaveOp 破坏 3 个 Validator 注册

**症状**：ISV 继承 `PositionTplTypeSaveOp`，重写了 `onAddValidators`，忘记调 super，导致 `PositionTplCommonValidator`/`PositionTplTypeIndexUniqueValidator`/`CtrlStrategyValidator` 全部失效，数据完整性约束丢失。

**原因**：违反 PR-001，继承了场景专属 OP。

**解决**：严格遵守 PR-001，ISV 只能并列挂新 OP，不继承 PositionTplTypeSaveOp。

---

### TRAP-TT-04 · 继承 AbsOrgBaseOp 导致运行时类加载失败

**症状**：ISV 包部署后，访问 hbpm_positiontpltype 时抛 ClassNotFoundException 或 NoClassDefFoundError。

**原因**：AbsOrgBaseOp 不在 HR SDK 白名单（forbidden），运行时 JVM 找不到该类。

**解决**：使用 `HRDataBaseOp` 作为 OP 父类，禁止使用 AbsOrgBaseOp。

---

### TRAP-TT-05 · PositionTplUtil 不可直接调用（内部 API）

**症状**：ISV 想复用 `PositionTplUtil.isNameEnableRe` 方法实现字段高亮，编译时报"类不在 SDK 白名单"。

**原因**：`PositionTplUtil` 是 hbpm 域内部工具类，不在 HR SDK 白名单，ISV 无法调用。

**解决**：ISV 自建字段高亮工具方法，通过 `getView().setFieldBackground(fieldKey, RED)` 或 `showErrorNotification` 实现。

---

## 二、通用陷阱（来自 cosmic_realworld_traps）

- **fieldType 传错名**：传 `dataType` 而非 `fieldType` → 静默走 TextField
- **加字段 key > 24 字符**：数据库列名截断
- **parentScope 写错**：加字段到不存在的实体报错
- **EmployeeField 类型**：OpenAPI 不支持（HR SDK limits）
