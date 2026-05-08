# 能力边界 · 职位体系维护 (hbjm_jobhr)

> **状态**: 🟢 基于 `scene_doc.json` (64 字段) + `_auto_operations.md` (61 opKey) + `_auto_plugin_semantics.md` (5 类反编译) 整合
> **confidence**: verified
> **数据源**: OpenAPI 实抓 + jar 反编译 (2026-04-24)

---

## 一、10 条平台级硬规则（必记）

这些规则**跨场景适用**，是定制开发的硬边界：

| # | 规则 | 影响 |
|---|---|---|
| 1 | `number` 字段创建后**不可修改** | 下游职位 / 岗位 / 人员引用都绑定 number |
| 2 | 字段 key ≤ **24** 字符 | 数据库列名上限 |
| 3 | 实体 key ≤ **36** 字符 | 数据库表名上限 |
| 4 | 表名 ≤ **25** 字符 | 数据库硬上限（`t_hbjm_job` / `t_hbjm_job_i` 已在限内） |
| 5 | 时序字段禁止手改 | `boid` / `iscurrentversion` / `datastatus` / `hisversion` / `firstbsed` / `sourcevid`（`scene_doc.json` 中 minefield=red 的时序字段） |
| 6 | `errorCode="0"` ≠ 成功 | 必须二次验证（`getFormSchema` / `list_rules`） |
| 7 | `op` 枚举**只 4 值** | `add` / `modify` / `remove` / `move` |
| 8 | `actionType` / `fieldKey` 必须 **PascalCase** | 小写静默忽略 |
| 9 | `updateOperation.plugins` 是**全量替换** | 本表 save 有 10 条插件链（`_auto_operations.md` L99-L113），覆盖前必须先 get 再 append |
| 10 | `registerPlugin.targetType` 必须**大写枚举** | 5 值: `BILL_FORM`/`LIST_FORM`/`MOBILE_BILL_FORM`/`OPERATION`/`EVENT` |

---

## 二、✅ 标品原生支持

### 列表展示
- ✅ 基础资料树形 / 列表视图（主实体 `hbjm_jobhr`，路径：组织发展云 → 职位管理 → 职位）
- ✅ 分页查询 + 按编码 / 名称 / 职位序列 / 职位族 / 职位类筛选
- ✅ HIES 导入导出套件：`importdata_hr` / `show_import_record_hr` / `export_from_list_hr` / `export_from_impttpl_hr` / `export_from_expttpl_hr` / `show_export_record_hr`（`_auto_operations.md` L54-L59）
- ✅ 标品导入 `importdata` 内嵌插件 `kd.hrmp.hbjm.formplugin.web.impt.JobBaseImportPlugin`（`_auto_operations.md` L364）
- ✅ 按创建组织数据权限过滤（`JobBaseBuListPlugin.filterContainerBeforeF7Select`，`_auto_plugin_semantics.md` L59-L70）
- ✅ 列表超链点击跳转详情（`JobInitFilterCommonPlugin.billListHyperLinkClick`，`_auto_plugin_semantics.md` L75-L81）

### 新增 / 修改职位
- ✅ 单个新增：opKey `new` / `modify`（`_auto_operations.md` L75-L91）
- ✅ **必填字段**：仅 `jobseq` 职位序列（`scene_doc.json` L664 `required: true`），其它字段非必填（由保存时 `MustInput` 校验器决定）
- ✅ 保存校验链 10 个插件（save opKey，`_auto_operations.md` L99-L113）：编码规则 → 基础资料保存 → 基础资料版本化 → HR 状态 → HR 日志 → HR 启用态 → HR 原始值 → 历史唯一 → 历史模型通用 → `JobHrSaveOp`
- ✅ 保存时 6 种合法性校验（`_auto_operations.md` L115-L122）：`MustInput` / `FormValidate` / `GroupFieldUnique × 3` / `生效日期不能填写未来`
- ✅ 职等方案 / 职级方案双引用（`jobgradescm` → `hbjm_jobgradescmhr`，`joblevelscm` → `hbjm_joblevelscmhr`）

### 审批流转（全套基础资料生命周期）
- ✅ `submit` 提交 → `audit` 审核 → `unaudit` 反审核 → `unsubmit` 撤销（`_auto_operations.md` L249-L295）
- ✅ `submit` 8 插件执行链 + 2 校验（`1cc0054f000017ac` 合法性 + `RS=E9QE25UN` 字段合规）
- ✅ `audit` 触发 `JobHrAuditOp`（`_auto_operations.md` L164 · `_auto_plugin_registry.md` L57）
- ✅ 状态流转：暂存 `A` → 已提交 `B` → 已审核 `C`（由 operationType parameter 中 `Value` 决定，`_auto_operations.md` L155 / L179 / L254）

