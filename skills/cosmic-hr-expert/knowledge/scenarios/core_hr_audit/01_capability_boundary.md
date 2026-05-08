# core_hr_audit · 能力边界

> **聚合场景**：core_hr_audit · 包含 1 个 hbss 字典实体（HR 审批通用基础 · 提供给离职/调动等业务单据复用的审批基类。ApprovalRejectToSubmitOp 通用模式：审批驳回后回到提交态。应优先继承标...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 审批通用基础 · 提供给离职/调动等业务单据复用的审批基类。ApprovalRejectToSubmitOp 通用模式：审批驳回后回到提交态。应优先继承标品审批基类，不要从零起。

## 涉及实体（1 个）

- `core_hr_audit`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
