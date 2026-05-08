# hrcs_relateentity — 关联实体 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hrcs_relateentity`  **操作数**: 24
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 1 | 1 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `36X+718OUI6F` | donothing | 添加实体 | add | `hrcs_relateentity` | 0 | 0 |
| 13 | `36X+BEG/09KJ` | deleteentry | 删除分录 | delete | `hrcs_relateentity` | 0 | 0 |
| 14 | `36X+FH=UUBR9` | moveentryup | 上移一行 | moveentryup | `hrcs_relateentity` | 0 | 0 |
| 15 | `36X+I6/OLE3D` | moveentrydown | 下移一行 | moveentrydown | `hrcs_relateentity` | 0 | 0 |
| 16 | `36X+O7/18J0C` | newentry | 新增实体关联设置 | addentitymapping | `hrcs_relateentity` | 0 | 0 |
| 17 | `36X+RSC/H7ST` | deleteentry | 删除实体关联设置 | deleteentitymapping | `hrcs_relateentity` | 0 | 0 |
| 18 | `36X+VAFLAF8Y` | moveentryup | 上移一行 | mappingentitymoveup | `hrcs_relateentity` | 0 | 0 |
| 19 | `36X+XKN=Z008` | moveentrydown | 下移一行 | mappingentitymovedown | `hrcs_relateentity` | 0 | 0 |
| 20 | `36X/+BZXIP0S` | newentry | 新增关联字段 | addmappingfield | `hrcs_relateentity` | 0 | 0 |
| 21 | `36X/32C93UWX` | deleteentry | 删除关联字段 | deletemappingfield | `hrcs_relateentity` | 0 | 0 |
| 22 | `36X/56FDC0CA` | moveentryup | 上移一行 | mappingfieldmoveup | `hrcs_relateentity` | 0 | 0 |
| 23 | `36X/75ICJQHA` | moveentrydown | 下移一行 | mappingfieldmovedown | `hrcs_relateentity` | 0 | 0 |
| 24 | `36Y3AQRAZU0Q` | close | 关闭 | close | `hrcs_relateentity` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrcs_relateentity` | `kd.hr.hrcs.opplugin.web.EntityMappingSaveOp` | kd.hr.hrcs.opplugin.web.EntityMappingSaveOp |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `36XMX40QB6KL` | 字段值合规性校验 | `hrcs_relateentity` |

