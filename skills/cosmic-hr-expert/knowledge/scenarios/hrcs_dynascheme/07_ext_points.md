# 扩展点全图 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于 _auto_plugin_registry.md 33 plugin 实抓 + 反编译 7 类
> **confidence**: verified
> **数据源**: OpenAPI `queryEditablePlugins` + CFR 反编译 (2026-04-28)

---

## 一、ISV 可扩展点全景图（按生命周期分组）

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    UI 层 (FormPlugin · 16 个)                            │
├─────────────────────────────────────────────────────────────────────────┤
│  preOpenForm                                                             │
│    ├─ HRBaseDataTplEdit (HRDataBaseEdit · 标品)                          │
│    ├─ HRAdminStrictPlugin (HRDynamicFormBasePlugin · 准入闸 · 不要继承)  │
│    └─ ★ ISV 扩展点 1: 添加 HR 准入二级闸（如限定到部门级管理员）         │
│                                                                          │
│  beforeBindData (9 个)                                                   │
│    ├─ HisModelFormCommonPlugin (@SdkInternal · 不要继承)                 │
│    ├─ DynaAuthSchemePlugin (HRDataBaseEdit · 场景专属 · 不要继承)        │
│    ├─ HRCustomControlPlugin (AbstractBasePlugIn)                         │
│    ├─ HRBaseDataTplList                                                  │
│    └─ ★ ISV 扩展点 2: 表单初始化前预填 ISV 自建字段 / 修改 customParam   │
│                                                                          │
│  afterBindData (5 个)                                                    │
│    ├─ HRBaseDataTplEdit                                                  │
│    ├─ HRHiesButtonSwitchPlugin (afterBindData 切换 HIES 导入导出按钮)    │
│    ├─ HisModelFormCommonPlugin (@SdkInternal · 不要继承)                 │
│    ├─ DynaAuthSchemePlugin (afterBindData L295-L332 设 PermFilter 控件)  │
│    └─ ★ ISV 扩展点 3: 表单完成后追加 UI 控件可见性 / 默认值              │
│                                                                          │
│  registerListener (1 个)                                                 │
│    └─ DynaAuthSchemePlugin (registerListener L188-L198 注册 F7/Click)    │
│                                                                          │
│  beforeDoOperation (7 个)                                                │
│    ├─ HRBaseDataTplEdit                                                  │
│    ├─ HisModelFormCommonPlugin (@SdkInternal · 不要继承)                 │
│    ├─ DynaAuthSchemePlugin (beforeDoOperation L424-L495 规则校验+灌库)   │
│    ├─ HRBasedataLogList                                                  │
│    ├─ HisModelListCommonPlugin                                           │
│    ├─ HisModelFilterPanelF7ListPlugin                                    │
│    ├─ DynaAuthSchemeListPlugin (beforeDoOperation L83-L108 列表前置)     │
│    └─ ★ ISV 扩展点 4: 业务规则前置校验（如 setadminrange 前权限检查）    │
│                                                                          │
│  afterDoOperation (6 个)                                                 │
│    ├─ HRBaseDataTplEdit                                                  │
│    ├─ HisModelFormCommonPlugin (@SdkInternal · 不要继承)                 │
│    ├─ DynaAuthSchemePlugin (afterDoOperation L507-L555)                  │
│    ├─ HRCustomControlPlugin                                              │
│    ├─ HisModelListCommonPlugin                                           │
│    ├─ DynaAuthSchemeListPlugin (afterDoOperation L135-L195)              │
│    └─ ★ ISV 扩展点 5: 操作后补刀（如审核后通知 + 更新 ISV 自建表）       │
│                                                                          │
│  propertyChanged (3 个)                                                  │
│    ├─ DynaAuthSchemePlugin (propertyChanged L679-L697 authaction 切换)   │
│    ├─ HisModelF7ListPlugin                                               │
│    ├─ HisModelFilterPanelF7ListPlugin                                    │
│    └─ ★ ISV 扩展点 6: 监听字段变更联动（CS-02 套路）                     │
│                                                                          │
│  closedCallBack (3 个)                                                   │
│    ├─ HisModelFormCommonPlugin                                           │
│    ├─ DynaAuthSchemePlugin (closedCallBack L594-L654 chooseRole/details) │
│    ├─ HisModelListCommonPlugin                                           │
│    └─ ★ ISV 扩展点 7: 子页面回填后追加联动（如 addrole 后写日志）        │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                    数据层 (OperationServicePlugIn · 10 个)                │
├─────────────────────────────────────────────────────────────────────────┤
│  onPreparePropertys (1 实证)                                             │
│    ├─ DynaAuthSchemeOp (onPreparePropertys L21-L26)                      │
│    └─ ★ ISV 扩展点 8: 声明 ISV 字段以便后续 OP 读到                      │
│                                                                          │
│  onAddValidators (4 个)                                                  │
│    ├─ HRBaseDataStatusOp                                                 │
│    ├─ HisModelOPCommonPlugin (@SdkInternal · 不要继承)                   │
│    ├─ HisUniqueValidateOp (@SdkInternal · 不要继承)                      │
│    ├─ DynaAuthSchemeOp (onAddValidators L28-L30 注册 DynaAuthSchemeValidator)│
│    └─ ★ ISV 扩展点 9: 注册自建 Validator（CS-03 / CS-04 套路）           │
│                                                                          │
│  beforeExecuteOperationTransaction (5 个)                                │
│    ├─ HRBaseDataStatusOp                                                 │
│    ├─ HRBaseDataLogOp                                                    │
│    ├─ HRBaseDataEnableOp                                                 │
│    ├─ HRBaseOriginalOp                                                   │
│    ├─ HisModelOPCommonPlugin (@SdkInternal · 不要继承)                   │
│    └─ ★ ISV 扩展点 10: 进事务前业务逻辑（注意：抛异常会回滚整个事务）    │
│                                                                          │
│  endOperationTransaction (3 实证)                                        │
│    ├─ DynaAuthSchemeSaveSubmitOp (save/submit · 调 saveRoleEntry)        │
│    ├─ DynaAuthSchemeAuditOp (audit · 列表/单据双分支)                    │
│    ├─ DynaAuthSchemeConfirmChangeOp (confirmchange · 双写 boid + bgVid)  │
│    └─ ★ ISV 扩展点 11: 事务收尾追加（CS-06 套路 · 同事务可回滚）          │
│                                                                          │
│  afterExecuteOperationTransaction (2 个)                                 │
│    ├─ HRBaseDataLogOp                                                    │
│    ├─ HisModelOPCommonPlugin (@SdkInternal · 不要继承)                   │
│    └─ ★ ISV 扩展点 12: 事务提交后异步操作（CS-05 BEC 发布方）            │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                    列表层 (ListPlugin)                                    │
├─────────────────────────────────────────────────────────────────────────┤
│  setFilter                                                               │
│    ├─ DynaAuthSchemeListPlugin (setFilter L130-L133 boid 过滤)           │
│    └─ ★ ISV 扩展点 13: 列表过滤叠加（CS-07 套路）                        │
│                                                                          │
│  beforeShowBill                                                          │
│    ├─ DynaAuthSchemeListPlugin (beforeShowBill L110-L128 setStatus VIEW) │
│    └─ ★ ISV 扩展点 14: 单据打开前修改 ShowParameter                      │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 二、Top 7 ISV 最常覆盖扩展点

