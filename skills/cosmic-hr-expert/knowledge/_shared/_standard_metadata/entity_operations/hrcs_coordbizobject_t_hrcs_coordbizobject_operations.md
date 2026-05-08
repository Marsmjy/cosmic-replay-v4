# hrcs_coordbizobject_t_hrcs_coordbizobject 系列 — 操作清单（合并去重）
**涵盖实体数**: 8  **去重后操作数**: 39
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── hbp_bd_tpl_all (HR基础资料全页面模板)        └── hrcs_coordbizobject (协作业务对象)            ├── hrcs_coordbizobject11 (协作字段配置_薪资档案)            ├── hrcs_coordbizobject12 (协作字段配置_定调薪档案)            ├── hrcs_coordbizobject13 (协作字段配置_社保档案)            ├── hrcs_coordbizobject14 (协作字段配置_个税档案)            ├── hrcs_coordbizobject51 (协作字段配置)            ├── hrcs_coordbizobject61 (协作字段配置_绩效档案)            └── hrcs_coordbizobject81 (协作业务对象_人才档案)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 8 |
| 2 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 8 |
| 3 | `b599405400001aac` | save | 保存 | save | 7 | 4 | 1 | 8 |
| 4 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 8 |
| 5 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 8 |
| 6 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 8 |
| 7 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 8 |
| 8 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 8 |
| 9 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 8 |
| 10 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 8 |
| 11 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 8 |
| 12 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 8 |
| 13 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 8 |
| 14 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 8 |
| 15 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 8 |
| 16 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 8 |
| 17 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 8 |
| 18 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 8 |
| 19 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 8 |
| 20 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 8 |
| 21 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 8 |
| 22 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 8 |
| 23 | `4YEXU2J/WZ3V` | newentry | 添加字段分组 | newgroup | 0 | 0 | 1 | 8 |
| 24 | `4YEXX3=55WMK` | newentry | 添加字段 | newfield | 0 | 0 | 1 | 8 |
| 25 | `4YEYK/JATV0=` | deleteentry | 删除字段分组 | deletegroup | 0 | 0 | 1 | 8 |
| 26 | `4YEYSGJ0LN=5` | deleteentry | 删除字段 | deletefield | 0 | 0 | 1 | 8 |
| 27 | `4YEYV56+ZVBN` | moveentryup | 上移字段分组 | movegroupup | 0 | 0 | 1 | 8 |
| 28 | `4YEYYV2Q6CRB` | moveentryup | 上移字段 | movefieldup | 0 | 0 | 1 | 8 |
| 29 | `4YEZ/4FETYF3` | moveentrydown | 下移字段分组 | movegroupodown | 0 | 0 | 1 | 8 |
| 30 | `4YEZ1QFHVI6I` | moveentrydown | 下移字段 | movefielddown | 0 | 0 | 1 | 8 |
| 31 | `4ZQUHNP=540M` | donothing | 新增 | newbase | 0 | 0 | 1 | 8 |
| 32 | `5+28GWYSD85X` | donothing | 添加 | newprop | 0 | 0 | 1 | 8 |
| 33 | `5COCV/9+8IW6` | enable | 启用 | enable | 1 | 1 | 1 | 8 |
| 34 | `5COCVO2JL29D` | disable | 禁用 | disable | 1 | 2 | 1 | 8 |
| 35 | `5COCXQF2NBX7` | submit | 提交 | submit | 4 | 4 | 1 | 8 |
| 36 | `5COCYU1=W=N6` | unaudit | 反审核 | unaudit | 1 | 2 | 1 | 8 |
| 37 | `5COCZI=J9XI5` | audit | 审核 | audit | 1 | 2 | 1 | 8 |
| 38 | `5COD0Y90WMEP` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 8 |
| 39 | `5COFU3P19EJ4` | delete | 删除 | delete | 3 | 2 | 1 | 8 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 3. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 6 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |
| 7 | 6 | `hrcs_coordbizobject` | `kd.hr.hrcs.opplugin.web.coordination.CoordBizObjOp` | 协作业务对象操作 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 33. 启用（enable / Key: enable / oid: 5COCV/9+8IW6）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COFY+H/KTCK` | 合法性校验 | `hrcs_coordbizobject` |

---
### 34. 禁用（disable / Key: disable / oid: 5COCVO2JL29D）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COG087+XCX0` | 合法性校验 | `hrcs_coordbizobject` |
| 2 | ConditionValidation | `5COG1G4TZA7L` | 合法性校验 | `hrcs_coordbizobject` |

---
### 35. 提交（submit / Key: submit / oid: 5COCXQF2NBX7）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.bos.business.plugin.CodeRuleOp` | kd.bos.business.plugin.CodeRuleOp |
| 2 | 1 | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 3 | 2 | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp |
| 4 | 3 | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COLL/45526X` | 合法性校验 | `hrcs_coordbizobject` |
| 2 | GrpfieldsuniqueValidation | `5COLLRLAUUDL` | 组合字段唯一性校验 | `hrcs_coordbizobject` |
| 3 | MustInputValidation | `5COLMG3QEB5W` | 字段值合规性校验 | `hrcs_coordbizobject` |
| 4 | GrpfieldsuniqueValidation | `5COLNQ+8VPGC` | 名称唯一性校验 | `hrcs_coordbizobject` |

---
### 36. 反审核（unaudit / Key: unaudit / oid: 5COCYU1=W=N6）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COLX5IYAQGW` | 合法性校验 | `hrcs_coordbizobject` |
| 2 | ConditionValidation | `5COLY2RZQW5Q` | 合法性校验 | `hrcs_coordbizobject` |

---
### 37. 审核（audit / Key: audit / oid: 5COCZI=J9XI5）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COM/K4CMYN7` | 合法性校验 | `hrcs_coordbizobject` |
| 2 | ConditionValidation | `5COM5MQ0O4HH` | 合法性校验 | `hrcs_coordbizobject` |

---
### 39. 删除（delete / Key: delete / oid: 5COFU3P19EJ4）
> 根定义: `hrcs_coordbizobject` | 涉及实体 (8): `hrcs_coordbizobject`, `hrcs_coordbizobject11`, `hrcs_coordbizobject12`, `hrcs_coordbizobject13`, `hrcs_coordbizobject14`, `hrcs_coordbizobject51`, `hrcs_coordbizobject61`, `hrcs_coordbizobject81`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_coordbizobject` | `kd.bos.coderule.CodeRuleDeleteOp` | kd.bos.coderule.CodeRuleDeleteOp |
| 2 | 1 | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 3 | 2 | `hrcs_coordbizobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `5COFSOU2LWXO` | 数据已经禁用，不能删除。 | `hrcs_coordbizobject` |
| 2 | CustValidation | `5COFTWROKRIC` | HR基础资料删除校验 | `hrcs_coordbizobject` |

