# core_hr_pfs_chgevent · 上下游逻辑

> **聚合场景**：core_hr_pfs_chgevent · 包含 5 个 hbss 字典实体（**chgaction 是 ISV 零代码扩展的中枢配置**：每张人事单据头有 BasedataField → hpfs_chgaction。chgaction...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 是 ISV 零代码扩展的中枢配置**：每张人事单据头有 BasedataField → hpfs_chgaction。chgaction 上挂 chgcategory（→ chgevent 大类）+ filemapmanager 字段映射规则。审批通过后标品自动按 chgaction 配置改 hrpi_* 底表。ChgActionEditPlugin 决定：chgcategory 切换 / chgtype 任职状态变化 / fowtype 流入流出 / createnewassign 是否创建新分配 / invalidassign 是否作废原分配。8 大变动事件常量：INFO_QUIT/RETIRE/TRANSFER/MODIFY/INIT/EXTERNAL_REGULAR 等。

## 涉及实体（5 个）

- `hpfs_chgevent`
- `hpfs_chgcategory`
- `hpfs_chgaction`
- `hpfs_chgreason`
- `hpfs_chgactionlist`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

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

> 自动生成 · 数据源 `_cross_cloud_reports/attendance_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：4 个本场景实体 · 共 14 处引用 · 其中 14 处跨云。

### `hpfs_chgaction` （跨云引用 4 处）

#### ⬇️ HR 基础服务云（`hr_hrmp`）2 处

| form | field | type |
|---|---|---|
| `hrcs_dynamsgdealtrace` | `chgaction` | BasedataField |
| `hrcs_permapplybill` | `chgaction` | BasedataField |

#### ⬇️ 核心人力云（`core_hr`）2 处

| form | field | type |
|---|---|---|
| `hrpi_empjobrel` | `chgaction` | BasedataField |
| `hrpi_empposorgrel` | `chgaction` | BasedataField |

### `hpfs_chgcategory` （跨云引用 4 处）

#### ⬇️ HR 基础服务云（`hr_hrmp`）4 处

| form | field | type |
|---|---|---|
| `hrcs_dynamsgdealtrace` | `chgcategory` | BasedataField |
| `hrcs_dynascheme` | `assignactype` | BasedataField |
| `hrcs_dynascheme` | `cancelactype` | BasedataField |
| `hrcs_permapplybill` | `chgcategory` | BasedataField |

### `hpfs_chgevent` （跨云引用 3 处）

#### ⬇️ HR 基础服务云（`hr_hrmp`）3 处

| form | field | type |
|---|---|---|
| `hrcs_chgeventblacklist` | `chgevent` | BasedataField |
| `hrcs_dynamsgdealtrace` | `chgevent` | BasedataField |
| `hrcs_permapplybill` | `chgevent` | BasedataField |

### `hpfs_chgreason` （跨云引用 3 处）

#### ⬇️ 核心人力云（`core_hr`）3 处

| form | field | type |
|---|---|---|
| `hrpi_blacklist` | `quitreasonid` | BasedataField |
| `hrpi_dispatchinfo` | `dispreason` | BasedataField |
| `hrpi_empposorgrel` | `chgreason` | BasedataField |

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
