# 扩展点全图 · 权限初始化

> **状态**: 🟢 已补全（2026-04-28 基于 20 插件 + 66 opKey 实证）
> **数据源**: `rules_chain_all.json` + `form_lifecycle_rules.json` + `curated_sdk.json`

## 🟢 标品插件全清单（20 插件）

### FormPlugin（表单插件 · 1 个）

| 插件 | 完整类名 | 父类 | 角色 |
|---|---|---|---|
| PermInitRecordEdit | `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit` | HRDataBaseEdit | 主表单：inittype 路由 / userimport-roleimport入口 / finishinit 五维校验 / reimport二次确认 / save 主键兜底 / beforeClosed脏数据清理 |

### ListPlugin（列表插件 · 1 个）

| 插件 | 完整类名 | 父类 | 角色 |
|---|---|---|---|
| PermInitRecordList | `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList` | HRDataBaseList | 列表：HR 管理员权限门禁 / inittype 过滤 / labelpolicy 跳转 / Session 智能激活 / 导出调度 / 按钮显隐 |

### OP 插件链（5 个 · 所有 opKey 共享同一执行链）

| 顺序 | 插件 | 完整类名 | 角色 |
|---|---|---|---|
| 1 | HRBaseDataStatusOp | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | 标品通用：状态控制（启用/禁用/数据状态） |
| 2 | HRBaseDataLogOp | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 标品通用：操作日志记录 |
| 3 | HRBaseDataEnableOp | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 标品通用：使用状态联动 |
| 4 | HRBaseOriginalOp | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | 标品通用：出厂数据保护 |
| 5 | PermInitRecordDeleteOp | `kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp` | 场景专属：delete 物理删除 / save 校验 / 事件发布 |

### 移动端插件（2 个）

| 插件 | 完整类名 | 角色 |
|---|---|---|
| mobtoolbarcancel | 标品移动端 | 取消操作 |
| mobtoolbarselect | 标品移动端 | 选择操作 |

### 其他共用插件（11 个 — HR 域准入/严格模式）

| 插件 | 完整类名 | 角色 |
|---|---|---|
| HRAdminStrictPlugin | `kd.hr.hrcs.formplugin.web.perm.init.HRAdminStrictPlugin` | HR 域管理员门禁（实现在 HRDynamicFormBasePlugin） |

## 🟢 生命周期方法全图（基于 PermInitRecordEdit 反编译）

| 方法 | 摘要 | ISV 可扩展 | ISV 扩展方式 |
|---|---|---|---|
| `beforeBindData` | inittype 双模路由 / 嵌入子表单 / 重画字段中文名 / dealstatus=1 隐藏 finishinit 按钮 | 是 | 继承 + super + afterBindData 追加 |
| `registerListener` | 为 userimport 按钮注册 ClickListener | 是 | 继承 + addClickListeners 追加自定义按钮 |
| `beforeDoOperation` | userimport/roleimport 入口校验（任务名必填+重名清理）+ 已有数据二次确认 + finishinit 五维校验 + save 主键 ORM.genLongId | 是 | 继承 + 在 super 前后追加自定义阻断 |
| `afterDoOperation` | finishinit 成功后调 UserPermInitConvertService.convertRecord / PermRoleInitService.initRole 落库 | 是 | 继承 + afterDoOperation 追加 ISV 下游写表 |
| `beforeClosed` | 自动清理 dealstatus=0（未完成）的脏数据 | 否（慎改） | 标品清理逻辑，覆盖会导致垃圾数据残留 |
| `propertyChanged` | 监听字段值变更（如 inittype 切换时联动分录显隐） | 是 | 继承 + super + 追加 |
| `closedCallBack` | 导入完成/子页面返回时刷新分录 + 联动子表单 | 是 | 继承 + closeCallBack 自定义 callBackId |

## 🟢 66 opKey 分类与 ISV 扩展值

### 导入导出类（11 个 · extValue=3 高价值）

| opKey | 名称 | opType |
|---|---|---|
| `importdata` | 导入数据 | importdata |
| `importdata_hr` | 导入数据 | importdata_hr |
| `importdetails` | 查看导入详情 | importdetails |
| `importtemplatelist` | 导入模板 | importtemplatelist |
| `importexport_userset` | 导入导出个性化设置 | importexport_userset |
| `export_from_list_hr` | 按列表导出 | export_from_list_hr |
| `export_from_impttpl_hr` | 按导入模板导出 | export_from_impttpl_hr |
| `export_from_expttpl_hr` | 按导出模板导出 | export_from_expttpl_hr |
| `exportlist` | 导出数据（按导入模板） | exportlist |
| `exportlist_expt` | 导出数据（按导出模板） | exportlist_expt |
| `exportlistbyselectfields` | 导出数据（按列表字段） | exportlist |

### 模板/示例下载类（4 个 · donothing 但 FormPlugin 拦截）

