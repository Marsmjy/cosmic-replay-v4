# core_hr_kdhr_reports · 异常处理

> **聚合场景**：core_hr_kdhr_reports · 包含 3 个 hbss 字典实体（kdhr_corehr_* 不是真业务实体，是 hrptmc（HR 报表配置）平台的派生报表配置。业务归属 core_hr · 技术实现归 hrptmc。ISV...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

kdhr_corehr_* 不是真业务实体，是 hrptmc（HR 报表配置）平台的派生报表配置。业务归属 core_hr · 技术实现归 hrptmc。ISV 扩展报表逻辑必须改 hrptmc 配置而非 kdhr 元数据。_implementedBy: hrptmc · 详见 hrptmc 应用的 reportmanage / analyseobject 场景。

## 涉及实体（3 个）

- `kdhr_corehr_002`
- `kdhr_corehr_008`
- `kdhr_tenure_journaling`

## 标准模式

- **插件模式**：HRTemplateBillEdit / DerivedTplConfig（派生模板）· ISV 通常不直接扩展派生
- **跨云影响**：中
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
