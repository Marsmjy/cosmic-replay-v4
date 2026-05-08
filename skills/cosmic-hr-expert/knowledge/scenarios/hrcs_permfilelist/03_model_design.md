# 模型设计 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于 `scene_doc.json`（列表视图）+ `_auto_plugin_registry.md`（4 plugins）+ `_auto_plugin_semantics.md`（2 反编译类）+ CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.permfile.PermfilesListPlugin`/`HRAdminStrictPlugin`
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema` + `_shared/_standard_metadata/entity_metadata/hrcs_permfilelist.md` + CFR 反编译（2026-04-28）

---

## ⭐ 关键业务事实 · "列表壳 + 主实体 + 分组实体 + 关联表"四件套

`hrcs_permfilelist` 是 HR 通用服务（hrcs）权限管理域里的**用户授权管理列表**入口（菜单：HR通用服务 / 权限管理 / 用户授权）。它是**列表型表单**（`ModelType` 推断为 `ListFormModel` · 见 `_auto_plugin_registry.md` 主插件父类 `HRStandardTreeList`），自身不含字段（`scene_doc.fields=0`）—— 业务字段都承载在它驱动的下游 4 张物理表里。

| 物理实体 | formNumber | 类型 | 业务含义 | 关键字段（实证） |
|---|---|---|---|---|
| `hrcs_permfilelist` | `hrcs_permfilelist` | **列表壳**（无字段） | 用户授权列表入口 · 树+列表混合视图 | -（仅承载列表 UI） |
| `hrcs_userpermfile` | `hrcs_userpermfile`（常量 `HBSS_USERPERMFILE`） | 主数据实体（用户授权档案） | 一行 = 一个用户在某 org 上的授权档案 | `id` / `user` / `user.name` / `user.isforbidden` / `org.name` / `org.id` / `permfileenable` |
| `hrcs_permfilegrp` | `hrcs_permfilegrp`（常量 `ENTITYTYPE_PERMFILEGRP`） | 分组树（左侧 TreeView 数据源） | 权限档案的分组（树形 · longnumber 编码） | `id` / `name` / `parent` / `longnumber` / `enable` / `isleaf` |
| `hrcs_permfilegrpmember` | `hrcs_permfilegrpmember` | 分组成员中间表 | 用户档案 ↔ 分组的多对多关联 | `id` / `permfilegrp` / `permfile` |
| `hrcs_userrolerelat` | `hrcs_userrolerelat` | 用户-角色关联表 | 一个用户档案绑定到 N 个角色 + 范围属性 | `id` / `user` / `permfile` / `role` / `role.enable` / `customenable` / `validstart` / `validend` / `creator` |

⚠️ **本场景独特点**：
- 主插件 `PermfilesListPlugin` 构造时把 `hrcs_permfilegrp` 作为树根实体（`super("hrcs_permfilegrp", ID_ROOTNODE, false)` · 反编译 L243-L244）；列表区显示的却是 `hrcs_userpermfile` 的数据（`USER_LIST = "billlistap"`）
- 树根写死的 ID：`8609760E-EF83-4775-A9FF-CCDEC7C0B689`（常量 `ID_ROOTNODE` · 反编译 L216）—— ISV 不要改这个 GUID
- "新增"按钮（`bar_new`）实际打开的是 `hrcs_userpermfile` 表单 · 不是 `hrcs_permfilelist` 自身
- 列表过滤器的 `permfileenable` 字段默认值 = `"1"`（仅显示生效档案 · 反编译 `filterContainerInit` L1007-L1016）

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"列表型表单 + 树过滤"形态。`hrcs_permfilelist` 的 UI 由 **三层独立元数据**组成，Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **列表壳（list 表单）** | TreeListFormModel · 树+列表 | `hrcs_permfilelist`（菜单直挂） | 列表 UI 壳 + 树控件 + 工具栏按钮（bar_new/inituserperm/btn_clearcache 等） |
| **主数据实体** | BillFormModel | `hrcs_userpermfile`（主档案）⭐ | 列表 row 的真实数据 / 字段定义 / save 时的 OP 链（PermFilesSaveOp） |
| **分组树实体** | BasedataModel | `hrcs_permfilegrp` | 左侧 TreeView 节点数据源（含 longnumber/parent 形成层级） |

**插件挂载职责分工**（务必区分）：
- **列表 UI 壳 / 工具栏点击 / 树过滤器** → 挂 `hrcs_permfilelist`（当前 `PermfilesListPlugin` + `HRAdminStrictPlugin` + `QueryListPlugin` 都挂这里）
- **save / disable / enable / delete 数据校验** → 挂 `hrcs_userpermfile`（当前 `PermFilesSaveOp` 挂这里 · 反编译 jar 缺失但 plugin_registry 实证）
- **分组节点的 add/edit/del** → 走 `treeToolbarClick` · 直接 showForm `hrcs_permfilegrp` 表单（反编译 L843-L865）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`） + `tablist`/`treelist` 三层模型。

