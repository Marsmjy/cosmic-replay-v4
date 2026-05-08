# hbss_law_entity · 扩展点

> **聚合场景**：hbss_law_entity · 包含 8 个 hbss 字典实体（HR 基础服务云 · 法律实体与聘用单位场景。核心实体：hbss_lawentity（法律实体，BaseEntity+EntryEntity，约50字段，最复杂...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 法律实体与聘用单位场景。核心实体：hbss_lawentity（法律实体，BaseEntity+EntryEntity，约50字段，最复杂的 hbss 实体）。相关实体：hbss_signcompany（聘用单位）、hbss_taxunit（纳税单位）、hbss_enterprise（用人单位）、hbss_signcompanyhis（聘用单位历史）。hbss_lawenti

## 涉及实体（8 个）

- `hbss_lawentity`
- `hbss_lawentityuse`
- `hbss_lawentityvrinf`
- `hbss_signcompany`
- `hbss_signcompanyhis`
- `hbss_taxunit`
- `hbss_enterprise`
- `hbss_payrollacrelation`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：高——法律实体被核心人力云(6次)引用，薪酬云中薪酬主体依赖法律实体
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（8 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
