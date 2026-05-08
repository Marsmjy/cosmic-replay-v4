# 模型设计 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 `scene_doc.json`（2 字段实抓）+ `_auto_inherit_chain.md` + 5 反编译类（HRAdminGroupTreeListPlugin/HRAdminStrictPlugin/AdminGroupAddUserOp/AdminGroupDelUserOp/AdminGroupDelOp）
> **confidence**：verified
> **数据源**：OpenAPI `getFormSchema` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.hradmin.*` / `kd.hr.hrcs.opplugin.web.perm.*`（2026-04-28）
> **场景类型**：DynamicFormModel · TreeList 视图 · 一张 2 字段中间关联表（user × usergroup）+ 树形结构由 perm_admingroup 承载

---

## ⭐ 关键业务事实 · "极简关联表 + 厚 TreeList 插件" 反差

`hrcs_useradmingroup` 是 hrcs 域里**字段最少、但 FormPlugin 业务逻辑最厚**的典型场景。它本身只是一张 **bos_user × perm_admingroup 多对多关联表**（2 个字段），但表单（**hrcs_useradmingroup** 这个 formNumber）实际承载的是一整套 HR 管理员组管理 TreeList UI——左边一棵管理员组树（数据来源是 `perm_admingroup`）+ 右边一个用户列表（数据来源就是本场景这张关联表）。

| 物理表 | 类型 | 业务含义 | 关键字段 |
|---|---|---|---|
| `t_perm_useradmingroup` | 关联实体（**本场景主表**） | 用户与管理员组的多对多绑定（一行 = 一个用户在一个管理员组里） | `fid` / `fuserid` / `fadmingroupid` |
| `t_perm_admingroup` | 树形管理员组（左侧树·非本场景主表，但极强耦合） | 管理员组本身（含 number/name/longnumber/parent/level） | `fid` / `fnumber` / `fname` / `flongnumber` / `fparent` / `fadminscheme` / `fadmintype` |
| `t_perm_admingroupfunperm` | 管理员组功能权限 | 删组级联表 | `fadmingroupid` |
| `t_perm_admingroupbizunit` | 管理员组业务单元 | 删组级联表 | `fadmingroupid` |
| `t_perm_admingrouporg` | 管理员组行政组织 | 删组级联表 | `fadmingroupid` |
| `t_perm_admingroupapp` | 管理员组应用 | 删组级联表 | `fadmingroupid` |
| `t_perm_admingroupadduser` | 管理员组可加用户范围 | 删组级联表 | `fadmingroupid` |
| `t_hrcs_admingrouporg` | hrcs 行政组织扩展 | 删组级联表 | `fid` |
| `t_hrcs_admingroupfunc` | hrcs 功能扩展 | 删组级联表 | `fid` |
| `t_hrcs_admingroupfile` | hrcs 档案扩展 | 删组级联表 | `fid` |

⚠️ **`t_perm_useradmingroup` 是本场景的物理底表**，但删管理员组（`do_remove_group` opKey）触发的级联清理涉及 9 张表（5 张 `perm_admingroup*` + 3 张 `hrcs_admingroup*` + 主表 `perm_admingroup` 自己）。详见反编译 `AdminGroupDelOp.java` L34-L57 实证。**ISV 自建跨表（如 `${ISV_FLAG}_admingroupext`）必须在自己的 OP 里加级联**，标品不会自动级联到 ISV 表。

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于 **TreeList**（树形列表）形态——左树 + 右列表。`hrcs_useradmingroup` 列表页由**两到三层独立元数据**组成。Claude 做列表/树形 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体** | DynamicFormModel | `hrcs_useradmingroup`（关联表 · 菜单直挂） | 查 `t_perm_useradmingroup` · 字段 user/usergroup |
| **TreeList 表单模板** | 动态表单 | `hrcs_useradmingroup`（UI 壳与数据实体共用 form · 由 `HRAdminGroupTreeListPlugin` 承载左树 + 右列表的 UI 逻辑） | TreeList UI 壳 + 工具栏按钮（btnnew/btnedit/btndel/searchap）+ donothing_* 拦截 + 子页面调度 |
| **F7 列表模板** | 动态表单（`bos_user` 系统级 lookup）| 不固定（添加用户走 `bos_usertreelistf7`） | 用户 F7 选择壳 |

**插件挂载职责分工**：
- **数据层 setFilter 增加权限过滤** → 挂在 `hrcs_useradmingroup` 数据实体（实证 `HRAdminGroupTreeListPlugin.setFilter` L644-L651）
- **TreeList 工具栏 / 树初始化 / donothing 拦截 / 子页面调度** → 同样挂在 `hrcs_useradmingroup` form（这就是 `HRAdminGroupTreeListPlugin extends AbstractTreeListPlugin`）
- **左树管理员组明细弹窗** → 挂 `hrcs_admingroupdetail`（不在本场景）
- **批量授权弹窗** → 挂 `hrcs_amingroupbatchauth`（不在本场景 · 注意 i 是 `aming` 不是 `aminig`）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）+ `treelist` TreeList 模型。

---

## 二、继承链 · DynamicFormModel + 普通中间表

### ModelType 实证

`probe_snapshot.json` `metadataMeta.modelType = "DynamicFormModel"`。这与 `hrcs_dynascheme` 的 BillFormModel + HisModel 不同，也不像 hjm/admin_org 的 BaseFormModel：

| 维度 | 本场景 | hrcs_dynascheme（对照） | admin_org（对照） |
|---|---|---|---|
| ModelType | DynamicFormModel | BillFormModel | BaseFormModel |
| 是否单据 | ❌（无 status/audit/submit） | ✅ | ❌ |
| 是否 HisModel 时序 | ❌（**实证 grep 无 boid/iscurrentversion/HisModel**） | ✅ | ✅ |
| 视图形态 | TreeList（左树右列表） | List | TreeList |
| 字段数 | 2 | 56 | 30+ |
| 主操作 | do_add_user / do_remove_user / do_remove_group / donothing_* | save/submit/audit/confirmchange | save/audit/disable |

⚠️ **本场景非 HisModel** —— 字段无 `boid` / `iscurrentversion` / `sourcevid` / `bsed` / `hisversion`，反编译类也无 `HisModel*` 系列引用。所以 PR-008（iscurrentversion 过滤）/PR-009（boid 业务维度）**不强引** —— 但下游 `perm_admingroup` 是不是 HisModel 不在本场景管辖范围（用户可以从 04/05 流程图看到 ISV 改字段不会涉及 boid 串联）。

### 字段层级分类

苍穹元数据字段分 4 层（与其他场景一致 · `scene_doc.json` `layer` 字段标注）。本场景因极简，L0/L1/L2 都没显式落字段，**全部 2 个字段都是 L3 业务字段**：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl（隐式） | `id` / `creator` / `modifier` / `createtime` / `modifytime`（schema_text 未列·苍穹默认带） | 🔒 不改 |
| **L1** 业务通用 | （无）| 本场景无 number/name/status/enable | - |
| **L2** 时序模型 | （**不适用 · 非 HisModel**） | - | - |
| **L3** 业务字段 | hrcs_useradmingroup 自身 | `user` / `usergroup` | ⚠️ 可加 ISV 字段（不可改这 2 个） |

### 关键认知

- 本场景**不是** HisModel · 不要套时序场景的 boid/iscurrentversion 模式
- `perm_admingroup`（左树）是不是 HisModel 不在本场景明细查得到 · 但对应反编译里没看到时序字段引用 · 推测也是普通基础资料
- 唯一的两个字段 `user` / `usergroup` 都是 BasedataField · 标 `isvCanModify=true` 但**实际 ISV 不应直接改它们的物理列**（破坏与 OP 链耦合）

---

## 三、完整字段表（OpenAPI scene_doc.json 实抓 · 共 2 个字段）

| Field Key | 类型 | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `user` | BasedataField | 用户 ⭐ | ❌ | ⚠️ 黄区 | → `bos_user`（physicalColumn=`fuserid`） |
| `usergroup` | BasedataField | 管理员分组 ⭐ | ❌ | ⚠️ 黄区 | → `perm_admingroup`（physicalColumn=`fadmingroupid`） |

⚠️ 注意 scene_doc 标 `required=false` · 但 FormPlugin closedCallBack 闭环里实际是 user 必从 F7 拿 · usergroup 必从 focusNodeId 拿 · 元数据层"不必填"在业务流上等价于"必填"。ISV 加 Validator 时可以补做"非空"校验（详见 06 CS-03）。

---

## 四、TreeList 视图三层模型（核心场景结构）

本场景的"左树 + 右列表"是苍穹 TreeList 的标准形态。其结构在 `HRAdminGroupTreeListPlugin extends AbstractTreeListPlugin` 上：

```
TreeList 视图
├─ 左侧 TreeView（控件 key = "treeview"）
│   └─ 数据源 = perm_admingroup（HRAdminGroupService.initAdminGroupTree 装载）
│   └─ 节点 id 格式: <adminGroupId>_<level>（实证 L184/L272/L344/L535/L615）
│   └─ 根节点 id = "8609760E-EF83-4775-A9FF-CCDEC7C0B689"（UUID 字符串硬编码常量）
│   └─ 树节点点击 → treeNodeClick → IPageCache 缓存 5 个 key
├─ 右侧 List（数据实体 = hrcs_useradmingroup · 即本场景）
│   └─ 显示当前节点（focusNodeId）下的用户行
│   └─ setFilter → 加 usergroup.id in adminGroupCanSee 过滤
└─ 顶部工具栏
    ├─ btnnew（donothing_add_group）
    ├─ btnedit（donothing_modify_group）
    ├─ btndel（donothing_remove_group）
    ├─ searchap（搜索框 · 注册 SearchEnterListener）
    └─ 行级按钮（donothing_add_user / donothing_remove_user / donothing_batch_perm / refresh）
