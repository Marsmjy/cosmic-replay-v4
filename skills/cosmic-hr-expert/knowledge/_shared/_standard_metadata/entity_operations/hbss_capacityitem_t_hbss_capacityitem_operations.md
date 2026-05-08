# hbss_capacityitem_t_hbss_capacityitem 系列 — 操作清单（合并去重）
**涵盖实体数**: 2  **去重后操作数**: 76
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── bos_basegrouptpl (分组基础资料模板)        └── bos_basegrouporgtpl (分组基础资料带组织模板)            └── hbp_bd_grouporgtpl_all (HR分组基础资料带组织全页面模板)                └── hbp_hisbugrptimeseqtpl (HR带BU带分组基础资料全页面历史模板)                    └── hbss_capacityitem (能力素质项)hbss_capacityitemdetail (能力素质详情)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 1 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 1 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 1 |
| 4 | `b599405400001aac` | save | 保存 | save | 9 | 4 | 1 | 1 |
| 5 | `b599405400001bac` | delete | 删除 | delete | 4 | 3 | 1 | 1 |
| 6 | `b599405400001cac` | audit | 审核 | audit | 2 | 3 | 1 | 1 |
| 7 | `b599405400001dac` | unaudit | 反审核 | unaudit | 2 | 3 | 1 | 1 |
| 8 | `b599405400001eac` | disable | 禁用 | disable | 2 | 1 | 1 | 1 |
| 9 | `b599405400001fac` | enable | 启用 | enable | 2 | 1 | 1 | 1 |
| 10 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 1 |
| 11 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 1 |
| 12 | `f381c03f00002cac` | submit | 提交 | submit | 8 | 5 | 1 | 1 |
| 13 | `f381c03f00002dac` | unsubmit | 撤销 | unsubmit | 2 | 2 | 1 | 1 |
| 14 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 1 |
| 15 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 1 |
| 16 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 1 |
| 17 | `f31a499a0000adac` | submitandnew | 提交并新增 | submitandnew | 0 | 2 | 1 | 1 |
| 18 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 1 | 0 | 1 | 1 |
| 19 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 1 |
| 20 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 1 |
| 21 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 1 |
| 22 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 1 |
| 23 | `VZIV=BOH9KA` | importdata | 导入数据 | importdata | 0 | 0 | 1 | 1 |
| 24 | `VZIWYQT9HE3` | importdetails | 查看导入结果 | importdetails | 0 | 0 | 1 | 1 |
| 25 | `VZIX=AIFOYM` | importtemplatelist | 管理模板 | importtemplatelist | 0 | 0 | 1 | 1 |
| 26 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 1 |
| 27 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 1 |
| 28 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 1 |
| 29 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 1 |
| 30 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 1 |
| 31 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 1 |
| 32 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 1 |
| 33 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 1 |
| 34 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 1 |
| 35 | `QQIJBVE9I53` | assign | 分配 | assign | 0 | 0 | 1 | 1 |
| 36 | `QQIKI82+UGS` | unassign | 取消分配 | unassign | 0 | 0 | 1 | 1 |
| 37 | `/IRIWSL8=1NN` | donothing | 变更控制策略 | bdctrlchange | 0 | 0 | 1 | 1 |
| 38 | `0D=RW=4KI8NO` | donothing | 个性化数据 | individuation | 0 | 0 | 1 | 1 |
| 39 | `329LC79ICFLQ` | donothing | 分配 | assign_new | 0 | 0 | 1 | 1 |
| 40 | `3A=00+Z5C=1/` | donothing | 自动分配管理 | auto_assign | 0 | 0 | 1 | 1 |
| 41 | `3ACNJE3KTLUM` | donothing | Excel导入分配关系 | tbl_assign_import | 0 | 0 | 1 | 1 |
| 42 | `21A5TBYHHMR1` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 1 |
| 43 | `2COQS595DGF4` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 1 |
| 44 | `36NOAN=5C=5R` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 1 |
| 45 | `36NOB=YZ+BQ2` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 1 |
| 46 | `36NOD8YTK1KF` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 1 |
| 47 | `36NOF+L92D7L` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 1 |
| 48 | `36NOGFYHBSHM` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 1 |
| 49 | `36NOHKKHH8XN` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 1 |
| 50 | `20L976W1=2+6` | donothing | 变更 | change | 0 | 0 | 1 | 1 |
| 51 | `20L99=X4T4A1` | donothing | 历史版本信息 | hisversioninfo | 0 | 0 | 1 | 1 |
| 52 | `20L9N4UP7C2Z` | donothing | 复制 | hiscopy | 0 | 0 | 1 | 1 |
| 53 | `23LL1ORTM161` | donothing | 管理权转让 | orgpermchange | 0 | 0 | 1 | 1 |
| 54 | `2BL9SXB9NB9L` | donothing | 新增数据版本 | newhisversion | 0 | 0 | 1 | 1 |
| 55 | `2NY/AWHX6FHV` | donothing | 修订 | revise | 0 | 0 | 1 | 1 |
| 56 | `2NY/C=9+=WDQ` | donothing | 版本修订历史 | reviserecord | 0 | 0 | 1 | 1 |
| 57 | `2P7PHECVN6V7` | donothing | 版本对比 | versionchangecompare | 0 | 0 | 1 | 1 |
| 58 | `2XZ4DGE3VXV/` | donothing | 查看所有版本 | showallversion | 0 | 0 | 1 | 1 |
| 59 | `4YH8Y+XI1/D/` | save | 确认变更 | confirmchange | 2 | 3 | 1 | 1 |
| 60 | `34EYI219BBEQ` | newentry | 增行 | newentry_fw | 0 | 0 | 1 | 1 |
| 61 | `34EYOB2RL95V` | deleteentry | 删除 | deleteentry_fw | 0 | 0 | 1 | 1 |
| 62 | `34EYS4884J9S` | moveentryup | 上移 | moveentryup_fw | 0 | 0 | 1 | 1 |
| 63 | `34EYV+PGFUPG` | moveentrydown | 下移 | moveentrydown_fw | 0 | 0 | 1 | 1 |
| 64 | `34EZ/36T6FXT` | donothing | 添加 | subaddentry | 0 | 0 | 1 | 1 |
| 65 | `34EZ2DOSQUKI` | deleteentry | 删行 | subdeleteentry | 0 | 0 | 1 | 1 |
| 66 | `34EZ4J3XTPIH` | moveentryup | 上移 | submoveentryup | 0 | 0 | 1 | 1 |
| 67 | `34EZ68S0HH/4` | moveentrydown | 下移一行 | submoveentrydown | 0 | 0 | 1 | 1 |
| 68 | `3HYM2GYLVQSC` | newentry | 增行 | newentry_nag | 0 | 0 | 1 | 1 |
| 69 | `3HYM7+LD8Z79` | deleteentry | 删除 | deleteentry_ng | 0 | 0 | 1 | 1 |
| 70 | `3HYM8I098VEN` | moveentryup | 上移 | moveentryup_ng | 0 | 0 | 1 | 1 |
| 71 | `3HYM9O1YEHA3` | moveentrydown | 下移 | moveentrydown_ng | 0 | 0 | 1 | 1 |
| 72 | `3HZ2U8LLJ8/S` | newentry | 增行 | newentry_all | 0 | 0 | 1 | 1 |
| 73 | `3HZ2VD3MQZXH` | deleteentry | 删除 | deleteentry_all | 0 | 0 | 1 | 1 |
| 74 | `3HZ2WJ0/ATM5` | moveentryup | 上移 | moveentryup_all | 0 | 0 | 1 | 1 |
| 75 | `3HZ2XNRXP02Q` | moveentrydown | 下移 | moveentrydown_all | 0 | 0 | 1 | 1 |
| 76 | `2K4Z8NSD1NP/` | close | 关闭 | close | 0 | 0 | 1 | 1 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSavePlugin` |  |
| 3 | 2 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 4 | 3 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 5 | 5 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 6 | 6 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 7 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验插件 |
| 8 | 8 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 9 | 9 | `hbss_capacityitem` | `kd.hr.hbss.opplugin.web.capacity.CapacityItemOp` | kd.hr.hbss.opplugin.web.capacity.CapacityItemOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `0+/RWGT811DG` | 创建组织和编码组合字段唯一性校验 | `bos_basegrouporgtpl` |
| 4 | GrpfieldsuniqueValidation | `43+S9HG+XF3O` | 名称字段唯一性校验 | `hbss_capacityitem` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `bos_basetpl` | `kd.bos.coderule.CodeRuleDeleteOp` |  |
| 2 | 2 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 4 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 4 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDeletePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000016ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000000ac` | 数据已经禁用，不能删除。 | `bos_basetpl` |
| 3 | CustValidation | `2VJ7IPV6785T` | HR基础资料删除校验 | `hbp_bd_grouporgtpl_all` |

