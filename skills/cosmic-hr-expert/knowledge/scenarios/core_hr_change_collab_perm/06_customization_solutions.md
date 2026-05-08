# 员工变动协作 · 权限同步 · 定制方案

## 协作链路全景

本场景的协作链路（v5+ 6 层闭环证据 · 详见 02_business_rules.md）：

```
[BEC 订阅] → [订阅入口类（壳）] → [调度作业] → [真处理类（壳）] → [抽象基类（模板）] → [业务接口]
```

## 标品已知协作映射

- 入口订阅 `EpaEmployeeCoordEventServicePlugin` → 调度作业 `epa_EpaEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.opmc.epa.business.coordination.EpaEmployeeCoordTask`

## ISV 扩展套路（薪酬/考勤/人才发展可参考）

### 套路 1：完全自定义协作（不依赖标品）

ISV 在自己应用里：
1. 在 `evt_subscription` 元数据加订阅条目（event_number = `hpfs_chgrecord.effect`）
2. 编写 `IEventServicePlugin` 实现类（FQN 由 ISV 自定义）
3. 在 plugin 里直接处理 chgaction → 写下游表

适用：ISV 项目自定义业务逻辑·与标品完全隔离

### 套路 2：复用标品 EmployeeCoordTask 模板（推荐）

ISV 实现自己的 `EmployeeCoordService` 接口实现类：
1. 实现 `kd.hr.hbp.business.coordination.api.EmployeeCoordService` 接口
2. 通过 ServiceFactory 注册（在 ISV 应用 spring config / serviceloader 里）
3. **复用 AbstractHrDataCoordTask 的 execute 模板**·无需重写定时调度

适用：ISV 项目跟标品下游云使用同一套协作框架

## ⭐ 留给下游云具体实现说明（不在本协作场景闭环）

> **本协作场景只到"接口契约 + 模板方法"层 · 具体业务实现 = 下游云独立场景的事**

