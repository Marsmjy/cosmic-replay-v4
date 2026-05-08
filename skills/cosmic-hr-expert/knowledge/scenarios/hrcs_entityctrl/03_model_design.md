# 模型设计 · 业务对象维度映射 (hrcs_entityctrl)

> **状态**: 🟢 基于 `scene_doc.json` (17 字段实抓) + `_auto_inherit_chain.md` + `_auto_plugin_semantics.md` (5 反编译类)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_entityctrl.md` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.dimension.*` + `kd.hr.hrcs.opplugin.web.perm.*`（2026-04-28）

---

## ⭐ 关键业务事实 · 一张主表 + 一张分录子表的"业务对象 → 维度 → 控权范围"映射

`hrcs_entityctrl`（业务对象维度映射）是 HR 通用服务（hrcs）域里的**权限控制核心配置表**。它回答这一个问题：**"业务对象 X 上的字段 Y 在做权限校验时 · 应该按哪个维度（dimension）来限制范围？"**

物理结构是 **1 张主表 + 1 张分录子表**：

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_hrcs_entityctrl` | 主实体 | 业务对象维度映射主体（绑业务对象 + 应用） | `fid` / `fentitytypeid` / `fappid` / `finitdatasource` / `fdescription` |
| `t_hrcs_entitydimentry` | 分录子表 | 字段-维度-控权范围映射明细（一个业务对象多个字段配多个维度） | `fentryid` / `fpropkey` / `fdimensionid` / `fauthrange` / `fismust` / `fissyspreset` / `fneedhisver` |

⚠️ **关键定律**：每条 `hrcs_entityctrl` 主记录 = 一个"业务对象"（如 `hrpi_person` / `haos_adminorgdetail` / `hbpm_position`）的全套维度映射配置。一个业务对象上有 **N 个字段** 需要控权 · 主记录就有 **N 行 entryentity 分录** · 每行配 1 个维度 + 1 种控权范围。

---

## 一、苍穹列表三层模型（参考管道坑 14.1）

本场景属于"基础资料类列表"形态。`hrcs_entityctrl` 列表页由**两到三层独立元数据**组成 · Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BillFormModel | `hrcs_entityctrl`（主实体 · 菜单直挂） | 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **TreeList 列表壳** | 动态表单（独立元数据） | ⚠ 待探针确认 · `EntityCtrlTreeListPlugin extends AbstractTreeListPlugin` 实证它确实挂在 TreeList 上 | TreeList UI 壳 + 树节点交互 + setFilter 数据过滤 |
| **F7 列表模板** | 动态表单（独立元数据） | `bos_listf7`（实证 `EntityCtrlEdit.beforeF7Select` L197 显式 setFormId） | F7 选业务对象时弹出的列表壳 |

**插件挂载职责分工**：

- **数据层过滤 / 权限 / setFilter** → 挂 `hrcs_entityctrl`（数据实体 · 当前 `EntityCtrlTreeListPlugin` 就挂这里 · 反编译实证 L79 `setFilter` 加 `entitytype.number is not null` 过滤）
- **TreeList UI 外壳 / 树节点动作** → 挂列表表单模板（待探针确认）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）+ `tablist`/`treelist` 三层模型。

---

## 二、继承链 · BillFormModel + 非时序模型

### ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "BillFormModel"`。这与 hjm/admin_org/dynascheme 的 HisModel 时序模型**不同** —— `hrcs_entityctrl` 是**纯配置型基础资料**（不含 audit/submit/confirmchange/HisModel 字段）· 类似于 `hrcs_dimension` / `hrcs_datarule` 这类权限配置基础资料。

### HisModel 实证（关键 · 不要套用时序场景模式）

按 `feedback_har_is_ground_truth.md` 铁律 · 必须 grep 实证：

```bash
grep -E "iscurrentversion|HisModel|boid" \
  knowledge/scenarios/hrcs_entityctrl/scene_doc.json \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_entityctrl/*.java
```

