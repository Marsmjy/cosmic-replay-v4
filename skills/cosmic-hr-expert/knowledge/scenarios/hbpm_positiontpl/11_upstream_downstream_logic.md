# 上下游联动 · 岗位模板（hbpm_positiontpl）

> **状态**: 🟢 基于反编译 4 类实证 + PositionTplSaveOp 级联同步实证
> **confidence**: verified
> **上游**: hbpm_positiontpltype（岗位模板类型）
> **下游**: hbpm_positionhr（岗位信息）· bos_position（平台岗位）· hbpm_applicationscope（适用范围）

---

## 一、上游链路

### 1.1 hbpm_positiontpltype → hbpm_positiontpl（岗位模板类型）

`PositionTplEditPlugin.registerListener` 实证：
```java
BasedataEdit positiontpl = (BasedataEdit)this.getView().getControl("posttpltype");
positiontpl.addBeforeF7SelectListener((BeforeF7SelectListener)this);
```

| 属性 | 值 |
|---|---|
| 上游实体 | `hbpm_positiontpltype`（岗位模板类型）|
| 引用字段 | `posttpltype`（本场景字段）|
| 引用类型 | BasedataField（单选）|
| 业务意义 | 每个岗位模板归属一个岗位模板类型 |

**F7 过滤**：通过 `JobLevelGradeRangeUtil.getInstance().beforeF7Select()` 处理岗位层级范围关联过滤，确保选择的模板类型符合层级范围约束。

---

## 二、下游链路

### 2.1 hbpm_positiontpl → hbpm_positionhr（级联同步岗位信息）

`PositionTplSaveOp.endOperationTransaction` 实证：
```java
PositionTplChangeSyncPosService syncUpdatePosService =
    new PositionTplChangeSyncPosService(this.oldDynMap);
syncUpdatePosService.syncUpdatePosition(e.getDataEntities());
```

| 属性 | 值 |
|---|---|
| 下游实体 | `hbpm_positionhr`（岗位信息维护）|
| 同步方式 | `PositionTplChangeSyncPosService` 差量同步 |
| 触发时机 | endOperationTransaction（主事务中）|
| 同步内容 | 根据 `oldDynMap`（保存前快照）与新数据的差异字段同步 |

**重要**：`endOperationTransaction` 在**主事务**中执行，若同步失败会导致整个保存回滚，数据不会持久化。

### 2.2 hbpm_positiontpl → bos_position（平台岗位同步）

`PositionTplSaveOp.afterExecuteOperationTransaction` 实证：
```java
IBosPositionService.getInstance().commonSyncPositions();
```

| 属性 | 值 |
|---|---|
| 下游实体 | `bos_position`（苍穹平台岗位数据）|
| 同步方式 | `IBosPositionService.commonSyncPositions()` 通用同步 |
| 触发时机 | afterExecuteOperationTransaction（主事务已提交）|

### 2.3 BEC 消息（变更通知下游系统）

`PositionTplSaveOp.afterExecuteOperationTransaction` 实证：
```java
ChangeMsgServiceImpl changeMsgServiceImpl = new ChangeMsgServiceImpl();
changeMsgServiceImpl.sendMsg();
```

| 属性 | 值 |
|---|---|
| 通知方式 | `ChangeMsgServiceImpl.sendMsg()`（封装实现）|
| 触发时机 | afterExecuteOperationTransaction（主事务已提交）|
| 影响 | 下游 BEC 订阅方感知岗位模板变更 |

**注意**：`sendMsg` 是封装的消息发送，内部可能走消息队列异步发送（参考 feedback_bec_3layer_async_publish.md）。若 sendMsg 失败，主事务已提交不会回滚，但下游可能数据不一致。

### 2.4 hbpm_applicationscope（适用范围关联）

通过列表的"设置适用范围"操作（donothing_setscope → 打开 hbpm_applicationscope 弹窗）进行关联写入，与保存操作解耦。

---

## 三、boid / id 区分（PR-009）

**本场景** `hbpm_positiontpl` 是 **BaseFormModel**（非时序），物理表无 `fboid` 列：
- `id` 是岗位模板记录的唯一标识
- 不存在 `boid` 与 `id` 的区分
- ISV 查询本场景：直接 `QFilter("id", "=", id)` 无需 `iscurrentversion` 过滤

**`PositionTplSaveOp.beforeExecuteOperationTransaction` 实证**：
```java
// BaseFormModel 查询模式：id != 0L 直接区分新旧
List oldIdList = Arrays.stream(dataEntities)
    .filter(d -> d.getLong("id") != 0L)  // 非 boid · 直接按 id
    .map(d -> d.getLong("id"))
    .collect(Collectors.toList());
```

如果下游 `hbpm_positionhr` 是 HisModel，ISV 查询时需加 `iscurrentversion=true`（PR-009）。

---

## 四、BEC 模式（本场景有 BEC）

**与 haos 域字典类场景的重要区别**：

