# 业务规则 + 可变性 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `_auto_operations.md` 57 opKey 校验规则 + `rules_chain_all.json` 反编译业务规则 + `scene_doc.json` 70+ 字段 minefield + 7 类反编译（2026-04-24）
> **可变性分类**: 🔒 硬编码（平台/时序模板级）/ ⚙️ 配置（UI/参数可切）/ 🔌 插件可改 / 📝 元数据可改
> **confidence**: verified

---

## 一、8 条核心不变式（时序模型 + 岗位树双重约束）

这些是**岗位域的系统级约束**，违反会导致时序数据错乱、岗位树混乱或下游引用失效。

### INV-01 · 行政组织 adminorg 必填 🔒

- **规则**：新增 / 修改岗位时 `adminorg` 必须有值（`scene_doc.json` L615 `required: true`）
- **可变性**：`hardcoded`（由元数据 required=true 保证 · save 的 MustInput 校验 `6096194600001fac` 强制执行）
- **业务意义**：岗位是挂组织的 · 无组织归属则下游编制/员工查询都失效
- **实现点**：save validations L112（`_auto_operations.md`）

### INV-02 · 岗位树不成环 🔒

- **规则**：`parent` 指向的上级岗位链不能成环（同一 `reportingtype` 同一 `[bsed,bsled]` 时间段内不成环）
- **可变性**：`hardcoded`（由 `PositionHisLoopValidator.checkSysRel` 保证 · his_save 时显式 `checkHis()` 激活）
- **违反后果**：岗位树遍历死循环 / 组织架构图无法渲染
- **实现点**：`kd.hrmp.hbpm.opplugin.web.position.validate.PositionHisLoopValidator` L91-L119
  - 查 `queryPosHisEnableAdminRelByTimeRange(minEffdt, maxLeffdt, ["1010"])` 获取同期行政汇报链
  - 逐节点 `chekParentLoop(boid, parent, "1010", pos, roleToReportListMap)` 走链检查
  - 特殊拦截：`parent.id == boid`（选自己当父岗位）在 `removeParentNeedentCheck` 阶段直接 fatal
- **前端配合**：`PositionEdit.beforeF7Select` L932-L936 `QFilter("boid","!=",boid)` 禁止 F7 选到自己

### INV-03 · 编码跨历史版本唯一 🔒

- **规则**：同一 `boid` 的多版本共享同一 `number`；不同 `boid` 的 `number` 必须全局唯一
- **可变性**：`hardcoded`
- **实现点**：`PositionImptRuleNumberValidator`（只在 import 时额外校验）+ `HisUniqueValidateOp`（save 链第 8 位）
- **编码回收**：`PositionCodeRuleHelper.recycleNumber` 在前端字段变更 / 保存失败时回收占用号（`PositionEdit.recycleNumber` L637-L649）

### INV-04 · 时序字段禁止手改 🔒

