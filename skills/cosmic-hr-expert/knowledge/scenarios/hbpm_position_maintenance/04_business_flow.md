# 业务流转 + 插入点地图 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `_auto_operations.md` 57 opKey + `_auto_plugin_registry.md` 37 插件 + 7 类 position 反编译 (2026-04-24)
> **confidence**: verified（每个节点的插件类名都来自实抓）

---

## 一、生命周期全景：基础资料 + 时序版本 + 岗位树三轨

苍穹岗位不是"单次创建 + 编辑"的简单模型，而是**基础资料保存流 + 时序版本化 + 岗位树 / 协作关系**三轨：

```
        岗位生命周期三轨
        ┌───────────────────────────────────┐
        │ 轨道 1 · 基础资料保存流             │
        │ new ─ save ─ enable ─ disable      │
        │ (无传统 submit/audit/unaudit · 岗位 │
        │  走即时生效 · 无审批态)              │
        └───────────────────────────────────┘
                      │
                      ▼
        ┌───────────────────────────────────┐
        │ 轨道 2 · 时序版本化                 │
        │ change ─ newhisversion ─ revise   │
        │       ─ confirmchange              │
        │       ─ hiscopy                    │
        │       ─ his_save (历史迁移)         │
        └───────────────────────────────────┘
                      │
                      ▼
        ┌───────────────────────────────────┐
        │ 轨道 3 · 岗位树 / 协作关系          │
        │ parent 字段 → 上级岗位树            │
        │ entryentity → 协作关系分录          │
        │ reportchange ─ dochangerelation    │
        │       ─ do_close                   │
        │ newentry / deleteentry             │
        └───────────────────────────────────┘
```

**状态机字段**：
- `status`（BillStatusField · `scene_doc.json` L102）：数据状态
- `enable`（BillStatusField · L146）：业务状态（启用 / 禁用）
- `iscurrentversion`：当前生效版本标记（由平台 `HisModelOPCommonPlugin` 自动维护）

**注意**：岗位**没有传统基础资料的 submit/audit/unaudit** opKey · 保存即落库 · 启用/禁用直接改 `enable` 字段。

---

## 二、子场景 1：列表展示（进入主流程入口）

### 流程

```
用户访问 hbpm_positionhr 菜单
    ↓
[列表加载] HRBaseDataTplList.beforeBindData
    ↓
[列表过滤] PositionList.setFilter
    (super.setFilter 调父类 + 追加 filter)
    ↓
    判断 entityId:
    ├─ hbpm_posorgtreelistf7 + viewstdpos=1
    │    → 追加 isstandardpos=1 (标准岗位 F7)
    └─ 其他
         → 追加 isstandardpos=0 (业务岗位列表)
    ↓
    追加 isdeleted != 1 (软删除过滤)
    排序: adminorg.id asc, positiontype.id asc, isleader desc
    ↓
[historicalList 特殊] PositionList.filterContainerInit
    hisPage=VERSION_LIST_PAGE 时移除 datastatus 过滤列
    ↓
[操作前] PositionList.beforeDoOperation
    hisversion_view 操作 + Mutex 互斥锁 + 打开 hbpm_positionrevise 页面
    ↓
[详情渲染] PositionEdit
    (HRDataBaseEdit 子类)
```

### 节点详情

| 节点 | 插件类名 | 触发方法 | 可扩展 |
|---|---|---|---|
| 列表绑数据 | `HRBaseDataTplList` | `beforeBindData` | ⚠️ 父类模板，避免覆盖 |
| 列表过滤 | `PositionList` | `setFilter(SetFilterEvent)` | ✅ 追加 filter |
| 筛选初始化 | `PositionList` | `filterContainerInit` | ✅ |
| 操作前处理 | `PositionList` | `beforeDoOperation` | ✅ |
| 历史视图 | `HisModelListCommonPlugin` | `beforeBindData / beforeDoOperation` | ⚠️ 父类模板 |

**证据**：`PositionList.java` L42-L108 + `_auto_plugin_registry.md` L28

---

## 三、子场景 2：新增 / 修改岗位（CRUD）

### 流程

