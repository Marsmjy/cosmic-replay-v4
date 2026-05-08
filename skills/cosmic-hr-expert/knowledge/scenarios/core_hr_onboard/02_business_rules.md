# core_hr_onboard · 业务规则

> **聚合场景**：入职管理（hom · 入职办理 + 预入职 + 候选人协作）（7 个子实体）
> **生成时间**：2026-04-30
> **方法**：从 `_shared/_standard_metadata/entity_metadata/<entity>.md` 提取每子实体真字段元数据 → 标品 HRBaseDataTplEdit 共性规则 + 子实体特有约束

## 一、共性规则（HRBaseDataTplEdit 标品模板 · 12 实体共用）

本聚合场景所有子实体均继承 `kd.hr.hbp.formplugin.web.template.HRBaseDataTplEdit` 标品模板，自带规则：

| 规则 ID | 触发点 | 行为 | ISV 是否可改 |
|---|---|---|:---:|
| BR_TPL_1 | 表单加载 (afterBindData) | 自动加载基础资料元数据 + 渲染字段 | ❌ 标品 |
| BR_TPL_2 | save 操作 | 触发 CodeRuleOp 自动生成 number 字段（按编码规则） | ❌ 标品 · 规则在元数据里配置 |
| BR_TPL_3 | save 操作 | HRBaseDataStatusOp · 设置 status 字段（A/B/C 状态机） | ❌ 标品 |
| BR_TPL_4 | save 操作 | HRBaseOriginalOp · 维护 orinumber/oriname/oristatus 出厂数据 | ❌ 标品 |
| BR_TPL_5 | enable / disable | HRBaseDataEnableOp · 维护 enable 字段 + disabledate/disabler | ❌ 标品 |
| BR_TPL_6 | save 操作 | HRBaseDataLogOp · 写变更日志（按 HRBaseDataConfigUtil 配置启用） | ❌ 标品 |

> ⚠️ ISV 不应继承 HRBaseDataTplEdit · 应**并列挂** `HRDataBaseEdit` 实现自定义逻辑（PR-001）

## 二、子实体特有规则（按字段提取）

### `hom_onbrdinfo` · ⚠️ 标品 entity_metadata md 不存在

### `hom_onbrdinfo_emp` · ⚠️ 标品 entity_metadata md 不存在

### `hom_preonbrdbasebill` · ⚠️ 标品 entity_metadata md 不存在

### `hom_prebatchonbrdbill` · ⚠️ 标品 entity_metadata md 不存在

### `hom_personallonbrd` · ⚠️ 标品 entity_metadata md 不存在

### `hom_personwaitstart` · ⚠️ 标品 entity_metadata md 不存在

### `hom_candidate` · ⚠️ 标品 entity_metadata md 不存在

## 三、关键约束（共 12 实体）

| 约束 | 适用实体 | 来源 |
|---|---|---|
| `number` 唯一性 | 全部 12 实体 | 标品 CodeRuleOp + 元数据 UniqueValidation |
| `enable` 默认值 = '1'（启用） | 全部 12 实体 | HRBaseDataEnableOp |
| `status` 状态机 (A 暂存 → B 待审核 → C 已审核) | 全部 12 实体 | HRBaseDataStatusOp |
| `disabler` / `disabledate` 自动维护 | 全部 12 实体 | HRBaseDataEnableOp |

---

**精修元数据**：
- 生成器：`scripts/polish_aggregate_scene.py`
- 数据源：12 子实体的 `_shared/_standard_metadata/entity_metadata/<entity>.md`
- 标品共性来自 `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` / `HRBaseDataEnableOp` / `HRBaseOriginalOp` / `HRBaseDataLogOp` 反编译实证

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

## chgaction 实证补充（HRTemplateBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

## chgaction 实证补充（HRPermCommonEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonEdit -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

## chgaction 实证补充（PerChgNewBillTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplEdit`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgHomBillTplEdit -->

## chgaction 实证补充（PerChgHomBillTplEdit 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgHomBillTplEdit`
> 跨类追踪: 23 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgHomBillTplEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgHomBillTplEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

## chgaction 实证补充（ForbidUrlOpenPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit -->

