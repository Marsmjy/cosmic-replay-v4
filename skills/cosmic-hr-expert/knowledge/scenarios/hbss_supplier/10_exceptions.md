# hbss_supplier · 异常处理

> **聚合场景**：hbss_supplier · 包含 12 个 hbss 字典实体（HR 基础服务云 · 供应商、银行、工作地点等后勤字典聚合场景。包含：供应商(hbss_supplier)、供应商级别(hbss_supplierlevel)、...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 供应商、银行、工作地点等后勤字典聚合场景。包含：供应商(hbss_supplier)、供应商级别(hbss_supplierlevel)、开户行(hbss_bankdeposit)、工作地点(hbss_workplace)、社会实体(hbss_enterprise)、鼓励语(hbss_encouragewords)、地址详情(hbss_addressdetail)、周期类型(

## 涉及实体（12 个）

- `hbss_supplier`
- `hbss_supplierlevel`
- `hbss_bankdeposit`
- `hbss_workplace`
- `hbss_encouragewords`
- `hbss_addressdetail`
- `hbss_cycletype`
- `hbss_entitytype`
- `hbss_procreatstatus`
- `hbss_procreatmode`
- `hbss_certmember`
- `hbss_category`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中——工作地点(hbss_workplace)被核心人力云引用3次
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（12 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
