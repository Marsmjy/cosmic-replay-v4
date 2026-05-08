# hrcs_userpermfile — 用户权限档案 操作清单
**继承链**: `bos_basetpl` → `hbp_bd_tpl_all` → `hbp_bd_tpl_dlg` → `hrcs_userpermfile`  **操作数**: 53
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `bos_basetpl` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `bos_basetpl` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `bos_basetpl` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `bos_basetpl` | 6 | 2 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `bos_basetpl` | 3 | 3 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `bos_basetpl` | 1 | 3 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `bos_basetpl` | 1 | 4 |
| 8 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 9 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `bos_basetpl` | 0 | 0 |
| 10 | `f381c03f00002cac` | submit | 提交 | submit | `bos_basetpl` | 5 | 5 |
| 11 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `bos_basetpl` | 1 | 2 |
| 12 | `f381c03f000033ac` | copy | 复制 | copy | `bos_basetpl` | 0 | 0 |
| 13 | `f381c03f000034ac` | refresh | 刷新 | refresh | `bos_basetpl` | 0 | 0 |
| 14 | `c8d21820000086ac` | option | 选项设置 | option | `bos_basetpl` | 0 | 0 |
| 15 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `bos_basetpl` | 0 | 2 |
| 16 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 1 | 0 |
| 17 | `S=VTOGXYQBY` | first | 第一 | first | `bos_basetpl` | 0 | 0 |
| 18 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `bos_basetpl` | 0 | 0 |
| 19 | `S=VVFX/B38V` | next | 后一 | next | `bos_basetpl` | 0 | 0 |
| 20 | `S=VW0K6WD7R` | last | 最后 | last | `bos_basetpl` | 0 | 0 |
| 21 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | `bos_basetpl` | 0 | 0 |
| 22 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | `bos_basetpl` | 0 | 0 |
| 23 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | `bos_basetpl` | 0 | 0 |
| 24 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | `bos_basetpl` | 0 | 0 |
| 25 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_basetpl` | 0 | 0 |
| 26 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_basetpl` | 0 | 0 |
| 27 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | `bos_basetpl` | 0 | 0 |
| 28 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | `bos_basetpl` | 0 | 0 |
| 29 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_basetpl` | 0 | 0 |
| 30 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_basetpl` | 0 | 0 |
| 31 | `2PVU302I5+FH` | donothing | 改名 | namehistory | `bos_basetpl` | 0 | 0 |
| 32 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | `bos_basetpl` | 0 | 0 |
| 33 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | `hbp_bd_tpl_all` | 0 | 0 |
| 34 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 35 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 36 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 37 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 38 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 39 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 40 | `QQJ9VPEIWE2` | assign | 分配 | assign | `hrcs_userpermfile` | 0 | 0 |
| 41 | `QQJATA099TH` | unassign | 取消分配 | unassign | `hrcs_userpermfile` | 0 | 0 |
| 42 | `/IRN4Y=FI53J` | donothing | 变更控制策略 | bdctrlchange | `hrcs_userpermfile` | 0 | 0 |
| 43 | `0DACD6GQT/MR` | donothing | 空操作 | individuation | `hrcs_userpermfile` | 0 | 0 |
| 44 | `2CM=+M12M1OM` | donothing | 查看日志 | viewonelog | `hrcs_userpermfile` | 0 | 0 |
| 45 | `0WH=4QGYI1JJ` | donothing | 空操作 | donothing | `hrcs_userpermfile` | 0 | 0 |
| 46 | `2=KLV7OR+AX2` | donothing | 分配角色 | assignrole | `hrcs_userpermfile` | 0 | 0 |
| 47 | `2OP1A30PFLIS` | donothing | 档案初始化 | syncperm | `hrcs_userpermfile` | 0 | 0 |
| 48 | `34RSPBGI92C+` | donothing | 初始化用户权限 | inituserperm | `hrcs_userpermfile` | 0 | 0 |
| 49 | `36A1LS1PIM2K` | donothing | 引出用户权限 | exportuserperm | `hrcs_userpermfile` | 0 | 0 |
| 50 | `3QO3NHTZXKRM` | donothing | 复制权限 | copyuserperm | `hrcs_userpermfile` | 0 | 0 |
| 51 | `5ACIIFXO7RRM` | donothing | 批量分组 | batchgroup | `hrcs_userpermfile` | 0 | 0 |
| 52 | `5LKNQ=IEXXVG` | donothing | 生效 | enable | `hrcs_userpermfile` | 1 | 0 |
| 53 | `5LKNT2BR3O79` | donothing | 失效 | disable | `hrcs_userpermfile` | 1 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 1 | `hrcs_userpermfile` | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` |  |
| 4 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 3 | `hrcs_userpermfile` | `kd.hr.hrcs.opplugin.web.perm.UserPermfileSaveOp` |  |
| 6 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hrcs_userpermfile` | `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` |  |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `0R6UAC9LYGXN` | HR基础资料删除校验 | `hrcs_userpermfile` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_userpermfile` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_userpermfile` | `kd.bos.form.plugin.bdctrl.BaseDataUnAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |
| 4 | ConditionValidation | `/U2/165YZ4K5` | 合法性校验 | `hrcs_userpermfile` |

---
### 10. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 4 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 5 | — | `hrcs_userpermfile` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `createorg.id` | 编码唯一性校验 | `hrcs_userpermfile` |
| 5 | GrpfieldsuniqueValidation | `62a4cf9800016fac` | 名称唯一性校验 | `hrcs_userpermfile` |

---
### 11. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl`
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
### 15. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 16. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_userpermfile` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

---
### 52. 生效（donothing / Key: enable / oid: 5LKNQ=IEXXVG）
> 根定义: `hrcs_userpermfile`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_userpermfile` | `kd.hr.hrcs.opplugin.web.perm.PermFileEnableOp` | 权限档案生效OP |

---
### 53. 失效（donothing / Key: disable / oid: 5LKNT2BR3O79）
> 根定义: `hrcs_userpermfile`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_userpermfile` | `kd.hr.hrcs.opplugin.web.perm.PermFileDisableOp` | 权限档案失效OP插件 |