---

## 二、继承链 · TreeListFormModel + Standard 派生

### ModelType 实证

`PermfilesListPlugin extends HRStandardTreeList implements TreeNodeQueryListener, TreeNodeClickListener`（反编译 L201-L204）。`HRStandardTreeList` 是 HR 提供的"标准树列表"父类 · 比 `AbstractTreeListView` 多了 admingroup 准入逻辑/树根管理/标准搜索框样式。这是**列表型表单**（不是 BillFormModel 单据 · 不是 BaseFormModel 基础资料）—— 用户授权管理是"管理列表"形态 · 不是单据类。

### 字段层级分类（主数据 hrcs_userpermfile）

苍穹元数据字段分 4 层（与 admin_org/dynascheme 一致 · scene_doc.json `layer` 字段标注）：

| 层级 | 来源 | 典型字段 | ISV 能否改 |
|---|---|---|---|
| **L0** 系统级 | bos_basetpl | `id` / `creator` / `modifier` / `createtime` / `modifytime` / `masterid` | 🔒 不改（破坏全系统） |
| **L1** HR 业务通用 | bos_basetpl + HR 父模板 | `enable` / `status` / `description` / `issyspreset` | 🔒 / ⚠️ 多数不改 |
| **L2** 时序模型 | （本场景非时序）❌ 不适用 | — | — |
| **L3** 业务字段 | hrcs_userpermfile 自身 | `user` / `org` / `permfileenable` / 关联 `hrcs_userrolerelat`.role | ⚠️ 谨慎改（涉及业务规则） |

### 关键认知 · 不是 HisModel 时序场景

- **本场景不带时序版本控制**（grep `iscurrentversion|HisModel|boid` 全无命中）⚠️ 实证
- 一个用户在一个 org 下只有一份授权档案（业务唯一键推测：`user + org`）
- `permfileenable = "1"` 表示档案生效 · `"0"` 表示失效（反编译 `setEnable` L767-L781）
- 删除/失效都不带版本号 · 直接物理操作（与 dynascheme/jobhr 等时序场景区分）

---

## 三、完整字段表（主数据 hrcs_userpermfile · 反编译实证）

> ⚠️ 本场景 `scene_doc.fields=0`（列表壳无字段）· 字段全部来自反编译 `PermfilesListPlugin` 对 `hrcs_userpermfile` 表的读写实证。`scene_doc.json` 没有字段元数据 · 这里以反编译为准。

### 3.1 主表业务核心字段（`hrcs_userpermfile`）

| Field Key | 类型（推断） | 业务含义 | 必填 | ISV 可改 | 备注 |
|---|---|---|---|---|---|
| `id` | BigIntField | 档案 ID | ✅ | 🔒 | 主键 · ORM 生成 |
| `user` | BasedataField | **用户**（→ `bos_user`） ⭐ | ✅ | ⚠️ 黄区 | 反编译多次读 `permfile.getString("user")` |
| `user.name` | 派生字段 | 用户姓名 | - | 🔒 | 读取展示 · 反编译 L725 |
| `user.isforbidden` | 派生字段 | 用户是否被禁用 | - | 🔒 | checkRoleForBidden 用于失效校验 · 反编译 L1199 |
| `org` | BasedataField | 所属组织（→ HR 行政组织） | ✅ | ⚠️ 黄区 | 列表过滤维度 · setFilter 用 `org.id in` · 反编译 L1035 |
| `org.name` | 派生字段 | 组织名称 | - | 🔒 | 显示 + 错误消息拼接 |
| `permfileenable` | ComboField（"1"/"0"）| **档案生效状态** ⭐ | ✅ | ⚠️ 黄区 | enable / disable opKey 修改 · 默认列表过滤 = "1" |