## chgaction 实证补充（OnbrdInfoEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit`
> 跨类追踪: 48 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `OcrHttpClientUtils_0` | 视觉识别服务请求异常，请重试。 |
| `OcrHttpClientUtils_1` | 视觉识别服务地址未配置，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `HomToHrcsAppServiceImpl.class`
- `ActivityHandleServiceImpl.dealTransfer`
- `HomToHrcsAppServiceImpl.initActivityInfo`
- `BaseDataDomainServiceImpl.class`
- `HomDataMutexServiceImpl.class`
- `HomToCvpAppServiceImpl.class`
- `HomFaceDetectServiceImpl.class`
- `HomFaceDetectServiceImpl.kdCloudPlatformHeader`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit -->

## chgaction 实证补充（AgainOnbrdInfoEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit`
> 跨类追踪: 40 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `OnbrdCommonAppServiceImpl.class`
- `PersonFileIntegrateServiceImpl.class`
- `HomToHrcsAppServiceImpl.class`
- `ActivityHandleServiceImpl.dealTransfer`
- `HomToHrcsAppServiceImpl.initActivityInfo`
- `BaseDataDomainServiceImpl.class`
- `HomDataMutexServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.AgainOnbrdInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit -->

## chgaction 实证补充（OnboardCadreInfoEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit`
> 跨类追踪: 5 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnboardCadreInfoEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin -->

## chgaction 实证补充（OnbrdChgActionPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `BaseDataDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.common.hpfs.OnbrdChgActionPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

## chgaction 实证补充（CommonPermissionEdit 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

## chgaction 实证补充（HRTemplateBillList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.template.HRTemplateBillList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.template.HRTemplateBillList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.template.HRTemplateBillList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

## chgaction 实证补充（HRPermCommonList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.HRPermCommonList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.HRPermCommonList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.HRPermCommonList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

## chgaction 实证补充（PerChgNewBillTplList 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.PerChgNewBillTplList`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.PerChgNewBillTplList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.PerChgNewBillTplList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin -->

## chgaction 实证补充（FrozenColumnsPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.activity.FrozenColumnsPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin -->

## chgaction 实证补充（OnbrdBillListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin`
> 跨类追踪: 27 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdBillListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

## chgaction 实证补充（HRCommonListFilterPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.filter.HRCommonListFilterPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

## chgaction 实证补充（CommonPermissionList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList`
> 跨类追踪: 11 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.odc.perm.CommonPermissionList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

## chgaction 实证补充（PerChgTplSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

## chgaction 实证补充（PerChgTplUpdateChgRecordOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplUpdateChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp -->

## chgaction 实证补充（OnbrdBillSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp`
> 跨类追踪: 23 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hpfs_filemapmanager` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `hcf_canbankcard` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveSingleRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateDataByOp | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteById | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveOne | HomCommonRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteByIds | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | msg |
| `OnbrdBillSaveOp_0` | 占编异常，请联系编制管理员。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |

### 调用的核心 Service（Top 10）
- `BaseDataDomainServiceImpl.class`
- `HomToHcfAppServiceImpl.getAttachmentPanelData`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`
- `StaffUseServiceImpl.judgeIfChangedWithDimension`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp -->

## chgaction 实证补充（PerChgOnbrdTplCommonOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplCommonOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp -->

## chgaction 实证补充（OnbrdBillSaveOpenAPIOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hpfs_filemapmanager` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `hcf_canbankcard` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveSingleRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateDataByOp | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteById | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveOne | HomCommonRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteByIds | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |

### 调用的核心 Service（Top 10）
- `BaseDataDomainServiceImpl.class`
- `HomToHcfAppServiceImpl.getAttachmentPanelData`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`
- `StaffUseServiceImpl.judgeIfChangedWithDimension`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBillSaveOpenAPIOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

## chgaction 实证补充（PerChgTplSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

## chgaction 实证补充（PerChgTplEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp -->

## chgaction 实证补充（PerChgOnbrdTplEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp`
> 跨类追踪: 22 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgOnbrdTplEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdEffectOp -->

## chgaction 实证补充（OnbrdEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdEffectOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `HomHPFSTemplatePropertyHandler.buildOpCustomPersonParams`
- `HomHPFSTemplatePropertyHandler.class`
- `HomHPFSTemplatePropertyHandler.createDynamicObjectByEntityNum`
- `HomHPFSTemplatePropertyHandler.createDynamicObject`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp -->

## chgaction 实证补充（OnbrdConfirmOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `OnbrdConfirmAppServiceImpl_4` | 占编异常，请联系编制管理员。 |
| `` | errorMessage |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `OnbrdConfirmAppServiceImpl.class`
- `OnbrdConfirmAppServiceImpl.syncAndUpdateOnbrd`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdConfirmOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp -->