### 启用 / 禁用 / 删除
- ✅ `enable` / `disable` 双路操作，各挂 3 个插件（标品 BaseData* + `HisModelOPCommonPlugin` + `JobHrEnableOp` / `JobHrDisableOp`）
- ✅ `JobHrEnableOp` 注册自定义 `JobEnableValidator`（`rules_chain_all.json` L678-L688）
- ✅ `JobHrEnableOp.beforeExecuteOperationTransaction` 读 `boid` / `enable` 双字段做启用预处理（`rules_chain_all.json` L733-L735）
- ✅ `delete` 5 插件链 + 3 校验（含 `hrbddeletevalidator` HR 基础资料删除校验，`_auto_operations.md` L148）

### 历史版本管理（时序模型核心）
- ✅ `newhisversion` 新增数据版本（`_auto_operations.md` L547-L551）
- ✅ `hisversioninfo` / `reviserecord` / `versionchangecompare` / `showallversion`（4 个只读版本操作）
- ✅ `revise` 修订 / `hiscopy` 复制 / `change` 变更
- ✅ `confirmchange` 确认变更：挂 3 插件（`HisModelOPCommonPlugin` + `HisUniqueValidateOp` + `JobHrSaveOp`）+ 3 校验（`_auto_operations.md` L591-L605）

### 管理权与组织控制
- ✅ `assign` 分配 / `unassign` 取消分配 / `assign_new` / `auto_assign` 自动分配管理
- ✅ `tbl_assign_import` Excel 导入分配关系
- ✅ `bdctrlchange` 变更控制策略
- ✅ `orgpermchange` 管理权转让

### 附加能力
- ✅ `namehistory` 改名 / `namehistoryview` 名称历史查询（基础资料名称版本化）
- ✅ `logview` / `viewonelog` 日志查询
- ✅ `selecttplprint` 打印职位说明书（`_auto_operations.md` L612-L615，operationType: `selecttplprint`）
- ✅ `saveandnew` 保存并新增（`BaseDataSavePlugin` 辅助插件，`_auto_operations.md` L327-L337）
- ✅ 时态版本模型（基于 `hbp_histimeseqtpl` 继承链）：`boid` / `iscurrentversion` / `datastatus` / `firstbsed` / `bsed` / `bsled` / `sourcevid` / `hisversion`（`scene_doc.json` L377-L497）

---

## 三、❌ 标品不支持（需定制）

- **职位序列批量跨序列调整**：标品只支持单条新增 / HIES 模板导入；无专用入口
- **职位说明书自定义打印模板**：`selecttplprint` 只选单模板，不支持"按职位类别选不同模板"
- **跨职位序列级联校验**：如"总监级职位必须属于管理序列"需要插件
- **最低职级 > 最高职级的前端校验**：`lowjoblevel` / `highjoblevel` / `lowjobgrade` / `highjobgrade` 4 字段前端不做区间校验
- **职位继任者规划（Succession）**：不属于本场景，需走人才发展
- **职位编码自动按职位序列前缀生成**：默认走 `CodeRulePlugin`，无职位序列联动
- **失效日期 bsled 到期自动禁用**：需要调度任务

---

## 四、🔧 可通过配置实现（无需代码）

### 编码规则
- `CodeRulePlugin` 标品支持按**规则表**配置编码前缀 / 步长（`_auto_operations.md` L104 save 链第 1 位）

### 职位属性字典
- `jobtype` 职位类别 → `hbjm_jobtype`（`scene_doc.json` L548-L557）
- `jobseq` 职位序列 → `hbjm_jobseqhr`（`scene_doc.json` L660-L671）
- `jobfamily` 职位族 → `hbjm_jobfamilyhr`
- `jobclass` 职位类 → `hbjm_jobclasshr`
- `jobgradescm` 职等方案 → `hbjm_jobgradescmhr`；`joblevelscm` 职级方案 → `hbjm_joblevelscmhr`
- `diplomareq` 学历要求 → `hbss_diploma`
- `depcytype` 属地员工类别 → `hbss_depcytype`（⚠ **`scene_doc.json` L566 标注"(废弃)"**）

### 控制策略
- `ctrlstrategy` 字段（`scene_doc.json` L188-L198）在**基础服务云 → 控制策略**中配置
- `createorg` / `org` / `useorg` 3 组织字段支持多组织控制

