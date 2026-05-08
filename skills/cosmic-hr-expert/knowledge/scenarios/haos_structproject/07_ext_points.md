# 扩展点全图 · 矩阵组织设置（haos_structproject）

> **状态**：🟢 基于 24 plugins + 14 反编译类 + 52 opKey 实证
> **confidence**：verified

## 1. 扩展点总览

| 维度 | 数量 |
|---|---|
| 全部插件（含 HIES 模板）| 24 |
| 反编译命中 | 14 / 24（其中 10 是 hbp 模板 / 平台插件 · 不需反编译）|
| ISV 已签 final 类（**禁继承**）| 8（StructProjectEditPlugin / ListPlugin / SaveOp / DeleteOP / DisableOp / EnableOp / StructOrgPermSaveOp / StructProjectBUListPlugin）|
| 可继承的 SDK 抽象基类 | 5（HRDataBaseEdit / HRDataBaseList / HRDataBaseOp / AbstractValidator / AbstractBUListPlugin）|
| opKey 总数 | 52（其中 6 HIES + 46 业务 opKey）|

## 2. opKey × 扩展点矩阵（52 opKey 全图）

### 2.1 核心业务 opKey（深度扩展）

| opKey | 中文 | 标品 OP 链 | 可挂的扩展点 | 推荐 CS |
|---|---|---|---|---|
| `save` | 保存 | 7 OP（CodeRule → BdVersion → Status → Log → Enable → Original → StructProjectSaveOp）| onAddValidators / beforeExecuteOperationTransaction / afterExecuteOperationTransaction | CS-02（roottype 切换校验）/ CS-05（BEC 发布）|
| `submit` / `submitandnew` | 提交 / 提交并新增 | 同 save 链 | 同 save | CS-02（同步挂 · 防绕过）|
| `audit` | 审核 | 1（HRBaseDataLogOp）| onAddValidators | 业务方真要走审批 走 OPM |
| `unaudit` | 反审核 | 1（HRBaseDataLogOp）| onAddValidators | 同 audit |
| `enable` | 启用 | 2（HRBaseDataLogOp + StructProjectEnableOp）| onAddValidators | CS-07（前置校验）|
| `disable` | 禁用 | 2（HRBaseDataLogOp + StructProjectDisableOp）| onAddValidators | （类似 CS-07 启用前置校验）|
| `delete` | 删除（表单内）| 1（StructProjectDeleteOP · 仅 enable=10 行）| onAddValidators | CS-04（反向引用校验）|
| `delete_project` | 删除（列表）| 1（StructProjectDeleteOP）| onAddValidators | CS-04（同步挂）|
| `modify` | 修改 | 0（仅 FormPlugin · 抢互斥锁）| beforeDoOperation / afterDoOperation | （ISV 一般不挂 · 互斥锁是平台层）|
| `new` / `addnew_org` | 新增 / 新增带组织 | 0 OP | afterCreateNewData (FormPlugin) | CS-03（联动）|
| `view` | 查看 | 0 | preOpenForm / afterBindData | （展示性扩展）|
| `copy` | 复制 | 0 OP（走 save 时跑 7 OP 链）| 同 save | （继承 save CS）|

### 2.2 跳转类 opKey（donothing 类型 · UI 行为扩展）

| opKey | 中文 | 标品行为 | ISV 扩展点 |
|---|---|---|---|
| `maintain_struct` | 维护架构（表单内）| StructProjectEditPlugin.openOperationPage L292-296 → showMaintainFrameworkForm | afterDoOperation 后挂 · 改 ListShowParameter customParam |
| `maintainframework` | 维护架构（列表）| StructProjectListPlugin.openOperationPage L124-127 → showMaintainFrameworkForm | 同上 + 处理 1040 全领域标记 |
| `edit_struct` | 编辑架构互斥锁 | beforeDoOperation L223-230 抢锁 | （平台互斥锁 · 不挂业务）|
| `namehistory` / `namehistoryview` | 改名 / 名称历史查询 | BaseDataNameVersionListPlugin | （展示性）|
| `logview` / `viewonelog` | 查看日志 | HRBaseDataLogOp + HRBasedataLogList | （展示性）|

### 2.3 HIES（导入导出）opKey · 6 个

