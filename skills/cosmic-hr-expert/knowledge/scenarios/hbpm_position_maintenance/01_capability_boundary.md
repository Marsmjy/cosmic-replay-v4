# 能力边界 · 岗位信息维护 (hbpm_positionhr)

> **状态**: 🟢 基于 `scene_doc.json` (70+ 字段 · 2 物理表) + `_auto_operations.md` (57 opKey) + `_auto_plugin_registry.md` (37 插件) + 7 类 position 反编译 (2026-04-24)
> **confidence**: verified
> **数据源**: OpenAPI 实抓 + jar 反编译（hrmp-hbpm-opplugin + hrmp-hbpm-formplugin + hrmp-hbpm-business）

---

## 一、10 条平台级硬规则（必记）

这些规则**跨场景适用**，是定制开发的硬边界：

| # | 规则 | 影响 |
|---|---|---|
| 1 | `number` 字段创建后**不可修改** | 下游 27 处基础资料引用都绑 number（hrpi_empposorgrel / hrpi_empjobrel 等） |
| 2 | 字段 key ≤ **24** 字符 | 数据库列名上限 |
| 3 | 实体 key ≤ **36** 字符 | 数据库表名上限 |
| 4 | 表名 ≤ **25** 字符 | `t_hbpm_position` + `t_hbpm_standposentry` 都在限内 |
| 5 | 时序字段禁止手改 | `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `sourcevid`（`scene_doc.json` 标 `autoComputed: true`；PR-003 违者破坏版本链） |
| 6 | `errorCode="0"` ≠ 成功 | 必须 `getFormSchema` / `list_rules` 二次验证 |
| 7 | `op` 枚举**只 4 值** | `add` / `modify` / `remove` / `move` |
| 8 | `actionType` / `fieldKey` 必须 **PascalCase** | 小写静默忽略 |
| 9 | `updateOperation.plugins` 是**全量替换** | save 有 9 条插件链（`_auto_operations.md` L98-L107），覆盖前必须先 get 再 append |
| 10 | `registerPlugin.targetType` 必须**大写枚举** | 5 值: `BILL_FORM`/`LIST_FORM`/`MOBILE_BILL_FORM`/`OPERATION`/`EVENT` |

---

## 二、✅ 标品原生支持

### 列表展示

- ✅ 岗位列表（主实体 `hbpm_positionhr`，路径：组织发展云 → 组织管理 → 岗位）
- ✅ 2 种列表视图：当前版本 `hbpm_positionhr` / 历史版本 `hbpm_positionhis`
- ✅ 分页查询 + 按编码 / 名称 / 行政组织 / 上级岗位 / 岗位类型筛选
- ✅ 标准岗位 vs 业务岗位分视图（`PositionList.setFilter` 按 `isstandardpos` + `viewstdpos` 开关切换）
- ✅ HIES 导入导出套件：`importdata_hr` / `show_import_record_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr`（`_auto_operations.md` L32-L37）
- ✅ 列表默认过滤软删除：`isdeleted != 1`（`PositionList.setFilter` L60）
- ✅ 列表默认排序：`adminorg.id asc, positiontype.id asc, isleader desc`
- ✅ 标品导入入口 `importdata` 挂 `PositionRuleNumberImportPlugin`（`_auto_operations.md` L214）
- ✅ 行政组织数据权限过滤（`PermHelper.getHRPermOrg` + `PositionEdit.beforeF7Select` adminorg 过滤）

### 新增 / 修改岗位

- ✅ 单个新增：opKey `new` / `modify`（`_auto_operations.md` L71-L82）
- ✅ 复制：`copy` / `hiscopy`
- ✅ **必填字段**：仅 `adminorg` 行政组织（`scene_doc.json` L615 `required: true`），以及分录 `applicableorgentity.applicableorg`（标准岗位适用组织）
- ✅ 保存校验链 9 个插件（save opKey，`_auto_operations.md` L94-L107）：编码规则 → 基础资料版本化 → HR 状态 → HR 日志 → HR 启用态 → 时序通用 → HR 原始值 → 时序唯一 → `PositionHrSaveOp`
- ✅ 保存时 1 条合法性校验（`MustInput 6096194600001fac`，`_auto_operations.md` L112）
- ✅ 编码自动生成：`PositionCodeRuleHelper.getCode` + `recycleNumber`（前端切字段时会回收重发）
- ✅ 岗位模板联动：选中 `positiontpl` 自动回填 `job` / `jobscm` / `lowjoblevel` / `highjoblevel` 等 8 个字段（`PositionEdit.changePositionTpl` L517-L549）

### 启用 / 禁用

- ✅ `enable` / `disable` 双路操作（`_auto_operations.md` L122-L141）
- ✅ `PositionHrDisableOp.afterExecuteOperationTransaction` 调 `IBosPositionService.disablePositions(positionIds)` 做底层业务校验 + `ChangeMsgServiceImpl.sendMsg()` 发变更消息
- ✅ `PositionHrEnableOp.afterExecuteOperationTransaction` 调 `IBosPositionService.commonSyncPositions()` + 发变更消息
- ✅ `beginOperationTransaction` 阶段自动补写 `changetype / changeoperate / changescene` 三分类（disable=1040/1030/1040，enable=1070/1070/1070）

### 时态版本管理（时序模型核心）

- ✅ `newhisversion` 新增数据版本（`donothing` · `_auto_operations.md` L336-L341）
- ✅ `revise` 修订（就地改当前版本 · INV-07）/ `hiscopy` 复制 / `change` 变更
- ✅ `confirmchange` 确认变更：挂 3 插件（`HisModelOPCommonPlugin` + `HisUniqueValidateOp` + `PositionHrChangeOp`）+ 1 校验（`_auto_operations.md` L375-L387）
  - `PositionHrChangeOp.beginOperationTransaction` 仅处理非 `iscurrentversion` 的版本 · 写 `changetype=1020 / changeoperate=1020 / changescene=1020`
- ✅ `reviserecord` / `versionchangecompare` / `showallversion` / `hisversion_view`（4 个只读版本操作）
- ✅ `his_save` 历史保存：挂 `PositionHisSaveOp`（L469-L473）· 负责历史版本迁移 + 区间链 bsled 自动计算

### 岗位树 / 上级岗位

- ✅ `parent` 字段：`HRPositionField` → `hbpm_positionhrf7` 自引用
- ✅ **成环校验**：`PositionHisLoopValidator.checkSysRel` (history save 时触发)
  - 检查行政汇报链（`reportingtype="1010"`）是否成环
  - 按 `bsed/bsled` 时间段判断同一时期不成环
- ✅ F7 选父岗位时自动排除自己（`PositionEdit.beforeF7Select` parent 分支 L932-L936：`QFilter("boid","!=",boid)`）

### 汇报 / 协作关系

- ✅ `entryentity` 子分录（协作关系）：`reporttype` + `targetpos` + `id`
- ✅ 专属分录 opKey：`newentry` / `deleteentry`（L417-L425）
- ✅ `reportchange` 协作关系变更（donothing，UI 切入 edit 态）
- ✅ `dochangerelation` 汇报关系确认变更：挂 `PositionHrRelationChangeOp`（L431-L443）· 写 `changetype=1030 / changeoperate=1020 / changescene=1030`
- ✅ **协作关系成环校验**：`PositionHisLoopValidator.checkTeamUpRel`（非默认开，`his_save` 时显式 `checkHis()` 启用）
- ✅ `reportchange` / `do_close` 成对出现：reportchange 进入编辑态 · do_close 结束回 view
- ✅ `confirmrelation` 按钮提交协作关系变更
- ✅ `relationimport` 导入协作关系（`PositionRelationImportPlugin`）

### 岗位模板

- ✅ `positiontpl` 字段：`BasedataField` → `hbpm_positiontpl`
- ✅ 前端选模板后批量回填 `POSITIONTPL_FILED_KEYS = [job, jobscm, lowjoblevel, highjoblevel, lowjobgrade, highjobgrade, jobgradescm, joblevelscm]`
- ✅ 模板的可修改字段白名单：`ablemodifyfield=true` + `fieldrange` 子分录控制（`PositionEdit.setTplFieldEnable` L827-L852）
- ✅ 参数控制模板显隐：`SystemParamHelper.getBatchParameter` + `openpositiontpl` 开关

### 职级职等范围

- ✅ 双方案依赖：`jobgradescm` 职等方案 / `joblevelscm` 职级方案
- ✅ 4 区间字段：`lowjobgrade` / `highjobgrade` / `lowjoblevel` / `highjoblevel`
- ✅ 2 范围文本：`jobgraderange` / `joblevelrange`（冗余存储）
- ✅ 前端 F7 范围选：`JobLevelGradeRangeUtil` 统一处理 4 字段联动
- ✅ 服务端校验：`JobLevelGradeRangeImportValidator`（`PositionHrSaveOp.onAddValidators` 中 `if (isImport())` 时注册；`PositionHisSaveOp` 历史保存时总是注册）
  - 校验 15 条业务规则（`JobLevelGradeRangeCheck_0` ~ `_15`）：职位/方案/范围/顺序码
- ✅ **岗位是否必关联职位参数**：`SystemParamHelper.getPosMustRelateJobParameter(orgId)` · 控制 `job` 是否必填

### 变动双分类（岗位特有）

- ✅ **3 分类字段**（岗位场景独有 · 区别于职位）：
  - `changetype` 变动类型（→ `hbpm_changetype` · 1010 新建 / 1020 变更 / 1030 汇报关系变更 / 1040 禁用 / 1070 启用 / 1030 关系变更）
  - `changeoperate` 变动操作（→ `hbpm_changeoperate` · 1010 / 1020 / 1030 / 1070）
  - `changescene` 变动场景（→ `hbpm_changescene` · 1010 新建 / 1020 变更 / 1030 汇报变更 / 1040 禁用 / 1070 启用）
- ✅ 由各 `PositionHr*Op.beginOperationTransaction` 自动回填 · 业务层无需关心
- ✅ `changedesc` 变动原因（→ `hbpm_changereason`）/ `changedescription` 变动说明文本（用户填）

### 附加能力

- ✅ `namehistory` 改名 / `namehistoryview` 名称历史查询（基础资料名称版本化）
- ✅ `logview` / `viewonelog` 日志查询
- ✅ `selecttplprint` 打印岗位说明书（`_auto_operations.md` L475-L479）
- ✅ `saveandnew` / `submitandnew` 保存/提交并新增
- ✅ 时态版本模型（基于 `hbp_histimeseqtpl` 继承链）：`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `bsed` / `bsled` / `sourcevid` / `hisversion`
- ✅ `sort` 排序（L482-L486）
- ✅ `import_positiondetailrevise` 批量修订属性（`PositionReviseHRImportPlugin`）
- ✅ `reviseimport` 修订导入数据（`PositionReviseImportPlugin`）
- ✅ 主负责岗标记：`isleader`（CheckBoxField）
- ✅ 标准岗位适用范围分录：`applicableorgentity` → `t_hbpm_standposentry`（标准岗位适用多组织，含 `iscontainsu` 是否包含下级）