```

### 4.1 IPageCache 状态约定（TreeList 模式核心）

TreeList 跨方法状态全靠 `IPageCache` 字符串键值传递。本场景使用的 8 个 key（实证 `HRAdminGroupTreeListPlugin.java`）：

| key | 类型 | 由谁写 | 由谁读 | 业务含义 |
|---|---|---|---|---|
| `focusNodeId` | String | treeNodeClick L621 + adminGroupTreeRemoveOperation L526 | beforeDoOperation / showUserF7TreeList / OperateOption | 当前点击的树节点 id（含 `_<level>` 后缀） |
| `focusNodeParentId` | String | treeNodeClick L622 | adminGroupTreeModifyOperation L271 | 当前节点的父节点 id |
| `focusAdgNumber` | String | treeNodeClick L623 | OperateOption（传给 OP 写权限日志） | 当前节点的 number |
| `focusAdgName` | String | treeNodeClick L624 | OperateOption | 当前节点的 name |
| `focusNodeLongNumber` | String | treeNodeClick L625 | verifyBatchAuth L453 / verifyAdminGroup L476 | 当前节点的 longnumber（用于 startsWith 算法判断管控范围） |
| `superiorGroupIds` | JSON String → List<Long> | （由其他模块在 initialize 阶段写 · 本类只读） | beforeDoOperation L185 / verifyAdminGroup L479 / OperateOption（传给 OP 守护） | 当前用户的"上级管理员组 id 列表"·上级组只能查看不能修改 |
| `currentUserInGroup` | JSON String → Set<String> | （初始化阶段由其他模块写） | verifyBatchAuth L460 / verifyAdminGroup L475 | 当前用户所在的管理员组 longnumber 集合（startsWith 防越权） |
| `adminScheme` / `adminType` | String → Long | （初始化阶段由其他模块写） | showUserF7TreeList L401-L407 | 当前用户的方案 id / 类型 id（用于跨 scheme 跨 type 用户排除） |
| `adminGroupCanSee` | JSON String → List<Long> | （初始化阶段由其他模块写） | setFilter L646 | 当前用户能看到的 admingroup id 列表 · 用作右列表 setFilter 过滤 |

⚠️ **ISV 想给 TreeList 加新行为**（如自定义按钮、自定义过滤）· 必须读 `IPageCache` 拿状态 · 不能假设变量在 `this` 上（场景专属类不允许继承 · 只能并列挂插件 · 拿不到 private 字段）。

### 4.2 OperateOption 跨阶段传值（FormPlugin → OP）

TreeList FormPlugin 调 OP 时通过 `OperateOption.setVariableValue` 传值（`HRAdminGroupTreeListPlugin.afterDoOperation` L347-L353 + `closedCallBack` L562-L566 + `confirmCallBack` L530-L532 实证）：

```java
OperateOption operateOption = OperateOption.create();
operateOption.setVariableValue("focusNodeId", focusNodeIdStr);
operateOption.setVariableValue("superiorGroupIds", this.getPageCache().get("superiorGroupIds"));
operateOption.setVariableValue("focusAdgNumber", this.getPageCache().get("focusAdgNumber"));
operateOption.setVariableValue("focusAdgName", this.getPageCache().get("focusAdgName"));
operateOption.setVariableValue("appId", this.getView().getFormShowParameter().getAppId());
operateOption.setVariableValue("tag_of_view", Boolean.TRUE.toString());

