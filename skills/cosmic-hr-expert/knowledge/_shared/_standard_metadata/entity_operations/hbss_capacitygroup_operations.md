# hbss_capacitygroup — 能力素质维度 操作清单
**继承链**: `bos_basetpl` → `bos_basetreetpl` → `bos_basetreeorgtpl` → `hbp_bd_treeorgtpl_all` → `hbp_bd_treeorgtpl_dlg` → `hbss_capacitygroup`  **操作数**: 49
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `bos_basetpl` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `bos_basetpl` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `bos_basetpl` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `bos_basetpl` | 11 | 6 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `bos_basetpl` | 5 | 3 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `bos_basetpl` | 2 | 3 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `bos_basetpl` | 2 | 3 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | `bos_basetpl` | 2 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | `bos_basetpl` | 3 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `bos_basetpl` | 0 | 0 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | `bos_basetpl` | 8 | 5 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `bos_basetpl` | 2 | 2 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | `bos_basetpl` | 0 | 0 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | `bos_basetpl` | 0 | 0 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | `bos_basetpl` | 0 | 0 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `bos_basetpl` | 0 | 2 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 1 | 0 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | `bos_basetpl` | 0 | 0 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `bos_basetpl` | 0 | 0 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | `bos_basetpl` | 0 | 0 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | `bos_basetpl` | 0 | 0 |
| 23 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | `bos_basetpl` | 0 | 0 |
| 24 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | `bos_basetpl` | 0 | 0 |
| 25 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | `bos_basetpl` | 0 | 0 |
| 26 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | `bos_basetpl` | 0 | 0 |
| 27 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_basetpl` | 0 | 0 |
| 28 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_basetpl` | 0 | 0 |
| 29 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | `bos_basetpl` | 0 | 0 |
| 30 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | `bos_basetpl` | 0 | 0 |
| 31 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_basetpl` | 0 | 0 |
| 32 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_basetpl` | 0 | 0 |
| 33 | `2PVU302I5+FH` | donothing | 改名 | namehistory | `bos_basetpl` | 0 | 0 |
| 34 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | `bos_basetpl` | 0 | 0 |
| 35 | `QQJFP=H2XYJ` | assign | 分配 | assign | `bos_basetreeorgtpl` | 0 | 0 |
| 36 | `QQJGN+SXCNA` | unassign | 取消分配 | unassign | `bos_basetreeorgtpl` | 0 | 0 |
| 37 | `/IRNELTAKRN5` | donothing | 变更控制策略 | bdctrlchange | `bos_basetreeorgtpl` | 0 | 0 |
| 38 | `0DAGX1UWTCMA` | donothing | 个性化数据 | individuation | `bos_basetreeorgtpl` | 0 | 0 |
| 39 | `329L9=+W+9EZ` | donothing | 分配 | assign_new | `bos_basetreeorgtpl` | 0 | 0 |
| 40 | `3A=8/ME7O/RV` | donothing | 自动分配管理 | auto_assign | `bos_basetreeorgtpl` | 0 | 0 |
| 41 | `3ACQVNUITEA8` | donothing | Excel导入分配关系 | tbl_assign_import | `bos_basetreeorgtpl` | 0 | 0 |
| 42 | `21A619YEYLAH` | donothing | 查看全部日志 | logview | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 43 | `2COR0JKWRGOD` | donothing | 查看日志 | viewonelog | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 44 | `36NPA7CD9M/7` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 45 | `36NPBECDG6UG` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 46 | `36NPD0P2P4U6` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 47 | `36NPEOG9NZ1+` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 48 | `36NPFYN903YM` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
| 49 | `36NPIJS3Y=2U` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_treeorgtpl_all` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 3 | 2 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 3 | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 5 | 4 | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.TreeBaseDataSavePlugin` |  |
| 6 | 5 | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` |  |
| 7 | 6 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 8 | 7 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 9 | 8 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 10 | 9 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 11 | 10 | `hbss_capacitygroup` | `kd.hr.hbss.opplugin.web.capacity.CapacityDimOp` | kd.hr.hbss.opplugin.web.capacity.CapacityDimOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | RegexValidation | `/=D33OTQL=12` | 长编码界定符检查 | `bos_basetreetpl` |
| 4 | GrpfieldsuniqueValidation | `/D4C17LPW9F+` | 组合字段唯一性校验 | `bos_basetreetpl` |
| 5 | GrpfieldsuniqueValidation | `number` | 创建组织和编码组合字段唯一性校验 | `bos_basetreeorgtpl` |
| 6 | GrpfieldsuniqueValidation | `4CS7NZ2UL8GY` | 名称字段唯一性校验 | `hbss_capacitygroup` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 2 | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataTreeDeletePlugin` |  |
| 2 | 3 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 3 | 4 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 5 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | — | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+U=5MGHE+DK` | HR基础资料删除校验 | `hbp_bd_treeorgtpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | — | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

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
| 1 | 1 | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnAuditPlugin` |  |
| 2 | 1 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | — | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `bos_basetpl` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin` |  |
| 2 | 2 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 3 | — | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeEnableValidate` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 3 | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 4 | 4 | `bos_basetreetpl` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 5 | 5 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 6 | 6 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 7 | 7 | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 8 | — | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `createorg.id` | 创建组织和编码组合字段唯一性校验 | `bos_basetreeorgtpl` |
| 5 | GrpfieldsuniqueValidation | `4CS86BZCSZ+4` | 名称字段唯一性校验 | `hbss_capacitygroup` |

---
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetreeorgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnSubmitPlugin` | 受控基础资料撤销插件 |
| 2 | — | `hbp_bd_treeorgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 18. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetreeorgtpl` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

