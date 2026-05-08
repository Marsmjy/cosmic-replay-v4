# core_hr_pfs_blacklist · 业务规则

> **聚合场景**：黑名单（hpfs 视角 · 含日志 + 简卡 + 弹窗）（3 个子实体）
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

### `hpfs_blacklist` · ⚠️ 标品 entity_metadata md 不存在

### `hpfs_blacklistlog` · ⚠️ 标品 entity_metadata md 不存在

### `hpfs_simplecard` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListFormPlugin -->

## chgaction 实证补充（BlackListFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.blacklist.BlackListFormPlugin`
> 跨类追踪: 30 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.blacklist.BlackListFormPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.showFormPara |
| `hrpi_blacklistlog` | saveOne | HRBaseServiceHelper | kd.hr.hpfs.business.infrastructure.client.hrpi.HRPIBlackList |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `PersonServiceImpl_5` | 请传入有效校验值。 |

### 调用的核心 Service（Top 10）
- `AssignmentApplicationServiceImpl.class`
- `EmpPosOrgRelApplicationServiceImpl.class`
- `AdminOrgServiceImpl.class`
- `PositionServiceImpl.class`
- `JobServiceImpl.class`
- `ChangePersonServiceImpl.class`
- `EmployeeApplicationServiceImpl.class`
- `AssignmentAttApplicationServiceImpl.class`
- `EmployeeDomainServiceImpl.class`
- `EmployeeDomainServiceImpl.getBosUserInfo`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListFormPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListListPlugin -->

## chgaction 实证补充（BlackListListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.blacklist.BlackListListPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.blacklist.BlackListListPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.showFormPara |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListOutSideImportPlugin -->

## chgaction 实证补充（BlackListOutSideImportPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.blacklist.BlackListOutSideImportPlugin`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.blacklist.BlackListOutSideImportPlugin/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `(via repository)` | saveBlackListLog | HRPIBlackListLogRepository | kd.hr.hpfs.business.domain.utils.BlacklistUtils.saveBlacklis |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `BlackListLogServiceImpl_0` | 黑名单日志分录参数为空。 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListOutSideImportPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListLogListPlugin -->

## chgaction 实证补充（BlackListLogListPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.blacklist.BlackListLogListPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.blacklist.BlackListLogListPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.BlackListLogListPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.PersonCardInfoFormPlugin -->

## chgaction 实证补充（PersonCardInfoFormPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.blacklist.PersonCardInfoFormPlugin`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.blacklist.PersonCardInfoFormPlugin/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.blacklist.PersonCardInfoFormPlugin -->
