# 扩展点全图 · hrobs_carddatasetinfo

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（标品插件链 + curated_sdk 9 桶 · v2）
> **数据源**: `_auto_plugin_registry.md` · `curated_sdk.json`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品插件清单（反编译实抓 FQN）

主表 `hrobs_carddatasetinfo` 标品接入 **11** 个插件。

### FormPlugin（3）
- `CodeRulePlugin` ← `kd.bos.form.plugin.CodeRulePlugin`
- `CardDataSetInfoFormPlugin` ← `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin`
- `BdVersionSaveServicePlugin` ← `kd.bos.base.bdversion.BdVersionSaveServicePlugin`

### ListPlugin（3）
- `BdVersionListPlugin` ← `kd.bos.base.bdversion.BdVersionListPlugin`
- `BaseDataNameVersionListPlugin` ← `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin`
- `CardDataSetInfoListPlugin` ← `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin`

### OpPlugin（4）
- `CodeRuleOp` ← `kd.bos.business.plugin.CodeRuleOp`
- `CardDataSetInfoSaveOp` ← `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp`
- `CodeRuleDeleteOp` ← `kd.bos.coderule.CodeRuleDeleteOp`
- `CardDataSetInfoDeleteOp` ← `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp`

### 其他（1）
- `templatebaseedit` ← `dev.tpl.base.kd.bos.form.plugin.templatebaseedit`

## 🚨 ISV 扩展铁律（必看）

- 只能继承/调用带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 `kd.*` 类（详见 `cosmic_sdk_annotation_whitelist.md`）
- 禁继承类参见本 scene 的 `rules_chain_all.json::opKeys.*.mines[]`
- 修改标品元数据要先建 ISV 扩展元数据（详见 `isv_ownership_redline.md`）

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin -->

## ISV 扩展指引（基于 CardDataSetInfoFormPlugin 真实证）

> FQN: `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.base.AbstractBasePlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `beforeF7Select`, `propertyChanged`, `afterLoadData`, `click`, `closedCallBack`, `beforeDoOperation`, `afterDoOperation`, `beforeClosed`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCopyData(java.util.EventObject)`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeClick(kd.bos.form.control.events.BeforeClickEvent)`
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · CardDataSetInfoFormPlugin L114
```java
 112           Map<String, String> formIdMap = this.getFormIdMap();
 113           String binddataFormId = formIdMap.get("binddataFormId");
 114 >         DynamicObject dynamicObject = QueryServiceHelper.queryOne((String)binddataFormId, (String)"id,name", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)Long.valueOf(binddatainfo))});
 115           if (dynamicObject != null) {
 116               this.getModel().setValue("basedatainfotext", (Object)dynamicObject.getString("name"));
```

**READ_VIA_HELPER** · CardDataSetInfoFormPlugin L114
```java
 112           Map<String, String> formIdMap = this.getFormIdMap();
 113           String binddataFormId = formIdMap.get("binddataFormId");
 114 >         DynamicObject dynamicObject = QueryServiceHelper.queryOne((String)binddataFormId, (String)"id,name", (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)Long.valueOf(binddatainfo))});
 115           if (dynamicObject != null) {
 116               this.getModel().setValue("basedatainfotext", (Object)dynamicObject.getString("name"));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin -->

## ISV 扩展指引（基于 CardDataSetInfoListPlugin 真实证）

> FQN: `kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.list.plugin.AbstractListPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `setFilter`, `beforePackageData`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · CardDataSetConfigRepository L37
```java
  35       public DynamicObject queryBaseOne(Long id) {
  36           String selectProperties = "id,number,name,wbgroup,dstype,binddatainfo,enable,status,issyspreset";
  37 >         return QueryServiceHelper.queryOne((String)ENTITY_NAME, (String)selectProperties, (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)id)});
  38       }
  39   
```

**READ_VIA_HELPER** · CardDataSetConfigRepository L37
```java
  35       public DynamicObject queryBaseOne(Long id) {
  36           String selectProperties = "id,number,name,wbgroup,dstype,binddatainfo,enable,status,issyspreset";
  37 >         return QueryServiceHelper.queryOne((String)ENTITY_NAME, (String)selectProperties, (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)id)});
  38       }
  39   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.formplugin.workbench.datasetconfig.CardDataSetInfoListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp -->

## ISV 扩展指引（基于 CardDataSetInfoSaveOp 真实证）

> FQN: `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp -->

## ISV 扩展指引（基于 CardDataSetInfoDeleteOp 真实证）

> FQN: `kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · CardDataSetConfigRepository L37
```java
  35       public DynamicObject queryBaseOne(Long id) {
  36           String selectProperties = "id,number,name,wbgroup,dstype,binddatainfo,enable,status,issyspreset";
  37 >         return QueryServiceHelper.queryOne((String)ENTITY_NAME, (String)selectProperties, (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)id)});
  38       }
  39   
```

**READ_VIA_HELPER** · CardDataSetConfigRepository L37
```java
  35       public DynamicObject queryBaseOne(Long id) {
  36           String selectProperties = "id,number,name,wbgroup,dstype,binddatainfo,enable,status,issyspreset";
  37 >         return QueryServiceHelper.queryOne((String)ENTITY_NAME, (String)selectProperties, (QFilter[])new QFilter[]{new QFilter("id", "=", (Object)id)});
  38       }
  39   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hrobs.opplugin.workbench.carddatasetinfo.CardDataSetInfoDeleteOp -->