OperationResult result = OperationServiceHelper.executeOperate(
    "do_remove_user", "hrcs_useradmingroup", dynamicObjects, operateOption);
```

OP 端通过 `this.getOption().getVariableValue(key, defaultValue)` 拿值（实证 `AdminGroupDelUserOp.java` L62 / `AdminGroupAddUserOp.java` L72）。**ISV 自定义 OP 需要拿 TreeList 状态时必须沿用这套 key 名**·否则 FormPlugin 不会传过来。

---

## 五、共用物理表分析（区分键）

`hrcs_useradmingroup` **不与其他 form 共用主物理表 `t_perm_useradmingroup`**（标品里没看到平行 form 共表写入此表的场景）。但它**通过 usergroup 字段跟下列表强耦合**：

| 关联表 | 关系 | 区分键 |
|---|---|---|
| `perm_admingroup` | 左树管理员组 · 主从（usergroup 是外键） | `usergroup = id` |
| `hrcs_admingrouporg` | hrcs 行政组织扩展 · 删组级联（标品 OP 实证 L50） | `id = adminGroupId` |
| `hrcs_admingroupfunc` | hrcs 功能扩展 · 删组级联 | `id = adminGroupId` |
| `hrcs_admingroupfile` | hrcs 档案扩展 · 删组级联 | `id = adminGroupId` |
| `perm_role` | 角色表 · 角色引用反查（FP_HAGTL_RREF_1） | `createadmingrp = adminGroupId` |
| `hrcs_roleopenscope` | 角色启用范围 · 引用反查（FP_HAGTL_RREF_2） | `admingroup = adminGroupId` |
| `hrcs_roleassignscope` | 角色分配范围 · 引用反查（FP_HAGTL_RREF_3·**这是 dynascheme ↔ useradmingroup 的耦合点**） | `admingroup = adminGroupId` |

⚠️ **delete 联动清理**（`AdminGroupDelOp.beginOperationTransaction` L34-L57 实证）：删除一个管理员组后，平台**自动级联清理** 9 张表（5 张 perm_admingroup* + 3 张 hrcs_admingroup* + perm_admingroup 主表）。这就是为什么 `do_remove_group` opKey 之前要在 FormPlugin 阶段做 4 道前置校验 + 角色引用反查 —— 一旦进 OP 就一刀斩 · 没回头路。

---

## 六、Plugin 链概览（8 标品 + 0 ISV）

完整 8 plugin 清单见 `_auto_plugin_registry.md`。这里只列对 ISV 扩展最关键的层次：

```
preOpenForm  (4 个)
  1. ForbidUrlOpenPlugin               (HRDynamicFormBasePlugin) · 防外部 URL 访问
  2. HRCertCheckEdit                   (HRDataBaseEdit) · 证书校验
  3. HRCertCheckList                   (AbstractListPlugin) · 列表证书校验
  4. HRAdminStrictPlugin               ⭐ HR 域准入闸 · 11 hrcs 表单共用 · 非 HR 管理员直接拒

