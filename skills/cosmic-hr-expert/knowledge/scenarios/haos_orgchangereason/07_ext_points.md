# 扩展点全图 · 行政组织变动原因（haos_orgchangereason）

> **状态**: 🟢 基于 `_auto_plugin_registry.md` 35 plugin + 反编译 2 类 + opKey 49 实证
> **视角**: 本场景下会触发的所有扩展点（按 ISV 推荐场景顺序）
> **完整档案**: 单插件深度参考 `_auto_plugin_registry.md` / `_auto_plugin_semantics.md`

---

## 一、所有标品插件清单（实抓 35 个）

| 类别 | 数量 | 主要类 |
|---|---|---|
| 场景特有 (`kd.hr.haos.*`) | **2** | ChangeReasonListPlugin / BaseDataBuOp |
| HR 通用 (`kd.hr.hbp.*`) | 8 | HRBaseDataTplEdit / HRBaseDataTplList / HRBaseDataImportEdit / HRBasedataLogList / HRHiesButtonSwitchPlugin / HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp |
| 苍穹基础框架 (`kd.bos.*`) | 25 | CodeRulePlugin / CodeRuleOp / CodeRuleDeleteOp / BdVersionListPlugin / BdVersionSaveServicePlugin / BdCtrl* / BaseData* |

> 📌 详细清单见 [_auto_plugin_registry.md](_auto_plugin_registry.md) · **执行顺序**：同一生命周期方法按 RowKey 顺序执行（ISV 自定义可插在标品之前/之后）。

> 📌 **跟配对场景 haos_changescene 的对比**：haos_changescene 有 3 个场景特有类（ChangeSceneEditPlugin / ChangeSceneListPlugin / ChangeSceneSaveOp · 共 139 行）· 本场景仅 2 个（ChangeReasonListPlugin / BaseDataBuOp · 共 50 行）· 业务深度更轻量。

---

## 二、按 ISV 推荐扩展场景排序

### 2.1 🔌 `onAddValidators@delete` ⭐ CS-02 入口

- **宿主**：`haos_orgchangereason` (主表) · operation=`delete`
- **时机**：删除前 · 事务未开始
- **接口**：`onAddValidators(AddValidatorsEventArgs)`
- **事务阶段**：pre_transaction（PR-010 第 4 步）

**标品执行链**（4 个插件 · `opkeys/delete.json` 实证）：
1. `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` — 平台默认删除
2. `kd.bos.coderule.CodeRuleDeleteOp` — 释放编码池
3. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` — 状态校验
4. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` — 日志记录

- **场景用途**：⭐ **删除前的反向引用校验**（INV-CR-06 缺口）
- **风险**：低（只读校验）
- **推荐父类**：`HRDataBaseOp`（白名单合规）
- **推荐模式**：并列挂 + onAddValidators 注册自建 AbstractValidator
- **CS 入口**：CS-02

### 2.2 🔌 `onAddValidators@disable`

- **宿主**：`haos_orgchangereason` · operation=`disable`
- **时机**：禁用前 · 事务未开始
- **标品执行链**（2 个插件 · `opkeys/disable.json` 实证）：
  1. `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin`
  2. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- **场景用途**：禁用前的下游引用提醒
- **CS 入口**：CS-02（同源）

### 2.3 🔌 `onAddValidators@save` ⭐ 业务校验主入口

- **宿主**：`haos_orgchangereason` · operation=`save`
- **时机**：保存前 · 事务未开始
- **标品执行链**（8 个插件 · `opkeys/save.json` 实证）：
  1. `kd.bos.business.plugin.CodeRuleOp` — 编码规则
  2. `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` — 平台默认保存
  3. `kd.bos.base.bdversion.BdVersionSaveServicePlugin` — 版本管理
  4. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
  5. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
  6. `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
  7. `kd.hr.haos.opplugin.web.BaseDataBuOp` ⭐ 场景特有 · 反编译 23 行
  8. `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`

- **场景用途**：业务规则定制 (字段联动 / 跨字段校验 / ISV 字段校验)
- **风险**：中（执行链长 · 容易跟标品冲突）
- **推荐父类**：`HRDataBaseOp`
- **推荐模式**：并列挂 + 在 onAddValidators 阶段注册 ISV Validator

### 2.4 🔌 `propertyChanged@haos_orgchangereason` ⭐ CS-03 入口

- **宿主**：`haos_orgchangereason` (表单层)
- **时机**：用户改字段 · 表单层事件
- **接口**：`propertyChanged(PropertyChangedArgs)`

**标品执行链**（3 个 · `_auto_plugin_registry.md` 实证 · 都是 bos / hbp 通用）：
- `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin` — ctrlstrategy 联动可见组织
- `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` — HR 基础资料模板默认行为
- 其他 BdCtrl* 平台插件

- **场景用途**：自定义字段联动（如改 ctrlstrategy 加自定义校验 / 改 ISV 字段联动其他字段）
- **风险**：⚠ 中（PR-004 死循环防护必加）
- **推荐父类**：`HRBaseDataTplEdit` 或 `HRCoreBaseBillEdit`（白名单 · HR 基础资料同源）
- **推荐模式**：并列挂 · 标品先跑联动 · ISV 后跑校验/追加
- **CS 入口**：CS-03

### 2.5 🔌 `setFilter@haos_orgchangereason` (列表层)

- **宿主**：`haos_orgchangereason` (列表)
- **时机**：列表查询前 · QFilter 组装阶段
- **接口**：`setFilter(SetFilterEvent)`

**标品执行链**（1 个）：
- `kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin` — 强制隐藏 1010L

- **场景用途**：列表过滤定制（如按 ctrlstrategy 切分）
- **风险**：高（覆盖 setFilter 会丢标品 INV-CR-04 过滤 · 推荐改用 customParam 传值）
- **推荐父类**：`HRDataBaseList`（白名单）
- **推荐模式**：**不重写 setFilter** · 通过 customParam 透传或自建过滤控件
- **CS 入口**：（场景轻量 · 通常不需要列表过滤定制 · 业务上隐藏 1010L 已经够用）

### 2.6 🔌 `afterDoOperation` (表单层 · 操作后)

- **宿主**：`haos_orgchangereason` (表单)
- **标品执行链**（1 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`