| opKey | 中文 | 标品插件 | ISV 扩展点 |
|---|---|---|---|
| `importdata_hr` | 导入数据 | HRBaseDataImportEdit | beforeImport / afterImport（参考 hbp 导入扩展规范）|
| `export_from_list_hr` | 按列表导出 | HRHiesButtonSwitchPlugin | 标品 HIES 模板控制 |
| `export_from_impttpl_hr` | 按导入模板导出 | 同上 | 同上 |
| `export_from_expttpl_hr` | 按导出模板导出 | 同上 | 同上 |
| `show_import_record_hr` | 查看导入记录 | 同上 | 展示性 |
| `show_export_record_hr` | 查看导出记录 | 同上 | 展示性 |

### 2.4 列表 / 分页 / 移动端 / 分录占位（剩余 ~30 opKey）

详见 `opkeys_index.json` · ISV 一般不动这些 · 平台标准行为。

## 3. 插件清单（24 个 · 来自 _auto_plugin_registry.md 实证）

### 3.1 表单插件（FormPlugin · 11 个）

| # | ClassName | 父类 | 生命周期方法 | 是否反编译 | 是否 final |
|---|---|---|---|---|---|
| 1 | `kd.bos.form.plugin.CodeRulePlugin` | - | - | 否 · 平台 | - |
| 2 | `dev.tpl.base.kd.bos.form.plugin.templatebaseedit` | - | - | 否 · 平台 | - |
| 3 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | HRDataBaseEdit | afterBindData / afterLoadData / beforeDoOperation / afterDoOperation / preOpenForm / beforeClosed | 否（hbp 模板）| 否 |
| 4 | `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit` | HRCoreBaseBillEdit | - | 否（hbp 模板）| 否 |
| 5 | `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | AbstractFormPlugin | afterBindData | 否（hbp 模板）| 否 |
| 6 | **`kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin`** | HRDataBaseEdit | afterCreateNewData / afterBindData / registerListener / preOpenForm / beforeDoOperation / afterDoOperation / beforeClosed / propertyChanged | **是 · 378 行** | **final L102** |
| 7 | `kd.bos.base.bdversion.BdVersionListPlugin` | - | - | 否 · 平台 | - |
| 8 | `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin` | - | - | 否 · 平台 | - |
| 9 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList` | HRDataBaseList | beforeBindData / preOpenForm | 否（hbp 模板）| 否 |
| 10 | `kd.hr.hbp.formplugin.web.template.HRBasedataLogList` | HRDataBaseList | beforeBindData / beforeDoOperation | 否（hbp 模板）| 否 |
| 11 | **`kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin`** | AbstractBUListPlugin | - | **是 · 20 行** | **final L13**（薄壳 · 仅 getCtrlBUFieldSet）|
| 12 | **`kd.hr.haos.formplugin.web.structures.StructProjectListPlugin`** | HRDataBaseList | afterDoOperation / closedCallBack | **是 · 225 行** | **final L76** |

### 3.2 操作插件（OperationPlugin · 12 个）

| # | ClassName | 父类 | 生命周期方法 | 是否反编译 | 是否 final |
|---|---|---|---|---|---|
| 13 | `kd.bos.business.plugin.CodeRuleOp` | - | - | 否 · 平台 | - |
| 14 | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | - | - | 否 · 平台 | - |
| 15 | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | AbstractOperationServicePlugIn | onAddValidators / beforeExecuteOperationTransaction | 否（hbp）| 否 |
| 16 | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction / afterExecuteOperationTransaction | 否（hbp）| 否 |
| 17 | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction | 否（hbp）| 否 |
| 18 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction | 否（hbp）| 否 |
| 19 | **`kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp`** | HRDataBaseOp | onAddValidators / beginOperationTransaction | **是 · 183 行 · 业务核心** | **final L69** |
| 20 | `kd.bos.coderule.CodeRuleDeleteOp` | - | - | 否 · 平台 | - |
| 21 | **`kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP`** | HRDataBaseOp | onAddValidators / onPreparePropertys / beginOperationTransaction | **是 · 56 行** | **final L30** |
| 22 | **`kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp`** | HRDataBaseOp | onAddValidators / beginOperationTransaction | **是 · 28 行** | **final L19** |
| 23 | **`kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp`** | HRDataBaseOp | onAddValidators / onPreparePropertys / beginOperationTransaction | **是 · 37 行** | **final L22** |
| 24 | **`kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp`** | HRDataBaseOp | onAddValidators / onPreparePropertys | **是 · 31 行** | **final L21** |

## 4. 同生命周期方法的执行顺序（实证）

