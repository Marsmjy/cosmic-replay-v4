# 业务规则 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 反编译实证 · listRules=0 · 隐性规则从代码提炼
> **confidence**: verified（源于 PositionTplTypeEditPlugin.java + PositionTplTypeSaveOp.java 反编译）
> **最后更新**: 2026-04-27

---

## 一、业务规则总体说明

`hbpm_positiontpltype` **没有任何显式 listRules**（`rules.json` 为空，0 条）。

但场景拥有明确的隐性业务规则，分布在两个专属类中：
- `PositionTplTypeEditPlugin`（FormPlugin）：UI 层的禁用态保护 + index 自动填充 + 操作失败字段高亮
- `PositionTplTypeSaveOp`（OP 插件）：OP 层的 3 个 Validator 校验（通用 + index 唯一 + 控制策略）

---

## 二、隐性业务规则（INV）

### INV-TT-01 · 禁用态强制只读（+ 隐藏保存/取消按钮）

**来源**: `PositionTplTypeEditPlugin.afterBindData`（反编译实证）

```java
String enable = basedataEntity.getString("enable");
if (HRStringUtils.equals(enable, "0")) {
    ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
    this.getView().setVisible(Boolean.FALSE, new String[]{"btnsave"});
    this.getView().setVisible(Boolean.FALSE, new String[]{"btncancel"});
    this.getView().setVisible(Boolean.TRUE, new String[]{"btnclose"});
}
```

**规则说明**：
- 当岗位模板类型的 `enable` 字段值为 `"0"`（禁用状态）时，编辑页切换为 VIEW 只读状态
- 同时隐藏"保存"和"取消"按钮，显示"关闭"按钮
- 与 hbpm_basedatalist 的 issyspreset 保护不同：本规则保护的是**业务禁用态数据**，不是出厂数据
- **ISV 注意**：enable="0" 时 ISV 扩展字段同样不可编辑，UI 联动逻辑必须判断 enable 状态

---

### INV-TT-02 · index 排序号自动递增填充（步长 10）

**来源**: `PositionTplTypeEditPlugin.afterBindData`（反编译实证）

```java
private static final Integer DEFAULT_INDEX = 10;

if (HRObjectUtils.isEmpty(index) || index.equals(0)) {
    try (DataSet rows = PositionTplTypeRepository.getInstance().queryOneIndexByIndexDesc()) {
        Iterator iterator = rows.iterator();
        if (iterator.hasNext()) {
            Row row = (Row)iterator.next();
            Integer indexDb = row.getInteger("index");
            this.getModel().setValue("index", indexDb + DEFAULT_INDEX);
        } else {
            this.getModel().setValue("index", DEFAULT_INDEX);  // 首条数据 index=10
        }
        this.getModel().setDataChanged(false);  // 防止触发保存提示
    }
}
```

**规则说明**：
- 新建岗位模板类型时，`index` 字段为空或 0，系统自动查询当前最大 index 值，填入 `maxIndex + 10`
- 首条数据 index = 10；第二条 = 20；以此类推（步长 10，留出插入空间）
- `setDataChanged(false)` 防止自动填值触发"数据已修改"弹窗
- **ISV 注意**：如果 ISV 需要改变排序逻辑，必须在 PositionTplTypeSaveOp（OP 层）中覆盖，因为 afterBindData 自动填的值会被 OP 层再次校验

---

### INV-TT-03 · 操作失败时字段级错误高亮（nameError / indexError）

**来源**: `PositionTplTypeEditPlugin.afterDoOperation`（反编译实证）

```java
public void afterDoOperation(AfterDoOperationEventArgs args) {
    super.afterDoOperation(args);
    if (null != args.getOperationResult() && !args.getOperationResult().isSuccess()) {
        FormOperate formOperate = (FormOperate)args.getSource();
        OperateOption option = formOperate.getOption();
        boolean nameErrorExist = option.containsVariable("nameError");
        if (nameErrorExist) {
            String displayErrorInfo = "已存在名称相同且可用的岗位模板类型。";
            PositionTplUtil.isNameEnableRe(this.getView(), "name", displayErrorInfo);
        }
        if (indexErrorExist = option.containsVariable("indexError")) {
            String indexErrorInfo = "数据已存在";
            PositionTplUtil.isNameEnableRe(this.getView(), "index", indexErrorInfo);
        }
    }
}
```

