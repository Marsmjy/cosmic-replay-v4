# core_hr_pfs_chgrecord · 数据流

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

1. 每个子实体单独的数据流章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

<!-- BEGIN ppt04-injected -->

## 数据流转 · datastatus 状态机

```
chgrecord 创建
  ↓ (effecttime 决定走向)
  ├── effecttime ≤ now or future-effect 关
  │     ↓
  │   datastatus=0 (待生效)
  │     ↓ ChgRecordEffectOp.beginOperationTransaction
  │     ├── 成功 → datastatus=1 (已生效) + bill.perchgstatus=SMALLEST_SUCCESS
  │     └── 失败 → 回滚 + bill.perchgstatus=SMALLEST_FAIL + chgrecord.errormsg=...
  │
  └── effecttime > now and future-effect 开
        ↓
      datastatus=2 (未来生效定时)
        ↓ 等定时任务（如 hdm_regeffect_plan_SKDP_S）扫到
        ↓ 转回 datastatus=0
        ↓ 走即时生效流程
```

## 流水持久化字段语义

| 字段 | 作用 |
|---|---|
| `hpfs_chgrecord.affrecord` | 关联回单据.id |
| `hpfs_chgrecord.billsource` | FK → 单据 form (反向解析 chgEntityNumber) |
| `hpfs_chgrecord.entryentity[].chgentity` | 被改的 hrpi 实体（如 hrpi_employee）|
| `hpfs_chgrecord.entryentity[].dataid` | 被改的 hrpi 记录 id |
| `hpfs_chgrecord.entryentity[].databefore_tag` | 改前 DynamicObject JSON (CustomDynamicObjectJsonSerializer) |
| `hpfs_chgrecord.entryentity[].dataafter_tag` | 改后 DynamicObject JSON |
| `hpfs_chgrecord.entryentity[].datacompare_tag` | 对比 JSON |
| `hpfs_chgrecord.paramentry[]` | 批量参数分录 (paramchgentity / paramerrormsg_tag 等) |
| `hpfs_chgrecord.datastatus` | 0=待生效 / 1=已生效 / 2=未来生效 |
| `hpfs_chgrecord.effecttime` | 生效时间（决定即时 vs 定时） |
| `hpfs_chgrecord.errormsg` | 异常信息 (回滚后写) |
| `hpfs_chgrecord.eventseq` | 事件序号 (CodeRule 生成) |
| `hpfs_chgrecord.traceid` | 链路追踪 ID |

<!-- END ppt04-injected -->
