# 扩展点全图 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类实证 + PR-001 铁律 + SDK 白名单实证
> **confidence**: verified
> **场景专属类**: 3 个（超出三胞胎的 2 个）

---

## 一、场景专属类（3 个 · 全部禁继承）

### 1.1 AdminorgtypeEditPlugin — 编辑视图核心插件

| 属性 | 值 |
|---|---|
| **全限定类名** | `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin` |
| **父类** | `HRDataBaseEdit`（白名单合规） |
| **Jar** | `hrmp-haos-formplugin-1.0.jar` |
| **注册顺序** | #9（在 5 个 kd.bos.* 基础插件之后） |
| **生命周期方法** | `beforeBindData` + `propertyChanged` |
| **ISV 继承** | ❌ **禁继承**（PR-001）|

**beforeBindData 职责**：
- 读取 `adminorgtypestd` 是否已被下游实体引用（`baseDataCheckReference` 平台 API）
- 被引用 → `getView().setEnable(false, ["adminorgtypestd"])` 字段灰化
- 未被引用 → 字段正常可编辑

**propertyChanged 职责**：
- 监听 `adminorgtypestd` 字段变更
- 通过 `AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)` 枚举查映射
- 调 `getModel().setValue("orgpattern", orgPatternId)` 自动填充 orgpattern

**ISV 扩展替代方案**：
- 禁止继承 `AdminorgtypeEditPlugin`（PR-001 · 场景专属）
- 改用 `HRCoreBaseBillEdit` 或 `HRBaseDataTplEdit` 并列挂新 FormPlugin
- ISV 的 propertyChanged 必须用 `beginInit/endInit` 包裹（PR-004 · 标品未用但 ISV 必须加）

---

### 1.2 AdminorgtypeListPlugin — 列表视图排序插件

| 属性 | 值 |
|---|---|
| **全限定类名** | `kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin` |
| **父类** | `HRDataBaseList`（白名单合规） |
| **Jar** | `hrmp-haos-formplugin-1.0.jar` |
| **注册顺序** | #17 |
| **生命周期方法** | `setFilter`（setOrderBy 三级排序）|
| **ISV 继承** | ❌ **禁继承**（PR-001）|

**setFilter 职责**：
```java
setFilterEvent.setOrderBy("enable desc,adminorgtypestd.number asc,number asc");
```
三级排序：启用优先 / 关联标准编码升序 / 本身编码升序。

**与三胞胎差异**：
- haos_adminorgfunction 的 ListOrderCommonPlugin：二级排序（enable desc, number asc）
- haos_orgchangereason 的 ChangeReasonListPlugin：QFilter 过滤特殊主键 + beforeShowBill
- **本场景**：三级排序（多出 adminorgtypestd.number asc 分组维度）

**ISV 扩展替代方案**：
- 禁止继承 `AdminorgtypeListPlugin`（PR-001）
- 改用 `HRDataBaseList` 或 `HRBaseDataTplList` 并列挂新 ListPlugin
- ISV 的 setFilter 追加自定义 QFilter 或 orderBy

---

### 1.3 AdminOrgTypeSaveOp — HIES 导入修正 OP

| 属性 | 值 |
|---|---|
| **全限定类名** | `kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp` |
| **父类** | `HRDataBaseOp`（白名单合规）|
| **Jar** | `hrmp-haos-opplugin-1.0.jar` |
| **注册顺序** | #28（在 BaseDataBuOp #26 之后）|
| **生命周期方法** | `beginOperationTransaction` |
| **触发条件** | `importtype` 变量非空（HIES 专用）|
| **ISV 继承** | ❌ **禁继承**（PR-001）|

**beginOperationTransaction 职责**：
- 检测 `options.getVariables().get("importtype")` 是否非空
- HIES 导入场景：读 AdminOrgTypeStdEnum → 查 bos_org_pattern → 修正 orgpattern 字段
- 非 HIES 导入：直接 return（不执行任何逻辑）

**ISV 扩展替代方案**：
- 禁止继承 `AdminOrgTypeSaveOp`（PR-001）
- 改用 `HRDataBaseOp` 并列挂新 OP，在 `beginOperationTransaction` 中检测 importtype 守卫
- RowKey 建议排在 AdminOrgTypeSaveOp 之后（PR-002 · 标品先修正 orgpattern · ISV 再补修正 ISV 字段）

---

## 二、共享 OP 插件（haos 域共享 · 全部禁继承）

### 2.1 BaseDataBuOp — 控制策略 Validator 注册 OP

