# 业务规则 · 动态授权方案 (hrcs_dynascheme)

> **状态**: 🟢 基于反编译 7 类 + opkeys_index.json 60 opKey + scene_doc.json 56 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：把 `DynaAuthSchemePlugin` / `DynaAuthSchemeOp` / `DynaAuthSchemeValidator` 等反编译类里的业务规则提炼成可机读 + 可人读的清单。**ISV 扩展时不要打破这些规则** —— 否则会跟标品冲突。

---

## 一、字段必填规则（动态计算）

### 1.1 admingroup 必填

`scene_doc.json` 实抓 `admingroup.required = true` · 但实际填值由 `DynaAuthSchemePlugin.beforeBindData` 自动从当前用户管理员组带出（L207-L213）：

```java
if (OperationStatus.ADDNEW.equals(status)) {
    Set userAdminGroups = HRRolePermHelper.queryUserAdminGroups(RequestContext.get().getCurrUserId());
    long userAdminGroupId = userAdminGroups.isEmpty() ? 0L : (Long)userAdminGroups.iterator().next();
    if (0L != userAdminGroupId) {
        this.getModel().setValue("admingroup", userAdminGroupId);
    }
}
```

→ 若用户没绑任何 HR admingroup · admingroup 为 0 · save 时会被 BasedataField 必填校验拦截。

### 1.2 authaction 必填 + 切换二次确认

`authaction.required = true` · 取值 `1`=分配 / `2`=取消 / `3`=两者。

**切换规则**（`DynaAuthSchemePlugin.propertyChanged` L682-L697）：
- 当用户已配规则（condition 不空）+ 用户切换 authaction → 弹确认框"切换授权动作后·规则配置将被清空·确定切换吗？"
- 用户选 Yes → 走 `confirmCallBack(clearRuleControl, Yes)` → 清规则 + 重置必填项
- 用户选 No → setValue(authaction, oldValue) 回滚 + 缓存 `secondConfirmCancel=true` 防再弹

### 1.3 分录字段必填动态计算

`DynaAuthSchemePlugin.setRequiredField` (L716-L758)：

```java
if (HRStringUtils.equals("1", action)) {              // 仅分配
    this.setAssignRequiredField(true);                // assignactype + assignpersonitem 必填
    this.setCancelRequiredField(false);
    this.getView().setVisible(TRUE, new String[]{"assignactionflex", "assignflex"});
    this.getView().setVisible(FALSE, new String[]{"cancelactionflex", "cancelflex"});
} else if (HRStringUtils.equals("2", action)) {       // 仅取消
    this.setCancelRequiredField(true);
    this.setAssignRequiredField(false);
    ...
} else {                                               // 默认 (3 或 null)
    this.setAssignRequiredField(true);
    this.setCancelRequiredField(true);
    ...
}
```

**assigndays 必填条件**（L740-L751）：
- authaction=1 时 → 看 `PermCommonUtil.isEnableValidateTime()`：
  - true → assigndays 必填 + flexpanelap1 显示
  - false → assigndays 不必填 + flexpanelap1 隐藏
- 其他 → assigndays 不必填

### 1.4 roleentry 角色与范围属性必填

`addrole` opKey 触发后 · 在 `closedCallBack(chooseRole)` 跳到 `hrcs_dyscassignroledetail` 子页面 · 用户必须填**dataProperty**（角色成员范围属性）才能回填到 customenable + custominfo（L644-L651）。

**checkroledetails opKey 校验**（L473-L485）：
```java
} else if (HRStringUtils.equals(operateKey, "checkroledetails")) {
    DynamicObject entryRow = this.getCurRoleEntryRow();
    Object role = entryRow.get("role");
    String customEnable = entryRow.getString(FIELD_ROLE_CUSTOM_ENABLE);
    if (ObjectUtils.isEmpty(role)) {
        this.getView().showTipNotification("请先选择角色");
        args.setCancel(true);
        return;
    }
    if (HRStringUtils.isEmpty(customEnable)) {
        this.getView().showTipNotification("请选择角色成员范围属性。");
        args.setCancel(true);
    }
}
```

---

## 二、F7 选择过滤规则

### 2.1 admingroup F7（DynaAuthSchemePlugin.beforeF7Select L345-L350）

