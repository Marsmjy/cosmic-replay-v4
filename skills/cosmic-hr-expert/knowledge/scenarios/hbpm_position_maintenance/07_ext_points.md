# 扩展点全图 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `_auto_plugin_registry.md` 37 插件 + 7 类 position 反编译 + `_auto_operations.md` 57 opKey 执行链整合
> **视角**: 本场景所有扩展点，按"实体 × 时机"二维定位
> **confidence**: verified（类名、方法签名、读写字段均来自 jar 反编译）

---

## ⭐ 机器校准总览（2026-04-24 `_auto_plugin_registry.md`）

**本场景 37 插件分 3 大族群**：

### 岗位域特有插件（10 个，`kd.hrmp.hbpm.*`）

| # | 类名 | 父类 | 主要方法 | 生命周期 |
|---|---|---|---|---|
| 1 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrCommonOp` | `HRDataBaseOp` | `onAddValidators` + `afterExecuteOperationTransaction` + `isImport` | 公共基类 · 被多个 Op 继承 |
| 2 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp` | `PositionHrCommonOp` | `onAddValidators` + `beforeExecuteOperationTransaction` + `beginOperationTransaction` | save / confirmchange |
| 3 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp` | `AbstractOperationServicePlugIn` | `onPreparePropertys` + `onAddValidators` + `beforeExecuteOperationTransaction` + `beginOperationTransaction` + `afterExecuteOperationTransaction` | disable |
| 4 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp` | `AbstractOperationServicePlugIn` | `onPreparePropertys` + `onAddValidators` + `beforeExecuteOperationTransaction` + `beginOperationTransaction` + `afterExecuteOperationTransaction` | enable |
| 5 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp` | `PositionHrCommonOp` | `onAddValidators` + `beforeExecuteOperationTransaction` + `beginOperationTransaction` | confirmchange |
| 6 | `kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp` | `HRDataBaseOp` | `onAddValidators` + `beginOperationTransaction` | dochangerelation |
| 7 | `kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp` | `HRDataBaseOp` | `onAddValidators` + `beforeExecuteOperationTransaction` + `beginOperationTransaction` | his_save |
| 8 | `kd.hrmp.hbpm.formplugin.web.position.PositionEdit` | `HRDataBaseEdit` | `preOpenForm` / `registerListener` / `afterBindData` / `propertyChanged` / `beforeDoOperation` / `afterDoOperation` / `beforeF7Select` / `closedCallBack` / `click` / `beforeClosed` / `pageRelease` | 主编辑表单 |
| 9 | `kd.hrmp.hbpm.formplugin.web.position.PositionList` | `HRDataBaseList` | `setFilter` / `filterContainerInit` / `beforeDoOperation` / `doMutex` | 列表 |
| 10 | `kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin` | - | - | 右侧动态插件 |
| 11 | `kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin` | - | - | BU 列表基类 |

### 岗位域 Validator（4 个 · 本场景独有）

| # | 类名 | 父类 | 激活条件 |
|---|---|---|---|
| 12 | `kd.hrmp.hbpm.opplugin.web.position.validate.PositionImptRuleNumberValidator` | `HRDataBaseValidator` | save + isImport() |
| 13 | `kd.hrmp.hbpm.opplugin.web.position.validate.JobLevelGradeRangeImportValidator` | `HRDataBaseValidator` | his_save (+ 新增 `JobLevelGradeRangeImportValidator(true)`) |
| 14 | `kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisLoopValidator` | `HRDataBaseValidator` | his_save 时 `.checkHis()` 激活 |
| 15 | `kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisValidator` | `HRDataBaseValidator` | his_save |

### 岗位域 Import/Export 专属插件（6 个）

| # | 类名 | 场景 |
|---|---|---|
| 16 | `kd.hrmp.hbpm.formplugin.web.impt.PositionHRImportPlugin` | importdata_hr · 岗位中台导入 |
| 17 | `kd.hrmp.hbpm.formplugin.web.impt.PositionRuleNumberImportPlugin` | importdata + importdata_hr · 编码规则控制 |
| 18 | `kd.hrmp.hbpm.formplugin.web.impt.PositionRelationImportPlugin` | relationimport · 协作关系导入 |
| 19 | `kd.hrmp.hbpm.formplugin.web.impt.PositionReviseImportPlugin` | reviseimport · 修订导入 |
| 20 | `kd.hrmp.hbpm.formplugin.web.impt.PositionReviseHRImportPlugin` | import_positiondetailrevise · 批量修订属性 |
| 21 | `kd.hrmp.hbpm.formplugin.web.impt.PositionHRExportPlugin` | export_from_impttpl_hr / export_from_expttpl_hr |

**证据**：`_auto_plugin_registry.md` L18-L22 + L40-L47 + 7 类反编译 `_sdk_audit/_decompiled/scenarios/position/*.java`

### HR 通用模板插件（约 20 个，`kd.hr.hbp.*`）

- 基础资料编辑族：`HRBaseDataTplEdit` / `HRBaseDataTplList` / `HRBaseDataImportEdit` / `HRHiesButtonSwitchPlugin`
- 时序模型族（8 个）：`HisModelFormCommonPlugin` / `HisModelListCommonPlugin` / `HisModelF7ListPlugin` / `HisModelFilterPanelListPlugin` / `HisModelFilterPanelF7ListPlugin` / `HisModelMobileListPlugin` / `HisBaseDataF7FastFilter` / `HisModelOPCommonPlugin` / `HisUniqueValidateOp`
- 基础资料操作族：`HRBaseDataStatusOp` / `HRBaseDataLogOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBasedataLogList`
- 关联页面：`HRRelatePageRightDynamicPlugin`
- F7 过滤：`OrgDisableAndIncludeFilterListPlugin` / `PositionBaseDataF7FastFilter`

### 苍穹基础框架插件（约 7 个，`kd.bos.*`）

- 编码规则：`CodeRulePlugin` + `CodeRuleOp`
- 基础资料版本：`BdVersionListPlugin` + `BdVersionSaveServicePlugin`
- 基础资料名称版本：`BaseDataNameVersionListPlugin`
- 模板基类：`templatebaseedit`

> 📌 **执行顺序**：同一生命周期方法（如多个 `onAddValidators`）按注册顺序依次执行（遵循 PR-002）。完整清单见 [_auto_plugin_registry.md](_auto_plugin_registry.md)。

---

## 一、扩展点的双维度理解

扩展点 = **"时机 × 宿主实体"** 的二维定位

苍穹有 **5 大扩展族群**（来自 platform/openapi_capability_map）：

1. **建模族**：`buildMeta` / `modifyMeta` / `getFormSchema` / `queryForms`
2. **操作族**：`listOperations` / `updateOperation`
3. **插件族**：`registerPlugin` / `queryEditable`
4. **规则族**：`addRule` (formRule/bizRule 两层)
5. **PDM 族**：跨 server / datamodel 两套环境

**本场景用到的**：2 + 3 + 4（CRUD / 启停用 / 变更 / 汇报关系都会触发运行时扩展点）

---

## 二、列表展示阶段

### 🔌 `setFilter@hbpm_positionhr` (列表) → `PositionList`

- **宿主**：`hbpm_positionhr`（列表视图）
- **时机**：列表查询前追加过滤条件
- **接口**：`IListPlugin#setFilter(SetFilterEvent setFilterEvent)`
- **标品插件**：`kd.hrmp.hbpm.formplugin.web.position.PositionList` L52-L63
- **父类**：`HRDataBaseList`
- **super 调用**：`super.setFilter(setFilterEvent)` 走父类链
- **标品默认**：
  - `isstandardpos=1` 当 `viewstdpos=1`（标岗 F7 视图）
  - `isstandardpos=0` 其他
  - `isdeleted != 1` 排除软删除
  - orderBy `adminorg.id asc, positiontype.id asc, isleader desc`
- **场景用途**：定制列表默认过滤规则、加数据权限
- **风险**：中
- **推荐模式**：继承 + super 调用 + 追加 filter（**遵循 PR-001 · 不覆盖**）

### 🔌 `filterContainerInit@hbpm_positionhr` → `PositionList`

- **时机**：筛选容器初始化
- **标品**：`PositionList.filterContainerInit` L44-L50 · hisPage=VERSION_LIST_PAGE 时移除 datastatus 过滤列
- **场景用途**：按视图切换可见的筛选项

### 🔌 `beforeDoOperation@hbpm_positionhr` (列表) → `PositionList`

- **时机**：操作前处理
- **标品**：`PositionList.beforeDoOperation` L65-L93 · hisversion_view 加 Mutex 互斥锁
- **场景用途**：批量操作前置校验 / 互斥控制

---

## 三、新增 / 修改阶段（save opKey）

### 🔌 `onAddValidators@save` ⭐⭐ 本场景最常扩展

- **宿主**：`hbpm_positionhr`
- **时机**：保存校验器注册（**事务未开始** · PR-010）
- **接口**：`onAddValidators(AddValidatorsEventArgs e)`
- **事务阶段**：`pre_transaction`

- **标品插件执行链 9 个**（`_auto_operations.md` L94-L107 · 按 RowKey）：
  1. `kd.bos.business.plugin.CodeRuleOp` — 编码规则（onAddValidators 注册 numberValidator）
  2. `kd.bos.base.bdversion.BdVersionSaveServicePlugin` — 基础资料版本管理
  3. `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` — HR 状态
  4. `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` — HR 日志（默认 disabled）
  5. `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` — HR 启用态
  6. `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` — 时序通用
  7. `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` — HR 原始值
  7. `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` — 时序唯一性
  9. `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp` ⭐ — 岗位保存
     - `onAddValidators` L43-L49：`isImport()` 时额外注册 `PositionImptRuleNumberValidator`

- **1 条 validations**（`_auto_operations.md` L112）：
  - `MustInput 6096194600001fac` (enabled)

- **场景用途**：80% 的保存期业务扩展发生在这里
- **风险**：⚠️ **中**（丢失时序 / 唯一性校验会导致脏数据）
- **推荐模式**：**并列挂** Validator（PR-001 · 不继承 `PositionHrSaveOp`）

### 🔌 `beforeExecuteOperationTransaction@save`

- **时机**：保存前（事务前）· PR-010 第 5 阶段
- **标品行为**（`PositionHrSaveOp.java` L51-L73）：
  - 读 `unitPositionMap` 导入变量 · 批量覆盖已有 id
  - `entity.set("org", entity.get("adminorg.org"))` — ⭐ 自动写 org 派生（PR-003 · OP 用 entity.set）
  - 新建时批量重建 parent 引用（导入场景新旧映射）
- **场景用途**：编码自动生成（CS-04）/ 业务字段预处理 / 引用数据校验

### 🔌 `beginOperationTransaction@save`

- **时机**：事务开始（PR-010 第 6 阶段）
- **标品行为**（`PositionHrSaveOp.java` L75-L101）：
  - super 调用走 `PositionHrCommonOp`
  - `IPositionRelationServiceApplication.saveSysRelation` + `saveCooperationRelation`
  - 查三分类 + 写 `changetype/changeoperate/changescene` 到 afterVersions
  - `IPositionHrServiceApplication.afterSavePosition` 通知下游缓存
- **风险**：⚠️ **中** · 事务已开始 · 扩展需小心数据一致性

### 🔌 `afterExecuteOperationTransaction@save`

- **时机**：事务提交后（PR-010 第 9 阶段 · **发业务事件安全**）
- **标品行为**（`PositionHrCommonOp.afterExecuteOperationTransaction` L28-L35）：
  - 非 import 时调 `ChangeMsgServiceImpl.sendMsg()`（启动调度任务发变更消息）
  - `IBosPositionService.addOrUpdatePositions(boids)` 更新下游缓存
- **场景用途**：消息推送 / 外部系统同步 / **事件发布**（CS-05 · PR-011 走 BEC）

---

## 四、启用 / 禁用阶段（enable / disable opKey）

### 🔌 `onAddValidators@disable` ⭐⭐ 本场景高价值扩展点

- **时机**：禁用前校验器注册
- **标品插件**：`PositionHrDisableOp`（`_auto_operations.md` L122-L128）
- **⚠ 关键认知**：`PositionHrDisableOp.onAddValidators` **是空**（`PositionHrDisableOp.java` L49-L50）· 标品业务阻断在 `afterExecuteOperationTransaction` 的 `IBosPositionService.disablePositions` 内部 · 事务已开始
- **扩展建议**：**并列挂** 新插件 · 在 `onAddValidators` 前置强阻断（CS-03）
- **场景用途**：禁用前业务检查（CS-03：检查下游在职员工 / 任职关系）· 关键 P0

### 🔌 `beforeExecuteOperationTransaction@disable`

- **时机**：禁用事务前
- **标品行为**（`PositionHrDisableOp.java` L52-L55）：
  - 查 `boid → latestHisId` 映射

### 🔌 `beginOperationTransaction@disable`

- **标品行为**（`PositionHrDisableOp.java` L57-L72）：
  - 写三分类 1040/1030/1040
  - sourcevid 指向上一版
  - `IPositionHrServiceApplication.afterSavePosition`

### 🔌 `afterExecuteOperationTransaction@disable`

- **标品行为**（`PositionHrDisableOp.java` L74-L79）：
  - `ChangeMsgServiceImpl.sendMsg()` 发消息
  - ⭐ `IBosPositionService.disablePositions(positionIds)` **委托底层业务校验**
- **扩展建议**：如需补充下游通知 · 走 BEC（CS-05）

### 🔌 `onAddValidators@enable`

- **时机**：启用前校验器注册
- **标品插件**：`PositionHrEnableOp`（`_auto_operations.md` L131-L141）
- **关键特性**（`PositionHrEnableOp.java` L45-L94）：
  - `onPreparePropertys` L49-L63：声明所有非多语言字段 + 非 `_id` 后缀
  - `onAddValidators` L65-L66：空（可扩展）
  - `beforeExecuteOperationTransaction` L68-L71：查 boid → latestHisId 映射
  - `beginOperationTransaction` L73-L88：写三分类 1070/1070/1070
  - `afterExecuteOperationTransaction` L90-L94：发消息 + `IBosPositionService.commonSyncPositions()`
- **场景用途**：启用校验（如"上级岗位必须启用才能启用本岗位"）
- **推荐模式**：并列挂 Validator（参考 `PositionHrEnableOp.onAddValidators` 本身为空的设计）

---

## 五、时态变更阶段（confirmchange / change / newhisversion / his_save）

### 🔌 `confirmchange` 走 `PositionHrChangeOp`

- **时机**：确认变更
- **3 插件执行链**（`_auto_operations.md` L375-L382）：
  - `HisModelOPCommonPlugin` — 时序通用
  - `HisUniqueValidateOp` — 时序唯一性
  - `PositionHrChangeOp` ⭐
- **1 校验**：`MustInput 4UXPTMTC=NCR` (enabled)
- **PositionHrChangeOp 核心行为**（`PositionHrChangeOp.java`）：
  - 仅处理 `!iscurrentversion` 的新版本
  - 写三分类 1020/1020/1020
  - import 时额外调 `changeCooperationRelation`

### 🔌 `his_save` 历史保存 ⭐ 时序完整性防线

- **时机**：历史版本迁移 / 批量导入历史数据
- **标品插件**：`PositionHisSaveOp`（`_auto_operations.md` L463-L473）
- **⭐ 3 Validator 注册**（`PositionHisSaveOp.onAddValidators` L60-L64）：
  - `JobLevelGradeRangeImportValidator(true)` — 职级/职等范围校验（isExistMustRelateJobParam=true）
  - `PositionHisLoopValidator.checkHis()` — 成环校验（激活 `checkSysRel`）
  - `PositionHisValidator` — 时序数据完整性（9 条检查）
- **beforeExecuteOperationTransaction 行为**（`PositionHisSaveOp.java` L66-L71）：
  - 排序 positionHisArray（按 boid, bsed）
  - `setDefaultValue`：自动填 org = adminorg.org / establishmentdate
  - `calcBsledByNextBsed`：按下一版 bsed - 1 天 自动算 bsled
- **beginOperationTransaction 行为** L104-L114：
  - 调 `HisModelServiceHelper.createDataVersions` 创建历史版本
  - 过滤 `!iscurrentversion` → `IPositionRelationServiceApplication.saveSysRelation`
- **扩展用途**：历史迁移前置业务校验（如"不允许导入 1970 年前数据"等）
- **推荐模式**：并列挂 Validator

### 🔌 `newhisversion` 无插件（donothing）

- **时机**：新增数据版本
- **机制**：前端 → `hispage` 模式 → 带入当前版本字段 → 用户填新 `bsed` → 走 save / confirmchange 入库
- **扩展点**：前端拦截 `beforeDoOperation@newhisversion` 或后端拦截 save 链

---

## 六、汇报 / 协作关系阶段（岗位独有）

### 🔌 `dochangerelation` 汇报关系确认

- **时机**：确认汇报/协作关系变更
- **标品插件**：`PositionHrRelationChangeOp`（`_auto_operations.md` L427-L443）
- **1 校验**：`MustInput 4T7SDY=13RY6`
- **PositionHrRelationChangeOp 行为**（`PositionHrRelationChangeOp.java`）：
  - `IPositionRelationServiceApplication.changeCooperationRelation(positions)` 变更协作关系
  - 写三分类 1030/1020/1030
  - `IPositionHrServiceApplication.afterSavePosition`

### 🔌 `reportchange` / `do_close` 纯前端 opKey

- **机制**：UI 状态切换 · 无后端插件
- **标品前端逻辑**：`PositionEdit.afterDoOperationForReportChange` L426-L444 + `afterDoOperationForReportChangeConfirm` L994-L1004
- **扩展用途**：变更态 UI 控制 / 业务检查

### 🔌 `newentry` / `deleteentry` 分录操作

- **机制**：entryentity 协作关系分录增删
- **标品行为**：平台 `newentry` / `deleteentry` 类型 · EntryId=`37vqW7g8w5nh2fSXuh`
- **扩展用途**：分录校验（如"最多 10 条协作关系"）

---

## 七、字段级扩展点（前端 FormPlugin）

### 🔌 `afterBindData@hbpm_positionhr` → PositionEdit

- **标品插件**：
  - `PositionEdit.afterBindData` L238-L292 · 处理 adminorg / positiontpl / 控件可见性 / 历史查询日期
  - `HRBaseDataTplEdit` / `HisModelFormCommonPlugin` / `HRRelatePageRightDynamicPlugin` / `HisModelFilterPanelF7ListPlugin` 共 5 个 `afterBindData` 钩子（见 `_auto_plugin_registry.md` L56-L62）
- **场景用途**：表单渲染完成后设置字段可见性、默认值、联动规则

### 🔌 `propertyChanged@hbpm_positionhr` → PositionEdit

- **6 分支**（`PositionEdit.propertyChanged` L455-L515）：
  - `adminorg` → 带出 countryregion / city / workplace / org · 置空 positiontpl
  - `city` → handleCity · 若 countryregion 空 · 自动填 · 按 (country,city) 查 workplace
  - `countryregion` → 置 city null
  - `parent` → 重算 parent 显示名
  - `bsed` → 若 establishmentdate 空 · 自动同值
  - `positiontpl` → `changePositionTpl` 批量回填 8 字段
- **非分支触发**：number-触发字段集 → changeNumber 重新生成编码
- **场景用途**：字段联动（如 CS-02 选类型带默认值）· 注意 PR-004 死循环

### 🔌 `beforeF7Select@hbpm_positionhr` → PositionEdit

- **5 F7 分支**（`PositionEdit.beforeF7Select` L905-L948）：
  - `reportType` → BatchFill=false
  - `city` → 按 countryregion 过滤
  - `positiontpl` → 按 adminorg 查可应用范围
  - `adminorg` → 按权限过滤
  - `parent` → 排除自身 boid（⭐ 成环预防）
  - `targetpos/parent/adminorg` → 按 bsed 过去日期传 `effectdate`
- **场景用途**：F7 定制过滤 / 权限范围

### 🔌 `beforeDoOperation` / `afterDoOperation@hbpm_positionhr` → PositionEdit

- **标品行为**：
  - `beforeDoOperation` L294-L304：save/confirmchange 传 bsed 给后端 · 所有操作设置 isFromPage=1 / appId
  - `afterDoOperation` L306-L365：reviseedit / future_modify / change / reportchange / confirmchangenoaudit / dochangerelation / save / confirmchange / do_close 9 分支 UI 切换
- **场景用途**：操作前/后 UI 控制 + 业务逻辑（推荐并列挂 · 不覆盖）

### 🔌 `closedCallBack@hbpm_positionhr` → PositionEdit

- **标品**：`PositionEdit.closedCallBack` L551-L559 · 处理 `hbjm_joblevelrange` / `hbjm_jobgraderange` 范围选择回调
- **场景用途**：子页面关闭回调处理

---

## 八、元数据扩展点（建模期使用）

### `modifyMeta op=add field`
- **用途**：给 `hbpm_positionhr` 加字段
- **接口**：OpenAPI `/modifyMeta`
- **参数**：`op: "add", treeType: "entity", elementType: "field"`
- **Pattern**：`add_field_extension`
- **本场景特点**：**无 `_l` 多语言子表**，多语言字段直接落主表

### `modifyMeta op=add entity`
- **用途**：给 `hbpm_positionhr` 加子分录
- **接口**：同上
- **参数**：`elementType: "entity"`
- **本场景已有**：`entryentity`（协作关系）+ `applicableorgentity`（标准岗位适用组织）

### `addRule`
- **用途**：加规则（formRule 界面联动 / bizRule 跨字段计算）
- **本场景当前**：`_auto_rules.md` 显示 formRule=0 / bizRule=0 · 业务规则都是 Java 插件实现

### `registerPlugin`
- **targetType 枚举**：`BILL_FORM` / `LIST_FORM` / `MOBILE_BILL_FORM` / `OPERATION` / `EVENT`
- **本场景典型**：
  - 前端联动 → `BILL_FORM`（CS-02）
  - 列表 → `LIST_FORM`
  - 操作扩展 → `OPERATION`（CS-03 / CS-04 / CS-05）
  - BEC 订阅 → `EVENT`（CS-05 下游订阅方）

### `updateOperation`
- **用途**：改操作配置（加校验、加插件）
- **⚠️ 陷阱**：`plugins` 和 `validations` 是全量替换，必须先 get 再 append（EX-20）

---

## 九、扩展点时序图

### 新建 / 修改岗位（save）

```
用户点击保存 → 前端 PositionEdit.beforeDoOperation (set bsed/appId/isFromPage)
    ↓
[前端事件结束，发送 HTTP]
    ↓
[onAddValidators] ← 按注册顺序
  ├── CodeRuleOp.onAddValidators (注册 numberValidator)
  ├── HRBaseDataStatusOp.onAddValidators
  ├── HisModelOPCommonPlugin.onAddValidators
  ├── HisUniqueValidateOp.onAddValidators ⭐ 时序唯一
  └── PositionHrSaveOp.onAddValidators
       │
       if (isImport()) args.addValidator(new PositionImptRuleNumberValidator())
       │
       └── [你的自定义 Validator 并列注册 · 如 CS-03 变种]
    ↓
[beforeExecuteOperationTransaction]
  ├── [你的 CS-04 编码生成插件, RowKey=0]
  ├── HRBaseDataStatusOp
  ├── HRBaseDataLogOp
  ├── HRBaseDataEnableOp
  ├── HisModelOPCommonPlugin
  ├── HRBaseOriginalOp
  └── PositionHrSaveOp (RowKey=9)
       entity.set("org", adminorg.org)
       批量重建 parent 引用 (导入场景)
    ↓
[1 validation 执行: MustInput]
    ↓
[beginOperationTransaction]
  └── PositionHrSaveOp.beginOperationTransaction
       IPositionRelationServiceApplication.saveSysRelation
       IPositionRelationServiceApplication.saveCooperationRelation
       查三分类 · 写 changetype/changeoperate/changescene (1010)
       IPositionHrServiceApplication.afterSavePosition
    ↓
[数据入库 t_hbpm_position + t_hbpm_standposentry]
    ↓
[afterExecuteOperationTransaction]
  └── PositionHrCommonOp.afterExecuteOperationTransaction
       if (!isImport()) ChangeMsgServiceImpl.sendMsg()
       IBosPositionService.addOrUpdatePositions(boids)
  └── [你的 CS-05 BEC 事件发布插件 · PR-011]
    ↓
[前端 afterDoOperation] → 根据 bsed 刷新 or 跳未来视图
```

### 启用 (enable)

```
用户点击启用
    ↓
[onPreparePropertys] PositionHrEnableOp 声明全字段
    ↓
[onAddValidators]
  ├── HisModelOPCommonPlugin.onAddValidators
  └── PositionHrEnableOp.onAddValidators (空)
       └── [你的自定义 Validator 并列注册]
    ↓
[beforeExecuteOperationTransaction]
  └── PositionHrEnableOp (查 boid → latestHisId)
    ↓
[beginOperationTransaction]
  └── PositionHrEnableOp (写 sourcevid + 三分类 1070)
    ↓
[数据入库 fenable = 1]
    ↓
[afterExecuteOperationTransaction]
  └── PositionHrEnableOp (ChangeMsgServiceImpl.sendMsg + commonSyncPositions)
```

### 禁用 (disable) + CS-03 前置阻断

```
用户点击禁用
    ↓
[onAddValidators]
  ├── HisModelOPCommonPlugin.onAddValidators
  ├── PositionHrDisableOp.onAddValidators (空)
  └── [你的 CS-03 Validator 并列注册 · PR-010 最早阶段阻断]
       查 Tier 1 下游 5 实体 → 有引用就 addErrorMessage
    ↓
[beforeExecuteOperationTransaction]
  └── PositionHrDisableOp (查 boid → latestHisId)
    ↓
[beginOperationTransaction]
  └── PositionHrDisableOp (写 sourcevid + 三分类 1040/1030/1040)
    ↓
[数据入库 fenable = 0]
    ↓
[afterExecuteOperationTransaction]
  └── PositionHrDisableOp
       ChangeMsgServiceImpl.sendMsg
       IBosPositionService.disablePositions (底层业务校验 · 兜底)
```

### 变更消息（confirmchange）

```
用户点击确认变更 → confirmchange opKey
    ↓
[3 插件执行链]
  ├── HisModelOPCommonPlugin
  ├── HisUniqueValidateOp
  └── PositionHrChangeOp ⭐
       └── beginOperationTransaction
            filter(!iscurrentversion) 只处理新版本
            写三分类 1020/1020/1020
            IPositionRelationServiceApplication.saveSysRelation
            if (importtype) changeCooperationRelation
    ↓
[你的 CS-05 BEC 事件发布插件 · afterExecuteOperationTransaction]
  → 通知下游薪酬/绩效/招聘模块
```

---

## 十、定制入口速查表（按需求类型）

| 用户需求 | 推荐扩展点 | 风险 | CS 参考 |
|---|---|---|---|
| 加字段 | `modifyMeta op=add field` | 低 | CS-01 |
| 选类型带默认值 | `propertyChanged@hbpm_positionhr` (BILL_FORM) | 低 | CS-02 |
| 禁用前检查在职员工 ⭐ P0 | `onAddValidators@disable`（并列挂） | 中 | CS-03 |
| 编码按类型前缀生成 | `beforeExecuteOperationTransaction@save RowKey=0` | 低 | CS-04 |
| 变更广播下游 | `afterExecuteOperationTransaction@save/enable/disable/confirmchange` + BEC | 中 | CS-05 |
| 启用校验（上级岗位必须启用） | `onAddValidators@enable` + Validator 并列 | 低 | - |
| 汇报关系成环高级校验 | `onAddValidators@dochangerelation` | 中 | - |
| 历史数据迁移前置校验 | `onAddValidators@his_save` | 中 | - |
| 列表按标岗 / 业务岗切换 | 继承 `PositionList` + super.setFilter | 中 | - |
| F7 岗位过滤 | `beforeF7Select` 分支扩展 | 低 | - |
| 协作关系导入定制 | `registerPlugin` 到 `relationimport` | 中 | - |
| 删除业务检查 | ⚠️ 岗位无标准 delete · 靠软删除 isdeleted · 扩展在 `beforeExecuteOperationTransaction@disable` | 中 | - |
| 分录字段校验 | `onAddValidators@save` + `ExtendedDataEntity.dataEntity.getDynamicObjectCollection("applicableorgentity")` | 低 | - |

---

## 十一、扩展点与 Pattern 的映射

| 扩展点 | 对应 Pattern | Pattern 路径 |
|---|---|---|
| `modifyMeta add field` | Pattern A | `pattern/add_field_extension/` |
| `onAddValidators` 自定义 | Pattern · add_unique_validation | `pattern/add_unique_validation/` |
| `propertyChanged` 字段联动 | - | 无独立 Pattern（参考本场景 CS-02） |
| `onAddValidators@disable` + Validator | - | 模仿 CS-03（下游引用检查） |
| `afterExecuteOperationTransaction` + BEC | - | 通用 BEC 模式（PR-011） |

---

## 十二、各生命周期方法的标品插件覆盖率（from `_auto_plugin_registry.md`）

| 生命周期方法 | 标品实现数 | 关键插件 |
|---|---|---|
| `afterBindData` | 5 | HRBaseDataTplEdit / HisModelFormCommonPlugin / HRRelatePageRightDynamicPlugin / HisModelFilterPanelF7ListPlugin / HRHiesButtonSwitchPlugin |
| `afterCreateNewData` | 1 | HisModelFormCommonPlugin |
| `afterDoOperation` (FormPlugin) | 4 | HRBaseDataTplEdit / HisModelFormCommonPlugin / HRRelatePageRightDynamicPlugin / HisModelListCommonPlugin |
| `afterExecuteOperationTransaction` (OP) | 2 | HRBaseDataLogOp / HisModelOPCommonPlugin（+ 岗位专属 PositionHr*Op） |
| `afterLoadData` | 2 | HRBaseDataTplEdit / HisModelFormCommonPlugin |
| `beforeBindData` | 7 | HisModelFormCommonPlugin / HRRelatePageRightDynamicPlugin / HRBaseDataTplList / HRBasedataLogList / HisModelListCommonPlugin / HisModelF7ListPlugin / HisModelFilterPanelF7ListPlugin |
| `beforeDoOperation` (FormPlugin) | 5 | HRBaseDataTplEdit / HisModelFormCommonPlugin / HRBasedataLogList / HisModelListCommonPlugin / HisModelFilterPanelF7ListPlugin |
| `beforeExecuteOperationTransaction` (OP) | 5 | HRBaseDataStatusOp / HRBaseDataLogOp / HRBaseDataEnableOp / HisModelOPCommonPlugin / HRBaseOriginalOp（+ 岗位专属） |
| `onAddValidators` (OP) | 3 | HRBaseDataStatusOp / HisModelOPCommonPlugin / HisUniqueValidateOp（+ 岗位专属） |
| `propertyChanged` | 2 | HisModelF7ListPlugin / HisModelFilterPanelF7ListPlugin |

---

**📌 来源追溯**：
- 扩展族群：`platform/openapi_capability_map.md`
- 37 插件清单：`_auto_plugin_registry.md` L11-L47
- 6 类反编译类签名：`_sdk_audit/_decompiled/scenarios/position/*.java` 7 类
- save 9 插件 + 1 validations：`_auto_operations.md` L94-L112
- disable 2 插件：`_auto_operations.md` L122-L128
- enable 2 插件：`_auto_operations.md` L131-L141
- confirmchange 3 插件：`_auto_operations.md` L375-L382
- dochangerelation 1 插件：`_auto_operations.md` L427-L443
- his_save 1 插件 + 3 Validator：`_auto_operations.md` L463-L473 + `PositionHisSaveOp.onAddValidators` L60-L64
- 生命周期方法统计：`_auto_plugin_registry.md` L51-L147

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

## ISV 扩展指引（基于 PositionEdit 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionEdit`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionEdit/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseEdit`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `preOpenForm`, `registerListener`, `afterLoadData`, `beforeBindData`, `afterBindData`, `beforeDoOperation`, `afterDoOperation`, `click`, `propertyChanged`, `closedCallBack`, `beforeClosed`, `beforeF7Select`, `beforeItemClick`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void registerListener(java.util.EventObject)` ⭐ lifecycle
- `public public void afterLoadData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void afterBindData(java.util.EventObject)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void click(java.util.EventObject)` ⭐ lifecycle
- `public public void propertyChanged(kd.bos.entity.datamodel.events.PropertyChangedArgs)` ⭐ lifecycle
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

**QUERY_BUILDER** · PositionBillQueryRepository L42
```java
  40       public DynamicObject[] queryPositionDataByNumbers(List<String> numbers) {
  41           HRBaseServiceHelper serviceHelper = new HRBaseServiceHelper("hbpm_positionhr");
  42 >         QFilter qFilter = new QFilter("number", "in", numbers);
  43           QFilter isdeletedFilter = new QFilter("isdeleted", "!=", (Object)"1");
  44           QFilter currentVersionFilter = new QFilter("iscurrentversion", "=", (Object)"1");
```

**READ_VIA_HELPER** · HRDateTimeUtils L1410
```java
1408       public static Date getSysMaxDate() {
1409           Date maxDate = null;
1410 >         DynamicObject configDy = QueryServiceHelper.queryOne((String)"hrcs_sysmaxdateconfig", (String)"maxenddate", (QFilter[])new QFilter[0]);
1411           if (configDy == null || configDy.getDate("maxenddate") == null) {
1412               LocalDate localDate = LocalDate.of(2999, 12, 31);
```

**CALL_CROSS_SERVICE** · PositionEdit L924
```java
 922       private void setTips() {
 923           Tips tips = new Tips();
 924 >         List tipsStr = (List)HRMServiceHelper.invokeHRMPService((String)"hrcs", (String)"IHRCSService", (String)"queryPromptForString", (Object[])new Object[]{this.getModel().getDataEntityType().getName(), "parent", this.getModel().getDataEntity()});
 925           if (!CollectionUtils.isEmpty((Collection)tipsStr)) {
 926               tips.setContent(new LocaleString((String)tipsStr.get(0)));
```

**THROW_BIZ_EXCEPTION** · HRDateTimeUtils L175
```java
 173                   LOGGER.debug("parseDate info:" + str + ",fmt:" + YYYY_MM_DD);
 174               }
 175 >             throw new KDBizException("parseDate info:" + date + ",fmt:" + YYYY_MM_DD);
 176           }
 177       }
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionEdit/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionEdit/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

## ISV 扩展指引（基于 PositionPageRightDynamicPlugin 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDynamicFormBasePlugin`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `preOpenForm`, `beforeBindData`

### 可重写方法（target.java self）
- `public public void preOpenForm(kd.bos.form.events.PreOpenFormEventArgs)` ⭐ lifecycle
- `public public void beforeBindData(java.util.EventObject)` ⭐ lifecycle

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

## ISV 扩展指引（基于 PositionList 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionList`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionList/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`, `setFilter`, `beforeDoOperation`, `afterDoOperation`, `closedCallBack`

### 可重写方法（target.java self）
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `public public void beforeDoOperation(kd.bos.form.events.BeforeDoOperationEventArgs)` ⭐ lifecycle
- `public public void afterDoOperation(kd.bos.form.events.AfterDoOperationEventArgs)` ⭐ lifecycle
- `public public void showSortViewPage(kd.bos.form.IFormView, java.util.List<java.lang.Long>)`
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

**QUERY_BUILDER** · PositionQueryRepository L62
```java
  60   
  61       public DynamicObject[] queryPositionsById(List<Long> ids) {
  62 >         QFilter idFilter = new QFilter("id", "in", ids);
  63           return this.serviceHelper.loadDynamicObjectArray(new QFilter[]{idFilter});
  64       }
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionList/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionList/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

## ISV 扩展指引（基于 AbstractBUListPlugin 真实证）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `filterContainerInit`

### 可重写方法（target.java self）
- `public public java.util.Set<java.lang.String> getCommonConditionFieldSet()`
- `public public java.util.Set<java.lang.String> getFilterSchemaFieldSet()`
- `public public java.lang.String getEntityName()`
- `public public java.lang.String getAppId()`
- `public public void filterContainerInit(kd.bos.form.events.FilterContainerInitArgs)` ⭐ lifecycle
- `public public kd.bos.permission.api.HasPermOrgResult getPermOrgResult()`
- `public public void filterColumnSetFilter(kd.bos.form.events.SetFilterEvent)`
- `public public void filterContainerBeforeF7Select(kd.bos.form.field.events.BeforeFilterF7SelectEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PermHelper L29
```java
  27   
  28       public static HasPermOrgResult getHRPermOrg(String entityNumber) {
  29 >         return PermHelper.getHRPermOrg(RequestContext.get().getCurrUserId(), HOMS_APP, entityNumber, "47150e89000000ac");
  30       }
  31   
```

**QUERY_BUILDER** · PermHelper L42
```java
  40       public static QFilter getBaseDataFilter(String entityName, List<Long> orgIdList) {
  41           if (CollectionUtils.isEmpty(orgIdList)) {
  42 >             return new QFilter("1", "!=", (Object)1);
  43           }
  44           QFilter qFilter = PositionHisEnum.ORG.getEntityName().equals(entityName) || "hbpm_positionhr".equals(entityName) ? new QFilter("org", "in", orgIdList) : (PositionHisEnum.MAIN_ORG.getEntityName().equals(entityName) ? new QFilter("id", "in", orgIdList) : (orgIdList.size() == 1 ? BaseDataServiceHelper.getBaseDataFilter((String)entityName, (Long)orgIdList.get(0)) : BaseDataServiceHelper.getBaseDataFilter((String)entityName, orgIdList, (boolean)true)));
```

**READ_VIA_HELPER** · PermHelper L37
```java
  35   
  36       public static HasPermOrgResult getHRPermOrg(long userId, String appId, String entityNumber, String permId) {
  37 >         return PermissionServiceHelper.getAllPermOrgs((long)userId, (String)HR_ORG_VIEW_TYPE, (String)appId, (String)entityNumber, (String)permId, (boolean)false);
  38       }
  39   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

## ISV 扩展指引（基于 OrgDisableAndIncludeFilterListPlugin 真实证）

> FQN: `kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin/`

### 类型与继承
- 插件类型：**FORM_PLUGIN**
- 父类: `kd.hr.hbp.formplugin.web.HRDataBaseList`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `setFilter`

### 可重写方法（target.java self）
- `public public void setFilter(kd.bos.form.events.SetFilterEvent)` ⭐ lifecycle
- `protected protected java.lang.String getDisableIdentifier()`
- `protected protected java.lang.String getListAdminOrgEnableField()`
- `protected protected java.lang.String getListPermProKey()`

### SDK 范式（ISV 抄作业）

**QUERY_BUILDER** · OrgDisableAndIncludeFilterListPlugin L32
```java
  30               enableStatusList.add("0");
  31           }
  32 >         setFilterEvent.getQFilters().add(new QFilter(this.getListAdminOrgEnableField(), "in", enableStatusList));
  33       }
  34   
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp -->

## ISV 扩展指引（基于 PositionHrSaveOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hrmp.hbpm.opplugin.web.position.PositionHrCommonOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp -->

## ISV 扩展指引（基于 PositionHrDisableOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp -->

## ISV 扩展指引（基于 PositionHrEnableOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.bos.entity.plugin.AbstractOperationServicePlugIn`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onPreparePropertys`, `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`, `afterExecuteOperationTransaction`

### 可重写方法（target.java self）
- `public public void onPreparePropertys(kd.bos.entity.plugin.PreparePropertysEventArgs)` ⭐ lifecycle
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle
- `public public void afterExecuteOperationTransaction(kd.bos.entity.plugin.args.AfterOperationArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRObjectUtils L296
```java
 294               String adminDivisionStr = "";
 295               long adminDivisionId = Long.parseLong(resultStr);
 296 >             QFilter filter = new QFilter("id", "=", (Object)adminDivisionId);
 297               DynamicObject admindivisionDyn = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])filter.toArray());
 298               String admindivisionFullName = admindivisionDyn != null ? admindivisionDyn.getString("fullname") : resultStr;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp -->

## ISV 扩展指引（基于 PositionHrChangeOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hrmp.hbpm.opplugin.web.position.PositionHrCommonOp`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp -->

## ISV 扩展指引（基于 PositionHrRelationChangeOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

## ISV 扩展指引（基于 PositionHisSaveOp 真实证）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp/`

### 类型与继承
- 插件类型：**OP_PLUGIN**
- 父类: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
- ISV 可继承: ✅ 推荐继承本父类
- 推荐挂载方法: `onAddValidators`, `beforeExecuteOperationTransaction`, `beginOperationTransaction`

### 可重写方法（target.java self）
- `public public void onAddValidators(kd.bos.entity.plugin.AddValidatorsEventArgs)` ⭐ lifecycle
- `public public void beforeExecuteOperationTransaction(kd.bos.entity.plugin.args.BeforeOperationArgs)` ⭐ lifecycle
- `public public void beginOperationTransaction(kd.bos.entity.plugin.args.BeginOperationTransactionArgs)` ⭐ lifecycle

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · HRDateTimeUtils L1662
```java
1660       public static SimpleDateFormat getUserSettingFormat() {
1661           IInteService service = (IInteService)ServiceFactory.getService(IInteService.class);
1662 >         String patternStr = service.getDateFormat(Long.valueOf(RequestContext.get().getCurrUserId()));
1663           if (HRStringUtils.isEmpty((String)patternStr)) {
1664               patternStr = YYYY_MM_DD;
```

**QUERY_BUILDER** · HRDynamicObjectUtils L337
```java
 335               String adminDivision = "";
 336               long adminDivisionId = Long.parseLong(result);
 337 >             QFilter idFilter = new QFilter("id", "=", (Object)adminDivisionId);
 338               DynamicObject admindivisionDynamic = QueryServiceHelper.queryOne((String)"bd_admindivision", (String)"fullname", (QFilter[])idFilter.toArray());
 339               String admindivisionFullName = admindivisionDynamic != null ? admindivisionDynamic.getString("fullname") : result;
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

详细参考：[`_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->

## ISV 扩展指引（基于 PositionBaseDataF7FastFilter 真实证）

> FQN: `kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter`
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter/`

### 类型与继承
- 插件类型：**OTHER**
- 父类: `kd.bos.base.AbstractBasedataController`
- ISV 可继承: ⚠️ 标品 @SdkInternal·改继承基类
- 推荐挂载方法: (无)

### 可重写方法（target.java self）
- `public public void buildBaseDataCoreFilter(kd.bos.form.field.events.BaseDataCustomControllerEvent)`

### SDK 范式（ISV 抄作业）

**CONTEXT_ACCESS** · PositionDimFilterService L145
```java
 143               String propKey = (String)customParam.get("hr_dataperm_dimmap_key");
 144               String appId = (String)customParam.get("hr_dataperm_app_id");
 145 >             authorizedOrgTeamResult = (AuthorizedOrgTeamResult)HRMServiceHelper.invokeBizService((String)"hrmp", (String)"hrcs", (String)"IHRCSBizDataPermissionService", (String)"getAuthorizedOrgTeamsF7", (Object[])new Object[]{RequestContext.getOrCreate().getCurrUserId(), appId, entityNum, "47150e89000000ac", propKey});
 146               if (authorizedOrgTeamResult == null) {
 147                   authorizedOrgTeamResult = new AuthorizedOrgTeamResult(false);
```

**QUERY_BUILDER** · PositionDimFilterService L50
```java
  48           ArrayList<QFilter> newQFilter = new ArrayList<QFilter>(10);
  49           for (QFilter qFilter : filterList) {
  50 >             QFilter allDataQFilter0 = new QFilter("1", "=", (Object)1);
  51               QFilter qFilterCopy = qFilter.copy();
  52               allDataQFilter0.and(qFilterCopy);
```

详细参考：[`_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter/extension_guide.md`](../../_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter/extension_guide.md)

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · extension_points · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->
