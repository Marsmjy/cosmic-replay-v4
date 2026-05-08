# 员工变动协作 · 权限同步 · 上下游逻辑

## 上游事件（来自 core_hr）

本场景订阅以下 core_hr 上游事件：

- `hpfs_chgrecord.effect`
- `hpfs_chgrecord.rollback`
- `opmc.epa.epa_empcoordverifbill`

## 下游云

**HR 基础服务云权限子域 + 通用** —— 通过本场景的 8 个订阅类接收事件，
然后派 1 个 sch_job 异步执行下游业务（真处理类详见 02_business_rules.md）。

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
