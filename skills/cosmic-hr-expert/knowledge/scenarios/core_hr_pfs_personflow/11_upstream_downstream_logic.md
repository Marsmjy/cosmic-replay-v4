# core_hr_pfs_personflow · 上下游逻辑

> **聚合场景**：core_hr_pfs_personflow · 包含 2 个 hbss 字典实体（**核心人力内部**的人员流转记录（hpfs 范围内）。每次 chgrecord 生效都会触发ChgRecordEffectOp.processPersonFl...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**核心人力内部**的人员流转记录（hpfs 范围内）。每次 chgrecord 生效都会触发ChgRecordEffectOp.processPersonFlow 写流入流出。注意：**此 personflow ≠ 员工变动协作**（后者是核心人力 → 下游云的事件协作机制 · 待用户单独输入展开 core_hr_change_collab 占位）。personflow 主表带 datastatus（0/1/2 对齐 chgrecord）+ effecttime + employee FK。personflow_file 是按 employee 聚合的档案视图。

## 涉及实体（2 个）

- `hpfs_personflow`
- `hpfs_personflow_file`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

<!-- BEGIN ppt04-injected -->

## 上下游联动 · personflow 与员工变动协作的边界（用户拍板 · 不重叠）

| 概念 | 范围 | form 锚点 |
|---|---|---|
| **chgaction 机制** | 单据 → **核心人力内部底表** 的映射执行 | hpfs_chgaction + filemapmanager + chgrecord |
| **personflow** | **核心人力内部** 的流转记录（hpfs 范围内）| hpfs_personflow + hpfs_personflow_file |
| **员工变动协作** | 核心人力 → **下游云**（薪酬/考勤/绩效/福利）的事件协作 | ⚠️ **另一套机制** · 跟 personflow **不重叠** · 待用户单独输入展开 core_hr_change_collab |

## 数据流转

```
ChgRecordEffectOp.processPersonFlow
  ├── 读 chgRecord.entryentity[chgentity == "hpfs_personflow"]
  ├── 解析 dataafter_tag JSON → DynamicObject
  ├── 设置 datastatus=1
  └── 调 IPersonGenericService.executeOperate(personFlow, "save")
        ↓
      hpfs_personflow（按 employee 维度存）
        ↓
      hpfs_personflow_file（按 employee 归档视图）
```

## 字段语义

| hpfs_personflow 字段 | 作用 |
|---|---|
| `employee` | FK → hrpi_employee |
| `chgrecord` | FK → hpfs_chgrecord (反向追溯流水) |
| `effecttime` | 流入流出时刻 |
| `fowtype` | 流入流出类型 (来自 chgaction.fowtype) |
| `chgcategory` | 关联变动类型 |
| `datastatus` | 对齐 chgrecord.datastatus (0/1/2) |

**注意**：personflow 不是给下游云用的事件源，下游云走 chgrecord.aftereffect。

<!-- END ppt04-injected -->

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 03 + 04 沉淀）

> 本场景的业务语义补充见以下沉淀文档：
> - [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md) · 总论 · 金字塔决策方法论 + 11 大特殊解决方案 + L0-L5 业务模型分层
> - [PPT03_DEEP_TRACE.md](../../docs/PPT03_DEEP_TRACE.md) · 核心人力 + 人员信息 · 33 实体清单 + 6 大可继承模板 + 8 SDK 扩展点 + 30 OpenAPI
> - [PPT04_DEEP_TRACE.md](../../docs/CHGACTION_DEEP_TRACE.md) · chgaction 机制 22 段 · 反编译 + PPT 双源印证
> - [PPT05_DEEP_TRACE.md](../../docs/PPT05_DEEP_TRACE.md) · 劳动合同（hlcm 第三波预用知识）

### 关键 SDK 抓手（按本场景常用情况）

```java
// hrpi 数据访问（核心人力 33 场景共用）
HRPIEmployeeServiceHelper      员工信息处理
HSPMFileServiceHelper          人员档案处理
HSPMBusinessDataServiceHelper  HSPM 自定义查询

// 通用插件继承点（ISV 高频选）
ERManFileCommonAttPlugin       人员档案附表通用插件 ⭐
MyFileCommonAttPlugin          员工端附表通用插件
ManFileFormMobileCommonPlugin  移动端附表通用插件

// 历史模型 / 时间轴
HisModelServiceHelper          HR 历史模型服务
TimelineServiceHelper          HR 时间轴模型服务
```

### 业务事件订阅点（跨云协作）

```
hpfs_chgrecord.aftereffect    ⭐ 跨云协作正确订阅点（不要订阅 effect · TX 内会回滚）
hrpi_employee.syncBosUser     HR 人员↔BOS 用户同步
```

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
