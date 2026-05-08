# 业务规则 · HR管理员（hrcs_useradmingroup）

> **状态**：🟢 基于 5 反编译类 + form_lifecycle_rules.json + rules_chain_all.json 整合
> **confidence**：verified
> **数据源**：CFR 反编译 `HRAdminGroupTreeListPlugin` / `HRAdminStrictPlugin` / `AdminGroup*Op`（2026-04-28）

> ⚠️ 本场景**无 formRule / bizRule**（OpenAPI listRules 实抓为空）。所有业务规则都写在插件代码里——这是 hrcs 域的常态（与 admin_org 的 140 条 FormPlugin 规则模式一致）。详见 `form_lifecycle_rules.json`（36 条规则） + `rules_chain_all.json`（10 opKey · 12+ businessRules）。

---

## 一、规则总览（按规则源分类）

| 规则源 | 数量 | 实证 |
|---|---|---|
| 准入校验规则（preOpenForm） | 3 条 | `HRAdminStrictPlugin.java` L29-L52 |
| FormPlugin 拦截规则（beforeDoOperation） | 8 条 | `HRAdminGroupTreeListPlugin.java` L165-L211 |
| TreeList 状态规则（treeNodeClick / verifyAdminGroup / verifyBatchAuth） | 7 条 | `HRAdminGroupTreeListPlugin.java` L469-L498, L448-L467, L600-L626 |
| 删组前置规则（adminGroupTreeRemoveOperation） | 5 条（4 道校验 + 角色反查） | `HRAdminGroupTreeListPlugin.java` L230-L258, L500-L519 |
| 用户F7范围规则（showUserF7TreeList） | 5 条 | `HRAdminGroupTreeListPlugin.java` L399-L443 |
| OP 阶段规则（beginOperationTransaction） | 12+ 条 | 3 个 OP 反编译实证 |

---

## 二、准入校验规则（preOpenForm · HRAdminStrictPlugin）

### 规则 R-A1 · F7 lookup 模式直接放行

```
trigger: preOpenForm
condition: fsp instanceof ListShowParameter && lsp.isLookUp() == true
action: return; (不做准入校验)
why: F7 lookup 是被其他表单引用作选项·此时不需要准入校验
source: HRAdminStrictPlugin.java L29-L35
```

### 规则 R-A2 · 非超管且非 cosmic 用户 → 拒绝

```
trigger: preOpenForm
condition: !PermissionServiceHelper.isAdminUser(uid) && !PermCommonUtil.isCosmicUser(uid)
action: e.setCancel(true); e.setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。")
i18n_key: HRAdminStrictPlugin_0
source: HRAdminStrictPlugin.java L40-L46
```

### 规则 R-A3 · 不是 HR 域管理员 → 拒绝

```
trigger: preOpenForm
condition: !HRAdminService.isHrAdmin()
action: e.setCancel(true); e.setCancelMessage(...同 R-A2)
why: 即使是平台 admin 也要拒（这是 HR 域专属功能）
source: HRAdminStrictPlugin.java L48-L52
```

⚠️ 这 3 条规则在所有 hrcs 11 个表单上**共用**·改一处坏所有。ISV 不能继承 `HRAdminStrictPlugin` 修改它（PR-001 红线）。

---

## 三、FormPlugin 拦截规则（beforeDoOperation · HRAdminGroupTreeListPlugin）

### 规则 R-B1 · 二次 isHrAdmin 准入校验

```
trigger: beforeDoOperation
condition: evt.getSource() instanceof FormOperate
action: if (!HRAdminService.isHrAdmin()) throw new KDBizException("您无法访问该功能...")
why: 防绕过 preOpenForm（前端伪造请求）
source: HRAdminGroupTreeListPlugin.java L165-L173
```

### 规则 R-B2 · donothing_remove_user · 上级组不允许删用户

```
trigger: beforeDoOperation [opKey="donothing_remove_user"]
condition: !ROOT && superiorGroupIds.contains(adminGroupId)
action: showTipNotification(getOnlyModifySubGroupText()); evt.setCancel(true);
i18n_key: HRAdminGroupTreeListPlugin_3
source: HRAdminGroupTreeListPlugin.java L179-L189
```

