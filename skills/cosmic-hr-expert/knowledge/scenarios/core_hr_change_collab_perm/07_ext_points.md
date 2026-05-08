# 员工变动协作 · 权限同步 · ISV 扩展点

> 数据源：cosmic-class-resolver 已反编译的 32 个标品订阅类 · 真证据已锚点到 `knowledge/_sdk_audit/_decompiled_deep/`

## ISV 写下游协作订阅类的指南

### 推荐继承基类

ISV 不应直接继承标品订阅类入口（如 `HpdiEmployeeCoordEventServicePlugin`），应继承其抽象基类：

| 基类 | 来源 | 用途 |
|---|---|---|
| `kd.hr.hbp.business.coordination.impl.AbstractEmployeeCoordSubService` | hbp · 协作公共基类 | 薪酬/考勤/人才发展协作通用 |
| `kd.bos.event.IEventServicePlugin`（接口）| bos · 平台事件接口 | 任何 BEC 订阅 |

### 实战路径

1. 在 ISV 应用里建 `evt_subscription` 元数据条目
2. event_number = 上游事件号（如 `hpfs_chgrecord.effect`）
3. service_implementation = 你的 plugin FQN（继承上述基类）
4. 实现 `handleEvent(KDBizEvent)` / `pullJobNumber()` / `sceneNumber()` 等方法

## 标品参考（真证据）

本场景含 8 个标品订阅类 + 1 个调度作业锚点：

- `kd.opmc.epa.business.coordination.EpaEmployeeCoordEventServicePlugin` (extends kd.hr.hbp.business.coordination.impl.AbstractEmployeeCoordSubService)
  - 派单 → `epa_EpaEmployeeCoordTask_SKDJ_S`
- `kd.hrmp.hrpi.business.application.event.EmpSyncEventServicePlugin` (extends None)
- `kd.hrmp.hrpi.business.application.event.EmpRollbackEventServicePlugin` (extends None)
- `kd.hr.hrcs.bussiness.service.perm.dyna.event.DynaPermEventPlugin` (extends None)
- `kd.hrmp.hrpi.business.application.event.UpdateEmpPosOrgRelVidEvent` (extends None)
- `kd.hrmp.hrpi.business.application.event.UpdateEmpPosOrgRelVidEvent` (extends None)
- `kd.hrmp.hrpi.business.application.event.SyncBosUserEvent` (extends None)
- `kd.sihc.soecadm.business.application.event.CoordinationEventServicePlugin` (extends None)
- `kd.hrmp.hrpi.business.application.event.SyncBosUserEvent` (extends None)
- `kd.sihc.soefam.business.application.event.UpdateFilPersonEventServicePlugin` (extends None)

详细类清单见 `_bec_subscriptions.json`。每个 FQN 对应 `_sdk_audit/_decompiled_deep/<fqn>/extension_guide.md`。

<!-- polished_form_scene_v2 · 2026-04-30 P1.4-A -->
