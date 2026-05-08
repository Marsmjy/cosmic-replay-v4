# core_hr_econtract · 数据流

> **聚合场景**：core_hr_econtract · 包含 1 个 hbss 字典实体（电子签集成实战 · 详见 PPT05_DEEP_TRACE.md 第 12 节。支持法大大公有云 + 法大大私有云 + 易企签公有云 3 厂商。签署状态：beg...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

电子签集成实战 · 详见 PPT05_DEEP_TRACE.md 第 12 节。支持法大大公有云 + 法大大私有云 + 易企签公有云 3 厂商。签署状态：begin / check / csign / esign / confirm / all。标品定时任务：hlcm_repair_esign_task (失败手动补偿)。

## 涉及实体（1 个）

- `core_hr_econtract`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的数据流章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
