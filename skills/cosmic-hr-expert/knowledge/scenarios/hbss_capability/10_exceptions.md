# hbss_capability · 异常处理

> **聚合场景**：hbss_capability · 包含 12 个 hbss 字典实体（HR 基础服务云 · 胜任力与能力管理字典聚合场景。包含：胜任力组(hbss_capacitygroup)、胜任力项(hbss_capacityitem)、胜任...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 胜任力与能力管理字典聚合场景。包含：胜任力组(hbss_capacitygroup)、胜任力项(hbss_capacityitem)、胜任力行为(hbss_capacityaction)、胜任力等级方案(hbss_capacityrankscheme)、胜任力评价节点(hbss_passessnode)、奖惩等级(hbss_rewpnmlevel)、奖惩类型(hbss_rew

## 涉及实体（12 个）

- `hbss_capacitygroup`
- `hbss_capacityitem`
- `hbss_capacityaction`
- `hbss_capacityrankscheme`
- `hbss_passessnode`
- `hbss_rewpnmlevel`
- `hbss_rewpnmtype`
- `hbss_scoreinterval`
- `hbss_scoresystem`
- `hbss_hrbuca`
- `hbss_hrbucafunc`
- `hbss_familiarity`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中——胜任力组/HR能力功能被核心人力云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（12 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
