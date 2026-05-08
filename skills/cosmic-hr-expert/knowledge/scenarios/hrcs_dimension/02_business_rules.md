# 业务规则 · 维度管理 (hrcs_dimension)

> **状态**: 🟢 基于反编译 4 类（DimensionNewEdit / DimensionList / DimensionDeleteOp / HRAdminStrictPlugin）+ opkeys_index.json 44 opKey + scene_doc.json 33 字段
> **confidence**: verified
> **数据源**: CFR 反编译 + OpenAPI (2026-04-28)

> 📌 **本文档作用**：把 `DimensionNewEdit` / `DimensionList` / `DimensionDeleteOp` / `HRAdminStrictPlugin` 反编译类里的业务规则提炼成可机读 + 可人读的清单。**ISV 扩展时不要打破这些规则** —— 否则会跟标品冲突。

---

## 一、字段必填规则（动态计算 · datasource 驱动）

dimension 的必填字段**不是固定的** · 而是由 `datasource` 字段值动态决定。`DimensionNewEdit.showEnumCtrl` (L145-L203) + `checkEntitytype` (L326-L362) 是规则源头。

### 1.1 datasource 必填（恒定）

`scene_doc.json` 实抓 `datasource.required = true`。这是 dimension 表唯一**真正不动的必填字段**。

### 1.2 showtype 必填（恒定）

`scene_doc.json` 实抓 `showtype.required = true`。但**当 entitytype 不继承 hbp_bd_treetpl_all 时 · showtype 强制设 list 且 disable**（DimensionNewEdit.handleShowTypeForEntityType L205-L215）。

### 1.3 entitytype / hrbu / org_classify · 动态必填矩阵

| datasource 取值 | entitytype 必填 | hrbu 必填 | org_classify 必填 | 实证位置 |
|---|---|---|---|---|
| `enum` | ❌ setMustInput(false) | ❌ false | ❌ false | DimensionNewEdit.showEnumCtrl L156-L158 |
| `basedata` | ✅ true | ❌ false | ❌ false | L175-L177 |
| `hrbu` | ❌ false | ✅ true | ❌ false | L186-L188 |
| `orgteam` | ✅ true | ✅ true | ✅ true | L195-L198 |

⚠️ ISV 改 datasource 联动时必须复制这 4 行规则 · 否则用户可能跳过必填校验做出错单。

### 1.4 entry 子表字段动态必填

| datasource | entry.value 必填 | entry.displayvalue 必填 | 实证位置 |
|---|---|---|---|
| `enum` | ✅ entry.setMustInput("value", true) | ✅ entry.setMustInput("displayvalue", true) | L169-L170 |
| 其他 | ❌ false | ❌ false | L178-L179 / L188-L189 / L199-L200 |

---

## 二、字段联动规则（PropertyChanged）

### 2.1 datasource 切换 · 5 路联动

`DimensionNewEdit.propertyChanged` L221-L226（`case "datasource"`）：

```java
case "datasource": {
    String newValue = (String)changeDatas[0].getNewValue();
    this.showEnumCtrl(newValue);    // 4 类 datasource 走不同分支 · 见 03_model_design.md 第四章
    break;
}
```

**联动效应**（按 datasource 新值）：

| 新 datasource | UI 动作 |
|---|---|
| `enum` | 显示 dimensionenum（entry 分录）· hrbu 清空 · 已被引用维度的 entry value 锁定 |
| `basedata` | 隐藏 entry · 删 entry 数据 · entitytype 必填 · showtype 强制 disable · hrbu 清空 |
| `hrbu` | 隐藏 entry · 删 entry 数据 · hrbu 必填 · showtype enable |
| `orgteam` | 隐藏 entry · 显示 org_classify · entitytype/hrbu/org_classify 全必填 · entitytype 强制 setValue("haos_adminorgdetail") |

### 2.2 entitytype 切换（datasource=basedata 时）· 双闸校验

`DimensionNewEdit.propertyChanged` L227-L232：

```java
case "entitytype": {
    if (!HRStringUtils.equals("basedata", (String)this.getModel().getValue("datasource"))) break;
    this.limitBasedataType((DynamicObject)changeDatas[0].getNewValue());      // 闸 1：业务对象必须有 number+name
    this.handleShowTypeForEntityType();                                         // 闸 2：树形显示需继承 hbp_bd_treetpl_all
    break;
}
```

**闸 1 · limitBasedataType (L236-L257)**：
- 检查新选业务对象的 properties 是否同时有 `name` 和 `number` 字段
- 没有 → showTipNotification "当前业务对象无编码或名称字段，不允许配置维度。"
- 走 model.beginInit() / endInit() 防死循环（PR-004）· 设 entitytype 为空

