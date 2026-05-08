# core_hr_econtract · 扩展点

> **聚合场景**：core_hr_econtract · 包含 1 个 hbss 字典实体（电子签集成实战 · 详见 PPT05_DEEP_TRACE.md 第 12 节。支持法大大公有云 + 法大大私有云 + 易企签公有云 3 厂商。签署状态：beg...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

电子签集成实战 · 详见 PPT05_DEEP_TRACE.md 第 12 节。支持法大大公有云 + 法大大私有云 + 易企签公有云 3 厂商。签署状态：begin / check / csign / esign / confirm / all。标品定时任务：hlcm_repair_esign_task (失败手动补偿)。

## 涉及实体（1 个）

- `core_hr_econtract`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin -->

## ISV 扩展指引（基于 ElectricSignDynamicPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `afterBindData`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void tabSelected(kd.bos.form.control.events.TabSelectEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PermissionService L34
```java
  32   public class PermissionService {
  33       public static boolean isSuperUser() {
  34 >         long currUserId = RequestContext.get().getCurrUserId();
  35           return SuperUserCache.isSuperUser((long)currUserId);
  36       }
```

**QUERY_BUILDER** · PermissionService L83
```java
  81           HRBaseServiceHelper helper = new HRBaseServiceHelper(entityName);
  82           QFilter baseDataFilterByOrg = PermissionService.getBaseDataFilter(entityType, userHasPermOrgs, isRemoveDup);
  83 >         QFilter enableFilter = new QFilter("status", "=", (Object)"C");
  84           if (baseDataFilterByOrg != null) {
  85               return helper.query(new QFilter[]{baseDataFilterByOrg, enableFilter});
```

**READ_VIA_HELPER** · PermissionService L39
```java
  37   
  38       public static List<Long> getUserHasPermOrgs(Long userId, boolean mustQueryAll) {
  39 >         HasPermOrgResult result = PermissionServiceHelper.getUserHasPermOrgs((long)userId, (boolean)mustQueryAll);
  40           if (result.hasAllOrgPerm()) {
  41               return null;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin -->

## ISV 扩展指引（基于 SignInitializationPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractMobFormPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRWordUtils L307
```java
 305               }
 306           } else {
 307 >             Map map = PreviewServiceFactory.getPreviewService((FileService)fileService).preview(fileName, url, RequestContext.get().getUserAgent());
 308               String status = (String)map.get("status");
 309               if (PreviewParams.PDF_SUCCESS.getEnumName().equals(status) || PreviewParams.NOT_NEED_CHANGE.getEnumName().equals(status)) {
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
```

**READ_VIA_HELPER** · HRWordUtils L288
```java
 286           String pathUrl = "";
 287           AppCustomParam appCustomParam = new AppCustomParam("15NPDX/GJFOO");
 288 >         Map appCustomParamMap = SystemParamServiceHelper.loadAppCustomParameterFromCache((AppCustomParam)appCustomParam);
 289           String word2PdfServiceIsv = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_ISV");
 290           String word2PdfServiceCloudid = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_CLOUDID");
```

**WRITE_VIA_HELPER** · SignManageServiceImpl L1434
```java
1432           operateOption.setVariableValue("terminateReason", String.valueOf(terminateCauseId));
1433           Object[] signBillIds = (Long[])Stream.of(signBills).map(signBill -> signBill.getLong("id")).toArray(Long[]::new);
1434 >         OperationResult terminateResult = OperationServiceHelper.executeOperate((String)"terminate", (String)"hlcm_contractapplyhandle", (Object[])signBillIds, (OperateOption)operateOption);
1435           if (terminateResult.isSuccess()) {
1436               SyncStartStatusService.getInstance().syncHirePersonStartStatus(signBills);
```

**CALL_CROSS_SERVICE** · HRWordUtils L296
```java
 294               LOGGER.info("switchWord2Pdf Isv={},cloudid={},appid={},servicename={}", new Object[]{word2PdfServiceIsv, word2PdfServiceCloudid, word2PdfServiceAppid, word2PdfServiceName});
 295               if ("kingdee".equals(word2PdfServiceIsv)) {
 296 >                 pathUrl = (String)DispatchServiceHelper.invokeBizService((String)word2PdfServiceCloudid, (String)word2PdfServiceAppid, (String)word2PdfServiceName, (String)"switchWord2Pdf", (Object[])new Object[]{url, fileName, formId, appId, pkId});
 297               } else {
 298                   ISVInfo isvInfo = ISVServiceHelper.getISVInfo();
```

**THROW_BIZ_EXCEPTION** · HRWordUtils L301
```java
 299                   if (isvInfo == null) {
 300                       LOGGER.error("isvInfo is null");
 301 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s. %2$s\u83b7\u53d6ISV\u4f9b\u5e94\u5546\u6807\u8bc6\u4e3a\u7a7a\u3002", (String)"HRMServiceHelper_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), word2PdfServiceName, "switchWord2Pdf"));
 302                   }
 303                   String factoryQualifiedPrefix = String.join((CharSequence)".", isvInfo.getId(), word2PdfServiceCloudid, word2PdfServiceAppid);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

## ISV 扩展指引（基于 HRTemplateBillEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `protected protected void setButtonStatus()`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `protected protected void setPageStatus(kd.bos.form.events.AfterDoOperationEventArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## ISV 扩展指引（基于 HRPermCommonEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void beforeCheckDataPermission(kd.bos.form.events.BeforeDoCheckDataPermissionArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin -->

## ISV 扩展指引（基于 HRExtToolPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin -->

## ISV 扩展指引（基于 ISDeletePersonFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ISDeletePersonFormPlugin L52
```java
  50                   return;
  51               }
  52 >             DynamicObject[] dys = BusinessDataServiceHelper.load((String)entity, (String)"id,assignment", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)pkId)});
  53               if (dys.length < 1 || dys[0].getBoolean("assignment.isdeleted")) {
  54                   preOpenFormEventArgs.setCancel(true);
```

**READ_VIA_HELPER** · ISDeletePersonFormPlugin L52
```java
  50                   return;
  51               }
  52 >             DynamicObject[] dys = BusinessDataServiceHelper.load((String)entity, (String)"id,assignment", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)pkId)});
  53               if (dys.length < 1 || dys[0].getBoolean("assignment.isdeleted")) {
  54                   preOpenFormEventArgs.setCancel(true);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin -->

## ISV 扩展指引（基于 BaseContractBillPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`

### 可重写方法（target.java self）
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

**CALL_CROSS_SERVICE** · ContractSignUtils L264
```java
 262               requestMap.put("inputParams", inputParams);
 263           }
 264 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMRuleService", (String)"callRuleEngine", (Object[])new Object[]{requestMap});
 265       }
 266   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin -->

## ISV 扩展指引（基于 SignManageBasePlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `afterBindData`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · ContractSignUtils L264
```java
 262               requestMap.put("inputParams", inputParams);
 263           }
 264 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMRuleService", (String)"callRuleEngine", (Object[])new Object[]{requestMap});
 265       }
 266   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin -->

