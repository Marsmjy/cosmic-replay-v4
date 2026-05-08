# core_hr_quit · 影响分析

> **聚合场景**：core_hr_quit · 包含 2 个 hbss 字典实体（离职管理 5 单据聚合 · chgaction 大类 = INFO_QUIT。实证：HtmTplEffectOp 处理 hpfs_chgrecord.param...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

离职管理 5 单据聚合 · chgaction 大类 = INFO_QUIT。实证：HtmTplEffectOp 处理 hpfs_chgrecord.paramentry 中的 hrpi_empcadre 等附属底表。调用 ChgRecordOpHelper.setChgRecords + ChgRecordRepository.save 持久化。改 hrpi_empentrel.laborrelstatus + 创建 hrpi_terminationinfo + 终止 hrpi_assignment.enable。关键 OP：HtmTplEffectOp / PerChgTplEffectOp / PerChgTplTerminationAfterEffectOp。继承通用模板：hpfs_hrhtmbillorgtplext。标品定时任务：htm_effectquit_plan_SKDP_S。ISV 扩展点：IStaffQuitExtPlugin (占编入参替换)、IShareTaskExtPlugin (共享中心)、IActivityExtPlugin (离职协作活动)。

## 涉及实体（2 个）

- `htm_quitapply`
- `htm_quitapplybill`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的影响分析章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