### 唯一性规则
- 保存校验中 3 条 `GroupFieldUnique`（`_auto_operations.md` L120-L122）：
  - `0+/SL/MZ=VJB` 创建组织和编码组合唯一（默认 disabled）
  - `2=K9URZCEUUS` 编码唯一性（默认 enabled）
  - `23ILP6JS0TBC` 创建组织和名称组合唯一（默认 disabled）
- 在标品控制策略 UI 可切换启用 / 禁用

---

## 五、💻 必须通过插件扩展

| 需求类型 | 推荐扩展点 | 参考插件类名 |
|---|---|---|
| 职位编码按序列前缀生成 | `beforeExecuteOperationTransaction@save` | 继承 `JobHrSaveOp` 或并列注册 |
| 选职位序列自动填最高 / 最低职级 | `onFieldChange@hbjm_jobhr` | 新建 `AbstractFormPlugin` 注册为 `BILL_FORM` |
| 保存前职级区间校验 | `onAddValidators@save` | 自定义 Validator 在 `JobHrSaveOp.onAddValidators` 并列注册 |
| 启用校验（职位族必须启用） | `onAddValidators@enable` | 类比现有 `JobEnableValidator`（`rules_chain_all.json` L678） |
| 禁用前检查有在职人员 | `beforeExecuteOperationTransaction@disable` | 继承 `JobHrDisableOp` |
| 变更消息监听 | 独立操作插件 | 参考 `JobHrMsgHandleOp`（`_auto_plugin_semantics.md` L13-L26） |
| 列表按职位族过滤 | `filterContainerBeforeF7Select` | 参考 `JobBaseBuListPlugin`（`_auto_plugin_semantics.md` L59-L70） |
| 附件变更监听 | `afterDoOperation` | 参考 `JobHisBasedataFiledChangeEdit.cacheAttachmentData / isAttachmentChange`（`_auto_plugin_semantics.md` L29-L43） |

---

## 六、📊 能力矩阵（61 opKey 分组）

| 子场景 | opKey 数 | 已有业务插件 | 可扩展 |
|---|---|---|---|
| **CRUD 基础** (new/modify/view/copy/refresh) | 5 | 0 | ✅ 通过 addRule / registerPlugin |
| **保存链** (save/saveandnew/submit/unsubmit/submitandnew) | 5 | `JobHrSaveOp` (save) | ✅ ⭐ 最常扩展 |
| **审核链** (audit/unaudit) | 2 | `JobHrAuditOp` (audit) | ✅ |
| **启停用** (enable/disable) | 2 | `JobHrEnableOp` / `JobHrDisableOp` | ✅ 且已有自定义 Validator 样板 |
| **删除** (delete) | 1 | 无 | ⚠️ 标品已有 `hrbddeletevalidator` 校验 |
| **时态版本** (hisversioninfo/change/revise/newhisversion/confirmchange/...) | 10 | `JobHrSaveOp` 在 confirmchange 复用 | ✅ |
| **导入导出 HIES** | 6 | `JobBaseImportPlugin` (importdata) | ⚠️ HIES 走 `cusstartpage` 定制页 |
| **分配 / 管理权** (assign/unassign/auto_assign/...) | 6 | 无 | ✅ |
| **打印 / 日志** (selecttplprint/logview/viewonelog/namehistory) | 4 | 无 | ✅ |
| **导航 / 工具栏** (first/previous/next/last/mobtoolbar*/option/returndata/close) | 10 | 无 | ❌ 一般不改 |

---

## 七、🚨 标品限制（重要）

- **主实体物理表**：`t_hbjm_job` + `t_hbjm_job_i`（2 张物理表；`t_hbjm_job_i` 是多语言子表，`scene_doc.json` L27 `physicalTable`）
- **多语言字段 12 个**：`name` / `simplename` / `description` / `oriname` / `jobduty` / `jobstandard` / `joborientation` / `knowledgereq` / `skillreq` / `abilityreq` / `experiencereq` / `agereq`（`MuliLangTextField`，存 `t_hbjm_job_i`）
- **时态模型依赖**：继承链 5 级（`_auto_inherit_chain.md` L12-L16），底层 `bos_basetpl` → `1942c188000065ac`（HR 基础资料模板） → 时序模板 → 职位自身
- **职位序列必填**：`jobseq` 是唯一 required=true 字段（`scene_doc.json` L664），其它含编码 / 名称都是非必填
- **系统预置保护**：`issyspreset=true` 的职位不能删除（`autoComputed: true`，`minefield: red`，`scene_doc.json` L278-L289）
- **出厂数据字段族**：`orinumber` / `oriname` / `oristatus` 共 3 字段带 `minefield: red`，修改破坏出厂数据比对