```
点击"新增" (opKey: new) → 打开 PositionEdit 向导
    ↓
[前端渲染] PositionEdit.preOpenForm
    设置 hbss_entitytype_id = "1021"
    ↓
[事件注册] PositionEdit.registerListener
    - reportType F7 单选
    - parent / targetpos / city 挂 BeforeF7SelectListener
    - JobLevelGradeRangeUtil.registerListener (职级职等范围联动)
    - positiontpl / adminorg 挂 F7
    ↓
[绑数据] PositionEdit.afterBindData
    - 从 CustomParam 带入 adminorg / positiontpl
    - JobLevelGradeRangeUtil.afterBindData
    - 新增默认 changetype = 1010
    - 编辑时 bsed = 当前日期 · changetype = 1020
    - 按 admin Org 判断 positiontpl 可见性
    - 按操作状态（ADDNEW / VIEW / EDIT）切换控件显隐
    ↓
[字段联动] PositionEdit.propertyChanged
    adminorg → 带出 countryregion / city / workplace / org · 置空 positiontpl
    city → 若 countryregion 空 · 自动填 · 并按 (country,city) 查默认 workplace
    countryregion → city 置 null
    parent → 重算 parent 显示名 (adminorg/name 拼接)
    bsed → 若 establishmentdate 空 · 自动同值
    positiontpl → changePositionTpl 批量回填 8 字段
    number-触发字段 → changeNumber 重新生成编码 (recycle + set)
    ↓
填写字段：必填仅 adminorg，推荐填 name / positiontype / job / 职级职等方案
    ↓
点击"保存" (opKey: save) → PositionEdit.beforeDoOperation
    设置 initdatasource = "0"
    option 变量: isFromPage=1 / appId=当前 / bsed=yyyy-MM-dd
    ↓
【服务端】beforeExecuteOperationTransaction: 9 插件链 (_auto_operations.md L94-L107)
    ├── (1) CodeRuleOp                编码规则 (PR-006 · 业务侧配置)
    ├── (1) BdVersionSaveServicePlugin 基础资料版本化
    ├── (3) HRBaseDataStatusOp         HR 状态维护
    ├── (4) HRBaseDataLogOp            HR 日志 (默认 disabled)
    ├── (5) HRBaseDataEnableOp         启用态维护
    ├── (6) HisModelOPCommonPlugin     时序通用 (写 boid/iscurrentversion/hisversion)
    ├── (7) HRBaseOriginalOp           原始值记录 (首次)
    ├── (7) HisUniqueValidateOp        时序唯一性
    └── (9) PositionHrSaveOp           ⭐ 岗位保存
        │
        ├── onAddValidators (_auto_plugin_registry)
        │     if (isImport()) args.addValidator(new PositionImptRuleNumberValidator())
        │
        ├── beforeExecuteOperationTransaction
        │     unitPositionMap JSON 处理 (导入场景 · 复用已有 id)
        │     entity.set("org", entity.get("adminorg.org"))
        │     批量替换 parent 引用 (导入场景新旧映射)
        │
        ├── beginOperationTransaction
        │     super.beginOperationTransaction 走 PositionHrCommonOp
        │     IPositionRelationServiceApplication.saveSysRelation(positions)    → 系统汇报关系
        │     IPositionRelationServiceApplication.saveCooperationRelation(positions) → 协作关系
        │     afterVersions = PositionQueryRepository.queryPositionsById(sourcevids)
        │     查三分类 (changetype=1010 · changeoperate=1010 · changescene=1010)
        │     afterVersion.set("changetype/changeoperate/changescene") 写新版本
        │     IPositionHrServiceApplication.afterSavePosition(afterVersions)
        │
        └── afterExecuteOperationTransaction (继承 PositionHrCommonOp)
              if (!isImport()) ChangeMsgServiceImpl.sendMsg()  → 发调度任务
              IBosPositionService.addOrUpdatePositions(boids)  → 更新下游缓存
    ↓
onAddValidators: 1 条校验 (_auto_operations.md L112)
    MustInput 6096194600001fac (enabled)  字段合规性
    ↓
数据入库 t_hbpm_position + t_hbpm_standposentry
    ↓
[前端] PositionEdit.afterDoOperation
    if (bsed > now) 显示 hbpm_position_future 未来视图
    else 刷新当前页
    recycleNumber 如 save 失败回收编码
```

### 节点详情