- **场景用途**：操作后 UI 反馈（如 toast 提示 · 刷新某些控件）
- **风险**：低
- **推荐模式**：并列挂 + super 调用

### 2.7 🔌 `afterBindData` (表单层 · 数据绑定后)

- **宿主**：`haos_orgchangereason`
- **标品执行链**（2 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
  - `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`

- **场景用途**：根据数据动态显示/隐藏控件、字段必填动态调整
- **风险**：中（影响 UI 行为）

### 2.8 🔌 `beforeBindData` (列表层)

- **宿主**：`haos_orgchangereason` (列表)
- **标品执行链**（2 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
  - `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`

- **场景用途**：列表绑定数据前的预处理（如改默认排序、隐藏某些列）

### 2.9 🔌 `beforeShowBill` (列表层 · 行点开前)

- **宿主**：`haos_orgchangereason` (列表)
- **标品执行链**（1 个）：
  - `kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin` — 标题透传（薄壳）

- **场景用途**：自定义行打开前的标题/参数（如根据数据展示不同标题）
- **CS 入口**：（场景轻量 · 业务上很少需要）

---

## 三、ISV 最常覆盖的扩展点 Top 5

| # | 扩展点 | 频率 | 入口 |
|---|---|---|---|
| 1 | `onAddValidators@delete` 反向引用校验 | ⭐⭐⭐⭐⭐ | CS-02 |
| 2 | `modifyMeta(op=add field)` 加业务字段 | ⭐⭐⭐⭐⭐ | CS-01 |
| 3 | `propertyChanged@ctrlstrategy` 自定义联动 | ⭐⭐⭐ | CS-03 |
| 4 | 编码规则配置（PR-006）| ⭐⭐⭐ | CS-04 |
| 5 | 子表 + ID 生成（CS-05）| ⭐⭐ | CS-05 |

---

## 四、禁继承 / 禁调用清单（mines）

> ⚠ **禁继承 BaseDataBuOp**（haos 域场景特有薄壳类 · 标品升级会改父类签名 · ISV 编译失败）· 改用 `HRDataBaseOp` + 并列挂（PR-001）
> ⚠ **禁继承 ChangeReasonListPlugin**（haos 域场景特有薄壳类 · 同上）· 改用 `HRDataBaseList` + 并列挂
> ⚠ **禁继承 ListOrderCommonPlugin**（haos 域内部类 · 不在 SDK 白名单）· 改用 `HRDataBaseList`
> ⚠ **禁继承 AbsOrgBaseOp**（hjm/admin_org 实证 forbidden · 不在 HR SDK 白名单）· 改用 `HRDataBaseOp`
> ⚠ **禁继承 HisModelOPCommonPlugin / HisUniqueValidateOp / HisModelFormCommonPlugin / HisModelListCommonPlugin**（@SdkInternal 平台历史版本内部类 · ISV 不得继承 · 本场景非时序也不需要）
> ⚠ **禁继承 BdCtrlStrtgyShowLogicPlugin / BdCtrlStrtgyShowLogicListPlugin**（bos 平台内部插件 · ISV 不应继承 bos.* 内部类）· 改用 HRCoreBaseBillEdit / HRDataBaseList 并列挂

---

## 五、扩展接入约束

### 5.1 RowKey 顺序原则（PR-002）

| 场景 | 推荐 RowKey | 理由 |
|---|---|---|
| 拦截删除（CS-02）| 1（早于标品 4 个）| 在标品 BaseDataDeletePlugin 之前拦 |
| 拦截保存校验（ISV 字段必填等）| 1-2（早于标品 8 个）| 在 CodeRuleOp 之前拦 |
| 联动追加（CS-03）| ≥ 100（晚于标品）| 标品先跑联动可见性 · ISV 后跑校验 |
| 列表过滤补充 | ≥ 100（晚于标品）| 不动标品 setFilter · 加 customParam |

### 5.2 父类选择决策树

