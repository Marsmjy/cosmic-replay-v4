# 扩展点全图 · haos_adminorgdetail

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `haos_adminorgdetail` 标品接入 **41** 个插件。

### FormPlugin（12）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `HisModelFormCommonPlugin` ← `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
- `NewAdminorgDetailEditPlugin` ← `kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin`
- `AdminOrgPageRightDynamicPlugin` ← `kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin`
- `HRRelatePageRightDynamicPlugin` ← `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
- `AdminOrgDetailEditPlugin` ← `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin`
- `HisModelListCommonPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`
- `HisModelOPCommonPlugin` ← `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`

### ListPlugin（12）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `HisModelF7ListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
- `HisModelFilterPanelListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
- `HisModelFilterPanelF7ListPlugin` ← `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
- `OrgDetailList` ← `kd.hr.haos.formplugin.web.adminorg.OrgDetailList`
- `AdminOrgDetailBUListPlugin` ← `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin`
- `AdminOrgDetailListPlugin` ← `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin`
- `AdminOrgDisableAndIncludeFilterListPlugin` ← `kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin`
- `HisModelMobileListPlugin` ← `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`

### OpPlugin（14）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `HisUniqueValidateOp` ← `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
- `AdminOrgFastSaveOp` ← `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `AdminOrgFastAuditOp` ← `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp`
- `AdminOrgFastParentChangeOp` ← `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp`
- `AdminOrgInitSaveOp` ← `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp`
- `AdminOrgFastDisableOrgOp` ← `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp`
- ...（共 14，详见 `_auto_plugin_registry.md`）

### 其他（3）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`
- `HisBaseDataF7FastFilter` ← `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
- `AdminOrgF7FastFilter` ← `kd.bos.ext.hr.filter.AdminOrgF7FastFilter`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## ISV 扩展指引（基于 HisModelFormCommonPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `afterCreateNewData`, `afterLoadData`, `afterBindData`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void initialize()`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

## ISV 扩展指引（基于 NewAdminorgDetailEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `afterDoOperation`, `afterLoadData`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · NewAdminorgDetailEditPlugin L273
```java
 271               catch (ParseException parseException) {
 272                   logger.error((Throwable)parseException);
 273 >                 throw new KDBizException(new ErrorCode("NewAdminorgDetailEditPlugin", parseException.getMessage()), new Object[0]);
 274               }
 275           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

## ISV 扩展指引（基于 AdminOrgPageRightDynamicPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

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

**THROW_BIZ_EXCEPTION** · AdminOrgPageRightDynamicPlugin L73
```java
  71               catch (ParseException e) {
  72                   logger.error("AdminOrgPageRightDynamicPlugin parse date error!");
  73 >                 throw new KDBizException(new ErrorCode("AdminOrgPageRightDynamicPlugin", e.getMessage()), new Object[0]);
  74               }
  75           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## ISV 扩展指引（基于 HRRelatePageRightDynamicPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `afterBindData`, `beforeBindData`, `afterDoOperation`, `click`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void loadCustomControlMetas(kd.bos.form.events.LoadCustomControlMetasArgs)`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void onGetControl(kd.bos.form.events.OnGetControlArgs)`
- `public public void click(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseUtils L307
```java
 305       public static void setCreateField(DynamicObject dy) {
 306           if (HRBaseUtils.getDynamicObjectByField(dy, "creator") == 0L) {
 307 >             Long userId = Long.valueOf(RequestContext.get().getUserId());
 308               dy.set("creator", (Object)userId);
 309           }
```

**QUERY_BUILDER** · HRRelatePageRightDynamicPlugin L213
```java
 211           pageFormShowParameter.setCustomParam("customPKFilter", (Object)customPKFilterMap);
 212           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_entitytype");
 213 >         QFilter idFilter = new QFilter("id", "=", (Object)Long.valueOf(reateEntityTypeId));
 214           QFilter[] idFilterArray = new QFilter[]{idFilter};
 215           DynamicObject entityTypeDynamicObj = serviceHelper.queryOne("id,name,relatepageinfo", idFilterArray);
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRBaseUtils L151
```java
 149           }
 150           catch (Exception e) {
 151 >             throw new KDBizException(e.getMessage());
 152           }
 153       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

## ISV 扩展指引（基于 AdminOrgDetailEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `beforeDoOperation`, `beforeClosed`, `beforeBindData`, `afterBindData`, `afterLoadData`, `registerListener`, `beforeF7Select`, `beforeItemClick`, `propertyChanged`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · AdminOrgDetailEditPlugin L572
```java
 570               }
 571               catch (ParseException exception) {
 572 >                 throw new KDBizException(new ErrorCode("AdminOrgDetailEditPlugin", exception.getMessage()), new Object[0]);
 573               }
 574           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## ISV 扩展指引（基于 HisModelListCommonPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`, `beforeBindData`, `setFilter`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelListCommonPlugin L174
