# 业务流转 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类（DynaAuthSchemePlugin/Op/SaveSubmitOp/AuditOp/ConfirmChangeOp/ListPlugin/HRAdminStrictPlugin）+ scene_doc.json + opkeys_index.json
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI `getOpKeyClasses` (2026-04-28)

---

## 一、业务全景：动态授权方案要做什么

`hrcs_dynascheme` 解决的是 **"按规则给一批人/角色批量发放/取消授权"** 的运营问题。区别于"静态权限分配"（管理员手动一个个绑定），这里：

1. **HR 管理员**配置一条"规则方案" —— 描述"满足某条件的员工"
2. 平台自动找出"满足条件的员工"（规则引擎 PermFilter）
3. 自动给这批员工分配/取消方案绑定的"角色"
4. 当员工岗位变动等情况触发"变动事件"（hpfs_chgcategory）时 · 自动重新评估并触发分配/取消

> **关键差异点**（决定本场景为何这么复杂）：
> - 一个方案绑**多个角色**（roleentry · 平均 3-5 个）
> - 每个角色可定制"成员范围属性"（roleentry.customenable + custominfo · 同一角色可在不同方案下挂不同范围）
> - 触发条件分**分配触发**（assignactionentry）和**取消触发**（cancelactionentry）两种事件类型
> - 方案是**HisModel 时序基础资料** —— 可以变更 · 历史版本保留 · 支持回溯

---

## 二、业务状态机

### 2.1 数据状态（`status` 字段 · BillStatusField）

```
A 暂存 ──── submit ────► B 已提交 ──── audit ───► C 已审核
   ▲                           │                        │
   │                           │ unsubmit               │ unaudit
   └───────────────────────────┘                        │
                                                         │
   C 已审核 ──── confirmchange ────► 新版本 (status=C · sourcevid=旧 id)
```

**关键 opKey 映射**：
| opKey | 状态变化 | 事务结尾 OP | 反编译实证位置 |
|---|---|---|---|
| `save` | 不改状态 · 但触发 condition 校验 + 反查表灌库 | `DynaAuthSchemeSaveSubmitOp.endOperationTransaction` | `DynaAuthSchemeSaveSubmitOp.java` L21-L32 |
| `submit` | A → B | `DynaAuthSchemeSaveSubmitOp` | 同上 + roleentry 落库 |
| `unsubmit` | B → A | (标品 OP · 无 ISV 自定义) | 见 `_auto_operations.md` |
| `audit` | B → C | `DynaAuthSchemeAuditOp.endOperationTransaction` | `DynaAuthSchemeAuditOp.java` L23-L42 · sourceVid 重映射 |
| `unaudit` | C → A | (标品 OP) | - |
| `confirmchange` | C → 新 C 版本 | `DynaAuthSchemeConfirmChangeOp.endOperationTransaction` | `DynaAuthSchemeConfirmChangeOp.java` L25-L40 · 双写 |

### 2.2 使用状态（`enable` 字段 · BillStatusField）

```
0 已禁用 ──── enable ───► 1 已启用 / 10 启用中
   ▲                          │
   │                          │ disable
   └──────────────────────────┘
```

**关键约束**（`DynaAuthSchemeListPlugin.beforeDoOperation` L101-L107）：
- enable / disable 都先记录 sourceMap（key=id · value=Pair(sourceVid, entryIds)）到 PageCache · 用于事务后下游同步
- 操作前 · 平台标品 OP `HRBaseDataEnableOp.beforeExecuteOperationTransaction` 先做单据状态校验（必须是 status=C 已审核才能 enable / disable）
- 实际落库由 `HRBaseDataStatusOp` 统一改 enable 字段

### 2.3 状态字段关系

| 状态组合 | 含义 | 能否 enable/disable | 能否 confirmchange |
|---|---|---|---|
| `status=A` 暂存 | 草稿 | ❌ | ❌（只能 submit） |
| `status=B` 已提交 | 待审 | ❌ | ❌（只能 audit/unsubmit） |
| `status=C, enable=1` 启用中 | 生效 | ✅ disable | ✅ confirmchange |
| `status=C, enable=0` 已禁用 | 暂停 | ✅ enable | ⚠️ 看下游约束 |

---

## 三、3 大核心业务流（按 opKey）

### 3.1 流程 A · 新建方案 → 提交 → 审核（典型创建链）

