# 推荐定制方案 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类（DynaRuleItemEdit / DynaItemDeleteOp / HRAdminStrictPlugin）+ scene_doc.json 34 字段 + rules_chain_all.json 44 opKey
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

所有方案遵循统一结构：**背景 → 扩展点 → 调用链 → 代码框架 → 踩坑 → 关联 PR**

---

## CS-01 · 给 hrcs_dynaruleitem 扩展自定义字段（最高频）

**关联 Pattern**：`pattern/add_field_extension/README.md`

### 需求

业务方说：规则参数项需要加"参数分类"字段（单选枚举：`组织维度 / 人员维度 / 岗位维度 / 其他维度`），用于列表分组过滤 + 方案配置时分类筛选 PermFilter 可选参数列表。

### 推荐方案

- **扩展对象**：`hrcs_dynaruleitem`（主实体）
- **扩展点**：`modifyMeta(op=add, elementType=field)` 或 IDEA 插件 Web UI 加字段
- **风险**：低（不动业务规则 · 只是数据展示 + 列表过滤位）
- **特点**：hrcs_dynaruleitem **不是 HisModel**（实证 grep 0 命中 boid/iscurrentversion）。ISV 加字段直接写入主表 `t_hrcs_dynaruleitem`，不会进版本控制。

### 调用链（3 步）

```
Step 1: getBizApps()                              // 拿 hrcs 应用 bizAppId
Step 2: modifyMeta({
  formId: "hrcs_dynaruleitem",
  ops: [{
    op: "add",
    treeType: "entity",
    elementType: "field",
    parentScope: "hrcs_dynaruleitem",
    element: {
      fieldType: "ComboField",
      key: "${ISV_FLAG}_paramcategory",
      name: {zh_CN: "参数分类", en_US: "Param Category"},
      mustInput: false,
      comboOptions: [
        {value: "ORG",  name: {zh_CN: "组织维度"}},
        {value: "EMP",  name: {zh_CN: "人员维度"}},
        {value: "POST", name: {zh_CN: "岗位维度"}},
        {value: "OTHER",name: {zh_CN: "其他维度"}}
      ]
    }
  }]
})
Step 3: getFormSchema("hrcs_dynaruleitem")        // 二次验证落库
```

### 踩坑

- **datatype / valsourcetype / entitytype 等核心字段 `isvCanModify=false`**（scene_doc.json 实证）。ISV 只能加新字段，不能改标品已有字段的元数据属性。
- **本场景非 HisModel**：加字段不需要考虑 boid / iscurrentversion / hisversion 等 HisModel 字段，直接写主表即可。
- **ISV 字段命名用 ISV 前缀 (${ISV_FLAG}_)**避免与标品未来新增字段冲突。

---

## CS-02 · 字段联动（datatype / valsourcetype 切换带出 ISV 自定义字段默认值）

**关联 Pattern**：`pattern/override_plugin_behavior/README.md`

### 需求

ISV 加了参数分类字段（CS-01）。当用户选 datatype=org 时，自动把参数分类默认设为"组织维度"；选 datatype=enum 时不显示参数分类。

### 推荐方案

- **扩展点**：自建 FormPlugin 并列挂到 `hrcs_dynaruleitem`，继承 `HRDataBaseEdit`
- **覆盖生命周期**：`propertyChanged`（监听 datatype 变化）+ `afterBindData`（初次加载时设置默认值）
- **风险**：低（标品 DynaRuleItemEdit.propertyChanged 只处理 datatype=org 时 entitytype 写死 haos_adminorghrf7 + 非 org 时清空 entitytype。ISV propertyChanged 在标品之后执行，不会冲突）

### 关键约束

- 标品 `propertyChanged` 只写 entitytype，**不动 ISV 字段** —— ISV 并列挂不会互相覆盖
- `clearUnMustData()` 是标品 DynaRuleItemEdit 的 **私有方法**，ISV 不能调 —— 如果 ISV 也要清理自建字段的脏值，需要自建 clear 逻辑
- 联动逻辑不要用 `setDataChanged(false)` —— 标品 save 依赖脏标记

---

## CS-03 · save 前置业务校验（自建 Validator）

**关联 Pattern**：`pattern/add_unique_validation/README.md`

### 需求

业务方要求：(1) datatype=bd 时必须填 description（描述不能空）；(2) ISV 参数分类字段不能为空。