```java
} else if (HRStringUtils.equals("admingroup", propKey)) {
    QFilter idFilter = new QFilter("id", "in", HRRolePermHelper.queryUserAdminGroups(currUserId));
    QFilter domainFilter = new QFilter("isdomain", "=", "1").and("domain.number", "=", "hr");
    formShowParameter.getListFilterParameter().getQFilters().add(idFilter);
    formShowParameter.getListFilterParameter().getQFilters().add(domainFilter);
}
```

→ 只显示**当前用户绑定的 HR 域管理员组**（双闸：用户范围 + HR 域过滤）。

### 2.2 assignactype / cancelactype F7（去重）

`DynaAuthSchemePlugin.beforeF7Select` (L337-L344)：

```java
if (HRStringUtils.equals(FIELD_ASSIGN_ACTYPE, propKey) || HRStringUtils.equals(FIELD_CANCEL_ACTYPE, propKey)) {
    Set eventIds = DynaPremEvtSubService.queryChgRecordEffEventIds();   // 全表事件 id 集
    String entryKey = HRStringUtils.equals(propKey, FIELD_ASSIGN_ACTYPE) ? KEY_ASSIGN_SCENE_ENTRY : KEY_CANCEL_SCENE_ENTRY;
    DynamicObjectCollection entryEntity = this.getModel().getEntryEntity(entryKey);
    Set usedEventIds = entryEntity.stream().map(it -> it.getLong(propKey + ".id")).collect(Collectors.toSet());
    eventIds.removeAll(usedEventIds);                                   // 剔除已用过的
    QFilter idFilter = new QFilter("id", "in", eventIds);
    formShowParameter.getListFilterParameter().getQFilters().add(idFilter);
}
```

→ **避免一个方案对同一变动类型挂多条分录**（业务上一种类型只能配一种处理方式）。

---

## 三、规则配置（condition）相关规则

### 3.1 condition 校验

`DynaAuthSchemePlugin.beforeDoOperation` (L432-L437)：

```java
RuleValidateInfo ruleValidateInfo = PermRuleValidateUtil.validCondition(allRuleConfigs);
if (!ruleValidateInfo.isSuccess()) {
    this.getView().showErrorNotification(ruleValidateInfo.getMsgList().toString());
    args.setCancel(true);
    return;
}
```

→ DecisionSet JSON 必须能通过 `PermRuleValidateUtil.validCondition` 校验（语法 + 参数完整性）。

### 3.2 authaction=3 时规则不能为空

(L441-L444)：

```java
if (HRStringUtils.equals("3", authAction)) {
    this.getView().showErrorNotification("请设置条件规则");
    args.setCancel(true);
    return;
}
```

→ "分配并取消"动作必须有规则 · 否则无法判断"哪些人该分配 / 哪些人该取消"。

### 3.3 ruledescription 自动生成

`DynaAuthSchemePlugin.resolveRuleDesc` (L497-L505)：

```java
private void resolveRuleDesc(String allRuleConfigs) {
    Map previewMulLang = RulePreviewUtil.getConditionPreviewMulLang(allRuleConfigs);
    LocaleString ruleDesc = new LocaleString();
    for (Map.Entry entry : previewMulLang.entrySet()) {
        String trimedRuleDesc = RulePreviewUtil.trimMaxLength((String)entry.getValue(), 1000);
        ruleDesc.setItem((String)entry.getKey(), trimedRuleDesc);
    }
    this.getModel().setValue("ruledescription", ruleDesc);
}
```

→ ruledescription 是**派生字段** · 由 condition JSON 生成 · 多语言 · 单语言最长 1000 字符。**ISV 不要手动 setValue 这个字段**（会被覆盖）。

---

## 四、save / submit / confirmchange 通用前置规则

`DynaAuthSchemePlugin.beforeDoOperation` (L429-L495)：

| 步骤 | 检查 | 失败行为 |
|---|---|---|
| 1 | 规则配置语法校验（PermRuleValidateUtil.validCondition） | showErrorNotification + setCancel |
| 2 | authaction=3 且规则空 | showErrorNotification "请设置条件规则" + setCancel |
| 3 | authaction=1 → 自动清空 cancelactionentry | (无失败 · 静默) |
| 4 | authaction=2 → 自动清空 assignactionentry + 清 assigndays | (无失败 · 静默) |
| 5 | 规则不空 → 调 resolveRuleConfigToSearch 灌反查表 | (内部异常向上抛) |
| 6 | 调 resolveSceneToSearch 灌反查表（assignaction/cancelaction） | (内部异常向上抛) |
| 7 | confirmchange/audit 时 · 调 showChangeTips 询问"刷新已分配人员" | 用户拒绝 → setCancel |