| opKey | 名称 | 实际行为（FormPlugin.beforeDoOperation 拦截） |
|---|---|---|
| `dlusertemp` | 下载用户权限模板 | 调 RecordExcelWriter 生成 userrole 模式的 Excel 模板 |
| `dlroletemp` | 下载角色模板 | 调 RoleRecordExcelWriter 生成 role 模式的 Excel 模板 |
| `dlusertip` | 下载用户权限示例 | 调 PermSheetHelper 生成 userrole 示例数据 Excel |
| `dlroletip` | 下载角色示例 | 调 PermSheetHelper 生成 role 示例数据 Excel |

### 核心业务操作（4 个 · donothing 但 FormPlugin 拦截）

| opKey | 名称 | 实际行为 |
|---|---|---|
| `finishinit` | 检测并完成初始化 | FormPlugin.beforeDoOperation 拦截：调 PermInitFinishValidateService(四维) / PermRoleInitFinishValidateService(五维) 校验 → 失败弹检查结果页 → 成功调 UserPermInitConvertService.convertRecord 或 PermRoleInitService.initRole |
| `userimport` | 导入用户权限 | FormPlugin.beforeDoOperation 拦截：清空 4 张 user 子分录 → 解析 Excel → 回填 userdimentry/userdataruleentry/userbdentry/userfieldentry |
| `roleimport` | 导入角色权限 | FormPlugin.beforeDoOperation 拦截：清空 5 张 role 子分录 → 解析 Excel → 回填 rolebaseentry/rolefuncentry/roledimentry/roledataentry/rolefieldentry |
| `inituserrole` | 初始化用户权限 | 生成 hrcs_permimportstart 单据 ShowParameter 打开导入源选择页面 |
| `initrole` | 角色权限初始化 | 生成 hrcs_rolepermimportstart 单据 ShowParameter 打开导入源选择页面 |

### 基础 CRUD（10 个）

| opKey | 名称 |
|---|---|
| `new` | 新增 |
| `modify` | 修改 |
| `save` | 保存（6 插件，比标准多一个） |
| `saveandnew` | 保存并新增 |
| `delete` | 删除（物理删除 + 标品垃圾清理） |
| `copy` | 复制 |
| `view` | 查看 |
| `close` | 关闭 |
| `refresh` | 刷新 |
| `option` | 选项设置 |

### 分录行操作（10 个）

| opKey | 作用分录 |
|---|---|
| `newentry` | 新增分录（通用） |
| `newfieldentry` | 新增字段分录 |
| `newdrentry` | 新增数据规则分录 |
| `newbdentry` | 新增基础资料分录 |
| `newdimvalentry` | 新增维度值分录 |
| `newfdentry` | 新增字段分录 |
| `deleteentry` | 删除分录（通用） |
| `deletefieldentry` | 删除字段分录 |
| `deletedrentry` | 删除数据规则分录 |
| `deletebdentry` | 删除基础资料分录 |
| `deletedimvalentry` | 删除维度值分录 |
| `deletebaseroleentry` | 删除角色基本信息分录 |
| `copybaseentryrow` | 复制角色基本信息记录 |

### 状态操作（4 个）

| opKey | 名称 |
|---|---|
| `enable` | 启用 |
| `disable` | 禁用 |
| `audit` | 审核 |
| `unaudit` | 反审核 |

### 导航类 + 日志（8 个）

| opKey | 名称 |
|---|---|
| `first` / `last` / `next` / `previous` | 导航（首/末/下/上） |
| `logview` | 查看全部日志 |
| `viewonelog` | 查看日志 |
| `namehistory` / `namehistoryview` | 编码历史/编码历史查询 |
| `returndata` | 返回数据 |

## 🟢 ISV 最常覆盖的扩展点 Top 5

1. **PermInitRecordEdit.beforeDoOperation** — 覆盖原因：finishinit / userimport / roleimport 是核心业务入口，ISV 要在完成初始化前加自定义校验或完成后调下游接口。

2. **PermInitRecordDeleteOp.onAddValidators** — 覆盖原因：save 前的字段级校验是最常见 ISV 需求（任务名规则 / 编码规则 / 自定义字段必填）。

3. **PermInitRecordEdit.afterBindData** — 覆盖原因：根据 inittype / dealstatus 动态控制自定义按钮/logic 显隐。

4. **PermInitRecordList.setFilter** — 覆盖原因：列表查询加 ISV 自定义过滤条件（如只看自己的任务）。

5. **PermInitRecordList.beforeShowBillForm** — 覆盖原因：从列表打开表单时传自定义 customParam（如指定 inittype=role）。

## 🟢 执行顺序规则

