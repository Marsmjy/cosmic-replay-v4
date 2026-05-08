# 员工变动协作 · 权限同步 · 业务规则

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
| `EpaEmployeeCoordEventServicePlugin` | AbstractEmployeeCoordSubService | `opmc.epa.epa_empcoordverifbill` | `epa_EpaEmployeeCoordTask_SKDJ_S` |
| `EmpSyncEventServicePlugin` | - | `hpfs_chgrecord.effect` | - |
| `EmpRollbackEventServicePlugin` | - | `hpfs_chgrecord.rollback` | - |
| `DynaPermEventPlugin` | - | `hpfs_chgrecord.effect` | - |
| `UpdateEmpPosOrgRelVidEvent` | - | `hpfs_chgrecord.effect` | - |
| `UpdateEmpPosOrgRelVidEvent` | - | `hpfs_chgrecord.rollback` | - |
| `SyncBosUserEvent` | - | `hpfs_chgrecord.effect` | - |
| `CoordinationEventServicePlugin` | - | `hpfs_chgrecord.effect` | - |
| `SyncBosUserEvent` | - | `hpfs_chgrecord.rollback` | - |
| `UpdateFilPersonEventServicePlugin` | - | `hpfs_chgrecord.effect` | - |

## 关联调度作业详情

### `epa_EpaEmployeeCoordTask_SKDJ_S`

- 作业名: -
- 类型: -
- 应用: -
- sch_job_id: `-`
- ✅ **真处理类 FQN**: `kd.opmc.epa.business.coordination.EpaEmployeeCoordTask`
- 真处理类 extends: `kd.hr.hbp.business.coordination.impl.AbstractEmployeeCoordTask`
- 反编译产物: `_sdk_audit/_decompiled_deep/kd.opmc.epa.business.coordination.EpaEmployeeCoordTask/`

## 6 层 FQN 闭环证据（v5+ · HR 基础服务云权限子域 + 通用）

```
[L1 BEC 订阅]                 共 10 条 · 8 FQN
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
