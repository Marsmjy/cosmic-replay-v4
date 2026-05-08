# core_hr_parttime · 扩展点

> **聚合场景**：core_hr_parttime · 包含 4 个 hbss 字典实体（兼职管理 5 单据聚合 · chgaction 大类 = INFO_TRANSFER (兼职子类)。实证：兼职 → 创建副 hrpi_assignment (i...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

兼职管理 5 单据聚合 · chgaction 大类 = INFO_TRANSFER (兼职子类)。实证：兼职 → 创建副 hrpi_assignment (isprimary=false) + 新 hrpi_empposorgrel。兼职终止 → 终结副 assignment.enddate。关键 OP：PartSaveOp / PartTimeEffectOp / PartBillSubmitAndEffectOp / PartTimeAfterEffectOp / PartTimeEndAfterEffectOp。继承通用模板：hpfs_hrpartbillorgtplext (新增任职经历类) / hpfs_hrendbillorgtplext (任职终止类)。通用 OP：PerChgTplSaveOp / PerChgTplEffectOp。标品定时任务：hdm_parteffect_plan_SKDP_S / hdm_batchpartquit_plan_SKDP_S。

## 涉及实体（4 个）

- `hdm_parttimeapplybill`
- `hdm_parttimeendbill`
- `hdm_batchparttime`
- `hdm_parttimebatch`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（4 个）
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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgPartBillTplEdit -->

## ISV 扩展指引（基于 PerChgPartBillTplEdit 真实证）

> FQN: `kd.hr.hpfs.formplugin.PerChgPartBillTplEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgPartBillTplEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `propertyChanged`

### 可重写方法（target.java self）
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgPartBillTplEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgPartBillTplEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgPartBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin -->

## ISV 扩展指引（基于 ParttimeApplyBillHeadPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterCreateNewData`, `afterBindData`

### 可重写方法（target.java self）
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void setBaseFieldValue()`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillHeadPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit -->

## ISV 扩展指引（基于 ParttimeApplyBillEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `registerListener`, `afterBindData`, `afterLoadData`, `propertyChanged`, `beforeF7Select`, `beforeDoOperation`, `afterDoOperation`, `beforeClosed`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeShowTips(kd.bos.form.control.events.BeforeShowTipsEvent)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit -->

## ISV 扩展指引（基于 ParttimeButtonEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin -->

## ISV 扩展指引（基于 ParttimeSideWorkflowPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeSideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin -->

## ISV 扩展指引（基于 ParttimeChgActionPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.common.hpfs.CommonChgActionPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `protected protected void setDefaultAffaction()`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ParttimeChgActionPlugin L30
```java
  28           }
  29           Object defaultAffaction = DefaultValueUtil.getDyDefaultValue((IDataModel)this.getModel(), (String)"affaction");
  30 >         DynamicObject affaction = RepositoryUtils.queryDynamicObject((String)"hpfs_chgaction", (String)"id, numebr", (QFilter[])new QFilter[]{new QFilter("id", "=", defaultAffaction)});
  31           if (affaction != null) {
  32               return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeChgActionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

## ISV 扩展指引（基于 CommonPermissionEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeF7Select`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · DataFilterService L70
```java
  68               return dimValueResultDto;
  69           }
  70 >         DimValueResultWithSub dimValueResultWithSub = (DimValueResultWithSub)HRMServiceHelper.invokeBizService((String)"hrmp", (String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getEntityDimValueWithSub", (Object[])new Object[]{RequestContext.getOrCreate().getCurrUserId(), BizAppServiceHelp.getAppIdByFormNum((String)entityNum), entityNum, "47150e89000000ac", "orgdesign"});
  71           ArrayList<Long> orgIds = new ArrayList<Long>(dimValueResultWithSub.getDimValues().size());
  72           if (!dimValueResultWithSub.isAll()) {
```

**QUERY_BUILDER** · DataFilterService L139
```java
 137       public QFilter getQFilterWithJobLevelScm(FieldNameInfo fieldNameInfo) {
 138           long jobLevelScmId = (Long)this.getJobLevelGradeScmId((FieldNameInfo)fieldNameInfo).item1;
 139 >         return jobLevelScmId == 0L ? null : new QFilter("id", "=", (Object)jobLevelScmId);
 140       }
 141   
```

**READ_VIA_HELPER** · DataFilterService L83
```java
  81               }
  82               if (!HRCollUtil.isEmpty(includeSubIds)) {
  83 >                 List allSubordinateOrgs = OrgUnitServiceHelper.getAllSubordinateOrgs((String)"25", includeSubIds, (boolean)true, (boolean)true);
  84                   orgIds.addAll(allSubordinateOrgs);
  85               }
```

**CALL_CROSS_SERVICE** · BizMserviceInvokeService L56
```java
  54               for (List idList : list) {
  55                   List dataList;
  56 >                 HrApiResponse result = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbpm", (String)"IPositionService", (String)"queryStandardPositionByOrg", (Object[])new Object[]{idList, null, true});
  57                   if (!result.isSuccess() || HRObjectUtils.isEmpty((Object)(dataList = (List)result.getData()))) continue;
  58                   for (Map data : dataList) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList -->

## ISV 扩展指引（基于 BasePartTimeApplyBillList 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.BasePartTimeApplyBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList -->

## ISV 扩展指引（基于 ParttimeApplyBillList 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeBindData`, `afterDoOperation`, `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `protected protected java.util.List<java.lang.String> initDefaultFixedColumnList()`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.ParttimeApplyBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

## ISV 扩展指引（基于 HRCommonListFilterPlugin 真实证）

> FQN: `kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

## ISV 扩展指引（基于 CommonPermissionList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void filterColumnSetFilter(kd.bos.form.events.SetFilterEvent)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · DataFilterService L70
```java
  68               return dimValueResultDto;
  69           }
  70 >         DimValueResultWithSub dimValueResultWithSub = (DimValueResultWithSub)HRMServiceHelper.invokeBizService((String)"hrmp", (String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getEntityDimValueWithSub", (Object[])new Object[]{RequestContext.getOrCreate().getCurrUserId(), BizAppServiceHelp.getAppIdByFormNum((String)entityNum), entityNum, "47150e89000000ac", "orgdesign"});
  71           ArrayList<Long> orgIds = new ArrayList<Long>(dimValueResultWithSub.getDimValues().size());
  72           if (!dimValueResultWithSub.isAll()) {
```

**QUERY_BUILDER** · DataFilterService L139
```java
 137       public QFilter getQFilterWithJobLevelScm(FieldNameInfo fieldNameInfo) {
 138           long jobLevelScmId = (Long)this.getJobLevelGradeScmId((FieldNameInfo)fieldNameInfo).item1;
 139 >         return jobLevelScmId == 0L ? null : new QFilter("id", "=", (Object)jobLevelScmId);
 140       }
 141   
```

**READ_VIA_HELPER** · DataFilterService L83
```java
  81               }
  82               if (!HRCollUtil.isEmpty(includeSubIds)) {
  83 >                 List allSubordinateOrgs = OrgUnitServiceHelper.getAllSubordinateOrgs((String)"25", includeSubIds, (boolean)true, (boolean)true);
  84                   orgIds.addAll(allSubordinateOrgs);
  85               }
```

**CALL_CROSS_SERVICE** · BizMserviceInvokeService L56
```java
  54               for (List idList : list) {
  55                   List dataList;
  56 >                 HrApiResponse result = (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbpm", (String)"IPositionService", (String)"queryStandardPositionByOrg", (Object[])new Object[]{idList, null, true});
  57                   if (!result.isSuccess() || HRObjectUtils.isEmpty((Object)(dataList = (List)result.getData()))) continue;
  58                   for (Map data : dataList) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartSaveOp -->

## ISV 扩展指引（基于 PartSaveOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `protected protected void setFieldValue(kd.bos.dataentity.entity.DynamicObject, java.util.Map<java.lang.String, java.lang.Object>, java.util.Map<java.lang.Long, kd.bos.dataentity.entity.DynamicObject>)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillSubmitOp -->

## ISV 扩展指引（基于 PartBillSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.UnSubmitOp -->

## ISV 扩展指引（基于 UnSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.UnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.UnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.UnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.UnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.UnSubmitOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp -->

## ISV 扩展指引（基于 PartBillSubmitAndEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillSubmitAndEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeEffectOp -->

## ISV 扩展指引（基于 PartTimeEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEffectOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp -->

## ISV 扩展指引（基于 ParttimeEffectRetryOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.ParttimeEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillDiscardOp -->

## ISV 扩展指引（基于 PartBillDiscardOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillDiscardOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillDiscardOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillDiscardOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillDiscardOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillDiscardOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp -->

## ISV 扩展指引（基于 PartTimeAfterEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp/`

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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeAfterEffectOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillRollbackOp -->

## ISV 扩展指引（基于 PartBillRollbackOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartBillRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillRollbackOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PartBillRollbackOp L43
```java
  41               return;
  42           }
  43 >         long currUserId = RequestContext.get().getCurrUserId();
  44           Date termDate = new Date();
  45           OperateOption option = this.getOption();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartBillRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartBillRollbackOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditPassOp -->

## ISV 扩展指引（基于 WFAuditPassOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp -->

## ISV 扩展指引（基于 WFRejectToSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditPendingOp -->

## ISV 扩展指引（基于 WFAuditPendingOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditPendingOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPendingOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ParttimeApplyRepository L44
```java
  42   
  43       public Map<Long, List<DynamicObject>> queryPartBill(String fields, List<Long> employeeIds) {
  44 >         QFilter qFilter = new QFilter("bb_em_tid.id", "in", employeeIds);
  45           qFilter.and("parttimestatus", "=", (Object)PartTimeStatusEnum.PROCESS.getCode());
  46           DynamicObject[] batchPartBillDys = this.query(fields, new QFilter[]{qFilter}, "createtime desc");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPendingOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditPendingOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditPendingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp -->

## ISV 扩展指引（基于 WFAuditNotPassOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.PartBaseOp`
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.WFAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp -->

## ISV 扩展指引（基于 PartTimeAfterRollbackOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ParttimeApplyRepository L44
```java
  42   
  43       public Map<Long, List<DynamicObject>> queryPartBill(String fields, List<Long> employeeIds) {
  44 >         QFilter qFilter = new QFilter("bb_em_tid.id", "in", employeeIds);
  45           qFilter.and("parttimestatus", "=", (Object)PartTimeStatusEnum.PROCESS.getCode());
  46           DynamicObject[] batchPartBillDys = this.query(fields, new QFilter[]{qFilter}, "createtime desc");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeAfterRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp -->

## ISV 扩展指引（基于 ParttimeTerminalDoOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp/`

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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.ParttimeTerminalDoOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp -->

## ISV 扩展指引（基于 PerChgEndTplFinishSupRelOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgEndTplFinishSupRelOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp -->

## ISV 扩展指引（基于 PartTimeEndAfterEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp/`

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

**QUERY_BUILDER** · ParttimeApplyRepository L44
```java
  42   
  43       public Map<Long, List<DynamicObject>> queryPartBill(String fields, List<Long> employeeIds) {
  44 >         QFilter qFilter = new QFilter("bb_em_tid.id", "in", employeeIds);
  45           qFilter.and("parttimestatus", "=", (Object)PartTimeStatusEnum.PROCESS.getCode());
  46           DynamicObject[] batchPartBillDys = this.query(fields, new QFilter[]{qFilter}, "createtime desc");
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.PartTimeEndAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit -->

## ISV 扩展指引（基于 BatchParttimeEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `afterBindData`, `beforeDoOperation`, `validate`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit -->

## ISV 扩展指引（基于 BatchParttimeHeadEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit -->

## ISV 扩展指引（基于 BatchParttimeButtonEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit -->

## ISV 扩展指引（基于 BatchPartTimeEntryImportEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.common.batch.CommonEntryImportEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `protected protected kd.hr.hdm.business.application.common.service.batchimport.EntryImportController getEntryImportController()`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeImportEntry(kd.bos.entity.datamodel.events.BeforeImportEntryEventArgs)`

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

**CALL_CROSS_SERVICE** · SihcConfigUtil L20
```java
  18       public static Boolean enableSihc() {
  19           try {
  20 >             return (Boolean)HRMServiceHelper.invokeHRMPService((String)"soecs", (String)"SystemConfigMService", (String)"enableSihc", (Object[])new Object[0]);
  21           }
  22           catch (Exception e) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchPartTimeEntryImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit -->

## ISV 扩展指引（基于 BatchParttimeEntryEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `afterBindData`, `propertyChanged`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · SihcConfigUtil L20
```java
  18       public static Boolean enableSihc() {
  19           try {
  20 >             return (Boolean)HRMServiceHelper.invokeHRMPService((String)"soecs", (String)"SystemConfigMService", (String)"enableSihc", (Object[])new Object[0]);
  21           }
  22           catch (Exception e) {
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeEntryEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit -->

## ISV 扩展指引（基于 BatchParttimeTerminateEdit 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin -->

## ISV 扩展指引（基于 BatchParttimeChgActionPlugin 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hdm.formplugin.common.hpfs.CommonChgActionPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeChgActionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList -->

## ISV 扩展指引（基于 BatchParttimeList 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList -->

## ISV 扩展指引（基于 BatchParttimeTerminateList 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ParttimeBatchRepository L72
```java
  70   
  71       public DynamicObject[] query(String fields, List<Long> ids) {
  72 >         QFilter idFilter = new QFilter("id", "in", ids);
  73           QFilter[] idFilterArray = new QFilter[]{idFilter};
  74           return this.query(fields, idFilterArray);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeTerminateList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList -->

## ISV 扩展指引（基于 BatchParttimeRollBackList 真实证）

> FQN: `kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.formplugin.parttime.web.batch.BatchParttimeRollBackList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchPartSaveOp -->

## ISV 扩展指引（基于 BatchPartSaveOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchPartSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.BatchPartBaseOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · AdminOrgExternalServiceImpl L62
```java
  60           HRBaseServiceHelper adminorgServiceHelper = new HRBaseServiceHelper("haos_adminorghrf7");
  61           String selectProperties = "id,boid,sourcevid";
  62 >         QFilter idFilter = new QFilter("id", "in", adminorgIds);
  63           DynamicObject[] adminOrgDys = adminorgServiceHelper.queryOriginalArray(selectProperties, new QFilter[]{idFilter});
  64           Map<Long, DynamicObject> idToBoIdMap = Arrays.stream(adminOrgDys).collect(Collectors.toMap(dyn -> dyn.getLong("id"), Function.identity()));
```

**READ_VIA_HELPER** · AdminOrgExternalServiceImpl L63
```java
  61           String selectProperties = "id,boid,sourcevid";
  62           QFilter idFilter = new QFilter("id", "in", adminorgIds);
  63 >         DynamicObject[] adminOrgDys = adminorgServiceHelper.queryOriginalArray(selectProperties, new QFilter[]{idFilter});
  64           Map<Long, DynamicObject> idToBoIdMap = Arrays.stream(adminOrgDys).collect(Collectors.toMap(dyn -> dyn.getLong("id"), Function.identity()));
  65           return idToBoIdMap;
```

**CALL_CROSS_SERVICE** · AdminOrgExternalServiceImpl L35
```java
  33               return Collections.emptyMap();
  34           }
  35 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSBatchAdminOrgInfoQueryService", (String)"queryOrgStructToMap", (Object[])new Object[]{param, new Date(), Boolean.TRUE});
  36       }
  37   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchPartSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp -->

## ISV 扩展指引（基于 BatchParttimeSubmitOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.BatchPartBaseOp`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp -->

## ISV 扩展指引（基于 BatchParttimeEventOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeEventOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp -->

## ISV 扩展指引（基于 BatchParttimeSubmitEffectOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hdm.opplugin.parttime.BatchPartBaseOp`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp -->

## ISV 扩展指引（基于 BatchPartBillRollbackOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp/`

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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchPartBillRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp -->

## ISV 扩展指引（基于 BatchParttimeTerminateDoOp 真实证）

> FQN: `kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp/`

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

**CALL_CROSS_SERVICE** · JobExternalServiceImpl L44
```java
  42               return null;
  43           }
  44 >         return (HrApiResponse)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobHisInfoByNumberToMap", (Object[])new Object[]{jobNumber, effectDate});
  45       }
  46   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hdm.opplugin.parttime.BatchParttimeTerminateDoOp -->
