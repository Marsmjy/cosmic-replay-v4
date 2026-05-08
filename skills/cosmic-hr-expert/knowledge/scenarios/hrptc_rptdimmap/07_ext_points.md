# 扩展点全图 · hrptc_rptdimmap

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrptc_rptdimmap` 标品接入 **9** 个插件。

### FormPlugin（5）
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `HRBaseUeEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `RptDimMapEdit` ← `kd.hr.hrptc.formplugin.permission.RptDimMapEdit`

### ListPlugin（2）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `RptDimMapList` ← `kd.hr.hrptc.formplugin.permission.RptDimMapList`

### OpPlugin（2）
- `RptDimMapOp` ← `kd.hr.hrptc.opplugin.web.perm.RptDimMapOp`
- `HRDataBaseOp` ← `kd.hr.hbp.opplugin.web.HRDataBaseOp`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.formplugin.permission.RptDimMapEdit -->

## ISV 扩展指引（基于 RptDimMapEdit 真实证）

> FQN: `kd.hr.hrptc.formplugin.permission.RptDimMapEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `preOpenForm`, `afterLoadData`, `beforeDoOperation`, `propertyChanged`, `closedCallBack`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeDeleteRow(kd.bos.entity.datamodel.events.BeforeDeleteRowEventArgs)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

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

**WRITE_VIA_HELPER** · ReportManageService L1080
```java
1078           HRBaseServiceHelper metaHelper = new HRBaseServiceHelper("haos_structproconfig");
1079           DynamicObject[] dys = metaHelper.load(new QFilter[]{new QFilter("entitytype", "=", (Object)number)});
1080 >         OperationServiceHelper.executeOperate((String)"delete", (String)"haos_structproconfig", (DynamicObject[])dys);
1081       }
1082   }
```

**THROW_BIZ_EXCEPTION** · HRFilterUtil L154
```java
 152               }
 153               catch (ParseException e) {
 154 >                 throw new KDBizException(ResManager.loadKDString((String)"\u65e5\u671f\u7c7b\u578b\u8f6c\u6362\u9519\u8bef\u3002", (String)"HRFilterUtil_0", (String)"hrmp-hbp-business", (Object[])new Object[0]));
 155               }
 156           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.formplugin.permission.RptDimMapEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.formplugin.permission.RptDimMapList -->

## ISV 扩展指引（基于 RptDimMapList 真实证）

> FQN: `kd.hr.hrptc.formplugin.permission.RptDimMapList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · DateTimeFieldTimeZoneUtil L54
```java
  52       public static String getUserTimeZone() {
  53           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
  54 >         DynamicObject dyo = service.getUserTimezone(Long.valueOf(RequestContext.get().getCurrUserId()));
  55           return dyo.getString("number");
  56       }
```

**QUERY_BUILDER** · HRFilterUtil L104
```java
 102           }
 103           if (RuleOperatorEnum.STARTS_WITH.getValue().equals(opt)) {
 104 >             return new QFilter(param, "like", (Object)(value + "%"));
 105           }
 106           if (RuleOperatorEnum.ENDS_WITH.getValue().equals(opt)) {
```

**READ_VIA_HELPER** · RptDimMapList L82
```java
  80               ListSelectedRowCollection rowColls = this.getSelectedRows();
  81               List rptDimMapIds = rowColls.stream().map(row -> (Long)row.getPrimaryKeyValue()).collect(Collectors.toList());
  82 >             List rptManageIds = RptDimMapServiceHelper.getRptIds(rptDimMapIds);
  83               if (ReportPermissionService.checkPermission((List)rptManageIds)) {
  84                   String tips = ResManager.loadKDString((String)"\u5f53\u524d\u9009\u62e9\u7684\u7ef4\u5ea6\u6620\u5c04\u5df2\u88ab\u5f15\u7528\uff0c\u5220\u9664\u540e\u62a5\u8868\u5c06\u4e0d\u518d\u8fdb\u884c\u6570\u636e\u6743\u9650\u63a7\u6743\uff0c\u786e\u5b9a\u5220\u9664\uff1f", (String)"RptDimMapEdit_3", (String)"hrmp-hrptc-formplugin", (Object[])new Object[0]);
```

**WRITE_VIA_HELPER** · ReportManageService L1080
```java
1078           HRBaseServiceHelper metaHelper = new HRBaseServiceHelper("haos_structproconfig");
1079           DynamicObject[] dys = metaHelper.load(new QFilter[]{new QFilter("entitytype", "=", (Object)number)});
1080 >         OperationServiceHelper.executeOperate((String)"delete", (String)"haos_structproconfig", (DynamicObject[])dys);
1081       }
1082   }
```

**THROW_BIZ_EXCEPTION** · HRFilterUtil L154
```java
 152               }
 153               catch (ParseException e) {
 154 >                 throw new KDBizException(ResManager.loadKDString((String)"\u65e5\u671f\u7c7b\u578b\u8f6c\u6362\u9519\u8bef\u3002", (String)"HRFilterUtil_0", (String)"hrmp-hbp-business", (Object[])new Object[0]));
 155               }
 156           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrptc.formplugin.permission.RptDimMapList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.formplugin.permission.RptDimMapList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.opplugin.web.perm.RptDimMapOp -->

