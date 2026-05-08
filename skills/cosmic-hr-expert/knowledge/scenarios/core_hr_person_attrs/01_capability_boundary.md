# core_hr_person_attrs · 能力边界

> **聚合场景**：core_hr_person_attrs · 包含 11 个 hbss 字典实体（员工个人属性 + 家庭关系 + 证件 + 联系方式聚合 · 11 hrpi 实体 · 大多引用 hbss_person_attrs 的字典基础数据 · ISV ...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

员工个人属性 + 家庭关系 + 证件 + 联系方式聚合 · 11 hrpi 实体 · 大多引用 hbss_person_attrs 的字典基础数据 · ISV 扩展按 chgaction 配置驱动

## 涉及实体（11 个）

- `hrpi_partymember`
- `hrpi_fertilityinfo`
- `hrpi_perhobby`
- `hrpi_peraddress`
- `hrpi_percontact`
- `hrpi_perregion`
- `hrpi_percre`
- `hrpi_familymemb`
- `hrpi_emrgcontact`
- `hrpi_perfresult`
- `hrpi_perrprecord`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（11 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
