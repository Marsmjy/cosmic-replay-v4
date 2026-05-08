# core_hr_pfs_personflow · 业务流程

> **聚合场景**：core_hr_pfs_personflow · 包含 2 个 hbss 字典实体（**核心人力内部**的人员流转记录（hpfs 范围内）。每次 chgrecord 生效都会触发ChgRecordEffectOp.processPersonFl...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**核心人力内部**的人员流转记录（hpfs 范围内）。每次 chgrecord 生效都会触发ChgRecordEffectOp.processPersonFlow 写流入流出。注意：**此 personflow ≠ 员工变动协作**（后者是核心人力 → 下游云的事件协作机制 · 待用户单独输入展开 core_hr_change_collab 占位）。personflow 主表带 datastatus（0/1/2 对齐 chgrecord）+ effecttime + employee FK。personflow_file 是按 employee 聚合的档案视图。

## 涉及实体（2 个）

- `hpfs_personflow`
- `hpfs_personflow_file`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