### 3.2 分组树（`hrcs_permfilegrp`）字段

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `id` | BigIntField | 分组节点 ID |
| `name` | MuliLangTextField | 分组名称 |
| `parent` | ParentBasedataProp | 上级分组（自引用） |
| `longnumber` | TextField | 长编码（树形 · 用于 like 子树查询 · 反编译 L333） |
| `enable` | ComboField | 是否启用（"1" 启用 · "0" 禁用） |
| `isleaf` | CheckBoxField | 是否叶子节点 |

### 3.3 分组成员中间表（`hrcs_permfilegrpmember`）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `id` | BigIntField | 关联 ID |
| `permfilegrp` | BasedataField | 分组（→ `hrcs_permfilegrp`） |
| `permfile` | BasedataField | 档案（→ `hrcs_userpermfile`） |

⚠️ 反编译 `nodeClickFilter` L996-L1005 / `buildNodeClickFilter` L310-L329：树节点点击过滤主列表用的是 `hrcs_permfilegrpmember.permfilegrp.id in/=` —— 一个档案可属于多个分组 · 中间表是多对多关系。

### 3.4 角色关联表（`hrcs_userrolerelat`）

| Field Key | 类型 | 业务含义 |
|---|---|---|
| `id` | BigIntField | 关联 ID |
| `user` | BasedataField | 用户（→ `bos_user`） |
| `permfile` | BasedataField | 档案（→ `hrcs_userpermfile`） |
| `role` | BasedataField | 角色（→ `perm_role`） |
| `role.enable` | 派生字段 | 角色是否启用 |
| `customenable` | ComboField | 角色成员范围属性 |
| `validstart` / `validend` | DateField | 授权有效期 |
| `creator` | BasedataField | 授予人 |

⚠️ 反编译 `getUserRelatesByPermFields` L1190-L1194：一个 permfile 通过 `permfile.id in (?)` 查关联角色 · 复制权限（copyperm）就是把 A 用户的这条关系复制到 B 用户。

### 3.5 系统派生字段（autoComputed · 不要手填）

`creator` / `modifier` / `createtime` / `modifytime` / `masterid` —— 平台维护 · 手改破坏数据一致性。

---

## 四、列表行为模型（hrcs_permfilelist 列表壳）

### 4.1 列表行为关键 = 树 + 列表 + 工具栏

`PermfilesListPlugin` 实现 4 大主题：

| 主题 | 入口方法 | 反编译行 | 说明 |
|---|---|---|---|
| **树根渲染** | `getRootDynamicObject()` / `getTreeViewCollection()` | L270-L308 | 树根 ID 写死 GUID · 子节点按 `enable=1 + parent=parentId` 过滤 |
| **节点点击 → 过滤主列表** | `buildNodeClickFilter()` / `nodeClickFilter()` | L310-L329, L996-L1005 | 走 `hrcs_permfilegrpmember.permfilegrp.id in (子树 longnumber 命中的所有 grpId)` |
| **工具栏点击** | `beforeItemClick()` / `beforeDoOperation()` / `afterDoOperation()` / `itemClick()` | L344-L518 | 19 个工具栏按钮（bar_new/btn_clearcache/btn_copyperm/...）分发 |
| **新增/编辑分组节点** | `treeToolbarClick()` / `addGroupNode()` / `editGroupNode()` | L843-L965 | btnnew/btnedit/btndel 三按钮 · 直接 showForm `hrcs_permfilegrp` |

### 4.2 setFilter 准入闸（数据隔离 · 关键）

```java
// 反编译 setFilter L1030-L1037
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    long level = HRPermServiceHelper.getUserGroupMinLevel();
    if (-1L == level || level > 2L) {
        List userPermFileIds = HRPermServiceHelper.getUserPermFile();
        setFilterEvent.getQFilters().add(new QFilter("org.id", "in", userPermFileIds));
    }
}
```

⚠️ **业务含义**：当前用户的 admingroup 等级 > 2（非顶级管理员）· 列表只能看到自己 admingroup 范围内的 org 下的档案。这是 hrcs 域的"管理员看自己的人"标准模式。**ISV 做列表过滤定制（CS-06）必须在此 super 之后追加 · 不能 reset**。

### 4.3 复杂工具栏按钮分支表

反编译 `beforeDoOperation`/`itemClick`/`beforeItemClick` 三方法分发表（L344-L518）：