| 属性 | 值 |
|---|---|
| **全限定类名** | `kd.hr.haos.opplugin.web.BaseDataBuOp` |
| **父类** | `HRDataBaseOp` |
| **Jar** | `hrmp-haos-opplugin-1.0.jar` |
| **注册顺序** | #26（本场景）|
| **生命周期方法** | `onAddValidators` |
| **ISV 继承** | ❌ **禁继承**（PR-001）|

**onAddValidators 职责**：
```java
super.onAddValidators(args);
args.addValidator((AbstractValidator) new CtrlStrategyValidator());
```
注册控制策略合规校验。与三胞胎 haos_adminorgfunction 共享**同一个类**（haos 域公共 OP）。

> **注意**：`BaseDataBuOp` 被本场景和三胞胎场景共享，是 haos 域的公共 OP 薄壳（23 行），ISV 禁止继承（PR-001），因为它是 haos 域共享实现，标品升级时方法签名可能变更。

---

## 三、禁继承完整清单（PR-001 铁律）

以下 6 个类 ISV **严禁继承**，全文出现次数 ≥ 2 次（本文集中列出，06_customization_solutions.md 每个 CS 均有提示）：

| 类名 | 包路径 | 禁止原因 |
|---|---|---|
| `AdminorgtypeEditPlugin` | `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin` | 场景专属 FormPlugin · PR-001 |
| `AdminorgtypeListPlugin` | `kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin` | 场景专属 List Plugin · PR-001 |
| `AdminOrgTypeSaveOp` | `kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp` | 场景专属 OP · HIES 导入修正 · PR-001 |
| `BaseDataBuOp` | `kd.hr.haos.opplugin.web.BaseDataBuOp` | haos 域共享薄壳 OP · PR-001 |
| `AbsOrgBaseOp` | `kd.hr.haos.opplugin.web.AbsOrgBaseOp` | 不在 HR SDK 白名单（实证 forbidden）|
| `CtrlStrategyValidator` | `kd.hr.haos.opplugin.web.validate.CtrlStrategyValidator` | haos 域内部 Validator · 不在白名单 |

**违反后果**：
- 继承场景专属类 → 标品升级方法签名变 → ISV 编译失败（PR-001 why 原文）
- 继承 AbsOrgBaseOp → 运行时类加载失败（不在 SDK 白名单 · 类可能被 obscure）

---

## 四、推荐 ISV 父类（白名单合规）

### OP 插件父类

| 父类 | 全限定类名 | 适用场景 |
|---|---|---|
| `HRDataBaseOp` | `kd.hr.hbp.opplugin.web.HRDataBaseOp` | **首选** · CS-02/CS-04/CS-05 主类 |
| `AbstractOperationServicePlugIn` | `kd.bos.entity.plugin.AbstractOperationServicePlugIn` | 通用 · 不含 HR 域能力 |

### FormPlugin 父类

| 父类 | 全限定类名 | 适用场景 |
|---|---|---|
| `HRCoreBaseBillEdit` | `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit` | **首选** · CS-03 字段联动 |
| `HRBaseDataTplEdit` | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | 与标品同源 · 更精准 |

### List Plugin 父类

| 父类 | 全限定类名 | 适用场景 |
|---|---|---|
| `HRDataBaseList` | `kd.hr.hbp.formplugin.web.HRDataBaseList` | 列表过滤/排序定制 |
| `HRBaseDataTplList` | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList` | 与标品 List 模板同源 |

### Validator 父类

| 父类 | 全限定类名 | 适用场景 |
|---|---|---|
| `AbstractValidator` | `kd.bos.entity.validate.AbstractValidator` | CS-02 校验逻辑 |

---

## 五、AdminorgtypeEditPlugin 设计模式分析

### 5.1 双方法设计模式

`AdminorgtypeEditPlugin` 是本场景最复杂的专属插件，实现了两个生命周期方法，各司其职：

```
beforeBindData（页面加载时）：
  用途：查引用状态 → 决定字段可编辑性
  特点：副作用（setEnable）· 一次性执行
  与数据无交互

propertyChanged（用户操作时）：
  用途：字段联动填值
  特点：响应式 · 多次可触发
  与 AdminOrgTypeStdEnum 枚举交互
```

### 5.2 AdminOrgTypeStdEnum 枚举使用模式（只查不继承）

`AdminOrgTypeStdEnum` 是标品内部枚举类，ISV 可以**查询**但**不可继承/修改**：
```java
// 查询枚举映射（正确用法 · 只查）
long orgPatternId = AdminOrgTypeStdEnum.getOrgPatternIdById(stdId);