| 维度 | 本场景（hbpm_positiontpl）| haos_adminorgtype |
|---|---|---|
| 是否发 BEC | **是**（ChangeMsgServiceImpl.sendMsg）| 否（grep 0）|
| BEC 实现 | ChangeMsgServiceImpl 封装 | — |
| 触发时机 | afterExecuteOperationTransaction | — |
| ISV 订阅 | 可订阅（实现 IEventServicePlugin）| 无效（没发）|

**ISV BEC 指引**：
- ✅ ISV 可通过实现 `IEventServicePlugin.handleEvent()` 订阅岗位模板变更 BEC 事件
- ❌ 不要继承 `PositionTplSaveOp` 来感知变更（PR-001）
- ⚠ 需确认 `ChangeMsgServiceImpl.sendMsg()` 具体的 BEC 事件 key（需进一步反编译 ChangeMsgServiceImpl）

---

## 五、上下游联动全景图

```
hbpm_positiontpltype（岗位模板类型 · 上游字典）
        ↓ posttpltype 字段（BasedataField 单选引用）
hbpm_positiontpl（本场景 · 岗位模板）
  ├── endOperationTransaction ───────→ hbpm_positionhr（岗位信息 · 差量同步）
  ├── afterExecuteOperationTransaction → bos_position（平台岗位 · 通用同步）
  ├── afterExecuteOperationTransaction → BEC 消息（下游系统感知）
  └── 适用范围操作 ─────────────────→ hbpm_applicationscope（适用组织范围）
```

---

## 六、操作与下游影响速查

| 操作 | 影响下游 | 影响范围 |
|---|---|---|
| 新建 positiontpl | ✅ 中 | 创建岗位模板记录 · 无级联同步（新建无历史快照）|
| 修改（modify+save）| ✅ 高 | 级联同步 hbpm_positionhr + bos_position + BEC 消息 |
| 禁用 | ❌ 低 | 仅本记录状态变更 |
| 设置适用范围 | ✅ 中 | 写入 hbpm_applicationscope |
| 删除 | ✅ 高 | 引用本模板的 hbpm_positionhr 可能受影响 |

---

## 七、API 规范补充（PR-003 / PR-005 / BEC）

### PR-003 · OP 层用 entity.set · FormPlugin 层用 setValue

```java
// ✅ OP 层（PositionTplSaveOp.beforeExecuteOperationTransaction）用 entity.set
// PR-003 OP 层规范
for (DynamicObject entity : e.getDataEntities()) {
    entity.set("index", newIndex);          // OP 层 · 直接 set
    entity.set("graderange", gradeRangeObj);
}

// ✅ FormPlugin 层（PositionTplEditPlugin.propertyChanged）用 setValue
// PR-003 FormPlugin 规范
this.getModel().setValue("graderange", gradeRangeObj);  // FormPlugin 用 setValue
// PR-004 配套：beginInit/endInit 防死循环
this.getModel().beginInit();
this.getModel().setValue("graderange", gradeRangeObj);
this.getModel().endInit();
```

### PR-005 · ID 生成用 kd.bos.id.ID

```java
// PR-005 规范：ISV 需要生成新 ID 时用框架工具
import kd.bos.id.ID;
long newId = ID.genLongId();  // 分布式安全 · 不用 UUID
```

### BEC 发布（triggerEventSubscribeJobs · PR-011）

`PositionTplSaveOp.afterExecuteOperationTransaction` 确认发布 BEC 事件（发布方 triggerEventSubscribeJobs），通知下游模块（hbpm_positionhr 等）同步岗位模板变更。

```java
// ISV 如需订阅岗位模板保存事件 · 实现 IEventServicePlugin
public class TdkwPositionTplSavedHandler implements IEventServicePlugin {
    @Override
    public void handleEvent(String eventNumber, Map<String, Object> eventData) {
        // 处理岗位模板保存后的业务逻辑
    }
}
```

**注意**：ISV 不应重复发布同一 BEC 事件（幂等问题）。如标品已发，ISV 只需实现订阅方（IEventServicePlugin.handleEvent）。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 02 沉淀）

> 本场景的业务语义补充见 [PPT02_DEEP_TRACE.md](../../docs/PPT02_DEEP_TRACE.md)
> - 16 实体清单（含历史模型类型/物理表）
> - 7 个标品定时任务（含 haos_func_orgsync_SKDP_S 同步平台）
> - 30+ OpenAPI（行政组织/岗位/职位查询保存等）
> - 5 SDK 扩展点（IAfterEffectAdminOrgExtPlugin / IAdminOrgTreeLabelExtPlugin 等）
> - 综合参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) 总论金字塔

### 关键 SDK Helper（按 org_dev 常用）

```java
HAOSServiceHelper   // 提供新增/变更/启用/禁用组织
HBJMServiceHelper   // 提供新增/变更/启用/禁用职位
HBPMServiceHelper   // 提供新增/变更/启用/禁用岗位
```

### 业务事件订阅点

```
haos.adminOrgChangeEvent           组织变动事件
hbpm.standarpositionChangeEvent    标准岗位变动事件
hbpm.positionChangeEvent           岗位变动事件
hbjm_jobhr.change                  职位变动·生效
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/org_dev_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 2 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