**实证结果（2026-04-28）**：
- `scene_doc.json` 中 **0 处** `iscurrentversion` / `HisModel` / `boid` 字段命中
- 反编译 5 类中 **仅 1 处** "boid" · 出现在 `EntityCtrlEdit.java` L231 字符串字面量 `HRStringUtils.equals(propKey, "boid")` —— 这是判断"被控权字段是不是 boid 这种特殊字段名"· **不是** 主表自身有 boid 字段
- 父类继承 `HRDataBaseEdit` / `AbstractTreeListPlugin` / `HRDataBaseOp` / `AbstractOperationServicePlugIn` —— **均非时序模型基类**

→ 结论：**`hrcs_entityctrl` 不是 HisModel 时序场景**。Claude 写 CS 时不要加 `iscurrentversion=true` 过滤、不要假设有 boid/sourcevid/bsed 字段、不要套用 dynascheme/jobhr/admin_org 的时序版本模式。**所有数据是"当时填什么是什么" · 改了直接改 · 改了 audit 也无效（没 audit）**。

### 字段层级分类

苍穹元数据字段分 4 层（与 admin_org/hjm/dynascheme 一致 · `scene_doc.json` `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `creator` / `modifier` / `createtime` / `modifytime` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | bos_basetpl + HR 父模板 | `description` / `initdatasource` / `t_hrcs_entitydimentry.issyspreset` | 🔒 / ⚠️ initdatasource/issyspreset 是平台维护 |
| **L2** 时序模型 | hbp_histimeseqtpl 父模板 | （无 · 本场景非 HisModel） | — |
| **L3** 业务字段 | hrcs_entityctrl 自身 | `entitytype` / `bizapp` / `entryentity.*` | ⚠️ 谨慎改（涉及业务规则） |

### 关键认知

- **没有 boid/iscurrentversion/sourcevid/bsed/bsled/hisversion/datastatus** —— 不要按时序模型查询
- **`issyspreset = true`** 表示"平台预置的维度映射"· 不能删除（实证 `EntityCtrlEdit.beforeDoOperation` L278-L280 + `EntityCtrlTreeListPlugin.beforeDoOperation` L97-L99 双层拦截）
- **`initdatasource`** 是数据来源（如 `import` / `manual`）· 平台/标品维护 · ISV 不要 setValue
- **`t_hrcs_entitydimentry.entryid`** 是分录主键（ID 由平台分配 · ISV 自建分录行用 `kd.bos.id.ID` · 见 PR-005）
- **`t_hrcs_entitydimentry.entryboid`** —— 本场景**没有**（与 dynascheme.roleentry 不同 · 因为本场景非 HisModel）

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 17 个字段）

### 3.1 主表业务核心字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| **`entitytype`** | **BasedataField** | **业务对象** ⭐ | **✅** | 🔒（isvCanModify=false） | → `bos_entityobject`（被映射的业务对象 · 如 `hrpi_person` / `haos_adminorgdetail`）F7 过滤见 § 3.5 |
| `bizapp` | BasedataField | 应用（自动从 entitytype 带出 · 见 `EntityCtrlEdit.bindAppCloud` L535-L538） | ❌ | ✅ | → `hbp_devportal_bizapp` · save 时 OP 自动带（实证 `EntityControlSaveOp.beginOperationTransaction` L85-L97） |
| `description` | MuliLangTextField | 描述 | ❌ | ✅ | 多语言 · 主表承载（无独立 `_l` 子表） |
| `initdatasource` | ComboField | 数据来源（`import` / `manual`） | ❌ | 🔒（isvCanModify=false） | 平台维护 · 标记此条记录是导入还是手填 |

### 3.2 系统派生字段（autoComputed · 不要手填）

`creator` / `modifier` / `createtime` / `modifytime` —— 平台/标品维护 · 手改破坏数据一致性。

### 3.3 分录子表 entryentity（核心业务表）

物理表 `t_hrcs_entitydimentry` · 主键 `fentryid`（无 entryboid · 非时序）。

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 |
|---|---|---|---|---|
| **`entryentity.propkey`** | **TextField** | **被控权字段 key** ⭐ | **✅** | 🔒（isvCanModify=false） |
| `entryentity.propname` | TextField | 字段显示名（前端缓存 · 不存库 / `physicalColumn = —`） | ❌ | ✅ | 由 `EntityCtrlEdit.beforeBindData` L160 + `EntityCtrlTreeListPlugin.afterQueryOfExport` L138 实时填充 |
| **`entryentity.dimension`** | **BasedataField** | **关联维度** ⭐ | **✅** | 🔒（isvCanModify=false） | → `hrcs_dimension` · F7 过滤见 § 3.5 |
| `entryentity.ismust` | CheckBoxField | 必选（"该字段必须要这个维度才能控权"） | ❌ | ✅ | save 时同步到 hrcs_roledimension（实证 `EntityControlSaveOp.endOperationTransaction` L99-L146） |
| `entryentity.issyspreset` | CheckBoxField | 系统预置（不能删除） | ❌ | 🔒 | `EntityCtrlEdit.beforeDoOperation` L278-L283 拦截 deleteentry |
| `entryentity.desc` | MuliLangTextField | 字段控权描述 | ❌ | ✅ | 多语言 · 在子表 t_hrcs_entitydimentry 自身承载 |
| **`entryentity.authrange`** | **ComboField** | **控权范围** ⭐ | **✅** | 🔒（isvCanModify=false） | 取值: `1`=精确控权 / `2`=粗粒度（实证 `EntityCtrlEdit.closedCallBack` L491 默认 `2`） |
| `entryentity.needhisver` | CheckBoxField | 是否需要历史版本 | ❌ | ✅ | 控制此字段值是否进入"维度变化历史"快照表 |

⚠️ **`propname` 是派生字段 · 不存库**：`scene_doc.json` 标 `physicalColumn = —`。所有保存写入路径都跳过此字段 · 它是 FormPlugin 在 `beforeBindData` 阶段从 `EntityCtrlServiceHelper.getEntityFieldMap` 实时拉取（实证 `EntityCtrlEdit.java` L152-L160 + L177）· **ISV 不要自建 OP 写 propname** —— 写了也会被前端 entityFieldMap 覆盖。

---

## 四、F7 过滤双闸（重要 · 决定可选范围）

### 4.1 业务对象 F7 过滤（`EntityCtrlEdit.bulidEntityFilters` L515-L533）

业务对象（`entitytype`）F7 不是显示所有 bos_entityobject · 而是经**4 道闸门**过滤：

```
闸 1：modeltype 限制
  modeltype IN ("BaseFormModel", "BillFormModel", "QueryListModel",
                "DynamicFormModel", "ReportFormModel")     // L530

