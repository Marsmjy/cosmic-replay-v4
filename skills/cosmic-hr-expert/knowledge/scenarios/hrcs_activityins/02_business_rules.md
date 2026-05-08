# hrcs_activityins · 业务规则

> **form**：`hrcs_activityins` · 活动任务实例
> **生成时间**：2026-04-29
> **方法**：从反编译实物（3 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `applier` | 发起人 | `UserField` | `bos_user` |
| `bizbillid` | 主业务单据ID值 | `TextField` | — |
| `biznum` | 主业务单据编号 | `TextField` | — |
| `bizkey` | 主业务单据标识 | `TextField` | — |
| `activity` | 活动 | `BasedataField` | `hrcs_activity` |
| `actscheme` | 活动方案 | `BasedataField` | `hrcs_activityscheme` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `ActivityInsAssigntoOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `ActivityInsAssigntoValidator`

**调用的 Service / Helper**（1 个）：`ActivityInsAssigntoValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `ActivityInsSaveOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `ActivityInsSaveValidator`

**调用的 Service / Helper**（2 个）：`ActivityInsSaveValidator`, `HRBaseServiceHelper`

**实现的生命周期方法**：`onAddValidators`(L63), `beforeExecuteOperationTransaction`(L67), `beginOperationTransaction`(L115)

## 三、FormPlugin 类业务规则（UI 联动）

### `ActivityInstancePlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin`
- **继承**：`AbstractListPlugin`
- **生命周期方法**：`beforeDoOperation`(L125), `afterDoOperation`(L154)
- **读字段**（6）：`bindbizbillid`, `bindbizkey`, `bindinglayoutid`, `bizbillid`, `bizkey`, `taskswitch`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_activityins/` 的 3 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_activityins.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin -->

## chgaction 实证补充（ActivityInstancePlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.activity.ActivityInstancePlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp -->

## chgaction 实证补充（ActivityInsSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityInsSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp -->

## chgaction 实证补充（ActivityInsAssigntoOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.activity.ActivityInsAssigntoOp -->
