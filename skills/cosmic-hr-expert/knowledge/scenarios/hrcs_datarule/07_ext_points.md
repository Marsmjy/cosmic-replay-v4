# 扩展点全图 · 数据规则 (hrcs_datarule)

> **状态**：基于 24 plugin 实抓 + 3 类反编译类名整合
> **confidence**：real_deploy

## 一、扩展点矩阵

按生命周期方法 × 是否标品 × 是否 ISV 可覆盖 整理：

| 阶段 | 标品插件 | ISV 推荐入口 | CS 引用 |
|---|---|---|---|
| 表单加载 · preOpenForm | HRBaseDataTplEdit / HRAdminStrictPlugin / HRBaseDataTplList | 自建 FormPlugin (并列挂)| 准入扩展（罕见）|
| 表单加载 · afterCreateNewData | HRDataRuleEditPlugin（场景专属）| 自建 FormPlugin · 走 propertyChanged | CS-02 |
| 表单加载 · afterLoadData | HRBaseDataTplEdit / HRDataRuleEditPlugin | 自建 FormPlugin · setEnable / showHide | CS-02 |
| 表单加载 · afterBindData | 5 个标品（含场景专属 HRDataRuleEditPlugin）| 自建 FormPlugin · 数据装载后 UI 调整 | CS-01 / CS-02 |
| 字段联动 · propertyChanged | HRDataRuleEditPlugin（entitynum 切换处理）| 自建 FormPlugin · ISV 字段联动 | CS-02 |
| F7 弹窗 · beforeF7Select | HRDataRuleEditPlugin（FilterGrid F7） | 自建 FormPlugin · BasedataField F7 | CS-02 |
| 操作前 · beforeDoOperation | 3 个标品（含 HRDataRuleEditPlugin）| 自建 FormPlugin · 操作前阻断 | CS-02 / CS-03 |
| 校验阶段 · onAddValidators | HRDataRuleSaveOp / HRBaseDataStatusOp | 自建 OP · 注册 Validator | CS-03 / CS-04 |
| 进事务前 · beforeExecute | 5 个标品 OP | 自建 OP · 事务内业务逻辑 | CS-03 |
| 事务提交后 · afterExecute | 2 个标品 OP（HRDataRuleSaveOp / HRBaseDataLogOp）| 自建 OP · 通知/缓存/BEC | CS-03 / CS-05 |
| 列表加载 · setFilter | HRBaseDataCommonList | 自建 ListPlugin · 加列表过滤 | CS-07 |
| 列表加载 · beforeBindData | HRBasedataLogList / HRBaseDataTplList | 自建 ListPlugin | CS-07 |
| 列表操作 · beforeDoOperation | HRBasedataLogList | 自建 ListPlugin · 列表按钮拦截 | CS-07 |

## 二、24 个标品插件全表

数据来源：`_auto_plugin_registry.md`（OpenAPI queryEditablePlugins 实抓）。

### 2.1 FormPlugin（12 个）

