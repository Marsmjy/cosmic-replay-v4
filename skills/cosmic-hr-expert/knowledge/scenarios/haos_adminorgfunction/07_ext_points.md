# 扩展点全图 · 行政组织职能（haos_adminorgfunction）

> **状态**: 🟢 基于 `_auto_plugin_registry.md` 35 plugin + 反编译 2 类 + opKey 49 实证
> **视角**: 本场景下会触发的所有扩展点（按 ISV 推荐场景顺序）
> **完整档案**: 单插件深度参考 `_auto_plugin_registry.md` / `rules_chain_all.json`

---

## 一、所有标品插件清单（实抓 35 个）

| 类别 | 数量 | 主要类 |
|---|---|---|
| 场景特有 (`kd.hr.haos.*`) | **2** | ListOrderCommonPlugin / BaseDataBuOp |
| HR 通用 (`kd.hr.hbp.*`) | 8 | HRBaseDataTplEdit / HRBaseDataTplList / HRBaseDataImportEdit / HRBasedataLogList / HRHiesButtonSwitchPlugin / HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HRBaseOriginalOp |
| 苍穹基础框架 (`kd.bos.*`) | 25 | CodeRulePlugin / CodeRuleOp / CodeRuleDeleteOp / BdVersionListPlugin / BdVersionSaveServicePlugin / BdCtrl* / BaseData* |

> 📌 详细清单见 [_auto_plugin_registry.md](_auto_plugin_registry.md) · **执行顺序**：同一生命周期方法按 RowKey 顺序执行（ISV 自定义可插在标品之前/之后）。

> 📌 **跟双胞胎场景 haos_orgchangereason 的对比**：haos_orgchangereason 的场景特有列表类 ChangeReasonListPlugin（27 行）做的是 setFilter 过滤 id != 1010L；本场景的 ListOrderCommonPlugin（19 行）做的是 **setOrderBy 排序**（源码 ListOrderCommonPlugin.java:1-19）。**本场景没有"保留主键过滤"需求**，关键区别之一。

---

## 二、按 ISV 推荐扩展场景排序

### 2.1 🔌 `onAddValidators@delete` ⭐ CS-02 入口

- **宿主**：`haos_adminorgfunction` (主表) · operation=`delete`
- **时机**：删除前 · 事务未开始
- **接口**：`onAddValidators(AddValidatorsEventArgs)`
- **事务阶段**：pre_transaction（PR-010 第 4 步）

**标品执行链**（4 个插件 · `rules_chain_all.json` delete opKey 实证）：
1. `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` — 平台默认删除
2. `kd.bos.coderule.CodeRuleDeleteOp` — 释放编码池
3. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` — 状态校验
4. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` — 日志记录

- **场景用途**：⭐ **删除前的反向引用校验**（INV-AF-06 标品缺口）
- **反向引用路径**：`haos_adminorg.adminorgfunction`（BasedataField 单选）
  - 查询方式：`QFilter("adminorgfunction", "=", id)` 直接外键查
  - **不走** `.fbasedataid` 子表路径（MulBasedataField 多选才用那个路径）
  - 额外约束：admin_org 是 HisModel 时序场景 · 必须加 `iscurrentversion=true`
- **风险**：低（只读校验）
- **推荐父类**：`HRDataBaseOp`（白名单合规）
- **推荐模式**：并列挂 + onAddValidators 注册自建 AbstractValidator
- **CS 入口**：CS-02

### 2.2 🔌 `onAddValidators@disable`

- **宿主**：`haos_adminorgfunction` · operation=`disable`
- **时机**：禁用前 · 事务未开始
- **标品执行链**（2 个插件 · `rules_chain_all.json` disable opKey 实证）：
  1. `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin`
  2. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
- **场景用途**：禁用前的 haos_adminorg 引用提醒
- **关键约束**：admin_org 是 HisModel 时序场景 · 反向校验需加 `iscurrentversion=true`（跟 haos_orgchangereason CS-02 的关键差异）
- **CS 入口**：CS-02（同源）

### 2.3 🔌 `onAddValidators@save` ⭐ 业务校验主入口

