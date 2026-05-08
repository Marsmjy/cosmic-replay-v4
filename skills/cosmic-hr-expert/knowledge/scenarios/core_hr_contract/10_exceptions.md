# core_hr_contract · 异常处理

> **聚合场景**：core_hr_contract · 包含 5 个 hbss 字典实体（劳动合同核心 4 单据 + 1 档案聚合。**特点**：标品采用配置化映射（'关联合同协议申请单配置'）实现单据 ↔ 档案双向字段映射，ISV 加扩展字段后无需...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

劳动合同核心 4 单据 + 1 档案聚合。**特点**：标品采用配置化映射（'关联合同协议申请单配置'）实现单据 ↔ 档案双向字段映射，ISV 加扩展字段后无需写代码即可自动落库。关键 OP：ContractBaseSaveOp / ContractBaseSubmitEffectOp / PreSubmitEffectOp / ContractBaseSubmitOp / ContractBaseUnSubmitOp / ContractBaseWfUnSubmitOp。标品定时任务：hlcm_contacttohrpiinfo_task_SKDJ_S 同步合同档案 → hrpi_contractinfo。ISV 扩展点：IHLCMTemplateExtPlugin (合同模板变量替换)、IHLCMAttachmentExtPlugin (附件)、ISignActivityExtPlugin (协作活动)。

## 涉及实体（5 个）

- `hlcm_contract`
- `hlcm_contractapply`
- `hlcm_empprotocol`
- `hlcm_otheragreements`
- `hlcm_renewinquery`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
