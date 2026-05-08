# core_hr_promotion · 异常处理

> **聚合场景**：core_hr_promotion · 包含 3 个 hbss 字典实体（试用和转正 6 单据聚合 · chgaction 大类 = INFO_MODIFY (用工关系转正)。实证：RegEffectOp 改 hrpi_trialpe...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

试用和转正 6 单据聚合 · chgaction 大类 = INFO_MODIFY (用工关系转正)。实证：RegEffectOp 改 hrpi_trialperiod.status + hrpi_employee.regstatus + hrpi_empentrel。关键 OP：RegSaveOp / RegEffectOp / RegHrSupplySubmitOp / RegSelfSaveOp / RegAfterEffectOp。标品定时任务：hdm_regeffect_plan_SKDP_S 处理 datastatus=2 未来生效。ISV 扩展点：IProbationMessageExtPlugin (转正问询消息 url 重定向)。

## 涉及实体（3 个）

- `hdm_regselfhelpbill`
- `hdm_regbasebill`
- `hdm_batchregbill`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
