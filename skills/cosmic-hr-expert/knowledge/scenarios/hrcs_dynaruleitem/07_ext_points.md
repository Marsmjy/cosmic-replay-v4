# 扩展点全图 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于 OpenAPI queryEditablePlugins 实抓 (19 plugin) + 反编译 3 类（DynaRuleItemEdit / DynaItemDeleteOp / HRAdminStrictPlugin）
> **confidence**: verified
> **数据源**: `_auto_plugin_registry.md` + `_auto_plugin_semantics.md` + `form_lifecycle_rules.json` (2026-04-28)

---

## 一、扩展点矩阵

按生命周期方法 × 标品插件 × ISV 可覆盖整理：

| 阶段 | 标品插件 | ISV 推荐入口 | CS 引用 |
|---|---|---|---|
| 表单准入 · preOpenForm | HRAdminStrictPlugin（三连校验）+ HRBaseDataTplEdit | 自建 FormPlugin（并列挂）| 准入扩展（罕见） |
| 数据绑定后 · afterBindData | DynaRuleItemEdit（propName 反查 + presetView + lockEnumValue）+ HRBaseDataTplEdit + HRHiesButtonSwitchPlugin | 自建 FormPlugin · 数据装载后 UI 调整 | CS-02 |
| 字段联动 · propertyChanged | DynaRuleItemEdit（datatype→entitytype 锁值 / 清空）| 自建 FormPlugin · ISV 字段联动监听 | CS-02 |
| F7 弹窗前 · beforeF7Select | DynaRuleItemEdit（entitytype / relatruleparam / sourceentitytype 三 F7 过滤）| 自建 FormPlugin · 追加 F7 过滤 | CS-02 |
| 控件点击 · click | DynaRuleItemEdit（relatpropname / sourcepropname 跳 hrcs_choosefield_page）| 自建 FormPlugin · 自定义 click 行为 | CS-02 |
| 子页面回调 · closedCallBack | DynaRuleItemEdit（selRelEntityProp / selSourceEntityProp 回调双写 propKey+propName）| 自建 FormPlugin · 自定义子页面回调 | - |
| 工具栏点击前 · beforeItemClick | DynaRuleItemEdit（bar_save/bar_saveandnew 枚举校验 + clearUnMustData）| 自建 FormPlugin · 操作前阻断 | CS-03 |
| 分录操作前 · beforeDoOperation | DynaRuleItemEdit（deleteentry 引用阻断校验）| 自建 FormPlugin · deleteentry 前追加 ISV 引用校验 | CS-04 |
| 校验阶段 · onAddValidators | HRBaseDataStatusOp + DynaItemDeleteOp（delete 时注册 DynaItemDelValidator）| 自建 OP · 注册 Validator | CS-03 / CS-04 |
| 进事务前 · beforeExecute | CodeRuleOp / BdVersionSaveServicePlugin / HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp / DynaItemDeleteOp / CodeRuleDeleteOp | 自建 OP · 事务内业务逻辑 | CS-03 |
| 事务提交后 · afterExecute | HRBaseDataLogOp（标品日志落库）| 自建 OP · 通知/缓存/BEC | CS-05 |
| 列表加载 · setFilter | HRBaseDataCommonList（hrcs 11 表单共用）| 自建 ListPlugin · 加列表过滤 | CS-07 |
| 列表数据绑定前 · beforeBindData | HRBasedataLogList / HRBaseDataTplList | 自建 ListPlugin | CS-07 |
| 列表操作前 · beforeDoOperation | HRBasedataLogList | 自建 ListPlugin · 列表按钮拦截 | CS-07 |

---

## 二、19 个标品插件全表

数据来源：`_auto_plugin_registry.md`（OpenAPI queryEditablePlugins 实抓 hrcs_dynaruleitem）。

### 2.1 FormPlugin（主表单 · 14 个）

