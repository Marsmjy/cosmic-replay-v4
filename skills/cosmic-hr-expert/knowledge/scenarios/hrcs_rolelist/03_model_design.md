# 模型设计 · HR 角色管理列表 (hrcs_rolelist)

> **状态**: 🟢 基于 `main_form.xml` (QueryListModel 元数据实抓) + `_auto_plugin_semantics.md` (2 反编译类) + `HRRoleListPlugin.java` (1765 行)
> **confidence**: verified
> **数据源**: OpenAPI `getFormSchema(0SXDNZK1PW66)` + CFR 反编译 `kd.hr.hrcs.formplugin.web.perm.role.*` + `kd.hr.hrcs.formplugin.web.perm.hradmin.*` (2026-04-28)

---

## ⭐ 关键业务事实 · 列表视图 + 6+ 张物理表的"角色权限矩阵入口"

`hrcs_rolelist` **不是一张数据实体** —— 它是一张 **`QueryListModel`（查询列表模型）类型的 UI 表单**，菜单挂载到"HR通用服务 / 权限管理 / 角色管理"。从反编译类 `HRRoleListPlugin` 实证（L260-L297），它继承自 `HRStandardTreeList`，并以 `perm_rolegroup` 为 ENTITY_BOSROLEGRP 类型构造（L296）：

```java
public final class HRRoleListPlugin extends HRStandardTreeList implements TreeNodeQueryListener {
    private static final String ENTITYTYPE_BOSROLEGRP = "perm_rolegroup";
    private static final String ENTITYTYPE_HRROLEGRP  = "hrcs_rolegrp";

    public HRRoleListPlugin() {
        super(ENTITYTYPE_BOSROLEGRP, ID_ROOTNODE_PRESET, false);
    }
}
```

- `main_form.xml` `ModelType = QueryListModel`（L9）· FormId = `hrcs_rolelist` · EntityId = `0SXDKS4AO4Q0` · BizappId = `15NPDX/GJFOO`（hrcs）
- 实际查询的字段都来自 `perm_role`（标品 BOS 角色表）· 列表过滤字段示例 `perm_role.issystem` / `perm_role.enable` / `perm_role.group.name`（main_form.xml L78-L79 / L411-L412 / L421）
- 树（左侧分组树）走 `hrcs_rolegrp` 表（HR 角色分组）· 与 `perm_rolegroup`（BOS 标准角色分组）形成"双层分组"模型：HR 域只显示自己注册过的分组（`HRRoleListPlugin.getTreeViewCollection` L324-L338）

`scene_doc.fields = []` 因为 `hrcs_rolelist` 是 list 视图、字段从 `main_form.xml` 实证而非 `scene_doc.json`。**所有"角色字段"实际属于 `perm_role` / `hrcs_role`/ 子配置表**，下方第三节会逐一列出。

> **物理表数量盘点**（来自 `HRRoleListPlugin.doDeleteHrRole` L617-L637 + `doDeleteBosRole` L703-L732）：单条角色一旦被删除，平台标品**同时清理 17 张物理表**（`hrcs_userrole` / `hrcs_userrolerelat` / `hrcs_userdatarule` / `hrcs_userdataruleentry` / `hrcs_userbdruleentry` / `hrcs_rolebu` / `hrcs_role` / `hrcs_roledimension` / `hrcs_rolefield` / `hrcs_rolebdruleentry` / `hrcs_roledatarule` / `hrcs_roledataruleentry` / `hrcs_roleopenscope` / `hrcs_roleassignscope` / `perm_roleperm` / `perm_rolefieldperm` / `perm_userrole` / `perm_role`）—— 这是为什么"删除角色"在本场景比一般基础资料严格得多。

---

## 一、苍穹列表三层模型（参考管道坑 14.1 · tablist|treelist）

本场景属于"标准树形列表 (HRStandardTreeList)"形态。`hrcs_rolelist` 的元数据由**两到三层独立元数据**组成，Claude 做列表类 CS 时必须区分挂哪层：

