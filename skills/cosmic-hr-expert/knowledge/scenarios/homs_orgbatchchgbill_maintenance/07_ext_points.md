# 扩展点全图 · 组织调整申请单

> **状态**: 🟢 基于 73 opKey + 24 标品插件 + 8 反编译类 + SDK 白名单整合
> **confidence**: verified
> **数据源**: `_auto_plugin_registry.md` + `_auto_plugin_semantics.md` + `rules_chain_all.json` + `cosmic_hr_sdk_whitelist_audit.md`

---

## 一、扩展能力总览

| 扩展类型 | opKey 候选 | 推荐父类 | 关联 CS |
|---|---|---|---|
| 加字段 | （不挂 opKey · 直接 modifyMeta） | （无） | CS-01 |
| 加业务校验 | `save` / `submit` / `submiteffect` / `breakup` | `HRDataBaseOp` + `AbstractValidator` | CS-02 / CS-04 / CS-07 |
| 加事务前阻断 | `submit` / `audit` | `HRDataBaseOp` | CS-04 |
| 加事务后通知 | `audit` / `submiteffect` | `HRDataBaseOp` | CS-06 |
| BEC 订阅方 | （不挂 opKey · 走业务事件中心） | `IEventServicePlugin` | CS-05 |
| 字段联动 | （表单插件 · 不挂 opKey） | `HRCoreBaseBillEdit` | CS-03 |
| 列表过滤 | （列表插件 · 不挂 opKey） | `HRDataBaseList` | （列表通用） |

---

## 二、73 opKey 全景图（按可挂载性分类）

### 🟢 主要 opKey（强烈推荐挂插件）

| opKey | 名称 | 标品 OP（非继承） | 推荐扩展点 | 风险 |
|---|---|---|---|---|
| `save` | 保存 | `OrgBatchBillSaveOp`（final） | `onAddValidators` 加 Validator | 低 |
| `submit` | 提交 | `OrgBatchBillSubmitOp`（final） | `onAddValidators` / `beforeExecute` | 低 |
| `submiteffect` | 提交即生效 | `OrgBatchBillSubmitAndEffectiveOp`（final） | 同 audit · 见生效 OP | 中 |
| `audit` | 审批通过 | `OrgBatchChgBillEffectOp`（final） | `afterExecuteOperationTransaction` | 中（事务已提交·失败要写补偿） |
| `breakup` | 终止 | `AdminOrgBatchBreakupOp`（final） | `onAddValidators` 加 Validator | 中 |
| `discard_row` | 列表废弃 | `AdminOrgBatchBillDiscardOpPlugin`（未反编译） | `beforeExecute` | 低 |

### 🟡 次要 opKey（可挂 · 业务用得少）

| opKey | 名称 | 何时挂 |
|---|---|---|
| `unsubmit` | 撤回 | 撤回时通知发起人 |
| `unaudit` | 反审批 | 跟 audit 对称 · 撤销生效 |
| `save_no_log` | 不进操作日志的 silent save | ⚠️ 不要扩展 · 标品内部用（FormPlugin L420 触发 · 改了会破坏 submit 链） |
| `delete_rows` | 列表批量删除 | 删除前补充检查 |
| `org_merge` / `edit_merge_detail` | 合并相关 | 合并前业务校验 |
| `org_split` / `edit_split_detail` | 拆分相关 | 拆分前业务校验 |

### ⚠️ HIES 注入 opKey（不要碰）

```
importdata_hr / show_import_record_hr / export_from_list_hr /
export_from_impttpl_hr / export_from_expttpl_hr / show_export_record_hr /
import_multientry_hr
```

→ 7 个 HIES 操作由平台模板注入 · ISV 想加导入校验只能在数据落库阶段 · **不要重写 import OP**。

### ⚠️ FormPlugin 联动 opKey（不挂 OP · 走 FormPlugin）

```
add / edit_add / parent_edit / edit_info / newentry_parent /
newentry_info / newentry_disable / viewflowchart
```

