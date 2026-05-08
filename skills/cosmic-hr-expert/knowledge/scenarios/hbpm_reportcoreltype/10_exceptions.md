# 异常诊断 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 基于反编译实证 + 场景特征分析
> **confidence**: verified
> **最后更新**: 2026-04-27

---

## 一、场景特有陷阱

### TRAP-RC-01 · issyspreset=true 时 ISV afterBindData setValue 无效

**症状**：ISV 并列挂的 FormPlugin afterBindData 中执行 getModel().setValue("${ISV_FLAG}_xxx", val)，打开系统预置数据行时，字段值未生效或有 WARN 日志。

**原因**：`PositionBasedataEdit`（#9）先执行，检测 issyspreset=true 后将页面切换为 VIEW 状态（`setBillStatus(BillOperationStatus.VIEW)`）。ISV 插件（#10+）后执行，此时 setValue 在 VIEW 状态下无效。

**解决**：
```java
@Override
public void afterBindData(EventObject e) {
    super.afterBindData(e);
    // ⭐ 先检查 issyspreset
    DynamicObject entity = this.getView().getModel().getDataEntity();
    if (entity.getBoolean("issyspreset")) return;  // 预置数据跳过联动
    // ISV 自定义联动逻辑
}
```

---

### TRAP-RC-02 · orgteamtype 子表全量替换导致数据丢失

**症状**：通过 API 或 Import 保存协作类型时，未传 orgteamtype 字段，导致子表 `t_hbpm_orgteamtype` 被全量清空。

**原因**：平台对 MulBasedataField（orgteamtype）的子表写入模式是**全量替换**（DELETE+INSERT）。API 调用不传该字段时，等同于传空值，触发全量删除不插入。

**解决**：
1. API 保存时，orgteamtype 字段必须与主表数据同时提交（至少一个值，required=true）
2. OP 层 `onPreparePropertys` 必须声明 orgteamtype，才能在事务内读取子表并做校验（PR-010）
3. 不要在 OP 外手动操作 `t_hbpm_orgteamtype` 子表（平台框架已处理）

---

### TRAP-RC-03 · 继承 BaseDataBuOp（hbpm 域）破坏 CtrlStrategyValidator 注册

**症状**：ISV 继承 `kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp`，重写了 `onAddValidators`，忘记调 super，导致 `CtrlStrategyValidator` 失效，控制策略合规校验被绕过。

**原因**：违反 PR-001，继承了场景专属 OP 类。

**解决**：严格遵守 PR-001，ISV 只能并列挂新 OP（父类 HRDataBaseOp），不继承 BaseDataBuOp（hbpm 域或 haos 域均禁继承）。

---

### TRAP-RC-04 · 继承 PositionnBaseDataOrderPlugin 导致双重排序冲突

**症状**：ISV 继承 `PositionnBaseDataOrderPlugin` 并重写 setFilter，导致列表排序逻辑冲突或双重排序产生不期望的结果。

**原因**：违反 PR-001。即使调用 super，继承链导致 setOrderBy 被调用两次，结果取决于最后一次调用的参数。

**解决**：禁止继承 `PositionnBaseDataOrderPlugin`（PR-001）。如需追加排序条件，并列挂独立 ListPlugin，RowKey 排在 #17 之后，在 setFilter 中先读出现有排序条件再拼接。

---

### TRAP-RC-05 · 继承 AbsOrgBaseOp 导致运行时类加载失败

**症状**：ISV 包部署后，访问 hbpm_reportcoreltype 时抛 ClassNotFoundException 或 NoClassDefFoundError。

**原因**：`kd.hr.haos.opplugin.web.AbsOrgBaseOp` 不在 HR SDK 白名单（forbidden），运行时 JVM 找不到该类定义。

**解决**：使用 `HRDataBaseOp` 作为 OP 父类，严禁使用 AbsOrgBaseOp（无论是本场景还是其他 hbpm/haos 域场景）。

---

### TRAP-RC-06 · MulBasedataField 反向查询路径错误

**症状**：ISV 通过 `new QFilter("orgteamtype", "=", orgteamTypeId)` 查询哪些协作类型关联了某组织分类，查询结果为空（但实际有数据）。

**原因**：orgteamtype 是 MulBasedataField，物理存储在子表 `t_hbpm_orgteamtype`，不是主表字段，不能用直字段路径查。

**解决**：
```java
// ✅ 正确：MulBasedataField 通过 .fbasedataid 子表路径查
QFilter qf = new QFilter("orgteamtype.fbasedataid", "=", orgteamTypeId);

// ❌ 错误：直字段查（MulBasedataField 不支持）
QFilter qf = new QFilter("orgteamtype", "=", orgteamTypeId);
```

---

## 二、通用陷阱（来自 cosmic_realworld_traps）

- **fieldType 传错名**：传 `dataType` 而非 `fieldType` → 静默走 TextField
- **加字段 key > 24 字符**：数据库列名截断
- **parentScope 写错**：加字段到不存在的实体报错
- **EmployeeField 类型**：OpenAPI 不支持（HR SDK limits）
- **iscurrentversion 过滤**：BaseFormModel 无此字段，加了直接报错（本场景 PR-009 不适用）
