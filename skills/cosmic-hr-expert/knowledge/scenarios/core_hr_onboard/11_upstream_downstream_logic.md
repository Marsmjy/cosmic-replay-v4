# core_hr_onboard · 上下游逻辑

> **聚合场景**：core_hr_onboard · 包含 7 个 hbss 字典实体（入职管理 7 单据聚合 · chgaction 大类 = INFO_INIT。实证：OnbrdEffectOp.beginOperationTransactio...）
> **占位说明**：本文档为骨架占位 · 待 Agent 深度精修补内容
> **生成时间**：2026-04-29

## 概述

入职管理 7 单据聚合 · chgaction 大类 = INFO_INIT。实证：OnbrdEffectOp.beginOperationTransaction 调 IPersonGenericService.saveBatch 创建 hrpi_employee + hrpi_assignment + hrpi_empentrel + hrpi_trialperiod。关键 OP：OnbrdBillSaveOp / OnbrdEffectOp / OnbrdAfterEffectOp / PreOnBrdSaveOp。ISV 扩展点：IStaffOnbrdExtPlugin (占编)、IHOMLoginExtPlugin (登录)、IOnbrdInviteSupportAttachmentExtPlugin (附件)、IActivityDomainExtPlugin (协作)。

## 涉及实体（7 个）

- `hom_onbrdinfo`
- `hom_onbrdinfo_emp`
- `hom_preonbrdbasebill`
- `hom_prebatchonbrdbill`
- `hom_personallonbrd`
- `hom_personwaitstart`
- `hom_candidate`

## 标准模式

- **插件模式**：继承 hpfs 通用单据模板 + 实现 chgaction → hrpi 字段映射 (filemapmanager)
- **跨云影响**：高 · 单据驱动 hrpi 底表变更 + 触发跨云事件 hpfs_chgrecord.aftereffect
- **ISV 扩展原则**（PR-001）：禁止继承 HRBaseDataTplEdit · 必须并列挂 HRDataBaseEdit

## 待 Agent 精修要点

1. 每个子实体单独的上下游逻辑章节（7 个）
2. 反编译 sourceLine 实证（如有 hbss 业务 jar · 需扫 `hrmp-hbss-*.jar`）
3. 跨云引用真实下游（hbss 是底座 · 76+ 实体被组织/薪酬云引用 · 见 `_org_entity_ref_report.json`）

参见上游脚本：`scripts/complete_aggregate_scenes.py` · 由 P0-1 沉淀。

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/` · 更新时间 2026-04-29
> 本场景实体当前**未被其他云**引用。

<!-- END cross-cloud-downstream -->
