# 模型设计 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于 `scene_doc.json` (56 字段实抓) + `_auto_inherit_chain.md` + `_auto_plugin_semantics.md` (7 反编译类)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_dynascheme.md` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.dyna.*` (2026-04-28)

---

## ⭐ 关键业务事实 · 一张主表 + 9 张子表的"分录矩阵"

`hrcs_dynascheme` 是 HR 通用服务（hrcs）域里**最复杂的多分录基础资料场景之一**。它不是单表 + 单分录的常规基础资料，而是 **1 张主表 + 6 个分录子表 + 4 个 MulBasedataField 隐式中间表** 共 10+ 张物理表协同。所有分录都受**时序版本控制（HisModel）**驱动，每个分录行带 `entryboid` 跟踪业务版本。

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_hrcs_dynascheme` | 主实体 | 动态授权方案主体（含编码/名称/授权动作/规则条件） | `fid` / `fboid` / `fsourcevid` / `fiscurrentversion` / `fcondition` / `fauthaction` / `fadmingroupid` |
| `t_hrcs_dynaschasgnactent` | 分录子表 | 方案分配操作分录（assignactionentry · assigndays/assignpersonitem/assignactype） | `fentryid` / `fentryboid` / `fassignpersonitemid` / `fassignactypeid` |
| `t_hrcs_dynaschcclactent` | 分录子表 | 方案取消操作分录（cancelactionentry · cancelpersonitem/cancelactype） | `fentryid` / `fentryboid` / `fcancelpersonitemid` / `fcancelactypeid` |
| `t_hrcs_dynaschemerole` | 分录子表 | 角色清单（roleentry · 多角色绑定到方案 · 含 customenable/custominfo 自定义范围属性） | `fentryid` / `fentryboid` / `froleid` / `fcustomenable` / `fcustominfo` |
| `t_hrcs_dynasearchparam` | 反查子表 | 方案搜索规则参数（resolveRuleConfigToSearch 落库 · 用于反查时用了哪些参数） | `fparamid` |
| `t_hrcs_dynasearchadminorg` | 反查子表 | 方案搜索行政组织（条件参数命中的 admingroup） | `fadminorgid` |
| `t_hrcs_dynasearchpos` | 反查子表 | 方案搜索岗位 | `fposid` |
| `t_hrcs_dynasearchjob` | 反查子表 | 方案搜索职位（HisModel 多基础资料子表） | `fjobid` |
| `t_hrcs_dynasearchactiona` | 反查子表 | 方案搜索分配变动类型 | `factypeid` |
| `t_hrcs_dynasearchactionc` | 反查子表 | 方案搜索取消变动类型 | `factypeid` |

⚠️ **`t_hrcs_dynasearch*` 6 张反查表的灌库时机**：在 `DynaAuthSchemePlugin.beforeDoOperation` 走 save / submit / confirmchange 时调用 `DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch` + `resolveSceneToSearch` 一次性写入（源码 `DynaAuthSchemePlugin.java` L457-L459）。这是为了用方案查"哪些 admingroup / 岗位 / 职位 / 变动类型 命中此方案"做反向索引而服务，不是用户填的字段。**ISV 不要直接 setValue 这些 search_* 字段** —— 它们是平台维护的派生数据。

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"基础资料类列表"形态。`hrcs_dynascheme` 列表页由**两到三层独立元数据**组成，Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | BillFormModel | `hrcs_dynascheme`（主实体 · 菜单直挂） | 查哪张表 / 有哪些字段 / 数据层业务逻辑 |
| **列表表单模板** | 动态表单（独立元数据） | ⚠ 待探针确认（推测 `hrcs_dynaschemelist` 类） | 列表 UI 壳 + UI 层动作按钮（setadminrange / assignrecord / audithisconfirmchange） |
| **F7 列表模板** | 动态表单（独立元数据） | ⚠ 待探针确认 · 业务上动态授权方案不常作为基础资料被引用 | F7 选择时的列表壳（如有） |

**插件挂载职责分工**：
- **数据层过滤 / 权限 / setFilter** → 挂 `hrcs_dynascheme`（数据实体 · 当前 `DynaAuthSchemeListPlugin` 就挂这里）
- **列表外壳 / setadminrange 动作 / 历史变更确认按钮 UI** → 挂列表表单模板（待探）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`） + `tablist`/`treelist` 三层模型。

---

## 二、继承链 · BillFormModel + 时序模型

### ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "BillFormModel"`。这与 hjm/admin_org 的 BaseFormModel 不同 —— 动态授权方案是**单据**形态（含 `submit/audit/unaudit/confirmchange` 工作流），不是纯基础资料。

### 字段层级分类

苍穹元数据字段分 4 层（与 admin_org/hjm 一致 · scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** 业务通用 | bos_basetpl + HR 父模板 | `number` / `name` / `simplename` / `description` / `status` / `enable` / `index` / `issyspreset` / `disabler` / `disabledate` / `initdatasource` / `orinumber` / `oristatus` / `oriname` | 🔒 / ⚠️ 多数不改 · 业务自建可改 |
| **L2** 时序模型 | hbp_histimeseqtpl 父模板 | `boid` / `iscurrentversion` / `datastatus` / `sourcevid` / `firstbsed` / `bsed` / `bsled` / `hisversion` | 🔒 不改（HisModel 依赖） |
| **L3** 业务字段 | hrcs_dynascheme 自身 | `admingroup` / `condition` / `authaction` / `assigndays` / `assigndesc` / `canceldesc` / `ruledescription` / `assignactionentry` / `cancelactionentry` / `roleentry` / `search_*` | ⚠️ 谨慎改（涉及业务规则） |

### 关键认知

- `boid` = 业务对象 id · 跨所有版本不变（**PR-009** 业务维度）
- `id` = 具体版本 id · 每次审核/变更产生新 id（**PR-009** 版本维度）
- `iscurrentversion = true` 表示当前生效版本（**PR-008** 时序当前版本过滤铁律）
- 所有 4 个分录子表都带 `entryboid` 跟踪 entry 行业务版本（与主表 boid 配合 · 一行 entry 在多个版本里共用同 entryboid）

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 56 个字段）

### 3.1 主表业务核心字段

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `number` | TextField | 编码 | ❌ | ✅ | CodeRuleOp 自动生成（PR-006） |
| `name` | MuliLangTextField | 名称 | ❌ | ✅ | 多语言 · 主表承载（无 _l 表） |
| `simplename` | MuliLangTextField | 简称 | ❌ | ✅ | - |
| `description` | MuliLangTextField | 规则说明 | ❌ | ✅ | - |
| `status` | BillStatusField | 数据状态（A 暂存 / B 已提交 / C 已审核） | ❌ | ⚠️ 黄区 | 工作流驱动 |
| `enable` | BillStatusField | 使用状态（0 / 1 / 10） | ❌ | ⚠️ 黄区 | enable / disable 操作管理 |
| `index` | IntegerField | 排序号 | ❌ | ✅ | - |
| **`admingroup`** | **BasedataField** | **所属管理员组** ⭐ | **✅** | 🔒（isvCanModify=false） | → `perm_admingroup`（domain=hr · isdomain=1） |

### 3.2 时序版本字段（HisModel · 不可改）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `boid` | BigIntField | **业务 ID**（跨版本不变 · 下游引用必用） |
| `iscurrentversion` | CheckBoxField | 是否当前生效版本（PR-008 过滤必带） |
| `datastatus` | ComboField | 数据版本状态 |
| `sourcevid` | BigIntField | 关联源版本 id（变更场景 · 指向上一个生效版本） |
| `firstbsed` | DateField | 最早生效日期 |
| `bsed` | DateField | 当前版本生效日期 |
| `bsled` | DateField | 当前版本失效日期 |
| `hisversion` | TextField | 版本号（V1/V2/...） |
| `changedescription` | TextField | 变更说明（confirmchange 时填） |

### 3.3 业务规则字段

| Field Key | 类型 | 业务含义 | 备注 |
|---|---|---|---|
| **`authaction`** | **ComboField** | **授权动作** ⭐ | **必填** · 取值：`1`=分配 / `2`=取消 / `3`=分配并取消（无规则时 `3` 必须设条件 · 见 `DynaAuthSchemePlugin.java` L441-L444） |
| `condition` | LargeTextField | 规则条件（DecisionSet JSON 格式 · 由 PermFilter 控件产出） | save/submit/confirmchange 前由 `PermRuleValidateUtil.validCondition` 校验（L432） |
| `ruledescription` | MuliLangTextField | 规则摘要（`RulePreviewUtil.getConditionPreviewMulLang` 自动生成） | - |
| `assigndays` | IntegerField | 分配天数（authaction=1 时是否必填取决于 `PermCommonUtil.isEnableValidateTime()`） | 见 `setAssignRequiredField` L740-L751 |
| `assigndesc` | MuliLangTextField | 分配文案 | - |
| `canceldesc` | MuliLangTextField | 取消文案 | - |