| 按钮（itemKey/opKey） | 行为 | 反编译行 |
|---|---|---|
| `bar_new` | 关闭 args + showForm hrcs_userpermfile + writeLog | L423, L555-L565 |
| `enable` / `disable` | 设 OperateOption variable `tag_of_view=true` | L427-L432 |
| `bar_import` | writeLog "importdata" | L433-L437 |
| `btn_allocprem` | userAssignRole(rows) | L438-L441, L567-L586 |
| `inituserperm` | showForm `bos_list` 显示 `hrcs_perminitrecord` | L442-L445, L588-L600 |
| `exportuserperm` | exportUserPerm(rows) | L446-L449, L602-L632 |
| `btn_initdata` | genPermFiles + writeLog | L450-L455, L1040-L1056 |
| `btn_copyperm` | copyPerm(rows) | L456-L459, L1140-L1163 |
| `btn_batchgroup` | handleBatchGroup(rows) | L460-L463, L531-L543 |
| `btn_clearcache` | HRPermCacheMgr.clearAllCache + clearAllManageCache | L505-L512 |
| `refresh` | billList.refresh + 树节点重新点击 | L513-L516, L545-L553 |

---

## 五、关键域逻辑（反编译实证）

### 5.1 `bar_new`（新增档案）特殊路径

```java
// 反编译 showNewForm L555-L565
private void showNewForm(BeforeDoOperationEventArgs args) {
    BillShowParameter bsp = new BillShowParameter();
    bsp.setCustomParam("groupId", this.getTreeModel().getCurrentNodeId());  // 携带当前选中分组
    bsp.setFormId(HBSS_USERPERMFILE);                                        // hrcs_userpermfile
    bsp.getOpenStyle().setShowType(ShowType.Modal);
    CloseCallBack callBack = new CloseCallBack((IFormPlugin)this, NEW_PERMFILE);
    bsp.setCloseCallBack(callBack);
    this.getView().showForm((FormShowParameter)bsp);
    RoleService.commonWriteLogBeforeDoOp(args);
    args.setCancel(true);  // ⭐ 取消标品默认 new 行为
}
```

⚠️ **关键 trick**：`bar_new` 走 `args.setCancel(true)` 阻断标品默认新增 · 自己控制 showForm。所以 `new` opKey 的标品逻辑（PermFilesSaveOp）**不会被触发** —— 真正的保存是新打开的 `hrcs_userpermfile` 表单上 save opKey 走的。

### 5.2 复制权限（copyPerm）的双重权限校验

```java
// 反编译 checkCanCopy L1166-L1188
1. checkRoleForBidden(permFileId)：当前记录的 user.isforbidden=true 或 permfileenable=0 → 不允许
2. getUserRelatesByPermFields：当前用户必须有可复制的角色（关联表中存在记录）
3. HRRolePermHelper.queryViewableRoles：当前操作员必须能"看见"那些角色（角色权限二次过滤）
4. 全部通过 → showCopyPermForm 弹 hrcs_copyperm 表单
```

### 5.3 批量分组（handleBatchGroup）调度链

```java
// 反编译 handleBatchGroup L531-L543
1. 收集 rows 的 primaryKeyValues 拼成 "id1,id2,id3" 字符串
2. writeLog "batch grouping"
3. RoleServiceHelper.showForm("hrcs_permfilegrptree", params, ShowType.Modal) 弹分组选择
4. 用户选完分组 · 由 hrcs_permfilegrptree 自己处理 hrcs_permfilegrpmember 的 INSERT
```

### 5.4 按组织生成档案（genPermFiles · 后台任务）

```java
// 反编译 launchSyncPermfileJob L1071-L1102
1. RoleService.isAdmin() 必须是 HR 域管理员（双闸：HRAdminStrictPlugin + 这里）
2. 检查 syncPermFilesTaskId 缓存 · 防重复触发
3. 弹 hrcs_syncpermfile 收集 laborrelTypeIds（员工类型）
4. 收到回填 · 构造 JobInfo:
   - jobType = JobType.REALTIME
   - taskClassname = "kd.hr.hrcs.bussiness.task.SyncPermFilesTask"
   - clickClassName = "kd.hr.hrcs.bussiness.task.SyncPermFilesTaskClick"
5. dispatch(jobFormInfo) · 弹 hrcs_syncprocess 进度条
6. 任务完成 · closedCallBack(syncPermFilesTask) 显示 newCount/updCount
```

