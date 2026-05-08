# 扩展点全图 · 架构类型（haos_structtype）

> **状态**: 🟢 基于反编译 7 类实证
> **confidence**: verified

---

## 一、FormPlugin 扩展点

| 生命周期 | 触发位置 | 标品实现 | ISV 扩展指引 |
|---|---|---|---|
| `preOpenForm` | 编辑视图打开前 | 设置标题（新增/编辑）| 自建 FormPlugin extends HRDataBaseEdit · 列挂 |
| `afterCreateNewData` | 新建数据创建后 | 填 org（customParam 或权限查询）| 同上 |
| `beforeBindData` | 数据绑定前 | R-1 effdt 启用控制 + setTips | 同上 · 可追加字段控制 |
| `afterDoOperation` | 操作执行后 | 保存成功后关窗通知 | 同上 · 可追加后置逻辑 |
| `beforeItemClick` | 列表点击操作前 | R-2/R-3 chgname 校验 + tbldeleteallrel 确认弹窗 | 自建 ListPlugin extends HRDataBaseList · 列挂 |
| `afterDoOperation`（List）| 列表操作后 | chgname → 打开改名弹窗 | 同上 |
| `closedCallBack` | 弹窗关闭回调 | haos_structtypenamechg 回调处理 | 同上 |
| `confirmCallBack` | 确认弹窗回调 | tbldeleteallrel 确认后执行删除 | 同上 |

---

## 二、OP 扩展点

| opKey | 标品 OP | 关键方法 | ISV 扩展指引 |
|---|---|---|---|
| `save` | `StructTypeSaveOp` | beginOpTx + afterExecuteOpTx | 自建 OP extends HRDataBaseOp · 列挂 · 排在标品之后 |
| `disable` | `StructTypeDisableOp` | beginOpTx（级联禁用）| 同上 · 可在之前加前置校验 |
| `enable` | `StructTypeEnableOp` | beginOpTx + afterExecuteOpTx | 同上 |
| `tbldeleteallrel`/`deleteallrel` | `StructTypeDeleteDonothingOp` | onAddValidators + beforeExecuteOpTx | 自建 OP onAddValidators 注册自定义校验器 · 排在标品之前 |
| `chgname` | `StructTypeChgNameOp` | afterExecuteOpTx | 同上 · 可追加后置逻辑 |

---

## 三、可注入自定义业务字段

| 字段位置 | 操作 | 注意 |
|---|---|---|
| `haos_structtype` 主实体 | modifyMeta op=add | 前缀必须 ISV 标识 · key≤24字符 · OP 层需 onPreparePropertys 声明 |
| 无 entry 子表 | 不可加子表（当前无子表结构）| 若需要子表 → 新建 ISV 扩展实体 |

---

## 四、禁止扩展的危险点

| 禁止操作 | 原因 |
|---|---|
| 继承 7 个场景专属类 | PR-001 · 标品升级方法签名变 → ISV 编译失败 |
| 手改 `metanumsuffix` 字段 | 改变后所有关联元数据/菜单/规则全部失效 |
| 手改 `enable` 字段（绕过 OP）| 绕过级联禁用逻辑（R-6）→ 数据不一致 |
| 直接删 bos_entityobject | 应走标品 tbldeleteallrel 操作 |
| 在 afterExecuteOpTx 新事务外做耗时操作 | 新事务（TX.requiresNew）内失败会 rollback |

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin -->