| 节点 | 可用扩展点 | 标品插件（顺序） | 风险 |
|---|---|---|---|
| 字段联动 | `onFieldChange@hbpm_positionhr` | `PositionEdit.propertyChanged` 6 分支 | 低 |
| **保存校验** | ⭐ `beforeExecuteOperationTransaction@save` | 9 插件完整链 | **中** |
| Validator 挂载 | `onAddValidators@save` | 并列挂 · 不覆盖 PositionHrSaveOp | ⭐ 最常扩展点 |
| 入库 | 事务内 | - | - |
| 消息发送 | 事务后 | `ChangeMsgServiceImpl.sendMsg` + `IBosPositionService.addOrUpdatePositions` | 低 |

### 关键：PositionHrSaveOp 的 4 阶段行为

来自 `PositionHrSaveOp.java`：
- `onAddValidators` L43-L49：导入时注册 `PositionImptRuleNumberValidator`
- `beforeExecuteOperationTransaction` L51-L73：写 `org = adminorg.org` + 导入 map 关联
- `beginOperationTransaction` L75-L101：保存系统汇报关系 + 协作关系 + 写三分类 + 触发 `afterSavePosition`
- `afterExecuteOperationTransaction` (继承 L28-L35)：非导入时发消息 + 同步下游

---

## 四、子场景 3：启用 / 禁用

### 流程

```
启用 (opKey: enable · _auto_operations.md L131-L141)
    ↓
beforeExecuteOperationTransaction
    ├── (-) HisModelOPCommonPlugin
    └── (1) PositionHrEnableOp
          ├── onPreparePropertys 声明全字段（去除 _id 后缀和 multilanguagetext）
          ├── onAddValidators (空)
          ├── beforeExecuteOperationTransaction
          │     查 boid → latestHisId 映射
          ├── beginOperationTransaction
          │     写三分类 1070/1070/1070 到 afterVersion
          │     sourcevid 指向上一版
          │     IPositionHrServiceApplication.afterSavePosition
          └── afterExecuteOperationTransaction
                ChangeMsgServiceImpl.sendMsg()
                IBosPositionService.commonSyncPositions()  → 同步下游
    ↓
    parameter: Value = "1" · StatusFieldId = ac5Y5Dax1q
    (即 enable 字段改为 "1")
```

### PositionHrEnableOp 的 5 阶段行为（`PositionHrEnableOp.java` L45-L94）

| 方法 | 作用 |
|---|---|
| `onPreparePropertys` | 声明所有字段（非多语言 + 非 `_id` 后缀）· 准备全字段对比 |
| `onAddValidators` | 空（可扩展加启用前业务校验） |
| `beforeExecuteOperationTransaction` | 查 `boid → latest id` 映射表 |
| `beginOperationTransaction` | 写三分类 + sourcevid |
| `afterExecuteOperationTransaction` | 发消息 + 同步下游 |

### 禁用流程

```
禁用 (opKey: disable · _auto_operations.md L122-L128)
    ↓
beforeExecuteOperationTransaction
    ├── (-) HisModelOPCommonPlugin
    └── (1) PositionHrDisableOp
          ├── onPreparePropertys
          │     声明 4 字段: adminorg / changeoperate / changescene / changetype
          ├── onAddValidators (空)
          ├── beforeExecuteOperationTransaction
          │     查 boid → latestHisId 映射
          ├── beginOperationTransaction
          │     写三分类 1040/1030/1040
          │     sourcevid 指向上一版
          │     IPositionHrServiceApplication.afterSavePosition
          └── afterExecuteOperationTransaction
                ChangeMsgServiceImpl.sendMsg()
                ⭐ IBosPositionService.disablePositions(positionIds)
                  → 委托底层业务校验（查下游引用）
    ↓
    parameter: Value = "0" (enable=0)
```

**⚠️ 重要认知**：`PositionHrDisableOp.onAddValidators` 是**空**（标品无业务引用校验）· 业务阻断在 `IBosPositionService.disablePositions` 内部 · 如需**前置强阻断**（早于事务开始）· 走 CS-04 新挂 Validator。

---

## 五、子场景 4：时态版本变更

### 5 种版本操作

| opKey | 作用 | 触发行为 |
|---|---|---|
| `revise` | 修订 | 不产新版本，直接改（INV-07） |
| `newhisversion` | 新增数据版本 | 产生新 `bsed` 内存对象（donothing · 用户再填） |
| `change` | 变更 | 走变更流程（donothing） |
| `confirmchange` | 确认变更 | ⭐ 3 插件链 + MustInput |
| `hiscopy` | 复制一个版本 | - |
| `his_save` | 历史保存 | ⭐ 挂 `PositionHisSaveOp` + 3 Validator |