闸 2：非模板（非父模板）
  istemplate = "0"                                          // L531

闸 3：剔除已配置的（每个对象只能配一次）
  排除 hrcs_entityctrl 表中已存在的 entitytype.number       // L518
  + 排除硬编码 "bos_org" / "hbss_hrbu"                       // L519-L520

闸 4：HR 域 + 有权限项
  通过 EntityCtrlServiceHelper.queryEntityForBidInfo
   + RoleManageService.getNoCtrlPermEntitysFromCache
   + EntityCtrlServiceHelper.getAllHrHasPermItemEntity      // L521-L527
```

→ Claude 加自定义业务对象时 · 必须在 `EntityCtrlServiceHelper.getAllHrHasPermItemEntity` 命中（即被某 hrcs 角色配过权限项）· 否则在 F7 看不到。

### 4.2 维度 F7 过滤（`EntityCtrlEdit.beforeF7Select` L197-L237）

维度（`entryentity.dimension`）F7 由"被控字段 propkey 类型"决定可选维度集 · 走 6 个分支（详见反编译 L207-L235）：

| 字段类型识别 | 过滤逻辑 | 实证位置 |
|---|---|---|
| 被控字段是 BasedataField + 非组织 | `datasource = "basedata"` AND `entitytype = bdPropInfos.get(propKey)` | L228 |
| 被控字段是组织字段（OrgProp） + 有 buCaFunc | `datasource = "hrbu"` AND `hrbu = buId` | L211-L212 |
| 被控字段是组织字段 · `buId == 1`（特殊全员组） | `datasource = "orgteam"` AND `org_classify.fbasedataid IN [1010, 1020]` | L213-L215 |
| 被控字段经 IHAOSStructProjectService 命中 otclassify | `org_classify.fbasedataid IN otclassifyIds`（含 1010/1020 时再 OR otBdFilter） | L222-L226 |
| 被控字段是普通业务字段（非 ID/boid/`.id` 结尾） | `datasource != "orgteam"` | L231-L232 |
| 被控字段是 ID/boid/`.id` 结尾 | filter = null（不过滤 · 但加 hrbu 默认） | L231 |

⚠️ **`buId == Integer.MAX_VALUE` 的特殊语义**：表示"该字段不限管理域" · 直接走 `datasource = "hrbu"` 不加 hrbu 等于过滤 · 任何 hrbu 维度都可选（实证 L211-L218）。

⚠️ **otBdFilter 兜底**：当 otclassify 含 1010/1020 时 · 加 OR 条件允许 datasource=basedata + 4 个标准 entitytype（`haos_adminorghrf7` × 2 / `haos_projectteamhr` / `haos_adminorgdetail`）· 即"组织字段也可以选业务基础资料维度" L206。

---

## 五、save / delete 业务联动（关键 · ISV 扩展前必读）

### 5.1 save 联动（`EntityControlSaveOp.endOperationTransaction` L99-L146）

save 后端 OP（`opKey=save`）按以下顺序执行 · 任何 ISV 自建联动都要避开它：

```
1. beginOperationTransaction (L85-L97)
   → 如 importtype != "override" · 自动从 entitytype.bizappid 带 bizapp 字段

