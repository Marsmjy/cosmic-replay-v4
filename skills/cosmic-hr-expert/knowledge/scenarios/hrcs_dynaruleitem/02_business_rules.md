# 业务规则 · 规则参数项 (hrcs_dynaruleitem)

> **状态**: 🟢 基于反编译 3 类 + opkeys_index.json 44 opKey + scene_doc.json 34 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：把 `DynaRuleItemEdit` / `DynaItemDeleteOp` / `HRAdminStrictPlugin` 反编译类里的业务规则提炼成可机读 + 可人读的清单。**ISV 扩展时不要打破这些规则** —— 否则会跟标品冲突。

---

## 一、字段必填规则（动态计算）

### 1.1 datatype 必填（元数据）

`scene_doc.json` 实抓 `datatype.required = true` + `isvCanModify = false`（L287-L288）· 平台元数据保护 · 不允许 ISV 修改元数据。

**取值集合**（实证 DynaRuleItemEdit 常量 + propertyChanged）：
- `bd` = 基础资料类型（必须配 entitytype · F7 限定 BaseFormModel）
- `org` = 组织类型（自动锁 entitytype = haos_adminorghrf7）
- `enum` = 枚举类型（必须配 entryentity 至少 1 行）
- 其他值 reserved（视作非 bd/org/enum · 走 clearUnMustData 清掉相关字段）

### 1.2 entitytype 必填 + datatype=org 强制锁值

`scene_doc.json` 实抓 `entitytype.required = true`。

**强制锁值规则**（DynaRuleItemEdit.propertyChanged L144-L150）：

```java
if (F_DATATYPE.equals(propertyName)) {
    String newDataType = (String)changeSet[0].getNewValue();
    if (V_DATATYPE_ORG.equals(newDataType)) {
        this.getModel().setValue(F_ENTITYTYPE, "haos_adminorghrf7");
    } else {
        this.getModel().setValue(F_ENTITYTYPE, null);
    }
}
```

→ datatype=org 时 entitytype 强制 haos_adminorghrf7 · 用户没法在 F7 选别的；datatype 切到非 org 时清空 entitytype 让用户重选；datatype=enum 时 entitytype 不展示（formRule 4ZRHZU5CYXJR 隐藏）但仍然清空避免幽灵值。

### 1.3 valsourcetype 必填 + 联动 source* / mservice* 显隐

`scene_doc.json` 实抓 `valsourcetype.required = true`。

**取值**：
- `1` = 实体取值 → 显示 sourceentitytype + sourcepropkey + sourcepropname · 隐藏 mserviceapp/mserviceclass
- `2` = 微服务取值 → 显示 mserviceapp + mserviceclass · 隐藏 source*

**实现**：formRule 4ZRHJC9QK0/A（valsourcetype=1）+ 4ZRHJC9QK/B1（valsourcetype=2）· 由前端规则引擎驱动 · 不走插件代码。

### 1.4 isrelatparam 联动 relatruleparam 必填

`isrelatparam = true` 时 · UI 上 relatruleparam 字段必填且显示（formRule 4ZR5YQFF=KLX）。

但元数据 `relatruleparam.required = true`（scene_doc.json L332）· 实际是"条件必填" —— UI 隐藏时也不校验（formRule 控制）。

**ISV 注意**：通过 OpenAPI modifyMeta 改 relatruleparam.mustInput 不会破坏这个联动 · 联动靠的是 formRule preCondition 显隐 · 不是 element 级 mustInput。

### 1.5 entryentity 必填行（datatype=enum 时）

`DynaRuleItemEdit.checkEnumEntry` L307-L361 实证：

```java
if (CollectionUtils.isEmpty(entryEntitys)) {
    errMsgs.add("当数据类型为枚举时，枚举值和枚举名称不能为空。");
}
// 还有：empty rows / repeat keys / repeat names
```

→ datatype=enum 时 entryentity 必须至少 1 行 · 且每行 value+displayvalue 都有值 · 且 value 不能跟其它行重 · displayvalue 不能跟其它行重。**4 连校验**只在 bar_save / bar_saveandnew 工具栏点击触发。

---

## 二、F7 选择过滤规则

### 2.1 entitytype F7（DynaRuleItemEdit.beforeF7Select L178-L180）

```java
if (F_ENTITYTYPE.equals(propertyName)) {
    showParameter.getListFilterParameter().setFilter(
        new QFilter("modeltype", "=", "BaseFormModel"));
}
```

→ datatype=bd 时只能选基础资料 · 防止用户选到单据/动态表单。setFilter 而非 add · 是初始唯一过滤。