## ISV 扩展指引（基于 RptDimMapOp 真实证）

> FQN: `kd.hr.hrptc.opplugin.web.perm.RptDimMapOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrptc.opplugin.web.perm.RptDimMapOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRReportPublishMenuService L357
```java
 355        */
 356       public static boolean saveRptMenuInfo(Map<String, Object> publishMetaResult, long reportManageId) {
 357 >         long currUserId = RequestContext.get().getCurrUserId();
 358           Date date = new Date();
 359           HRBaseServiceHelper menuHelper = new HRBaseServiceHelper("hrptmc_publishmenu");
```

**QUERY_BUILDER** · AnalyseObjectService L168
```java
 166       public Map<String, Long> queryFieldIdByFieldAlias(long reportId, List<String> fieldAliasList) {
 167           Long anObjId = ReportManageService.getAnObjId((Long)reportId);
 168 >         DynamicObjectCollection fieldDys = this.queryFieldHelper.queryOriginalCollection("id, fieldalias", new QFilter[]{new QFilter("fieldalias", "in", fieldAliasList), new QFilter("anobj", "=", (Object)anObjId)});
 169           return fieldDys.stream().collect(Collectors.toMap(field -> field.getString("fieldalias"), field -> field.getLong("id")));
 170       }
```

**READ_VIA_HELPER** · AnalyseObjectService L355
```java
 353           if (virtualEntityAnObj) {
 354               HRBaseServiceHelper hRBaseServiceHelper = new HRBaseServiceHelper("hrptmc_virtualentity");
 355 >             DynamicObject virtualEntityDy = hRBaseServiceHelper.queryOriginalOne("name", new QFilter[]{new QFilter("number", "=", (Object)((JoinEntityBo)joinEntities.get(0)).getEntityNumber())});
 356               String entityName = virtualEntityDy.getString("name");
 357               ((JoinEntityBo)joinEntities.get(0)).setDisplayName(entityName);
```

**WRITE_VIA_HELPER** · GenMetaDataHelper L138
```java
 136               dObject.set("bizunit", (Object)bizUnitId);
 137               dObject.set("form", (Object)formId);
 138 >             SaveServiceHelper.save((DynamicObject[])new DynamicObject[]{dObject});
 139               AppUtils.addLog((String)"bos_formmeta", (String)ResManager.loadKDString((String)"\u4fdd\u5b58", (String)"GenMetaDataHelper_0", (String)"hrmp-hrptmc-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u4fdd\u5b58\u8868\u5355\u548c\u529f\u80fd\u5206\u7ec4\u7684\u5173\u8054\u5173\u7cfb", (String)"GenMetaDataHelper_2", (String)"hrmp-hrptmc-business", (Object[])new Object[0]));
 140               message.put("formid", formId);
```

**THROW_BIZ_EXCEPTION** · AnalyseObjectService L474
```java
 472               DynamicObjectCollection joinConditionCol = entityRelationDy.getDynamicObjectCollection("joinconditions");
 473               if (joinConditionCol.isEmpty()) {
 474 >                 throw new KDBizException("analyse object join conditions is empty.");
 475               }
 476               ArrayList joinConditions = Lists.newArrayListWithCapacity((int)joinConditionCol.size());
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrptc.opplugin.web.perm.RptDimMapOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrptc.opplugin.web.perm.RptDimMapOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrptc.opplugin.web.perm.RptDimMapOp -->

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