---

## 三、❌ 标品不支持（需定制）

- **禁用前检查有在职员工 / 任职关系**：`PositionHrDisableOp` 只委托 `IBosPositionService.disablePositions` · 未反查 27 处下游引用（hrpi_empposorgrel 等）· 见 CS-04
- **岗位设立日期 establishmentdate 跟 bsed 联动**：前端 `propertyChanged@bsed` 只有"设立日期为空时填 bsed"的单向逻辑（`PositionEdit.propertyChanged` bsed 分支 L497-L502）· 已设立的不重填
- **职级职等范围跨方案的级联校验**：4 区间字段切换方案时不会自动重置，有配置残留风险
- **岗位标准化转换**：`isstandardpos` 切换后的 `applicableorgentity` 分录不会强制校验必填
- **编码按 `jobseq / positiontype` 前缀生成**：默认走 `CodeRulePlugin`，不带 positiontype 联动
- **失效日期 bsled 到期自动禁用**：需要调度任务
- **协作关系批量重建**：只支持单条维护 + HIES 导入
- **岗位容量 / 编制数字段**：scene_doc.json 无 `capacity` / `headcount` 等字段 · 需扩展

---

## 四、🔧 可通过配置实现（无需代码）

### 编码规则
- `CodeRulePlugin` 标品支持按**规则表**配置编码前缀 / 步长（`_auto_operations.md` save 链第 1 位）
- `PositionCodeRuleHelper.getCode` 读规则 · `recycleNumber` 失败时回收序号 · 业务侧只配规则表

