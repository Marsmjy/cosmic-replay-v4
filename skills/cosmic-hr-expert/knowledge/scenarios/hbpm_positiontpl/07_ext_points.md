# 扩展点全图 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified

---

## 一、FormPlugin 扩展点

| 生命周期 | 触发位置 | 标品实现 | ISV 扩展指引 |
|---|---|---|---|
| `preOpenForm` | 编辑视图打开前 | 设置标题（新增/查看-岗位模板-{name}）| 自建 FormPlugin extends HRDataBaseEdit · 列挂 |
| `afterCreateNewData` | 新建数据创建后 | 填 org + setModifyStrategy | 同上 |
| `beforeBindData` | 数据绑定前 | viewStatus 三态 + setModifyStrategy | 同上 |
| `afterBindData` | 数据绑定后 | JobLevelGradeRangeUtil + setFiedRangeVisable | 同上 |
| `beforeDoOperation` | 操作前 | BU 开关校验 + modify 前校验 + save 数据变更校验 | 同上 |
| `afterDoOperation` | 操作后 | 名称/排序号唯一校验错误显示 + 状态切换 | 同上 |
| `propertyChanged` | 字段值变更 | org 变更 → setModifyStrategy | 同上 |
| `click` | 按钮点击 | JobLevelGradeRangeUtil.click | 同上 |
| `closedCallBack` | 弹窗关闭回调 | JobLevelGradeRangeUtil.closedCallBack | 同上 |
| `beforeItemClick`（List）| 列表点击操作前 | 禁用不可设适用范围 | 自建 ListPlugin extends HRDataBaseList · 列挂 |
| `beforeDoOperation`（List）| 列表操作前 | 新建前 BU 校验 | 同上 |
| `afterDoOperation`（List）| 列表操作后 | donothing_setscope → 打开适用范围弹窗 | 同上 |
| `closedCallBack`（List）| 弹窗关闭回调 | 适用范围设置回调 refresh | 同上 |
| `beforeShowBill` | 打开详情前 | 设置为 VIEW 态 | 同上 |
| `filterColumnSetFilter` | 列表过滤 | BU 权限过滤（PositionTplBuListPlugin）| 自建 ListPlugin extends HRDataBaseList |
| `filterContainerBeforeF7Select` | 过滤 F7 弹窗前 | BU 权限过滤 | 同上 |
| `filterContainerInit` | 过滤器初始化 | 清空 org.name 默认值 | 同上 |

---

## 二、OP 扩展点

| opKey | 标品 OP | 关键方法 | ISV 扩展指引 |
|---|---|---|---|
| `save` / `modify` | `PositionTplSaveOp` | beforeExecuteOpTx + endOpTx + afterExecuteOpTx | 自建 OP extends HRDataBaseOp · 列挂 · 排在标品之后 |
| `disable` | （平台标准）| — | 自建 OP onAddValidators 注册前置校验器 |
| `delete` | （平台标准）| — | 同上 |
| `donothing_setscope` | （平台标准 donothing）| — | 列表 afterDoOperation 实现后续逻辑 |

---

## 三、可注入自定义业务字段

| 字段位置 | 操作 | 注意 |
|---|---|---|
| `hbpm_positiontpl` 主实体 | modifyMeta op=add | 前缀必须 ISV 标识 · key≤24字符 |
| 无标准 entry 子表 | 若需要子表 → 新建 ISV 扩展实体 | — |

---

## 四、禁止扩展的危险点

| 禁止操作 | 原因 |
|---|---|
| 继承 4 个场景专属类 | PR-001 · 标品升级方法签名变 → ISV 编译失败 |
| 手改 `enable` 字段（绕过 OP）| 绕过禁用前 R-2 校验 → 数据不一致 |
| 在 endOperationTransaction 中做耗时操作 | 同事务 · 失败导致整个保存回滚 |
| 重复实现 BU 组织过滤（PositionTplBuListPlugin 已实现）| 冲突 · 双重过滤 |
| 直接改 `posttpltype` 属性（modifyMeta）| 上游字典关联 · ISV 不可改其元数据属性 |

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin -->