---

## 五、confirmchange / audit 二次确认机制

`DynaAuthSchemePlugin.confirmCallBack` (L353-L374)：

```java
} else if ((StringUtils.equals("confirmchange", callBackId) || StringUtils.equals("audit", callBackId))
           && evt.getResult() == MessageBoxResult.Yes) {
    this.getPageCache().put(this.PARAM_SKIP_CHANGE_COMMON_TIPS, "true");
    this.getPageCache().put(this.PARAM_SKIP_CHANGE_TIPS, "true");
    this.getView().invokeOperation(callBackId);                       // 重新触发 op · 这次跳过提示
}
```

机制：
1. 用户首次点 confirmchange/audit → beforeDoOperation 调 `showChangeTips` → 弹"会影响 X 人 · 确定吗？"
2. 用户点 Yes → confirmCallBack 标记 `skipChangeTips=true` + 重发同 op
3. 第二次进 beforeDoOperation 时检查标记（L461-L465）：
   ```java
   String skipChangeTips = this.getView().getPageCache().get(this.PARAM_SKIP_CHANGE_TIPS);
   if (HRStringUtils.equals(skipChangeTips, "true")) {
       this.getView().getPageCache().remove(this.PARAM_SKIP_CHANGE_TIPS);
       return;                                                         // 跳过 showChangeTips · 直接走
   }
   ```

---

## 六、列表层规则（DynaAuthSchemeListPlugin）

| 操作 | 规则 |
|---|---|
| `setadminrange` / `assignrecord` | 列表只允许选 1 条（`>1` 时 setCancel）· L88-L92 |
| `copy` | 自动加"-复制"后缀到 schemeName + 设 sourceSchemeId customParam · L93-L99 |
| `enable` / `disable` | 加载所有选中方案的 sourceMap (id→Pair(sourceVid, entryIds)) 缓存到 PageCache · L100-L105 |
| `confirmchange` / `audit` / `submit` | 列表批量 → 设 list_op="1" · L105-L107 |
| `audithisconfirmchange` | 用 successPkIds[0] 的 boid 调 showChangeTips · L159-L167 |
| `delete` | 级联删除 5 张下游配置表 · L168-L183 |
| `enable` / `disable` afterDoOperation | load 已审核状态的方案数组 · L184-L194 (后续做联动 · 实际 afterDoOperation 没继续 · 看版本) |

---

## 七、列表过滤规则（setFilter）

`DynaAuthSchemeListPlugin.setFilter` (L130-L133)：

```java
public void setFilter(SetFilterEvent evt) {
    QFilter idFilter = new QFilter("boid", "in", DynaAuthSchemeServiceHelper.queryViewableSchemes());
    evt.getQFilters().add(idFilter);
}
```

→ **强制按 boid 过滤** · 只显示当前用户**可见**的方案集合。这是**数据权限闸**。

→ ISV 扩展 ListPlugin 不要继承 DynaAuthSchemeListPlugin（PR-001）· 应继承 HRDataBaseList · 在 setFilter 走 `evt.getQFilters().add()` 加自己的过滤即可（标品的过滤会保留）。

---

## 八、版本控制规则（HisModel · 时序）

### 8.1 当前版本判定

```
当且仅当：boid == id  且  iscurrentversion == true
```

`scene_doc.json` 字段 `iscurrentversion = CheckBoxField · L2 · isvCanModify=false`。

`DynaAuthSchemePlugin.beforeBindData` 用 isCurrentVersion 判定（L237-L244）：

```java
boolean isCurrentVersion = (Boolean)this.getModel().getValue("iscurrentversion");
if (BillOperationStatus.AUDIT == billStatus && isCurrentVersion) {
    schemeId = this.getSourceVid(schemeId);                           // 查源版本 id
}
```

### 8.2 sourcevid 链式追溯

```
V1 (id=A, boid=A, sourcevid=0)
   ↓ confirmchange
V2 (id=B, boid=A, sourcevid=A)                  ← V2.sourcevid 指向 V1.id
   ↓ confirmchange
V3 (id=C, boid=A, sourcevid=B)
```

