# core_hr_archive_subforms · 数据流

> **聚合场景**：core_hr_archive_subforms · 包含 8 个 hbss 字典实体（员工档案派生模板大聚合 · hspm 实际有 200+ 个派生 form（按 5 后缀模式 + 附件 + 变动记录），每个对应特定视角（编辑/版本/时间轴/移动...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

员工档案派生模板大聚合 · hspm 实际有 200+ 个派生 form（按 5 后缀模式 + 附件 + 变动记录），每个对应特定视角（编辑/版本/时间轴/移动）。OpenAPI 不提供完整列表，需要从 hspm jar 反编译 DerivedTplConfig 获取。本场景作信息密度入口 · 列出 8 个代表实体 · ISV 扩展按'reform 三视角'（specialist/my/mobile）走，单个派生 form 不直接扩展。

## 涉及实体（8 个）

- `hspm_ermanfile_e`
- `hspm_ermanfile_ly`
- `hspm_ermanfile_emly`
- `hspm_ermanfile_tl`
- `hspm_ermanfile_tlmob`
- `hspm_attmgr`
- `hspm_attmgr_emly`
- `hspm_chgrecordview`

## 标准模式

- **插件模式**：HRTemplateBillEdit / DerivedTplConfig（派生模板）· ISV 通常不直接扩展派生
- **跨云影响**：中
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的数据流章节（8 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
