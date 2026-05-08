# hrcs_coordstrategy_t_hrcs_coordstrategy 系列 — 操作清单（合并去重）
**涵盖实体数**: 8  **去重后操作数**: 55
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── bos_baseorgtpl (基础资料带组织模板)        └── hbp_bd_orgtpl_all (HR带组织基础资料全页面模板)            └── hbp_coordstrategy (协作处理策略模板)                └── hrcs_coordstrategy (协作处理策略)                    ├── hrcs_coordstrategy11 (协作处理策略_薪资档案)                    ├── hrcs_coordstrategy12 (协作处理策略_定调薪档案)                    ├── hrcs_coordstrategy13 (协作处理策略_社保档案)                    ├── hrcs_coordstrategy14 (协作处理策略_个税档案)                    ├── hrcs_coordstrategy51 ()                    ├── hrcs_coordstrategy61 (协作处理策略_绩效档案)                    └── hrcs_coordstrategy81 (协作处理策略_人才档案)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 8 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 8 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 8 |
| 4 | `b599405400001aac` | save | 保存 | save | 8 | 4 | 1 | 8 |
| 5 | `b599405400001bac` | delete | 删除 | delete | 5 | 3 | 1 | 8 |
| 6 | `b599405400001cac` | audit | 审核 | audit | 3 | 3 | 1 | 8 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | 2 | 3 | 1 | 8 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | 2 | 2 | 1 | 8 |
| 9 | `b599405400001fac` | enable | 启用 | enable | 2 | 1 | 1 | 8 |
| 10 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 8 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 8 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | 7 | 5 | 1 | 8 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 2 | 2 | 1 | 8 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 8 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 8 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 8 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 6 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 1 | 0 | 1 | 8 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 8 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 8 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 8 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 8 |
| 23 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 8 |
| 24 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 8 |
| 25 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 8 |
| 26 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 8 |
| 27 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 8 |
| 28 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 8 |
| 29 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 8 |
| 30 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 8 |
| 31 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 8 |
| 32 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 8 |
| 33 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 8 |
| 34 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 8 |
| 35 | `QQJ9VPEIWE2` | assign | 分配 | assign | 0 | 0 | 1 | 8 |
| 36 | `QQJATA099TH` | unassign | 取消分配 | unassign | 0 | 0 | 1 | 8 |
| 37 | `/IRN4Y=FI53J` | donothing | 变更控制策略 | bdctrlchange | 0 | 0 | 1 | 8 |
| 38 | `0DACD6GQT/MR` | donothing | 空操作 | individuation | 0 | 0 | 1 | 8 |
| 39 | `329L6QTC2T7I` | donothing | 分配 | assign_new | 0 | 0 | 1 | 8 |
| 40 | `3A=7K395P2/8` | donothing | 自动分配管理 | auto_assign | 0 | 0 | 1 | 8 |
| 41 | `3ACT23RWB6N/` | donothing | Excel导入分配关系 | tbl_assign_import | 0 | 0 | 1 | 8 |
| 42 | `21A5G1REGO=D` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 8 |
| 43 | `2CM=+M12M1OM` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 8 |
| 44 | `36NMATCD+40R` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 8 |
| 45 | `36NMCH7J7K17` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 8 |
| 46 | `36NMEU7ZJ64A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 8 |
| 47 | `36NMFZKH+JK4` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 8 |
| 48 | `36NMHG0I3HKP` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 8 |
| 49 | `36NMIYKCRQOF` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 8 |
| 50 | `51JDEXL=/DXT` | deleteentry | 删除 | deleteentry | 0 | 0 | 1 | 8 |
| 51 | `51JDG9BQF7R8` | moveentryup | 上移 | moveentryup | 0 | 0 | 1 | 8 |
| 52 | `51JDH3+/0TTF` | moveentrydown | 下移 | moveentrydown | 0 | 0 | 1 | 8 |
| 53 | `51Y=I3L2A+AG` | donothing | 提交 | submitconfirm | 0 | 0 | 1 | 8 |
| 54 | `52360CXPFCG7` | donothing | 审核 | auditsyn | 0 | 0 | 1 | 8 |
| 55 | `5LUGGR1GCOMS` | submitandnew | 提交并新增 | submitandnew | 0 | 0 | 1 | 1 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` |  |
| 3 | 2 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 4 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 6 | 5 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 7 | 6 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 8 | 7 | `hrcs_coordstrategy` | `kd.hr.hrcs.opplugin.web.coordination.CoordStrategySaveOp` | kd.hr.hrcs.opplugin.web.coordination.CoordStrategySaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `0+/SL/MZ=VJB` | 创建组织和编码组合字段唯一性校验 | `bos_baseorgtpl` |
| 4 | GrpfieldsuniqueValidation | `3D3G/MZ2++JS` | 名称唯一性校验 | `hbp_bd_orgtpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 2 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 4 | `hrcs_coordstrategy` | `kd.hr.hrcs.opplugin.web.coordination.CoordStrategyDelOp` | kd.hr.hrcs.opplugin.web.coordination.CoordStrategyDelOp |
| 5 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+U=J7R7IEF/` | HR基础资料删除校验 | `hbp_bd_orgtpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 2 | `hrcs_coordstrategy` | `kd.hr.hrcs.opplugin.web.coordination.CoordStrategyAuditOp` | kd.hr.hrcs.opplugin.web.coordination.CoordStrategyAuditOp |
| 3 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `bos_basetpl` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 1 | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |
| 4 | 2 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 6 | 4 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 5 | `hrcs_coordstrategy` | `kd.hr.hrcs.opplugin.web.coordination.CoordStrategySaveOp` | kd.hr.hrcs.opplugin.web.coordination.CoordStrategySaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `createorg.id` | 创建组织和编码组合字段唯一性校验 | `bos_baseorgtpl` |
| 5 | GrpfieldsuniqueValidation | `2+U=UD1KBODU` | 名称唯一性校验 | `hbp_bd_orgtpl_all` |

---
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnSubmitPlugin` | 受控基础资料撤销插件 |
| 2 | — | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl` | 涉及实体 (6): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 18. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordstrategy`, `hrcs_coordstrategy11`, `hrcs_coordstrategy12`, `hrcs_coordstrategy13`, `hrcs_coordstrategy14`, `hrcs_coordstrategy51`, `hrcs_coordstrategy61`, `hrcs_coordstrategy81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