| # | 扩展点 | 覆盖原因 | 推荐 CS |
|---|---|---|---|
| 1 | onAddValidators (OP) | 加业务校验 | CS-03 / CS-04 |
| 2 | propertyChanged (FormPlugin) | 字段联动 | CS-02 |
| 3 | endOperationTransaction (OP) | 跟主事务同事务写 ISV 表 | CS-06 |
| 4 | afterExecuteOperationTransaction (OP) | 发外部事件 / BEC | CS-05 |
| 5 | setFilter (ListPlugin) | 列表过滤叠加 | CS-07 |
| 6 | beforeDoOperation (FormPlugin) | UI 层前置校验 / 阻断 | CS-04（部分） |
| 7 | modifyMeta (元数据 op) | 加 ISV 字段 | CS-01 |

---

## 三、推荐父类对照表

| 想做什么 | 推荐父类 | 注解要求 | 反编译实证 |
|---|---|---|---|
| 表单插件（FormPlugin） | `kd.hr.hbp.formplugin.web.HRDataBaseEdit` | @SdkPublic | DynaAuthSchemePlugin extends 它 |
| 列表插件（ListPlugin） | `kd.hr.hbp.formplugin.web.HRDataBaseList` | @SdkPublic | DynaAuthSchemeListPlugin extends 它 |
| OP 插件 | `kd.hr.hbp.opplugin.web.HRDataBaseOp` | @SdkPublic | DynaAuthSchemeOp/SaveSubmitOp/AuditOp/ConfirmChangeOp extends 它 |
| Validator | `kd.bos.entity.validate.AbstractValidator` | @SdkPublic | DynaAuthSchemeValidator extends 它（间接证据） |
| 动态表单插件 | `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin` | @SdkPublic | HRAdminStrictPlugin extends 它 |
| 自定义控件 | `kd.hr.hbp.formplugin.web.HRCustomControlPlugin` | - | (注：不一定 @SdkPublic · 谨慎用) |

