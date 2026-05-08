# 上下游联动 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于反编译 + scene_doc.json + HisModel 框架
> **confidence**: verified（BEC 部分为 likely · StandardPositionMsgHandleOp 未反编译）
> **审计时间**: 2026-04-27

---

## 1. 整体上下游图

```
┌──────────────────────────────────────────────────────────────┐
│                        上游（进入路径）                          │
└──────────────────────────────────────────────────────────────┘

  ├── 路径 A：菜单直达"岗位维护 → 标准岗位维护"（menuId=1493506395784391680）
  │
  ├── 路径 B：其他场景 F7 选标准岗位
  │     ├── hbpm_positionhr.stposition F7
  │     └── hrpi_* 任职相关场景 F7
  │
  └── 路径 C：数据导入（HIES）
               │
               ▼
        ┌─────────────────────────┐
        │   hbpm_stposition        │ ← 本场景
        │ （HisModel 时序资料）     │
        │   isstandardpos = "1"   │
        └─────────────────────────┘
               │
               ▼
     查询/写入 t_hbpm_position（共用）
               │
               ▼
┌──────────────────────────────────────────────────────────────┐
│                     下游（数据/联动方向）                        │
└──────────────────────────────────────────────────────────────┘

  ├── 物理表 t_hbpm_position（与 hbpm_positionhr 共用）
  │     └── hbpm_positionhr.stposition → 存 hbpm_stposition.boid
  │
  ├── 多语言表 t_hbpm_position_l
  │
  ├── 子表 t_hbpm_standposentry
  │     └── adminorg（行政组织 boid 关联）
  │
  └── BEC 业务事件中心（likely · StandardPositionMsgHandleOp 发布）
        └── 下游订阅方可订阅标准岗位变更/禁用事件
```

---

## 2. 上游：本场景的进入路径

### 2.1 路径 A：菜单直达

| 触发 | 实现 |
|---|---|
| 用户点菜单"岗位维护 → 标准岗位维护" | 平台路由打开 formNumber=hbpm_stposition 列表 |
| StandardPositionListPlugin.setFilter | 强制排序 index asc, number asc |
| HisModelListCommonPlugin 接管列表 | 提供历史版本查看 / 版本高亮等能力 |

### 2.2 路径 B：F7 选值（跨场景引用）

| 调用方场景 | 字段 | 触发 F7 |
|---|---|---|
| `hbpm_positionhr` | stposition（HisModelBasedataField）| F7 打开 hbpm_stposition 选值 |
| hrpi_* 任职场景 | stposition（若有直接引用）| 同上 |

**F7 返回值**：iscurrentversion=true 版本的 **boid**（PR-009），不是 id。

### 2.3 路径 C：HIES 数据导入

标品通过 HRBaseDataImportEdit + 导入向导支持批量导入标准岗位。导入后同样走 save 操作链，StandardPositionSaveOp 保证 isstandardpos="1"。

---

## 3. 下游：本场景影响哪些模块

### 3.1 hbpm_positionhr（岗位信息维护）· 最重要的下游

| 关系 | 说明 |
|---|---|
| 物理表共用 | 共用 t_hbpm_position，isstandardpos 区分 |
| 字段引用 | hbpm_positionhr.stposition = hbpm_stposition.boid（PR-009）|
| 变更影响 | 标准岗位变更时 boid 不变 → hbpm_positionhr 不需级联更新 |
| 禁用影响 | 标准岗位禁用后 → hbpm_positionhr 数据保留（引用的是 boid）|
| 元数据影响 | ISV 给 hbpm_stposition 加字段 → t_hbpm_position 有新列 → hbpm_positionhr 也能看到该列 |

**hbpm_positionhr 引用 stposition 的查询模式**（PR-008 + PR-009）：
```java
// hbpm_positionhr 通过 boid 引用 stposition（PR-009）
// 查关联的当前版本标准岗位
QFilter filter = new QFilter("stposition", "=", stPosBoid)  // stposition 字段存的是 boid
    .and(new QFilter("iscurrentversion", "=", Boolean.TRUE));  // PR-008
DynamicObject stPos = QueryServiceHelper.queryOne("hbpm_stposition", "id,boid,name,job", new QFilter[]{
    new QFilter("boid", "=", stPosBoid),
    new QFilter("iscurrentversion", "=", Boolean.TRUE)
});
```

### 3.2 hrpi 域（员工档案）· 间接下游

员工任职记录（hrpi_empjobrel）可能通过岗位信息（hbpm_positionhr）间接引用标准岗位 boid。标准岗位变更时，boid 不变，hrpi 数据不需要更新。

### 3.3 BEC 业务事件中心

**结论**：标准岗位的 save / change / disable 操作**可能通过 StandardPositionMsgHandleOp 发布 BEC 事件**。

**置信度**：likely（类名强暗示 · 未反编译父类）

**如何确认**：
- 在【开发平台 → 业务事件管理】查 hbpm 域相关事件号
- 或在测试环境执行变更操作，监控 BEC 消息队列