同一方法内，插件按注册顺序依次调用：
- OP链: HRBaseDataStatusOp → HRBaseDataLogOp → HRBaseDataEnableOp → HRBaseOriginalOp → PermInitRecordDeleteOp
- ISV 扩展插件注册在最后 → 执行在标品之后
- FormPlugin: 标品 PermInitRecordEdit 先执行 → ISV ext FormPlugin 后执行

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## ISV 扩展指引（基于 HRAdminStrictPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public static public static void showMesIfUserIsNotAdmin(kd.bos.form.events.PreOpenFormEventArgs)`
- `public static public static boolean isHrAdmin()`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRAdminService L23
```java
  21   public class HRAdminService {
  22       public static boolean isHrAdmin() {
  23 >         Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
```

**QUERY_BUILDER** · HRAdminService L24
```java
  22       public static boolean isHrAdmin() {
  23           Long userId = RequestContext.get().getCurrUserId();
  24 >         QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25           DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
```

**READ_VIA_HELPER** · HRAdminService L25
```java
  23           Long userId = RequestContext.get().getCurrUserId();
  24           QFilter[] filters = new QFilter[]{new QFilter("enable", "=", (Object)"1")};
  25 >         DynamicObjectCollection adminSchemes = QueryServiceHelper.query((String)"perm_adminscheme", (String)"id", (QFilter[])filters);
  26           Long adminScheme = (Long)((DynamicObject)adminSchemes.get(0)).get("id");
  27           filters = new QFilter[]{new QFilter("user.id", "=", (Object)userId).and("usergroup.adminscheme.id", "=", (Object)adminScheme).and("usergroup.isdomain", "=", (Object)"1").and("usergroup.domain", "=", (Object)1386267129346523136L).or("usergroup.id", "=", (Object)1393280986623636480L).and("user.id", "=", (Object)userId)};
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit -->

## ISV 扩展指引（基于 PermInitRecordEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `registerListener`, `afterDoOperation`, `beforeDoOperation`, `propertyChanged`, `beforeClosed`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
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

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
```

**READ_VIA_HELPER** · OrgServiceUtil L21
```java
  19   
  20       public static long getAdminRootOrgId() {
  21 >         return OrgUnitServiceHelper.getRootOrgId();
  22       }
  23   
```

**WRITE_VIA_HELPER** · UserPermInitConvertService L218
```java
 216               dynamicObject.set("id", (Object)recordId);
 217               dynamicObject.set("dealstatus", (Object)"1");
 218 >             SaveServiceHelper.update((DynamicObject)dynamicObject);
 219               HRPermCacheMgr.clearAllCache();
 220               LOGGER.info("ConvertPermInitRecord end,recordId:{}", (Object)recordId);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList -->

## ISV 扩展指引（基于 PermInitRecordList 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforeItemClick`, `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRRolePermHelper L55
```java
  53           fsp.getOpenStyle().setShowType(ShowType.Modal);
  54           fsp.setMultiSelect(false);
  55 >         Set<String> roleIds = HRRolePermHelper.queryViewableRoles(RequestContext.get().getCurrUserId());
  56           fsp.getListFilterParameter().setFilter(new QFilter("perm_role.enable", "=", (Object)"1").and("id", "in", roleIds).and("id", "not in", selectedRoleIds));
  57           fsp.setFormId("hrcs_roletreelistf7");
```

**QUERY_BUILDER** · HRRolePermHelper L56
```java
  54           fsp.setMultiSelect(false);
  55           Set<String> roleIds = HRRolePermHelper.queryViewableRoles(RequestContext.get().getCurrUserId());
  56 >         fsp.getListFilterParameter().setFilter(new QFilter("perm_role.enable", "=", (Object)"1").and("id", "in", roleIds).and("id", "not in", selectedRoleIds));
  57           fsp.setFormId("hrcs_roletreelistf7");
  58           fsp.setShowTitle(false);
```

**READ_VIA_HELPER** · HRRolePermHelper L85
```java
  83       public static Set<Long> queryUserAdminGroups(long userId) {
  84           HRBaseServiceHelper userAdminGroupServiceHelper = new HRBaseServiceHelper("perm_useradmingroup");
  85 >         DynamicObject[] userGroupItems = userAdminGroupServiceHelper.queryOriginalArray("usergroup.id", new QFilter[]{new QFilter("user.id", "=", (Object)userId)});
  86           Set groupIds = Arrays.stream(userGroupItems).map(it -> it.getLong("usergroup.id")).collect(Collectors.toSet());
  87           HRBaseServiceHelper adminGroupServiceHelper = new HRBaseServiceHelper("perm_admingroup");
```

**CALL_CROSS_SERVICE** · PermHelper L465
```java
 463                   config.put("baseDataType", dataTypes);
 464               }
 465 >             List viewUseConfig = (List)HRMServiceHelper.invokeHRMPService((String)"haos", (String)"IHAOSStructProjectService", (String)"batchQueryStructProConfig", (Object[])new Object[]{structConfigMap});
 466               for (Map entry : viewUseConfig) {
 467                   String entityType = (String)entry.get("entitytype");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.init.PermInitRecordList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp -->

## ISV 扩展指引（基于 PermInitRecordDeleteOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.PermInitRecordDeleteOp -->