### confirmchange 流程（`_auto_operations.md` L375-L387）

```
用户"确认变更"
    ↓
3 插件执行链
    ├── (-) HisModelOPCommonPlugin  时序通用
    ├── (1) HisUniqueValidateOp     历史唯一性
    └── (2) PositionHrChangeOp      ⭐ 岗位变更
          ├── onAddValidators L36-L38: super.onAddValidators (PositionHrCommonOp)
          ├── beforeExecuteOperationTransaction L40-L43
          │     查 boid → latestHisId 映射
          └── beginOperationTransaction L45-L63
                filter(!iscurrentversion) 只处理新版本
                查三分类 (1020/1020/1020)
                afterVersion.set changetype/changeoperate/changescene
                IPositionRelationServiceApplication.saveSysRelation
                if (importtype) changeCooperationRelation
                IPositionHrServiceApplication.afterSavePosition
    ↓
1 校验
    MustInput 4UXPTMTC=NCR (enabled)
    ↓
数据入库新版本（只标记新版本为变更态 · 当前版本不改）
```

### his_save 历史保存流程（`_auto_operations.md` L463-L473）

```
his_save (历史迁移 · 5/2GRHBL33SR)
    ↓
1 插件执行链
    └── PositionHisSaveOp
          ├── onAddValidators 注册 3 Validator:
          │     - JobLevelGradeRangeImportValidator(true)  职级/职等范围
          │     - PositionHisLoopValidator().checkHis()    成环校验
          │     - PositionHisValidator                     时序数据完整性
          ├── beforeExecuteOperationTransaction
          │     排序 positionHisArray (按 boid, bsed)
          │     setDefaultValue: 自动填 org / establishmentdate
          │     calcBsledByNextBsed: 按下一版 bsed - 1 天 自动算 bsled
          └── beginOperationTransaction
                准备 HisCreateVersionParam (entityNumber=hbpm_positionhr)
                HisModelServiceHelper.createDataVersions  → 创建历史版本
                过滤 !iscurrentversion → IPositionRelationServiceApplication.saveSysRelation
```

### 关键：PositionHisValidator 的 9 条校验（`PositionHisValidator.java` L53-L86）

| 方法 | 校验内容 |
|---|---|
| `validateBoId` | boid 是否为空 / 0（必须先导入当前版本数据） |
| `validateBSEDLessFirstBSED` | 首个版本 bsed 不能早于 firstBSED |
| `validateBSLEDRequired` | 最后一个版本 bsled 必填 |
| `validateAdminOrg` | 历史版本 adminorg 需与当前版本一致 |
| `validateJobScm` | jobscm 规则（委托 `PositionValidatorServiceHelper.validateJobScm`） |
| `validateBSEDLESSBSLED` | bsed ≤ bsled |
| `validateEstablishLessParentFirstBsed` | establishmentdate 不早于 parent 的首生效日 |
| `validateBSedAndAdminOrgFirstBsed` | bsed 不早于 adminorg 的首生效日 |
| `validateBSedLessNow` | bsed 不晚于当前日期 |
| `validateBsedAndBsledRelationship` | 版本间 bsled = 下一版本 bsed - 1 天（严格连续） |

---

## 六、子场景 5：汇报 / 协作关系变更（岗位独有）

### 协作关系维护流程

