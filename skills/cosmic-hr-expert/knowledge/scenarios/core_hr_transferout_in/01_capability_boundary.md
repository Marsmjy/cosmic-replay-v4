# core_hr_transferout_in · 能力边界

> **聚合场景**：core_hr_transferout_in · 包含 1 个 hbss 字典实体（调出 + 调入跨组织协同 · 实际复用 hdm_transferapply 主表 + ButlinkType 字段区分子类。build_plan 提到 tran...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

调出 + 调入跨组织协同 · 实际复用 hdm_transferapply 主表 + ButlinkType 字段区分子类。build_plan 提到 transferoutbill / transferinbill / 各 layout，但探针未直接发现独立 form — 推测为 transferapply 的视图派生（按 transferType 字段过滤）。详查 PR-001 反编译。

## 涉及实体（1 个）

- `hdm_transferapply`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