- **宿主**：`haos_adminorgfunction` · operation=`save`
- **时机**：保存前 · 事务未开始
- **标品执行链**（8 个插件 · `rules_chain_all.json` save opKey 实证）：
  1. `kd.bos.business.plugin.CodeRuleOp` — 编码规则
  2. `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` — 平台默认保存
  3. `kd.bos.base.bdversion.BdVersionSaveServicePlugin` — 版本管理
  4. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
  5. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
  6. `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
  7. `kd.hr.haos.opplugin.web.BaseDataBuOp` ⭐ 场景特有 · 反编译 23 行（源码 BaseDataBuOp.java:1-23）
  8. `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`

- **场景用途**：业务规则定制 (字段联动 / 跨字段校验 / ISV 字段校验)
- **风险**：中（执行链长 · 容易跟标品冲突）
- **推荐父类**：`HRDataBaseOp`
- **推荐模式**：并列挂 + 在 onAddValidators 阶段注册 ISV Validator

### 2.4 🔌 `setFilter@haos_adminorgfunction` (列表层) ⚠ 排序场景 · 与 haos_orgchangereason 关键差异

- **宿主**：`haos_adminorgfunction` (列表)
- **时机**：列表查询前 · QFilter 组装阶段
- **接口**：`setFilter(SetFilterEvent)`

**标品执行链**（1 个 · `_auto_plugin_registry.md` #16 实证）：
- `kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin`（源码 ListOrderCommonPlugin.java:1-19）
  - 执行内容：`setOrderBy("enable desc, number asc")` — 启用在前 · 编码升序

> ⚠ **关键差异（vs haos_orgchangereason）**：
> - `haos_orgchangereason` 的 ChangeReasonListPlugin 做 **setFilter** → 追加 `id != 1010L` 的 QFilter 条件
> - 本场景的 `ListOrderCommonPlugin` 做 **setOrderBy** → 只设排序规则 · 不过滤任何主键
> - **本场景没有"隐藏特定 id 记录"的需求** · ISV 重写 setFilter 不存在"丢失标品 id 过滤"的问题 · 但仍会丢失排序

- **场景用途**：列表过滤定制（如按职能分类切分）
- **风险**：中（重写会丢失标品排序效果）
- **推荐模式**：**不重写 setFilter** · 通过 customParam 透传或自建过滤控件 · 保留标品 setOrderBy

### 2.5 🔌 `propertyChanged@haos_adminorgfunction` ⭐ CS-03 入口

- **宿主**：`haos_adminorgfunction` (表单层)
- **时机**：用户改字段 · 表单层事件
- **接口**：`propertyChanged(PropertyChangedArgs)`

**标品执行链**（3 个 · `_auto_plugin_registry.md` 实证 · 都是 bos / hbp 通用）：
- `kd.bos.form.plugin.bdctrl.BdCtrlStrtgyShowLogicPlugin` — ctrlstrategy 联动可见组织
- `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` — HR 基础资料模板默认行为
- 其他 BdCtrl* 平台插件

- **场景用途**：自定义字段联动（改 ctrlstrategy 加自定义校验 / ISV 字段联动）
- **风险**：⚠ 中（PR-004 死循环防护必加）
- **推荐父类**：`HRBaseDataTplEdit` 或 `HRCoreBaseBillEdit`（白名单 · HR 基础资料同源）
- **推荐模式**：并列挂 · 标品先跑联动 · ISV 后跑校验/追加（RowKey ≥ 100）
- **CS 入口**：CS-03

### 2.6 🔌 `afterDoOperation` (表单层 · 操作后)

- **宿主**：`haos_adminorgfunction` (表单)
- **标品执行链**（1 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`

- **场景用途**：操作后 UI 反馈（如 toast 提示 · 刷新某些控件）
- **风险**：低
- **推荐模式**：并列挂 + super 调用

### 2.7 🔌 `afterBindData` (表单层 · 数据绑定后)

- **宿主**：`haos_adminorgfunction`
- **标品执行链**（2 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
  - `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`

- **场景用途**：根据数据动态显示/隐藏控件 · 字段必填动态调整
- **特殊考虑**：issyspreset=true 出厂职能受 1 条 listRules formRule 保护 · afterBindData 可追加 ISV 级只读提示 UI（INV-AF-04）

### 2.8 🔌 `beforeBindData` (列表层)

- **宿主**：`haos_adminorgfunction` (列表)
- **标品执行链**（2 个）：
  - `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
  - `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`

- **场景用途**：列表绑定数据前的预处理（如追加 ISV 过滤、改列显示）
- **补充说明**：标品 ListOrderCommonPlugin 在 setFilter 阶段已设 `enable desc, number asc` 排序 · ISV 如需改排序 · 可在 beforeBindData 里覆盖

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

> ⚠ **禁继承 BaseDataBuOp**（`kd.hr.haos.opplugin.web.BaseDataBuOp` · haos 域场景特有薄壳类 · 标品升级会改父类签名 · ISV 编译失败）· 改用 `HRDataBaseOp` + 并列挂（PR-001）
> ⚠ **禁继承 ListOrderCommonPlugin**（`kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin` · haos 域场景特有薄壳类 · 同上）· 改用 `HRDataBaseList` + 并列挂
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
| 列表过滤补充 | ≥ 100（晚于标品）| 不动标品 setOrderBy · 加 customParam |

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
│        ⚠ 不要继承 ListOrderCommonPlugin / BdCtrlStrtgyShowLogicPlugin（bos 内部）
├─ 列表 ListForm 类（setFilter / beforeBindData）
│    └─ 推荐父类：kd.hr.hbp.formplugin.web.template.HRBaseDataTplList 或 HRDataBaseList
│        ⚠ 不要继承 ListOrderCommonPlugin
├─ Validator 类（onAddValidators 注册的校验器）
│    └─ 推荐父类：kd.bos.entity.validate.AbstractValidator
└─ FormPlugin 通用（按钮 click / 表单事件）
     └─ 推荐父类：kd.bos.form.plugin.AbstractFormPlugin（极少 · 通常走 HRBaseDataTplEdit/HRCoreBase*）
```

