# core_hr_onboard · 扩展点

> **聚合场景**：core_hr_onboard · 包含 7 个 hbss 字典实体（入职管理 7 单据聚合 · chgaction 大类 = INFO_INIT。实证：OnbrdEffectOp.beginOperationTransactio...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

入职管理 7 单据聚合 · chgaction 大类 = INFO_INIT。实证：OnbrdEffectOp.beginOperationTransaction 调 IPersonGenericService.saveBatch 创建 hrpi_employee + hrpi_assignment + hrpi_empentrel + hrpi_trialperiod。关键 OP：OnbrdBillSaveOp / OnbrdEffectOp / OnbrdAfterEffectOp / PreOnBrdSaveOp。ISV 扩展点：IStaffOnbrdExtPlugin (占编)、IHOMLoginExtPlugin (登录)、IOnbrdInviteSupportAttachmentExtPlugin (附件)、IActivityDomainExtPlugin (协作)。

## 涉及实体（7 个）

- `hom_onbrdinfo`
- `hom_onbrdinfo_emp`
- `hom_preonbrdbasebill`
- `hom_prebatchonbrdbill`
- `hom_personallonbrd`
- `hom_personwaitstart`
- `hom_candidate`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（7 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp -->

## ISV 扩展指引（基于 PerChgOnbrdTplCommonOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp -->

## ISV 扩展指引（基于 PerChgOnbrdTplEffectOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdEffectOp -->

## ISV 扩展指引（基于 OnbrdEffectOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp -->

## ISV 扩展指引（基于 OnbrdConfirmOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp/`

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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp -->

## ISV 扩展指引（基于 OnbrdEffectRetryOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp -->

## ISV 扩展指引（基于 OnbrdAfterEffectOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp/`

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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp -->

