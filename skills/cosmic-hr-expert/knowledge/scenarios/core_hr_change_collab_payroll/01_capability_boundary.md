# 员工变动协作 · 薪酬福利云 · 能力边界

> 场景类型：协作场景（非 form）· 下游云：薪酬福利云 (swc)
> 生成时间：2026-05-02 · 数据源：cosmic-bec-probe + cosmic-scheduler-probe + cosmic-class-resolver

## 场景定位

本场景描述 **swc/hpdi/hcdm/hsbs · 薪资数据集成 / 候选人定薪 / 银行卡同步** 的标品 BEC 订阅 + 调度链路。

## 核心能力

- 订阅 core_hr 上游事件（hpfs_chgrecord.effect / .rollback / 等）
- 接收 KDBizEvent 后派 sch_job 异步执行下游业务

## 不覆盖能力

<TODO 人工补：本场景不做的事·与下游云直接业务规则的边界>

## 标品订阅清单

共 5 条订阅 · 3 个唯一插件 FQN · 1 个调度作业锚点。

详见 `_bec_subscriptions.json` 机读 + 下方 02_business_rules.md。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4-A 协作场景骨架 -->
