# hrcs_activity_t_hrcs_activity 系列 — 操作清单（合并去重）
**涵盖实体数**: 2  **去重后操作数**: 43
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── hbp_bd_tpl_all (HR基础资料全页面模板)        └── hbp_bd_tpl_dlg (HR基础资料对话框模板)            └── hrcs_activity (活动)                └── hrcs_activityview (新增活动布局-交付工具)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 2 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 2 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 2 |
| 4 | `b599405400001aac` | save | 保存 | save | 7 | 4 | 1 | 2 |
| 5 | `b599405400001bac` | delete | 删除 | delete | 4 | 3 | 1 | 2 |
| 6 | `b599405400001cac` | audit | 审核 | audit | 1 | 3 | 1 | 2 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | 1 | 3 | 1 | 2 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | 2 | 2 | 1 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | 2 | 1 | 1 | 2 |
| 10 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 2 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 2 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | 5 | 4 | 1 | 2 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 1 | 2 | 1 | 2 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 2 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 2 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 2 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 2 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 2 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 23 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 2 |
| 24 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 2 |
| 25 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 2 |
| 26 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 2 |
| 27 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 2 |
| 28 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 2 |
| 29 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 2 |
| 30 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 2 |
| 31 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 2 |
| 32 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 2 |
| 33 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 2 |
| 34 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 2 |
| 35 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 2 |
| 36 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 2 |
| 37 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 2 |
| 38 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 39 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 40 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 41 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 2 |
| 42 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 2 |
| 43 | `4I70A44E3C0X` | save | 保存 | saveforlogicentity | 1 | 0 | 1 | 2 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 4 | `hrcs_activity` | `kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp` | 活动保存插件，生成工作流节点模板 |
| 6 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 7 | 6 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 3 | `hrcs_activity` | `kd.hr.hrcs.opplugin.web.activity.ActivityDeleteOp` | 活动删除插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `hbp_bd_tpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_activity` | `kd.hr.hrcs.opplugin.web.activity.ActivityDisableOp` | 活动禁用校验插件 |
| 2 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `bos_basetpl` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrcs_activity` | `kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp` | kd.hr.hrcs.opplugin.web.activity.ActivityEnableOp |
| 2 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `2DDGGGIVWM1U` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 43. 保存（save / Key: saveforlogicentity / oid: 4I70A44E3C0X）
> 根定义: `hrcs_activity` | 涉及实体 (2): `hrcs_activity`, `hrcs_activityview`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_activity` | `kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp` | kd.hr.hrcs.opplugin.web.activity.ActivitySaveOp |

