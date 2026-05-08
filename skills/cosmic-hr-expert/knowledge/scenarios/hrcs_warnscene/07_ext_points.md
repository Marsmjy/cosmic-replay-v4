# 扩展点全图 · hrcs_warnscene

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrcs_warnscene` 标品接入 **29** 个插件。

### FormPlugin（14）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `WarnSceneEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit`
- `WarningSceneDataFilterPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin`
- `WarnSceneCommonConditionsEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit`
- `WarnSceneCalConfigEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit`
- `WarningSceneReceiverPermEdit` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit`
- `WarnSceneAdFilterLeftTreeEditPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin`
- `WarnAdFilterRightTreeEditPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin`
- `WarnSceneAdFilterEditPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin`
- ...（共 14，详见 `_auto_plugin_registry.md`）

### ListPlugin（5）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `WarningSceneTreeListPlugin` ← `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin`

### OpPlugin（9）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `WarnSceneOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp`
- `WarnSceneCommonConditionOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp`
- `WarnSceneAdConditionOp` ← `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp`
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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit -->

## ISV 扩展指引（基于 WarnSceneEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `registerListener`, `afterLoadData`, `afterBindData`, `beforeItemClick`, `beforeClosed`, `propertyChanged`, `closedCallBack`, `beforeDoOperation`, `afterDoOperation`, `itemClick`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCopyData(java.util.EventObject)`
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin -->

## ISV 扩展指引（基于 WarningSceneDataFilterPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeItemClick`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public kd.bos.ext.hr.filter.control.HRFilter getHRFilter()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRFilterUtil L104
```java
 102           }
 103           if (RuleOperatorEnum.STARTS_WITH.getValue().equals(opt)) {
 104 >             return new QFilter(param, "like", (Object)(value + "%"));
 105           }
 106           if (RuleOperatorEnum.ENDS_WITH.getValue().equals(opt)) {
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRFilterUtil L154
```java
 152               }
 153               catch (ParseException e) {
 154 >                 throw new KDBizException(ResManager.loadKDString((String)"\u65e5\u671f\u7c7b\u578b\u8f6c\u6362\u9519\u8bef\u3002", (String)"HRFilterUtil_0", (String)"hrmp-hbp-business", (Object[])new Object[0]));
 155               }
 156           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneDataFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit -->

## ISV 扩展指引（基于 WarnSceneCommonConditionsEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeDoOperation`, `beforeClosed`, `afterDoOperation`, `afterBindData`, `closedCallBack`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void tabSelected(kd.bos.form.control.events.TabSelectEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · WarnCommonConditionUtils L49
```java
  47               HashMap ruleValueUniqueToSchemeName = Maps.newHashMapWithExpectedSize((int)16);
  48               HRBaseServiceHelper schemeHelper = new HRBaseServiceHelper("hrcs_warnscheme");
  49 >             for (DynamicObject dy : dbData = schemeHelper.queryOriginalArray(String.join((CharSequence)",", Lists.newArrayList((Object[])new String[]{"name", "basecondition"})), new QFilter[]{new QFilter("warnscene", "=", (Object)sceneId)})) {
  50                   String baseStr = dy.getString("basecondition");
  51                   if (!HRStringUtils.isNotEmpty((String)baseStr)) continue;
```