| # | 类 | 父类 | 关键生命周期 | 场景专属? | jar |
|---|---|---|---|---|---|
| 1 | `kd.bos.form.plugin.CodeRulePlugin` | AbstractFormPlugin | beforeBindData | 平台 | bos |
| 2 | `dev.tpl.base.kd.bos.form.plugin.templatebaseedit` | - | afterBindData / afterLoadData / beforeClosed | 平台 | bos |
| 3 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | HRDataBaseEdit | afterBindData / afterLoadData / beforeDoOperation / afterDoOperation / preOpenForm / beforeClosed | HR 通用 | hbp-formplugin |
| 4 | `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit` | HRCoreBaseBillEdit | - | HR 通用 | hbp-formplugin |
| 5 | `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | AbstractFormPlugin | afterBindData | HR 通用 | hbp-formplugin |
| **6** | **`kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`** | HRDynamicFormBasePlugin | preOpenForm（三连准入校验）| hrcs 11 表单共用 | **hrcs-formplugin** |
| **7** | **`kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit`** | **HRDataBaseEdit** | **registerListener / afterBindData / propertyChanged / click / closedCallBack / beforeF7Select / beforeDoOperation / beforeItemClick** | **场景专属** | **hrcs-formplugin** |
| 8 | `kd.hr.hbp.formplugin.web.template.TemplateBillEdit` | AbstractBasePlugIn | afterBindData | HR 通用 | hbp-formplugin |
| 9 | `kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit` | HRDataBaseEdit | beforeClosed | HR 通用 | hbp-formplugin |
| 10 | `kd.hr.hbp.formplugin.web.HRBaseDataCommonFieldLockEdit` | HRDataBaseEdit | afterBindData | HR 通用 | hbp-formplugin |
| 11 | `kd.hr.hbp.formplugin.web.template.HRBasedataLogList` | HRDataBaseList | beforeBindData / beforeDoOperation | HR 通用 | hbp-formplugin |

### 2.2 OP 插件（save / submit / audit / delete 等 · 5 个核心 + 3 个辅助）

| # | 类 | 父类 | 关键生命周期 | 场景专属? | jar |
|---|---|---|---|---|---|
| **12** | **`kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp`** | **HRDataBaseOp** | **onPreparePropertys / onAddValidators（注册 DynaItemDelValidator）** | **场景专属 · 仅 delete** | **hrcs-opplugin** |
| 13 | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | AbstractOperationServicePlugIn | onAddValidators / beforeExecute | HR 通用 | hbp-opplugin |
| 14 | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | AbstractOperationServicePlugIn | beforeExecute / afterExecute | HR 通用 | hbp-opplugin |
| 15 | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | AbstractOperationServicePlugIn | beforeExecute | HR 通用 | hbp-opplugin |
| 16 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | AbstractOperationServicePlugIn | beforeExecute | HR 通用 | hbp-opplugin |
| 17 | `kd.bos.business.plugin.CodeRuleOp` | - | onAddValidators / beforeExecute | 平台 | bos |
| 18 | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | - | beforeExecute / afterExecute | 平台 | bos |
| 19 | `kd.bos.coderule.CodeRuleDeleteOp` | - | beforeExecute | 平台 · delete 用 | bos |

### 2.3 场景专属插件（3 个 · ISV 扩展时必须了解）

| 类 | 类型 | 覆盖的 opKey / 生命周期 | ISV 能否继承 |
|---|---|---|---|
| **DynaRuleItemEdit** | FormPlugin | 所有表单生命周期（registerListener / afterBindData / propertyChanged / click / closedCallBack / beforeF7Select / beforeDoOperation / beforeItemClick）| **否**（无 SDK 注解） |
| **DynaItemDeleteOp** | OpPlugin | delete / importdata_hr / show_import_record_hr / export_* 等（onPreparePropertys / onAddValidators）| **否**（无 SDK 注解） |
| **HRAdminStrictPlugin** | FormPlugin | preOpenForm（hrcs 11 表单共用准入闸）| **否**（无 SDK 注解） |

---

## 三、ISV 最常覆盖 Top 5 扩展点

### 3.1 propertyChanged（CS-02 主战场 · 字段联动）

**标品**：DynaRuleItemEdit.propertyChanged —— 仅处理 datatype 切换：
- datatype=org → entitytype = "haos_adminorghrf7"
- datatype!=org → entitytype = null

**ISV 推荐方式**：自建 FormPlugin 继承 `HRDataBaseEdit` · 在 `propertyChanged` 里监听 ISV 字段联动。注意：**标品 entitytype 处理在前**，ISV 可以后覆盖 entitytype 值（如果业务需要）。

**可监听字段**：
- `datatype`（切 bd/org/enum 时联动 ISV 字段显隐/默认值）
- `valsourcetype`（切 1/2 时联动 ISV 字段）
- `isrelatparam`（勾选/取消关联时联动）
- ISV 自建字段间的联动

### 3.2 onAddValidators（CS-03 + CS-04 主战场）

**标品**：
- **save** opKey 链无场景专属 Validator（只有 HRBaseDataStatusOp 做状态机校验）
- **delete** opKey 链有 `DynaItemDelValidator`（DynaItemDeleteOp.onAddValidators 注册）—— 只查 `hrcs_dynascheme.condition` 引用

**ISV 推荐方式**：自建 OP 继承 `HRDataBaseOp`，在 `onAddValidators` 调 `args.addValidator(new MyValidator())`。

**注意**：标品 Validator 与 ISV Validator **并列执行** · 任一报错都阻断。ISV 不需要"先于标品"。

### 3.3 afterExecute（CS-05 主战场 · BEC 通知）

**标品**：HRBaseDataLogOp.afterExecute —— 仅操作日志落库。**没有任何场景专属 afterExecute**（DynaItemDeleteOp 没有覆盖 afterExecute）。

**ISV 推荐方式**：自建 OP 继承 `HRDataBaseOp`，在 `afterExecute` 调 `IEventService.triggerEventSubscribeJobs`。

**覆盖 opKey**：save / delete / enable / disable（根据业务需要选择）。

### 3.4 beforeF7Select（CS-02 · F7 过滤扩展）

**标品**：DynaRuleItemEdit.beforeF7Select 处理 3 个 F7：
- entitytype → QFilter modeltype=BaseFormModel
- relatruleparam → QFilter isrelatparam=false AND datatype in (bd, org)
- sourceentitytype → QFilter modeltype in (BaseFormModel, BillFormModel)

**ISV 推荐方式**：自建 FormPlugin 实现 `BeforeF7SelectListener`，在自己的 `beforeF7Select` 里追加 QFilter。

**注意**：标品 entitytype/sourceentitytype 用的是 `setFilter`（覆盖初始过滤）· relatruleparam 用的是 `getQFilters().add`（叠加）。ISV 追加时统一用 `add` 最安全。

### 3.5 beforeDoOperation（CS-04 · deleteentry 引用校验扩展）

**标品**：DynaRuleItemEdit.beforeDoOperation 仅拦截 `deleteentry`：
- 调 `DynaSchemeServiceHelper.queryRelDynaScheme(itemId)` 反查
- 解析所有方案的 condition JSON → 找 `conditionList[].value` 命中选中行的 value
- 命中 → `setCancel(true)` + 提示

**ISV 推荐方式**：自建 FormPlugin 的 `beforeDoOperation` 拦截 `deleteentry`，查 ISV 自建表的引用。

**注意**：这是 FormPlugin 端操作（**不走 OP 链**）—— 不能用 Validator，必须在 beforeDoOperation 里做。

---

## 四、OP 链扩展插槽（save / delete 两个核心 opKey）

### save 链（6 OP）

```
CodeRuleOp → BdVersionSaveServicePlugin → HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp
                                                                                                                      ↑
                                                                                                              ISV OP 插这里