// ❌ 禁止继承 AdminOrgTypeStdEnum（标品内部枚举 · 不在 SDK 白名单）
// ❌ 禁止通过反射修改枚举值
```

### 5.3 与三胞胎 ListOrderCommonPlugin 模式对比

| 维度 | AdminorgtypeEditPlugin（本场景）| ListOrderCommonPlugin（三胞胎）|
|---|---|---|
| 方法数 | 2（beforeBindData + propertyChanged）| 1（setFilter）|
| 业务职责 | 引用检查 + 字段联动 | 仅排序 |
| 复杂度 | 中（有外部 API 调用）| 低（纯 setOrderBy）|
| ISV 模仿难度 | 需要理解 baseDataCheckReference + 枚举映射 | 简单（只 setOrderBy）|

---

## 六、扩展点决策树

```
需求类型？
├─ 加 ISV 字段 → CS-01（modifyMeta op=add）
├─ 删除/禁用前引用校验 → CS-02（HRDataBaseOp 并列挂 · onAddValidators）
├─ 自定义字段联动 → CS-03（HRCoreBaseBillEdit 并列挂 · propertyChanged）
│    ⚠ 必须 beginInit/endInit 防死循环（PR-004）
│    ⚠ 禁继承 AdminorgtypeEditPlugin（PR-001）
├─ HIES 导入修正扩展 → CS-04（HRDataBaseOp 并列挂 · beginOperationTransaction）
│    ⚠ 禁继承 AdminOrgTypeSaveOp（PR-001）
│    ⚠ 必须检测 importtype 守卫
├─ 加子表 → CS-05（modifyMeta add entity + HRDataBaseOp 并列挂 · PR-005）
│    ⚠ 禁继承 BaseDataBuOp / AdminOrgTypeSaveOp（PR-001）
└─ 跨模块通知 → BEC 反指引（CS-05 末尾 · 标品 grep 0 · 不推荐）
```

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin -->

## ISV 扩展指引（基于 AdminorgtypeEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin -->

## ISV 扩展指引（基于 AdminorgtypeListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

## ISV 扩展指引（基于 HRBaseDataCommonMobList 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractMobListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `protected protected kd.bos.orm.query.QFilter getStatusFilter()`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HRBaseDataCommonMobList L24
```java
  22   
  23       protected QFilter getStatusFilter() {
  24 >         QFilter qFilter = new QFilter("enable", "=", (Object)Character.valueOf('1'));
  25           qFilter.and(new QFilter("status", "!=", (Object)"LD"));
  26           return qFilter;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.BaseDataBuOp -->

## ISV 扩展指引（基于 BaseDataBuOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.BaseDataBuOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.BaseDataBuOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.BaseDataBuOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.BaseDataBuOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.BaseDataBuOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp -->

## ISV 扩展指引（基于 AdminOrgTypeSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgTypeSaveOp L37
```java
  35        */
  36       public void beginOperationTransaction(BeginOperationTransactionArgs e) {
  37 >         String appCacheKey = "IMPORT_ADMIN_TYPE" + RequestContext.get().getTraceId();
  38           try {
  39               if (HRStringUtils.isEmpty((String)((String)this.getOption().getVariables().get("importtype")))) {
```

**QUERY_BUILDER** · AdminOrgTypeSaveOp L44
```java
  42               Map appCache = (Map)HRAppCache.get((String)"homs").get(appCacheKey, Map.class);
  43               List orgPatternIds = Arrays.stream(AdminOrgTypeStdEnum.values()).map(std -> std.getOrgPatternId()).collect(Collectors.toList());
  44 >             DynamicObject[] orgPatternDynamicObjectArray = BusinessDataServiceHelper.load((String)"bos_org_pattern", (String)"id", (QFilter[])new QFilter[]{new QFilter("id", "in", orgPatternIds)});
  45               Map<Long, DynamicObject> orgPatternMap = Arrays.stream(orgPatternDynamicObjectArray).collect(Collectors.toMap(pattern -> pattern.getLong("id"), pattern -> pattern));
  46               DynamicObject[] dataEntities = e.getDataEntities();
```

**READ_VIA_HELPER** · AdminOrgTypeSaveOp L44
```java
  42               Map appCache = (Map)HRAppCache.get((String)"homs").get(appCacheKey, Map.class);
  43               List orgPatternIds = Arrays.stream(AdminOrgTypeStdEnum.values()).map(std -> std.getOrgPatternId()).collect(Collectors.toList());
  44 >             DynamicObject[] orgPatternDynamicObjectArray = BusinessDataServiceHelper.load((String)"bos_org_pattern", (String)"id", (QFilter[])new QFilter[]{new QFilter("id", "in", orgPatternIds)});
  45               Map<Long, DynamicObject> orgPatternMap = Arrays.stream(orgPatternDynamicObjectArray).collect(Collectors.toMap(pattern -> pattern.getLong("id"), pattern -> pattern));
  46               DynamicObject[] dataEntities = e.getDataEntities();
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp -->
