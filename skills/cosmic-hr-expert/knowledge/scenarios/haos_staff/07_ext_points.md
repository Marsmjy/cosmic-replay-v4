# 扩展点全图 · haos_staff

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `haos_staff` 标品接入 **24** 个插件。

### FormPlugin（9）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `HRBaseDataTplEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
- `HRBaseDataImportEdit` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
- `HRHiesButtonSwitchPlugin` ← `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
- `OrgStaffMainEdit` ← `kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit`
- `OrgStaffAddEdit` ← `kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit`
- `OrgStaffChangeEdit` ← `kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit`
- `OrgStaffEntryImportPlugin` ← `kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`

### ListPlugin（5）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `HRBaseDataTplList` ← `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
- `HRBasedataLogList` ← `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
- `OrgStaffListPlugin` ← `kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin`

### OpPlugin（9）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `HRBaseDataStatusOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
- `HRBaseDataLogOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- `HRBaseDataEnableOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
- `OrgStaffSaveOp` ← `kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp`
- `HRBaseOriginalOp` ← `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
- `OrgStaffDisableOp` ← `kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp`
- `OrgStaffEnableOp` ← `kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp`
- `OrgStaffDeleteOp` ← `kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp`

### 其他（1）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit -->

## ISV 扩展指引（基于 OrgStaffMainEdit 真实证）

> FQN: `kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `afterBindData`, `preOpenForm`, `itemClick`, `afterLoadData`, `beforeDoOperation`, `afterDoOperation`, `beforeF7Select`, `propertyChanged`, `afterCreateNewData`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void itemClick(kd.bos.form.control.events.ItemClickEvent)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StaffCommonService L361
```java
 359   
 360       public static AuthorizedOrgResult getOrgAuth() {
 361 >         Long userId = RequestContext.get().getCurrUserId();
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · BaseDataHelper L34
```java
  32   
  33       public static QFilter getBaseDataFilter(String entityName, long orgId) {
  34 >         return BaseDataServiceHelper.getBaseDataFilter((String)entityName, (Long)orgId);
  35       }
  36   
```

**CALL_CROSS_SERVICE** · StaffCommonService L364
```java
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
 364 >         AuthorizedOrgResult permResult = (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgsF7", (Object[])new Object[]{userId, appId, "haos_staff", permItemId, "adminorgboid"});
 365           return permResult;
 366       }
```

**THROW_BIZ_EXCEPTION** · StaffExtEntryHelper L146
```java
 144               return StaffExtEntryHelper.getEntityNameByDimension(mark);
 145           }
 146 >         throw new KDBizException("DynamicDimension match error");
 147       }
 148   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffMainEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit -->

## ISV 扩展指引（基于 OrgStaffAddEdit 真实证）

> FQN: `kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `afterBindData`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgStaffAddEdit L42
```java
  40                   pkIdLong = (Long)pkId;
  41               }
  42 >             QFilter idFilter = new QFilter("id", "=", (Object)pkIdLong);
  43               DynamicObject staff = staffHelper.queryOne("id,name,number", new QFilter[]{idFilter});
  44               String captionName = staff.getString("name");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffAddEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit -->

## ISV 扩展指引（基于 OrgStaffChangeEdit 真实证）

