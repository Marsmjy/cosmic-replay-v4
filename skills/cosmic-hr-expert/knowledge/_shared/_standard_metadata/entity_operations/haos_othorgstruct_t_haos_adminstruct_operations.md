# haos_othorgstruct_t_haos_adminstruct 系列 — 操作清单（合并去重）
**涵盖实体数**: 3  **去重后操作数**: 83
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```haos_adminstruct (行政组织结构)hbp_bd_originalmintpl (HR原生基础资料最小模板)    └── hbp_bd_timelinemintpl (HR时间轴最小模板)        └── haos_adminorgstruct (组织结构信息)            └── haos_othorgstruct (其他形态-组织结构)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | 2 | 1 | 1 | 2 |
| 6 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 7 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 8 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 9 | `4R/HR6LQCLZ1` | donothing | 复制 | copy | 0 | 0 | 1 | 2 |
| 10 | `4R/HUQ+ARVEU` | donothing | 查看日志 | viewlog | 0 | 0 | 1 | 2 |
| 11 | `4RIR/ALATGNY` | delete | 删除 | delete | 2 | 0 | 1 | 2 |
| 12 | `4RJA//W+TN+N` | new | 新增 | new | 0 | 0 | 1 | 2 |
| 13 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 2 |
| 14 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 2 |
| 15 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 2 |
| 16 | `b599405400001aac` | save | 保存 | save | 11 | 5 | 1 | 2 |
| 17 | `b599405400001bac` | delete | 删除 | delete | 7 | 3 | 1 | 2 |
| 18 | `b599405400001cac` | audit | 审核 | audit | 2 | 3 | 1 | 2 |
| 19 | `b599405400001dac` | unaudit | 反审核 | unaudit | 2 | 3 | 1 | 2 |
| 20 | `b599405400001eac` | disable | 禁用 | disable | 1 | 2 | 1 | 2 |
| 21 | `b599405400001fac` | enable | 启用 | enable | 2 | 1 | 1 | 2 |
| 22 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 2 |
| 23 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 2 |
| 24 | `f381c03f00002cac` | submit | 提交 | submit | 8 | 5 | 1 | 2 |
| 25 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 1 | 2 | 1 | 2 |
| 26 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 2 |
| 27 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 2 |
| 28 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 2 |
| 29 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 2 |
| 30 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 2 |
| 31 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 32 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 33 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 34 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 35 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 2 |
| 36 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 2 |
| 37 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 2 |
| 38 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 2 |
| 39 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 2 |
| 40 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 2 |
| 41 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 2 |
| 42 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 2 |
| 43 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 2 |
| 44 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 2 |
| 45 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 2 |
| 46 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 2 |
| 47 | `21A667R9=MRE` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 2 |
| 48 | `2COR52N52LPD` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 2 |
| 49 | `36NPU2DI6FXA` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 2 |
| 50 | `36NPVCLGSZN4` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 51 | `36NPWODABTRF` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 52 | `36NPYUG2BL2D` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 53 | `36NQ+5HU6UIF` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 2 |
| 54 | `36NQ/DH4BCQA` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 2 |
| 55 | `20LAOMH5GS9G` | donothing | 变更 | insertdata_his | 0 | 0 | 1 | 2 |
| 56 | `20LAQ1+BN=C4` | donothing | 历史版本信息 | showhisversion | 0 | 0 | 1 | 2 |
| 57 | `20LASIGWP/+V` | donothing | 确认变更 | confirmchange | 2 | 3 | 1 | 2 |
| 58 | `20LAXVM+O8Y4` | donothing | 修改 | his_modify | 0 | 0 | 1 | 2 |
| 59 | `20LAYTCKH5=1` | donothing | 复制 | his_copy | 1 | 0 | 1 | 2 |
| 60 | `20LAZXW2M87R` | donothing | 变更生效日期 | changebed | 1 | 0 | 1 | 2 |
| 61 | `20LB/GJONMOU` | donothing | 取消变更 | cancelchange | 1 | 0 | 1 | 2 |
| 62 | `21D086OEEI=1` | donothing | 删除历史版本 | his_delete | 1 | 0 | 1 | 2 |
| 63 | `21U2=OQDV9T1` | donothing | 确认变更 | confirmchangenoaudit | 2 | 3 | 1 | 2 |
| 64 | `2=XW1JPC69D6` | donothing | 禁用 | his_disable | 1 | 2 | 1 | 2 |
| 65 | `2BLCMMB7XGY8` | donothing | 新增数据版本 | newhisversion | 0 | 0 | 1 | 2 |
| 66 | `2BT8ISS19IVB` | donothing | 保存 | his_import_save | 2 | 3 | 1 | 2 |
| 67 | `2NY04Y3HCOJE` | donothing | 修订 | revise | 0 | 0 | 1 | 2 |
| 68 | `2NY0697GAXEN` | donothing | 版本修订历史 | reviserecord | 0 | 0 | 1 | 2 |
| 69 | `2P7PREKGKZEL` | donothing | 版本对比 | versionchangecompare | 0 | 0 | 1 | 2 |
| 70 | `2XZ4TAF9YGGC` | donothing | 查看所有版本 | showallversion | 0 | 0 | 1 | 2 |
| 71 | `WDD988SL0PR` | new | 新增 | new | 0 | 0 | 1 | 1 |
| 72 | `WDDDTLUFRCN` | modify | 修改 | modify | 0 | 0 | 1 | 1 |
| 73 | `YUKYX4B+EE6` | exportlist | 引出数据 | exportlist | 0 | 0 | 1 | 1 |
| 74 | `YULBP9HQIQ3` | enable | 启用 | enable | 1 | 2 | 1 | 1 |
| 75 | `ZQW8SC3G6+Q` | refresh | 刷新 | refresh | 0 | 0 | 1 | 1 |
| 76 | `ZQWSMWNAC7Z` | disable | 禁用 | disable | 1 | 1 | 1 | 1 |
| 77 | `ZXE=MXT1/W8` | delete | 删除 | delete | 1 | 1 | 1 | 1 |
| 78 | `ZXSKAFHZ027` | donothing | 空操作 | donothing | 1 | 0 | 1 | 1 |
| 79 | `/ER3UQ3V028D` | submit | 提交 | submit | 1 | 0 | 1 | 1 |
| 80 | `/ER3WJ0718U7` | audit | 审核 | audit | 1 | 0 | 1 | 1 |
| 81 | `/ER4ATV93OQW` | unaudit | 反审核 | unaudit | 1 | 0 | 1 | 1 |
| 82 | `/ERAXIE4+1V8` | close | 关闭 | close | 0 | 0 | 1 | 1 |
| 83 | `/HEJPMIDM7RB` | unsubmit | 撤销 | unsubmit | 1 | 0 | 1 | 1 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |
| 2 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板OP插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4R38557==XM/` | 字段值合规性校验 | `hbp_bd_timelinemintpl` |

---
### 11. 删除（delete / Key: delete / oid: 4RIR/ALATGNY）
> 根定义: `hbp_bd_timelinemintpl` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板op插件 |
| 2 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |

---
### 16. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 3 | 2 | `haos_adminorgstruct` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 3 | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 5 | 4 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 6 | 5 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 7 | 6 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 8 | 7 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 9 | 8 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 10 | 9 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 11 | 10 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `haos_adminorgstruct` |
| 3 | RegexValidation | `/=D33OTQL=12` | 长编码界定符检查 | `haos_adminorgstruct` |
| 4 | GrpfieldsuniqueValidation | `2+UBQR52MZPL` | 编码唯一性校验 | `haos_adminorgstruct` |
| 5 | GrpfieldsuniqueValidation | `2+UBRNZ73MPD` | 名称唯一性校验 | `haos_adminorgstruct` |

---
### 17. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 2 | 1 | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeDeleteValidate` |  |
| 3 | 2 | `haos_adminorgstruct` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 4 | 3 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 4 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp` | kd.hr.hbp.opplugin.web.hismodel.HisHRBaseDataOp |
| 6 | 5 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 7 | 6 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `haos_adminorgstruct` |
| 3 | CustValidation | `2+UBV409MZA0` | HR基础资料删除校验 | `haos_adminorgstruct` |

---
### 18. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `haos_adminorgstruct` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `haos_adminorgstruct` |

---
### 19. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `haos_adminorgstruct` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `haos_adminorgstruct` |

---
### 20. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `f2843bab0000bbac` | 合法性校验 | `haos_adminorgstruct` |

---
### 21. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeEnableValidate` |  |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `haos_adminorgstruct` |

