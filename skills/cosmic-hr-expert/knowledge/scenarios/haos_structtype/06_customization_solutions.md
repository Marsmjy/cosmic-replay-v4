# 推荐定制方案 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证 + platform_rules 11 PR
> **confidence**: verified

**禁继承铁律（PR-001）**：以下 7 个类 ISV 绝对禁止继承：
- `StructTypeEditPlugin`（场景专属 FormPlugin）
- `StructTypeListPlugin`（场景专属 List Plugin · ItemClickListener）
- `StructTypeSaveOp`（场景专属 OP · 元数据工厂）
- `StructTypeDisableOp`（场景专属 OP · 级联禁用）
- `StructTypeEnableOp`（场景专属 OP · 元数据工厂）
- `StructTypeDeleteDonothingOp`（场景专属 OP · 级联删除）
- `StructTypeChgNameOp`（场景专属 OP · 改名同步）

---

## CS-01 · 给 haos_structtype 加自定义业务字段

### 需求

业务方说：要给架构类型加上"类型分类"（战略型/职能型/支撑型）标签，用于报表分析。

### 推荐方案

- **扩展对象**：`haos_structtype` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 扩展安全）

### 调用链

```
Step 1: getFormSchema(formNumber=haos_structtype)  // 查目前字段清单 · 防重名
Step 2: modifyMeta({
  formId: "haos_structtype",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "haos_structtype",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_structtypecate",        // ⭐ ISV 前缀防覆盖
      name: {zh_CN: "类型分类", en_US: "Struct Type Category"},
      mustInput: false
    }
  }]
})
Step 3: getFormSchema(haos_structtype)  // 二次验证落库
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖（PR-001）
- ❌ 字段 key > 24 字符 → 数据库列名上限触顶
- ❌ 在 OP 层读取时忘记 onPreparePropertys 声明 → 字段值为 null（PR-010）
- ⚠ metanumsuffix 字段 ISV 不可修改属性 → 会导致元数据命名空间失效

---

## CS-02 · 删除前置校验：自定义引用检查

### 需求

业务方说：要在删除架构类型前检查是否有 ISV 自建的数据引用了它。

### 推荐方案

- 自建 `extends AbstractValidator` 的校验器
- 通过 `extends HRDataBaseOp` + `onAddValidators` 并列挂
- ISV 自建 OP 的 rowkey 必须排在 `StructTypeDeleteDonothingOp` 之前（先校验 ISV 规则）

### 注意

- ❌ 不继承 `StructTypeDeleteDonothingOp`（PR-001）
- ✅ 在 `onAddValidators` 中 `args.addValidator(new TdkwStructtypeDeleteValidator())`
- ✅ 不要直接查 t_haos_structtype，通过 `HRBaseServiceHelper("${ISV_FLAG}_custom_entity")` 查自建实体

---

## CS-03 · 列表扩展自定义列/过滤条件

### 需求

业务方说：要在架构类型列表加一列"类型分类"并支持过滤。

### 推荐方案

- 自建 `extends HRDataBaseList` 的 ListPlugin 并列挂
- 在 `setFilter` 中追加自定义 QFilter 条件
- ❌ 不继承 `StructTypeListPlugin`（PR-001 · 且 ItemClickListener 实现复杂）

---

## CS-04 · 监听架构类型保存后进行额外处理

### 需求

业务方说：每当新建一条架构类型时，需要通知 ISV 自建的系统。

### 推荐方案

- 自建 `extends HRDataBaseOp` 的 OP 插件，挂在 save opKey
- 在 `afterExecuteOperationTransaction` 中实现自定义逻辑
- 通过 `this.getOption().getVariables()` 获取 OP 层传递的变量

### 关键模式

```java
public class TdkwStructTypeSaveNotifyOp extends HRDataBaseOp {
    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("metanumsuffix");
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("enable");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject dataEntity : e.getDataEntities()) {
            // 仅处理新建且启用的记录
            String enable = dataEntity.getString("enable");
            if (!"1".equals(enable)) continue;
            // 自定义通知逻辑
        }
    }
}
```

### 注意

- ⚠ 标品 StructTypeSaveOp 在 afterExecuteOperationTransaction 中建元数据/菜单，ISV OP 排在其后，元数据创建成功后再执行
- ⚠ 检测是否是新建记录可通过 `option.getVariables()` 里的自定义变量（但 ISV 无法读 StructTypeSaveOp 设的变量）→ 可通过 `beginOperationTransaction` 里判断 `fromDatabase=false` 来标记

---

## CS-05 · BEC 反指引（不推荐）

标品在本场景不发 BEC（grep 0 命中）。ISV 不应在本场景订阅 BEC。若需要跨系统通知，应在 CS-04 的 OP 插件中直接调用目标系统 API 或使用平台消息队列，不走 BEC。
