# hrcs_filterparam — 过滤场景参数 操作清单
**继承链**: `bos_basetpl` → `hbp_bd_tpl_all` → `hbp_bd_tpl_dlg` → `hrcs_filterparam`  **操作数**: 50
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `bos_basetpl` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `bos_basetpl` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `bos_basetpl` | 0 | 0 |
| 4 | `b599405400001bac` | delete | 删除 | delete | `bos_basetpl` | 3 | 3 |
| 5 | `b599405400001cac` | audit | 审核 | audit | `bos_basetpl` | 1 | 3 |
| 6 | `b599405400001dac` | unaudit | 反审核 | unaudit | `bos_basetpl` | 1 | 3 |
| 7 | `b599405400001eac` | disable | 禁用 | disable | `bos_basetpl` | 1 | 2 |
| 8 | `b599405400001fac` | enable | 启用 | enable | `bos_basetpl` | 1 | 1 |
| 9 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 10 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `bos_basetpl` | 0 | 0 |
| 11 | `f381c03f00002cac` | submit | 提交 | submit | `bos_basetpl` | 5 | 4 |
| 12 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `bos_basetpl` | 1 | 2 |
| 13 | `f381c03f000033ac` | copy | 复制 | copy | `bos_basetpl` | 0 | 0 |
| 14 | `f381c03f000034ac` | refresh | 刷新 | refresh | `bos_basetpl` | 0 | 0 |
| 15 | `c8d21820000086ac` | option | 选项设置 | option | `bos_basetpl` | 0 | 0 |
| 16 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `bos_basetpl` | 0 | 2 |
| 17 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 0 | 0 |
| 18 | `S=VTOGXYQBY` | first | 第一 | first | `bos_basetpl` | 0 | 0 |
| 19 | `S=VUN3Y4R6Q` | previous | 前一 | previous | `bos_basetpl` | 0 | 0 |
| 20 | `S=VVFX/B38V` | next | 后一 | next | `bos_basetpl` | 0 | 0 |
| 21 | `S=VW0K6WD7R` | last | 最后 | last | `bos_basetpl` | 0 | 0 |
| 22 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | `bos_basetpl` | 0 | 0 |
| 23 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | `bos_basetpl` | 0 | 0 |
| 24 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | `bos_basetpl` | 0 | 0 |
| 25 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | `bos_basetpl` | 0 | 0 |
| 26 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_basetpl` | 0 | 0 |
| 27 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_basetpl` | 0 | 0 |
| 28 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | `bos_basetpl` | 0 | 0 |
| 29 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | `bos_basetpl` | 0 | 0 |
| 30 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_basetpl` | 0 | 0 |
| 31 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_basetpl` | 0 | 0 |
| 32 | `2PVU302I5+FH` | donothing | 改名 | namehistory | `bos_basetpl` | 0 | 0 |
| 33 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | `bos_basetpl` | 0 | 0 |
| 34 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | `hbp_bd_tpl_all` | 0 | 0 |
| 35 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | `hbp_bd_tpl_all` | 0 | 0 |
| 36 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 37 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 38 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 39 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 40 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 41 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 42 | `1EWU3AYHHMG1` | donothing | 保存 | save | `hrcs_filterparam` | 0 | 1 |
| 43 | `2H3RRUPPEIVF` | deleteentry | 删行 | deleteentry | `hrcs_filterparam` | 0 | 0 |
| 44 | `2H3RWMG/5=JH` | moveentryup | 上移 | moveentryup | `hrcs_filterparam` | 0 | 0 |
| 45 | `2H3RXMK5K5JT` | moveentrydown | 下移 | moveentrydown | `hrcs_filterparam` | 0 | 0 |
| 46 | `2H3RZK5ZRI7E` | newentry | 增行 | newentry1 | `hrcs_filterparam` | 0 | 0 |
| 47 | `2H3S/QEPOJNR` | deleteentry | 删行 | deleteentry1 | `hrcs_filterparam` | 0 | 0 |
| 48 | `2H3S1G/2I9YN` | moveentryup | 上移 | moveentryup1 | `hrcs_filterparam` | 0 | 0 |
| 49 | `2H3S2Q9I1PZL` | moveentrydown | 下移 | moveentrydown1 | `hrcs_filterparam` | 0 | 0 |
| 50 | `2H4BMBE1JPD9` | donothing | 导入字段 | importfield | `hrcs_filterparam` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 4. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `hbp_bd_tpl_all` |

---
### 5. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl`
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
### 6. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl`
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
### 7. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `bos_basetpl` |

---
### 8. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 11. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl`
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
### 12. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
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
### 16. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 42. 保存（donothing / Key: save / oid: 1EWU3AYHHMG1）
> 根定义: `hrcs_filterparam`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `1EWU36TSSTBS` | 字段值合规性校验 | `hrcs_filterparam` |