---
### 6. 审核（audit / Key: audit / oid: b599405400001cac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 2 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000018ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000bfac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BRWU+MXP7` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 7. 反审核（unaudit / Key: unaudit / oid: b599405400001dac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnAuditPlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000019ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f2843bab0000c0ac` | 合法性校验 | `bos_basetpl` |
| 3 | CustValidation | `2W/BU/L+T3I2` | 基础资料在流程中校验 | `bos_basetpl` |

---
### 8. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataDisablePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000baac` | 合法性校验 | `bos_basetpl` |

---
### 9. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataEnablePlugin` |  |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

---
### 12. 提交（submit / Key: submit / oid: f381c03f00002cac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 1 | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataSubmitPlugin` |  |
| 4 | 2 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 5 | 4 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 6 | 5 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验器 |
| 7 | 6 | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 8 | 7 | `hbss_capacityitem` | `kd.hr.hbss.opplugin.web.capacity.CapacityItemOp` | kd.hr.hbss.opplugin.web.capacity.CapacityItemOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `1cc0054f000017ac` | 合法性校验 | `bos_basetpl` |
| 2 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 3 | MustInputValidation | `RS=E9QE25UN` | 字段值合规性校验 | `bos_basetpl` |
| 4 | GrpfieldsuniqueValidation | `createorg.id` | 创建组织和编码组合字段唯一性校验 | `bos_basegrouporgtpl` |
| 5 | GrpfieldsuniqueValidation | `43+SCI7/T4S3` | 名称字段唯一性校验 | `hbss_capacityitem` |

