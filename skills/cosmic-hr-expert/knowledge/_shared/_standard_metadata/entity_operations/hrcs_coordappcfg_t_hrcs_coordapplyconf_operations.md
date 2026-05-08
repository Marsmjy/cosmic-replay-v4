# hrcs_coordappcfg_t_hrcs_coordapplyconf 系列 — 操作清单（合并去重）
**涵盖实体数**: 8  **去重后操作数**: 33
---
## 1. 继承树
以下展示目标实体的继承关系（缩进表示父子关系）：
```bos_basetpl (基础资料模板)    └── hbp_bd_tpl_all (HR基础资料全页面模板)        └── hbp_coordappcfgtpl (协作应用配置模板)            └── hrcs_coordappcfg (协作应用配置)                ├── hrcs_coordappcfg51 ()                ├── hrcs_coordappcfg61 (协作应用配置_绩效档案)                ├── hrcs_coordappcfg81 (协作应用配置_人才档案)                ├── hrcs_coordapplyconf11 (协作应用配置_薪资档案)                ├── hrcs_coordapplyconf12 (协作应用配置_定调薪档案)                ├── hrcs_coordapplyconf13 (协作应用配置_社保档案)                └── hrcs_coordapplyconf14 (协作应用配置_个税档案)```
---
## 2. 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 插件数 | 校验器数 | 变体数 | 涉及实体数 |
|---|-----|---------|---------|-----|--------|--------|--------|----------|
| 1 | `b5994054000016ac` | new | 新增 | new | 0 | 0 | 1 | 8 |
| 2 | `b5994054000017ac` | modify | 修改 | modify | 0 | 0 | 1 | 8 |
| 3 | `b5994054000018ac` | view | 查看 | view | 0 | 0 | 1 | 8 |
| 4 | `b599405400001aac` | save | 保存 | save | 6 | 4 | 1 | 8 |
| 5 | `b599405400001bac` | delete | 删除 | delete | 3 | 3 | 1 | 8 |
| 6 | `b599405400001eac` | disable | 禁用 | disable | 1 | 2 | 1 | 8 |
| 7 | `b599405400001fac` | enable | 启用 | enable | 1 | 1 | 1 | 8 |
| 8 | `b5994054000021ac` | close | 关闭 | close | 0 | 0 | 1 | 8 |
| 9 | `c54e30e7000010ac` | returndata | 返回数据 | returndata | 0 | 0 | 1 | 8 |
| 10 | `f381c03f000033ac` | copy | 复制 | copy | 0 | 0 | 1 | 8 |
| 11 | `f381c03f000034ac` | refresh | 刷新 | refresh | 0 | 0 | 1 | 8 |
| 12 | `c8d21820000086ac` | option | 选项设置 | option | 0 | 0 | 1 | 8 |
| 13 | `QQKYA+LT1/4` | saveandnew | 保存并新增 | saveandnew | 0 | 0 | 1 | 8 |
| 14 | `S=VTOGXYQBY` | first | 第一 | first | 0 | 0 | 1 | 8 |
| 15 | `S=VUN3Y4R6Q` | previous | 前一 | previous | 0 | 0 | 1 | 8 |
| 16 | `S=VVFX/B38V` | next | 后一 | next | 0 | 0 | 1 | 8 |
| 17 | `S=VW0K6WD7R` | last | 最后 | last | 0 | 0 | 1 | 8 |
| 18 | `10MEU32VZDCP` | exportlist | 导出数据（按导入模板） | exportlist | 0 | 0 | 1 | 8 |
| 19 | `13NUAIBA368/` | exportlist | 导出数据（按列表） | exportlistbyselectfields | 0 | 0 | 1 | 8 |
| 20 | `/J6LFFUQC1K0` | exportlist_expt | 导出数据（按导出模板） | exportlist_expt | 0 | 0 | 1 | 8 |
| 21 | `0H1RYY0PPC=0` | exportdetails | 查看导出结果 | exportdetails | 0 | 0 | 1 | 8 |
| 22 | `1V3RLZ3IDN4R` | importexport_userset | 导入导出个性化设置 | importexport_userset | 0 | 0 | 1 | 8 |
| 23 | `2MMZBM6FC174` | mobtoolbarselect | 选择 | mobtoolbarselect | 0 | 0 | 1 | 8 |
| 24 | `2MMZF4V/MARY` | mobtoolbarcancel | 取消 | mobtoolbarcancel | 0 | 0 | 1 | 8 |
| 25 | `2PVU302I5+FH` | donothing | 改名 | namehistory | 0 | 0 | 1 | 8 |
| 26 | `2PVU5DLAO/49` | donothing | 名称历史查询 | namehistoryview | 0 | 0 | 1 | 8 |
| 27 | `21A5O/3PV7IL` | donothing | 查看全部日志 | logview | 0 | 0 | 1 | 8 |
| 28 | `2COQ5049N75G` | donothing | 查看日志 | viewonelog | 0 | 0 | 1 | 8 |
| 29 | `36NN1BHX+R/A` | export_from_list_hr | 按列表导出 | export_from_list_hr | 0 | 0 | 1 | 8 |
| 30 | `36NN2SRUE7N/` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | 0 | 0 | 1 | 8 |
| 31 | `36NN3ZT9PZNE` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | 0 | 0 | 1 | 8 |
| 32 | `36NN590J0H1R` | show_export_record_hr | 查看导出记录 | show_export_record_hr | 0 | 0 | 1 | 8 |
| 33 | `4ZJ844XN21NU` | donothing | 新增 | add | 0 | 0 | 1 | 8 |
---
## 3. 操作详情（仅展示有插件或校验器的操作）
---
### 4. 保存（save / Key: save / oid: b599405400001aac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordappcfg`, `hrcs_coordappcfg51`, `hrcs_coordappcfg61`, `hrcs_coordappcfg81`, `hrcs_coordapplyconf11`, `hrcs_coordapplyconf12`, `hrcs_coordapplyconf13`, `hrcs_coordapplyconf14`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `bos_basetpl` | `kd.bos.business.plugin.CodeRuleOp` |  |
| 2 | 1 | `bos_basetpl` | `kd.bos.base.bdversion.BdVersionSaveServicePlugin` | 基础资料版本化保存操作插件 |
| 3 | 2 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataStatusOp |
| 4 | 3 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |
| 5 | 4 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataEnableOp` | 控制使用状态 |
| 6 | 5 | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp` | kd.hr.hbp.opplugin.web.config.HRBaseOriginalOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `6096194600001fac` | 字段值合规性校验 | `bos_basetpl` |
| 2 | ConditionValidation | `1VRALXJOVNKD` | 合法性校验 | `bos_basetpl` |
| 3 | GrpfieldsuniqueValidation | `2+R3Y9FBSJ51` | 编码唯一性校验 | `hbp_bd_tpl_all` |
| 4 | GrpfieldsuniqueValidation | `2+R3ZR7WI4N2` | 名称唯一性校验 | `hbp_bd_tpl_all` |

