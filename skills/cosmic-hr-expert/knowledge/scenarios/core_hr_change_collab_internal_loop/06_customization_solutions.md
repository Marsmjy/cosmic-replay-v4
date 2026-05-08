# 员工变动协作 · 核心人力内部回流 · 定制方案

## 协作链路全景

本场景的协作链路（v5+ 6 层闭环证据 · 详见 02_business_rules.md）：

```
[BEC 订阅] → [订阅入口类（壳）] → [调度作业] → [真处理类（壳）] → [抽象基类（模板）] → [业务接口]
```

## 标品已知协作映射

(本场景无明确"调度作业 → 真处理类"映射·走 BEC 直接订阅模式)

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
| (无 sch_job 调度作业链路 · 走纯 BEC 直接订阅) | - | - | - |

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

### hdm · 调配管理（调动/兼职/转正等单据 → 写底表）

| 反哺场景 | 反哺类数 | 真业务规则文档 |
|---|:---:|---|
| [`core_hr_promotion`](../core_hr_promotion/) | 67 | [02_business_rules.md](../core_hr_promotion/02_business_rules.md) · [07_ext_points.md](../core_hr_promotion/07_ext_points.md) |
| [`core_hr_transfer`](../core_hr_transfer/) | 46 | [02_business_rules.md](../core_hr_transfer/02_business_rules.md) · [07_ext_points.md](../core_hr_transfer/07_ext_points.md) |
| [`core_hr_parttime`](../core_hr_parttime/) | 39 | [02_business_rules.md](../core_hr_parttime/02_business_rules.md) · [07_ext_points.md](../core_hr_parttime/07_ext_points.md) |
| [`core_hr_transferout_in`](../core_hr_transferout_in/) | 28 | [02_business_rules.md](../core_hr_transferout_in/02_business_rules.md) · [07_ext_points.md](../core_hr_transferout_in/07_ext_points.md) |
| [`core_hr_infochange`](../core_hr_infochange/) | 14 | [02_business_rules.md](../core_hr_infochange/02_business_rules.md) · [07_ext_points.md](../core_hr_infochange/07_ext_points.md) |

### hlcm · 劳动合同和协议管理（合同档案/电子签）

| 反哺场景 | 反哺类数 | 真业务规则文档 |
|---|:---:|---|
| [`core_hr_econtract`](../core_hr_econtract/) | 190 | [02_business_rules.md](../core_hr_econtract/02_business_rules.md) · [07_ext_points.md](../core_hr_econtract/07_ext_points.md) |
| [`core_hr_contract`](../core_hr_contract/) | 85 | [02_business_rules.md](../core_hr_contract/02_business_rules.md) · [07_ext_points.md](../core_hr_contract/07_ext_points.md) |
| [`core_hr_contract_termination`](../core_hr_contract_termination/) | 36 | [02_business_rules.md](../core_hr_contract_termination/02_business_rules.md) · [07_ext_points.md](../core_hr_contract_termination/07_ext_points.md) |

### hpfs · 人事基础服务（chgaction 中枢/字段映射）

| 反哺场景 | 反哺类数 | 真业务规则文档 |
|---|:---:|---|
| [`core_hr_onboard`](../core_hr_onboard/) | 39 | [02_business_rules.md](../core_hr_onboard/02_business_rules.md) · [07_ext_points.md](../core_hr_onboard/07_ext_points.md) |
| [`core_hr_audit`](../core_hr_audit/) | 27 | [02_business_rules.md](../core_hr_audit/02_business_rules.md) · [07_ext_points.md](../core_hr_audit/07_ext_points.md) |
| [`core_hr_parttime`](../core_hr_parttime/) | 21 | [02_business_rules.md](../core_hr_parttime/02_business_rules.md) · [07_ext_points.md](../core_hr_parttime/07_ext_points.md) |
| [`core_hr_promotion`](../core_hr_promotion/) | 18 | [02_business_rules.md](../core_hr_promotion/02_business_rules.md) · [07_ext_points.md](../core_hr_promotion/07_ext_points.md) |
| [`core_hr_transfer`](../core_hr_transfer/) | 16 | [02_business_rules.md](../core_hr_transfer/02_business_rules.md) · [07_ext_points.md](../core_hr_transfer/07_ext_points.md) |
| [`core_hr_transferout_in`](../core_hr_transferout_in/) | 15 | [02_business_rules.md](../core_hr_transferout_in/02_business_rules.md) · [07_ext_points.md](../core_hr_transferout_in/07_ext_points.md) |
| [`core_hr_infochange`](../core_hr_infochange/) | 14 | [02_business_rules.md](../core_hr_infochange/02_business_rules.md) · [07_ext_points.md](../core_hr_infochange/07_ext_points.md) |
| [`core_hr_quit`](../core_hr_quit/) | 13 | [02_business_rules.md](../core_hr_quit/02_business_rules.md) · [07_ext_points.md](../core_hr_quit/07_ext_points.md) |

> **备注**：上面是按 deep_resolve 反哺类数排序的 Top 场景·
> 未来如下游云（payroll_*/attendance_*/talent_dev_*）建立独立场景且包含 EmployeeCoordServiceImpl 的具体实现·
> 重跑 `python scripts/_check_collab_downstream.py` 看新的 L7 实证发现
---

## L7 真实现层 · 下游云高频锚点（2026-05-02 v3 升级·已建场景反向链接）

**下游云**: 核心人力内循环（不出 core_hr） · `bizCloudId=<core_hr 内部>`

**真处理类**: `<内部循环·不分发>`

**状态**: ✅ 内循环已闭环 · 见 _patch_collab_l7.py 已补的 L7 实证发现段（hdm/hlcm/hpfs/hspm）

**下游云锚点**：

> ✅ 内循环已闭环 · 见 _patch_collab_l7.py 已补的 L7 实证发现段（hdm/hlcm/hpfs/hspm）

