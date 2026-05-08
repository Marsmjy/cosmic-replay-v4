# 扩展点全图 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 5 反编译类 + rules_chain_all.json + form_lifecycle_rules.json 实证
> **confidence**：verified
> **数据源**：CFR 反编译 HRAdminGroupTreeListPlugin / HRAdminStrictPlugin / AdminGroupAddUserOp / AdminGroupDelUserOp / AdminGroupDelOp（2026-04-28）

---

## 一、标品插件全览（8 个 · 按层级分组）

### 1.1 准入层（preOpenForm · 11 hrcs 表单共用）

| 插件 | 完整类名 | 父类 | 触发生命周期 | 角色 |
|---|---|---|---|---|
| `HRAdminStrictPlugin` | `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` | HRDynamicFormBasePlugin | `preOpenForm` | HR 域管理员准入闸。3 层判定（isAdminUser / isCosmicUser / isHrAdmin），任一失败 setCancel 阻断打开。F7 lookup 模式直接放行。 |

⚠ **ISV 禁止继承**。这是 hrcs 11 个表单的共用准入闸，改一处坏所有（PR-001 红线）。

### 1.2 TreeList 视图层（主插件 · 场景专属）

| 插件 | 完整类名 | 父类 / 接口 | 触发生命周期 |
|---|---|---|---|
| `HRAdminGroupTreeListPlugin` | `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin` | AbstractTreeListPlugin | `registerListener`, `initializeTree`, `initTreeToolbar`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`, `confirmCallBack`, `treeNodeClick`, `setFilter`, `chat` |

实现接口：`IAdminGroupListSubExtPlugin`（SDK 扩展点）, `HRAdminConstant`（常量接口）。

⚠ **ISV 禁止继承**。这是场景专属类，只允许并列挂自定义 TreeListPlugin（PR-001 红线）。其 `beforeDoOperation` 是核心 dispatcher——拦截 7 个 donothing_* opKey 并路由到私有业务方法。

### 1.3 OP 层（3 个 · 场景专属）

| OP 插件 | 完整类名 | 父类 | OP 阶段 | 关键行为 |
|---|---|---|---|---|
| `AdminGroupAddUserOp` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp` | HRDataBaseOp | `beginOperationTransaction` | SaveServiceHelper.save → cancelShowFormRights → writePermLog → writeOpLog |
| `AdminGroupDelUserOp` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp` | HRDataBaseOp | `beginOperationTransaction` | superiorGroupIds 守护 → DeleteServiceHelper.delete → cancelShowFormRights → writePermLog |
| `AdminGroupDelOp` | `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp` | HRDataBaseOp | `beginOperationTransaction` | 9 表级联 DeleteServiceHelper.delete（事务内） |

⚠ **ISV 禁止继承这 3 个 OP**。ISV 应继承 `HRDataBaseOp` 并列挂到对应 opKey（do_add_user / do_remove_user / do_remove_group）。

### 1.4 扩展接口层（1 个 SDK 扩展点 · ISV 可实现）

| 接口 | 完整类名 | 方法 | 触发位置 |
|---|---|---|---|
| `IAdminGroupListSubExtPlugin` | `kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin` | `beforeAddCustomUser(AddCustomUserEventArgs)` | `HRAdminGroupTreeListPlugin.showUserF7TreeList` L432-L438（HRPluginProxy.callAfter 调用） |

⭐ **ISV 唯一可扩展用户 F7 范围的入口**。实现此接口后通过 KingdeeCloudConfig 注册，可向 `AddCustomUserEventArgs.getLsp().getListFilterParameter().getQFilters()` 追加自定义 QFilter。

---

## 二、按生命周期方法分组（完整执行顺序）

TreeList 生命周期完整时序（标品注册顺序）：

```
registerListener         → searchap.SearchEnterListener 挂载
  ↓
initializeTree           → HRAdminGroupService.initAdminGroupTree（树根节点装载）
  ↓
initTreeToolbar          → setVisible(TRUE, btnnew/btnedit/btndel)
  ↓
treeNodeClick            → 缓存 5 个 IPageCache key（focusNodeId 等）
  ↓
setFilter                → adminGroupCanSee in 过滤（右侧用户列表）
  ↓
beforeDoOperation        → 7 个 donothing_* 路由（核心 dispatcher）
  ↓ (donothing_remove_user 成功后)
afterDoOperation         → 调 do_remove_user OP（二次落库 + 权限清理）
  ↓ (用户 F7 关闭 / 明细页关闭)
closedCallBack           → do_add_user 落库 / 树重建
  ↓ (删组确认框)
confirmCallBack          → do_remove_group OP 调度
  ↓
