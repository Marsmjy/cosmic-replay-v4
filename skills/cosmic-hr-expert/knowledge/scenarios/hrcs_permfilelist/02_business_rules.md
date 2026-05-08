# 业务规则 · 用户授权（hrcs_permfilelist）

> **状态**: 🟢 基于反编译 `PermfilesListPlugin.java`/`HRAdminStrictPlugin.java` + `_auto_operations.md`（23 opKey · 0 元数据 formRule/bizRule）
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI listOperations · 2026-04-28

---

## ⭐ 一、规则全景图

`hrcs_permfilelist` 的元数据规则（formRule / bizRule）层 **0 条**（_auto_rules.md / opkeys_index.json 都为 0）—— **所有业务规则都写在 Java 反编译层**。这与 `hrcs_dynascheme`（2 formRule）不同 · permfilelist 是纯 Java 驱动场景。

| 规则类型 | 数量 | 说明 |
|---|---|---|
| 元数据 formRule | **0** | 列表壳没有元数据规则 |
| 元数据 bizRule | **0** | 列表壳没有元数据业务规则 |
| 元数据 validation（OP 上挂的） | **3** | save / delete / disable_new opKey 各挂 1 FormValidate（_auto_operations.md L84/L109/L132） |
| Java FormPlugin 隐式规则 | **30+** | 反编译 PermfilesListPlugin 各 lifecycle 内部规则 |
| Java OP 校验（PermFilesSaveOp） | ❓未知 | jar 缺失 · 但根据 opKey validations 推断有 3 个 FormValidate |
| 业务变量 / 阈值常量 | **20+** | 反编译 PermfilesListPlugin 类常量字段 |

---

## 二、元数据级 validation 清单（实抓）

来自 `_auto_operations.md` opKey validations：

| opKey | RuleType | Validation Id | 说明 |
|---|---|---|---|
| `save` | （此 opKey 0 validation · plugin 1 个 PermFilesSaveOp） | - | 校验在 PermFilesSaveOp 内部 |
| `enable` | FormValidate | `08VSPFHHJ4PU` | 合法性校验 · 名字不详（待获取 ruleDetail） |
| `delete` | FormValidate | `08VTK+YML3MU` | 合法性校验 |
| `disable_new` | FormValidate | `0===1HWWTAJR` | 合法性校验（注意是 disable_new opKey · 不是 disable） |

⚠️ 这 3 个 FormValidate 没有 errorMessage / preCondition 字段在我抓到的数据里 · 是平台启发式生成的"合法性校验"占位 · 真实业务规则在 PermFilesSaveOp 反编译里（jar 缺失 · 暂不可见）。

---

## 三、HRAdminStrictPlugin 准入规则（反编译实证）

```java
// HRAdminStrictPlugin.preOpenForm L29-L37 + showMesIfUserIsNotAdmin L39-L53
public void preOpenForm(PreOpenFormEventArgs args) {
    super.preOpenForm(args);
    FormShowParameter fsp = args.getFormShowParameter();
    // F7 lookup 模式（被引用方）跳过校验
    if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) {
        return;
    }
    HRAdminStrictPlugin.showMesIfUserIsNotAdmin(args);
}

public static void showMesIfUserIsNotAdmin(PreOpenFormEventArgs e) {
    long userId = RequestContext.get().getCurrUserId();
    boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);              // 平台超管？
    boolean isCosmic = PermCommonUtil.isCosmicUser(userId);                     // 苍穹技术管理员？
    if (!isAdmin && !isCosmic) {
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。");
        return;
    }
    if (!HRAdminStrictPlugin.isHrAdmin()) {                                     // HR 域管理员？
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。");
        return;
    }
}
```

### 规则 R-PreOpen-1：HR 领域管理员准入（双闸）

| ID | R-PreOpen-1 |
|---|---|
| 触发 | preOpenForm |
| 条件 | 非 lookup 模式打开 |
| 校验逻辑 | `(isAdmin OR isCosmic) AND isHrAdmin()` |
| 失败行为 | `e.setCancel(true)` · 弹错"您无法访问该功能，因为您不是HR领域管理员。" |
| 实证 | HRAdminStrictPlugin.java L39-L53 |

⚠️ 这是 **hrcs 11 个表单共用准入闸**（admingroup/userpermfile/role/...）。ISV 不要继承此类 · 配置即可。

---

## 四、PermfilesListPlugin Java 规则清单（30+ 条 · 反编译实证）