| # | 类 | 父类 | 关键生命周期 | jar |
|---|---|---|---|---|
| 1 | `kd.bos.form.plugin.CodeRulePlugin` | – | – | – |
| 2 | `dev.tpl.base.kd.bos.form.plugin.templatebaseedit` | – | – | – |
| 3 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | HRDataBaseEdit | afterBindData / afterLoadData / beforeDoOperation / afterDoOperation / preOpenForm / beforeClosed | hbp-formplugin |
| 4 | `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit` | HRCoreBaseBillEdit | – | hbp-formplugin |
| 5 | `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | AbstractFormPlugin | afterBindData | hbp-formplugin |
| 6 | `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin` ⭐ | HRDynamicFormBasePlugin | preOpenForm | hrcs-formplugin |
| 7 | `kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin` ⭐⭐ | HRDataBaseEdit | registerListener / afterLoadData / afterCreateNewData / afterBindData / propertyChanged / beforeDoOperation | hrcs-formplugin |
| 8 | `kd.hr.hbp.formplugin.web.template.TemplateBillEdit` | AbstractBasePlugIn | afterBindData | hbp-formplugin |
| 9 | `kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit` | HRDataBaseEdit | beforeClosed | hbp-formplugin |
| 10 | `kd.hr.hbp.formplugin.web.HRBaseDataCommonFieldLockEdit` | HRDataBaseEdit | afterBindData | hbp-formplugin |
| 11 | `kd.hr.hbp.formplugin.web.template.HRBasedataLogList` | HRDataBaseList | beforeBindData / beforeDoOperation | hbp-formplugin |
| 12-16 | （5 个标品列表辅助）| – | – | – |

### 2.2 OP 插件（5 个核心）

| # | 类 | 父类 | 生命周期 | jar |
|---|---|---|---|---|
| 19 | `kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp` ⭐⭐ | HRDataBaseOp | onAddValidators / beforeExecute / afterExecute | hrcs-opplugin |
| 20 | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | AbstractOperationServicePlugIn | onAddValidators / beforeExecute | hbp-opplugin |
| 21 | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | AbstractOperationServicePlugIn | beforeExecute / afterExecute | hbp-opplugin |
| 22 | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | AbstractOperationServicePlugIn | beforeExecute | hbp-opplugin |
| 23 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | AbstractOperationServicePlugIn | beforeExecute | hbp-opplugin |

### 2.3 平台辅助（7 个）

CodeRuleOp / BdVersionSaveServicePlugin / CodeRuleDeleteOp / BdVersionListPlugin / BaseDataNameVersionListPlugin / HRBaseDataCommonList / HRBaseDataCommonMobList。

## 三、ISV 最常覆盖 Top 5

### 3.1 onAddValidators（CS-03 主战场）

**标品**：HRDataRuleSaveOp.onAddValidators（注册 HRDataRuleSaveValidator）+ HRBaseDataStatusOp.onAddValidators（状态机校验）。

**ISV 推荐方式**：自建 OP 继承 HRDataBaseOp · 在自己的 onAddValidators 调 args.addValidator(new MyValidator())。

**典型场景**：
- 同名校验（CS-03）
- 下游引用查（CS-04）
- 字段一致性校验
- audit 二次 FilterBuilder 校验

⚠️ 标品 HRDataRuleSaveValidator 跟 ISV Validator 是**并列执行** · 任一报错都阻断 · ISV 不需要"先于标品"。

### 3.2 afterExecuteOperationTransaction（CS-03 + CS-05 主战场）

**标品**：HRDataRuleSaveOp.afterExecute（仅 save · 处理 modify 模式 · 清缓存 + 通知 + 写日志）+ HRBaseDataLogOp.afterExecute（标品日志）。

**ISV 推荐方式**：自建 OP 继承 HRDataBaseOp · 在 afterExecute 调：
- `IEventService.triggerEventSubscribeJobs`（CS-05 BEC）
- `HRPermCacheMgr.clearCache`（CS-03 补 audit 后清缓存）
- `DataRuleLogServiceHelper.dataRuleLogInit`（CS-03 补 audit/disable 等模式日志）

⚠️ 不在事务内 · 失败不影响主事务 · 但要 try-catch 防止吞错。

### 3.3 propertyChanged（CS-02 主战场）

**标品**：HRDataRuleEditPlugin.propertyChanged（仅处理 entitynum 切换 · 清规则 + 重建 FilterGrid）。

**ISV 推荐方式**：自建 FormPlugin 继承 HRDataBaseEdit · 在 propertyChanged 处理 ISV 自己加的字段（如 ${ISV_FLAG}_rulecategory · 见 CS-02）。

⚠️ propertyChanged 里 setValue 其他字段 → 死循环 · 必用 beginInit/endInit（PR-004）。

### 3.4 beforeF7Select（CS-02 主战场）

**标品**：HRDataRuleEditPlugin.beforeF7Select（FilterGrid 行字段 F7 · 加 iscurrentversion=1 过滤）。

**ISV 推荐方式**：自建 FormPlugin · 给 entitynum 等 BasedataField 字段挂 BeforeF7Select 监听 · 用 evt.addCustomQFilter 加 ISV 过滤。

⚠️ FilterGrid 用 BeforeFilterF7Select（不同接口）· BasedataField 用 BeforeF7Select · 不要混。

### 3.5 setFilter（CS-07 主战场）

**标品**：HRBaseDataCommonList / HRBaseDataTplList。

**ISV 推荐方式**：自建 ListPlugin 继承 HRDataBaseList · 覆写 setFilter · super.setFilter 后追加 QFilter。

⚠️ super.setFilter 必须调 · 否则丢标品过滤。

## 四、不推荐的扩展点

| 扩展点 | 不推荐原因 | 替代 |
|---|---|---|
| 继承 HRDataRuleEditPlugin | 场景专属类 · 标品改签名跟不上（PR-001）| 并列挂新 FormPlugin · 继承 HRDataBaseEdit |
| 继承 HRDataRuleSaveOp | 同上（PR-001）| 并列挂新 OP · 继承 HRDataBaseOp |
| 继承 HRDataRuleSaveValidator | 同上（PR-001）| 并列加 Validator · 继承 AbstractValidator |
| 继承 HRAdminStrictPlugin | hrcs 11 表单已挂 · 重复挂校验报错 | 复用即可（preOpenForm 阻断已经够）|
| 继承 HRBaseDataCommonList | 标品列表 · 跨版本可能改 | 自建 ListPlugin 继承 HRDataBaseList |
| 继承 HisModelOPCommonPlugin | @SdkInternal 时序内部类 | 不要继承 |
| 继承 HisUniqueValidateOp | @SdkInternal | 不要继承 |
| 继承 AbsOrgBaseOp | 组织域专属 · hrcs 不适用 | 用 HRDataBaseOp |
| 继承 BdVersionSaveServicePlugin | 平台基础资料版本 · 行为复杂 | 自建独立 OP |
| 继承 CodeRuleOp / CodeRuleDeleteOp | 平台编码规则 · 业务侧通过【编码规则】基础资料配置即可（PR-006）| 不需代码定制 |

## 五、扩展点 → CS 索引

| 扩展点 | 对应 CS |
|---|---|
| modifyMeta add field | CS-01 |
| FormPlugin propertyChanged + beforeF7Select | CS-02 |
| OP onAddValidators + Validator | CS-03 / CS-04 |
| OP afterExecute（清缓存 + 写日志）| CS-03 |
| OP afterExecute（发 BEC）| CS-05 |
| modifyMeta add entryentity + ID.genLongId | CS-06 |
| ListPlugin setFilter | CS-07 |

详见 `06_customization_solutions.md`。

## 六、扩展点的注册路径

苍穹注册插件用 modifyMeta + targetType（**大写枚举** · R20 实证）：

| targetType | 适用场景 |
|---|---|
| `BILL_FORM` | 主表单 FormPlugin（CS-02 / CS-03 用）|
| `LIST_FORM` | 列表插件 ListPlugin（CS-07 用）|
| `OPERATION` | OP 插件（CS-03 / CS-05 用）|
| `MOBILE_BILL_FORM` | 移动端表单（hrcs_datarule 不主用）|

⚠️ 不是 `pluginType` · 也不是小写 · R20 已实证。

## 七、扩展点性能 / 风险评级

| 扩展点 | 性能影响 | 风险 | 备注 |
|---|---|---|---|
| modifyMeta add field | 一次性 | 低 | 标品升级覆盖时需重新加 · 因此要进版本控制 |
| propertyChanged | 每次字段变都触发 | 低 | 注意 setValue 死循环 |
| beforeF7Select | 每次 F7 都触发 | 低 | 加 QFilter 不要查数据库 |
| onAddValidators | 每次操作都触发 | 中 | Validator.validate 跑慢会拖整个保存 |
| beforeExecute | 进事务内 | 中 | 慢 SQL 会拖事务 · 阻断要 fail-fast |
| afterExecute | 事务外 | 低 | 失败不影响主事务 · 但要 try-catch |
| setFilter | 每次列表查询触发 | 中 | 复杂 QFilter 影响 SQL · 大数据量场景注意 |

## 八、扩展点联动建议（多 CS 共建）

实际 ISV 项目通常组合多个 CS · 建议组织成：

```
mycompany.${ISV_FLAG}.hrcs/
├─ formplugin/
│  ├─ TdkwDataRuleCategoryEdit.java        // CS-02 字段联动
│  ├─ TdkwParamEntryEdit.java               // CS-06 参数项管理
│  └─ TdkwDataRuleListEnhance.java          // CS-07 列表过滤
├─ opplugin/
│  ├─ TdkwDataRuleSaveEnhanceOp.java        // CS-03 save 增强
│  ├─ TdkwDataRuleAuditOp.java              // CS-03 audit 增强
│  ├─ TdkwDataRuleDeleteCheckOp.java        // CS-04 删除校验
│  ├─ TdkwDataRuleBecPublishOp.java         // CS-05 BEC 发布
│  └─ validator/
│     ├─ TdkwDataRuleSameNameValidator.java
│     ├─ TdkwDataRuleRefCheckValidator.java
│     └─ TdkwParamConsistencyValidator.java
└─ bec/
   └─ TdkwDataRuleAuditSubscriber.java      // CS-05 BEC 订阅
