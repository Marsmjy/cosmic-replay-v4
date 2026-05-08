# 模型设计 · 数据规则 (hrcs_datarule)

> **状态**：基于 OpenAPI 实抓 21 字段 + 3 类反编译（HRDataRuleEditPlugin / HRDataRuleSaveOp / HRAdminStrictPlugin）+ `scene_doc.json` 整合
> **confidence**：real_deploy（所有字段含 physicalColumn / physicalTable / 反编译类名均来自 `_auto_*` 实证）
> **物理表**：`t_hrcs_datarule`（来自 21 字段 physicalTable=(main) 实抓）

## 一、单据头（formId / appNumber / cloud / 物理表）

| 项 | 值 | 数据来源 |
|---|---|---|
| formId | `hrcs_datarule` | scene_doc.json `_meta.formId` |
| 显示名 | 数据规则 | scene_doc.json `_meta.title` |
| 应用 | HR通用服务 (`hrcs`) | scene_doc.json `_meta.app` |
| 云 | HR基础服务云 | scene_doc.json `_meta.cloud` |
| 业务域 | HR基础服务/HR权限管理 | scene_doc.json `_meta.domain` |
| ModelType | BillFormModel · HR 数据规则配置 | 实抓菜单/查表 |
| 物理主表 | `t_hrcs_datarule` | scene_doc.json fields[*].physicalTable |
| 多语言子表 | （主表承载 · 标品没单独 _l 表 · MuliLangTextField 在主表） | physicalColumn 实抓 |
| 字段总数 | **21** · 主表 21 / 子表 0 | scene_doc.json `fields` |
| 父类 | `HRDataBaseEdit` (FormPlugin) / `HRDataBaseOp` (OP) | 反编译实证 |
| 共用准入 | `HRAdminStrictPlugin` · hrcs 11 表单共用 | 反编译实证 |

## 二、字段总图（21 字段 · 按 minefield 红/黄/无分组）

### 2.1 业务字段（无雷区 · 6 字段）

| key | type | layer | required | isvCanModify | 说明 |
|---|---|---|---|---|---|
| `number` | TextField | L1 | false | true | 规则编码 · CodeRuleOp 编码规则插件挂在 save · 业务可配 |
| `name` | MuliLangTextField | L1 | false | true | 规则名称 · 主表承载多语言（`fname` 列）|
| `simplename` | MuliLangTextField | L1 | false | true | 简称 · 主表承载（`fsimplename`）|
| `description` | MuliLangTextField | L1 | false | true | 描述 · 主表承载（`fdescription`）|
| `index` | IntegerField | L1 | false | true | 排序号 · 主表（`findex`）|
| `rule` | TextField | L3 | false | true | **规则**（核心字段）· 存 FilterCondition.toJsonString · `frule` 列 |

### 2.2 业务对象引用字段（核心 · 1 字段）

| key | type | layer | required | refEntity | minefield | 说明 |
|---|---|---|---|---|---|---|
| `entitynum` | BasedataField | L3 | **true** | `bos_entityobject` | – | **业务对象**（数据规则作用的目标 form）· F7 弹窗带 iscurrentversion=1 过滤 · 创建后只读（FP_AL1）· 切换会清空 rule（FP_PC1）|

### 2.3 黄雷区字段（变更级联下游 · 慎改 · 2 字段）

| key | type | physicalColumn | minefield | 说明 |
|---|---|---|---|---|
| `status` | BillStatusField | `fstatus` | yellow | 数据状态 · audit/unaudit/disable/enable/submit/unsubmit OP 都改它 · ISV 不要直接 setValue |
| `enable` | BillStatusField | `fenable` | yellow | 使用状态 · disable/enable OP 改 · 影响权限链是否使用本规则 |

### 2.4 红雷区字段（系统/平台维护 · 12 字段）

| key | type | physicalColumn | autoComputed | 说明 |
|---|---|---|---|---|
| `creator` | CreaterField | `fcreatorid` | true | BOS L0 · 平台维护 |
| `modifier` | ModifierField | `fmodifierid` | true | BOS L0 · 平台维护 |
| `createtime` | CreateDateField | `fcreatetime` | true | BOS L0 |
| `modifytime` | ModifyDateField | `fmodifytime` | true | BOS L0 |
| `masterid` | MasterIdField | `fmasterid` | true | BOS L0 · 主数据内码 |
| `issyspreset` | CheckBoxField | `fissyspreset` | true | 系统预置标志 · 配 PR-007（预置数据不可改）|
| `disabler` | UserField | `FDisablerID` | true | 禁用人 · disable OP 自动维护 |
| `disabledate` | DateTimeField | `FDisableDate` | true | 禁用时间 · disable OP 自动维护 |
| `initdatasource` | ComboField | `finitdatasource` | true | 数据来源（出厂值）· 平台维护 |
| `orinumber` | TextField | `forinumber` | true | 出厂编码 · 标品出厂数据维护 |
| `oristatus` | ComboField | `foristatus` | true | 出厂数据编辑状态 · 标品维护 |
| `oriname` | MuliLangTextField | `foriname` | true | 出厂名称 · 标品出厂数据维护 |

## 三、关键字段语义详解

### 3.1 `rule` · 数据规则的核心载体（重点）

`rule` 是 `TextField`（不是结构化字段），存的是 **`FilterCondition` 序列化后的 JSON 字符串**。

实证（`HRDataRuleEditPlugin.java`）：

```java
// L138-L146 · save 时序列化
String ruleStr = SerializationUtils.toJsonString((Object)fc);
this.getModel().setValue("rule", (Object)ruleStr);

// L181-L184 · afterBindData 时反序列化恢复
if (HRStringUtils.isNotEmpty((String)ruleStr)) {
    FilterCondition fc = (FilterCondition)SerializationUtils.fromJsonString((String)ruleStr, FilterCondition.class);
    filterGrid.SetValue(fc);
}
```

⚠️ 这意味着：

1. **直接 set 字符串会绕过 FilterBuilder 校验** · 必须走 FilterGrid 控件 · 或 `new FilterBuilder(entityType, fc).buildFilter()` 二次校验
2. **rule 不是结构化字段** · QueryServiceHelper 用 LIKE 模糊查找内容会很难
3. **rule 的合法性依赖 entitynum 业务对象的当前字段集** · 业务对象元数据被改字段后 · 老规则可能失效

### 3.2 `entitynum` · 业务对象（数据规则作用的目标 form）

- 类型：`BasedataField` · refEntity = `bos_entityobject`
- **必填**（mustInput=true）· 创建后只读（`HRDataRuleEditPlugin#afterLoadData` 永久 setEnable(false)）
- 切换会清空 `rule` + 重建 FilterGrid（`HRDataRuleEditPlugin#propertyChanged`）
- F7 弹窗时 · 时序基础资料带 `iscurrentversion=1` 过滤（`HRDataRuleEditPlugin#beforeF7Select` · 用 `HisModelServiceHelper.isInheritHisModelTemplate` 判定）

### 3.3 `status` 状态机

| 值（标品状态码） | 状态 | 来源 OP |
|---|---|---|
| `A` (推断 · 标品惯例) | draft 草稿 | `new` / `unsubmit` |
| `B` (推断) | saved 已保存 | `save` / `unaudit` |
| `C` (推断) | audit 已审核 | `audit` |
| `D` (推断) | submitted 已提交 | `submit` |

实证：`HRBaseDataStatusOp` 走标品 status 状态机 · 在 8 核心 opKey 的 `executionChain` 中都有它（save / delete / audit / unaudit / submit / unsubmit / disable / enable）。

### 3.4 `enable` 启用状态

| 值 | 含义 | 来源 OP |
|---|---|---|
| `1` | 启用（默认）| `enable` |
| `0` | 禁用 | `disable` |

`disable` 操作会同时填 `disabler`（当前用户）+ `disabledate`（now）· `enable` 操作清空这两字段。

### 3.5 `issyspreset` · 系统预置标志（PR-007 关键）

`CheckBoxField` · 默认 `false` · 平台维护。

PR-007 规定：
- `issyspreset=true` 的数据规则 → 标品 `disable` / `delete` / `unaudit` / `unsubmit` 都会拦截
- ISV 自建 OP 时也要复用此判断 · 不要把业务自建规则一刀切归类为"可删可改"

## 四、字段继承关系（Layer L0 / L1 / L3）

| Layer | 含义 | 字段数 | 字段示例 |
|---|---|---|---|
| L0 | BOS 基础字段（平台基类）| 5 | creator / modifier / createtime / modifytime / masterid |
| L1 | 标品基础资料字段（HR 模板）| 14 | number / name / status / enable / simplename / description / index / issyspreset / disabler / disabledate / initdatasource / orinumber / oristatus / oriname |
| L3 | 场景定制字段（hrcs_datarule 专属）| 2 | entitynum · rule |

**结论**：hrcs_datarule 是 BillFormModel + HR 标品基础资料模板 · 14 个字段来自 HR 标品模板继承（HRBaseDataTplEdit 处理）· 仅 2 个字段是数据规则场景专属。

## 五、Plugin 注册全景（24 plugin · 实抓）

数据来源：`_auto_plugin_registry.md`（OpenAPI `queryEditablePlugins` 实抓）。

### 5.1 FormPlugin 链（12 个）

按生命周期方法分组：

- **registerListener**（1）：HRDataRuleEditPlugin（filtergridap.BeforeF7Select）
- **afterCreateNewData**（1）：HRDataRuleEditPlugin（entitynum 预填 + 锁定）
- **afterLoadData**（2）：HRBaseDataTplEdit · HRDataRuleEditPlugin（永久锁 entitynum + 视图态隐藏添加按钮）
- **afterBindData**（5）：HRBaseDataTplEdit · HRHiesButtonSwitchPlugin · HRDataRuleEditPlugin（refreshFilterGrid）· TemplateBillEdit · HRBaseDataCommonFieldLockEdit
- **propertyChanged**（1）：HRDataRuleEditPlugin（entitynum 切换清规则）
- **beforeDoOperation**（3）：HRBaseDataTplEdit · HRDataRuleEditPlugin（doSave 序列化 rule）· HRBasedataLogList
- **afterDoOperation**（2）：HRBaseDataTplEdit · HRBaseDataCommonList
- **preOpenForm**（3）：HRBaseDataTplEdit · HRAdminStrictPlugin（HR 领域准入）· HRBaseDataTplList
- **beforeBindData**（2）：HRBasedataLogList · HRBaseDataTplList
- **beforeClosed**（2）：HRBaseDataTplEdit · HRBaseDataCommonEdit

### 5.2 OP 链（5 个）

- **HRDataRuleSaveOp**（save 专属 · onAddValidators / beforeExecute / afterExecute）⭐ 核心
- HRBaseDataStatusOp（onAddValidators / beforeExecute · status 状态机 8 个 opKey 共用）
- HRBaseDataLogOp（beforeExecute / afterExecute · 操作日志）
- HRBaseDataEnableOp（beforeExecute · enable / disable）
- HRBaseOriginalOp（beforeExecute · 出厂数据原始值）

### 5.3 平台模板（7 个 · 不展开）

CodeRulePlugin / templatebaseedit / HRBaseDataImportEdit / TemplateBillEdit / HRBaseDataCommonMobList / BdVersionListPlugin / BaseDataNameVersionListPlugin / CodeRuleOp / BdVersionSaveServicePlugin / CodeRuleDeleteOp 等。

## 六、操作链 42 opKey 概览

详见 `rules_chain_all.json` · 8 个核心 opKey 富化（≥3 V + ≥5 B + executionChain）：

| opKey | name | 核心动作 | extValue |
|---|---|---|---|
| save | 保存 | FilterCondition → rule + diff + 通知权限链 + 写日志 | 5 |
| delete | 删除 | 标品基础资料 delete + 状态校验 | 3 |
| audit | 审核 | status=audit · 让规则生效进权限链 | 3 |
| unaudit | 反审核 | status=saved · 让规则失效 · 检查下游 | 2 |
| disable | 禁用 | enable=0 + disabler/disabledate | 2 |
| enable | 启用 | enable=1 · 通知权限链 | 2 |
| submit | 提交 | status=submitted | 2 |
| unsubmit | 撤销 | status=draft | 2 |

其余 34 opKey 为标品通用（new / modify / view / copy / refresh / close / submitandnew / saveandnew / first / previous / next / last / option / returndata / 6 个 importdata_hr 系列 + 6 个 importdata 系列 / 2 个 mobtoolbar / 4 个 donothing 日志改名）· 全部无 ISV 关注点。

## 七、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加自定义业务字段（如"规则备注/分类"）| modifyMeta add field 到主实体 hrcs_datarule · ISV 前缀（CS-01）| 低 |
| 数据规则类型→维度联动 | FormPlugin propertyChanged 监听 ISV 字段（CS-02）| 低 |
| save / delete 前置校验（防止规则冲突）| ISV 自建 Validator 挂 onAddValidators（CS-03）| 中 |
| 删除/禁用前查下游引用 | ISV 自建 Validator 查 hrcs_dynascheme（CS-04）| 中 |
| save 后发 BEC 业务事件 | 标品没发 · ISV 自建 SaveOp 在 afterExecute 调 IEventService（CS-05）| 中 |
| 数据规则参数项扩展 | ID 用 kd.bos.id.ID（PR-005 · CS-06）| 低 |
| 列表过滤定制 | 自建 ListPlugin · setFilter 加 QFilter（CS-07）| 低 |

详见 `06_customization_solutions.md`（7 个 CS）。

## 八、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org / hjm / hrcs_dynascheme）保持一致

### 8.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。

`hrcs_datarule` 的 MuliLangTextField 字段（`name` / `simplename` / `description` / `oriname`）目前在主表 `t_hrcs_datarule` 承载多语言（无独立 `_l` 表 · 这是标品配置 · 不是默认行为）。

⚠️ **ISV 扩展 MuliLangTextField 字段时不要假设会自动有 _l 表** · 看具体场景物理表实抓。

### 8.2 反模式 · 继承场景专属类（核心铁律）

| 场景专属类（**禁继承**）| 推荐做法 |
|---|---|
| `HRDataRuleEditPlugin` | 并列挂新 FormPlugin · 继承 `HRDataBaseEdit` |
| `HRDataRuleSaveOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `HRDataRuleSaveValidator` | 并列加新 Validator · 继承 `AbstractValidator` |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸 · ISV 不要继承 · 直接复用即可 |
| `HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisModelFormCommonPlugin` / `HisModelListCommonPlugin` | @SdkInternal 平台时序内部类 · ISV 不得继承 |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs）· 不在白名单 |

`HRDataRuleEditPlugin / HRDataRuleSaveOp` 都是 hrcs_datarule 场景专属类 · ISV 不要继承 · 走并列挂或继承 SDK 白名单父类（参考 `_shared/platform_rules.json` PR-001）。

### 8.3 HisModel 时序场景（hrcs_datarule 角色：消费方）

`hrcs_datarule` **本身不是 HisModel 时序基础资料** · 但它通过 `entitynum` 引用 HisModel 时序基础资料（如 `haos_adminorg` / `hbjm_job` / `hrpi_empjobrel`）· 因此实证：

```java
// HRDataRuleEditPlugin.java L82-L84
if (HisModelServiceHelper.isInheritHisModelTemplate((String)entityNumber)) {
    evt.addCustomQFilter(new QFilter("iscurrentversion", "=", (Object)"1"));
}
```

**5 条 HisModel 必读**（PR-008 / PR-009）：
- **boid 业务维度** · 跨所有版本不变 · 下游引用必用 boid
- **id 版本维度** · 每次审核/confirmchange 产生新 id
- **iscurrentversion** · true 标记当前生效版本 · PR-008 必加过滤
- **sourcevid** · 指向上一版本（V2.sourcevid = V1.id）· 链式追溯
- **本场景 entitynum F7** · 必须只让用户选 `iscurrentversion=1` 当前版本（实证已加）

参考 `_shared/platform_rules.json` PR-008 / PR-009 · 不可绕过。

### 8.4 列表三层模型（tablist|treelist）

`hrcs_datarule` 是 BillFormModel 单据 · 列表挂在数据实体上（不是 tablist/treelist 层）。

Claude 做列表类 CS（如 setFilter / 自定义按钮）时 · 看反编译 `HRBasedataLogList` / `HRBaseDataCommonList` 这些标品列表插件 · 自建 ListPlugin 继承 `HRDataBaseList`。

### 8.5 PR 引用速查（本场景必引）

- **PR-001** 并列挂不继承 · 是核心铁律（适用 HRDataRuleEditPlugin / HRDataRuleSaveOp / HRDataRuleSaveValidator / HRAdminStrictPlugin）
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环（CS-02 联动场景必用）
- **PR-005** ID 生成用 kd.bos.id.ID（CS-06 数据规则参数项扩展）
- **PR-007** 预置数据（issyspreset=true）不可改 · 业务自建可改
- **PR-008** 时序必带 iscurrentversion=true 过滤（**已实证 entitynum F7 用**）
- **PR-009** 下游用 boid 不用 id（消费 HisModel 类业务对象时）
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验 · afterExecute 通知（**已实证 HRDataRuleSaveOp**）
- **PR-011** BEC 走平台事件中心（**hrcs_datarule 标品没发 BEC** · ISV 自建发布方时引用 · CS-05）

## 九、ServiceHelper 反编译实证（hrcs 域）

| ServiceHelper | 关键方法 | 用途 |
|---|---|---|
| `HRPermCacheMgr` | `getTypeByPrefix` / `clearCache` | hrcs 权限缓存管理 · 实证 SaveOp.afterExecute 调 |
| `PermNotifyService` | `notifyByDataRule(Long id)` | 数据规则变更通知权限链 · 实证 SaveOp.afterExecute 调 |
| `DataRuleLogServiceHelper` | `getDataRuleLogModel(id, before)` / `compareFilterControls` / `dataRuleLogInit` | 数据规则操作日志（前后对比）· 实证 SaveOp 调 |
| `PermFormCommonUtil` | `setFilterGridAddBtnVisible` | FilterGrid 添加按钮显隐控制 · 实证 EditPlugin 调 |
| `HRAdminService` | `isHrAdmin()` | HR 领域管理员判定 · 实证 HRAdminStrictPlugin 调 |
| `HisModelServiceHelper` | `isInheritHisModelTemplate(String num)` | 时序基础资料模板判定（@SdkPublic）· 实证 EditPlugin 调 |
| `HRBaseServiceHelper` | `isExists(pk)` / `queryOne(pk)` | HR 基础资料服务·首选 · 实证 SaveOp 调 |

⚠️ ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。

## 十、模型级别的元数据校验汇总

| 校验 ID | 触发点 | 规则 | errorMessage | sourceFile |
|---|---|---|---|---|
| MV_V1 | save · FormPlugin doSave | rule + filtergridap 不能空 | 请配置规则。 | HRDataRuleEditPlugin.java#L150 |
| MV_V2 | save · FormPlugin doSave | FilterCondition 字段必须存在于 entitynum | 规则字段非法 · FilterBuilder 校验失败 | HRDataRuleEditPlugin.java#L142-145 |
| MV_V3 | save · OP onAddValidators | HRDataRuleSaveValidator 业务校验 | （待 hrmp-hrcs-opplugin 反编译 HRDataRuleSaveValidator）| HRDataRuleSaveOp.java#L48 |
| MV_V4 | preOpenForm | 当前用户必须 isAdmin OR isCosmic AND isHrAdmin | 您无法访问该功能 · 因为您不是 HR 领域管理员。 | HRAdminStrictPlugin.java#showMesIfUserIsNotAdmin |

详见 `form_lifecycle_rules.json#metadataRules.validations`。
