# core_hr_pfs_mapping · 能力边界

> **聚合场景**：core_hr_pfs_mapping · 包含 4 个 hbss 字典实体（**ISV 零代码扩展核心引擎**：把人事单据的字段值按配置自动写入 hrpi_* 底表。filemapmanager 主表挂着 entryentity（每行一...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**ISV 零代码扩展核心引擎**：把人事单据的字段值按配置自动写入 hrpi_* 底表。filemapmanager 主表挂着 entryentity（每行一个目标实体）+ subentryentity（每行一个字段映射对，含 sourcefield/targetfieldnew/loadpersoninfo/writepersoninfo）。ISV 加自定义字段不需写 OP · 在此 form 上配置即可。fieldrelation/fieldrelationc 是字段对照明细查询视图。核心反编译：FieldRelationEdit (455 lines)、FileMapManagerSaveOp+FileMapManagerSaveValidator、ChgActionEditPlugin (244 lines · chgcategory 变动类型驱动)。

## 涉及实体（4 个）

- `hpfs_filemapmanager`
- `hpfs_fieldrelation`
- `hpfs_fieldrelationc`
- `hpfs_hrbm_config`

## 标准模式

- **插件模式**：HRBaseDataTplEdit / HRDataBaseOp · ISV 走 hpfs_chgaction + filemapmanager 配置 · 不直接继承 hpfs OP 类
- **跨云影响**：高 · hpfs 是所有人事单据的元配置层 · 改这里影响所有下游业务云
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的能力边界章节（4 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
