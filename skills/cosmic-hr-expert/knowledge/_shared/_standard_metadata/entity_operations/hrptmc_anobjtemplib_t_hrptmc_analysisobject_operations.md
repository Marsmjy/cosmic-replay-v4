# hrptmc_anobjtemplib_t_hrptmc_analysisobject 系列 — 操作清单（合并去重）
**涵盖实体数**: 2  **去重后操作数**: 35
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```hbp_bd_originaltpl (HR原生基础资料模板)    └── hrptmc_analyseobject (分析对象)        └── hrptmc_anobjtemplib (分析对象模板库)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | 0 | 0 | 1 | 2 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | 0 | 0 | 1 | 2 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | 0 | 0 | 1 | 2 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | 0 | 0 | 1 | 2 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | 0 | 0 | 1 | 2 |
| 6 | `36XNX2J0IYJ/` | importdata_hr | 导入数据 | importdata_hr | 0 | 0 | 1 | 2 |
| 7 | `36XNYXBDN8US` | show_import_record_hr | 查看导入记录 | show_import_record_hr | 0 | 0 | 1 | 2 |
| 8 | `36XO+0YA=NQ1` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 2 |
| 9 | `36XO/49Y36PX` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 2 |
| 10 | `36XO07P0TYXD` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 2 |
| 11 | `36XO16LFA2/E` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 2 |
| 12 | `2UU=AIF3ZPNE` | donothing | 下一步 | nextstep | 0 | 2 | 1 | 2 |
| 13 | `2UU=CGZF=LW5` | donothing | 上一步 | laststep | 0 | 0 | 1 | 2 |
| 14 | `2UU=DWNNYXR2` | donothing | 数据预览 | previewdata | 0 | 0 | 1 | 2 |
| 15 | `2VA43AWR7JFO` | save | 保存 | save | 2 | 2 | 1 | 2 |
| 16 | `2VA47NH4C234` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 2 |
| 17 | `2VA4=2JPKSV3` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 2 |
| 18 | `2VA4BHCG4MHD` | enable | 启用 | enable | 1 | 1 | 1 | 2 |
| 19 | `2VA50=T=LZ6H` | disable | 禁用 | disable | 1 | 1 | 1 | 2 |
| 20 | `2VA5LU442J7Z` | donothing | 控权维度 | controlperm | 0 | 0 | 1 | 2 |
| 21 | `2VA5V7T9C2V1` | donothing | 同步策略 | syncstrategy | 0 | 0 | 1 | 2 |
| 22 | `2VY/WPCAS4PH` | delete | 删除 | delete | 2 | 2 | 1 | 2 |
| 23 | `2YD97AXGWWN9` | modify | 修改 | modify | 0 | 0 | 1 | 2 |
| 24 | `3+99A4B09CA7` | donothing | 导出配置 | exportconfig | 0 | 0 | 1 | 2 |
| 25 | `32U5CQ=6SDA3` | new | 新增 | new | 0 | 0 | 1 | 2 |
| 26 | `358CNHWZX9KX` | donothing | 预览数据 | pivotpreview | 0 | 0 | 1 | 2 |
| 27 | `358I3=8=I47V` | donothing | 显示维度值面板 | showdimvalflex | 0 | 0 | 1 | 2 |
| 28 | `358I5XZ42LMW` | donothing | 显示指标面板 | showindexflex | 0 | 0 | 1 | 2 |
| 29 | `35GMX9RH=JD/` | donothing | 生成新指标 | generatenewindex | 0 | 0 | 1 | 2 |
| 30 | `3MGVGMKHM=UL` | donothing | 数据控权规则配置 | configdataperm | 0 | 0 | 1 | 2 |
| 31 | `3P2/R6W53UI5` | donothing | 导入配置 | importconfig | 0 | 0 | 1 | 2 |
| 32 | `3TP/2SU755YF` | donothing | 复制分析对象 | copyanobj | 0 | 0 | 1 | 2 |
| 33 | `4/PX4A8H5MXJ` | donothing | 从模板库导入 | importbytemplatelib | 0 | 0 | 1 | 2 |
| 34 | `4/WPWN2QQDPA` | donothing | 数据抽取 | dataextract | 0 | 0 | 1 | 2 |
| 35 | `43IV47U45W3Y` | donothing | 导出SQL | exportconfigsql | 0 | 0 | 1 | 2 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 12. 下一步（donothing / Key: nextstep / oid: 2UU=AIF3ZPNE）
> 根定义: `hrptmc_analyseobject` | 涉及实体 (2): `hrptmc_analyseobject`, `hrptmc_anobjtemplib`
**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2VXXVC0LLZ8O` | 字段值合规性校验 | `hrptmc_analyseobject` |
| 2 | GrpfieldsuniqueValidation | `2Y3Y73/55LTP` | 编码唯一性校验 | `hrptmc_analyseobject` |

---
### 15. 保存（save / Key: save / oid: 2VA43AWR7JFO）
> 根定义: `hrptmc_analyseobject` | 涉及实体 (2): `hrptmc_analyseobject`, `hrptmc_anobjtemplib`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_analyseobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 中台日志插件 |
| 2 | 1 | `hrptmc_analyseobject` | `kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp` | kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2VA4+ZV3AW9Q` | 字段值合规性校验 | `hrptmc_analyseobject` |
| 2 | GrpfieldsuniqueValidation | `2VA41IHR=C1Y` | 编码唯一性校验 | `hrptmc_analyseobject` |

---
### 18. 启用（enable / Key: enable / oid: 2VA4BHCG4MHD）
> 根定义: `hrptmc_analyseobject` | 涉及实体 (2): `hrptmc_analyseobject`, `hrptmc_anobjtemplib`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_analyseobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 中台模板日志插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `2VA4P7L7NU37` | 合法性校验 | `hrptmc_analyseobject` |

---
### 19. 禁用（disable / Key: disable / oid: 2VA50=T=LZ6H）
> 根定义: `hrptmc_analyseobject` | 涉及实体 (2): `hrptmc_analyseobject`, `hrptmc_anobjtemplib`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_analyseobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 中台模板日志插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `2VA5+YJEB91B` | 合法性校验 | `hrptmc_analyseobject` |

---
### 22. 删除（delete / Key: delete / oid: 2VY/WPCAS4PH）
> 根定义: `hrptmc_analyseobject` | 涉及实体 (2): `hrptmc_analyseobject`, `hrptmc_anobjtemplib`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrptmc_analyseobject` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | 中台日志插件 |
| 2 | 1 | `hrptmc_analyseobject` | `kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp` | kd.hr.hrptmc.opplugin.web.anobj.ReportAnalyseObjectOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `2VY0D4XX7E=1` | 数据已经禁用，不能删除。 | `hrptmc_analyseobject` |
| 2 | CustValidation | `2VY0DV/UYE6K` | HR基础资料删除校验 | `hrptmc_analyseobject` |