**闸 2 · handleShowTypeForEntityType (L205-L215)**：
- 检查新选业务对象的 InheritPath 是否包含 `hbp_bd_treetpl_all` 的 InheritPath（即"是不是树形基础资料"）
- 不包含 → 强制 `setValue("showtype", "list")` + setEnable(showtype, false)
- 包含 → setEnable(showtype, true) 让用户自由选

---

## 三、F7 选择规则

### 3.1 entitytype F7 过滤 · `modeltype = BaseFormModel`

`DimensionNewEdit.beforeF7Select` (L364-L372) · 当 F7 字段是 entitytype 时：

```java
QFilter filter = new QFilter("modeltype", "=", "BaseFormModel");
lsp.getListFilterParameter().setFilter(filter);
lsp.setFormId("bos_listf7");
```

**为什么**：维度只允许挂在"基础资料形态"业务对象上 · BillFormModel/DynamicFormModel 等不允许（这跟 dynascheme 完全不同 · dynascheme 没限制业务对象）。

⚠️ ISV 加 entitytype 候选过滤时（如限定到 hr 域业务对象） · 必须**保留 modeltype=BaseFormModel** · 否则用户能选到非基础资料业务对象 · 后续 limitBasedataType 还是会拒绝 · 不如一开始就过滤掉。

### 3.2 entitytype 监听器注册（registerListener L98-L102）

```java
public void registerListener(EventObject e) {
    super.registerListener(e);
    BasedataEdit entryEntityEdit = (BasedataEdit)this.getView().getControl("entitytype");
    entryEntityEdit.addBeforeF7SelectListener(this);
}
```

**唯一注册** · 没有 hrbu / org_classify 等的 F7 监听器（这跟 dynascheme 大量注册 admingroup/role/actype F7 监听不同）。

---

## 四、按钮 / 工具栏点击规则

### 4.1 表单工具栏 `refrole` 跳转角色清单（DimensionNewEdit.beforeItemClick L259-L270）

```java
public void beforeItemClick(BeforeItemClickEvent evt) {
    super.beforeItemClick(evt);
    String itemKey = evt.getItemKey();
    if (HRStringUtils.equals(BAR_REFROLE, itemKey)) {        // BAR_REFROLE = "refrole"
        long dimensionId = this.getModel().getDataEntity().getLong("id");
        FormShowParameter fsp = new FormShowParameter();
        fsp.setFormId("hrcs_refdetails");
        fsp.setCustomParam("dimension.id", dimensionId);
        fsp.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm(fsp);
    }
}
```

→ 跳转 hrcs_refdetails（角色引用清单子页面）· Modal 弹窗 · 带 dimension.id 参数。

### 4.2 列表工具栏 `tbldisable` 二次确认（DimensionList.beforeItemClick L29-L34）

```java
public void beforeItemClick(BeforeItemClickEvent evt) {
    String itemKey = evt.getItemKey();
    if (HRStringUtils.equals("tbldisable", itemKey)) {
        this.getView().showConfirm("禁用维度后·不允许在'业务对象维度映射'中使用·已有的角色维度的数据权限不受影响",
            MessageBoxOptions.OKCancel,
            new ConfirmCallBackListener("disable_conform", this));
        evt.setCancel(true);
    }
}
```

**callBackId = "disable_conform"** · 用户点 Yes 后调 `getView().invokeOperation("disable")` 走标品 disable opKey。

⚠️ **ISV 不要改 callBackId 名** · 标品 confirmCallBack 用此名做路由（DimensionList.confirmCallBack L37-L43）。

---

## 五、save / submit 业务校验规则（DimensionNewEdit.beforeDoOperation L272-L296）

### 5.1 modifytime 自动重置（恒定）

```java
this.getModel().setValue("modifytime", new Date());
```

ISV 别再写 modifytime 维护代码 · 标品恒定。

### 5.2 hrbu 类型自动设 entitytype=bos_org

```java
if (HRStringUtils.equals((String)this.getModel().getValue("datasource"), "hrbu")) {
    this.getModel().setValue("entitytype", "bos_org");
}
```

**为什么**：hrbu（职能类型）在 entityctrl 下游需要绑定到组织 · 因此 entitytype 写死 bos_org（统一处理）。

### 5.3 hadConfirm 缓存跳过（防止 checkEnumChange 二次确认无限循环）

```java
String hadConfirm;
if ((hadConfirm = this.getPageCache().get("hadConfirm")) != null) {
    this.getPageCache().remove("hadConfirm");
    args.setCancel(false);
    return;
}
```

