# 数据流转 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + scene_doc.json 56 字段实抓
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

---

## 一、save / submit 数据落地链路

### 1.1 入口：DynaAuthSchemePlugin.beforeDoOperation

```
FormPlugin 端 (DynaAuthSchemePlugin.java L424-L495)
   ↓
1. 抽取 PermFilter 控件值: allRuleConfigs = permFilter.getValue()
2. 校验: PermRuleValidateUtil.validCondition(allRuleConfigs)
   - 失败 → showErrorNotification + setCancel(true) 阻断
3. 写回 model.condition + model.ruledescription（resolveRuleDesc）
4. 自动清理无关分录:
   - authaction=1 → deleteEntryData("cancelactionentry")
   - authaction=2 → deleteEntryData("assignactionentry") + assigndays=0
5. 灌库 6 张反查表:
   - DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch(view)
   - DynaAuthSchemeServiceHelper.resolveSceneToSearch(view)
6. PageCache 缓存 customInfoList (roleentry 的 custominfo 字段集合 · L488-L494)
   缓存原因：save 后 model 会重置 · 用 PageCache 缓存的 customInfo 在 beforeBindData 重灌
```

### 1.2 标品 OP 链（save · 10 个标品 OP）

按 `executionChain` 顺序：

```
1. CodeRuleOp                  (kd.bos.business.plugin.CodeRuleOp)
   onAddValidators 注册 numberValidator · 自动生成 number 字段（PR-006）
2. BdVersionSaveServicePlugin  (kd.bos.base.bdversion.BdVersionSaveServicePlugin)
   基础资料版本管理 · 主表 name + 版本子表 name 写历史
3. HRBaseDataStatusOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp)
   onAddValidators · 单据状态校验
4. HRBaseDataLogOp             (kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp)
   beforeExecuteOperationTransaction · 操作日志埋点
   afterExecuteOperationTransaction · 操作日志落库
5. HRBaseDataEnableOp          (kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp)
   beforeExecuteOperationTransaction · enable 字段管理
6. HRBaseOriginalOp            (kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp)
   原始值记录（变更前后对比）
7. HisModelOPCommonPlugin      (kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin · @SdkInternal)
   onAddValidators + beforeExecuteOperationTransaction + afterExecuteOperationTransaction
   时序模型公共 · 维护 his_bsed/his_bsled/effectdate
8. HisUniqueValidateOp         (kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp · @SdkInternal)
   onAddValidators · 时态唯一性（同一 boid 同一时间段唯一）
9. DynaAuthSchemeOp            (kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp)
   onPreparePropertys 声明读 admingroup/boid/assignactionentry/cancelactionentry (L21-L26)
   onAddValidators · 注册 DynaAuthSchemeValidator (L28-L30)
10. DynaAuthSchemeSaveSubmitOp (kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp)
   endOperationTransaction · save/submit 通用收尾
```

### 1.3 DynaAuthSchemeSaveSubmitOp.endOperationTransaction（关键收尾）

```java
public void endOperationTransaction(EndOperationTransactionArgs args) {  // L21-L32
    super.endOperationTransaction(args);
    String isListOp = this.getOption().getVariableValue("list_op", "");
    if ("1".equals(isListOp)) return;                           // 列表上的 save/submit 跳过

    DynamicObject[] dyoArr = args.getDataEntities();
    DynamicObject dyo = dyoArr[0];
    long schemeId = dyo.getLong("id");
    DynamicObjectCollection roleEntryView = dyo.getDynamicObjectCollection("roleentry");
    DynaSchemeRoleAssignServiceHelper.saveRoleEntry(schemeId, roleEntryView);
}
```

→ 落 `t_hrcs_dynaschemerole` 表（含 customenable / custominfo / role 等字段）

---

## 二、audit 数据落地链路

`DynaAuthSchemeAuditOp.endOperationTransaction` (L23-L42)：

```java
public void endOperationTransaction(EndOperationTransactionArgs args) {
    super.endOperationTransaction(args);
    DynamicObject[] dyoArr = args.getDataEntities();
    String isListOp = this.getOption().getVariableValue("list_op", "");

    if ("1".equals(isListOp)) {                                      // 列表批量审核
        for (DynamicObject dyo : dyoArr) {
            long boId = dyo.getLong("id");                           // 注意：列表分支用 id 当 boId
            DynamicObjectCollection roleEntryColl = DynaRoleDetailServiceHelper.loadRoleCustomInfo(boId);
            long sourceVid = dyo.getLong("sourcevid");
            DynamicObjectCollection versionRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl(sourceVid, roleEntryColl);
            DynaSchemeRoleAssignServiceHelper.saveRoleEntry(sourceVid, versionRoleEntryColl);
        }
    } else {                                                         // 单据审核
        DynamicObject dyo = dyoArr[0];
        long sourceVid = dyo.getLong("sourcevid");
        DynamicObjectCollection roleEntryColl = dyo.getDynamicObjectCollection("roleentry");
        DynamicObjectCollection versionRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl(sourceVid, roleEntryColl);
        DynaSchemeRoleAssignServiceHelper.saveRoleEntry(sourceVid, versionRoleEntryColl);
    }
}
```

