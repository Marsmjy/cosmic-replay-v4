# 员工变动协作 · 工时假勤云 · 定制方案

## 协作链路全景

本场景的协作链路（v5+ 6 层闭环证据 · 详见 02_business_rules.md）：

```
[BEC 订阅] → [订阅入口类（壳）] → [调度作业] → [真处理类（壳）] → [抽象基类（模板）] → [业务接口]
```

## 标品已知协作映射

- 入口订阅 `WtcEmployeeCoordEventServicePlugin` → 调度作业 `wtp_WtcEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask`
- 入口订阅 `WtcEmployeeCoordEventServicePlugin` → 调度作业 `wtp_WtcEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask`

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
| 工时假勤云 (wtc) | `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |
| 工时假勤云 (wtc) | `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |

> **下游云场景建立时·补这一段**：每个下游云的 EmployeeCoordServiceImpl
> （实现接口的具体类）有它自己的真业务规则（写哪些下游表 / 抛哪些异常 / 调哪些 helper），
> 这些规则属于**下游云的业务边界**·应在下游云独立场景的 02 / 06 md 里说明。
> 本协作场景只负责**协作链路**·不重复说明下游业务。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4 v5+ · 留口子设计 -->
---

## L7 真实现层 · 下游云高频锚点（2026-05-02 v3 升级·已建场景反向链接）

**下游云**: 工时假勤云 (wtc) · `bizCloudId=13MN3ZU1+G54`

**真处理类**: `kd.wtc.wtp.business.newcoordination.WtcEmployeeCoordTask`

**状态**: ✅ 高频 10 场景已建（v5.1 跑通·4A/5B/1C）

### ✅ 已建独立场景（10 个 · 可点击查看完整 11md）

| 场景 | 名称 | 应用 | 评级 | sceneCategory | 协作关系 |
|---|---|---|:---:|---|---|
| [`wtp_attfilebase`](../wtp_attfilebase/) | 考勤档案主表 | 日常考勤 | ✅ A | main_form | ⭐ collab 直接锚点·考勤档案是协作链路写入的核心目标表（73 字段·业务核心） |
| [`wtp_attfilebasetab`](../wtp_attfilebasetab/) | 考勤档案 list | 日常考勤 | ✅ A | derived_view | 考勤档案 list view |
| [`wts_rosterview`](../wts_rosterview/) | 按人员排班 | 排班管理 | ✅ A | derived_view | 排班·变动后可能影响排班计划 |
| [`wtbd_shift`](../wtbd_shift/) | 班次 | 工时假勤规则 | ✅ A | main_form | 班次基础资料·间接关联（76 字段） |
| [`wtte_calresult`](../wtte_calresult/) | 考勤核算 | 考勤核算 | 🟡 B | main_form | 考勤核算结果·变动可能触发重算 |
| [`wtbd_holiday`](../wtbd_holiday/) | 假期类型 | 工时假勤规则 | 🔶 C | main_form | 假期类型基础资料 |
| [`wtbd_workschedule`](../wtbd_workschedule/) | 工作日程表 | 工时假勤规则 | 🟡 B | main_form | 工作日程 |
| [`wtabm_vaapplyself`](../wtabm_vaapplyself/) | 休假申请 | 假期管理 | 🟡 B | main_form | 员工休假申请·人员入离会触发 |
| [`wtom_overtimeapplybill`](../wtom_overtimeapplybill/) | 加班申请 | 加班管理 | 🟡 B | main_form | 加班申请单据 |
| [`wtam_busitripbill`](../wtam_busitripbill/) | 出差申请 | 日常考勤 | 🟡 B | main_form | 出差申请单据 |

> 完整应用菜单图谱：[`knowledge/_wtc_app_map.json`](../../_wtc_app_map.json)

