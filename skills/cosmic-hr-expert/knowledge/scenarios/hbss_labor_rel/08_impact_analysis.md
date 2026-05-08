# hbss_labor_rel · 影响分析

> **聚合场景**：hbss_labor_rel · 包含 13 个 hbss 字典实体（HR 基础服务云 · 劳动/用工关系字典聚合场景。包含：用工关系类型(hbss_laborreltype)、劳动关系类型分类(hbss_laborreltype...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 劳动/用工关系字典聚合场景。包含：用工关系类型(hbss_laborreltype)、劳动关系类型分类(hbss_laborreltypecls)、劳动关系状态(hbss_laborrelstatus)、劳动关系子类(hbss_laborrelsub)、合同类型(hbss_contracttypes)、合同类型分类(hbss_contracttypecat)、用工性质(hbs

## 涉及实体（13 个）

- `hbss_laborreltype`
- `hbss_laborreltypecls`
- `hbss_laborrelstatus`
- `hbss_laborrelsub`
- `hbss_contracttypes`
- `hbss_contracttypecat`
- `hbss_empnature`
- `hbss_employeegroup`
- `hbss_empgroup`
- `hbss_onboardsource`
- `hbss_appointtype`
- `hbss_apptreasongroup`
- `hbss_timelimittype`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：高——用工关系类型被核心人力云(4次)/组织发展云(4次)引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的影响分析章节（13 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
