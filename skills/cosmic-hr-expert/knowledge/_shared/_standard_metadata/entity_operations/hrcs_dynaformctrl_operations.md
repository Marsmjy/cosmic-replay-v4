# hrcs_dynaformctrl — 虚字段数据控权配置 操作清单
**继承链**: `hbp_bd_originaltpl` → `hrcs_dynaformctrl`  **操作数**: 17
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originaltpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originaltpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originaltpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originaltpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originaltpl` | 1 | 3 |
| 6 | `36XNX2J0IYJ/` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 7 | `36XNYXBDN8US` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 8 | `36XO+0YA=NQ1` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 9 | `36XO/49Y36PX` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 10 | `36XO07P0TYXD` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 11 | `36XO16LFA2/E` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originaltpl` | 0 | 0 |
| 12 | `2XB7VTH2H=DC` | newentry | 增行 | newentry | `hrcs_dynaformctrl` | 0 | 0 |
| 13 | `2XB7XSIRY6HF` | donothing | 删行 | deleteentry | `hrcs_dynaformctrl` | 0 | 0 |
| 14 | `2XS0JMUPDR45` | delete | 删除 | delete | `hrcs_dynaformctrl` | 1 | 0 |
| 15 | `2XS0L7LABSX0` | new | 新增 | new | `hrcs_dynaformctrl` | 0 | 0 |
| 16 | `2XS0N+R8MDCO` | modify | 修改 | modify | `hrcs_dynaformctrl` | 0 | 0 |
| 17 | `33SIR45PO/X9` | donothing | 生成脚本 | generatesql | `hrcs_dynaformctrl` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originaltpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_dynaformctrl` | `kd.hr.hrcs.opplugin.web.perm.DynaFormCtrlSaveOp` | kd.hr.hrcs.opplugin.web.perm.DynaFormCtrlSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `2XOFPXEH4+6P` | 字段值合规性校验 | `hrcs_dynaformctrl` |
| 2 | GrpfieldsuniqueValidation | `2XOFR537Y2J9` | 组合字段唯一性校验 | `hrcs_dynaformctrl` |
| 3 | GrpfieldsuniqueValidation | `2XP6NWKRRAAV` | 组合字段唯一性校验 | `hrcs_dynaformctrl` |

---
### 14. 删除（delete / Key: delete / oid: 2XS0JMUPDR45）
> 根定义: `hrcs_dynaformctrl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_dynaformctrl` | `kd.hr.hrcs.opplugin.web.perm.DynamicFormCtrlDelOp` | kd.hr.hrcs.opplugin.web.perm.DynamicFormCtrlDelOp |

