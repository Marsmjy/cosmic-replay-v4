# 员工变动协作 · 薪酬福利云 · 业务规则

> 数据源：BEC 订阅详情 (插件 FQN) + sch_job 调度作业 (锚点 jobnumber) + cosmic-class-resolver 反编译

## chgaction 协作链路（标品 v5+ 完整闭环）

本协作场景的执行链路（六层 FQN 真证据 · 详见下方"6 层 FQN 闭环证据"段）：

```
core_hr 上游事件（hpfs_chgrecord.effect 等）
  ↓ KDBizEvent
[L1 订阅入口] EventServicePlugin（壳·继承 AbstractEmployeeCoordSubService）
  ↓ pullJobNumber() 返回字面量
[L2 调度作业] sch_job（jobnumber）
  ↓ classname 字段（sch_job 详情）
[L3 真处理类] EmployeeCoordTask（壳）
  ↓ super.execute() 模板方法
[L4 模板基类] AbstractHrDataCoordTask · service.pullAndGenerate() ⭐
  ↓ matchCoordService()
[L5 业务接口] EmployeeCoordService
  ↓ ServiceFactory 注册的下游具体实现
[L6 下游真业务] EmployeeCoordServiceImpl（在下游云场景说明）
```

## 订阅 + 调度作业映射表

| 订阅插件类（短名）| extends 父类 | 上游事件 | 关联调度作业 jobnumber |
|---|---|---|---|
| `CandidateStopEventServicePlugin` | - | `swc.hcdm.hcdm_candsetsalact` | - |
| `CandidateStopEventServicePlugin` | - | `swc.hcdm.hcdm_candsetsalact` | - |
| `HpdiEmployeeCoordEventServicePlugin` | AbstractEmployeeCoordSubService | `swc.hpdi.hpdi_empcoordverifbill` | `hpdi_HpdiEmployeeCoordTask_SKDJ_S` |
| `HpdiEmployeeCoordEventServicePlugin` | AbstractEmployeeCoordSubService | `swc.hpdi.hpdi_empcoordverifbill` | `hpdi_HpdiEmployeeCoordTask_SKDJ_S` |
| `PerBankcardSynHirePersonEventServicePlugin` | - | `hpfs_chgrecord.effect` | - |

## 关联调度作业详情

### `hpdi_HpdiEmployeeCoordTask_SKDJ_S`

- 作业名: 员工变动_薪酬个税调度任务
- 类型: BIZ
- 应用: hpdi
- sch_job_id: `4YLWZL0X5MPH`
- ✅ **真处理类 FQN**: `kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask`
- 真处理类 extends: `kd.hr.hbp.business.coordination.impl.AbstractEmployeeCoordTask`
- 反编译产物: `_sdk_audit/_decompiled_deep/kd.swc.hpdi.business.coordination.HpdiEmployeeCoordTask/`

## 6 层 FQN 闭环证据（v5+ · 薪酬福利云 (swc)）

```
[L1 BEC 订阅]                 共 5 条 · 3 FQN
   ↓ 入口·壳类
[L2 订阅入口类]                32 类已反编译 · 多继承 AbstractEmployeeCoordSubService
   ↓ pullJobNumber() 字面量
[L3 调度作业 jobnumber]         共 1 个 · 锚点见上节"关联调度作业详情"
   ↓ classname 字段（sch_job 详情）
[L4 真处理类（壳）]              1 类 · 多继承 AbstractEmployeeCoordTask
   ↓ super.execute() 模板方法
[L5 共享抽象基类]               kd.hr.hbp.business.coordination.impl.AbstractHrDataCoordTask
                              · execute() 调 service.pullAndGenerate() · 真业务模板
   ↓ matchCoordService()
[L6 业务接口]                  kd.hr.hbp.business.coordination.api.EmployeeCoordService
                              · 各下游云独立实现·ISV 实现这个接口扩展
                              · ⭐ 具体实现的真业务规则在下游云场景里说明（见 06）
```

## 标品 ISV 扩展提示

- 这些订阅都是 `is_preset_subscription=True` 系统预置 · ISV **不要继承订阅类入口**
- ISV 项目要做"自定义协作"·应在 evt_subscription 元数据加自己的订阅条目（独立 service_implementation）
- 详见 07_ext_points.md

<!-- polished_form_scene_v2 · 2026-04-30 P1.4 v5+ -->
