# 扩展点全图 · hrcs_activityins

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrcs_activityins` 标品接入 **5** 个插件。

### FormPlugin（2）
- `HRCertCheckEdit` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
- `ActivityInstancePlugin` ← `kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin`

### ListPlugin（1）
- `HRCertCheckList` ← `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`

### OpPlugin（2）
- `ActivityInsSaveOp` ← `kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp`
- `ActivityInsAssigntoOp` ← `kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin -->

## ISV 扩展指引（基于 ActivityInstancePlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `closedCallBack`, `beforeDoOperation`, `afterDoOperation`, `setFilter`

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void hyperLinkClick(kd.bos.form.events.HyperLinkClickEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ActivityInstancePlugin L113
```java
 111               return;
 112           }
 113 >         QFilter qfilter = new QFilter("activityins.id", "=", (Object)actInsId);
 114           this.getView().showForm((FormShowParameter)ActivityBillFormUtil.assembleShowListFormParam((String)HRCS_ASSIGNREC_PAGE_KEY, null, (CloseCallBack)new CloseCallBack((IFormPlugin)this, HRCS_ASSIGNREC_PAGE_KEY), (ShowType)ShowType.Modal, (QFilter)qfilter));
 115       }
```

**READ_VIA_HELPER** · ActivityInstancePlugin L130
```java
 128           FormOperate source = (FormOperate)args.getSource();
 129           if (OP_SHOW_LOG.equals(source.getOperateKey()) && !listSelectedData.isEmpty()) {
 130 >             DynamicObject activityIns = ActivityInsServiceHelper.getActivityInsById((String)"wfnode,wfprocessinsid", (Long)((Long)listSelectedData.get(0).getPrimaryKeyValue()));
 131               if (activityIns != null) {
 132                   ListShowParameter listShowParameter = new ListShowParameter();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp -->

## ISV 扩展指引（基于 ActivityInsSaveOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · ActivityInsSaveOp L71
```java
  69           int halfLen = (int)Math.ceil((double)items.length / 2.0);
  70           this.recInfoArr = new ArrayList<DynamicObject>(halfLen);
  71 >         Long currentUserId = RequestContext.get().getCurrUserId();
  72           HRBaseServiceHelper insHelper = new HRBaseServiceHelper(ENTITYNAME_INS);
  73           HashMap actIns = Maps.newHashMapWithExpectedSize((int)items.length);
```

**QUERY_BUILDER** · ActivityInsSaveOp L79
```java
  77               actIns.put(item.getLong("activityins"), item);
  78           });
  79 >         Arrays.stream(insHelper.query("id,handlers.fbasedataid", new QFilter[]{new QFilter("id", "in", actIns.keySet())})).forEach(actInsInDb -> {
  80               DynamicObject item = (DynamicObject)actIns.get(actInsInDb.getLong("id"));
  81               HRBaseServiceHelper recHelper = new HRBaseServiceHelper(ENTITYNAME_REC);
```

**READ_VIA_HELPER** · ActivityInsSaveOp L82
```java
  80               DynamicObject item = (DynamicObject)actIns.get(actInsInDb.getLong("id"));
  81               HRBaseServiceHelper recHelper = new HRBaseServiceHelper(ENTITYNAME_REC);
  82 >             DynamicObject actTransRec = BusinessDataServiceHelper.newDynamicObject((String)ENTITYNAME_REC);
  83               DynamicObjectCollection oriHandlerColl = actTransRec.getDynamicObjectCollection("mulhandlerori");
  84               DynamicObjectType oriDyType = oriHandlerColl.getDynamicObjectType();
```

**WRITE_VIA_HELPER** · ActivityInsSaveOp L202
```java
 200               return actTransRec;
 201           }).toArray(DynamicObject[]::new);
 202 >         OperationServiceHelper.executeOperate((String)"submit", (String)ENTITYNAME_REC, (DynamicObject[])result, (OperateOption)OperateOption.create());
 203       }
 204   }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp -->

## ISV 扩展指引（基于 ActivityInsAssigntoOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp -->
