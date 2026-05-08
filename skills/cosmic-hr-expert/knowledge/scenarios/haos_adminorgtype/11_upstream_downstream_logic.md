# 上下游联动 · 行政组织类型（haos_adminorgtype）

> **状态**: 🟢 基于 refentity_reverse.json 实证 + 反编译 3 类分析 + grep BEC 0 命中
> **confidence**: verified
> **上游**: haos_adminorgtypestd（类型标准字典）
> **下游**: haos_adminorg（行政组织 · 单选 BasedataField 引用）

---

## 一、上游链路

### 1.1 haos_adminorgtypestd → haos_adminorgtype（类型标准字典）

`haos_adminorgtype.adminorgtypestd` 字段（BasedataField）引用 `haos_adminorgtypestd` 实体（refentity_reverse 实证）：

```json
"haos_adminorgtypestd": [
  {
    "form": "haos_adminorgtype",
    "field": "adminorgtypestd",
    "type": "BasedataField",
    "cnName": "类型归属"
  }
]
```

| 属性 | 值 |
|---|---|
| 上游实体 | `haos_adminorgtypestd`（行政组织类型标准）|
| 引用字段 | `adminorgtypestd`（本场景字段）|
| 引用类型 | BasedataField（单选）|
| 物理外键列 | `fadminorgtypestdid` |
| 业务意义 | 每条 adminorgtype 归属一个 adminorgtypestd 类型标准 |

**关键依赖**：`AdminOrgTypeStdEnum` 枚举维护 adminorgtypestd id → orgpattern id 的映射，这个枚举与 haos_adminorgtypestd 实体的数据高度耦合。haos_adminorgtypestd 数据变更可能导致枚举映射失效。

### 1.2 bos_org_pattern → haos_adminorgtype（组织形态字典）

`haos_adminorgtype.orgpattern` 字段（BasedataField）引用 `bos_org_pattern` 实体：

| 属性 | 值 |
|---|---|
| 上游实体 | `bos_org_pattern`（平台组织形态字典）|
| 引用字段 | `orgpattern`（本场景字段）|
| 引用类型 | BasedataField（单选）|
| 物理外键列 | `forgpatternid` |
| 业务意义 | 每条 adminorgtype 指定对应的组织形态 |

**注意**：`bos_org_pattern` 是苍穹平台基础资料（bos_* 前缀），ISV 禁止修改其元数据。

---

## 二、下游链路

### 2.1 haos_adminorgtype → haos_adminorg.adminorgtype（refentity_reverse 实证）

```json
"haos_adminorgtype": [
  {
    "form": "haos_adminorg",
    "field": "adminorgtype",
    "type": "BasedataField",
    "cnName": "行政组织类型"
  }
]
```

| 属性 | 值 |
|---|---|
| 下游实体 | `haos_adminorg`（行政组织）|
| 引用字段 | `adminorgtype`（haos_adminorg 上的字段）|
| 引用类型 | BasedataField（单选）|
| 物理外键列（推断）| `fadminorgtypeid` |
| 业务意义 | 每条行政组织数据有一个行政组织类型 |

**重要**：`haos_adminorg` 是 HisModel（时序资料），不是普通 BaseFormModel。查询时必须加 `iscurrentversion=true` 过滤（PR-008 / PR-009）。

### 2.2 下游反向引用查询（CS-02 使用）

```java
// 查询哪些 haos_adminorg 当前引用了某条 adminorgtype 记录
// ⭐ admin_org 是 HisModel 时序 · 必须加 iscurrentversion=true
QFilter qf = new QFilter("adminorgtype", "=", adminorgtypeId)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE))
    .and(new QFilter("enable", "=", "1"));
boolean isReferenced = new HRBaseServiceHelper("haos_adminorg").isExists(qf);
```

**与三胞胎查询路径对比**：

| 场景 | 下游实体 | 字段类型 | 查询路径 |
|---|---|---|---|
| haos_adminorgtype（本场景）| haos_adminorg | 单选 BasedataField | `QFilter("adminorgtype", "=", id)` 直字段 + iscurrentversion |
| haos_adminorgfunction | haos_adminorg | 单选 BasedataField | `QFilter("adminorgfunction", "=", id)` 直字段 + iscurrentversion |
| haos_orgchangereason | haos_changescene | 多选 MulBasedataField | `QFilter("changereason.fbasedataid", "=", id)` 子表 join |

---

## 三、BEC 模式（标品无 BEC）

**grep 实证**（标品 haos_adminorgtype 场景 0 命中）：
```
knowledge/_sdk_audit/_decompiled/scenarios/haos_adminorgtype/ 下
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" → 0 处命中
```

**结论**：
- ✅ 标品**不在本场景发任何 BEC 事件**
- ✅ 标品**不在本场景订阅 BEC**
- ✅ 与三胞胎 haos_adminorgfunction / haos_orgchangereason 完全一致（都是 grep 0）

**ISV BEC 反指引**：
- ❌ ISV 不应在本场景订阅 BEC（标品没发，收不到）
- ⚠ ISV 也不推荐在本场景做 BEC 发布方（基础资料变更频率低，见 06 CS-05 反指引）
- 若下游需要通知，应挂在 `haos_adminorg` 业务链（admin_org 有异步派单链）