```
ISV 要扩展什么？
├─ OP 操作类（delete/save/disable/enable 等的业务校验）
│    └─ 推荐父类：kd.hr.hbp.opplugin.web.HRDataBaseOp
│        ⚠ 不要继承 BaseDataBuOp / AbsOrgBaseOp
├─ 表单 BillForm 类（编辑表单的 propertyChanged / afterBindData）
│    └─ 推荐父类：
│        - kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit（HR 基础资料模板 · 跟标品同源）
│        - kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit（更通用）
│        ⚠ 不要继承 ChangeReasonListPlugin / BdCtrlStrtgyShowLogicPlugin（bos 内部）
├─ 列表 ListForm 类（setFilter / beforeBindData）
│    └─ 推荐父类：kd.hr.hbp.formplugin.web.template.HRBaseDataTplList 或 HRDataBaseList
│        ⚠ 不要继承 ChangeReasonListPlugin / ListOrderCommonPlugin
├─ Validator 类（onAddValidators 注册的校验器）
│    └─ 推荐父类：kd.bos.entity.validate.AbstractValidator
└─ FormPlugin 通用（按钮 click / 表单事件）
     └─ 推荐父类：kd.bos.form.plugin.AbstractFormPlugin（极少 · 通常都该走 HRBaseDataTplEdit/HRCoreBase*）
```

---

## 六、本场景特有的扩展约束

### 约束 A · 反编译实证：2 类全是薄壳

- `ChangeReasonListPlugin` 仅 27 行 · 只做 setFilter / beforeShowBill
- `BaseDataBuOp` 仅 23 行 · 只做 onAddValidators 注册 CtrlStrategyValidator

→ **业务深度极有限** · ISV 扩展时不要"嫁接复杂业务" · 多数情况需要在 CtrlStrategyValidator 内部做的事 · 走业务自建插件（CS-03）而非这 2 个薄壳的扩展。

### 约束 B · 没有 MulBasedata 子表 · 字段扩展更简单

- 不像 haos_changescene 有 changereason / changeoperat 两个 MulBasedata 子表 · 本场景**主表只读写 28 字段**
- 加字段（CS-01）只能加到主实体 `haos_orgchangereason` · 没有 entry 子表选项
- ISV 想要明细数据（如"适用业务线明细"）必须 modifyMeta 自建 entry 子表（CS-05）

### 约束 C · 出厂数据 isvCanModify=false 字段不要写

INV-CR-03 列表的字段 · 即使你的插件能拿到 dataEntity 也不要 set · 平台会忽略或抛异常。

### 约束 D · ctrlstrategy 是平台级行为 · 不要替代

- ctrlstrategy 联动 createorg/org/useorg 是 bos 平台 BdCtrlStrtgyShowLogicPlugin 的统一逻辑
- ISV 不应替代或重写这个联动 · 应该并列挂追加自己的校验/逻辑

---

## 七、示例：CS-02 的扩展点配置（开发平台）

```
苍穹开发平台
└─ 表单管理 → haos_orgchangereason
    └─ 操作 → 找到 opKey=delete
        └─ 扩展插件 → 新增
            ├─ ClassName: com.kingdee.${ISV_FLAG}.haos.opplugin.web.ChangeReasonRefCheckOp
            ├─ targetType: OPERATION（大写枚举 · 平台规则 #10）
            ├─ RowKey: 1
            └─ 启用：✅
        └─ 操作 → 找到 opKey=disable
            └─ 同上 · 同一插件类（onAddValidators 都会触发）
```

---

## 八、跟 haos_changescene 扩展点对比

| 扩展点 | haos_orgchangereason（本场景）| haos_changescene |
|---|---|---|
| onAddValidators@save 场景特有类 | BaseDataBuOp（23 行）| ChangeSceneSaveOp（57 行）|
| onPreparePropertys 显式声明 | 无（标品默认）| orgchangetype + changeoperat |
| beginOperationTransaction 业务逻辑 | 无 | 导入路径兜底联动 changeoperat |
| propertyChanged 场景特有类 | 无（仅平台 BdCtrl*）| ChangeSceneEditPlugin（37 行 · 联动 changeoperat）|
| setFilter 场景特有类 | ChangeReasonListPlugin（27 行）| ChangeSceneListPlugin（45 行）|
| 列表过滤条数 | 1（id != 1010）| 3（otclassify + id != 1070 + orinumber != 1100_S）|

→ **本场景扩展面更窄 · 业务复杂度更低** · ISV 扩展所需触达的标品表面更少。
→ 但**ISV 责任范围一致**（删除前置校验 / 加字段 / 联动校验）· 不能因为薄壳就放松 CS-02。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin -->

## ISV 扩展指引（基于 ChangeReasonListPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · ChangeReasonListPlugin L21
```java
  19       public void setFilter(SetFilterEvent setFilterEvent) {
  20           super.setFilter(setFilterEvent);
  21 >         setFilterEvent.getQFilters().add(new QFilter("id", "!=", (Object)1010L));
  22       }
  23   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.ChangeReasonListPlugin -->

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