---
### 13. 撤销（unsubmit / Key: unsubmit / oid: f381c03f00002dac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basegrouporgtpl` | `kd.bos.form.plugin.bdctrl.BaseDataUnSubmitPlugin` | 受控基础资料撤销插件 |
| 2 | — | `hbp_bd_grouporgtpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `3bdba4470000d3ac` | 合法性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `f789ca66000001ac` | 合法性校验 | `bos_basetpl` |

---
### 17. 提交并新增（submitandnew / Key: submitandnew / oid: f31a499a0000adac）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | GrpfieldsuniqueValidation | `number` | 组合字段唯一性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `2f5773ca0001b6ac` | 合法性校验 | `bos_basetpl` |

---
### 18. 保存并新增（saveandnew / Key: saveandnew / oid: QQKYA+LT1/4）
> 根定义: `bos_basetpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basegrouporgtpl` | `kd.bd.assistant.plugin.basedata.BaseDataSavePlugin` |  |

---
### 59. 确认变更（save / Key: confirmchange / oid: 4YH8Y+XI1/D/）
> 根定义: `hbp_hisbugrptimeseqtpl` | 涉及实体 (1): `hbss_capacityitem`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisModelOPCommonPlugin` | 历史模型OP操作通用插件 |
| 2 | 1 | `hbp_hisbugrptimeseqtpl` | `kd.hr.hbp.opplugin.web.history.HisUniqueValidateOp` | 历史模型唯一性校验插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4YH8S97/77YP` | 字段值合规性校验 | `hbp_hisbugrptimeseqtpl` |
| 2 | GrpfieldsuniqueValidation | `4YH8TN9A2966` | 名称唯一性校验 | `hbp_hisbugrptimeseqtpl` |
| 3 | GrpfieldsuniqueValidation | `4YH8VH+NG7/N` | 创建组织字段和编码唯一性校验 | `hbp_hisbugrptimeseqtpl` |