→ 这些都是表单上的"打开子页"按钮 · 不需要 OP 插件 · 在 FormPlugin 的 `afterDoOperation` 拦（参考 `AdminOrgBatchBillPlugin.java:512-577`）。

---

## 三、24 标品插件继承层 · 扩展决策树

来自 `_auto_plugin_registry.md` 全量清单：

### 表单插件（FormPlugin · 12 个）

| # | 插件 | 父类 | 可继承否 | 扩展建议 |
|---|---|---|---|---|
| 1 | `kd.bos.form.plugin.CodeRulePlugin` | - | ❌（平台编码规则） | 业务侧配 · 不动 |
| 2 | `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit` | `HRCoreBaseBillEdit` | ✅ HR 模板 · 可扩 | 一般不直接扩 · 通过具体 form 继承 |
| 3 | `HRBUCAApplicationEdit` | `HRDataBaseEdit` | ❌（HR 应用模板内部） | - |
| 4 | `HRPermCommonEdit` | `HRDataBaseEdit` | ❌ | - |
| 5 | `HRBaseDataImportEdit` | `HRCoreBaseBillEdit` | ❌（导入模板） | - |
| 6 | `HRBaseUeEdit` | `HRDataBaseEdit` | ❌（UE 编辑模板） | - |
| 7 | `HRHiesButtonSwitchPlugin` | `AbstractFormPlugin` | ❌（HIES 内部） | - |
| **8** | **`AdminOrgBatchBillPlugin`** | **`HRCoreBaseBillEdit`** | **❌（final · 标品专属）** | **不继承 · 并列挂新 FormPlugin extends `HRCoreBaseBillEdit`** |
| 9 | `AdminorgCubeViewPlugin` | `AbstractFormPlugin` | ❌（标品 cube 视图） | 不动 |
| 10 | `HRTemplateBillList` | `HRCoreBaseBillList` | ❌ | 通过具体 form 继承 |
| 11 | `HRPermCommonList` | `HRCoreBaseList` | ❌ | - |
| **12** | **`AdminOrgBatchBillListPlugin`** | **`HRDataBaseList`** | **❌（final · 标品专属）** | **不继承 · 并列挂新 ListPlugin extends `HRDataBaseList`** |

### 操作插件（OP · 12 个）

| # | 插件 | 父类 | 可继承否 | 扩展建议 |
|---|---|---|---|---|
| 13 | `kd.bos.business.plugin.CodeRuleOp` | - | ❌（平台编码规则） | 业务侧配 |
| **14** | **`OrgBatchBillSaveOp`** | **`HRCoreBaseBillOp`** | **❌ final** | 并列挂新 OP extends `HRDataBaseOp` |
| 15 | `OrgBatchBillSaveAndSubmitOp` | （未解析 · jar 内部） | ❌（标品内部） | - |
| **16** | **`OrgBatchBillSubmitOp`** | **`HRCoreBaseBillOp`** | **❌ final** | 同 14 |
| 17 | `AdminOrgBatchBillDiscardOpPlugin` | `HRCoreBaseBillOp` | ❌（standardly final 标品） | 同 14 |
| 18 | `HRCodeRuleOp` | `AbstractOperationServicePlugIn` | ❌（HR 编码规则） | 业务侧配 |
| **19** | **`OrgBatchBillSubmitAndEffectiveOp`** | **`HRCoreBaseBillOp`** | **❌ final** | 同 14 |
| 20 | `AdminOrgBatchChgBillEffectiveOp` | （未解析） | ❌ | - |
| **21** | **`OrgBatchChgBillEffectOp`** | **`HRDataBaseOp`** | **❌ final** | 并列挂 |
| 22 | `OrgBatchBillAuditSaveOp` | `HRCoreBaseBillOp` | ❌（standardly final） | - |
| **23** | **`AdminOrgBatchBreakupOp`** | **`HRCoreBaseBillOp`** | **❌ final** | 同 14 |
| 24 | `AdminOrgBatchBillDeleteOpPlugin` | `HRCoreBaseBillOp` | ❌（standardly final） | 并列挂 |

⭐ **核心结论**：本场景 24 标品插件中 · **0 个可继承**。所有标品具体业务插件都是 `final` 或标品内部专属。ISV 扩展唯一路径 = **并列挂新插件 · 继承通用 HR 抽象基类**。

---

## 四、SDK 父类白名单（HR 域 · 反编译实证）

来源：`memory/cosmic_hr_sdk_whitelist_audit.md`（2026-04-20 实证 9 个可继承基类）

### ✅ 9 个可继承基类（带 @SdkPlugin / @SdkPublic 注解）

| 父类 | 用途 | 注解 | jar |
|---|---|---|---|
| `kd.hr.hbp.opplugin.web.HRDataBaseOp` | HR 通用 OP 父类 ⭐ 推荐 | `@SdkPlugin` | `hrmp-hbp-opplugin-1.0.jar` |
| `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp` | HR 单据 OP 父类 | `@SdkPlugin` | 同 |
| `kd.hr.hbp.formplugin.web.HRDataBaseEdit` | HR 通用 FormPlugin 父类 | `@SdkPlugin` | `hrmp-hbp-formplugin-1.0.jar` |
| `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit` | HR 单据 FormPlugin 父类 ⭐ 推荐 | `@SdkPlugin` | 同 |
| `kd.hr.hbp.formplugin.web.HRDataBaseList` | HR 通用 ListPlugin 父类 ⭐ 推荐 | `@SdkPlugin` | 同 |
| `kd.hr.hbp.formplugin.web.HRCoreBaseBillList` | HR 单据 ListPlugin 父类 | `@SdkPlugin` | 同 |
| `kd.bos.entity.plugin.AbstractOperationServicePlugIn` | 平台 OP 父类 | `@SdkPublic` | `bos-entity-metadata-8.0.jar` |
| `kd.bos.entity.validate.AbstractValidator` | 校验器父类 | `@SdkPublic` | 同 |
| `kd.bos.form.plugin.AbstractFormPlugin` | 平台 FormPlugin 父类 | `@SdkPublic` | `bos-form-services-8.0.jar` |

### ⛔ 禁止继承清单（本场景特定）

| 类 | 原因 | 实证 |
|---|---|---|
| `OrgBatchBillSaveOp` | `public final class` | 反编译 L35 |
| `OrgBatchBillSubmitOp` | `public final class` | 反编译 L30 |
| `OrgBatchBillSubmitAndEffectiveOp` | `public final class` | 反编译 L54 |
| `OrgBatchChgBillEffectOp` | `public final class` | 反编译 L52 |
| `AdminOrgBatchBreakupOp` | `public final class` | 反编译 L23 |
| `AdminOrgBatchBillPlugin` | `public final class` | 反编译 L209 |
| `AdminOrgBatchBillListPlugin` | `public final class` | 反编译 L108 |
| `AdminChangeMsgService` | 标品内部 service · 非 SDK 公共类 | 包路径 `kd.hr.haos.business.domain.org.service` |
| `OrgBillBatchEffectService` | 标品内部 service | 包路径 `kd.hr.homs.business.service.batcheffect` |
| `OrgBatchBreakupService` | 标品内部 service | 包路径 `kd.hr.homs.business.domain.batchbill.service` |
| `OrgBatchBillSaveAndSubmitValidator` | 标品内部 Validator | 包路径 `kd.hr.homs.opplugin.web.orgbatch` |
| `AdminOrgBillSaveValidator` | 标品内部 Validator | 包路径 `kd.hr.homs.opplugin.web.orgbatch.validator` |
| `AdminOrgBatchBillBreakupStatusValidator` | 标品内部 Validator | 同 |

⛔ **禁继承的全部都是 `kd.hr.homs.*` / `kd.hr.haos.*` 包内的具体业务类**。SDK 白名单铁律：ISV 只能继承带 `@SdkPlugin` / `@SdkPublic` 注解的抽象基类。

---

## 五、`OrgBatchChgBillEffectOp` 生命周期 5 阶段（深度参考）

