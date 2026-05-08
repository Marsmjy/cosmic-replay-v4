# hrcs_querydynsourcelist · 业务规则

> **form**：`hrcs_querydynsourcelist` · HR多实体查询配置列表
> **生成时间**：2026-04-29
> **方法**：从反编译实物（2 类）提取真业务规则 + standard md 字段必填约束

## 二、OP 类业务规则（反编译实证）

### `HRQueryListOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`BaseDataDeleteValidator`

**实现的生命周期方法**：`onAddValidators`(L29), `beginOperationTransaction`(L37), `afterExecuteOperationTransaction`(L43)

## 三、FormPlugin 类业务规则（UI 联动）

### `HRQueryTreeListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin`
- **继承**：`QueryTreeListPlugin`
- **生命周期方法**：`beforeDoOperation`(L199)
- **读字段**（14）：`app`, `cloud`, `datasourcetype`, `fappid`, `flocaleid`, `fname`, `id`, `index`
- **写字段**（1）：`name`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_querydynsourcelist/` 的 2 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_querydynsourcelist.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin -->

## chgaction 实证补充（HRQueryTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.query.HRQueryTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp -->

## chgaction 实证补充（HRQueryListOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.multientity.HRQueryListOp -->
