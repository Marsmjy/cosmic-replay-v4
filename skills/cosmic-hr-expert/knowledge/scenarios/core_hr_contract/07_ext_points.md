# core_hr_contract · 扩展点

> **聚合场景**：core_hr_contract · 包含 5 个 hbss 字典实体（劳动合同核心 4 单据 + 1 档案聚合。**特点**：标品采用配置化映射（'关联合同协议申请单配置'）实现单据 ↔ 档案双向字段映射，ISV 加扩展字段后无需...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

劳动合同核心 4 单据 + 1 档案聚合。**特点**：标品采用配置化映射（'关联合同协议申请单配置'）实现单据 ↔ 档案双向字段映射，ISV 加扩展字段后无需写代码即可自动落库。关键 OP：ContractBaseSaveOp / ContractBaseSubmitEffectOp / PreSubmitEffectOp / ContractBaseSubmitOp / ContractBaseUnSubmitOp / ContractBaseWfUnSubmitOp。标品定时任务：hlcm_contacttohrpiinfo_task_SKDJ_S 同步合同档案 → hrpi_contractinfo。ISV 扩展点：IHLCMTemplateExtPlugin (合同模板变量替换)、IHLCMAttachmentExtPlugin (附件)、ISignActivityExtPlugin (协作活动)。

## 涉及实体（5 个）

- `hlcm_contract`
- `hlcm_contractapply`
- `hlcm_empprotocol`
- `hlcm_otheragreements`
- `hlcm_renewinquery`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin -->

## ISV 扩展指引（基于 ContractFileFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileBaseFormPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFormPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins -->

## ISV 扩展指引（基于 ContractFileProtocolListPlugins 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `filterContainerInit`, `setFilter`

### 可重写方法（target.java self）
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileProtocolListPlugins -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin -->

## ISV 扩展指引（基于 ContractFileFixedCommonListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.billapply.contractfile.FixedFileListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterDoOperation`

### 可重写方法（target.java self）
- `public public java.util.List<java.lang.String> getOpKeyList()`
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.contractfile.ContractFileFixedCommonListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.ContractListPlugin -->

## ISV 扩展指引（基于 ContractListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.ContractListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ContractListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · ContractPermissionUtils L59
```java
  57   
  58       public static boolean checkPermission(String appId, String entityNum, String permItemId) {
  59 >         return PermissionServiceHelper.checkPermission((Long)RequestContext.get().getCurrUserId(), (String)appId, (String)entityNum, (String)permItemId);
  60       }
  61   }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ContractListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.ContractListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.ContractListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp -->

## ISV 扩展指引（基于 ContractFileBaseSaveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `endOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void employeeInfoFieldSet(kd.bos.dataentity.entity.DynamicObject, java.util.Map<java.lang.String, java.lang.Object>)`
- `public public void empOrgRelFieldSet(kd.bos.dataentity.entity.DynamicObject, kd.hr.hlcm.business.infrastructure.client.hbpm.IHbpmService, java.util.Map<java.lang.String, java.lang.Object>)`
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp -->

## ISV 扩展指引（基于 ContractFileSaveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractListDeleteOp -->

## ISV 扩展指引（基于 ContractListDeleteOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractListDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractListDeleteOp/`

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

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractListDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractListDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.ContractListDeleteOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp -->

## ISV 扩展指引（基于 ContractFileBaseReviseOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

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

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.contractfile.ContractFileBaseReviseOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin -->

## ISV 扩展指引（基于 ContractEmpProtocolListPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractBaseListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ContractEmpProtocolListPlugin L28
```java
  26           String billFormId = view.getBillFormId();
  27           List qFilters = e.getQFilters();
  28 >         qFilters.add(new QFilter("protocoltype", "=", (Object)ProtocolTypeEnum.YG.getCombKey()));
  29           switch (billFormId) {
  30               case "hlcm_empprotocolnew": {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractEmpProtocolListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp -->

## ISV 扩展指引（基于 ProtocolBasePerTerminateOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.protocol.ProtocolBasePerTerminateOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins -->

## ISV 扩展指引（基于 OtherAncillaryAgreementApplyPlugins 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractProtocolBaseFormPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeF7Select`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `protected protected void clearBillInfoWhenSwitchFileID()`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementApplyPlugins -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit -->

## ISV 扩展指引（基于 EmpProtocolTemplateBillEdit 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `protected protected void setButtonStatus()`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.empprotocolapply.EmpProtocolTemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin -->

