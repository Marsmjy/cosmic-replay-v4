# core_hr_contract_termination · 业务流程

> **聚合场景**：core_hr_contract_termination · 包含 1 个 hbss 字典实体（合同终止 / 解除 / 改签 · chgaction 大类 = 用工终止类 (复用 hpfs_hrhtmbillorgtplext) 或专属终止逻辑。影响 hr...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

合同终止 / 解除 / 改签 · chgaction 大类 = 用工终止类 (复用 hpfs_hrhtmbillorgtplext) 或专属终止逻辑。影响 hrpi_empentrel 状态 (laborrelstatus 转 'terminated' 或 'changed')。ISV 扩展：基础资料配置自定义终止/解除原因 + 业务规则关联校验。

## 涉及实体（1 个）

- `core_hr_contract_termination`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