参考 `_shared/cosmic_hr_sdk_whitelist_audit.md` 实证 9 个可继承基类清单。

---

## 四、不要继承的标品场景专属类（红区）

按 `_shared/platform_rules.json` PR-001 + `cosmic_sdk_annotation_whitelist.md` SDK 注解白名单铁律：

### 4.1 dynascheme 场景专属（**禁止继承**）

| 类 | 原因 |
|---|---|
| `DynaAuthSchemePlugin` | 场景专属 · 标品改 propertyChanged/beforeDoOperation 签名你必须跟 |
| `DynaAuthSchemeListPlugin` | 场景专属 · 标品改 setFilter/afterDoOperation 你必须跟 |
| `DynaAuthSchemeOp` | 场景专属 · 它的 onPreparePropertys 字段列表会变 |
| `DynaAuthSchemeSaveSubmitOp` | 场景专属 · saveRoleEntry 调用方式可能变 |
| `DynaAuthSchemeAuditOp` | 场景专属 · 列表/单据双分支可能变 |
| `DynaAuthSchemeConfirmChangeOp` | 场景专属 · 双写逻辑可能变 |
| `DynaAuthSchemeValidator` | 场景专属 |

### 4.2 平台时序内部类（@SdkInternal · **禁止继承**）

| 类 | 原因 |
|---|---|
| `HisModelFormCommonPlugin` | @SdkInternal · 时序公共表单插件 · 内部 API |
| `HisModelOPCommonPlugin` | @SdkInternal · 时序公共 OP 插件 |
| `HisUniqueValidateOp` | @SdkInternal · 时序唯一性校验 |
| `HisModelListCommonPlugin` | @SdkInternal · 时序公共列表插件 |
| `HisModelF7ListPlugin` | @SdkInternal · 时序 F7 列表插件 |
| `HisModelFilterPanelListPlugin` | @SdkInternal |
| `HisModelFilterPanelF7ListPlugin` | @SdkInternal |
| `HisModelMobileListPlugin` | @SdkInternal · 移动端时序列表 |
| `HisBaseDataF7FastFilter` | @SdkInternal · F7 快速过滤 |

### 4.3 hrcs 共用准入闸（**复用即可 · 不要继承**）

| 类 | 处理方式 |
|---|---|
| `HRAdminStrictPlugin` | 11 hrcs 表单已挂 · ISV 不要继承 · 也不要重新挂（重复挂会重复校验报错） |

### 4.4 其他领域专属（**不在 hrcs 白名单**）

