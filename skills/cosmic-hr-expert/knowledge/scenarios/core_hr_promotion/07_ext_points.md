# core_hr_promotion · 扩展点

> **聚合场景**：core_hr_promotion · 包含 3 个 hbss 字典实体（试用和转正 6 单据聚合 · chgaction 大类 = INFO_MODIFY (用工关系转正)。实证：RegEffectOp 改 hrpi_trialpe...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

试用和转正 6 单据聚合 · chgaction 大类 = INFO_MODIFY (用工关系转正)。实证：RegEffectOp 改 hrpi_trialperiod.status + hrpi_employee.regstatus + hrpi_empentrel。关键 OP：RegSaveOp / RegEffectOp / RegHrSupplySubmitOp / RegSelfSaveOp / RegAfterEffectOp。标品定时任务：hdm_regeffect_plan_SKDP_S 处理 datastatus=2 未来生效。ISV 扩展点：IProbationMessageExtPlugin (转正问询消息 url 重定向)。

## 涉及实体（3 个）

- `hdm_regselfhelpbill`
- `hdm_regbasebill`
- `hdm_batchregbill`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（3 个）
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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

## ISV 扩展指引（基于 PerChgNewBillTplEdit 真实证）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `beforeBindData`, `registerListener`, `propertyChanged`, `beforeDoOperation`, `afterDoOperation`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · PerChgNewBillTplEdit L326
```java
 324       private DynamicObject getDefaultAdminOrgFromManageOrgStrategy(long orgId) {
 325           long businessField = 1010L;
 326 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"getHrbuFromManageEmpStrategy", (Object[])new Object[]{orgId, 0L, businessField, 0L});
 327           LOG.info("Got default adminOrg :{} from HrbuFromManageEmpStrategy with orgId: {}.", (Object)result, (Object)orgId);
 328           return (DynamicObject)result.get("hrbu");
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin -->

## ISV 扩展指引（基于 RegBaseBillPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.reg.common.RegBillBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `registerListener`, `beforeClosed`, `afterBindData`, `afterLoadData`, `afterCreateNewData`, `beforeBindData`, `beforeDoOperation`, `closedCallBack`, `afterDoOperation`, `propertyChanged`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public boolean checkEmp(kd.bos.form.events.BeforeDoOperationEventArgs, long)`
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

**CALL_CROSS_SERVICE** · RegPeronalBillHelper L331
```java
 329       public Long queryEmployeeByCurrentUser() {
 330           long userId = RequestContext.get().getCurrUserId();
 331 >         HrApiResponse hrApiResponse = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmployeeService", (String)"queryEmployeeByUserIds", (Object[])new Object[]{Lists.newArrayList((Object[])new Long[]{userId}), null, "0"});
 332           if (!hrApiResponse.isSuccess()) {
 333               throw new KDBizException(ResManager.loadKDString((String)"\u83b7\u53d6\u7cfb\u7edf\u4eba\u5458\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"RegPeronalBillHelper_0", (String)"hr-hdm-business", (Object[])new Object[0]));
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegBaseBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin -->

## ISV 扩展指引（基于 RegSingleHrBillPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegSingleHrBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin -->

## ISV 扩展指引（基于 SideWorkflowPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.SideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin -->

## ISV 扩展指引（基于 RegChgAffactionPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.common.hpfs.CommonChgActionPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void setDefaultAffaction()`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin -->

