# 扩展点全图 · core_hr_contractinfo

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrpi_contractinfo` 标品接入 **21** 个插件。

### FormPlugin（6）
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `TimelineTplFormEdit` ← `kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit`
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`

### ListPlugin（3）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `TimelineTplListPlugin` ← `kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
- `TimelineLogList` ← `kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList`

### OpPlugin（10）
- `TimelineTplOp` ← `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp`
- `TimelineLogOp` ← `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp`
- `TimeLineCommonSaveOp` ← `kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp`
- `AssignmentTplCommonSaveOp` ← `kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp`
- `ChgRecordSaveOp` ← `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `EmpStageHandleOpPlugin` ← `kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin`
- `ContractInfoSaveOp` ← `kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp`
- `EmployeeCommonStandardMustInputOp` ← `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp`
- `IgnoreReferenceDeleteOp` ← `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp`

### 其他（2）
- `EmployeeAuditCommonOP` ← `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP`
- `TimelineF7FastFilter` ← `kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit -->

## ISV 扩展指引（基于 TimelineTplFormEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterCreateNewData`, `afterBindData`, `propertyChanged`, `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void getEntityType(kd.bos.entity.datamodel.events.GetEntityTypeEventArgs)`
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineTplFormEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin -->

## ISV 扩展指引（基于 TimelineTplListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · TimelineService L134
```java
 132                   Object object = val = entry.getValue() instanceof DynamicObject ? ((DynamicObject)entry.getValue()).get("id") : entry.getValue();
 133                   if (qFilterTemp == null) {
 134 >                     qFilterTemp = new QFilter(logicKeyField, "=", val);
 135                       continue;
 136                   }
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**WRITE_VIA_HELPER** · TimelineTplListPlugin L162
```java
 160               HRBaseServiceHelper helper = new HRBaseServiceHelper(entityNumber);
 161               DynamicObject[] dataList = helper.loadDynamicObjectArray(new QFilter[]{new QFilter("id", "in", (Object)ids)});
 162 >             OperationResult operationResult = OperationServiceHelper.executeOperate((String)"delete", (String)entityNumber, (DynamicObject[])dataList, (OperateOption)OperateOption.create());
 163               if (operationResult.isSuccess()) {
 164                   this.getView().showSuccessNotification(ResManager.loadKDString((String)"\u5220\u9664\u6210\u529f\u3002", (String)"TimelineTplListPlugin_2", (String)"hrmp-hbp-formplugin", (Object[])new Object[0]));
```

**THROW_BIZ_EXCEPTION** · TimelineService L101
```java
  99                   IDataEntityProperty property = (IDataEntityProperty)dataEntityType.getAllFields().get("isdeleted");
 100                   if (HRStringUtils.isEmpty((String)property.getAlias())) {
 101 >                     throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u5df2\u5f00\u542f\u65f6\u95f4\u8f74\u903b\u8f91\u5220\u9664\uff0c\u8bf7\u914d\u7f6e\u5b57\u6bb5\u201c\u662f\u5426\u5df2\u5220\u9664\u201d\u7684\u6570\u636e\u5e93\u5b57\u6bb5\u540d\u3002", (String)"TimelineService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityName));
 102                   }
 103               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineTplListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList -->

## ISV 扩展指引（基于 TimelineLogList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · TimelineLogList L62
```java
  60           FormShowParameter listShowParameter = this.getView().getFormShowParameter();
  61           ListFilterParameter listFilterParameter = new ListFilterParameter();
  62 >         QFilter qFilter = new QFilter("entitynumber", "=", (Object)billFormId);
  63           if (primaryKeyValue != 0L) {
  64               qFilter.and(new QFilter("opobjid", "=", (Object)primaryKeyValue));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.log.TimelineLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

## ISV 扩展指引（基于 TimelineTplOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.TimelineTplOp/`

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

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HisModelAttachmentService L347
```java
 345           InputStream inputStream = fileService.getInputStream(path);
 346           String tempUrl = tempFileCache.saveAsUrl(name, inputStream, 7200);
 347 >         String address = RequestContext.get().getClientFullContextPath();
 348           if (HRStringUtils.isEmpty((String)address)) {
 349               address = UrlService.getDomainContextUrl();
```

**QUERY_BUILDER** · HisModelAttachmentService L162
```java
 160               attachKeys = Sets.newHashSetWithExpectedSize((int)16);
 161           }
 162 >         DynamicObject[] attachmentKeyDys = attachKeys.isEmpty() ? helper.queryOriginalArray("finterid, fattachmentpanel", new QFilter[]{new QFilter("finterid", "in", idStrList), new QFilter("fbilltype", "=", (Object)entityNumber)}) : helper.queryOriginalArray("finterid, fattachmentpanel", new QFilter[]{new QFilter("finterid", "in", idStrList), new QFilter("fattachmentpanel", "in", (Object)attachKeys), new QFilter("fbilltype", "=", (Object)entityNumber)});
 163           Map<String, List<DynamicObject>> attachmentDyMap = Arrays.stream(attachmentKeyDys).collect(Collectors.groupingBy(dy -> dy.getString("fattachmentpanel")));
 164           attachmentDyMap.forEach((key, attachmentDys) -> hasAttachmentsIdMap.put(key, attachmentDys.stream().map(dy -> dy.getLong("finterid")).collect(Collectors.toList())));
```

