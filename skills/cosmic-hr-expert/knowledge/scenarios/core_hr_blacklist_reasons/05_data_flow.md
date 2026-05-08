# core_hr_blacklist_reasons · 数据流

> **聚合场景**：core_hr_blacklist_reasons · 包含 3 个 hbss 字典实体（黑名单理由维护 + 流水 · 3 hrpi 字典/日志实体 · 配合 hrpi_blacklist 使用 · ISV 通常只加自定义理由不改流水结构...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

黑名单理由维护 + 流水 · 3 hrpi 字典/日志实体 · 配合 hrpi_blacklist 使用 · ISV 通常只加自定义理由不改流水结构

## 涉及实体（3 个）

- `hrpi_blacklistrmreason`
- `hrpi_toblacklistreason`
- `hrpi_blacklistlog`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的数据流章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