## ISV 扩展指引（基于 WebRegSelfHelpPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterLoadData`, `preOpenForm`, `afterDoOperation`, `beforeClosed`, `closedCallBack`, `beforeDoOperation`, `afterBindData`, `afterCreateNewData`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · RegPeronalBillHelper L331
```java
 329       public Long queryEmployeeByCurrentUser() {
 330           long userId = RequestContext.get().getCurrUserId();
 331 >         HrApiResponse hrApiResponse = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmployeeService", (String)"queryEmployeeByUserIds", (Object[])new Object[]{Lists.newArrayList((Object[])new Long[]{userId}), null, "0"});
 332           if (!hrApiResponse.isSuccess()) {
 333               throw new KDBizException(ResManager.loadKDString((String)"\u83b7\u53d6\u7cfb\u7edf\u4eba\u5458\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"RegPeronalBillHelper_0", (String)"hr-hdm-business", (Object[])new Object[0]));
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.WebRegSelfHelpPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

## ISV 扩展指引（基于 PerChgNewBillTplList 真实证）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · PerChgNewBillTplList L50
```java
  48               Object pkVal = this.getSelectedRows().get(0).getPrimaryKeyValue();
  49               String entityId = ((IListView)this.getView()).getBillFormId();
  50 >             DynamicObject billDy = RepositoryUtils.queryDynamicObject((String)entityId, (String)"affrecord", (QFilter[])new QFilter[]{new QFilter("id", "=", pkVal)});
  51               long affRecordId = billDy.getLong("affrecord");
  52               if (affRecordId == 0L) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList -->

## ISV 扩展指引（基于 RegularApplySourceList 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `beforePackageData`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`, `setFilter`

### 可重写方法（target.java self）
- `public public void setEnableCustomSum(kd.bos.list.events.EnableCustomSumEvent)`
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `protected protected java.util.List<java.lang.String> initDefaultFixedColumnList()`
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
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

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.RegularApplySourceList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin -->

## ISV 扩展指引（基于 TerminateWorkFlowListPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · RegWorkFlowServiceImpl L37
```java
  35           }
  36           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("wf_hiprocinst");
  37 >         DynamicObject[] hiProcInstArray = serviceHelper.query(String.join((CharSequence)",", "processinstanceid", "businesskey"), new QFilter[]{new QFilter("businesskey", "in", businessKeys), new QFilter("endtype", "!=", (Object)"20"), new QFilter("superprocessinstanceid", "=", (Object)0L), QFilter.isNull((String)"endtime")});
  38           if (hiProcInstArray.length == 0) {
  39               LOGGER.error("hiProcInstArray is null");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.applybill.TerminateWorkFlowListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin -->

## ISV 扩展指引（基于 RegBaseBillMobPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractMobBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterCreateNewData`, `registerListener`, `afterLoadData`, `beforeBindData`, `afterBindData`, `click`, `propertyChanged`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · RegPeronalBillHelper L331
```java
 329       public Long queryEmployeeByCurrentUser() {
 330           long userId = RequestContext.get().getCurrUserId();
 331 >         HrApiResponse hrApiResponse = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmployeeService", (String)"queryEmployeeByUserIds", (Object[])new Object[]{Lists.newArrayList((Object[])new Long[]{userId}), null, "0"});
 332           if (!hrApiResponse.isSuccess()) {
 333               throw new KDBizException(ResManager.loadKDString((String)"\u83b7\u53d6\u7cfb\u7edf\u4eba\u5458\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"RegPeronalBillHelper_0", (String)"hr-hdm-business", (Object[])new Object[0]));
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.mobile.RegBaseBillMobPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin -->

## ISV 扩展指引（基于 RegSelfHelpMobPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractMobBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterLoadData`, `registerListener`, `click`, `preOpenForm`, `afterCreateNewData`, `beforeDoOperation`, `beforeClosed`, `afterDoOperation`, `closedCallBack`, `afterBindData`

### 可重写方法（target.java self）
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.mobile.RegSelfHelpMobPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin -->

## ISV 扩展指引（基于 RegMobileChgAffactionPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.reg.common.RegChgAffactionPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · RegMobileChgAffactionPlugin L48
```java
  46               List affactionIds = ChgActionUtils.getActionEnableByEntityId((String)"hdm_regbasebill");
  47               evt.getFormShowParameter().setCustomParam("businessvalue", (Object)"1");
  48 >             evt.getCustomQFilters().add(new QFilter("id", "in", (Object)affactionIds));
  49           } else if ("ba_e_laborrelstatus".equals(fieldKey)) {
  50               DynamicObject affaction = this.getModel().getDataEntity().getDynamicObject("affaction");
```