```

### delete 链（4 OP）

```
CodeRuleDeleteOp → HRBaseDataStatusOp → HRBaseDataLogOp → DynaItemDeleteOp
                                                                         ↑
                                                                 ISV OP 插这里
```

### enable / disable 链（1 OP）

```
HRBaseDataLogOp
             ↑
     ISV OP 插这里（如 CS-05 通知）
```

---

## 五、formRule 扩展（前端规则引擎）

本场景 5 条 formRule（`form_lifecycle_rules.json` metadataRules.formRules）：

| ruleId | preCondition | 效果 |
|---|---|---|
| `4ZR5YQFF=KLX` | isrelatparam=true | 显示 relatruleparam + relatpropkey + relatpropname |
| `4ZR9JXP/TBN=` | datatype='bd' | 显示 entitytype · 隐藏 entryentity |
| `4ZRHJC9QK0/A` | valsourcetype='1' | 显示 sourceentitytype/sourcepropkey/sourcepropname · 隐藏 mservice* |
| `4ZRHJC9QK/B1` | valsourcetype='2' | 显示 mserviceapp/mserviceclass · 隐藏 source* |
| `4ZRHZU5CYXJR` | datatype='enum' | 显示 entryentity + enumbar · 隐藏 entitytype |

**ISV 可以加新 formRule**（用 `addRule` API），但 preCondition 不能引用标品字段的 `==''`（`kb_cosmic_addrule_traps.md` 已知坑）。ISV formRule 用 ISV 自建字段做 preCondition 最安全。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## ISV 扩展指引（基于 HRBaseDataTplEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `afterLoadData`, `beforeDoOperation`, `afterDoOperation`, `preOpenForm`, `beforeClosed`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void initImportData(kd.bos.entity.datamodel.events.InitImportDataEventArgs)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · HrEntityCommonService L46
```java
  44       public List<String> getParentEntity(String entryEntity) {
  45           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_formmeta");
  46 >         QFilter entityFilter = new QFilter("number", "=", (Object)entryEntity);
  47           DynamicObject dynamicObject = helper.queryOriginalOne("inheritpath", new QFilter[]{entityFilter});
  48           String inheritPath = dynamicObject.getString("inheritpath");
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## ISV 扩展指引（基于 HRBaseDataImportEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void initImportData(kd.bos.entity.datamodel.events.InitImportDataEventArgs)`
- `public public void beforeImportData(kd.bos.entity.datamodel.events.BeforeImportDataEventArgs)`
- `public public void afterImportData(kd.bos.entity.datamodel.events.ImportDataEventArgs)`
- `public public void queryImportBasedata(kd.bos.entity.datamodel.events.QueryImportBasedataEventArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · LogHandlerUtil L158
```java
 156                   DynamicObject logDy = new DynamicObject((DynamicObjectType)dataEntityType);
 157                   logDy.set("id", (Object)ids[index]);
 158 >                 logDy.set("username", (Object)RequestContext.get().getUserName());
 159                   logDy.set("opname", (Object)entityModifyInfo.getOperationKey());
 160                   logDy.set("opdate", (Object)now);
```

**QUERY_BUILDER** · LogHandlerUtil L346
```java
 344                   attachmentIds.add(refBaseObj.getLong("id"));
 345               }
 346 >             DynamicObject[] attachments = BusinessDataServiceHelper.load((String)"bd_attachment", (String)"id,name,url,createtime", (QFilter[])new QFilter[]{new QFilter("id", "in", (Object)attachmentIds)});
 347               Arrays.stream(attachments).forEach(attachment -> attachmentLogInfoList.add(new AttachmentLogInfo("2", (Object)attachment.getLong("id"), Long.valueOf(0L), attachment.getString(displayProp), LogHandlerUtil.getAttachmentFullUrl(URLEncoder.encode(attachment.getString("url"))), attachment.getDate("createtime"), displayProp)));
 348           }
