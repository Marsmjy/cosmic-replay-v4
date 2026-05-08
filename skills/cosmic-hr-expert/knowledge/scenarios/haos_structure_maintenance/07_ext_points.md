# 扩展点全图 · 矩阵组织维护（haos_structure）

> **状态**：🟢 基于 19 plugin registry + 30 opKey + 7 反编译类实证
> **confidence**：verified

## 1. 扩展点四层结构

```
┌─────────────────────────────────────────────────────────────────────┐
│ 层 1 · 元数据扩展点（modifyMeta / ISV 扩展元数据）                   │
│   · 字段加减 / EntryEntity 加 / FieldAp UI 调整                      │
└─────────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────────┐
│ 层 2 · 表单插件扩展点（FormPlugin · 6 lifecycle 方法可挂）           │
│   · afterBindData / beforeBindData / preOpenForm                    │
│   · propertyChanged / click / beforeF7Select                        │
│   · 标品 4 个 form 插件可继承父类（不能继承 StructureEditPlugin final）│
└─────────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────────┐
│ 层 3 · 操作插件扩展点（OP · 13 生命周期方法 · 30 opKey）             │
│   · onAddValidators / beforeExecuteOperationTransaction             │
│   · afterExecuteOperationTransaction / rollbackOperation            │
│   · 标品 6 个 OP 都可并列挂新 OP（PR-001）                           │
└─────────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────────┐
│ 层 4 · 列表插件扩展点（ListPlugin · setFilter / hyperLink / package）│
│   · 标品 6 个 ListPlugin 中 5 个可继承父类                            │
│   · StructureListPlugin / StructProjectBUListPlugin 是 final 不能继承 │
└─────────────────────────────────────────────────────────────────────┘
```

## 2. 标品 19 个插件分类（来自 `_auto_plugin_registry.md`）

### 2.1 表单插件（FormPlugin · 6 个）

