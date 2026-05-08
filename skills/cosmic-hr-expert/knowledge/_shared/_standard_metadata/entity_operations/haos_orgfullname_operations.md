# haos_orgfullname — 组织全称 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hbp_bd_timelinemintpl` → `haos_orgfullname`  **操作数**: 16
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 2 | 1 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `4R/HR6LQCLZ1` | donothing | 复制 | copy | `hbp_bd_timelinemintpl` | 0 | 0 |
| 13 | `4R/HUQ+ARVEU` | donothing | 查看日志 | viewlog | `hbp_bd_timelinemintpl` | 0 | 0 |
| 14 | `4R39DC6NFRP7` | modify | 修订 | modify | `hbp_bd_timelinemintpl` | 0 | 0 |
| 15 | `4RIR/ALATGNY` | delete | 删除 | delete | `hbp_bd_timelinemintpl` | 2 | 0 |
| 16 | `4RJA//W+TN+N` | new | 新增 | new | `hbp_bd_timelinemintpl` | 0 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
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
### 15. 删除（delete / Key: delete / oid: 4RIR/ALATGNY）
> 根定义: `hbp_bd_timelinemintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板op插件 |
| 2 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |

