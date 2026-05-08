# 扩展点全图 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `_auto_plugin_registry.md` 57 插件 + `_auto_plugin_semantics.md` 5 类反编译 + `_auto_operations.md` 61 opKey 执行链整合
> **视角**: 本场景所有扩展点，按"实体 × 时机"二维定位
> **confidence**: verified（类名、方法签名、读写字段均来自 jar 反编译）

---

## ⭐ 机器校准总览（2026-04-24 `_auto_plugin_registry.md`）

**本场景 57 插件分 3 大族群**：

### 职位域特有插件（10 个，`kd.hrmp.hbjm.*`）

| # | 类名 | 父类 | 主要方法 | 生命周期 |
|---|---|---|---|---|
| 1 | `kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp` | `HRDataBaseOp` | `onPreparePropertys` + `onAddValidators` | save / confirmchange |
| 2 | `kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp` | `HRDataBaseOp` | - | audit |
| 3 | `kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp` | `HRDataBaseOp` | `onPreparePropertys` + `onAddValidators` + `beforeExecuteOperationTransaction` | enable |
| 4 | `kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp` | `HRDataBaseOp` | - | disable |
| 5 | `kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp` | `HRDataBaseOp` | `beforeExecuteOperationTransaction` + `endOperationTransaction` + `afterExecuteOperationTransaction` | 消息监听 |
| 6 | `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit` | - | afterBindData 等 | 详情表单 |
| 7 | `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit` | `HRDataBaseEdit` | `afterBindData` + `beforeDoOperation` + `afterDoOperation` + `cacheAttachmentData` + `isAttachmentChange` | 字段/附件变更 |
| 8 | `kd.hrmp.hbjm.formplugin.web.basedata.JobBaseBuListPlugin` | `HRDataBaseList` | `filterContainerBeforeF7Select` + `getFilterSchemaFieldSet` + `getAppId` + `getEntityName` + `getPermOrgResult` | 列表权限 |
| 9 | `kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin` | `HRDataBaseList` | `billListHyperLinkClick` | 列表超链 |
| 10 | `kd.hrmp.hbjm.formplugin.web.common.JobBaseDataListPlugin` | `HRDataBaseList` | `setFilter` | 列表过滤 |
| + | `kd.hrmp.hbjm.formplugin.web.job.JobListPlugin` | - | - | 列表 |
| + | `kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit` | - | - | 编辑 |
| + | `kd.hrmp.hbjm.formplugin.web.impt.JobBaseImportPlugin` | - | - | 导入（仅在 importdata opKey 引用） |

**证据**：`_auto_plugin_registry.md` L21-L22、L25、L28-L30、L43、L48、L53、L58、L62（完整清单）+ `_auto_plugin_semantics.md` L13-L81（5 类深度反编译）

### HR 通用模板插件（约 25 个，`kd.hr.hbp.*`）

- 基础资料编辑族：`HRBaseDataTplEdit` / `HRBaseDataTplList` / `HRBaseDataImportEdit` / `HRHiesButtonSwitchPlugin`
- 时序模型族（8 个）：`HisModelFormCommonPlugin` / `HisModelBuFormPlugin` / `HisModelListCommonPlugin` / `HisModelBuListPlugin` / `HisModelF7ListPlugin` / `HisModelFilterPanelListPlugin` / `HisModelFilterPanelF7ListPlugin` / `HisModelMobileListPlugin`
- 时序操作族：`HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisBaseDataF7FastFilter`
- 基础资料配置族：`HRBaseDataStatusOp` / `HRBaseDataLogOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBasedataLogList`
- 业务单元：`HRBUCAApplicationEdit` / `HRRelatePageRightDynamicPlugin`

### 苍穹基础框架插件（约 22 个，`kd.bos.*`）

- 编码规则：`CodeRulePlugin` + `CodeRuleOp` + `CodeRuleDeleteOp`
- 版本管理：`BdVersionListPlugin` + `BdVersionSaveServicePlugin` + `BaseDataNameVersionListPlugin`
- 基础资料控制：`BaseDataCreateOrgPlugin` + `BaseDataFormPlugin` + `BaseDataListPlugin` + `BaseDataNewPlugin` + `BaseDataMobListPlugin` + `BasedataFilterController`
- 基础资料操作族：`BaseDataSavePlugin` + `BaseDataDeletePlugin` + `BaseDataAuditPlugin` + `BaseDataUnAuditPlugin` + `BaseDataDisablePlugin` + `BaseDataEnablePlugin` + `BaseDataSubmitPlugin` + `BaseDataUnSubmitPlugin`
- 控制策略：`BdCtrlStrtgyShowLogicPlugin` + `BdCtrlStrtgyShowLogicListPlugin`

> 📌 **执行顺序**：同一生命周期方法（如多个 `onAddValidators`）按注册顺序依次执行。完整清单见 [_auto_plugin_registry.md](_auto_plugin_registry.md)。

---

## 一、扩展点的双维度理解

扩展点 = **"时机 × 宿主实体"** 的二维定位

苍穹有 **5 大扩展族群**（来自 platform/openapi_capability_map）：

1. **建模族**：`buildMeta` / `modifyMeta` / `getFormSchema` / `queryForms`
2. **操作族**：`listOperations` / `updateOperation`
3. **插件族**：`registerPlugin` / `queryEditable`
4. **规则族**：`addRule` (formRule/bizRule 两层)
5. **PDM 族**：跨 server / datamodel 两套环境

**本场景用到的**：2 + 3 + 4（CRUD / 审核 / 启停用 / 版本变更都会触发运行时扩展点）

---

## 二、列表展示阶段

### 🔌 `setFilter@hbjm_jobhr` (列表) → `JobBaseDataListPlugin`

- **宿主**：`hbjm_jobhr`（列表视图，基础资料列表共用）
- **时机**：列表查询前追加过滤条件
- **接口**：`IListPlugin#setFilter(SetFilterEvent setFilterEvent)`
- **标品插件**：`kd.hrmp.hbjm.formplugin.web.common.JobBaseDataListPlugin`（`_auto_plugin_semantics.md` L47-L55）
- **父类**：`HRDataBaseList`
- **super 调用**：`setFilter()`
- **场景用途**：定制列表默认过滤规则、加数据权限
- **风险**：中
- **推荐模式**：继承 + super 调用 + 追加 filter

### 🔌 `filterContainerBeforeF7Select@hbjm_jobhr` → `JobBaseBuListPlugin`

- **时机**：F7 选择容器展示前
- **接口**：`IListPlugin#filterContainerBeforeF7Select(BeforeFilterF7SelectEvent args)`
- **标品插件**：`kd.hrmp.hbjm.formplugin.web.basedata.JobBaseBuListPlugin`（`_auto_plugin_semantics.md` L59-L70）
- **父类**：`HRDataBaseList`
- **5 个方法**：`getFilterSchemaFieldSet` / `getAppId` / `getEntityName` / `filterContainerBeforeF7Select` / `getPermOrgResult`
- **场景用途**：按创建组织数据权限限定可选职位范围
- **风险**：中

### 🔌 `billListHyperLinkClick@hbjm_jobhr` → `JobInitFilterCommonPlugin`

- **时机**：列表行超链点击
- **接口**：`IListPlugin#billListHyperLinkClick(HyperLinkClickArgs args)`
- **标品插件**：`kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin`（`_auto_plugin_semantics.md` L75-L81）
- **父类**：`HRDataBaseList`
- **场景用途**：跳转详情时传参 / 换目标表单
- **风险**：低

---

## 三、新增 / 修改阶段（save opKey）

### 🔌 `onAddValidators@save` ⭐⭐ 本场景最常扩展

- **宿主**：`hbjm_jobhr`
- **时机**：保存校验器注册（**事务未开始**）
- **接口**：`onAddValidators(AddValidatorsEventArgs e)`
- **事务阶段**：`pre_transaction`

- **标品插件执行链 10 个**（`_auto_operations.md` L99-L113，按 RowKey）：
  1. `kd.bos.business.plugin.CodeRuleOp` — 编码规则（onAddValidators 注册 numberValidator）
  2. `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` — 基础资料保存
  3. `kd.bos.base.bdversion.BdVersionSaveServicePlugin` — 基础资料版本管理
  4. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` — HR 单据状态
  5. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` — HR 日志
  6. `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` — HR 启用态
  7. `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` — HR 原始值
  8. `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` — 时序唯一性
  9. `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` — 时序通用
  10. `kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp` ⭐ — 职位保存
     - `onPreparePropertys` L22-L31：声明读写字段
     - `onAddValidators` L33-L46：注册校验器，写 `id`（`rules_chain_all.json` L377-L401）

- **6 条 validations**（`_auto_operations.md` L117-L122）：
  - `MustInput 6096194600001fac` (enabled)
  - `FormValidate 1VRALXJOVNKD` (disabled)
  - `GroupFieldUnique 0+/SL/MZ=VJB` (disabled)
  - `GroupFieldUnique 2=K9URZCEUUS` (enabled) ⭐ 编码唯一
  - `GroupFieldUnique 23ILP6JS0TBC` (disabled)
  - `FormValidate 2M42D=Y0/0XK` (enabled) — 生效日期不能填写未来

- **场景用途**：80% 的保存期业务扩展发生在这里
- **风险**：⚠️ **中**（丢失时序 / 唯一性校验会导致脏数据）
- **推荐模式**：新增插件并列注册，不要覆盖 `JobHrSaveOp`

### 🔌 `beforeExecuteOperationTransaction@save`

- **时机**：保存前（事务前）
- **场景用途**：编码自动生成（CS-05）/ 业务字段预处理 / 引用数据校验

### 🔌 `afterExecuteOperationTransaction@save`

- **时机**：保存后（事务提交前）
- **场景用途**：消息推送 / 外部系统同步 / 事件发布（CS-06）

---

## 四、审核 / 反审核阶段（audit / unaudit opKey）

### 🔌 `beforeExecuteOperationTransaction@audit` → `JobHrAuditOp`

- **时机**：审核事务前
- **标品插件**：`kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp`（`_auto_operations.md` L164 audit 链第 4 位）
- **父类**：`HRDataBaseOp`
- **3 标品校验**：
  - `FormValidate 1cc0054f000018ac` (enabled)
  - `FormValidate f2843bab0000bfac` (enabled)
  - `InProcess 2W/BRWU+MXP7` (disabled) — 基础资料在流程中校验
- **场景用途**：审核前的自定义业务校验（如"关键职位审核需双人签核"）
- **风险**：中
- **推荐模式**：继承 `JobHrAuditOp` + super 调用

### 🔌 `afterExecuteOperationTransaction@unaudit`

- **时机**：反审核后
- **标品插件**：`BaseDataUnAuditPlugin` + `HisModelOPCommonPlugin`（无 JobHr 专用，`_auto_operations.md` L181-L187）
- **场景用途**：反审核后的恢复逻辑（如解除关联锁）

---

## 五、启用 / 禁用阶段（enable / disable opKey）

### 🔌 `onAddValidators@enable` ⭐⭐ 已有 JobEnableValidator 样板

- **时机**：启用前校验器注册
- **标品插件**：`JobHrEnableOp`（`_auto_operations.md` L231 enable 链第 3 位）
- **注册的 Validator**：`JobEnableValidator`（`rules_chain_all.json` L678-L688）
- **3 个业务方法**（`rules_chain_all.json` L690-L741）：
  - `onPreparePropertys` L31-L35
  - `onAddValidators` L37-L39
  - `beforeExecuteOperationTransaction` L41-L46（读 `boid` / `enable`）
- **场景用途**：启用校验（如"职位族必须启用才能启用职位"）
- **推荐模式**：新增 Validator 并列注册（参考 `JobEnableValidator` 实现模式）

### 🔌 `beforeExecuteOperationTransaction@disable`

- **时机**：禁用事务前
- **标品插件**：`JobHrDisableOp`（`_auto_operations.md` L210 disable 链第 3 位）
- **1 个校验**：`FormValidate f2843bab0000baac` (enabled)
- **场景用途**：禁用前业务检查（CS-04：检查在职员工 / 关联岗位）
- **风险**：中（阻断业务操作需充分测试）

---

## 六、时态变更阶段（change / newhisversion / confirmchange）

### 🔌 `confirmchange` 复用 JobHrSaveOp

- **时机**：确认变更时走完整保存链
- **3 插件执行链**（`_auto_operations.md` L591-L597）：
  - `HisModelOPCommonPlugin` — 时序通用
  - `HisUniqueValidateOp` — 时序唯一性
  - `JobHrSaveOp` — ⭐ 复用 save 插件（见 III 节）
- **3 校验**：
  - `MustInput 4V/78ID=HERR` (enabled)
  - `GroupFieldUnique 4V/7C958CRVD` 名称唯一 (disabled)
  - `GroupFieldUnique 4V/7I6HFRZ3+` 创建组织+编码唯一 (enabled)

### 🔌 JobHrMsgHandleOp 消息订阅 ⭐ 变更广播

- **作用**：在时序变更时维护版本链 `sourcevid`（`_auto_plugin_semantics.md` L13-L26）
- **3 方法**：
  - `beforeExecuteOperationTransaction(BeforeOperationArgs args)` — 事务前
  - `endOperationTransaction(EndOperationTransactionArgs args)` — 事务结束
  - `afterExecuteOperationTransaction(AfterOperationArgs e)` — 事务后
- **读字段**：`boid` / `iscurrentversion` / `sourcevid`
- **写字段**：`sourcevid`
- **扩展方式**：本场景下业务扩展通常**参考这个类的模式**新建自己的事件发布插件（CS-06），不直接覆盖 `JobHrMsgHandleOp`

### 🔌 `newhisversion` 前置校验

- **1 校验**：`FormValidate 2K+JH30V7OJV` (enabled)（`_auto_operations.md` L555-L556）
- **场景用途**：新增数据版本时的前置业务校验（如"本年度只允许变更 3 次职级"）

---

## 七、删除阶段（delete opKey）

### 🔌 `beforeExecuteOperationTransaction@delete`

- **标品 5 插件**（`_auto_operations.md` L133-L140）：
  - `BaseDataDeletePlugin` / `CodeRuleDeleteOp` / `HRBaseDataStatusOp` / `HRBaseDataLogOp` / `HisModelOPCommonPlugin`
- **关键校验**：`hrbddeletevalidator 2+U=J7R7IEF/` (enabled) — HR 基础资料删除校验
- **⚠️ 标品无 JobHr 专用 op**（对比 save/audit/enable/disable 都有）
- **场景用途**：新建 `JobHrDeleteOp extends HRDataBaseOp` 加业务侧删除检查

---

## 八、字段级扩展点（前端 FormPlugin）

### 🔌 `afterBindData@hbjm_jobhr` → JobHisBasedataEdit 系列

- **标品插件**：
  - `JobHisBasedataEdit`（`_auto_plugin_registry.md` L21）
  - `JobHisBasedataFiledChangeEdit`（`_auto_plugin_semantics.md` L29-L43）
  - `JobBasedataEdit`（`_auto_plugin_registry.md` L25）
- **场景用途**：表单渲染完成后设置字段可见性、默认值、联动规则

### 🔌 `beforeDoOperation` / `afterDoOperation@hbjm_jobhr` → JobHisBasedataFiledChangeEdit

- **7 个方法**（`_auto_plugin_semantics.md` L36-L43）：
  - `afterBindData(EventObject e)`
  - `beforeDoOperation(BeforeDoOperationEventArgs args)`
  - `afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs)`
  - `cacheAttachmentData()` (private)
  - `isAttachmentChange()` (private)
  - `isAttachmentChange(List<Map<String, Object>> attachmentData, List<Map<String, Object>> oldAttachmen)` (public)
  - `attachmentDataMapQuals(Map<String, Object> newAttachmentDataMap, Map<String, Object> oldAttachmentDataM)` (private)
- **关键特性**：附件变更监听（职位说明书附件）

### 🔌 `propertyChanged@hbjm_jobhr` (推荐 CS-02 使用)

- **标品无默认插件**
- **场景用途**：字段联动（如选 `jobseq` 自动带出职级）
- **注册方式**：`AbstractFormPlugin` + `targetType=BILL_FORM`

---

## 九、元数据扩展点（建模期使用）

### `modifyMeta op=add field`
- **用途**：给 `hbjm_jobhr` 加字段
- **接口**：OpenAPI `/modifyMeta`
- **参数**：`op: "add", treeType: "entity", elementType: "field"`
- **Pattern**：`add_field_extension`
- **本场景特点**：**无 4 前缀分录**，单表加即可

### `modifyMeta op=add entity`
- **用途**：给 `hbjm_jobhr` 加子分录（如"职位关键任务"分录）
- **接口**：同上
- **参数**：`elementType: "entity"`

### `addRule`
- **用途**：加规则（formRule 界面联动 / bizRule 跨字段计算）
- **本场景当前**：`_auto_rules.md` 显示 formRule=0 / bizRule=0，意味着所有业务规则都是用 Java 插件实现

### `registerPlugin`
- **targetType 枚举**：`BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT`
- **本场景典型**：前端联动 → BILL_FORM；列表 → LIST_FORM；操作扩展 → OPERATION

### `updateOperation`
- **用途**：改操作配置（加校验、加插件）
- **⚠️ 陷阱**：`plugins` 和 `validations` 是全量替换，必须先 get 再 append

---

## 十、扩展点时序图

### 新建 / 修改职位（save）

```
用户点击保存 → 前端 JobHisBasedataFiledChangeEdit.beforeDoOperation
    ↓
[前端事件结束，发送 HTTP]
    ↓
[onAddValidators] ← 按注册顺序
  ├── CodeRuleOp.onAddValidators (注册 numberValidator)
  ├── BaseDataSavePlugin
  ├── BdVersionSaveServicePlugin
  ├── HRBaseData* × 4
  ├── HisUniqueValidateOp ⭐ 时序唯一
  ├── HisModelOPCommonPlugin
  └── JobHrSaveOp.onAddValidators ⭐ 职位校验
       └── [你的自定义 Validator 并列注册]
    ↓
[beforeExecuteOperationTransaction]
  └── [你的 CS-05 编码生成插件, RowKey=1]
  └── JobHrSaveOp (RowKey=10)
    ↓
[6 validations 按 enabled 顺序执行]
    ↓
[数据入库 t_hbjm_job + _i + jobscmmul]
    ↓
[afterExecuteOperationTransaction]
  └── [你的 CS-06 事件发布插件]
    ↓
[前端 afterDoOperation] → 列表刷新
```

### 启用 (enable)

```
用户点击启用
    ↓
[onAddValidators]
  ├── BaseDataEnablePlugin
  ├── HisModelOPCommonPlugin
  └── JobHrEnableOp.onAddValidators
       └── 注册 JobEnableValidator ⭐
       └── [你的自定义 Validator 并列注册]
    ↓
[beforeExecuteOperationTransaction]
  └── JobHrEnableOp (读 boid + enable)
       └── [你的禁用前检查 - 参考 CS-04 反着来]
    ↓
[数据入库 fenable = 1]
```

### 变更消息（change → confirmchange）

```
用户点击变更 → newhisversion 或 change opKey
    ↓
[产生新版本数据]
    ↓
用户点击确认变更 → confirmchange opKey
    ↓
[3 插件执行链]
  ├── HisModelOPCommonPlugin
  ├── HisUniqueValidateOp
  └── JobHrSaveOp ⭐ 复用 save 插件
    ↓
[JobHrMsgHandleOp 监听]
  ├── beforeExecuteOperationTransaction (暂存旧 sourcevid)
  ├── endOperationTransaction (事务结束前)
  └── afterExecuteOperationTransaction (写新 sourcevid)
    ↓
[你的 CS-06 事件发布插件 - 通知下游岗位模块]
```

---

## 十一、定制入口速查表（按需求类型）

| 用户需求 | 推荐扩展点 | 风险 | CS 参考 |
|---|---|---|---|
| 加字段 | `modifyMeta op=add field` | 低 | CS-01 |
| 选序列带出职级 | `propertyChanged@hbjm_jobhr` (BILL_FORM) | 低 | CS-02 |
| 职级区间校验 | `onAddValidators@save` | 低 | CS-03 |
| 禁用前业务检查 | `beforeExecuteOperationTransaction@disable` | 中 | CS-04 |
| 编码按序列生成 | `beforeExecuteOperationTransaction@save (RowKey=1)` | 低 | CS-05 |
| 变更广播下游 | `afterExecuteOperationTransaction@save + confirmchange` | 中 | CS-06 |
| 审核加双签 | 继承 `JobHrAuditOp` | 中 | - |
| 启用校验 | `onAddValidators@enable` + `JobEnableValidator` 模式 | 低 | - |
| 列表按职位族过滤 | 继承 `JobBaseBuListPlugin` | 中 | - |
| 附件变更监听 | 继承 `JobHisBasedataFiledChangeEdit` | 低 | - |
| 删除业务检查 | 新建 `JobHrDeleteOp` + `beforeExecuteOperationTransaction` | 中 | - |
| 列表超链改目标 | 继承 `JobInitFilterCommonPlugin` | 低 | - |

---

## 十二、扩展点与 Pattern 的映射