**READ_VIA_HELPER** · RegMobileChgAffactionPlugin L54
```java
  52                   return;
  53               }
  54 >             List laborrelstatusList = PersonnelChangeServiceHelper.getLaborrelstatusByAffaction((DynamicObject)affaction);
  55               if (laborrelstatusList != null && laborrelstatusList.size() > 0) {
  56                   evt.getCustomQFilters().add(new QFilter("id", "in", (Object)laborrelstatusList));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.common.RegMobileChgAffactionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## ISV 扩展指引（基于 PerChgTplSaveOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

## ISV 扩展指引（基于 PerChgTplUpdateChgRecordOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

**CALL_CROSS_SERVICE** · HRServiceUtil L49
```java
  47               param.put(adminOrgId, Collections.singleton(103010L));
  48           }
  49 >         List result = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"getHrBuByBusinessType", (Object[])new Object[]{param, 1010L});
  50           LOG.info("getHrBuByBusinessType result :{}", (Object)result);
  51           if (ObjectUtils.isEmpty((Object)result)) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSaveOp -->

## ISV 扩展指引（基于 RegSaveOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegAppBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp -->

## ISV 扩展指引（基于 RegSaveOpenAPIOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp/`

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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSaveOpenAPIOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

## ISV 扩展指引（基于 PerChgTplSubmitOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp/`

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
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

**CALL_CROSS_SERVICE** · HRServiceUtil L49
```java
  47               param.put(adminOrgId, Collections.singleton(103010L));
  48           }
  49 >         List result = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"getHrBuByBusinessType", (Object[])new Object[]{param, 1010L});
  50           LOG.info("getHrBuByBusinessType result :{}", (Object)result);
  51           if (ObjectUtils.isEmpty((Object)result)) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSubmitOp -->

## ISV 扩展指引（基于 RegSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegAppBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp -->

## ISV 扩展指引（基于 RegSelfSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegSelfAppBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSelfSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

## ISV 扩展指引（基于 PerChgTplDiscardChgRecordOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp/`

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

**CALL_CROSS_SERVICE** · ChgUtils L984
```java
 982   
 983       public static String getPrivacyPropJson() {
 984 >         return (String)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIDevParamConfigService", (String)"queryChgRecordFieldBlackList", (Object[])new Object[0]);
 985       }
 986   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp -->

## ISV 扩展指引（基于 RegUnSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegEventBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegUnSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

## ISV 扩展指引（基于 PerChgTplEffectOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp/`

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
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

**PUBLISH_EVENT** · PerChgTplEffectOp L163
```java
 161           effectChgRecordIds.forEach(effectChgRecordId -> {
 162               jsonObject.put("businessKeys", effectChgRecordId);
 163 >             EventServiceHelper.triggerEventSubscribe((String)"hpfs_hrnewbillorgtpl.effect", (String)jsonObject.toJSONString());
 164           });
 165           LOG.info("####PerChgTplEffectOp.endOperationTransaction.triggerEventSubscribes-cost1:{} ms.", (Object)(System.currentTimeMillis() - start2));
```

**CALL_CROSS_SERVICE** · HRServiceUtil L49
```java
  47               param.put(adminOrgId, Collections.singleton(103010L));
  48           }
  49 >         List result = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"getHrBuByBusinessType", (Object[])new Object[]{param, 1010L});
  50           LOG.info("getHrBuByBusinessType result :{}", (Object)result);
  51           if (ObjectUtils.isEmpty((Object)result)) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp -->

## ISV 扩展指引（基于 RegSubmitEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegAppBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp -->

## ISV 扩展指引（基于 RegRejectToSubmitEventOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegEventBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegRejectToSubmitEventOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp -->

## ISV 扩展指引（基于 RegWfAuditNotPassOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegEventBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegWfAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegEffectOp -->

## ISV 扩展指引（基于 RegEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp -->

## ISV 扩展指引（基于 RegEffectRetryOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.RegAfterEffectOp -->

## ISV 扩展指引（基于 RegAfterEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.reg.RegAfterEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegAfterEffectOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegAfterEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegAfterEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.RegAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

## ISV 扩展指引（基于 PerChgTplRollbackOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · ChgUtils L984
```java
 982   
 983       public static String getPrivacyPropJson() {
 984 >         return (String)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIDevParamConfigService", (String)"queryChgRecordFieldBlackList", (Object[])new Object[0]);
 985       }
 986   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp -->