---

## 八、扩展对象选择决策树

```
我要改什么？
    │
    ├─ 职位业务字段 / 规则   →  扩展 hbjm_jobhr (主)
    │                        物理表: t_hbjm_job(+ _i)
    │
    ├─ 列表显示 / 过滤规则   →  参考 JobBaseDataListPlugin / JobBaseBuListPlugin
    │                        (HRDataBaseList 子类, _auto_plugin_semantics.md)
    │
    ├─ 附件 / 字段变更监听    →  参考 JobHisBasedataFiledChangeEdit
    │
    ├─ 启停用业务检查        →  覆盖 JobHrEnableOp / JobHrDisableOp
    │
    ├─ 变更消息订阅          →  独立 op 插件, 参考 JobHrMsgHandleOp
    │                        (读 boid/iscurrentversion, 写 sourcevid)
    │
    └─ 不知道                →  先看 07_ext_points.md
```

**⚠️ 不要扩展继承父模板**：`hbp_histimeseqtpl`（时序模板）/ `hbp_bd_tpl_all`（HR 基础资料模板）/ `bos_basetpl`（苍穹基础）—— 改了全 HR 时态资料炸。

---

## 九、字段类型约束（本场景特有）

### `scene_doc.json` 实抓 64 字段分布

- `TextField` × 11（number / orinumber / changedescription / hisversion / sourcesyskey / joblevelrang / jobgraderang / highjoblevelname / lowjoblevelname / lowjobgradename / highjobgradename）
- `MuliLangTextField` × 12（name / simplename / description / oriname / agereq / knowledgereq / skillreq / abilityreq / experiencereq / joborientation / jobduty / jobstandard）
- `BasedataField` × 8（lowjoblevel / highjoblevel / lowjobgrade / highjobgrade / jobtype / depcytype / diplomareq / jobgradescm / joblevelscm）
- `HisModelBasedataField` × 3 ⭐ **职位特有**（jobseq / jobfamily / jobclass，时序基础资料字段类型）
- `MulBasedataField` × 1（jobscm 职位体系方案，子表 `t_hbjm_jobscmmul`）
- `OrgField` × 4（createorg / org / useorg / srccreateorg）
- `DateField` × 3（bsed / bsled / firstbsed）
- `DateTimeField` × 1（disabledate）
- `ComboField` × 4（ctrlstrategy / initdatasource / oristatus / datastatus）
- `BigIntField` × 3（sourcedata / boid / sourcevid）
- `IntegerField` × 3（bitindex / srcindex / index）
- `CheckBoxField` × 2（issyspreset / iscurrentversion）
- 平台维护 × 7（CreaterField / ModifierField / UserField / CreateDateField / ModifyDateField / MasterIdField / BillStatusField × 2）

### ❌ 禁用 / 废弃
- `depcytype` (属地员工类别) → `scene_doc.json` L566 明确**标注"(废弃)"**，不要在新规则中引用
- `EmployeeField` → OpenAPI buildMeta 74 值枚举不支持（用 `BasedataField` + `basedataNumber` 替代）

---

## 十、OpenAPI 覆盖度（本场景实测）

| 操作 | OpenAPI 支持 | 本场景关键点 |
|---|---|---|
| buildMeta 建新字段 | ✅ | ISV 前缀 `{isv}_{semantic}`，参考 64 字段命名 |
| modifyMeta 改元数据 | ✅ | formId = `hbjm_jobhr`（不是内部 ID `/IJRHGRN5RVY`） |
| addRule 加规则 | ✅ | 但本场景当前 formRule / bizRule = 0（`_auto_rules.md`） |
| registerPlugin 注册插件 | ✅ | 参考 `JobHrSaveOp` 的 `OPERATION` 类型 |
| updateOperation 改操作 | ✅ | save 10 插件 / audit 4 插件必须先 get |
| 时序模型版本操作 | ✅ | newhisversion / revise / confirmchange 都可扩展 |
| HIES 导入模板 | ⚠️ 部分 | `importdata_hr` 走 `cusstartpage: hismodel_importstart`，插件注册在自定义页面 |

---

**📌 来源追溯**：
- 10 条硬规则：`knowledge/cosmic_realworld_traps/` + `platform/openapi_capability_map.md`
- 61 opKey 清单：`_auto_operations.md` 总览表 L10-L71
- 5 类反编译插件：`_auto_plugin_semantics.md` L13-L81
- 字段清单：`scene_doc.json` L31-L821（64 字段）
- 继承链：`_auto_inherit_chain.md` L12-L16（5 级）