按生命周期分组：

### 4.1 preOpenForm 规则

无（HRAdminStrictPlugin 已处理 · PermfilesListPlugin 不重复）

### 4.2 beforeBindData 规则（L252-L255）

| ID | 规则 | 触发 | 实证 |
|---|---|---|---|
| FP_BBD1 | 调用 super.beforeBindData | 列表初始化 | L253 |
| FP_BBD2 | 调用 updateSearch() · 设搜索框占位 "请输入权限档案组名称。" | 列表初始化 | L254 + L257-L262 |

### 4.3 registerListener 规则（L1018-L1022）

| ID | 规则 | 触发 | 实证 |
|---|---|---|---|
| FP_RL1 | 注册 TreeView.addTreeNodeQueryListener(this) · 实现按需懒加载子节点 | 列表初始化 | L1018-L1022 |

### 4.4 beforeItemClick 规则（L344-L386）

| ID | 规则 | 触发 itemKey | 实证 |
|---|---|---|---|
| FP_BIC1 | btn_batchgroup · 选中 0 行 → 报错"请选择要执行的数据" | btn_batchgroup | L351-L353 |
| FP_BIC2 | btn_allocprem · 选中 0 行报错 / 选中 >1 行报"请选择一个用户" | btn_allocprem | L355-L357 + L388-L404 |
| FP_BIC3 | enable · 选中 0 行报错 | enable | L359-L361 |
| FP_BIC4 | disable · 选中 0 行报错 | disable | L363-L365 |
| FP_BIC5 | baritem_assignperm · 选中 0/>1 报错 | baritem_assignperm | L367-L369 |
| FP_BIC6 | btn_copyperm · 选中 0 行报"请选择一个用户。" / >1 行报同上 | btn_copyperm | L371-L382 |
| FP_BIC7 | 任意 cancel 标志 · evt.setCancel(true) 阻止后续 OP | 上述任一 | L383-L385 |

⚠️ **FP_BIC1-7 是工具栏前置选中校验** · 必须在 ListPlugin 自己处理（OP 拿不到 BeforeItemClickEvent）。ISV 改这层用并列挂 ListPlugin 在 beforeItemClick 阶段加规则。

### 4.5 beforeDoOperation 规则（L416-L466）

| ID | 规则 | 触发 itemKey | 实证 |
|---|---|---|---|
| FP_BDO1 | bar_new · 走 showNewForm + setCancel(true) 阻断标品 new | bar_new | L423-L426 + L555-L565 |
| FP_BDO2 | enable / disable · 设 OperateOption variable tag_of_view=true（给标品 OP 传"列表上下文"） | enable / disable | L427-L432 |
| FP_BDO3 | bar_import · writeLog "importdata" | bar_import | L433-L437 |
| FP_BDO4 | btn_allocprem · userAssignRole(rows) → 弹 hrcs_userassignrole | btn_allocprem | L438-L441 + L567-L586 |
| FP_BDO5 | inituserperm · userPermInit · isAdmin 校验后跳 bos_list 显示 hrcs_perminitrecord | inituserperm | L442-L445 + L588-L600 |
| FP_BDO6 | exportuserperm · exportUserPerm · 双闸 isAdmin + 关联非空校验 | exportuserperm | L446-L449 + L602-L632 |
| FP_BDO7 | btn_initdata · genPermFiles · 双闸 isAdmin + 重复任务防护 + 弹 hrcs_syncpermfile | btn_initdata | L450-L455 + L1040-L1056 |
| FP_BDO8 | btn_copyperm · copyPerm · 三重校验 checkRoleForBidden + getUserRelatesByPermFields + queryViewableRoles | btn_copyperm | L456-L459 + L1140-L1188 |
| FP_BDO9 | btn_batchgroup · handleBatchGroup · 弹 hrcs_permfilegrptree | btn_batchgroup | L460-L463 + L531-L543 |

### 4.6 afterDoOperation 规则（L468-L496）

| ID | 规则 | 触发 itemKey | 实证 |
|---|---|---|---|
| FP_ADO1 | enable/disable · 读 customDataMap.dealResult_notNeedUpdateTag · 都已是目标态 → 提示且不刷新 | enable/disable | L478-L483 |
| FP_ADO2 | enable/disable · 读 dealResult_successNumber + dealResult_detailMsg · showChangeStatusMessage（X 条 / 成功 N / 失败 M） | enable/disable | L484-L489 + L684-L701 |
| FP_ADO3 | 任意 itemKey 处理完 · pageCache.remove("itemKey")（防回退） | any | L494-L495 |