| 层 | 元数据类型 | 本场景 formNumber | 职责 |
|---|---|---|---|
| **数据实体 (左)** | BasedataModel | `hrcs_rolegrp`（HR 角色分组 · 树节点） | 维护 HR 域看到的角色分组 |
| **数据实体 (右)** | BasedataModel | `perm_role`（标品 BOS 角色 · 列表行） | 真正的"角色"主体 · 编码/名称/启用/管理员组等 |
| **列表表单壳** | QueryListModel（**本 form**） | `hrcs_rolelist` | 树+列表 UI 壳 + 分组维护按钮 + 角色批量按钮 |
| **角色编辑/详情** | DynamicFormModel | `hrcs_modifyrole`（点击列表行 hyperLink 跳转） | 单角色权限矩阵编辑 (功能权限/数据权限/字段权限) |
| **新建角色** | DynamicFormModel | `hrcs_newrole`（new opKey 跳转） | 新建角色简单表单 |

**插件挂载职责分工**：
- **列表树 / 列表行 / 列表按钮 / setFilter / addLogNoOpKey 准入** → 挂 `hrcs_rolelist`（当前 `HRRoleListPlugin` 就在这里）
- **角色权限矩阵详情** → 挂 `hrcs_modifyrole`（不在本场景范围）
- **新建角色** → 挂 `hrcs_newrole`（不在本场景范围）
- **HR 领域准入** → 挂 `hrcs_rolelist` 的 `HRAdminStrictPlugin.preOpenForm`（11 hrcs 表单共用）

参考：管道坑 14.1（`docs/NEW_SCENARIO_PIPELINE.md`）+ `tablist`/`treelist` 三层模型。

---

## 二、继承链 · QueryListModel + 树形列表

### ModelType 实证

`main_form.xml` L9 `<ModelType>QueryListModel</ModelType>` —— 这是苍穹"查询列表"模板（区别于 BillFormModel / BaseFormModel）：

- **QueryListModel** = 列表壳模板，本身不存数据，只是查询/展示其他基础资料的视图
- 列表行的来源由 `BeforeCreateListDataProviderArgs` 决定（`HRRoleListPlugin.beforeCreateListDataProvider` L1762-L1764 注入了 `HRRoleProvider` 自定义数据源 · 列表数据从 `perm_role` 表查）
- ❌ 不是 BillFormModel —— 没有 status/audit/unaudit/submit/unsubmit 工作流
- ❌ 不是 HisModel —— **本场景 0 处时序版本字段引用** （已 grep 实证 · 详见第六节）

### 类继承链（实证 _auto_plugin_semantics.md）

```
HRRoleListPlugin                          ← 业务核心 · L260-L1765
    ↓ extends
HRStandardTreeList                        ← HR 通用：双 entity 树 + 列表 + 分组 CRUD（kd.hr.hbp.formplugin.web）
    ↓ extends
HRStandardTreeListBase  (推测 · 未在 jar)
    ↓ extends
AbstractTreeListPlugin (kd.bos.list.plugin)
    ↓ extends
AbstractListPlugin
    ↓ extends
AbstractFormPlugin
    ↓ implements
IFormPlugin
```

**接口实现**：`implements TreeNodeQueryListener` —— 实现树节点查询监听（用于 `queryTreeNodeChildren` L495-L497）

### HR 平台准入闸（共用插件 · 不要继承）

`HRAdminStrictPlugin`（L27-L58）继承自 `HRDynamicFormBasePlugin`，11 个 hrcs 表单共用一份。**ISV 想做"HR 管理员"前置校验时不要继承它，直接复用即可** —— 因为：
- 它已经挂在 `hrcs_rolelist` 上
- 它只暴露一个 `preOpenForm` 入口，没有可扩展点
- 它的 `isHrAdmin` 是静态方法，可以直接 `HRAdminStrictPlugin.isHrAdmin()` 调用

---

## 三、字段表（`main_form.xml` 实抓 · 来自 `perm_role` / `hrcs_role` 引用）

> ⚠ `scene_doc.fields = []` —— 真实字段在 `perm_role` 主体表上，本表单只是它的列表视图。下表是 `main_form.xml` 列定义实抓 + 反编译里读写的字段汇总。

