# 业务规则 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于反编译 3 类实证 + listRules 探针 + opKey 链分析
> **confidence**: verified
> **规则数**: 4 条（R-1 系统预置保护 · R-2 adminorgtypestd 引用锁定 · R-3 orgpattern 联动 · R-4 HIES 导入修正）

---

## R-1 · 系统预置数据不可修改

### 基本信息

| 属性 | 值 |
|---|---|
| **ruleId** | `0SWB2VGDUNX+` |
| **ruleType** | `formRule`（listRules 表单规则） |
| **enabled** | ❌ false（规则本身已禁用） |
| **preCondition** | `issyspreset = true` |
| **描述** | 系统预置数据不可修改 |
| **triggerField** | null（无绑定字段 · 进入编辑视图即触发）|

### 说明

**注意**：虽然 listRules formRule 的 `enabled=false`（已禁用），但 `issyspreset` 字段本身仍有多重保护机制：
1. `issyspreset` 字段的 `isvCanModify=false`（ISV 无法通过 modifyMeta 修改该字段属性）
2. 平台层系统字段保护（`minefield: red` 标记）
3. ISV 禁止继承 `AdminorgtypeEditPlugin` 绕过保护（PR-001）

**实际效果**：即使 listRules 规则被禁用，ISV 也无法轻易修改 issyspreset=true 的预置数据，因为字段本身受平台保护。

### 下游关联

系统预置的 adminorgtype 记录通常被更多 haos_adminorg 数据引用，因此被双重保护：
- 不可修改（issyspreset 字段保护）
- 通常也不可删除（R-2 机制：被引用后 adminorgtypestd 不可修改 · 间接保护整条记录）

---

## R-2 · adminorgtypestd 被引用后禁止修改（字段灰化）

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin` |
| **生命周期方法** | `beforeBindData` |
| **实证来源** | AdminorgtypeEditPlugin.java 反编译 |
| **触发时机** | 进入编辑视图时（beforeBindData 阶段） |
| **影响字段** | `adminorgtypestd` |

### 业务语义

当一条 `haos_adminorgtype` 记录的 `adminorgtypestd` 字段已被下游实体引用时，禁止用户修改该字段，防止破坏下游的数据一致性。引用检查使用平台提供的 `baseDataCheckReference` 接口（苍穹内置反向引用检查 API）。

### 反编译实证代码

```java
// AdminorgtypeEditPlugin.java - beforeBindData 方法
public void beforeBindData(EventObject e) {
    super.beforeBindData(e);
    DynamicObject dataEntity = this.getModel().getDataEntity();
    Long id = dataEntity.getLong("id");
    // id != 0L 表示是已存在记录（不是新建空白表单）
    // baseDataCheckReference 检查 adminorgtypestd 是否已被下游实体引用
    if (id != 0L && baseDataCheckReference(..., id).isRefence()) {
        // 被引用时 → adminorgtypestd 字段禁用（setEnable false · 灰化不可点击）
        this.getView().setEnable(Boolean.valueOf(false), new String[]{"adminorgtypestd"});
    }
}
```

### 触发路径

```
用户打开已有 adminorgtype 记录的编辑视图
    ↓
AdminorgtypeEditPlugin.beforeBindData 触发（#9 插件 · 第 1 个 beforeBindData）
    ↓
id != 0L → 非新建记录
    ↓
baseDataCheckReference(id).isRefence() 检查引用状态
    ↓ 有引用
getView().setEnable(false, ["adminorgtypestd"]) → adminorgtypestd 字段灰化
    ↓ 无引用
adminorgtypestd 字段正常可编辑
```

### 与三胞胎差异

三胞胎场景（haos_adminorgfunction / haos_adminorglayer / haos_orgchangereason）均无此引用检查逻辑。本场景独有，原因是 `adminorgtypestd` 决定了 `orgpattern`（组织形态），一旦 adminorgtypestd 已被下游引用，修改会导致联动关系混乱。

---

## R-3 · adminorgtypestd → orgpattern 自动联动填值

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin` |
| **生命周期方法** | `propertyChanged` |
| **实证来源** | AdminorgtypeEditPlugin.java 反编译 |
| **触发字段** | `adminorgtypestd` |
| **写入字段** | `orgpattern` |
| **核心依赖** | `AdminOrgTypeStdEnum.getOrgPatternIdById(stdId)` |

### 业务语义

用户在编辑视图选择或变更 `adminorgtypestd`（类型归属）字段时，系统根据 `AdminOrgTypeStdEnum` 枚举的映射关系自动填充 `orgpattern`（组织形态）字段，无需用户手动选择。`AdminOrgTypeStdEnum` 枚举定义了 adminorgtypestd 与 orgpattern 之间的标准化映射关系（苍穹 HR haos 域内部枚举类）。

### 反编译实证代码

```java
// AdminorgtypeEditPlugin.java - propertyChanged 方法
public void propertyChanged(PropertyChangedArgs e) {
    String name = e.getProperty().getName();
    if ("adminorgtypestd".equals(name)) {
        DynamicObject dynamicObject = (DynamicObject) this.getModel().getValue("adminorgtypestd");
        if (dynamicObject == null) {
            return;  // 清空 adminorgtypestd 时不联动
        }
        // AdminOrgTypeStdEnum 根据 adminorgtypestd 的 id 查对应 orgPattern 的 id
        this.getModel().setValue(
            "orgpattern",
            AdminOrgTypeStdEnum.getOrgPatternIdById(dynamicObject.getLong("id"))
        );
    }
}
```

