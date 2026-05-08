# 业务规则 · 员工个税信息

> **状态**: 🟢 已实证（v5+ 13 step + polish 后 · 2026-05-02 v5.1 升级）
> **初始化**: 2026-05-02
> **数据源**: `probe_snapshot.json` + 人工补充

> 📌 **来源追溯**: 本文由 `init_scenario_skeleton.py` 生成。verified 段来自 OpenAPI/jar 实抓；likely/unverified 段需专家补充。

## ✅ verified · 真实 formRule / bizRule（listRules 实抓）

_本场景无 formRule / bizRule（可能规则写在插件代码里，见下面 likely 段）_

## 🟡 likely · 隐式规则（来自插件命名）

<TODO> 看 `_auto_plugin_registry.md` 里 `*ValidateOp` / `*CheckOp` 插件，这些通常承载隐式规则：

- [ ] 组织编码唯一性
- [ ] 禁用前是否有下级校验
- [ ] 变动场景选择后的字段联动

## ⚠️ unverified · 跨模块约束

<TODO> 问专家：
1. 启用 / 禁用会触发哪些下游校验？（薪酬 / 考勤成本中心等）
2. 有没有不写在 rule 而是靠人肉 SOP 的规则？

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

## chgaction 实证补充（ForbidUrlOpenPlugin 跨类追踪聚合）

> FQN: `kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.formplugin.web.ForbidUrlOpenPlugin -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

## chgaction 实证补充（TimelineTplOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp`
> 跨类追踪: 13 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.TimelineTplOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `TimelineTplOp_2` | 删除失败。 |
| `TimelineTplOp_1` | 保存失败。 |
| `HisModelAttachmentService_1` | 实体编码不能为空。 |
| `HisModelAttachmentService_2` | 数据id不能为空。 |
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |
| `TimelineService_1` | “%s”已开启时间轴逻辑删除，请配置字段“是否已删除”的数据库字段名。 |
| `TimelineService_2` | “%s”的时间轴实体配置“时间段约束模式”或“逻辑主键”未配置，请先完成配置。 |

### 调用的核心 Service（Top 10）
- `timelineLogHandler.buildModifyContent`
- `timelineLogHandler.batchInsertLog`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.TimelineTplOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

## chgaction 实证补充（TimelineLogOp 跨类追踪聚合）

> FQN: `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp/`

### 调用的核心 Service（Top 10）
- `timeLineLogHandler.buildModifyContent`
- `timeLineLogHandler.setModifyInfoMap`
- `timeLineLogHandler.batchInsertLog`
- `timeLineLogHandler.getModifyInfoMap`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

## chgaction 实证补充（ChgRecordSaveOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp`
> 跨类追踪: 17 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp/`

### 业务异常清单（含异常码 + 中文消息）
| 异常码 | 业务消息 |
|---|---|
| `` | "parseDate info:" + date + ",fmt:" + YYYY_MM_DD |
| `KDDateUtils_0` | %s不是有效的当前时区时间格式 |

### 调用的核心 Service（Top 10）
- `DevParamConfigDomainServiceImpl.class`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp -->

<!-- AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->

## chgaction 实证补充（IgnoreReferenceDeleteOp 跨类追踪聚合）

> FQN: `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp`
> 跨类追踪: 1 个类访问
> 反编译产物: `_sdk_audit/_decompiled_deep/kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp/`

<!-- /AUTO-INJECTED-BY-CLASS-RESOLVER · business_rules · kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp -->
