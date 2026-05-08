# core_hr_contract_termination · 扩展点

> **聚合场景**：core_hr_contract_termination · 包含 1 个 hbss 字典实体（合同终止 / 解除 / 改签 · chgaction 大类 = 用工终止类 (复用 hpfs_hrhtmbillorgtplext) 或专属终止逻辑。影响 hr...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

合同终止 / 解除 / 改签 · chgaction 大类 = 用工终止类 (复用 hpfs_hrhtmbillorgtplext) 或专属终止逻辑。影响 hrpi_empentrel 状态 (laborrelstatus 转 'terminated' 或 'changed')。ISV 扩展：基础资料配置自定义终止/解除原因 + 业务规则关联校验。

## 涉及实体（1 个）

- `core_hr_contract_termination`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin -->

## ISV 扩展指引（基于 ContractSignFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterCreateNewData`, `preOpenForm`, `beforeBindData`, `afterBindData`, `beforeF7Select`, `beforeDoOperation`, `afterDoOperation`, `propertyChanged`, `beforeClosed`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin -->

## ISV 扩展指引（基于 ContractImportPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.importplugin.SignImportBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String doCheckErManFile(java.lang.Long, java.util.List<java.lang.Long>, boolean)`
- `protected protected void fillVirtualFieldSignBillInfo(kd.bos.entity.datamodel.events.ImportDataEventArgs, java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.String, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>>)`
- `protected protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>> getBaseDataMaps(kd.bos.entity.datamodel.events.InitImportDataEventArgs, java.lang.String)`
- `protected protected java.lang.String checkContractEntry(java.util.Map<java.lang.String, java.lang.Object>, java.util.Set<java.lang.String>, boolean, java.lang.String)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · DevParamConfigRepository L27
```java
  25   
  26       public static DynamicObject queryConfigByBusinessKey(String businessKey) {
  27 >         return DEV_PARAM_CONFIG.queryOriginalOne("businessvalue,largebusinessvalue_tag", new QFilter[]{new QFilter("businesskey", "=", (Object)businessKey)});
  28       }
  29   
```

**READ_VIA_HELPER** · DevParamConfigServiceImpl L63
```java
  61           apm.setAppId(appId);
  62           apm.setOrgId(orgId);
  63 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)apm, (String)paramKey);
  64       }
  65   }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.importplugin.ContractImportPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.ErManFilePlugin -->

## ISV 扩展指引（基于 ErManFilePlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.ErManFilePlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ErManFilePlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ErManFilePlugin L90
```java
  88                   this.erManFileQFilterInit(listFilterParameter, entityId);
  89               }
  90 >             Optional.ofNullable(this.getModel().getDataEntity().getDynamicObject("org")).ifPresent(org -> listFilterParameter.getQFilters().add(new QFilter("org", "=", org.getPkValue())));
  91               String caption = ResManager.loadKDString((String)"\u5458\u5de5", (String)"ErManFilePlugin_1", (String)"hr-hlcm-formplugin", (Object[])new Object[0]);
  92               formShowParameter.setCaption(caption);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ErManFilePlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ErManFilePlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.ErManFilePlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin -->

## ISV 扩展指引（基于 ContractSignListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `closedCallBack`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractapply.ContractSignListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin -->

## ISV 扩展指引（基于 MulSelectConfirmCommonListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public java.util.Map<java.lang.Object, java.lang.String> batchInvokeOperation(kd.bos.form.IFormView, java.lang.String, java.lang.Object[])`
- `public public java.lang.String getSuccessOperation()`
- `public public java.lang.String getFailOperation()`

### SDK 范式（ISV 抄作业）

**WRITE_VIA_HELPER** · MulSelectConfirmCommonListPlugin L115
```java
 113               OperateOption operateOption = OperateOption.create();
 114               operateOption.setVariableValue("currbizappid", appId);
 115 >             OperationResult preOpResult = OperationServiceHelper.executeOperate((String)("pre" + operateKey), (String)listView.getBillFormId(), (Object[])pkValues, (OperateOption)operateOption);
 116               ArrayList<Object> preValidateSuccessPkValues = new ArrayList<Object>(pkValues.length);
 117               if (preOpResult.isSuccess()) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.common.MulSelectConfirmCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin -->

## ISV 扩展指引（基于 PeriodUnitSetNullCommonListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.PeriodUnitSetNullCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp -->

## ISV 扩展指引（基于 ContractBaseSaveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp -->

## ISV 扩展指引（基于 ContractBaseSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp -->

## ISV 扩展指引（基于 ContractBaseWfUnSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractBaseWfUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.database.TerminateWorkflowOp -->

## ISV 扩展指引（基于 TerminateWorkflowOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.database.TerminateWorkflowOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.database.TerminateWorkflowOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.database.TerminateWorkflowOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.database.TerminateWorkflowOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.database.TerminateWorkflowOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin -->

## ISV 扩展指引（基于 ContractApplyStopMobPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.mobile.contract.ContractApplyCommonMobPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `preOpenForm`, `afterBindData`, `click`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.mobile.contract.ContractApplyStopMobPlugin -->