```

通过 modifyMeta 把对应类挂在 hrcs_datarule 的不同操作 / 表单 · ISV 7 个 CS 完整覆盖。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin -->

## ISV 扩展指引（基于 HRDataRuleEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeF7Select`, `afterLoadData`, `afterCreateNewData`, `afterBindData`, `propertyChanged`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · PermFormCommonUtil L89
```java
  87           HashMap<Long, String[]> result = new HashMap<Long, String[]>(16);
  88           String selFields = "id,number,name";
  89 >         for (DynamicObject dObj : arrDObj = BusinessDataServiceHelper.load((String)"bos_org", (String)selFields, (QFilter[])new QFilter[]{new QFilter("id", "in", orgIds)})) {
  90               Long orgId = dObj.getLong("id");
  91               String orgNumber = dObj.getString("number");
```

**READ_VIA_HELPER** · PermFormCommonUtil L89
```java
  87           HashMap<Long, String[]> result = new HashMap<Long, String[]>(16);
  88           String selFields = "id,number,name";
  89 >         for (DynamicObject dObj : arrDObj = BusinessDataServiceHelper.load((String)"bos_org", (String)selFields, (QFilter[])new QFilter[]{new QFilter("id", "in", orgIds)})) {
  90               Long orgId = dObj.getLong("id");
  91               String orgNumber = dObj.getString("number");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.datarule.HRDataRuleEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.TemplateBillEdit -->

## ISV 扩展指引（基于 TemplateBillEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.TemplateBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.TemplateBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.base.AbstractBasePlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.TemplateBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.TemplateBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.TemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit -->

## ISV 扩展指引（基于 HRBaseDataCommonEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeClosed`