来源：反编译 `OrgBatchChgBillEffectOp.java`（132 行）

| 阶段 | 标品代码 | ISV 可在此并列挂 | 说明 |
|---|---|---|---|
| `onPreparePropertys` | 声明读 effdt（L57-60） | ✅ 可加 · 声明读 ISV 自定义字段 | 不要清空标品已声明的字段 |
| `onAddValidators` | super.onAddValidators（L62-64） | ✅ 推荐挂 · 加自定义 Validator | 跟标品 Validator 链并列 |
| `beginOperationTransaction` | `billBatchEffectService.batchEffect()`（L66-74） | ⚠️ 慎用 · 写主数据并发风险 | 主写入路径 |
| `afterExecuteOperationTransaction` | `handleChangeMsg()` + `afterBatchEffect()`（L76-83） | ✅ **推荐挂 · 调外系（CS-06）** | 事务已提交 · 安全 |
| `endOperationTransaction` | 修复 boid → vid 引用（L85-131） | ❌ 不要挂 · 标品收尾逻辑很重 | 容易破坏标品逻辑 |

### afterExecute 阶段 ISV 扩展模板

```java
@Override
public void afterExecuteOperationTransaction(AfterOperationArgs e) {
    super.afterExecuteOperationTransaction(e);
    String opKey = e.getOperationKey();
    // 只处理 audit / submiteffect（OrgBatchChgBillEffectOp 共用同 OP）
    if (!"audit".equals(opKey) && !"submiteffect".equals(opKey)) {
        return;
    }
    DynamicObject[] bills = e.getDataEntities();
    // ISV 业务逻辑：调外系 / 写自定义日志 / 触发其他流程
    // ⚠️ 主事务已提交 · 此处异常必须 catch · 不能让用户看到红错（事务已生效改不回去）
}
```

---

## 六、AdminOrgBatchBillPlugin（FormPlugin · 992 行）扩展点

| 生命周期方法 | 标品行为 | ISV 推荐挂法 |
|---|---|---|
| `preOpenForm` | wftask 场景跳权限校验（L228-234） | ⚠️ 慎扩 · 容易破坏权限 |
| `afterCreateNewData` | 默认填 BU + billno 处理（L236-260） | ✅ 可扩 · 加默认值 |
| `loadCustomControlMetas` | 设置 caption（L276-289） | ⚠️ 改 caption 可扩 |
| `beforeBindData` | 委托 BeforeBindDataService（L291-296） | ✅ 可扩 · 加表单初始化 |
| `afterBindData` | 委托 AfterBindDataService（L298-303） | ✅ 可扩 · 字段动态显隐 |
| `beforeBasedataSetValue` | F7 多选缓存（L305-354） | ⚠️ 标品复杂逻辑 · 不要轻动 |
| **`propertyChanged`** | 委托 PropertyChangedService（L356-360） | **✅ 推荐挂 · 字段联动 CS-03** |
| `customEvent` | 委托 CustomEventService（L362-367） | ✅ 可扩 · 加自定义事件 |
| `confirmCallBack` | 委托 ConfirmCallBackService（L369-373） | ✅ 可扩 |
| `closedCallBack` | 委托 ClosedCallBackService（L375-380） | ✅ 可扩 |
| `beforeClosed` | 检查 entry 是否变更未保存（L382-410） | ⚠️ 慎扩 · 标品已处理 |
| **`beforeDoOperation`** | 各种 opKey 拦截（L428-480） | **✅ 推荐挂 · 提交前补充逻辑** |
| **`afterDoOperation`** | 各种 opKey 后置（L482-578） | **✅ 推荐挂 · 操作后业务** |
| `registerListener` | 30 字段 F7 注册（L735-751） | ✅ 可扩 · 注册 ISV F7 监听 |
| **`beforeF7Select`** | 30 字段 F7 过滤（L753-885） | **✅ 推荐挂 · 改 F7 过滤** |

→ ⭐ 推荐挂的 4 个方法：`propertyChanged` / `beforeDoOperation` / `afterDoOperation` / `beforeF7Select`。

---