### 3.1 列表过滤列（CommonFilterColumn / FilterColumn）

| Field Key | 类型 | 业务含义 | 默认值 | 来源 |
|---|---|---|---|---|
| `perm_role.issystem` | CommonCheckBoxFilterColumn (枚举) | 系统预置 (1=是 / 0=否) | - | main_form.xml L77-L92 |
| `perm_role.enable` | FilterColumn | 启用状态 (1=启用 / 0=禁用) | "1" | `HRRoleListPlugin.filterContainerInit` L407-L415 |
| `perm_role.group.name` | FilterColumn | 角色分组名（F7 二级过滤） | - | `filterContainerBeforeF7Select` L418-L432 |
| `perm_role.number` | FilterColumn | 编号搜索 | - | main_form.xml + standard list |
| `perm_role.name` | FilterColumn | 名称搜索 | - | main_form.xml + standard list |

### 3.2 列表行字段（`perm_role` 主体）

| Field Key | 类型 | 业务含义 | ISV 可改 | 备注 |
|---|---|---|---|---|
| `id` | BigIntField | 角色 ID | 🔒 | 主键 · `LiSelectedRow.getPrimaryKeyValue()` 取它 |
| `number` | TextField | 角色编号 | ✅ 业务自建 | 系统预置（`issystem=1`）不可改 PR-007 |
| `name` | MuliLangTextField | 角色名称 | ✅ | - |
| `enable` | BillStatusField | 启用状态 (0/1/10) | ⚠️ 黄区 | `HRRoleListPlugin.enableRole` L1310 / `disableRole` L1378 直接改 |
| `issyspreset` | CheckBoxField | 是否预置 | 🔒 | `HRRoleListPlugin.checkCanDel` L1503 阻止删除预置 |
| `issystem` | CheckBoxField | 是否系统角色 | 🔒 | 列表过滤用 |
| `group` | BasedataField → `perm_rolegroup` | 角色分组 | ✅ | 树节点对应字段 |
| `createadmingrp` | BasedataField → `bos_admingrp` | 创建管理员组 | 🔒 | 决定哪些用户能改这个角色（`canOperateRole` L1075-L1099） |
| `usescope` | ComboField | 使用范围 (0=私有 / 其他=公开) | ⚠️ | 0=只 createadmingrp 能用 |

### 3.3 树节点字段（`hrcs_rolegrp` + `perm_rolegroup`）

| Field Key | 类型 | 业务含义 | 来源 |
|---|---|---|---|
| `id` | BigIntField | 分组 ID | - |
| `name` | MuliLangTextField | 分组名称 | - |
| `parent` | ParentBasedataField | 父分组（自引用） | `HRRoleListPlugin.PARENT="parent"` L265 |
| `longnumber` | TextField | 长编码（用于子树查询） | `LONGNUMBER="longnumber"` L263 + `getSubGrp` L395-L405 |
| `isleaf` | CheckBoxField | 是否叶子 | `getTreeViewByParent` L1638 |

### 3.4 角色权限子配置表（删除时级联清理 · 见第五节）