### 规则 R-B3 · donothing_add_group · 层级上限校验

```
trigger: beforeDoOperation [opKey="donothing_add_group"] → adminGroupTreeAddOperation
condition: level >= PermCommonUtil.getAdminLevelLimit()
action: showTipNotification("仅允许创建%s级管理员。", levelLimit); return;
i18n_key: HRAdminGroupTreeListPlugin_1
source: HRAdminGroupTreeListPlugin.java L322-L324
```

### 规则 R-B4 · donothing_modify_group · EDIT/VIEW 双态判定

```
trigger: beforeDoOperation [opKey="donothing_modify_group"] → adminGroupTreeModifyOperation
condition: superiorGroupId.contains(adminGroupId)
action_if_true: bsp.setStatus(OperationStatus.VIEW); bsp.getCustomParams().put("viewstatus", "1");
action_if_false: bsp.setStatus(OperationStatus.EDIT); viewstatus="0";
why: 上级组只能查看·不能编辑
source: HRAdminGroupTreeListPlugin.java L292-L300
```

### 规则 R-B5 · donothing_remove_group · 4 道前置校验

依次（任一失败 return · 不弹确认框）：

```
1. 不是根节点 (ROOT_NODE_ID = "8609760E-EF83-4775-A9FF-CCDEC7C0B689")
2. verifyAdminGroup 通过（在管控范围）
3. 无下级分组 (perm_admingroup.parent=adminGroupId 查空)
4. 无用户 (hrcs_useradmingroup.usergroup=adminGroupId 查空)
5. checkHasRoleRef 通过（3 张表都不能引用·见 R-RREF）
```

source: HRAdminGroupTreeListPlugin.java L230-L258

### 规则 R-B6 · donothing_batch_perm · verifyBatchAuth

依次（任一失败 return）：

```
1. 不是根节点
2. 有下级分组（longnumber like "%X%" 至少 2 条·明细组拒）
3. 在管控范围（focusNodeLongNumber.startsWith currentUserInGroup.longnumber）
```

source: HRAdminGroupTreeListPlugin.java L448-L467

---

## 四、删组角色反查规则（checkHasRoleRef · 5 张表）

### 规则 R-RREF1 · perm_role.createadmingrp 反查

```
condition: PermDBServiceHelper.roleDBService.isExists(new QFilter("createadmingrp", "=", adminGroupId))
action_if_exists: showTipNotification("不允许删除管理员分组\"%s\"，该组已被角色引用。"); return true;
i18n_key: HRAdminGroupTreeListPlugin_4
source: HRAdminGroupTreeListPlugin.java L500-L505
```

### 规则 R-RREF2 · hrcs_roleopenscope.admingroup 反查

```
condition: HRBaseServiceHelper("hrcs_roleopenscope").isExists(new QFilter("admingroup", "=", adminGroupId))
action_if_exists: 同 R-RREF1
source: HRAdminGroupTreeListPlugin.java L506-L511
```

### 规则 R-RREF3 · hrcs_roleassignscope.admingroup 反查

```
condition: HRBaseServiceHelper("hrcs_roleassignscope").isExists(new QFilter("admingroup", "=", adminGroupId))
action_if_exists: 同 R-RREF1
why: 这是 hrcs_dynascheme ↔ hrcs_useradmingroup 的耦合点（动态授权方案的 assign scope 引用 admingroup）
source: HRAdminGroupTreeListPlugin.java L512-L517
```

⚠️ ISV 自建表（如 ${ISV_FLAG}_admingroupext）有 admingroup 引用时 · **必须 ISV 自建反查规则**（详见 CS-04）· 标品不会自动反查 ISV 表。

---

## 五、用户 F7 范围规则（showUserF7TreeList · 5 道过滤）

### 规则 R-F7-1 · 排除当前组已有用户（DB 直查）

