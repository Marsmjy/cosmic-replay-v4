# core_hr_pfs_crossvalid · 异常处理

> **聚合场景**：core_hr_pfs_crossvalid · 包含 2 个 hbss 字典实体（ISV 加自定义业务约束的位置 · 在 chgaction 执行前检查跨字段/跨实体规则。和 hpfs_chgaction 配套使用：chgaction 决定改...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

ISV 加自定义业务约束的位置 · 在 chgaction 执行前检查跨字段/跨实体规则。和 hpfs_chgaction 配套使用：chgaction 决定改什么字段，crossvalidation 决定何时拒绝改。典型用法：试用期内不可调岗 / 离职后 30 天内不可重雇 / 借调期间不可发起调动等。

## 涉及实体（2 个）

- `hpfs_crossvalidation`
- `hpfs_chgcrossvalid`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