## ISV 扩展指引（基于 OnbrdStartUpProcessOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · HomToHrcsAppServiceImpl L91
```java
  89               }
  90               LOG.info("dealTransfer_batchTransferTask_star:{},userId:{}", activityIns.item2 == null ? "null" : activityIns.item2, (Object)userId);
  91 >             OperationResult result = (OperationResult)HRMServiceHelper.invokeHRMPService((String)HRCS, (String)IHRCS_ACTIVITY_SERVICE, (String)"batchTransferTask", (Object[])new Object[]{activityIns.item2, userId, description == null ? "" : description});
  92               LOG.info("dealTransfer_end:{}", (Object)(result == null ? "null" : result.toString()));
  93               ArrayList<Long> personList = new ArrayList<Long>();
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp -->

## ISV 扩展指引（基于 ForceApprovalAuditPassOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.onbrd.AbstractApprovalAuditOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HomCommonRepository L31
```java
  29           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(entityName);
  30           if (HRStringUtils.isEmpty((String)selectProperties)) {
  31 >             return serviceHelper.loadDynamicObject(new QFilter("id", "=", pk));
  32           }
  33           return serviceHelper.queryOne(selectProperties, new QFilter[]{new QFilter("id", "=", pk)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp -->

## ISV 扩展指引（基于 OnbrdBreakupExtOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · OnbrdCommonAppServiceImpl L310
```java
 308   
 309       public boolean checkPermission(String entityNum, String permItemId, String appId) {
 310 >         Long userId = RequestContext.get().getCurrUserId();
 311           return PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityNum, (String)permItemId);
 312       }
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDynamicObjectUtils L338
```java
 336               long adminDivisionId = Long.parseLong(result);
 337               QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338 >             DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
 340               String[] admindivisionArray = admindivisionFullName.split("_");
```

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · PersonFileIntegrateServiceImpl L223
```java
 221           }
 222           catch (Exception exception) {
 223 >             throw new KDBizException(exception.getMessage());
 224           }
 225       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp -->

## ISV 扩展指引（基于 OnbrdConfirmExtOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgHomBillTplEdit -->

## ISV 扩展指引（基于 PerChgHomBillTplEdit 真实证）

> FQN: `kd.hr.hpfs.formplugin.PerChgHomBillTplEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgHomBillTplEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.bill.AbstractBillPlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `propertyChanged`, `afterBindData`

### 可重写方法（target.java self）
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgHomBillTplEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgHomBillTplEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.PerChgHomBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

## ISV 扩展指引（基于 ForbidUrlOpenPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · ForbidUrlOpenPlugin L42
```java
  40           BillShowParameter billShowParameter;
  41           super.preOpenForm(args);
  42 >         if (PermissionServiceHelper.isSuperUser((long)RequestContext.get().getCurrUserId())) {
  43               return;
  44           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit -->

## ISV 扩展指引（基于 OnbrdInfoEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `afterCreateNewData`, `afterDoOperation`, `afterBindData`, `beforeDoOperation`, `propertyChanged`, `registerListener`, `beforeF7Select`, `beforeClosed`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HrGuestUrlUtil L52
```java
  50           clientViewProxy.addAction("openUrl", urlMap);
  51           String hrGlobalSessionId = HrUserContext.getGlobalSessionId();
  52 >         String accountId = RequestContext.get().getAccountId();
  53           HrUserCacheUtil.remove((String)accountId, (String)hrGlobalSessionId);
  54           LoginOpDTO loginOpDTO = new LoginOpDTO(HrGuestUrlUtil.transFromUserID(hrUserVO.getRealBizUserId(), loginType), "logout", "logout", currClientType);
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

**CALL_CROSS_SERVICE** · HrGuestUrlUtil L55
```java
  53           HrUserCacheUtil.remove((String)accountId, (String)hrGlobalSessionId);
  54           LoginOpDTO loginOpDTO = new LoginOpDTO(HrGuestUrlUtil.transFromUserID(hrUserVO.getRealBizUserId(), loginType), "logout", "logout", currClientType);
  55 >         DispatchServiceHelper.invokeBizService((String)"hrmp", (String)"hbss", (String)"IHBSSLoginService", (String)"insertLoginOpLog", (Object[])new Object[]{loginOpDTO});
  56       }
  57   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit -->

## ISV 扩展指引（基于 AgainOnbrdInfoEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterCreateNewData`, `beforeBindData`, `propertyChanged`, `beforeF7Select`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HrGuestUrlUtil L52
```java
  50           clientViewProxy.addAction("openUrl", urlMap);
  51           String hrGlobalSessionId = HrUserContext.getGlobalSessionId();
  52 >         String accountId = RequestContext.get().getAccountId();
  53           HrUserCacheUtil.remove((String)accountId, (String)hrGlobalSessionId);
  54           LoginOpDTO loginOpDTO = new LoginOpDTO(HrGuestUrlUtil.transFromUserID(hrUserVO.getRealBizUserId(), loginType), "logout", "logout", currClientType);
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

**CALL_CROSS_SERVICE** · HrGuestUrlUtil L55
```java
  53           HrUserCacheUtil.remove((String)accountId, (String)hrGlobalSessionId);
  54           LoginOpDTO loginOpDTO = new LoginOpDTO(HrGuestUrlUtil.transFromUserID(hrUserVO.getRealBizUserId(), loginType), "logout", "logout", currClientType);
  55 >         DispatchServiceHelper.invokeBizService((String)"hrmp", (String)"hbss", (String)"IHBSSLoginService", (String)"insertLoginOpLog", (Object[])new Object[]{loginOpDTO});
  56       }
  57   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit -->