### 3.4 反查派生字段（平台维护 · ISV 不要 setValue）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `search_param` | MulBasedataField | 方案搜索规则参数（resolveRuleConfigToSearch 灌库） |
| `search_adminorg` | HRMulAdminOrgField | 方案搜索行政组织 |
| `search_pos` | HRMulPositionField | 方案搜索岗位 |
| `search_job` | MulHisModelBasedataField | 方案搜索职位 |
| `search_assignaction` | MulBasedataField | 方案搜索分配变动类型 |
| `search_cancelaction` | MulBasedataField | 方案搜索取消变动类型 |

### 3.5 系统派生字段（autoComputed · 不要手填）

`creator` / `modifier` / `createtime` / `modifytime` / `masterid` / `disabler` / `disabledate` / `issyspreset` / `initdatasource` / `orinumber` / `oristatus` / `oriname` —— 平台/标品维护 · 手改破坏数据一致性。

---

## 四、3 个分录子表（核心业务表）

### 4.1 assignactionentry · 方案分配操作分录

物理表 `t_hrcs_dynaschasgnactent` · 主键 `fentryid`，业务版本键 `fentryboid`。

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `t_hrcs_dynaschasgnactent.entryboidassign` | BigIntField | 分录行业务 id（跨版本不变） |
| `t_hrcs_dynaschasgnactent.assignpersonitem` | BasedataField | 人员（→ `hrcs_dynaauthobject`） |
| `t_hrcs_dynaschasgnactent.assignactype` | BasedataField | 变动类型（→ `hpfs_chgcategory`） |

**关键约束**（`DynaAuthSchemePlugin.beforeF7Select` L337-L344）：
- F7 选 `assignactype` 时 · 已用过的 eventId（同分录已选过的变动类型）会被剔除（避免一个方案重复挂同一类型）
- 候选范围 = `DynaPremEvtSubService.queryChgRecordEffEventIds()` 全集 - 当前分录已选

**特殊情况**（`isOnly1010()` L557-L564）：当 `hrcs_dynaauthobject` 全表只有 id=1010L 一条记录时 · `afterF7Select` 多选会被认为合法（不清空 personitem · L771）

### 4.2 cancelactionentry · 方案取消操作分录

物理表 `t_hrcs_dynaschcclactent` · 与 assignactionentry 字段结构对称。

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `t_hrcs_dynaschcclactent.entryboidcancel` | BigIntField | 分录行业务 id |
| `t_hrcs_dynaschcclactent.cancelpersonitem` | BasedataField | 人员（→ `hrcs_dynaauthobject`） |
| `t_hrcs_dynaschcclactent.cancelactype` | BasedataField | 变动类型（→ `hpfs_chgcategory`） |

⚠️ **删除联动**（`DynaAuthSchemePlugin.beforeDoOperation` L450-L455）：
- 当 `authaction = 1`（仅分配）时 · save 前自动清空 cancelactionentry 分录（`deleteEntryData`）
- 当 `authaction = 2`（仅取消）时 · save 前自动清空 assignactionentry 分录 + `assigndays = 0`

### 4.3 roleentry · 角色清单 ⭐ 最复杂

物理表 `t_hrcs_dynaschemerole` · 一个方案绑定多个角色 · 每个角色还能定制成员范围属性。

| Field Key | 类型 | 业务含义 | 必填 |
|---|---|---|---|
| `t_hrcs_dynaschemerole.role` | BasedataField | 角色名称（→ `perm_role`） | ✅ |
| `t_hrcs_dynaschemerole.hrcsrole` | BasedataField | 中台角色（→ `hrcs_role` · 由角色编号反查 RoleDBServiceHelper 装载） | ❌ |
| `t_hrcs_dynaschemerole.customenable` | ComboField | 角色成员范围属性（数据范围属性 · 取值由 hrcs 角色配置决定） | ✅ |
| `t_hrcs_dynaschemerole.roleremark` | MuliLangTextField | 角色描述 | ❌ |
| `t_hrcs_dynaschemerole.custominfo` | LargeTextField | 自定义信息（DynaSchemeRoleAssignDetailBean 的 JSON 序列化 · 存 dataProperty/remark/roleId/dynaSchemeId/entryId） | ❌ |
| `t_hrcs_dynaschemerole.entryboidrole` | BigIntField | 分录行业务 id（跨版本不变） |