```
sql: "SELECT FUSERID FROM T_PERM_USERADMINGROUP WHERE FADMINGROUPID = " + adminGroupId
result: filterUserIds 集合（用作 not in 过滤）
source: HRAdminGroupTreeListPlugin.java L415-L416
```

### 规则 R-F7-2 · 排除当前用户自己

```
filterUserIds.add(RequestContext.get().getCurrUserId());
why: 防止管理员把自己加到自己组·避免循环授权
source: HRAdminGroupTreeListPlugin.java L417
```

### 规则 R-F7-3 · 排除同 adminScheme 不同 adminType 用户（DB 直查）

```
sql: "SELECT DISTINCT uap.fuserid FROM t_perm_useradmingroup uap
      INNER JOIN t_perm_admingroup ug ON uap.fadmingroupid = ug.fid
      WHERE ug.fadminscheme = ? AND ug.fadmintype != ?"
params: [adminScheme, adminType]
source: HRAdminGroupTreeListPlugin.java L418-L420
```

### 规则 R-F7-4 · enable=1（必启用）

```
filter: new QFilter("enable", "=", "1")
source: HRAdminGroupTreeListPlugin.java L426
```

### 规则 R-F7-5 · usertype 多值匹配（4 个 OR）

```
filter: usertype = "1"
     OR usertype like "1,%"
     OR usertype like "%,1"
     OR usertype like "%,1,%"
why: usertype 是逗号分隔多值字段·"1" 是某个特殊用户类型代码·必须包含此类型才允许加管理员
source: HRAdminGroupTreeListPlugin.java L427-L431
```

### 规则 R-F7-EXT · IAdminGroupListSubExtPlugin SDK 扩展点

```
extPoint: kd.sdk.hr.hbp.business.extpoint.permission.hradmi.IAdminGroupListSubExtPlugin
method: beforeAddCustomUser(AddCustomUserEventArgs eventArgs)
trigger: HRPluginProxy.callAfter(p -> p.beforeAddCustomUser(eventArgs))
source: HRAdminGroupTreeListPlugin.java L432-L438
```

⭐ **ISV 唯一可扩展用户F7范围的入口**·实现此接口可加额外 QFilter（详见 CS-06）。

---

## 六、OP 阶段规则（beginOperationTransaction）

### 6.1 AdminGroupAddUserOp 规则

| Rule ID | trigger | condition | action |
|---|---|---|---|
| `B_DAU_save` | beginOperationTransaction | for each userId | newDynamicObject + set user/usergroup + SaveServiceHelper.save |
| `B_DAU_isEnableAuthorityChangeNotice` | beginOperationTransaction | `PermCommonUtil.isEnableAuthorityChangeNotice() == true` | `FormConfigFactory.cancelShowFormRights(saveUserIds)` 失败被吞 |
| `B_DAU_writePermLog` | beginOperationTransaction | `PermCommonUtil.isEnablePermLog() == true` | `adminEventImage` + `adminEvent2PermLog(appId, "bar_add", opbtn, adminGroupId, focusAdgNumber, focusAdgName, "", afterData, ConstantsHelper.getAdminAddBusifrom(), EnumPermBusiType.ADMIN_ADD)` |
| `B_DAU_writeOpLog` | beginOperationTransaction | (always) | `HRAdminGroupService.writeOpLog(true, appId)` |

source: AdminGroupAddUserOp.java L52-L81

### 6.2 AdminGroupDelUserOp 规则