| 类 | 原因 |
|---|---|
| `AbsOrgBaseOp` | 组织域专属 · 不是 hrcs · 不在白名单 |
| `JobHrSaveOp / JobHrAuditOp / JobHrDisableOp` | hjm 域 · 不是 hrcs |

---

## 五、扩展点 × opKey 映射快表

| opKey | 推荐扩展点 | 备注 |
|---|---|---|
| `save` | onAddValidators / endOperationTransaction / afterExecuteOperationTransaction | 三阶段都可挂 |
| `submit` | onAddValidators / endOperationTransaction | 与 save 共用 OP `DynaAuthSchemeSaveSubmitOp` |
| `audit` | onAddValidators / afterExecuteOperationTransaction | 注意 list_op 双分支 |
| `unaudit` | onAddValidators | 标品 OP 较简单 |
| `confirmchange` | endOperationTransaction / afterExecuteOperationTransaction | CS-05 / CS-06 |
| `delete` | onAddValidators (CS-04) | 列表上才走 · DynaAuthSchemeListPlugin.afterDoOperation 已级联清下游 |
| `disable` / `enable` | onAddValidators | 标品 HRBaseDataEnableOp 已校验状态 |
| `setadminrange` | beforeDoOperation (FormPlugin) | 跟 hrcs_dynaschrange 子页面联动 |
| `addrole` | closedCallBack 监听 chooseRole/roleDetails | 角色范围属性子页面 |
| `assignrecord` | afterDoOperation (ListPlugin) | 跳 hrcs_userrolerelat 列表 · 改 sourcetype 过滤可定制 |
| `audithisconfirmchange` | afterDoOperation | showChangeTips 提示 |

---

## 六、ISV 扩展元数据点

### 6.1 modifyMeta 加字段（CS-01）

| op | elementType | parentScope | 说明 |
|---|---|---|---|
| `add` | `field` | `hrcs_dynascheme` | 加主表字段 |
| `add` | `field` | `hrcs_dynascheme.roleentry` | 加 roleentry 子表字段 |
| `add` | `field` | `hrcs_dynascheme.assignactionentry` | 加 assignactionentry 子表字段 |
| `add` | `field` | `hrcs_dynascheme.cancelactionentry` | 加 cancelactionentry 子表字段 |
| `add` | `entity` | `hrcs_dynascheme` | 加新分录子实体（如自建"方案变更日志"分录） |

### 6.2 元数据规则扩展（addRule）

dynascheme 当前 listRules 有 2 条 formRule（authaction=1 显示分配 / authaction=2 显示取消）· ISV 可以加：
- 新 formRule（控制 ISV 字段可见性）· 用 PermissionRule / 通过 IDEA 插件 addRule 接口
- ⚠️ preCondition 不能用 `==''`（kb_cosmic_addrule_traps.md 实证 · 用 `is empty` / `is not empty`）

参考 `kb_cosmic_addrule_traps.md` 实证规则添加约束。

---

## 七、扩展点选择决策树

```
我要做什么？
   ↓
1. 加字段？ → modifyMeta add field（CS-01）
   ↓
2. 字段联动？ → propertyChanged@FormPlugin（CS-02）
   ↓
3. save 前业务校验？
   ├─ 校验主表数据 → onAddValidators 注册 Validator（CS-03）
   ├─ 跨表查下游引用 → onAddValidators + QueryServiceHelper（CS-04）
   └─ 校验角色权限 → onAddValidators + HRRolePermHelper
   ↓
4. save/audit 后写 ISV 表？
   ├─ 同事务 + 失败回滚 → endOperationTransaction（CS-06）
   └─ 异步 + 跨系统 → afterExecuteOperationTransaction + BEC（CS-05）
   ↓
5. 列表过滤？ → setFilter@ListPlugin（CS-07）
   ↓
6. 子页面跳转 / 修改 customParam？ → beforeShowBill@ListPlugin
   ↓
7. UI 控件可见性 / 必填？
   ├─ 静态规则 → 元数据 formRule (addRule API)
   └─ 动态条件 → afterBindData@FormPlugin
```