### 4.7 itemClick 规则（L498-L518）

| ID | 规则 | 触发 itemKey | 实证 |
|---|---|---|---|
| FP_IC1 | btn_clearcache · HRPermCacheMgr.clearAllCache + clearAllManageCache · writeLog · 提示"权限缓存已清理。" | btn_clearcache | L505-L512 |
| FP_IC2 | refresh · billList.refresh + 树节点 treeNodeClick 重触 | refresh | L513-L516 + L545-L553 |

### 4.8 closedCallBack 规则（L784-L841）

| ID | 规则 | 触发 actionId | 实证 |
|---|---|---|---|
| FP_CB1 | NEW_PERMFILE · returnData.isNeedRefresh="true" → billList.refresh | bar_new 后 | L788-L797 |
| FP_CB2 | syncPermFilesTask · 读 HRAppCache.get(syncPermFilesTask{userId}) · 显示 newCount/updCount/success | syncperm 后 | L798-L827 |
| FP_CB3 | exportUrl · returnData.taskinfo · handleTaskInfo · IClientViewProxy.addAction("download", url) | exportuserperm 后 | L827-L834 + L634-L649 |
| FP_CB4 | confirmcallback_save · returnData 是 MulBasedataDynamicObjectCollection · launchSyncPermfileJob | hrcs_syncpermfile 后 | L835-L838 + L1071-L1102 |

### 4.9 setFilter 规则（L1030-L1037）

| ID | 规则 | 触发 | 实证 |
|---|---|---|---|
| FP_SF1 | super.setFilter | 列表 setFilter | L1031 |
| FP_SF2 | 当前用户 admingroup level == -1 OR > 2 → qfilters.add(org.id in userPermFileIds) | 数据隔离 | L1032-L1036 |

### 4.10 filterContainerInit 规则（L1007-L1016）

| ID | 规则 | 触发 | 实证 |
|---|---|---|---|
| FP_FCI1 | 通用过滤列里找 fieldName=permfileenable · setDefaultValue("1") | 过滤器初始化 | L1009-L1015 |

### 4.11 treeToolbarClick 规则（L843-L865）

| ID | 规则 | 触发 itemKey | 实证 |
|---|---|---|---|
| FP_TTC1 | btnnew · addGroupNode(entityId, currentNodeId) · 弹分组新增表单 | btnnew | L850 + L931-L939 |
| FP_TTC2 | btnedit · buildBtnEditErrorMsg 校验后 editGroupNode | btnedit | L851-L855 + L869-L883 |
| FP_TTC3 | btnedit · 编辑根节点报"不允许编辑根节点。" | btnedit | L869-L873 |
| FP_TTC4 | btnedit/btndel · 空 currentNodeId 报"请选择有效的分组节点。" | btnedit/btndel | L874-L877 |
| FP_TTC5 | btnedit/btndel · 禁用刷新情况报"禁用刷新情况下不允许修改/删除节点" | btnedit/btndel | L878-L881 |
| FP_TTC6 | btndel · canDelGroup 检查 · 弹 confirmCallBack 二次确认"您确认删除分组X？" | btndel | L856-L865 + L902-L918 |
| FP_TTC7 | btndel · 分组下有子分组 · 报"不允许删除该分组，因为它包含下级分组。" | btndel | L902-L909 |
| FP_TTC8 | btndel · 分组下有 permfile member · 报"该分组存在权限档案，不允许删除。" | btndel | L910-L915 |

### 4.12 billListHyperLinkClick 规则（L967-L983）

| ID | 规则 | 触发 | 实证 |
|---|---|---|---|
| FP_HLC1 | 拦截原超链 · setCancel(true) | 超链点击 | L968 |
| FP_HLC2 | validateModifyPermission（permItemId="4715a0df000000ac" 即"修改"权限项） · 决定 EDIT vs VIEW | 超链点击 | L976-L982 + L985-L987 |
| FP_HLC3 | 弹 hrcs_userpermfile 详情 · setCloseCallBack(NEW_PERMFILE) · pkid = 当前行 | 超链点击 | L971-L982 |

---

## 五、PermFilesSaveOp 标品 OP 规则（推断 · jar 缺失）

