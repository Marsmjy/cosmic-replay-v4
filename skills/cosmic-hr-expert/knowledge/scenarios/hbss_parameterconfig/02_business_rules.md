# hbss_parameterconfig · 业务规则

> **form**：`hbss_parameterconfig` · HR基础资料参数配置
> **生成时间**：2026-04-29
> **方法**：从反编译实物（4 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `app` | 所属应用 | `BasedataField` | `bos_devportal_bizapp` |
| `basedatafield` | 基础资料 | `BasedataField` | `hbp_entityobject` |
| `enablestatus` | 默认使用状态 | `ComboField` | — |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `BasedataConfigOp`

- **FQN**：`kd.hr.hbss.opplugin.web.BasedataConfigOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `BasedataConfigOpValidator`

**调用的 Service / Helper**（1 个）：`BasedataConfigOpValidator`

**实现的生命周期方法**：`onAddValidators`(L19)

### `HrParamsConfigOp`

- **FQN**：`kd.hr.hbss.opplugin.web.HrParamsConfigOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`HRBaseServiceHelper`

**实现的生命周期方法**：`onAddValidators`(L41)

## 三、FormPlugin 类业务规则（UI 联动）

### `AppConfigEditPlugin`

- **FQN**：`kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L90), `propertyChanged`(L63), `beforeF7Select`(L122)
- **读字段**（5）：`cloud.number`, `entryentity`, `modifyenable`, `number`, `paramtype`
- **调用的 Service**：`HRBaseServiceHelper`

### `HRConfigTreeListPlugin`

- **FQN**：`kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin`
- **继承**：`StandardTreeListPlugin`
- **生命周期方法**：`registerListener`(L101), `afterCreateNewData`(L133), `beforeDoOperation`(L106)
- **读字段**（3）：`basedata.number`, `basedatafield.number`, `refbasedata.number`
- **调用的 Service**：`HRParamConfigTreeServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hbss_parameterconfig/` 的 4 个反编译类 + `_shared/_standard_metadata/entity_metadata/hbss_parameterconfig.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

## chgaction 实证补充（HRBaseDataImportEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataImportEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

## chgaction 实证补充（HRCertCheckEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

## chgaction 实证补充（HRBaseUeEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseUeEdit`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseUeEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseUeEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin -->

## chgaction 实证补充（AppConfigEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.formplugin.web.config.AppConfigEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin -->

## chgaction 实证补充（HRConfigTreeListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.formplugin.web.config.HRConfigTreeListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

## chgaction 实证补充（HRDataBaseOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRDataBaseOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRDataBaseOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRDataBaseOp_0` | 数据量超过限制阈值%1$s，当前记录数：%2$s。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRDataBaseOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.opplugin.web.HrParamsConfigOp -->

## chgaction 实证补充（HrParamsConfigOp 跨类追踪聚合）

> FQN: `kd.hr.hbss.opplugin.web.HrParamsConfigOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.HrParamsConfigOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.opplugin.web.HrParamsConfigOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.opplugin.web.BasedataConfigOp -->

## chgaction 实证补充（BasedataConfigOp 跨类追踪聚合）

> FQN: `kd.hr.hbss.opplugin.web.BasedataConfigOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbss.opplugin.web.BasedataConfigOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbss.opplugin.web.BasedataConfigOp -->