```
用户点击"协作关系变更" (reportchange opKey · donothing)
    ↓
PositionEdit.afterDoOperation "reportchange" 分支 L426-L444
    setStatus(EDIT)
    setVisible(TRUE, [confirmrelation, CHANGEINFO_FLEXPANELAP, changedescription])
    setVisible(FALSE, [bar_save, bar_print, baseinfo/jobinfo/dutyinfo/qualinfo/attachment])
    bsed = 当前日期 · changetype = 1030
    customHRPermItemId = "2DNXU/27ZSXL"
    ↓
用户维护 entryentity 分录 (newentry / deleteentry)
    reporttype → hbpm_reportcoreltype
    targetpos → hbpm_positionhrf7
    ↓
点击"确认变更" (opKey: dochangerelation · _auto_operations.md L427-L443)
    ↓
1 插件执行链
    └── PositionHrRelationChangeOp
          ├── onAddValidators L26-L27 空
          └── beginOperationTransaction L29-L42
                IPositionRelationServiceApplication.changeCooperationRelation(positions)
                查三分类 (1030/1020/1030)
                for each pos: set changetype/changeoperate/changescene
                IPositionHrServiceApplication.afterSavePosition
    ↓
1 校验
    MustInput 4T7SDY=13RY6 (enabled)
    ↓
PositionEdit.afterDochangerelation L1020-L1026
    setVisible(FALSE, [confirmrelation, CHANGEINFO_FLEXPANELAP])
    setVisible(TRUE, [baseinfo/jobinfo/dutyinfo/qualinfo/attachment/role/anchor])
    invokeOperation("refresh")
```

### do_close（退出变更态）

```
do_close (_auto_operations.md L452-L455)
    ↓
PositionEdit.afterDoOperationForReportChangeConfirm L994-L1004
    setStatus(VIEW)
    setVisible(FALSE, [confirmrelation, CHANGEINFO_FLEXPANELAP, baritemap])
    setVisible(TRUE, [bar_save, bar_print, propertychange, reportchange, ...])
```

### 关联：PositionHisLoopValidator 成环校验

| 方法 | 校验 | 使用场景 |
|---|---|---|
| `checkSysRel` | 行政汇报链（`parent` 字段 · reportingtype=1010）不成环 | his_save 激活（`checkHis()`） |
| `checkTeamUpRel` | 协作关系（`entryentity.reporttype` 多类型）不成环 | his_save 有 entryentity 时激活 |

---

## 七、子场景 6：HIES 导入导出

### 导入流程（`importdata_hr` · `_auto_operations.md` L290-L295）

```
点击"导入数据"（opKey: importdata_hr）
    ↓
parameter.cusstartpage = hismodel_importstart
parameter.plugins = [
  {ClassName: kd.hrmp.hbpm.formplugin.web.impt.PositionHRImportPlugin, Description: 岗位中台导入插件, RowKey: 0},
  {ClassName: kd.hrmp.hbpm.formplugin.web.impt.PositionRuleNumberImportPlugin, Description: 岗位编码规则控制导入插件, RowKey: 1}
]
    ↓
向导选模板 → 上传文件 → 后台异步处理
    ↓
后台走 PositionHrSaveOp (isImport()=true)
    ├── PositionImptRuleNumberValidator 激活 (编码回收)
    ├── PositionHrCommonOp.afterExecuteOperationTransaction 跳过 ChangeMsgServiceImpl.sendMsg (避免批量发消息)
    └── unitPositionMap 处理批量 id 映射
    ↓
用 opKey: show_import_record_hr 查看结果
```

### 4 种导入 opKey 并存

| opKey | 插件 | 用途 |
|---|---|---|
| `importdata` | `PositionRuleNumberImportPlugin` | 普通导入（`importdata` 标准类型） |
| `importdata_hr` | `PositionHRImportPlugin` + `PositionRuleNumberImportPlugin` | HIES 新岗位导入（含历史） |
| `relationimport` | `PositionRelationImportPlugin` | 协作关系导入 |
| `reviseimport` | `PositionReviseImportPlugin` | 修订导入数据 |
| `import_positiondetailrevise` | `PositionReviseHRImportPlugin` | 批量修订属性 |

### 导出 3 种模式（`_auto_operations.md` L303-L322）

| opKey | 模式 | 插件 |
|---|---|---|
| `export_from_list_hr` | 按列表导出（带当前 filter） | 无专属 plugins |
| `export_from_impttpl_hr` | 按导入模板导出 | `PositionHRExportPlugin` |
| `export_from_expttpl_hr` | 按导出模板导出 | `PositionHRExportPlugin` |

### 传统 opKey（`exportlist` / `exportlistbyselectfields` / `exportlist_expt`）

这 3 个是苍穹基础框架的导出，**本场景 HR 推荐走 hr 专用 opKey**（`importdata_hr` / `export_from_*_hr`）。

---

## 八、子场景 7：打印 / 日志

