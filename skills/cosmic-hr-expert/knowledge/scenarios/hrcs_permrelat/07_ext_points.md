# 扩展点全图 · 关联权限项 (hrcs_permrelat)

> **状态**: 🟢 基于 _auto_plugin_registry.md (9 插件) + form_lifecycle_rules.json (17 lifecycleMethods · 37 rules)
> **confidence**: verified
> **数据源**: CFR 反编译 PermRelateEdit/PermRelateList/HRAdminStrictPlugin + 6 个基础设施插件 (2026-04-28)

---

## 一、插件全景 (9 个标品插件)

### 1.1 按表单分组

| 插件类 | 挂载位置 | 类型 | 父类 | 接口 |
|---|---|---|---|---|
| **PermRelateEdit** | hrcs_permrelat (form) | FormPlugin | HRDataBaseEdit | BeforeF7SelectListener, ClickListener |
| **PermRelateList** | hrcs_permrelat (list) | ListPlugin | HRDataBaseList | — |
| **HRAdminStrictPlugin** | hrcs_permrelat (动态表单模板 · preOpenForm) | FormPlugin | HRDynamicFormBasePlugin | — |
| HRDataBaseOp | hrcs_permrelat (OP 默认) | OP | — | — |
| PermItemProvider | hrcs_permrelat (list · 内部类) | IListDataProvider | — | IListDataProvider |
| 5 个 HR 引入引出 OP | hrcs_permrelat (import/export opKeys) | OP | HRDataBaseOp | — |

### 1.2 反编译类实证统计

| 反编译类 | 生命周期方法数 | 规则数 | 源码行数 (估计) |
|---|---|---|---|
| PermRelateEdit | 10 | 21 | ~740 行 |
| PermRelateList | 7 | 13 | ~340 行 |
| HRAdminStrictPlugin | 1 | 3 | ~55 行 |

---

## 二、生命周期扩展点 (按方法分组)

### 2.1 表单 FormPlugin 扩展点 (PermRelateEdit 实证)

| 生命周期方法 | 标品规则数 | ISV 可扩展 | 扩展优先级 | 关键约束 |
|---|---|---|---|---|
| **registerListener** | 6 (FP_RL1-6) | ✅ 高 | 追加自己的 Listener 在标品之后 | PR-001: 不继承 PermRelateEdit · 走并列挂 |
| **afterBindData** | 5 (FP_ABD1-5) | ✅ 高 | ISV 先于标品执行 (RowKey 靠前) | PR-004: 用 beginInit/endInit 包裹 setValue |
| **beforeDoOperation** | 3 (FP_BDO1-3) | ✅ 高 | save: ISV 检验在前 | PR-003: 不要用 getModel() 设值 |
| **beforeF7Select** | 5 (FP_BF7_1-5) | ✅ 中 | 追加 QFilter | 保持标品 F7 过滤链完整 · 不要 remove |
| **propertyChanged** | 5 (FP_PC1-5) | ✅ 高 | ISV 联动在前 | PR-004: beginInit/endInit + updateView |
| **afterDoOperation** | 4 (FP_ADO1-4) | ✅ 高 | ISV 后置逻辑在后 | 事务已提交 · 不要抛异常期望回滚 |
| **click** | 1 (FP_CLK1) | ⚠️ 低 | 新增点击目标 | 不要覆盖标品 permitem 点击行为 |
| **closedCallBack** | 1 (FP_CCB1) | ⚠️ 低 | 监听标品回调 | actionId = "hrcs_choose_permitem" 时不要拦截 |
| **confirmCallBack** | 2 (FP_CFB1-2) | ⚠️ 低 | 新增回调 ID | 标品 callBackId = "mainEntityChangeConfirm" 已占用 |
| **beforeItemClick** | 1 (FP_BIC1) | ✅ 中 | 新增 toolbar Item 拦截 | addrows 已被标品拦截 · 不要覆盖 |

### 2.2 列表 ListPlugin 扩展点 (PermRelateList 实证)