```

**READ_VIA_HELPER** · LogHandlerUtil L208
```java
 206           if (oldDys == null || oldDys.length == 0) {
 207               List pks = Stream.of(newDys).map(DataEntityBase::getPkValue).distinct().collect(Collectors.toList());
 208 >             objectDynamicObjectMap = Arrays.stream(BusinessDataServiceHelper.load((Object[])pks.toArray(), (DynamicObjectType)dynamicObjectType)).collect(Collectors.toMap(DataEntityBase::getPkValue, dy -> dy));
 209           } else {
 210               objectDynamicObjectMap = Arrays.stream(oldDys).collect(Collectors.toMap(dy -> dy.get("id"), Function.identity(), (x1, x2) -> x2));
```

**THROW_BIZ_EXCEPTION** · HisModelCommonService L124
```java
 122                   LOGGER.error((Throwable)exception);
 123               }
 124 >             throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u7684\u5386\u53f2\u6a21\u578b\u5b9e\u4f53\u914d\u7f6e\u201c\u6a21\u5f0f\u9009\u62e9\u201d\u672a\u914d\u7f6e\uff0c\u8bf7\u5148\u5b8c\u6210\u914d\u7f6e\u3002", (String)"HisModelCommonService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityNumber));
 125           }
 126           return hisModelEntityConfig;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## ISV 扩展指引（基于 HRHiesButtonSwitchPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRHiesButtonSwitchPlugin L92
```java
  90               if (enableNoPermBtnHide) {
  91                   String appId = HRPermUtil.getAppIdFromShowParam((FormShowParameter)view.getFormShowParameter());
  92 >                 long currUserId = RequestContext.get().getCurrUserId();
  93                   boolean isPerm = PermissionServiceHelper.checkPermission((Long)currUserId, (String)appId, (String)billFormId, (String)permItem);
  94                   LOGGER.info("currUserId:{} appId:{} billFormId:{} permItem:{}", new Object[]{currUserId, appId, billFormId, permItem});
```

**QUERY_BUILDER** · HRQFilterHelper L17
```java
  15   public class HRQFilterHelper {
  16       public static QFilter buildEql(String filed, Object val) {
  17 >         return new QFilter(filed, "=", val);
  18       }
  19   
```

**CALL_CROSS_SERVICE** · HRPermUtil L65
```java
  63   
  64       public static Map<String, Object> queryPermConfig(String formId) {
  65 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hbss", (String)"IHBSSPermService", (String)"queryPermConfig", (Object[])new Object[]{formId});
  66       }
  67   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit -->

## ISV 扩展指引（基于 DynaRuleItemEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `propertyChanged`, `click`, `beforeF7Select`, `closedCallBack`, `beforeDoOperation`, `beforeItemClick`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · DynaRuleItemEdit L180
```java
 178           if (F_ENTITYTYPE.equals(propertyName)) {
 179               ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
 180 >             showParameter.getListFilterParameter().setFilter(new QFilter("modeltype", "=", (Object)"BaseFormModel"));
 181           } else if (HRStringUtils.equals((String)F_RELATE_RULE_ITEM, (String)propertyName)) {
 182               ListShowParameter showParameter = (ListShowParameter)evt.getFormShowParameter();
```

**READ_VIA_HELPER** · DynaRuleItemEdit L220
```java
 218           if (operateKey.equals("deleteentry")) {
 219               DynamicObject dataEntity = this.getModel().getDataEntity(true);
 220 >             Object[] schemeDynArr = DynaSchemeServiceHelper.queryRelDynaScheme((String)dataEntity.getString("id"));
 221               if (ArrayUtils.isEmpty((Object[])schemeDynArr)) {
 222                   return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## ISV 扩展指引（基于 HRBaseDataTplList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `filterContainerInit`, `preOpenForm`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void listColumnCompareTypesSet(kd.bos.form.events.ListColumnCompareTypesSetEvent)`
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## ISV 扩展指引（基于 HRBasedataLogList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · HRBasedataLogList L90
```java
  88           lsp.setBillFormId("hbss_history_logview");
  89           ListFilterParameter listFilterParameter = new ListFilterParameter();
  90 >         QFilter bizobj = new QFilter("bizobj", "=", (Object)billFormId);
  91           if (primaryKeyValue != 0L) {
  92               bizobj.and(new QFilter("modifybillid", "like", (Object)(String.valueOf(primaryKeyValue) + "%")));
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## ISV 扩展指引（基于 HRBaseDataStatusOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## ISV 扩展指引（基于 HRBaseDataLogOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## ISV 扩展指引（基于 HRBaseDataEnableOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## ISV 扩展指引（基于 HRBaseOriginalOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp -->

## ISV 扩展指引（基于 DynaItemDeleteOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp -->