chat                     → setCancel(true) 始终禁用
```

**ISV 并列挂载顺序**：同生命周期方法，标品先注册 → ISV 后注册，按顺序依次调用。ISV 插件不能通过注册顺序来"覆盖"标品逻辑——只能在标品逻辑执行后做追加，或在 OP 层 onAddValidators 阶段做前置校验。

---

## 三、ISV 可覆盖的扩展点（Top 3 · 实际生产高频）

### 扩展点 1：`do_add_user` OP 的 onAddValidators

- **接口**：`AbstractOperationServicePlugIn.onAddValidators(AddValidatorsEventArgs)`
- **覆盖原因**：标品 OP 无 onAddValidators——添加用户前无业务校验。ISV 可注册 Validator 做"用户是否能被加到此组"的自定义校验（如用户必须在特定部门、不能是离职状态等）。
- **关键数据源**：`args.getDataEntities()` 拿待保存行，`row.getDataEntity().get("user")` 拿用户 F7 选中值。
- **对应 CS**：CS-03

### 扩展点 2：`do_remove_group` OP 的 onAddValidators

- **接口**：`AbstractOperationServicePlugIn.onAddValidators(AddValidatorsEventArgs)`
- **覆盖原因**：标品 AdminGroupDelOp 级联删 9 张标品表，但**不自动反查 ISV 自建表**。ISV 自建表（如 ${ISV_FLAG}_admingroupext）有 admingroup 引用时，必须在此 Validator 中做反查，阻断删组操作。
- **关键数据源**：`this.getOption().getVariableValue("focusNodeId")` 拿操作上下文。
- **对应 CS**：CS-04

### 扩展点 3：`IAdminGroupListSubExtPlugin.beforeAddCustomUser`（SDK 扩展）

- **接口**：`kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin`
- **覆盖原因**：标品用户 F7 5 道过滤（已加/自己/同scheme不同type/enable=1/usertype）已基本完备，但业务方可能要求"只显示部门负责人"或"只显示正式员工"等额外范围限制。这是唯一不侵入标品代码的扩展方式。
- **调用链**：`HRPluginProxy.callAfter(p -> p.beforeAddCustomUser(eventArgs))` 在标品 5 道过滤之后调用。
- **对应 CS**：CS-06

---

## 四、ISV 不能覆盖的扩展点（红线）

| 不能覆盖的类/方法 | 原因 | PR |
|---|---|---|
| `HRAdminStrictPlugin.preOpenForm` | 11 hrcs 表单共用准入闸，改一处坏所有 | PR-001 |
| `HRAdminGroupTreeListPlugin.beforeDoOperation` | 场景专属类，7 个 donothing_* 路由逻辑，继承会破坏 | PR-001 |
| `HRAdminGroupTreeListPlugin.showUserF7TreeList` | 私有方法，5 道 DB 直查过滤逻辑 | PR-001 |
| `HRAdminGroupTreeListPlugin.adminGroupTreeRemoveOperation` | 私有方法，4 道前置校验 + 角色反查 | PR-001 |
| `AdminGroupAddUserOp.beginOperationTransaction` | 场景专属 OP 类 | PR-001 |
| `AdminGroupDelUserOp.beginOperationTransaction` | 场景专属 OP 类 | PR-001 |
| `AdminGroupDelOp.beginOperationTransaction` | 场景专属 OP 类（9 表级联） | PR-001 |
| `hrcs_useradmingroup` 的 `user` / `usergroup` 字段元属性 | 破坏关联表语义 | PR-007 |

### 正确做法：并列挂载

- **FormPlugin 层**：ISV 继承 `AbstractTreeListPlugin`（不是 HRAdminGroupTreeListPlugin），并列注册到 `hrcs_useradmingroup`。
- **OP 层**：ISV 继承 `HRDataBaseOp`（不是 AdminGroup*Op），并列注册到对应的 do_* opKey。
- **SDK 层**：实现 `IAdminGroupListSubExtPlugin` 接口（标品已预留 HRPluginProxy 调用链）。

---

## 五、BEC 事件订阅（0 处 · 不适用）

标品 hrcs_useradmingroup 没有 BEC 发布方（grep 反编译 5 类 0 处 triggerEventSubscribeJobs 调用）。ISV 不建议在此场景自建 BEC 发布——无现成订阅方，自建无价值（CS-05 已砍，详见 06_customization_solutions.md CS-05）。

如果 ISV 确需跨系统通知（如同步到外部 SSO），推荐在 `do_add_user` 或 `do_remove_user` OP 的 `afterExecuteOperationTransaction` 阶段调用外部 API（事务已提交，不影响主业务回滚）。

---

## 六、OP 层扩展完整方法签名

ISV 继承 `HRDataBaseOp` 后可选覆盖的方法（按执行顺序）：

| 方法 | 阶段 | 可用于 |
|---|---|---|
| `onAddValidators(AddValidatorsEventArgs)` | OP 前校验 | 防重复加用户、删组前 ISV 表反查 |
| `beforeExecuteOperationTransaction(BeforeOperationArgs)` | 事务前 | 记录操作前状态快照 |
| `beginOperationTransaction(BeginOperationTransactionArgs)` | 事务中 | 级联增删 ISV 表 |
| `endOperationTransaction(EndOperationTransactionArgs)` | 事务中 | 同事务内的收尾逻辑 |
| `afterExecuteOperationTransaction(AfterOperationArgs)` | 事务已提交 | 通知外部系统、清缓存 |

⚠ **不要在 afterExecuteOperationTransaction 做关键校验**——事务已提交，校验失败无法回滚（PR-010）。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

## ISV 扩展指引（基于 ForbidUrlOpenPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · ForbidUrlOpenPlugin L42
```java
  40           BillShowParameter billShowParameter;
  41           super.preOpenForm(args);
  42 >         if (PermissionServiceHelper.isSuperUser((long)RequestContext.get().getCurrUserId())) {
  43               return;
  44           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## ISV 扩展指引（基于 HRCertCheckEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## ISV 扩展指引（基于 HRCertCheckList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## ISV 扩展指引（基于 HRAdminStrictPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public static public static void showMesIfUserIsNotAdmin(kd.bos.form.events.PreOpenFormEventArgs)`
- `public static public static boolean isHrAdmin()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminService L23
```java
  21   public class HRAdminService {
  22       public static boolean isHrAdmin() {
  23 >         Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
```

**QUERY_BUILDER** · HRAdminService L24
```java
  22       public static boolean isHrAdmin() {
  23           Long userId = RequestContext.get().getCurrUserId();
  24 >         QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminService L25
```java
  23           Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25 >         DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
  27           filters = new QFilter[]{new QFilter("user.id", "=", (Object)userId).and("usergroup.adminscheme.id", "=", (Object)adminScheme).and("usergroup.isdomain", "=", (Object)"1").and("usergroup.domain", "=", (Object)1386267129346523136L).or("usergroup.id", "=", (Object)1393280986623636480L).and("user.id", "=", (Object)userId)};
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin -->

## ISV 扩展指引（基于 HRAdminGroupTreeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractTreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`, `setFilter`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void initializeTree(java.util.EventObject)`
- `public public void initTreeToolbar(java.util.EventObject)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeAddCustomUser(kd.sdk.hr.hbp.business.extpoint.permission.hradmi.AddCustomUserEventArgs)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void treeNodeClick(kd.bos.form.control.events.TreeNodeEvent)`
- `protected protected kd.bos.orm.query.QFilter nodeClickFilter()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminService L23
```java
  21   public class HRAdminService {
  22       public static boolean isHrAdmin() {
  23 >         Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
```

**QUERY_BUILDER** · HRAdminService L24
```java
  22       public static boolean isHrAdmin() {
  23           Long userId = RequestContext.get().getCurrUserId();
  24 >         QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminService L25
```java
  23           Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25 >         DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
  27           filters = new QFilter[]{new QFilter("user.id", "=", (Object)userId).and("usergroup.adminscheme.id", "=", (Object)adminScheme).and("usergroup.isdomain", "=", (Object)"1").and("usergroup.domain", "=", (Object)1386267129346523136L).or("usergroup.id", "=", (Object)1393280986623636480L).and("user.id", "=", (Object)userId)};
```

**WRITE_VIA_HELPER** · HRAdminGroupTreeListPlugin L419
```java
 417                   ++i;
 418               }
 419 >             OperationResult operationResult = OperationServiceHelper.executeOperate((String)"do_remove_user", (String)"hrcs_useradmingroup", (DynamicObject[])dynamicObjects, (OperateOption)operateOption);
 420               if (operationResult.isSuccess()) {
 421                   Map customData = operationResult.getCustomData();
```

**THROW_BIZ_EXCEPTION** · HRAdminGroupService L120
```java
 118           catch (Exception e) {
 119               logger.error("[AdminGroupService]\u83b7\u53d6\u7528\u6237\u6240\u5c5e\u7ba1\u7406\u5458\u7ec4\u5931\u8d25\uff1a", (Throwable)e);
 120 >             throw new KDBizException(ResManager.loadKDString((String)"\u7ba1\u7406\u5458\u7ec4\u6570\u636e\u5f02\u5e38\u6216\u5143\u6570\u636e\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u6216\u91cd\u65b0\u5347\u7ea7\u7ba1\u7406\u5458\u6570\u636e\u3002", (String)"AdminGroupService_0", (String)"bos-mservice-permission", (Object[])new Object[0]));
 121           }
 122           String longNumberFirst = (String)userAdminGroup[0].getDynamicObject("usergroup").get("longnumber");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp -->

## ISV 扩展指引（基于 AdminGroupDelOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · AdminGroupDelOp L44
```java
  42           Long adminGroupId = Long.parseLong(arr[0]);
  43           try {
  44 >             DeleteServiceHelper.delete((String)"perm_admingroupfunperm", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
  45               DeleteServiceHelper.delete((String)"perm_admingroupbizunit", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
  46               DeleteServiceHelper.delete((String)"perm_admingrouporg", (QFilter[])new QFilter[]{new QFilter("usergroup", "=", (Object)adminGroupId)});
```

**THROW_BIZ_EXCEPTION** · AdminGroupDelOp L56
```java
  54           catch (Exception ex) {
  55               LOGGER.error("\u5220\u9664\u7ba1\u7406\u5458\u7ec4\u5931\u8d25", (Throwable)ex);
  56 >             throw new KDBizException(ResManager.loadKDString((String)"\u5220\u9664\u5931\u8d25\u3002", (String)"AdminGroupTreeListPlugin_7", (String)"bos-permission-formplugin", (Object[])new Object[0]));
  57           }
  58       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp -->

## ISV 扩展指引（基于 AdminGroupAddUserOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminGroupService L101
```java
  99           Long adminScheme;
 100           QFilter[] filters;
 101 >         Long userId = RequestContext.get().getCurrUserId();
 102           String adminSchemeStr = pageCache.get("adminScheme");
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
```

**QUERY_BUILDER** · HRAdminGroupService L104
```java
 102           String adminSchemeStr = pageCache.get("adminScheme");
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
 104 >             filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
 105               DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
 106               adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminGroupService L105
```java
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
 104               filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
 105 >             DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
 106               adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
 107           } else {
```

**WRITE_VIA_HELPER** · AdminGroupAddUserOp L93
```java
  91               saveUserIds.add((Long)primaryKeyValue);
  92           });
  93 >         SaveServiceHelper.save((DynamicObject[])saveList.toArray(new DynamicObject[0]));
  94           return saveUserIds;
  95       }
```

**THROW_BIZ_EXCEPTION** · HRAdminGroupService L120
```java
 118           catch (Exception e) {
 119               logger.error("[AdminGroupService]\u83b7\u53d6\u7528\u6237\u6240\u5c5e\u7ba1\u7406\u5458\u7ec4\u5931\u8d25\uff1a", (Throwable)e);
 120 >             throw new KDBizException(ResManager.loadKDString((String)"\u7ba1\u7406\u5458\u7ec4\u6570\u636e\u5f02\u5e38\u6216\u5143\u6570\u636e\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u6216\u91cd\u65b0\u5347\u7ea7\u7ba1\u7406\u5458\u6570\u636e\u3002", (String)"AdminGroupService_0", (String)"bos-mservice-permission", (Object[])new Object[0]));
 121           }
 122           String longNumberFirst = (String)userAdminGroup[0].getDynamicObject("usergroup").get("longnumber");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp -->

## ISV 扩展指引（基于 AdminGroupDelUserOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminGroupService L101
```java
  99           Long adminScheme;
 100           QFilter[] filters;
 101 >         Long userId = RequestContext.get().getCurrUserId();
 102           String adminSchemeStr = pageCache.get("adminScheme");
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
```

**QUERY_BUILDER** · HRAdminGroupService L104
```java
 102           String adminSchemeStr = pageCache.get("adminScheme");
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
 104 >             filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
 105               DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
 106               adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminGroupService L105
```java
 103           if (StringUtils.isEmpty((String)adminSchemeStr)) {
 104               filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
 105 >             DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
 106               adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
 107           } else {
```

**THROW_BIZ_EXCEPTION** · HRAdminGroupService L120
```java
 118           catch (Exception e) {
 119               logger.error("[AdminGroupService]\u83b7\u53d6\u7528\u6237\u6240\u5c5e\u7ba1\u7406\u5458\u7ec4\u5931\u8d25\uff1a", (Throwable)e);
 120 >             throw new KDBizException(ResManager.loadKDString((String)"\u7ba1\u7406\u5458\u7ec4\u6570\u636e\u5f02\u5e38\u6216\u5143\u6570\u636e\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u6216\u91cd\u65b0\u5347\u7ea7\u7ba1\u7406\u5458\u6570\u636e\u3002", (String)"AdminGroupService_0", (String)"bos-mservice-permission", (Object[])new Object[0]));
 121           }
 122           String longNumberFirst = (String)userAdminGroup[0].getDynamicObject("usergroup").get("longnumber");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp -->