→ 用户在 enum 修改二次确认弹框中点 Yes · setPageCache("hadConfirm", "confirmed") 后再 invokeOperation("save") · 第二次进 beforeDoOperation 时跳过 checkEnumChange 直接放行。

### 5.4 checkEntitytype 校验（L286-L289）

`checkEntitytype()` (L326-L362) 三层校验：
1. **datasource=basedata + entitytype 空** → showError "请选择业务对象。"
2. **datasource=basedata + showtype=checkbox** → showError "基础资料类型维度的显示类型不能为复选框"
3. **datasource=basedata + showtype=tree + entitytype 不继承 hbp_bd_treetpl_all** → showError "业务对象没有继承平台树形基础资料模板（bos_basetreetpl），显示类型不允许设置为树形"
4. **datasource=enum + entry 空** → showError "请设置枚举值"
5. **datasource=enum + entry.displayvalue 重复** → showError "枚举名称重复，请重新填写。"

→ 任一失败 · args.setCancel(true) 阻断 save。

### 5.5 EDIT 状态 + datasource=enum · checkEnumChange（L290-L294）

```java
OperationStatus status = this.getView().getFormShowParameter().getStatus();
String datasource = (String)this.getModel().getValue("datasource");
if (status.equals(OperationStatus.EDIT) && datasource.equals("enum")) {
    this.checkEnumChange(args);
}
```

`checkEnumChange` (L308-L324) 比对 model.entry 跟 DB.entry 的 displayvalue → value 映射 · 任一对不上就弹"调整枚举值会影响角色维度·确定修改吗？"二次确认。

---

## 六、afterBindData 启动规则（DimensionNewEdit.afterBindData L104-L143）

### 6.1 ctrlentry 反查灌库（核心）

EDIT 状态 + 反查 hrcs_entityctrl 有数据时：
1. 显示 ctrlentry 控件（setVisible(true, "entityctrl")）
2. 反查 `hrcs_entityctrl` · 按 `entryentity.dimension = dimensionId` 过滤
3. 对每行 hrcs_entityctrl 取它的 entityFieldMap（业务对象字段 key→显示名映射）
4. 对每行 entryentity（dimension match）· createNewEntryRow 加 ctrlentry 行
5. setValue: entity / authrange / propkey / propname / issyspreset1 / ismust / desc

ADDNEW 状态时直接 `setVisible(false, "entityctrl")` 隐藏 ctrlentry。

### 6.2 model.beginInit / endInit 防死循环（PR-004）

```java
this.getModel().beginInit();
// 大量 setValue ...
this.getModel().endInit();
this.getView().updateView(KEY_CTRL_ENTRY);
this.getModel().setDataChanged(false);
```

→ 标准 PR-004 套路 · ISV 加 ctrlentry 类逻辑必须复用。

### 6.3 datasource 默认显示

```java
Object datasource = this.getModel().getValue("datasource");
this.showEnumCtrl(datasource.toString());      // 启动时按 datasource 重设 UI/必填
```

---

## 七、删除前置校验规则（DimensionDeleteOp）

`DimensionDeleteOp` (DimensionDeleteOp.java L17-L22) 唯一逻辑是注册 Validator：

```java
public final class DimensionDeleteOp extends HRDataBaseOp {
    public void onAddValidators(AddValidatorsEventArgs args) {
        args.addValidator(new DimensionDeleteValidator());
    }
}
```

**DimensionDeleteValidator 反编译没读到具体内容** · 但根据 02 业务规则上下文可推断：
- 反查 `hrcs_entityctrl` 是否有引用此 dimension（"业务对象维度映射"中是否使用）
- 反查 `hrcs_dynascheme.condition` 是否引用此 dimension（动态授权方案规则参数）
- 反查 `hrcs_datarule` 是否引用此 dimension
- 任一引用 → 阻断 delete · addErrorMessage("不能删除：维度已被 X 个 Y 引用")

⚠️ **ISV 不要继承 DimensionDeleteOp 来加自己的删除校验** · 走"并列挂新 OP" + 自建 Validator + onAddValidators 注册（PR-001 / PR-010）。

---

## 八、HR 管理员准入规则（HRAdminStrictPlugin）

挂在 dimension 表单的 preOpenForm（11 个 hrcs 表单共用） · `HRAdminStrictPlugin.preOpenForm` (L29-L37)：

```java
public void preOpenForm(PreOpenFormEventArgs args) {
    super.preOpenForm(args);
    FormShowParameter fsp = args.getFormShowParameter();
    // F7 lookup 模式跳过
    if (fsp instanceof ListShowParameter && ((ListShowParameter)fsp).isLookUp()) return;
    HRAdminStrictPlugin.showMesIfUserIsNotAdmin(args);
}
```