2. endOperationTransaction (L99-L146) ⭐
   → 收集所有 ismust=true 的分录 · 调 syncMustDimToRoleDim 同步到 hrcs_roledimension
   → 处理删除的分录（propDimInfo vs originPropDimInfo 差集）调 deleteRowsPostProcessing
     · 自动同步删除 hrcs_roledimension.entry 中对应的 entitytype + propkey 行
   → 调 EntityCtrlLogService.resolveLog 落操作日志
   → 调 HRPermCacheMgr.clearAllCache 清权限缓存

3. afterDoOperation (FormPlugin · EntityCtrlEdit L292-L298)
   → 再次 HRPermCacheMgr.clearAllCache（双重保险）
```

⚠️ **propDimInfo / originPropDimInfo 由 FormPlugin 提前传**：`EntityCtrlEdit.beforeDoOperation` L268-L271 把分录的 propkey→dimensionId map 序列化到 `formOperate.getOption().setVariableValue("propDimInfo", ...)` · OP 端 `endOperationTransaction` 反序列化用 —— **ISV 列表批量 save / 接口直推保存** 时这两个变量为空 · 删除联动会被跳过 · 须自建逻辑兜底。

### 5.2 delete 联动（双层）

**前端拦截**（`EntityCtrlEdit.beforeDoOperation` L272-L289）：
- `deleteentry`（删行）操作前 · 检查选中行的 `issyspreset` · 命中则 setCancel + 错误提示 "预置数据无法删除" L280
- 弹"删除后角色下的业务对象将不再参与控权 · 删除维度控权将影响角色所有成员 · 确认删除吗？"二次确认 L286

**列表批量 delete 拦截**（`EntityCtrlTreeListPlugin.beforeDoOperation` L89-L101）：
- 调 `EntityCtrlServiceHelper.beforeDelOp(ids, entryIds, OperateOption)` 检查
- 不通过则 setCancel + 错误提示 "预置数据无法删除" L98

**后端联动**（`EntityCtrlDelOp.beginOperationTransaction` L47-L70）：
- 从 OperateOption 读 `toDelDimRoleRanges`（FormPlugin/列表事先填的"要删的角色范围"map · 格式 `roleId|entityId|propKey|dimId`）
- 逐条调 `EntityCtrlServiceHelper.deleteRoleRange` 把 hrcs_roledimension 表对应行删掉
- 读 `logInfos` 调 `EntityCtrlLogService.resolveLog` 落删除日志

---

## 六、共用物理表分析（区分键）

`hrcs_entityctrl` **不与其他 form 共用主物理表 `t_hrcs_entityctrl`**（标品里没看到平行 form 共表写入此表的场景）。但它**通过 entitytype + propkey 跟下游表强耦合**：

| 下游表 | 关系 | 区分键 |
|---|---|---|
| `hrcs_roledimension` | 角色维度配置（动态推算）· 角色绑定的"业务对象-字段-维度-范围" | `entry.entitytype` + `entry.propkey` 双键定位 |
| `hrcs_dynaformctrl` | 虚字段数据控权配置（仅当业务对象是 dynamic/virtual 实体时） | `entitytype = entitytype.number` · 实证 `EntityCtrlEdit.putDynaFormCtrlInfo` L370 |
| `hrcs_dimension` | 维度基础资料（被引用方） | `entryentity.dimension` 外键 |
| `bos_entityobject` | 业务对象基础资料（被引用方） | `entitytype` 外键 |
| `hbp_devportal_bizapp` | 应用基础资料（被引用方） | `bizapp` 外键 |

⚠️ **save 联动写 hrcs_roledimension**：实证 `EntityControlSaveOp.syncMustDimToRoleDim` L187-L259 + `deleteRowsPostProcessing` L148-L178。这是为什么 save 操作日志会出现"修改了 N 条角色维度"—— 一条 entityctrl 行的 ismust 变更会扩散到所有相关角色。

---

## 七、Plugin 链概览（12 标品 + ISV 未挂）

完整 12 plugin 清单见 `_auto_plugin_registry.md`。这里只列对 ISV 扩展最关键的层次：

```
preOpenForm  (4 个)
  2. HRCertCheckEdit           (HR 证书检查 · 标品不可继承)
  3. HRBaseUeEdit              (HR UE 模板 · 标品不可继承)
  5. HRAdminStrictPlugin       ⭐ HR 领域管理员准入闸 · 非 HR 管理员直接拒（hrcs 11+ 表单共用）
  7. HRCertCheckList           (HR 证书检查列表)