registerListener / initializeTree / initTreeToolbar / beforeDoOperation / afterDoOperation / closedCallBack / confirmCallBack / treeNodeClick / setFilter / chat (1 个)
  5. HRAdminGroupTreeListPlugin        ⭐ 主 TreeList 插件 · 业务核心 · 场景专属类禁继承

beginOperationTransaction (3 个独立 OP)
  6. AdminGroupDelOp        do_remove_group · 9 表级联删
  7. AdminGroupAddUserOp    do_add_user · SaveServiceHelper.save + 权限日志 + 缓存清理
  8. AdminGroupDelUserOp    do_remove_user · 跨组守护 + 部分成功反馈 + 权限日志
```

---

## 七、模型层对外 API（ServiceHelper 反编译实证）

| ServiceHelper / 类 | 关键方法 | 用途 |
|---|---|---|
| `HRAdminGroupService` | `initAdminGroupTree(tv, pageCache)` / `writeOpLog(success, appId)` / `adminEvent2PermLog(...)` | 主业务 helper · TreeList 树初始化 + 操作日志 + 权限事件转日志 |
| `HRPermCacheMgr` | `clearAllCache()` | 权限缓存清理（add/del 用户成功必调）|
| `PermDBServiceHelper` | `roleDBService.isExists(qf)` | 角色存在性反查（删组前 createadmingrp 反查） |
| `HRBaseServiceHelper` | `queryOriginalCollection(fields, qf)` / `queryOne(fields, qf)` / `isExists(qf)` | HR 基础资料服务 · 角色 open/assign scope 反查 |
| `HRAdminService` | `isHrAdmin()` | HR 域管理员准入判定（HRAdminStrictPlugin / TreeList beforeDoOperation 都用） |
| `PermCommonUtil` | `isAdminUser(uid)` / `isCosmicUser(uid)` / `isEnablePermLog()` / `isEnableAuthorityChangeNotice()` / `getAdminLevelLimit()` | 权限基础工具 · 准入开关 + 层级上限 |
| `PermissionServiceHelper` | `isAdminUser(uid)` | 权限服务 helper |
| `PermAdminLogHelper` | `adminEventImage(adminGroupId, lang, userIds)` | 权限事件快照（日志格式） |
| `FormConfigFactory` | `cancelShowFormRights(userIds)` | 用户领域缓存清理（add/del 用户后必调） |
| `IAdminGroupListSubExtPlugin` | `beforeAddCustomUser(eventArgs)` | ⭐ ISV 唯一可继承的 SDK 扩展点（用户F7范围扩展） |

⚠️ ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。

特别值得 ISV 关注的是 **`IAdminGroupListSubExtPlugin`**（包名 `kd.sdk.hr.hbp.business.extpoint.permission.hradmi`）—— 这是反编译实证（`HRAdminGroupTreeListPlugin.java` L123-L124 + L432-L438）的 SDK 扩展点·**ISV 可以实现它来扩展用户F7范围**（详见 06 CS-06）。

---

## 八、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/hjm/hbpm/hrcs_dynascheme）保持一致

### 8.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。`hrcs_useradmingroup` **没有任何 MuliLangTextField 字段**（只有 user/usergroup 两个 BasedataField），所以**无独立 `_l` 表**。但其依赖的 `perm_admingroup`（左树）有 name/description 多语言 · 平台维护 `t_perm_admingroup_l` 多语言子表。**ISV 给本场景加 MuliLangTextField 字段时不要假设会自动有 _l 表** · 平台行为不一致。

### 8.2 反模式 · 继承场景专属类

| 场景专属类（**禁继承**） | 推荐做法 |
|---|---|
| `HRAdminGroupTreeListPlugin` | 并列挂新 ListPlugin · 继承 `AbstractTreeListPlugin` 或 `HRDataBaseList` |
| `AdminGroupAddUserOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `AdminGroupDelUserOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `AdminGroupDelOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸 · ISV 不要继承 · 直接复用即可 |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs）· 不在白名单 |