**规则说明**：
- 保存操作失败时，检查 OperateOption 中是否携带 `nameError`/`indexError` 变量
- 若存在 `nameError`：高亮 `name` 字段并显示"已存在名称相同且可用的岗位模板类型"
- 若存在 `indexError`：高亮 `index` 字段并显示"数据已存在"
- 这两个错误变量由 `PositionTplTypeSaveOp` 中的 Validator 在校验失败时写入

---

### INV-TT-04 · 保存前 3 层 Validator 校验（OP 层）

**来源**: `PositionTplTypeSaveOp.onAddValidators`（反编译实证）

```java
public void onAddValidators(AddValidatorsEventArgs evt) {
    super.onAddValidators(evt);
    evt.addValidator(new PositionTplCommonValidator());      // 通用校验
    evt.addValidator(new PositionTplTypeIndexUniqueValidator()); // index 唯一性
    evt.addValidator(new CtrlStrategyValidator());           // 控制策略合规
}
```

**三个 Validator 说明**：

| Validator | 校验内容 | 失败时行为 |
|---|---|---|
| `PositionTplCommonValidator` | 岗位模板通用校验（name 唯一性等）| 写入 `nameError` 选项变量 |
| `PositionTplTypeIndexUniqueValidator` | index 排序号唯一性（同 org 下不能重复）| 写入 `indexError` 选项变量 |
| `CtrlStrategyValidator` | 控制策略（ctrlstrategy）合规检查 | 直接 addErrorMessage |

---

### INV-TT-05 · beginOperationTransaction 中 index 再次兜底填充

**来源**: `PositionTplTypeSaveOp.beginOperationTransaction`（反编译实证）

```java
public void beginOperationTransaction(BeginOperationTransactionArgs e) {
    int largestIndex = this.getLargestIndex();
    for (DynamicObject dataEntity : e.getDataEntities()) {
        DynamicProperty indexProp = dataEntity.getDynamicObjectType().getProperty("index");
        Object indexPropValue = dataEntity.getDataStorage().getLocalValue(indexProp);
        if (!HRObjectUtils.isEmpty(indexPropValue)) continue;
        dataEntity.set("index", largestIndex += 10);  // OP 层再兜底
    }
}
```

**规则说明**：
- 在 afterBindData 自动填值（INV-TT-02）的基础上，OP 层 beginOperationTransaction 再做一次兜底
- 防止通过 API/Import 方式绕过 UI 直接保存时 index 为空
- `getLargestIndex()` 查所有现有 index 的最大值，从该值 +10 开始递增
- 这是双层保护：UI 层（afterBindData）+ OP 层（beginOperationTransaction）

---

## 三、规则交互关系

```
[用户打开新建弹窗]
    ↓
afterBindData（PositionTplTypeEditPlugin）
    → enable="0"? → VIEW 模式（INV-TT-01）
    → index=0/空? → 自动填 maxIndex+10（INV-TT-02）
    ↓
[用户点保存]
    ↓
onAddValidators（PositionTplTypeSaveOp）
    → PositionTplCommonValidator（名称唯一）
    → PositionTplTypeIndexUniqueValidator（index 唯一）
    → CtrlStrategyValidator（控制策略）
    ↓ 如校验失败
afterDoOperation（PositionTplTypeEditPlugin）
    → nameError? → 高亮 name 字段（INV-TT-03）
    → indexError? → 高亮 index 字段（INV-TT-03）
    ↓ 如校验通过
beginOperationTransaction（PositionTplTypeSaveOp）
    → index 还是空? → 兜底填值（INV-TT-05）
```

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplTypeEditPlugin -->

## chgaction 实证补充（PositionTplTypeEditPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplTypeEditPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplTypeEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplTypeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionTplTypeSaveOp -->

## chgaction 实证补充（PositionTplTypeSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionTplTypeSaveOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionTplTypeSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionTplTypeSaveOp -->