```java
 172           }
 173           if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
 174 >             QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
 175               event.getQFilters().add(currentDataFilter);
 176           } else {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## ISV 扩展指引（基于 HisModelF7ListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforePackageData`, `propertyChanged`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

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

**THROW_BIZ_EXCEPTION** · TimelineService L101
```java
  99                   IDataEntityProperty property = (IDataEntityProperty)dataEntityType.getAllFields().get("isdeleted");
 100                   if (HRStringUtils.isEmpty((String)property.getAlias())) {
 101 >                     throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u5df2\u5f00\u542f\u65f6\u95f4\u8f74\u903b\u8f91\u5220\u9664\uff0c\u8bf7\u914d\u7f6e\u5b57\u6bb5\u201c\u662f\u5426\u5df2\u5220\u9664\u201d\u7684\u6570\u636e\u5e93\u5b57\u6bb5\u540d\u3002", (String)"TimelineService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityName));
 102                   }
 103               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## ISV 扩展指引（基于 HisModelFilterPanelListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`, `setFilter`

### 可重写方法（target.java self）
- `public public void initialize()`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void filterContainerSearchClick(kd.bos.form.events.FilterContainerSearchClickArgs)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## ISV 扩展指引（基于 HisModelFilterPanelF7ListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `afterBindData`, `beforePackageData`, `setFilter`, `propertyChanged`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

## ISV 扩展指引（基于 OrgDetailList 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.OrgDetailList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.haos.formplugin.web.adminorg.OrgDetailCommonListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**THROW_BIZ_EXCEPTION** · OrgDetailList L168
```java
 166                   catch (ParseException parseException) {
 167                       LOGGER.error((Throwable)parseException);
 168 >                     throw new KDBizException(new ErrorCode("OrgDetailList", parseException.getMessage()), new Object[0]);
 169                   }
 170               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

## ISV 扩展指引（基于 AdminOrgDetailBUListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public java.util.Set<java.lang.String> getCtrlBUFieldSet()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

## ISV 扩展指引（基于 AdminOrgDetailListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void showSortViewPage(kd.bos.form.IFormView, java.util.List<java.lang.Long>)`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · AdminOrgDetailListPlugin L362
```java
 360           }
 361           catch (Exception exception) {
 362 >             throw new KDBizException(new ErrorCode("closedCallBack", exception.getMessage()), new Object[0]);
 363           }
 364       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

## ISV 扩展指引（基于 AdminOrgDisableAndIncludeFilterListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String getListAdminOrgEnableField()`
- `protected protected java.lang.String getListPermProKey()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## ISV 扩展指引（基于 HisModelMobileListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractMobListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelMobileListPlugin L31
```java
  29           super.setFilter(event);
  30           if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
  31 >             QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
  32               event.getQFilters().add(currentDataFilter);
  33           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## ISV 扩展指引（基于 HisModelOPCommonPlugin 真实证）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## ISV 扩展指引（基于 HisUniqueValidateOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

## ISV 扩展指引（基于 AdminOrgFastSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `initBatchOrgOpService`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `protected protected kd.hr.haos.business.domain.org.abs.IBatchOrgOpService initBatchOrgOpService(kd.bos.dataentity.entity.DynamicObject[], kd.bos.dataentity.OperateOption)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

## ISV 扩展指引（基于 AdminOrgFastAuditOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `initBatchOrgOpService`

### 可重写方法（target.java self）
- `protected protected kd.hr.haos.business.domain.org.abs.IBatchOrgOpService initBatchOrgOpService(kd.bos.dataentity.entity.DynamicObject[], kd.bos.dataentity.OperateOption)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

## ISV 扩展指引（基于 AdminOrgFastParentChangeOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.haos.opplugin.web.orgfast.AbsOrgBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `initBatchOrgOpService`

### 可重写方法（target.java self）
- `protected protected kd.hr.haos.business.domain.org.abs.IBatchOrgOpService initBatchOrgOpService(kd.bos.dataentity.entity.DynamicObject[], kd.bos.dataentity.OperateOption)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

## ISV 扩展指引（基于 AdminOrgInitSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

## ISV 扩展指引（基于 AdminOrgFastDisableOrgOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public kd.bos.dataentity.entity.DynamicObject[] buildAdminOrg(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgUnitSyncService L246
```java
 244           }
 245           this.afterCreateOrgParamExtend(paramList, Arrays.asList(adminOrgArrays));
 246 >         Long userId = RequestContext.get().getCurrUserId();
 247           this.recordSyncOrgInfo(paramList, UPDATE, userId);
 248           this.orgUnitUpdate(paramList);
```

**QUERY_BUILDER** · AdminOrgTypeRepository L53
```java
  51   
  52       public DynamicObjectCollection queryColByIds(String selectFields, Set<Long> idSet) {
  53 >         return this.serviceHelper.queryOriginalCollection(selectFields, new QFilter("id", "in", idSet).toArray());
  54       }
  55   
```

