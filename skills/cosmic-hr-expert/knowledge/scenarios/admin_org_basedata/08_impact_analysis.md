# admin_org_basedata · 影响分析

> **聚合场景**：admin_org_basedata · 包含 5 个 hbss 字典实体（组织基础资料 5 件套 · 聚合场景...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

组织基础资料 5 件套 · 聚合场景

## 涉及实体（5 个）

- `haos_adminorgtype`
- `haos_adminorglayer`
- `haos_adminorgfunction`
- `haos_orgchangereason`
- `haos_changescene`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的影响分析章节（5 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
