# 业务规则 · 标准岗位维护（hbpm_stposition）

> **状态**: 🟢 verified · 基于 6 反编译类 + plugin_registry 实证
> **confidence**: verified
> **审计时间**: 2026-04-27
> **来源**: StandardPositionSaveOp / StandardPositionDisableOp / StandardPositionChangeOp + HisUniqueValidateOp + HisModelOPCommonPlugin 反编译

---

## 0. 规则来源说明

本场景 **listRules = 0 条**（OpenAPI 实抓），所有业务规则全部通过操作插件 `onAddValidators` 注册 Validator 实现，没有走 formRule 配置。

---

## 1. 隐性规则：HisUniqueValidateOp · boid 级唯一性校验

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-01 |
| 实现类 | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` extends `HRDataBaseOp` |
| 生命周期 | `onAddValidators` |
| 适用操作 | save / change / confirmchange |
| 校验维度 | boid 级（跨所有历史版本） |

**规则内容**：在整个时序版本链中，标准岗位的唯一性约束在 boid 维度上生效，而非 id 维度。具体校验逻辑由 HisUniqueValidateOp 注册 Validator，防止同一业务对象在同一生效区间内产生重叠版本。

**ISV 注意**：
- 不要绕过 HisUniqueValidateOp · 它是时序资料完整性的守门人
- ISV 自建唯一性校验时，同样要在 boid 维度做，不能只检查 id

---

## 2. StandardPositionSaveOp 校验链

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-02 |
| 实现类 | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp` |
| 生命周期 | `onAddValidators` + `beforeExecuteOperationTransaction` |
| 适用操作 | `save` |

**onAddValidators**：方法体为空（反编译实证），说明保存前的校验完全由父链（HisModelOPCommonPlugin + HisUniqueValidateOp）负责，StandardPositionSaveOp 不额外注册校验器。

**beforeExecuteOperationTransaction**（实证逻辑）：
```
super.beforeExecuteOperationTransaction(args);  // 调父类 StandardPositionMsgHandleOp
DynamicObject[] positions = args.getDataEntities();
if (positions.length == 0) return;
positions.stream().forEach(position -> position.set("isstandardpos", "1"));
```

**规则含义**：保存时强制将所有被保存的岗位记录标记为 `isstandardpos="1"`，确保通过本场景保存的数据都是标准岗位，不会与 hbpm_positionhr（普通岗位，isstandardpos="0"）混淆。

---

## 3. StandardPositionDisableOp 禁用校验

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-03 |
| 实现类 | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp` |
| 生命周期 | `onAddValidators` |
| 适用操作 | `disable` |

**反编译实证**：onAddValidators 方法体为空，实际校验由父类 `StandardPositionMsgHandleOp` 的 onAddValidators（若有）或由平台默认校验承担。

**业务含义**：禁用操作的拦截逻辑（如：检查该标准岗位是否仍有在职人员引用）可能由父类 `StandardPositionMsgHandleOp` 实现，或依赖 `HRBaseDataStatusOp` 平台通用禁用校验。

---

## 4. StandardPositionChangeOp 变更校验

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-04 |
| 实现类 | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp` |
| 生命周期 | `onAddValidators` |
| 适用操作 | `change` / `confirmchange` |

**反编译实证**：onAddValidators 方法体为空，变更前校验由 HisModelOPCommonPlugin（时序版本合法性）+ HisUniqueValidateOp（boid 唯一性）负责。

**业务含义**：变更操作产生新版本时，时序区间（bsed/bsled）不能与同 boid 的现有版本重叠（由 HisUniqueValidateOp 保证）。

---

## 5. StandardPositionMsgHandleOp 父类规则（推断）

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-05 |
| 实现类 | `kd.hrmp.hbpm.opplugin.web.position.StandardPositionMsgHandleOp`（未反编译，推断） |
| 地位 | StandardPositionSaveOp / DisableOp / EnableOp / ChangeOp / AuditOp 的父类 |

**从子类推断的父类行为**：
- 类名含 `MsgHandle`，强烈暗示父类负责消息/事件发送逻辑
- 所有子类 OP 都调用 `super.beforeExecuteOperationTransaction(args)`（StandardPositionSaveOp 实证）
- 父类可能在 `afterExecuteOperationTransaction` 阶段发布 BEC 业务事件（标准岗位变更通知）
- **ISV 关键认知**：不要继承 StandardPositionMsgHandleOp，否则可能重复触发消息发送

**关于 BEC**：由于父类未反编译，无法确认是否发 BEC。保守结论：本场景的 change / save 操作**可能通过 StandardPositionMsgHandleOp 发布消息事件**，具体事件号需在【开发平台 → 业务事件管理】查询。

---

## 6. 前端联动隐性规则（来自 StandardPositionEdit）