| 物理表 / formId | 类型 | 业务含义 | 主外键 |
|---|---|---|---|
| `hrcs_role` | 子配置 | HR 域角色（与 perm_role 1:1，扩展 HR 字段） | `id = perm_role.id` |
| `hrcs_userrolerelat` | 关联 | 用户-角色关联 | `role = perm_role.id` |
| `hrcs_userrole` | 关联 | 用户角色（对接 perm_userrole） | `userrolerealt = hrcs_userrolerelat.id` |
| `hrcs_userdatarule` | 配置 | 用户数据规则 | `userrolerelate = hrcs_userrolerelat.id` |
| `hrcs_userdataruleentry` / `hrcs_userbdruleentry` | 子配置 | 数据规则分录 / 基础资料规则分录 | `userdatarule = hrcs_userdatarule.id` |
| `hrcs_rolebu` | 配置 | 角色 BU 范围 | `role = perm_role.id` |
| `hrcs_roledatarule` | 配置 | 角色数据规则 | `role = perm_role.id` |
| `hrcs_roledataruleentry` / `hrcs_rolebdruleentry` | 子配置 | 角色数据规则分录 | `roledatarule = hrcs_roledatarule.id` |
| `hrcs_roledimension` | 配置 | 角色维度 | `role = perm_role.id` |
| `hrcs_rolefield` | 配置 | 角色字段权限 | `role = perm_role.id` |
| `hrcs_roleopenscope` | 配置 | 角色公开范围 | `roleid = perm_role.id` |
| `hrcs_roleassignscope` | 配置 | 角色分配范围（管理员组可改性） | `roleid = perm_role.id` + `ismodifiable=1` |
| `perm_role` | BOS 标准 | 标准角色主体 | `id` |
| `perm_roleperm` | BOS 标准 | 角色功能权限 | `roleid = perm_role.id` |
| `perm_rolefieldperm` / `perm_fieldperm` | BOS 标准 | 角色字段权限（双层） | `role` / `fieldperm.id` |
| `perm_roledataperm` / `perm_dataperm` | BOS 标准 | 角色数据权限（双层） | `role` / `datapermid.id` |
| `perm_userrole` | BOS 标准 | 用户角色绑定 | `role = perm_role.id` |
| `T_Perm_BizRoleComRole` | BOS 标准 | 业务角色复用关系 | `FRoleID = perm_role.id` （`enable/disable` 时同步刷 FEnable · L1401） |

### 3.5 反查 / 业务相关表

| 表 | 在本场景中的角色 |
|---|---|
| `hrcs_dynascheme.roleentry.role` | **下游引用方** · `getRefrencedRoles` L1556-L1568 反查"动态授权方案"是否引用此角色 · 引用了不能删（CS-04 核心场景） |
| `hrcs_permfilelist` | `RoleService.addLogNoOpKey` 写日志的 formId · L694 · 删除日志归集到此 |

---

## 四、3 个 ID 概念辨析（管道坑 14.2）

苍穹的 ID 在角色管理场景里有 3 套并存，**ISV 写代码必须分清是用哪个**：

| 概念 | 字段 | 例子 | 用途 |
|---|---|---|---|
| **角色主键 ID** | `perm_role.id` / `hrcs_role.id` | `1755...` | 单角色操作（删/改/启用） |
| **角色编号** | `perm_role.number` | `HR_ADMIN_001` | 业务编码（自动/手动）· `RoleService.addLogNoOpKey` 写日志带它 L694 |
| **管理员组 longnumber** | `perm_admingrp.longnumber` | `0001.0001.0002` | 树形结构遍历过滤（`HRRolePermHelper.queryUserAdminGroups` 返回 Map<id, longnumber>，本场景 `canOperateRole` L1075 / `enableRole` L1259 / `deleteRoleInfo` L1436 都用） |

⚠️ **`HRRolePermHelper.queryViewableRoles(currUserId)` 返回的是当前用户**可视**角色 id 集合**（`HRRoleListPlugin.setFilter` L367-L372），不是全部角色。这就是为什么不同管理员看到的列表行不同 —— 这是**标品已有的"行级权限"**。

```java
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    long currUserId = RequestContext.get().getCurrUserId();
    QFilter filter = new QFilter("id", "in", HRRolePermHelper.queryViewableRoles(currUserId));
    setFilterEvent.getQFilters().add(filter);
}
```

---

## 五、删除级联清理路径（17 张表 · 唯一最重要的数据流）

`HRRoleListPlugin` 处理 delete opKey 的代码在 L1436-L1474（`deleteRoleInfo`）+ L505-L537（`doDeleteGroup`）+ L617-L637（`doDeleteHrRole`）+ L703-L732（`doDeleteBosRole`）。删除一个角色后：