## chgaction 实证补充（ApprovalAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp -->

## chgaction 实证补充（OnbrdEffectRetryOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp`
> 跨类追踪: 34 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `OnbrdConfirmAppServiceImpl_4` | 占编异常，请联系编制管理员。 |
| `` | errorMessage |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `OnbrdConfirmAppServiceImpl.class`
- `OnbrdConfirmAppServiceImpl.syncAndUpdateOnbrd`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdEffectRetryOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

## chgaction 实证补充（PerChgTplDiscardChgRecordOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplDiscardChgRecordOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp -->

## chgaction 实证补充（OnbrdAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp`
> 跨类追踪: 37 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | <self> (depth=0) |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `OnbrdConfirmAppServiceImpl_4` | 占编异常，请联系编制管理员。 |
| `` | errorMessage |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `OnbrdConfirmAppServiceImpl.class`
- `OnbrdConfirmAppServiceImpl.syncAndUpdateOnbrd`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdAfterEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

## chgaction 实证补充（PerChgTplRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | result.getMessage() |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

## chgaction 实证补充（PerChgTplEffectSyncOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp`
> 跨类追踪: 24 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ChgUtils_1` | 没有从管理关系策略中找到行政组织为“%1$s”且业务类型为人事管理的默认HR管理组织。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.tpl.PerChgTplEffectSyncOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp -->

## chgaction 实证补充（OnbrdStartUpProcessOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_activityoverview` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.activity.ActivityRepository.update |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `ActivityCommonInfoServiceImpl_12` | 活动实例不存在，请重新选择。 |
| `ActivityCommonInfoServiceImpl_17` | 转交给 用户不能为空！ |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `OnbrdConfirmAppServiceImpl_4` | 占编异常，请联系编制管理员。 |

### 调用的核心 Service（Top 10）
- `HomToHrcsAppServiceImpl.class`
- `ActivityHandleServiceImpl.dealTransfer`
- `HomToHrcsAppServiceImpl.initActivityInfo`
- `BaseDataDomainServiceImpl.class`
- `ActivityCommonInfoServiceImpl.class`
- `checkHandler.booleanValue`
- `OnbrdConfirmAppServiceImpl.class`
- `OnbrdConfirmAppServiceImpl.syncAndUpdateOnbrd`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdStartUpProcessOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp -->

## chgaction 实证补充（ForceApprovalAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ForceApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp -->

## chgaction 实证补充（OnbrdBreakupExtOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateDynamicObjects | HomCommonRepository | kd.hr.hom.business.application.service.impl.onbrd.OnbrdBreak |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteByIds | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |

### 调用的核心 Service（Top 10）
- `OnbrdBreakupAppServiceImpl.class`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdBreakupExtOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp -->

## chgaction 实证补充（OnbrdConfirmExtOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp`
> 跨类追踪: 34 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `OnbrdConfirmAppServiceImpl_4` | 占编异常，请联系编制管理员。 |
| `` | errorMessage |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `OnbrdConfirmAppServiceImpl.class`
- `OnbrdConfirmAppServiceImpl.syncAndUpdateOnbrd`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdConfirmExtOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.UnSubmitOp -->

## chgaction 实证补充（UnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.UnSubmitOp`
> 跨类追踪: 31 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.UnSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | deleteByFilter | HomCommonRepository | <self> (depth=0) |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`
- `StaffUseServiceImpl.judgeIfChangedWithDimension`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.UnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp -->

## chgaction 实证补充（ApprovalAuditingOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp -->

## chgaction 实证补充（ApprovalTerminationOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp`
> 跨类追踪: 29 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateDynamicObjects | HomCommonRepository | kd.hr.hom.business.application.service.impl.onbrd.OnbrdBreak |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `hrcs_activity` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateOne | ActivityRepository | kd.hr.hom.business.domain.service.impl.activity.ActivityDoma |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteByIds | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |

### 调用的核心 Service（Top 10）
- `OnbrdBreakupAppServiceImpl.class`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalTerminationOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp -->

## chgaction 实证补充（ApprovalRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`
- `StaffUseServiceImpl.judgeIfChangedWithDimension`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp -->

