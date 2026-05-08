# hies_diaetplconf · 业务规则

> **form**：`hies_diaetplconf` · 导入导出模板配置
> **生成时间**：2026-04-29
> **方法**：从反编译实物（8 类）提取真业务规则 + standard md 字段必填约束

## 一、字段级必填约束（元数据规则）

| 字段 | 中文名 | 类型 | 引用 |
|---|---|---|---|
| `number` | 编码 | `TextField` | — |
| `name` | 模板名称 | `MuliLangTextField` | — |
| `entitytype` | 实体类型 | `ComboField` | — |
| `tmpltype` | 模板类型 | `ComboField` | — |
| `entity` | 实体 | `BasedataField` | `hbp_entityobject` |
| `allocationpolicy` | 分配策略 | `ComboField` | — |
| `orgrole` | 角色编码 | `BasedataField` | `perm_role` |
| `orgrolenumber` | 行政组织编码 | `HRAdminOrgField` | `haos_adminorghrf7` |
| `sheetreadrow` | 数据导入行 | `IntegerField` | — |
| `queryentity` | 实体 | `BasedataField` | `hbp_entityobject` |

> 这些字段在 standard 元数据里标 `required=true` · 标品 MustInputValidation 自动校验 · ISV 不可改其必填属性

## 二、OP 类业务规则（反编译实证）

### `TemplateDeleteOp`

- **FQN**：`kd.hr.hies.opplugin.web.TemplateDeleteOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`TemplateDeleteValidator`

**实现的生命周期方法**：`onAddValidators`(L36)

### `TemplateEnableOp`

- **FQN**：`kd.hr.hies.opplugin.web.TemplateEnableOp`
- **继承**：`HRDataBaseOp`

**调用的 Service / Helper**（1 个）：`TemplateEnableValidator`

**实现的生命周期方法**：`onAddValidators`(L37)

### `TemplateSaveOp`

- **FQN**：`kd.hr.hies.opplugin.web.TemplateSaveOp`
- **继承**：`HRDataBaseOp`

**触发的 Validator**（save/delete 前置校验）：
- `TemplateSaveValidator`

**调用的 Service / Helper**（2 个）：`HRBaseServiceHelper`, `TemplateSaveValidator`

**实现的生命周期方法**：`onAddValidators`(L63), `beforeExecuteOperationTransaction`(L67), `beginOperationTransaction`(L111), `afterExecuteOperationTransaction`(L115)

## 三、FormPlugin 类业务规则（UI 联动）

### `TemplateAddSubEntityPlugin`

- **FQN**：`kd.hr.hies.formplugin.TemplateAddSubEntityPlugin`
- **继承**：`HRBaseDataCommonEdit`
- **生命周期方法**：`registerListener`(L189), `afterBindData`(L215), `afterCreateNewData`(L532), `propertyChanged`(L593), `beforeF7Select`(L536), `afterDoOperation`(L406), `click`(L431), `itemClick`(L392)
- **读字段**（6）：`id`, `name`, `number`, `rentity`, `rentity.name`, `rentity.number`

### `TemplateAssignRolePlugin`

- **FQN**：`kd.hr.hies.formplugin.TemplateAssignRolePlugin`
- **继承**：`HRDataBaseEdit`

### `TemplateAssignUserPlugin`

- **FQN**：`kd.hr.hies.formplugin.TemplateAssignUserPlugin`
- **继承**：`HRBaseDataCommonEdit`
- **生命周期方法**：`propertyChanged`(L218), `beforeF7Select`(L221)
- **读字段**（5）：`hrpi_empposorgrel.adminorg.name`, `hrpi_empposorgrel.company.name`, `hrpi_empposorgrel.position.name`, `hrpi_person.id`, `id`
- **写字段**（3）：`company`, `position`, `userorg`
- **调用的 Service**：`HRBaseServiceHelper`

### `TemplateConfPlugin`

- **FQN**：`kd.hr.impt.formplugin.TemplateConfPlugin`
- **继承**：`HRBaseDataCommonEdit`
- **生命周期方法**：`preOpenForm`(L353), `registerListener`(L336), `afterBindData`(L781), `afterCreateNewData`(L481), `propertyChanged`(L1343), `beforeF7Select`(L851), `beforeDoOperation`(L860), `afterDoOperation`(L1873), `click`(L1766)
- **读字段**（15）：`childentity`, `deffieldnumber`, `deffieldvalprop`, `defvalprop`, `entity.id`, `entitytype`, `fieldname`, `fieldnumber`
- **写字段**（1）：`enable`
- **调用的 Service**：`HRBaseServiceHelper`, `InteService`

### `TemplateTreeListEdit`

- **FQN**：`kd.hr.hies.formplugin.TemplateTreeListEdit`
- **继承**：`TreeListBizAppsPlugin`
- **生命周期方法**：`beforeDoOperation`(L182), `afterDoOperation`(L492), `itemClick`(L172)
- **读字段**（9）：`bizappid`, `bizappid_id`, `bizcloud`, `cloud`, `id`, `issyspreset`, `masterid`, `number`
- **调用的 Service**：`HRBaseServiceHelper`

## 四、共性约束（HR 标品共用）

- save 前 `MustInput` 元数据校验自动跑
- 标品 OP 父类（如 HRDataBaseOp）注入通用 Validator 链
- ISV 在自己 OP 里调 `super.onAddValidators(args)` 不会破坏标品链

---

**精修元数据**：
- 生成器：`scripts/polish_form_scene.py`
- 数据源：`_sdk_audit/_decompiled/scenarios/hies_diaetplconf/` 的 8 个反编译类 + `_shared/_standard_metadata/entity_metadata/hies_diaetplconf.md`

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.impt.formplugin.TemplateConfPlugin -->

## chgaction 实证补充（TemplateConfPlugin 跨类追踪聚合）

> FQN: `kd.hr.impt.formplugin.TemplateConfPlugin`
> 跨类追踪: 33 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.impt.formplugin.TemplateConfPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `inte_enabledlanguage` | updateOne | HRBaseServiceHelper | <self> (depth=0) |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `sheetHandler.getSheetEntityMap`
- `sheetHandler.getErrMsgs`
- `sheetHandler.getTemplateValidater`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.impt.formplugin.TemplateConfPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAddSubEntityPlugin -->

## chgaction 实证补充（TemplateAddSubEntityPlugin 跨类追踪聚合）

> FQN: `kd.hr.hies.formplugin.TemplateAddSubEntityPlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.formplugin.TemplateAddSubEntityPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not suppot level3 headers" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAddSubEntityPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAssignUserPlugin -->

