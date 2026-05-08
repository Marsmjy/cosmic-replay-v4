# core_hr_pfs_chgrecord · 异常处理

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

1. 每个子实体单独的异常处理章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