## ISV 扩展指引（基于 ConfirmCommonFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRWordUtils L307
```java
 305               }
 306           } else {
 307 >             Map map = PreviewServiceFactory.getPreviewService((FileService)fileService).preview(fileName, url, RequestContext.get().getUserAgent());
 308               String status = (String)map.get("status");
 309               if (PreviewParams.PDF_SUCCESS.getEnumName().equals(status) || PreviewParams.NOT_NEED_CHANGE.getEnumName().equals(status)) {
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
```

**READ_VIA_HELPER** · HRWordUtils L288
```java
 286           String pathUrl = "";
 287           AppCustomParam appCustomParam = new AppCustomParam("15NPDX/GJFOO");
 288 >         Map appCustomParamMap = SystemParamServiceHelper.loadAppCustomParameterFromCache((AppCustomParam)appCustomParam);
 289           String word2PdfServiceIsv = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_ISV");
 290           String word2PdfServiceCloudid = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_CLOUDID");
```

**WRITE_VIA_HELPER** · SignManageServiceImpl L1434
```java
1432           operateOption.setVariableValue("terminateReason", String.valueOf(terminateCauseId));
1433           Object[] signBillIds = (Long[])Stream.of(signBills).map(signBill -> signBill.getLong("id")).toArray(Long[]::new);
1434 >         OperationResult terminateResult = OperationServiceHelper.executeOperate((String)"terminate", (String)"hlcm_contractapplyhandle", (Object[])signBillIds, (OperateOption)operateOption);
1435           if (terminateResult.isSuccess()) {
1436               SyncStartStatusService.getInstance().syncHirePersonStartStatus(signBills);
```

**CALL_CROSS_SERVICE** · HRWordUtils L296
```java
 294               LOGGER.info("switchWord2Pdf Isv={},cloudid={},appid={},servicename={}", new Object[]{word2PdfServiceIsv, word2PdfServiceCloudid, word2PdfServiceAppid, word2PdfServiceName});
 295               if ("kingdee".equals(word2PdfServiceIsv)) {
 296 >                 pathUrl = (String)DispatchServiceHelper.invokeBizService((String)word2PdfServiceCloudid, (String)word2PdfServiceAppid, (String)word2PdfServiceName, (String)"switchWord2Pdf", (Object[])new Object[]{url, fileName, formId, appId, pkId});
 297               } else {
 298                   ISVInfo isvInfo = ISVServiceHelper.getISVInfo();
```