beforeBindData (1 个)
  6. EntityCtrlEdit            ⭐ 主 FormPlugin · 计算 entityType F7 过滤 · 装载 propname / orgInfos / bdPropInfos

afterBindData (2 个)
  4. HRHiesButtonSwitchPlugin  (HIES 按钮切换 · 标品)
  6. EntityCtrlEdit            ⭐ 缓存 originPropDimInfo / changedMustDim / beforeOpData 用于 save 阶段比对

beforeDoOperation (2 个)
  6. EntityCtrlEdit            ⭐ save / deleteentry 前置校验 · 设 propDimInfo / originPropDimInfo 给 OP
  8. EntityCtrlTreeListPlugin  ⭐ TreeList 端 delete 前置（issyspreset 拦截）

afterDoOperation (2 个)
  6. EntityCtrlEdit            ⭐ save 成功后清缓存 HRPermCacheMgr.clearAllCache
  8. EntityCtrlTreeListPlugin  ⭐ delete 成功后清缓存 HRPermCacheMgr.clearAllCache

beforeExecuteOperationTransaction (2 个)
  9. HRBaseDataLogOp           ⭐ HR 基础资料操作日志
  11. HRDataBaseOp             ⭐ HR OP 父类（不会被列出来 · 实际是父类 · 这里 plugins.json 显式注册）

onAddValidators (1 个)
  10. EntityControlSaveOp      ⭐ 主 OP · 注册 EntityControlSaveValidator

beginOperationTransaction (隐式 · 反编译实证)
  10. EntityControlSaveOp      save · 自动带 bizapp
  12. EntityCtrlDelOp          delete · 调 EntityCtrlServiceHelper.deleteRoleRange + 落日志

endOperationTransaction (隐式 · 反编译实证)
  10. EntityControlSaveOp      save · 同步 hrcs_roledimension + 落日志 + 清缓存

afterExecuteOperationTransaction (1 个)
  9. HRBaseDataLogOp           HR 基础资料操作日志（事务后）
