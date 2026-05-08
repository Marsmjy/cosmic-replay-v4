# core_hr_pfs_personfield · 业务流程

> **聚合场景**：core_hr_pfs_personfield · 包含 2 个 hbss 字典实体（**ISV 配置 chgaction 字段映射时的字段元数据池**。filemapmanager 配置界面选 targetfieldnew 时下拉源就是 hpf...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**ISV 配置 chgaction 字段映射时的字段元数据池**。filemapmanager 配置界面选 targetfieldnew 时下拉源就是 hpfs_personfield。base01 是单据基模板（4 层继承 hbp_hrbilltpl），ISV 写自定义单据时 inherit base01 即可继承基础字段（org / employee / billno / billstatus 等）。build_plan 提到 22 单据基模板（base01-base22）· 此处只列代表实体。

## 涉及实体（2 个）

- `hpfs_personfield`
- `hpfs_base01`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的业务流程章节（2 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