### 岗位属性字典
- `positiontype` 岗位类型 → `hbpm_positiontype`
- `positiontpl` 岗位模板 → `hbpm_positiontpl`
- `changetype` / `changescene` / `changeoperate` → 各自平台基础资料（1010/1020/1030/1040/1070 预置编码）
- `diplomareq` 学历要求 → `hbss_diploma`
- `workplace` 工作地 → `hbss_workplace` · `city` → `bd_admindivision` · `countryregion` → `bd_country`

### 控制策略
- `org` 字段走 HR 基础资料公共控制策略（继承自 L1 `hbp_bd_tpl_all`）
- `orgdesignbu` 职位体系管理组织：`OrgField`（`scene_doc.json` L810）

### 系统参数（SystemParamHelper）
- **岗位是否必关联职位信息**：`getPosMustRelateJobParameter(orgId)` · 路径：组织发展云 → 组织管理 → 参数配置 · 控制 `job` 是否必填 + `joblevelscm/jobgradescm` 是否允许独立于 job（`JobLevelGradeRangeImportValidator` 读此参数）
- **打开岗位模板对话框**：`getBatchParameter` → `openpositiontpl` 控制 `positiontpl` 显隐 / 必填

---

## 五、💻 必须通过插件扩展

| 需求类型 | 推荐扩展点 | 参考插件类名 |
|---|---|---|
| 岗位编码按类型前缀生成 | `beforeExecuteOperationTransaction@save` RowKey=0 | 并列注册（参考 PR-001 · 不继承 `PositionHrSaveOp`）·新 `extends HRDataBaseOp` |
| 选模板自动带出字段 | `propertyChanged@hbpm_positionhr` | 并列注册 `extends AbstractFormPlugin` · 注册为 `BILL_FORM` |
| 保存前职级区间校验 | `onAddValidators@save` | 自定义 `extends AbstractValidator` 并列注册（不继承 `PositionHrSaveOp`） |
| 启用校验（上级岗位必须启用） | `onAddValidators@enable` | 类比 `JobLevelGradeRangeImportValidator` · 新 Validator 并列挂 |
| 禁用前检查在职任职关系 | `beforeExecuteOperationTransaction@disable` | 并列挂新插件（不继承 `PositionHrDisableOp`）· 查 `hrpi_empposorgrel.position.boid = ?` |
| 变更消息订阅 | 独立事件插件 | 参考 `ChangeMsgServiceImpl` 走苍穹调度（sch_task）· 或用 BEC（PR-011） |
| 列表按标岗/业务岗切换 | `setFilter@hbpm_positionhr` | 参考 `PositionList.setFilter` L52-L63 |
| 协作关系成环校验扩展 | `onAddValidators@his_save / @dochangerelation` | 参考 `PositionHisLoopValidator.checkTeamUpRel` |
| 父岗位选择过滤 | `beforeF7Select@parent` | 参考 `PositionEdit.beforeF7Select` L932-L936（已自带排除自身）· 继承追加 |