### 可重写方法（target.java self）
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public boolean isView()`
- `protected protected java.util.List<java.lang.String> getUnCheckField()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonList -->

## ISV 扩展指引（基于 HRBaseDataCommonList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `protected protected kd.bos.orm.query.QFilter getStatusFilter()`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HRBaseDataCommonList L30
```java
  28   
  29       protected QFilter getStatusFilter() {
  30 >         return new QFilter("status", "!=", (Object)"LD");
  31       }
  32   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

## ISV 扩展指引（基于 HRBaseDataCommonMobList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractMobListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `protected protected kd.bos.orm.query.QFilter getStatusFilter()`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HRBaseDataCommonMobList L24
```java
  22   
  23       protected QFilter getStatusFilter() {
  24 >         QFilter qFilter = new QFilter("enable", "=", (Object)Character.valueOf('1'));
  25           qFilter.and(new QFilter("status", "!=", (Object)"LD"));
  26           return qFilter;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp -->

## ISV 扩展指引（基于 HRDataRuleSaveOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · PermNotifyService L53
```java
  51               ThreadPools.executeOnce((String)"kd.hr.hrcs.bussiness.service.perm.PermNotifyService.notifyByDataProperty", () -> {
  52                   HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_userrolerelat");
  53 >                 DynamicObject[] items = serviceHelper.query("user.id", new QFilter[]{new QFilter("role.id", "=", (Object)roleId), new QFilter("customenable", "=", (Object)dataProperty)});
  54                   List<Long> userIds = Arrays.stream(items).map(it -> it.getLong("user.id")).collect(Collectors.toList());
  55                   PermNotifyService.notifyUsers(userIds);
```

**READ_VIA_HELPER** · OrgServiceUtil L21
```java
  19   
  20       public static long getAdminRootOrgId() {
  21 >         return OrgUnitServiceHelper.getRootOrgId();
  22       }
  23   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.HRDataRuleSaveOp -->

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