## ISV 扩展指引（基于 StructTypeEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `afterCreateNewData`, `beforeBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · OrgPermHelper L35
```java
  33   
  34       public static HasPermOrgResult getHRPermOrg() {
  35 >         return OrgPermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, "haos_adminorgdetail", "47150e89000000ac");
  36       }
  37   
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · StructTypeHelper L45
```java
  43           for (String controlName : controlNames) {
  44               Tips tips = new Tips();
  45 >             List tipList = HRCSRPCServiceHelper.queryPromptForString((IFormView)iFormView, (String)pageName, (String)controlName);
  46               if (CollectionUtils.isEmpty((Collection)tipList)) continue;
  47               tips.setContent(new LocaleString((String)tipList.get(0)));
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin -->

## ISV 扩展指引（基于 StructTypeListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeItemClick`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.otherstruct.structtype.StructTypeListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp -->

## ISV 扩展指引（基于 StructTypeSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StructClassHelper L452
```java
 450           structProject.set("rootnumber", (Object)structProjectDy.getString("number"));
 451           structProject.set("rootname", (Object)structProjectDy.getString("name"));
 452 >         structProject.set("creator", (Object)UserServiceHelper.getCurrentUserId());
 453           structProject.set("index", (Object)1);
 454           structProject.set("status", (Object)"C");
```

**QUERY_BUILDER** · QFilterHelper L17
```java
  15   public class QFilterHelper {
  16       public static QFilter createInitFinishedFilter() {
  17 >         return new QFilter("initstatus", "=", (Object)"2");
  18       }
  19   
```

**READ_VIA_HELPER** · GenMetaDataHelper L265
```java
 263       public static void createAppMenuAndSave(AppMenuInfo appMenuInfo, boolean handleDel) {
 264           String bizAppId = appMenuInfo.getBizAppId();
 265 >         AppMetadata metadata = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)bizAppId, (boolean)false);
 266           List appMenus = metadata.getAppMenus();
 267           AppMenuUtil.buildMenus((List)appMenus, (AppMenuInfo)appMenuInfo, (String)bizAppId);
```

**WRITE_VIA_HELPER** · GenMetaDataHelper L183
```java
 181           dObject.set("bizunit", (Object)bizUnitId);
 182           dObject.set("form", (Object)formId);
 183 >         SaveServiceHelper.save((DynamicObject[])new DynamicObject[]{dObject});
 184           AppUtils.addLog((String)"bos_formmeta", (String)ResManager.loadKDString((String)"\u4fdd\u5b58", (String)"GenMetaDataHelper_0", (String)"hrmp-haos-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u4fdd\u5b58\u8868\u5355\u548c\u529f\u80fd\u5206\u7ec4\u7684\u5173\u8054\u5173\u7cfb", (String)"GenMetaDataHelper_2", (String)"hrmp-haos-business", (Object[])new Object[0]));
 185           message.put("formid", formId);
```

**THROW_BIZ_EXCEPTION** · GenMetaDataHelper L175
```java
 173               }
 174               log.error("\u521b\u5efa\u5931\u8d25\u539f\u56e0\uff1a" + saveResult);
 175 >             throw new KDBizException(message.get("message").toString());
 176           }
 177           String formId = (String)content.get("id");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp -->

## ISV 扩展指引（基于 StructTypeDisableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp -->

## ISV 扩展指引（基于 StructTypeEnableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp/`

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

**CONTEXT_ACCESS** · StructClassHelper L452
```java
 450           structProject.set("rootnumber", (Object)structProjectDy.getString("number"));
 451           structProject.set("rootname", (Object)structProjectDy.getString("name"));
 452 >         structProject.set("creator", (Object)UserServiceHelper.getCurrentUserId());
 453           structProject.set("index", (Object)1);
 454           structProject.set("status", (Object)"C");
```

**QUERY_BUILDER** · QFilterHelper L17
```java
  15   public class QFilterHelper {
  16       public static QFilter createInitFinishedFilter() {
  17 >         return new QFilter("initstatus", "=", (Object)"2");
  18       }
  19   
```

**READ_VIA_HELPER** · GenMetaDataHelper L265
```java
 263       public static void createAppMenuAndSave(AppMenuInfo appMenuInfo, boolean handleDel) {
 264           String bizAppId = appMenuInfo.getBizAppId();
 265 >         AppMetadata metadata = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)bizAppId, (boolean)false);
 266           List appMenus = metadata.getAppMenus();
 267           AppMenuUtil.buildMenus((List)appMenus, (AppMenuInfo)appMenuInfo, (String)bizAppId);
```

**WRITE_VIA_HELPER** · GenMetaDataHelper L183
```java
 181           dObject.set("bizunit", (Object)bizUnitId);
 182           dObject.set("form", (Object)formId);
 183 >         SaveServiceHelper.save((DynamicObject[])new DynamicObject[]{dObject});
 184           AppUtils.addLog((String)"bos_formmeta", (String)ResManager.loadKDString((String)"\u4fdd\u5b58", (String)"GenMetaDataHelper_0", (String)"hrmp-haos-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u4fdd\u5b58\u8868\u5355\u548c\u529f\u80fd\u5206\u7ec4\u7684\u5173\u8054\u5173\u7cfb", (String)"GenMetaDataHelper_2", (String)"hrmp-haos-business", (Object[])new Object[0]));
 185           message.put("formid", formId);
```

**THROW_BIZ_EXCEPTION** · GenMetaDataHelper L175
```java
 173               }
 174               log.error("\u521b\u5efa\u5931\u8d25\u539f\u56e0\uff1a" + saveResult);
 175 >             throw new KDBizException(message.get("message").toString());
 176           }
 177           String formId = (String)content.get("id");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp -->

## ISV 扩展指引（基于 StructTypeDeleteDonothingOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StructClassHelper L452
```java
 450           structProject.set("rootnumber", (Object)structProjectDy.getString("number"));
 451           structProject.set("rootname", (Object)structProjectDy.getString("name"));
 452 >         structProject.set("creator", (Object)UserServiceHelper.getCurrentUserId());
 453           structProject.set("index", (Object)1);
 454           structProject.set("status", (Object)"C");
```

**QUERY_BUILDER** · QFilterHelper L17
```java
  15   public class QFilterHelper {
  16       public static QFilter createInitFinishedFilter() {
  17 >         return new QFilter("initstatus", "=", (Object)"2");
  18       }
  19   
```

**READ_VIA_HELPER** · GenMetaDataHelper L265
```java
 263       public static void createAppMenuAndSave(AppMenuInfo appMenuInfo, boolean handleDel) {
 264           String bizAppId = appMenuInfo.getBizAppId();
 265 >         AppMetadata metadata = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)bizAppId, (boolean)false);
 266           List appMenus = metadata.getAppMenus();
 267           AppMenuUtil.buildMenus((List)appMenus, (AppMenuInfo)appMenuInfo, (String)bizAppId);
```

**WRITE_VIA_HELPER** · GenMetaDataHelper L183
```java
 181           dObject.set("bizunit", (Object)bizUnitId);
 182           dObject.set("form", (Object)formId);
 183 >         SaveServiceHelper.save((DynamicObject[])new DynamicObject[]{dObject});
 184           AppUtils.addLog((String)"bos_formmeta", (String)ResManager.loadKDString((String)"\u4fdd\u5b58", (String)"GenMetaDataHelper_0", (String)"hrmp-haos-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u4fdd\u5b58\u8868\u5355\u548c\u529f\u80fd\u5206\u7ec4\u7684\u5173\u8054\u5173\u7cfb", (String)"GenMetaDataHelper_2", (String)"hrmp-haos-business", (Object[])new Object[0]));
 185           message.put("formid", formId);
```

**THROW_BIZ_EXCEPTION** · GenMetaDataHelper L175
```java
 173               }
 174               log.error("\u521b\u5efa\u5931\u8d25\u539f\u56e0\uff1a" + saveResult);
 175 >             throw new KDBizException(message.get("message").toString());
 176           }
 177           String formId = (String)content.get("id");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeDeleteDonothingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp -->