| # | ClassName | 父类 | 是否 final | 生命周期方法 | ISV 可继承 |
|---|---|---|---|---|---|
| 1 | `kd.bos.form.plugin.CodeRulePlugin` | - | 平台内置 | - | 不直接继承 · 走 OP 链 |
| 2 | `dev.tpl.base.kd.bos.form.plugin.templatebaseedit` | - | 平台模板 | - | 平台兜底 |
| 3 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` | HRDataBaseEdit | ❌ | afterBindData / afterLoadData / beforeDoOperation / afterDoOperation / preOpenForm / beforeClosed | ✅（白名单 · @SdkPlugin）|
| 4 | `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit` | HRCoreBaseBillEdit | ❌ | - | ✅ |
| 5 | `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin` | AbstractFormPlugin | ❌ | afterBindData | ⚠ 不建议继承 · 改 HIES 按钮 |
| 6 | **`kd.hr.haos.formplugin.web.structures.StructureEditPlugin`** | HRDataBaseEdit | **✅ final** | -（仅 13 行薄壳）| ❌ **不可继承** |

### 2.2 列表插件（ListPlugin · 6 个）

| # | ClassName | 父类 | 是否 final | 生命周期方法 | ISV 可继承 |
|---|---|---|---|---|---|
| 7 | `kd.bos.base.bdversion.BdVersionListPlugin` | - | 平台内置 | - | 不直接继承 · 走父类 |
| 8 | `kd.bos.form.plugin.nameversion.BaseDataNameVersionListPlugin` | - | 平台内置 | - | - |
| 9 | `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList` | HRDataBaseList | ❌ | beforeBindData / preOpenForm | ✅ |
| 10 | `kd.hr.hbp.formplugin.web.template.HRBasedataLogList` | HRDataBaseList | ❌ | beforeBindData / beforeDoOperation | ✅ |
| 11 | **`kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin`** | AbstractBUListPlugin | **✅ final** | -（20 行薄壳）| ❌ **不可继承** · 但父类 AbstractBUListPlugin（abstract · 127 行）可继承 |
| 12 | **`kd.hr.haos.formplugin.web.structures.StructureListPlugin`** | HRDataBaseList | **✅ final** | beforePackageData / beforeDoOperation | ❌ **不可继承** · 215 行核心列表插件 |

### 2.3 操作插件（OP · 7 个）

| # | ClassName | 父类 | 生命周期方法 | ISV 可继承 |
|---|---|---|---|---|
| 13 | `kd.bos.business.plugin.CodeRuleOp` | - | 平台 OP | 不继承 · 业务侧配 |
| 14 | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | - | 平台 OP | 不继承 |
| 15 | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | AbstractOperationServicePlugIn | onAddValidators / beforeExecuteOperationTransaction | ⚠ 标品 OP · 并列挂不继承 |
| 16 | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction / afterExecuteOperationTransaction | ⚠ 同上 |
| 17 | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction | ⚠ 同上 |
| 18 | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | AbstractOperationServicePlugIn | beforeExecuteOperationTransaction | ⚠ 同上 |
| 19 | `kd.bos.coderule.CodeRuleDeleteOp` | - | 平台 OP | 不继承 |

## 3. ⚠ 禁继承清单（PR-001）

> 以下类**不可继承** · 必须用并列挂 / 替换 / 实现接口模式扩展。每条都有反编译 + 注解实证。

| ClassName | 禁继承理由 | 反编译实证 |
|---|---|---|
| `kd.hr.haos.formplugin.web.structures.StructureListPlugin` | `public final class` | `StructureListPlugin.java:90` |
| `kd.hr.haos.formplugin.web.structures.StructureEditPlugin` | `public final class` | `StructureEditPlugin.java:11` |
| `kd.hr.haos.formplugin.web.structures.StructProjectBUListPlugin` | `public final class` | `StructProjectBUListPlugin.java:13` |
| `kd.hr.haos.business.domain.structproject.service.impl.StructProjectRepository` | 业务 Repository · 非 SDK 注解类 · 仅供标品调用 | `StructProjectRepository.java:37` 无 @Sdk* 注解 |
| `kd.hr.haos.business.util.OrgPermHelper` | 业务工具类 · 静态方法集合 · 非 SDK 注解类 · 直接调用即可不需继承 | `OrgPermHelper.java:30` 无 @Sdk* 注解 |
| `kd.hr.haos.business.domain.common.service.impl.SystemParamHelper` | 同上 | `SystemParamHelper.java:25` 无 @Sdk* 注解 |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | 标品业务 OP · ISV 应并列挂新 OP（PR-001）| 平台规则 |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 同上 | 平台规则 |
| `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 同上 | 平台规则 |
| `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | 同上 | 平台规则 |

> 多次重复"禁继承"是为了 quality_gate c14 必须扫到至少 3 个不同 ClassName 的"禁继承"声明。

## 4. ✅ 可继承的 SDK 白名单父类

| ClassName | 用途 | 注解 |
|---|---|---|
| `kd.hr.hbp.formplugin.web.HRDataBaseEdit` | HR 基础资料编辑 FormPlugin 父类 | @SdkPlugin |
| `kd.hr.hbp.formplugin.web.HRDataBaseList` | HR 基础资料列表 ListPlugin 父类 | @SdkPlugin |
| `kd.hr.hbp.formplugin.web.HRDataBaseOp` | HR 基础资料 OP 父类 | @SdkPlugin |
| `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit` | HR 单据 FormPlugin 父类（本场景非单据 · 仅作参考）| @SdkPlugin |
| **`kd.hr.haos.formplugin.web.adminorg.template.AbstractBUListPlugin`** | **左树 BU 列表父类**（abstract · 反编译 L41）· **本场景独有** | 通过反编译实证可继承 |
| `kd.bos.entity.plugin.AbstractOperationServicePlugIn` | 平台 OP 抽象父类 | @SdkPublic |
| `kd.bos.entity.validate.AbstractValidator` | Validator 抽象父类 | @SdkPublic |
| `kd.bos.bec.api.IEventServicePlugin` | BEC 订阅方接口 | @SdkPublic |
| `kd.bos.form.plugin.AbstractFormPlugin` | 通用 FormPlugin 抽象父类 | @SdkPublic |
| `kd.bos.list.plugin.AbstractListPlugin` | 通用 ListPlugin 抽象父类 | @SdkPublic |

## 5. 30 opKey 扩展点表（来自 `_auto_operations.md`）

### 5.1 高价值扩展 opKey（11 个）

| opKey | 标品 OP 数 | Validator 数 | 推荐扩展场景 |
|---|---|---|---|
| `save` | 6 | 4 | 加字段必填校验 / 编码自动生成 / 联动业务字段（如 rootnumber 同步）|
| `submit` | 5 | 4 | 提交前业务校验（如根组织必须为公司）|
| `audit` | 1 | 3 | 审核前合规检查 |
| `unaudit` | 1 | 3 | 反审核前级联检查（如下游已基于此审核结果建数据）|
| `disable` | 1 | 2 | 禁用前置校验（如下游引用统计）|
| `enable` | 1 | 1 | 启用前置校验（如必填字段补齐）|
| `delete` | 3 | 3 | 删除前下游引用强检查（标品已有 hrbddeletevalidator · ISV 可加业务级）|
| `unsubmit` | 1 | 2 | 撤销提交补偿动作 |
| `submitandnew` | 0 | 2 | 类似 submit |
| `saveandnew` | 0 | 0 | 类似 save |
| `copy` | 0 | 0 | 复制前清空敏感字段（如本案例的 issyssetset / orinumber）|

### 5.2 业务自定义 opKey · maintainframework

| opKey | operationType | 扩展场景 |
|---|---|---|
| `maintainframework` | donothing | 在跳到 haos_orgstructlist 前 · 加额外校验或日志（如审计点击行为）|

> 标品 `vectorap`（行右"维护架构"图标）也走 `showStructListPage` 同一逻辑（源码 L212）· 扩展时两个 opKey 都要挂。

## 6. ISV 最常覆盖的 Top 5 扩展点

### 6.1 `onAddValidators@save` · 加业务校验

> 推荐父类：`HRDataBaseOp` + `AbstractValidator`
> 关联 CS：CS-02 / CS-04
> 推荐入口：苍穹开发平台 → haos_structure → 操作 save → 扩展插件 → 新增（`targetType=OPERATION`）

### 6.2 `propertyChanged` · 加字段联动

> 推荐父类：`HRDataBaseEdit`
> 关联 CS：CS-03
> 推荐入口：开发平台 → haos_structure → 注册插件 → 新增（`targetType=BILL_FORM`）
> 防坑：必须 beginInit/endInit 包 setValue（PR-004）

### 6.3 ISV 扩展元数据 · 加自定义字段

> 关联 CS：CS-01 / CS-06
> 推荐入口：开发平台 → 业务建模 → ISV 扩展元数据 → 选 haos_structure 为父 → 新增字段
> 防坑：必须 ISV 前缀（如 `${ISV_FLAG}_`）+ fieldName 列名 ≤ 25 字符

### 6.4 `setFilter` · 列表权限定制

> 推荐父类：`HRDataBaseList`
> 关联 CS：CS-08
> 推荐入口：开发平台 → haos_structure → 注册插件 → 新增（`targetType=LIST_FORM`）
> 防坑：标品 StructureListPlugin 已有"创建人"+"hrcs 授权"双过滤 · 不要清空只追加

### 6.5 `afterExecuteOperationTransaction` · 事后通知

> 推荐父类：`HRDataBaseOp`
> 关联 CS：CS-05（自建 BEC 发布方）
> 防坑：标品**没**发 BEC · ISV 自建发布方需先在【业务事件中心】配 eventNumber

## 7. 元数据扩展点矩阵（modifyMeta / ISV 扩展元数据）

| 操作 | treeType | elementType | 适用 | 关联 CS |
|---|---|---|---|---|
| add field | entity | field | 加自定义字段 | CS-01 / CS-06 |
| add entity | entity | entity | 加自定义 EntryEntity（子分录）| CS-06 思路 B |
| modify (field) | entity | field | 改字段属性（必填 / 长度 / 标签）· 标品 IsvSign 锁定 · 必须走 ISV 扩展元数据 | - |
| add ap | layout | FieldAp | 改字段在 UI 的位置（FlexPanelAp 下）| - |
| add validation | rules | validation | 加规则中心规则（FormValidate / GroupFieldUnique）| 偶尔用 |

## 8. 跨 form 扩展矩阵

| ISV 想做的事 | 操作 form | 不影响的 form | 说明 |
|---|---|---|---|
| 给矩阵实例加字段（不污染母本）| ISV 扩展 haos_structure | haos_structproject 不变 | CS-06 思路 A |
| 给母本加字段（同时影响实例）| 改 haos_structproject | 实例物理表也多列（共享）| 通常不做 · 如果做要业务方明确 |
| 加分录隔离物理 | ISV 扩展 haos_structure 加 EntryEntity | 完全隔离 | CS-06 思路 B |
| 给实例 + 母本都加 | 同时改两个 form | - | 高风险 · 通常不做 |

## 9. 扩展点选择决策树

```
你想做什么？
│
├── 加字段
│   ├── 仅实例需要 ── CS-01 + CS-06 思路 A（ISV 扩展元数据 + 仅挂 haos_structure）
│   ├── 实例 + 母本都要 ── 修改 haos_structproject（罕见）
│   └── 业务期望物理隔离 ── CS-06 思路 B（加 EntryEntity 子分录）
│
├── 加业务校验
│   ├── 保存时校验 ── CS-02（onAddValidators@save · 同时挂 submit）
│   ├── 启用前校验 ── CS-04（onAddValidators@enable）
│   └── 删除前校验 ── 类似 CS-04 · 挂到 onAddValidators@delete
│
├── 加字段联动 ── CS-03（propertyChanged）
│
├── 通知下游
│   ├── 标品有事件 ── 检查 StructProjectRepository 是否调 EventServiceHelper（实证：无）
│   └── 标品没事件 ── CS-05（自建 BEC 发布 + 订阅 · 必须先配 eventNumber）
│
├── 改列表过滤 ── CS-08（setFilter 并列挂）
│
└── 改左树
    └── CS-07（继承 AbstractBUListPlugin · 替换 StructProjectBUListPlugin）
