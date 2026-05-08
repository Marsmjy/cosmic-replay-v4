# core_hr_econtract · 业务规则

> **聚合场景**：电子签 6 状态 + 3 厂商集成（法大大公有/私有 + 易企签）（1 个子实体）
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

### `core_hr_econtract` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin -->

## chgaction 实证补充（ElectricSignDynamicPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.electric.ElectricSignDynamicPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin -->

## chgaction 实证补充（SignInitializationPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin`
> 跨类追踪: 37 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateOne | CommonRepository | kd.hr.hlcm.business.domian.service.signmgt.impl.SignManageSe |
| `bos_attachment` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.repository.BosAttachmentRepositor |
| `hlcm_hiredperson` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `hlcm_contractapplybase` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `SignManageServiceImpl_13` | 参数错误 |
| `SignManageServiceImpl_12` | 未找到合同单据。 |
| `SignManageServiceImpl_9` | 个人信息确认失败。 |
| `SignManageServiceImpl_16` | 替换关键字生成合同失败。 |
| `` | errMsg |
| `SignManageServiceImpl_19` | 上传文件失败 |
| `HRCSServiceImpl_0` | 未找到合同单据。 |
| `HRMServiceHelper_0` | %1$s. %2$s获取ISV供应商标识为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `PrivacyServiceImpl.class`
- `PrivacyServiceImpl.queryPrivacy`
- `PrivacyServiceImpl.signPrivacy`
- `SignManageServiceImpl.class`
- `SignManageServiceImpl.getActivityStatusEnum`
- `SignManageServiceImpl.empSign`
- `SignManageServiceImpl.invalidateContract`
- `SignManageServiceImpl.getWFActivityId`
- `HRCSServiceImpl.class`
- `HRCSServiceImpl.toSign`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.mobile.SignInitializationPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin -->

## chgaction 实证补充（HRExtToolPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.HRExtToolPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin -->

## chgaction 实证补充（ISDeletePersonFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.base.ISDeletePersonFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin -->

## chgaction 实证补充（BaseContractBillPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin`
> 跨类追踪: 27 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.service.hismodel.helper.ContractH |
| `bos_attachment` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.repository.BosAttachmentRepositor |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ContractHisHelper_0` | 数据错误，请联系管理员。 |

### 调用的核心 Service（Top 10）
- `SignManageServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.BaseContractBillPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin -->

## chgaction 实证补充（SignManageBasePlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin`
> 跨类追踪: 27 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ContractPageLoadUtils_0` | 操作失败，入职撤回导致人员数据已被删除。 |

### 调用的核心 Service（Top 10）
- `SignManageServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignManageBasePlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin -->

## chgaction 实证补充（ConfirmCommonFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin`
> 跨类追踪: 36 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateOne | CommonRepository | kd.hr.hlcm.business.domian.service.signmgt.impl.SignManageSe |
| `bos_attachment` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.repository.BosAttachmentRepositor |
| `hlcm_hiredperson` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `hlcm_contractapplybase` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `SignManageServiceImpl_13` | 参数错误 |
| `SignManageServiceImpl_12` | 未找到合同单据。 |
| `SignManageServiceImpl_9` | 个人信息确认失败。 |
| `SignManageServiceImpl_16` | 替换关键字生成合同失败。 |
| `` | errMsg |
| `SignManageServiceImpl_19` | 上传文件失败 |
| `HRCSServiceImpl_0` | 未找到合同单据。 |
| `HRMServiceHelper_0` | %1$s. %2$s获取ISV供应商标识为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `SignManageServiceImpl.class`
- `SignManageServiceImpl.getActivityStatusEnum`
- `SignManageServiceImpl.empSign`
- `SignManageServiceImpl.invalidateContract`
- `SignManageServiceImpl.getWFActivityId`
- `HRCSServiceImpl.class`
- `HRCSServiceImpl.toSign`
- `ShareServiceImpl.class`
- `ShareServiceImpl.getSlaInfo`
- `SendMessageServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.common.ConfirmCommonFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin -->

## chgaction 实证补充（ContractEntryEntityFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.base.ContractEntryEntityFormPlugin -->

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin -->

## chgaction 实证补充（FixedCommonListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.FixedCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin -->

## chgaction 实证补充（HisFieldF7FilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.billapply.HisFieldF7FilterListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin -->

## chgaction 实证补充（SignCommonListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin`
> 跨类追踪: 16 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignCommonListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin -->