## ISV 扩展指引（基于 StructTypeChgNameOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StructClassHelper L452
```java
 450           structProject.set("rootnumber", (Object)structProjectDy.getString("number"));
 451           structProject.set("rootname", (Object)structProjectDy.getString("name"));
 452 >         structProject.set("creator", (Object)UserServiceHelper.getCurrentUserId());
 453           structProject.set("index", (Object)1);
 454           structProject.set("status", (Object)"C");
```

**QUERY_BUILDER** · QFilterHelper L17
```java
  15   public class QFilterHelper {
  16       public static QFilter createInitFinishedFilter() {
  17 >         return new QFilter("initstatus", "=", (Object)"2");
  18       }
  19   
```

**READ_VIA_HELPER** · GenMetaDataHelper L265
```java
 263       public static void createAppMenuAndSave(AppMenuInfo appMenuInfo, boolean handleDel) {
 264           String bizAppId = appMenuInfo.getBizAppId();
 265 >         AppMetadata metadata = AppMetaServiceHelper.loadAppMetadataFromCacheById((String)bizAppId, (boolean)false);
 266           List appMenus = metadata.getAppMenus();
 267           AppMenuUtil.buildMenus((List)appMenus, (AppMenuInfo)appMenuInfo, (String)bizAppId);
```

**WRITE_VIA_HELPER** · GenMetaDataHelper L183
```java
 181           dObject.set("bizunit", (Object)bizUnitId);
 182           dObject.set("form", (Object)formId);
 183 >         SaveServiceHelper.save((DynamicObject[])new DynamicObject[]{dObject});
 184           AppUtils.addLog((String)"bos_formmeta", (String)ResManager.loadKDString((String)"\u4fdd\u5b58", (String)"GenMetaDataHelper_0", (String)"hrmp-haos-business", (Object[])new Object[0]), (String)ResManager.loadKDString((String)"\u4fdd\u5b58\u8868\u5355\u548c\u529f\u80fd\u5206\u7ec4\u7684\u5173\u8054\u5173\u7cfb", (String)"GenMetaDataHelper_2", (String)"hrmp-haos-business", (Object[])new Object[0]));
 185           message.put("formid", formId);
```

**THROW_BIZ_EXCEPTION** · GenMetaDataHelper L175
```java
 173               }
 174               log.error("\u521b\u5efa\u5931\u8d25\u539f\u56e0\uff1a" + saveResult);
 175 >             throw new KDBizException(message.get("message").toString());
 176           }
 177           String formId = (String)content.get("id");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherstruct.structtype.StructTypeChgNameOp -->