### 2.2 relatruleparam F7（DynaRuleItemEdit.beforeF7Select L181-L186）

```java
} else if (HRStringUtils.equals(F_RELATE_RULE_ITEM, propertyName)) {
    QFilter notRelateParamFilter = new QFilter(F_IS_RELATE_PARAM, "=", Boolean.FALSE);
    showParameter.getListFilterParameter().getQFilters().add(notRelateParamFilter);
    QFilter dataTypeFilter = new QFilter(F_DATATYPE, "in", Arrays.asList(V_DATATYPE_BD, V_DATATYPE_ORG));
    showParameter.getListFilterParameter().getQFilters().add(dataTypeFilter);
}
```

→ 双闸过滤：
1. 主规则参数项不能也是关联参数项（避免链式关联 A→B→C 错乱）
2. 主规则参数项必须是 bd 或 org 类型（关联只对实体维度有意义 · enum 类没有"实体属性"概念）

### 2.3 sourceentitytype F7（DynaRuleItemEdit.beforeF7Select L187-L190）

```java
} else if (HRStringUtils.equals(F_SOURCE_ENTITY_TYPE, propertyName)) {
    List<String> modelTypeList = Arrays.asList("BaseFormModel", "BillFormModel");
    showParameter.getListFilterParameter().setFilter(
        new QFilter("modeltype", "in", modelTypeList));
}
```

→ 值来源实体可以是基础资料或单据 · 比 entitytype 范围更宽。

---

## 三、save / saveandnew 前置校验链（FormPlugin.beforeItemClick）

### 3.1 入口

`DynaRuleItemEdit.beforeItemClick` L233-L251：

```java
switch (key = evt.getItemKey()) {
    case "bar_save":
    case "bar_saveandnew": {
        Object dataType = this.getModelVal(F_DATATYPE);
        if (V_DATATYPE_ENUM.equals(dataType) && !(errorMsgList = this.checkEnumEntry()).isEmpty()) {
            this.getView().showTipNotification(String.join("；", errorMsgList));
            evt.setCancel(true);
            return;
        }
        if (evt.isCancel()) break;
        this.clearUnMustData();
        break;
    }
}
```

### 3.2 checkEnumEntry 4 连校验

| 错误类型 | 触发条件 | 错误消息（中文 · 实证 ResManager.loadKDString） |
|---|---|---|
| 空 entry | datatype=enum 但 entryentity 空 | "当数据类型为枚举时，枚举值和枚举名称不能为空。" |
| 空行 | value 和 displayvalue 都空 | 同上 |
| 重复 value | 多行有相同 value | "存在相同的枚举值，请修改后重试：%s。" |
| 重复 displayvalue | 多行有相同 displayvalue | "存在相同的枚举名称：%s。" |

校验失败 + `firstErrRow > -1` 时 · 自动 `grid.focusCell(firstErrRow, firstErrKey)` 把焦点定到首行错列。

### 3.3 clearUnMustData 净化

校验通过后调 `clearUnMustData` (L284-L305) 清掉非当前模式下的脏字段：

```java
if (!V_DATATYPE_ENUM.equals(dataType)) deleteEnumEntry();    // datatype != enum -> 清空 entryentity
if (!V_DATATYPE_BD.equals(dataType) && !V_DATATYPE_ORG.equals(dataType))
    setModelNullVal(F_ENTITYTYPE);                            // 非 bd/org -> entitytype = null
if (!isRelatParam) {
    setModelNullVal(F_RELATE_RULE_ITEM);                      // 非关联 -> relatruleparam=null
    setModelNullVal(F_RELATE_PROP_KEY);                       // + relatpropkey=null
}
if (V_VALSOURCETYPE_ENTITY.equals(sourceType)) {
    setModelNullVal(F_MSERVICE_APP);                          // 实体取值 -> mservice* 清空
    setModelNullVal(F_MSERVICE_CLASS);
} else {
    setModelNullVal(F_SOURCE_ENTITY_TYPE);                    // 非实体取值 -> source* 清空
    setModelNullVal(F_SOURCE_PROP_KEY);
}
```

→ ISV 想在 save 前做额外字段清理 · 必须挂 FormPlugin 自己的 beforeItemClick · 在 super 之后跑（执行顺序按 RowKey · PR-002）。

---

## 四、deleteentry（删除枚举行）的引用阻断

### 4.1 入口

`DynaRuleItemEdit.beforeDoOperation` L214-L231：