---

## 八、Cosmic 注解白名单速查（生成代码前必查）

按 `cosmic_sdk_annotation_whitelist.md` 铁律：

> ISV 扩展代码只能调用 / 继承 / 实现带 `@SdkPublic` / `@SdkPlugin` / `@SdkService` 之一的 kd.* 类或方法 · 无注解视为内部 API 禁用

| 类家族 | 注解 | 可继承？ |
|---|---|---|
| `kd.hr.hbp.formplugin.web.HRDataBaseEdit/List/etc` | `@SdkPublic` | ✅ |
| `kd.hr.hbp.opplugin.web.HRDataBaseOp` | `@SdkPublic` | ✅ |
| `kd.bos.entity.validate.AbstractValidator` | `@SdkPublic` | ✅ |
| `kd.bos.entity.plugin.AbstractOperationServicePlugIn` | `@SdkPublic` | ✅ |
| `kd.bos.form.plugin.AbstractFormPlugin` | `@SdkPublic` | ✅ |
| `kd.bos.list.plugin.AbstractListPlugin` | `@SdkPublic` | ✅ |
| `kd.bos.bec.api.IEventService` | `@SdkPublic` | ✅（用 ServiceHelper.getService） |
| `kd.bos.id.ID` | `@SdkPublic` | ✅ |
| `kd.hr.hbp.business.servicehelper.HRBaseServiceHelper` | `@SdkPublic` | ✅（直接 new） |
| `kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin` | （无 SDK 注解） | ❌ 内部 API |
| `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp` | （无 SDK 注解） | ❌ 内部 API |
| `kd.hr.hrcs.bussiness.servicehelper.perm.dyna.DynaAuthSchemeServiceHelper` | （无 SDK 注解） | ⚠️ 可调用但非保证 · 用前查 sdk_registry |

→ 调用 ServiceHelper 类前必查 `_sdk_audit/sdk_registry.json` · 若无注解 · 视作内部 API · ISV 自负其险（标品升级可能改）。

---

## 九、扩展点版本兼容警告

- **8.0**：当前实证版本（hrmp-hrcs-formplugin-1.0.jar / hrmp-hrcs-opplugin-1.0.jar）
- **2024R1 / 2025**：未实测 · 建议每个版本重跑 probe 看 opKey/插件链是否变化
- **HisModel 时序模板**：`hbp_histimeseqtpl` 是平台底层模板 · 各版本会保持兼容（标品承诺）
- **PermFilter 控件**：8.0 已稳定 · DynaAuthSchemePlugin.afterBindData 大量依赖 · 不要替换控件

---

## 十、ISV 自建插件命名规范

按 `_shared/platform_rules.json` 推断 + 苍穹规约：

```
ISV 前缀（不可省）：
  tdkw / tdkp / xxxx (取决于客户/产品)

包名规范：
  kd.<isv>.hrcs.formplugin.dyna.<功能名>Plugin       // 表单插件
  kd.<isv>.hrcs.formplugin.dyna.<功能名>ListPlugin   // 列表插件
  kd.<isv>.hrcs.opplugin.dyna.<功能名>Op             // OP 插件
  kd.<isv>.hrcs.opplugin.dyna.<功能名>Validator      // Validator

字段 key 前缀：
  ${ISV_FLAG}_xxx                                           // ISV 自建主表字段
  ${ISV_FLAG}_xxxxxxxxx                                     // 子表字段同前缀

实体表名：
  ${ISV_FLAG}_<业务名>_<细分>                               // 如 ${ISV_FLAG}_dynascheme_changelog
```

→ 包名 + 字段名都带 ISV 前缀 · 防止跨 ISV 冲突。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## ISV 扩展指引（基于 HisModelFormCommonPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `beforeBindData`, `afterCreateNewData`, `afterLoadData`, `afterBindData`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void initialize()`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin -->