## 七、AdminOrgBatchBillListPlugin（ListPlugin · 388 行）扩展点

| 生命周期方法 | 标品行为 | ISV 推荐挂法 |
|---|---|---|
| `setFilter` | 默认排序 createtime desc（L114-117） | ✅ 推荐挂 · 加权限过滤 |
| `beforePackageData` | 6 entry 数聚合（L119-145） | ⚠️ 慎扩 · 标品聚合逻辑 |
| `packageData` | 列显示格式（L148-163） | ⚠️ 列定制化 · 慎扩 |
| `listRowClick` | 点击聚合数列展开浮层（L179-204） | ⚠️ 慎扩 |
| `billListHyperLinkClick` | hyperlink 拦截（L257-265） | ✅ 可扩 |
| `beforeShowBill` | 默认 BU customParam（L267-289） | ✅ 可扩 |
| `filterContainerAfterSearchClick` | BU 缓存（L291-299） | ✅ 可扩 |
| **`beforeDoOperation`** | breakup 校验单条 + 走前置 Validator（L301-318） | **✅ 推荐挂 · 列表批量操作前置** |
| `afterDoOperation` | breakup 后刷新（L320-329） | ✅ 可扩 |
| `confirmCallBack` | del_row / discard_row 处理（L331-365） | ⚠️ 慎扩 |
| `closedCallBack` | breakup 弹窗回调（L374-387） | ⚠️ 慎扩 |

---

## 八、不推荐扩展的标品 Service（白名单外）

| Service | 包路径 | 标品职责 | 替代方案 |
|---|---|---|---|
| `AdminChangeMsgService` | `kd.hr.haos.business.domain.org.service` | 派 sch_task + 写 haos_adminorg_msgdetail | ✅ 用 BEC 订阅方（CS-05）· 不要重写 |
| `OrgBillBatchEffectService` | `kd.hr.homs.business.service.batcheffect` | 调度 6 大策略写 haos_adminorg | ❌ 不要碰 · 改 entry 数据落地走单据本身 |
| `OrgBatchBreakupService` | `kd.hr.homs.business.domain.batchbill.service` | breakup 反向回退 | ❌ 不要碰 · 改 breakup 走 onAddValidators 加校验（CS-07） |
| `OrgBatchBillSaveHelper` | `kd.hr.homs.business.domain.orgbatch.service.impl` | save 写库 | ❌ 不要碰 |
| `OrgBatchValidateHelper` | `kd.hr.homs.business.domain.batchbill.service` | save Validator 工具 | ❌ 不要碰 · ISV 自写 Validator |
| `AdminOrgBatchChgHelper` | `kd.hr.homs.business.domain.batchbill.repository` | UI 联动工具 | ❌ 不要继承 · 看 `AdminOrgBatchBillPlugin` 用法即可 |
| `AdminOrgBatchChartBaseHelper` | 同 | 合并/拆分图表 | - |
| `BillEntryHelperEnum` | `kd.hr.homs.business.domain.batchbill.enums` | entry 名称枚举 | ✅ 可读引用 · 不要重写 |
| `EntryEntityEnum` | `kd.hr.homs.business.domain.orgfast.enums` | 同 | ✅ 可读引用 |
| `OrgBatchChgBillConstants` | `kd.hr.homs.common.constants.batchchg` | 业务常量（CHANGE_TYPE_ADD 等） | ✅ 可读引用 |
| `AuditStatusEnum` | `kd.hr.homs.common.enums` | 状态枚举 | ✅ 可读引用 · 不要重定义 |

---

## 九、扩展点 vs CS 对照速查

| 扩展点 | 父类 | 关联 CS | 风险 |
|---|---|---|---|
| `modifyMeta op=add field` parentScope=entryentity_X | （无） | CS-01 | 低 |
| `onAddValidators@save` + `AbstractValidator` | `HRDataBaseOp` | CS-02 | 中 |
| `propertyChanged` (FormPlugin) | `HRCoreBaseBillEdit` | CS-03 | 低 |
| `onAddValidators@submit` + `AbstractValidator` | `HRDataBaseOp` | CS-04 | 低 |
| `IEventServicePlugin.handleEvent` | （无 · implements） | CS-05 | 低 |
| `afterExecuteOperationTransaction@audit` | `HRDataBaseOp` | CS-06 | 中 |
| `onAddValidators@breakup` + `AbstractValidator` | `HRDataBaseOp` | CS-07 | 中 |

