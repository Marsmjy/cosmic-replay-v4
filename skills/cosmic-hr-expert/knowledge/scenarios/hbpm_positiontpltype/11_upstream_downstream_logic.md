# 上下游联动 · 岗位模板类型（hbpm_positiontpltype）

> **状态**: 🟢 基于场景分析 + 反编译实证（无 BEC）
> **confidence**: verified（BEC 部分 grep 0 实证）
> **最后更新**: 2026-04-27

---

## 一、上游依赖

| 上游 | 依赖方式 | 说明 |
|---|---|---|
| 平台编码规则（bos_coderule）| CodeRuleOp 生成 number | 编码格式可在编码规则基础资料中配置 |
| 出厂数据包（标品导入）| issyspreset=true 行 | 系统预置模板类型由标品升级包写入 |
| bos_org（组织）| createorg/org/useorg 字段 | L3 带组织字段引用平台基础组织数据 |

---

## 二、下游影响

### 2.1 直接下游

| 下游场景 | 引用字段 | 说明 |
|---|---|---|
| `hbpm_positiontpl`（岗位模板）| type → hbpm_positiontpltype.id | 岗位模板选择所属类型 |

### 2.2 间接下游

```
hbpm_positiontpltype（岗位模板类型）
    ↓ 被引用（type 字段）
hbpm_positiontpl（岗位模板）
    ↓ 岗位模板应用于
hbjm_jobhr（岗位主数据）
    ↓
hrpi_empjobrel（员工岗位关系）
```

---

## 三、boid / PR-009 在本场景的说明

**本场景是 BaseFormModel，不涉及 boid 概念。**

- id 即是唯一标识
- 下游（hbpm_positiontpl）引用本场景时，直接存本场景的 id
- 与 HisModel（如 hbjm_jobhr）的区别：hbjm_jobhr 有 boid，下游引用岗位时存 boid（PR-009）；但 hbpm_positiontpltype 作为更上游的字典，引用时直接用 id

---

## 四、BEC 订阅方说明（标品 grep 0 · 实证）

**反编译验证**：
```bash
grep -rE "triggerEventSubscribe|IEventService|EventServiceHelper" \
  knowledge/_sdk_audit/_decompiled/scenarios/hbpm_positiontpltype/
# 0 处命中
```

**结论**：
- 标品**没有在本场景发布任何 BEC 事件**
- 标品**没有在本场景订阅任何 BEC 事件**

### ISV 如需 BEC 的方案

如确有外部系统需要感知岗位模板类型变更，遵循 PR-011：
1. 在【开发平台 → 业务事件管理】预注册 eventNumber（如 `hbpm.positiontpltype.changed`）
2. 建 ISV 并列挂 OP（父类：`HRDataBaseOp`，**禁继承 PositionTplTypeSaveOp** · PR-001）
3. 重写 `afterExecuteOperationTransaction`（PR-010 第 9 步）
4. 调 `IEventService.triggerEventSubscribeJobs(...)` 发布事件

但基础资料字典类场景通常不需要 BEC，优先考虑下游直接查表。

---

## 五、与同域场景的联动关系

| 场景 | 关系 | 说明 |
|---|---|---|
| `hbpm_positiontpl`（岗位模板）| 下游（引用本场景）| 岗位模板的 type 字段引用本场景 |
| `hbpm_basedatalist`（岗位基础资料）| 同域并列 | 同为 hbpm 岗位域字典 |
| `hbpm_reportcoreltype`（协作类型）| 同域并列 | 同为 hbpm 岗位域字典 |
| `hbp_bd_orgtpl_dlg`（HR 组织基础资料模板）| 继承关系（L3）| 本场景继承自此模板，组织管控字段由此引入 |

---

## 五、数据库多语言表规范（TOPIC-11 · _l 结尾）

苍穹多语言字段（name/description 等 I18nTextField）存储在以 `_l` 结尾的多语言表（不是 `_i` 拆分表）：

| 表类型 | 示例 | 说明 |
|---|---|---|
| 主表 | `t_hbpm_positiontpltype` | 非多语言字段 |
| 多语言表（_l 结尾）| `t_hbpm_positiontpltype_l` | name/description 等 I18nTextField |

ISV 用 `BusinessDataServiceHelper.load` 查询时会自动 JOIN `_l` 多语言表，无需手动处理。

---

## 六、ID 生成规范（PR-005 · kd.bos.id.ID）

ISV 代码中需要生成主键时，**必须用苍穹框架的 ID 工具**，不可用 UUID 或自增序号：

```java
// PR-005 规范：用 kd.bos.id.ID 生成分布式 ID
import kd.bos.id.ID;

long newId = ID.genLongId();      // 生成 Long 型 ID
String newStrId = ID.genStringId(); // 生成 String 型 ID（较少用）
```

**反模式**：
- ❌ `UUID.randomUUID()` — 苍穹 ORM 不接受 UUID 格式主键
- ❌ `System.currentTimeMillis()` — 分布式下会重复
- ❌ 手写自增序列 — 跨节点冲突

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

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