```
delete opKey
  ↓ HRRoleListPlugin.beforeDoOperation (L989-L1003)
    → handleBeforeOperatetionEvent → "delete" → deleteRoleInfo (L1436)
      ↓ checkCanDel (L1488-L1553)
        ❌ 预置角色 issyspreset=true → 拦截 L1512-L1517
        ❌ hrcs_dynascheme.roleentry 引用此角色 → 拦截 L1506-L1511 ⭐ CS-04 重头
        ❌ 当前管理员不在 createadmingrp 范围 → 拦截 L1521-L1528
        ❌ 角色未禁用（enable!=0）或成员不为空 → 拦截 L1534-L1538
      ↓ confirmCallBack(group_bar_del) → confirmDeleteRoleInfo (L558-L599)
        ↓ deleteBosRoleById (L662-L700)
          → doDeleteBosRole (L703-L732)
            DELETE FROM perm_roleperm WHERE roleid IN (...)        # 1
            DELETE FROM perm_fieldperm WHERE id IN (...)           # 2 通过 perm_rolefieldperm 反查
            DELETE FROM perm_rolefieldperm WHERE role IN (...)     # 3
            DELETE FROM perm_dataperm WHERE id IN (...)            # 4 通过 perm_roledataperm 反查
            DELETE FROM perm_roledataperm WHERE role IN (...)      # 5
            DELETE FROM perm_userrole WHERE role IN (...)          # 6
            DELETE FROM perm_role WHERE id IN (...)                # 7
            CacheMrg.clearCache(rolePermType, roleId)              # 8 清缓存
        ↓ deleteHrRoleById (L602-L614)
          → doDeleteHrRole (L617-L637)
            DELETE FROM hrcs_userrole WHERE userrolerealt IN (...) # 9
            DELETE FROM hrcs_userdataruleentry / userbdruleentry / userdatarule  # 10-12
            DELETE FROM hrcs_userrolerelat WHERE id IN (...)       # 13
            DELETE FROM hrcs_rolebu WHERE role IN (...)            # 14
            DELETE FROM hrcs_roledataruleentry / rolebdruleentry / roledatarule  # 15-17
            DELETE FROM hrcs_rolefield WHERE role IN (...)         # 18
            DELETE FROM hrcs_role WHERE id IN (...)                # 19
            DELETE FROM hrcs_roledimension WHERE role IN (...)     # 20
            DBService.deleteByFilter(hrcs_roledimgroup) WHERE role IN (...) # 21
            DELETE FROM hrcs_roleopenscope WHERE roleid IN (...)   # 22
            DELETE FROM hrcs_roleassignscope WHERE roleid IN (...) # 23
        ↓ RoleService.addLogNoOpKey + ILogService.addBatchLog (L692-L697)
        ↓ HRPermCacheMgr.clearAllCacheAsync() / .clearAllCacheAndNotifyAsync(roleIds) (L1409 / L1432)
```

⚠️ **物理表实际清理 23 行 SQL（统计了反查 perm_fieldperm/perm_dataperm 各算一行）**。所以"删除一个角色"在性能上不便宜 —— ISV 加自定义"删除前置 / 后置"插件时必须考虑这套链路（CS-04）。

---

## 六、HisModel 时序辨析（本场景 **不是** 时序模型）

> ✅ **grep 实证**：执行 `grep -E "iscurrentversion|HisModel|boid"` 检查 `scene_doc.json` + `HRRoleListPlugin.java` + `HRAdminStrictPlugin.java` —— **0 处命中**。

- `hrcs_rolelist` ModelType = **QueryListModel**，不是 HisModel
- `perm_role` 是 BOS 标准基础资料，没有 `boid` / `iscurrentversion` / `bsed` / `bsled`
- `hrcs_role` 是 HR 域扩展角色表，只是 1:1 跟 perm_role 拓扑相同，也没时序字段
- 所以 **PR-008 / PR-009 在本场景不适用** —— 删除/启用/禁用都是直接物理删/物理改 enable

ISV 加字段时不要假设字段会被多版本快照保留 —— 改了就生效，不可逆。需要"历史变更可追溯"必须自己加 `_l`/_h 等历史表 + 触发器。

---

## 七、Plugin 链概览（仅 2 标品 · ISV 未挂）

完整 2 plugin 清单见 `_auto_plugin_registry.md`。本场景**没有 OP 插件**（QueryListModel 不走 OP 链 · 所有 opKey 都是 list/form 范畴）。