### 推荐方案

- **扩展点**：自建 OP 继承 `HRDataBaseOp`，在 `onAddValidators` 注册自定义 `AbstractValidator`
- **标品已有**：`DynaRuleItemEdit.beforeItemClick` 在 bar_save 时做 enum 三连校验 + clearUnMustData（FormPlugin 端）· 标品 OP 链无场景专属校验（仅 DynaItemDeleteOp 在 delete 时做）
- **注意**：save opKey 的 OP 链是 `CodeRuleOp → BdVersionSaveServicePlugin → HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp`（6 个标品 OP · 无场景专属 Validator）。ISV OP 插在 HRBaseOriginalOp 之后即可。

### Validator 代码框架

```java
// 自建 OP
public class MyDynaRuleItemSaveOp extends HRDataBaseOp {
    @Override
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator(new MyDynaRuleItemValidator());
    }
}

// 自建 Validator
public class MyDynaRuleItemValidator extends AbstractValidator {
    @Override
    public void validate(ExtendedDataEntity row) {
        DynamicObject data = row.getDataEntity();
        String datatype = data.getString("datatype");
        String desc = data.getString("description");
        if ("bd".equals(datatype) && StringUtils.isBlank(desc)) {
            addErrorMessage(row, "基础资料类型的规则参数项必须填写描述。");
        }
    }
}
```

### 踩坑

- 不要在 FormPlugin 端 `beforeItemClick` 做复杂校验 —— DynaRuleItemEdit 的 `beforeItemClick` 已经拦截了 bar_save 做 enum 校验，ISV 再拦可能跟标品冲突。推荐在 OP 链的 Validator 阶段做。
- `addErrorMessage(row, msg)` 会自动聚焦到错误行，不需要手写 focusCell。

---

## CS-04 · 删除前查下游引用（ISV 自建表也引用本表）

**关联场景**：CS-03 的 delete 变体

### 需求

ISV 自建了"方案审批记录表"（`t_isv_scheme_approval`），其中 `fparam_id` 引用 `hrcs_dynaruleitem.id`。删除规则参数项时，除了标品检查 `hrcs_dynascheme.condition` 引用外，还要检查 ISV 自建表引用。

### 推荐方案