`HRAdminGroupTreeListPlugin / AdminGroup*Op` 都是 hrcs_useradmingroup 场景专属类 · ISV 不要继承 · 走并列挂或继承 SDK 白名单父类（参考 `_shared/platform_rules.json` PR-001）。

### 8.3 TreeList 三层模型（treelist 关键词）

`hrcs_useradmingroup` 是 **TreeList**（树形列表）形态 · ISV 做"左树 + 右列表"类 CS 时：

- **左树管理员组** · 数据源是 `perm_admingroup` · 不是本场景；树节点点击事件由 `HRAdminGroupTreeListPlugin.treeNodeClick` 处理 · ISV 不能继承重写
- **右列表用户** · 数据源是 `hrcs_useradmingroup`（本场景）· setFilter 由 `HRAdminGroupTreeListPlugin.setFilter` 处理 · ISV 加自定义过滤需通过 IAdminGroupListSubExtPlugin 扩展或新 ListPlugin
- **节点 id 格式**：`<adminGroupId>_<level>` · 根节点 id 是 UUID `8609760E-EF83-4775-A9FF-CCDEC7C0B689` · ISV 不要自己解析 nodeId

参考 `_shared/platform_rules.json` 跨场景规范 · 不要假设 TreeList 跟普通 List 同模式。