> FQN: `kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `beforeDoOperation`, `afterBindData`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StaffCommonService L361
```java
 359   
 360       public static AuthorizedOrgResult getOrgAuth() {
 361 >         Long userId = RequestContext.get().getCurrUserId();
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · BaseDataHelper L34
```java
  32   
  33       public static QFilter getBaseDataFilter(String entityName, long orgId) {
  34 >         return BaseDataServiceHelper.getBaseDataFilter((String)entityName, (Long)orgId);
  35       }
  36   
```

**CALL_CROSS_SERVICE** · StaffCommonService L364
```java
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
 364 >         AuthorizedOrgResult permResult = (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgsF7", (Object[])new Object[]{userId, appId, "haos_staff", permItemId, "adminorgboid"});
 365           return permResult;
 366       }
```

**THROW_BIZ_EXCEPTION** · StaffExtEntryHelper L146
```java
 144               return StaffExtEntryHelper.getEntityNameByDimension(mark);
 145           }
 146 >         throw new KDBizException("DynamicDimension match error");
 147       }
 148   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffChangeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin -->

## ISV 扩展指引（基于 OrgStaffEntryImportPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void beforeImportEntry(kd.bos.entity.datamodel.events.BeforeImportEntryEventArgs)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · StaffDimensionRepository L26
```java
  24   
  25       public DynamicObject[] loadAllDynamicObjectArray() {
  26 >         QFilter alwaysEqualsFilter = new QFilter("1", "=", (Object)1);
  27           return this.serviceHelper.loadDynamicObjectArray(new QFilter[]{alwaysEqualsFilter});
  28       }
```

**READ_VIA_HELPER** · StaffExtEntryHelper L298
```java
 296           Map<String, DynamicObject> staffdimensionMap = Arrays.stream(dynamicObjects).collect(Collectors.toMap(dyn -> dyn.getString("staffdimension"), dyn -> dyn));
 297           Set staffdimension = Arrays.stream(dynamicObjects).map(temp -> temp.getString("staffdimension")).collect(Collectors.toSet());
 298 >         MainEntityType entityType = MetadataServiceHelper.getDataEntityType((String)"haos_staff");
 299           for (String dimension : staffdimension) {
 300               String prefix = dimensionprefixMap.get(dimension);
```

**THROW_BIZ_EXCEPTION** · StaffExtEntryHelper L146
```java
 144               return StaffExtEntryHelper.getEntityNameByDimension(mark);
 145           }
 146 >         throw new KDBizException("DynamicDimension match error");
 147       }
 148   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.form.OrgStaffEntryImportPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin -->

## ISV 扩展指引（基于 OrgStaffListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StaffCommonService L361
```java
 359   
 360       public static AuthorizedOrgResult getOrgAuth() {
 361 >         Long userId = RequestContext.get().getCurrUserId();
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · BaseDataHelper L34
```java
  32   
  33       public static QFilter getBaseDataFilter(String entityName, long orgId) {
  34 >         return BaseDataServiceHelper.getBaseDataFilter((String)entityName, (Long)orgId);
  35       }
  36   
```

**CALL_CROSS_SERVICE** · StaffCommonService L364
```java
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
 364 >         AuthorizedOrgResult permResult = (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgsF7", (Object[])new Object[]{userId, appId, "haos_staff", permItemId, "adminorgboid"});
 365           return permResult;
 366       }
```

**THROW_BIZ_EXCEPTION** · StaffExtEntryHelper L146
```java
 144               return StaffExtEntryHelper.getEntityNameByDimension(mark);
 145           }
 146 >         throw new KDBizException("DynamicDimension match error");
 147       }
 148   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.staff.list.OrgStaffListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp -->

## ISV 扩展指引（基于 OrgStaffSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp -->

## ISV 扩展指引（基于 OrgStaffDisableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgStaffDisableOp L93
```java
  91       private Map<Long, List<DynamicObject>> getStaffVsDynListMap(List<Long> staffIdList, String entityName) {
  92           HRBaseServiceHelper helper = new HRBaseServiceHelper(entityName);
  93 >         QFilter staffIdFilter = new QFilter("staff", "in", staffIdList);
  94           QFilter dataStatusFilter = new QFilter("datastatus", "=", (Object)"1");
  95           DynamicObject[] dyns = helper.loadDynamicObjectArray(new QFilter[]{staffIdFilter, dataStatusFilter});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp -->

## ISV 扩展指引（基于 OrgStaffEnableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgStaffEnableOp L110
```java
 108       private Map<Long, List<DynamicObject>> getStaffVsDynListMap(List<Long> staffIdList, String entityName) {
 109           HRBaseServiceHelper helper = new HRBaseServiceHelper(entityName);
 110 >         QFilter staffIdFilter = new QFilter("staff", "in", staffIdList);
 111           QFilter dataStatusFilter = new QFilter("datastatus", "=", (Object)"-3");
 112           dataStatusFilter.or(new QFilter("datastatus", "in", (Object)"1"));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp -->

## ISV 扩展指引（基于 OrgStaffDeleteOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp -->