- **扩展点**：自建 OP 继承 `HRDataBaseOp`，在 `onAddValidators` 注册 Validator，查 ISV 表
- **标品已有**：`DynaItemDeleteOp.onAddValidators` 注册 `DynaItemDelValidator` —— 只查 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)`。**ISV Validator 与标品并列执行**，任一报错都阻断。
- **delete entry sub-row**：如果想在删除枚举行（deleteentry）时也查 ISV 表引用，必须在 FormPlugin 端 `beforeDoOperation(operateKey=="deleteentry")` 自建拦截（标品 `DynaRuleItemEdit.beforeDoOperation` 只查 dynascheme.condition 引用）

### 注意

- `deleteentry` 是 FormPlugin 端操作（不走 OP 链），所以自定义 Validator **不适用** —— 必须在 FormPlugin 的 `beforeDoOperation` 里做。
- `DynaItemDelValidator` 的类名是内部类/包级类，没有 @SdkPublic 注解 —— ISV **不能继承它**，只能并列注册自己的 Validator。

---

## CS-05 · 变更/启用/禁用后通知下游（BEC 发布方）

**重要前提**：grep `triggerEventSubscribe / IEventService / EventServiceHelper` 在反编译 DynaRuleItemEdit + DynaItemDeleteOp + HRAdminStrictPlugin **全 0 命中**（2026-04-28 实证）。标品 hrcs_dynaruleitem **不发任何 BEC 事件**。

### 需求

业务方要求：规则参数项 save / delete / enable / disable 后，通知下游 OA 审批系统同步刷新规则配置缓存。

### 推荐方案

- **扩展点**：自建 OP 继承 `HRDataBaseOp`，在 `afterExecute`（事务提交后）调 `IEventService.triggerEventSubscribeJobs`
- **事件号**：ISV 自定义（如 `isv_dynaruleitem_changed`）· 不可与标品事件号冲突
- **订阅方**：下游系统实现 `IEventServicePlugin`，在 `handleEvent` 里解析 `KDBizEvent` 拿到 paramId + opType

### 代码框架

```java
// 发布方 OP
public class MyDynaRuleItemEventOp extends HRDataBaseOp {
    @Override
    public void afterExecute(AfterOperationArgs args) {
        IEventService svc = IEventService.getInstance();
        for (DynamicObject row : args.getDataEntities()) {
            KDBizEvent evt = new KDBizEvent();
            evt.setEventNumber("isv_dynaruleitem_changed");
            evt.setBizData("paramId", row.getString("id"));
            evt.setBizData("opType", args.getOperateKey());
            svc.triggerEventSubscribeJobs("hrcs", evt.getEventNumber(), evt);
        }
    }
}
```

### 踩坑

- **OP 注册时要覆盖 save / delete / enable / disable 4 个 opKey**（或覆盖所有 44 个 opKey 中需要通知的）
- **afterExecute 在事务提交后执行** —— 此时数据已落库，下游可以安全反查
- **enable/disable 的 OP 链只有 `HRBaseDataLogOp`**（标品没有场景专属 OP），ISV OP 加在它后面即可
- **不要用 `beforeExecute`** —— 事务还没提交，下游反查可能读到旧数据

---

## CS-06 · 枚举子表行 ID 生成（自建分录子表）

### 需求

ISV 需要在枚举子表（entryentity）上加"枚举值描述"字段，存更长的文字说明。注意这**不是替换** entryentity —— 是加一个子表的补充列。自建一条分录子表也行（如"参数扩展属性表"），需要确保行 ID 跟标品不冲突。

### 推荐方案

- **方式 A（推荐）**：直接在 entryentity 上 `modifyMeta add field` 加子表字段（`treeType: "entryentity"`，`parentScope: "hrcs_dynaruleitem"`）
- **方式 B**：自建新的分录子表（`modifyMeta add entity`），行 ID 用 `ID.genLongId()` 或 `ORM.create().genLongId("hrcs_dynaruleitem.custom_entry")`

### 行 ID 生成规范

标品 entryentity 行 id 由平台自动生成（`kd.bos.id.ID`），ISV 自建分录子表也用同一套：

```java
long rowId = ID.genLongId();  // 分布式 ID，不会跟标品冲突
```

### 踩坑

- 不要在 entryentity 上加 `isvCanModify=false` 的字段 —— 标品已锁 `entryentity.value` 列（FP_ABD4），ISV 加新列不受影响
- 自建子表的行 ID 不要用自增 —— 苍穹分布式部署下会冲突

---

## CS-07 · 列表过滤定制（按 datatype / ISV 字段过滤）

### 需求

HR 管理员说规则参数项列表太长，想默认只显示 datatype=bd 的参数项（最常用），或者按 ISV 参数分类字段过滤。

### 推荐方案

- **扩展点**：自建 ListPlugin 继承 `HRDataBaseList`（**不继承场景专属类** —— dynaruleitem 没有场景专属 ListPlugin）
- **覆盖生命周期**：`setFilter` —— 加默认 QFilter
- **注意**：标品 hrcs 11 表单共用 `HRBaseDataCommonList` 列表插件，没有场景专属 ListPlugin。ISV ListPlugin 跟它并列挂。

```java
public class MyDynaRuleItemList extends HRDataBaseList {
    @Override
    public void setFilter(SetFilterEvent evt) {
        // 默认只显示 bd 类型
        evt.getQFilters().add(new QFilter("datatype", "=", "bd"));
    }
}
```

---

## 不可行的扩展（明确禁止）

| 需求 | 为什么不行 | 替代方案 |
|---|---|---|
| 把 dynaruleitem 改成 HisModel | 平台模型固定 · BillFormModel 不能改成 HisModel | ISV 自建 HisModel 表镜像本表 |
| 修改 datatype 字段的必填/可改属性 | isvCanModify=false（元数据保护） | 加 ISV 字段替代 |
| 替换 datatype=org 时 entitytype 的硬编码 | DynaRuleItemEdit.propertyChanged 写死 haos_adminorghrf7 | ISV 在自家 propertyChanged 里重新 setValue 覆盖 |
| 直接继承 DynaRuleItemEdit / DynaItemDeleteOp | 无 @SdkPublic 注解 · 白名单不允许 | 自建类继承 HRDataBaseEdit / HRDataBaseOp 并列挂 |
| 在标品 OP 链中移除某个 OP | 平台不允许动态修改 OP 链 | 在 Validator 阶段返回 false 跳过 |
