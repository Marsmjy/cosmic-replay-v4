# core_hr_pfs_blacklist · 能力边界

> **聚合场景**：core_hr_pfs_blacklist · 包含 3 个 hbss 字典实体（hpfs 视角的黑名单管理。注意：**hpfs_blacklist 继承 hrpi_blacklist**（探针实证）—— 是 hrpi 黑名单底表的视图扩展，...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

hpfs 视角的黑名单管理。注意：**hpfs_blacklist 继承 hrpi_blacklist**（探针实证）—— 是 hrpi 黑名单底表的视图扩展，业务字段在 hrpi_blacklist 上。此场景管 ISV 在 hpfs 端追加的视图字段（如 simplecard 简卡展示）+ 操作日志。底表 ISV 扩展 → 看 core_hr_blacklist (1.6.1) + core_hr_blacklist_reasons (1.6.2)。

## 涉及实体（3 个）

- `hpfs_blacklist`
- `hpfs_blacklistlog`
- `hpfs_simplecard`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（3 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