## ISV 扩展指引（基于 OnboardCadreInfoEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeDoOperation`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OnboardCadreInfoEdit L149
```java
 147       private DynamicObject getAppointRemoveRelObj(String appDispatchNum, long employeeId) {
 148           String selectFields = "company,adminorgvid,positionvid,empposrel,jobvid";
 149 >         QFilter qFilters = new QFilter("employee.id", "=", (Object)employeeId).and(new QFilter("appointdispatchnymber", "=", (Object)appDispatchNum)).and(new QFilter("posstatus.id", "=", (Object)1010L)).and(new QFilter("isdeleted", "=", (Object)Boolean.FALSE)).and(new QFilter("appointtypestatus", "=", (Object)"1"));
 150           Object[] dynamicObjects = HrpiRepository.queryHrpiAttachedData((String)"hrpi_appointremoverel", (String)selectFields, (QFilter[])new QFilter[]{qFilters});
 151           if (HRArrayUtils.isEmpty((Object[])dynamicObjects)) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin -->

## ISV 扩展指引（基于 OnbrdChgActionPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · BaseDataDomainServiceImpl L158
```java
 156       public Map<Long, Map<String, List<DynamicObject>>> getJobLevelGradesByJobIds(List<Long> jobBoids) {
 157           LOG.info("getJobLevelGradesByJobIds size:" + jobBoids.size());
 158 >         Map queryResultRpc = (Map)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobLevelGradeRangeByIdToMap", (Object[])new Object[]{jobBoids});
 159           if (CollectionUtils.isEmpty((Map)queryResultRpc)) {
 160               return null;
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin -->

## ISV 扩展指引（基于 FrozenColumnsPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin -->

## ISV 扩展指引（基于 OnbrdBillListPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeClosed`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp -->

## ISV 扩展指引（基于 OnbrdBillSaveOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp/`

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

**WRITE_VIA_HELPER** · HomToHcfAppServiceImpl L527
```java
 525           }
 526           if (flag2) {
 527 >             new HRBaseServiceHelper("hcf_candidate").updateOne(candidateDy);
 528           }
 529       }
```

**CALL_CROSS_SERVICE** · HomToHcfAppServiceImpl L449
```java
 447           }
 448           try {
 449 >             Map ret = (Map)DispatchServiceHelper.invokeBizService((String)"swc", (String)"hsbs", (String)"IHSBSPerBankCardService", (String)"queryPerBankCard", (Object[])new Object[]{Collections.singletonList(employeeId)});
 450               apiResult = ApiResult.fromMap((Map)ret);
 451           }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp -->

## ISV 扩展指引（基于 OnbrdBillSaveOpenAPIOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp/`

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

**WRITE_VIA_HELPER** · HomToHcfAppServiceImpl L527
```java
 525           }
 526           if (flag2) {
 527 >             new HRBaseServiceHelper("hcf_candidate").updateOne(candidateDy);
 528           }
 529       }
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp -->

## ISV 扩展指引（基于 ApprovalAuditPassOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.onbrd.AbstractApprovalAuditOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HomCommonRepository L31
```java
  29           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(entityName);
  30           if (HRStringUtils.isEmpty((String)selectProperties)) {
  31 >             return serviceHelper.loadDynamicObject(new QFilter("id", "=", pk));
  32           }
  33           return serviceHelper.queryOne(selectProperties, new QFilter[]{new QFilter("id", "=", pk)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.UnSubmitOp -->

## ISV 扩展指引（基于 UnSubmitOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.UnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.UnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HomToHrcsAppServiceImpl L211
```java
 209   
 210       public QFilter getDataResult(String entityNumber, String permNumber) {
 211 >         long userId = RequestContext.get().getCurrUserId();
 212           String appName = ThreadLocalUtil.get();
 213           appName = HRStringUtils.isNotEmpty((String)appName) ? appName : "hom";
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDynamicObjectUtils L338
```java
 336               long adminDivisionId = Long.parseLong(result);
 337               QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338 >             DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
 340               String[] admindivisionArray = admindivisionFullName.split("_");
```

**CALL_CROSS_SERVICE** · HrmpExternalServiceImpl L31
```java
  29   
  30       public Map<Long, DynamicObject> queryAdminOrgDetailInfoByBoidToMap(List<Long> ids, Date date, String selectFields) {
  31 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSBatchAdminOrgInfoQueryService", (String)"queryAdminOrgDetailInfoByBoidToMap", (Object[])new Object[]{ids, date, selectFields});
  32       }
  33   
```

**THROW_BIZ_EXCEPTION** · PersonFileIntegrateServiceImpl L223
```java
 221           }
 222           catch (Exception exception) {
 223 >             throw new KDBizException(exception.getMessage());
 224           }
 225       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.UnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.UnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.UnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp -->

## ISV 扩展指引（基于 ApprovalAuditingOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.onbrd.AbstractApprovalAuditOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp -->

## ISV 扩展指引（基于 ApprovalTerminationOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · OnbrdCommonAppServiceImpl L310
```java
 308   
 309       public boolean checkPermission(String entityNum, String permItemId, String appId) {
 310 >         Long userId = RequestContext.get().getCurrUserId();
 311           return PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityNum, (String)permItemId);
 312       }
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDynamicObjectUtils L338
```java
 336               long adminDivisionId = Long.parseLong(result);
 337               QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338 >             DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
 340               String[] admindivisionArray = admindivisionFullName.split("_");
```

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · PersonFileIntegrateServiceImpl L223
```java
 221           }
 222           catch (Exception exception) {
 223 >             throw new KDBizException(exception.getMessage());
 224           }
 225       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp -->

## ISV 扩展指引（基于 ApprovalRejectToSubmitOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp/`

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

**CONTEXT_ACCESS** · OnbrdCommonAppServiceImpl L310
```java
 308   
 309       public boolean checkPermission(String entityNum, String permItemId, String appId) {
 310 >         Long userId = RequestContext.get().getCurrUserId();
 311           return PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityNum, (String)permItemId);
 312       }
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDynamicObjectUtils L338
```java
 336               long adminDivisionId = Long.parseLong(result);
 337               QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338 >             DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
 340               String[] admindivisionArray = admindivisionFullName.split("_");
```

**CALL_CROSS_SERVICE** · HrmpExternalServiceImpl L31
```java
  29   
  30       public Map<Long, DynamicObject> queryAdminOrgDetailInfoByBoidToMap(List<Long> ids, Date date, String selectFields) {
  31 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSBatchAdminOrgInfoQueryService", (String)"queryAdminOrgDetailInfoByBoidToMap", (Object[])new Object[]{ids, date, selectFields});
  32       }
  33   
```

**THROW_BIZ_EXCEPTION** · PersonFileIntegrateServiceImpl L223
```java
 221           }
 222           catch (Exception exception) {
 223 >             throw new KDBizException(exception.getMessage());
 224           }
 225       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp -->

## ISV 扩展指引（基于 ApprovalAuditNotPassOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · OnbrdCommonAppServiceImpl L310
```java
 308   
 309       public boolean checkPermission(String entityNum, String permItemId, String appId) {
 310 >         Long userId = RequestContext.get().getCurrUserId();
 311           return PermissionServiceHelper.checkPermission((Long)userId, (String)appId, (String)entityNum, (String)permItemId);
 312       }
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRDynamicObjectUtils L338
```java
 336               long adminDivisionId = Long.parseLong(result);
 337               QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338 >             DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
 340               String[] admindivisionArray = admindivisionFullName.split("_");
```

**CALL_CROSS_SERVICE** · HrmpExternalServiceImpl L31
```java
  29   
  30       public Map<Long, DynamicObject> queryAdminOrgDetailInfoByBoidToMap(List<Long> ids, Date date, String selectFields) {
  31 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSBatchAdminOrgInfoQueryService", (String)"queryAdminOrgDetailInfoByBoidToMap", (Object[])new Object[]{ids, date, selectFields});
  32       }
  33   
```

**THROW_BIZ_EXCEPTION** · PersonFileIntegrateServiceImpl L223
```java
 221           }
 222           catch (Exception exception) {
 223 >             throw new KDBizException(exception.getMessage());
 224           }
 225       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit -->