⚠️ **业务含义**：这是"按人事档案批量生成权限档案"的批量初始化操作 · 走苍穹调度任务（`kd.bos.schedule.api.JobInfo`）· 异步处理。**ISV 不要在 syncPermFilesTask 同事务里做下游写入** · 应该在 `afterExecuteOperationTransaction` 阶段或者订阅任务完成事件。

### 5.5 失效（disable）的复杂级联

```java
// 反编译 setEnable L767-L781
private int setEnable(String enable, List<Object> idList) {
    int success = 0;
    if (idList != null && idList.size() > 0) {
        // 1. 批量更新 hrcs_userpermfile.permfileenable
        success = serviceHelper.update(...).length;
        // 2. 如果是 disable · 调 RoleServiceHelper.disablePermfile 级联清理 hrcs_userrolerelat
        if (HRStringUtils.equals(PERFILE_STATUS_0, enable)) {
            RoleServiceHelper.disablePermfile(idList);
        }
    }
    HRPermCacheMgr.clearAllCache();  // 3. 清权限缓存（强制下次查询重读）
    return success;
}
```

⚠️ **关键约束**：disable 不只是改 `permfileenable` · 还会清空当前 archive 上挂的所有角色关联 + 全局权限缓存。**ISV 的禁用前置校验（CS-03）必须在 OP 的 `beforeExecuteOperationTransaction` 阶段做 · 进入此方法已不可逆**。

---

## 六、共用物理表分析（区分键）

`hrcs_permfilelist` 是**列表壳**（无物理表）· 但它驱动的下游表都有"区分键"：

| 物理表 | 类型 | 区分维度 |
|---|---|---|
| `hrcs_userpermfile` | 主档案 | `(user, org)` 业务唯一 · `permfileenable in ("0","1")` 软删除 |
| `hrcs_permfilegrp` | 分组树 | `parent + longnumber` 形成层级 · `enable=1` 仅显示生效分组 |
| `hrcs_permfilegrpmember` | 中间表 | `(permfilegrp, permfile)` 多对多 · 删除分组前必须先清空成员（反编译 `canDelGroup` L902-L918） |
| `hrcs_userrolerelat` | 角色关联 | `permfile.id` + `role.id` 区分 · `customenable` 决定数据范围属性 |

⚠️ **delete 联动清理**（`canDelGroup` L902-L918 · 删除分组前置校验）：
1. 分组下还有子分组 → 不允许删除（提示"分组包含下级分组"）
2. 分组下还有 member（关联档案）→ 不允许删除（提示"该分组存在权限档案"）

这是 **不级联** 设计 —— 标品要求用户先手动剥离档案再删分组 · 不会自动清。

---

## 七、Plugin 链概览（4 标品 + ISV 未挂）

完整 4 plugin 清单见 `_auto_plugin_registry.md`。这里只列对 ISV 扩展最关键的层次：

```
preOpenForm  (1 个)
  1. HRAdminStrictPlugin       ⭐ HR 领域管理员准入闸 · 11 hrcs 共用 · ISV 不要继承

beforeBindData (1 个)
  2. PermfilesListPlugin       ⭐ 主 ListPlugin · updateSearch + 树工具栏可见性

beforeItemClick (1 个)
  2. PermfilesListPlugin       ⭐ 工具栏按钮前置校验（选中行数）

beforeDoOperation (2 个)
  2. PermfilesListPlugin       ⭐ 19 按钮分发 · bar_new 拦截
  3. QueryListPlugin           标品多实体查询列表插件

afterDoOperation (1 个)
  2. PermfilesListPlugin       ⭐ enable/disable 后置消息 + billList 刷新

itemClick (1 个)
  2. PermfilesListPlugin       ⭐ btn_clearcache / refresh

closedCallBack (1 个)
  2. PermfilesListPlugin       ⭐ NEW_PERMFILE / syncPermFilesTask / exportUrl / confirmcallback_save

registerListener (1 个)
  2. PermfilesListPlugin       ⭐ TreeView.addTreeNodeQueryListener(this)

afterBindData (1 个)
  3. QueryListPlugin

OP 链（hrcs_userpermfile 主数据上）
  - PermFilesSaveOp            ⭐ 标品 save/enable/disable/delete/disable_new 共用 OP（jar 缺失 · 仅 plugin_registry 实证）
```

---

## 八、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/dynascheme/hjm）保持一致