---

## 六、📊 能力矩阵（57 opKey 分组）

| 子场景 | opKey 数 | 已有业务插件 | 可扩展 |
|---|---|---|---|
| **CRUD 基础** (new/modify/view/copy/hiscopy/refresh) | 6 | 0 | ✅ 通过 addRule / registerPlugin |
| **保存链** (save/saveandnew/submitandnew) | 3 | `PositionHrSaveOp` (save) | ✅ ⭐ 最常扩展 |
| **启停用** (enable/disable) | 2 | `PositionHrEnableOp` / `PositionHrDisableOp` | ✅ |
| **时态版本** (newhisversion/change/revise/confirmchange/...) | 9 | `PositionHrChangeOp` (confirmchange) · `PositionHisSaveOp` (his_save) | ✅ |
| **协作关系** (newentry/deleteentry/reportchange/dochangerelation/do_close/relationimport) | 6 | `PositionHrRelationChangeOp` | ✅ 专属业务 |
| **导入导出 HIES** | 8 | `PositionHRImportPlugin` / `PositionRuleNumberImportPlugin` / `PositionHRExportPlugin` / `PositionRelationImportPlugin` / `PositionReviseHRImportPlugin` / `PositionReviseImportPlugin` | ⚠️ HIES 走 `cusstartpage=hismodel_importstart` 定制页 |
| **分录** (newentry/deleteentry/sort) | 3 | 0 | ✅ |
| **打印 / 日志** (selecttplprint/logview/viewonelog/namehistory/namehistoryview) | 5 | 0 | ✅ |
| **导航 / 工具栏** (first/previous/next/last/mobtoolbar*/option/returndata/close/do_close) | 11 | 0 | ❌ 一般不改 |
| **历史保存** (his_save) | 1 | `PositionHisSaveOp` | ✅ |
| **变更** (change) | 1 | 0 | ✅ |