---

## 四、上下游联动全景图

```
haos_adminorgtypestd（类型标准 · 上游字典）
        ↓ 单选引用（adminorgtypestd 字段）
haos_adminorgtype（本场景 · 行政组织类型字典）
  ├─ adminorgtypestd → orgpattern 联动（propertyChanged · R-3）
  ├─ HIES 导入修正（AdminOrgTypeSaveOp · R-4）
  └─ 字段引用保护（beforeBindData · R-2）
        ↓ 单选引用（adminorgtype 字段）
haos_adminorg（行政组织 · 下游 HisModel 时序）
        ↓ 被多个场景引用（组织变动等业务链）
组织变动业务（homs_orgbatchchgbill 等）
```

---

## 五、关联场景

| 关联场景 | 关联方式 | 业务意义 |
|---|---|---|
| `haos_adminorgtypestd` | 上游字典 · adminorgtypestd 字段引用 | 类型标准定义 |
| `haos_adminorg` | 下游实体 · 单选引用 adminorgtype 字段 | 行政组织使用本字典 |
| `bos_org_pattern` | 上游字典 · orgpattern 字段引用 | 组织形态定义 |
| `haos_adminorgfunction` | 同域字典 · 不直接关联 | 共享 BaseDataBuOp 插件 |
| `haos_adminorglayer` | 同域字典 · 不直接关联 | 同模板基因 |

---

## 六、操作与下游影响速查

| 操作 | 是否影响下游 | 影响范围 |
|---|---|---|
| 新增 adminorgtype | ❌ 不影响 | 不影响已有 haos_adminorg 数据 |
| 修改 name/description | ❌ 轻微 | 下游查询时显示名变化（但 id 外键不变）|
| 修改 adminorgtypestd | **⚠ 高** | orgpattern 联动变 + 报表语义变化 · 被引用时 UI 禁止 |
| 禁用 adminorgtype | **⚠ 高** | haos_adminorg 引用了此类型的数据可能受影响（enable=0）|
| 删除 adminorgtype | **⚠ 高** | haos_adminorg.adminorgtype 外键游离（CS-02 拦截）|
| HIES 导入 | **⚠ 修正** | AdminOrgTypeSaveOp 修正 orgpattern（覆盖导入值）|

---

## 七、与三胞胎上下游结构对比

| 维度 | haos_adminorgtype（本场景）| haos_adminorgfunction | haos_adminorglayer | haos_orgchangereason |
|---|---|---|---|---|
| 上游字典 | haos_adminorgtypestd + bos_org_pattern | 无特殊上游 | 无特殊上游 | 无特殊上游 |
| 下游实体 | haos_adminorg（HisModel）| haos_adminorg（HisModel）| haos_adminorg（HisModel）| haos_changescene |
| 下游引用类型 | 单选 BasedataField | 单选 BasedataField | 单选 BasedataField | 多选 MulBasedataField |
| BEC | 无（grep 0）| 无（grep 0）| 无（grep 0）| 无（grep 0）|
| 上下游联动复杂度 | **高**（有枚举联动 + 引用保护）| 低 | 低 | 低 |

---

## 八、维度说明：boid / id 区分（PR-009）

**本场景** `haos_adminorgtype` 是 **BaseFormModel**（非时序资料），物理表无 `fboid` 列：
- `id` 是行政组织类型记录的唯一标识（业务维度 = 版本维度合一）
- 不存在 `boid`（业务对象 id）与 `id`（版本 id）的区分（PR-009 适用于 HisModel 场景）

**下游 `haos_adminorg`（HisModel）查询时**，必须理解 boid 与 id 的区分（PR-009）：
- `haos_adminorg.boid` = 行政组织的业务唯一标识（跨版本不变）
- `haos_adminorg.id` = 当前版本记录的 id（随生效日期变化）
- ISV 查"哪些行政组织引用了某 adminorgtype"时，务必加 `iscurrentversion=true` 过滤，避免重复计算历史版本

```java
// PR-009 正确写法：HisModel 下游必须区分 boid 和 id
QFilter filter = new QFilter("adminorgtype", "=", typeId)
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));
// 返回的是当前版本记录，每个 boid 对应一条
```

---

## 九、BEC 事件中心（PR-011）

**本场景标品无 BEC**（`IEventServicePlugin` 未注册，`triggerEventSubscribeJobs` grep 0 命中）。

- 本场景不作为 BEC 订阅方（无 `IEventServicePlugin` 实现）
- 本场景不作为 BEC 发布方（无 `triggerEventSubscribeJobs` 调用）
- 若 ISV 需要在行政组织类型保存后通知其他模块，应自行注册 BEC 订阅方（`IEventServicePlugin.handleEvent`）并列挂 OP 插件

```java
// ISV 如需 BEC 订阅（非标品功能，ISV 自建）
public class AdminOrgtypeSavedEventHandler implements IEventServicePlugin {
    @Override
    public void handleEvent(String eventNumber, Map<String, Object> eventData) {
        // 处理行政组织类型相关业务事件
    }
}
```

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

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