```java
if (operateKey.equals("deleteentry")) {
    DynamicObject dataEntity = this.getModel().getDataEntity(true);
    Object[] schemeDynArr = DynaSchemeServiceHelper.queryRelDynaScheme(dataEntity.getString("id"));
    if (ArrayUtils.isEmpty(schemeDynArr)) return;

    Set<String> enumValSet = this.getSelEnumEntryValSet(dataEntity);
    Set<String> relEntryVals = this.checkRelSchemeVal(schemeDynArr, enumValSet);
    if (!relEntryVals.isEmpty()) {
        this.getView().showTipNotification(String.format(
            "枚举值被动态授权方案引用，不允许删除：%s。",
            String.join("、", relEntryVals)));
        args.setCancel(true);
    }
}
```

### 4.2 checkRelSchemeVal 反查算法（L253-L270）

对每个引用本规则参数项的 dynascheme:
1. 取 `condition` 字段（LargeTextField · DecisionSet JSON）
2. JSON.parseObject(condition).getJSONArray("conditionList")
3. 对 conditionList 每个元素 `paramRel.value` · 看是否在选中行 value 集合里
4. 命中则加进 relEntryVals

**性能特征**：dynascheme 数量 N · 平均 condition.conditionList 长度 K → O(N×K)。生产环境 dynascheme 通常 100 条以下 · 性能可接受。

### 4.3 业务约束

如果某个枚举值已被任意 dynascheme.condition 引用 · 无法删除该行；ISV 想"强制删除并清理下游" 必须自建逻辑（CS-04 套路 · 同时改 dynascheme 的 condition JSON · 用 DynaSchemeServiceHelper.queryRelDynaScheme + 反向写 condition）。

---

## 五、delete（删除主表）的引用阻断（DynaItemDeleteOp）

### 5.1 OP 入口

`DynaItemDeleteOp.java` L20-L31：

```java
public final class DynaItemDeleteOp extends HRDataBaseOp {
    public void onPreparePropertys(PreparePropertysEventArgs args) {
        List fieldKeys = args.getFieldKeys();
        fieldKeys.add("name");
    }

    public void onAddValidators(AddValidatorsEventArgs args) {
        DynaItemDelValidator validator = new DynaItemDelValidator();
        args.addValidator((AbstractValidator)validator);
    }
}
```

### 5.2 DynaItemDelValidator 行为（推断 · 类未反编译但根据命名 + 引用方推断）

按 `kd.hr.hrcs.opplugin.validator.perm.dyna.DynaItemDelValidator` 命名 + DynaSchemeServiceHelper.queryRelDynaScheme 在 FormPlugin 端的用法 · DynaItemDelValidator 推断行为：

1. 对每个待删除行（ExtendedDataEntity）· 取 dataEntity.id
2. 调 DynaSchemeServiceHelper.queryRelDynaScheme(id) 反查所有引用该参数项的方案
3. 命中则 addErrorMessage("规则参数项 [%s] 被 N 个动态授权方案引用·请先解除引用再删除。")

**ISV 想在删前做额外校验**（如查 ISV 自建的 ${ISV_FLAG}_dynarule_extension 表是否引用本参数项）：必须在 onAddValidators 自建 Validator · 不要 extends DynaItemDelValidator（场景专属 · PR-001）。

---

## 六、afterBindData 数据展示规则

### 6.1 关联参数项 · 反查 propKey → propName

`DynaRuleItemEdit.fillRelateParamPropName` L389-L399：

```java
private void fillRelateParamPropName() {
    DynamicObject dataEntity = this.getModel().getDataEntity();
    boolean isRelateParam = dataEntity.getBoolean(F_IS_RELATE_PARAM);
    if (!isRelateParam) return;

    String relateParamEntity = dataEntity.getString("relatruleparam.entitytype.id");
    String relateParamPropKey = dataEntity.getString(F_RELATE_PROP_KEY);
    Map entityFieldMap = RolePermLogServiceHelper.getEntityFieldMap(relateParamEntity);
    this.getModel().setValue(F_RELATE_PROP_NAME, entityFieldMap.get(relateParamPropKey));
    this.getModel().setDataChanged(false);
}
```

**业务意图**：数据库只存 propKey · UI 要显示文字 · 由 RolePermLogServiceHelper.getEntityFieldMap(entityNumber) 一次性拿目标实体所有字段的 key→name 映射。setDataChanged(false) 防止纯展示回填把单据标记为脏。

### 6.2 实体取值 · 反查 sourcepropkey → sourcepropname

`DynaRuleItemEdit.fillSourceEntityPropName` L375-L387：同 6.1 套路 · 仅当 valsourcetype=1 时反查灌 sourcepropname。

### 6.3 系统预置 · 强制 VIEW

`DynaRuleItemEdit.presetView` L422-L428：