---

## 六、本场景特有的扩展约束

### 约束 A · 反编译实证：2 类全是薄壳

- `ListOrderCommonPlugin` 仅 19 行 · 仅做 setOrderBy（源码 ListOrderCommonPlugin.java:1-19）
- `BaseDataBuOp` 仅 23 行 · 仅做 onAddValidators 注册 CtrlStrategyValidator（源码 BaseDataBuOp.java:1-23）

→ **业务深度极有限** · ISV 不要在这 2 个薄壳上"嫁接复杂业务" · 走业务自建并列挂插件（CS-03）

### 约束 B · 没有 MulBasedata 子表 · 字段扩展更简单

- 本场景主表有 27 字段（不含 ISV 扩展）
- 加字段（CS-01）只能加到主实体 `haos_adminorgfunction` · 没有 entry 子表选项
- ISV 想要明细数据（如"适用职位类别"）必须 modifyMeta 自建 entry 子表（CS-05）

### 约束 C · issyspreset=true 出厂数据有 listRules 双保护（haos_orgchangereason 没有）

- 1 条 listRules formRule 保护预置职能（INV-AF-04）· `haos_orgchangereason` 没有此规则
- isvCanModify=false 字段：即使 ISV 插件能拿到 dataEntity 也不要 set · 平台升级会覆盖或抛异常

### 约束 D · 下游引用是单选 BasedataField（不是多选 MulBasedataField）

- haos_adminorg 的 `adminorgfunction` 字段是 **BasedataField 单选**
- 物理外键：`t_haos_adminorg.fadminorgfunctionid` 直接存 id
- 反向引用查询直接走 `QFilter("adminorgfunction", "=", id)` · **不走** `.fbasedataid` 子表路径
- admin_org 是 HisModel 时序场景 · 反向引用校验必须加 `iscurrentversion=true`

### 约束 E · ctrlstrategy 是平台级行为 · 不要替代

- ctrlstrategy 联动 createorg/org/useorg 是 bos 平台 BdCtrlStrtgyShowLogicPlugin 的统一逻辑
- ISV 不应替代或重写这个联动 · 应该并列挂追加自己的校验/逻辑

---

## 七、示例：CS-02 的扩展点配置（开发平台）

```
苍穹开发平台
└─ 表单管理 → haos_adminorgfunction
    └─ 操作 → 找到 opKey=delete
        └─ 扩展插件 → 新增
            ├─ ClassName: com.kingdee.${ISV_FLAG}.haos.opplugin.web.AdminOrgFunctionRefCheckOp
            ├─ targetType: OPERATION（大写枚举 · 平台规则 #10）
            ├─ RowKey: 1
            └─ 启用：✅
        └─ 操作 → 找到 opKey=disable
            └─ 同上 · 同一插件类（onAddValidators 都会触发）
```

---

## 八、跟 haos_orgchangereason 扩展点对比

| 扩展点 | haos_adminorgfunction（本场景）| haos_orgchangereason |
|---|---|---|
| 场景特有列表类 | ListOrderCommonPlugin（19 行）| ChangeReasonListPlugin（27 行）|
| 列表类核心行为 | **setOrderBy**（排序 `enable desc, number asc`）| **setFilter**（追加 QFilter `id != 1010L`）|
| 场景特有 OP 类 | BaseDataBuOp（23 行）| BaseDataBuOp（23 行）|
| 有无 listRules | **有 1 条 formRule**（issyspreset 保护）| 无 |
| 下游引用类型 | **BasedataField 单选**（haos_adminorg.adminorgfunction）| MulBasedataField 多选（haos_changescene.changereason）|
| 反向引用查询路径 | `QFilter("adminorgfunction", "=", id)` 直查 | `QFilter("changereason.fbasedataid", "=", id)` 子表路径 |
| delete 反向引用 HisModel 约束 | 需加 **iscurrentversion=true** | 不需要 |
| 保留主键 id 过滤 | 无（不过滤任何 id）| 有（id != 1010L 强制隐藏）|

---

## 参考

- PR-001 · PR-006 · PR-007 · PR-010
- `_auto_plugin_registry.md` — 35 个插件完整清单
- `rules_chain_all.json` — 49 opKey 执行链
- `06_customization_solutions.md` — CS-01~CS-05 完整实施方案

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin -->

## ISV 扩展指引（基于 ListOrderCommonPlugin 真实证）

> FQN: `kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.haos.formplugin.web.database.ListOrderCommonPlugin -->

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
