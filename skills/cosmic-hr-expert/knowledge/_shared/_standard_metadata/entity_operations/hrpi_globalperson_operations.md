# hrpi_globalperson — 全球员工 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hrpi_globalperson`  **操作数**: 12
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 2 | 0 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `4UHF282AQRS4` | delete | 删除 | delete | `hrpi_globalperson` | 2 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hrpi_globalperson` | `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp` | 标品字段必填Op |
| 2 | — | `hrpi_globalperson` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新变动事务记录 |

---
### 12. 删除（delete / Key: delete / oid: 4UHF282AQRS4）
> 根定义: `hrpi_globalperson`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_globalperson` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新变动事务记录 |
| 2 | 1 | `hrpi_globalperson` | `kd.hrmp.hrpi.opplugin.web.globalperson.GlobalPersonDeleteOp` | 全球员工删除插件 |

