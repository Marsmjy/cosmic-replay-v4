# hrptmc_reportmanage — 报表管理 操作清单
**继承链**: `bos_basetpl` → `hbp_bd_tpl_all` → `hrptmc_reportmanage`  **操作数**: 65
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b5994054000016ac` | new | 新增 | new | `bos_basetpl` | 0 | 0 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | `bos_basetpl` | 0 | 0 |
| 3 | `b5994054000018ac` | view | 查看 | view | `bos_basetpl` | 0 | 0 |
| 4 | `b599405400001aac` | save | 保存 | save | `bos_basetpl` | 7 | 4 |
| 5 | `b599405400001bac` | delete | 删除 | delete | `bos_basetpl` | 4 | 3 |
| 6 | `b599405400001cac` | audit | 审核 | audit | `bos_basetpl` | 1 | 3 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | `bos_basetpl` | 1 | 3 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | `bos_basetpl` | 1 | 2 |
| 9 | `b599405400001fac` | enable | 启用 | enable | `bos_basetpl` | 1 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | `bos_basetpl` | 0 | 0 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | `bos_basetpl` | 5 | 4 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | `bos_basetpl` | 1 | 2 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | `bos_basetpl` | 0 | 0 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | `bos_basetpl` | 0 | 0 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | `bos_basetpl` | 0 | 0 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | `bos_basetpl` | 0 | 2 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 0 | 0 |
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
| 35 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | `hbp_bd_tpl_all` | 0 | 0 |
| 36 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | `hbp_bd_tpl_all` | 0 | 0 |
| 37 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 38 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 39 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 40 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 41 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 42 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 43 | `2W6AC1N9ZSEC` | donothing | 预览 | preview | `hrptmc_reportmanage` | 0 | 0 |
| 44 | `2WTVTP1YFI97` | donothing | 确定 | enter | `hrptmc_reportmanage` | 0 | 0 |
| 45 | `2XBLLI847RDD` | donothing | 发布 | publish | `hrptmc_reportmanage` | 0 | 0 |
| 46 | `2XBLO+S5CUIP` | donothing | 下线 | offline | `hrptmc_reportmanage` | 0 | 0 |
| 47 | `2XBUP4VHW/0A` | donothing | 保存 | bizsave | `hrptmc_reportmanage` | 1 | 3 |
| 48 | `2XT57HOE/HOT` | donothing | 打开单据头 | newreport | `hrptmc_reportmanage` | 0 | 0 |
| 49 | `2YZ96ISA1JVB` | donothing | 行列转置测试 | setrowcoltransposition | `hrptmc_reportmanage` | 0 | 0 |
| 50 | `3+9G199B2Y=A` | donothing | 导出配置 | exportconfig | `hrptmc_reportmanage` | 0 | 0 |
| 51 | `36MMKDGYQPWZ` | donothing | 设置显示方案 | setdisplayscheme | `hrptmc_reportmanage` | 0 | 0 |
| 52 | `379XOTKS+3OX` | donothing | 保存 | saveandvalidate | `hrptmc_reportmanage` | 0 | 0 |
| 53 | `3=0I=6PBPQ65` | donothing | 发布到应用菜单 | saveandpublish | `hrptmc_reportmanage` | 1 | 0 |
| 54 | `3NXE7GIPJ=0E` | donothing | 引入 | importconfig | `hrptmc_reportmanage` | 0 | 0 |
| 55 | `3XW=7IZ249F1` | donothing | 复制 | rptcopy | `hrptmc_reportmanage` | 0 | 0 |
| 56 | `4/AT+W4XL61X` | donothing | 参数配置 | config | `hrptmc_reportmanage` | 0 | 0 |
| 57 | `40OEQ0QNBGW8` | donothing | 数据升级 | dataup | `hrptmc_reportmanage` | 0 | 0 |
| 58 | `43IUPSZN8BT0` | donothing | 导出SQL | exportconfigsql | `hrptmc_reportmanage` | 0 | 0 |
| 59 | `49AA42G8U/RP` | donothing | 发布到报表中心 | publishrpt | `hrptmc_reportmanage` | 0 | 0 |
| 60 | `49AA65NB0P=0` | donothing | 从报表中心下线 | offlinerpt | `hrptmc_reportmanage` | 0 | 0 |
| 61 | `49E6QONZO+Q0` | donothing | 发布到报表中心 | saveandpublishrpt | `hrptmc_reportmanage` | 1 | 0 |
| 62 | `4NM2ELKH30==` | donothing | 测试功能 | donothing | `hrptmc_reportmanage` | 0 | 0 |
| 63 | `588FKXS79A7A` | donothing | 发布到工作台 | saveandpublishwork | `hrptmc_reportmanage` | 1 | 0 |
| 64 | `588FN98/OGAN` | donothing | 从工作台下线 | offlinework | `hrptmc_reportmanage` | 0 | 0 |
| 65 | `58BAC/UVU66P` | donothing | 发布到工作台 | publishwork | `hrptmc_reportmanage` | 0 | 0 |
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
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 6 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 5 | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 1 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 4 | 3 | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2+RE4J37K857` | HR基础资料删除校验 | `hbp_bd_tpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
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
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
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
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
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
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
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
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
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
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
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
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 47. 保存（donothing / Key: bizsave / oid: 2XBUP4VHW/0A）
> 根定义: `hrptmc_reportmanage`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2XBUNGJC2731` | 字段值合规性校验 | `hrptmc_reportmanage` |
| 2 | GrpfieldsuniqueValidation | `2XBUYDCDENF0` | 编码唯一性校验 | `hrptmc_reportmanage` |
| 3 | GrpfieldsuniqueValidation | `2XBUZQ5=+VY2` | 名称唯一性校验 | `hrptmc_reportmanage` |

---
### 53. 发布到应用菜单（donothing / Key: saveandpublish / oid: 3=0I=6PBPQ65）
> 根定义: `hrptmc_reportmanage`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

---
### 61. 发布到报表中心（donothing / Key: saveandpublishrpt / oid: 49E6QONZO+Q0）
> 根定义: `hrptmc_reportmanage`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

---
### 63. 发布到工作台（donothing / Key: saveandpublishwork / oid: 588FKXS79A7A）
> 根定义: `hrptmc_reportmanage`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_reportmanage` | `kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp` | kd.hr.hrptmc.opplugin.web.repdesign.ReportManageOp |