**THROW_BIZ_EXCEPTION** · HRWordUtils L301
```java
 299                   if (isvInfo == null) {
 300                       LOGGER.error("isvInfo is null");
 301 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s. %2$s\u83b7\u53d6ISV\u4f9b\u5e94\u5546\u6807\u8bc6\u4e3a\u7a7a\u3002", (String)"HRMServiceHelper_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), word2PdfServiceName, "switchWord2Pdf"));
 302                   }
 303                   String factoryQualifiedPrefix = String.join((CharSequence)".", isvInfo.getId(), word2PdfServiceCloudid, word2PdfServiceAppid);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin -->

## ISV 扩展指引（基于 ContractEntryEntityFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `propertyChanged`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void initialize()`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void afterAddRow(kd.bos.entity.datamodel.events.AfterAddRowEventArgs)`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

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

**CALL_CROSS_SERVICE** · ContractSignUtils L264
```java
 262               requestMap.put("inputParams", inputParams);
 263           }
 264 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMRuleService", (String)"callRuleEngine", (Object[])new Object[]{requestMap});
 265       }
 266   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

## ISV 扩展指引（基于 HRTemplateBillList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonList -->

## ISV 扩展指引（基于 HRPermCommonList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeF7Select`

### 可重写方法（target.java self）
- `public public void beforeCheckDataPermission(kd.bos.form.events.BeforeDoCheckDataPermissionArgs)`
- `public public void initialize()`
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)` ⭐ lifecycle
- `protected protected java.lang.String getAdminOrgFilterField()`
- `protected protected boolean isAdminOrgFilterEnable()`
- `protected protected java.lang.String getBUFilterEntityName()`
- `protected protected java.lang.String getBUFilterField()`
- `protected protected java.lang.String getEmpgrpFilterField()`
- `protected protected java.lang.String getBUFilterAppId()`
- `protected protected kd.bos.orm.query.QFilter getCustomFilter()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin -->

## ISV 扩展指引（基于 FixedCommonListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin -->

## ISV 扩展指引（基于 HisFieldF7FilterListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin -->

## ISV 扩展指引（基于 SignCommonListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin -->

## ISV 扩展指引（基于 SignOutListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `itemClick`

### 可重写方法（target.java self）
- `public public void itemClick(kd.bos.form.control.events.ItemClickEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp -->

## ISV 扩展指引（基于 ContractBaseUnSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

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

**CALL_CROSS_SERVICE** · HRCSServiceImpl L85
```java
  83       public String getContent(Long id) {
  84           LOG.info("invoke IHRCSService#getPrompts id:{}", (Object)id);
  85 >         String result = (String)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"getContent", (Object[])new Object[]{id});
  86           LOG.info("invoke IHRCSService#getContent result:{}", (Object)result);
  87           return result;
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## ISV 扩展指引（基于 HRCodeRuleOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`
- `protected protected void recycleNumber(kd.bos.dataentity.entity.DynamicObject[])`

### SDK 范式（ISV 抄作业）

**READ_VIA_HELPER** · HRCodeRuleOp L81
```java
  79           String orgId;
  80           String entityId = obj.getDataEntityType().getName();
  81 >         CodeRuleInfo codeRuleInfo = CodeRuleServiceHelper.getCodeRule((String)entityId, (DynamicObject)obj, (String)(orgId = this.getMainOrgId(obj)));
  82           if (codeRuleInfo == null) {
  83               return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp -->

## ISV 扩展指引（基于 ContractBaseSubmitEffectOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · ContractSignUtils L264
```java
 262               requestMap.put("inputParams", inputParams);
 263           }
 264 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"brm", (String)"IBRMRuleService", (String)"callRuleEngine", (Object[])new Object[]{requestMap});
 265       }
 266   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp -->

## ISV 扩展指引（基于 ContractBaseTerminateOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp -->

## ISV 扩展指引（基于 PreSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp -->

## ISV 扩展指引（基于 PreSubmitEffectOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp -->

## ISV 扩展指引（基于 PreUnSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp -->

