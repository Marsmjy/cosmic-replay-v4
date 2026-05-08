# 业务规则 · 岗位基础资料（hbpm_basedatalist）

> **状态**: 🟢 反编译实证 · listRules=0 · 隐性规则从代码提炼
> **confidence**: verified（源于 PositionBasedataEdit.java + HRBDGroupList.java 反编译）
> **最后更新**: 2026-04-27

---

## 一、业务规则总体说明

`hbpm_basedatalist` **没有任何显式 listRules**（`rules.json` 为空，0 条）。

这是 hbpm 岗位基础资料模块的公共特征：岗位域的字典类基础资料（岗位级别、岗位序列、岗位分类等）均不走审批单流程，修改权限由插件层的 `issyspreset` 判断直接控制，而非通过 listRules 声明校验链。

以下规则均为从反编译代码中提炼的**隐性业务规则（Invariant）**，在开发扩展时必须遵守。

---

## 二、隐性业务规则（INV）

### INV-BD-01 · 系统预置数据强制只读（PositionBasedataEdit 实证）

**来源**: `PositionBasedataEdit.afterBindData`（反编译实证 · kd.hrmp.hbpm.formplugin.web.basedata）

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
- 当基础资料的 `issyspreset` 字段为 `true` 时，页面被切换为 VIEW（只读）状态
- 此规则在 afterBindData（数据加载完成后）执行，对用户透明无感知
- 被系统预置的岗位基础资料（出厂内置数据）无法在 UI 层编辑任何字段
- **ISV 注意**：即使 ISV 加了扩展字段，如果当前行是预置数据（issyspreset=true），扩展字段在 UI 层同样不可编辑。如需允许预置数据行编辑 ISV 扩展字段，须在 ISV 自建 FormPlugin 中判断字段归属后解锁（但需谨慎，避免影响标品只读语义）。

**相关字段**：
- `issyspreset`（CheckBoxField · L1 · isvCanModify=false · 系统自动写入 · 物理列 `fissyspreset`）

---

### INV-BD-02 · 分组行单击禁止默认行为 · 仅响应超链接/双击跳转

**来源**: `HRBDGroupList.listRowClick + billListHyperLinkClick + listRowDoubleClick`（反编译实证）

```java
public void listRowClick(ListRowClickEvent evt) {
    evt.setCancel(true);  // 取消默认单击行为（不弹编辑弹窗）
}

public void billListHyperLinkClick(HyperLinkClickArgs args) {
    args.setCancel(true);
    IListView listView = (IListView)this.getView();
    this.showBaseDataList(listView);  // 跳转到具体子页面
}

public void listRowDoubleClick(ListRowClickEvent evt) {
    evt.setCancel(true);
    IListView listView = (IListView)this.getView();
    this.showBaseDataList(listView);  // 双击同效果
}
```

**规则说明**：
- 列表行单击（listRowClick）被取消，避免触发默认表单打开逻辑
- 只有点击超链接（billListHyperLinkClick）或双击行（listRowDoubleClick）才触发子页面跳转
- 跳转目标通过查询 `pagekey` 字段动态确定（从数据库根据行 id 查 `fpagekey` 列）
- 如果 `pagekey` 无效或查询异常，会通过 `showTipNotification` 提示"请检查页面标识是否正确"

---

### INV-BD-03 · pagekey 字段驱动子页面跳转

**来源**: `HRBDGroupList.getPageKeyById + showBaseDataList`（反编译实证）

```java
private String getPageKeyById(Long baseDataId, String pageKey) {
    HRBaseServiceHelper baseDataListHelper = new HRBaseServiceHelper(pageKey);
    QFilter idFilter = new QFilter("id", "=", (Object)baseDataId);
    QFilter[] idFilterArray = new QFilter[]{idFilter};
    DynamicObject baseDataDy = baseDataListHelper.queryOne("pagekey", idFilterArray);
    return baseDataDy.getString("pagekey");
}
```

**规则说明**：
- 每个基础资料分类行都有一个 `pagekey` 字段（`fpagekey` 物理列），存储对应列表页的 formId
- 点击跳转时，系统先查当前行的 `pagekey`，再用该 key 打开对应的列表
- `pagekey` 是 L3 层字段（由 hbpm_bd_tpl_dlg 岗位模板对话框层引入），isvCanModify=false（不可修改）
- 如果业务需要新增一个基础资料分类，必须先有对应的 form（有 formId/pagekey），否则跳转异常

---

### INV-BD-04 · 出厂数据双层防护（UI + OP 两道关）

**来源**: OP 链 `HRBaseOriginalOp`（L1 标品）+ `PositionBasedataEdit`（hbpm 专属）

- **UI 层（前置）**：afterBindData 检测 issyspreset → 切换到 VIEW 状态（INV-BD-01）
- **OP 层（后置）**：HRBaseOriginalOp 的 beforeExecuteOperationTransaction 阻止直接修改出厂数据（即使绕过 UI 也被 OP 拦截）

这是双层保护机制，ISV 扩展时不需要额外处理出厂数据保护逻辑，平台和标品已覆盖。

---

## 三、与显式 listRules 的对比

| 维度 | 有 listRules 场景（如 haos_adminorgtype） | 本场景（hbpm_basedatalist） |
|---|---|---|
| 规则声明方式 | 显式 JSON 声明，可枚举 | 无声明，隐性在插件代码中 |
| 规则触发时机 | 平台规则引擎调度 | 插件生命周期方法（afterBindData） |
| 可配置性 | 高（可在开发平台配置） | 低（需改代码） |
| ISV 扩展性 | 可追加新规则 | 可并列挂插件追加逻辑 |

---

## 四、ISV 扩展时的规则兼容要求

1. **不要破坏 issyspreset 保护**：ISV 并列挂的 FormPlugin 若要修改字段可见性/必填性，必须先判断 `issyspreset`，对预置数据保持 VIEW 状态不干扰
2. **不要干扰 HRBDGroupList 的分组跳转逻辑**：ISV 若需要在列表页追加行为，需在另一个 ListPlugin 中实现，不能继承 `HRBDGroupList`（PR-001）
3. **Validator 注册时机**：本场景无 CtrlStrategyValidator（与 hbpm_reportcoreltype 不同），如需 ISV 校验，走 `onAddValidators` 挂 `HRDataBaseOp` 子类（PR-001 · 不继承 AbsOrgBaseOp）
4. **无组织控制字段**：本场景不含 createorg/org/useorg/ctrlstrategy 等 L3 组织管控字段（与 hbpm_positiontpltype 区别），无需处理组织可见性联动

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBDGroupList -->

## chgaction 实证补充（HRBDGroupList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRBDGroupList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRBDGroupList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRBDGroupList -->

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
