# hbpm_positionhis · 业务规则

> **form**：`hbpm_positionhis` · 岗位历史查询
> **生成时间**：2026-04-29
> **方法**：从反编译实物（0 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 岗位编码 | `TextField` | — |
| `name` | 岗位名称 | `MuliLangTextField` | — |
| `bsed` | 生效日期 | `DateField` | — |
| `adminorg` | 行政组织 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `reporttype` | 协作类型 | `BasedataField` | `hbpm_reportcoreltype` |
| `targetpos` | 协作岗位 | `HRPositionField` | `hbpm_positionhrf7` |
| `applicableorg` | 行政组织名称 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hbpm_positionhis/` 的 0 个反编译类 + `_shared/_standard_metadata/entity_metadata/hbpm_positionhis.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

## chgaction 实证补充（HRRelatePageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.rp.HRRelatePageRightDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

## chgaction 实证补充（PositionEdit 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionEdit`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

## chgaction 实证补充（PositionPageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionPageRightDynamicPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

## chgaction 实证补充（PositionList 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionList`
> 跨类追踪: 10 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

## chgaction 实证补充（AbstractBUListPlugin 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.AbstractBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

## chgaction 实证补充（OrgDisableAndIncludeFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.OrgDisableAndIncludeFilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionHistList -->

## chgaction 实证补充（PositionHistList 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.formplugin.web.position.PositionHistList`
> 跨类追踪: 20 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.formplugin.web.position.PositionHistList/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOrUpdate | positionFutureLogRepository | kd.hrmp.hbpm.business.domain.position.service.impl.PositionF |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `BosPositionServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.formplugin.web.position.PositionHistList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

## chgaction 实证补充（HisModelMobileListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.history.mobile.HisModelMobileListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

## chgaction 实证补充（PositionHisSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hbpm.opplugin.web.position.PositionHisSaveOp -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->

## chgaction 实证补充（PositionBaseDataF7FastFilter 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.controller.PositionBaseDataF7FastFilter -->
