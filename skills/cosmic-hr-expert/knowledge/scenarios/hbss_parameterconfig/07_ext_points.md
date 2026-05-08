# 扩展点全图 · hbss_parameterconfig

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hbss_parameterconfig` 标品接入 **12** 个插件。

### FormPlugin（7）
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `AppConfigEditPlugin` ← `kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin`
- `BaseDataDisablePlugin` ← `kd.bd.assistant.plugin.basedata.BaseDataDisablePlugin`
- `BaseDataEnablePlugin` ← `kd.bd.assistant.plugin.basedata.BaseDataEnablePlugin`

### ListPlugin（2）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `HRConfigTreeListPlugin` ← `kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin`

### OpPlugin（3）
- `HRDataBaseOp` ← `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- `HrParamsConfigOp` ← `kd.hr.hbss.opplugin.web.HrParamsConfigOp`
- `BasedataConfigOp` ← `kd.hr.hbss.opplugin.web.BasedataConfigOp`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## ISV 扩展指引（基于 HRBaseUeEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HrEntityCommonService L46
```java
  44       public List<String> getParentEntity(String entryEntity) {
  45           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_formmeta");
  46 >         QFilter entityFilter = new QFilter("number", "=", (Object)entryEntity);
  47           DynamicObject dynamicObject = helper.queryOriginalOne("inheritpath", new QFilter[]{entityFilter});
  48           String inheritPath = dynamicObject.getString("inheritpath");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin -->

## ISV 扩展指引（基于 AppConfigEditPlugin 真实证）

> FQN: `kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `propertyChanged`, `registerListener`, `beforeBindData`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · AppConfigEditPlugin L125
```java
 123           String fieldKey = beforeF7SelectEvent.getProperty().getName();
 124           if (HRStringUtils.equals((String)fieldKey, (String)BASEDATAFIELD)) {
 125 >             QFilter qFilter = new QFilter("modeltype", "=", (Object)"BaseFormModel");
 126               qFilter.and(new QFilter("bizappid.bizcloud.number", "in", AppConfigEditPlugin.getAllHRCloudIdInStr()));
 127               Set banBdNums = HRBdWhiteListServiceHelper.queryAllBanModifyBDNum();
```

**READ_VIA_HELPER** · AppConfigEditPlugin L106
```java
 104           if (baseDataDyo != null) {
 105               String entityNumber = baseDataDyo.getString("number");
 106 >             DynamicObject whiteListDyo = HRBdWhiteListServiceHelper.queryWhiteListByEntityNum((String)entityNumber);
 107               if (whiteListDyo == null) {
 108                   return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin -->

## ISV 扩展指引（基于 HRConfigTreeListPlugin 真实证）

> FQN: `kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.list.plugin.StandardTreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforePackageData`, `registerListener`, `beforeDoOperation`, `afterCreateNewData`, `setFilter`

### 可重写方法（target.java self）
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void initialize()`
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void treeNodeClick(kd.bos.form.control.events.TreeNodeEvent)`
- `public public void queryTreeNodeChildren(kd.bos.form.control.events.TreeNodeEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HRConfigTreeListPlugin L175
```java
 173               }
 174           }
 175 >         QFilter qFilter = new QFilter("basedatafield", "in", (Object)formIdList);
 176           this.getPageCache().put("lefttree_qfilter_json", qFilter.toSerializedString());
 177       }
```

**READ_VIA_HELPER** · HRConfigTreeListPlugin L71
```java
  69               return map;
  70           }, (key1, key2) -> key1));
  71 >         DynamicObject[] whiteLists = HRBdWhiteListServiceHelper.queryAllWhiteList();
  72           Map<String, DynamicObject> whiteListMap = Arrays.stream(whiteLists).filter(dyo -> HRStringUtils.isNotEmpty((String)dyo.getString("basedata.number"))).collect(Collectors.toMap(dy -> dy.getString("basedata.number"), Function.identity(), (x, y) -> y));
  73           for (DynamicObject pageDatum : event.getPageData()) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## ISV 扩展指引（基于 HRDataBaseOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `protected protected void setRecordLimit(int)`

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · HRDataBaseOp L39
```java
  37               case "save": {
  38                   if (recordCount <= this.recordLimit) break;
  39 >                 throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6570\u636e\u91cf\u8d85\u8fc7\u9650\u5236\u9608\u503c%1$s\uff0c\u5f53\u524d\u8bb0\u5f55\u6570\uff1a%2$s\u3002", (String)"HRDataBaseOp_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), this.recordLimit, recordCount));
  40               }
  41           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.opplugin.web.HrParamsConfigOp -->

## ISV 扩展指引（基于 HrParamsConfigOp 真实证）

> FQN: `kd.hr.hbss.opplugin.web.HrParamsConfigOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.HrParamsConfigOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HrParamsConfigOp L69
```java
  67                   return false;
  68               }
  69 >             QFilter idFilter = new QFilter("status", "in", (Object)Lists.newArrayList((Object[])new String[]{"A", "B"}));
  70               DynamicObject[] dynamicObjects = serviceHelper.queryOriginalArray("id", new QFilter[]{idFilter});
  71               return dynamicObjects.length != 0;
```

**READ_VIA_HELPER** · HrParamsConfigOp L64
```java
  62           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(number);
  63           try {
  64 >             MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)number);
  65               IDataEntityProperty property = dataEntityType.findProperty("status");
  66               if (property == null) {
```

**REGISTER_VALIDATOR** · HrParamsConfigOp L46
```java
  44   
  45       public void onAddValidators(AddValidatorsEventArgs args) {
  46 >         args.addValidator(new AbstractValidator(){
  47   
  48               public void validate() {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.HrParamsConfigOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.HrParamsConfigOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.opplugin.web.HrParamsConfigOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.opplugin.web.BasedataConfigOp -->

## ISV 扩展指引（基于 BasedataConfigOp 真实证）

> FQN: `kd.hr.hbss.opplugin.web.BasedataConfigOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.BasedataConfigOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.BasedataConfigOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.BasedataConfigOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbss.opplugin.web.BasedataConfigOp -->