## chgaction 实证补充（SignOutListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignOutListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp -->

## chgaction 实证补充（ContractBaseUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp`
> 跨类追踪: 19 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hlcm_hiredperson` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `hlcm_contractapplybase` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | exp.getMessage() |
| `HRCSServiceImpl_0` | 未找到合同单据。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `DevConfigServiceImpl.class`
- `HRCSServiceImpl.class`
- `HRCSServiceImpl.toSign`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

## chgaction 实证补充（HRCodeRuleOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.HRCodeRuleOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.HRCodeRuleOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.HRCodeRuleOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp -->

## chgaction 实证补充（ContractBaseSubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp`
> 跨类追踪: 31 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateDynamicObject | CommonRepository | <self> (depth=0) |
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.service.hismodel.helper.ContractH |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `ContractHisHelper_0` | 数据错误，请联系管理员。 |
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |

### 调用的核心 Service（Top 10）
- `SignManageServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp -->

## chgaction 实证补充（ContractBaseTerminateOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractBaseTerminateOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp -->

## chgaction 实证补充（PreSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp -->

## chgaction 实证补充（PreSubmitEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreSubmitEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp -->

## chgaction 实证补充（PreUnSubmitOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.apply.PreUnSubmitOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp -->

## chgaction 实证补充（ContractInitWorkFlowOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp`
> 跨类追踪: 4 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.ContractInitWorkFlowOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.BeginSignOp -->

## chgaction 实证补充（BeginSignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.BeginSignOp`
> 跨类追踪: 3 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.BeginSignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.BeginSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp -->

## chgaction 实证补充（CompleteSignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.CompleteSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp -->

## chgaction 实证补充（ConfirmArchiveOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp`
> 跨类追踪: 35 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | updateOne | CommonRepository | kd.hr.hlcm.business.domian.service.signmgt.impl.SignManageSe |
| `bos_attachment` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.repository.BosAttachmentRepositor |
| `hlcm_hiredperson` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `hlcm_contractapplybase` | deleteByFilter | HRBaseServiceHelper | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |
| `(via repository)` | updateDynamicObjects | HLCMCommonRepository | kd.hr.hlcm.business.domian.prewarn.SyncStartStatusService.sy |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `SignManageServiceImpl_13` | 参数错误 |
| `SignManageServiceImpl_12` | 未找到合同单据。 |
| `SignManageServiceImpl_9` | 个人信息确认失败。 |
| `SignManageServiceImpl_16` | 替换关键字生成合同失败。 |
| `` | errMsg |
| `SignManageServiceImpl_19` | 上传文件失败 |
| `HRCSServiceImpl_0` | 未找到合同单据。 |
| `HRMServiceHelper_0` | %1$s. %2$s获取ISV供应商标识为空。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `SignManageServiceImpl.class`
- `SignManageServiceImpl.getActivityStatusEnum`
- `SignManageServiceImpl.empSign`
- `SignManageServiceImpl.invalidateContract`
- `SignManageServiceImpl.getWFActivityId`
- `HRCSServiceImpl.class`
- `HRCSServiceImpl.toSign`
- `ShareServiceImpl.class`
- `ShareServiceImpl.getSlaInfo`
- `SendMessageServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.ConfirmArchiveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.CompanySignOp -->

## chgaction 实证补充（CompanySignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.CompanySignOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.CompanySignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.CompanySignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp -->

## chgaction 实证补充（PreBeginSignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreBeginSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp -->

## chgaction 实证补充（PreCompanySignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreCompanySignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp -->

## chgaction 实证补充（PreCompleteSignOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreCompleteSignOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp -->

## chgaction 实证补充（PreConfirmArchiveOp 跨类追踪聚合）

> FQN: `kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.opplugin.contract.sign.PreConfirmArchiveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin -->

## chgaction 实证补充（SignAllFilterListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hlcm.formplugin.signmgt.SignAllFilterListPlugin -->