**READ_VIA_HELPER** · AdminOrgTypeRepository L72
```java
  70           long orgId = requestContext.getOrgId();
  71           if (orgId == 0L) {
  72 >             orgId = OrgUnitServiceHelper.getRootOrgId();
  73           }
  74           newOrgType.set("createorg", (Object)orgId);
```

**WRITE_VIA_HELPER** · OpExecuteUtils L44
```java
  42           operateOption.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
  43           try {
  44 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)entityNumber, (DynamicObject[])dys, (OperateOption)operateOption);
  45           }
  46           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · OrgStrategyService L60
```java
  58           LOGGER.info(String.format("param: %s", orgList.stream().map(dy -> dy.getLong("id")).collect(Collectors.toList())));
  59           try {
  60 >             HRMServiceResult hrmServiceResult = (HRMServiceResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"addStrategyByNewOrg", (Object[])new Object[]{orgList});
  61               if (hrmServiceResult.isSuccess()) {
  62                   LOGGER.info("success");
```

**THROW_BIZ_EXCEPTION** · AdminOrgTypeRepository L102
```java
 100               }
 101           }
 102 >         throw new KDBizException(errorMessage.toString());
 103       }
 104   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

## ISV 扩展指引（基于 AdminOrgFastEnableOrgOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public kd.bos.dataentity.entity.DynamicObject[] buildAdminOrg(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgUnitSyncService L246
```java
 244           }
 245           this.afterCreateOrgParamExtend(paramList, Arrays.asList(adminOrgArrays));
 246 >         Long userId = RequestContext.get().getCurrUserId();
 247           this.recordSyncOrgInfo(paramList, UPDATE, userId);
 248           this.orgUnitUpdate(paramList);
```

**QUERY_BUILDER** · AdminOrgTypeRepository L53
```java
  51   
  52       public DynamicObjectCollection queryColByIds(String selectFields, Set<Long> idSet) {
  53 >         return this.serviceHelper.queryOriginalCollection(selectFields, new QFilter("id", "in", idSet).toArray());
  54       }
  55   
```

**READ_VIA_HELPER** · AdminOrgTypeRepository L72
```java
  70           long orgId = requestContext.getOrgId();
  71           if (orgId == 0L) {
  72 >             orgId = OrgUnitServiceHelper.getRootOrgId();
  73           }
  74           newOrgType.set("createorg", (Object)orgId);
```

**WRITE_VIA_HELPER** · OpExecuteUtils L44
```java
  42           operateOption.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
  43           try {
  44 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)entityNumber, (DynamicObject[])dys, (OperateOption)operateOption);
  45           }
  46           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · OrgStrategyService L60
```java
  58           LOGGER.info(String.format("param: %s", orgList.stream().map(dy -> dy.getLong("id")).collect(Collectors.toList())));
  59           try {
  60 >             HRMServiceResult hrmServiceResult = (HRMServiceResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"addStrategyByNewOrg", (Object[])new Object[]{orgList});
  61               if (hrmServiceResult.isSuccess()) {
  62                   LOGGER.info("success");
```

**THROW_BIZ_EXCEPTION** · AdminOrgTypeRepository L102
```java
 100               }
 101           }
 102 >         throw new KDBizException(errorMessage.toString());
 103       }
 104   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

## ISV 扩展指引（基于 AdminOrgRootResetOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public kd.bos.dataentity.entity.DynamicObject buildAdminOrg(java.lang.Long, kd.bos.dataentity.entity.DynamicObject)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · AdminOrgRootResetOp L52
```java
  50           if (adminOrgs.length > 1) {
  51               LOGGER.error("AdminOrgRootResetOp error, data length:{}", (Object)adminOrgs.length);
  52 >             throw new KDBizException(this.reset_one_msg);
  53           }
  54           DynamicObject rootOrg = adminOrgs[0];
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## ISV 扩展指引（基于 HisBaseDataF7FastFilter 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.base.AbstractBasedataController`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void buildBaseDataCoreFilter(kd.bos.form.field.events.BaseDataCustomControllerEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelCommonService L196
```java
 194       public QFilter getEffectingVersionQFilter() {
 195           Date now = new Date();
 196 >         return new QFilter("bsed", "<=", (Object)now).and(new QFilter("bsled", ">=", (Object)now)).and(new QFilter("datastatus", "=", (Object)HisModelDataStatusEnum.EFFECTING.getStatus())).and(new QFilter("iscurrentversion", "=", (Object)Boolean.FALSE));
 197       }
 198   
```

**THROW_BIZ_EXCEPTION** · HisModelCommonService L124
```java
 122                   LOGGER.error((Throwable)exception);
 123               }
 124 >             throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u7684\u5386\u53f2\u6a21\u578b\u5b9e\u4f53\u914d\u7f6e\u201c\u6a21\u5f0f\u9009\u62e9\u201d\u672a\u914d\u7f6e\uff0c\u8bf7\u5148\u5b8c\u6210\u914d\u7f6e\u3002", (String)"HisModelCommonService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityNumber));
 125           }
 126           return hisModelEntityConfig;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