`DynaAuthSchemePlugin.getSourceVid` (L290-L293)：
```java
private long getSourceVid(long schemeId) {
    HRBaseServiceHelper schemeHelper = new HRBaseServiceHelper("hrcs_dynascheme");
    return schemeHelper.queryOriginalOne("sourcevid", schemeId).getLong("sourcevid");
}
```

### 8.3 复制方案场景

复制时（L246-L248）从 customParam 拿 sourceSchemeId · 走与变更不同的入口：

```java
if (null != isCopy && sourceSchemeIdNotUsed) {
    schemeId = (Long)this.getView().getFormShowParameter().getCustomParam("sourceSchemeId");
    this.getPageCache().put("sourceSchemeIdUsed", "1");                // 防多次重复 reload
}
```

→ 复制不是变更 · 是新建一个 boid · `DynaSchemeRoleAssignDetailBean.setEntryId(roleEntryId)` 给每行重新分配 entryId（L256-L263 · `ORM.genLongIds("hrcs_dynascheme.roleentry", size)`）。

---

## 九、setadminrange / addrole 子页面跳转规则

| opKey | 跳转表单 | 关键 customParam | 来源代码 |
|---|---|---|---|
| `setadminrange` | (内部 DynaAuthSchemeServiceHelper.showAdminRangeDetail · 看 helper 实现) | `id` (方案 id) | DynaAuthSchemePlugin.afterDoOperation L527-L528 |
| `addrole` | `hrcs_dyscassignroledetail` | `dynaSchemeBean` (DynaSchemeRoleAssignDetailBean JSON) | showRoleDetails L656-L671 |
| `assignrecord` | `bos_list` (showFormId) + `hrcs_userrolerelat` (billFormId) | `schemeNumber` + 过滤 sourcetype=4 | DynaAuthSchemeListPlugin.afterDoOperation L143-L158 |

---

## 十、已建机读规则索引（rules_chain_all.json + form_lifecycle_rules.json）

| 文件 | 含义 |
|---|---|
| `rules_chain_all.json` | 60 opKey 的执行链 · 每个 opKey 列 `executionChain` / `validators` / `businessRules` / `mines`（机读） |
| `form_lifecycle_rules.json` | 4 个反编译 FormPlugin 类的 lifecycleMethods（preOpenForm / beforeBindData / beforeDoOperation / afterDoOperation / propertyChanged / closedCallBack 等）（机读） |
| `_auto_rules.md` | listRules + formRules 实抓（机读 · 当前 2 条） |
| `_auto_rules_deep.md` | 字段级业务规则（FormulaProp / RequiredProp / DefaultValueProp 等）（机读） |

→ Claude 做需求分析时 · 先读 rules_chain_all.json 的 opKey 列表 · 再 form_lifecycle 看 FormPlugin 业务点 · 最后看本文档的人读侧总结。

---

## 十一、verified · 真实 formRule（listRules 实抓）

来自 `_auto_rules.md`：

| ruleId | 类型 | preCondition | 描述 |
|---|---|---|---|
| `4VQXGEWQ=LRS` | formRule | `authaction = '1' or authaction = '3'` | 显示分配场景 flex |
| `4VQXGEWQBD36` | formRule | `authaction = '2' or authaction = '3'` | 显示取消场景 flex |

→ 这是**元数据级 formRule**（与 setRequiredField 的 Java 端逻辑配合 · 双重保障）。

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin -->

## chgaction 实证补充（DynaAuthSchemePlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemePlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRCustomControlPlugin -->

## chgaction 实证补充（HRCustomControlPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRCustomControlPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRCustomControlPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRCustomControlPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin -->

## chgaction 实证补充（DynaAuthSchemeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hrcs_dynascheme` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_dynaschorg` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_dynaschdimgrp` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_dynaschdatarule` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |
| `hrcs_dynaschfield` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaAuthSchemeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp -->

## chgaction 实证补充（DynaAuthSchemeOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp -->

## chgaction 实证补充（DynaAuthSchemeSaveSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeSaveSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp -->

## chgaction 实证补充（DynaAuthSchemeAuditOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp -->

## chgaction 实证补充（DynaAuthSchemeConfirmChangeOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not support" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaAuthSchemeConfirmChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