| 扩展点 | 对应 Pattern | Pattern 路径 |
|---|---|---|
| `modifyMeta add field` | Pattern A | `pattern/add_field_extension/` |
| `onAddValidators` 自定义 | Pattern · add_unique_validation | `pattern/add_unique_validation/` |
| `propertyChanged` 字段联动 | - | 无独立 Pattern（参考本场景 CS-02） |
| `onAddValidators@enable` + Validator | - | 模仿 `JobEnableValidator`（`rules_chain_all.json` L678） |

---

**📌 来源追溯**：
- 扩展族群：`platform/openapi_capability_map.md`
- 57 插件清单：`_auto_plugin_registry.md` L11-L66
- 5 类反编译类签名：`_auto_plugin_semantics.md` L13-L81
- save 10 插件 + 6 validations：`_auto_operations.md` L99-L122
- enable JobEnableValidator：`rules_chain_all.json` L678-L688
- JobHrEnableOp 3 业务方法：`rules_chain_all.json` L690-L741
- JobHrSaveOp 2 业务方法：`rules_chain_all.json` L368-L403

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin -->

## ISV 扩展指引（基于 HisModelBuFormPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `beforeItemClick`, `beforeDoOperation`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.form.HisModelBuFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit -->

## ISV 扩展指引（基于 JobHisBasedataEdit 真实证）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `beforeBindData`, `afterBindData`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterImportData(kd.bos.entity.datamodel.events.ImportDataEventArgs)`
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

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit -->

## ISV 扩展指引（基于 JobHisBasedataFiledChangeEdit 真实证）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `afterBindData`, `beforeDoOperation`, `afterDoOperation`

### 可重写方法（target.java self）
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public static public static boolean isAttachmentChange(java.util.List<java.util.Map<java.lang.String, java.lang.Object>>, java.util.List<java.util.Map<java.lang.String, java.lang.Object>>)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobHisBasedataFiledChangeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## ISV 扩展指引（基于 HRRelatePageRightDynamicPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `registerListener`, `afterBindData`, `beforeBindData`, `afterDoOperation`, `click`