```
1. 用户点【新增】 → opKey=new
   触发：HRAdminStrictPlugin.preOpenForm 校验是否 HR 管理员（HRAdminStrictPlugin.java L29-L37）
        → 不是 HR 管理员 · 直接拒绝打开 · 回 "您无法访问该功能·因为您不是HR领域管理员"

2. 表单 beforeBindData
   触发：DynaAuthSchemePlugin.beforeBindData (L200-L288)
        - 自动设置 admingroup = 当前用户的第一个管理员组
        - 走 setRequiredField 设置默认必填项（authaction=null · 默认两个分录都必填）

3. 用户填字段 · 选 admingroup（F7 过滤 isdomain=1, domain.number=hr · L347-L349）
   触发：DynaAuthSchemePlugin.beforeF7Select

4. 用户切换 authaction（1=分配 / 2=取消 / 3=两者）
   触发：DynaAuthSchemePlugin.propertyChanged (L679-L697)
        - 如果原值有 + 新值有 + 已配规则条件不空 + 没标记 secondConfirmCancel
          → 弹出确认框 "切换授权动作后·规则配置将被清空·确定切换吗？"（L688-L689）
        - 用户选 Yes → 走 confirmCallBack(clearRuleControl) → updateRuleControlEmpty + 清 condition
        - 用户选 No → setValue(authaction, oldValue) 回滚 + 标记 secondConfirmCancel 防再弹

5. 用户用 PermFilter 控件配规则条件（condition 字段 · DecisionSet JSON）

6. 用户在 roleentry 上点【添加角色】 → opKey=addrole
   触发：DynaAuthSchemePlugin.afterDoOperation (L539-L547)
        - 校验主表 name 不为空（L540-L544 · 否则提示"请先录入名称"）
        - 调 HRRolePermHelper.showRoleF7 弹出角色选择 F7
        - 用户选完 → closedCallBack("chooseRole", roleId) (L598-L617)
          → 用 ORM.genLongId 给 roleEntry 行分 entryId（PR-005）
          → 跳到 hrcs_dyscassignroledetail 子页面填范围属性
        - 子页面回填 → closedCallBack("roleDetails", json) (L619-L654)
          → setValue(role/hrcsrole/customenable/roleremark/custominfo)

7. 用户点【保存】 → opKey=save
   触发：DynaAuthSchemePlugin.beforeDoOperation (L424-L495)
        a. 抽取所有规则配置 allRuleConfigs = permFilter.getValue()
        b. 校验：PermRuleValidateUtil.validCondition(allRuleConfigs)
           失败 → showErrorNotification + setCancel(true) 阻断
        c. 检查 authaction=3 + 规则空 → 报错"请设置条件规则"
        d. 写回 condition 字段
        e. 自动清理无关分录（authaction=1 时清 cancelactionentry · L450-L455）
        f. 调 DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch + resolveSceneToSearch 灌库 6 张反查表
   触发：标品 OP 链（HRBaseDataStatusOp/HRBaseDataLogOp/HRBaseOriginalOp/HisModelOPCommonPlugin/HisUniqueValidateOp）
   触发：DynaAuthSchemeOp.onAddValidators 注册 DynaAuthSchemeValidator
   触发：DynaAuthSchemeSaveSubmitOp.endOperationTransaction (L21-L32)
        → list_op != "1" 时调 DynaSchemeRoleAssignServiceHelper.saveRoleEntry(schemeId, roleEntry)

8. 用户点【提交】 → opKey=submit
   流程类似 save · 但 status A → B
   关键：DynaAuthSchemePlugin.beforeDoOperation 同样走规则校验 + 灌库
   关键：在 List 走 submit 时设 list_op="1"（DynaAuthSchemeListPlugin.beforeDoOperation L105-L107）

9. 用户点【审核】 → opKey=audit
   触发：DynaAuthSchemePlugin.beforeDoOperation 走 confirmchange/audit 分支 (L460-L471)
        - 检查 PARAM_SKIP_CHANGE_TIPS 缓存 · 如果是 true → 跳过提示直接走
        - 否则调 DynaAuthSchemeServiceHelper.showChangeTips · 询问"是否同时刷新已分配的人员？"
          showTips 返回 true → setCancel(true) 等待用户响应
          showTips 返回 false → 直接进 audit
   触发：DynaAuthSchemeAuditOp.endOperationTransaction (L23-L42)
        - list_op="1" 路径：每条 dyo 单独 loadRoleCustomInfo · genVersionRoleEntryColl(sourceVid, ...)
        - 普通路径：直接拿 dyo.roleentry · genVersionRoleEntryColl(sourceVid, ...)
        - 双路径都调 DynaSchemeRoleAssignServiceHelper.saveRoleEntry(sourceVid, versionRoleEntryColl)
   触发：HisModelOPCommonPlugin.afterExecuteOperationTransaction
        - 维护 HisModel 时序：旧 boid 行的 iscurrentversion 由 true → false · 新版本 true
```

### 3.2 流程 B · 已审核方案变更 → confirmchange（业务变更）