**关键设计** —— roleentry 不是普通分录：
- `customenable` + `custominfo` 是一对组合字段：customenable 选了"自定义"后 · custominfo JSON 里存详细范围（dataProperty）
- F7 添加角色按钮（`addrole` opKey）会先校验主表 `name` 不为空（L540-L544）· 否则提示"请先录入名称"
- 然后调 `HRRolePermHelper.showRoleF7` 弹出角色选择 F7 · 选完跳到 `hrcs_dyscassignroledetail` 子页面填范围（L660-L671）
- 子页面回填走 `closedCallBack(roleDetails)` · 把整段 `DynaSchemeRoleAssignDetailBean` JSON 写回 custominfo 字段（L619-L654）
- 复制方案场景下 · 调 `ORM.genLongIds("hrcs_dynascheme.roleentry", size)` 给每行 entry 重新分配 entryId（L253-L258 · **PR-005 ID 生成实证**）

---

## 五、HisModel 时序场景三套路（dynascheme 重头）

### 5.1 主表 boid / id / sourcevid 三角关系

```
方案 V1 创建：    boid=A id=A          iscurrentversion=true sourcevid=0
        ↓ 审核通过 + 后续 confirmchange
方案 V2：         boid=A id=B          iscurrentversion=true sourcevid=A   （V1 由 true → false）
方案 V3：         boid=A id=C          iscurrentversion=true sourcevid=B   （V2 由 true → false）
```

定律：当一行的 `boid == id` 时 · 它就是当前版本。

### 5.2 分录子表 entryboid 联动

每行 entry 也独立有 entryboid 跟踪："谁是这行 entry 在新版本里的对应行"。`DynaAuthSchemeAuditOp.endOperationTransaction` 走 `DynaRoleDetailServiceHelper.genVersionRoleEntryColl(sourceVid, currentRoleEntryColl)` 重建 entryId 映射 · 以保证 V1.entryX → V2.entryX' 的 entryboid 一致（源码 `DynaAuthSchemeAuditOp.java` L29-L41）。

### 5.3 confirmchange · 业务变更双写

dynascheme 特有的 confirmchange opKey 表示"业务变更"：用户改了已审核方案 · 需要确认变更后才落库。`DynaAuthSchemeConfirmChangeOp.endOperationTransaction` 同时写入两条记录（源码 `DynaAuthSchemeConfirmChangeOp.java` L25-L40）：

```java
long boId = dyo.getLong("boid");                              // 业务维度
long bgVid = dyo.getLong("id");                               // 新版本 id
DynamicObjectCollection versionRuleColl = dyo.getDynamicObjectCollection("roleentry");

// 1. 先写新版本的 roleentry
DynaSchemeRoleAssignServiceHelper.saveRoleEntry(bgVid, versionRuleColl);

// 2. 再用 boId 重映射 entryId · 落业务对象自身的 roleentry
DynamicObjectCollection boRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl(boId, versionRuleColl);
DynaSchemeRoleAssignServiceHelper.saveRoleEntry(boId, boRoleEntryColl);
```

⚠️ confirmchange 在列表上不支持（源码 L29-L31 抛 `KDBizException("not support")`）—— 这是**列表 list_op=1 时不允许 confirmchange**的硬约束。

---

## 六、共用物理表分析（区分键）

`hrcs_dynascheme` **不与其他 form 共用主物理表 `t_hrcs_dynascheme`**（标品里没看到平行 form 共表写入此表的场景）。但它**通过 boid 跟下列下游表强耦合**：

| 下游表 | 关系 | 区分键 |
|---|---|---|
| `hrcs_userrolerelat` | 用户-角色关联表（动态授权方案分配出来的角色绑定结果落这里） | `sourcetype = "4"` 区分"由动态方案分配"（assignrecord opKey 实证 · `DynaAuthSchemeListPlugin.java` L154） |
| `hrcs_dynaschemerange` | 方案权限范围（按 admingroup 划定） | `scheme = boid` |
| `hrcs_dynaschorg` | 方案组织 | `scheme = boid` |
| `hrcs_dynaschdimgrp` | 方案维度组 | `scheme = boid` |
| `hrcs_dynaschdatarule` | 方案数据规则 | `scheme = boid` |
| `hrcs_dynaschfield` | 方案字段 | `scheme = boid` |
| `hrcs_dynaschemerole` | 主表分录（roleentry） | `entryboid` 跨版本一致 |