---
### 24. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeOp` |  |
| 3 | 2 | `haos_adminorgstruct` | `kd.bos.business.plugin.BaseTreeLongNumberOp` |  |
| 4 | 3 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |
| 5 | 4 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 6 | 5 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 7 | 6 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |
| 8 | 7 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | GrpfieldsuniqueValidation | `424b895300015bac` | 编码唯一性校验 | `haos_adminorgstruct` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `haos_adminorgstruct` |
| 4 | GrpfieldsuniqueValidation | `2DDGVBA4NNWC` | 名称唯一性校验 | `haos_adminorgstruct` |
| 5 | GrpfieldsuniqueValidation | `28IWMI434CF8` | 名称唯一性校验 | `haos_adminorgstruct` |

---
### 25. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `haos_adminorgstruct` |

---
### 29. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `2f5773ca0001b5ac` | 组合字段唯一性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `haos_adminorgstruct` |

---
### 57. 确认变更（donothing / Key: confirmchange / oid: 20LASIGWP/+V）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `22GYIDWX2REM` | 字段值合规性校验 | `haos_adminorgstruct` |
| 2 | GrpfieldsuniqueValidation | `254GZ45=1ZPH` | 编码唯一性校验 | `haos_adminorgstruct` |
| 3 | GrpfieldsuniqueValidation | `254H++M=0/MZ` | 名称唯一性校验 | `haos_adminorgstruct` |