## ISV 扩展指引（基于 DynaAuthSchemePlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `beforeBindData`, `afterBindData`, `beforeF7Select`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`, `propertyChanged`, `afterF7Select`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void afterF7Select(kd.bos.form.field.events.AfterF7SelectEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRRolePermHelper L55
```java
  53           fsp.getOpenStyle().setShowType(ShowType.Modal);
  54           fsp.setMultiSelect(false);
  55 >         Set<String> roleIds = HRRolePermHelper.queryViewableRoles(RequestContext.get().getCurrUserId());
  56           fsp.getListFilterParameter().setFilter(new QFilter("perm_role.enable", "=", (Object)"1").and("id", "in", roleIds).and("id", "not in", selectedRoleIds));
  57           fsp.setFormId("hrcs_roletreelistf7");
```

**QUERY_BUILDER** · HRQFilterHelper L17
```java
  15   public class HRQFilterHelper {
  16       public static QFilter buildEql(String filed, Object val) {
  17 >         return new QFilter(filed, "=", val);
  18       }
  19   
```

**READ_VIA_HELPER** · HRRolePermHelper L85
```java
  83       public static Set<Long> queryUserAdminGroups(long userId) {
  84           HRBaseServiceHelper userAdminGroupServiceHelper = new HRBaseServiceHelper("perm_useradmingroup");
  85 >         DynamicObject[] userGroupItems = userAdminGroupServiceHelper.queryOriginalArray("usergroup.id", new QFilter[]{new QFilter("user.id", "=", (Object)userId)});
  86           Set groupIds = Arrays.stream(userGroupItems).map(it -> it.getLong("usergroup.id")).collect(Collectors.toSet());
  87           HRBaseServiceHelper adminGroupServiceHelper = new HRBaseServiceHelper("perm_admingroup");
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRCustomControlPlugin -->

## ISV 扩展指引（基于 HRCustomControlPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRCustomControlPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRCustomControlPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.base.AbstractBasePlugIn`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public boolean isLock(int, java.lang.String)`
- `public public void updateCustomControlStatus()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRCustomControlPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRCustomControlPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRCustomControlPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## ISV 扩展指引（基于 HisModelListCommonPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`, `beforeBindData`, `setFilter`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelListCommonPlugin L174
```java
 172           }
 173           if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
 174 >             QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
 175               event.getQFilters().add(currentDataFilter);
 176           } else {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## ISV 扩展指引（基于 HisModelF7ListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforePackageData`, `propertyChanged`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

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

**THROW_BIZ_EXCEPTION** · TimelineService L101
```java
  99                   IDataEntityProperty property = (IDataEntityProperty)dataEntityType.getAllFields().get("isdeleted");
 100                   if (HRStringUtils.isEmpty((String)property.getAlias())) {
 101 >                     throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u5df2\u5f00\u542f\u65f6\u95f4\u8f74\u903b\u8f91\u5220\u9664\uff0c\u8bf7\u914d\u7f6e\u5b57\u6bb5\u201c\u662f\u5426\u5df2\u5220\u9664\u201d\u7684\u6570\u636e\u5e93\u5b57\u6bb5\u540d\u3002", (String)"TimelineService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityName));
 102                   }
 103               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## ISV 扩展指引（基于 HisModelFilterPanelListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`, `setFilter`

### 可重写方法（target.java self）
- `public public void initialize()`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void filterContainerSearchClick(kd.bos.form.events.FilterContainerSearchClickArgs)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## ISV 扩展指引（基于 HisModelFilterPanelF7ListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `afterBindData`, `beforePackageData`, `setFilter`, `propertyChanged`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void beforeCreateListColumns(kd.bos.form.events.BeforeCreateListColumnsArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin -->