```
preOpenForm (1 个)
  1. HRAdminStrictPlugin       ⭐ HR 领域管理员准入闸 · 11 hrcs 表单共用 · 非 HR 管理员直接拒

beforeBindData (1 个)
  2. HRRoleListPlugin          ⭐ 主 ListPlugin · updateSearch / setSearchEmptyText

beforeItemClick (1 个 · 按钮点击前)
  2. HRRoleListPlugin          ⭐ 校验 "btn_batchgroup / btn_allocprem / bar_assignmember / exportroleperm / baritem_alteruserperm" 等

beforeDoOperation (1 个)
  2. HRRoleListPlugin          ⭐ delete/enable/disable/bar_assignmember/bar_exportrole/bar_initrole/new/bar_copy 入口

afterDoOperation (1 个)
  2. HRRoleListPlugin          ⭐ 清 itemKey pageCache

itemClick (1 个)
  2. HRRoleListPlugin          ⭐ refresh

closedCallBack (1 个)
  2. HRRoleListPlugin          ⭐ 回调 export URL（导出权限场景）

registerListener (1 个)
  2. HRRoleListPlugin          ⭐ 注册 TreeNodeQueryListener / ListRowClickListener

createTreeListView (1 个)
  2. HRRoleListPlugin          ⭐ 替换为 CommonTreeListView

setFilter (1 个 · 数据行级权限闸)
  2. HRRoleListPlugin          ⭐ 加 viewableRoles 过滤（非常重要 · 决定列表能看到哪些角色）

filterContainerInit (1 个)
  2. HRRoleListPlugin          ⭐ enable=1 默认值

filterContainerBeforeF7Select (1 个)
  2. HRRoleListPlugin          ⭐ group.name F7 过滤（限 hrcs_rolegrp 注册过的）

billListHyperLinkClick (1 个)
  2. HRRoleListPlugin          ⭐ 点击行 → 跳转 hrcs_modifyrole 编辑页

beforeCreateListDataProvider (1 个)
  2. HRRoleListPlugin          ⭐ 注入 HRRoleProvider 自定义数据源
```

---

## 八、对比：rolelist vs dynascheme（同应用 hrcs · 风险点不同）

| 维度 | hrcs_rolelist | hrcs_dynascheme |
|---|---|---|
| ModelType | QueryListModel（列表壳） | BillFormModel（业务单据） |
| HisModel | ❌ 不是时序 | ✅ 时序（boid/iscurrentversion） |
| 工作流 | ❌ 无 status (没 submit/audit) | ✅ A/B/C 状态机 |
| 物理表数 | 主体 perm_role + 17 张子表（删除时级联） | 主表 + 9 张分录/反查表 |
| 反编译类数 | 2 类 (HRRoleListPlugin / HRAdminStrictPlugin) | 7 类 (Plugin/Op/SaveSubmitOp/AuditOp/ConfirmChangeOp/ListPlugin/HRAdminStrictPlugin) |
| OP 插件 | **无** | 4 个 OP（DynaAuthSchemeOp/SaveSubmitOp/AuditOp/ConfirmChangeOp） |
| 自定义按钮 | 12 个（btn_allocprem/btn_batchgroup/bar_assignmember/exportroleperm/inituserperm/bar_initrole/bar_exportrole/bar_copy/bar_disable/bar_enable/baritem_alteruserperm + 树工具栏 btnnew/btnedit/btndel） | 8 个（setadminrange/assignrecord/audithisconfirmchange/checkroledetails/addrole 等） |
| BEC 标品发 | ❌ 0 处 | ❌ 0 处 |
| 共用插件 | HRAdminStrictPlugin | HRAdminStrictPlugin |
| 核心 ISV 风险 | ❌ 把过滤改窄了导致管理员看不见自己的角色（`setFilter`） / 加自定义按钮没在 `beforeItemClick` 注册 | HisModel 时序版本污染 / authaction 切换误清规则 |

---

## 九、平台命名规则速查（跨场景对齐）

> ⚠️ Claude 生成代码前必读 · 跟其他场景（admin_org/hjm/dynascheme/hbpm）保持一致

### 9.1 多语言表 `_l` 结尾

