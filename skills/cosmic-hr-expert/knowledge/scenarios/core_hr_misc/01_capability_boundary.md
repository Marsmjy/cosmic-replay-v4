# core_hr_misc · 能力边界

> **聚合场景**：core_hr_misc · 包含 3 个 hbss 字典实体（核心人力杂项底表 · 3 个独立 hrpi 实体 · personuserrel 是登录用户与员工的桥 · quittype 是离职单据下拉源 · reempl...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

核心人力杂项底表 · 3 个独立 hrpi 实体 · personuserrel 是登录用户与员工的桥 · quittype 是离职单据下拉源 · reemploymentrel 是离职后再入职关联

## 涉及实体（3 个）

- `hrpi_personuserrel`
- `hrpi_quittype`
- `hrpi_reemploymentrel`

## 标准模式

- **插件模式**：HRTemplateBillEdit / HRBaseDataTplEdit · ISV @SdkPlugin 并列挂 · 禁继承 HisModel*
- **跨云影响**：中——多数实体在核心人力域内 · 部分（peraddress/familymemb 等）被薪酬/福利云引用
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
