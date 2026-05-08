# haos_adminorgdetail · 业务规则

> **form**：`haos_adminorgdetail` · 组织快速维护
> **生成时间**：2026-04-29
> **方法**：从反编译实物（0 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 行政组织编码 | `TextField` | — |
| `name` | 行政组织名称 | `MuliLangTextField` | — |
| `enable` | 业务状态 | `BillStatusField` | — |
| `adminorgtype` | 行政组织类型 | `BasedataField` | `haos_adminorgtype` |
| `org` | 组织体系管理组织 | `OrgField` | `bos_org` |
| `otclassify` | 组织分类 | `BasedataField` | `haos_otclassify` |
| `changescene` | 变动场景 | `BasedataField` | `haos_changescene` |
| `coopreltype` | 协作类型 | `BasedataField` | `haos_teamcoopreltype` |
| `cooporgteam` | 协作组织 | `HRAdminOrgField` | `haos_adminorghrf7` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/haos_adminorgdetail/` 的 0 个反编译类 + `_shared/_standard_metadata/entity_metadata/haos_adminorgdetail.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

## chgaction 实证补充（NewAdminorgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin`
> 跨类追踪: 25 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("NewAdminorgDetailEditPlugin", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.NewAdminorgDetailEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

## chgaction 实证补充（AdminOrgPageRightDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin`
> 跨类追踪: 6 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgPageRightDynamicPlugin", e.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgPageRightDynamicPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

## chgaction 实证补充（AdminOrgDetailEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("AdminOrgDetailEditPlugin", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailEditPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

## chgaction 实证补充（OrgDetailList 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.OrgDetailList`
> 跨类追踪: 9 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.OrgDetailList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("OrgDetailList", parseException.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.OrgDetailList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

## chgaction 实证补充（AdminOrgDetailBUListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailBUListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

## chgaction 实证补充（AdminOrgDetailListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin`
> 跨类追踪: 26 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | new ErrorCode("closedCallBack", exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDetailListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

## chgaction 实证补充（AdminOrgDisableAndIncludeFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.formplugin.web.adminorg.AdminOrgDisableAndIncludeFilterListPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

## chgaction 实证补充（HRBaseOriginalOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

## chgaction 实证补充（HisUniqueValidateOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

## chgaction 实证补充（AdminOrgFastSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

## chgaction 实证补充（AdminOrgFastAuditOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastAuditOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

## chgaction 实证补充（HRBaseDataLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

## chgaction 实证补充（AdminOrgFastParentChangeOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastParentChangeOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

## chgaction 实证补充（AdminOrgInitSaveOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.adminorg.init.AdminOrgInitSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

## chgaction 实证补充（AdminOrgFastDisableOrgOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | msgBuilder.toString() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastDisableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

## chgaction 实证补充（AdminOrgFastEnableOrgOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | msgBuilder.toString() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgFastEnableOrgOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

## chgaction 实证补充（AdminOrgRootResetOp 跨类追踪聚合）

> FQN: `kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | this.reset_one_msg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.haos.opplugin.web.orgfast.AdminOrgRootResetOp -->

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