---
### 59. 复制（donothing / Key: his_copy / oid: 20LAYTCKH5=1）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 60. 变更生效日期（donothing / Key: changebed / oid: 20LAZXW2M87R）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 61. 取消变更（donothing / Key: cancelchange / oid: 20LB/GJONMOU）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 62. 删除历史版本（donothing / Key: his_delete / oid: 21D086OEEI=1）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

---
### 63. 确认变更（donothing / Key: confirmchangenoaudit / oid: 21U2=OQDV9T1）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `22GYJI9=W1LR` | 字段值合规性校验 | `haos_adminorgstruct` |
| 2 | GrpfieldsuniqueValidation | `254H4MI=BHO8` | 编码唯一性校验 | `haos_adminorgstruct` |
| 3 | GrpfieldsuniqueValidation | `254H5C8JCJUQ` | 名称唯一性校验 | `haos_adminorgstruct` |

---
### 64. 禁用（donothing / Key: his_disable / oid: 2=XW1JPC69D6）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `2=XW+P69BNF+` | 合法性校验 | `haos_adminorgstruct` |
| 2 | ConditionValidation | `2=XW04+8Z5M9` | 合法性校验 | `haos_adminorgstruct` |

---
### 66. 保存（donothing / Key: his_import_save / oid: 2BT8ISS19IVB）
> 根定义: `haos_adminorgstruct` | 涉及实体 (2): `haos_adminorgstruct`, `haos_othorgstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp |
| 2 | 1 | `haos_adminorgstruct` | `kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp` | kd.hr.hbp.opplugin.web.hismodel.HisLineTimeTplOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2BT8FFHIFLS=` | 字段值合规性校验 | `haos_adminorgstruct` |
| 2 | GrpfieldsuniqueValidation | `2BT8GE22K=ZC` | 名称唯一性校验 | `haos_adminorgstruct` |
| 3 | GrpfieldsuniqueValidation | `2BT8HGLX9WE8` | 编码唯一性校验 | `haos_adminorgstruct` |

---
### 74. 启用（enable / Key: enable / oid: YULBP9HQIQ3）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `/DUQFDPBP=IF` | 合法性校验 | `haos_adminstruct` |
| 2 | ConditionValidation | `/IUO9LGW3VWO` | 合法性校验 | `haos_adminstruct` |

---
### 76. 禁用（disable / Key: disable / oid: ZQWSMWNAC7Z）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `/IUOC59LRALJ` | 合法性校验 | `haos_adminstruct` |

---
### 77. 删除（delete / Key: delete / oid: ZXE=MXT1/W8）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `/R=FSVB37KLI` | 合法性校验 | `haos_adminstruct` |

---
### 78. 空操作（donothing / Key: donothing / oid: ZXSKAFHZ027）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

---
### 79. 提交（submit / Key: submit / oid: /ER3UQ3V028D）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

---
### 80. 审核（audit / Key: audit / oid: /ER3WJ0718U7）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

---
### 81. 反审核（unaudit / Key: unaudit / oid: /ER4ATV93OQW）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

---
### 83. 撤销（unsubmit / Key: unsubmit / oid: /HEJPMIDM7RB）
> 根定义: `haos_adminstruct` | 涉及实体 (1): `haos_adminstruct`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `haos_adminstruct` | `kd.hr.haos.opplugin.web.AdminOrgSaveOp` |  |

