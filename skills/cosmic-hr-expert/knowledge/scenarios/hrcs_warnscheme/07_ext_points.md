# 扩展点全图 · hrcs_warnscheme

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrcs_warnscheme` 标品接入 **28** 个插件。

### FormPlugin（10）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WarnSchemeEditPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin`
- `WarnSchemeBCFilterEditPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin`
- `WarningSceneReceiverEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit`
- `WarnSceneMsgBaseEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit`
- `WarnSchemeReceiverEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`

### ListPlugin（6）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `WarnSchemeListPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin`
- `WarnSchemeTreeListPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin`

### OpPlugin（11）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `WarnSchemeOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp`
- `WarnSchemeBaseConditionOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp`
- `WarnReceiverSaveOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp`
- `WarnSchemeAdConditionOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp`
- `WarnSchemeLogOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`

### 其他（1）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin -->

## ISV 扩展指引（基于 WarnSchemeEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterCreateNewData`, `beforeBindData`, `afterBindData`, `afterLoadData`, `beforeItemClick`, `itemClick`, `propertyChanged`, `beforeDoOperation`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void setFieldControlType()`
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void itemClick(kd.bos.form.control.events.ItemClickEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseUtils L307
```java
 305       public static void setCreateField(DynamicObject dy) {
 306           if (HRBaseUtils.getDynamicObjectByField(dy, "creator") == 0L) {
 307 >             Long userId = Long.valueOf(RequestContext.get().getUserId());
 308               dy.set("creator", (Object)userId);
 309           }
```

**QUERY_BUILDER** · WarnObjTplService L135
```java
 133   
 134       public DynamicObject[] loadJoinEntities(Long sourceId) {
 135 >         QFilter[] qFilters = new QFilter[]{new QFilter("sourceid", "=", (Object)sourceId)};
 136           return this.joinEntityHelper.loadDynamicObjectArray(qFilters);
 137       }
```

**READ_VIA_HELPER** · WarnObjComUtils L84
```java
  82   public class WarnObjComUtils {
  83       public static void changeWarnFormEntityEntry(IDataModel thisModel, IPageCache thisPageCache, IFormView thisView, String entityNumber, String propKey, String appId) {
  84 >         List result = EntityCtrlServiceHelper.getCtrlDimensionByEntity((String)entityNumber, (String)propKey);
  85           result = result.stream().filter(en -> StringUtils.equalsIgnoreCase((String)"orgteam", (String)String.valueOf(en.get("datasource"))) && (((List)en.get("orgClassifys")).contains(1010L) || ((List)en.get("orgClassifys")).contains(1020L)) || StringUtils.equalsIgnoreCase((String)"basedata", (String)String.valueOf(en.get("datasource"))) && StringUtils.equalsIgnoreCase((String)"haos_adminorghrf7", (String)String.valueOf(en.get("entitytype")))).collect(Collectors.toList());
  86           MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType((String)entityNumber);
```

**THROW_BIZ_EXCEPTION** · HRBaseUtils L151
```java
 149           }
 150           catch (Exception e) {
 151 >             throw new KDBizException(e.getMessage());
 152           }
 153       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin -->

## ISV 扩展指引（基于 WarnSchemeBCFilterEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `propertyChanged`, `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · RuleEngineHelper L26
```java
  24   public class RuleEngineHelper {
  25       public static DynamicObject queryScene(Long id) {
  26 >         QFilter qf = new QFilter("id", "=", (Object)id);
  27           return QueryServiceHelper.queryOne((String)"brm_scene", (String)"id,bizappid.id", (QFilter[])new QFilter[]{qf});
  28       }
```

**READ_VIA_HELPER** · RuleEngineHelper L27
```java
  25       public static DynamicObject queryScene(Long id) {
  26           QFilter qf = new QFilter("id", "=", (Object)id);
  27 >         return QueryServiceHelper.queryOne((String)"brm_scene", (String)"id,bizappid.id", (QFilter[])new QFilter[]{qf});
  28       }
  29   
```

**THROW_BIZ_EXCEPTION** · ParamsUtil L161
```java
 159               if (ParamTypeEnum.DYNAMICOBJECT.getValue().equals(obj.getString(paramsType))) {
 160                   if (null == obj.get(object)) {
 161 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u7f16\u7801\u5bf9\u5e94\u7684\u4e1a\u52a1\u5bf9\u8c61\u4e0d\u5b58\u5728\u3002", (String)"ParamsUtil_0", (String)"bos-ext-hr", (Object[])new Object[0]), obj.get(number)));
 162                   }
 163                   String entityNumber = ((DynamicObject)obj.get(object)).get(NUMBER).toString();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeBCFilterEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit -->

## ISV 扩展指引（基于 WarningSceneReceiverEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `propertyChanged`, `beforeDoOperation`, `closedCallBack`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · RuleEngineHelper L26
```java
  24   public class RuleEngineHelper {
  25       public static DynamicObject queryScene(Long id) {
  26 >         QFilter qf = new QFilter("id", "=", (Object)id);
  27           return QueryServiceHelper.queryOne((String)"brm_scene", (String)"id,bizappid.id", (QFilter[])new QFilter[]{qf});
  28       }
```

**READ_VIA_HELPER** · RuleEngineHelper L27
```java
  25       public static DynamicObject queryScene(Long id) {
  26           QFilter qf = new QFilter("id", "=", (Object)id);
  27 >         return QueryServiceHelper.queryOne((String)"brm_scene", (String)"id,bizappid.id", (QFilter[])new QFilter[]{qf});
  28       }
  29   
```

**THROW_BIZ_EXCEPTION** · ParamsUtil L161
```java
 159               if (ParamTypeEnum.DYNAMICOBJECT.getValue().equals(obj.getString(paramsType))) {
 160                   if (null == obj.get(object)) {
 161 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u7f16\u7801\u5bf9\u5e94\u7684\u4e1a\u52a1\u5bf9\u8c61\u4e0d\u5b58\u5728\u3002", (String)"ParamsUtil_0", (String)"bos-ext-hr", (Object[])new Object[0]), obj.get(number)));
 162                   }
 163                   String entityNumber = ((DynamicObject)obj.get(object)).get(NUMBER).toString();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit -->

## ISV 扩展指引（基于 WarnSceneMsgBaseEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `propertyChanged`, `beforeItemClick`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · WarnSchemeService L56
```java
  54       @ExcludeFromJacocoGeneratedReport
  55       public static DynamicObject[] getSchemeMsgConfDys(Object[] schemeIds) {
  56 >         return MSG_CONF_HELPER.loadDynamicObjectArray(new QFilter[]{new QFilter("sourceid", "in", (Object)schemeIds)});
  57       }
  58   
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneMsgBaseEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit -->

## ISV 扩展指引（基于 WarnSchemeReceiverEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `beforeClosed`, `closedCallBack`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · WarnSchemeReceiverEdit L275
```java
 273       @ExcludeFromJacocoGeneratedReport
 274       private boolean hasRolePerm(String roleId, String appId, String entity) {
 275 >         QFilter roleIdFilter = new QFilter("roleid", "in", (Object)roleId);
 276           QFilter bizAppFilter = new QFilter("roleperm.bizapp", "=", (Object)appId);
 277           QFilter entityFilter = new QFilter("roleperm.entity", "=", (Object)entity);
```

**READ_VIA_HELPER** · WarnSchemeReceiverEdit L278
```java
 276           QFilter bizAppFilter = new QFilter("roleperm.bizapp", "=", (Object)appId);
 277           QFilter entityFilter = new QFilter("roleperm.entity", "=", (Object)entity);
 278 >         DynamicObject[] rolePerms = BusinessDataServiceHelper.load((String)"perm_roleperm", (String)"roleperm, roleperm.bizapp, roleperm.entity,roleperm.entityname,roleperm.permitem", (QFilter[])new QFilter[]{roleIdFilter, bizAppFilter, entityFilter});
 279           return rolePerms != null && rolePerms.length > 0;
 280       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeReceiverEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin -->

## ISV 扩展指引（基于 WarnSchemeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `beforeDoOperation`, `setFilter`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · WarnSchemeListPlugin L252
```java
 250       private Map<String, List<String>> getPlanIds(List<Object> ids) {
 251           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hrcs_warnscheme");
 252 >         return Arrays.stream(serviceHelper.queryOriginalArray("id,planid", new QFilter[]{new QFilter("id", "in", ids)})).collect(Collectors.toMap(it -> it.getString("id"), it -> Arrays.asList(it.getString("planid").split(",")), (oldValue, newValue) -> oldValue));
 253       }
 254   
```

**READ_VIA_HELPER** · WarnSchemeListPlugin L119
```java
 117                   Object id = focusNode.get("id");
 118                   TreeNode treeNode = ((ListView)this.getView()).getTreeListView().getTreeModel().getRoot().getTreeNode(String.valueOf(id), 15);
 119 >                 if (treeNode != null && "scene".equals(leafType = (String)treeNode.getData()) && !(exists = new HRBaseServiceHelper("hrcs_warnscene").isExists((Object)Long.parseLong(String.valueOf(id))))) {
 120                       eve.setCancel(true);
 121                       this.getView().showTipNotification(ResManager.loadKDString((String)"\u9009\u62e9\u7684\u9884\u8b66\u573a\u666f\u5df2\u88ab\u5220\u9664\uff0c\u8bf7\u5237\u65b0\u9875\u9762\u540e\uff0c\u91cd\u65b0\u9009\u62e9\u3002", (String)"WarnSchemeListPlugin_3", (String)"hrmp-hrcs-warn-formplugin", (Object[])new Object[0]));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin -->

## ISV 扩展指引（基于 WarnSchemeTreeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `setFilter`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public java.lang.String getBizAppId()`
- `public public void initializeTree(java.util.EventObject)`
- `public public void buildTreeListFilter(kd.bos.list.events.BuildTreeListFilterEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public kd.bos.orm.query.QFilter getHRAppFilter(kd.bos.list.ListShowParameter)`
- `public public void search(kd.bos.form.control.events.SearchEnterEvent)`
- `public public void refreshNode(kd.bos.form.control.events.RefreshNodeEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · WarnSchemeTreeListPlugin L149
```java
 147               DynamicObject[] apps;
 148               ArrayList qFilters = Lists.newArrayListWithExpectedSize((int)3);
 149 >             qFilters.add(new QFilter("id", "in", (Object)appIds).or(new QFilter("masterid", "in", (Object)appIds)));
 150               qFilters.add(new QFilter(DEPLOY_STATUS, "=", (Object)"2"));
 151               qFilters.add(new QFilter(VISIBLE, "=", (Object)Boolean.TRUE));
```

**READ_VIA_HELPER** · WarnSchemeTreeListPlugin L136
```java
 134           List cloudIds = HRCloudServiceHelper.getAllHRCommonCloudIdsSort();
 135           LOG.info("initChildrenNode_cloudIds_{}", (Object)cloudIds);
 136 >         DynamicObjectCollection sceneDys = new HRBaseServiceHelper("hrcs_warnscene").queryOriginalCollection("bizapp,name,number,id", new QFilter[0]);
 137           ArrayList cloudTreeNodesTemp = Lists.newArrayListWithExpectedSize((int)cloudTreeNodes.size());
 138           HashMap appNodesMap = Maps.newHashMapWithExpectedSize((int)16);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scheme.WarnSchemeTreeListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp -->

## ISV 扩展指引（基于 WarnSchemeOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp -->

## ISV 扩展指引（基于 WarnSchemeBaseConditionOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeBaseConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp -->

## ISV 扩展指引（基于 WarnReceiverSaveOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**REGISTER_VALIDATOR** · WarnReceiverSaveOp L27
```java
  25       public void onAddValidators(AddValidatorsEventArgs args) {
  26           super.onAddValidators(args);
  27 >         args.addValidator(new AbstractValidator(){
  28   
  29               public void validate() {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnReceiverSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp -->

## ISV 扩展指引（基于 WarnSchemeAdConditionOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.hr.hrcs.opplugin.web.earlywarn.ad.WarnAdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String getWarnAdType()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scheme.WarnSchemeAdConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp -->

## ISV 扩展指引（基于 WarnSchemeLogOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.WarnSchemeLogOp -->

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