## ISV 扩展指引（基于 OtherProtocolImportPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.importplugin.SignImportBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected java.lang.String doCheckErManFile(java.lang.Long, java.util.List<java.lang.Long>, boolean)`
- `protected protected void fillVirtualFieldSignBillInfo(kd.bos.entity.datamodel.events.ImportDataEventArgs, java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.String, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>>)`
- `public public java.util.Map<java.lang.String, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>> getBaseDataMaps(kd.bos.entity.datamodel.events.InitImportDataEventArgs, java.lang.String)`
- `public static public static void checkValVirtualFieldMainContract(kd.bos.entity.datamodel.events.ImportDataEventArgs, java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>)`
- `public static public static void setValVirtualFieldMainContract(kd.bos.entity.datamodel.IDataModel, java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.String, kd.bos.dataentity.entity.DynamicObject>)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.importplugin.OtherProtocolImportPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins -->

## ISV 扩展指引（基于 OtherAncillaryAgreementListPlugins 真实证）

> FQN: `kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.hlcm.formplugin.billapply.empprotocolapply.ContractBaseListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OtherAncillaryAgreementListPlugins L24
```java
  22       public void setFilter(SetFilterEvent e) {
  23           List qFilters = e.getQFilters();
  24 >         qFilters.add(new QFilter("protocoltype", "=", (Object)ProtocolTypeEnum.FS.getCombKey()));
  25           qFilters.add(new QFilter("businesstype", "=", (Object)BusinessTypeEnum.NEW.getCombKey()));
  26       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.billapply.otherprotocolapply.OtherAncillaryAgreementListPlugins -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp -->

## ISV 扩展指引（基于 ContractProtocolBaseSaveOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.protocol.ContractProtocolBaseSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin -->

## ISV 扩展指引（基于 RenewInqueryFormPlugin 真实证）

> FQN: `kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterLoadData`, `beforeClosed`

### 可重写方法（target.java self）
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void setPersonPanelInfo()`
- `public public void showNewParameter(kd.bos.form.IFormView, kd.hr.hlcm.common.entity.SignHeadInfoEntity)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRInteDateTimeUtil L43
```java
  41           }
  42           IFormat format = prop.getRegionType() == FormatTypes.Time.getValue() ? FormatFactory.get((FormatTypes)FormatTypes.Time) : FormatFactory.get((FormatTypes)FormatTypes.Date);
  43 >         FormatObject formatObject = InteServiceHelper.getUserFormat((Long)Long.parseLong(RequestContext.get().getUserId()));
  44           InteTimeZone timeZone = prop.getTimeZone(orgId);
  45           if (formatObject != null && formatObject.isTimeNotEmpty()) {
```

**READ_VIA_HELPER** · HRInteDateTimeUtil L43
```java
  41           }
  42           IFormat format = prop.getRegionType() == FormatTypes.Time.getValue() ? FormatFactory.get((FormatTypes)FormatTypes.Time) : FormatFactory.get((FormatTypes)FormatTypes.Date);
  43 >         FormatObject formatObject = InteServiceHelper.getUserFormat((Long)Long.parseLong(RequestContext.get().getUserId()));
  44           InteTimeZone timeZone = prop.getTimeZone(orgId);
  45           if (formatObject != null && formatObject.isTimeNotEmpty()) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.formplugin.renew.RenewInqueryFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp -->

## ISV 扩展指引（基于 InqueryTaskSubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp -->

## ISV 扩展指引（基于 InqueryTaskUnsubmitOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskUnsubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp -->

## ISV 扩展指引（基于 InqueryTaskdiscardOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskdiscardOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp -->

## ISV 扩展指引（基于 InqueryTaskSubmitEffectOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp -->

## ISV 扩展指引（基于 InqueryTaskAuditOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp -->

## ISV 扩展指引（基于 InqueryTaskAgreeOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · InqueryTaskAgreeOp L42
```java
  40               DynamicObjectCollection cols = dynamicObject.getDynamicObjectCollection("entryentity");
  41               String roleName = ((DynamicObject)cols.get(cols.size() - 1)).getString("userrole");
  42 >             long currUserId = RequestContext.get().getCurrUserId();
  43               DynamicObject object = cols.stream().filter(dy -> StringUtils.equals((CharSequence)roleName, (CharSequence)dy.getString("userrole")) && currUserId == dy.getLong("user.id")).findFirst().orElse(null);
  44               int index = cols.indexOf((Object)object);
```

**QUERY_BUILDER** · RenewInqueryRepository L36
```java
  34   
  35       public List<Long> queryByEmpIdAndStatus(List<Long> employeeIds) {
  36 >         QFilter qFilter = new QFilter("employee", "in", employeeIds);
  37           QFilter statusFilter = new QFilter("businessstatus", "!=", (Object)RenewInqueryStatusEnum.FINISHED.getCode());
  38           DynamicObject[] dynamicObjects = SERVICE_HELPER.query("employee", new QFilter[]{qFilter, statusFilter});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskAgreeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp -->

## ISV 扩展指引（基于 InqueryTaskDisagreeOp 真实证）

> FQN: `kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hlcm.opplugin.contract.renew.InqueryTaskDisagreeOp -->