---

## 十、F7 扩展点（30 字段全可改）

来自 `AdminOrgBatchBillPlugin.java:749-884`：

```
注册的 F7 字段（30 个）:
info_org, parent_org, to_merge_org, merge_target_org, to_split_org, split_target_org,
parent_adminorg, parent_parentorg, parent_adminorgtype, parent_changescene,
parent_changereason, parent_city, info_adminorg, info_adminorgtype, info_changescene,
info_changereason, info_city, disable_adminorg, disable_changescene, disable_changereason,
parent_corporateorg, info_corporateorg, disorg, parent_adminorglayer, parent_adminorgfunction,
info_adminorglayer, info_adminorgfunction, merge_changescene, merge_changereason,
split_changescene, split_changereason
```

ISV 想改任何字段 F7 过滤逻辑 · 走：
1. 新 FormPlugin extends `HRCoreBaseBillEdit`（不继承标品 final · PR-001）
2. 重写 `registerListener` 注册 BeforeF7SelectListener
3. 重写 `beforeF7Select` · 在 `event.getCustomQFilters().add(...)` 追加自定义过滤
4. 标品 F7 监听照常跑（注册顺序 · 多个监听都会触发 · `AdminOrgBatchBillPlugin` 的标品逻辑不影响）

---

## 十一、扩展决策树

```
我要做什么？
    │
    ├─ 加业务字段
    │   └─► CS-01 · modifyMeta（不挂插件）
    │
    ├─ 加业务校验（保存/提交时校验）
    │   └─► CS-02 / CS-04 · 新 OP onAddValidators + Validator
    │       继承：HRDataBaseOp + AbstractValidator
    │
    ├─ 字段联动（选了X自动填Y）
    │   └─► CS-03 · 新 FormPlugin propertyChanged
    │       继承：HRCoreBaseBillEdit（不继承 AdminOrgBatchBillPlugin · final）
    │
    ├─ 监听变动事件 · 通知外系（异步）
    │   └─► CS-05 · BEC 订阅方
    │       implements IEventServicePlugin
    │
    ├─ 同步推送外系（生效后实时回执）
    │   └─► CS-06 · 新 OP afterExecuteOperationTransaction@audit
    │       继承：HRDataBaseOp（不继承 OrgBatchChgBillEffectOp · final）
    │
    ├─ 终止申请前补校验
    │   └─► CS-07 · 新 OP onAddValidators@breakup + Validator
    │       继承：HRDataBaseOp + AbstractValidator
    │
    ├─ 列表过滤 / 列表自定义按钮
    │   └─► 新 ListPlugin
    │       继承：HRDataBaseList
    │
    └─ 改 F7 字段过滤
        └─► 新 FormPlugin · beforeF7Select 追加 QFilter
            继承：HRCoreBaseBillEdit
```

---

**📌 来源追溯**：

- 24 标品插件继承层：`_auto_plugin_registry.md`
- 8 反编译类生命周期方法：`_auto_plugin_semantics.md`
- final 修饰实证：8 反编译 java 文件 class 行
- SDK 白名单 9 基类：`memory/cosmic_hr_sdk_whitelist_audit.md`（2026-04-20 实证）
- 73 opKey 清单：`rules_chain_all.json`
- 30 F7 字段：`AdminOrgBatchBillPlugin.java:749-751`
- HIES 7 opKey：`_auto_facts.md` + `_auto_operations.md`（HIES 注入）

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## ISV 扩展指引（基于 HRPermCommonEdit 真实证）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void beforeCheckDataPermission(kd.bos.form.events.BeforeDoCheckDataPermissionArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin -->