```

---

## 八、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/hjm/hbpm/dynascheme）保持一致

### 8.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。`hrcs_entityctrl` 的 MuliLangTextField 字段（`description` / `entryentity.desc`）目前**在主表/子表自身承载多语言**（无独立 `_l` 表 · 这是标品配置 · 不是默认行为）。**ISV 扩展 MuliLangTextField 字段时不要假设会自动有 _l 表**。

### 8.2 反模式 · 继承场景专属类

| 场景专属类（**禁继承**） | 推荐做法 |
|---|---|
| `EntityCtrlEdit` | 并列挂新 FormPlugin · 继承 `HRDataBaseEdit` |
| `EntityCtrlTreeListPlugin` | 并列挂新 ListPlugin · 继承 `HRDataBaseList`（或 `AbstractTreeListPlugin` · TreeList 必须） |
| `EntityControlSaveOp` | 并列挂新 OP · 继承 `HRDataBaseOp`（不要继承本 OP · 它做了大量 save 联动 · 改了直接挂） |
| `EntityCtrlDelOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `EntityControlSaveValidator` | 并列加新 Validator · 继承 `AbstractValidator` |
| `HisModelFormCommonPlugin` / `HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisModelListCommonPlugin` | @SdkInternal 平台时序内部类 · 本场景**不挂这些** · ISV 任何场景都不得继承 |
| `HRAdminStrictPlugin` | hrcs 11+ 表单共用准入闸 · ISV 不要继承 · 直接复用即可 |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs）· 不在白名单 |

`EntityCtrlXxxOp / EntityCtrlXxxPlugin` 都是 hrcs 场景专属类 · ISV 不要继承 · 走并列挂或继承 SDK 白名单父类（参考 `_shared/platform_rules.json` PR-001）。

### 8.3 HisModel 时序场景 · 本场景不适用

`hrcs_entityctrl` 是 BillFormModel **非时序**配置基础资料：
- ❌ **不要** 用 `iscurrentversion=true` 过滤（本场景没此字段）
- ❌ **不要** 假设有 boid / sourcevid / bsed / bsled / hisversion / datastatus 字段
- ❌ **不要** 写"按 boid 反查下游"逻辑 —— 本场景所有引用都用 entitytype + propkey 双键
- ✅ 改了就是改了 · 没有版本控制 · 没有 audit/confirmchange · 也没 submit/unaudit

参考 `_shared/platform_rules.json` PR-008 / PR-009 · 本场景**不适用**。

### 8.4 列表三层模型（tablist|treelist）

`hrcs_entityctrl` 是 BillFormModel + **TreeList 列表壳**（`EntityCtrlTreeListPlugin extends AbstractTreeListPlugin` 实证）。Claude 做列表类 CS（如 setFilter / 自定义按钮）时 · 看反编译 `EntityCtrlTreeListPlugin` —— 这是 TreeList 数据层 · 不是普通 List。如果要做 UI 外壳定制 · 需要找 TreeList 表单模板（待探针确认 form 是否独立）。

⚠️ **TreeList 与普通 List 的差异**：
- TreeList 必须继承 `AbstractTreeListPlugin`（不是 `AbstractListPlugin`）
- TreeList 有树节点点击事件 · 但本场景的 TreeList 实现里只用了 setFilter / beforeDoOperation / afterDoOperation / afterQueryOfExport 这 4 个非树特有方法
- TreeList 导出走 `afterQueryOfExport` 钩子 · 标品 `EntityCtrlTreeListPlugin.afterQueryOfExport` L112-L144 在导出时把 propname 实时填入分录（缓存到 entityFieldNameMap）· ISV 想改导出列内容必须在此挂

### 8.5 PR 引用速查

本场景定制必引：
- **PR-001** 并列挂不继承 · 是核心铁律
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环（本场景 `EntityCtrlEdit.propertyChanged` L336-L365 改 entitytype 时会 deleteEntryData · 要小心二次触发）
- **PR-005** ID 生成用 kd.bos.id.ID（本场景 OP 没用 · 但 ISV 自建分录行必须用）
- **PR-007** 预置数据（issyspreset）不可改 · 业务自建可改（本场景子表 issyspreset 是预置标记 · 不能删）
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验
- **PR-011** BEC 走平台事件中心（**entityctrl 标品没发 BEC** · ISV 自建发布方时引用 · 实证见 § 九）

### 8.6 BEC 实证 · 标品没发

按 `feedback_bec_3layer_async_publish.md` 铁律 · grep 反编译产物 + Service + 后台 Task：

```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hrcs_entityctrl/
```

**实证结果（2026-04-28）**：
```
（0 处命中 · 标品没发任何 BEC 事件）
```

→ 已扫 OP（EntityControlSaveOp / EntityCtrlDelOp）+ FormPlugin（EntityCtrlEdit / EntityCtrlTreeListPlugin / HRAdminStrictPlugin）三层 · 都没找到 BEC。