**THROW_BIZ_EXCEPTION** · HRLongValueParseService L62
```java
  60           catch (Exception exception) {
  61               String message = exception.getMessage();
  62 >             throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u503c\u8f6cLong\u7c7b\u578b\u9519\u8bef\uff0c\u503c\uff1a%1$s\uff0c\u9519\u8bef\u4fe1\u606f\uff1a%2$s\u3002", (String)"HRFormShowParameterService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), obj.toString(), message));
  63           }
  64       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonConditionsEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit -->

## ISV 扩展指引（基于 WarnSceneCalConfigEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCommonEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `closedCallBack`

### 可重写方法（target.java self）
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

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

**THROW_BIZ_EXCEPTION** · ParamsUtil L161
```java
 159               if (ParamTypeEnum.DYNAMICOBJECT.getValue().equals(obj.getString(paramsType))) {
 160                   if (null == obj.get(object)) {
 161 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u7f16\u7801\u5bf9\u5e94\u7684\u4e1a\u52a1\u5bf9\u8c61\u4e0d\u5b58\u5728\u3002", (String)"ParamsUtil_0", (String)"bos-ext-hr", (Object[])new Object[0]), obj.get(number)));
 162                   }
 163                   String entityNumber = ((DynamicObject)obj.get(object)).get(NUMBER).toString();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneCalConfigEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit -->

## ISV 扩展指引（基于 WarningSceneReceiverPermEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `propertyChanged`, `click`, `beforeF7Select`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void cellClick(kd.bos.form.control.events.CellClickEvent)`
- `public public void cellDoubleClick(kd.bos.form.control.events.CellClickEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void getEntityType(kd.bos.entity.datamodel.events.GetEntityTypeEventArgs)`
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneReceiverPermEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin -->

## ISV 扩展指引（基于 WarnSceneAdFilterLeftTreeEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterLeftTreeEditPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.util.List<kd.hr.hbp.common.model.complexobj.labelandreport.FieldTreeNode> getTreeFieldNodes()`
- `protected protected java.lang.String getCalFieldCacheKey()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterLeftTreeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin -->

## ISV 扩展指引（基于 WarnAdFilterRightTreeEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void treeNodeClick(kd.bos.form.control.events.TreeNodeEvent)`
- `public public void treeNodeDoubleClick(kd.bos.form.control.events.TreeNodeEvent)`
- `public public void search(kd.bos.form.control.events.SearchEnterEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRFunctionHelper L313
```java
 311           String appId = AppMetadataCache.getAppInfoByNumber((String)appNumber).getId();
 312           appIds.add(appId);
 313 >         QFilter filter = new QFilter("masterid", "=", (Object)appId);
 314           DynamicObjectCollection dyColl = APP_HELPER.queryOriginalCollection("id", new QFilter[]{filter});
 315           if (dyColl != null && dyColl.size() > 0) {
```

**READ_VIA_HELPER** · FormulaUtils L456
```java
 454               String number = son.getString("number");
 455               try {
 456 >                 MainEntityType dataEntityType = MetadataServiceHelper.getDataEntityType((String)number);
 457                   if (StringUtils.isEmpty((CharSequence)dataEntityType.getAlias())) continue;
 458                   numberList.add(number);
```

**THROW_BIZ_EXCEPTION** · HRFunctionHelper L170
```java
 168       public static Object executeFunction(String fcExecuteCode, Map<String, Object> params) throws NotFoundException, CannotCompileException, NoSuchAlgorithmException, HRException {
 169           if (StringUtils.isEmpty((CharSequence)fcExecuteCode)) {
 170 >             throw new KDBizException("fcExecuteCode isEmpty");
 171           }
 172           Expr expr = ExprParser.parse((String)fcExecuteCode);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterRightTreeEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin -->

## ISV 扩展指引（基于 WarnSceneAdFilterEditPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hrcs.formplugin.web.earlywarn.ad.WarnAdFilterEditPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String getWarnADType()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarnSceneAdFilterEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit -->

## ISV 扩展指引（基于 WarnDataPermEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `click`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void cellClick(kd.bos.form.control.events.CellClickEvent)`
- `public public void cellDoubleClick(kd.bos.form.control.events.CellClickEvent)`
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void getEntityType(kd.bos.entity.datamodel.events.GetEntityTypeEventArgs)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.objecttpl.WarnDataPermEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin -->

## ISV 扩展指引（基于 WarningSceneTreeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.template.HRF7TreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public java.lang.String getBizAppId()`
- `public public void search(kd.bos.form.control.events.SearchEnterEvent)`
- `public public void refreshNode(kd.bos.form.control.events.RefreshNodeEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.earlywarn.scene.WarningSceneTreeListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp -->

## ISV 扩展指引（基于 WarnSceneOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

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

**THROW_BIZ_EXCEPTION** · ParamsUtil L161
```java
 159               if (ParamTypeEnum.DYNAMICOBJECT.getValue().equals(obj.getString(paramsType))) {
 160                   if (null == obj.get(object)) {
 161 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%s\u7f16\u7801\u5bf9\u5e94\u7684\u4e1a\u52a1\u5bf9\u8c61\u4e0d\u5b58\u5728\u3002", (String)"ParamsUtil_0", (String)"bos-ext-hr", (Object[])new Object[0]), obj.get(number)));
 162                   }
 163                   String entityNumber = ((DynamicObject)obj.get(object)).get(NUMBER).toString();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp -->

## ISV 扩展指引（基于 WarnSceneCommonConditionOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneCommonConditionOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp -->

## ISV 扩展指引（基于 WarnSceneAdConditionOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.hr.hrcs.opplugin.web.earlywarn.ad.WarnAdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String getWarnAdType()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.earlywarn.scene.WarnSceneAdConditionOp -->