| Rule ID | trigger | condition | action |
|---|---|---|---|
| `B_DRU_focusNodeIdEmpty` | beginOperationTransaction | `HRStringUtils.isEmpty(focusNodeId) == true` | `LOGGER.info("FOCUS_NODE_ID_IS_EMPTY"); return;` 直接结束 |
| `B_DRU_nonRoot` | beginOperationTransaction | `focusNodeId != ROOT_NODE_ID` | 直接 `DeleteServiceHelper.delete(...userAdminGroupIdList)` |
| `B_DRU_root` | beginOperationTransaction | `focusNodeId == ROOT_NODE_ID` | for each row 看 superiorGroupIds 守护 · 不在守护列表才加入 deleteList · 否则 delAll=false + notDelData |
| `B_DRU_setCustomData` | beginOperationTransaction | (always 根节点分支) | `getOperationResult().setCustomData(returnMap)` 含 delAll/count/notDelData |
| `B_DRU_writePermLog` | beginOperationTransaction | `PermCommonUtil.isEnablePermLog()` | `adminEvent2PermLog(... "donothing_remove_user", ..., EnumPermBusiType.ADMIN_DEL)` |
| `B_DRU_isEnableAuthorityChangeNotice` | beginOperationTransaction | `PermCommonUtil.isEnableAuthorityChangeNotice()` | `FormConfigFactory.cancelShowFormRights(userIds)` |

source: AdminGroupDelUserOp.java L60-L125

### 6.3 AdminGroupDelOp 规则

| Rule ID | trigger | condition | action |
|---|---|---|---|
| `B_DRG_focusNodeIdEmpty` | beginOperationTransaction | `StringUtils.isEmpty(focusNodeId)` | `LOGGER.info("FOCUS_NODE_ID_IS_EMPTY"); return;` |
| `B_DRG_parseAdminGroupId` | beginOperationTransaction | (always) | `Long adminGroupId = Long.parseLong(focusNodeId.split("_")[0])` |
| `B_DRG_9tableCascade` | beginOperationTransaction | (always) | 9 张表逐一 `DeleteServiceHelper.delete` |
| `B_DRG_unifiedExceptionHandle` | beginOperationTransaction | any DeleteServiceHelper throws | catch 抛 `KDBizException("删除失败。")` 让事务回滚 |

source: AdminGroupDelOp.java L34-L57

### 6.4 ⭐ 9 表级联清单（do_remove_group 的核心）

```
DeleteServiceHelper.delete(perm_admingroupfunperm,  usergroup=adminGroupId)
DeleteServiceHelper.delete(perm_admingroupbizunit,  usergroup=adminGroupId)
DeleteServiceHelper.delete(perm_admingrouporg,      usergroup=adminGroupId)
DeleteServiceHelper.delete(perm_admingroupapp,      usergroup=adminGroupId)
DeleteServiceHelper.delete(perm_admingroupadduser,  usergroup=adminGroupId)
DeleteServiceHelper.delete(perm_admingroup,         id=adminGroupId)            ⭐ 主表
DeleteServiceHelper.delete(hrcs_admingrouporg,      id=adminGroupId)
DeleteServiceHelper.delete(hrcs_admingroupfunc,     id=adminGroupId)
DeleteServiceHelper.delete(hrcs_admingroupfile,     id=adminGroupId)
```

⚠️ **ISV 自建跨表（如 ${ISV_FLAG}_admingroupext）不会被自动级联** · ISV 必须自建 OP 在同事务里加级联（详见 CS-04）。

---

## 七、TreeList 状态规则（IPageCache）

### 规则 R-T1 · treeNodeClick 缓存 5 个 key

```
trigger: treeNodeClick(TreeNodeEvent e)
action:
  pageCache.put("focusNodeId", nodeId)
  pageCache.put("focusNodeParentId", parentNodeId)
  pageCache.put("focusAdgNumber", adgNumber)       // 根节点缓存为 ""
  pageCache.put("focusAdgName", adgName)           // 根节点缓存为 ""
  pageCache.put("focusNodeLongNumber", longNumber) // 根节点缓存为 ""
source: HRAdminGroupTreeListPlugin.java L600-L626
```

### 规则 R-T2 · 根节点点击特殊处理

```
condition: nodeId == "8609760E-EF83-4775-A9FF-CCDEC7C0B689"
action: adgNumber/adgName/longNumber 全设为 ""
why: 根节点是虚拟节点·没有对应 perm_admingroup 行·避免 NPE
source: HRAdminGroupTreeListPlugin.java L610-L613
```

### 规则 R-T3 · 非根节点加载 perm_admingroup