| 生命周期方法 | 标品规则数 | ISV 可扩展 | 扩展优先级 | 关键约束 |
|---|---|---|---|---|
| **beforeDoOperation** | 5 (FP_LBDO1-5) | ✅ 高 | delete/auth/btnsycrole 前置 | PR-001: 不继承 PermRelateList |
| **afterDoOperation** | 2 (FP_LADO1-2) | ✅ 高 | delete 后同步 / exportscript 后 | deleteRows 取自 PageCache · exportscript 不覆盖 |
| **beforeCreateListDataProvider** | 1 (FP_LBCDP1) | ⚠️ 低 | 不要覆盖 PermItemProvider | 标品已替换 IListDataProvider |
| **beforeShowBill** | 1 (FP_LBSB1) | ✅ 中 | 追加 CloseCallBack | incPermTips 回调已挂 · ISV 改挂自己的 |
| **confirmCallBack** | 1 (FP_LCFB1) | ⚠️ 低 | 新增回调 ID | syncAllRtPermRole 已占用 |
| **closedCallBack** | 2 (FP_LCCB1-2) | ✅ 高 | incPermTips 回调监控 | syncRole=1 触发角色弹窗 · changed 触发 refresh |
| **beforeCreateListColumns** | 1 (FP_LBCC1) | ⚠️ 低 | 额外列过滤 | isNotShowFilter 模式已处理 |

### 2.3 准入闸扩展点 (HRAdminStrictPlugin 实证)

| 生命周期方法 | 标品规则数 | ISV 可扩展 | 扩展优先级 | 关键约束 |
|---|---|---|---|---|
| **preOpenForm** | 3 (FP_HAS1-3) | ✅ 高 | ISV 额外准入条件 | 排在 HRAdminStrictPlugin **之后** 执行 |
| F7 lookUp 放行 | 1 (FP_HAS1) | 🔒 不改 | 这是通路 · 不是闸 | 普通用户可选 F7 引用 · 不能锁死 |
| 第一闸 | 1 (FP_HAS2) | ⚠️ 不改 | 平台管理员 + Cosmic 用户 | isAdminUser \|\| isCosmicUser |
| 第二闸 | 1 (FP_HAS3) | ⚠️ 不改 | HR 领域管理员 | HRAdminService.isHrAdmin() |

---

## 三、ISV 最常覆盖的 Top 3 扩展点

### 3.1 afterDoOperation(save) -- 保存后处理

**覆盖原因**：关联权限项保存后，ISV 需要同步自己的下游表、发送通知、更新缓存。

**标品已做**：
- FP_ADO4: sync hrcs_permrelatcfg (PermRelateServiceHelper.delete/addPermRelateConfigs)
- FP_ADO3: calcRtPermRole → 角色差异计算 → 弹 hrcs_syncrolesel
- FP_ADO2: returnDataToParent("changed") → 列表 refresh
- FP_ADO1: returnDataToParent(entityNum/permNum) → 父表单刷新

**ISV 扩展点**：在标品 afterDoOperation 之后追加 ISV 逻辑（排在 PermRelateEdit 之后执行）。

### 3.2 propertyChanged -- 字段联动

**覆盖原因**：加了自定义字段后需要联动（如选"优先级=高"时锁定某些字段）。

**标品已做**：
- FP_PC1: entitytype 改 → 弹确认 → 清空 mainpermitem + 分录 + 重置 appcombo
- FP_PC2: appcombo 改 → 同步 bizapp + 重算 mainpermitem
- FP_PC3: 分录 entitytypeid 改 → 清 app + 自动带出唯一应用
- FP_PC4: 分录 permitem 清 → 同步清 permitemid
- FP_PC5: 分录 app 改 → BU 一致性校验

**ISV 扩展点**：在标品 propertyChanged 之前执行 ISV 联动（RowKey 靠前），用 beginInit/endInit 防死循环。

### 3.3 preOpenForm -- 准入闸追加

**覆盖原因**：客户要求额外准入条件（如：限定特定角色才能配置关联权限项）。

**标品已做**：
- FP_HAS1: F7 lookUp 模式直接放行
- FP_HAS2: isAdminUser || isCosmicUser
- FP_HAS3: HRAdminService.isHrAdmin()

**ISV 扩展点**：自建 PreOpenForm 插件 · 排在 HRAdminStrictPlugin **之后** · 在标品双闸通过后才执行 ISV 准入逻辑。