→ 结论：**hrcs_entityctrl 标品没发 BEC** · ISV 必须自建发布方（如果业务真有需要把维度映射变更通知下游系统）。这与 hjm（3 层异步发布）不同 · 与 dynascheme（标品没发）/ haos_structure（标品没发）属于同一类。

---

## 九、模型层对外 API（ServiceHelper 反编译实证）

| ServiceHelper | 关键方法 | 用途 |
|---|---|---|
| `EntityCtrlServiceHelper` | `isDynOrVir(entityType)` / `getEntityFieldMap(entityType)` / `getAllNoExistEntityCtrlNumbers()` / `queryEntityForBidInfo(set, ..., ...)` / `getHRApps(false)` / `getAllHrHasPermItemEntity(view, validApps)` / `beforeDelOp(ids, entryIds, option)` / `deleteRoleRange(roleDimHelper, dimId, entityNum, propKey, roleId, list)` | 主业务 helper · EntityCtrlEdit/TreeListPlugin/SaveOp/DelOp 都依赖 |
| `ChoiceFieldPageCustomQueryService` | `parsePropertySub(entityType, _, queryCondition, "1=1", noDBProps)` | 解析业务对象的所有字段 · 返回 `Map<field_id, field_name>` |
| `EntityOrgFieldBuQueryService` | `getPropBuMap(entityNumber)` | 解析业务对象上的组织字段 · 返回 `Map<propKey, buId>` |
| `HRPermCacheMgr` | `clearAllCache()` | 清空 HR 权限缓存（save / delete 后必调） |
| `RoleManageService` | `getNoCtrlPermEntitysFromCache()` | 不参与控权的实体集合（F7 过滤用） |
| `HRBuCaServiceHelper` | `getBuCaFuncFromSpec(appEntityMap)` | 按应用分组查 buCaFunc id |
| `EntityCtrlLogService` | `resolveLog(entityCtrlLogInfos)` | 落 entityctrl 操作日志 |
| `HRMServiceHelper` | `invokeHRMPService("haos", "IHAOSStructProjectService", "queryStructProConfig", ...)` | 跨模块调 HAOS 查"业务对象组织结构项目配置" |
| `HRBaseServiceHelper` | `new HRBaseServiceHelper("hrcs_entityctrl")` / `new HRBaseServiceHelper("hrcs_dynaformctrl")` / `new HRBaseServiceHelper("hrcs_roledimension")` / `new HRBaseServiceHelper("bos_objecttype")` | HR 基础资料服务 helper（核心 ServiceHelper · `extends` 走 SDK 白名单） |

⚠️ ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。`EntityCtrlServiceHelper / HRPermCacheMgr / EntityCtrlLogService / RoleManageService` 都是 **hrcs 业务包** · 默认不带 SDK 注解 · ISV 调用前必须确认白名单（详见 `curated_sdk.json` `hrcs` 分类）。

---

## 十、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加自定义业务字段（如"映射备注分类"） | modifyMeta add field 到主实体 hrcs_entityctrl · ISV 前缀 | 低 |
| 加分录字段（在 entryentity 加"控权失效日期"） | modifyMeta add field 到子实体 entryentity · ISV 前缀 | 中（注意不是 HisModel · 没有 entryboid · 分录唯一索引按 entryid 走） |
| 监听 save 后做下游同步（如刷新本地缓存） | ISV 自建 OP 挂 save 的 afterExecuteOperationTransaction（PR-010） · 不要跟标品的 endOperationTransaction 混 | 中 |
| save 前业务校验（如"propkey 不能重复"） | ISV 自建 Validator 挂 onAddValidators | 低 |
| 列表 setFilter 增加权限过滤 | ISV 自建 ListPlugin 挂 hrcs_entityctrl 列表（继承 `AbstractTreeListPlugin` · 注意是 TreeList） | 低 |
| 共用 HRAdminStrictPlugin 准入闸 | 配置即可 · 不要修改 | 0 |
| 自定义 dimension F7 过滤 | ISV 自建 FormPlugin 挂同 form · 在 registerListener 加 BeforeF7SelectListener · 自己的 filter and 进 `EntityCtrlEdit.beforeF7Select` 已加的 filter | 中（要避免覆盖标品 6 分支逻辑） |

详见 `06_customization_solutions.md`。