## ISV 扩展指引（基于 DynaAuthSchemeListPlugin 真实证）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforeBindData`, `beforeDoOperation`, `setFilter`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · DynaAuthSchemeListPlugin L131
```java
 129   
 130       public void setFilter(SetFilterEvent evt) {
 131 >         QFilter idFilter = new QFilter("boid", "in", (Object)DynaAuthSchemeServiceHelper.queryViewableSchemes());
 132           evt.getQFilters().add(idFilter);
 133       }
```

**READ_VIA_HELPER** · DynaAuthSchemeListPlugin L115
```java
 113               return;
 114           }
 115 >         long boid = DynaAuthSchemeServiceHelper.querySchemeBoid((Object)pkId);
 116           Map customParams = this.getView().getFormShowParameter().getCustomParams();
 117           if (customParams.containsKey("schemeName")) {
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## ISV 扩展指引（基于 HisModelMobileListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.list.plugin.AbstractMobListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelMobileListPlugin L31
```java
  29           super.setFilter(event);
  30           if (this.listProcessor.getHisPageEnum() == HisPageEnum.NOT_HIS_PAGE) {
  31 >             QFilter currentDataFilter = new QFilter("iscurrentversion", "=", (Object)Boolean.TRUE);
  32               event.getQFilters().add(currentDataFilter);
  33           }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## ISV 扩展指引（基于 HisModelOPCommonPlugin 真实证）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`

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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## ISV 扩展指引（基于 HisUniqueValidateOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp -->

## ISV 扩展指引（基于 DynaAuthSchemeOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp -->

## ISV 扩展指引（基于 DynaAuthSchemeSaveSubmitOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp -->

## ISV 扩展指引（基于 DynaAuthSchemeAuditOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**READ_VIA_HELPER** · DynaAuthSchemeAuditOp L30
```java
  28               for (DynamicObject dyo : dyoArr) {
  29                   long boId = dyo.getLong("id");
  30 >                 DynamicObjectCollection roleEntryColl = DynaRoleDetailServiceHelper.loadRoleCustomInfo((long)boId);
  31                   long sourceVid = dyo.getLong("sourcevid");
  32                   DynamicObjectCollection versionRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl((long)sourceVid, (DynamicObjectCollection)roleEntryColl);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp -->

## ISV 扩展指引（基于 DynaAuthSchemeConfirmChangeOp 真实证）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**THROW_BIZ_EXCEPTION** · DynaAuthSchemeConfirmChangeOp L30
```java
  28           String isListConfirmChange = this.getOption().getVariableValue("list_op", "");
  29           if (isListConfirmChange.equals("1")) {
  30 >             throw new KDBizException("not support");
  31           }
  32           DynamicObject dyo = dyoArr[0];
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## ISV 扩展指引（基于 HisBaseDataF7FastFilter 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.base.AbstractBasedataController`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void buildBaseDataCoreFilter(kd.bos.form.field.events.BaseDataCustomControllerEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · HisModelCommonService L196
```java
 194       public QFilter getEffectingVersionQFilter() {
 195           Date now = new Date();
 196 >         return new QFilter("bsed", "<=", (Object)now).and(new QFilter("bsled", ">=", (Object)now)).and(new QFilter("datastatus", "=", (Object)HisModelDataStatusEnum.EFFECTING.getStatus())).and(new QFilter("iscurrentversion", "=", (Object)Boolean.FALSE));
 197       }
 198   
```

**THROW_BIZ_EXCEPTION** · HisModelCommonService L124
```java
 122                   LOGGER.error((Throwable)exception);
 123               }
 124 >             throw new KDBizException(String.format(ResManager.loadKDString((String)"\u201c%s\u201d\u7684\u5386\u53f2\u6a21\u578b\u5b9e\u4f53\u914d\u7f6e\u201c\u6a21\u5f0f\u9009\u62e9\u201d\u672a\u914d\u7f6e\uff0c\u8bf7\u5148\u5b8c\u6210\u914d\u7f6e\u3002", (String)"HisModelCommonService_1", (String)"hrmp-hbp-business", (Object[])new Object[0]), entityNumber));
 125           }
 126           return hisModelEntityConfig;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