## ISV 扩展指引（基于 RegRollbackOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.apply.RegRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

## ISV 扩展指引（基于 PerChgTplEffectSyncOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp/`

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
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

**CALL_CROSS_SERVICE** · HRServiceUtil L49
```java
  47               param.put(adminOrgId, Collections.singleton(103010L));
  48           }
  49 >         List result = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSStrategyService", (String)"getHrBuByBusinessType", (Object[])new Object[]{param, 1010L});
  50           LOG.info("getHrBuByBusinessType result :{}", (Object)result);
  51           if (ObjectUtils.isEmpty((Object)result)) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp -->

## ISV 扩展指引（基于 RegSelfSaveOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.RegSelfAppBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegSelfSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp -->

## ISV 扩展指引（基于 RegAppBillTerminateOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegAppBillTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp -->

## ISV 扩展指引（基于 RegRemindConfirmDelayOp 真实证）

> FQN: `kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · RegWorkFlowServiceImpl L37
```java
  35           }
  36           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("wf_hiprocinst");
  37 >         DynamicObject[] hiProcInstArray = serviceHelper.query(String.join((CharSequence)",", "processinstanceid", "businesskey"), new QFilter[]{new QFilter("businesskey", "in", businessKeys), new QFilter("endtype", "!=", (Object)"20"), new QFilter("superprocessinstanceid", "=", (Object)0L), QFilter.isNull((String)"endtime")});
  38           if (hiProcInstArray.length == 0) {
  39               LOGGER.error("hiProcInstArray is null");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.RegRemindConfirmDelayOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp -->

## ISV 扩展指引（基于 RegRpcDonothingOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegRpcDonothingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp -->

## ISV 扩展指引（基于 RegWfRemindOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp/`

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

**QUERY_BUILDER** · RegWorkFlowServiceImpl L37
```java
  35           }
  36           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("wf_hiprocinst");
  37 >         DynamicObject[] hiProcInstArray = serviceHelper.query(String.join((CharSequence)",", "processinstanceid", "businesskey"), new QFilter[]{new QFilter("businesskey", "in", businessKeys), new QFilter("endtype", "!=", (Object)"20"), new QFilter("superprocessinstanceid", "=", (Object)0L), QFilter.isNull((String)"endtime")});
  38           if (hiProcInstArray.length == 0) {
  39               LOGGER.error("hiProcInstArray is null");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.apply.RegWfRemindOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp -->

## ISV 扩展指引（基于 RegBillTerminateDoOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegBillTerminateDoOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp -->

## ISV 扩展指引（基于 RegHrSupplySubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegHrSupplySubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp -->

## ISV 扩展指引（基于 RegHrSupplementOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.RegHrSupplementOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit -->

## ISV 扩展指引（基于 BatchRegHeadEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit -->

## ISV 扩展指引（基于 BatchRegButtonEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit -->

## ISV 扩展指引（基于 BatchRegBillEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `propertyChanged`, `afterBindData`, `afterCreateNewData`, `beforeDoOperation`, `afterDoOperation`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin -->

## ISV 扩展指引（基于 BatchRegChgAffactionPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.common.hpfs.CommonChgActionPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegChgAffactionPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegList -->

## ISV 扩展指引（基于 BatchRegList 真实证）

> FQN: `kd.hr.hdm.formplugin.reg.web.batch.BatchRegList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.reg.web.batch.BatchRegList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.reg.web.batch.BatchRegList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp -->

