# 扩展点全图 · hrcs_querydynsourcelist

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrcs_querydynsourcelist` 标品接入 **4** 个插件。

### FormPlugin（1）
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`

### ListPlugin（2）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
- `HRQueryTreeListPlugin` ← `kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin`

### OpPlugin（1）
- `HRQueryListOp` ← `kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin -->

## ISV 扩展指引（基于 HRQueryTreeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.designer.query.QueryTreeListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeItemClick`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void refreshNode(kd.bos.form.control.events.RefreshNodeEvent)`
- `public public void initializeTree(java.util.EventObject)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRQueryTreeListPlugin L334
```java
 332           Iterator<TreeNode> cloudIterator = cloudNodes.iterator();
 333           Set<String> disabledAppIds = this.getDisabledAppIds();
 334 >         AdminAppResult adminAppResult = PermissionServiceHelper.getAdminApps((Long)RequestContext.get().getCurrUserId(), (boolean)true);
 335           logger.debug("Appid is diabled : {}, needAdminAppRange : {}, rangeApp size : {}", new Object[]{disabledAppIds, adminAppResult.needAdminAppRange(), adminAppResult.getAppIds() == null ? "null" : Integer.valueOf(adminAppResult.getAppIds().size())});
 336           while (cloudIterator.hasNext()) {
```

**QUERY_BUILDER** · EntityReleaseInfoService L43
```java
  41       public static DynamicObject getEntityReleaseInfoByName(String queryEntityName) {
  42           Map<String, String> srcAndExtNumMap = EntityReleaseInfoService.querySrcAndExtNum(queryEntityName);
  43 >         QFilter filterQueryEntityName = new QFilter("queryentityname", "in", srcAndExtNumMap.values());
  44           DynamicObject[] dyReleaseInfoList = serviceHelper.query("queryentityname,modifytime,datasourcetype,ksqlquerytype,ksqluseunion,bizapplytype", new QFilter[]{filterQueryEntityName});
  45           if (dyReleaseInfoList == null || dyReleaseInfoList.length == 0) {
```

**READ_VIA_HELPER** · EntityReleaseInfoService L125
```java
 123       public static List<String> findExtentQueryentityId() {
 124           QFilter[] qFilters = new QFilter[]{new QFilter("modeltype", "=", (Object)"QueryListModel"), new QFilter("inheritpath", "!=", (Object)" ")};
 125 >         DynamicObject[] collection = BusinessDataServiceHelper.load((String)"bos_formmeta", (String)"id", (QFilter[])qFilters);
 126           return Arrays.asList(collection).stream().map(de -> de.getString("id")).collect(Collectors.toList());
 127       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp -->

## ISV 扩展指引（基于 HRQueryListOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · EntityReleaseInfoService L43
```java
  41       public static DynamicObject getEntityReleaseInfoByName(String queryEntityName) {
  42           Map<String, String> srcAndExtNumMap = EntityReleaseInfoService.querySrcAndExtNum(queryEntityName);
  43 >         QFilter filterQueryEntityName = new QFilter("queryentityname", "in", srcAndExtNumMap.values());
  44           DynamicObject[] dyReleaseInfoList = serviceHelper.query("queryentityname,modifytime,datasourcetype,ksqlquerytype,ksqluseunion,bizapplytype", new QFilter[]{filterQueryEntityName});
  45           if (dyReleaseInfoList == null || dyReleaseInfoList.length == 0) {
```

**READ_VIA_HELPER** · EntityReleaseInfoService L125
```java
 123       public static List<String> findExtentQueryentityId() {
 124           QFilter[] qFilters = new QFilter[]{new QFilter("modeltype", "=", (Object)"QueryListModel"), new QFilter("inheritpath", "!=", (Object)" ")};
 125 >         DynamicObject[] collection = BusinessDataServiceHelper.load((String)"bos_formmeta", (String)"id", (QFilter[])qFilters);
 126           return Arrays.asList(collection).stream().map(de -> de.getString("id")).collect(Collectors.toList());
 127       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp -->
