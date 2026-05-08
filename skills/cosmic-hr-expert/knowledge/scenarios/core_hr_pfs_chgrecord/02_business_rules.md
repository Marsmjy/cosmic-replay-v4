# core_hr_pfs_chgrecord · 业务规则

> **聚合场景**：变动记录流水持久化（chgaction 实际执行处）（3 个子实体）
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

### `hpfs_chgrecord` · ⚠️ 标品 entity_metadata md 不存在

### `hpfs_chgrecordentry` · ⚠️ 标品 entity_metadata md 不存在

### `hpfs_changerecord` · ⚠️ 标品 entity_metadata md 不存在

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

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin -->

## chgaction 实证补充（ChgRecordEditPlugin 跨类追踪聚合）

> FQN: `kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin`
> 跨类追踪: 2 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.formplugin.chgrecord.ChgRecordEditPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

## chgaction 实证补充（HRCertCheckList 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.cert.HRCertCheckList`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.cert.HRCertCheckList/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.cert.HRCertCheckList -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp -->

## chgaction 实证补充（ChgRollbackOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp`
> 跨类追踪: 21 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hpfs_licensedetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.hpfs.business.application.service.cert.impl.LicenseCer |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | errorMsg.toString() |
| `ChgRollbackOp_1` | 许可回滚异常。 |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRCertUtils_0` | 当前业务对象%s没有匹配的许可分组。 |
| `HRCertManager_4` | 许可占用数量已超出购买数量上限，系统功能限制使用，请联系系统管理员补充许可购买数量。 |
| `HRCertManager_9` | 当前元数据没有关联的套件信息，请联系系统管理员。 |

### 调用的核心 Service（Top 10）
- `PersonGenericServiceImpl.class`
- `LicenseCertApplicationServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.cross.ChgRollbackOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp -->

## chgaction 实证补充（ChgRecordEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp`
> 跨类追踪: 23 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp/`

### 实际改的底表
| 实体 | 操作 | 入口类 | 来源链 |
|---|---|---|---|
| `hpfs_licensedetail` | deleteByFilter | HRBaseServiceHelper | kd.hr.hpfs.business.application.service.cert.impl.LicenseCer |

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | message |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `HRCertUtils_0` | 当前业务对象%s没有匹配的许可分组。 |
| `HRCertManager_4` | 许可占用数量已超出购买数量上限，系统功能限制使用，请联系系统管理员补充许可购买数量。 |
| `HRCertManager_9` | 当前元数据没有关联的套件信息，请联系系统管理员。 |

### 调用的核心 Service（Top 10）
- `PersonGenericServiceImpl.class`
- `LicenseCertApplicationServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.basedata.ChgRecordEffectOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp -->

## chgaction 实证补充（ChgRecordDiscardOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp`
> 跨类追踪: 15 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `HRHisEntryObjectUtils_1` | 历史模型中单据体字段“%s”必须为长整型。 |
| `HRHisEntryObjectUtils_2` | 单据体中必须有“%1$s”字段或以“%2$s”开头的字段或包含“_%3$s”的字段，用于记录单据体的历史。 |
| `HRHisEntryObjectUtils_3` | 历史模型中分录中字段“%s”, 只能出现一次。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.basedata.ChgRecordDiscardOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp -->

## chgaction 实证补充（ChgRecordAfterEffectOp 跨类追踪聚合）

> FQN: `kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp`
> 跨类追踪: 18 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | ex.getMessage() |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hpfs.opplugin.op.chgrecord.ChgRecordAfterEffectOp -->