## ISV 扩展指引（基于 ContractInitWorkFlowOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**READ_VIA_HELPER** · ContractInitWorkFlowOp L45
```java
  43           DynamicObject[] dataEntities;
  44           for (DynamicObject dataEntity : dataEntities = e.getDataEntities()) {
  45 >             List processElements = WorkflowServiceHelper.getProcessElements((DynamicObject)dataEntity, null);
  46               List activityIds = processElements.stream().filter(element -> HRStringUtils.equals((String)element.getType(), (String)TYPE_HR_ACTIVITY)).filter(element -> element instanceof WFAuditTask).map(element -> ((WFAuditTask)element).getCustomParams().stream().findFirst().orElse(null)).filter(Objects::nonNull).sorted(Comparator.comparing(WFBaseElement::getNumber)).map(WFBaseElement::getId).filter(Objects::nonNull).map(Long::parseLong).collect(Collectors.toList());
  47               ISignActivity.getInstance().initAllActivity(Long.valueOf(dataEntity.getLong("id")), activityIds);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.BeginSignOp -->

## ISV 扩展指引（基于 BeginSignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.BeginSignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.BeginSignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.BeginSignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.BeginSignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.BeginSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp -->

## ISV 扩展指引（基于 CompleteSignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp -->

## ISV 扩展指引（基于 ConfirmArchiveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRWordUtils L307
```java
 305               }
 306           } else {
 307 >             Map map = PreviewServiceFactory.getPreviewService((FileService)fileService).preview(fileName, url, RequestContext.get().getUserAgent());
 308               String status = (String)map.get("status");
 309               if (PreviewParams.PDF_SUCCESS.getEnumName().equals(status) || PreviewParams.NOT_NEED_CHANGE.getEnumName().equals(status)) {
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
```

**READ_VIA_HELPER** · HRWordUtils L288
```java
 286           String pathUrl = "";
 287           AppCustomParam appCustomParam = new AppCustomParam("15NPDX/GJFOO");
 288 >         Map appCustomParamMap = SystemParamServiceHelper.loadAppCustomParameterFromCache((AppCustomParam)appCustomParam);
 289           String word2PdfServiceIsv = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_ISV");
 290           String word2PdfServiceCloudid = (String)appCustomParamMap.get("HR_CONTRACT_WORD2PDF_SERVICE_CLOUDID");
```

**WRITE_VIA_HELPER** · SignManageServiceImpl L1434
```java
1432           operateOption.setVariableValue("terminateReason", String.valueOf(terminateCauseId));
1433           Object[] signBillIds = (Long[])Stream.of(signBills).map(signBill -> signBill.getLong("id")).toArray(Long[]::new);
1434 >         OperationResult terminateResult = OperationServiceHelper.executeOperate((String)"terminate", (String)"hlcm_contractapplyhandle", (Object[])signBillIds, (OperateOption)operateOption);
1435           if (terminateResult.isSuccess()) {
1436               SyncStartStatusService.getInstance().syncHirePersonStartStatus(signBills);
```

**CALL_CROSS_SERVICE** · HRWordUtils L296
```java
 294               LOGGER.info("switchWord2Pdf Isv={},cloudid={},appid={},servicename={}", new Object[]{word2PdfServiceIsv, word2PdfServiceCloudid, word2PdfServiceAppid, word2PdfServiceName});
 295               if ("kingdee".equals(word2PdfServiceIsv)) {
 296 >                 pathUrl = (String)DispatchServiceHelper.invokeBizService((String)word2PdfServiceCloudid, (String)word2PdfServiceAppid, (String)word2PdfServiceName, (String)"switchWord2Pdf", (Object[])new Object[]{url, fileName, formId, appId, pkId});
 297               } else {
 298                   ISVInfo isvInfo = ISVServiceHelper.getISVInfo();
```

**THROW_BIZ_EXCEPTION** · HRWordUtils L301
```java
 299                   if (isvInfo == null) {
 300                       LOGGER.error("isvInfo is null");
 301 >                     throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"%1$s. %2$s\u83b7\u53d6ISV\u4f9b\u5e94\u5546\u6807\u8bc6\u4e3a\u7a7a\u3002", (String)"HRMServiceHelper_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), word2PdfServiceName, "switchWord2Pdf"));
 302                   }
 303                   String factoryQualifiedPrefix = String.join((CharSequence)".", isvInfo.getId(), word2PdfServiceCloudid, word2PdfServiceAppid);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.CompanySignOp -->

## ISV 扩展指引（基于 CompanySignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.CompanySignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompanySignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompanySignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompanySignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.CompanySignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp -->

## ISV 扩展指引（基于 PreBeginSignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractPreValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp -->

## ISV 扩展指引（基于 PreCompanySignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractPreValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp -->

## ISV 扩展指引（基于 PreCompleteSignOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractPreValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp -->

## ISV 扩展指引（基于 PreConfirmArchiveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hlcm.opplugin.prevalidate.AbstractPreValidateOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin -->

## ISV 扩展指引（基于 SignAllFilterListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · SignAllFilterListPlugin L31
```java
  29           if (HRStringUtils.equals((String)fieldName, (String)"activityins.activity.name")) {
  30               List qfilters = args.getQfilters();
  31 >             qfilters.add(new QFilter("app.number", "=", (Object)"hlcm"));
  32           }
  33       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin -->