| opKey | 作用 | 备注 |
|---|---|---|
| `selecttplprint` | 打印岗位说明书 | `selecttplprint` 类型 · 权限 `2PYK5I1A00N+` |
| `logview` | 查看全部日志 | `donothing` · 参数 `CanNoData: true` |
| `viewonelog` | 查看单条日志 | `donothing` |
| `namehistory` | 改名（改名历史） | `donothing` |
| `namehistoryview` | 查看改名历史 | `donothing` |

---

## 九、扩展点时序图（综合）

```
用户操作 (new/save/enable/disable/confirmchange/dochangerelation/his_save)
    ↓
┌──────────────────────────────────────────┐
│ 前端 formPlugin 层                         │
│ ├── PositionEdit (HRDataBaseEdit)         │
│ │   ├── preOpenForm                       │
│ │   ├── registerListener                  │
│ │   ├── afterBindData                     │
│ │   ├── propertyChanged (6 分支字段联动)  │
│ │   ├── beforeDoOperation                 │
│ │   ├── afterDoOperation                  │
│ │   ├── closedCallBack (职级/职等范围回调)│
│ │   └── beforeF7Select (5 F7 分支)        │
│ └── 前端校验（浏览器层）                   │
└────────────────┬─────────────────────────┘
                 │ HTTP
                 ▼
┌──────────────────────────────────────────┐
│ 后端 opPlugin 层（按 opKey 分发）          │
│ ├── onPreparePropertys    声明读写字段    │
│ ├── onAddValidators       注册 Validator  │
│ ├── beforeExecuteOperationTransaction     │ ⭐ 最常扩展
│ ├── beginOperationTransaction             │ ⭐ 事务内业务写入
│ ├── endOperationTransaction               │
│ └── afterExecuteOperationTransaction      │ ⭐ 主事务后发消息/同步下游
└────────────────┬─────────────────────────┘
                 ▼
           数据入库（t_hbpm_position + t_hbpm_standposentry）
                 ▼
           ChangeMsgServiceImpl.sendMsg (调度任务)
                 ▼
           调度任务 (sch_task) 读 hbpm_position_msgdetail 批量发消息
                 ▼
           IBosPositionService 同步下游缓存
                 ▼
           下游订阅者 (hrpi_empposorgrel / haos_chargeperson 等)
```

---

## 十、各子场景耗时预估

| 子场景 | opKey | 插件数 | 典型耗时 |
|---|---|---|---|
| 列表展示 | - | ~6 list 插件 | < 200ms |
| 新增 / 修改 + save | save | 9 | 300-500ms |
| 启用 | enable | 2 | 150-300ms |
| 禁用 | disable | 2 (+ IBosPositionService) | 200-400ms |
| 确认变更 | confirmchange | 3 | 200-300ms |
| 汇报关系确认 | dochangerelation | 1 | 150-300ms |
| 历史保存 | his_save | 1 (+ 3 Validator) | 500-1000ms（批量时） |
| 新增数据版本 | newhisversion | 0 业务插件 | < 100ms（仅前端） |
| HIES 导入 | importdata_hr | 2 自定义 | 秒级（按文件大小） |
| 协作关系导入 | relationimport | 1 | 秒级 |

---

**📌 来源追溯**：
- 57 opKey 总览：`_auto_operations.md` L9-L67
- save 9 插件链：`_auto_operations.md` L94-L107
- enable 2 插件：`_auto_operations.md` L131-L141
- disable 2 插件：`_auto_operations.md` L122-L128
- confirmchange 3 插件：`_auto_operations.md` L375-L387
- dochangerelation 1 插件：`_auto_operations.md` L427-L443
- his_save 1 插件：`_auto_operations.md` L463-L473
- PositionHrSaveOp 流：`PositionHrSaveOp.java` L43-L101
- PositionHrEnableOp 流：`PositionHrEnableOp.java` L45-L94
- PositionHrDisableOp 流：`PositionHrDisableOp.java` L38-L79
- PositionHrChangeOp 流：`PositionHrChangeOp.java` L31-L63
- PositionHrRelationChangeOp 流：`PositionHrRelationChangeOp.java` L24-L42
- PositionHisSaveOp 流：`PositionHisSaveOp.java` L56-L114
- PositionHisValidator 9 校验：`PositionHisValidator.java` L53-L186
- PositionEdit 前端流：`PositionEdit.java` L171-L1027
- PositionList 列表流：`PositionList.java` L42-L108