| 下游云 | 真处理类 FQN（v5+ 已反编译入口）| 接口具体实现位置 | 详细业务规则归属场景 |
|---|---|---|---|
| HR 基础服务云权限子域 + 通用 | `kd.opmc.epa.business.coordination.EpaEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |

> **下游云场景建立时·补这一段**：每个下游云的 EmployeeCoordServiceImpl
> （实现接口的具体类）有它自己的真业务规则（写哪些下游表 / 抛哪些异常 / 调哪些 helper），
> 这些规则属于**下游云的业务边界**·应在下游云独立场景的 02 / 06 md 里说明。
> 本协作场景只负责**协作链路**·不重复说明下游业务。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4 v5+ · 留口子设计 -->

---

## L7 真实现层 · 实证发现（2026-05-02 自动补齐）

> 本场景的「留给下游云具体实现说明」原本是占位 `<TODO 下游云独立场景>`。
> 经 `_check_collab_downstream.py` 扫描·全仓已有以下场景反哺过本协作链路相关下游模块的真证据。
> ISV 想找「具体写哪些表 / 调哪些 helper / 抛哪些异常」·去这些场景的 02_business_rules.md / 07_ext_points.md。

### hrcs · HR 共享服务（权限/动态授权/数据规则）

| 反哺场景 | 反哺类数 | 真业务规则文档 |
|---|:---:|---|
| [`hrcs_warnscene`](../hrcs_warnscene/) | 13 | [02_business_rules.md](../hrcs_warnscene/02_business_rules.md) · [07_ext_points.md](../hrcs_warnscene/07_ext_points.md) |
| [`hrcs_warnscheme`](../hrcs_warnscheme/) | 12 | [02_business_rules.md](../hrcs_warnscheme/02_business_rules.md) · [07_ext_points.md](../hrcs_warnscheme/07_ext_points.md) |
| [`hrcs_dynascheme`](../hrcs_dynascheme/) | 7 | [02_business_rules.md](../hrcs_dynascheme/02_business_rules.md) · [07_ext_points.md](../hrcs_dynascheme/07_ext_points.md) |
| [`hrcs_activity`](../hrcs_activity/) | 6 | [02_business_rules.md](../hrcs_activity/02_business_rules.md) · [07_ext_points.md](../hrcs_activity/07_ext_points.md) |
| [`hrcs_empstrategy`](../hrcs_empstrategy/) | 5 | [02_business_rules.md](../hrcs_empstrategy/02_business_rules.md) · [07_ext_points.md](../hrcs_empstrategy/07_ext_points.md) |
| [`hrcs_entityctrl`](../hrcs_entityctrl/) | 5 | [02_business_rules.md](../hrcs_entityctrl/02_business_rules.md) · [07_ext_points.md](../hrcs_entityctrl/07_ext_points.md) |
| [`hrcs_label`](../hrcs_label/) | 5 | [02_business_rules.md](../hrcs_label/02_business_rules.md) · [07_ext_points.md](../hrcs_label/07_ext_points.md) |
| [`hrcs_orgstrategy`](../hrcs_orgstrategy/) | 5 | [02_business_rules.md](../hrcs_orgstrategy/02_business_rules.md) · [07_ext_points.md](../hrcs_orgstrategy/07_ext_points.md) |

### hrpi · HR 人员信息中心（员工档案/任职/同步 BOS 用户）

| 反哺场景 | 反哺类数 | 真业务规则文档 |
|---|:---:|---|
| [`core_hr_employee`](../core_hr_employee/) | 9 | [02_business_rules.md](../core_hr_employee/02_business_rules.md) · [07_ext_points.md](../core_hr_employee/07_ext_points.md) |
| [`core_hr_terminationinfo`](../core_hr_terminationinfo/) | 8 | [02_business_rules.md](../core_hr_terminationinfo/02_business_rules.md) · [07_ext_points.md](../core_hr_terminationinfo/07_ext_points.md) |
| [`core_hr_assignmentmag`](../core_hr_assignmentmag/) | 7 | [02_business_rules.md](../core_hr_assignmentmag/02_business_rules.md) · [07_ext_points.md](../core_hr_assignmentmag/07_ext_points.md) |
| [`core_hr_empentrel`](../core_hr_empentrel/) | 7 | [02_business_rules.md](../core_hr_empentrel/02_business_rules.md) · [07_ext_points.md](../core_hr_empentrel/07_ext_points.md) |
| [`core_hr_empposorgrel`](../core_hr_empposorgrel/) | 7 | [02_business_rules.md](../core_hr_empposorgrel/02_business_rules.md) · [07_ext_points.md](../core_hr_empposorgrel/07_ext_points.md) |
| [`core_hr_empsuprel`](../core_hr_empsuprel/) | 7 | [02_business_rules.md](../core_hr_empsuprel/02_business_rules.md) · [07_ext_points.md](../core_hr_empsuprel/07_ext_points.md) |
| [`core_hr_appointremoverel`](../core_hr_appointremoverel/) | 6 | [02_business_rules.md](../core_hr_appointremoverel/02_business_rules.md) · [07_ext_points.md](../core_hr_appointremoverel/07_ext_points.md) |
| [`core_hr_assignment`](../core_hr_assignment/) | 6 | [02_business_rules.md](../core_hr_assignment/02_business_rules.md) · [07_ext_points.md](../core_hr_assignment/07_ext_points.md) |

> **备注**：上面是按 deep_resolve 反哺类数排序的 Top 场景·
> 未来如下游云（payroll_*/attendance_*/talent_dev_*）建立独立场景且包含 EmployeeCoordServiceImpl 的具体实现·
> 重跑 `python scripts/_check_collab_downstream.py` 看新的 L7 实证发现
---

## L7 真实现层 · 下游云高频锚点（2026-05-02 v3 升级·已建场景反向链接）

**下游云**: 目标绩效云 (opmc) + HR 共享服务 (hrcs) · `bizCloudId=<opmc 未实探 / hrcs 在 hr_hrmp 已建>`

**真处理类**: `kd.opmc.epa.business.coordination.EpaEmployeeCoordTask`

**状态**: ✅ hrcs 侧已 11 场景闭环（hrcs_useradmingroup / hrcs_rolelist / hrcs_permfilelist 等） · ⏸ opmc 待 v3.x 增量

**下游云锚点**：

> ✅ hrcs 侧已 11 场景闭环（hrcs_useradmingroup / hrcs_rolelist / hrcs_permfilelist 等） · ⏸ opmc 待 v3.x 增量