### 8.4 非 HisModel 时序场景

`hrcs_useradmingroup` **不是 HisModel** · ISV 实证（grep 反编译 + scene_doc.json）·无 boid / iscurrentversion / sourcevid / bsed / hisversion 字段，反编译类无 `HisModel*` 引用。所以：

- **不需要带 iscurrentversion=true 过滤**（PR-008 不强引）
- **不需要用 boid 反查下游**（PR-009 不强引）
- 但 `perm_admingroup` 是不是 HisModel 不在本场景管辖 · ISV 反查 perm_admingroup 时仍要查 `_shared/_standard_metadata`

参考 `_shared/platform_rules.json` PR-008 / PR-009 · 在不适用场景不强引 · 但要在 03 解释清楚（避免 ISV 误套时序模式）。

### 8.5 PR 引用速查

本场景定制必引（11 PR · 红线）：
- **PR-001** 并列挂不继承 · 是核心铁律（HRAdminGroupTreeListPlugin / AdminGroup*Op 全是场景专属类）
- **PR-002** 插件 RowKey 执行顺序（ISV 扩展元数据可插队标品之前）
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环（本场景 closedCallBack 写值时也要注意）
- **PR-005** ID 生成用 kd.bos.id.ID（本场景 hrcs_useradmingroup 主键由 SaveServiceHelper 自动分配）
- **PR-006** CodeRuleOp 是平台模板插件（本场景无 number 字段·不引）
- **PR-007** 预置数据不可改 · 业务自建可改
- **PR-008** 时序必带 iscurrentversion=true 过滤（**本场景非 HisModel · 不强引**）
- **PR-009** 下游用 boid 不用 id（**本场景非 HisModel · 不强引**）
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验
- **PR-011** BEC 走平台事件中心（**hrcs_useradmingroup 标品 0 处发布 · CS-05 已砍**）

---

## 九、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加 ISV 自定义字段（如"分配标签"） | modifyMeta add field 到主实体 hrcs_useradmingroup · ISV 前缀 | 低（字段少 · 加扩展无版本控制干扰） |
| 联动管理员组分类下拉 | ISV 自建 FormPlugin 挂 hrcs_useradmingroup · 在 propertyChanged 联动 | 中 |
| 加 do_add_user / do_remove_user 前置校验 | ISV 自建 Validator 挂 onAddValidators | 中（需注意 OperateOption 拿 focusNodeId） |
| 删组前查 ISV 自建表反向引用 | ISV 自建 OP 挂 do_remove_group beforeExecuteOperationTransaction（早于 standalone OP） | 中 |
| 树形列表节点过滤（ISV 域管理员只见自己组） | 实现 `IAdminGroupListSubExtPlugin.beforeAddCustomUser` SDK 扩展点 | 低（SDK 接口稳定） |
| 批量授权 donothing_batch_perm 自动化扩展 | 挂 hrcs_amingroupbatchauth 子页面 · 不挂本场景 | 中 |
| BEC 跨模块通知（用户加入管理员组后通知其他系统） | **不建议 · 标品 0 处发布 · ISV 自建无订阅方** | 高 |

详见 `06_customization_solutions.md`。