## ISV 扩展指引（基于 AdminOrgBatchBillPlugin 真实证）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRCoreBaseBillEdit`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `afterCreateNewData`, `beforeBindData`, `afterBindData`, `propertyChanged`, `closedCallBack`, `beforeClosed`, `beforeDoOperation`, `afterDoOperation`, `registerListener`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void afterCreateNewData(java.util.EventObject)` ⭐ lifecycle
- `public public void loadCustomControlMetas(kd.bos.form.events.LoadCustomControlMetasArgs)`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBasedataSetValue(kd.bos.form.field.events.BeforeBasedataSetValueEvent)`
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void customEvent(kd.bos.form.events.CustomEventArgs)`
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin -->

## ISV 扩展指引（基于 AdminorgCubeViewPlugin 真实证）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.bos.form.plugin.AbstractFormPlugin`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `registerListener`, `click`, `afterDoOperation`, `beforeDoOperation`, `propertyChanged`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminorgCubeViewPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin -->

## ISV 扩展指引（基于 AdminOrgBatchBillListPlugin 真实证）

> FQN: `kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`, `beforePackageData`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void listRowClick(kd.bos.list.events.ListRowClickEvent)`
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void beforeShowBill(kd.bos.list.events.BeforeShowBillFormEvent)`
- `public public void filterContainerAfterSearchClick(kd.bos.form.events.FilterContainerSearchClickArgs)`
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void confirmCallBack(kd.bos.form.events.MessageBoxClosedEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.formplugin.web.orgbatch.AdminOrgBatchBillListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp -->

## ISV 扩展指引（基于 OrgBatchBillSaveOp 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp -->

## ISV 扩展指引（基于 OrgBatchBillSubmitOp 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · AdminOrgChgBillSaveService L56
```java
  54       public Map<Long, DynamicObject> getOrgBeforeChangeVersionBasicInfo(List<Long> orgIdList) {
  55           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper(AdminOrgHisDynKey.ADMIN_ORG_KEY.getDynKey());
  56 >         QFilter filter = new QFilter("id", "in", orgIdList);
  57           filter.and("iscurrentversion", "=", (Object)"1");
  58           DynamicObject[] dynamicObjects = serviceHelper.query("id,parentorg,name,sourcevid", new QFilter[]{filter});
```

**THROW_BIZ_EXCEPTION** · AdminOrgChgBillSaveService L86
```java
  84               LOG.error((Throwable)e);
  85               String message = String.format(ResManager.loadKDString((String)"\u5f53\u524d\u7cfb\u7edf\u51fa\u73b0\u5f02\u5e38\uff1b\u5f02\u5e38traceId\uff1a%1$s\u3002", (String)"AdminOrgChgBillSaveService_0", (String)"odc-homs-business", (Object[])new Object[0]), TraceIdUtil.getCurrentTraceIdString());
  86 >             throw new KDBizException(new ErrorCode("getOrgRelateVersion", message), new Object[0]);
  87           }
  88           return infoMap;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin -->

## ISV 扩展指引（基于 AdminOrgBatchBillDiscardOpPlugin 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · AdminOrgBatchBillDiscardOpPlugin L31
```java
  29               List selectedRows = Arrays.stream(datas).map(dyn -> dyn.getLong("id")).collect(Collectors.toList());
  30               HRBaseServiceHelper billHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
  31 >             for (DynamicObject billDy : dynamicObjectArr = billHelper.query("billstatus", new QFilter[]{new QFilter("id", "in", selectedRows)})) {
  32                   billDy.set("billstatus", (Object)"F");
  33               }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDiscardOpPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## ISV 扩展指引（基于 HRCodeRuleOp 真实证）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void onReturnOperation(kd.bos.entity.plugin.args.ReturnOperationArgs)`
- `protected protected void recycleNumber(kd.bos.dataentity.entity.DynamicObject[])`

### SDK 范式（ISV 抄作业）

**READ_VIA_HELPER** · HRCodeRuleOp L81
```java
  79           String orgId;
  80           String entityId = obj.getDataEntityType().getName();
  81 >         CodeRuleInfo codeRuleInfo = CodeRuleServiceHelper.getCodeRule((String)entityId, (DynamicObject)obj, (String)(orgId = this.getMainOrgId(obj)));
  82           if (codeRuleInfo == null) {
  83               return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp -->

## ISV 扩展指引（基于 OrgBatchChgBillEffectOp 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beginOperationTransaction`, `afterExecuteOperationTransaction`, `endOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle
- `public public void endOperationTransaction(kd.bos.entity.plugin.args.EndOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgBatchChgBillEffectOp L93
```java
  91           }
  92           HRBaseServiceHelper batchOrgEntityHelper = new HRBaseServiceHelper("homs_batchorgentity");
  93 >         QFilter adminOrgIdFilter = new QFilter("creator", "=", (Object)0);
  94           QFilter billIdFilter = new QFilter("billid", "in", (Object)billIds);
  95           adminOrgIdFilter.and(billIdFilter);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchChgBillEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp -->

## ISV 扩展指引（基于 OrgBatchBillAuditSaveOp 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.OrgBatchBillAuditSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp -->

## ISV 扩展指引（基于 AdminOrgBatchBreakupOp 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgBatchBreakupService L101
```java
  99           hrBaseServiceHelper.update(new DynamicObject[]{billDB});
 100           HRBaseServiceHelper logHelper = new HRBaseServiceHelper("haos_orgchgeffectlog");
 101 >         DynamicObject adminorgLog = logHelper.queryOriginalOne("id", new QFilter("refbill", "=", (Object)id));
 102           if (adminorgLog == null) {
 103               return;
```

**READ_VIA_HELPER** · OrgBatchBreakupService L90
```java
  88           HRBaseServiceHelper hrBaseServiceHelper = new HRBaseServiceHelper("homs_orgbatchchgbill");
  89           long id = bill.getLong("id");
  90 >         DynamicObject billDB = hrBaseServiceHelper.loadOne((Object)id);
  91           String returnData = option.getVariableValue("returnData", null);
  92           if (HRStringUtils.isNotEmpty((String)returnData)) {
```

**WRITE_VIA_HELPER** · OrgBatchBreakupService L65
```java
  63           Object[] ids = new Object[]{billId};
  64           operateOption.setVariableValue("onlyvalidate", Boolean.TRUE.toString());
  65 >         OperationResult preOpResult = OperationServiceHelper.executeOperate((String)operate.getOperateKey(), (String)"homs_orgbatchchgbill", (Object[])ids, (OperateOption)operateOption);
  66           if (preOpResult == null) {
  67               return;
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBreakupOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin -->

## ISV 扩展指引（基于 AdminOrgBatchBillDeleteOpPlugin 真实证）

> FQN: `kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRCoreBaseBillOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · AdminOrgDetailHelper L167
```java
 165   
 166       public static AuthorizedOrgResult getOrgAuth(IFormView iFormView) {
 167 >         Long userId = RequestContext.get().getCurrUserId();
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
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

**WRITE_VIA_HELPER** · AdminOrgDetailHelper L603
```java
 601           option.setVariableValue("skipCheckSpecialDataPermission", Boolean.TRUE.toString());
 602           try {
 603 >             return OperationServiceHelper.executeOperate((String)operationKey, (String)"haos_adminorgdetail", (DynamicObject[])adminOrgs, (OperateOption)option);
 604           }
 605           catch (Exception ex) {
```

**CALL_CROSS_SERVICE** · AdminOrgDetailHelper L170
```java
 168           String permItemId = "47150e89000000ac";
 169           String appId = AdminOrgDetailHelper.getAppIdWithDealThirdApp(iFormView.getFormShowParameter(), "haos_adminorgdetail");
 170 >         return (AuthorizedOrgResult)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedAdminOrgSet", (Object[])new Object[]{userId, appId, "haos_adminorgdetail", permItemId, "boid"});
 171       }
 172   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.homs.opplugin.web.orgbatch.AdminOrgBatchBillDeleteOpPlugin -->