## chgaction 实证补充（ApprovalAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `StaffUseServiceImpl_9` | 调用编制服务异常，请稍后重试。 |
| `` | errorMessage |
| `TransferStaffServiceImpl_0` | 调用编制服务异常，请稍后重试。 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `StaffUseServiceImpl.class`
- `StaffUseServiceImpl.queryStaffUseInfos`
- `StaffUseServiceImpl.getOrgTeamId`
- `StaffUseServiceImpl.getPositionId`
- `StaffUseServiceImpl.getJobId`
- `StaffUseServiceImpl.getDimensionValue`
- `StaffUseServiceImpl.buildStaffUseInParams`
- `StaffUseServiceImpl.judgeIfChangedWithDimension`
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.ApprovalAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit -->

## chgaction 实证补充（PreOnBrdBillHeadEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillHeadEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit -->

## chgaction 实证补充（PreOnBrdButtonEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdButtonEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit -->

## chgaction 实证补充（PreOnBrdBillEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PersonFileIntegrateServiceImpl.class`
- `BaseDataDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin -->

## chgaction 实证补充（SideWorkflowPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.SideWorkflowPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit -->

## chgaction 实证补充（PreOnBrdF7SelectEdit 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `BaseDataDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdF7SelectEdit -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin -->

## chgaction 实证补充（PreOnBrdBillListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.preonbrd.PreOnBrdBillListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp -->

## chgaction 实证补充（PreOnBrdSaveOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp -->

## chgaction 实证补充（PreOnBrdSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp -->

## chgaction 实证补充（PreOnBrdUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp -->

## chgaction 实证补充（PreApprovalRejectToSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreApprovalRejectToSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp -->

## chgaction 实证补充（ApprovalAuditPassOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |
| `hpfs_filemapmanager` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `hcf_canbankcard` | updateOne | HRBaseServiceHelper | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveSingleRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveCandidateDataByOp | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteById | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | saveOne | HomCommonRepository | kd.hr.hom.business.application.service.impl.hcf.HomToHcfAppS |
| `(via repository)` | updateDynamicObject | HomCommonRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteByIds | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |
| `(via repository)` | deleteRowEntity | HcfRepository | kd.hr.hom.business.domain.service.impl.hcf.HcfDataDomainServ |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `HomToHcfAppServiceImpl.getAttachmentPanelData`
- `BaseDataDomainServiceImpl.class`
- `PersonFileIntegrateServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp -->

## chgaction 实证补充（ApprovalAuditingOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditingOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp -->

## chgaction 实证补充（ApprovalAuditNotPassOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveDynamicObjects | HomCommonRepository | <self> (depth=0) |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.ApprovalAuditNotPassOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp -->

## chgaction 实证补充（PreOnBrdDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.preonbrd.PreOnBrdDeleteOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin -->

## chgaction 实证补充（OnbrdAddNewPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdAddNewPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin -->

## chgaction 实证补充（OnbrdStartupPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin`
> 跨类追踪: 14 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdStartupPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin -->

## chgaction 实证补充（QFilterSetPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.personinfo.QFilterSetPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin -->

## chgaction 实证补充（OnbrdBillListImportCertPlugin 跨类追踪聚合）

> FQN: `kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.formplugin.web.personmange.OnbrdBillListImportCertPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp -->

## chgaction 实证补充（OnbrdInfoDeleteOp 跨类追踪聚合）

> FQN: `kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp`
> 跨类追踪: 28 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveOnbrdBillInfo | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | saveOnbrdBillInfos | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `(via repository)` | deleteByPkIds | OnbrdBillRepository | kd.hr.hom.business.domain.service.impl.onbrd.OnbrdBillDomain |
| `hom_onbrdbilltpl` | saveOne | HRBaseServiceHelper | kd.hr.hom.business.domain.onbrd.OnbrdBillRepository.findOnbr |
| `(via repository)` | saveCandidateData | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |
| `(via repository)` | saveCandidateDynamicObjectCollection | HcfRepository | kd.hr.hom.business.domain.hcf.HcfRepository.queryRowEntity ( |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exception.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HcfRepository_0` | 校验不通过,请检查 |
| `HcfRepository_8` | 参数为空 |
| `HcfRepository_9` | 查询失败 |

### 调用的核心 Service（Top 10）
- `PersonFileIntegrateServiceImpl.class`
- `OnbrdCommonAppServiceImpl.class`
- `HomDataMutexServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hom.opplugin.onbrd.OnbrdInfoDeleteOp -->

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
