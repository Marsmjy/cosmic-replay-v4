# 业务规则 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证
> **confidence**: verified
> **规则数**: 6 条（R-1 BU 模板库开关校验 · R-2 禁用数据不可设适用范围 · R-3 modify 前模板库开关校验 · R-4 名称/排序号唯一校验 · R-5 级联同步岗位 · R-6 BEC 变更消息）

---

## R-1 · 新建前校验 BU 已开启模板库（openpositiontpl=true）

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin` |
| **生命周期方法** | `beforeDoOperation`（operateKey=new）|
| **实证来源** | PositionTplListPlugin.java 反编译 |
| **触发时机** | 列表点击"新建"操作前 |

### 业务语义

新建岗位模板前，检查当前用户权限下是否有 BU（管理组织）已开启岗位模板库（`openpositiontpl=true`）。若所有 BU 均未开启 → 提示"所在权限下组织体系管理组织均未启用模板库，无法新增。" → 取消操作。

新建实际走 `showStructProjectForm()`，打开 `hbpm_positiontpl` 表单为 `ShowType.MainNewTabPage`（主页新标签页），并通过 `customParam("orgId")` 传递当前列表筛选的 org ID。

### 反编译实证代码

```java
case "new": {
    String validateResult = this.validateOrgExistOpenTpl();
    if (!validateResult.isEmpty()) {
        this.getView().showTipNotification(validateResult);
        args.setCancel(true);
        return;
    }
    this.showStructProjectForm();
    args.setCancel(true);
}
```

---

## R-2 · 禁用状态的岗位模板不可设置适用组织范围

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin` |
| **生命周期方法** | `beforeItemClick`（itemKey=applicationscope）|
| **实证来源** | PositionTplListPlugin.java 反编译 |

### 业务语义

点击"设置适用范围"（applicationscope）操作时，通过 `PositionTplRepository.queryDisablePositionTplByIds(primaryKeyValues)` 检查选中数据中是否有禁用记录。若有 → 提示"禁用数据不可设置适用组织范围。" → 取消操作并记录操作日志（`addOperateLog()`）。

---

## R-3 · modify 前校验 BU 已开启模板库 + 模板变更岗位提示

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin` |
| **生命周期方法** | `beforeDoOperation`（operateKey=modify）|
| **实证来源** | PositionTplEditPlugin.java 反编译 |

### 业务语义

编辑视图点击"变更"前：
1. 获取当前 org 对应 BU 的系统参数（`SystemParamHelper.getBatchParameter()`）
2. 若 `openpositiontpl=false` → 提示"组织体系管理组织{orgName}未启用模板库，不可变更数据。" → 取消
3. 若 `openpositiontpl=true` 且 `positiontplchangepos=true` → 调 `IHRCSService.getContent(TIPS_ID=2000164510891009024L)` 获取公告内容 → `showTipNotification(tips, 10000 ms)`

**save 操作前置校验**：若 `operateKey=save` 且 `!getModel().getDataChanged()` → 提示"无信息变更，请确认。" → 取消。

---

## R-4 · 名称唯一 + 排序号唯一校验（afterDoOperation 错误反馈）

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin` |
| **生命周期方法** | `afterDoOperation`（保存失败时）|
| **实证来源** | PositionTplEditPlugin.java 反编译 |

### 业务语义

保存操作失败时（`operationResult.isSuccess()=false`），检查 OP 层通过 `OperateOption.setVariableValue()` 写入的错误变量：
- `nameError` 变量存在 → `PositionTplUtil.isNameEnableRe(view, "name", "已存在名称相同且可用的岗位类型。")` → name 字段下方显示错误提示
- `indexError` 变量存在 → `PositionTplUtil.isNameEnableRe(view, "index", "数据已存在")` → index 字段下方显示错误提示

---

## R-5 · 保存后级联同步下游岗位数据

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp` |
| **生命周期方法** | `beforeExecuteOperationTransaction` + `endOperationTransaction` |
| **实证来源** | PositionTplSaveOp.java 反编译 |

### 业务语义

**beforeExecuteOperationTransaction（保存前快照）**：
- 过滤出已有记录（`id != 0L`），通过 `HRBaseServiceHelper.loadDynamicObjectArray()` 加载历史快照
- 构建 `oldDynMap`（id → 历史 DynamicObject）供 endOpTx 差量对比

**endOperationTransaction（事务提交后同步）**：
```java
PositionTplChangeSyncPosService syncUpdatePosService =
    new PositionTplChangeSyncPosService(this.oldDynMap);
syncUpdatePosService.syncUpdatePosition(e.getDataEntities());
```
根据保存前后字段差异，级联更新下游岗位（`hbpm_positionhr`）数据。

---

## R-6 · 保存后发送 BEC 变更消息 + 通用岗位同步

### 基本信息

| 属性 | 值 |
|---|---|
| **实现类** | `kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp` |
| **生命周期方法** | `afterExecuteOperationTransaction` |
| **实证来源** | PositionTplSaveOp.java 反编译 |

### 业务语义

事务完成后（afterExecuteOperationTransaction）：
```java
new ChangeMsgServiceImpl().sendMsg();         // 发 BEC 变更消息（封装实现）
IBosPositionService.getInstance().commonSyncPositions();  // 通用岗位同步
```

**注意**：`ChangeMsgServiceImpl.sendMsg()` 是封装实现，内部可能走消息队列异步发送。ISV 如需感知岗位模板变更，应订阅对应 BEC 事件，不要继承 `PositionTplSaveOp`（PR-001 禁继承）。

---

## modifystrategy 字段可见性规则（内嵌于 PositionTplEditPlugin）

`setModifyStrategy()` 方法依据 BU 参数动态控制字段可见性：

| 条件 | modifystrategy 可见 | fieldrange 可见 |
|---|---|---|
| org 为空 | 隐藏 | 隐藏 |
| BU 参数不存在/为空 | 隐藏 | 隐藏 |
| `openpositiontpl=false` | 隐藏 | 隐藏 |
| `openpositiontpl=true && positiontplismodify=true` | 显示 | 显示 |
| `openpositiontpl=true && positiontplismodify=false` | 显示 | 隐藏 |

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin -->

## chgaction 实证补充（PositionTplEditPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin -->

## chgaction 实证补充（PositionTplListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin -->

## chgaction 实证补充（PositionTplBuListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionTplBuListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp -->

## chgaction 实证补充（PositionTplSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `changeMsgServiceImpl.sendMsg`
- `BosPositionServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionTplSaveOp -->