### 可重写方法（target.java self）
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void loadCustomControlMetas(kd.bos.form.events.LoadCustomControlMetasArgs)`
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void onGetControl(kd.bos.form.events.OnGetControlArgs)`
- `public public void click(java.util.EventObject)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseUtils L307
```java
 305       public static void setCreateField(DynamicObject dy) {
 306           if (HRBaseUtils.getDynamicObjectByField(dy, "creator") == 0L) {
 307 >             Long userId = Long.valueOf(RequestContext.get().getUserId());
 308               dy.set("creator", (Object)userId);
 309           }
```

**QUERY_BUILDER** · HRRelatePageRightDynamicPlugin L213
```java
 211           pageFormShowParameter.setCustomParam("customPKFilter", (Object)customPKFilterMap);
 212           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbss_entitytype");
 213 >         QFilter idFilter = new QFilter("id", "=", (Object)Long.valueOf(reateEntityTypeId));
 214           QFilter[] idFilterArray = new QFilter[]{idFilter};
 215           DynamicObject entityTypeDynamicObj = serviceHelper.queryOne("id,name,relatepageinfo", idFilterArray);
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRBaseUtils L151
```java
 149           }
 150           catch (Exception e) {
 151 >             throw new KDBizException(e.getMessage());
 152           }
 153       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit -->

## ISV 扩展指引（基于 JobBasedataEdit 真实证）

> FQN: `kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `registerListener`, `beforeBindData`, `beforeItemClick`, `afterBindData`, `click`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`, `propertyChanged`, `beforeF7Select`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void dealDisableData()`
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · JobRepository L43
```java
  41       public DynamicObject[] queryByJobClassIds(Collection<Long> ids) {
  42           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbjm_jobhr");
  43 >         QFilter idsFilter = new QFilter("jobclass", "in", ids);
  44           QFilter enableFilter = new QFilter("enable", "=", (Object)"1");
  45           QFilter currentFilter = new QFilter("iscurrentversion", "=", (Object)"1");
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.job.JobBasedataEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin -->

## ISV 扩展指引（基于 HisModelBuListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin/`

### 类型与继承
- 插件类型：**LIST_PLUGIN**
- 父类: `kd.bos.form.plugin.bdctrl.BaseDataListPlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `afterBindData`, `beforeItemClick`, `itemClick`, `beforeDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void filterContainerSearchClick(kd.bos.form.events.FilterContainerSearchClickArgs)`
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeItemClick(kd.bos.form.control.events.BeforeItemClickEvent)` ⭐ lifecycle
- `public public void itemClick(kd.bos.form.control.events.ItemClickEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void closedCallBack(kd.bos.form.events.ClosedCallBackEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HisModelBuListPlugin L224
```java
 222   
 223       private void beforeItemClickUNAssignSearch(BeforeItemClickEvent evt, Long orgid, String appId, String entityName) {
 224 >         int permRes = PermissionServiceHelper.checkPermission((long)RequestContext.get().getCurrUserId(), (String)"DIM_ORG", (long)orgid, (String)appId, (String)entityName, (String)"80513208000000ac");
 225           if (0 == permRes) {
 226               String caption = FormMetadataCache.getFormConfig((String)entityName).getCaption().getLocaleValue();
```

**QUERY_BUILDER** · HisModelBuListPlugin L188
```java
 186                   Set ids = this.getSelectedRows().stream().map(ListSelectedRow::getPrimaryKeyValue).collect(Collectors.toSet());
 187                   HRBaseServiceHelper helper = new HRBaseServiceHelper(entity);
 188 >                 DynamicObject[] dataCol = helper.load("id, org, createorg, ctrlstrategy", new QFilter[]{new QFilter("id", "in", ids)});
 189                   String itemKey = this.getPageCache().get("itemKey");
 190                   if (HRStringUtils.equals((String)itemKey, (String)"bdctrlchange")) {
```

**READ_VIA_HELPER** · HisModelBuListPlugin L237
```java
 235   
 236       private boolean checkCtrlStrategy() {
 237 >         DynamicObject view = BaseDataServiceHelper.getCtrlview((String)this.listProcessor.getEntityNumber());
 238           if (view == null) {
 239               this.getView().showErrorNotification(ResManager.loadKDString((String)"\u83b7\u53d6\u63a7\u5236\u7b56\u7565\u7684\u7ba1\u63a7\u89c6\u56fe\u5931\u8d25\uff0c \u8bf7\u5728\u201c\u57fa\u7840\u6570\u636e\u7ba1\u63a7\u7b56\u7565\u201d\u4e2d\u5148\u914d\u7f6e\u65b0\u589e\u3002", (String)"HisModelBuListPlugin_5", (String)"bos-bd-formplugin", (Object[])new Object[0]));
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.history.list.HisModelBuListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin -->

## ISV 扩展指引（基于 JobInitFilterCommonPlugin 真实证）

> FQN: `kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.basedata.JobInitFilterCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.job.JobListPlugin -->

## ISV 扩展指引（基于 JobListPlugin 真实证）

> FQN: `kd.hrmp.hbjm.formplugin.web.job.JobListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobListPlugin/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `beforePackageData`, `registerListener`, `setFilter`

### 可重写方法（target.java self）
- `public public void addItemClickListeners(java.lang.String...)`
- `public public void beforePackageData(kd.bos.entity.datamodel.events.BeforePackageDataEvent)` ⭐ lifecycle
- `public public void beforeCreateListDataProvider(kd.bos.form.events.BeforeCreateListDataProviderArgs)`
- `public public void packageData(kd.bos.entity.datamodel.events.PackageDataEvent)`
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void billListHyperLinkClick(kd.bos.form.events.HyperLinkClickArgs)`
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRBaseDataConfigUtil L70
```java
  68   
  69       private static Map<String, Object> getConfigParams() {
  70 >         long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72           return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
```

**READ_VIA_HELPER** · HRBaseDataConfigUtil L72
```java
  70           long orgId = RequestContext.get().getOrgId() == 0L ? OrgServiceUtil.getHRRootOrgId() : RequestContext.get().getOrgId();
  71           AppParam appParam = new AppParam("XYRL3+A8Z+Z", Long.valueOf(orgId));
  72 >         return SystemParamServiceHelper.loadAppParameterFromCache((AppParam)appParam);
  73       }
  74   
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.formplugin.web.job.JobListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.formplugin.web.job.JobListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp -->

## ISV 扩展指引（基于 JobHrSaveOp 真实证）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp -->

## ISV 扩展指引（基于 JobHrAuditOp 真实证）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp -->

## ISV 扩展指引（基于 JobHrDisableOp 真实证）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp -->

## ISV 扩展指引（基于 JobHrEnableOp 真实证）

> FQN: `kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hrmp.hbjm.opplugin.web.job.JobHrMsgHandleOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbjm.opplugin.web.job.JobHrEnableOp -->

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