苍穹多语言子表统一以 `_l` 结尾命名（如 `t_perm_role_l` / `t_hrcs_rolegrp_l`）。`hrcs_rolelist` 列表显示的多语言字段（`name` 多语言）实际由 BOS 标品 `perm_role` 自动维护 `t_perm_role_l` 子表。**ISV 在 `perm_role` 上加 MuliLangTextField 时**：
- ❌ **绝对不能改 `perm_role` 元数据**（标品 form · ISV 隔离铁律 PR-001 · 详见 `isv_ownership_redline.md`）
- ✅ 要扩字段必须**新建 ISV 扩展元数据**继承 `perm_role` · 然后在子元数据上加字段（参考 `kb_isv_ownership_rules.md`）
- ⚠️ MuliLangTextField 默认会自动建 `_l` 表 · 但跨子实体扩展时多语言可能落到父表 `_l` 还是子表 `_l` 需要看 ParentId 配置

### 9.2 反模式 · 继承场景专属类（**禁继承**）

| 场景专属类 | 推荐做法 |
|---|---|
| `HRRoleListPlugin` ⭐ 本场景核心 | 并列挂新 ListPlugin · 继承 `HRDataBaseList` 或 `AbstractListPlugin`；如果业务需要 super 调，必须问"是不是把代码逻辑搬到自己类更简单" |
| `HRStandardTreeList`（kd.hr.hbp.formplugin.web） | 通用基类 · 在 SDK 白名单中（参考 `cosmic_hr_sdk_whitelist_audit.md`），可继承；但要业务上"复制 + 改"时优先 ISV 类继承 `AbstractTreeListPlugin` 而非这个基类 |
| `HRAdminStrictPlugin` | hrcs 11 表单共用准入闸 · ISV **不要继承** · 直接复用即可（已挂 `hrcs_rolelist`） |
| `CommonTreeListView`（HRRoleListPlugin 内部用 L257 / L289） | 列表树视图实现类 · ISV 不要继承 · 用 `AbstractTreeListView` 父类 |
| `HisModelOPCommonPlugin` / `HisUniqueValidateOp` / `HisModelFormCommonPlugin` / `HisModelListCommonPlugin` | @SdkInternal 平台时序内部类 · ISV 不得继承（**本场景不是时序所以不适用** · 但 dynascheme 等同应用其他 form 适用） |
| `AbsOrgBaseOp` | 组织域专属 · 不在白名单 · 用 `HRDataBaseOp` 代替 |

### 9.3 列表三层模型（list / treelist / tablist）

苍穹的列表外形主要 3 类：
- **list（普通列表）**：单 entity · 单 schema · 列表壳 + 行
- **treelist（树形列表 · 本场景）**：左 tree（一个 entity） + 右 list（另一个 entity） · 用 `HRStandardTreeList` 基类
- **tablist（标签页列表）**：列表上方 N 个 tab · 每个 tab 切换不同 filter

`hrcs_rolelist` 是 **treelist** 形态：左侧 `hrcs_rolegrp` 树（HR 注册分组） · 右侧 `perm_role` 列表行。ISV 写自定义列表时不要把 list 模式套到 treelist 上（看不到 tree） · 反之也一样。

### 9.4 perm_role / hrcs_role 父子表关系

```
perm_role (BOS 标品)
  + id (PK)
  + number / name / enable / issyspreset / issystem / group / createadmingrp / usescope ...
  + (BOS 通用字段：creator / modifier / createtime / modifytime ...)

hrcs_role (HR 扩展)
  + id = perm_role.id (1:1 同 ID 关联)
  + (HR 域专属字段：可视范围 / 数据规则继承策略 / ...)
```

⚠️ **`hrcs_role` 不是 ISV 扩展元数据 · 是标品 HR 域元数据**（见 `_auto_plugin_semantics.md`：`HRRoleListPlugin.doDeleteHrRole` L632 直接 DELETE `hrcs_role`）。ISV 自己要给"角色"加字段不能改 `hrcs_role` —— 必须新建 ISV 扩展继承 `perm_role`（PR-001 / `isv_ownership_redline.md` 铁律）。