| 方法 | 插件个数 | 顺序（按 _auto_plugin_registry.md）|
|---|---|---|
| `afterBindData` | 3 | 3 → 5 → 6 |
| `afterCreateNewData` | 1 | 6 |
| `afterDoOperation` | 3 | 3 → 6 → 12 |
| `afterExecuteOperationTransaction` | 1 | 16 |
| `afterLoadData` | 1 | 3 |
| `beforeBindData` | 2 | 9 → 10 |
| `beforeClosed` | 2 | 3 → 6 |
| `beforeDoOperation` | 3 | 3 → 6 → 10 |
| `beforeExecuteOperationTransaction` | 4 | 15 → 16 → 17 → 18 |
| `closedCallBack` | 1 | 12 |
| `onAddValidators` | 6 | 15 → 19 → 21 → 22 → 23 → 24 |
| `preOpenForm` | 3 | 3 → 6 → 9 |
| `propertyChanged` | 1 | 6 |
| `registerListener` | 1 | 6 |

> **关键观测**：`onAddValidators` 阶段 6 个插件并列跑 · ISV 加 Validator 时**不会**与标品冲突（每个 OP 独立 addValidator）。`beforeExecuteOperationTransaction` 阶段 4 个 hbp 插件并列跑 · ISV 加新 OP 走 `HRDataBaseOp` 也是并列。

## 5. SDK 注解白名单（继承指南）

### 5.1 可继承的父类（有 SDK 注解）

