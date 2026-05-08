# 推荐定制方案 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证 + platform_rules 11 PR
> **confidence**: verified

**禁继承铁律（PR-001）**：以下 4 个类 ISV 绝对禁止继承：
- `PositionTplEditPlugin`（场景专属 FormPlugin · BeforeF7SelectListener）
- `PositionTplListPlugin`（场景专属 List Plugin）
- `PositionTplBuListPlugin`（场景专属 BU 过滤 List Plugin）
- `PositionTplSaveOp`（场景专属 OP · 级联同步 + BEC 发送）

---

## CS-01 · 给 hbpm_positiontpl 加自定义业务字段

### 需求

业务方说：要给岗位模板加上"岗位等级范围"或"适用地区"标签，用于精细化管控。

### 推荐方案

- **扩展对象**：`hbpm_positiontpl` 主实体
- **扩展点**：`modifyMeta(op=add field)`
- **风险**：低（基础资料 · 扩展安全）

### 调用链

```
Step 1: getFormSchema(formNumber=hbpm_positiontpl)  // 查目前字段清单 · 防重名
Step 2: modifyMeta({
  formId: "hbpm_positiontpl",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hbpm_positiontpl",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_positiontplcate",
      name: {zh_CN: "岗位分类", en_US: "Position Tpl Category"},
      mustInput: false
    }
  }]
})
Step 3: getFormSchema(hbpm_positiontpl)  // 二次验证落库
```

### 踩坑

- ❌ 字段 key 不带 ISV 前缀 → 标品升级被覆盖（PR-001）
- ❌ 忘记在 OP 层 onPreparePropertys 声明 → 字段值为 null（PR-010）
- ⚠ posttpltype / org 字段属性 ISV 不可通过 modifyMeta 修改

---

## CS-02 · 保存后添加自定义联动逻辑

### 需求

业务方说：每当岗位模板保存后，需要同步更新 ISV 自建的"岗位配置"表。

### 推荐方案

- 自建 `extends HRDataBaseOp` 的 OP 插件，挂在 save opKey
- 在 `endOperationTransaction` 或 `afterExecuteOperationTransaction` 中实现
- ❌ 不继承 `PositionTplSaveOp`（PR-001）
- ✅ ISV OP 排在 `PositionTplSaveOp` 之后，确保标品同步先完成

### 关键模式

```java
public class TdkwPositionTplSyncOp extends HRDataBaseOp {
    @Override
    public void onPreparePropertys(PreparePropertysEventArgs e) {
        e.getFieldKeys().add("${ISV_FLAG}_positiontplcate");
        e.getFieldKeys().add("name");
        e.getFieldKeys().add("org");
    }

    @Override
    public void afterExecuteOperationTransaction(AfterOperationArgs e) {
        super.afterExecuteOperationTransaction(e);
        for (DynamicObject dataEntity : e.getDataEntities()) {
            String name = dataEntity.getString("name");
            // 自定义同步逻辑...
        }
    }
}
```

---

## CS-03 · 删除/禁用前置校验

### 需求

业务方说：要在禁用/删除岗位模板前检查是否有 ISV 自建数据引用了它。

### 推荐方案

- 自建 `extends AbstractValidator` 的校验器
- 通过 `extends HRDataBaseOp` + `onAddValidators` 并列挂
- ISV OP rowkey 排在标品 OP 之前（先校验 ISV 规则再执行标品逻辑）

```java
public class TdkwPositionTplDeleteValidator extends AbstractValidator {
    @Override
    public void validate() {
        DynamicObject[] dataObjects = this.getDataObjects();
        for (DynamicObject dyn : dataObjects) {
            Long tplId = dyn.getLong("id");
            QFilter qf = new QFilter("positiontpl", "=", tplId);
            if (new HRBaseServiceHelper("${ISV_FLAG}_custom_entity").isExists(qf)) {
                this.addErrorMessage(dyn, "该岗位模板已被自定义配置引用，不可删除。");
            }
        }
    }
}
```

---

## CS-04 · 列表自定义列/BU 过滤扩展

### 需求

业务方说：要在岗位模板列表加"岗位分类"列，并按 ISV 自建字段过滤。

### 推荐方案

- 自建 `extends HRDataBaseList` 的 ListPlugin 并列挂
- 注意：`PositionTplBuListPlugin` 已提供 org BU 过滤，ISV 无需重复实现
- ❌ 不继承 `PositionTplBuListPlugin`（PR-001）

---

## CS-05 · BEC 订阅岗位模板变更

### 重要

本场景**有 BEC**：`PositionTplSaveOp.afterExecuteOperationTransaction` 通过 `ChangeMsgServiceImpl.sendMsg()` 发送变更消息。

### 推荐方案

- ISV 可订阅对应 BEC 事件（需确认 `ChangeMsgServiceImpl.sendMsg()` 具体的事件 key）
- ❌ 不要通过继承 `PositionTplSaveOp` 来感知变更（PR-001）
- ✅ 实现 `IEventServicePlugin.handleEvent()` 订阅对应事件

### 注意

`ChangeMsgServiceImpl` 是封装实现，内部是否走异步队列需进一步确认（参考 feedback_bec_3layer_async_publish.md）。