```
0. 已审核方案 (status=C · iscurrentversion=true)

1. 用户点【变更】（change opKey · 标品 HRBaseDataChangeOp 拷贝出新版本）
   注：scene_doc.json 字段 isChange · DynaAuthSchemePlugin.afterBindData L322-L331 通过 customParam("isChange") 识别
   触发：DynaAuthSchemePlugin.setPermFilterStatus("change") (L577-L590)
        - dataMap.pageState = OperationStatus.EDIT
        - 标记 customParam("isChange", "1")
        - 如果 conditionExpress 里有空值 → 重灌一遍 param/comparisonOpt（防版本规则参数变了）

2. 用户改字段（含规则、分录、角色等）

3. 用户点【确认变更】 → opKey=confirmchange
   触发：DynaAuthSchemePlugin.beforeDoOperation (L429-L471)
        - 走与 save 相同的规则校验 + 反查灌库
        - 调 DynaAuthSchemeServiceHelper.showChangeTips
          - 返回 true（说明会影响人员分配） → setCancel(true) · 弹"已分配人员变更影响"确认
          - 用户选 Yes → confirmCallBack("confirmchange", Yes) (L369)
            → 设 PARAM_SKIP_CHANGE_COMMON_TIPS = true + invokeOperation("confirmchange") 重发
        - 第二次走时跳过 showChangeTips → 进 OP

4. 触发：DynaAuthSchemeConfirmChangeOp.endOperationTransaction (L25-L40)
   双写：
       a. saveRoleEntry(bgVid, versionRuleColl)         // 新版本 id 维度
       b. genVersionRoleEntryColl(boId, versionRuleColl) → saveRoleEntry(boId, ...)  // boid 业务维度

   ⚠️ list_op="1" 时抛 KDBizException("not support") · 列表上不能 confirmchange（L29-L31）

5. afterDoOperation：
   - DynaAuthSchemeListPlugin.audithisconfirmchange 分支（L159-L167）：
     如果 successPkIds > 0 · 用第一条的 boid 调 showChangeTips 提示后续操作（如重新跑权限重算任务）
```

### 3.3 流程 C · 列表删除 → 级联清理（delete）

```
1. 用户在列表上选若干条方案点【删除】 → opKey=delete

2. 触发：DynaAuthSchemeListPlugin.beforeDoOperation
   注：delete 在标品里走 BdVersionDeleteOp 等 · 不调主 OP

3. 标品 OP 删主表 t_hrcs_dynascheme

4. afterDoOperation 进 delete 分支（L168-L183）：
   foreach successPkIds → 5 张下游表批量删除：
   - hrcs_dynaschemerange   QFilter("scheme", "in", successPkIds)
   - hrcs_dynaschorg        QFilter("scheme", "in", successPkIds)
   - hrcs_dynaschdimgrp     QFilter("scheme", "in", successPkIds)
   - hrcs_dynaschdatarule   QFilter("scheme", "in", successPkIds)
   - hrcs_dynaschfield      QFilter("scheme", "in", successPkIds)

   ⚠️ 这是【DynaAuthSchemeListPlugin 的 afterDoOperation 实现】· 不是平台默认级联
       ISV 自建 OP 删除时 · 也要走这个清理（参考 CS-04）
```

---

## 四、4 个特色 opKey（dynascheme 独有）

### 4.1 `setadminrange` · 配置方案权限范围

操作触发后 · `DynaAuthSchemeServiceHelper.showAdminRangeDetail` 跳转到 hrcs_dynaschrange 子页面（编辑权限范围）。

**约束**（`DynaAuthSchemeListPlugin.beforeDoOperation` L88-L92）：
- 列表多选时 · 仅允许 1 条 · 多于 1 条 → showTipNotification("请选择一条方案") + setCancel(true)

### 4.2 `assignrecord` · 查看分配记录

跳到 `hrcs_userrolerelat` 列表 · QFilter("sourcetype", "=", "4") 过滤"由动态方案分配出的角色绑定"（`DynaAuthSchemeListPlugin.afterDoOperation` L143-L158）。

**为什么 sourcetype=4** —— 这是 hrcs 用户角色关联表里区分授权来源的标记位（1=手工 / 2=组织继承 / 3=岗位继承 / 4=动态方案 / ...）。

### 4.3 `audithisconfirmchange` · 历史变更确认（列表批量）

list 上批量 confirmchange 后 · 用第一条的 boid 调 showChangeTips 提示。是 confirmchange 的列表版本入口。

### 4.4 `checkroledetails` · 查看角色详情

打开 `DynaAuthSchemePlugin.afterDoOperation` checkroledetails 分支（L529-L532）：
- 取当前 roleentry 行 · 校验 role 不为空 + customenable 不为空（beforeDoOperation 校验 L473-L485）
- 拿 custominfo JSON · 调 showRoleDetails 跳到 hrcs_dyscassignroledetail 查看角色范围