### 8.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_hbjm_job_l` / `t_haos_adminorg_l`）。`hrcs_permfilelist` 自身无字段不涉及；下游 `hrcs_permfilegrp.name` 是 MuliLangTextField · 多语言由父模板承载（推断有独立 `t_hrcs_permfilegrp_l` · 待元数据探针确认）。**ISV 扩展 MuliLangTextField 字段时不要假设会自动有 _l 表 · 看实际元数据**。

### 8.2 反模式 · 继承场景专属类（PermfilesListPlugin 不要继承）

| 场景专属类（**禁继承**） | 推荐做法 |
|---|---|
| `PermfilesListPlugin` | 并列挂新 ListPlugin · 继承 `HRStandardTreeList` 或 `HRDataBaseList` |
| `PermFilesSaveOp` | 并列挂新 OP · 继承 `HRDataBaseOp` |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸 · ISV 不要继承 · 直接复用即可 |
| `HRStandardTreeList` | ✅ 可继承（HR 标准父类 · 已审核 SDK 白名单） |
| `HRDataBaseList` / `HRDataBaseOp` / `HRDataBaseEdit` | ✅ 可继承（HR SDK 白名单） |
| `HRDynamicFormBasePlugin` | ✅ 可继承 |
| `QueryListPlugin` | hrmp-hbp · 内部类 · ISV 不要继承（PR-001） |
| `AbsOrgBaseOp` | 组织域专属（非 hrcs）· 不在白名单 |

`PermfilesListPlugin / PermFilesSaveOp` 都是 hrcs 场景专属类 · ISV 不要继承 · 走并列挂或继承 SDK 白名单父类（参考 `_shared/platform_rules.json` PR-001）。

### 8.3 列表三层模型（list 壳 + 主实体 + 树实体）

`hrcs_permfilelist` 是 ListFormModel · 列表挂在数据实体上：
- **数据层过滤** → 挂 `hrcs_userpermfile`（主数据 · setFilter 应该挂这层）⚠️ 但本场景标品例外：标品的 `setFilter` 反而挂在了列表壳的 `PermfilesListPlugin` 上（反编译 L1030-L1037）—— 这是 hrcs 历史习惯。**ISV 跟标品对齐 · setFilter 也挂列表壳**
- **UI 外壳 / 工具栏** → 挂 `hrcs_permfilelist`（当前 `PermfilesListPlugin` 都挂这里）
- **F7 选择列表壳** → 一般有独立模板（待探针确认）