**READ_VIA_HELPER** · HisModelAttachmentService L172
```java
 170           }
 171           hasAttachmentsIdMap.forEach((attachKey, dataIds) -> {
 172 >             Map attachmentsMap = AttachmentServiceHelper.getAttachments((String)entityNumber, (Object[])dataIds.toArray(new Object[0]), (String)attachKey, (boolean)true);
 173               attachmentsMap.forEach((idStr, attachments) -> {
 174                   HisModelAttachInfo attachInfo = new HisModelAttachInfo();
```

**WRITE_VIA_HELPER** · TimelineTplOp L132
```java
 130                   }
 131                   if (entityConfig.getLogicDelete().booleanValue()) {
 132 >                     SaveServiceHelper.save((DynamicObject[])dataEntities);
 133                       args.setCancelOperation(true);
 134                   }
```

**THROW_BIZ_EXCEPTION** · HisModelAttachmentService L151
```java
 149           ArrayList hisModelAttachInfos = Lists.newArrayListWithCapacity((int)10);
 150           if (HRStringUtils.isEmpty((String)entityNumber)) {
 151 >             throw new KDBizException(ResManager.loadKDString((String)"\u5b9e\u4f53\u7f16\u7801\u4e0d\u80fd\u4e3a\u7a7a\u3002", (String)"HisModelAttachmentService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]));
 152           }
 153           if (ids == null || ids.isEmpty()) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.TimelineTplOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.TimelineTplOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

## ISV 扩展指引（基于 TimelineLogOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp -->

## ISV 扩展指引（基于 TimeLineCommonSaveOp 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp -->

## ISV 扩展指引（基于 AssignmentTplCommonSaveOp 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**CALL_CROSS_SERVICE** · AdminOrgServiceImpl L46
```java
  44           }
  45           try {
  46 >             Map adminOrgHisMap = (Map)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSBatchAdminOrgInfoQueryService", (String)"queryAdminOrgDetailInfoByBoidToMap", (Object[])new Object[]{adminOrgIds, date, "id,boid"});
  47               LOGGER.info("HbpmServiceImpl.getAdminOrgHis======adminOrgIds|{}adminOrgHisMap|{}", adminOrgIds, (Object)adminOrgHisMap);
  48               if (CollectionUtils.isEmpty((Map)adminOrgHisMap)) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

## ISV 扩展指引（基于 ChgRecordSaveOp 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin -->

## ISV 扩展指引（基于 EmpStageHandleOpPlugin 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · EmpStageApplicationServiceImpl L77
```java
  75   
  76       public DynamicObject getEmpStageByDate(Date date, Long employeeId) {
  77 >         Object[] empStages = IEmpStageDomainService.getInstance().query(empStageSql, new QFilter[]{new QFilter("employee.id", "=", (Object)employeeId).and(new QFilter("entrydate", "<=", (Object)date).and(new QFilter("enddate", ">=", (Object)date)))});
  78           if (ObjectUtils.isEmpty((Object[])empStages)) {
  79               LOGGER.info("EmpStageApplicationServiceImpl.getEmpStageByDate,EmpStageByDate is null,need find closest data...");
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp -->

## ISV 扩展指引（基于 ContractInfoSaveOp 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.assignment.attach.ContractInfoSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->

## ISV 扩展指引（基于 IgnoreReferenceDeleteOp 真实证）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter -->

## ISV 扩展指引（基于 TimelineF7FastFilter 真实证）

> FQN: `kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.base.AbstractBasedataController`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void buildBaseDataCoreFilter(kd.bos.form.field.events.BaseDataCustomControllerEvent)`
- `public public void addLogicDeleteDataFilter(java.lang.String, java.util.List<kd.bos.orm.query.QFilter>, java.util.Map<java.lang.String, kd.hr.hbp.business.service.timeline.dao.TimelineEntityConf>)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · TimelineService L134
```java
 132                   Object object = val = entry.getValue() instanceof DynamicObject ? ((DynamicObject)entry.getValue()).get("id") : entry.getValue();
 133                   if (qFilterTemp == null) {
 134 >                     qFilterTemp = new QFilter(logicKeyField, "=", val);
 135                       continue;
 136                   }
```

**THROW_BIZ_EXCEPTION** · TimelineService L101
```java
  99                   IDataEntityProperty property = (IDataEntityProperty)dataEntityType.getAllFields().get("isdeleted");
 100                   if (HRStringUtils.isEmpty((String)property.getAlias())) {
 101 >                     throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u5df2\u5f00\u542f\u65f6\u95f4\u8f74\u903b\u8f91\u5220\u9664\uff0c\u8bf7\u914d\u7f6e\u5b57\u6bb5\u201c\u662f\u5426\u5df2\u5220\u9664\u201d\u7684\u6570\u636e\u5e93\u5b57\u6bb5\u540d\u3002", (String)"TimelineService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityName));
 102                   }
 103               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.timeline.TimelineF7FastFilter -->
