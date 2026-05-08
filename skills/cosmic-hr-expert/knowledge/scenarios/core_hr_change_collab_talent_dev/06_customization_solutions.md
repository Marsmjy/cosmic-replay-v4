# 员工变动协作 · 人才发展云 · 定制方案

## 协作链路全景

本场景的协作链路（v5+ 6 层闭环证据 · 详见 02_business_rules.md）：

```
[BEC 订阅] → [订阅入口类（壳）] → [调度作业] → [真处理类（壳）] → [抽象基类（模板）] → [业务接口]
```

## 标品已知协作映射

- 入口订阅 `TdcsEmployeeCoordEventServicePlugin` → 调度作业 `tdcs_TdcsEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.${ISV_FLAG}.tdcs.bussiness.task.TdcsEmployeeCoordTask`

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
| 人才发展云 (tdc) | `kd.${ISV_FLAG}.tdcs.bussiness.task.TdcsEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |

> **下游云场景建立时·补这一段**：每个下游云的 EmployeeCoordServiceImpl
> （实现接口的具体类）有它自己的真业务规则（写哪些下游表 / 抛哪些异常 / 调哪些 helper），
> 这些规则属于**下游云的业务边界**·应在下游云独立场景的 02 / 06 md 里说明。
> 本协作场景只负责**协作链路**·不重复说明下游业务。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4 v5+ · 留口子设计 -->
---

## L7 真实现层 · 下游云高频锚点（2026-05-02 v3 升级·已建场景反向链接）

**下游云**: 人才发展云 (tdc) · `bizCloudId=<未实探>`

**真处理类**: `kd.${ISV_FLAG}.tdcs.bussiness.task.TdcsEmployeeCoordTask`

**状态**: ⏸ v3.x 增量再补 · 本轮 swc/wtc 优先 · tdc 菜单未探（应用清单已知 = 人才发展云 TDC）

**下游云锚点**：

> ⏸ v3.x 增量再补 · 本轮 swc/wtc 优先 · tdc 菜单未探（应用清单已知 = 人才发展云 TDC）

