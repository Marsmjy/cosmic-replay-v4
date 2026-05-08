# 业务规则 · 协作类型（hbpm_reportcoreltype）

> **状态**: 🟢 反编译实证 · listRules=0 · 隐性规则从代码提炼
> **confidence**: verified（源于 PositionBasedataEdit.java + PositionnBaseDataOrderPlugin.java + BaseDataBuOp.java 反编译）
> **最后更新**: 2026-04-27

---

## 一、业务规则总体说明

`hbpm_reportcoreltype` **没有任何显式 listRules**（`rules.json` 为空，0 条）。

隐性业务规则分布在 3 个专属类中，层级清晰：
- `PositionBasedataEdit`（FormPlugin · 与 hbpm_basedatalist 共用）：issyspreset 只读保护
- `PositionnBaseDataOrderPlugin`（ListPlugin · 本场景专属）：列表双列排序
- `BaseDataBuOp`（OP 插件 · **hbpm 域版本**，区别于 haos 域同名类）：控制策略合规校验

---

## 二、隐性业务规则（INV）

### INV-RC-01 · 系统预置数据强制只读（PositionBasedataEdit 实证）

**来源**: `PositionBasedataEdit.afterBindData`（`kd.hrmp.hbpm.formplugin.web.basedata` 包，与 hbpm_basedatalist 共用同一类）

```java
public void afterBindData(EventObject e) {
    DynamicObject basedataEntity = this.getView().getModel().getDataEntity();
    boolean issyspreset = basedataEntity.getBoolean("issyspreset");
    if (issyspreset) {
        ((IBillView)this.getView()).setBillStatus(BillOperationStatus.VIEW);
    }
}
```

**规则说明**：
- `issyspreset=true` 时整页切换为 VIEW 只读状态（与 hbpm_basedatalist 完全相同的实现）
- 本场景是 hbpm_basedatalist 和 hbpm_reportcoreltype 的**共用类**，两个场景使用同一个 `PositionBasedataEdit.java`
- **ISV 注意**：任何 afterBindData 联动逻辑都必须先判断 issyspreset，预置数据跳过

---

### INV-RC-02 · 列表双列排序（createorg.id + index 升序）

**来源**: `PositionnBaseDataOrderPlugin.setFilter`（反编译实证）

```java
public void setFilter(SetFilterEvent setFilterEvent) {
    super.setFilter(setFilterEvent);
    setFilterEvent.setOrderBy("createorg.id asc,index asc");
}
```

**规则说明**：
- 列表数据先按**创建组织 id 升序**，再按**排序号 index 升序**
- 第一排序维度 `createorg.id`：按创建组织分组排列（同一组织的协作类型聚集）
- 第二排序维度 `index`：组织内按排序号升序展示
- 这是本场景独有的排序规则（hbpm_basedatalist 和 hbpm_positiontpltype 均无此双列排序）
- **ISV 注意**：如需追加排序条件，只能在另一个 ListPlugin 中追加（禁继承 `PositionnBaseDataOrderPlugin` · PR-001）

---

### INV-RC-03 · 控制策略合规校验（hbpm 域 BaseDataBuOp）

**来源**: `BaseDataBuOp.onAddValidators`（`kd.hrmp.hbpm.opplugin.web.basedata` 包，**hbpm 域版本**）

```java
public void onAddValidators(AddValidatorsEventArgs args) {
    super.onAddValidators(args);
    args.addValidator((AbstractValidator)new CtrlStrategyValidator());
}
```

**规则说明**：
- 注册 `CtrlStrategyValidator`（hbpm 域内部 Validator）检查控制策略配置合规
- 与 haos 域 `BaseDataBuOp`（`kd.hr.haos.opplugin.web.BaseDataBuOp`）功能相似，但是**两个不同的类**
- **域区分关键**：
  - haos 域：`kd.hr.haos.opplugin.web.BaseDataBuOp`（用于 haos_adminorgtype 等行政组织场景）
  - hbpm 域：`kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp`（用于本场景）
- ISV 不可继承任何一个版本（PR-001）

---

### INV-RC-04 · orgteamtype 多选字段（L4 特有）

**来源**: `scene_doc.json` 字段表（L4 层引入 · orgteamtype）

```json
{
  "name": "(main).orgteamtype",
  "type": "MulBasedataField",
  "layer": "L3",
  "required": true,
  "isvCanModify": false,
  "physicalColumn": "t_hbpm_orgteamtype（子表）"
}
```

**规则说明**：
- `orgteamtype`（所属组织分类）是**多选基础资料字段**（MulBasedataField），物理上存在子表 `t_hbpm_orgteamtype`
- `required=true`：必填字段
- `isvCanModify=false`：ISV 不可修改此字段的元数据（但可以在 OP/FormPlugin 层读取其值）
- 这是本场景与 hbpm_basedatalist 和 hbpm_positiontpltype 的关键区别：本场景有一个强制必填的多选子表字段

---

## 三、hbpm 域 vs haos 域 BaseDataBuOp 对比

| 维度 | haos 域 BaseDataBuOp | hbpm 域 BaseDataBuOp（本场景）|
|---|---|---|
| 包路径 | `kd.hr.haos.opplugin.web.BaseDataBuOp` | `kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp` |
| Jar | `hrmp-haos-opplugin-1.0.jar` | `hrmp-hbpm-opplugin-1.0.jar` |
| 使用场景 | haos_adminorgtype 等行政组织基础资料 | hbpm_reportcoreltype 协作类型 |
| 注册的 Validator | haos 域 CtrlStrategyValidator | hbpm 域 CtrlStrategyValidator |
| 可继承 | ❌ 禁继承（PR-001）| ❌ 禁继承（PR-001）|

**ISV 注意**：两个同名类都禁继承，使用时不要因为包路径不同而忽略 PR-001 铁律。

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.basedata.PositionBasedataEdit -->

## chgaction 实证补充（PositionBasedataEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.basedata.PositionBasedataEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.basedata.PositionBasedataEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.basedata.PositionBasedataEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.basedata.PositionnBaseDataOrderPlugin -->

## chgaction 实证补充（PositionnBaseDataOrderPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.basedata.PositionnBaseDataOrderPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.basedata.PositionnBaseDataOrderPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.basedata.PositionnBaseDataOrderPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp -->

## chgaction 实证补充（BaseDataBuOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.basedata.BaseDataBuOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->
