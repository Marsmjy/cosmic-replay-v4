# core_hr_infochange · 异常处理

> **聚合场景**：core_hr_infochange · 包含 1 个 hbss 字典实体（**chgaction 用得最纯粹的场景** · 信息变更单 → 通过 chgaction 直接驱动 hrpi 字段变更，不涉及任职变化（chgEvent = ...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

**chgaction 用得最纯粹的场景** · 信息变更单 → 通过 chgaction 直接驱动 hrpi 字段变更，不涉及任职变化（chgEvent = INFO_MODIFY）· 100% 体现'零代码 ISV 扩展'。ISV 加 hrpi_employee/empentrel 等字段后，配置 hpfs_filemapmanager 即可让标品自动写入。通用 OP：PerChgTplSaveOp / PerChgTplEffectOp / PerChgTplSubmitOp（通用人事变动模板 OP · hpfs_hrcommonbilltplext 模板派生）。

## 涉及实体（1 个）

- `core_hr_infochange`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的异常处理章节（1 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。
