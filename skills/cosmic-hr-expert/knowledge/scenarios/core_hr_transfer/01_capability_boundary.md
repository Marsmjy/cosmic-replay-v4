# core_hr_transfer · 能力边界

> **聚合场景**：core_hr_transfer · 包含 2 个 hbss 字典实体（调动管理多单据聚合 · chgaction 大类 = INFO_TRANSFER。实证：TransferEffectOp:318 newEmpJobRel.se...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

调动管理多单据聚合 · chgaction 大类 = INFO_TRANSFER。实证：TransferEffectOp:318 newEmpJobRel.set('chgaction', transferBill.get('affaction')) — **调动单据 affaction 字段直接传给新职级职等记录的 chgaction 字段**。字段命名规则实证：a_ = after (目标态) / b_ = before (原态) / ba_a_tid (调后 assignment) / bb_a_tid (调前 assignment) / arealitycompany (调后实际公司) / ajobclass / ajoblevel / ajobgrade (调后职位类/级/等) / b_effectivedate (生效日期)。关键 OP：TransferSaveOp / TransferEffectOp / TransferAfterEffectOp / TransferConfirmOp。标品定时任务：hdm_transfereffect_plan_SKDP_S。ISV 扩展点：IStaffTransferExtPlugin (跳过占编)。

## 涉及实体（2 个）

- `hdm_transferapply`
- `hdm_transferbatch`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