**关键差异点**：
- 列表分支：从 `DynaRoleDetailServiceHelper.loadRoleCustomInfo(boId)` 二次查 roleentry · 不用 `dyo.roleentry`（防 list selection 不带分录详情）
- 单据分支：直接拿 model 的 roleentry
- **两路都调 `saveRoleEntry(sourceVid, ...)` 写到旧版本对应的 entryboid 体系下** —— 这是 HisModel 时序：审核完 · 数据"绑定"在 sourceVid 上而不是新 id 上（因为 sourceVid 才是稳定的"业务对象"）

---

## 三、confirmchange 数据落地链路

`DynaAuthSchemeConfirmChangeOp.endOperationTransaction` (L25-L40)：

```java
public void endOperationTransaction(EndOperationTransactionArgs args) {
    super.endOperationTransaction(args);
    DynamicObject[] dyoArr = args.getDataEntities();
    String isListConfirmChange = this.getOption().getVariableValue("list_op", "");

    if (isListConfirmChange.equals("1")) {
        throw new KDBizException("not support");                     // 列表不允许 confirmchange
    }

    DynamicObject dyo = dyoArr[0];
    long boId = dyo.getLong("boid");
    long bgVid = dyo.getLong("id");
    DynamicObjectCollection versionRuleColl = dyo.getDynamicObjectCollection("roleentry");

    // 1. 写新版本维度
    DynaSchemeRoleAssignServiceHelper.saveRoleEntry(bgVid, versionRuleColl);

    // 2. 写业务对象维度（boId · 跨版本不变的）
    DynamicObjectCollection boRoleEntryColl = DynaRoleDetailServiceHelper.genVersionRoleEntryColl(boId, versionRuleColl);
    DynaSchemeRoleAssignServiceHelper.saveRoleEntry(boId, boRoleEntryColl);
}
```

→ confirmchange 是 HisModel 时序场景下"业务变更"的关键 OP · **必须双写**：
- `bgVid` 维度：留下"这一个版本绑了哪些角色 + 范围属性"的快照
- `boId` 维度：更新"业务对象当前绑了哪些角色"（下游用 boid 查 · 必须保持是最新的）

---

## 四、6 张反查派生表的灌库流（save/submit/confirmchange 都走）

`DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch(view)` 调用时机：

```
DynaAuthSchemePlugin.beforeDoOperation (L457-L459)
   if (operateKey ∈ {save, submit, confirmchange} 且 condition 不空) {
       DynaAuthSchemeServiceHelper.resolveRuleConfigToSearch(view);   // 灌 search_param + search_adminorg + search_pos + search_job
       DynaAuthSchemeServiceHelper.resolveSceneToSearch(view);        // 灌 search_assignaction + search_cancelaction
   }
```

→ 这两个调用把规则条件展开成扁平索引 · 写入：
- `t_hrcs_dynasearchparam`（规则用了哪些参数 id）
- `t_hrcs_dynasearchadminorg` / `t_hrcs_dynasearchpos` / `t_hrcs_dynasearchjob`（规则命中哪些 admingroup / 岗位 / 职位）
- `t_hrcs_dynasearchactiona` / `t_hrcs_dynasearchactionc`（规则用了哪些变动类型）

**用途**：业务侧反查"我这个组织/岗位/职位/变动类型 触发了哪些方案" · 走这 6 张索引表 · 不用每次重新解析 condition JSON。

---

## 五、delete 级联清理流（仅列表）

`DynaAuthSchemeListPlugin.afterDoOperation` (L168-L183)：

```java
} else if (HRStringUtils.equals("delete", operateKey)) {
    List successPkIds = operationResult.getSuccessPkIds();
    if (successPkIds.size() <= 0) return;
    QFilter[] idFilter = new QFilter[]{new QFilter("scheme", "in", successPkIds)};
    new HRBaseServiceHelper("hrcs_dynaschemerange").deleteByFilter(idFilter);
    new HRBaseServiceHelper("hrcs_dynaschorg").deleteByFilter(idFilter);
    new HRBaseServiceHelper("hrcs_dynaschdimgrp").deleteByFilter(idFilter);
    new HRBaseServiceHelper("hrcs_dynaschdatarule").deleteByFilter(idFilter);
    new HRBaseServiceHelper("hrcs_dynaschfield").deleteByFilter(idFilter);
}
```

→ 删除主表后 · **5 张下游配置表**按 `scheme = pkId` 物理清理。**注意：t_hrcs_dynaschemerole 没在这里删** · 走的是 BdVersionDeleteOp 标品流程级联（由分录 belongTo 关系自动）。

---

## 六、reload 缓存数据回灌（beforeBindData）

`DynaAuthSchemePlugin.beforeBindData` (L276-L285) save/confirm 后表单重渲染时：