**ISV 订阅建议**：
- 若标品已发 BEC，ISV 直接订阅（走 IEventServicePlugin）
- 若标品未发或事件号不满足，ISV 可在并列挂 OP 的 afterExecuteOperationTransaction 阶段自行发布（PR-010 + PR-011）

---

## 4. 关联时序场景对比表

本场景属于 "HR 时序资料场景簇"：

| 场景 | 物理表 | 时序模型 | 区分键 | 下游主引用方 |
|---|---|---|---|---|
| **hbpm_stposition**（本场景）| t_hbpm_position | NO_INTERRUPTION_NO_OVERLAP | isstandardpos=1 | hbpm_positionhr |
| hbpm_positionhr | t_hbpm_position | HisModel（同表）| isstandardpos=0 | hrpi_empjobrel |
| haos_adminorghis | t_haos_adminorg | NO_INTERRUPTION_NO_OVERLAP | iscurrentversion | hrpi_* 多个 |
| hbjm_jobhr（职位）| t_hbjm_jobhr | HisModel | - | hbpm_stposition.job |

---

## 5. 上下游依赖图（关键耦合点）

```
hbp_histimeseqtpl (HR 时序模板 · 28 个时序字段)
       │
       └── hbpm_stposition（标准岗位维护 · 本场景）
               │
               ├── isstandardpos="1" 区分键
               │
               ├── 物理表 t_hbpm_position 共用
               │         │
               │         └── hbpm_positionhr（岗位信息 · isstandardpos="0"）
               │                   │
               │                   └── stposition = hbpm_stposition.boid（PR-009）
               │                               │
               │                               └── hrpi_empjobrel（员工任职）
               │
               ├── 时序版本管理（HisModelOPCommonPlugin）
               │         └── save/change 产生版本链
               │                   └── boid 跨版本不变
               │
               ├── 编码规则（CodeRuleOp）
               │         └── 按 org 维度自动生成 number
               │
               └── StandardPositionMsgHandleOp（消息父类）
                         └── 可能发 BEC 通知下游（likely · 待实证）
                                   └── ISV 订阅方
```

---

## 6. 上下游 5 条核心认知

1. **物理表共享**：本场景与 hbpm_positionhr 共用 t_hbpm_position · ISV 加字段必须双场景回归
2. **boid 是引用维度**（PR-009）· hbpm_positionhr.stposition 存 boid · 变更不需级联更新
3. **BEC 待确认**（likely）· StandardPositionMsgHandleOp 父类可能发消息 · ISV 订阅前先去开发平台查事件号
4. **F7 返回 boid**（PR-009）· 所有从 F7 拿到的 stposition 值都是 boid · 查询时必须配合 iscurrentversion=true（PR-008）
5. **双场景元数据修改影响**（重要）· 给 hbpm_stposition 加字段同时影响 hbpm_positionhr · 部署后必须双场景回归

---

## 7. ISV 扩展协作建议

| 协作目的 | 推荐 CS | 影响范围 |
|---|---|---|
| 禁用前检查 positionhr 引用 | CS-02（onAddValidators@disable）| 仅 disable 路径 |
| 变更时通知下游系统 | CS-05（BEC 扩展）| 所有订阅方 |
| 字段联动（job → positiontype）| CS-03（propertyChanged）| 仅本场景表单 |
| 批量查当前版本岗位 | CS-04（时序查询规范）| 跨场景查询 |
| 给岗位加扩展字段 | CS-01（modifyMeta）| 本场景 + hbpm_positionhr |

---

## 8. 关联文档

- `03_model_design.md` · 共用物理表架构
- `08_impact_analysis.md` · 变更影响面
- `06_customization_solutions.md` · CS-02 ~ CS-05
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009 / PR-010 / PR-011
- `knowledge/scenarios/haos_adminorghis/11_upstream_downstream_logic.md` · 同性质场景对比

---

## 9. ISV 插件注册顺序（RowKey / PR-002）

**PR-002 铁律**：ISV 插件 RowKey 必须大于标品插件 RowKey，确保执行顺序正确。

| 标品插件 | RowKey（参考值）| ISV 插件 RowKey 要求 |
|---|---|---|
| StandardPositionEdit | 10 | ISV 使用 RowKey ≥ 20 |
| HisModelFormCommonPlugin | 5 | ISV 使用 RowKey ≥ 15 |
| StandardPositionSaveOp | 20 | ISV OP 使用 RowKey ≥ 30 |

```java
// PR-002 正确姿势：ISV FormPlugin RowKey 要在标品之后
@SdkPlugin(entityNumber="hbpm_stposition", pluginKey="${ISV_FLAG}_stposition_custom_edit", 
    order=50)  // ← 标品最大 RowKey 约 20，ISV 用 50 确保父模板先执行
public class TdkwStPositionCustomPlugin extends HRDataBaseEdit {
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);  // 调用父类（PR-001 并列挂原则）
        // ISV 自定义逻辑
    }
}
```

**注意**：RowKey 不是继承顺序，而是注册顺序。两个并列挂的插件通过 RowKey 决定谁先执行 afterBindData 等方法。

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