**校验逻辑** (L39-L52)：
1. RequestContext.get().getCurrUserId() 取当前用户
2. PermissionServiceHelper.isAdminUser + PermCommonUtil.isCosmicUser 双闸（不是任一就拒）
3. HRAdminService.isHrAdmin() 必须 true（不是 HR 领域管理员就拒）
4. setCancel(true) + setCancelMessage "您无法访问该功能·因为您不是HR领域管理员"

⚠️ **复用即可 · 不要继承** · 改了等于改全部 hrcs 11 表单。

---

## 九、原始 formRule / bizRule（listRules 实抓 · 9 条）

| ruleId | 类型 | preCondition | 描述 |
|---|---|---|---|
| `21DMN6FC+O9Y` | formRule | `issyspreset = true` | 系统预置数据 |
| `2ABS=9Q7JA/3` | formRule | `datasource='hrbu'` | 类型为职能类型 |
| `2ABSN5/LA5W1` | formRule | `datasource='basedata' or datasource='orgteam'` | 类型为基础资料 |
| `2ABT6F+ERI9Z` | formRule | `datasource='enum'` | 类型为枚举 |
| `2MH+AVNU/Q=G` | formRule | `datasource = 'orgteam'` | 类型为组织团队 |
| `2ABTZN/1ZM26` | bizRule | `datasource='enum'` | 类型为枚举 |
| `2ABXRVK/YNI9` | bizRule | `datasource='hrbu'` | 类型为职能类型 |
| `2MH/XQ4VCW41` | bizRule | `datasource = 'orgteam'` | 类型为组织团队 |
| `2NWLWH9R5+M7` | bizRule | `datasource = 'basedata'` | 类型为基础资料 |

→ 这些 rule 是**元数据级别的可见性 / 必填性 / 联动规则**（在元数据设计器里手动配的 ruleConfig）· 跟 Java 反编译里的 datasource 联动**职责分工**：

| 元数据 rule（formRule/bizRule） | Java 插件代码 |
|---|---|
| 跨字段的可见性 / 必填快速联动（如 datasource=enum 时显示 entry 控件） | 复杂业务校验 / 反查 / 二次确认 / 跳子页面 |
| 在元数据设计器配置 · 不需要写代码 · 走平台 ruleEngine | DimensionNewEdit 等 Java 类 · 走插件链 |
| 不能调 ServiceHelper · 不能写下游表 | 可以调 ServiceHelper / 反查 / setEnable / showConfirm 等 |

⚠️ **ISV 改 datasource 联动时**：能在元数据 rule 配的（如新增 datasource=position 选项时的可见性切换）优先在元数据 ruleConfig 配 · 复杂逻辑（如反查下游引用、二次确认）才写 Java 插件。

---

## 十、规则不变量速查（ISV 必守）

```
1. datasource 是模式开关 · 不可改（isvCanModify=false）
2. showtype 不可改（isvCanModify=false）· 改 datasource 时自动联动重置
3. datasource=hrbu save 时 entitytype 必须 = bos_org（标品强制）
4. datasource=orgteam 启动时 entitytype 必须 = haos_adminorgdetail（标品强制）
5. datasource=basedata + showtype=checkbox 不允许（标品拒绝）
6. datasource=basedata + showtype=tree → entitytype 必须继承 hbp_bd_treetpl_all
7. datasource=basedata 选 entitytype 时 · 业务对象必须有 number + name 字段
8. datasource=enum 时 entry 不能空 · displayvalue 不能重复
9. EDIT + datasource=enum + 改了 entry value · 必须二次确认（hadConfirm 缓存跳过）
10. dimension 被角色引用时 · entry value 字段 setEnable(false) · UI 锁定（后端不强制）
11. 系统预置维度（issyspreset=true）不能改 number（PR-007）
12. modifytime save 时自动写 new Date()（标品恒定）
13. F7 entitytype 强制 modeltype=BaseFormModel（业务对象限基础资料形态）
14. delete 走 DimensionDeleteValidator 反查下游（不要继承覆盖）
15. list disable 必须二次确认（callBackId=disable_conform）
16. 非 HR 管理员被 HRAdminStrictPlugin 在 preOpenForm 拒绝（hrcs 11 表单共用）
```

→ ISV 写 Validator/OP/FormPlugin 不要打破上述任一项 · 否则跟标品冲突。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit -->

## chgaction 实证补充（DimensionNewEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionNewEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList -->

## chgaction 实证补充（DimensionList 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.perm.dimension.DimensionList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp -->

## chgaction 实证补充（DimensionDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.perm.DimensionDeleteOp -->
