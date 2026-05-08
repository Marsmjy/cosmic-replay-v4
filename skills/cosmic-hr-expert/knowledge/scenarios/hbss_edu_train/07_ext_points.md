# hbss_edu_train · 扩展点

> **聚合场景**：hbss_edu_train · 包含 18 个 hbss 字典实体（HR 基础服务云 · 教育、培训、资历字典聚合场景。包含：高等院校(hbss_college)、学历(hbss_diploma)、学位(hbss_degree)...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

HR 基础服务云 · 教育、培训、资历字典聚合场景。包含：高等院校(hbss_college)、学历(hbss_diploma)、学位(hbss_degree)、文凭类型(hbss_diplomatype)、教育证书类型(hbss_educerttype)、语言证书(hbss_languagecert)、语言类型(hbss_languagetype)、培训方式(hbss_trainmode)、培训

## 涉及实体（18 个）

- `hbss_college`
- `hbss_diploma`
- `hbss_degree`
- `hbss_diplomatype`
- `hbss_educerttype`
- `hbss_languagecert`
- `hbss_languagetype`
- `hbss_trainmode`
- `hbss_traintype`
- `hbss_ocpqual`
- `hbss_ocpquallevel`
- `hbss_protitle`
- `hbss_protitlelevel`
- `hbss_protitletype`
- `hbss_operationqual`
- `hbss_patentstatus`
- `hbss_patentscategory`
- `hbss_credentialstype`

## 标准模式

- **插件模式**：HRBaseDataTplEdit（标准基础资料模板 · ISV 禁继承 · @SdkPlugin 并列挂）
- **跨云影响**：中——学历/国籍/证书类型被核心人力云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的扩展点章节（18 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