虽然 jar 不可用 · 但根据 _auto_operations.md 揭示：
- save opKey · 1 plugin · 0 validations
- enable opKey · 1 plugin · 1 validations（FormValidate `08VSPFHHJ4PU`）
- disable opKey · 0 plugins · 0 validations
- delete opKey · 1 plugin · 1 validations（FormValidate `08VTK+YML3MU`）
- disable_new opKey · 1 plugin · 1 validations（FormValidate `0===1HWWTAJR`）

ISV **不要继承 PermFilesSaveOp**（PR-001 · 它是 hrcs 场景专属类）· 走并列挂 OP · 见 06 CS。

---

## 六、关键业务约束（跨方法的隐式规则）

### 6.1 不允许给自己授权（checkDisable L651-L667）

```java
// 反编译 L660-L665
if (!HRStringUtils.equals(currentUserId, userId)) {
    flag = true;  // 允许操作
} else {
    flag = false;
    showTipNotification("不允许授权给自己。");
}
```

⚠️ 当前用户的 userId 跟档案的 user.id 相同 → 报"不允许授权给自己。" · 应用于 userAssignRole 流程（L567-L586）。

### 6.2 user.isforbidden 校验（checkRoleForBidden L1197-L1200）

```java
return Objects.isNull(permFile)
    || permFile.getBoolean("user.isforbidden")
    || HRStringUtils.equals(permFile.getString(PERMFILEENABLE), "0");
```

⚠️ 三种情况都视为"不可复制"：档案不存在 / 用户被禁用 / 档案已失效。

### 6.3 树根节点写死 GUID（L216）

```java
private static final String ID_ROOTNODE = "8609760E-EF83-4775-A9FF-CCDEC7C0B689";
```

⚠️ ISV 自建逻辑里**不要用这个 GUID** · 不要重新构造树根 · 直接调 `getTreeModel().getRoot().getId()` 拿。

### 6.4 默认搜索文案（updateSearch L257-L262）

```java
search.setSearchEmptyText(new LocaleString("请输入权限档案组名称。"));
```

⚠️ 搜索框是按"分组名"搜·不是按用户姓名/ID。如要按用户搜·需 ISV 加搜索字段（CS-06）。

### 6.5 列表搜索默认 permfileenable=1（filterContainerInit L1007-L1016）

```java
if (HRStringUtils.equals(PERMFILEENABLE, fieldName)) {
    commFilter.setDefaultValue("1");
    break;
}
```

⚠️ 列表默认只显示生效档案。用户要看失效档案需手动改过滤器。

---

## 七、跨场景规则关联

| 上游/下游场景 | 规则 | 触发点 |
|---|---|---|
| `hrcs_admingroup` | admingroup level > 2 限制 org 可见 | setFilter L1032-L1036 |
| `hrcs_userrolerelat` | role.enable=1 仅显示生效角色 | getUserRelatesByPermFields L1192 |
| `bos_user` | user.isforbidden=true 禁复制 | checkRoleForBidden L1199 |
| `hrcs_permfilegrp` | enable=1 只显示生效分组 | getTreeViewCollection L282 |
| `hrcs_permfilegrpmember` | 分组下有 member 禁删 | canDelGroup L910-L916 |
| `hrcs_perminitrecord` | inituserperm opKey 跳此列表 | userPermInit L598 |
| `hrcs_userassignrole` | assignrole opKey 弹此表单 | userAssignRole L585 |
| `hrcs_copyperm` | copyperm opKey 弹此表单 | showCopyPermForm L1154 |
| `hrcs_syncpermfile` / `hrcs_syncprocess` | syncperm 流程 2 表单 | genPermFiles L1060 / L1109 |
| `hrcs_permfilegrptree` | batchgroup 弹此表单 | handleBatchGroup L542 |
| `bos_operationresult` | enable/disable 失败展示 | showResultForm L735-L743 + showChangeStatusMessage L693-L700 |

---

## 八、规则定制扩展点（导向 06 CS）

| 规则需求示例 | 推荐 CS |
|---|---|
| 加自定义字段（"档案备注"） | CS-01 |
| 字段联动（用户 → 自动带 org） | CS-02 |
| save 前业务校验（user+org 唯一） | CS-03 |
| disable 前查下游引用 | CS-04 |
| disable 后通知下游 BPM | CS-05 BEC |
| 列表 setFilter 加项目维度 | CS-06 |
| batchgroup 时自动写自定义字段 | CS-07 |

详见 `06_customization_solutions.md`。