- **规则**：`boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `sourcevid` 由平台自动维护
- **可变性**：`hardcoded`
- **违反后果**：时序表各版本间关联断裂，`iscurrentversion` 判断失效
- **实现点**：时序模板 `hbp_histimeseqtpl` 级约束；`HisUniqueValidateOp` 校验（`_auto_operations.md` save 链 L105-L106）
- **遵循规则**：PR-008（时序资料当前版本查询用 `iscurrentversion=true`） + PR-009（下游引用用 boid · 不用 id）
- **`scene_doc.json` 证据**：L330-L454 6 个字段 `minefield: red`，`autoComputed: true`

### INV-05 · 变动必须双分类 🔒

- **规则**：任何保存 / 启停用 / 变更操作 · `changetype` + `changeoperate` + `changescene` 三分类必须有值（由 Op 自动填）
- **可变性**：`hardcoded`（由各 `PositionHr*Op.beginOperationTransaction` 统一写入）
- **预置编码**（`PositionChangeEventQueryRepository.queryChangeType/Operation/Scene` 传的 id）：
  - `save` (新建)：changetype=1010 / changeoperate=1010 / changescene=1010（`PositionHrSaveOp.beginOperationTransaction` L88-L90）
  - `confirmchange` (变更)：changetype=1020 / changeoperate=1020 / changescene=1020（`PositionHrChangeOp` L49-L51）
  - `dochangerelation` (汇报关系变更)：changetype=1030 / changeoperate=1020 / changescene=1030（`PositionHrRelationChangeOp` L33-L35）
  - `disable` (禁用)：changetype=1040 / changeoperate=1030 / changescene=1040（`PositionHrDisableOp` L62-L64）
  - `enable` (启用)：changetype=1070 / changeoperate=1070 / changescene=1070（`PositionHrEnableOp` L78-L80）
- **业务意义**：变更溯源需要双分类维度 · 下游消费 `hbpm_position_msgdetail` 时按 `changescene` 分发

### INV-06 · 编码创建后不可修改 🔒（对业务岗位 · PR-007）

- **规则**：`number` 一经保存，后续 modify 不允许改（仅针对 `issyspreset=false` 的业务岗位）
- **可变性**：`hardcoded`（对预置岗位）· 业务岗位技术上 `isvCanModify=true` 但业务不应改
- **违反后果**：下游 27 处引用断裂（hrpi_empposorgrel.position / hrpi_empjobrel.position 等）
- **遵循规则**：PR-007 · 系统预置不可改 · 业务数据可改
- **扩展口**：如需业务侧支持改编码 · 可走 ISV 扩展加"老编码映射表"

### INV-07 · 修订不产生新版本 🔒

- **规则**：`revise` 操作修改当前版本记录 · 不创建新 `bsed` 版本行
- **可变性**：`hardcoded`（由 `revise` opKey 的 `donothing` 类型决定 · `_auto_operations.md` L343-L347）
- **对比**：`change` / `newhisversion` 产生新版本；`revise` 就地改
- **业务意义**：revise 用于"改错别字 / 补资料" · 不触发历史链

### INV-08 · 岗位模板字段回填不可人为撤销 🔒

- **规则**：选中 `positiontpl` 后 · 模板 `fieldrange` 外的字段被 `setTplFieldEnable` 置灰（无法改）
- **可变性**：`hardcoded`（由模板 `ablemodifyfield` 控制）
- **实现点**：`PositionEdit.setTplFieldEnable` L827-L852
- **业务逻辑**：
  - 模板 `ablemodifyfield=false` → 所有 `POSITIONTPL_FILED_KEYS + 模板 fields` 全部置灰
  - 模板 `ablemodifyfield=true` + `fieldrange` → 白名单可改，其余置灰

---

## 二、列表展示规则

### R-L01 · 列表默认过滤软删除 🔌

- **可变性**：`pluggable`
- **实现**：`PositionList.setFilter` L60（`setFilterEvent.getQFilters().add(new QFilter("isdeleted", "!=", "1"))`）
- **扩展方式**：继承 `PositionList` + super.setFilter + 追加或调整 filter

### R-L02 · 列表按标准岗位 / 业务岗位分视图 🔌

- **可变性**：`pluggable`
- **实现**：`PositionList.setFilter` L54-L63
  - F7 视图 `hbpm_posorgtreelistf7` + 页面缓存 `viewstdpos=1` → `isstandardpos=1`（标准岗位）
  - 其他情况 → `isstandardpos=0`（业务岗位）
- **默认排序**：`adminorg.id asc, positiontype.id asc, isleader desc`（L61）

### R-L03 · 历史版本列表隐藏 datastatus 过滤列 🔌

- **可变性**：`pluggable`
- **实现**：`PositionList.filterContainerInit` L44-L50 检查 `hisPage=VERSION_LIST_PAGE` 时移除 `datastatus` 筛选项
- **原因**：历史视图不需要数据状态过滤

### R-L04 · 行政组织数据权限过滤 ⚙️🔌

- **可变性**：`config`（`HasPermOrgResult.hasAllOrgPerm` 自动判断）+ `pluggable`
- **实现**：`PositionEdit.beforeF7Select` adminorg 分支 L926-L931 + `getPermOrgResult()` L951-L968
- **缓存**：页面缓存 `org_perm_result` + 内存 `hasPermOrgResult`

---

## 三、新增 / 修改岗位规则

### R-C01 · 行政组织必填 🔒

- 见 INV-01

### R-C02 · 编码跨历史版本唯一 🔒

- 见 INV-03

### R-C03 · 岗位模板字段联动 🔌

- **可变性**：`pluggable`（可改触发字段集）
- **触发字段**：`POSITIONTPL_FILED_KEYS = [job, jobscm, lowjoblevel, highjoblevel, lowjobgrade, highjobgrade, jobgradescm, joblevelscm]`（`PositionEdit` L183）
- **排除字段**：`EXINCLUDE_POSITIONTPL_FILED_KEYS = [joblevelrange, jobgraderange]`（L184）
- **实现**：`PositionEdit.changePositionTpl` L517-L549
- **扩展方式**：新 Edit 并列挂 · 重写 `propertyChanged@positiontpl`

### R-C04 · 行政组织变更联动地址信息 🔌

- **可变性**：`pluggable`
- **实现**：`PositionEdit.propertyChanged` adminorg 分支 L462-L481
- **联动字段**：`countryregion` / `city` / `workplace` / `org` 从 adminorg 带出
- **同时重置**：`positiontpl` 置 null（新组织可能不挂此模板）

### R-C05 · 编码规则联动字段 🔌

- **可变性**：`pluggable`
- **触发字段**（`PositionEdit.getNumberRuleAllFieldSet` L783-L796）：`org` / `adminorg` / `parent` / `job` / `positiontype` + 扩展字段（页面缓存 `numberrule_extfield`）
- **实现**：`PositionEdit.propertyChanged` L512-L514 检测触发字段变化 → `changeNumber` → `recycleNumber` + `setPositionNumber`
- **扩展方式**：通过缓存注入额外触发字段 · 无需改代码

### R-C06 · 岗位必关联职位参数 ⚙️

- **可变性**：`config`（通过 `SystemParamHelper.getPosMustRelateJobParameter(orgId)` 切换）
- **校验规则**：参数开启 + `job=null` 时，`JobLevelGradeRangeImportValidator` 抛出"请填写职位"错误（`JobLevelGradeRangeCheck_0`）
- **路径**：组织发展云 → 组织管理 → 参数配置

### R-C07 · 职级职等方案校验 🔌

- **可变性**：`pluggable`
- **实现**：`JobLevelGradeRangeImportValidator.checkJobRelatedInformation` L58-L77
- **校验点**：
  - 职级/职等方案需与 job 关联的方案一致（`JobLevelGradeRangeCheck_4` / `_6`）
  - 最低/最高职级职等需在方案范围内（`JobLevelGradeRangeCheck_9` ~ `_14`）
  - 最低顺序码需 ≤ 最高顺序码（`JobLevelGradeRangeCheck_11` / `_15`）
  - 职位体系方案需在 job 关联的 jobscm 范围内（`JobLevelGradeRangeCheck_2`）

### R-C08 · 标准岗位分录联动 📝

- **可变性**：`metadata-bound`（分录字段约束 `t_hbpm_standposentry.applicableorg` required=true）
- **触发**：`isstandardpos=1` 时 `applicableorgentity` 分录至少 1 行
- **实现**：标准岗位适用组织（含 `iscontainsu` 是否包含下级）

---

## 四、保存链核心规则（save opKey · 9 插件）

### R-S01 · 保存合法性校验 ⚙️

- **可变性**：`config`（`MustInput 6096194600001fac` 默认 enabled · 可禁用）
- **证据**：`_auto_operations.md` L112

### R-S02 · CodeRuleOp 自动生成编码 🔌

- **可变性**：`config`（规则由编码规则基础资料配置）+ `pluggable`
- **证据**：save 链第 1 位 `kd.bos.business.plugin.CodeRuleOp`（`_auto_operations.md` L99）
- **遵循规则**：PR-006（CodeRuleOp 是平台模板绑定插件 · 业务侧配置即可）

### R-S03 · PositionHrSaveOp 保存链第 9 位 ⭐🔌

- **可变性**：`pluggable`（并列挂 · 不继承 · PR-001）
- **核心行为**（`PositionHrSaveOp` 反编译）：
  - `onAddValidators` L43-L49：`isImport()` 时额外注册 `PositionImptRuleNumberValidator`（编码回收）
  - `beforeExecuteOperationTransaction` L51-L73：
    - 读 `unitPositionMap` 导入变量 · 用于批量覆盖已有岗位 id
    - 自动写 `org = adminorg.org`（保持组织体系一致）
    - 处理 parent 的批量关联（根据导入 map 重新绑 parent 关系）
  - `beginOperationTransaction` L75-L101：
    - 调 `IPositionRelationServiceApplication.saveSysRelation` 保存系统汇报关系
    - 调 `IPositionRelationServiceApplication.saveCooperationRelation` 保存协作关系
    - 查 afterVersions 并写三分类 (changetype=1010 / changeoperate=1010 / changescene=1010)
    - 调 `IPositionHrServiceApplication.afterSavePosition` 触发保存后处理
  - `afterExecuteOperationTransaction` 继承 `PositionHrCommonOp`：
    - 非 import 时 · 调 `ChangeMsgServiceImpl.sendMsg()` 触发调度任务发变更消息
    - 调 `IBosPositionService.addOrUpdatePositions(boids)` 更新下游岗位缓存

### R-S04 · 导入场景独有校验 🔌

- **可变性**：`pluggable`
- **触发条件**：`getOption().containsVariable("importtype")`（`PositionHrCommonOp.isImport()` L37-L39）
- **效果**：
  - 跳过 `ChangeMsgServiceImpl.sendMsg()`（避免导入批量发消息）
  - 激活 `PositionImptRuleNumberValidator`（编码回收 · 只在导入失败时释放号）

---

## 五、变更规则（confirmchange / change opKey）

### R-V01 · confirmchange 复用 3 插件链 🔌

- **可变性**：`pluggable`
- **3 插件执行链**（`_auto_operations.md` L375-L382）：
  - `HisModelOPCommonPlugin` — 时序通用
  - `HisUniqueValidateOp` — 时序唯一性
  - `PositionHrChangeOp` ⭐ — 岗位变更专属
- **1 校验**：`MustInput 4UXPTMTC=NCR`（`_auto_operations.md` L386）

### R-V02 · PositionHrChangeOp 仅处理非当前版本 🔌

- **可变性**：`pluggable`
- **实现**：`PositionHrChangeOp.beginOperationTransaction` L47
  - `Arrays.stream(positions).filter(data -> !data.getBoolean("iscurrentversion"))` · 只给待生效的新版本写变更标记
  - 写三分类 1020/1020/1020
  - import 时额外调 `changeCooperationRelation` 重建协作关系
- **业务意义**：当前版本不改 · 只标记新版本为"变更态" · 待 bsed 生效后切换

### R-V03 · 新增数据版本 newhisversion 🔌

- **可变性**：`pluggable`（无标品业务插件 · donothing）
- **证据**：`_auto_operations.md` L336-L341 无 plugins / validations
- **机制**：前端点击 → 进入 `hispage` 模式 · 带入当前版本字段 · 用户填新 `bsed` · 后续走 save / confirmchange 入库

---

## 六、启停用规则

### R-E01 · PositionHrEnableOp 启用链 🔌

- **可变性**：`pluggable`
- **插件链**（`_auto_operations.md` L131-L141）：
  - `HisModelOPCommonPlugin` + `PositionHrEnableOp`
- **核心行为**（`PositionHrEnableOp` 反编译）：
  - `onPreparePropertys` L49-L63：**声明所有非多语言字段 + 非 `_id` 后缀字段** · 便于全字段预处理
  - `beforeExecuteOperationTransaction` L68-L71：查 `boid → latestHisId` 映射
  - `beginOperationTransaction` L73-L88：写 `sourcevid` 指向上一版 + 写三分类 1070/1070/1070
  - `afterExecuteOperationTransaction` L90-L94：`ChangeMsgServiceImpl.sendMsg()` + `IBosPositionService.commonSyncPositions()` 同步下游
- **onAddValidators 空**：标品启用无业务校验（可扩展加"上级岗位必须启用"等）

### R-E02 · PositionHrDisableOp 禁用链 🔌

- **可变性**：`pluggable`
- **插件链**（`_auto_operations.md` L122-L128）：
  - `HisModelOPCommonPlugin` + `PositionHrDisableOp`
- **核心行为**（`PositionHrDisableOp` 反编译）：
  - `onPreparePropertys` L42-L47：只声明 4 字段（adminorg / changeoperate / changescene / changetype）
  - `beforeExecuteOperationTransaction` L52-L55：查 `boid → latestHisId` 映射
  - `beginOperationTransaction` L57-L72：写 `sourcevid` + 三分类 1040/1030/1040
  - `afterExecuteOperationTransaction` L74-L79：
    - `ChangeMsgServiceImpl.sendMsg()` 发变更消息
    - ⭐ `IBosPositionService.getInstance().disablePositions(positionIds)` **委托底层业务校验**（下游引用检查在这里发生）
- **扩展口**：如需前置强阻断（不等 disable 内部校验）· 参考 CS-04 新挂 Validator

---

## 七、汇报关系规则（dochangerelation opKey · 岗位独有）

### R-R01 · 1 插件链 + 1 校验 🔌

- **可变性**：`pluggable`
- **证据**（`_auto_operations.md` L427-L443）：
  - 插件：`PositionHrRelationChangeOp`（RowKey=-）
  - validation：`MustInput 4T7SDY=13RY6` enabled

### R-R02 · PositionHrRelationChangeOp 核心行为 🔌

- **实现**：`PositionHrRelationChangeOp.beginOperationTransaction` L29-L42
- 调 `IPositionRelationServiceApplication.changeCooperationRelation(positions)` 变更协作关系
- 写三分类 1030/1020/1030
- 调 `IPositionHrServiceApplication.afterSavePosition`

### R-R03 · 协作关系成环校验 🔌（仅 his_save）

- **可变性**：`pluggable`（需显式启用）
- **实现**：`PositionHisLoopValidator.checkTeamUpRel` L121-L165
- **触发**：`PositionHisSaveOp.onAddValidators` 显式 `.checkHis()` 激活 (sysRel) + 分录 `entryentity` 不空时检查 (teamUpRel)
- **校验点**：同一 `reportingtype` 同一时间段内 · 协作关系不成环

---

## 八、删除规则（历史版本特殊 · 软删除）

### R-D01 · 软删除 🔒

- **规则**：岗位没有标准 `delete` opKey（不在 57 opKey 列表）· 删除走软删除 `isdeleted=1`
- **可变性**：`hardcoded`（由平台时序模型 + 基础资料配置决定）
- **列表过滤**：`PositionList.setFilter` 自动追加 `isdeleted != 1`

---

## 九、字段可变性速查（基于 scene_doc.json 70+ 字段 minefield）

| Minefield | 数量 | 典型字段 | 规则 |
|---|---|---|---|
| 🔴 red（系统 / 平台维护） | 18 | `creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` / `sourcevid` / `sourcesyskey` / `isdeleted` / `enabler` / `enabledate` | `isvCanModify: false`，禁止手改（遵循 PR-007 · 预置不可改 + 时序字段不可改） |
| 🟡 yellow（变更级联下游） | 4 | `status` / `enable` / `bsed` / `bsled` / `adminorg` / `parent` | 改前必测下游引用（27 处）· 遵循 PR-009 用 boid 查 |
| ⚪ 无标注（可改） | 35+ | `number`（创建后业务不应改）· `name` · `posduty` · `positiontype` 等业务字段 | 可按 pattern 扩展 |
| 🟢 变更标记（自动填） | 3 | `changetype` / `changeoperate` / `changescene` | 由 Op 自动写，业务勿动 |

---

## 十、规则速查表

| 规则类型 | 数量 | 典型可变性 |
|---|---|---|
| 系统不变式（INV） | 8 | 🔒 hardcoded |
| 列表（R-L） | 4 | 多为 🔌 pluggable |
| 新增 / 修改（R-C） | 8 | 混合 |
| 保存链（R-S） | 4 | 🔌 pluggable |
| 变更（R-V） | 3 | 🔌 pluggable |
| 启停用（R-E） | 2 | 🔌 pluggable |
| 汇报关系（R-R） | 3 | 🔌 pluggable |
| 删除（R-D） | 1 | 🔒 hardcoded（软删除） |

---

## 十一、本场景与职位域 (hjm_jobhr) 的规则对比

| 规则维度 | 岗位 hbpm_positionhr | 职位 hbjm_jobhr |
|---|---|---|
| 必填字段 | `adminorg` 行政组织 | `jobseq` 职位序列 |
| 成环校验 | ✅ `parent` 上级岗位树（行政汇报）+ 协作关系 | ❌ 无自引用 |
| 变动双分类 | ✅ changetype + changeoperate + changescene（5 套预置 id） | ❌ 无（只有 changedescription 文本） |
| 消息触发 | ✅ `ChangeMsgServiceImpl.sendMsg` 调度任务 · 每次 save/disable/enable 都发 | ⚠️ 仅 `JobHrMsgHandleOp` 维护 sourcevid · 无外发 |
| 委托业务校验 | ✅ `IBosPositionService.disablePositions` · 禁用时底层校验下游 | ❌ 无 · 需自扩展 |
| 导入特殊场景 | ✅ `PositionHrSaveOp.isImport()` 分支 + 跳消息 + 编码回收 | ⚠️ 仅 HIES 入口 · 无回收 |
| 区间校验位置 | save / his_save + 导入 | 仅 save · 无分级 |
| 关联方案 | positiontpl 批量回填 8 字段 | 无模板机制 |

---

**📌 来源追溯**：
- INV 1-8：`scene_doc.json` `invariants` 段 L1065-L1108 + `rules_chain_all.json` businessRules + 7 类反编译
- R-L / R-C 清单：`_auto_operations.md` 各 opKey validations + `PositionList.setFilter` + `PositionEdit.propertyChanged`
- 插件类名：`_auto_plugin_registry.md` L11-L47
- PositionHrSaveOp 行为：`PositionHrSaveOp.java` L43-L101
- PositionHrDisableOp 行为：`PositionHrDisableOp.java` L42-L79
- PositionHrEnableOp 行为：`PositionHrEnableOp.java` L45-L94
- PositionHrChangeOp 行为：`PositionHrChangeOp.java` L36-L63
- PositionHrRelationChangeOp 行为：`PositionHrRelationChangeOp.java` L26-L42
- PositionHisSaveOp 行为：`PositionHisSaveOp.java` L60-L114
- PositionHisLoopValidator 逻辑：`PositionHisLoopValidator.java` L68-L220
- 岗位模板字段集：`PositionEdit.java` L183-L184
- 编码规则触发字段：`PositionEdit.getNumberRuleAllFieldSet` L783-L796

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## chgaction 实证补充（HRRelatePageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

## chgaction 实证补充（PositionEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionEdit`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

## chgaction 实证补充（PositionPageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

## chgaction 实证补充（PositionList 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionList`
> 跨类追踪: 10 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

## chgaction 实证补充（AbstractBUListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

## chgaction 实证补充（OrgDisableAndIncludeFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp -->

## chgaction 实证补充（PositionHrSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp -->

## chgaction 实证补充（PositionHrDisableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOrUpdate | positionFutureLogRepository | kd.hrmp.hbpm.business.domain.position.service.impl.PositionF |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `changeMsgServiceImpl.sendMsg`
- `BosPositionServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp -->

## chgaction 实证补充（PositionHrEnableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOrUpdate | positionFutureLogRepository | kd.hrmp.hbpm.business.domain.position.service.impl.PositionF |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `changeMsgServiceImpl.sendMsg`
- `BosPositionServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp -->

## chgaction 实证补充（PositionHrChangeOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp -->

## chgaction 实证补充（PositionHrRelationChangeOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHrRelationChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

## chgaction 实证补充（PositionHisSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->

## chgaction 实证补充（PositionBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->