---

## 七、🚨 标品限制（重要）

- **主实体物理表**：`t_hbpm_position` + `t_hbpm_standposentry`（2 张物理表 · `scene_doc.json` L37）
- **多语言字段 10 个**：`name` / `simplename` / `description` / `oriname` / `posduty` / `posstandard` / `posorientation` / `knowledgereq` / `skillreq` / `abilityreq` / `experiencereq` / `agereq`（`MuliLangTextField`；**没有独立 `_l` 多语言表**，本场景多语言直接存主表）
- **时态模型依赖**：继承链 4 级（L0 bos_basetpl → L1 hbp_bd_tpl_all → L2 hbp_histimeseqtpl → L3 hbpm_positionhr 自身 · `scene_doc.json` `inheritance` 段）
- **行政组织必填**：`adminorg` 是 `HRAdminOrgField` 唯一 required=true 字段（`scene_doc.json` L615）
- **系统预置保护**：`issyspreset=true` 的岗位不能删除
- **出厂数据字段族**：`orinumber` / `oriname` / `oristatus` 共 3 字段，改会破坏 `HRBaseOriginalOp`（save 链第 7 位）
- **软删除字段**：`isdeleted` 由系统维护，不能手改；列表默认 `isdeleted != 1` 过滤
- **变动 3 分类由 Op 自动填**：`changetype` / `changeoperate` / `changescene` 由各 `PositionHr*Op.beginOperationTransaction` 写入（参考 `PositionHrDisableOp` L57-L70）· 业务层**不要手动设置**，会被覆盖
- **历史表面**：`hbpm_positionhis` 是历史版本视图（与 `hbpm_positionhr` 共享 `t_hbpm_position` 主表 · 通过 `iscurrentversion` 区分 · 遵循 PR-008）

---

## 八、扩展对象选择决策树

```
我要改什么？
    │
    ├─ 岗位业务字段 / 规则   →  扩展 hbpm_positionhr (主)
    │                        物理表: t_hbpm_position
    │
    ├─ 标准岗位适用组织      →  扩展 applicableorgentity 分录
    │                        物理表: t_hbpm_standposentry
    │
    ├─ 列表显示 / 过滤规则   →  参考 PositionList (HRDataBaseList 子类)
    │                        父类 HRDataBaseList 的 setFilter / filterContainerInit
    │
    ├─ 前端字段联动          →  参考 PositionEdit.propertyChanged
    │                        (HRDataBaseEdit 子类, 已有 adminorg/city/countryregion/parent/bsed/positiontpl 6 分支)
    │
    ├─ 启停用业务检查        →  并列挂新插件（不覆盖 PositionHr[En|Dis]ableOp · 见 PR-001）
    │
    ├─ 变更消息订阅          →  走 BEC (PR-011) · 不自接 MQ · 参考 ChangeMsgServiceImpl 的调度任务模式
    │
    ├─ 协作关系 / 上级岗位校验 → 参考 PositionHisLoopValidator （成环检测）
    │
    └─ 不知道                →  先看 07_ext_points.md
```

**⚠️ 不要扩展继承父模板**：`hbp_histimeseqtpl`（时序模板）/ `hbp_bd_tpl_all`（HR 基础资料模板）/ `bos_basetpl`（苍穹基础）—— 改了全 HR 时态资料炸。