## ISV 扩展指引（基于 BatchRegApplyBillSaveOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.BatchRegBillBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · RegBillServiceImpl L251
```java
 249   
 250       public void syncRegTrace(DynamicObject[] billObjs, String traceType) {
 251 >         long userId = RequestContext.get().getCurrUserId() == 0L ? 1L : RequestContext.get().getCurrUserId();
 252           this.syncRegTrace(new ArrayList<DynamicObject>(Arrays.asList(billObjs)), traceType, userId);
 253       }
```

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · PersonAboutServiceImpl L276
```java
 274           Map<Long, DynamicObject> empIdsWithLabStatus = Arrays.stream(dys).collect(Collectors.toMap(dy -> dy.getDynamicObject("employee").getLong("id"), Function.identity(), (v1, v2) -> v1));
 275           if (ObjectUtils.isEmpty(empIdsWithLabStatus)) {
 276 >             throw new KDBizException(ResManager.loadKDString((String)"\u83b7\u53d6\u4eba\u5458\u6570\u636e\u5931\u8d25\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u3002", (String)"PersonAboutServiceImpl_6", (String)"hr-hdm-business", (Object[])new Object[0]));
 277           }
 278           return empIdsWithLabStatus;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp -->

## ISV 扩展指引（基于 BatchRegApplyBillSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.BatchRegBillBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · OperationServiceUtil L109
```java
 107                   errInfo = operationResult.getMessage();
 108               }
 109 >             throw new KDBizException(errInfo);
 110           }
 111       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp -->

## ISV 扩展指引（基于 BatchRegUnSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp -->

## ISV 扩展指引（基于 BatchRegApplyBillSubmitEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.BatchRegBillBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · OperationServiceUtil L109
```java
 107                   errInfo = operationResult.getMessage();
 108               }
 109 >             throw new KDBizException(errInfo);
 110           }
 111       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.BatchRegApplyBillSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp -->

## ISV 扩展指引（基于 BatchRegWfRejectToSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp -->

## ISV 扩展指引（基于 BatchRegWfAuditNotPassOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp -->

## ISV 扩展指引（基于 BatchRegWfAuditPassOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegWfAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.BatchRegTerminateOp -->

## ISV 扩展指引（基于 BatchRegTerminateOp 真实证）

> FQN: `kd.hr.hdm.opplugin.reg.BatchRegTerminateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.BatchRegTerminateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.BatchRegTerminateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.reg.BatchRegTerminateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.reg.BatchRegTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp -->

## ISV 扩展指引（基于 BatchRegRollbackOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp -->

## ISV 扩展指引（基于 BatchRegTerminateDoOp 真实证）

> FQN: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.web.reg.batch.BatchRegEventOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
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

**QUERY_BUILDER** · TimeLineServiceUtil L69
```java
  67       public static boolean entityMetaBeReferenced(String entityNumber) {
  68           HRBaseServiceHelper helper = new HRBaseServiceHelper("bos_objecttyperef");
  69 >         return helper.isExists(new QFilter[]{new QFilter("frefobjecttypeid", "=", (Object)entityNumber)});
  70       }
  71   
```

**READ_VIA_HELPER** · TimeLineServiceUtil L293
```java
 291       private static Map<String, Object> getHBSSAppParam() {
 292           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(ParameterOrgUtils.getParamRootOrgId()));
 293 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
 294       }
 295   
```

**CALL_CROSS_SERVICE** · PersonAboutServiceImpl L581
```java
 579           paramMap.put("mustAllSuccess", true);
 580           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch param:{}", paramMap);
 581 >         Map result = (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIPersonGenericService", (String)"reviseVersionBatch", (Object[])new Object[]{paramMap});
 582           LOGGER.info("IHRPIPersonGenericService reviseVersionBatch result:{}", (Object)result);
 583       }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.web.reg.batch.BatchRegTerminateDoOp -->
