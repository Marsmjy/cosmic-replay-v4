# hies_entity_import · 能力边界

> **聚合场景**：hies_entity_import · 包含 1 个 hbss 字典实体（HR 实体导入（HIES-Pro） · 聚合场景...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 实体导入（HIES-Pro） · 聚合场景

## 涉及实体（1 个）

- `(场景中性 · 具体实体按子场景 · 如 hrpi_attfile / haos_personroster / hspm_* 等)`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
