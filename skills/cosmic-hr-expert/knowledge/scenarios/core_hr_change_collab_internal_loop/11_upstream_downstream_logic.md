# 员工变动协作 · 核心人力内部回流 · 上下游逻辑

## 上游事件（来自 core_hr）

本场景订阅以下 core_hr 上游事件：

- `hpfs_chgrecord.effect`
- `hpfs_chgrecord.rollback`
- `hpfs_chgrecord.rollbackcontract`
- `hr.hdm.hdm_parttimeapplybill`
- `hr.hdm.hdm_regbasebill`
- `hr.hdm.hdm_transferlayoutbill`
- `hr.hlcm.hlcm_contract`
- `hr.hlcm.hlcm_contract.revisecontract`
- `hr.hlcm.hlcm_contract.trancontract`
- `hr.hlcm.hlcm_hiredperson`
- `hr.hpfs.hpfs_chgrecord`
- `hr.hspm.hspm_attachmentmanage`
- `hr.hspm.hspm_infoapproval`

## 下游云

**核心人力云内部** —— 通过本场景的 15 个订阅类接收事件，
然后派 0 个 sch_job 异步执行下游业务（真处理类详见 02_business_rules.md）。

## 跨云数据契约

<!-- BEGIN cross-cloud-upstream -->
- 上游契约：`hpfs_chgrecord` 实体的 chgaction / chgcategory / status 字段
- 上游不能改字段定义（破坏所有协作下游）
<!-- END cross-cloud-upstream -->

<!-- BEGIN cross-cloud-downstream -->
- 下游服务：见 02 章节"关联调度作业详情"
- 下游 ISV 扩展：见 07_ext_points.md
<!-- END cross-cloud-downstream -->

<!-- polished_form_scene_v2 · 2026-04-30 P1.4-A -->