---
### 5. 删除（delete / Key: delete / oid: b599405400001bac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordappcfg`, `hrcs_coordappcfg51`, `hrcs_coordappcfg61`, `hrcs_coordappcfg81`, `hrcs_coordapplyconf11`, `hrcs_coordapplyconf12`, `hrcs_coordapplyconf13`, `hrcs_coordapplyconf14`
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
### 6. 禁用（disable / Key: disable / oid: b599405400001eac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordappcfg`, `hrcs_coordappcfg51`, `hrcs_coordappcfg61`, `hrcs_coordappcfg81`, `hrcs_coordapplyconf11`, `hrcs_coordapplyconf12`, `hrcs_coordapplyconf13`, `hrcs_coordapplyconf14`
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
### 7. 启用（enable / Key: enable / oid: b599405400001fac）
> 根定义: `bos_basetpl` | 涉及实体 (8): `hrcs_coordappcfg`, `hrcs_coordappcfg51`, `hrcs_coordappcfg61`, `hrcs_coordappcfg81`, `hrcs_coordapplyconf11`, `hrcs_coordapplyconf12`, `hrcs_coordapplyconf13`, `hrcs_coordapplyconf14`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_tpl_all` | `kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp` | kd.hr.hbp.opplugin.web.config.HRBaseDataLogOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | ConditionValidation | `f2843bab0000bcac` | 合法性校验 | `bos_basetpl` |