---

## 四、opKey OP 链扩展点 (31 opKey · 7 关键)

| opKey | executionChain | ISV 可挂 OP | 推荐阶段 | 参考 |
|---|---|---|---|---|
| `save` | HRDataBaseOp | ✅ | onAddValidators / beforeExecuteOperationTransaction / afterExecuteOperationTransaction | CS-03 / CS-05 |
| `delete` | HRDataBaseOp | ✅ | beforeExecuteOperationTransaction | CS-04 |
| `auth` | (donothing · 不调 OP) | ❌ | — | 不走 OP 链 |
| `btnsycrole` | (donothing · 不调 OP) | ❌ | — | 不走 OP 链 |
| `exportscript` | (donothing · 不调 OP) | ❌ | — | 不走 OP 链 |
| `newentry` / `deleteentry` | (donothing · 不调 OP) | ❌ | — | 表单内增删行 |
| HR 引入引出 (importdata_hr 等 6 个) | HRDataBaseOp | ✅ 低 | onAddValidators | 低频 · 一般不改 |

---

## 五、子页面扩展点 (4 个子页面)

| 子页面 formId | 触发点 | 回调 actionId | ISV 可扩展 |
|---|---|---|---|
| `hrcs_choose_permitem` | entryentity.permitem Click | `hrcs_choose_permitem` | ❌ 不改 · 私有 pk 协议 "permId\|\|permName" |
| `hrcs_permrelatcfg` | auth opKey | — (MainNewTabPage · 无回调) | ⚠️ 可扩展 hrcs_permrelatcfg 场景自身 |
| `hrcs_syncroleperm` | btnsycrole 选 1-10 行 | — (Modal · 不回调) | ⚠️ 可扩展该子页面元数据 |
| `hrcs_syncrolesel` | incPermTips syncRole=1 | — (Modal · 不回调) | ⚠️ 可扩展该子页面元数据 |

---

## 六、执行顺序规则

同一生命周期方法，插件按注册顺序依次调用：

```
HRAdminStrictPlugin.preOpenForm        ← 第一（准入闸）
    ↓ (通过双闸)
ISV PreOpenFormPlugin.preOpenForm      ← ISV 额外准入（排在标品之后）
    ↓
PermRelateList (列表生命周期)
    ↓ 用户点击进入详情
PermRelateEdit.registerListener        ← 第一
ISV FormPlugin.registerListener         ← ISV 追加 Listener
    ↓
PermRelateEdit.afterBindData            ← 第一（标品灌 PageCache）
ISV FormPlugin.afterBindData            ← ISV 读 PageCache 数据
    ↓
ISV FormPlugin.propertyChanged          ← ISV 联动在前（RowKey 靠前）
PermRelateEdit.propertyChanged          ← 标品联动在后
    ↓
ISV OP.onAddValidators                  ← ISV Validator 注册
HRDataBaseOp.onAddValidators            ← 标品默认
    ↓
ISV OP.afterExecuteOperationTransaction ← ISV BEC 发送
HRDataBaseOp.afterExecuteOperationTransaction ← 标品默认
    ↓
ISV FormPlugin.afterDoOperation         ← ISV 后置逻辑
PermRelateEdit.afterDoOperation         ← 标品后置（sync hrcs_permrelatcfg + calcRtPermRole）
```

---

## 七、不要覆盖的扩展点（禁区）

| 扩展点 | 原因 |
|---|---|
| PermRelateEdit.registerListener FP_RL5 (permitem ClickListener) | 覆盖后权限项选择子页面无法弹出 |
| PermRelateList.beforeCreateListDataProvider FP_LBCDP1 | 覆盖后 PermItemProvider 替换失效 |
| HRAdminStrictPlugin.preOpenForm FP_HAS1 (F7 lookUp 放行) | 锁死后所有 F7 引用被拒 |
| PermRelateEdit.beforeDoOperation FP_BDO3 (deleteentry 预置保护) | 覆盖后预置行可删 · 破坏 PR-007 |
| PermRelateEdit.afterDoOperation FP_ADO4 (sync hrcs_permrelatcfg) | 覆盖后 hrcs_permrelatcfg 不同步 · 产生孤儿数据 |
| PermRelateEdit.confirmCallBack FP_CFB2 (cancel 回滚) | 覆盖后 entitytype 切换取消无法回滚 |

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit -->