参考管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`） + tablist/treelist 三层模型。

### 8.4 非时序场景（与 dynascheme/jobhr 区分）

`hrcs_permfilelist` 驱动的 `hrcs_userpermfile` 是**普通基础资料**（非 HisModel）：
- 没有 `boid` / `iscurrentversion` / `sourcevid` / `bsed` / `bsled`
- 状态切换（permfileenable）走软删除 · 不是版本切换
- 删除走物理删（permfile + 级联 hrcs_userrolerelat）· 不是 datastatus 标记

**PR-008 / PR-009（HisModel 必带 iscurrentversion + boid）本场景不适用** —— 但下游 `hrcs_userrolerelat` 引用 user 的下游字段（`user.id` · `org.id`）时 · 如果 user/org 是时序资料 · 必须用 boid 而不是 id。

### 8.5 PR 引用速查

本场景定制必引：
- **PR-001** 并列挂不继承 · 是核心铁律
- **PR-002** RowKey 执行顺序规则 · ISV 插件可排在标品之前
- **PR-003** FormPlugin 用 getModel().setValue · OP 用 entity.set
- **PR-004** beginInit/endInit 防死循环（列表场景较少 · 但子表单 hrcs_userpermfile 编辑时可能用到）
- **PR-005** ID 生成用 kd.bos.id.ID（自建 hrcs_userrolerelat 行时）
- **PR-006** CodeRuleOp 业务侧配置（permfile 是否要编码规则视项目）
- **PR-007** 预置数据不可改 · `issyspreset=1` 的档案不能被 ISV 改
- **PR-010** OP 13 生命周期 · onAddValidators 注册校验
- **PR-011** BEC 走平台事件中心（**permfilelist 标品没发 BEC** · ISV 自建发布方时引用）

---

## 九、模型层对外 API（ServiceHelper 反编译实证）

| ServiceHelper | 关键方法 | 用途 |
|---|---|---|
| `HRBaseServiceHelper` | `query(fields, qf, order)` / `queryOriginalCollection(fields, qf)` / `queryOriginalOne(fields, id)` / `update(arr)` | HR 通用 dao · 反编译广泛使用（L292/L334/L1192 等 8+ 处） |
| `HRBaseDaoFactory` | `getInstance(entityName)` | dao 工厂 |
| `BusinessDataServiceHelper` | `newDynamicObject(entityName)` | 创建空 DO（反编译 L271） |
| `RoleService` | `commonWriteLogBeforeDoOp(args)` / `commonWriteLogNoOpKey(opKey, bridge, success, formId, appId)` / `addLogWithOpKey(...)` / `isAdmin()` | hrcs 角色域审计日志 + admin 判定 · 反编译 12+ 处 |
| `RoleServiceHelper` | `showForm(formId, params, callback, showType, ...)` / `disablePermfile(idList)` | hrcs 角色域 helper · 失效档案级联清理 · 反编译 L584/L776 |
| `RoleMemberAssignServiceHelper` | `getPermFileById(permfileId)` | 按 ID 加载档案（含 user.* 派生字段） · 反编译 L573/L1198 |
| `HRRolePermHelper` | `showRoleF7()` / `queryViewableRoles(userId)` | 角色 F7 + 当前用户可见角色集 · 反编译 L1176 |
| `HRPermServiceHelper` | `getUserGroupMinLevel()` / `getUserPermFile()` | 当前用户管理员组等级 + 用户权限档案 ID 列表 · 反编译 L1032-L1034 |
| `HRPermCacheMgr` | `clearAllCache()` / `clearAllManageCache()` | 全局/管理员缓存清理 · 反编译 L506-L507/L779 |
| `PermissionServiceHelper` | `isAdminUser(userId)` / `checkPermission(userId, appId, formId, permItemId)` | 平台权限 helper · 反编译 HRAdminStrictPlugin L42-L43 + L991 |
| `PermCommonUtil` | `isCosmicUser(userId)` / `getAppIdFromSuspectedAppNum(appNum)` | 平台权限工具 · 反编译 HRAdminStrictPlugin L43 |
| `PermHelper` | `getUserRelatesByPermFields(ids)` | 反编译 L614 |

特别注意：`RoleService` / `RoleServiceHelper` / `RoleMemberAssignServiceHelper` / `HRRolePermHelper` / `HRPermServiceHelper` / `HRPermCacheMgr` / `PermHelper` 都属于 hrcs 域内部 helper（包路径 `kd.hr.hrcs.bussiness.*`）· 不一定有 `@SdkPublic` 注解。**调用前必查 sdk 白名单**（违反 `cosmic_sdk_annotation_whitelist.md` 铁律）。

ServiceHelper 类需走 SDK 白名单审核才能在 ISV 代码里调用（部分类未标 `@SdkPublic` · 视作内部 API）。**调用前查 `_shared/platform_rules.json` 跟 `_sdk_audit/sdk_registry.json`**。

---

## 十、模型设计的边界与扩展建议

| 扩展场景 | 推荐做法 | 风险 |
|---|---|---|
| 加自定义字段（如"档案备注"在 hrcs_userpermfile） | modifyMeta add field 到 `hrcs_userpermfile` · ISV 前缀 | 低 |
| 加分组属性（在 hrcs_permfilegrp 加"业务线"） | modifyMeta add field 到 `hrcs_permfilegrp` · ISV 前缀 | 低 |
| 监听 enable/disable 后做下游同步 | ISV 自建 OP 挂 enable/disable 的 afterExecuteOperationTransaction（PR-010） | 中 |
| save 前业务校验（如"同 org 同 user 不能重复"） | ISV 自建 Validator 挂 onAddValidators （PR-010） | 低 |
| 列表 setFilter 增加权限过滤 | ISV 自建 ListPlugin 挂 hrcs_permfilelist · super 后追加 QFilter | 中 |
| 共用 HRAdminStrictPlugin 准入闸 | 配置即可 · 不要修改 | 0 |
| 删除前查下游引用 | 自建 Validator 挂 delete · 查 hrcs_userrolerelat / hrcs_permfilegrpmember 反向引用 | 中 |
| 批量分组扩展 | ISV ListPlugin 挂 batchgroup opKey · 写 hrcs_permfilegrpmember | 中 |

详见 `06_customization_solutions.md`。