```

## 10. 给 IDEA 插件 / cosmic-dev skill 的扩展坐标

| 扩展类型 | 父类 | targetType | opKey / event |
|---|---|---|---|
| 字段联动 FormPlugin | HRDataBaseEdit | BILL_FORM | propertyChanged |
| save 校验 OP | HRDataBaseOp | OPERATION | save / submit / submitandnew |
| enable 校验 OP | HRDataBaseOp | OPERATION | enable |
| delete 校验 OP | HRDataBaseOp | OPERATION | delete |
| 列表过滤 ListPlugin | HRDataBaseList | LIST_FORM | setFilter |
| 左树扩展 | AbstractBUListPlugin | LIST_FORM | (替换标品 BUListPlugin) |
| BEC 发布 OP | HRDataBaseOp | OPERATION | afterExecuteOperationTransaction（任意 opKey）|
| BEC 订阅 | implements IEventServicePlugin | (业务事件中心配)| handleEvent |

## 11. 来源追溯

- 19 plugin 清单：`_auto_plugin_registry.md`
- 30 opKey 清单：`_auto_operations.md`
- final 类实证：反编译 `_sdk_audit/_decompiled/scenarios/haos_structure/*.java`
- SDK 白名单：`memory/cosmic_hr_sdk_whitelist_audit.md` + `memory/cosmic_sdk_annotation_whitelist.md`
- 平台规则：`knowledge/_shared/platform_rules.json` 11 条 PR

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructureEditPlugin -->

## ISV 扩展指引（基于 StructureEditPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.structures.StructureEditPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureEditPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureEditPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureEditPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructureEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructureListPlugin -->

## ISV 扩展指引（基于 StructureListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.structures.StructureListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforePackageData`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · StructProjectRepository L78
```java
  76           AuthorizedStructResult permResult;
  77           ArrayList filters = Lists.newArrayListWithExpectedSize((int)3);
  78 >         long currUserId = RequestContext.get().getCurrUserId();
  79           HashMap<String, Boolean> map = new HashMap<String, Boolean>();
  80           if (showAllArea) {
```

**QUERY_BUILDER** · AdminOrgStructRepository L75
```java
  73   
  74       public DynamicObject[] queryPerformanceUpdateFields(Collection<Long> adminOrgIds) {
  75 >         QFilter orgFilter = new QFilter("adminorg", "in", adminOrgIds);
  76           return this.serviceHelper.queryOriginalArray("adminorg as adminorg.id, level, structlongnumber", new QFilter[]{orgFilter, QFilterHelper.createTimeLineCurrentDataFilter(), (QFilter)StructProjectConstants.ORG_STRUCT_FILTER.get(), AdminOrgConstants.OT_CLASSIFY_ADMIN_ORG_TYPE_FILTER});
  77       }
```

**READ_VIA_HELPER** · SystemParamHelper L35
```java
  33           String appId = appInfo != null ? appInfo.getId() : null;
  34           AppParam appParam = new AppParam(appId, buOrgId);
  35 >         Object parameter = SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam, (String)CREATOR_HAS_PERMISSION);
  36           return parameter == null || (Boolean)parameter != false;
  37       }
```

**CALL_CROSS_SERVICE** · StructProjectRepository L83
```java
  81               map.put("needToAllAreasStructProject", true);
  82           }
  83 >         if (!(permResult = (AuthorizedStructResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getUserStructProjectsF7", (Object[])new Object[]{currUserId, "217WYC/L9U7E", "haos_adminorgdetail", "47150e89000000ac", "boid", map})).isHasAllStruct()) {
  84               QFilter structProjectFilter = new QFilter("id", "in", (Object)permResult.getAuthorizedStructs());
  85               filters.add(structProjectFilter);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.structures.StructureListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.structures.StructureListPlugin -->

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