### 联动链

```
用户在编辑视图选择 adminorgtypestd（类型归属）
    ↓
propertyChanged 事件 · name="adminorgtypestd"
    ↓
获取 adminorgtypestd DynamicObject · 取 id
    ↓
AdminOrgTypeStdEnum.getOrgPatternIdById(stdId) → 枚举映射 → orgPatternId
    ↓
getModel().setValue("orgpattern", orgPatternId) → orgpattern 字段自动填充
```

### 注意事项

1. **清空时不联动**：若 adminorgtypestd 被设为 null，则 propertyChanged 直接 return，原 orgpattern 值保留
2. **可以手工覆盖**：orgpattern 字段本身 isvCanModify=true，用户可在自动填充后手工修改
3. **PR-004 死循环风险**：ISV 若参考此模式实现类似联动，必须用 `beginInit/endInit` 包裹防止死循环（PR-004）

---

## R-4 · HIES 导入时 orgpattern 同步修正

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp` |
| **生命周期方法** | `beginOperationTransaction` |
| **实证来源** | AdminOrgTypeSaveOp.java 反编译 |
| **触发条件** | `importtype` 变量非空（HIES 导入场景专用） |
| **关联 opKey** | `importdata_hr`（HIES 导入数据）|

### 业务语义

当通过 HIES 导入行政组织类型数据时，orgpattern 字段可能因数据来源不规范而与 adminorgtypestd 不一致。`AdminOrgTypeSaveOp` 在 HIES 导入事务中检查并修正这种不一致性，确保 adminorgtypestd → orgpattern 联动关系在导入后正确。

### 反编译实证代码（逻辑摘要）

```java
// AdminOrgTypeSaveOp.java - beginOperationTransaction 方法
public void beginOperationTransaction(BeginOperationTransactionArgs e) {
    // 关键：只有 importtype 变量非空时才执行（HIES 导入场景标志）
    if (HRStringUtils.isEmpty(this.getOption().getVariables().get("importtype"))) {
        return;  // 非 HIES 导入 → 直接返回 · 不做任何修正
    }
    // HIES 导入场景：
    // 1. 读 AdminOrgTypeStdEnum 所有 orgPatternId 枚举值
    // 2. 从 bos_org_pattern 查对应实体
    // 3. 对导入数据按 adminorgtypestd 同步修正 orgpattern
}
```

### 触发条件分析

`importtype` 变量来自苍穹 HIES 导入框架，在 `importdata_hr` opKey 的执行链中由框架注入到 options.variables。普通保存（save opKey）不会有这个变量，所以 AdminOrgTypeSaveOp 的修正逻辑只在 HIES 导入时执行。

### 与 R-3 的区别

| 维度 | R-3（propertyChanged 联动）| R-4（beginOperationTransaction 修正）|
|---|---|---|
| 触发时机 | UI 交互时（用户选择 adminorgtypestd）| 事务执行时（HIES 导入）|
| 执行层 | FormPlugin（前端交互层）| OP（后端事务层）|
| 目的 | 实时填充 orgpattern | 修正批量导入的不规范数据 |
| 触发条件 | propertyChanged 事件 | importtype 变量非空 |

---

## 规则优先级与关系

```
R-1（issyspreset 保护）← 最优先 · 预置数据全面禁改
    ↓ 非预置数据
R-2（adminorgtypestd 引用检查）← beforeBindData 阶段 · 字段级禁用
    ↓ 未被引用 · 用户可正常编辑
R-3（adminorgtypestd → orgpattern 联动）← 用户操作时实时联动
    ↓ 用户可手工覆盖 orgpattern
R-4（HIES 导入修正）← 仅在 importtype 变量存在时执行 · 修正批量数据
```

四条规则分别针对不同触发条件，不会相互干扰。

---

## 与三胞胎差异总结

| 规则 | 本场景 | haos_adminorgfunction | haos_adminorglayer | haos_orgchangereason |
|---|---|---|---|---|
| 系统预置保护 | R-1（listRules · 已禁用 · 字段保护有效）| 有（listRules 启用）| 有 | 无 |
| 引用检查字段禁用 | **R-2（baseDataCheckReference + setEnable）** | 无 | 无 | 无 |
| 字段联动填值 | **R-3（adminorgtypestd → orgpattern）** | 无 | 无 | 无 |
| HIES 导入修正 | **R-4（AdminOrgTypeSaveOp）** | 无 | 无 | 无 |

**结论**：本场景业务规则比三胞胎多 3 条（R-2/R-3/R-4），复杂度显著更高，原因在于 adminorgtypestd → orgpattern 的枚举联动机制是 haos 域独有设计。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin -->

## chgaction 实证补充（AdminorgtypeEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.AdminorgtypeEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin -->

## chgaction 实证补充（AdminorgtypeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.database.AdminorgtypeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

## chgaction 实证补充（HRBaseDataCommonMobList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBaseDataCommonMobList -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.BaseDataBuOp -->

## chgaction 实证补充（BaseDataBuOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.BaseDataBuOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.BaseDataBuOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.BaseDataBuOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp -->

## chgaction 实证补充（AdminOrgTypeSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.AdminOrgTypeSaveOp -->