## chgaction 实证补充（TemplateAssignUserPlugin 跨类追踪聚合）

> FQN: `kd.hr.hies.formplugin.TemplateAssignUserPlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.formplugin.TemplateAssignUserPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not suppot level3 headers" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAssignUserPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAssignRolePlugin -->

## chgaction 实证补充（TemplateAssignRolePlugin 跨类追踪聚合）

> FQN: `kd.hr.hies.formplugin.TemplateAssignRolePlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.formplugin.TemplateAssignRolePlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not suppot level3 headers" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateAssignRolePlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateTreeListEdit -->

## chgaction 实证补充（TemplateTreeListEdit 跨类追踪聚合）

> FQN: `kd.hr.hies.formplugin.TemplateTreeListEdit`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.formplugin.TemplateTreeListEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not suppot level3 headers" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.formplugin.TemplateTreeListEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.opplugin.web.TemplateSaveOp -->

## chgaction 实证补充（TemplateSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hies.opplugin.web.TemplateSaveOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.opplugin.web.TemplateSaveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hies_fieldshowname` | deleteByFilter | HRBaseServiceHelper | <self> (depth=0) |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "not suppot level3 headers" |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.opplugin.web.TemplateSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.opplugin.web.TemplateEnableOp -->

## chgaction 实证补充（TemplateEnableOp 跨类追踪聚合）

> FQN: `kd.hr.hies.opplugin.web.TemplateEnableOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hies.opplugin.web.TemplateEnableOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hies.opplugin.web.TemplateEnableOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.impt.formplugin.DiaeNewImportTemplate -->

## chgaction 实证补充（DiaeNewImportTemplate 跨类追踪聚合）

> FQN: `kd.hr.impt.formplugin.DiaeNewImportTemplate`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.impt.formplugin.DiaeNewImportTemplate/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.impt.formplugin.DiaeNewImportTemplate -->