```
condition: nodeId != ROOT
action:
  Long adminGroupId = Long.parseLong(nodeId.split("_")[0]);
  DynamicObject adg = BusinessDataServiceHelper.loadSingleFromCache(
      adminGroupId, "perm_admingroup", "number, name, longnumber");
  adgNumber = adg.getString("number");
  adgName = adg.getString("name");
  longNumber = adg.getString("longnumber");
source: HRAdminGroupTreeListPlugin.java L614-L620
```

### 规则 R-T4 · setFilter 加 adminGroupCanSee 过滤

```
trigger: setFilter(SetFilterEvent e)
condition: pageCache.get("adminGroupCanSee") != null && !adminGroupIdCanSee.isEmpty()
action: e.getCustomQFilters().add(new QFilter("usergroup.id", "in", adminGroupIdCanSee))
why: 右侧用户列表只显示当前用户能看到的 admingroup 下的用户
source: HRAdminGroupTreeListPlugin.java L644-L651
```

### 规则 R-T5 · chat 事件禁用

```
trigger: chat(ChatEvent e)
action: e.setCancel(true)
source: HRAdminGroupTreeListPlugin.java L665-L667
```

---

## 八、规则与 PR（平台规范）映射

| 本场景规则 | 引用 PR | 说明 |
|---|---|---|
| R-A1 / R-A2 / R-A3 准入 | PR-001 | HRAdminStrictPlugin 是共用类·ISV 不继承 |
| R-B1 二次 isHrAdmin | PR-001 | HRAdminGroupTreeListPlugin 是场景专属类·ISV 不继承 |
| R-B5 删组前置校验 | PR-001/PR-007 | 不继承场景专属类 · 但反查标品业务字段不破坏一致性 |
| R-RREF1/2/3 角色反查 | PR-007 | 反查的是预置 perm_role / hrcs_roleopenscope 等标品表 |
| R-F7-EXT IAdminGroupListSubExtPlugin | PR-001 | SDK 扩展点 · ISV 实现接口而非继承 |
| OP 阶段规则 | PR-010 | beginOperationTransaction 阶段 · 同事务 |
| 9 表级联 | （特有）| 平台未提供级联保证 · 标品 OP 自己写 · ISV 表必须自建级联 |

---

## 九、ISV 加新规则的位置

| 想加的规则 | 推荐挂载点 | CS |
|---|---|---|
| 加用户前的业务校验 | ISV 自建 OP 挂 do_add_user 的 onAddValidators | CS-03 |
| 删组前的 ISV 表反查 | ISV 自建 OP 挂 do_remove_group 的 onAddValidators | CS-04 |
| 用户 F7 范围扩展 | 实现 IAdminGroupListSubExtPlugin SDK 接口 | CS-06 |
| 字段联动 | ISV 自建 FormPlugin 挂 hrcs_useradmingroup · propertyChanged | CS-02 |
| 跨系统通知 | ISV 自建 OP 挂 do_add_user 的 afterExecuteOperationTransaction | （不推荐 BEC） |

详见 `06_customization_solutions.md`。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

## chgaction 实证补充（ForbidUrlOpenPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin -->

## chgaction 实证补充（HRAdminGroupTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin`
> 跨类追踪: 10 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRAdminStrictPlugin_0` | 您无法访问该功能，因为您不是HR领域管理员。 |
| `AdminGroupService_0` | 管理员组数据异常或元数据异常，请联系管理员或重新升级管理员数据。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminGroupTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp -->

## chgaction 实证补充（AdminGroupDelOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `AdminGroupTreeListPlugin_7` | 删除失败。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp -->

## chgaction 实证补充（AdminGroupAddUserOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `AdminGroupService_0` | 管理员组数据异常或元数据异常，请联系管理员或重新升级管理员数据。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupAddUserOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp -->

## chgaction 实证补充（AdminGroupDelUserOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `AdminGroupService_0` | 管理员组数据异常或元数据异常，请联系管理员或重新升级管理员数据。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.AdminGroupDelUserOp -->