## ISV 扩展指引（基于 PositionTplEditPlugin 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterCreateNewData`, `registerListener`, `beforeDoOperation`, `click`, `beforeBindData`, `afterBindData`, `afterDoOperation`, `propertyChanged`, `preOpenForm`, `closedCallBack`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · PositionTplRepository L36
```java
  34   
  35       public DynamicObject[] queryPositionTplNameById(List<Long> ids) {
  36 >         QFilter idFilter = new QFilter("id", "in", ids);
  37           return serviceHelper.query("name", new QFilter[]{idFilter});
  38       }
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

**CALL_CROSS_SERVICE** · PositionTplEditPlugin L162
```java
 160                   args.setCancel(true);
 161               }
 162 >             if ((openTplChangePos = parameterData.getOrDefault("positiontplchangepos", false).booleanValue()) && HRStringUtils.isNotEmpty((String)(tips = (String)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"getContent", (Object[])new Object[]{2000164510891009024L})))) {
 163                   this.getView().showTipNotification(tips, Integer.valueOf(10000));
 164               }
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin -->

## ISV 扩展指引（基于 PositionTplListPlugin 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `click`, `beforeItemClick`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PositionPermissionUtil L41
```java
  39   
  40       public static HasPermOrgResult getPosHasPermOrg() {
  41 >         return PositionPermissionUtil.getPosHasPermOrg(RequestContext.get().getCurrUserId(), APPID, ENTITY_NAME, "47150e89000000ac");
  42       }
  43   
```

**QUERY_BUILDER** · PositionTplRepository L36
```java
  34   
  35       public DynamicObject[] queryPositionTplNameById(List<Long> ids) {
  36 >         QFilter idFilter = new QFilter("id", "in", ids);
  37           return serviceHelper.query("name", new QFilter[]{idFilter});
  38       }
```

**READ_VIA_HELPER** · PositionTplRepository L66
```java
  64   
  65       public Set<String> getTplAllFields() {
  66 >         MainEntityType entityType = MetadataServiceHelper.getDataEntityType((String)"hbpm_positiontpl");
  67           Map entityTypeMap = entityType.getAllEntities();
  68           return ((EntityType)entityTypeMap.get("hbpm_positiontpl")).getFields().keySet();
```

**CALL_CROSS_SERVICE** · PositionPermissionUtil L29
```java
  27   
  28       public static AuthorizedOrgResult getHasPermAdminOrg(long userId, String appId) {
  29 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, ENTITY_NAME, "47150e89000000ac", PROP_KEY});
  30       }
  31   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin -->

## ISV 扩展指引（基于 PositionTplBuListPlugin 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`

### 可重写方法（target.java self）
- `public public java.util.Set<java.lang.String> getCtrlBUFieldSet()`
- `public public kd.bos.permission.api.HasPermOrgResult getPermOrgResult()`
- `public public void filterColumnSetFilter(kd.bos.form.events.SetFilterEvent)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public static public static kd.bos.orm.query.QFilter getAdminOrgBaseDataFilter(java.lang.String, java.util.List<java.lang.Long>)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PositionTplBuListPlugin L56
```java
  54               if (orgPermString == null) {
  55                   String appId = this.getView().getFormShowParameter().getCheckRightAppId();
  56 >                 this.hasPermOrgResult = PermissionServiceHelper.getAllPermOrgs((long)RequestContext.get().getCurrUserId(), (String)"21", (String)appId, (String)"hbpm_positiontpl", (String)"47150e89000000ac");
  57                   this.getPageCache().put(ORG_PERM_CACHE_KEY, ((HasPermOrgResultImpl)this.hasPermOrgResult).toSerializeStr());
  58               } else {
```

**QUERY_BUILDER** · PositionTplBuListPlugin L134
```java
 132       public static QFilter getAdminOrgBaseDataFilter(String entityName, List<Long> orgIdList) {
 133           if (CollectionUtils.isEmpty(orgIdList)) {
 134 >             return new QFilter("1", "!=", (Object)1);
 135           }
 136           return new QFilter("id", "in", orgIdList);
```

**READ_VIA_HELPER** · PositionTplBuListPlugin L56
```java
  54               if (orgPermString == null) {
  55                   String appId = this.getView().getFormShowParameter().getCheckRightAppId();
  56 >                 this.hasPermOrgResult = PermissionServiceHelper.getAllPermOrgs((long)RequestContext.get().getCurrUserId(), (String)"21", (String)appId, (String)"hbpm_positiontpl", (String)"47150e89000000ac");
  57                   this.getPageCache().put(ORG_PERM_CACHE_KEY, ((HasPermOrgResultImpl)this.hasPermOrgResult).toSerializeStr());
  58               } else {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp -->

## ISV 扩展指引（基于 PositionTplSaveOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `endOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp -->
