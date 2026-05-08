# 员工变动协作 · 薪酬福利云 · 定制方案

## 协作链路全景

本场景的协作链路（v5+ 6 层闭环证据 · 详见 02_business_rules.md）：

```
[BEC 订阅] → [订阅入口类（壳）] → [调度作业] → [真处理类（壳）] → [抽象基类（模板）] → [业务接口]
```

## 标品已知协作映射

- 入口订阅 `HpdiEmployeeCoordEventServicePlugin` → 调度作业 `hpdi_HpdiEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask`
- 入口订阅 `HpdiEmployeeCoordEventServicePlugin` → 调度作业 `hpdi_HpdiEmployeeCoordTask_SKDJ_S` → 真处理类 `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask`

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
| 薪酬福利云 (swc) | `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |
| 薪酬福利云 (swc) | `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask` | 实现 EmployeeCoordService 接口 (待下游云场景补) | <TODO 下游云独立场景> |

> **下游云场景建立时·补这一段**：每个下游云的 EmployeeCoordServiceImpl
> （实现接口的具体类）有它自己的真业务规则（写哪些下游表 / 抛哪些异常 / 调哪些 helper），
> 这些规则属于**下游云的业务边界**·应在下游云独立场景的 02 / 06 md 里说明。
> 本协作场景只负责**协作链路**·不重复说明下游业务。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4 v5+ · 留口子设计 -->
---

## L7 真实现层 · 下游云高频锚点（2026-05-02 v3 升级·已建场景反向链接）

**下游云**: 薪酬福利云 (swc) · `bizCloudId=/U+QDTL900//`

**真处理类**: `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask`

**状态**: ✅ 高频 10 场景已建（v5.1 跑通·8A/2非A）

### ✅ 已建独立场景（10 个 · 可点击查看完整 11md）

| 场景 | 名称 | 应用 | 评级 | sceneCategory | 协作关系 |
|---|---|---|:---:|---|---|
| [`hsas_personchange`](../hsas_personchange/) | 人员变动记录 | 薪资核算 | 🔶 C | main_form | ⭐ collab 直接锚点·人员变动 → 触发本协作链路 |
| [`hsas_salaryfilelisttab`](../hsas_salaryfilelisttab/) | 薪资档案 | 薪资核算 | ✅ A | derived_view | 薪资主档案·人员变动后由本协作链路写入薪资档案 |
| [`hsas_calplatformcard`](../hsas_calplatformcard/) | 专员工作台 | 薪资核算 | ✅ A | derived_view | 薪资专员处理变动事件的入口 |
| [`hsbs_employeequerylist`](../hsbs_employeequerylist/) | 计薪人员 | 薪资核算 | ✅ A | derived_view | 本协作链路写入算薪范围的目标 |
| [`hsbs_salaryitem`](../hsbs_salaryitem/) | 薪酬项目 | 薪资核算 | ✅ A | main_form | 薪酬项·变动可能触发薪酬项重算 |
| [`hsas_retroevent`](../hsas_retroevent/) | 薪资回溯事件 | 薪资核算 | ✅ A | chgrecord_view | 回溯事件触发器·人员变动是回溯源之一 |
| [`hsas_bizdatalist`](../hsas_bizdatalist/) | 前端业务数据 | 薪资核算 | ✅ A | derived_view | 前端业务数据(纵表)·算薪输入 |
| [`hsas_employeetaxcnlist`](../hsas_employeetaxcnlist/) | 员工个税信息 | 薪资核算 | ✅ A | derived_view | 中国个税·人员变动同步个税信息 |
| [`hcdm_salarystandard`](../hcdm_salarystandard/) | 薪酬标准表 | 薪酬管理 | ✅ A | main_form | 薪酬设计·变动后可能触发标准切换 |
| [`hsbs_calperiodtype`](../hsbs_calperiodtype/) | 薪资期间 | 薪酬基础服务 | 🟡 B | main_form | 薪资期间配置·间接关联 |

> 完整应用菜单图谱：[`knowledge/_swc_app_map.json`](../../_swc_app_map.json)