| 规则 ID | 触发条件 | 行为 | 实现 |
|---|---|---|---|
| INV-SP-06 | enable=10（禁用状态） | 打开时自动切换为 VIEW 状态，仅显示修改按钮（bar_modify）| StandardPositionEdit.beforeBindData |
| INV-SP-07 | 新增时 | 清空 org 字段（防止带入历史值）| StandardPositionEdit.afterBindData |
| INV-SP-08 | status=C 或 B（已提交/审核中） | adminorg 字段禁止编辑 | StandardPositionEdit.afterBindData |
| INV-SP-09 | 变更操作无信息变化 | 拦截保存/变更操作，提示"无信息变更，请确认" | StandardPositionEdit.beforeDoOperation |
| INV-SP-10 | adminorg 字段变更 | 自动同步 org 字段（adminorg.org → org）| StandardPositionEdit.propertyChanged |
| INV-SP-11 | job / org / adminorg 变更 | 若编码规则存在则自动重新生成 number 编码 | StandardPositionEdit.propertyChanged + changeNumber |
| INV-SP-12 | afterImportData | bsed 为空时自动填入当天日期（HRDateTimeUtils.truncateDate）| StandardPositionEdit.afterImportData |

---

## 7. 列表排序规则（StandardPositionListPlugin）

| 属性 | 值 |
|---|---|
| 规则 ID | INV-SP-13 |
| 实现类 | `StandardPositionListPlugin.setFilter` |

**反编译实证**：
```java
setFilterEvent.setOrderBy("index asc,number asc");
```
标准岗位列表强制按 `index（排序号）升序` 优先，再按 `number（编码）升序` 排列。ISV 自定义排序须小心不要覆盖此规则。

---

## 8. 时序资料版本管理隐性规则（HisModelOPCommonPlugin）

| 规则 ID | 规则 |
|---|---|
| INV-SP-14 | 新建操作：boid = id（第一版本 boid 和 id 相同）· 由 HisModelOPCommonPlugin 框架设置 |
| INV-SP-15 | 变更操作：新版本的 sourcevid = 旧版本的 id（历史链表指针）|
| INV-SP-16 | 变更后：旧版本 iscurrentversion = false · 新版本 iscurrentversion = true |
| INV-SP-17 | hisversion 在同一 boid 链中递增（1, 2, 3...）|
| INV-SP-18 | firstbsed 保持不变（记录该 boid 最早的生效日期，所有版本共享）|

---

## 9. 规则完整性索引

| 规则 ID | 类别 | 来源 | 置信度 |
|---|---|---|---|
| INV-SP-01 | 时序唯一性校验 | HisUniqueValidateOp 反编译实证 | verified |
| INV-SP-02 | isstandardpos 强制写入 | StandardPositionSaveOp 反编译实证 | verified |
| INV-SP-03 | 禁用前置校验（父类承担）| StandardPositionDisableOp 反编译实证 | verified |
| INV-SP-04 | 变更前时序合法性 | StandardPositionChangeOp 反编译实证 | verified |
| INV-SP-05 | 消息发送（父类推断）| StandardPositionMsgHandleOp 类名推断 | likely |
| INV-SP-06 ~ 12 | 前端联动规则 | StandardPositionEdit 反编译实证 | verified |
| INV-SP-13 | 列表排序 | StandardPositionListPlugin 反编译实证 | verified |
| INV-SP-14 ~ 18 | 时序版本管理 | HisModelOPCommonPlugin 框架规范 | verified |

---

## 10. 关联文档

- `06_customization_solutions.md` · CS-01 ~ CS-05 具体实现
- `07_ext_points.md` · 禁继承清单（StandardPositionMsgHandleOp 在列）
- `knowledge/_shared/platform_rules.json` · PR-008 / PR-009 时序规范

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

## chgaction 实证补充（HisModelFormCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin/`

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

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisModelFormCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionEdit -->

## chgaction 实证补充（StandardPositionEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionEdit`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

## chgaction 实证补充（HisModelListCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelListCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

## chgaction 实证补充（HisModelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

## chgaction 实证补充（HisModelFilterPanelListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

## chgaction 实证补充（HisModelFilterPanelF7ListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.list.HisModelFilterPanelF7ListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionListPlugin -->

## chgaction 实证补充（StandardPositionListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.standardposition.StandardPositionListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

## chgaction 实证补充（HRBaseDataStatusOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

## chgaction 实证补充（HRBaseDataEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

## chgaction 实证补充（HisModelOPCommonPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp -->

## chgaction 实证补充（StandardPositionSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionAuditOp -->

## chgaction 实证补充（StandardPositionAuditOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.StandardPositionAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.StandardPositionAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp -->

## chgaction 实证补充（StandardPositionDisableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionDisableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionEnableOp -->

## chgaction 实证补充（StandardPositionEnableOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.StandardPositionEnableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.StandardPositionEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp -->

## chgaction 实证补充（StandardPositionChangeOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.StandardPositionChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->

## chgaction 实证补充（HisBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HisModelCommonService_1` | “%s”的历史模型实体配置“模式选择”未配置，请先完成配置。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.form.HisBaseDataF7FastFilter -->
