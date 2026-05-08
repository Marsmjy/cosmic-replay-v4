# 扩展点全图 · 员工个税信息

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 所有标品插件清单

📌 详见 [_auto_plugin_registry.md](_auto_plugin_registry.md) —— 每次重跑 probe 会覆盖

**关键统计**：

- 插件总数: **7**（含跨 form 重复）
- 已解出生命周期方法数: **5**

## ✅ verified · 按生命周期方法分组

见 [_auto_plugin_registry.md](_auto_plugin_registry.md) 的 **按生命周期方法分组** 段

执行顺序规则：**同一方法，按插件注册顺序依次调用**

## 🟡 likely · ISV 最常覆盖的扩展点

<TODO> 专家补充 Top 3 最常覆盖：

1. `<classname>` — 覆盖原因: <TODO>
2. `<classname>` — 覆盖原因: <TODO>
3. `<classname>` — 覆盖原因: <TODO>

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