| 父类 | 注解 | 适用 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.HRDataBaseEdit` | @SdkPlugin | FormPlugin · 表单页扩展（CS-03 · CS-07 关联）|
| `kd.hr.hbp.formplugin.web.HRDataBaseList` | @SdkPlugin | ListPlugin · 列表页扩展 |
| `kd.hr.hbp.opplugin.web.HRDataBaseOp` | @SdkPlugin | OP · 操作扩展（CS-02 / CS-04 / CS-05 / CS-07）|
| `kd.bos.entity.validate.AbstractValidator` | @SdkPublic | Validator · 校验器（所有 CS 都用）|
| `kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin` | abstract（公开）| 左树 BU 列表 · 替换 StructProjectBUListPlugin |
| `kd.bos.form.plugin.AbstractFormPlugin` | @SdkPublic | 通用 FormPlugin（HR 域优先用 HRDataBaseEdit）|
| `kd.bos.list.plugin.AbstractListPlugin` | @SdkPublic | 通用 ListPlugin（HR 域优先用 HRDataBaseList）|
| `kd.bos.entity.plugin.AbstractOperationServicePlugIn` | @SdkPublic | 通用 OP（HR 域优先用 HRDataBaseOp）|

### 5.2 禁继承清单（PR-001 · 全 final）

| 类 | 行数 | 反编译 final 实证 |
|---|---|---|
| `StructProjectEditPlugin` | 378 | L102 `public final class` |
| `StructProjectListPlugin` | 225 | L76 `public final class` |
| `StructProjectSaveOp` | 183 | L69 `public final class` |
| `StructProjectDeleteOP` | 56 | L30 `public final class` |
| `StructProjectDisableOp` | 28 | L19 `public final class` |
| `StructProjectEnableOp` | 37 | L22 `public final class` |
| `StructOrgPermSaveOp` | 31 | L21 `public final class` |
| `StructProjectBUListPlugin` | 20 | L13 `public final class` |

> **同样禁继承（无 SDK 注解 · 跨域抓的 service 类）**：
> - `OrgChangeDetailService`（591 行 · 业务 service · 无 @Sdk* · ISV 不可继承 · 可调用其 public 方法 · 但反编译没看到）
> - `BatchAdminOrgNewOpService`（207 行 · `public class` 但 admin_org 域内部 · 不在 SDK 白名单）
> - `AdminChangeMsgService`（124 行 · 同上）
> - `AdminOrgChangeLogService`（226 行 · 同上）
> - `BatchOrgDeleteOpService`（89 行 · 同上）
> - `BatchOrgExecuteOpService`（42 行 · 同上）

## 6. ISV 最常覆盖的扩展点（Top 5）

### 6.1 `onAddValidators@save / submit` · 业务校验

- 推荐父类：`HRDataBaseOp` + `AbstractValidator`
- 关联 CS：CS-02（roottype 变更校验）+ CS-04（删除反向引用校验）
- 占用方法：`onAddValidators`（与标品 6 个并列）

### 6.2 `propertyChanged` · 字段联动

- 推荐父类：`HRDataBaseEdit`
- 关联 CS：CS-03（应用领域 → 权限模板联动）
- 占用方法：`propertyChanged`（与标品 1 个并列）
- 必须包 `getModel().beginInit() / endInit()`（PR-004）

### 6.3 `afterExecuteOperationTransaction@save / disable / enable` · BEC 发布

- 推荐父类：`HRDataBaseOp`
- 关联 CS：CS-05（BEC 自建发布方）
- 占用方法：`afterExecuteOperationTransaction`（与标品 HRBaseDataLogOp 并列）

### 6.4 `setFilter` · 列表过滤定制

- 推荐父类：`HRDataBaseList`
- 关联 CS：（haos_structure CS-08 同模式）
- 占用方法：`setFilter`（与标品 StructProjectListPlugin.setFilter 并列）

### 6.5 ISV 扩展元数据加字段

- 不走继承 · 走 ISV 扩展元数据机制
- 关联 CS：CS-01 / CS-06
- 物理列共用 · 元数据隔离（CS-06）

## 7. 反向引用扩展点（haos_structproject 被引时）

| 来源 | 引用字段 | 业务 | ISV 扩展机会 |
|---|---|---|---|
| `haos_structure.relyonstructproject` | BasedataField → haos_structproject | 实例引方案 | CS-04 反向引用校验跨表查 |
| `haos_structproject.relyonstructproject` | BasedataField → haos_structproject（自引用）| 方案级链式依赖 | 加链式依赖校验（避免循环引用）|

## 8. 扩展点选型决策表（业务诉求 → 扩展点）

| 业务诉求 | 扩展点 | 推荐 CS |
|---|---|---|
| 加字段 | ISV 扩展元数据 | CS-01 / CS-06 |
| 校验保存数据 | onAddValidators@save | CS-02 |
| 校验启用前置 | onAddValidators@enable | CS-07 |
| 校验删除反向引用 | onAddValidators@delete + delete_project | CS-04 |
| 字段联动 | propertyChanged | CS-03 |
| 通知下游变更 | afterExecuteOperationTransaction + BEC | CS-05 |
| 列表过滤定制 | setFilter（new ListPlugin extends HRDataBaseList）| 参考 haos_structure CS-08 |
| 修改互斥锁逻辑 | （不建议改 · 平台层）| - |
| 改维护架构跳转 | afterDoOperation + showForm 自定义 | 业务方真要再开 |

## 9. 反模式（绝对不要做）

- ❌ 继承 8 个 final 类中的任何一个 → 编译报错
- ❌ 在 OP 里 getModel().setValue() → NPE（PR-003）
- ❌ propertyChanged 不包 beginInit/endInit → 死循环（PR-004）
- ❌ 套用 hjm 的 EventServiceHelper.triggerEventSubscribeJobs 假装"标品发了 BEC" → grep 14 类 0 命中
- ❌ 在 t_haos_structproject 物理表直接 SQL 改 → 绕过 BdVersion 历史
- ❌ 给 haos_structproject 加字段又给 haos_structure 也绑同字段 → 物理列变共享（CS-06 思路 A 失效）
- ❌ 改 otclassify 业务硬编码 → 列表过滤掉看不见

## 10. 来源追溯

- 24 plugins：`_auto_plugin_registry.md`
- 14 反编译类：`knowledge/_sdk_audit/_decompiled/scenarios/haos_structproject/*.java`
- 52 opKey：`opkeys_index.json` + `opkeys/<opKey>.json`
- final 实证：每个反编译类源码首行 `public final class`
- SDK 注解白名单：`knowledge/_shared/platform_rules.json` PR-001 + `cosmic_hr_sdk_whitelist_audit.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin -->

## ISV 扩展指引（基于 StructProjectEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterCreateNewData`, `afterBindData`, `registerListener`, `preOpenForm`, `beforeF7Select`, `beforeDoOperation`, `afterDoOperation`, `beforeClosed`, `propertyChanged`

### 可重写方法（target.java self）
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeF7Select(kd.bos.form.field.events.BeforeF7SelectEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeClosed(kd.bos.form.events.BeforeClosedEvent)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · OrgPermHelper L35
```java
  33   
  34       public static HasPermOrgResult getHRPermOrg() {
  35 >         return OrgPermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, "haos_adminorgdetail", "47150e89000000ac");
  36       }
  37   
```

**QUERY_BUILDER** · AdOrgRepository L64
```java
  62   
  63       public DynamicObject loadByBoIdAndDate(Long boId, Date date) {
  64 >         QFilter qFilter = new QFilter("boid", "=", (Object)boId);
  65           qFilter.and("bsed", "<=", (Object)date).and("bsled", ">=", (Object)date);
  66           qFilter.and(QFilterHelper.createValidHisVersionFilter());
```

**READ_VIA_HELPER** · AdOrgRepository L122
```java
 120           list.add(AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER);
 121           HRBaseServiceHelper adminOrgServiceHelper = new HRBaseServiceHelper("haos_adminorgdetail");
 122 >         DynamicObject[] idDyns = adminOrgServiceHelper.queryOriginalArray("sourcevid", list.toArray(new QFilter[0]));
 123           List ids = Arrays.stream(idDyns).map(dyn -> dyn.getLong("sourcevid")).collect(Collectors.toList());
 124           QFilter idFilter = new QFilter("id", "in", ids);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

## ISV 扩展指引（基于 StructProjectBUListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public java.util.Set<java.lang.String> getCtrlBUFieldSet()`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectListPlugin -->

## ISV 扩展指引（基于 StructProjectListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.structures.StructProjectListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StaffCommonService L361
```java
 359   
 360       public static AuthorizedOrgResult getOrgAuth() {
 361 >         Long userId = RequestContext.get().getCurrUserId();
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · BaseDataHelper L34
```java
  32   
  33       public static QFilter getBaseDataFilter(String entityName, long orgId) {
  34 >         return BaseDataServiceHelper.getBaseDataFilter((String)entityName, (Long)orgId);
  35       }
  36   
```

**CALL_CROSS_SERVICE** · StaffCommonService L364
```java
 362           String permItemId = "47150e89000000ac";
 363           String appId = "homs";
 364 >         AuthorizedOrgResult permResult = (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgsF7", (Object[])new Object[]{userId, appId, "haos_staff", permItemId, "adminorgboid"});
 365           return permResult;
 366       }
```

**THROW_BIZ_EXCEPTION** · StaffExtEntryHelper L146
```java
 144               return StaffExtEntryHelper.getEntityNameByDimension(mark);
 145           }
 146 >         throw new KDBizException("DynamicDimension match error");
 147       }
 148   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructProjectListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructProjectListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp -->

## ISV 扩展指引（基于 StructProjectSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StructProjectRepository L78
```java
  76           AuthorizedStructResult permResult;
  77           ArrayList filters = Lists.newArrayListWithExpectedSize((int)3);
  78 >         long currUserId = RequestContext.get().getCurrUserId();
  79           HashMap<String, Boolean> map = new HashMap<String, Boolean>();
  80           if (showAllArea) {
```

**QUERY_BUILDER** · OrgRepository L35
```java
  33               return null;
  34           }
  35 >         filter.and(new QFilter("id", "=", orgId));
  36           return this.serviceHelper.loadDynamicObject(new QFilter[]{filter});
  37       }
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**CALL_CROSS_SERVICE** · StructProjectRepository L83
```java
  81               map.put("needToAllAreasStructProject", true);
  82           }
  83 >         if (!(permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{currUserId, "217WYC/L9U7E", "haos_adminorgdetail", "47150e89000000ac", "boid", map})).isHasAllStruct()) {
  84               QFilter structProjectFilter = new QFilter("id", "in", (Object)permResult.getAuthorizedStructs());
  85               filters.add(structProjectFilter);
```

**THROW_BIZ_EXCEPTION** · TimeLineHelper L25
```java
  23           HrApiResponse result;
  24           if (dys.length > 0 && !(result = TimelineServiceHelper.createTimespans((String)entityNumber, (DynamicObject[])dys)).isSuccess()) {
  25 >             throw new KDBizException(MessageFormat.format(ResManager.loadKDString((String)"\u5b9e\u4f53{0}\u4fdd\u5b58\u65f6\u95f4\u8f74\u6570\u636e\u5931\u8d25:{1}", (String)"TimeLineHelper_0", (String)"hrmp-haos-business", (Object[])new Object[0]), entityNumber, result.getErrorMessage()));
  26           }
  27       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP -->

## ISV 扩展指引（基于 StructProjectDeleteOP 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectDeleteOP -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp -->

## ISV 扩展指引（基于 StructProjectDisableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp -->

## ISV 扩展指引（基于 StructProjectEnableOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `onPreparePropertys`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.otherframework.StructProjectEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp -->

## ISV 扩展指引（基于 StructOrgPermSaveOp 真实证）

> FQN: `kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.opplugin.web.structproject.StructOrgPermSaveOp -->
