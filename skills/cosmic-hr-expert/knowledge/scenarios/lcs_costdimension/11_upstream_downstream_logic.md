# 上下游联动 · lcs_costdimension

<!-- polished_form_scene_v2 -->
> **状态**: 🟢 半自动（chgaction + Service 调用 + 跨云引用 · v2）
> **数据源**: 反编译 OP 类 · `_cross_cloud_index.json` · `cosmic_hr_chgaction_mechanism.md`
> **生成**: polish_form_scene_v2.py

## ✅ verified · 标品流转链路

## 🟡 同 form 维度的下游联动（待人工补充）

<TODO 人工补> 本场景往下游传播什么数据：

| 下游模块 | 联动点 | 同步/异步 | 失败策略 |
|---|---|---|---|
| 薪酬云 (payroll) | `<TODO>` | | |
| 考勤云 (attendance) | `<TODO>` | | |
| 绩效云 (performance) | `<TODO>` | | |
| 福利云 (welfare) | `<TODO>` | | |

---

<!-- BEGIN cross-cloud-upstream (auto · ADR-009) -->

## 上游底座引用（跨云）

> 自动生成 · 数据源 `_cross_cloud_index.json`
> 本场景无跨云上游底座引用。

<!-- END cross-cloud-upstream -->

---

<!-- BEGIN ppt-cross-injected -->

## 📚 PPT 知识引用（PPT 01 总论）

> 本场景属 HR 基础服务云（hr_hrmp）· 业务语义参考 [PPT01_DEEP_TRACE.md](../../docs/PPT01_DEEP_TRACE.md)
> - 跨云元规则：金字塔决策方法论 + 11 大特殊解决方案
> - 6 大可继承通用模板（hbp_bd_tpl_all / hbp_bd_timelinemintpl 等）
> - HR 通用 SDK 服务 16 个（HisModelServiceHelper / TimelineServiceHelper / RuleEngineServiceHelper 等）
> - 历史模型 vs 时间轴的 6 模板 + 字段差异

<!-- END ppt-cross-injected -->

<!-- BEGIN cross-cloud-downstream (auto · ADR-009) -->

## 下游消费者（被其他云引用）

> 自动生成 · 数据源 `_cross_cloud_reports/hr_hrmp_consumed_by.json` · 更新时间 2026-04-29
> 本场景拥有的实体被以下消费方引用：

**汇总**：1 个本场景实体 · 共 1 处引用 · 其中 0 处跨云。

> ⚠️ ISV 修改本场景实体的字段定义前，**必读**上面的下游消费者清单 · 改 fieldType / 删字段都会破坏跨云数据契约。

<!-- END cross-cloud-downstream -->