⚠️ **delete 联动清理**（`DynaAuthSchemeListPlugin.afterDoOperation` L168-L183）：删除一个方案后，平台**自动级联清理** 5 张下游配置表。这是为什么 `delete` opKey 比 `disable` 严格的多 —— ISV 做删除前置校验（CS-04）必须考虑这套链路。

---

## 七、Plugin 链概览（22 标品 + ISV 未挂）

完整 33 plugin 清单见 `_auto_plugin_registry.md`。这里只列对 ISV 扩展最关键的层次：

```
preOpenForm  (3 个)
  3. HRBaseDataTplEdit         (HRDataBaseEdit)
  8. HRAdminStrictPlugin       ⭐ HR 领域管理员准入闸 · 非 HR 管理员直接拒
  12. HRBaseDataTplList

beforeBindData (9 个)
  6. HisModelFormCommonPlugin  ⭐ 时序公共 · @SdkInternal 不可继承
  7. DynaAuthSchemePlugin      ⭐ 主 FormPlugin · 业务核心
  9. HRCustomControlPlugin
  ...

beforeDoOperation (7 个)
  3. HRBaseDataTplEdit
  6. HisModelFormCommonPlugin
  7. DynaAuthSchemePlugin      ⭐ save/submit/confirmchange 前置校验 · ruleConfig 抽取 · 下游灌库
  ...

afterDoOperation (6 个)
  7. DynaAuthSchemePlugin      ⭐ newassignentry/checkroledetails/setadminrange/unsubmit/unaudit
  18. DynaAuthSchemeListPlugin ⭐ 列表 · setadminrange/assignrecord/audithisconfirmchange/delete 后置

onAddValidators (4 个)
  22. HRBaseDataStatusOp
  26. HisModelOPCommonPlugin   ⭐ 时序公共 OP · @SdkInternal 不可继承
  27. HisUniqueValidateOp      ⭐ 时序唯一性校验 OP · @SdkInternal 不可继承
  28. DynaAuthSchemeOp          ⭐ 主 OP · 注册 DynaAuthSchemeValidator

endOperationTransaction (3 个 · 实证 _auto_plugin_semantics)
  29. DynaAuthSchemeSaveSubmitOp   save/submit · 调 DynaSchemeRoleAssignServiceHelper.saveRoleEntry
  31. DynaAuthSchemeAuditOp        audit · 列表/单据双分支 + entryboid 重建
  32. DynaAuthSchemeConfirmChangeOp confirmchange · boid + bgVid 双写
```

---

## 八、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/hjm/hbpm）保持一致

### 8.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。`hrcs_dynascheme` 的 MuliLangTextField 字段（`name`/`simplename`/`description`/`assigndesc`/`canceldesc`/`oriname`/`ruledescription`）目前在主表 `t_hrcs_dynascheme` 承载多语言（无独立 `_l` 表 · 这是标品配置 · 不是默认行为）。**ISV 扩展 MuliLangTextField 字段时不要假设会自动有 _l 表**。

### 8.2 反模式 · 继承场景专属类