```java
if (!roleEntryReload) {                                              // pageCache 有 roleEntryReload=1
    String customInfoListStr = this.getPageCache().get("customInfoList");
    List customInfoList = SerializationUtils.fromJsonString(customInfoListStr, List.class);
    int index = 0;
    for (String customInfo : customInfoList) {
        this.getModel().setValue(FIELD_ROLE_CUSTOM_INFO, customInfo, index++);
    }
    this.getPageCache().remove("roleEntryReload");
    this.getPageCache().remove("customInfoList");
}
```

→ save/confirm 时（L487-L494）把 roleentry 的 custominfo 字段值缓存到 PageCache · 下次 beforeBindData 时重灌回 model。**为什么要这套**：因为标品的版本变更/审核会重做 model · custominfo 是 LargeText JSON · 不在标品默认字段读取列表里 · 不缓存就丢。

---

## 七、事务边界（基于 PR-010 13 个生命周期）

| 阶段 | 时机 | 干什么 |
|---|---|---|
| `beforeDoOperation` (FormPlugin) | UI 层 · 进事务前 | 规则校验 + 反查灌库 + 分录清理 |
| `onAddValidators` (OP) | 事务前 · validator 注册 | 注册 DynaAuthSchemeValidator + HisUniqueValidateOp 等 |
| `beforeExecuteOperationTransaction` (OP) | 事务开始前 | 标品的状态/log/enable/original 各自做活 |
| `endOperationTransaction` (OP) | 事务即将提交 | DynaAuthSchemeSaveSubmitOp/AuditOp/ConfirmChangeOp 落 roleentry |
| `afterExecuteOperationTransaction` (OP) | 事务提交后 | HRBaseDataLogOp 写日志 + ISV 自定义"发 BEC"应在此阶段（PR-010） |

**PR-010 重点**：发外部事件（BEC / 通知下游）必须放 `afterExecuteOperationTransaction` · 不在 `endOperationTransaction` —— 否则事务可能还未真正提交 · 产生脏事件。

---

## 八、回滚路径

OP 链任何一步抛异常 · 走 `rollbackOperation` （PR-010 步骤 7）：
- DB 事务由 Spring 自动回滚（JTA 或 JDBC TX）
- DynaSchemeRoleAssignServiceHelper.saveRoleEntry 写的子表也会回滚（同一事务内）
- 反查表 6 张是在 `beforeDoOperation` 写的（FormPlugin · 不在 OP 事务内） —— **小心：这是个潜在脏写点**
  - 实际上 `resolveRuleConfigToSearch` 内部用了 HRBaseServiceHelper · 走标品事务管理 · 跟主流程同事务（标品已确保）
  - 但 ISV 自建的"灌反查表"扩展 · 必须走 `afterExecuteOperationTransaction` 阶段防脏写

---

## 九、关键调用计数（每条 save 至少几次落库）

```
新建方案 · 含 5 条 roleentry · 1 条 assignactionentry：

主表 t_hrcs_dynascheme              1 INSERT
分录 t_hrcs_dynaschemerole         5 INSERT (saveRoleEntry)
分录 t_hrcs_dynaschasgnactent      1 INSERT (标品分录写入)
反查 t_hrcs_dynasearchparam        N INSERT (条件参数数 N)
反查 t_hrcs_dynasearchadminorg     M INSERT (命中组织数 M)
反查 t_hrcs_dynasearchpos          K INSERT
日志 hr_log_xxx                    1 INSERT (HRBaseDataLogOp)
                                 ─────
                                 总计 N+M+K+8 行
```

→ 性能上 · save 一次的 DB 写入量与"规则条件复杂度"线性相关。**ISV 写 CS 时不要在 save 时再额外 INSERT 大量数据**（特别是 `beforeDoOperation` · 同步写入会拉长用户感知 RT）。

---

## 十、跨场景数据流引用

| 流向 | 上游/下游 | 关系 |
|---|---|---|
| ⬅ admingroup | `perm_admingroup`（domain=hr） | 方案归属管理员组 · F7 选择 |
| ⬅ role | `perm_role` | 角色清单分录（roleentry.role） |
| ⬅ hrcsrole | `hrcs_role` | 中台角色 · 由 role 反查带出 |
| ⬅ assignpersonitem / cancelpersonitem | `hrcs_dynaauthobject` | 授权对象（人员） |
| ⬅ assignactype / cancelactype | `hpfs_chgcategory` | 变动类型 |
| ⬅ search_adminorg | 行政组织（HRMulAdminOrgField） | - |
| ⬅ search_pos | 岗位（HRMulPositionField） | - |
| ⬅ search_job | 职位（MulHisModelBasedataField） | - |
| ➡ hrcs_userrolerelat | 用户角色关联 | sourcetype=4 标记"由动态方案分配" |
| ➡ hrcs_dynaschemerange / dynaschorg / dynaschdimgrp / dynaschdatarule / dynaschfield | 方案配置下游 5 表 | scheme = boid 关联 |

详见 `11_upstream_downstream_logic.md`。
