# haos_staff — 编制信息维护 操作清单
**继承链**: `bos_basetpl` → `hbp_bd_tpl_all` → `haos_staff`  **操作数**: 102
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `b599405400001aac` | save | 保存 | save | `bos_basetpl` | 7 | 7 |
| 2 | `b599405400001eac` | disable | 禁用 | disable | `bos_basetpl` | 2 | 3 |
| 3 | `b599405400001fac` | enable | 启用 | enable | `bos_basetpl` | 2 | 1 |
| 4 | `b5994054000021ac` | close | 关闭 | close | `bos_basetpl` | 0 | 0 |
| 5 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | `bos_basetpl` | 0 | 0 |
| 6 | `S=VVFX/B38V` | next | 后一 | next | `bos_basetpl` | 0 | 0 |
| 7 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | `bos_basetpl` | 0 | 0 |
| 8 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | `bos_basetpl` | 0 | 0 |
| 9 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | `bos_basetpl` | 0 | 0 |
| 10 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | `bos_basetpl` | 0 | 0 |
| 11 | `2PVU302I5+FH` | donothing | 改名 | namehistory | `bos_basetpl` | 0 | 0 |
| 12 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | `bos_basetpl` | 0 | 0 |
| 13 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | `hbp_bd_tpl_all` | 0 | 0 |
| 14 | `36NMYUPXH7+Y` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 15 | `36NN+52WYF1Y` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 16 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 17 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 18 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 19 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_tpl_all` | 0 | 0 |
| 20 | `2IV2IIXNMQB/` | donothing | 复制使用组织 | copy_useorgdetail | `haos_staff` | 0 | 0 |
| 21 | `2IV2KCXZWWNA` | donothing | 添加使用组织 | new_useorgdetail | `haos_staff` | 0 | 0 |
| 22 | `2IV2NKK9ITUN` | deleteentry | 删除使用组织 | delete_useorgdetail | `haos_staff` | 0 | 0 |
| 23 | `2IV2QV2E=10B` | donothing | 汇总计算 | donothing_sum | `haos_staff` | 0 | 0 |
| 24 | `2IV4N10TEQ++` | donothing | 复制多维细分编制 | copy_muldimendetail | `haos_staff` | 0 | 0 |
| 25 | `2IV4T970C=VP` | importentry | 导入多维细分编制 | importmuldimendetail | `haos_staff` | 0 | 0 |
| 26 | `2IY4D9I1QQ/Z` | importentry | 导入责任组织 | import_dutyorg | `haos_staff` | 0 | 0 |
| 27 | `2IY4HJ8CU0+4` | donothing | 添加责任组织 | new_dutyorg | `haos_staff` | 0 | 0 |
| 28 | `2IY4PC6V==7N` | deleteentry | 删除责任组织 | delete_dutyorg | `haos_staff` | 0 | 0 |
| 29 | `2JBHB9+1DCP3` | new | 新增 | new | `haos_staff` | 0 | 0 |
| 30 | `2K19GKPU/IYQ` | importentry | 导入使用组织 | importuseorgdetail | `haos_staff` | 0 | 0 |
| 31 | `2KOK12XC9S4S` | modify | 修改 | modify | `haos_staff` | 0 | 0 |
| 32 | `2LHQ+N3QCKB5` | donothing | 删除 | donothing_delete | `haos_staff` | 1 | 0 |
| 33 | `2P4X=YTN5MRO` | donothing | 变更 | donothing_modify | `haos_staff` | 0 | 0 |
| 34 | `2XSR+HFX0UYE` | donothing | 添加职位 | donothing_newjob | `haos_staff` | 0 | 0 |
| 35 | `2XSR89QSMS0V` | donothing | 添加用工关系类型 | donothing_newlaborreltype | `haos_staff` | 0 | 0 |
| 36 | `2XSREWF=576G` | donothing | 删除职位 | donothing_deletejob | `haos_staff` | 0 | 0 |
| 37 | `2XSRKN7TZLOT` | donothing | 删除用工关系类型 | donothing_deletelaborreltype | `haos_staff` | 0 | 0 |
| 38 | `2ZXL1F3IYRK7` | donothing | 查询使用情况 | donothing_openreport | `haos_staff` | 0 | 0 |
| 39 | `3YVM/1LVDEFB` | exportentry | 导出 | exportentry_useorg | `haos_staff` | 0 | 0 |
| 40 | `3YVM7K8BN1I9` | exportentry | 导出 | exportentry_position | `haos_staff` | 0 | 0 |
| 41 | `3YVMBB=7PC1=` | exportentry | 导出 | exportentry_job | `haos_staff` | 0 | 0 |
| 42 | `3YVMFIRBCH23` | exportentry | 导出 | exportentry_laborreltype | `haos_staff` | 0 | 0 |
| 43 | `4MR64E+KETJD` | newentry | 添加 | addflexdim1 | `haos_staff` | 0 | 0 |
| 44 | `4MR65E4LC/27` | newentry | 添加 | addflexdim2 | `haos_staff` | 0 | 0 |
| 45 | `4MR66QU=PERX` | newentry | 添加 | addflexdim3 | `haos_staff` | 0 | 0 |
| 46 | `4MR67MVYQE/3` | newentry | 添加 | addflexdim4 | `haos_staff` | 0 | 0 |
| 47 | `4MR68KATM+AE` | newentry | 添加 | addflexdim5 | `haos_staff` | 0 | 0 |
| 48 | `4MR69KU0F471` | newentry | 添加 | addflexdim6 | `haos_staff` | 0 | 0 |
| 49 | `4MR6G289QM3V` | exportentry | 导出 | exportentry_flexdim1 | `haos_staff` | 0 | 0 |
| 50 | `4MR6H54ZLLQI` | exportentry | 导出 | exportentry_flexdim2 | `haos_staff` | 0 | 0 |
| 51 | `4MR6I0Z7DM5E` | exportentry | 导出 | exportentry_flexdim3 | `haos_staff` | 0 | 0 |
| 52 | `4MR6IXA3U171` | exportentry | 导出 | exportentry_flexdim4 | `haos_staff` | 0 | 0 |
| 53 | `4MR6K/KTIOYS` | exportentry | 导出 | exportentry_flexdim5 | `haos_staff` | 0 | 0 |
| 54 | `4MR6L6COARES` | exportentry | 导出 | exportentry_flexdim6 | `haos_staff` | 0 | 0 |
| 55 | `4OM6WANHI=W2` | newentry | 添加 | addflexdim7 | `haos_staff` | 0 | 0 |
| 56 | `4OM7I8H6EIWE` | exportentry | 导出 | exportentry_flexdim7 | `haos_staff` | 0 | 0 |
| 57 | `4OVPINLAZ8VA` | newentry | 添加 | addflexdim8 | `haos_staff` | 0 | 0 |
| 58 | `4OVPLXSNAC6T` | exportentry | 导出 | exportentry_flexdim8 | `haos_staff` | 0 | 0 |
| 59 | `4OVPNRYMPKDJ` | newentry | 添加 | addflexdim9 | `haos_staff` | 0 | 0 |
| 60 | `4OVPT2L4GST9` | exportentry | 导出 | exportentry_flexdim9 | `haos_staff` | 0 | 0 |
| 61 | `4OVPVFMJ1YVH` | newentry | 添加 | addflexdim10 | `haos_staff` | 0 | 0 |
| 62 | `4OVPYNSFTGP0` | exportentry | 导出 | exportentry_flexdim10 | `haos_staff` | 0 | 0 |
| 63 | `4OVQ53BB80M1` | newentry | 添加 | addflexdim11 | `haos_staff` | 0 | 0 |
| 64 | `4OVQ9/2DB2A3` | exportentry | 导出 | exportentry_flexdim11 | `haos_staff` | 0 | 0 |
| 65 | `4OVQA/R=IDVY` | newentry | 添加 | addflexdim12 | `haos_staff` | 0 | 0 |
| 66 | `4OVQDGCAL9GC` | exportentry | 导出 | exportentry_flexdim12 | `haos_staff` | 0 | 0 |
| 67 | `4OVQF/P4W36I` | newentry | 新增 | addflexdim13 | `haos_staff` | 0 | 0 |
| 68 | `4OVQI4VGK+G+` | exportentry | 导出 | exportentry_flexdim13 | `haos_staff` | 0 | 0 |
| 69 | `4OVQK17R/PP6` | newentry | 添加 | addflexdim14 | `haos_staff` | 0 | 0 |
| 70 | `4OVQNCWGKCTG` | exportentry | 导出 | exportentry_flexdim14 | `haos_staff` | 0 | 0 |
| 71 | `4OVQOY7DGMQ4` | newentry | 添加 | addflexdim15 | `haos_staff` | 0 | 0 |
| 72 | `4OVQRZINRD1X` | exportentry | 导出 | exportentry_flexdim15 | `haos_staff` | 0 | 0 |
| 73 | `4OVQTZT4/SNL` | newentry | 添加 | addflexdim16 | `haos_staff` | 0 | 0 |
| 74 | `4OVQX1X9CQKW` | exportentry | 导出 | exportentry_flexdim16 | `haos_staff` | 0 | 0 |
| 75 | `4OVQYPP69V75` | newentry | 添加 | addflexdim17 | `haos_staff` | 0 | 0 |
| 76 | `4OVR/TJ6DJ4J` | exportentry | 导出 | exportentry_flexdim17 | `haos_staff` | 0 | 0 |
| 77 | `4OVR1JDJ5C=8` | newentry | 添加 | addflexdim18 | `haos_staff` | 0 | 0 |
| 78 | `4OVR4J9CF2PQ` | exportentry | 导出 | exportentry_flexdim18 | `haos_staff` | 0 | 0 |
| 79 | `4OVR6929S2SS` | newentry | 添加 | addflexdim19 | `haos_staff` | 0 | 0 |
| 80 | `4OVR9A05YW6F` | exportentry | 导出 | exportentry_flexdim19 | `haos_staff` | 0 | 0 |
| 81 | `4OVR=X7IMSF5` | newentry | 添加 | addflexdim20 | `haos_staff` | 0 | 0 |
| 82 | `4OVRCZ/KCDPI` | exportentry | 导出 | exportentry_flexdim20 | `haos_staff` | 0 | 0 |
| 83 | `4S53+W8I/QPO` | donothing | 删除 | donothing_deleteflexdim1 | `haos_staff` | 0 | 0 |
| 84 | `4S539OCNZFSE` | donothing | 删除 | donothing_deleteflexdim2 | `haos_staff` | 0 | 0 |
| 85 | `4S53A2Z/C3O=` | donothing | 删除 | donothing_deleteflexdim3 | `haos_staff` | 0 | 0 |
| 86 | `4S53CW9B1HRS` | donothing | 删除 | donothing_deleteflexdim4 | `haos_staff` | 0 | 0 |
| 87 | `4S53EO=V7KKF` | donothing | 删除 | donothing_deleteflexdim5 | `haos_staff` | 0 | 0 |
| 88 | `4S53GI7TYKW7` | donothing | 删除 | donothing_deleteflexdim6 | `haos_staff` | 0 | 0 |
| 89 | `4S53I5SZBSKZ` | donothing | 删除 | donothing_deleteflexdim7 | `haos_staff` | 0 | 0 |
| 90 | `4S53JY1DC=KK` | donothing | 删除 | donothing_deleteflexdim8 | `haos_staff` | 0 | 0 |
| 91 | `4S53LT9YN8CB` | donothing | 删除 | donothing_deleteflexdim9 | `haos_staff` | 0 | 0 |
| 92 | `4S53NDSCO+5Y` | donothing | 删除 | donothing_deleteflexdim10 | `haos_staff` | 0 | 0 |
| 93 | `4S53P1O8V19Y` | donothing | 删除 | donothing_deleteflexdim11 | `haos_staff` | 0 | 0 |
| 94 | `4S53QPYOCW00` | donothing | 删除 | donothing_deleteflexdim12 | `haos_staff` | 0 | 0 |
| 95 | `4S53S=TK41O9` | donothing | 删除 | donothing_deleteflexdim13 | `haos_staff` | 0 | 0 |
| 96 | `4S53U+SYDU3L` | donothing | 删除 | donothing_deleteflexdim14 | `haos_staff` | 0 | 0 |
| 97 | `4S53VLYNPK/P` | donothing | 删除 | donothing_deleteflexdim15 | `haos_staff` | 0 | 0 |
| 98 | `4S53XA/9WV7+` | donothing | 删除 | donothing_deleteflexdim16 | `haos_staff` | 0 | 0 |
| 99 | `4S53YY0QDH8H` | donothing | 删除 | donothing_deleteflexdim17 | `haos_staff` | 0 | 0 |
| 100 | `4S54+MHW43LE` | donothing | 删除 | donothing_deleteflexdim18 | `haos_staff` | 0 | 0 |
| 101 | `4S540CT4SF/O` | donothing | 删除 | donothing_deleteflexdim19 | `haos_staff` | 0 | 0 |
| 102 | `4S542HMYQ0L5` | donothing | 删除 | donothing_deleteflexdim20 | `haos_staff` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 1. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 6 | 4 | `haos_staff` | `kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp` | kd.hr.haos.opplugin.web.staff.OrgStaffSaveOp |
| 7 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |
| 5 | ConditionValidation | `2KVMVEORQQ73` | 弹性额度b分录合法性校验 | `haos_staff` |
| 6 | ConditionValidation | `2KVOG0=DFH1F` | 弹性额度c分录合法性校验 | `haos_staff` |
| 7 | ConditionValidation | `2KVOI5Q1D9CL` | 弹性额度d分录合法性校验 | `haos_staff` |

---
### 2. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `haos_staff` | `kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp` | kd.hr.haos.opplugin.web.staff.OrgStaffDisableOp |
| 2 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `bos_basetpl` |
| 3 | ConditionValidation | `2LEHVAOUR5TC` | 合法性校验 | `haos_staff` |

---
### 3. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `haos_staff` | `kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp` | kd.hr.haos.opplugin.web.staff.OrgStaffEnableOp |
| 2 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 32. 删除（donothing / Key: donothing_delete / oid: 2LHQ+N3QCKB5）
> 根定义: `haos_staff`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_staff` | `kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp` | kd.hr.haos.opplugin.web.staff.OrgStaffDeleteOp |

