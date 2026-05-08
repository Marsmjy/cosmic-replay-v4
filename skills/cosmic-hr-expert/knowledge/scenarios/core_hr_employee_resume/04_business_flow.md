# core_hr_employee_resume · 业务流程

> **聚合场景**：core_hr_employee_resume · 包含 12 个 hbss 字典实体（员工履历类附表聚合 · 12 hrpi 实体 · 多为时间轴/历史版本附表 · ISV 扩展走 @SdkPlugin 并列挂 HRTemplateBillEdi...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

员工履历类附表聚合 · 12 hrpi 实体 · 多为时间轴/历史版本附表 · ISV 扩展走 @SdkPlugin 并列挂 HRTemplateBillEdit · 禁继承 HisModelOPCommonPlugin / HisModelFormCommonPlugin / TimelineTplOp

## 涉及实体（12 个）

- `hrpi_pereduexp`
- `hrpi_preworkexp`
- `hrpi_empproexp`
- `hrpi_perlgability`
- `hrpi_perocpqual`
- `hrpi_perpractqual`
- `hrpi_perprotitle`
- `hrpi_emptrainfile`
- `hrpi_rsmpatinv`
- `hrpi_rsmproskl`
- `hrpi_empstage`
- `hrpi_emptutor`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（12 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