---

## 九、字段类型约束（本场景特有）

### `scene_doc.json` 实抓 70+ 字段分布（按类型）

- `TextField` × 6（number / orinumber / sourcesyskey / hisversion / changedescription / joblevelrange / jobgraderange）
- `MuliLangTextField` × 10（name / simplename / description / oriname / posduty / posstandard / posorientation / knowledgereq / skillreq / abilityreq / experiencereq / agereq）
- `BasedataField` × 11（positiontype / diplomareq / lowjoblevel / highjoblevel / lowjobgrade / highjobgrade / jobgradescm / joblevelscm / jobscm / positiontpl / job / changetype / changeoperate / changescene / changedesc / countryregion / workplace / city）
- `HRAdminOrgField` × 2 ⭐ **岗位特有**（adminorg · 分录 applicableorg）
- `HRPositionField` × 1 ⭐ **自引用**（parent 上级岗位 → `hbpm_positionhrf7`）
- `OrgField` × 2（org / orgdesignbu）
- `DateField` × 3（bsed / bsled / firstbsed / establishmentdate）
- `DateTimeField` × 2（disabledate / enabledate）
- `ComboField` × 4（initdatasource / oristatus / datastatus / isstandardpos）
- `BigIntField` × 3（boid / sourcevid / entryboid）
- `IntegerField` × 1（index）
- `CheckBoxField` × 4（issyspreset / iscurrentversion / isleader / isdeleted / iscontainsu）
- `EntryEntity` × 2（entryentity 协作关系分录 / applicableorgentity 适用组织分录）
- 平台维护 × 7（CreaterField / ModifierField / UserField × 2 / CreateDateField / ModifyDateField / MasterIdField / BillStatusField × 2）

### ❌ 禁用 / 废弃
- `EmployeeField` → OpenAPI buildMeta 74 值枚举不支持（用 `BasedataField` + `basedataNumber: "hrpi_employeenewf7query"` 替代）
- 不要在新规则引用软删除行（`isdeleted=1` 是系统标记已删）

---

## 十、OpenAPI 覆盖度（本场景实测）

| 操作 | OpenAPI 支持 | 本场景关键点 |
|---|---|---|
| buildMeta 建新字段 | ✅ | ISV 前缀 `{isv}_{semantic}`，参考 70+ 字段命名 · 注意多语言表继承规则（本场景无 `_l`，直接存主表） |
| modifyMeta 改元数据 | ✅ | formId = `hbpm_positionhr`（不是 `/IJP/IQGX57W` 内部 ID） |
| addRule 加规则 | ✅ | 但本场景当前 formRule / bizRule = 0（`_auto_rules.md`）· 业务规则都是 Java 插件 |
| registerPlugin 注册插件 | ✅ | 参考 `PositionHrSaveOp` 的 `OPERATION` 类型 |
| updateOperation 改操作 | ✅ | save 9 插件 / confirmchange 3 插件必须先 get 再 append |
| 时序模型版本操作 | ✅ | newhisversion / revise / confirmchange / his_save 都可扩展 |
| HIES 导入模板 | ⚠️ 部分 | `importdata_hr` 走 `cusstartpage: hismodel_importstart`，插件注册在自定义页面 |
| HRPositionField 自引用 | ✅ | `parent` 字段 F7 自动拉 `hbpm_positionhrf7` 视图 |

---

**📌 来源追溯**：
- 10 条硬规则：`knowledge/_shared/platform_rules.json` 11 PR + `knowledge/cosmic_realworld_traps/` + `platform/openapi_capability_map.md`
- 57 opKey 清单：`_auto_operations.md` 总览表 L9-L67
- 37 插件清单：`_auto_plugin_registry.md` L11-L47
- 7 类反编译：`_sdk_audit/_decompiled/scenarios/position/PositionHr[Save|Disable|Enable|Change|RelationChange]Op.java` + `PositionHisSaveOp` + `PositionHisLoopValidator` + `PositionEdit` + `PositionList`
- 70+ 字段清单：`scene_doc.json` L65-L993
- 2 物理表：`scene_doc.json` L37 `physicalTable` + L959 `applicableorgentity.physicalTable`
