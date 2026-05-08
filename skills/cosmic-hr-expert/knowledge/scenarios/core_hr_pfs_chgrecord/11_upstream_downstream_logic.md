# core_hr_pfs_chgrecord · 上下游逻辑

> **聚合场景**：core_hr_pfs_chgrecord · 包含 3 个 hbss 字典实体（**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.begin...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 真正的执行入口**：单据审批通过 → 生成 hpfs_chgrecord 流水 → 标品 ChgRecordEffectOp.beginOperationTransaction 调 IPersonGenericService.deleteBatch + saveBatch 实际改人员底表 → processPersonFlow 写流入流出 → processChargePerson 写部门负责人 → syncEmployeeLicenseCert 同步许可证。**datastatus 状态机**：0=待生效 / 1=已生效 / 2=未来生效定时（effecttime>now 且 future-effect 配置）。异常时 PerChgStatusEnum.SMALLEST_FAIL 写回单据 + 触发 aftereffect op。5 关键 OP：ChgRecordEffectOp / DiscardOp / AfterEffectOp / ChgRollbackOp / ChgRecordEditPlugin。

## 涉及实体（3 个）

- `hpfs_chgrecord`
- `hpfs_chgrecordentry`
- `hpfs_changerecord`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（3 个）
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

## 上下游联动 · 跨云协作机制（PPT slide 49 + 反编译）

### 上游：人事单据 → hpfs_chgrecord

```
hdm_transferapply (调动单据)
hdm_partbill (兼职单据)
hdm_regbill (转正单据)
hom_onbrdbill (入职单据)
htm_quitapply (离职单据)
ISV 自定义单据 (继承 6 大模板之一)
   │
   │ 单据头.chgaction = hpfs_chgaction.id
   │ 单据.提交并生效
   ↓
hpfs_chgrecord (流水头) + entryentity (字段明细)
```

### 下游：hpfs_chgrecord → hrpi_* + 流入流出 + 跨云事件

```
ChgRecordEffectOp.beginOperationTransaction
  ├── IPersonGenericService.deleteBatch + saveBatch
  │     ↓ 改 hrpi_employee / empentrel / assignment / empposorgrel
  │     ↓ 改扩展实体 (按 filemapmanager 配置)
  ├── ChgUtils.savePersonFlow
  │     ↓ 写 hpfs_personflow + hpfs_personflow_file
  ├── ChgUtils.executeOperate(haos_chargeperson, "save")
  │     ↓ 写部门负责人 (org_dev 云联动)
  ├── ILicenseCertApplicationService.syncEmployeeLicenseCert
  │     ↓ 同步许可明细
  └── 完成后发布 hpfs_chgrecord.aftereffect
        ↓
        ├── 薪酬云订阅 → 写 hcdm_* 业务数据
        ├── 考勤云订阅 → 写 hcam_* 业务数据
        ├── 绩效云订阅 → 写 perf_* 业务数据
        └── 福利云订阅 → 写 welfare_* 业务数据
```

### 跨云协作铁律

- **下游云不直接读 hrpi_***：通过订阅 chgrecord.aftereffect 异步同步
- **不要订阅 chgrecord.effect**：在 TX 内 · 异常会回滚 · 双写脏数据风险
- **对 chgrecord.id 做幂等**：可能被重发
- **按 effecttime 排序处理**：保证下游状态一致性

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

> 自动生成 · 数据源 `_cross_cloud_reports/attendance_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 1 处跨云。

### `hpfs_chgrecord` （跨云引用 1 处）

#### ⬇️ HR 基础服务云（`hr_hrmp`）1 处

| form | field | type |
|---|---|---|
| `hrcs_permapplybill` | `chgrecord` | BasedataField |

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
