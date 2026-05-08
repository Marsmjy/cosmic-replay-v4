# hrpi_dispatchinfo — 外派信息 操作清单
**继承链**: `hbp_bd_originalmintpl` → `hbp_bd_timelinemintpl` → `hrpi_assigntimelinetpl` → `hrpi_dispatchinfo`  **操作数**: 20
---
## 操作总览
| # | oid | 操作类型 | 操作名称 | Key | 根定义 | 插件数 | 校验器数 |
|---|-----|---------|---------|-----|--------|--------|--------|
| 1 | `/9AXL8BLRPMZ` | first | 第一 | first | `hbp_bd_originalmintpl` | 0 | 0 |
| 2 | `/9AXMFLGA=WK` | previous | 前一 | previous | `hbp_bd_originalmintpl` | 0 | 0 |
| 3 | `/9AXNHU=9=4S` | next | 后一 | next | `hbp_bd_originalmintpl` | 0 | 0 |
| 4 | `/9AXONQK5XN9` | last | 最后 | last | `hbp_bd_originalmintpl` | 0 | 0 |
| 5 | `/X9FTE5ABBM6` | save | 保存 | save | `hbp_bd_originalmintpl` | 8 | 2 |
| 6 | `36XN4LPFOIIF` | importdata_hr | 导入数据 | importdata_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 7 | `36XN5U/XQV52` | show_import_record_hr | 查看导入记录 | show_import_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 8 | `36XN7/RHUUE8` | export_from_list_hr | 按列表导出 | export_from_list_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 9 | `36XN8AH7/42R` | export_from_impttpl_hr | 按导入模板导出 | export_from_impttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 10 | `36XN9NHN4BV=` | export_from_expttpl_hr | 按导出模板导出 | export_from_expttpl_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 11 | `36XN=PH5GTV7` | show_export_record_hr | 查看导出记录 | show_export_record_hr | `hbp_bd_originalmintpl` | 0 | 0 |
| 12 | `4R/HR6LQCLZ1` | donothing | 复制 | copy | `hbp_bd_timelinemintpl` | 0 | 0 |
| 13 | `4R/HUQ+ARVEU` | donothing | 查看日志 | viewlog | `hbp_bd_timelinemintpl` | 0 | 0 |
| 14 | `4R39DC6NFRP7` | modify | 修订 | modify | `hbp_bd_timelinemintpl` | 0 | 0 |
| 15 | `4RIR/ALATGNY` | delete | 删除 | delete | `hbp_bd_timelinemintpl` | 5 | 0 |
| 16 | `4RJA//W+TN+N` | new | 新增 | new | `hbp_bd_timelinemintpl` | 0 | 0 |
| 17 | `4UNZ06CRZMST` | donothing | 放弃 | abandon | `hrpi_assigntimelinetpl` | 1 | 0 |
| 18 | `4V2MA=KN=QWK` | donothing | 修改 | modifyatt | `hrpi_assigntimelinetpl` | 0 | 0 |
| 19 | `541P1YK8HUDJ` | save | 保存 | saveatt | `hrpi_assigntimelinetpl` | 1 | 1 |
| 20 | `541VW9XU6W60` | delete | 删除 | deleteatt | `hrpi_assigntimelinetpl` | 2 | 0 |
---
## 操作详情（仅展示有插件或校验器的操作）
---
### 5. 保存（save / Key: save / oid: /X9FTE5ABBM6）
> 根定义: `hbp_bd_originalmintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |
| 2 | 2 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.template.TimeLineCommonSaveOp` | 时间轴通用操作 |
| 3 | 3 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.template.AssignmentTplCommonSaveOp` | 组织分配通用保存 |
| 4 | 4 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新变动事务记录 |
| 5 | 5 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin` | 雇佣阶段关联处理 |
| 6 | 5 | `hrpi_dispatchinfo` | `kd.hrmp.hrpi.opplugin.web.assignment.attach.DispatchSaveOp` | 外派信息插件 |
| 7 | 6 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.common.EmployeeCommonStandardMustInputOp` | 标品字段必填Op |
| 8 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板OP插件 |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `4R38557==XM/` | 字段值合规性校验 | `hbp_bd_timelinemintpl` |
| 2 | DynamicValidation | `5CS8UEP/R2CU` | 组织分配附表同一员工校验 | `hrpi_assigntimelinetpl` |

---
### 15. 删除（delete / Key: delete / oid: 4RIR/ALATGNY）
> 根定义: `hbp_bd_timelinemintpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.TimelineTplOp` | 时间轴模型模板op插件 |
| 2 | 1 | `hbp_bd_timelinemintpl` | `kd.hr.hbp.opplugin.web.timeline.log.TimelineLogOp` | 时间轴模型OP日志插件 |
| 3 | 2 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.chgrecord.ChgRecordSaveOp` | 操作成功后更新变动事务记录 |
| 4 | 3 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.assignment.EmpStageHandleOpPlugin` | 雇佣阶段关联处理 |
| 5 | 4 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp` | 附表删除忽略基础资料引用OP |

---
### 17. 放弃（donothing / Key: abandon / oid: 4UNZ06CRZMST）
> 根定义: `hrpi_assigntimelinetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assigntimelinetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 员工审核通用op插件 |

---
### 19. 保存（save / Key: saveatt / oid: 541P1YK8HUDJ）
> 根定义: `hrpi_assigntimelinetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assigntimelinetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审核OP |

**校验器**：
| # | 类型 | Id | 说明 | 来源 |
|---|-----|----|------|------|
| 1 | MustInputValidation | `541P4O8SRE/H` | 字段值合规性校验 | `hrpi_assigntimelinetpl` |

---
### 20. 删除（delete / Key: deleteatt / oid: 541VW9XU6W60）
> 根定义: `hrpi_assigntimelinetpl`
**插件清单**：
| # | RowKey | 来源层级 | 插件类全路径 | 说明 |
|---|--------|---------|------------|------|
| 1 | — | `hrpi_assigntimelinetpl` | `kd.sdk.hr.hspm.opplugin.reform.EmployeeAuditCommonOP` | 审核OP |
| 2 | 1 | `hrpi_assigntimelinetpl` | `kd.hrmp.hrpi.opplugin.web.template.IgnoreReferenceDeleteOp` | 附表删除忽略基础资料引用OP |

