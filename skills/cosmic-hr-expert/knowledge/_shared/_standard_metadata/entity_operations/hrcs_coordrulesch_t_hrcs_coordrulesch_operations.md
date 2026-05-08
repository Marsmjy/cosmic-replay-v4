# hrcs_coordrulesch_t_hrcs_coordrulesch 系列 — 操作清单（合并去重）
**涵盖实体数**: 8  **去重后操作数**: 60
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── bos_baseorgtpl (基础资料带组织模板)        └── hbp_bd_orgtpl_all (HR带组织基础资料全页面模板)            └── hbp_coordrulesch (协作规则方案模板)                └── hrcs_coordrulesch (协作规则方案)                    ├── hrcs_coordrulesch11 (协作规则方案_薪资档案)                    ├── hrcs_coordrulesch12 (协作规则方案_定调薪档案)                    ├── hrcs_coordrulesch13 (协作规则方案_社保档案)                    ├── hrcs_coordrulesch14 (协作规则方案_个税档案)                    ├── hrcs_coordrulesch51 ()                    ├── hrcs_coordrulesch61 (协作规则方案_绩效档案)                    └── hrcs_coordrulesch81 (协作规则方案_人才档案)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 8 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 8 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 8 |
| 4 | `b599405400001aac` | save | 保存 | save | 7 | 4 | 1 | 8 |
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
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 8 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 1 | 0 | 1 | 8 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 8 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 8 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 8 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 8 |
| 23 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 8 |
| 24 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 8 |
| 25 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 8 |
| 26 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 8 |
| 27 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 8 |
| 28 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 8 |
| 29 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 8 |
| 30 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 8 |
| 31 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 8 |
| 32 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 8 |
| 33 | `QQJ9VPEIWE2` | assign | 分配 | assign | 0 | 0 | 1 | 8 |
| 34 | `QQJATA099TH` | unassign | 取消分配 | unassign | 0 | 0 | 1 | 8 |
| 35 | `/IRN4Y=FI53J` | donothing | 变更控制策略 | bdctrlchange | 0 | 0 | 1 | 8 |
| 36 | `0DACD6GQT/MR` | donothing | 空操作 | individuation | 0 | 0 | 1 | 8 |
| 37 | `329L6QTC2T7I` | donothing | 分配 | assign_new | 0 | 0 | 1 | 8 |
| 38 | `3A=7K395P2/8` | donothing | 自动分配管理 | auto_assign | 0 | 0 | 1 | 8 |
| 39 | `3ACT23RWB6N/` | donothing | Excel导入分配关系 | tbl_assign_import | 0 | 0 | 1 | 8 |
| 40 | `21A5G1REGO=D` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 8 |
| 41 | `2CM=+M12M1OM` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 8 |
| 42 | `36NMEU7ZJ64A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 8 |
| 43 | `36NMFZKH+JK4` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 8 |
| 44 | `36NMHG0I3HKP` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 8 |
| 45 | `36NMIYKCRQOF` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 8 |
| 46 | `4Z3IRU+FWEO+` | deleteentry | 删除分录 | deleteentry | 0 | 0 | 1 | 8 |
| 47 | `4Z3IVD+/2RJY` | deleteentry | 删除分录 | deleteoldentry | 0 | 0 | 1 | 8 |
| 48 | `4Z3IWTDDEV4+` | moveentryup | 上移一行 | moveentryup | 0 | 0 | 1 | 8 |
| 49 | `4Z3IY6J4G545` | moveentrydown | 下移 | moveentrydown | 0 | 0 | 1 | 8 |
| 50 | `4Z3IZR=PGW/E` | moveentryup | 上移 | moveoldentryup | 0 | 0 | 1 | 8 |
| 51 | `4Z3J/1UXWSNL` | moveentrydown | 下移 | moveoldentrydown | 0 | 0 | 1 | 8 |
| 52 | `4Z3SWIRLY1IU` | donothing | 保存 | saverule | 1 | 0 | 1 | 8 |
| 53 | `4Z9ZTE5=3BMC` | donothing | 保存 | saveruleclick | 0 | 0 | 1 | 8 |
| 54 | `4ZK04HZCH/M8` | donothing | 取消 | cancel | 0 | 0 | 1 | 8 |
| 55 | `5+2404/4QFVU` | donothing | 修改 | basemodify | 0 | 0 | 1 | 8 |
| 56 | `5+243N6VT+2N` | donothing | 修改 | rulemodify | 0 | 0 | 1 | 8 |
| 57 | `5+2NLDR616Z+` | donothing | 取消 | basecancel | 0 | 0 | 1 | 8 |
| 58 | `5+6OMIALC0BL` | donothing | 提交 | submitrule | 0 | 0 | 1 | 8 |
| 59 | `5+CKEN371CLZ` | donothing | 审核确认 | auditconfirm | 0 | 0 | 1 | 8 |
| 60 | `5+X3BOAQFID4` | donothing | 复制规则 | rulecopy | 1 | 0 | 1 | 8 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
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

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `0+/SL/MZ=VJB` | 创建组织和编码组合字段唯一性校验 | `bos_baseorgtpl` |
| 4 | GrpfieldsuniqueValidation | `3D3G/MZ2++JS` | 名称唯一性校验 | `hbp_bd_orgtpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 2 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 4 | `hrcs_coordrulesch` | `kd.hr.hrcs.opplugin.web.coordination.RuleSchDelOp` | kd.hr.hrcs.opplugin.web.coordination.RuleSchDelOp |
| 5 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+U=J7R7IEF/` | HR基础资料删除校验 | `hbp_bd_orgtpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 2 | `hbp_coordrulesch` | `kd.hr.hrcs.opplugin.web.coordination.RuleSchAuditOp` | kd.hr.hrcs.opplugin.web.coordination.RuleSchAuditOp |
| 3 | — | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
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
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
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
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
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
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 1 | `bos_baseorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |
| 4 | 2 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 3 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 6 | 4 | `hbp_bd_orgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 5 | `hbp_coordrulesch` | `kd.hr.hrcs.opplugin.web.coordination.RuleSchSubmitOp` | kd.hr.hrcs.opplugin.web.coordination.RuleSchSubmitOp |

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
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
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
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 18. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_baseorgtpl` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

---
### 52. 保存（donothing / Key: saverule / oid: 4Z3SWIRLY1IU）
> 根定义: `hbp_coordrulesch` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordrulesch` | `kd.hr.hrcs.opplugin.web.coordination.RuleSchSaveRuleOp` | kd.hr.hrcs.opplugin.web.coordination.RuleSchSaveRuleOp |

---
### 60. 复制规则（donothing / Key: rulecopy / oid: 5+X3BOAQFID4）
> 根定义: `hbp_coordrulesch` | 涉及实体 (8): `hrcs_coordrulesch`, `hrcs_coordrulesch11`, `hrcs_coordrulesch12`, `hrcs_coordrulesch13`, `hrcs_coordrulesch14`, `hrcs_coordrulesch51`, `hrcs_coordrulesch61`, `hrcs_coordrulesch81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_coordrulesch` | `kd.hr.hrcs.opplugin.web.coordination.RuleSchCopyOp` | kd.hr.hrcs.opplugin.web.coordination.RuleSchCopyOp |

