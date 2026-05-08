# core_hr_parttime · 异常处理

> **聚合场景**：core_hr_parttime · 包含 4 个 hbss 字典实体（兼职管理 5 单据聚合 · chgaction 大类 = INFO_TRANSFER (兼职子类)。实证：兼职 → 创建副 hrpi_assignment (i...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

兼职管理 5 单据聚合 · chgaction 大类 = INFO_TRANSFER (兼职子类)。实证：兼职 → 创建副 hrpi_assignment (isprimary=false) + 新 hrpi_empposorgrel。兼职终止 → 终结副 assignment.enddate。关键 OP：PartSaveOp / PartTimeEffectOp / PartBillSubmitAndEffectOp / PartTimeAfterEffectOp / PartTimeEndAfterEffectOp。继承通用模板：hpfs_hrpartbillorgtplext (新增任职经历类) / hpfs_hrendbillorgtplext (任职终止类)。通用 OP：PerChgTplSaveOp / PerChgTplEffectOp。标品定时任务：hdm_parteffect_plan_SKDP_S / hdm_batchpartquit_plan_SKDP_S。

## 涉及实体（4 个）

- `hdm_parttimeapplybill`
- `hdm_parttimeendbill`
- `hdm_batchparttime`
- `hdm_parttimebatch`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（4 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
