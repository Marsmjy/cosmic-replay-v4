# hrcs_esignspmgr · 业务规则

> **form**：`hrcs_esignspmgr` · 电子签服务商管理
> **生成时间**：2026-04-29
> **方法**：从反编译实物（4 类）提取真业务规则 + standard md 字段必填约束

## 二、OP 类业务规则（反编译实证）

### `ESignSPMgrOp`

- **FQN**：`kd.hr.hrcs.opplugin.web.esign.ESignSPMgrOp`
- **继承**：`HRDataBaseOp`

**实现的生命周期方法**：`beginOperationTransaction`(L39), `afterExecuteOperationTransaction`(L79)

## 三、FormPlugin 类业务规则（UI 联动）

### `ESignSPMgrCloseEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.esign.ESignSPMgrCloseEdit`
- **继承**：`HRCloseCheckFormPlugin`
- **读字段**（3）：`bdesignappcfg`, `id`, `number`

### `ESignSPMgrEdit`

- **FQN**：`kd.hr.hrcs.formplugin.web.esign.ESignSPMgrEdit`
- **继承**：`HRDataBaseEdit`
- **生命周期方法**：`registerListener`(L521), `afterCreateNewData`(L132), `beforeDoOperation`(L170), `afterDoOperation`(L208), `itemClick`(L531)
- **读字段**（10）：`appid`, `bdesignappcfg`, `bdesignappcfg_id`, `entryentity1`, `esignsp`, `id`, `interface`, `mustflag`

### `ESignSPMgrListPlugin`

- **FQN**：`kd.hr.hrcs.formplugin.web.esign.ESignSPMgrListPlugin`
- **继承**：`HRDataBaseList`
- **生命周期方法**：`registerListener`(L114), `beforeF7Select`(L120), `beforeDoOperation`(L161)
- **读字段**（5）：`enable`, `entryentity`, `entryentity1`, `id`, `number`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hrcs_esignspmgr/` 的 4 个反编译类 + `_shared/_standard_metadata/entity_metadata/hrcs_esignspmgr.md`

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

## chgaction 实证补充（HRBaseDataTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit`
> 跨类追踪: 7 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

## chgaction 实证补充（HRHiesButtonSwitchPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRHiesButtonSwitchPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrEdit -->

## chgaction 实证补充（ESignSPMgrEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.esign.ESignSPMgrEdit`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.esign.ESignSPMgrEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrCloseEdit -->

## chgaction 实证补充（ESignSPMgrCloseEdit 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.esign.ESignSPMgrCloseEdit`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.esign.ESignSPMgrCloseEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrCloseEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

## chgaction 实证补充（HRBaseDataTplList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBaseDataTplList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBaseDataTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBaseDataTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

## chgaction 实证补充（HRBasedataLogList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRBasedataLogList`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRBasedataLogList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRBasedataLogList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrListPlugin -->

## chgaction 实证补充（ESignSPMgrListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hrcs.formplugin.web.esign.ESignSPMgrListPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.formplugin.web.esign.ESignSPMgrListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.formplugin.web.esign.ESignSPMgrListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.esign.ESignSPMgrOp -->

## chgaction 实证补充（ESignSPMgrOp 跨类追踪聚合）

> FQN: `kd.hr.hrcs.opplugin.web.esign.ESignSPMgrOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hrcs.opplugin.web.esign.ESignSPMgrOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hrcs.opplugin.web.esign.ESignSPMgrOp -->