## ISV 扩展指引（基于 PreOnBrdBillHeadEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit -->

## ISV 扩展指引（基于 PreOnBrdButtonEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `protected protected java.lang.String getPreOnBrdAuditStatus()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit -->

## ISV 扩展指引（基于 PreOnBrdBillEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `afterCreateNewData`, `beforeDoOperation`, `afterDoOperation`, `propertyChanged`, `afterBindData`, `preOpenForm`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterAddRow(kd.bos.entity.datamodel.events.AfterAddRowEventArgs)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin -->

## ISV 扩展指引（基于 SideWorkflowPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit -->

## ISV 扩展指引（基于 PreOnBrdF7SelectEdit 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · BaseDataDomainServiceImpl L158
```java
 156       public Map<Long, Map<String, List<DynamicObject>>> getJobLevelGradesByJobIds(List<Long> jobBoids) {
 157           LOG.info("getJobLevelGradesByJobIds size:" + jobBoids.size());
 158 >         Map queryResultRpc = (Map)HRMServiceHelper.invokeHRMPService((String)"hbjm", (String)"IHBJMJobService", (String)"queryJobLevelGradeRangeByIdToMap", (Object[])new Object[]{jobBoids});
 159           if (CollectionUtils.isEmpty((Map)queryResultRpc)) {
 160               return null;
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin -->

## ISV 扩展指引（基于 PreOnBrdBillListPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
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

**WRITE_VIA_HELPER** · PreOnBrdBillRepository L33
```java
  31   
  32       public Object[] update(DynamicObject[] preBills) {
  33 >         return SaveServiceHelper.save((DynamicObject[])preBills);
  34       }
  35   }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp -->

