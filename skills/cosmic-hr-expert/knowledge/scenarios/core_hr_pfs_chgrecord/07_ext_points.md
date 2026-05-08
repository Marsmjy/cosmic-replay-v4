# core_hr_pfs_chgrecord · 扩展点

> **聚合场景**：core_hr_pfs_chgrecord · 包含 3 个 hbss 字典实体（**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.begin...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.beginOperationTransaction 调 IPersonGenericService.deleteBatch + saveBatch 实际改人员底表 → processPersonFlow 写流入流出 → processChargePerson 写部门负责人 → syncEmployeeLicenseCert 同步许可证。**datastatus 状态机**：0=待生效 / 1=已生效 / 2=未来生效定时（effecttime>now 且 future-effect 配置）。异常时 PerChgStatusEnum.SMALLEST_FAIL 写回单据 + 触发 aftereffect op。5 关键 OP：ChgRecordEffectOp / DiscardOp / AfterEffectOp / ChgRollbackOp / ChgRecordEditPlugin。

## 涉及实体（3 个）

- `hpfs_chgrecord`
- `hpfs_chgrecordentry`
- `hpfs_changerecord`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin -->

## ISV 扩展指引（基于 ChgRecordEditPlugin 真实证）

> FQN: `kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp -->

## ISV 扩展指引（基于 ChgRollbackOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp/`

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

**CONTEXT_ACCESS** · HRCertCacheManager L94
```java
  92   
  93           static int getUsedCertCount(String groupId) {
  94 >             String accountId = RequestContext.get().getAccountId();
  95               Map<String, String> certUsedCountMap = dataCenterGroupCertCountMap.get(accountId);
  96               if (certUsedCountMap == null) {
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · HRCertCacheManager L70
```java
  68   
  69       static int getPurchaseCount(String groupId) {
  70 >         int totalNumber = LicenseServiceHelper.getTotalNumber((Object)groupId);
  71           logger.info("HRCertCacheManager.getPurchaseCount groupId : {},totalNumber:{}", (Object)groupId, (Object)totalNumber);
  72           return totalNumber;
```

**CALL_CROSS_SERVICE** · ChgUtils L984
```java
 982   
 983       public static String getPrivacyPropJson() {
 984 >         return (String)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIDevParamConfigService", (String)"queryChgRecordFieldBlackList", (Object[])new Object[0]);
 985       }
 986   
```

**THROW_BIZ_EXCEPTION** · HRBizCertSignHelper L27
```java
  25           HRCertUtils.isAllowedClassName(allowedClassNameSet);
  26           if (datas == null || salts == null) {
  27 >             throw new KDBizException("the input datas or salts is null");
  28           }
  29           if (datas.length != salts.length) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp -->

## ISV 扩展指引（基于 ChgRecordEffectOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void rollbackOperation(kd.bos.entity.plugin.args.RollbackOperationArgs)`
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRCertCacheManager L94
```java
  92   
  93           static int getUsedCertCount(String groupId) {
  94 >             String accountId = RequestContext.get().getAccountId();
  95               Map<String, String> certUsedCountMap = dataCenterGroupCertCountMap.get(accountId);
  96               if (certUsedCountMap == null) {
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
```

**READ_VIA_HELPER** · HRCertCacheManager L70
```java
  68   
  69       static int getPurchaseCount(String groupId) {
  70 >         int totalNumber = LicenseServiceHelper.getTotalNumber((Object)groupId);
  71           logger.info("HRCertCacheManager.getPurchaseCount groupId : {},totalNumber:{}", (Object)groupId, (Object)totalNumber);
  72           return totalNumber;
```

**CALL_CROSS_SERVICE** · ChgUtils L984
```java
 982   
 983       public static String getPrivacyPropJson() {
 984 >         return (String)HRMServiceHelper.invokeHRMPService((String)"hrpi", (String)"IHRPIDevParamConfigService", (String)"queryChgRecordFieldBlackList", (Object[])new Object[0]);
 985       }
 986   
```

**THROW_BIZ_EXCEPTION** · HRBizCertSignHelper L27
```java
  25           HRCertUtils.isAllowedClassName(allowedClassNameSet);
  26           if (datas == null || salts == null) {
  27 >             throw new KDBizException("the input datas or salts is null");
  28           }
  29           if (datas.length != salts.length) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp -->

## ISV 扩展指引（基于 ChgRecordDiscardOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp/`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp -->

## ISV 扩展指引（基于 ChgRecordAfterEffectOp 真实证）

> FQN: `kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void rollbackOperation(kd.bos.entity.plugin.args.RollbackOperationArgs)`
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp -->