---

## 五、`addrole` / `newassignentry` / `newcancelentry` 分录新增的隐式行 id 规则

`DynaAuthSchemePlugin.afterDoOperation` 处理 newassignentry / newcancelentry（L514-L526）：

```java
if (HRStringUtils.equals("newassignentry", operateKey) || HRStringUtils.equals("newcancelentry", operateKey)) {
    if (!this.isOnly1010()) {                            // 全表只剩 1010 时跳过清空
        int[] selectIndex = ((EntryGrid)this.getControl(KEY_ASSIGN_SCENE_ENTRY)).getSelectRows();
        this.getModel().setValue(FIELD_ASSIGN_PERSON_ITEM, null, selectIndex[0]);  // 清新行的 personitem
        ((DynamicObject)entryEntity.get(selectIndex[0])).getDataEntityState().setBizChanged(false);
    }
}
```

行为：新增分录行后 · 自动把 personitem 字段清空（防止用户复制旧行 · 但会强制重选）。

`addrole` 走另一套（不是 entry 的 newentry op）：F7 选完直接 createNewEntryRow + ORM.genLongId 给 entryId · 见 closedCallBack("chooseRole")。

---

## 六、列表批量行为约束

| opKey | list_op="1" 行为 | 单据行为 |
|---|---|---|
| `save` / `submit` | DynaAuthSchemeSaveSubmitOp 跳过 · 不调 saveRoleEntry（L23-L26） | 调 saveRoleEntry(schemeId, roleEntryView) |
| `audit` | DynaAuthSchemeAuditOp 走 list 分支 · loadRoleCustomInfo 后 saveRoleEntry | 直接 saveRoleEntry(sourceVid, versionRoleEntryColl) |
| `confirmchange` | DynaAuthSchemeConfirmChangeOp 抛 KDBizException("not support") | 双写 saveRoleEntry(bgVid) + saveRoleEntry(boId) |

`list_op="1"` 由 `DynaAuthSchemeListPlugin.beforeDoOperation` L105-L107 设置：

```java
} else if (HRStringUtils.equals(operateKey, "confirmchange") || HRStringUtils.equals(operateKey, "audit") || HRStringUtils.equals(operateKey, "submit")) {
    formOperate.getOption().setVariableValue("list_op", "1");
}
```

---

## 七、HR 领域管理员准入闸（HRAdminStrictPlugin）

11 个 hrcs 表单共用此插件（dynascheme 是其中之一）：

```java
public void preOpenForm(PreOpenFormEventArgs args) {                  // L29-L37
    super.preOpenForm(args);
    FormShowParameter fsp = args.getFormShowParameter();
    if (fsp instanceof ListShowParameter && lsp.isLookUp()) return;   // F7 lookup 不拦
    HRAdminStrictPlugin.showMesIfUserIsNotAdmin(args);
}

public static void showMesIfUserIsNotAdmin(PreOpenFormEventArgs e) {  // L39-L52
    long userId = RequestContext.get().getCurrUserId();
    boolean isAdmin = PermissionServiceHelper.isAdminUser(userId);
    boolean isCosmic = PermCommonUtil.isCosmicUser(userId);
    if (!isAdmin && !isCosmic) {                                       // 双闸：必须是平台 admin 或 cosmic 用户
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能·因为您不是HR领域管理员。");
    }
    if (!HRAdminStrictPlugin.isHrAdmin()) {                            // 第二闸：必须是 HR admin
        e.setCancel(true);
        e.setCancelMessage("您无法访问该功能·因为您不是HR领域管理员。");
    }
}
```

→ 任何非 HR 管理员的用户打开方案表单都会被拒。**ISV 不要继承这个类** · 直接复用即可（在 hrcs 标品配置已挂）。

---

## 八、与下游联动

```
方案审核通过 / confirmchange
   ↓
DynaSchemeRoleAssignServiceHelper.saveRoleEntry 写入 t_hrcs_dynaschemerole
   ↓
后续触发权限重算任务 · 把"满足条件的员工 × 方案绑定的角色"展开
   ↓
落表 hrcs_userrolerelat (sourcetype=4 标记)
   ↓
hrcs_userrolerelat 是 HR 用户角色关联标准表 · 被各种权限校验场景查询
```

**注**：dynascheme 标品**没发 BEC 业务事件**（grep `triggerEventSubscribe / IEventService / EventServiceHelper` 0 处命中）· 下游同步走的是**任务调度（schedule task）**或**用户登录时实时计算**模式 · 而非异步事件。详见 `11_upstream_downstream_logic.md`。