| 场景专属类（**禁继承**） | 推荐做法 |
|---|---|
| `DynaAuthSchemePlugin` | 并列挂新 FormPlugin · 继承 `HRDataBaseEdit` |
| `DynaAuthSchemeListPlugin` | 并列挂新 ListPlugin · 继承 `HRDataBaseList` |
| `DynaAuthSchemeOp` / `DynaAuthSchemeSaveSubmitOp` / `DynaAuthSchemeAuditOp` / `DynaAuthSchemeConfirmChangeOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `DynaAuthSchemeValidator` | 并列加新 Validator · 继承 `AbstractValidator` |
| `HisModelFormCommonPlugin` / `HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisModelListCommonPlugin` | @SdkInternal 平台时序内部类 · ISV 不得继承 |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸 · ISV 不要继承 · 直接复用即可 |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs）· 不在白名单 |

`DynaAuthSchemeXxxOp / DynaAuthSchemeXxxPlugin` 都是 hrcs 场景专属类 · ISV 不要继承 · 走并列挂或继承 SDK 白名单父类（参考 `_shared/platform_rules.json` PR-001）。

### 8.3 HisModel 时序场景

`hrcs_dynascheme` 是 HisModel 时序场景：
- **boid 业务维度** · 跨所有版本不变 · 下游引用必用 boid
- **id 版本维度** · 每次审核/confirmchange 产生新 id
- **iscurrentversion** · true 标记当前生效版本 · PR-008 必加过滤
- **sourcevid** · 指向上一版本（V2.sourcevid = V1.id）· 链式追溯
- **entryboid** · 分录行业务版本 · 跟主表 boid 配合追踪行级版本

参考 `_shared/platform_rules.json` PR-008 / PR-009 · 不可绕过。

### 8.4 列表三层模型（tablist|treelist）

`hrcs_dynascheme` 是 BillFormModel 单据 · 列表挂在数据实体上（不是 tablist/treelist 层）。Claude 做列表类 CS（如 setFilter / 自定义按钮）时 · 看反编译 `DynaAuthSchemeListPlugin extends HRDataBaseList` · 这是数据层。如果要做 UI 外壳定制 · 需要找列表表单模板（待探针确认 form 是否独立）。

### 8.5 PR 引用速查

本场景定制必引：
- **PR-001** 并列挂不继承 · 是核心铁律
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环
- **PR-005** ID 生成用 kd.bos.id.ID（已实证 · `DynaAuthSchemePlugin.java` L253 / L606 / L609 用 `ORM.genLongIds` · 同语义）
- **PR-007** 预置数据（issyspreset）不可改 · 业务自建可改
- **PR-008** 时序必带 iscurrentversion=true 过滤
- **PR-009** 下游用 boid 不用 id
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验
- **PR-011** BEC 走平台事件中心（**dynascheme 标品没发 BEC** · ISV 自建发布方时引用）

---

## 九、模型层对外 API（ServiceHelper 反编译实证）

| ServiceHelper | 关键方法 | 用途 |
|---|---|---|
| `DynaAuthSchemeServiceHelper` | `queryDyanRuleItems()` / `queryViewableSchemes()` / `queryOperationalSchemes()` / `querySchemeBoid()` / `resolveRuleConfigToSearch()` / `resolveSceneToSearch()` / `showAdminRangeDetail()` / `showChangeTips()` | 主业务 helper · DynaAuthSchemePlugin/ListPlugin 都依赖 |
| `DynaAuthSchemeParamRuleService` | `getComparisonOperatorsMap()` | 规则参数比较符 map |
| `DynaPremEvtSubService` | `queryChgRecordEffEventIds()` | 查询变动事件 id |
| `DynaRoleDetailServiceHelper` | `loadRoleCustomInfo()` / `genVersionRoleEntryColl()` | 角色清单装载 + 版本重建 |
| `DynaSchemeRoleAssignServiceHelper` | `saveRoleEntry()` | save/audit/confirmchange 都调它落 roleentry |
| `HRRolePermHelper` | `queryUserAdminGroups()` / `showRoleF7()` | 当前用户管理员组查询 + 角色 F7 |
| `RoleDBServiceHelper` | `queryHrRoleDyn()` / `loadHrRoleDyn()` | hrcs 中台角色 DynamicObject 装载 |
| `PermRuleValidateUtil` | `isDecisionSetConditionEmpty()` / `validCondition()` | 规则条件校验 |
| `RulePreviewUtil` | `getConditionPreviewMulLang()` / `trimMaxLength()` | 规则摘要生成 |

⚠️ ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。

---

## 十、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加自定义业务字段（如"方案备注分类"） | modifyMeta add field 到主实体 hrcs_dynascheme · ISV 前缀 | 低 |
| 加分录字段（在 roleentry 加"角色生效日期"） | modifyMeta add field 到子实体 roleentry · ISV 前缀 | 中（要对齐 entryboid 版本控制） |
| 监听 confirmchange 后做下游同步 | ISV 自建 OP 挂 confirmchange 的 afterExecuteOperationTransaction（PR-010） | 中 |
| save 前业务校验（如"角色不能为空"） | ISV 自建 Validator 挂 onAddValidators | 低 |
| 列表 setFilter 增加权限过滤 | ISV 自建 ListPlugin 挂 hrcs_dynascheme 列表 | 低 |
| 共用 HRAdminStrictPlugin 准入闸 | 配置即可 · 不要修改 | 0 |

详见 `06_customization_solutions.md`。