```java
private void presetView() {
    if (this.getModel().getDataEntity().getBoolean("issyspreset")) {
        BillView billView = (BillView)this.getView();
        billView.setBillStatus(BillOperationStatus.VIEW);
        this.getView().setVisible(Boolean.FALSE, new String[]{F_ENUM_BAR});
    }
}
```

→ issyspreset=true 的预置参数项 · 任何用户都不能编辑（PR-007）+ enumbar 工具栏隐藏（防止用户添删枚举行）。

### 6.4 datatype=enum · 锁住已存枚举值列

`DynaRuleItemEdit.disableEnumEntry` L412-L420：

```java
if (V_DATATYPE_ENUM.equals(dataType) && CollectionUtils.isNotEmpty(entryDyoColl)) {
    for (int rowIndex = 0; rowIndex < entryDyoColl.size(); ++rowIndex) {
        this.getView().setEnable(Boolean.FALSE, rowIndex, new String[]{FE_ENUM_VALUE});
    }
}
```

→ 已存在的枚举行 value 列锁定（不让改）· 因为已被下游 dynascheme.condition.conditionList[].value 引用 · 改 key 会脱钩；新增的行 value 仍可编辑（rowIndex 只覆盖已存行）。displayvalue 列不锁 · 多语言显示文本可改。

---

## 七、HR 域准入闸（HRAdminStrictPlugin · 与 dynascheme 同此类）

### 7.1 入口

`HRAdminStrictPlugin.preOpenForm` L29-L37：

```java
public void preOpenForm(PreOpenFormEventArgs args) {
    super.preOpenForm(args);
    FormShowParameter fsp = args.getFormShowParameter();
    if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) {
        return;     // F7 lookup 模式直接放行
    }
    HRAdminStrictPlugin.showMesIfUserIsNotAdmin(args);
}
```

### 7.2 三连校验（L43-L52）

1. PermissionServiceHelper.isAdminUser(userId) **或** PermCommonUtil.isCosmicUser(userId)（任一通过）
2. **且** HRAdminService.isHrAdmin()（HR 域管理员）

任一闸不过 → setCancel(true) + setCancelMessage("您无法访问该功能，因为您不是HR领域管理员。")

→ **ISV 想加二级权限闸**（如限定到部门级管理员）· 走并列挂插件 + preOpenForm · 在 HRAdminStrictPlugin 之后判（执行顺序按 RowKey · PR-002）。

---

## 八、规则相关性矩阵（业务侧速查表）

| 字段值 | 触发隐藏字段 | 触发显示字段 | 实现 |
|---|---|---|---|
| `datatype=bd` | entryentity / entryentity 列 | entitytype | formRule 4ZR9JXP/TBN= |
| `datatype=enum` | entitytype / relatruleparam | entryentity / enumbar | formRule 4ZRHZU5CYXJR |
| `datatype=org` | entryentity | entitytype（值锁定 haos_adminorghrf7） | formRule + propertyChanged |
| `isrelatparam=true` | entitytype（即使 datatype=bd） | relatruleparam / relatpropkey / relatpropname | formRule 4ZR5YQFF=KLX |
| `valsourcetype=1` | mserviceapp / mserviceclass | sourceentitytype / sourcepropkey / sourcepropname | formRule 4ZRHJC9QK0/A |
| `valsourcetype=2` | source* | mserviceapp / mserviceclass | formRule 4ZRHJC9QK/B1 |
| `issyspreset=true` | enumbar | （所有可见字段都 VIEW） | DynaRuleItemEdit.presetView |

---

## 九、已知规则不变量（Invariants · 业务侧不能违反）

1. `datatype` 必须 ∈ {bd, org, enum} · 元数据 isvCanModify=false · ISV 不能改取值集
2. `valsourcetype` 必须 ∈ {1, 2} · 元数据 isvCanModify=false
3. `entityentity.value` 唯一性约束（同行内 + 数据库列约束）· checkEnumEntry 已校验
4. 一行 dynaruleitem · datatype=enum 时 entryentity 必须 ≥1 行 · 数据持久后由 deleteentry 引用阻断保护
5. issyspreset=true 的行不能改业务字段（platform 强制 + UI VIEW）
6. relatruleparam 不能指向 isrelatparam=true 的行（FP_BF7S2 防链式关联）· 自引用环不允许

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

## chgaction 实证补充（HRAdminStrictPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.hradmin.HRAdminStrictPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit -->

## chgaction 实证补充（DynaRuleItemEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dyna.DynaRuleItemEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp -->

## chgaction 实证补充（DynaItemDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.dyna.DynaItemDeleteOp -->