## ISV 扩展指引（基于 PermRelateEdit 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `afterBindData`, `beforeDoOperation`, `beforeF7Select`, `propertyChanged`, `closedCallBack`, `beforeItemClick`, `afterDoOperation`, `click`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PermRtSyncService L333
```java
 331           appLogInfo.setBizObjID(entityNum);
 332           appLogInfo.setBizAppID(PermCommonUtil.getAppIdFromSuspectedAppNum((String)appId));
 333 >         appLogInfo.setUserID(Long.valueOf(RequestContext.get().getCurrUserId()));
 334           service.addLog((AppLogInfo)appLogInfo);
 335       }
```

**QUERY_BUILDER** · RoleDimRelatService L65
```java
  63           ArrayList entityDimPropRelats = Lists.newArrayListWithExpectedSize((int)16);
  64           HRBaseServiceHelper entityCtrlHelper = new HRBaseServiceHelper("hrcs_entityctrl");
  65 >         DynamicObject entityCtrlDyna = entityCtrlHelper.queryOne("entryentity.propkey,entryentity.dimension,entryentity.authrange,entryentity.needhisver", new QFilter[]{new QFilter("entitytype", "=", (Object)entityNum)});
  66           if (Objects.isNull(entityCtrlDyna)) {
  67               return entityDimPropRelats;
```

**READ_VIA_HELPER** · OrgServiceUtil L21
```java
  19   
  20       public static long getAdminRootOrgId() {
  21 >         return OrgUnitServiceHelper.getRootOrgId();
  22       }
  23   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList -->

## ISV 扩展指引（基于 PermRelateList 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void generateSql(java.lang.Object[])`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PermRtSyncService L333
```java
 331           appLogInfo.setBizObjID(entityNum);
 332           appLogInfo.setBizAppID(PermCommonUtil.getAppIdFromSuspectedAppNum((String)appId));
 333 >         appLogInfo.setUserID(Long.valueOf(RequestContext.get().getCurrUserId()));
 334           service.addLog((AppLogInfo)appLogInfo);
 335       }
```

**QUERY_BUILDER** · RoleDimRelatService L65
```java
  63           ArrayList entityDimPropRelats = Lists.newArrayListWithExpectedSize((int)16);
  64           HRBaseServiceHelper entityCtrlHelper = new HRBaseServiceHelper("hrcs_entityctrl");
  65 >         DynamicObject entityCtrlDyna = entityCtrlHelper.queryOne("entryentity.propkey,entryentity.dimension,entryentity.authrange,entryentity.needhisver", new QFilter[]{new QFilter("entitytype", "=", (Object)entityNum)});
  66           if (Objects.isNull(entityCtrlDyna)) {
  67               return entityDimPropRelats;
```

**READ_VIA_HELPER** · OrgServiceUtil L21
```java
  19   
  20       public static long getAdminRootOrgId() {
  21 >         return OrgUnitServiceHelper.getRootOrgId();
  22       }
  23   
```

**THROW_BIZ_EXCEPTION** · PermRelateList L345
```java
 343               catch (Exception exception) {
 344                   tx.markRollback();
 345 >                 throw new KDBizException(exception.getMessage());
 346               }
 347           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dimension.PermRelateList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## ISV 扩展指引（基于 HRDataBaseOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `protected protected void setRecordLimit(int)`

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · HRDataBaseOp L39
```java
  37               case "save": {
  38                   if (recordCount <= this.recordLimit) break;
  39 >                 throw new KDBizException(String.format(Locale.ROOT, ResManager.loadKDString((String)"\u6570\u636e\u91cf\u8d85\u8fc7\u9650\u5236\u9608\u503c%1$s\uff0c\u5f53\u524d\u8bb0\u5f55\u6570\uff1a%2$s\u3002", (String)"HRDataBaseOp_0", (String)"hrmp-hbp-business", (Object[])new Object[0]), this.recordLimit, recordCount));
  40               }
  41           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRDataBaseOp -->