## ISV 扩展指引（基于 PreOnBrdSaveOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractPreOnBrdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp -->

## ISV 扩展指引（基于 PreOnBrdSubmitOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractPreOnBrdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp -->

## ISV 扩展指引（基于 PreOnBrdUnSubmitOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractPreOnBrdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp -->

## ISV 扩展指引（基于 PreApprovalRejectToSubmitOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HomCommonRepository L31
```java
  29           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(entityName);
  30           if (HRStringUtils.isEmpty((String)selectProperties)) {
  31 >             return serviceHelper.loadDynamicObject(new QFilter("id", "=", pk));
  32           }
  33           return serviceHelper.queryOne(selectProperties, new QFilter[]{new QFilter("id", "=", pk)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp -->

## ISV 扩展指引（基于 ApprovalAuditPassOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractApprovalAuditOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
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

**WRITE_VIA_HELPER** · HomToHcfAppServiceImpl L527
```java
 525           }
 526           if (flag2) {
 527 >             new HRBaseServiceHelper("hcf_candidate").updateOne(candidateDy);
 528           }
 529       }
```

**CALL_CROSS_SERVICE** · HomToHcfAppServiceImpl L449
```java
 447           }
 448           try {
 449 >             Map ret = (Map)DispatchServiceHelper.invokeBizService((String)"swc", (String)"hsbs", (String)"IHSBSPerBankCardService", (String)"queryPerBankCard", (Object[])new Object[]{Collections.singletonList(employeeId)});
 450               apiResult = ApiResult.fromMap((Map)ret);
 451           }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp -->

## ISV 扩展指引（基于 ApprovalAuditingOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractApprovalAuditOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HomCommonRepository L31
```java
  29           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(entityName);
  30           if (HRStringUtils.isEmpty((String)selectProperties)) {
  31 >             return serviceHelper.loadDynamicObject(new QFilter("id", "=", pk));
  32           }
  33           return serviceHelper.queryOne(selectProperties, new QFilter[]{new QFilter("id", "=", pk)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp -->

## ISV 扩展指引（基于 ApprovalAuditNotPassOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HomCommonRepository L31
```java
  29           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(entityName);
  30           if (HRStringUtils.isEmpty((String)selectProperties)) {
  31 >             return serviceHelper.loadDynamicObject(new QFilter("id", "=", pk));
  32           }
  33           return serviceHelper.queryOne(selectProperties, new QFilter[]{new QFilter("id", "=", pk)});
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp -->

## ISV 扩展指引（基于 PreOnBrdDeleteOp 真实证）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hom.opplugin.preonbrd.AbstractPreOnBrdOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin -->

## ISV 扩展指引（基于 OnbrdAddNewPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin -->

## ISV 扩展指引（基于 OnbrdStartupPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `closedCallBack`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin -->

## ISV 扩展指引（基于 QFilterSetPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`

### 可重写方法（target.java self）
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin -->

## ISV 扩展指引（基于 OnbrdBillListImportCertPlugin 真实证）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeItemClick`

### 可重写方法（target.java self）
- `public public void addItemClickListeners(java.lang.String...)`
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp -->

## ISV 扩展指引（基于 OnbrdInfoDeleteOp 真实证）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

**CALL_CROSS_SERVICE** · PersonFileIntegrateServiceImpl L100
```java
  98               return Maps.newHashMapWithExpectedSize((int)1);
  99           }
 100 >         return (Map)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIEmpSupRelService", (String)"querySuperiors", (Object[])new Object[]{paramList});
 101       }
 102   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp -->

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
